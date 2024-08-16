package com.android.server.wm;

import android.app.ActivityOptions;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
public interface IOplusActivityStackInner {
    default boolean canEnterPipOnTaskSwitch(ActivityRecord activityRecord, Task task, ActivityRecord activityRecord2, ActivityOptions activityOptions) {
        return false;
    }
}
