package com.ydc.basepack.manager;

import com.iflytek.cloud.speech.*;
import com.ydc.basepack.config.VoiceConfig;

import com.ydc.basepack.voice.VoiceTask;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.concurrent.Executor;

/**
 * @author ydc
 * 语音合成
 */
@Component
public class VoiceClient {

    private final static Log logger = LogFactory.getLog(VoiceClient.class);

    @Resource(name=ExecutorsManager.EXECUTOR_NAME)
    private Executor executor;

    @Autowired
    private VoiceConfig voiceConfig;

    private  VoiceClient voiceClient;

    private Thread acceptThread;
    /**
     * 合成监听器
     */
    private SynthesizeToUriListener listener;


    /**
     * appid
     */
    private String appId;

    @PostConstruct
    public void postCreate() {
        SpeechUtility.createUtility(SpeechConstant.APPID + "=" + voiceConfig.getAppId());
        SpeechSynthesizer.createSynthesizer();
    }

    public void start(VoiceTask task) {
        task.setVoiceClient(this);
        executor.execute(task);
    }

    public void setAcceptThread(Thread acceptThread) {
        this.acceptThread = acceptThread;
    }

    public SynthesizeToUriListener getListener() {

        if (listener == null) {
            listener = new SynthesizeToUriListener() {
                @Override
                public void onBufferProgress(int progress) {
                    logger.debug("合成进度:" + progress);
                }

                @Override
                public void onSynthesizeCompleted(String uri, SpeechError error) {
                    if (error == null) {
                        logger.info("音频合成成功,uri:" + uri);
//                        synchronized (VoiceConstants.LOCK) {
//                            VoiceConstants.LOCK.notifyAll();
                        while (!acceptThread.isInterrupted()) {
                            acceptThread.interrupt();
//                            }
                        }
                    } else {
                        //如果有错误码如何终止后续任务？快速失败！！！
                        logger.error("*************语音合成失败,错误码:" + error.getErrorCode() + "*************");
                    }


                }


                @Override
                public void onEvent(int eventType, int arg1, int arg2, int arg3, Object obj1, Object obj2) {
                    if (SpeechEvent.EVENT_TTS_BUFFER == eventType) {
                        ArrayList<?> bufs = null;
                        if (obj1 instanceof ArrayList<?>) {
                            bufs = (ArrayList<?>) obj1;
                        } else {
                            logger.debug("onEvent error obj1 is not ArrayList !");
                        }

                        if (null != bufs) {
                            for (final Object obj : bufs) {
                                if (obj instanceof byte[]) {
                                    final byte[] buf = (byte[]) obj;
                                    logger.debug("onEvent buf length: " + buf.length);
                                } else {
                                    logger.debug("onEvent error element is not byte[] !");
                                }
                            }
                        }
                    }
                }
            };
        }
        return listener;
    }


}
