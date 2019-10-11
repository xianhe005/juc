package com.hxh.juc;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

class ShareData { //资源类
    private int number = 0;
    private Lock lock = new ReentrantLock();
    private Condition condition = lock.newCondition();

    public void increment() throws Exception {
        lock.lock();
        try {
            // 1 判断
            // 防止虚假唤醒,多线程判断一定要用while，不能用if
            while (number != 0) {
                // 等待，不能生产
                condition.await();
            }
            // 2 干活
            number++;
            System.out.println(Thread.currentThread().getName() + "\t" + number);
            // 3 通知唤醒
            condition.signalAll();
        } finally {
            lock.unlock();
        }
    }

    public void decrement() throws Exception {
        lock.lock();
        try {
            // 1 判断
            while (number == 0) {
                // 等待，不能消费
                condition.await();
            }
            // 2 干活
            number--;
            System.out.println(Thread.currentThread().getName() + "\t" + number);
            // 3 通知唤醒
            condition.signalAll();
        } finally {
            lock.unlock();
        }
    }
}

/**
 * 生产者消费者模式
 * <p>
 * 一个初始值为0的变量 两个线程交替操作 一个加1 一个减1来5轮
 * <p>
 * 1    线程      操作 (方法)   资源类<br/>
 * 2    判断      干活          通知<br/>
 * 3    防止虚假唤醒<br/>
 * <p>
 * <p>
 * 虚假唤醒原因，如下图：<br/>
 * <img src="../../../../../img/spurious_wakeup.png" width="600" height="300" alt="虚假唤醒" /><br/>
 */
public class ProdConsumer_TraditionDemo {

    public static void main(String[] args) {
        ShareData shareData = new ShareData();

        new Thread(() -> {
            for (int i = 1; i <= 5; i++) {
                try {
                    shareData.increment();
                    ThreadUtil.sleep(1);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, "AAA").start();

        new Thread(() -> {
            for (int i = 1; i <= 5; i++) {
                try {
                    shareData.decrement();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, "BBB").start();

        /*new Thread(() -> {
            for (int i = 1; i <= 5; i++) {
                try {
                    shareData.increment();
                    ThreadUtil.sleep(1);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, "CCC").start();

        new Thread(() -> {
            for (int i = 1; i <= 5; i++) {
                try {
                    shareData.decrement();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, "DDD").start();*/
    }
}
