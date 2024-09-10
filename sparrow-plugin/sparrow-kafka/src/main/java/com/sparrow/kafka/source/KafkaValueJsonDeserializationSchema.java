package com.sparrow.kafka.source;

import com.alibaba.fastjson2.JSON;
import com.sparrow.api.bean.DataRecord;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.streaming.connectors.kafka.KafkaDeserializationSchema;
import org.apache.kafka.clients.consumer.ConsumerRecord;

import java.util.Map;

/**
 * @author daitf
 * @date 2024/9/10
 */
public class KafkaValueJsonDeserializationSchema implements KafkaDeserializationSchema<DataRecord> {
    private final String encoding = "UTF8";

    @Override
    public boolean isEndOfStream(DataRecord dataRecord) {
        return false;
    }

    @Override
    public DataRecord deserialize(ConsumerRecord<byte[], byte[]> consumerRecord) throws Exception {
        if (consumerRecord.value() != null) {
            String data = new String(consumerRecord.value(), this.encoding);
            return new DataRecord(Map.of("topic", consumerRecord.topic()), JSON.parseObject(data));
        }
        return null;
    }

    @Override
    public TypeInformation<DataRecord> getProducedType() {
        return TypeInformation.of(DataRecord.class);
    }
}
