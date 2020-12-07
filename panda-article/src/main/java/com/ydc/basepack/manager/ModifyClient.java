package com.ydc.basepack.manager;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ydc.basepack.config.ModifyConfig;
import com.ydc.basepack.util.ArticleUtil;
import com.ydc.basepack.vo.ModifyResVO;
import com.ydc.basepack.vo.ModifyVO;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;

/**
 * @author ydc
 * @description 智能校对
 */
@Component
public class ModifyClient {
    public static Log log = LogFactory.getLog(ModifyClient.class);

    @Autowired
    private Executor executor;

    @Autowired
    private ModifyConfig modifyConfig;

    @Autowired
    private RestTemplate restTemplate;

    //todo 增强@value让他支持像@ConfigurationProperties prefix前缀匹配
    @Value(value = "modify.sync-url")
    private String syncUrl;
    @Value(value = "modify.async-url")
    private String asyncUrl;
    @Value(value = "modify.progress-url")
    private String progressUrl;
    @Value(value = "modify.res-url")
    private String resUrl;

    private ObjectMapper jsonUtil = new ObjectMapper();


    /**
     * 任务状态 -1：任务失败 0：未处理 2：审校处理完成 21：审校处理中 22：审校处理失败
     */
    private static final String STATE_OVER = "2";
    private static final String STATE_WAIT = "21";
    private static final String STATE_TASK_FAIL = "-1";
    private static final String STATE_MODIFY_FAIL = "22";


    private static final String NORMAL_STATUS = "0";
    private static final String EXPIRE_STATUS = "1";
    private static final String NO_PERMISSION_STATUS = "2";

    private static final int WORD_LIMIT = 2000;


    /**
     * @param text  文稿内容
     * @param async false同步/true异步
     * @return
     * @throws Exception
     */

    public List<ModifyVO> modify(String contId, String text, Boolean async) throws Exception {
        int length = text.length();
        if (length <= 0) {
            return Collections.emptyList();
        }
        //async=true调异步接口
        if (async) {
            return modifyAsync(contId, text);
        }
        return modifySync(contId, text);
    }


    /**
     * 同步审校 需要拆分文稿
     *
     * @param text 文本内容
     * @return
     */
    public List<ModifyVO> modifySync(String contId, String text) throws InterruptedException {

        //分割文稿，每次2048个字符
        List<String> textList = ArticleUtil.getStrList(text, WORD_LIMIT);
        List<ModifyVO> modifyList = new ArrayList<>();
        CountDownLatch latch = new CountDownLatch(textList.size());
        //这里的map要保证不会扩容需要>size*4/3   64>>size
        Map<Integer, List<ModifyVO>> resMap = new HashMap<>(64);

        for (int i = 0; i < textList.size(); i++) {
            ModifyTask modifyTask = new ModifyTask(i, contId, textList.get(i), latch, resMap);
            executor.execute(modifyTask);
        }

        latch.await(20, TimeUnit.SECONDS);

        for (int i = 0; i < textList.size(); i++) {
            modifyList.addAll(resMap.get(i));
        }
        return modifyList;
    }


    /**
     * 异步审校
     *
     * @param text 文本内容
     * @return
     */
    public List<ModifyVO> modifyAsync(String contId, String text) throws Exception {

        Map<String, String> taskReqMap = new HashMap<>(1);
        taskReqMap.put("text", text);

//        ResponseEntity<String> responseEntity = restTemplate.postForEntity(modifyConfig.getAsyncUrl(), taskReqMap, String.class);
//        String body1 = responseEntity.getBody();

        Map taskRespMap = restTemplate.postForObject(modifyConfig.getAsyncUrl(), taskReqMap, Map.class);

        //log.info(String.format("智能校对接口响应:%s,文稿id:%s", map.get("data"), contId));

        String jobId = (String) taskRespMap.get("data");

        String pUrl = progressUrl;
        String rUrl = resUrl;
        if ("".equals(jobId)) {
            log.info(String.format("创建智能校对任务失败：jobId为空,文稿id:%s", contId));
            return Collections.emptyList();
        }
        //接口是rest风格，需要替换{jobId}
        pUrl = pUrl.replace("{jobId}", jobId);
        rUrl = rUrl.replace("{jobId}", jobId);

        log.info("智能审校进度,url:" + pUrl);
        //轮询获取进度
        String jobState = "";
        while (true) {

            Map resMap2 = restTemplate.getForObject(pUrl, Map.class);
            //这里的响应保证会有值，不做空指针校验
            Map data2 = (Map) resMap2.get("data");
            jobState = (String) data2.get("jobState");
            String pState = STATE_WAIT.equals(jobState) ? "正在处理" : "已完成";
            log.info(String.format("智能审校进度：%s,文稿id:%s", pState, contId));
            if (STATE_WAIT.equals(jobState)) {
                TimeUnit.SECONDS.sleep(2);
            } else if (STATE_OVER.equals(jobState)) {
                //直接返回
                break;
            } else if (STATE_TASK_FAIL.equals(jobState)) {
                log.error(String.format("智能审校任务失败,文稿id:%s", contId));
                return Collections.emptyList();
            } else if (STATE_MODIFY_FAIL.equals(jobState)) {
                log.error(String.format("智能审校处理失败", contId));
                return Collections.emptyList();
            }
        }

        String result = restTemplate.getForObject(rUrl, String.class);
        log.info(String.format("获取智能校对任务结果,url:%s,文稿id: %s", rUrl, contId));
        log.info(String.format("获取智能校对任务结果：%s,文稿id: %s", result, contId));
        Map resMap2 = jsonUtil.readValue(result, Map.class);
        Map data = (Map) resMap2.get("data");
        Map wordCorrect = (Map) data.get("wordcorrect");
        List<ModifyVO> detail = (List<ModifyVO>) wordCorrect.get("detail");
        return detail;


    }


    /**
     * @descrption 任务类：一段作为一个任务
     */
    class ModifyTask implements Runnable {

        private final Integer order;

        private final String contId;

        private final String partText;

        private final CountDownLatch latch;

        private final Map<Integer, List<ModifyVO>> resMap;


        public ModifyTask(Integer order, String contId, String partText, CountDownLatch latch, Map<Integer, List<ModifyVO>> resMap) {
            this.order = order;
            this.contId = contId;
            this.partText = partText;
            this.latch = latch;
            this.resMap = resMap;
        }


        @Override
        public void run() {
            Map<String, String> map = new HashMap<>(1);
            map.put("text", partText);
            try {
                String res = restTemplate.postForObject(syncUrl, map, String.class);
                log.info(String.format("文稿id:%s,order:%d,智能校对接口响应:%s", contId, order, res));
                ModifyResVO modifyResVO = jsonUtil.readValue(res, ModifyResVO.class);
                String status = modifyResVO.getStatus();
                if (NORMAL_STATUS.equals(status)) {
                    ModifyResVO.Data data = modifyResVO.getData();
                    //每次结果
                    List<ModifyVO> modifyPart = data.getDetail();
                    resMap.put(order, modifyPart);
                } else if (EXPIRE_STATUS.equals(status)) {
                    log.warn("智能校对：token过期");
                } else if (NO_PERMISSION_STATUS.equals(status)) {
                    log.warn("智能校对：无权限");
                }
            } catch (Exception e) {
                log.error(String.format("文稿id:%s,序号:%d,智能校对接口响应异常", contId, order), e);
            } finally {
                latch.countDown();
            }

        }
    }

}
