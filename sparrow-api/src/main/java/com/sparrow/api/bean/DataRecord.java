package com.sparrow.api.bean;

import com.alibaba.fastjson2.JSONObject;
import com.sparrow.common.utils.JsonUtil;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Map;

/**
 * @author daitf
 * @date 2024/8/15
 */
public class DataRecord implements Serializable {
    private static final long serialVersionUID = -1L;

    @Setter
    @Getter
    private long timestamp = -1;

    private JSONObject config;

    private JSONObject data;

    public DataRecord() {
        this.timestamp = System.currentTimeMillis();
    }

    public DataRecord(Map<String, Object> map) {
        this.config = new JSONObject();
        this.data = new JSONObject(map);
        this.timestamp = System.currentTimeMillis();
    }

    public DataRecord(Map<String, Object> config, Map<String, Object> data) {
        this.config = new JSONObject(config);
        this.data = new JSONObject(data);
        this.timestamp = System.currentTimeMillis();
    }

    public JSONObject getData() {
        return data;
    }

    public void setData(Map<String, Object> map) {
        this.data = new JSONObject(map);
    }

    public void setData(JSONObject data) {
        this.data = data;
    }

    public JSONObject getConfig() {
        return config;
    }

    public void setConfig(Map<String, Object> map) {
        this.config = new JSONObject(map);
    }

    public void setConfig(JSONObject config) {
        this.config = config;
    }

    public Object getField(String fieldPath) {
        return JsonUtil.getField(this.data, fieldPath);
    }

    public void setField(String fieldPath, Object value) {
        JsonUtil.setField(this.data, fieldPath, value);
    }

    public Object getConfigField(String fieldPath) {
        return JsonUtil.getField(this.config, fieldPath);
    }

    public void setConfigField(String fieldPath, Object value) {
        JsonUtil.setField(this.config, fieldPath, value);
    }

    public DataRecord copy() {
        DataRecord dataRecord = new DataRecord();
        dataRecord.config = config.clone();
        dataRecord.data = data.clone();
        return dataRecord;
    }

    @Override
    public String toString() {
        return "DataRecord{timestamp=" + timestamp + ", config=" + config.toJSONString() + ", data=" + data.toJSONString() + '}';
    }
}
