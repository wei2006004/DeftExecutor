package com.deft.example;

import com.deft.executor.annotation.Asynchronous;
import com.deft.executor.annotation.Synchronous;

/**
 * Created by Administrator on 2016/9/27.
 */
public interface ICall {
    @Synchronous
    String getString();

    @Synchronous
    void setString(String value);

    @Synchronous
    default void callSync() {
        System.out.println("callSync, threadId:" + Thread.currentThread().getId());
    }

    @Asynchronous
    default void callAsync() {
        System.out.println("callAsync, threadId:" + Thread.currentThread().getId());
    }

    default void call() {
        System.out.println("call, threadId:" + Thread.currentThread().getId());
    }
}
