package com.bxtpw.common.paas.lock;

import com.bxtpw.common.paas.lock.exceptions.LockException;

/**
 * Created by Arvin on 2016/3/5.
 */
public interface SharedLock {

    /**
     * To get write lock
     *
     * @throws LockException
     */
    void write() throws LockException;

    /**
     * To get read lock
     *
     * @throws LockException
     */
    void read() throws LockException;

    /**
     * To release lock
     *
     * @throws LockException
     */
    void close() throws LockException;

}
