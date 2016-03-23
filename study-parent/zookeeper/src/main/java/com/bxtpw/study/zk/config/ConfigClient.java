package com.bxtpw.study.zk.config;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

/**
 * Created by Arvin on 2016/3/5.
 * 配置项客户端，读取及监听配置变化，获取最新的数据
 */
public class ConfigClient implements Watcher {

    public static String url = "192.168.137.90:2181";

    private String root = "/Conf";
    private String UrlNode = root + "/url";
    private String userNameNode = root + "/username";
    private String passwdNode = root + "/passwd";

    private final static String auth_type = "digest";
    private final static String auth_passwd = "password";


    private String jdbcUrl;
    private String username;
    private String passwd;

    private ZooKeeper zk;

    public ZooKeeper getZk() {
        return zk;
    }

    public void setZk(ZooKeeper zk) {
        this.zk = zk;
    }

    public String getJdbcUrl() {
        return jdbcUrl;
    }

    public void setJdbcUrl(String jdbcUrl) {
        this.jdbcUrl = jdbcUrl;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPasswd() {
        return passwd;
    }

    public void setPasswd(String passwd) {
        this.passwd = passwd;
    }

    public ConfigClient() {
        try {
            ZooKeeper zk = new ZooKeeper(url, 3000, this);
            zk.addAuthInfo(auth_type, auth_passwd.getBytes());

            this.setZk(zk);

            while (zk.getState() != ZooKeeper.States.CONNECTED) {
                Thread.sleep(3000);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        // 初始化参数
        initValue();
    }

    public void initValue() {
        try {
            jdbcUrl = new String(zk.getData(UrlNode, true, null));
            username = new String(zk.getData(userNameNode, true, null));
            passwd = new String(zk.getData(passwdNode, true, null));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws Exception {
        ConfigClient client = new ConfigClient();

        ZooKeeper zk = client.getZk();

        int i = 0;

        while (true) {
            System.out.println(client.getJdbcUrl());
            System.out.println(client.getUsername());
            System.out.println(client.getPasswd());
            System.out.println("---------------------------------------------------------------------------");
            Thread.sleep(5000);
            i++;
            if (i == 10) {
                break;
            }
        }
        zk.close();
    }

    @Override
    public void process(WatchedEvent event) {
        System.out.println("触发了事件：" + event.getType() + ", Path=" + event.getPath());
        // 监听节点更新时间
        switch (event.getType()) {
            case NodeDataChanged:
            case NodeChildrenChanged:
                System.out.println("节点更新，重新加载属性！");
                initValue();
                break;
            default:
                break;
        }
    }
}
