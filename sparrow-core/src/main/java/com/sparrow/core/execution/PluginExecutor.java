package com.sparrow.core.execution;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.sparrow.api.bean.DataRecord;
import com.sparrow.api.config.PluginConfig;
import com.sparrow.api.plugin.sink.ISinkPlugin;
import com.sparrow.api.plugin.source.ISourcePlugin;
import com.sparrow.api.plugin.transform.ITransformPlugin;
import com.sparrow.api.plugin.transform.ProcessCountWindowTransform;
import com.sparrow.api.plugin.transform.ProcessTimeWindowTransform;
import com.sparrow.api.plugin.transform.ProcessTransform;
import com.sparrow.core.constants.ConfigConstants;
import com.sparrow.core.factory.SinkPluginFactory;
import com.sparrow.core.factory.SourcePluginFactory;
import com.sparrow.core.factory.TransformPluginFactory;
import lombok.extern.slf4j.Slf4j;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.windowing.assigners.TumblingEventTimeWindows;
import org.apache.flink.streaming.api.windowing.time.Time;

/**
 * @author daitf
 * @date 2024/8/18
 */
@Slf4j
public class PluginExecutor{
    private final JSONObject pluginConfig;

    private final FlinkRuntimeEnvironment flinkRuntimeEnvironment;

    public PluginExecutor(JSONObject pluginConfig, FlinkRuntimeEnvironment flinkRuntimeEnvironment) {
        this.pluginConfig = pluginConfig;
        this.flinkRuntimeEnvironment = flinkRuntimeEnvironment;
    }

    public void execute() {
        // todo
        // source plugin
        JSONObject source = pluginConfig.getJSONArray(ConfigConstants.SOURCE).getJSONObject(0);
        String sourcePluginName = source.getString(ConfigConstants.PLUGIN_NAME);
        ISourcePlugin sourcePlugin = SourcePluginFactory.getPluginByName(sourcePluginName);
        PluginConfig sourcePluginConfig = JSON.to(SourcePluginFactory.getPluginConfigClass(sourcePlugin), source);
        DataStream<DataRecord> dataStream = flinkRuntimeEnvironment.getStreamExecutionEnvironment()
                .fromSource(sourcePlugin.getSource(sourcePluginConfig),
                        WatermarkStrategy.<DataRecord>forMonotonousTimestamps()
                                .withTimestampAssigner((event, timestamp) -> event.getTimestamp()),
                        sourcePluginConfig.getName());
        log.info("source plugin [{}] load success", sourcePluginName);

        // transform plugin
        JSONArray transforms = pluginConfig.getJSONArray(ConfigConstants.TRANSFORM);
        for (int i = 0; i < transforms.size(); i++) {
            JSONObject transform = transforms.getJSONObject(i);
            String transformPluginName = transform.getString(ConfigConstants.PLUGIN_NAME);
            ITransformPlugin transformPlugin = TransformPluginFactory.getPluginByName(transformPluginName);
            PluginConfig transformPluginConfig = JSON.to(TransformPluginFactory.getPluginConfigClass(transformPlugin), transform);
            if (transformPlugin instanceof ProcessTransform) {
                ProcessTransform processTransform = (ProcessTransform) transformPlugin;
                processTransform.prepare(transformPluginConfig);
                dataStream = dataStream.process(processTransform.getTransformFunction());

            } else if (transformPlugin instanceof ProcessTimeWindowTransform) {
                ProcessTimeWindowTransform processWindowTransform = (ProcessTimeWindowTransform) transformPlugin;
                processWindowTransform.prepare(transformPluginConfig);
                dataStream = dataStream
                        .keyBy(processWindowTransform.getKeySelector())
                        .window(TumblingEventTimeWindows.of(Time.milliseconds(processWindowTransform.getWindowTime())))
                        .process(processWindowTransform.getTransformFunction());

            } else if (transformPlugin instanceof ProcessCountWindowTransform) {
                ProcessCountWindowTransform processCountWindowTransform = (ProcessCountWindowTransform) transformPlugin;
                processCountWindowTransform.prepare(transformPluginConfig);
                dataStream = dataStream
                        .keyBy(processCountWindowTransform.getKeySelector())
                        .countWindow(processCountWindowTransform.getWindowCount())
                        .process(processCountWindowTransform.getTransformFunction());
            } else {
                throw new RuntimeException("transform plugin type not support.");
            }
            log.info("transform plugin [{}] load success.", transform.getString(ConfigConstants.PLUGIN_NAME));
        }

        // sink plugin
        JSONObject sink = pluginConfig.getJSONArray(ConfigConstants.SINK).getJSONObject(0);
        String sinkPluginName = sink.getString(ConfigConstants.PLUGIN_NAME);
        ISinkPlugin sinkPlugin = SinkPluginFactory.getPluginByName(sinkPluginName);
        PluginConfig sinkPluginConfig = JSON.to(SinkPluginFactory.getPluginConfigClass(sinkPlugin), sink);
        dataStream.sinkTo(sinkPlugin.getSinkFunction(sinkPluginConfig));
        log.info("sink plugin [{}] load success", sink.getString(ConfigConstants.PLUGIN_NAME));
    }
}
