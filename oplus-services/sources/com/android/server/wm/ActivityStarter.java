package com.android.server.wm;

import android.app.ActivityManager;
import android.app.ActivityOptions;
import android.app.BackgroundStartPrivileges;
import android.app.IApplicationThread;
import android.app.ProfilerInfo;
import android.app.WaitResult;
import android.content.ComponentName;
import android.content.Context;
import android.content.IIntentSender;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.AuxiliaryResolveInfo;
import android.content.pm.PackageManagerInternal;
import android.content.pm.ResolveInfo;
import android.content.pm.UserInfo;
import android.content.res.Configuration;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Process;
import android.os.RemoteException;
import android.os.Trace;
import android.os.UserHandle;
import android.os.UserManager;
import android.service.voice.IVoiceInteractionSession;
import android.text.TextUtils;
import android.util.Pair;
import android.util.Pools;
import android.util.Slog;
import android.view.RemoteAnimationAdapter;
import android.widget.Toast;
import android.window.RemoteTransition;
import android.window.WindowContainerToken;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.app.HeavyWeightSwitcherActivity;
import com.android.internal.app.IVoiceInteractor;
import com.android.internal.protolog.ProtoLogGroup;
import com.android.internal.protolog.ProtoLogImpl;
import com.android.internal.util.FrameworkStatsLog;
import com.android.server.UiThread;
import com.android.server.am.PendingIntentRecord;
import com.android.server.pm.InstantAppResolver;
import com.android.server.power.ShutdownCheckPoints;
import com.android.server.statusbar.StatusBarManagerInternal;
import com.android.server.uri.NeededUriGrants;
import com.android.server.uri.UriGrantsManagerInternal;
import com.android.server.wm.ActivityMetricsLogger;
import com.android.server.wm.ActivityRecord;
import com.android.server.wm.LaunchParamsController;
import com.android.server.wm.TaskFragment;
import com.android.server.zenmode.IZenModeManagerExt;
import com.oplus.uifirst.IOplusUIFirstManagerExt;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.StringJoiner;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import system.ext.loader.core.ExtLoader;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
public class ActivityStarter {
    static final long ASM_RESTRICTIONS = 230590090;
    static final long ENABLE_PENDING_INTENT_BAL_OPTION = 192341120;
    private static final int INVALID_LAUNCH_MODE = -1;
    private static final long MAX_TASK_WEIGHT_FOR_ADDING_ACTIVITY = 300;

    @VisibleForTesting
    boolean mAddingToTask;
    private TaskFragment mAddingToTaskFragment;
    private boolean mAvoidMoveToFront;
    private int mBalCode;
    private int mCallingUid;
    private final ActivityStartController mController;
    private boolean mDisplayLockAndOccluded;
    private boolean mDoResume;
    private boolean mFrozeTaskList;
    private Task mInTask;
    private TaskFragment mInTaskFragment;
    private Intent mIntent;
    private boolean mIntentDelivered;
    private final ActivityStartInterceptor mInterceptor;
    private boolean mIsTaskCleared;
    private ActivityRecord mLastStartActivityRecord;
    private int mLastStartActivityResult;
    private long mLastStartActivityTimeMs;
    private String mLastStartReason;
    private int mLaunchFlags;
    private int mLaunchMode;
    private boolean mLaunchTaskBehind;
    private boolean mMovedToFront;

    @VisibleForTesting
    ActivityRecord mMovedToTopActivity;
    private boolean mNoAnimation;
    private ActivityRecord mNotTop;
    private ActivityOptions mOptions;
    private TaskDisplayArea mPreferredTaskDisplayArea;
    private int mPreferredWindowingMode;
    private Task mPriorAboveTask;
    private int mRealCallingUid;
    private final RootWindowContainer mRootWindowContainer;
    private final ActivityTaskManagerService mService;
    private ActivityRecord mSourceRecord;
    private Task mSourceRootTask;

    @VisibleForTesting
    ActivityRecord mStartActivity;
    private int mStartFlags;
    private final ActivityTaskSupervisor mSupervisor;
    private Task mTargetRootTask;
    private Task mTargetTask;
    private boolean mTransientLaunch;
    private IVoiceInteractor mVoiceInteractor;
    private IVoiceInteractionSession mVoiceSession;
    private static final String TAG = "ActivityTaskManager";
    private static final String TAG_RESULTS = TAG + ActivityTaskManagerDebugConfig.POSTFIX_RESULTS;
    private static final String TAG_FOCUS = TAG + ActivityTaskManagerDebugConfig.POSTFIX_FOCUS;
    private static final String TAG_CONFIGURATION = TAG + ActivityTaskManagerDebugConfig.POSTFIX_CONFIGURATION;
    private static final String TAG_USER_LEAVING = TAG + ActivityTaskManagerDebugConfig.POSTFIX_USER_LEAVING;
    private LaunchParamsController.LaunchParams mLaunchParams = new LaunchParamsController.LaunchParams();
    public IZenModeManagerExt mZenModeManagerExt = (IZenModeManagerExt) ExtLoader.type(IZenModeManagerExt.class).create();
    private IOplusUIFirstManagerExt mUIFirstManagerExt = (IOplusUIFirstManagerExt) ExtLoader.type(IOplusUIFirstManagerExt.class).create();

    @VisibleForTesting
    Request mRequest = new Request();
    private ActivityStarterWrapper mASWrapper = new ActivityStarterWrapper();

    /* JADX INFO: Access modifiers changed from: package-private */
    @VisibleForTesting
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
    public interface Factory {
        ActivityStarter obtain();

        void recycle(ActivityStarter activityStarter);

        void setController(ActivityStartController activityStartController);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int computeResolveFilterUid(int i, int i2, int i3) {
        return i3 != -10000 ? i3 : i >= 0 ? i : i2;
    }

    static int getExternalResult(int i) {
        if (i != 102) {
            return i;
        }
        return 0;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static boolean isDocumentLaunchesIntoExisting(int i) {
        return (524288 & i) != 0 && (i & 134217728) == 0;
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
    static class DefaultFactory implements Factory {
        private ActivityStartController mController;
        private ActivityStartInterceptor mInterceptor;
        private ActivityTaskManagerService mService;
        private ActivityTaskSupervisor mSupervisor;
        private final int MAX_STARTER_COUNT = 3;
        private Pools.SynchronizedPool<ActivityStarter> mStarterPool = new Pools.SynchronizedPool<>(3);

        /* JADX INFO: Access modifiers changed from: package-private */
        public DefaultFactory(ActivityTaskManagerService activityTaskManagerService, ActivityTaskSupervisor activityTaskSupervisor, ActivityStartInterceptor activityStartInterceptor) {
            this.mService = activityTaskManagerService;
            this.mSupervisor = activityTaskSupervisor;
            this.mInterceptor = activityStartInterceptor;
        }

        @Override // com.android.server.wm.ActivityStarter.Factory
        public void setController(ActivityStartController activityStartController) {
            this.mController = activityStartController;
        }

        @Override // com.android.server.wm.ActivityStarter.Factory
        public ActivityStarter obtain() {
            ActivityStarter activityStarter = (ActivityStarter) this.mStarterPool.acquire();
            if (activityStarter != null) {
                return activityStarter;
            }
            if (this.mService.mRootWindowContainer == null) {
                throw new IllegalStateException("Too early to start activity.");
            }
            return new ActivityStarter(this.mController, this.mService, this.mSupervisor, this.mInterceptor);
        }

        @Override // com.android.server.wm.ActivityStarter.Factory
        public void recycle(ActivityStarter activityStarter) {
            activityStarter.reset(true);
            this.mStarterPool.release(activityStarter);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @VisibleForTesting
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
    public static class Request {
        private static final int DEFAULT_CALLING_PID = 0;
        private static final int DEFAULT_CALLING_UID = -1;
        static final int DEFAULT_REAL_CALLING_PID = 0;
        static final int DEFAULT_REAL_CALLING_UID = -1;
        ActivityInfo activityInfo;
        SafeActivityOptions activityOptions;
        boolean allowPendingRemoteAnimationRegistryLookup;
        boolean avoidMoveToFront;
        BackgroundStartPrivileges backgroundStartPrivileges;
        IApplicationThread caller;
        String callingFeatureId;
        String callingPackage;
        boolean componentSpecified;
        Intent ephemeralIntent;
        IBinder errorCallbackToken;
        int filterCallingUid;
        Configuration globalConfig;
        boolean ignoreTargetSecurity;
        Task inTask;
        TaskFragment inTaskFragment;
        Intent intent;
        NeededUriGrants intentGrants;
        PendingIntentRecord originatingPendingIntent;
        ActivityRecord[] outActivity;
        ProfilerInfo profilerInfo;
        String reason;
        int requestCode;
        ResolveInfo resolveInfo;
        String resolvedType;
        IBinder resultTo;
        String resultWho;
        int startFlags;
        int userId;
        IVoiceInteractor voiceInteractor;
        IVoiceInteractionSession voiceSession;
        WaitResult waitResult;
        int callingPid = 0;
        int callingUid = -1;
        int realCallingPid = 0;
        int realCallingUid = -1;
        final StringBuilder logMessage = new StringBuilder();

        Request() {
            reset();
        }

        void reset() {
            this.caller = null;
            this.intent = null;
            this.intentGrants = null;
            this.ephemeralIntent = null;
            this.resolvedType = null;
            this.activityInfo = null;
            this.resolveInfo = null;
            this.voiceSession = null;
            this.voiceInteractor = null;
            this.resultTo = null;
            this.resultWho = null;
            this.requestCode = 0;
            this.callingPid = 0;
            this.callingUid = -1;
            this.callingPackage = null;
            this.callingFeatureId = null;
            this.realCallingPid = 0;
            this.realCallingUid = -1;
            this.startFlags = 0;
            this.activityOptions = null;
            this.ignoreTargetSecurity = false;
            this.componentSpecified = false;
            this.outActivity = null;
            this.inTask = null;
            this.inTaskFragment = null;
            this.reason = null;
            this.profilerInfo = null;
            this.globalConfig = null;
            this.userId = 0;
            this.waitResult = null;
            this.avoidMoveToFront = false;
            this.allowPendingRemoteAnimationRegistryLookup = true;
            this.filterCallingUid = -10000;
            this.originatingPendingIntent = null;
            this.backgroundStartPrivileges = BackgroundStartPrivileges.NONE;
            this.errorCallbackToken = null;
        }

        void set(Request request) {
            this.caller = request.caller;
            this.intent = request.intent;
            this.intentGrants = request.intentGrants;
            this.ephemeralIntent = request.ephemeralIntent;
            this.resolvedType = request.resolvedType;
            this.activityInfo = request.activityInfo;
            this.resolveInfo = request.resolveInfo;
            this.voiceSession = request.voiceSession;
            this.voiceInteractor = request.voiceInteractor;
            this.resultTo = request.resultTo;
            this.resultWho = request.resultWho;
            this.requestCode = request.requestCode;
            this.callingPid = request.callingPid;
            this.callingUid = request.callingUid;
            this.callingPackage = request.callingPackage;
            this.callingFeatureId = request.callingFeatureId;
            this.realCallingPid = request.realCallingPid;
            this.realCallingUid = request.realCallingUid;
            this.startFlags = request.startFlags;
            this.activityOptions = request.activityOptions;
            this.ignoreTargetSecurity = request.ignoreTargetSecurity;
            this.componentSpecified = request.componentSpecified;
            this.outActivity = request.outActivity;
            this.inTask = request.inTask;
            this.inTaskFragment = request.inTaskFragment;
            this.reason = request.reason;
            this.profilerInfo = request.profilerInfo;
            this.globalConfig = request.globalConfig;
            this.userId = request.userId;
            this.waitResult = request.waitResult;
            this.avoidMoveToFront = request.avoidMoveToFront;
            this.allowPendingRemoteAnimationRegistryLookup = request.allowPendingRemoteAnimationRegistryLookup;
            this.filterCallingUid = request.filterCallingUid;
            this.originatingPendingIntent = request.originatingPendingIntent;
            this.backgroundStartPrivileges = request.backgroundStartPrivileges;
            this.errorCallbackToken = request.errorCallbackToken;
        }

        /* JADX WARN: Removed duplicated region for block: B:56:0x012d  */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        void resolveActivity(ActivityTaskSupervisor activityTaskSupervisor) {
            UserInfo userInfo;
            boolean z;
            activityTaskSupervisor.getWrapper().getExtImpl().resolveActivity(this.intent);
            if (this.realCallingPid == 0) {
                this.realCallingPid = Binder.getCallingPid();
            }
            if (this.realCallingUid == -1) {
                this.realCallingUid = Binder.getCallingUid();
            }
            if (this.callingUid >= 0) {
                this.callingPid = -1;
            } else if (this.caller == null) {
                this.callingPid = this.realCallingPid;
                this.callingUid = this.realCallingUid;
            } else {
                this.callingUid = -1;
                this.callingPid = -1;
            }
            int i = this.callingUid;
            if (this.caller != null) {
                WindowManagerGlobalLock windowManagerGlobalLock = activityTaskSupervisor.mService.mGlobalLock;
                WindowManagerService.boostPriorityForLockedSection();
                synchronized (windowManagerGlobalLock) {
                    try {
                        WindowProcessController processController = activityTaskSupervisor.mService.getProcessController(this.caller);
                        if (processController != null) {
                            i = processController.mInfo.uid;
                        }
                    } catch (Throwable th) {
                        WindowManagerService.resetPriorityAfterLockedSection();
                        throw th;
                    }
                }
                WindowManagerService.resetPriorityAfterLockedSection();
            }
            int resolvedCallingUid = activityTaskSupervisor.getWrapper().getExtImpl().resolvedCallingUid(ActivityRecord.forTokenLocked(this.resultTo), this.intent, i);
            this.ephemeralIntent = new Intent(this.intent);
            Intent intent = new Intent(this.intent);
            this.intent = intent;
            if (intent.getComponent() != null && ((!"android.intent.action.VIEW".equals(this.intent.getAction()) || this.intent.getData() != null) && !"android.intent.action.INSTALL_INSTANT_APP_PACKAGE".equals(this.intent.getAction()) && !"android.intent.action.RESOLVE_INSTANT_APP_PACKAGE".equals(this.intent.getAction()) && activityTaskSupervisor.mService.getPackageManagerInternalLocked().isInstantAppInstallerComponent(this.intent.getComponent()))) {
                this.intent.setComponent(null);
            }
            ResolveInfo resolveIntent = activityTaskSupervisor.resolveIntent(this.intent, this.resolvedType, this.userId, 0, ActivityStarter.computeResolveFilterUid(this.callingUid, this.realCallingUid, this.filterCallingUid), this.realCallingPid);
            this.resolveInfo = resolveIntent;
            if (resolveIntent == null && (userInfo = activityTaskSupervisor.getUserInfo(this.userId)) != null && userInfo.isManagedProfile()) {
                UserManager userManager = UserManager.get(activityTaskSupervisor.mService.mContext);
                long clearCallingIdentity = Binder.clearCallingIdentity();
                try {
                    UserInfo profileParent = userManager.getProfileParent(this.userId);
                    if (profileParent != null && userManager.isUserUnlockingOrUnlocked(profileParent.id)) {
                        if (!userManager.isUserUnlockingOrUnlocked(this.userId)) {
                            z = true;
                            if (z) {
                                this.resolveInfo = activityTaskSupervisor.resolveIntent(this.intent, this.resolvedType, this.userId, 786432, ActivityStarter.computeResolveFilterUid(this.callingUid, this.realCallingUid, this.filterCallingUid), this.realCallingPid);
                            }
                        }
                    }
                    z = false;
                    if (z) {
                    }
                } finally {
                    Binder.restoreCallingIdentity(clearCallingIdentity);
                }
            }
            ResolveInfo multiAppResolveInfoIfNeed = activityTaskSupervisor.getWrapper().getExtImpl().getMultiAppResolveInfoIfNeed(this.resolveInfo, this.userId, activityTaskSupervisor, this.intent, this.resolvedType, ActivityStarter.computeResolveFilterUid(this.callingUid, this.realCallingUid, this.filterCallingUid), this.callingPid);
            this.resolveInfo = multiAppResolveInfoIfNeed;
            ActivityInfo resolveActivity = activityTaskSupervisor.resolveActivity(this.intent, multiAppResolveInfoIfNeed, this.startFlags, this.profilerInfo);
            this.activityInfo = resolveActivity;
            if (resolveActivity != null) {
                UriGrantsManagerInternal uriGrantsManagerInternal = activityTaskSupervisor.mService.mUgmInternal;
                Intent intent2 = this.intent;
                ApplicationInfo applicationInfo = resolveActivity.applicationInfo;
                this.intentGrants = uriGrantsManagerInternal.checkGrantUriPermissionFromIntent(intent2, resolvedCallingUid, applicationInfo.packageName, UserHandle.getUserId(applicationInfo.uid));
            }
        }
    }

    ActivityStarter(ActivityStartController activityStartController, ActivityTaskManagerService activityTaskManagerService, ActivityTaskSupervisor activityTaskSupervisor, ActivityStartInterceptor activityStartInterceptor) {
        this.mController = activityStartController;
        this.mService = activityTaskManagerService;
        this.mRootWindowContainer = activityTaskManagerService.mRootWindowContainer;
        this.mSupervisor = activityTaskSupervisor;
        this.mInterceptor = activityStartInterceptor;
        reset(true);
        this.mASWrapper.getSocExtImpl().initSoc();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void set(ActivityStarter activityStarter) {
        this.mStartActivity = activityStarter.mStartActivity;
        this.mIntent = activityStarter.mIntent;
        this.mCallingUid = activityStarter.mCallingUid;
        this.mRealCallingUid = activityStarter.mRealCallingUid;
        this.mOptions = activityStarter.mOptions;
        this.mBalCode = activityStarter.mBalCode;
        this.mLaunchTaskBehind = activityStarter.mLaunchTaskBehind;
        this.mLaunchFlags = activityStarter.mLaunchFlags;
        this.mLaunchMode = activityStarter.mLaunchMode;
        this.mLaunchParams.set(activityStarter.mLaunchParams);
        this.mNotTop = activityStarter.mNotTop;
        this.mDoResume = activityStarter.mDoResume;
        this.mStartFlags = activityStarter.mStartFlags;
        this.mSourceRecord = activityStarter.mSourceRecord;
        this.mPreferredTaskDisplayArea = activityStarter.mPreferredTaskDisplayArea;
        this.mPreferredWindowingMode = activityStarter.mPreferredWindowingMode;
        this.mInTask = activityStarter.mInTask;
        this.mInTaskFragment = activityStarter.mInTaskFragment;
        this.mAddingToTask = activityStarter.mAddingToTask;
        this.mSourceRootTask = activityStarter.mSourceRootTask;
        this.mTargetTask = activityStarter.mTargetTask;
        this.mTargetRootTask = activityStarter.mTargetRootTask;
        this.mIsTaskCleared = activityStarter.mIsTaskCleared;
        this.mMovedToFront = activityStarter.mMovedToFront;
        this.mNoAnimation = activityStarter.mNoAnimation;
        this.mAvoidMoveToFront = activityStarter.mAvoidMoveToFront;
        this.mFrozeTaskList = activityStarter.mFrozeTaskList;
        this.mVoiceSession = activityStarter.mVoiceSession;
        this.mVoiceInteractor = activityStarter.mVoiceInteractor;
        this.mIntentDelivered = activityStarter.mIntentDelivered;
        this.mLastStartActivityResult = activityStarter.mLastStartActivityResult;
        this.mLastStartActivityTimeMs = activityStarter.mLastStartActivityTimeMs;
        this.mLastStartReason = activityStarter.mLastStartReason;
        this.mRequest.set(activityStarter.mRequest);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean relatedToPackage(String str) {
        ActivityRecord activityRecord;
        ActivityRecord activityRecord2 = this.mLastStartActivityRecord;
        return (activityRecord2 != null && str.equals(activityRecord2.packageName)) || ((activityRecord = this.mStartActivity) != null && str.equals(activityRecord.packageName));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: Finally extract failed */
    public int execute() {
        try {
            this.mASWrapper.getExtImpl().hooksetUxThreadValue(Process.myPid(), Process.myTid(), "1", this.mRequest.resultTo);
            onExecutionStarted();
            Intent intent = this.mRequest.intent;
            if (intent != null && intent.hasFileDescriptors()) {
                throw new IllegalArgumentException("File descriptors passed in Intent");
            }
            WindowManagerGlobalLock windowManagerGlobalLock = this.mService.mGlobalLock;
            WindowManagerService.boostPriorityForLockedSection();
            synchronized (windowManagerGlobalLock) {
                try {
                    ActivityRecord forTokenLocked = ActivityRecord.forTokenLocked(this.mRequest.resultTo);
                    int i = this.mRequest.realCallingUid;
                    if (i == -1) {
                        i = Binder.getCallingUid();
                    }
                    ActivityMetricsLogger.LaunchingState notifyActivityLaunching = this.mSupervisor.getActivityMetricsLogger().notifyActivityLaunching(this.mRequest.intent, forTokenLocked, i);
                    if (this.mASWrapper.getExtImpl().interceptStartForAsyncRotation(forTokenLocked, this.mRequest.intent)) {
                        Slog.d(TAG, "Can not start the launcher because the async rotation is not finished.");
                        WindowManagerService.resetPriorityAfterLockedSection();
                        onExecutionComplete();
                        this.mASWrapper.getExtImpl().hooksetUxThreadValue(Process.myPid(), Process.myTid(), "0", this.mRequest.resultTo);
                        return -96;
                    }
                    WindowManagerService.resetPriorityAfterLockedSection();
                    IActivityStarterExt extImpl = this.mASWrapper.getExtImpl();
                    Request request = this.mRequest;
                    extImpl.interceptInitiatorForAppDetails(request.callingPackage, request.intent, request.realCallingUid);
                    IActivityStarterExt extImpl2 = this.mASWrapper.getExtImpl();
                    ActivityTaskSupervisor activityTaskSupervisor = this.mSupervisor;
                    Request request2 = this.mRequest;
                    extImpl2.handlerIntentForAppDetails(activityTaskSupervisor, request2.callingPackage, request2.callingUid, request2.userId, request2.intent, request2.resolvedType);
                    Request request3 = this.mRequest;
                    if (request3.activityInfo == null) {
                        request3.resolveActivity(this.mSupervisor);
                    }
                    int i2 = 0;
                    if (!this.mASWrapper.getExtImpl().executeBeforeShutdownCheck(this.mSupervisor, this.mRequest)) {
                        this.mASWrapper.getExtImpl().resetStartRecentFlag(this.mOptions);
                        Intent intent2 = this.mRequest.intent;
                        if (intent2 != null) {
                            String action = intent2.getAction();
                            String str = this.mRequest.callingPackage;
                            if (action != null && str != null && ("com.android.internal.intent.action.REQUEST_SHUTDOWN".equals(action) || "android.intent.action.ACTION_SHUTDOWN".equals(action) || "android.intent.action.REBOOT".equals(action))) {
                                ShutdownCheckPoints.recordCheckPoint(action, str, (String) null);
                            }
                        }
                        if (!this.mASWrapper.getExtImpl().executeAfterShutdownCheck(this.mRequest)) {
                            IActivityStarterExt extImpl3 = this.mASWrapper.getExtImpl();
                            Request request4 = this.mRequest;
                            if (!extImpl3.interceptStartForActiveSplitScreen(request4.intent, request4.activityOptions, request4.callingPackage) && !this.mASWrapper.getExtImpl().replaceActivityStartFromLab(this.mRequest)) {
                                WindowManagerGlobalLock windowManagerGlobalLock2 = this.mService.mGlobalLock;
                                WindowManagerService.boostPriorityForLockedSection();
                                synchronized (windowManagerGlobalLock2) {
                                    try {
                                        boolean z = (this.mRequest.globalConfig == null || this.mService.getGlobalConfiguration().diff(this.mRequest.globalConfig) == 0) ? false : true;
                                        Task topDisplayFocusedRootTask = this.mRootWindowContainer.getTopDisplayFocusedRootTask();
                                        if (topDisplayFocusedRootTask != null) {
                                            topDisplayFocusedRootTask.mConfigWillChange = z;
                                        }
                                        if (ProtoLogCache.WM_DEBUG_CONFIGURATION_enabled) {
                                            ProtoLogImpl.v(ProtoLogGroup.WM_DEBUG_CONFIGURATION, -1492881555, 3, (String) null, new Object[]{Boolean.valueOf(z)});
                                        }
                                        long clearCallingIdentity = Binder.clearCallingIdentity();
                                        int resolveToHeavyWeightSwitcherIfNeeded = resolveToHeavyWeightSwitcherIfNeeded();
                                        if (resolveToHeavyWeightSwitcherIfNeeded != 0) {
                                            WindowManagerService.resetPriorityAfterLockedSection();
                                            onExecutionComplete();
                                            this.mASWrapper.getExtImpl().hooksetUxThreadValue(Process.myPid(), Process.myTid(), "0", this.mRequest.resultTo);
                                            return resolveToHeavyWeightSwitcherIfNeeded;
                                        }
                                        try {
                                            int executeRequest = executeRequest(this.mRequest);
                                            StringBuilder sb = this.mRequest.logMessage;
                                            sb.append(" result code=");
                                            sb.append(executeRequest);
                                            Slog.i(TAG, this.mRequest.logMessage.toString());
                                            this.mRequest.logMessage.setLength(0);
                                            Binder.restoreCallingIdentity(clearCallingIdentity);
                                            if (z) {
                                                this.mService.mAmInternal.enforceCallingPermission("android.permission.CHANGE_CONFIGURATION", "updateConfiguration()");
                                                if (topDisplayFocusedRootTask != null) {
                                                    topDisplayFocusedRootTask.mConfigWillChange = false;
                                                }
                                                if (ProtoLogCache.WM_DEBUG_CONFIGURATION_enabled) {
                                                    ProtoLogImpl.v(ProtoLogGroup.WM_DEBUG_CONFIGURATION, -1868048288, 0, (String) null, (Object[]) null);
                                                }
                                                this.mService.updateConfigurationLocked(this.mRequest.globalConfig, null, false);
                                            }
                                            SafeActivityOptions safeActivityOptions = this.mRequest.activityOptions;
                                            ActivityOptions originalOptions = safeActivityOptions != null ? safeActivityOptions.getOriginalOptions() : null;
                                            ActivityRecord activityRecord = this.mDoResume ? this.mLastStartActivityRecord : null;
                                            boolean z2 = this.mStartActivity == activityRecord;
                                            this.mASWrapper.getExtImpl().boostLaunchActivity(this.mService.mHomeProcess, this.mRequest.activityInfo);
                                            this.mSupervisor.getActivityMetricsLogger().notifyActivityLaunched(notifyActivityLaunching, executeRequest, z2, activityRecord, originalOptions);
                                            WaitResult waitResult = this.mRequest.waitResult;
                                            if (waitResult != null) {
                                                waitResult.result = executeRequest;
                                                executeRequest = waitResultIfNeeded(waitResult, this.mLastStartActivityRecord, notifyActivityLaunching);
                                            }
                                            i2 = getExternalResult(executeRequest);
                                        } catch (Throwable th) {
                                            StringBuilder sb2 = this.mRequest.logMessage;
                                            sb2.append(" result code=");
                                            sb2.append(resolveToHeavyWeightSwitcherIfNeeded);
                                            Slog.i(TAG, this.mRequest.logMessage.toString());
                                            this.mRequest.logMessage.setLength(0);
                                            throw th;
                                        }
                                    } catch (Throwable th2) {
                                        WindowManagerService.resetPriorityAfterLockedSection();
                                        throw th2;
                                    }
                                }
                            }
                        }
                    }
                    onExecutionComplete();
                    this.mASWrapper.getExtImpl().hooksetUxThreadValue(Process.myPid(), Process.myTid(), "0", this.mRequest.resultTo);
                    return i2;
                } finally {
                    WindowManagerService.resetPriorityAfterLockedSection();
                }
            }
        } catch (Throwable th3) {
            onExecutionComplete();
            this.mASWrapper.getExtImpl().hooksetUxThreadValue(Process.myPid(), Process.myTid(), "0", this.mRequest.resultTo);
            throw th3;
        }
    }

    private int resolveToHeavyWeightSwitcherIfNeeded() {
        WindowProcessController windowProcessController;
        ActivityInfo activityInfo = this.mRequest.activityInfo;
        if (activityInfo != null && this.mService.mHasHeavyWeightFeature) {
            ApplicationInfo applicationInfo = activityInfo.applicationInfo;
            if ((applicationInfo.privateFlags & 2) != 0 && activityInfo.processName.equals(applicationInfo.packageName) && (windowProcessController = this.mService.mHeavyWeightProcess) != null) {
                int i = windowProcessController.mInfo.uid;
                ActivityInfo activityInfo2 = this.mRequest.activityInfo;
                if (i != activityInfo2.applicationInfo.uid || !windowProcessController.mName.equals(activityInfo2.processName)) {
                    Request request = this.mRequest;
                    int i2 = request.callingUid;
                    IApplicationThread iApplicationThread = request.caller;
                    if (iApplicationThread != null) {
                        WindowProcessController processController = this.mService.getProcessController(iApplicationThread);
                        if (processController != null) {
                            i2 = processController.mInfo.uid;
                        } else {
                            Slog.w(TAG, "Unable to find app for caller " + this.mRequest.caller + " (pid=" + this.mRequest.callingPid + ") when starting: " + this.mRequest.intent.toString());
                            SafeActivityOptions.abort(this.mRequest.activityOptions);
                            return -94;
                        }
                    }
                    ActivityTaskManagerService activityTaskManagerService = this.mService;
                    Request request2 = this.mRequest;
                    IIntentSender intentSenderLocked = activityTaskManagerService.getIntentSenderLocked(2, "android", null, i2, request2.userId, null, null, 0, new Intent[]{request2.intent}, new String[]{request2.resolvedType}, 1342177280, null);
                    Intent intent = new Intent();
                    if (this.mRequest.requestCode >= 0) {
                        intent.putExtra("has_result", true);
                    }
                    intent.putExtra("intent", new IntentSender(intentSenderLocked));
                    windowProcessController.updateIntentForHeavyWeightActivity(intent);
                    intent.putExtra("new_app", this.mRequest.activityInfo.packageName);
                    intent.setFlags(this.mRequest.intent.getFlags());
                    intent.setClassName("android", HeavyWeightSwitcherActivity.class.getName());
                    Request request3 = this.mRequest;
                    request3.intent = intent;
                    request3.resolvedType = null;
                    request3.caller = null;
                    request3.callingUid = Binder.getCallingUid();
                    this.mRequest.callingPid = Binder.getCallingPid();
                    Request request4 = this.mRequest;
                    request4.componentSpecified = true;
                    request4.resolveInfo = this.mSupervisor.resolveIntent(request4.intent, null, request4.userId, 0, computeResolveFilterUid(request4.callingUid, request4.realCallingUid, request4.filterCallingUid), this.mRequest.realCallingPid);
                    Request request5 = this.mRequest;
                    ResolveInfo resolveInfo = request5.resolveInfo;
                    ActivityInfo activityInfo3 = resolveInfo != null ? resolveInfo.activityInfo : null;
                    request5.activityInfo = activityInfo3;
                    if (activityInfo3 != null) {
                        request5.activityInfo = this.mService.mAmInternal.getActivityInfoForUser(activityInfo3, request5.userId);
                    }
                }
            }
        }
        return 0;
    }

    private int waitResultIfNeeded(WaitResult waitResult, ActivityRecord activityRecord, ActivityMetricsLogger.LaunchingState launchingState) {
        int i = waitResult.result;
        if (i == 3 || (i == 2 && activityRecord.nowVisible && activityRecord.isState(ActivityRecord.State.RESUMED))) {
            waitResult.timeout = false;
            waitResult.who = activityRecord.mActivityComponent;
            waitResult.totalTime = 0L;
            return i;
        }
        this.mSupervisor.waitActivityVisibleOrLaunched(waitResult, activityRecord, launchingState);
        if (i == 0 && waitResult.result == 2) {
            return 2;
        }
        return i;
    }

    /* JADX WARN: Removed duplicated region for block: B:129:0x04eb  */
    /* JADX WARN: Removed duplicated region for block: B:132:0x0523  */
    /* JADX WARN: Removed duplicated region for block: B:135:0x054e  */
    /* JADX WARN: Removed duplicated region for block: B:141:0x05a5  */
    /* JADX WARN: Removed duplicated region for block: B:200:0x0543  */
    /* JADX WARN: Removed duplicated region for block: B:237:0x02e2  */
    /* JADX WARN: Removed duplicated region for block: B:76:0x02a8 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:85:0x02e0  */
    /* JADX WARN: Removed duplicated region for block: B:87:0x02e8  */
    /* JADX WARN: Removed duplicated region for block: B:91:0x02ff  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private int executeRequest(Request request) {
        TaskFragment taskFragment;
        Bundle bundle;
        int i;
        int i2;
        WindowProcessController windowProcessController;
        int i3;
        int i4;
        String str;
        String str2;
        int i5;
        String str3;
        IBinder iBinder;
        WindowProcessController windowProcessController2;
        int i6;
        IVoiceInteractionSession iVoiceInteractionSession;
        String str4;
        String str5;
        ActivityRecord activityRecord;
        ActivityRecord activityRecord2;
        ActivityRecord activityRecord3;
        String str6;
        int i7;
        String str7;
        String str8;
        String str9;
        int i8;
        ActivityRecord activityRecord4;
        ActivityInfo activityInfo;
        String str10;
        String str11;
        String str12;
        String str13;
        String str14;
        WindowProcessController windowProcessController3;
        ActivityInfo activityInfo2;
        ActivityOptions activityOptions;
        ActivityInfo activityInfo3;
        ActivityRecord activityRecord5;
        int i9;
        String str15;
        ActivityOptions activityOptions2;
        Intent intent;
        ActivityInfo activityInfo4;
        int i10;
        boolean z;
        Pair<Intent, ActivityInfo> checkStartActivity;
        int i11;
        ActivityOptions activityOptions3;
        ResolveInfo resolveInfo;
        String str16;
        ActivityOptions activityOptions4;
        int i12;
        String str17;
        ResolveInfo resolveInfo2;
        NeededUriGrants neededUriGrants;
        int i13;
        String str18;
        int i14;
        ActivityRecord activityRecord6;
        ActivityInfo activityInfo5;
        int i15;
        int i16;
        NeededUriGrants neededUriGrants2;
        String str19;
        WindowProcessController windowProcessController4;
        int i17;
        AuxiliaryResolveInfo auxiliaryResolveInfo;
        ResolveInfo resolveInfo3;
        Object obj;
        long j;
        ApplicationInfo applicationInfo;
        if (TextUtils.isEmpty(request.reason)) {
            throw new IllegalArgumentException("Need to specify a reason.");
        }
        if (!this.mZenModeManagerExt.canActivityGo(request.activityInfo)) {
            return 0;
        }
        this.mLastStartReason = request.reason;
        this.mLastStartActivityTimeMs = System.currentTimeMillis();
        this.mLastStartActivityRecord = null;
        IApplicationThread iApplicationThread = request.caller;
        Intent intent2 = request.intent;
        NeededUriGrants neededUriGrants3 = request.intentGrants;
        String str20 = request.resolvedType;
        ActivityInfo activityInfo6 = request.activityInfo;
        ResolveInfo resolveInfo4 = request.resolveInfo;
        IVoiceInteractionSession iVoiceInteractionSession2 = request.voiceSession;
        IBinder iBinder2 = request.resultTo;
        String str21 = request.resultWho;
        int i18 = request.requestCode;
        int i19 = request.callingPid;
        int i20 = request.callingUid;
        String str22 = request.callingPackage;
        String str23 = request.callingFeatureId;
        NeededUriGrants neededUriGrants4 = neededUriGrants3;
        int i21 = request.realCallingPid;
        int i22 = request.realCallingUid;
        int i23 = request.startFlags;
        SafeActivityOptions safeActivityOptions = request.activityOptions;
        Task task = request.inTask;
        TaskFragment taskFragment2 = request.inTaskFragment;
        if (safeActivityOptions != null) {
            taskFragment = taskFragment2;
            bundle = safeActivityOptions.popAppVerificationBundle();
        } else {
            taskFragment = taskFragment2;
            bundle = null;
        }
        if (iApplicationThread != null) {
            WindowProcessController processController = this.mService.getProcessController(iApplicationThread);
            if (processController != null) {
                i = i18;
                i2 = processController.getPid();
                i3 = 0;
                windowProcessController = processController;
                i20 = processController.mInfo.uid;
            } else {
                StringBuilder sb = new StringBuilder();
                i = i18;
                sb.append("Unable to find app for caller ");
                sb.append(iApplicationThread);
                sb.append(" (pid=");
                sb.append(i19);
                sb.append(") when starting: ");
                sb.append(intent2.toString());
                Slog.w(TAG, sb.toString());
                i2 = i19;
                i3 = -94;
                i20 = i20;
                windowProcessController = processController;
            }
        } else {
            i = i18;
            i2 = i19;
            windowProcessController = null;
            i3 = 0;
        }
        this.mASWrapper.getExtImpl().notifySysActivityStart(ActivityStarter.class, new ComponentName("", ""));
        int userId = (activityInfo6 == null || (applicationInfo = activityInfo6.applicationInfo) == null) ? 0 : UserHandle.getUserId(applicationInfo.uid);
        int i24 = activityInfo6 != null ? activityInfo6.launchMode : 0;
        int executeRequestReplaceErrCheck = this.mASWrapper.getExtImpl().executeRequestReplaceErrCheck(str22, i20, i2, i3, intent2, userId, activityInfo6, safeActivityOptions, this.mLastStartReason, i22, i21);
        if (executeRequestReplaceErrCheck == 0) {
            StringBuilder sb2 = request.logMessage;
            sb2.append("START u");
            sb2.append(userId);
            sb2.append(" {");
            int i25 = userId;
            str2 = " {";
            sb2.append(intent2.toShortString(true, true, true, false));
            sb2.append("} with ");
            sb2.append(ActivityInfo.launchModeToString(i24));
            sb2.append(" from uid ");
            sb2.append(i20);
            if (i20 != i22 && i22 != -1) {
                StringBuilder sb3 = request.logMessage;
                sb3.append(" (realCallingUid=");
                sb3.append(i22);
                sb3.append(")");
            }
            i4 = i20;
            i5 = i;
            str3 = str21;
            str4 = "START u";
            i6 = i25;
            str5 = ")";
            str = str22;
            iBinder = iBinder2;
            windowProcessController2 = windowProcessController;
            iVoiceInteractionSession = iVoiceInteractionSession2;
            this.mASWrapper.getExtImpl().executeRequestAfterErrSuccess(iApplicationThread, windowProcessController, str22, i2, i4, intent2);
        } else {
            i4 = i20;
            str = str22;
            str2 = " {";
            i5 = i;
            str3 = str21;
            iBinder = iBinder2;
            windowProcessController2 = windowProcessController;
            i6 = userId;
            iVoiceInteractionSession = iVoiceInteractionSession2;
            str4 = "START u";
            str5 = ")";
        }
        if (iBinder != null) {
            activityRecord = ActivityRecord.isInAnyTask(iBinder);
            if (ActivityTaskManagerDebugConfig.DEBUG_RESULTS) {
                Slog.v(TAG_RESULTS, "Will send result to " + iBinder + " " + activityRecord);
            }
            if (activityRecord == null || i5 < 0 || activityRecord.finishing) {
                activityRecord2 = activityRecord;
                activityRecord = null;
            } else {
                activityRecord2 = activityRecord;
            }
        } else {
            activityRecord = null;
            activityRecord2 = null;
        }
        int flags = intent2.getFlags();
        if ((33554432 & flags) == 0 || activityRecord2 == null) {
            activityRecord3 = activityRecord;
            str6 = str;
            i7 = i5;
            str7 = str3;
        } else {
            if (i5 >= 0) {
                SafeActivityOptions.abort(safeActivityOptions);
                return -93;
            }
            ActivityRecord activityRecord7 = activityRecord2.resultTo;
            if (activityRecord7 != null && !activityRecord7.isInRootTaskLocked()) {
                activityRecord7 = null;
            }
            String str24 = activityRecord2.resultWho;
            int i26 = activityRecord2.requestCode;
            activityRecord2.resultTo = null;
            if (activityRecord7 != null) {
                activityRecord7.removeResultsLocked(activityRecord2, str24, i26);
            }
            if (activityRecord2.launchedFromUid == i4) {
                activityRecord3 = activityRecord7;
                str6 = activityRecord2.launchedFromPackage;
                i7 = i26;
                str7 = str24;
                str8 = activityRecord2.launchedFromFeatureId;
                if (executeRequestReplaceErrCheck == 0 && intent2.getComponent() == null) {
                    executeRequestReplaceErrCheck = -91;
                }
                if (executeRequestReplaceErrCheck == 0 || activityInfo6 != null) {
                    str9 = str8;
                } else {
                    str9 = str8;
                    executeRequestReplaceErrCheck = -92;
                }
                i8 = -97;
                if (executeRequestReplaceErrCheck == 0 || activityRecord2 == null || activityRecord2.getTask().voiceSession == null || (268435456 & flags) != 0) {
                    activityRecord4 = activityRecord2;
                    activityInfo = activityInfo6;
                    str10 = str20;
                    str11 = str6;
                } else {
                    activityInfo = activityInfo6;
                    str11 = str6;
                    if (activityRecord2.info.applicationInfo.uid != activityInfo.applicationInfo.uid) {
                        try {
                            intent2.addCategory("android.intent.category.VOICE");
                            activityRecord4 = activityRecord2;
                            str10 = str20;
                        } catch (RemoteException e) {
                            e = e;
                            activityRecord4 = activityRecord2;
                            str10 = str20;
                        }
                        try {
                        } catch (RemoteException e2) {
                            e = e2;
                            str12 = TAG;
                            Slog.w(str12, "Failure checking voice capabilities", e);
                            executeRequestReplaceErrCheck = -97;
                            if (executeRequestReplaceErrCheck == 0) {
                            }
                            str13 = str5;
                            i8 = executeRequestReplaceErrCheck;
                            if (activityRecord3 == null) {
                            }
                            if (i8 != 0) {
                            }
                        }
                        if (!this.mService.getPackageManager().activitySupportsIntentAsUser(intent2.getComponent(), intent2, str10, i6)) {
                            String str25 = "Activity being started in current voice task does not support voice: " + intent2;
                            str12 = TAG;
                            try {
                                Slog.w(str12, str25);
                            } catch (RemoteException e3) {
                                e = e3;
                                Slog.w(str12, "Failure checking voice capabilities", e);
                                executeRequestReplaceErrCheck = -97;
                                if (executeRequestReplaceErrCheck == 0) {
                                }
                                str13 = str5;
                                i8 = executeRequestReplaceErrCheck;
                                if (activityRecord3 == null) {
                                }
                                if (i8 != 0) {
                                }
                            }
                            executeRequestReplaceErrCheck = -97;
                            if (executeRequestReplaceErrCheck == 0 || iVoiceInteractionSession == null) {
                                str13 = str5;
                            } else {
                                try {
                                    str13 = str5;
                                } catch (RemoteException e4) {
                                    e = e4;
                                    str13 = str5;
                                }
                                try {
                                } catch (RemoteException e5) {
                                    e = e5;
                                    Slog.w(str12, "Failure checking voice capabilities", e);
                                    if (activityRecord3 == null) {
                                    }
                                    if (i8 != 0) {
                                    }
                                }
                                if (!this.mService.getPackageManager().activitySupportsIntentAsUser(intent2.getComponent(), intent2, str10, i6)) {
                                    Slog.w(str12, "Activity being started in new voice task does not support: " + intent2);
                                    Task rootTask = activityRecord3 == null ? null : activityRecord3.getRootTask();
                                    if (i8 != 0) {
                                        if (activityRecord3 != null) {
                                            activityRecord3.sendResult(-1, str7, i7, 0, null, null);
                                        }
                                        SafeActivityOptions.abort(safeActivityOptions);
                                        return i8;
                                    }
                                    int i27 = i7;
                                    String str26 = str7;
                                    String str27 = str9;
                                    ActivityInfo activityInfo7 = activityInfo;
                                    String str28 = str11;
                                    ActivityInfo activityInfo8 = activityInfo;
                                    String str29 = str12;
                                    ActivityRecord activityRecord8 = activityRecord3;
                                    int i28 = i6;
                                    int modifyCallingUidWhenRecentTask = this.mASWrapper.getExtImpl().modifyCallingUidWhenRecentTask(task, activityInfo7, this.mService, i4, i22, intent2);
                                    try {
                                        boolean z2 = !this.mSupervisor.checkStartAnyActivityPermission(intent2, activityInfo8, str26, i27, i2, modifyCallingUidWhenRecentTask, str28, str27, request.ignoreTargetSecurity, task != null, windowProcessController2, activityRecord8, rootTask);
                                        boolean z3 = z2 | (!this.mService.mIntentFirewall.checkStartActivity(intent2, modifyCallingUidWhenRecentTask, i2, str10, activityInfo8.applicationInfo));
                                        boolean z4 = z3 | (!this.mService.getPermissionPolicyInternal().checkStartActivity(intent2, modifyCallingUidWhenRecentTask, str28));
                                        if (safeActivityOptions != null) {
                                            windowProcessController3 = windowProcessController2;
                                            activityInfo2 = activityInfo8;
                                            activityOptions = safeActivityOptions.getOptions(intent2, activityInfo2, windowProcessController3, this.mSupervisor);
                                        } else {
                                            windowProcessController3 = windowProcessController2;
                                            activityInfo2 = activityInfo8;
                                            activityOptions = null;
                                        }
                                        if (z4) {
                                            activityInfo3 = activityInfo2;
                                            activityRecord5 = activityRecord8;
                                            i9 = 1;
                                        } else {
                                            activityRecord5 = activityRecord8;
                                            activityInfo3 = activityInfo2;
                                            try {
                                                Trace.traceBegin(32L, "shouldAbortBackgroundActivityStart");
                                                try {
                                                    int checkBackgroundActivityStart = this.mController.getBackgroundActivityLaunchController().checkBackgroundActivityStart(modifyCallingUidWhenRecentTask, i2, str28, i22, i21, windowProcessController3, request.originatingPendingIntent, request.backgroundStartPrivileges, intent2, activityOptions);
                                                    if (checkBackgroundActivityStart != 1) {
                                                        StringBuilder sb4 = request.logMessage;
                                                        sb4.append(" (");
                                                        sb4.append(BackgroundActivityStartController.balCodeToString(checkBackgroundActivityStart));
                                                        sb4.append(str13);
                                                    }
                                                    Trace.traceEnd(32L);
                                                    i9 = checkBackgroundActivityStart;
                                                } catch (Throwable th) {
                                                    th = th;
                                                    j = 32;
                                                    Trace.traceEnd(j);
                                                    throw th;
                                                }
                                            } catch (Throwable th2) {
                                                th = th2;
                                                j = 32;
                                            }
                                        }
                                        this.mASWrapper.getExtImpl().hookAfterCheckBackgroundActivityStart();
                                        int i29 = i9;
                                        ActivityOptions activityOptions5 = activityOptions;
                                        WindowProcessController windowProcessController5 = windowProcessController3;
                                        String str30 = str10;
                                        ActivityInfo activityInfo9 = activityInfo3;
                                        boolean triggerMaskFromIntentIfNeed = this.mASWrapper.getExtImpl().triggerMaskFromIntentIfNeed(this.mSupervisor, intent2, activityRecord4, activityInfo9, str28, i2) | z4;
                                        ActivityRecord activityRecord9 = activityRecord4;
                                        int i30 = this.mASWrapper.getExtImpl().acPreloadAbortBgActivityStart(activityRecord9, windowProcessController5) ? 0 : i29;
                                        if (request.allowPendingRemoteAnimationRegistryLookup) {
                                            str15 = str28;
                                            activityOptions2 = this.mService.getActivityStartController().getPendingRemoteAnimationRegistry().overrideOptionsIfNeeded(str15, activityOptions5);
                                        } else {
                                            str15 = str28;
                                            activityOptions2 = activityOptions5;
                                        }
                                        Pair<Integer, Pair<Intent, ActivityInfo>> multiAppActivityInfo = this.mASWrapper.getExtImpl().getMultiAppActivityInfo(i28, intent2, str15, activityInfo9, modifyCallingUidWhenRecentTask, i27, i23, safeActivityOptions, str30, this.mSupervisor, this.mRootWindowContainer, computeResolveFilterUid(modifyCallingUidWhenRecentTask, i22, this.mRequest.filterCallingUid), this.mRequest.realCallingPid);
                                        if (multiAppActivityInfo == null || (obj = multiAppActivityInfo.first) == null || multiAppActivityInfo.second == null) {
                                            intent = intent2;
                                            activityInfo4 = activityInfo9;
                                            i10 = i28;
                                        } else {
                                            int intValue = ((Integer) obj).intValue();
                                            Object obj2 = multiAppActivityInfo.second;
                                            Intent intent3 = (Intent) ((Pair) obj2).first;
                                            activityInfo4 = (ActivityInfo) ((Pair) obj2).second;
                                            i10 = intValue;
                                            intent = intent3;
                                        }
                                        if (this.mService.mController != null) {
                                            try {
                                                triggerMaskFromIntentIfNeed |= !this.mService.mController.activityStarting(intent.cloneFilter(), activityInfo4.applicationInfo.packageName);
                                                z = triggerMaskFromIntentIfNeed;
                                            } catch (RemoteException unused) {
                                                this.mService.mController = null;
                                            }
                                            checkStartActivity = this.mASWrapper.getExtImpl().checkStartActivity(activityRecord9, activityInfo4, intent, i27, i22, str15, activityOptions2, this.mRequest.profilerInfo, task, triggerMaskFromIntentIfNeed);
                                            if (checkStartActivity != null) {
                                                intent = (Intent) checkStartActivity.first;
                                                activityInfo4 = (ActivityInfo) checkStartActivity.second;
                                            }
                                            this.mInterceptor.setStates(i10, i21, i22, i23, str15, str27);
                                            if (this.mInterceptor.intercept(intent, resolveInfo4, activityInfo4, str30, task, taskFragment, i2, modifyCallingUidWhenRecentTask, activityOptions2)) {
                                                i11 = modifyCallingUidWhenRecentTask;
                                                activityOptions3 = activityOptions2;
                                                resolveInfo = resolveInfo4;
                                                str16 = str30;
                                            } else {
                                                ActivityStartInterceptor activityStartInterceptor = this.mInterceptor;
                                                intent = activityStartInterceptor.mIntent;
                                                ResolveInfo resolveInfo5 = activityStartInterceptor.mRInfo;
                                                activityInfo4 = activityStartInterceptor.mAInfo;
                                                str16 = activityStartInterceptor.mResolvedType;
                                                Task task2 = activityStartInterceptor.mInTask;
                                                resolveInfo = resolveInfo5;
                                                int i31 = activityStartInterceptor.mCallingPid;
                                                i11 = activityStartInterceptor.mCallingUid;
                                                task = task2;
                                                i2 = i31;
                                                neededUriGrants4 = null;
                                                activityOptions3 = activityStartInterceptor.mActivityOptions;
                                            }
                                            if (!triggerMaskFromIntentIfNeed) {
                                                if (activityRecord5 != null) {
                                                    activityRecord5.sendResult(-1, str26, i27, 0, null, null);
                                                }
                                                ActivityOptions.abort(activityOptions3);
                                                if (triggerMaskFromIntentIfNeed) {
                                                    Slog.w(str29, "Check which step was aborted : abortPermission = " + z2 + ", abortFire = " + z3 + ", abortPolicy = " + z4 + ", abortWatch = " + z + ", mService.mController = " + this.mService.mController);
                                                }
                                                return 102;
                                            }
                                            if (activityInfo4 == null || !this.mService.getPackageManagerInternalLocked().isPermissionsReviewRequired(activityInfo4.packageName, i10)) {
                                                activityOptions4 = activityOptions3;
                                                i12 = i23;
                                                str17 = str16;
                                                resolveInfo2 = resolveInfo;
                                                neededUriGrants = neededUriGrants4;
                                            } else {
                                                IIntentSender intentSenderLocked = this.mService.getIntentSenderLocked(2, str15, str27, i11, i10, null, null, 0, new Intent[]{intent}, new String[]{str16}, 1342177280, null);
                                                Intent intent4 = new Intent("android.intent.action.REVIEW_PERMISSIONS");
                                                int flags2 = intent.getFlags() | 8388608;
                                                if ((268959744 & flags2) != 0) {
                                                    flags2 |= 134217728;
                                                }
                                                intent4.setFlags(flags2);
                                                intent4.putExtra("android.intent.extra.PACKAGE_NAME", activityInfo4.packageName);
                                                intent4.putExtra("android.intent.extra.INTENT", new IntentSender(intentSenderLocked));
                                                if (activityRecord5 != null) {
                                                    intent4.putExtra("android.intent.extra.RESULT_NEEDED", true);
                                                }
                                                ResolveInfo resolveIntent = this.mSupervisor.resolveIntent(intent4, null, i10, 0, computeResolveFilterUid(i22, i22, request.filterCallingUid), i21);
                                                i12 = i23;
                                                activityInfo4 = this.mSupervisor.resolveActivity(intent4, resolveIntent, i12, null);
                                                if (ActivityTaskManagerDebugConfig.DEBUG_PERMISSIONS_REVIEW) {
                                                    Task topDisplayFocusedRootTask = this.mRootWindowContainer.getTopDisplayFocusedRootTask();
                                                    StringBuilder sb5 = new StringBuilder();
                                                    resolveInfo3 = resolveIntent;
                                                    sb5.append(str4);
                                                    sb5.append(i10);
                                                    sb5.append(str2);
                                                    activityOptions4 = activityOptions3;
                                                    sb5.append(intent4.toShortString(true, true, true, false));
                                                    sb5.append("} from uid ");
                                                    sb5.append(i22);
                                                    sb5.append(" on display ");
                                                    sb5.append(topDisplayFocusedRootTask == null ? 0 : topDisplayFocusedRootTask.getDisplayId());
                                                    Slog.i(str29, sb5.toString());
                                                } else {
                                                    resolveInfo3 = resolveIntent;
                                                    activityOptions4 = activityOptions3;
                                                }
                                                intent = intent4;
                                                i11 = i22;
                                                resolveInfo2 = resolveInfo3;
                                                i2 = i21;
                                                neededUriGrants = null;
                                                str17 = null;
                                            }
                                            if (resolveInfo2 == null || (auxiliaryResolveInfo = resolveInfo2.auxiliaryInfo) == null) {
                                                i13 = i12;
                                                str18 = str15;
                                                i14 = i22;
                                                activityRecord6 = activityRecord9;
                                                activityInfo5 = activityInfo4;
                                                i15 = i2;
                                                i16 = i11;
                                                String str31 = str17;
                                                neededUriGrants2 = neededUriGrants;
                                                str19 = str31;
                                            } else {
                                                i13 = i12;
                                                str18 = str15;
                                                i14 = i22;
                                                activityRecord6 = activityRecord9;
                                                intent = createLaunchIntent(auxiliaryResolveInfo, request.ephemeralIntent, str15, str27, bundle, str17, i10);
                                                str19 = null;
                                                neededUriGrants2 = null;
                                                activityInfo5 = this.mSupervisor.resolveActivity(intent, resolveInfo2, i13, null);
                                                i16 = i14;
                                                i15 = i21;
                                            }
                                            if (windowProcessController5 != null || i21 <= 0 || (windowProcessController4 = this.mService.mProcessMap.getProcess(i21)) == null) {
                                                windowProcessController4 = windowProcessController5;
                                            }
                                            ActivityOptions activityOptions6 = activityOptions4;
                                            ActivityRecord build = new ActivityRecord.Builder(this.mService).setCaller(windowProcessController4).setLaunchedFromPid(i15).setLaunchedFromUid(i16).setLaunchedFromPackage(str18).setLaunchedFromFeature(str27).setIntent(intent).setResolvedType(str19).setActivityInfo(activityInfo5).setConfiguration(this.mService.getGlobalConfiguration()).setResultTo(activityRecord5).setResultWho(str26).setRequestCode(i27).setComponentSpecified(request.componentSpecified).setRootVoiceInteraction(iVoiceInteractionSession != null).setActivityOptions(activityOptions6).setSourceRecord(activityRecord6).build();
                                            this.mLastStartActivityRecord = build;
                                            if (build.appTimeTracker == null && activityRecord6 != null) {
                                                build.appTimeTracker = activityRecord6.appTimeTracker;
                                            }
                                            if (this.mASWrapper.getExtImpl().hansActivityIfNeeded(i16, str18, build)) {
                                                ActivityOptions.abort(activityOptions6);
                                                return 102;
                                            }
                                            WindowProcessController windowProcessController6 = this.mService.mHomeProcess;
                                            boolean z5 = windowProcessController6 != null && activityInfo5.applicationInfo.uid == windowProcessController6.mUid;
                                            if (i30 != 0 && !z5) {
                                                this.mService.resumeAppSwitches();
                                            }
                                            this.mASWrapper.getExtImpl().executeRequestBeforeStartActivity(activityInfo5, i10);
                                            this.mLastStartActivityResult = startActivityUnchecked(build, activityRecord6, iVoiceInteractionSession, request.voiceInteractor, i13, activityOptions6, task, taskFragment, i30, neededUriGrants2, i14);
                                            ActivityRecord[] activityRecordArr = request.outActivity;
                                            if (activityRecordArr != null) {
                                                i17 = 0;
                                                activityRecordArr[0] = this.mLastStartActivityRecord;
                                            } else {
                                                i17 = 0;
                                            }
                                            if (activityInfo5 != null) {
                                                this.mUIFirstManagerExt.onAppStatusChanged(i17, activityInfo5.packageName, activityInfo5.name);
                                            }
                                            return this.mLastStartActivityResult;
                                        }
                                        z = false;
                                        checkStartActivity = this.mASWrapper.getExtImpl().checkStartActivity(activityRecord9, activityInfo4, intent, i27, i22, str15, activityOptions2, this.mRequest.profilerInfo, task, triggerMaskFromIntentIfNeed);
                                        if (checkStartActivity != null) {
                                        }
                                        this.mInterceptor.setStates(i10, i21, i22, i23, str15, str27);
                                        if (this.mInterceptor.intercept(intent, resolveInfo4, activityInfo4, str30, task, taskFragment, i2, modifyCallingUidWhenRecentTask, activityOptions2)) {
                                        }
                                        if (!triggerMaskFromIntentIfNeed) {
                                        }
                                    } catch (SecurityException e6) {
                                        Intent intent5 = request.ephemeralIntent;
                                        if (intent5 != null && (intent5.getComponent() != null || intent5.getPackage() != null)) {
                                            if (intent5.getComponent() != null) {
                                                str14 = intent5.getComponent().getPackageName();
                                            } else {
                                                str14 = intent5.getPackage();
                                            }
                                            if (this.mService.getPackageManagerInternalLocked().filterAppAccess(str14, modifyCallingUidWhenRecentTask, i28)) {
                                                if (activityRecord8 != null) {
                                                    activityRecord8.sendResult(-1, str26, i27, 0, null, null);
                                                }
                                                SafeActivityOptions.abort(safeActivityOptions);
                                                return -92;
                                            }
                                        }
                                        throw e6;
                                    }
                                }
                            }
                            i8 = executeRequestReplaceErrCheck;
                            if (activityRecord3 == null) {
                            }
                            if (i8 != 0) {
                            }
                        }
                    } else {
                        activityRecord4 = activityRecord2;
                        str10 = str20;
                    }
                }
                str12 = TAG;
                if (executeRequestReplaceErrCheck == 0) {
                }
                str13 = str5;
                i8 = executeRequestReplaceErrCheck;
                if (activityRecord3 == null) {
                }
                if (i8 != 0) {
                }
            } else {
                activityRecord3 = activityRecord7;
                i7 = i26;
                str7 = str24;
                str6 = str;
            }
        }
        str8 = str23;
        if (executeRequestReplaceErrCheck == 0) {
            executeRequestReplaceErrCheck = -91;
        }
        if (executeRequestReplaceErrCheck == 0) {
        }
        str9 = str8;
        i8 = -97;
        if (executeRequestReplaceErrCheck == 0) {
        }
        activityRecord4 = activityRecord2;
        activityInfo = activityInfo6;
        str10 = str20;
        str11 = str6;
        str12 = TAG;
        if (executeRequestReplaceErrCheck == 0) {
        }
        str13 = str5;
        i8 = executeRequestReplaceErrCheck;
        if (activityRecord3 == null) {
        }
        if (i8 != 0) {
        }
    }

    private boolean handleBackgroundActivityAbort(ActivityRecord activityRecord) {
        if (!(!this.mService.isBackgroundActivityStartsEnabled())) {
            return false;
        }
        ActivityRecord activityRecord2 = activityRecord.resultTo;
        String str = activityRecord.resultWho;
        int i = activityRecord.requestCode;
        if (activityRecord2 != null) {
            activityRecord2.sendResult(-1, str, i, 0, null, null);
        }
        ActivityOptions.abort(activityRecord.getOptions());
        return true;
    }

    private void onExecutionComplete() {
        this.mController.onExecutionComplete(this);
    }

    private void onExecutionStarted() {
        this.mController.onExecutionStarted();
    }

    private Intent createLaunchIntent(AuxiliaryResolveInfo auxiliaryResolveInfo, Intent intent, String str, String str2, Bundle bundle, String str3, int i) {
        if (auxiliaryResolveInfo != null && auxiliaryResolveInfo.needsPhaseTwo) {
            PackageManagerInternal packageManagerInternalLocked = this.mService.getPackageManagerInternalLocked();
            packageManagerInternalLocked.requestInstantAppResolutionPhaseTwo(auxiliaryResolveInfo, intent, str3, str, str2, packageManagerInternalLocked.isInstantApp(str, i), bundle, i);
        }
        return InstantAppResolver.buildEphemeralInstallerIntent(intent, InstantAppResolver.sanitizeIntent(intent), auxiliaryResolveInfo == null ? null : auxiliaryResolveInfo.failureIntent, str, str2, bundle, str3, i, auxiliaryResolveInfo == null ? null : auxiliaryResolveInfo.installFailureActivity, auxiliaryResolveInfo == null ? null : auxiliaryResolveInfo.token, auxiliaryResolveInfo != null && auxiliaryResolveInfo.needsPhaseTwo, auxiliaryResolveInfo != null ? auxiliaryResolveInfo.filters : null);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void postStartActivityProcessing(ActivityRecord activityRecord, int i, Task task) {
        Task task2;
        Task rootHomeTask;
        if (!ActivityManager.isStartResultSuccessful(i) && this.mFrozeTaskList) {
            this.mSupervisor.mRecentTasks.resetFreezeTaskListReorderingOnTimeout();
        }
        if (ActivityManager.isStartResultFatalError(i)) {
            return;
        }
        this.mSupervisor.reportWaitingActivityLaunchedIfNeeded(activityRecord, i);
        if (activityRecord.getTask() != null) {
            task2 = activityRecord.getTask();
        } else {
            task2 = this.mTargetTask;
        }
        if (task == null || task2 == null || !task2.isAttached()) {
            return;
        }
        if (i == 2 || i == 3) {
            TaskDisplayArea displayArea = task2.getDisplayArea();
            boolean z = (displayArea == null || (rootHomeTask = displayArea.getRootHomeTask()) == null || !rootHomeTask.shouldBeVisible(null)) ? false : true;
            ActivityRecord topNonFinishingActivity = task2.getTopNonFinishingActivity();
            this.mService.getTaskChangeNotificationController().notifyActivityRestartAttempt(task2.getTaskInfo(), z, this.mIsTaskCleared, topNonFinishingActivity != null && topNonFinishingActivity.isVisible());
        }
        if (ActivityManager.isStartResultSuccessful(i)) {
            this.mInterceptor.onActivityLaunched(task2.getTaskInfo(), activityRecord);
        }
        this.mASWrapper.getExtImpl().hookPostStartActivityProcessing(i, task2, activityRecord);
    }

    private int startActivityUnchecked(ActivityRecord activityRecord, ActivityRecord activityRecord2, IVoiceInteractionSession iVoiceInteractionSession, IVoiceInteractor iVoiceInteractor, int i, ActivityOptions activityOptions, Task task, TaskFragment taskFragment, int i2, NeededUriGrants neededUriGrants, int i3) {
        TransitionController transitionController = activityRecord.mTransitionController;
        boolean z = (!transitionController.isShellTransitionsEnabled() || this.mService.getWrapper().getExtImpl().withNoneTransition(activityRecord, null, activityOptions, 1, "startActivityInner") || i2 == 0) ? false : true;
        this.mASWrapper.getExtImpl().forceCancelTransitionIfNeed(activityRecord, 1);
        Transition createAndStartCollecting = z ? transitionController.createAndStartCollecting(1) : null;
        RemoteTransition takeRemoteTransition = activityRecord.takeRemoteTransition();
        if (createAndStartCollecting != null) {
            createAndStartCollecting.getWrapper().getExtImpl().setRemoteTransitionRequested(takeRemoteTransition);
        }
        try {
            this.mService.deferWindowLayout();
            transitionController.collect(activityRecord);
            try {
                Trace.traceBegin(32L, "startActivityInner");
                if (!ActivityTaskManagerService.LTW_DISABLE) {
                    this.mService.getWrapper().getExtImpl().getRemoteTaskManager().resetSession();
                }
                int startActivityInner = startActivityInner(activityRecord, activityRecord2, iVoiceInteractionSession, iVoiceInteractor, i, activityOptions, task, taskFragment, i2, neededUriGrants, i3);
                Trace.traceEnd(32L);
                this.mASWrapper.getExtImpl().notifyNoneTransition(z, createAndStartCollecting, this.mTargetRootTask, transitionController);
                Transition transition = this.mService.getWrapper().getExtImpl().withNoneTransition(activityRecord, null, activityOptions, 1, "startActivityInner#handleStartResult") ? null : createAndStartCollecting;
                if (!ActivityTaskManagerService.LTW_DISABLE) {
                    this.mService.getWrapper().getExtImpl().getRemoteTaskManager().handleInterceptSessionIfNeeded();
                }
                this.mASWrapper.getExtImpl().addNextAppTransitionRequests(transitionController, 1, startActivityInner);
                Task handleStartResult = handleStartResult(activityRecord, activityOptions, startActivityInner, transition, takeRemoteTransition);
                this.mService.continueWindowLayout();
                postStartActivityProcessing(activityRecord, startActivityInner, handleStartResult);
                if (!ActivityTaskManagerService.LTW_DISABLE) {
                    this.mService.getWrapper().getExtImpl().getRemoteTaskManager().resetSession();
                }
                return startActivityInner;
            } catch (Throwable th) {
                Trace.traceEnd(32L);
                this.mASWrapper.getExtImpl().notifyNoneTransition(z, createAndStartCollecting, this.mTargetRootTask, transitionController);
                Transition transition2 = this.mService.getWrapper().getExtImpl().withNoneTransition(activityRecord, null, activityOptions, 1, "startActivityInner#handleStartResult") ? null : createAndStartCollecting;
                if (!ActivityTaskManagerService.LTW_DISABLE) {
                    this.mService.getWrapper().getExtImpl().getRemoteTaskManager().handleInterceptSessionIfNeeded();
                }
                this.mASWrapper.getExtImpl().addNextAppTransitionRequests(transitionController, 1, -96);
                handleStartResult(activityRecord, activityOptions, -96, transition2, takeRemoteTransition);
                throw th;
            }
        } catch (Throwable th2) {
            this.mService.continueWindowLayout();
            throw th2;
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:44:0x00cd  */
    /* JADX WARN: Removed duplicated region for block: B:50:0x00f2  */
    /* JADX WARN: Removed duplicated region for block: B:52:0x00f7  */
    /* JADX WARN: Removed duplicated region for block: B:58:0x0105  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private Task handleStartResult(ActivityRecord activityRecord, ActivityOptions activityOptions, int i, Transition transition, RemoteTransition remoteTransition) {
        Transition transition2;
        StatusBarManagerInternal statusBarManagerInternal;
        ActivityTaskSupervisor activityTaskSupervisor = this.mSupervisor;
        boolean z = activityTaskSupervisor.mUserLeaving;
        activityTaskSupervisor.mUserLeaving = false;
        Task rootTask = activityRecord.getRootTask();
        if (rootTask == null) {
            rootTask = this.mTargetRootTask;
        }
        if (!ActivityManager.isStartResultSuccessful(i) || rootTask == null) {
            if (this.mStartActivity.getTask() != null) {
                this.mStartActivity.finishIfPossible("startActivity", true);
            } else if (this.mStartActivity.getParent() != null) {
                this.mStartActivity.getParent().removeChild(this.mStartActivity);
            }
            if (rootTask != null && rootTask.isAttached() && !rootTask.hasActivity() && !rootTask.isActivityTypeHome() && !rootTask.mCreatedByOrganizer) {
                rootTask.removeIfPossible("handleStartResult");
            }
            if (transition != null) {
                transition.abort();
            }
            return null;
        }
        if (activityOptions != null && activityOptions.getTaskAlwaysOnTop()) {
            rootTask.setAlwaysOnTop(true);
        }
        ActivityRecord activityRecord2 = rootTask.topRunningActivity();
        if (activityRecord2 != null && activityRecord2.shouldUpdateConfigForDisplayChanged()) {
            this.mRootWindowContainer.ensureVisibilityAndConfig(activityRecord2, activityRecord2.getDisplayId(), true, false);
        }
        if (!this.mAvoidMoveToFront && this.mDoResume && this.mRootWindowContainer.hasVisibleWindowAboveButDoesNotOwnNotificationShade(activityRecord.launchedFromUid) && (statusBarManagerInternal = this.mService.getStatusBarManagerInternal()) != null) {
            statusBarManagerInternal.collapsePanels();
        }
        TransitionController transitionController = activityRecord.mTransitionController;
        boolean z2 = i == 0 || i == 2;
        boolean z3 = activityOptions != null && activityOptions.getTransientLaunch();
        boolean z4 = z3 && this.mPriorAboveTask != null && this.mDisplayLockAndOccluded;
        if (z2) {
            transitionController.collectExistenceChange(activityRecord);
        } else if (i == 3 && transition != null && this.mMovedToTopActivity == null && !transitionController.isTransientHide(rootTask) && !z4 && !this.mASWrapper.getExtImpl().isStartZoom(activityRecord)) {
            if (!this.mASWrapper.getExtImpl().isMirageDisplay(activityOptions != null ? activityOptions.getLaunchDisplayId() : -1) && !this.mASWrapper.getExtImpl().isInSplitScreenMode()) {
                transition.abort();
                transition2 = null;
                if (z3) {
                    if (z4) {
                        transitionController.collect(this.mLastStartActivityRecord);
                        transitionController.collect(this.mPriorAboveTask);
                    }
                    transitionController.setTransientLaunch(this.mLastStartActivityRecord, this.mPriorAboveTask);
                    if (z4) {
                        DisplayContent displayContent = this.mLastStartActivityRecord.getDisplayContent();
                        displayContent.mWallpaperController.adjustWallpaperWindows();
                        transitionController.setReady(displayContent, true);
                    }
                }
                if (!z) {
                    transitionController.setCanPipOnFinish(false);
                }
                if (transition2 == null) {
                    Task task = this.mTargetTask;
                    if (task == null) {
                        task = activityRecord.getTask();
                    }
                    transitionController.requestStartTransition(transition2, task, remoteTransition, null);
                } else if ((i != 0 || !this.mStartActivity.isState(ActivityRecord.State.RESUMED)) && z2 && this.mASWrapper.getExtImpl().checkSendReady(this.mStartActivity, activityRecord2, i)) {
                    transitionController.setReady(activityRecord, false);
                }
                return rootTask;
            }
        }
        transition2 = transition;
        if (z3) {
        }
        if (!z) {
        }
        if (transition2 == null) {
        }
        return rootTask;
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:103:0x03e0  */
    /* JADX WARN: Removed duplicated region for block: B:121:0x04aa  */
    /* JADX WARN: Removed duplicated region for block: B:124:0x04d7  */
    /* JADX WARN: Removed duplicated region for block: B:131:0x0518  */
    /* JADX WARN: Removed duplicated region for block: B:149:0x057c  */
    /* JADX WARN: Removed duplicated region for block: B:151:0x0597  */
    /* JADX WARN: Removed duplicated region for block: B:167:0x04d9  */
    /* JADX WARN: Removed duplicated region for block: B:170:0x047f  */
    /* JADX WARN: Type inference failed for: r8v5 */
    /* JADX WARN: Type inference failed for: r8v6, types: [com.android.server.wm.ActivityRecord, com.android.server.wm.Task] */
    /* JADX WARN: Type inference failed for: r8v7 */
    /* JADX WARN: Type inference failed for: r8v9 */
    @VisibleForTesting
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    int startActivityInner(ActivityRecord activityRecord, ActivityRecord activityRecord2, IVoiceInteractionSession iVoiceInteractionSession, IVoiceInteractor iVoiceInteractor, int i, ActivityOptions activityOptions, Task task, TaskFragment taskFragment, int i2, NeededUriGrants neededUriGrants, int i3) {
        boolean z;
        boolean z2;
        Task task2;
        NeededUriGrants neededUriGrants2;
        ActivityRecord activityRecord3;
        ActivityRecord activityRecord4;
        ActivityOptions activityOptions2;
        ActivityOptions activityOptions3;
        boolean z3;
        ?? r8;
        ActivityRecord activityRecord5;
        ActivityRecord activityRecord6;
        ActivityRecord activityRecord7;
        int deliverToCurrentTopIfNeeded;
        ActivityRecord activityRecord8;
        ActivityRecord findActivity;
        this.mASWrapper.getExtImpl().hookActivityBoost();
        ActivityOptions adjustOptionsForSplitPair = this.mASWrapper.getExtImpl().adjustOptionsForSplitPair(this.mASWrapper.getExtImpl().modifyOptionsForCompactModeIfNeed(activityOptions, activityRecord, activityRecord2), activityRecord);
        this.mASWrapper.getExtImpl().shouldLaunchInSplitTask(adjustOptionsForSplitPair);
        ActivityOptions createOptionsForZoom = this.mASWrapper.getExtImpl().createOptionsForZoom(this.mASWrapper.getExtImpl().adjustOptionsForSplitScreen(adjustOptionsForSplitPair, activityRecord), activityRecord2, activityRecord, this.mRequest.realCallingPid);
        IActivityStarterExt extImpl = this.mASWrapper.getExtImpl();
        ActivityRecord activityRecord9 = this.mService.mLastResumedActivity;
        Request request = this.mRequest;
        ActivityOptions adjustOptionsForFlexibleWindow = extImpl.adjustOptionsForFlexibleWindow(createOptionsForZoom, activityRecord2, activityRecord, activityRecord9, request.realCallingPid, request.realCallingUid);
        setInitialState(activityRecord, adjustOptionsForFlexibleWindow, task, taskFragment, i, activityRecord2, iVoiceInteractionSession, iVoiceInteractor, i2, i3);
        computeLaunchingTaskFlags();
        this.mIntent.setFlags(this.mLaunchFlags);
        Iterator<ActivityRecord> it = this.mSupervisor.mStoppingActivities.iterator();
        while (true) {
            if (!it.hasNext()) {
                z = false;
                break;
            }
            if (it.next().getActivityType() == 5) {
                z = true;
                break;
            }
        }
        Task focusedRootTask = this.mPreferredTaskDisplayArea.getFocusedRootTask();
        Task topLeafTask = focusedRootTask != null ? focusedRootTask.getTopLeafTask() : null;
        Task reusableTask = getReusableTask();
        this.mASWrapper.getExtImpl().changeReusedTask(reusableTask);
        this.mASWrapper.getExtImpl().exitFlexibleEmbeddedTask(activityRecord, reusableTask, adjustOptionsForFlexibleWindow, this.mRequest.realCallingPid, false);
        Task changeReusedTaskForAppInner = this.mASWrapper.getExtImpl().changeReusedTaskForAppInner(reusableTask, activityRecord);
        Pair<Boolean, Task> isAppUnlockPasswordActivity = this.mASWrapper.getExtImpl().isAppUnlockPasswordActivity(this.mRootWindowContainer, adjustOptionsForFlexibleWindow, this.mOptions, this.mAddingToTask, activityRecord, activityRecord2);
        if (isAppUnlockPasswordActivity != null) {
            this.mAddingToTask = ((Boolean) isAppUnlockPasswordActivity.first).booleanValue();
            this.mInTask = (Task) isAppUnlockPasswordActivity.second;
        }
        ActivityOptions activityOptions4 = this.mOptions;
        if (activityOptions4 != null && activityOptions4.freezeRecentTasksReordering() && this.mSupervisor.mRecentTasks.isCallerRecents(activityRecord.launchedFromUid) && !this.mSupervisor.mRecentTasks.isFreezeTaskListReorderingSet()) {
            this.mFrozeTaskList = true;
            this.mSupervisor.mRecentTasks.setFreezeTaskListReordering();
        }
        Task computeTargetTask = changeReusedTaskForAppInner != null ? changeReusedTaskForAppInner : computeTargetTask();
        boolean z4 = computeTargetTask == null;
        if (ActivityTaskManagerService.LTW_DISABLE) {
            z2 = z;
            task2 = topLeafTask;
            neededUriGrants2 = neededUriGrants;
            activityRecord3 = activityRecord2;
            activityRecord4 = activityRecord;
        } else {
            task2 = topLeafTask;
            neededUriGrants2 = neededUriGrants;
            z2 = z;
            activityRecord3 = activityRecord2;
            activityRecord4 = activityRecord;
            ActivityOptions handleRemoteTaskIfNeeded = this.mASWrapper.getExtImpl().handleRemoteTaskIfNeeded(activityRecord, this.mPreferredTaskDisplayArea, this.mSourceRecord, activityRecord2, adjustOptionsForFlexibleWindow, this.mNotTop, z4, changeReusedTaskForAppInner, this.mLaunchFlags, this.mLaunchMode, this.mIntent);
            this.mOptions = handleRemoteTaskIfNeeded;
            adjustOptionsForFlexibleWindow = handleRemoteTaskIfNeeded;
        }
        this.mASWrapper.getExtImpl().setForceUpdateWindow(computeTargetTask, activityRecord4);
        this.mTargetTask = computeTargetTask;
        ActivityOptions adjustOptionsForFlexibleTask = this.mASWrapper.getExtImpl().adjustOptionsForFlexibleTask(computeTargetTask, this.mPreferredTaskDisplayArea, adjustOptionsForFlexibleWindow, this.mStartActivity, activityRecord2);
        this.mOptions = adjustOptionsForFlexibleTask;
        NeededUriGrants neededUriGrants3 = neededUriGrants2;
        ActivityRecord activityRecord10 = activityRecord4;
        boolean z5 = z4;
        Task task3 = computeTargetTask;
        this.mASWrapper.getExtImpl().updateFlexibleWindowTask(computeTargetTask, changeReusedTaskForAppInner, adjustOptionsForFlexibleTask, this.mStartActivity, activityRecord2, this.mRequest.realCallingPid);
        if (this.mASWrapper.getExtImpl().interceptStartActivityFromFlexibleWindow(focusedRootTask, task3, adjustOptionsForFlexibleTask, this.mStartActivity, this.mRequest, activityRecord2)) {
            return 0;
        }
        if (this.mASWrapper.getExtImpl().pullPuttTaskBack(this, this.mStartActivity, task3, adjustOptionsForFlexibleTask, this.mOptions, activityRecord2)) {
            Slog.w(TAG, "start Activity abort as putt, a: " + this.mStartActivity + " tt: " + task3 + " s:" + activityRecord3);
            return 102;
        }
        ActivityOptions adjustOptionsForZoom = this.mASWrapper.getExtImpl().adjustOptionsForZoom(adjustOptionsForFlexibleTask, activityRecord2, activityRecord, task3, this.mRequest.realCallingPid);
        this.mASWrapper.getExtImpl().launchIntoCompatMode(adjustOptionsForZoom, activityRecord3, activityRecord10, task3);
        computeLaunchParams(activityRecord10, activityRecord3, task3);
        int isAllowedToStart = isAllowedToStart(activityRecord10, z5, task3);
        if (isAllowedToStart != 0) {
            ActivityRecord activityRecord11 = activityRecord10.resultTo;
            if (activityRecord11 != null) {
                activityRecord11.sendResult(-1, activityRecord10.resultWho, activityRecord10.requestCode, 0, null, null);
            }
            return isAllowedToStart;
        }
        if (task3 != null) {
            if (task3.getTreeWeight() > MAX_TASK_WEIGHT_FOR_ADDING_ACTIVITY) {
                Slog.e(TAG, "Remove " + task3 + " because it has contained too many activities or windows (abort starting " + activityRecord10 + " from uid=" + this.mCallingUid);
                task3.removeImmediately("bulky-task");
                return 102;
            }
            if (!this.mAvoidMoveToFront && activityRecord10.mTransitionController.isTransientHide(task3)) {
                this.mAvoidMoveToFront = true;
            }
            this.mPriorAboveTask = TaskDisplayArea.getRootTaskAbove(task3.getRootTask());
        }
        ActivityRecord topNonFinishingActivity = z5 ? null : task3.getTopNonFinishingActivity();
        this.mASWrapper.getExtImpl().shouldClearReusedActivity(changeReusedTaskForAppInner, topNonFinishingActivity, adjustOptionsForZoom, this.mStartActivity);
        ActivityRecord activityRecord12 = topNonFinishingActivity;
        if (this.mASWrapper.getExtImpl().interceptStartForMirageCarMode(this.mIntent, activityRecord2, activityRecord, changeReusedTaskForAppInner, adjustOptionsForZoom, this) || this.mASWrapper.getExtImpl().interceptStartForSplitScreenMode(this.mIntent, this.mRequest, activityRecord2, activityRecord, adjustOptionsForZoom, changeReusedTaskForAppInner)) {
            return 0;
        }
        this.mASWrapper.getExtImpl().activityPreloadHandleStartActivity(activityRecord10);
        if (activityRecord12 != null) {
            if (3 == this.mLaunchMode && (activityRecord8 = this.mSourceRecord) != null && task3 == activityRecord8.getTask() && (findActivity = this.mRootWindowContainer.findActivity(this.mIntent, this.mStartActivity.info, false)) != null && findActivity.getTask() != task3) {
                findActivity.destroyIfPossible("Removes redundant singleInstance");
            }
            if (this.mASWrapper.getExtImpl().onStartFromPrimaryScreen(changeReusedTaskForAppInner, activityRecord2, this.mStartActivity, adjustOptionsForZoom, this.mPreferredTaskDisplayArea, this.mIntent) || this.mASWrapper.getExtImpl().startPreloadActivityWhilePreloading(changeReusedTaskForAppInner, activityRecord2, activityRecord12, adjustOptionsForZoom, activityRecord10.launchedFromPackage, this.mLastStartReason)) {
                return 0;
            }
            IActivityStarterExt extImpl2 = this.mASWrapper.getExtImpl();
            Request request2 = this.mRequest;
            activityOptions2 = adjustOptionsForZoom;
            extImpl2.updateTaskForZoom(adjustOptionsForZoom, activityRecord2, activityRecord, task3, request2.realCallingPid, request2.callingPackage, focusedRootTask);
            this.mASWrapper.getExtImpl().markFlexibleSubTaskIfForceStopNeeded(activityRecord3, this.mStartActivity, this.mTargetTask);
            int recycleTask = recycleTask(task3, activityRecord12, changeReusedTaskForAppInner, neededUriGrants3);
            if (recycleTask != 0) {
                return recycleTask;
            }
        } else {
            activityOptions2 = adjustOptionsForZoom;
            this.mAddingToTask = true;
        }
        Task focusedRootTask2 = this.mPreferredTaskDisplayArea.getFocusedRootTask();
        if (focusedRootTask2 != null && (deliverToCurrentTopIfNeeded = deliverToCurrentTopIfNeeded(focusedRootTask2, neededUriGrants3)) != 0) {
            return deliverToCurrentTopIfNeeded;
        }
        if (this.mTargetRootTask == null) {
            this.mTargetRootTask = getOrCreateRootTask(this.mStartActivity, this.mLaunchFlags, task3, this.mOptions);
        }
        if (z5) {
            setNewTask((!this.mLaunchTaskBehind || (activityRecord7 = this.mSourceRecord) == null) ? null : activityRecord7.getTask());
        } else if (this.mAddingToTask) {
            activityOptions3 = activityOptions2;
            this.mASWrapper.getExtImpl().parseFlexibleActivityInfo(activityOptions3, activityRecord3, activityRecord10);
            addOrReparentStartingActivity(task3, "adding to task");
            if (!ActivityTaskManagerService.LTW_DISABLE) {
                this.mService.getWrapper().getExtImpl().getRemoteTaskManager().updateRemoteTaskIfNeeded(activityRecord.getRootTask(), activityOptions3);
            }
            if (this.mAvoidMoveToFront && this.mDoResume) {
                r8 = 0;
                r8 = 0;
                this.mService.getWrapper().getFlexibleExtImpl().moveTaskToFront(this.mTargetRootTask, activityOptions3, null);
                this.mTargetRootTask.getRootTask().moveToFront("reuseOrNewTask", task3);
                if (this.mTargetRootTask.isTopRootTaskInDisplayArea() || !this.mService.isDreaming() || z2) {
                    z3 = true;
                } else {
                    z3 = true;
                    this.mLaunchTaskBehind = true;
                    activityRecord10.mLaunchTaskBehind = true;
                }
            } else {
                z3 = true;
                r8 = 0;
            }
            this.mService.mUgmInternal.grantUriPermissionUncheckedFromIntent(neededUriGrants3, this.mStartActivity.getUriPermissionsLocked());
            activityRecord5 = this.mStartActivity;
            activityRecord6 = activityRecord5.resultTo;
            if (activityRecord6 == null && activityRecord6.info != null) {
                PackageManagerInternal packageManagerInternalLocked = this.mService.getPackageManagerInternalLocked();
                ActivityRecord activityRecord13 = this.mStartActivity;
                int packageUid = packageManagerInternalLocked.getPackageUid(activityRecord13.resultTo.info.packageName, 0L, activityRecord13.mUserId);
                ActivityRecord activityRecord14 = this.mStartActivity;
                packageManagerInternalLocked.grantImplicitAccess(activityRecord14.mUserId, this.mIntent, UserHandle.getAppId(activityRecord14.info.applicationInfo.uid), packageUid, true);
            } else if (activityRecord5.mShareIdentity) {
                PackageManagerInternal packageManagerInternalLocked2 = this.mService.getPackageManagerInternalLocked();
                ActivityRecord activityRecord15 = this.mStartActivity;
                packageManagerInternalLocked2.grantImplicitAccess(activityRecord15.mUserId, this.mIntent, UserHandle.getAppId(activityRecord15.info.applicationInfo.uid), activityRecord10.launchedFromUid, true);
            }
            Task task4 = this.mStartActivity.getTask();
            if (z5) {
                EventLogTags.writeWmCreateTask(this.mStartActivity.mUserId, task4.mTaskId, task4.getRootTaskId(), task4.getDisplayId());
            }
            this.mStartActivity.logStartActivity(EventLogTags.WM_CREATE_ACTIVITY, task4);
            this.mStartActivity.getTaskFragment().clearLastPausedActivity();
            this.mRootWindowContainer.startPowerModeLaunchIfNeeded(false, this.mStartActivity);
            boolean z6 = task4 == task2 ? z3 : false;
            IActivityRecordExt extImpl3 = this.mStartActivity.getWrapper().getExtImpl();
            if (this.mAvoidMoveToFront || this.mDoResume) {
                z3 = false;
            }
            extImpl3.setSkipAppTransitionWhenStarting(z3);
            this.mTargetRootTask.startActivityLocked(this.mStartActivity, focusedRootTask2, z5, z6, this.mOptions, activityRecord2);
            IActivityStarterExt extImpl4 = this.mASWrapper.getExtImpl();
            Request request3 = this.mRequest;
            extImpl4.updateTaskForZoom(activityOptions3, activityRecord2, activityRecord, task4, request3.realCallingPid, request3.callingPackage, focusedRootTask);
            if (this.mDoResume) {
                ActivityRecord activityRecord16 = task4.topRunningActivityLocked();
                if (!this.mTargetRootTask.isTopActivityFocusable() || (activityRecord16 != null && activityRecord16.isTaskOverlay() && this.mStartActivity != activityRecord16)) {
                    this.mTargetRootTask.ensureActivitiesVisible(r8, 0, false);
                    this.mTargetRootTask.mDisplayContent.executeAppTransition();
                } else {
                    if (!this.mAvoidMoveToFront && this.mTargetRootTask.isTopActivityFocusable() && !this.mRootWindowContainer.isTopDisplayFocusedRootTask(this.mTargetRootTask)) {
                        this.mTargetRootTask.moveToFront("startActivityInner");
                    }
                    this.mRootWindowContainer.resumeFocusedTasksTopActivities(this.mTargetRootTask, this.mStartActivity, this.mOptions, this.mTransientLaunch);
                }
            }
            this.mRootWindowContainer.updateUserRootTask(this.mStartActivity.mUserId, this.mTargetRootTask);
            if (this.mStartActivity.getTask() != null) {
                Slog.w(TAG, "startActivityInner: NSTART_ABORTED for task is null, mStartActivity = " + this.mStartActivity);
                return 102;
            }
            this.mASWrapper.getExtImpl().markFlexibleSubTaskIfForceStopNeeded(activityRecord3, this.mStartActivity, r8);
            this.mSupervisor.mRecentTasks.add(task4);
            this.mSupervisor.handleNonResizableTaskIfNeeded(task4, this.mPreferredWindowingMode, this.mPreferredTaskDisplayArea, this.mTargetRootTask);
            ActivityOptions activityOptions5 = this.mOptions;
            if (activityOptions5 == null || !activityOptions5.isLaunchIntoPip() || activityRecord3 == null || activityRecord2.getTask() != this.mStartActivity.getTask() || i2 == 0) {
                return 0;
            }
            this.mRootWindowContainer.moveActivityToPinnedRootTask(this.mStartActivity, activityRecord3, "launch-into-pip");
            return 0;
        }
        activityOptions3 = activityOptions2;
        if (!ActivityTaskManagerService.LTW_DISABLE) {
        }
        if (this.mAvoidMoveToFront) {
        }
        z3 = true;
        r8 = 0;
        this.mService.mUgmInternal.grantUriPermissionUncheckedFromIntent(neededUriGrants3, this.mStartActivity.getUriPermissionsLocked());
        activityRecord5 = this.mStartActivity;
        activityRecord6 = activityRecord5.resultTo;
        if (activityRecord6 == null) {
        }
        if (activityRecord5.mShareIdentity) {
        }
        Task task42 = this.mStartActivity.getTask();
        if (z5) {
        }
        this.mStartActivity.logStartActivity(EventLogTags.WM_CREATE_ACTIVITY, task42);
        this.mStartActivity.getTaskFragment().clearLastPausedActivity();
        this.mRootWindowContainer.startPowerModeLaunchIfNeeded(false, this.mStartActivity);
        if (task42 == task2) {
        }
        IActivityRecordExt extImpl32 = this.mStartActivity.getWrapper().getExtImpl();
        if (this.mAvoidMoveToFront) {
        }
        z3 = false;
        extImpl32.setSkipAppTransitionWhenStarting(z3);
        this.mTargetRootTask.startActivityLocked(this.mStartActivity, focusedRootTask2, z5, z6, this.mOptions, activityRecord2);
        IActivityStarterExt extImpl42 = this.mASWrapper.getExtImpl();
        Request request32 = this.mRequest;
        extImpl42.updateTaskForZoom(activityOptions3, activityRecord2, activityRecord, task42, request32.realCallingPid, request32.callingPackage, focusedRootTask);
        if (this.mDoResume) {
        }
        this.mRootWindowContainer.updateUserRootTask(this.mStartActivity.mUserId, this.mTargetRootTask);
        if (this.mStartActivity.getTask() != null) {
        }
    }

    private Task computeTargetTask() {
        Task task;
        ActivityRecord activityRecord = this.mStartActivity;
        if (activityRecord.resultTo == null && this.mInTask == null && !this.mAddingToTask && (this.mLaunchFlags & 268435456) != 0) {
            return null;
        }
        if (this.mSourceRecord != null) {
            if (this.mASWrapper.getExtImpl().replaceNewTaskIfNeed(this.mSourceRecord, this.mStartActivity)) {
                return null;
            }
            return (!this.mASWrapper.getExtImpl().isAppUnlockActivityFromPocketStudio(this.mSourceRecord, this.mStartActivity) || (task = this.mInTask) == null) ? this.mSourceRecord.getTask() : task;
        }
        Task task2 = this.mInTask;
        if (task2 != null) {
            if (!task2.isAttached()) {
                getOrCreateRootTask(this.mStartActivity, this.mLaunchFlags, this.mInTask, this.mOptions);
            }
            return this.mInTask;
        }
        Task orCreateRootTask = getOrCreateRootTask(activityRecord, this.mLaunchFlags, null, this.mOptions);
        ActivityRecord topNonFinishingActivity = orCreateRootTask.getTopNonFinishingActivity();
        if (topNonFinishingActivity != null) {
            return topNonFinishingActivity.getTask();
        }
        orCreateRootTask.removeIfPossible("computeTargetTask");
        return null;
    }

    private void computeLaunchParams(ActivityRecord activityRecord, ActivityRecord activityRecord2, Task task) {
        TaskDisplayArea defaultTaskDisplayArea;
        this.mOptions = this.mASWrapper.getExtImpl().hookOptionsForSplit(activityRecord, activityRecord2, task, this.mOptions);
        this.mSupervisor.getLaunchParamsController().calculate(task, activityRecord.info.windowLayout, activityRecord, activityRecord2, this.mOptions, this.mRequest, 3, this.mLaunchParams);
        if (this.mLaunchParams.hasPreferredTaskDisplayArea()) {
            defaultTaskDisplayArea = this.mLaunchParams.mPreferredTaskDisplayArea;
        } else {
            defaultTaskDisplayArea = this.mRootWindowContainer.getDefaultTaskDisplayArea();
        }
        this.mPreferredTaskDisplayArea = defaultTaskDisplayArea;
        this.mPreferredWindowingMode = this.mLaunchParams.mWindowingMode;
    }

    @VisibleForTesting
    int isAllowedToStart(ActivityRecord activityRecord, boolean z, Task task) {
        DisplayContent displayContentOrCreate;
        if (activityRecord.packageName == null) {
            ActivityOptions.abort(this.mOptions);
            return -92;
        }
        if (activityRecord.isActivityTypeHome() && !this.mRootWindowContainer.canStartHomeOnDisplayArea(activityRecord.info, this.mPreferredTaskDisplayArea, true)) {
            Slog.w(TAG, "Cannot launch home on display area " + this.mPreferredTaskDisplayArea);
            return -96;
        }
        boolean z2 = z || !task.isUidPresent(this.mCallingUid) || (3 == this.mLaunchMode && task.inPinnedWindowingMode());
        if (this.mBalCode == 0 && z2 && handleBackgroundActivityAbort(activityRecord)) {
            Slog.e(TAG, "Abort background activity starts from " + this.mCallingUid);
            return 102;
        }
        boolean z3 = (this.mLaunchFlags & 268468224) == 268468224;
        if (!z) {
            if (this.mService.getLockTaskController().isLockTaskModeViolation(task, z3)) {
                Slog.e(TAG, "Attempted Lock Task Mode violation r=" + activityRecord);
                return 101;
            }
        } else if (this.mService.getLockTaskController().isNewTaskLockTaskModeViolation(activityRecord)) {
            Slog.e(TAG, "Attempted Lock Task Mode violation r=" + activityRecord);
            return 101;
        }
        if (this.mASWrapper.getExtImpl().interceptActivityForAppShareModeIfNeed(z, z3, task, this.mStartActivity, this.mRootWindowContainer, this.mSourceRootTask, this.mSourceRecord)) {
            Slog.d(TAG, "is In AppShareMirageMode mStartActivity = " + this.mStartActivity);
            return 101;
        }
        if (!this.mASWrapper.getExtImpl().isAllowedToStartActivityInZoom(activityRecord, z, task)) {
            Slog.d(TAG, "zoom root task is exiting mStartActivity = " + this.mStartActivity);
            return -96;
        }
        if (this.mASWrapper.getExtImpl().interceptWhenAnr(this.mService, activityRecord)) {
            Slog.d(TAG, "interceptWhenAnr mStartActivity = " + this.mStartActivity);
            return -96;
        }
        TaskDisplayArea taskDisplayArea = this.mPreferredTaskDisplayArea;
        if (taskDisplayArea != null && (displayContentOrCreate = this.mRootWindowContainer.getDisplayContentOrCreate(taskDisplayArea.getDisplayId())) != null) {
            int windowingMode = task != null ? task.getWindowingMode() : displayContentOrCreate.getWindowingMode();
            ActivityRecord activityRecord2 = this.mSourceRecord;
            if (!displayContentOrCreate.mDwpcHelper.canActivityBeLaunched(activityRecord.info, activityRecord.intent, windowingMode, activityRecord2 != null ? activityRecord2.getDisplayId() : 0, z)) {
                Slog.w(TAG, "Abort to launch " + activityRecord.info.getComponentName() + " on display area " + this.mPreferredTaskDisplayArea);
                return 102;
            }
        }
        return !checkActivitySecurityModel(activityRecord, z, task) ? 102 : 0;
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r1v11, types: [com.android.server.wm.ActivityRecord] */
    private boolean checkActivitySecurityModel(ActivityRecord activityRecord, boolean z, Task task) {
        boolean z2;
        int i;
        ActivityRecord activityRecord2;
        ActivityRecord activityRecord3;
        ActivityRecord activityRecord4;
        boolean z3;
        int i2 = this.mBalCode;
        if (i2 == 2) {
            return true;
        }
        boolean z4 = z || (this.mLaunchFlags & 268435456) == 268435456;
        if (z4 && (i2 == 3 || i2 == 6 || i2 == 5 || i2 == 7 || i2 == 4)) {
            return true;
        }
        ActivityRecord activityRecord5 = this.mSourceRecord;
        if (activityRecord5 != null) {
            Task task2 = activityRecord5.getTask();
            if (!z4 || (task2 != null && (task2.isVisible() || task2 == task))) {
                if (!z4) {
                    task2 = task;
                }
                Pair<Boolean, Boolean> doesTopActivityMatchingUidExistForAsm = ActivityTaskSupervisor.doesTopActivityMatchingUidExistForAsm(task2, this.mSourceRecord.getUid(), this.mSourceRecord);
                z2 = !((Boolean) doesTopActivityMatchingUidExistForAsm.first).booleanValue();
                z3 = !((Boolean) doesTopActivityMatchingUidExistForAsm.second).booleanValue();
            } else {
                z3 = true;
                z2 = true;
            }
            if (!z3) {
                return true;
            }
        } else {
            z2 = true;
        }
        ActivityRecord activity = task == null ? null : task.getActivity(new Predicate() { // from class: com.android.server.wm.ActivityStarter$$ExternalSyntheticLambda2
            @Override // java.util.function.Predicate
            public final boolean test(Object obj) {
                boolean lambda$checkActivitySecurityModel$0;
                lambda$checkActivitySecurityModel$0 = ActivityStarter.lambda$checkActivitySecurityModel$0((ActivityRecord) obj);
                return lambda$checkActivitySecurityModel$0;
            }
        });
        if (z || (activityRecord4 = this.mSourceRecord) == null) {
            i = 3;
        } else {
            i = activityRecord4.getTask().equals(task) ? 1 : 2;
        }
        ActivityRecord activityRecord6 = this.mSourceRecord;
        int uid = activityRecord6 != null ? activityRecord6.getUid() : this.mCallingUid;
        ActivityRecord activityRecord7 = this.mSourceRecord;
        FrameworkStatsLog.write(495, uid, activityRecord7 != null ? activityRecord7.info.name : null, activity != null ? activity.getUid() : -1, activity != null ? activity.info.name : null, z || (activityRecord3 = this.mSourceRecord) == null || task == null || !task.equals(activityRecord3.getTask()), activityRecord.getUid(), activityRecord.info.name, activityRecord.intent.getAction(), this.mLaunchFlags, i, 7, (task == null || (activityRecord2 = this.mSourceRecord) == null || task.equals(activityRecord2.getTask()) || !task.isVisible()) ? false : true, this.mBalCode);
        boolean z5 = ActivitySecurityModelFeatureFlags.shouldRestrictActivitySwitch(this.mCallingUid) && z2;
        String str = activityRecord.launchedFromPackage;
        if (ActivitySecurityModelFeatureFlags.shouldShowToast(this.mCallingUid)) {
            StringBuilder sb = new StringBuilder();
            sb.append("go/android-asm");
            sb.append(z5 ? " blocked " : " would block ");
            sb.append((Object) ActivityTaskSupervisor.getApplicationLabel(this.mService.mContext.getPackageManager(), str));
            final String sb2 = sb.toString();
            UiThread.getHandler().post(new Runnable() { // from class: com.android.server.wm.ActivityStarter$$ExternalSyntheticLambda3
                @Override // java.lang.Runnable
                public final void run() {
                    ActivityStarter.this.lambda$checkActivitySecurityModel$1(sb2);
                }
            });
            logDebugInfoForActivitySecurity("Launch", activityRecord, task, activity, z5, z4);
        }
        if (!z5) {
            return true;
        }
        StringBuilder sb3 = new StringBuilder();
        sb3.append("[ASM] Abort Launching r: ");
        sb3.append(activityRecord);
        sb3.append(" as source: ");
        ?? r1 = this.mSourceRecord;
        if (r1 != 0) {
            str = r1;
        }
        sb3.append((Object) str);
        sb3.append(" is in background. New task: ");
        sb3.append(z);
        sb3.append(". Top activity: ");
        sb3.append(activity);
        sb3.append(". BAL Code: ");
        sb3.append(BackgroundActivityStartController.balCodeToString(this.mBalCode));
        Slog.e(TAG, sb3.toString());
        return false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ boolean lambda$checkActivitySecurityModel$0(ActivityRecord activityRecord) {
        return (activityRecord.finishing || activityRecord.isAlwaysOnTop()) ? false : true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$checkActivitySecurityModel$1(String str) {
        Toast.makeText(this.mService.mContext, str, 1).show();
    }

    private void logDebugInfoForActivitySecurity(String str, final ActivityRecord activityRecord, Task task, final ActivityRecord activityRecord2, boolean z, boolean z2) {
        ActivityRecord activityRecord3;
        final Function function = new Function() { // from class: com.android.server.wm.ActivityStarter$$ExternalSyntheticLambda4
            @Override // java.util.function.Function
            public final Object apply(Object obj) {
                String lambda$logDebugInfoForActivitySecurity$2;
                lambda$logDebugInfoForActivitySecurity$2 = ActivityStarter.this.lambda$logDebugInfoForActivitySecurity$2(activityRecord2, activityRecord, (ActivityRecord) obj);
                return lambda$logDebugInfoForActivitySecurity$2;
            }
        };
        final StringJoiner stringJoiner = new StringJoiner("\n");
        stringJoiner.add("[ASM] ------ Activity Security " + str + " Debug Logging Start ------");
        StringBuilder sb = new StringBuilder();
        sb.append("[ASM] Block Enabled: ");
        sb.append(z);
        stringJoiner.add(sb.toString());
        stringJoiner.add("[ASM] ASM Version: 7");
        boolean z3 = (task == null || (activityRecord3 = this.mSourceRecord) == null || activityRecord3.getTask() != task) ? false : true;
        if (this.mSourceRecord == null) {
            stringJoiner.add("[ASM] Source Package: " + activityRecord.launchedFromPackage);
            stringJoiner.add("[ASM] Real Calling Uid Package: " + this.mService.mContext.getPackageManager().getNameForUid(this.mRealCallingUid));
        } else {
            stringJoiner.add("[ASM] Source Record: " + ((String) function.apply(this.mSourceRecord)));
            if (z3) {
                stringJoiner.add("[ASM] Source/Target Task: " + this.mSourceRecord.getTask());
                stringJoiner.add("[ASM] Source/Target Task Stack: ");
            } else {
                stringJoiner.add("[ASM] Source Task: " + this.mSourceRecord.getTask());
                stringJoiner.add("[ASM] Source Task Stack: ");
            }
            this.mSourceRecord.getTask().forAllActivities(new Consumer() { // from class: com.android.server.wm.ActivityStarter$$ExternalSyntheticLambda5
                @Override // java.util.function.Consumer
                public final void accept(Object obj) {
                    ActivityStarter.lambda$logDebugInfoForActivitySecurity$3(stringJoiner, function, (ActivityRecord) obj);
                }
            });
        }
        stringJoiner.add("[ASM] Target Task Top: " + ((String) function.apply(activityRecord2)));
        if (!z3) {
            stringJoiner.add("[ASM] Target Task: " + task);
            if (task != null) {
                stringJoiner.add("[ASM] Target Task Stack: ");
                task.forAllActivities(new Consumer() { // from class: com.android.server.wm.ActivityStarter$$ExternalSyntheticLambda6
                    @Override // java.util.function.Consumer
                    public final void accept(Object obj) {
                        ActivityStarter.lambda$logDebugInfoForActivitySecurity$4(stringJoiner, function, (ActivityRecord) obj);
                    }
                });
            }
        }
        stringJoiner.add("[ASM] Target Record: " + ((String) function.apply(activityRecord)));
        stringJoiner.add("[ASM] Intent: " + this.mIntent);
        stringJoiner.add("[ASM] TaskToFront: " + z2);
        stringJoiner.add("[ASM] BalCode: " + BackgroundActivityStartController.balCodeToString(this.mBalCode));
        stringJoiner.add("[ASM] ------ Activity Security " + str + " Debug Logging End ------");
        Slog.i(TAG, stringJoiner.toString());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ String lambda$logDebugInfoForActivitySecurity$2(ActivityRecord activityRecord, ActivityRecord activityRecord2, ActivityRecord activityRecord3) {
        if (activityRecord3 == null) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        sb.append(activityRecord3 == this.mSourceRecord ? " [source]=> " : activityRecord3 == activityRecord ? " [ top  ]=> " : activityRecord3 == activityRecord2 ? " [target]=> " : "         => ");
        sb.append(activityRecord3);
        sb.append(" :: visible=");
        sb.append(activityRecord3.isVisible());
        sb.append(", finishing=");
        sb.append(activityRecord3.isFinishing());
        sb.append(", alwaysOnTop=");
        sb.append(activityRecord3.isAlwaysOnTop());
        sb.append(", taskFragment=");
        sb.append(activityRecord3.getTaskFragment());
        return sb.toString();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$logDebugInfoForActivitySecurity$3(StringJoiner stringJoiner, Function function, ActivityRecord activityRecord) {
        stringJoiner.add("[ASM] " + ((String) function.apply(activityRecord)));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$logDebugInfoForActivitySecurity$4(StringJoiner stringJoiner, Function function, ActivityRecord activityRecord) {
        stringJoiner.add("[ASM] " + ((String) function.apply(activityRecord)));
    }

    @VisibleForTesting
    @TaskFragment.EmbeddingCheckResult
    static int canEmbedActivity(TaskFragment taskFragment, ActivityRecord activityRecord, Task task) {
        Task task2 = taskFragment.getTask();
        if (task2 == null || task != task2) {
            return 3;
        }
        return taskFragment.isAllowedToEmbedActivity(activityRecord);
    }

    @VisibleForTesting
    int recycleTask(Task task, ActivityRecord activityRecord, Task task2, NeededUriGrants neededUriGrants) {
        int i = task.mUserId;
        ActivityRecord activityRecord2 = this.mStartActivity;
        if (i != activityRecord2.mUserId) {
            this.mTargetRootTask = task.getRootTask();
            this.mAddingToTask = true;
            return 0;
        }
        if (task2 != null) {
            if (activityRecord2.getWrapper().getExtImpl().getLaunchedFromMultiSearch()) {
                task.getWrapper().getExtImpl().setLaunchedFromMultiSearch(true);
            }
            if (task.intent == null) {
                task.setIntent(this.mStartActivity);
            } else {
                if ((this.mStartActivity.intent.getFlags() & 16384) != 0) {
                    task.intent.addFlags(16384);
                } else {
                    task.intent.removeFlags(16384);
                }
            }
        }
        this.mRootWindowContainer.startPowerModeLaunchIfNeeded(false, activityRecord);
        this.mASWrapper.getExtImpl().setHandleForcedResizableFlag(activityRecord, task2);
        ActivityRecord activityRecord3 = this.mLastStartActivityRecord;
        if (activityRecord3 != null && (activityRecord3.finishing || activityRecord3.noDisplay)) {
            this.mLastStartActivityRecord = activityRecord;
        }
        if ((this.mStartFlags & 1) != 0) {
            if (!this.mMovedToFront && this.mDoResume) {
                if (ProtoLogCache.WM_DEBUG_TASKS_enabled) {
                    ProtoLogImpl.d(ProtoLogGroup.WM_DEBUG_TASKS, -1585311008, 0, (String) null, new Object[]{String.valueOf(this.mTargetRootTask), String.valueOf(activityRecord)});
                }
                this.mTargetRootTask.moveToFront("intentActivityFound");
            }
            resumeTargetRootTaskIfNeeded();
            return 1;
        }
        complyActivityFlags(task, task2 != null ? task2.getTopNonFinishingActivity() : null, neededUriGrants);
        if (this.mAddingToTask) {
            clearTopIfNeeded(task, this.mCallingUid, this.mRealCallingUid, this.mStartActivity.getUid(), this.mLaunchFlags);
            return 0;
        }
        if (activityRecord.finishing) {
            activityRecord = task.getTopNonFinishingActivity();
        }
        if (this.mMovedToFront) {
            this.mASWrapper.getExtImpl().shouldShowStartingwidnowWhenMoveToFront(this.mStartActivity, task2, activityRecord);
        } else if (this.mDoResume) {
            this.mTargetRootTask.moveToFront("intentActivityFound");
        }
        resumeTargetRootTaskIfNeeded();
        if (this.mService.isDreaming() && activityRecord.canTurnScreenOn()) {
            activityRecord.mTaskSupervisor.wakeUp("recycleTask#turnScreenOnFlag");
        }
        this.mLastStartActivityRecord = activityRecord;
        return this.mMovedToFront ? 2 : 3;
    }

    private void clearTopIfNeeded(Task task, final int i, final int i2, final int i3, int i4) {
        if ((i4 & 268435456) != 268435456 || this.mBalCode == 2) {
            return;
        }
        Predicate<ActivityRecord> predicate = new Predicate() { // from class: com.android.server.wm.ActivityStarter$$ExternalSyntheticLambda0
            @Override // java.util.function.Predicate
            public final boolean test(Object obj) {
                boolean lambda$clearTopIfNeeded$5;
                lambda$clearTopIfNeeded$5 = ActivityStarter.lambda$clearTopIfNeeded$5(i3, i, i2, (ActivityRecord) obj);
                return lambda$clearTopIfNeeded$5;
            }
        };
        ActivityRecord topMostActivity = task.getTopMostActivity();
        if (topMostActivity == null || predicate.test(topMostActivity)) {
            return;
        }
        final boolean shouldRestrictActivitySwitch = ActivitySecurityModelFeatureFlags.shouldRestrictActivitySwitch(i);
        int[] iArr = new int[0];
        if (shouldRestrictActivitySwitch) {
            ActivityRecord activity = task.getActivity(predicate);
            if (activity == null) {
                activity = this.mStartActivity;
            }
            int[] iArr2 = new int[1];
            task.performClearTop(activity, i4, iArr2);
            if (iArr2[0] > 0) {
                Slog.w(TAG, "Cleared top n: " + iArr2[0] + " activities from task t: " + task + " not matching top uid: " + i);
            }
            iArr = iArr2;
        }
        if (ActivitySecurityModelFeatureFlags.shouldShowToast(i)) {
            if (!shouldRestrictActivitySwitch || iArr[0] > 0) {
                UiThread.getHandler().post(new Runnable() { // from class: com.android.server.wm.ActivityStarter$$ExternalSyntheticLambda1
                    @Override // java.lang.Runnable
                    public final void run() {
                        ActivityStarter.this.lambda$clearTopIfNeeded$6(shouldRestrictActivitySwitch);
                    }
                });
                logDebugInfoForActivitySecurity("Clear Top", this.mStartActivity, task, topMostActivity, shouldRestrictActivitySwitch, true);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ boolean lambda$clearTopIfNeeded$5(int i, int i2, int i3, ActivityRecord activityRecord) {
        return activityRecord.isUid(i) || activityRecord.isUid(i2) || activityRecord.isUid(i3);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$clearTopIfNeeded$6(boolean z) {
        Context context = this.mService.mContext;
        StringBuilder sb = new StringBuilder();
        sb.append(z ? "Top activities cleared by " : "Top activities would be cleared by ");
        sb.append("go/android-asm");
        Toast.makeText(context, sb.toString(), 1).show();
    }

    private int deliverToCurrentTopIfNeeded(Task task, NeededUriGrants neededUriGrants) {
        ActivityRecord activityRecord = task.topRunningNonDelayedActivityLocked(this.mNotTop);
        if (!(activityRecord != null && activityRecord.mActivityComponent.equals(this.mStartActivity.mActivityComponent) && activityRecord.mUserId == this.mStartActivity.mUserId && activityRecord.attachedToProcess() && ((this.mLaunchFlags & 536870912) != 0 || 1 == this.mLaunchMode) && ((!activityRecord.isActivityTypeHome() || activityRecord.getDisplayArea() == this.mPreferredTaskDisplayArea) && !this.mASWrapper.getExtImpl().canClearActivityRecord(activityRecord)))) {
            return 0;
        }
        activityRecord.getTaskFragment().clearLastPausedActivity();
        if (this.mDoResume) {
            this.mRootWindowContainer.resumeFocusedTasksTopActivities();
        }
        ActivityOptions.abort(this.mOptions);
        if ((this.mStartFlags & 1) != 0) {
            return 1;
        }
        ActivityRecord activityRecord2 = this.mStartActivity;
        ActivityRecord activityRecord3 = activityRecord2.resultTo;
        if (activityRecord3 != null) {
            activityRecord3.sendResult(-1, activityRecord2.resultWho, activityRecord2.requestCode, 0, null, null);
            this.mStartActivity.resultTo = null;
        }
        deliverNewIntent(activityRecord, neededUriGrants);
        this.mSupervisor.handleNonResizableTaskIfNeeded(activityRecord.getTask(), this.mLaunchParams.mWindowingMode, this.mPreferredTaskDisplayArea, task);
        return 3;
    }

    private void complyActivityFlags(Task task, ActivityRecord activityRecord, NeededUriGrants neededUriGrants) {
        ActivityRecord topNonFinishingActivity = task.getTopNonFinishingActivity();
        boolean z = (activityRecord == null || (this.mLaunchFlags & 2097152) == 0) ? false : true;
        if (z) {
            topNonFinishingActivity = this.mTargetRootTask.resetTaskIfNeeded(topNonFinishingActivity, this.mStartActivity);
        }
        int i = this.mLaunchFlags;
        if ((i & 268468224) == 268468224) {
            if (!ActivityTaskManagerService.LTW_DISABLE) {
                if (!this.mService.getWrapper().getExtImpl().getRemoteTaskManager().inAnyInterceptSession()) {
                    task.performClearTaskForReuse(true);
                }
            } else {
                task.performClearTaskForReuse(true);
            }
            task.setIntent(this.mStartActivity);
            this.mAddingToTask = true;
            this.mIsTaskCleared = true;
            return;
        }
        if ((i & 67108864) != 0 || isDocumentLaunchesIntoExisting(i) || isLaunchModeOneOf(3, 2, 4)) {
            if (!ActivityTaskManagerService.LTW_DISABLE && this.mService.getWrapper().getExtImpl().getRemoteTaskManager().inAnyInterceptSession() && (this.mLaunchFlags & 67108864) == 0) {
                return;
            }
            int[] iArr = new int[1];
            ActivityRecord performClearTop = task.performClearTop(this.mStartActivity, this.mLaunchFlags, iArr);
            if (performClearTop != null && !performClearTop.finishing) {
                if (iArr[0] > 0) {
                    this.mMovedToTopActivity = performClearTop;
                }
                if (performClearTop.isRootOfTask()) {
                    performClearTop.getTask().setIntent(this.mStartActivity);
                }
                deliverNewIntent(performClearTop, neededUriGrants);
                return;
            }
            this.mAddingToTask = true;
            if (performClearTop != null && performClearTop.getTaskFragment() != null && performClearTop.getTaskFragment().isEmbedded()) {
                this.mAddingToTaskFragment = performClearTop.getTaskFragment();
            }
            if (task.getRootTask() == null) {
                Task orCreateRootTask = getOrCreateRootTask(this.mStartActivity, this.mLaunchFlags, null, this.mOptions);
                this.mTargetRootTask = orCreateRootTask;
                orCreateRootTask.addChild(task, !this.mLaunchTaskBehind, (this.mStartActivity.info.flags & 1024) != 0);
                return;
            }
            return;
        }
        int i2 = this.mLaunchFlags;
        if ((67108864 & i2) == 0 && !this.mAddingToTask && (i2 & 131072) != 0) {
            ActivityRecord activityRecord2 = this.mStartActivity;
            ActivityRecord findActivityInHistory = task.findActivityInHistory(activityRecord2.mActivityComponent, activityRecord2.mUserId);
            if (findActivityInHistory != null) {
                if (findActivityInHistory.getTask().moveActivityToFront(findActivityInHistory)) {
                    this.mMovedToTopActivity = findActivityInHistory;
                    if (this.mNoAnimation) {
                        findActivityInHistory.mDisplayContent.prepareAppTransition(0);
                    } else {
                        findActivityInHistory.mDisplayContent.prepareAppTransition(3);
                    }
                }
                findActivityInHistory.updateOptionsLocked(this.mOptions);
                deliverNewIntent(findActivityInHistory, neededUriGrants);
                findActivityInHistory.getTaskFragment().clearLastPausedActivity();
                return;
            }
            this.mAddingToTask = true;
            return;
        }
        if (!this.mStartActivity.mActivityComponent.equals(task.realActivity)) {
            if (!z) {
                this.mAddingToTask = true;
                return;
            } else {
                if (task.rootWasReset) {
                    return;
                }
                task.setIntent(this.mStartActivity);
                return;
            }
        }
        if (task == this.mInTask) {
            return;
        }
        if (((this.mLaunchFlags & 536870912) != 0 || 1 == this.mLaunchMode) && topNonFinishingActivity.mActivityComponent.equals(this.mStartActivity.mActivityComponent) && this.mStartActivity.resultTo == null) {
            if (topNonFinishingActivity.isRootOfTask()) {
                topNonFinishingActivity.getTask().setIntent(this.mStartActivity);
            }
            deliverNewIntent(topNonFinishingActivity, neededUriGrants);
        } else if (!task.isSameIntentFilter(this.mStartActivity)) {
            this.mAddingToTask = true;
        } else if (activityRecord == null) {
            this.mAddingToTask = true;
        }
    }

    void reset(boolean z) {
        this.mStartActivity = null;
        this.mIntent = null;
        this.mCallingUid = -1;
        this.mRealCallingUid = -1;
        this.mOptions = null;
        this.mBalCode = 1;
        this.mLaunchTaskBehind = false;
        this.mLaunchFlags = 0;
        this.mLaunchMode = -1;
        this.mLaunchParams.reset();
        this.mNotTop = null;
        this.mDoResume = false;
        this.mStartFlags = 0;
        this.mSourceRecord = null;
        this.mPreferredTaskDisplayArea = null;
        this.mPreferredWindowingMode = 0;
        this.mInTask = null;
        this.mInTaskFragment = null;
        this.mAddingToTask = false;
        this.mAddingToTaskFragment = null;
        this.mSourceRootTask = null;
        this.mTargetRootTask = null;
        this.mTargetTask = null;
        this.mIsTaskCleared = false;
        this.mMovedToFront = false;
        this.mNoAnimation = false;
        this.mAvoidMoveToFront = false;
        this.mFrozeTaskList = false;
        this.mTransientLaunch = false;
        this.mPriorAboveTask = null;
        this.mDisplayLockAndOccluded = false;
        this.mVoiceSession = null;
        this.mVoiceInteractor = null;
        this.mIntentDelivered = false;
        if (z) {
            this.mRequest.reset();
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:102:0x025d  */
    /* JADX WARN: Removed duplicated region for block: B:112:0x0287  */
    /* JADX WARN: Removed duplicated region for block: B:122:0x0289  */
    /* JADX WARN: Removed duplicated region for block: B:123:0x0210  */
    /* JADX WARN: Removed duplicated region for block: B:89:0x020e  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private void setInitialState(ActivityRecord activityRecord, ActivityOptions activityOptions, Task task, TaskFragment taskFragment, int i, ActivityRecord activityRecord2, IVoiceInteractionSession iVoiceInteractionSession, IVoiceInteractor iVoiceInteractor, int i2, int i3) {
        TaskDisplayArea defaultTaskDisplayArea;
        TaskFragment taskFragment2;
        Task task2;
        Task topDisplayFocusedRootTask;
        ActivityRecord activityRecord3;
        ActivityRecord activityRecord4 = activityRecord2;
        reset(false);
        this.mASWrapper.getExtImpl().setInitialState(activityRecord, activityOptions, task, activityRecord2, this.mService.mLastResumedActivity);
        this.mStartActivity = activityRecord;
        this.mIntent = activityRecord.intent;
        this.mOptions = activityOptions;
        this.mCallingUid = activityRecord.launchedFromUid;
        this.mRealCallingUid = i3;
        this.mSourceRecord = activityRecord4;
        this.mSourceRootTask = activityRecord4 != null ? activityRecord2.getRootTask() : null;
        this.mVoiceSession = iVoiceInteractionSession;
        this.mVoiceInteractor = iVoiceInteractor;
        this.mBalCode = i2;
        this.mLaunchParams.reset();
        this.mSupervisor.getLaunchParamsController().calculate(task, activityRecord.info.windowLayout, activityRecord, activityRecord2, activityOptions, this.mRequest, 0, this.mLaunchParams);
        if (this.mLaunchParams.hasPreferredTaskDisplayArea()) {
            defaultTaskDisplayArea = this.mLaunchParams.mPreferredTaskDisplayArea;
        } else {
            defaultTaskDisplayArea = this.mRootWindowContainer.getDefaultTaskDisplayArea();
        }
        this.mPreferredTaskDisplayArea = defaultTaskDisplayArea;
        this.mPreferredWindowingMode = this.mLaunchParams.mWindowingMode;
        int i4 = activityRecord.launchMode;
        this.mLaunchMode = i4;
        this.mLaunchFlags = adjustLaunchFlagsToDocumentMode(activityRecord, 3 == i4, 2 == i4, this.mIntent.getFlags());
        this.mLaunchTaskBehind = (!activityRecord.mLaunchTaskBehind || isLaunchModeOneOf(2, 3) || (this.mLaunchFlags & 524288) == 0) ? false : true;
        if (this.mLaunchMode == 4) {
            this.mLaunchFlags |= 268435456;
        }
        String str = activityRecord.info.requiredDisplayCategory;
        if (str != null && (activityRecord3 = this.mSourceRecord) != null && !str.equals(activityRecord3.info.requiredDisplayCategory)) {
            this.mLaunchFlags |= 268435456;
        }
        sendNewTaskResultRequestIfNeeded();
        int i5 = this.mLaunchFlags;
        if ((i5 & 524288) != 0 && activityRecord.resultTo == null) {
            this.mLaunchFlags = i5 | 268435456;
        }
        int i6 = this.mLaunchFlags;
        if ((i6 & 268435456) != 0 && (this.mLaunchTaskBehind || activityRecord.info.documentLaunchMode == 2)) {
            this.mLaunchFlags = i6 | 134217728;
        }
        this.mSupervisor.mUserLeaving = (this.mLaunchFlags & 262144) == 0;
        if (ActivityTaskManagerDebugConfig.DEBUG_USER_LEAVING) {
            Slog.v(TAG_USER_LEAVING, "startActivity() => mUserLeaving=" + this.mSupervisor.mUserLeaving);
        }
        if (!activityRecord.showToCurrentUser() || this.mLaunchTaskBehind) {
            activityRecord.delayedResume = true;
            this.mDoResume = false;
        } else {
            this.mDoResume = true;
        }
        ActivityOptions activityOptions2 = this.mOptions;
        if (activityOptions2 != null) {
            if (activityOptions2.getLaunchTaskId() != -1 && this.mOptions.getTaskOverlay()) {
                activityRecord.setTaskOverlay(true);
                if (!this.mOptions.canTaskOverlayResume()) {
                    Task anyTaskForId = this.mRootWindowContainer.anyTaskForId(this.mOptions.getLaunchTaskId());
                    ActivityRecord topNonFinishingActivity = anyTaskForId != null ? anyTaskForId.getTopNonFinishingActivity() : null;
                    if (topNonFinishingActivity != null && !topNonFinishingActivity.isState(ActivityRecord.State.RESUMED)) {
                        this.mDoResume = false;
                        this.mAvoidMoveToFront = true;
                    }
                }
            } else if (this.mOptions.getAvoidMoveToFront()) {
                this.mDoResume = false;
                this.mAvoidMoveToFront = true;
            }
            this.mTransientLaunch = this.mOptions.getTransientLaunch();
            KeyguardController keyguardController = this.mSupervisor.getKeyguardController();
            int displayId = this.mPreferredTaskDisplayArea.getDisplayId();
            boolean z = keyguardController.isKeyguardLocked(displayId) && keyguardController.isDisplayOccluded(displayId);
            this.mDisplayLockAndOccluded = z;
            if (this.mTransientLaunch && z && this.mService.getTransitionController().isShellTransitionsEnabled()) {
                this.mDoResume = false;
                this.mAvoidMoveToFront = true;
            }
            this.mTargetRootTask = Task.fromWindowContainerToken(this.mOptions.getLaunchRootTask());
            if (taskFragment == null) {
                taskFragment2 = TaskFragment.fromTaskFragmentToken(this.mOptions.getLaunchTaskFragmentToken(), this.mService);
                if (taskFragment2 != null && taskFragment2.isEmbeddedTaskFragmentInPip()) {
                    Slog.w(TAG, "Can not start activity in TaskFragment in PIP: " + taskFragment2);
                    taskFragment2 = null;
                }
                this.mNotTop = (this.mLaunchFlags & 16777216) == 0 ? activityRecord4 : null;
                this.mInTask = task;
                if (task != null && !task.inRecents) {
                    Slog.w(TAG, "Starting activity in task not in recents: " + task);
                    this.mInTask = null;
                }
                task2 = this.mInTask;
                if (task2 != null && !task2.isSameRequiredDisplayCategory(activityRecord.info)) {
                    Slog.w(TAG, "Starting activity in task with different display category: " + this.mInTask);
                    this.mInTask = null;
                }
                this.mInTaskFragment = taskFragment2;
                this.mStartFlags = i;
                if ((i & 1) != 0) {
                    if (activityRecord4 == null && (topDisplayFocusedRootTask = this.mRootWindowContainer.getTopDisplayFocusedRootTask()) != null) {
                        activityRecord4 = topDisplayFocusedRootTask.topRunningNonDelayedActivityLocked(this.mNotTop);
                    }
                    if (activityRecord4 == null || !activityRecord4.mActivityComponent.equals(activityRecord.mActivityComponent)) {
                        this.mStartFlags &= -2;
                    }
                }
                this.mNoAnimation = (this.mLaunchFlags & 65536) == 0;
                if (this.mBalCode == 0 || this.mService.isBackgroundActivityStartsEnabled()) {
                }
                this.mAvoidMoveToFront = true;
                this.mDoResume = false;
                return;
            }
        }
        taskFragment2 = taskFragment;
        this.mNotTop = (this.mLaunchFlags & 16777216) == 0 ? activityRecord4 : null;
        this.mInTask = task;
        if (task != null) {
            Slog.w(TAG, "Starting activity in task not in recents: " + task);
            this.mInTask = null;
        }
        task2 = this.mInTask;
        if (task2 != null) {
            Slog.w(TAG, "Starting activity in task with different display category: " + this.mInTask);
            this.mInTask = null;
        }
        this.mInTaskFragment = taskFragment2;
        this.mStartFlags = i;
        if ((i & 1) != 0) {
        }
        this.mNoAnimation = (this.mLaunchFlags & 65536) == 0;
        if (this.mBalCode == 0) {
        }
    }

    private void sendNewTaskResultRequestIfNeeded() {
        if (this.mStartActivity.resultTo == null || (this.mLaunchFlags & 268435456) == 0) {
            return;
        }
        Slog.w(TAG, "Activity is launching as a new task, so cancelling activity result.");
        ActivityRecord activityRecord = this.mStartActivity;
        activityRecord.resultTo.sendResult(-1, activityRecord.resultWho, activityRecord.requestCode, 0, null, null);
        this.mStartActivity.resultTo = null;
    }

    private void computeLaunchingTaskFlags() {
        ActivityRecord activityRecord;
        Task task;
        if (this.mSourceRecord == null && (task = this.mInTask) != null && task.getRootTask() != null) {
            Intent baseIntent = this.mInTask.getBaseIntent();
            ActivityRecord rootActivity = this.mInTask.getRootActivity();
            if (baseIntent == null) {
                ActivityOptions.abort(this.mOptions);
                throw new IllegalArgumentException("Launching into task without base intent: " + this.mInTask);
            }
            if (isLaunchModeOneOf(3, 2)) {
                if (!baseIntent.getComponent().equals(this.mStartActivity.intent.getComponent())) {
                    ActivityOptions.abort(this.mOptions);
                    throw new IllegalArgumentException("Trying to launch singleInstance/Task " + this.mStartActivity + " into different task " + this.mInTask);
                }
                if (rootActivity != null) {
                    ActivityOptions.abort(this.mOptions);
                    throw new IllegalArgumentException("Caller with mInTask " + this.mInTask + " has root " + rootActivity + " but target is singleInstance/Task");
                }
            }
            if (rootActivity == null) {
                int flags = (baseIntent.getFlags() & 403185664) | (this.mLaunchFlags & (-403185665));
                this.mLaunchFlags = flags;
                this.mIntent.setFlags(flags);
                this.mInTask.setIntent(this.mStartActivity);
                this.mAddingToTask = true;
            } else if ((this.mLaunchFlags & 268435456) != 0) {
                this.mAddingToTask = false;
            } else {
                this.mAddingToTask = true;
            }
        } else {
            this.mInTask = null;
            if ((this.mStartActivity.isResolverOrDelegateActivity() || this.mStartActivity.noDisplay) && (activityRecord = this.mSourceRecord) != null && activityRecord.inFreeformWindowingMode()) {
                this.mAddingToTask = true;
            }
        }
        Task task2 = this.mInTask;
        if (task2 == null) {
            ActivityRecord activityRecord2 = this.mSourceRecord;
            if (activityRecord2 == null) {
                if ((this.mLaunchFlags & 268435456) == 0 && task2 == null) {
                    Slog.w(TAG, "startActivity called from non-Activity context; forcing Intent.FLAG_ACTIVITY_NEW_TASK for: " + this.mIntent);
                    this.mLaunchFlags = this.mLaunchFlags | 268435456;
                }
            } else if (activityRecord2.launchMode == 3) {
                this.mLaunchFlags |= 268435456;
            } else if (isLaunchModeOneOf(3, 2) && !this.mASWrapper.getExtImpl().newTaskFlagDisable(this.mStartActivity, this.mSourceRecord)) {
                this.mLaunchFlags |= 268435456;
            }
        }
        int i = this.mLaunchFlags;
        if ((i & 4096) != 0) {
            if ((i & 268435456) == 0 || this.mSourceRecord == null) {
                this.mLaunchFlags = i & (-4097);
            }
        }
    }

    private Task getReusableTask() {
        ActivityRecord findTaskForReuseIfNeeded;
        ActivityOptions activityOptions = this.mOptions;
        if (activityOptions != null && activityOptions.getLaunchTaskId() != -1) {
            Task anyTaskForId = this.mRootWindowContainer.anyTaskForId(this.mOptions.getLaunchTaskId());
            if (anyTaskForId != null) {
                return anyTaskForId;
            }
            return null;
        }
        int i = this.mLaunchFlags;
        if ((((268435456 & i) != 0 && (i & 134217728) == 0) || isLaunchModeOneOf(3, 2)) & (this.mInTask == null && this.mStartActivity.resultTo == null)) {
            int i2 = this.mLaunchMode;
            if (3 == i2) {
                RootWindowContainer rootWindowContainer = this.mRootWindowContainer;
                Intent intent = this.mIntent;
                ActivityRecord activityRecord = this.mStartActivity;
                findTaskForReuseIfNeeded = rootWindowContainer.findActivity(intent, activityRecord.info, activityRecord.isActivityTypeHome());
            } else if ((this.mLaunchFlags & 4096) != 0) {
                findTaskForReuseIfNeeded = this.mRootWindowContainer.findActivity(this.mIntent, this.mStartActivity.info, 2 != i2);
            } else {
                findTaskForReuseIfNeeded = this.mRootWindowContainer.findTask(this.mStartActivity, this.mPreferredTaskDisplayArea);
            }
        } else {
            findTaskForReuseIfNeeded = !ActivityTaskManagerService.LTW_DISABLE ? this.mService.getWrapper().getExtImpl().getRemoteTaskManager().findTaskForReuseIfNeeded(this.mStartActivity, this.mOptions, this.mPreferredTaskDisplayArea, this.mLaunchFlags) : null;
        }
        if (findTaskForReuseIfNeeded != null && this.mLaunchMode == 4 && !findTaskForReuseIfNeeded.getTask().getRootActivity().mActivityComponent.equals(this.mStartActivity.mActivityComponent)) {
            findTaskForReuseIfNeeded = null;
        }
        if (findTaskForReuseIfNeeded != null && ((this.mStartActivity.isActivityTypeHome() || findTaskForReuseIfNeeded.isActivityTypeHome()) && findTaskForReuseIfNeeded.getDisplayArea() != this.mPreferredTaskDisplayArea)) {
            findTaskForReuseIfNeeded = null;
        }
        ActivityRecord isAppUnlockPasswordActivity = this.mASWrapper.getExtImpl().isAppUnlockPasswordActivity(findTaskForReuseIfNeeded, this.mSourceRecord, this.mStartActivity, this.mInTask);
        if (isAppUnlockPasswordActivity != null) {
            return isAppUnlockPasswordActivity.getTask();
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Removed duplicated region for block: B:52:0x00cd  */
    /* JADX WARN: Removed duplicated region for block: B:72:0x013c  */
    /* JADX WARN: Removed duplicated region for block: B:82:0x017f  */
    /* JADX WARN: Removed duplicated region for block: B:85:0x0185  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void setTargetRootTaskIfNeeded(ActivityRecord activityRecord) {
        boolean z;
        IBinder iBinder;
        RemoteAnimationAdapter remoteAnimationAdapter;
        ActivityRecord activityRecord2;
        ActivityRecord activityRecord3;
        WindowContainerToken windowContainerToken;
        if (activityRecord.getTaskFragment() != null) {
            activityRecord.getTaskFragment().clearLastPausedActivity();
        }
        Task task = activityRecord.getTask();
        Task rootTask = task != null ? task.getRootTask() : null;
        if (this.mTargetRootTask == null) {
            ActivityRecord activityRecord4 = this.mSourceRecord;
            if (activityRecord4 != null && (windowContainerToken = activityRecord4.mLaunchRootTask) != null) {
                this.mTargetRootTask = Task.fromWindowContainerToken(windowContainerToken);
            } else {
                this.mTargetRootTask = getOrCreateRootTask(this.mStartActivity, this.mLaunchFlags, task, this.mOptions);
            }
        }
        Task adjacentTask = this.mTargetRootTask.getAdjacentTask();
        if (adjacentTask != null && activityRecord.isDescendantOf(adjacentTask) && task.isOnTop()) {
            this.mTargetRootTask = adjacentTask;
        }
        boolean z2 = false;
        if (this.mTargetRootTask.getDisplayArea() == this.mPreferredTaskDisplayArea) {
            Task focusedRootTask = this.mTargetRootTask.mDisplayContent.getFocusedRootTask();
            ActivityRecord activityRecord5 = focusedRootTask == null ? null : focusedRootTask.topRunningNonDelayedActivityLocked(this.mNotTop);
            Task task2 = activityRecord5 != null ? activityRecord5.getTask() : null;
            if (task2 == task && ((focusedRootTask == null || task2 == focusedRootTask.getTopMostTask()) && ((focusedRootTask == null || focusedRootTask == rootTask) && !this.mASWrapper.getExtImpl().getSubDifferentTopTask(task, this.mPreferredTaskDisplayArea, this.mOptions, this.mSourceRecord)))) {
                z = false;
                if (z && !this.mAvoidMoveToFront) {
                    this.mStartActivity.intent.addFlags(4194304);
                    activityRecord2 = this.mSourceRecord;
                    if (activityRecord2 != null || inTopNonFinishingTask(activityRecord2)) {
                        if (this.mLaunchTaskBehind && (activityRecord3 = this.mSourceRecord) != null) {
                            activityRecord.setTaskToAffiliateWith(activityRecord3.getTask());
                        }
                        if (!activityRecord.isDescendantOf(this.mTargetRootTask)) {
                            Task task3 = this.mTargetRootTask;
                            if (task3 != task && task3 != task.getParent().asTask()) {
                                task.getParent().positionChildAt(Integer.MAX_VALUE, task, false);
                                task = task.getParent().asTaskFragment().getTask();
                            }
                            if (activityRecord.isVisibleRequested() && activityRecord.inMultiWindowMode() && activityRecord == this.mTargetRootTask.topRunningActivity()) {
                                z2 = true;
                            }
                            boolean z3 = this.mNoAnimation;
                            if (!ActivityTaskManagerService.LTW_DISABLE) {
                                z3 = this.mService.getWrapper().getExtImpl().getRemoteTaskManager().isDisplaySwitchDetected() ? true : this.mNoAnimation;
                            }
                            this.mTargetRootTask.moveTaskToFront(task, z3, this.mOptions, this.mStartActivity.appTimeTracker, true, "bringingFoundTaskToFront");
                            this.mMovedToFront = !z2;
                        } else if (activityRecord.getWindowingMode() != 2 && !this.mASWrapper.mASExt.notReparentForComapctWindow(task, activityRecord, this.mTargetRootTask)) {
                            task.reparent(this.mTargetRootTask, true, 0, true, true, "reparentToTargetRootTask");
                            if (OplusPairTaskManager.isPairTaskEnabled()) {
                                this.mTargetRootTask.moveTaskToFront(task, this.mNoAnimation, this.mOptions, this.mStartActivity.appTimeTracker, true, "bringingFoundTaskToFront");
                            }
                            this.mMovedToFront = true;
                        }
                        this.mOptions = null;
                    }
                }
                Task task4 = task;
                ActivityRecord activityRecord6 = this.mStartActivity;
                iBinder = activityRecord6.mLaunchCookie;
                if (iBinder != null) {
                    activityRecord.mLaunchCookie = iBinder;
                }
                remoteAnimationAdapter = activityRecord6.mPendingRemoteAnimation;
                if (remoteAnimationAdapter != null) {
                    activityRecord.mPendingRemoteAnimation = remoteAnimationAdapter;
                }
                this.mTargetRootTask = activityRecord.getRootTask();
                this.mASWrapper.getExtImpl().handleNonResizableTask(this.mSupervisor, task4, 0, this.mRootWindowContainer.getDefaultTaskDisplayArea(), this.mTargetRootTask);
            }
        }
        z = true;
        if (z) {
            this.mStartActivity.intent.addFlags(4194304);
            activityRecord2 = this.mSourceRecord;
            if (activityRecord2 != null) {
            }
            if (this.mLaunchTaskBehind) {
                activityRecord.setTaskToAffiliateWith(activityRecord3.getTask());
            }
            if (!activityRecord.isDescendantOf(this.mTargetRootTask)) {
            }
            this.mOptions = null;
        }
        Task task42 = task;
        ActivityRecord activityRecord62 = this.mStartActivity;
        iBinder = activityRecord62.mLaunchCookie;
        if (iBinder != null) {
        }
        remoteAnimationAdapter = activityRecord62.mPendingRemoteAnimation;
        if (remoteAnimationAdapter != null) {
        }
        this.mTargetRootTask = activityRecord.getRootTask();
        this.mASWrapper.getExtImpl().handleNonResizableTask(this.mSupervisor, task42, 0, this.mRootWindowContainer.getDefaultTaskDisplayArea(), this.mTargetRootTask);
    }

    private boolean inTopNonFinishingTask(ActivityRecord activityRecord) {
        if (activityRecord == null || activityRecord.getTask() == null) {
            return false;
        }
        Task task = activityRecord.getTask();
        Task createdByOrganizerTask = task.getCreatedByOrganizerTask() != null ? task.getCreatedByOrganizerTask() : activityRecord.getRootTask();
        ActivityRecord topNonFinishingActivity = createdByOrganizerTask != null ? createdByOrganizerTask.getTopNonFinishingActivity() : null;
        return topNonFinishingActivity != null && topNonFinishingActivity.getTask() == task;
    }

    private void resumeTargetRootTaskIfNeeded() {
        if (this.mDoResume) {
            ActivityRecord activityRecord = this.mTargetRootTask.topRunningActivity(true);
            if (activityRecord != null) {
                activityRecord.setCurrentLaunchCanTurnScreenOn(true);
            }
            if (this.mTargetRootTask.isFocusable()) {
                this.mRootWindowContainer.resumeFocusedTasksTopActivities(this.mTargetRootTask, null, this.mOptions, this.mTransientLaunch);
            } else {
                this.mRootWindowContainer.ensureActivitiesVisible(null, 0, false);
            }
        } else {
            ActivityOptions.abort(this.mOptions);
        }
        this.mRootWindowContainer.updateUserRootTask(this.mStartActivity.mUserId, this.mTargetRootTask);
    }

    private void setNewTask(Task task) {
        boolean z = (this.mLaunchTaskBehind || this.mAvoidMoveToFront) ? false : true;
        Task task2 = this.mTargetRootTask;
        ActivityRecord activityRecord = this.mStartActivity;
        Task reuseOrCreateTask = task2.reuseOrCreateTask(activityRecord.info, this.mIntent, this.mVoiceSession, this.mVoiceInteractor, z, activityRecord, this.mSourceRecord, this.mOptions);
        reuseOrCreateTask.mTransitionController.collectExistenceChange(reuseOrCreateTask);
        addOrReparentStartingActivity(reuseOrCreateTask, "setTaskFromReuseOrCreateNewTask");
        if (ProtoLogCache.WM_DEBUG_TASKS_enabled) {
            ProtoLogImpl.v(ProtoLogGroup.WM_DEBUG_TASKS, -1304806505, 0, (String) null, new Object[]{String.valueOf(this.mStartActivity), String.valueOf(this.mStartActivity.getTask())});
        }
        if (task != null) {
            this.mStartActivity.setTaskToAffiliateWith(task);
        }
    }

    private void deliverNewIntent(ActivityRecord activityRecord, NeededUriGrants neededUriGrants) {
        if (this.mIntentDelivered) {
            return;
        }
        activityRecord.logStartActivity(EventLogTags.WM_NEW_INTENT, activityRecord.getTask());
        int i = this.mCallingUid;
        ActivityRecord activityRecord2 = this.mStartActivity;
        activityRecord.deliverNewIntentLocked(i, activityRecord2.intent, neededUriGrants, activityRecord2.launchedFromPackage);
        this.mIntentDelivered = true;
    }

    /* JADX WARN: Code restructure failed: missing block: B:31:0x0067, code lost:
    
        if (canEmbedActivity(r0, r7.mStartActivity, r8) == 0) goto L26;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private void addOrReparentStartingActivity(Task task, String str) {
        TaskFragment taskFragment;
        IActivityRecordSocExt socExtImpl = this.mStartActivity.getWrapper().getSocExtImpl();
        ActivityRecord activityRecord = this.mStartActivity;
        socExtImpl.acquireActivityBoost(activityRecord.packageName, activityRecord.app, activityRecord.info, this.mService, activityRecord.processName);
        TaskFragment taskFragment2 = this.mInTaskFragment;
        if (taskFragment2 != null) {
            int canEmbedActivity = canEmbedActivity(taskFragment2, this.mStartActivity, task);
            if (canEmbedActivity == 0) {
                taskFragment = this.mInTaskFragment;
                this.mStartActivity.mRequestedLaunchingTaskFragmentToken = taskFragment.getFragmentToken();
            } else {
                sendCanNotEmbedActivityError(this.mInTaskFragment, canEmbedActivity);
                taskFragment = task;
            }
        } else {
            taskFragment = this.mAddingToTaskFragment;
            if (taskFragment == null) {
                taskFragment = null;
            }
            if (taskFragment == null) {
                ActivityRecord activityRecord2 = task.topRunningActivity(false);
                if (activityRecord2 != null) {
                    taskFragment = activityRecord2.getTaskFragment();
                }
                if (!this.mASWrapper.getExtImpl().canAddingToTaskFragment(task, taskFragment, this.mStartActivity)) {
                    taskFragment = null;
                }
            }
            if (taskFragment != null) {
                if (taskFragment.isEmbedded()) {
                }
            }
            taskFragment = task;
        }
        TaskFragment modifyParentForEmbeddingSettingIfNeed = this.mASWrapper.getExtImpl().modifyParentForEmbeddingSettingIfNeed(this.mStartActivity, task, taskFragment);
        this.mStartActivity.getWrapper().getExtImpl().setSourceRecordHint(this.mSourceRecord);
        if (this.mStartActivity.getTaskFragment() == null || this.mStartActivity.getTaskFragment() == modifyParentForEmbeddingSettingIfNeed) {
            modifyParentForEmbeddingSettingIfNeed.addChild(this.mStartActivity, Integer.MAX_VALUE);
        } else {
            this.mStartActivity.reparent(modifyParentForEmbeddingSettingIfNeed, modifyParentForEmbeddingSettingIfNeed.getChildCount(), str);
        }
    }

    private void sendCanNotEmbedActivityError(TaskFragment taskFragment, @TaskFragment.EmbeddingCheckResult int i) {
        String str;
        if (i == 1) {
            str = "The app:" + this.mCallingUid + "is not trusted to " + this.mStartActivity;
        } else if (i == 2) {
            str = "Cannot embed " + this.mStartActivity + ". TaskFragment's bounds:" + taskFragment.getBounds() + ", minimum dimensions:" + this.mStartActivity.getMinDimensions();
        } else if (i == 3) {
            str = "Cannot embed " + this.mStartActivity + " that launched on another task,mLaunchMode=" + ActivityInfo.launchModeToString(this.mLaunchMode) + ",mLaunchFlag=" + Integer.toHexString(this.mLaunchFlags);
        } else {
            str = "Unhandled embed result:" + i;
        }
        if (taskFragment.isOrganized()) {
            this.mService.mWindowOrganizerController.sendTaskFragmentOperationFailure(taskFragment.getTaskFragmentOrganizer(), this.mRequest.errorCallbackToken, taskFragment, 2, new SecurityException(str));
        } else {
            Slog.w(TAG, str);
        }
    }

    private int adjustLaunchFlagsToDocumentMode(ActivityRecord activityRecord, boolean z, boolean z2, int i) {
        int i2 = i & 524288;
        if (i2 != 0 && (z || z2)) {
            Slog.i(TAG, "Ignoring FLAG_ACTIVITY_NEW_DOCUMENT, launchMode is \"singleInstance\" or \"singleTask\"");
        } else {
            int i3 = activityRecord.info.documentLaunchMode;
            if (i3 == 1 || i3 == 2) {
                return i | 524288;
            }
            if (i3 != 3) {
                return i;
            }
            if (this.mLaunchMode == 4 && i2 == 0) {
                return i;
            }
        }
        return i & (-134742017);
    }

    private Task getOrCreateRootTask(ActivityRecord activityRecord, int i, Task task, ActivityOptions activityOptions) {
        boolean scenarioTaskOrder = this.mASWrapper.getExtImpl().getScenarioTaskOrder(task, this.mPreferredTaskDisplayArea, this.mRequest.callingPackage, this.mOptions, this.mSourceRecord, (activityOptions == null || !activityOptions.getAvoidMoveToFront()) && !this.mLaunchTaskBehind);
        ActivityRecord activityRecord2 = this.mSourceRecord;
        return this.mRootWindowContainer.getOrCreateRootTask(activityRecord, activityOptions, task, activityRecord2 != null ? activityRecord2.getTask() : null, scenarioTaskOrder, this.mLaunchParams, i);
    }

    private boolean isLaunchModeOneOf(int i, int i2) {
        int i3 = this.mLaunchMode;
        return i == i3 || i2 == i3;
    }

    private boolean isLaunchModeOneOf(int i, int i2, int i3) {
        int i4 = this.mLaunchMode;
        return i == i4 || i2 == i4 || i3 == i4;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public ActivityStarter setIntent(Intent intent) {
        this.mRequest.intent = intent;
        return this;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Intent getIntent() {
        return this.mRequest.intent;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public ActivityStarter setIntentGrants(NeededUriGrants neededUriGrants) {
        this.mRequest.intentGrants = neededUriGrants;
        return this;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public ActivityStarter setReason(String str) {
        this.mRequest.reason = str;
        return this;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public ActivityStarter setCaller(IApplicationThread iApplicationThread) {
        this.mRequest.caller = iApplicationThread;
        return this;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public ActivityStarter setResolvedType(String str) {
        this.mRequest.resolvedType = str;
        return this;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public ActivityStarter setActivityInfo(ActivityInfo activityInfo) {
        this.mRequest.activityInfo = activityInfo;
        return this;
    }

    ActivityStarter setResolveInfo(ResolveInfo resolveInfo) {
        this.mRequest.resolveInfo = resolveInfo;
        return this;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public ActivityStarter setVoiceSession(IVoiceInteractionSession iVoiceInteractionSession) {
        this.mRequest.voiceSession = iVoiceInteractionSession;
        return this;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public ActivityStarter setVoiceInteractor(IVoiceInteractor iVoiceInteractor) {
        this.mRequest.voiceInteractor = iVoiceInteractor;
        return this;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public ActivityStarter setResultTo(IBinder iBinder) {
        this.mRequest.resultTo = iBinder;
        return this;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public ActivityStarter setResultWho(String str) {
        this.mRequest.resultWho = str;
        return this;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public ActivityStarter setRequestCode(int i) {
        this.mRequest.requestCode = i;
        return this;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public ActivityStarter setCallingPid(int i) {
        this.mRequest.callingPid = i;
        return this;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public ActivityStarter setCallingUid(int i) {
        this.mRequest.callingUid = i;
        return this;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public ActivityStarter setCallingPackage(String str) {
        this.mRequest.callingPackage = str;
        return this;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public ActivityStarter setCallingFeatureId(String str) {
        this.mRequest.callingFeatureId = str;
        return this;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public ActivityStarter setRealCallingPid(int i) {
        this.mRequest.realCallingPid = i;
        return this;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public ActivityStarter setRealCallingUid(int i) {
        this.mRequest.realCallingUid = i;
        return this;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public ActivityStarter setStartFlags(int i) {
        this.mRequest.startFlags = i;
        return this;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public ActivityStarter setActivityOptions(SafeActivityOptions safeActivityOptions) {
        this.mRequest.activityOptions = safeActivityOptions;
        return this;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public ActivityStarter setActivityOptions(Bundle bundle) {
        return setActivityOptions(SafeActivityOptions.fromBundle(bundle));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public ActivityStarter setIgnoreTargetSecurity(boolean z) {
        this.mRequest.ignoreTargetSecurity = z;
        return this;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public ActivityStarter setFilterCallingUid(int i) {
        this.mRequest.filterCallingUid = i;
        return this;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public ActivityStarter setComponentSpecified(boolean z) {
        this.mRequest.componentSpecified = z;
        return this;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public ActivityStarter setOutActivity(ActivityRecord[] activityRecordArr) {
        this.mRequest.outActivity = activityRecordArr;
        return this;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public ActivityStarter setInTask(Task task) {
        this.mRequest.inTask = task;
        return this;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public ActivityStarter setInTaskFragment(TaskFragment taskFragment) {
        this.mRequest.inTaskFragment = taskFragment;
        return this;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public ActivityStarter setWaitResult(WaitResult waitResult) {
        this.mRequest.waitResult = waitResult;
        return this;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public ActivityStarter setProfilerInfo(ProfilerInfo profilerInfo) {
        this.mRequest.profilerInfo = profilerInfo;
        return this;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public ActivityStarter setGlobalConfiguration(Configuration configuration) {
        this.mRequest.globalConfig = configuration;
        return this;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public ActivityStarter setUserId(int i) {
        this.mRequest.userId = i;
        return this;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public ActivityStarter setAllowPendingRemoteAnimationRegistryLookup(boolean z) {
        this.mRequest.allowPendingRemoteAnimationRegistryLookup = z;
        return this;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public ActivityStarter setOriginatingPendingIntent(PendingIntentRecord pendingIntentRecord) {
        this.mRequest.originatingPendingIntent = pendingIntentRecord;
        return this;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public ActivityStarter setBackgroundStartPrivileges(BackgroundStartPrivileges backgroundStartPrivileges) {
        this.mRequest.backgroundStartPrivileges = backgroundStartPrivileges;
        return this;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public ActivityStarter setErrorCallbackToken(IBinder iBinder) {
        this.mRequest.errorCallbackToken = iBinder;
        return this;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void dump(PrintWriter printWriter, String str) {
        printWriter.print(str);
        printWriter.print("mCurrentUser=");
        printWriter.println(this.mRootWindowContainer.mCurrentUser);
        printWriter.print(str);
        printWriter.print("mLastStartReason=");
        printWriter.println(this.mLastStartReason);
        printWriter.print(str);
        printWriter.print("mLastStartActivityTimeMs=");
        printWriter.println(DateFormat.getDateTimeInstance().format(new Date(this.mLastStartActivityTimeMs)));
        printWriter.print(str);
        printWriter.print("mLastStartActivityResult=");
        printWriter.println(this.mLastStartActivityResult);
        if (this.mLastStartActivityRecord != null) {
            printWriter.print(str);
            printWriter.println("mLastStartActivityRecord:");
            this.mLastStartActivityRecord.dump(printWriter, str + "  ", true);
        }
        if (this.mStartActivity != null) {
            printWriter.print(str);
            printWriter.println("mStartActivity:");
            this.mStartActivity.dump(printWriter, str + "  ", true);
        }
        if (this.mIntent != null) {
            printWriter.print(str);
            printWriter.print("mIntent=");
            printWriter.println(this.mIntent);
        }
        if (this.mOptions != null) {
            printWriter.print(str);
            printWriter.print("mOptions=");
            printWriter.println(this.mOptions);
        }
        printWriter.print(str);
        printWriter.print("mLaunchMode=");
        printWriter.print(ActivityInfo.launchModeToString(this.mLaunchMode));
        printWriter.print(str);
        printWriter.print("mLaunchFlags=0x");
        printWriter.print(Integer.toHexString(this.mLaunchFlags));
        printWriter.print(" mDoResume=");
        printWriter.print(this.mDoResume);
        printWriter.print(" mAddingToTask=");
        printWriter.print(this.mAddingToTask);
        printWriter.print(" mInTaskFragment=");
        printWriter.println(this.mInTaskFragment);
    }

    public IActivityStarterWrapper getWrapper() {
        return this.mASWrapper;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
    public class ActivityStarterWrapper implements IActivityStarterWrapper {
        private IActivityStarterExt mASExt;
        private IActivityStarterSocExt mActivityStarterSocExt;

        private ActivityStarterWrapper() {
            this.mASExt = (IActivityStarterExt) ExtLoader.type(IActivityStarterExt.class).base(ActivityStarter.this).create();
            this.mActivityStarterSocExt = (IActivityStarterSocExt) ExtLoader.type(IActivityStarterSocExt.class).base(ActivityStarter.this).create();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public IActivityStarterSocExt getSocExtImpl() {
            return this.mActivityStarterSocExt;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public IActivityStarterExt getExtImpl() {
            return this.mASExt;
        }

        @Override // com.android.server.wm.IActivityStarterWrapper
        public void setTargetRootTaskIfNeeded(ActivityRecord activityRecord) {
            ActivityStarter.this.setTargetRootTaskIfNeeded(activityRecord);
        }

        @Override // com.android.server.wm.IActivityStarterWrapper
        public void setSourceRecord(ActivityRecord activityRecord) {
            ActivityStarter.this.mSourceRecord = activityRecord;
        }

        @Override // com.android.server.wm.IActivityStarterWrapper
        public void setSourceRootTask(Task task) {
            ActivityStarter.this.mSourceRootTask = task;
        }

        @Override // com.android.server.wm.IActivityStarterWrapper
        public void setInTask(Task task) {
            ActivityStarter.this.mInTask = task;
        }

        @Override // com.android.server.wm.IActivityStarterWrapper
        public ActivityTaskManagerService getService() {
            return ActivityStarter.this.mService;
        }

        @Override // com.android.server.wm.IActivityStarterWrapper
        public ActivityOptions getOptions() {
            return ActivityStarter.this.mOptions;
        }

        @Override // com.android.server.wm.IActivityStarterWrapper
        public void setOptions(ActivityOptions activityOptions) {
            ActivityStarter.this.mOptions = activityOptions;
        }
    }
}
