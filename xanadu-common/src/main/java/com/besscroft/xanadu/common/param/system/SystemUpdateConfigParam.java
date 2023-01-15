package com.besscroft.xanadu.common.param.system;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * @Description 系统配置更新请求参数
 * @Author Bess Croft
 * @Date 2023/1/15 13:30
 */
@Data
public class SystemUpdateConfigParam {

    /** 系统配置键 */
    @NotBlank(message = "系统配置键不能为空！")
    private String configKey;

    /** 系统配置值 */
    private String configValue;

}
