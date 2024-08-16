package com.android.server.wm;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
public class ActivityStarterSocExtImpl implements IActivityStarterSocExt {
    ActivityStarter mActivityStarter;

    @Override // com.android.server.wm.IActivityStarterSocExt
    public void hookNewTask(String str, ActivityRecord activityRecord) {
    }

    @Override // com.android.server.wm.IActivityStarterSocExt
    public void initSoc() {
    }

    public ActivityStarterSocExtImpl(Object obj) {
        this.mActivityStarter = (ActivityStarter) obj;
    }
}
