package com.bxtpw.common.distributed.lock.impl;

import com.bxtpw.common.distributed.lock.MutexLock;
import com.bxtpw.common.distributed.lock.ProviderConfig;
import com.bxtpw.common.paas.lock.exceptions.LockException;
import org.apache.zookeeper.ZooKeeper;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * 组合互斥锁实现
 *
 * @author 夏集球
 * @version 0.1
 * @time 2016/3/7 17:56
 * @since 0.1
 */
public class ZookeeperGroupMutexLockTest {

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

    @Test
    public void testGroupMutexLock() throws Exception {
        // 获取互斥锁
        getGroupLock(1500, "user1", "user2", "user3");
        // 等待500毫秒
        Thread.sleep(500);
        // 重新获取锁， 此时是无法获取锁的
        getGroupLock(1500, "user1", "user4", "user5");

        Thread.sleep(4000);
    }

    public void getGroupLock(final int businessExecuteTime, final String... resIds) throws Exception {
        new Thread(new Runnable() {
            @Override
            public void run() {
                MutexLock lock = null;
                try {
                    ProviderConfig config = ProviderConfig.Builder.builder()//
                            .zk(zk)//
                            .root("/group/mutex").buildMain();
                    lock = new ZookeeperGroupMutexLock(config, resIds);
                    // 准备获取锁
                    System.out.println("Thread(" + Thread.currentThread().getId() + ")尝试获得互斥锁");
                    // 获取互斥锁
                    lock.lock();
                    System.out.println("Thread(" + Thread.currentThread().getId() + ")已经获得互斥锁，准备执行业务");
                    try {
                        Thread.sleep(businessExecuteTime);
                    } catch (InterruptedException ignored) {
                    }
                    System.out.println("Thread(" + Thread.currentThread().getId() + ")已经执行了业务");
                } catch (LockException e) {
                    System.out.println("Thread(" + Thread.currentThread().getId() + ")没有获得互斥锁");
                } finally {
                    if (null != lock) {
                        try {
                            lock.release();
                        } catch (LockException ignored) {
                        }
                    }
                }
            }
        }).start();
    }
}