package com.bond.yunjian.common.exception;

import com.bond.yunjian.common.result.ResultCode;
import lombok.Getter;

/**
 * 异常基类
 * <p>
 * 所有自定义异常继承此类，统一携带 5 位错误码与提示信息。
 * 不依赖 Spring 注解，可在任意模块使用。
 *
 * @author yunjian
 */
@Getter
public abstract class BaseException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    /** 5 位错误码 */
    private final String code;

    /** 错误码枚举（便于日志与扩展） */
    private final ResultCode errorCode;

    protected BaseException(ResultCode errorCode) {
        super(errorCode.getMsg());
        this.errorCode = errorCode;
        this.code = errorCode.getCode();
    }

    protected BaseException(ResultCode errorCode, String msg) {
        super(msg);
        this.errorCode = errorCode;
        this.code = errorCode.getCode();
    }

    protected BaseException(ResultCode errorCode, String msg, Throwable cause) {
        super(msg, cause);
        this.errorCode = errorCode;
        this.code = errorCode.getCode();
    }
}