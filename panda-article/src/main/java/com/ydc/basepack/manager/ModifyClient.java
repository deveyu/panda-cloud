package com.ydc.basepack.manager;


import com.ydc.basepack.vo.ModifyVO;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * @author ydc
 * @description 智能校对
 */
public class ModifyClient {


    public static Log log = LogFactory.getLog(ModifyClient.class);

    /**
     * 同步接口
     */
    private String syncUrl;

    /**
     * 异步接口
     */
    private String asyncUrl;
    /**
     * 获取异步任务进度接口
     */
    private String progressUrl;
    /**
     * 获取异步结果
     */
    private String resUrl;

    private static ModifyClient modifyClient;


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

    public synchronized static ModifyClient getInstance() {

        if (modifyClient == null) {
//            modifyClient = new ModifyClient(CmsConstants.FANGZHENG_SYCH_URL, CmsConstants.FANGZHENG_ASYCH_URL,
//                    CmsConstants.FANGZHENG_PROGRESS_URL, CmsConstants.FANGZHENG_RES_URL);
        }
        return modifyClient;

    }

    private ModifyClient(String syncUrl, String asyncUrl, String progressUrl, String resUrl) {
        this.syncUrl = syncUrl;
        this.asyncUrl = asyncUrl;
        this.progressUrl = progressUrl;
        this.resUrl = resUrl;
    }

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
        List<String> textList = StringUtil.getStrList(text, WORD_LIMIT);
        List<ModifyVO> modifyList = new ArrayList<>();
        CountDownLatch latch = new CountDownLatch(textList.size());
        //这里的map要保证不会扩容需要>size*4/3   64>>size
        Map<Integer, List<ModifyVO>> resMap = new HashMap<>(64);

        for (int i = 0; i < textList.size(); i++) {
            ModifyTask modifyTask = new ModifyTask(i, contId, textList.get(i), latch, resMap);
            PublishAppSkinThreadPool.executorService.execute(modifyTask);
        }

        latch.await(10, TimeUnit.SECONDS);

        for (int i = 0; i < textList.size(); i++) {
            List<ModifyVO> modifyVOS = resMap.get(i);
            if (modifyVOS != null) {
                modifyList.addAll(modifyVOS);
            }
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
        String taskResp = HttpClientUtil.HttpPostByJson2(asyncUrl, JsonUtil.map2json(taskReqMap));

        log.info(String.format("智能校对接口响应:%s,文稿id:%s", taskResp, contId));
        Map<String, Object> taskRespMap = (Map<String, Object>) JsonUtil.fromJson(taskResp, HashMap.class);
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

            String progressRes = HttpClientUtil.requestGet(pUrl);
            //这里的响应保证会有值，不做空指针校验
            Map<String, Object> resMap2 = (Map<String, Object>) JsonUtil.fromJson(progressRes, HashMap.class);
            Map<String, Object> data2 = (Map<String, Object>) resMap2.get("data");
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

        String result = HttpClientUtil.requestGet(rUrl);
        log.info(String.format("获取智能校对任务结果,url:%s,文稿id: %s", rUrl, contId));
        log.info(String.format("获取智能校对任务结果：%s,文稿id: %s", result, contId));
        Map<String, Object> resMap2 = (Map<String, Object>) JsonUtil.fromJson(result, HashMap.class);
        Map<String, Object> data = (Map<String, Object>) resMap2.get("data");
        Map<String, Object> wordCorrect = (Map<String, Object>) data.get("wordcorrect");
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
                String jsonBody = JsonUtil.map2json(map);

                String res = HttpClientUtil.HttpPostByJson2(syncUrl, jsonBody);
                log.info(String.format("文稿id:%s,order:%d,智能校对接口响应:%s", contId, order, res));
                ModifyResVO modifyResVO = JsonUtil.fromJson(res, ModifyResVO.class);

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
