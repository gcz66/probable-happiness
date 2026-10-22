package com.xiaoyuan;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 校园外卖平台 - 启动类
 */
@SpringBootApplication
public class XiaoyuanApplication {

    public static void main(String[] args) {
        SpringApplication.run(XiaoyuanApplication.class, args);
        System.out.println("校园外卖平台后端启动成功: http://localhost:8080");
    }
}
