package com.oplus.epona;

/* loaded from: classes.dex */
public interface Call {

    /* loaded from: classes.dex */
    public interface Callback {
        void onReceive(Response response);
    }

    void asyncExecute(Callback callback);

    Response execute();

    Request request();
}
