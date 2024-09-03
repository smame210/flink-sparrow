package com.sparrow.core.factory;

import com.sparrow.api.config.PluginConfig;
import com.sparrow.api.plugin.sink.ISinkPlugin;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
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
        Type[] genericInterfaces = plugin.getClass().getGenericInterfaces();
        for (Type genericInterface : genericInterfaces) {
            if (genericInterface instanceof ParameterizedType) {
                ParameterizedType parameterizedType = (ParameterizedType) genericInterface;
                if (parameterizedType.getRawType().equals(ISinkPlugin.class)) {
                    return (Class) parameterizedType.getActualTypeArguments()[1];
                }
            }
        }
        throw new IllegalArgumentException("No plugin config found for plugin: " + plugin.getClass().getName());
    }
}
