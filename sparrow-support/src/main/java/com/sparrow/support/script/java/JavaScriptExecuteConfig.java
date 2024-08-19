package com.sparrow.support.script.java;

import cn.hutool.core.collection.CollUtil;
import com.sparrow.support.script.ScriptExecuteConfig;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * @author daitf
 * @date 2024/8/19
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class JavaScriptExecuteConfig extends ScriptExecuteConfig {
    private List<String> argNames;

    private List<Class<?>> argTypes;

    public String[] getArgNameArray() {
        if (CollUtil.isEmpty(argNames)) {
            return new String[0];
        }
        return argNames.toArray(new String[0]);
    }

    public Class[] getArgTypeArray() {
        if (CollUtil.isEmpty(argTypes)) {
            return new Class[0];
        }
        return argTypes.toArray(new Class[0]);
    }
}
