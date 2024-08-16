package com.android.server.pm;

import com.android.server.am.ActivityManagerService;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public interface IBatchExt {
    default ActivityManagerService getAmsRef() {
        return null;
    }

    default int getUserId() {
        return -10000;
    }

    default boolean isAsyncJob() {
        return false;
    }

    default boolean isUserRunningAndNotStopping() {
        return true;
    }

    default void setAmsRef(ActivityManagerService activityManagerService) {
    }

    default void setIsAsyncJob(boolean z) {
    }

    default void setUserId(int i) {
    }
}
