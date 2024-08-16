package com.android.server.wm;

import android.window.TaskSnapshot;
import com.android.server.wm.StartingSurfaceController;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
public class SnapshotStartingData extends StartingData {
    private final WindowManagerService mService;
    private final TaskSnapshot mSnapshot;

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.android.server.wm.StartingData
    public boolean needRevealAnimation() {
        return false;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public SnapshotStartingData(WindowManagerService windowManagerService, TaskSnapshot taskSnapshot, int i) {
        super(windowManagerService, i);
        this.mService = windowManagerService;
        this.mSnapshot = taskSnapshot;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.android.server.wm.StartingData
    public StartingSurfaceController.StartingSurface createStartingSurface(ActivityRecord activityRecord) {
        return this.mService.mStartingSurfaceController.createTaskSnapshotSurface(activityRecord, this.mSnapshot);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.android.server.wm.StartingData
    public boolean hasImeSurface() {
        return this.mSnapshot.hasImeSurface();
    }
}
