package com.ydc.basepack.constants;

import lombok.Data;


public enum MessageState {
    PREPARE_SEND(0,"准备发送"),
    SEND(1,"已发送至broker"),
    SUCCESS(2,"已成功confirm"),
    FAILURE(3,"重试后仍然失败"),
    ;


    private int code;
    private String description;

    MessageState(int code, String description) {
        this.code = code;
        this.description = description;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
