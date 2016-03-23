package com.bxtpw.common.zk.provider.impl;

import com.bxtpw.common.zk.provider.ZKProvider;
import org.apache.zookeeper.ZooKeeper;

/**
 * 具有缓存功能的ZK Provider
 *
 * @author 夏集球
 * @version 0.1
 * @time 2016/3/5 16:04
 * @since 0.1
 */
public class CachingZKProvider implements ZKProvider {

    /**
     * 缓存的Zookeeper对象
     */
    private ZooKeeper cachingZookeeper;

    @Override
    public ZooKeeper getZookeeper() {
        return null;
    }
}
