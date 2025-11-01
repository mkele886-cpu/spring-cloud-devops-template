package org.example.entity;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;

/**
 * 客户端处理器 - 优化版
 *
 * 优化点:
 * 1. 实现 Runnable 接口，而不是继承 Thread。
 * 2. 持有 ChatServer 引用，以便调用 broadcast 和 removeClient。
 * 3. 实现了更健壮的 close() 方法，并由 ChatServer 统一管理移除。
 * 4. sendMessage 方法增加了IO异常处理，如果发送失败则自动关闭连接。
 */
public class ClientHandler implements Runnable {

    private final Socket socket;
    private final ChatServer server;
    private BufferedReader reader;
    private BufferedWriter writer;
    private String clientName;

    public ClientHandler(Socket socket, ChatServer server) {
        this.socket = socket;
        this.server = server;
        try {
            this.reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        } catch (IOException e) {
            System.err.println("❌ 初始化 ClientHandler 失败: " + e.getMessage());
            closeSilently(); // 初始化失败，立即关闭
        }
    }

    @Override
    public void run() {
        try {
            // 1. 获取昵称
            sendMessage("欢迎来到聊天室，请输入您的昵称：");
            clientName = reader.readLine();
            if (clientName == null || clientName.trim().isEmpty()) {
                clientName = "匿名用户_" + socket.getPort();
            }

            // 2. 广播加入信息
            String joinMessage = "📢 用户 [" + clientName + "] 加入了聊天！";
            System.out.println("用户 " + clientName + " (来自 " + socket.getRemoteSocketAddress() + ") 已连接。");
            server.broadcast(joinMessage, this); // 广播给其他人
            sendMessage("你好, " + clientName + "! (输入 'exit' 退出)"); // 私信欢迎

            // 3. 循环接收和广播消息
            String message;
            while ((message = reader.readLine()) != null) {
                if ("exit".equalsIgnoreCase(message.trim())) {
                    break; // 客户端主动退出
                }
                String formattedMessage = "[" + clientName + "]: " + message;
                System.out.println(formattedMessage); // 在服务器控制台打印
                server.broadcast(formattedMessage, this); // 广播给其他客户端
            }
        } catch (SocketException e) {
            // 客户端强制断开 (如关闭终端)
            System.out.println("🔌 客户端 [" + clientName + "] 异常断开: " + e.getMessage());
        } catch (IOException e) {
            System.err.println("❌ 客户端 [" + clientName + "] 读写错误: " + e.getMessage());
        } finally {
            // 无论如何，最后都要关闭和移除
            closeAndRemove();
        }
    }

    /**
     * 发送消息给这个客户端
     * @param message 消息内容
     */
    public void sendMessage(String message) {
        if (writer != null) {
            try {
                writer.write(message);
                writer.newLine();
                writer.flush();
            } catch (IOException e) {
                // 发送失败，意味着此客户端可能已断开
                System.err.println("❌ 向 [" + clientName + "] 发送消息失败，关闭连接。");
                closeAndRemove(); // 触发关闭和移除
            }
        }
    }

    /**
     * 关闭资源并通知服务器移除此客户端
     */
    private void closeAndRemove() {
        server.removeClient(this); // 通知服务器移除
        closeSilently(); // 关闭自己的资源
    }

    /**
     * 安静地关闭 IO 资源，不抛出异常
     */
    public void closeSilently() {
        try {
            if (reader != null) reader.close();
        } catch (IOException e) { /* 忽略 */ }
        try {
            if (writer != null) writer.close();
        } catch (IOException e) { /* 忽略 */ }
        try {
            if (socket != null && !socket.isClosed()) socket.close();
        } catch (IOException e) { /* 忽略 */ }
    }

    public String getClientName() {
        return (clientName == null || clientName.isEmpty()) ? "未知用户" : clientName;
    }
}
