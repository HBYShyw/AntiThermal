package com.android.server.biometrics.sensors.fingerprint.aidl;

import android.content.Context;
import android.content.pm.UserInfo;
import android.hardware.biometrics.BiometricAuthenticator;
import android.hardware.biometrics.ITestSession;
import android.hardware.biometrics.ITestSessionCallback;
import android.hardware.biometrics.fingerprint.ISession;
import android.hardware.biometrics.fingerprint.ISessionCallback;
import android.hardware.fingerprint.Fingerprint;
import android.hardware.fingerprint.FingerprintSensorPropertiesInternal;
import android.hardware.keymaster.HardwareAuthToken;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Parcel;
import android.os.RemoteException;
import android.os.UserManager;
import android.util.Slog;
import android.util.proto.ProtoOutputStream;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.util.FrameworkStatsLog;
import com.android.server.biometrics.HardwareAuthTokenUtils;
import com.android.server.biometrics.Utils;
import com.android.server.biometrics.log.BiometricContext;
import com.android.server.biometrics.log.BiometricLogger;
import com.android.server.biometrics.sensors.AcquisitionClient;
import com.android.server.biometrics.sensors.AuthSessionCoordinator;
import com.android.server.biometrics.sensors.AuthenticationConsumer;
import com.android.server.biometrics.sensors.BaseClientMonitor;
import com.android.server.biometrics.sensors.BiometricScheduler;
import com.android.server.biometrics.sensors.BiometricStateCallback;
import com.android.server.biometrics.sensors.EnumerateConsumer;
import com.android.server.biometrics.sensors.ErrorConsumer;
import com.android.server.biometrics.sensors.LockoutCache;
import com.android.server.biometrics.sensors.LockoutConsumer;
import com.android.server.biometrics.sensors.LockoutResetDispatcher;
import com.android.server.biometrics.sensors.RemovalConsumer;
import com.android.server.biometrics.sensors.StartUserClient;
import com.android.server.biometrics.sensors.StopUserClient;
import com.android.server.biometrics.sensors.UserAwareBiometricScheduler;
import com.android.server.biometrics.sensors.fingerprint.FingerprintUtils;
import com.android.server.biometrics.sensors.fingerprint.GestureAvailabilityDispatcher;
import com.android.server.biometrics.sensors.fingerprint.aidl.Sensor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.function.Supplier;
import system.ext.loader.core.ExtLoader;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public class Sensor {
    public static IFingerprintSensorExt mFingerprintSensorExt = (IFingerprintSensorExt) ExtLoader.type(IFingerprintSensorExt.class).create();
    private static FingerprintProvider mOplusProvider;
    private final Map<Integer, Long> mAuthenticatorIds;
    private final Context mContext;
    AidlSession mCurrentSession;
    private final Handler mHandler;
    private final Supplier<AidlSession> mLazySession;
    private final LockoutCache mLockoutCache;
    private final FingerprintProvider mProvider;
    private final UserAwareBiometricScheduler mScheduler;
    private final FingerprintSensorPropertiesInternal mSensorProperties;
    private final String mTag;
    private boolean mTestHalEnabled;
    private final IBinder mToken;

    @VisibleForTesting
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public static class HalSessionCallback extends ISessionCallback.Stub {
        private AuthSessionCoordinator mAuthSessionCoordinator;
        private final Callback mCallback;
        private final Context mContext;
        private final Handler mHandler;
        private final LockoutCache mLockoutCache;
        private final LockoutResetDispatcher mLockoutResetDispatcher;
        private final UserAwareBiometricScheduler mScheduler;
        private final int mSensorId;
        private final String mTag;
        private final int mUserId;

        /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
        public interface Callback {
            void onHardwareUnavailable();
        }

        public String getInterfaceHash() {
            return "637371b53fb7faf9bd43aa51b72c23852d6e6d96";
        }

        public int getInterfaceVersion() {
            return 3;
        }

        HalSessionCallback(Context context, Handler handler, String str, UserAwareBiometricScheduler userAwareBiometricScheduler, int i, int i2, LockoutCache lockoutCache, LockoutResetDispatcher lockoutResetDispatcher, AuthSessionCoordinator authSessionCoordinator, Callback callback) {
            this.mContext = context;
            this.mHandler = handler;
            this.mTag = str;
            this.mScheduler = userAwareBiometricScheduler;
            this.mSensorId = i;
            this.mUserId = i2;
            this.mLockoutCache = lockoutCache;
            this.mLockoutResetDispatcher = lockoutResetDispatcher;
            this.mAuthSessionCoordinator = authSessionCoordinator;
            this.mCallback = callback;
        }

        public void onChallengeGenerated(final long j) {
            this.mHandler.post(new Runnable() { // from class: com.android.server.biometrics.sensors.fingerprint.aidl.Sensor$HalSessionCallback$$ExternalSyntheticLambda7
                @Override // java.lang.Runnable
                public final void run() {
                    Sensor.HalSessionCallback.this.lambda$onChallengeGenerated$0(j);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onChallengeGenerated$0(long j) {
            BaseClientMonitor currentClient = this.mScheduler.getCurrentClient();
            if (!(currentClient instanceof FingerprintGenerateChallengeClient)) {
                Slog.e(this.mTag, "onChallengeGenerated for wrong client: " + Utils.getClientName(currentClient));
                return;
            }
            ((FingerprintGenerateChallengeClient) currentClient).onChallengeGenerated(this.mSensorId, this.mUserId, j);
        }

        public void onChallengeRevoked(final long j) {
            this.mHandler.post(new Runnable() { // from class: com.android.server.biometrics.sensors.fingerprint.aidl.Sensor$HalSessionCallback$$ExternalSyntheticLambda1
                @Override // java.lang.Runnable
                public final void run() {
                    Sensor.HalSessionCallback.this.lambda$onChallengeRevoked$1(j);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onChallengeRevoked$1(long j) {
            BaseClientMonitor currentClient = this.mScheduler.getCurrentClient();
            if (!(currentClient instanceof FingerprintRevokeChallengeClient)) {
                Slog.e(this.mTag, "onChallengeRevoked for wrong client: " + Utils.getClientName(currentClient));
                return;
            }
            ((FingerprintRevokeChallengeClient) currentClient).onChallengeRevoked(this.mSensorId, this.mUserId, j);
        }

        public void onAcquired(final byte b, final int i) {
            this.mHandler.post(new Runnable() { // from class: com.android.server.biometrics.sensors.fingerprint.aidl.Sensor$HalSessionCallback$$ExternalSyntheticLambda11
                @Override // java.lang.Runnable
                public final void run() {
                    Sensor.HalSessionCallback.this.lambda$onAcquired$2(b, i);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onAcquired$2(byte b, int i) {
            BaseClientMonitor currentClient = this.mScheduler.getCurrentClient();
            if (!(currentClient instanceof AcquisitionClient)) {
                Slog.e(this.mTag, "onAcquired for non-acquisition client: " + Utils.getClientName(currentClient));
                return;
            }
            AcquisitionClient acquisitionClient = (AcquisitionClient) currentClient;
            if ("android.server.biometrics.fingerprint".equals(currentClient.getOwnerString()) && b == 1 && (currentClient instanceof FingerprintAuthenticationClient)) {
                Slog.d(this.mTag, "aidlAcquiredInfo: 2 ,info:" + ((int) b));
                b = (byte) 2;
            }
            IFingerprintSensorExt iFingerprintSensorExt = Sensor.mFingerprintSensorExt;
            if (iFingerprintSensorExt == null || !iFingerprintSensorExt.onAcquired(AidlConversionUtils.toFrameworkAcquiredInfo(b), i)) {
                acquisitionClient.onAcquired(AidlConversionUtils.toFrameworkAcquiredInfo(b), i);
            }
        }

        public void onError(final byte b, final int i) {
            this.mHandler.post(new Runnable() { // from class: com.android.server.biometrics.sensors.fingerprint.aidl.Sensor$HalSessionCallback$$ExternalSyntheticLambda6
                @Override // java.lang.Runnable
                public final void run() {
                    Sensor.HalSessionCallback.this.lambda$onError$3(b, i);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* JADX WARN: Multi-variable type inference failed */
        public /* synthetic */ void lambda$onError$3(byte b, int i) {
            BaseClientMonitor currentClient = this.mScheduler.getCurrentClient();
            Slog.d(this.mTag, "onError, client: " + Utils.getClientName(currentClient) + ", error: " + ((int) b) + ", vendorCode: " + i);
            if (!(currentClient instanceof ErrorConsumer)) {
                Slog.e(this.mTag, "onError for non-error consumer: " + Utils.getClientName(currentClient));
                return;
            }
            IFingerprintSensorExt iFingerprintSensorExt = Sensor.mFingerprintSensorExt;
            if (iFingerprintSensorExt == null || !iFingerprintSensorExt.onError(AidlConversionUtils.toFrameworkError(b), i)) {
                ((ErrorConsumer) currentClient).onError(AidlConversionUtils.toFrameworkError(b), i);
                if (b == 1) {
                    this.mCallback.onHardwareUnavailable();
                }
            }
        }

        public void onEnrollmentProgress(final int i, final int i2) {
            this.mHandler.post(new Runnable() { // from class: com.android.server.biometrics.sensors.fingerprint.aidl.Sensor$HalSessionCallback$$ExternalSyntheticLambda4
                @Override // java.lang.Runnable
                public final void run() {
                    Sensor.HalSessionCallback.this.lambda$onEnrollmentProgress$4(i, i2);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onEnrollmentProgress$4(int i, int i2) {
            BaseClientMonitor currentClient = this.mScheduler.getCurrentClient();
            if (!(currentClient instanceof FingerprintEnrollClient)) {
                Slog.e(this.mTag, "onEnrollmentProgress for non-enroll client: " + Utils.getClientName(currentClient));
                return;
            }
            BiometricAuthenticator.Identifier fingerprint = new Fingerprint(FingerprintUtils.getInstance(this.mSensorId).getUniqueName(this.mContext, currentClient.getTargetUserId()), i, this.mSensorId);
            IFingerprintSensorExt iFingerprintSensorExt = Sensor.mFingerprintSensorExt;
            if (iFingerprintSensorExt != null) {
                iFingerprintSensorExt.handleOnEnrollment(fingerprint, i2);
            }
            ((FingerprintEnrollClient) currentClient).onEnrollResult(fingerprint, i2);
        }

        public void onAuthenticationSucceeded(final int i, final HardwareAuthToken hardwareAuthToken) {
            this.mHandler.post(new Runnable() { // from class: com.android.server.biometrics.sensors.fingerprint.aidl.Sensor$HalSessionCallback$$ExternalSyntheticLambda10
                @Override // java.lang.Runnable
                public final void run() {
                    Sensor.HalSessionCallback.this.lambda$onAuthenticationSucceeded$5(i, hardwareAuthToken);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* JADX WARN: Multi-variable type inference failed */
        public /* synthetic */ void lambda$onAuthenticationSucceeded$5(int i, HardwareAuthToken hardwareAuthToken) {
            BaseClientMonitor currentClient = this.mScheduler.getCurrentClient();
            if (!(currentClient instanceof AuthenticationConsumer)) {
                Slog.e(this.mTag, "onAuthenticationSucceeded for non-authentication consumer: " + Utils.getClientName(currentClient));
                return;
            }
            AuthenticationConsumer authenticationConsumer = (AuthenticationConsumer) currentClient;
            Fingerprint fingerprint = new Fingerprint("", i, this.mSensorId);
            byte[] byteArray = HardwareAuthTokenUtils.toByteArray(hardwareAuthToken);
            ArrayList<Byte> arrayList = new ArrayList<>();
            for (byte b : byteArray) {
                arrayList.add(Byte.valueOf(b));
            }
            IFingerprintSensorExt iFingerprintSensorExt = Sensor.mFingerprintSensorExt;
            if (iFingerprintSensorExt == null || !iFingerprintSensorExt.onAuthenticated(this.mSensorId, i, fingerprint.getGroupId(), arrayList)) {
                authenticationConsumer.onAuthenticated(fingerprint, true, arrayList);
            }
        }

        public void onAuthenticationFailed() {
            this.mHandler.post(new Runnable() { // from class: com.android.server.biometrics.sensors.fingerprint.aidl.Sensor$HalSessionCallback$$ExternalSyntheticLambda2
                @Override // java.lang.Runnable
                public final void run() {
                    Sensor.HalSessionCallback.this.lambda$onAuthenticationFailed$6();
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* JADX WARN: Multi-variable type inference failed */
        public /* synthetic */ void lambda$onAuthenticationFailed$6() {
            BaseClientMonitor currentClient = this.mScheduler.getCurrentClient();
            if (!(currentClient instanceof AuthenticationConsumer)) {
                Slog.e(this.mTag, "onAuthenticationFailed for non-authentication consumer: " + Utils.getClientName(currentClient));
                return;
            }
            AuthenticationConsumer authenticationConsumer = (AuthenticationConsumer) currentClient;
            Fingerprint fingerprint = new Fingerprint("", 0, this.mSensorId);
            IFingerprintSensorExt iFingerprintSensorExt = Sensor.mFingerprintSensorExt;
            if (iFingerprintSensorExt == null || !iFingerprintSensorExt.onAuthenticated(this.mSensorId, 0, fingerprint.getGroupId(), null)) {
                authenticationConsumer.onAuthenticated(fingerprint, false, null);
            }
        }

        public void onLockoutTimed(final long j) {
            this.mHandler.post(new Runnable() { // from class: com.android.server.biometrics.sensors.fingerprint.aidl.Sensor$HalSessionCallback$$ExternalSyntheticLambda13
                @Override // java.lang.Runnable
                public final void run() {
                    Sensor.HalSessionCallback.this.lambda$onLockoutTimed$7(j);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* JADX WARN: Multi-variable type inference failed */
        public /* synthetic */ void lambda$onLockoutTimed$7(long j) {
            BaseClientMonitor currentClient = this.mScheduler.getCurrentClient();
            if (!(currentClient instanceof LockoutConsumer)) {
                Slog.e(this.mTag, "onLockoutTimed for non-lockout consumer: " + Utils.getClientName(currentClient));
                return;
            }
            if (Sensor.mOplusProvider.getServiceProviderAidlEx() != null && (Sensor.mOplusProvider.getServiceProviderAidlEx().getFailedAttempts() + 1) % 5 != 0) {
                Slog.e(this.mTag, "onLockoutTimed maybe just in touch mode, user not press side key");
                return;
            }
            IFingerprintSensorExt iFingerprintSensorExt = Sensor.mFingerprintSensorExt;
            if (iFingerprintSensorExt != null) {
                iFingerprintSensorExt.onLockoutTimed();
            }
            ((LockoutConsumer) currentClient).onLockoutTimed(j);
        }

        public void onLockoutPermanent() {
            this.mHandler.post(new Runnable() { // from class: com.android.server.biometrics.sensors.fingerprint.aidl.Sensor$HalSessionCallback$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    Sensor.HalSessionCallback.this.lambda$onLockoutPermanent$8();
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* JADX WARN: Multi-variable type inference failed */
        public /* synthetic */ void lambda$onLockoutPermanent$8() {
            BaseClientMonitor currentClient = this.mScheduler.getCurrentClient();
            if (!(currentClient instanceof LockoutConsumer)) {
                Slog.e(this.mTag, "onLockoutPermanent for non-lockout consumer: " + Utils.getClientName(currentClient));
                return;
            }
            if (Sensor.mOplusProvider.getServiceProviderAidlEx() != null && (Sensor.mOplusProvider.getServiceProviderAidlEx().getFailedAttempts() + 1) % 5 != 0) {
                Slog.e(this.mTag, "onLockoutPermanent maybe just in touch mode, user not press side key");
                return;
            }
            IFingerprintSensorExt iFingerprintSensorExt = Sensor.mFingerprintSensorExt;
            if (iFingerprintSensorExt != null) {
                iFingerprintSensorExt.onLockoutPermanent();
            }
            ((LockoutConsumer) currentClient).onLockoutPermanent();
        }

        public void onLockoutCleared() {
            this.mHandler.post(new Runnable() { // from class: com.android.server.biometrics.sensors.fingerprint.aidl.Sensor$HalSessionCallback$$ExternalSyntheticLambda8
                @Override // java.lang.Runnable
                public final void run() {
                    Sensor.HalSessionCallback.this.lambda$onLockoutCleared$9();
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onLockoutCleared$9() {
            BaseClientMonitor currentClient = this.mScheduler.getCurrentClient();
            if (!(currentClient instanceof FingerprintResetLockoutClient)) {
                Slog.d(this.mTag, "onLockoutCleared outside of resetLockout by HAL");
                int i = this.mSensorId;
                FingerprintResetLockoutClient.resetLocalLockoutStateToNone(i, this.mUserId, this.mLockoutCache, this.mLockoutResetDispatcher, this.mAuthSessionCoordinator, Utils.getCurrentStrength(i), -1L);
            } else {
                Slog.d(this.mTag, "onLockoutCleared after resetLockout");
                ((FingerprintResetLockoutClient) currentClient).onLockoutCleared();
            }
        }

        public void onInteractionDetected() {
            this.mHandler.post(new Runnable() { // from class: com.android.server.biometrics.sensors.fingerprint.aidl.Sensor$HalSessionCallback$$ExternalSyntheticLambda5
                @Override // java.lang.Runnable
                public final void run() {
                    Sensor.HalSessionCallback.this.lambda$onInteractionDetected$10();
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onInteractionDetected$10() {
            BaseClientMonitor currentClient = this.mScheduler.getCurrentClient();
            if (!(currentClient instanceof FingerprintDetectClient)) {
                Slog.e(this.mTag, "onInteractionDetected for non-detect client: " + Utils.getClientName(currentClient));
                return;
            }
            ((FingerprintDetectClient) currentClient).onInteractionDetected();
        }

        public void onEnrollmentsEnumerated(final int[] iArr) {
            this.mHandler.post(new Runnable() { // from class: com.android.server.biometrics.sensors.fingerprint.aidl.Sensor$HalSessionCallback$$ExternalSyntheticLambda12
                @Override // java.lang.Runnable
                public final void run() {
                    Sensor.HalSessionCallback.this.lambda$onEnrollmentsEnumerated$11(iArr);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* JADX WARN: Multi-variable type inference failed */
        public /* synthetic */ void lambda$onEnrollmentsEnumerated$11(int[] iArr) {
            BaseClientMonitor currentClient = this.mScheduler.getCurrentClient();
            if (!(currentClient instanceof EnumerateConsumer)) {
                Slog.e(this.mTag, "onEnrollmentsEnumerated for non-enumerate consumer: " + Utils.getClientName(currentClient));
                return;
            }
            EnumerateConsumer enumerateConsumer = (EnumerateConsumer) currentClient;
            if (iArr.length > 0) {
                for (int i = 0; i < iArr.length; i++) {
                    enumerateConsumer.onEnumerationResult(new Fingerprint("", iArr[i], this.mSensorId), (iArr.length - i) - 1);
                }
                return;
            }
            enumerateConsumer.onEnumerationResult(null, 0);
        }

        public void onEnrollmentsRemoved(final int[] iArr) {
            this.mHandler.post(new Runnable() { // from class: com.android.server.biometrics.sensors.fingerprint.aidl.Sensor$HalSessionCallback$$ExternalSyntheticLambda14
                @Override // java.lang.Runnable
                public final void run() {
                    Sensor.HalSessionCallback.this.lambda$onEnrollmentsRemoved$12(iArr);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* JADX WARN: Multi-variable type inference failed */
        public /* synthetic */ void lambda$onEnrollmentsRemoved$12(int[] iArr) {
            BaseClientMonitor currentClient = this.mScheduler.getCurrentClient();
            if (!(currentClient instanceof RemovalConsumer)) {
                Slog.e(this.mTag, "onRemoved for non-removal consumer: " + Utils.getClientName(currentClient));
                return;
            }
            RemovalConsumer removalConsumer = (RemovalConsumer) currentClient;
            if (iArr.length > 0) {
                for (int i = 0; i < iArr.length; i++) {
                    removalConsumer.onRemoved(new Fingerprint("", iArr[i], this.mSensorId), (iArr.length - i) - 1);
                }
                return;
            }
            removalConsumer.onRemoved(null, 0);
        }

        public void onAuthenticatorIdRetrieved(final long j) {
            this.mHandler.post(new Runnable() { // from class: com.android.server.biometrics.sensors.fingerprint.aidl.Sensor$HalSessionCallback$$ExternalSyntheticLambda9
                @Override // java.lang.Runnable
                public final void run() {
                    Sensor.HalSessionCallback.this.lambda$onAuthenticatorIdRetrieved$13(j);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onAuthenticatorIdRetrieved$13(long j) {
            BaseClientMonitor currentClient = this.mScheduler.getCurrentClient();
            if (!(currentClient instanceof FingerprintGetAuthenticatorIdClient)) {
                Slog.e(this.mTag, "onAuthenticatorIdRetrieved for wrong consumer: " + Utils.getClientName(currentClient));
                return;
            }
            ((FingerprintGetAuthenticatorIdClient) currentClient).onAuthenticatorIdRetrieved(j);
        }

        public void onAuthenticatorIdInvalidated(final long j) {
            this.mHandler.post(new Runnable() { // from class: com.android.server.biometrics.sensors.fingerprint.aidl.Sensor$HalSessionCallback$$ExternalSyntheticLambda3
                @Override // java.lang.Runnable
                public final void run() {
                    Sensor.HalSessionCallback.this.lambda$onAuthenticatorIdInvalidated$14(j);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onAuthenticatorIdInvalidated$14(long j) {
            BaseClientMonitor currentClient = this.mScheduler.getCurrentClient();
            if (!(currentClient instanceof FingerprintInvalidationClient)) {
                Slog.e(this.mTag, "onAuthenticatorIdInvalidated for wrong consumer: " + Utils.getClientName(currentClient));
                return;
            }
            ((FingerprintInvalidationClient) currentClient).onAuthenticatorIdInvalidated(j);
        }

        public void onSessionClosed() {
            Handler handler = this.mHandler;
            UserAwareBiometricScheduler userAwareBiometricScheduler = this.mScheduler;
            Objects.requireNonNull(userAwareBiometricScheduler);
            handler.post(new com.android.server.biometrics.sensors.face.aidl.Sensor$HalSessionCallback$$ExternalSyntheticLambda2(userAwareBiometricScheduler));
        }

        public boolean onTransact(int i, Parcel parcel, Parcel parcel2, int i2) throws RemoteException {
            if (super.onTransact(i, parcel, parcel2, i2)) {
                return true;
            }
            IFingerprintSensorExt iFingerprintSensorExt = Sensor.mFingerprintSensorExt;
            if (iFingerprintSensorExt != null && iFingerprintSensorExt.onTransactFromHal(i, parcel, parcel2, i2)) {
                return true;
            }
            Slog.d(this.mTag, "[onTransact]code " + i + " flags: " + i2);
            return false;
        }
    }

    Sensor(String str, FingerprintProvider fingerprintProvider, Context context, Handler handler, FingerprintSensorPropertiesInternal fingerprintSensorPropertiesInternal, LockoutResetDispatcher lockoutResetDispatcher, GestureAvailabilityDispatcher gestureAvailabilityDispatcher, BiometricContext biometricContext, AidlSession aidlSession) {
        this.mTag = str;
        this.mProvider = fingerprintProvider;
        mOplusProvider = fingerprintProvider;
        this.mContext = context;
        this.mToken = new Binder();
        this.mHandler = handler;
        this.mSensorProperties = fingerprintSensorPropertiesInternal;
        this.mLockoutCache = new LockoutCache();
        this.mScheduler = new UserAwareBiometricScheduler(str, BiometricScheduler.sensorTypeFromFingerprintProperties(fingerprintSensorPropertiesInternal), gestureAvailabilityDispatcher, new UserAwareBiometricScheduler.CurrentUserRetriever() { // from class: com.android.server.biometrics.sensors.fingerprint.aidl.Sensor$$ExternalSyntheticLambda0
            @Override // com.android.server.biometrics.sensors.UserAwareBiometricScheduler.CurrentUserRetriever
            public final int getCurrentUserId() {
                int lambda$new$0;
                lambda$new$0 = Sensor.this.lambda$new$0();
                return lambda$new$0;
            }
        }, new AnonymousClass1(biometricContext, lockoutResetDispatcher, fingerprintProvider));
        this.mAuthenticatorIds = new HashMap();
        this.mLazySession = new Supplier() { // from class: com.android.server.biometrics.sensors.fingerprint.aidl.Sensor$$ExternalSyntheticLambda1
            @Override // java.util.function.Supplier
            public final Object get() {
                AidlSession lambda$new$1;
                lambda$new$1 = Sensor.this.lambda$new$1();
                return lambda$new$1;
            }
        };
        this.mCurrentSession = aidlSession;
        mFingerprintSensorExt.init(context, fingerprintProvider);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: com.android.server.biometrics.sensors.fingerprint.aidl.Sensor$1, reason: invalid class name */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public class AnonymousClass1 implements UserAwareBiometricScheduler.UserSwitchCallback {
        final /* synthetic */ BiometricContext val$biometricContext;
        final /* synthetic */ LockoutResetDispatcher val$lockoutResetDispatcher;
        final /* synthetic */ FingerprintProvider val$provider;

        AnonymousClass1(BiometricContext biometricContext, LockoutResetDispatcher lockoutResetDispatcher, FingerprintProvider fingerprintProvider) {
            this.val$biometricContext = biometricContext;
            this.val$lockoutResetDispatcher = lockoutResetDispatcher;
            this.val$provider = fingerprintProvider;
        }

        @Override // com.android.server.biometrics.sensors.UserAwareBiometricScheduler.UserSwitchCallback
        public StopUserClient<?> getStopUserClient(int i) {
            return new FingerprintStopUserClient(Sensor.this.mContext, Sensor.this.mLazySession, Sensor.this.mToken, i, Sensor.this.mSensorProperties.sensorId, BiometricLogger.ofUnknown(Sensor.this.mContext), this.val$biometricContext, new StopUserClient.UserStoppedCallback() { // from class: com.android.server.biometrics.sensors.fingerprint.aidl.Sensor$1$$ExternalSyntheticLambda0
                @Override // com.android.server.biometrics.sensors.StopUserClient.UserStoppedCallback
                public final void onUserStopped() {
                    Sensor.AnonymousClass1.this.lambda$getStopUserClient$0();
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$getStopUserClient$0() {
            Sensor.this.mCurrentSession = null;
        }

        @Override // com.android.server.biometrics.sensors.UserAwareBiometricScheduler.UserSwitchCallback
        public StartUserClient<?, ?> getStartUserClient(int i) {
            final int i2 = Sensor.this.mSensorProperties.sensorId;
            final HalSessionCallback halSessionCallback = new HalSessionCallback(Sensor.this.mContext, Sensor.this.mHandler, Sensor.this.mTag, Sensor.this.mScheduler, i2, i, Sensor.this.mLockoutCache, this.val$lockoutResetDispatcher, this.val$biometricContext.getAuthSessionCoordinator(), new HalSessionCallback.Callback() { // from class: com.android.server.biometrics.sensors.fingerprint.aidl.Sensor$1$$ExternalSyntheticLambda1
                @Override // com.android.server.biometrics.sensors.fingerprint.aidl.Sensor.HalSessionCallback.Callback
                public final void onHardwareUnavailable() {
                    Sensor.AnonymousClass1.this.lambda$getStartUserClient$1();
                }
            });
            final FingerprintProvider fingerprintProvider = this.val$provider;
            StartUserClient.UserStartedCallback userStartedCallback = new StartUserClient.UserStartedCallback() { // from class: com.android.server.biometrics.sensors.fingerprint.aidl.Sensor$1$$ExternalSyntheticLambda2
                @Override // com.android.server.biometrics.sensors.StartUserClient.UserStartedCallback
                public final void onUserStarted(int i3, Object obj, int i4) {
                    Sensor.AnonymousClass1.this.lambda$getStartUserClient$2(halSessionCallback, i2, fingerprintProvider, i3, (ISession) obj, i4);
                }
            };
            Context context = Sensor.this.mContext;
            final FingerprintProvider fingerprintProvider2 = this.val$provider;
            Objects.requireNonNull(fingerprintProvider2);
            return new FingerprintStartUserClient(context, new Supplier() { // from class: com.android.server.biometrics.sensors.fingerprint.aidl.Sensor$1$$ExternalSyntheticLambda3
                @Override // java.util.function.Supplier
                public final Object get() {
                    return FingerprintProvider.this.getHalInstance();
                }
            }, Sensor.this.mToken, i, Sensor.this.mSensorProperties.sensorId, BiometricLogger.ofUnknown(Sensor.this.mContext), this.val$biometricContext, halSessionCallback, userStartedCallback);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$getStartUserClient$1() {
            Slog.e(Sensor.this.mTag, "Got ERROR_HW_UNAVAILABLE");
            Sensor.this.mCurrentSession = null;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$getStartUserClient$2(HalSessionCallback halSessionCallback, int i, FingerprintProvider fingerprintProvider, int i2, ISession iSession, int i3) {
            Slog.d(Sensor.this.mTag, "New session created for user: " + i2 + " with hal version: " + i3);
            Sensor.this.mCurrentSession = new AidlSession(i3, iSession, i2, halSessionCallback);
            if (FingerprintUtils.getInstance(i).isInvalidationInProgress(Sensor.this.mContext, i2)) {
                Slog.w(Sensor.this.mTag, "Scheduling unfinished invalidation request for sensor: " + i + ", user: " + i2);
                fingerprintProvider.scheduleInvalidationRequest(i, i2);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ int lambda$new$0() {
        AidlSession aidlSession = this.mCurrentSession;
        if (aidlSession != null) {
            return aidlSession.getUserId();
        }
        return -10000;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ AidlSession lambda$new$1() {
        AidlSession aidlSession = this.mCurrentSession;
        if (aidlSession != null) {
            return aidlSession;
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Sensor(String str, FingerprintProvider fingerprintProvider, Context context, Handler handler, FingerprintSensorPropertiesInternal fingerprintSensorPropertiesInternal, LockoutResetDispatcher lockoutResetDispatcher, GestureAvailabilityDispatcher gestureAvailabilityDispatcher, BiometricContext biometricContext) {
        this(str, fingerprintProvider, context, handler, fingerprintSensorPropertiesInternal, lockoutResetDispatcher, gestureAvailabilityDispatcher, biometricContext, null);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Supplier<AidlSession> getLazySession() {
        return this.mLazySession;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public FingerprintSensorPropertiesInternal getSensorProperties() {
        return this.mSensorProperties;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public AidlSession getSessionForUser(int i) {
        AidlSession aidlSession = this.mCurrentSession;
        if (aidlSession == null || aidlSession.getUserId() != i) {
            return null;
        }
        return this.mCurrentSession;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public ITestSession createTestSession(ITestSessionCallback iTestSessionCallback, BiometricStateCallback biometricStateCallback) {
        return new BiometricTestSessionImpl(this.mContext, this.mSensorProperties.sensorId, iTestSessionCallback, biometricStateCallback, this.mProvider, this);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public BiometricScheduler getScheduler() {
        return this.mScheduler;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public LockoutCache getLockoutCache() {
        return this.mLockoutCache;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Map<Integer, Long> getAuthenticatorIds() {
        return this.mAuthenticatorIds;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setTestHalEnabled(boolean z) {
        Slog.w(this.mTag, "setTestHalEnabled: " + z);
        if (z != this.mTestHalEnabled) {
            try {
                if (this.mCurrentSession != null) {
                    Slog.d(this.mTag, "Closing old session");
                    this.mCurrentSession.getSession().close();
                }
            } catch (RemoteException e) {
                Slog.e(this.mTag, "RemoteException", e);
            }
            this.mCurrentSession = null;
        }
        this.mTestHalEnabled = z;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void dumpProtoState(int i, ProtoOutputStream protoOutputStream, boolean z) {
        long start = protoOutputStream.start(2246267895809L);
        protoOutputStream.write(1120986464257L, this.mSensorProperties.sensorId);
        protoOutputStream.write(1159641169922L, 1);
        if (this.mSensorProperties.isAnyUdfpsType()) {
            protoOutputStream.write(2259152797704L, 0);
        }
        protoOutputStream.write(1120986464259L, Utils.getCurrentStrength(this.mSensorProperties.sensorId));
        protoOutputStream.write(1146756268036L, this.mScheduler.dumpProtoState(z));
        Iterator it = UserManager.get(this.mContext).getUsers().iterator();
        while (it.hasNext()) {
            int identifier = ((UserInfo) it.next()).getUserHandle().getIdentifier();
            long start2 = protoOutputStream.start(2246267895813L);
            protoOutputStream.write(1120986464257L, identifier);
            protoOutputStream.write(1120986464258L, FingerprintUtils.getInstance(this.mSensorProperties.sensorId).getBiometricsForUser(this.mContext, identifier).size());
            protoOutputStream.end(start2);
        }
        protoOutputStream.write(1133871366150L, this.mSensorProperties.resetLockoutRequiresHardwareAuthToken);
        protoOutputStream.write(1133871366151L, this.mSensorProperties.resetLockoutRequiresChallenge);
        protoOutputStream.end(start);
    }

    /* JADX WARN: Multi-variable type inference failed */
    public void onBinderDied() {
        BaseClientMonitor currentClient = this.mScheduler.getCurrentClient();
        if (currentClient instanceof ErrorConsumer) {
            Slog.e(this.mTag, "Sending ERROR_HW_UNAVAILABLE for client: " + currentClient);
            ((ErrorConsumer) currentClient).onError(1, 0);
            FrameworkStatsLog.write(148, 1, 1, -1);
        } else if (currentClient != 0) {
            currentClient.cancel();
        }
        this.mScheduler.recordCrashState();
        this.mScheduler.reset();
        this.mCurrentSession = null;
    }
}
