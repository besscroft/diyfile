package com.besscroft.diyfile.common.result;

import org.springframework.http.HttpStatus;

import java.util.HashMap;
import java.util.Objects;

/**
 * @Description 通用封装返回对象
 * @Author Bess Croft
 * @Date 2022/12/15 14:39
 */
public class AjaxResult extends HashMap<String, Object> {

    private static final long serialVersionUID = 1L;

    /** 状态码 */
    public static final String CODE_TAG = "code";

    /** 返回内容 */
    public static final String MSG_TAG = "message";

    /** 数据对象 */
    public static final String DATA_TAG = "data";

    /**
     * 初始化一个新创建的 AjaxResult 对象，使其表示一个空消息。
     */
    public AjaxResult() {
    }

    /**
     * 初始化一个新创建的 AjaxResult 对象
     *
     * @param code 状态码
     * @param message 返回内容
     */
    public AjaxResult(int code, String message) {
        super.put(CODE_TAG, code);
        super.put(MSG_TAG, message);
    }

    /**
     * 初始化一个新创建的 AjaxResult 对象
     *
     * @param code 状态码
     * @param message 消息提示
     * @param data 数据对象
     */
    public AjaxResult(int code, String message, Object data) {
        super.put(CODE_TAG, code);
        super.put(MSG_TAG, message);
        if (Objects.nonNull(data)) {
            super.put(DATA_TAG, data);
        }
    }

    /**
     * 成功封装方法
     *
     * @return 成功消息
     */
    public static AjaxResult success() {
        return AjaxResult.success("操作成功！");
    }

    /**
     * 成功封装方法
     *
     * @param message 消息提示
     * @return 成功消息
     */
    public static AjaxResult success(String message) {
        return AjaxResult.success(message, null);
    }

    /**
     * 成功封装方法
     *
     * @param data 数据对象
     * @return 成功消息
     */
    public static AjaxResult success(Object data) {
        return AjaxResult.success("操作成功！", data);
    }

    /**
     * 成功封装方法
     *
     * @param message 消息提示
     * @param data 数据对象
     * @return 成功消息
     */
    public static AjaxResult success(String message, Object data) {
        return new AjaxResult(HttpStatus.OK.value(), message, data);
    }

    /**
     * 失败封装方法
     *
     * @return 错误消息
     */
    public static AjaxResult error() {
        return AjaxResult.error("操作失败!");
    }

    /**
     * 失败封装方法
     *
     * @param message 消息提示
     * @return 错误消息
     */
    public static AjaxResult error(String message) {
        return AjaxResult.error(message, null);
    }

    /**
     * 失败封装方法
     *
     * @param message 消息提示
     * @param data 数据对象
     * @return 错误消息
     */
    public static AjaxResult error(String message, Object data) {
        return new AjaxResult(HttpStatus.INTERNAL_SERVER_ERROR.value(), message, data);
    }

    /**
     * 失败封装方法
     *
     * @param code 状态码
     * @param message 消息提示
     * @return 错误消息
     */
    public static AjaxResult error(int code, String message) {
        return new AjaxResult(code, message, null);
    }

}
