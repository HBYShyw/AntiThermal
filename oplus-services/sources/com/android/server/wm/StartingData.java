package com.android.server.wm;

import com.android.server.wm.StartingSurfaceController;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
public abstract class StartingData {
    Task mAssociatedTask;
    boolean mIsDisplayed;
    boolean mIsTransitionForward;
    boolean mPrepareRemoveAnimation;
    boolean mRemoveAfterTransaction;
    protected final WindowManagerService mService;
    protected final int mTypeParams;
    boolean mWaitForSyncTransactionCommit;

    /* JADX INFO: Access modifiers changed from: package-private */
    public abstract StartingSurfaceController.StartingSurface createStartingSurface(ActivityRecord activityRecord);

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean hasImeSurface() {
        return false;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public abstract boolean needRevealAnimation();

    /* JADX INFO: Access modifiers changed from: protected */
    public StartingData(WindowManagerService windowManagerService, int i) {
        this.mService = windowManagerService;
        this.mTypeParams = i;
    }
}
