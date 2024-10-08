package com.android.server.autofill.ui;

import android.app.AppOpsManager;
import android.content.Context;
import android.os.Binder;
import android.os.IBinder;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
class OverlayControl {
    private final AppOpsManager mAppOpsManager;
    private final IBinder mToken = new Binder();

    /* JADX INFO: Access modifiers changed from: package-private */
    public OverlayControl(Context context) {
        this.mAppOpsManager = (AppOpsManager) context.getSystemService(AppOpsManager.class);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void hideOverlays() {
        setOverlayAllowed(false);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void showOverlays() {
        setOverlayAllowed(true);
    }

    private void setOverlayAllowed(boolean z) {
        AppOpsManager appOpsManager = this.mAppOpsManager;
        if (appOpsManager != null) {
            appOpsManager.setUserRestrictionForUser(24, !z, this.mToken, null, -1);
            this.mAppOpsManager.setUserRestrictionForUser(45, !z, this.mToken, null, -1);
        }
    }
}
