package com.sparrow.common.enums;

import java.util.Arrays;

/**
 * @author daiTengFei
 * @date 2024/8/18
 */
public enum JobMode {
    BATCH,
    STREAMING,
    ;

    public static JobMode getJobMode(String jobModeStr) {
        if (jobModeStr == null) {
            return null;
        }
        return  Arrays.stream(JobMode.values())
                .filter(jobMode -> jobMode.name().equalsIgnoreCase(jobModeStr))
               .findFirst()
               .orElse(null);
    }

}
