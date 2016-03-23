package com.bxtpw.common.distributed.lock.impl;

import com.bxtpw.common.distributed.lock.ProviderConfig;
import com.bxtpw.common.distributed.lock.MutexLock;
import com.bxtpw.common.paas.lock.exceptions.LockException;
import org.apache.zookeeper.ZooKeeper;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Zookeeper 互斥锁测试
 */
public class ZookeeperMutexLockTest {

    /**
     * Zookeeper url
     */
    private final String url = "192.168.137.90:2181";

    private final static String auth_type = "digest";
    private final static String auth_passwd = "password";

    private ZooKeeper zk;

    @Before
    public void ready() throws Exception {
        zk = new ZooKeeper(url, 3000, null);
        zk.addAuthInfo(auth_type, auth_passwd.getBytes());
        System.out.println("连接ZK成功！");
    }

    @After
    public void after() {
        if (null != zk) {
            try {
                zk.close();
            } catch (InterruptedException e) {
                System.out.println("关闭ZK错误");
            }
        }
    }

    /**
     * 互斥锁测试
     */
    @Test
    public void testMutexLock() throws Exception {

        getMutexLock(1500);

        Thread.sleep(200);

        getMutexLock(1500);

        Thread.sleep(5000);

    }

    public void getMutexLock(final int businessTime) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                MutexLock lock = null;
                try {
                    // 构造锁的提供配置
                    ProviderConfig config = ProviderConfig.Builder.builder()//
                            .resId("user1")//
                            .zk(zk).root("mutex").build();

                    lock = new ZookeeperMutexLock(config);
                    // 执行锁定操作
                    lock.lock();
                    // 处理业务
                    System.out.println("Thread(" + Thread.currentThread().getId() + ")获得锁成功，处理相关业务");
                    try {
                        Thread.sleep(businessTime);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.println("Thread(" + Thread.currentThread().getId() + ")业务处理完成");
                } catch (LockException e) {
                    System.out.println("Thread(" + Thread.currentThread().getId() + ")没有获得锁！");
                } finally {
                    if (null != lock) {
                        try {
                            // 释放锁
                            lock.release();
                        } catch (LockException ignored) {
                        }
                    }
                }
            }
        }).start();
    }

}