package com.android.server.biometrics.sensors.fingerprint.hidl;

import android.R;
import android.app.trust.TrustManager;
import android.content.ContentResolver;
import android.content.Context;
import android.hardware.biometrics.fingerprint.PointerContext;
import android.hardware.fingerprint.FingerprintAuthenticateOptions;
import android.hardware.fingerprint.FingerprintSensorPropertiesInternal;
import android.hardware.fingerprint.IUdfpsOverlayController;
import android.os.Handler;
import android.os.IBinder;
import android.os.RemoteException;
import android.provider.Settings;
import android.util.Slog;
import android.util.SparseBooleanArray;
import com.android.server.IDeviceIdleControllerExt;
import com.android.server.ServiceThread;
import com.android.server.biometrics.log.BiometricContext;
import com.android.server.biometrics.sensors.AuthenticationConsumer;
import com.android.server.biometrics.sensors.BaseClientMonitor;
import com.android.server.biometrics.sensors.BiometricScheduler;
import com.android.server.biometrics.sensors.BiometricStateCallback;
import com.android.server.biometrics.sensors.ClientMonitorCallbackConverter;
import com.android.server.biometrics.sensors.LockoutResetDispatcher;
import com.android.server.biometrics.sensors.fingerprint.GestureAvailabilityDispatcher;
import com.android.server.biometrics.sensors.fingerprint.hidl.Fingerprint21;
import com.android.server.biometrics.sensors.fingerprint.hidl.Fingerprint21UdfpsMock;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public class Fingerprint21UdfpsMock extends Fingerprint21 implements TrustManager.TrustListener {
    private static final String CONFIG_AUTH_DELAY_PT1 = "com.android.server.biometrics.sensors.fingerprint.test_udfps.auth_delay_pt1";
    private static final String CONFIG_AUTH_DELAY_PT2 = "com.android.server.biometrics.sensors.fingerprint.test_udfps.auth_delay_pt2";
    private static final String CONFIG_AUTH_DELAY_RANDOMNESS = "com.android.server.biometrics.sensors.fingerprint.test_udfps.auth_delay_randomness";
    public static final String CONFIG_ENABLE_TEST_UDFPS = "com.android.server.biometrics.sensors.fingerprint.test_udfps.enable";
    private static final int DEFAULT_AUTH_DELAY_PT1_MS = 300;
    private static final int DEFAULT_AUTH_DELAY_PT2_MS = 400;
    private static final int DEFAULT_AUTH_DELAY_RANDOMNESS_MS = 100;
    private static final String TAG = "Fingerprint21UdfpsMock";
    private final FakeAcceptRunnable mFakeAcceptRunnable;
    private final FakeRejectRunnable mFakeRejectRunnable;
    private final Handler mHandler;
    private final MockHalResultController mMockHalResultController;
    private final Random mRandom;
    private final RestartAuthRunnable mRestartAuthRunnable;
    private final TestableBiometricScheduler mScheduler;
    private final FingerprintSensorPropertiesInternal mSensorProperties;
    private final TrustManager mTrustManager;
    private final SparseBooleanArray mUserHasTrust;

    public void onEnabledTrustAgentsChanged(int i) {
    }

    public void onIsActiveUnlockRunningChanged(boolean z, int i) {
    }

    public void onTrustError(CharSequence charSequence) {
    }

    public void onTrustManagedChanged(boolean z, int i) {
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public static class TestableBiometricScheduler extends BiometricScheduler {
        private Fingerprint21UdfpsMock mFingerprint21;

        TestableBiometricScheduler(String str, Handler handler, GestureAvailabilityDispatcher gestureAvailabilityDispatcher) {
            super(str, 3, gestureAvailabilityDispatcher);
        }

        void init(Fingerprint21UdfpsMock fingerprint21UdfpsMock) {
            this.mFingerprint21 = fingerprint21UdfpsMock;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public static class MockHalResultController extends Fingerprint21.HalResultController {
        private static final int AUTH_VALIDITY_MS = 10000;
        private Fingerprint21UdfpsMock mFingerprint21;
        private LastAuthArgs mLastAuthArgs;
        private RestartAuthRunnable mRestartAuthRunnable;

        /* JADX INFO: Access modifiers changed from: package-private */
        /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
        public static class LastAuthArgs {
            final long deviceId;
            final int fingerId;
            final int groupId;
            final AuthenticationConsumer lastAuthenticatedClient;
            final ArrayList<Byte> token;

            LastAuthArgs(AuthenticationConsumer authenticationConsumer, long j, int i, int i2, ArrayList<Byte> arrayList) {
                this.lastAuthenticatedClient = authenticationConsumer;
                this.deviceId = j;
                this.fingerId = i;
                this.groupId = i2;
                if (arrayList == null) {
                    this.token = null;
                } else {
                    this.token = new ArrayList<>(arrayList);
                }
            }
        }

        MockHalResultController(int i, Context context, Handler handler, BiometricScheduler biometricScheduler) {
            super(i, context, handler, biometricScheduler);
        }

        void init(RestartAuthRunnable restartAuthRunnable, Fingerprint21UdfpsMock fingerprint21UdfpsMock) {
            this.mRestartAuthRunnable = restartAuthRunnable;
            this.mFingerprint21 = fingerprint21UdfpsMock;
        }

        AuthenticationConsumer getLastAuthenticatedClient() {
            LastAuthArgs lastAuthArgs = this.mLastAuthArgs;
            if (lastAuthArgs != null) {
                return lastAuthArgs.lastAuthenticatedClient;
            }
            return null;
        }

        @Override // com.android.server.biometrics.sensors.fingerprint.hidl.Fingerprint21.HalResultController, android.hardware.biometrics.fingerprint.V2_1.IBiometricsFingerprintClientCallback
        public void onAuthenticated(final long j, final int i, final int i2, final ArrayList<Byte> arrayList) {
            this.mHandler.post(new Runnable() { // from class: com.android.server.biometrics.sensors.fingerprint.hidl.Fingerprint21UdfpsMock$MockHalResultController$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    Fingerprint21UdfpsMock.MockHalResultController.this.lambda$onAuthenticated$0(i, j, i2, arrayList);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onAuthenticated$0(int i, long j, int i2, ArrayList arrayList) {
            Object currentClient = this.mScheduler.getCurrentClient();
            if (!(currentClient instanceof AuthenticationConsumer)) {
                Slog.e(Fingerprint21UdfpsMock.TAG, "Non authentication consumer: " + currentClient);
                return;
            }
            if (i != 0) {
                this.mFingerprint21.setDebugMessage("Finger accepted");
            } else {
                this.mFingerprint21.setDebugMessage("Finger rejected");
            }
            AuthenticationConsumer authenticationConsumer = (AuthenticationConsumer) currentClient;
            this.mLastAuthArgs = new LastAuthArgs(authenticationConsumer, j, i, i2, arrayList);
            this.mHandler.removeCallbacks(this.mRestartAuthRunnable);
            this.mRestartAuthRunnable.setLastAuthReference(authenticationConsumer);
            this.mHandler.postDelayed(this.mRestartAuthRunnable, IDeviceIdleControllerExt.ADVANCE_TIME);
        }

        void sendAuthenticated(long j, int i, int i2, ArrayList<Byte> arrayList) {
            StringBuilder sb = new StringBuilder();
            sb.append("sendAuthenticated: ");
            sb.append(i != 0);
            Slog.d(Fingerprint21UdfpsMock.TAG, sb.toString());
            Fingerprint21UdfpsMock fingerprint21UdfpsMock = this.mFingerprint21;
            StringBuilder sb2 = new StringBuilder();
            sb2.append("Udfps match: ");
            sb2.append(i != 0);
            fingerprint21UdfpsMock.setDebugMessage(sb2.toString());
            super.onAuthenticated(j, i, i2, arrayList);
        }
    }

    public static Fingerprint21UdfpsMock newInstance(Context context, BiometricStateCallback biometricStateCallback, FingerprintSensorPropertiesInternal fingerprintSensorPropertiesInternal, LockoutResetDispatcher lockoutResetDispatcher, GestureAvailabilityDispatcher gestureAvailabilityDispatcher, BiometricContext biometricContext) {
        Slog.d(TAG, "Creating Fingerprint23Mock!");
        ServiceThread serviceThread = new ServiceThread(TAG, -2, true);
        serviceThread.start();
        Handler handler = new Handler(serviceThread.getLooper());
        TestableBiometricScheduler testableBiometricScheduler = new TestableBiometricScheduler(TAG, handler, gestureAvailabilityDispatcher);
        return new Fingerprint21UdfpsMock(context, biometricStateCallback, fingerprintSensorPropertiesInternal, testableBiometricScheduler, handler, lockoutResetDispatcher, new MockHalResultController(fingerprintSensorPropertiesInternal.sensorId, context, handler, testableBiometricScheduler), biometricContext);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public static abstract class FakeFingerRunnable implements Runnable {
        private int mCaptureDuration;
        private long mFingerDownTime;

        private FakeFingerRunnable() {
        }

        void setSimulationTime(long j, int i) {
            this.mFingerDownTime = j;
            this.mCaptureDuration = i;
        }

        boolean isImageCaptureComplete() {
            return System.currentTimeMillis() - this.mFingerDownTime > ((long) this.mCaptureDuration);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public final class FakeRejectRunnable extends FakeFingerRunnable {
        private FakeRejectRunnable() {
            super();
        }

        @Override // java.lang.Runnable
        public void run() {
            Fingerprint21UdfpsMock.this.mMockHalResultController.sendAuthenticated(0L, 0, 0, null);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public final class FakeAcceptRunnable extends FakeFingerRunnable {
        private FakeAcceptRunnable() {
            super();
        }

        @Override // java.lang.Runnable
        public void run() {
            if (Fingerprint21UdfpsMock.this.mMockHalResultController.mLastAuthArgs == null) {
                Slog.d(Fingerprint21UdfpsMock.TAG, "Sending fake finger");
                Fingerprint21UdfpsMock.this.mMockHalResultController.sendAuthenticated(1L, 1, 1, null);
            } else {
                Fingerprint21UdfpsMock.this.mMockHalResultController.sendAuthenticated(Fingerprint21UdfpsMock.this.mMockHalResultController.mLastAuthArgs.deviceId, Fingerprint21UdfpsMock.this.mMockHalResultController.mLastAuthArgs.fingerId, Fingerprint21UdfpsMock.this.mMockHalResultController.mLastAuthArgs.groupId, Fingerprint21UdfpsMock.this.mMockHalResultController.mLastAuthArgs.token);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public static final class RestartAuthRunnable implements Runnable {
        private final Fingerprint21UdfpsMock mFingerprint21;
        private AuthenticationConsumer mLastAuthConsumer;
        private final TestableBiometricScheduler mScheduler;

        RestartAuthRunnable(Fingerprint21UdfpsMock fingerprint21UdfpsMock, TestableBiometricScheduler testableBiometricScheduler) {
            this.mFingerprint21 = fingerprint21UdfpsMock;
            this.mScheduler = testableBiometricScheduler;
        }

        void setLastAuthReference(AuthenticationConsumer authenticationConsumer) {
            this.mLastAuthConsumer = authenticationConsumer;
        }

        @Override // java.lang.Runnable
        public void run() {
            BaseClientMonitor currentClient = this.mScheduler.getCurrentClient();
            if (!(currentClient instanceof FingerprintAuthenticationClient)) {
                Slog.e(Fingerprint21UdfpsMock.TAG, "Non-FingerprintAuthenticationClient client: " + currentClient);
                return;
            }
            if (currentClient != this.mLastAuthConsumer) {
                Slog.e(Fingerprint21UdfpsMock.TAG, "Current client: " + currentClient + " does not match mLastAuthConsumer: " + this.mLastAuthConsumer);
                return;
            }
            Slog.d(Fingerprint21UdfpsMock.TAG, "Restarting auth, current: " + currentClient);
            this.mFingerprint21.setDebugMessage("Auth timed out");
            FingerprintAuthenticationClient fingerprintAuthenticationClient = (FingerprintAuthenticationClient) currentClient;
            IBinder token = currentClient.getToken();
            long operationId = fingerprintAuthenticationClient.getOperationId();
            int cookie = currentClient.getCookie();
            ClientMonitorCallbackConverter listener = currentClient.getListener();
            boolean isRestricted = fingerprintAuthenticationClient.isRestricted();
            int statsClient = currentClient.getLogger().getStatsClient();
            boolean isKeyguard = fingerprintAuthenticationClient.isKeyguard();
            FingerprintAuthenticateOptions build = new FingerprintAuthenticateOptions.Builder().setUserId(currentClient.getTargetUserId()).setOpPackageName(currentClient.getOwnerString()).build();
            this.mScheduler.getInternalCallback().onClientFinished(currentClient, true);
            this.mFingerprint21.scheduleAuthenticate(token, operationId, cookie, listener, build, isRestricted, statsClient, isKeyguard);
        }
    }

    private Fingerprint21UdfpsMock(Context context, BiometricStateCallback biometricStateCallback, FingerprintSensorPropertiesInternal fingerprintSensorPropertiesInternal, TestableBiometricScheduler testableBiometricScheduler, Handler handler, LockoutResetDispatcher lockoutResetDispatcher, MockHalResultController mockHalResultController, BiometricContext biometricContext) {
        super(context, biometricStateCallback, fingerprintSensorPropertiesInternal, testableBiometricScheduler, handler, lockoutResetDispatcher, mockHalResultController, biometricContext);
        this.mScheduler = testableBiometricScheduler;
        testableBiometricScheduler.init(this);
        this.mHandler = handler;
        this.mSensorProperties = new FingerprintSensorPropertiesInternal(fingerprintSensorPropertiesInternal.sensorId, fingerprintSensorPropertiesInternal.sensorStrength, this.mContext.getResources().getInteger(R.integer.config_multiuserMaximumUsers), fingerprintSensorPropertiesInternal.componentInfo, 3, false, false, fingerprintSensorPropertiesInternal.getAllLocations());
        this.mMockHalResultController = mockHalResultController;
        this.mUserHasTrust = new SparseBooleanArray();
        TrustManager trustManager = (TrustManager) context.getSystemService(TrustManager.class);
        this.mTrustManager = trustManager;
        trustManager.registerTrustListener(this);
        this.mRandom = new Random();
        this.mFakeRejectRunnable = new FakeRejectRunnable();
        this.mFakeAcceptRunnable = new FakeAcceptRunnable();
        RestartAuthRunnable restartAuthRunnable = new RestartAuthRunnable(this, testableBiometricScheduler);
        this.mRestartAuthRunnable = restartAuthRunnable;
        mockHalResultController.init(restartAuthRunnable, this);
    }

    public void onTrustChanged(boolean z, boolean z2, int i, int i2, List<String> list) {
        this.mUserHasTrust.put(i, z);
    }

    @Override // com.android.server.biometrics.sensors.fingerprint.hidl.Fingerprint21, com.android.server.biometrics.sensors.BiometricServiceProvider
    public List<FingerprintSensorPropertiesInternal> getSensorProperties() {
        ArrayList arrayList = new ArrayList();
        arrayList.add(this.mSensorProperties);
        return arrayList;
    }

    @Override // com.android.server.biometrics.sensors.fingerprint.hidl.Fingerprint21, com.android.server.biometrics.sensors.fingerprint.ServiceProvider
    public void onPointerDown(long j, int i, PointerContext pointerContext) {
        this.mHandler.post(new Runnable() { // from class: com.android.server.biometrics.sensors.fingerprint.hidl.Fingerprint21UdfpsMock$$ExternalSyntheticLambda1
            @Override // java.lang.Runnable
            public final void run() {
                Fingerprint21UdfpsMock.this.lambda$onPointerDown$0();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onPointerDown$0() {
        Slog.d(TAG, "onFingerDown");
        AuthenticationConsumer lastAuthenticatedClient = this.mMockHalResultController.getLastAuthenticatedClient();
        BaseClientMonitor currentClient = this.mScheduler.getCurrentClient();
        if (currentClient == null) {
            Slog.d(TAG, "Not authenticating");
            return;
        }
        this.mHandler.removeCallbacks(this.mFakeRejectRunnable);
        this.mHandler.removeCallbacks(this.mFakeAcceptRunnable);
        boolean z = false;
        boolean z2 = lastAuthenticatedClient != null && lastAuthenticatedClient == currentClient;
        if (currentClient instanceof FingerprintAuthenticationClient) {
            z = ((FingerprintAuthenticationClient) currentClient).isKeyguard() && this.mUserHasTrust.get(currentClient.getTargetUserId(), false);
        }
        int newCaptureDuration = getNewCaptureDuration();
        int matchingDuration = getMatchingDuration();
        int i = newCaptureDuration + matchingDuration;
        setDebugMessage("Duration: " + i + " (" + newCaptureDuration + " + " + matchingDuration + ")");
        if (z2 || z) {
            this.mFakeAcceptRunnable.setSimulationTime(System.currentTimeMillis(), newCaptureDuration);
            this.mHandler.postDelayed(this.mFakeAcceptRunnable, i);
        } else if (currentClient instanceof AuthenticationConsumer) {
            this.mFakeRejectRunnable.setSimulationTime(System.currentTimeMillis(), newCaptureDuration);
            this.mHandler.postDelayed(this.mFakeRejectRunnable, i);
        }
    }

    @Override // com.android.server.biometrics.sensors.fingerprint.hidl.Fingerprint21, com.android.server.biometrics.sensors.fingerprint.ServiceProvider
    public void onPointerUp(long j, int i, PointerContext pointerContext) {
        this.mHandler.post(new Runnable() { // from class: com.android.server.biometrics.sensors.fingerprint.hidl.Fingerprint21UdfpsMock$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                Fingerprint21UdfpsMock.this.lambda$onPointerUp$1();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onPointerUp$1() {
        Slog.d(TAG, "onFingerUp");
        if (this.mHandler.hasCallbacks(this.mFakeRejectRunnable) && !this.mFakeRejectRunnable.isImageCaptureComplete()) {
            this.mHandler.removeCallbacks(this.mFakeRejectRunnable);
            this.mMockHalResultController.onAcquired(0L, 5, 0);
        } else {
            if (!this.mHandler.hasCallbacks(this.mFakeAcceptRunnable) || this.mFakeAcceptRunnable.isImageCaptureComplete()) {
                return;
            }
            this.mHandler.removeCallbacks(this.mFakeAcceptRunnable);
            this.mMockHalResultController.onAcquired(0L, 5, 0);
        }
    }

    private int getNewCaptureDuration() {
        ContentResolver contentResolver = this.mContext.getContentResolver();
        int intForUser = Settings.Secure.getIntForUser(contentResolver, CONFIG_AUTH_DELAY_PT1, 300, -2);
        int intForUser2 = Settings.Secure.getIntForUser(contentResolver, CONFIG_AUTH_DELAY_RANDOMNESS, 100, -2);
        return Math.max(intForUser + (this.mRandom.nextInt(intForUser2 * 2) - intForUser2), 0);
    }

    private int getMatchingDuration() {
        return Math.max(Settings.Secure.getIntForUser(this.mContext.getContentResolver(), CONFIG_AUTH_DELAY_PT2, 400, -2), 0);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setDebugMessage(String str) {
        try {
            IUdfpsOverlayController udfpsOverlayController = getUdfpsOverlayController();
            if (udfpsOverlayController != null) {
                Slog.d(TAG, "setDebugMessage: " + str);
                udfpsOverlayController.setDebugMessage(this.mSensorProperties.sensorId, str);
            }
        } catch (RemoteException e) {
            Slog.e(TAG, "Remote exception when sending message: " + str, e);
        }
    }
}
