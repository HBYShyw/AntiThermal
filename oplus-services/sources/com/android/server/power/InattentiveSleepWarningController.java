package com.android.server.power;

import android.os.Handler;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.util.Log;
import com.android.internal.annotations.GuardedBy;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.statusbar.IStatusBarService;

@VisibleForTesting
/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public class InattentiveSleepWarningController {
    private static final String TAG = "InattentiveSleepWarning";
    private final Handler mHandler = new Handler();
    private boolean mIsShown;
    private IStatusBarService mStatusBarService;

    @GuardedBy({"PowerManagerService.mLock"})
    public boolean isShown() {
        return this.mIsShown;
    }

    @GuardedBy({"PowerManagerService.mLock"})
    public void show() {
        if (isShown()) {
            return;
        }
        this.mHandler.post(new Runnable() { // from class: com.android.server.power.InattentiveSleepWarningController$$ExternalSyntheticLambda1
            @Override // java.lang.Runnable
            public final void run() {
                InattentiveSleepWarningController.this.showInternal();
            }
        });
        this.mIsShown = true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void showInternal() {
        try {
            getStatusBar().showInattentiveSleepWarning();
        } catch (RemoteException e) {
            Log.e(TAG, "Failed to show inattentive sleep warning", e);
            this.mIsShown = false;
        }
    }

    @GuardedBy({"PowerManagerService.mLock"})
    public void dismiss(final boolean z) {
        if (isShown()) {
            this.mHandler.post(new Runnable() { // from class: com.android.server.power.InattentiveSleepWarningController$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    InattentiveSleepWarningController.this.lambda$dismiss$0(z);
                }
            });
            this.mIsShown = false;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* renamed from: dismissInternal, reason: merged with bridge method [inline-methods] */
    public void lambda$dismiss$0(boolean z) {
        try {
            getStatusBar().dismissInattentiveSleepWarning(z);
        } catch (RemoteException e) {
            Log.e(TAG, "Failed to dismiss inattentive sleep warning", e);
        }
    }

    private IStatusBarService getStatusBar() {
        if (this.mStatusBarService == null) {
            this.mStatusBarService = IStatusBarService.Stub.asInterface(ServiceManager.getService("statusbar"));
        }
        return this.mStatusBarService;
    }
}
