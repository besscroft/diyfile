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
                        .description("Spring shop sample application")
                        .version("v0.1.2")
                        .license(new License().name("Apache 2.0").url("https://github.com/besscroft/Xanadu/blob/main/LICENSE")))
                .externalDocs(new ExternalDocumentation()
                        .description("DiyFile 文档")
                        .url(""));
    }

}
