package com.ydc.basepack;

import java.util.List;
import java.util.concurrent.Executor;

import com.ydc.basepack.model.Message;
import com.ydc.basepack.util.ExecutorsManager;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.common.base.Preconditions;

import javax.annotation.Resource;

/**
 * 发送消息
 */
@Component
@Slf4j
public class ProducerClient  {

    @Autowired
    private MessageProvider messageProvider;

    public ProducerClient() {
    }

    /**
     *  发送单条消息
     */
    public void send(Message message) {
    	// 验证topic是否存在
        Preconditions.checkNotNull(message.getTopic());
        messageProvider.sendMessages();
        log.info("message is send .");
    }

    /**
     * 	批量发送消息, 利用了ThreadLocal(MessageHolder)
     */
	@Override
	public void send(List<Message> messages) throws MessageException {
		messages.forEach(message -> {
				message.setMessageType(MessageType.RAPID);
				MessageHolder.add(message);
		});
		messageProvider.sendMessages();
	}




}
