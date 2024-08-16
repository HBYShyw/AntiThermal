package com.android.server.biometrics.sensors.face.aidl;

import android.content.Context;
import android.hardware.biometrics.BiometricManager;
import android.hardware.keymaster.HardwareAuthToken;
import android.util.Slog;
import com.android.server.biometrics.HardwareAuthTokenUtils;
import com.android.server.biometrics.log.BiometricContext;
import com.android.server.biometrics.log.BiometricLogger;
import com.android.server.biometrics.sensors.AuthSessionCoordinator;
import com.android.server.biometrics.sensors.ClientMonitorCallback;
import com.android.server.biometrics.sensors.ErrorConsumer;
import com.android.server.biometrics.sensors.HalClientMonitor;
import com.android.server.biometrics.sensors.LockoutCache;
import com.android.server.biometrics.sensors.LockoutResetDispatcher;
import com.android.server.biometrics.sensors.face.IFaceResetLockoutClientExt;
import java.util.function.Supplier;
import system.ext.loader.core.ExtLoader;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public class FaceResetLockoutClient extends HalClientMonitor<AidlSession> implements ErrorConsumer {
    private static final String TAG = "FaceResetLockoutClient";
    private final int mBiometricStrength;
    IFaceResetLockoutClientExt mFaceResetLockoutClientExt;
    private final HardwareAuthToken mHardwareAuthToken;
    private final LockoutCache mLockoutCache;
    private final LockoutResetDispatcher mLockoutResetDispatcher;

    @Override // com.android.server.biometrics.sensors.BaseClientMonitor
    public int getProtoEnum() {
        return 12;
    }

    @Override // com.android.server.biometrics.sensors.BaseClientMonitor
    public boolean interruptsPrecedingClients() {
        return true;
    }

    @Override // com.android.server.biometrics.sensors.HalClientMonitor
    public void unableToStart() {
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public FaceResetLockoutClient(Context context, Supplier<AidlSession> supplier, int i, String str, int i2, BiometricLogger biometricLogger, BiometricContext biometricContext, byte[] bArr, LockoutCache lockoutCache, LockoutResetDispatcher lockoutResetDispatcher, @BiometricManager.Authenticators.Types int i3) {
        super(context, supplier, null, null, i, str, 0, i2, biometricLogger, biometricContext);
        this.mFaceResetLockoutClientExt = (IFaceResetLockoutClientExt) ExtLoader.type(IFaceResetLockoutClientExt.class).base(this).create();
        HardwareAuthToken hardwareAuthToken = HardwareAuthTokenUtils.toHardwareAuthToken(bArr);
        this.mHardwareAuthToken = hardwareAuthToken;
        this.mLockoutCache = lockoutCache;
        this.mLockoutResetDispatcher = lockoutResetDispatcher;
        this.mBiometricStrength = i3;
        this.mFaceResetLockoutClientExt.init(context, supplier, hardwareAuthToken);
    }

    @Override // com.android.server.biometrics.sensors.BaseClientMonitor
    public void start(ClientMonitorCallback clientMonitorCallback) {
        super.start(clientMonitorCallback);
        startHalOperation();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.android.server.biometrics.sensors.HalClientMonitor
    public void startHalOperation() {
        try {
            IFaceResetLockoutClientExt iFaceResetLockoutClientExt = this.mFaceResetLockoutClientExt;
            if (iFaceResetLockoutClientExt != null) {
                iFaceResetLockoutClientExt.startHalOperation();
            } else {
                getFreshDaemon().getSession().resetLockout(this.mHardwareAuthToken);
            }
        } catch (Exception e) {
            Slog.e(TAG, "Unable to reset lockout", e);
            this.mCallback.onClientFinished(this, false);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void onLockoutCleared() {
        resetLocalLockoutStateToNone(getSensorId(), getTargetUserId(), this.mLockoutCache, this.mLockoutResetDispatcher, getBiometricContext().getAuthSessionCoordinator(), this.mBiometricStrength, getRequestId());
        this.mCallback.onClientFinished(this, true);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void resetLocalLockoutStateToNone(int i, int i2, LockoutCache lockoutCache, LockoutResetDispatcher lockoutResetDispatcher, AuthSessionCoordinator authSessionCoordinator, @BiometricManager.Authenticators.Types int i3, long j) {
        authSessionCoordinator.resetLockoutFor(i2, i3, j);
        lockoutCache.setLockoutModeForUser(i2, 0);
        lockoutResetDispatcher.notifyLockoutResetCallbacks(i);
    }

    @Override // com.android.server.biometrics.sensors.ErrorConsumer
    public void onError(int i, int i2) {
        Slog.e(TAG, "Error during resetLockout: " + i);
        this.mCallback.onClientFinished(this, false);
    }
}
