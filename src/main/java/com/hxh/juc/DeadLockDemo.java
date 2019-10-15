package com.hxh.juc;

class HoldLockThread implements Runnable {

    private Object lockA;
    private Object lockB;

    public HoldLockThread(Object lockA, Object lockB) {
        this.lockA = lockA;
        this.lockB = lockB;
    }

    @Override
    public void run() {
        synchronized (lockA) {
            System.out.println(Thread.currentThread().getName() + "\t自已持有：" + lockA + "\t尝试获得" + lockB);
            ThreadUtil.sleep(2);
            synchronized (lockB) {
                System.out.println(Thread.currentThread().getName() + "\t自已持有：" + lockB + "\t尝试获得" + lockA);
            }
        }
    }
}

/**
 * 死锁是指两个或两个以上的进程在执行过程中，因争夺资源而造成的一种互相等待的现象，
 * 若无外力进干涉那它们都将无法推进下去。
 * <p>
 * 死锁是什么?<br/>
 * <img src="../../../../../img/deadlock.png" width="800" height="300" alt="死锁" /><br/>
 */
public class DeadLockDemo {

    /*
     * linux    ps -ef|grep xxxx    ls -l
     * windows下的java运行程序 也有类似ps的查看进程的命令，但是目前我们需要查看的只是java
     *          jps = java ps       jps -l
     */

    /**
     * 死锁java命令分析<br/><br/>
     * 1.通过jps -l命令，查看应用进程ID<br/>
     * <img src="../../../../../img/jps.png" width="800" height="300" alt="jps -l" /><br/>
     * <br/>
     * 2.通过jstack 15056(应用进程ID)命令查看错误栈信息<br/>
     * <img src="../../../../../img/jstack.png" width="800" height="300" alt="jstack 15056(应用进程ID)" /><br/>
     * <br/>
     * 3.发现死板，分析死锁<br/>
     * <img src="../../../../../img/deadlock.png" width="800" height="300" alt="分析死锁" /><br/>
     */
    public static void main(String[] args) {
        String lockA = "lockA";
        String lockB = "lockB";
        new Thread(new HoldLockThread(lockA, lockB), "AAA").start();
        new Thread(new HoldLockThread(lockB, lockA), "BBB").start();


    }
}
