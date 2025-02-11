package com.sparrow.support.script.java;

import cn.hutool.core.util.ReUtil;
import cn.hutool.core.util.StrUtil;
import com.sparrow.support.script.ScriptExecutor;
import com.sparrow.support.script.ScriptTypeEnum;
import org.apache.commons.lang3.StringUtils;
import org.noear.liquor.eval.CodeSpec;
import org.noear.liquor.eval.Execable;
import org.noear.liquor.eval.ParamSpec;
import org.noear.liquor.eval.Scripts;

import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author daitf
 * @date 2024/8/18
 */
public class JavaExecutor implements ScriptExecutor<JavaScriptExecuteConfig> {
    private static final Map<String, Execable> compiledScriptMap = new ConcurrentHashMap<>();

    @Override
    public ScriptTypeEnum scriptType() {
        return ScriptTypeEnum.JAVA;
    }

    @Override
    public boolean scriptExists(String id) {
        return compiledScriptMap.containsKey(id);
    }

    @Override
    public boolean validate(JavaScriptExecuteConfig config) {
        try {
            this.compile(config);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    @Override
    public void load(JavaScriptExecuteConfig config) {
        try {
            compiledScriptMap.put(config.getId(), (Execable) this.compile(config));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void unload(String id) {
        compiledScriptMap.remove(id);
    }

    @Override
    public Object compile(JavaScriptExecuteConfig config) throws Exception {
        String[] argNameArray = config.getArgNames();
        Class<?>[] argTypeArray = config.getArgTypes();
        if (argNameArray.length != argTypeArray.length) {
            throw new IllegalArgumentException("argNameArray and argTypeArray length not match");
        }

        Entry<String, Class<?>>[] entries = new Entry[argNameArray.length];
        for (int i = 0; i < argNameArray.length; i++) {
            entries[i] = new ParamSpec(argNameArray[i], argTypeArray[i]);
        }

        CodeSpec codeSpec = new CodeSpec(this.convertScript(config.getScript(), config.getArgNames()))
                .returnType(Object.class)
                .parameters(entries);
        return Scripts.compile(codeSpec);
    }

    private String convertScript(String script, String[] argNames) {
        String script1 = script
                .replace("public class", "class")
                .replace("private class", "class")
                .replace("protected class", "class");
        String className = ReUtil.getGroup1("class\\s+(\\w+)\\s*\\{", script1);
        if (StringUtils.isBlank(className)) {
            throw new RuntimeException("cannot find class defined or not implements 'JavaTemplate' interface");
        }
        if (!ReUtil.contains("(main\\()([^\\)]*)(\\))", script1)) {
            throw new RuntimeException("cannot find main method defined");
        }

        return script1 + "\n" +
                StrUtil.format("{} item = new {}();\n", className, className) +
                StrUtil.format("return item.main({});", String.join(", ", argNames));
    }

    @Override
    public Object execute(JavaScriptExecuteConfig config, Object... args) throws Exception {
        if (!compiledScriptMap.containsKey(config.getId())) {
            throw new RuntimeException("No script found for id: " + config.getId());
        }
        Execable se = compiledScriptMap.get(config.getId());
        return se.exec(args);
    }
}
