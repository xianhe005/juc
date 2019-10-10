package com.hxh.juc;

import java.util.concurrent.atomic.AtomicReference;

/**
 * 题目：实现一个自旋锁<br/>
 * 自旋锁的好处：循环比较直到成功为止，没有类似wait的阻塞
 * <p/>
 * 自旋锁原理，如下图<br/>
 * <img src="../../../../../img/spinlock.png" width="600" height="180" alt="自旋锁"/><br/>
 * <p/>
 * 通过CAS操作完成自旋锁，A线程先进来调myLock方法自己持有锁5秒钟，
 * B线程随后进来的发现当前有线程持有锁，不是null，所以只能通过自旋等待，直到A释放锁后B随后抢到
 */
public class SpinLockDemo {

    // 原子引用线程
    AtomicReference<Thread> atomicReference = new AtomicReference<>();

    public void myLock() {

        Thread thread = Thread.currentThread();
        System.out.println(thread.getName() + "\t come in");

        while (!atomicReference.compareAndSet(null, thread)) {

        }

    }

    public void myUnlock() {
        Thread thread = Thread.currentThread();
        atomicReference.compareAndSet(thread, null);
        System.out.println(thread.getName() + "\t invoked myUnlock()");
    }

    public static void main(String[] args) {
        SpinLockDemo spinLockDemo = new SpinLockDemo();
        new Thread(() -> {
            spinLockDemo.myLock();
            ThreadUtil.sleep(5);
            spinLockDemo.myUnlock();
        }, "AAA").start();

        ThreadUtil.sleep(1);

        new Thread(() -> {
            spinLockDemo.myLock();
            ThreadUtil.sleep(1);
            spinLockDemo.myUnlock();
        }, "BBB").start();
    }
}
