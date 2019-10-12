package com.hxh.juc;

import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

/*class MyThread implements Runnable{

    @Override
    public void run() {

    }
}*/

class MyThread implements Callable<Integer> {

    @Override
    public Integer call() throws Exception {
        System.out.println("*******************come in Callable");
        ThreadUtil.sleep(2);
        return 1024;
    }
}

/**
 * 多线程中，第3种获得多线程的方式
 */
public class CallableDemo {

    public static void main(String[] args) throws Exception {
        // FutureTask(Callable<V> callable) 将来的任务
        FutureTask<Integer> futureTask = new FutureTask<>(new MyThread());

        new Thread(futureTask, "AA").start();
        new Thread(futureTask, "BB").start();

        //int result02 = futureTask.get();

        System.out.println(Thread.currentThread().getName() + "****************");
        int result01 = 100;

        /*while (!futureTask.isDone()) {
            // 自旋
        }*/

        //建议放在最后,要求获得Callable的计算结果，如果没有计算完成就要去强求，会导致阻塞，直到计算完成
        int result02 = futureTask.get();
        System.out.println("*****reult:" + (result01 + result02));

    }
}
