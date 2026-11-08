package com.bond.yunjian.common.result;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 错误码枚举
 * <p>
 * 阿里巴巴规范：错误码为 5 位字符串，格式为「来源字母 + 4 位数字」：
 * A = 用户端错误（客户端参数、权限、登录等）
 * B = 当前系统错误（服务端自身异常）
 * C = 第三方服务错误（外部依赖调用失败）
 * <p>
 * 编码段位约定：
 * A0xxx / B0xxx / C0xxx：通用错误
 * A04xx：参数与校验类
 * A02xx：登录与认证类
 * A03xx：权限类
 * B01xx：系统繁忙/超时
 * B03xx：数据库/中间件
 *
 * @author yunjian
 */
@Getter
@RequiredArgsConstructor
public enum ResultCode {

    // ==================== 成功 ====================
    SUCCESS("00000", "success"),

    // ==================== 用户端错误 A ====================
    USER_PARAM_INVALID("A0400", "请求参数不合法"),
    USER_PARAM_MISSING("A0401", "请求参数缺失"),
    USER_PARAM_FORMAT_ERROR("A0402", "请求参数格式错误"),
    USER_NOT_LOGIN("A0201", "用户未登录或登录已过期"),
    USER_LOGIN_FAILED("A0202", "用户名或密码错误"),
    USER_TOKEN_INVALID("A0203", "登录凭证无效"),
    USER_NO_PERMISSION("A0301", "用户无权限访问该资源"),
    USER_ACCOUNT_DISABLED("A0302", "用户账号已被禁用"),
    USER_NOT_FOUND("A0404", "用户不存在"),
    USER_REPEAT_SUBMIT("A0405", "请勿重复提交"),
    USER_REGISTER_EXISTS("A0406", "该用户已注册"),

    // ==================== 当前系统错误 B ====================
    SYSTEM_ERROR("B0001", "系统内部错误，请稍后重试"),
    SYSTEM_TIMEOUT("B0101", "系统处理超时"),
    SYSTEM_BUSY("B0102", "系统繁忙，请稍后重试"),
    SYSTEM_SERVICE_UNAVAILABLE("B0103", "服务暂不可用"),
    DB_ERROR("B0300", "数据库操作异常"),
    DB_DUPLICATE_KEY("B0301", "数据已存在，请勿重复添加"),
    CACHE_ERROR("B0302", "缓存服务异常"),
    MQ_ERROR("B0303", "消息队列异常"),

    // ==================== 第三方服务错误 C ====================
    THIRD_PARTY_ERROR("C0001", "第三方服务调用失败"),
    THIRD_PARTY_TIMEOUT("C0101", "第三方服务调用超时"),
    THIRD_PARTY_RETURN_ERROR("C0102", "第三方服务返回异常");

    /** 5 位错误码 */
    private final String code;

    /** 默认用户提示信息 */
    private final String msg;
}