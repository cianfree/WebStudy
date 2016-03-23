package com.bxtpw.common.domain;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

/**
 * @author 夏集球
 * @time 2015年6月15日 下午3:56:19
 * @version 0.1
 * @since 0.1
 */
@JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class, property = "@id")
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer id;

    private String account;

    private float property;

    private Date createTime;

    private User father;

    private Set<User> friends;

    private Map<String, Object> maps;

    private List<User> children;

    public Integer getId() {
        return id;
    }

    public User setId(Integer id) {
        this.id = id;
        return this;
    }

    public String getAccount() {
        return account;
    }

    public User setAccount(String account) {
        this.account = account;
        return this;
    }

    public float getProperty() {
        return property;
    }

    public User setProperty(float property) {
        this.property = property;
        return this;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public User setCreateTime(Date createTime) {
        this.createTime = createTime;
        return this;
    }

    public User getFather() {
        return father;
    }

    public User setFather(User father) {
        this.father = father;
        return this;
    }

    public Set<User> getFriends() {
        return friends;
    }

    public User setFriends(Set<User> friends) {
        this.friends = friends;
        return this;
    }

    public Map<String, Object> getMaps() {
        return maps;
    }

    public User setMaps(Map<String, Object> maps) {
        this.maps = maps;
        return this;
    }

    public List<User> getChildren() {
        return children;
    }

    public User setChildren(List<User> children) {
        this.children = children;
        return this;
    }

    @JsonIgnore
    public String getSimpleString() {
        return "User[id=" + this.id + ", account=" + this.account + "]";
    }
}
