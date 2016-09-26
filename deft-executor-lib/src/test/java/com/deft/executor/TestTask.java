package com.deft.executor;

/**
 * Created by Administrator on 2016/9/26.
 */
public class TestTask extends Task<ICall>{
    int ivalue;
    String svalue;
    boolean bvalue;
    String[] args;

    @Override
    public void run() {
        proxy().setBoolean(bvalue);
        proxy().setInt(ivalue);
        proxy().setString(svalue);

        proxy().call(args);
        proxy().callSync(args);
        proxy().callAsync(args);

        ivalue = proxy().getInt();
        svalue = proxy().getString();
        bvalue = proxy().getBoolean();
    }
}
