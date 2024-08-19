package com.sparrow.support.script;

import java.io.Serializable;

/**
 * @author daitf
 * @date 2024/8/18
 */
public abstract class ScriptExecutor implements Serializable {

    public ScriptExecutor init(){
        return this;
    }

    public abstract ScriptTypeEnum scriptType();

    public abstract boolean scriptExists(String id);

    public abstract boolean validateScript(ScriptExecuteConfig config);

    public abstract void reload(ScriptExecuteConfig config);

    public abstract void load(ScriptExecuteConfig config);

    public abstract void unLoad(String id);

    public abstract Object compile(ScriptExecuteConfig config) throws Exception;

    public abstract Object executeScript(ScriptExecuteConfig config, Object... args) throws Exception;
}
