package com.deft.executor;

import com.deft.executor.util.ProxyUtils;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

/**
 * Created by Administrator on 2016/9/26.
 */
public class TaskService {
    private ExecutorService mExecutorService;

    public TaskService(ExecutorService executorService) {
        mExecutorService = executorService;
    }

    public static void run(Runnable runnable) {
        runnable.run();
    }

    public static <T> void run(Task<T> task, T callback) {
        task.bindCallback(callback);
        task.run();
        task.ubBind();
    }

    public Future post(Runnable runnable) {
        return mExecutorService.submit(runnable);
    }

    public <T> Future post(Task<T> task, T callback) {
        DefaultTask defaultTask = new DefaultTask(task, callback);
        return mExecutorService.submit(defaultTask);
    }

    public <T> Future post(Task<T> task, T callback, ExecutorService executorService) {
        T asyncCallback = ProxyUtils.createHandlerProxy(callback, task.getCallBackClass(), executorService);
        DefaultTask defaultTask = new DefaultTask(task, asyncCallback);
        return mExecutorService.submit(defaultTask);
    }

    private static class DefaultTask<T> implements Runnable{
        private Task mTask;
        private T mCallback;

        DefaultTask(Task<T> task, T callback){
            mTask = task;
            mCallback = callback;
        }

        @Override
        public void run() {
            mTask.bindCallback(mCallback);
            mTask.run();
            mTask.ubBind();
        }
    }
}
