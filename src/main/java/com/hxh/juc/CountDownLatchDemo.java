package com.hxh.juc;

import java.util.concurrent.CountDownLatch;

// 枚举的使用，相当于多张表的数据，如下的ONE:表示一张名为ONE的表,其中只有一条数据(id=1,name=齐)
enum CountryEnum {
    ONE(1, "齐"), TWO(2, "楚"), THREE(3, "燕"), FOUR(4, "赵"), FIVE(5, "魏"), SEX(6, "韩");

    private final int id;
    private final String name;

    CountryEnum(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public static CountryEnum find(int index) {
        for (CountryEnum value : CountryEnum.values()) {
            if (value.id == index) {
                return value;
            }
        }
        return null;
    }
}

/**
 * CountDownLatch主要有两个方法,当一个或多个线程调用await方法时,调用线程会被阻塞.
 * 其他线程调用countDown方法计数器减1(调用countDown方法时线程不会阻塞),当计数器的值变为0,
 * 因调用await方法被阻塞的线程会被唤醒,继续执行
 */
public class CountDownLatchDemo {

    //类比：火箭倒计时 同学都上完晚自习全部离开教室班长锁门 秦灭六国一统天下
    public static void main(String[] args) throws InterruptedException {
        //closeDoor();
        sixCountry();
    }

    //秦灭六国 一统华夏
    private static void sixCountry() throws InterruptedException {
        CountDownLatch countDownLatch = new CountDownLatch(6);

        for (int i = 1; i <= 6; i++) {
            new Thread(() -> {
                System.out.println(Thread.currentThread().getName() + "\t国，被灭");
                countDownLatch.countDown();
            }, CountryEnum.find(i).getName()).start();
        }
        countDownLatch.await();
        System.out.println(Thread.currentThread().getName() + "\t **********秦帝国，一统华夏");
    }

    //同学都上完晚自习全部离开教室班长锁门
    private static void closeDoor() throws InterruptedException {
        CountDownLatch countDownLatch = new CountDownLatch(6);

        for (int i = 1; i <= 6; i++) {
            new Thread(() -> {
                System.out.println(Thread.currentThread().getName() + "\t上完成自习，离开教室");
                countDownLatch.countDown();
            }, String.valueOf(i)).start();
        }
        countDownLatch.await();
        System.out.println(Thread.currentThread().getName() + "\t **********班长最后关门走人");
    }
}
