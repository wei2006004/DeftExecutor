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

            public void call(String[] parm) {
                assertThat("hello10").isEqualTo(parm[0]);
            }
        };
        DTask task = new DTask();

        task.bindCallback(call);
        assertTrue(task.isBind());

        task.run();

        task.ubBind();
        assertFalse(task.isBind());
    }

    @Test
    public void testDefaultCallBack(){
        DefaultTask task = new DefaultTask();
        task.run();
        assertFalse(task.isBind());
    }

    private static class DefaultTask extends Task<ICall> {

        @Override
        public void run() {
            ICall proxy = proxy();

            assertThat(proxy.getBoolean()).isEqualTo(false);
            assertThat(proxy.getInt()).isEqualTo(0);
            assertThat(proxy.getString()).isEmpty();

            proxy.setBoolean(true);
            proxy.setInt(2);
            proxy.setString("");
            proxy.call(null);
        }
    }

    private static class DTask extends Task<ICall> {
        public void run() {
            proxy().call(new String[]{proxy().getString() + proxy().getInt()});
        }
    }

    private static class TTask<T> extends DTask {
    }

    private static class BTask extends TTask<String> {
    }
}
