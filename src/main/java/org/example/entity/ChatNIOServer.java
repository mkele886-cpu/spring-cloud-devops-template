// 包名保持不变
package org.example.entity;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * ChatServer (服务端) - NIO (Non-blocking I/O) 优化版
 *
 * 架构变更:
 * 1. 移除了 ExecutorService (线程池):
 * NIO 模型使用一个"选择器线程" (Selector Thread) 来处理所有客户端的 I/O 事件，
 * 取代了 BIO "一个客户端一个处理线程(或任务)" 的模型。
 * 2. 使用 Selector:
 * 一个选择器用于"监视"所有 Channel (通道) 上的事件 (如：接受连接, 读取数据, 写入数据)。
 * 3. 使用 ServerSocketChannel 和 SocketChannel:
 * 替代 ServerSocket 和 Socket。
 * 4. 使用 ByteBuffer:
 * 所有 I/O 都通过字节缓冲区完成，替代了基于流 (Stream) 的 BufferedReader/PrintWriter。
 * 5. 状态管理:
 * 使用 SelectionKey 的 attachment() 方法来附加客户端的状态 (如昵称、写队列)。
 */
public class ChatNIOServer {

    private final int port;
    private Selector selector;
    private ServerSocketChannel serverSocketChannel;
    private volatile boolean running = true;

    public ChatNIOServer(int port) {
        this.port = port;
    }

    public static void main(String[] args) {
        int port = 8888;
        ChatNIOServer server = new ChatNIOServer(port);
        // 添加关闭钩子，用于优雅停机 (与之前相同)
        server.addShutdownHook();
        server.start();
    }

    public void start() {
        try {
            // 1. 初始化 Selector 和 ServerSocketChannel
            selector = Selector.open();
            serverSocketChannel = ServerSocketChannel.open();

            // 2. 配置为非阻塞
            serverSocketChannel.configureBlocking(false);

            // 3. 绑定端口
            serverSocketChannel.socket().bind(new InetSocketAddress(port));

            // 4. 将 ServerSocketChannel 注册到 Selector, 监听 "接受连接" (OP_ACCEPT) 事件
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

            System.out.println("💬 NIO 聊天服务器启动，端口：" + port);
            running = true;

            // 5. 启动 "选择器线程" (即当前主线程)
            while (running) {
                // 阻塞, 直到至少一个 channel 准备好了 I/O 操作
                selector.select();

                if (!running) {
                    break;
                }

                Set<SelectionKey> selectedKeys = selector.selectedKeys();
                Iterator<SelectionKey> keyIterator = selectedKeys.iterator();

                while (keyIterator.hasNext()) {
                    SelectionKey key = keyIterator.next();
                    // 必须手动移除, 否则下次 select() 会重复处理
                    keyIterator.remove();

                    if (!key.isValid()) {
                        continue;
                    }

                    // 6. 分发事件
                    try {
                        if (key.isAcceptable()) {
                            handleAccept(key);
                        }
                        if (key.isReadable()) {
                            handleRead(key);
                        }
                        if (key.isWritable()) {
                            handleWrite(key);
                        }
                    } catch (IOException e) {
                        // 发生 I/O 异常时 (如客户端崩溃), 断开连接
                        System.err.println("❌ I/O 异常: " + e.getMessage());
                        removeClient(key);
                    }
                }
            }
        } catch (IOException e) {
            if (running) {
                System.err.println("❌ 服务器套接字异常: " + e.getMessage());
            }
        } finally {
            // 确保服务器停止时清理资源 (这也会被 ShutdownHook 调用)
            stop();
        }
    }

    /**
     * 处理新的客户端连接
     */
    private void handleAccept(SelectionKey key) throws IOException {
        ServerSocketChannel serverChannel = (ServerSocketChannel) key.channel();
        SocketChannel clientChannel = serverChannel.accept(); // 绝不会阻塞
        if (clientChannel != null) {
            clientChannel.configureBlocking(false); // 必须设置为非阻塞

            // 为新客户端创建上下文(状态)并附加到 Key
            ClientContext context = new ClientContext();
            SelectionKey clientKey = clientChannel.register(selector, SelectionKey.OP_READ, context);

            System.out.println("✅ 新客户端连接：" + clientChannel.getRemoteAddress());

            // 发送欢迎消息 (放入写队列)
            sendMessage(clientKey, "👋 欢迎! 请输入你的昵称: ");
        }
    }

    /**
     * 处理来自客户端的可读事件
     */
    private void handleRead(SelectionKey key) throws IOException {
        SocketChannel clientChannel = (SocketChannel) key.channel();
        ClientContext context = (ClientContext) key.attachment();
        ByteBuffer readBuffer = context.readBuffer;

        int bytesRead = clientChannel.read(readBuffer);

        if (bytesRead == -1) {
            // 客户端正常关闭连接
            removeClient(key);
            return;
        }

        if (bytesRead > 0) {
            readBuffer.flip(); // 切换到读模式

            // 循环处理所有完整的行 (以 \n 结尾)
            // 这是为了处理 "粘包" (多个消息一次性到达)
            while (true) {
                String line = extractLine(readBuffer);
                if (line == null) {
                    // 缓冲区中没有完整的行了
                    break;
                }

                // 处理消息
                processMessage(key, line.trim());
            }
            readBuffer.compact(); // 压缩缓冲区, 移除非完整的行到开头, 准备下次写入
        }
    }

    /**
     * 处理一个完整的消息行
     */
    private void processMessage(SelectionKey key, String message) {
        ClientContext context = (ClientContext) key.attachment();

        if (context.name == null) {
            // 这是客户端发送的第一条消息, 视为昵称
            context.name = message;
            System.out.println("👤 用户 [" + context.name + "] 已设置昵称。");
            broadcast("📢 用户 [" + context.name + "] 已加入聊天室。", null);
        } else {
            // 正常的聊天消息
            String formattedMessage = "💬 [" + context.name + "]: " + message;
            System.out.println(formattedMessage);
            // 广播 (排除发送者)
            broadcast(formattedMessage, (SocketChannel) key.channel());
        }
    }

    /**
     * 处理可写事件 (当通道的 TCP 缓冲区有空间时)
     */
    private void handleWrite(SelectionKey key) throws IOException {
        SocketChannel clientChannel = (SocketChannel) key.channel();
        ClientContext context = (ClientContext) key.attachment();
        Queue<ByteBuffer> writeQueue = context.writeQueue;

        while (!writeQueue.isEmpty()) {
            ByteBuffer buffer = writeQueue.peek(); // 查看队首的 buffer
            clientChannel.write(buffer);

            if (buffer.hasRemaining()) {
                // 缓冲区未写完 (TCP 缓冲区满了), 停止写入, 等待下次 OP_WRITE
                return;
            } else {
                // 这个 buffer 已写完, 将其出队
                writeQueue.poll();
            }
        }

        // 所有排队的消息都写完了, 取消对 OP_WRITE 的监听 (非常重要!)
        // 否则 selector 会一直触发 OP_WRITE 事件 (CPU 100%)
        key.interestOps(key.interestOps() & ~SelectionKey.OP_WRITE);
    }

    /**
     * 优雅地停止服务器
     */
    public void stop() {
        if (!running) return;
        running = false;
        try {
            System.out.println("🛑 正在关闭服务器...");

            // 唤醒可能在 select() 处阻塞的 Selector 线程
            if (selector != null) {
                selector.wakeup();
            }

            // 向所有客户端发送关闭通知
            broadcast("📢 [服务器] 即将关闭，感谢使用！", null);

            // 关闭所有客户端连接
            if (selector != null) {
                for (SelectionKey key : selector.keys()) {
                    Channel channel = key.channel();
                    if (channel instanceof SocketChannel) {
                        channel.close();
                    }
                }
            }

            // 关闭 ServerSocket
            if (serverSocketChannel != null) {
                serverSocketChannel.close();
            }

            // 关闭 Selector
            if (selector != null) {
                selector.close();
            }

            System.out.println("✅ 服务器已成功关闭。");
        } catch (IOException e) {
            System.err.println("❌ 关闭服务器时出错: " + e.getMessage());
        }
    }

    /**
     * 广播消息给所有客户端
     * @param message 消息内容
     * @param sender 发送者 (null 表示是服务器广播, 或排除此发送者)
     */
    public void broadcast(String message, SocketChannel sender) {
        String messageWithNewline = message + "\n";
        ByteBuffer messageBuffer = StandardCharsets.UTF_8.encode(messageWithNewline);

        for (SelectionKey key : selector.keys()) {
            if (key.isValid() && key.channel() instanceof SocketChannel) {
                SocketChannel targetChannel = (SocketChannel) key.channel();

                if (targetChannel != sender) {
                    // 将消息放入每个客户端的写队列
                    sendMessage(key, messageBuffer.asReadOnlyBuffer());
                }
            }
        }
    }

    /**
     * 将消息放入指定客户端的写队列, 并注册 OP_WRITE
     */
    private void sendMessage(SelectionKey key, String message) {
        sendMessage(key, StandardCharsets.UTF_8.encode(message + "\n"));
    }

    private void sendMessage(SelectionKey key, ByteBuffer messageBuffer) {
        if (!key.isValid()) return;
        ClientContext context = (ClientContext) key.attachment();

        // 将消息放入写队列
        context.writeQueue.offer(messageBuffer);

        // 注册 (或保持) OP_WRITE, 这样 Selector 就会在通道可写时
        // 通知我们去调用 handleWrite()
        key.interestOps(key.interestOps() | SelectionKey.OP_WRITE);
    }

    /**
     * 移除一个客户端
     * @param key 要移除的客户端的 SelectionKey
     */
    private void removeClient(SelectionKey key) {
        ClientContext context = (ClientContext) key.attachment();

        try {
            key.cancel(); // 1. 从 Selector 取消注册
            key.channel().close(); // 2. 关闭 Channel
        } catch (IOException e) {
            // 忽略关闭时的异常
        }

        if (context != null && context.name != null) {
            System.out.println("🔌 客户端 [" + context.name + "] 已断开连接。");
            broadcast("📢 用户 [" + context.name + "] 已离开聊天室。", null);
        } else {
            System.out.println("🔌 一个未命名客户端已断开连接。");
        }
    }

    /**
     * 添加 JVM 关闭钩子 (与之前相同)
     */
    private void addShutdownHook() {
        Runtime.getRuntime().addShutdownHook(new Thread(this::stop));
    }

    // --- 辅助内部类和方法 ---

    /**
     * 用于存储每个客户端的状态 (附加到 SelectionKey)
     */
    private static class ClientContext {
        String name = null;
        ByteBuffer readBuffer = ByteBuffer.allocate(1024); // 读缓冲区
        // 写队列, 用于处理非阻塞写入
        Queue<ByteBuffer> writeQueue = new ConcurrentLinkedQueue<>();
    }

    /**
     * 辅助方法: 从 ByteBuffer 中提取一行 (以 \n 结尾)
     * @return 提取的行 (包含 \n), 或 null (如果没有完整的行)
     */
    private String extractLine(ByteBuffer buffer) {
        int startPos = buffer.position();
        int limit = buffer.limit();

        for (int i = startPos; i < limit; i++) {
            if (buffer.get(i) == '\n') {
                // 找到了一个完整的行
                int lineLength = i - startPos + 1;
                byte[] lineBytes = new byte[lineLength];

                // 从 buffer 批量读取这一行
                buffer.get(lineBytes);

                return new String(lineBytes, StandardCharsets.UTF_8);
            }
        }

        // 没有找到换行符, 重置位置
        buffer.position(startPos);
        return null;
    }
}