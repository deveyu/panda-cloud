package com.ydc.basepack.model;

/**
 * @author ydc
 * @description
 */
public interface MessageState {
    int SEND_PREPARE=0;
    int SEND=1;
    int SEND_CONFIRM=2;

    int SEND_FAIL=3;

}
