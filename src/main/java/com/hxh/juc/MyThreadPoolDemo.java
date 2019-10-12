package com.hxh.juc;

import java.util.concurrent.*;

/**
 * Java中的线程池是通过Executor框架实现的,该框架中用到了Executor,Executors,ExecutorService,ThreadPoolExecutor这几个类.
 * <p>
 * 线程池架构体系
 * <p>
 * <img src="../../../../../img/executor.png" width="600" height="300" alt="线程池架构体系" /><br/>
 * <hr/>
 * 第4种获得/使用java多线程的方式，线程池
 * <hr/>
 * 线程池七大参数：<br/>
 * <img src="../../../../../img/threadpool_parms.png" width="1000" height="130" alt="线程池架构体系" /><br/>
 * <hr/>
 * 线程池底层原理：<br/>
 * <img src="../../../../../img/threadpool_principle1.png" width="800" height="200" alt="线程池底层原理1" /><br/>
 * <img src="../../../../../img/threadpool_principle2.png" width="800" height="200" alt="线程池底层原理1" /><br/>
 */
public class MyThreadPoolDemo {

    //public ThreadPoolExecutor(int corePoolSize,
    //                              int maximumPoolSize,
    //                              long keepAliveTime,
    //                              TimeUnit unit,
    //                              BlockingQueue<Runnable> workQueue,
    //                              ThreadFactory threadFactory,
    //                              RejectedExecutionHandler handler) {
    //        if (corePoolSize < 0 ||
    //            maximumPoolSize <= 0 ||
    //            maximumPoolSize < corePoolSize ||
    //            keepAliveTime < 0)
    //            throw new IllegalArgumentException();
    //        if (workQueue == null || threadFactory == null || handler == null)
    //            throw new NullPointerException();
    //        this.corePoolSize = corePoolSize;
    //        this.maximumPoolSize = maximumPoolSize;
    //        this.workQueue = workQueue;
    //        this.keepAliveTime = unit.toNanos(keepAliveTime);
    //        this.threadFactory = threadFactory;
    //        this.handler = handler;
    //    }

    // 线程池7大参数：类比银行办理业务
    // 1.corePoolSize:线程池中的常驻核心线程数
    //      1.在创建了线程池后,当有请求任务来之后,就会安排池中的线程去执行请求任务,近似理解为今日当值线程
    //      2.当线程池中的线程数目达到corePoolSize后,就会把到达的任务放入到缓存队列当中.
    // 2.maximumPoolSize:线程池能够容纳同时执行的最大线程数,此值大于等于1
    // 3.keepAliveTime:多余的空闲线程存活时间,当空间时间达到keepAliveTime值时,多余的线程会被销毁直到只剩下corePoolSize个线程为止
    //      默认情况下:只有当线程池中的线程数大于corePoolSize时keepAliveTime才会起作用,直到线程中的线程数不大于corepoolSize
    // 4.unit:keepAliveTime的单位
    // 5.workQueue:任务队列,被提交但尚未被执行的任务.
    // 6.threadFactory:表示生成线程池中工作线程的线程工厂,用户创建新线程,一般用默认即可
    // 7.handler:拒绝策略,表示当线程队列满了并且工作线程大于等于线程池的最大显示数(maximumPoolSize)时如何来拒绝.


    // 线程池的拒绝策略请你谈谈
    // 拒绝策略是什么？
    // 等待队列也已经排满了,再也塞不下新的任务了，同时,线程池的max也到达了,无法接续为新任务服务,这时我们需要拒绝策略机制合理的处理这个问题.
    // JDK内置的拒绝策略
    //   1.AbortPolicy(默认):直接抛出RejectedException异常阻止系统正常运行
    //   2.CallerRunPolicy:"调用者运行"一种调节机制,该策略既不会抛弃任务,也不会抛出异常,而是将某些任务回退到调用者，从而降低新任务的流量
    //   3.DiscardOldestPolicy:抛弃队列中等待最久的任务,然后把当前任务加入队列中尝试再次提交
    //   4.DiscardPolicy:直接丢弃任务,不予任何处理也不抛出异常.如果允许任务丢失,这是最好的拒绝策略


    // 你在工作中单一的/固定数的/可变你的三种创建线程池的方法,你用哪个多?超级大坑
    // 答案是一个都不用,我们生产上只能使用自定义的,参考阿里巴巴java开发手册
    //  【强制】线程资源必须通过线程池提供，不允许在应用中自行显式创建线程。
    //      说明：使用线程池的好处是减少在创建和销毁线程上所消耗的时间以及系统资源的开销，解决资源不足的问题。
    //      如果不使用线程池，有可能造成系统创建大量同类线程而导致消耗完内存或者“过度切换”的问题。
    //  【强制】线程池不允许使用Executors去创建，而是通过ThreadPoolExecutor的方式，
    //   这样的处理方式让写的同学更加明确线程池的运行规则，规避资源耗尽的风险。说明：Executors返回的线程池对象的弊端如下：
    //      1）FixedThreadPool和SingleThreadPool:允许的请求队列长度为Integer.MAX_VALUE，可能会堆积大量的请求，从而导致OOM。
    //      2）CachedThreadPool和ScheduledThreadPool:允许的创建线程数量为Integer.MAX_VALUE，可能会创建大量的线程，从而导致OOM。
    public static void main(String[] args) {
        //System.out.println(Runtime.getRuntime().availableProcessors());
        //Array Arrays
        //Collection Collections
        //Executor Executors

        //threadPoolInit();

        ThreadPoolExecutor threadPool = new ThreadPoolExecutor(
                2,
                5,
                1L,
                TimeUnit.SECONDS,
                new ArrayBlockingQueue<>(3),
                Executors.defaultThreadFactory(),
                // 默认抛出异常 java.util.concurrent.RejectedExecutionException
                //new ThreadPoolExecutor.AbortPolicy());
                // 回退调用者
                //new ThreadPoolExecutor.CallerRunsPolicy());
                // 抛弃队列中等待最久的任务
                //new ThreadPoolExecutor.DiscardOldestPolicy());
                // 直接丢弃任务
                new ThreadPoolExecutor.DiscardPolicy());

        try {
            // 模拟多个用户来办理业务，每个用户就是一个来自外部的请求线程
            for (int i = 1; i <= 11; i++) {
                threadPool.execute(() -> {
                    System.out.println(Thread.currentThread().getName() + "\t办理业务");
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            threadPool.shutdown();
        }

        System.out.println(Runtime.getRuntime().availableProcessors());//查看CPU核数
        // 合理配置线程池你是如何考虑的?maximumPoolSize
        // 1.CPU密集型任务配置尽可能少的线程数据,一般公式：CPU核数+1个线程的线程池
        // 2.由于IO密集型任务线程并不是一直在执行任务，则应配置尽可能多的线程，
        //      2.1 如CPU核数*2
        //      2.2 也可以参考公式：CPU核数/(1-阻塞系数),阻塞系数在0.8~0.9之间，比如8核CPU：8/(1-0.9)=80个线程数
    }

    private static void threadPoolInit() {
        // 主要特点如下:
        //  1.创建一个定长线程池,可控制线程的最大并发数,超出的线程会在队列中等待.
        //  2.newFixedThreadPool创建的线程池corePoolSize和MaximumPoolSize是 相等的,它使用的的LinkedBlockingQueue
        //  public static ExecutorService newFixedThreadPool(int nThreads) {
        //        return new ThreadPoolExecutor(nThreads, nThreads,
        //                                      0L, TimeUnit.MILLISECONDS,
        //                                      new LinkedBlockingQueue<Runnable>());
        //    }
        //  适用:执行一个长期的任务,性能好很多
        //ExecutorService threadPool = Executors.newFixedThreadPool(5);//一池5个处理线程

        // 主要特点如下:
        //  1.创建一个单线程化的线程池,它只会用唯一的工作线程来执行任务,保证所有任务都按照指定顺序执行.
        //  2.newSingleThreadExecutor将corePoolSize和MaximumPoolSize都设置为1,它使用的的LinkedBlockingQueue
        //  public static ExecutorService newSingleThreadExecutor() {
        //        return new FinalizableDelegatedExecutorService
        //            (new ThreadPoolExecutor(1, 1,
        //                                    0L, TimeUnit.MILLISECONDS,
        //                                    new LinkedBlockingQueue<Runnable>()));
        //    }
        // 适用:一个任务一个线程执行的任务场景
        //ExecutorService threadPool = Executors.newSingleThreadExecutor();//一池1个处理线程

        //  主要特点如下:
        //  1.创建一个可缓存线程池,如果线程池长度超过处理需要,可灵活回收空闲线程,若无可回收,则创建新线程.
        //  2.newCachedThreadPool将corePoolSize设置为0,MaximumPoolSize设置为Integer.MAX_VALUE,它使用的是SynchronousQueue,也就是说来了任务就创建线程运行,如果线程空闲超过60秒,就销毁线程
        //  public static ExecutorService newCachedThreadPool() {
        //        return new ThreadPoolExecutor(0, Integer.MAX_VALUE,
        //                                      60L, TimeUnit.SECONDS,
        //                                      new SynchronousQueue<Runnable>());
        //    }
        // 适用:执行很多短期异步的小程序或者负载较轻的服务器
        ExecutorService threadPool = Executors.newCachedThreadPool();//一池N个处理线程

        try {
            // 模拟10个用户来办理业务，每个用户就是一个来自外部的请求线程
            for (int i = 1; i <= 10; i++) {
                threadPool.execute(() -> {
                    System.out.println(Thread.currentThread().getName() + "\t办理业务");
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            threadPool.shutdown();
        }
    }
}
