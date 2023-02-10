package com.besscroft.diyfile.config;

import com.ejlchina.okhttps.Config;
import com.ejlchina.okhttps.HTTP;
import okhttp3.OkHttpClient;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

/**
 * @Description OkHttps 配置类
 * @Author Bess Croft
 * @Date 2023/1/20 20:48
 */
@Configuration
public class OkHttpsConfig implements Config {

    @Override
    public void with(HTTP.Builder builder) {
        // 在这里对 HTTP.Builder 做一些自定义的配置
        builder.config((OkHttpClient.Builder okBuilder) -> {
            // 连接超时时间（默认10秒）
            okBuilder.connectTimeout(10, TimeUnit.SECONDS);
            // 写入超时时间（默认10秒）
            okBuilder.writeTimeout(10, TimeUnit.SECONDS);
            // 读取超时时间（默认10秒）
            okBuilder.readTimeout(10, TimeUnit.SECONDS);
        }).build();
    }

}
