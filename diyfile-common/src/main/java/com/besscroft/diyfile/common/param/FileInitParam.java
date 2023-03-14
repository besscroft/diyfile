package com.besscroft.diyfile.common.param;

import lombok.Data;

/**
 * @Description 文件基础服务初始化参数
 * @Author Bess Croft
 * @Date 2023/1/20 15:43
 */
@Data
public abstract class FileInitParam {

    /** 挂载路径 */
    private String mountPath;

}
