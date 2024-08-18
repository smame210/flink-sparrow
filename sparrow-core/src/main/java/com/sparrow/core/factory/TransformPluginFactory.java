package com.sparrow.core.factory;

import com.sparrow.api.config.PluginConfig;
import com.sparrow.api.plugin.transform.ITransformPlugin;

import java.lang.reflect.ParameterizedType;
import java.util.ServiceLoader;

/**
 * @author daitf
 * @date 2024/8/18
 */
public class TransformPluginFactory {

    public static ITransformPlugin getPluginByName(String pluginName) {
        ServiceLoader<ITransformPlugin> serviceLoader = ServiceLoader.load(ITransformPlugin.class);
        for (ITransformPlugin transformPlugin : serviceLoader) {
            if (transformPlugin.getPluginName().equalsIgnoreCase(pluginName)) {
                return transformPlugin;
            }
        }
        throw new IllegalArgumentException("No source plugin found for name: " + pluginName);
    }

    public static Class<? extends PluginConfig> getPluginConfigClass(ITransformPlugin plugin) {
        return (Class) ((ParameterizedType) plugin.getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    }
}
