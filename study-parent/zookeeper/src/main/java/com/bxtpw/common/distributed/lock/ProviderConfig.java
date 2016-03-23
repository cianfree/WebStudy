package com.bxtpw.common.distributed.lock;

import com.bxtpw.common.distributed.lock.exceptions.ResNotFoundException;
import com.bxtpw.common.distributed.lock.exceptions.RootNotProvidedException;
import com.bxtpw.common.distributed.lock.exceptions.ZookeeperConnectException;
import com.bxtpw.common.distributed.lock.utils.ZKPathUtil;
import org.apache.zookeeper.ZooKeeper;

import java.io.Serializable;

/**
 * 锁的提供配置信息，即获取锁的时候应该尝试多少次，超时多久，等等
 *
 * @author 夏集球
 * @version 0.1
 * @time 2016/3/5 16:32
 * @since 0.1
 */
public class ProviderConfig implements Serializable {

    /**
     * 构造Config
     */
    public static class Builder {
        private ZooKeeper zk;
        private String root = "/mutex";
        private String resId;
        private int retryTimes = 0; // 默认不能进行重试
        private int retryTimeout = 100;

        public Builder zk(ZooKeeper zookeeper) {
            this.zk = zookeeper;
            return this;
        }

        public Builder root(String root) {
            this.root = ZKPathUtil.normalize(root);
            return this;
        }

        public Builder resId(String resId) {
            if (null == resId || "".equals(resId.trim())) throw new ResNotFoundException();
            this.resId = resId.trim();
            return this;
        }

        public Builder retryTimes(int retryTime) {
            this.retryTimes = retryTime < 0 ? 0 : retryTime;
            return this;
        }

        public Builder retryTimeout(int retryTimeout) {
            this.retryTimeout = retryTimeout < 0 ? 1 : retryTimeout;
            return this;
        }

        public ProviderConfig build() {
            // 验证
            if (null == resId || "".equals(resId.trim())) throw new ResNotFoundException();
            if (null == zk) throw new ZookeeperConnectException();
            if (null == root) throw new RootNotProvidedException();
            return new ProviderConfig(zk, root, resId, retryTimes, retryTimeout);
        }

        /**
         * 不对res进行检测
         */
        public ProviderConfig buildMain() {
            if (null == zk) throw new ZookeeperConnectException();
            if (null == root) throw new RootNotProvidedException();
            return new ProviderConfig(zk, root, resId, retryTimes, retryTimeout);
        }

        /**
         * 获取Builder
         */
        public static Builder builder() {
            return new Builder();
        }
    }

    /**
     * Zookeeper对象
     */
    private final ZooKeeper zk;

    /**
     * 资源根路径, 默认是 "/mutex"
     */
    private final String root;

    /**
     * 锁的资源
     */
    private final String resId;

    /**
     * 尝试获取锁的次数，默认不能进行重试
     */
    private final int retryTimes;

    /**
     * 每次进行重试获取锁的时间，即经过该时间如果没有获得锁就继续获取, 默认100毫秒后进行重试
     */
    private final int retryTimeout;

    /**
     * Construction
     *
     * @param zookeeper    Zookeeper对象
     * @param root         锁的基路径
     * @param resId        资源对象
     * @param retryTimes   重试次数
     * @param retryTimeout 每次重试的超时时间
     */
    private ProviderConfig(ZooKeeper zookeeper, String root, String resId, int retryTimes, int retryTimeout) {
        this.zk = zookeeper;
        this.root = root;
        this.resId = resId;
        this.retryTimes = retryTimes;
        this.retryTimeout = retryTimeout;
    }

    public ZooKeeper getZk() {
        return zk;
    }

    public String getRoot() {
        return root;
    }

    public String getResId() {
        return ZKPathUtil.contact(root, resId);
    }

    public String getSubResId() {
        return resId;
    }

    public int getRetryTimes() {
        return retryTimes;
    }

    public int getRetryTimeout() {
        return retryTimeout;
    }
}
