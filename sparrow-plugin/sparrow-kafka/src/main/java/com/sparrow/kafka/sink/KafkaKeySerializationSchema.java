package com.sparrow.kafka.sink;

import com.sparrow.api.bean.DataRecord;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.flink.api.common.serialization.SerializationSchema;
import org.apache.kafka.common.errors.SerializationException;

import java.io.UnsupportedEncodingException;

/**
 * @author daitf
 * @date 2024/7/3
 */
@Slf4j
public class KafkaKeySerializationSchema implements SerializationSchema<DataRecord> {
    private String encoding = "UTF8";

    private String keyField;

    public KafkaKeySerializationSchema(String keyField) {
        this.keyField = keyField;
    }

    @Override
    public byte[] serialize(DataRecord data) {
        try {
            if (StringUtils.isEmpty(keyField)) {
                return null;
            }

            if (data == null) {
                return null;
            }
            return data.getField(keyField) == null ? null : data.getField(keyField).toString().getBytes(encoding);
        } catch (UnsupportedEncodingException e) {
            throw new SerializationException("Error when serializing key string to byte[] due to unsupported encoding " + this.encoding);
        } catch (Exception e) {
            log.error("Error when serializing key dataRecord to byte[]", e);
            return null;
        }
    }
}
