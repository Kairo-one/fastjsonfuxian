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
在IDEA中运行 `FastjsonVulnDemoApplication` 类

### ⚠️ 安全提示
仅限授权测试：本项目仅供安全研究、漏洞复现和教育用途

禁止非法使用：请勿用于任何非法或未授权的测试

及时修复：生产环境请升级Fastjson到最新版本（2.0.25+）

防御措施：推荐使用SafeMode或配置白名单

🔍 检测与防御
检测方法
bash
# 查看项目中使用的Fastjson版本
mvn dependency:tree | grep fastjson

# 检查是否存在漏洞版本
# 受影响版本：fastjson <= 1.2.24
# 修复版本：fastjson 1.2.25+
防御建议
升级版本：将Fastjson升级到最新版本

开启SafeMode：1.2.68+版本支持SafeMode

配置白名单：使用ParserConfig.addAccept()

使用AutoType过滤器：自定义反序列化类检查

部署RASP：运行时应用自我保护

📖 参考资源
Fastjson官方文档

CVE-2017-18349详情

Fastjson漏洞分析

marshalsec JNDI工具
