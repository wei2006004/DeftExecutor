package com.deft.executor;

import org.junit.Assert;

import java.util.concurrent.ExecutorService;

/**
 * Created by Administrator on 2016/9/27.
 */
public class SimpleHandler implements IHandler {
    private ExecutorService executorService;

    public SimpleHandler(ExecutorService executorService){
        this.executorService = executorService;
    }

    @Override
    public void submit(Runnable runnable) {
        executorService.submit(runnable);
    }

    @Override
    public void handleThrowable(Throwable throwable) {
        throwable.printStackTrace();
        Assert.fail();
    }
}
