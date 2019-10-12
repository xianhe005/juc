package com.hxh.juc;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/*
 * 题目：synchronized和Lock有什么区别？用新的Lock有什么好处？举例说说
 * 1 原始构成
 *   synchronized是关键字，属于JVM层面
 *      monitorenter(底层是通过monitor对象来完成，其实wait/notify等方法也依赖于monitor对象只有在同步块或方法中才能调wait/notify等方法)
 *      monitorexit
 *   Lock是具体类(java.util.concurrent.locks.Lock)是Api层面的锁
 * 2 使用方法
 *   synchronized 不需要用户手动释放锁，当synchronized代码执行完后系统会自动让线程释放对锁的占用
 *   ReentrantLock 则需要用户去手动释放锁，若没有主动释放锁，就有可能导致出现死锁现象
 *   ReentrantLock需要lock()和unlock()方法配合try/finally诗句块来完成
 * 3 等待是否可中断
 *   synchronized 不可中断，除非抛出异常或者正常运行完成
 *   ReentrantLock 可中断，
 *      1.设置超时方法 tryLock(long timeout, TimeUnit unit)
 *      2.lockInterruptibly()放代码块中，调用interrupt()方法可中断
 * 4 加锁是否公平
 *   synchronized 非公平锁
 *   ReentrantLock 两者都可以，默认非公平锁，构造方法可以传入boolean值，true为公平锁，false为非公平锁
 * 5 绑定多个条件Condition
 *   synchronized没有
 *   ReentrantLock用来实现分组唤醒需要唤醒的线程们，可以精确唤醒，而不是像synchronized要么随机唤醒一个线程要么唤醒全部线程
 */

/*
 * // synchronized
 *         // 1 底层,属于系统级别的 用的monitor（监视器）
 *         // 2 不会死锁
 *         //      9: monitorenter //进入
 *         //      10: aload_1
 *         //      11: monitorexit // 正常退出
 *         //      12: goto          20
 *         //      15: astore_2
 *         //      16: aload_1
 *         //      17: monitorexit // 异常退出
 *         synchronized (new Object()) {
 *
 *         }
 * // ReentrantLock
 *         // 1 属于API级别的
 *         //      20: new           #3                  // class java/util/concurrent/locks/ReentrantLock
 */

/**
 * 题目：多线程之间按顺序调用，实现A->B->C三个线程启动，要求如下：
 * <p>
 * A打印5次，B打印10次，C打印15次
 * <p>
 * 紧接着
 * <p>
 * A打印5次，B打印10次，C打印15次
 * <p>
 * ...
 * <p>
 * 来10轮
 */

class ShareResource {
    // TODO: 2019/10/12  这里不加volatile为什么也能在多线程可见？？？
    private int number = 1;//A:1 B:2 C:3
    private Lock lock = new ReentrantLock();
    private Condition c1 = lock.newCondition();
    private Condition c2 = lock.newCondition();
    private Condition c3 = lock.newCondition();

    public void print(int curFlag) {
        Condition cur;
        Condition next;
        int repeat;
        if (curFlag == 1) {
            cur = c1;
            next = c2;
            repeat = 5;
        } else if (curFlag == 2) {
            cur = c2;
            next = c3;
            repeat = 10;
        } else {
            cur = c3;
            next = c1;
            repeat = 15;
        }
        lock.lock();
        try {
            //1 判断
            while (number != curFlag) {
                cur.await();
            }
            //2 干活
            for (int i = 1; i <= repeat; i++) {
                System.out.println(Thread.currentThread().getName() + "\t" + i);
            }
            //3 通知
            number++;
            if (number > 3) {
                number = 1;
            }
            System.out.println(Thread.currentThread().getName() + "\t通知下一个线程干活");
            next.signal();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

}

public class SyncAndReentrantLockDemo {

    public static void main(String[] args) {

        ShareResource shareResource = new ShareResource();

        new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                shareResource.print(1);
            }
        }, "A").start();

        new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                shareResource.print(2);
            }
        }, "B").start();

        new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                shareResource.print(3);
            }
        }, "C").start();
    }
}
