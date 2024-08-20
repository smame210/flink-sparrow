package com.sparrow.kafka.sink;

import com.google.auto.service.AutoService;
import com.sparrow.api.bean.DataRecord;
import com.sparrow.api.plugin.sink.ISinkPlugin;
import org.apache.flink.api.connector.sink2.Sink;
import org.apache.flink.connector.base.DeliveryGuarantee;
import org.apache.flink.connector.kafka.sink.KafkaRecordSerializationSchema;
import org.apache.flink.connector.kafka.sink.KafkaSink;

/**
 * @author daitf
 * @date 2024/7/3
 */
@AutoService(ISinkPlugin.class)
public class KafkaSinkPlugin implements ISinkPlugin<DataRecord, KafkaSinkConfig> {

    @Override
    public String getPluginName() {
        return "Kafka-Sink";
    }

    @Override
    public Sink<DataRecord> getSinkFunction(KafkaSinkConfig config) {
        KafkaRecordSerializationSchema<DataRecord> kafkaRecordSerializationSchema =KafkaRecordSerializationSchema.builder()
                .setTopic(config.getTopic())
                .setKeySerializationSchema(new KafkaKeySerializationSchema(config.getKeyField()))
                .setValueSerializationSchema(new KafkaValueSerializationSchema())
                .build();

        return KafkaSink.<DataRecord>builder()
                .setBootstrapServers(config.getBootstrapServers())
                .setRecordSerializer(kafkaRecordSerializationSchema)
                .setDeliverGuarantee(this.getDeliveryGuarantee(config))
                .build();
    }

    private DeliveryGuarantee getDeliveryGuarantee(KafkaSinkConfig config) {
        if (config.getDeliverGuarantee() == null) {
            return DeliveryGuarantee.NONE;
        }
        switch (config.getDeliverGuarantee().toLowerCase()) {
            case "at_least_once":
                return DeliveryGuarantee.AT_LEAST_ONCE;
            case "exactly_once":
                return DeliveryGuarantee.EXACTLY_ONCE;
            case "none":
                return DeliveryGuarantee.NONE;
            default:
                throw new IllegalArgumentException("Unsupported delivery guarantee: " + config.getDeliverGuarantee());
        }
    }
}
