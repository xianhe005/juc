package com.hxh.juc;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * 1 CAS是什么？ ===> compareAndSet->compareAndSwap
 * 比较并交换
 */
public class CASDemo {

    /**
     * Unsafe类+CAS核心思想源码(自旋),见图<br/>
     * <img src="../../../../../img/cas.png" width="600" height="300" alt="CAS核心思想源码" /><br/>
     * <br/>
     * Unsafe类compareAndSwapInt底层汇编本地方法，如下图：
     * <br/><img src="../../../../../img/unsafe.png" width="600" height="180" alt="Unsafe类compareAndSwapInt底层汇编" /><br/>
     * <br/>
     * 原理分析:
     * 假设线程A和线程B两个线程同时执行getAndAddInt操作(分别在不同的CPU上):<br/><br/>
     * <p>
     * 1.AtomicInteger里面的value原始值为3,即主内存中AtomicInteger的value为3,
     * 根据JMM模型,线程A和线程B各自持有一份值为3的value的副本分别到各自的工作内存.<br/><br/>
     * <p>
     * 2.线程A通过getIntVolatile(var1,var2) 拿到value值3,这是线程A被挂起.<br/><br/>
     * <p>
     * 3.线程B也通过getIntVolatile(var1,var2) 拿到value值3,
     * 此时刚好线程B没有被挂起并执行compareAndSwapInt方法比较内存中的值也是3 成功修改内存的值为4 线程B打完收工 一切OK.<br/><br/>
     * <p>
     * 4.这是线程A恢复,执行compareAndSwapInt方法比较,
     * 发现自己手里的数值和内存中的数字4不一致,说明该值已经被其他线程抢先一步修改了,那A线程修改失败,只能重新来一遍了.<br/><br/>
     * <p>
     * 5.线程A重新获取value值,因为变量value是volatile修饰,所以其他线程对他的修改,
     * 线程A总是能够看到,线程A继续执行compareAndSwapInt方法进行比较替换,直到成功.<br/><br/>
     *
     *
     * CAS缺点：<br/>
     * 1 循环时间长开销很大
     * 2 只能保证一个共享变量的原子性
     * 3 引出ABA问题
     */
    public static void main(String[] args) {
        AtomicInteger atomicInteger = new AtomicInteger(5);

        System.out.println(atomicInteger.compareAndSet(5, 2019) + "\t current data:" + atomicInteger.get());
        System.out.println(atomicInteger.compareAndSet(5, 1024) + "\t current data:" + atomicInteger.get());

        atomicInteger.getAndIncrement();
    }
}
