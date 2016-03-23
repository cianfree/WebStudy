package com.bxtpw.zk.config.manager;

import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;

/**
 * Created by Arvin on 2016/3/10.
 */
public class ZKConfig implements Config {

    @Override
    public byte[] getConfig(String path) throws Exception {
        ZooKeeper zk = ZooKeeperFactory.get();
        if (!exists(zk, path)) {
            throw new RuntimeException("Path" + path + "does not exists.");
        }
        return zk.getData(path, false, null);
    }

    private boolean exists(ZooKeeper zk, String path) throws Exception {
        Stat stat = zk.exists(path, null);
        return stat != null;
    }
}
