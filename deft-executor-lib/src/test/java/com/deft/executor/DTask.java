package com.deft.executor;

/**
 * Created by wei on 16-9-15.
 */
public class DTask extends Task<ICall> {
    public void run() {
        proxy().call(proxy().getString() + proxy().getInt());
    }
}

class TTask<T> extends DTask{
}

class BTask extends TTask<String>{
}
