package com.sparrow.core;

import com.sparrow.core.args.FlinkStarterArgs;
import com.sparrow.core.execution.FlinkTaskExecutor;
import com.sparrow.core.utils.ArgumentUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * @author daitf
 * @date 2024/7/8
 */
@Slf4j
public class FlinkStarter {
    public static void main(String[] args) {
        log.info("FlinkStarter start...");
        FlinkStarterArgs parse = ArgumentUtil.parse(args, new FlinkStarterArgs());
        FlinkTaskExecutor flinkTaskExecutor = new FlinkTaskExecutor(parse);
        flinkTaskExecutor.execute();
    }
}
