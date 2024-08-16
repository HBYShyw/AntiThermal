package com.android.server.wm;

import android.app.ActivityOptions;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
public interface ITaskLaunchParamsModifierExt {
    default TaskDisplayArea modifierTaskDisplayAreaIfNeed(ActivityRecord activityRecord, ActivityOptions activityOptions, TaskDisplayArea taskDisplayArea, Task task, ActivityRecord activityRecord2) {
        return taskDisplayArea;
    }

    default TaskDisplayArea modifierTaskDisplayAreaIfNeed(ActivityTaskSupervisor activityTaskSupervisor, TaskDisplayArea taskDisplayArea, ActivityRecord activityRecord) {
        return taskDisplayArea;
    }

    default TaskDisplayArea modifierTaskDisplayAreaIfNeed(ActivityTaskSupervisor activityTaskSupervisor, TaskDisplayArea taskDisplayArea, ActivityRecord activityRecord, boolean z, Task task) {
        return taskDisplayArea;
    }

    default TaskDisplayArea modifierTaskDisplayAreaIfNeed(ActivityTaskSupervisor activityTaskSupervisor, TaskDisplayArea taskDisplayArea, Task task, ActivityRecord activityRecord) {
        return taskDisplayArea;
    }
}
