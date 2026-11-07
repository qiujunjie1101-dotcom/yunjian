package com.bond.yunjian;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.bond.yunjian.mapper")
public class YunJianApplication {
    public static void main(String[] args) {
        SpringApplication.run(YunJianApplication.class, args);
    }
}
