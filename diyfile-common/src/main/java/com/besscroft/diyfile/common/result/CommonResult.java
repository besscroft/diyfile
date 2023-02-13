package com.besscroft.diyfile.common.result;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.http.HttpStatus;

/**
 * @Description 泛型通用返回对象
 * @Author Bess Croft
 * @Date 2022/12/15 14:41
 */
@Data
@Schema(title = "泛型通用返回对象")
public class CommonResult<T> {

    /** 状态码 */
    private int code;

    /** 消息 */
    private String message;

    /** 数据对象 */
    private T data;

    /**
     * 初始化一个新创建的 CommonResult 对象，使其表示一个空消息。
     */
    public CommonResult() {
    }

    /**
     * 初始化一个新创建的 CommonResult 对象
     * @param code 状态码
     * @param message 消息提示
     * @param data 数据对象
     */
    public CommonResult(int code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    /**
     * 成功封装方法
     * @return 成功消息
     */
    public static CommonResult success() {
        return success("调用成功！");
    }

    /**
     * 成功封装方法
     * @param message 消息提示
     * @param <T>
     * @return 成功消息
     */
    public static <T> CommonResult<T> success(String message) {
        return new CommonResult<T>(HttpStatus.OK.value(), message, null);
    }

    /**
     * 成功封装方法
     * @param data 数据对象
     * @param <T>
     * @return 成功消息
     */
    public static <T> CommonResult<T> success(T data) {
        return new CommonResult<T>(HttpStatus.OK.value(), "调用成功！", data);
    }

    /**
     * 成功封装方法
     * @param message 消息提示
     * @param data 数据对象
     * @param <T>
     * @return 成功消息
     */
    public static <T> CommonResult<T> success(String message, T data) {
        return new CommonResult<T>(HttpStatus.OK.value(), message, data);
    }

    /**
     * 失败封装方法
     * @return 错误消息
     */
    public static CommonResult failed() {
        return failed("调用失败");
    }

    /**
     * 失败封装方法
     * @param message 消息提示
     * @param <T>
     * @return 错误消息
     */
    public static <T> CommonResult<T> failed(String message) {
        return new CommonResult<T>(HttpStatus.INTERNAL_SERVER_ERROR.value(), message, null);
    }

    /**
     * 失败封装方法
     * @param message 消息提示
     * @param data 数据对象
     * @param <T>
     * @return 错误消息
     */
    public static <T> CommonResult<T> failed(String message, T data) {
        return new CommonResult<T>(HttpStatus.INTERNAL_SERVER_ERROR.value(), message, data);
    }

    /**
     * 失败封装方法
     * @param data 数据对象
     * @param <T>
     * @return 错误消息
     */
    public static <T> CommonResult<T> failed(T data) {
        return new CommonResult<T>(HttpStatus.INTERNAL_SERVER_ERROR.value(), "调用失败！", data);
    }

    /**
     * 失败封装方法
     * @param code 数据对象
     * @param message 消息提示
     * @param <T>
     * @return 错误消息
     */
    public static <T> CommonResult<T> failed(int code, String message) {
        return new CommonResult<T>(HttpStatus.INTERNAL_SERVER_ERROR.value(), message, null);
    }

    /**
     * 失败封装方法
     * @param code 数据对象
     * @param message 消息提示
     * @param data 数据对象
     * @param <T>
     * @return 错误消息
     */
    public static <T> CommonResult<T> failed(int code, String message, T data) {
        return new CommonResult<T>(code, message, data);
    }

}
