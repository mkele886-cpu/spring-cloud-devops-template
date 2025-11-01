package org.example.entity;

import java.util.Random;
import java.util.concurrent.Callable;

public class TaskTest implements Callable<String> {

    private final int id;

    public TaskTest(int id) {
        this.id = id;
    }

    @Override
    public String call() throws Exception {
        // 模拟任务执行时间
        int time = new Random().nextInt(2000) + 1000;
        Thread.sleep(time);
        return "线程 "+Thread.currentThread().getName()+"任务 " + id + " 完成，耗时 " + time + "ms";
    }
}
