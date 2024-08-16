package com.android.server.wm;

import com.android.server.wm.StartingSurfaceController;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
public class SplashScreenStartingData extends StartingData {
    private final int mTheme;

    @Override // com.android.server.wm.StartingData
    boolean needRevealAnimation() {
        return true;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public SplashScreenStartingData(WindowManagerService windowManagerService, int i, int i2) {
        super(windowManagerService, i2);
        this.mTheme = i;
    }

    @Override // com.android.server.wm.StartingData
    StartingSurfaceController.StartingSurface createStartingSurface(ActivityRecord activityRecord) {
        return this.mService.mStartingSurfaceController.createSplashScreenStartingSurface(activityRecord, this.mTheme);
    }
}
