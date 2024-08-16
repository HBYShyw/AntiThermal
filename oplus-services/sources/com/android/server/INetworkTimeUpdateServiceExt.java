package com.android.server;

import android.content.Context;
import com.android.server.timedetector.NetworkTimeUpdateService;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public interface INetworkTimeUpdateServiceExt {
    default void checkSystemTime() {
    }

    default void init(Context context, NetworkTimeUpdateService networkTimeUpdateService) {
    }

    default boolean isAutoTimeOrSkipPollNetworkTime() {
        return false;
    }
}
