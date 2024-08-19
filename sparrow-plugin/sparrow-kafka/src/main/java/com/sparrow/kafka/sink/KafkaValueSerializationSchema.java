package com.sparrow.kafka.sink;

import com.alibaba.fastjson2.JSON;
import com.sparrow.api.bean.DataRecord;
import lombok.extern.slf4j.Slf4j;
import org.apache.flink.api.common.serialization.SerializationSchema;
import org.apache.kafka.common.errors.SerializationException;

import java.io.UnsupportedEncodingException;

/**
 * @author daitf
 * @date 2024/7/3
 */
@Slf4j
public class KafkaValueSerializationSchema implements SerializationSchema<DataRecord> {
    private String encoding = "UTF8";

    @Override
    public byte[] serialize(DataRecord data) {
        try {
            return data == null ? null : JSON.toJSONString(data.getData()).getBytes(this.encoding);
        } catch (UnsupportedEncodingException e) {
            throw new SerializationException("Error when serializing key string to byte[] due to unsupported encoding " + this.encoding);
        } catch (Exception e) {
            log.error("Error when serializing key dataRecord to byte[]", e);
            return null;
        }
    }
}
