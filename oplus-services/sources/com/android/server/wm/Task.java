package com.android.server.wm;

import android.app.ActivityManager;
import android.app.ActivityOptions;
import android.app.ActivityTaskManager;
import android.app.AppGlobals;
import android.app.IActivityController;
import android.app.PictureInPictureParams;
import android.app.TaskInfo;
import android.app.WindowConfiguration;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.res.Configuration;
import android.graphics.Matrix;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Binder;
import android.os.Debug;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.RemoteException;
import android.os.SystemClock;
import android.os.Trace;
import android.os.UserHandle;
import android.provider.Settings;
import android.service.voice.IVoiceInteractionSession;
import android.util.ArraySet;
import android.util.Slog;
import android.util.proto.ProtoOutputStream;
import android.view.DisplayInfo;
import android.view.InsetsSource;
import android.view.InsetsState;
import android.view.RemoteAnimationAdapter;
import android.view.SurfaceControl;
import android.view.WindowManager;
import android.window.ITaskOrganizer;
import android.window.PictureInPictureSurfaceTransaction;
import android.window.StartingWindowInfo;
import android.window.TaskFragmentParentInfo;
import android.window.TaskSnapshot;
import android.window.WindowContainerToken;
import com.android.internal.annotations.GuardedBy;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.app.IVoiceInteractor;
import com.android.internal.protolog.ProtoLogGroup;
import com.android.internal.protolog.ProtoLogImpl;
import com.android.internal.util.XmlUtils;
import com.android.internal.util.function.TriPredicate;
import com.android.internal.util.function.pooled.PooledLambda;
import com.android.internal.util.function.pooled.PooledPredicate;
import com.android.modules.utils.TypedXmlPullParser;
import com.android.modules.utils.TypedXmlSerializer;
import com.android.server.Watchdog;
import com.android.server.am.ActivityManagerService;
import com.android.server.am.AppTimeTracker;
import com.android.server.uri.NeededUriGrants;
import com.android.server.wm.ActivityRecord;
import com.android.server.wm.SurfaceAnimator;
import com.android.server.wm.TransitionController;
import java.io.FileDescriptor;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.function.BiPredicate;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import org.xmlpull.v1.XmlPullParserException;
import system.ext.loader.core.ExtLoader;
import vendor.oplus.hardware.charger.ChargerErrorCode;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
public class Task extends TaskFragment {
    private static final String ATTR_AFFINITY = "affinity";
    private static final String ATTR_ASKEDCOMPATMODE = "asked_compat_mode";
    private static final String ATTR_AUTOREMOVERECENTS = "auto_remove_recents";
    private static final String ATTR_CALLING_FEATURE_ID = "calling_feature_id";
    private static final String ATTR_CALLING_PACKAGE = "calling_package";
    private static final String ATTR_CALLING_UID = "calling_uid";
    private static final String ATTR_CONTAINER_TASK_ID = "containerTaskId";
    private static final String ATTR_EFFECTIVE_UID = "effective_uid";
    private static final String ATTR_EMBEDDED_CHILDREN = "embeddedChildren";
    private static final String ATTR_EMBEDDED_CONTAINER = "embedded_container";
    private static final String ATTR_IS_CONTAINER_TASK = "isContainerTask";
    private static final String ATTR_IS_SHOW_RECENT = "isShowRecent";
    private static final String ATTR_LASTDESCRIPTION = "last_description";
    private static final String ATTR_LASTTIMEMOVED = "last_time_moved";
    private static final String ATTR_LAST_SNAPSHOT_BUFFER_SIZE = "last_snapshot_buffer_size";
    private static final String ATTR_LAST_SNAPSHOT_CONTENT_INSETS = "last_snapshot_content_insets";
    private static final String ATTR_LAST_SNAPSHOT_TASK_SIZE = "last_snapshot_task_size";
    private static final String ATTR_MIN_HEIGHT = "min_height";
    private static final String ATTR_MIN_WIDTH = "min_width";
    private static final String ATTR_NEVERRELINQUISH = "never_relinquish_identity";
    private static final String ATTR_NEXT_AFFILIATION = "next_affiliation";
    private static final String ATTR_NON_FULLSCREEN_BOUNDS = "non_fullscreen_bounds";
    private static final String ATTR_OPLUS_FLAGS = "oplusFlags";
    private static final String ATTR_ORIGACTIVITY = "orig_activity";
    private static final String ATTR_PERSIST_TASK_VERSION = "persist_task_version";
    private static final String ATTR_PREV_AFFILIATION = "prev_affiliation";
    private static final String ATTR_REALACTIVITY = "real_activity";
    private static final String ATTR_REALACTIVITY_SUSPENDED = "real_activity_suspended";
    private static final String ATTR_RESIZE_MODE = "resize_mode";
    private static final String ATTR_ROOTHASRESET = "root_has_reset";
    private static final String ATTR_ROOT_AFFINITY = "root_affinity";
    private static final String ATTR_SUPPORTS_PICTURE_IN_PICTURE = "supports_picture_in_picture";
    private static final String ATTR_TASKID = "task_id";

    @Deprecated
    private static final String ATTR_TASKTYPE = "task_type";
    private static final String ATTR_TASK_AFFILIATION = "task_affiliation";
    private static final String ATTR_USERID = "user_id";
    private static final String ATTR_USER_SETUP_COMPLETE = "user_setup_complete";
    private static final String ATTR_WINDOW_LAYOUT_AFFINITY = "window_layout_affinity";
    private static final int DEFAULT_MIN_TASK_SIZE_DP = 220;
    static final int FLAG_FORCE_HIDDEN_FOR_PINNED_TASK = 1;
    static final int FLAG_FORCE_HIDDEN_FOR_TASK_ORG = 2;
    static final int LAYER_RANK_INVISIBLE = -1;
    static final int PERSIST_TASK_VERSION = 1;
    static final int REPARENT_KEEP_ROOT_TASK_AT_FRONT = 1;
    static final int REPARENT_LEAVE_ROOT_TASK_IN_PLACE = 2;
    static final int REPARENT_MOVE_ROOT_TASK_TO_FRONT = 0;
    private static final String TAG_ACTIVITY = "activity";
    private static final String TAG_AFFINITYINTENT = "affinity_intent";
    private static final String TAG_INTENT = "intent";
    private static final long TRANSLUCENT_CONVERSION_TIMEOUT = 2000;
    private static final int TRANSLUCENT_TIMEOUT_MSG = 101;
    private static Exception sTmpException;
    String affinity;
    Intent affinityIntent;
    boolean askedCompatMode;
    boolean autoRemoveRecents;
    int effectiveUid;
    boolean inRecents;
    Intent intent;
    boolean isAvailable;
    boolean isPersistable;
    long lastActiveTime;
    CharSequence lastDescription;
    int mAffiliatedTaskId;
    boolean mAlignActivityLocaleWithTask;
    private final AnimatingActivityRegistry mAnimatingActivityRegistry;
    String mCallingFeatureId;
    String mCallingPackage;
    int mCallingUid;
    private boolean mCanAffectSystemUiFlags;
    ActivityRecord mChildPipActivity;
    boolean mConfigWillChange;
    int mCurrentUser;
    private boolean mDeferTaskAppear;
    private boolean mDragResizing;
    private final FindRootHelper mFindRootHelper;
    private int mForceHiddenFlags;
    private boolean mForceShowForAllUsers;
    private boolean mForceTranslucent;
    private final Handler mHandler;
    private boolean mHasBeenVisible;
    boolean mInRemoveTask;
    boolean mInResumeTopActivity;
    boolean mIsEffectivelySystemApp;
    boolean mKillProcessesOnDestroyed;
    Rect mLastNonFullscreenBounds;
    SurfaceControl mLastRecentsAnimationOverlay;
    PictureInPictureSurfaceTransaction mLastRecentsAnimationTransaction;
    int mLastReportedRequestedOrientation;
    private int mLastRotationDisplayId;
    boolean mLastSurfaceShowing;
    final ActivityManager.RecentTaskInfo.PersistedTaskSnapshotData mLastTaskSnapshotData;
    long mLastTimeMoved;
    IBinder mLaunchCookie;
    int mLayerRank;
    int mLockTaskAuth;
    int mLockTaskUid;
    int mMultiWindowRestoreWindowingMode;
    private boolean mNeverRelinquishIdentity;
    Task mNextAffiliate;
    int mNextAffiliateTaskId;
    Task mPrevAffiliate;
    int mPrevAffiliateTaskId;
    int mPrevDisplayId;
    boolean mRemoveWithTaskOrganizer;
    private boolean mRemoving;
    boolean mReparentLeafTaskIfRelaunch;
    String mRequiredDisplayCategory;
    int mResizeMode;
    private boolean mReuseTask;
    private WindowProcessController mRootProcess;
    private int mRotation;
    StartingData mSharedStartingData;
    boolean mSupportsPictureInPicture;
    boolean mTaskAppearedSent;
    private ActivityManager.TaskDescription mTaskDescription;
    final int mTaskId;
    ITaskOrganizer mTaskOrganizer;
    private TaskWrapper mTaskWrapper;
    private Rect mTmpRect;
    private Rect mTmpRect2;
    ActivityRecord mTranslucentActivityWaiting;
    ArrayList<ActivityRecord> mUndrawnActivitiesBelowTopTranslucent;
    int mUserId;
    boolean mUserSetupComplete;
    String mWindowLayoutAffinity;
    int maxRecents;
    ComponentName origActivity;
    ComponentName realActivity;
    boolean realActivitySuspended;
    String rootAffinity;
    boolean rootWasReset;
    String stringName;
    IVoiceInteractor voiceInteractor;
    IVoiceInteractionSession voiceSession;
    private static final String TAG = "ActivityTaskManager";
    private static final String TAG_RECENTS = TAG + ActivityTaskManagerDebugConfig.POSTFIX_RECENTS;
    static final String TAG_TASKS = TAG + ActivityTaskManagerDebugConfig.POSTFIX_TASKS;
    static final String TAG_CLEANUP = TAG + ActivityTaskManagerDebugConfig.POSTFIX_CLEANUP;
    private static final String TAG_SWITCH = TAG + ActivityTaskManagerDebugConfig.POSTFIX_SWITCH;
    private static final String TAG_TRANSITION = TAG + ActivityTaskManagerDebugConfig.POSTFIX_TRANSITION;
    private static final String TAG_USER_LEAVING = TAG + ActivityTaskManagerDebugConfig.POSTFIX_USER_LEAVING;
    static final String TAG_VISIBILITY = TAG + ActivityTaskManagerDebugConfig.POSTFIX_VISIBILITY;
    private static final Rect sTmpBounds = new Rect();
    private static final ResetTargetTaskHelper sResetTargetTaskHelper = new ResetTargetTaskHelper();

    @Retention(RetentionPolicy.SOURCE)
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
    @interface ReparentMoveRootTaskMode {
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.android.server.wm.WindowContainer
    public Task asTask() {
        return this;
    }

    @Override // com.android.server.wm.TaskFragment, com.android.server.wm.WindowContainer
    long getProtoFieldId() {
        return 1146756268037L;
    }

    @Override // com.android.server.wm.WindowContainer
    boolean showSurfaceOnCreation() {
        return false;
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
    private class ActivityTaskHandler extends Handler {
        ActivityTaskHandler(Looper looper) {
            super(looper);
        }

        @Override // android.os.Handler
        public void handleMessage(Message message) {
            if (message.what != 101) {
                return;
            }
            WindowManagerGlobalLock windowManagerGlobalLock = Task.this.mAtmService.mGlobalLock;
            WindowManagerService.boostPriorityForLockedSection();
            synchronized (windowManagerGlobalLock) {
                try {
                    Task.this.notifyActivityDrawnLocked(null);
                } catch (Throwable th) {
                    WindowManagerService.resetPriorityAfterLockedSection();
                    throw th;
                }
            }
            WindowManagerService.resetPriorityAfterLockedSection();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
    public class FindRootHelper implements Predicate<ActivityRecord> {
        private boolean mIgnoreRelinquishIdentity;
        private ActivityRecord mRoot;
        private boolean mSetToBottomIfNone;

        private FindRootHelper() {
        }

        ActivityRecord findRoot(boolean z, boolean z2) {
            this.mIgnoreRelinquishIdentity = z;
            this.mSetToBottomIfNone = z2;
            Task.this.forAllActivities((Predicate<ActivityRecord>) this, false);
            ActivityRecord activityRecord = this.mRoot;
            this.mRoot = null;
            return activityRecord;
        }

        @Override // java.util.function.Predicate
        public boolean test(ActivityRecord activityRecord) {
            if (this.mRoot == null && this.mSetToBottomIfNone) {
                this.mRoot = activityRecord;
            }
            if (activityRecord.finishing) {
                return false;
            }
            ActivityRecord activityRecord2 = this.mRoot;
            if (activityRecord2 == null || activityRecord2.finishing) {
                this.mRoot = activityRecord;
            }
            ActivityRecord activityRecord3 = this.mRoot;
            int i = activityRecord3 == activityRecord ? Task.this.effectiveUid : activityRecord.info.applicationInfo.uid;
            if (this.mIgnoreRelinquishIdentity) {
                return true;
            }
            ActivityInfo activityInfo = activityRecord3.info;
            if ((activityInfo.flags & 4096) == 0) {
                return true;
            }
            ApplicationInfo applicationInfo = activityInfo.applicationInfo;
            if (applicationInfo.uid != 1000 && !applicationInfo.isSystemApp() && this.mRoot.info.applicationInfo.uid != i) {
                return true;
            }
            this.mRoot = activityRecord;
            return false;
        }
    }

    private Task(ActivityTaskManagerService activityTaskManagerService, int i, Intent intent, Intent intent2, String str, String str2, ComponentName componentName, ComponentName componentName2, boolean z, boolean z2, boolean z3, int i2, int i3, String str3, long j, boolean z4, ActivityManager.TaskDescription taskDescription, ActivityManager.RecentTaskInfo.PersistedTaskSnapshotData persistedTaskSnapshotData, int i4, int i5, int i6, int i7, String str4, String str5, int i8, boolean z5, boolean z6, boolean z7, int i9, int i10, ActivityInfo activityInfo, IVoiceInteractionSession iVoiceInteractionSession, IVoiceInteractor iVoiceInteractor, boolean z8, IBinder iBinder, boolean z9, boolean z10) {
        super(activityTaskManagerService, null, z8, false);
        this.mTranslucentActivityWaiting = null;
        this.mUndrawnActivitiesBelowTopTranslucent = new ArrayList<>();
        this.mInResumeTopActivity = false;
        this.mLockTaskAuth = 1;
        this.mLockTaskUid = -1;
        this.isPersistable = false;
        this.mNeverRelinquishIdentity = true;
        this.mReuseTask = false;
        this.mPrevAffiliateTaskId = -1;
        this.mNextAffiliateTaskId = -1;
        this.mLastNonFullscreenBounds = null;
        this.mLayerRank = -1;
        this.mPrevDisplayId = -1;
        this.mLastRotationDisplayId = -1;
        this.mMultiWindowRestoreWindowingMode = -1;
        this.mLastReportedRequestedOrientation = -1;
        this.mTmpRect = new Rect();
        this.mTmpRect2 = new Rect();
        this.mCanAffectSystemUiFlags = true;
        this.mForceHiddenFlags = 0;
        this.mForceTranslucent = false;
        this.mAnimatingActivityRegistry = new AnimatingActivityRegistry();
        this.mFindRootHelper = new FindRootHelper();
        this.mAlignActivityLocaleWithTask = false;
        this.mTaskWrapper = new TaskWrapper();
        this.mTaskId = i;
        this.mUserId = i2;
        this.mResizeMode = i8;
        this.mSupportsPictureInPicture = z5;
        this.mTaskDescription = taskDescription != null ? taskDescription : new ActivityManager.TaskDescription();
        this.mLastTaskSnapshotData = persistedTaskSnapshotData != null ? persistedTaskSnapshotData : new ActivityManager.RecentTaskInfo.PersistedTaskSnapshotData();
        setOrientation(-2);
        this.affinityIntent = intent2;
        this.affinity = str;
        this.rootAffinity = str2;
        this.voiceSession = iVoiceInteractionSession;
        this.voiceInteractor = iVoiceInteractor;
        this.realActivity = componentName;
        this.realActivitySuspended = z6;
        this.origActivity = componentName2;
        this.rootWasReset = z;
        this.isAvailable = true;
        this.autoRemoveRecents = z2;
        this.askedCompatMode = z3;
        this.mUserSetupComplete = z7;
        this.effectiveUid = i3;
        touchActiveTime();
        this.lastDescription = str3;
        this.mLastTimeMoved = j;
        this.mNeverRelinquishIdentity = z4;
        this.mAffiliatedTaskId = i4;
        this.mPrevAffiliateTaskId = i5;
        this.mNextAffiliateTaskId = i6;
        this.mCallingUid = i7;
        this.mCallingPackage = str4;
        this.mCallingFeatureId = str5;
        this.mResizeMode = i8;
        if (activityInfo != null) {
            setIntent(intent, activityInfo);
            setMinDimensions(activityInfo);
        } else {
            this.intent = intent;
            this.mMinWidth = i9;
            this.mMinHeight = i10;
        }
        this.mAtmService.getTaskChangeNotificationController().notifyTaskCreated(i, this.realActivity);
        this.mHandler = new ActivityTaskHandler(this.mTaskSupervisor.mLooper);
        this.mCurrentUser = this.mAtmService.mAmInternal.getCurrentUserId();
        this.mLaunchCookie = iBinder;
        this.mDeferTaskAppear = z9;
        this.mRemoveWithTaskOrganizer = z10;
        EventLogTags.writeWmTaskCreated(i);
        this.mTaskWrapper.getExtImpl().handleTaskCreated(this);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static Task fromWindowContainerToken(WindowContainerToken windowContainerToken) {
        WindowContainer fromBinder;
        if (windowContainerToken == null || (fromBinder = WindowContainer.fromBinder(windowContainerToken.asBinder())) == null) {
            return null;
        }
        return fromBinder.asTask();
    }

    Task reuseAsLeafTask(IVoiceInteractionSession iVoiceInteractionSession, IVoiceInteractor iVoiceInteractor, Intent intent, ActivityInfo activityInfo, ActivityRecord activityRecord) {
        this.voiceSession = iVoiceInteractionSession;
        this.voiceInteractor = iVoiceInteractor;
        setIntent(activityRecord, intent, activityInfo);
        setMinDimensions(activityInfo);
        this.mAtmService.getTaskChangeNotificationController().notifyTaskCreated(this.mTaskId, this.realActivity);
        return this;
    }

    private void cleanUpResourcesForDestroy(WindowContainer<?> windowContainer) {
        if (hasChild()) {
            return;
        }
        saveLaunchingStateIfNeeded(windowContainer.getDisplayContent());
        IVoiceInteractionSession iVoiceInteractionSession = this.voiceSession;
        boolean z = iVoiceInteractionSession != null;
        if (z) {
            try {
                iVoiceInteractionSession.taskFinished(this.intent, this.mTaskId);
            } catch (RemoteException unused) {
            }
        }
        if (autoRemoveFromRecents(windowContainer.asTaskFragment()) || z) {
            this.mTaskSupervisor.mRecentTasks.remove(this);
        }
        removeIfPossible("cleanUpResourcesForDestroy");
    }

    @Override // com.android.server.wm.WindowContainer
    @VisibleForTesting
    void removeIfPossible() {
        removeIfPossible("removeTaskIfPossible");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void removeIfPossible(String str) {
        if (!isRootTask() || !this.mAtmService.getLockTaskController().mLockTaskControllerExt.isLockDeviceMode()) {
            this.mAtmService.getLockTaskController().clearLockedTask(this);
        }
        if (shouldDeferRemoval()) {
            if (WindowManagerDebugConfig.DEBUG_ROOT_TASK) {
                Slog.i(TAG, "removeTask:" + str + " deferring removing taskId=" + this.mTaskId);
                return;
            }
            return;
        }
        boolean isLeafTask = isLeafTask();
        removeImmediately(str);
        if (isLeafTask) {
            this.mAtmService.getTaskChangeNotificationController().notifyTaskRemoved(this.mTaskId);
            TaskDisplayArea displayArea = getDisplayArea();
            if (displayArea != null) {
                displayArea.onLeafTaskRemoved(this.mTaskId);
            }
        }
        this.mTaskWrapper.getExtImpl().onTaskRemoved(this);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setResizeMode(int i) {
        if (this.mResizeMode == i) {
            return;
        }
        this.mResizeMode = i;
        this.mRootWindowContainer.ensureActivitiesVisible(null, 0, false);
        this.mRootWindowContainer.resumeFocusedTasksTopActivities();
        updateTaskDescription();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean resize(Rect rect, int i, boolean z) {
        ActivityRecord activityRecord;
        this.mAtmService.deferWindowLayout();
        boolean z2 = true;
        boolean z3 = (i & 2) != 0;
        try {
            if (getParent() == null) {
                setBounds(rect);
                if (!inFreeformWindowingMode()) {
                    this.mTaskSupervisor.restoreRecentTaskLocked(this, null, false);
                }
            } else {
                if (!canResizeToBounds(rect)) {
                    throw new IllegalArgumentException("resizeTask: Can not resize task=" + this + " to bounds=" + rect + " resizeMode=" + this.mResizeMode);
                }
                Trace.traceBegin(32L, "resizeTask_" + this.mTaskId);
                if (setBounds(rect, z3) != 0 && (activityRecord = topRunningActivityLocked()) != null) {
                    z2 = activityRecord.ensureActivityConfiguration(0, z);
                    this.mRootWindowContainer.ensureActivitiesVisible(activityRecord, 0, z);
                    if (!z2) {
                        this.mRootWindowContainer.resumeFocusedTasksTopActivities();
                    }
                }
                saveLaunchingStateIfNeeded();
                Trace.traceEnd(32L);
            }
            return z2;
        } finally {
            this.mAtmService.continueWindowLayout();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean reparent(Task task, boolean z, int i, boolean z2, boolean z3, String str) {
        return reparent(task, z ? Integer.MAX_VALUE : 0, i, z2, z3, true, str);
    }

    /* JADX WARN: Removed duplicated region for block: B:34:0x007a A[Catch: all -> 0x00c2, TryCatch #0 {all -> 0x00c2, blocks: (B:13:0x003a, B:15:0x0049, B:17:0x004f, B:21:0x005a, B:23:0x0060, B:32:0x0075, B:34:0x007a, B:37:0x0081, B:39:0x008c, B:41:0x0094, B:43:0x009b), top: B:12:0x003a }] */
    /* JADX WARN: Removed duplicated region for block: B:43:0x009b A[Catch: all -> 0x00c2, TRY_LEAVE, TryCatch #0 {all -> 0x00c2, blocks: (B:13:0x003a, B:15:0x0049, B:17:0x004f, B:21:0x005a, B:23:0x0060, B:32:0x0075, B:34:0x007a, B:37:0x0081, B:39:0x008c, B:41:0x0094, B:43:0x009b), top: B:12:0x003a }] */
    /* JADX WARN: Removed duplicated region for block: B:47:0x00a9  */
    /* JADX WARN: Removed duplicated region for block: B:50:0x00c0  */
    /* JADX WARN: Removed duplicated region for block: B:52:? A[RETURN, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    boolean reparent(Task task, int i, int i2, boolean z, boolean z2, boolean z3, String str) {
        boolean z4;
        ActivityTaskSupervisor activityTaskSupervisor = this.mTaskSupervisor;
        RootWindowContainer rootWindowContainer = this.mRootWindowContainer;
        WindowManagerService windowManagerService = this.mAtmService.mWindowManager;
        Task rootTask = getRootTask();
        Task reparentTargetRootTask = activityTaskSupervisor.getReparentTargetRootTask(this, task, i == Integer.MAX_VALUE);
        if (reparentTargetRootTask == rootTask || !canBeLaunchedOnDisplay(reparentTargetRootTask.getDisplayId())) {
            return false;
        }
        ActivityRecord topNonFinishingActivity = getTopNonFinishingActivity();
        this.mAtmService.deferWindowLayout();
        try {
            this.mTaskWrapper.getExtImpl().reparentTask(this, reparentTargetRootTask);
            ActivityRecord activityRecord = topRunningActivityLocked();
            boolean z5 = activityRecord != null && rootWindowContainer.isTopDisplayFocusedRootTask(rootTask) && topRunningActivityLocked() == activityRecord;
            boolean z6 = activityRecord != null && rootTask.isTopRootTaskInDisplayArea() && rootTask.topRunningActivity() == activityRecord;
            if (i2 != 0 && (i2 != 1 || (!z5 && !z6))) {
                z4 = false;
                reparent(reparentTargetRootTask, i, z4, str);
                if (z3) {
                    activityTaskSupervisor.scheduleUpdatePictureInPictureModeIfNeeded(this, rootTask);
                }
                if (activityRecord != null && z4) {
                    reparentTargetRootTask.moveToFront(str);
                    if (activityRecord.isState(ActivityRecord.State.RESUMED) && activityRecord == this.mRootWindowContainer.getTopResumedActivity()) {
                        this.mAtmService.setLastResumedActivityUncheckLocked(activityRecord, str);
                    }
                }
                if (!z) {
                    this.mTaskSupervisor.mNoAnimActivities.add(topNonFinishingActivity);
                }
                if (!z2) {
                    rootWindowContainer.ensureActivitiesVisible(null, 0, true);
                    rootWindowContainer.resumeFocusedTasksTopActivities();
                }
                activityTaskSupervisor.handleNonResizableTaskIfNeeded(this, task.getWindowingMode(), this.mRootWindowContainer.getDefaultTaskDisplayArea(), reparentTargetRootTask);
                return task != reparentTargetRootTask;
            }
            z4 = true;
            reparent(reparentTargetRootTask, i, z4, str);
            if (z3) {
            }
            if (activityRecord != null) {
                reparentTargetRootTask.moveToFront(str);
                if (activityRecord.isState(ActivityRecord.State.RESUMED)) {
                    this.mAtmService.setLastResumedActivityUncheckLocked(activityRecord, str);
                }
            }
            if (!z) {
            }
            if (!z2) {
            }
            activityTaskSupervisor.handleNonResizableTaskIfNeeded(this, task.getWindowingMode(), this.mRootWindowContainer.getDefaultTaskDisplayArea(), reparentTargetRootTask);
            if (task != reparentTargetRootTask) {
            }
        } finally {
            this.mAtmService.continueWindowLayout();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void touchActiveTime() {
        this.lastActiveTime = SystemClock.elapsedRealtime();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public long getInactiveDuration() {
        return SystemClock.elapsedRealtime() - this.lastActiveTime;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setIntent(ActivityRecord activityRecord) {
        setIntent(activityRecord, null, null);
    }

    /* JADX WARN: Code restructure failed: missing block: B:15:0x0022, code lost:
    
        if (r3 != r0.applicationInfo.uid) goto L19;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    void setIntent(ActivityRecord activityRecord, Intent intent, ActivityInfo activityInfo) {
        if (activityRecord == null) {
            return;
        }
        boolean z = true;
        if (this.intent != null) {
            if (!this.mNeverRelinquishIdentity) {
                ActivityInfo activityInfo2 = activityInfo != null ? activityInfo : activityRecord.info;
                int i = this.effectiveUid;
                if (i != 1000) {
                    if (!this.mIsEffectivelySystemApp) {
                    }
                }
            }
            z = false;
        }
        if (z) {
            this.mCallingUid = activityRecord.launchedFromUid;
            this.mCallingPackage = activityRecord.launchedFromPackage;
            this.mCallingFeatureId = activityRecord.launchedFromFeatureId;
            if (intent == null) {
                intent = activityRecord.intent;
            }
            setIntent(intent, activityInfo != null ? activityInfo : activityRecord.info);
        }
        ITaskExt extImpl = this.mTaskWrapper.getExtImpl();
        if (activityInfo == null) {
            activityInfo = activityRecord.info;
        }
        extImpl.onSetTaskIntent(this, activityInfo, z);
        setLockTaskAuth(activityRecord);
    }

    private void setIntent(Intent intent, ActivityInfo activityInfo) {
        if (isLeafTask()) {
            this.mNeverRelinquishIdentity = (activityInfo.flags & 4096) == 0;
            String str = activityInfo.taskAffinity;
            this.affinity = str;
            if (this.intent == null) {
                this.rootAffinity = str;
                this.mRequiredDisplayCategory = activityInfo.requiredDisplayCategory;
            }
            ApplicationInfo applicationInfo = activityInfo.applicationInfo;
            this.effectiveUid = applicationInfo.uid;
            this.mIsEffectivelySystemApp = applicationInfo.isSystemApp();
            this.stringName = null;
            if (activityInfo.targetActivity == null) {
                if (intent != null && (intent.getSelector() != null || intent.getSourceBounds() != null)) {
                    Intent intent2 = new Intent(intent);
                    intent2.setSelector(null);
                    intent2.setSourceBounds(null);
                    intent = intent2;
                }
                if (ProtoLogCache.WM_DEBUG_TASKS_enabled) {
                    ProtoLogImpl.v(ProtoLogGroup.WM_DEBUG_TASKS, -2054442123, 0, (String) null, new Object[]{String.valueOf(this), String.valueOf(intent)});
                }
                this.intent = intent;
                this.realActivity = intent != null ? intent.getComponent() : null;
                this.origActivity = null;
            } else {
                ComponentName componentName = new ComponentName(activityInfo.packageName, activityInfo.targetActivity);
                if (intent != null) {
                    Intent intent3 = new Intent(intent);
                    intent3.setSelector(null);
                    intent3.setSourceBounds(null);
                    if (ProtoLogCache.WM_DEBUG_TASKS_enabled) {
                        ProtoLogImpl.v(ProtoLogGroup.WM_DEBUG_TASKS, 674932310, 0, (String) null, new Object[]{String.valueOf(this), String.valueOf(intent3)});
                    }
                    this.intent = intent3;
                    this.realActivity = componentName;
                    this.origActivity = intent.getComponent();
                } else {
                    this.intent = null;
                    this.realActivity = componentName;
                    this.origActivity = new ComponentName(activityInfo.packageName, activityInfo.name);
                }
            }
            this.mTaskFragmentExt.addTask(getParent(), this, intent, activityInfo);
            ActivityInfo.WindowLayout windowLayout = activityInfo.windowLayout;
            this.mWindowLayoutAffinity = windowLayout != null ? windowLayout.windowLayoutAffinity : null;
            Intent intent4 = this.intent;
            int flags = intent4 == null ? 0 : intent4.getFlags();
            if ((2097152 & flags) != 0) {
                this.rootWasReset = true;
            }
            this.mUserId = UserHandle.getUserId(activityInfo.applicationInfo.uid);
            this.mUserSetupComplete = Settings.Secure.getIntForUser(this.mAtmService.mContext.getContentResolver(), ATTR_USER_SETUP_COMPLETE, 0, this.mUserId) != 0;
            if ((activityInfo.flags & 8192) != 0) {
                this.autoRemoveRecents = true;
            } else if ((flags & 532480) == 524288) {
                if (activityInfo.documentLaunchMode != 0) {
                    this.autoRemoveRecents = false;
                } else {
                    this.autoRemoveRecents = true;
                }
            } else {
                this.autoRemoveRecents = false;
            }
            int i = this.mResizeMode;
            int i2 = activityInfo.resizeMode;
            if (i != i2) {
                this.mResizeMode = i2;
                updateTaskDescription();
            }
            this.mSupportsPictureInPicture = activityInfo.supportsPictureInPicture();
            if (this.inRecents) {
                this.mTaskSupervisor.mRecentTasks.remove(this);
                this.mTaskSupervisor.mRecentTasks.add(this);
            }
        }
    }

    void setMinDimensions(ActivityInfo activityInfo) {
        ActivityInfo.WindowLayout windowLayout;
        if (activityInfo != null && (windowLayout = activityInfo.windowLayout) != null) {
            this.mMinWidth = windowLayout.minWidth;
            this.mMinHeight = windowLayout.minHeight;
        } else {
            this.mMinWidth = -1;
            this.mMinHeight = -1;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean isSameIntentFilter(ActivityRecord activityRecord) {
        Intent intent;
        Intent intent2 = new Intent(activityRecord.intent);
        if (Objects.equals(this.realActivity, activityRecord.mActivityComponent) && (intent = this.intent) != null) {
            intent2.setComponent(intent.getComponent());
            intent2.setPackage(this.intent.getPackage());
        }
        return intent2.filterEquals(this.intent);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean returnsToHomeRootTask() {
        if (inMultiWindowMode() || !hasChild() || getWrapper().getExtImpl().isFlexibleWindowScenario(new int[0])) {
            return false;
        }
        if (this.intent != null && !WindowConfiguration.sExtImpl.isWindowingZoomMode(getWindowingMode())) {
            if ((this.intent.getFlags() & 268451840) != 268451840) {
                return false;
            }
            Task rootHomeTask = getDisplayArea() != null ? getDisplayArea().getRootHomeTask() : null;
            return rootHomeTask == null || !this.mAtmService.getLockTaskController().isLockTaskModeViolation(rootHomeTask);
        }
        Task bottomMostTask = getBottomMostTask();
        return bottomMostTask != this && bottomMostTask.returnsToHomeRootTask();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setPrevAffiliate(Task task) {
        this.mPrevAffiliate = task;
        this.mPrevAffiliateTaskId = task == null ? -1 : task.mTaskId;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setNextAffiliate(Task task) {
        this.mNextAffiliate = task;
        this.mNextAffiliateTaskId = task == null ? -1 : task.mTaskId;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.android.server.wm.WindowContainer, com.android.server.wm.ConfigurationContainer
    public void onParentChanged(ConfigurationContainer configurationContainer, ConfigurationContainer configurationContainer2) {
        WindowContainer windowContainer = (WindowContainer) configurationContainer;
        WindowContainer<?> windowContainer2 = (WindowContainer) configurationContainer2;
        DisplayContent displayContent = windowContainer != null ? windowContainer.getDisplayContent() : null;
        DisplayContent displayContent2 = windowContainer2 != null ? windowContainer2.getDisplayContent() : null;
        this.mPrevDisplayId = displayContent2 != null ? displayContent2.mDisplayId : -1;
        if (windowContainer2 != null && windowContainer == null) {
            cleanUpResourcesForDestroy(windowContainer2);
        }
        if (displayContent != null) {
            getConfiguration().windowConfiguration.setRotation(displayContent.getWindowConfiguration().getRotation());
        }
        super.onParentChanged(windowContainer, windowContainer2);
        this.mTaskWrapper.getExtImpl().onTaskParentChanged(displayContent2, displayContent, windowContainer2, windowContainer, this);
        this.mTaskWrapper.getExtImpl().onTaskParentChanged(windowContainer2, windowContainer, this);
        updateTaskOrganizerState();
        if (getParent() == null && this.mDisplayContent != null) {
            this.mDisplayContent = null;
            this.mWmService.mWindowPlacerLocked.requestTraversal();
        }
        if (windowContainer2 != null) {
            final Task asTask = windowContainer2.asTask();
            if (asTask != null) {
                forAllActivities(new Consumer() { // from class: com.android.server.wm.Task$$ExternalSyntheticLambda40
                    @Override // java.util.function.Consumer
                    public final void accept(Object obj) {
                        Task.this.cleanUpActivityReferences((ActivityRecord) obj);
                    }
                });
            }
            if (windowContainer2.inPinnedWindowingMode() && (windowContainer == null || !windowContainer.inPinnedWindowingMode())) {
                this.mRootWindowContainer.notifyActivityPipModeChanged(this, null);
            }
        }
        if (windowContainer != null) {
            if (!this.mCreatedByOrganizer && !canBeOrganized()) {
                getSyncTransaction().show(this.mSurfaceControl);
            }
            IVoiceInteractionSession iVoiceInteractionSession = this.voiceSession;
            if (iVoiceInteractionSession != null) {
                try {
                    iVoiceInteractionSession.taskStarted(this.intent, this.mTaskId);
                } catch (RemoteException unused) {
                }
            }
        }
        if (windowContainer2 == null && windowContainer != null) {
            updateOverrideConfigurationFromLaunchBounds();
        }
        adjustBoundsForDisplayChangeIfNeeded(getDisplayContent());
        this.mRootWindowContainer.updateUIDsPresentOnDisplay();
        forAllActivities(new Consumer() { // from class: com.android.server.wm.Task$$ExternalSyntheticLambda41
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                ((ActivityRecord) obj).updateAnimatingActivityRegistry();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.android.server.wm.TaskFragment
    public ActivityRecord getTopResumedActivity() {
        ActivityRecord topResumedActivity;
        if (!isLeafTask()) {
            for (int size = this.mChildren.size() - 1; size >= 0; size--) {
                Task asTask = ((WindowContainer) this.mChildren.get(size)).asTask();
                if (asTask != null && (topResumedActivity = asTask.getTopResumedActivity()) != null) {
                    return topResumedActivity;
                }
            }
        }
        ActivityRecord resumedActivity = getResumedActivity();
        ActivityRecord activityRecord = null;
        for (int size2 = this.mChildren.size() - 1; size2 >= 0; size2--) {
            WindowContainer windowContainer = (WindowContainer) this.mChildren.get(size2);
            if (windowContainer.asTaskFragment() != null) {
                activityRecord = windowContainer.asTaskFragment().getTopResumedActivity();
            } else if (resumedActivity != null && windowContainer.asActivityRecord() == resumedActivity) {
                activityRecord = resumedActivity;
            }
            if (activityRecord != null) {
                return activityRecord;
            }
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.android.server.wm.TaskFragment
    public ActivityRecord getTopPausingActivity() {
        if (!isLeafTask()) {
            for (int size = this.mChildren.size() - 1; size >= 0; size--) {
                ActivityRecord topPausingActivity = ((WindowContainer) this.mChildren.get(size)).asTask().getTopPausingActivity();
                if (topPausingActivity != null) {
                    return topPausingActivity;
                }
            }
        }
        ActivityRecord pausingActivity = getPausingActivity();
        ActivityRecord activityRecord = null;
        for (int size2 = this.mChildren.size() - 1; size2 >= 0; size2--) {
            WindowContainer windowContainer = (WindowContainer) this.mChildren.get(size2);
            if (windowContainer.asTaskFragment() != null) {
                activityRecord = windowContainer.asTaskFragment().getTopPausingActivity();
            } else if (pausingActivity != null && windowContainer.asActivityRecord() == pausingActivity) {
                activityRecord = pausingActivity;
            }
            if (activityRecord != null) {
                return activityRecord;
            }
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void updateTaskMovement(boolean z, boolean z2, int i) {
        EventLogTags.writeWmTaskMoved(this.mTaskId, getRootTaskId(), getDisplayId(), z ? 1 : 0, i);
        TaskDisplayArea displayArea = getDisplayArea();
        if (displayArea != null && isLeafTask()) {
            displayArea.onLeafTaskMoved(this, z, z2);
        }
        if (this.isPersistable) {
            this.mLastTimeMoved = System.currentTimeMillis();
        }
    }

    private void closeRecentsChain() {
        Task task = this.mPrevAffiliate;
        if (task != null) {
            task.setNextAffiliate(this.mNextAffiliate);
        }
        Task task2 = this.mNextAffiliate;
        if (task2 != null) {
            task2.setPrevAffiliate(this.mPrevAffiliate);
        }
        setPrevAffiliate(null);
        setNextAffiliate(null);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void removedFromRecents() {
        closeRecentsChain();
        if (this.inRecents) {
            this.inRecents = false;
            this.mAtmService.notifyTaskPersisterLocked(this, false);
        }
        clearRootProcess();
        this.mAtmService.mWindowManager.mTaskSnapshotController.notifyTaskRemovedFromRecents(this.mTaskId, this.mUserId);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setTaskToAffiliateWith(Task task) {
        closeRecentsChain();
        this.mAffiliatedTaskId = task.mAffiliatedTaskId;
        while (true) {
            Task task2 = task.mNextAffiliate;
            if (task2 == null) {
                break;
            }
            if (task2.mAffiliatedTaskId != this.mAffiliatedTaskId) {
                Slog.e(TAG, "setTaskToAffiliateWith: nextRecents=" + task2 + " affilTaskId=" + task2.mAffiliatedTaskId + " should be " + this.mAffiliatedTaskId);
                if (task2.mPrevAffiliate == task) {
                    task2.setPrevAffiliate(null);
                }
                task.setNextAffiliate(null);
            } else {
                task = task2;
            }
        }
        task.setNextAffiliate(this);
        setPrevAffiliate(task);
        setNextAffiliate(null);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Intent getBaseIntent() {
        Intent intent = this.intent;
        if (intent != null) {
            return intent;
        }
        Intent intent2 = this.affinityIntent;
        if (intent2 != null) {
            return intent2;
        }
        Task topMostTask = getTopMostTask();
        if (topMostTask == this || topMostTask == null) {
            return null;
        }
        return topMostTask.getBaseIntent();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public String getBasePackageName() {
        ComponentName component;
        Intent baseIntent = getBaseIntent();
        return (baseIntent == null || (component = baseIntent.getComponent()) == null) ? "" : component.getPackageName();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public ActivityRecord getRootActivity() {
        return getRootActivity(true, false);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public ActivityRecord getRootActivity(boolean z) {
        return getRootActivity(false, z);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public ActivityRecord getRootActivity(boolean z, boolean z2) {
        return this.mFindRootHelper.findRoot(z, z2);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public ActivityRecord topRunningActivityLocked() {
        if (getParent() == null) {
            return null;
        }
        return getActivity(new Task$$ExternalSyntheticLambda28());
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean isUidPresent(int i) {
        PooledPredicate obtainPredicate = PooledLambda.obtainPredicate(new DisplayContent$$ExternalSyntheticLambda24(), PooledLambda.__(ActivityRecord.class), Integer.valueOf(i));
        boolean z = getActivity(obtainPredicate) != null;
        obtainPredicate.recycle();
        return z;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public ActivityRecord topActivityContainsStartingWindow() {
        if (getParent() == null) {
            return null;
        }
        return getActivity(new Predicate() { // from class: com.android.server.wm.Task$$ExternalSyntheticLambda4
            @Override // java.util.function.Predicate
            public final boolean test(Object obj) {
                boolean lambda$topActivityContainsStartingWindow$1;
                lambda$topActivityContainsStartingWindow$1 = Task.lambda$topActivityContainsStartingWindow$1((ActivityRecord) obj);
                return lambda$topActivityContainsStartingWindow$1;
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ boolean lambda$topActivityContainsStartingWindow$1(ActivityRecord activityRecord) {
        return activityRecord.getWindow(new Predicate() { // from class: com.android.server.wm.Task$$ExternalSyntheticLambda33
            @Override // java.util.function.Predicate
            public final boolean test(Object obj) {
                boolean lambda$topActivityContainsStartingWindow$0;
                lambda$topActivityContainsStartingWindow$0 = Task.lambda$topActivityContainsStartingWindow$0((WindowState) obj);
                return lambda$topActivityContainsStartingWindow$0;
            }
        }) != null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ boolean lambda$topActivityContainsStartingWindow$0(WindowState windowState) {
        return windowState.getBaseType() == 3;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final boolean moveActivityToFront(ActivityRecord activityRecord) {
        boolean moveChildToFront;
        if (ProtoLogCache.WM_DEBUG_ADD_REMOVE_enabled) {
            ProtoLogImpl.i(ProtoLogGroup.WM_DEBUG_ADD_REMOVE, -463348344, 0, (String) null, new Object[]{String.valueOf(activityRecord), String.valueOf(Debug.getCallers(4))});
        }
        TaskFragment taskFragment = activityRecord.getTaskFragment();
        if (taskFragment != this) {
            moveChildToFront = true;
            if (taskFragment.isEmbedded() && taskFragment.getNonFinishingActivityCount() == 1) {
                taskFragment.mClearedForReorderActivityToFront = true;
            }
            if (taskFragment.mTaskFragmentExt.isCreateForMagicWindow()) {
                taskFragment.positionChildAt(Integer.MAX_VALUE, activityRecord, false);
            } else {
                activityRecord.reparent(this, Integer.MAX_VALUE);
            }
            if (taskFragment.isEmbedded()) {
                this.mAtmService.mWindowOrganizerController.mTaskFragmentOrganizerController.onActivityReparentedToTask(activityRecord);
            }
        } else {
            moveChildToFront = moveChildToFront(activityRecord);
        }
        updateEffectiveIntent();
        return moveChildToFront;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.android.server.wm.TaskFragment, com.android.server.wm.WindowContainer
    public void addChild(WindowContainer windowContainer, int i) {
        super.addChild(windowContainer, getAdjustedChildPosition(windowContainer, i));
        if (ProtoLogCache.WM_DEBUG_ADD_REMOVE_enabled) {
            ProtoLogImpl.v(ProtoLogGroup.WM_DEBUG_ADD_REMOVE, 1330804250, 0, (String) null, new Object[]{String.valueOf(this)});
        }
        if (this.mTaskOrganizer != null && this.mCreatedByOrganizer && windowContainer.asTask() != null) {
            getDisplayArea().addRootTaskReferenceIfNeeded((Task) windowContainer);
        }
        this.mTaskWrapper.getExtImpl().addChild(windowContainer);
        this.mRootWindowContainer.updateUIDsPresentOnDisplay();
        TaskFragment asTaskFragment = windowContainer.asTaskFragment();
        if (asTaskFragment == null || asTaskFragment.asTask() != null) {
            return;
        }
        asTaskFragment.setMinDimensions(this.mMinWidth, this.mMinHeight);
        ActivityRecord topMostActivity = getTopMostActivity();
        if (topMostActivity != null) {
            topMostActivity.associateStartingWindowWithTaskIfNeeded();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void onDescendantActivityAdded(boolean z, int i, ActivityRecord activityRecord) {
        warnForNonLeafTask("onDescendantActivityAdded");
        if (!z) {
            int activityType = activityRecord.getRequestedOverrideConfiguration().windowConfiguration.getActivityType();
            if (activityType == 0) {
                if (i == 0) {
                    i = 1;
                }
                activityRecord.getRequestedOverrideConfiguration().windowConfiguration.setActivityType(i);
                activityType = i;
            }
            setActivityType(activityType);
            this.isPersistable = activityRecord.isPersistable();
            this.mCallingUid = activityRecord.launchedFromUid;
            this.mCallingPackage = activityRecord.launchedFromPackage;
            this.mCallingFeatureId = activityRecord.launchedFromFeatureId;
            this.maxRecents = Math.min(Math.max(activityRecord.info.maxRecents, 1), ActivityTaskManager.getMaxAppRecentsLimitStatic());
        } else {
            activityRecord.setActivityType(i);
        }
        updateEffectiveIntent();
    }

    @Override // com.android.server.wm.TaskFragment, com.android.server.wm.WindowContainer
    void removeChild(WindowContainer windowContainer) {
        removeChild(windowContainer, "removeChild");
        this.mTaskFragmentExt.removeChild(windowContainer);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void removeChild(WindowContainer windowContainer, String str) {
        if (this.mCreatedByOrganizer && windowContainer.asTask() != null) {
            getDisplayArea().removeRootTaskReferenceIfNeeded((Task) windowContainer);
        }
        if (!this.mChildren.contains(windowContainer)) {
            Slog.e(TAG, "removeChild: r=" + windowContainer + " not found in t=" + this);
            return;
        }
        this.mTaskWrapper.getExtImpl().removeChild(windowContainer);
        if (WindowManagerDebugConfig.DEBUG_TASK_MOVEMENT) {
            Slog.d("WindowManager", "removeChild: child=" + windowContainer + " reason=" + str);
        }
        super.removeChild(windowContainer, false);
        if (inPinnedWindowingMode()) {
            this.mAtmService.getTaskChangeNotificationController().notifyTaskStackChanged();
        }
        if (hasChild()) {
            updateEffectiveIntent();
            if (onlyHasTaskOverlayActivities(true)) {
                this.mTaskSupervisor.removeTask(this, false, false, str);
                return;
            }
            return;
        }
        if (this.mReuseTask || !shouldRemoveSelfOnLastChildRemoval() || this.mTaskWrapper.mTaskExt.isTaskInreParent()) {
            return;
        }
        removeIfPossible(str + ", last child = " + windowContainer + " in " + this);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean onlyHasTaskOverlayActivities(boolean z) {
        int i = 0;
        for (int childCount = getChildCount() - 1; childCount >= 0; childCount--) {
            ActivityRecord asActivityRecord = getChildAt(childCount).asActivityRecord();
            if (asActivityRecord == null) {
                return false;
            }
            if (z || !asActivityRecord.finishing) {
                if (!asActivityRecord.isTaskOverlay()) {
                    return false;
                }
                i++;
            }
        }
        return i > 0;
    }

    private boolean autoRemoveFromRecents(TaskFragment taskFragment) {
        return this.autoRemoveRecents || !(hasChild() || getHasBeenVisible()) || (taskFragment != null && taskFragment.isEmbedded());
    }

    private void clearPinnedTaskIfNeed() {
        ActivityRecord activityRecord = this.mChildPipActivity;
        if (activityRecord == null || activityRecord.getTask() == null) {
            return;
        }
        this.mTaskSupervisor.removeRootTask(this.mChildPipActivity.getTask());
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void removeActivities(final String str, final boolean z) {
        DisplayContent displayContent;
        clearPinnedTaskIfNeed();
        if (getRootTask() == null) {
            forAllActivities(new Consumer() { // from class: com.android.server.wm.Task$$ExternalSyntheticLambda18
                @Override // java.util.function.Consumer
                public final void accept(Object obj) {
                    Task.this.lambda$removeActivities$2(z, str, (ActivityRecord) obj);
                }
            });
            return;
        }
        final ArrayList arrayList = new ArrayList();
        forAllActivities(new Consumer() { // from class: com.android.server.wm.Task$$ExternalSyntheticLambda19
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                Task.lambda$removeActivities$3(z, arrayList, (ActivityRecord) obj);
            }
        });
        for (int size = arrayList.size() - 1; size >= 0; size--) {
            ActivityRecord activityRecord = (ActivityRecord) arrayList.get(size);
            if (activityRecord.isState(ActivityRecord.State.RESUMED) || (activityRecord.isVisible() && (displayContent = this.mDisplayContent) != null && !displayContent.mAppTransition.containsTransitRequest(2))) {
                activityRecord.finishIfPossible(str, false);
            } else {
                activityRecord.destroyIfPossible(str);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$removeActivities$2(boolean z, String str, ActivityRecord activityRecord) {
        if (activityRecord.finishing) {
            return;
        }
        if (z && activityRecord.isTaskOverlay()) {
            return;
        }
        activityRecord.takeFromHistory();
        removeChild(activityRecord, str);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$removeActivities$3(boolean z, ArrayList arrayList, ActivityRecord activityRecord) {
        if (activityRecord.finishing) {
            return;
        }
        if (z && activityRecord.isTaskOverlay()) {
            return;
        }
        arrayList.add(activityRecord);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void performClearTaskForReuse(boolean z) {
        this.mReuseTask = true;
        this.mTaskSupervisor.beginDeferResume();
        try {
            removeActivities("clear-task-all", z);
        } finally {
            this.mTaskSupervisor.endDeferResume();
            this.mReuseTask = false;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public ActivityRecord performClearTop(ActivityRecord activityRecord, int i, int[] iArr) {
        this.mReuseTask = true;
        this.mTaskSupervisor.beginDeferResume();
        try {
            return clearTopActivities(activityRecord, i, iArr);
        } finally {
            this.mTaskSupervisor.endDeferResume();
            this.mReuseTask = false;
        }
    }

    private ActivityRecord clearTopActivities(ActivityRecord activityRecord, int i, final int[] iArr) {
        ActivityRecord findActivityInHistory = findActivityInHistory(activityRecord.mActivityComponent, activityRecord.mUserId);
        if (findActivityInHistory == null) {
            return null;
        }
        if (this.mTaskWrapper.getExtImpl().canClearActivityRecord(findActivityInHistory)) {
            if (findActivityInHistory.isVisible()) {
                return null;
            }
            return findActivityInHistory;
        }
        PooledPredicate obtainPredicate = PooledLambda.obtainPredicate(new BiPredicate() { // from class: com.android.server.wm.Task$$ExternalSyntheticLambda10
            @Override // java.util.function.BiPredicate
            public final boolean test(Object obj, Object obj2) {
                boolean lambda$clearTopActivities$4;
                lambda$clearTopActivities$4 = Task.lambda$clearTopActivities$4(iArr, (ActivityRecord) obj, (ActivityRecord) obj2);
                return lambda$clearTopActivities$4;
            }
        }, PooledLambda.__(ActivityRecord.class), findActivityInHistory);
        forAllActivities((Predicate<ActivityRecord>) obtainPredicate);
        obtainPredicate.recycle();
        if (findActivityInHistory.launchMode == 0 && (536870912 & i) == 0 && !ActivityStarter.isDocumentLaunchesIntoExisting(i) && !findActivityInHistory.finishing) {
            findActivityInHistory.finishIfPossible("clear-task-top", false);
        }
        this.mTaskWrapper.getExtImpl().applyNewOrientationWhenReuseIfNeed(findActivityInHistory, activityRecord);
        return findActivityInHistory;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ boolean lambda$clearTopActivities$4(int[] iArr, ActivityRecord activityRecord, ActivityRecord activityRecord2) {
        return finishActivityAbove(activityRecord, activityRecord2, iArr);
    }

    private static boolean finishActivityAbove(ActivityRecord activityRecord, ActivityRecord activityRecord2, int[] iArr) {
        if (activityRecord == activityRecord2) {
            return true;
        }
        if (!activityRecord.finishing && !activityRecord.isTaskOverlay()) {
            ActivityOptions options = activityRecord.getOptions();
            if (options != null) {
                activityRecord.clearOptionsAnimation();
                activityRecord2.updateOptionsLocked(options);
            }
            if (activityRecord.getWrapper().getExtImpl().performClearTaskLocked(activityRecord, activityRecord2)) {
                iArr[0] = iArr[0] + 1;
                activityRecord.finishIfPossible("clear-task-stack", false);
            }
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public String lockTaskAuthToString() {
        int i = this.mLockTaskAuth;
        if (i == 0) {
            return "LOCK_TASK_AUTH_DONT_LOCK";
        }
        if (i == 1) {
            return "LOCK_TASK_AUTH_PINNABLE";
        }
        if (i == 2) {
            return "LOCK_TASK_AUTH_LAUNCHABLE";
        }
        if (i == 3) {
            return "LOCK_TASK_AUTH_ALLOWLISTED";
        }
        if (i == 4) {
            return "LOCK_TASK_AUTH_LAUNCHABLE_PRIV";
        }
        return "unknown=" + this.mLockTaskAuth;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setLockTaskAuth() {
        setLockTaskAuth(getRootActivity());
    }

    private void setLockTaskAuth(ActivityRecord activityRecord) {
        this.mLockTaskAuth = this.mAtmService.getLockTaskController().getLockTaskAuth(activityRecord, this);
        if (ProtoLogCache.WM_DEBUG_LOCKTASK_enabled) {
            ProtoLogImpl.d(ProtoLogGroup.WM_DEBUG_LOCKTASK, 1824105730, 0, (String) null, new Object[]{String.valueOf(this), String.valueOf(lockTaskAuthToString())});
        }
    }

    @Override // com.android.server.wm.ConfigurationContainer
    public boolean supportsSplitScreenWindowingMode() {
        return supportsSplitScreenWindowingModeInDisplayArea(getDisplayArea());
    }

    boolean supportsSplitScreenWindowingModeInDisplayArea(TaskDisplayArea taskDisplayArea) {
        Task topMostTask = getTopMostTask();
        return super.supportsSplitScreenWindowingMode() && (topMostTask == null || topMostTask.supportsSplitScreenWindowingModeInner(taskDisplayArea));
    }

    private boolean supportsSplitScreenWindowingModeInner(TaskDisplayArea taskDisplayArea) {
        return super.supportsSplitScreenWindowingMode() && this.mAtmService.mSupportsSplitScreenMultiWindow && this.mTaskWrapper.getExtImpl().supportsSplitScreenByVendorPolicy(this, supportsMultiWindowInDisplayArea(taskDisplayArea));
    }

    boolean supportsFreeform() {
        return supportsFreeformInDisplayArea(getDisplayArea());
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean supportsFreeformInDisplayArea(TaskDisplayArea taskDisplayArea) {
        return this.mAtmService.mSupportsFreeformWindowManagement && supportsMultiWindowInDisplayArea(taskDisplayArea);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean canBeLaunchedOnDisplay(int i) {
        return this.mTaskSupervisor.canPlaceEntityOnDisplay(i, -1, -1, this);
    }

    private boolean canResizeToBounds(Rect rect) {
        if (rect == null || !inFreeformWindowingMode()) {
            return true;
        }
        boolean z = rect.width() > rect.height();
        Rect requestedOverrideBounds = getRequestedOverrideBounds();
        int i = this.mResizeMode;
        if (i != 7) {
            return !(i == 6 && z) && (i != 5 || z);
        }
        if (requestedOverrideBounds.isEmpty()) {
            return true;
        }
        return z == (requestedOverrideBounds.width() > requestedOverrideBounds.height());
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean isClearingToReuseTask() {
        return this.mReuseTask;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public ActivityRecord findActivityInHistory(ComponentName componentName, int i) {
        PooledPredicate obtainPredicate = PooledLambda.obtainPredicate(new TriPredicate() { // from class: com.android.server.wm.Task$$ExternalSyntheticLambda1
            public final boolean test(Object obj, Object obj2, Object obj3) {
                boolean matchesActivityInHistory;
                matchesActivityInHistory = Task.matchesActivityInHistory((ActivityRecord) obj, (ComponentName) obj2, ((Integer) obj3).intValue());
                return matchesActivityInHistory;
            }
        }, PooledLambda.__(ActivityRecord.class), componentName, Integer.valueOf(i));
        ActivityRecord activity = getActivity(obtainPredicate);
        obtainPredicate.recycle();
        return activity;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static boolean matchesActivityInHistory(ActivityRecord activityRecord, ComponentName componentName, int i) {
        return !activityRecord.finishing && activityRecord.mActivityComponent.equals(componentName) && activityRecord.mUserId == i;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void updateTaskDescription() {
        Task asTask;
        ActivityRecord rootActivity = getRootActivity(true);
        if (rootActivity == null) {
            return;
        }
        ActivityManager.TaskDescription taskDescription = new ActivityManager.TaskDescription();
        PooledPredicate obtainPredicate = PooledLambda.obtainPredicate(new TriPredicate() { // from class: com.android.server.wm.Task$$ExternalSyntheticLambda35
            public final boolean test(Object obj, Object obj2, Object obj3) {
                boolean taskDescriptionFromActivityAboveRoot;
                taskDescriptionFromActivityAboveRoot = Task.setTaskDescriptionFromActivityAboveRoot((ActivityRecord) obj, (ActivityRecord) obj2, (ActivityManager.TaskDescription) obj3);
                return taskDescriptionFromActivityAboveRoot;
            }
        }, PooledLambda.__(ActivityRecord.class), rootActivity, taskDescription);
        forAllActivities((Predicate<ActivityRecord>) obtainPredicate);
        obtainPredicate.recycle();
        taskDescription.setResizeMode(this.mResizeMode);
        taskDescription.setMinWidth(this.mMinWidth);
        taskDescription.setMinHeight(this.mMinHeight);
        setTaskDescription(taskDescription);
        this.mAtmService.getTaskChangeNotificationController().notifyTaskDescriptionChanged(getTaskInfo());
        WindowContainer parent = getParent();
        if (parent != null && (asTask = parent.asTask()) != null) {
            asTask.updateTaskDescription();
        }
        dispatchTaskInfoChangedIfNeeded(false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static boolean setTaskDescriptionFromActivityAboveRoot(ActivityRecord activityRecord, ActivityRecord activityRecord2, ActivityManager.TaskDescription taskDescription) {
        ActivityManager.TaskDescription taskDescription2;
        if (!activityRecord.isTaskOverlay() && (taskDescription2 = activityRecord.taskDescription) != null) {
            if (taskDescription.getLabel() == null) {
                taskDescription.setLabel(taskDescription2.getLabel());
            }
            if (taskDescription.getRawIcon() == null) {
                taskDescription.setIcon(taskDescription2.getRawIcon());
            }
            if (taskDescription.getIconFilename() == null) {
                taskDescription.setIconFilename(taskDescription2.getIconFilename());
            }
            if (taskDescription.getPrimaryColor() == 0) {
                taskDescription.setPrimaryColor(taskDescription2.getPrimaryColor());
            }
            if (taskDescription.getBackgroundColor() == 0) {
                taskDescription.setBackgroundColor(taskDescription2.getBackgroundColor());
            }
            if (taskDescription.getStatusBarColor() == 0) {
                taskDescription.setStatusBarColor(taskDescription2.getStatusBarColor());
                taskDescription.setEnsureStatusBarContrastWhenTransparent(taskDescription2.getEnsureStatusBarContrastWhenTransparent());
            }
            if (taskDescription.getNavigationBarColor() == 0) {
                taskDescription.setNavigationBarColor(taskDescription2.getNavigationBarColor());
                taskDescription.setEnsureNavigationBarContrastWhenTransparent(taskDescription2.getEnsureNavigationBarContrastWhenTransparent());
            }
            if (taskDescription.getBackgroundColorFloating() == 0) {
                taskDescription.setBackgroundColorFloating(taskDescription2.getBackgroundColorFloating());
            }
        }
        return activityRecord == activityRecord2;
    }

    @VisibleForTesting
    void updateEffectiveIntent() {
        ActivityRecord rootActivity = getRootActivity(true);
        if (rootActivity != null) {
            setIntent(rootActivity);
            updateTaskDescription();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setLastNonFullscreenBounds(Rect rect) {
        Rect rect2 = this.mLastNonFullscreenBounds;
        if (rect2 == null) {
            this.mLastNonFullscreenBounds = new Rect(rect);
        } else {
            rect2.set(rect);
        }
    }

    private void onConfigurationChangedInner(Configuration configuration) {
        ActivityRecord activityRecord;
        Rect rect;
        boolean persistTaskBounds = getWindowConfiguration().persistTaskBounds();
        boolean persistTaskBounds2 = getRequestedOverrideConfiguration().windowConfiguration.persistTaskBounds();
        if (getRequestedOverrideWindowingMode() == 0) {
            persistTaskBounds2 = configuration.windowConfiguration.persistTaskBounds();
        }
        boolean z = persistTaskBounds2 & (getRequestedOverrideConfiguration().windowConfiguration.getBounds() == null || getRequestedOverrideConfiguration().windowConfiguration.getBounds().isEmpty());
        if (!persistTaskBounds && z && (rect = this.mLastNonFullscreenBounds) != null && !rect.isEmpty()) {
            getRequestedOverrideConfiguration().windowConfiguration.setBounds(this.mLastNonFullscreenBounds);
        }
        int windowingMode = getWindowingMode();
        this.mTmpPrevBounds.set(getBounds());
        boolean inMultiWindowMode = inMultiWindowMode();
        boolean inPinnedWindowingMode = inPinnedWindowingMode();
        super.onConfigurationChanged(configuration);
        updateSurfaceSize(getSyncTransaction());
        this.mTaskWrapper.getExtImpl().onConfigurationChangedOfTask(configuration, this.mTmpPrevBounds, this);
        boolean z2 = inPinnedWindowingMode != inPinnedWindowingMode();
        if (z2) {
            this.mTaskSupervisor.scheduleUpdatePictureInPictureModeIfNeeded(this, getRootTask());
        } else if (inMultiWindowMode != inMultiWindowMode()) {
            this.mTaskSupervisor.scheduleUpdateMultiWindowMode(this);
        }
        int windowingMode2 = getWindowingMode();
        if (windowingMode2 != windowingMode) {
            EventLogTags.writeWmTaskWindowingModeChanged(this.mTaskId, getRootTaskId(), windowingMode2, windowingMode);
            if (WindowManagerDebugConfig.DEBUG_TASK_MOVEMENT) {
                Slog.d(TAG, String.format("newWinMode:%s prevWinMode:%s %s", Integer.valueOf(windowingMode2), Integer.valueOf(windowingMode), this));
            }
        }
        if (shouldStartChangeTransition(windowingMode, this.mTmpPrevBounds)) {
            initializeChangeTransition(this.mTmpPrevBounds);
        }
        if (windowingMode != windowingMode2) {
            this.mTaskWrapper.getExtImpl().onTaskWindowingModeChanged(this, windowingMode, windowingMode2);
        }
        if (getWindowConfiguration().persistTaskBounds()) {
            Rect requestedOverrideBounds = getRequestedOverrideBounds();
            if (!requestedOverrideBounds.isEmpty()) {
                setLastNonFullscreenBounds(requestedOverrideBounds);
            }
        }
        if (z2 && inPinnedWindowingMode && !this.mTransitionController.isShellTransitionsEnabled() && (activityRecord = topRunningActivity()) != null && this.mDisplayContent.isFixedRotationLaunchingApp(activityRecord)) {
            resetSurfaceControlTransforms();
        }
        saveLaunchingStateIfNeeded();
        boolean updateTaskOrganizerState = updateTaskOrganizerState();
        if (updateTaskOrganizerState) {
            updateSurfacePosition(getSyncTransaction());
            if (!isOrganized()) {
                updateSurfaceSize(getSyncTransaction());
            }
        }
        if (!updateTaskOrganizerState) {
            dispatchTaskInfoChangedIfNeeded(false);
        }
        this.mTaskWrapper.mTaskExt.handleConfigChanged(configuration, this.mTmpRect, this);
    }

    @Override // com.android.server.wm.TaskFragment, com.android.server.wm.WindowContainer, com.android.server.wm.ConfigurationContainer
    public void onConfigurationChanged(Configuration configuration) {
        DisplayContent displayContent = this.mDisplayContent;
        if (displayContent == null || !displayContent.mPinnedTaskController.isFreezingTaskConfig(this)) {
            if (!isRootTask()) {
                onConfigurationChangedInner(configuration);
                return;
            }
            int windowingMode = getWindowingMode();
            boolean isAlwaysOnTop = isAlwaysOnTop();
            int rotation = getWindowConfiguration().getRotation();
            Rect rect = this.mTmpRect;
            this.mTaskWrapper.getExtImpl().adjustTaskConfiguration(this, configuration);
            getBounds(rect);
            onConfigurationChangedInner(configuration);
            TaskDisplayArea displayArea = getDisplayArea();
            if (displayArea == null) {
                return;
            }
            if (windowingMode != getWindowingMode()) {
                displayArea.onRootTaskWindowingModeChanged(this);
            }
            if (!isOrganized() && !getRequestedOverrideBounds().isEmpty() && this.mDisplayContent != null) {
                int rotation2 = getWindowConfiguration().getRotation();
                if (rotation != rotation2) {
                    this.mDisplayContent.rotateBounds(rotation, rotation2, rect);
                    setBounds(rect);
                }
            }
            if (isAlwaysOnTop == isAlwaysOnTop() || this.mTaskWrapper.getExtImpl().isPendingToBottomTask(this.mTaskId) || getWrapper().getExtImpl().isFlexibleWindowScenario(new int[0])) {
                return;
            }
            displayArea.positionChildAt(Integer.MAX_VALUE, this, false);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void resolveLeafTaskOnlyOverrideConfigs(Configuration configuration, Rect rect) {
        if (isLeafTask()) {
            int windowingMode = getResolvedOverrideConfiguration().windowConfiguration.getWindowingMode();
            if (windowingMode == 0) {
                windowingMode = configuration.windowConfiguration.getWindowingMode();
            }
            getConfiguration().windowConfiguration.setWindowingMode(windowingMode);
            Rect bounds = getResolvedOverrideConfiguration().windowConfiguration.getBounds();
            if (windowingMode == 1) {
                if ((!isOrganized() || (this.mTaskWrapper.getExtImpl().isZoomMode(this.mTaskFragmentExt.getPrevWinMode()) && !this.mCreatedByOrganizer)) && !getWrapper().getExtImpl().isFlexibleWindowScenario(new int[0])) {
                    bounds.setEmpty();
                    return;
                }
                return;
            }
            adjustForMinimalTaskDimensions(bounds, rect, configuration);
            if (windowingMode == 5) {
                computeFreeformBounds(bounds, configuration);
            }
        }
    }

    void adjustForMinimalTaskDimensions(Rect rect, Rect rect2, Configuration configuration) {
        int i;
        int i2;
        int i3 = this.mMinWidth;
        int i4 = this.mMinHeight;
        if (!inPinnedWindowingMode()) {
            int i5 = (int) ((this.mDisplayContent == null ? 220 : r4.mMinSizeOfResizeableTaskDp) * (configuration.densityDpi / 160.0f));
            if (i3 == -1) {
                i3 = i5;
            }
            if (i4 == -1) {
                i4 = i5;
            }
        }
        if (rect.isEmpty()) {
            Rect bounds = configuration.windowConfiguration.getBounds();
            if (bounds.width() >= i3 && bounds.height() >= i4) {
                return;
            } else {
                rect.set(bounds);
            }
        }
        boolean z = i3 > rect.width();
        boolean z2 = i4 > rect.height();
        if (z || z2) {
            if (z) {
                if (!rect2.isEmpty() && (i2 = rect.right) == rect2.right) {
                    rect.left = i2 - i3;
                } else {
                    rect.right = rect.left + i3;
                }
            }
            if (z2) {
                if (!rect2.isEmpty() && (i = rect.bottom) == rect2.bottom) {
                    rect.top = i - i4;
                } else {
                    rect.bottom = rect.top + i4;
                }
            }
        }
    }

    private void computeFreeformBounds(Rect rect, Configuration configuration) {
        float f = configuration.densityDpi / 160.0f;
        Rect rect2 = new Rect(configuration.windowConfiguration.getBounds());
        DisplayContent displayContent = getDisplayContent();
        if (displayContent != null) {
            Rect rect3 = new Rect();
            displayContent.getStableRect(rect3);
            rect2.intersect(rect3);
        }
        fitWithinBounds(rect, rect2, (int) (48.0f * f), (int) (f * 32.0f));
        int i = rect2.top - rect.top;
        if (i > 0) {
            rect.offset(0, i);
        }
    }

    private static void fitWithinBounds(Rect rect, Rect rect2, int i, int i2) {
        int i3;
        if (rect2 == null || rect2.isEmpty() || rect2.contains(rect)) {
            return;
        }
        int min = Math.min(i, rect.width());
        int i4 = rect.right;
        int i5 = rect2.left;
        int i6 = 0;
        if (i4 < i5 + min) {
            i3 = min - (i4 - i5);
        } else {
            int i7 = rect.left;
            int i8 = rect2.right;
            i3 = i7 > i8 - min ? -(min - (i8 - i7)) : 0;
        }
        int min2 = Math.min(i2, rect.width());
        int i9 = rect.bottom;
        int i10 = rect2.top;
        if (i9 < i10 + min2) {
            i6 = min2 - (i9 - i10);
        } else {
            int i11 = rect.top;
            int i12 = rect2.bottom;
            if (i11 > i12 - min2) {
                i6 = -(min2 - (i12 - i11));
            }
        }
        rect.offset(i3, i6);
    }

    private boolean shouldStartChangeTransition(int i, Rect rect) {
        if ((!isLeafTask() && !this.mCreatedByOrganizer) || !canStartChangeTransition()) {
            return false;
        }
        int windowingMode = getWindowingMode();
        if (!this.mTransitionController.inTransition(this)) {
            return (i == 5) != (windowingMode == 5);
        }
        Rect bounds = getConfiguration().windowConfiguration.getBounds();
        return (i == windowingMode && rect.width() == bounds.width() && rect.height() == bounds.height()) ? false : true;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.android.server.wm.WindowContainer
    public void migrateToNewSurfaceControl(SurfaceControl.Transaction transaction) {
        super.migrateToNewSurfaceControl(transaction);
        Point point = this.mLastSurfaceSize;
        point.x = 0;
        point.y = 0;
        updateSurfaceSize(transaction);
    }

    void updateSurfaceSize(SurfaceControl.Transaction transaction) {
        int i;
        int i2;
        SurfaceControl surfaceControl = this.mSurfaceControl;
        if (surfaceControl == null || !surfaceControl.isValid() || isOrganized() || this.mTaskWrapper.getExtImpl().isCompactWindowingMode(getWindowingMode()) || this.mTaskWrapper.getExtImpl().isPuttTask()) {
            return;
        }
        if (isRootTask()) {
            Rect bounds = getBounds();
            i = bounds.width();
            i2 = bounds.height();
        } else {
            i = 0;
            i2 = 0;
        }
        Point point = this.mLastSurfaceSize;
        if (i == point.x && i2 == point.y) {
            return;
        }
        transaction.setWindowCrop(this.mSurfaceControl, i, i2);
        this.mLastSurfaceSize.set(i, i2);
    }

    @VisibleForTesting
    Point getLastSurfaceSize() {
        return this.mLastSurfaceSize;
    }

    @VisibleForTesting
    boolean isInChangeTransition() {
        return this.mSurfaceFreezer.hasLeash() || AppTransition.isChangeTransitOld(this.mTransit);
    }

    @Override // com.android.server.wm.WindowContainer, com.android.server.wm.SurfaceFreezer.Freezable
    public SurfaceControl getFreezeSnapshotTarget() {
        if (!this.mDisplayContent.mAppTransition.containsTransitRequest(6)) {
            return null;
        }
        ArraySet<Integer> arraySet = new ArraySet<>();
        arraySet.add(Integer.valueOf(getActivityType()));
        RemoteAnimationAdapter remoteAnimationOverride = this.mDisplayContent.mAppTransitionController.getRemoteAnimationOverride(this, 27, arraySet);
        if (remoteAnimationOverride == null || remoteAnimationOverride.getChangeNeedsSnapshot()) {
            return getSurfaceControl();
        }
        return null;
    }

    @Override // com.android.server.wm.TaskFragment, com.android.server.wm.WindowContainer
    void writeIdentifierToProto(ProtoOutputStream protoOutputStream, long j) {
        long start = protoOutputStream.start(j);
        protoOutputStream.write(1120986464257L, System.identityHashCode(this));
        protoOutputStream.write(1120986464258L, this.mUserId);
        Intent intent = this.intent;
        protoOutputStream.write(1138166333443L, (intent == null || intent.getComponent() == null) ? "Task" : this.intent.getComponent().flattenToShortString());
        protoOutputStream.end(start);
    }

    private void saveLaunchingStateIfNeeded() {
        saveLaunchingStateIfNeeded(getDisplayContent());
    }

    private void saveLaunchingStateIfNeeded(DisplayContent displayContent) {
        if (isLeafTask() && getHasBeenVisible()) {
            int windowingMode = getWindowingMode();
            if ((windowingMode == 1 || windowingMode == 5) && getTaskDisplayArea() != null && getTaskDisplayArea().getWindowingMode() == 5) {
                this.mTaskSupervisor.mLaunchParamsPersister.saveTask(this, displayContent);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Rect updateOverrideConfigurationFromLaunchBounds() {
        Task rootTask = getRootTask();
        Rect launchBounds = (rootTask == this || !rootTask.isOrganized()) ? getLaunchBounds() : null;
        setBounds(launchBounds);
        if (launchBounds != null && !launchBounds.isEmpty()) {
            launchBounds.set(getRequestedOverrideBounds());
        }
        return launchBounds;
    }

    Rect getLaunchBounds() {
        Task rootTask = getRootTask();
        if (rootTask == null) {
            return null;
        }
        int windowingMode = getWindowingMode();
        if (!isActivityTypeStandardOrUndefined() || windowingMode == 1) {
            if (isResizeable() || getWrapper().getExtImpl().isFlexibleWindowScenario(new int[0])) {
                return rootTask.getRequestedOverrideBounds();
            }
            return null;
        }
        if (!getWindowConfiguration().persistTaskBounds()) {
            return rootTask.getRequestedOverrideBounds();
        }
        return this.mLastNonFullscreenBounds;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setRootProcess(WindowProcessController windowProcessController) {
        clearRootProcess();
        Intent intent = this.intent;
        if (intent == null || (intent.getFlags() & 8388608) != 0) {
            return;
        }
        this.mRootProcess = windowProcessController;
        windowProcessController.addRecentTask(this);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void clearRootProcess() {
        WindowProcessController windowProcessController = this.mRootProcess;
        if (windowProcessController != null) {
            windowProcessController.removeRecentTask(this);
            this.mRootProcess = null;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int getRootTaskId() {
        return getRootTask().mTaskId;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Task getOrganizedTask() {
        Task asTask;
        if (isOrganized()) {
            return this;
        }
        WindowContainer parent = getParent();
        if (parent == null || (asTask = parent.asTask()) == null) {
            return null;
        }
        return asTask.getOrganizedTask();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Task getCreatedByOrganizerTask() {
        Task asTask;
        if (this.mCreatedByOrganizer) {
            return this;
        }
        WindowContainer parent = getParent();
        if (parent == null || (asTask = parent.asTask()) == null) {
            return null;
        }
        return asTask.getCreatedByOrganizerTask();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Task getAdjacentTask() {
        TaskFragment adjacentTaskFragment = getAdjacentTaskFragment();
        if (adjacentTaskFragment != null && adjacentTaskFragment.asTask() != null) {
            return adjacentTaskFragment.asTask();
        }
        WindowContainer parent = getParent();
        if (parent == null || parent.asTask() == null) {
            return null;
        }
        return parent.asTask().getAdjacentTask();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean isRootTask() {
        return getRootTask() == this;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean isLeafTask() {
        for (int size = this.mChildren.size() - 1; size >= 0; size--) {
            if (((WindowContainer) this.mChildren.get(size)).asTask() != null) {
                return false;
            }
        }
        return true;
    }

    public Task getTopLeafTask() {
        for (int size = this.mChildren.size() - 1; size >= 0; size--) {
            Task asTask = ((WindowContainer) this.mChildren.get(size)).asTask();
            if (asTask != null) {
                return asTask.getTopLeafTask();
            }
        }
        return this;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int getDescendantTaskCount() {
        final int[] iArr = {0};
        forAllLeafTasks(new Consumer() { // from class: com.android.server.wm.Task$$ExternalSyntheticLambda0
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                Task.lambda$getDescendantTaskCount$5(iArr, (Task) obj);
            }
        }, false);
        return iArr[0];
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$getDescendantTaskCount$5(int[] iArr, Task task) {
        iArr[0] = iArr[0] + 1;
    }

    Task adjustFocusToNextFocusableTask(String str) {
        return adjustFocusToNextFocusableTask(str, false, true);
    }

    private Task getNextFocusableTask(final boolean z) {
        WindowContainer parent = getParent();
        if (parent == null) {
            return null;
        }
        Task task = parent.getTask(new Predicate() { // from class: com.android.server.wm.Task$$ExternalSyntheticLambda50
            @Override // java.util.function.Predicate
            public final boolean test(Object obj) {
                boolean lambda$getNextFocusableTask$6;
                lambda$getNextFocusableTask$6 = Task.this.lambda$getNextFocusableTask$6(z, obj);
                return lambda$getNextFocusableTask$6;
            }
        });
        return (task != null || parent.asTask() == null) ? task : parent.asTask().getNextFocusableTask(z);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ boolean lambda$getNextFocusableTask$6(boolean z, Object obj) {
        if (z || obj != this) {
            Task task = (Task) obj;
            if (task.isFocusableAndVisible() && this.mTaskWrapper.getExtImpl().isMiniRootTask(task)) {
                return true;
            }
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Task adjustFocusToNextFocusableTask(String str, boolean z, boolean z2) {
        Task nextFocusableTask = getNextFocusableTask(z);
        if (nextFocusableTask == null) {
            nextFocusableTask = this.mRootWindowContainer.getNextFocusableRootTask(this, !z);
        }
        if (nextFocusableTask == null) {
            TaskDisplayArea displayArea = getDisplayArea();
            if (displayArea == null) {
                return null;
            }
            displayArea.clearPreferredTopFocusableRootTask();
            return null;
        }
        Task rootTask = nextFocusableTask.getRootTask();
        if (!this.mTaskWrapper.getExtImpl().adjustMoveDisplayToTopForMirage(rootTask.getDisplayId(), z2)) {
            WindowContainer parent = nextFocusableTask.getParent();
            do {
                Task task = nextFocusableTask;
                nextFocusableTask = parent;
                nextFocusableTask.positionChildAt(Integer.MAX_VALUE, task, false);
                parent = nextFocusableTask.getParent();
                if (nextFocusableTask.asTask() == null) {
                    break;
                }
            } while (parent != null);
            return rootTask;
        }
        String str2 = str + " adjustFocusToNextFocusableTask";
        ActivityRecord activityRecord = nextFocusableTask.topRunningActivity();
        if (nextFocusableTask.isActivityTypeHome() && (activityRecord == null || !activityRecord.isVisibleRequested())) {
            nextFocusableTask.getDisplayArea().moveHomeActivityToTop(str2);
            return rootTask;
        }
        nextFocusableTask.moveToFront(str2);
        if (rootTask.getTopResumedActivity() != null) {
            this.mTaskSupervisor.updateTopResumedActivityIfNeeded(str);
        }
        return rootTask;
    }

    private int computeMinUserPosition(int i, int i2) {
        while (i < i2 && !((WindowContainer) this.mChildren.get(i)).showToCurrentUser()) {
            i++;
        }
        return i;
    }

    private int computeMaxUserPosition(int i) {
        while (i > 0 && ((WindowContainer) this.mChildren.get(i)).showToCurrentUser()) {
            i--;
        }
        return i;
    }

    private int getAdjustedChildPosition(WindowContainer windowContainer, int i) {
        int i2;
        boolean showToCurrentUser = windowContainer.showToCurrentUser();
        int size = this.mChildren.size();
        int computeMinUserPosition = showToCurrentUser ? computeMinUserPosition(0, size) : 0;
        if (size > 0) {
            i2 = showToCurrentUser ? size - 1 : computeMaxUserPosition(size - 1);
        } else {
            i2 = computeMinUserPosition;
        }
        if (!windowContainer.isAlwaysOnTop()) {
            while (i2 > computeMinUserPosition && ((WindowContainer) this.mChildren.get(i2)).isAlwaysOnTop() && (!isAlwaysOnTop() || !this.mTaskWrapper.getExtImpl().isZoomMode(getWindowingMode()))) {
                i2--;
            }
        }
        if (i == Integer.MIN_VALUE && computeMinUserPosition == 0) {
            return ChargerErrorCode.ERR_FILE_FAILURE_ACCESS;
        }
        if (i == Integer.MAX_VALUE && i2 >= size - 1) {
            return Integer.MAX_VALUE;
        }
        if (!hasChild(windowContainer)) {
            i2++;
        }
        return Math.min(Math.max(i, computeMinUserPosition), i2);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.android.server.wm.WindowContainer
    public void positionChildAt(int i, WindowContainer windowContainer, boolean z) {
        boolean z2 = i >= this.mChildren.size() - 1;
        if (this.mTaskWrapper.mTaskExt.handleActivityReorder(this, windowContainer, i, z2)) {
            return;
        }
        int adjustedChildPosition = getAdjustedChildPosition(windowContainer, i);
        super.positionChildAt(adjustedChildPosition, windowContainer, z);
        if (WindowManagerDebugConfig.DEBUG_TASK_MOVEMENT) {
            Slog.d("WindowManager", "positionChildAt: child=" + windowContainer + " position=" + adjustedChildPosition + " parent=" + this);
        }
        Task asTask = windowContainer.asTask();
        if (asTask != null) {
            asTask.updateTaskMovement(z2, adjustedChildPosition == Integer.MIN_VALUE, adjustedChildPosition);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.android.server.wm.TaskFragment, com.android.server.wm.WindowContainer
    public void removeImmediately() {
        removeImmediately("removeTask");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.android.server.wm.TaskFragment
    public void removeImmediately(String str) {
        if (WindowManagerDebugConfig.DEBUG_ROOT_TASK) {
            Slog.i(TAG, "removeTask:" + str + " removing taskId=" + this.mTaskId);
        }
        if (this.mRemoving) {
            return;
        }
        this.mRemoving = true;
        EventLogTags.writeWmTaskRemoved(this.mTaskId, getRootTaskId(), getDisplayId(), str);
        clearPinnedTaskIfNeed();
        ActivityRecord activityRecord = this.mChildPipActivity;
        if (activityRecord != null) {
            activityRecord.clearLastParentBeforePip();
        }
        setTaskOrganizer(null);
        super.removeImmediately();
        this.mRemoving = false;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void reparent(Task task, int i, boolean z, String str) {
        if (WindowManagerDebugConfig.DEBUG_ROOT_TASK) {
            Slog.i(TAG, "reParentTask: removing taskId=" + this.mTaskId + " from rootTask=" + getRootTask());
        }
        EventLogTags.writeWmTaskRemoved(this.mTaskId, getRootTaskId(), getDisplayId(), "reParentTask:" + str);
        reparent(task, i);
        task.positionChildAt(i, this, z);
    }

    public int setBounds(Rect rect, boolean z) {
        int bounds = setBounds(rect);
        if (!z || (bounds & 2) == 2) {
            return bounds;
        }
        onResize();
        return bounds | 2;
    }

    @Override // com.android.server.wm.ConfigurationContainer
    public int setBounds(Rect rect) {
        if ((!this.mTaskWrapper.getExtImpl().isPuttTask() || !isRootTask()) && isRootTask()) {
            return setBounds(getRequestedOverrideBounds(), rect);
        }
        DisplayContent displayContent = getRootTask() != null ? getRootTask().getDisplayContent() : null;
        int i = displayContent != null ? displayContent.getDisplayInfo().rotation : 0;
        int bounds = super.setBounds(rect);
        this.mRotation = i;
        updateSurfacePositionNonOrganized();
        return bounds;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int setBoundsUnchecked(Rect rect) {
        int bounds = super.setBounds(rect);
        updateSurfaceBounds();
        return bounds;
    }

    @Override // com.android.server.wm.ConfigurationContainer
    public boolean isCompatible(int i, int i2) {
        if (i2 == 0) {
            i2 = 1;
        }
        return super.isCompatible(i, i2);
    }

    @Override // com.android.server.wm.WindowContainer
    public boolean onDescendantOrientationChanged(WindowContainer windowContainer) {
        getWrapper().getExtImpl().onDescendantOrientationChanged(windowContainer);
        if (super.onDescendantOrientationChanged(windowContainer)) {
            return true;
        }
        if (getParent() == null) {
            return false;
        }
        onConfigurationChanged(getParent().getConfiguration());
        return true;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.android.server.wm.WindowContainer
    public boolean handlesOrientationChangeFromDescendant(int i) {
        if (!super.handlesOrientationChangeFromDescendant(i)) {
            return false;
        }
        if (isLeafTask()) {
            return canSpecifyOrientation() && getDisplayArea().canSpecifyOrientation(i);
        }
        return true;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.android.server.wm.WindowContainer
    public void reparent(WindowContainer windowContainer, int i) {
        if (ActivityTaskManagerService.LTW_DISABLE || this.mTaskWrapper.getExtImpl().getAllowReparent()) {
            super.reparent(windowContainer, i);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.android.server.wm.WindowContainer
    public void onDisplayChanged(DisplayContent displayContent) {
        if (!isRootTask() && !this.mCreatedByOrganizer) {
            adjustBoundsForDisplayChangeIfNeeded(displayContent);
        }
        super.onDisplayChanged(displayContent);
        if (isLeafTask()) {
            this.mWmService.mAtmService.getTaskChangeNotificationController().notifyTaskDisplayChanged(this.mTaskId, displayContent != null ? displayContent.getDisplayId() : -1);
        }
        if (isRootTask()) {
            updateSurfaceBounds();
        }
        sendTaskFragmentParentInfoChangedIfNeeded();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean isResizeable() {
        return isResizeable(true);
    }

    boolean isResizeable(boolean z) {
        return (this.mAtmService.mForceResizableActivities && getActivityType() == 1) || ActivityInfo.isResizeableMode(this.mResizeMode) || this.mTaskWrapper.getExtImpl().getLaunchedFromMultiSearch() || (this.mSupportsPictureInPicture && z);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean preserveOrientationOnResize() {
        int i = this.mResizeMode;
        return i == 6 || i == 5 || i == 7;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean cropWindowsToRootTaskBounds() {
        if (isActivityTypeHomeOrRecents()) {
            Task rootTask = getRootTask();
            if (rootTask.mCreatedByOrganizer) {
                rootTask = rootTask.getTopMostTask();
            }
            if (this == rootTask || isDescendantOf(rootTask)) {
                return false;
            }
        }
        if (getWrapper().getExtImpl().cropWindowsToRootTaskBounds(this)) {
            return isResizeable();
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.android.server.wm.WindowContainer
    public void getAnimationFrames(Rect rect, Rect rect2, Rect rect3, Rect rect4) {
        if (getAdjacentTask() != null) {
            super.getAnimationFrames(rect, rect2, rect3, rect4);
            return;
        }
        WindowState topVisibleAppMainWindow = getTopVisibleAppMainWindow();
        if (topVisibleAppMainWindow != null) {
            topVisibleAppMainWindow.getAnimationFrames(rect, rect2, rect3, rect4);
        } else {
            super.getAnimationFrames(rect, rect2, rect3, rect4);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void getMaxVisibleBounds(ActivityRecord activityRecord, Rect rect, boolean[] zArr) {
        WindowState findMainWindow;
        if (activityRecord.mIsExiting || !activityRecord.isClientVisible() || !activityRecord.isVisibleRequested() || (findMainWindow = activityRecord.findMainWindow()) == null) {
            return;
        }
        if (!zArr[0]) {
            zArr[0] = true;
            rect.setEmpty();
        }
        Rect rect2 = sTmpBounds;
        WindowManager.LayoutParams layoutParams = findMainWindow.mAttrs;
        rect2.set(findMainWindow.getFrame());
        rect2.inset(findMainWindow.getInsetsStateWithVisibilityOverride().calculateVisibleInsets(rect2, layoutParams.type, findMainWindow.getWindowingMode(), layoutParams.softInputMode, layoutParams.flags));
        rect.union(rect2);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void getDimBounds(final Rect rect) {
        if (isRootTask()) {
            getBounds(rect);
            return;
        }
        Task rootTask = getRootTask();
        if (inFreeformWindowingMode()) {
            final boolean[] zArr = {false};
            forAllActivities(new Consumer() { // from class: com.android.server.wm.Task$$ExternalSyntheticLambda16
                @Override // java.util.function.Consumer
                public final void accept(Object obj) {
                    Task.getMaxVisibleBounds((ActivityRecord) obj, rect, zArr);
                }
            });
            if (zArr[0]) {
                return;
            }
        }
        if (!matchParentBounds()) {
            rootTask.getBounds(this.mTmpRect);
            this.mTmpRect.intersect(getBounds());
            rect.set(this.mTmpRect);
            return;
        }
        rect.set(getBounds());
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void adjustAnimationBoundsForTransition(Rect rect) {
        if (this.mWmService.mTaskTransitionSpec != null) {
            InsetsState rawInsetsState = getDisplayContent().getInsetsStateController().getRawInsetsState();
            for (int sourceSize = rawInsetsState.sourceSize() - 1; sourceSize >= 0; sourceSize--) {
                InsetsSource sourceAt = rawInsetsState.sourceAt(sourceSize);
                if (sourceAt.insetsRoundedCornerFrame()) {
                    rect.inset(sourceAt.calculateVisibleInsets(rect));
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setDragResizing(boolean z) {
        if (this.mDragResizing != z) {
            if (z && getRootTask().getWindowingMode() != 5) {
                throw new IllegalArgumentException("Drag resize not allow for root task id=" + getRootTaskId());
            }
            this.mDragResizing = z;
            resetDragResizingChangeReported();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean isDragResizing() {
        return this.mDragResizing;
    }

    void adjustBoundsForDisplayChangeIfNeeded(DisplayContent displayContent) {
        if (displayContent == null || getRequestedOverrideBounds().isEmpty()) {
            return;
        }
        int displayId = displayContent.getDisplayId();
        int i = displayContent.getDisplayInfo().rotation;
        if (displayId != this.mLastRotationDisplayId) {
            this.mLastRotationDisplayId = displayId;
            this.mRotation = i;
        } else {
            if (this.mRotation == i) {
                return;
            }
            this.mTmpRect2.set(getBounds());
            if (!getWindowConfiguration().canResizeTask()) {
                setBounds(this.mTmpRect2);
                return;
            }
            displayContent.rotateBounds(this.mRotation, i, this.mTmpRect2);
            if (setBounds(this.mTmpRect2) != 0) {
                this.mAtmService.resizeTask(this.mTaskId, getBounds(), 1);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void cancelTaskWindowTransition() {
        for (int size = this.mChildren.size() - 1; size >= 0; size--) {
            ((WindowContainer) this.mChildren.get(size)).cancelAnimation();
        }
    }

    boolean showForAllUsers() {
        ActivityRecord topNonFinishingActivity;
        return (this.mChildren.isEmpty() || (topNonFinishingActivity = getTopNonFinishingActivity()) == null || !topNonFinishingActivity.mShowForAllUsers) ? false : true;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.android.server.wm.WindowContainer
    public boolean showToCurrentUser() {
        return this.mForceShowForAllUsers || showForAllUsers() || this.mWmService.isUserVisible(getTopMostTask().mUserId);
    }

    void setForceShowForAllUsers(boolean z) {
        this.mForceShowForAllUsers = z;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public ActivityRecord getOccludingActivityAbove(final ActivityRecord activityRecord) {
        ActivityRecord activity = getActivity(new Predicate() { // from class: com.android.server.wm.Task$$ExternalSyntheticLambda48
            @Override // java.util.function.Predicate
            public final boolean test(Object obj) {
                boolean lambda$getOccludingActivityAbove$8;
                lambda$getOccludingActivityAbove$8 = Task.lambda$getOccludingActivityAbove$8(ActivityRecord.this, (ActivityRecord) obj);
                return lambda$getOccludingActivityAbove$8;
            }
        });
        if (activity != activityRecord) {
            return activity;
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ boolean lambda$getOccludingActivityAbove$8(ActivityRecord activityRecord, ActivityRecord activityRecord2) {
        if (activityRecord2 == activityRecord) {
            return true;
        }
        if (!activityRecord2.occludesParent()) {
            return false;
        }
        TaskFragment taskFragment = activityRecord2.getTaskFragment();
        if (taskFragment == activityRecord.getTaskFragment()) {
            return true;
        }
        if (taskFragment != null && taskFragment.asTask() != null) {
            return true;
        }
        TaskFragment asTaskFragment = taskFragment.getParent().asTaskFragment();
        while (true) {
            TaskFragment taskFragment2 = taskFragment;
            taskFragment = asTaskFragment;
            if (taskFragment == null || !taskFragment2.getBounds().equals(taskFragment.getBounds())) {
                break;
            }
            if (taskFragment.asTask() != null) {
                return true;
            }
            asTaskFragment = taskFragment.getParent().asTaskFragment();
        }
        return false;
    }

    @Override // com.android.server.wm.WindowContainer, com.android.server.wm.SurfaceAnimator.Animatable
    public SurfaceControl.Builder makeAnimationLeash() {
        return super.makeAnimationLeash().setMetadata(3, this.mTaskId);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean shouldAnimate() {
        if (isOrganized()) {
            return false;
        }
        RecentsAnimationController recentsAnimationController = this.mWmService.getRecentsAnimationController();
        return (recentsAnimationController != null && recentsAnimationController.isAnimatingTask(this) && recentsAnimationController.shouldDeferCancelUntilNextTransition()) ? false : true;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.android.server.wm.WindowContainer
    public void setInitialSurfaceControlProperties(SurfaceControl.Builder builder) {
        builder.setEffectLayer().setMetadata(3, this.mTaskId);
        super.setInitialSurfaceControlProperties(builder);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean isAnimatingByRecents() {
        return isAnimating(4, 8) || this.mTransitionController.isTransientHide(this);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public WindowState getTopVisibleAppMainWindow() {
        ActivityRecord topVisibleActivity = getTopVisibleActivity();
        if (topVisibleActivity != null) {
            return topVisibleActivity.findMainWindow();
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public ActivityRecord topRunningNonDelayedActivityLocked(ActivityRecord activityRecord) {
        PooledPredicate obtainPredicate = PooledLambda.obtainPredicate(new BiPredicate() { // from class: com.android.server.wm.Task$$ExternalSyntheticLambda37
            @Override // java.util.function.BiPredicate
            public final boolean test(Object obj, Object obj2) {
                boolean isTopRunningNonDelayed;
                isTopRunningNonDelayed = Task.isTopRunningNonDelayed((ActivityRecord) obj, (ActivityRecord) obj2);
                return isTopRunningNonDelayed;
            }
        }, PooledLambda.__(ActivityRecord.class), activityRecord);
        ActivityRecord activity = getActivity(obtainPredicate);
        obtainPredicate.recycle();
        return activity;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static boolean isTopRunningNonDelayed(ActivityRecord activityRecord, ActivityRecord activityRecord2) {
        return (activityRecord.delayedResume || activityRecord == activityRecord2 || !activityRecord.canBeTopRunning()) ? false : true;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public ActivityRecord topRunningActivity(IBinder iBinder, int i) {
        PooledPredicate obtainPredicate = PooledLambda.obtainPredicate(new TriPredicate() { // from class: com.android.server.wm.Task$$ExternalSyntheticLambda14
            public final boolean test(Object obj, Object obj2, Object obj3) {
                boolean isTopRunning;
                isTopRunning = Task.isTopRunning((ActivityRecord) obj, ((Integer) obj2).intValue(), (IBinder) obj3);
                return isTopRunning;
            }
        }, PooledLambda.__(ActivityRecord.class), Integer.valueOf(i), iBinder);
        ActivityRecord activity = getActivity(obtainPredicate);
        obtainPredicate.recycle();
        return activity;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static boolean isTopRunning(ActivityRecord activityRecord, int i, IBinder iBinder) {
        return (activityRecord.getTask().mTaskId == i || activityRecord.token == iBinder || !activityRecord.canBeTopRunning()) ? false : true;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public ActivityRecord getTopFullscreenActivity() {
        return getActivity(new Predicate() { // from class: com.android.server.wm.Task$$ExternalSyntheticLambda46
            @Override // java.util.function.Predicate
            public final boolean test(Object obj) {
                boolean lambda$getTopFullscreenActivity$9;
                lambda$getTopFullscreenActivity$9 = Task.lambda$getTopFullscreenActivity$9((ActivityRecord) obj);
                return lambda$getTopFullscreenActivity$9;
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ boolean lambda$getTopFullscreenActivity$9(ActivityRecord activityRecord) {
        WindowState findMainWindow = activityRecord.findMainWindow();
        return findMainWindow != null && findMainWindow.mAttrs.isFullscreen();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ boolean lambda$getTopVisibleActivity$10(ActivityRecord activityRecord) {
        return !activityRecord.mIsExiting && activityRecord.isClientVisible() && activityRecord.isVisibleRequested();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public ActivityRecord getTopVisibleActivity() {
        return getActivity(new Predicate() { // from class: com.android.server.wm.Task$$ExternalSyntheticLambda47
            @Override // java.util.function.Predicate
            public final boolean test(Object obj) {
                boolean lambda$getTopVisibleActivity$10;
                lambda$getTopVisibleActivity$10 = Task.lambda$getTopVisibleActivity$10((ActivityRecord) obj);
                return lambda$getTopVisibleActivity$10;
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ boolean lambda$getTopRealVisibleActivity$11(ActivityRecord activityRecord) {
        return !activityRecord.mIsExiting && activityRecord.isClientVisible() && activityRecord.isVisible();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public ActivityRecord getTopRealVisibleActivity() {
        return getActivity(new Predicate() { // from class: com.android.server.wm.Task$$ExternalSyntheticLambda5
            @Override // java.util.function.Predicate
            public final boolean test(Object obj) {
                boolean lambda$getTopRealVisibleActivity$11;
                lambda$getTopRealVisibleActivity$11 = Task.lambda$getTopRealVisibleActivity$11((ActivityRecord) obj);
                return lambda$getTopRealVisibleActivity$11;
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public ActivityRecord getTopWaitSplashScreenActivity() {
        return getActivity(new Predicate() { // from class: com.android.server.wm.Task$$ExternalSyntheticLambda31
            @Override // java.util.function.Predicate
            public final boolean test(Object obj) {
                boolean lambda$getTopWaitSplashScreenActivity$12;
                lambda$getTopWaitSplashScreenActivity$12 = Task.lambda$getTopWaitSplashScreenActivity$12((ActivityRecord) obj);
                return lambda$getTopWaitSplashScreenActivity$12;
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ boolean lambda$getTopWaitSplashScreenActivity$12(ActivityRecord activityRecord) {
        return activityRecord.mHandleExitSplashScreen && activityRecord.mTransferringSplashScreenState == 1;
    }

    void setTaskDescription(ActivityManager.TaskDescription taskDescription) {
        this.mTaskDescription = taskDescription;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void onSnapshotChanged(TaskSnapshot taskSnapshot) {
        this.mLastTaskSnapshotData.set(taskSnapshot);
        this.mAtmService.getTaskChangeNotificationController().notifyTaskSnapshotChanged(this.mTaskId, taskSnapshot);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public ActivityManager.TaskDescription getTaskDescription() {
        return this.mTaskDescription;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.android.server.wm.WindowContainer
    public void forAllLeafTasks(Consumer<Task> consumer, boolean z) {
        int size = this.mChildren.size();
        boolean z2 = true;
        if (z) {
            for (int i = size - 1; i >= 0; i--) {
                Task asTask = ((WindowContainer) this.mChildren.get(i)).asTask();
                if (asTask != null) {
                    asTask.forAllLeafTasks(consumer, z);
                    z2 = false;
                }
            }
        } else {
            for (int i2 = 0; i2 < size; i2++) {
                Task asTask2 = ((WindowContainer) this.mChildren.get(i2)).asTask();
                if (asTask2 != null) {
                    asTask2.forAllLeafTasks(consumer, z);
                    z2 = false;
                }
            }
        }
        if (z2) {
            consumer.accept(this);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.android.server.wm.WindowContainer
    public void forAllTasks(Consumer<Task> consumer, boolean z) {
        super.forAllTasks(consumer, z);
        consumer.accept(this);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.android.server.wm.WindowContainer
    public void forAllRootTasks(Consumer<Task> consumer, boolean z) {
        if (isRootTask()) {
            consumer.accept(this);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.android.server.wm.WindowContainer
    public boolean forAllTasks(Predicate<Task> predicate) {
        if (super.forAllTasks(predicate)) {
            return true;
        }
        return predicate.test(this);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.android.server.wm.WindowContainer
    public boolean forAllLeafTasks(Predicate<Task> predicate) {
        boolean z = true;
        for (int size = this.mChildren.size() - 1; size >= 0; size--) {
            Task asTask = ((WindowContainer) this.mChildren.get(size)).asTask();
            if (asTask != null) {
                if (asTask.forAllLeafTasks(predicate)) {
                    return true;
                }
                z = false;
            }
        }
        if (z) {
            return predicate.test(this);
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void forAllLeafTasksAndLeafTaskFragments(final Consumer<TaskFragment> consumer, final boolean z) {
        forAllLeafTasks(new Consumer() { // from class: com.android.server.wm.Task$$ExternalSyntheticLambda34
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                Task.lambda$forAllLeafTasksAndLeafTaskFragments$13(consumer, z, (Task) obj);
            }
        }, z);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$forAllLeafTasksAndLeafTaskFragments$13(Consumer consumer, boolean z, Task task) {
        if (task.isLeafTaskFragment()) {
            consumer.accept(task);
            return;
        }
        int i = 0;
        if (z) {
            for (int size = task.mChildren.size() - 1; size >= 0; size--) {
                WindowContainer windowContainer = (WindowContainer) task.mChildren.get(size);
                if (windowContainer.asTaskFragment() != null) {
                    windowContainer.forAllLeafTaskFragments(consumer, z);
                } else if (windowContainer.asActivityRecord() != null && i == 0) {
                    consumer.accept(task);
                    i = 1;
                }
            }
            return;
        }
        boolean z2 = false;
        while (i < task.mChildren.size()) {
            WindowContainer windowContainer2 = (WindowContainer) task.mChildren.get(i);
            if (windowContainer2.asTaskFragment() != null) {
                windowContainer2.forAllLeafTaskFragments(consumer, z);
            } else if (windowContainer2.asActivityRecord() != null && !z2) {
                consumer.accept(task);
                z2 = true;
            }
            i++;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.android.server.wm.WindowContainer
    public boolean forAllRootTasks(Predicate<Task> predicate, boolean z) {
        if (isRootTask()) {
            return predicate.test(this);
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.android.server.wm.WindowContainer
    public Task getTask(Predicate<Task> predicate, boolean z) {
        Task task = super.getTask(predicate, z);
        if (task != null) {
            return task;
        }
        if (predicate.test(this)) {
            return this;
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.android.server.wm.WindowContainer
    public Task getRootTask(Predicate<Task> predicate, boolean z) {
        if (isRootTask() && predicate.test(this)) {
            return this;
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setCanAffectSystemUiFlags(boolean z) {
        this.mCanAffectSystemUiFlags = z;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean canAffectSystemUiFlags() {
        return this.mCanAffectSystemUiFlags;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void dontAnimateDimExit() {
        Dimmer dimmer = getDimmer();
        if (dimmer != null) {
            dimmer.dontAnimateExit();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.android.server.wm.ConfigurationContainer
    public String getName() {
        return "Task=" + this.mTaskId;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.android.server.wm.TaskFragment, com.android.server.wm.WindowContainer
    public Dimmer getDimmer() {
        if (inMultiWindowMode() || this.mTaskWrapper.getExtImpl().shouldUseSelfDimmer()) {
            return this.mDimmer;
        }
        if (!isRootTask() || !this.mTaskWrapper.getExtImpl().shouldUseTaskDimmer(this, this.mDimmer)) {
            return super.getDimmer();
        }
        return this.mDimmer;
    }

    @Override // com.android.server.wm.TaskFragment, com.android.server.wm.WindowContainer
    void prepareSurfaces() {
        this.mDimmer.resetDimStates();
        super.prepareSurfaces();
        this.mTaskWrapper.getExtImpl().hideBackgroundSurface(this);
        this.mTaskWrapper.getExtImpl().removeCompactMask(this, false);
        this.mTaskWrapper.getExtImpl().prepareSurfaces(getPendingTransaction(), this.mDisplayContent, this);
        Rect dimBounds = this.mDimmer.getDimBounds();
        if (dimBounds != null) {
            getDimBounds(dimBounds);
            if (inFreeformWindowingMode()) {
                getBounds(this.mTmpRect);
                int i = dimBounds.left;
                Rect rect = this.mTmpRect;
                dimBounds.offsetTo(i - rect.left, dimBounds.top - rect.top);
            } else {
                dimBounds.offsetTo(0, 0);
            }
        }
        SurfaceControl.Transaction syncTransaction = getSyncTransaction();
        this.mAtmService.getWrapper().getFlexibleExtImpl().prepareSurfaces(this);
        if (dimBounds != null && this.mDimmer.updateDims(syncTransaction)) {
            scheduleAnimation();
        }
        if (this.mTransitionController.isCollecting() && this.mCreatedByOrganizer) {
            return;
        }
        boolean isVisible = isVisible();
        boolean z = true;
        boolean z2 = isVisible || isAnimating(7);
        boolean inTransition = inTransition();
        if (WindowConfiguration.sExtImpl.isWindowingZoomMode(getWindowingMode()) || (!z2 && this.mTransitionController.mTransitionControllerExt.isRemoteAnimationPlaying(inTransition))) {
            if (!z2 && !inTransition) {
                z = false;
            }
            z2 = z;
        }
        if (this.mTaskWrapper.getExtImpl().hasNoSurfaceShowing(this, z2, this.mLastSurfaceShowing)) {
            this.mLastSurfaceShowing = false;
        }
        if (this.mSurfaceControl != null && z2 != this.mLastSurfaceShowing) {
            if (getWrapper().getExtImpl().isTaskEmbedded() && !getWrapper().getExtImpl().isReparentToTaskView()) {
                Slog.d(TAG, "prepareSurfaces skip task show while pocketstudio embeddedtask not reparent to taskview" + this);
                syncTransaction.setVisibility(this.mSurfaceControl, false);
                return;
            }
            syncTransaction.setVisibility(this.mSurfaceControl, z2);
            this.mTaskWrapper.getExtImpl().updateAlphaInPinnedMode(this, syncTransaction, this.mSurfaceControl);
        }
        TrustedOverlayHost trustedOverlayHost = this.mOverlayHost;
        if (trustedOverlayHost != null) {
            trustedOverlayHost.setVisibility(syncTransaction, isVisible);
        }
        this.mLastSurfaceShowing = z2;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.android.server.wm.WindowContainer
    public void applyAnimationUnchecked(WindowManager.LayoutParams layoutParams, boolean z, int i, boolean z2, final ArrayList<WindowContainer> arrayList) {
        RecentsAnimationController recentsAnimationController = this.mWmService.getRecentsAnimationController();
        if (recentsAnimationController != null) {
            if (!z || isActivityTypeHomeOrRecents()) {
                return;
            }
            if (ProtoLogCache.WM_DEBUG_RECENTS_ANIMATIONS_enabled) {
                ProtoLogImpl.d(ProtoLogGroup.WM_DEBUG_RECENTS_ANIMATIONS, 210750281, 0, (String) null, new Object[]{String.valueOf(recentsAnimationController), String.valueOf(asTask()), String.valueOf(AppTransition.appTransitionOldToString(i))});
            }
            final int size = arrayList != null ? arrayList.size() : 0;
            recentsAnimationController.addTaskToTargets(this, new SurfaceAnimator.OnAnimationFinishedCallback() { // from class: com.android.server.wm.Task$$ExternalSyntheticLambda29
                @Override // com.android.server.wm.SurfaceAnimator.OnAnimationFinishedCallback
                public final void onAnimationFinished(int i2, AnimationAdapter animationAdapter) {
                    Task.lambda$applyAnimationUnchecked$14(size, arrayList, i2, animationAdapter);
                }
            });
            return;
        }
        if (this.mTaskWrapper.getExtImpl().isDragZoomMode()) {
            Slog.d(TAG, "skipping app transition animation task:" + this);
            return;
        }
        if (!z && this.mTaskWrapper.getExtImpl().isNoAnimationTask(this.mTaskId)) {
            if (WindowManagerDebugConfig.DEBUG_ANIM) {
                Slog.d(TAG, "applyAnimationUnchecked ==> for exiting task we marked onAnimation, don't apply animation any more : " + this);
            }
            this.mTaskWrapper.getExtImpl().onApplyNoAnimationOfTask(this);
            return;
        }
        this.mTaskWrapper.getExtImpl().saveFixedRotatedTaskWhenKeyGuardGoingAway(this, i, z);
        super.applyAnimationUnchecked(layoutParams, z, i, z2, arrayList);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$applyAnimationUnchecked$14(int i, ArrayList arrayList, int i2, AnimationAdapter animationAdapter) {
        for (int i3 = 0; i3 < i; i3++) {
            ((WindowContainer) arrayList.get(i3)).onAnimationFinished(i2, animationAdapter);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.android.server.wm.TaskFragment, com.android.server.wm.WindowContainer
    public void dump(PrintWriter printWriter, String str, boolean z) {
        super.dump(printWriter, str, z);
        this.mAnimatingActivityRegistry.dump(printWriter, "AnimatingApps:", str);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void fillTaskInfo(TaskInfo taskInfo) {
        fillTaskInfo(taskInfo, true);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void fillTaskInfo(TaskInfo taskInfo, boolean z) {
        fillTaskInfo(taskInfo, z, getDisplayArea());
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void fillTaskInfo(TaskInfo taskInfo, boolean z, TaskDisplayArea taskDisplayArea) {
        Intent cloneFilter;
        WindowManagerGlobalLock windowManagerGlobalLock = this.mAtmService.mGlobalLock;
        WindowManagerService.boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                taskInfo.launchCookies.clear();
                taskInfo.addLaunchCookie(this.mLaunchCookie);
                ActivityRecord fillAndReturnTop = this.mTaskSupervisor.mTaskInfoHelper.fillAndReturnTop(this, taskInfo);
                taskInfo.userId = isLeafTask() ? this.mUserId : this.mCurrentUser;
                taskInfo.taskId = this.mTaskId;
                taskInfo.displayId = getDisplayId();
                taskInfo.displayAreaFeatureId = taskDisplayArea != null ? taskDisplayArea.mFeatureId : -1;
                Intent baseIntent = getBaseIntent();
                boolean z2 = false;
                int flags = baseIntent == null ? 0 : baseIntent.getFlags();
                if (baseIntent == null) {
                    cloneFilter = new Intent();
                } else {
                    cloneFilter = z ? baseIntent.cloneFilter() : new Intent(baseIntent);
                }
                taskInfo.baseIntent = cloneFilter;
                cloneFilter.setFlags(flags);
                taskInfo.isRunning = fillAndReturnTop != null;
                taskInfo.topActivity = fillAndReturnTop != null ? fillAndReturnTop.mActivityComponent : null;
                taskInfo.origActivity = this.origActivity;
                taskInfo.realActivity = this.realActivity;
                taskInfo.lastActiveTime = this.lastActiveTime;
                taskInfo.taskDescription = new ActivityManager.TaskDescription(getTaskDescription());
                taskInfo.supportsSplitScreenMultiWindow = supportsSplitScreenWindowingModeInDisplayArea(taskDisplayArea);
                taskInfo.isInFlexibleEmbedded = this.mHasBeenVisible && getWrapper().getExtImpl().isFlexibleEmbedded();
                taskInfo.uid = -1;
                taskInfo.pid = -1;
                if (fillAndReturnTop != null) {
                    taskInfo.uid = fillAndReturnTop.getUid();
                    taskInfo.pid = fillAndReturnTop.getPid();
                }
                taskInfo.supportsMultiWindow = supportsMultiWindowInDisplayArea(taskDisplayArea);
                taskInfo.configuration.setTo(getConfiguration());
                taskInfo.configuration.windowConfiguration.setActivityType(getActivityType());
                taskInfo.configuration.windowConfiguration.setWindowingMode(getWindowingMode());
                taskInfo.token = this.mRemoteToken.toWindowContainerToken();
                Task task = fillAndReturnTop != null ? fillAndReturnTop.getTask() : this;
                taskInfo.resizeMode = task.mResizeMode;
                taskInfo.topActivityType = task.getActivityType();
                taskInfo.displayCutoutInsets = task.getDisplayCutoutInsets();
                taskInfo.isResizeable = isResizeable();
                taskInfo.minWidth = this.mMinWidth;
                taskInfo.minHeight = this.mMinHeight;
                DisplayContent displayContent = this.mDisplayContent;
                taskInfo.defaultMinSize = displayContent == null ? 220 : displayContent.mMinSizeOfResizeableTaskDp;
                taskInfo.positionInParent = getRelativePosition();
                taskInfo.topActivityInfo = fillAndReturnTop != null ? fillAndReturnTop.info : null;
                PictureInPictureParams pictureInPictureParams = getPictureInPictureParams(fillAndReturnTop);
                taskInfo.pictureInPictureParams = pictureInPictureParams;
                taskInfo.launchIntoPipHostTaskId = (pictureInPictureParams == null || !pictureInPictureParams.isLaunchIntoPip() || fillAndReturnTop.getLastParentBeforePip() == null) ? -1 : fillAndReturnTop.getLastParentBeforePip().mTaskId;
                taskInfo.lastParentTaskIdBeforePip = (fillAndReturnTop == null || fillAndReturnTop.getLastParentBeforePip() == null) ? -1 : fillAndReturnTop.getLastParentBeforePip().mTaskId;
                taskInfo.shouldDockBigOverlays = fillAndReturnTop != null && fillAndReturnTop.shouldDockBigOverlays;
                taskInfo.mTopActivityLocusId = fillAndReturnTop != null ? fillAndReturnTop.getLocusId() : null;
                boolean z3 = fillAndReturnTop != null && fillAndReturnTop.getOrganizedTask() == this && fillAndReturnTop.isState(ActivityRecord.State.RESUMED);
                boolean z4 = (fillAndReturnTop != null && fillAndReturnTop.getOrganizedTask() == this && fillAndReturnTop.isVisible()) && fillAndReturnTop.inSizeCompatMode();
                taskInfo.topActivityInSizeCompat = z4;
                if (z4 && this.mWmService.mLetterboxConfiguration.isTranslucentLetterboxingEnabled()) {
                    taskInfo.topActivityInSizeCompat = fillAndReturnTop.fillsParent();
                }
                taskInfo.topActivityEligibleForLetterboxEducation = z3 && fillAndReturnTop.isEligibleForLetterboxEducation();
                taskInfo.cameraCompatControlState = z3 ? fillAndReturnTop.getCameraCompatControlState() : 0;
                Task asTask = getParent() != null ? getParent().asTask() : null;
                taskInfo.parentTaskId = (asTask == null || !asTask.mCreatedByOrganizer) ? -1 : asTask.mTaskId;
                taskInfo.isFocused = isFocused();
                taskInfo.isVisible = hasVisibleChildren();
                taskInfo.isVisibleRequested = isVisibleRequested();
                taskInfo.isSleeping = shouldSleepActivities();
                taskInfo.isLetterboxDoubleTapEnabled = fillAndReturnTop != null && fillAndReturnTop.mLetterboxUiController.isLetterboxDoubleTapEducationEnabled();
                taskInfo.topActivityLetterboxVerticalPosition = -1;
                taskInfo.topActivityLetterboxHorizontalPosition = -1;
                taskInfo.topActivityLetterboxWidth = -1;
                taskInfo.topActivityLetterboxHeight = -1;
                taskInfo.isFromLetterboxDoubleTap = fillAndReturnTop != null && fillAndReturnTop.mLetterboxUiController.isFromDoubleTap();
                if (taskInfo.isLetterboxDoubleTapEnabled) {
                    taskInfo.topActivityLetterboxWidth = fillAndReturnTop.getBounds().width();
                    int height = fillAndReturnTop.getBounds().height();
                    taskInfo.topActivityLetterboxHeight = height;
                    if (taskInfo.topActivityLetterboxWidth < height) {
                        taskInfo.topActivityLetterboxHorizontalPosition = fillAndReturnTop.mLetterboxUiController.getLetterboxPositionForHorizontalReachability();
                    } else {
                        taskInfo.topActivityLetterboxVerticalPosition = fillAndReturnTop.mLetterboxUiController.getLetterboxPositionForVerticalReachability();
                    }
                }
                if (fillAndReturnTop != null && fillAndReturnTop.getWrapper().getExtImpl().inOplusCompatMode()) {
                    z2 = true;
                }
                taskInfo.topActivityInOplusCompatMode = z2;
                if (fillAndReturnTop != null && fillAndReturnTop.info.applicationInfo.isSystemApp()) {
                    taskInfo.supportsSplitScreenMultiWindow &= fillAndReturnTop.supportsSplitScreenWindowingMode();
                }
                taskInfo.topActivityLetterboxInsets = fillAndReturnTop != null ? fillAndReturnTop.getLetterboxInsets() : new Rect();
                this.mTaskWrapper.getExtImpl().addExtraTaskInfo(this, taskInfo);
            } catch (Throwable th) {
                WindowManagerService.resetPriorityAfterLockedSection();
                throw th;
            }
        }
        WindowManagerService.resetPriorityAfterLockedSection();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ boolean lambda$trimIneffectiveInfo$15(ActivityRecord activityRecord) {
        return !activityRecord.finishing;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void trimIneffectiveInfo(Task task, TaskInfo taskInfo) {
        ActivityRecord activity = task.getActivity(new Predicate() { // from class: com.android.server.wm.Task$$ExternalSyntheticLambda17
            @Override // java.util.function.Predicate
            public final boolean test(Object obj) {
                boolean lambda$trimIneffectiveInfo$15;
                lambda$trimIneffectiveInfo$15 = Task.lambda$trimIneffectiveInfo$15((ActivityRecord) obj);
                return lambda$trimIneffectiveInfo$15;
            }
        }, false);
        int uid = activity != null ? activity.getUid() : task.effectiveUid;
        ActivityInfo activityInfo = taskInfo.topActivityInfo;
        if (activityInfo != null && task.effectiveUid != activityInfo.applicationInfo.uid) {
            ActivityInfo activityInfo2 = new ActivityInfo(taskInfo.topActivityInfo);
            taskInfo.topActivityInfo = activityInfo2;
            activityInfo2.applicationInfo = new ApplicationInfo(taskInfo.topActivityInfo.applicationInfo);
            taskInfo.topActivity = new ComponentName("", "");
            ActivityInfo activityInfo3 = taskInfo.topActivityInfo;
            activityInfo3.packageName = "";
            activityInfo3.taskAffinity = "";
            activityInfo3.processName = "";
            activityInfo3.name = "";
            activityInfo3.parentActivityName = "";
            activityInfo3.targetActivity = "";
            activityInfo3.splitName = "";
            ApplicationInfo applicationInfo = activityInfo3.applicationInfo;
            applicationInfo.className = "";
            applicationInfo.credentialProtectedDataDir = "";
            applicationInfo.dataDir = "";
            applicationInfo.deviceProtectedDataDir = "";
            applicationInfo.manageSpaceActivityName = "";
            applicationInfo.nativeLibraryDir = "";
            applicationInfo.nativeLibraryRootDir = "";
            applicationInfo.processName = "";
            applicationInfo.publicSourceDir = "";
            applicationInfo.scanPublicSourceDir = "";
            applicationInfo.scanSourceDir = "";
            applicationInfo.sourceDir = "";
            applicationInfo.taskAffinity = "";
            applicationInfo.name = "";
            applicationInfo.packageName = "";
        }
        if (task.effectiveUid != uid) {
            taskInfo.baseActivity = new ComponentName("", "");
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public PictureInPictureParams getPictureInPictureParams() {
        Task topMostTask = getTopMostTask();
        if (topMostTask == null) {
            return null;
        }
        return getPictureInPictureParams(topMostTask.getTopMostActivity());
    }

    private static PictureInPictureParams getPictureInPictureParams(ActivityRecord activityRecord) {
        if (activityRecord == null || activityRecord.pictureInPictureArgs.empty()) {
            return null;
        }
        return new PictureInPictureParams(activityRecord.pictureInPictureArgs);
    }

    private boolean shouldDockBigOverlays() {
        ActivityRecord topMostActivity = getTopMostActivity();
        return topMostActivity != null && topMostActivity.shouldDockBigOverlays;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Rect getDisplayCutoutInsets() {
        if (this.mDisplayContent == null || getDisplayInfo().displayCutout == null) {
            return null;
        }
        WindowState topVisibleAppMainWindow = getTopVisibleAppMainWindow();
        int i = topVisibleAppMainWindow == null ? 0 : topVisibleAppMainWindow.getAttrs().layoutInDisplayCutoutMode;
        if (i == 3 || i == 1) {
            return null;
        }
        return getDisplayInfo().displayCutout.getSafeInsets();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public ActivityManager.RunningTaskInfo getTaskInfo() {
        ActivityManager.RunningTaskInfo runningTaskInfo = new ActivityManager.RunningTaskInfo();
        fillTaskInfo(runningTaskInfo);
        return runningTaskInfo;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public StartingWindowInfo getStartingWindowInfo(ActivityRecord activityRecord) {
        ActivityInfo activityInfo;
        WindowState topFullscreenOpaqueWindow;
        WindowState window;
        StartingWindowInfo startingWindowInfo = new StartingWindowInfo();
        ActivityManager.RunningTaskInfo taskInfo = getTaskInfo();
        startingWindowInfo.taskInfo = taskInfo;
        ActivityInfo activityInfo2 = taskInfo.topActivityInfo;
        if (activityInfo2 == null || (activityInfo = activityRecord.info) == activityInfo2) {
            activityInfo = null;
        }
        startingWindowInfo.targetActivityInfo = activityInfo;
        startingWindowInfo.isKeyguardOccluded = this.mAtmService.mKeyguardController.isDisplayOccluded(0);
        StartingData startingData = activityRecord.mStartingData;
        int i = startingData != null ? startingData.mTypeParams : 272;
        startingWindowInfo.startingWindowTypeParameter = i;
        if ((i & 16) != 0 && (window = getWindow(new Predicate() { // from class: com.android.server.wm.Task$$ExternalSyntheticLambda11
            @Override // java.util.function.Predicate
            public final boolean test(Object obj) {
                boolean lambda$getStartingWindowInfo$16;
                lambda$getStartingWindowInfo$16 = Task.lambda$getStartingWindowInfo$16((WindowState) obj);
                return lambda$getStartingWindowInfo$16;
            }
        })) != null) {
            startingWindowInfo.mainWindowLayoutParams = window.getAttrs();
            startingWindowInfo.requestedVisibleTypes = window.getRequestedVisibleTypes();
        }
        startingWindowInfo.taskInfo.configuration.setTo(activityRecord.getConfiguration());
        ActivityRecord topFullscreenActivity = getTopFullscreenActivity();
        if (topFullscreenActivity != null && (topFullscreenOpaqueWindow = topFullscreenActivity.getTopFullscreenOpaqueWindow()) != null) {
            startingWindowInfo.topOpaqueWindowInsetsState = topFullscreenOpaqueWindow.getInsetsStateWithVisibilityOverride();
            startingWindowInfo.topOpaqueWindowLayoutParams = topFullscreenOpaqueWindow.getAttrs();
        }
        return startingWindowInfo;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ boolean lambda$getStartingWindowInfo$16(WindowState windowState) {
        return windowState.mAttrs.type == 1;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public TaskFragmentParentInfo getTaskFragmentParentInfo() {
        return new TaskFragmentParentInfo(getConfiguration(), getDisplayId(), shouldBeVisible(null));
    }

    @Override // com.android.server.wm.TaskFragment, com.android.server.wm.WindowContainer
    protected boolean onChildVisibleRequestedChanged(WindowContainer windowContainer) {
        if (!super.onChildVisibleRequestedChanged(windowContainer)) {
            return false;
        }
        sendTaskFragmentParentInfoChangedIfNeeded();
        return true;
    }

    void sendTaskFragmentParentInfoChangedIfNeeded() {
        TaskFragment taskFragment;
        if (isLeafTask() && (taskFragment = getTaskFragment(new Predicate() { // from class: com.android.server.wm.Task$$ExternalSyntheticLambda7
            @Override // java.util.function.Predicate
            public final boolean test(Object obj) {
                return ((TaskFragment) obj).isOrganizedTaskFragment();
            }
        })) != null) {
            taskFragment.sendTaskFragmentParentInfoChanged();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean isTaskId(int i) {
        return this.mTaskId == i;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public ActivityRecord isInTask(ActivityRecord activityRecord) {
        if (activityRecord != null && activityRecord.isDescendantOf(this)) {
            return activityRecord;
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void dump(PrintWriter printWriter, String str) {
        printWriter.print(str);
        printWriter.print("userId=");
        printWriter.print(this.mUserId);
        printWriter.print(" effectiveUid=");
        UserHandle.formatUid(printWriter, this.effectiveUid);
        printWriter.print(" mCallingUid=");
        UserHandle.formatUid(printWriter, this.mCallingUid);
        printWriter.print(" mUserSetupComplete=");
        printWriter.print(this.mUserSetupComplete);
        printWriter.print(" mCallingPackage=");
        printWriter.print(this.mCallingPackage);
        printWriter.print(" mCallingFeatureId=");
        printWriter.println(this.mCallingFeatureId);
        if (this.affinity != null || this.rootAffinity != null) {
            printWriter.print(str);
            printWriter.print("affinity=");
            printWriter.print(this.affinity);
            String str2 = this.affinity;
            if (str2 == null || !str2.equals(this.rootAffinity)) {
                printWriter.print(" root=");
                printWriter.println(this.rootAffinity);
            } else {
                printWriter.println();
            }
        }
        if (this.mWindowLayoutAffinity != null) {
            printWriter.print(str);
            printWriter.print("windowLayoutAffinity=");
            printWriter.println(this.mWindowLayoutAffinity);
        }
        if (this.voiceSession != null || this.voiceInteractor != null) {
            printWriter.print(str);
            printWriter.print("VOICE: session=0x");
            printWriter.print(Integer.toHexString(System.identityHashCode(this.voiceSession)));
            printWriter.print(" interactor=0x");
            printWriter.println(Integer.toHexString(System.identityHashCode(this.voiceInteractor)));
        }
        if (this.intent != null) {
            StringBuilder sb = new StringBuilder(128);
            sb.append(str);
            sb.append("intent={");
            this.intent.toShortString(sb, false, true, false, false);
            sb.append('}');
            printWriter.println(sb.toString());
        }
        if (this.affinityIntent != null) {
            StringBuilder sb2 = new StringBuilder(128);
            sb2.append(str);
            sb2.append("affinityIntent={");
            this.affinityIntent.toShortString(sb2, false, true, false, false);
            sb2.append('}');
            printWriter.println(sb2.toString());
        }
        if (this.origActivity != null) {
            printWriter.print(str);
            printWriter.print("origActivity=");
            printWriter.println(this.origActivity.flattenToShortString());
        }
        if (this.realActivity != null) {
            printWriter.print(str);
            printWriter.print("mActivityComponent=");
            printWriter.println(this.realActivity.flattenToShortString());
        }
        if (this.autoRemoveRecents || this.isPersistable || !isActivityTypeStandard()) {
            printWriter.print(str);
            printWriter.print("autoRemoveRecents=");
            printWriter.print(this.autoRemoveRecents);
            printWriter.print(" isPersistable=");
            printWriter.print(this.isPersistable);
            printWriter.print(" activityType=");
            printWriter.println(getActivityType());
        }
        if (this.rootWasReset || this.mNeverRelinquishIdentity || this.mReuseTask || this.mLockTaskAuth != 1) {
            printWriter.print(str);
            printWriter.print("rootWasReset=");
            printWriter.print(this.rootWasReset);
            printWriter.print(" mNeverRelinquishIdentity=");
            printWriter.print(this.mNeverRelinquishIdentity);
            printWriter.print(" mReuseTask=");
            printWriter.print(this.mReuseTask);
            printWriter.print(" mLockTaskAuth=");
            printWriter.println(lockTaskAuthToString());
        }
        if (this.mAffiliatedTaskId != this.mTaskId || this.mPrevAffiliateTaskId != -1 || this.mPrevAffiliate != null || this.mNextAffiliateTaskId != -1 || this.mNextAffiliate != null) {
            printWriter.print(str);
            printWriter.print("affiliation=");
            printWriter.print(this.mAffiliatedTaskId);
            printWriter.print(" prevAffiliation=");
            printWriter.print(this.mPrevAffiliateTaskId);
            printWriter.print(" (");
            Task task = this.mPrevAffiliate;
            if (task == null) {
                printWriter.print("null");
            } else {
                printWriter.print(Integer.toHexString(System.identityHashCode(task)));
            }
            printWriter.print(") nextAffiliation=");
            printWriter.print(this.mNextAffiliateTaskId);
            printWriter.print(" (");
            Task task2 = this.mNextAffiliate;
            if (task2 == null) {
                printWriter.print("null");
            } else {
                printWriter.print(Integer.toHexString(System.identityHashCode(task2)));
            }
            printWriter.println(")");
        }
        printWriter.print(str);
        printWriter.print("Activities=");
        printWriter.println(this.mChildren);
        if (!this.askedCompatMode || !this.inRecents || !this.isAvailable) {
            printWriter.print(str);
            printWriter.print("askedCompatMode=");
            printWriter.print(this.askedCompatMode);
            printWriter.print(" inRecents=");
            printWriter.print(this.inRecents);
            printWriter.print(" isAvailable=");
            printWriter.println(this.isAvailable);
        }
        if (this.lastDescription != null) {
            printWriter.print(str);
            printWriter.print("lastDescription=");
            printWriter.println(this.lastDescription);
        }
        if (this.mRootProcess != null) {
            printWriter.print(str);
            printWriter.print("mRootProcess=");
            printWriter.println(this.mRootProcess);
        }
        if (this.mSharedStartingData != null) {
            printWriter.println(str + "mSharedStartingData=" + this.mSharedStartingData);
        }
        if (this.mKillProcessesOnDestroyed) {
            printWriter.println(str + "mKillProcessesOnDestroyed=true");
        }
        printWriter.print(str);
        printWriter.print("taskId=" + this.mTaskId);
        printWriter.println(" rootTaskId=" + getRootTaskId());
        printWriter.print(str);
        StringBuilder sb3 = new StringBuilder();
        sb3.append("hasChildPipActivity=");
        sb3.append(this.mChildPipActivity != null);
        printWriter.println(sb3.toString());
        printWriter.print(str);
        printWriter.print("mHasBeenVisible=");
        printWriter.println(getHasBeenVisible());
        printWriter.print(str);
        printWriter.print("mResizeMode=");
        printWriter.print(ActivityInfo.resizeModeToString(this.mResizeMode));
        printWriter.print(" mSupportsPictureInPicture=");
        printWriter.print(this.mSupportsPictureInPicture);
        printWriter.print(" isResizeable=");
        printWriter.println(isResizeable());
        printWriter.print(str);
        printWriter.print("lastActiveTime=");
        printWriter.print(this.lastActiveTime);
        printWriter.println(" (inactive for " + (getInactiveDuration() / 1000) + "s)");
        this.mTaskWrapper.getExtImpl().dump(printWriter, str);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.android.server.wm.TaskFragment
    public String toFullString() {
        StringBuilder sb = new StringBuilder(192);
        sb.append(this);
        sb.setLength(sb.length() - 1);
        sb.append(" U=");
        sb.append(this.mUserId);
        Task rootTask = getRootTask();
        if (rootTask != this) {
            sb.append(" rootTaskId=");
            sb.append(rootTask.mTaskId);
        }
        sb.append(" visible=");
        sb.append(shouldBeVisible(null));
        sb.append(" visibleRequested=");
        sb.append(isVisibleRequested());
        sb.append(" mode=");
        sb.append(WindowConfiguration.windowingModeToString(getWindowingMode()));
        sb.append(" translucent=");
        sb.append(isTranslucent(null));
        sb.append(" sz=");
        sb.append(getChildCount());
        sb.append('}');
        return sb.toString();
    }

    @Override // com.android.server.wm.TaskFragment
    public String toString() {
        String str = this.stringName;
        if (str != null) {
            return str;
        }
        StringBuilder sb = new StringBuilder(128);
        sb.append("Task{");
        sb.append(Integer.toHexString(System.identityHashCode(this)));
        sb.append(" #");
        sb.append(this.mTaskId);
        sb.append(" type=" + WindowConfiguration.activityTypeToString(getActivityType()));
        if (this.affinity != null) {
            sb.append(" A=");
            sb.append(this.affinity);
        } else {
            Intent intent = this.intent;
            if (intent != null && intent.getComponent() != null) {
                sb.append(" I=");
                sb.append(this.intent.getComponent().flattenToShortString());
            } else {
                Intent intent2 = this.affinityIntent;
                if (intent2 != null && intent2.getComponent() != null) {
                    sb.append(" aI=");
                    sb.append(this.affinityIntent.getComponent().flattenToShortString());
                }
            }
        }
        sb.append('}');
        String sb2 = sb.toString();
        this.stringName = sb2;
        return sb2;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void saveToXml(TypedXmlSerializer typedXmlSerializer) throws Exception {
        if (ActivityTaskManagerDebugConfig.DEBUG_RECENTS) {
            Slog.i(TAG_RECENTS, "Saving task=" + this);
        }
        typedXmlSerializer.attributeInt((String) null, ATTR_TASKID, this.mTaskId);
        ComponentName componentName = this.realActivity;
        if (componentName != null) {
            typedXmlSerializer.attribute((String) null, ATTR_REALACTIVITY, componentName.flattenToShortString());
        }
        typedXmlSerializer.attributeBoolean((String) null, ATTR_REALACTIVITY_SUSPENDED, this.realActivitySuspended);
        ComponentName componentName2 = this.origActivity;
        if (componentName2 != null) {
            typedXmlSerializer.attribute((String) null, ATTR_ORIGACTIVITY, componentName2.flattenToShortString());
        }
        String str = this.affinity;
        if (str != null) {
            typedXmlSerializer.attribute((String) null, ATTR_AFFINITY, str);
            if (!this.affinity.equals(this.rootAffinity)) {
                String str2 = this.rootAffinity;
                typedXmlSerializer.attribute((String) null, ATTR_ROOT_AFFINITY, str2 != null ? str2 : "@");
            }
        } else {
            String str3 = this.rootAffinity;
            if (str3 != null) {
                typedXmlSerializer.attribute((String) null, ATTR_ROOT_AFFINITY, str3 != null ? str3 : "@");
            }
        }
        String str4 = this.mWindowLayoutAffinity;
        if (str4 != null) {
            typedXmlSerializer.attribute((String) null, ATTR_WINDOW_LAYOUT_AFFINITY, str4);
        }
        typedXmlSerializer.attributeBoolean((String) null, ATTR_ROOTHASRESET, this.rootWasReset);
        typedXmlSerializer.attributeBoolean((String) null, ATTR_AUTOREMOVERECENTS, this.autoRemoveRecents);
        typedXmlSerializer.attributeBoolean((String) null, ATTR_ASKEDCOMPATMODE, this.askedCompatMode);
        typedXmlSerializer.attributeInt((String) null, ATTR_USERID, this.mUserId);
        typedXmlSerializer.attributeBoolean((String) null, ATTR_USER_SETUP_COMPLETE, this.mUserSetupComplete);
        typedXmlSerializer.attributeInt((String) null, ATTR_EFFECTIVE_UID, this.effectiveUid);
        typedXmlSerializer.attributeLong((String) null, ATTR_LASTTIMEMOVED, this.mLastTimeMoved);
        typedXmlSerializer.attributeBoolean((String) null, ATTR_NEVERRELINQUISH, this.mNeverRelinquishIdentity);
        CharSequence charSequence = this.lastDescription;
        if (charSequence != null) {
            typedXmlSerializer.attribute((String) null, ATTR_LASTDESCRIPTION, charSequence.toString());
        }
        if (getTaskDescription() != null) {
            getTaskDescription().saveToXml(typedXmlSerializer);
        }
        typedXmlSerializer.attributeInt((String) null, ATTR_TASK_AFFILIATION, this.mAffiliatedTaskId);
        typedXmlSerializer.attributeInt((String) null, ATTR_PREV_AFFILIATION, this.mPrevAffiliateTaskId);
        typedXmlSerializer.attributeInt((String) null, ATTR_NEXT_AFFILIATION, this.mNextAffiliateTaskId);
        typedXmlSerializer.attributeInt((String) null, ATTR_CALLING_UID, this.mCallingUid);
        String str5 = this.mCallingPackage;
        if (str5 == null) {
            str5 = "";
        }
        typedXmlSerializer.attribute((String) null, ATTR_CALLING_PACKAGE, str5);
        String str6 = this.mCallingFeatureId;
        typedXmlSerializer.attribute((String) null, ATTR_CALLING_FEATURE_ID, str6 != null ? str6 : "");
        typedXmlSerializer.attributeInt((String) null, ATTR_RESIZE_MODE, this.mResizeMode);
        typedXmlSerializer.attributeBoolean((String) null, ATTR_SUPPORTS_PICTURE_IN_PICTURE, this.mSupportsPictureInPicture);
        Rect rect = this.mLastNonFullscreenBounds;
        if (rect != null) {
            typedXmlSerializer.attribute((String) null, ATTR_NON_FULLSCREEN_BOUNDS, rect.flattenToString());
        }
        typedXmlSerializer.attributeInt((String) null, ATTR_MIN_WIDTH, this.mMinWidth);
        typedXmlSerializer.attributeInt((String) null, ATTR_MIN_HEIGHT, this.mMinHeight);
        typedXmlSerializer.attributeInt((String) null, ATTR_PERSIST_TASK_VERSION, 1);
        Point point = this.mLastTaskSnapshotData.taskSize;
        if (point != null) {
            typedXmlSerializer.attribute((String) null, ATTR_LAST_SNAPSHOT_TASK_SIZE, point.flattenToString());
        }
        Rect rect2 = this.mLastTaskSnapshotData.contentInsets;
        if (rect2 != null) {
            typedXmlSerializer.attribute((String) null, ATTR_LAST_SNAPSHOT_CONTENT_INSETS, rect2.flattenToString());
        }
        Point point2 = this.mLastTaskSnapshotData.bufferSize;
        if (point2 != null) {
            typedXmlSerializer.attribute((String) null, ATTR_LAST_SNAPSHOT_BUFFER_SIZE, point2.flattenToString());
        }
        if (this.affinityIntent != null) {
            typedXmlSerializer.startTag((String) null, TAG_AFFINITYINTENT);
            this.affinityIntent.saveToXml(typedXmlSerializer);
            typedXmlSerializer.endTag((String) null, TAG_AFFINITYINTENT);
        }
        if (this.intent != null) {
            typedXmlSerializer.startTag((String) null, TAG_INTENT);
            this.intent.saveToXml(typedXmlSerializer);
            typedXmlSerializer.endTag((String) null, TAG_INTENT);
        }
        typedXmlSerializer.startTag((String) null, ATTR_EMBEDDED_CONTAINER);
        typedXmlSerializer.attributeBoolean((String) null, ATTR_IS_CONTAINER_TASK, this.mTaskWrapper.getExtImpl().isContainerTask());
        typedXmlSerializer.attributeBoolean((String) null, ATTR_IS_SHOW_RECENT, this.mTaskWrapper.getExtImpl().isShowRecent());
        typedXmlSerializer.attributeInt((String) null, ATTR_CONTAINER_TASK_ID, this.mTaskWrapper.getExtImpl().getEmbeddedContainerTaskId());
        if (this.mTaskWrapper.getExtImpl().getEmbeddedChildren() != null && this.mTaskWrapper.getExtImpl().getEmbeddedChildren().size() > 0) {
            typedXmlSerializer.attribute((String) null, ATTR_EMBEDDED_CHILDREN, (String) ((List) this.mTaskWrapper.getExtImpl().getEmbeddedChildren().stream().collect(Collectors.toList())).stream().map(new Function() { // from class: com.android.server.wm.Task$$ExternalSyntheticLambda12
                @Override // java.util.function.Function
                public final Object apply(Object obj) {
                    return String.valueOf((Integer) obj);
                }
            }).collect(Collectors.joining(",")));
        }
        Intent intent = this.intent;
        if (intent != null && (intent.getIntentExt().getOplusFlags() & 16384) != 0) {
            typedXmlSerializer.attributeInt((String) null, ATTR_OPLUS_FLAGS, 16384);
        }
        typedXmlSerializer.endTag((String) null, ATTR_EMBEDDED_CONTAINER);
        sTmpException = null;
        PooledPredicate obtainPredicate = PooledLambda.obtainPredicate(new TriPredicate() { // from class: com.android.server.wm.Task$$ExternalSyntheticLambda13
            public final boolean test(Object obj, Object obj2, Object obj3) {
                boolean saveActivityToXml;
                saveActivityToXml = Task.saveActivityToXml((ActivityRecord) obj, (ActivityRecord) obj2, (TypedXmlSerializer) obj3);
                return saveActivityToXml;
            }
        }, PooledLambda.__(ActivityRecord.class), getBottomMostActivity(), typedXmlSerializer);
        forAllActivities((Predicate<ActivityRecord>) obtainPredicate);
        obtainPredicate.recycle();
        Exception exc = sTmpException;
        if (exc != null) {
            throw exc;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static boolean saveActivityToXml(ActivityRecord activityRecord, ActivityRecord activityRecord2, TypedXmlSerializer typedXmlSerializer) {
        if (activityRecord.info.persistableMode != 0 && activityRecord.isPersistable() && (((activityRecord.intent.getFlags() & 524288) | 8192) != 524288 || activityRecord == activityRecord2)) {
            try {
                typedXmlSerializer.startTag((String) null, TAG_ACTIVITY);
                activityRecord.saveToXml(typedXmlSerializer);
                typedXmlSerializer.endTag((String) null, TAG_ACTIVITY);
                return false;
            } catch (Exception e) {
                sTmpException = e;
            }
        }
        return true;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: Failed to find 'out' block for switch in B:8:0x025a. Please report as an issue. */
    public static Task restoreFromXml(TypedXmlPullParser typedXmlPullParser, ActivityTaskSupervisor activityTaskSupervisor) throws IOException, XmlPullParserException {
        boolean z;
        ActivityManager.TaskDescription taskDescription;
        ActivityManager.RecentTaskInfo.PersistedTaskSnapshotData persistedTaskSnapshotData;
        boolean z2;
        int i;
        int i2;
        int i3;
        boolean z3;
        boolean z4;
        boolean z5;
        int i4;
        ApplicationInfo applicationInfo;
        int i5;
        String str;
        char c;
        long j;
        TypedXmlPullParser typedXmlPullParser2 = typedXmlPullParser;
        ArrayList arrayList = new ArrayList();
        int depth = typedXmlPullParser.getDepth();
        ActivityManager.TaskDescription taskDescription2 = new ActivityManager.TaskDescription();
        ActivityManager.RecentTaskInfo.PersistedTaskSnapshotData persistedTaskSnapshotData2 = new ActivityManager.RecentTaskInfo.PersistedTaskSnapshotData();
        long j2 = 0;
        String str2 = "";
        boolean z6 = true;
        boolean z7 = true;
        String str3 = null;
        ComponentName componentName = null;
        String str4 = null;
        int i6 = -1;
        int i7 = -1;
        boolean z8 = false;
        boolean z9 = false;
        ComponentName componentName2 = null;
        String str5 = null;
        boolean z10 = false;
        boolean z11 = false;
        boolean z12 = false;
        int i8 = 0;
        int i9 = 0;
        String str6 = null;
        int i10 = -1;
        int i11 = -1;
        int i12 = -1;
        int i13 = -1;
        String str7 = null;
        int i14 = 4;
        boolean z13 = false;
        Rect rect = null;
        int i15 = -1;
        int i16 = -1;
        int i17 = 0;
        for (int attributeCount = typedXmlPullParser.getAttributeCount() - 1; attributeCount >= 0; attributeCount--) {
            String attributeName = typedXmlPullParser2.getAttributeName(attributeCount);
            String attributeValue = typedXmlPullParser2.getAttributeValue(attributeCount);
            attributeName.hashCode();
            switch (attributeName.hashCode()) {
                case -2134816935:
                    str = str2;
                    if (attributeName.equals(ATTR_ASKEDCOMPATMODE)) {
                        c = 0;
                        break;
                    }
                    break;
                case -1588736338:
                    str = str2;
                    if (attributeName.equals(ATTR_LAST_SNAPSHOT_CONTENT_INSETS)) {
                        c = 1;
                        break;
                    }
                    break;
                case -1556983798:
                    str = str2;
                    if (attributeName.equals(ATTR_LASTTIMEMOVED)) {
                        c = 2;
                        break;
                    }
                    break;
                case -1537240555:
                    str = str2;
                    if (attributeName.equals(ATTR_TASKID)) {
                        c = 3;
                        break;
                    }
                    break;
                case -1494902876:
                    str = str2;
                    if (attributeName.equals(ATTR_NEXT_AFFILIATION)) {
                        c = 4;
                        break;
                    }
                    break;
                case -1138503444:
                    str = str2;
                    if (attributeName.equals(ATTR_REALACTIVITY_SUSPENDED)) {
                        c = 5;
                        break;
                    }
                    break;
                case -1124927690:
                    str = str2;
                    if (attributeName.equals(ATTR_TASK_AFFILIATION)) {
                        c = 6;
                        break;
                    }
                    break;
                case -974080081:
                    str = str2;
                    if (attributeName.equals(ATTR_USER_SETUP_COMPLETE)) {
                        c = 7;
                        break;
                    }
                    break;
                case -929566280:
                    str = str2;
                    if (attributeName.equals(ATTR_EFFECTIVE_UID)) {
                        c = '\b';
                        break;
                    }
                    break;
                case -865458610:
                    str = str2;
                    if (attributeName.equals(ATTR_RESIZE_MODE)) {
                        c = '\t';
                        break;
                    }
                    break;
                case -826243148:
                    str = str2;
                    if (attributeName.equals(ATTR_MIN_HEIGHT)) {
                        c = '\n';
                        break;
                    }
                    break;
                case -801863159:
                    str = str2;
                    if (attributeName.equals(ATTR_LAST_SNAPSHOT_TASK_SIZE)) {
                        c = 11;
                        break;
                    }
                    break;
                case -707249465:
                    str = str2;
                    if (attributeName.equals(ATTR_NON_FULLSCREEN_BOUNDS)) {
                        c = '\f';
                        break;
                    }
                    break;
                case -705269939:
                    str = str2;
                    if (attributeName.equals(ATTR_ORIGACTIVITY)) {
                        c = '\r';
                        break;
                    }
                    break;
                case -551322450:
                    str = str2;
                    if (attributeName.equals(ATTR_LAST_SNAPSHOT_BUFFER_SIZE)) {
                        c = 14;
                        break;
                    }
                    break;
                case -502399667:
                    str = str2;
                    if (attributeName.equals(ATTR_AUTOREMOVERECENTS)) {
                        c = 15;
                        break;
                    }
                    break;
                case -360792224:
                    str = str2;
                    if (attributeName.equals(ATTR_SUPPORTS_PICTURE_IN_PICTURE)) {
                        c = 16;
                        break;
                    }
                    break;
                case -162744347:
                    str = str2;
                    if (attributeName.equals(ATTR_ROOT_AFFINITY)) {
                        c = 17;
                        break;
                    }
                    break;
                case -147132913:
                    str = str2;
                    if (attributeName.equals(ATTR_USERID)) {
                        c = 18;
                        break;
                    }
                    break;
                case -132216235:
                    str = str2;
                    if (attributeName.equals(ATTR_CALLING_UID)) {
                        c = 19;
                        break;
                    }
                    break;
                case 180927924:
                    str = str2;
                    if (attributeName.equals(ATTR_TASKTYPE)) {
                        c = 20;
                        break;
                    }
                    break;
                case 331206372:
                    str = str2;
                    if (attributeName.equals(ATTR_PREV_AFFILIATION)) {
                        c = 21;
                        break;
                    }
                    break;
                case 394454367:
                    str = str2;
                    if (attributeName.equals(ATTR_CALLING_FEATURE_ID)) {
                        c = 22;
                        break;
                    }
                    break;
                case 541503897:
                    str = str2;
                    if (attributeName.equals(ATTR_MIN_WIDTH)) {
                        c = 23;
                        break;
                    }
                    break;
                case 605497640:
                    str = str2;
                    if (attributeName.equals(ATTR_AFFINITY)) {
                        c = 24;
                        break;
                    }
                    break;
                case 869221331:
                    str = str2;
                    if (attributeName.equals(ATTR_LASTDESCRIPTION)) {
                        c = 25;
                        break;
                    }
                    break;
                case 1007873193:
                    str = str2;
                    if (attributeName.equals(ATTR_PERSIST_TASK_VERSION)) {
                        c = 26;
                        break;
                    }
                    break;
                case 1081438155:
                    str = str2;
                    if (attributeName.equals(ATTR_CALLING_PACKAGE)) {
                        c = 27;
                        break;
                    }
                    break;
                case 1457608782:
                    str = str2;
                    if (attributeName.equals(ATTR_NEVERRELINQUISH)) {
                        c = 28;
                        break;
                    }
                    break;
                case 1539554448:
                    str = str2;
                    if (attributeName.equals(ATTR_REALACTIVITY)) {
                        c = 29;
                        break;
                    }
                    break;
                case 1999609934:
                    str = str2;
                    if (attributeName.equals(ATTR_WINDOW_LAYOUT_AFFINITY)) {
                        c = 30;
                        break;
                    }
                    break;
                case 2023391309:
                    str = str2;
                    if (attributeName.equals(ATTR_ROOTHASRESET)) {
                        c = 31;
                        break;
                    }
                    break;
                default:
                    str = str2;
                    break;
            }
            c = 65535;
            switch (c) {
                case 0:
                    z12 = Boolean.parseBoolean(attributeValue);
                    str2 = str;
                    break;
                case 1:
                    j = j2;
                    persistedTaskSnapshotData2.contentInsets = Rect.unflattenFromString(attributeValue);
                    str2 = str;
                    j2 = j;
                    break;
                case 2:
                    j2 = Long.parseLong(attributeValue);
                    str2 = str;
                    break;
                case 3:
                    j = j2;
                    if (i6 == -1) {
                        i6 = Integer.parseInt(attributeValue);
                    }
                    str2 = str;
                    j2 = j;
                    break;
                case 4:
                    i12 = Integer.parseInt(attributeValue);
                    str2 = str;
                    break;
                case 5:
                    z9 = Boolean.valueOf(attributeValue).booleanValue();
                    str2 = str;
                    break;
                case 6:
                    i10 = Integer.parseInt(attributeValue);
                    str2 = str;
                    break;
                case 7:
                    z6 = Boolean.parseBoolean(attributeValue);
                    str2 = str;
                    break;
                case '\b':
                    i7 = Integer.parseInt(attributeValue);
                    str2 = str;
                    break;
                case '\t':
                    i14 = Integer.parseInt(attributeValue);
                    str2 = str;
                    break;
                case '\n':
                    i16 = Integer.parseInt(attributeValue);
                    str2 = str;
                    break;
                case 11:
                    j = j2;
                    persistedTaskSnapshotData2.taskSize = Point.unflattenFromString(attributeValue);
                    str2 = str;
                    j2 = j;
                    break;
                case '\f':
                    rect = Rect.unflattenFromString(attributeValue);
                    str2 = str;
                    break;
                case '\r':
                    componentName2 = ComponentName.unflattenFromString(attributeValue);
                    str2 = str;
                    break;
                case 14:
                    j = j2;
                    persistedTaskSnapshotData2.bufferSize = Point.unflattenFromString(attributeValue);
                    str2 = str;
                    j2 = j;
                    break;
                case 15:
                    z11 = Boolean.parseBoolean(attributeValue);
                    str2 = str;
                    break;
                case 16:
                    z13 = Boolean.parseBoolean(attributeValue);
                    str2 = str;
                    break;
                case 17:
                    str4 = attributeValue;
                    str2 = str;
                    z8 = true;
                    break;
                case 18:
                    i9 = Integer.parseInt(attributeValue);
                    str2 = str;
                    break;
                case 19:
                    i13 = Integer.parseInt(attributeValue);
                    str2 = str;
                    break;
                case 20:
                    i8 = Integer.parseInt(attributeValue);
                    str2 = str;
                    break;
                case 21:
                    i11 = Integer.parseInt(attributeValue);
                    str2 = str;
                    break;
                case 22:
                    str7 = attributeValue;
                    str2 = str;
                    break;
                case 23:
                    i15 = Integer.parseInt(attributeValue);
                    str2 = str;
                    break;
                case 24:
                    str3 = attributeValue;
                    str2 = str;
                    break;
                case 25:
                    str6 = attributeValue;
                    str2 = str;
                    break;
                case 26:
                    i17 = Integer.parseInt(attributeValue);
                    str2 = str;
                    break;
                case 27:
                    str2 = attributeValue;
                    break;
                case 28:
                    z7 = Boolean.parseBoolean(attributeValue);
                    str2 = str;
                    break;
                case 29:
                    componentName = ComponentName.unflattenFromString(attributeValue);
                    str2 = str;
                    break;
                case 30:
                    str5 = attributeValue;
                    str2 = str;
                    break;
                case 31:
                    z10 = Boolean.parseBoolean(attributeValue);
                    str2 = str;
                    break;
                default:
                    if (attributeName.startsWith("task_description_")) {
                        j = j2;
                    } else {
                        StringBuilder sb = new StringBuilder();
                        j = j2;
                        sb.append("Task: Unknown attribute=");
                        sb.append(attributeName);
                        Slog.w(TAG, sb.toString());
                    }
                    str2 = str;
                    j2 = j;
                    break;
            }
        }
        long j3 = j2;
        String str8 = str2;
        taskDescription2.restoreFromXml(typedXmlPullParser2);
        Set arraySet = new ArraySet();
        int i18 = -1;
        boolean z14 = true;
        Intent intent = null;
        Intent intent2 = null;
        boolean z15 = false;
        int i19 = 0;
        while (true) {
            int next = typedXmlPullParser.next();
            z = z14;
            if (next != 1 && (next != 3 || typedXmlPullParser.getDepth() >= depth)) {
                if (next == 2) {
                    String name = typedXmlPullParser.getName();
                    if (TAG_AFFINITYINTENT.equals(name)) {
                        i5 = depth;
                        intent2 = Intent.restoreFromXml(typedXmlPullParser);
                    } else if (TAG_INTENT.equals(name)) {
                        i5 = depth;
                        intent = Intent.restoreFromXml(typedXmlPullParser);
                    } else if (ATTR_EMBEDDED_CONTAINER.equals(name)) {
                        int attributeCount2 = typedXmlPullParser.getAttributeCount() - 1;
                        int i20 = i18;
                        while (attributeCount2 >= 0) {
                            int i21 = depth;
                            String attributeName2 = typedXmlPullParser2.getAttributeName(attributeCount2);
                            int i22 = i20;
                            String attributeValue2 = typedXmlPullParser2.getAttributeValue(attributeCount2);
                            if (ATTR_IS_CONTAINER_TASK.equals(attributeName2)) {
                                z15 = Boolean.parseBoolean(attributeValue2);
                            } else if (ATTR_IS_SHOW_RECENT.equals(attributeName2)) {
                                z = Boolean.parseBoolean(attributeValue2);
                            } else if (ATTR_CONTAINER_TASK_ID.equals(attributeName2)) {
                                i20 = Integer.parseInt(attributeValue2);
                                attributeCount2--;
                                typedXmlPullParser2 = typedXmlPullParser;
                                depth = i21;
                            } else if (ATTR_EMBEDDED_CHILDREN.equals(attributeName2)) {
                                if (attributeValue2 != null) {
                                    List asList = Arrays.asList(attributeValue2.split(","));
                                    if (asList.size() > 0) {
                                        arraySet = (Set) asList.stream().filter(new Predicate() { // from class: com.android.server.wm.Task$$ExternalSyntheticLambda25
                                            @Override // java.util.function.Predicate
                                            public final boolean test(Object obj) {
                                                boolean lambda$restoreFromXml$17;
                                                lambda$restoreFromXml$17 = Task.lambda$restoreFromXml$17((String) obj);
                                                return lambda$restoreFromXml$17;
                                            }
                                        }).map(new Function() { // from class: com.android.server.wm.Task$$ExternalSyntheticLambda26
                                            @Override // java.util.function.Function
                                            public final Object apply(Object obj) {
                                                return Integer.valueOf((String) obj);
                                            }
                                        }).collect(Collectors.toSet());
                                    }
                                }
                            } else if (ATTR_OPLUS_FLAGS.equals(attributeName2)) {
                                i19 = Integer.parseInt(attributeValue2);
                            } else {
                                Slog.e(TAG, "restoreFromXml: unknown attribute=" + attributeName2);
                            }
                            i20 = i22;
                            attributeCount2--;
                            typedXmlPullParser2 = typedXmlPullParser;
                            depth = i21;
                        }
                        i5 = depth;
                        i18 = i20;
                    } else {
                        i5 = depth;
                        if (TAG_ACTIVITY.equals(name)) {
                            ActivityRecord restoreFromXml = ActivityRecord.restoreFromXml(typedXmlPullParser, activityTaskSupervisor);
                            if (restoreFromXml != null) {
                                arrayList.add(restoreFromXml);
                            }
                        } else {
                            Slog.e(TAG, "restoreTask: Unexpected name=" + name);
                            XmlUtils.skipCurrentTag(typedXmlPullParser);
                        }
                    }
                    z14 = z;
                    typedXmlPullParser2 = typedXmlPullParser;
                    depth = i5;
                } else {
                    typedXmlPullParser2 = typedXmlPullParser;
                    z14 = z;
                }
            }
        }
        if (!z8) {
            str4 = str3;
        } else if ("@".equals(str4)) {
            str4 = null;
        }
        if (i7 <= 0) {
            Intent intent3 = intent != null ? intent : intent2;
            if (intent3 != null) {
                try {
                    taskDescription = taskDescription2;
                    persistedTaskSnapshotData = persistedTaskSnapshotData2;
                    z2 = z15;
                    i = i9;
                    try {
                        applicationInfo = AppGlobals.getPackageManager().getApplicationInfo(intent3.getComponent().getPackageName(), 8704L, i);
                    } catch (RemoteException unused) {
                    }
                } catch (RemoteException unused2) {
                }
                if (applicationInfo != null) {
                    i2 = applicationInfo.uid;
                    Slog.w(TAG, "Updating task #" + i6 + " for " + intent3 + ": effectiveUid=" + i2);
                }
                i2 = 0;
                Slog.w(TAG, "Updating task #" + i6 + " for " + intent3 + ": effectiveUid=" + i2);
            }
            taskDescription = taskDescription2;
            persistedTaskSnapshotData = persistedTaskSnapshotData2;
            z2 = z15;
            i = i9;
            i2 = 0;
            Slog.w(TAG, "Updating task #" + i6 + " for " + intent3 + ": effectiveUid=" + i2);
        } else {
            taskDescription = taskDescription2;
            persistedTaskSnapshotData = persistedTaskSnapshotData2;
            z2 = z15;
            i = i9;
            i2 = i7;
        }
        if (i17 < 1) {
            if (i8 == 1) {
                i3 = i14;
                if (i3 == 2) {
                    z3 = z13;
                    i3 = 1;
                }
            } else {
                i3 = i14;
            }
            z3 = z13;
        } else {
            i3 = i14;
            if (i3 == 3) {
                i3 = 2;
                z3 = true;
            }
            z3 = z13;
        }
        final Task buildInner = new Builder(activityTaskSupervisor.mService).setTaskId(i6).setIntent(intent).setAffinityIntent(intent2).setAffinity(str3).setRootAffinity(str4).setRealActivity(componentName).setOrigActivity(componentName2).setRootWasReset(z10).setAutoRemoveRecents(z11).setAskedCompatMode(z12).setUserId(i).setEffectiveUid(i2).setLastDescription(str6).setLastTimeMoved(j3).setNeverRelinquishIdentity(z7).setLastTaskDescription(taskDescription).setLastSnapshotData(persistedTaskSnapshotData).setTaskAffiliation(i10).setPrevAffiliateTaskId(i11).setNextAffiliateTaskId(i12).setCallingUid(i13).setCallingPackage(str8).setCallingFeatureId(str7).setResizeMode(i3).setSupportsPictureInPicture(z3).setRealActivitySuspended(z9).setUserSetupComplete(z6).setMinWidth(i15).setMinHeight(i16).buildInner();
        Rect rect2 = rect;
        buildInner.mLastNonFullscreenBounds = rect2;
        buildInner.setBounds(rect2);
        buildInner.mWindowLayoutAffinity = str5;
        if (arrayList.size() > 0) {
            z4 = false;
            activityTaskSupervisor.mRootWindowContainer.getDisplayContent(0).getDefaultTaskDisplayArea().addChild(buildInner, ChargerErrorCode.ERR_FILE_FAILURE_ACCESS);
            z5 = true;
            for (int size = arrayList.size() - 1; size >= 0; size--) {
                buildInner.addChild((ActivityRecord) arrayList.get(size));
            }
        } else {
            z4 = false;
            z5 = true;
        }
        buildInner.getWrapper().getExtImpl().setContainerTask(z2);
        buildInner.getWrapper().getExtImpl().setShowRecent(z);
        buildInner.getWrapper().getExtImpl().setEmbeddedContainerTask(i18);
        arraySet.stream().forEach(new Consumer() { // from class: com.android.server.wm.Task$$ExternalSyntheticLambda27
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                Task.lambda$restoreFromXml$18(Task.this, (Integer) obj);
            }
        });
        if (intent != null && (i4 = i19) != 0) {
            intent.getIntentExt().addOplusFlags(i4);
            buildInner.getWrapper().getExtImpl().setTaskCanvas((intent.getIntentExt().getOplusFlags() & 16384) != 0 ? z5 : z4);
        }
        if (ActivityTaskManagerDebugConfig.DEBUG_RECENTS) {
            Slog.d(TAG_RECENTS, "Restored task=" + buildInner);
        }
        return buildInner;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ boolean lambda$restoreFromXml$17(String str) {
        return (str == null || str.isEmpty()) ? false : true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$restoreFromXml$18(Task task, Integer num) {
        task.getWrapper().getExtImpl().addEmbeddedChildren(num.intValue());
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.android.server.wm.TaskFragment, com.android.server.wm.WindowContainer
    public boolean isOrganized() {
        return this.mTaskOrganizer != null;
    }

    private boolean canBeOrganized() {
        if (isRootTask() || this.mCreatedByOrganizer) {
            return true;
        }
        Task asTask = getParent().asTask();
        return asTask != null && asTask.mCreatedByOrganizer;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.android.server.wm.WindowContainer
    public void reparentSurfaceControl(SurfaceControl.Transaction transaction, SurfaceControl surfaceControl) {
        if (isOrganized() && isAlwaysOnTop()) {
            return;
        }
        super.reparentSurfaceControl(transaction, surfaceControl);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r1v1, types: [com.android.server.wm.WindowContainer] */
    /* JADX WARN: Type inference failed for: r1v3 */
    /* JADX WARN: Type inference failed for: r1v4 */
    public void setHasBeenVisible(boolean z) {
        Task asTask;
        this.mHasBeenVisible = z;
        if (!z || this.mDeferTaskAppear) {
            return;
        }
        sendTaskAppeared();
        ?? r1 = this;
        while (true) {
            WindowContainer parent = r1.getParent();
            if (parent == null || (asTask = parent.asTask()) == null) {
                return;
            }
            asTask.setHasBeenVisible(true);
            r1 = parent;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean getHasBeenVisible() {
        return this.mHasBeenVisible;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setDeferTaskAppear(boolean z) {
        boolean z2 = this.mDeferTaskAppear;
        this.mDeferTaskAppear = z;
        if (!z2 || z) {
            return;
        }
        sendTaskAppeared();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean taskAppearedReady() {
        if (this.mTaskOrganizer == null || this.mDeferTaskAppear) {
            return false;
        }
        if (this.mCreatedByOrganizer) {
            return true;
        }
        return this.mSurfaceControl != null && getHasBeenVisible();
    }

    private void sendTaskAppeared() {
        ITaskOrganizer iTaskOrganizer = this.mTaskOrganizer;
        if (iTaskOrganizer != null) {
            this.mAtmService.mTaskOrganizerController.onTaskAppeared(iTaskOrganizer, this);
        }
    }

    private void sendTaskVanished(ITaskOrganizer iTaskOrganizer) {
        if (iTaskOrganizer != null) {
            this.mAtmService.mTaskOrganizerController.onTaskVanished(iTaskOrganizer, this);
        }
    }

    @VisibleForTesting
    boolean setTaskOrganizer(ITaskOrganizer iTaskOrganizer) {
        return setTaskOrganizer(iTaskOrganizer, false);
    }

    @VisibleForTesting
    boolean setTaskOrganizer(ITaskOrganizer iTaskOrganizer, boolean z) {
        ITaskOrganizer iTaskOrganizer2 = this.mTaskOrganizer;
        if (iTaskOrganizer2 == iTaskOrganizer) {
            return false;
        }
        this.mTaskOrganizer = iTaskOrganizer;
        sendTaskVanished(iTaskOrganizer2);
        if (this.mTaskOrganizer != null) {
            if (z) {
                return true;
            }
            sendTaskAppeared();
            return true;
        }
        TaskDisplayArea displayArea = getDisplayArea();
        if (displayArea != null) {
            displayArea.removeLaunchRootTask(this);
        }
        setForceHidden(2, false);
        if (!this.mCreatedByOrganizer) {
            return true;
        }
        removeImmediately("setTaskOrganizer");
        return true;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean updateTaskOrganizerState() {
        return updateTaskOrganizerState(false);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean updateTaskOrganizerState(boolean z) {
        ITaskOrganizer iTaskOrganizer;
        if (getSurfaceControl() == null) {
            return false;
        }
        if (!canBeOrganized()) {
            return setTaskOrganizer(null);
        }
        ITaskOrganizer taskOrganizer = this.mWmService.mAtmService.mTaskOrganizerController.getTaskOrganizer();
        if (!this.mCreatedByOrganizer || (iTaskOrganizer = this.mTaskOrganizer) == null || taskOrganizer == null || iTaskOrganizer == taskOrganizer) {
            return setTaskOrganizer(taskOrganizer, z);
        }
        return false;
    }

    @Override // com.android.server.wm.TaskFragment, com.android.server.wm.WindowContainer
    void setSurfaceControl(SurfaceControl surfaceControl) {
        super.setSurfaceControl(surfaceControl);
        sendTaskAppeared();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean isFocused() {
        ActivityRecord activityRecord;
        DisplayContent displayContent = this.mDisplayContent;
        if (displayContent == null || (activityRecord = displayContent.mFocusedApp) == null) {
            return false;
        }
        Task task = activityRecord.getTask();
        return task == this || (task != null && task.getParent() == this);
    }

    private boolean hasVisibleChildren() {
        return (!isAttached() || isForceHidden() || getActivity(new Predicate() { // from class: com.android.server.wm.Task$$ExternalSyntheticLambda38
            @Override // java.util.function.Predicate
            public final boolean test(Object obj) {
                return ((ActivityRecord) obj).isVisible();
            }
        }) == null) ? false : true;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void onAppFocusChanged(boolean z) {
        if (z && (!this.mTaskWrapper.getExtImpl().isInPendingAnimation(this) || !this.mTaskWrapper.getExtImpl().isDragZoomMode())) {
            if (this.mTaskWrapper.getExtImpl().shouldSkipLaunchIntoCompactWindowingMode()) {
                this.mTaskWrapper.getExtImpl().skipNextLaunchIntoCompactWindowingMode(false);
            } else {
                Slog.d(TAG, "onWindowFocusChanged " + this + " would transfer to compact");
                this.mTaskWrapper.getExtImpl().launchIntoCompactwindowingMode(this, false);
            }
        }
        dispatchTaskInfoChangedIfNeeded(false);
        Task asTask = getParent().asTask();
        if (asTask != null) {
            asTask.dispatchTaskInfoChangedIfNeeded(false);
        }
        this.mAtmService.getTaskChangeNotificationController().notifyTaskFocusChanged(this.mTaskId, z);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void onPictureInPictureParamsChanged() {
        if (inPinnedWindowingMode()) {
            dispatchTaskInfoChangedIfNeeded(true);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void onShouldDockBigOverlaysChanged() {
        dispatchTaskInfoChangedIfNeeded(true);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void onSizeCompatActivityChanged() {
        dispatchTaskInfoChangedIfNeeded(true);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setMainWindowSizeChangeTransaction(SurfaceControl.Transaction transaction) {
        setMainWindowSizeChangeTransaction(transaction, this);
        forAllWindows(new Consumer() { // from class: com.android.server.wm.Task$$ExternalSyntheticLambda6
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                ((WindowState) obj).requestRedrawForSync();
            }
        }, true);
    }

    private void setMainWindowSizeChangeTransaction(final SurfaceControl.Transaction transaction, Task task) {
        ActivityRecord topNonFinishingActivity = getTopNonFinishingActivity();
        Task task2 = topNonFinishingActivity != null ? topNonFinishingActivity.getTask() : null;
        if (task2 == null) {
            return;
        }
        if (task2 != this) {
            task2.setMainWindowSizeChangeTransaction(transaction, task);
            return;
        }
        WindowState topVisibleAppMainWindow = getTopVisibleAppMainWindow();
        if (topVisibleAppMainWindow != null) {
            topVisibleAppMainWindow.applyWithNextDraw(new Consumer() { // from class: com.android.server.wm.Task$$ExternalSyntheticLambda20
                @Override // java.util.function.Consumer
                public final void accept(Object obj) {
                    ((SurfaceControl.Transaction) obj).merge(transaction);
                }
            });
        } else {
            transaction.apply();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean setForceHidden(int i, boolean z) {
        DisplayContent displayContent;
        ActivityRecord activityRecord;
        int i2 = this.mForceHiddenFlags;
        int i3 = z ? i | i2 : (~i) & i2;
        if (i2 == i3) {
            return false;
        }
        boolean isForceHidden = isForceHidden();
        boolean z2 = isVisible() || (!this.mWmService.mPolicy.isScreenOn() && getWindowingMode() == 6 && (displayContent = this.mDisplayContent) != null && (activityRecord = displayContent.mFocusedApp) != null && activityRecord.getTask() == this);
        this.mForceHiddenFlags = i3;
        boolean isForceHidden2 = isForceHidden();
        if (isForceHidden != isForceHidden2) {
            if (z2 && isForceHidden2) {
                moveToBack("setForceHidden", null);
            } else if (isAlwaysOnTop() || (getWrapper().getExtImpl().isAlwaysOnTop() && !isForceHidden2)) {
                moveToFront("setForceHidden");
            }
        }
        return true;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setForceTranslucent(boolean z) {
        this.mForceTranslucent = z;
    }

    @Override // com.android.server.wm.ConfigurationContainer
    public boolean isAlwaysOnTop() {
        if (getWrapper().getExtImpl().isFlexibleWindowScenario(new int[0])) {
            return !isForceHidden() && getWindowConfiguration().isFlexibleAlwaysOnTop();
        }
        if (isForceHidden()) {
            return false;
        }
        return super.isAlwaysOnTop() || getWrapper().getExtImpl().isForceAlwaysOnTop(this);
    }

    public boolean isAlwaysOnTopWhenVisible() {
        return super.isAlwaysOnTop();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.android.server.wm.TaskFragment
    public boolean isForceHidden() {
        return this.mForceHiddenFlags != 0;
    }

    @Override // com.android.server.wm.TaskFragment
    protected boolean isForceTranslucent() {
        return this.mForceTranslucent;
    }

    @Override // com.android.server.wm.ConfigurationContainer
    public void setWindowingMode(int i) {
        if (!isRootTask()) {
            super.setWindowingMode(i);
        } else {
            setWindowingMode(i, false);
        }
    }

    void setWindowingMode(final int i, final boolean z) {
        this.mWmService.inSurfaceTransaction(new Runnable() { // from class: com.android.server.wm.Task$$ExternalSyntheticLambda43
            @Override // java.lang.Runnable
            public final void run() {
                Task.this.lambda$setWindowingMode$20(i, z);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* renamed from: setWindowingModeInSurfaceTransaction, reason: merged with bridge method [inline-methods] */
    public void lambda$setWindowingMode$20(int i, boolean z) {
        int i2;
        TaskDisplayArea displayArea = getDisplayArea();
        if (displayArea == null) {
            Slog.d(TAG, "taskDisplayArea is null, bail early");
            return;
        }
        int windowingMode = getWindowingMode();
        Task topMostTask = getTopMostTask();
        int i3 = (z || displayArea.isValidWindowingMode(i, null, topMostTask)) ? i : 0;
        if (windowingMode == i3) {
            getRequestedOverrideConfiguration().windowConfiguration.setWindowingMode(i3);
            return;
        }
        ActivityRecord topNonFinishingActivity = getTopNonFinishingActivity();
        if (i3 == 0) {
            WindowContainer parent = getParent();
            i2 = parent != null ? parent.getWindowingMode() : 1;
        } else {
            i2 = i3;
        }
        if (windowingMode == 2) {
            setCanAffectSystemUiFlags(true);
            this.mTaskSupervisor.mUserLeaving = true;
            Task topDisplayFocusedRootTask = this.mRootWindowContainer.getTopDisplayFocusedRootTask();
            if (topDisplayFocusedRootTask != null) {
                ActivityRecord topResumedActivity = topDisplayFocusedRootTask.getTopResumedActivity();
                enableEnterPipOnTaskSwitch(topResumedActivity, null, topResumedActivity, null);
            }
            this.mRootWindowContainer.notifyActivityPipModeChanged(this, null);
        }
        if (i2 == 2 && displayArea.getRootPinnedTask() != null) {
            displayArea.getRootPinnedTask().dismissPip();
        }
        if (i2 != 1 && topNonFinishingActivity != null && !topNonFinishingActivity.noDisplay && i != 6 && !this.mTaskWrapper.getExtImpl().isZoomMode(i) && !this.mTaskWrapper.getExtImpl().isCompactWindowingMode(i) && topNonFinishingActivity.canForceResizeNonResizable(i2)) {
            this.mAtmService.getTaskChangeNotificationController().notifyActivityForcedResizable(topMostTask.mTaskId, 1, topNonFinishingActivity.info.applicationInfo.packageName);
        }
        this.mAtmService.deferWindowLayout();
        if (topNonFinishingActivity != null) {
            try {
                this.mTaskSupervisor.mNoAnimActivities.add(topNonFinishingActivity);
            } finally {
                this.mAtmService.continueWindowLayout();
            }
        }
        super.setWindowingMode(i3);
        this.mTaskWrapper.getExtImpl().onWindowingModeChanged(this, windowingMode);
        if (windowingMode == 2 && topNonFinishingActivity != null) {
            if (topNonFinishingActivity.getLastParentBeforePip() != null && !isForceHidden()) {
                Task lastParentBeforePip = topNonFinishingActivity.getLastParentBeforePip();
                if (lastParentBeforePip.isAttached()) {
                    topNonFinishingActivity.setWindowingMode(0);
                    topNonFinishingActivity.reparent(lastParentBeforePip, lastParentBeforePip.getChildCount(), "movePinnedActivityToOriginalTask");
                    DisplayContent displayContent = topNonFinishingActivity.getDisplayContent();
                    if (displayContent != null && displayContent.isFixedRotationLaunchingApp(topNonFinishingActivity)) {
                        topNonFinishingActivity.getOrCreateFixedRotationLeash(topNonFinishingActivity.getSyncTransaction());
                    }
                    lastParentBeforePip.moveToFront("movePinnedActivityToOriginalTask");
                }
            }
            if (topNonFinishingActivity.shouldBeVisible()) {
                this.mAtmService.resumeAppSwitches();
            }
        }
        this.mTaskWrapper.getExtImpl().notifyZoomModeChanged(getWindowingMode(), windowingMode);
        if (z) {
            return;
        }
        if (topNonFinishingActivity != null && windowingMode == 1 && i3 == 2 && !this.mTransitionController.isShellTransitionsEnabled()) {
            this.mDisplayContent.mPinnedTaskController.deferOrientationChangeForEnteringPipFromFullScreenIfNeeded();
        }
        this.mAtmService.continueWindowLayout();
        if (this.mTaskSupervisor.isRootVisibilityUpdateDeferred()) {
            return;
        }
        this.mRootWindowContainer.ensureActivitiesVisible(null, 0, true);
        this.mRootWindowContainer.resumeFocusedTasksTopActivities();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void abortPipEnter(ActivityRecord activityRecord) {
        if (inPinnedWindowingMode() && !activityRecord.inPinnedWindowingMode() && canMoveTaskToBack(this)) {
            Transition transition = new Transition(4, 0, this.mTransitionController, this.mWmService.mSyncEngine);
            this.mTransitionController.moveToCollecting(transition);
            this.mTransitionController.requestStartTransition(transition, this, null, null);
            if (activityRecord.getLastParentBeforePip() != null) {
                Task lastParentBeforePip = activityRecord.getLastParentBeforePip();
                if (lastParentBeforePip.isAttached()) {
                    activityRecord.reparent(lastParentBeforePip, lastParentBeforePip.getChildCount(), "movePinnedActivityToOriginalTask");
                }
            }
            if (isAttached()) {
                setWindowingMode(0);
                moveTaskToBackInner(this);
            }
            if (activityRecord.isAttached()) {
                activityRecord.setWindowingMode(0);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void resumeNextFocusAfterReparent() {
        adjustFocusToNextFocusableTask("reparent", true, true);
        this.mRootWindowContainer.resumeFocusedTasksTopActivities();
        this.mRootWindowContainer.ensureActivitiesVisible(null, 0, false);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final boolean isOnHomeDisplay() {
        return getDisplayId() == 0;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void moveToFront(String str) {
        moveToFront(str, null);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @VisibleForTesting
    public void moveToFront(String str, Task task) {
        if (isAttached()) {
            this.mTransitionController.recordTaskOrder(this);
            TaskDisplayArea displayArea = getDisplayArea();
            if (!isActivityTypeHome() && returnsToHomeRootTask()) {
                displayArea.moveHomeRootTaskToFront(str + " returnToHome");
            }
            if (getWrapper().getExtImpl().isAlwaysOnTop()) {
                setAlwaysOnTopOnly(true);
            }
            Task focusedRootTask = isRootTask() ? displayArea.getFocusedRootTask() : null;
            if (task == null) {
                task = this;
            }
            this.mAtmService.getWrapper().getFlexibleExtImpl().moveTaskToFront(task, null, str);
            task.getParent().positionChildAt(Integer.MAX_VALUE, task, true);
            displayArea.updateLastFocusedRootTask(focusedRootTask, str);
        }
    }

    void moveToBack(String str, Task task) {
        if (isAttached()) {
            if (getWrapper().getExtImpl().isAlwaysOnTop()) {
                setAlwaysOnTopOnly(false);
            }
            TaskDisplayArea displayArea = getDisplayArea();
            if (this.mCreatedByOrganizer && !this.mTaskWrapper.getExtImpl().isZoomMode(getWindowingMode())) {
                if (task == null || task == this) {
                    return;
                }
                displayArea.positionTaskBehindHome(task);
                return;
            }
            WindowContainer parent = getParent();
            Task asTask = parent != null ? parent.asTask() : null;
            if (asTask != null) {
                asTask.moveToBack(str, this);
            } else {
                Task focusedRootTask = displayArea.getFocusedRootTask();
                displayArea.positionChildAt(ChargerErrorCode.ERR_FILE_FAILURE_ACCESS, this, false);
                displayArea.updateLastFocusedRootTask(focusedRootTask, str);
            }
            if (task == null || task == this) {
                return;
            }
            if (this.mTaskWrapper.getExtImpl().isZoomMode(getWindowingMode())) {
                if (this.mTaskWrapper.getExtImpl().isParentChanged(task.getParent() != null ? task.getParent().asTask() : null, this)) {
                    return;
                }
            }
            positionChildAtBottom(task);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.android.server.wm.WindowContainer
    public void switchUser(int i) {
        if (this.mCurrentUser == i) {
            return;
        }
        this.mCurrentUser = i;
        super.switchUser(i);
        if (isRootTask() || !showToCurrentUser()) {
            return;
        }
        getParent().positionChildAt(Integer.MAX_VALUE, this, false);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void minimalResumeActivityLocked(ActivityRecord activityRecord) {
        if (ProtoLogCache.WM_DEBUG_STATES_enabled) {
            ProtoLogImpl.v(ProtoLogGroup.WM_DEBUG_STATES, -1164930508, 0, (String) null, new Object[]{String.valueOf(activityRecord), String.valueOf(Debug.getCallers(5))});
        }
        activityRecord.setState(ActivityRecord.State.RESUMED, "minimalResumeActivityLocked");
        this.mTaskWrapper.getExtImpl().sendBroadcastResumedActivity(this.mHandler, this.mAtmService.mContext, activityRecord);
        activityRecord.completeResumeLocked();
        this.mAtmService.mSocExt.onAfterActivityResumed(activityRecord);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void checkReadyForSleep() {
        if (shouldSleepActivities() && goToSleepIfPossible(false)) {
            this.mTaskSupervisor.checkReadyForSleepLocked(true);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean goToSleepIfPossible(final boolean z) {
        if (this.mTaskWrapper.mTaskExt.skipGoToSleep()) {
            return true;
        }
        final int[] iArr = {0};
        forAllLeafTasksAndLeafTaskFragments(new Consumer() { // from class: com.android.server.wm.Task$$ExternalSyntheticLambda15
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                Task.lambda$goToSleepIfPossible$21(z, iArr, (TaskFragment) obj);
            }
        }, true);
        return iArr[0] == 0;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$goToSleepIfPossible$21(boolean z, int[] iArr, TaskFragment taskFragment) {
        if (taskFragment.sleepIfPossible(z)) {
            return;
        }
        iArr[0] = iArr[0] + 1;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean isTopRootTaskInDisplayArea() {
        TaskDisplayArea displayArea = getDisplayArea();
        return displayArea != null && displayArea.isTopRootTask(this);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean isFocusedRootTaskOnDisplay() {
        DisplayContent displayContent = this.mDisplayContent;
        return displayContent != null && this == displayContent.getFocusedRootTask();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void ensureActivitiesVisible(ActivityRecord activityRecord, int i, boolean z) {
        ensureActivitiesVisible(activityRecord, i, z, true);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void ensureActivitiesVisible(final ActivityRecord activityRecord, final int i, final boolean z, final boolean z2) {
        this.mTaskSupervisor.beginActivityVisibilityUpdate();
        try {
            forAllLeafTasks(new Consumer() { // from class: com.android.server.wm.Task$$ExternalSyntheticLambda30
                @Override // java.util.function.Consumer
                public final void accept(Object obj) {
                    ((Task) obj).updateActivityVisibilities(ActivityRecord.this, i, z, z2);
                }
            }, true);
            if (this.mTranslucentActivityWaiting != null && this.mUndrawnActivitiesBelowTopTranslucent.isEmpty()) {
                notifyActivityDrawnLocked(null);
            }
        } finally {
            this.mTaskSupervisor.endActivityVisibilityUpdate();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void checkTranslucentActivityWaiting(ActivityRecord activityRecord) {
        if (this.mTranslucentActivityWaiting != activityRecord) {
            this.mUndrawnActivitiesBelowTopTranslucent.clear();
            if (this.mTranslucentActivityWaiting != null) {
                notifyActivityDrawnLocked(null);
                this.mTranslucentActivityWaiting = null;
            }
            this.mHandler.removeMessages(101);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void convertActivityToTranslucent(ActivityRecord activityRecord) {
        this.mTranslucentActivityWaiting = activityRecord;
        this.mUndrawnActivitiesBelowTopTranslucent.clear();
        this.mHandler.sendEmptyMessageDelayed(101, TRANSLUCENT_CONVERSION_TIMEOUT);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void notifyActivityDrawnLocked(ActivityRecord activityRecord) {
        if (activityRecord == null || (this.mUndrawnActivitiesBelowTopTranslucent.remove(activityRecord) && this.mUndrawnActivitiesBelowTopTranslucent.isEmpty())) {
            ActivityRecord activityRecord2 = this.mTranslucentActivityWaiting;
            this.mTranslucentActivityWaiting = null;
            this.mUndrawnActivitiesBelowTopTranslucent.clear();
            this.mHandler.removeMessages(101);
            if (activityRecord2 != null) {
                this.mWmService.setWindowOpaqueLocked(activityRecord2.token, false);
                if (activityRecord2.attachedToProcess()) {
                    try {
                        activityRecord2.app.getThread().scheduleTranslucentConversionComplete(activityRecord2.token, activityRecord != null);
                    } catch (RemoteException unused) {
                    }
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy({"mService"})
    public boolean resumeTopActivityUncheckedLocked(ActivityRecord activityRecord, ActivityOptions activityOptions, boolean z) {
        boolean z2;
        if (this.mInResumeTopActivity) {
            return false;
        }
        if (this.mTaskWrapper.getExtImpl().ignoreResumePuttTask(getRootTask())) {
            Slog.v(TAG, "putt ignore resume top: " + activityRecord);
            return false;
        }
        try {
            this.mInResumeTopActivity = true;
            if (isLeafTask()) {
                z2 = isFocusableAndVisible() ? resumeTopActivityInnerLocked(activityRecord, activityOptions, z) : false;
            } else {
                int size = this.mChildren.size() - 1;
                boolean z3 = false;
                while (size >= 0) {
                    int i = size - 1;
                    Task task = (Task) getChildAt(size);
                    if (task.isTopActivityFocusable()) {
                        if (task.getVisibility(null) != 0) {
                            if (task.topRunningActivity() != null) {
                                break;
                            }
                        } else {
                            z3 |= task.resumeTopActivityUncheckedLocked(activityRecord, activityOptions, z);
                            if (i >= this.mChildren.size()) {
                                size = this.mChildren.size() - 1;
                            }
                        }
                    }
                    size = i;
                }
                z2 = z3;
            }
            ActivityRecord activityRecord2 = topRunningActivity(true);
            if (activityRecord2 == null || !activityRecord2.canTurnScreenOn()) {
                checkReadyForSleep();
            }
            return z2;
        } finally {
            this.mInResumeTopActivity = false;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy({"mService"})
    public boolean resumeTopActivityUncheckedLocked(ActivityRecord activityRecord, ActivityOptions activityOptions) {
        return resumeTopActivityUncheckedLocked(activityRecord, activityOptions, false);
    }

    @GuardedBy({"mService"})
    private boolean resumeTopActivityInnerLocked(final ActivityRecord activityRecord, final ActivityOptions activityOptions, final boolean z) {
        if (!this.mAtmService.isBooting() && !this.mAtmService.isBooted()) {
            return false;
        }
        ActivityRecord activityRecord2 = topRunningActivity(true);
        if (activityRecord2 == null) {
            return resumeNextFocusableActivityWhenRootTaskIsEmpty(activityRecord, activityOptions);
        }
        final boolean[] zArr = new boolean[1];
        if (getWrapper().getExtImpl().resumeTopActivityInnerInCompactWindow(zArr, activityRecord, activityOptions, z)) {
            return zArr[0];
        }
        final TaskFragment taskFragment = activityRecord2.getTaskFragment();
        zArr[0] = taskFragment.resumeTopActivity(activityRecord, activityOptions, z);
        forAllLeafTaskFragments(new Consumer() { // from class: com.android.server.wm.Task$$ExternalSyntheticLambda36
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                Task.lambda$resumeTopActivityInnerLocked$23(TaskFragment.this, zArr, activityRecord, activityOptions, z, (TaskFragment) obj);
            }
        }, true);
        return zArr[0];
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$resumeTopActivityInnerLocked$23(TaskFragment taskFragment, boolean[] zArr, ActivityRecord activityRecord, ActivityOptions activityOptions, boolean z, TaskFragment taskFragment2) {
        if (taskFragment != taskFragment2 && taskFragment2.canBeResumed(null)) {
            zArr[0] = taskFragment2.resumeTopActivity(activityRecord, activityOptions, z) | zArr[0];
        }
    }

    private boolean resumeNextFocusableActivityWhenRootTaskIsEmpty(ActivityRecord activityRecord, ActivityOptions activityOptions) {
        Task adjustFocusToNextFocusableTask;
        if (!isActivityTypeHome() && (adjustFocusToNextFocusableTask = adjustFocusToNextFocusableTask("noMoreActivities")) != null) {
            return this.mRootWindowContainer.resumeFocusedTasksTopActivities(adjustFocusToNextFocusableTask, activityRecord, null);
        }
        ActivityOptions.abort(activityOptions);
        if (ProtoLogCache.WM_DEBUG_STATES_enabled) {
            ProtoLogImpl.d(ProtoLogGroup.WM_DEBUG_STATES, -143556958, 0, (String) null, new Object[]{"noMoreActivities"});
        }
        return this.mRootWindowContainer.resumeHomeActivity(activityRecord, "noMoreActivities", getDisplayArea());
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: Code restructure failed: missing block: B:56:0x011e, code lost:
    
        if (topRunningNonDelayedActivityLocked(null) == r10) goto L69;
     */
    /* JADX WARN: Code restructure failed: missing block: B:57:0x012a, code lost:
    
        r11 = false;
     */
    /* JADX WARN: Code restructure failed: missing block: B:81:0x0128, code lost:
    
        if (r14.getAnimationType() == 5) goto L68;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void startActivityLocked(ActivityRecord activityRecord, Task task, boolean z, boolean z2, ActivityOptions activityOptions, ActivityRecord activityRecord2) {
        boolean z3;
        ActivityRecord findEnterPipOnTaskSwitchCandidate = findEnterPipOnTaskSwitchCandidate(task);
        Task task2 = activityRecord.getTask();
        boolean z4 = activityOptions == null || !activityOptions.getAvoidMoveToFront();
        boolean z5 = task2 == this || hasChild(task2);
        if (!activityRecord.mLaunchTaskBehind && z4 && (!z5 || z)) {
            positionChildAtTop(task2);
        }
        if (!z && z5 && !activityRecord.shouldBeVisible()) {
            ActivityOptions.abort(activityOptions);
            return;
        }
        Task task3 = activityRecord.getTask();
        if (task3 == null && this.mChildren.indexOf(null) != getChildCount() - 1) {
            this.mTaskSupervisor.mUserLeaving = false;
            if (ActivityTaskManagerDebugConfig.DEBUG_USER_LEAVING) {
                Slog.v(TAG_USER_LEAVING, "startActivity() behind front, mUserLeaving=false");
            }
        }
        if (ProtoLogCache.WM_DEBUG_ADD_REMOVE_enabled) {
            ProtoLogImpl.i(ProtoLogGroup.WM_DEBUG_ADD_REMOVE, -1699018375, 0, (String) null, new Object[]{String.valueOf(activityRecord), String.valueOf(task3), String.valueOf(new RuntimeException("here").fillInStackTrace())});
        }
        if (isActivityTypeHomeOrRecents() && getActivityBelow(activityRecord) == null) {
            ActivityOptions.abort(activityOptions);
            return;
        }
        if (!z4) {
            ActivityOptions.abort(activityOptions);
            return;
        }
        DisplayContent displayContent = this.mDisplayContent;
        if (ActivityTaskManagerDebugConfig.DEBUG_TRANSITION) {
            Slog.v(TAG_TRANSITION, "Prepare open transition: starting " + activityRecord);
        }
        if ((activityRecord.intent.getFlags() & 65536) != 0) {
            displayContent.prepareAppTransition(0);
            this.mTaskSupervisor.mNoAnimActivities.add(activityRecord);
            this.mTransitionController.setNoAnimation(activityRecord);
        } else {
            displayContent.prepareAppTransition(1);
            this.mTaskSupervisor.mNoAnimActivities.remove(activityRecord);
            if (!activityRecord.getWrapper().getExtImpl().shouldSkipAppTransitionWhenStarting()) {
                displayContent.prepareAppTransition(1);
                this.mTaskSupervisor.mNoAnimActivities.remove(activityRecord);
                getWrapper().getExtImpl().excuteAppTransitionForCompactWindowIfNeed(activityRecord, this);
            }
        }
        if (z && !activityRecord.mLaunchTaskBehind) {
            enableEnterPipOnTaskSwitch(findEnterPipOnTaskSwitchCandidate, null, activityRecord, activityOptions);
        }
        if (z) {
            if ((activityRecord.intent.getFlags() & 2097152) != 0) {
                resetTaskIfNeeded(activityRecord, activityRecord);
            }
            z3 = true;
        } else {
            if (activityOptions != null) {
            }
            z3 = true;
        }
        if (activityOptions != null && activityOptions.getDisableStartingWindow()) {
            z3 = false;
        }
        if (activityRecord.mLaunchTaskBehind) {
            activityRecord.setVisibility(true);
            ensureActivitiesVisible(null, 0, false);
            if (!activityRecord.isVisibleRequested()) {
                activityRecord.notifyUnknownVisibilityLaunchedForKeyguardTransition();
            }
            this.mDisplayContent.executeAppTransition();
            return;
        }
        if (z3) {
            if (activityOptions != null && activityOptions.getExtraNoAnimation()) {
                Slog.w(TAG, "not need startingWindow when set KEY_ACTIVITY_NO_ANIM");
            } else {
                this.mWmService.mStartingSurfaceController.showStartingWindow(activityRecord, activityRecord.getTask().getActivity(new Predicate() { // from class: com.android.server.wm.Task$$ExternalSyntheticLambda2
                    @Override // java.util.function.Predicate
                    public final boolean test(Object obj) {
                        boolean lambda$startActivityLocked$24;
                        lambda$startActivityLocked$24 = Task.lambda$startActivityLocked$24((ActivityRecord) obj);
                        return lambda$startActivityLocked$24;
                    }
                }), z, z2, activityRecord2);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ boolean lambda$startActivityLocked$24(ActivityRecord activityRecord) {
        return activityRecord.mStartingData != null && activityRecord.showToCurrentUser();
    }

    static ActivityRecord findEnterPipOnTaskSwitchCandidate(Task task) {
        if (task == null) {
            return null;
        }
        final ActivityRecord[] activityRecordArr = new ActivityRecord[1];
        task.forAllLeafTaskFragments(new Predicate() { // from class: com.android.server.wm.Task$$ExternalSyntheticLambda24
            @Override // java.util.function.Predicate
            public final boolean test(Object obj) {
                boolean lambda$findEnterPipOnTaskSwitchCandidate$25;
                lambda$findEnterPipOnTaskSwitchCandidate$25 = Task.lambda$findEnterPipOnTaskSwitchCandidate$25(activityRecordArr, (TaskFragment) obj);
                return lambda$findEnterPipOnTaskSwitchCandidate$25;
            }
        });
        return activityRecordArr[0];
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ boolean lambda$findEnterPipOnTaskSwitchCandidate$25(ActivityRecord[] activityRecordArr, TaskFragment taskFragment) {
        ActivityRecord topNonFinishingActivity = taskFragment.getTopNonFinishingActivity();
        if (topNonFinishingActivity == null || !topNonFinishingActivity.isState(ActivityRecord.State.RESUMED, ActivityRecord.State.PAUSING) || !topNonFinishingActivity.supportsPictureInPicture()) {
            return false;
        }
        activityRecordArr[0] = topNonFinishingActivity;
        return true;
    }

    private static void enableEnterPipOnTaskSwitch(ActivityRecord activityRecord, Task task, ActivityRecord activityRecord2, ActivityOptions activityOptions) {
        if (activityRecord == null) {
            return;
        }
        if ((activityOptions == null || !activityOptions.disallowEnterPictureInPictureWhileLaunching()) && !activityRecord.inPinnedWindowingMode()) {
            boolean z = activityOptions != null && activityOptions.getTransientLaunch();
            Task rootTask = task != null ? task.getRootTask() : activityRecord2.getRootTask();
            if (rootTask == null || !(rootTask.isActivityTypeAssistant() || z)) {
                activityRecord.supportsEnterPipOnTaskSwitch = true;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public ActivityRecord resetTaskIfNeeded(ActivityRecord activityRecord, ActivityRecord activityRecord2) {
        ActivityRecord topNonFinishingActivity;
        boolean z = (activityRecord2.info.flags & 4) != 0;
        Task task = activityRecord.getTask();
        this.mReuseTask = true;
        try {
            ActivityOptions process = sResetTargetTaskHelper.process(task, z);
            this.mReuseTask = false;
            if ((this.mChildren.contains(task) || this == task) && (topNonFinishingActivity = task.getTopNonFinishingActivity()) != null) {
                activityRecord = topNonFinishingActivity;
            }
            if (process != null) {
                activityRecord.updateOptionsLocked(process);
            }
            return activityRecord;
        } catch (Throwable th) {
            this.mReuseTask = false;
            throw th;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final Task finishTopCrashedActivityLocked(WindowProcessController windowProcessController, String str) {
        ActivityRecord activityRecord = topRunningActivity();
        if (activityRecord == null || activityRecord.app != windowProcessController) {
            return null;
        }
        if (activityRecord.isActivityTypeHome() && this.mAtmService.mHomeProcess == windowProcessController) {
            Slog.w(TAG, "  Not force finishing home activity " + activityRecord.intent.getComponent().flattenToShortString());
            return null;
        }
        Slog.w(TAG, "  Force finishing activity " + activityRecord.intent.getComponent().flattenToShortString());
        Task task = activityRecord.getTask();
        this.mDisplayContent.requestTransitionAndLegacyPrepare(2, 16);
        activityRecord.finishIfPossible(str, false);
        ActivityRecord activityBelow = getActivityBelow(activityRecord);
        if (activityBelow != null && activityBelow.isState(ActivityRecord.State.STARTED, ActivityRecord.State.RESUMED, ActivityRecord.State.PAUSING, ActivityRecord.State.PAUSED) && (!activityBelow.isActivityTypeHome() || this.mAtmService.mHomeProcess != activityBelow.app)) {
            Slog.w(TAG, "  Force finishing activity " + activityBelow.intent.getComponent().flattenToShortString());
            activityBelow.finishIfPossible(str, false);
        }
        this.mTaskWrapper.getExtImpl().onTaskTopActivityCrashed(this);
        return task;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void finishIfVoiceTask(IBinder iBinder) {
        IVoiceInteractionSession iVoiceInteractionSession = this.voiceSession;
        if (iVoiceInteractionSession != null && iVoiceInteractionSession.asBinder() == iBinder) {
            forAllActivities(new Consumer() { // from class: com.android.server.wm.Task$$ExternalSyntheticLambda21
                @Override // java.util.function.Consumer
                public final void accept(Object obj) {
                    Task.this.lambda$finishIfVoiceTask$26((ActivityRecord) obj);
                }
            });
            return;
        }
        PooledPredicate obtainPredicate = PooledLambda.obtainPredicate(new BiPredicate() { // from class: com.android.server.wm.Task$$ExternalSyntheticLambda22
            @Override // java.util.function.BiPredicate
            public final boolean test(Object obj, Object obj2) {
                boolean finishIfVoiceActivity;
                finishIfVoiceActivity = Task.finishIfVoiceActivity((ActivityRecord) obj, (IBinder) obj2);
                return finishIfVoiceActivity;
            }
        }, PooledLambda.__(ActivityRecord.class), iBinder);
        forAllActivities((Predicate<ActivityRecord>) obtainPredicate);
        obtainPredicate.recycle();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$finishIfVoiceTask$26(ActivityRecord activityRecord) {
        if (activityRecord.finishing) {
            return;
        }
        activityRecord.finishIfPossible("finish-voice", false);
        this.mAtmService.updateOomAdj();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static boolean finishIfVoiceActivity(ActivityRecord activityRecord, IBinder iBinder) {
        IVoiceInteractionSession iVoiceInteractionSession = activityRecord.voiceSession;
        if (iVoiceInteractionSession == null || iVoiceInteractionSession.asBinder() != iBinder) {
            return false;
        }
        activityRecord.clearVoiceSessionLocked();
        try {
            activityRecord.app.getThread().scheduleLocalVoiceInteractionStarted(activityRecord.token, (IVoiceInteractor) null);
        } catch (RemoteException unused) {
        }
        activityRecord.mAtmService.finishRunningVoiceLocked();
        return true;
    }

    private boolean inFrontOfStandardRootTask() {
        TaskDisplayArea displayArea = getDisplayArea();
        if (displayArea == null) {
            return false;
        }
        final boolean[] zArr = new boolean[1];
        Task rootTask = displayArea.getRootTask(new Predicate() { // from class: com.android.server.wm.Task$$ExternalSyntheticLambda23
            @Override // java.util.function.Predicate
            public final boolean test(Object obj) {
                boolean lambda$inFrontOfStandardRootTask$27;
                lambda$inFrontOfStandardRootTask$27 = Task.this.lambda$inFrontOfStandardRootTask$27(zArr, (Task) obj);
                return lambda$inFrontOfStandardRootTask$27;
            }
        });
        return rootTask != null && rootTask.isActivityTypeStandard();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ boolean lambda$inFrontOfStandardRootTask$27(boolean[] zArr, Task task) {
        if (zArr[0]) {
            return true;
        }
        if (task == this) {
            zArr[0] = true;
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean shouldUpRecreateTaskLocked(ActivityRecord activityRecord, String str) {
        String computeTaskAffinity = ActivityRecord.computeTaskAffinity(str, activityRecord.getUid(), activityRecord.launchMode, activityRecord.mActivityComponent);
        if (activityRecord.getTask().affinity == null || !activityRecord.getTask().affinity.equals(computeTaskAffinity)) {
            return true;
        }
        Task task = activityRecord.getTask();
        if (activityRecord.isRootOfTask() && task.getBaseIntent() != null && task.getBaseIntent().isDocument()) {
            if (!inFrontOfStandardRootTask()) {
                return true;
            }
            Task taskBelow = getTaskBelow(task);
            if (taskBelow == null) {
                Slog.w(TAG, "shouldUpRecreateTask: task not in history for " + activityRecord);
                return false;
            }
            if (!task.affinity.equals(taskBelow.affinity)) {
                return true;
            }
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean navigateUpTo(ActivityRecord activityRecord, Intent intent, String str, NeededUriGrants neededUriGrants, int i, Intent intent2, NeededUriGrants neededUriGrants2) {
        boolean z;
        ActivityRecord activityRecord2;
        boolean z2;
        ActivityRecord activity;
        if (!activityRecord.attachedToProcess()) {
            return false;
        }
        Task task = activityRecord.getTask();
        if (!activityRecord.isDescendantOf(this)) {
            return false;
        }
        final ActivityRecord activityBelow = task.getActivityBelow(activityRecord);
        final ComponentName component = intent.getComponent();
        if (task.getBottomMostActivity() == activityRecord || component == null || (activity = task.getActivity(new Predicate() { // from class: com.android.server.wm.Task$$ExternalSyntheticLambda8
            @Override // java.util.function.Predicate
            public final boolean test(Object obj) {
                boolean lambda$navigateUpTo$28;
                lambda$navigateUpTo$28 = Task.lambda$navigateUpTo$28(component, (ActivityRecord) obj);
                return lambda$navigateUpTo$28;
            }
        }, activityRecord, false, true)) == null) {
            z = false;
        } else {
            activityBelow = activity;
            z = true;
        }
        IActivityController iActivityController = this.mAtmService.mController;
        if (iActivityController != null && (activityRecord2 = topRunningActivity(activityRecord.token, -1)) != null) {
            try {
                z2 = iActivityController.activityResuming(activityRecord2.packageName);
            } catch (RemoteException unused) {
                this.mAtmService.mController = null;
                Watchdog.getInstance().setActivityController((IActivityController) null);
                z2 = true;
            }
            if (!z2) {
                return false;
            }
        }
        long clearCallingIdentity = Binder.clearCallingIdentity();
        final int[] iArr = {i};
        final Intent[] intentArr = {intent2};
        final NeededUriGrants[] neededUriGrantsArr = {neededUriGrants2};
        task.forAllActivities(new Predicate() { // from class: com.android.server.wm.Task$$ExternalSyntheticLambda9
            @Override // java.util.function.Predicate
            public final boolean test(Object obj) {
                boolean lambda$navigateUpTo$29;
                lambda$navigateUpTo$29 = Task.lambda$navigateUpTo$29(ActivityRecord.this, iArr, intentArr, neededUriGrantsArr, (ActivityRecord) obj);
                return lambda$navigateUpTo$29;
            }
        }, activityRecord, true, true);
        int i2 = iArr[0];
        Intent intent3 = intentArr[0];
        if (activityBelow != null && z) {
            int i3 = activityRecord.info.applicationInfo.uid;
            int execute = this.mAtmService.getActivityStartController().obtainStarter(intent, "navigateUpTo").setResolvedType(str).setUserId(activityRecord.mUserId).setCaller(activityRecord.app.getThread()).setResultTo(activityBelow.token).setIntentGrants(neededUriGrants).setCallingPid(-1).setCallingUid(i3).setCallingPackage(activityRecord.packageName).setCallingFeatureId(activityBelow.launchedFromFeatureId).setRealCallingPid(-1).setRealCallingUid(i3).setComponentSpecified(true).execute();
            z = ActivityManager.isStartResultSuccessful(execute);
            if (execute == 0) {
                activityBelow.finishIfPossible(i2, intent3, neededUriGrants2, "navigate-top", true);
            }
        }
        Binder.restoreCallingIdentity(clearCallingIdentity);
        return z;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ boolean lambda$navigateUpTo$28(ComponentName componentName, ActivityRecord activityRecord) {
        return activityRecord.info.packageName.equals(componentName.getPackageName()) && activityRecord.info.name.equals(componentName.getClassName());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ boolean lambda$navigateUpTo$29(ActivityRecord activityRecord, int[] iArr, Intent[] intentArr, NeededUriGrants[] neededUriGrantsArr, ActivityRecord activityRecord2) {
        if (activityRecord2 == activityRecord) {
            return true;
        }
        activityRecord2.finishIfPossible(iArr[0], intentArr[0], neededUriGrantsArr[0], "navigate-up", true);
        iArr[0] = 0;
        intentArr[0] = null;
        return false;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void removeLaunchTickMessages() {
        forAllActivities(new Consumer() { // from class: com.android.server.wm.Task$$ExternalSyntheticLambda3
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                ((ActivityRecord) obj).removeLaunchTickRunnable();
            }
        });
    }

    private void updateTransitLocked(int i, ActivityOptions activityOptions) {
        if (activityOptions != null) {
            ActivityRecord activityRecord = topRunningActivity();
            if (activityRecord != null && (!activityRecord.isState(ActivityRecord.State.RESUMED) || this.mTaskWrapper.getExtImpl().shouldUpdateTransitLocked(activityRecord, i, activityOptions))) {
                activityRecord.updateOptionsLocked(activityOptions);
            } else {
                ActivityOptions.abort(activityOptions);
            }
        }
        this.mDisplayContent.prepareAppTransition(i);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final void moveTaskToFront(Task task, boolean z, ActivityOptions activityOptions, AppTimeTracker appTimeTracker, String str) {
        moveTaskToFront(task, z, activityOptions, appTimeTracker, false, str);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: Removed duplicated region for block: B:49:0x0122 A[Catch: all -> 0x0146, TRY_LEAVE, TryCatch #0 {all -> 0x0146, blocks: (B:24:0x008c, B:26:0x0097, B:29:0x009f, B:31:0x00ad, B:32:0x00b0, B:34:0x00b4, B:36:0x00cc, B:39:0x00d9, B:41:0x00e7, B:43:0x0103, B:45:0x0113, B:49:0x0122, B:52:0x011c, B:53:0x00ed, B:54:0x00f1, B:55:0x012d, B:57:0x0132, B:58:0x013d), top: B:23:0x008c }] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public final void moveTaskToFront(Task task, boolean z, ActivityOptions activityOptions, final AppTimeTracker appTimeTracker, boolean z2, String str) {
        if (ActivityTaskManagerDebugConfig.DEBUG_SWITCH) {
            Slog.v(TAG_SWITCH, "moveTaskToFront: " + task);
        }
        this.mTaskWrapper.getExtImpl().moveTaskToFront(activityOptions, task);
        this.mTaskWrapper.getExtImpl().launchIntoCompactwindowingMode(this, true);
        ActivityRecord findEnterPipOnTaskSwitchCandidate = findEnterPipOnTaskSwitchCandidate(getDisplayArea().getTopRootTask());
        if (findEnterPipOnTaskSwitchCandidate == null && getDisplayArea().getTopRootTask() != null && getDisplayArea().getTopRootTask().getWrapper().getExtImpl().isTaskEmbedded()) {
            findEnterPipOnTaskSwitchCandidate = getWrapper().getExtImpl().findEnterPipOnTaskSwitchCandidateForPs(getDisplayArea().getTopRootTask());
        }
        if (task != this && !task.isDescendantOf(this)) {
            if (z) {
                ActivityOptions.abort(activityOptions);
                return;
            } else {
                updateTransitLocked(3, activityOptions);
                return;
            }
        }
        if (appTimeTracker != null) {
            task.forAllActivities(new Consumer() { // from class: com.android.server.wm.Task$$ExternalSyntheticLambda42
                @Override // java.util.function.Consumer
                public final void accept(Object obj) {
                    ((ActivityRecord) obj).appTimeTracker = appTimeTracker;
                }
            });
        }
        try {
            this.mDisplayContent.deferUpdateImeTarget();
            ActivityRecord topNonFinishingActivity = task.getTopNonFinishingActivity();
            if (topNonFinishingActivity != null && topNonFinishingActivity.showToCurrentUser()) {
                if (!getWrapper().getExtImpl().isAvoidMoveTaskToFront(activityOptions)) {
                    topNonFinishingActivity.moveFocusableActivityToTop(str);
                }
                if (ActivityTaskManagerDebugConfig.DEBUG_TRANSITION) {
                    Slog.v(TAG_TRANSITION, "Prepare to front transition: task=" + task);
                }
                if (!z && !task.mTaskWrapper.getExtImpl().isTaskEmbedded()) {
                    if (this.mTaskWrapper.getExtImpl().shouldDoPuttTransition(this.mTaskId)) {
                        updateTransitLocked(100, activityOptions);
                    } else {
                        updateTransitLocked(3, activityOptions);
                    }
                    if (findEnterPipOnTaskSwitchCandidate != null || !this.mTaskWrapper.getExtImpl().isZoomMode(findEnterPipOnTaskSwitchCandidate.getWindowingMode()) || !findEnterPipOnTaskSwitchCandidate.isState(ActivityRecord.State.RESUMED)) {
                        enableEnterPipOnTaskSwitch(findEnterPipOnTaskSwitchCandidate, task, null, activityOptions);
                    }
                    if (!z2) {
                        this.mRootWindowContainer.resumeFocusedTasksTopActivities();
                    }
                    return;
                }
                this.mDisplayContent.prepareAppTransition(0);
                this.mTaskSupervisor.mNoAnimActivities.add(topNonFinishingActivity);
                ActivityOptions.abort(activityOptions);
                if (findEnterPipOnTaskSwitchCandidate != null) {
                }
                enableEnterPipOnTaskSwitch(findEnterPipOnTaskSwitchCandidate, task, null, activityOptions);
                if (!z2) {
                }
                return;
            }
            positionChildAtTop(task);
            if (topNonFinishingActivity != null) {
                this.mTaskSupervisor.mRecentTasks.add(topNonFinishingActivity.getTask());
            }
            ActivityOptions.abort(activityOptions);
        } finally {
            this.mDisplayContent.continueUpdateImeTarget();
        }
    }

    private boolean canMoveTaskToBack(Task task) {
        boolean z;
        if (!this.mAtmService.getLockTaskController().canMoveTaskToBack(task)) {
            return false;
        }
        if (isTopRootTaskInDisplayArea() && this.mAtmService.mController != null) {
            ActivityRecord activityRecord = topRunningActivity(null, task.mTaskId);
            if (activityRecord == null) {
                activityRecord = topRunningActivity(null, -1);
            }
            if (activityRecord != null) {
                try {
                    z = this.mAtmService.mController.activityResuming(activityRecord.packageName);
                } catch (RemoteException unused) {
                    this.mAtmService.mController = null;
                    Watchdog.getInstance().setActivityController((IActivityController) null);
                    z = true;
                }
                if (!z) {
                    return false;
                }
            }
        }
        return true;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean moveTaskToBack(final Task task) {
        Slog.i(TAG, "moveTaskToBack: " + task);
        if (!canMoveTaskToBack(task)) {
            return false;
        }
        this.mTaskWrapper.getExtImpl().startFreezingDisplay(task, this.mAtmService);
        if (ActivityTaskManagerDebugConfig.DEBUG_TRANSITION) {
            Slog.v(TAG_TRANSITION, "Prepare to back transition: task=" + task.mTaskId);
        }
        if (this.mTransitionController.isShellTransitionsEnabled() && !this.mAtmService.getWrapper().getExtImpl().withNoneTransition(null, task, null, 4, "moveTaskToBack")) {
            final Transition transition = new Transition(4, 0, this.mTransitionController, this.mWmService.mSyncEngine);
            this.mTransitionController.startCollectOrQueue(transition, new TransitionController.OnStartCollect() { // from class: com.android.server.wm.Task$$ExternalSyntheticLambda39
                @Override // com.android.server.wm.TransitionController.OnStartCollect
                public final void onCollectStarted(boolean z) {
                    Task.this.lambda$moveTaskToBack$31(task, transition, z);
                }
            });
            return true;
        }
        if (!inPinnedWindowingMode()) {
            this.mDisplayContent.prepareAppTransition(4);
        }
        moveTaskToBackInner(task);
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$moveTaskToBack$31(Task task, Transition transition, boolean z) {
        if (!isAttached() || (z && !canMoveTaskToBack(task))) {
            Slog.e(TAG, "Failed to move task to back after saying we could: " + task.mTaskId);
            transition.abort();
            return;
        }
        this.mTransitionController.requestStartTransition(transition, task, null, null);
        this.mTransitionController.collect(task);
        moveTaskToBackInner(task);
    }

    private boolean moveTaskToBackInner(Task task) {
        if (this.mTransitionController.isShellTransitionsEnabled()) {
            this.mAtmService.deferWindowLayout();
        }
        try {
            moveToBack("moveTaskToBackInner", task);
            this.mDisplayContent.getWrapper().getExtImpl().setAnimationThreadUx(true, false, 1);
            if (task.getWrapper().getExtImpl().isAlwaysOnTop()) {
                task.setAlwaysOnTopOnly(false);
            }
            this.mTaskWrapper.getExtImpl().moveTaskToBack(task, getDisplayContent().getFocusedRootTask());
            if (inPinnedWindowingMode()) {
                this.mTaskSupervisor.removeRootTask(this);
                return true;
            }
            this.mRootWindowContainer.ensureVisibilityAndConfig(null, this.mDisplayContent.mDisplayId, false, false);
            if (this.mTransitionController.isShellTransitionsEnabled()) {
                this.mAtmService.continueWindowLayout();
            }
            ActivityRecord activityRecord = getDisplayArea().topRunningActivity();
            Task rootTask = activityRecord != null ? activityRecord.getRootTask() : null;
            if (rootTask != null && rootTask != this && activityRecord.isState(ActivityRecord.State.RESUMED)) {
                this.mDisplayContent.executeAppTransition();
                this.mDisplayContent.setFocusedApp(activityRecord);
            } else {
                this.mRootWindowContainer.resumeFocusedTasksTopActivities();
            }
            return true;
        } finally {
            if (this.mTransitionController.isShellTransitionsEnabled()) {
                this.mAtmService.continueWindowLayout();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean willActivityBeVisible(IBinder iBinder) {
        ActivityRecord forTokenLocked = ActivityRecord.forTokenLocked(iBinder);
        if (forTokenLocked == null || !forTokenLocked.shouldBeVisible()) {
            return false;
        }
        if (forTokenLocked.finishing) {
            Slog.e(TAG, "willActivityBeVisible: Returning false, would have returned true for r=" + forTokenLocked);
        }
        return !forTokenLocked.finishing;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void unhandledBackLocked() {
        ActivityRecord topMostActivity = getTopMostActivity();
        if (ActivityTaskManagerDebugConfig.DEBUG_SWITCH) {
            Slog.d(TAG_SWITCH, "Performing unhandledBack(): top activity: " + topMostActivity);
        }
        if (topMostActivity != null) {
            topMostActivity.finishIfPossible("unhandled-back", true);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean dump(FileDescriptor fileDescriptor, PrintWriter printWriter, boolean z, boolean z2, String str, boolean z3) {
        return dump("  ", fileDescriptor, printWriter, z, z2, str, z3, null);
    }

    @Override // com.android.server.wm.TaskFragment
    void dumpInner(String str, PrintWriter printWriter, boolean z, String str2) {
        super.dumpInner(str, printWriter, z, str2);
        if (this.mCreatedByOrganizer) {
            printWriter.println(str + "  mCreatedByOrganizer=true");
        }
        if (this.mLastNonFullscreenBounds != null) {
            printWriter.print(str);
            printWriter.print("  mLastNonFullscreenBounds=");
            printWriter.println(this.mLastNonFullscreenBounds);
        }
        if (isLeafTask()) {
            printWriter.println(str + "  isSleeping=" + shouldSleepActivities());
            ActivityTaskSupervisor.printThisActivity(printWriter, getTopPausingActivity(), str2, false, str + "  topPausingActivity=", null);
            ActivityTaskSupervisor.printThisActivity(printWriter, getTopResumedActivity(), str2, false, str + "  topResumedActivity=", null);
            if (this.mMinWidth == -1 && this.mMinHeight == -1) {
                return;
            }
            printWriter.print(str);
            printWriter.print("  mMinWidth=");
            printWriter.print(this.mMinWidth);
            printWriter.print(" mMinHeight=");
            printWriter.println(this.mMinHeight);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public ArrayList<ActivityRecord> getDumpActivitiesLocked(String str, int i) {
        final ArrayList<ActivityRecord> arrayList = new ArrayList<>();
        if ("all".equals(str)) {
            forAllActivities(new Task$$ExternalSyntheticLambda44(arrayList));
        } else if ("top".equals(str)) {
            ActivityRecord topMostActivity = getTopMostActivity();
            if (topMostActivity != null) {
                arrayList.add(topMostActivity);
            }
        } else {
            final ActivityManagerService.ItemMatcher itemMatcher = new ActivityManagerService.ItemMatcher();
            itemMatcher.build(str);
            forAllActivities(new Consumer() { // from class: com.android.server.wm.Task$$ExternalSyntheticLambda45
                @Override // java.util.function.Consumer
                public final void accept(Object obj) {
                    Task.lambda$getDumpActivitiesLocked$32(itemMatcher, arrayList, (ActivityRecord) obj);
                }
            });
        }
        if (i != -1) {
            for (int size = arrayList.size() - 1; size >= 0; size--) {
                if (arrayList.get(size).mUserId != i) {
                    arrayList.remove(size);
                }
            }
        }
        return arrayList;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$getDumpActivitiesLocked$32(ActivityManagerService.ItemMatcher itemMatcher, ArrayList arrayList, ActivityRecord activityRecord) {
        if (itemMatcher.match(activityRecord, activityRecord.intent.getComponent())) {
            arrayList.add(activityRecord);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public ActivityRecord restartPackage(final String str) {
        final ActivityRecord activityRecord = topRunningActivity();
        forAllActivities(new Consumer() { // from class: com.android.server.wm.Task$$ExternalSyntheticLambda49
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                Task.lambda$restartPackage$33(str, activityRecord, (ActivityRecord) obj);
            }
        });
        return activityRecord;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$restartPackage$33(String str, ActivityRecord activityRecord, ActivityRecord activityRecord2) {
        if (activityRecord2.info.packageName.equals(str)) {
            activityRecord2.forceNewConfig = true;
            if (activityRecord != null && activityRecord2 == activityRecord && activityRecord2.isVisibleRequested()) {
                activityRecord2.startFreezingScreenLocked(256);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Task reuseOrCreateTask(ActivityInfo activityInfo, Intent intent, boolean z) {
        return reuseOrCreateTask(activityInfo, intent, null, null, z, null, null, null);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Task reuseOrCreateTask(ActivityInfo activityInfo, Intent intent, IVoiceInteractionSession iVoiceInteractionSession, IVoiceInteractor iVoiceInteractor, boolean z, ActivityRecord activityRecord, ActivityRecord activityRecord2, ActivityOptions activityOptions) {
        int nextTaskIdForUser;
        Task build;
        if (canReuseAsLeafTask()) {
            build = reuseAsLeafTask(iVoiceInteractionSession, iVoiceInteractor, intent, activityInfo, activityRecord);
        } else {
            if (activityRecord != null) {
                nextTaskIdForUser = this.mTaskSupervisor.getNextTaskIdForUser(activityRecord.mUserId);
            } else {
                nextTaskIdForUser = this.mTaskSupervisor.getNextTaskIdForUser();
            }
            getActivityType();
            build = new Builder(this.mAtmService).setTaskId(nextTaskIdForUser).setActivityInfo(activityInfo).setActivityOptions(activityOptions).setIntent(intent).setVoiceSession(iVoiceInteractionSession).setVoiceInteractor(iVoiceInteractor).setOnTop(z).setParent(this).build();
        }
        if (activityRecord.getWrapper().getExtImpl().getLaunchedFromMultiSearch()) {
            build.getWrapper().getExtImpl().setLaunchedFromMultiSearch(true);
        }
        this.mTaskWrapper.getExtImpl().setLaunchParams(activityOptions);
        this.mTaskWrapper.getExtImpl().setTaskCanvas((intent.getIntentExt().getOplusFlags() & 16384) != 0);
        int displayId = getDisplayId();
        boolean isKeyguardOrAodShowing = this.mAtmService.mTaskSupervisor.getKeyguardController().isKeyguardOrAodShowing(displayId != -1 ? displayId : 0);
        if (!this.mTaskSupervisor.getLaunchParamsController().layoutTask(build, activityInfo.windowLayout, activityRecord, activityRecord2, activityOptions) && !getRequestedOverrideBounds().isEmpty() && build.isResizeable() && !isKeyguardOrAodShowing) {
            build.setBounds(getRequestedOverrideBounds());
        }
        return build;
    }

    private boolean canReuseAsLeafTask() {
        if (this.mCreatedByOrganizer || !isLeafTask()) {
            return false;
        }
        return DisplayContent.alwaysCreateRootTask(getWindowingMode(), getActivityType());
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void addChild(WindowContainer windowContainer, boolean z, boolean z2) {
        Task asTask = windowContainer.asTask();
        if (asTask != null) {
            try {
                asTask.setForceShowForAllUsers(z2);
            } catch (Throwable th) {
                if (asTask != null) {
                    asTask.setForceShowForAllUsers(false);
                }
                throw th;
            }
        }
        addChild(windowContainer, z ? Integer.MAX_VALUE : 0, z);
        if (asTask != null) {
            asTask.setForceShowForAllUsers(false);
        }
    }

    public void setAlwaysOnTopOnly(boolean z) {
        if (getWindowConfiguration().isFlexibleAlwaysOnTop() != z) {
            Slog.d(TAG, "setAlwaysOnTopOnly alwaysOnTop " + z + " this " + this);
            super.setAlwaysOnTop(z);
        }
    }

    @Override // com.android.server.wm.ConfigurationContainer
    public void setAlwaysOnTop(boolean z) {
        if (super.isAlwaysOnTop() == z) {
            return;
        }
        super.setAlwaysOnTop(z);
        if (isForceHidden() || this.mTaskWrapper.getExtImpl().isPendingToBottomTask(this.mTaskId)) {
            return;
        }
        getDisplayArea().positionChildAt(Integer.MAX_VALUE, this, false);
    }

    void dismissPip() {
        if (!isActivityTypeStandardOrUndefined()) {
            throw new IllegalArgumentException("You can't move tasks from non-standard root tasks.");
        }
        if (getWindowingMode() != 2) {
            throw new IllegalArgumentException("Can't exit pinned mode if it's not pinned already.");
        }
        this.mWmService.inSurfaceTransaction(new Runnable() { // from class: com.android.server.wm.Task$$ExternalSyntheticLambda32
            @Override // java.lang.Runnable
            public final void run() {
                Task.this.lambda$dismissPip$34();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$dismissPip$34() {
        Task bottomMostTask = getBottomMostTask();
        setWindowingMode(0);
        if (isAttached()) {
            getDisplayArea().positionChildAt(Integer.MAX_VALUE, this, false);
        }
        this.mTaskSupervisor.scheduleUpdatePictureInPictureModeIfNeeded(bottomMostTask, this);
    }

    private int setBounds(Rect rect, Rect rect2) {
        if (ConfigurationContainer.equivalentBounds(rect, rect2)) {
            return 0;
        }
        if (!inMultiWindowMode() && !this.mTaskWrapper.getExtImpl().isZoomMode(getWindowingMode()) && !getWrapper().getExtImpl().isFlexibleWindowScenario(new int[0])) {
            rect2 = null;
        }
        return setBoundsUnchecked(rect2);
    }

    @Override // com.android.server.wm.ConfigurationContainer
    public void getBounds(Rect rect) {
        rect.set(getBounds());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void addChild(WindowContainer windowContainer, int i, boolean z) {
        addChild((Task) windowContainer, (Comparator<Task>) null);
        positionChildAt(i, windowContainer, z);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void positionChildAtTop(Task task) {
        if (task == null) {
            return;
        }
        if (task == this) {
            moveToFront("positionChildAtTop");
        } else {
            positionChildAt(Integer.MAX_VALUE, task, true);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void positionChildAtBottom(Task task) {
        positionChildAtBottom(task, getDisplayArea().getNextFocusableRootTask(task.getRootTask(), true) == null);
    }

    @VisibleForTesting
    void positionChildAtBottom(Task task, boolean z) {
        if (task == null) {
            return;
        }
        positionChildAt(ChargerErrorCode.ERR_FILE_FAILURE_ACCESS, task, z);
    }

    @Override // com.android.server.wm.TaskFragment, com.android.server.wm.WindowContainer
    void onChildPositionChanged(WindowContainer windowContainer) {
        dispatchTaskInfoChangedIfNeeded(false);
        if (this.mChildren.contains(windowContainer) && windowContainer.asTask() != null) {
            this.mRootWindowContainer.invalidateTaskLayers();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void reparent(TaskDisplayArea taskDisplayArea, boolean z) {
        if (taskDisplayArea == null) {
            throw new IllegalArgumentException("Task can't reparent to null " + this);
        }
        if (getParent() == taskDisplayArea) {
            throw new IllegalArgumentException("Task=" + this + " already child of " + taskDisplayArea);
        }
        if (canBeLaunchedOnDisplay(taskDisplayArea.getDisplayId())) {
            reparent(taskDisplayArea, z ? Integer.MAX_VALUE : ChargerErrorCode.ERR_FILE_FAILURE_ACCESS);
            if (isLeafTask()) {
                taskDisplayArea.onLeafTaskMoved(this, z, !z);
                return;
            }
            return;
        }
        Slog.w(TAG, "Task=" + this + " can't reparent to " + taskDisplayArea);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setLastRecentsAnimationTransaction(PictureInPictureSurfaceTransaction pictureInPictureSurfaceTransaction, SurfaceControl surfaceControl) {
        this.mLastRecentsAnimationTransaction = new PictureInPictureSurfaceTransaction(pictureInPictureSurfaceTransaction);
        this.mLastRecentsAnimationOverlay = surfaceControl;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void clearLastRecentsAnimationTransaction(boolean z) {
        if (z && this.mLastRecentsAnimationOverlay != null) {
            getPendingTransaction().remove(this.mLastRecentsAnimationOverlay);
        }
        this.mLastRecentsAnimationTransaction = null;
        this.mLastRecentsAnimationOverlay = null;
        resetSurfaceControlTransforms();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void resetSurfaceControlTransforms() {
        getSyncTransaction().setMatrix(this.mSurfaceControl, Matrix.IDENTITY_MATRIX, new float[9]).setWindowCrop(this.mSurfaceControl, null).setShadowRadius(this.mSurfaceControl, 0.0f).setCornerRadius(this.mSurfaceControl, 0.0f);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void maybeApplyLastRecentsAnimationTransaction() {
        if (this.mLastRecentsAnimationTransaction != null) {
            SurfaceControl.Transaction pendingTransaction = getPendingTransaction();
            SurfaceControl surfaceControl = this.mLastRecentsAnimationOverlay;
            if (surfaceControl != null) {
                pendingTransaction.reparent(surfaceControl, this.mSurfaceControl);
            }
            PictureInPictureSurfaceTransaction.apply(this.mLastRecentsAnimationTransaction, this.mSurfaceControl, pendingTransaction);
            pendingTransaction.show(this.mSurfaceControl);
            this.mLastRecentsAnimationTransaction = null;
            this.mLastRecentsAnimationOverlay = null;
        }
    }

    private void updateSurfaceBounds() {
        updateSurfaceSize(getSyncTransaction());
        updateSurfacePositionNonOrganized();
        scheduleAnimation();
    }

    private Point getRelativePosition() {
        Point point = new Point();
        getRelativePosition(point);
        return point;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean shouldIgnoreInput() {
        return this.mAtmService.mHasLeanbackFeature && inPinnedWindowingMode() && !isFocusedRootTaskOnDisplay();
    }

    private void warnForNonLeafTask(String str) {
        if (isLeafTask()) {
            return;
        }
        Slog.w(TAG, str + " on non-leaf task " + this);
    }

    public DisplayInfo getDisplayInfo() {
        return this.mDisplayContent.getDisplayInfo();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public AnimatingActivityRegistry getAnimatingActivityRegistry() {
        return this.mAnimatingActivityRegistry;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.android.server.wm.TaskFragment
    public void executeAppTransition(ActivityOptions activityOptions) {
        this.mDisplayContent.executeAppTransition();
        ActivityOptions.abort(activityOptions);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.android.server.wm.TaskFragment
    public boolean shouldSleepActivities() {
        boolean isKeyguardGoingAway;
        DisplayContent displayContent = this.mDisplayContent;
        if (displayContent != null) {
            isKeyguardGoingAway = displayContent.isKeyguardGoingAway();
        } else {
            isKeyguardGoingAway = this.mRootWindowContainer.getDefaultDisplay().isKeyguardGoingAway();
        }
        if (isKeyguardGoingAway && isFocusedRootTaskOnDisplay() && displayContent.isDefaultDisplay) {
            return false;
        }
        return displayContent != null ? displayContent.isSleeping() : this.mAtmService.isSleepingLocked();
    }

    private Rect getRawBounds() {
        return super.getBounds();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void dispatchTaskInfoChangedIfNeeded(boolean z) {
        if (isOrganized()) {
            this.mAtmService.mTaskOrganizerController.onTaskInfoChanged(this, z);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setReparentLeafTaskIfRelaunch(boolean z) {
        if (isOrganized()) {
            this.mReparentLeafTaskIfRelaunch = z;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean isSameRequiredDisplayCategory(ActivityInfo activityInfo) {
        String str = this.mRequiredDisplayCategory;
        return (str != null && str.equals(activityInfo.requiredDisplayCategory)) || (this.mRequiredDisplayCategory == null && activityInfo.requiredDisplayCategory == null);
    }

    @Override // com.android.server.wm.TaskFragment, com.android.server.wm.WindowContainer, com.android.server.wm.ConfigurationContainer
    public void dumpDebug(ProtoOutputStream protoOutputStream, long j, int i) {
        if (i != 2 || isVisible()) {
            long start = protoOutputStream.start(j);
            protoOutputStream.write(1120986464258L, this.mTaskId);
            protoOutputStream.write(1120986464272L, getRootTaskId());
            if (getTopResumedActivity() != null) {
                getTopResumedActivity().writeIdentifierToProto(protoOutputStream, 1146756268044L);
            }
            ComponentName componentName = this.realActivity;
            if (componentName != null) {
                protoOutputStream.write(1138166333453L, componentName.flattenToShortString());
            }
            ComponentName componentName2 = this.origActivity;
            if (componentName2 != null) {
                protoOutputStream.write(1138166333454L, componentName2.flattenToShortString());
            }
            protoOutputStream.write(1120986464274L, this.mResizeMode);
            protoOutputStream.write(1133871366148L, matchParentBounds());
            getRawBounds().dumpDebug(protoOutputStream, 1146756268037L);
            Rect rect = this.mLastNonFullscreenBounds;
            if (rect != null) {
                rect.dumpDebug(protoOutputStream, 1146756268054L);
            }
            SurfaceControl surfaceControl = this.mSurfaceControl;
            if (surfaceControl != null) {
                protoOutputStream.write(1120986464264L, surfaceControl.getWidth());
                protoOutputStream.write(1120986464265L, this.mSurfaceControl.getHeight());
            }
            protoOutputStream.write(1133871366172L, this.mCreatedByOrganizer);
            protoOutputStream.write(1138166333469L, this.affinity);
            protoOutputStream.write(1133871366174L, this.mChildPipActivity != null);
            super.dumpDebug(protoOutputStream, 1146756268063L, i);
            protoOutputStream.end(start);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
    public static class Builder {
        private ActivityInfo mActivityInfo;
        private ActivityOptions mActivityOptions;
        private int mActivityType;
        private String mAffinity;
        private Intent mAffinityIntent;
        private boolean mAskedCompatMode;
        private final ActivityTaskManagerService mAtmService;
        private boolean mAutoRemoveRecents;
        private String mCallingFeatureId;
        private String mCallingPackage;
        private int mCallingUid;
        private boolean mCreatedByOrganizer;
        private boolean mDeferTaskAppear;
        private int mEffectiveUid;
        private boolean mHasBeenVisible;
        private Intent mIntent;
        private String mLastDescription;
        private ActivityManager.RecentTaskInfo.PersistedTaskSnapshotData mLastSnapshotData;
        private ActivityManager.TaskDescription mLastTaskDescription;
        private long mLastTimeMoved;
        private IBinder mLaunchCookie;
        private int mLaunchFlags;
        private boolean mNeverRelinquishIdentity;
        private boolean mOnTop;
        private ComponentName mOrigActivity;
        private WindowContainer mParent;
        private ComponentName mRealActivity;
        private boolean mRealActivitySuspended;
        private boolean mRemoveWithTaskOrganizer;
        private int mResizeMode;
        private String mRootAffinity;
        private boolean mRootWasReset;
        private Task mSourceTask;
        private boolean mSupportsPictureInPicture;
        private int mTaskAffiliation;
        private int mTaskId;
        private int mUserId;
        private boolean mUserSetupComplete;
        private IVoiceInteractor mVoiceInteractor;
        private IVoiceInteractionSession mVoiceSession;
        private int mPrevAffiliateTaskId = -1;
        private int mNextAffiliateTaskId = -1;
        private int mMinWidth = -1;
        private int mMinHeight = -1;
        private int mWindowingMode = 0;

        /* JADX INFO: Access modifiers changed from: package-private */
        public Builder(ActivityTaskManagerService activityTaskManagerService) {
            this.mAtmService = activityTaskManagerService;
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public Builder setParent(WindowContainer windowContainer) {
            this.mParent = windowContainer;
            return this;
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public Builder setSourceTask(Task task) {
            this.mSourceTask = task;
            return this;
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public Builder setLaunchFlags(int i) {
            this.mLaunchFlags = i;
            return this;
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public Builder setTaskId(int i) {
            this.mTaskId = i;
            return this;
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public Builder setIntent(Intent intent) {
            this.mIntent = intent;
            return this;
        }

        Builder setRealActivity(ComponentName componentName) {
            this.mRealActivity = componentName;
            return this;
        }

        Builder setEffectiveUid(int i) {
            this.mEffectiveUid = i;
            return this;
        }

        Builder setMinWidth(int i) {
            this.mMinWidth = i;
            return this;
        }

        Builder setMinHeight(int i) {
            this.mMinHeight = i;
            return this;
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public Builder setActivityInfo(ActivityInfo activityInfo) {
            this.mActivityInfo = activityInfo;
            return this;
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public Builder setActivityOptions(ActivityOptions activityOptions) {
            this.mActivityOptions = activityOptions;
            return this;
        }

        Builder setVoiceSession(IVoiceInteractionSession iVoiceInteractionSession) {
            this.mVoiceSession = iVoiceInteractionSession;
            return this;
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public Builder setActivityType(int i) {
            this.mActivityType = i;
            return this;
        }

        int getActivityType() {
            return this.mActivityType;
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public Builder setWindowingMode(int i) {
            this.mWindowingMode = i;
            return this;
        }

        int getWindowingMode() {
            return this.mWindowingMode;
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public Builder setCreatedByOrganizer(boolean z) {
            this.mCreatedByOrganizer = z;
            return this;
        }

        boolean getCreatedByOrganizer() {
            return this.mCreatedByOrganizer;
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public Builder setDeferTaskAppear(boolean z) {
            this.mDeferTaskAppear = z;
            return this;
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public Builder setLaunchCookie(IBinder iBinder) {
            this.mLaunchCookie = iBinder;
            return this;
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public Builder setOnTop(boolean z) {
            this.mOnTop = z;
            return this;
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public Builder setHasBeenVisible(boolean z) {
            this.mHasBeenVisible = z;
            return this;
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public Builder setRemoveWithTaskOrganizer(boolean z) {
            this.mRemoveWithTaskOrganizer = z;
            return this;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public Builder setUserId(int i) {
            this.mUserId = i;
            return this;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public Builder setLastTimeMoved(long j) {
            this.mLastTimeMoved = j;
            return this;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public Builder setNeverRelinquishIdentity(boolean z) {
            this.mNeverRelinquishIdentity = z;
            return this;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public Builder setCallingUid(int i) {
            this.mCallingUid = i;
            return this;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public Builder setCallingPackage(String str) {
            this.mCallingPackage = str;
            return this;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public Builder setResizeMode(int i) {
            this.mResizeMode = i;
            return this;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public Builder setSupportsPictureInPicture(boolean z) {
            this.mSupportsPictureInPicture = z;
            return this;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public Builder setUserSetupComplete(boolean z) {
            this.mUserSetupComplete = z;
            return this;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public Builder setTaskAffiliation(int i) {
            this.mTaskAffiliation = i;
            return this;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public Builder setPrevAffiliateTaskId(int i) {
            this.mPrevAffiliateTaskId = i;
            return this;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public Builder setNextAffiliateTaskId(int i) {
            this.mNextAffiliateTaskId = i;
            return this;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public Builder setCallingFeatureId(String str) {
            this.mCallingFeatureId = str;
            return this;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public Builder setRealActivitySuspended(boolean z) {
            this.mRealActivitySuspended = z;
            return this;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public Builder setLastDescription(String str) {
            this.mLastDescription = str;
            return this;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public Builder setLastTaskDescription(ActivityManager.TaskDescription taskDescription) {
            this.mLastTaskDescription = taskDescription;
            return this;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public Builder setLastSnapshotData(ActivityManager.RecentTaskInfo.PersistedTaskSnapshotData persistedTaskSnapshotData) {
            this.mLastSnapshotData = persistedTaskSnapshotData;
            return this;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public Builder setOrigActivity(ComponentName componentName) {
            this.mOrigActivity = componentName;
            return this;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public Builder setRootWasReset(boolean z) {
            this.mRootWasReset = z;
            return this;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public Builder setAutoRemoveRecents(boolean z) {
            this.mAutoRemoveRecents = z;
            return this;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public Builder setAskedCompatMode(boolean z) {
            this.mAskedCompatMode = z;
            return this;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public Builder setAffinityIntent(Intent intent) {
            this.mAffinityIntent = intent;
            return this;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public Builder setAffinity(String str) {
            this.mAffinity = str;
            return this;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public Builder setRootAffinity(String str) {
            this.mRootAffinity = str;
            return this;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public Builder setVoiceInteractor(IVoiceInteractor iVoiceInteractor) {
            this.mVoiceInteractor = iVoiceInteractor;
            return this;
        }

        private void validateRootTask(TaskDisplayArea taskDisplayArea) {
            Task rootTask;
            if (this.mActivityType == 0 && !this.mCreatedByOrganizer) {
                this.mActivityType = 1;
            }
            int i = this.mActivityType;
            if (i != 1 && i != 0 && (rootTask = taskDisplayArea.getRootTask(0, i)) != null) {
                throw new IllegalArgumentException("Root task=" + rootTask + " of activityType=" + this.mActivityType + " already on display=" + taskDisplayArea + ". Can't have multiple.");
            }
            int i2 = this.mWindowingMode;
            ActivityTaskManagerService activityTaskManagerService = this.mAtmService;
            if (!TaskDisplayArea.isWindowingModeSupported(i2, activityTaskManagerService.mSupportsMultiWindow, activityTaskManagerService.mSupportsFreeformWindowManagement, activityTaskManagerService.mSupportsPictureInPicture)) {
                throw new IllegalArgumentException("Can't create root task for unsupported windowingMode=" + this.mWindowingMode);
            }
            int i3 = this.mWindowingMode;
            if (i3 == 2 && this.mActivityType != 1) {
                throw new IllegalArgumentException("Root task with pinned windowing mode cannot with non-standard activity type.");
            }
            if (i3 == 2 && taskDisplayArea.getRootPinnedTask() != null) {
                taskDisplayArea.getRootPinnedTask().dismissPip();
            }
            Intent intent = this.mIntent;
            if (intent != null) {
                this.mLaunchFlags = intent.getFlags() | this.mLaunchFlags;
            }
            Task launchRootTask = this.mCreatedByOrganizer ? null : taskDisplayArea.getLaunchRootTask(this.mWindowingMode, this.mActivityType, this.mActivityOptions, this.mSourceTask, this.mLaunchFlags);
            if (launchRootTask != null) {
                this.mWindowingMode = 0;
                this.mParent = launchRootTask;
            }
            this.mTaskId = taskDisplayArea.getNextRootTaskId();
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public Task build() {
            ActivityOptions activityOptions;
            WindowContainer windowContainer = this.mParent;
            if (windowContainer != null && (windowContainer instanceof TaskDisplayArea)) {
                validateRootTask((TaskDisplayArea) windowContainer);
            }
            if (this.mActivityInfo == null) {
                ActivityInfo activityInfo = new ActivityInfo();
                this.mActivityInfo = activityInfo;
                activityInfo.applicationInfo = new ApplicationInfo();
            }
            this.mUserId = UserHandle.getUserId(this.mActivityInfo.applicationInfo.uid);
            this.mTaskAffiliation = this.mTaskId;
            this.mLastTimeMoved = System.currentTimeMillis();
            this.mNeverRelinquishIdentity = true;
            ActivityInfo activityInfo2 = this.mActivityInfo;
            this.mCallingUid = activityInfo2.applicationInfo.uid;
            this.mCallingPackage = activityInfo2.packageName;
            this.mResizeMode = activityInfo2.resizeMode;
            this.mSupportsPictureInPicture = activityInfo2.supportsPictureInPicture();
            if (!this.mRemoveWithTaskOrganizer && (activityOptions = this.mActivityOptions) != null) {
                this.mRemoveWithTaskOrganizer = activityOptions.getRemoveWithTaskOranizer();
            }
            Task buildInner = buildInner();
            buildInner.mHasBeenVisible = this.mHasBeenVisible;
            int i = this.mActivityType;
            if (i != 0) {
                buildInner.setActivityType(i);
            }
            WindowContainer windowContainer2 = this.mParent;
            if (windowContainer2 != null) {
                if (windowContainer2 instanceof Task) {
                    ((Task) windowContainer2).addChild(buildInner, this.mOnTop ? Integer.MAX_VALUE : Integer.MIN_VALUE, (this.mActivityInfo.flags & 1024) != 0);
                } else {
                    windowContainer2.addChild(buildInner, this.mOnTop ? Integer.MAX_VALUE : Integer.MIN_VALUE);
                }
            }
            int i2 = this.mWindowingMode;
            if (i2 != 0) {
                buildInner.setWindowingMode(i2, true);
            }
            return buildInner;
        }

        @VisibleForTesting
        Task buildInner() {
            return new Task(this.mAtmService, this.mTaskId, this.mIntent, this.mAffinityIntent, this.mAffinity, this.mRootAffinity, this.mRealActivity, this.mOrigActivity, this.mRootWasReset, this.mAutoRemoveRecents, this.mAskedCompatMode, this.mUserId, this.mEffectiveUid, this.mLastDescription, this.mLastTimeMoved, this.mNeverRelinquishIdentity, this.mLastTaskDescription, this.mLastSnapshotData, this.mTaskAffiliation, this.mPrevAffiliateTaskId, this.mNextAffiliateTaskId, this.mCallingUid, this.mCallingPackage, this.mCallingFeatureId, this.mResizeMode, this.mSupportsPictureInPicture, this.mRealActivitySuspended, this.mUserSetupComplete, this.mMinWidth, this.mMinHeight, this.mActivityInfo, this.mVoiceSession, this.mVoiceInteractor, this.mCreatedByOrganizer, this.mLaunchCookie, this.mDeferTaskAppear, this.mRemoveWithTaskOrganizer);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.android.server.wm.WindowContainer
    public void updateOverlayInsetsState(WindowState windowState) {
        super.updateOverlayInsetsState(windowState);
        if (windowState == getTopVisibleAppMainWindow() && this.mOverlayHost != null) {
            InsetsState insetsState = windowState.getInsetsState(true);
            getBounds(this.mTmpRect);
            this.mOverlayHost.dispatchInsetsChanged(insetsState, this.mTmpRect);
        }
    }

    @Override // com.android.server.wm.TaskFragment
    boolean forceCreateRemoteAnimationTarget(ActivityRecord activityRecord) {
        boolean forceCreateRemoteAnimationTarget = this.mTaskWrapper.mTaskExt.forceCreateRemoteAnimationTarget(activityRecord);
        if (forceCreateRemoteAnimationTarget) {
            this.mTaskWrapper.mTaskExt.addStartingBackColorLayerIfNeed(activityRecord);
        }
        return forceCreateRemoteAnimationTarget;
    }

    @Override // com.android.server.wm.TaskFragment
    void setAppTransitionReadyInAdvance(DisplayContent displayContent, ActivityRecord activityRecord) {
        this.mTaskWrapper.mTaskExt.setAppTransitionReadyInAdvance(this.mDisplayContent, activityRecord);
    }

    boolean allResumedActivitiesComplete() {
        if (!isLeafTask()) {
            for (int size = this.mChildren.size() - 1; size >= 0; size--) {
                Task asTask = ((WindowContainer) this.mChildren.get(size)).asTask();
                if (asTask != null && !asTask.allResumedActivitiesComplete()) {
                    return false;
                }
            }
        } else {
            ActivityRecord resumedActivity = getResumedActivity();
            ActivityRecord activityRecord = null;
            for (int size2 = this.mChildren.size() - 1; size2 >= 0; size2--) {
                WindowContainer windowContainer = (WindowContainer) this.mChildren.get(size2);
                if (windowContainer.asTaskFragment() != null) {
                    activityRecord = windowContainer.asTaskFragment().getTopResumedActivity();
                } else if (resumedActivity != null && windowContainer.asActivityRecord() == resumedActivity) {
                    activityRecord = resumedActivity;
                }
                if (activityRecord != null && !activityRecord.isState(ActivityRecord.State.RESUMED)) {
                    return false;
                }
            }
        }
        return true;
    }

    public ITaskWrapper getWrapper() {
        return this.mTaskWrapper;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
    public class TaskWrapper implements ITaskWrapper {
        private ITaskExt mTaskExt;

        private TaskWrapper() {
            this.mTaskExt = (ITaskExt) ExtLoader.type(ITaskExt.class).base(Task.this).create();
        }

        @Override // com.android.server.wm.ITaskWrapper
        public ITaskExt getExtImpl() {
            return this.mTaskExt;
        }

        @Override // com.android.server.wm.ITaskWrapper
        public WindowProcessController getWindowProcessController() {
            return Task.this.mRootProcess;
        }

        @Override // com.android.server.wm.ITaskWrapper
        public void removeHiddenFlags(int i) {
            Task task = Task.this;
            task.mForceHiddenFlags = (~i) & task.mForceHiddenFlags;
        }

        @Override // com.android.server.wm.ITaskWrapper
        public ActivityRecord topRunningActivityLocked() {
            return Task.this.topRunningActivityLocked();
        }
    }
}
