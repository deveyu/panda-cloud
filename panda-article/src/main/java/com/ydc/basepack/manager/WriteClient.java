package com.ydc.basepack.manager;

import cn.hutool.crypto.SecureUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ydc.basepack.config.VoiceConfig;
import com.ydc.basepack.config.WriterConfig;
import com.ydc.basepack.constants.WriteConstants;
import com.yukong.panda.common.enums.ResponseCodeEnum;
import com.yukong.panda.common.exception.ServiceException;
import io.netty.util.internal.StringUtil;
import org.apache.commons.codec.digest.Md5Crypt;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.NameValuePair;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

/**
 * @author ydc
 * @description
 */
@Component
public class WriteClient {
    private final static Log log = LogFactory.getLog(WriteClient.class);

    @Autowired
    private WriterConfig writerConfig;
    @Autowired
    private RestTemplate restTemplate;

    private ObjectMapper jsonUtil = new ObjectMapper();


    public void write() {
        String apiName = "writer.normal_article";
        String secretId = "c934d5e2cec44a399ab2bb1ad9c809c5";
        String secretKey = "c0e8aa3b5e544d8b900d2f111b103584";
        String apiHost = "http://robotsdk.giiso.com/grobot-api/";

        Map<String, String> reqMap = new HashMap<>(16);
        reqMap.put("secretId", secretId);
        reqMap.put("token", getToken(secretKey));
        reqMap.put("keyword", "特朗普");
        reqMap.put("field", "社会");
        reqMap.put("timeScope", "1");
        reqMap.put("articleSize", "750");

        String url = apiHost + apiName.replace('.', '/');
        String resp = restTemplate.postForObject(url, reqMap, String.class);
        System.out.println("resp = " + resp);

    }

    private String getToken(String key) {
        String hostname = "127.0.0.1";
        String timestamp = String.valueOf(System.currentTimeMillis());
        return SecureUtil.md5(hostname + key + timestamp) + timestamp;

    }

    public String getWriteUrl(String userId, String nickname) {
        TreeMap<String, String> treeMap = new TreeMap<>();
        if (userId == null || nickname == null) {
            return "";
        }

        treeMap.put(WriteConstants.PARAM_NICKNAME, nickname);
        treeMap.put(WriteConstants.PARAM_USERID, userId);
        //todo 遍历实体类属性
        treeMap.put(WriteConstants.PARAM_APPID, writerConfig.getAppId());
        treeMap.put(WriteConstants.PARAM_SIGNTYPE, writerConfig.getSignType());
        treeMap.put(WriteConstants.PARAM_PLATFROM, writerConfig.getPlatform());
        String roleId = writerConfig.getRoleId();
        if (StringUtils.isNotBlank(roleId)) {
            treeMap.put(WriteConstants.PARAM_ROLEID, roleId);
        }

        StringBuilder sb = new StringBuilder();
        //将map序列化为key=value&
        for (Map.Entry<String, String> entry : treeMap.entrySet()) {
            sb.append(entry.getKey() + "=" + entry.getValue() + "&");
        }
        StringBuilder noKeyString = new StringBuilder(sb.toString());
        sb.append(WriteConstants.PARAM_APPSECRET).append("=").append(writerConfig.getAppSecret());
        String signString = sb.toString();

        String md5String = SecureUtil.md5(signString).toUpperCase();

        //apache的加密不可用
        //String md5String = Md5Crypt.md5Crypt(signString.getBytes()).toUpperCase();

        String suffix = noKeyString.append(WriteConstants.PARAM_SIGN).append("=").append(md5String).toString();

        return writerConfig.getLoginUrl() + "?" + suffix;

    }

}
