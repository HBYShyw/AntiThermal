package com.android.server.policy;

import android.R;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.biometrics.BiometricStateListener;
import android.hardware.fingerprint.FingerprintManager;
import android.hardware.fingerprint.FingerprintSensorPropertiesInternal;
import android.hardware.fingerprint.IFingerprintAuthenticatorsRegisteredCallback;
import android.os.Handler;
import android.os.PowerManager;
import android.util.Log;
import android.view.View;
import com.android.internal.annotations.VisibleForTesting;
import com.android.server.policy.SideFpsEventHandler;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public class SideFpsEventHandler implements View.OnClickListener {
    private static final int DEBOUNCE_DELAY_MILLIS = 500;
    private static final String TAG = "SideFpsEventHandler";
    private int mBiometricState;
    private final Context mContext;
    private SideFpsToast mDialog;
    private DialogProvider mDialogProvider;
    private final int mDismissDialogTimeout;
    private FingerprintManager mFingerprintManager;
    private final Handler mHandler;
    private long mLastPowerPressTime;
    private final PowerManager mPowerManager;
    private final AtomicBoolean mSideFpsEventHandlerReady;
    private final Runnable mTurnOffDialog;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public interface DialogProvider {
        SideFpsToast provideDialog(Context context);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$0() {
        dismissDialog("mTurnOffDialog");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public SideFpsEventHandler(Context context, Handler handler, PowerManager powerManager) {
        this(context, handler, powerManager, new DialogProvider() { // from class: com.android.server.policy.SideFpsEventHandler$$ExternalSyntheticLambda0
            @Override // com.android.server.policy.SideFpsEventHandler.DialogProvider
            public final SideFpsToast provideDialog(Context context2) {
                SideFpsToast lambda$new$1;
                lambda$new$1 = SideFpsEventHandler.lambda$new$1(context2);
                return lambda$new$1;
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ SideFpsToast lambda$new$1(Context context) {
        SideFpsToast sideFpsToast = new SideFpsToast(context);
        sideFpsToast.getWindow().setType(2017);
        sideFpsToast.requestWindowFeature(1);
        return sideFpsToast;
    }

    @VisibleForTesting
    SideFpsEventHandler(Context context, Handler handler, PowerManager powerManager, DialogProvider dialogProvider) {
        this.mTurnOffDialog = new Runnable() { // from class: com.android.server.policy.SideFpsEventHandler$$ExternalSyntheticLambda1
            @Override // java.lang.Runnable
            public final void run() {
                SideFpsEventHandler.this.lambda$new$0();
            }
        };
        this.mContext = context;
        this.mHandler = handler;
        this.mPowerManager = powerManager;
        this.mBiometricState = 0;
        this.mSideFpsEventHandlerReady = new AtomicBoolean(false);
        this.mDialogProvider = dialogProvider;
        context.registerReceiver(new BroadcastReceiver() { // from class: com.android.server.policy.SideFpsEventHandler.1
            @Override // android.content.BroadcastReceiver
            public void onReceive(Context context2, Intent intent) {
                if (SideFpsEventHandler.this.mDialog != null) {
                    SideFpsEventHandler.this.mDialog.dismiss();
                    SideFpsEventHandler.this.mDialog = null;
                }
            }
        }, new IntentFilter("android.intent.action.SCREEN_OFF"));
        this.mDismissDialogTimeout = context.getResources().getInteger(R.integer.kg_carousel_angle);
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View view) {
        goToSleep(this.mLastPowerPressTime);
    }

    public void notifyPowerPressed() {
        Log.i(TAG, "notifyPowerPressed");
        if (this.mFingerprintManager == null && this.mSideFpsEventHandlerReady.get()) {
            this.mFingerprintManager = (FingerprintManager) this.mContext.getSystemService(FingerprintManager.class);
        }
        FingerprintManager fingerprintManager = this.mFingerprintManager;
        if (fingerprintManager == null) {
            return;
        }
        fingerprintManager.onPowerPressed();
    }

    public boolean shouldConsumeSinglePress(final long j) {
        if (!this.mSideFpsEventHandlerReady.get()) {
            return false;
        }
        int i = this.mBiometricState;
        if (i != 1) {
            return i == 3;
        }
        this.mHandler.post(new Runnable() { // from class: com.android.server.policy.SideFpsEventHandler$$ExternalSyntheticLambda2
            @Override // java.lang.Runnable
            public final void run() {
                SideFpsEventHandler.this.lambda$shouldConsumeSinglePress$2(j);
            }
        });
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$shouldConsumeSinglePress$2(long j) {
        if (this.mHandler.hasCallbacks(this.mTurnOffDialog)) {
            Log.v(TAG, "Detected a tap to turn off dialog, ignoring");
            this.mHandler.removeCallbacks(this.mTurnOffDialog);
        }
        showDialog(j, "Enroll Power Press");
        this.mHandler.postDelayed(this.mTurnOffDialog, this.mDismissDialogTimeout);
    }

    private void goToSleep(long j) {
        this.mPowerManager.goToSleep(j, 4, 0);
    }

    public void onFingerprintSensorReady() {
        if (this.mContext.getPackageManager().hasSystemFeature("android.hardware.fingerprint")) {
            final FingerprintManager fingerprintManager = (FingerprintManager) this.mContext.getSystemService(FingerprintManager.class);
            fingerprintManager.addAuthenticatorsRegisteredCallback(new IFingerprintAuthenticatorsRegisteredCallback.Stub() { // from class: com.android.server.policy.SideFpsEventHandler.2
                public void onAllAuthenticatorsRegistered(List<FingerprintSensorPropertiesInternal> list) {
                    if (fingerprintManager.isPowerbuttonFps()) {
                        fingerprintManager.registerBiometricStateListener(new AnonymousClass1());
                        SideFpsEventHandler.this.mSideFpsEventHandlerReady.set(true);
                    }
                }

                /* JADX INFO: Access modifiers changed from: package-private */
                /* renamed from: com.android.server.policy.SideFpsEventHandler$2$1, reason: invalid class name */
                /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
                public class AnonymousClass1 extends BiometricStateListener {
                    private Runnable mStateRunnable = null;

                    AnonymousClass1() {
                    }

                    public void onStateChanged(final int i) {
                        Log.d(SideFpsEventHandler.TAG, "onStateChanged : " + i);
                        if (this.mStateRunnable != null) {
                            SideFpsEventHandler.this.mHandler.removeCallbacks(this.mStateRunnable);
                            this.mStateRunnable = null;
                        }
                        if (i == 0) {
                            this.mStateRunnable = new Runnable() { // from class: com.android.server.policy.SideFpsEventHandler$2$1$$ExternalSyntheticLambda0
                                @Override // java.lang.Runnable
                                public final void run() {
                                    SideFpsEventHandler.AnonymousClass2.AnonymousClass1.this.lambda$onStateChanged$0(i);
                                }
                            };
                            SideFpsEventHandler.this.mHandler.postDelayed(this.mStateRunnable, 500L);
                            SideFpsEventHandler.this.dismissDialog("STATE_IDLE");
                            return;
                        }
                        SideFpsEventHandler.this.mBiometricState = i;
                    }

                    /* JADX INFO: Access modifiers changed from: private */
                    public /* synthetic */ void lambda$onStateChanged$0(int i) {
                        SideFpsEventHandler.this.mBiometricState = i;
                    }

                    public void onBiometricAction(int i) {
                        Log.d(SideFpsEventHandler.TAG, "onBiometricAction " + i);
                    }
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void dismissDialog(String str) {
        Log.d(TAG, "Dismissing dialog with reason: " + str);
        SideFpsToast sideFpsToast = this.mDialog;
        if (sideFpsToast == null || !sideFpsToast.isShowing()) {
            return;
        }
        this.mDialog.dismiss();
    }

    private void showDialog(long j, String str) {
        Log.d(TAG, "Showing dialog with reason: " + str);
        SideFpsToast sideFpsToast = this.mDialog;
        if (sideFpsToast != null && sideFpsToast.isShowing()) {
            Log.d(TAG, "Ignoring show dialog");
            return;
        }
        SideFpsToast provideDialog = this.mDialogProvider.provideDialog(this.mContext);
        this.mDialog = provideDialog;
        this.mLastPowerPressTime = j;
        provideDialog.show();
        this.mDialog.setOnClickListener(this);
    }
}
