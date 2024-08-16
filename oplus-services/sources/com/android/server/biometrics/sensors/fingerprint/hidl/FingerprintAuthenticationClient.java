package com.android.server.biometrics.sensors.fingerprint.hidl;

import android.app.TaskStackListener;
import android.content.Context;
import android.hardware.biometrics.BiometricAuthenticator;
import android.hardware.biometrics.BiometricManager;
import android.hardware.biometrics.fingerprint.PointerContext;
import android.hardware.biometrics.fingerprint.V2_1.IBiometricsFingerprint;
import android.hardware.fingerprint.FingerprintAuthenticateOptions;
import android.hardware.fingerprint.FingerprintSensorPropertiesInternal;
import android.hardware.fingerprint.ISidefpsController;
import android.hardware.fingerprint.IUdfpsOverlay;
import android.hardware.fingerprint.IUdfpsOverlayController;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Slog;
import com.android.server.biometrics.log.BiometricContext;
import com.android.server.biometrics.log.BiometricLogger;
import com.android.server.biometrics.log.CallbackWithProbe;
import com.android.server.biometrics.log.Probe;
import com.android.server.biometrics.sensors.AuthenticationClient;
import com.android.server.biometrics.sensors.BiometricNotificationUtils;
import com.android.server.biometrics.sensors.ClientMonitorCallback;
import com.android.server.biometrics.sensors.ClientMonitorCallbackConverter;
import com.android.server.biometrics.sensors.ClientMonitorCompositeCallback;
import com.android.server.biometrics.sensors.PerformanceTracker;
import com.android.server.biometrics.sensors.SensorOverlays;
import com.android.server.biometrics.sensors.fingerprint.Udfps;
import com.android.server.biometrics.sensors.fingerprint.UdfpsHelper;
import java.util.ArrayList;
import java.util.function.Supplier;
import system.ext.loader.core.ExtLoader;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public class FingerprintAuthenticationClient extends AuthenticationClient<IBiometricsFingerprint, FingerprintAuthenticateOptions> implements Udfps {
    private static final String TAG = "Biometrics/FingerprintAuthClient";
    private final CallbackWithProbe<Probe> mALSProbeCallback;
    public IFingerprintAuthenticationClientExt mFingerprintAuthenticationClientExt;
    private boolean mIsPointerDown;
    private final LockoutFrameworkImpl mLockoutFrameworkImpl;
    private final SensorOverlays mSensorOverlays;
    private final FingerprintSensorPropertiesInternal mSensorProps;

    @Override // com.android.server.biometrics.sensors.fingerprint.Udfps
    public void onUiReady() {
    }

    @Override // com.android.server.biometrics.sensors.AuthenticationClient
    public boolean wasUserDetected() {
        return false;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public FingerprintAuthenticationClient(Context context, Supplier<IBiometricsFingerprint> supplier, IBinder iBinder, long j, ClientMonitorCallbackConverter clientMonitorCallbackConverter, long j2, boolean z, FingerprintAuthenticateOptions fingerprintAuthenticateOptions, int i, boolean z2, BiometricLogger biometricLogger, BiometricContext biometricContext, boolean z3, TaskStackListener taskStackListener, LockoutFrameworkImpl lockoutFrameworkImpl, IUdfpsOverlayController iUdfpsOverlayController, ISidefpsController iSidefpsController, IUdfpsOverlay iUdfpsOverlay, boolean z4, FingerprintSensorPropertiesInternal fingerprintSensorPropertiesInternal, @BiometricManager.Authenticators.Types int i2) {
        super(context, supplier, iBinder, clientMonitorCallbackConverter, j2, z, fingerprintAuthenticateOptions, i, z2, biometricLogger, biometricContext, z3, taskStackListener, lockoutFrameworkImpl, z4, true, i2);
        this.mFingerprintAuthenticationClientExt = (IFingerprintAuthenticationClientExt) ExtLoader.type(IFingerprintAuthenticationClientExt.class).base(this).create();
        setRequestId(j);
        this.mLockoutFrameworkImpl = lockoutFrameworkImpl;
        this.mSensorOverlays = new SensorOverlays(iUdfpsOverlayController, iSidefpsController, iUdfpsOverlay);
        this.mFingerprintAuthenticationClientExt.init(context, supplier, fingerprintAuthenticateOptions.getUserId(), j2, fingerprintAuthenticateOptions.getOpPackageName());
        this.mSensorProps = fingerprintSensorPropertiesInternal;
        this.mALSProbeCallback = getLogger().getAmbientLightProbe(false);
    }

    @Override // com.android.server.biometrics.sensors.AuthenticationClient, com.android.server.biometrics.sensors.BaseClientMonitor
    public void start(ClientMonitorCallback clientMonitorCallback) {
        super.start(clientMonitorCallback);
        if (this.mSensorProps.isAnyUdfpsType()) {
            this.mState = 2;
        } else {
            this.mState = 1;
        }
    }

    @Override // com.android.server.biometrics.sensors.BaseClientMonitor
    protected ClientMonitorCallback wrapCallbackForStart(ClientMonitorCallback clientMonitorCallback) {
        return new ClientMonitorCompositeCallback(this.mALSProbeCallback, clientMonitorCallback);
    }

    @Override // com.android.server.biometrics.sensors.AuthenticationClient, com.android.server.biometrics.sensors.AuthenticationConsumer
    public void onAuthenticated(BiometricAuthenticator.Identifier identifier, boolean z, ArrayList<Byte> arrayList) {
        super.onAuthenticated(identifier, z, arrayList);
        if (z) {
            this.mState = 4;
            resetFailedAttempts(getTargetUserId());
            this.mSensorOverlays.hide(getSensorId());
            return;
        }
        this.mState = 3;
        int lockoutModeForUser = this.mLockoutFrameworkImpl.getLockoutModeForUser(getTargetUserId());
        if (lockoutModeForUser != 0) {
            Slog.w(TAG, "Fingerprint locked out, lockoutMode(" + lockoutModeForUser + ")");
            int i = lockoutModeForUser == 1 ? 7 : 9;
            this.mSensorOverlays.hide(getSensorId());
            onErrorInternal(i, 0, false);
            cancel();
        }
    }

    @Override // com.android.server.biometrics.sensors.AuthenticationClient, com.android.server.biometrics.sensors.AcquisitionClient, com.android.server.biometrics.sensors.ErrorConsumer
    public void onError(int i, int i2) {
        super.onError(i, i2);
        if (i == 18) {
            BiometricNotificationUtils.showBadCalibrationNotification(getContext());
        }
        this.mSensorOverlays.hide(getSensorId());
    }

    private void resetFailedAttempts(int i) {
        this.mLockoutFrameworkImpl.resetFailedAttemptsForUser(true, i);
    }

    @Override // com.android.server.biometrics.sensors.AuthenticationClient
    protected void handleLifecycleAfterAuth(boolean z) {
        if (z) {
            this.mCallback.onClientFinished(this, true);
        }
    }

    @Override // com.android.server.biometrics.sensors.AuthenticationClient, com.android.server.biometrics.sensors.AcquisitionClient
    public void onAcquired(int i, int i2) {
        super.onAcquired(i, i2);
        if (getLockoutTracker().getLockoutModeForUser(getTargetUserId()) == 0) {
            PerformanceTracker.getInstanceForSensorId(getSensorId()).incrementAcquireForUser(getTargetUserId(), isCryptoOperation());
        }
    }

    @Override // com.android.server.biometrics.sensors.AuthenticationClient
    public int handleFailedAttempt(int i) {
        if (this.mFingerprintAuthenticationClientExt.isMistakeTouchMode()) {
            return 0;
        }
        this.mLockoutFrameworkImpl.addFailedAttemptForUser(i);
        int lockoutModeForUser = getLockoutTracker().getLockoutModeForUser(i);
        PerformanceTracker instanceForSensorId = PerformanceTracker.getInstanceForSensorId(getSensorId());
        if (lockoutModeForUser == 2) {
            instanceForSensorId.incrementPermanentLockoutForUser(i);
        } else if (lockoutModeForUser == 1) {
            instanceForSensorId.incrementTimedLockoutForUser(i);
        }
        this.mFingerprintAuthenticationClientExt.onHandleFailedAttempt(this.mLockoutFrameworkImpl, i);
        return lockoutModeForUser;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.android.server.biometrics.sensors.HalClientMonitor
    public void startHalOperation() {
        this.mSensorOverlays.show(getSensorId(), getShowOverlayReason(), this);
        try {
            if (this.mFingerprintAuthenticationClientExt.startHalOperation()) {
                return;
            }
            getFreshDaemon().authenticate(this.mOperationId, getTargetUserId());
        } catch (RemoteException e) {
            Slog.e(TAG, "Remote exception when requesting auth", e);
            onError(1, 0);
            this.mSensorOverlays.hide(getSensorId());
            this.mCallback.onClientFinished(this, false);
        }
    }

    @Override // com.android.server.biometrics.sensors.AcquisitionClient
    protected void stopHalOperation() {
        this.mSensorOverlays.hide(getSensorId());
        try {
            getFreshDaemon().cancel();
        } catch (RemoteException e) {
            Slog.e(TAG, "Remote exception when requesting cancel", e);
            onError(1, 0);
            this.mCallback.onClientFinished(this, false);
        }
    }

    @Override // com.android.server.biometrics.sensors.fingerprint.Udfps
    public void onPointerDown(PointerContext pointerContext) {
        this.mIsPointerDown = true;
        this.mState = 1;
        this.mALSProbeCallback.getProbe().enable();
        UdfpsHelper.onFingerDown(getFreshDaemon(), (int) pointerContext.x, (int) pointerContext.y, pointerContext.minor, pointerContext.major);
        if (getListener() != null) {
            try {
                getListener().onUdfpsPointerDown(getSensorId());
            } catch (RemoteException e) {
                Slog.e(TAG, "Remote exception", e);
            }
        }
    }

    @Override // com.android.server.biometrics.sensors.fingerprint.Udfps
    public void onPointerUp(PointerContext pointerContext) {
        this.mIsPointerDown = false;
        this.mState = 3;
        this.mALSProbeCallback.getProbe().disable();
        UdfpsHelper.onFingerUp(getFreshDaemon());
        if (getListener() != null) {
            try {
                getListener().onUdfpsPointerUp(getSensorId());
            } catch (RemoteException e) {
                Slog.e(TAG, "Remote exception", e);
            }
        }
    }

    @Override // com.android.server.biometrics.sensors.fingerprint.Udfps
    public boolean isPointerDown() {
        return this.mIsPointerDown;
    }
}
