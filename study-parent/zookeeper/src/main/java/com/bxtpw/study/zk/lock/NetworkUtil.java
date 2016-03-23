package com.bxtpw.study.zk.lock;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;

/**
 * Created by Arvin on 2016/3/5.
 */
public class NetworkUtil {
    static private final char COLON = ':';

    /**
     * 获取当前机器ip地址
     * 据说多网卡的时候会有问题.
     */
    public static String getNetworkAddress() {
        Enumeration<NetworkInterface> netInterfaces;
        try {
            netInterfaces = NetworkInterface.getNetworkInterfaces();
            InetAddress ip;
            while (netInterfaces.hasMoreElements()) {
                NetworkInterface ni = netInterfaces
                        .nextElement();
                Enumeration<InetAddress> addresses = ni.getInetAddresses();
                while (addresses.hasMoreElements()) {
                    ip = addresses.nextElement();
                    if (!ip.isLoopbackAddress()
                            && ip.getHostAddress().indexOf(COLON) == -1) {
                        return ip.getHostAddress();
                    }
                }
            }
            return "";
        } catch (Exception e) {
            return "";
        }
    }

}
