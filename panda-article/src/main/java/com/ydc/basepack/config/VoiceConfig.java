package com.ydc.basepack.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author ydc
 * @description
 */
@Data
@ConfigurationProperties("voice")
public class VoiceConfig {
    private String appId;
    private String speed;
    private String speaker;
    private String volume;
    private String sampleRate;
    private String engineType;
    private String ttsAudioPath;
    private String localUrlPrefix;
    private String uploadUrlPrefix;
}
