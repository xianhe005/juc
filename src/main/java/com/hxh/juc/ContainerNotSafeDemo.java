package com.hxh.juc;

import java.util.*;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * 集合类不安全问题
 */
public class ContainerNotSafeDemo {

    public static void main(String[] args) {
        //listNotSafe();
        //setNotSafe();
        mapNotSafe();

        //java.util.ConcurrentModificationException
        //并发修改异常

        /**
         * 1 故障现象
         *      java.util.ConcurrentModificationException
         *
         * 2 导致原因
         *      并发争抢修改导致
         *
         * 3 解决方案
         *      3.1 new Vector<>()
         *      3.2 Collections.synchronizedList(new ArrayList<>());
         *      3.3 new CopyOnWriteArrayList<>();
         *
         * 4 优化建议(同样的错误不犯第二次)
         *
         */

        /**
         *      写时复制 CopyOnWrite 容器即写时复制的容器 往容器添加元素的时候,
         *      不直接往当前容器object[]添加,而是先将当前容器object[]进行 copy 复制出一个新的object[] newElements
         *      然后向新容器object[] newElements 里面添加元素 添加元素后,
         *      再将原容器的引用指向新的容器 setArray(newElements);
         *      这样的好处是可以对copyOnWrite容器进行并发的读,而不需要加锁 因为当前容器不会添加任何容器.
         *      所以CopyOnWrite容器也是一种读写分离的思想,读和写不同的容器.
         *            public boolean add(E e) {
         *               final ReentrantLock lock = this.lock;
         *               lock.lock();
         *               try {
         *                   Object[] elements = getArray();
         *                   int len = elements.length;
         *                   Object[] newElements = Arrays.copyOf(elements, len + 1);
         *                   newElements[len] = e;
         *                   setArray(newElements);
         *                   return true;
         *               } finally {
         *                   lock.unlock();
         *               }
         *           }
         */
    }

    private static void listNotSafe() {
        List<String> list = new ArrayList<>();
        //List<String> list = new Vector<>();
        //List<String> list = Collections.synchronizedList(new ArrayList<>());
        //List<String> list = new CopyOnWriteArrayList<>();

        for (int i = 1; i <= 30; i++) {
            new Thread(() -> {
                list.add(UUID.randomUUID().toString().substring(0, 8));
                System.out.println(list);
            }, String.valueOf(i)).start();
        }
    }

    private static void setNotSafe() {
        Set<String> set = new HashSet<>();
        //Set<String> set = Collections.synchronizedSet(new HashSet<>());
        //Set<String> set = new CopyOnWriteArraySet<>();

        for (int i = 1; i <= 30; i++) {
            new Thread(() -> {
                set.add(UUID.randomUUID().toString().substring(0, 8));
                System.out.println(set);
            }, String.valueOf(i)).start();
        }
    }

    private static void mapNotSafe() {
        Map<String, String> map = new HashMap<>();
        //Map<String, String> map = Collections.synchronizedMap(new HashMap<>());
        //Map<String, String> map = new ConcurrentHashMap<>();
        //Map<String, String> map = new ConcurrentSkipListMap<>();

        for (int i = 1; i <= 30; i++) {
            new Thread(() -> {
                map.put(Thread.currentThread().getName(), UUID.randomUUID().toString().substring(0, 8));
                System.out.println(map);
            }, String.valueOf(i)).start();
        }
    }
}
