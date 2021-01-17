package com.ydc.basepack.model;

import com.google.common.collect.Lists;

import java.util.List;

/**
 * 	消息临时等待发送，用于发送批量消息，一批的消息在一个线程发送
 * @author lzw
 *
 */
public class MessageHolder {
	
    private List<Message> messages = Lists.newArrayList();

    //public static final ThreadLocal<MessageHolder> holder =ThreadLocal.withInitial()

    public static final ThreadLocal<MessageHolder> holder = new ThreadLocal(){
        @Override
        protected Object initialValue() {
            return new MessageHolder();
        }
    };

    /**
     * 将message放到holder
     * @param message
     */
    public static void add(Message message) {
        holder.get().messages.add(message);
    }

    /**
     * 从threadlocal里获取MessageHolder.messages 赋值给tmp集合并返回，
     * 删除该threadlocal里面的MessageHolder对象
     * @return
     */
    public static List<Message> clear() {
        List<Message> tmp = Lists.newArrayList(holder.get().messages);
        holder.remove();
        return tmp;
    }
    
}