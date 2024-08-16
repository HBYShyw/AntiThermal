package com.android.server.biometrics.sensors.fingerprint.aidl;

import android.R;
import android.app.TaskStackListener;
import android.content.Context;
import android.hardware.biometrics.BiometricAuthenticator;
import android.hardware.biometrics.BiometricManager;
import android.hardware.biometrics.common.ICancellationSignal;
import android.hardware.biometrics.common.OperationContext;
import android.hardware.biometrics.fingerprint.PointerContext;
import android.hardware.fingerprint.FingerprintAuthenticateOptions;
import android.hardware.fingerprint.FingerprintSensorPropertiesInternal;
import android.hardware.fingerprint.ISidefpsController;
import android.hardware.fingerprint.IUdfpsOverlay;
import android.hardware.fingerprint.IUdfpsOverlayController;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.RemoteException;
import android.provider.Settings;
import android.util.Slog;
import com.android.server.biometrics.log.BiometricContext;
import com.android.server.biometrics.log.BiometricLogger;
import com.android.server.biometrics.log.CallbackWithProbe;
import com.android.server.biometrics.log.OperationContextExt;
import com.android.server.biometrics.log.Probe;
import com.android.server.biometrics.sensors.AuthSessionCoordinator;
import com.android.server.biometrics.sensors.AuthenticationClient;
import com.android.server.biometrics.sensors.BiometricNotificationUtils;
import com.android.server.biometrics.sensors.ClientMonitorCallback;
import com.android.server.biometrics.sensors.ClientMonitorCallbackConverter;
import com.android.server.biometrics.sensors.ClientMonitorCompositeCallback;
import com.android.server.biometrics.sensors.LockoutCache;
import com.android.server.biometrics.sensors.LockoutConsumer;
import com.android.server.biometrics.sensors.PerformanceTracker;
import com.android.server.biometrics.sensors.SensorOverlays;
import com.android.server.biometrics.sensors.fingerprint.PowerPressHandler;
import com.android.server.biometrics.sensors.fingerprint.Udfps;
import java.time.Clock;
import java.util.ArrayList;
import java.util.function.Consumer;
import java.util.function.Supplier;
import system.ext.loader.core.ExtLoader;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public class FingerprintAuthenticationClient extends AuthenticationClient<AidlSession, FingerprintAuthenticateOptions> implements Udfps, LockoutConsumer, PowerPressHandler {
    private static final int MESSAGE_AUTH_SUCCESS = 2;
    private static final int MESSAGE_FINGER_UP = 3;
    private static final String TAG = "FingerprintAuthenticationClient";
    private final CallbackWithProbe<Probe> mALSProbeCallback;
    private final AuthSessionCoordinator mAuthSessionCoordinator;
    private Runnable mAuthSuccessRunnable;
    private ICancellationSignal mCancellationSignal;
    private final Clock mClock;
    private final long mFingerUpIgnoresPower;
    public IFingerprintAuthenticationClientAidlExt mFingerprintAuthenticationClientAidlExt;
    private final Handler mHandler;
    private long mIgnoreAuthFor;
    private boolean mIsPointerDown;
    private final SensorOverlays mSensorOverlays;
    private final FingerprintSensorPropertiesInternal mSensorProps;
    private long mSideFpsLastAcquireStartTime;
    private final int mSkipWaitForPowerAcquireMessage;
    private final int mSkipWaitForPowerVendorAcquireMessage;
    private long mWaitForAuthBp;
    private long mWaitForAuthKeyguard;

    @Override // com.android.server.biometrics.sensors.fingerprint.PowerPressHandler
    public void onPowerPressed() {
    }

    @Override // com.android.server.biometrics.sensors.AuthenticationClient
    public boolean wasUserDetected() {
        return false;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public FingerprintAuthenticationClient(Context context, Supplier<AidlSession> supplier, IBinder iBinder, long j, ClientMonitorCallbackConverter clientMonitorCallbackConverter, long j2, boolean z, FingerprintAuthenticateOptions fingerprintAuthenticateOptions, int i, boolean z2, BiometricLogger biometricLogger, BiometricContext biometricContext, boolean z3, TaskStackListener taskStackListener, LockoutCache lockoutCache, IUdfpsOverlayController iUdfpsOverlayController, ISidefpsController iSidefpsController, IUdfpsOverlay iUdfpsOverlay, boolean z4, FingerprintSensorPropertiesInternal fingerprintSensorPropertiesInternal, Handler handler, @BiometricManager.Authenticators.Types int i2, Clock clock) {
        super(context, supplier, iBinder, clientMonitorCallbackConverter, j2, z, fingerprintAuthenticateOptions, i, z2, biometricLogger, biometricContext, z3, taskStackListener, null, z4, true, i2);
        this.mFingerUpIgnoresPower = 500L;
        this.mFingerprintAuthenticationClientAidlExt = (IFingerprintAuthenticationClientAidlExt) ExtLoader.type(IFingerprintAuthenticationClientAidlExt.class).base(this).create();
        setRequestId(j);
        this.mSensorOverlays = new SensorOverlays(iUdfpsOverlayController, iSidefpsController, iUdfpsOverlay);
        this.mSensorProps = fingerprintSensorPropertiesInternal;
        this.mALSProbeCallback = getLogger().getAmbientLightProbe(false);
        this.mHandler = handler;
        this.mWaitForAuthKeyguard = context.getResources().getInteger(R.integer.kg_security_flipper_weight);
        this.mWaitForAuthBp = context.getResources().getInteger(R.integer.kg_glowpad_rotation_offset);
        this.mIgnoreAuthFor = context.getResources().getInteger(R.integer.kg_selector_gravity);
        this.mSkipWaitForPowerAcquireMessage = context.getResources().getInteger(R.integer.kg_widget_region_weight);
        this.mSkipWaitForPowerVendorAcquireMessage = context.getResources().getInteger(R.integer.leanback_setup_alpha_activity_in_bkg_delay);
        this.mAuthSessionCoordinator = biometricContext.getAuthSessionCoordinator();
        this.mSideFpsLastAcquireStartTime = -1L;
        this.mClock = clock;
        if (fingerprintSensorPropertiesInternal.isAnySidefpsType() && Build.isDebuggable()) {
            this.mWaitForAuthKeyguard = Settings.Secure.getIntForUser(context.getContentResolver(), "fingerprint_side_fps_kg_power_window", (int) this.mWaitForAuthKeyguard, -2);
            this.mWaitForAuthBp = Settings.Secure.getIntForUser(context.getContentResolver(), "fingerprint_side_fps_bp_power_window", (int) this.mWaitForAuthBp, -2);
            this.mIgnoreAuthFor = Settings.Secure.getIntForUser(context.getContentResolver(), "fingerprint_side_fps_auth_downtime", (int) this.mIgnoreAuthFor, -2);
        }
        this.mFingerprintAuthenticationClientAidlExt.init(context, supplier, fingerprintAuthenticateOptions.getUserId(), j2, fingerprintAuthenticateOptions.getOpPackageName());
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
        return new ClientMonitorCompositeCallback(this.mALSProbeCallback, getBiometricContextUnsubscriber(), clientMonitorCallback);
    }

    @Override // com.android.server.biometrics.sensors.AuthenticationClient
    protected void handleLifecycleAfterAuth(boolean z) {
        if (z) {
            this.mCallback.onClientFinished(this, true);
        }
    }

    @Override // com.android.server.biometrics.sensors.AuthenticationClient, com.android.server.biometrics.sensors.AuthenticationConsumer
    public void onAuthenticated(BiometricAuthenticator.Identifier identifier, boolean z, ArrayList<Byte> arrayList) {
        super.onAuthenticated(identifier, z, arrayList);
        if (z) {
            this.mState = 4;
            this.mSensorOverlays.hide(getSensorId());
        } else {
            this.mState = 3;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onAcquired$0(int i, IUdfpsOverlayController iUdfpsOverlayController) throws RemoteException {
        iUdfpsOverlayController.onAcquired(getSensorId(), i);
    }

    @Override // com.android.server.biometrics.sensors.AuthenticationClient, com.android.server.biometrics.sensors.AcquisitionClient
    public void onAcquired(final int i, int i2) {
        this.mSensorOverlays.ifUdfps(new SensorOverlays.OverlayControllerConsumer() { // from class: com.android.server.biometrics.sensors.fingerprint.aidl.FingerprintAuthenticationClient$$ExternalSyntheticLambda0
            @Override // com.android.server.biometrics.sensors.SensorOverlays.OverlayControllerConsumer
            public final void accept(Object obj) {
                FingerprintAuthenticationClient.this.lambda$onAcquired$0(i, (IUdfpsOverlayController) obj);
            }
        });
        super.onAcquired(i, i2);
        PerformanceTracker.getInstanceForSensorId(getSensorId()).incrementAcquireForUser(getTargetUserId(), isCryptoOperation());
    }

    @Override // com.android.server.biometrics.sensors.AuthenticationClient, com.android.server.biometrics.sensors.AcquisitionClient, com.android.server.biometrics.sensors.ErrorConsumer
    public void onError(int i, int i2) {
        super.onError(i, i2);
        if (i == 18) {
            BiometricNotificationUtils.showBadCalibrationNotification(getContext());
        }
        this.mSensorOverlays.hide(getSensorId());
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.android.server.biometrics.sensors.HalClientMonitor
    public void startHalOperation() {
        this.mSensorOverlays.show(getSensorId(), getShowOverlayReason(), this);
        try {
            IFingerprintAuthenticationClientAidlExt iFingerprintAuthenticationClientAidlExt = this.mFingerprintAuthenticationClientAidlExt;
            if (iFingerprintAuthenticationClientAidlExt != null) {
                ICancellationSignal startHalOperation = iFingerprintAuthenticationClientAidlExt.startHalOperation();
                this.mCancellationSignal = startHalOperation;
                if (startHalOperation != null) {
                    return;
                }
            }
            this.mCancellationSignal = doAuthenticate();
        } catch (RemoteException e) {
            Slog.e(TAG, "Remote exception", e);
            onError(1, 0);
            this.mSensorOverlays.hide(getSensorId());
            this.mCallback.onClientFinished(this, false);
        }
    }

    private ICancellationSignal doAuthenticate() throws RemoteException {
        ICancellationSignal authenticate;
        final AidlSession freshDaemon = getFreshDaemon();
        OperationContextExt operationContext = getOperationContext();
        if (freshDaemon.hasContextMethods()) {
            authenticate = freshDaemon.getSession().authenticateWithContext(this.mOperationId, operationContext.toAidlContext(getOptions()));
        } else {
            authenticate = freshDaemon.getSession().authenticate(this.mOperationId);
        }
        getBiometricContext().subscribe(operationContext, new Consumer() { // from class: com.android.server.biometrics.sensors.fingerprint.aidl.FingerprintAuthenticationClient$$ExternalSyntheticLambda1
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                FingerprintAuthenticationClient.this.lambda$doAuthenticate$1(freshDaemon, (OperationContext) obj);
            }
        });
        if (getBiometricContext().isAwake()) {
            this.mALSProbeCallback.getProbe().enable();
        }
        return authenticate;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$doAuthenticate$1(AidlSession aidlSession, OperationContext operationContext) {
        if (aidlSession.hasContextMethods()) {
            try {
                aidlSession.getSession().onContextChanged(operationContext);
            } catch (RemoteException e) {
                Slog.e(TAG, "Unable to notify context changed", e);
            }
        }
        if (getBiometricContext().isAwake()) {
            this.mALSProbeCallback.getProbe().enable();
        } else {
            this.mALSProbeCallback.getProbe().disable();
        }
    }

    @Override // com.android.server.biometrics.sensors.AcquisitionClient
    protected void stopHalOperation() {
        this.mSensorOverlays.hide(getSensorId());
        unsubscribeBiometricContext();
        ICancellationSignal iCancellationSignal = this.mCancellationSignal;
        if (iCancellationSignal != null) {
            try {
                iCancellationSignal.cancel();
                return;
            } catch (RemoteException e) {
                Slog.e(TAG, "Remote exception", e);
                onError(1, 0);
                this.mCallback.onClientFinished(this, false);
                return;
            }
        }
        Slog.e(TAG, "cancellation signal was null");
    }

    @Override // com.android.server.biometrics.sensors.fingerprint.Udfps
    public void onPointerDown(PointerContext pointerContext) {
        try {
            this.mIsPointerDown = true;
            this.mState = 1;
            AidlSession freshDaemon = getFreshDaemon();
            if (freshDaemon.hasContextMethods()) {
                freshDaemon.getSession().onPointerDownWithContext(pointerContext);
            } else {
                freshDaemon.getSession().onPointerDown(pointerContext.pointerId, (int) pointerContext.x, (int) pointerContext.y, pointerContext.minor, pointerContext.major);
            }
            if (getListener() != null) {
                getListener().onUdfpsPointerDown(getSensorId());
            }
        } catch (RemoteException e) {
            Slog.e(TAG, "Remote exception", e);
        }
    }

    @Override // com.android.server.biometrics.sensors.fingerprint.Udfps
    public void onPointerUp(PointerContext pointerContext) {
        try {
            this.mIsPointerDown = false;
            this.mState = 3;
            AidlSession freshDaemon = getFreshDaemon();
            if (freshDaemon.hasContextMethods()) {
                freshDaemon.getSession().onPointerUpWithContext(pointerContext);
            } else {
                freshDaemon.getSession().onPointerUp(pointerContext.pointerId);
            }
            if (getListener() != null) {
                getListener().onUdfpsPointerUp(getSensorId());
            }
        } catch (RemoteException e) {
            Slog.e(TAG, "Remote exception", e);
        }
    }

    @Override // com.android.server.biometrics.sensors.fingerprint.Udfps
    public boolean isPointerDown() {
        return this.mIsPointerDown;
    }

    @Override // com.android.server.biometrics.sensors.fingerprint.Udfps
    public void onUiReady() {
        try {
            getFreshDaemon().getSession().onUiReady();
        } catch (RemoteException e) {
            Slog.e(TAG, "Remote exception", e);
        }
    }

    @Override // com.android.server.biometrics.sensors.LockoutConsumer
    public void onLockoutTimed(long j) {
        this.mAuthSessionCoordinator.lockOutTimed(getTargetUserId(), getSensorStrength(), getSensorId(), j, getRequestId());
        getLogger().logOnError(getContext(), getOperationContext(), 7, 0, getTargetUserId());
        PerformanceTracker.getInstanceForSensorId(getSensorId()).incrementTimedLockoutForUser(getTargetUserId());
        try {
            getListener().onError(getSensorId(), getCookie(), 7, 0);
        } catch (RemoteException e) {
            Slog.e(TAG, "Remote exception", e);
        }
        this.mSensorOverlays.hide(getSensorId());
        this.mCallback.onClientFinished(this, false);
    }

    @Override // com.android.server.biometrics.sensors.LockoutConsumer
    public void onLockoutPermanent() {
        this.mAuthSessionCoordinator.lockedOutFor(getTargetUserId(), getSensorStrength(), getSensorId(), getRequestId());
        getLogger().logOnError(getContext(), getOperationContext(), 9, 0, getTargetUserId());
        PerformanceTracker.getInstanceForSensorId(getSensorId()).incrementPermanentLockoutForUser(getTargetUserId());
        try {
            getListener().onError(getSensorId(), getCookie(), 9, 0);
        } catch (RemoteException e) {
            Slog.e(TAG, "Remote exception", e);
        }
        this.mSensorOverlays.hide(getSensorId());
        this.mCallback.onClientFinished(this, false);
    }
}
