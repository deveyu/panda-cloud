package com.ydc.basepack.config;


import cn.hutool.extra.ftp.Ftp;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FtpUtilConfig {

    @Bean(name = "ftpUtil")
    public Ftp createFtp() {
        Ftp ftp = new Ftp("119.45.104.69",21,"root","root");
        return ftp;
    }



}
