package com.besscroft.diyfile.common.param.storage.init;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @Description 腾讯云 COS 初始化参数
 * @Author Bess Croft
 * @Date 2023/8/13 17:11
 */
@Data
@Builder
@EqualsAndHashCode(callSuper = true)
public class QCloudCosParam extends OssParam {

    /** secretId */
    private String secretId;

    /** secretKey */
    private String secretKey;

    /** 地域 */
    private String region;

    /** endpoint 端点 */
    private String endpoint;

}
