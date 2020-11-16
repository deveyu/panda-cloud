package com.ydc.basepack.config;


import cn.hutool.extra.ftp.Ftp;
import cn.hutool.extra.ftp.FtpConfig;
import cn.hutool.extra.ftp.FtpMode;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

/**
 * @author ydc
 */
@Configuration
public class FtpClientConfig {


    @Bean("ftpConfig")
    @ConfigurationProperties(prefix = "ftp")
    public FtpConfig getFtpConfig() {
        //因为FtpConfig的构造器中必须注入charset,所以yml中需要配置
        FtpConfig ftpConfig = new FtpConfig();
        return ftpConfig;
    }

    @Bean(name = "ftpClientUtil")
    @DependsOn("ftpConfig")
    public Ftp createFtp(@Qualifier("ftpConfig")FtpConfig ftpConfig) {
        Ftp ftp = new Ftp(ftpConfig, FtpMode.Active);
        return ftp;
    }



}
