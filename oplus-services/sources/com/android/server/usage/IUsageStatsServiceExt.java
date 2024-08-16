package com.android.server.usage;

import android.os.HandlerThread;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public interface IUsageStatsServiceExt {
    default HandlerThread getBackgroundHandlerThread() {
        return null;
    }
}
