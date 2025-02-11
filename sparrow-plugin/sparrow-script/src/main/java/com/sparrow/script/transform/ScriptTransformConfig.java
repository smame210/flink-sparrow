package com.sparrow.script.transform;

import com.sparrow.api.config.PluginConfig;
import lombok.Data;

/**
 * @author daitf
 * @date 2024/8/27
 */
@Data
public class ScriptTransformConfig extends PluginConfig {

    /**
     * script language. support java
     */
    private String language;

    /**
     * script content
     */
    private String script;

    /**
     * don't return null value
     */
    private Boolean filterNull;
}
