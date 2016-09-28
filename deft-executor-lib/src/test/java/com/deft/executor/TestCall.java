package com.deft.executor;

/**
 * Created by Administrator on 2016/9/26.
 */
public class TestCall implements ICall{
    public int ivalue;
    public String svalue;
    public boolean bvalue;

    @Override
    public int getInt() {
        return ivalue;
    }

    @Override
    public String getString() {
        return svalue;
    }

    @Override
    public boolean getBoolean() {
        return bvalue;
    }

    public long callThreadId;
    public long syncCallThreadId;
    public long asyncCallThreadId;

    @Override
    public void call(String[] args) {
        callThreadId = Thread.currentThread().getId();
    }

    @Override
    public void callSync(String[] args) {
        syncCallThreadId = Thread.currentThread().getId();
    }

    @Override
    public void callAsync(String[] args) {
        asyncCallThreadId = Thread.currentThread().getId();
    }
}
