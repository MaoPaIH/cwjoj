package com.cwj.cwjoj;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.cwj.cwjoj.mapper")
public class CwjojApplication {

    public static void main(String[] args) {
        SpringApplication.run(CwjojApplication.class, args);
    }

}
