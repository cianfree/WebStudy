package com.bxtpw.study;

import com.alibaba.fastjson.JSON;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Arvin on 2016/3/12.
 */
public class ConfigManagerTest {

    private ZooKeeper zk;

    private String root = "/cfg";

    private Map<String, String> configs = new HashMap<>();

    private Watcher watcher = new Watcher() {
        @Override
        public void process(WatchedEvent event) {
            System.out.println("触发了事件：" + event.getType() + ", Path=" + event.getPath());
            if (event.getPath().startsWith(root)) {
                System.out.println("重新初始化配置");
                try {
                    loadConfigs();
                } catch (KeeperException | InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    };

    @Before
    public void ready() throws IOException {
        zk = new ZooKeeper("192.168.137.90:2181", 3000, watcher);
    }

    @Test
    public void testRun() throws KeeperException, InterruptedException {
        loadConfigs();
        System.out.println(JSON.toJSONString(configs));
        while(true) ;
    }

    private void loadConfigs() throws KeeperException, InterruptedException {
        List<String> children = zk.getChildren(root, true);
        for (String key : children) {
            String path = root + "/" + key;
            String val = new String(zk.getData(path, true, null));
            System.out.println("KeyPath: " + path + ", KeyValue: " + val);
            configs.put(key, val);
        }
        System.out.println(JSON.toJSONString(configs));
    }

    @Test
    public void testChangeConfig() throws KeeperException, InterruptedException {
        zk.setData("/cfg/apikey", "hello".getBytes(), -1);
    }
}
