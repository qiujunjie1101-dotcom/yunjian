package com.bond.yunjian.common.exception;

import com.bond.yunjian.common.result.ResultCode;

public class SystemException extends BaseException {

    protected SystemException(ResultCode errorCode) {
        super(errorCode);
    }

    protected SystemException(ResultCode errorCode, String msg) {
        super(errorCode, msg);
    }

    protected SystemException(ResultCode errorCode, String msg, Throwable cause) {
        super(errorCode, msg, cause);
    }
}
