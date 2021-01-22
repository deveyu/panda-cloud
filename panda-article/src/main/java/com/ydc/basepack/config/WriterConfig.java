package com.ydc.basepack.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author ydc
 * @description
 */
@Data
@ConfigurationProperties("write")
public class WriterConfig {
    private String appId;
    private String appSecret;
    /**
     * 平台类型 1：pc
     */
    private String platform;
    /**
     * 角色 null为主账户
     */
    private String roleId;
    /**
     * 签名方式 md5
     */
    private String signType;
    /**
     * url
     */
    private String loginUrl;
}
