package com.deft.executor;

import com.deft.executor.annotation.Asynchronous;
import com.deft.executor.annotation.Synchronous;

/**
 * Created by wei on 16-9-15.
 */
public interface ICall {

    @Synchronous
    default boolean getBoolean(){
        return false;
    }

    @Synchronous
    default int getInt(){
        return 0;
    }

    @Synchronous
    default String getString(){
        return "";
    }

    @Synchronous
    default void setBoolean(boolean value){}

    @Synchronous
    default void setInt(int value){}

    @Synchronous
    default void setString(String value){}

    @Synchronous
    default void callSync(String[] args){}

    @Asynchronous
    default void callAsync(String[] args){}

    default void call(String[] args){}
}
