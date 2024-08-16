package com.android.server.biometrics;

import android.content.Context;
import android.hardware.biometrics.IBiometricSensorReceiver;
import android.hardware.biometrics.IBiometricServiceReceiver;
import android.hardware.biometrics.IBiometricSysuiReceiver;
import android.hardware.biometrics.PromptInfo;
import android.hardware.face.FaceManager;
import android.hardware.fingerprint.FingerprintManager;
import android.hardware.fingerprint.FingerprintSensorPropertiesInternal;
import android.os.IBinder;
import android.os.RemoteException;
import android.security.KeyStore;
import android.util.Slog;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.statusbar.IStatusBarService;
import com.android.server.biometrics.log.BiometricContext;
import com.android.server.biometrics.log.BiometricFrameworkStatsLogger;
import com.android.server.biometrics.log.OperationContextExt;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.function.Function;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public final class AuthSession implements IBinder.DeathRecipient {
    private static final boolean DEBUG = true;
    private static final String TAG = "BiometricService/AuthSession";
    private int mAuthenticatedSensorId;
    private long mAuthenticatedTimeMs;
    private final BiometricContext mBiometricContext;

    @VisibleForTesting
    final BiometricFrameworkStatsLogger mBiometricFrameworkStatsLogger;
    private boolean mCancelled;
    private final ClientDeathReceiver mClientDeathReceiver;
    private final IBiometricServiceReceiver mClientReceiver;
    private final Context mContext;
    private final boolean mDebugEnabled;
    private int mErrorEscrow;
    private final List<FingerprintSensorPropertiesInternal> mFingerprintSensorProperties;
    private final KeyStore mKeyStore;
    private final String mOpPackageName;
    private final long mOperationId;
    final PreAuthInfo mPreAuthInfo;

    @VisibleForTesting
    final PromptInfo mPromptInfo;
    private final Random mRandom;
    private final long mRequestId;

    @VisibleForTesting
    final IBiometricSensorReceiver mSensorReceiver;
    private int[] mSensors;
    private long mStartTimeMs;
    private int mState;
    private final IStatusBarService mStatusBarService;

    @VisibleForTesting
    final IBiometricSysuiReceiver mSysuiReceiver;

    @VisibleForTesting
    final IBinder mToken;
    private byte[] mTokenEscrow;
    private final int mUserId;
    private int mVendorCodeEscrow;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public interface ClientDeathReceiver {
        void onClientDied();
    }

    @Retention(RetentionPolicy.SOURCE)
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    @interface SessionState {
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public AuthSession(Context context, BiometricContext biometricContext, IStatusBarService iStatusBarService, IBiometricSysuiReceiver iBiometricSysuiReceiver, KeyStore keyStore, Random random, ClientDeathReceiver clientDeathReceiver, PreAuthInfo preAuthInfo, IBinder iBinder, long j, long j2, int i, IBiometricSensorReceiver iBiometricSensorReceiver, IBiometricServiceReceiver iBiometricServiceReceiver, String str, PromptInfo promptInfo, boolean z, List<FingerprintSensorPropertiesInternal> list) {
        this(context, biometricContext, iStatusBarService, iBiometricSysuiReceiver, keyStore, random, clientDeathReceiver, preAuthInfo, iBinder, j, j2, i, iBiometricSensorReceiver, iBiometricServiceReceiver, str, promptInfo, z, list, BiometricFrameworkStatsLogger.getInstance());
    }

    @VisibleForTesting
    AuthSession(Context context, BiometricContext biometricContext, IStatusBarService iStatusBarService, IBiometricSysuiReceiver iBiometricSysuiReceiver, KeyStore keyStore, Random random, ClientDeathReceiver clientDeathReceiver, PreAuthInfo preAuthInfo, IBinder iBinder, long j, long j2, int i, IBiometricSensorReceiver iBiometricSensorReceiver, IBiometricServiceReceiver iBiometricServiceReceiver, String str, PromptInfo promptInfo, boolean z, List<FingerprintSensorPropertiesInternal> list, BiometricFrameworkStatsLogger biometricFrameworkStatsLogger) {
        this.mState = 0;
        this.mAuthenticatedSensorId = -1;
        Slog.d(TAG, "Creating AuthSession with: " + preAuthInfo);
        this.mContext = context;
        this.mBiometricContext = biometricContext;
        this.mStatusBarService = iStatusBarService;
        this.mSysuiReceiver = iBiometricSysuiReceiver;
        this.mKeyStore = keyStore;
        this.mRandom = random;
        this.mClientDeathReceiver = clientDeathReceiver;
        this.mPreAuthInfo = preAuthInfo;
        this.mToken = iBinder;
        this.mRequestId = j;
        this.mOperationId = j2;
        this.mUserId = i;
        this.mSensorReceiver = iBiometricSensorReceiver;
        this.mClientReceiver = iBiometricServiceReceiver;
        this.mOpPackageName = str;
        this.mPromptInfo = promptInfo;
        this.mDebugEnabled = z;
        this.mFingerprintSensorProperties = list;
        this.mCancelled = false;
        this.mBiometricFrameworkStatsLogger = biometricFrameworkStatsLogger;
        try {
            iBiometricServiceReceiver.asBinder().linkToDeath(this, 0);
        } catch (RemoteException unused) {
            Slog.w(TAG, "Unable to link to death");
        }
        setSensorsToStateUnknown();
    }

    @Override // android.os.IBinder.DeathRecipient
    public void binderDied() {
        Slog.e(TAG, "Binder died, session: " + this);
        this.mClientDeathReceiver.onClientDied();
    }

    private int getEligibleModalities() {
        return this.mPreAuthInfo.getEligibleModalities();
    }

    private void setSensorsToStateUnknown() {
        for (BiometricSensor biometricSensor : this.mPreAuthInfo.eligibleSensors) {
            Slog.v(TAG, "set to unknown state sensor: " + biometricSensor.id);
            biometricSensor.goToStateUnknown();
        }
    }

    private void setSensorsToStateWaitingForCookie(boolean z) throws RemoteException {
        for (BiometricSensor biometricSensor : this.mPreAuthInfo.eligibleSensors) {
            int sensorState = biometricSensor.getSensorState();
            if (!z || sensorState == 5 || sensorState == 4) {
                int nextInt = this.mRandom.nextInt(2147483646) + 1;
                boolean isConfirmationRequired = isConfirmationRequired(biometricSensor);
                Slog.v(TAG, "waiting for cooking for sensor: " + biometricSensor.id);
                biometricSensor.goToStateWaitingForCookie(isConfirmationRequired, this.mToken, this.mOperationId, this.mUserId, this.mSensorReceiver, this.mOpPackageName, this.mRequestId, nextInt, this.mPromptInfo.isAllowBackgroundAuthentication());
            } else {
                Slog.d(TAG, "Skip retry because sensor: " + biometricSensor.id + " is: " + sensorState);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void goToInitialState() throws RemoteException {
        PreAuthInfo preAuthInfo = this.mPreAuthInfo;
        if (preAuthInfo.credentialAvailable && preAuthInfo.eligibleSensors.isEmpty()) {
            this.mState = 9;
            int[] iArr = new int[0];
            this.mSensors = iArr;
            this.mStatusBarService.showAuthenticationDialog(this.mPromptInfo, this.mSysuiReceiver, iArr, true, false, this.mUserId, this.mOperationId, this.mOpPackageName, this.mRequestId);
            return;
        }
        if (!this.mPreAuthInfo.eligibleSensors.isEmpty()) {
            setSensorsToStateWaitingForCookie(false);
            this.mState = 1;
            return;
        }
        throw new IllegalStateException("No authenticators requested");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void onCookieReceived(int i) {
        if (this.mCancelled) {
            Slog.w(TAG, "Received cookie but already cancelled (ignoring): " + i);
            return;
        }
        if (hasAuthenticated()) {
            Slog.d(TAG, "onCookieReceived after successful auth");
            return;
        }
        Iterator<BiometricSensor> it = this.mPreAuthInfo.eligibleSensors.iterator();
        while (it.hasNext()) {
            it.next().goToStateCookieReturnedIfCookieMatches(i);
        }
        if (allCookiesReceived()) {
            this.mStartTimeMs = System.currentTimeMillis();
            startAllPreparedSensorsExceptFingerprint();
            if (this.mState != 5) {
                try {
                    boolean isConfirmationRequiredByAnyEligibleSensor = isConfirmationRequiredByAnyEligibleSensor();
                    this.mSensors = new int[this.mPreAuthInfo.eligibleSensors.size()];
                    for (int i2 = 0; i2 < this.mPreAuthInfo.eligibleSensors.size(); i2++) {
                        this.mSensors[i2] = this.mPreAuthInfo.eligibleSensors.get(i2).id;
                    }
                    this.mStatusBarService.showAuthenticationDialog(this.mPromptInfo, this.mSysuiReceiver, this.mSensors, this.mPreAuthInfo.shouldShowCredential(), isConfirmationRequiredByAnyEligibleSensor, this.mUserId, this.mOperationId, this.mOpPackageName, this.mRequestId);
                    this.mState = 2;
                    return;
                } catch (RemoteException e) {
                    Slog.e(TAG, "Remote exception", e);
                    return;
                }
            }
            this.mState = 3;
            return;
        }
        Slog.v(TAG, "onCookieReceived: still waiting");
    }

    private boolean isConfirmationRequired(BiometricSensor biometricSensor) {
        return biometricSensor.confirmationSupported() && (biometricSensor.confirmationAlwaysRequired(this.mUserId) || this.mPreAuthInfo.confirmationRequested);
    }

    private boolean isConfirmationRequiredByAnyEligibleSensor() {
        Iterator<BiometricSensor> it = this.mPreAuthInfo.eligibleSensors.iterator();
        while (it.hasNext()) {
            if (isConfirmationRequired(it.next())) {
                return true;
            }
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ Boolean lambda$startAllPreparedSensorsExceptFingerprint$0(BiometricSensor biometricSensor) {
        return Boolean.valueOf(biometricSensor.modality != 2);
    }

    private void startAllPreparedSensorsExceptFingerprint() {
        startAllPreparedSensors(new Function() { // from class: com.android.server.biometrics.AuthSession$$ExternalSyntheticLambda4
            @Override // java.util.function.Function
            public final Object apply(Object obj) {
                Boolean lambda$startAllPreparedSensorsExceptFingerprint$0;
                lambda$startAllPreparedSensorsExceptFingerprint$0 = AuthSession.lambda$startAllPreparedSensorsExceptFingerprint$0((BiometricSensor) obj);
                return lambda$startAllPreparedSensorsExceptFingerprint$0;
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ Boolean lambda$startAllPreparedFingerprintSensors$1(BiometricSensor biometricSensor) {
        return Boolean.valueOf(biometricSensor.modality == 2);
    }

    private void startAllPreparedFingerprintSensors() {
        startAllPreparedSensors(new Function() { // from class: com.android.server.biometrics.AuthSession$$ExternalSyntheticLambda2
            @Override // java.util.function.Function
            public final Object apply(Object obj) {
                Boolean lambda$startAllPreparedFingerprintSensors$1;
                lambda$startAllPreparedFingerprintSensors$1 = AuthSession.lambda$startAllPreparedFingerprintSensors$1((BiometricSensor) obj);
                return lambda$startAllPreparedFingerprintSensors$1;
            }
        });
    }

    private void startAllPreparedSensors(Function<BiometricSensor, Boolean> function) {
        for (BiometricSensor biometricSensor : this.mPreAuthInfo.eligibleSensors) {
            if (function.apply(biometricSensor).booleanValue()) {
                try {
                    Slog.v(TAG, "Starting sensor: " + biometricSensor.id);
                    biometricSensor.startSensor();
                } catch (RemoteException e) {
                    Slog.e(TAG, "Unable to start prepared client, sensor: " + biometricSensor, e);
                }
            }
        }
    }

    private void cancelAllSensors() {
        cancelAllSensors(new Function() { // from class: com.android.server.biometrics.AuthSession$$ExternalSyntheticLambda1
            @Override // java.util.function.Function
            public final Object apply(Object obj) {
                Boolean lambda$cancelAllSensors$2;
                lambda$cancelAllSensors$2 = AuthSession.lambda$cancelAllSensors$2((BiometricSensor) obj);
                return lambda$cancelAllSensors$2;
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ Boolean lambda$cancelAllSensors$2(BiometricSensor biometricSensor) {
        return Boolean.TRUE;
    }

    private void cancelAllSensors(Function<BiometricSensor, Boolean> function) {
        for (BiometricSensor biometricSensor : this.mPreAuthInfo.eligibleSensors) {
            try {
                if (function.apply(biometricSensor).booleanValue()) {
                    Slog.d(TAG, "Cancelling sensorId: " + biometricSensor.id);
                    biometricSensor.goToStateCancelling(this.mToken, this.mOpPackageName, this.mRequestId);
                }
            } catch (RemoteException unused) {
                Slog.e(TAG, "Unable to cancel authentication");
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean onErrorReceived(int i, int i2, int i3, int i4) throws RemoteException {
        Slog.d(TAG, "onErrorReceived sensor: " + i + " error: " + i3);
        if (!containsCookie(i2)) {
            Slog.e(TAG, "Unknown/expired cookie: " + i2);
            return false;
        }
        for (BiometricSensor biometricSensor : this.mPreAuthInfo.eligibleSensors) {
            if (biometricSensor.getSensorState() == 3) {
                biometricSensor.goToStoppedStateIfCookieMatches(i2, i3);
            }
        }
        if (hasAuthenticated()) {
            Slog.d(TAG, "onErrorReceived after successful auth (ignoring)");
            return false;
        }
        this.mErrorEscrow = i3;
        this.mVendorCodeEscrow = i4;
        int sensorIdToModality = sensorIdToModality(i);
        int i5 = this.mState;
        if (i5 != 1) {
            if (i5 == 2 || i5 == 3) {
                boolean z = i3 == 7 || i3 == 9;
                if (isAllowDeviceCredential() && z) {
                    this.mState = 9;
                    this.mStatusBarService.onBiometricError(sensorIdToModality, i3, i4);
                } else {
                    if (i3 == 5) {
                        this.mStatusBarService.hideAuthenticationDialog(this.mRequestId);
                        this.mClientReceiver.onError(sensorIdToModality, i3, i4);
                        return true;
                    }
                    this.mState = 8;
                    this.mStatusBarService.onBiometricError(sensorIdToModality, i3, i4);
                }
            } else {
                if (i5 == 4) {
                    this.mClientReceiver.onError(sensorIdToModality, i3, i4);
                    this.mStatusBarService.hideAuthenticationDialog(this.mRequestId);
                    return true;
                }
                if (i5 == 9) {
                    Slog.d(TAG, "Biometric canceled, ignoring from state: " + this.mState);
                } else {
                    if (i5 == 10) {
                        this.mStatusBarService.hideAuthenticationDialog(this.mRequestId);
                        return true;
                    }
                    Slog.e(TAG, "Unhandled error state, mState: " + this.mState);
                }
            }
        } else if (isAllowDeviceCredential()) {
            this.mPromptInfo.setAuthenticators(Utils.removeBiometricBits(this.mPromptInfo.getAuthenticators()));
            this.mState = 9;
            int[] iArr = new int[0];
            this.mSensors = iArr;
            this.mStatusBarService.showAuthenticationDialog(this.mPromptInfo, this.mSysuiReceiver, iArr, true, false, this.mUserId, this.mOperationId, this.mOpPackageName, this.mRequestId);
        } else {
            this.mClientReceiver.onError(sensorIdToModality, i3, i4);
            return true;
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void onAcquired(int i, int i2, int i3) {
        if (hasAuthenticated()) {
            Slog.d(TAG, "onAcquired after successful auth");
            return;
        }
        String acquiredMessageForSensor = getAcquiredMessageForSensor(i, i2, i3);
        Slog.d(TAG, "sensorId: " + i + " acquiredInfo: " + i2 + " message: " + acquiredMessageForSensor);
        if (acquiredMessageForSensor == null) {
            return;
        }
        try {
            this.mStatusBarService.onBiometricHelp(sensorIdToModality(i), acquiredMessageForSensor);
            if (i2 == 6) {
                i2 = i3 + 1000;
            }
            this.mClientReceiver.onAcquired(i2, acquiredMessageForSensor);
        } catch (RemoteException e) {
            Slog.e(TAG, "Remote exception", e);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void onSystemEvent(int i) {
        if (hasAuthenticated()) {
            Slog.d(TAG, "onSystemEvent after successful auth");
        } else if (this.mPromptInfo.isReceiveSystemEvents()) {
            try {
                this.mClientReceiver.onSystemEvent(i);
            } catch (RemoteException e) {
                Slog.e(TAG, "RemoteException", e);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void onDialogAnimatedIn(boolean z) {
        if (this.mState != 2) {
            Slog.e(TAG, "onDialogAnimatedIn, unexpected state: " + this.mState);
            return;
        }
        this.mState = 3;
        if (z) {
            startAllPreparedFingerprintSensors();
        } else {
            Slog.d(TAG, "delaying fingerprint sensor start");
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void onStartFingerprint() {
        int i = this.mState;
        if (i != 2 && i != 3 && i != 4 && i != 8) {
            Slog.w(TAG, "onStartFingerprint, started from unexpected state: " + this.mState);
        }
        startAllPreparedFingerprintSensors();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void onTryAgainPressed() {
        if (hasAuthenticated()) {
            Slog.d(TAG, "onTryAgainPressed after successful auth");
            return;
        }
        if (this.mState != 4) {
            Slog.w(TAG, "onTryAgainPressed, state: " + this.mState);
        }
        try {
            setSensorsToStateWaitingForCookie(true);
            this.mState = 5;
        } catch (RemoteException e) {
            Slog.e(TAG, "RemoteException: " + e);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void onAuthenticationSucceeded(final int i, boolean z, byte[] bArr) {
        if (hasAuthenticated()) {
            Slog.d(TAG, "onAuthenticationSucceeded after successful auth");
            return;
        }
        this.mAuthenticatedSensorId = i;
        if (z && sensorIdToModality(i) != 8) {
            this.mTokenEscrow = bArr;
        } else if (bArr != null) {
            Slog.w(TAG, "Dropping authToken for non-strong biometric, id: " + i);
        }
        try {
            this.mStatusBarService.onBiometricAuthenticated(sensorIdToModality(i));
            if (!isConfirmationRequiredByAnyEligibleSensor()) {
                this.mState = 7;
            } else {
                this.mAuthenticatedTimeMs = System.currentTimeMillis();
                this.mState = 6;
            }
        } catch (RemoteException e) {
            Slog.e(TAG, "RemoteException", e);
        }
        cancelAllSensors(new Function() { // from class: com.android.server.biometrics.AuthSession$$ExternalSyntheticLambda3
            @Override // java.util.function.Function
            public final Object apply(Object obj) {
                Boolean lambda$onAuthenticationSucceeded$3;
                lambda$onAuthenticationSucceeded$3 = AuthSession.lambda$onAuthenticationSucceeded$3(i, (BiometricSensor) obj);
                return lambda$onAuthenticationSucceeded$3;
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ Boolean lambda$onAuthenticationSucceeded$3(int i, BiometricSensor biometricSensor) {
        return Boolean.valueOf(biometricSensor.id != i);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void onAuthenticationRejected(int i) {
        if (hasAuthenticated()) {
            Slog.d(TAG, "onAuthenticationRejected after successful auth");
            return;
        }
        try {
            this.mStatusBarService.onBiometricError(sensorIdToModality(i), 100, 0);
            if (pauseSensorIfSupported(i)) {
                this.mState = 4;
            }
            this.mClientReceiver.onAuthenticationFailed();
        } catch (RemoteException e) {
            Slog.e(TAG, "RemoteException", e);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void onAuthenticationTimedOut(int i, int i2, int i3, int i4) {
        if (hasAuthenticated()) {
            Slog.d(TAG, "onAuthenticationTimedOut after successful auth");
            return;
        }
        try {
            this.mStatusBarService.onBiometricError(sensorIdToModality(i), i3, i4);
            pauseSensorIfSupported(i);
            this.mState = 4;
        } catch (RemoteException e) {
            Slog.e(TAG, "RemoteException", e);
        }
    }

    private boolean pauseSensorIfSupported(final int i) {
        if (sensorIdToModality(i) != 8) {
            return false;
        }
        cancelAllSensors(new Function() { // from class: com.android.server.biometrics.AuthSession$$ExternalSyntheticLambda0
            @Override // java.util.function.Function
            public final Object apply(Object obj) {
                Boolean lambda$pauseSensorIfSupported$4;
                lambda$pauseSensorIfSupported$4 = AuthSession.lambda$pauseSensorIfSupported$4(i, (BiometricSensor) obj);
                return lambda$pauseSensorIfSupported$4;
            }
        });
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ Boolean lambda$pauseSensorIfSupported$4(int i, BiometricSensor biometricSensor) {
        return Boolean.valueOf(biometricSensor.id == i);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void onDeviceCredentialPressed() {
        if (hasAuthenticated()) {
            Slog.d(TAG, "onDeviceCredentialPressed after successful auth");
        } else {
            cancelAllSensors();
            this.mState = 9;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean onClientDied() {
        try {
            int i = this.mState;
            if (i == 2 || i == 3) {
                this.mState = 10;
                cancelAllSensors();
                return false;
            }
            this.mStatusBarService.hideAuthenticationDialog(this.mRequestId);
            return true;
        } catch (RemoteException e) {
            Slog.e(TAG, "Remote Exception: " + e);
            return true;
        }
    }

    private boolean hasAuthenticated() {
        return this.mAuthenticatedSensorId != -1;
    }

    private void logOnDialogDismissed(int i) {
        if (i == 1) {
            long currentTimeMillis = System.currentTimeMillis() - this.mAuthenticatedTimeMs;
            Slog.v(TAG, "Confirmed! Modality: " + statsModality() + ", User: " + this.mUserId + ", IsCrypto: " + isCrypto() + ", Client: 2, RequireConfirmation: " + this.mPreAuthInfo.confirmationRequested + ", State: 3, Latency: " + currentTimeMillis);
            this.mBiometricFrameworkStatsLogger.authenticate(this.mBiometricContext.updateContext(new OperationContextExt(true), isCrypto()), statsModality(), 0, 2, this.mDebugEnabled, currentTimeMillis, 3, this.mPreAuthInfo.confirmationRequested, this.mUserId, -1.0f);
            return;
        }
        long currentTimeMillis2 = System.currentTimeMillis() - this.mStartTimeMs;
        int i2 = i != 2 ? i != 3 ? 0 : 10 : 13;
        Slog.v(TAG, "Dismissed! Modality: " + statsModality() + ", User: " + this.mUserId + ", IsCrypto: " + isCrypto() + ", Action: 2, Client: 2, Reason: " + i + ", Error: " + i2 + ", Latency: " + currentTimeMillis2);
        if (i2 != 0) {
            this.mBiometricFrameworkStatsLogger.error(this.mBiometricContext.updateContext(new OperationContextExt(true), isCrypto()), statsModality(), 2, 2, this.mDebugEnabled, currentTimeMillis2, i2, 0, this.mUserId);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Removed duplicated region for block: B:15:0x003c A[Catch: all -> 0x0067, RemoteException -> 0x0069, TryCatch #1 {RemoteException -> 0x0069, blocks: (B:5:0x000b, B:6:0x0011, B:7:0x0017, B:11:0x0025, B:12:0x0032, B:13:0x0038, B:15:0x003c, B:16:0x005d, B:17:0x0057, B:18:0x006b), top: B:2:0x0005, outer: #0 }] */
    /* JADX WARN: Removed duplicated region for block: B:17:0x0057 A[Catch: all -> 0x0067, RemoteException -> 0x0069, TryCatch #1 {RemoteException -> 0x0069, blocks: (B:5:0x000b, B:6:0x0011, B:7:0x0017, B:11:0x0025, B:12:0x0032, B:13:0x0038, B:15:0x003c, B:16:0x005d, B:17:0x0057, B:18:0x006b), top: B:2:0x0005, outer: #0 }] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void onDialogDismissed(int i, byte[] bArr) {
        byte[] bArr2;
        logOnDialogDismissed(i);
        try {
            try {
                switch (i) {
                    case 1:
                    case 4:
                        bArr2 = this.mTokenEscrow;
                        if (bArr2 == null) {
                            Slog.d(TAG, "addAuthToken: " + this.mKeyStore.addAuthToken(bArr2));
                        } else {
                            Slog.e(TAG, "mTokenEscrow is null");
                        }
                        this.mClientReceiver.onAuthenticationSucceeded(Utils.getAuthenticationTypeForResult(i));
                        break;
                    case 2:
                        this.mClientReceiver.onDialogDismissed(i);
                        break;
                    case 3:
                        this.mClientReceiver.onError(getEligibleModalities(), 10, 0);
                        break;
                    case 5:
                    case 6:
                        this.mClientReceiver.onError(getEligibleModalities(), this.mErrorEscrow, this.mVendorCodeEscrow);
                        break;
                    case 7:
                        if (bArr != null) {
                            this.mKeyStore.addAuthToken(bArr);
                        } else {
                            Slog.e(TAG, "credentialAttestation is null");
                        }
                        bArr2 = this.mTokenEscrow;
                        if (bArr2 == null) {
                        }
                        this.mClientReceiver.onAuthenticationSucceeded(Utils.getAuthenticationTypeForResult(i));
                        break;
                    default:
                        Slog.w(TAG, "Unhandled reason: " + i);
                        break;
                }
            } catch (RemoteException e) {
                Slog.e(TAG, "Remote exception", e);
            }
            cancelAllSensors();
        } catch (Throwable th) {
            cancelAllSensors();
            throw th;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean onCancelAuthSession(boolean z) {
        if (hasAuthenticated()) {
            Slog.d(TAG, "onCancelAuthSession after successful auth");
            return true;
        }
        this.mCancelled = true;
        int i = this.mState;
        boolean z2 = i == 1 || i == 2 || i == 3;
        cancelAllSensors();
        if (z2 && !z) {
            return false;
        }
        try {
            this.mClientReceiver.onError(getEligibleModalities(), 5, 0);
            this.mStatusBarService.hideAuthenticationDialog(this.mRequestId);
            return true;
        } catch (RemoteException e) {
            Slog.e(TAG, "Remote exception", e);
            return false;
        }
    }

    boolean isCrypto() {
        return this.mOperationId != 0;
    }

    private boolean containsCookie(int i) {
        Iterator<BiometricSensor> it = this.mPreAuthInfo.eligibleSensors.iterator();
        while (it.hasNext()) {
            if (it.next().getCookie() == i) {
                return true;
            }
        }
        return false;
    }

    private boolean isAllowDeviceCredential() {
        return Utils.isCredentialRequested(this.mPromptInfo);
    }

    @VisibleForTesting
    boolean allCookiesReceived() {
        int numSensorsWaitingForCookie = this.mPreAuthInfo.numSensorsWaitingForCookie();
        Slog.d(TAG, "Remaining cookies: " + numSensorsWaitingForCookie);
        return numSensorsWaitingForCookie == 0;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int getState() {
        return this.mState;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public long getRequestId() {
        return this.mRequestId;
    }

    private int statsModality() {
        Iterator<BiometricSensor> it = this.mPreAuthInfo.eligibleSensors.iterator();
        int i = 0;
        while (it.hasNext()) {
            int i2 = it.next().modality;
            if ((i2 & 2) != 0) {
                i |= 1;
            }
            if ((i2 & 4) != 0) {
                i |= 2;
            }
            if ((i2 & 8) != 0) {
                i |= 4;
            }
        }
        return i;
    }

    private int sensorIdToModality(int i) {
        for (BiometricSensor biometricSensor : this.mPreAuthInfo.eligibleSensors) {
            if (i == biometricSensor.id) {
                return biometricSensor.modality;
            }
        }
        Slog.e(TAG, "Unknown sensor: " + i);
        return 0;
    }

    private String getAcquiredMessageForSensor(int i, int i2, int i3) {
        int sensorIdToModality = sensorIdToModality(i);
        if (sensorIdToModality == 2) {
            return FingerprintManager.getAcquiredString(this.mContext, i2, i3);
        }
        if (sensorIdToModality != 8) {
            return null;
        }
        return FaceManager.getAuthHelpMessage(this.mContext, i2, i3);
    }

    public String toString() {
        return "State: " + this.mState + ", cancelled: " + this.mCancelled + ", isCrypto: " + isCrypto() + ", PreAuthInfo: " + this.mPreAuthInfo + ", requestId: " + this.mRequestId;
    }
}
