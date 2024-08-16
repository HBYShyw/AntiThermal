package android.os;

import android.app.IOplusApplicationThread;
import android.telephony.IOplusOSTelephony;
import android.util.Log;
import android.view.IOplusBaseWindowManager;
import dalvik.annotation.optimization.CriticalNative;
import dalvik.annotation.optimization.FastNative;
import java.util.HashMap;

/* loaded from: classes.dex */
public enum OneTraceExtImpl implements IOneTraceExt {
    INSTANCE;

    private static final String ONETRACE_LIB = "onetrace_jni";
    private static final String TAG = "OneTrace";
    private static volatile boolean sOneTraceJniNotImplement;
    private static final String[] INTERFACE_LIST = {"android.app.IAlarmManager", "android.app.IActivityManager", IOplusApplicationThread.DESCRIPTOR, "android.content.pm.IPackageManager", "android.os.IServiceManager", "android.view.accessibility.IAccessibilityManagerClient", "android.content.IContentProvider", "android.net.INetworkStatsService", "android.net.IConnectivityManager", "android.os.storage.IStorageManager", "android.view.IWindowSession", "android.permission.IPermissionManager", IOplusOSTelephony.OPLUS_SINGLE_CARD_DESCRIPTOR, "android.media.IPlaybackConfigDispatcher", "android.view.accessibility.IAccessibilityManager", IOplusExInputCallBack.DESCRIPTOR, "android.content.IBulkCursor", "android.bluetooth.le.IScannerCallback", "com.android.internal.app.IAppOpsService", "android.content.IContentService", "android.net.metrics.INetdEventListener", IOplusBaseWindowManager.DESCRIPTOR, "android.hardware.input.IInputManager", IOplusTraceService.DESCRIPTOR, IOplusTraceCallBack.DESCRIPTOR, "android.location.IGnssStatusListener", "com.android.internal.app.IBatteryStats", "com.android.internal.app.IAppOpsNotedCallback", "android.hardware.display.IDisplayManagerCallback", IOplusFilterListener.DESCRIPTOR, "android.database.IContentObserver"};
    private static volatile boolean sMarkActiveInProcess = false;

    @FastNative
    private static native void nativeOneTraceBegin(long j, String str);

    @FastNative
    private static native void nativeOneTraceBeginAsync(long j, String str, int i);

    @FastNative
    private static native void nativeOneTraceEnd(long j);

    @FastNative
    private static native void nativeOneTraceEndAsync(long j, String str, int i);

    @CriticalNative
    private static native long nativeOneTraceGetEnabledTags();

    @FastNative
    private static native void nativeSetTraceEnabled(boolean z);

    static {
        sOneTraceJniNotImplement = false;
        try {
            System.loadLibrary(ONETRACE_LIB);
        } catch (UnsatisfiedLinkError e) {
            Log.e(TAG, "Failed to LoadLibrary onetrace_jni");
            sOneTraceJniNotImplement = true;
        }
    }

    public boolean isEnable(long traceTag) {
        return isOneTraceEnable(traceTag);
    }

    public void begin(long traceTag, String methodName) {
        oneTraceBegin(traceTag, methodName);
    }

    public void end(long traceTag) {
        oneTraceEnd(traceTag);
    }

    public void beginAsync(long traceTag, String methodName, int cookie) {
        oneTraceBeginAsync(traceTag, methodName, cookie);
    }

    public void endAsync(long traceTag, String methodName, int cookie) {
        oneTraceEndAsync(traceTag, methodName, cookie);
    }

    private static boolean isOneTraceEnable(long traceTag) {
        if (!sMarkActiveInProcess || sOneTraceJniNotImplement) {
            return false;
        }
        long tags = 0;
        try {
            tags = nativeOneTraceGetEnabledTags();
        } catch (UnsatisfiedLinkError e) {
            Log.w(TAG, "Onetrace jni was not implemented in android runtime");
            sOneTraceJniNotImplement = true;
        }
        return (tags & traceTag) != 0;
    }

    public void setActiveState(boolean isActive) {
        Log.i(TAG, "Mark active for pid=" + Process.myPid() + "? " + isActive);
        sMarkActiveInProcess = isActive;
        oneTraceSetTraceEnabled(isActive);
    }

    private static void oneTraceBegin(long traceTag, String methodName) {
        if (sOneTraceJniNotImplement) {
            return;
        }
        try {
            nativeOneTraceBegin(traceTag, methodName);
        } catch (UnsatisfiedLinkError e) {
            Log.w(TAG, "Onetrace jni was not implemented in android runtime");
            sOneTraceJniNotImplement = true;
        }
    }

    private static void oneTraceEnd(long traceTag) {
        if (sOneTraceJniNotImplement) {
            return;
        }
        try {
            nativeOneTraceEnd(traceTag);
        } catch (UnsatisfiedLinkError e) {
            Log.w(TAG, "Onetrace jni was not implemented in android runtime");
            sOneTraceJniNotImplement = true;
        }
    }

    private static void oneTraceBeginAsync(long traceTag, String methodName, int cookie) {
        if (sOneTraceJniNotImplement) {
            return;
        }
        try {
            nativeOneTraceBeginAsync(traceTag, methodName, cookie);
        } catch (UnsatisfiedLinkError e) {
            Log.w(TAG, "Onetrace jni was not implemented in android runtime");
            sOneTraceJniNotImplement = true;
        }
    }

    private static void oneTraceEndAsync(long traceTag, String methodName, int cookie) {
        if (sOneTraceJniNotImplement) {
            return;
        }
        try {
            nativeOneTraceEndAsync(traceTag, methodName, cookie);
        } catch (UnsatisfiedLinkError e) {
            Log.w(TAG, "Onetrace jni was not implemented in android runtime");
            sOneTraceJniNotImplement = true;
        }
    }

    public String matchDescriptor(String descriptor) {
        return (String) LazyLoadDescriptorMap.DESCRIPTOR_ID_MAP.getOrDefault(descriptor, descriptor);
    }

    static void oneTraceSetTraceEnabled(boolean enabled) {
        if (sOneTraceJniNotImplement) {
            return;
        }
        try {
            nativeSetTraceEnabled(enabled);
        } catch (UnsatisfiedLinkError e) {
            Log.w(TAG, "Onetrace jni was not implemented in android runtime");
            sOneTraceJniNotImplement = true;
        }
    }

    /* loaded from: classes.dex */
    private static class LazyLoadDescriptorMap {
        private static final HashMap<String, String> DESCRIPTOR_ID_MAP = new HashMap<>();

        private LazyLoadDescriptorMap() {
        }

        static {
            int length = OneTraceExtImpl.INTERFACE_LIST.length;
            for (int i = 0; i < length; i++) {
                DESCRIPTOR_ID_MAP.put(OneTraceExtImpl.INTERFACE_LIST[i], String.valueOf(i));
            }
        }
    }
}
