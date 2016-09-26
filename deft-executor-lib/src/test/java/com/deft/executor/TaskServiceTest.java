package com.deft.executor;

import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by Administrator on 2016/9/26.
 */
public class TaskServiceTest {
    private TestCall mCall;
    private TaskService mTaskService;
    private TestTask mTask;

    @Before
    public void testBefore(){
        mTask = new TestTask();
        mCall = new TestCall();
        mTaskService = new TaskService(Executors.newSingleThreadExecutor());
    }

    @Test
    public void testRun() throws Exception {
        initValue();

        mTask.ivalue = 10;
        mTask.svalue = "hh";
        TaskService.run(mTask);
        assertValue(false, 0, "");

        initValue();

        setValues(true, 3, "bbb");
        TaskService.run(mTask, mCall);
        assertValue(true, 3, "bbb");
    }

    @Test
    public void testPost() throws Exception {
        initValue();

        mTask.ivalue = 10;
        mTask.svalue = "hh";
        Future future = mTaskService.post(mTask);
        future.get();
        assertValue(false, 0, "");

        initValue();

        setValues(true, 3, "bbb");
        future = mTaskService.post(mTask, mCall);
        future.get();
        assertValue(true, 3, "bbb");
    }

    private void initValue() {
        setValues(false, 0 ,"");
        setTaskValues(false, 0 ,"");
    }

    private void setTaskValues(boolean b, int i, String s) {
        mTask.bvalue = b;
        mTask.ivalue = i;
        mTask.svalue = s;
    }

    private void assertValue(boolean b, int i, String s) {
        assertThat(mTask.bvalue).isEqualTo(b);
        assertThat(mTask.ivalue).isEqualTo(i);
        assertThat(mTask.svalue).isEqualTo(s);
    }

    private void setValues(boolean b, int i, String bbb) {
        mCall.bvalue = b;
        mCall.ivalue = i;
        mCall.svalue = bbb;
    }

}