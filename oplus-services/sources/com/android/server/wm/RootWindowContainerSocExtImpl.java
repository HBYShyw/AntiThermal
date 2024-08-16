package com.android.server.wm;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
public class RootWindowContainerSocExtImpl implements IRootWindowContainerSocExt {
    RootWindowContainer mRootWindowContainer;

    @Override // com.android.server.wm.IRootWindowContainerSocExt
    public void acquireAppLaunchPerfLock(ActivityRecord activityRecord, ActivityTaskManagerService activityTaskManagerService) {
    }

    @Override // com.android.server.wm.IRootWindowContainerSocExt
    public void acquireUxPerfLock(int i, String str) {
    }

    public RootWindowContainerSocExtImpl(Object obj) {
        this.mRootWindowContainer = (RootWindowContainer) obj;
    }
}
