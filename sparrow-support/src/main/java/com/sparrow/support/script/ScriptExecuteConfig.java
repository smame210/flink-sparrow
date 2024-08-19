package com.sparrow.support.script;

import lombok.Data;

import java.io.Serializable;

/**
 * @author daitf
 * @date 2024/8/18
 */
@Data
public class ScriptExecuteConfig implements Serializable {
    private String id;

    private String script;
}
