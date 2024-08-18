package com.sparrow.core.utils;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.sparrow.core.constants.ConfigConstants;

/**
 * @author daitf
 * @date 2024/8/18
 */
public class ConfigCheckUtil {
    public static void checkConfig(JSONObject config) {
        if (!config.containsKey(ConfigConstants.ENV)) {
            throw new IllegalArgumentException("env is not set in config");
        }
        if (!(config.containsKey(ConfigConstants.SOURCE) && config.get(ConfigConstants.SOURCE) instanceof JSONArray)) {
            throw new IllegalArgumentException("source is not set or source is not an array in config");
        }
        if (!(config.containsKey(ConfigConstants.TRANSFORM) && config.get(ConfigConstants.TRANSFORM) instanceof JSONArray)) {
            throw new IllegalArgumentException("transform is not set or transform is not an array in config");
        }
        if (!(config.containsKey(ConfigConstants.SINK) && config.get(ConfigConstants.SINK) instanceof JSONArray)) {
            throw new IllegalArgumentException("sink is not set or sink is not an array in config");
        }

    }
}
