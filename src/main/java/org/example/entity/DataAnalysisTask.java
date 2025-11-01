package org.example.entity;

import java.util.concurrent.Callable;

//批量数据分析 (模拟)
public class DataAnalysisTask implements Callable<String> {

    private final int dataChunkId;
    private final int dataSize;

    public DataAnalysisTask(int id, int size) {
        this.dataChunkId = id;
        this.dataSize = size;
    }

    @Override
    public String call() throws Exception {
        // 模拟计算量与耗时
        long startTime = System.currentTimeMillis();
        // 假设分析时间与数据量正相关
        Thread.sleep(dataSize / 10 + 100);
        long endTime = System.currentTimeMillis();

        // 假设分析得出一个结果
        int analysisResult = dataSize * 2 / 1000;

        return "✅ 数据块 #" + dataChunkId + " 分析完毕，数据量 " + dataSize + "KB，结果摘要: " + analysisResult;
    }
}
