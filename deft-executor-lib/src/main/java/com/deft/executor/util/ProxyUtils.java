package com.deft.executor.util;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.concurrent.ExecutorService;

/**
 * Created by Administrator on 2016/9/28.
 */
public class ProxyUtils {

    public static final int DEFAULT_CALLBACK_VALUE_INT = 0;
    public static final boolean DEFAULT_CALLBACK_VALUE_BOOLEAN = false;
    public static final String DEFAULT_CALLBACK_VALUE_STRING = "";

    @SuppressWarnings("unchecked")
    public static <T> T createDefaultProxy(Class<T> clazz) {
        if (!clazz.isInterface()) {
            throw new IllegalArgumentException("ProxyUtils.createDefaultProxy: argument clazz must be interface. Error class:" + clazz);
        }
        return (T) Proxy.newProxyInstance(
                clazz.getClassLoader(),
                new Class[]{clazz},
                new DefaultProxyInvocationHandler());
    }

    @SuppressWarnings("unchecked")
    public static <T> T createHandlerProxy(T callback, Class<T> clazz, ExecutorService executorService) {
        if (!clazz.isInterface()) {
            throw new IllegalArgumentException("ProxyUtils.createHandlerProxy: argument clazz must be interface. Error class:" + clazz);
        }
        return (T) Proxy.newProxyInstance(
                callback.getClass().getClassLoader(),
                new Class[]{clazz},
                new HandlerProxyInvocationHandler(callback, executorService));
    }

    public static class DefaultProxyInvocationHandler implements InvocationHandler {

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            Class returnType = method.getReturnType();
            if (ClassUtils.isIntegerType(returnType)) {
                return DEFAULT_CALLBACK_VALUE_INT;
            } else if (ClassUtils.isStringType(returnType)) {
                return DEFAULT_CALLBACK_VALUE_STRING;
            } else if (ClassUtils.isBooleanType(returnType)) {
                return DEFAULT_CALLBACK_VALUE_BOOLEAN;
            } else if (ClassUtils.isVoidType(returnType)) {
                return null;
            } else {
                throw new IllegalStateException("Callback method return type must be int/boolean/string when use default callback. callback:"
                        + method.getDeclaringClass()
                        + " method:" + method.getName()
                        + " retrunType:" + returnType);
            }
        }
    }

    public static class HandlerProxyInvocationHandler<T> implements InvocationHandler {
        private T mCallback;
        private ExecutorService mService;

        public HandlerProxyInvocationHandler(T callback, ExecutorService executorService) {
            mCallback = callback;
            mService = executorService;
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
            mService.submit(() -> {
                try {
                    method.invoke(mCallback, args);
                } catch (Throwable throwable) {
                    throw new RuntimeException(throwable.getMessage());
                }
            });
            return null;
        }
    }
}
