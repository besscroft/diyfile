package com.besscroft.diyfile.common.param.storage.init;

import com.besscroft.diyfile.common.param.FileInitParam;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @Description OneDrive 初始化参数
 * @Author Bess Croft
 * @Date 2023/1/20 15:58
 */
@Data
@Builder
@EqualsAndHashCode(callSuper = true)
public class OneDriveParam extends FileInitParam {

    /** 客户端 id */
    private String clientId;

    /** 客户端机密 */
    private String clientSecret;

    /** 回调地址 */
    private String redirectUri;

    /** 访问令牌 */
    private String accessToken;

    /** 刷新令牌 */
    private String refreshToken;

    /** 代理地址 */
    private String proxyUrl;

}
