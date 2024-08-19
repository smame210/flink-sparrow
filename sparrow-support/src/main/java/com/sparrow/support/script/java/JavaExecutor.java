package com.sparrow.support.script.java;

import cn.hutool.core.util.ReUtil;
import cn.hutool.core.util.StrUtil;
import com.sparrow.support.script.ScriptExecuteConfig;
import com.sparrow.support.script.ScriptExecutor;
import com.sparrow.support.script.ScriptTypeEnum;
import org.apache.commons.lang3.StringUtils;
import org.codehaus.commons.compiler.CompilerFactoryFactory;
import org.codehaus.commons.compiler.IScriptEvaluator;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author daitf
 * @date 2024/8/18
 */
public class JavaExecutor extends ScriptExecutor {
    private static final Map<String, IScriptEvaluator> compiledScriptMap = new ConcurrentHashMap<>();

    @Override
    public ScriptTypeEnum scriptType() {
        return ScriptTypeEnum.JAVA;
    }

    @Override
    public boolean scriptExists(String id) {
        return compiledScriptMap.containsKey(id);
    }

    @Override
    public boolean validateScript(ScriptExecuteConfig config) {
        try {
            this.compile(config);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    @Override
    public void reload(ScriptExecuteConfig config) {
        try {
            compiledScriptMap.put(config.getId(), (IScriptEvaluator) this.compile(config));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void load(ScriptExecuteConfig config) {
        try {
            compiledScriptMap.put(config.getId(), (IScriptEvaluator) this.compile(config));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void unLoad(String id) {
        compiledScriptMap.remove(id);
    }

    @Override
    public Object compile(ScriptExecuteConfig config) throws Exception {
        JavaScriptExecuteConfig javaScriptExecuteConfig = (JavaScriptExecuteConfig) config;
        IScriptEvaluator se = CompilerFactoryFactory.getDefaultCompilerFactory(this.getClass().getClassLoader()).newScriptEvaluator();
        se.setTargetVersion(8);
        se.setReturnType(Object.class);
        se.setParameters(javaScriptExecuteConfig.getArgNameArray(), javaScriptExecuteConfig.getArgTypeArray());
        se.cook(this.convertScript(config.getScript(), javaScriptExecuteConfig.getArgNameArray()));
        return se;
    }

    private String convertScript(String script, String[] argNames) {
        String script1 = script
                .replace("public class", "class")
                .replace("private class", "class")
                .replace("protected class", "class");
        String className = ReUtil.getGroup1("class\\s+(\\w+)\\s+\\{", script1);
        if (StringUtils.isBlank(className)) {
            throw new RuntimeException("cannot find class defined");
        }

        return script1 + "\n" +
                StrUtil.format("{} item = new {}();\n", className, className) +
                StrUtil.format("return item.main({});", String.join(", ", argNames));
    }

    @Override
    public Object executeScript(ScriptExecuteConfig config, Object... args) throws Exception {
        if (!compiledScriptMap.containsKey(config.getId())) {
            throw new RuntimeException("No script found for id: " + config.getId());
        }
        IScriptEvaluator se = compiledScriptMap.get(config.getId());
        return se.evaluate(args);
    }
}
