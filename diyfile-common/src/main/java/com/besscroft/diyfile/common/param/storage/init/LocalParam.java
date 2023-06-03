package com.besscroft.diyfile.common.param.storage.init;

import com.besscroft.diyfile.common.param.FileInitParam;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @Description 本地存储初始化参数
 * @Author Bess Croft
 * @Date 2023/2/15 10:37
 */
@Data
@Builder
@EqualsAndHashCode(callSuper = true)
public class LocalParam extends FileInitParam {

    /** 代理访问域名 */
    private String domain;

}
