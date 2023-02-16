package com.besscroft.diyfile.common.param.storage.init;

import com.besscroft.diyfile.common.param.FileInitParam;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @Description 阿里云 OSS 初始化参数
 * @Author Bess Croft
 * @Date 2023/2/12 22:14
 */
@Data
@Builder
@EqualsAndHashCode(callSuper = true)
public class AliYunOssParam extends FileInitParam {

    /** Endpoint 表示 OSS 对外服务的访问域名 */
    private String endpoint;

    /** AccessKeyId 用于标识用户 */
    private String accessKeyId;

    /** AccessKeySecret 是用户用于加密签名字符串和 OSS 用来验证签名字符串的密钥，必须保密 */
    private String accessKeySecret;

    /** 阿里云 OSS Bucket 名称 */
    private String bucketName;

    /** 挂载路径 */
    private String mountPath;

    /**
     * 获取挂载路径
     * @return 挂载路径
     */
    public String getMountPath() {
        return mountPath;
    }

}
