package com.ydc.basepack.manager;

import com.aliyun.oss.*;
import com.ydc.basepack.config.UploadConfig;
import com.ydc.basepack.config.VoiceConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

/**
 * @author ydc
 */
@Slf4j
public class OssClient {
    @Autowired
    private VoiceConfig voiceConfig;
    @Autowired
    private static UploadConfig.OssConfig ossConfig;

    private static OSS ossClient;

    @Bean
    @Scope(value = "prototype")
    public static OSS getOssClient() {
        ClientBuilderConfiguration conf = new ClientBuilderConfiguration();
        conf.setMaxConnections(200);
        conf.setSocketTimeout(10000);
        conf.setConnectionTimeout(10000);
        conf.setConnectionRequestTimeout(1000);
        conf.setIdleConnectionTime(10000);
        conf.setMaxErrorRetry(5);
        conf.setSupportCname(true);
        // 创建OSSClient实例。
        ossClient= new OSSClientBuilder().build(ossConfig.getEndpoint(),
                ossConfig.getAccessKey(), ossConfig.getSecretAccessKey(), conf);
        return ossClient;
    }

    public boolean uploadOss(String[] files) {
        try {
            String ossUrl = "";
            long start = System.currentTimeMillis();
            for (String file : files) {
                InputStream inputStream = new FileInputStream(file);
                String ossName = file.substring(file.lastIndexOf("/") + 1);
                ossUrl = "voice/" + ossName;
                log.info("准备上传 :" + ossUrl);
                try {
                    ossClient.putObject(ossConfig.getBucketName(), ossUrl, inputStream);
                } catch (OSSException oe) {
                    log.error("Error Message: " + oe.getErrorMessage());
                    log.error("Error Code:       " + oe.getErrorCode());
                    log.error("Request ID:      " + oe.getRequestId());
                    log.error("Host ID:           " + oe.getHostId());
                } finally {
                    inputStream.close();
                }
            }
            long uploadTime = (System.currentTimeMillis() - start) / 1000;
            log.info(String.format("文件上传成功,url:%s,文件大小:%sM,上传耗时%s秒",
                    voiceConfig.getUploadUrlPrefix() + ossUrl, new File(files[0]).length() / 1048576, uploadTime));
            return true;
        } catch (Exception e) {
            log.error("文件上传失败", e);
        } finally {
            if (ossClient != null) {
                ossClient.shutdown();
            }
        }
        return false;
    }

}
