package com.android.server.wm;

import android.R;
import android.app.ActivityManagerInternal;
import android.app.ActivityOptions;
import android.app.AppOpsManager;
import android.app.AppOpsManagerInternal;
import android.app.BackgroundStartPrivileges;
import android.app.IActivityClientController;
import android.app.ProfilerInfo;
import android.app.ResultInfo;
import android.app.TaskInfo;
import android.app.WaitResult;
import android.app.servertransaction.ClientTransaction;
import android.app.servertransaction.LaunchActivityItem;
import android.app.servertransaction.PauseActivityItem;
import android.app.servertransaction.ResumeActivityItem;
import android.companion.virtual.VirtualDeviceManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.pm.UserInfo;
import android.content.res.Configuration;
import android.graphics.Rect;
import android.hardware.SensorPrivacyManagerInternal;
import android.os.Binder;
import android.os.Build;
import android.os.Bundle;
import android.os.Debug;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.PowerManager;
import android.os.RemoteException;
import android.os.SystemClock;
import android.os.Trace;
import android.os.UserHandle;
import android.os.UserManager;
import android.os.WorkSource;
import android.util.ArrayMap;
import android.util.MergedConfiguration;
import android.util.Pair;
import android.util.Slog;
import android.util.SparseArray;
import android.util.SparseIntArray;
import android.view.Display;
import android.widget.Toast;
import android.window.RemoteTransition;
import com.android.internal.annotations.GuardedBy;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.content.ReferrerIntent;
import com.android.internal.protolog.ProtoLogGroup;
import com.android.internal.protolog.ProtoLogImpl;
import com.android.internal.util.ArrayUtils;
import com.android.internal.util.FrameworkStatsLog;
import com.android.internal.util.function.QuadConsumer;
import com.android.internal.util.function.pooled.PooledLambda;
import com.android.server.LocalServices;
import com.android.server.UiThread;
import com.android.server.am.UserState;
import com.android.server.pm.PackageManagerServiceUtils;
import com.android.server.utils.Slogf;
import com.android.server.wm.ActivityMetricsLogger;
import com.android.server.wm.ActivityRecord;
import com.android.server.wm.RecentTasks;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Predicate;
import system.ext.loader.core.ExtLoader;
import vendor.oplus.hardware.charger.ChargerErrorCode;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
public class ActivityTaskSupervisor implements RecentTasks.Callbacks {
    private static final ArrayMap<String, String> ACTION_TO_RUNTIME_PERMISSION;
    private static final int ACTIVITY_RESTRICTION_APPOP = 2;
    private static final int ACTIVITY_RESTRICTION_NONE = 0;
    private static final int ACTIVITY_RESTRICTION_PERMISSION = 1;
    static final boolean DEFER_RESUME = true;
    private static final int IDLE_NOW_MSG = 201;
    private static final int IDLE_TIMEOUT;
    private static final int IDLE_TIMEOUT_MSG = 200;
    private static final int KILL_TASK_PROCESSES_TIMEOUT_MS = 1000;
    private static final int KILL_TASK_PROCESSES_TIMEOUT_MSG = 206;
    private static final int LAUNCH_TASK_BEHIND_COMPLETE = 212;
    private static final int LAUNCH_TIMEOUT;
    private static final int LAUNCH_TIMEOUT_MSG = 204;
    private static final int MAX_TASK_IDS_PER_USER = 100000;
    static final boolean ON_TOP = true;
    static final boolean PRESERVE_WINDOWS = true;
    private static final int PROCESS_STOPPING_AND_FINISHING_MSG = 205;
    static final boolean REMOVE_FROM_RECENTS = true;
    private static final int REPORT_MULTI_WINDOW_MODE_CHANGED_MSG = 214;
    private static final int REPORT_PIP_MODE_CHANGED_MSG = 215;
    private static final int RESTART_ACTIVITY_PROCESS_TIMEOUT_MSG = 213;
    private static final int RESUME_TOP_ACTIVITY_MSG = 202;
    private static final int SCHEDULE_FINISHING_STOPPING_ACTIVITY_MS = 200;
    private static final int SLEEP_TIMEOUT;
    private static final int SLEEP_TIMEOUT_MSG = 203;
    private static final int START_HOME_MSG = 216;
    private static final int TOP_RESUMED_STATE_LOSS_TIMEOUT = 500;
    private static final int TOP_RESUMED_STATE_LOSS_TIMEOUT_MSG = 217;
    private static final boolean VALIDATE_WAKE_LOCK_CALLER = false;
    private ActivityMetricsLogger mActivityMetricsLogger;
    private AppOpsManager mAppOpsManager;
    boolean mAppVisibilitiesChangedSinceLastPause;
    private int mDeferResumeCount;
    private boolean mDeferRootVisibilityUpdate;
    PowerManager.WakeLock mGoingToSleepWakeLock;
    private final ActivityTaskSupervisorHandler mHandler;
    private boolean mInitialized;
    private KeyguardController mKeyguardController;
    private LaunchParamsController mLaunchParamsController;
    LaunchParamsPersister mLaunchParamsPersister;
    PowerManager.WakeLock mLaunchingActivityWakeLock;
    final Looper mLooper;
    PersisterQueue mPersisterQueue;
    private Rect mPipModeChangedTargetRootTaskBounds;
    private PowerManager mPowerManager;
    RecentTasks mRecentTasks;
    public RootWindowContainer mRootWindowContainer;
    private RunningTasks mRunningTasks;
    final ActivityTaskManagerService mService;
    private ComponentName mSystemChooserActivity;
    private ActivityRecord mTopResumedActivity;
    private boolean mTopResumedActivityWaitingForPrev;
    private VirtualDeviceManager mVirtualDeviceManager;
    private int mVisibilityTransactionDepth;
    private WindowManagerService mWindowManager;
    private static final String TAG = "ActivityTaskManager";
    private static final String TAG_IDLE = TAG + ActivityTaskManagerDebugConfig.POSTFIX_IDLE;
    private static final String TAG_PAUSE = TAG + ActivityTaskManagerDebugConfig.POSTFIX_PAUSE;
    private static final String TAG_RECENTS = TAG + ActivityTaskManagerDebugConfig.POSTFIX_RECENTS;
    private static final String TAG_ROOT_TASK = TAG + ActivityTaskManagerDebugConfig.POSTFIX_ROOT_TASK;
    private static final String TAG_SWITCH = TAG + ActivityTaskManagerDebugConfig.POSTFIX_SWITCH;
    static final String TAG_TASKS = TAG + ActivityTaskManagerDebugConfig.POSTFIX_TASKS;
    public static boolean mPerfSendTapHint = false;
    public static boolean mIsPerfBoostAcquired = false;
    public static int mPerfHandle = -1;
    final TaskInfoHelper mTaskInfoHelper = new TaskInfoHelper();
    final OpaqueActivityHelper mOpaqueActivityHelper = new OpaqueActivityHelper();
    private final ArrayList<WindowProcessController> mActivityStateChangedProcs = new ArrayList<>();
    private final SparseIntArray mCurTaskIdForUser = new SparseIntArray(20);
    private final ArrayList<WaitInfo> mWaitingActivityLaunched = new ArrayList<>();
    final ArrayList<ActivityRecord> mStoppingActivities = new ArrayList<>();
    final ArrayList<ActivityRecord> mFinishingActivities = new ArrayList<>();
    final ArrayList<ActivityRecord> mNoHistoryActivities = new ArrayList<>();
    private final ArrayList<ActivityRecord> mMultiWindowModeChangedActivities = new ArrayList<>();
    private final ArrayList<ActivityRecord> mPipModeChangedActivities = new ArrayList<>();
    final ArrayList<ActivityRecord> mNoAnimActivities = new ArrayList<>();
    final ArrayList<UserState> mStartingUsers = new ArrayList<>();
    boolean mUserLeaving = false;
    private ActivityTaskSupervisorWrapper mATSWrapper = new ActivityTaskSupervisorWrapper();
    private IActivityTaskSupervisorExt mActivityTaskSupervisorExt = (IActivityTaskSupervisorExt) ExtLoader.type(IActivityTaskSupervisorExt.class).base(this).create();
    private IActivityTaskSupervisorSocExt mSocExt = (IActivityTaskSupervisorSocExt) ExtLoader.type(IActivityTaskSupervisorSocExt.class).base(this).create();

    private static int nextTaskIdForUser(int i, int i2) {
        int i3 = i + 1;
        return i3 == (i2 + 1) * MAX_TASK_IDS_PER_USER ? i3 - MAX_TASK_IDS_PER_USER : i3;
    }

    static {
        int i = Build.HW_TIMEOUT_MULTIPLIER;
        IDLE_TIMEOUT = i * 10000;
        SLEEP_TIMEOUT = i * 5000;
        LAUNCH_TIMEOUT = i * 10000;
        ArrayMap<String, String> arrayMap = new ArrayMap<>();
        ACTION_TO_RUNTIME_PERMISSION = arrayMap;
        arrayMap.put("android.media.action.IMAGE_CAPTURE", "android.permission.CAMERA");
        arrayMap.put("android.media.action.VIDEO_CAPTURE", "android.permission.CAMERA");
        arrayMap.put("android.intent.action.CALL", "android.permission.CALL_PHONE");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean canPlaceEntityOnDisplay(int i, int i2, int i3, ActivityInfo activityInfo) {
        return canPlaceEntityOnDisplay(i, i2, i3, null, activityInfo);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean canPlaceEntityOnDisplay(int i, int i2, int i3, Task task) {
        return canPlaceEntityOnDisplay(i, i2, i3, task, null);
    }

    private boolean canPlaceEntityOnDisplay(int i, int i2, int i3, Task task, ActivityInfo activityInfo) {
        if (i == 0) {
            return true;
        }
        if (!this.mService.mSupportsMultiDisplay || !isCallerAllowedToLaunchOnDisplay(i2, i3, i, activityInfo)) {
            return false;
        }
        DisplayContent displayContentOrCreate = this.mRootWindowContainer.getDisplayContentOrCreate(i);
        if (displayContentOrCreate == null) {
            return true;
        }
        final ArrayList arrayList = new ArrayList();
        if (activityInfo != null) {
            arrayList.add(activityInfo);
        }
        if (task != null) {
            task.forAllActivities(new Consumer() { // from class: com.android.server.wm.ActivityTaskSupervisor$$ExternalSyntheticLambda2
                @Override // java.util.function.Consumer
                public final void accept(Object obj) {
                    ActivityTaskSupervisor.lambda$canPlaceEntityOnDisplay$0(arrayList, (ActivityRecord) obj);
                }
            });
        }
        return displayContentOrCreate.mDwpcHelper.canContainActivities(arrayList, displayContentOrCreate.getWindowingMode());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$canPlaceEntityOnDisplay$0(ArrayList arrayList, ActivityRecord activityRecord) {
        arrayList.add(activityRecord.info);
    }

    public ActivityTaskSupervisor(ActivityTaskManagerService activityTaskManagerService, Looper looper) {
        this.mService = activityTaskManagerService;
        this.mLooper = looper;
        this.mHandler = new ActivityTaskSupervisorHandler(looper);
    }

    public void initialize() {
        if (this.mInitialized) {
            return;
        }
        this.mInitialized = true;
        setRunningTasks(new RunningTasks());
        this.mActivityMetricsLogger = new ActivityMetricsLogger(this, this.mHandler.getLooper());
        this.mKeyguardController = new KeyguardController(this.mService, this);
        PersisterQueue persisterQueue = new PersisterQueue();
        this.mPersisterQueue = persisterQueue;
        LaunchParamsPersister launchParamsPersister = new LaunchParamsPersister(persisterQueue, this);
        this.mLaunchParamsPersister = launchParamsPersister;
        LaunchParamsController launchParamsController = new LaunchParamsController(this.mService, launchParamsPersister);
        this.mLaunchParamsController = launchParamsController;
        launchParamsController.registerDefaultModifiers(this);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void onSystemReady() {
        this.mLaunchParamsPersister.onSystemReady();
    }

    public void notifyServiceTracker(ActivityRecord.State state, boolean z, ActivityRecord activityRecord, long j) {
        this.mSocExt.notifyServiceTracker(state, z, activityRecord, j);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void onUserUnlocked(int i) {
        this.mPersisterQueue.startPersisting();
        this.mLaunchParamsPersister.onUnlockUser(i);
        scheduleStartHome("userUnlocked");
    }

    public ActivityMetricsLogger getActivityMetricsLogger() {
        return this.mActivityMetricsLogger;
    }

    public KeyguardController getKeyguardController() {
        return this.mKeyguardController;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public ComponentName getSystemChooserActivity() {
        if (this.mSystemChooserActivity == null) {
            this.mSystemChooserActivity = ComponentName.unflattenFromString(this.mService.mContext.getResources().getString(R.string.config_dreamsDefaultComponent));
        }
        return this.mSystemChooserActivity;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setRecentTasks(RecentTasks recentTasks) {
        RecentTasks recentTasks2 = this.mRecentTasks;
        if (recentTasks2 != null) {
            recentTasks2.unregisterCallback(this);
        }
        this.mRecentTasks = recentTasks;
        recentTasks.registerCallback(this);
    }

    @VisibleForTesting
    void setRunningTasks(RunningTasks runningTasks) {
        this.mRunningTasks = runningTasks;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public RunningTasks getRunningTasks() {
        return this.mRunningTasks;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void initPowerManagement() {
        PowerManager powerManager = (PowerManager) this.mService.mContext.getSystemService(PowerManager.class);
        this.mPowerManager = powerManager;
        this.mGoingToSleepWakeLock = powerManager.newWakeLock(1, "ActivityManager-Sleep");
        PowerManager.WakeLock newWakeLock = this.mPowerManager.newWakeLock(1, "*launch*");
        this.mLaunchingActivityWakeLock = newWakeLock;
        newWakeLock.setReferenceCounted(false);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setWindowManager(WindowManagerService windowManagerService) {
        this.mWindowManager = windowManagerService;
        getKeyguardController().setWindowManager(windowManagerService);
    }

    void moveRecentsRootTaskToFront(String str) {
        Task rootTask = this.mRootWindowContainer.getDefaultTaskDisplayArea().getRootTask(0, 3);
        if (rootTask != null) {
            rootTask.moveToFront(str);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setNextTaskIdForUser(int i, int i2) {
        if (i > this.mCurTaskIdForUser.get(i2, -1)) {
            this.mCurTaskIdForUser.put(i2, i);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void finishNoHistoryActivitiesIfNeeded(ActivityRecord activityRecord) {
        for (int size = this.mNoHistoryActivities.size() - 1; size >= 0; size--) {
            ActivityRecord activityRecord2 = this.mNoHistoryActivities.get(size);
            if (!activityRecord2.finishing && activityRecord2 != activityRecord && activityRecord.occludesParent() && activityRecord2.getDisplayId() == activityRecord.getDisplayId()) {
                if (ProtoLogCache.WM_DEBUG_STATES_enabled) {
                    ProtoLogImpl.d(ProtoLogGroup.WM_DEBUG_STATES, -484194149, 0, (String) null, new Object[]{String.valueOf(activityRecord2)});
                }
                activityRecord2.finishIfPossible("resume-no-history", false);
                this.mNoHistoryActivities.remove(activityRecord2);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int getNextTaskIdForUser() {
        return getNextTaskIdForUser(this.mRootWindowContainer.mCurrentUser);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int getNextTaskIdForUser(int i) {
        int i2 = this.mCurTaskIdForUser.get(i, MAX_TASK_IDS_PER_USER * i);
        int nextTaskIdForUser = nextTaskIdForUser(i2, i);
        do {
            if (this.mRecentTasks.containsTaskId(nextTaskIdForUser, i) || this.mRootWindowContainer.anyTaskForId(nextTaskIdForUser, 1) != null) {
                nextTaskIdForUser = nextTaskIdForUser(nextTaskIdForUser, i);
            } else {
                this.mCurTaskIdForUser.put(i, nextTaskIdForUser);
                return nextTaskIdForUser;
            }
        } while (nextTaskIdForUser != i2);
        throw new IllegalStateException("Cannot get an available task id. Reached limit of 100000 running tasks per user.");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void waitActivityVisibleOrLaunched(WaitResult waitResult, ActivityRecord activityRecord, ActivityMetricsLogger.LaunchingState launchingState) {
        int i = waitResult.result;
        if (i == 2 || i == 0) {
            WaitInfo waitInfo = new WaitInfo(waitResult, activityRecord.mActivityComponent, launchingState);
            this.mWaitingActivityLaunched.add(waitInfo);
            do {
                try {
                    this.mService.mGlobalLock.wait();
                } catch (InterruptedException unused) {
                }
            } while (this.mWaitingActivityLaunched.contains(waitInfo));
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void cleanupActivity(ActivityRecord activityRecord) {
        this.mFinishingActivities.remove(activityRecord);
        stopWaitingForActivityVisible(activityRecord);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void stopWaitingForActivityVisible(ActivityRecord activityRecord) {
        reportActivityLaunched(false, activityRecord, -1L, 0);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void reportActivityLaunched(boolean z, ActivityRecord activityRecord, long j, int i) {
        boolean z2 = false;
        for (int size = this.mWaitingActivityLaunched.size() - 1; size >= 0; size--) {
            WaitInfo waitInfo = this.mWaitingActivityLaunched.get(size);
            if (waitInfo.matches(activityRecord)) {
                WaitResult waitResult = waitInfo.mResult;
                waitResult.timeout = z;
                waitResult.who = activityRecord.mActivityComponent;
                waitResult.totalTime = j;
                waitResult.launchState = i;
                this.mWaitingActivityLaunched.remove(size);
                z2 = true;
            }
        }
        if (z2) {
            this.mService.mGlobalLock.notifyAll();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void reportWaitingActivityLaunchedIfNeeded(ActivityRecord activityRecord, int i) {
        if (this.mWaitingActivityLaunched.isEmpty()) {
            return;
        }
        if (i == 3 || i == 2) {
            boolean z = false;
            for (int size = this.mWaitingActivityLaunched.size() - 1; size >= 0; size--) {
                WaitInfo waitInfo = this.mWaitingActivityLaunched.get(size);
                if (waitInfo.matches(activityRecord)) {
                    WaitResult waitResult = waitInfo.mResult;
                    waitResult.result = i;
                    if (i == 3) {
                        waitResult.who = activityRecord.mActivityComponent;
                        this.mWaitingActivityLaunched.remove(size);
                        z = true;
                    }
                }
            }
            if (z) {
                this.mService.mGlobalLock.notifyAll();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public ActivityInfo resolveActivity(Intent intent, ResolveInfo resolveInfo, final int i, final ProfilerInfo profilerInfo) {
        final ActivityInfo activityInfo = resolveInfo != null ? resolveInfo.activityInfo : null;
        if (activityInfo != null) {
            intent.setComponent(new ComponentName(activityInfo.applicationInfo.packageName, activityInfo.name));
            boolean z = (i & 14) != 0;
            boolean z2 = profilerInfo != null;
            if (z || z2) {
                boolean z3 = (Build.IS_DEBUGGABLE || (activityInfo.applicationInfo.flags & 2) != 0) && !activityInfo.processName.equals("system");
                if ((z && !z3) || (z2 && !z3 && !activityInfo.applicationInfo.isProfileableByShell())) {
                    Slog.w(TAG, "Ignore debugging for non-debuggable app: " + activityInfo.packageName);
                } else {
                    WindowManagerGlobalLock windowManagerGlobalLock = this.mService.mGlobalLock;
                    WindowManagerService.boostPriorityForLockedSection();
                    synchronized (windowManagerGlobalLock) {
                        try {
                            this.mService.mH.post(new Runnable() { // from class: com.android.server.wm.ActivityTaskSupervisor$$ExternalSyntheticLambda6
                                @Override // java.lang.Runnable
                                public final void run() {
                                    ActivityTaskSupervisor.this.lambda$resolveActivity$1(activityInfo, i, profilerInfo);
                                }
                            });
                            try {
                                this.mService.mGlobalLock.wait();
                            } catch (InterruptedException unused) {
                            }
                        } catch (Throwable th) {
                            WindowManagerService.resetPriorityAfterLockedSection();
                            throw th;
                        }
                    }
                    WindowManagerService.resetPriorityAfterLockedSection();
                }
            }
            String launchToken = intent.getLaunchToken();
            if (activityInfo.launchToken == null && launchToken != null) {
                activityInfo.launchToken = launchToken;
            }
        }
        return activityInfo;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$resolveActivity$1(ActivityInfo activityInfo, int i, ProfilerInfo profilerInfo) {
        try {
            ActivityTaskManagerService activityTaskManagerService = this.mService;
            activityTaskManagerService.mAmInternal.setDebugFlagsForStartingActivity(activityInfo, i, profilerInfo, activityTaskManagerService.mGlobalLock);
        } finally {
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public ResolveInfo resolveIntent(Intent intent, String str, int i, int i2, int i3, int i4) {
        try {
            Trace.traceBegin(32L, "resolveIntent");
            int i5 = i2 | 65536 | 1024;
            if (intent.isWebIntent() || (intent.getFlags() & 2048) != 0) {
                i5 |= 8388608;
            }
            int i6 = (!intent.isWebIntent() || (intent.getFlags() & 1024) == 0) ? 0 : 1;
            if ((intent.getFlags() & 512) != 0) {
                i6 |= 2;
            }
            this.mActivityTaskSupervisorExt.setOplusCallingUid(intent);
            long clearCallingIdentity = Binder.clearCallingIdentity();
            try {
                return this.mService.getPackageManagerInternalLocked().resolveIntentExported(intent, str, i5, i6, i, true, i3, i4);
            } finally {
                Binder.restoreCallingIdentity(clearCallingIdentity);
            }
        } finally {
            Trace.traceEnd(32L);
        }
    }

    public ActivityInfo resolveActivity(Intent intent, String str, int i, ProfilerInfo profilerInfo, int i2, int i3, int i4) {
        return resolveActivity(intent, resolveIntent(intent, str, i2, 0, i3, i4), i, profilerInfo);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean realStartActivityLocked(ActivityRecord activityRecord, WindowProcessController windowProcessController, boolean z, boolean z2) throws RemoteException {
        ArrayList<ResultInfo> arrayList;
        ArrayList<ReferrerIntent> arrayList2;
        ResumeActivityItem obtain;
        Task task;
        this.mActivityTaskSupervisorExt.handleActivityStart(activityRecord.packageName, activityRecord.processName, activityRecord.info.applicationInfo.uid);
        windowProcessController.getWrapper().getExtImpl().updateWaitActivityToAttach(true);
        if (!this.mRootWindowContainer.allPausedActivitiesComplete()) {
            if (ProtoLogCache.WM_DEBUG_STATES_enabled) {
                ProtoLogImpl.v(ProtoLogGroup.WM_DEBUG_STATES, -641258376, 0, (String) null, new Object[]{String.valueOf(activityRecord)});
            }
            return false;
        }
        Task task2 = activityRecord.getTask();
        if (task2 == null) {
            Slog.w(TAG, "realStartActivityLocked: Skipping start for task is null. r =" + activityRecord);
            return false;
        }
        if (!this.mActivityTaskSupervisorExt.canStartActivity(activityRecord)) {
            Slog.i(TAG, "dont start activity who was finished by compactmode:" + activityRecord);
            return false;
        }
        Task rootTask = task2.getRootTask();
        beginDeferResume();
        windowProcessController.pauseConfigurationDispatch();
        try {
            activityRecord.startFreezingScreenLocked(windowProcessController, 0);
            activityRecord.startLaunchTickingLocked();
            activityRecord.lastLaunchTime = SystemClock.uptimeMillis();
            activityRecord.setProcess(windowProcessController);
            boolean z3 = (!z || activityRecord.canResumeByCompat()) ? z : false;
            activityRecord.notifyUnknownVisibilityLaunchedForKeyguardTransition();
            if (z2) {
                this.mRootWindowContainer.ensureVisibilityAndConfig(activityRecord, activityRecord.getDisplayId(), false, true);
            }
            if (this.mKeyguardController.checkKeyguardVisibility(activityRecord) && activityRecord.allowMoveToFront()) {
                activityRecord.setVisibility(true);
            }
            ApplicationInfo applicationInfo = activityRecord.info.applicationInfo;
            int i = applicationInfo != null ? applicationInfo.uid : -1;
            if (activityRecord.mUserId != windowProcessController.mUserId || applicationInfo.uid != i) {
                Slog.wtf(TAG, "User ID for activity changing for " + activityRecord + " appInfo.uid=" + activityRecord.info.applicationInfo.uid + " info.ai.uid=" + i + " old=" + activityRecord.app + " new=" + windowProcessController);
            }
            IActivityClientController iActivityClientController = windowProcessController.hasEverLaunchedActivity() ? null : this.mService.mActivityClientController;
            activityRecord.launchCount++;
            if (ActivityTaskManagerDebugConfig.DEBUG_ALL) {
                Slog.v(TAG, "Launching: " + activityRecord);
            }
            LockTaskController lockTaskController = this.mService.getLockTaskController();
            int i2 = task2.mLockTaskAuth;
            if (i2 == 2 || i2 == 4 || (i2 == 3 && lockTaskController.getLockTaskModeState() == 1)) {
                lockTaskController.startLockTaskMode(task2, false, 0);
            }
            try {
            } catch (RemoteException e) {
                e = e;
            }
            try {
                if (!windowProcessController.hasThread()) {
                    throw new RemoteException();
                }
                if (z3) {
                    arrayList = activityRecord.results;
                    arrayList2 = activityRecord.newIntents;
                } else {
                    arrayList = null;
                    arrayList2 = null;
                }
                if (ActivityTaskManagerDebugConfig.DEBUG_SWITCH) {
                    Slog.v(TAG_SWITCH, "Launching: " + activityRecord + " savedState=" + activityRecord.getSavedState() + " with results=" + arrayList + " newIntents=" + arrayList2 + " andResume=" + z3);
                }
                EventLogTags.writeWmRestartActivity(activityRecord.mUserId, System.identityHashCode(activityRecord), task2.mTaskId, activityRecord.shortComponentName);
                if (activityRecord.isActivityTypeHome()) {
                    updateHomeProcess(task2.getBottomMostActivity().app);
                }
                this.mService.getPackageManagerInternalLocked().notifyPackageUse(activityRecord.intent.getComponent().getPackageName(), 0);
                activityRecord.forceNewConfig = false;
                this.mService.getAppWarningsLocked().onStartActivity(activityRecord);
                Configuration prepareConfigurationForLaunchingActivity = windowProcessController.prepareConfigurationForLaunchingActivity();
                MergedConfiguration mergedConfiguration = new MergedConfiguration(prepareConfigurationForLaunchingActivity, activityRecord.getMergedOverrideConfiguration());
                activityRecord.setLastReportedConfiguration(mergedConfiguration);
                logIfTransactionTooLarge(activityRecord.intent, activityRecord.getSavedState());
                TaskFragment organizedTaskFragment = activityRecord.getOrganizedTaskFragment();
                if (organizedTaskFragment != null) {
                    this.mService.mTaskFragmentOrganizerController.dispatchPendingInfoChangedEvent(organizedTaskFragment);
                }
                ClientTransaction obtain2 = ClientTransaction.obtain(windowProcessController.getThread(), activityRecord.token);
                boolean isTransitionForward = activityRecord.isTransitionForward();
                if (activityRecord.getTaskFragment() == null) {
                    Slog.e(TAG, "TaskFragment is null so return false!");
                    return false;
                }
                IBinder fragmentToken = activityRecord.getTaskFragment().getFragmentToken();
                this.mActivityTaskSupervisorExt.adjustStartActivityIntentIfNeed(activityRecord);
                this.mActivityTaskSupervisorExt.cameraPreopenIfNeed(this.mService.mContext, activityRecord);
                obtain2.addCallback(LaunchActivityItem.obtain(new Intent(activityRecord.intent), System.identityHashCode(activityRecord), activityRecord.info, mergedConfiguration.getGlobalConfiguration(), mergedConfiguration.getOverrideConfiguration(), getDeviceIdForDisplayId(activityRecord.getDisplayId()), activityRecord.getFilteredReferrer(activityRecord.launchedFromPackage), task2.voiceInteractor, windowProcessController.getReportedProcState(), activityRecord.getSavedState(), activityRecord.getPersistentSavedState(), arrayList, arrayList2, activityRecord.takeOptions(), isTransitionForward, windowProcessController.createProfilerInfoIfNeeded(), activityRecord.assistToken, iActivityClientController, activityRecord.shareableActivityToken, activityRecord.getLaunchedFromBubble(), fragmentToken));
                if (z3) {
                    this.mActivityTaskSupervisorExt.hookRealStartActivityLocked(activityRecord);
                    this.mActivityTaskSupervisorExt.addColorModeOnResume(obtain2, z3, activityRecord.packageName);
                    obtain = ResumeActivityItem.obtain(isTransitionForward, activityRecord.shouldSendCompatFakeFocus());
                } else {
                    obtain = PauseActivityItem.obtain();
                }
                obtain2.setLifecycleStateRequest(obtain);
                this.mService.getLifecycleManager().scheduleTransaction(obtain2);
                if (prepareConfigurationForLaunchingActivity.seq > this.mRootWindowContainer.getConfiguration().seq) {
                    windowProcessController.setLastReportedConfiguration(prepareConfigurationForLaunchingActivity);
                }
                ApplicationInfo applicationInfo2 = windowProcessController.mInfo;
                if ((applicationInfo2.privateFlags & 2) != 0 && this.mService.mHasHeavyWeightFeature && windowProcessController.mName.equals(applicationInfo2.packageName)) {
                    if (this.mService.mHeavyWeightProcess != null && this.mService.mHeavyWeightProcess != windowProcessController) {
                        Slog.w(TAG, "Starting new heavy weight process " + windowProcessController + " when already running " + this.mService.mHeavyWeightProcess);
                    }
                    this.mService.setHeavyWeightProcess(activityRecord);
                }
                endDeferResume();
                windowProcessController.resumeConfigurationDispatch();
                activityRecord.launchFailed = false;
                if (z3 && readyToResume()) {
                    task = rootTask;
                    task.minimalResumeActivityLocked(activityRecord);
                } else {
                    task = rootTask;
                    if (ProtoLogCache.WM_DEBUG_STATES_enabled) {
                        ProtoLogImpl.v(ProtoLogGroup.WM_DEBUG_STATES, -1468740466, 0, (String) null, new Object[]{String.valueOf(activityRecord)});
                    }
                    activityRecord.setState(ActivityRecord.State.PAUSED, "realStartActivityLocked");
                    this.mRootWindowContainer.executeAppTransitionForAllDisplay();
                }
                windowProcessController.onStartActivity(this.mService.mTopProcessState, activityRecord.info);
                if (this.mRootWindowContainer.isTopDisplayFocusedRootTask(task)) {
                    this.mService.getActivityStartController().startSetupActivity();
                }
                try {
                    WindowProcessController windowProcessController2 = activityRecord.app;
                    if (windowProcessController2 != null) {
                        windowProcessController2.updateServiceConnectionActivities();
                    }
                } catch (NullPointerException e2) {
                    Slog.w(TAG, "updateServiceConnectionActivities catch NullPointerException", e2);
                }
                this.mActivityTaskSupervisorExt.hookRecordAppStartCount(activityRecord.info.applicationInfo.uid, activityRecord.packageName, activityRecord.processName);
                return true;
            } catch (RemoteException e3) {
                e = e3;
                if (!activityRecord.launchFailed) {
                    activityRecord.launchFailed = true;
                    activityRecord.detachFromProcess();
                    throw e;
                }
                Slog.e(TAG, "Second failure launching " + activityRecord.intent.getComponent().flattenToShortString() + ", giving up", e);
                windowProcessController.appDied("2nd-crash");
                activityRecord.finishIfPossible("2nd-crash", false);
                return false;
            }
        } finally {
            endDeferResume();
            windowProcessController.resumeConfigurationDispatch();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void updateHomeProcess(WindowProcessController windowProcessController) {
        if (windowProcessController == null || this.mService.mHomeProcess == windowProcessController) {
            return;
        }
        scheduleStartHome("homeChanged");
        this.mService.mHomeProcess = windowProcessController;
    }

    private void scheduleStartHome(String str) {
        if (this.mHandler.hasMessages(START_HOME_MSG)) {
            return;
        }
        this.mHandler.obtainMessage(START_HOME_MSG, str).sendToTarget();
    }

    private void logIfTransactionTooLarge(Intent intent, Bundle bundle) {
        Bundle extras;
        int size = (intent == null || (extras = intent.getExtras()) == null) ? 0 : extras.getSize();
        int size2 = bundle != null ? bundle.getSize() : 0;
        if (size + size2 > 200000) {
            Slog.e(TAG, "Transaction too large, intent: " + intent + ", extras size: " + size + ", icicle size: " + size2);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void startSpecificActivity(ActivityRecord activityRecord, boolean z, boolean z2) {
        boolean z3;
        WindowProcessController processController = this.mService.getProcessController(activityRecord.processName, activityRecord.info.applicationInfo.uid);
        this.mActivityTaskSupervisorExt.setLaunchTimeStart(activityRecord);
        if (processController != null && processController.hasThread()) {
            try {
                this.mSocExt.startSpecificActivityPerfHint(TAG, activityRecord, processController.getPid());
                realStartActivityLocked(activityRecord, processController, z, z2);
                return;
            } catch (RemoteException e) {
                Slog.w(TAG, "Exception when starting activity " + activityRecord.intent.getComponent().flattenToShortString(), e);
                this.mService.mProcessNames.remove(processController.mName, processController.mUid);
                this.mService.mProcessMap.remove(processController.getPid());
                z3 = true;
            }
        } else {
            if (activityRecord.intent.isSandboxActivity(this.mService.mContext)) {
                Slog.e(TAG, "Abort sandbox activity launching as no sandbox process to host it.");
                activityRecord.finishIfPossible("No sandbox process for the activity", false);
                activityRecord.launchFailed = true;
                activityRecord.detachFromProcess();
                return;
            }
            z3 = false;
        }
        this.mActivityTaskSupervisorExt.hookStartSpecificActivity(this.mService.mContext);
        activityRecord.notifyUnknownVisibilityLaunchedForKeyguardTransition();
        this.mActivityTaskSupervisorExt.handleActivityStartBeforeStartProc(activityRecord, z);
        boolean z4 = z && activityRecord.isTopRunningActivity();
        this.mActivityTaskSupervisorExt.modifyApplicaitonInfoForMirageCarMode(activityRecord);
        this.mService.startProcessAsync(activityRecord, z3, z4, z4 ? "top-activity" : "activity");
        this.mActivityTaskSupervisorExt.handleActivityStartAfterStartProc(activityRecord);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean checkStartAnyActivityPermission(Intent intent, ActivityInfo activityInfo, String str, int i, int i2, int i3, String str2, String str3, boolean z, boolean z2, WindowProcessController windowProcessController, ActivityRecord activityRecord, Task task) {
        String str4;
        boolean z3 = this.mService.getRecentTasks() != null && this.mService.getRecentTasks().isCallerRecents(i3);
        if (ActivityTaskManagerService.checkPermission("android.permission.START_ANY_ACTIVITY", i2, i3) == 0 || (z3 && z2)) {
            return true;
        }
        int componentRestrictionForCallingPackage = getComponentRestrictionForCallingPackage(activityInfo, str2, str3, i2, i3, z);
        int actionRestrictionForCallingPackage = getActionRestrictionForCallingPackage(intent.getAction(), str2, str3, i2, i3);
        if (componentRestrictionForCallingPackage != 1 && actionRestrictionForCallingPackage != 1) {
            if (actionRestrictionForCallingPackage == 2) {
                Slog.w(TAG, "Appop Denial: starting " + intent.toString() + " from " + windowProcessController + " (pid=" + i2 + ", uid=" + i3 + ") requires " + AppOpsManager.permissionToOp(ACTION_TO_RUNTIME_PERMISSION.get(intent.getAction())));
                return false;
            }
            if (componentRestrictionForCallingPackage != 2) {
                return true;
            }
            Slog.w(TAG, "Appop Denial: starting " + intent.toString() + " from " + windowProcessController + " (pid=" + i2 + ", uid=" + i3 + ") requires appop " + AppOpsManager.permissionToOp(activityInfo.permission));
            return false;
        }
        if (activityRecord != null) {
            activityRecord.sendResult(-1, str, i, 0, null, null);
        }
        if (actionRestrictionForCallingPackage == 1) {
            str4 = "Permission Denial: starting " + intent.toString() + " from " + windowProcessController + " (pid=" + i2 + ", uid=" + i3 + ") with revoked permission " + ACTION_TO_RUNTIME_PERMISSION.get(intent.getAction());
        } else if (!activityInfo.exported) {
            str4 = "Permission Denial: starting " + intent.toString() + " from " + windowProcessController + " (pid=" + i2 + ", uid=" + i3 + ") not exported from uid " + activityInfo.applicationInfo.uid;
        } else {
            str4 = "Permission Denial: starting " + intent.toString() + " from " + windowProcessController + " (pid=" + i2 + ", uid=" + i3 + ") requires " + activityInfo.permission;
        }
        Slog.w(TAG, str4);
        throw new SecurityException(str4);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean isCallerAllowedToLaunchOnTaskDisplayArea(int i, int i2, TaskDisplayArea taskDisplayArea, ActivityInfo activityInfo) {
        return isCallerAllowedToLaunchOnDisplay(i, i2, taskDisplayArea != null ? taskDisplayArea.getDisplayId() : 0, activityInfo);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean isCallerAllowedToLaunchOnDisplay(int i, int i2, int i3, ActivityInfo activityInfo) {
        if (ProtoLogCache.WM_DEBUG_TASKS_enabled) {
            ProtoLogImpl.d(ProtoLogGroup.WM_DEBUG_TASKS, -1228653755, 21, (String) null, new Object[]{Long.valueOf(i3), Long.valueOf(i), Long.valueOf(i2)});
        }
        if (i == -1 && i2 == -1) {
            if (ProtoLogCache.WM_DEBUG_TASKS_enabled) {
                ProtoLogImpl.d(ProtoLogGroup.WM_DEBUG_TASKS, 1524174282, 0, (String) null, (Object[]) null);
            }
            return true;
        }
        DisplayContent displayContentOrCreate = this.mRootWindowContainer.getDisplayContentOrCreate(i3);
        if (displayContentOrCreate == null || displayContentOrCreate.isRemoved()) {
            Slog.w(TAG, "Launch on display check: display not found");
            return false;
        }
        if ((displayContentOrCreate.mDisplay.getFlags() & 8192) != 0) {
            Slog.w(TAG, "Launch on display check: activity launch is not allowed on rear display");
            return false;
        }
        if (ActivityTaskManagerService.checkPermission("android.permission.INTERNAL_SYSTEM_WINDOW", i, i2) == 0) {
            if (ProtoLogCache.WM_DEBUG_TASKS_enabled) {
                ProtoLogImpl.d(ProtoLogGroup.WM_DEBUG_TASKS, 431715812, 0, (String) null, (Object[]) null);
            }
            return true;
        }
        boolean isUidPresent = displayContentOrCreate.isUidPresent(i2);
        Display display = displayContentOrCreate.mDisplay;
        if (!display.isTrusted()) {
            if ((activityInfo.flags & ChargerErrorCode.ERR_FILE_FAILURE_ACCESS) == 0) {
                if (ProtoLogCache.WM_DEBUG_TASKS_enabled) {
                    ProtoLogImpl.d(ProtoLogGroup.WM_DEBUG_TASKS, -1474602871, 0, (String) null, (Object[]) null);
                }
                if (!this.mActivityTaskSupervisorExt.isPuttDisplay(i3)) {
                    Slog.w(TAG, "Launch on display check:  allow putt task on virtual display");
                    return false;
                }
            }
            if (ActivityTaskManagerService.checkPermission("android.permission.ACTIVITY_EMBEDDING", i, i2) == -1 && !isUidPresent) {
                if (ProtoLogCache.WM_DEBUG_TASKS_enabled) {
                    ProtoLogImpl.d(ProtoLogGroup.WM_DEBUG_TASKS, 979347997, 0, (String) null, (Object[]) null);
                }
                if (!this.mActivityTaskSupervisorExt.isPuttDisplay(i3)) {
                    Slog.w(TAG, "Launch on display check:  allow putt task on virtual display");
                    return false;
                }
            }
        }
        if (!displayContentOrCreate.isPrivate()) {
            int userId = UserHandle.getUserId(i2);
            int displayId = display.getDisplayId();
            boolean isUserVisible = this.mWindowManager.mUmInternal.isUserVisible(userId, displayId);
            if (ProtoLogCache.WM_DEBUG_TASKS_enabled) {
                ProtoLogImpl.d(ProtoLogGroup.WM_DEBUG_TASKS, -1253056469, 20, (String) null, new Object[]{isUserVisible ? "allow" : "disallow", Long.valueOf(userId), Long.valueOf(displayId)});
            }
            return isUserVisible;
        }
        if (display.getOwnerUid() == i2) {
            if (ProtoLogCache.WM_DEBUG_TASKS_enabled) {
                ProtoLogImpl.d(ProtoLogGroup.WM_DEBUG_TASKS, -856750101, 0, (String) null, (Object[]) null);
            }
            return true;
        }
        if (isUidPresent) {
            if (ProtoLogCache.WM_DEBUG_TASKS_enabled) {
                ProtoLogImpl.d(ProtoLogGroup.WM_DEBUG_TASKS, -1979455254, 0, (String) null, (Object[]) null);
            }
            return true;
        }
        Slog.w(TAG, "Launch on display check: denied");
        return false;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public UserInfo getUserInfo(int i) {
        long clearCallingIdentity = Binder.clearCallingIdentity();
        try {
            return UserManager.get(this.mService.mContext).getUserInfo(i);
        } finally {
            Binder.restoreCallingIdentity(clearCallingIdentity);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int getDeviceIdForDisplayId(int i) {
        if (i == 0 || i == -1) {
            return 0;
        }
        if (this.mVirtualDeviceManager == null) {
            ActivityTaskManagerService activityTaskManagerService = this.mService;
            if (activityTaskManagerService.mHasCompanionDeviceSetupFeature) {
                this.mVirtualDeviceManager = (VirtualDeviceManager) activityTaskManagerService.mContext.getSystemService(VirtualDeviceManager.class);
            }
            if (this.mVirtualDeviceManager == null) {
                return 0;
            }
        }
        return this.mVirtualDeviceManager.getDeviceIdForDisplayId(i);
    }

    private AppOpsManager getAppOpsManager() {
        if (this.mAppOpsManager == null) {
            this.mAppOpsManager = (AppOpsManager) this.mService.mContext.getSystemService(AppOpsManager.class);
        }
        return this.mAppOpsManager;
    }

    private int getComponentRestrictionForCallingPackage(ActivityInfo activityInfo, String str, String str2, int i, int i2, boolean z) {
        int permissionToOpCode;
        if (!z && ActivityTaskManagerService.checkComponentPermission(activityInfo.permission, i, i2, activityInfo.applicationInfo.uid, activityInfo.exported) == -1) {
            return 1;
        }
        String str3 = activityInfo.permission;
        return (str3 == null || (permissionToOpCode = AppOpsManager.permissionToOpCode(str3)) == -1 || getAppOpsManager().noteOpNoThrow(permissionToOpCode, i2, str, str2, "") == 0 || z) ? 0 : 2;
    }

    private int getActionRestrictionForCallingPackage(String str, String str2, String str3, int i, int i2) {
        String str4;
        if (str == null || (str4 = ACTION_TO_RUNTIME_PERMISSION.get(str)) == null) {
            return 0;
        }
        try {
            if (!ArrayUtils.contains(this.mService.mContext.getPackageManager().getPackageInfoAsUser(str2, 4096, UserHandle.getUserId(i2)).requestedPermissions, str4)) {
                return 0;
            }
            if (ActivityTaskManagerService.checkPermission(str4, i, i2) == -1) {
                return 1;
            }
            int permissionToOpCode = AppOpsManager.permissionToOpCode(str4);
            if (permissionToOpCode == -1 || getAppOpsManager().noteOpNoThrow(permissionToOpCode, i2, str2, str3, "") == 0) {
                return 0;
            }
            if ("android.permission.CAMERA".equals(str4)) {
                SensorPrivacyManagerInternal sensorPrivacyManagerInternal = (SensorPrivacyManagerInternal) LocalServices.getService(SensorPrivacyManagerInternal.class);
                UserHandle userHandleForUid = UserHandle.getUserHandleForUid(i2);
                if (sensorPrivacyManagerInternal.isSensorPrivacyEnabled(userHandleForUid.getIdentifier(), 2) && ((AppOpsManagerInternal) LocalServices.getService(AppOpsManagerInternal.class)).getOpRestrictionCount(26, userHandleForUid, str2, (String) null) == 1) {
                    return 0;
                }
            }
            return 2;
        } catch (PackageManager.NameNotFoundException unused) {
            Slog.i(TAG, "Cannot find package info for " + str2);
            return 0;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setLaunchSource(int i) {
        this.mLaunchingActivityWakeLock.setWorkSource(new WorkSource(i));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void acquireLaunchWakelock() {
        this.mLaunchingActivityWakeLock.acquire();
        if (this.mHandler.hasMessages(LAUNCH_TIMEOUT_MSG)) {
            return;
        }
        this.mHandler.sendEmptyMessageDelayed(LAUNCH_TIMEOUT_MSG, LAUNCH_TIMEOUT);
    }

    @GuardedBy({"mService"})
    private void checkFinishBootingLocked() {
        boolean isBooting = this.mService.isBooting();
        boolean z = false;
        this.mService.setBooting(false);
        if (!this.mService.isBooted()) {
            z = true;
            this.mService.setBooted(true);
        }
        if (isBooting || z) {
            this.mService.postFinishBooting(isBooting, z);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void activityIdleInternal(ActivityRecord activityRecord, boolean z, boolean z2, Configuration configuration) {
        if (ActivityTaskManagerDebugConfig.DEBUG_ALL || this.mService.isBooting()) {
            Slog.v(TAG, "Activity idle: " + activityRecord + ",fromTimeout=" + z + ",isBooting=" + this.mService.isBooting());
        }
        if (activityRecord != null) {
            if (ActivityTaskManagerDebugConfig.DEBUG_IDLE) {
                Slog.d(TAG_IDLE, "activityIdleInternal: Callers=" + Debug.getCallers(4));
            }
            this.mHandler.removeMessages(200, activityRecord);
            activityRecord.finishLaunchTickingLocked();
            if (z) {
                reportActivityLaunched(z, activityRecord, -1L, -1);
            }
            if (configuration != null) {
                activityRecord.setLastReportedGlobalConfiguration(configuration);
            }
            activityRecord.idle = true;
            this.mActivityTaskSupervisorExt.handleActivityIdle(activityRecord);
            if ((this.mService.isBooting() && this.mRootWindowContainer.allResumedActivitiesIdle()) || z) {
                checkFinishBootingLocked();
            }
            activityRecord.mRelaunchReason = 0;
        }
        if (this.mRootWindowContainer.allResumedActivitiesIdle()) {
            if (activityRecord != null) {
                this.mService.scheduleAppGcsLocked();
                this.mRecentTasks.onActivityIdle(activityRecord);
            }
            if (this.mLaunchingActivityWakeLock.isHeld()) {
                this.mHandler.removeMessages(LAUNCH_TIMEOUT_MSG);
                this.mLaunchingActivityWakeLock.release();
            }
            this.mRootWindowContainer.ensureActivitiesVisible(null, 0, false);
        }
        processStoppingAndFinishingActivities(activityRecord, z2, "idle");
        if (ActivityTaskManagerDebugConfig.DEBUG_IDLE) {
            Slogf.i(TAG, "activityIdleInternal(): r=%s, mStartingUsers=%s", new Object[]{activityRecord, this.mStartingUsers});
        }
        if (!this.mStartingUsers.isEmpty()) {
            ArrayList arrayList = new ArrayList(this.mStartingUsers);
            this.mStartingUsers.clear();
            for (int i = 0; i < arrayList.size(); i++) {
                UserState userState = (UserState) arrayList.get(i);
                Slogf.i(TAG, "finishing switch of user %d", new Object[]{Integer.valueOf(userState.mHandle.getIdentifier())});
                this.mService.mAmInternal.finishUserSwitch(userState);
            }
        }
        this.mService.mH.post(new Runnable() { // from class: com.android.server.wm.ActivityTaskSupervisor$$ExternalSyntheticLambda9
            @Override // java.lang.Runnable
            public final void run() {
                ActivityTaskSupervisor.this.lambda$activityIdleInternal$2();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$activityIdleInternal$2() {
        this.mService.mAmInternal.trimApplications();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: Removed duplicated region for block: B:34:0x00f7 A[Catch: all -> 0x0181, TryCatch #0 {all -> 0x0181, blocks: (B:31:0x00e2, B:32:0x00ea, B:34:0x00f7, B:35:0x00fe, B:37:0x0104, B:39:0x010a, B:67:0x011d), top: B:30:0x00e2 }] */
    /* JADX WARN: Removed duplicated region for block: B:43:0x011b  */
    /* JADX WARN: Removed duplicated region for block: B:48:0x0133 A[Catch: all -> 0x017f, TryCatch #2 {all -> 0x017f, blocks: (B:46:0x012c, B:48:0x0133, B:49:0x0149, B:52:0x015e, B:54:0x0164, B:56:0x016a, B:58:0x016e, B:59:0x0174), top: B:45:0x012c }] */
    /* JADX WARN: Removed duplicated region for block: B:56:0x016a A[Catch: all -> 0x017f, TryCatch #2 {all -> 0x017f, blocks: (B:46:0x012c, B:48:0x0133, B:49:0x0149, B:52:0x015e, B:54:0x0164, B:56:0x016a, B:58:0x016e, B:59:0x0174), top: B:45:0x012c }] */
    /* JADX WARN: Removed duplicated region for block: B:67:0x011d A[Catch: all -> 0x0181, TRY_LEAVE, TryCatch #0 {all -> 0x0181, blocks: (B:31:0x00e2, B:32:0x00ea, B:34:0x00f7, B:35:0x00fe, B:37:0x0104, B:39:0x010a, B:67:0x011d), top: B:30:0x00e2 }] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void findTaskToMoveToFront(Task task, int i, ActivityOptions activityOptions, String str, boolean z) {
        boolean z2;
        Transition transition;
        boolean z3;
        RemoteTransition remoteTransition;
        ActivityRecord topNonFinishingActivity;
        Transition transition2;
        Task rootTask = task.getRootTask();
        Task topDisplayFocusedRootTask = this.mRootWindowContainer.getTopDisplayFocusedRootTask();
        ActivityRecord topNonFinishingActivity2 = topDisplayFocusedRootTask != null ? topDisplayFocusedRootTask.getTopNonFinishingActivity() : null;
        if (topNonFinishingActivity2 != null && topNonFinishingActivity2.isState(ActivityRecord.State.DESTROYED)) {
            this.mSocExt.acquireAppLaunchPerfLock(topNonFinishingActivity2, this.mService);
            this.mActivityTaskSupervisorExt.hookAcquireLaunchBoost();
            this.mActivityTaskSupervisorExt.findTaskToMoveToFront(this.mService.mContext);
        }
        if (rootTask == null) {
            Slog.e(TAG, "findTaskToMoveToFront: can't move task=" + task + " to front. Root task is null");
            return;
        }
        boolean z4 = false;
        if ((i & 2) == 0) {
            try {
                this.mUserLeaving = true;
            } catch (Throwable th) {
                th = th;
                z2 = false;
                this.mUserLeaving = z2;
                this.mService.continueWindowLayout();
                throw th;
            }
        }
        this.mService.deferWindowLayout();
        int modifyTransitionType = this.mATSWrapper.getExtImpl().modifyTransitionType(task, 3);
        try {
            if (task.mTransitionController.isShellTransitionsEnabled() && !task.mAtmService.getWrapper().getExtImpl().withNoneTransition(null, task, activityOptions, modifyTransitionType, "findTaskToMoveToFront") && !task.mTransitionController.isCollecting()) {
                transition = task.mTransitionController.createTransition(modifyTransitionType);
                task.mTransitionController.collect(task);
                String str2 = str + " findTaskToMoveToFront";
                if (task.isResizeable() || !canUseActivityOptionsLaunchBounds(activityOptions)) {
                    z3 = false;
                    remoteTransition = null;
                    z4 = false;
                } else {
                    Task orCreateRootTask = this.mRootWindowContainer.getOrCreateRootTask(null, activityOptions, task, true);
                    if (orCreateRootTask != rootTask) {
                        moveHomeRootTaskToFrontIfNeeded(i, orCreateRootTask.getDisplayArea(), str2);
                        z3 = false;
                        remoteTransition = null;
                        try {
                            task.reparent(orCreateRootTask, true, 1, false, true, str2);
                            rootTask = orCreateRootTask;
                            z4 = true;
                        } catch (Throwable th2) {
                            th = th2;
                            z2 = z3;
                            this.mUserLeaving = z2;
                            this.mService.continueWindowLayout();
                            throw th;
                        }
                    } else {
                        z3 = false;
                        remoteTransition = null;
                    }
                    task.setBounds(activityOptions.getLaunchBounds());
                }
                if (!z4) {
                    moveHomeRootTaskToFrontIfNeeded(i, rootTask.getDisplayArea(), str2);
                }
                topNonFinishingActivity = task.getTopNonFinishingActivity();
                z2 = z3;
                RemoteTransition remoteTransition2 = remoteTransition;
                transition2 = transition;
                rootTask.moveTaskToFront(task, (activityOptions == null && activityOptions.getExtraBundle() != null && activityOptions.getExtraBundle().getBoolean(IFlexibleWindowManagerExt.KEY_ACTIVITY_NO_ANIM)) ? true : z3, activityOptions, topNonFinishingActivity != null ? remoteTransition : topNonFinishingActivity.appTimeTracker, str2);
                if (ActivityTaskManagerDebugConfig.DEBUG_ROOT_TASK) {
                    Slog.d(TAG_ROOT_TASK, "findTaskToMoveToFront: moved to front of root task=" + rootTask);
                }
                handleNonResizableTaskIfNeeded(task, 0, this.mRootWindowContainer.getDefaultTaskDisplayArea(), rootTask, z);
                if (topNonFinishingActivity != null && (activityOptions == null || !activityOptions.getDisableStartingWindow())) {
                    topNonFinishingActivity.showStartingWindow(true);
                }
                if (transition2 != null) {
                    task.mTransitionController.requestStartTransition(transition2, task, activityOptions != null ? activityOptions.getRemoteTransition() : remoteTransition2, remoteTransition2);
                }
                this.mUserLeaving = z2;
                this.mService.continueWindowLayout();
                return;
            }
            rootTask.moveTaskToFront(task, (activityOptions == null && activityOptions.getExtraBundle() != null && activityOptions.getExtraBundle().getBoolean(IFlexibleWindowManagerExt.KEY_ACTIVITY_NO_ANIM)) ? true : z3, activityOptions, topNonFinishingActivity != null ? remoteTransition : topNonFinishingActivity.appTimeTracker, str2);
            if (ActivityTaskManagerDebugConfig.DEBUG_ROOT_TASK) {
            }
            handleNonResizableTaskIfNeeded(task, 0, this.mRootWindowContainer.getDefaultTaskDisplayArea(), rootTask, z);
            if (topNonFinishingActivity != null) {
                topNonFinishingActivity.showStartingWindow(true);
            }
            if (transition2 != null) {
            }
            this.mUserLeaving = z2;
            this.mService.continueWindowLayout();
            return;
        } catch (Throwable th3) {
            th = th3;
            this.mUserLeaving = z2;
            this.mService.continueWindowLayout();
            throw th;
        }
        transition = null;
        task.mTransitionController.collect(task);
        String str22 = str + " findTaskToMoveToFront";
        if (task.isResizeable()) {
        }
        z3 = false;
        remoteTransition = null;
        z4 = false;
        if (!z4) {
        }
        topNonFinishingActivity = task.getTopNonFinishingActivity();
        z2 = z3;
        RemoteTransition remoteTransition22 = remoteTransition;
        transition2 = transition;
    }

    private void moveHomeRootTaskToFrontIfNeeded(int i, TaskDisplayArea taskDisplayArea, String str) {
        Task focusedRootTask = taskDisplayArea.getFocusedRootTask();
        if ((taskDisplayArea.getWindowingMode() != 1 || (i & 1) == 0) && (focusedRootTask == null || !focusedRootTask.isActivityTypeRecents())) {
            return;
        }
        taskDisplayArea.moveHomeRootTaskToFront(str);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean canUseActivityOptionsLaunchBounds(ActivityOptions activityOptions) {
        if (activityOptions == null || activityOptions.getLaunchBounds() == null) {
            return false;
        }
        return (this.mService.mSupportsPictureInPicture && activityOptions.getLaunchWindowingMode() == 2) || this.mService.mSupportsFreeformWindowManagement;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public LaunchParamsController getLaunchParamsController() {
        return this.mLaunchParamsController;
    }

    private void removePinnedRootTaskInSurfaceTransaction(Task task) {
        task.cancelAnimation();
        task.setForceHidden(1, true);
        task.ensureActivitiesVisible(null, 0, true);
        activityIdleInternal(null, false, true, null);
        DisplayContent displayContent = this.mRootWindowContainer.getDisplayContent(0);
        this.mService.deferWindowLayout();
        try {
            task.setWindowingMode(0);
            if (task.getWindowingMode() != 5) {
                task.setBounds(null);
            }
            displayContent.getDefaultTaskDisplayArea().positionTaskBehindHome(task);
            task.setForceHidden(1, false);
            this.mRootWindowContainer.ensureActivitiesVisible(null, 0, true);
            this.mRootWindowContainer.resumeFocusedTasksTopActivities();
        } finally {
            this.mService.continueWindowLayout();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* renamed from: removeRootTaskInSurfaceTransaction, reason: merged with bridge method [inline-methods] */
    public void lambda$removeRootTask$4(Task task) {
        if (task.getWindowingMode() == 2) {
            removePinnedRootTaskInSurfaceTransaction(task);
        } else {
            task.forAllLeafTasks(new Consumer() { // from class: com.android.server.wm.ActivityTaskSupervisor$$ExternalSyntheticLambda0
                @Override // java.util.function.Consumer
                public final void accept(Object obj) {
                    ActivityTaskSupervisor.this.lambda$removeRootTaskInSurfaceTransaction$3((Task) obj);
                }
            }, true);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$removeRootTaskInSurfaceTransaction$3(Task task) {
        removeTask(task, true, true, "remove-root-task");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void removeRootTask(final Task task) {
        this.mWindowManager.inSurfaceTransaction(new Runnable() { // from class: com.android.server.wm.ActivityTaskSupervisor$$ExternalSyntheticLambda12
            @Override // java.lang.Runnable
            public final void run() {
                ActivityTaskSupervisor.this.lambda$removeRootTask$4(task);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean removeTaskById(int i, boolean z, boolean z2, String str, int i2) {
        Task anyTaskForId = this.mRootWindowContainer.anyTaskForId(i, 1);
        if (anyTaskForId != null) {
            removeTask(anyTaskForId, z, z2, str, i2, null);
            return true;
        }
        Slog.w(TAG, "Request to remove task ignored for non-existent task " + i);
        return false;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void removeTask(Task task, boolean z, boolean z2, String str) {
        removeTask(task, z, z2, str, 1000, null);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void removeTask(Task task, boolean z, boolean z2, String str, int i, String str2) {
        if (task.mInRemoveTask) {
            return;
        }
        task.mTransitionController.requestCloseTransitionIfNeeded(task);
        if (z) {
            ArrayList arrayList = null;
            for (int size = this.mStoppingActivities.size() - 1; size >= 0; size--) {
                ActivityRecord activityRecord = this.mStoppingActivities.get(size);
                if (activityRecord.getTask() == task) {
                    if (arrayList == null) {
                        arrayList = new ArrayList();
                    }
                    arrayList.add(activityRecord);
                    this.mStoppingActivities.remove(size);
                }
            }
            if (arrayList != null) {
                for (int size2 = arrayList.size() - 1; size2 >= 0; size2--) {
                    if (!((ActivityRecord) arrayList.get(size2)).isState(ActivityRecord.State.DESTROYING, ActivityRecord.State.DESTROYED)) {
                        ((ActivityRecord) arrayList.get(size2)).stopIfPossible();
                    }
                }
            }
        }
        this.mActivityTaskSupervisorExt.hookBeforeRemoveTask(task, str);
        task.mInRemoveTask = true;
        try {
            task.removeActivities(str, false);
            this.mActivityTaskSupervisorExt.handleRemoveTask(task, z, z2, str);
            if (task.getWrapper().getExtImpl().isCompactWindowingMode(task.getWindowingMode()) && task.getBaseIntent() != null && task.getBaseIntent().getComponent() != null) {
                task.getWrapper().getExtImpl().handleRemoveTask(task.getBaseIntent().getComponent().getPackageName(), task.mUserId);
            }
            cleanUpRemovedTask(task, z, z2);
            this.mService.getLockTaskController().clearLockedTask(task);
            this.mService.getTaskChangeNotificationController().notifyTaskStackChanged();
            if (task.isPersistable) {
                this.mService.notifyTaskPersisterLocked(null, true);
            }
            checkActivitySecurityForTaskClear(i, task, str2);
            if (!ActivityTaskManagerService.LTW_DISABLE) {
                this.mService.getWrapper().getExtImpl().getRemoteTaskManager().handleRemoveTask(task, str);
            }
        } finally {
            task.mInRemoveTask = false;
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r2v11, types: [java.lang.CharSequence] */
    private void checkActivitySecurityForTaskClear(int i, Task task, String str) {
        TaskDisplayArea taskDisplayArea;
        final String applicationLabel;
        String str2;
        if (i == 1000 || !task.isVisible() || task.inMultiWindowMode() || (taskDisplayArea = task.getTaskDisplayArea()) == null) {
            return;
        }
        boolean z = !((Boolean) doesTopActivityMatchingUidExistForAsm(task, i, null).first).booleanValue();
        if (!((Boolean) r2.second).booleanValue()) {
            ActivityRecord activity = task.getActivity(new Predicate() { // from class: com.android.server.wm.ActivityTaskSupervisor$$ExternalSyntheticLambda7
                @Override // java.util.function.Predicate
                public final boolean test(Object obj) {
                    boolean lambda$checkActivitySecurityForTaskClear$5;
                    lambda$checkActivitySecurityForTaskClear$5 = ActivityTaskSupervisor.lambda$checkActivitySecurityForTaskClear$5((ActivityRecord) obj);
                    return lambda$checkActivitySecurityForTaskClear$5;
                }
            });
            FrameworkStatsLog.write(495, i, str, activity == null ? -1 : activity.getUid(), activity != null ? activity.info.name : null, false, -1, (String) null, (String) null, 0, 4, 7, false, -1);
            final boolean z2 = ActivitySecurityModelFeatureFlags.shouldRestrictActivitySwitch(i) && z;
            PackageManager packageManager = this.mService.mContext.getPackageManager();
            String nameForUid = packageManager.getNameForUid(i);
            if (nameForUid == null) {
                applicationLabel = String.valueOf(i);
                str2 = applicationLabel;
            } else {
                applicationLabel = getApplicationLabel(packageManager, nameForUid);
                str2 = nameForUid;
            }
            if (ActivitySecurityModelFeatureFlags.shouldShowToast(i)) {
                UiThread.getHandler().post(new Runnable() { // from class: com.android.server.wm.ActivityTaskSupervisor$$ExternalSyntheticLambda8
                    @Override // java.lang.Runnable
                    public final void run() {
                        ActivityTaskSupervisor.this.lambda$checkActivitySecurityForTaskClear$6(z2, applicationLabel);
                    }
                });
            }
            if (z2) {
                Slog.w(TAG, "[ASM] Return to home as source: " + str2 + " is not on top of task t: " + task);
                taskDisplayArea.moveHomeActivityToTop("taskRemoved");
                return;
            }
            Slog.i(TAG, "[ASM] Would return to home as source: " + str2 + " is not on top of task t: " + task);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ boolean lambda$checkActivitySecurityForTaskClear$5(ActivityRecord activityRecord) {
        return (activityRecord.finishing || activityRecord.isAlwaysOnTop()) ? false : true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$checkActivitySecurityForTaskClear$6(boolean z, CharSequence charSequence) {
        Context context = this.mService.mContext;
        StringBuilder sb = new StringBuilder();
        sb.append("go/android-asm");
        sb.append(z ? " returned home due to " : " would return home due to ");
        sb.append((Object) charSequence);
        Toast.makeText(context, sb.toString(), 1).show();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static Pair<Boolean, Boolean> doesTopActivityMatchingUidExistForAsm(Task task, int i, final ActivityRecord activityRecord) {
        if (activityRecord != null && activityRecord.isVisible()) {
            Boolean bool = Boolean.TRUE;
            return new Pair<>(bool, bool);
        }
        ActivityRecord topMostActivity = task.getTopMostActivity();
        if (topMostActivity != null && topMostActivity.isUid(i)) {
            Boolean bool2 = Boolean.TRUE;
            return new Pair<>(bool2, bool2);
        }
        Predicate<ActivityRecord> predicate = new Predicate() { // from class: com.android.server.wm.ActivityTaskSupervisor$$ExternalSyntheticLambda11
            @Override // java.util.function.Predicate
            public final boolean test(Object obj) {
                boolean lambda$doesTopActivityMatchingUidExistForAsm$7;
                lambda$doesTopActivityMatchingUidExistForAsm$7 = ActivityTaskSupervisor.lambda$doesTopActivityMatchingUidExistForAsm$7(ActivityRecord.this, (ActivityRecord) obj);
                return lambda$doesTopActivityMatchingUidExistForAsm$7;
            }
        };
        ActivityRecord activity = task.getActivity(predicate);
        if (activity == null) {
            Boolean bool3 = Boolean.FALSE;
            return new Pair<>(bool3, bool3);
        }
        Pair<Boolean, Boolean> allowCrossUidActivitySwitchFromBelow = activity.allowCrossUidActivitySwitchFromBelow(i);
        if (((Boolean) allowCrossUidActivitySwitchFromBelow.first).booleanValue()) {
            return new Pair<>(Boolean.TRUE, (Boolean) allowCrossUidActivitySwitchFromBelow.second);
        }
        TaskFragment taskFragment = activity.getTaskFragment();
        if (taskFragment == null) {
            Boolean bool4 = Boolean.FALSE;
            return new Pair<>(bool4, bool4);
        }
        TaskFragment adjacentTaskFragment = taskFragment.getAdjacentTaskFragment();
        if (adjacentTaskFragment == null) {
            Boolean bool5 = Boolean.FALSE;
            return new Pair<>(bool5, bool5);
        }
        ActivityRecord activity2 = adjacentTaskFragment.getActivity(predicate);
        if (activity2 == null) {
            Boolean bool6 = Boolean.FALSE;
            return new Pair<>(bool6, bool6);
        }
        return activity2.allowCrossUidActivitySwitchFromBelow(i);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ boolean lambda$doesTopActivityMatchingUidExistForAsm$7(ActivityRecord activityRecord, ActivityRecord activityRecord2) {
        return activityRecord2.equals(activityRecord) || !(activityRecord2.finishing || activityRecord2.isAlwaysOnTop());
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static CharSequence getApplicationLabel(PackageManager packageManager, String str) {
        try {
            return packageManager.getApplicationLabel(packageManager.getApplicationInfo(str, PackageManager.ApplicationInfoFlags.of(0L)));
        } catch (PackageManager.NameNotFoundException unused) {
            return str;
        }
    }

    private void cleanUpRemovedTask(Task task, boolean z, boolean z2) {
        if (z2) {
            this.mRecentTasks.remove(task);
        }
        Intent baseIntent = task.getBaseIntent();
        ComponentName component = baseIntent != null ? baseIntent.getComponent() : null;
        if (component == null) {
            Slog.w(TAG, "No component for base intent of task: " + task);
            return;
        }
        this.mActivityTaskSupervisorExt.handleRemoveTask(z, task.mUserId, component.getPackageName());
        this.mService.mH.sendMessage(PooledLambda.obtainMessage(new QuadConsumer() { // from class: com.android.server.wm.ActivityTaskSupervisor$$ExternalSyntheticLambda10
            public final void accept(Object obj, Object obj2, Object obj3, Object obj4) {
                ((ActivityManagerInternal) obj).cleanUpServices(((Integer) obj2).intValue(), (ComponentName) obj3, (Intent) obj4);
            }
        }, this.mService.mAmInternal, Integer.valueOf(task.mUserId), component, new Intent(baseIntent)));
        if (z) {
            ActivityRecord topMostActivity = task.getTopMostActivity();
            if (topMostActivity != null && topMostActivity.finishing && !topMostActivity.mAppStopped && topMostActivity.lastVisibleTime > 0 && !task.mKillProcessesOnDestroyed) {
                task.mKillProcessesOnDestroyed = true;
                ActivityTaskSupervisorHandler activityTaskSupervisorHandler = this.mHandler;
                activityTaskSupervisorHandler.sendMessageDelayed(activityTaskSupervisorHandler.obtainMessage(KILL_TASK_PROCESSES_TIMEOUT_MSG, task), 1000L);
                return;
            }
            killTaskProcessesIfPossible(task);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void killTaskProcessesOnDestroyedIfNeeded(Task task) {
        if (task == null || !task.mKillProcessesOnDestroyed) {
            return;
        }
        this.mHandler.removeMessages(KILL_TASK_PROCESSES_TIMEOUT_MSG, task);
        killTaskProcessesIfPossible(task);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void killTaskProcessesIfPossible(Task task) {
        int removeTaskFilterType;
        task.mKillProcessesOnDestroyed = false;
        String basePackageName = task.getBasePackageName();
        ArrayMap map = this.mService.mProcessNames.getMap();
        ArrayList arrayList = null;
        for (int i = 0; i < map.size(); i++) {
            SparseArray sparseArray = (SparseArray) map.valueAt(i);
            for (int i2 = 0; i2 < sparseArray.size(); i2++) {
                WindowProcessController windowProcessController = (WindowProcessController) sparseArray.valueAt(i2);
                if (windowProcessController.mUserId == task.mUserId && windowProcessController != this.mService.mHomeProcess && windowProcessController.containsPackage(basePackageName)) {
                    if (!windowProcessController.shouldKillProcessForRemovedTask(task) || (removeTaskFilterType = this.mActivityTaskSupervisorExt.getRemoveTaskFilterType(windowProcessController)) == 1) {
                        return;
                    }
                    if (removeTaskFilterType == 2) {
                        continue;
                    } else if (removeTaskFilterType == 3) {
                        if (arrayList == null) {
                            arrayList = new ArrayList();
                        }
                        arrayList.add(windowProcessController);
                    } else {
                        if (windowProcessController.hasForegroundServices()) {
                            return;
                        }
                        if (arrayList == null) {
                            arrayList = new ArrayList();
                        }
                        arrayList.add(windowProcessController);
                    }
                }
            }
        }
        if (arrayList == null) {
            return;
        }
        this.mService.mH.sendMessage(PooledLambda.obtainMessage(new BiConsumer() { // from class: com.android.server.wm.ActivityTaskSupervisor$$ExternalSyntheticLambda1
            @Override // java.util.function.BiConsumer
            public final void accept(Object obj, Object obj2) {
                ((ActivityManagerInternal) obj).killProcessesForRemovedTask((ArrayList) obj2);
            }
        }, this.mService.mAmInternal, arrayList));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean restoreRecentTaskLocked(Task task, ActivityOptions activityOptions, boolean z) {
        Task orCreateRootTask = this.mRootWindowContainer.getOrCreateRootTask(null, activityOptions, task, z);
        WindowContainer parent = task.getParent();
        if (parent != orCreateRootTask && task != orCreateRootTask) {
            if (parent != null) {
                if (!this.mActivityTaskSupervisorExt.checkIsValidParentForSplitScreen(task, orCreateRootTask)) {
                    return true;
                }
                task.reparent(orCreateRootTask, Integer.MAX_VALUE, true, "restoreRecentTaskLocked");
                return true;
            }
            orCreateRootTask.addChild((WindowContainer) task, z, true);
            if (ActivityTaskManagerDebugConfig.DEBUG_RECENTS) {
                Slog.v(TAG_RECENTS, "Added restored task=" + task + " to root task=" + orCreateRootTask);
            }
        }
        return true;
    }

    @Override // com.android.server.wm.RecentTasks.Callbacks
    public void onRecentTaskAdded(Task task) {
        task.touchActiveTime();
    }

    @Override // com.android.server.wm.RecentTasks.Callbacks
    public void onRecentTaskRemoved(Task task, boolean z, boolean z2) {
        if (z) {
            removeTaskById(task.mTaskId, z2, false, "recent-task-trimmed", 1000);
        }
        task.removedFromRecents();
        if (z2) {
            task.getWrapper().getExtImpl().removedFromRecents(task);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Task getReparentTargetRootTask(Task task, Task task2, boolean z) {
        Task rootTask = task.getRootTask();
        int i = task2.mTaskId;
        boolean inMultiWindowMode = task2.inMultiWindowMode();
        if (rootTask != null && rootTask.mTaskId == i) {
            Slog.w(TAG, "Can not reparent to same root task, task=" + task + " already in rootTaskId=" + i);
            return rootTask;
        }
        if (inMultiWindowMode && !this.mService.mSupportsMultiWindow) {
            throw new IllegalArgumentException("Device doesn't support multi-window, can not reparent task=" + task + " to root-task=" + task2);
        }
        if (task2.getDisplayId() != 0 && !this.mService.mSupportsMultiDisplay) {
            throw new IllegalArgumentException("Device doesn't support multi-display, can not reparent task=" + task + " to rootTaskId=" + i);
        }
        if (task2.getWindowingMode() == 5 && !this.mService.mSupportsFreeformWindowManagement) {
            throw new IllegalArgumentException("Device doesn't support freeform, can not reparent task=" + task);
        }
        if (task2.inPinnedWindowingMode()) {
            throw new IllegalArgumentException("No support to reparent to PIP, task=" + task);
        }
        if (!inMultiWindowMode || task.supportsMultiWindowInDisplayArea(task2.getDisplayArea())) {
            return task2;
        }
        Slog.w(TAG, "Can not move unresizeable task=" + task + " to multi-window root task=" + task2 + " Moving to a fullscreen root task instead.");
        return rootTask != null ? rootTask : task2.getDisplayArea().createRootTask(1, task2.getActivityType(), z);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void goingToSleepLocked() {
        this.mActivityTaskSupervisorExt.removeAccessControlPassAsUser(null, 0, true);
        this.mActivityTaskSupervisorExt.recordTopActivityWhenScreenOff(this.mService);
        scheduleSleepTimeout();
        if (!this.mGoingToSleepWakeLock.isHeld()) {
            this.mGoingToSleepWakeLock.acquire();
            if (this.mLaunchingActivityWakeLock.isHeld()) {
                this.mLaunchingActivityWakeLock.release();
                this.mHandler.removeMessages(LAUNCH_TIMEOUT_MSG);
            }
        }
        this.mRootWindowContainer.applySleepTokens(false);
        WindowManagerGlobalLock windowManagerGlobalLock = this.mService.mGlobalLock;
        WindowManagerService.boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                checkReadyForSleepLocked(true);
            } catch (Throwable th) {
                WindowManagerService.resetPriorityAfterLockedSection();
                throw th;
            }
        }
        WindowManagerService.resetPriorityAfterLockedSection();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean shutdownLocked(int i) {
        boolean z;
        goingToSleepLocked();
        long currentTimeMillis = System.currentTimeMillis() + i;
        while (true) {
            z = true;
            if (!this.mRootWindowContainer.putTasksToSleep(true, true)) {
                long currentTimeMillis2 = currentTimeMillis - System.currentTimeMillis();
                if (currentTimeMillis2 > 0) {
                    try {
                        this.mService.mGlobalLock.wait(currentTimeMillis2);
                    } catch (InterruptedException unused) {
                    }
                } else {
                    Slog.w(TAG, "Activity manager shutdown timed out");
                    break;
                }
            } else {
                z = false;
                break;
            }
        }
        checkReadyForSleepLocked(false);
        return z;
    }

    public ActivityRecord getTopResumedActivity() {
        return this.mTopResumedActivity;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void comeOutOfSleepIfNeededLocked() {
        removeSleepTimeouts();
        if (this.mGoingToSleepWakeLock.isHeld()) {
            try {
                this.mGoingToSleepWakeLock.release();
            } catch (RuntimeException e) {
                Slog.e(TAG, "Fail to release wakelock." + e);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void checkReadyForSleepLocked(boolean z) {
        if (this.mService.isSleepingOrShuttingDownLocked() && this.mRootWindowContainer.putTasksToSleep(z, false)) {
            this.mService.endLaunchPowerMode(3);
            this.mRootWindowContainer.rankTaskLayers();
            removeSleepTimeouts();
            if (this.mGoingToSleepWakeLock.isHeld()) {
                try {
                    this.mGoingToSleepWakeLock.release();
                } catch (RuntimeException e) {
                    Slog.e(TAG, "Fail to release wakelock." + e);
                }
            }
            ActivityTaskManagerService activityTaskManagerService = this.mService;
            if (activityTaskManagerService.mShuttingDown) {
                activityTaskManagerService.mGlobalLock.notifyAll();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean reportResumedActivityLocked(ActivityRecord activityRecord) {
        this.mStoppingActivities.remove(activityRecord);
        if (!activityRecord.getRootTask().getDisplayArea().allResumedActivitiesComplete()) {
            return false;
        }
        this.mRootWindowContainer.ensureActivitiesVisible(null, 0, false);
        this.mRootWindowContainer.executeAppTransitionForAllDisplay();
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleLaunchTaskBehindCompleteLocked(ActivityRecord activityRecord) {
        Task task = activityRecord.getTask();
        Task rootTask = task.getRootTask();
        this.mRecentTasks.add(task);
        this.mService.getTaskChangeNotificationController().notifyTaskStackChanged();
        rootTask.ensureActivitiesVisible(null, 0, false);
        ActivityRecord topNonFinishingActivity = rootTask.getTopNonFinishingActivity();
        if (topNonFinishingActivity != null) {
            topNonFinishingActivity.getTask().touchActiveTime();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void scheduleLaunchTaskBehindComplete(IBinder iBinder) {
        this.mHandler.obtainMessage(LAUNCH_TASK_BEHIND_COMPLETE, iBinder).sendToTarget();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void processStoppingAndFinishingActivities(ActivityRecord activityRecord, boolean z, String str) {
        int i = 0;
        boolean z2 = false;
        ArrayList arrayList = null;
        while (i < this.mStoppingActivities.size()) {
            ActivityRecord activityRecord2 = this.mStoppingActivities.get(i);
            if (activityRecord2.getWrapper().getExtImpl().shouldMakeHomeActivityVisibleOnSecondary(activityRecord2, getKeyguardController())) {
                Slog.d(TAG, "activity: " + activityRecord2 + "should be intercepted to stop state");
            } else {
                boolean z3 = (!activityRecord2.isInTransition() || activityRecord2.getTask() == null || activityRecord2.getTask().isForceHidden()) ? false : true;
                z2 |= activityRecord2.isDisplaySleepingAndSwapping();
                if (ProtoLogCache.WM_DEBUG_STATES_enabled) {
                    ProtoLogImpl.v(ProtoLogGroup.WM_DEBUG_STATES, -1740512980, 60, (String) null, new Object[]{String.valueOf(activityRecord2), Boolean.valueOf(activityRecord2.nowVisible), Boolean.valueOf(z3), String.valueOf(activityRecord2.finishing)});
                }
                if ((!z3 && !z2) || this.mService.mShuttingDown) {
                    if (!z && activityRecord2.isState(ActivityRecord.State.PAUSING)) {
                        removeIdleTimeoutForActivity(activityRecord);
                        scheduleIdleTimeout(activityRecord);
                    } else {
                        if (ProtoLogCache.WM_DEBUG_STATES_enabled) {
                            ProtoLogImpl.v(ProtoLogGroup.WM_DEBUG_STATES, -1707370822, 0, (String) null, new Object[]{String.valueOf(activityRecord2)});
                        }
                        if (arrayList == null) {
                            arrayList = new ArrayList();
                        }
                        arrayList.add(activityRecord2);
                        this.mStoppingActivities.remove(i);
                        i--;
                    }
                }
            }
            i++;
        }
        if (z2) {
            this.mHandler.postDelayed(new Runnable() { // from class: com.android.server.wm.ActivityTaskSupervisor$$ExternalSyntheticLambda5
                @Override // java.lang.Runnable
                public final void run() {
                    ActivityTaskSupervisor.this.lambda$processStoppingAndFinishingActivities$8();
                }
            }, 200L);
        }
        int size = arrayList == null ? 0 : arrayList.size();
        for (int i2 = 0; i2 < size; i2++) {
            ActivityRecord activityRecord3 = (ActivityRecord) arrayList.get(i2);
            if (activityRecord3.isInHistory()) {
                if (activityRecord3.finishing) {
                    activityRecord3.destroyIfPossible(str);
                } else {
                    activityRecord3.stopIfPossible();
                }
            }
        }
        int size2 = this.mFinishingActivities.size();
        if (size2 == 0) {
            return;
        }
        ArrayList arrayList2 = new ArrayList(this.mFinishingActivities);
        this.mFinishingActivities.clear();
        for (int i3 = 0; i3 < size2; i3++) {
            ActivityRecord activityRecord4 = (ActivityRecord) arrayList2.get(i3);
            if (activityRecord4.isInHistory()) {
                activityRecord4.destroyImmediately("finish-" + str);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$processStoppingAndFinishingActivities$8() {
        WindowManagerGlobalLock windowManagerGlobalLock = this.mService.mGlobalLock;
        WindowManagerService.boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                scheduleProcessStoppingAndFinishingActivitiesIfNeeded();
            } catch (Throwable th) {
                WindowManagerService.resetPriorityAfterLockedSection();
                throw th;
            }
        }
        WindowManagerService.resetPriorityAfterLockedSection();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void removeHistoryRecords(WindowProcessController windowProcessController) {
        removeHistoryRecords(this.mStoppingActivities, windowProcessController, "mStoppingActivities");
        removeHistoryRecords(this.mFinishingActivities, windowProcessController, "mFinishingActivities");
        removeHistoryRecords(this.mNoHistoryActivities, windowProcessController, "mNoHistoryActivities");
    }

    private void removeHistoryRecords(ArrayList<ActivityRecord> arrayList, WindowProcessController windowProcessController, String str) {
        int size = arrayList.size();
        if (ActivityTaskManagerDebugConfig.DEBUG_CLEANUP) {
            Slog.v(Task.TAG_CLEANUP, "Removing app " + this + " from list " + str + " with " + size + " entries");
        }
        while (size > 0) {
            size--;
            ActivityRecord activityRecord = arrayList.get(size);
            if (ActivityTaskManagerDebugConfig.DEBUG_CLEANUP) {
                Slog.v(Task.TAG_CLEANUP, "Record #" + size + " " + activityRecord);
            }
            if (activityRecord.app == windowProcessController) {
                if (ActivityTaskManagerDebugConfig.DEBUG_CLEANUP) {
                    Slog.v(Task.TAG_CLEANUP, "---> REMOVING this entry!");
                }
                arrayList.remove(size);
                activityRecord.removeTimeouts();
            }
        }
    }

    public void dump(PrintWriter printWriter, String str) {
        printWriter.println();
        printWriter.println("ActivityTaskSupervisor state:");
        this.mRootWindowContainer.dump(printWriter, str, true);
        getKeyguardController().dump(printWriter, str);
        this.mService.getLockTaskController().dump(printWriter, str);
        printWriter.print(str);
        printWriter.println("mCurTaskIdForUser=" + this.mCurTaskIdForUser);
        printWriter.println(str + "mUserRootTaskInFront=" + this.mRootWindowContainer.mUserRootTaskInFront);
        printWriter.println(str + "mVisibilityTransactionDepth=" + this.mVisibilityTransactionDepth);
        printWriter.print(str);
        printWriter.print("isHomeRecentsComponent=");
        printWriter.println(this.mRecentTasks.isRecentsComponentHomeActivity(this.mRootWindowContainer.mCurrentUser));
        if (!this.mWaitingActivityLaunched.isEmpty()) {
            printWriter.println(str + "mWaitingActivityLaunched=");
            for (int size = this.mWaitingActivityLaunched.size() - 1; size >= 0; size += -1) {
                this.mWaitingActivityLaunched.get(size).dump(printWriter, str + "  ");
            }
        }
        printWriter.println(str + "mNoHistoryActivities=" + this.mNoHistoryActivities);
        printWriter.println();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static boolean printThisActivity(PrintWriter printWriter, ActivityRecord activityRecord, String str, boolean z, String str2, Runnable runnable) {
        return printThisActivity(printWriter, activityRecord, str, -1, z, str2, runnable);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static boolean printThisActivity(PrintWriter printWriter, ActivityRecord activityRecord, String str, int i, boolean z, String str2, Runnable runnable) {
        if (activityRecord == null) {
            return false;
        }
        if (i != -1 && i != activityRecord.getDisplayId()) {
            return false;
        }
        if (str != null && !str.equals(activityRecord.packageName)) {
            return false;
        }
        if (z) {
            printWriter.println();
        }
        if (runnable != null) {
            runnable.run();
        }
        printWriter.print(str2);
        printWriter.println(activityRecord);
        return true;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static boolean dumpHistoryList(FileDescriptor fileDescriptor, PrintWriter printWriter, List<ActivityRecord> list, String str, String str2, boolean z, boolean z2, boolean z3, String str3, boolean z4, Runnable runnable, Task task) {
        int size = list.size() - 1;
        boolean z5 = z4;
        Runnable runnable2 = runnable;
        Task task2 = task;
        while (size >= 0) {
            ActivityRecord activityRecord = list.get(size);
            ActivityRecord.dumpActivity(fileDescriptor, printWriter, size, activityRecord, str, str2, z, z2, z3, str3, z5, runnable2, task2);
            task2 = activityRecord.getTask();
            z5 = z3 && activityRecord.attachedToProcess();
            size--;
            runnable2 = null;
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void scheduleIdleTimeout(ActivityRecord activityRecord) {
        if (ActivityTaskManagerDebugConfig.DEBUG_IDLE) {
            Slog.d(TAG_IDLE, "scheduleIdleTimeout: Callers=" + Debug.getCallers(4));
        }
        this.mHandler.sendMessageDelayed(this.mHandler.obtainMessage(200, activityRecord), IDLE_TIMEOUT);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final void scheduleIdle() {
        if (this.mHandler.hasMessages(IDLE_NOW_MSG)) {
            return;
        }
        if (ActivityTaskManagerDebugConfig.DEBUG_IDLE) {
            Slog.d(TAG_IDLE, "scheduleIdle: Callers=" + Debug.getCallers(4));
        }
        this.mHandler.sendEmptyMessage(IDLE_NOW_MSG);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void updateTopResumedActivityIfNeeded(String str) {
        ActivityRecord activityRecord = this.mTopResumedActivity;
        Task topDisplayFocusedRootTask = this.mRootWindowContainer.getTopDisplayFocusedRootTask();
        if (topDisplayFocusedRootTask == null || topDisplayFocusedRootTask.getTopResumedActivity() == activityRecord || this.mActivityTaskSupervisorExt.skipUpdateResumedActivityIfNeeded(topDisplayFocusedRootTask, activityRecord, str)) {
            if (this.mService.isSleepingLocked()) {
                this.mService.updateTopApp(null);
                return;
            }
            return;
        }
        if (((activityRecord == null || this.mTopResumedActivityWaitingForPrev) ? false : true) && activityRecord.scheduleTopResumedActivityChanged(false)) {
            scheduleTopResumedStateLossTimeout(activityRecord);
            this.mTopResumedActivityWaitingForPrev = true;
            this.mActivityTaskSupervisorExt.updateResumeLostActivity(activityRecord);
        }
        ActivityRecord topResumedActivity = topDisplayFocusedRootTask.getTopResumedActivity();
        this.mTopResumedActivity = topResumedActivity;
        if (topResumedActivity != null && activityRecord != null) {
            WindowProcessController windowProcessController = topResumedActivity.app;
            if (windowProcessController != null) {
                windowProcessController.addToPendingTop();
            }
            this.mService.updateOomAdj();
        }
        ActivityRecord activityRecord2 = this.mTopResumedActivity;
        if (activityRecord2 != null) {
            this.mService.setLastResumedActivityUncheckLocked(activityRecord2, str);
        }
        scheduleTopResumedActivityStateIfNeeded();
        this.mService.updateTopApp(this.mTopResumedActivity);
    }

    private void scheduleTopResumedActivityStateIfNeeded() {
        ActivityRecord activityRecord = this.mTopResumedActivity;
        if (activityRecord == null || this.mTopResumedActivityWaitingForPrev) {
            return;
        }
        this.mActivityTaskSupervisorExt.notifyAppSwitch(activityRecord, this.mService, this.mUserLeaving);
        activityRecord.scheduleTopResumedActivityChanged(true);
    }

    private void scheduleTopResumedStateLossTimeout(ActivityRecord activityRecord) {
        Message obtainMessage = this.mHandler.obtainMessage(TOP_RESUMED_STATE_LOSS_TIMEOUT_MSG);
        obtainMessage.obj = activityRecord;
        activityRecord.topResumedStateLossTime = SystemClock.uptimeMillis();
        this.mHandler.sendMessageDelayed(obtainMessage, 500L);
        if (ProtoLogCache.WM_DEBUG_STATES_enabled) {
            ProtoLogImpl.v(ProtoLogGroup.WM_DEBUG_STATES, 74885950, 0, (String) null, new Object[]{String.valueOf(activityRecord)});
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void handleTopResumedStateReleased(boolean z) {
        if (ProtoLogCache.WM_DEBUG_STATES_enabled) {
            ProtoLogImpl.v(ProtoLogGroup.WM_DEBUG_STATES, 708142634, 0, (String) null, new Object[]{z ? "(due to timeout)" : "(transition complete)"});
        }
        this.mHandler.removeMessages(TOP_RESUMED_STATE_LOSS_TIMEOUT_MSG);
        if (this.mTopResumedActivityWaitingForPrev) {
            this.mTopResumedActivityWaitingForPrev = false;
            scheduleTopResumedActivityStateIfNeeded();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void removeIdleTimeoutForActivity(ActivityRecord activityRecord) {
        if (ActivityTaskManagerDebugConfig.DEBUG_IDLE) {
            Slog.d(TAG_IDLE, "removeTimeoutsForActivity: Callers=" + Debug.getCallers(4));
        }
        this.mHandler.removeMessages(200, activityRecord);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final void scheduleResumeTopActivities() {
        if (this.mHandler.hasMessages(RESUME_TOP_ACTIVITY_MSG)) {
            return;
        }
        this.mHandler.sendEmptyMessage(RESUME_TOP_ACTIVITY_MSG);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void scheduleProcessStoppingAndFinishingActivitiesIfNeeded() {
        if (this.mStoppingActivities.isEmpty() && this.mFinishingActivities.isEmpty()) {
            return;
        }
        if (this.mRootWindowContainer.allResumedActivitiesIdle()) {
            scheduleIdle();
        } else {
            if (this.mHandler.hasMessages(PROCESS_STOPPING_AND_FINISHING_MSG) || !this.mRootWindowContainer.allResumedActivitiesVisible()) {
                return;
            }
            this.mHandler.sendEmptyMessage(PROCESS_STOPPING_AND_FINISHING_MSG);
        }
    }

    void removeSleepTimeouts() {
        this.mHandler.removeMessages(SLEEP_TIMEOUT_MSG);
    }

    final void scheduleSleepTimeout() {
        removeSleepTimeouts();
        this.mHandler.sendEmptyMessageDelayed(SLEEP_TIMEOUT_MSG, SLEEP_TIMEOUT);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean hasScheduledRestartTimeouts(ActivityRecord activityRecord) {
        return this.mHandler.hasMessages(RESTART_ACTIVITY_PROCESS_TIMEOUT_MSG, activityRecord);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void removeRestartTimeouts(ActivityRecord activityRecord) {
        this.mHandler.removeMessages(RESTART_ACTIVITY_PROCESS_TIMEOUT_MSG, activityRecord);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final void scheduleRestartTimeout(ActivityRecord activityRecord) {
        removeRestartTimeouts(activityRecord);
        ActivityTaskSupervisorHandler activityTaskSupervisorHandler = this.mHandler;
        activityTaskSupervisorHandler.sendMessageDelayed(activityTaskSupervisorHandler.obtainMessage(RESTART_ACTIVITY_PROCESS_TIMEOUT_MSG, activityRecord), 2000L);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void handleNonResizableTaskIfNeeded(Task task, int i, TaskDisplayArea taskDisplayArea, Task task2) {
        handleNonResizableTaskIfNeeded(task, i, taskDisplayArea, task2, false);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void handleNonResizableTaskIfNeeded(Task task, int i, TaskDisplayArea taskDisplayArea, Task task2, boolean z) {
        boolean z2 = (taskDisplayArea == null || taskDisplayArea.getDisplayId() == 0) ? false : true;
        if (task.isActivityTypeStandardOrUndefined()) {
            if (!z2) {
                if (z) {
                    return;
                }
                handleForcedResizableTaskIfNeeded(task, 1);
                return;
            }
            if (!task.canBeLaunchedOnDisplay(task.getDisplayId())) {
                throw new IllegalStateException("Task resolved to incompatible display");
            }
            DisplayContent displayContent = taskDisplayArea.mDisplayContent;
            if (displayContent == task.getDisplayContent()) {
                if (z) {
                    return;
                }
                handleForcedResizableTaskIfNeeded(task, 2);
                return;
            }
            Slog.w(TAG, "Failed to put " + task + " on display " + displayContent.mDisplayId);
            this.mService.getTaskChangeNotificationController().notifyActivityLaunchOnSecondaryDisplayFailed(task.getTaskInfo(), displayContent.mDisplayId);
        }
    }

    private void handleForcedResizableTaskIfNeeded(Task task, int i) {
        ActivityRecord topNonFinishingActivity = task.getTopNonFinishingActivity();
        if (topNonFinishingActivity == null || topNonFinishingActivity.noDisplay || !topNonFinishingActivity.canForceResizeNonResizable(task.getWindowingMode())) {
            return;
        }
        this.mService.getTaskChangeNotificationController().notifyActivityForcedResizable(task.mTaskId, i, topNonFinishingActivity.info.applicationInfo.packageName);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void scheduleUpdateMultiWindowMode(Task task) {
        task.forAllActivities(new Consumer() { // from class: com.android.server.wm.ActivityTaskSupervisor$$ExternalSyntheticLambda4
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                ActivityTaskSupervisor.this.lambda$scheduleUpdateMultiWindowMode$9((ActivityRecord) obj);
            }
        });
        if (this.mHandler.hasMessages(REPORT_MULTI_WINDOW_MODE_CHANGED_MSG)) {
            return;
        }
        this.mHandler.sendEmptyMessage(REPORT_MULTI_WINDOW_MODE_CHANGED_MSG);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$scheduleUpdateMultiWindowMode$9(ActivityRecord activityRecord) {
        if (activityRecord.attachedToProcess()) {
            this.mMultiWindowModeChangedActivities.add(activityRecord);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void scheduleUpdatePictureInPictureModeIfNeeded(Task task, Task task2) {
        Task rootTask = task.getRootTask();
        if (task2 != null) {
            if (task2 == rootTask || task2.inPinnedWindowingMode() || rootTask.inPinnedWindowingMode()) {
                scheduleUpdatePictureInPictureModeIfNeeded(task, rootTask.getRequestedOverrideBounds());
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void scheduleUpdatePictureInPictureModeIfNeeded(Task task, Rect rect) {
        task.forAllActivities(new Consumer() { // from class: com.android.server.wm.ActivityTaskSupervisor$$ExternalSyntheticLambda3
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                ActivityTaskSupervisor.this.lambda$scheduleUpdatePictureInPictureModeIfNeeded$10((ActivityRecord) obj);
            }
        });
        this.mPipModeChangedTargetRootTaskBounds = rect;
        if (this.mHandler.hasMessages(REPORT_PIP_MODE_CHANGED_MSG)) {
            return;
        }
        this.mHandler.sendEmptyMessage(REPORT_PIP_MODE_CHANGED_MSG);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$scheduleUpdatePictureInPictureModeIfNeeded$10(ActivityRecord activityRecord) {
        if (activityRecord.attachedToProcess()) {
            this.mPipModeChangedActivities.add(activityRecord);
            this.mMultiWindowModeChangedActivities.remove(activityRecord);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void wakeUp(String str) {
        this.mPowerManager.wakeUp(SystemClock.uptimeMillis(), 2, "android.server.am:TURN_ON:" + str);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void beginActivityVisibilityUpdate() {
        if (this.mVisibilityTransactionDepth == 0) {
            getKeyguardController().updateVisibility();
        }
        this.mVisibilityTransactionDepth++;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void endActivityVisibilityUpdate() {
        int i = this.mVisibilityTransactionDepth - 1;
        this.mVisibilityTransactionDepth = i;
        if (i == 0) {
            computeProcessActivityStateBatch();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean inActivityVisibilityUpdate() {
        return this.mVisibilityTransactionDepth > 0;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setDeferRootVisibilityUpdate(boolean z) {
        this.mDeferRootVisibilityUpdate = z;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean isRootVisibilityUpdateDeferred() {
        return this.mDeferRootVisibilityUpdate;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void onProcessActivityStateChanged(WindowProcessController windowProcessController, boolean z) {
        if (z || inActivityVisibilityUpdate()) {
            if (this.mActivityStateChangedProcs.contains(windowProcessController)) {
                return;
            }
            this.mActivityStateChangedProcs.add(windowProcessController);
            return;
        }
        windowProcessController.computeProcessActivityState();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void computeProcessActivityStateBatch() {
        if (this.mActivityStateChangedProcs.isEmpty()) {
            return;
        }
        for (int size = this.mActivityStateChangedProcs.size() - 1; size >= 0; size--) {
            this.mActivityStateChangedProcs.get(size).computeProcessActivityState();
        }
        this.mActivityStateChangedProcs.clear();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void beginDeferResume() {
        this.mDeferResumeCount++;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void endDeferResume() {
        this.mDeferResumeCount--;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean readyToResume() {
        return this.mDeferResumeCount == 0;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
    public final class ActivityTaskSupervisorHandler extends Handler {
        ActivityTaskSupervisorHandler(Looper looper) {
            super(looper);
        }

        @Override // android.os.Handler
        public void handleMessage(Message message) {
            String str;
            int i;
            WindowManagerGlobalLock windowManagerGlobalLock = ActivityTaskSupervisor.this.mService.mGlobalLock;
            WindowManagerService.boostPriorityForLockedSection();
            synchronized (windowManagerGlobalLock) {
                try {
                    if (handleMessageInner(message)) {
                        return;
                    }
                    WindowManagerService.resetPriorityAfterLockedSection();
                    if (message.what != ActivityTaskSupervisor.RESTART_ACTIVITY_PROCESS_TIMEOUT_MSG) {
                        return;
                    }
                    ActivityRecord activityRecord = (ActivityRecord) message.obj;
                    WindowManagerGlobalLock windowManagerGlobalLock2 = ActivityTaskSupervisor.this.mService.mGlobalLock;
                    WindowManagerService.boostPriorityForLockedSection();
                    synchronized (windowManagerGlobalLock2) {
                        try {
                            if (activityRecord.attachedToProcess() && activityRecord.isState(ActivityRecord.State.RESTARTING_PROCESS)) {
                                WindowProcessController windowProcessController = activityRecord.app;
                                str = windowProcessController.mName;
                                i = windowProcessController.mUid;
                            } else {
                                str = null;
                                i = 0;
                            }
                        } finally {
                        }
                    }
                    WindowManagerService.resetPriorityAfterLockedSection();
                    if (str != null) {
                        ActivityTaskSupervisor.this.mService.mAmInternal.killProcess(str, i, "restartActivityProcessTimeout");
                    }
                } finally {
                    WindowManagerService.resetPriorityAfterLockedSection();
                }
            }
        }

        private void activityIdleFromMessage(ActivityRecord activityRecord, boolean z) {
            ActivityTaskSupervisor.this.activityIdleInternal(activityRecord, z, z, null);
        }

        private boolean handleMessageInner(Message message) {
            int i = message.what;
            if (i != ActivityTaskSupervisor.LAUNCH_TASK_BEHIND_COMPLETE) {
                switch (i) {
                    case 200:
                        if (ActivityTaskManagerDebugConfig.DEBUG_IDLE) {
                            Slog.d(ActivityTaskSupervisor.TAG_IDLE, "handleMessage: IDLE_TIMEOUT_MSG: r=" + message.obj);
                        }
                        activityIdleFromMessage((ActivityRecord) message.obj, true);
                        break;
                    case ActivityTaskSupervisor.IDLE_NOW_MSG /* 201 */:
                        if (ActivityTaskManagerDebugConfig.DEBUG_IDLE) {
                            Slog.d(ActivityTaskSupervisor.TAG_IDLE, "handleMessage: IDLE_NOW_MSG: r=" + message.obj);
                        }
                        activityIdleFromMessage((ActivityRecord) message.obj, false);
                        break;
                    case ActivityTaskSupervisor.RESUME_TOP_ACTIVITY_MSG /* 202 */:
                        ActivityTaskSupervisor.this.mRootWindowContainer.resumeFocusedTasksTopActivities();
                        break;
                    case ActivityTaskSupervisor.SLEEP_TIMEOUT_MSG /* 203 */:
                        if (ActivityTaskSupervisor.this.mService.isSleepingOrShuttingDownLocked()) {
                            Slog.w(ActivityTaskSupervisor.TAG, "Sleep timeout!  Sleeping now.");
                            ActivityTaskSupervisor.this.checkReadyForSleepLocked(false);
                            break;
                        }
                        break;
                    case ActivityTaskSupervisor.LAUNCH_TIMEOUT_MSG /* 204 */:
                        if (ActivityTaskSupervisor.this.mLaunchingActivityWakeLock.isHeld()) {
                            Slog.w(ActivityTaskSupervisor.TAG, "Launch timeout has expired, giving up wake lock!");
                            IActivityTaskSupervisorExt iActivityTaskSupervisorExt = ActivityTaskSupervisor.this.mActivityTaskSupervisorExt;
                            ActivityTaskSupervisor activityTaskSupervisor = ActivityTaskSupervisor.this;
                            iActivityTaskSupervisorExt.appLaunchTimeout(activityTaskSupervisor.mRootWindowContainer, activityTaskSupervisor.mService.mContext);
                            ActivityTaskSupervisor.this.mLaunchingActivityWakeLock.release();
                            break;
                        }
                        break;
                    case ActivityTaskSupervisor.PROCESS_STOPPING_AND_FINISHING_MSG /* 205 */:
                        ActivityTaskSupervisor.this.processStoppingAndFinishingActivities(null, false, "transit");
                        break;
                    case ActivityTaskSupervisor.KILL_TASK_PROCESSES_TIMEOUT_MSG /* 206 */:
                        Task task = (Task) message.obj;
                        if (task.mKillProcessesOnDestroyed) {
                            Slog.i(ActivityTaskSupervisor.TAG, "Destroy timeout of remove-task, attempt to kill " + task);
                            ActivityTaskSupervisor.this.killTaskProcessesIfPossible(task);
                            break;
                        }
                        break;
                    default:
                        switch (i) {
                            case ActivityTaskSupervisor.REPORT_MULTI_WINDOW_MODE_CHANGED_MSG /* 214 */:
                                for (int size = ActivityTaskSupervisor.this.mMultiWindowModeChangedActivities.size() - 1; size >= 0; size--) {
                                    ((ActivityRecord) ActivityTaskSupervisor.this.mMultiWindowModeChangedActivities.remove(size)).updateMultiWindowMode();
                                }
                                break;
                            case ActivityTaskSupervisor.REPORT_PIP_MODE_CHANGED_MSG /* 215 */:
                                for (int size2 = ActivityTaskSupervisor.this.mPipModeChangedActivities.size() - 1; size2 >= 0; size2--) {
                                    ((ActivityRecord) ActivityTaskSupervisor.this.mPipModeChangedActivities.remove(size2)).updatePictureInPictureMode(ActivityTaskSupervisor.this.mPipModeChangedTargetRootTaskBounds, false);
                                }
                                break;
                            case ActivityTaskSupervisor.START_HOME_MSG /* 216 */:
                                ActivityTaskSupervisor.this.mHandler.removeMessages(ActivityTaskSupervisor.START_HOME_MSG);
                                ActivityTaskSupervisor.this.mRootWindowContainer.startHomeOnEmptyDisplays((String) message.obj);
                                break;
                            case ActivityTaskSupervisor.TOP_RESUMED_STATE_LOSS_TIMEOUT_MSG /* 217 */:
                                ActivityRecord activityRecord = (ActivityRecord) message.obj;
                                Slog.w(ActivityTaskSupervisor.TAG, "Activity top resumed state loss timeout for " + activityRecord);
                                if (activityRecord.hasProcess()) {
                                    ActivityTaskSupervisor.this.mService.logAppTooSlow(activityRecord.app, activityRecord.topResumedStateLossTime, "top state loss for " + activityRecord);
                                }
                                ActivityTaskSupervisor.this.handleTopResumedStateReleased(true);
                                break;
                            default:
                                return false;
                        }
                }
            } else {
                ActivityRecord forTokenLocked = ActivityRecord.forTokenLocked((IBinder) message.obj);
                if (forTokenLocked != null) {
                    ActivityTaskSupervisor.this.handleLaunchTaskBehindCompleteLocked(forTokenLocked);
                }
            }
            return true;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int startActivityFromRecents(int i, int i2, int i3, SafeActivityOptions safeActivityOptions) {
        boolean z;
        int i4;
        ActivityOptions options = safeActivityOptions != null ? safeActivityOptions.getOptions(this) : null;
        if (this.mActivityTaskSupervisorExt.startActivityFromRecents(i3, options, i2)) {
            return -96;
        }
        WindowManagerGlobalLock windowManagerGlobalLock = this.mService.mGlobalLock;
        WindowManagerService.boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                boolean isCallerRecents = this.mRecentTasks.isCallerRecents(i2);
                if (options != null) {
                    i4 = options.getLaunchActivityType();
                    if (options.freezeRecentTasksReordering() && (isCallerRecents || ActivityTaskManagerService.checkPermission("android.permission.MANAGE_ACTIVITY_TASKS", i, i2) == 0)) {
                        this.mRecentTasks.setFreezeTaskListReordering();
                    }
                    z = options.getLaunchRootTask() == null;
                } else {
                    z = true;
                    i4 = 0;
                }
                if (i4 == 2 || i4 == 3) {
                    throw new IllegalArgumentException("startActivityFromRecents: Task " + i3 + " can't be launch in the home/recents root task.");
                }
                this.mService.deferWindowLayout();
                try {
                    Task anyTaskForId = this.mRootWindowContainer.anyTaskForId(i3, 2, options, true);
                    if (anyTaskForId == null) {
                        this.mWindowManager.executeAppTransition();
                        throw new IllegalArgumentException("startActivityFromRecents: Task " + i3 + " not found.");
                    }
                    this.mActivityTaskSupervisorExt.beforeStartActivityFromRecents(anyTaskForId, options);
                    if (this.mActivityTaskSupervisorExt.exitFlexibleEmbeddedTask(anyTaskForId, options, false)) {
                        Slog.d(TAG, " cancel smart backend start task " + anyTaskForId);
                        return -96;
                    }
                    this.mActivityTaskSupervisorExt.updateRecentWindowingModeIfNeeded(anyTaskForId);
                    this.mActivityTaskSupervisorExt.updateFlexibleWindowTask(anyTaskForId, options, i);
                    if (this.mActivityTaskSupervisorExt.intercepTaskStartForFlexibleWindow(anyTaskForId, this.mService.mContext)) {
                        Slog.d(TAG, "intercepTaskStartForFlexibleWindow task " + anyTaskForId.toString());
                        WindowManagerService.resetPriorityAfterLockedSection();
                        return -96;
                    }
                    if (this.mActivityTaskSupervisorExt.interceptRecentStartForAsyncRotation(anyTaskForId, options, isCallerRecents)) {
                        Slog.d(TAG, "Async rotation not finished, intercept recent task start.");
                        WindowManagerService.resetPriorityAfterLockedSection();
                        return -96;
                    }
                    this.mActivityTaskSupervisorExt.setStartRecentsReason(options, isCallerRecents);
                    if (this.mActivityTaskSupervisorExt.isRunningDisallowed(anyTaskForId.getTopNonFinishingActivity(), this.mWindowManager)) {
                        WindowManagerService.resetPriorityAfterLockedSection();
                        return 0;
                    }
                    if (z && !anyTaskForId.getWrapper().getExtImpl().isFlexibleWindowScenario(options) && !anyTaskForId.getWrapper().getExtImpl().isStartZoomFormFloatScenario(options)) {
                        this.mRootWindowContainer.getDefaultTaskDisplayArea().moveHomeRootTaskToFront("startActivityFromRecents");
                    }
                    if (this.mActivityTaskSupervisorExt.startActivityFromRecents(anyTaskForId, options)) {
                        WindowManagerService.resetPriorityAfterLockedSection();
                        return 0;
                    }
                    this.mActivityTaskSupervisorExt.startActivityFromRecents(anyTaskForId);
                    if (!this.mService.mAmInternal.shouldConfirmCredentials(anyTaskForId.mUserId) && anyTaskForId.getRootActivity() != null) {
                        ActivityRecord topNonFinishingActivity = anyTaskForId.getTopNonFinishingActivity();
                        this.mRootWindowContainer.startPowerModeLaunchIfNeeded(true, topNonFinishingActivity);
                        ActivityMetricsLogger.LaunchingState notifyActivityLaunching = this.mActivityMetricsLogger.notifyActivityLaunching(anyTaskForId.intent, null, isCallerRecents ? -1 : i2);
                        try {
                            this.mActivityTaskSupervisorExt.removePuttTask(anyTaskForId);
                            if (!ActivityTaskManagerService.LTW_DISABLE) {
                                this.mService.getWrapper().getExtImpl().getRemoteTaskManager().interceptFromRecents(anyTaskForId, topNonFinishingActivity.intent);
                            }
                            this.mService.moveTaskToFrontLocked(null, null, anyTaskForId.mTaskId, 0, safeActivityOptions);
                            if (options != null && options.getAnimationType() == 13) {
                                topNonFinishingActivity.mPendingRemoteAnimation = options.getRemoteAnimationAdapter();
                            }
                            topNonFinishingActivity.applyOptionsAnimation();
                            if (options != null && options.getLaunchCookie() != null) {
                                topNonFinishingActivity.mLaunchCookie = options.getLaunchCookie();
                            }
                            this.mActivityMetricsLogger.notifyActivityLaunched(notifyActivityLaunching, 2, false, topNonFinishingActivity, options);
                            this.mService.getActivityStartController().postStartActivityProcessingForLastStarter(anyTaskForId.getTopNonFinishingActivity(), 2, anyTaskForId.getRootTask());
                            this.mService.resumeAppSwitches();
                            WindowManagerService.resetPriorityAfterLockedSection();
                            return 2;
                        } catch (Throwable th) {
                            this.mActivityMetricsLogger.notifyActivityLaunched(notifyActivityLaunching, 2, false, topNonFinishingActivity, options);
                            throw th;
                        }
                    }
                    int i5 = anyTaskForId.mCallingUid;
                    String str = anyTaskForId.mCallingPackage;
                    String str2 = anyTaskForId.mCallingFeatureId;
                    Intent intent = anyTaskForId.intent;
                    intent.addFlags(1048576);
                    int i6 = anyTaskForId.mUserId;
                    WindowManagerService.resetPriorityAfterLockedSection();
                    try {
                        ThreadLocal threadLocal = PackageManagerServiceUtils.DISABLE_ENFORCE_INTENTS_TO_MATCH_INTENT_FILTERS;
                        threadLocal.set(Boolean.TRUE);
                        int startActivityInPackage = this.mService.getActivityStartController().startActivityInPackage(i5, i, i2, str, str2, intent, intent.resolveType(this.mService.mContext.getContentResolver()), null, null, 0, 0, safeActivityOptions, i6, anyTaskForId, "startActivityFromRecents", false, null, BackgroundStartPrivileges.NONE);
                        threadLocal.set(Boolean.FALSE);
                        WindowManagerGlobalLock windowManagerGlobalLock2 = this.mService.mGlobalLock;
                        WindowManagerService.boostPriorityForLockedSection();
                        synchronized (windowManagerGlobalLock2) {
                            try {
                            } finally {
                            }
                        }
                        WindowManagerService.resetPriorityAfterLockedSection();
                        if (!ActivityTaskManagerService.LTW_DISABLE) {
                            this.mService.getWrapper().getExtImpl().getRemoteTaskManager().resetSession();
                        }
                        return startActivityInPackage;
                    } catch (Throwable th2) {
                        PackageManagerServiceUtils.DISABLE_ENFORCE_INTENTS_TO_MATCH_INTENT_FILTERS.set(Boolean.FALSE);
                        WindowManagerGlobalLock windowManagerGlobalLock3 = this.mService.mGlobalLock;
                        WindowManagerService.boostPriorityForLockedSection();
                        synchronized (windowManagerGlobalLock3) {
                            try {
                                WindowManagerService.resetPriorityAfterLockedSection();
                                if (!ActivityTaskManagerService.LTW_DISABLE) {
                                    this.mService.getWrapper().getExtImpl().getRemoteTaskManager().resetSession();
                                }
                                throw th2;
                            } finally {
                                WindowManagerService.resetPriorityAfterLockedSection();
                            }
                        }
                    }
                } finally {
                    this.mService.continueWindowLayout();
                }
            } finally {
                WindowManagerService.resetPriorityAfterLockedSection();
            }
        }
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
    static class OpaqueActivityHelper implements Predicate<ActivityRecord> {
        private boolean mIncludeInvisibleAndFinishing;
        private ActivityRecord mStarting;

        OpaqueActivityHelper() {
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public ActivityRecord getOpaqueActivity(WindowContainer<?> windowContainer) {
            this.mIncludeInvisibleAndFinishing = true;
            return windowContainer.getActivity(this, true, null);
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public ActivityRecord getVisibleOpaqueActivity(WindowContainer<?> windowContainer, ActivityRecord activityRecord) {
            this.mStarting = activityRecord;
            this.mIncludeInvisibleAndFinishing = false;
            ActivityRecord activity = windowContainer.getActivity(this, true, null);
            this.mStarting = null;
            return activity;
        }

        @Override // java.util.function.Predicate
        public boolean test(ActivityRecord activityRecord) {
            boolean z = this.mIncludeInvisibleAndFinishing;
            if (z || activityRecord.visibleIgnoringKeyguard || activityRecord == this.mStarting) {
                return activityRecord.occludesParent(z);
            }
            return false;
        }
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
    static class TaskInfoHelper implements Consumer<ActivityRecord> {
        private TaskInfo mInfo;
        private ActivityRecord mTopRunning;

        TaskInfoHelper() {
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public ActivityRecord fillAndReturnTop(Task task, TaskInfo taskInfo) {
            taskInfo.numActivities = 0;
            taskInfo.baseActivity = null;
            this.mInfo = taskInfo;
            task.forAllActivities(this);
            ActivityRecord activityRecord = this.mTopRunning;
            this.mTopRunning = null;
            this.mInfo = null;
            return activityRecord;
        }

        @Override // java.util.function.Consumer
        public void accept(ActivityRecord activityRecord) {
            IBinder iBinder = activityRecord.mLaunchCookie;
            if (iBinder != null) {
                this.mInfo.addLaunchCookie(iBinder);
            }
            if (activityRecord.finishing) {
                return;
            }
            TaskInfo taskInfo = this.mInfo;
            taskInfo.numActivities++;
            taskInfo.baseActivity = activityRecord.mActivityComponent;
            if (this.mTopRunning == null) {
                this.mTopRunning = activityRecord;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
    public static class WaitInfo {
        final ActivityMetricsLogger.LaunchingState mLaunchingState;
        final WaitResult mResult;
        final ComponentName mTargetComponent;

        WaitInfo(WaitResult waitResult, ComponentName componentName, ActivityMetricsLogger.LaunchingState launchingState) {
            this.mResult = waitResult;
            this.mTargetComponent = componentName;
            this.mLaunchingState = launchingState;
        }

        boolean matches(ActivityRecord activityRecord) {
            if (!this.mLaunchingState.hasActiveTransitionInfo()) {
                return this.mTargetComponent.equals(activityRecord.mActivityComponent);
            }
            return this.mLaunchingState.contains(activityRecord);
        }

        void dump(PrintWriter printWriter, String str) {
            printWriter.println(str + "WaitInfo:");
            printWriter.println(str + "  mTargetComponent=" + this.mTargetComponent);
            StringBuilder sb = new StringBuilder();
            sb.append(str);
            sb.append("  mResult=");
            printWriter.println(sb.toString());
            this.mResult.dump(printWriter, str + "    ");
        }
    }

    public IActivityTaskSupervisorWrapper getWrapper() {
        return this.mATSWrapper;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
    public class ActivityTaskSupervisorWrapper implements IActivityTaskSupervisorWrapper {
        private ActivityTaskSupervisorWrapper() {
        }

        @Override // com.android.server.wm.IActivityTaskSupervisorWrapper
        public IActivityTaskSupervisorExt getExtImpl() {
            return ActivityTaskSupervisor.this.mActivityTaskSupervisorExt;
        }

        @Override // com.android.server.wm.IActivityTaskSupervisorWrapper
        public ResolveInfo resolveIntent(Intent intent, String str, int i, int i2, int i3, int i4) {
            return ActivityTaskSupervisor.this.resolveIntent(intent, str, i, i2, i3, i4);
        }
    }
}
