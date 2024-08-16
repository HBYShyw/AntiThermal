package com.android.server.wm;

import android.provider.DeviceConfig;
import android.provider.DeviceConfigInterface;
import com.android.internal.annotations.VisibleForTesting;
import java.io.PrintWriter;
import java.util.Objects;
import java.util.concurrent.Executor;
import java.util.function.Consumer;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
public final class WindowManagerConstants {
    static final String KEY_SYSTEM_GESTURE_EXCLUSION_LOG_DEBOUNCE_MILLIS = "system_gesture_exclusion_log_debounce_millis";
    private static final int MIN_GESTURE_EXCLUSION_LIMIT_DP = 200;
    private final DeviceConfigInterface mDeviceConfig;
    private final WindowManagerGlobalLock mGlobalLock;
    private final DeviceConfig.OnPropertiesChangedListener mListenerAndroid;
    private final DeviceConfig.OnPropertiesChangedListener mListenerWindowManager;
    boolean mSystemGestureExcludedByPreQStickyImmersive;
    int mSystemGestureExclusionLimitDp;
    long mSystemGestureExclusionLogDebounceTimeoutMillis;
    private final Runnable mUpdateSystemGestureExclusionCallback;

    /* JADX INFO: Access modifiers changed from: package-private */
    public WindowManagerConstants(final WindowManagerService windowManagerService, DeviceConfigInterface deviceConfigInterface) {
        this(windowManagerService.mGlobalLock, new Runnable() { // from class: com.android.server.wm.WindowManagerConstants$$ExternalSyntheticLambda2
            @Override // java.lang.Runnable
            public final void run() {
                WindowManagerConstants.lambda$new$0(WindowManagerService.this);
            }
        }, deviceConfigInterface);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$new$0(WindowManagerService windowManagerService) {
        windowManagerService.mRoot.forAllDisplays(new Consumer() { // from class: com.android.server.wm.WindowManagerConstants$$ExternalSyntheticLambda3
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                ((DisplayContent) obj).updateSystemGestureExclusionLimit();
            }
        });
    }

    @VisibleForTesting
    WindowManagerConstants(WindowManagerGlobalLock windowManagerGlobalLock, Runnable runnable, DeviceConfigInterface deviceConfigInterface) {
        Objects.requireNonNull(windowManagerGlobalLock);
        this.mGlobalLock = windowManagerGlobalLock;
        Objects.requireNonNull(runnable);
        this.mUpdateSystemGestureExclusionCallback = runnable;
        this.mDeviceConfig = deviceConfigInterface;
        this.mListenerAndroid = new DeviceConfig.OnPropertiesChangedListener() { // from class: com.android.server.wm.WindowManagerConstants$$ExternalSyntheticLambda0
            public final void onPropertiesChanged(DeviceConfig.Properties properties) {
                WindowManagerConstants.this.onAndroidPropertiesChanged(properties);
            }
        };
        this.mListenerWindowManager = new DeviceConfig.OnPropertiesChangedListener() { // from class: com.android.server.wm.WindowManagerConstants$$ExternalSyntheticLambda1
            public final void onPropertiesChanged(DeviceConfig.Properties properties) {
                WindowManagerConstants.this.onWindowPropertiesChanged(properties);
            }
        };
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void start(Executor executor) {
        this.mDeviceConfig.addOnPropertiesChangedListener("android", executor, this.mListenerAndroid);
        this.mDeviceConfig.addOnPropertiesChangedListener("window_manager", executor, this.mListenerWindowManager);
        updateSystemGestureExclusionLogDebounceMillis();
        updateSystemGestureExclusionLimitDp();
        updateSystemGestureExcludedByPreQStickyImmersive();
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Removed duplicated region for block: B:16:0x004a  */
    /* JADX WARN: Removed duplicated region for block: B:25:0x0051 A[SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void onAndroidPropertiesChanged(DeviceConfig.Properties properties) {
        char c;
        WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
        WindowManagerService.boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                boolean z = false;
                for (String str : properties.getKeyset()) {
                    if (str == null) {
                        WindowManagerService.resetPriorityAfterLockedSection();
                        return;
                    }
                    int hashCode = str.hashCode();
                    if (hashCode != -1271675449) {
                        if (hashCode == 316878247 && str.equals("system_gesture_exclusion_limit_dp")) {
                            c = 0;
                            if (c != 0) {
                                updateSystemGestureExclusionLimitDp();
                            } else if (c == 1) {
                                updateSystemGestureExcludedByPreQStickyImmersive();
                            }
                            z = true;
                        }
                        c = 65535;
                        if (c != 0) {
                        }
                        z = true;
                    } else {
                        if (str.equals("system_gestures_excluded_by_pre_q_sticky_immersive")) {
                            c = 1;
                            if (c != 0) {
                            }
                            z = true;
                        }
                        c = 65535;
                        if (c != 0) {
                        }
                        z = true;
                    }
                }
                if (z) {
                    this.mUpdateSystemGestureExclusionCallback.run();
                }
                WindowManagerService.resetPriorityAfterLockedSection();
            } catch (Throwable th) {
                WindowManagerService.resetPriorityAfterLockedSection();
                throw th;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Removed duplicated region for block: B:14:0x0039 A[SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:18:0x0038 A[SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void onWindowPropertiesChanged(DeviceConfig.Properties properties) {
        char c;
        WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
        WindowManagerService.boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                for (String str : properties.getKeyset()) {
                    if (str == null) {
                        WindowManagerService.resetPriorityAfterLockedSection();
                        return;
                    }
                    if (str.hashCode() == -125834358 && str.equals(KEY_SYSTEM_GESTURE_EXCLUSION_LOG_DEBOUNCE_MILLIS)) {
                        c = 0;
                        if (c != 0) {
                            updateSystemGestureExclusionLogDebounceMillis();
                        }
                    }
                    c = 65535;
                    if (c != 0) {
                    }
                }
                WindowManagerService.resetPriorityAfterLockedSection();
            } catch (Throwable th) {
                WindowManagerService.resetPriorityAfterLockedSection();
                throw th;
            }
        }
    }

    private void updateSystemGestureExclusionLogDebounceMillis() {
        this.mSystemGestureExclusionLogDebounceTimeoutMillis = this.mDeviceConfig.getLong("window_manager", KEY_SYSTEM_GESTURE_EXCLUSION_LOG_DEBOUNCE_MILLIS, 0L);
    }

    private void updateSystemGestureExclusionLimitDp() {
        this.mSystemGestureExclusionLimitDp = Math.max(MIN_GESTURE_EXCLUSION_LIMIT_DP, this.mDeviceConfig.getInt("android", "system_gesture_exclusion_limit_dp", 0));
    }

    private void updateSystemGestureExcludedByPreQStickyImmersive() {
        this.mSystemGestureExcludedByPreQStickyImmersive = this.mDeviceConfig.getBoolean("android", "system_gestures_excluded_by_pre_q_sticky_immersive", false);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void dump(PrintWriter printWriter) {
        printWriter.println("WINDOW MANAGER CONSTANTS (dumpsys window constants):");
        printWriter.print("  ");
        printWriter.print(KEY_SYSTEM_GESTURE_EXCLUSION_LOG_DEBOUNCE_MILLIS);
        printWriter.print("=");
        printWriter.println(this.mSystemGestureExclusionLogDebounceTimeoutMillis);
        printWriter.print("  ");
        printWriter.print("system_gesture_exclusion_limit_dp");
        printWriter.print("=");
        printWriter.println(this.mSystemGestureExclusionLimitDp);
        printWriter.print("  ");
        printWriter.print("system_gestures_excluded_by_pre_q_sticky_immersive");
        printWriter.print("=");
        printWriter.println(this.mSystemGestureExcludedByPreQStickyImmersive);
        printWriter.println();
    }
}
