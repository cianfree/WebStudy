package com.bxtpw.study.context;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;

import javax.servlet.ServletContext;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Arvin on 2016/3/12.
 */
public class ConfigContext {

    private static ServletContext context;

    private static Map<String, String> configs = new HashMap<>();

    private static ZooKeeper zk;

    public static void init(ServletContext context, ZooKeeper zk) {
        ConfigContext.context = context;
        ConfigContext.zk = zk;
    }

    public static void update(String key, String value) {
        configs.put(key, value);
        String path = "/cfg/" + key;
        // 写入zk
        try {
            if (zk.exists(path, true) == null) {
                zk.create(path, value.getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
            } else {
                zk.setData(path, value.getBytes(), -1);
            }
            // 缓存
            context.setAttribute(key, value);
        } catch (KeeperException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
