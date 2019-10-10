package com.hxh.juc;

import java.util.concurrent.TimeUnit;

public class ThreadUtil {

    /**
     * 线程暂停
     *
     * @param seconds 暂停秒数
     */
    public static void sleep(long seconds) {
        try {
            TimeUnit.SECONDS.sleep(seconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    /**
     * 线程暂停
     *
     * @param mills 暂停毫秒数
     */
    public static void sleep2(long mills) {
        try {
            TimeUnit.MILLISECONDS.sleep(mills);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
