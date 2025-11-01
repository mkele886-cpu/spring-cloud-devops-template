package org.example.entity;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * ChatServer (服务端) - 优化版
 *
 * 优化点:
 * 1. 封装为实例类，而不是完全依赖 static main。
 * 2. 增加了优雅停机 (Shutdown Hook)，在服务器关闭时通知所有客户端并释放资源。
 * 3. 广播和客户端移除逻辑更健壮，能处理掉线的客户端。
 */
public class ChatServer {

    private final int port;
    // 存放所有客户端处理器
    private final CopyOnWriteArrayList<ClientHandler> clients;
    private final ExecutorService threadPool;
    private ServerSocket serverSocket;
    private boolean running = true;

    private static final int MAX_THREADS = 50;

    public ChatServer(int port) {
        this.port = port;
        this.clients = new CopyOnWriteArrayList<>();
        // 使用有界线程池
        this.threadPool = Executors.newFixedThreadPool(MAX_THREADS);
    }

    public static void main(String[] args) {
        int port = 8888;
        ChatServer server = new ChatServer(port);
        // 添加关闭钩子，用于优雅停机
        server.addShutdownHook();
        server.start();
    }

    public void start() {
        System.out.println("💬 聊天服务器启动，端口：" + port);
        try {
            serverSocket = new ServerSocket(port);
            running = true;
            while (running) {
                Socket socket = serverSocket.accept(); // 等待客户端连接
                System.out.println("✅ 新客户端连接：" + socket.getRemoteSocketAddress());

                ClientHandler handler = new ClientHandler(socket, this);
                clients.add(handler);
                threadPool.execute(handler); // 使用线程池执行任务
            }
        } catch (IOException e) {
            if (running) { // 只有在服务器还在运行时才打印错误
                System.err.println("❌ 服务器套接字异常: " + e.getMessage());
            }
        } finally {
            stop(); // 确保服务器停止时清理资源
        }
    }

    /**
     * 优雅地停止服务器
     */
    public void stop() {
        if (!running) return;
        running = false;
        try {
            System.out.println("🛑 正在关闭服务器...");
            // 停止接受新连接
            if (serverSocket != null && !serverSocket.isClosed()) {
                serverSocket.close();
            }
            // 向所有客户端发送关闭通知
            broadcast("📢 [服务器] 即将关闭，感谢使用！", null);
            for (ClientHandler client : clients) {
                client.closeSilently(); // 安静地关闭每个客户端
            }
            clients.clear();
            // 关闭线程池
            threadPool.shutdownNow();
            System.out.println("✅ 服务器已成功关闭。");
        } catch (IOException e) {
            System.err.println("❌ 关闭服务器时出错: " + e.getMessage());
        }
    }

    /**
     * 广播消息给所有客户端
     * @param message 消息内容
     * @param sender 发送者 (null 表示是服务器广播)
     */
    public void broadcast(String message, ClientHandler sender) {
        for (ClientHandler client : clients) {
            if (client != sender) {
                client.sendMessage(message);
            }
        }
    }

    /**
     * 移除一个客户端
     * @param handler 要移除的客户端处理器
     */
    public void removeClient(ClientHandler handler) {
        boolean removed = clients.remove(handler);
        if (removed) {
            System.out.println("🔌 客户端 [" + handler.getClientName() + "] 已断开连接。");
            broadcast("📢 用户 [" + handler.getClientName() + "] 已离开聊天室。", null);
        }
    }

    /**
     * 添加 JVM 关闭钩子，确保在程序退出时（如 Ctrl+C）执行
     */
    private void addShutdownHook() {
        Runtime.getRuntime().addShutdownHook(new Thread(this::stop));
    }
}
