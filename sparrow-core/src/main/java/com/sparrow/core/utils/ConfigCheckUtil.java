package com.sparrow.core.utils;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.sparrow.common.utils.JsonUtil;
import com.sparrow.core.constants.ConfigConstants;
import org.apache.commons.lang3.StringUtils;

/**
 * @author daitf
 * @date 2024/8/18
 */
public class ConfigCheckUtil {
    public static void checkConfig(JSONObject config) {
        if (!config.containsKey(ConfigConstants.ENV)) {
            throw new IllegalArgumentException("env is not set in config");
        }
        JSONObject env = config.getJSONObject(ConfigConstants.ENV);
        if (env.containsKey("sql") && StringUtils.isNoneBlank(env.getString("sql"))) {
            return;
        }

        JSONObject plugins = config.getJSONObject("plugins");
        if (plugins == null) {
            throw new IllegalArgumentException("plugins is not set in config");
        }
        if (!(plugins.containsKey(ConfigConstants.SOURCE) && plugins.get(ConfigConstants.SOURCE) instanceof JSONArray)) {
            throw new IllegalArgumentException("source is not set or source is not an array in config");
        }
        if (!(plugins.containsKey(ConfigConstants.TRANSFORM) && plugins.get(ConfigConstants.TRANSFORM) instanceof JSONArray)) {
            throw new IllegalArgumentException("transform is not set or transform is not an array in config");
        }
        if (!(plugins.containsKey(ConfigConstants.SINK) && plugins.get(ConfigConstants.SINK) instanceof JSONArray)) {
            throw new IllegalArgumentException("sink is not set or sink is not an array in config");
        }

    }
}
