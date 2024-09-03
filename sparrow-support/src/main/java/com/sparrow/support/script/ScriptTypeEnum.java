package com.sparrow.support.script;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

/**
 * @author daitf
 * @date 2024/8/18
 */
@Getter
@AllArgsConstructor
public enum ScriptTypeEnum {

    JAVA("java"),
    ;

    private final String type;

    public static boolean checkScriptType(String scriptType) {
        return Arrays.stream(ScriptTypeEnum.values())
                .anyMatch(e -> e.getType().equalsIgnoreCase(scriptType));
    }

    public static ScriptTypeEnum getScriptType(String scriptType) {
        return ScriptTypeEnum.valueOf(scriptType.toUpperCase());
    }
}
