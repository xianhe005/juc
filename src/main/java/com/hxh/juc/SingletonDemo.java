package com.hxh.juc;

/**
 * DCL(双端检锁) 机制不一定线程安全,原因是有指令重排的存在,加入volatile可以禁止指令重排
 *   原因在于某一个线程在执行到第一次检测,读取到的instance不为null时,instance的引用对象可能没有完成初始化.
 * instance=new SingletonDem(); 可以分为以下步骤(伪代码)
 *
 * memory=allocate();//1.分配对象内存空间
 * instance(memory);//2.初始化对象
 * instance=memory;//3.设置instance的指向刚分配的内存地址,此时instance!=null
 *
 * 步骤2和步骤3不存在数据依赖关系.而且无论重排前还是重排后程序执行的结果在单线程中并没有改变,因此这种重排优化是允许的.
 * memory=allocate();//1.分配对象内存空间
 * instance=memory;//3.设置instance的指向刚分配的内存地址,此时instance!=null 但对象还没有初始化完.
 * instance(memory);//2.初始化对象
 * 但是指令重排只会保证串行语义的执行一致性(单线程) 并不会关心多线程间的语义一致性
 * 所以当一条线程访问instance不为null时,由于instance实例未必完成初始化,也就造成了线程安全问题.
 */
public class SingletonDemo {

    private static volatile SingletonDemo instance = null;

    private SingletonDemo() {
        System.out.println(Thread.currentThread().getName() + "\t 我是构造方法");
    }

    // DCL (Double Check Lock双端检锁机制)
    public static SingletonDemo getInstance() {
        if (instance == null) {
            synchronized (SingletonDemo.class) {
                if (instance == null) {
                    instance = new SingletonDemo();
                }
            }
        }
        return instance;
    }

    public static void main(String[] args) {
        // 单线程(main线程的操作...)
        //System.out.println(SingletonDemo.getInstance() == SingletonDemo.getInstance());
        //System.out.println(SingletonDemo.getInstance() == SingletonDemo.getInstance());
        //System.out.println(SingletonDemo.getInstance() == SingletonDemo.getInstance());

        // 并发多线程后，情况发生变化,可能出现多个instance
        for (int i = 1; i <= 10; i++) {
            new Thread(() -> {
                SingletonDemo.getInstance();
            }, String.valueOf(i)).start();
        }


    }
}
