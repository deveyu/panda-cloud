package com.ydc.basepack.voice;

import cn.hutool.core.io.FileUtil;
import com.ydc.basepack.config.VoiceConfig;
import com.ydc.basepack.constants.VoiceConstants;
import com.ydc.basepack.manager.OssClient;
import com.ydc.basepack.voice.support.WaveHeader;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.*;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.Arrays;

/**
 * @author ydc
 * @description 合成语音的后续处理
 */
public class VoicePostTask implements Runnable {
    private final static Log logger = LogFactory.getLog(VoicePostTask.class);

    @Autowired
    VoiceConfig voiceConfig;
    private String[] pcmParts;
    private String uploadName;
    private String contId;

    public VoicePostTask(String contId, String[] pcmPartFile, String uploadName) {
        this.pcmParts = pcmPartFile;
        this.uploadName = uploadName;
        this.contId = contId;
    }

    @Override
    public void run() {
        String[] mp3Parts = new String[pcmParts.length];
        for (int i = 0; i < mp3Parts.length; i++) {
            String mp3Part = pcmParts[i].substring(0, pcmParts[i].lastIndexOf(".")) + VoiceConstants.MP3_FORMAT;
            try {
                convertAudioFiles(pcmParts[i], mp3Part);
                mp3Parts[i] = mp3Part;
            } catch (IOException e) {
                logger.error("pcm音频转mp3失败：" + pcmParts, e);
            }
        }
        //合成完毕
        String uploadFile = voiceConfig.getUploadUrlPrefix() + contId + "/" + uploadName + ".mp3";
        if (pcmParts.length > 1) {
            //如果多段文稿,需要聚合
            aggregateThenDelete(uploadFile, mp3Parts);
        }
        String[] files = new String[]{uploadFile};
        OssClient ossClient = new OssClient();
        ossClient.uploadOss(files);
    }


    /**
     * @param src    待转换文件路径
     * @param target 目标文件路径
     * @throws IOException 抛出异常
     */
    public static String convertAudioFiles(String src, String target) throws IOException {
        FileInputStream fis = new FileInputStream(src);
        FileOutputStream fos = new FileOutputStream(target);

        //计算长度
        byte[] buf = new byte[1024 * 4];
        int size = fis.read(buf);
        int PCMSize = 0;
        while (size != -1) {
            PCMSize += size;
            size = fis.read(buf);
        }
        fis.close();

        //填入参数，比特率等等。这里用的是16位单声道 8000 hz
        WaveHeader header = new WaveHeader();
        //长度字段 = 内容的大小（PCMSize) + 头部字段的大小(不包括前面4字节的标识符RIFF以及fileLength本身的4字节)
        header.fileLength = PCMSize + (44 - 8);
        header.FmtHdrLeth = 16;
        header.BitsPerSample = 16;
        header.Channels = 1;
        header.FormatTag = 0x0001;
        header.SamplesPerSec = 16000;//正常速度是8000，这里写成了16000，速度加快一倍
        header.BlockAlign = (short) (header.Channels * header.BitsPerSample / 8);
        header.AvgBytesPerSec = header.BlockAlign * header.SamplesPerSec;
        header.DataHdrLeth = PCMSize;

        byte[] h = header.getHeader();

        assert h.length == 44; //WAV标准，头部应该是44字节
        //write header
        fos.write(h, 0, h.length);
        //write data stream
        fis = new FileInputStream(src);
        size = fis.read(buf);
        while (size != -1) {
            fos.write(buf, 0, size);
            size = fis.read(buf);
        }
        fis.close();
        fos.close();
        logger.info("pcm convert to mp3 success,url:" + target );
        return "ok";
    }


    /**
     * 查找并聚合
     *
     * @param uploadFile 最终文件名
     * @param parts      要聚合的文件名
     */
    private void aggregateThenDelete(String uploadFile, String... parts) {

        logger.info(String.format("文稿%s,%d段音频准备聚合", contId, parts.length));
        logger.info("待聚合音频文件:" + Arrays.toString(parts));

        //执行合成MP3
        try {
            addWav(uploadFile, parts);
            logger.info(String.format("文稿%s,%d段音频聚合成功", contId, parts.length));
        } catch (IOException e) {
            logger.error(String.format("文稿%s,%d段音频聚合失败", contId, parts.length), e);
        }

       // String saveDir = CmsConstants.DEPOSITORY_PATH + "/" + CmsConstants.ARTICLE_VOICE_PATH;
        File f = new File(voiceConfig.getLocalUrlPrefix());
        //聚合以后删除分段文件,可能失败！
        try {
            for (String name : f.list()) {
                if (name.endsWith(".pcm")) {

                    FileUtil.del(new File(voiceConfig.getLocalUrlPrefix() + "/" + name));
                }
            }
            for (String part : parts) {
                FileUtil.del(new File(part));
            }
        } catch (Exception e) {
            logger.error("删除文件失败", e);
        }
    }

    /**
     * 将多个wav合成一个新的wav
     *
     * @param out 输出文件
     * @param in  输入文件数组
     * @throws IOException
     */
    private void addWav(String out, String... in) throws IOException {
        File outFile = new File(out);
        if (!outFile.getParentFile().exists())
            outFile.getParentFile().mkdirs();
        if (!outFile.exists())
            outFile.createNewFile();
        OutputStream os = new FileOutputStream(outFile);// 追加
        for (int i = 0; i < in.length; i++) {
            File file1 = new File(in[i]);
            if (!file1.exists())
                continue;
            InputStream is = new FileInputStream(file1);
            if (i != 0) {
                is.skip(44);// 跳过后面的.wav的文件头
            }
            byte[] tempBuffer = new byte[1024];
            int nRed = 0;
            // 将wav全部内容复制到out.wav
            while ((nRed = is.read(tempBuffer)) != -1) {
                os.write(tempBuffer, 0, nRed);
                os.flush();
            }
            is.close();
        }
        os.close();
        // 到此完成了in数组全部wav合并成out.wav,但是此时播放out.wav,音频内容仍然只是第一个音频的内容，所以还要更改out.wav的文件头
        for (String s : in) updateFileHead(s, false);
        updateFileHead(out, true);//头部合成
    }


    //更改文件头
    private void updateFileHead(String out, boolean ifUpdate) throws IOException {
        RandomAccessFile raf = new RandomAccessFile(out, "rw");
        // 打开一个文件通道
        FileChannel channel = raf.getChannel();
        // 映射文件中的某一部分数据以读写模式到内存中
        MappedByteBuffer buffer = channel.map(FileChannel.MapMode.READ_WRITE, 0, 44);// 文件头长度
        // 修改头部文件
        if (ifUpdate) {
            byte[] head1 = byteToByte(intToByteArray(headLength1));
            byte[] head2 = byteToByte(intToByteArray(headLength2));
            // 进行修改操作
            buffer.put(4, head1[0]);
            buffer.put(5, head1[1]);
            buffer.put(6, head1[2]);
            buffer.put(7, head1[3]);
            buffer.put(40, head2[0]);
            buffer.put(41, head2[1]);
            buffer.put(42, head2[2]);
            buffer.put(43, head2[3]);
            buffer.force();//强制输出，在buffer中的改动生效到文件
        } else {
            headLength1 = headLength1 + byteArrayToInt(byteToByte(new byte[]{buffer.get(4), buffer.get(5), buffer.get(6), buffer.get(7)}));
            headLength2 = headLength2 + byteArrayToInt(byteToByte(new byte[]{buffer.get(40), buffer.get(41), buffer.get(42), buffer.get(43)}));
        }
        buffer.clear();
        channel.close();
        raf.close();
    }

    //字节翻转
    public static byte[] byteToByte(byte[] a) {
        if (a.length == 4)
            return new byte[]{a[3], a[2], a[1], a[0]};
        return null;
    }

    private int headLength1 = 0;
    private int headLength2 = 0;

    public static int byteArrayToInt(byte[] b) {
        return b[3] & 0xFF | (b[2] & 0xFF) << 8 | (b[1] & 0xFF) << 16 | (b[0] & 0xFF) << 24;
    }

    public static byte[] intToByteArray(int a) {
        return new byte[]{(byte) ((a >> 24) & 0xFF), (byte) ((a >> 16) & 0xFF), (byte) ((a >> 8) & 0xFF), (byte) (a & 0xFF)};
    }


}
