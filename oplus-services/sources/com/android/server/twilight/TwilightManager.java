package com.android.server.twilight;

import android.os.Handler;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public interface TwilightManager {
    TwilightState getLastTwilightState();

    void registerListener(TwilightListener twilightListener, Handler handler);

    void unregisterListener(TwilightListener twilightListener);
}
