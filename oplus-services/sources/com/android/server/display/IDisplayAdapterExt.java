package com.android.server.display;

import android.os.Handler;
import com.android.server.display.DisplayAdapter;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public interface IDisplayAdapterExt {
    default void sendDisplayDeviceEventLocked(DisplayDevice displayDevice, int i) {
    }

    default void setDisplayHandler(Handler handler) {
    }

    default void setListener(DisplayAdapter.Listener listener) {
    }
}
