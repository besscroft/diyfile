package com.besscroft.diyfile.common.param.storage.init;

import com.besscroft.diyfile.common.param.FileInitParam;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @Description OSS 初始化参数
 * @Author Bess Croft
 * @Date 2023/2/17 12:46
 */
@Data
@EqualsAndHashCode(callSuper = true)
public abstract class OssParam extends FileInitParam {

    /**
     * 获取挂载路径
     * @return 挂载路径
     */
    public String getMountPath() {
        return null;
    }

}
