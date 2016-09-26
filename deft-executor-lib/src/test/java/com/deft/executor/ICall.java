package com.deft.executor;

/**
 * Created by wei on 16-9-15.
 */
interface ICall {

    default boolean getBoolean(){
        return false;
    }

    default int getInt(){
        return 0;
    }

    default String getString(){
        return "";
    }

    default void setBoolean(boolean value){}

    default void setInt(int value){}

    default void setString(String value){}

    default void call(String[] args){}
}
