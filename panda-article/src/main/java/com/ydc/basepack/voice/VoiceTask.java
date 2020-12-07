package com.ydc.basepack.voice;

import cn.hutool.http.HtmlUtil;
import com.iflytek.cloud.speech.SpeechSynthesizer;
import com.iflytek.cloud.speech.SynthesizeToUriListener;
import com.ydc.basepack.config.VoiceConfig;
import com.ydc.basepack.constants.VoiceConstants;
import com.ydc.basepack.manager.VoiceClient;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;

/**
 * @author ydc
 * @description VoiceTask多例，是线程安全的
 */
public class VoiceTask implements Runnable {
    private final static Log log = LogFactory.getLog(VoiceTask.class);

    @Autowired
    VoiceConfig voiceConfig;

    private String contId;
    /**
     * 最终音频文件名 时间戳
     */
    private String fileName;

    private String htmlText;

    private VoiceClient voiceClient;

    @Autowired
    private Executor executor;


    public VoiceTask(String contId, String fileName, String htmlText) {
        this.contId = contId;
        this.fileName = fileName;
        this.htmlText = htmlText;
    }


    @Override
    public void run() {
        String text = cleanHtml(contId, htmlText);
        String pcmName;
        String[] pcmParts;
        long start = System.currentTimeMillis();
        if (text.length() <= VoiceConstants.WORD_LIMIT) {
            pcmName = voiceConfig.getLocalUrlPrefix()+ contId + "/" + fileName + VoiceConstants.PCM_FORMAT;
            synthesize(text, pcmName, 1, 1);
            pcmParts = new String[]{pcmName};
        } else {
            List<String> textList = splitArticle(text, VoiceConstants.WORD_LIMIT);
            int partCount = textList.size();
            String tempName = String.valueOf(System.currentTimeMillis());
            pcmParts = new String[partCount];

            for (int i = 0; i < textList.size(); i++) {
                pcmName = voiceConfig.getLocalUrlPrefix() + contId + "/" + tempName + "_" + (i + 1) + VoiceConstants.PCM_FORMAT;
                synthesize(textList.get(i), pcmName, i + 1, partCount);
                pcmParts[i] = pcmName;
            }

        }

        long synthesizeTime = (System.currentTimeMillis() - start) / 1000;
        log.info(String.format("  文稿%s,字数%d,合成耗时%d秒", contId, text.length(), synthesizeTime));

        //后续任务交给其他线程处理
        VoicePostTask voicePostTask = new VoicePostTask(contId, pcmParts, fileName);
        executor.execute(voicePostTask);
    }

    private void synthesize(String text, String tempName, int order, int partCount) {
        Thread currentThread = Thread.currentThread();

        SpeechSynthesizer speechSynthesizer = SpeechSynthesizer.getSynthesizer();

        SynthesizeToUriListener listener = voiceClient.getListener();
        try {
//            synchronized (VoiceConstants.LOCK) {
            voiceClient.setAcceptThread(currentThread);
            speechSynthesizer.synthesizeToUri(text, tempName, listener);
            log.info(String.format("接收线程%s阻塞,文稿%s第%d段正在合成......", currentThread.getName(), contId, order));

            //阻塞超时120s
            //这里wait的话会释放锁，然后导致其他线程可以获取锁
            //VoiceConstants.LOCK.wait(120*1000);
            //这里利用sleep不会释放锁，难道设置了超时时间，interrupt就不起作用了码
            TimeUnit.SECONDS.sleep(120);
//            }
        } catch (InterruptedException e) {
            log.info("当前线程状态：" + currentThread.isInterrupted());
            log.info(String.format("接收线程%s已唤醒,文稿%s还有%d段待合成...", currentThread.getName(), contId, partCount - order));

            //不做处理，继续执行代码

        } catch (Exception e1) {
            log.error(String.format("语音合成失败，文稿id:%s", contId), e1);

        }
    }

    private String cleanHtml(String contId, String htmlText) {
        String text = HtmlUtil.cleanHtmlTag(htmlText);
        log.info(String.format("文稿%s,字数为%s", contId, text.length()));
        return text;
    }

    private List<String> splitArticle(String htmlText, int wordLimit) {

        //分割文稿，每次2048个字符
        List<String> textList = getStrList(htmlText, wordLimit);

        return textList;
    }


    /**
     * 把原始字符串分割成指定长度的字符串列表
     *
     * @param inputString 原始字符串
     * @param length      指定长度
     * @return
     */
    public static List<String> getStrList(String inputString, int length) {
        int size = inputString.length() / length;
        if (inputString.length() % length != 0) {
            size += 1;
        }
        return getStrList(inputString, length, size);
    }

    /**
     * 把原始字符串分割成指定长度的字符串列表
     *
     * @param inputString 原始字符串
     * @param length      指定长度
     * @param size        指定列表大小
     * @return
     */
    public static List<String> getStrList(String inputString, int length,
                                          int size) {
        List<String> list = new ArrayList<String>();
        for (int index = 0; index < size; index++) {
            String childStr = substring(inputString, index * length,
                    (index + 1) * length);
            list.add(childStr);
        }
        return list;
    }

    /**
     * 分割字符串，如果开始位置大于字符串长度，返回空
     *
     * @param str 原始字符串
     * @param f   开始位置
     * @param t   结束位置
     * @return
     */
    public static String substring(String str, int f, int t) {
        if (f > str.length())
            return null;
        if (t > str.length()) {
            return str.substring(f, str.length());
        } else {
            return str.substring(f, t);
        }
    }


    public void setVoiceClient(VoiceClient voiceClient) {
        this.voiceClient = voiceClient;
    }
}


