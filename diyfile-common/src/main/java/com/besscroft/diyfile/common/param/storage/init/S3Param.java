package com.besscroft.diyfile.common.param.storage.init;

import com.besscroft.diyfile.common.param.FileInitParam;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @Description S3 初始化参数
 * @Author Bess Croft
 * @Date 2023/3/10 23:27
 */
@Data
@EqualsAndHashCode(callSuper = true)
public abstract class S3Param extends FileInitParam {

    /** Bucket 名称 */
    private String bucketName;

}
