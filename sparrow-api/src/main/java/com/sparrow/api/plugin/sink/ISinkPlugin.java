package com.sparrow.api.plugin.sink;

import com.sparrow.api.common.IPluginIdentifier;
import com.sparrow.api.config.PluginConfig;
import org.apache.flink.api.connector.sink2.Sink;

import java.io.Serializable;

/**
 * @author daitf
 * @date 2024/7/3
 */
public interface ISinkPlugin<IN, T extends PluginConfig> extends Serializable, IPluginIdentifier {

    Sink<IN> getSinkFunction(T config);
}
