package com.android.server.wm;

import com.android.server.wm.ActivityRecord;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
public class ActivityTaskSupervisorSocExtImpl implements IActivityTaskSupervisorSocExt {
    ActivityTaskSupervisor mSupervisor;

    @Override // com.android.server.wm.IActivityTaskSupervisorSocExt
    public void acquireAppLaunchPerfLock(ActivityRecord activityRecord, ActivityTaskManagerService activityTaskManagerService) {
    }

    @Override // com.android.server.wm.IActivityTaskSupervisorSocExt
    public void notifyServiceTracker(ActivityRecord.State state, boolean z, ActivityRecord activityRecord, long j) {
    }

    @Override // com.android.server.wm.IActivityTaskSupervisorSocExt
    public void reportActivityLaunchedPerfHint(ActivityRecord activityRecord) {
    }

    @Override // com.android.server.wm.IActivityTaskSupervisorSocExt
    public void startPreferredApps(String str, ActivityTaskManagerService activityTaskManagerService) {
    }

    @Override // com.android.server.wm.IActivityTaskSupervisorSocExt
    public void startSpecificActivityPerfHint(String str, ActivityRecord activityRecord, int i) {
    }

    public ActivityTaskSupervisorSocExtImpl(Object obj) {
        this.mSupervisor = (ActivityTaskSupervisor) obj;
    }
}
