package com.sparrow.core.factory;

import com.sparrow.api.config.PluginConfig;
import com.sparrow.api.plugin.source.ISourcePlugin;

import java.lang.reflect.ParameterizedType;
import java.util.ServiceLoader;

/**
 * @author daitf
 * @date 2024/8/18
 */
public class SourcePluginFactory {

    public static ISourcePlugin getPluginByName(String pluginName) {
        ServiceLoader<ISourcePlugin> serviceLoader = ServiceLoader.load(ISourcePlugin.class);
        for (ISourcePlugin sourcePlugin : serviceLoader) {
            if (sourcePlugin.getPluginName().equalsIgnoreCase(pluginName)) {
                return sourcePlugin;
            }
        }
        throw new IllegalArgumentException("No source plugin found for name: " + pluginName);
    }

    public static Class<? extends PluginConfig> getPluginConfigClass(ISourcePlugin plugin) {
        return (Class) ((ParameterizedType) plugin.getClass().getGenericSuperclass()).getActualTypeArguments()[1];
    }
}
