package com.kz.coderscircle.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 统一API响应结果封装类
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Result<T> {
    private Integer code; // 业务状态码  0-成功  1-失败
    private String message; // 提示信息
    private T data; // 响应数据

    // 成功结果（无响应数据）
    public static <E> Result<E> success() {
        return new Result<>(0, "操作成功", null);
    }

    // 成功结果（有响应数据）
    public static <E> Result<E> success(E data) {
        return new Result<>(0, "操作成功", data);
    }

    // 失败结果
    public static <E> Result<E> error(String message) {
        return new Result<>(1, message, null);
    }
}