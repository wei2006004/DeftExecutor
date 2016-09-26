package com.deft.executor.util;

import com.deft.executor.ICall;
import org.junit.Test;

import java.lang.reflect.Method;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

/**
 * Created by Administrator on 2016/9/26.
 */
public class AnnotationUtilsTest {
    @Test
    public void isSyncAnnotationMethod() throws Exception {
        Method syncMethod = ICall.class.getMethod("callSync", String[].class);
        Method asyncMethod = ICall.class.getMethod("callAsync", String[].class);
        Method callMethod = ICall.class.getMethod("call", String[].class);
        assertTrue(AnnotationUtils.isSyncAnnotationMethod(syncMethod));
        assertFalse(AnnotationUtils.isSyncAnnotationMethod(asyncMethod));
        assertFalse(AnnotationUtils.isSyncAnnotationMethod(callMethod));
    }

    @Test
    public void isAsyncAnnotationMethod() throws Exception {
        Method syncMethod = ICall.class.getMethod("callSync", String[].class);
        Method asyncMethod = ICall.class.getMethod("callAsync", String[].class);
        Method callMethod = ICall.class.getMethod("call", String[].class);
        assertTrue(AnnotationUtils.isAsyncAnnotationMethod(asyncMethod));
        assertFalse(AnnotationUtils.isAsyncAnnotationMethod(syncMethod));
        assertFalse(AnnotationUtils.isAsyncAnnotationMethod(callMethod));
    }

}