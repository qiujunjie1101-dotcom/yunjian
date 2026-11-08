package com.bond.yunjian.common.exception;

import com.bond.yunjian.common.result.Result;
import com.bond.yunjian.common.result.ResultCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice // 全局异常处理器——切面思想
public class GlobalExceptionHandler {

    // 1. 处理业务异常（预期内，不打堆栈，提示用户）
    @ExceptionHandler(BizException.class)
    public Result<Void> handleBizException(BizException e) {
        log.warn("业务异常 code={}, msg={}", e.getCode(), e.getMessage());
        return Result.fail(e.getErrorCode().getCode(), e.getMessage());
    }

    // 2. 处理系统异常（预期外，打堆栈，对外隐瞒细节）
    @ExceptionHandler(SystemException.class)
    public Result<Void> handleSystemException(SystemException e) {
        log.error("系统异常 code={}, msg={}", e.getCode(), e.getMessage(), e);
        // 注意：即使 e.getMessage() 里有内部细节，这里也不要用，直接用枚举的通用提示
        return Result.fail(ResultCode.SYSTEM_ERROR.getCode(), ResultCode.SYSTEM_ERROR.getMsg());
    }

    // 3. 处理框架参数校验异常（@Valid）
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST) // HTTP返回 400
    public Result<Void> handleValidException(MethodArgumentNotValidException e) {
        String msg = e.getBindingResult().getFieldError() == null
                ? "参数错误" : e.getBindingResult().getFieldError().getDefaultMessage();
        log.warn("参数校验异常: {}", msg);
        return Result.fail(ResultCode.USER_PARAM_INVALID.getCode(), msg);
    }

    // 4. 终极兜底（所有没被捕获的异常，打堆栈，返回系统繁忙）
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR) // HTTP返回 500
    public Result<Void> handleException(Exception e) {
        log.error("未知系统异常", e);
        return Result.fail(ResultCode.SYSTEM_ERROR.getCode(), ResultCode.SYSTEM_ERROR.getMsg());
    }


}
