package com.android.server.wm;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
public class TaskFragmentSocExtImpl implements ITaskFragmentSocExt {
    TaskFragment mTaskFragment;

    @Override // com.android.server.wm.ITaskFragmentSocExt
    public void hookTriggerActivityPause(ActivityRecord activityRecord) {
    }

    @Override // com.android.server.wm.ITaskFragmentSocExt
    public void hookTriggerActivityResume(ActivityRecord activityRecord) {
    }

    @Override // com.android.server.wm.ITaskFragmentSocExt
    public void hookVendorHintAnimBoost(ActivityRecord activityRecord, ActivityRecord activityRecord2) {
    }

    @Override // com.android.server.wm.ITaskFragmentSocExt
    public void initPerf() {
    }

    public TaskFragmentSocExtImpl(Object obj) {
        this.mTaskFragment = (TaskFragment) obj;
    }
}
