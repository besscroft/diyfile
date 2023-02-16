package com.besscroft.diyfile.doc.config;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Description 文档配置
 * @Author Bess Croft
 * @Date 2022/12/15 13:44
 */
@Configuration
public class OpenApiConfiguration {

    @Bean
    public OpenAPI springShopOpenAPI() {
        return new OpenAPI()
                .info(new Info().title("DiyFile")
                        .description("一款好看的在线文件列表程序")
                        .version("v0.1.7")
                        .license(new License().name("MIT license").url("https://github.com/besscroft/diyfile/blob/main/LICENSE")))
                .externalDocs(new ExternalDocumentation()
                        .description("DiyFile 文档")
                        .url("https://doc.diyfile.besscroft.com"));
    }

}
