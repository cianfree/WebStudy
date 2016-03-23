package com.bxtpw.zk.config.manager;

import org.apache.zookeeper.ZooKeeper;

import java.io.IOException;

/**
 * Created by Arvin on 2016/3/10.
 */
public class ZooKeeperFactory {

    public static final String CONNECT_STRING = "192.168.137.90:2181";

    public static final int MAX_RETRIES = 3;

    public static final int BASE_SLEEP_TIMEMS = 3000;

    public static final String NAME_SPACE = "cfg";

    /**
     * Zookeeper url
     */
    private final static String url = "192.168.137.90:2181";

    private final static String auth_type = "digest";
    private final static String auth_passwd = "password";

    public static ZooKeeper get() {
        ZooKeeper zk;
        try {
            zk = new ZooKeeper(url, 3000, null);
        } catch (IOException e) {
            return null;
        }
        zk.addAuthInfo(auth_type, auth_passwd.getBytes());
        return zk;
    }
}
