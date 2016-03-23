package com.bxtpw.common.paas.lock.impl;

import com.bxtpw.common.paas.lock.MutexLock;
import com.bxtpw.common.paas.lock.exceptions.LockException;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

/**
 * ZookeeperMutexLock is thread safe, so the class can't be extended and modified. On the other hand,
 * private and final and synchronized keywords can't be remove and modified by other keywords except to
 * you are sure that the class is thread safe.
 * ZookeeperMutexLock provide mutual lock function and support lock's reenter function.
 * ZookeeperMutexLock depends on the zookeeper severs, so you must be sure that zookeeper servers
 * are work.
 * usage:
 * <p/>
 * MutexLock l = null;
 * <p/>
 * try {
 * <p/>
 * l = new ZookeeperMutexLock("127.0.0.1:2181", "key1");
 * //or l = new ZookeeperMutexLock("127.0.0.1:2181", "key1", 3, 100, 10000);
 * <p/>
 * ...
 * <p/>
 * l.lock();
 * <p/>
 * ...
 * <p/>
 * } catch (LockException e) {
 * <p/>
 * ...
 * <p/>
 * } finally {
 * try {
 * if(l!=null) l.release();
 * } catch (LockException e) {}
 * }
 *
 * @author 夏集球
 * @version 0.1
 * @time 2016/3/5 14:07
 * @since 0.1
 */
public class ZookeeperMutexLock implements MutexLock {

    final private String root = "/mutex";

    final private String serverAddress;

    final private String ownID;

    final private int retry;

    final private int waitTime;

    final private int timeout;

    private boolean isReentry;

    private int count;

    private int step;

    private ZooKeeper zk;

    final private static ThreadLocal<Set<String>> session = new ThreadLocal<Set<String>>();

    /**
     * Construction
     *
     * @param serverAddress Zookeeper servers' address-port like '127.0.0.1:2181,127.0.0.2:2181,...'.
     * @param ownID         Source Key or lock name.
     * @throws LockException
     */
    public ZookeeperMutexLock(String serverAddress, String ownID) throws LockException {

        this(serverAddress, ownID, 0, 100, 10000);
    }

    /**
     * Construction
     *
     * @param serverAddress Zookeeper servers' address-port like '127.0.0.1:2181,127.0.0.2:2181,...'.
     * @param ownID         Source Key or lock name.
     * @param retry         Retry times.
     * @param waitTime      Wait time.
     * @param timeout       Time out.
     * @throws LockException
     */
    public ZookeeperMutexLock(String serverAddress, String ownID, int retry, int waitTime, int timeout) throws LockException {

        this.serverAddress = serverAddress;
        this.ownID = ownID;
        this.retry = retry;
        this.waitTime = waitTime;
        this.timeout = timeout;
        this.step = 1;
        this.count = 0;
    }

    @Override
    synchronized public void lock() throws LockException {

        if (step != 1)
            throw new LockException(new Exception("This lock is invalid."));
        else
            step = 2;

        init();
        while (!isReentry) {
            count++;

            try {
                execute();
                break;
            } catch (LockException ex) {
                if (count >= retry) {

                    closeLocalThread();
                    closeZookeeper();

                    throw new LockException(ex);
                } else
                    try {
                        Thread.sleep(waitTime);
                    } catch (Exception e) {
                    }
            }
        }
    }

    private void init() throws LockException {

        if (session.get() == null || !session.get().contains(ownID)) {

            connectZooKeeper();

            Set<String> set = session.get() == null ? new HashSet<String>() : session.get();
            set.add(ownID);
            session.set(set);

            isReentry = false;
        } else {
            isReentry = true;
        }
    }

    private void execute() throws LockException {

        try {
            zk.create(createPath(), new byte[0], ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
        } catch (KeeperException | InterruptedException e) {
            throw new LockException(e);
        }
    }

    private String createPath() {
        try {
            if (zk.exists(root, false) == null) {
                zk.create(root, new byte[0], ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
            }
        } catch (KeeperException | InterruptedException e) {
        }

        return root + "/" + ownID;
    }

    private void connectZooKeeper() throws LockException {

        try {
            long begTime = System.currentTimeMillis();
            if (zk == null) zk = new ZooKeeper(serverAddress, timeout, null);
            long endTime = System.currentTimeMillis();
            System.out.println("连接Zookeeper使用的时间： " + (endTime - begTime));
        } catch (IOException e) {
            throw new LockException(e);
        }
    }

    private void closeLocalThread() {

        if (!isReentry && session.get() != null) {
            if (session.get().contains(ownID)) session.get().remove(ownID);
        }
    }

    private void closeZookeeper() {

        if (zk != null)
            try {
                zk.close();
            } catch (InterruptedException e) {
            } finally {
                zk = null;
            }
    }

    @Override
    synchronized public void close() throws LockException {

        if (step != 2)
            throw new LockException(new Exception("This lock is invalid."));
        else
            step = 3;

        closeLocalThread();
        closeZookeeper();
    }

}
