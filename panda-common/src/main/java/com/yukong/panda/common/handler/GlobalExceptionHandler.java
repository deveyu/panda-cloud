package com.yukong.panda.common.handler;

import com.yukong.panda.common.enums.ResponseCodeEnum;
import com.yukong.panda.common.exception.PermissionDeniedException;
import com.yukong.panda.common.exception.ServiceException;
import com.yukong.panda.common.util.ApiResult;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.internal.engine.path.PathImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.Set;

/**
 * @author: ydc
 * 怎样处理异常？
 * service层抛出运行时异常ServiceException(code,message),统一异常处理器捕获这个异常
 * 首先打印日志，然后将code,message封装到响应实体中
 *
 * !要分清状态码的message和异常的message
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {


    /**
     * 参数为空异常
     * @param e
     * @return
     */
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ApiResult handleError(MissingServletRequestParameterException e) {
        log.warn("Missing Request Parameter", e);
        String message = String.format("Missing Request Parameter: %s", e.getParameterName());
        return ApiResult
                .builder()
                .code(ResponseCodeEnum.PARAM_MISS)
                .message(message)
                .build();
    }

    /**
     * 参数类型异常
     * @param e
     * @return
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ApiResult handleError(MethodArgumentTypeMismatchException e) {
        log.warn("Method Argument Type Mismatch", e);
        String message = String.format("Method Argument Type Mismatch: %s", e.getName());
        return ApiResult
                .builder()
                .code(ResponseCodeEnum.PARAM_TYPE_ERROR)
                .message(message)
                .build();
    }

    /**
     * 参数不合法异常
     * @param e
     * @return
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ApiResult handleError(MethodArgumentNotValidException e) {
        log.warn("Method Argument Not Valid", e);
        BindingResult result = e.getBindingResult();
        FieldError error = result.getFieldError();
        String message = String.format("%s:%s", error.getField(), error.getDefaultMessage());
        return ApiResult
                .builder()
                .code(ResponseCodeEnum.PARAM_VALID_ERROR)
                .message(message)
                .build();
    }

    /**
     *
     * @param e
     * @return
     */
    @ExceptionHandler(BindException.class)
    public ApiResult handleError(BindException e) {
        log.warn("Bind Exception", e);
        FieldError error = e.getFieldError();
        String message = String.format("%s:%s", error.getField(), error.getDefaultMessage());
        return ApiResult
                .builder()
                .code(ResponseCodeEnum.PARAM_BIND_ERROR)
                .message(message)
                .build();
    }

    /**
     *
     * @param e
     * @return
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public ApiResult handleError(ConstraintViolationException e) {
        log.warn("Constraint Violation", e);
        Set<ConstraintViolation<?>> violations = e.getConstraintViolations();
        ConstraintViolation<?> violation = violations.iterator().next();
        String path = ((PathImpl) violation.getPropertyPath()).getLeafNode().getName();
        String message = String.format("%s:%s", path, violation.getMessage());
        return ApiResult
                .builder()
                .code(ResponseCodeEnum.PARAM_VALID_ERROR)
                .message(message)
                .build();
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public ApiResult handleError(NoHandlerFoundException e) {
        log.error("404 Not Found", e);
        return ApiResult
                .builder()
                .code(ResponseCodeEnum.NOT_FOUND)
                .message(e.getMessage())
                .build();
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ApiResult handleError(HttpMessageNotReadableException e) {
        log.error("Message Not Readable", e);
        return ApiResult
                .builder()
                .code(ResponseCodeEnum.MSG_NOT_READABLE)
                .message(e.getMessage())
                .build();
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ApiResult handleError(HttpRequestMethodNotSupportedException e) {
        log.error("Request Method Not Supported", e);
        return ApiResult
                .builder()
                .code(ResponseCodeEnum.METHOD_NOT_SUPPORTED)
                .message(e.getMessage())
                .build();
    }

    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public ApiResult handleError(HttpMediaTypeNotSupportedException e) {
        log.error("Media Type Not Supported", e);
        return ApiResult
                .builder()
                .code(ResponseCodeEnum.MEDIA_TYPE_NOT_SUPPORTED)
                .message(e.getMessage())
                .build();
    }

    @ExceptionHandler(ServiceException.class)
    public ApiResult handleError(ServiceException e) {
        log.error("Service Exception", e);
        return ApiResult
                .builder()
                .code(e.getResponseCodeEnum())
                .message(e.getMessage())
                .build();
    }

    @ExceptionHandler(PermissionDeniedException.class)
    public ApiResult handleError(PermissionDeniedException e) {
        log.error("Permission Denied", e);
        return ApiResult
                .builder()
                .code(e.getResponseCodeEnum())
                .message(e.getMessage())
                .build();
    }


    @ExceptionHandler(Throwable.class)
    public ApiResult handleError(Throwable e) {
        log.error("Internal Server Error", e);
        return ApiResult
                .builder()
                .code(ResponseCodeEnum.INTERNAL_SERVER_ERROR)
                .message(e.getMessage())
                .build();
    }

}
