package com.yukong.panda.common.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix="mybatis-plus.configuration")
@Data
public class MybatisPlusProps {
    private String mapUderscoreToCamelCase;


}
