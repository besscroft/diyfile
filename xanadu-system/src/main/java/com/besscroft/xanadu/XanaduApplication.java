package com.besscroft.xanadu;

import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@Slf4j
@EnableCaching
@MapperScan("com.besscroft.xanadu.mapper")
@SpringBootApplication(scanBasePackages = "com.besscroft.xanadu")
public class XanaduApplication {

    public static void main(String[] args) {
        SpringApplication.run(XanaduApplication.class, args);
        log.info("""
                            /\\             /\\
                           |`\\\\_,--="=--,_//`|
                           \\ ."  :'. .':  ". /
                          ==)  _ :  '  : _  (==
                            |>/O\\   _   /O\\<|
                            | \\-"~` _ `~"-/ |
                           >|`===. \\_/ .===`|<
                     .-"-.   \\==='  |  '===/   .-"-.
                .---{'. '`}---\\,  .-'-.  ,/---{.'. '}---.
                 )  `"---"`     `~-===-~`     `"---"`  (
                (                 Xanadu                )
                 )         SPRING IS IN THE AIR!       (
                '---------------------------------------'
                """);
    }

}
