package com.deft.executor.util;

import com.deft.executor.ICall;
import com.deft.executor.TestCall;
import com.deft.executor.annotation.Asynchronous;
import com.deft.executor.annotation.Synchronous;
import org.junit.Test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;
/**
 * Created by Administrator on 2016/9/28.
 */
public class ProxyUtilsTest {

    interface InnerCall {
        @Synchronous
        boolean getBoolean();

        @Synchronous
        int getInt();

        @Synchronous
        String getString();

        @Synchronous
        default void setBoolean(boolean value) {
        }

        @Synchronous
        default void setInt(int value) {
        }

        @Synchronous
        default void setString(String value) {
        }

        @Synchronous
        void callSync(String[] args);

        @Asynchronous
        void callAsync(String[] args);

        void call(String[] args);
    }

    @Test
    public void createDefaultProxy() throws Exception {
        log("### ICall default proxy");
        ICall iCall = ProxyUtils.createDefaultProxy(ICall.class);
        assertThat(iCall.getBoolean()).isEqualTo(ProxyUtils.DEFAULT_CALLBACK_VALUE_BOOLEAN);
        assertThat(iCall.getInt()).isEqualTo(ProxyUtils.DEFAULT_CALLBACK_VALUE_INT);
        assertThat(iCall.getString()).isEqualTo(ProxyUtils.DEFAULT_CALLBACK_VALUE_STRING);
        logend();

        log("### InnerCall default proxy");
        InnerCall innerCall = ProxyUtils.createDefaultProxy(InnerCall.class);
        assertThat(innerCall.getBoolean()).isEqualTo(ProxyUtils.DEFAULT_CALLBACK_VALUE_BOOLEAN);
        assertThat(innerCall.getInt()).isEqualTo(ProxyUtils.DEFAULT_CALLBACK_VALUE_INT);
        assertThat(innerCall.getString()).isEqualTo(ProxyUtils.DEFAULT_CALLBACK_VALUE_STRING);
        innerCall.call(null);
        innerCall.callAsync(null);
        innerCall.callSync(null);
        logend();
    }

    @Test
    public void createHandlerProxy() throws Exception {
        long currentThreadId = Thread.currentThread().getId();
        ExecutorService executorService = Executors.newSingleThreadExecutor();

        log("### ICall handler proxy");
        TCall tCall = new TCall();
        ICall iCall = ProxyUtils.createHandlerProxy(tCall, ICall.class, executorService);
        iCall.call(null);
        iCall.callSync(null);
        iCall.callAsync(null);

        executorService.awaitTermination(500, TimeUnit.MILLISECONDS);
        assertThat(tCall.callThreadId).isNotEqualTo(currentThreadId);
        assertThat(tCall.syncCallThreadId).isEqualTo(currentThreadId);
        assertThat(tCall.asyncCallThreadId).isNotEqualTo(currentThreadId);
        logend();

        log("### InnerCall handler proxy");
        BCall bCall = new BCall();
        InnerCall innerCall = ProxyUtils.createHandlerProxy(bCall, InnerCall.class, executorService);
        innerCall.call(null);
        innerCall.callSync(null);
        innerCall.callAsync(null);
        assertThat(innerCall.getBoolean()).isEqualTo(true);
        assertThat(innerCall.getInt()).isEqualTo(10);
        assertThat(innerCall.getString()).isEqualTo("hhh");
        executorService.awaitTermination(500, TimeUnit.MILLISECONDS);
        executorService.shutdown();
        logend();
    }

    static class TCall extends TestCall {
        @Override
        public void call(String[] args) {
            log("call:" + args);
            super.call(args);
        }

        @Override
        public void callSync(String[] args) {
            log("callSync:" + args);
            super.callSync(args);
        }

        @Override
        public void callAsync(String[] args) {
            log("callAsync:" + args);
            super.callAsync(args);
        }
    }

    static class BCall implements InnerCall {
        @Override
        public void call(String[] args) {
            log("call:" + args);
        }

        @Override
        public boolean getBoolean() {
            return true;
        }

        @Override
        public int getInt() {
            return 10;
        }

        @Override
        public String getString() {
            return "hhh";
        }

        @Override
        public void callSync(String[] args) {
            log("callSync:" + args);
        }

        @Override
        public void callAsync(String[] args) {
            log("callAsync:" + args);
        }
    }

    static void log(String string) {
        System.out.println("thread(" + Thread.currentThread().getId() + "): " + string);
    }

    static void logend() {
        System.out.println();
    }

}