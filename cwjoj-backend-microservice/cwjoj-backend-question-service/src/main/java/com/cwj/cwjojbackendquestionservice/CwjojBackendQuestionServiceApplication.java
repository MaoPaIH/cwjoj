package com.cwj.cwjojbackendquestionservice;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@MapperScan("com.cwj.cwjojbackendquestionservice.mapper")
@EnableDiscoveryClient
@EnableFeignClients(basePackages = {"com.cwj.cwjojbackendserviceclient"})
public class CwjojBackendQuestionServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(CwjojBackendQuestionServiceApplication.class, args);
    }

}
