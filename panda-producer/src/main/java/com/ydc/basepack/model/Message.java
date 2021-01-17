package com.ydc.basepack.model;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * 既然消息要落库，如果只存在一张表 那么他们的实体类就要统一
 */
@Data
public class Message implements Serializable {

    private String messageId;
    private String topic;
    private String routingKey;


    /**
     * 延迟时间
     */
    private long delay;
    private TimeUnit timeUnit;
    /**
     * 重试次数
     */
    private int retry;
    /**
     * 下一次重试时间间隔
     */
    private long timeout;

    private Date createTime;

}
