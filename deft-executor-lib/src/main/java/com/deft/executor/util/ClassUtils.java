package com.deft.executor.util;

/**
 * Created by Administrator on 2016/9/26.
 */
public class ClassUtils {

    public static boolean isIntegerType(Class clazz) {
        Class[] classes = new Class[]{
                int.class, long.class, byte.class, short.class,
                Integer.class, Long.class, Byte.class, Short.class
        };
        for (Class check : classes) {
            if (clazz == check) {
                return true;
            }
        }
        return false;
    }

    public static boolean isBooleanType(Class clazz) {
        Class[] classes = new Class[]{
                Boolean.class, boolean.class
        };
        for (Class check : classes) {
            if (clazz == check) {
                return true;
            }
        }
        return false;
    }

    public static boolean isStringType(Class clazz) {
        Class[] classes = new Class[]{
                String.class
        };
        for (Class check : classes) {
            if (clazz == check) {
                return true;
            }
        }
        return false;
    }

    public static boolean isVoidType(Class clazz) {
        return clazz == void.class;
    }
}
