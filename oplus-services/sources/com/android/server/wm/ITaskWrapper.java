package com.android.server.wm;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
public interface ITaskWrapper {
    default void activityInvokeNotification(String str, boolean z) {
    }

    default WindowProcessController getWindowProcessController() {
        return null;
    }

    default void onARStopTriggered(ActivityRecord activityRecord) {
    }

    default void removeHiddenFlags(int i) {
    }

    default ActivityRecord topRunningActivityLocked() {
        return null;
    }

    default ITaskExt getExtImpl() {
        return new ITaskExt() { // from class: com.android.server.wm.ITaskWrapper.1
        };
    }
}
