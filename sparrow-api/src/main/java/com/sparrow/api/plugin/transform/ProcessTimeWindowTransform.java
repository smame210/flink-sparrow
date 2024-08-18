package com.sparrow.api.plugin.transform;

import com.sparrow.api.config.PluginConfig;
import org.apache.flink.api.java.functions.KeySelector;
import org.apache.flink.streaming.api.functions.windowing.ProcessWindowFunction;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;
import org.apache.flink.util.Collector;

/**
 * @author daitf
 * @date 2024/7/3
 */
public abstract class ProcessTimeWindowTransform<IN, OUT, KEY, T extends PluginConfig>
        extends ProcessWindowFunction<IN, OUT, KEY, TimeWindow> implements ITransformPlugin<T> {

    /**
     * 获取key选择器
     *
     * @return key选择器
     */
    public abstract KeySelector<IN, ?> getKeySelector();

    /**
     * 获取窗口时间
     *
     * @return 窗口时间(毫秒)
     */
    public abstract long getWindowTime();

    public abstract void process(KEY key, Context context, Iterable<IN> elements, Collector<OUT> out) throws Exception;

    @Override
    public ProcessWindowFunction<IN, OUT, KEY, TimeWindow> getTransformFunction() {
        return this;
    }
}
