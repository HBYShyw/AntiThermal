package com.android.server.biometrics.sensors.face.aidl;

import android.content.Context;
import android.content.pm.UserInfo;
import android.hardware.biometrics.ITestSession;
import android.hardware.biometrics.ITestSessionCallback;
import android.hardware.biometrics.face.AuthenticationFrame;
import android.hardware.biometrics.face.EnrollmentFrame;
import android.hardware.biometrics.face.ISession;
import android.hardware.biometrics.face.ISessionCallback;
import android.hardware.face.Face;
import android.hardware.face.FaceSensorPropertiesInternal;
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
import com.android.server.biometrics.sensors.AuthSessionCoordinator;
import com.android.server.biometrics.sensors.AuthenticationConsumer;
import com.android.server.biometrics.sensors.BaseClientMonitor;
import com.android.server.biometrics.sensors.BiometricScheduler;
import com.android.server.biometrics.sensors.EnumerateConsumer;
import com.android.server.biometrics.sensors.ErrorConsumer;
import com.android.server.biometrics.sensors.LockoutCache;
import com.android.server.biometrics.sensors.LockoutConsumer;
import com.android.server.biometrics.sensors.LockoutResetDispatcher;
import com.android.server.biometrics.sensors.RemovalConsumer;
import com.android.server.biometrics.sensors.StartUserClient;
import com.android.server.biometrics.sensors.StopUserClient;
import com.android.server.biometrics.sensors.UserAwareBiometricScheduler;
import com.android.server.biometrics.sensors.face.FaceUtils;
import com.android.server.biometrics.sensors.face.aidl.Sensor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.function.Supplier;
import system.ext.loader.core.ExtLoader;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public class Sensor {
    private static final int FACE_ACQUIRED_VENDOR_IMAGA_BUFFER_SEQ = 1003;
    private static final int REMAINING_FOR_SIMILAR_FACE = 9999;
    private static int mImageBufferSeq;
    public static ISensorExt mOplusSensor;
    private final Map<Integer, Long> mAuthenticatorIds;
    private final Context mContext;
    AidlSession mCurrentSession;
    private final Handler mHandler;
    private final Supplier<AidlSession> mLazySession;
    private final LockoutCache mLockoutCache;
    private final FaceProvider mProvider;
    private final UserAwareBiometricScheduler mScheduler;
    private final FaceSensorPropertiesInternal mSensorProperties;
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

        @Override // android.hardware.biometrics.face.ISessionCallback
        public String getInterfaceHash() {
            return "fca1ab84dda6c013b251270d848eb6d964a6d765";
        }

        @Override // android.hardware.biometrics.face.ISessionCallback
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

        @Override // android.hardware.biometrics.face.ISessionCallback
        public void onChallengeGenerated(final long j) {
            this.mHandler.post(new Runnable() { // from class: com.android.server.biometrics.sensors.face.aidl.Sensor$HalSessionCallback$$ExternalSyntheticLambda9
                @Override // java.lang.Runnable
                public final void run() {
                    Sensor.HalSessionCallback.this.lambda$onChallengeGenerated$0(j);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onChallengeGenerated$0(long j) {
            BaseClientMonitor currentClient = this.mScheduler.getCurrentClient();
            if (!(currentClient instanceof FaceGenerateChallengeClient)) {
                Slog.e(this.mTag, "onChallengeGenerated for wrong client: " + Utils.getClientName(currentClient));
                return;
            }
            ((FaceGenerateChallengeClient) currentClient).onChallengeGenerated(this.mSensorId, this.mUserId, j);
        }

        @Override // android.hardware.biometrics.face.ISessionCallback
        public void onChallengeRevoked(final long j) {
            this.mHandler.post(new Runnable() { // from class: com.android.server.biometrics.sensors.face.aidl.Sensor$HalSessionCallback$$ExternalSyntheticLambda5
                @Override // java.lang.Runnable
                public final void run() {
                    Sensor.HalSessionCallback.this.lambda$onChallengeRevoked$1(j);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onChallengeRevoked$1(long j) {
            BaseClientMonitor currentClient = this.mScheduler.getCurrentClient();
            if (!(currentClient instanceof FaceRevokeChallengeClient)) {
                Slog.e(this.mTag, "onChallengeRevoked for wrong client: " + Utils.getClientName(currentClient));
                return;
            }
            ((FaceRevokeChallengeClient) currentClient).onChallengeRevoked(this.mSensorId, this.mUserId, j);
        }

        @Override // android.hardware.biometrics.face.ISessionCallback
        public void onAuthenticationFrame(final AuthenticationFrame authenticationFrame) {
            this.mHandler.post(new Runnable() { // from class: com.android.server.biometrics.sensors.face.aidl.Sensor$HalSessionCallback$$ExternalSyntheticLambda14
                @Override // java.lang.Runnable
                public final void run() {
                    Sensor.HalSessionCallback.this.lambda$onAuthenticationFrame$2(authenticationFrame);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onAuthenticationFrame$2(AuthenticationFrame authenticationFrame) {
            BaseClientMonitor currentClient = this.mScheduler.getCurrentClient();
            if (!(currentClient instanceof FaceAuthenticationClient)) {
                Slog.e(this.mTag, "onAuthenticationFrame for incompatible client: " + Utils.getClientName(currentClient));
                return;
            }
            if (authenticationFrame == null) {
                Slog.e(this.mTag, "Received null authentication frame for client: " + Utils.getClientName(currentClient));
                return;
            }
            Sensor.mImageBufferSeq++;
            Sensor.mOplusSensor.onAcquired(Sensor.mImageBufferSeq, 1003);
            if (Sensor.mOplusSensor.onAcquired(AidlConversionUtils.toFrameworkAuthenticationFrame(authenticationFrame).getData().getAcquiredInfo(), AidlConversionUtils.toFrameworkAuthenticationFrame(authenticationFrame).getData().getVendorCode())) {
                Slog.d(this.mTag, "skip authentication frame return");
            } else {
                ((FaceAuthenticationClient) currentClient).onAuthenticationFrame(AidlConversionUtils.toFrameworkAuthenticationFrame(authenticationFrame));
            }
        }

        @Override // android.hardware.biometrics.face.ISessionCallback
        public void onEnrollmentFrame(final EnrollmentFrame enrollmentFrame) {
            this.mHandler.post(new Runnable() { // from class: com.android.server.biometrics.sensors.face.aidl.Sensor$HalSessionCallback$$ExternalSyntheticLambda16
                @Override // java.lang.Runnable
                public final void run() {
                    Sensor.HalSessionCallback.this.lambda$onEnrollmentFrame$3(enrollmentFrame);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onEnrollmentFrame$3(EnrollmentFrame enrollmentFrame) {
            BaseClientMonitor currentClient = this.mScheduler.getCurrentClient();
            if (!(currentClient instanceof FaceEnrollClient)) {
                Slog.e(this.mTag, "onEnrollmentFrame for incompatible client: " + Utils.getClientName(currentClient));
                return;
            }
            if (enrollmentFrame == null) {
                Slog.e(this.mTag, "Received null enrollment frame for client: " + Utils.getClientName(currentClient));
                return;
            }
            if (Sensor.mOplusSensor.onAcquired(AidlConversionUtils.toFrameworkEnrollmentFrame(enrollmentFrame).getData().getAcquiredInfo(), AidlConversionUtils.toFrameworkEnrollmentFrame(enrollmentFrame).getData().getVendorCode())) {
                Slog.d(this.mTag, "skip enrollment frame return");
            } else {
                ((FaceEnrollClient) currentClient).onEnrollmentFrame(AidlConversionUtils.toFrameworkEnrollmentFrame(enrollmentFrame));
            }
        }

        @Override // android.hardware.biometrics.face.ISessionCallback
        public void onError(final byte b, final int i) {
            this.mHandler.post(new Runnable() { // from class: com.android.server.biometrics.sensors.face.aidl.Sensor$HalSessionCallback$$ExternalSyntheticLambda8
                @Override // java.lang.Runnable
                public final void run() {
                    Sensor.HalSessionCallback.this.lambda$onError$4(b, i);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* JADX WARN: Multi-variable type inference failed */
        public /* synthetic */ void lambda$onError$4(byte b, int i) {
            BaseClientMonitor currentClient = this.mScheduler.getCurrentClient();
            Slog.d(this.mTag, "onError, client: " + Utils.getClientName(currentClient) + ", error: " + ((int) b) + ", vendorCode: " + i);
            if (!(currentClient instanceof ErrorConsumer)) {
                Slog.e(this.mTag, "onError for non-error consumer: " + Utils.getClientName(currentClient));
                return;
            }
            ((ErrorConsumer) currentClient).onError(AidlConversionUtils.toFrameworkError(b), i);
            if (b == 1) {
                this.mCallback.onHardwareUnavailable();
            }
            Sensor.mImageBufferSeq = 0;
            Sensor.mOplusSensor.onError(AidlConversionUtils.toFrameworkError(b), i);
        }

        @Override // android.hardware.biometrics.face.ISessionCallback
        public void onEnrollmentProgress(final int i, final int i2) {
            this.mHandler.post(new Runnable() { // from class: com.android.server.biometrics.sensors.face.aidl.Sensor$HalSessionCallback$$ExternalSyntheticLambda18
                @Override // java.lang.Runnable
                public final void run() {
                    Sensor.HalSessionCallback.this.lambda$onEnrollmentProgress$5(i2, i);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onEnrollmentProgress$5(int i, int i2) {
            BaseClientMonitor currentClient = this.mScheduler.getCurrentClient();
            if (currentClient instanceof FaceEnrollClient) {
                if (i == Sensor.REMAINING_FOR_SIMILAR_FACE) {
                    return;
                }
                ((FaceEnrollClient) currentClient).onEnrollResult(new Face(FaceUtils.getInstance(this.mSensorId).getUniqueName(this.mContext, currentClient.getTargetUserId()), i2, this.mSensorId), i);
                return;
            }
            Slog.e(this.mTag, "onEnrollmentProgress for non-enroll client: " + Utils.getClientName(currentClient));
        }

        @Override // android.hardware.biometrics.face.ISessionCallback
        public void onAuthenticationSucceeded(final int i, final HardwareAuthToken hardwareAuthToken) {
            this.mHandler.post(new Runnable() { // from class: com.android.server.biometrics.sensors.face.aidl.Sensor$HalSessionCallback$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    Sensor.HalSessionCallback.this.lambda$onAuthenticationSucceeded$6(i, hardwareAuthToken);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* JADX WARN: Multi-variable type inference failed */
        public /* synthetic */ void lambda$onAuthenticationSucceeded$6(int i, HardwareAuthToken hardwareAuthToken) {
            BaseClientMonitor currentClient = this.mScheduler.getCurrentClient();
            if (!(currentClient instanceof AuthenticationConsumer)) {
                Slog.e(this.mTag, "onAuthenticationSucceeded for non-authentication consumer: " + Utils.getClientName(currentClient));
                return;
            }
            AuthenticationConsumer authenticationConsumer = (AuthenticationConsumer) currentClient;
            Face face = new Face("", i, this.mSensorId);
            byte[] byteArray = HardwareAuthTokenUtils.toByteArray(hardwareAuthToken);
            ArrayList<Byte> arrayList = new ArrayList<>();
            for (byte b : byteArray) {
                arrayList.add(Byte.valueOf(b));
            }
            authenticationConsumer.onAuthenticated(face, true, arrayList);
            Sensor.mImageBufferSeq = 0;
            Sensor.mOplusSensor.onAuthenticated(true);
        }

        @Override // android.hardware.biometrics.face.ISessionCallback
        public void onAuthenticationFailed() {
            this.mHandler.post(new Runnable() { // from class: com.android.server.biometrics.sensors.face.aidl.Sensor$HalSessionCallback$$ExternalSyntheticLambda4
                @Override // java.lang.Runnable
                public final void run() {
                    Sensor.HalSessionCallback.this.lambda$onAuthenticationFailed$7();
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* JADX WARN: Multi-variable type inference failed */
        public /* synthetic */ void lambda$onAuthenticationFailed$7() {
            BaseClientMonitor currentClient = this.mScheduler.getCurrentClient();
            if (!(currentClient instanceof AuthenticationConsumer)) {
                Slog.e(this.mTag, "onAuthenticationFailed for non-authentication consumer: " + Utils.getClientName(currentClient));
                return;
            }
            ((AuthenticationConsumer) currentClient).onAuthenticated(new Face("", 0, this.mSensorId), false, null);
            Sensor.mImageBufferSeq = 0;
            Sensor.mOplusSensor.onAuthenticated(false);
        }

        @Override // android.hardware.biometrics.face.ISessionCallback
        public void onLockoutTimed(final long j) {
            this.mHandler.post(new Runnable() { // from class: com.android.server.biometrics.sensors.face.aidl.Sensor$HalSessionCallback$$ExternalSyntheticLambda12
                @Override // java.lang.Runnable
                public final void run() {
                    Sensor.HalSessionCallback.this.lambda$onLockoutTimed$8(j);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* JADX WARN: Multi-variable type inference failed */
        public /* synthetic */ void lambda$onLockoutTimed$8(long j) {
            BaseClientMonitor currentClient = this.mScheduler.getCurrentClient();
            if (!(currentClient instanceof LockoutConsumer)) {
                Slog.e(this.mTag, "onLockoutTimed for non-lockout consumer: " + Utils.getClientName(currentClient));
                return;
            }
            Sensor.mOplusSensor.onLockoutTimed();
            ((LockoutConsumer) currentClient).onLockoutTimed(j);
        }

        @Override // android.hardware.biometrics.face.ISessionCallback
        public void onLockoutPermanent() {
            this.mHandler.post(new Runnable() { // from class: com.android.server.biometrics.sensors.face.aidl.Sensor$HalSessionCallback$$ExternalSyntheticLambda3
                @Override // java.lang.Runnable
                public final void run() {
                    Sensor.HalSessionCallback.this.lambda$onLockoutPermanent$9();
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* JADX WARN: Multi-variable type inference failed */
        public /* synthetic */ void lambda$onLockoutPermanent$9() {
            BaseClientMonitor currentClient = this.mScheduler.getCurrentClient();
            if (!(currentClient instanceof LockoutConsumer)) {
                Slog.e(this.mTag, "onLockoutPermanent for non-lockout consumer: " + Utils.getClientName(currentClient));
                return;
            }
            Sensor.mOplusSensor.onLockoutPermanent();
            ((LockoutConsumer) currentClient).onLockoutPermanent();
        }

        @Override // android.hardware.biometrics.face.ISessionCallback
        public void onLockoutCleared() {
            this.mHandler.post(new Runnable() { // from class: com.android.server.biometrics.sensors.face.aidl.Sensor$HalSessionCallback$$ExternalSyntheticLambda11
                @Override // java.lang.Runnable
                public final void run() {
                    Sensor.HalSessionCallback.this.lambda$onLockoutCleared$10();
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onLockoutCleared$10() {
            BaseClientMonitor currentClient = this.mScheduler.getCurrentClient();
            if (!(currentClient instanceof FaceResetLockoutClient)) {
                Slog.d(this.mTag, "onLockoutCleared outside of resetLockout by HAL");
                int i = this.mSensorId;
                FaceResetLockoutClient.resetLocalLockoutStateToNone(i, this.mUserId, this.mLockoutCache, this.mLockoutResetDispatcher, this.mAuthSessionCoordinator, Utils.getCurrentStrength(i), -1L);
            } else {
                Slog.d(this.mTag, "onLockoutCleared after resetLockout");
                ((FaceResetLockoutClient) currentClient).onLockoutCleared();
            }
        }

        @Override // android.hardware.biometrics.face.ISessionCallback
        public void onInteractionDetected() {
            this.mHandler.post(new Runnable() { // from class: com.android.server.biometrics.sensors.face.aidl.Sensor$HalSessionCallback$$ExternalSyntheticLambda15
                @Override // java.lang.Runnable
                public final void run() {
                    Sensor.HalSessionCallback.this.lambda$onInteractionDetected$11();
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onInteractionDetected$11() {
            BaseClientMonitor currentClient = this.mScheduler.getCurrentClient();
            if (!(currentClient instanceof FaceDetectClient)) {
                Slog.e(this.mTag, "onInteractionDetected for wrong client: " + Utils.getClientName(currentClient));
                return;
            }
            ((FaceDetectClient) currentClient).onInteractionDetected();
        }

        @Override // android.hardware.biometrics.face.ISessionCallback
        public void onEnrollmentsEnumerated(final int[] iArr) {
            this.mHandler.post(new Runnable() { // from class: com.android.server.biometrics.sensors.face.aidl.Sensor$HalSessionCallback$$ExternalSyntheticLambda17
                @Override // java.lang.Runnable
                public final void run() {
                    Sensor.HalSessionCallback.this.lambda$onEnrollmentsEnumerated$12(iArr);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* JADX WARN: Multi-variable type inference failed */
        public /* synthetic */ void lambda$onEnrollmentsEnumerated$12(int[] iArr) {
            BaseClientMonitor currentClient = this.mScheduler.getCurrentClient();
            if (!(currentClient instanceof EnumerateConsumer)) {
                Slog.e(this.mTag, "onEnrollmentsEnumerated for non-enumerate consumer: " + Utils.getClientName(currentClient));
                return;
            }
            EnumerateConsumer enumerateConsumer = (EnumerateConsumer) currentClient;
            if (iArr.length > 0) {
                for (int i = 0; i < iArr.length; i++) {
                    enumerateConsumer.onEnumerationResult(new Face("", iArr[i], this.mSensorId), (iArr.length - i) - 1);
                }
                return;
            }
            enumerateConsumer.onEnumerationResult(null, 0);
        }

        @Override // android.hardware.biometrics.face.ISessionCallback
        public void onFeaturesRetrieved(final byte[] bArr) {
            this.mHandler.post(new Runnable() { // from class: com.android.server.biometrics.sensors.face.aidl.Sensor$HalSessionCallback$$ExternalSyntheticLambda1
                @Override // java.lang.Runnable
                public final void run() {
                    Sensor.HalSessionCallback.this.lambda$onFeaturesRetrieved$13(bArr);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onFeaturesRetrieved$13(byte[] bArr) {
            BaseClientMonitor currentClient = this.mScheduler.getCurrentClient();
            if (!(currentClient instanceof FaceGetFeatureClient)) {
                Slog.e(this.mTag, "onFeaturesRetrieved for non-get feature consumer: " + Utils.getClientName(currentClient));
                return;
            }
            ((FaceGetFeatureClient) currentClient).onFeatureGet(true, bArr);
        }

        @Override // android.hardware.biometrics.face.ISessionCallback
        public void onFeatureSet(byte b) {
            this.mHandler.post(new Runnable() { // from class: com.android.server.biometrics.sensors.face.aidl.Sensor$HalSessionCallback$$ExternalSyntheticLambda10
                @Override // java.lang.Runnable
                public final void run() {
                    Sensor.HalSessionCallback.this.lambda$onFeatureSet$14();
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onFeatureSet$14() {
            BaseClientMonitor currentClient = this.mScheduler.getCurrentClient();
            if (!(currentClient instanceof FaceSetFeatureClient)) {
                Slog.e(this.mTag, "onFeatureSet for non-set consumer: " + Utils.getClientName(currentClient));
                return;
            }
            ((FaceSetFeatureClient) currentClient).onFeatureSet(true);
        }

        @Override // android.hardware.biometrics.face.ISessionCallback
        public void onEnrollmentsRemoved(final int[] iArr) {
            this.mHandler.post(new Runnable() { // from class: com.android.server.biometrics.sensors.face.aidl.Sensor$HalSessionCallback$$ExternalSyntheticLambda7
                @Override // java.lang.Runnable
                public final void run() {
                    Sensor.HalSessionCallback.this.lambda$onEnrollmentsRemoved$15(iArr);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* JADX WARN: Multi-variable type inference failed */
        public /* synthetic */ void lambda$onEnrollmentsRemoved$15(int[] iArr) {
            BaseClientMonitor currentClient = this.mScheduler.getCurrentClient();
            if (!(currentClient instanceof RemovalConsumer)) {
                Slog.e(this.mTag, "onRemoved for non-removal consumer: " + Utils.getClientName(currentClient));
                return;
            }
            RemovalConsumer removalConsumer = (RemovalConsumer) currentClient;
            if (iArr.length > 0) {
                for (int i = 0; i < iArr.length; i++) {
                    removalConsumer.onRemoved(new Face("", iArr[i], this.mSensorId), (iArr.length - i) - 1);
                }
                return;
            }
            removalConsumer.onRemoved(null, 0);
        }

        @Override // android.hardware.biometrics.face.ISessionCallback
        public void onAuthenticatorIdRetrieved(final long j) {
            this.mHandler.post(new Runnable() { // from class: com.android.server.biometrics.sensors.face.aidl.Sensor$HalSessionCallback$$ExternalSyntheticLambda13
                @Override // java.lang.Runnable
                public final void run() {
                    Sensor.HalSessionCallback.this.lambda$onAuthenticatorIdRetrieved$16(j);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onAuthenticatorIdRetrieved$16(long j) {
            BaseClientMonitor currentClient = this.mScheduler.getCurrentClient();
            if (!(currentClient instanceof FaceGetAuthenticatorIdClient)) {
                Slog.e(this.mTag, "onAuthenticatorIdRetrieved for wrong consumer: " + Utils.getClientName(currentClient));
                return;
            }
            ((FaceGetAuthenticatorIdClient) currentClient).onAuthenticatorIdRetrieved(j);
        }

        @Override // android.hardware.biometrics.face.ISessionCallback
        public void onAuthenticatorIdInvalidated(final long j) {
            this.mHandler.post(new Runnable() { // from class: com.android.server.biometrics.sensors.face.aidl.Sensor$HalSessionCallback$$ExternalSyntheticLambda6
                @Override // java.lang.Runnable
                public final void run() {
                    Sensor.HalSessionCallback.this.lambda$onAuthenticatorIdInvalidated$17(j);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onAuthenticatorIdInvalidated$17(long j) {
            BaseClientMonitor currentClient = this.mScheduler.getCurrentClient();
            if (!(currentClient instanceof FaceInvalidationClient)) {
                Slog.e(this.mTag, "onAuthenticatorIdInvalidated for wrong consumer: " + Utils.getClientName(currentClient));
                return;
            }
            ((FaceInvalidationClient) currentClient).onAuthenticatorIdInvalidated(j);
        }

        @Override // android.hardware.biometrics.face.ISessionCallback
        public void onSessionClosed() {
            Handler handler = this.mHandler;
            UserAwareBiometricScheduler userAwareBiometricScheduler = this.mScheduler;
            Objects.requireNonNull(userAwareBiometricScheduler);
            handler.post(new Sensor$HalSessionCallback$$ExternalSyntheticLambda2(userAwareBiometricScheduler));
        }

        @Override // android.hardware.biometrics.face.ISessionCallback.Stub, android.os.Binder
        public boolean onTransact(int i, Parcel parcel, Parcel parcel2, int i2) throws RemoteException {
            if (super.onTransact(i, parcel, parcel2, i2)) {
                return true;
            }
            ISensorExt iSensorExt = Sensor.mOplusSensor;
            if (iSensorExt != null && iSensorExt.onTransactFromHal(i, parcel, parcel2, i2)) {
                return true;
            }
            Slog.d(this.mTag, "[onTransact]code " + i + " flags: " + i2);
            return false;
        }
    }

    Sensor(String str, FaceProvider faceProvider, Context context, Handler handler, FaceSensorPropertiesInternal faceSensorPropertiesInternal, LockoutResetDispatcher lockoutResetDispatcher, BiometricContext biometricContext, AidlSession aidlSession) {
        this.mTag = str;
        this.mProvider = faceProvider;
        this.mContext = context;
        this.mToken = new Binder();
        this.mHandler = handler;
        this.mSensorProperties = faceSensorPropertiesInternal;
        this.mScheduler = new UserAwareBiometricScheduler(str, 1, null, new UserAwareBiometricScheduler.CurrentUserRetriever() { // from class: com.android.server.biometrics.sensors.face.aidl.Sensor$$ExternalSyntheticLambda0
            @Override // com.android.server.biometrics.sensors.UserAwareBiometricScheduler.CurrentUserRetriever
            public final int getCurrentUserId() {
                int lambda$new$0;
                lambda$new$0 = Sensor.this.lambda$new$0();
                return lambda$new$0;
            }
        }, new AnonymousClass1(biometricContext, lockoutResetDispatcher, faceProvider));
        this.mLockoutCache = new LockoutCache();
        this.mAuthenticatorIds = new HashMap();
        this.mLazySession = new Supplier() { // from class: com.android.server.biometrics.sensors.face.aidl.Sensor$$ExternalSyntheticLambda1
            @Override // java.util.function.Supplier
            public final Object get() {
                AidlSession lambda$new$1;
                lambda$new$1 = Sensor.this.lambda$new$1();
                return lambda$new$1;
            }
        };
        ISensorExt iSensorExt = (ISensorExt) ExtLoader.type(ISensorExt.class).base(this).create();
        mOplusSensor = iSensorExt;
        iSensorExt.init(context, faceProvider);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: com.android.server.biometrics.sensors.face.aidl.Sensor$1, reason: invalid class name */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public class AnonymousClass1 implements UserAwareBiometricScheduler.UserSwitchCallback {
        final /* synthetic */ BiometricContext val$biometricContext;
        final /* synthetic */ LockoutResetDispatcher val$lockoutResetDispatcher;
        final /* synthetic */ FaceProvider val$provider;

        AnonymousClass1(BiometricContext biometricContext, LockoutResetDispatcher lockoutResetDispatcher, FaceProvider faceProvider) {
            this.val$biometricContext = biometricContext;
            this.val$lockoutResetDispatcher = lockoutResetDispatcher;
            this.val$provider = faceProvider;
        }

        @Override // com.android.server.biometrics.sensors.UserAwareBiometricScheduler.UserSwitchCallback
        public StopUserClient<?> getStopUserClient(int i) {
            return new FaceStopUserClient(Sensor.this.mContext, Sensor.this.mLazySession, Sensor.this.mToken, i, Sensor.this.mSensorProperties.sensorId, BiometricLogger.ofUnknown(Sensor.this.mContext), this.val$biometricContext, new StopUserClient.UserStoppedCallback() { // from class: com.android.server.biometrics.sensors.face.aidl.Sensor$1$$ExternalSyntheticLambda0
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
            final HalSessionCallback halSessionCallback = new HalSessionCallback(Sensor.this.mContext, Sensor.this.mHandler, Sensor.this.mTag, Sensor.this.mScheduler, i2, i, Sensor.this.mLockoutCache, this.val$lockoutResetDispatcher, this.val$biometricContext.getAuthSessionCoordinator(), new HalSessionCallback.Callback() { // from class: com.android.server.biometrics.sensors.face.aidl.Sensor$1$$ExternalSyntheticLambda1
                @Override // com.android.server.biometrics.sensors.face.aidl.Sensor.HalSessionCallback.Callback
                public final void onHardwareUnavailable() {
                    Sensor.AnonymousClass1.this.lambda$getStartUserClient$1();
                }
            });
            final FaceProvider faceProvider = this.val$provider;
            StartUserClient.UserStartedCallback userStartedCallback = new StartUserClient.UserStartedCallback() { // from class: com.android.server.biometrics.sensors.face.aidl.Sensor$1$$ExternalSyntheticLambda2
                @Override // com.android.server.biometrics.sensors.StartUserClient.UserStartedCallback
                public final void onUserStarted(int i3, Object obj, int i4) {
                    Sensor.AnonymousClass1.this.lambda$getStartUserClient$2(halSessionCallback, i2, faceProvider, i3, (ISession) obj, i4);
                }
            };
            Context context = Sensor.this.mContext;
            final FaceProvider faceProvider2 = this.val$provider;
            Objects.requireNonNull(faceProvider2);
            return new FaceStartUserClient(context, new Supplier() { // from class: com.android.server.biometrics.sensors.face.aidl.Sensor$1$$ExternalSyntheticLambda3
                @Override // java.util.function.Supplier
                public final Object get() {
                    return FaceProvider.this.getHalInstance();
                }
            }, Sensor.this.mToken, i, Sensor.this.mSensorProperties.sensorId, BiometricLogger.ofUnknown(Sensor.this.mContext), this.val$biometricContext, halSessionCallback, userStartedCallback);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$getStartUserClient$1() {
            Slog.e(Sensor.this.mTag, "Got ERROR_HW_UNAVAILABLE");
            Sensor.this.mCurrentSession = null;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$getStartUserClient$2(HalSessionCallback halSessionCallback, int i, FaceProvider faceProvider, int i2, ISession iSession, int i3) {
            Slog.d(Sensor.this.mTag, "New session created for user: " + i2 + " with hal version: " + i3);
            Sensor.this.mCurrentSession = new AidlSession(i3, iSession, i2, halSessionCallback);
            if (FaceUtils.getLegacyInstance(i).isInvalidationInProgress(Sensor.this.mContext, i2)) {
                Slog.w(Sensor.this.mTag, "Scheduling unfinished invalidation request for sensor: " + i + ", user: " + i2);
                faceProvider.scheduleInvalidationRequest(i, i2);
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
    public Sensor(String str, FaceProvider faceProvider, Context context, Handler handler, FaceSensorPropertiesInternal faceSensorPropertiesInternal, LockoutResetDispatcher lockoutResetDispatcher, BiometricContext biometricContext) {
        this(str, faceProvider, context, handler, faceSensorPropertiesInternal, lockoutResetDispatcher, biometricContext, null);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Supplier<AidlSession> getLazySession() {
        return this.mLazySession;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public FaceSensorPropertiesInternal getSensorProperties() {
        return this.mSensorProperties;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @VisibleForTesting
    public AidlSession getSessionForUser(int i) {
        AidlSession aidlSession = this.mCurrentSession;
        if (aidlSession == null || aidlSession.getUserId() != i) {
            return null;
        }
        return this.mCurrentSession;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public ITestSession createTestSession(ITestSessionCallback iTestSessionCallback) {
        return new BiometricTestSessionImpl(this.mContext, this.mSensorProperties.sensorId, iTestSessionCallback, this.mProvider, this);
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
        protoOutputStream.write(1159641169922L, 2);
        protoOutputStream.write(1120986464259L, Utils.getCurrentStrength(this.mSensorProperties.sensorId));
        protoOutputStream.write(1146756268036L, this.mScheduler.dumpProtoState(z));
        Iterator it = UserManager.get(this.mContext).getUsers().iterator();
        while (it.hasNext()) {
            int identifier = ((UserInfo) it.next()).getUserHandle().getIdentifier();
            long start2 = protoOutputStream.start(2246267895813L);
            protoOutputStream.write(1120986464257L, identifier);
            protoOutputStream.write(1120986464258L, FaceUtils.getInstance(this.mSensorProperties.sensorId).getBiometricsForUser(this.mContext, identifier).size());
            protoOutputStream.end(start2);
        }
        protoOutputStream.write(1133871366150L, this.mSensorProperties.resetLockoutRequiresHardwareAuthToken);
        protoOutputStream.write(1133871366151L, this.mSensorProperties.resetLockoutRequiresChallenge);
        protoOutputStream.end(start);
    }

    /* JADX WARN: Multi-variable type inference failed */
    public void onBinderDied() {
        BaseClientMonitor currentClient = this.mScheduler.getCurrentClient();
        if (currentClient != 0 && currentClient.isInterruptable()) {
            Slog.e(this.mTag, "Sending ERROR_HW_UNAVAILABLE for client: " + currentClient);
            ((ErrorConsumer) currentClient).onError(1, 0);
            FrameworkStatsLog.write(148, 4, 1, -1);
        } else if (currentClient != 0) {
            currentClient.cancel();
        }
        this.mScheduler.recordCrashState();
        this.mScheduler.reset();
        this.mCurrentSession = null;
    }
}
