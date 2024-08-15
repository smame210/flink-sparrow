package com.sparrow.common.utils;

import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.JSONPath;

/**
 * @author daitf
 * @date 2024/7/10
 */
public class JsonUtil {

    public static boolean containsField(JSONObject data, String fieldPath) {
        if (fieldPath == null) {
            return false;
        }
        if (!fieldPath.startsWith("$")) {
            fieldPath = "$." + fieldPath;
        }
        return JSONPath.contains(data, fieldPath);
    }

    public static Object getField(JSONObject data, String fieldPath) {
        if (fieldPath == null) {
            return null;
        }
        if (!fieldPath.startsWith("$")) {
            fieldPath = "$." + fieldPath;
        }
        return JSONPath.eval(data, fieldPath);
    }

    public static JSONObject setField(JSONObject data, String fieldPath, Object value) {
        if (fieldPath == null) {
            return data;
        }
        if (!fieldPath.startsWith("$")) {
            fieldPath = "$." + fieldPath;
        }
        return (JSONObject) JSONPath.set(data, fieldPath, value);
    }
}
