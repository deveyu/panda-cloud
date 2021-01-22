package com.ydc.basepack.model;

import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.util.Assert;

import java.util.Date;
import java.util.Map;

/**
 * @author ydc
 * @description 落库存储 消息实体类
 */
public class ConfirmMessage<T> extends GenericMessage<T> {


    private Integer retryCount;
    private Integer state;
    private Date createTime;
    //过了多久没有ack,就执行重试
    private Long timeout;


    public ConfirmMessage(T payload) {
        super(payload);
    }

    public ConfirmMessage(T payload, Map<String, Object> headers) {
        super(payload, headers);
    }

    public ConfirmMessage(T payload, MessageHeaders headers) {
        super(payload, headers);
    }
}
