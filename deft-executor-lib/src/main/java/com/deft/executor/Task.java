package com.deft.executor;

import com.deft.executor.util.ClassUtils;

import java.lang.reflect.*;

/**
 * Created by wei on 16-9-5.
 */
public abstract class Task<T> implements Runnable {

    public static final int DEFAULT_CALLBACK_VALUE_INT = 0;
    public static final boolean DEFAULT_CALLBACK_VALUE_BOOLEAN = false;
    public static final String DEFAULT_CALLBACK_VALUE_STRING = "";

    private T mCallback;
    private T mDefaultCallback;

    protected T proxy() {
        if (mCallback == null) {
            // use default proxy
            if (mDefaultCallback == null){
                mDefaultCallback = createDefaultProxy();
            }
            return mDefaultCallback;
        }
        return mCallback;
    }

    @SuppressWarnings("unchecked")
    private T createDefaultProxy() {
        return (T) Proxy.newProxyInstance(
                getClass().getClassLoader(),
                new Class[]{getCallBackClass()},
                new DefaultProxyInvocationHandler());
    }

    private static class DefaultProxyInvocationHandler implements InvocationHandler {

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

    synchronized public void bindCallback(T callback) {
        mCallback = callback;
    }

    synchronized public boolean isBind() {
        return mCallback != null;
    }

    synchronized public void ubBind() {
        mCallback = null;
    }

    public Class<T> getCallBackClass() {
        Class clazz = getClass();
        Type type = clazz.getGenericSuperclass();
        while (type != null && type != Object.class) {
            if (type instanceof ParameterizedType && clazz.getSuperclass() == Task.class) {
                type = ((ParameterizedType) type).getActualTypeArguments()[0];
                return (Class) type;
            }
            clazz = clazz.getSuperclass();
            type = clazz.getGenericSuperclass();
        }
        return null;
    }
}
