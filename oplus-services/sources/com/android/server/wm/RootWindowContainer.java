package com.android.server.wm;

import android.app.ActivityManager;
import android.app.ActivityOptions;
import android.app.ActivityTaskManager;
import android.app.AppGlobals;
import android.app.WindowConfiguration;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.ResolveInfo;
import android.content.res.Configuration;
import android.graphics.Rect;
import android.hardware.display.DisplayManager;
import android.hardware.display.DisplayManagerInternal;
import android.net.Uri;
import android.os.Binder;
import android.os.Debug;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.Process;
import android.os.RemoteException;
import android.os.SystemClock;
import android.os.Trace;
import android.os.UserHandle;
import android.os.storage.StorageManager;
import android.provider.Settings;
import android.service.voice.IVoiceInteractionSession;
import android.util.ArrayMap;
import android.util.ArraySet;
import android.util.IntArray;
import android.util.Pair;
import android.util.Slog;
import android.util.SparseArray;
import android.util.SparseIntArray;
import android.util.TimeUtils;
import android.util.proto.ProtoOutputStream;
import android.view.Display;
import android.view.DisplayInfo;
import android.view.SurfaceControl;
import android.view.WindowManager;
import android.window.PictureInPictureSurfaceTransaction;
import android.window.TaskFragmentAnimationParams;
import android.window.WindowContainerToken;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.app.ResolverActivity;
import com.android.internal.protolog.ProtoLogGroup;
import com.android.internal.protolog.ProtoLogImpl;
import com.android.internal.util.ToBooleanFunction;
import com.android.internal.util.function.QuintPredicate;
import com.android.internal.util.function.pooled.PooledLambda;
import com.android.internal.util.function.pooled.PooledPredicate;
import com.android.server.LocalServices;
import com.android.server.am.AppTimeTracker;
import com.android.server.am.UserState;
import com.android.server.display.IMirageDisplayManagerExt;
import com.android.server.policy.DeviceStateProviderImpl;
import com.android.server.policy.PermissionPolicyInternal;
import com.android.server.utils.Slogf;
import com.android.server.wm.ActivityRecord;
import com.android.server.wm.ActivityTaskManagerInternal;
import com.android.server.wm.ActivityTaskManagerService;
import com.android.server.wm.IRootWindowContainerExt;
import com.android.server.wm.LaunchParamsController;
import com.android.server.wm.RootWindowContainer;
import com.android.server.wm.Task;
import com.android.server.wm.TransitionController;
import com.android.server.wm.utils.LogUtil;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import system.ext.loader.core.ExtLoader;
import vendor.oplus.hardware.charger.ChargerErrorCode;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
public class RootWindowContainer extends WindowContainer<DisplayContent> implements DisplayManager.DisplayListener {
    private static final String DISPLAY_OFF_SLEEP_TOKEN_TAG = "Display-off";
    static final int MATCH_ATTACHED_TASK_ONLY = 0;
    static final int MATCH_ATTACHED_TASK_OR_RECENT_TASKS = 1;
    static final int MATCH_ATTACHED_TASK_OR_RECENT_TASKS_AND_RESTORE = 2;
    public static final int MATCH_TASK_IN_STACKS_ONLY = 0;
    private static final int SET_SCREEN_BRIGHTNESS_OVERRIDE = 1;
    private static final int SET_USER_ACTIVITY_TIMEOUT = 2;
    private final AttachApplicationHelper mAttachApplicationHelper;
    private final Consumer<WindowState> mCloseSystemDialogsConsumer;
    private String mCloseSystemDialogsReason;
    int mCurrentUser;
    private DisplayContent mDefaultDisplay;
    int mDefaultMinSizeOfResizeableTaskDp;
    private String mDestroyAllActivitiesReason;
    private final Runnable mDestroyAllActivitiesRunnable;
    private final DeviceStateController mDeviceStateController;
    private final SparseArray<IntArray> mDisplayAccessUIDs;
    DisplayManager mDisplayManager;
    private DisplayManagerInternal mDisplayManagerInternal;
    final ActivityTaskManagerInternal.SleepTokenAcquirer mDisplayOffTokenAcquirer;
    private final DisplayRotationCoordinator mDisplayRotationCoordinator;
    private final ToBooleanFunction<WindowState> mFindOrientationChangingFunction;
    FinishDisabledPackageActivitiesHelper mFinishDisabledPackageActivitiesHelper;
    private final Handler mHandler;
    private Object mLastWindowFreezeSource;
    private boolean mObscureApplicationContentOnSecondaryDisplays;
    boolean mOrientationChangeComplete;
    private final RankTaskLayersRunnable mRankTaskLayersRunnable;
    public IRootWindowContainerExt mRootWindowContainerExt;
    private float mScreenBrightnessOverride;
    ActivityTaskManagerService mService;
    final SparseArray<SleepToken> mSleepTokens;
    public IRootWindowContainerSocExt mSocExt;
    private boolean mSustainedPerformanceModeCurrent;
    private boolean mSustainedPerformanceModeEnabled;
    private boolean mTaskLayersChanged;
    ActivityTaskSupervisor mTaskSupervisor;
    private final FindTaskResult mTmpFindTaskResult;
    boolean mTmpOrientationChangeComplete;
    private int mTmpTaskLayerRank;
    final ArrayMap<Integer, ActivityRecord> mTopFocusedAppByProcess;
    private int mTopFocusedDisplayId;
    private boolean mUpdateRotation;
    private long mUserActivityTimeout;
    SparseIntArray mUserRootTaskInFront;
    boolean mWallpaperActionPending;
    WindowManagerService mWindowManager;
    private static final String TAG = "WindowManager";
    static final String TAG_TASKS = TAG + ActivityTaskManagerDebugConfig.POSTFIX_TASKS;
    static final String TAG_STATES = TAG + ActivityTaskManagerDebugConfig.POSTFIX_STATES;
    private static final String TAG_RECENTS = TAG + ActivityTaskManagerDebugConfig.POSTFIX_RECENTS;
    public static boolean mPerfSendTapHint = false;
    public static boolean mIsPerfBoostAcquired = false;
    public static int mPerfHandle = -1;

    @Retention(RetentionPolicy.SOURCE)
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
    public @interface AnyTaskForIdMatchTaskMode {
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.android.server.wm.ConfigurationContainer
    public String getName() {
        return "ROOT";
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.android.server.wm.WindowContainer
    public boolean isAttached() {
        return true;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.android.server.wm.WindowContainer
    public boolean isOnTop() {
        return true;
    }

    @Override // com.android.server.wm.WindowContainer, com.android.server.wm.SurfaceAnimator.Animatable
    public /* bridge */ /* synthetic */ void commitPendingTransaction() {
        super.commitPendingTransaction();
    }

    @Override // com.android.server.wm.WindowContainer
    public /* bridge */ /* synthetic */ int compareTo(WindowContainer windowContainer) {
        return super.compareTo(windowContainer);
    }

    @Override // com.android.server.wm.WindowContainer, com.android.server.wm.SurfaceAnimator.Animatable
    public /* bridge */ /* synthetic */ SurfaceControl getAnimationLeash() {
        return super.getAnimationLeash();
    }

    @Override // com.android.server.wm.WindowContainer, com.android.server.wm.SurfaceAnimator.Animatable
    public /* bridge */ /* synthetic */ SurfaceControl getAnimationLeashParent() {
        return super.getAnimationLeashParent();
    }

    @Override // com.android.server.wm.WindowContainer
    public /* bridge */ /* synthetic */ DisplayContent getDisplayContent() {
        return super.getDisplayContent();
    }

    @Override // com.android.server.wm.WindowContainer, com.android.server.wm.SurfaceFreezer.Freezable
    public /* bridge */ /* synthetic */ SurfaceControl getFreezeSnapshotTarget() {
        return super.getFreezeSnapshotTarget();
    }

    @Override // com.android.server.wm.WindowContainer
    public /* bridge */ /* synthetic */ SparseArray getInsetsSourceProviders() {
        return super.getInsetsSourceProviders();
    }

    @Override // com.android.server.wm.WindowContainer, com.android.server.wm.SurfaceAnimator.Animatable
    public /* bridge */ /* synthetic */ SurfaceControl getParentSurfaceControl() {
        return super.getParentSurfaceControl();
    }

    @Override // com.android.server.wm.WindowContainer, com.android.server.wm.SurfaceAnimator.Animatable
    public /* bridge */ /* synthetic */ SurfaceControl.Transaction getPendingTransaction() {
        return super.getPendingTransaction();
    }

    @Override // com.android.server.wm.WindowContainer, com.android.server.wm.SurfaceAnimator.Animatable
    public /* bridge */ /* synthetic */ SurfaceControl getSurfaceControl() {
        return super.getSurfaceControl();
    }

    @Override // com.android.server.wm.WindowContainer, com.android.server.wm.SurfaceAnimator.Animatable
    public /* bridge */ /* synthetic */ int getSurfaceHeight() {
        return super.getSurfaceHeight();
    }

    @Override // com.android.server.wm.WindowContainer, com.android.server.wm.SurfaceAnimator.Animatable
    public /* bridge */ /* synthetic */ int getSurfaceWidth() {
        return super.getSurfaceWidth();
    }

    @Override // com.android.server.wm.WindowContainer, com.android.server.wm.SurfaceAnimator.Animatable
    public /* bridge */ /* synthetic */ SurfaceControl.Transaction getSyncTransaction() {
        return super.getSyncTransaction();
    }

    @Override // com.android.server.wm.WindowContainer
    public /* bridge */ /* synthetic */ IWindowContainerWrapper getWCWrapper() {
        return super.getWCWrapper();
    }

    @Override // com.android.server.wm.WindowContainer
    public /* bridge */ /* synthetic */ boolean hasInsetsSourceProvider() {
        return super.hasInsetsSourceProvider();
    }

    @Override // com.android.server.wm.WindowContainer, com.android.server.wm.SurfaceAnimator.Animatable
    public /* bridge */ /* synthetic */ SurfaceControl.Builder makeAnimationLeash() {
        return super.makeAnimationLeash();
    }

    @Override // com.android.server.wm.WindowContainer, com.android.server.wm.SurfaceAnimator.Animatable
    public /* bridge */ /* synthetic */ void onAnimationLeashCreated(SurfaceControl.Transaction transaction, SurfaceControl surfaceControl) {
        super.onAnimationLeashCreated(transaction, surfaceControl);
    }

    @Override // com.android.server.wm.WindowContainer, com.android.server.wm.SurfaceAnimator.Animatable
    public /* bridge */ /* synthetic */ void onAnimationLeashLost(SurfaceControl.Transaction transaction) {
        super.onAnimationLeashLost(transaction);
    }

    @Override // com.android.server.wm.WindowContainer, com.android.server.wm.ConfigurationContainer
    public /* bridge */ /* synthetic */ void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
    }

    @Override // com.android.server.wm.WindowContainer, com.android.server.wm.ConfigurationContainer
    public /* bridge */ /* synthetic */ void onRequestedOverrideConfigurationChanged(Configuration configuration) {
        super.onRequestedOverrideConfigurationChanged(configuration);
    }

    @Override // com.android.server.wm.WindowContainer, com.android.server.wm.SurfaceFreezer.Freezable
    public /* bridge */ /* synthetic */ void onUnfrozen() {
        super.onUnfrozen();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: com.android.server.wm.RootWindowContainer$1, reason: invalid class name */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
    public class AnonymousClass1 implements Runnable {
        AnonymousClass1() {
        }

        @Override // java.lang.Runnable
        public void run() {
            WindowManagerGlobalLock windowManagerGlobalLock = RootWindowContainer.this.mService.mGlobalLock;
            WindowManagerService.boostPriorityForLockedSection();
            synchronized (windowManagerGlobalLock) {
                try {
                    try {
                        RootWindowContainer.this.mTaskSupervisor.beginDeferResume();
                        RootWindowContainer.this.forAllActivities(new Consumer() { // from class: com.android.server.wm.RootWindowContainer$1$$ExternalSyntheticLambda0
                            @Override // java.util.function.Consumer
                            public final void accept(Object obj) {
                                RootWindowContainer.AnonymousClass1.this.lambda$run$0((ActivityRecord) obj);
                            }
                        });
                    } finally {
                        RootWindowContainer.this.mTaskSupervisor.endDeferResume();
                        RootWindowContainer.this.resumeFocusedTasksTopActivities();
                    }
                } catch (Throwable th) {
                    WindowManagerService.resetPriorityAfterLockedSection();
                    throw th;
                }
            }
            WindowManagerService.resetPriorityAfterLockedSection();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$run$0(ActivityRecord activityRecord) {
            if (activityRecord.finishing || !activityRecord.isDestroyable()) {
                return;
            }
            if (ActivityTaskManagerDebugConfig.DEBUG_SWITCH) {
                Slog.v(ActivityTaskManagerService.TAG_SWITCH, "Destroying " + activityRecord + " in state " + activityRecord.getState() + " resumed=" + activityRecord.getTask().getTopResumedActivity() + " pausing=" + activityRecord.getTask().getTopPausingActivity() + " for reason " + RootWindowContainer.this.mDestroyAllActivitiesReason);
            }
            activityRecord.destroyImmediately(RootWindowContainer.this.mDestroyAllActivitiesReason);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
    public static class FindTaskResult implements Predicate<Task> {
        private ComponentName cls;
        private Uri documentData;
        private boolean isDocument;
        private int mActivityType;
        ActivityRecord mCandidateRecord;
        private IRootWindowContainerExt.IFindTaskResultExt mFindTaskResultExt = (IRootWindowContainerExt.IFindTaskResultExt) ExtLoader.type(IRootWindowContainerExt.IFindTaskResultExt.class).create();
        ActivityRecord mIdealRecord;
        private ActivityInfo mInfo;
        private Intent mIntent;
        private String mTaskAffinity;
        private int userId;

        FindTaskResult() {
        }

        void init(int i, String str, Intent intent, ActivityInfo activityInfo) {
            this.mActivityType = i;
            this.mTaskAffinity = str;
            this.mIntent = intent;
            this.mInfo = activityInfo;
            this.mIdealRecord = null;
            this.mCandidateRecord = null;
        }

        void process(WindowContainer windowContainer) {
            this.cls = this.mIntent.getComponent();
            if (this.mInfo.targetActivity != null) {
                ActivityInfo activityInfo = this.mInfo;
                this.cls = new ComponentName(activityInfo.packageName, activityInfo.targetActivity);
            }
            this.userId = UserHandle.getUserId(this.mInfo.applicationInfo.uid);
            Intent intent = this.mIntent;
            boolean isDocument = intent.isDocument() & (intent != null);
            this.isDocument = isDocument;
            this.documentData = isDocument ? this.mIntent.getData() : null;
            if (ProtoLogCache.WM_DEBUG_TASKS_enabled) {
                ProtoLogImpl.d(ProtoLogGroup.WM_DEBUG_TASKS, -814760297, 0, (String) null, new Object[]{String.valueOf(this.mInfo), String.valueOf(windowContainer)});
            }
            windowContainer.forAllLeafTasks(this);
        }

        /* JADX WARN: Removed duplicated region for block: B:105:0x01d4  */
        /* JADX WARN: Removed duplicated region for block: B:51:0x00ea  */
        @Override // java.util.function.Predicate
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        public boolean test(Task task) {
            boolean z;
            Uri uri;
            ComponentName componentName;
            String str;
            if (!task.isLeafTask()) {
                return false;
            }
            if (!ConfigurationContainer.isCompatibleActivityType(this.mActivityType, task.getActivityType())) {
                if (ProtoLogCache.WM_DEBUG_TASKS_enabled) {
                    ProtoLogImpl.d(ProtoLogGroup.WM_DEBUG_TASKS, -373110070, 0, (String) null, new Object[]{String.valueOf(task)});
                }
                return false;
            }
            if (task.voiceSession != null) {
                if (ProtoLogCache.WM_DEBUG_TASKS_enabled) {
                    ProtoLogImpl.d(ProtoLogGroup.WM_DEBUG_TASKS, 51927339, 0, (String) null, new Object[]{String.valueOf(task)});
                }
                return false;
            }
            if (task.mUserId != this.userId) {
                if (ProtoLogCache.WM_DEBUG_TASKS_enabled) {
                    ProtoLogImpl.d(ProtoLogGroup.WM_DEBUG_TASKS, -399343789, 0, (String) null, new Object[]{String.valueOf(task)});
                }
                return false;
            }
            if (task.getWrapper().getExtImpl().isCreateForSingleSplit()) {
                if (ActivityTaskManagerDebugConfig.DEBUG_SWITCH) {
                    Slog.d(RootWindowContainer.TAG, "Skipping " + task + ", create for single split");
                }
                return false;
            }
            ActivityRecord topNonFinishingActivity = task.getTopNonFinishingActivity(false);
            if (topNonFinishingActivity == null || topNonFinishingActivity.finishing || topNonFinishingActivity.mUserId != this.userId || topNonFinishingActivity.launchMode == 3) {
                if (ProtoLogCache.WM_DEBUG_TASKS_enabled) {
                    ProtoLogImpl.d(ProtoLogGroup.WM_DEBUG_TASKS, -1575977269, 0, (String) null, new Object[]{String.valueOf(task), String.valueOf(topNonFinishingActivity)});
                }
                return false;
            }
            if (!ConfigurationContainer.isCompatibleActivityType(topNonFinishingActivity.getActivityType(), this.mActivityType)) {
                if (ProtoLogCache.WM_DEBUG_TASKS_enabled) {
                    ProtoLogImpl.d(ProtoLogGroup.WM_DEBUG_TASKS, -1022146708, 0, (String) null, new Object[]{String.valueOf(task)});
                }
                return false;
            }
            Intent intent = task.intent;
            Intent intent2 = task.affinityIntent;
            if (intent != null && intent.isDocument()) {
                uri = intent.getData();
            } else if (intent2 != null && intent2.isDocument()) {
                uri = intent2.getData();
            } else {
                z = false;
                uri = null;
                if (ProtoLogCache.WM_DEBUG_TASKS_enabled) {
                    ComponentName componentName2 = task.realActivity;
                    ProtoLogImpl.d(ProtoLogGroup.WM_DEBUG_TASKS, 1192413464, 0, (String) null, new Object[]{String.valueOf(componentName2 != null ? componentName2.flattenToShortString() : ""), String.valueOf(task.rootAffinity), String.valueOf(this.mIntent.getComponent().flattenToShortString()), String.valueOf(this.mTaskAffinity)});
                }
                componentName = task.realActivity;
                if (componentName == null && componentName.compareTo(this.cls) == 0 && Objects.equals(this.documentData, uri)) {
                    if (ProtoLogCache.WM_DEBUG_TASKS_enabled) {
                        ProtoLogImpl.d(ProtoLogGroup.WM_DEBUG_TASKS, 1947936538, 0, (String) null, (Object[]) null);
                    }
                    if (ProtoLogCache.WM_DEBUG_TASKS_enabled) {
                        ProtoLogImpl.d(ProtoLogGroup.WM_DEBUG_TASKS, 1557732761, 0, (String) null, new Object[]{String.valueOf(this.mIntent), String.valueOf(topNonFinishingActivity.intent)});
                    }
                    this.mIdealRecord = topNonFinishingActivity;
                    return true;
                }
                if (intent2 == null && intent2.getComponent() != null && intent2.getComponent().compareTo(this.cls) == 0 && Objects.equals(this.documentData, uri)) {
                    if (ProtoLogCache.WM_DEBUG_TASKS_enabled) {
                        ProtoLogImpl.d(ProtoLogGroup.WM_DEBUG_TASKS, 1947936538, 0, (String) null, (Object[]) null);
                    }
                    if (ProtoLogCache.WM_DEBUG_TASKS_enabled) {
                        ProtoLogImpl.d(ProtoLogGroup.WM_DEBUG_TASKS, 1557732761, 0, (String) null, new Object[]{String.valueOf(this.mIntent), String.valueOf(topNonFinishingActivity.intent)});
                    }
                    this.mIdealRecord = topNonFinishingActivity;
                    return true;
                }
                if (this.isDocument && !z && this.mIdealRecord == null && this.mCandidateRecord == null && (str = task.rootAffinity) != null) {
                    if (str.equals(this.mTaskAffinity) && task.isSameRequiredDisplayCategory(this.mInfo)) {
                        if (ProtoLogCache.WM_DEBUG_TASKS_enabled) {
                            ProtoLogImpl.d(ProtoLogGroup.WM_DEBUG_TASKS, 2039056415, 0, (String) null, (Object[]) null);
                        }
                        this.mCandidateRecord = topNonFinishingActivity;
                    }
                } else if (ProtoLogCache.WM_DEBUG_TASKS_enabled) {
                    ProtoLogImpl.d(ProtoLogGroup.WM_DEBUG_TASKS, -775004869, 0, (String) null, new Object[]{String.valueOf(task)});
                }
                return false;
            }
            z = true;
            if (ProtoLogCache.WM_DEBUG_TASKS_enabled) {
            }
            componentName = task.realActivity;
            if (componentName == null) {
            }
            if (intent2 == null) {
            }
            if (this.isDocument) {
            }
            if (ProtoLogCache.WM_DEBUG_TASKS_enabled) {
            }
            return false;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$0(WindowState windowState) {
        if (windowState.mHasSurface) {
            try {
                windowState.mClient.closeSystemDialogs(this.mCloseSystemDialogsReason);
            } catch (RemoteException unused) {
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public RootWindowContainer(WindowManagerService windowManagerService) {
        super(windowManagerService);
        this.mLastWindowFreezeSource = null;
        this.mScreenBrightnessOverride = Float.NaN;
        this.mUserActivityTimeout = -1L;
        this.mUpdateRotation = false;
        this.mObscureApplicationContentOnSecondaryDisplays = false;
        this.mSustainedPerformanceModeEnabled = false;
        this.mSustainedPerformanceModeCurrent = false;
        this.mOrientationChangeComplete = true;
        this.mWallpaperActionPending = false;
        this.mTopFocusedDisplayId = -1;
        this.mTopFocusedAppByProcess = new ArrayMap<>();
        this.mDisplayAccessUIDs = new SparseArray<>();
        this.mUserRootTaskInFront = new SparseIntArray(2);
        this.mSocExt = (IRootWindowContainerSocExt) ExtLoader.type(IRootWindowContainerSocExt.class).base(this).create();
        this.mRootWindowContainerExt = (IRootWindowContainerExt) ExtLoader.type(IRootWindowContainerExt.class).base(this).create();
        this.mSleepTokens = new SparseArray<>();
        this.mDefaultMinSizeOfResizeableTaskDp = -1;
        this.mTaskLayersChanged = true;
        this.mRankTaskLayersRunnable = new RankTaskLayersRunnable();
        this.mAttachApplicationHelper = new AttachApplicationHelper();
        this.mDestroyAllActivitiesRunnable = new AnonymousClass1();
        this.mTmpFindTaskResult = new FindTaskResult();
        this.mCloseSystemDialogsConsumer = new Consumer() { // from class: com.android.server.wm.RootWindowContainer$$ExternalSyntheticLambda41
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                RootWindowContainer.this.lambda$new$0((WindowState) obj);
            }
        };
        this.mFindOrientationChangingFunction = new ToBooleanFunction() { // from class: com.android.server.wm.RootWindowContainer$$ExternalSyntheticLambda42
            public final boolean apply(Object obj) {
                boolean lambda$new$8;
                lambda$new$8 = RootWindowContainer.this.lambda$new$8((WindowState) obj);
                return lambda$new$8;
            }
        };
        this.mFinishDisabledPackageActivitiesHelper = new FinishDisabledPackageActivitiesHelper();
        this.mHandler = new MyHandler(windowManagerService.mH.getLooper());
        ActivityTaskManagerService activityTaskManagerService = windowManagerService.mAtmService;
        this.mService = activityTaskManagerService;
        ActivityTaskSupervisor activityTaskSupervisor = activityTaskManagerService.mTaskSupervisor;
        this.mTaskSupervisor = activityTaskSupervisor;
        activityTaskSupervisor.mRootWindowContainer = this;
        Objects.requireNonNull(activityTaskManagerService);
        this.mDisplayOffTokenAcquirer = new ActivityTaskManagerService.SleepTokenAcquirerImpl(DISPLAY_OFF_SLEEP_TOKEN_TAG);
        this.mDeviceStateController = new DeviceStateController(windowManagerService.mContext, windowManagerService.mGlobalLock);
        this.mDisplayRotationCoordinator = new DisplayRotationCoordinator();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean updateFocusedWindowLocked(int i, boolean z) {
        this.mTopFocusedAppByProcess.clear();
        boolean z2 = false;
        int i2 = -1;
        for (int size = this.mChildren.size() - 1; size >= 0; size--) {
            DisplayContent displayContent = (DisplayContent) this.mChildren.get(size);
            z2 |= displayContent.updateFocusedWindowLocked(i, z, i2);
            WindowState windowState = displayContent.mCurrentFocus;
            if (windowState != null) {
                int i3 = windowState.mSession.mPid;
                if (this.mTopFocusedAppByProcess.get(Integer.valueOf(i3)) == null) {
                    this.mTopFocusedAppByProcess.put(Integer.valueOf(i3), windowState.mActivityRecord);
                }
                if (i2 == -1) {
                    i2 = displayContent.getDisplayId();
                }
            } else if (i2 == -1 && displayContent.mFocusedApp != null) {
                i2 = displayContent.getDisplayId();
            }
        }
        int i4 = i2 != -1 ? i2 : 0;
        if (this.mTopFocusedDisplayId != i4) {
            this.mTopFocusedDisplayId = i4;
            this.mWmService.mInputManager.setFocusedDisplay(i4);
            this.mWmService.mPolicy.setTopFocusedDisplay(i4);
            this.mWmService.mAccessibilityController.setFocusedDisplay(i4);
            if (ProtoLogCache.WM_DEBUG_FOCUS_LIGHT_enabled) {
                ProtoLogImpl.d(ProtoLogGroup.WM_DEBUG_FOCUS_LIGHT, 312030608, 1, (String) null, new Object[]{Long.valueOf(i4)});
            }
        }
        return z2;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public DisplayContent getTopFocusedDisplayContent() {
        DisplayContent displayContent = getDisplayContent(this.mTopFocusedDisplayId);
        return displayContent != null ? displayContent : getDisplayContent(0);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.android.server.wm.WindowContainer
    public void onChildPositionChanged(WindowContainer windowContainer) {
        this.mWmService.updateFocusedWindowLocked(0, !r3.mPerDisplayFocusEnabled);
        this.mTaskSupervisor.updateTopResumedActivityIfNeeded("onChildPositionChanged");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void onSettingsRetrieved() {
        int size = this.mChildren.size();
        for (int i = 0; i < size; i++) {
            DisplayContent displayContent = (DisplayContent) this.mChildren.get(i);
            if (this.mWmService.mDisplayWindowSettings.updateSettingsForDisplay(displayContent)) {
                displayContent.reconfigureDisplayLocked();
                if (displayContent.isDefaultDisplay) {
                    this.mWmService.mAtmService.updateConfigurationLocked(this.mWmService.computeNewConfiguration(displayContent.getDisplayId()), null, false);
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean isLayoutNeeded() {
        int size = this.mChildren.size();
        for (int i = 0; i < size; i++) {
            if (((DisplayContent) this.mChildren.get(i)).isLayoutNeeded()) {
                return true;
            }
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void getWindowsByName(ArrayList<WindowState> arrayList, String str) {
        int i;
        try {
            i = Integer.parseInt(str, 16);
            str = null;
        } catch (RuntimeException unused) {
            i = 0;
        }
        getWindowsByName(arrayList, str, i);
    }

    private void getWindowsByName(final ArrayList<WindowState> arrayList, final String str, final int i) {
        forAllWindows(new Consumer() { // from class: com.android.server.wm.RootWindowContainer$$ExternalSyntheticLambda43
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                RootWindowContainer.lambda$getWindowsByName$1(str, arrayList, i, (WindowState) obj);
            }
        }, true);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$getWindowsByName$1(String str, ArrayList arrayList, int i, WindowState windowState) {
        if (str != null) {
            if (windowState.mAttrs.getTitle().toString().contains(str)) {
                arrayList.add(windowState);
            }
        } else if (System.identityHashCode(windowState) == i) {
            arrayList.add(windowState);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public ActivityRecord getActivityRecord(IBinder iBinder) {
        for (int size = this.mChildren.size() - 1; size >= 0; size--) {
            ActivityRecord activityRecord = ((DisplayContent) this.mChildren.get(size)).getActivityRecord(iBinder);
            if (activityRecord != null) {
                return activityRecord;
            }
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public WindowToken getWindowToken(IBinder iBinder) {
        for (int size = this.mChildren.size() - 1; size >= 0; size--) {
            WindowToken windowToken = ((DisplayContent) this.mChildren.get(size)).getWindowToken(iBinder);
            if (windowToken != null) {
                return windowToken;
            }
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public DisplayContent getWindowTokenDisplay(WindowToken windowToken) {
        if (windowToken == null) {
            return null;
        }
        for (int size = this.mChildren.size() - 1; size >= 0; size--) {
            DisplayContent displayContent = (DisplayContent) this.mChildren.get(size);
            if (displayContent.getWindowToken(windowToken.token) == windowToken) {
                return displayContent;
            }
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.android.server.wm.ConfigurationContainer
    public void dispatchConfigurationToChild(DisplayContent displayContent, Configuration configuration) {
        if (displayContent.isDefaultDisplay) {
            displayContent.performDisplayOverrideConfigUpdate(configuration);
        } else {
            this.mWindowContainerExt.dispatchConfigurationToChild(displayContent, configuration);
            displayContent.onConfigurationChanged(configuration);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void refreshSecureSurfaceState() {
        forAllWindows(new Consumer() { // from class: com.android.server.wm.RootWindowContainer$$ExternalSyntheticLambda25
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                RootWindowContainer.lambda$refreshSecureSurfaceState$2((WindowState) obj);
            }
        }, true);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$refreshSecureSurfaceState$2(WindowState windowState) {
        if (windowState.mHasSurface) {
            windowState.mWinAnimator.setSecureLocked(windowState.isSecureLocked());
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void updateHiddenWhileSuspendedState(final ArraySet<String> arraySet, final boolean z) {
        forAllWindows(new Consumer() { // from class: com.android.server.wm.RootWindowContainer$$ExternalSyntheticLambda36
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                RootWindowContainer.lambda$updateHiddenWhileSuspendedState$3(arraySet, z, (WindowState) obj);
            }
        }, false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$updateHiddenWhileSuspendedState$3(ArraySet arraySet, boolean z, WindowState windowState) {
        if (arraySet.contains(windowState.getOwningPackage())) {
            windowState.setHiddenWhileSuspended(z);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void updateAppOpsState() {
        forAllWindows(new Consumer() { // from class: com.android.server.wm.RootWindowContainer$$ExternalSyntheticLambda49
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                ((WindowState) obj).updateAppOpsState();
            }
        }, false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ boolean lambda$canShowStrictModeViolation$5(int i, WindowState windowState) {
        return windowState.mSession.mPid == i && windowState.isVisible();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean canShowStrictModeViolation(final int i) {
        return getWindow(new Predicate() { // from class: com.android.server.wm.RootWindowContainer$$ExternalSyntheticLambda37
            @Override // java.util.function.Predicate
            public final boolean test(Object obj) {
                boolean lambda$canShowStrictModeViolation$5;
                lambda$canShowStrictModeViolation$5 = RootWindowContainer.lambda$canShowStrictModeViolation$5(i, (WindowState) obj);
                return lambda$canShowStrictModeViolation$5;
            }
        }) != null;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void closeSystemDialogs(String str) {
        this.mCloseSystemDialogsReason = str;
        forAllWindows(this.mCloseSystemDialogsConsumer, false);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean hasPendingLayoutChanges(WindowAnimator windowAnimator) {
        int size = this.mChildren.size();
        boolean z = false;
        for (int i = 0; i < size; i++) {
            int i2 = ((DisplayContent) this.mChildren.get(i)).pendingLayoutChanges;
            if ((i2 & 4) != 0) {
                windowAnimator.mBulkUpdateParams |= 2;
            }
            if (i2 != 0) {
                z = true;
            }
        }
        return z;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean reclaimSomeSurfaceMemory(WindowStateAnimator windowStateAnimator, String str, boolean z) {
        boolean z2;
        WindowSurfaceController windowSurfaceController = windowStateAnimator.mSurfaceController;
        EventLogTags.writeWmNoSurfaceMemory(windowStateAnimator.mWin.toString(), windowStateAnimator.mSession.mPid, str);
        long clearCallingIdentity = Binder.clearCallingIdentity();
        try {
            Slog.i(TAG, "Out of memory for surface!  Looking for leaks...");
            int size = this.mChildren.size();
            boolean z3 = false;
            for (int i = 0; i < size; i++) {
                z3 |= ((DisplayContent) this.mChildren.get(i)).destroyLeakedSurfaces();
            }
            if (z3) {
                z2 = false;
            } else {
                Slog.w(TAG, "No leaked surfaces; killing applications!");
                final SparseIntArray sparseIntArray = new SparseIntArray();
                z2 = false;
                for (int i2 = 0; i2 < size; i2++) {
                    ((DisplayContent) this.mChildren.get(i2)).forAllWindows(new Consumer() { // from class: com.android.server.wm.RootWindowContainer$$ExternalSyntheticLambda17
                        @Override // java.util.function.Consumer
                        public final void accept(Object obj) {
                            RootWindowContainer.this.lambda$reclaimSomeSurfaceMemory$6(sparseIntArray, (WindowState) obj);
                        }
                    }, false);
                    if (sparseIntArray.size() > 0) {
                        int size2 = sparseIntArray.size();
                        int[] iArr = new int[size2];
                        for (int i3 = 0; i3 < size2; i3++) {
                            iArr[i3] = sparseIntArray.keyAt(i3);
                        }
                        try {
                            try {
                                if (this.mWmService.mActivityManager.killPids(iArr, "Free memory", z)) {
                                    z2 = true;
                                }
                            } catch (RemoteException unused) {
                            }
                        } catch (RemoteException unused2) {
                        }
                    }
                }
            }
            if (z3 || z2) {
                Slog.w(TAG, "Looks like we have reclaimed some memory, clearing surface for retry.");
                if (windowSurfaceController != null) {
                    if (ProtoLogCache.WM_SHOW_SURFACE_ALLOC_enabled) {
                        ProtoLogImpl.i(ProtoLogGroup.WM_SHOW_SURFACE_ALLOC, 399841913, 0, (String) null, new Object[]{String.valueOf(windowStateAnimator.mWin)});
                    }
                    SurfaceControl.Transaction transaction = this.mWmService.mTransactionFactory.get();
                    windowStateAnimator.destroySurface(transaction);
                    transaction.apply();
                    ActivityRecord activityRecord = windowStateAnimator.mWin.mActivityRecord;
                    if (activityRecord != null) {
                        activityRecord.removeStartingWindow();
                    }
                }
                try {
                    windowStateAnimator.mWin.mClient.dispatchGetNewSurface();
                } catch (RemoteException unused3) {
                }
            }
            return z3 || z2;
        } finally {
            Binder.restoreCallingIdentity(clearCallingIdentity);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$reclaimSomeSurfaceMemory$6(SparseIntArray sparseIntArray, WindowState windowState) {
        if (this.mWmService.mForceRemoves.contains(windowState)) {
            return;
        }
        WindowStateAnimator windowStateAnimator = windowState.mWinAnimator;
        if (windowStateAnimator.mSurfaceController != null) {
            int i = windowStateAnimator.mSession.mPid;
            sparseIntArray.append(i, i);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void performSurfacePlacement() {
        Trace.traceBegin(32L, "performSurfacePlacement:" + Debug.getCallers(3));
        try {
            performSurfacePlacementNoTrace();
        } finally {
            Trace.traceEnd(32L);
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:100:0x024d, code lost:
    
        if (r13.mWmService.mWaitingForDrawnCallbacks.isEmpty() == false) goto L105;
     */
    /* JADX WARN: Code restructure failed: missing block: B:102:0x0251, code lost:
    
        if (r13.mOrientationChangeComplete == false) goto L106;
     */
    /* JADX WARN: Code restructure failed: missing block: B:104:0x0257, code lost:
    
        if (isLayoutNeeded() != false) goto L106;
     */
    /* JADX WARN: Code restructure failed: missing block: B:106:0x025b, code lost:
    
        if (r13.mUpdateRotation != false) goto L106;
     */
    /* JADX WARN: Code restructure failed: missing block: B:107:0x0262, code lost:
    
        forAllDisplays(new com.android.server.wm.RootWindowContainer$$ExternalSyntheticLambda48());
        r13.mWmService.enableScreenIfNeededLocked();
        r13.mWmService.scheduleAnimationLocked();
     */
    /* JADX WARN: Code restructure failed: missing block: B:108:0x0276, code lost:
    
        if (com.android.server.wm.WindowManagerDebugConfig.DEBUG_WINDOW_TRACE == false) goto L127;
     */
    /* JADX WARN: Code restructure failed: missing block: B:109:0x0278, code lost:
    
        android.util.Slog.e(com.android.server.wm.RootWindowContainer.TAG, "performSurfacePlacementInner exit");
     */
    /* JADX WARN: Code restructure failed: missing block: B:110:0x027d, code lost:
    
        return;
     */
    /* JADX WARN: Code restructure failed: missing block: B:112:?, code lost:
    
        return;
     */
    /* JADX WARN: Code restructure failed: missing block: B:113:0x025d, code lost:
    
        r13.mWmService.checkDrawnWindowsLocked();
     */
    /* JADX WARN: Code restructure failed: missing block: B:123:0x008b, code lost:
    
        if (com.android.server.wm.WindowManagerDebugConfig.SHOW_LIGHT_TRANSACTIONS == false) goto L23;
     */
    /* JADX WARN: Code restructure failed: missing block: B:16:0x008e, code lost:
    
        r13.mWmService.mAtmService.mTaskOrganizerController.dispatchPendingEvents();
        r13.mWmService.mAtmService.mTaskFragmentOrganizerController.dispatchPendingEvents();
        r13.mWmService.mSyncEngine.onSurfacePlacement();
        r13.mWmService.mAnimator.executeAfterPrepareSurfacesRunnables();
        checkAppTransitionReady(r7);
        r0 = r13.mWmService.getRecentsAnimationController();
     */
    /* JADX WARN: Code restructure failed: missing block: B:17:0x00b7, code lost:
    
        if (r0 == null) goto L26;
     */
    /* JADX WARN: Code restructure failed: missing block: B:18:0x00b9, code lost:
    
        r0.checkAnimationReady(r5.mWallpaperController);
     */
    /* JADX WARN: Code restructure failed: missing block: B:19:0x00be, code lost:
    
        r13.mWmService.mAtmService.mBackNavigationController.checkAnimationReady(r5.mWallpaperController);
        r13.mRootWindowContainerExt.checkAnimationReady();
        r0 = 0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:21:0x00d6, code lost:
    
        if (r0 >= r13.mChildren.size()) goto L116;
     */
    /* JADX WARN: Code restructure failed: missing block: B:22:0x00d8, code lost:
    
        r1 = (com.android.server.wm.DisplayContent) r13.mChildren.get(r0);
     */
    /* JADX WARN: Code restructure failed: missing block: B:23:0x00e2, code lost:
    
        if (r1.mWallpaperMayChange == false) goto L118;
     */
    /* JADX WARN: Code restructure failed: missing block: B:25:0x00e6, code lost:
    
        if (com.android.server.wm.ProtoLogCache.WM_DEBUG_WALLPAPER_enabled == false) goto L34;
     */
    /* JADX WARN: Code restructure failed: missing block: B:26:0x00e8, code lost:
    
        com.android.internal.protolog.ProtoLogImpl.v(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_WALLPAPER, 535103992, 0, (java.lang.String) null, (java.lang.Object[]) null);
     */
    /* JADX WARN: Code restructure failed: missing block: B:27:0x00f0, code lost:
    
        r9 = r1.pendingLayoutChanges | 4;
        r1.pendingLayoutChanges = r9;
     */
    /* JADX WARN: Code restructure failed: missing block: B:28:0x00f8, code lost:
    
        if (com.android.server.wm.WindowManagerDebugConfig.DEBUG_LAYOUT_REPEATS == false) goto L119;
     */
    /* JADX WARN: Code restructure failed: missing block: B:29:0x00fa, code lost:
    
        r7.debugLayoutRepeats("WallpaperMayChange", r9);
     */
    /* JADX WARN: Code restructure failed: missing block: B:31:0x00ff, code lost:
    
        r0 = r0 + 1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:35:0x0102, code lost:
    
        r0 = r13.mWmService;
     */
    /* JADX WARN: Code restructure failed: missing block: B:36:0x0107, code lost:
    
        if (r0.mFocusMayChange == false) goto L41;
     */
    /* JADX WARN: Code restructure failed: missing block: B:37:0x0109, code lost:
    
        r0.mFocusMayChange = false;
        r0.updateFocusedWindowLocked(2, false);
     */
    /* JADX WARN: Code restructure failed: missing block: B:39:0x0112, code lost:
    
        if (isLayoutNeeded() == false) goto L46;
     */
    /* JADX WARN: Code restructure failed: missing block: B:40:0x0114, code lost:
    
        r0 = r5.pendingLayoutChanges | 1;
        r5.pendingLayoutChanges = r0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:41:0x011b, code lost:
    
        if (com.android.server.wm.WindowManagerDebugConfig.DEBUG_LAYOUT_REPEATS == false) goto L46;
     */
    /* JADX WARN: Code restructure failed: missing block: B:42:0x011d, code lost:
    
        r7.debugLayoutRepeats("mLayoutNeeded", r0);
     */
    /* JADX WARN: Code restructure failed: missing block: B:43:0x0122, code lost:
    
        handleResizingWindows();
     */
    /* JADX WARN: Code restructure failed: missing block: B:44:0x0129, code lost:
    
        if (r13.mWmService.mDisplayFrozen == false) goto L51;
     */
    /* JADX WARN: Code restructure failed: missing block: B:46:0x012d, code lost:
    
        if (com.android.server.wm.ProtoLogCache.WM_DEBUG_ORIENTATION_enabled == false) goto L51;
     */
    /* JADX WARN: Code restructure failed: missing block: B:47:0x012f, code lost:
    
        com.android.internal.protolog.ProtoLogImpl.v(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_ORIENTATION, -666510420, 3, (java.lang.String) null, new java.lang.Object[]{java.lang.Boolean.valueOf(r13.mOrientationChangeComplete)});
     */
    /* JADX WARN: Code restructure failed: missing block: B:49:0x0143, code lost:
    
        if (r13.mOrientationChangeComplete == false) goto L57;
     */
    /* JADX WARN: Code restructure failed: missing block: B:50:0x0145, code lost:
    
        r0 = r13.mWmService;
     */
    /* JADX WARN: Code restructure failed: missing block: B:51:0x0149, code lost:
    
        if (r0.mWindowsFreezingScreen == 0) goto L56;
     */
    /* JADX WARN: Code restructure failed: missing block: B:52:0x014b, code lost:
    
        r0.mWindowsFreezingScreen = 0;
        r0.mLastFinishedFreezeSource = r13.mLastWindowFreezeSource;
        r0.mH.removeMessages(11);
     */
    /* JADX WARN: Code restructure failed: missing block: B:53:0x0158, code lost:
    
        r13.mWmService.stopFreezingDisplayLocked();
     */
    /* JADX WARN: Code restructure failed: missing block: B:54:0x015d, code lost:
    
        r0 = r13.mWmService.mDestroySurface.size();
     */
    /* JADX WARN: Code restructure failed: missing block: B:55:0x0165, code lost:
    
        if (r0 <= 0) goto L72;
     */
    /* JADX WARN: Code restructure failed: missing block: B:56:0x0167, code lost:
    
        r1 = (com.android.server.wm.IWindowManagerServiceExt) system.ext.loader.core.ExtLoader.type(com.android.server.wm.IWindowManagerServiceExt.class).create();
     */
    /* JADX WARN: Code restructure failed: missing block: B:57:0x0173, code lost:
    
        r0 = r0 - 1;
        r3 = r13.mWmService.mDestroySurface.get(r0);
     */
    /* JADX WARN: Code restructure failed: missing block: B:58:0x0189, code lost:
    
        if (r13.mRootWindowContainerExt.shouldWindowSurfaceSaved(r3, r3.getDisplayContent()) == false) goto L63;
     */
    /* JADX WARN: Code restructure failed: missing block: B:59:0x018c, code lost:
    
        r1.getDestroySavedSurface().remove(r3);
        r3.mDestroying = false;
        r5 = r3.getDisplayContent();
     */
    /* JADX WARN: Code restructure failed: missing block: B:60:0x019b, code lost:
    
        if (r5.mInputMethodWindow != r3) goto L66;
     */
    /* JADX WARN: Code restructure failed: missing block: B:61:0x019d, code lost:
    
        r13.mWmService.getWrapper().getExtImpl().setInputMethodWindow(r5, null);
     */
    /* JADX WARN: Code restructure failed: missing block: B:63:0x01b0, code lost:
    
        if (r5.mWallpaperController.isWallpaperTarget(r3) == false) goto L69;
     */
    /* JADX WARN: Code restructure failed: missing block: B:64:0x01b2, code lost:
    
        r5.pendingLayoutChanges |= 4;
     */
    /* JADX WARN: Code restructure failed: missing block: B:65:0x01b8, code lost:
    
        r3.destroySurfaceUnchecked();
     */
    /* JADX WARN: Code restructure failed: missing block: B:67:0x01bb, code lost:
    
        if (r0 > 0) goto L123;
     */
    /* JADX WARN: Code restructure failed: missing block: B:69:0x01bd, code lost:
    
        r13.mWmService.mDestroySurface.clear();
        r13.mWmService.mDestroySurface.addAll(r1.getDestroySavedSurface());
     */
    /* JADX WARN: Code restructure failed: missing block: B:72:0x01cf, code lost:
    
        r0 = 0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:74:0x01d6, code lost:
    
        if (r0 >= r13.mChildren.size()) goto L124;
     */
    /* JADX WARN: Code restructure failed: missing block: B:75:0x01d8, code lost:
    
        r1 = (com.android.server.wm.DisplayContent) r13.mChildren.get(r0);
     */
    /* JADX WARN: Code restructure failed: missing block: B:76:0x01e2, code lost:
    
        if (r1.pendingLayoutChanges == 0) goto L126;
     */
    /* JADX WARN: Code restructure failed: missing block: B:77:0x01e4, code lost:
    
        r1.setLayoutNeeded();
     */
    /* JADX WARN: Code restructure failed: missing block: B:79:0x01e7, code lost:
    
        r0 = r0 + 1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:83:0x01ee, code lost:
    
        if (r13.mWmService.mDisplayFrozen != false) goto L88;
     */
    /* JADX WARN: Code restructure failed: missing block: B:84:0x01f0, code lost:
    
        r13.mRootWindowContainerExt.updatePendingScreenBrightnessOverrideMap();
        r0 = r13.mScreenBrightnessOverride;
     */
    /* JADX WARN: Code restructure failed: missing block: B:85:0x01fa, code lost:
    
        if (r0 < 0.0f) goto L87;
     */
    /* JADX WARN: Code restructure failed: missing block: B:87:0x0200, code lost:
    
        if (r0 <= 1.0f) goto L86;
     */
    /* JADX WARN: Code restructure failed: missing block: B:88:0x0203, code lost:
    
        r2 = r0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:89:0x0204, code lost:
    
        r13.mHandler.obtainMessage(1, java.lang.Float.floatToIntBits(r2), 0).sendToTarget();
        r13.mHandler.obtainMessage(2, java.lang.Long.valueOf(r13.mUserActivityTimeout)).sendToTarget();
     */
    /* JADX WARN: Code restructure failed: missing block: B:90:0x0220, code lost:
    
        r0 = r13.mSustainedPerformanceModeCurrent;
     */
    /* JADX WARN: Code restructure failed: missing block: B:91:0x0224, code lost:
    
        if (r0 == r13.mSustainedPerformanceModeEnabled) goto L91;
     */
    /* JADX WARN: Code restructure failed: missing block: B:92:0x0226, code lost:
    
        r13.mSustainedPerformanceModeEnabled = r0;
        r13.mWmService.mPowerManagerInternal.setPowerMode(2, r0);
     */
    /* JADX WARN: Code restructure failed: missing block: B:94:0x0231, code lost:
    
        if (r13.mUpdateRotation == false) goto L97;
     */
    /* JADX WARN: Code restructure failed: missing block: B:96:0x0235, code lost:
    
        if (com.android.server.wm.ProtoLogCache.WM_DEBUG_ORIENTATION_enabled == false) goto L96;
     */
    /* JADX WARN: Code restructure failed: missing block: B:97:0x0237, code lost:
    
        com.android.internal.protolog.ProtoLogImpl.d(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_ORIENTATION, -1103115659, 0, (java.lang.String) null, (java.lang.Object[]) null);
     */
    /* JADX WARN: Code restructure failed: missing block: B:98:0x023f, code lost:
    
        r13.mUpdateRotation = updateRotationUnchecked();
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    void performSurfacePlacementNoTrace() {
        if (WindowManagerDebugConfig.DEBUG_WINDOW_TRACE) {
            Slog.v(TAG, "performSurfacePlacementInner: entry. Called by " + Debug.getCallers(3));
        }
        WindowManagerService windowManagerService = this.mWmService;
        if (windowManagerService.mFocusMayChange) {
            windowManagerService.mFocusMayChange = false;
            windowManagerService.updateFocusedWindowLocked(3, false);
        }
        float f = Float.NaN;
        this.mScreenBrightnessOverride = Float.NaN;
        this.mRootWindowContainerExt.hookPerformSurfacePlacementNoTraceInit();
        this.mUserActivityTimeout = -1L;
        this.mObscureApplicationContentOnSecondaryDisplays = false;
        this.mSustainedPerformanceModeCurrent = false;
        WindowManagerService windowManagerService2 = this.mWmService;
        windowManagerService2.mTransactionSequence++;
        DisplayContent defaultDisplayContentLocked = windowManagerService2.getDefaultDisplayContentLocked();
        WindowSurfacePlacer windowSurfacePlacer = this.mWmService.mWindowPlacerLocked;
        if (WindowManagerDebugConfig.SHOW_LIGHT_TRANSACTIONS) {
            Slog.i(TAG, ">>> OPEN TRANSACTION performLayoutAndPlaceSurfaces");
        }
        Trace.traceBegin(32L, "applySurfaceChanges");
        this.mWmService.openSurfaceTransaction();
        try {
            try {
                applySurfaceChangesTransaction();
            } catch (RuntimeException e) {
                Slog.wtf(TAG, "Unhandled exception in Window Manager", e);
                this.mWmService.closeSurfaceTransaction("performLayoutAndPlaceSurfaces");
                Trace.traceEnd(32L);
            }
        } finally {
            this.mWmService.closeSurfaceTransaction("performLayoutAndPlaceSurfaces");
            Trace.traceEnd(32L);
            if (WindowManagerDebugConfig.SHOW_LIGHT_TRANSACTIONS) {
                Slog.i(TAG, "<<< CLOSE TRANSACTION performLayoutAndPlaceSurfaces");
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$performSurfacePlacementNoTrace$7(DisplayContent displayContent) {
        displayContent.getInputMonitor().updateInputWindowsLw(true);
        displayContent.updateSystemGestureExclusion();
        displayContent.updateKeepClearAreas();
        displayContent.updateTouchExcludeRegion();
    }

    private void checkAppTransitionReady(WindowSurfacePlacer windowSurfacePlacer) {
        for (int size = this.mChildren.size() - 1; size >= 0; size--) {
            DisplayContent displayContent = (DisplayContent) this.mChildren.get(size);
            if (displayContent.mAppTransition.isReady()) {
                displayContent.mAppTransitionController.handleAppTransitionReady();
                if (WindowManagerDebugConfig.DEBUG_LAYOUT_REPEATS) {
                    windowSurfacePlacer.debugLayoutRepeats("after handleAppTransitionReady", displayContent.pendingLayoutChanges);
                }
            }
            if (displayContent.mAppTransition.isRunning() && !displayContent.isAppTransitioning()) {
                displayContent.handleAnimatingStoppedAndTransition();
                if (WindowManagerDebugConfig.DEBUG_LAYOUT_REPEATS) {
                    windowSurfacePlacer.debugLayoutRepeats("after handleAnimStopAndXitionLock", displayContent.pendingLayoutChanges);
                }
            }
        }
    }

    private void applySurfaceChangesTransaction() {
        DisplayContent displayContent = this.mDefaultDisplay;
        DisplayInfo displayInfo = displayContent.getDisplayInfo();
        int i = displayInfo.logicalWidth;
        int i2 = displayInfo.logicalHeight;
        SurfaceControl.Transaction syncTransaction = displayContent.getSyncTransaction();
        Watermark watermark = this.mWmService.mWatermark;
        if (watermark != null) {
            watermark.positionSurface(i, i2, syncTransaction);
        }
        this.mRootWindowContainerExt.positionSurface(i, i2);
        StrictModeFlash strictModeFlash = this.mWmService.mStrictModeFlash;
        if (strictModeFlash != null) {
            strictModeFlash.positionSurface(i, i2, syncTransaction);
        }
        EmulatorDisplayOverlay emulatorDisplayOverlay = this.mWmService.mEmulatorDisplayOverlay;
        if (emulatorDisplayOverlay != null) {
            emulatorDisplayOverlay.positionSurface(i, i2, displayContent.getRotation(), syncTransaction);
        }
        int size = this.mChildren.size();
        for (int i3 = 0; i3 < size; i3++) {
            ((DisplayContent) this.mChildren.get(i3)).applySurfaceChangesTransaction();
        }
        if (this.mWmService.mDisplayEnabled || this.mRootWindowContainerExt.isNotLargeFoldDevice()) {
            this.mWmService.mDisplayManagerInternal.performTraversal(syncTransaction);
        }
        if (syncTransaction != displayContent.mSyncTransaction) {
            SurfaceControl.mergeToGlobalTransaction(syncTransaction);
        }
    }

    private void handleResizingWindows() {
        for (int size = this.mWmService.mResizingWindows.size() - 1; size >= 0; size--) {
            WindowState windowState = this.mWmService.mResizingWindows.get(size);
            if (!windowState.mAppFreezing && !windowState.getDisplayContent().mWaitingForConfig) {
                windowState.reportResized();
                this.mWmService.mResizingWindows.remove(size);
            }
        }
        if (this.mWmService.mResizingWindows.isEmpty()) {
            this.mRootWindowContainerExt.handleResizingWindows();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: Code restructure failed: missing block: B:47:0x00d6, code lost:
    
        if (r3 == 2009) goto L45;
     */
    /* JADX WARN: Removed duplicated region for block: B:36:0x00de  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public boolean handleNotObscuredLocked(WindowState windowState, boolean z, boolean z2) {
        WindowManager.LayoutParams layoutParams = windowState.mAttrs;
        int i = layoutParams.flags;
        boolean isOnScreen = windowState.isOnScreen();
        boolean isDisplayed = windowState.isDisplayed();
        int i2 = layoutParams.privateFlags;
        if (ProtoLogCache.WM_DEBUG_KEEP_SCREEN_ON_enabled) {
            ProtoLogImpl.d(ProtoLogGroup.WM_DEBUG_KEEP_SCREEN_ON, -481924678, 508, (String) null, new Object[]{String.valueOf(windowState), Boolean.valueOf(windowState.mHasSurface), Boolean.valueOf(isOnScreen), Boolean.valueOf(windowState.isDisplayed()), Long.valueOf(windowState.mAttrs.userActivityTimeout)});
        }
        if (windowState.mHasSurface && isOnScreen && !z2) {
            long j = windowState.mAttrs.userActivityTimeout;
            if (j >= 0 && this.mUserActivityTimeout < 0) {
                this.mUserActivityTimeout = j;
                if (ProtoLogCache.WM_DEBUG_KEEP_SCREEN_ON_enabled) {
                    ProtoLogImpl.d(ProtoLogGroup.WM_DEBUG_KEEP_SCREEN_ON, 221540118, 1, (String) null, new Object[]{Long.valueOf(j)});
                }
            }
        }
        boolean z3 = false;
        if (windowState.mHasSurface && isDisplayed) {
            this.mRootWindowContainerExt.hookHandleNotObscuredLocked(windowState, z, z2, this.mScreenBrightnessOverride);
            if (!z2 && windowState.mAttrs.screenBrightness >= 0.0f && Float.isNaN(this.mScreenBrightnessOverride)) {
                this.mScreenBrightnessOverride = windowState.mAttrs.screenBrightness;
            }
            int i3 = layoutParams.type;
            DisplayContent displayContent = windowState.getDisplayContent();
            if (displayContent == null || !displayContent.isDefaultDisplay) {
                if (displayContent != null) {
                    if (this.mObscureApplicationContentOnSecondaryDisplays) {
                        if (!displayContent.isKeyguardAlwaysUnlocked()) {
                            if (z) {
                            }
                        }
                    }
                }
                if ((65536 & i2) != 0) {
                    this.mSustainedPerformanceModeCurrent = true;
                }
            } else if ((windowState.isDreamWindow() || this.mWmService.mPolicy.isKeyguardShowing()) && this.mRootWindowContainerExt.shouldObscureApplicationContentOnSecondaryDisplay()) {
                this.mObscureApplicationContentOnSecondaryDisplays = true;
            }
            z3 = true;
            if ((65536 & i2) != 0) {
            }
        }
        return z3;
    }

    boolean updateRotationUnchecked() {
        boolean z = false;
        for (int size = this.mChildren.size() - 1; size >= 0; size--) {
            if (((DisplayContent) this.mChildren.get(size)).getDisplayRotation().updateRotationAndSendNewConfigIfChanged()) {
                z = true;
            }
        }
        return z;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ boolean lambda$new$8(WindowState windowState) {
        if (!windowState.getOrientationChanging() || windowState.isDrawn() || this.mRootWindowContainerExt.shouldSkipUnFreezeCheck(windowState)) {
            return false;
        }
        this.mTmpOrientationChangeComplete = false;
        return true;
    }

    void updateOrientationChangeIfNeeded() {
        if (this.mOrientationChangeComplete) {
            return;
        }
        this.mTmpOrientationChangeComplete = true;
        forAllWindows(this.mFindOrientationChangingFunction, true);
        this.mOrientationChangeComplete = this.mTmpOrientationChangeComplete;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean copyAnimToLayoutParams() {
        boolean z;
        WindowManagerService windowManagerService = this.mWmService;
        WindowAnimator windowAnimator = windowManagerService.mAnimator;
        int i = windowAnimator.mBulkUpdateParams;
        if ((i & 1) != 0) {
            this.mUpdateRotation = true;
            z = true;
        } else {
            z = false;
        }
        if (this.mOrientationChangeComplete) {
            this.mLastWindowFreezeSource = windowAnimator.mLastWindowFreezeSource;
            if (windowManagerService.mWindowsFreezingScreen != 0) {
                z = true;
            }
        }
        if ((i & 2) != 0) {
            this.mWallpaperActionPending = true;
        }
        return z;
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
    private final class MyHandler extends Handler {
        public MyHandler(Looper looper) {
            super(looper);
        }

        @Override // android.os.Handler
        public void handleMessage(Message message) {
            int i = message.what;
            if (i == 1) {
                RootWindowContainer.this.mWmService.mPowerManagerInternal.setScreenBrightnessOverrideFromWindowManager(Float.intBitsToFloat(message.arg1));
            } else {
                if (i != 2) {
                    return;
                }
                RootWindowContainer.this.mWmService.mPowerManagerInternal.setUserActivityTimeoutOverrideFromWindowManager(((Long) message.obj).longValue());
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void dumpDisplayContents(PrintWriter printWriter) {
        printWriter.println("WINDOW MANAGER DISPLAY CONTENTS (dumpsys window displays)");
        if (this.mWmService.mDisplayReady) {
            int size = this.mChildren.size();
            for (int i = 0; i < size; i++) {
                ((DisplayContent) this.mChildren.get(i)).dump(printWriter, "  ", true);
            }
            return;
        }
        printWriter.println("  NO DISPLAY");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void dumpTopFocusedDisplayId(PrintWriter printWriter) {
        printWriter.print("  mTopFocusedDisplayId=");
        printWriter.println(this.mTopFocusedDisplayId);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void dumpLayoutNeededDisplayIds(PrintWriter printWriter) {
        if (isLayoutNeeded()) {
            printWriter.print("  mLayoutNeeded on displays=");
            int size = this.mChildren.size();
            for (int i = 0; i < size; i++) {
                DisplayContent displayContent = (DisplayContent) this.mChildren.get(i);
                if (displayContent.isLayoutNeeded()) {
                    printWriter.print(displayContent.getDisplayId());
                }
            }
            printWriter.println();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void dumpWindowsNoHeader(final PrintWriter printWriter, final boolean z, final ArrayList<WindowState> arrayList) {
        final int[] iArr = new int[1];
        forAllWindows(new Consumer() { // from class: com.android.server.wm.RootWindowContainer$$ExternalSyntheticLambda46
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                RootWindowContainer.lambda$dumpWindowsNoHeader$9(arrayList, printWriter, iArr, z, (WindowState) obj);
            }
        }, true);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$dumpWindowsNoHeader$9(ArrayList arrayList, PrintWriter printWriter, int[] iArr, boolean z, WindowState windowState) {
        if (arrayList == null || arrayList.contains(windowState)) {
            printWriter.println("  Window #" + iArr[0] + " " + windowState + ":");
            windowState.dump(printWriter, "    ", z || arrayList != null);
            iArr[0] = iArr[0] + 1;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void dumpTokens(PrintWriter printWriter, boolean z) {
        printWriter.println("  All tokens:");
        for (int size = this.mChildren.size() - 1; size >= 0; size--) {
            ((DisplayContent) this.mChildren.get(size)).dumpTokens(printWriter, z);
        }
    }

    @Override // com.android.server.wm.WindowContainer, com.android.server.wm.ConfigurationContainer
    public void dumpDebug(ProtoOutputStream protoOutputStream, long j, int i) {
        if (i != 2 || isVisible()) {
            long start = protoOutputStream.start(j);
            super.dumpDebug(protoOutputStream, 1146756268033L, i);
            this.mTaskSupervisor.getKeyguardController().dumpDebug(protoOutputStream, 1146756268037L);
            protoOutputStream.write(1133871366150L, this.mTaskSupervisor.mRecentTasks.isRecentsComponentHomeActivity(this.mCurrentUser));
            protoOutputStream.end(start);
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.android.server.wm.WindowContainer
    public void removeChild(DisplayContent displayContent) {
        super.removeChild((RootWindowContainer) displayContent);
        if (this.mTopFocusedDisplayId == displayContent.getDisplayId()) {
            this.mWmService.updateFocusedWindowLocked(0, true);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void forAllDisplays(Consumer<DisplayContent> consumer) {
        for (int size = this.mChildren.size() - 1; size >= 0; size--) {
            consumer.accept((DisplayContent) this.mChildren.get(size));
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void forAllDisplayPolicies(Consumer<DisplayPolicy> consumer) {
        for (int size = this.mChildren.size() - 1; size >= 0; size--) {
            consumer.accept(((DisplayContent) this.mChildren.get(size)).getDisplayPolicy());
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public WindowState getCurrentInputMethodWindow() {
        for (int size = this.mChildren.size() - 1; size >= 0; size--) {
            WindowState windowState = ((DisplayContent) this.mChildren.get(size)).mInputMethodWindow;
            if (windowState != null) {
                return windowState;
            }
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void getDisplayContextsWithNonToastVisibleWindows(final int i, List<Context> list) {
        if (list == null) {
            return;
        }
        for (int size = this.mChildren.size() - 1; size >= 0; size--) {
            DisplayContent displayContent = (DisplayContent) this.mChildren.get(size);
            if (displayContent.getWindow(new Predicate() { // from class: com.android.server.wm.RootWindowContainer$$ExternalSyntheticLambda45
                @Override // java.util.function.Predicate
                public final boolean test(Object obj) {
                    boolean lambda$getDisplayContextsWithNonToastVisibleWindows$10;
                    lambda$getDisplayContextsWithNonToastVisibleWindows$10 = RootWindowContainer.lambda$getDisplayContextsWithNonToastVisibleWindows$10(i, (WindowState) obj);
                    return lambda$getDisplayContextsWithNonToastVisibleWindows$10;
                }
            }) != null) {
                list.add(displayContent.getDisplayUiContext());
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ boolean lambda$getDisplayContextsWithNonToastVisibleWindows$10(int i, WindowState windowState) {
        return i == windowState.mSession.mPid && windowState.isVisibleNow() && windowState.mAttrs.type != 2005;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Context getDisplayUiContext(int i) {
        if (getDisplayContent(i) != null) {
            return getDisplayContent(i).getDisplayUiContext();
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setWindowManager(WindowManagerService windowManagerService) {
        this.mWindowManager = windowManagerService;
        DisplayManager displayManager = (DisplayManager) this.mService.mContext.getSystemService(DisplayManager.class);
        this.mDisplayManager = displayManager;
        displayManager.registerDisplayListener(this, this.mService.mUiHandler);
        this.mDisplayManagerInternal = (DisplayManagerInternal) LocalServices.getService(DisplayManagerInternal.class);
        for (Display display : this.mDisplayManager.getDisplays()) {
            DisplayContent displayContent = new DisplayContent(display, this, this.mDeviceStateController);
            addChild((RootWindowContainer) displayContent, ChargerErrorCode.ERR_FILE_FAILURE_ACCESS);
            if (displayContent.mDisplayId == 0) {
                this.mDefaultDisplay = displayContent;
            }
        }
        TaskDisplayArea defaultTaskDisplayArea = getDefaultTaskDisplayArea();
        defaultTaskDisplayArea.getOrCreateRootHomeTask(true);
        positionChildAt(Integer.MAX_VALUE, defaultTaskDisplayArea.mDisplayContent, false);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void onDisplayManagerReceivedDeviceState(int i) {
        this.mDeviceStateController.onDeviceStateReceivedByDisplayManager(i);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public DisplayContent getDefaultDisplay() {
        return this.mDefaultDisplay;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public DisplayRotationCoordinator getDisplayRotationCoordinator() {
        return this.mDisplayRotationCoordinator;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public TaskDisplayArea getDefaultTaskDisplayArea() {
        return this.mDefaultDisplay.getDefaultTaskDisplayArea();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public DisplayContent getDisplayContent(String str) {
        for (int childCount = getChildCount() - 1; childCount >= 0; childCount--) {
            DisplayContent childAt = getChildAt(childCount);
            if (childAt.mDisplay.isValid() && childAt.mDisplay.getUniqueId() != null && childAt.mDisplay.getUniqueId().equals(str)) {
                return childAt;
            }
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public DisplayContent getDisplayContent(int i) {
        for (int childCount = getChildCount() - 1; childCount >= 0; childCount--) {
            DisplayContent childAt = getChildAt(childCount);
            if (childAt.mDisplayId == i) {
                return childAt;
            }
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public DisplayContent getDisplayContentOrCreate(int i) {
        Display display;
        DisplayContent displayContent = getDisplayContent(i);
        if (displayContent != null) {
            return displayContent;
        }
        DisplayManager displayManager = this.mDisplayManager;
        if (displayManager == null || (display = displayManager.getDisplay(i)) == null) {
            return null;
        }
        DisplayContent displayContent2 = new DisplayContent(display, this, this.mDeviceStateController);
        if (displayContent2.getWrapper().getExtImpl().isActivityPreloadDisplay(displayContent2)) {
            displayContent2.mDontMoveToTop = true;
        }
        addChild((RootWindowContainer) displayContent2, ChargerErrorCode.ERR_FILE_FAILURE_ACCESS);
        return displayContent2;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public ActivityRecord getDefaultDisplayHomeActivityForUser(int i) {
        return getDefaultTaskDisplayArea().getHomeActivityForUser(i);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean startHomeOnAllDisplays(int i, String str) {
        boolean z = false;
        for (int childCount = getChildCount() - 1; childCount >= 0; childCount--) {
            z |= startHomeOnDisplay(i, str, getChildAt(childCount).mDisplayId);
        }
        return z;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void startHomeOnEmptyDisplays(final String str) {
        forAllTaskDisplayAreas(new Consumer() { // from class: com.android.server.wm.RootWindowContainer$$ExternalSyntheticLambda35
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                RootWindowContainer.this.lambda$startHomeOnEmptyDisplays$11(str, (TaskDisplayArea) obj);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$startHomeOnEmptyDisplays$11(String str, TaskDisplayArea taskDisplayArea) {
        if (taskDisplayArea.topRunningActivity() == null) {
            startHomeOnTaskDisplayArea(this.mWmService.getUserAssignedToDisplay(taskDisplayArea.getDisplayId()), str, taskDisplayArea, false, false);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean startHomeOnDisplay(int i, String str, int i2) {
        return startHomeOnDisplay(i, str, i2, false, false);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean startHomeOnDisplay(final int i, final String str, int i2, final boolean z, final boolean z2) {
        if (i2 == -1) {
            Task topDisplayFocusedRootTask = getTopDisplayFocusedRootTask();
            i2 = topDisplayFocusedRootTask != null ? topDisplayFocusedRootTask.getDisplayId() : 0;
        }
        DisplayContent displayContent = getDisplayContent(i2);
        if (displayContent == null) {
            Slog.d(TAG, "startHomeOnDisplay display null return");
            return false;
        }
        return ((Boolean) displayContent.reduceOnAllTaskDisplayAreas(new BiFunction() { // from class: com.android.server.wm.RootWindowContainer$$ExternalSyntheticLambda5
            @Override // java.util.function.BiFunction
            public final Object apply(Object obj, Object obj2) {
                Boolean lambda$startHomeOnDisplay$12;
                lambda$startHomeOnDisplay$12 = RootWindowContainer.this.lambda$startHomeOnDisplay$12(i, str, z, z2, (TaskDisplayArea) obj, (Boolean) obj2);
                return lambda$startHomeOnDisplay$12;
            }
        }, Boolean.FALSE)).booleanValue();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ Boolean lambda$startHomeOnDisplay$12(int i, String str, boolean z, boolean z2, TaskDisplayArea taskDisplayArea, Boolean bool) {
        return Boolean.valueOf(startHomeOnTaskDisplayArea(i, str, taskDisplayArea, z, z2) | bool.booleanValue());
    }

    boolean startHomeOnTaskDisplayArea(int i, String str, TaskDisplayArea taskDisplayArea, boolean z, boolean z2) {
        Intent homeIntent;
        ActivityInfo resolveHomeActivity;
        if (taskDisplayArea == null) {
            Task topDisplayFocusedRootTask = getTopDisplayFocusedRootTask();
            if (topDisplayFocusedRootTask != null) {
                taskDisplayArea = topDisplayFocusedRootTask.getDisplayArea();
            } else {
                taskDisplayArea = getDefaultTaskDisplayArea();
            }
        }
        if (taskDisplayArea == getDefaultTaskDisplayArea() || this.mWmService.shouldPlacePrimaryHomeOnDisplay(taskDisplayArea.getDisplayId(), i)) {
            homeIntent = this.mService.getHomeIntent();
            resolveHomeActivity = resolveHomeActivity(i, homeIntent);
        } else if (shouldPlaceSecondaryHomeOnDisplayArea(taskDisplayArea)) {
            Pair<ActivityInfo, Intent> resolveSecondaryHomeActivity = resolveSecondaryHomeActivity(i, taskDisplayArea);
            resolveHomeActivity = (ActivityInfo) resolveSecondaryHomeActivity.first;
            homeIntent = (Intent) resolveSecondaryHomeActivity.second;
        } else {
            resolveHomeActivity = null;
            homeIntent = null;
        }
        if (resolveHomeActivity == null || homeIntent == null) {
            return false;
        }
        ActivityInfo switchDefaultLauncherForBootAware = this.mRootWindowContainerExt.switchDefaultLauncherForBootAware(this.mService.mContext, resolveHomeActivity, i, homeIntent);
        if (!canStartHomeOnDisplayArea(switchDefaultLauncherForBootAware, taskDisplayArea, z)) {
            return false;
        }
        homeIntent.setComponent(new ComponentName(switchDefaultLauncherForBootAware.applicationInfo.packageName, switchDefaultLauncherForBootAware.name));
        homeIntent.setFlags(homeIntent.getFlags() | 268435456);
        if (z2) {
            homeIntent.putExtra("android.intent.extra.FROM_HOME_KEY", true);
            if (this.mWindowManager.getRecentsAnimationController() != null) {
                this.mWindowManager.getRecentsAnimationController().cancelAnimationForHomeStart();
            }
        }
        homeIntent.putExtra("android.intent.extra.EXTRA_START_REASON", str);
        this.mRootWindowContainerExt.putExtraIfNeededForDisplayingNewFeatures(str, homeIntent, i);
        this.mService.getActivityStartController().startHomeActivity(homeIntent, switchDefaultLauncherForBootAware, str + ":" + i + ":" + UserHandle.getUserId(switchDefaultLauncherForBootAware.applicationInfo.uid) + ":" + taskDisplayArea.getDisplayId(), taskDisplayArea);
        return true;
    }

    /* JADX WARN: Code restructure failed: missing block: B:15:0x0036, code lost:
    
        r0 = null;
     */
    @VisibleForTesting
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    ActivityInfo resolveHomeActivity(int i, Intent intent) {
        ActivityInfo activityInfo;
        ComponentName component = intent.getComponent();
        try {
        } catch (RemoteException unused) {
            activityInfo = null;
        }
        if (component != null) {
            activityInfo = AppGlobals.getPackageManager().getActivityInfo(component, 1024L, i);
        } else {
            ResolveInfo resolveIntent = this.mTaskSupervisor.resolveIntent(intent, intent.resolveTypeIfNeeded(this.mService.mContext.getContentResolver()), i, 1024, Binder.getCallingUid(), Binder.getCallingPid());
            if (resolveIntent != null) {
                activityInfo = resolveIntent.activityInfo;
            }
            activityInfo = null;
        }
        if (activityInfo == null) {
            Slogf.wtf(TAG, new Exception(), "No home screen found for %s and user %d", new Object[]{intent, Integer.valueOf(i)});
            return null;
        }
        ActivityInfo activityInfo2 = new ActivityInfo(activityInfo);
        activityInfo2.applicationInfo = this.mService.getAppInfoForUser(activityInfo2.applicationInfo, i);
        return activityInfo2;
    }

    @VisibleForTesting
    Pair<ActivityInfo, Intent> resolveSecondaryHomeActivity(int i, TaskDisplayArea taskDisplayArea) {
        if (taskDisplayArea == getDefaultTaskDisplayArea()) {
            throw new IllegalArgumentException("resolveSecondaryHomeActivity: Should not be default task container");
        }
        Intent homeIntent = this.mService.getHomeIntent();
        ActivityInfo resolveHomeActivity = resolveHomeActivity(i, homeIntent);
        if (resolveHomeActivity != null) {
            if (ResolverActivity.class.getName().equals(resolveHomeActivity.name)) {
                resolveHomeActivity = null;
            } else {
                homeIntent = this.mService.getSecondaryHomeIntent(resolveHomeActivity.applicationInfo.packageName);
                List<ResolveInfo> resolveActivities = resolveActivities(i, homeIntent);
                int size = resolveActivities.size();
                String str = resolveHomeActivity.name;
                int i2 = 0;
                while (true) {
                    if (i2 >= size) {
                        resolveHomeActivity = null;
                        break;
                    }
                    ResolveInfo resolveInfo = resolveActivities.get(i2);
                    if (resolveInfo.activityInfo.name.equals(str)) {
                        resolveHomeActivity = resolveInfo.activityInfo;
                        break;
                    }
                    i2++;
                }
                if (resolveHomeActivity == null && size > 0) {
                    resolveHomeActivity = resolveActivities.get(0).activityInfo;
                }
            }
        }
        if (resolveHomeActivity != null && !canStartHomeOnDisplayArea(resolveHomeActivity, taskDisplayArea, false)) {
            resolveHomeActivity = null;
        }
        if (resolveHomeActivity == null) {
            homeIntent = this.mService.getSecondaryHomeIntent(null);
            resolveHomeActivity = resolveHomeActivity(i, homeIntent);
        }
        return Pair.create(resolveHomeActivity, homeIntent);
    }

    @VisibleForTesting
    List<ResolveInfo> resolveActivities(int i, Intent intent) {
        try {
            return AppGlobals.getPackageManager().queryIntentActivities(intent, intent.resolveTypeIfNeeded(this.mService.mContext.getContentResolver()), 1024L, i).getList();
        } catch (RemoteException unused) {
            return new ArrayList();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean resumeHomeActivity(ActivityRecord activityRecord, String str, TaskDisplayArea taskDisplayArea) {
        if (!this.mService.isBooting() && !this.mService.isBooted()) {
            return false;
        }
        if (taskDisplayArea == null) {
            taskDisplayArea = getDefaultTaskDisplayArea();
        }
        TaskDisplayArea taskDisplayArea2 = taskDisplayArea;
        ActivityRecord homeActivity = taskDisplayArea2.getHomeActivity();
        String str2 = str + " resumeHomeActivity";
        if (homeActivity != null && !homeActivity.finishing) {
            homeActivity.moveFocusableActivityToTop(str2);
            return resumeFocusedTasksTopActivities(homeActivity.getRootTask(), activityRecord, null);
        }
        return startHomeOnTaskDisplayArea(this.mWmService.getUserAssignedToDisplay(taskDisplayArea2.getDisplayId()), str2, taskDisplayArea2, false, false);
    }

    boolean shouldPlaceSecondaryHomeOnDisplayArea(TaskDisplayArea taskDisplayArea) {
        DisplayContent displayContent;
        DisplayInfo displayInfo;
        if (getDefaultTaskDisplayArea() == taskDisplayArea) {
            throw new IllegalArgumentException("shouldPlaceSecondaryHomeOnDisplay: Should not be on default task container");
        }
        if (taskDisplayArea == null) {
            return false;
        }
        boolean z = this.mService.mContext.getResources().getBoolean(17891890);
        DisplayContent displayContent2 = taskDisplayArea.getDisplayContent();
        if (displayContent2 != null && z && (displayInfo = displayContent2.getDisplayInfo()) != null && displayInfo.type == 1) {
            return true;
        }
        if (!taskDisplayArea.canHostHomeTask()) {
            return false;
        }
        if (taskDisplayArea.getDisplayId() == 0 || this.mService.mSupportsMultiDisplay) {
            return (Settings.Global.getInt(this.mService.mContext.getContentResolver(), "device_provisioned", 0) != 0) && StorageManager.isUserKeyUnlocked(this.mCurrentUser) && (displayContent = taskDisplayArea.getDisplayContent()) != null && !displayContent.isRemoved() && displayContent.supportsSystemDecorations();
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean canStartHomeOnDisplayArea(ActivityInfo activityInfo, TaskDisplayArea taskDisplayArea, boolean z) {
        ActivityTaskManagerService activityTaskManagerService = this.mService;
        if (activityTaskManagerService.mFactoryTest == 1 && activityTaskManagerService.mTopAction == null) {
            return false;
        }
        WindowProcessController processController = activityTaskManagerService.getProcessController(activityInfo.processName, activityInfo.applicationInfo.uid);
        if (!z && processController != null && processController.isInstrumenting()) {
            return false;
        }
        int displayId = taskDisplayArea != null ? taskDisplayArea.getDisplayId() : -1;
        if (displayId != 0 && (displayId == -1 || (displayId != this.mService.mVr2dDisplayId && !this.mWmService.shouldPlacePrimaryHomeOnDisplay(displayId)))) {
            if (!shouldPlaceSecondaryHomeOnDisplayArea(taskDisplayArea)) {
                return false;
            }
            int i = activityInfo.launchMode;
            if (!((i == 2 || i == 3) ? false : true)) {
                return false;
            }
        }
        return true;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean ensureVisibilityAndConfig(ActivityRecord activityRecord, int i, boolean z, boolean z2) {
        ensureActivitiesVisible(null, 0, false, false);
        if (i == -1) {
            return true;
        }
        DisplayContent displayContent = getDisplayContent(i);
        Configuration updateOrientation = displayContent != null ? displayContent.updateOrientation(activityRecord, true) : null;
        if (activityRecord != null) {
            activityRecord.reportDescendantOrientationChangeIfNeeded();
        }
        if (activityRecord != null && z && updateOrientation != null) {
            activityRecord.frozenBeforeDestroy = true;
        }
        if (displayContent != null) {
            return displayContent.updateDisplayOverrideConfigurationLocked(updateOrientation, activityRecord, z2, null);
        }
        return true;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public List<ActivityAssistInfo> getTopVisibleActivities() {
        final ArrayList arrayList = new ArrayList();
        final ArrayList arrayList2 = new ArrayList();
        final Task topDisplayFocusedRootTask = getTopDisplayFocusedRootTask();
        forAllRootTasks(new Consumer() { // from class: com.android.server.wm.RootWindowContainer$$ExternalSyntheticLambda4
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                RootWindowContainer.lambda$getTopVisibleActivities$13(arrayList2, topDisplayFocusedRootTask, arrayList, (Task) obj);
            }
        });
        return arrayList;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$getTopVisibleActivities$13(ArrayList arrayList, Task task, ArrayList arrayList2, Task task2) {
        ActivityRecord topNonFinishingActivity;
        ActivityRecord topNonFinishingActivity2;
        if (!task2.shouldBeVisible(null) || (topNonFinishingActivity = task2.getTopNonFinishingActivity()) == null) {
            return;
        }
        arrayList.clear();
        arrayList.add(new ActivityAssistInfo(topNonFinishingActivity));
        Task adjacentTask = topNonFinishingActivity.getTask().getAdjacentTask();
        if (adjacentTask != null && (topNonFinishingActivity2 = adjacentTask.getTopNonFinishingActivity()) != null) {
            arrayList.add(new ActivityAssistInfo(topNonFinishingActivity2));
        }
        if (task2 == task) {
            arrayList2.addAll(0, arrayList);
        } else {
            arrayList2.addAll(arrayList);
        }
    }

    public Task getTopDisplayFocusedRootTask() {
        for (int childCount = getChildCount() - 1; childCount >= 0; childCount--) {
            Task focusedRootTask = getChildAt(childCount).getFocusedRootTask();
            if (focusedRootTask != null) {
                return focusedRootTask;
            }
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public ActivityRecord getTopResumedActivity() {
        Task topDisplayFocusedRootTask = getTopDisplayFocusedRootTask();
        if (topDisplayFocusedRootTask == null) {
            return null;
        }
        ActivityRecord topResumedActivity = topDisplayFocusedRootTask.getTopResumedActivity();
        return (topResumedActivity == null || topResumedActivity.app == null) ? (ActivityRecord) getItemFromTaskDisplayAreas(new Function() { // from class: com.android.server.wm.RootWindowContainer$$ExternalSyntheticLambda7
            @Override // java.util.function.Function
            public final Object apply(Object obj) {
                return ((TaskDisplayArea) obj).getFocusedActivity();
            }
        }) : topResumedActivity;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean isTopDisplayFocusedRootTask(Task task) {
        return task != null && task == getTopDisplayFocusedRootTask();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean attachApplication(WindowProcessController windowProcessController) throws RemoteException {
        try {
            return this.mAttachApplicationHelper.process(windowProcessController);
        } finally {
            this.mAttachApplicationHelper.reset();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void ensureActivitiesVisible(ActivityRecord activityRecord, int i, boolean z) {
        ensureActivitiesVisible(activityRecord, i, z, true);
    }

    void ensureActivitiesVisible(ActivityRecord activityRecord, int i, boolean z, boolean z2) {
        if (this.mTaskSupervisor.inActivityVisibilityUpdate() || this.mTaskSupervisor.isRootVisibilityUpdateDeferred()) {
            return;
        }
        try {
            this.mTaskSupervisor.beginActivityVisibilityUpdate();
            for (int childCount = getChildCount() - 1; childCount >= 0; childCount--) {
                getChildAt(childCount).ensureActivitiesVisible(activityRecord, i, z, z2);
            }
        } finally {
            this.mTaskSupervisor.endActivityVisibilityUpdate();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean switchUser(final int i, UserState userState) {
        Task topDisplayFocusedRootTask = getTopDisplayFocusedRootTask();
        int rootTaskId = topDisplayFocusedRootTask != null ? topDisplayFocusedRootTask.getRootTaskId() : -1;
        removeRootTasksInWindowingModes(2);
        this.mUserRootTaskInFront.put(this.mCurrentUser, rootTaskId);
        this.mCurrentUser = i;
        this.mTaskSupervisor.mStartingUsers.add(userState);
        forAllRootTasks(new Consumer() { // from class: com.android.server.wm.RootWindowContainer$$ExternalSyntheticLambda8
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                ((Task) obj).switchUser(i);
            }
        });
        Task rootTask = getRootTask(this.mUserRootTaskInFront.get(i));
        if (rootTask == null) {
            rootTask = getDefaultTaskDisplayArea().getOrCreateRootHomeTask();
        }
        boolean isActivityTypeHome = rootTask.isActivityTypeHome();
        if (rootTask.isOnHomeDisplay()) {
            rootTask.moveToFront("switchUserOnHomeDisplay");
        } else {
            resumeHomeActivity(null, "switchUserOnOtherDisplay", getDefaultTaskDisplayArea());
        }
        return isActivityTypeHome;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void removeUser(int i) {
        this.mUserRootTaskInFront.delete(i);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void updateUserRootTask(int i, Task task) {
        if (i != this.mCurrentUser) {
            if (task == null) {
                task = getDefaultTaskDisplayArea().getOrCreateRootHomeTask();
            }
            this.mUserRootTaskInFront.put(i, task.getRootTaskId());
        }
    }

    void moveRootTaskToTaskDisplayArea(int i, TaskDisplayArea taskDisplayArea, boolean z) {
        Task rootTask = getRootTask(i);
        if (rootTask == null) {
            throw new IllegalArgumentException("moveRootTaskToTaskDisplayArea: Unknown rootTaskId=" + i);
        }
        TaskDisplayArea displayArea = rootTask.getDisplayArea();
        if (displayArea == null) {
            throw new IllegalStateException("moveRootTaskToTaskDisplayArea: rootTask=" + rootTask + " is not attached to any task display area.");
        }
        if (taskDisplayArea == null) {
            throw new IllegalArgumentException("moveRootTaskToTaskDisplayArea: Unknown taskDisplayArea=" + taskDisplayArea);
        }
        if (displayArea == taskDisplayArea) {
            throw new IllegalArgumentException("Trying to move rootTask=" + rootTask + " to its current taskDisplayArea=" + taskDisplayArea);
        }
        rootTask.reparent(taskDisplayArea, z);
        rootTask.resumeNextFocusAfterReparent();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void moveRootTaskToDisplay(int i, int i2, boolean z) {
        DisplayContent displayContentOrCreate = getDisplayContentOrCreate(i2);
        if (displayContentOrCreate == null) {
            throw new IllegalArgumentException("moveRootTaskToDisplay: Unknown displayId=" + i2);
        }
        moveRootTaskToTaskDisplayArea(i, displayContentOrCreate.getDefaultTaskDisplayArea(), z);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void moveActivityToPinnedRootTask(ActivityRecord activityRecord, ActivityRecord activityRecord2, String str) {
        moveActivityToPinnedRootTask(activityRecord, activityRecord2, str, null);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void moveActivityToPinnedRootTask(ActivityRecord activityRecord, ActivityRecord activityRecord2, String str, Transition transition) {
        Task build;
        TaskDisplayArea displayArea = activityRecord.getDisplayArea();
        Task task = activityRecord.getTask();
        TransitionController transitionController = task.mTransitionController;
        if (transition == null && !transitionController.isCollecting() && transitionController.getTransitionPlayer() != null) {
            transition = transitionController.createTransition(10);
        }
        transitionController.deferTransitionReady();
        this.mService.deferWindowLayout();
        try {
            Task rootPinnedTask = displayArea.getRootPinnedTask();
            if (rootPinnedTask != null) {
                transitionController.collect(rootPinnedTask);
                removeRootTasksInWindowingModes(2);
            }
            activityRecord.getDisplayContent().prepareAppTransition(0);
            transitionController.collect(task);
            activityRecord.setWindowingMode(activityRecord.getWindowingMode());
            TaskFragment organizedTaskFragment = activityRecord.getOrganizedTaskFragment();
            if (task.getNonFinishingActivityCount() == 1) {
                task.maybeApplyLastRecentsAnimationTransaction();
                if (task.getParent() != displayArea) {
                    task.reparent(displayArea, true);
                }
                task.forAllTaskFragments(new Consumer() { // from class: com.android.server.wm.RootWindowContainer$$ExternalSyntheticLambda32
                    @Override // java.util.function.Consumer
                    public final void accept(Object obj) {
                        RootWindowContainer.lambda$moveActivityToPinnedRootTask$15((TaskFragment) obj);
                    }
                });
                build = task;
            } else {
                build = new Task.Builder(this.mService).setActivityType(activityRecord.getActivityType()).setOnTop(true).setActivityInfo(activityRecord.info).setParent(displayArea).setIntent(activityRecord.intent).setDeferTaskAppear(true).setHasBeenVisible(true).setWindowingMode(task.getRequestedOverrideWindowingMode()).build();
                activityRecord.setLastParentBeforePip(activityRecord2);
                build.setLastNonFullscreenBounds(task.mLastNonFullscreenBounds);
                build.setBoundsUnchecked(activityRecord.getTaskFragment().getBounds());
                PictureInPictureSurfaceTransaction pictureInPictureSurfaceTransaction = task.mLastRecentsAnimationTransaction;
                if (pictureInPictureSurfaceTransaction != null) {
                    build.setLastRecentsAnimationTransaction(pictureInPictureSurfaceTransaction, task.mLastRecentsAnimationOverlay);
                    task.clearLastRecentsAnimationTransaction(false);
                } else {
                    task.resetSurfaceControlTransforms();
                }
                if (organizedTaskFragment != null && organizedTaskFragment.getNonFinishingActivityCount() == 1 && organizedTaskFragment.getTopNonFinishingActivity() == activityRecord) {
                    organizedTaskFragment.mClearedTaskFragmentForPip = true;
                }
                transitionController.collect(build);
                if (transitionController.isShellTransitionsEnabled()) {
                    build.setWindowingMode(2);
                }
                activityRecord.reparent(build, Integer.MAX_VALUE, str);
                build.maybeApplyLastRecentsAnimationTransaction();
                ActivityRecord topMostActivity = task.getTopMostActivity();
                if (topMostActivity != null && topMostActivity.isState(ActivityRecord.State.STOPPED) && task.getDisplayContent().mAppTransition.containsTransitRequest(4)) {
                    task.getDisplayContent().mClosingApps.add(topMostActivity);
                    topMostActivity.mRequestForceTransition = true;
                }
            }
            if (build.getWrapper().getExtImpl().isTaskEmbedded()) {
                this.mTaskSupervisor.getWrapper().getExtImpl().removeContainerTaskForEmbeddedTask(build);
            }
            this.mRootWindowContainerExt.moveActivityToPinnedRootTask(task, activityRecord);
            build.setWindowingMode(2);
            if (activityRecord.getOptions() != null && activityRecord.getOptions().isLaunchIntoPip()) {
                this.mWindowManager.mTaskSnapshotController.recordSnapshot(task, false);
                build.setBounds(activityRecord.pictureInPictureArgs.getSourceRectHint());
            }
            build.setDeferTaskAppear(false);
            activityRecord.mWaitForEnteringPinnedMode = true;
            activityRecord.supportsEnterPipOnTaskSwitch = false;
            if (organizedTaskFragment != null && organizedTaskFragment.mClearedTaskFragmentForPip && organizedTaskFragment.isTaskVisibleRequested()) {
                this.mService.mTaskFragmentOrganizerController.dispatchPendingInfoChangedEvent(organizedTaskFragment);
            }
            this.mService.continueWindowLayout();
            try {
                ensureActivitiesVisible(null, 0, false);
                if (transition != null) {
                    transitionController.requestStartTransition(transition, build, null, null);
                    transition.setReady(build, true);
                }
                resumeFocusedTasksTopActivities();
                LogUtil.d(TAG, "notifyActivityPipModeChanged");
                notifyActivityPipModeChanged(activityRecord.getTask(), activityRecord);
            } finally {
            }
        } catch (Throwable th) {
            this.mService.continueWindowLayout();
            try {
                ensureActivitiesVisible(null, 0, false);
                throw th;
            } finally {
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$moveActivityToPinnedRootTask$15(TaskFragment taskFragment) {
        if (taskFragment.isOrganizedTaskFragment()) {
            taskFragment.resetAdjacentTaskFragment();
            taskFragment.setCompanionTaskFragment(null);
            taskFragment.setAnimationParams(TaskFragmentAnimationParams.DEFAULT);
            if (taskFragment.getTopNonFinishingActivity() != null) {
                taskFragment.setRelativeEmbeddedBounds(new Rect());
                taskFragment.updateRequestedOverrideConfiguration(Configuration.EMPTY);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void notifyActivityPipModeChanged(Task task, ActivityRecord activityRecord) {
        boolean z = activityRecord != null;
        if (z) {
            this.mService.getTaskChangeNotificationController().notifyActivityPinned(activityRecord);
        } else {
            this.mService.getTaskChangeNotificationController().notifyActivityUnpinned();
        }
        this.mWindowManager.mPolicy.setPipVisibilityLw(z);
        this.mWmService.mTransactionFactory.get().setTrustedOverlay(task.getSurfaceControl(), z).apply();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void executeAppTransitionForAllDisplay() {
        for (int childCount = getChildCount() - 1; childCount >= 0; childCount--) {
            getChildAt(childCount).mDisplayContent.executeAppTransition();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public ActivityRecord findTask(ActivityRecord activityRecord, TaskDisplayArea taskDisplayArea) {
        return findTask(activityRecord.getActivityType(), activityRecord.taskAffinity, activityRecord.intent, activityRecord.info, taskDisplayArea, activityRecord);
    }

    /* JADX WARN: Code restructure failed: missing block: B:18:0x0067, code lost:
    
        if (r10 != null) goto L21;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    ActivityRecord findTask(int i, final String str, final Intent intent, ActivityInfo activityInfo, final TaskDisplayArea taskDisplayArea, final ActivityRecord activityRecord) {
        ActivityRecord activityRecord2;
        if (ProtoLogCache.WM_DEBUG_TASKS_enabled) {
            ProtoLogImpl.d(ProtoLogGroup.WM_DEBUG_TASKS, -1559645910, 0, (String) null, new Object[]{String.valueOf(i), String.valueOf(str), String.valueOf(intent), String.valueOf(activityInfo), String.valueOf(taskDisplayArea)});
        }
        this.mTmpFindTaskResult.init(i, str, intent, activityInfo);
        if (taskDisplayArea != null) {
            this.mTmpFindTaskResult.process(taskDisplayArea);
            FindTaskResult findTaskResult = this.mTmpFindTaskResult;
            ActivityRecord activityRecord3 = findTaskResult.mIdealRecord;
            if (activityRecord3 != null) {
                if (activityRecord3.isState(ActivityRecord.State.DESTROYED)) {
                    this.mSocExt.acquireAppLaunchPerfLock(activityRecord, this.mService);
                    this.mRootWindowContainerExt.hookAcquireLaunchBoost();
                }
                if (this.mTmpFindTaskResult.mIdealRecord.isState(ActivityRecord.State.STOPPED)) {
                    this.mSocExt.acquireUxPerfLock(6, activityRecord.packageName);
                }
                return this.mTmpFindTaskResult.mIdealRecord;
            }
            activityRecord2 = findTaskResult.mCandidateRecord;
        }
        activityRecord2 = null;
        ActivityRecord activityRecord4 = this.mTmpFindTaskResult.mIdealRecord;
        if (activityRecord4 == null || activityRecord4.isState(ActivityRecord.State.DESTROYED)) {
            this.mSocExt.acquireAppLaunchPerfLock(activityRecord, this.mService);
            this.mRootWindowContainerExt.hookAcquireLaunchBoost();
        } else if (activityRecord == null) {
            Slog.w(TAG, "Should not happen! Didn't apply launch boost");
        }
        ActivityRecord activityRecord5 = (ActivityRecord) getItemFromTaskDisplayAreas(new Function() { // from class: com.android.server.wm.RootWindowContainer$$ExternalSyntheticLambda44
            @Override // java.util.function.Function
            public final Object apply(Object obj) {
                ActivityRecord lambda$findTask$16;
                lambda$findTask$16 = RootWindowContainer.this.lambda$findTask$16(taskDisplayArea, str, intent, activityRecord, (TaskDisplayArea) obj);
                return lambda$findTask$16;
            }
        });
        if (activityRecord5 != null) {
            return activityRecord5;
        }
        if (ProtoLogGroup.WM_DEBUG_TASKS.isEnabled() && activityRecord2 == null && ProtoLogCache.WM_DEBUG_TASKS_enabled) {
            ProtoLogImpl.d(ProtoLogGroup.WM_DEBUG_TASKS, -1376035390, 0, (String) null, (Object[]) null);
        }
        return activityRecord2;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ ActivityRecord lambda$findTask$16(TaskDisplayArea taskDisplayArea, String str, Intent intent, ActivityRecord activityRecord, TaskDisplayArea taskDisplayArea2) {
        DisplayContent displayContent;
        if (taskDisplayArea2 == taskDisplayArea) {
            return null;
        }
        this.mTmpFindTaskResult.process(taskDisplayArea2);
        FindTaskResult findTaskResult = this.mTmpFindTaskResult;
        ActivityRecord activityRecord2 = findTaskResult.mIdealRecord;
        if (activityRecord2 != null) {
            return activityRecord2;
        }
        if (!ActivityTaskManagerService.LTW_DISABLE && this.mRootWindowContainerExt.findTaskOnlyForLaunch(this.mService, findTaskResult, str, intent)) {
            return this.mTmpFindTaskResult.mCandidateRecord;
        }
        ActivityRecord activityRecord3 = this.mTmpFindTaskResult.mCandidateRecord;
        if (activityRecord3 != null && activityRecord3.getRootTask() != null && (this.mTmpFindTaskResult.mCandidateRecord.getRootTask().getWrapper().getExtImpl().isPuttTask() || (taskDisplayArea != null && (displayContent = taskDisplayArea.mDisplayContent) != null && displayContent.getWrapper().getNonStaticExtImpl().isPuttDisplay()))) {
            Slog.w(TAG, "putt: task exchange ,candidateR:" + this.mTmpFindTaskResult.mCandidateRecord + " for r:" + activityRecord);
            return this.mTmpFindTaskResult.mCandidateRecord;
        }
        if (this.mTmpFindTaskResult.mCandidateRecord == null || !(((IMirageDisplayManagerExt) ExtLoader.type(IMirageDisplayManagerExt.class).create()).isMirageDisplay(this.mTmpFindTaskResult.mCandidateRecord.getDisplayId()) || ((IMirageDisplayManagerExt) ExtLoader.type(IMirageDisplayManagerExt.class).create()).isMirageDisplay(activityRecord.mHandoverLaunchDisplayId))) {
            return null;
        }
        Slog.d(TAG, "Return candidate record for mirage mode");
        return this.mTmpFindTaskResult.mCandidateRecord;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int finishTopCrashedActivities(final WindowProcessController windowProcessController, final String str) {
        final Task topDisplayFocusedRootTask = getTopDisplayFocusedRootTask();
        final Task[] taskArr = new Task[1];
        forAllTasks(new Consumer() { // from class: com.android.server.wm.RootWindowContainer$$ExternalSyntheticLambda13
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                RootWindowContainer.lambda$finishTopCrashedActivities$17(WindowProcessController.this, str, topDisplayFocusedRootTask, taskArr, (Task) obj);
            }
        });
        Task task = taskArr[0];
        if (task != null) {
            return task.mTaskId;
        }
        return -1;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$finishTopCrashedActivities$17(WindowProcessController windowProcessController, String str, Task task, Task[] taskArr, Task task2) {
        Task finishTopCrashedActivityLocked = task2.finishTopCrashedActivityLocked(windowProcessController, str);
        if (task2 == task || taskArr[0] == null) {
            taskArr[0] = finishTopCrashedActivityLocked;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean resumeFocusedTasksTopActivities() {
        return resumeFocusedTasksTopActivities(null, null, null);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean resumeFocusedTasksTopActivities(Task task, ActivityRecord activityRecord, ActivityOptions activityOptions) {
        return resumeFocusedTasksTopActivities(task, activityRecord, activityOptions, false);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean resumeFocusedTasksTopActivities(final Task task, final ActivityRecord activityRecord, final ActivityOptions activityOptions, boolean z) {
        Object obj;
        if (!this.mTaskSupervisor.readyToResume()) {
            return false;
        }
        Object obj2 = null;
        int i = 1;
        boolean resumeTopActivityUncheckedLocked = (task == null || !((task.isTopRootTaskInDisplayArea() || getTopDisplayFocusedRootTask() == task) && getWCWrapper().getExtImpl().shouldResumeTaskTopActivity(task, null))) ? false : task.resumeTopActivityUncheckedLocked(activityRecord, activityOptions, z);
        int childCount = getChildCount() - 1;
        while (childCount >= 0) {
            DisplayContent childAt = getChildAt(childCount);
            final boolean[] zArr = new boolean[i];
            if (this.mRootWindowContainerExt.resumeFocusedSkipped(childAt, task, activityRecord)) {
                obj = obj2;
            } else {
                final boolean z2 = resumeTopActivityUncheckedLocked;
                childAt.forAllRootTasks(new Consumer() { // from class: com.android.server.wm.RootWindowContainer$$ExternalSyntheticLambda14
                    @Override // java.util.function.Consumer
                    public final void accept(Object obj3) {
                        RootWindowContainer.this.lambda$resumeFocusedTasksTopActivities$18(task, zArr, z2, activityOptions, activityRecord, (Task) obj3);
                    }
                });
                boolean z3 = zArr[0];
                boolean z4 = resumeTopActivityUncheckedLocked | z3;
                if (!z3) {
                    Task focusedRootTask = childAt.getFocusedRootTask();
                    if (focusedRootTask == null || this.mRootWindowContainerExt.resumeSecondHomeIfNeed(childAt, focusedRootTask, task)) {
                        if (task == null) {
                            obj = null;
                            resumeTopActivityUncheckedLocked = resumeHomeActivity(null, "no-focusable-task", childAt.getDefaultTaskDisplayArea()) | z4;
                        }
                    } else if (getWCWrapper().getExtImpl().shouldResumeTaskTopActivity(focusedRootTask, null)) {
                        resumeTopActivityUncheckedLocked = focusedRootTask.resumeTopActivityUncheckedLocked(activityRecord, activityOptions) | z4;
                        obj = null;
                    } else if (focusedRootTask.getDisplayContent() != null) {
                        focusedRootTask.executeAppTransition(activityOptions);
                    }
                }
                obj = null;
                resumeTopActivityUncheckedLocked = z4;
            }
            childCount--;
            obj2 = obj;
            i = 1;
        }
        return resumeTopActivityUncheckedLocked;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$resumeFocusedTasksTopActivities$18(Task task, boolean[] zArr, boolean z, ActivityOptions activityOptions, ActivityRecord activityRecord, Task task2) {
        ActivityRecord activityRecord2 = task2.topRunningActivity();
        if (!task2.isFocusableAndVisible() || activityRecord2 == null) {
            return;
        }
        if (task2 == task) {
            zArr[0] = zArr[0] | z;
            return;
        }
        if (WindowConfiguration.sExtImpl.isWindowingZoomMode(task2.getWindowingMode())) {
            return;
        }
        if (activityRecord2.isState(ActivityRecord.State.RESUMED) && activityRecord2 == task2.getDisplayArea().topRunningActivity()) {
            task2.executeAppTransition(activityOptions);
        } else if (!activityRecord2.shouldResumeActivity(activityRecord) || getWCWrapper().getExtImpl().shouldResumeTaskTopActivity(task2, activityRecord2)) {
            zArr[0] = zArr[0] | activityRecord2.makeActiveIfNeeded(activityRecord);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: Removed duplicated region for block: B:32:0x00b4  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void applySleepTokens(boolean z) {
        int i;
        Task taskOccludingKeyguard;
        boolean z2 = false;
        for (int childCount = getChildCount() - 1; childCount >= 0; childCount--) {
            final DisplayContent childAt = getChildAt(childCount);
            final boolean shouldSleep = childAt.shouldSleep();
            if (shouldSleep != childAt.isSleeping()) {
                childAt.setIsSleeping(shouldSleep);
                if (childAt.mTransitionController.isShellTransitionsEnabled() && !z2 && !this.mRootWindowContainerExt.skipSleepTransition(childAt) && shouldSleep && !childAt.mAllSleepTokens.isEmpty()) {
                    final Transition transition = new Transition(12, 0, childAt.mTransitionController, this.mWmService.mSyncEngine);
                    TransitionController.OnStartCollect onStartCollect = new TransitionController.OnStartCollect() { // from class: com.android.server.wm.RootWindowContainer$$ExternalSyntheticLambda11
                        @Override // com.android.server.wm.TransitionController.OnStartCollect
                        public final void onCollectStarted(boolean z3) {
                            RootWindowContainer.lambda$applySleepTokens$19(DisplayContent.this, transition, z3);
                        }
                    };
                    if (!childAt.mTransitionController.isCollecting()) {
                        if (this.mWindowManager.mSyncEngine.hasActiveSync()) {
                            Slog.w(TAG, "Ongoing sync outside of a transition.");
                        }
                        childAt.mTransitionController.moveToCollecting(transition);
                        onStartCollect.onCollectStarted(false);
                    } else {
                        childAt.mTransitionController.startCollectOrQueue(transition, onStartCollect);
                    }
                    z2 = true;
                }
                if (z) {
                    if (!shouldSleep && childAt.mTransitionController.isShellTransitionsEnabled() && !childAt.mTransitionController.isCollecting()) {
                        if (!childAt.getDisplayPolicy().isAwake()) {
                            i = 11;
                        } else if (!childAt.isKeyguardOccluded() || this.mRootWindowContainerExt.shouldIgnoreKeyguardOccluedTransition(childAt)) {
                            i = 0;
                        } else {
                            taskOccludingKeyguard = childAt.getTaskOccludingKeyguard();
                            i = 8;
                            if (i != 0) {
                                TransitionController transitionController = childAt.mTransitionController;
                                transitionController.requestStartTransition(transitionController.createTransition(i), taskOccludingKeyguard, null, null);
                            }
                        }
                        taskOccludingKeyguard = null;
                        if (i != 0) {
                        }
                    }
                    childAt.forAllRootTasks(new Consumer() { // from class: com.android.server.wm.RootWindowContainer$$ExternalSyntheticLambda12
                        @Override // java.util.function.Consumer
                        public final void accept(Object obj) {
                            RootWindowContainer.this.lambda$applySleepTokens$21(shouldSleep, childAt, (Task) obj);
                        }
                    });
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$applySleepTokens$19(DisplayContent displayContent, Transition transition, boolean z) {
        if (z && !displayContent.shouldSleep()) {
            transition.abort();
        } else {
            displayContent.mTransitionController.requestStartTransition(transition, null, null, null);
            transition.playNow();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$applySleepTokens$21(boolean z, DisplayContent displayContent, Task task) {
        if (z) {
            task.goToSleepIfPossible(false);
            return;
        }
        task.forAllLeafTasksAndLeafTaskFragments(new Consumer() { // from class: com.android.server.wm.RootWindowContainer$$ExternalSyntheticLambda38
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                ((TaskFragment) obj).awakeFromSleeping();
            }
        }, true);
        if (task.isFocusedRootTaskOnDisplay() && !this.mTaskSupervisor.getKeyguardController().isKeyguardOrAodShowing(displayContent.mDisplayId)) {
            task.resumeTopActivityUncheckedLocked(null, null);
        }
        task.ensureActivitiesVisible(null, 0, false);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public Task getRootTask(int i) {
        for (int childCount = getChildCount() - 1; childCount >= 0; childCount--) {
            Task rootTask = getChildAt(childCount).getRootTask(i);
            if (rootTask != null) {
                return rootTask;
            }
        }
        return null;
    }

    Task getRootTask(int i, int i2) {
        for (int childCount = getChildCount() - 1; childCount >= 0; childCount--) {
            Task rootTask = getChildAt(childCount).getRootTask(i, i2);
            if (rootTask != null) {
                return rootTask;
            }
        }
        return null;
    }

    private Task getRootTask(int i, int i2, int i3) {
        DisplayContent displayContent = getDisplayContent(i3);
        if (displayContent == null) {
            return null;
        }
        return displayContent.getRootTask(i, i2);
    }

    private ActivityTaskManager.RootTaskInfo getRootTaskInfo(final Task task) {
        final ActivityTaskManager.RootTaskInfo rootTaskInfo = new ActivityTaskManager.RootTaskInfo();
        task.fillTaskInfo(rootTaskInfo);
        DisplayContent displayContent = task.getDisplayContent();
        if (displayContent == null) {
            rootTaskInfo.position = -1;
        } else {
            final int[] iArr = new int[1];
            final boolean[] zArr = new boolean[1];
            displayContent.forAllRootTasks(new Predicate() { // from class: com.android.server.wm.RootWindowContainer$$ExternalSyntheticLambda20
                @Override // java.util.function.Predicate
                public final boolean test(Object obj) {
                    boolean lambda$getRootTaskInfo$22;
                    lambda$getRootTaskInfo$22 = RootWindowContainer.lambda$getRootTaskInfo$22(Task.this, zArr, iArr, (Task) obj);
                    return lambda$getRootTaskInfo$22;
                }
            }, false);
            rootTaskInfo.position = zArr[0] ? iArr[0] : -1;
        }
        rootTaskInfo.visible = task.shouldBeVisible(null);
        task.getBounds(rootTaskInfo.bounds);
        int descendantTaskCount = task.getDescendantTaskCount();
        rootTaskInfo.childTaskIds = new int[descendantTaskCount];
        rootTaskInfo.childTaskNames = new String[descendantTaskCount];
        rootTaskInfo.childTaskBounds = new Rect[descendantTaskCount];
        rootTaskInfo.childTaskUserIds = new int[descendantTaskCount];
        final int[] iArr2 = {0};
        task.forAllLeafTasks(new Consumer() { // from class: com.android.server.wm.RootWindowContainer$$ExternalSyntheticLambda21
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                RootWindowContainer.lambda$getRootTaskInfo$23(iArr2, rootTaskInfo, (Task) obj);
            }
        }, false);
        ActivityRecord activityRecord = task.topRunningActivity();
        rootTaskInfo.topActivity = activityRecord != null ? activityRecord.intent.getComponent() : null;
        return rootTaskInfo;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ boolean lambda$getRootTaskInfo$22(Task task, boolean[] zArr, int[] iArr, Task task2) {
        if (task == task2) {
            zArr[0] = true;
            return true;
        }
        iArr[0] = iArr[0] + 1;
        return false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$getRootTaskInfo$23(int[] iArr, ActivityTaskManager.RootTaskInfo rootTaskInfo, Task task) {
        String str;
        int i = iArr[0];
        rootTaskInfo.childTaskIds[i] = task.mTaskId;
        String[] strArr = rootTaskInfo.childTaskNames;
        ComponentName componentName = task.origActivity;
        if (componentName != null) {
            str = componentName.flattenToString();
        } else {
            ComponentName componentName2 = task.realActivity;
            if (componentName2 != null) {
                str = componentName2.flattenToString();
            } else {
                str = task.getTopNonFinishingActivity() != null ? task.getTopNonFinishingActivity().packageName : "unknown";
            }
        }
        strArr[i] = str;
        rootTaskInfo.childTaskBounds[i] = task.mAtmService.getTaskBounds(task.mTaskId);
        rootTaskInfo.childTaskUserIds[i] = task.mUserId;
        iArr[0] = i + 1;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public ActivityTaskManager.RootTaskInfo getRootTaskInfo(int i) {
        Task rootTask = getRootTask(i);
        if (rootTask != null) {
            return getRootTaskInfo(rootTask);
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public ActivityTaskManager.RootTaskInfo getRootTaskInfo(int i, int i2) {
        Task rootTask = getRootTask(i, i2);
        if (rootTask != null) {
            return getRootTaskInfo(rootTask);
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public ActivityTaskManager.RootTaskInfo getRootTaskInfo(int i, int i2, int i3) {
        Task rootTask = getRootTask(i, i2, i3);
        if (rootTask != null) {
            return getRootTaskInfo(rootTask);
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public ArrayList<ActivityTaskManager.RootTaskInfo> getAllRootTaskInfos(int i) {
        final ArrayList<ActivityTaskManager.RootTaskInfo> arrayList = new ArrayList<>();
        if (i == -1) {
            forAllRootTasks(new Consumer() { // from class: com.android.server.wm.RootWindowContainer$$ExternalSyntheticLambda33
                @Override // java.util.function.Consumer
                public final void accept(Object obj) {
                    RootWindowContainer.this.lambda$getAllRootTaskInfos$24(arrayList, (Task) obj);
                }
            });
            return arrayList;
        }
        DisplayContent displayContent = getDisplayContent(i);
        if (displayContent == null) {
            return arrayList;
        }
        displayContent.forAllRootTasks(new Consumer() { // from class: com.android.server.wm.RootWindowContainer$$ExternalSyntheticLambda34
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                RootWindowContainer.this.lambda$getAllRootTaskInfos$25(arrayList, (Task) obj);
            }
        });
        return arrayList;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$getAllRootTaskInfos$24(ArrayList arrayList, Task task) {
        arrayList.add(getRootTaskInfo(task));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$getAllRootTaskInfos$25(ArrayList arrayList, Task task) {
        arrayList.add(getRootTaskInfo(task));
    }

    @Override // android.hardware.display.DisplayManager.DisplayListener
    public void onDisplayAdded(int i) {
        if (ActivityTaskManagerDebugConfig.DEBUG_ROOT_TASK) {
            Slog.v(TAG, "Display added displayId=" + i);
        }
        WindowManagerGlobalLock windowManagerGlobalLock = this.mService.mGlobalLock;
        WindowManagerService.boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                DisplayContent displayContentOrCreate = getDisplayContentOrCreate(i);
                if (displayContentOrCreate == null) {
                    WindowManagerService.resetPriorityAfterLockedSection();
                    return;
                }
                if (this.mService.isBooted() || this.mService.isBooting()) {
                    startSystemDecorations(displayContentOrCreate);
                }
                this.mWmService.mPossibleDisplayInfoMapper.removePossibleDisplayInfos(i);
                this.mRootWindowContainerExt.onDisplayAdded(displayContentOrCreate);
                ((IMirageWindowManagerExt) ExtLoader.type(IMirageWindowManagerExt.class).create()).onDisplayAdded(i);
                WindowManagerService.resetPriorityAfterLockedSection();
            } catch (Throwable th) {
                WindowManagerService.resetPriorityAfterLockedSection();
                throw th;
            }
        }
    }

    private void startSystemDecorations(DisplayContent displayContent) {
        startHomeOnDisplay(this.mCurrentUser, "displayAdded", displayContent.getDisplayId());
        displayContent.getDisplayPolicy().notifyDisplayReady();
    }

    @Override // android.hardware.display.DisplayManager.DisplayListener
    public void onDisplayRemoved(int i) {
        if (ActivityTaskManagerDebugConfig.DEBUG_ROOT_TASK) {
            Slog.v(TAG, "Display removed displayId=" + i);
        }
        if (i == 0) {
            throw new IllegalArgumentException("Can't remove the primary display.");
        }
        WindowManagerGlobalLock windowManagerGlobalLock = this.mService.mGlobalLock;
        WindowManagerService.boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                DisplayContent displayContent = getDisplayContent(i);
                if (displayContent == null) {
                    WindowManagerService.resetPriorityAfterLockedSection();
                    return;
                }
                this.mRootWindowContainerExt.onDisplayRemoved(displayContent);
                ((IMirageWindowManagerExt) ExtLoader.type(IMirageWindowManagerExt.class).create()).onDisplayRemoved(i);
                displayContent.remove();
                this.mWmService.mPossibleDisplayInfoMapper.removePossibleDisplayInfos(i);
                WindowManagerService.resetPriorityAfterLockedSection();
            } catch (Throwable th) {
                WindowManagerService.resetPriorityAfterLockedSection();
                throw th;
            }
        }
    }

    @Override // android.hardware.display.DisplayManager.DisplayListener
    public void onDisplayChanged(int i) {
        if (ActivityTaskManagerDebugConfig.DEBUG_ROOT_TASK) {
            Slog.v(TAG, "Display changed displayId=" + i);
        }
        WindowManagerGlobalLock windowManagerGlobalLock = this.mService.mGlobalLock;
        WindowManagerService.boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                DisplayContent displayContent = getDisplayContent(i);
                if (displayContent != null) {
                    this.mRootWindowContainerExt.hooksetUxThreadValue(Process.myPid(), Process.myTid(), "1");
                    displayContent.onDisplayChanged();
                    this.mRootWindowContainerExt.hooksetUxThreadValue(Process.myPid(), Process.myTid(), "0");
                }
                this.mWmService.mPossibleDisplayInfoMapper.removePossibleDisplayInfos(i);
                updateDisplayImePolicyCache();
            } catch (Throwable th) {
                WindowManagerService.resetPriorityAfterLockedSection();
                throw th;
            }
        }
        WindowManagerService.resetPriorityAfterLockedSection();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void updateDisplayImePolicyCache() {
        final ArrayMap arrayMap = new ArrayMap();
        forAllDisplays(new Consumer() { // from class: com.android.server.wm.RootWindowContainer$$ExternalSyntheticLambda1
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                RootWindowContainer.lambda$updateDisplayImePolicyCache$26(arrayMap, (DisplayContent) obj);
            }
        });
        this.mWmService.mDisplayImePolicyCache = Collections.unmodifiableMap(arrayMap);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$updateDisplayImePolicyCache$26(ArrayMap arrayMap, DisplayContent displayContent) {
        arrayMap.put(Integer.valueOf(displayContent.getDisplayId()), Integer.valueOf(displayContent.getImePolicy()));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void updateUIDsPresentOnDisplay() {
        this.mDisplayAccessUIDs.clear();
        for (int childCount = getChildCount() - 1; childCount >= 0; childCount--) {
            DisplayContent childAt = getChildAt(childCount);
            if (childAt.isPrivate()) {
                this.mDisplayAccessUIDs.append(childAt.mDisplayId, childAt.getPresentUIDs());
            }
        }
        this.mDisplayManagerInternal.setDisplayAccessUIDs(this.mDisplayAccessUIDs);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void prepareForShutdown() {
        for (int i = 0; i < getChildCount(); i++) {
            createSleepToken("shutdown", getChildAt(i).mDisplayId);
        }
    }

    SleepToken createSleepToken(String str, int i) {
        return createSleepToken(str, i, false);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public SleepToken createSleepToken(String str, int i, boolean z) {
        DisplayContent displayContent = getDisplayContent(i);
        if (displayContent == null) {
            throw new IllegalArgumentException("Invalid display: " + i);
        }
        int makeSleepTokenKey = makeSleepTokenKey(str, i);
        SleepToken sleepToken = this.mSleepTokens.get(makeSleepTokenKey);
        if (sleepToken == null) {
            SleepToken sleepToken2 = new SleepToken(str, i, z);
            this.mSleepTokens.put(makeSleepTokenKey, sleepToken2);
            displayContent.mAllSleepTokens.add(sleepToken2);
            if (ProtoLogCache.WM_DEBUG_STATES_enabled) {
                ProtoLogImpl.d(ProtoLogGroup.WM_DEBUG_STATES, -317761482, 4, (String) null, new Object[]{String.valueOf(str), Long.valueOf(i)});
            }
            DeviceStateProviderImpl.sExtImpl.notifyCreateSleepToken(sleepToken2.mTag, sleepToken2.mDisplayId, displayContent.getDisplay());
            return sleepToken2;
        }
        throw new RuntimeException("Create the same sleep token twice: " + sleepToken);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void removeSleepToken(SleepToken sleepToken) {
        if (!this.mSleepTokens.contains(sleepToken.mHashKey)) {
            Slog.d(TAG, "Remove non-exist sleep token: " + sleepToken + " from " + Debug.getCallers(6));
        }
        this.mSleepTokens.remove(sleepToken.mHashKey);
        DisplayContent displayContent = getDisplayContent(sleepToken.mDisplayId);
        if (displayContent == null) {
            Slog.d(TAG, "Remove sleep token for non-existing display: " + sleepToken + " from " + Debug.getCallers(6));
            return;
        }
        if (ProtoLogCache.WM_DEBUG_STATES_enabled) {
            ProtoLogImpl.d(ProtoLogGroup.WM_DEBUG_STATES, -436553282, 4, (String) null, new Object[]{String.valueOf(sleepToken.mTag), Long.valueOf(sleepToken.mDisplayId)});
        }
        displayContent.mAllSleepTokens.remove(sleepToken);
        if (displayContent.mAllSleepTokens.isEmpty()) {
            this.mService.updateSleepIfNeededLocked();
            if ((!this.mTaskSupervisor.getKeyguardController().isDisplayOccluded(displayContent.mDisplayId) && sleepToken.mTag.equals("keyguard")) || sleepToken.mTag.equals(DISPLAY_OFF_SLEEP_TOKEN_TAG)) {
                displayContent.mSkipAppTransitionAnimation = true;
            }
        }
        DeviceStateProviderImpl.sExtImpl.notifyRemoveSleepToken(sleepToken.mTag, sleepToken.mDisplayId, displayContent.getDisplay());
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void addStartingWindowsForVisibleActivities() {
        final ArrayList arrayList = new ArrayList();
        forAllActivities(new Consumer() { // from class: com.android.server.wm.RootWindowContainer$$ExternalSyntheticLambda31
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                RootWindowContainer.lambda$addStartingWindowsForVisibleActivities$27(arrayList, (ActivityRecord) obj);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$addStartingWindowsForVisibleActivities$27(ArrayList arrayList, ActivityRecord activityRecord) {
        Task task = activityRecord.getTask();
        if (activityRecord.isVisibleRequested() && activityRecord.mStartingData != null) {
            arrayList.add(task);
        }
        if (activityRecord.isVisibleRequested() && activityRecord.mStartingData == null && !arrayList.contains(task)) {
            activityRecord.showStartingWindow(true);
            arrayList.add(task);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void invalidateTaskLayers() {
        if (this.mTaskLayersChanged) {
            return;
        }
        this.mTaskLayersChanged = true;
        this.mService.mH.post(this.mRankTaskLayersRunnable);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void rankTaskLayers() {
        if (this.mTaskLayersChanged) {
            this.mTaskLayersChanged = false;
            this.mService.mH.removeCallbacks(this.mRankTaskLayersRunnable);
        }
        this.mTmpTaskLayerRank = 0;
        forAllLeafTasks(new Consumer() { // from class: com.android.server.wm.RootWindowContainer$$ExternalSyntheticLambda16
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                RootWindowContainer.this.lambda$rankTaskLayers$29((Task) obj);
            }
        }, true);
        if (this.mTaskSupervisor.inActivityVisibilityUpdate()) {
            return;
        }
        this.mTaskSupervisor.computeProcessActivityStateBatch();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$rankTaskLayers$29(Task task) {
        int i = task.mLayerRank;
        ActivityRecord activityRecord = task.topRunningActivityLocked();
        if (activityRecord != null && activityRecord.isVisibleRequested()) {
            int i2 = this.mTmpTaskLayerRank + 1;
            this.mTmpTaskLayerRank = i2;
            task.mLayerRank = i2;
        } else {
            task.mLayerRank = -1;
        }
        if (task.mLayerRank != i) {
            task.forAllActivities(new Consumer() { // from class: com.android.server.wm.RootWindowContainer$$ExternalSyntheticLambda19
                @Override // java.util.function.Consumer
                public final void accept(Object obj) {
                    RootWindowContainer.this.lambda$rankTaskLayers$28((ActivityRecord) obj);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$rankTaskLayers$28(ActivityRecord activityRecord) {
        if (activityRecord.hasProcess()) {
            this.mTaskSupervisor.onProcessActivityStateChanged(activityRecord.app, true);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void clearOtherAppTimeTrackers(final AppTimeTracker appTimeTracker) {
        forAllActivities(new Consumer() { // from class: com.android.server.wm.RootWindowContainer$$ExternalSyntheticLambda9
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                RootWindowContainer.lambda$clearOtherAppTimeTrackers$30(appTimeTracker, (ActivityRecord) obj);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$clearOtherAppTimeTrackers$30(AppTimeTracker appTimeTracker, ActivityRecord activityRecord) {
        if (activityRecord.appTimeTracker != appTimeTracker) {
            activityRecord.appTimeTracker = null;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void scheduleDestroyAllActivities(String str) {
        this.mDestroyAllActivitiesReason = str;
        this.mService.mH.post(this.mDestroyAllActivitiesRunnable);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean putTasksToSleep(final boolean z, final boolean z2) {
        final boolean[] zArr = {true};
        forAllRootTasks(new Consumer() { // from class: com.android.server.wm.RootWindowContainer$$ExternalSyntheticLambda39
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                RootWindowContainer.lambda$putTasksToSleep$31(z2, z, zArr, (Task) obj);
            }
        });
        return zArr[0];
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$putTasksToSleep$31(boolean z, boolean z2, boolean[] zArr, Task task) {
        if (z || ((IMirageWindowManagerExt) ExtLoader.type(IMirageWindowManagerExt.class).create()).onGoingToSleep(task.getDisplayId())) {
            if (z2) {
                zArr[0] = task.goToSleepIfPossible(z) & zArr[0];
            } else {
                task.ensureActivitiesVisible(null, 0, false);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public ActivityRecord findActivity(Intent intent, ActivityInfo activityInfo, boolean z) {
        ComponentName component = intent.getComponent();
        if (activityInfo.targetActivity != null) {
            component = new ComponentName(activityInfo.packageName, activityInfo.targetActivity);
        }
        int userId = UserHandle.getUserId(activityInfo.applicationInfo.uid);
        PooledPredicate obtainPredicate = PooledLambda.obtainPredicate(new QuintPredicate() { // from class: com.android.server.wm.RootWindowContainer$$ExternalSyntheticLambda47
            public final boolean test(Object obj, Object obj2, Object obj3, Object obj4, Object obj5) {
                boolean matchesActivity;
                matchesActivity = RootWindowContainer.matchesActivity((ActivityRecord) obj, ((Integer) obj2).intValue(), ((Boolean) obj3).booleanValue(), (Intent) obj4, (ComponentName) obj5);
                return matchesActivity;
            }
        }, PooledLambda.__(ActivityRecord.class), Integer.valueOf(userId), Boolean.valueOf(z), intent, component);
        ActivityRecord activity = getActivity(obtainPredicate);
        obtainPredicate.recycle();
        return activity;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static boolean matchesActivity(ActivityRecord activityRecord, int i, boolean z, Intent intent, ComponentName componentName) {
        if (activityRecord.canBeTopRunning() && activityRecord.mUserId == i) {
            if (z) {
                if (activityRecord.intent.filterEquals(intent)) {
                    return true;
                }
            } else if (activityRecord.mActivityComponent.equals(componentName)) {
                return true;
            }
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean hasAwakeDisplay() {
        for (int childCount = getChildCount() - 1; childCount >= 0; childCount--) {
            if (!getChildAt(childCount).shouldSleep()) {
                return true;
            }
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Task getOrCreateRootTask(ActivityRecord activityRecord, ActivityOptions activityOptions, Task task, boolean z) {
        return getOrCreateRootTask(activityRecord, activityOptions, task, null, z, null, 0);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: Removed duplicated region for block: B:48:0x00cc  */
    /* JADX WARN: Removed duplicated region for block: B:51:0x00d1  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public Task getOrCreateRootTask(ActivityRecord activityRecord, ActivityOptions activityOptions, Task task, Task task2, boolean z, LaunchParamsController.LaunchParams launchParams, int i) {
        TaskDisplayArea taskDisplayArea;
        int launchDisplayId;
        DisplayContent displayContent;
        int launchTaskId;
        Task fromWindowContainerToken;
        if (activityOptions != null && (fromWindowContainerToken = Task.fromWindowContainerToken(activityOptions.getLaunchRootTask())) != null && canLaunchOnDisplay(activityRecord, fromWindowContainerToken)) {
            return fromWindowContainerToken;
        }
        if (activityOptions != null && (launchTaskId = activityOptions.getLaunchTaskId()) != -1) {
            activityOptions.setLaunchTaskId(-1);
            Task anyTaskForId = anyTaskForId(launchTaskId, 2, activityOptions, z);
            activityOptions.setLaunchTaskId(launchTaskId);
            if (canLaunchOnDisplay(activityRecord, anyTaskForId)) {
                return anyTaskForId.getRootTask();
            }
        }
        TaskDisplayArea taskDisplayArea2 = null;
        if (launchParams == null || (taskDisplayArea = launchParams.mPreferredTaskDisplayArea) == null) {
            if (activityOptions != null) {
                WindowContainerToken launchTaskDisplayArea = activityOptions.getLaunchTaskDisplayArea();
                taskDisplayArea = launchTaskDisplayArea != null ? (TaskDisplayArea) WindowContainer.fromBinder(launchTaskDisplayArea.asBinder()) : null;
                if (taskDisplayArea == null && (launchDisplayId = activityOptions.getLaunchDisplayId()) != -1 && (displayContent = getDisplayContent(launchDisplayId)) != null) {
                    taskDisplayArea = displayContent.getDefaultTaskDisplayArea();
                }
            } else {
                taskDisplayArea = null;
            }
        }
        int resolveActivityType = resolveActivityType(activityRecord, activityOptions, task);
        if (taskDisplayArea != null) {
            if (canLaunchOnDisplay(activityRecord, taskDisplayArea.getDisplayId())) {
                return taskDisplayArea.getOrCreateRootTask(activityRecord, activityOptions, task, task2, launchParams, i, resolveActivityType, z);
            }
            taskDisplayArea = null;
        }
        Task rootTask = task != null ? task.getRootTask() : null;
        if (rootTask == null && activityRecord != null) {
            rootTask = activityRecord.getRootTask();
        }
        int i2 = launchParams != null ? launchParams.mWindowingMode : 0;
        if (rootTask != null) {
            taskDisplayArea = rootTask.getDisplayArea();
            if (taskDisplayArea != null && canLaunchOnDisplay(activityRecord, taskDisplayArea.mDisplayContent.mDisplayId)) {
                if (i2 == 0) {
                    i2 = taskDisplayArea.resolveWindowingMode(activityRecord, activityOptions, task);
                }
                if (rootTask.isCompatible(i2, resolveActivityType) || rootTask.mCreatedByOrganizer) {
                    return rootTask;
                }
            }
            return (taskDisplayArea2 != null ? getDefaultTaskDisplayArea() : taskDisplayArea2).getOrCreateRootTask(activityRecord, activityOptions, task, task2, launchParams, i, resolveActivityType, z);
        }
        taskDisplayArea2 = taskDisplayArea;
        return (taskDisplayArea2 != null ? getDefaultTaskDisplayArea() : taskDisplayArea2).getOrCreateRootTask(activityRecord, activityOptions, task, task2, launchParams, i, resolveActivityType, z);
    }

    private boolean canLaunchOnDisplay(ActivityRecord activityRecord, Task task) {
        if (task == null) {
            Slog.w(TAG, "canLaunchOnDisplay(), invalid task: " + task);
            return false;
        }
        if (!task.isAttached()) {
            Slog.w(TAG, "canLaunchOnDisplay(), Task is not attached: " + task);
            return false;
        }
        return canLaunchOnDisplay(activityRecord, task.getTaskDisplayArea().getDisplayId());
    }

    private boolean canLaunchOnDisplay(ActivityRecord activityRecord, int i) {
        if (activityRecord == null || activityRecord.canBeLaunchedOnDisplay(i)) {
            return true;
        }
        Slog.w(TAG, "Not allow to launch " + activityRecord + " on display " + i);
        return false;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int resolveActivityType(ActivityRecord activityRecord, ActivityOptions activityOptions, Task task) {
        int activityType = activityRecord != null ? activityRecord.getActivityType() : 0;
        if (activityType == 0 && task != null) {
            activityType = task.getActivityType();
        }
        if (activityType != 0) {
            return activityType;
        }
        if (activityOptions != null) {
            activityType = activityOptions.getLaunchActivityType();
        }
        if (activityType != 0) {
            return activityType;
        }
        return 1;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Task getNextFocusableRootTask(Task task, boolean z) {
        Task nextFocusableRootTask;
        TaskDisplayArea displayArea = task.getDisplayArea();
        if (displayArea == null) {
            displayArea = getDisplayContent(task.mPrevDisplayId).getDefaultTaskDisplayArea();
        }
        Task nextFocusableRootTask2 = displayArea.getNextFocusableRootTask(task, z);
        if (nextFocusableRootTask2 != null) {
            return nextFocusableRootTask2;
        }
        if (displayArea.mDisplayContent.supportsSystemDecorations()) {
            return null;
        }
        for (int childCount = getChildCount() - 1; childCount >= 0; childCount--) {
            DisplayContent childAt = getChildAt(childCount);
            if (childAt != displayArea.mDisplayContent && (nextFocusableRootTask = childAt.getDefaultTaskDisplayArea().getNextFocusableRootTask(task, z)) != null) {
                return nextFocusableRootTask;
            }
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void closeSystemDialogActivities(final String str) {
        forAllActivities(new Consumer() { // from class: com.android.server.wm.RootWindowContainer$$ExternalSyntheticLambda23
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                RootWindowContainer.this.lambda$closeSystemDialogActivities$32(str, (ActivityRecord) obj);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$closeSystemDialogActivities$32(String str, ActivityRecord activityRecord) {
        if ((activityRecord.info.flags & 256) != 0 || shouldCloseAssistant(activityRecord, str)) {
            activityRecord.finishIfPossible(str, true);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean hasVisibleWindowAboveButDoesNotOwnNotificationShade(final int i) {
        final boolean[] zArr = {false};
        return forAllWindows(new ToBooleanFunction() { // from class: com.android.server.wm.RootWindowContainer$$ExternalSyntheticLambda0
            public final boolean apply(Object obj) {
                boolean lambda$hasVisibleWindowAboveButDoesNotOwnNotificationShade$33;
                lambda$hasVisibleWindowAboveButDoesNotOwnNotificationShade$33 = RootWindowContainer.lambda$hasVisibleWindowAboveButDoesNotOwnNotificationShade$33(i, zArr, (WindowState) obj);
                return lambda$hasVisibleWindowAboveButDoesNotOwnNotificationShade$33;
            }
        }, true);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ boolean lambda$hasVisibleWindowAboveButDoesNotOwnNotificationShade$33(int i, boolean[] zArr, WindowState windowState) {
        if (windowState.mOwnerUid == i && windowState.isVisible()) {
            zArr[0] = true;
        }
        if (windowState.mAttrs.type == 2040) {
            return zArr[0] && windowState.mOwnerUid != i;
        }
        return false;
    }

    private boolean shouldCloseAssistant(ActivityRecord activityRecord, String str) {
        if (activityRecord.isActivityTypeAssistant() && str != "assist") {
            return this.mWmService.mAssistantOnTopOfDream;
        }
        return false;
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
    class FinishDisabledPackageActivitiesHelper implements Predicate<ActivityRecord> {
        private final ArrayList<ActivityRecord> mCollectedActivities = new ArrayList<>();
        private boolean mDoit;
        private boolean mEvenPersistent;
        private Set<String> mFilterByClasses;
        private Task mLastTask;
        private boolean mOnlyRemoveNoProcess;
        private String mPackageName;
        private int mUserId;

        FinishDisabledPackageActivitiesHelper() {
        }

        private void reset(String str, Set<String> set, boolean z, boolean z2, int i, boolean z3) {
            this.mPackageName = str;
            this.mFilterByClasses = set;
            this.mDoit = z;
            this.mEvenPersistent = z2;
            this.mUserId = i;
            this.mOnlyRemoveNoProcess = z3;
            this.mLastTask = null;
        }

        boolean process(String str, Set<String> set, boolean z, boolean z2, int i, boolean z3) {
            reset(str, set, z, z2, i, z3);
            RootWindowContainer.this.forAllActivities(this);
            int size = this.mCollectedActivities.size();
            boolean z4 = false;
            for (int i2 = 0; i2 < size; i2++) {
                ActivityRecord activityRecord = this.mCollectedActivities.get(i2);
                if (this.mOnlyRemoveNoProcess) {
                    if (!activityRecord.hasProcess()) {
                        Slog.i(RootWindowContainer.TAG, "  Force removing " + activityRecord);
                        activityRecord.cleanUp(false, false);
                        activityRecord.removeFromHistory("force-stop");
                    }
                } else {
                    Slog.i(RootWindowContainer.TAG, "  Force finishing " + activityRecord);
                    activityRecord.finishIfPossible("force-stop", true);
                }
                z4 = true;
            }
            this.mCollectedActivities.clear();
            return z4;
        }

        @Override // java.util.function.Predicate
        public boolean test(ActivityRecord activityRecord) {
            Set<String> set;
            boolean z = (activityRecord.packageName.equals(this.mPackageName) && ((set = this.mFilterByClasses) == null || set.contains(activityRecord.mActivityComponent.getClassName()))) || (this.mPackageName == null && activityRecord.mUserId == this.mUserId);
            boolean z2 = !activityRecord.hasProcess();
            int i = this.mUserId;
            if ((i == -1 || activityRecord.mUserId == i) && ((z || activityRecord.getTask() == this.mLastTask) && (z2 || this.mEvenPersistent || !activityRecord.app.isPersistent()))) {
                if (!this.mDoit) {
                    return !activityRecord.finishing;
                }
                this.mCollectedActivities.add(activityRecord);
                this.mLastTask = activityRecord.getTask();
            }
            return false;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean finishDisabledPackageActivities(String str, Set<String> set, boolean z, boolean z2, int i, boolean z3) {
        return this.mFinishDisabledPackageActivitiesHelper.process(str, set, z, z2, i, z3);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void updateActivityApplicationInfo(final ApplicationInfo applicationInfo) {
        final String str = applicationInfo.packageName;
        final int userId = UserHandle.getUserId(applicationInfo.uid);
        forAllActivities(new Consumer() { // from class: com.android.server.wm.RootWindowContainer$$ExternalSyntheticLambda2
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                RootWindowContainer.lambda$updateActivityApplicationInfo$34(userId, str, applicationInfo, (ActivityRecord) obj);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$updateActivityApplicationInfo$34(int i, String str, ApplicationInfo applicationInfo, ActivityRecord activityRecord) {
        if (activityRecord.mUserId == i && str.equals(activityRecord.packageName)) {
            activityRecord.updateApplicationInfo(applicationInfo);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void finishVoiceTask(IVoiceInteractionSession iVoiceInteractionSession) {
        final IBinder asBinder = iVoiceInteractionSession.asBinder();
        forAllLeafTasks(new Consumer() { // from class: com.android.server.wm.RootWindowContainer$$ExternalSyntheticLambda10
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                ((Task) obj).finishIfVoiceTask(asBinder);
            }
        }, true);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void removeRootTasksInWindowingModes(int... iArr) {
        for (int childCount = getChildCount() - 1; childCount >= 0; childCount--) {
            getChildAt(childCount).removeRootTasksInWindowingModes(iArr);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void removeRootTasksWithActivityTypes(int... iArr) {
        for (int childCount = getChildCount() - 1; childCount >= 0; childCount--) {
            getChildAt(childCount).removeRootTasksWithActivityTypes(iArr);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public ActivityRecord topRunningActivity() {
        for (int childCount = getChildCount() - 1; childCount >= 0; childCount--) {
            ActivityRecord activityRecord = getChildAt(childCount).topRunningActivity();
            if (activityRecord != null) {
                return activityRecord;
            }
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean allResumedActivitiesIdle() {
        Task focusedRootTask;
        for (int childCount = getChildCount() - 1; childCount >= 0; childCount--) {
            DisplayContent childAt = getChildAt(childCount);
            if (!childAt.isSleeping() && (focusedRootTask = childAt.getFocusedRootTask()) != null && focusedRootTask.hasActivity()) {
                ActivityRecord topResumedActivity = focusedRootTask.getTopResumedActivity();
                if (topResumedActivity == null || !topResumedActivity.idle) {
                    if (ProtoLogCache.WM_DEBUG_STATES_enabled) {
                        ProtoLogImpl.d(ProtoLogGroup.WM_DEBUG_STATES, -938271693, 1, (String) null, new Object[]{Long.valueOf(focusedRootTask.getRootTaskId()), String.valueOf(topResumedActivity)});
                    }
                    if (this.mService.isBooting()) {
                        Slog.d(TAG, "allResumedActivitiesIdle not idle display=" + childAt + ",rootTask=" + focusedRootTask + ",resumedActivity=" + topResumedActivity);
                    }
                    return false;
                }
                if (this.mTransitionController.isTransientLaunch(topResumedActivity)) {
                    return false;
                }
            }
        }
        this.mService.endLaunchPowerMode(1);
        return true;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean allResumedActivitiesVisible() {
        final boolean[] zArr = {false};
        if (forAllRootTasks(new Predicate() { // from class: com.android.server.wm.RootWindowContainer$$ExternalSyntheticLambda22
            @Override // java.util.function.Predicate
            public final boolean test(Object obj) {
                boolean lambda$allResumedActivitiesVisible$36;
                lambda$allResumedActivitiesVisible$36 = RootWindowContainer.lambda$allResumedActivitiesVisible$36(zArr, (Task) obj);
                return lambda$allResumedActivitiesVisible$36;
            }
        })) {
            return false;
        }
        return zArr[0];
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ boolean lambda$allResumedActivitiesVisible$36(boolean[] zArr, Task task) {
        ActivityRecord topResumedActivity = task.getTopResumedActivity();
        if (topResumedActivity != null) {
            if (!topResumedActivity.nowVisible) {
                return true;
            }
            zArr[0] = true;
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean allPausedActivitiesComplete() {
        final boolean[] zArr = {true};
        if (forAllLeafTasks(new Predicate() { // from class: com.android.server.wm.RootWindowContainer$$ExternalSyntheticLambda30
            @Override // java.util.function.Predicate
            public final boolean test(Object obj) {
                boolean lambda$allPausedActivitiesComplete$37;
                lambda$allPausedActivitiesComplete$37 = RootWindowContainer.lambda$allPausedActivitiesComplete$37(zArr, (Task) obj);
                return lambda$allPausedActivitiesComplete$37;
            }
        })) {
            return false;
        }
        return zArr[0];
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ boolean lambda$allPausedActivitiesComplete$37(boolean[] zArr, Task task) {
        ActivityRecord topPausingActivity = task.getTopPausingActivity();
        if (topPausingActivity != null && !topPausingActivity.isState(ActivityRecord.State.PAUSED, ActivityRecord.State.STOPPED, ActivityRecord.State.STOPPING, ActivityRecord.State.FINISHING)) {
            if (ProtoLogCache.WM_DEBUG_STATES_enabled) {
                ProtoLogImpl.d(ProtoLogGroup.WM_DEBUG_STATES, 895158150, 0, (String) null, new Object[]{String.valueOf(topPausingActivity), String.valueOf(topPausingActivity.getState())});
            }
            if (!ProtoLogGroup.WM_DEBUG_STATES.isEnabled()) {
                return true;
            }
            zArr[0] = false;
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void lockAllProfileTasks(final int i) {
        forAllLeafTasks(new Consumer() { // from class: com.android.server.wm.RootWindowContainer$$ExternalSyntheticLambda3
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                RootWindowContainer.this.lambda$lockAllProfileTasks$39(i, (Task) obj);
            }
        }, true);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$lockAllProfileTasks$39(final int i, Task task) {
        ActivityRecord activityRecord = task.topRunningActivity();
        if ((activityRecord == null || activityRecord.finishing || !"android.app.action.CONFIRM_DEVICE_CREDENTIAL_WITH_USER".equals(activityRecord.intent.getAction()) || !activityRecord.packageName.equals(this.mService.getSysUiServiceComponentLocked().getPackageName())) && task.getActivity(new Predicate() { // from class: com.android.server.wm.RootWindowContainer$$ExternalSyntheticLambda40
            @Override // java.util.function.Predicate
            public final boolean test(Object obj) {
                boolean lambda$lockAllProfileTasks$38;
                lambda$lockAllProfileTasks$38 = RootWindowContainer.lambda$lockAllProfileTasks$38(i, (ActivityRecord) obj);
                return lambda$lockAllProfileTasks$38;
            }
        }) != null) {
            this.mService.getTaskChangeNotificationController().notifyTaskProfileLocked(task.getTaskInfo(), i);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ boolean lambda$lockAllProfileTasks$38(int i, ActivityRecord activityRecord) {
        return !activityRecord.finishing && activityRecord.mUserId == i;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Task anyTaskForId(int i) {
        return anyTaskForId(i, 2);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Task anyTaskForId(int i, int i2) {
        return anyTaskForId(i, i2, null, false);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Task anyTaskForId(int i, int i2, ActivityOptions activityOptions, boolean z) {
        Task orCreateRootTask;
        if (i2 != 2 && activityOptions != null) {
            throw new IllegalArgumentException("Should not specify activity options for non-restore lookup");
        }
        PooledPredicate obtainPredicate = PooledLambda.obtainPredicate(new AppTransition$$ExternalSyntheticLambda2(), PooledLambda.__(Task.class), Integer.valueOf(i));
        Task task = getTask(obtainPredicate);
        obtainPredicate.recycle();
        if (task != null) {
            if (activityOptions != null && !this.mRootWindowContainerExt.skipResolveRootTaskIfNeed(task) && !this.mRootWindowContainerExt.isTaskOnPuttDisplay(task) && (orCreateRootTask = getOrCreateRootTask(null, activityOptions, task, z)) != null && task.getRootTask() != orCreateRootTask && task.getParent() != orCreateRootTask) {
                task.reparent(orCreateRootTask, z, z ? 0 : 2, true, true, "anyTaskForId");
            }
            return task;
        }
        if (i2 == 0) {
            return null;
        }
        if (ActivityTaskManagerDebugConfig.DEBUG_RECENTS) {
            Slog.v(TAG_RECENTS, "Looking for task id=" + i + " in recents");
        }
        Task task2 = this.mTaskSupervisor.mRecentTasks.getTask(i);
        if (task2 == null) {
            if (ActivityTaskManagerDebugConfig.DEBUG_RECENTS) {
                Slog.d(TAG_RECENTS, "\tDidn't find task id=" + i + " in recents");
            }
            return null;
        }
        if (i2 == 1) {
            return task2;
        }
        if (!this.mTaskSupervisor.restoreRecentTaskLocked(task2, activityOptions, z)) {
            if (ActivityTaskManagerDebugConfig.DEBUG_RECENTS) {
                Slog.w(TAG_RECENTS, "Couldn't restore task id=" + i + " found in recents");
            }
            return null;
        }
        if (ActivityTaskManagerDebugConfig.DEBUG_RECENTS) {
            Slog.w(TAG_RECENTS, "Restored task id=" + i + " from in recents");
        }
        return task2;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @VisibleForTesting
    public void getRunningTasks(int i, List<ActivityManager.RunningTaskInfo> list, int i2, int i3, ArraySet<Integer> arraySet, int i4) {
        RootWindowContainer rootWindowContainer;
        if (i4 != -1) {
            DisplayContent displayContent = getDisplayContent(i4);
            if (displayContent == null) {
                return;
            } else {
                rootWindowContainer = displayContent;
            }
        } else {
            rootWindowContainer = this;
        }
        this.mTaskSupervisor.getRunningTasks().getTasks(i, list, i2, this.mService.getRecentTasks(), rootWindowContainer, i3, arraySet);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void startPowerModeLaunchIfNeeded(boolean z, final ActivityRecord activityRecord) {
        ActivityOptions options;
        int i = 1;
        if (!z && activityRecord != null && activityRecord.app != null) {
            final boolean[] zArr = {true};
            final boolean[] zArr2 = {true};
            forAllTaskDisplayAreas(new Consumer() { // from class: com.android.server.wm.RootWindowContainer$$ExternalSyntheticLambda15
                @Override // java.util.function.Consumer
                public final void accept(Object obj) {
                    RootWindowContainer.lambda$startPowerModeLaunchIfNeeded$40(zArr, zArr2, activityRecord, (TaskDisplayArea) obj);
                }
            });
            if (!zArr[0] && !zArr2[0]) {
                return;
            }
        }
        if ((activityRecord != null ? activityRecord.isKeyguardLocked() : this.mDefaultDisplay.isKeyguardLocked()) && activityRecord != null && !activityRecord.isLaunchSourceType(3) && ((options = activityRecord.getOptions()) == null || options.getSourceInfo() == null || options.getSourceInfo().type != 3)) {
            i = 5;
        }
        this.mService.startLaunchPowerMode(i);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$startPowerModeLaunchIfNeeded$40(boolean[] zArr, boolean[] zArr2, ActivityRecord activityRecord, TaskDisplayArea taskDisplayArea) {
        ActivityRecord focusedActivity = taskDisplayArea.getFocusedActivity();
        WindowProcessController windowProcessController = focusedActivity == null ? null : focusedActivity.app;
        zArr[0] = zArr[0] & (windowProcessController == null);
        if (windowProcessController != null) {
            zArr2[0] = zArr2[0] & (!windowProcessController.equals(activityRecord.app));
        }
    }

    public int getTaskToShowPermissionDialogOn(final String str, final int i) {
        final PermissionPolicyInternal permissionPolicyInternal = this.mService.getPermissionPolicyInternal();
        if (permissionPolicyInternal == null) {
            return -1;
        }
        final int[] iArr = {-1};
        forAllLeafTaskFragments(new Predicate() { // from class: com.android.server.wm.RootWindowContainer$$ExternalSyntheticLambda6
            @Override // java.util.function.Predicate
            public final boolean test(Object obj) {
                boolean lambda$getTaskToShowPermissionDialogOn$42;
                lambda$getTaskToShowPermissionDialogOn$42 = RootWindowContainer.lambda$getTaskToShowPermissionDialogOn$42(permissionPolicyInternal, i, str, iArr, (TaskFragment) obj);
                return lambda$getTaskToShowPermissionDialogOn$42;
            }
        });
        return iArr[0];
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ boolean lambda$getTaskToShowPermissionDialogOn$42(final PermissionPolicyInternal permissionPolicyInternal, int i, String str, int[] iArr, TaskFragment taskFragment) {
        ActivityRecord activity = taskFragment.getActivity(new Predicate() { // from class: com.android.server.wm.RootWindowContainer$$ExternalSyntheticLambda50
            @Override // java.util.function.Predicate
            public final boolean test(Object obj) {
                boolean lambda$getTaskToShowPermissionDialogOn$41;
                lambda$getTaskToShowPermissionDialogOn$41 = RootWindowContainer.lambda$getTaskToShowPermissionDialogOn$41(permissionPolicyInternal, (ActivityRecord) obj);
                return lambda$getTaskToShowPermissionDialogOn$41;
            }
        });
        if (activity == null || !activity.isUid(i) || !Objects.equals(str, activity.packageName) || !permissionPolicyInternal.shouldShowNotificationDialogForTask(activity.getTask().getTaskInfo(), str, activity.launchedFromPackage, activity.intent, activity.getName())) {
            return false;
        }
        iArr[0] = activity.getTask().mTaskId;
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ boolean lambda$getTaskToShowPermissionDialogOn$41(PermissionPolicyInternal permissionPolicyInternal, ActivityRecord activityRecord) {
        return activityRecord.canBeTopRunning() && activityRecord.isVisibleRequested() && !permissionPolicyInternal.isIntentToPermissionDialog(activityRecord.intent);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public ArrayList<ActivityRecord> getDumpActivities(final String str, final boolean z, boolean z2, final int i) {
        if (z2) {
            Task topDisplayFocusedRootTask = getTopDisplayFocusedRootTask();
            if (topDisplayFocusedRootTask != null) {
                return topDisplayFocusedRootTask.getDumpActivitiesLocked(str, i);
            }
            return new ArrayList<>();
        }
        RecentTasks recentTasks = this.mWindowManager.mAtmService.getRecentTasks();
        final int recentsComponentUid = recentTasks != null ? recentTasks.getRecentsComponentUid() : -1;
        final ArrayList<ActivityRecord> arrayList = new ArrayList<>();
        forAllLeafTasks(new Predicate() { // from class: com.android.server.wm.RootWindowContainer$$ExternalSyntheticLambda18
            @Override // java.util.function.Predicate
            public final boolean test(Object obj) {
                boolean lambda$getDumpActivities$43;
                lambda$getDumpActivities$43 = RootWindowContainer.lambda$getDumpActivities$43(recentsComponentUid, z, arrayList, str, i, (Task) obj);
                return lambda$getDumpActivities$43;
            }
        });
        return arrayList;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ boolean lambda$getDumpActivities$43(int i, boolean z, ArrayList arrayList, String str, int i2, Task task) {
        boolean z2 = task.effectiveUid == i;
        if (!z || task.shouldBeVisible(null) || z2) {
            arrayList.addAll(task.getDumpActivitiesLocked(str, i2));
        }
        return false;
    }

    @Override // com.android.server.wm.WindowContainer
    public void dump(PrintWriter printWriter, String str, boolean z) {
        super.dump(printWriter, str, z);
        printWriter.print(str);
        printWriter.println("topDisplayFocusedRootTask=" + getTopDisplayFocusedRootTask());
        for (int childCount = getChildCount() + (-1); childCount >= 0; childCount--) {
            getChildAt(childCount).dump(printWriter, str, z);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void dumpDisplayConfigs(PrintWriter printWriter, String str) {
        printWriter.print(str);
        printWriter.println("Display override configurations:");
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            DisplayContent childAt = getChildAt(i);
            printWriter.print(str);
            printWriter.print("  ");
            printWriter.print(childAt.mDisplayId);
            printWriter.print(": ");
            printWriter.println(childAt.getRequestedOverrideConfiguration());
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean dumpActivities(final FileDescriptor fileDescriptor, final PrintWriter printWriter, final boolean z, final boolean z2, final String str, int i) {
        final boolean[] zArr = {false};
        final boolean[] zArr2 = {false};
        for (int childCount = getChildCount() - 1; childCount >= 0; childCount--) {
            DisplayContent childAt = getChildAt(childCount);
            if (zArr[0]) {
                printWriter.println();
            }
            if (i == -1 || childAt.mDisplayId == i) {
                printWriter.print("Display #");
                printWriter.print(childAt.mDisplayId);
                printWriter.println(" (activities from top to bottom):");
                childAt.forAllRootTasks(new Consumer() { // from class: com.android.server.wm.RootWindowContainer$$ExternalSyntheticLambda26
                    @Override // java.util.function.Consumer
                    public final void accept(Object obj) {
                        RootWindowContainer.lambda$dumpActivities$44(zArr2, printWriter, fileDescriptor, z, z2, str, zArr, (Task) obj);
                    }
                });
                childAt.forAllTaskDisplayAreas(new Consumer() { // from class: com.android.server.wm.RootWindowContainer$$ExternalSyntheticLambda27
                    @Override // java.util.function.Consumer
                    public final void accept(Object obj) {
                        RootWindowContainer.lambda$dumpActivities$46(zArr, printWriter, str, zArr2, (TaskDisplayArea) obj);
                    }
                });
            }
        }
        boolean dumpHistoryList = zArr[0] | ActivityTaskSupervisor.dumpHistoryList(fileDescriptor, printWriter, this.mTaskSupervisor.mFinishingActivities, "  ", "Fin", false, !z, false, str, true, new Runnable() { // from class: com.android.server.wm.RootWindowContainer$$ExternalSyntheticLambda28
            @Override // java.lang.Runnable
            public final void run() {
                printWriter.println("  Activities waiting to finish:");
            }
        }, null);
        zArr[0] = dumpHistoryList;
        boolean dumpHistoryList2 = ActivityTaskSupervisor.dumpHistoryList(fileDescriptor, printWriter, this.mTaskSupervisor.mStoppingActivities, "  ", "Stop", false, !z, false, str, true, new Runnable() { // from class: com.android.server.wm.RootWindowContainer$$ExternalSyntheticLambda29
            @Override // java.lang.Runnable
            public final void run() {
                printWriter.println("  Activities waiting to stop:");
            }
        }, null) | dumpHistoryList;
        zArr[0] = dumpHistoryList2;
        return dumpHistoryList2;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$dumpActivities$44(boolean[] zArr, PrintWriter printWriter, FileDescriptor fileDescriptor, boolean z, boolean z2, String str, boolean[] zArr2, Task task) {
        if (zArr[0]) {
            printWriter.println();
        }
        boolean dump = task.dump(fileDescriptor, printWriter, z, z2, str, false);
        zArr[0] = dump;
        zArr2[0] = dump | zArr2[0];
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$dumpActivities$46(boolean[] zArr, final PrintWriter printWriter, String str, boolean[] zArr2, TaskDisplayArea taskDisplayArea) {
        zArr[0] = ActivityTaskSupervisor.printThisActivity(printWriter, taskDisplayArea.getFocusedActivity(), str, zArr2[0], "    Resumed: ", new Runnable() { // from class: com.android.server.wm.RootWindowContainer$$ExternalSyntheticLambda24
            @Override // java.lang.Runnable
            public final void run() {
                printWriter.println("  Resumed activities in task display areas (from top to bottom):");
            }
        }) | zArr[0];
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static int makeSleepTokenKey(String str, int i) {
        return (str + i).hashCode();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
    public static final class SleepToken {
        private static final long DISPLAY_SWAP_TIMEOUT = 1000;
        private final long mAcquireTime = SystemClock.uptimeMillis();
        private final int mDisplayId;
        final int mHashKey;
        private final boolean mIsSwappingDisplay;
        private final String mTag;

        SleepToken(String str, int i, boolean z) {
            this.mTag = str;
            this.mDisplayId = i;
            this.mIsSwappingDisplay = z;
            this.mHashKey = RootWindowContainer.makeSleepTokenKey(str, i);
        }

        public boolean isDisplaySwapping() {
            if (SystemClock.uptimeMillis() - this.mAcquireTime > DISPLAY_SWAP_TIMEOUT) {
                return false;
            }
            return this.mIsSwappingDisplay;
        }

        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("{\"");
            sb.append(this.mTag);
            sb.append("\", display ");
            sb.append(this.mDisplayId);
            sb.append(this.mIsSwappingDisplay ? " is swapping " : "");
            sb.append(", acquire at ");
            sb.append(TimeUtils.formatUptime(this.mAcquireTime));
            sb.append("}");
            return sb.toString();
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public void writeTagToProto(ProtoOutputStream protoOutputStream, long j) {
            protoOutputStream.write(j, this.mTag);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
    public class RankTaskLayersRunnable implements Runnable {
        private RankTaskLayersRunnable() {
        }

        @Override // java.lang.Runnable
        public void run() {
            WindowManagerGlobalLock windowManagerGlobalLock = RootWindowContainer.this.mService.mGlobalLock;
            WindowManagerService.boostPriorityForLockedSection();
            synchronized (windowManagerGlobalLock) {
                try {
                    if (RootWindowContainer.this.mTaskLayersChanged) {
                        RootWindowContainer.this.mTaskLayersChanged = false;
                        RootWindowContainer.this.rankTaskLayers();
                    }
                } catch (Throwable th) {
                    WindowManagerService.resetPriorityAfterLockedSection();
                    throw th;
                }
            }
            WindowManagerService.resetPriorityAfterLockedSection();
        }
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
    private class AttachApplicationHelper implements Consumer<Task>, Predicate<ActivityRecord> {
        private WindowProcessController mApp;
        private boolean mHasActivityStarted;
        private RemoteException mRemoteException;
        private ActivityRecord mTop;

        private AttachApplicationHelper() {
        }

        void reset() {
            this.mHasActivityStarted = false;
            this.mRemoteException = null;
            this.mApp = null;
            this.mTop = null;
        }

        boolean process(WindowProcessController windowProcessController) throws RemoteException {
            this.mApp = windowProcessController;
            for (int childCount = RootWindowContainer.this.getChildCount() - 1; childCount >= 0; childCount--) {
                RootWindowContainer.this.getChildAt(childCount).forAllRootTasks((Consumer<Task>) this);
                RemoteException remoteException = this.mRemoteException;
                if (remoteException != null) {
                    throw remoteException;
                }
            }
            if (!this.mHasActivityStarted) {
                RootWindowContainer.this.ensureActivitiesVisible(null, 0, false);
            }
            return this.mHasActivityStarted;
        }

        @Override // java.util.function.Consumer
        public void accept(Task task) {
            if (this.mRemoteException == null && task.getVisibility(null) != 2) {
                this.mTop = task.topRunningActivity();
                task.forAllActivities((Predicate<ActivityRecord>) this);
            }
        }

        @Override // java.util.function.Predicate
        public boolean test(ActivityRecord activityRecord) {
            if (!activityRecord.finishing && activityRecord.showToCurrentUser() && activityRecord.visibleIgnoringKeyguard && activityRecord.app == null) {
                WindowProcessController windowProcessController = this.mApp;
                if (windowProcessController.mUid == activityRecord.info.applicationInfo.uid && windowProcessController.mName.equals(activityRecord.processName)) {
                    try {
                        activityRecord.getWrapper().getExtImpl().setNotifyHotStart(false);
                        if (RootWindowContainer.this.mTaskSupervisor.realStartActivityLocked(activityRecord, this.mApp, this.mTop == activityRecord && activityRecord.getTask().canBeResumed(activityRecord), true)) {
                            this.mHasActivityStarted = true;
                        }
                        return false;
                    } catch (RemoteException e) {
                        Slog.w(RootWindowContainer.TAG, "Exception in new application when starting activity " + this.mTop, e);
                        this.mRemoteException = e;
                        return true;
                    }
                }
            }
            return false;
        }
    }
}
