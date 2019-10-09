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
}
