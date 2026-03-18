package com.example.vuln.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.Feature;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
public class VulnController {

    /**
     * 漏洞接口1: 直接解析JSON（存在漏洞）
     * 用于复现 JdbcRowSetImpl 利用链
     */
    @PostMapping("/parse")
    public String parse(@RequestBody String json) {
        System.out.println("收到请求: " + json);
        try {
            // 默认调用，存在漏洞
            Object obj = JSON.parse(json);
            return "解析成功: " + obj.toString();
        } catch (Exception e) {
            return "解析异常: " + e.getMessage();
        }
    }

    /**
     * 漏洞接口2: 使用 parseObject（存在漏洞）
     */
    @PostMapping("/parseObject")
    public String parseObject(@RequestBody String json) {
        System.out.println("收到请求: " + json);
        try {
            Object obj = JSON.parseObject(json);
            return "解析成功: " + obj.toString();
        } catch (Exception e) {
            return "解析异常: " + e.getMessage();
        }
    }

    /**
     * 漏洞接口3: TemplatesImpl利用链专用
     * 需要 SupportNonPublicField 特性
     */
    @PostMapping("/parseTemplates")
    public String parseTemplates(@RequestBody String json) {
        System.out.println("收到Templates利用链请求: " + json);
        try {
            // TemplatesImpl利用链需要 SupportNonPublicField 来设置私有字段
            Object obj = JSON.parseObject(json, Object.class, Feature.SupportNonPublicField);
            return "解析成功: " + obj.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return "解析异常: " + e.getMessage();
        }
    }

    /**
     * 测试接口：返回简单的用户对象
     */
    @GetMapping("/user")
    public String getUser() {
        Map<String, Object> user = new HashMap<>();
        user.put("name", "admin");
        user.put("age", 20);
        return JSON.toJSONString(user);
    }

    /**
     * 首页说明
     */
    @GetMapping("/")
    public String index() {
        return "<h1>Fastjson 1.2.24 漏洞复现环境</h1>" +
                "<h2>可用接口：</h2>" +
                "<ul>" +
                "<li>POST /parse - 基础解析接口（JdbcRowSetImpl利用链）</li>" +
                "<li>POST /parseObject - parseObject接口</li>" +
                "<li>POST /parseTemplates - TemplatesImpl利用链专用接口</li>" +
                "<li>GET /user - 测试接口</li>" +
                "</ul>" +
                "<h2>JDK 11+ 必需JVM参数：</h2>" +
                "<code>--add-opens java.xml/com.sun.org.apache.xalan.internal.xsltc.trax=ALL-UNNAMED</code><br>" +
                "<code>--add-opens java.xml/com.sun.org.apache.xalan.internal.xsltc.runtime=ALL-UNNAMED</code><br>" +
                "<code>--add-opens java.xml/com.sun.org.apache.xalan.internal.xsltc.dom=ALL-UNNAMED</code><br>" +
                "<code>--add-opens java.xml/com.sun.org.apache.xalan.internal.xsltc=ALL-UNNAMED</code><br>" +
                "<code>--add-opens java.xml/com.sun.org.apache.xml.internal.serializer=ALL-UNNAMED</code><br>" +
                "<h2>利用链说明：</h2>" +
                "<p>1. JdbcRowSetImpl利用链：需要配合RMI/LDAP服务器</p>" +
                "<p>2. TemplatesImpl利用链：无需外部服务器，直接加载字节码</p>";
    }
}