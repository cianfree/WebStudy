package com.bxtpw.study.context;

import org.apache.zookeeper.*;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.util.List;

/**
 * Created by Arvin on 2016/3/12.
 */
public class ConfigCacheListener implements ServletContextListener {

    public static String url = "192.168.137.90:2181";

    private String root = "/cfg";

    private ZooKeeper zk;

    private ServletContext context;

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        context = sce.getServletContext();
        // 1. 从数据库读取配置
        String cp = "http://localhost/";
        String scp = "http://localhost/";
        String apikey = "arvin";

        // 缓存
        context.setAttribute("cp", cp);
        context.setAttribute("scp", scp);
        context.setAttribute("apikey", apikey);

        // 存入zk
        try {
            zk = new ZooKeeper(url, 3000, new Watcher() {
                @Override
                public void process(WatchedEvent event) {
                    System.out.println("触发了事件：" + event.getType() + ", Path=" + event.getPath());
                    if (event.getPath().startsWith(root)) {
                        System.out.println("重新初始化配置");
                        try {
                            List<String> children = zk.getChildren(root, true);
                            for (String key : children) {
                                String val = new String(zk.getData(root + "/" + key, true, null));
                                System.out.println("KeyPath: " + key + ", KeyValue: " + val);
                                context.setAttribute(key, val);
                            }
                        } catch (KeeperException | InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
            // 存入zk
            if (zk.exists(root, true) == null) {
                zk.create(root, "root".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
            }
            if (zk.exists(root + "/cp", true) == null) {
                zk.create(root + "/cp", cp.getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
            } else {
                zk.setData(root + "/cp", cp.getBytes(), -1);
            }
            if (zk.exists(root + "/scp", true) == null) {
                zk.create(root + "/scp", scp.getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
            } else {
                zk.setData(root + "/scp", scp.getBytes(), -1);
            }
            if (zk.exists(root + "/apikey", true) == null) {
                zk.create(root + "/apikey", apikey.getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
            } else {
                zk.setData(root + "/apikey", apikey.getBytes(), -1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        ConfigContext.init(context, zk);
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        try {
            zk.close();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
