package com.android.server.biometrics.sensors.fingerprint.aidl;

import android.annotation.EnforcePermission;
import android.content.Context;
import android.hardware.biometrics.ITestSession;
import android.hardware.biometrics.ITestSessionCallback;
import android.hardware.fingerprint.Fingerprint;
import android.hardware.fingerprint.IFingerprintServiceReceiver;
import android.os.Binder;
import android.os.RemoteException;
import android.util.Slog;
import com.android.server.biometrics.HardwareAuthTokenUtils;
import com.android.server.biometrics.sensors.BaseClientMonitor;
import com.android.server.biometrics.sensors.BiometricStateCallback;
import com.android.server.biometrics.sensors.ClientMonitorCallback;
import com.android.server.biometrics.sensors.fingerprint.FingerprintUtils;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public class BiometricTestSessionImpl extends ITestSession.Stub {
    private static final String TAG = "fp/aidl/BiometricTestSessionImpl";
    private final BiometricStateCallback mBiometricStateCallback;
    private final ITestSessionCallback mCallback;
    private final Context mContext;
    private final FingerprintProvider mProvider;
    private final Sensor mSensor;
    private final int mSensorId;
    private final IFingerprintServiceReceiver mReceiver = new IFingerprintServiceReceiver.Stub() { // from class: com.android.server.biometrics.sensors.fingerprint.aidl.BiometricTestSessionImpl.1
        public void onAcquired(int i, int i2) {
        }

        public void onAuthenticationFailed() {
        }

        public void onAuthenticationSucceeded(Fingerprint fingerprint, int i, boolean z) {
        }

        public void onChallengeGenerated(int i, int i2, long j) {
        }

        public void onEnrollResult(Fingerprint fingerprint, int i) {
        }

        public void onError(int i, int i2) {
        }

        public void onFingerprintDetected(int i, int i2, boolean z) {
        }

        public void onRemoved(Fingerprint fingerprint, int i) {
        }

        public void onUdfpsPointerDown(int i) {
        }

        public void onUdfpsPointerUp(int i) {
        }
    };
    private final Set<Integer> mEnrollmentIds = new HashSet();
    private final Random mRandom = new Random();

    /* JADX INFO: Access modifiers changed from: package-private */
    public BiometricTestSessionImpl(Context context, int i, ITestSessionCallback iTestSessionCallback, BiometricStateCallback biometricStateCallback, FingerprintProvider fingerprintProvider, Sensor sensor) {
        this.mContext = context;
        this.mSensorId = i;
        this.mCallback = iTestSessionCallback;
        this.mBiometricStateCallback = biometricStateCallback;
        this.mProvider = fingerprintProvider;
        this.mSensor = sensor;
    }

    @EnforcePermission("android.permission.TEST_BIOMETRIC")
    public void setTestHalEnabled(boolean z) {
        super.setTestHalEnabled_enforcePermission();
        this.mProvider.setTestHalEnabled(z);
        this.mSensor.setTestHalEnabled(z);
    }

    @EnforcePermission("android.permission.TEST_BIOMETRIC")
    public void startEnroll(int i) {
        super.startEnroll_enforcePermission();
        this.mProvider.scheduleEnroll(this.mSensorId, new Binder(), new byte[69], i, this.mReceiver, this.mContext.getOpPackageName(), 2);
    }

    @EnforcePermission("android.permission.TEST_BIOMETRIC")
    public void finishEnroll(int i) {
        super.finishEnroll_enforcePermission();
        int nextInt = this.mRandom.nextInt();
        while (this.mEnrollmentIds.contains(Integer.valueOf(nextInt))) {
            nextInt = this.mRandom.nextInt();
        }
        this.mEnrollmentIds.add(Integer.valueOf(nextInt));
        this.mSensor.getSessionForUser(i).getHalSessionCallback().onEnrollmentProgress(nextInt, 0);
    }

    @EnforcePermission("android.permission.TEST_BIOMETRIC")
    public void acceptAuthentication(int i) {
        super.acceptAuthentication_enforcePermission();
        List<Fingerprint> biometricsForUser = FingerprintUtils.getInstance(this.mSensorId).getBiometricsForUser(this.mContext, i);
        if (biometricsForUser.isEmpty()) {
            Slog.w(TAG, "No fingerprints, returning");
        } else {
            this.mSensor.getSessionForUser(i).getHalSessionCallback().onAuthenticationSucceeded(biometricsForUser.get(0).getBiometricId(), HardwareAuthTokenUtils.toHardwareAuthToken(new byte[69]));
        }
    }

    @EnforcePermission("android.permission.TEST_BIOMETRIC")
    public void rejectAuthentication(int i) {
        super.rejectAuthentication_enforcePermission();
        this.mSensor.getSessionForUser(i).getHalSessionCallback().onAuthenticationFailed();
    }

    @EnforcePermission("android.permission.TEST_BIOMETRIC")
    public void notifyAcquired(int i, int i2) {
        super.notifyAcquired_enforcePermission();
        this.mSensor.getSessionForUser(i).getHalSessionCallback().onAcquired((byte) i2, 0);
    }

    @EnforcePermission("android.permission.TEST_BIOMETRIC")
    public void notifyError(int i, int i2) {
        super.notifyError_enforcePermission();
        this.mSensor.getSessionForUser(i).getHalSessionCallback().onError((byte) i2, 0);
    }

    @EnforcePermission("android.permission.TEST_BIOMETRIC")
    public void cleanupInternalState(int i) {
        super.cleanupInternalState_enforcePermission();
        Slog.d(TAG, "cleanupInternalState: " + i);
        this.mProvider.scheduleInternalCleanup(this.mSensorId, i, new ClientMonitorCallback() { // from class: com.android.server.biometrics.sensors.fingerprint.aidl.BiometricTestSessionImpl.2
            @Override // com.android.server.biometrics.sensors.ClientMonitorCallback
            public void onClientStarted(BaseClientMonitor baseClientMonitor) {
                try {
                    Slog.d(BiometricTestSessionImpl.TAG, "onClientStarted: " + baseClientMonitor);
                    BiometricTestSessionImpl.this.mCallback.onCleanupStarted(baseClientMonitor.getTargetUserId());
                } catch (RemoteException e) {
                    Slog.e(BiometricTestSessionImpl.TAG, "Remote exception", e);
                }
            }

            @Override // com.android.server.biometrics.sensors.ClientMonitorCallback
            public void onClientFinished(BaseClientMonitor baseClientMonitor, boolean z) {
                try {
                    Slog.d(BiometricTestSessionImpl.TAG, "onClientFinished: " + baseClientMonitor);
                    BiometricTestSessionImpl.this.mCallback.onCleanupFinished(baseClientMonitor.getTargetUserId());
                } catch (RemoteException e) {
                    Slog.e(BiometricTestSessionImpl.TAG, "Remote exception", e);
                }
            }
        });
    }
}
