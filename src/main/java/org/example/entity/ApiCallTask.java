package org.example.entity;

import java.util.Random;
import java.util.concurrent.Callable;

// 模拟外部 API 接口调用任务
public class ApiCallTask implements Callable<String> {

    private final String endpoint;
    public ApiCallTask(String endpoint) { this.endpoint = endpoint; }

    @Override
    public String call() throws Exception {
        // 模拟 API 接口响应时间
        int latency = new Random().nextInt(800) + 200;
        Thread.sleep(latency);

        // 假设获取了数据，返回接口状态
        if (latency < 1000) {
            return "✅ 接口 [" + endpoint + "] 调用成功，响应时间 " + latency + "ms";
        } else {
            // 模拟超时或失败
            throw new Exception("❌ 接口 [" + endpoint + "] 调用超时！");
        }
    }
}
