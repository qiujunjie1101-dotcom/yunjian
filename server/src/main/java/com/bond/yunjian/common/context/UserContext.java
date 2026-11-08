package com.bond.yunjian.common.context;

import com.bond.yunjian.common.exception.BizException;
import com.bond.yunjian.common.result.ResultCode;

/**
 * 当前请求的登录用户上下文。
 * 仅由 {@code LoginInterceptor} 写入，且必须在 afterCompletion 中调用 {@link #clear()}，
 * 否则线程池复用线程时会出现用户身份串号。
 */
public final class UserContext {

    private static final ThreadLocal<Long> USER_ID = new ThreadLocal<>();

    private UserContext() {
    }

    public static void setUserId(Long userId) {
        USER_ID.set(userId);
    }

    public static Long getUserId() {
        return USER_ID.get();
    }

    /**
     * 取当前登录用户 ID，未登录直接抛出业务异常。
     */
    public static Long requireUserId() {
        Long userId = USER_ID.get();
        if (userId == null) {
            throw new BizException(ResultCode.USER_NOT_LOGIN);
        }
        return userId;
    }

    public static void clear() {
        USER_ID.remove();
    }
}
