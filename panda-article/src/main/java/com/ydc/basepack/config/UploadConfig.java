package com.ydc.basepack.config;

import cn.hutool.extra.ftp.FtpConfig;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author ydc
 * @description
 */

public class UploadConfig {

    @Bean("ftpConfig")
    @ConfigurationProperties(prefix = "upload.ftp")
    public FtpConfig getFtpConfig() {
        //因为FtpConfig的构造器中必须注入charset,所以yml中需要配置
        FtpConfig ftpConfig = new FtpConfig();
        return ftpConfig;
    }

    @Bean("ossConfig")
    @ConfigurationProperties(prefix = "upload.aliyun.oss")
    public OssConfig getOssConfig() {
        OssConfig ossConfig = new OssConfig();
        return ossConfig;
    }


    @Data
    public static class OssConfig {
        private String endpoint;
        private String bucketName;
        private String accessKey;
        private String secretAccessKey;
        private String domain;
    }
    
    
    
    
}
