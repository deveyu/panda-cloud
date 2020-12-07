package com.ydc.basepack.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author ydc
 * @description
 */
@Data
@ConfigurationProperties("modify")
public class ModifyConfig {
    private String syncUrl;
    private String asyncUrl;
    private String progressUrl;
    private String resUrl;
    private Boolean asyncEnable;
}
