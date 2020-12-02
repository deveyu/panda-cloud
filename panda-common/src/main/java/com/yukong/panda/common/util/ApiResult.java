package com.yukong.panda.common.util;

import com.yukong.panda.common.enums.ResponseCodeEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author: ydc
 * @date: 2018/10/12 10:39
 * @description: 统一响应信息主体
 */
@Data
@AllArgsConstructor
@Builder
public class ApiResult<T> implements Serializable {

    private T data;

    private Integer code = ResponseCodeEnum.SUCCESS.getCode();

    private String message = ResponseCodeEnum.SUCCESS.getMessage();


    public ApiResult(T data) {
        this.data = data;
    }

    public ApiResult(T data, String message) {
        this.data = data;
        this.message = message;
    }

    public ApiResult(T data, ResponseCodeEnum responseCode) {
        this.data = data;
        this.code = responseCode.getCode();
        this.message = responseCode.getMessage();
    }

    public ApiResult(Throwable throwable) {
        this.message = throwable.getMessage();
        this.code = ResponseCodeEnum.INTERNAL_SERVER_ERROR.getCode();
    }

    public ApiResult(Throwable throwable, ResponseCodeEnum  code) {
        this.message = throwable.getMessage();
        this.code = code.getCode();
    }


}
