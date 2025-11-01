package org.example.entity;

import java.util.Random;
import java.util.concurrent.Callable;

//批量文件上传 (模拟)
public class FileUploadTask implements Callable<String> {

    private final String fileName;
    public FileUploadTask(String fileName) { this.fileName = fileName; }

    @Override
    public String call() throws Exception {
        // 模拟网络延迟和上传时间
        int time = new Random().nextInt(1500) + 500;
        Thread.sleep(time);

        // 假设上传成功，返回文件信息
        return "✅ 文件 [" + fileName + "] 上传成功，耗时 " + time + "ms";
    }
}
