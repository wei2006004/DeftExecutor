package com.deft.executor.util;

import com.deft.executor.annotation.Asynchronous;
import com.deft.executor.annotation.Synchronous;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

/**
 * Created by Administrator on 2016/9/26.
 */
public class AnnotationUtils {

    public static boolean isSyncAnnotationMethod(Method method) {
        return isAnnotationMethod(method, Synchronous.class);
    }


    public static boolean isAsyncAnnotationMethod(Method method) {
        return isAnnotationMethod(method, Asynchronous.class);
    }

    public static boolean isAnnotationMethod(Method method, Class annotationType) {
        Annotation[] annotations = method.getDeclaredAnnotations();
        for (Annotation annotation : annotations) {
            if (annotation.annotationType() == annotationType) {
                return true;
            }
        }
        return false;
    }
}
