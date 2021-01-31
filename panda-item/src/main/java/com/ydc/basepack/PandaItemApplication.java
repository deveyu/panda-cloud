package com.ydc.basepack;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootApplication
@EnableDiscoveryClient
@EnableAspectJAutoProxy
@ComponentScan(basePackages = {"com.ydc.basepack", "com.yukong.panda.common"})
@MapperScan(basePackages = {"com.ydc.basepack.mapper"})
@EnableFeignClients
public class PandaItemApplication {

    public static void main(String[] args) {
        SpringApplication.run(PandaItemApplication.class, args);
    }

}


