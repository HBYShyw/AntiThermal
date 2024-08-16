package com.oplus.deepthinker;

/* loaded from: classes.dex */
public interface ServiceStateObserver {
    default void onServiceDied() {
    }

    default void onStartup() {
    }
}
