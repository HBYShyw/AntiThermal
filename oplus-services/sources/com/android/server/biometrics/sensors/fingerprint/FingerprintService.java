package com.android.server.biometrics.sensors.fingerprint;

import android.R;
import android.annotation.EnforcePermission;
import android.app.ActivityManager;
import android.app.AppOpsManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.hardware.biometrics.BiometricPrompt;
import android.hardware.biometrics.IBiometricSensorReceiver;
import android.hardware.biometrics.IBiometricService;
import android.hardware.biometrics.IBiometricServiceLockoutResetCallback;
import android.hardware.biometrics.IBiometricStateListener;
import android.hardware.biometrics.IInvalidationCallback;
import android.hardware.biometrics.ITestSession;
import android.hardware.biometrics.ITestSessionCallback;
import android.hardware.biometrics.fingerprint.IFingerprint;
import android.hardware.biometrics.fingerprint.PointerContext;
import android.hardware.fingerprint.Fingerprint;
import android.hardware.fingerprint.FingerprintAuthenticateOptions;
import android.hardware.fingerprint.FingerprintSensorPropertiesInternal;
import android.hardware.fingerprint.FingerprintServiceReceiver;
import android.hardware.fingerprint.IFingerprintAuthenticatorsRegisteredCallback;
import android.hardware.fingerprint.IFingerprintClientActiveCallback;
import android.hardware.fingerprint.IFingerprintService;
import android.hardware.fingerprint.IFingerprintServiceReceiver;
import android.hardware.fingerprint.ISidefpsController;
import android.hardware.fingerprint.IUdfpsOverlay;
import android.hardware.fingerprint.IUdfpsOverlayController;
import android.os.Binder;
import android.os.Build;
import android.os.CancellationSignal;
import android.os.Handler;
import android.os.IBinder;
import android.os.RemoteException;
import android.os.ResultReceiver;
import android.os.ServiceManager;
import android.os.ShellCallback;
import android.os.UserHandle;
import android.os.UserManager;
import android.provider.Settings;
import android.util.EventLog;
import android.util.Pair;
import android.util.Slog;
import android.util.proto.ProtoOutputStream;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.util.DumpUtils;
import com.android.internal.widget.LockPatternUtils;
import com.android.server.SystemService;
import com.android.server.biometrics.Utils;
import com.android.server.biometrics.log.BiometricContext;
import com.android.server.biometrics.sensors.BaseClientMonitor;
import com.android.server.biometrics.sensors.BiometricStateCallback;
import com.android.server.biometrics.sensors.ClientMonitorCallback;
import com.android.server.biometrics.sensors.ClientMonitorCallbackConverter;
import com.android.server.biometrics.sensors.LockoutResetDispatcher;
import com.android.server.biometrics.sensors.fingerprint.FingerprintService;
import com.android.server.biometrics.sensors.fingerprint.aidl.FingerprintProvider;
import com.android.server.biometrics.sensors.fingerprint.hidl.Fingerprint21;
import com.android.server.biometrics.sensors.fingerprint.hidl.Fingerprint21UdfpsMock;
import com.android.server.companion.virtual.VirtualDeviceManagerInternal;
import com.google.android.collect.Lists;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.function.Supplier;
import system.ext.loader.core.ExtLoader;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public class FingerprintService extends SystemService {
    protected static final String TAG = "FingerprintService";
    private final Supplier<String[]> mAidlInstanceNameSupplier;
    private final AppOpsManager mAppOps;
    private final BiometricContext mBiometricContext;
    private final BiometricStateCallback<ServiceProvider, FingerprintSensorPropertiesInternal> mBiometricStateCallback;
    private final Function<String, FingerprintProvider> mFingerprintProvider;
    private final GestureAvailabilityDispatcher mGestureAvailabilityDispatcher;
    private final Handler mHandler;
    private IFingerprintServiceExt mIOplusFingerprintServiceExt;
    private final LockPatternUtils mLockPatternUtils;
    private final LockoutResetDispatcher mLockoutResetDispatcher;
    private IFingerprintServiceWrapper mOplusFingerprintServiceWrapper;
    private final FingerprintServiceRegistry mRegistry;

    @VisibleForTesting
    final IFingerprintService.Stub mServiceWrapper;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: com.android.server.biometrics.sensors.fingerprint.FingerprintService$1, reason: invalid class name */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public class AnonymousClass1 extends IFingerprintService.Stub {
        AnonymousClass1() {
        }

        @EnforcePermission("android.permission.TEST_BIOMETRIC")
        public ITestSession createTestSession(int i, ITestSessionCallback iTestSessionCallback, String str) {
            super.createTestSession_enforcePermission();
            ServiceProvider providerForSensor = FingerprintService.this.mRegistry.getProviderForSensor(i);
            if (providerForSensor == null) {
                Slog.w(FingerprintService.TAG, "Null provider for createTestSession, sensorId: " + i);
                return null;
            }
            return providerForSensor.createTestSession(i, iTestSessionCallback, str);
        }

        @EnforcePermission("android.permission.USE_BIOMETRIC_INTERNAL")
        public byte[] dumpSensorServiceStateProto(int i, boolean z) {
            super.dumpSensorServiceStateProto_enforcePermission();
            ProtoOutputStream protoOutputStream = new ProtoOutputStream();
            ServiceProvider providerForSensor = FingerprintService.this.mRegistry.getProviderForSensor(i);
            if (providerForSensor != null) {
                providerForSensor.dumpProtoState(i, protoOutputStream, z);
            }
            protoOutputStream.flush();
            return protoOutputStream.getBytes();
        }

        public List<FingerprintSensorPropertiesInternal> getSensorPropertiesInternal(String str) {
            if (FingerprintService.this.getContext().checkCallingOrSelfPermission("android.permission.USE_BIOMETRIC_INTERNAL") != 0) {
                Utils.checkPermission(FingerprintService.this.getContext(), "android.permission.TEST_BIOMETRIC");
            }
            return FingerprintService.this.mRegistry.getAllProperties();
        }

        @EnforcePermission("android.permission.USE_BIOMETRIC_INTERNAL")
        public FingerprintSensorPropertiesInternal getSensorProperties(int i, String str) {
            super.getSensorProperties_enforcePermission();
            ServiceProvider providerForSensor = FingerprintService.this.mRegistry.getProviderForSensor(i);
            if (providerForSensor == null) {
                Slog.w(FingerprintService.TAG, "No matching sensor for getSensorProperties, sensorId: " + i + ", caller: " + str);
                return null;
            }
            return providerForSensor.getSensorProperties(i);
        }

        @EnforcePermission("android.permission.MANAGE_FINGERPRINT")
        public void generateChallenge(IBinder iBinder, int i, int i2, IFingerprintServiceReceiver iFingerprintServiceReceiver, String str) {
            super.generateChallenge_enforcePermission();
            ServiceProvider providerForSensor = FingerprintService.this.mRegistry.getProviderForSensor(i);
            if (providerForSensor == null) {
                Slog.w(FingerprintService.TAG, "No matching sensor for generateChallenge, sensorId: " + i);
                return;
            }
            providerForSensor.scheduleGenerateChallenge(i, i2, iBinder, iFingerprintServiceReceiver, str);
        }

        @EnforcePermission("android.permission.MANAGE_FINGERPRINT")
        public void revokeChallenge(IBinder iBinder, int i, int i2, String str, long j) {
            super.revokeChallenge_enforcePermission();
            ServiceProvider providerForSensor = FingerprintService.this.mRegistry.getProviderForSensor(i);
            if (providerForSensor == null) {
                Slog.w(FingerprintService.TAG, "No matching sensor for revokeChallenge, sensorId: " + i);
                return;
            }
            providerForSensor.scheduleRevokeChallenge(i, i2, iBinder, str, j);
        }

        @EnforcePermission("android.permission.MANAGE_FINGERPRINT")
        public long enroll(IBinder iBinder, byte[] bArr, int i, IFingerprintServiceReceiver iFingerprintServiceReceiver, String str, int i2) {
            super.enroll_enforcePermission();
            Pair<Integer, ServiceProvider> singleProvider = FingerprintService.this.mRegistry.getSingleProvider();
            if (singleProvider == null) {
                Slog.w(FingerprintService.TAG, "Null provider for enroll");
                return -1L;
            }
            if (FingerprintService.this.getWrapper().getExtImpl().enrollPreOperation(iBinder, str, i)) {
                FingerprintService.this.getWrapper().getExtImpl().notifyOperationCanceled(iFingerprintServiceReceiver);
                return -1L;
            }
            return ((ServiceProvider) singleProvider.second).scheduleEnroll(((Integer) singleProvider.first).intValue(), iBinder, bArr, i, iFingerprintServiceReceiver, str, i2);
        }

        @EnforcePermission("android.permission.MANAGE_FINGERPRINT")
        public void cancelEnrollment(IBinder iBinder, long j) {
            super.cancelEnrollment_enforcePermission();
            Pair<Integer, ServiceProvider> singleProvider = FingerprintService.this.mRegistry.getSingleProvider();
            if (singleProvider == null) {
                Slog.w(FingerprintService.TAG, "Null provider for cancelEnrollment");
            } else {
                ((ServiceProvider) singleProvider.second).cancelEnrollment(((Integer) singleProvider.first).intValue(), iBinder, j);
            }
        }

        public long authenticate(IBinder iBinder, long j, IFingerprintServiceReceiver iFingerprintServiceReceiver, FingerprintAuthenticateOptions fingerprintAuthenticateOptions) {
            Pair<Integer, ServiceProvider> pair;
            int callingUid = Binder.getCallingUid();
            int callingPid = Binder.getCallingPid();
            int callingUserId = UserHandle.getCallingUserId();
            String opPackageName = fingerprintAuthenticateOptions.getOpPackageName();
            String attributionTag = fingerprintAuthenticateOptions.getAttributionTag();
            int userId = fingerprintAuthenticateOptions.getUserId();
            if (!FingerprintService.this.canUseFingerprint(opPackageName, attributionTag, true, callingUid, callingPid, callingUserId)) {
                Slog.w(FingerprintService.TAG, "Authenticate rejecting package: " + opPackageName);
                return -1L;
            }
            int changeUserIdIfNeeded = FingerprintService.this.getWrapper().getExtImpl().changeUserIdIfNeeded(userId);
            boolean isKeyguard = Utils.isKeyguard(FingerprintService.this.getContext(), opPackageName);
            long clearCallingIdentity = Binder.clearCallingIdentity();
            if (isKeyguard) {
                try {
                    if (Utils.isUserEncryptedOrLockdown(FingerprintService.this.mLockPatternUtils, changeUserIdIfNeeded)) {
                        EventLog.writeEvent(1397638484, "79776455");
                        Slog.e(FingerprintService.TAG, "Authenticate invoked when user is encrypted or lockdown");
                        return -1L;
                    }
                } finally {
                }
            }
            Binder.restoreCallingIdentity(clearCallingIdentity);
            boolean z = FingerprintService.this.getContext().checkCallingPermission("android.permission.MANAGE_FINGERPRINT") != 0;
            int i = isKeyguard ? 1 : 3;
            if (fingerprintAuthenticateOptions.getSensorId() == -1) {
                pair = FingerprintService.this.mRegistry.getSingleProvider();
            } else {
                Utils.checkPermission(FingerprintService.this.getContext(), "android.permission.USE_BIOMETRIC_INTERNAL");
                pair = new Pair<>(Integer.valueOf(fingerprintAuthenticateOptions.getSensorId()), FingerprintService.this.mRegistry.getProviderForSensor(fingerprintAuthenticateOptions.getSensorId()));
            }
            if (pair == null) {
                Slog.w(FingerprintService.TAG, "Null provider for authenticate");
                return -1L;
            }
            fingerprintAuthenticateOptions.setSensorId(((Integer) pair.first).intValue());
            if (FingerprintService.this.getWrapper().getExtImpl().authPreOperation(iBinder, opPackageName, fingerprintAuthenticateOptions.getSensorId())) {
                FingerprintService.this.getWrapper().getExtImpl().notifyOperationCanceled(iFingerprintServiceReceiver);
                return -1L;
            }
            FingerprintSensorPropertiesInternal sensorProperties = ((ServiceProvider) pair.second).getSensorProperties(fingerprintAuthenticateOptions.getSensorId());
            if (!isKeyguard && !Utils.isSettings(FingerprintService.this.getContext(), opPackageName) && !FingerprintService.this.getWrapper().getExtImpl().skipAuthWithPrompt(opPackageName) && sensorProperties != null && (sensorProperties.isAnyUdfpsType() || sensorProperties.isAnySidefpsType())) {
                try {
                    return authenticateWithPrompt(j, sensorProperties, callingUid, callingUserId, iFingerprintServiceReceiver, opPackageName, fingerprintAuthenticateOptions.isIgnoreEnrollmentState());
                } catch (PackageManager.NameNotFoundException e) {
                    Slog.e(FingerprintService.TAG, "Invalid package", e);
                    return -1L;
                }
            }
            clearCallingIdentity = Binder.clearCallingIdentity();
            try {
                VirtualDeviceManagerInternal virtualDeviceManagerInternal = (VirtualDeviceManagerInternal) FingerprintService.this.getLocalService(VirtualDeviceManagerInternal.class);
                if (virtualDeviceManagerInternal != null) {
                    virtualDeviceManagerInternal.onAuthenticationPrompt(callingUid);
                }
                Binder.restoreCallingIdentity(clearCallingIdentity);
                return ((ServiceProvider) pair.second).scheduleAuthenticate(iBinder, j, 0, new ClientMonitorCallbackConverter(iFingerprintServiceReceiver), new FingerprintAuthenticateOptions.Builder().setUserId(changeUserIdIfNeeded).setSensorId(fingerprintAuthenticateOptions.getSensorId()).setIgnoreEnrollmentState(fingerprintAuthenticateOptions.isIgnoreEnrollmentState()).setOpPackageName(fingerprintAuthenticateOptions.getOpPackageName()).setAttributionTag(fingerprintAuthenticateOptions.getAttributionTag()).setDisplayState(fingerprintAuthenticateOptions.getDisplayState()).build(), z, i, isKeyguard);
            } finally {
            }
        }

        private long authenticateWithPrompt(long j, final FingerprintSensorPropertiesInternal fingerprintSensorPropertiesInternal, int i, final int i2, final IFingerprintServiceReceiver iFingerprintServiceReceiver, String str, boolean z) throws PackageManager.NameNotFoundException {
            Context uiContext = FingerprintService.this.getUiContext();
            Context createPackageContextAsUser = uiContext.createPackageContextAsUser(str, 0, UserHandle.getUserHandleForUid(i));
            Executor mainExecutor = uiContext.getMainExecutor();
            return new BiometricPrompt.Builder(createPackageContextAsUser).setTitle(uiContext.getString(R.string.capability_desc_canRequestFilterKeyEvents)).setSubtitle(uiContext.getString(R.string.keyguard_password_wrong_pin_code)).setNegativeButton(uiContext.getString(R.string.cancel), mainExecutor, new DialogInterface.OnClickListener() { // from class: com.android.server.biometrics.sensors.fingerprint.FingerprintService$1$$ExternalSyntheticLambda1
                @Override // android.content.DialogInterface.OnClickListener
                public final void onClick(DialogInterface dialogInterface, int i3) {
                    FingerprintService.AnonymousClass1.lambda$authenticateWithPrompt$0(iFingerprintServiceReceiver, dialogInterface, i3);
                }
            }).setIsForLegacyFingerprintManager(fingerprintSensorPropertiesInternal.sensorId).setIgnoreEnrollmentState(z).build().authenticateForOperation(new CancellationSignal(), mainExecutor, new BiometricPrompt.AuthenticationCallback() { // from class: com.android.server.biometrics.sensors.fingerprint.FingerprintService.1.1
                @Override // android.hardware.biometrics.BiometricPrompt.AuthenticationCallback
                public void onAuthenticationError(int i3, CharSequence charSequence) {
                    try {
                        if (FingerprintUtils.isKnownErrorCode(i3)) {
                            iFingerprintServiceReceiver.onError(i3, 0);
                        } else {
                            iFingerprintServiceReceiver.onError(8, i3);
                        }
                    } catch (RemoteException e) {
                        Slog.e(FingerprintService.TAG, "Remote exception in onAuthenticationError()", e);
                    }
                }

                @Override // android.hardware.biometrics.BiometricPrompt.AuthenticationCallback
                public void onAuthenticationSucceeded(BiometricPrompt.AuthenticationResult authenticationResult) {
                    try {
                        iFingerprintServiceReceiver.onAuthenticationSucceeded(new Fingerprint("", 0, 0L), i2, fingerprintSensorPropertiesInternal.sensorStrength == 2);
                    } catch (RemoteException e) {
                        Slog.e(FingerprintService.TAG, "Remote exception in onAuthenticationSucceeded()", e);
                    }
                }

                @Override // android.hardware.biometrics.BiometricPrompt.AuthenticationCallback
                public void onAuthenticationFailed() {
                    try {
                        iFingerprintServiceReceiver.onAuthenticationFailed();
                    } catch (RemoteException e) {
                        Slog.e(FingerprintService.TAG, "Remote exception in onAuthenticationFailed()", e);
                    }
                }

                public void onAuthenticationAcquired(int i3) {
                    try {
                        if (FingerprintUtils.isKnownAcquiredCode(i3)) {
                            iFingerprintServiceReceiver.onAcquired(i3, 0);
                        } else {
                            iFingerprintServiceReceiver.onAcquired(6, i3);
                        }
                    } catch (RemoteException e) {
                        Slog.e(FingerprintService.TAG, "Remote exception in onAuthenticationAcquired()", e);
                    }
                }

                @Override // android.hardware.biometrics.BiometricPrompt.AuthenticationCallback
                public void onAuthenticationHelp(int i3, CharSequence charSequence) {
                    onAuthenticationAcquired(i3);
                }
            }, j);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static /* synthetic */ void lambda$authenticateWithPrompt$0(IFingerprintServiceReceiver iFingerprintServiceReceiver, DialogInterface dialogInterface, int i) {
            try {
                iFingerprintServiceReceiver.onError(10, 0);
            } catch (RemoteException e) {
                Slog.e(FingerprintService.TAG, "Remote exception in negative button onClick()", e);
            }
        }

        @EnforcePermission("android.permission.USE_BIOMETRIC_INTERNAL")
        public long detectFingerprint(IBinder iBinder, IFingerprintServiceReceiver iFingerprintServiceReceiver, FingerprintAuthenticateOptions fingerprintAuthenticateOptions) {
            super.detectFingerprint_enforcePermission();
            String opPackageName = fingerprintAuthenticateOptions.getOpPackageName();
            if (!Utils.isKeyguard(FingerprintService.this.getContext(), opPackageName)) {
                Slog.w(FingerprintService.TAG, "detectFingerprint called from non-sysui package: " + opPackageName);
                return -1L;
            }
            Pair<Integer, ServiceProvider> singleProvider = FingerprintService.this.mRegistry.getSingleProvider();
            if (singleProvider == null) {
                Slog.w(FingerprintService.TAG, "Null provider for detectFingerprint");
                return -1L;
            }
            fingerprintAuthenticateOptions.setSensorId(((Integer) singleProvider.first).intValue());
            return ((ServiceProvider) singleProvider.second).scheduleFingerDetect(iBinder, new ClientMonitorCallbackConverter(iFingerprintServiceReceiver), fingerprintAuthenticateOptions, 1);
        }

        @EnforcePermission("android.permission.MANAGE_BIOMETRIC")
        public void prepareForAuthentication(IBinder iBinder, long j, IBiometricSensorReceiver iBiometricSensorReceiver, FingerprintAuthenticateOptions fingerprintAuthenticateOptions, long j2, int i, boolean z) {
            super.prepareForAuthentication_enforcePermission();
            ServiceProvider providerForSensor = FingerprintService.this.mRegistry.getProviderForSensor(fingerprintAuthenticateOptions.getSensorId());
            if (providerForSensor == null) {
                Slog.w(FingerprintService.TAG, "Null provider for prepareForAuthentication");
            } else {
                FingerprintService.this.getWrapper().getExtImpl().prepareForAuthPreOperation(iBinder, fingerprintAuthenticateOptions.getOpPackageName());
                providerForSensor.scheduleAuthenticate(iBinder, j, i, new ClientMonitorCallbackConverter(iBiometricSensorReceiver), fingerprintAuthenticateOptions, j2, true, 2, z);
            }
        }

        @EnforcePermission("android.permission.MANAGE_BIOMETRIC")
        public void startPreparedClient(int i, int i2) {
            super.startPreparedClient_enforcePermission();
            ServiceProvider providerForSensor = FingerprintService.this.mRegistry.getProviderForSensor(i);
            if (providerForSensor == null) {
                Slog.w(FingerprintService.TAG, "Null provider for startPreparedClient");
            } else {
                providerForSensor.startPreparedClient(i, i2);
            }
        }

        public void cancelAuthentication(IBinder iBinder, String str, String str2, long j) {
            if (!FingerprintService.this.canUseFingerprint(str, str2, true, Binder.getCallingUid(), Binder.getCallingPid(), UserHandle.getCallingUserId()) && !FingerprintService.this.getWrapper().getExtImpl().skipAuthWithPrompt(str)) {
                Slog.w(FingerprintService.TAG, "cancelAuthentication rejecting package: " + str);
                return;
            }
            Pair<Integer, ServiceProvider> singleProvider = FingerprintService.this.mRegistry.getSingleProvider();
            if (singleProvider == null) {
                Slog.w(FingerprintService.TAG, "Null provider for cancelAuthentication");
            } else {
                ((ServiceProvider) singleProvider.second).cancelAuthentication(((Integer) singleProvider.first).intValue(), iBinder, j);
            }
        }

        @EnforcePermission("android.permission.USE_BIOMETRIC_INTERNAL")
        public void cancelFingerprintDetect(IBinder iBinder, String str, long j) {
            super.cancelFingerprintDetect_enforcePermission();
            if (!Utils.isKeyguard(FingerprintService.this.getContext(), str)) {
                Slog.w(FingerprintService.TAG, "cancelFingerprintDetect called from non-sysui package: " + str);
                return;
            }
            Pair<Integer, ServiceProvider> singleProvider = FingerprintService.this.mRegistry.getSingleProvider();
            if (singleProvider == null) {
                Slog.w(FingerprintService.TAG, "Null provider for cancelFingerprintDetect");
            } else {
                ((ServiceProvider) singleProvider.second).cancelAuthentication(((Integer) singleProvider.first).intValue(), iBinder, j);
            }
        }

        @EnforcePermission("android.permission.MANAGE_BIOMETRIC")
        public void cancelAuthenticationFromService(int i, IBinder iBinder, String str, long j) {
            super.cancelAuthenticationFromService_enforcePermission();
            Slog.d(FingerprintService.TAG, "cancelAuthenticationFromService, sensorId: " + i);
            ServiceProvider providerForSensor = FingerprintService.this.mRegistry.getProviderForSensor(i);
            if (providerForSensor == null) {
                Slog.w(FingerprintService.TAG, "Null provider for cancelAuthenticationFromService");
            } else {
                providerForSensor.cancelAuthentication(i, iBinder, j);
            }
        }

        @EnforcePermission("android.permission.MANAGE_FINGERPRINT")
        public void remove(IBinder iBinder, int i, int i2, IFingerprintServiceReceiver iFingerprintServiceReceiver, String str) {
            super.remove_enforcePermission();
            Pair<Integer, ServiceProvider> singleProvider = FingerprintService.this.mRegistry.getSingleProvider();
            if (singleProvider == null) {
                Slog.w(FingerprintService.TAG, "Null provider for remove");
            } else {
                ((ServiceProvider) singleProvider.second).scheduleRemove(((Integer) singleProvider.first).intValue(), iBinder, iFingerprintServiceReceiver, i, i2, str);
            }
        }

        @EnforcePermission("android.permission.USE_BIOMETRIC_INTERNAL")
        public void removeAll(IBinder iBinder, int i, final IFingerprintServiceReceiver iFingerprintServiceReceiver, String str) {
            super.removeAll_enforcePermission();
            IFingerprintServiceReceiver iFingerprintServiceReceiver2 = new FingerprintServiceReceiver() { // from class: com.android.server.biometrics.sensors.fingerprint.FingerprintService.1.2
                final int numSensors;
                int sensorsFinishedRemoving = 0;

                {
                    this.numSensors = AnonymousClass1.this.getSensorPropertiesInternal(FingerprintService.this.getContext().getOpPackageName()).size();
                }

                public void onRemoved(Fingerprint fingerprint, int i2) throws RemoteException {
                    if (i2 == 0) {
                        this.sensorsFinishedRemoving++;
                        Slog.d(FingerprintService.TAG, "sensorsFinishedRemoving: " + this.sensorsFinishedRemoving + ", numSensors: " + this.numSensors);
                        if (this.sensorsFinishedRemoving == this.numSensors) {
                            iFingerprintServiceReceiver.onRemoved((Fingerprint) null, 0);
                        }
                    }
                }
            };
            for (ServiceProvider serviceProvider : FingerprintService.this.mRegistry.getProviders()) {
                Iterator<FingerprintSensorPropertiesInternal> it = serviceProvider.getSensorProperties().iterator();
                while (it.hasNext()) {
                    serviceProvider.scheduleRemoveAll(it.next().sensorId, iBinder, iFingerprintServiceReceiver2, i, str);
                }
            }
        }

        @EnforcePermission("android.permission.USE_BIOMETRIC_INTERNAL")
        public void addLockoutResetCallback(IBiometricServiceLockoutResetCallback iBiometricServiceLockoutResetCallback, String str) {
            super.addLockoutResetCallback_enforcePermission();
            FingerprintService.this.mLockoutResetDispatcher.addCallback(iBiometricServiceLockoutResetCallback, str);
        }

        /* JADX WARN: Multi-variable type inference failed */
        public void onShellCommand(FileDescriptor fileDescriptor, FileDescriptor fileDescriptor2, FileDescriptor fileDescriptor3, String[] strArr, ShellCallback shellCallback, ResultReceiver resultReceiver) throws RemoteException {
            new FingerprintShellCommand(FingerprintService.this.getContext(), FingerprintService.this).exec(this, fileDescriptor, fileDescriptor2, fileDescriptor3, strArr, shellCallback, resultReceiver);
        }

        protected void dump(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
            if (DumpUtils.checkDumpPermission(FingerprintService.this.getContext(), FingerprintService.TAG, printWriter)) {
                long clearCallingIdentity = Binder.clearCallingIdentity();
                try {
                    if (strArr.length > 1 && "--proto".equals(strArr[0]) && "--state".equals(strArr[1])) {
                        ProtoOutputStream protoOutputStream = new ProtoOutputStream(fileDescriptor);
                        for (ServiceProvider serviceProvider : FingerprintService.this.mRegistry.getProviders()) {
                            Iterator<FingerprintSensorPropertiesInternal> it = serviceProvider.getSensorProperties().iterator();
                            while (it.hasNext()) {
                                serviceProvider.dumpProtoState(it.next().sensorId, protoOutputStream, false);
                            }
                        }
                        protoOutputStream.flush();
                    } else if (strArr.length > 0 && "--proto".equals(strArr[0])) {
                        for (ServiceProvider serviceProvider2 : FingerprintService.this.mRegistry.getProviders()) {
                            Iterator<FingerprintSensorPropertiesInternal> it2 = serviceProvider2.getSensorProperties().iterator();
                            while (it2.hasNext()) {
                                serviceProvider2.dumpProtoMetrics(it2.next().sensorId, fileDescriptor);
                            }
                        }
                    } else {
                        for (ServiceProvider serviceProvider3 : FingerprintService.this.mRegistry.getProviders()) {
                            for (FingerprintSensorPropertiesInternal fingerprintSensorPropertiesInternal : serviceProvider3.getSensorProperties()) {
                                printWriter.println("Dumping for sensorId: " + fingerprintSensorPropertiesInternal.sensorId + ", provider: " + serviceProvider3.getClass().getSimpleName());
                                StringBuilder sb = new StringBuilder();
                                sb.append("Fps state: ");
                                sb.append(FingerprintService.this.mBiometricStateCallback.getBiometricState());
                                printWriter.println(sb.toString());
                                serviceProvider3.dumpInternal(fingerprintSensorPropertiesInternal.sensorId, printWriter);
                                FingerprintService.this.getWrapper().getExtImpl().dumpInternal(serviceProvider3, fileDescriptor, printWriter, strArr);
                                printWriter.println();
                            }
                        }
                    }
                } finally {
                    Binder.restoreCallingIdentity(clearCallingIdentity);
                }
            }
        }

        public boolean isHardwareDetectedDeprecated(String str, String str2) {
            if (!FingerprintService.this.canUseFingerprint(str, str2, false, Binder.getCallingUid(), Binder.getCallingPid(), UserHandle.getCallingUserId())) {
                return false;
            }
            long clearCallingIdentity = Binder.clearCallingIdentity();
            try {
                Pair<Integer, ServiceProvider> singleProvider = FingerprintService.this.mRegistry.getSingleProvider();
                if (singleProvider == null) {
                    Slog.w(FingerprintService.TAG, "Null provider for isHardwareDetectedDeprecated, caller: " + str);
                    return false;
                }
                return ((ServiceProvider) singleProvider.second).isHardwareDetected(((Integer) singleProvider.first).intValue());
            } finally {
                Binder.restoreCallingIdentity(clearCallingIdentity);
            }
        }

        @EnforcePermission("android.permission.USE_BIOMETRIC_INTERNAL")
        public boolean isHardwareDetected(int i, String str) {
            super.isHardwareDetected_enforcePermission();
            ServiceProvider providerForSensor = FingerprintService.this.mRegistry.getProviderForSensor(i);
            if (providerForSensor == null) {
                Slog.w(FingerprintService.TAG, "Null provider for isHardwareDetected, caller: " + str);
                return false;
            }
            return providerForSensor.isHardwareDetected(i);
        }

        @EnforcePermission("android.permission.MANAGE_FINGERPRINT")
        public void rename(int i, int i2, String str) {
            super.rename_enforcePermission();
            if (Utils.isCurrentUserOrProfile(FingerprintService.this.getContext(), i2)) {
                Pair<Integer, ServiceProvider> singleProvider = FingerprintService.this.mRegistry.getSingleProvider();
                if (singleProvider == null) {
                    Slog.w(FingerprintService.TAG, "Null provider for rename");
                } else {
                    ((ServiceProvider) singleProvider.second).rename(((Integer) singleProvider.first).intValue(), i, i2, str);
                }
            }
        }

        public List<Fingerprint> getEnrolledFingerprints(int i, String str, String str2) {
            if (!FingerprintService.this.canUseFingerprint(str, str2, false, Binder.getCallingUid(), Binder.getCallingPid(), UserHandle.getCallingUserId())) {
                return Collections.emptyList();
            }
            if (i != UserHandle.getCallingUserId()) {
                Utils.checkPermission(FingerprintService.this.getContext(), "android.permission.INTERACT_ACROSS_USERS");
            }
            return FingerprintService.this.getEnrolledFingerprintsDeprecated(i, str);
        }

        public boolean hasEnrolledFingerprintsDeprecated(int i, String str, String str2) {
            if (!FingerprintService.this.canUseFingerprint(str, str2, false, Binder.getCallingUid(), Binder.getCallingPid(), UserHandle.getCallingUserId())) {
                return false;
            }
            if (i != UserHandle.getCallingUserId()) {
                Utils.checkPermission(FingerprintService.this.getContext(), "android.permission.INTERACT_ACROSS_USERS");
            }
            return !FingerprintService.this.getEnrolledFingerprintsDeprecated(i, str).isEmpty();
        }

        @EnforcePermission("android.permission.USE_BIOMETRIC_INTERNAL")
        public boolean hasEnrolledFingerprints(int i, int i2, String str) {
            super.hasEnrolledFingerprints_enforcePermission();
            ServiceProvider providerForSensor = FingerprintService.this.mRegistry.getProviderForSensor(i);
            if (providerForSensor != null) {
                return providerForSensor.getEnrolledFingerprints(i, FingerprintService.this.getWrapper().getExtImpl().changeUserIdIfNeeded(i2)).size() > 0;
            }
            Slog.w(FingerprintService.TAG, "Null provider for hasEnrolledFingerprints, caller: " + str);
            return false;
        }

        @EnforcePermission("android.permission.USE_BIOMETRIC_INTERNAL")
        public int getLockoutModeForUser(int i, int i2) {
            super.getLockoutModeForUser_enforcePermission();
            ServiceProvider providerForSensor = FingerprintService.this.mRegistry.getProviderForSensor(i);
            if (providerForSensor == null) {
                Slog.w(FingerprintService.TAG, "Null provider for getLockoutModeForUser");
                return 0;
            }
            return providerForSensor.getLockoutModeForUser(i, i2);
        }

        @EnforcePermission("android.permission.USE_BIOMETRIC_INTERNAL")
        public void invalidateAuthenticatorId(int i, int i2, IInvalidationCallback iInvalidationCallback) {
            super.invalidateAuthenticatorId_enforcePermission();
            ServiceProvider providerForSensor = FingerprintService.this.mRegistry.getProviderForSensor(i);
            if (providerForSensor == null) {
                Slog.w(FingerprintService.TAG, "Null provider for invalidateAuthenticatorId");
            } else {
                providerForSensor.scheduleInvalidateAuthenticatorId(i, i2, iInvalidationCallback);
            }
        }

        @EnforcePermission("android.permission.USE_BIOMETRIC_INTERNAL")
        public long getAuthenticatorId(int i, int i2) {
            super.getAuthenticatorId_enforcePermission();
            ServiceProvider providerForSensor = FingerprintService.this.mRegistry.getProviderForSensor(i);
            if (providerForSensor == null) {
                Slog.w(FingerprintService.TAG, "Null provider for getAuthenticatorId");
                return 0L;
            }
            return providerForSensor.getAuthenticatorId(i, i2);
        }

        @EnforcePermission("android.permission.RESET_FINGERPRINT_LOCKOUT")
        public void resetLockout(IBinder iBinder, int i, int i2, byte[] bArr, String str) {
            super.resetLockout_enforcePermission();
            ServiceProvider providerForSensor = FingerprintService.this.mRegistry.getProviderForSensor(i);
            if (providerForSensor == null) {
                Slog.w(FingerprintService.TAG, "Null provider for resetLockout, caller: " + str);
                return;
            }
            providerForSensor.scheduleResetLockout(i, i2, bArr);
        }

        @EnforcePermission("android.permission.MANAGE_FINGERPRINT")
        public boolean isClientActive() {
            super.isClientActive_enforcePermission();
            return FingerprintService.this.mGestureAvailabilityDispatcher.isAnySensorActive();
        }

        @EnforcePermission("android.permission.MANAGE_FINGERPRINT")
        public void addClientActiveCallback(IFingerprintClientActiveCallback iFingerprintClientActiveCallback) {
            super.addClientActiveCallback_enforcePermission();
            FingerprintService.this.mGestureAvailabilityDispatcher.registerCallback(iFingerprintClientActiveCallback);
        }

        @EnforcePermission("android.permission.MANAGE_FINGERPRINT")
        public void removeClientActiveCallback(IFingerprintClientActiveCallback iFingerprintClientActiveCallback) {
            super.removeClientActiveCallback_enforcePermission();
            FingerprintService.this.mGestureAvailabilityDispatcher.removeCallback(iFingerprintClientActiveCallback);
        }

        @EnforcePermission("android.permission.USE_BIOMETRIC_INTERNAL")
        public void registerAuthenticators(final List<FingerprintSensorPropertiesInternal> list) {
            super.registerAuthenticators_enforcePermission();
            FingerprintService.this.mRegistry.registerAll(new Supplier() { // from class: com.android.server.biometrics.sensors.fingerprint.FingerprintService$1$$ExternalSyntheticLambda0
                @Override // java.util.function.Supplier
                public final Object get() {
                    List lambda$registerAuthenticators$1;
                    lambda$registerAuthenticators$1 = FingerprintService.AnonymousClass1.this.lambda$registerAuthenticators$1(list);
                    return lambda$registerAuthenticators$1;
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ List lambda$registerAuthenticators$1(List list) {
            ArrayList arrayList = new ArrayList();
            arrayList.addAll(FingerprintService.this.getHidlProviders(list));
            ArrayList arrayList2 = new ArrayList();
            String[] strArr = (String[]) FingerprintService.this.mAidlInstanceNameSupplier.get();
            if (strArr != null) {
                arrayList2.addAll(Lists.newArrayList(strArr));
            }
            FingerprintService fingerprintService = FingerprintService.this;
            arrayList.addAll(fingerprintService.getAidlProviders(Utils.filterAvailableHalInstances(fingerprintService.getContext(), arrayList2)));
            FingerprintService.this.getWrapper().getExtImpl().onSystemReady();
            return arrayList;
        }

        @EnforcePermission("android.permission.USE_BIOMETRIC_INTERNAL")
        public void addAuthenticatorsRegisteredCallback(IFingerprintAuthenticatorsRegisteredCallback iFingerprintAuthenticatorsRegisteredCallback) {
            super.addAuthenticatorsRegisteredCallback_enforcePermission();
            FingerprintService.this.mRegistry.addAllRegisteredCallback(iFingerprintAuthenticatorsRegisteredCallback);
        }

        @EnforcePermission("android.permission.USE_BIOMETRIC_INTERNAL")
        public void registerBiometricStateListener(IBiometricStateListener iBiometricStateListener) {
            super.registerBiometricStateListener_enforcePermission();
            FingerprintService.this.mBiometricStateCallback.registerBiometricStateListener(iBiometricStateListener);
        }

        @EnforcePermission("android.permission.USE_BIOMETRIC_INTERNAL")
        public void onPointerDown(long j, int i, PointerContext pointerContext) {
            super.onPointerDown_enforcePermission();
            ServiceProvider providerForSensor = FingerprintService.this.mRegistry.getProviderForSensor(i);
            if (providerForSensor == null) {
                Slog.w(FingerprintService.TAG, "No matching provider for onFingerDown, sensorId: " + i);
                return;
            }
            providerForSensor.onPointerDown(j, i, pointerContext);
        }

        @EnforcePermission("android.permission.USE_BIOMETRIC_INTERNAL")
        public void onPointerUp(long j, int i, PointerContext pointerContext) {
            super.onPointerUp_enforcePermission();
            ServiceProvider providerForSensor = FingerprintService.this.mRegistry.getProviderForSensor(i);
            if (providerForSensor == null) {
                Slog.w(FingerprintService.TAG, "No matching provider for onFingerUp, sensorId: " + i);
                return;
            }
            providerForSensor.onPointerUp(j, i, pointerContext);
        }

        @EnforcePermission("android.permission.USE_BIOMETRIC_INTERNAL")
        public void onUiReady(long j, int i) {
            super.onUiReady_enforcePermission();
            ServiceProvider providerForSensor = FingerprintService.this.mRegistry.getProviderForSensor(i);
            if (providerForSensor == null) {
                Slog.w(FingerprintService.TAG, "No matching provider for onUiReady, sensorId: " + i);
                return;
            }
            providerForSensor.onUiReady(j, i);
        }

        @EnforcePermission("android.permission.USE_BIOMETRIC_INTERNAL")
        public void setUdfpsOverlayController(IUdfpsOverlayController iUdfpsOverlayController) {
            super.setUdfpsOverlayController_enforcePermission();
            Iterator<ServiceProvider> it = FingerprintService.this.mRegistry.getProviders().iterator();
            while (it.hasNext()) {
                it.next().setUdfpsOverlayController(iUdfpsOverlayController);
            }
        }

        @EnforcePermission("android.permission.USE_BIOMETRIC_INTERNAL")
        public void setSidefpsController(ISidefpsController iSidefpsController) {
            super.setSidefpsController_enforcePermission();
            Iterator<ServiceProvider> it = FingerprintService.this.mRegistry.getProviders().iterator();
            while (it.hasNext()) {
                it.next().setSidefpsController(iSidefpsController);
            }
        }

        @EnforcePermission("android.permission.USE_BIOMETRIC_INTERNAL")
        public void setUdfpsOverlay(IUdfpsOverlay iUdfpsOverlay) {
            super.setUdfpsOverlay_enforcePermission();
            Iterator<ServiceProvider> it = FingerprintService.this.mRegistry.getProviders().iterator();
            while (it.hasNext()) {
                it.next().setUdfpsOverlay(iUdfpsOverlay);
            }
        }

        @EnforcePermission("android.permission.USE_BIOMETRIC_INTERNAL")
        public void onPowerPressed() {
            super.onPowerPressed_enforcePermission();
            Iterator<ServiceProvider> it = FingerprintService.this.mRegistry.getProviders().iterator();
            while (it.hasNext()) {
                it.next().onPowerPressed();
            }
        }

        @EnforcePermission("android.permission.USE_BIOMETRIC_INTERNAL")
        public void scheduleWatchdog() {
            super.scheduleWatchdog_enforcePermission();
            Pair<Integer, ServiceProvider> singleProvider = FingerprintService.this.mRegistry.getSingleProvider();
            if (singleProvider == null) {
                Slog.w(FingerprintService.TAG, "Null provider for scheduling watchdog");
            } else {
                ((ServiceProvider) singleProvider.second).scheduleWatchdog(((Integer) singleProvider.first).intValue());
            }
        }
    }

    public FingerprintService(Context context) {
        this(context, BiometricContext.getInstance(context), new Supplier() { // from class: com.android.server.biometrics.sensors.fingerprint.FingerprintService$$ExternalSyntheticLambda0
            @Override // java.util.function.Supplier
            public final Object get() {
                IBiometricService lambda$new$0;
                lambda$new$0 = FingerprintService.lambda$new$0();
                return lambda$new$0;
            }
        }, new Supplier() { // from class: com.android.server.biometrics.sensors.fingerprint.FingerprintService$$ExternalSyntheticLambda1
            @Override // java.util.function.Supplier
            public final Object get() {
                String[] lambda$new$1;
                lambda$new$1 = FingerprintService.lambda$new$1();
                return lambda$new$1;
            }
        }, null);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ IBiometricService lambda$new$0() {
        return IBiometricService.Stub.asInterface(ServiceManager.getService("biometric"));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ String[] lambda$new$1() {
        return ServiceManager.getDeclaredInstances(IFingerprint.DESCRIPTOR);
    }

    @VisibleForTesting
    FingerprintService(Context context, BiometricContext biometricContext, Supplier<IBiometricService> supplier, Supplier<String[]> supplier2, Function<String, FingerprintProvider> function) {
        super(context);
        IFingerprintService.Stub anonymousClass1 = new AnonymousClass1();
        this.mServiceWrapper = anonymousClass1;
        this.mOplusFingerprintServiceWrapper = new OplusFingerprintServiceWrapper();
        this.mIOplusFingerprintServiceExt = (IFingerprintServiceExt) ExtLoader.type(IFingerprintServiceExt.class).base(this).create();
        this.mBiometricContext = biometricContext;
        this.mAidlInstanceNameSupplier = supplier2;
        this.mAppOps = (AppOpsManager) context.getSystemService(AppOpsManager.class);
        this.mGestureAvailabilityDispatcher = new GestureAvailabilityDispatcher();
        this.mLockoutResetDispatcher = new LockoutResetDispatcher(context);
        this.mLockPatternUtils = new LockPatternUtils(context);
        this.mBiometricStateCallback = new BiometricStateCallback<>(UserManager.get(context));
        this.mFingerprintProvider = function == null ? new Function() { // from class: com.android.server.biometrics.sensors.fingerprint.FingerprintService$$ExternalSyntheticLambda2
            @Override // java.util.function.Function
            public final Object apply(Object obj) {
                FingerprintProvider lambda$new$2;
                lambda$new$2 = FingerprintService.this.lambda$new$2((String) obj);
                return lambda$new$2;
            }
        } : function;
        this.mHandler = getWrapper().getExtImpl().createHandlerWithNewLooper();
        getWrapper().getExtImpl().setBinderExtension(anonymousClass1);
        FingerprintServiceRegistry fingerprintServiceRegistry = new FingerprintServiceRegistry(anonymousClass1, supplier);
        this.mRegistry = fingerprintServiceRegistry;
        fingerprintServiceRegistry.addAllRegisteredCallback(new IFingerprintAuthenticatorsRegisteredCallback.Stub() { // from class: com.android.server.biometrics.sensors.fingerprint.FingerprintService.2
            public void onAllAuthenticatorsRegistered(List<FingerprintSensorPropertiesInternal> list) {
                FingerprintService.this.mBiometricStateCallback.start(FingerprintService.this.mRegistry.getProviders());
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ FingerprintProvider lambda$new$2(String str) {
        String str2 = IFingerprint.DESCRIPTOR + "/" + str;
        IFingerprint asInterface = IFingerprint.Stub.asInterface(Binder.allowBlocking(ServiceManager.waitForDeclaredService(str2)));
        if (asInterface != null) {
            try {
                return new FingerprintProvider(getContext(), this.mBiometricStateCallback, asInterface.getSensorProps(), str, this.mLockoutResetDispatcher, this.mGestureAvailabilityDispatcher, this.mBiometricContext);
            } catch (RemoteException unused) {
                Slog.e(TAG, "Remote exception in getSensorProps: " + str2);
                return null;
            }
        }
        Slog.e(TAG, "Unable to get declared service: " + str2);
        return null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public List<ServiceProvider> getHidlProviders(List<FingerprintSensorPropertiesInternal> list) {
        Fingerprint21 newInstance;
        ArrayList arrayList = new ArrayList();
        for (FingerprintSensorPropertiesInternal fingerprintSensorPropertiesInternal : list) {
            if ((Build.IS_USERDEBUG || Build.IS_ENG) && getContext().getResources().getBoolean(R.bool.config_allowDisablingAssistDisclosure) && Settings.Secure.getIntForUser(getContext().getContentResolver(), Fingerprint21UdfpsMock.CONFIG_ENABLE_TEST_UDFPS, 0, -2) != 0) {
                newInstance = Fingerprint21UdfpsMock.newInstance(getContext(), this.mBiometricStateCallback, fingerprintSensorPropertiesInternal, this.mLockoutResetDispatcher, this.mGestureAvailabilityDispatcher, BiometricContext.getInstance(getContext()));
            } else {
                newInstance = Fingerprint21.newInstance(getContext(), this.mBiometricStateCallback, fingerprintSensorPropertiesInternal, this.mHandler, this.mLockoutResetDispatcher, this.mGestureAvailabilityDispatcher);
            }
            arrayList.add(newInstance);
        }
        return arrayList;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public List<ServiceProvider> getAidlProviders(List<String> list) {
        ArrayList arrayList = new ArrayList();
        for (String str : list) {
            FingerprintProvider apply = this.mFingerprintProvider.apply(str);
            Slog.i(TAG, "Adding AIDL provider: " + str);
            arrayList.add(apply);
        }
        return arrayList;
    }

    @Override // com.android.server.SystemService
    public void onStart() {
        publishBinderService("fingerprint", this.mServiceWrapper);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public List<Fingerprint> getEnrolledFingerprintsDeprecated(int i, String str) {
        Pair<Integer, ServiceProvider> singleProvider = this.mRegistry.getSingleProvider();
        if (singleProvider == null) {
            Slog.w(TAG, "Null provider for getEnrolledFingerprintsDeprecated, caller: " + str);
            return Collections.emptyList();
        }
        return ((ServiceProvider) singleProvider.second).getEnrolledFingerprints(((Integer) singleProvider.first).intValue(), getWrapper().getExtImpl().changeUserIdIfNeeded(i));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean canUseFingerprint(String str, String str2, boolean z, int i, int i2, int i3) {
        if (getWrapper().getExtImpl().isBiometricDisabled()) {
            return false;
        }
        if (getContext().checkCallingPermission("android.permission.USE_FINGERPRINT") != 0) {
            Utils.checkPermission(getContext(), "android.permission.USE_BIOMETRIC");
        }
        if (Binder.getCallingUid() == 1000 || Utils.isKeyguard(getContext(), str)) {
            return true;
        }
        if (!Utils.isCurrentUserOrProfile(getContext(), i3)) {
            Slog.w(TAG, "Rejecting " + str + "; not a current user or profile");
            return false;
        }
        if (!checkAppOps(i, str, str2)) {
            Slog.w(TAG, "Rejecting " + str + "; permission denied");
            return false;
        }
        if (!z || Utils.isForeground(i, i2)) {
            return true;
        }
        Slog.w(TAG, "Rejecting " + str + "; not in foreground");
        return false;
    }

    private boolean checkAppOps(int i, String str, String str2) {
        return this.mAppOps.noteOp(78, i, str, str2, (String) null) == 0 || this.mAppOps.noteOp(55, i, str, str2, (String) null) == 0;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void syncEnrollmentsNow() {
        Utils.checkPermissionOrShell(getContext(), "android.permission.MANAGE_FINGERPRINT");
        if (Utils.isVirtualEnabled(getContext())) {
            Slog.i(TAG, "Sync virtual enrollments");
            int currentUser = ActivityManager.getCurrentUser();
            final CountDownLatch countDownLatch = new CountDownLatch(this.mRegistry.getProviders().size());
            for (ServiceProvider serviceProvider : this.mRegistry.getProviders()) {
                Iterator<FingerprintSensorPropertiesInternal> it = serviceProvider.getSensorProperties().iterator();
                while (it.hasNext()) {
                    serviceProvider.scheduleInternalCleanup(it.next().sensorId, currentUser, new ClientMonitorCallback() { // from class: com.android.server.biometrics.sensors.fingerprint.FingerprintService.3
                        @Override // com.android.server.biometrics.sensors.ClientMonitorCallback
                        public void onClientFinished(BaseClientMonitor baseClientMonitor, boolean z) {
                            countDownLatch.countDown();
                            if (z) {
                                return;
                            }
                            Slog.e(FingerprintService.TAG, "Sync virtual enrollments failed");
                        }
                    }, true);
                }
            }
            try {
                countDownLatch.await(3L, TimeUnit.SECONDS);
            } catch (Exception e) {
                Slog.e(TAG, "Failed to wait for sync finishing", e);
            }
        }
    }

    public IFingerprintServiceWrapper getWrapper() {
        return this.mOplusFingerprintServiceWrapper;
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    private class OplusFingerprintServiceWrapper implements IFingerprintServiceWrapper {
        private OplusFingerprintServiceWrapper() {
        }

        @Override // com.android.server.biometrics.sensors.fingerprint.IFingerprintServiceWrapper
        public IFingerprintServiceExt getExtImpl() {
            return FingerprintService.this.mIOplusFingerprintServiceExt;
        }

        @Override // com.android.server.biometrics.sensors.fingerprint.IFingerprintServiceWrapper
        public ServiceProvider getProviderForSensorWrapper(int i) {
            return FingerprintService.this.mRegistry.getProviderForSensor(i);
        }

        @Override // com.android.server.biometrics.sensors.fingerprint.IFingerprintServiceWrapper
        public Pair<Integer, ServiceProvider> getSingleProviderWrapper() {
            return FingerprintService.this.mRegistry.getSingleProvider();
        }

        @Override // com.android.server.biometrics.sensors.fingerprint.IFingerprintServiceWrapper
        public boolean canUseFingerprintWrapper(String str, String str2, boolean z, int i, int i2, int i3) {
            return FingerprintService.this.canUseFingerprint(str, str2, z, i, i2, i3);
        }
    }
}
