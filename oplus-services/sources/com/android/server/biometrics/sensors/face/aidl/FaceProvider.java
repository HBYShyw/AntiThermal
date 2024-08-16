package com.android.server.biometrics.sensors.face.aidl;

import android.app.ActivityManager;
import android.app.ActivityTaskManager;
import android.app.SynchronousUserSwitchObserver;
import android.app.TaskStackListener;
import android.content.Context;
import android.content.pm.UserInfo;
import android.hardware.biometrics.ComponentInfoInternal;
import android.hardware.biometrics.IInvalidationCallback;
import android.hardware.biometrics.ITestSession;
import android.hardware.biometrics.ITestSessionCallback;
import android.hardware.biometrics.common.CommonProps;
import android.hardware.biometrics.common.ComponentInfo;
import android.hardware.biometrics.face.IFace;
import android.hardware.biometrics.face.SensorProps;
import android.hardware.face.Face;
import android.hardware.face.FaceAuthenticateOptions;
import android.hardware.face.FaceSensorPropertiesInternal;
import android.hardware.face.IFaceCommandCallback;
import android.hardware.face.IFaceServiceReceiver;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.os.UserManager;
import android.util.Slog;
import android.util.proto.ProtoOutputStream;
import android.view.Surface;
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
import com.android.server.biometrics.sensors.face.FaceUtils;
import com.android.server.biometrics.sensors.face.IServiceProviderWrapper;
import com.android.server.biometrics.sensors.face.ServiceProvider;
import com.android.server.biometrics.sensors.face.UsageStats;
import com.android.server.biometrics.sensors.face.aidl.FaceProvider;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import system.ext.loader.core.ExtLoader;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public class FaceProvider implements IBinder.DeathRecipient, ServiceProvider {
    private static final int ENROLL_TIMEOUT_SEC = 75;
    private static IFaceProviderExt sOplusServiceProvider = (IFaceProviderExt) ExtLoader.type(IFaceProviderExt.class).base((Object) null).create();
    private final AuthSessionCoordinator mAuthSessionCoordinator;
    private final BiometricContext mBiometricContext;
    private final BiometricStateCallback mBiometricStateCallback;
    private final Context mContext;
    private IFace mDaemon;
    private final String mHalInstanceName;
    private final LockoutResetDispatcher mLockoutResetDispatcher;
    private boolean mTestHalEnabled;
    private final UsageStats mUsageStats;
    private final AtomicLong mRequestCounter = new AtomicLong(0);
    private IServiceProviderWrapper mServiceProviderWrapper = new OplusFaceProviderWrapper();

    @VisibleForTesting
    final SensorList<Sensor> mFaceSensors = new SensorList<>(ActivityManager.getService());
    private final Handler mHandler = sOplusServiceProvider.createHandlerWithNewLooper();
    private final ActivityTaskManager mActivityTaskManager = ActivityTaskManager.getInstance();
    private final BiometricTaskStackListener mTaskStackListener = new BiometricTaskStackListener();

    @Override // com.android.server.biometrics.sensors.face.ServiceProvider
    public void dumpHal(int i, FileDescriptor fileDescriptor, String[] strArr) {
    }

    @Override // com.android.server.biometrics.sensors.BiometricServiceProvider
    public void dumpProtoMetrics(int i, FileDescriptor fileDescriptor) {
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public final class BiometricTaskStackListener extends TaskStackListener {
        private BiometricTaskStackListener() {
        }

        public void onTaskStackChanged() {
            FaceProvider.this.mHandler.post(new Runnable() { // from class: com.android.server.biometrics.sensors.face.aidl.FaceProvider$BiometricTaskStackListener$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    FaceProvider.BiometricTaskStackListener.this.lambda$onTaskStackChanged$0();
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onTaskStackChanged$0() {
            for (int i = 0; i < FaceProvider.this.mFaceSensors.size(); i++) {
                BaseClientMonitor currentClient = FaceProvider.this.mFaceSensors.valueAt(i).getScheduler().getCurrentClient();
                if (!(currentClient instanceof AuthenticationClient)) {
                    Slog.e(FaceProvider.this.getTag(), "Task stack changed for client: " + currentClient);
                } else if (!Utils.isKeyguard(FaceProvider.this.mContext, currentClient.getOwnerString()) && !Utils.isSystem(FaceProvider.this.mContext, currentClient.getOwnerString()) && Utils.isBackground(currentClient.getOwnerString()) && !currentClient.isAlreadyDone()) {
                    Slog.e(FaceProvider.this.getTag(), "Stopping background authentication, currentClient: " + currentClient);
                    FaceProvider.this.mFaceSensors.valueAt(i).getScheduler().cancelAuthenticationOrDetection(currentClient.getToken(), currentClient.getRequestId());
                }
            }
        }
    }

    public FaceProvider(Context context, BiometricStateCallback biometricStateCallback, SensorProps[] sensorPropsArr, String str, LockoutResetDispatcher lockoutResetDispatcher, BiometricContext biometricContext) {
        this.mContext = context;
        this.mBiometricStateCallback = biometricStateCallback;
        this.mHalInstanceName = str;
        this.mUsageStats = new UsageStats(context);
        this.mLockoutResetDispatcher = lockoutResetDispatcher;
        this.mBiometricContext = biometricContext;
        this.mAuthSessionCoordinator = biometricContext.getAuthSessionCoordinator();
        for (SensorProps sensorProps : sensorPropsArr) {
            final int i = sensorProps.commonProps.sensorId;
            ArrayList arrayList = new ArrayList();
            ComponentInfo[] componentInfoArr = sensorProps.commonProps.componentInfo;
            if (componentInfoArr != null) {
                int length = componentInfoArr.length;
                int i2 = 0;
                while (i2 < length) {
                    ComponentInfo componentInfo = componentInfoArr[i2];
                    arrayList.add(new ComponentInfoInternal(componentInfo.componentId, componentInfo.hardwareVersion, componentInfo.firmwareVersion, componentInfo.serialNumber, componentInfo.softwareVersion));
                    i2++;
                    componentInfoArr = componentInfoArr;
                }
            }
            CommonProps commonProps = sensorProps.commonProps;
            FaceSensorPropertiesInternal faceSensorPropertiesInternal = new FaceSensorPropertiesInternal(commonProps.sensorId, commonProps.sensorStrength, commonProps.maxEnrollmentsPerUser, arrayList, sensorProps.sensorType, sensorProps.supportsDetectInteraction, sensorProps.halControlsPreview, false);
            Sensor sensor = new Sensor(getTag() + "/" + i, this, this.mContext, this.mHandler, faceSensorPropertiesInternal, lockoutResetDispatcher, this.mBiometricContext);
            this.mFaceSensors.addSensor(i, sensor, sensor.getLazySession().get() == null ? -10000 : sensor.getLazySession().get().getUserId(), new SynchronousUserSwitchObserver() { // from class: com.android.server.biometrics.sensors.face.aidl.FaceProvider.1
                public void onUserSwitching(int i3) {
                    FaceProvider.this.scheduleInternalCleanup(i, i3, null);
                }
            });
            Slog.d(getTag(), "Added: " + faceSensorPropertiesInternal);
        }
        getExtImpl().init(this, this.mContext, sensorPropsArr, this.mHalInstanceName, this.mHandler);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public String getTag() {
        return "FaceProvider/" + this.mHalInstanceName;
    }

    boolean hasHalInstance() {
        if (this.mTestHalEnabled) {
            return true;
        }
        StringBuilder sb = new StringBuilder();
        sb.append(IFace.DESCRIPTOR);
        sb.append("/");
        sb.append(this.mHalInstanceName);
        return ServiceManager.checkService(sb.toString()) != null;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @VisibleForTesting
    public synchronized IFace getHalInstance() {
        if (this.mTestHalEnabled) {
            return new TestHal();
        }
        IFace iFace = this.mDaemon;
        if (iFace != null) {
            return iFace;
        }
        Slog.d(getTag(), "Daemon was null, reconnecting");
        IFace asInterface = IFace.Stub.asInterface(Binder.allowBlocking(ServiceManager.waitForDeclaredService(IFace.DESCRIPTOR + "/" + this.mHalInstanceName)));
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
        for (int i = 0; i < this.mFaceSensors.size(); i++) {
            int keyAt = this.mFaceSensors.keyAt(i);
            scheduleLoadAuthenticatorIds(keyAt);
            scheduleInternalCleanup(keyAt, ActivityManager.getCurrentUser(), null);
        }
        return this.mDaemon;
    }

    private void scheduleForSensor(int i, BaseClientMonitor baseClientMonitor) {
        if (!this.mFaceSensors.contains(i)) {
            throw new IllegalStateException("Unable to schedule client: " + baseClientMonitor + " for sensor: " + i);
        }
        this.mFaceSensors.get(i).getScheduler().scheduleClientMonitor(baseClientMonitor);
    }

    private void scheduleForSensor(int i, BaseClientMonitor baseClientMonitor, ClientMonitorCallback clientMonitorCallback) {
        if (!this.mFaceSensors.contains(i)) {
            throw new IllegalStateException("Unable to schedule client: " + baseClientMonitor + " for sensor: " + i);
        }
        this.mFaceSensors.get(i).getScheduler().scheduleClientMonitor(baseClientMonitor, clientMonitorCallback);
    }

    private void scheduleLoadAuthenticatorIds(int i) {
        Iterator it = UserManager.get(this.mContext).getAliveUsers().iterator();
        while (it.hasNext()) {
            scheduleLoadAuthenticatorIdsForUser(i, ((UserInfo) it.next()).id);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void scheduleLoadAuthenticatorIdsForUser(final int i, final int i2) {
        this.mHandler.post(new Runnable() { // from class: com.android.server.biometrics.sensors.face.aidl.FaceProvider$$ExternalSyntheticLambda1
            @Override // java.lang.Runnable
            public final void run() {
                FaceProvider.this.lambda$scheduleLoadAuthenticatorIdsForUser$0(i, i2);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$scheduleLoadAuthenticatorIdsForUser$0(int i, int i2) {
        scheduleForSensor(i, new FaceGetAuthenticatorIdClient(this.mContext, this.mFaceSensors.get(i).getLazySession(), i2, this.mContext.getOpPackageName(), i, createLogger(0, 0), this.mBiometricContext, this.mFaceSensors.get(i).getAuthenticatorIds()));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void scheduleInvalidationRequest(final int i, final int i2) {
        this.mHandler.post(new Runnable() { // from class: com.android.server.biometrics.sensors.face.aidl.FaceProvider$$ExternalSyntheticLambda9
            @Override // java.lang.Runnable
            public final void run() {
                FaceProvider.this.lambda$scheduleInvalidationRequest$1(i2, i);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$scheduleInvalidationRequest$1(int i, int i2) {
        Context context = this.mContext;
        scheduleForSensor(i2, new InvalidationRequesterClient(context, i, i2, BiometricLogger.ofUnknown(context), this.mBiometricContext, FaceUtils.getInstance(i2)));
    }

    @Override // com.android.server.biometrics.sensors.BiometricServiceProvider
    public boolean containsSensor(int i) {
        return this.mFaceSensors.contains(i);
    }

    @Override // com.android.server.biometrics.sensors.BiometricServiceProvider
    public List<FaceSensorPropertiesInternal> getSensorProperties() {
        ArrayList arrayList = new ArrayList();
        for (int i = 0; i < this.mFaceSensors.size(); i++) {
            arrayList.add(this.mFaceSensors.valueAt(i).getSensorProperties());
        }
        return arrayList;
    }

    @Override // com.android.server.biometrics.sensors.BiometricServiceProvider
    public FaceSensorPropertiesInternal getSensorProperties(int i) {
        return this.mFaceSensors.get(i).getSensorProperties();
    }

    @Override // com.android.server.biometrics.sensors.face.ServiceProvider
    public List<Face> getEnrolledFaces(int i, int i2) {
        return FaceUtils.getInstance(i).getBiometricsForUser(this.mContext, i2);
    }

    @Override // com.android.server.biometrics.sensors.BiometricServiceProvider
    public boolean hasEnrollments(int i, int i2) {
        return !getEnrolledFaces(i, i2).isEmpty();
    }

    @Override // com.android.server.biometrics.sensors.face.ServiceProvider
    public void scheduleInvalidateAuthenticatorId(final int i, final int i2, final IInvalidationCallback iInvalidationCallback) {
        this.mHandler.post(new Runnable() { // from class: com.android.server.biometrics.sensors.face.aidl.FaceProvider$$ExternalSyntheticLambda4
            @Override // java.lang.Runnable
            public final void run() {
                FaceProvider.this.lambda$scheduleInvalidateAuthenticatorId$2(i, i2, iInvalidationCallback);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$scheduleInvalidateAuthenticatorId$2(int i, int i2, IInvalidationCallback iInvalidationCallback) {
        scheduleForSensor(i, new FaceInvalidationClient(this.mContext, this.mFaceSensors.get(i).getLazySession(), i2, i, createLogger(0, 0), this.mBiometricContext, this.mFaceSensors.get(i).getAuthenticatorIds(), iInvalidationCallback));
    }

    @Override // com.android.server.biometrics.sensors.BiometricServiceProvider
    public int getLockoutModeForUser(int i, int i2) {
        return this.mBiometricContext.getAuthSessionCoordinator().getLockoutStateFor(i2, Utils.getCurrentStrength(i));
    }

    @Override // com.android.server.biometrics.sensors.BiometricServiceProvider
    public long getAuthenticatorId(int i, int i2) {
        return this.mFaceSensors.get(i).getAuthenticatorIds().getOrDefault(Integer.valueOf(i2), 0L).longValue();
    }

    @Override // com.android.server.biometrics.sensors.BiometricServiceProvider
    public boolean isHardwareDetected(int i) {
        return hasHalInstance();
    }

    @Override // com.android.server.biometrics.sensors.face.ServiceProvider
    public void scheduleGenerateChallenge(final int i, final int i2, final IBinder iBinder, final IFaceServiceReceiver iFaceServiceReceiver, final String str) {
        this.mHandler.post(new Runnable() { // from class: com.android.server.biometrics.sensors.face.aidl.FaceProvider$$ExternalSyntheticLambda15
            @Override // java.lang.Runnable
            public final void run() {
                FaceProvider.this.lambda$scheduleGenerateChallenge$3(i, iBinder, iFaceServiceReceiver, i2, str);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$scheduleGenerateChallenge$3(int i, IBinder iBinder, IFaceServiceReceiver iFaceServiceReceiver, int i2, String str) {
        scheduleForSensor(i, new FaceGenerateChallengeClient(this.mContext, this.mFaceSensors.get(i).getLazySession(), iBinder, new ClientMonitorCallbackConverter(iFaceServiceReceiver), i2, str, i, createLogger(0, 0), this.mBiometricContext));
    }

    @Override // com.android.server.biometrics.sensors.face.ServiceProvider
    public void scheduleRevokeChallenge(final int i, final int i2, final IBinder iBinder, final String str, final long j) {
        this.mHandler.post(new Runnable() { // from class: com.android.server.biometrics.sensors.face.aidl.FaceProvider$$ExternalSyntheticLambda8
            @Override // java.lang.Runnable
            public final void run() {
                FaceProvider.this.lambda$scheduleRevokeChallenge$4(i, iBinder, i2, str, j);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$scheduleRevokeChallenge$4(int i, IBinder iBinder, int i2, String str, long j) {
        scheduleForSensor(i, new FaceRevokeChallengeClient(this.mContext, this.mFaceSensors.get(i).getLazySession(), iBinder, i2, str, i, createLogger(0, 0), this.mBiometricContext, j));
    }

    @Override // com.android.server.biometrics.sensors.face.ServiceProvider
    public long scheduleEnroll(final int i, final IBinder iBinder, final byte[] bArr, final int i2, final IFaceServiceReceiver iFaceServiceReceiver, final String str, final int[] iArr, final Surface surface, final boolean z) {
        final long incrementAndGet = this.mRequestCounter.incrementAndGet();
        this.mHandler.post(new Runnable() { // from class: com.android.server.biometrics.sensors.face.aidl.FaceProvider$$ExternalSyntheticLambda13
            @Override // java.lang.Runnable
            public final void run() {
                FaceProvider.this.lambda$scheduleEnroll$5(i, iBinder, iFaceServiceReceiver, i2, bArr, str, incrementAndGet, iArr, surface, z);
            }
        });
        return incrementAndGet;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$scheduleEnroll$5(final int i, IBinder iBinder, IFaceServiceReceiver iFaceServiceReceiver, final int i2, byte[] bArr, String str, long j, int[] iArr, Surface surface, boolean z) {
        scheduleForSensor(i, new FaceEnrollClient(this.mContext, this.mFaceSensors.get(i).getLazySession(), iBinder, new ClientMonitorCallbackConverter(iFaceServiceReceiver), i2, bArr, str, j, FaceUtils.getInstance(i), iArr, 75, surface, i, createLogger(1, 0), this.mBiometricContext, this.mFaceSensors.get(i).getSensorProperties().maxEnrollmentsPerUser, z), new ClientMonitorCompositeCallback(this.mBiometricStateCallback, new ClientMonitorCallback() { // from class: com.android.server.biometrics.sensors.face.aidl.FaceProvider.2
            @Override // com.android.server.biometrics.sensors.ClientMonitorCallback
            public void onClientFinished(BaseClientMonitor baseClientMonitor, boolean z2) {
                super.onClientFinished(baseClientMonitor, z2);
                if (z2) {
                    FaceProvider.this.scheduleLoadAuthenticatorIdsForUser(i, i2);
                    FaceProvider.this.scheduleInvalidationRequest(i, i2);
                }
            }
        }));
    }

    @Override // com.android.server.biometrics.sensors.face.ServiceProvider
    public void cancelEnrollment(final int i, final IBinder iBinder, final long j) {
        this.mHandler.post(new Runnable() { // from class: com.android.server.biometrics.sensors.face.aidl.FaceProvider$$ExternalSyntheticLambda14
            @Override // java.lang.Runnable
            public final void run() {
                FaceProvider.this.lambda$cancelEnrollment$6(i, iBinder, j);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$cancelEnrollment$6(int i, IBinder iBinder, long j) {
        this.mFaceSensors.get(i).getScheduler().cancelEnrollment(iBinder, j);
    }

    @Override // com.android.server.biometrics.sensors.face.ServiceProvider
    public long scheduleFaceDetect(final IBinder iBinder, final ClientMonitorCallbackConverter clientMonitorCallbackConverter, final FaceAuthenticateOptions faceAuthenticateOptions, final int i) {
        final long incrementAndGet = this.mRequestCounter.incrementAndGet();
        final int sensorId = faceAuthenticateOptions.getSensorId();
        this.mHandler.post(new Runnable() { // from class: com.android.server.biometrics.sensors.face.aidl.FaceProvider$$ExternalSyntheticLambda2
            @Override // java.lang.Runnable
            public final void run() {
                FaceProvider.this.lambda$scheduleFaceDetect$7(sensorId, iBinder, incrementAndGet, clientMonitorCallbackConverter, faceAuthenticateOptions, i);
            }
        });
        return incrementAndGet;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$scheduleFaceDetect$7(int i, IBinder iBinder, long j, ClientMonitorCallbackConverter clientMonitorCallbackConverter, FaceAuthenticateOptions faceAuthenticateOptions, int i2) {
        scheduleForSensor(i, new FaceDetectClient(this.mContext, this.mFaceSensors.get(i).getLazySession(), iBinder, j, clientMonitorCallbackConverter, faceAuthenticateOptions, createLogger(2, i2), this.mBiometricContext, Utils.isStrongBiometric(i)), this.mBiometricStateCallback);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$cancelFaceDetect$8(int i, IBinder iBinder, long j) {
        this.mFaceSensors.get(i).getScheduler().cancelAuthenticationOrDetection(iBinder, j);
    }

    @Override // com.android.server.biometrics.sensors.face.ServiceProvider
    public void cancelFaceDetect(final int i, final IBinder iBinder, final long j) {
        this.mHandler.post(new Runnable() { // from class: com.android.server.biometrics.sensors.face.aidl.FaceProvider$$ExternalSyntheticLambda12
            @Override // java.lang.Runnable
            public final void run() {
                FaceProvider.this.lambda$cancelFaceDetect$8(i, iBinder, j);
            }
        });
    }

    @Override // com.android.server.biometrics.sensors.face.ServiceProvider
    public void scheduleAuthenticate(final IBinder iBinder, final long j, final int i, final ClientMonitorCallbackConverter clientMonitorCallbackConverter, final FaceAuthenticateOptions faceAuthenticateOptions, final long j2, final boolean z, final int i2, final boolean z2) {
        this.mHandler.post(new Runnable() { // from class: com.android.server.biometrics.sensors.face.aidl.FaceProvider$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                FaceProvider.this.lambda$scheduleAuthenticate$9(faceAuthenticateOptions, z2, iBinder, j2, clientMonitorCallbackConverter, j, z, i, i2);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$scheduleAuthenticate$9(FaceAuthenticateOptions faceAuthenticateOptions, boolean z, IBinder iBinder, final long j, ClientMonitorCallbackConverter clientMonitorCallbackConverter, long j2, boolean z2, int i, int i2) {
        final int userId = faceAuthenticateOptions.getUserId();
        final int sensorId = faceAuthenticateOptions.getSensorId();
        final FaceAuthenticationClient faceAuthenticationClient = new FaceAuthenticationClient(this.mContext, this.mFaceSensors.get(sensorId).getLazySession(), iBinder, j, clientMonitorCallbackConverter, j2, z2, faceAuthenticateOptions, i, false, createLogger(2, i2), this.mBiometricContext, Utils.isStrongBiometric(sensorId), this.mUsageStats, this.mFaceSensors.get(sensorId).getLockoutCache(), !z ? getExtImpl().isBackgroundAuthAllow(faceAuthenticateOptions.getOpPackageName()) : z, Utils.getCurrentStrength(sensorId));
        scheduleForSensor(sensorId, faceAuthenticationClient, new ClientMonitorCallback() { // from class: com.android.server.biometrics.sensors.face.aidl.FaceProvider.3
            @Override // com.android.server.biometrics.sensors.ClientMonitorCallback
            public void onClientStarted(BaseClientMonitor baseClientMonitor) {
                FaceProvider.this.mAuthSessionCoordinator.authStartedFor(userId, sensorId, j);
            }

            @Override // com.android.server.biometrics.sensors.ClientMonitorCallback
            public void onClientFinished(BaseClientMonitor baseClientMonitor, boolean z3) {
                FaceProvider.this.mAuthSessionCoordinator.authEndedFor(userId, Utils.getCurrentStrength(sensorId), sensorId, j, faceAuthenticationClient.wasAuthSuccessful());
            }
        });
    }

    @Override // com.android.server.biometrics.sensors.face.ServiceProvider
    public long scheduleAuthenticate(IBinder iBinder, long j, int i, ClientMonitorCallbackConverter clientMonitorCallbackConverter, FaceAuthenticateOptions faceAuthenticateOptions, boolean z, int i2, boolean z2) {
        long incrementAndGet = this.mRequestCounter.incrementAndGet();
        scheduleAuthenticate(iBinder, j, i, clientMonitorCallbackConverter, faceAuthenticateOptions, incrementAndGet, z, i2, z2);
        return incrementAndGet;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$cancelAuthentication$10(int i, IBinder iBinder, long j) {
        this.mFaceSensors.get(i).getScheduler().cancelAuthenticationOrDetection(iBinder, j);
    }

    @Override // com.android.server.biometrics.sensors.face.ServiceProvider
    public void cancelAuthentication(final int i, final IBinder iBinder, final long j) {
        this.mHandler.post(new Runnable() { // from class: com.android.server.biometrics.sensors.face.aidl.FaceProvider$$ExternalSyntheticLambda7
            @Override // java.lang.Runnable
            public final void run() {
                FaceProvider.this.lambda$cancelAuthentication$10(i, iBinder, j);
            }
        });
    }

    @Override // com.android.server.biometrics.sensors.face.ServiceProvider
    public void scheduleRemove(int i, IBinder iBinder, int i2, int i3, IFaceServiceReceiver iFaceServiceReceiver, String str) {
        scheduleRemoveSpecifiedIds(i, iBinder, new int[]{i2}, i3, iFaceServiceReceiver, str);
    }

    @Override // com.android.server.biometrics.sensors.face.ServiceProvider
    public void scheduleRemoveAll(int i, IBinder iBinder, int i2, IFaceServiceReceiver iFaceServiceReceiver, String str) {
        List<Face> biometricsForUser = FaceUtils.getInstance(i).getBiometricsForUser(this.mContext, i2);
        int[] iArr = new int[biometricsForUser.size()];
        for (int i3 = 0; i3 < biometricsForUser.size(); i3++) {
            iArr[i3] = biometricsForUser.get(i3).getBiometricId();
        }
        scheduleRemoveSpecifiedIds(i, iBinder, iArr, i2, iFaceServiceReceiver, str);
    }

    private void scheduleRemoveSpecifiedIds(final int i, final IBinder iBinder, final int[] iArr, final int i2, final IFaceServiceReceiver iFaceServiceReceiver, final String str) {
        this.mHandler.post(new Runnable() { // from class: com.android.server.biometrics.sensors.face.aidl.FaceProvider$$ExternalSyntheticLambda10
            @Override // java.lang.Runnable
            public final void run() {
                FaceProvider.this.lambda$scheduleRemoveSpecifiedIds$11(i, iBinder, iFaceServiceReceiver, iArr, i2, str);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$scheduleRemoveSpecifiedIds$11(int i, IBinder iBinder, IFaceServiceReceiver iFaceServiceReceiver, int[] iArr, int i2, String str) {
        scheduleForSensor(i, new FaceRemovalClient(this.mContext, this.mFaceSensors.get(i).getLazySession(), iBinder, new ClientMonitorCallbackConverter(iFaceServiceReceiver), iArr, i2, str, FaceUtils.getInstance(i), i, createLogger(4, 0), this.mBiometricContext, this.mFaceSensors.get(i).getAuthenticatorIds()), this.mBiometricStateCallback);
    }

    @Override // com.android.server.biometrics.sensors.face.ServiceProvider
    public void scheduleResetLockout(final int i, final int i2, final byte[] bArr) {
        this.mHandler.post(new Runnable() { // from class: com.android.server.biometrics.sensors.face.aidl.FaceProvider$$ExternalSyntheticLambda16
            @Override // java.lang.Runnable
            public final void run() {
                FaceProvider.this.lambda$scheduleResetLockout$12(i, i2, bArr);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$scheduleResetLockout$12(int i, int i2, byte[] bArr) {
        scheduleForSensor(i, new FaceResetLockoutClient(this.mContext, this.mFaceSensors.get(i).getLazySession(), i2, this.mContext.getOpPackageName(), i, createLogger(0, 0), this.mBiometricContext, bArr, this.mFaceSensors.get(i).getLockoutCache(), this.mLockoutResetDispatcher, Utils.getCurrentStrength(i)));
        getExtImpl().resetFaceLockout(bArr);
    }

    @Override // com.android.server.biometrics.sensors.face.ServiceProvider
    public void scheduleSetFeature(final int i, final IBinder iBinder, final int i2, final int i3, final boolean z, final byte[] bArr, final IFaceServiceReceiver iFaceServiceReceiver, String str) {
        this.mHandler.post(new Runnable() { // from class: com.android.server.biometrics.sensors.face.aidl.FaceProvider$$ExternalSyntheticLambda17
            @Override // java.lang.Runnable
            public final void run() {
                FaceProvider.this.lambda$scheduleSetFeature$13(i, i2, iBinder, iFaceServiceReceiver, i3, z, bArr);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$scheduleSetFeature$13(int i, int i2, IBinder iBinder, IFaceServiceReceiver iFaceServiceReceiver, int i3, boolean z, byte[] bArr) {
        if (FaceUtils.getInstance(i).getBiometricsForUser(this.mContext, i2).isEmpty()) {
            Slog.w(getTag(), "Ignoring setFeature, no templates enrolled for user: " + i2);
            return;
        }
        scheduleForSensor(i, new FaceSetFeatureClient(this.mContext, this.mFaceSensors.get(i).getLazySession(), iBinder, new ClientMonitorCallbackConverter(iFaceServiceReceiver), i2, this.mContext.getOpPackageName(), i, BiometricLogger.ofUnknown(this.mContext), this.mBiometricContext, i3, z, bArr));
    }

    @Override // com.android.server.biometrics.sensors.face.ServiceProvider
    public void scheduleGetFeature(final int i, final IBinder iBinder, final int i2, int i3, final ClientMonitorCallbackConverter clientMonitorCallbackConverter, String str) {
        this.mHandler.post(new Runnable() { // from class: com.android.server.biometrics.sensors.face.aidl.FaceProvider$$ExternalSyntheticLambda3
            @Override // java.lang.Runnable
            public final void run() {
                FaceProvider.this.lambda$scheduleGetFeature$14(i, i2, iBinder, clientMonitorCallbackConverter);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$scheduleGetFeature$14(int i, int i2, IBinder iBinder, ClientMonitorCallbackConverter clientMonitorCallbackConverter) {
        if (FaceUtils.getInstance(i).getBiometricsForUser(this.mContext, i2).isEmpty()) {
            Slog.w(getTag(), "Ignoring getFeature, no templates enrolled for user: " + i2);
            return;
        }
        scheduleForSensor(i, new FaceGetFeatureClient(this.mContext, this.mFaceSensors.get(i).getLazySession(), iBinder, clientMonitorCallbackConverter, i2, this.mContext.getOpPackageName(), i, BiometricLogger.ofUnknown(this.mContext), this.mBiometricContext));
    }

    @Override // com.android.server.biometrics.sensors.face.ServiceProvider
    public void startPreparedClient(final int i, final int i2) {
        this.mHandler.post(new Runnable() { // from class: com.android.server.biometrics.sensors.face.aidl.FaceProvider$$ExternalSyntheticLambda6
            @Override // java.lang.Runnable
            public final void run() {
                FaceProvider.this.lambda$startPreparedClient$15(i, i2);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$startPreparedClient$15(int i, int i2) {
        this.mFaceSensors.get(i).getScheduler().startPreparedClient(i2);
    }

    @Override // com.android.server.biometrics.sensors.face.ServiceProvider
    public void scheduleInternalCleanup(int i, int i2, ClientMonitorCallback clientMonitorCallback) {
        scheduleInternalCleanup(i, i2, clientMonitorCallback, false);
    }

    @Override // com.android.server.biometrics.sensors.face.ServiceProvider
    public void scheduleInternalCleanup(final int i, final int i2, final ClientMonitorCallback clientMonitorCallback, final boolean z) {
        this.mHandler.post(new Runnable() { // from class: com.android.server.biometrics.sensors.face.aidl.FaceProvider$$ExternalSyntheticLambda5
            @Override // java.lang.Runnable
            public final void run() {
                FaceProvider.this.lambda$scheduleInternalCleanup$16(i, i2, z, clientMonitorCallback);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$scheduleInternalCleanup$16(int i, int i2, boolean z, ClientMonitorCallback clientMonitorCallback) {
        FaceInternalCleanupClient faceInternalCleanupClient = new FaceInternalCleanupClient(this.mContext, this.mFaceSensors.get(i).getLazySession(), i2, this.mContext.getOpPackageName(), i, createLogger(3, 0), this.mBiometricContext, FaceUtils.getInstance(i), this.mFaceSensors.get(i).getAuthenticatorIds());
        if (z) {
            faceInternalCleanupClient.setFavorHalEnrollments();
        }
        scheduleForSensor(i, faceInternalCleanupClient, new ClientMonitorCompositeCallback(clientMonitorCallback, this.mBiometricStateCallback));
    }

    private BiometricLogger createLogger(int i, int i2) {
        return new BiometricLogger(this.mContext, 4, i, i2);
    }

    @Override // com.android.server.biometrics.sensors.BiometricServiceProvider
    public void dumpProtoState(int i, ProtoOutputStream protoOutputStream, boolean z) {
        if (this.mFaceSensors.contains(i)) {
            this.mFaceSensors.get(i).dumpProtoState(i, protoOutputStream, z);
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
                int size = FaceUtils.getInstance(i).getBiometricsForUser(this.mContext, identifier).size();
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
        this.mFaceSensors.get(i).getScheduler().dump(printWriter);
        this.mUsageStats.print(printWriter);
    }

    @Override // com.android.server.biometrics.sensors.face.ServiceProvider
    public ITestSession createTestSession(int i, ITestSessionCallback iTestSessionCallback, String str) {
        return this.mFaceSensors.get(i).createTestSession(iTestSessionCallback);
    }

    @Override // android.os.IBinder.DeathRecipient
    public void binderDied() {
        Slog.e(getTag(), "HAL died");
        this.mHandler.post(new Runnable() { // from class: com.android.server.biometrics.sensors.face.aidl.FaceProvider$$ExternalSyntheticLambda11
            @Override // java.lang.Runnable
            public final void run() {
                FaceProvider.this.lambda$binderDied$17();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$binderDied$17() {
        this.mDaemon = null;
        for (int i = 0; i < this.mFaceSensors.size(); i++) {
            Sensor valueAt = this.mFaceSensors.valueAt(i);
            PerformanceTracker.getInstanceForSensorId(this.mFaceSensors.keyAt(i)).incrementHALDeathCount();
            valueAt.onBinderDied();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setTestHalEnabled(boolean z) {
        this.mTestHalEnabled = z;
    }

    @Override // com.android.server.biometrics.sensors.face.ServiceProvider
    public void scheduleWatchdog(int i) {
        Slog.d(getTag(), "Starting watchdog for face");
        BiometricScheduler scheduler = this.mFaceSensors.get(i).getScheduler();
        if (scheduler == null) {
            return;
        }
        scheduler.startWatchdog();
    }

    public void handleOnFaceCmd(int i, ArrayList<Byte> arrayList, int i2) {
        getExtImpl().handleOnFaceCmd(i, arrayList, i2);
    }

    public static IFaceProviderExt getExtImpl() {
        IFaceProviderExt iFaceProviderExt = sOplusServiceProvider;
        return iFaceProviderExt == null ? new IFaceProviderExt() { // from class: com.android.server.biometrics.sensors.face.aidl.FaceProvider.4
        } : iFaceProviderExt;
    }

    @Override // com.android.server.biometrics.sensors.face.ServiceProvider
    public IServiceProviderWrapper getServiceProviderWrapper() {
        return this.mServiceProviderWrapper;
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    private class OplusFaceProviderWrapper implements IServiceProviderWrapper {
        private OplusFaceProviderWrapper() {
        }

        @Override // com.android.server.biometrics.sensors.face.IServiceProviderWrapper
        public long getLockoutAttemptDeadline(int i) {
            return FaceProvider.getExtImpl().getLockoutAttemptDeadline(i);
        }

        @Override // com.android.server.biometrics.sensors.face.IServiceProviderWrapper
        public int getFailedAttempts() {
            return FaceProvider.getExtImpl().getFailedAttempts();
        }

        @Override // com.android.server.biometrics.sensors.face.IServiceProviderWrapper
        public void resetFaceDaemon() {
            FaceProvider.getExtImpl().resetFaceDaemon();
        }

        @Override // com.android.server.biometrics.sensors.face.IServiceProviderWrapper
        public int getFaceProcessMemory() {
            return FaceProvider.getExtImpl().getFaceProcessMemory();
        }

        @Override // com.android.server.biometrics.sensors.face.IServiceProviderWrapper
        public int scheduleSendFaceCmd(int i, int i2, byte[] bArr) {
            return FaceProvider.getExtImpl().scheduleSendFaceCmd(i, i2, bArr);
        }

        @Override // com.android.server.biometrics.sensors.face.IServiceProviderWrapper
        public void authPreOperation(IBinder iBinder, String str) {
            FaceProvider.getExtImpl().authPreOperation(iBinder, str);
        }

        @Override // com.android.server.biometrics.sensors.face.IServiceProviderWrapper
        public void onSystemReady() {
            FaceProvider.getExtImpl().onSystemReady();
        }

        @Override // com.android.server.biometrics.sensors.face.IServiceProviderWrapper
        public void dumpInternal(PrintWriter printWriter, String[] strArr) {
            FaceProvider.getExtImpl().dumpInternal(printWriter, strArr);
        }

        @Override // com.android.server.biometrics.sensors.face.IServiceProviderWrapper
        public void onAuthenticated(boolean z) {
            FaceProvider.getExtImpl().onAuthenticated(z);
        }

        @Override // com.android.server.biometrics.sensors.face.IServiceProviderWrapper
        public void onError(int i, int i2) {
            FaceProvider.getExtImpl().onError(i, i2);
        }

        @Override // com.android.server.biometrics.sensors.face.IServiceProviderWrapper
        public boolean onAcquired(int i, int i2) {
            return FaceProvider.getExtImpl().onAcquired(i, i2);
        }

        @Override // com.android.server.biometrics.sensors.face.IServiceProviderWrapper
        public void onLockoutTimed() {
            FaceProvider.getExtImpl().onLockoutTimed();
        }

        @Override // com.android.server.biometrics.sensors.face.IServiceProviderWrapper
        public void onLockoutPermanent() {
            FaceProvider.getExtImpl().onLockoutPermanent();
        }

        @Override // com.android.server.biometrics.sensors.face.IServiceProviderWrapper
        public int regsiterFaceCmdCallback(IFaceCommandCallback iFaceCommandCallback) {
            return FaceProvider.getExtImpl().regsiterFaceCmdCallback(iFaceCommandCallback);
        }

        @Override // com.android.server.biometrics.sensors.face.IServiceProviderWrapper
        public int unregsiterFaceCmdCallback(IFaceCommandCallback iFaceCommandCallback) {
            return FaceProvider.getExtImpl().unregsiterFaceCmdCallback(iFaceCommandCallback);
        }
    }
}
