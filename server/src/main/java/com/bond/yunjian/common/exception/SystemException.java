package com.bond.yunjian.common.exception;

import com.bond.yunjian.common.result.ResultCode;

public class SystemException extends BaseException {

    public SystemException(ResultCode errorCode) {
        super(errorCode);
    }

    public SystemException(ResultCode errorCode, String msg) {
        super(errorCode, msg);
    }

    public SystemException(ResultCode errorCode, String msg, Throwable cause) {
        super(errorCode, msg, cause);
    }
}
