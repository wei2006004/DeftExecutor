package com.deft.executor;

import com.deft.executor.util.ProxyUtils;

import java.lang.reflect.*;

/**
 * Created by wei on 16-9-5.
 */
public abstract class Task<T> implements Runnable {

    private T mCallback;
    private T mDefaultCallback;

    protected T proxy() {
        if (mCallback == null) {
            // use default proxy
            if (mDefaultCallback == null){
                mDefaultCallback = ProxyUtils.createDefaultProxy(getCallBackClass());
            }
            return mDefaultCallback;
        }
        return mCallback;
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
