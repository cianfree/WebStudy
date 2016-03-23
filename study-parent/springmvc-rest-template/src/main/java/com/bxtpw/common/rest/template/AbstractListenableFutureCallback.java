package com.bxtpw.common.rest.template;

import org.springframework.http.ResponseEntity;
import org.springframework.util.concurrent.ListenableFutureCallback;

/**
 * @author 夏集球
 * @time 2015年6月16日 上午9:37:06
 * @version 0.1
 * @since 0.1
 */
public abstract class AbstractListenableFutureCallback<T> implements ListenableFutureCallback<ResponseEntity<T>> {

    @Override
    public void onSuccess(ResponseEntity<T> result) {
        handleSuccess(result.getBody());
    }

    /**
     * 抽象函数用于继承
     * 
     * @author 夏集球
     * @time 2015年6月16日 上午9:41:47
     * @version 0.1
     * @since 0.1
     * @param object
     */
    public void handleSuccess(T object) {
    };

    @Override
    public void onFailure(Throwable ex) {
        // DO NOTHING
        handleFailure(ex);
    }

    /**
     * 抽象函数用于继承
     * 
     * @author 夏集球
     * @time 2015年6月16日 上午9:42:46
     * @version 0.1
     * @since 0.1
     * @param ex
     */
    public void handleFailure(Throwable ex) {
    };

}
