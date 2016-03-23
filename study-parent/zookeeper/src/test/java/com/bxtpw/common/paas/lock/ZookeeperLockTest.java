package com.bxtpw.common.paas.lock;

import com.bxtpw.common.paas.lock.exceptions.LockException;
import com.bxtpw.common.paas.lock.impl.ZookeeperMutexLock;
import com.bxtpw.common.paas.lock.impl.ZookeeperSharedLock;
import org.junit.Test;

/**
 * Created by Arvin on 2016/3/5.
 */
public class ZookeeperLockTest {

    @Test
    public void testMutexLock() {
        doMutexBusiness();
    }

    /**
     * 互斥业务执行
     */
    protected void doMutexBusiness() {
        MutexLock lock = null;
        try {
            // 获取互斥锁, 尝试5次，1秒间隔尝试，超时时间10秒
            lock = new ZookeeperMutexLock("192.168.137.90:2181", "key1", 5, 1000, 10000);
            System.out.println("准备获取锁");
            long begTime = System.currentTimeMillis();
            lock.lock();
            long endTime = System.currentTimeMillis();
            System.out.println(endTime - begTime);

            System.out.println("Thread(" + Thread.currentThread().getId() + ") 获得了锁！");

            // Thread.sleep 模拟业务执行
            Thread.sleep(3000);
            System.out.println("Thread(" + Thread.currentThread().getId() + ") 正在处理业务！");
            System.out.println("Thread(" + Thread.currentThread().getId() + ") 完成业务处理！");

            // 独占资源操作代码
        } catch (LockException e) {
            System.out.println("Thread(" + Thread.currentThread().getId() + ") 未能获得锁！");
            // 10秒后继续获取锁
            try {
                System.out.println("尝试6秒后继续获取锁");
                for (int i = 0; i < 7; ++i) {
                    System.out.println(i + 1);
                    Thread.sleep(1000);
                }
                try {
                    lock = new ZookeeperMutexLock("192.168.137.90:2181", "key1");
                    lock.lock();
                    System.out.println("6秒后获得了锁");
                } catch (LockException e1) {
                    System.out.println("Thread(" + Thread.currentThread().getId() + ") 第二次还是没有获得锁");
                    e1.printStackTrace();
                }
            } catch (InterruptedException e1) {
                e1.printStackTrace();
            }
            // 异常处理代码
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            try {
                // 记得释放锁
                if (lock != null) {
                    lock.close();
                    System.out.println("Thread(" + Thread.currentThread().getId() + ") 释放锁");
                }
            } catch (LockException e) {
            }
        }
    }

    /**
     * 测试共享锁
     */
    @Test
    public void testSharedLock() {
        SharedLock lock = null;
        try {
            lock = new ZookeeperSharedLock("192.168.137.90:2181", "key1");

            lock.read();//lock.write();

            //独占资源操作代码
        } catch (LockException e) {
            //异常处理代码
        } finally {
            try {
                if (lock != null) lock.close();
            } catch (LockException e) {
            }
        }

    }

}