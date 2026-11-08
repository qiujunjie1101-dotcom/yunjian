package com.bond.yunjian;

import com.bond.yunjian.utils.JwtProperties;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(JwtProperties.class)
@MapperScan("com.bond.yunjian.mapper")
public class YunJianApplication {
    public static void main(String[] args) {
        SpringApplication.run(YunJianApplication.class, args);
    }
}
