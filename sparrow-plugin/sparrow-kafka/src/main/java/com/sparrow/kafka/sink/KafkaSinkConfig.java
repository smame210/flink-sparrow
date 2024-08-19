package com.sparrow.kafka.sink;

import com.sparrow.api.config.PluginConfig;
import lombok.Data;

/**
 * @author daitf
 * @date 2024/7/3
 */
@Data
public class KafkaSinkConfig extends PluginConfig {
    /**
     * the bootstrap servers of kafka cluster
     */
    private String bootstrapServers;

    /**
     * the topic of kafka message
     */
    private String topic;

    /**
     * the key field of kafka message
     */
    private String keyField;

    /**
     * support NONE, AT_LEAST_ONCE, EXACTLY_ONCE. default is NONE.
     */
    private String deliverGuarantee;
}
