package com.bxtpw.common.paas.lock;

import com.bxtpw.common.paas.lock.exceptions.LockException;

/**
 * 数据库锁
 *
 * @author 夏集球
 * @version 0.1
 * @time 2016/3/5 14:10
 * @since 0.1
 */
public interface DBLock {

    /**
     * 数据库事务支持级别
     */
    enum IsolateLevel {
        READUNCOMMIT, READCOMMIT, NOREPEAT, SERIALIZE
    }

    /**
     * To get read lock
     *
     * @param group It is group name, which is often organization name.
     * @param table It is table name.
     * @throws LockException
     */
    void readLock(String group, String table) throws LockException;

    /**
     * To get read lock
     *
     * @param group It is group name, which is often organization name.
     * @param table It is table name.
     * @param id    It is a primary key, which is often uuid.
     * @throws LockException
     */
    void readLock(String group, String table, String id) throws LockException;

    /**
     * To get write lock
     *
     * @param group It is group name, which is often organization name.
     * @param table It is table name.
     * @throws LockException
     */
    void writeLock(String group, String table) throws LockException;

    /**
     * To get write lock
     *
     * @param group It is group name, which is often organization name.
     * @param table It is table name.
     * @param id    It is a primary key, which is often uuid.
     * @throws LockException
     */
    void writeLock(String group, String table, String id) throws LockException;

    /**
     * To release lock
     *
     * @throws LockException
     */
    void release() throws LockException;

}
