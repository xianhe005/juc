package com.hxh.juc;

// idea 添加 javap 方法
// ctrl+alt+s打开设置界面,找到Tool-> External Tools 点击 +来增加一个新的外部工具。

/**
 * volatile不保证原子性原理,见图：<br/>
 * <img src="../../../../../img/volatile.png" width="700" height="266" alt="volatile不保证原子性" />
 */
public class T {
    volatile int n = 0;

    public void add() {
        n++;
    }

    public static void main(String[] args) {
        new T();
    }
}
