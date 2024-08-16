package com.oplus.epona;

import com.oplus.epona.Call;

/* loaded from: classes.dex */
public interface DynamicProvider {
    String getName();

    default boolean needIPC() {
        return false;
    }

    Response onCall(Request request);

    default void onCall(Request request, Call.Callback callback) {
        callback.onReceive(onCall(request));
    }
}
