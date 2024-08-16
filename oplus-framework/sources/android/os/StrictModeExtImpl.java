package android.os;

import android.app.ActivityThread;
import android.app.OplusUxIconConstants;
import android.content.pm.ApplicationInfo;
import android.os.StrictMode;
import android.os.strictmode.BindServiceViolation;
import android.os.strictmode.BinderCallViolation;
import android.os.strictmode.ContentProviderViolation;
import android.os.strictmode.LooperMsgViolation;
import android.os.strictmode.RegisterReceiverViolation;
import android.util.ArraySet;
import android.util.Log;
import android.util.StatsEvent;
import android.util.StatsLog;
import com.oplus.view.OplusWindowUtils;

/* loaded from: classes.dex */
public class StrictModeExtImpl implements IStrictModeExt {
    private static final int MAX_OFFENSES_PER_LOOP_LOCALPERFMONITOR;
    private static final ArraySet<String> NEED_MONITOR_PACKAGES_LIST;
    private static final long SLOW_DELIVERY_THRESHOLD_MS;
    private static final long SLOW_DISPATCH_THRESHOLD_MS;
    private static final String TAG = "StrictMode";
    private static boolean sIsAnimating;
    private static String sMonitorPkg;
    private static boolean sPerfMonitorEnable;
    private static boolean sStrictDebug;
    IStrictModeWrapper mStrictModeWrapper = null;

    static {
        ArraySet<String> arraySet = new ArraySet<>();
        NEED_MONITOR_PACKAGES_LIST = arraySet;
        arraySet.add("com.android.launcher");
        arraySet.add("com.android.systemui");
        arraySet.add("com.android.settings");
        arraySet.add("com.coloros.gallery3d");
        arraySet.add(OplusWindowUtils.PACKAGE_ASSISTANTSCREEN);
        arraySet.add("com.android.mms");
        arraySet.add(OplusUxIconConstants.IconLoader.COM_ANDROID_CONTACTS);
        MAX_OFFENSES_PER_LOOP_LOCALPERFMONITOR = SystemProperties.getInt("persist.sys.max.per.loop", 100);
        SLOW_DISPATCH_THRESHOLD_MS = SystemProperties.getLong("persist.sys.msg.dispatch.threshold", 100L);
        SLOW_DELIVERY_THRESHOLD_MS = SystemProperties.getLong("persist.sys.msg.delivery.threshold", 200L);
        sPerfMonitorEnable = SystemProperties.getBoolean("persist.sys.strictmode.LocalPerfMonitor", false);
        sStrictDebug = SystemProperties.getBoolean("persist.sys.strictmode.debug", false);
        sMonitorPkg = SystemProperties.get("persist.sys.strictmode.monitor.package", "");
        sIsAnimating = false;
    }

    public StrictModeExtImpl(Object base) {
    }

    public void initStrictModeExt(IStrictModeWrapper smw) {
        this.mStrictModeWrapper = smw;
    }

    public void detectRegisterReceiver(StrictMode.ThreadPolicy.Builder builder) {
        IStrictModeWrapper iStrictModeWrapper = this.mStrictModeWrapper;
        if (iStrictModeWrapper == null) {
            if (sStrictDebug) {
                Log.d(TAG, "detectRegisterReceiver mStrictModeWrapper == null");
                return;
            }
            return;
        }
        iStrictModeWrapper.enable(builder, 256);
    }

    public void permitRegisterReceiver(StrictMode.ThreadPolicy.Builder builder) {
        IStrictModeWrapper iStrictModeWrapper = this.mStrictModeWrapper;
        if (iStrictModeWrapper == null) {
            if (sStrictDebug) {
                Log.d(TAG, "permitRegisterReceiver mStrictModeWrapper == null");
                return;
            }
            return;
        }
        iStrictModeWrapper.disable(builder, 256);
    }

    public void detectBindService(StrictMode.ThreadPolicy.Builder builder) {
        IStrictModeWrapper iStrictModeWrapper = this.mStrictModeWrapper;
        if (iStrictModeWrapper == null) {
            if (sStrictDebug) {
                Log.d(TAG, "detectBindService mStrictModeWrapper == null");
                return;
            }
            return;
        }
        iStrictModeWrapper.enable(builder, 512);
    }

    public void permitBindService(StrictMode.ThreadPolicy.Builder builder) {
        IStrictModeWrapper iStrictModeWrapper = this.mStrictModeWrapper;
        if (iStrictModeWrapper == null) {
            if (sStrictDebug) {
                Log.d(TAG, "permitBindService mStrictModeWrapper == null");
                return;
            }
            return;
        }
        iStrictModeWrapper.disable(builder, 512);
    }

    public void detectContentProvider(StrictMode.ThreadPolicy.Builder builder) {
        IStrictModeWrapper iStrictModeWrapper = this.mStrictModeWrapper;
        if (iStrictModeWrapper == null) {
            if (sStrictDebug) {
                Log.d(TAG, "detectContentProvider mStrictModeWrapper == null");
                return;
            }
            return;
        }
        iStrictModeWrapper.enable(builder, 1024);
    }

    public void permitContentProvider(StrictMode.ThreadPolicy.Builder builder) {
        IStrictModeWrapper iStrictModeWrapper = this.mStrictModeWrapper;
        if (iStrictModeWrapper == null) {
            if (sStrictDebug) {
                Log.d(TAG, "permitContentProvider mStrictModeWrapper == null");
                return;
            }
            return;
        }
        iStrictModeWrapper.disable(builder, 1024);
    }

    public void detectBinderCall(StrictMode.ThreadPolicy.Builder builder) {
        IStrictModeWrapper iStrictModeWrapper = this.mStrictModeWrapper;
        if (iStrictModeWrapper == null) {
            if (sStrictDebug) {
                Log.d(TAG, "detectBinderCall mStrictModeWrapper == null");
                return;
            }
            return;
        }
        iStrictModeWrapper.enable(builder, 2048);
    }

    public void permitBinderCall(StrictMode.ThreadPolicy.Builder builder) {
        IStrictModeWrapper iStrictModeWrapper = this.mStrictModeWrapper;
        if (iStrictModeWrapper == null) {
            if (sStrictDebug) {
                Log.d(TAG, "permitBinderCall mStrictModeWrapper == null");
                return;
            }
            return;
        }
        iStrictModeWrapper.disable(builder, 2048);
    }

    public void detectLooperMsg(StrictMode.ThreadPolicy.Builder builder) {
        IStrictModeWrapper iStrictModeWrapper = this.mStrictModeWrapper;
        if (iStrictModeWrapper == null) {
            if (sStrictDebug) {
                Log.d(TAG, "detectLooperMsg mStrictModeWrapper == null");
                return;
            }
            return;
        }
        iStrictModeWrapper.enable(builder, 4096);
    }

    public void permitLooperMsg(StrictMode.ThreadPolicy.Builder builder) {
        IStrictModeWrapper iStrictModeWrapper = this.mStrictModeWrapper;
        if (iStrictModeWrapper == null) {
            if (sStrictDebug) {
                Log.d(TAG, "permitLooperMsg mStrictModeWrapper == null");
                return;
            }
            return;
        }
        iStrictModeWrapper.disable(builder, 4096);
    }

    private static boolean isSelfDevelopApp(ApplicationInfo ai) {
        if (ai == null) {
            return false;
        }
        String pkgName = ai.processName;
        return pkgName.startsWith("com.oplus.") || pkgName.startsWith("com.oppo.") || pkgName.startsWith("com.coloros.") || pkgName.startsWith("com.nearme.") || pkgName.startsWith("com.heytap.");
    }

    private static boolean isSpecailApp(ApplicationInfo ai) {
        if (ai == null) {
            return false;
        }
        String pkgName = ai.processName;
        return NEED_MONITOR_PACKAGES_LIST.contains(pkgName);
    }

    private static boolean isMonitorApp(ApplicationInfo ai) {
        if (ai == null) {
            return false;
        }
        String pkgName = ai.processName;
        return sMonitorPkg.equals(pkgName);
    }

    public void setAnimating(boolean animating) {
        if (!sPerfMonitorEnable) {
            return;
        }
        boolean mainThread = "main".equals(Thread.currentThread().getName());
        if (mainThread) {
            if (sStrictDebug) {
                Log.d(TAG, "setAnimating = " + animating);
            }
            sIsAnimating = animating;
        }
    }

    public boolean isAnimating() {
        if (sStrictDebug) {
            Log.d(TAG, "isAnimating = " + sIsAnimating);
        }
        return sIsAnimating;
    }

    public boolean isPerfMonitorEnable() {
        if (sStrictDebug) {
            Log.d(TAG, "isPerfMonitorEnable = " + sPerfMonitorEnable);
        }
        return sPerfMonitorEnable;
    }

    public void onWriteToDisk() {
        if (!sPerfMonitorEnable) {
            if (sStrictDebug) {
                Log.d(TAG, "onWriteToDisk skip Quality");
            }
        } else {
            ActivityThread.currentActivityThread();
            String processName = ActivityThread.currentProcessName();
            Log.p("Quality", "local:strictmode," + processName + ",1,1,onWriteToDisk");
            writeAtomValue(1, 2, "strictmode", processName, "onWriteToDisk", false);
        }
    }

    public void onReadFromDisk() {
        if (!sPerfMonitorEnable) {
            if (sStrictDebug) {
                Log.d(TAG, "onReadFromDisk skip Quality");
            }
        } else {
            ActivityThread.currentActivityThread();
            String processName = ActivityThread.currentProcessName();
            Log.p("Quality", "local:strictmode," + processName + ",1,1,onReadFromDisk");
            writeAtomValue(1, 2, "strictmode", processName, "onReadFromDisk", false);
        }
    }

    public void onNetwork() {
        if (!sPerfMonitorEnable) {
            if (sStrictDebug) {
                Log.d(TAG, "onNetwork skip Quality");
            }
        } else {
            ActivityThread.currentActivityThread();
            String processName = ActivityThread.currentProcessName();
            Log.p("Quality", "local:strictmode," + processName + ",1,2,onNetwork");
            writeAtomValue(1, 2, "strictmode", processName, "onNetwork", false);
        }
    }

    public void onRegisterReceiver() {
        IStrictModeWrapper iStrictModeWrapper = this.mStrictModeWrapper;
        if (iStrictModeWrapper == null) {
            if (sStrictDebug) {
                Log.d(TAG, "onRegisterReceiver mStrictModeWrapper == null");
            }
        } else {
            if ((iStrictModeWrapper.getThreadPolicyMask() & 256) == 0 || this.mStrictModeWrapper.tooManyViolationsThisLoop()) {
                return;
            }
            if (sPerfMonitorEnable && !isAnimating()) {
                if (sStrictDebug) {
                    Log.d(TAG, "onRegisterReceiver skip not Animating");
                }
            } else {
                this.mStrictModeWrapper.startHandlingViolationException(new RegisterReceiverViolation());
                ActivityThread.currentActivityThread();
                String processName = ActivityThread.currentProcessName();
                Log.p("Quality", "local:strictmode," + processName + ",1,4,onRegisterReceiver");
                writeAtomValue(1, 2, "strictmode", processName, "onRegisterReceiver", false);
            }
        }
    }

    public void onBindService() {
        IStrictModeWrapper iStrictModeWrapper = this.mStrictModeWrapper;
        if (iStrictModeWrapper == null) {
            if (sStrictDebug) {
                Log.d(TAG, "onBindService mStrictModeWrapper == null");
            }
        } else {
            if ((iStrictModeWrapper.getThreadPolicyMask() & 512) == 0 || this.mStrictModeWrapper.tooManyViolationsThisLoop()) {
                return;
            }
            if (sPerfMonitorEnable && !isAnimating()) {
                if (sStrictDebug) {
                    Log.d(TAG, "onBindService skip not Animating");
                }
            } else {
                this.mStrictModeWrapper.startHandlingViolationException(new BindServiceViolation());
                ActivityThread.currentActivityThread();
                String processName = ActivityThread.currentProcessName();
                Log.p("Quality", "local:strictmode," + processName + ",1,8,onBindService");
                writeAtomValue(1, 2, "strictmode", processName, "onBindService", false);
            }
        }
    }

    public void onContentProvider() {
        IStrictModeWrapper iStrictModeWrapper = this.mStrictModeWrapper;
        if (iStrictModeWrapper == null) {
            if (sStrictDebug) {
                Log.d(TAG, "onContentProvider mStrictModeWrapper == null");
            }
        } else {
            if ((iStrictModeWrapper.getThreadPolicyMask() & 1024) == 0 || this.mStrictModeWrapper.tooManyViolationsThisLoop()) {
                return;
            }
            if (sPerfMonitorEnable && !isAnimating()) {
                if (sStrictDebug) {
                    Log.d(TAG, "onContentProvider skip not Animating");
                }
            } else {
                this.mStrictModeWrapper.startHandlingViolationException(new ContentProviderViolation());
                ActivityThread.currentActivityThread();
                String processName = ActivityThread.currentProcessName();
                Log.p("Quality", "local:strictmode," + processName + ",1,16,onContentProvider");
                writeAtomValue(1, 2, "strictmode", processName, "onContentProvider", false);
            }
        }
    }

    public void onBinderCall() {
        IStrictModeWrapper iStrictModeWrapper = this.mStrictModeWrapper;
        if (iStrictModeWrapper == null) {
            if (sStrictDebug) {
                Log.d(TAG, "onBinderCall mStrictModeWrapper == null");
            }
        } else {
            if ((iStrictModeWrapper.getThreadPolicyMask() & 2048) == 0 || this.mStrictModeWrapper.tooManyViolationsThisLoop()) {
                return;
            }
            if (sPerfMonitorEnable && !isAnimating()) {
                if (sStrictDebug) {
                    Log.d(TAG, "onBinderCall skip not Animating");
                }
            } else {
                this.mStrictModeWrapper.startHandlingViolationException(new BinderCallViolation());
                ActivityThread.currentActivityThread();
                String processName = ActivityThread.currentProcessName();
                Log.p("Quality", "local:strictmode," + processName + ",1,512,onBinderCall");
                writeAtomValue(1, 2, "strictmode", processName, "onBinderCall", false);
            }
        }
    }

    public void onLooperMsg(String msg) {
        IStrictModeWrapper iStrictModeWrapper = this.mStrictModeWrapper;
        if (iStrictModeWrapper == null) {
            if (sStrictDebug) {
                Log.d(TAG, "onLooperMsg mStrictModeWrapper == null");
            }
        } else {
            if ((iStrictModeWrapper.getThreadPolicyMask() & 4096) == 0 || this.mStrictModeWrapper.tooManyViolationsThisLoop()) {
                return;
            }
            if (sPerfMonitorEnable && !isAnimating()) {
                if (sStrictDebug) {
                    Log.d(TAG, "onLooperMsg skip not Animating");
                }
            } else {
                this.mStrictModeWrapper.startHandlingViolationException(new LooperMsgViolation(msg));
                ActivityThread.currentActivityThread();
                String processName = ActivityThread.currentProcessName();
                Log.p("Quality", "local:strictmode," + processName + ",1,256," + msg);
                writeAtomValue(1, 2, "strictmode", processName, msg, false);
            }
        }
    }

    public void setMonitorSettings(StrictMode.ThreadPolicy.Builder builder, ApplicationInfo ai) {
        if (sStrictDebug) {
            Log.d(TAG, "initThreadDefaults " + ai);
        }
        if (sPerfMonitorEnable && isMonitorApp(ai)) {
            if (sStrictDebug) {
                Log.d(TAG, "initThreadDefaults enable " + ai);
            }
            builder.permitAll();
            builder.detectDiskReads();
            builder.detectDiskWrites();
            builder.detectNetwork();
            builder.penaltyLog();
            builder.penaltyDropBox();
            builder.penaltyFlashScreen();
            builder.detectCustomSlowCalls();
            detectRegisterReceiver(builder);
            detectBindService(builder);
            detectContentProvider(builder);
            detectBinderCall(builder);
            detectLooperMsg(builder);
            Looper.getMainLooper().setSlowLogThresholdMs(SLOW_DISPATCH_THRESHOLD_MS, SLOW_DELIVERY_THRESHOLD_MS);
        }
    }

    public boolean isCustomSlowCallEnable() {
        IStrictModeWrapper iStrictModeWrapper = this.mStrictModeWrapper;
        if (iStrictModeWrapper == null) {
            if (sStrictDebug) {
                Log.d(TAG, "call isCustomSlowCallEnable");
                return false;
            }
            return false;
        }
        return iStrictModeWrapper.isCustomSlowCallEnable();
    }

    public static void writeAtomValue(int reporttype, int scenetype, String scene, String packagename, String content, boolean issystrace) {
        if (!sPerfMonitorEnable) {
            if (sStrictDebug) {
                Log.d(TAG, "writeAtomValue skip not enable");
                return;
            }
            return;
        }
        StatsEvent.Builder builder = StatsEvent.newBuilder();
        builder.setAtomId(100071);
        builder.writeLong(System.currentTimeMillis());
        builder.writeInt(reporttype);
        builder.writeInt(scenetype);
        builder.writeString(scene);
        builder.writeString(packagename);
        builder.writeString(content);
        builder.writeBoolean(issystrace);
        builder.usePooledBuffer();
        StatsLog.write(builder.build());
    }
}
