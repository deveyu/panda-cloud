package com.ydc.basepack;

import com.ydc.basepack.model.Message;

public interface MessageProvider {


    /**
     * rapidSend: 迅速消息,不做可靠性（入库），不做confirm
     * @param message
     */
    void rapidSend(Message message);

    /**
     * confirmSend：只是confirm
     * @param message
     */
    void confirmSend(Message message);

    /**
     * reliantSend: 可靠且confirm
     * @param message
     */
    void reliantSend(Message message);


    void sendMessages();
}
