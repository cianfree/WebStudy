package com.bxtpw.common.springmvc.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.bxtpw.common.domain.User;

/**
 * @author 夏集球
 * @time 2015年6月15日 下午4:04:38
 * @version 0.1
 * @since 0.1
 */
public class DataProvider {

    private DataProvider() {
    }

    /**
     * 获取一个没有User外键的用户
     * 
     * @author 夏集球
     * @time 2015年6月15日 下午4:05:41
     * @version 0.1
     * @since 0.1
     * @return
     */
    public static User getNoneRefUser() {
        return new User().setId(1).setAccount("clean user account").setMaps(createMaps()).setCreateTime(new Date());
    }
    
    /**
     * 获取有孩子的用户
     * @author 夏集球
     * @time 2015年6月15日 下午4:10:49
     * @version 0.1
     * @since 0.1
     * @return
     */
    public static User getHasChildrenUser() {
        User father = getNoneRefUser();
        // 创建孩子列表
        father.setChildren(createUsers(2, 3, father));
        return father;
    }
    
    /**
     * 获取孩子列表，没有父亲
     * @author 夏集球
     * @time 2015年6月15日 下午4:12:23
     * @version 0.1
     * @since 0.1
     * @return
     */
    public static List<User> getListUsersForNoneFather() {
        return createUsers(1, 2, null);
    }
    
    /**
     * 获取孩子列表，这些孩子列表都有父亲的引用
     * @author 夏集球
     * @time 2015年6月15日 下午4:13:12
     * @version 0.1
     * @since 0.1
     * @return
     */
    public static List<User> getListUsersWithFather() {
        User father = getNoneRefUser();
        // 创建孩子列表
        father.setChildren(createUsers(2, 3, father));
        return father.getChildren();
    }
    
    /**
     * 构造用户集合
     * @author 夏集球
     * @time 2015年6月15日 下午4:03:25
     * @version 0.1
     * @since 0.1
     * @param startId
     * @param num
     * @param father
     * @return
     */
    private static List<User> createUsers(int startId, int num, User father) {
        List<User> users = new ArrayList<User>();
        int endIndex = startId + num;
        for (int i= startId; i<endIndex; ++i) {
            users.add(new User().setId(i)//
                    .setAccount("account_" + i)//
                    .setFather(father)//
                    .setProperty(i + 0.6F)//
                    .setCreateTime(new Date())//
                    );
        }
        return users;
    }

    /**
     * 创建Map
     * 
     * @author 夏集球
     * @time 2015年6月15日 下午4:09:22
     * @version 0.1
     * @since 0.1
     * @return
     */
    private static Map<String, Object> createMaps() {
        Map<String, Object> maps = new HashMap<String, Object>();
        maps.put("id", 2);
        maps.put("name", "Map name");
        return maps;
    }
}
