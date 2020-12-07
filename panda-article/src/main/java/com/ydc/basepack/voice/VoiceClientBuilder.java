package com.ydc.basepack.voice;

import com.iflytek.cloud.speech.SpeechConstant;
import com.iflytek.cloud.speech.SpeechSynthesizer;
import com.iflytek.cloud.speech.SpeechUtility;
import com.ydc.basepack.config.VoiceConfig;
import com.ydc.basepack.manager.VoiceClient;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author ydc
 * todo 使用voiceConfig配置VoiceClientBuilder
 * 既然使用VoiceClientBuilder就不再使用spring实例化的VoiceClient
 * 用于自己定制化VoiceClient,
 * 优先级：定制化参数>config配置中心>sdk内部配置
 */
public class VoiceClientBuilder {
    @Autowired
    private VoiceConfig voiceConfig;

    private SpeechSynthesizer speechSynthesizer;

    public VoiceClientBuilder() {
        SpeechUtility.createUtility(SpeechConstant.APPID + "=" + voiceConfig.getAppId());
        SpeechSynthesizer.createSynthesizer();
    }

    public SpeechSynthesizer getSpeechSynthesizer() {
        return speechSynthesizer;
    }

    /**
     * voice_name	合成发音人	合成所需发音人,对应发音人参数可在控制台"发音人授权管理"查看。
     * todo 建造者模式都要判断 != null
     */
    public VoiceClientBuilder speaker(String voiceName) {

//        voiceName!=null? speechSynthesizer.setParameter(SpeechConstant.VOICE_NAME, voiceName):
//                voiceConfig.getSpeaker()!=null?speechSynthesizer.setParameter(SpeechConstant.VOICE_NAME,voiceConfig.getSpeaker()):
//                   ;
        if (voiceName != null) {
            speechSynthesizer.setParameter(SpeechConstant.VOICE_NAME, voiceName);
            return this;
        }
        voiceName = voiceConfig.getSpeaker();
        //这里因为不知道sdk内部是否做了!= null的判断，安全起见
        if (voiceName != null) {
            speechSynthesizer.setParameter(SpeechConstant.VOICE_NAME, voiceName);
        }
        return this;
    }

    /**
     * speed	语速	通过此参数，设置合成返回音频的语速。默认值：50，取值范围:[0,100]。
     */
    public VoiceClientBuilder speed(String speed) {
        speechSynthesizer.setParameter(SpeechConstant.SPEED, speed);
        return this;
    }

    /**
     * volume	音量	通过此参数，设置合成返回音频的音量。默认值：50，取值范围:[0,100]。
     */
    public VoiceClientBuilder volume(String volume) {
        speechSynthesizer.setParameter(SpeechConstant.VOLUME, volume);
        return this;
    }

    /**
     * pitch	语调	通过此参数，设置合成返回音频的语调。默认值：50，取值范围:[0,100]。
     */
    public VoiceClientBuilder pitch(String pitch) {
        speechSynthesizer.setParameter(SpeechConstant.PITCH, pitch);
        return this;
    }

    /**
     * sample_rate	采样率	音频的采样率是音频属性的其中一个，一般来说，采样率越高音频的质量越好，
     * 识别的匹配率越高，但上传带宽消耗也越大。 默认：16KHZ，取值{8KHZ,16KHZ}。
     */
    public VoiceClientBuilder sampleRate(String sampleRate) {
        speechSynthesizer.setParameter(SpeechConstant.SAMPLE_RATE, sampleRate);
        return this;
    }

    /**
     * engine_type	引擎类型	设置使用的引擎类型：在线、离线、混合。在线合成设置参数为："cloud" 。
     */
    public VoiceClientBuilder engineType(String engineType) {
        speechSynthesizer.setParameter(SpeechConstant.ENGINE_TYPE, engineType);
        return this;
    }

    /**
     * tts_audio_path	合成录音保存路径	通过此参数，可以在合成完成后在本地保存一个音频文件 。
     */
    public VoiceClientBuilder ttsAudioPath(String ttsAudioPath) {
        speechSynthesizer.setParameter(SpeechConstant.TTS_AUDIO_PATH, ttsAudioPath);
        return this;
    }

    public VoiceClient build() {
        return new VoiceClient();
    }
}