package com.yukong.panda.common.exception;

import com.yukong.panda.common.enums.ResponseCodeEnum;
import lombok.Getter;

/**
 * @author: ydc
 */
public class ServiceException extends RuntimeException{

    public ServiceException() {
    }

    @Getter
    private  ResponseCodeEnum responseCodeEnum;//final

    public ServiceException(String message) {
        super(message);
        this.responseCodeEnum = ResponseCodeEnum.FAILURE;
    }

    public ServiceException(ResponseCodeEnum responseCodeEnum) {
        super(responseCodeEnum.getMessage());
        this.responseCodeEnum = responseCodeEnum;
    }

    public ServiceException(ResponseCodeEnum responseCodeEnum, String msg) {
        super(msg);
        this.responseCodeEnum = responseCodeEnum;
    }

    public ServiceException(ResponseCodeEnum responseCodeEnum, Throwable cause) {
        super(cause);
        this.responseCodeEnum = responseCodeEnum;
    }

    public ServiceException(String msg, Throwable cause) {
        super(msg, cause);
        this.responseCodeEnum = ResponseCodeEnum.FAILURE;
    }

    @Override
    public Throwable fillInStackTrace() {
        return this;
    }

    public Throwable doFillInStackTrace() {
        return super.fillInStackTrace();
    }
}
