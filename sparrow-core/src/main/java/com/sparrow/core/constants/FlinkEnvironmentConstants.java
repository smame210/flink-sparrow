package com.sparrow.core.constants;

/**
 * @author daitf
 * @date 2024/8/18
 */
public class FlinkEnvironmentConstants {
    private FlinkEnvironmentConstants() {}

    public static final String JOB_TYPE = "jobType";

    public static final String DEFAULT_JOB_TYPE = "dataStream";

    public static final String JOB_NAME = "jobName";

    public static final String DEFAULT_JOB_NAME = "SparrowJob";

    public static final String JOB_MODE = "jobMode";

    public static final String PARALLELISM = "parallelism";

    public static final String RESTART_STRATEGY = "restart.strategy";

    public static final String RESTART_ATTEMPTS = "restart.attempts";
    public static final String RESTART_DELAY_BETWEEN_ATTEMPTS = "restart.delayBetweenAttempts";

    public static final String RESTART_FAILURE_INTERVAL = "restart.failureInterval";
    public static final String RESTART_FAILURE_RATE = "restart.failureRate";
    public static final String RESTART_DELAY_INTERVAL = "restart.delayInterval";

    public static final String CHECKPOINT_INTERVAL = "checkpoint.interval";
    public static final String CHECKPOINT_MODE = "checkpoint.mode";
    public static final String STATE_BACKEND = "checkpoint.state.backend";

}
