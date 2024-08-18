package com.sparrow.api.plugin.source;

import com.sparrow.api.common.IPluginIdentifier;
import com.sparrow.api.config.PluginConfig;
import org.apache.flink.api.connector.source.Source;
import org.apache.flink.api.connector.source.SourceSplit;

import java.io.Serializable;

/**
 * @author daitf
 * @date 2024/7/3
 */
public interface ISourcePlugin<OUT, T extends PluginConfig> extends Serializable, IPluginIdentifier {

    Source<OUT, ? extends SourceSplit, ?> getSource(T config);
}
