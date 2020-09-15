package com.yukong.panda.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

/**
 * @author pc
 */
@EnableEurekaServer
@SpringBootApplication
public class PandaServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(PandaServerApplication.class, args);
    }
}
