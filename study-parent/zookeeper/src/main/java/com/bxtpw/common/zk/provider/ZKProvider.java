package com.bxtpw.common.zk.provider;

import org.apache.zookeeper.ZooKeeper;

/**
 * Zookeeper 连接提供者
 *
 * @author 夏集球
 * @version 0.1
 * @time 2016/3/5 16:01
 * @since 0.1
 */
public interface ZKProvider {

    /**
     * 获取Zookeeper连接对象
     *
     * @return 返回有效的Zookeeper对象
     */
    ZooKeeper getZookeeper();
}
