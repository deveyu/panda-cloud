package com.ydc.basepack.service.impl;

import cn.hutool.http.HtmlUtil;
import com.esotericsoftware.minlog.Log;
import com.ydc.basepack.manager.ModifyClient2;
import com.ydc.basepack.manager.VoiceClient;
import com.ydc.basepack.manager.WriteClient;
import com.ydc.basepack.service.ArticleService;
import com.ydc.basepack.voice.VoiceTask;
import com.ydc.basepack.vo.ModifyVO;
import com.yukong.panda.common.enums.ResponseCodeEnum;
import com.yukong.panda.common.exception.ServiceException;
import com.yukong.panda.common.util.ApplicationContextProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author ydc
 * @description
 */
@Service
public class ArticleServiceImpl implements ArticleService {

    @Autowired
    private ApplicationContextProvider ctx;

    @Resource(name = "voiceClient")
    private VoiceClient voiceClient;

    @Resource(name = "modifyClient2")
    private ModifyClient2 modifyClient;

    @Resource(name = "writeClient")
    private WriteClient writeClient;

    @Override
    public void convert2Voice(String htmlText) {

        VoiceClient voiceClient = (VoiceClient) ctx.getBean("voiceClient");
        String fileName = String.valueOf(System.currentTimeMillis());
        VoiceTask task = new VoiceTask("10800211232", fileName, htmlText);
        voiceClient.start(task);
    }

    @Override
    public List<ModifyVO> modifyArticle(String htmlText, Boolean async) {
        String text = HtmlUtil.cleanHtmlTag(htmlText);
        List<ModifyVO> modify = null;
        try {
            modify = modifyClient.modify("10800211232", text, async);
        } catch (Exception e) {
            Log.error("校对失败", e);
            throw new ServiceException(ResponseCodeEnum.FAILURE, e);
        }
        return modify;
    }

    @Override
    public String getWriteUrl(String userId, String nickname) {
      // return writeClient.getWriteUrl(userId, nickname);
        writeClient.write();
        return "";
    }

}
