package com.bxtpw.common.paas.lock.impl;

import com.bxtpw.common.paas.lock.SharedLock;
import com.bxtpw.common.paas.lock.exceptions.LockException;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;

import java.io.IOException;
import java.util.*;

/**
 * SharedLockZooKeeper is thread safe, so the class can't be extended and modified. On the other hand,
 * private and final and synchronized keywords can't be remove and modified by other keywords except to
 * you are sure that the class is thread safe.
 * ZookeeperSharedLock provide read-write lock function and support lock's reenter function.
 * ZookeeperSharedLock depends on the zookeeper severs, so you must be sure that zookeeper servers
 * are work.
 * usage:
 * <p/>
 * SharedLock l = null;
 * <p/>
 * try {
 * <p/>
 * l = new SharedLock("127.0.0.1:2181", "key1");
 * //or new SharedLock("127.0.0.1:2181", "key1", 3, 100, 10000);
 * <p/>
 * ...
 * <p/>
 * l.write();
 * //or l.read();
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
 * @time 2016/3/5 14:03
 * @since 0.1
 */

public class ZookeeperSharedLock implements SharedLock {
    final private String root = "/shared";

    final private String serverAddress;

    final private String ownID;

    final private int retry;

    final private int waitTime;

    final private int timeout;

    private boolean isReentry;

    private int count;

    private int step;

    private ZooKeeper zk;

    final private String zookeeperID;

    final static private ThreadLocal<Set<String>> session = new ThreadLocal<>();

    /**
     * Construction
     *
     * @param serverAddress Zookeeper servers' address-port like '127.0.0.1:2181,127.0.0.2:2181,...'.
     * @param ownID         Source Key or lock name.
     * @throws LockException
     */
    public ZookeeperSharedLock(String serverAddress, String ownID) throws LockException {
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
    public ZookeeperSharedLock(String serverAddress, String ownID, int retry, int waitTime, int timeout) throws LockException {

        this.serverAddress = serverAddress;
        this.ownID = ownID;
        this.retry = retry;
        this.waitTime = waitTime;
        this.timeout = timeout;
        this.step = 1;
        this.count = 0;
        this.zookeeperID = UUID.randomUUID().toString();
    }

    private void init(String type) throws LockException {

        if (type.equals("read")) {
            if (session.get() == null || (!session.get().contains(ownID + "_write") && !session.get().contains(ownID + "_read"))) {

                connectZooKeeper();

                Set<String> set = session.get() == null ? new HashSet<String>() : session.get();
                set.add(ownID + "_read");
                session.set(set);

                isReentry = false;
            } else {
                isReentry = true;
            }
        } else if (type.equals("write")) {
            if (session.get() == null || !session.get().contains(ownID + "_write")) {

                if (session.get() != null && session.get().contains(ownID + "_read")) {
                    throw new LockException(new Exception("The lock has been locked."));
                }

                connectZooKeeper();

                Set<String> set = session.get() == null ? new HashSet<String>() : session.get();
                set.add(ownID + "_write");
                session.set(set);

                isReentry = false;
            } else {
                isReentry = true;
            }
        }

    }

    @Override
    synchronized public void write() throws LockException {

        if (step != 1)
            throw new LockException(new Exception("This lock is invalid."));
        else
            step = 2;

        init("write");

        while (true && !isReentry) {
            count++;

            try {
                executeWrite();
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

    private void executeWrite() throws LockException {

        String path = createPath();

        String actualPath = "";

        try {

            actualPath = zk.create(path + "/" + ownID + "###" + zookeeperID + "###write###", new byte[0], ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL);

            List<String> list1 = zk.getChildren(path, null);
            List<String> list2 = new ArrayList<String>();
            for (String node : list1) {
                if (node.split("###").length == 4) list2.add(node);
            }

            Collections.sort(list2, new Comparator<String>() {
                @Override
                public int compare(String node1, String node2) {
                    String[] node1s = node1.split("###");
                    String[] node2s = node2.split("###");

                    if (Long.parseLong(node1s[3]) > Long.parseLong(node2s[3]))
                        return 1;

                    if (Long.parseLong(node1s[3]) < Long.parseLong(node2s[3]))
                        return -1;

                    return 0;
                }
            });

            for (String node : list2)
                if (node.indexOf(ownID) > -1)
                    if (node.indexOf(zookeeperID) == -1)
                        throw new LockException(new Exception("The lock has been locked."));
                    else
                        break;
        } catch (KeeperException e) {
            throw new LockException(e);
        } catch (InterruptedException e) {
            throw new LockException(e);
        } catch (LockException e) {

            if (actualPath != null)
                try {
                    zk.delete(actualPath, 0);
                } catch (InterruptedException e1) {
                } catch (KeeperException e1) {
                }

            throw e;
        }
    }

    @Override
    public void read() throws LockException {

        if (step != 1)
            throw new LockException(new Exception("This lock is invalid."));
        else
            step = 2;

        init("read");

        while (true && !isReentry) {
            count++;

            try {
                executeRead();
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

    private void executeRead() throws LockException {

        String path = createPath();

        String actualPath = "";

        try {
            actualPath = zk.create(path + "/" + ownID + "###" + zookeeperID + "###read###", new byte[0], ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL);

            List<String> list1 = zk.getChildren(path, null);
            List<String> list2 = new ArrayList<String>();
            for (String node : list1) {
                if (node.split("###").length == 4) list2.add(node);
            }

            Collections.sort(list2, new Comparator<String>() {
                @Override
                public int compare(String node1, String node2) {
                    String[] node1s = node1.split("###");
                    String[] node2s = node2.split("###");

                    if (Long.parseLong(node1s[3]) > Long.parseLong(node2s[3]))
                        return 1;

                    if (Long.parseLong(node1s[3]) < Long.parseLong(node2s[3]))
                        return -1;

                    return 0;
                }
            });

            for (String node : list2)
                if (node.indexOf(ownID) > -1) {
                    if (node.indexOf(zookeeperID) == -1) {
                        if (node.indexOf("###write###") > -1)
                            throw new LockException(new Exception("The lock has been write locked."));
                    } else {
                        break;
                    }
                }
        } catch (KeeperException e) {
            throw new LockException(e);
        } catch (InterruptedException e) {
            throw new LockException(e);
        } catch (LockException e) {

            if (actualPath != null)
                try {
                    zk.delete(actualPath, -1);
                } catch (InterruptedException e1) {
                } catch (KeeperException e1) {
                }

            throw e;
        }

    }

    private String createPath() {
        try {
            if (zk.exists(root, false) == null) {
                zk.create(root, new byte[0], ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
            }
        } catch (KeeperException e) {
        } catch (InterruptedException e) {
        }

        return root;
    }

    private void connectZooKeeper() throws LockException {

        try {
            if (zk == null) zk = new ZooKeeper(serverAddress, timeout, null);
        } catch (IOException e) {
            throw new LockException(e);
        }
    }

    private void closeLocalThread() {

        if (!isReentry && session.get() != null) {
            session.get().remove(ownID + "_write");
            session.get().remove(ownID + "_read");
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
            throw new LockException(new Exception("The lock is invalid"));
        else
            step = 3;

        closeLocalThread();
        closeZookeeper();
    }

}
