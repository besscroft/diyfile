package com.besscroft.diyfile.common.param.file;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * @Description 获取上传地址请求参数
 * @Author Bess Croft
 * @Date 2023/2/8 21:34
 */
@Data
public class GetUploadUrlParam {

    /** 存储 key */
    @NotBlank(message = "存储 key 不能为空")
    private String storageKey;

    /** 地址 */
    @NotBlank(message = "地址不能为空")
    private String path;

}
