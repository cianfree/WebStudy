package com.bxtpw.common.paas.lock.impl;

import com.bxtpw.common.paas.lock.DBLock;
import com.bxtpw.common.paas.lock.exceptions.LockException;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;

import java.io.IOException;
import java.util.*;

/**
 * ZookeeperDBLock is threadSafed, so the class can't be extended and modified. On the other hand,
 * private and final and synchronized keywords can't be remove and modified by other keywords except to
 * you are sure that the class is threadSafed.
 * ZookeeperDBLock support 4 type of transaction isolate level.
 * ZookeeperDBLock depends on the zookeeper severs, so you must be sure that zookeeper servers
 * are work.
 * usage:
 * <p/>
 * ZookeeperDBLock l = null;
 * <p/>
 * try {
 * <p/>
 * l = new ZookeeperDBLock("127.0.0.1:2181");
 * //or new ZookeeperDBLock("127.0.0.1:2181", IsolateLevel.READCOMMIT);
 * //or new ZookeeperDBLock("127.0.0.1:2181", 3, 100, 10000, IsolateLevel.READCOMMIT);
 * <p/>
 * ...
 * <p/>
 * l.readLock("test", "t1", "*");
 * <p/>
 * ...
 * <p/>
 * l.writeLock("test", "t1", "100");
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
 * @time 2016/3/5 14:22
 * @since 0.1
 */

public class ZookeeperDBLock implements DBLock {

    final private String root = "/dblock";

    final private String serverAddress;

    final private String ownID;

    final private int retry;

    final private int waitTime;

    final private int timeout;

    final private IsolateLevel isolationLevel;

    private int count;

    private int step;

    private ZooKeeper zk;

    final private List<String> paths = new ArrayList<String>();

    /**
     * Constructor
     *
     * @param serverAddress Zookeeper servers' address-port like '127.0.0.1:2181,127.0.0.2:2181,...'.
     */
    public ZookeeperDBLock(String serverAddress) {
        this(serverAddress, 3, 100, 10000, IsolateLevel.READCOMMIT);
    }

    /**
     * Constructor
     *
     * @param serverAddress             Zookeeper servers' address-port like '127.0.0.1:2181,127.0.0.2:2181,...'.
     * @param isolationLevel。Transation isolate level
     */
    public ZookeeperDBLock(String serverAddress, IsolateLevel isolationLevel) {

        this(serverAddress, 3, 100, 10000, isolationLevel);
    }

    /**
     * Construction
     *
     * @param serverAddress  Zookeeper servers' address-port like '127.0.0.1:2181,127.0.0.2:2181,...'.
     * @param retry          Retry times.
     * @param waitTime       Wait time.
     * @param timeout        Time out.
     * @param isolationLevel Transation isolate level
     * @throws LockException
     */
    public ZookeeperDBLock(String serverAddress, int retry, int waitTime, int timeout, IsolateLevel isolationLevel) {

        this.serverAddress = serverAddress;
        this.ownID = UUID.randomUUID().toString();
        this.retry = retry;
        this.waitTime = waitTime;
        this.timeout = timeout;
        this.isolationLevel = isolationLevel;
        this.step = 1;
    }

    @Override
    public void readLock(String group, String table)
            throws LockException {
        readLock(group, table, null);
    }

    @Override
    public void readLock(String group, String table, String id)
            throws LockException {

        if (step != 1) throw new LockException(new Exception("This lock is invalid."));

        if (group == null || group.trim().equals("")) throw new NullPointerException("Group is null.");

        if (table == null || table.trim().equals("")) throw new NullPointerException("Table is null.");

        connectZooKeeper();

        while (true) {
            count++;

            try {
                execute(group, table, id, "read");
                break;
            } catch (LockException ex) {
                if (count >= retry) {

                    release();

                    throw new LockException(ex);
                } else
                    try {
                        Thread.sleep(waitTime);
                    } catch (Exception e) {
                    }
            }
        }

        count = 0;
    }

    @Override
    public void writeLock(String group, String table)
            throws LockException {
        writeLock(group, table, null);
    }

    @Override
    public void writeLock(String group, String table, String id)
            throws LockException {

        if (step != 1) throw new LockException(new Exception("This lock is invalid."));

        if (group == null || group.trim().equals("")) throw new NullPointerException("Group is null.");

        if (table == null || table.trim().equals("")) throw new NullPointerException("Table is null.");

        connectZooKeeper();

        while (true) {
            count++;

            try {
                execute(group, table, id, "write");
                break;
            } catch (LockException ex) {
                if (count >= retry) {

                    release();

                    throw new LockException(ex);
                } else
                    try {
                        Thread.sleep(waitTime);
                    } catch (Exception e) {
                    }
            }
        }

        count = 0;
    }

    private void execute(String group, String table, String id, String operation) throws LockException {

        String path = createPath(group, table);

        String actualPath = "";

        if (id == null || id.trim().equals("")) id = "*";

        try {
            String level = "";
            if (isolationLevel == IsolateLevel.READUNCOMMIT) {
                level = "RU";
            } else if (isolationLevel == IsolateLevel.READCOMMIT) {
                level = "RC";
            } else if (isolationLevel == IsolateLevel.NOREPEAT) {
                level = "NR";
            } else if (isolationLevel == IsolateLevel.SERIALIZE) {
                level = "SL";
            }

            String newNode = id + "###" + operation + "###" + ownID + "###" + level;
            actualPath = zk.create(path + "/" + newNode + "###", new byte[0], Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL);

            List<String> list1 = zk.getChildren(path, null);
            List<String> list2 = new ArrayList<String>();
            for (String node : list1) {
                if (node.split("###").length == 5) list2.add(node);
            }

            Collections.sort(list2, new Comparator<String>() {
                @Override
                public int compare(String node1, String node2) {
                    String[] node1s = node1.split("###");
                    String[] node2s = node2.split("###");

                    if (Long.parseLong(node1s[4]) > Long.parseLong(node2s[4]))
                        return 1;

                    if (Long.parseLong(node1s[4]) < Long.parseLong(node2s[4]))
                        return -1;

                    return 0;
                }
            });

            for (String oldNode : list2) {
                String[] olds = oldNode.split("###");
                String[] news = newNode.split("###");

                if (!olds[2].equals(news[2])) {
                    checkPrivilege(olds, news);
                    checkPrivilege(news, olds);
                } else {
                    if (hasPrivilege(olds, news)) break;
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

        if (!paths.contains(path)) paths.add(path);

    }

    /*
     * array[0] is id
     * array[1] is operation
     * array[2] is ownID
     * array[3] is isolate level
     */
    private void checkPrivilege(String[] in, String[] off) throws LockException {

        if (in[0].equals("*") || in[0].equals(off[0]) || off[0].equals("*"))
            if (in[3].equals("RU") && in[1].equals("write") && off[1].equals("write")) {

                throw new LockException(new Exception("Record has be locked."));

            } else if (in[3].equals("RC") && in[1].equals("write")) {

                throw new LockException(new Exception("Record has be locked."));

            } else if (in[3].equals("NR")) {

                if (in[1].equals("write"))
                    throw new LockException(new Exception("Record has be locked."));
                else if (off[1].equals("write")) throw new LockException(new Exception("Record has be locked."));

            } else if (in[3].equals("SL")) {

                throw new LockException(new Exception("Record has be locked."));
            }

    }

    /*
     * array[0] is id
     * array[1] is operation
     * array[2] is ownID
     * array[3] is isolate level
     */
    private boolean hasPrivilege(String[] in, String[] off) {

        if ((in[0].equals("*") || in[0].equals(off[0]))) {
            if (in[1].equals("write"))
                return true;
            else if (off[1].equals("read"))
                return true;
        }

        return false;
    }

    private String createPath(String group, String table) {
        try {
            if (zk.exists(root, false) == null) {
                zk.create(root, new byte[0], Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
            }

            if (zk.exists(root + "/" + group, false) == null) {
                zk.create(root + "/" + group, new byte[0], Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
            }

            if (zk.exists(root + "/" + group + "/" + table, false) == null) {
                zk.create(root + "/" + group + "/" + table, new byte[0], Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
            }
        } catch (KeeperException e) {
        } catch (InterruptedException e) {
        }

        return root + "/" + group + "/" + table;
    }

    private void connectZooKeeper() throws LockException {

        try {
            if (zk == null) zk = new ZooKeeper(serverAddress, timeout, null);
        } catch (IOException e) {
            throw new LockException(e);
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
    synchronized public void release() throws LockException {

        if (step != 1)
            throw new LockException(new Exception("This lock is invalid."));
        else
            step = 2;

        closeZookeeper();

        connectZooKeeper();

        String group = "";
        for (String path : paths)
            try {
                if (!group.equals("/" + path.split("/")[1] + "/" + path.split("/")[2])) {
                    if (!group.equals("")) zk.delete(group, -1);
                    group = "/" + path.split("/")[1] + "/" + path.split("/")[2];
                }

                Stat stat = zk.exists(path, null);

                if (stat != null && stat.getNumChildren() == 0) zk.delete(path, -1);
            } catch (InterruptedException e) {
            } catch (KeeperException e) {
            }

        if (!group.equals(""))
            try {
                zk.delete(group, -1);
            } catch (InterruptedException e) {
            } catch (KeeperException e) {
            }

        closeZookeeper();
    }

}
