package com.jia.ac.common;

import lombok.Getter;

/**
 * @author jialunyin
 * @version 1.0
 */

@Getter
public enum ErrorCode {

    PARAMS_ERROR(40000, "请求参数错误", ""),
    PARAMS_NULL_ERROR(40001, "请求数据为空", ""),
    NO_LOGIN(40100, "没登陆", ""),
    NO_AUTH(40101, "无权限", ""),
    SYSTEM_ERROR(50000, "系统内部异常", "");

    private final int code;
    private final String description;
    private final String message;

    ErrorCode(int code, String message, String description) {
        this.code = code;
        this.description = description;
        this.message = message;
    }

}
