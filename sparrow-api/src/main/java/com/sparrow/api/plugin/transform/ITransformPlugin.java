package com.sparrow.api.plugin.transform;

import com.sparrow.api.common.IPluginIdentifier;
import com.sparrow.api.config.PluginConfig;
import org.apache.flink.api.common.functions.AbstractRichFunction;

import java.io.Serializable;

/**
 * @author daitf
 * @date 2024/7/3
 */
public interface ITransformPlugin<T extends PluginConfig> extends Serializable, IPluginIdentifier {

    void prepare(T pluginConfig);

    AbstractRichFunction getTransformFunction();
}
