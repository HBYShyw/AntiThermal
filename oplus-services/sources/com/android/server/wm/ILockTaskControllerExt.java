package com.android.server.wm;

import android.content.Context;
import android.util.SparseArray;
import java.io.PrintWriter;
import java.util.ArrayList;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
public interface ILockTaskControllerExt {
    default void dump(PrintWriter printWriter, String str) {
    }

    default void init(Context context, ActivityTaskSupervisor activityTaskSupervisor, LockTaskController lockTaskController, ArrayList<Task> arrayList, SparseArray<String[]> sparseArray) {
    }

    default boolean isLockDeviceMode() {
        return false;
    }

    default boolean isLockTaskModeViolationInternal(WindowContainer windowContainer) {
        return false;
    }

    default boolean isLockTaskModeViolationInternal(WindowContainer windowContainer, int i) {
        return false;
    }

    default boolean setLockTaskMode(ArrayList<Task> arrayList, Task task) {
        return false;
    }

    default void stopLockDeviceModeBySystem() {
    }
}
