package com.sparrow.api.config;

import lombok.Data;

import java.io.Serializable;

/**
 * @author daitf
 * @date 2024/7/3
 */
@Data
public class PluginConfig implements Serializable {

    private String id;

    private String name;
}
