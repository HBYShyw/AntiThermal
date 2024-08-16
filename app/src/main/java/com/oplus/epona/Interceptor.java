package com.oplus.epona;

import com.oplus.epona.Call;

/* loaded from: classes.dex */
public interface Interceptor {

    /* loaded from: classes.dex */
    public interface Chain {
        Call.Callback callback();

        boolean isAsync();

        void proceed();

        Request request();
    }

    void intercept(Chain chain);
}
