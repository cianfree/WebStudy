package com.bxtpw.zk.config.manager;

/**
 * Created by Arvin on 2016/3/10.
 */
public interface Config {

    byte[] getConfig(String path) throws Exception;
}
