package com.android.server.devicepolicy;

import android.app.admin.DevicePolicyManager;
import android.app.admin.DevicePolicyManagerLiteInternal;
import android.app.admin.DevicePolicySafetyChecker;
import android.os.Handler;
import android.os.Looper;
import android.util.Slog;
import com.android.internal.os.IResultReceiver;
import com.android.server.LocalServices;
import java.util.Objects;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public final class OneTimeSafetyChecker implements DevicePolicySafetyChecker {
    private static final long SELF_DESTRUCT_TIMEOUT_MS = 10000;
    private static final String TAG = OneTimeSafetyChecker.class.getSimpleName();
    private boolean mDone;
    private final Handler mHandler;
    private final int mOperation;
    private final DevicePolicySafetyChecker mRealSafetyChecker;
    private final int mReason;
    private final DevicePolicyManagerService mService;

    /* JADX INFO: Access modifiers changed from: package-private */
    public OneTimeSafetyChecker(DevicePolicyManagerService devicePolicyManagerService, int i, int i2) {
        Handler handler = new Handler(Looper.getMainLooper());
        this.mHandler = handler;
        Objects.requireNonNull(devicePolicyManagerService);
        this.mService = devicePolicyManagerService;
        this.mOperation = i;
        this.mReason = i2;
        DevicePolicySafetyChecker devicePolicySafetyChecker = devicePolicyManagerService.getDevicePolicySafetyChecker();
        this.mRealSafetyChecker = devicePolicySafetyChecker;
        Slog.i(TAG, "OneTimeSafetyChecker constructor: operation=" + DevicePolicyManager.operationToString(i) + ", reason=" + DevicePolicyManager.operationSafetyReasonToString(i2) + ", realChecker=" + devicePolicySafetyChecker + ", maxDuration=10000ms");
        handler.postDelayed(new Runnable() { // from class: com.android.server.devicepolicy.OneTimeSafetyChecker$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                OneTimeSafetyChecker.this.lambda$new$0();
            }
        }, 10000L);
    }

    public int getUnsafeOperationReason(int i) {
        int i2;
        String operationToString = DevicePolicyManager.operationToString(i);
        String str = TAG;
        Slog.i(str, "getUnsafeOperationReason(" + operationToString + ")");
        if (i == this.mOperation) {
            i2 = this.mReason;
        } else {
            Slog.wtf(str, "invalid call to isDevicePolicyOperationSafe(): asked for " + operationToString + ", should be " + DevicePolicyManager.operationToString(this.mOperation));
            i2 = -1;
        }
        String operationSafetyReasonToString = DevicePolicyManager.operationSafetyReasonToString(i2);
        DevicePolicyManagerLiteInternal devicePolicyManagerLiteInternal = (DevicePolicyManagerLiteInternal) LocalServices.getService(DevicePolicyManagerLiteInternal.class);
        Slog.i(str, "notifying " + operationSafetyReasonToString + " is UNSAFE");
        devicePolicyManagerLiteInternal.notifyUnsafeOperationStateChanged(this, i2, false);
        Slog.i(str, "notifying " + operationSafetyReasonToString + " is SAFE");
        devicePolicyManagerLiteInternal.notifyUnsafeOperationStateChanged(this, i2, true);
        Slog.i(str, "returning " + operationSafetyReasonToString);
        disableSelf();
        return i2;
    }

    public boolean isSafeOperation(int i) {
        boolean z = this.mReason != i;
        Slog.i(TAG, "isSafeOperation(" + DevicePolicyManager.operationSafetyReasonToString(i) + "): " + z);
        disableSelf();
        return z;
    }

    public void onFactoryReset(IResultReceiver iResultReceiver) {
        throw new UnsupportedOperationException();
    }

    private void disableSelf() {
        if (this.mDone) {
            Slog.w(TAG, "disableSelf(): already disabled");
            return;
        }
        Slog.i(TAG, "restoring DevicePolicySafetyChecker to " + this.mRealSafetyChecker);
        this.mService.setDevicePolicySafetyCheckerUnchecked(this.mRealSafetyChecker);
        this.mDone = true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* renamed from: selfDestruct, reason: merged with bridge method [inline-methods] */
    public void lambda$new$0() {
        if (this.mDone) {
            return;
        }
        Slog.e(TAG, "Self destructing " + this + ", as it was not automatically disabled");
        disableSelf();
    }

    public String toString() {
        return "OneTimeSafetyChecker[id=" + System.identityHashCode(this) + ", reason=" + DevicePolicyManager.operationSafetyReasonToString(this.mReason) + ", operation=" + DevicePolicyManager.operationToString(this.mOperation) + ']';
    }
}
