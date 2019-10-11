package com.hxh.juc;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * 1 队列
 * <p>
 * 2 阻塞队列
 * <p>
 * 2.1 阻塞队列有没有好的一面
 * <p>
 * 2.2 不得不阻塞，你如何管理
 */
public class BlockingQueueDemo {

    /**
     * 阻塞队列(BlockingQueue) 原理：<br/>
     * <img src="../../../../../img/blockingqueue.png" width="600" height="300" alt="队列+阻塞队列" /><br/>
     * <br/>
     * 阻塞队列的好处<br/>
     * <img src="../../../../../img/blockingqueue2.png" width="600" height="300" alt="阻塞队列的好处" /><br/>
     * <br/>
     * 阻塞队列架构体系<br/>
     * <img src="../../../../../img/blockingqueue_fw.png" width="800" height="400" alt="阻塞队列架构" /><br/>
     * <br/>
     * BlockingQueue的核心方法<br/>
     * <img src="../../../../../img/bqcore_method.png" width="800" height="400" alt="BlockingQueue的核心方法" /><br/>
     */
    public static void main(String[] args) throws Exception {

        //exceptionGroup();
        //specialGroup();
        //blockGroup();
        timeOutGroup();

    }

    // 超时退出组 Times out
    private static void timeOutGroup() throws InterruptedException {
        // List<String> list = new ArrayList<>();
        BlockingQueue<String> blockingQueue = new ArrayBlockingQueue<>(3);
        // offer(E,long, TimeUnit) 插入方法,当阻塞队列满时,队列会阻塞生产者线程一定时间,超过后限时后生产者线程就会退出
        System.out.println(blockingQueue.offer("a", 1, TimeUnit.SECONDS));
        System.out.println(blockingQueue.offer("b", 1, TimeUnit.SECONDS));
        System.out.println(blockingQueue.offer("c", 1, TimeUnit.SECONDS));

        System.out.println("=======================");
        // 当阻塞队列满时,队列会阻塞生产者线程一定时间,超过后限时后生产者线程就会退出
        System.out.println(blockingQueue.offer("x", 1, TimeUnit.SECONDS));
        System.out.println("=======================");

        // poll(long, TimeUnit) 移除方法,当阻塞队列空时,消费者试图从队列poll元素,队列会阻塞生产者线程一定时间,超过后限时后生产者线程就会退出
        System.out.println(blockingQueue.poll(1, TimeUnit.SECONDS));
        System.out.println(blockingQueue.poll(1, TimeUnit.SECONDS));
        System.out.println(blockingQueue.poll(1, TimeUnit.SECONDS));

        System.out.println("=======================");
        //当阻塞队列空时,消费者试图从队列poll元素,队列会阻塞生产者线程一定时间,超过后限时后生产者线程就会退出
        System.out.println(blockingQueue.poll(1, TimeUnit.SECONDS));
        System.out.println("=======================");

        // 不支持检查方法
    }

    // 阻塞组 Blocks
    private static void blockGroup() throws InterruptedException {
        // List<String> list = new ArrayList<>();
        BlockingQueue<String> blockingQueue = new ArrayBlockingQueue<>(3);
        // put(E) 插入方法,当阻塞队列满时,生产者继续往队列里面put元素,队列会一直阻塞直到put数据or响应中断退出
        blockingQueue.put("a");
        blockingQueue.put("b");
        blockingQueue.put("c");

        System.out.println("=======================");
        // 当阻塞队列满时,生产者继续往队列里面put元素,队列会一直阻塞直到put数据or响应中断退出
        //blockingQueue.put("x");
        System.out.println("=======================");

        // poll() 移除方法,当阻塞队列空时,消费者试图从队列take元素,队列会一直阻塞消费者线程直到队列可用.
        System.out.println(blockingQueue.take());
        System.out.println(blockingQueue.take());
        System.out.println(blockingQueue.take());
        // 当阻塞队列空时,消费者试图从队列take元素,队列会一直阻塞消费者线程直到队列可用.
        //System.out.println(blockingQueue.take());

        // 不支持检查方法
    }

    // 特殊值组 Special value
    private static void specialGroup() {
        // List<String> list = new ArrayList<>();
        BlockingQueue<String> blockingQueue = new ArrayBlockingQueue<>(3);
        // offer(E) 插入方法,成功返回true 失败返回false
        System.out.println(blockingQueue.offer("a"));
        System.out.println(blockingQueue.offer("b"));
        System.out.println(blockingQueue.offer("c"));
        // 当阻塞队列满时,再往队列里面offer元素会失败，返回false
        System.out.println(blockingQueue.offer("x"));

        System.out.println("=======================");
        // peek() 检查方法，返回阻塞队列头的元素(不会从队列中移除)，当阻塞队列空时,返回null
        System.out.println(blockingQueue.peek());
        System.out.println("=======================");

        //poll() 移除方法,成功返回元素,队列里面没有就返回null
        System.out.println(blockingQueue.poll());
        System.out.println(blockingQueue.poll());
        System.out.println(blockingQueue.poll());
        // 当阻塞队列空时,再往队列poll元素时候返回null
        System.out.println(blockingQueue.poll());

        // 当阻塞队列空时,返回null
        System.out.println(blockingQueue.peek());
    }

    //抛出异常组 Throws exception
    private static void exceptionGroup() {
        // List<String> list = new ArrayList<>();
        BlockingQueue<String> blockingQueue = new ArrayBlockingQueue<>(3);
        // add(E) 插入方法，成功返回true,当阻塞队列满时,再往队列里面add插入元素会抛java.util.IllegalStateException: Queue full
        System.out.println(blockingQueue.add("a"));
        System.out.println(blockingQueue.add("b"));
        System.out.println(blockingQueue.add("c"));
        // 当阻塞队列满时,再往队列里面add插入元素会抛java.util.IllegalStateException: Queue full
        //System.out.println(blockingQueue.add("x"));

        System.out.println("=======================");
        // element() 检查方法,返回阻塞队列头的元素(不会从队列中移除)，当阻塞队列空时,java.util.NoSuchElementException
        System.out.println(blockingQueue.element());
        System.out.println("=======================");

        // remove() 移除方法，返回移出的元素，当阻塞队列空时,再往队列Remove元素时候回抛出java.util.NoSuchElementException
        System.out.println(blockingQueue.remove());
        System.out.println(blockingQueue.remove());
        System.out.println(blockingQueue.remove());
        // 当阻塞队列空时,再往队列Remove元素时候回抛出java.util.NoSuchElementException
        //System.out.println(blockingQueue.remove());

        // 当阻塞队列空时,java.util.NoSuchElementException
        //System.out.println(blockingQueue.element());
    }
}
