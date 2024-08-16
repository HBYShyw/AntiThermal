package com.aiunit.aon.AON;

import android.os.Bundle;
import android.util.Log;

/* loaded from: classes.dex */
public interface AONEventCallback {
    void onAONEvent(int i, int i2);

    default void onAONEvent(int eventType, Bundle aonEventInfo) {
        Log.d("AONEventCallback", "default : not process");
    }
}
