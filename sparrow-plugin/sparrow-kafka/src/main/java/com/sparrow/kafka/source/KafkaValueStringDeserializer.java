package com.sparrow.kafka.source;

import com.sparrow.api.bean.DataRecord;
import org.apache.kafka.common.errors.SerializationException;
import org.apache.kafka.common.serialization.Deserializer;

import java.io.UnsupportedEncodingException;
import java.util.Map;

/**
 * @author daitf
 * @date 2024/7/3
 */
public class KafkaValueStringDeserializer implements Deserializer<DataRecord> {
    private String encoding = "UTF8";

    @Override
    public DataRecord deserialize(String topic, byte[] data) {
        try {
            if (data != null) {
                return new DataRecord(Map.of("topic", topic), Map.of("value", new String(data, this.encoding)));
            }
            return null;
        } catch (UnsupportedEncodingException var4) {
            throw new SerializationException("Error when deserializing byte[] to DataRecord due to unsupported encoding " + this.encoding);
        }
    }
}
