package com.sparrow.core.factory;

import com.sparrow.api.config.PluginConfig;
import com.sparrow.api.plugin.sink.ISinkPlugin;

import java.lang.reflect.ParameterizedType;
import java.util.ServiceLoader;

/**
 * @author daitf
 * @date 2024/8/18
 */
public class SinkPluginFactory {

    public static ISinkPlugin getPluginByName(String pluginName) {
        ServiceLoader<ISinkPlugin> serviceLoader = ServiceLoader.load(ISinkPlugin.class);
        for (ISinkPlugin sinkPlugin : serviceLoader) {
            if (sinkPlugin.getPluginName().equalsIgnoreCase(pluginName)) {
                return sinkPlugin;
            }
        }
        throw new IllegalArgumentException("No source plugin found for name: " + pluginName);
    }

    public static Class<? extends PluginConfig> getPluginConfigClass(ISinkPlugin plugin) {
        return (Class) ((ParameterizedType) plugin.getClass().getGenericSuperclass()).getActualTypeArguments()[1];
    }
}
