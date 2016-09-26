package com.deft.executor;

import org.junit.Test;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.assertj.core.api.Assertions.assertThat;
/**
 * Created by wei on 16-9-15.
 */
public class TaskTest {
    @Test
    public void testCallbaskClass(){
        assertThat(new DTask().getCallBackClass()).isEqualTo(ICall.class);
        assertThat(new TTask<Object>().getCallBackClass()).isEqualTo(ICall.class);
        assertThat(new BTask().getCallBackClass()).isEqualTo(ICall.class);
    }

    @Test
    public void testBindCallBack(){
        ICall call = new ICall() {
            public int getInt() {
                return 10;
            }

            public String getString() {
                return "hello";
            }

            public void call(String parm) {
                assertThat("hello10").isEqualTo(parm);
            }
        };
        DTask task = new DTask();

        task.bindCallback(call);
        assertTrue(task.isBind());

        task.run();

        task.ubBind();
        assertFalse(task.isBind());
    }
}
