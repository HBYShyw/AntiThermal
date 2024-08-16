package com.android.server.locksettings;

import android.hardware.biometrics.BiometricManager;
import android.hardware.face.FaceManager;
import android.hardware.face.FaceSensorPropertiesInternal;
import android.hardware.fingerprint.FingerprintManager;
import android.hardware.fingerprint.FingerprintSensorPropertiesInternal;
import android.os.Handler;
import android.os.IBinder;
import android.os.ServiceManager;
import android.service.gatekeeper.IGateKeeperService;
import android.util.ArraySet;
import android.util.Slog;
import com.android.internal.widget.VerifyCredentialResponse;
import com.android.server.biometrics.sensors.face.IBiometricDeferredQueueExt;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import system.ext.loader.core.ExtLoader;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public class BiometricDeferredQueue {
    private static final String TAG = "BiometricDeferredQueue";
    private BiometricManager mBiometricManager;
    private FaceManager mFaceManager;
    private FaceResetLockoutTask mFaceResetLockoutTask;
    private FingerprintManager mFingerprintManager;
    private final Handler mHandler;
    private final SyntheticPasswordManager mSpManager;
    private IBiometricDeferredQueueExt mIBiometricDeferredQueueExt = (IBiometricDeferredQueueExt) ExtLoader.type(IBiometricDeferredQueueExt.class).base(this).create();
    private final FaceResetLockoutTask.FinishCallback mFaceFinishCallback = new FaceResetLockoutTask.FinishCallback() { // from class: com.android.server.locksettings.BiometricDeferredQueue$$ExternalSyntheticLambda0
        @Override // com.android.server.locksettings.BiometricDeferredQueue.FaceResetLockoutTask.FinishCallback
        public final void onFinished() {
            BiometricDeferredQueue.this.lambda$new$0();
        }
    };
    private final ArrayList<UserAuthInfo> mPendingResetLockoutsForFingerprint = new ArrayList<>();
    private final ArrayList<UserAuthInfo> mPendingResetLockoutsForFace = new ArrayList<>();
    private final ArrayList<UserAuthInfo> mPendingResetLockouts = new ArrayList<>();

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public static class UserAuthInfo {
        final byte[] gatekeeperPassword;
        final int userId;

        UserAuthInfo(int i, byte[] bArr) {
            this.userId = i;
            this.gatekeeperPassword = bArr;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public static class FaceResetLockoutTask implements FaceManager.GenerateChallengeCallback {
        FaceManager faceManager;
        FinishCallback finishCallback;
        List<UserAuthInfo> pendingResetLockuts;
        Set<Integer> sensorIds;
        SyntheticPasswordManager spManager;

        /* JADX INFO: Access modifiers changed from: package-private */
        /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
        public interface FinishCallback {
            void onFinished();
        }

        FaceResetLockoutTask(FinishCallback finishCallback, FaceManager faceManager, SyntheticPasswordManager syntheticPasswordManager, Set<Integer> set, List<UserAuthInfo> list) {
            this.finishCallback = finishCallback;
            this.faceManager = faceManager;
            this.spManager = syntheticPasswordManager;
            this.sensorIds = set;
            this.pendingResetLockuts = list;
        }

        public void onGenerateChallengeResult(int i, int i2, long j) {
            if (!this.sensorIds.contains(Integer.valueOf(i))) {
                Slog.e(BiometricDeferredQueue.TAG, "Unknown sensorId received: " + i);
                return;
            }
            for (UserAuthInfo userAuthInfo : this.pendingResetLockuts) {
                Slog.d(BiometricDeferredQueue.TAG, "Resetting face lockout for sensor: " + i + ", user: " + userAuthInfo.userId);
                byte[] requestHatFromGatekeeperPassword = BiometricDeferredQueue.requestHatFromGatekeeperPassword(this.spManager, userAuthInfo, j);
                if (requestHatFromGatekeeperPassword != null) {
                    this.faceManager.resetLockout(i, userAuthInfo.userId, requestHatFromGatekeeperPassword);
                }
            }
            this.sensorIds.remove(Integer.valueOf(i));
            this.faceManager.revokeChallenge(i, i2, j);
            if (this.sensorIds.isEmpty()) {
                Slog.d(BiometricDeferredQueue.TAG, "Done requesting resetLockout for all face sensors");
                this.finishCallback.onFinished();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$0() {
        this.mFaceResetLockoutTask = null;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public BiometricDeferredQueue(SyntheticPasswordManager syntheticPasswordManager, Handler handler) {
        this.mSpManager = syntheticPasswordManager;
        this.mHandler = handler;
    }

    public void systemReady(FingerprintManager fingerprintManager, FaceManager faceManager, BiometricManager biometricManager) {
        this.mFingerprintManager = fingerprintManager;
        this.mFaceManager = faceManager;
        this.mBiometricManager = biometricManager;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void addPendingLockoutResetForUser(final int i, final byte[] bArr) {
        this.mHandler.post(new Runnable() { // from class: com.android.server.locksettings.BiometricDeferredQueue$$ExternalSyntheticLambda1
            @Override // java.lang.Runnable
            public final void run() {
                BiometricDeferredQueue.this.lambda$addPendingLockoutResetForUser$1(i, bArr);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$addPendingLockoutResetForUser$1(int i, byte[] bArr) {
        FingerprintManager fingerprintManager = this.mFingerprintManager;
        if (fingerprintManager != null && fingerprintManager.hasEnrolledFingerprints(i)) {
            Slog.d(TAG, "Fingerprint addPendingLockoutResetForUser: " + i);
            this.mPendingResetLockoutsForFingerprint.add(new UserAuthInfo(i, bArr));
        }
        if (this.mBiometricManager != null) {
            Slog.d(TAG, "Fingerprint addPendingLockoutResetForUser: " + i);
            this.mPendingResetLockouts.add(new UserAuthInfo(i, bArr));
        }
        FaceManager faceManager = this.mFaceManager;
        if (faceManager != null && faceManager.hasEnrolledTemplates(i)) {
            Slog.d(TAG, "Face addPendingLockoutResetForUser: " + i);
            this.mPendingResetLockoutsForFace.add(new UserAuthInfo(i, bArr));
        }
        IBiometricDeferredQueueExt iBiometricDeferredQueueExt = this.mIBiometricDeferredQueueExt;
        if (iBiometricDeferredQueueExt == null || !iBiometricDeferredQueueExt.hasEnrolledPalms(i)) {
            return;
        }
        Slog.d(TAG, "Palm addPendingLockoutResetForUser: " + i);
        this.mPendingResetLockoutsForFace.add(new UserAuthInfo(i, bArr));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void processPendingLockoutResets() {
        this.mHandler.post(new Runnable() { // from class: com.android.server.locksettings.BiometricDeferredQueue$$ExternalSyntheticLambda2
            @Override // java.lang.Runnable
            public final void run() {
                BiometricDeferredQueue.this.lambda$processPendingLockoutResets$2();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$processPendingLockoutResets$2() {
        if (!this.mPendingResetLockoutsForFingerprint.isEmpty()) {
            Slog.d(TAG, "Processing pending resetLockout for fingerprint");
            processPendingLockoutsForFingerprint(new ArrayList(this.mPendingResetLockoutsForFingerprint));
            this.mPendingResetLockoutsForFingerprint.clear();
        }
        if (!this.mPendingResetLockouts.isEmpty()) {
            Slog.d(TAG, "Processing pending resetLockouts(Generic)");
            processPendingLockoutsGeneric(new ArrayList(this.mPendingResetLockouts));
            this.mPendingResetLockouts.clear();
        }
        if (this.mPendingResetLockoutsForFace.isEmpty()) {
            return;
        }
        Slog.d(TAG, "Processing pending resetLockout for face");
        processPendingLockoutsForFace(new ArrayList(this.mPendingResetLockoutsForFace));
        this.mPendingResetLockoutsForFace.clear();
    }

    private void processPendingLockoutsForFingerprint(List<UserAuthInfo> list) {
        FingerprintManager fingerprintManager = this.mFingerprintManager;
        if (fingerprintManager != null) {
            for (FingerprintSensorPropertiesInternal fingerprintSensorPropertiesInternal : fingerprintManager.getSensorPropertiesInternal()) {
                if (!fingerprintSensorPropertiesInternal.resetLockoutRequiresHardwareAuthToken) {
                    Iterator<UserAuthInfo> it = list.iterator();
                    while (it.hasNext()) {
                        this.mFingerprintManager.resetLockout(fingerprintSensorPropertiesInternal.sensorId, it.next().userId, null);
                    }
                } else if (fingerprintSensorPropertiesInternal.resetLockoutRequiresChallenge) {
                    Slog.w(TAG, "No fingerprint HAL interface requires HAT with challenge, sensorId: " + fingerprintSensorPropertiesInternal.sensorId);
                } else {
                    for (UserAuthInfo userAuthInfo : list) {
                        Slog.d(TAG, "Resetting fingerprint lockout for sensor: " + fingerprintSensorPropertiesInternal.sensorId + ", user: " + userAuthInfo.userId);
                        byte[] requestHatFromGatekeeperPassword = requestHatFromGatekeeperPassword(this.mSpManager, userAuthInfo, 0L);
                        if (requestHatFromGatekeeperPassword != null) {
                            this.mFingerprintManager.resetLockout(fingerprintSensorPropertiesInternal.sensorId, userAuthInfo.userId, requestHatFromGatekeeperPassword);
                        }
                    }
                }
            }
        }
    }

    private void processPendingLockoutsForFace(List<UserAuthInfo> list) {
        if (this.mFaceManager != null) {
            if (this.mFaceResetLockoutTask != null) {
                Slog.w(TAG, "mFaceGenerateChallengeCallback not null, previous operation may be stuck");
            }
            List<FaceSensorPropertiesInternal> sensorPropertiesInternal = this.mFaceManager.getSensorPropertiesInternal();
            ArraySet arraySet = new ArraySet();
            Iterator it = sensorPropertiesInternal.iterator();
            while (it.hasNext()) {
                arraySet.add(Integer.valueOf(((FaceSensorPropertiesInternal) it.next()).sensorId));
            }
            this.mFaceResetLockoutTask = new FaceResetLockoutTask(this.mFaceFinishCallback, this.mFaceManager, this.mSpManager, arraySet, list);
            for (FaceSensorPropertiesInternal faceSensorPropertiesInternal : sensorPropertiesInternal) {
                if (faceSensorPropertiesInternal.resetLockoutRequiresHardwareAuthToken) {
                    for (UserAuthInfo userAuthInfo : list) {
                        if (faceSensorPropertiesInternal.resetLockoutRequiresChallenge) {
                            Slog.d(TAG, "Generating challenge for sensor: " + faceSensorPropertiesInternal.sensorId + ", user: " + userAuthInfo.userId);
                            this.mFaceManager.generateChallenge(faceSensorPropertiesInternal.sensorId, userAuthInfo.userId, this.mFaceResetLockoutTask);
                        } else {
                            Slog.d(TAG, "Resetting face lockout for sensor: " + faceSensorPropertiesInternal.sensorId + ", user: " + userAuthInfo.userId);
                            byte[] requestHatFromGatekeeperPassword = requestHatFromGatekeeperPassword(this.mSpManager, userAuthInfo, 0L);
                            if (requestHatFromGatekeeperPassword != null) {
                                this.mFaceManager.resetLockout(faceSensorPropertiesInternal.sensorId, userAuthInfo.userId, requestHatFromGatekeeperPassword);
                            }
                        }
                    }
                } else {
                    Slog.w(TAG, "Lockout is below the HAL for all face authentication interfaces, sensorId: " + faceSensorPropertiesInternal.sensorId);
                }
            }
        }
    }

    private void processPendingLockoutsGeneric(List<UserAuthInfo> list) {
        for (UserAuthInfo userAuthInfo : list) {
            Slog.d(TAG, "Resetting biometric lockout for user: " + userAuthInfo.userId);
            byte[] requestHatFromGatekeeperPassword = requestHatFromGatekeeperPassword(this.mSpManager, userAuthInfo, 0L);
            if (requestHatFromGatekeeperPassword != null) {
                this.mBiometricManager.resetLockout(userAuthInfo.userId, requestHatFromGatekeeperPassword);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static byte[] requestHatFromGatekeeperPassword(SyntheticPasswordManager syntheticPasswordManager, UserAuthInfo userAuthInfo, long j) {
        VerifyCredentialResponse verifyChallengeInternal = syntheticPasswordManager.verifyChallengeInternal(getGatekeeperService(), userAuthInfo.gatekeeperPassword, j, userAuthInfo.userId);
        if (verifyChallengeInternal == null) {
            Slog.wtf(TAG, "VerifyChallenge failed, null response");
            return null;
        }
        if (verifyChallengeInternal.getResponseCode() != 0) {
            Slog.wtf(TAG, "VerifyChallenge failed, response: " + verifyChallengeInternal.getResponseCode());
            return null;
        }
        if (verifyChallengeInternal.getGatekeeperHAT() == null) {
            Slog.e(TAG, "Null HAT received from spManager");
        }
        return verifyChallengeInternal.getGatekeeperHAT();
    }

    private static synchronized IGateKeeperService getGatekeeperService() {
        synchronized (BiometricDeferredQueue.class) {
            IBinder service = ServiceManager.getService("android.service.gatekeeper.IGateKeeperService");
            if (service == null) {
                Slog.e(TAG, "Unable to acquire GateKeeperService");
                return null;
            }
            return IGateKeeperService.Stub.asInterface(service);
        }
    }
}
