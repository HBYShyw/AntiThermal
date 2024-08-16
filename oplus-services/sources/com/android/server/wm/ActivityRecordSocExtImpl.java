package com.android.server.wm;

import android.content.pm.ActivityInfo;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
public class ActivityRecordSocExtImpl implements IActivityRecordSocExt {
    public final boolean ENABLE_BOOST_FRAMEWORK = false;
    ActivityRecord mActivityRecord;

    @Override // com.android.server.wm.IActivityRecordSocExt
    public int getPerfActivityBoostHandler() {
        return -1;
    }

    @Override // com.android.server.wm.IActivityRecordSocExt
    public void hookOnWindowsDrawn() {
    }

    @Override // com.android.server.wm.IActivityRecordSocExt
    public void initSoc() {
    }

    @Override // com.android.server.wm.IActivityRecordSocExt
    public int isAppInfoGame(ActivityInfo activityInfo) {
        return 0;
    }

    @Override // com.android.server.wm.IActivityRecordSocExt
    public boolean isEnableBoostFramework() {
        return false;
    }

    @Override // com.android.server.wm.IActivityRecordSocExt
    public boolean isLaunching() {
        return false;
    }

    @Override // com.android.server.wm.IActivityRecordSocExt
    public void perfLockReleaseHandler() {
    }

    @Override // com.android.server.wm.IActivityRecordSocExt
    public void setLaunching(boolean z) {
    }

    @Override // com.android.server.wm.IActivityRecordSocExt
    public void setPerfActivityBoostHandler(int i) {
    }

    @Override // com.android.server.wm.IActivityRecordSocExt
    public void setTranslucentWindowLaunch(boolean z) {
    }

    public ActivityRecordSocExtImpl(Object obj) {
        this.mActivityRecord = (ActivityRecord) obj;
    }
}
