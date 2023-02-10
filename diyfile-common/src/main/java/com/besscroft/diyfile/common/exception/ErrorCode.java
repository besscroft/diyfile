package com.besscroft.diyfile.common.exception;

import lombok.Data;

/**
 * @Description 异常错误码对象
 * @Author Bess Croft
 * @Date 2022/12/15 14:43
 */
@Data
public class ErrorCode {

    /** 错误码 */
    private final Integer code;

    /** 错误提示 */
    private final String message;

    public ErrorCode(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

}
