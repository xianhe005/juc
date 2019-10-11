package com.hxh.juc;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

class Phone implements Runnable {

    public synchronized void sendSMS() throws Exception {
        System.out.println(Thread.currentThread().getName() + "\t invoked sendSMS");
        sendEmail();
    }

    public synchronized void sendEmail() throws Exception {
        System.out.println(Thread.currentThread().getName() + "\t invoked sendEmail");
    }
    //=========================================================

    Lock lock = new ReentrantLock();

    @Override
    public void run() {
        get();
    }

    private void get() {
        lock.lock();
        //lock.lock();
        try {
            System.out.println(Thread.currentThread().getName() + "\t invoked get");
            set();
        } finally {
            lock.unlock();
        }
    }

    private void set() {
        lock.lock();
        try {
            System.out.println(Thread.currentThread().getName() + "\t invoked set");
        } finally {
            lock.unlock();
        }
    }

    public void multiLock() {
        lock.lock();
        lock.lock();
        try {
            System.out.println(Thread.currentThread().getName() + "\t multiLock");
        } finally {
            lock.unlock();
            lock.unlock();
        }
    }
}

/**
 * 可重入锁(也叫做递归锁)<br/>
 * <p>
 * 指的在同一线程外层函数获得锁之后，内层递归函数仍然能获取该锁的代码
 * 在同一个线程在外层方法获取锁的时候，在进入内层会自动获取锁
 * <p/>
 * 也就是说，线程可以进入任何一个它已经拥有的锁所同步着的代码块
 * <p>
 * 公平锁/非公平锁<br/>
 * 并发包ReentrantLock的创建可以指定构造函数的boolean类型来得到公平锁或者非公平锁 默认是非公平锁<br/>
 * 见下图<br/>
 * <img src="../../../../../img/fair.png" width="700" height="160" alt="公平锁/非公平锁"/>
 */
public class ReentrantLockDemo {

    //公平锁
    //    是指多个线程按照申请锁的顺序来获取锁类似排队打饭 先来后到
    //非公平锁
    //    是指在多线程获取锁的顺序并不是按照申请锁的顺序,有可能后申请的线程比先申请的线程优先获取到锁,
    //    在高并发的情况下,有可能造成优先级反转或者饥饿现象
    // 公平锁/非公平锁
    //    并发包ReentrantLock的创建可以指定构造函数的boolean类型来得到公平锁或者非公平锁 默认是非公平锁
    //
    // Java ReentrantLock而言,
    //    通过构造函数指定该锁是否是公平锁 默认是非公平锁 非公平锁的优点在于吞吐量必公平锁大.
    //
    //    对于synchronized而言 也是一种非公平锁.


    /**
     * 可重入锁(也叫做递归锁)
     * 指的是同一先生外层函数获得锁后,内层敌对函数任然能获取该锁的代码
     * 在同一线程外外层方法获取锁的时候,在进入内层方法会自动获取锁
     * <p>
     * 也就是说,线程可以进入任何一个它已经标记的锁所同步的代码块
     */
    public static void main(String[] args) {
        Phone phone = new Phone();
        new Thread(() -> {
            try {
                phone.sendSMS();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }, "t1").start();

        new Thread(() -> {
            try {
                phone.sendSMS();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }, "t2").start();

        ThreadUtil.sleep(1);
        System.out.println("===========================");

        new Thread(phone, "t3").start();
        new Thread(phone, "t4").start();

        ThreadUtil.sleep(1);
        System.out.println("===========================");
        new Thread(() -> {
            phone.multiLock();
        }, "t5").start();

    }
}
