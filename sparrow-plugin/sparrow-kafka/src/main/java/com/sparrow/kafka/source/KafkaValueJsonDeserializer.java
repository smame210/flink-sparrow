package com.sparrow.kafka.source;

import com.alibaba.fastjson2.JSON;
import com.sparrow.api.bean.DataRecord;
import org.apache.kafka.common.errors.SerializationException;
import org.apache.kafka.common.serialization.Deserializer;

import java.io.UnsupportedEncodingException;
import java.util.Map;

/**
 * @author daitf
 * @date 2024/7/3
 */
public class KafkaValueJsonDeserializer implements Deserializer<DataRecord> {
    private String encoding = "UTF8";

    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {
        String propertyName = isKey ? "key.deserializer.encoding" : "value.deserializer.encoding";
        Object encodingValue = configs.get(propertyName);
        if (encodingValue == null) {
            encodingValue = configs.get("deserializer.encoding");
        }

        if (encodingValue instanceof String) {
            this.encoding = (String) encodingValue;
        }

    }

    @Override
    public DataRecord deserialize(String topic, byte[] data) {
        try {
            if (data != null) {
                return new DataRecord(Map.of("topic", topic), JSON.parseObject(new String(data, this.encoding)));
            }
            return null;
        } catch (UnsupportedEncodingException var4) {
            throw new SerializationException("Error when deserializing byte[] to DataRecord due to unsupported encoding " + this.encoding);
        }
    }
}
