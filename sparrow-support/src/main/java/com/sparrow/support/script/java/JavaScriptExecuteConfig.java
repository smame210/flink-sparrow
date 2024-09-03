package com.sparrow.support.script.java;

import com.sparrow.support.script.ScriptExecuteConfig;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author daitf
 * @date 2024/8/19
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class JavaScriptExecuteConfig extends ScriptExecuteConfig {
    private String[] argNames = new String[0];

    private Class<?>[] argTypes = new Class[0];

    public JavaScriptExecuteConfig(){}

    public JavaScriptExecuteConfig(String id, String script, String[] argNames, Class<?>[] argTypes){
        this.setId(id);
        this.setScript(script);
        this.setArgNames(argNames);
        this.setArgTypes(argTypes);
    }
}
