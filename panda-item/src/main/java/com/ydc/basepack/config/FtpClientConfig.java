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


    /**
     *
     * @return
     */
    @Bean("ftpConfig")
    @ConfigurationProperties(prefix = "ftp")
    public FtpConfig getFtpConfig() {
        //因为FtpConfig的构造器中必须注入charset,所以yml中需要配置
        FtpConfig ftpConfig = new FtpConfig();
        return ftpConfig;
    }

    /**
     * todo 疑问：FtpClient是否应该为单例？？如果是单例Ftpclinet.close将会导致空指针
     * @param ftpConfig
     * @return
     */
    @Bean(name = "ftpClientUtil")
    @DependsOn("ftpConfig")
    public Ftp createFtp(@Qualifier("ftpConfig")FtpConfig ftpConfig) {

        //选择模式：要看源码，此处要看到apache FtpClient enterLocalPassiveMode()但是鉴于本人能力未能看懂
        Ftp ftp = new Ftp(ftpConfig, FtpMode.Passive);
        return ftp;
    }



}
