package com.bond.yunjian.common.exception;

import com.bond.yunjian.common.result.ResultCode;
import lombok.NoArgsConstructor;

public class BizException extends BaseException {

    public BizException(ResultCode errorCode) {
        super(errorCode);
    }

    public BizException(ResultCode errorCode, String msg) {
        super(errorCode, msg);
    }

    public BizException(ResultCode errorCode, String msg, Throwable cause) {
        super(errorCode, msg, cause);
    }

}
