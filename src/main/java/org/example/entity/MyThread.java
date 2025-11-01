package org.example.entity;

public class MyThread extends Thread {

    /*
    * 继承 Thread 类并重写 run() 方法。
     *start() 启动线程，不要直接调用 run()。
     * 多线程交替执行，顺序不固定。
     */
    @Override
    public void run() {
        for (int i = 0; i < 50; i++) {
            System.out.println(Thread.currentThread().getName()+"正在执行:" + i);
        }
    }
}
