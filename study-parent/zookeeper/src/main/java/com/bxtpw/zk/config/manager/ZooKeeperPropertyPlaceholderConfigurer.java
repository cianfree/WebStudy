package com.bxtpw.zk.config.manager;

import com.alibaba.fastjson.JSON;
import org.apache.commons.lang.StringUtils;
import org.apache.zookeeper.ZooKeeper;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Properties;

/**
 * Created by Arvin on 2016/3/10.
 */
public class ZooKeeperPropertyPlaceholderConfigurer extends PropertyPlaceholderConfigurer {

    public static final String PATH = "zoo.paths";

    @Override
    protected void processProperties(ConfigurableListableBeanFactory beanFactoryToProcess, Properties props)
            throws BeansException {
        super.processProperties(beanFactoryToProcess, props);
        try {
            fillCustomProperties(props);
            System.out.println(props);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void fillCustomProperties(Properties props) throws Exception {
        System.out.println(JSON.toJSONString(props));
        ZooKeeper zk = ZooKeeperFactory.get();
        String root = "/cfg";
        List<String> children = zk.getChildren(root, true);
        for (String key : children) {
            String val = new String(zk.getData(root + "/" + key, true, null));
            System.out.println("KeyPath: " + key + ", KeyValue: " + val);
        }
        //byte[] data = getData(props);
        //fillProperties(props, data);
    }

    private void fillProperties(Properties props, byte[] data) throws UnsupportedEncodingException {
        String cfg = new String(data, "UTF-8");
        if (StringUtils.isNotBlank(cfg)) {
            // 完整的应该还需要处理：多条配置、value中包含 =、忽略#号开头
            String[] cfgItem = StringUtils.split(cfg, "=");
            props.put(cfgItem[0], cfgItem[1]);
        }
    }

    private byte[] getData(Properties props) throws Exception {
        String path = props.getProperty(PATH);
        Config config = new ZKConfig();
        return config.getConfig(path);
    }

}
