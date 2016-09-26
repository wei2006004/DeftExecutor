package com.deft.executor.util;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.omg.CORBA.Object;

import static org.junit.Assert.*;

/**
 * Created by Administrator on 2016/9/26.
 */
public class ClassUtilsTest {
    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void isIntegerType() throws Exception {
        Class[] classes = new Class[]{
                int.class, long.class, byte.class, short.class,
                Integer.class, Long.class, Byte.class, Short.class
        };
        Class[] others = new Class[]{
                String.class, Object.class, Boolean.class, Class.class, boolean.class
        };
        for (Class clazz : classes) {
            assertTrue(ClassUtils.isIntegerType(clazz));
        }
        for (Class clazz : others) {
            assertFalse(ClassUtils.isIntegerType(clazz));
        }
    }

    @Test
    public void isBooleanType() throws Exception {
        Class[] classes = new Class[]{
                Boolean.class, boolean.class
        };
        Class[] others = new Class[]{
                int.class, long.class, byte.class, short.class, String.class, Object.class, Class.class, Integer.class, Long.class, Byte.class, Short.class
        };
        for (Class clazz : classes) {
            assertTrue(ClassUtils.isBooleanType(clazz));
        }
        for (Class clazz : others) {
            assertFalse(ClassUtils.isBooleanType(clazz));
        }
    }

    @Test
    public void isStringType() throws Exception {
        Class[] classes = new Class[]{
                String.class
        };
        Class[] others = new Class[]{
                int.class, long.class, byte.class, short.class, Object.class, Boolean.class, Class.class, Integer.class, Long.class, Byte.class, Short.class
        };
        for (Class clazz : classes) {
            assertTrue(ClassUtils.isStringType(clazz));
        }
        for (Class clazz : others) {
            assertFalse(ClassUtils.isStringType(clazz));
        }
    }

}