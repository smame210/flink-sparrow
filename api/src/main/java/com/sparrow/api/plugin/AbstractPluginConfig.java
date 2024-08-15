package com.sparrow.api.plugin;

import com.sparrow.api.config.PluginConfig;

import java.lang.reflect.ParameterizedType;

/**
 * @author daitf
 * @date 2024/7/5
 */
public abstract class AbstractPluginConfig<T extends PluginConfig> {

    protected Class<T> getConfigClass() {
        return (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    }
}
