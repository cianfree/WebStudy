package com.bxtpw.common.springmvc.controller;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

/**
 * 结果用户
 * 
 * @author 夏集球
 * @time 2015年6月16日 上午8:11:37
 * @version 0.1
 * @since 0.1
 */
@JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class, property = "@id")
public class ResultUser implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer id;

    private String account;

    private Date createTime;

    private ResultUser father;

    private List<ResultUser> children;

    public Integer getId() {
        return id;
    }

    public ResultUser setId(Integer id) {
        this.id = id;
        return this;
    }

    public String getAccount() {
        return account;
    }

    public ResultUser setAccount(String account) {
        this.account = account;
        return this;
    }

    public Date getCreateTime() {
        return createTime;
    }

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    public ResultUser setCreateTime(Date createTime) {
        this.createTime = createTime;
        return this;
    }

    public ResultUser getFather() {
        return father;
    }

    public ResultUser setFather(ResultUser father) {
        this.father = father;
        return this;
    }

    public List<ResultUser> getChildren() {
        return children;
    }

    public ResultUser setChildren(List<ResultUser> children) {
        this.children = children;
        return this;
    }

    @JsonIgnore
    public String getSimpleString() {
        return "ResultUser[id=" + this.id + ", account=" + this.account + "]";
    }
}
