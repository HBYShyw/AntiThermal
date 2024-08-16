package com.android.server.biometrics.sensors.fingerprint.aidl;

import android.content.Context;
import android.hardware.biometrics.BiometricManager;
import android.hardware.keymaster.HardwareAuthToken;
import android.os.RemoteException;
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
import java.util.function.Supplier;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public class FingerprintResetLockoutClient extends HalClientMonitor<AidlSession> implements ErrorConsumer {
    private static final String TAG = "FingerprintResetLockoutClient";
    private final int mBiometricStrength;
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
    public FingerprintResetLockoutClient(Context context, Supplier<AidlSession> supplier, int i, String str, int i2, BiometricLogger biometricLogger, BiometricContext biometricContext, byte[] bArr, LockoutCache lockoutCache, LockoutResetDispatcher lockoutResetDispatcher, @BiometricManager.Authenticators.Types int i3) {
        super(context, supplier, null, null, i, str, 0, i2, biometricLogger, biometricContext);
        this.mHardwareAuthToken = HardwareAuthTokenUtils.toHardwareAuthToken(bArr);
        this.mLockoutCache = lockoutCache;
        this.mLockoutResetDispatcher = lockoutResetDispatcher;
        this.mBiometricStrength = i3;
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
            getFreshDaemon().getSession().resetLockout(this.mHardwareAuthToken);
        } catch (RemoteException e) {
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
        lockoutCache.setLockoutModeForUser(i2, 0);
        lockoutResetDispatcher.notifyLockoutResetCallbacks(i);
        authSessionCoordinator.resetLockoutFor(i2, i3, j);
    }

    @Override // com.android.server.biometrics.sensors.ErrorConsumer
    public void onError(int i, int i2) {
        Slog.e(TAG, "Error during resetLockout: " + i);
        this.mCallback.onClientFinished(this, false);
    }
}
