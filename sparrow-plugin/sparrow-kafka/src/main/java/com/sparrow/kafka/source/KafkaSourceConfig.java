package com.sparrow.kafka.source;

import com.sparrow.api.config.PluginConfig;
import lombok.Data;

import java.util.Map;

/**
 * kafka source config
 *
 * @author daitf
 * @date 2024/7/3
 */
@Data
public class KafkaSourceConfig extends PluginConfig {
    /**
     * The bootstrap servers for the Kafka cluster.
     */
    private String bootstrapServers;

    /**
     * The topics to consume.
     */
    private String topics;

    /**
     * The group id for this consumer.
     */
    private String groupId;

    /**
     * Starting offsets for each topic. supports earliest, latest, timestamp, or specific_offset.
     */
    private String startingOffsets;

    /**
     * The timestamp to start consuming from if startingOffsets is set to "timestamp".
     */
    private Long timestamp;

    /**
     * The specific offset to start consuming from if startingOffsets is set to "specific_offset".
     *
     * e.g. {"topic1": {0: 100, 1: 200}, "topic2": {0: 300, 1: 400}}
     */
    private Map<String, Map<Integer, Long>> offsets;

    /**
     * The serializer class for the message value. supports string, json
     */
    private String deserializer;

}
