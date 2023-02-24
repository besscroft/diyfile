package com.besscroft.diyfile;

import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@Slf4j
@EnableCaching
@MapperScan("com.besscroft.diyfile.mapper")
@SpringBootApplication(scanBasePackages = "com.besscroft.diyfile")
public class DiyFileApplication {

    public static void main(String[] args) {
        SpringApplication.run(DiyFileApplication.class, args);
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
                (                 DiyFile               )
                 )         SPRING IS IN THE AIR!       (
                '---------------------------------------'
                """);
    }

}
