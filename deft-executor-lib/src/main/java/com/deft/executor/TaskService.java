package com.deft.executor;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

/**
 * Created by Administrator on 2016/9/26.
 */
public class TaskService {
    private ExecutorService mExecutorService;

    public TaskService(ExecutorService executorService){
        mExecutorService = executorService;
    }

    public static void run(Runnable runnable){
        runnable.run();
    }

    public static <T> void run(Task<T> task, T callback){
        task.bindCallback(callback);
        task.run();
    }

    public Future post(Runnable runnable){
        return mExecutorService.submit(runnable);
    }

    public <T> Future post(Task<T> task, T callback){
        task.bindCallback(callback);
        return mExecutorService.submit(task);
    }
}
