package com.android.server.soundtrigger_middleware;

import android.hardware.soundtrigger3.ISoundTriggerHw;
import android.os.HwBinder;
import android.os.NativeHandle;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.os.SystemProperties;
import android.util.Log;
import java.util.ArrayList;
import java.util.Arrays;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
class DefaultHalFactory implements HalFactory {
    private static final String TAG = "SoundTriggerMiddlewareDefaultHalFactory";
    private static final int USE_DEFAULT_HAL = 0;
    private static final int USE_MOCK_HAL_V2 = 2;
    private static final int USE_MOCK_HAL_V3 = 3;
    private static final ICaptureStateNotifier mCaptureStateNotifier = new ExternalCaptureStateTracker();

    @Override // com.android.server.soundtrigger_middleware.HalFactory
    public ISoundTriggerHal create() {
        try {
            int i = SystemProperties.getInt("debug.soundtrigger_middleware.use_mock_hal", 0);
            if (i == 0) {
                String str = ISoundTriggerHw.class.getCanonicalName() + "/default";
                if (ServiceManager.isDeclared(str)) {
                    Log.i(TAG, "Connecting to default soundtrigger3.ISoundTriggerHw");
                    return new SoundTriggerHw3Compat(ServiceManager.waitForService(str), new Runnable() { // from class: com.android.server.soundtrigger_middleware.DefaultHalFactory$$ExternalSyntheticLambda0
                        @Override // java.lang.Runnable
                        public final void run() {
                            SystemProperties.set("sys.audio.restart.hal", "1");
                        }
                    });
                }
                Log.i(TAG, "Connecting to default soundtrigger-V2.x.ISoundTriggerHw");
                android.hardware.soundtrigger.V2_0.ISoundTriggerHw service = android.hardware.soundtrigger.V2_0.ISoundTriggerHw.getService(true);
                if (service == null) {
                    throw new RemoteException("driver is null");
                }
                return SoundTriggerHw2Compat.create(service, new Runnable() { // from class: com.android.server.soundtrigger_middleware.DefaultHalFactory$$ExternalSyntheticLambda1
                    @Override // java.lang.Runnable
                    public final void run() {
                        SystemProperties.set("sys.audio.restart.hal", "1");
                    }
                }, mCaptureStateNotifier);
            }
            if (i == 2) {
                Log.i(TAG, "Connecting to mock soundtrigger-V2.x.ISoundTriggerHw");
                HwBinder.setTrebleTestingOverride(true);
                try {
                    final android.hardware.soundtrigger.V2_0.ISoundTriggerHw service2 = android.hardware.soundtrigger.V2_0.ISoundTriggerHw.getService("mock", true);
                    return SoundTriggerHw2Compat.create(service2, new Runnable() { // from class: com.android.server.soundtrigger_middleware.DefaultHalFactory$$ExternalSyntheticLambda2
                        @Override // java.lang.Runnable
                        public final void run() {
                            DefaultHalFactory.lambda$create$2(service2);
                        }
                    }, mCaptureStateNotifier);
                } finally {
                    HwBinder.setTrebleTestingOverride(false);
                }
            }
            if (i == 3) {
                final String str2 = ISoundTriggerHw.class.getCanonicalName() + "/mock";
                Log.i(TAG, "Connecting to mock soundtrigger3.ISoundTriggerHw");
                return new SoundTriggerHw3Compat(ServiceManager.waitForService(str2), new Runnable() { // from class: com.android.server.soundtrigger_middleware.DefaultHalFactory$$ExternalSyntheticLambda3
                    @Override // java.lang.Runnable
                    public final void run() {
                        DefaultHalFactory.lambda$create$3(str2);
                    }
                });
            }
            throw new RuntimeException("Unknown HAL mock version: " + i);
        } catch (RemoteException e) {
            throw e.rethrowAsRuntimeException();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$create$2(android.hardware.soundtrigger.V2_0.ISoundTriggerHw iSoundTriggerHw) {
        try {
            iSoundTriggerHw.debug((NativeHandle) null, new ArrayList(Arrays.asList("reboot")));
        } catch (Exception e) {
            Log.e(TAG, "Failed to reboot mock HAL", e);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$create$3(String str) {
        try {
            ServiceManager.waitForService(str).shellCommand(null, null, null, new String[]{"reboot"}, null, null);
        } catch (Exception e) {
            Log.e(TAG, "Failed to reboot mock HAL", e);
        }
    }
}
