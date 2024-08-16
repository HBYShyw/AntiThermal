package com.android.server.wm;

import android.content.res.Configuration;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
public interface IWindowProcessControllerExt {
    default boolean canSetPreQTopResumedActivity(TaskFragment taskFragment, int i) {
        return false;
    }

    default boolean getFirstTransferState() {
        return true;
    }

    default ActivityRecord getTopActivity() {
        return null;
    }

    default boolean hookUpdateRapidActivityLaunchSkipApp(String str) {
        return false;
    }

    default boolean hookappEarlyNotRespondingForAging() {
        return false;
    }

    default void hookappEarlyNotRespondingPrecess(ActivityTaskManagerService activityTaskManagerService) {
    }

    default boolean hookappNotRespondingForAgine() {
        return false;
    }

    default void hookappNotRespondingProcess(ActivityTaskManagerService activityTaskManagerService) {
    }

    default void resolveOverrideConfiguration(Configuration configuration, ActivityRecord activityRecord) {
    }

    default void setFirstTransferState() {
    }

    default boolean shouldMakeActivityFinishing(String str, int i) {
        return true;
    }

    default boolean shouldUpdateProcessConfig(WindowProcessController windowProcessController, ActivityTaskManagerService activityTaskManagerService) {
        return false;
    }

    default void updateWaitActivityToAttach(boolean z) {
    }

    default boolean waitActivityToAttach() {
        return false;
    }
}
