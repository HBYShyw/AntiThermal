package com.android.server.biometrics.sensors.face.aidl;

import android.R;
import android.app.NotificationManager;
import android.content.Context;
import android.content.res.Resources;
import android.hardware.SensorPrivacyManager;
import android.hardware.biometrics.BiometricAuthenticator;
import android.hardware.biometrics.BiometricManager;
import android.hardware.biometrics.common.ICancellationSignal;
import android.hardware.biometrics.common.OperationContext;
import android.hardware.face.FaceAuthenticateOptions;
import android.hardware.face.FaceAuthenticationFrame;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Slog;
import com.android.internal.annotations.VisibleForTesting;
import com.android.server.biometrics.Utils;
import com.android.server.biometrics.log.BiometricContext;
import com.android.server.biometrics.log.BiometricLogger;
import com.android.server.biometrics.log.OperationContextExt;
import com.android.server.biometrics.sensors.AuthSessionCoordinator;
import com.android.server.biometrics.sensors.AuthenticationClient;
import com.android.server.biometrics.sensors.BiometricNotificationUtils;
import com.android.server.biometrics.sensors.ClientMonitorCallback;
import com.android.server.biometrics.sensors.ClientMonitorCallbackConverter;
import com.android.server.biometrics.sensors.ClientMonitorCompositeCallback;
import com.android.server.biometrics.sensors.LockoutCache;
import com.android.server.biometrics.sensors.LockoutConsumer;
import com.android.server.biometrics.sensors.PerformanceTracker;
import com.android.server.biometrics.sensors.face.UsageStats;
import java.util.ArrayList;
import java.util.function.Consumer;
import java.util.function.Supplier;
import system.ext.loader.core.ExtLoader;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public class FaceAuthenticationClient extends AuthenticationClient<AidlSession, FaceAuthenticateOptions> implements LockoutConsumer {
    private static final String TAG = "FaceAuthenticationClient";
    private final AuthSessionCoordinator mAuthSessionCoordinator;
    private final int[] mBiometricPromptIgnoreList;
    private final int[] mBiometricPromptIgnoreListVendor;
    private ICancellationSignal mCancellationSignal;
    private IFaceAuthenticationClientExt mFaceAuthenticationClientExt;
    private final int[] mKeyguardIgnoreList;
    private final int[] mKeyguardIgnoreListVendor;
    private int mLastAcquire;
    private final NotificationManager mNotificationManager;
    private SensorPrivacyManager mSensorPrivacyManager;
    private final UsageStats mUsageStats;

    /* JADX INFO: Access modifiers changed from: package-private */
    public FaceAuthenticationClient(Context context, Supplier<AidlSession> supplier, IBinder iBinder, long j, ClientMonitorCallbackConverter clientMonitorCallbackConverter, long j2, boolean z, FaceAuthenticateOptions faceAuthenticateOptions, int i, boolean z2, BiometricLogger biometricLogger, BiometricContext biometricContext, boolean z3, UsageStats usageStats, LockoutCache lockoutCache, boolean z4, @BiometricManager.Authenticators.Types int i2) {
        this(context, supplier, iBinder, j, clientMonitorCallbackConverter, j2, z, faceAuthenticateOptions, i, z2, biometricLogger, biometricContext, z3, usageStats, lockoutCache, z4, (SensorPrivacyManager) context.getSystemService(SensorPrivacyManager.class), i2);
    }

    @VisibleForTesting
    FaceAuthenticationClient(Context context, Supplier<AidlSession> supplier, IBinder iBinder, long j, ClientMonitorCallbackConverter clientMonitorCallbackConverter, long j2, boolean z, FaceAuthenticateOptions faceAuthenticateOptions, int i, boolean z2, BiometricLogger biometricLogger, BiometricContext biometricContext, boolean z3, UsageStats usageStats, LockoutCache lockoutCache, boolean z4, SensorPrivacyManager sensorPrivacyManager, @BiometricManager.Authenticators.Types int i2) {
        super(context, supplier, iBinder, clientMonitorCallbackConverter, j2, z, faceAuthenticateOptions, i, z2, biometricLogger, biometricContext, z3, null, null, z4, true, i2);
        this.mLastAcquire = 23;
        setRequestId(j);
        this.mUsageStats = usageStats;
        this.mNotificationManager = (NotificationManager) context.getSystemService(NotificationManager.class);
        this.mSensorPrivacyManager = sensorPrivacyManager;
        this.mAuthSessionCoordinator = biometricContext.getAuthSessionCoordinator();
        Resources resources = getContext().getResources();
        this.mBiometricPromptIgnoreList = resources.getIntArray(R.array.config_restrictedImagesServices);
        this.mBiometricPromptIgnoreListVendor = resources.getIntArray(R.array.config_screenBrighteningThresholds);
        this.mKeyguardIgnoreList = resources.getIntArray(R.array.config_safeModeEnabledVibePattern);
        this.mKeyguardIgnoreListVendor = resources.getIntArray(R.array.config_screenBrightnessNits);
        IFaceAuthenticationClientExt iFaceAuthenticationClientExt = (IFaceAuthenticationClientExt) ExtLoader.type(IFaceAuthenticationClientExt.class).base(this).create();
        this.mFaceAuthenticationClientExt = iFaceAuthenticationClientExt;
        iFaceAuthenticationClientExt.init(context, supplier, faceAuthenticateOptions.getOpPackageName(), faceAuthenticateOptions.getUserId(), j2);
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
        try {
            SensorPrivacyManager sensorPrivacyManager = this.mSensorPrivacyManager;
            if (sensorPrivacyManager != null && sensorPrivacyManager.isSensorPrivacyEnabled(1, 2)) {
                onError(1, 0);
                this.mCallback.onClientFinished(this, false);
            } else {
                FaceProvider.getExtImpl().scheduleAuthenticate();
                ICancellationSignal startHalOperation = this.mFaceAuthenticationClientExt.startHalOperation();
                this.mCancellationSignal = startHalOperation;
                if (startHalOperation != null) {
                } else {
                    this.mCancellationSignal = doAuthenticate();
                }
            }
        } catch (Exception e) {
            Slog.e(TAG, "Remote exception when requesting auth", e);
            onError(1, 0);
            this.mCallback.onClientFinished(this, false);
        }
    }

    private ICancellationSignal doAuthenticate() throws RemoteException {
        final AidlSession freshDaemon = getFreshDaemon();
        if (freshDaemon.hasContextMethods()) {
            OperationContextExt operationContext = getOperationContext();
            ICancellationSignal authenticateWithContext = freshDaemon.getSession().authenticateWithContext(this.mOperationId, operationContext.toAidlContext(getOptions()));
            getBiometricContext().subscribe(operationContext, new Consumer() { // from class: com.android.server.biometrics.sensors.face.aidl.FaceAuthenticationClient$$ExternalSyntheticLambda0
                @Override // java.util.function.Consumer
                public final void accept(Object obj) {
                    FaceAuthenticationClient.lambda$doAuthenticate$0(AidlSession.this, (OperationContext) obj);
                }
            });
            return authenticateWithContext;
        }
        return freshDaemon.getSession().authenticate(this.mOperationId);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$doAuthenticate$0(AidlSession aidlSession, OperationContext operationContext) {
        try {
            aidlSession.getSession().onContextChanged(operationContext);
        } catch (RemoteException e) {
            Slog.e(TAG, "Unable to notify context changed", e);
        }
    }

    @Override // com.android.server.biometrics.sensors.AcquisitionClient
    protected void stopHalOperation() {
        unsubscribeBiometricContext();
        if (this.mCancellationSignal != null) {
            try {
                if (this.mFaceAuthenticationClientExt.stopHalOperation()) {
                    return;
                }
                this.mCancellationSignal.cancel();
            } catch (RemoteException e) {
                Slog.e(TAG, "Remote exception when requesting cancel", e);
                onError(1, 0);
                this.mCallback.onClientFinished(this, false);
            }
        }
    }

    @Override // com.android.server.biometrics.sensors.AuthenticationClient
    public boolean wasUserDetected() {
        int i = this.mLastAcquire;
        return (i == 11 || i == 21 || i == 23) ? false : true;
    }

    @Override // com.android.server.biometrics.sensors.AuthenticationClient
    protected void handleLifecycleAfterAuth(boolean z) {
        this.mCallback.onClientFinished(this, true);
    }

    @Override // com.android.server.biometrics.sensors.AuthenticationClient, com.android.server.biometrics.sensors.AuthenticationConsumer
    public void onAuthenticated(BiometricAuthenticator.Identifier identifier, boolean z, ArrayList<Byte> arrayList) {
        super.onAuthenticated(identifier, z, arrayList);
        this.mState = 4;
        this.mUsageStats.addEvent(new UsageStats.AuthenticationEvent(getStartTimeMs(), System.currentTimeMillis() - getStartTimeMs(), z, 0, 0, getTargetUserId()));
        this.mCallback.onClientFinished(this, true);
    }

    @Override // com.android.server.biometrics.sensors.AuthenticationClient, com.android.server.biometrics.sensors.AcquisitionClient, com.android.server.biometrics.sensors.ErrorConsumer
    public void onError(int i, int i2) {
        this.mUsageStats.addEvent(new UsageStats.AuthenticationEvent(getStartTimeMs(), System.currentTimeMillis() - getStartTimeMs(), false, i, i2, getTargetUserId()));
        if (i == 16) {
            BiometricNotificationUtils.showReEnrollmentNotification(getContext());
        }
        super.onError(i, i2);
    }

    private int[] getAcquireIgnorelist() {
        return isBiometricPrompt() ? this.mBiometricPromptIgnoreList : this.mKeyguardIgnoreList;
    }

    private int[] getAcquireVendorIgnorelist() {
        return isBiometricPrompt() ? this.mBiometricPromptIgnoreListVendor : this.mKeyguardIgnoreListVendor;
    }

    private boolean shouldSendAcquiredMessage(int i, int i2) {
        if (i == 22) {
            if (!Utils.listContains(getAcquireVendorIgnorelist(), i2)) {
                return true;
            }
        } else if (!Utils.listContains(getAcquireIgnorelist(), i)) {
            return true;
        }
        return false;
    }

    @Override // com.android.server.biometrics.sensors.AuthenticationClient, com.android.server.biometrics.sensors.AcquisitionClient
    public void onAcquired(int i, int i2) {
        this.mLastAcquire = i;
        onAcquiredInternal(i, i2, shouldSendAcquiredMessage(i, i2));
        PerformanceTracker.getInstanceForSensorId(getSensorId()).incrementAcquireForUser(getTargetUserId(), isCryptoOperation());
    }

    public void onAuthenticationFrame(FaceAuthenticationFrame faceAuthenticationFrame) {
        int acquiredInfo = faceAuthenticationFrame.getData().getAcquiredInfo();
        int vendorCode = faceAuthenticationFrame.getData().getVendorCode();
        this.mLastAcquire = acquiredInfo;
        onAcquiredInternal(acquiredInfo, vendorCode, false);
        if (!shouldSendAcquiredMessage(acquiredInfo, vendorCode) || getListener() == null) {
            return;
        }
        try {
            getListener().onAuthenticationFrame(faceAuthenticationFrame);
        } catch (RemoteException e) {
            Slog.w(TAG, "Failed to send authentication frame", e);
            this.mCallback.onClientFinished(this, false);
        }
    }

    @Override // com.android.server.biometrics.sensors.LockoutConsumer
    public void onLockoutTimed(long j) {
        this.mAuthSessionCoordinator.lockOutTimed(getTargetUserId(), getSensorStrength(), getSensorId(), j, getRequestId());
        getLogger().logOnError(getContext(), getOperationContext(), 7, 0, getTargetUserId());
        PerformanceTracker.getInstanceForSensorId(getSensorId()).incrementTimedLockoutForUser(getTargetUserId());
        onError(7, 0);
    }

    @Override // com.android.server.biometrics.sensors.LockoutConsumer
    public void onLockoutPermanent() {
        this.mAuthSessionCoordinator.lockedOutFor(getTargetUserId(), getSensorStrength(), getSensorId(), getRequestId());
        getLogger().logOnError(getContext(), getOperationContext(), 9, 0, getTargetUserId());
        PerformanceTracker.getInstanceForSensorId(getSensorId()).incrementPermanentLockoutForUser(getTargetUserId());
        onError(9, 0);
    }
}
