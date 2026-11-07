package com.bond.yunjian.common.exception;

import com.bond.yunjian.common.result.ResultCode;

public class BizException extends BaseException {

    protected BizException(ResultCode errorCode) {
        super(errorCode);
    }

    protected BizException(ResultCode errorCode, String msg) {
        super(errorCode, msg);
    }

    protected BizException(ResultCode errorCode, String msg, Throwable cause) {
        super(errorCode, msg, cause);
    }
}
