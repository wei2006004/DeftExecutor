package com.deft.executor;

import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by Administrator on 2016/9/26.
 */
public class TaskServiceTest {
    private TestCall mCall;
    private TaskService mTaskService;
    private TestTask mTask;

    private long mCurrentThreadId;

    @Before
    public void testBefore(){
        mTask = new TestTask();
        mCall = new TestCall();
        mTaskService = new TaskService(Executors.newSingleThreadExecutor());
        mCurrentThreadId = Thread.currentThread().getId();
    }

    @Test
    public void testRun() throws Exception {
        resetValues();

        mTask.ivalue = 10;
        mTask.svalue = "hh";
        TaskService.run(mTask);
        assertValue(false, 0, "");

        resetValues();

        setValues(true, 3, "bbb");
        TaskService.run(mTask, mCall);
        assertValue(true, 3, "bbb");

        assertThat(mCurrentThreadId).isEqualTo(mCall.syncCallThreadId);
        assertThat(mCurrentThreadId).isEqualTo(mCall.asyncCallThreadId);
        assertThat(mCurrentThreadId).isEqualTo(mCall.callThreadId);

        assertCallbackThreadId(false);
    }

    @Test
    public void testPost() throws Exception {
        resetValues();

        mTask.ivalue = 10;
        mTask.svalue = "hh";
        Future future = mTaskService.post(mTask);
        future.get();
        assertValue(false, 0, "");

        resetValues();

        setValues(true, 3, "bbb");
        future = mTaskService.post(mTask, mCall);
        future.get();
        assertValue(true, 3, "bbb");

        assertThat(mCurrentThreadId).isNotEqualTo(mCall.syncCallThreadId);
        assertThat(mCurrentThreadId).isNotEqualTo(mCall.asyncCallThreadId);
        assertThat(mCurrentThreadId).isNotEqualTo(mCall.callThreadId);

        assertCallbackThreadId(false);
    }

    private void assertCallbackThreadId(boolean isAsyncPostCallback) {
        if (isAsyncPostCallback){
            assertThat(mCall.syncCallThreadId).isNotEqualTo(mCall.asyncCallThreadId);
            assertThat(mCall.syncCallThreadId).isNotEqualTo(mCall.callThreadId);
        }else {
            assertThat(mCall.syncCallThreadId).isEqualTo(mCall.asyncCallThreadId);
        }
        assertThat(mCall.asyncCallThreadId).isEqualTo(mCall.callThreadId);
    }

    @Test
    public void testPostHandler() throws Exception {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        resetValues();

        setValues(true, 1, "bbb");
        Future future = mTaskService.post(mTask, mCall, executorService);
        future.get();
        assertValue(true, 1, "bbb");

        executorService.awaitTermination(500, TimeUnit.MILLISECONDS);

        assertThat(mCurrentThreadId).isNotEqualTo(mCall.syncCallThreadId);
        assertThat(mCurrentThreadId).isNotEqualTo(mCall.asyncCallThreadId);
        assertThat(mCurrentThreadId).isNotEqualTo(mCall.callThreadId);

        assertCallbackThreadId(true);
        executorService.shutdown();
    }

    private void resetValues() {
        setValues(false, 0 ,"");
        setTaskValues(false, 0 ,"");

        resetThreadId();
    }

    private void resetThreadId() {
        mCurrentThreadId = Thread.currentThread().getId();
        mCall.asyncCallThreadId = 0;
        mCall.syncCallThreadId = 0;
        mCall.callThreadId = 0;
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