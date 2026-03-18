package com.example.vuln.server;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.*;
import java.net.InetSocketAddress;

/**
 * 简单的HTTP服务器，用于提供恶意class文件
 */
public class SimpleHTTPServer {

    public static void main(String[] args) throws Exception {
        // 监听8000端口
        HttpServer server = HttpServer.create(new InetSocketAddress(8000), 0);
        server.createContext("/", new FileHandler());
        server.setExecutor(null);
        server.start();
        System.out.println("HTTP服务器启动，监听端口: 8000");
        System.out.println("请将编译好的Exploit.class放在当前目录");
    }

    static class FileHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String path = exchange.getRequestURI().getPath();
            System.out.println("收到请求: " + path);

            // 读取文件
            File file = new File("." + path);
            if (file.exists() && !file.isDirectory()) {
                byte[] bytes = readFile(file);
                exchange.sendResponseHeaders(200, bytes.length);
                OutputStream os = exchange.getResponseBody();
                os.write(bytes);
                os.close();
                System.out.println("发送文件: " + file.getName() + " (" + bytes.length + " bytes)");
            } else {
                String response = "File not found";
                exchange.sendResponseHeaders(404, response.length());
                OutputStream os = exchange.getResponseBody();
                os.write(response.getBytes());
                os.close();
            }
        }

        private byte[] readFile(File file) throws IOException {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            FileInputStream fis = new FileInputStream(file);
            byte[] buffer = new byte[1024];
            int len;
            while ((len = fis.read(buffer)) != -1) {
                bos.write(buffer, 0, len);
            }
            fis.close();
            return bos.toByteArray();
        }
    }
}