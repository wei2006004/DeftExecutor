package com.deft.executor;

/**
 * Created by Administrator on 2016/9/26.
 */
public interface AsyncHandler {
    void submit(Runnable runnable);

    default void handleThrowable(Throwable throwable) {
    }
}
