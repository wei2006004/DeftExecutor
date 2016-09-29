package com.deft.example;

import com.deft.executor.Task;
import com.deft.executor.TaskService;
import com.deft.executor.annotation.Asynchronous;
import com.deft.executor.annotation.Synchronous;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * Created by wei on 16-9-15.
 */
public class Main {
    public static void main(String[] args) throws Throwable {
        TestCall testCall = new TestCall();
        TestTask testTask = new TestTask();

        log("Main thread id: " + Thread.currentThread().getId());
        log("task.run()");
        testTask.run();     // use default proxy
        logend();

        ExecutorService executorService = Executors.newSingleThreadExecutor();
        TaskService taskService = new TaskService(executorService);

        log("run(task)");
        TaskService.run(testTask);      // use default proxy
        logend();

        log("run(task, call)");
        TaskService.run(testTask, testCall);
        logend();

        log("post(task)");
        Future future = taskService.post(testTask);
        future.get();
        logend();

        log("post(task, call)");
        future = taskService.post(testTask, testCall);
        future.get();
        logend();

        ExecutorService handlerExecutor = Executors.newSingleThreadExecutor();
        log("post(task, call, handler)");
        future = taskService.post(testTask, testCall, handlerExecutor);
        future.get();
        logend();

        handlerExecutor.awaitTermination(300, TimeUnit.MILLISECONDS);
        executorService.shutdown();
        handlerExecutor.shutdown();
    }

    public static void log(String string) {
        System.out.println("####  " + string);
    }

    public static void logend() {
        System.out.println();
    }

    // the callback interface must define public
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

    static class TestCall implements ICall {
        @Override
        public String getString() {
            System.out.println("getString: hello, threadId:" + Thread.currentThread().getId());
            return "hello";
        }

        @Override
        public void setString(String value) {
            System.out.println("setString: " + value + ", threadId:" + Thread.currentThread().getId());
        }
    }

    static class TestTask extends Task<ICall> {

        @Override
        public void run() {
            System.out.println("beforeRun, threadId:" + Thread.currentThread().getId());

            String string = proxy().getString();
            proxy().setString(string);

            proxy().call();
            proxy().callSync();
            proxy().callAsync();

            System.out.println("afterRun, threadId:" + Thread.currentThread().getId());
        }
    }
}
