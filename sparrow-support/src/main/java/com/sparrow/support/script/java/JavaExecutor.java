package com.sparrow.support.script.java;

import cn.hutool.core.util.ReUtil;
import cn.hutool.core.util.StrUtil;
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
public class JavaExecutor implements ScriptExecutor<JavaScriptExecuteConfig> {
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
            compiledScriptMap.put(config.getId(), (IScriptEvaluator) this.compile(config));
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
        IScriptEvaluator se = CompilerFactoryFactory.getDefaultCompilerFactory(this.getClass().getClassLoader()).newScriptEvaluator();
        se.setTargetVersion(8);
        se.setReturnType(Object.class);
        se.setParameters(config.getArgNames(), config.getArgTypes());
        se.cook(this.convertScript(config.getScript(), config.getArgNames()));
        return se;
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
        IScriptEvaluator se = compiledScriptMap.get(config.getId());
        return se.evaluate(args);
    }
}
