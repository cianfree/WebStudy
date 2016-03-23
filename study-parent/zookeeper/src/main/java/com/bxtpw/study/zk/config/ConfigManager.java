package com.bxtpw.study.zk.config;

import org.apache.zookeeper.*;

/**
 * Created by Arvin on 2016/3/5.
 * 项目管理类
 */
public class ConfigManager {

    public static String url = "192.168.137.90:2181";

    private final static String root = "/Conf";
    private final static String UrlNode = root + "/url";
    private final static String userNameNode = root + "/username";
    private final static String passwdNode = root + "/passwd";

    private final static String auth_type = "digest";
    private final static String auth_passwd = "password";

    public static void main(String[] args) throws Exception {

        ZooKeeper zk = new ZooKeeper(url, 3000, new Watcher() {
            @Override
            public void process(WatchedEvent event) {
                System.out.println("触发了事件： " + event.getType());
            }
        });

        while (ZooKeeper.States.CONNECTED != zk.getState()) {
            Thread.sleep(3000);
        }

        zk.addAuthInfo(auth_type, auth_passwd.getBytes());

        // 删除节点
//        zk.delete(UrlNode, -1);
//        zk.delete(userNameNode, -1);
//        zk.delete(passwdNode, -1);
//        zk.delete(root, -1);

        if (zk.exists(root, true) == null) {
            zk.create(root, "root".getBytes(), ZooDefs.Ids.CREATOR_ALL_ACL, CreateMode.PERSISTENT);
        }

        if (zk.exists(UrlNode, true) == null) {
            zk.create(UrlNode, "192.168.137.90:2181".getBytes(), ZooDefs.Ids.CREATOR_ALL_ACL, CreateMode.PERSISTENT);
        }

        if (zk.exists(userNameNode, true) == null) {
            zk.create(userNameNode, "admin".getBytes(), ZooDefs.Ids.CREATOR_ALL_ACL, CreateMode.PERSISTENT);
        }

        if (zk.exists(passwdNode, true) == null) {
            zk.create(passwdNode, "admin123".getBytes(), ZooDefs.Ids.CREATOR_ALL_ACL, CreateMode.PERSISTENT);
        }

        // 修改配置项
        zk.setData(passwdNode, "123456".getBytes(), -1);


        zk.close();
    }
}
