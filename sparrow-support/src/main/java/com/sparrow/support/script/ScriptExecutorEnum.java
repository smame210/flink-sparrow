package com.sparrow.support.script;

import com.sparrow.support.script.java.JavaExecutor;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author daitf
 * @date 2024/8/27
 */
@Getter
@AllArgsConstructor
public enum ScriptExecutorEnum {

    JAVA(ScriptTypeEnum.JAVA, new JavaExecutor()),
    ;

    private final ScriptTypeEnum type;

    private final ScriptExecutor<?> executor;

    public static ScriptExecutor<? extends ScriptExecuteConfig> getExecutorByType(ScriptTypeEnum type) {
        return ScriptExecutorEnum.valueOf(type.name()).executor;
    }
}
