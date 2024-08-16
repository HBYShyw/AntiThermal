package com.oplus.nas.cybersense.sdk;

import android.os.Bundle;

/* loaded from: classes.dex */
public interface IEventCallback {
    @Deprecated
    default void onEventStateChanged(int type, String result) {
    }

    default void onEventStateChanged(int type, Bundle result) {
    }
}
