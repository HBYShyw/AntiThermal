package com.android.server.wm;

import android.app.ActivityOptions;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
public interface ISafeActivityOptionsExt {
    default ActivityOptions getActivityOptions() {
        return null;
    }

    default boolean isPuttDisplay(int i) {
        return false;
    }
}
