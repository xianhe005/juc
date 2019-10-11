package com.hxh.juc;

import java.util.concurrent.locks.ReentrantLock;

/**
 * 题目：synchronized和Lock有什么区别？用新的Lock有什么好处？举例说说
 * 1 原始构成
 *   synchronized是关键字，属于JVM层面
 *      monitorenter(底层是通过monitor对象来完成，其实wait/notify等方法也依赖于monitor对象只有在同步块或方法中才能调wait/notify等方法)
 *      monitorexit
 *   Lock是具体类(java.util.concurrent.locks.Lock)是Api层面的锁
 *
 *  2 使用方法
 *    synchronized 不需要用户手动释放锁，当synchronized代码执行完后系统会自动让线程释放对锁的占用
 *    ReentrantLock 则需要用户去手动释放锁，若没有主动释放锁，就有可能导致出现死锁现象
 *    ReentrantLock需要lock()和unlock()方法配合try/finally诗句块来完成
 *
 *  3 等待是否可中断
 *    synchronized 不可中断，除非抛出异常或者正常运行完成
 *  TODO
 *
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
public class SyncAndReentrantLockDemo {

    public static void main(String[] args) {
        // synchronized
        // 1 底层,属于系统级别的 用的monitor（监视器）
        // 2 不会死锁
        //      9: monitorenter //进入
        //      10: aload_1
        //      11: monitorexit // 正常退出
        //      12: goto          20
        //      15: astore_2
        //      16: aload_1
        //      17: monitorexit // 异常退出
        synchronized (new Object()) {

        }

        // ReentrantLock
        // 1 属于API级别的
        //      20: new           #3                  // class java/util/concurrent/locks/ReentrantLock
        new ReentrantLock();
    }
}
