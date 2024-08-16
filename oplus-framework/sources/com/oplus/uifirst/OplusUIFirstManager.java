package com.oplus.uifirst;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.os.PerformanceManager;
import android.os.Process;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.os.SystemProperties;
import android.os.Trace;
import android.util.IntArray;
import android.util.Log;
import android.util.Pair;
import com.oplus.filter.DynamicFilterManager;
import com.oplus.filter.IDynamicFilterService;
import com.oplus.uifirst.Utils;
import dalvik.annotation.optimization.FastNative;
import java.util.Arrays;
import java.util.Collection;
import java.util.function.IntFunction;
import java.util.function.IntPredicate;
import java.util.stream.Collectors;

/* loaded from: classes.dex */
public class OplusUIFirstManager implements IOplusUIFirstManager {
    static final int APP_MOVE_TO_BG_DELAY = 1000;
    private static final String BINDER_UX_FLAG_PATH = "/proc/oplus_binder/ux_flag";
    private static final String FRAME_BOOST_DEBUG = "/proc/sys/fbg/frame_boost_debug";
    private static final String FRAME_BOOST_DEBUG_LEGACY = "/proc/sys/kernel/frame_boost_debug";
    static final int HANDLE_THREAD_OP_DELAY = 1000;
    static final int HOT_LAUNCH_BOOST_DURATION = 1000;
    private static final String INKERNEL_FBT_CPU_PARAM_BHR_OPP = "sys/module/fbt_cpu/parameters/bhr_opp";
    static final int LAUNCH_BOOST_DURATION = 3000;
    static final int MSG_APP_MOVE_TO_BG = 2;
    static final int MSG_APP_MOVE_TO_BG_DELAY = 3;
    static final int MSG_APP_STATUS_CHANGED = 0;
    static final int MSG_DISABLE_LAUNCH_BOOST = 1;
    static final int MSG_HANDEL_FBTHREAD_OP = 8;
    static final int MSG_HANDEL_FBTHREAD_OP_BY_PKG = 9;
    static final int MSG_HANDLE_GLTHREAD_OP = 4;
    static final int MSG_HANDLE_GLTHREAD_OP_BY_PKG = 5;
    static final int MSG_HANDLE_TPD_THREAD_OP = 6;
    static final int MSG_HANDLE_TPD_THREAD_OP_BY_PKG = 7;
    static final int NEW_LINUX_VERSION = 6;
    static final int PID_NUM = 4;
    static final String PKG_ASSISTANT_SCREEN = "com.coloros.assistantscreen";
    static final String PKG_BAIDUIMS = "com.baidu.input";
    static final String PKG_DUMMY_INPUT_METHOD = "DUMMY_INPUT_METHOD";
    static final String PKG_KEY_IMS = "inputmethod:";
    static final String PKG_LAUNCHER = "com.android.launcher";
    static final String PKG_SYSTEMUI = "com.android.systemui";
    static final int RENDER_PID = 1;
    private static final String SCHED_ASSIST_DEBUG = "/proc/oplus_scheduler/sched_assist/debug_enabled";
    private static final String SCHED_ASSIST_JNI_LIB = "SchedAssistJni";
    private static final String TAG = "OplusUIFirst";
    static final int TASK_ANIMATION_BOOST_FREQ = 50;
    static final int TASK_ANIMATION_BOOST_MIGR = 50;
    static final int UIFIRST_PRIORITY_TOP_APP = 167772160;
    static final int UIFIRST_PRIORITY_VIS_APP = 150994944;
    private static final String UX_IM_FLAG_PATH = "/proc/oplus_scheduler/sched_assist/im_flag";
    private static OplusUIFirstManager sInstance = null;
    private FBThreadManager mFBThreadManager;
    private IDynamicFilterService mFilterService;
    private GlThreadManager mGlThreadManager;
    private Handler mHandler;
    private String mRealInputMethodPkg;
    private HandlerThread mThread;
    private TpdManager mTpdManager;
    private UagManager mUagManager;
    private String mbhropp;
    private AppInfo mAssistantScreen = null;
    private AppInfo mOnTopSystemUi = null;
    private AppInfo mOnTopLauncher = null;
    private AppInfo mOnTopApp = null;
    private AppInfo mOnTopAppLast = null;
    private boolean mSystemUiShowed = false;
    private boolean mAssistantScreenShowed = false;
    private boolean mInputMethodShowed = false;
    private AppInfo mInputMethod = null;

    static native int[] nativeGetUxAppTids();

    @FastNative
    static native void nativeOfbBoostHint(int i, int i2, int i3, int i4, int i5, int i6, long j, long j2, long j3);

    /* JADX INFO: Access modifiers changed from: package-private */
    public static native void nativeOfbCfgAppFrameParam(int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8, int i9, int i10, int i11, int i12, int i13, int i14, int i15, int i16);

    /* JADX INFO: Access modifiers changed from: package-private */
    public static native void nativeOfbCfgEnabled(boolean z);

    /* JADX INFO: Access modifiers changed from: package-private */
    public static native void nativeOfbCfgFrameTask(int i, int i2, int i3, int i4, int i5);

    static native void nativeOfbCfgUxTask(int i, int i2, int i3, int i4, int i5);

    static native void nativeOfbEndFrame(int i, int i2, long j);

    static native void nativeOfbSetFps(int i, int i2, long j, long j2);

    static native void nativeSetImFlag(int i, int i2);

    static native void nativeSetProcessUx(int i, int i2);

    /* JADX INFO: Access modifiers changed from: package-private */
    public static native void nativeSetThreadUx(int i, int i2, int i3);

    private OplusUIFirstManager() {
        System.loadLibrary(SCHED_ASSIST_JNI_LIB);
    }

    @Override // com.oplus.uifirst.IOplusUIFirstManager
    public void serviceReady() {
        this.mFilterService = IDynamicFilterService.Stub.asInterface(ServiceManager.getService(DynamicFilterManager.SERVICE_NAME));
        HandlerThread handlerThread = new HandlerThread("OplusUIFirst", -2);
        this.mThread = handlerThread;
        handlerThread.start();
        this.mHandler = new UIFirstHandler(this.mThread.getLooper());
        nativeSetThreadUx(Process.myPid(), this.mThread.getThreadId(), 129);
        GlThreadManager.initialize(this, this.mHandler);
        this.mGlThreadManager = GlThreadManager.getInstance();
        this.mFBThreadManager = new FBThreadManager(this, this.mHandler);
        this.mTpdManager = new TpdManager(this, this.mHandler);
        this.mUagManager = new UagManager(this, this.mHandler);
        this.mTpdManager.handleProcessStart("system_server", 1000, Process.myPid(), false, "system_server");
        this.mTpdManager.handleProcessStart("system", 0, -1, false, "system");
        this.mInputMethod = new AppInfo(PKG_DUMMY_INPUT_METHOD, 0, 0, null, true);
        updateLoadBalanceConfig();
    }

    public static OplusUIFirstManager getInstance() {
        if (sInstance == null) {
            sInstance = new OplusUIFirstManager();
        }
        return sInstance;
    }

    @Override // com.oplus.uifirst.IOplusUIFirstManager
    public void acquireUxSceneBoost(String scene, int timeout) {
        if (this.mHandler.hasMessages(1)) {
            return;
        }
        PerformanceManager.setSchedAssistScene(scene);
        this.mHandler.sendEmptyMessageDelayed(1, timeout);
    }

    @Override // com.oplus.uifirst.IOplusUIFirstManager
    public void acquireLaunchBoost() {
        if (Utils.DEBUG) {
            Log.d("OplusUIFirst", "acquireLaunchBoost");
        }
        if (this.mHandler.hasMessages(1)) {
            this.mHandler.removeMessages(1);
        } else {
            PerformanceManager.setSchedAssistScene(String.valueOf(129));
        }
        this.mHandler.sendEmptyMessageDelayed(1, 3000L);
    }

    @Override // com.oplus.uifirst.IOplusUIFirstManager
    public void setResumedApp(String pkgName, int uid, int pid) {
        String inStr = "fg " + Integer.toString(pid);
        if ("com.android.launcher".equals(pkgName)) {
            inStr = "fgLauncher " + Integer.toString(pid);
        }
        PerformanceManager.setSchedAssistImptTask(inStr);
    }

    @Override // com.oplus.uifirst.IOplusUIFirstManager
    public void setTaskAnimation(String packageName, int type, int pid, int renderTid, String uxValue, String flag) {
        int[] iArr = new int[4];
        int[] appPids = getUxAppTids();
        int renderPid = appPids[1];
        if (Utils.DEBUG) {
            Log.d("OplusUIFirst", "setTaskAnimation: package is " + packageName + " type is " + type + " flag is " + flag + " pid is " + pid);
        }
        switch (type) {
            case 4:
                String sceneValue = String.valueOf(16);
                if (flag.equals("4")) {
                    String sceneValue2 = String.valueOf(144);
                    if (checkUxRemoteAnimationList(packageName)) {
                        Utils.setUxThreadValue(pid, pid, 132);
                        Utils.setUxThreadValue(pid, renderPid, 132);
                    }
                    Trace.asyncTraceBegin(64L, "Task SI Animation", 0);
                    sceneValue = sceneValue2;
                } else {
                    Trace.asyncTraceEnd(64L, "Task SI Animation", 0);
                }
                PerformanceManager.setSchedAssistScene(sceneValue);
                return;
            case 5:
                String sceneValue3 = String.valueOf(24);
                int processValue = 4;
                if (flag.equals("1")) {
                    String sceneValue4 = String.valueOf(136);
                    processValue = 132;
                    sceneValue3 = sceneValue4;
                } else if (!flag.equals("0")) {
                    String sceneValue5 = String.valueOf(144);
                    processValue = 132;
                    sceneValue3 = sceneValue5;
                }
                boolean isAppInAnimationList = checkUxRemoteAnimationList(packageName);
                if (!flag.equals("0")) {
                    if (isAppInAnimationList) {
                        nativeOfbCfgAppFrameParam(50, 50, 255, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1);
                    }
                    Trace.asyncTraceBegin(64L, "Task Animation", 0);
                } else {
                    if (isAppInAnimationList) {
                        nativeOfbCfgAppFrameParam(4474182, -1, 255, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1);
                    }
                    Trace.asyncTraceEnd(64L, "Task Animation", 0);
                }
                PerformanceManager.setSchedAssistScene(sceneValue3);
                if (isAppInAnimationList) {
                    Utils.setUxThreadValue(pid, pid, processValue);
                    Utils.setUxThreadValue(pid, renderPid, processValue);
                    return;
                }
                return;
            case 6:
                try {
                    int processValue2 = Integer.parseInt(uxValue);
                    if (processValue2 > 128) {
                        Trace.asyncTraceBegin(64L, "Set Process Ux: " + pid, pid);
                    } else {
                        Trace.asyncTraceEnd(64L, "Set Process Ux: " + pid, pid);
                    }
                    Utils.setUxThreadValue(pid, pid, processValue2);
                    return;
                } catch (NumberFormatException e) {
                    Log.e("OplusUIFirst", "set process ux value error, msg = " + e.toString());
                    return;
                }
            default:
                return;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean inFilter(String tag, String packageName) {
        try {
            IDynamicFilterService iDynamicFilterService = this.mFilterService;
            if (iDynamicFilterService == null) {
                return false;
            }
            if (iDynamicFilterService.inFilter(tag, packageName)) {
                return true;
            }
            return false;
        } catch (RemoteException e) {
            return false;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public String getFilterConfig(String tag, String packageName) {
        try {
            IDynamicFilterService iDynamicFilterService = this.mFilterService;
            if (iDynamicFilterService != null) {
                return iDynamicFilterService.getFilterTagValue(tag, packageName);
            }
            return null;
        } catch (RemoteException e) {
            return null;
        }
    }

    @Override // com.oplus.uifirst.IOplusUIFirstManager
    public void setUxThreadValue(int pid, int tid, String value) {
        if (pid == 0 || tid == 0) {
            return;
        }
        Utils.setUxThreadValue(pid, tid, value);
    }

    @Override // com.oplus.uifirst.IOplusUIFirstManager
    public void setUxThreadValueByFile(int pid, int tid, int value) {
        if (pid == 0 || tid == 0) {
            return;
        }
        nativeSetThreadUx(pid, tid, value);
    }

    @Override // com.oplus.uifirst.IOplusUIFirstManager
    public int[] getUxAppTids() {
        return nativeGetUxAppTids();
    }

    @Override // com.oplus.uifirst.IOplusUIFirstManager
    public void writeProcNode(String filePath, String val) {
        if (val != null) {
            Utils.writeProcNode(filePath, val);
        }
    }

    @Override // com.oplus.uifirst.IOplusUIFirstManager
    public String readProcNode(String filePath) {
        return Utils.readProcNode(filePath);
    }

    void uxTrace(int appPid, int renderThreadTid, int[] threads, boolean boost) {
        if (!Trace.isTagEnabled(64L)) {
            return;
        }
        StringBuilder tids = new StringBuilder();
        tids.append(appPid);
        if (renderThreadTid > 0) {
            tids.append(",").append(renderThreadTid);
        }
        if (threads.length > 0) {
            String hwuiTasks = (String) Arrays.stream(threads).filter(new IntPredicate() { // from class: com.oplus.uifirst.OplusUIFirstManager$$ExternalSyntheticLambda0
                @Override // java.util.function.IntPredicate
                public final boolean test(int i) {
                    return OplusUIFirstManager.lambda$uxTrace$0(i);
                }
            }).mapToObj(new IntFunction() { // from class: com.oplus.uifirst.OplusUIFirstManager$$ExternalSyntheticLambda1
                @Override // java.util.function.IntFunction
                public final Object apply(int i) {
                    return Integer.toString(i);
                }
            }).collect(Collectors.joining(","));
            tids.append(",").append(hwuiTasks);
        }
        String tidsStr = tids.toString();
        if (boost) {
            Trace.asyncTraceBegin(64L, "UX Boost: " + tidsStr, appPid);
        } else {
            Trace.asyncTraceEnd(64L, "UX Boost: " + tidsStr, appPid);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ boolean lambda$uxTrace$0(int e) {
        return e > 0;
    }

    @Override // com.oplus.uifirst.IOplusUIFirstManager
    public boolean checkUxRemoteAnimationList(String packageName) {
        return packageName != null && "com.android.launcher".equals(packageName);
    }

    boolean checkIsListPickPkg(String packageName) {
        return packageName != null && packageName.equals(SystemProperties.get("ro.oplus.system.camera.name", ""));
    }

    @Override // com.oplus.uifirst.IOplusUIFirstManager
    public void setTaskAsRemoteAnimationUx(int appPid, int renderThreadTid, IntArray hwuiTasks, String packageName, boolean isRemoteAnimation) {
    }

    @Override // com.oplus.uifirst.IOplusUIFirstManager
    public synchronized int[] adjustUxProcess(String packageName, int status, int appPid, int renderThreadTid, IntArray hwuiTasks, Collection<Integer> glThreads, boolean isRemoteAnimation) {
        String str;
        if ("com.android.systemui".equals(packageName)) {
            if (status == 5) {
                AppInfo appInfo = this.mOnTopSystemUi;
                if (appInfo == null) {
                    this.mOnTopSystemUi = new AppInfo(packageName, appPid, renderThreadTid, hwuiTasks);
                } else {
                    appInfo.mAppPid = appPid;
                    this.mOnTopSystemUi.mRenderThreadTid = renderThreadTid;
                    this.mOnTopSystemUi.mHwuiTasks = hwuiTasks;
                }
            } else {
                AppInfo appInfo2 = this.mOnTopSystemUi;
                if (appInfo2 != null) {
                    appInfo2.mAppPid = 0;
                }
            }
            updateTopApp();
        } else if ("com.android.launcher".equals(packageName)) {
            if (status == 5) {
                AppInfo appInfo3 = this.mOnTopLauncher;
                if (appInfo3 == null) {
                    this.mOnTopLauncher = new AppInfo(packageName, appPid, renderThreadTid, hwuiTasks);
                } else {
                    appInfo3.mAppPid = appPid;
                    this.mOnTopLauncher.mRenderThreadTid = renderThreadTid;
                    this.mOnTopLauncher.mHwuiTasks = hwuiTasks;
                }
            } else {
                AppInfo appInfo4 = this.mOnTopLauncher;
                if (appInfo4 != null) {
                    appInfo4.mAppPid = 0;
                }
            }
            updateTopApp();
        } else if ("com.coloros.assistantscreen".equals(packageName)) {
            if (status == 5) {
                AppInfo appInfo5 = this.mAssistantScreen;
                if (appInfo5 == null) {
                    this.mAssistantScreen = new AppInfo(packageName, appPid, renderThreadTid, hwuiTasks);
                } else {
                    appInfo5.mAppPid = appPid;
                    this.mAssistantScreen.mRenderThreadTid = renderThreadTid;
                    this.mAssistantScreen.mHwuiTasks = hwuiTasks;
                }
            } else {
                AppInfo appInfo6 = this.mAssistantScreen;
                if (appInfo6 != null) {
                    appInfo6.mAppPid = 0;
                }
            }
            updateTopApp();
        } else if (packageName != null && (str = this.mRealInputMethodPkg) != null && (packageName.equals(str) || packageName.contains(PKG_BAIDUIMS))) {
            if (status == 5) {
                this.mInputMethod.mAppPid = appPid;
                this.mInputMethod.mRenderThreadTid = renderThreadTid;
                this.mInputMethod.mHwuiTasks = hwuiTasks;
            } else {
                AppInfo appInfo7 = this.mInputMethod;
                if (appInfo7 != null) {
                    appInfo7.mAppPid = 0;
                }
            }
            updateTopApp();
        }
        int[] ret = null;
        if (inFilter(DynamicFilterManager.FILTER_UI_FIRST_BLACK, packageName)) {
            return null;
        }
        boolean enableGlThreadUx = inFilter(DynamicFilterManager.FILTER_GL_THREAD_UX, packageName);
        if (Utils.DEBUG) {
            Log.d("OplusUIFirst", "adjustUxProcess: " + packageName + ", pid: " + appPid + ", glthread: " + enableGlThreadUx + ", status: " + status);
        }
        boolean isSystemui = "com.android.systemui".equals(packageName);
        boolean isLauncher = "com.android.launcher".equals(packageName);
        "com.coloros.assistantscreen".equals(packageName);
        switch (status) {
            case 1:
                if (isLauncher) {
                    break;
                } else {
                    nativeSetThreadUx(appPid, appPid, 642);
                    nativeSetThreadUx(appPid, renderThreadTid, 642);
                    for (int i = 0; i < hwuiTasks.size(); i++) {
                        int thread = hwuiTasks.get(i);
                        nativeSetThreadUx(appPid, thread, 641);
                    }
                    this.mGlThreadManager.updateUxForPkg(packageName, appPid, 642);
                    int[] iArr = new int[2];
                    iArr[0] = 642;
                    iArr[1] = enableGlThreadUx ? 642 : 0;
                    ret = iArr;
                    break;
                }
            case 2:
                if (isSystemui) {
                    String sceneValue = String.valueOf(16);
                    PerformanceManager.setSchedAssistScene(sceneValue);
                }
                if (!isLauncher) {
                    nativeSetThreadUx(appPid, appPid, 0);
                    nativeSetThreadUx(appPid, renderThreadTid, 0);
                    for (int i2 = 0; i2 < hwuiTasks.size(); i2++) {
                        int thread2 = hwuiTasks.get(i2);
                        nativeSetThreadUx(appPid, thread2, 0);
                    }
                    if (enableGlThreadUx) {
                        this.mGlThreadManager.moveToBack(packageName, appPid);
                    }
                }
                this.mFBThreadManager.moveToBack(packageName, appPid);
                ret = new int[]{0, 0};
                uxTrace(appPid, renderThreadTid, hwuiTasks.toArray(), false);
                break;
            case 5:
                nativeSetThreadUx(appPid, appPid, 167772802);
                nativeSetThreadUx(appPid, renderThreadTid, 167772802);
                if (isSystemui) {
                    String sceneValue2 = String.valueOf(144);
                    PerformanceManager.setSchedAssistScene(sceneValue2);
                }
                for (int i3 = 0; i3 < hwuiTasks.size(); i3++) {
                    int thread3 = hwuiTasks.get(i3);
                    nativeSetThreadUx(appPid, thread3, 167772801);
                }
                this.mGlThreadManager.updateUxForPkg(packageName, appPid, 167772802);
                if (enableGlThreadUx) {
                    this.mGlThreadManager.moveToFore(packageName, appPid);
                }
                this.mFBThreadManager.moveToFore(packageName, appPid);
                this.mTpdManager.moveToFore(packageName, appPid);
                int[] iArr2 = new int[2];
                iArr2[0] = 167772802;
                iArr2[1] = enableGlThreadUx ? 167772802 : 0;
                ret = iArr2;
                uxTrace(appPid, renderThreadTid, hwuiTasks.toArray(), true);
                break;
        }
        return ret;
    }

    @Override // com.oplus.uifirst.IOplusUIFirstManager
    public void addApplicationGlThread(String packageName, int pid, int tid) {
        this.mGlThreadManager.addGlThread(packageName, pid, tid);
        this.mFBThreadManager.addGlThread(packageName, pid, tid);
    }

    @Override // com.oplus.uifirst.IOplusUIFirstManager
    public void removeApplicationGlThread(String packageName, int pid, int tid) {
        this.mGlThreadManager.removeGlThread(packageName, pid, tid);
        this.mFBThreadManager.removeGlThread(packageName, pid, tid);
    }

    @Override // com.oplus.uifirst.IOplusUIFirstManager
    public int getApplicationGlThreadValue(String packageName) {
        return 0;
    }

    @Override // com.oplus.uifirst.IOplusUIFirstManager
    public void addApplicationHwuiTaskThread(String packageName, int pid, int tid) {
        this.mGlThreadManager.addHwuiTaskThread(packageName, pid, tid);
    }

    @Override // com.oplus.uifirst.IOplusUIFirstManager
    public void setRenderThreadTid(String packageName, int pid, int tid) {
        this.mGlThreadManager.setRenderThreadTid(packageName, pid, tid);
    }

    @Override // com.oplus.uifirst.IOplusUIFirstManager
    public void onAppStatusChanged(int status, String packageName, String activityName) {
        if (Utils.DEBUG) {
            Log.d("OplusUIFirst", "onAppStatusChanged: " + status + " for " + packageName + "/" + activityName);
        }
        this.mGlThreadManager.handleActivityEvent(status, packageName, activityName);
        this.mFBThreadManager.handleActivityEvent(status, packageName, activityName);
        this.mTpdManager.handleActivityEvent(status, packageName, activityName);
    }

    @Override // com.oplus.uifirst.IOplusUIFirstManager
    public void handleProcessStart(String packageName, int uid, int pid, boolean isolated, String processName) {
        boolean isInputMethod;
        String str = this.mRealInputMethodPkg;
        if (str != null && packageName.equals(str)) {
            isInputMethod = true;
        } else {
            isInputMethod = false;
        }
        boolean isInputMethod2 = Utils.DEBUG;
        if (isInputMethod2) {
            Log.d("OplusUIFirst", "handleProcessStart: " + packageName + " " + uid + " " + pid + " " + isolated + " " + processName + " " + isInputMethod);
        }
        this.mGlThreadManager.handleProcessStart(packageName, uid, pid, isolated, processName);
        this.mFBThreadManager.handleProcessStart(isInputMethod ? PKG_DUMMY_INPUT_METHOD : packageName, uid, pid, isolated, processName);
        this.mTpdManager.handleProcessStart(packageName, uid, pid, isolated, processName);
    }

    @Override // com.oplus.uifirst.IOplusUIFirstManager
    public void setfpsgoparamforperformance() {
        if (Utils.DEBUG) {
            Log.d("OplusUIFirst", "set fps go para fo performance");
        }
        writeProcNode(INKERNEL_FBT_CPU_PARAM_BHR_OPP, "15");
    }

    @Override // com.oplus.uifirst.IOplusUIFirstManager
    public void getfpsgoparamforreserve() {
        if (Utils.DEBUG) {
            Log.d("OplusUIFirst", "get now origin fpsgo param");
        }
        this.mbhropp = readProcNode(INKERNEL_FBT_CPU_PARAM_BHR_OPP);
    }

    @Override // com.oplus.uifirst.IOplusUIFirstManager
    public void backfpsgoparam() {
        if (Utils.DEBUG) {
            Log.d("OplusUIFirst", "back to origin fpsgo param");
        }
        writeProcNode(INKERNEL_FBT_CPU_PARAM_BHR_OPP, this.mbhropp);
    }

    @Override // com.oplus.uifirst.IOplusUIFirstManager
    public void enableUXTrace(boolean enable) {
        if (enable) {
            writeProcNode(SCHED_ASSIST_DEBUG, "1");
            writeProcNode(FRAME_BOOST_DEBUG, "1");
            writeProcNode(FRAME_BOOST_DEBUG_LEGACY, "1");
        } else {
            writeProcNode(SCHED_ASSIST_DEBUG, "0");
            writeProcNode(FRAME_BOOST_DEBUG, "0");
            writeProcNode(FRAME_BOOST_DEBUG_LEGACY, "0");
        }
    }

    @Override // com.oplus.uifirst.IOplusUIFirstManager
    public void setUxImFlag(int pid, int flag) {
        String str = "p " + pid + " " + flag;
        writeProcNode(UX_IM_FLAG_PATH, str);
    }

    @Override // com.oplus.uifirst.IOplusUIFirstManager
    public void setBinderThreadUxFlag(int pid, int flag) {
        String str = "p " + pid + " " + flag;
        writeProcNode(BINDER_UX_FLAG_PATH, str);
    }

    @Override // com.oplus.uifirst.IOplusUIFirstManager
    public void setSchedAssistScene(String scene) {
        PerformanceManager.setSchedAssistScene(scene);
    }

    @Override // com.oplus.uifirst.IOplusUIFirstManager
    public synchronized void handleProcessStop(String packageName, int uid, int pid) {
        if (Utils.DEBUG) {
            Log.d("OplusUIFirst", "handleProcessStop: " + packageName + " " + pid);
        }
        boolean isInputMethod = false;
        AppInfo appInfo = this.mOnTopSystemUi;
        if (appInfo != null && appInfo.mAppPid == pid) {
            this.mOnTopSystemUi.mAppPid = 0;
        } else {
            AppInfo appInfo2 = this.mAssistantScreen;
            if (appInfo2 != null && appInfo2.mAppPid == pid) {
                this.mAssistantScreen.mAppPid = 0;
            } else {
                AppInfo appInfo3 = this.mInputMethod;
                if (appInfo3 != null && appInfo3.mAppPid == pid) {
                    this.mInputMethod.mAppPid = 0;
                    isInputMethod = true;
                }
            }
        }
        this.mGlThreadManager.removeAppPid(packageName, pid);
        this.mFBThreadManager.removeAppPid(isInputMethod ? PKG_DUMMY_INPUT_METHOD : packageName, pid);
        this.mTpdManager.removePid(packageName, pid);
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    @Override // com.oplus.uifirst.IOplusUIFirstManager
    public synchronized void notifyUiSwitched(String uiInfo, int status) {
        char c;
        if (Utils.DEBUG) {
            Log.d("OplusUIFirst", "notifyUiSwitched: " + uiInfo + " " + status);
        }
        boolean z = true;
        switch (uiInfo.hashCode()) {
            case -648482826:
                if (uiInfo.equals("com.coloros.assistantscreen")) {
                    c = 1;
                    break;
                }
                c = 65535;
                break;
            case 1698344559:
                if (uiInfo.equals("com.android.systemui")) {
                    c = 0;
                    break;
                }
                c = 65535;
                break;
            default:
                c = 65535;
                break;
        }
        switch (c) {
            case 0:
                if (status <= 0) {
                    z = false;
                }
                this.mSystemUiShowed = z;
                adjustLauncherPrio(z);
                break;
            case 1:
                if (status <= 0) {
                    z = false;
                }
                this.mAssistantScreenShowed = z;
                adjustLauncherPrio(z);
                break;
            default:
                if (uiInfo.startsWith(PKG_KEY_IMS)) {
                    if (status <= 0) {
                        z = false;
                    }
                    this.mInputMethodShowed = z;
                    String str = this.mRealInputMethodPkg;
                    if (str == null || uiInfo.indexOf(str) < 0) {
                        this.mRealInputMethodPkg = uiInfo.substring(PKG_KEY_IMS.length());
                        break;
                    }
                }
                break;
        }
        updateTopApp();
    }

    @Override // com.oplus.uifirst.IOplusUIFirstManager
    public synchronized void notifyRomUpdate() {
        if (Utils.DEBUG) {
            Log.d("OplusUIFirst", "notifyRomUpdate start");
        }
        this.mUagManager.updateConfig();
        updateLoadBalanceConfig();
    }

    /* loaded from: classes.dex */
    private class UIFirstHandler extends Handler {
        public UIFirstHandler(Looper looper) {
            super(looper);
        }

        @Override // android.os.Handler
        public void handleMessage(Message msg) {
            try {
                switch (msg.what) {
                    case 0:
                        return;
                    case 1:
                        if (Utils.DEBUG) {
                            Log.d("OplusUIFirst", "disableLaunchBoost");
                        }
                        PerformanceManager.setSchedAssistScene(String.valueOf(1));
                        return;
                    case 2:
                        return;
                    case 3:
                        return;
                    case 4:
                        Pair<String, Utils.ThreadOp> pair = (Pair) msg.obj;
                        String packageName = (String) pair.first;
                        Utils.ThreadOp op = (Utils.ThreadOp) pair.second;
                        int pid = msg.arg1;
                        OplusUIFirstManager.this.mGlThreadManager.handleGlThreadOp(packageName, pid, op);
                        return;
                    case 5:
                        Pair<String, Utils.ThreadOp> pair2 = (Pair) msg.obj;
                        String packageName2 = (String) pair2.first;
                        Utils.ThreadOp op2 = (Utils.ThreadOp) pair2.second;
                        OplusUIFirstManager.this.mGlThreadManager.handleGlThreadOp(packageName2, op2);
                        return;
                    case 6:
                        Pair<String, Utils.ThreadOp> pair3 = (Pair) msg.obj;
                        String packageName3 = (String) pair3.first;
                        Utils.ThreadOp op3 = (Utils.ThreadOp) pair3.second;
                        int pid2 = msg.arg1;
                        OplusUIFirstManager.this.mTpdManager.handleThreadOp(packageName3, pid2, op3);
                        return;
                    case 7:
                        Pair<String, Utils.ThreadOp> pair4 = (Pair) msg.obj;
                        String packageName4 = (String) pair4.first;
                        Utils.ThreadOp op4 = (Utils.ThreadOp) pair4.second;
                        OplusUIFirstManager.this.mTpdManager.handleThreadOp(packageName4, op4);
                        return;
                    case 8:
                        Pair<String, Utils.ThreadOp> pair5 = (Pair) msg.obj;
                        String packageName5 = (String) pair5.first;
                        Utils.ThreadOp op5 = (Utils.ThreadOp) pair5.second;
                        int pid3 = msg.arg1;
                        OplusUIFirstManager.this.mFBThreadManager.handleFbThreadOp(packageName5, pid3, op5);
                        return;
                    case 9:
                        Pair<String, Utils.ThreadOp> pair6 = (Pair) msg.obj;
                        String packageName6 = (String) pair6.first;
                        Utils.ThreadOp op6 = (Utils.ThreadOp) pair6.second;
                        OplusUIFirstManager.this.mFBThreadManager.handleFbThreadOp(packageName6, op6);
                        return;
                    default:
                        return;
                }
            } catch (Exception e) {
                Log.e("OplusUIFirst", "exception in handle msg: " + e);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public static class AppInfo {
        int mAppPid;
        IntArray mHwuiTasks;
        boolean mInputMethodRtg;
        String mPackageName;
        int mRenderThreadTid;

        AppInfo(String packageName, int appPid, int renderThreadTid, IntArray hwuiTasks) {
            this(packageName, appPid, renderThreadTid, hwuiTasks, false);
        }

        AppInfo(String packageName, int appPid, int renderThreadTid, IntArray hwuiTasks, boolean inputMethodRtg) {
            this.mPackageName = packageName;
            this.mAppPid = appPid;
            this.mRenderThreadTid = renderThreadTid;
            this.mHwuiTasks = hwuiTasks;
            this.mInputMethodRtg = inputMethodRtg;
        }
    }

    @Override // com.oplus.uifirst.IOplusUIFirstManager
    public synchronized void adjustTopApp(String packageName, int appPid, int renderThreadTid, IntArray hwuiTasks) {
        if (Utils.DEBUG) {
            Log.d("OplusUIFirst", "adjustTopApp:" + packageName + " " + appPid + " " + renderThreadTid);
        }
        if ("com.android.permissioncontroller".equals(packageName)) {
            return;
        }
        AppInfo appInfo = this.mOnTopApp;
        if (appInfo == null) {
            this.mOnTopApp = new AppInfo(packageName, appPid, renderThreadTid, hwuiTasks);
        } else {
            appInfo.mPackageName = packageName;
            this.mOnTopApp.mAppPid = appPid;
            this.mOnTopApp.mRenderThreadTid = renderThreadTid;
            this.mOnTopApp.mHwuiTasks = hwuiTasks;
        }
        updateTopApp();
    }

    private void adjustLauncherPrio(boolean down) {
        int value = 640 + (down ? UIFIRST_PRIORITY_VIS_APP : UIFIRST_PRIORITY_TOP_APP);
        AppInfo appInfo = this.mOnTopLauncher;
        if (appInfo == null || appInfo.mAppPid <= 0) {
            return;
        }
        nativeSetThreadUx(this.mOnTopLauncher.mAppPid, this.mOnTopLauncher.mAppPid, value + 2);
        nativeSetThreadUx(this.mOnTopLauncher.mAppPid, this.mOnTopLauncher.mRenderThreadTid, value + 2);
        for (int i = 0; i < this.mOnTopLauncher.mHwuiTasks.size(); i++) {
            int thread = this.mOnTopLauncher.mHwuiTasks.get(i);
            nativeSetThreadUx(this.mOnTopLauncher.mAppPid, thread, value + 1);
        }
    }

    private void updateTopApp() {
        AppInfo info;
        AppInfo appInfo = this.mOnTopSystemUi;
        if (appInfo != null && appInfo.mAppPid != 0 && this.mSystemUiShowed) {
            info = this.mOnTopSystemUi;
        } else {
            AppInfo appInfo2 = this.mInputMethod;
            if (appInfo2 != null && appInfo2.mAppPid != 0 && this.mInputMethodShowed) {
                info = this.mInputMethod;
            } else {
                AppInfo appInfo3 = this.mAssistantScreen;
                if (appInfo3 != null && appInfo3.mAppPid != 0 && this.mAssistantScreenShowed) {
                    info = this.mAssistantScreen;
                } else {
                    info = this.mOnTopApp;
                }
            }
        }
        if (info != null) {
            AppInfo appInfo4 = this.mOnTopAppLast;
            if (appInfo4 == null) {
                this.mOnTopAppLast = new AppInfo(info.mPackageName, info.mAppPid, info.mRenderThreadTid, info.mHwuiTasks);
            } else {
                if (appInfo4.mAppPid == info.mAppPid && this.mOnTopAppLast.mRenderThreadTid == info.mRenderThreadTid && this.mOnTopAppLast.mHwuiTasks.size() == info.mHwuiTasks.size()) {
                    if (Utils.DEBUG) {
                        Log.d("OplusUIFirst", "updateTopApp curr freground app ingore:" + info.mPackageName + " " + info.mAppPid + " " + info.mRenderThreadTid);
                        return;
                    }
                    return;
                }
                this.mOnTopAppLast.mPackageName = info.mPackageName;
                this.mOnTopAppLast.mAppPid = info.mAppPid;
                this.mOnTopAppLast.mRenderThreadTid = info.mRenderThreadTid;
                this.mOnTopAppLast.mHwuiTasks = info.mHwuiTasks;
            }
            this.mFBThreadManager.moveToTop(info);
        }
    }

    @Override // com.oplus.uifirst.IOplusUIFirstManager
    public void setImFlag(int tid, int im) {
        nativeSetImFlag(tid, im);
    }

    @Override // com.oplus.uifirst.IOplusUIFirstManager
    public void ofbSetFps(int pid, int tid, long fpsNs, long vsyncNs) {
        nativeOfbSetFps(pid, tid, fpsNs, vsyncNs);
    }

    @Override // com.oplus.uifirst.IOplusUIFirstManager
    public void ofbBoostHint(int pid, int tid, int hwtid1, int hwtid2, int stage, int level, long fnumber, long sourceDelta, long targetDelta) {
        nativeOfbBoostHint(pid, tid, hwtid1, hwtid2, stage, level, fnumber, sourceDelta, targetDelta);
    }

    @Override // com.oplus.uifirst.IOplusUIFirstManager
    public void ofbEndFrame(int pid, int tid, long fnumber) {
        nativeOfbEndFrame(pid, tid, fnumber);
    }

    @Override // com.oplus.uifirst.IOplusUIFirstManager
    public void setProcessUxValue(int pid, int value) {
        nativeSetProcessUx(pid, value);
    }

    public void ofbCfgUxTask(int pid, int tid, int value, int depth, int width) {
        nativeOfbCfgUxTask(pid, tid, value, depth, width);
    }

    void updateLoadBalanceConfig() {
        String cfg = getFilterConfig("loadbalance", "lb_enable");
        if (Utils.DEBUG) {
            Log.d("OplusUIFirst", "loadbalance: lb_enable=" + cfg);
        }
        if (cfg != null) {
            Utils.writeProcNode("/proc/oplus_scheduler/sched_assist/lb_enable", cfg);
        }
    }
}
