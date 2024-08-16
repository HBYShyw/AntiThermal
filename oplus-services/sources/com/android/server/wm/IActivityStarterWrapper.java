package com.android.server.wm;

import android.app.ActivityOptions;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
public interface IActivityStarterWrapper {
    default ActivityOptions getOptions() {
        return null;
    }

    default ActivityTaskManagerService getService() {
        return null;
    }

    default void setInTask(Task task) {
    }

    default void setOptions(ActivityOptions activityOptions) {
    }

    default void setSourceRecord(ActivityRecord activityRecord) {
    }

    default void setSourceRootTask(Task task) {
    }

    default void setTargetRootTaskIfNeeded(ActivityRecord activityRecord) {
    }
}
