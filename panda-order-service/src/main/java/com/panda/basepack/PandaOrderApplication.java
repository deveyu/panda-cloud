package com.panda.basepack;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
@MapperScan(basePackages = {"com.ydc.basepack.mapper"})
public class PandaOrderApplication {

    public static void main(String[] args) {
        SpringApplication.run(PandaOrderApplication.class, args);
    }

}