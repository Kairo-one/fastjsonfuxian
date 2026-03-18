package com.example.vuln;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class FastjsonVulnDemoApplication {
    public static void main(String[] args) {
        SpringApplication.run(FastjsonVulnDemoApplication.class, args);
        System.out.println("Fastjson Vuln Demo 启动成功！");
        System.out.println("访问 http://localhost:8080/ 查看测试接口");
    }
}