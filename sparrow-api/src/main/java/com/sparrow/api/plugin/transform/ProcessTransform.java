package com.sparrow.api.plugin.transform;

import com.sparrow.api.config.PluginConfig;
import org.apache.flink.streaming.api.functions.ProcessFunction;

/**
 * @author daitf
 * @date 2024/7/3
 */
public abstract class ProcessTransform<IN, OUT, T extends PluginConfig> extends ProcessFunction<IN, OUT> implements ITransformPlugin<T> {


    @Override
    public ProcessFunction<IN, OUT> getTransformFunction() {
        return this;
    }
}
