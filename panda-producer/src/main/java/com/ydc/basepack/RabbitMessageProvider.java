package com.ydc.basepack;


import com.ydc.basepack.model.Message;
import com.ydc.basepack.model.MessageHolder;
import com.ydc.basepack.util.ExecutorsManager;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.support.CorrelationData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Executor;

/**
 *
 * 	真正的发送消息的核心类
 */
@Slf4j
@Component
public class RabbitMessageProvider implements MessageProvider {

    /**
     *  表示rabbitmqtemplate 模板容器类
     */
    @Autowired
    private RabbitTemplateContainer rabbitTemplateContainer;
    @Resource(name= ExecutorsManager.EXECUTOR_NAME)
    private Executor executor;
    
    /**
     * 	消息被真正的物理存储服务
     */
    @Autowired
    private MessageStoreService messageStoreService;

    /**
     * 可靠性消息发送
     * @param message
     */
	@Override
	public void reliantSend(Message message,int retryCount,long timeout,boolean async) {

        message.setCreateTime(new Date());
        message.setRetry(retryCount);
        message.setTimeout(timeout);

        messageStoreService.insert(message);
        try {
        	sendMessage(message);
        } catch (Throwable e) {
            log.warn("reliantSend error!param: ", e);
        }
	}

    /**
     * 确认性消息发送
     * @param message
     */
    public void confirmSend(Message message) {
		sendMessage(message);
    }

    /**
     * 迅速消息发送
     * @param message
     */
	@Override
	public void rapidSend(Message message) {
		sendMessage(message);
	}

    private void sendMessage(Message message) {
    	executor.execute((Runnable) () -> {
                String routingKey = StringUtils.trimToEmpty(message.getRoutingKey());
                CorrelationData correlationData = new CorrelationData(String.format("%s#%s", message.getMessageId(), System.currentTimeMillis()));
                //rabbitTemplateContainer中存在回调方法
                RabbitTemplate template = rabbitTemplateContainer.getTemplate(message);
				template.convertAndSend(template.getExchange(), routingKey, message, correlationData);
                log.info("send to broker, message_id: {}", message.getMessageId());
        });
    }

	public void sendMessages() {
		List<Message> messages = MessageHolder.clear();
		System.err.println("messages------------" + messages);
		messages.forEach(message ->{
			MessageHolderAsyncQueue.submit((Runnable) () -> {
                String routingKey = StringUtils.trimToEmpty(message.getRoutingKey());
                CorrelationData correlationData = new CorrelationData(String.format("%s#%s", message.getMessageId(), System.currentTimeMillis()));
                RabbitTemplate template = rabbitTemplateContainer.getTemplate(message);
				template.convertAndSend(template.getExchange(), routingKey, message, correlationData);
                log.info("send to broker, message_id: {}", message.getMessageId());
			});
		});
	}
}
