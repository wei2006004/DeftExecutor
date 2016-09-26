package com.deft.executor.annotation;

import java.lang.annotation.*;

/**
 * Created by Administrator on 2016/9/26.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Asynchronous {
}
