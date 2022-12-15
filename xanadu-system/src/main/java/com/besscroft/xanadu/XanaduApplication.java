package com.besscroft.xanadu;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@Slf4j
@SpringBootApplication
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
