package com.yukong.panda.common.exception;

import com.yukong.panda.common.enums.ResponseCodeEnum;
import lombok.Getter;

/**
 * @author: yukong
 * @date: 2018/10/12 11:03
 * @description:
 */
public class PermissionDeniedException extends RuntimeException {

    @Getter
    private  ResponseCodeEnum responseCodeEnum;//final

    public PermissionDeniedException(String message) {
        super(message);
        this.responseCodeEnum = ResponseCodeEnum.UN_AUTHORIZED;
    }

    public PermissionDeniedException(ResponseCodeEnum resultCode) {
        super(resultCode.getMessage());
        this.responseCodeEnum = resultCode;
    }

    public PermissionDeniedException(ResponseCodeEnum resultCode, Throwable cause) {
        super(cause);
        this.responseCodeEnum = resultCode;
    }

    @Override
    public Throwable fillInStackTrace() {
        return this;
    }
}
