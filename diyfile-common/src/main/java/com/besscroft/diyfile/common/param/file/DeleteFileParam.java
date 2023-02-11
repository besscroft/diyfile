package com.besscroft.diyfile.common.param.file;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * @Description 删除文件请求参数
 * @Author Bess Croft
 * @Date 2023/2/11 19:58
 */
@Data
public class DeleteFileParam {

    /** 存储 key */
    @NotBlank(message = "存储 key 不能为空")
    private String storageKey;

    /** 地址 */
    @NotBlank(message = "地址不能为空")
    private String path;

}
