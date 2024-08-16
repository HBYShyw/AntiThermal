package com.android.server.biometrics.sensors.fingerprint.aidl;

import android.app.ActivityManager;
import android.app.ActivityTaskManager;
import android.app.SynchronousUserSwitchObserver;
import android.app.TaskStackListener;
import android.content.Context;
import android.content.pm.UserInfo;
import android.content.res.TypedArray;
import android.hardware.biometrics.ComponentInfoInternal;
import android.hardware.biometrics.IInvalidationCallback;
import android.hardware.biometrics.ITestSession;
import android.hardware.biometrics.ITestSessionCallback;
import android.hardware.biometrics.SensorLocationInternal;
import android.hardware.biometrics.common.CommonProps;
import android.hardware.biometrics.common.ComponentInfo;
import android.hardware.biometrics.fingerprint.IFingerprint;
import android.hardware.biometrics.fingerprint.PointerContext;
import android.hardware.biometrics.fingerprint.SensorLocation;
import android.hardware.biometrics.fingerprint.SensorProps;
import android.hardware.fingerprint.Fingerprint;
import android.hardware.fingerprint.FingerprintAuthenticateOptions;
import android.hardware.fingerprint.FingerprintSensorPropertiesInternal;
import android.hardware.fingerprint.IFingerprintServiceReceiver;
import android.hardware.fingerprint.ISidefpsController;
import android.hardware.fingerprint.IUdfpsOverlay;
import android.hardware.fingerprint.IUdfpsOverlayController;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Parcel;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.os.SystemClock;
import android.os.UserManager;
import android.util.Slog;
import android.util.proto.ProtoOutputStream;
import com.android.internal.annotations.VisibleForTesting;
import com.android.server.am.AssistDataRequester;
import com.android.server.am.HostingRecord;
import com.android.server.biometrics.Utils;
import com.android.server.biometrics.log.BiometricContext;
import com.android.server.biometrics.log.BiometricLogger;
import com.android.server.biometrics.sensors.AuthSessionCoordinator;
import com.android.server.biometrics.sensors.AuthenticationClient;
import com.android.server.biometrics.sensors.BaseClientMonitor;
import com.android.server.biometrics.sensors.BiometricScheduler;
import com.android.server.biometrics.sensors.BiometricStateCallback;
import com.android.server.biometrics.sensors.ClientMonitorCallback;
import com.android.server.biometrics.sensors.ClientMonitorCallbackConverter;
import com.android.server.biometrics.sensors.ClientMonitorCompositeCallback;
import com.android.server.biometrics.sensors.InvalidationRequesterClient;
import com.android.server.biometrics.sensors.LockoutResetDispatcher;
import com.android.server.biometrics.sensors.PerformanceTracker;
import com.android.server.biometrics.sensors.SensorList;
import com.android.server.biometrics.sensors.fingerprint.FingerprintUtils;
import com.android.server.biometrics.sensors.fingerprint.GestureAvailabilityDispatcher;
import com.android.server.biometrics.sensors.fingerprint.PowerPressHandler;
import com.android.server.biometrics.sensors.fingerprint.ServiceProvider;
import com.android.server.biometrics.sensors.fingerprint.Udfps;
import com.android.server.biometrics.sensors.fingerprint.aidl.FingerprintProvider;
import com.android.server.biometrics.sensors.fingerprint.hidl.IFingerprint21ServiceProviderExt;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import system.ext.loader.core.ExtLoader;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public class FingerprintProvider implements IBinder.DeathRecipient, ServiceProvider {
    public static IFingerprintServiceProviderExt mFingerprintProviderExt = (IFingerprintServiceProviderExt) ExtLoader.type(IFingerprintServiceProviderExt.class).create();
    private AuthSessionCoordinator mAuthSessionCoordinator;
    private final BiometricContext mBiometricContext;
    private final BiometricStateCallback mBiometricStateCallback;
    private final Context mContext;
    private IFingerprint mDaemon;
    private final String mHalInstanceName;
    private final LockoutResetDispatcher mLockoutResetDispatcher;
    private ISidefpsController mSidefpsController;
    private boolean mTestHalEnabled;
    private IUdfpsOverlay mUdfpsOverlay;
    private IUdfpsOverlayController mUdfpsOverlayController;
    private final AtomicLong mRequestCounter = new AtomicLong(0);

    @VisibleForTesting
    final SensorList<Sensor> mFingerprintSensors = new SensorList<>(ActivityManager.getService());
    private final Handler mHandler = mFingerprintProviderExt.createHandlerWithNewLooper();
    private final ActivityTaskManager mActivityTaskManager = ActivityTaskManager.getInstance();
    private final BiometricTaskStackListener mTaskStackListener = new BiometricTaskStackListener();

    @Override // com.android.server.biometrics.sensors.BiometricServiceProvider
    public void dumpProtoMetrics(int i, FileDescriptor fileDescriptor) {
    }

    @Override // com.android.server.biometrics.sensors.fingerprint.ServiceProvider
    public IFingerprint21ServiceProviderExt getServiceProviderEx() {
        return null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public final class BiometricTaskStackListener extends TaskStackListener {
        private BiometricTaskStackListener() {
        }

        public void onTaskStackChanged() {
            FingerprintProvider.this.mHandler.post(new Runnable() { // from class: com.android.server.biometrics.sensors.fingerprint.aidl.FingerprintProvider$BiometricTaskStackListener$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    FingerprintProvider.BiometricTaskStackListener.this.lambda$onTaskStackChanged$0();
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onTaskStackChanged$0() {
            for (int i = 0; i < FingerprintProvider.this.mFingerprintSensors.size(); i++) {
                BaseClientMonitor currentClient = FingerprintProvider.this.mFingerprintSensors.valueAt(i).getScheduler().getCurrentClient();
                if (!(currentClient instanceof AuthenticationClient)) {
                    Slog.e(FingerprintProvider.this.getTag(), "Task stack changed for client: " + currentClient);
                } else if (!Utils.isKeyguard(FingerprintProvider.this.mContext, currentClient.getOwnerString()) && !Utils.isSystem(FingerprintProvider.this.mContext, currentClient.getOwnerString()) && Utils.isBackground(currentClient.getOwnerString()) && !currentClient.isAlreadyDone()) {
                    Slog.e(FingerprintProvider.this.getTag(), "Stopping background authentication, currentClient: " + currentClient);
                    FingerprintProvider.this.mFingerprintSensors.valueAt(i).getScheduler().cancelAuthenticationOrDetection(currentClient.getToken(), currentClient.getRequestId());
                }
            }
        }
    }

    public FingerprintProvider(Context context, BiometricStateCallback biometricStateCallback, SensorProps[] sensorPropsArr, String str, LockoutResetDispatcher lockoutResetDispatcher, GestureAvailabilityDispatcher gestureAvailabilityDispatcher, BiometricContext biometricContext) {
        this.mContext = context;
        this.mBiometricStateCallback = biometricStateCallback;
        this.mHalInstanceName = str;
        this.mLockoutResetDispatcher = lockoutResetDispatcher;
        this.mBiometricContext = biometricContext;
        this.mAuthSessionCoordinator = biometricContext.getAuthSessionCoordinator();
        List<SensorLocationInternal> workaroundSensorProps = getWorkaroundSensorProps(context);
        for (SensorProps sensorProps : sensorPropsArr) {
            final int i = sensorProps.commonProps.sensorId;
            ArrayList arrayList = new ArrayList();
            ComponentInfo[] componentInfoArr = sensorProps.commonProps.componentInfo;
            if (componentInfoArr != null) {
                int i2 = 0;
                for (int length = componentInfoArr.length; i2 < length; length = length) {
                    ComponentInfo componentInfo = componentInfoArr[i2];
                    arrayList.add(new ComponentInfoInternal(componentInfo.componentId, componentInfo.hardwareVersion, componentInfo.firmwareVersion, componentInfo.serialNumber, componentInfo.softwareVersion));
                    i2++;
                    componentInfoArr = componentInfoArr;
                }
            }
            CommonProps commonProps = sensorProps.commonProps;
            FingerprintSensorPropertiesInternal fingerprintSensorPropertiesInternal = new FingerprintSensorPropertiesInternal(commonProps.sensorId, commonProps.sensorStrength, commonProps.maxEnrollmentsPerUser, arrayList, sensorProps.sensorType, sensorProps.halControlsIllumination, true, !workaroundSensorProps.isEmpty() ? workaroundSensorProps : (List) Arrays.stream(sensorProps.sensorLocations).map(new Function() { // from class: com.android.server.biometrics.sensors.fingerprint.aidl.FingerprintProvider$$ExternalSyntheticLambda0
                @Override // java.util.function.Function
                public final Object apply(Object obj) {
                    SensorLocationInternal lambda$new$0;
                    lambda$new$0 = FingerprintProvider.lambda$new$0((SensorLocation) obj);
                    return lambda$new$0;
                }
            }).collect(Collectors.toList()));
            Sensor sensor = new Sensor(getTag() + "/" + i, this, this.mContext, this.mHandler, fingerprintSensorPropertiesInternal, lockoutResetDispatcher, gestureAvailabilityDispatcher, this.mBiometricContext);
            this.mFingerprintSensors.addSensor(i, sensor, sensor.getLazySession().get() == null ? -10000 : sensor.getLazySession().get().getUserId(), new SynchronousUserSwitchObserver() { // from class: com.android.server.biometrics.sensors.fingerprint.aidl.FingerprintProvider.1
                public void onUserSwitching(int i3) {
                    IFingerprintServiceProviderExt iFingerprintServiceProviderExt = FingerprintProvider.mFingerprintProviderExt;
                    if (iFingerprintServiceProviderExt != null) {
                        iFingerprintServiceProviderExt.userSwitchNotice(FingerprintProvider.this.mContext);
                    }
                    FingerprintProvider.this.scheduleInternalCleanup(i, i3, null);
                }
            });
            Slog.d(getTag(), "Added: " + fingerprintSensorPropertiesInternal);
        }
        Slog.d(getTag(), "mFingerprintSensors.length: " + this.mFingerprintSensors.size() + " , mFingerprintSensors.valueAt(0):" + sensorPropsArr[0].commonProps.sensorId);
        mFingerprintProviderExt.init(this.mContext, this.mHalInstanceName, this.mHandler, this.mFingerprintSensors, this.mBiometricContext, this);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ SensorLocationInternal lambda$new$0(SensorLocation sensorLocation) {
        return new SensorLocationInternal(sensorLocation.display, sensorLocation.sensorLocationX, sensorLocation.sensorLocationY, sensorLocation.sensorRadius);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public String getTag() {
        return "FingerprintProvider/" + this.mHalInstanceName;
    }

    boolean hasHalInstance() {
        if (this.mTestHalEnabled) {
            return true;
        }
        StringBuilder sb = new StringBuilder();
        sb.append(IFingerprint.DESCRIPTOR);
        sb.append("/");
        sb.append(this.mHalInstanceName);
        return ServiceManager.checkService(sb.toString()) != null;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @VisibleForTesting
    public synchronized IFingerprint getHalInstance() {
        if (this.mTestHalEnabled) {
            return new TestHal();
        }
        IFingerprint iFingerprint = this.mDaemon;
        if (iFingerprint != null) {
            return iFingerprint;
        }
        Slog.d(getTag(), "Daemon was null, reconnecting");
        IFingerprint asInterface = IFingerprint.Stub.asInterface(Binder.allowBlocking(ServiceManager.waitForDeclaredService(IFingerprint.DESCRIPTOR + "/" + this.mHalInstanceName)));
        this.mDaemon = asInterface;
        if (asInterface == null) {
            Slog.e(getTag(), "Unable to get daemon");
            return null;
        }
        try {
            asInterface.asBinder().linkToDeath(this, 0);
        } catch (RemoteException e) {
            Slog.e(getTag(), "Unable to linkToDeath", e);
        }
        for (int i = 0; i < this.mFingerprintSensors.size(); i++) {
            int keyAt = this.mFingerprintSensors.keyAt(i);
            scheduleLoadAuthenticatorIds(keyAt);
            scheduleInternalCleanup(keyAt, ActivityManager.getCurrentUser(), null);
        }
        IFingerprintServiceProviderExt iFingerprintServiceProviderExt = mFingerprintProviderExt;
        if (iFingerprintServiceProviderExt != null) {
            iFingerprintServiceProviderExt.notifyHalReady();
        }
        return this.mDaemon;
    }

    private void scheduleForSensor(int i, BaseClientMonitor baseClientMonitor) {
        if (!this.mFingerprintSensors.contains(i)) {
            throw new IllegalStateException("Unable to schedule client: " + baseClientMonitor + " for sensor: " + i);
        }
        this.mFingerprintSensors.get(i).getScheduler().scheduleClientMonitor(baseClientMonitor);
    }

    private void scheduleForSensor(int i, BaseClientMonitor baseClientMonitor, ClientMonitorCallback clientMonitorCallback) {
        if (!this.mFingerprintSensors.contains(i)) {
            throw new IllegalStateException("Unable to schedule client: " + baseClientMonitor + " for sensor: " + i);
        }
        this.mFingerprintSensors.get(i).getScheduler().scheduleClientMonitor(baseClientMonitor, clientMonitorCallback);
    }

    @Override // com.android.server.biometrics.sensors.BiometricServiceProvider
    public boolean containsSensor(int i) {
        return this.mFingerprintSensors.contains(i);
    }

    @Override // com.android.server.biometrics.sensors.BiometricServiceProvider
    public List<FingerprintSensorPropertiesInternal> getSensorProperties() {
        ArrayList arrayList = new ArrayList();
        for (int i = 0; i < this.mFingerprintSensors.size(); i++) {
            arrayList.add(this.mFingerprintSensors.valueAt(i).getSensorProperties());
        }
        return arrayList;
    }

    @Override // com.android.server.biometrics.sensors.BiometricServiceProvider
    public FingerprintSensorPropertiesInternal getSensorProperties(int i) {
        if (this.mFingerprintSensors.size() == 0) {
            return null;
        }
        if (i == -1) {
            return this.mFingerprintSensors.valueAt(0).getSensorProperties();
        }
        Sensor sensor = this.mFingerprintSensors.get(i);
        if (sensor != null) {
            return sensor.getSensorProperties();
        }
        return null;
    }

    private void scheduleLoadAuthenticatorIds(int i) {
        Iterator it = UserManager.get(this.mContext).getAliveUsers().iterator();
        while (it.hasNext()) {
            scheduleLoadAuthenticatorIdsForUser(i, ((UserInfo) it.next()).id);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void scheduleLoadAuthenticatorIdsForUser(final int i, final int i2) {
        this.mHandler.post(new Runnable() { // from class: com.android.server.biometrics.sensors.fingerprint.aidl.FingerprintProvider$$ExternalSyntheticLambda5
            @Override // java.lang.Runnable
            public final void run() {
                FingerprintProvider.this.lambda$scheduleLoadAuthenticatorIdsForUser$1(i, i2);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$scheduleLoadAuthenticatorIdsForUser$1(int i, int i2) {
        scheduleForSensor(i, new FingerprintGetAuthenticatorIdClient(this.mContext, this.mFingerprintSensors.get(i).getLazySession(), i2, this.mContext.getOpPackageName(), i, createLogger(0, 0), this.mBiometricContext, this.mFingerprintSensors.get(i).getAuthenticatorIds()));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void scheduleInvalidationRequest(final int i, final int i2) {
        this.mHandler.post(new Runnable() { // from class: com.android.server.biometrics.sensors.fingerprint.aidl.FingerprintProvider$$ExternalSyntheticLambda16
            @Override // java.lang.Runnable
            public final void run() {
                FingerprintProvider.this.lambda$scheduleInvalidationRequest$2(i2, i);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$scheduleInvalidationRequest$2(int i, int i2) {
        Context context = this.mContext;
        scheduleForSensor(i2, new InvalidationRequesterClient(context, i, i2, BiometricLogger.ofUnknown(context), this.mBiometricContext, FingerprintUtils.getInstance(i2)));
    }

    @Override // com.android.server.biometrics.sensors.fingerprint.ServiceProvider
    public void scheduleResetLockout(final int i, final int i2, final byte[] bArr) {
        this.mHandler.post(new Runnable() { // from class: com.android.server.biometrics.sensors.fingerprint.aidl.FingerprintProvider$$ExternalSyntheticLambda7
            @Override // java.lang.Runnable
            public final void run() {
                FingerprintProvider.this.lambda$scheduleResetLockout$3(i, i2, bArr);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$scheduleResetLockout$3(int i, int i2, byte[] bArr) {
        scheduleForSensor(i, new FingerprintResetLockoutClient(this.mContext, this.mFingerprintSensors.get(i).getLazySession(), i2, this.mContext.getOpPackageName(), i, createLogger(0, 0), this.mBiometricContext, bArr, this.mFingerprintSensors.get(i).getLockoutCache(), this.mLockoutResetDispatcher, Utils.getCurrentStrength(i)));
        mFingerprintProviderExt.resetFingerprintLockout(bArr, i2);
    }

    @Override // com.android.server.biometrics.sensors.fingerprint.ServiceProvider
    public void scheduleGenerateChallenge(final int i, final int i2, final IBinder iBinder, final IFingerprintServiceReceiver iFingerprintServiceReceiver, final String str) {
        this.mHandler.post(new Runnable() { // from class: com.android.server.biometrics.sensors.fingerprint.aidl.FingerprintProvider$$ExternalSyntheticLambda1
            @Override // java.lang.Runnable
            public final void run() {
                FingerprintProvider.this.lambda$scheduleGenerateChallenge$4(i, iBinder, iFingerprintServiceReceiver, i2, str);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$scheduleGenerateChallenge$4(int i, IBinder iBinder, IFingerprintServiceReceiver iFingerprintServiceReceiver, int i2, String str) {
        scheduleForSensor(i, new FingerprintGenerateChallengeClient(this.mContext, this.mFingerprintSensors.get(i).getLazySession(), iBinder, new ClientMonitorCallbackConverter(iFingerprintServiceReceiver), i2, str, i, createLogger(0, 0), this.mBiometricContext));
    }

    @Override // com.android.server.biometrics.sensors.fingerprint.ServiceProvider
    public void scheduleRevokeChallenge(final int i, final int i2, final IBinder iBinder, final String str, final long j) {
        this.mHandler.post(new Runnable() { // from class: com.android.server.biometrics.sensors.fingerprint.aidl.FingerprintProvider$$ExternalSyntheticLambda18
            @Override // java.lang.Runnable
            public final void run() {
                FingerprintProvider.this.lambda$scheduleRevokeChallenge$5(i, iBinder, i2, str, j);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$scheduleRevokeChallenge$5(int i, IBinder iBinder, int i2, String str, long j) {
        scheduleForSensor(i, new FingerprintRevokeChallengeClient(this.mContext, this.mFingerprintSensors.get(i).getLazySession(), iBinder, i2, str, i, createLogger(0, 0), this.mBiometricContext, j));
    }

    @Override // com.android.server.biometrics.sensors.fingerprint.ServiceProvider
    public long scheduleEnroll(final int i, final IBinder iBinder, final byte[] bArr, final int i2, final IFingerprintServiceReceiver iFingerprintServiceReceiver, final String str, final int i3) {
        final long incrementAndGet = this.mRequestCounter.incrementAndGet();
        this.mHandler.post(new Runnable() { // from class: com.android.server.biometrics.sensors.fingerprint.aidl.FingerprintProvider$$ExternalSyntheticLambda17
            @Override // java.lang.Runnable
            public final void run() {
                FingerprintProvider.this.lambda$scheduleEnroll$6(i, iBinder, incrementAndGet, iFingerprintServiceReceiver, i2, bArr, str, i3);
            }
        });
        return incrementAndGet;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$scheduleEnroll$6(final int i, IBinder iBinder, long j, IFingerprintServiceReceiver iFingerprintServiceReceiver, final int i2, byte[] bArr, String str, int i3) {
        scheduleForSensor(i, new FingerprintEnrollClient(this.mContext, this.mFingerprintSensors.get(i).getLazySession(), iBinder, j, new ClientMonitorCallbackConverter(iFingerprintServiceReceiver), i2, bArr, str, FingerprintUtils.getInstance(i), i, createLogger(1, 0), this.mBiometricContext, this.mFingerprintSensors.get(i).getSensorProperties(), this.mUdfpsOverlayController, this.mSidefpsController, this.mUdfpsOverlay, this.mFingerprintSensors.get(i).getSensorProperties().maxEnrollmentsPerUser, i3), new ClientMonitorCompositeCallback(this.mBiometricStateCallback, new ClientMonitorCallback() { // from class: com.android.server.biometrics.sensors.fingerprint.aidl.FingerprintProvider.2
            @Override // com.android.server.biometrics.sensors.ClientMonitorCallback
            public void onClientFinished(BaseClientMonitor baseClientMonitor, boolean z) {
                super.onClientFinished(baseClientMonitor, z);
                if (z) {
                    FingerprintProvider.this.scheduleLoadAuthenticatorIdsForUser(i, i2);
                    FingerprintProvider.this.scheduleInvalidationRequest(i, i2);
                }
            }
        }));
    }

    @Override // com.android.server.biometrics.sensors.fingerprint.ServiceProvider
    public void cancelEnrollment(final int i, final IBinder iBinder, final long j) {
        mFingerprintProviderExt.handleCancelEnrollment(i, iBinder);
        this.mHandler.post(new Runnable() { // from class: com.android.server.biometrics.sensors.fingerprint.aidl.FingerprintProvider$$ExternalSyntheticLambda15
            @Override // java.lang.Runnable
            public final void run() {
                FingerprintProvider.this.lambda$cancelEnrollment$7(i, iBinder, j);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$cancelEnrollment$7(int i, IBinder iBinder, long j) {
        this.mFingerprintSensors.get(i).getScheduler().cancelEnrollment(iBinder, j);
    }

    @Override // com.android.server.biometrics.sensors.fingerprint.ServiceProvider
    public long scheduleFingerDetect(final IBinder iBinder, final ClientMonitorCallbackConverter clientMonitorCallbackConverter, final FingerprintAuthenticateOptions fingerprintAuthenticateOptions, final int i) {
        final long incrementAndGet = this.mRequestCounter.incrementAndGet();
        this.mHandler.post(new Runnable() { // from class: com.android.server.biometrics.sensors.fingerprint.aidl.FingerprintProvider$$ExternalSyntheticLambda4
            @Override // java.lang.Runnable
            public final void run() {
                FingerprintProvider.this.lambda$scheduleFingerDetect$8(fingerprintAuthenticateOptions, iBinder, incrementAndGet, clientMonitorCallbackConverter, i);
            }
        });
        return incrementAndGet;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$scheduleFingerDetect$8(FingerprintAuthenticateOptions fingerprintAuthenticateOptions, IBinder iBinder, long j, ClientMonitorCallbackConverter clientMonitorCallbackConverter, int i) {
        int sensorId = fingerprintAuthenticateOptions.getSensorId();
        scheduleForSensor(sensorId, new FingerprintDetectClient(this.mContext, this.mFingerprintSensors.get(sensorId).getLazySession(), iBinder, j, clientMonitorCallbackConverter, fingerprintAuthenticateOptions, createLogger(2, i), this.mBiometricContext, this.mUdfpsOverlayController, this.mUdfpsOverlay, Utils.isStrongBiometric(sensorId)), this.mBiometricStateCallback);
    }

    @Override // com.android.server.biometrics.sensors.fingerprint.ServiceProvider
    public void scheduleAuthenticate(final IBinder iBinder, final long j, final int i, final ClientMonitorCallbackConverter clientMonitorCallbackConverter, final FingerprintAuthenticateOptions fingerprintAuthenticateOptions, final long j2, final boolean z, final int i2, final boolean z2) {
        this.mHandler.post(new Runnable() { // from class: com.android.server.biometrics.sensors.fingerprint.aidl.FingerprintProvider$$ExternalSyntheticLambda14
            @Override // java.lang.Runnable
            public final void run() {
                FingerprintProvider.this.lambda$scheduleAuthenticate$9(fingerprintAuthenticateOptions, iBinder, j2, clientMonitorCallbackConverter, j, z, i, i2, z2);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$scheduleAuthenticate$9(FingerprintAuthenticateOptions fingerprintAuthenticateOptions, IBinder iBinder, final long j, ClientMonitorCallbackConverter clientMonitorCallbackConverter, long j2, boolean z, int i, int i2, boolean z2) {
        final int userId = fingerprintAuthenticateOptions.getUserId();
        final int sensorId = fingerprintAuthenticateOptions.getSensorId();
        scheduleForSensor(sensorId, new FingerprintAuthenticationClient(this.mContext, this.mFingerprintSensors.get(sensorId).getLazySession(), iBinder, j, clientMonitorCallbackConverter, j2, z, fingerprintAuthenticateOptions, i, false, createLogger(2, i2), this.mBiometricContext, Utils.isStrongBiometric(sensorId), this.mTaskStackListener, this.mFingerprintSensors.get(sensorId).getLockoutCache(), this.mUdfpsOverlayController, this.mSidefpsController, this.mUdfpsOverlay, z2, this.mFingerprintSensors.get(sensorId).getSensorProperties(), this.mHandler, Utils.getCurrentStrength(sensorId), SystemClock.elapsedRealtimeClock()), new ClientMonitorCallback() { // from class: com.android.server.biometrics.sensors.fingerprint.aidl.FingerprintProvider.3
            @Override // com.android.server.biometrics.sensors.ClientMonitorCallback
            public void onClientStarted(BaseClientMonitor baseClientMonitor) {
                FingerprintProvider.this.mBiometricStateCallback.onClientStarted(baseClientMonitor);
                FingerprintProvider.this.mAuthSessionCoordinator.authStartedFor(userId, sensorId, j);
            }

            @Override // com.android.server.biometrics.sensors.ClientMonitorCallback
            public void onBiometricAction(int i3) {
                FingerprintProvider.this.mBiometricStateCallback.onBiometricAction(i3);
            }

            @Override // com.android.server.biometrics.sensors.ClientMonitorCallback
            public void onClientFinished(BaseClientMonitor baseClientMonitor, boolean z3) {
                FingerprintProvider.this.mBiometricStateCallback.onClientFinished(baseClientMonitor, z3);
                FingerprintProvider.this.mAuthSessionCoordinator.authEndedFor(userId, Utils.getCurrentStrength(sensorId), sensorId, j, z3);
            }
        });
    }

    @Override // com.android.server.biometrics.sensors.fingerprint.ServiceProvider
    public long scheduleAuthenticate(IBinder iBinder, long j, int i, ClientMonitorCallbackConverter clientMonitorCallbackConverter, FingerprintAuthenticateOptions fingerprintAuthenticateOptions, boolean z, int i2, boolean z2) {
        long incrementAndGet = this.mRequestCounter.incrementAndGet();
        scheduleAuthenticate(iBinder, j, i, clientMonitorCallbackConverter, fingerprintAuthenticateOptions, incrementAndGet, z, i2, z2);
        return incrementAndGet;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$startPreparedClient$10(int i, int i2) {
        this.mFingerprintSensors.get(i).getScheduler().startPreparedClient(i2);
    }

    @Override // com.android.server.biometrics.sensors.fingerprint.ServiceProvider
    public void startPreparedClient(final int i, final int i2) {
        this.mHandler.post(new Runnable() { // from class: com.android.server.biometrics.sensors.fingerprint.aidl.FingerprintProvider$$ExternalSyntheticLambda12
            @Override // java.lang.Runnable
            public final void run() {
                FingerprintProvider.this.lambda$startPreparedClient$10(i, i2);
            }
        });
    }

    @Override // com.android.server.biometrics.sensors.fingerprint.ServiceProvider
    public void cancelAuthentication(final int i, final IBinder iBinder, final long j) {
        Slog.d(getTag(), "[cancelAuthentication] sensorId: " + i + " ,token: " + iBinder + " ,requestId: " + j);
        mFingerprintProviderExt.handleCancelAuthentication(i, iBinder);
        this.mHandler.post(new Runnable() { // from class: com.android.server.biometrics.sensors.fingerprint.aidl.FingerprintProvider$$ExternalSyntheticLambda10
            @Override // java.lang.Runnable
            public final void run() {
                FingerprintProvider.this.lambda$cancelAuthentication$11(i, iBinder, j);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$cancelAuthentication$11(int i, IBinder iBinder, long j) {
        this.mFingerprintSensors.get(i).getScheduler().cancelAuthenticationOrDetection(iBinder, j);
    }

    @Override // com.android.server.biometrics.sensors.fingerprint.ServiceProvider
    public void scheduleRemove(int i, IBinder iBinder, IFingerprintServiceReceiver iFingerprintServiceReceiver, int i2, int i3, String str) {
        scheduleRemoveSpecifiedIds(i, iBinder, new int[]{i2}, i3, iFingerprintServiceReceiver, str);
    }

    @Override // com.android.server.biometrics.sensors.fingerprint.ServiceProvider
    public void scheduleRemoveAll(int i, IBinder iBinder, IFingerprintServiceReceiver iFingerprintServiceReceiver, int i2, String str) {
        List<Fingerprint> biometricsForUser = FingerprintUtils.getInstance(i).getBiometricsForUser(this.mContext, i2);
        int[] iArr = new int[biometricsForUser.size()];
        for (int i3 = 0; i3 < biometricsForUser.size(); i3++) {
            iArr[i3] = biometricsForUser.get(i3).getBiometricId();
        }
        scheduleRemoveSpecifiedIds(i, iBinder, iArr, i2, iFingerprintServiceReceiver, str);
    }

    private void scheduleRemoveSpecifiedIds(final int i, final IBinder iBinder, final int[] iArr, final int i2, final IFingerprintServiceReceiver iFingerprintServiceReceiver, final String str) {
        this.mHandler.post(new Runnable() { // from class: com.android.server.biometrics.sensors.fingerprint.aidl.FingerprintProvider$$ExternalSyntheticLambda9
            @Override // java.lang.Runnable
            public final void run() {
                FingerprintProvider.this.lambda$scheduleRemoveSpecifiedIds$12(str, i2, i, iBinder, iFingerprintServiceReceiver, iArr);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$scheduleRemoveSpecifiedIds$12(String str, int i, int i2, IBinder iBinder, IFingerprintServiceReceiver iFingerprintServiceReceiver, int[] iArr) {
        Slog.d(getTag(), "[scheduleRemoveSpecifiedIds] opPackageName:" + str + " userId:" + i);
        scheduleForSensor(i2, new FingerprintRemovalClient(this.mContext, this.mFingerprintSensors.get(i2).getLazySession(), iBinder, new ClientMonitorCallbackConverter(iFingerprintServiceReceiver), iArr, i, str, FingerprintUtils.getInstance(i2), i2, createLogger(4, 0), this.mBiometricContext, this.mFingerprintSensors.get(i2).getAuthenticatorIds()), this.mBiometricStateCallback);
    }

    @Override // com.android.server.biometrics.sensors.fingerprint.ServiceProvider
    public void scheduleInternalCleanup(int i, int i2, ClientMonitorCallback clientMonitorCallback) {
        scheduleInternalCleanup(i, i2, clientMonitorCallback, false);
    }

    @Override // com.android.server.biometrics.sensors.fingerprint.ServiceProvider
    public void scheduleInternalCleanup(final int i, final int i2, final ClientMonitorCallback clientMonitorCallback, final boolean z) {
        this.mHandler.post(new Runnable() { // from class: com.android.server.biometrics.sensors.fingerprint.aidl.FingerprintProvider$$ExternalSyntheticLambda8
            @Override // java.lang.Runnable
            public final void run() {
                FingerprintProvider.this.lambda$scheduleInternalCleanup$13(i, i2, z, clientMonitorCallback);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$scheduleInternalCleanup$13(int i, int i2, boolean z, ClientMonitorCallback clientMonitorCallback) {
        FingerprintInternalCleanupClient fingerprintInternalCleanupClient = new FingerprintInternalCleanupClient(this.mContext, this.mFingerprintSensors.get(i).getLazySession(), i2, this.mContext.getOpPackageName(), i, createLogger(3, 0), this.mBiometricContext, FingerprintUtils.getInstance(i), this.mFingerprintSensors.get(i).getAuthenticatorIds());
        if (z) {
            fingerprintInternalCleanupClient.setFavorHalEnrollments();
        }
        scheduleForSensor(i, fingerprintInternalCleanupClient, new ClientMonitorCompositeCallback(clientMonitorCallback, this.mBiometricStateCallback));
    }

    private BiometricLogger createLogger(int i, int i2) {
        return new BiometricLogger(this.mContext, 1, i, i2);
    }

    @Override // com.android.server.biometrics.sensors.BiometricServiceProvider
    public boolean isHardwareDetected(int i) {
        return hasHalInstance();
    }

    @Override // com.android.server.biometrics.sensors.fingerprint.ServiceProvider
    public void rename(int i, int i2, int i3, String str) {
        FingerprintUtils.getInstance(i).renameBiometricForUser(this.mContext, i3, i2, str);
    }

    @Override // com.android.server.biometrics.sensors.fingerprint.ServiceProvider
    public List<Fingerprint> getEnrolledFingerprints(int i, int i2) {
        return FingerprintUtils.getInstance(i).getBiometricsForUser(this.mContext, i2);
    }

    @Override // com.android.server.biometrics.sensors.BiometricServiceProvider
    public boolean hasEnrollments(int i, int i2) {
        return !getEnrolledFingerprints(i, i2).isEmpty();
    }

    @Override // com.android.server.biometrics.sensors.fingerprint.ServiceProvider
    public void scheduleInvalidateAuthenticatorId(final int i, final int i2, final IInvalidationCallback iInvalidationCallback) {
        this.mHandler.post(new Runnable() { // from class: com.android.server.biometrics.sensors.fingerprint.aidl.FingerprintProvider$$ExternalSyntheticLambda3
            @Override // java.lang.Runnable
            public final void run() {
                FingerprintProvider.this.lambda$scheduleInvalidateAuthenticatorId$14(i, i2, iInvalidationCallback);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$scheduleInvalidateAuthenticatorId$14(int i, int i2, IInvalidationCallback iInvalidationCallback) {
        scheduleForSensor(i, new FingerprintInvalidationClient(this.mContext, this.mFingerprintSensors.get(i).getLazySession(), i2, i, createLogger(0, 0), this.mBiometricContext, this.mFingerprintSensors.get(i).getAuthenticatorIds(), iInvalidationCallback));
    }

    @Override // com.android.server.biometrics.sensors.BiometricServiceProvider
    public int getLockoutModeForUser(int i, int i2) {
        return this.mBiometricContext.getAuthSessionCoordinator().getLockoutStateFor(i2, Utils.getCurrentStrength(i));
    }

    @Override // com.android.server.biometrics.sensors.BiometricServiceProvider
    public long getAuthenticatorId(int i, int i2) {
        if (i2 == 999) {
            i2 = 0;
        }
        return this.mFingerprintSensors.get(i).getAuthenticatorIds().getOrDefault(Integer.valueOf(i2), 0L).longValue();
    }

    @Override // com.android.server.biometrics.sensors.fingerprint.ServiceProvider
    public void onPointerDown(long j, int i, final PointerContext pointerContext) {
        this.mFingerprintSensors.get(i).getScheduler().getCurrentClientIfMatches(j, new Consumer() { // from class: com.android.server.biometrics.sensors.fingerprint.aidl.FingerprintProvider$$ExternalSyntheticLambda13
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                FingerprintProvider.this.lambda$onPointerDown$15(pointerContext, (BaseClientMonitor) obj);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Multi-variable type inference failed */
    public /* synthetic */ void lambda$onPointerDown$15(PointerContext pointerContext, BaseClientMonitor baseClientMonitor) {
        if (mFingerprintProviderExt.handleOnPointerDown()) {
            return;
        }
        if (!(baseClientMonitor instanceof Udfps)) {
            Slog.e(getTag(), "onPointerDown received during client: " + baseClientMonitor);
            return;
        }
        ((Udfps) baseClientMonitor).onPointerDown(pointerContext);
    }

    @Override // com.android.server.biometrics.sensors.fingerprint.ServiceProvider
    public void onPointerUp(long j, int i, final PointerContext pointerContext) {
        this.mFingerprintSensors.get(i).getScheduler().getCurrentClientIfMatches(j, new Consumer() { // from class: com.android.server.biometrics.sensors.fingerprint.aidl.FingerprintProvider$$ExternalSyntheticLambda11
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                FingerprintProvider.this.lambda$onPointerUp$16(pointerContext, (BaseClientMonitor) obj);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Multi-variable type inference failed */
    public /* synthetic */ void lambda$onPointerUp$16(PointerContext pointerContext, BaseClientMonitor baseClientMonitor) {
        if (mFingerprintProviderExt.handleOnPointerUp()) {
            return;
        }
        if (!(baseClientMonitor instanceof Udfps)) {
            Slog.e(getTag(), "onPointerUp received during client: " + baseClientMonitor);
            return;
        }
        ((Udfps) baseClientMonitor).onPointerUp(pointerContext);
    }

    @Override // com.android.server.biometrics.sensors.fingerprint.ServiceProvider
    public void onUiReady(long j, int i) {
        this.mFingerprintSensors.get(i).getScheduler().getCurrentClientIfMatches(j, new Consumer() { // from class: com.android.server.biometrics.sensors.fingerprint.aidl.FingerprintProvider$$ExternalSyntheticLambda2
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                FingerprintProvider.this.lambda$onUiReady$17((BaseClientMonitor) obj);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Multi-variable type inference failed */
    public /* synthetic */ void lambda$onUiReady$17(BaseClientMonitor baseClientMonitor) {
        if (!(baseClientMonitor instanceof Udfps)) {
            Slog.e(getTag(), "onUiReady received during client: " + baseClientMonitor);
            return;
        }
        ((Udfps) baseClientMonitor).onUiReady();
    }

    @Override // com.android.server.biometrics.sensors.fingerprint.ServiceProvider
    public void setUdfpsOverlayController(IUdfpsOverlayController iUdfpsOverlayController) {
        this.mUdfpsOverlayController = iUdfpsOverlayController;
        mFingerprintProviderExt.setUdfpsOverlayController(iUdfpsOverlayController);
    }

    @Override // com.android.server.biometrics.sensors.fingerprint.ServiceProvider
    public void onPowerPressed() {
        IBinder.DeathRecipient currentClient;
        for (int i = 0; i < this.mFingerprintSensors.size() && (currentClient = this.mFingerprintSensors.valueAt(i).getScheduler().getCurrentClient()) != null; i++) {
            if (currentClient instanceof PowerPressHandler) {
                ((PowerPressHandler) currentClient).onPowerPressed();
            }
        }
    }

    @Override // com.android.server.biometrics.sensors.fingerprint.ServiceProvider
    public void setSidefpsController(ISidefpsController iSidefpsController) {
        this.mSidefpsController = iSidefpsController;
        mFingerprintProviderExt.setSidefpsController(iSidefpsController);
    }

    @Override // com.android.server.biometrics.sensors.fingerprint.ServiceProvider
    public void setUdfpsOverlay(IUdfpsOverlay iUdfpsOverlay) {
        this.mUdfpsOverlay = iUdfpsOverlay;
        mFingerprintProviderExt.setUdfpsOverlay(iUdfpsOverlay);
    }

    @Override // com.android.server.biometrics.sensors.BiometricServiceProvider
    public void dumpProtoState(int i, ProtoOutputStream protoOutputStream, boolean z) {
        if (this.mFingerprintSensors.contains(i)) {
            this.mFingerprintSensors.get(i).dumpProtoState(i, protoOutputStream, z);
        }
    }

    @Override // com.android.server.biometrics.sensors.BiometricServiceProvider
    public void dumpInternal(int i, PrintWriter printWriter) {
        PerformanceTracker instanceForSensorId = PerformanceTracker.getInstanceForSensorId(i);
        JSONObject jSONObject = new JSONObject();
        try {
            jSONObject.put(HostingRecord.HOSTING_TYPE_SERVICE, getTag());
            JSONArray jSONArray = new JSONArray();
            Iterator it = UserManager.get(this.mContext).getUsers().iterator();
            while (it.hasNext()) {
                int identifier = ((UserInfo) it.next()).getUserHandle().getIdentifier();
                int size = FingerprintUtils.getInstance(i).getBiometricsForUser(this.mContext, identifier).size();
                JSONObject jSONObject2 = new JSONObject();
                jSONObject2.put("id", identifier);
                jSONObject2.put(AssistDataRequester.KEY_RECEIVER_EXTRA_COUNT, size);
                jSONObject2.put("accept", instanceForSensorId.getAcceptForUser(identifier));
                jSONObject2.put("reject", instanceForSensorId.getRejectForUser(identifier));
                jSONObject2.put("acquire", instanceForSensorId.getAcquireForUser(identifier));
                jSONObject2.put("lockout", instanceForSensorId.getTimedLockoutForUser(identifier));
                jSONObject2.put("permanentLockout", instanceForSensorId.getPermanentLockoutForUser(identifier));
                jSONObject2.put("acceptCrypto", instanceForSensorId.getAcceptCryptoForUser(identifier));
                jSONObject2.put("rejectCrypto", instanceForSensorId.getRejectCryptoForUser(identifier));
                jSONObject2.put("acquireCrypto", instanceForSensorId.getAcquireCryptoForUser(identifier));
                jSONArray.put(jSONObject2);
            }
            jSONObject.put("prints", jSONArray);
        } catch (JSONException e) {
            Slog.e(getTag(), "dump formatting failure", e);
        }
        printWriter.println(jSONObject);
        printWriter.println("HAL deaths since last reboot: " + instanceForSensorId.getHALDeathCount());
        printWriter.println("---AuthSessionCoordinator logs begin---");
        printWriter.println(this.mBiometricContext.getAuthSessionCoordinator());
        printWriter.println("---AuthSessionCoordinator logs end  ---");
        this.mFingerprintSensors.get(i).getScheduler().dump(printWriter);
    }

    @Override // com.android.server.biometrics.sensors.fingerprint.ServiceProvider
    public ITestSession createTestSession(int i, ITestSessionCallback iTestSessionCallback, String str) {
        return this.mFingerprintSensors.get(i).createTestSession(iTestSessionCallback, this.mBiometricStateCallback);
    }

    @Override // android.os.IBinder.DeathRecipient
    public void binderDied() {
        Slog.e(getTag(), "HAL died");
        this.mHandler.post(new Runnable() { // from class: com.android.server.biometrics.sensors.fingerprint.aidl.FingerprintProvider$$ExternalSyntheticLambda6
            @Override // java.lang.Runnable
            public final void run() {
                FingerprintProvider.this.lambda$binderDied$18();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$binderDied$18() {
        this.mDaemon = null;
        for (int i = 0; i < this.mFingerprintSensors.size(); i++) {
            Sensor valueAt = this.mFingerprintSensors.valueAt(i);
            PerformanceTracker.getInstanceForSensorId(this.mFingerprintSensors.keyAt(i)).incrementHALDeathCount();
            valueAt.onBinderDied();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setTestHalEnabled(boolean z) {
        this.mTestHalEnabled = z;
    }

    private List<SensorLocationInternal> getWorkaroundSensorProps(Context context) {
        SensorLocationInternal parseSensorLocation;
        ArrayList arrayList = new ArrayList();
        TypedArray obtainTypedArray = context.getResources().obtainTypedArray(17236143);
        for (int i = 0; i < obtainTypedArray.length(); i++) {
            int resourceId = obtainTypedArray.getResourceId(i, -1);
            if (resourceId > 0 && (parseSensorLocation = parseSensorLocation(context.getResources().obtainTypedArray(resourceId))) != null) {
                arrayList.add(parseSensorLocation);
            }
        }
        obtainTypedArray.recycle();
        return arrayList;
    }

    private SensorLocationInternal parseSensorLocation(TypedArray typedArray) {
        if (typedArray == null) {
            return null;
        }
        try {
            return new SensorLocationInternal(typedArray.getString(0), typedArray.getInt(1, 0), typedArray.getInt(2, 0), typedArray.getInt(3, 0));
        } catch (Exception e) {
            Slog.w(getTag(), "malformed sensor location", e);
            return null;
        }
    }

    @Override // com.android.server.biometrics.sensors.fingerprint.ServiceProvider
    public void scheduleWatchdog(int i) {
        Slog.d(getTag(), "Starting watchdog for fingerprint");
        BiometricScheduler scheduler = this.mFingerprintSensors.get(i).getScheduler();
        if (scheduler == null) {
            return;
        }
        scheduler.startWatchdog();
    }

    @Override // com.android.server.biometrics.sensors.fingerprint.ServiceProvider
    public IFingerprintServiceProviderExt getServiceProviderAidlEx() {
        return mFingerprintProviderExt;
    }

    public void handleOnEnrollment(Fingerprint fingerprint, int i) {
        mFingerprintProviderExt.handleOnEnrollment(fingerprint, i);
    }

    public boolean onAcquired(int i, int i2) {
        return mFingerprintProviderExt.onAcquired(i, i2);
    }

    public boolean onAuthenticated(int i, int i2, int i3, ArrayList<Byte> arrayList) {
        return mFingerprintProviderExt.onAuthenticated(i, i2, i3, arrayList);
    }

    public boolean onError(int i, int i2) {
        return mFingerprintProviderExt.onError(i, i2);
    }

    public void onLockoutTimed() {
        mFingerprintProviderExt.onLockoutTimed();
    }

    public void onLockoutPermanent() {
        mFingerprintProviderExt.onLockoutPermanent();
    }

    public boolean onTransactFromHal(int i, Parcel parcel, Parcel parcel2, int i2) {
        return mFingerprintProviderExt.onTransactFromHal(i, parcel, parcel2, i2);
    }
}
