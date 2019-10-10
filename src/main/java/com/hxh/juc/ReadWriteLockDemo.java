package com.hxh.juc;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

class MyCache { //资源类
    private volatile Map<String, Object> map = new HashMap<>(); // volatile 保证可见性
    private ReadWriteLock rwLock = new ReentrantReadWriteLock(); // 读写分离

    //写
    public void put(String key, Object value) {
        rwLock.writeLock().lock();
        try {
            System.out.println(Thread.currentThread().getName() + "\t 正在写入：" + key);
            //模拟网络延时
            ThreadUtil.sleep2(300);
            map.put(key, value);
            System.out.println(Thread.currentThread().getName() + "\t 写入完成：");
        } finally {
            rwLock.writeLock().unlock();
        }
    }

    //读
    public Object get(String key) {
        rwLock.readLock().lock();
        try {
            System.out.println(Thread.currentThread().getName() + "\t 正在读取：");
            //模拟网络延时
            ThreadUtil.sleep2(300);
            Object result = map.get(key);
            System.out.println(Thread.currentThread().getName() + "\t 读取完成：" + result);
            return result;
        } finally {
            rwLock.readLock().unlock();
        }
    }
}

/**
 * 多个线程同时操作 一个资源类没有任何问题 所以为了满足并发量 读取共享资源应该可以同时进行<br/>
 * 但是 如果有一个线程想去写共享资源来  就不应该有其他线程可以对资源进行读或写<br/>
 * <p>
 * 小总结:<br/>
 * 读-读能共存<br/>
 * 读-写不能共存<br/>
 * 写-写不能共存<br/>
 * 写操作 原子+独占 整个过程必须是一个完成的统一整体 中间不允许被分割 被打断<br/>
 * <p>
 * 独占锁与共享锁，如下图<br/>
 * <img src="../../../../../img/rwlock.png" width="600" height="180" alt="独占锁与共享锁"/><br/>
 * <p/>
 */
public class ReadWriteLockDemo {

    public static void main(String[] args) {
        MyCache myCache = new MyCache();

        for (int i = 1; i <= 5; i++) {
            int finalI = i;
            new Thread(() -> {
                myCache.put(finalI + "", finalI + "");
            }, String.valueOf(i)).start();
        }

        for (int i = 1; i <= 5; i++) {
            int finalI = i;
            new Thread(() -> {
                myCache.get(finalI + "");
            }, String.valueOf(i)).start();
        }
    }
}
