package com.deft.executor;

import com.deft.executor.util.AnnotationUtils;
import com.deft.executor.util.ClassUtils;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
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
    }

    public Future post(Runnable runnable) {
        return mExecutorService.submit(runnable);
    }

    public <T> Future post(Task<T> task, T callback) {
        task.bindCallback(callback);
        return mExecutorService.submit(task);
    }

    public <T> Future post(Task<T> task, T callback, AsyncHandler handler) {
        Class callbackClass = task.getCallBackClass();
        T asyncCallback = createAsyncCallback(callback, callbackClass, handler);
        task.bindCallback(asyncCallback);
        return mExecutorService.submit(task);
    }

    @SuppressWarnings("unchecked")
    private <T> T createAsyncCallback(T callback, Class callbackClass, AsyncHandler handler) {
        return (T) Proxy.newProxyInstance(
                getClass().getClassLoader(),
                new Class[]{callbackClass},
                new AsyncInvocationHandler(callback, handler));
    }

    private static class AsyncInvocationHandler<T> implements InvocationHandler {
        private T mCallback;
        private AsyncHandler mHandler;

        AsyncInvocationHandler(T callback, AsyncHandler handler) {
            mCallback = callback;
            mHandler = handler;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            // if the method is sync method, invoke directly
            if (AnnotationUtils.isSyncAnnotationMethod(method)) {
                return method.invoke(mCallback, args);
            }

            Class returnType = method.getReturnType();
            if (!ClassUtils.isVoidType(returnType)) {
                throw new IllegalStateException("Asynchronous method can not return value. callback:"
                        + mCallback.getClass()
                        + ", method:" + method.getName()
                        + ", returnType:" + returnType);
            }
            mHandler.submit(() -> {
                try {
                    method.invoke(mCallback, args);
                } catch (Throwable throwable) {
                    mHandler.handleThrowable(throwable);
                }
            });
            return null;
        }


    }
}
