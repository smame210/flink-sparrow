package com.sparrow.kafka.source;

import com.google.auto.service.AutoService;
import com.sparrow.api.bean.DataRecord;
import com.sparrow.api.plugin.source.ISourcePlugin;
import org.apache.commons.lang3.StringUtils;
import org.apache.flink.api.connector.source.Source;
import org.apache.flink.api.connector.source.SourceSplit;
import org.apache.flink.connector.kafka.source.KafkaSource;
import org.apache.flink.connector.kafka.source.enumerator.initializer.OffsetsInitializer;
import org.apache.flink.connector.kafka.source.reader.deserializer.KafkaRecordDeserializationSchema;
import org.apache.kafka.common.TopicPartition;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @author daitf
 * @date 2024/7/3
 */
@AutoService(ISourcePlugin.class)
public class KafkaSourcePlugin implements ISourcePlugin<DataRecord, KafkaSourceConfig> {

    @Override
    public String getPluginName() {
        return "Kafka-Source";
    }

    @Override
    public Source<DataRecord, ? extends SourceSplit, ?> getSource(KafkaSourceConfig config) {
        return KafkaSource.<DataRecord>builder()
                .setBootstrapServers(config.getBootstrapServers())
                .setTopics(config.getTopics())
                .setGroupId(StringUtils.isBlank(config.getGroupId()) ? UUID.randomUUID().toString() : config.getGroupId())
                .setStartingOffsets(this.getStartingOffsets(config))
                .setDeserializer(this.getDeserializer(config))
                .build();
    }

    private OffsetsInitializer getStartingOffsets(KafkaSourceConfig config) {
        if (config.getStartingOffsets() == null) {
            return OffsetsInitializer.committedOffsets();
        }

        switch (config.getStartingOffsets().toLowerCase()) {
            case "latest":
                return OffsetsInitializer.latest();
            case "earliest":
                return OffsetsInitializer.earliest();
            case "timestamp":
                if (config.getTimestamp() == null || config.getTimestamp() < 0) {
                    throw new IllegalArgumentException("Timestamp must be provided for 'timestamp' starting offset");
                }
                return OffsetsInitializer.timestamp(config.getTimestamp());
            case "specific_offset":
                if (config.getOffsets() == null || config.getOffsets().isEmpty()) {
                    throw new IllegalArgumentException("Specific offsets must be provided for 'specific_offset' starting offset");
                }
                Map<TopicPartition, Long> offsets = new HashMap<>();
                for (Map.Entry<String, Map<Integer, Long>> topicOffsets : config.getOffsets().entrySet()) {
                    if (topicOffsets.getValue() == null || topicOffsets.getValue().isEmpty()) {
                        throw new IllegalArgumentException("Specific offsets must be provided for 'specific_offset' starting offset");
                    }

                    for (Map.Entry<Integer, Long> partitionOffset : topicOffsets.getValue().entrySet()) {
                        offsets.put(new TopicPartition(topicOffsets.getKey(), partitionOffset.getKey()), partitionOffset.getValue());
                    }
                }
                return OffsetsInitializer.offsets(offsets);
            default:
                throw new IllegalArgumentException("Unsupported starting offset type: " + config.getStartingOffsets());
        }
    }

    private  KafkaRecordDeserializationSchema<DataRecord> getDeserializer(KafkaSourceConfig config) {
        if (config.getDeserializer() == null) {
            return KafkaRecordDeserializationSchema.valueOnly(KafkaValueJsonDeserializer.class);
        }
        switch (config.getDeserializer().toLowerCase()) {
            case "json":
                return KafkaRecordDeserializationSchema.valueOnly(KafkaValueJsonDeserializer.class);
            case "string":
                return KafkaRecordDeserializationSchema.valueOnly(KafkaValueStringDeserializer.class);
            default:
                throw new IllegalArgumentException("Unsupported deserializer type: " + config.getDeserializer());
        }
    }
}
