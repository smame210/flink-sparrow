package com.sparrow.core.execution;

import com.alibaba.fastjson2.JSONObject;
import com.sparrow.common.enums.JobMode;
import com.sparrow.common.utils.JsonUtil;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.flink.api.common.ExecutionConfig;
import org.apache.flink.api.common.RuntimeExecutionMode;
import org.apache.flink.api.common.restartstrategy.RestartStrategies;
import org.apache.flink.api.common.time.Time;
import org.apache.flink.contrib.streaming.state.EmbeddedRocksDBStateBackend;
import org.apache.flink.streaming.api.CheckpointingMode;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.table.api.EnvironmentSettings;
import org.apache.flink.table.api.bridge.java.StreamTableEnvironment;

import java.util.concurrent.TimeUnit;

import static com.sparrow.core.constants.FlinkEnvironmentConstants.*;

/**
 * @author daitf
 * @date 2024/8/18
 */
@Slf4j
public class FlinkRuntimeEnvironment {
    private static volatile FlinkRuntimeEnvironment INSTANCE = null;

    private StreamExecutionEnvironment streamEnv;

    private StreamTableEnvironment tableEnv;

    @Getter
    private String jobName = DEFAULT_JOB_NAME;

    private JobMode jobMode = JobMode.STREAMING;

    private JSONObject config;

    @Getter
    private String JobType = DEFAULT_JOB_TYPE;

    private FlinkRuntimeEnvironment(JSONObject config) {
        this.config = config;
        // jobName
        if (config.containsKey(JOB_NAME)) {
            this.jobName = config.getString(JOB_NAME);
        }
        // jobMode
        if (config.containsKey(JOB_MODE)) {
            this.jobMode = JobMode.getJobMode(config.getString(JOB_MODE));
        }
        // jobType
        if (config.containsKey(JOB_TYPE)) {
            this.JobType = config.getString(JOB_TYPE);
        }
        // streamEnv
        createStreamEnvironment();
        // tableEnv
        createStreamTableEnvironment();
    }

    private void createStreamEnvironment() {
        streamEnv = StreamExecutionEnvironment.getExecutionEnvironment();
        int parallelism = config.containsKey(PARALLELISM) ? config.getInteger(PARALLELISM) : 1;
        streamEnv.setParallelism(parallelism);
        if (this.jobMode.equals(JobMode.BATCH)) {
            streamEnv.setRuntimeMode(RuntimeExecutionMode.BATCH);
        }
        setRestartStrategy(config, streamEnv.getConfig());
        setCheckpoint(config, streamEnv);
    }

    private void createStreamTableEnvironment() {
        EnvironmentSettings environmentSettings = EnvironmentSettings.newInstance()
                .inStreamingMode()
                .build();
        tableEnv = StreamTableEnvironment.create(getStreamExecutionEnvironment(), environmentSettings);
    }

    public StreamExecutionEnvironment getStreamExecutionEnvironment() {
        return streamEnv;
    }

    public StreamTableEnvironment getStreamTableEnvironment() {
        return tableEnv;
    }

    private void setRestartStrategy(JSONObject config, ExecutionConfig executionConfig) {
        try {
            if (JsonUtil.containsField(config, RESTART_STRATEGY)) {
                String restartStrategy = config.getString(RESTART_STRATEGY);
                switch (restartStrategy.toLowerCase()) {
                    case "never":
                        executionConfig.setRestartStrategy(RestartStrategies.noRestart());
                        break;
                    case "fixed-delay":
                        int attempts = config.getInteger(RESTART_ATTEMPTS);
                        long delay = config.getLong(RESTART_DELAY_BETWEEN_ATTEMPTS);
                        executionConfig.setRestartStrategy(RestartStrategies.fixedDelayRestart(attempts, delay));
                        break;
                    case "failure-rate":
                        long failureInterval = config.getLong(RESTART_FAILURE_INTERVAL);
                        int rate = config.getInteger(RESTART_FAILURE_RATE);
                        long delayInterval = config.getLong(RESTART_DELAY_INTERVAL);
                        executionConfig.setRestartStrategy(RestartStrategies.failureRateRestart(
                                rate,
                                Time.of(failureInterval, TimeUnit.MILLISECONDS),
                                Time.of(delayInterval, TimeUnit.MILLISECONDS)));
                        break;
                    default:
                        log.warn("unknown restart-strategy: [{}], just support [never, fixed-delay, failure-rate]", config);
                        throw new IllegalArgumentException("unknown restart-strategy in the config");
                }
            }
        } catch (Exception e) {
            log.warn("failed to set restart-strategy. the env config {}", config);
            throw new IllegalArgumentException(e);
        }
    }

    private void setCheckpoint(JSONObject config, StreamExecutionEnvironment streamEnv) {
        if (config.containsKey(CHECKPOINT_INTERVAL)) {
            streamEnv.enableCheckpointing((Long) JsonUtil.getField(config,CHECKPOINT_INTERVAL));
        }

        if (config.containsKey(CHECKPOINT_MODE)) {
            String mode = config.getString(CHECKPOINT_MODE);
            switch (mode.toLowerCase()) {
                case "exactly-once":
                    streamEnv.getCheckpointConfig().setCheckpointingMode(CheckpointingMode.EXACTLY_ONCE);
                    break;
                case "at-least-once":
                    streamEnv.getCheckpointConfig().setCheckpointingMode(CheckpointingMode.AT_LEAST_ONCE);
                    break;
                default:
                    log.warn("unknown checkpoint-mode: [{}], just support [exactly-once, at-least-once]", mode);
                    throw new IllegalArgumentException("unknown checkpoint-mode in the config");
            }
        }

        if (config.containsKey(STATE_BACKEND)) {
            // todo only support rocksdb state backend now.
            switch (config.getString(STATE_BACKEND).toLowerCase()) {
                case "rocksdb":
                    EmbeddedRocksDBStateBackend embeddedRocksDBStateBackend = new EmbeddedRocksDBStateBackend();
                    streamEnv.setStateBackend(embeddedRocksDBStateBackend);
                    break;
                default:
                    log.warn("unknown state-backend: [{}], just support [rocksdb]", config.getString(STATE_BACKEND));
                    throw new IllegalArgumentException("unknown state-backend in the config");
            }
        }
    }

    public static FlinkRuntimeEnvironment getInstance(JSONObject config) {
        if (INSTANCE == null) {
            synchronized (FlinkRuntimeEnvironment.class) {
                if (INSTANCE == null) {
                    INSTANCE = new FlinkRuntimeEnvironment(config);
                }
            }
        }
        return INSTANCE;
    }
}
