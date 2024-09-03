package com.sparrow.support.script;

/**
 * @author daitf
 * @date 2024/8/18
 */
public interface ScriptExecutor<T extends ScriptExecuteConfig> {

    ScriptTypeEnum scriptType();

    boolean scriptExists(String id);

    boolean validate(T config);

    void load(T config);

    void unload(String id);

    Object compile(T config) throws Exception;

    Object execute(T config, Object... args) throws Exception;
}
