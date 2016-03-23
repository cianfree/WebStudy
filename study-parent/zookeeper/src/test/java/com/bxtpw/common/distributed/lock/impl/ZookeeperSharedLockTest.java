package com.bxtpw.common.distributed.lock.impl;

import com.bxtpw.common.distributed.lock.ProviderConfig;
import com.bxtpw.common.distributed.lock.SharedLock;
import com.bxtpw.common.paas.lock.exceptions.LockException;
import org.apache.zookeeper.ZooKeeper;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * 共享锁测试
 *
 * @author 夏集球
 * @version 0.1
 * @time 2016/3/7 14:07
 * @since 0.1
 */
public class ZookeeperSharedLockTest {
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
     * 共享锁测试，同时获取读锁， 允许两次该测试即可
     */
    @Test
    public void testSharedLock_ReadLock_AtSameTime() throws Exception {
        // 第一次获取读锁
        getReadLock(2000);
        // 暂停500毫秒后继续获取
        Thread.sleep(500);

        // 第二次获取读锁
        getReadLock(2000);

        // 3秒后断开
        Thread.sleep(3000);
    }

    /**
     * 获取读锁
     *
     * @param businessExecuteTime 业务执行时间
     */
    public void getReadLock(final int businessExecuteTime) throws Exception {
        new Thread(new Runnable() {
            @Override
            public void run() {
                SharedLock lock = null;
                try {
                    // 构造锁的提供配置
                    ProviderConfig config = ProviderConfig.Builder.builder()//
                            .resId("user1")//
                            .zk(zk).root("shared").build();

                    lock = new ZookeeperSharedLock(config);
                    System.out.println("Thread(" + Thread.currentThread().getId() + ")尝试获得读锁");
                    // 获取读锁
                    lock.read();
                    System.out.println("Thread(" + Thread.currentThread().getId() + ")已经获得读锁，准备执行业务");
                    Thread.sleep(businessExecuteTime);
                    System.out.println("Thread(" + Thread.currentThread().getId() + ")已经执行了业务");
                } catch (LockException e) {
                    System.out.println("Thread(" + Thread.currentThread().getId() + ")未能获取读锁");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        if (null != lock) {
                            lock.release();
                        }
                    } catch (LockException ignored) {
                    }
                }
            }
        }).start();
    }

    /**
     * 测试有客户端获得读锁之后，另外一个客户端获取写锁，写锁无法获取
     */
    @Test
    public void testSharedLock_ReadLock_ThenGetWriteLock() throws Exception {
        // 先获取读锁
        getReadLock(2000);

        Thread.sleep(500);

        // 获取写锁
        getWriteLock(2000);

        Thread.sleep(3000);
    }

    /**
     * 获取写锁
     *
     * @param businessExecuteTime 业务执行时间
     */
    public void getWriteLock(final int businessExecuteTime) throws Exception {
        new Thread(new Runnable() {
            @Override
            public void run() {
                SharedLock lock = null;
                try {
                    // 构造锁的提供配置
                    ProviderConfig config = ProviderConfig.Builder.builder()//
                            .resId("user1")//
                            .zk(zk).root("shared").build();

                    lock = new ZookeeperSharedLock(config);
                    System.out.println("Thread(" + Thread.currentThread().getId() + ")尝试获得写锁");
                    // 获取读锁
                    lock.write();
                    System.out.println("Thread(" + Thread.currentThread().getId() + ")已经获得写锁，准备执行业务");
                    Thread.sleep(businessExecuteTime);
                    System.out.println("Thread(" + Thread.currentThread().getId() + ")已经执行了业务");
                } catch (LockException e) {
                    System.out.println("Thread(" + Thread.currentThread().getId() + ")未能获取写锁");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        if (null != lock) {
                            lock.release();
                        }
                    } catch (LockException ignored) {
                    }
                }
            }
        }).start();
    }

    /**
     * 当有客户端获取了写锁，那么其他客户端将无法获得写锁
     */
    @Test
    public void testSharedLock_WriteLock_ThenGetReadLock() throws Exception {
        // 获取写锁
        getWriteLock(1000);
        Thread.sleep(500);
        // 获取读锁
        getReadLock(1000);

        Thread.sleep(3000);
    }

}