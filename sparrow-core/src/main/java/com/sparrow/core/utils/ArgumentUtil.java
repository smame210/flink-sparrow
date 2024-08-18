package com.sparrow.core.utils;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.ParameterException;
import com.sparrow.core.args.CommandArgs;

/**
 * @author daitf
 * @date 2024/8/18
 */
public class ArgumentUtil {
    private ArgumentUtil() {}

    public static <T extends CommandArgs> T parse(String[] args, T obj) {
        JCommander jCommander = JCommander.newBuilder()
                        .addObject(obj)
                        .build();
        try {
            jCommander.parse(args);
        } catch (ParameterException e) {
            e.printStackTrace();
        }
        return obj;
    }
}
