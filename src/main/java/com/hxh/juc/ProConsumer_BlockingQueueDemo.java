package com.hxh.juc;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

class MyResource {
    private volatile boolean flag = true; //默认开启，进行生产+消费
    private AtomicInteger atomicInteger = new AtomicInteger();

    BlockingQueue<String> blockingQueue;

    public MyResource(BlockingQueue<String> blockingQueue) {
        this.blockingQueue = blockingQueue;
        System.out.println(blockingQueue.getClass().getName());
    }

    // 生产
    public void myProd() throws Exception {
        String data;
        boolean retValue;
        while (flag) {
            data = atomicInteger.incrementAndGet() + "";
            retValue = blockingQueue.offer(data, 2L, TimeUnit.SECONDS);
            if (retValue) {
                System.out.println(Thread.currentThread().getName() + "\t插入队列" + data + "成功");
            } else {
                System.out.println(Thread.currentThread().getName() + "\t插入队列" + data + "失败");
            }
            ThreadUtil.sleep(1);
        }
        System.out.println(Thread.currentThread().getName() + "\t大老板叫停了，表示flag = false,生产动作结束");
    }

    // 消费
    public void myConsumer() throws Exception {
        String result;
        while (flag) {
            result = blockingQueue.poll(2L, TimeUnit.SECONDS);
            if (null == result || result.equals("")) {
                flag = false;
                System.out.println(Thread.currentThread().getName() + "\t超过2秒钟没有取到蛋糕，消费退出");
                System.out.println();
                System.out.println();
                return;
            }
            System.out.println(Thread.currentThread().getName() + "\t消费队列蛋糕" + result + "成功");
        }
    }

    // 叫停
    public void stop () throws Exception {
        this.flag = false;
    }
}

/**
 * volatile/CAS/atomicInteger/BlockingQueue/线程交互/原子引用
 */
public class ProConsumer_BlockingQueueDemo {

    public static void main(String[] args) {
        MyResource myResource = new MyResource(new ArrayBlockingQueue<>(10));

        new Thread(() -> {
            System.out.println(Thread.currentThread().getName() + "\t生产线程启动");
            System.out.println();
            System.out.println();
            try {
                myResource.myProd();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }, "Prod").start();

        new Thread(() -> {
            System.out.println(Thread.currentThread().getName() + "\t消费线程启动");
            System.out.println();
            System.out.println();
            try {
                myResource.myConsumer();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }, "Consumer").start();

        ThreadUtil.sleep(5);
        System.out.println();
        System.out.println();
        System.out.println();
        System.out.println("5秒钟时间到，大老板main线程叫停,活动结束");

        try {
            myResource.stop();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
