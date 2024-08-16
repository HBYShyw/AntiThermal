package com.android.server.audio;

import android.content.Context;
import android.hardware.devicestate.DeviceStateManager;
import android.hardware.display.DisplayManager;
import android.hardware.display.DisplayManagerGlobal;
import android.os.Handler;
import android.os.HandlerExecutor;
import android.util.Log;
import com.android.bluetooth.BluetoothStatsLog;
import com.android.internal.util.FrameworkStatsLog;
import java.util.function.Consumer;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
class RotationHelper {
    private static final boolean DEBUG_ROTATION = false;
    private static final String TAG = "AudioService.RotationHelper";
    private static Context sContext;
    private static AudioDisplayListener sDisplayListener;
    private static Boolean sFoldState;
    private static Consumer<Boolean> sFoldStateCallback;
    private static DeviceStateManager.FoldStateListener sFoldStateListener;
    private static Handler sHandler;
    private static Integer sRotation;
    private static Consumer<Integer> sRotationCallback;
    private static final Object sRotationLock = new Object();
    private static final Object sFoldStateLock = new Object();

    RotationHelper() {
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void init(Context context, Handler handler, Consumer<Integer> consumer, Consumer<Boolean> consumer2) {
        if (context == null) {
            throw new IllegalArgumentException("Invalid null context");
        }
        sContext = context;
        sHandler = handler;
        sDisplayListener = new AudioDisplayListener();
        sRotationCallback = consumer;
        sFoldStateCallback = consumer2;
        enable();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void enable() {
        ((DisplayManager) sContext.getSystemService("display")).registerDisplayListener(sDisplayListener, sHandler);
        updateOrientation();
        sFoldStateListener = new DeviceStateManager.FoldStateListener(sContext, new Consumer() { // from class: com.android.server.audio.RotationHelper$$ExternalSyntheticLambda0
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                RotationHelper.lambda$enable$0((Boolean) obj);
            }
        });
        ((DeviceStateManager) sContext.getSystemService(DeviceStateManager.class)).registerCallback(new HandlerExecutor(sHandler), sFoldStateListener);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$enable$0(Boolean bool) {
        updateFoldState(bool.booleanValue());
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void disable() {
        ((DisplayManager) sContext.getSystemService("display")).unregisterDisplayListener(sDisplayListener);
        ((DeviceStateManager) sContext.getSystemService(DeviceStateManager.class)).unregisterCallback(sFoldStateListener);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void updateOrientation() {
        int i = DisplayManagerGlobal.getInstance().getDisplayInfo(0).rotation;
        synchronized (sRotationLock) {
            Integer num = sRotation;
            if (num == null || num.intValue() != i) {
                Integer valueOf = Integer.valueOf(i);
                sRotation = valueOf;
                publishRotation(valueOf.intValue());
            }
        }
    }

    private static void publishRotation(int i) {
        int i2;
        if (i == 0) {
            i2 = 0;
        } else if (i == 1) {
            i2 = 90;
        } else if (i == 2) {
            i2 = FrameworkStatsLog.DEVICE_POLICY_EVENT__EVENT_ID__CREDENTIAL_MANAGEMENT_APP_REQUEST_ACCEPTED;
        } else if (i != 3) {
            Log.e(TAG, "Unknown device rotation");
            i2 = -1;
        } else {
            i2 = BluetoothStatsLog.BLUETOOTH_SMP_PAIRING_EVENT_REPORTED__SMP_COMMAND__CMD_BR_PAIRING_KEYPRESS_INFO;
        }
        if (i2 != -1) {
            sRotationCallback.accept(Integer.valueOf(i2));
        }
    }

    static void updateFoldState(boolean z) {
        synchronized (sFoldStateLock) {
            Boolean bool = sFoldState;
            if (bool == null || bool.booleanValue() != z) {
                sFoldState = Boolean.valueOf(z);
                sFoldStateCallback.accept(Boolean.valueOf(z));
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void forceUpdate() {
        synchronized (sRotationLock) {
            sRotation = null;
        }
        updateOrientation();
        synchronized (sFoldStateLock) {
            Boolean bool = sFoldState;
            if (bool != null) {
                sFoldStateCallback.accept(bool);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public static final class AudioDisplayListener implements DisplayManager.DisplayListener {
        @Override // android.hardware.display.DisplayManager.DisplayListener
        public void onDisplayAdded(int i) {
        }

        @Override // android.hardware.display.DisplayManager.DisplayListener
        public void onDisplayRemoved(int i) {
        }

        AudioDisplayListener() {
        }

        @Override // android.hardware.display.DisplayManager.DisplayListener
        public void onDisplayChanged(int i) {
            RotationHelper.updateOrientation();
        }
    }
}
