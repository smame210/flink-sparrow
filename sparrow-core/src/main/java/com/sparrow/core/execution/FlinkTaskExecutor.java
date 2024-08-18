package com.sparrow.core.execution;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IoUtil;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.sparrow.core.args.FlinkStarterArgs;
import com.sparrow.core.utils.ConfigCheckUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.flink.configuration.Configuration;

import java.io.FileReader;

import static com.sparrow.core.constants.ConfigConstants.*;

/**
 * @author daitf
 * @date 2024/8/18
 */
@Slf4j
public class FlinkTaskExecutor implements TaskExecutor {
    private final FlinkStarterArgs flinkStarterArgs;

    private FlinkRuntimeEnvironment flinkRuntimeEnvironment;

    public FlinkTaskExecutor(FlinkStarterArgs flinkStarterArgs) {
        this.flinkStarterArgs = flinkStarterArgs;
    }

    @Override
    public void execute() {
        JSONObject config = parseConfig();
        ConfigCheckUtil.checkConfig(config);
        flinkRuntimeEnvironment = FlinkRuntimeEnvironment.getInstance(config.getJSONObject(ENV));
        if ("sql".equalsIgnoreCase(flinkRuntimeEnvironment.getJobType())) {
            // flink sql job
            if (!config.containsKey("sql")) {
                throw new IllegalArgumentException("sql not found! config: " + config.toJSONString());
            }
            Configuration configuration = flinkRuntimeEnvironment.getStreamTableEnvironment().getConfig().getConfiguration();
            configuration.setString("pipeline.name", flinkRuntimeEnvironment.getJobName());
            String sqlContent = config.getString("sql");
            String[] sqlStatements = sqlContent.split(";");
            for (String sqlStatement : sqlStatements) {
                sqlStatement = sqlStatement.trim();
                if (!sqlStatement.isEmpty()) {
                    flinkRuntimeEnvironment.getStreamTableEnvironment()
                            .executeSql(sqlStatement);
                }
            }
        } else {
            PluginExecutor pluginExecutor = new PluginExecutor(config.getJSONObject("plugins"), flinkRuntimeEnvironment);
            pluginExecutor.execute();
            try {
                flinkRuntimeEnvironment.getStreamExecutionEnvironment()
                        .execute(StringUtils.isNotBlank(this.flinkStarterArgs.getJobName()) ?
                                this.flinkStarterArgs.getJobName() : flinkRuntimeEnvironment.getJobName());
            } catch (Exception e) {
                throw new RuntimeException("Execute Flink job error", e);
            }
        }
    }

    private JSONObject parseConfig() {
        JSONObject jsonObject;
        if (StringUtils.isNotBlank(flinkStarterArgs.getConfigFile())) {
            // config file
            boolean exist = FileUtil.exist(flinkStarterArgs.getConfigFile());
            if (!exist) {
                throw new IllegalArgumentException("config file not exist");
            }

            try(FileReader fr = new FileReader(flinkStarterArgs.getConfigFile())) {
                String content = IoUtil.read(fr);
                jsonObject = JSON.parseObject(content);
            } catch (Exception e) {
                log.error("config file format error", e);
                throw new IllegalArgumentException("config file format error");
            }
        } else if (StringUtils.isNotBlank(flinkStarterArgs.getConfigContent())) {
            // config content
            try {
                jsonObject = JSON.parseObject(flinkStarterArgs.getConfigContent());
            } catch (Exception e) {
                log.error("config content format error", e);
                throw new IllegalArgumentException("config file format error");
            }
        } else {
            throw new IllegalArgumentException("config file or content must be provided");
        }
        return jsonObject;
    }
}
