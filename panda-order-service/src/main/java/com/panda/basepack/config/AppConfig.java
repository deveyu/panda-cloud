package com.panda.basepack.config;


import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(SnowFlakeProperties.class)
public class AppConfig {

}
