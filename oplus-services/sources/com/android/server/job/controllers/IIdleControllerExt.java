package com.android.server.job.controllers;

import com.android.server.job.JobSchedulerService;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public interface IIdleControllerExt {
    public static final String ACTION_FAST_IDLE_TRIGGER_INTENT = "android.intent.action.FAST_IDLE_TRIGGER";

    default void addTasks(JobStatus jobStatus) {
    }

    default void handleFastIdleTrigger(boolean z, boolean z2, boolean z3) {
    }

    default void initFastIdle(JobSchedulerService jobSchedulerService) {
    }

    default void removeTasks(JobStatus jobStatus) {
    }

    default void updateFastIdleflag() {
    }
}
