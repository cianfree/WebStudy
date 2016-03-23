package com.bxtpw.common.paas.lock.exceptions;

/**
 * Created by Arvin on 2016/3/5.
 */
public class LockException extends Exception {

    private Exception ex;

    public LockException(Exception ex) {
        this.ex = ex;
    }

    public LockException(String message) {
        super(message);
    }

    public void printStackTrace() {
        ex.printStackTrace();
    }

}
