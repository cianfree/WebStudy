package com.bxtpw.common.paas.lock;

import com.bxtpw.common.paas.lock.exceptions.LockException;

/**
 * Created by Arvin on 2016/3/5.
 * 互斥锁
 *
 * @author 夏集球
 * @version 0.1
 * @time 2016/3/5 14:06
 * @since 0.1
 */
public interface MutexLock {

    /**
     * To get lock
     *
     * @throws LockException
     */
    void lock() throws LockException;

    /**
     * To release lock
     *
     * @throws LockException
     */
    void close() throws LockException;

}
