package com.deft.executor;

/**
 * Created by Administrator on 2016/9/26.
 */
class TestCall implements ICall{
    int ivalue;
    String svalue;
    boolean bvalue;

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
}
