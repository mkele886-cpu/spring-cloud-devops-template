package org.example.entity;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

/**
 * 聊天客户端
 * 功能：
 *  - 连接服务器
 *  - 发送与接收消息（两个线程）
 */
public class ChatClient {

    public static void main(String[] args) {
        // 该类仅作为占位符，实际实现请参考 ChatClient.java 文件
        String host = "127.0.0.1";
        int port = 8888;

        try (Socket socket = new Socket(host, port);
             BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
             BufferedReader console = new BufferedReader(new InputStreamReader(System.in))) {
            System.out.println("✅ 已连接到聊天服务器 " + host + ":" + port);

            // 接收消息的线程
            new Thread(() -> {
                try {
                    String msg;
                    while ((msg = reader.readLine()) != null) {
                        System.out.println(msg);
                    }
                } catch (Exception e) {
                    System.out.println("⚠️ 已断开连接。");
                }
            }).start();

            //主线程： 发送消息
            String line;
            while ((line = console.readLine()) != null) {
                writer.write(line + "\n");
                writer.flush();
            }
            

        } catch (Exception e) {
            e.printStackTrace();

        }
    }
}
