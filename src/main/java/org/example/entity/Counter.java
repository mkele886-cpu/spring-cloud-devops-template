package org.example.entity;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Counter {

    private int count = 0;

    private final Lock lock = new ReentrantLock();

    //同步方法1，保证线程安全
    public void increment() {
        lock.lock();
        try {
            count++;
        } finally {
            lock.unlock();
        }
    }


//    //同步方法2，保证线程安全，如果不加synchronized ，多线程环境下会出现数据不一致的问题
//    public synchronized void increment() {
//        count++;
//    }

    public  int getCount() {
        return count;
    }

}
