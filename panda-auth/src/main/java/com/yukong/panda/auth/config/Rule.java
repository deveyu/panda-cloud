package com.yukong.panda.auth.config;

import com.netflix.loadbalancer.IRule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;


@Configuration
public class Rule {

    @Bean
    @Scope("prototype")//这个注解不能少,否则快速调用几次就会出现com.netflix.client.ClientException: Load balancer does not have available server for client 异常
    public IRule tagRule() {
        return new CustomIsolationRule();
    }
}
