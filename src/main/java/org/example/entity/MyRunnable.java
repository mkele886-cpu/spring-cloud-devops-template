package org.example.entity;

public class MyRunnable implements  Runnable{

    //Runnable 更灵活，能与其他类多继承
    //推荐用此方式配合线程池使用
    @Override
    public void run() {
        for (int i = 0; i < 50; i++) {
            System.out.println(Thread.currentThread().getName()+"正在执行:" + i);
        }
    }
}
