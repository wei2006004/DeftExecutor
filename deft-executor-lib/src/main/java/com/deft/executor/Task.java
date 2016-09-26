package com.deft.executor;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * Created by wei on 16-9-5.
 */
public abstract class Task<T> implements Runnable {

    private T mCallback;

    protected T proxy(){
        return mCallback;
    }

    synchronized void bindCallback(T callback){
        mCallback = callback;
    }

    synchronized boolean isBind(){
        return mCallback != null;
    }

    synchronized void ubBind(){
        mCallback = null;
    }

    Class getCallBackClass(){
        Class clazz = getClass();
        Type type = clazz.getGenericSuperclass();
        while (type != null && type != Object.class){
            if (type instanceof ParameterizedType && clazz.getSuperclass() == Task.class) {
                type = ((ParameterizedType) type).getActualTypeArguments()[0];
                return (Class)type;
            }
            clazz = clazz.getSuperclass();
            type = clazz.getGenericSuperclass();
        }
        return null;
    }
}
