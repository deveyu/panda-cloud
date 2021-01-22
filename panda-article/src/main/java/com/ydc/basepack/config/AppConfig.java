package com.ydc.basepack.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author ydc
 * @description
 */
@Configuration
@EnableConfigurationProperties({VoiceConfig.class,ModifyConfig.class,WriterConfig.class})
public class AppConfig {
}
