package com.sparrow.script.transform;

import com.google.auto.service.AutoService;
import com.sparrow.api.bean.DataRecord;
import com.sparrow.api.plugin.transform.ITransformPlugin;
import com.sparrow.api.plugin.transform.ProcessTransform;
import com.sparrow.support.script.ScriptExecuteConfig;
import com.sparrow.support.script.ScriptExecutor;
import com.sparrow.support.script.ScriptExecutorEnum;
import com.sparrow.support.script.ScriptTypeEnum;
import com.sparrow.support.script.java.JavaScriptExecuteConfig;
import org.apache.flink.util.Collector;

import java.util.Arrays;
import java.util.UUID;

/**
 * @author daitf
 * @date 2024/8/27
 */
@AutoService(ITransformPlugin.class)
public class ScriptTransformPlugin extends ProcessTransform<DataRecord, DataRecord, ScriptTransformConfig> {

    private ScriptTransformConfig pluginConfig;

    private ScriptExecutor executor;

    private ScriptExecuteConfig scriptExecuteConfig;

    @Override
    public String getPluginName() {
        return "Script-Transform";
    }

    @Override
    public void prepare(ScriptTransformConfig pluginConfig) {
        this.pluginConfig = pluginConfig;
        if(!ScriptTypeEnum.checkScriptType(pluginConfig.getLanguage())) {
            throw new IllegalArgumentException("Unsupported script type: " + pluginConfig.getLanguage() + ", only support: " + Arrays.toString(ScriptTypeEnum.values()));
        }
        this.executor = ScriptExecutorEnum.getExecutorByType(ScriptTypeEnum.getScriptType(pluginConfig.getLanguage()));
        this.scriptExecuteConfig = buildScriptExecuteConfig(pluginConfig);
        boolean validated = this.executor.validate(scriptExecuteConfig);
        if (!validated) {
            throw new IllegalArgumentException("Invalid script: " + pluginConfig.getScript());
        }
        this.executor.load(scriptExecuteConfig);
    }

    private ScriptExecuteConfig buildScriptExecuteConfig(ScriptTransformConfig pluginConfig) {
        switch (ScriptTypeEnum.getScriptType(pluginConfig.getLanguage())) {
            case JAVA:
                return new JavaScriptExecuteConfig(UUID.randomUUID().toString(), pluginConfig.getScript(), new String[]{"data"}, new Class[]{Object.class});
            default:
                throw new IllegalArgumentException("Unsupported script type: " + pluginConfig.getLanguage() + ", only support: " + Arrays.toString(ScriptTypeEnum.values()));
        }
    }

    @Override
    public void processElement(DataRecord value, Context ctx, Collector<DataRecord> out) throws Exception {
        Object result = this.executor.execute(scriptExecuteConfig, value.getData());
        if (result == null && Boolean.TRUE.equals(this.pluginConfig.getFilterNull())){
            return;
        }
        value.setData(result);
        out.collect(value);
    }
}
