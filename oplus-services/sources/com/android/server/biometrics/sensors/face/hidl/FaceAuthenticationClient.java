package com.android.server.biometrics.sensors.face.hidl;

import android.R;
import android.content.Context;
import android.content.res.Resources;
import android.hardware.SensorPrivacyManager;
import android.hardware.biometrics.BiometricAuthenticator;
import android.hardware.biometrics.BiometricManager;
import android.hardware.biometrics.face.V1_0.IBiometricsFace;
import android.hardware.face.FaceAuthenticateOptions;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Slog;
import com.android.server.biometrics.Utils;
import com.android.server.biometrics.log.BiometricContext;
import com.android.server.biometrics.log.BiometricLogger;
import com.android.server.biometrics.sensors.AuthenticationClient;
import com.android.server.biometrics.sensors.BiometricNotificationUtils;
import com.android.server.biometrics.sensors.ClientMonitorCallback;
import com.android.server.biometrics.sensors.ClientMonitorCallbackConverter;
import com.android.server.biometrics.sensors.ClientMonitorCompositeCallback;
import com.android.server.biometrics.sensors.LockoutTracker;
import com.android.server.biometrics.sensors.PerformanceTracker;
import com.android.server.biometrics.sensors.face.UsageStats;
import java.util.ArrayList;
import java.util.function.Supplier;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public class FaceAuthenticationClient extends AuthenticationClient<IBiometricsFace, FaceAuthenticateOptions> {
    private static final String TAG = "FaceAuthenticationClient";
    private final int[] mBiometricPromptIgnoreList;
    private final int[] mBiometricPromptIgnoreListVendor;
    private final int[] mKeyguardIgnoreList;
    private final int[] mKeyguardIgnoreListVendor;
    private int mLastAcquire;
    private SensorPrivacyManager mSensorPrivacyManager;
    private final UsageStats mUsageStats;

    /* JADX INFO: Access modifiers changed from: package-private */
    public FaceAuthenticationClient(Context context, Supplier<IBiometricsFace> supplier, IBinder iBinder, long j, ClientMonitorCallbackConverter clientMonitorCallbackConverter, long j2, boolean z, FaceAuthenticateOptions faceAuthenticateOptions, int i, boolean z2, BiometricLogger biometricLogger, BiometricContext biometricContext, boolean z3, LockoutTracker lockoutTracker, UsageStats usageStats, boolean z4, @BiometricManager.Authenticators.Types int i2) {
        super(context, supplier, iBinder, clientMonitorCallbackConverter, j2, z, faceAuthenticateOptions, i, z2, biometricLogger, biometricContext, z3, null, lockoutTracker, z4, true, i2);
        setRequestId(j);
        this.mUsageStats = usageStats;
        this.mSensorPrivacyManager = (SensorPrivacyManager) context.getSystemService(SensorPrivacyManager.class);
        Resources resources = getContext().getResources();
        this.mBiometricPromptIgnoreList = resources.getIntArray(R.array.config_restrictedImagesServices);
        this.mBiometricPromptIgnoreListVendor = resources.getIntArray(R.array.config_screenBrighteningThresholds);
        this.mKeyguardIgnoreList = resources.getIntArray(R.array.config_safeModeEnabledVibePattern);
        this.mKeyguardIgnoreListVendor = resources.getIntArray(R.array.config_screenBrightnessNits);
    }

    @Override // com.android.server.biometrics.sensors.AuthenticationClient, com.android.server.biometrics.sensors.BaseClientMonitor
    public void start(ClientMonitorCallback clientMonitorCallback) {
        super.start(clientMonitorCallback);
        this.mState = 1;
    }

    @Override // com.android.server.biometrics.sensors.BaseClientMonitor
    protected ClientMonitorCallback wrapCallbackForStart(ClientMonitorCallback clientMonitorCallback) {
        return new ClientMonitorCompositeCallback(getLogger().getAmbientLightProbe(true), clientMonitorCallback);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.android.server.biometrics.sensors.HalClientMonitor
    public void startHalOperation() {
        SensorPrivacyManager sensorPrivacyManager = this.mSensorPrivacyManager;
        if (sensorPrivacyManager != null && sensorPrivacyManager.isSensorPrivacyEnabled(1, 2)) {
            onError(1, 0);
            this.mCallback.onClientFinished(this, false);
            return;
        }
        try {
            getFreshDaemon().authenticate(this.mOperationId);
        } catch (RemoteException e) {
            Slog.e(TAG, "Remote exception when requesting auth", e);
            onError(1, 0);
            this.mCallback.onClientFinished(this, false);
        }
    }

    @Override // com.android.server.biometrics.sensors.AcquisitionClient
    protected void stopHalOperation() {
        try {
            getFreshDaemon().cancel();
        } catch (RemoteException e) {
            Slog.e(TAG, "Remote exception when requesting cancel", e);
            onError(1, 0);
            this.mCallback.onClientFinished(this, false);
        }
    }

    @Override // com.android.server.biometrics.sensors.AuthenticationClient
    public boolean wasUserDetected() {
        int i = this.mLastAcquire;
        return (i == 11 || i == 21) ? false : true;
    }

    @Override // com.android.server.biometrics.sensors.AuthenticationClient
    protected void handleLifecycleAfterAuth(boolean z) {
        this.mCallback.onClientFinished(this, true);
    }

    @Override // com.android.server.biometrics.sensors.AuthenticationClient
    public int handleFailedAttempt(int i) {
        int lockoutModeForUser = getLockoutTracker().getLockoutModeForUser(i);
        PerformanceTracker instanceForSensorId = PerformanceTracker.getInstanceForSensorId(getSensorId());
        if (lockoutModeForUser == 2) {
            instanceForSensorId.incrementPermanentLockoutForUser(i);
        } else if (lockoutModeForUser == 1) {
            instanceForSensorId.incrementTimedLockoutForUser(i);
        }
        return lockoutModeForUser;
    }

    @Override // com.android.server.biometrics.sensors.AuthenticationClient, com.android.server.biometrics.sensors.AuthenticationConsumer
    public void onAuthenticated(BiometricAuthenticator.Identifier identifier, boolean z, ArrayList<Byte> arrayList) {
        super.onAuthenticated(identifier, z, arrayList);
        this.mState = 4;
        this.mUsageStats.addEvent(new UsageStats.AuthenticationEvent(getStartTimeMs(), System.currentTimeMillis() - getStartTimeMs(), z, 0, 0, getTargetUserId()));
    }

    @Override // com.android.server.biometrics.sensors.AuthenticationClient, com.android.server.biometrics.sensors.AcquisitionClient, com.android.server.biometrics.sensors.ErrorConsumer
    public void onError(int i, int i2) {
        this.mUsageStats.addEvent(new UsageStats.AuthenticationEvent(getStartTimeMs(), System.currentTimeMillis() - getStartTimeMs(), false, i, i2, getTargetUserId()));
        super.onError(i, i2);
    }

    private int[] getAcquireIgnorelist() {
        return isBiometricPrompt() ? this.mBiometricPromptIgnoreList : this.mKeyguardIgnoreList;
    }

    private int[] getAcquireVendorIgnorelist() {
        return isBiometricPrompt() ? this.mBiometricPromptIgnoreListVendor : this.mKeyguardIgnoreListVendor;
    }

    private boolean shouldSend(int i, int i2) {
        if (i == 22) {
            return !Utils.listContains(getAcquireVendorIgnorelist(), i2);
        }
        return !Utils.listContains(getAcquireIgnorelist(), i);
    }

    @Override // com.android.server.biometrics.sensors.AuthenticationClient, com.android.server.biometrics.sensors.AcquisitionClient
    public void onAcquired(int i, int i2) {
        this.mLastAcquire = i;
        if (i == 13) {
            BiometricNotificationUtils.showReEnrollmentNotification(getContext());
        }
        if (getLockoutTracker().getLockoutModeForUser(getTargetUserId()) == 0) {
            PerformanceTracker.getInstanceForSensorId(getSensorId()).incrementAcquireForUser(getTargetUserId(), isCryptoOperation());
        }
        onAcquiredInternal(i, i2, shouldSend(i, i2));
    }
}
