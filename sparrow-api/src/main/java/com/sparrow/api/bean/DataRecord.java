package com.sparrow.api.bean;

import com.alibaba.fastjson2.JSONObject;
import com.sparrow.common.utils.JsonUtil;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;
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

    private JSONObject header;

    @Setter
    @Getter
    private Object data;

    public DataRecord() {
        this.timestamp = System.currentTimeMillis();
    }

    public DataRecord(Object data) {
        this.header = new JSONObject();
        this.data = data;
        this.timestamp = System.currentTimeMillis();
    }

    public DataRecord(Map<String, Object> header, Object data) {
        this.header = new JSONObject(header);
        this.data = data;
        this.timestamp = System.currentTimeMillis();
    }

    public JSONObject getHeader() {
        return header;
    }

    public void setHeader(Map<String, Object> map) {
        if (map == null || map.isEmpty()){
            return;
        }
        this.header = new JSONObject(map);
    }

    public void setHeader(JSONObject config) {
        if (config == null){
            return;
        }
        this.header = config;
    }

    public void addHeader(Map<String, Object> map) {
        if (map == null || map.isEmpty()){
            return;
        }
        this.header.putAll(map);
    }

    public void removeHeader(List<String> keys) {
        if (keys == null || keys.isEmpty()){
            return;
        }
        for (String key : keys) {
            this.header.remove(key);
        }
    }

    public Object getHeaderField(String fieldPath) {
        return JsonUtil.getField(this.header, fieldPath);
    }

    public void setHeaderField(String fieldPath, Object value) {
        JsonUtil.setField(this.header, fieldPath, value);
    }

    public DataRecord copy() {
        DataRecord dataRecord = new DataRecord();
        dataRecord.header = header.clone();
        dataRecord.data = data;
        return dataRecord;
    }

    @Override
    public String toString() {
        return "DataRecord{timestamp=" + timestamp + ", header=" + header.toJSONString() + ", data=" + data.toString() + '}';
    }
}
