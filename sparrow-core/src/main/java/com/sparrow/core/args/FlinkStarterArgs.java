package com.sparrow.core.args;

import com.beust.jcommander.Parameter;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author daitf
 * @date 2024/7/8
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class FlinkStarterArgs extends CommandArgs {
    @Parameter(names = {"-n", "--name"}, description = "flink job name")
    protected String jobName = "default";

    @Parameter(names = {"-f", "--file"}, description = "Config file (json) path")
    protected String configFile;

    @Parameter(names = {"-j", "--json"}, description = "Config content of json")
    protected String configContent;
}
