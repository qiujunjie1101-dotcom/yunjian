package com.bond.yunjian.common.result;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;

/**
 * 统一响应结果封装
 * <p>
 * 遵循阿里巴巴开发规范：
 * 1. 错误码为 5 位字符串，格式为「来源字母 + 4 位数字」：
 *    A = 用户端错误，B = 当前系统错误，C = 第三方服务错误；
 * 2. 成功时错误码统一为 "00000"；
 * 3. 对外响应对象设计为不可变，禁止外部直接 new 或 set。
 *
 * @author yunjian
 */
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Result<T> implements Serializable {
    private static final long serialVersionUID = 1L;

    /** 是否成功 */
    private final boolean success;

    /** 错误码：5 位字符串，A/B/C + 4 位数字 */
    private final String code;

    /** 用户提示信息（可直接展示给前端） */
    private final String msg;

    /** 业务数据 */
    private final T data;

    /**
     * 成功携带数据
     * @param data 数据
     */
    public static <T> Result<T> success(T data){
        return new Result<>(true, ResultCode.SUCCESS.getCode(), ResultCode.SUCCESS.getMsg(), data);
    }

    /**
     * 成功不携带数据
     */
    public static <T> Result<T> success(){
        return success(null);
    }

    /**
     * 失败
     * @param code 错误码
     * @param msg 用户提示信息
     */
    public static <T> Result<T> fail(String code, String msg){
        return new Result<>(false, code, msg,null);
    }

    /**
     * 语义化判断
     */
    @JsonIgnore
    public boolean isOk() {
        return success;
    }

    /**
     * 语义化判断
     */
    @JsonIgnore
    public boolean isFail() {
        return !success;
    }

}