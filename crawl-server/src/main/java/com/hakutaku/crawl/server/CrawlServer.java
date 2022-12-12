package com.hakutaku.crawl.server;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.hakutaku.crawl.server.mapper")
public class CrawlServer {

    public static void main(String[] args) {
        SpringApplication.run(CrawlServer.class, args);
    }

}
