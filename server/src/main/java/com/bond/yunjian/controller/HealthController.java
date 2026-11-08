package com.bond.yunjian.controller;

import com.bond.yunjian.common.result.Result;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthController {

    @GetMapping("/health")
    public Result<String[]> health() {
        String[] name = new String[]{"张三", "李四", "王五"};
        return Result.success(name);
    }

    @GetMapping("/test")
    public Result<String> test(){
        System.out.println(1/0);
        //测试抓异常
        return Result.success("test");
    }
}
