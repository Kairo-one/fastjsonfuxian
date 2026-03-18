# Fastjson 1.2.24 反序列化漏洞复现

## 项目说明
本项目用于复现Fastjson 1.2.24版本的反序列化漏洞（CVE-2017-18349），包含两种经典利用链：
1. **JdbcRowSetImpl利用链** - 基于JNDI注入
2. **TemplatesImpl利用链** - 基于字节码加载

## 环境要求
- JDK 11
- IDEA 2023+
- Maven 3.6+

## 快速开始

### 1. 启动项目
在IDEA中运行 `FastjsonVulnDemoApplication` 类，或执行：
```bash
mvn spring-boot:run