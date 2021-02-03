package com.panda.basepack.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "snow.flake")
public class SnowFlakeProperties {
    private long  workerId;
    private long dataCenterId;


}
