package com.android.server.wm;

import android.app.ActivityManager;
import android.app.IApplicationThread;
import android.app.PictureInPictureParams;
import android.graphics.Point;
import android.graphics.Rect;
import android.hardware.HardwareBuffer;
import android.os.Binder;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.IRemoteCallback;
import android.os.RemoteException;
import android.os.SystemClock;
import android.os.SystemProperties;
import android.os.Trace;
import android.util.ArrayMap;
import android.util.ArraySet;
import android.util.Slog;
import android.util.SparseArray;
import android.view.SurfaceControl;
import android.view.WindowManager;
import android.window.ScreenCapture;
import android.window.TransitionInfo;
import android.window.WindowContainerToken;
import android.window.WindowContainerTransaction;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.graphics.ColorUtils;
import com.android.internal.policy.TransitionAnimation;
import com.android.internal.protolog.ProtoLogGroup;
import com.android.internal.protolog.ProtoLogImpl;
import com.android.internal.util.function.pooled.PooledLambda;
import com.android.server.inputmethod.InputMethodManagerInternal;
import com.android.server.policy.WindowManagerPolicy;
import com.android.server.statusbar.StatusBarManagerInternal;
import com.android.server.wm.ActivityRecord;
import com.android.server.wm.BLASTSyncEngine;
import com.android.server.wm.DisplayArea;
import com.android.server.wm.ITransitionExt;
import com.android.server.wm.TransitionController;
import com.android.server.wm.WindowContainer;
import java.io.PrintWriter;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Predicate;
import system.ext.loader.core.ExtLoader;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
public class Transition implements BLASTSyncEngine.TransactionReadyListener {
    private static final int CAPTIRE_LAYERS_FOR_SCREEN_ROTATION = -222;
    private static final String DEFAULT_PACKAGE = "android";
    static final int PARALLEL_TYPE_MUTUAL = 1;
    static final int PARALLEL_TYPE_NONE = 0;
    static final int PARALLEL_TYPE_RECENTS = 2;
    private static final int STATE_ABORT = 3;
    private static final int STATE_COLLECTING = 0;
    private static final int STATE_FINISHED = 4;
    private static final int STATE_PENDING = -1;
    private static final int STATE_PLAYING = 2;
    private static final int STATE_STARTED = 1;
    private static final String TAG = "Transition";
    private static final String TRACE_NAME_PLAY_TRANSITION = "playing";
    int mAnimationTrack;
    private final TransitionController mController;
    private int mFlags;
    private boolean mForcePlaying;
    boolean mIsPlayerEnabled;
    final TransitionController.Logger mLogger;
    private TransitionInfo.AnimationOptions mOverrideOptions;
    int mParallelCollectType;
    private IApplicationThread mRemoteAnimApp;
    long mStatusBarTransitionDelay;
    private final BLASTSyncEngine mSyncEngine;
    ArrayList<ChangeInfo> mTargets;
    private final Token mToken;
    private ArrayList<Task> mTransientHideTasks;
    private ITransitionExt mTransitionExt;
    private ITransitionSocExt mTransitionSocExt;
    private TransitionWrapper mTransitionWrapper;
    final int mType;
    private static final boolean PANIC_DEBUG = SystemProperties.getBoolean("persist.sys.assert.panic", false);
    private static ITransitionExt.IStaticExt mTransitionStaticExt = (ITransitionExt.IStaticExt) ExtLoader.type(ITransitionExt.IStaticExt.class).create();
    private int mSyncId = -1;
    private SurfaceControl.Transaction mStartTransaction = null;
    private SurfaceControl.Transaction mFinishTransaction = null;
    private SurfaceControl.Transaction mCleanupTransaction = null;
    final ArrayMap<WindowContainer, ChangeInfo> mChanges = new ArrayMap<>();
    final ArraySet<WindowContainer> mParticipants = new ArraySet<>();
    private final ArrayList<DisplayContent> mTargetDisplays = new ArrayList<>();
    private final ArrayList<Task> mOnTopTasksStart = new ArrayList<>();
    private final ArrayList<Task> mOnTopTasksAtReady = new ArrayList<>();
    private final ArraySet<WindowToken> mVisibleAtTransitionEndTokens = new ArraySet<>();
    private ArrayMap<ActivityRecord, Task> mTransientLaunches = null;
    private IRemoteCallback mClientAnimationStartCallback = null;
    private IRemoteCallback mClientAnimationFinishCallback = null;
    private int mState = -1;
    private final ReadyTracker mReadyTracker = new ReadyTracker();
    private int mRecentsDisplayId = -1;
    private boolean mCanPipOnFinish = true;
    private boolean mIsSeamlessRotation = false;
    private IContainerFreezer mContainerFreezer = null;
    private final SurfaceControl.Transaction mTmpTransaction = new SurfaceControl.Transaction();
    boolean mPriorVisibilityMightBeDirty = false;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
    public interface IContainerFreezer {
        void cleanUp(SurfaceControl.Transaction transaction);

        boolean freeze(WindowContainer windowContainer, Rect rect);
    }

    @Retention(RetentionPolicy.SOURCE)
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
    @interface ParallelType {
    }

    @Retention(RetentionPolicy.SOURCE)
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
    @interface TransitionState {
    }

    @TransitionInfo.TransitionMode
    private static int reduceMode(@TransitionInfo.TransitionMode int i) {
        if (i == 3) {
            return 1;
        }
        if (i != 4) {
            return i;
        }
        return 2;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Transition(int i, int i2, TransitionController transitionController, BLASTSyncEngine bLASTSyncEngine) {
        TransitionController.Logger logger = new TransitionController.Logger();
        this.mLogger = logger;
        this.mForcePlaying = false;
        this.mIsPlayerEnabled = true;
        this.mTransitionExt = (ITransitionExt) ExtLoader.type(ITransitionExt.class).base(this).create();
        this.mTransitionSocExt = (ITransitionSocExt) ExtLoader.type(ITransitionSocExt.class).base(this).create();
        this.mParallelCollectType = 0;
        this.mAnimationTrack = 0;
        this.mTransitionWrapper = new TransitionWrapper();
        this.mType = i;
        this.mFlags = i2;
        this.mController = transitionController;
        this.mSyncEngine = bLASTSyncEngine;
        this.mToken = new Token(this);
        logger.mCreateWallTimeMs = System.currentTimeMillis();
        logger.mCreateTimeNs = SystemClock.elapsedRealtimeNanos();
        this.mTransitionSocExt.hookInitPerf();
        transitionController.mTransitionControllerExt.addNextAppTransitionRequests(i);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static Transition fromBinder(IBinder iBinder) {
        if (iBinder == null) {
            return null;
        }
        try {
            return ((Token) iBinder).mTransition.get();
        } catch (ClassCastException e) {
            Slog.w(TAG, "Invalid transition token: " + iBinder, e);
            return null;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public IBinder getToken() {
        return this.mToken;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void addFlag(int i) {
        this.mTransitionExt.addFlag(i);
        this.mFlags = i | this.mFlags;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void calcParallelCollectType(WindowContainerTransaction windowContainerTransaction) {
        Bundle launchOptions;
        for (int i = 0; i < windowContainerTransaction.getHierarchyOps().size(); i++) {
            WindowContainerTransaction.HierarchyOp hierarchyOp = (WindowContainerTransaction.HierarchyOp) windowContainerTransaction.getHierarchyOps().get(i);
            if (hierarchyOp.getType() == 7 && (launchOptions = hierarchyOp.getLaunchOptions()) != null && !launchOptions.isEmpty() && launchOptions.getBoolean("android.activity.transientLaunch")) {
                if (ProtoLogCache.WM_DEBUG_WINDOW_TRANSITIONS_enabled) {
                    ProtoLogImpl.v(ProtoLogGroup.WM_DEBUG_WINDOW_TRANSITIONS, -186693085, 0, (String) null, (Object[]) null);
                }
                this.mParallelCollectType = 2;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setTransientLaunch(ActivityRecord activityRecord, final Task task) {
        if (this.mTransientLaunches == null) {
            this.mTransientLaunches = new ArrayMap<>();
            this.mTransientHideTasks = new ArrayList<>();
        }
        this.mTransientLaunches.put(activityRecord, task);
        setTransientLaunchToChanges(activityRecord);
        if (task != null) {
            final Task rootTask = activityRecord.getRootTask();
            task.getParent().forAllTasks(new Predicate() { // from class: com.android.server.wm.Transition$$ExternalSyntheticLambda1
                @Override // java.util.function.Predicate
                public final boolean test(Object obj) {
                    boolean lambda$setTransientLaunch$0;
                    lambda$setTransientLaunch$0 = Transition.this.lambda$setTransientLaunch$0(task, rootTask, (Task) obj);
                    return lambda$setTransientLaunch$0;
                }
            });
            for (int size = this.mChanges.size() - 1; size >= 0; size--) {
                updateTransientFlags(this.mChanges.valueAt(size));
            }
        }
        if (ProtoLogCache.WM_DEBUG_WINDOW_TRANSITIONS_enabled) {
            ProtoLogImpl.v(ProtoLogGroup.WM_DEBUG_WINDOW_TRANSITIONS, -779535710, 1, (String) null, new Object[]{Long.valueOf(this.mSyncId), String.valueOf(activityRecord)});
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ boolean lambda$setTransientLaunch$0(Task task, Task task2, Task task3) {
        if ((task3.isVisibleRequested() && !task3.isAlwaysOnTop() && !task3.getWindowConfiguration().tasksAreFloating()) || this.mTransitionExt.addTaskToTransientHideTasks(task3, task)) {
            if (task3.isRootTask() && task3 != task2) {
                this.mTransientHideTasks.add(task3);
            }
            if (task3.isLeafTask()) {
                collect(task3);
            }
        }
        return task3 == task;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean isInTransientHide(WindowContainer windowContainer) {
        ArrayList<Task> arrayList = this.mTransientHideTasks;
        if (arrayList == null) {
            return false;
        }
        for (int size = arrayList.size() - 1; size >= 0; size--) {
            Task task = this.mTransientHideTasks.get(size);
            if (windowContainer == task || windowContainer.isDescendantOf(task)) {
                return true;
            }
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean canApplyDim(Task task) {
        if (this.mTransientLaunches == null) {
            return true;
        }
        Dimmer dimmer = task.getDimmer();
        WindowContainer<?> host = dimmer != null ? dimmer.getHost() : null;
        if (host == null) {
            return false;
        }
        if (isInTransientHide(host)) {
            return true;
        }
        for (int size = this.mTransientLaunches.size() - 1; size >= 0; size--) {
            Task task2 = this.mTransientLaunches.keyAt(size).getTask();
            if (task2 != null && task2.canAffectSystemUiFlags()) {
                return false;
            }
        }
        return true;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean hasTransientLaunch() {
        ArrayMap<ActivityRecord, Task> arrayMap = this.mTransientLaunches;
        return (arrayMap == null || arrayMap.isEmpty()) ? false : true;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean isTransientLaunch(ActivityRecord activityRecord) {
        ArrayMap<ActivityRecord, Task> arrayMap = this.mTransientLaunches;
        return arrayMap != null && arrayMap.containsKey(activityRecord);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Task getTransientLaunchRestoreTarget(WindowContainer windowContainer) {
        if (this.mTransientLaunches == null) {
            return null;
        }
        for (int i = 0; i < this.mTransientLaunches.size(); i++) {
            if (this.mTransientLaunches.keyAt(i).isDescendantOf(windowContainer)) {
                return this.mTransientLaunches.valueAt(i);
            }
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean isOnDisplay(DisplayContent displayContent) {
        return this.mTargetDisplays.contains(displayContent);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setSeamlessRotation(WindowContainer windowContainer) {
        ChangeInfo changeInfo = this.mChanges.get(windowContainer);
        if (changeInfo == null) {
            return;
        }
        changeInfo.mFlags |= 1;
        onSeamlessRotating(windowContainer.getDisplayContent());
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void onSeamlessRotating(DisplayContent displayContent) {
        if (this.mSyncEngine.getSyncSet(this.mSyncId).mSyncMethod == 1) {
            return;
        }
        if (this.mContainerFreezer == null) {
            this.mContainerFreezer = new ScreenshotFreezer();
        }
        WindowState topFullscreenOpaqueWindow = displayContent.getDisplayPolicy().getTopFullscreenOpaqueWindow();
        if (topFullscreenOpaqueWindow != null) {
            this.mIsSeamlessRotation = true;
            topFullscreenOpaqueWindow.mSyncMethodOverride = 1;
            if (ProtoLogCache.WM_DEBUG_WINDOW_TRANSITIONS_enabled) {
                ProtoLogImpl.v(ProtoLogGroup.WM_DEBUG_WINDOW_TRANSITIONS, 2004282287, 0, (String) null, new Object[]{String.valueOf(topFullscreenOpaqueWindow.getName())});
            }
        }
    }

    private void setTransientLaunchToChanges(WindowContainer windowContainer) {
        while (windowContainer != null && this.mChanges.containsKey(windowContainer)) {
            if (windowContainer.asTask() == null && windowContainer.asActivityRecord() == null) {
                return;
            }
            this.mChanges.get(windowContainer).mFlags |= 2;
            windowContainer = windowContainer.getParent();
        }
    }

    void setContainerFreezer(IContainerFreezer iContainerFreezer) {
        this.mContainerFreezer = iContainerFreezer;
    }

    int getState() {
        return this.mState;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int getSyncId() {
        return this.mSyncId;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int getFlags() {
        return this.mFlags;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @VisibleForTesting
    public SurfaceControl.Transaction getStartTransaction() {
        return this.mStartTransaction;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @VisibleForTesting
    public SurfaceControl.Transaction getFinishTransaction() {
        return this.mFinishTransaction;
    }

    boolean isPending() {
        return this.mState == -1;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean isCollecting() {
        int i = this.mState;
        return i == 0 || i == 1;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean isAborted() {
        return this.mState == 3;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean isStarted() {
        return this.mState == 1;
    }

    boolean isPlaying() {
        return this.mState == 2;
    }

    boolean isFinished() {
        return this.mState == 4;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void startCollecting(long j) {
        if (this.mState != -1) {
            throw new IllegalStateException("Attempting to re-use a transition");
        }
        this.mState = 0;
        int startSyncSet = this.mSyncEngine.startSyncSet(this, j, TAG, this.mParallelCollectType != 0);
        this.mSyncId = startSyncSet;
        this.mSyncEngine.setSyncMethod(startSyncSet, TransitionController.SYNC_METHOD);
        TransitionController.Logger logger = this.mLogger;
        logger.mSyncId = this.mSyncId;
        logger.mCollectTimeNs = SystemClock.elapsedRealtimeNanos();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void start() {
        int i = this.mState;
        if (i < 0) {
            throw new IllegalStateException("Can't start Transition which isn't collecting.");
        }
        if (i >= 1) {
            Slog.w(TAG, "Transition already started id=" + this.mSyncId + " state=" + this.mState);
            return;
        }
        this.mState = 1;
        this.mTransitionSocExt.hookPerfHint(this.mType);
        if (ProtoLogCache.WM_DEBUG_WINDOW_TRANSITIONS_enabled) {
            ProtoLogImpl.v(ProtoLogGroup.WM_DEBUG_WINDOW_TRANSITIONS, 996960396, 1, (String) null, new Object[]{Long.valueOf(this.mSyncId)});
        }
        applyReady();
        this.mLogger.mStartTimeNs = SystemClock.elapsedRealtimeNanos();
        this.mController.updateAnimatingState(this.mTmpTransaction);
        SurfaceControl.mergeToGlobalTransaction(this.mTmpTransaction);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void collect(WindowContainer windowContainer) {
        if (this.mState < 0) {
            throw new IllegalStateException("Transition hasn't started collecting.");
        }
        if (isCollecting()) {
            if (ProtoLogCache.WM_DEBUG_WINDOW_TRANSITIONS_enabled) {
                ProtoLogImpl.v(ProtoLogGroup.WM_DEBUG_WINDOW_TRANSITIONS, -1567866547, 1, (String) null, new Object[]{Long.valueOf(this.mSyncId), String.valueOf(windowContainer)});
            }
            for (WindowContainer<?> animatableParent = getAnimatableParent(windowContainer); animatableParent != null && !this.mChanges.containsKey(animatableParent); animatableParent = getAnimatableParent(animatableParent)) {
                ChangeInfo changeInfo = new ChangeInfo(animatableParent);
                updateTransientFlags(changeInfo);
                this.mChanges.put(animatableParent, changeInfo);
                if (isReadyGroup(animatableParent)) {
                    this.mReadyTracker.addGroup(animatableParent);
                    if (ProtoLogCache.WM_DEBUG_WINDOW_TRANSITIONS_enabled) {
                        ProtoLogImpl.v(ProtoLogGroup.WM_DEBUG_WINDOW_TRANSITIONS, -1442613680, 1, (String) null, new Object[]{Long.valueOf(this.mSyncId), String.valueOf(animatableParent)});
                    }
                }
            }
            if (this.mParticipants.contains(windowContainer)) {
                return;
            }
            if ((!isWallpaper(windowContainer) || this.mParticipants.contains(windowContainer.mDisplayContent)) && !isInTransientHide(windowContainer)) {
                this.mSyncEngine.addToSyncSet(this.mSyncId, windowContainer);
            }
            ChangeInfo changeInfo2 = this.mChanges.get(windowContainer);
            if (changeInfo2 == null) {
                changeInfo2 = new ChangeInfo(windowContainer);
                updateTransientFlags(changeInfo2);
                this.mChanges.put(windowContainer, changeInfo2);
            }
            this.mParticipants.add(windowContainer);
            recordDisplay(windowContainer.getDisplayContent());
            if (changeInfo2.mShowWallpaper) {
                List<WindowState> allTopWallpapers = windowContainer.getDisplayContent().mWallpaperController.getAllTopWallpapers();
                for (int size = allTopWallpapers.size() - 1; size >= 0; size--) {
                    collect(allTopWallpapers.get(size).mToken);
                }
            }
        }
    }

    private void updateTransientFlags(ChangeInfo changeInfo) {
        WindowContainer windowContainer = changeInfo.mContainer;
        if (!(windowContainer.asTaskFragment() == null && windowContainer.asActivityRecord() == null) && isInTransientHide(windowContainer)) {
            changeInfo.mFlags |= 4;
        }
    }

    private void recordDisplay(DisplayContent displayContent) {
        if (displayContent == null || this.mTargetDisplays.contains(displayContent)) {
            return;
        }
        this.mTargetDisplays.add(displayContent);
        addOnTopTasks(displayContent, this.mOnTopTasksStart);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void recordTaskOrder(WindowContainer windowContainer) {
        recordDisplay(windowContainer.getDisplayContent());
    }

    private static void addOnTopTasks(Task task, ArrayList<Task> arrayList) {
        for (int childCount = task.getChildCount() - 1; childCount >= 0; childCount--) {
            Task asTask = task.getChildAt(childCount).asTask();
            if (asTask == null) {
                return;
            }
            if (!asTask.getWindowConfiguration().isAlwaysOnTop()) {
                arrayList.add(asTask);
                addOnTopTasks(asTask, arrayList);
                return;
            }
        }
    }

    private static void addOnTopTasks(DisplayContent displayContent, ArrayList<Task> arrayList) {
        Task rootTask = displayContent.getRootTask(new Predicate() { // from class: com.android.server.wm.Transition$$ExternalSyntheticLambda9
            @Override // java.util.function.Predicate
            public final boolean test(Object obj) {
                boolean lambda$addOnTopTasks$1;
                lambda$addOnTopTasks$1 = Transition.lambda$addOnTopTasks$1((Task) obj);
                return lambda$addOnTopTasks$1;
            }
        });
        if (rootTask == null) {
            return;
        }
        arrayList.add(rootTask);
        addOnTopTasks(rootTask, arrayList);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ boolean lambda$addOnTopTasks$1(Task task) {
        return !task.getWindowConfiguration().isAlwaysOnTop();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void collectExistenceChange(WindowContainer windowContainer) {
        if (this.mState >= 2) {
            return;
        }
        if (ProtoLogCache.WM_DEBUG_WINDOW_TRANSITIONS_enabled) {
            ProtoLogImpl.v(ProtoLogGroup.WM_DEBUG_WINDOW_TRANSITIONS, -354571697, 1, (String) null, new Object[]{Long.valueOf(this.mSyncId), String.valueOf(windowContainer)});
        }
        collect(windowContainer);
        if (this.mChanges.get(windowContainer) == null) {
            return;
        }
        this.mChanges.get(windowContainer).mExistenceChanged = true;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void collectVisibleChange(WindowContainer windowContainer) {
        StartingData startingData;
        if (this.mSyncEngine.getSyncSet(this.mSyncId).mSyncMethod == 1 || windowContainer.mDisplayContent == null || !isInTransition(windowContainer)) {
            return;
        }
        if (!windowContainer.mDisplayContent.getDisplayPolicy().isScreenOnFully() || windowContainer.mDisplayContent.getDisplayInfo().state == 1) {
            this.mFlags |= 1024;
            return;
        }
        if (windowContainer.asActivityRecord() == null || (startingData = windowContainer.asActivityRecord().mStartingData) == null || startingData.mAssociatedTask == null) {
            if (this.mContainerFreezer == null) {
                this.mContainerFreezer = new ScreenshotFreezer();
            }
            ChangeInfo changeInfo = this.mChanges.get(windowContainer);
            if (changeInfo != null && changeInfo.mVisible && windowContainer.isVisibleRequested()) {
                Trace.traceBegin(32L, "WMS.doStartFreezingDisplay");
                this.mContainerFreezer.freeze(windowContainer, changeInfo.mAbsoluteBounds);
                Trace.traceEnd(32L);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void collectReparentChange(WindowContainer windowContainer, WindowContainer windowContainer2) {
        WindowContainer windowContainer3;
        if (this.mChanges.containsKey(windowContainer)) {
            ChangeInfo changeInfo = this.mChanges.get(windowContainer);
            WindowContainer windowContainer4 = changeInfo.mStartParent;
            if (windowContainer4 == null || windowContainer4.isAttached()) {
                windowContainer3 = changeInfo.mStartParent;
            } else {
                windowContainer3 = changeInfo.mCommonAncestor;
            }
            if (windowContainer3 == null || !windowContainer3.isAttached()) {
                Slog.w(TAG, "Trying to collect reparenting of a window after the previous parent has been detached: " + windowContainer);
                return;
            }
            if (windowContainer3 == windowContainer2) {
                Slog.w(TAG, "Trying to collect reparenting of a window that has not been reparented: " + windowContainer);
                return;
            }
            if (!windowContainer2.isAttached()) {
                Slog.w(TAG, "Trying to collect reparenting of a window that is not attached after reparenting: " + windowContainer);
                return;
            }
            while (windowContainer3 != windowContainer2 && !windowContainer3.isDescendantOf(windowContainer2)) {
                windowContainer2 = windowContainer2.getParent();
            }
            changeInfo.mCommonAncestor = windowContainer2;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean isInTransition(WindowContainer windowContainer) {
        while (windowContainer != null) {
            if (this.mParticipants.contains(windowContainer)) {
                return true;
            }
            windowContainer = windowContainer.getParent();
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setKnownConfigChanges(WindowContainer<?> windowContainer, int i) {
        ChangeInfo changeInfo = this.mChanges.get(windowContainer);
        if (changeInfo != null) {
            changeInfo.mKnownConfigChanges = i;
        }
    }

    private void sendRemoteCallback(IRemoteCallback iRemoteCallback) {
        if (iRemoteCallback == null) {
            return;
        }
        this.mController.mAtm.mH.sendMessage(PooledLambda.obtainMessage(new Consumer() { // from class: com.android.server.wm.Transition$$ExternalSyntheticLambda2
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                Transition.lambda$sendRemoteCallback$2((IRemoteCallback) obj);
            }
        }, iRemoteCallback));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$sendRemoteCallback$2(IRemoteCallback iRemoteCallback) {
        try {
            iRemoteCallback.sendResult((Bundle) null);
        } catch (RemoteException unused) {
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setOverrideAnimation(TransitionInfo.AnimationOptions animationOptions, IRemoteCallback iRemoteCallback, IRemoteCallback iRemoteCallback2) {
        if (isCollecting()) {
            this.mOverrideOptions = animationOptions;
            sendRemoteCallback(this.mClientAnimationStartCallback);
            this.mClientAnimationStartCallback = iRemoteCallback;
            this.mClientAnimationFinishCallback = iRemoteCallback2;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setReady(WindowContainer windowContainer, boolean z) {
        if (!isCollecting() || this.mSyncId < 0) {
            return;
        }
        this.mReadyTracker.setReadyFrom(windowContainer, z);
        applyReady();
    }

    private void applyReady() {
        if (this.mState < 1) {
            return;
        }
        boolean allReady = this.mReadyTracker.allReady();
        if (ProtoLogCache.WM_DEBUG_WINDOW_TRANSITIONS_enabled) {
            ProtoLogImpl.v(ProtoLogGroup.WM_DEBUG_WINDOW_TRANSITIONS, -874888131, 7, (String) null, new Object[]{Boolean.valueOf(allReady), Long.valueOf(this.mSyncId)});
        }
        if (this.mSyncEngine.setReady(this.mSyncId, allReady) && allReady) {
            this.mLogger.mReadyTimeNs = SystemClock.elapsedRealtimeNanos();
            this.mOnTopTasksAtReady.clear();
            for (int i = 0; i < this.mTargetDisplays.size(); i++) {
                addOnTopTasks(this.mTargetDisplays.get(i), this.mOnTopTasksAtReady);
            }
            this.mController.onTransitionPopulated(this);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setAllReady() {
        if (!isCollecting() || this.mSyncId < 0) {
            return;
        }
        this.mReadyTracker.setAllReady();
        applyReady();
    }

    @VisibleForTesting
    boolean allReady() {
        return this.mReadyTracker.allReady();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean isPopulated() {
        return this.mState >= 1 && this.mReadyTracker.allReady();
    }

    private void buildFinishTransaction(SurfaceControl.Transaction transaction, TransitionInfo transitionInfo) {
        Point point = new Point();
        ArraySet arraySet = new ArraySet();
        int size = this.mTargets.size();
        while (true) {
            size--;
            if (size < 0) {
                break;
            }
            WindowContainer windowContainer = this.mTargets.get(size).mContainer;
            if (windowContainer.getParent() != null) {
                SurfaceControl leashSurface = getLeashSurface(windowContainer, null);
                transaction.reparent(leashSurface, getOrigParentSurface(windowContainer));
                transaction.setLayer(leashSurface, windowContainer.getLastLayer());
                windowContainer.getRelativePosition(point);
                transaction.setPosition(leashSurface, point.x, point.y);
                if (windowContainer.asTaskFragment() == null) {
                    transaction.setCrop(leashSurface, null);
                } else {
                    Rect resolvedOverrideBounds = windowContainer.getResolvedOverrideBounds();
                    transaction.setWindowCrop(leashSurface, resolvedOverrideBounds.width(), resolvedOverrideBounds.height());
                }
                transaction.setCornerRadius(leashSurface, 0.0f);
                transaction.setShadowRadius(leashSurface, 0.0f);
                transaction.setMatrix(leashSurface, 1.0f, 0.0f, 0.0f, 1.0f);
                transaction.setAlpha(leashSurface, 1.0f);
                if (windowContainer.isOrganized() && windowContainer.matchParentBounds()) {
                    transaction.setWindowCrop(leashSurface, -1, -1);
                }
                arraySet.add(windowContainer.getDisplayContent());
                this.mTransitionExt.buildFinishTransaction(transaction, transitionInfo, windowContainer, leashSurface);
            }
        }
        if (this.mContainerFreezer != null) {
            Trace.traceBegin(32L, "WMS.doStopFreezingDisplayLocked");
            this.mContainerFreezer.cleanUp(transaction);
            Trace.traceEnd(32L);
        }
        for (int size2 = arraySet.size() - 1; size2 >= 0; size2--) {
            if (arraySet.valueAt(size2) != null) {
                updateDisplayLayers((DisplayContent) arraySet.valueAt(size2), transaction);
            }
        }
        for (int i = 0; i < transitionInfo.getRootCount(); i++) {
            transaction.reparent(transitionInfo.getRoot(i).getLeash(), null);
        }
    }

    private static void updateDisplayLayers(DisplayContent displayContent, SurfaceControl.Transaction transaction) {
        displayContent.mTransitionController.mBuildingFinishLayers = true;
        try {
            displayContent.assignChildLayers(transaction);
        } finally {
            displayContent.mTransitionController.mBuildingFinishLayers = false;
        }
    }

    private static void buildCleanupTransaction(SurfaceControl.Transaction transaction, TransitionInfo transitionInfo) {
        int size = transitionInfo.getChanges().size();
        while (true) {
            size--;
            if (size < 0) {
                break;
            }
            TransitionInfo.Change change = (TransitionInfo.Change) transitionInfo.getChanges().get(size);
            if (change.getSnapshot() != null) {
                transaction.reparent(change.getSnapshot(), null);
            }
            if (change.hasFlags(32) && change.getStartRotation() != change.getEndRotation() && change.getContainer() != null) {
                transaction.unsetFixedTransformHint(WindowContainer.fromBinder(change.getContainer().asBinder()).asDisplayContent().mSurfaceControl);
            }
        }
        for (int rootCount = transitionInfo.getRootCount() - 1; rootCount >= 0; rootCount--) {
            SurfaceControl leash = transitionInfo.getRoot(rootCount).getLeash();
            if (leash != null) {
                transaction.reparent(leash, null);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setCanPipOnFinish(boolean z) {
        this.mCanPipOnFinish = z;
    }

    private boolean didCommitTransientLaunch() {
        if (this.mTransientLaunches == null) {
            return false;
        }
        for (int i = 0; i < this.mTransientLaunches.size(); i++) {
            if (this.mTransientLaunches.keyAt(i).isVisibleRequested()) {
                return true;
            }
        }
        return false;
    }

    private boolean checkEnterPipOnFinish(ActivityRecord activityRecord) {
        if (!this.mCanPipOnFinish || !activityRecord.isVisible() || activityRecord.getTask() == null || !activityRecord.isState(ActivityRecord.State.RESUMED)) {
            return false;
        }
        PictureInPictureParams pictureInPictureParams = activityRecord.pictureInPictureArgs;
        if (pictureInPictureParams != null && pictureInPictureParams.isAutoEnterEnabled()) {
            if (!activityRecord.getTask().isVisibleRequested() || didCommitTransientLaunch()) {
                activityRecord.supportsEnterPipOnTaskSwitch = true;
            }
            if (!activityRecord.checkEnterPictureInPictureState("enterPictureInPictureMode", true)) {
                return false;
            }
            int windowingMode = activityRecord.getTask().getWindowingMode();
            boolean enterPictureInPictureMode = this.mController.mAtm.enterPictureInPictureMode(activityRecord, activityRecord.pictureInPictureArgs, false, true);
            int windowingMode2 = activityRecord.getTask().getWindowingMode();
            if (windowingMode == 1 && windowingMode2 == 2 && this.mTransientLaunches != null && activityRecord.mDisplayContent.hasTopFixedRotationLaunchingApp()) {
                activityRecord.mDisplayContent.mPinnedTaskController.setEnterPipTransaction(null);
            }
            return enterPictureInPictureMode;
        }
        if ((!activityRecord.getTask().isVisibleRequested() || didCommitTransientLaunch()) && activityRecord.supportsPictureInPicture()) {
            activityRecord.supportsEnterPipOnTaskSwitch = true;
        }
        try {
            this.mController.mAtm.mTaskSupervisor.mUserLeaving = true;
            activityRecord.getTaskFragment().startPausing(false, null, "finishTransition");
            return false;
        } finally {
            this.mController.mAtm.mTaskSupervisor.mUserLeaving = false;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void finishTransition() {
        WindowState windowState;
        TaskDisplayArea taskDisplayArea;
        ChangeInfo changeInfo;
        ActivityRecord topNonFinishingActivity;
        if (Trace.isTagEnabled(32L) && this.mIsPlayerEnabled) {
            asyncTraceEnd(System.identityHashCode(this));
        }
        this.mLogger.mFinishTimeNs = SystemClock.elapsedRealtimeNanos();
        Handler handler = this.mController.mLoggerHandler;
        final TransitionController.Logger logger = this.mLogger;
        Objects.requireNonNull(logger);
        handler.post(new Runnable() { // from class: com.android.server.wm.Transition$$ExternalSyntheticLambda5
            @Override // java.lang.Runnable
            public final void run() {
                TransitionController.Logger.this.logOnFinish();
            }
        });
        this.mController.mTransitionTracer.logFinishedTransition(this);
        SurfaceControl.Transaction transaction = this.mStartTransaction;
        if (transaction != null) {
            transaction.close();
        }
        SurfaceControl.Transaction transaction2 = this.mFinishTransaction;
        if (transaction2 != null) {
            transaction2.close();
        }
        String str = null;
        this.mFinishTransaction = null;
        this.mStartTransaction = null;
        SurfaceControl.Transaction transaction3 = this.mCleanupTransaction;
        if (transaction3 != null) {
            transaction3.apply();
            this.mCleanupTransaction = null;
        }
        if (this.mState < 2) {
            throw new IllegalStateException("Can't finish a non-playing transition " + this.mSyncId);
        }
        this.mController.mFinishingTransition = this;
        ArrayList<Task> arrayList = this.mTransientHideTasks;
        if (arrayList != null && !arrayList.isEmpty()) {
            this.mController.mAtm.mRootWindowContainer.ensureActivitiesVisible(null, 0, true);
            for (int i = 0; i < this.mTransientHideTasks.size(); i++) {
                final Task task = this.mTransientHideTasks.get(i);
                task.forAllActivities(new Consumer() { // from class: com.android.server.wm.Transition$$ExternalSyntheticLambda6
                    @Override // java.util.function.Consumer
                    public final void accept(Object obj) {
                        Transition.this.lambda$finishTransition$3(task, (ActivityRecord) obj);
                    }
                });
            }
        }
        int i2 = 0;
        boolean z = false;
        boolean z2 = false;
        boolean z3 = false;
        boolean z4 = false;
        while (i2 < this.mParticipants.size()) {
            WindowContainer<?> valueAt = this.mParticipants.valueAt(i2);
            ActivityRecord asActivityRecord = valueAt.asActivityRecord();
            if (asActivityRecord != null) {
                Task task2 = asActivityRecord.getTask();
                if (task2 != null) {
                    boolean contains = this.mVisibleAtTransitionEndTokens.contains(asActivityRecord);
                    if (isTransientLaunch(asActivityRecord) && !asActivityRecord.isVisibleRequested() && (this.mController.inCollectingTransition(asActivityRecord) || this.mTransitionExt.forceVisibleAtTransitionEnd(this, asActivityRecord))) {
                        contains = true;
                    }
                    DisplayContent displayContent = asActivityRecord.mDisplayContent;
                    boolean z5 = displayContent == null || displayContent.getDisplayInfo().state == 1;
                    if ((!contains || z5) && !asActivityRecord.isVisibleRequested()) {
                        if (!checkEnterPipOnFinish(asActivityRecord)) {
                            if (ProtoLogCache.WM_DEBUG_WINDOW_TRANSITIONS_enabled) {
                                ProtoLogImpl.v(ProtoLogGroup.WM_DEBUG_WINDOW_TRANSITIONS, -532081937, 0, str, new Object[]{String.valueOf(asActivityRecord)});
                            }
                            SnapshotController snapshotController = this.mController.mSnapshotController;
                            if (this.mTransientLaunches != null && !task2.isVisibleRequested()) {
                                if (snapshotController.mTaskSnapshotController.getSnapshotCaptureTime(task2.mTaskId) < this.mLogger.mSendTimeNs) {
                                    if (this.mTransitionExt.checkIfNeedRecordSnapshot(task2.mTaskId)) {
                                        snapshotController.mTaskSnapshotController.recordSnapshot(task2, false);
                                    }
                                } else if (ProtoLogCache.WM_DEBUG_WINDOW_TRANSITIONS_enabled) {
                                    ProtoLogImpl.v(ProtoLogGroup.WM_DEBUG_WINDOW_TRANSITIONS, -1060529098, 1, (String) null, new Object[]{Long.valueOf(task2.mTaskId)});
                                }
                                snapshotController.mActivitySnapshotController.notifyAppVisibilityChanged(asActivityRecord, false);
                            }
                            asActivityRecord.commitVisibility(false, false, true);
                            this.mTransitionExt.hideDeferredWallpapersIfNeeded(valueAt, asActivityRecord, this.mController);
                            z = true;
                        } else {
                            z3 = true;
                        }
                    }
                    ChangeInfo changeInfo2 = this.mChanges.get(asActivityRecord);
                    if (changeInfo2 != null && changeInfo2.mVisible != contains) {
                        asActivityRecord.mEnteringAnimation = contains;
                        if (contains) {
                            asActivityRecord.forAllWindows(new Consumer() { // from class: com.android.server.wm.Transition$$ExternalSyntheticLambda7
                                @Override // java.util.function.Consumer
                                public final void accept(Object obj) {
                                    Transition.lambda$finishTransition$4((WindowState) obj);
                                }
                            }, true);
                        }
                    } else {
                        ArrayMap<ActivityRecord, Task> arrayMap = this.mTransientLaunches;
                        if (arrayMap != null && arrayMap.containsKey(asActivityRecord) && asActivityRecord.isVisible()) {
                            asActivityRecord.mEnteringAnimation = true;
                            if (!task2.isFocused() && asActivityRecord.isTopRunningActivity()) {
                                this.mController.mAtm.setLastResumedActivityUncheckLocked(asActivityRecord, "transitionFinished");
                            }
                            z2 = true;
                        }
                    }
                }
            } else if (valueAt.asDisplayContent() != null) {
                z4 = true;
            } else {
                WallpaperWindowToken asWallpaperToken = valueAt.asWallpaperToken();
                if (asWallpaperToken != null) {
                    if (!this.mVisibleAtTransitionEndTokens.contains(asWallpaperToken) && !asWallpaperToken.isVisibleRequested()) {
                        if (ProtoLogCache.WM_DEBUG_WINDOW_TRANSITIONS_enabled) {
                            ProtoLogImpl.v(ProtoLogGroup.WM_DEBUG_WINDOW_TRANSITIONS, 691515534, 0, (String) null, new Object[]{String.valueOf(asWallpaperToken)});
                        }
                        asWallpaperToken.commitVisibility(false);
                    }
                } else {
                    final Task asTask = valueAt.asTask();
                    if (asTask != null && asTask.isVisibleRequested() && asTask.inPinnedWindowingMode() && (topNonFinishingActivity = asTask.getTopNonFinishingActivity()) != null && !topNonFinishingActivity.inPinnedWindowingMode()) {
                        this.mController.mStateValidators.add(new Runnable() { // from class: com.android.server.wm.Transition$$ExternalSyntheticLambda8
                            @Override // java.lang.Runnable
                            public final void run() {
                                Transition.lambda$finishTransition$5(Task.this);
                            }
                        });
                    }
                }
            }
            i2++;
            str = null;
        }
        if (z) {
            this.mController.onCommittedInvisibles();
        }
        if (z2) {
            if (z3) {
                this.mController.mAtm.getTaskChangeNotificationController().notifyTaskStackChanged();
            }
            this.mController.mAtm.stopAppSwitches();
            this.mController.mAtm.mRootWindowContainer.rankTaskLayers();
        }
        for (int i3 = 0; i3 < this.mParticipants.size(); i3++) {
            ActivityRecord asActivityRecord2 = this.mParticipants.valueAt(i3).asActivityRecord();
            if (asActivityRecord2 != null && (asActivityRecord2.isVisibleRequested() || !asActivityRecord2.isState(ActivityRecord.State.INITIALIZING))) {
                this.mController.dispatchLegacyAppTransitionFinished(asActivityRecord2);
            }
        }
        SurfaceControl.Transaction transaction4 = null;
        for (int i4 = 0; i4 < this.mParticipants.size(); i4++) {
            ActivityRecord asActivityRecord3 = this.mParticipants.valueAt(i4).asActivityRecord();
            if (asActivityRecord3 != null && asActivityRecord3.isVisible() && asActivityRecord3.getParent() != null) {
                if (transaction4 == null) {
                    transaction4 = asActivityRecord3.mWmService.mTransactionFactory.get();
                }
                asActivityRecord3.mActivityRecordInputSink.applyChangesToSurfaceIfChanged(transaction4);
            }
        }
        if (transaction4 != null) {
            transaction4.apply();
        }
        this.mController.mAtm.mTaskSupervisor.scheduleProcessStoppingAndFinishingActivitiesIfNeeded();
        sendRemoteCallback(this.mClientAnimationFinishCallback);
        legacyRestoreNavigationBarFromApp();
        int i5 = this.mRecentsDisplayId;
        if (i5 != -1) {
            this.mController.mAtm.mRootWindowContainer.getDisplayContent(i5).getInputMonitor().setActiveRecents(null, null);
        }
        ArrayMap<ActivityRecord, Task> arrayMap2 = this.mTransientLaunches;
        if (arrayMap2 != null) {
            for (int size = arrayMap2.size() - 1; size >= 0; size--) {
                Task task3 = this.mTransientLaunches.keyAt(size).getTask();
                if (task3 != null) {
                    task3.setCanAffectSystemUiFlags(true);
                }
            }
        }
        for (int i6 = 0; i6 < this.mTargetDisplays.size(); i6++) {
            DisplayContent displayContent2 = this.mTargetDisplays.get(i6);
            AsyncRotationController asyncRotationController = displayContent2.getAsyncRotationController();
            if (asyncRotationController != null && containsChangeFor(displayContent2, this.mTargets)) {
                asyncRotationController.onTransitionFinished();
            }
            if (z4 && displayContent2.mDisplayRotationCompatPolicy != null && (changeInfo = this.mChanges.get(displayContent2)) != null && changeInfo.mRotation != displayContent2.getWindowConfiguration().getRotation()) {
                displayContent2.mDisplayRotationCompatPolicy.onScreenRotationAnimationFinished();
            }
            if (this.mTransientLaunches != null) {
                InsetsControlTarget imeTarget = displayContent2.getImeTarget(2);
                int i7 = 0;
                while (true) {
                    if (i7 >= this.mTransientLaunches.size()) {
                        windowState = null;
                        taskDisplayArea = null;
                        break;
                    } else {
                        if (this.mTransientLaunches.keyAt(i7).getDisplayContent() == displayContent2) {
                            windowState = displayContent2.computeImeTarget(true);
                            taskDisplayArea = this.mTransientLaunches.keyAt(i6).getTaskDisplayArea();
                            break;
                        }
                        i7++;
                    }
                }
                if (this.mRecentsDisplayId != -1 && imeTarget == windowState) {
                    InputMethodManagerInternal.get().updateImeWindowStatus(false);
                }
                if (!z2 && taskDisplayArea != null) {
                    taskDisplayArea.pauseBackTasks(null);
                }
            }
            displayContent2.removeImeSurfaceImmediately();
            displayContent2.handleCompleteDeferredRemoval();
        }
        validateKeyguardOcclusion();
        validateVisibility();
        this.mState = 4;
        this.mTransitionSocExt.hookPerfLockRelease();
        if (z4 && !this.mController.useShellTransitionsRotation()) {
            this.mController.mAtm.mWindowManager.updateRotation(false, false);
        }
        cleanUpInternal();
        this.mController.updateAnimatingState(this.mTmpTransaction);
        this.mTmpTransaction.apply();
        this.mController.mAtm.mBackNavigationController.onTransitionFinish(this.mTargets, this);
        this.mTransitionExt.finishTransition(this, this.mParticipants);
        this.mController.mFinishingTransition = null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$finishTransition$3(Task task, ActivityRecord activityRecord) {
        if (this.mParticipants.contains(activityRecord.getTask())) {
            if (task.isVisibleRequested()) {
                if (activityRecord.isVisibleRequested()) {
                    return;
                }
                this.mController.mValidateCommitVis.add(activityRecord);
                return;
            }
            this.mParticipants.add(activityRecord);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$finishTransition$4(WindowState windowState) {
        ActivityRecord activityRecord = windowState.mActivityRecord;
        if (windowState.mWinAnimator.mDrawState == 3 && activityRecord != null && activityRecord.canShowWindows()) {
            Slog.w(TAG, "Transition is finish,but window not show " + windowState);
            windowState.performShowLocked();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$finishTransition$5(Task task) {
        if (task.isAttached() && task.isVisibleRequested() && task.inPinnedWindowingMode()) {
            ActivityRecord topNonFinishingActivity = task.getTopNonFinishingActivity();
            if (topNonFinishingActivity.inPinnedWindowingMode()) {
                return;
            }
            Slog.e(TAG, "Enter-PIP was started but not completed, this is a Shell/SysUI bug. This state breaks gesture-nav, so attempting clean-up.");
            task.abortPipEnter(topNonFinishingActivity);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void abort() {
        int i = this.mState;
        if (i == 3) {
            return;
        }
        if (i == -1) {
            this.mState = 3;
            return;
        }
        if (i != 0 && i != 1) {
            throw new IllegalStateException("Too late to abort. state=" + this.mState);
        }
        if (ProtoLogCache.WM_DEBUG_WINDOW_TRANSITIONS_enabled) {
            ProtoLogImpl.v(ProtoLogGroup.WM_DEBUG_WINDOW_TRANSITIONS, -863438038, 1, (String) null, new Object[]{Long.valueOf(this.mSyncId)});
        }
        this.mState = 3;
        this.mLogger.mAbortTimeNs = SystemClock.elapsedRealtimeNanos();
        this.mController.mTransitionTracer.logAbortedTransition(this);
        this.mSyncEngine.abort(this.mSyncId);
        this.mController.dispatchLegacyAppTransitionCancelled();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void playNow() {
        int i = this.mState;
        if (i != 0 && i != 1) {
            if (this.mType == 12) {
                Slog.i(TAG, "playNow() mType == TRANSIT_SLEEP,mState=" + this.mState);
                return;
            }
            return;
        }
        if (ProtoLogCache.WM_DEBUG_WINDOW_TRANSITIONS_enabled) {
            ProtoLogImpl.v(ProtoLogGroup.WM_DEBUG_WINDOW_TRANSITIONS, -894942237, 1, (String) null, new Object[]{Long.valueOf(this.mSyncId)});
        }
        this.mForcePlaying = true;
        setAllReady();
        if (this.mState == 0) {
            start();
        }
        this.mSyncEngine.onSurfacePlacement();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean isForcePlaying() {
        return this.mForcePlaying;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setRemoteAnimationApp(IApplicationThread iApplicationThread) {
        this.mRemoteAnimApp = iApplicationThread;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public IApplicationThread getRemoteAnimationApp() {
        return this.mRemoteAnimApp;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setNoAnimation(WindowContainer windowContainer) {
        ChangeInfo changeInfo = this.mChanges.get(windowContainer);
        if (changeInfo == null) {
            throw new IllegalStateException("Can't set no-animation property of non-participant");
        }
        changeInfo.mFlags |= 8;
    }

    @VisibleForTesting(visibility = VisibleForTesting.Visibility.PRIVATE)
    static boolean containsChangeFor(WindowContainer windowContainer, ArrayList<ChangeInfo> arrayList) {
        for (int size = arrayList.size() - 1; size >= 0; size--) {
            if (arrayList.get(size).mContainer == windowContainer) {
                return true;
            }
        }
        return false;
    }

    @Override // com.android.server.wm.BLASTSyncEngine.TransactionReadyListener
    public void onTransactionReady(int i, SurfaceControl.Transaction transaction) {
        ChangeInfo changeInfo;
        if (i != this.mSyncId) {
            Slog.e(TAG, "Unexpected Sync ID " + i + ". Expected " + this.mSyncId);
            return;
        }
        commitVisibleActivities(transaction);
        commitVisibleWallpapers();
        DisplayContent defaultDisplay = !this.mTargetDisplays.isEmpty() ? this.mTargetDisplays.get(0) : this.mController.mAtm.mRootWindowContainer.getDefaultDisplay();
        if (this.mState == 3) {
            this.mController.onAbort(this);
            defaultDisplay.getPendingTransaction().merge(transaction);
            this.mSyncId = -1;
            this.mOverrideOptions = null;
            cleanUpInternal();
            return;
        }
        this.mState = 2;
        this.mStartTransaction = transaction;
        this.mFinishTransaction = this.mController.mAtm.mWindowManager.mTransactionFactory.get();
        if (defaultDisplay.isKeyguardLocked()) {
            this.mFlags |= 64;
        }
        collectOrderChanges(this.mController.mWaitingTransitions.isEmpty());
        if (this.mPriorVisibilityMightBeDirty) {
            updatePriorVisibility();
        }
        this.mFlags = this.mTransitionExt.updateFlag(this.mFlags, this.mController.mAtm.mRootWindowContainer.getDefaultDisplay());
        ArrayList<ChangeInfo> calculateTargets = calculateTargets(this.mParticipants, this.mChanges);
        this.mTargets = calculateTargets;
        this.mController.mAtm.mBackNavigationController.onTransactionReady(this, calculateTargets);
        TransitionInfo calculateTransitionInfo = calculateTransitionInfo(this.mTransitionExt.fixTransitType(this.mType, this.mController), this.mFlags, this.mTargets, transaction);
        calculateTransitionInfo.setDebugId(this.mSyncId);
        this.mController.assignTrack(this, calculateTransitionInfo);
        this.mController.moveToPlaying(this);
        this.mTargetDisplays.clear();
        for (int i2 = 0; i2 < calculateTransitionInfo.getRootCount(); i2++) {
            this.mTargetDisplays.add(this.mController.mAtm.mRootWindowContainer.getDisplayContent(calculateTransitionInfo.getRoot(i2).getDisplayId()));
        }
        TransitionInfo.AnimationOptions animationOptions = this.mOverrideOptions;
        if (animationOptions != null) {
            calculateTransitionInfo.setAnimationOptions(animationOptions);
            if (this.mOverrideOptions.getType() == 12) {
                int i3 = 0;
                while (true) {
                    if (i3 >= this.mTargets.size()) {
                        break;
                    }
                    TransitionInfo.Change change = (TransitionInfo.Change) calculateTransitionInfo.getChanges().get(i3);
                    ActivityRecord asActivityRecord = this.mTargets.get(i3).mContainer.asActivityRecord();
                    if (asActivityRecord == null || change.getMode() != 1) {
                        i3++;
                    } else {
                        change.setFlags(change.getFlags() | (asActivityRecord.mUserId == asActivityRecord.mWmService.mCurrentUserId ? 4096 : 8192));
                    }
                }
            }
        }
        this.mTransitionExt.onTransactionReady(this, this.mTargets, calculateTransitionInfo, transaction);
        for (int i4 = 0; i4 < this.mTargetDisplays.size(); i4++) {
            handleLegacyRecentsStartBehavior(this.mTargetDisplays.get(i4), calculateTransitionInfo);
            if (this.mRecentsDisplayId != -1) {
                break;
            }
        }
        sendRemoteCallback(this.mClientAnimationStartCallback);
        for (int size = this.mParticipants.size() - 1; size >= 0; size--) {
            ActivityRecord asActivityRecord2 = this.mParticipants.valueAt(size).asActivityRecord();
            if (asActivityRecord2 != null && asActivityRecord2.isVisibleRequested()) {
                transaction.show(asActivityRecord2.getSurfaceControl());
                for (WindowContainer parent = asActivityRecord2.getParent(); parent != null && !containsChangeFor(parent, this.mTargets); parent = parent.getParent()) {
                    if (parent.getSurfaceControl() != null) {
                        transaction.show(parent.getSurfaceControl());
                    }
                }
            }
        }
        if (this.mTransientLaunches == null) {
            for (int size2 = this.mParticipants.size() - 1; size2 >= 0; size2--) {
                WindowContainer valueAt = this.mParticipants.valueAt(size2);
                if (valueAt.asWindowToken() != null && valueAt.isVisibleRequested()) {
                    this.mVisibleAtTransitionEndTokens.add(valueAt.asWindowToken());
                }
            }
        }
        if (this.mTransientLaunches == null) {
            for (int size3 = this.mParticipants.size() - 1; size3 >= 0; size3--) {
                ActivityRecord asActivityRecord3 = this.mParticipants.valueAt(size3).asActivityRecord();
                if (asActivityRecord3 != null && asActivityRecord3.getTask() != null && !asActivityRecord3.getTask().isVisibleRequested() && (((changeInfo = this.mChanges.get(asActivityRecord3)) == null || changeInfo.mWindowingMode != 2) && this.mTransitionExt.checkIfNeedRecordSnapshot(asActivityRecord3.getTask().mTaskId))) {
                    this.mTransitionExt.recordTaskSnapShot(this.mController.mSnapshotController.mTaskSnapshotController, asActivityRecord3.getTask());
                }
            }
        }
        for (int i5 = 0; i5 < this.mTargetDisplays.size(); i5++) {
            DisplayContent displayContent = this.mTargetDisplays.get(i5);
            AsyncRotationController asyncRotationController = displayContent.getAsyncRotationController();
            if (asyncRotationController != null && containsChangeFor(displayContent, this.mTargets)) {
                asyncRotationController.setupStartTransaction(transaction);
            }
        }
        buildFinishTransaction(this.mFinishTransaction, calculateTransitionInfo);
        SurfaceControl.Transaction transaction2 = this.mController.mAtm.mWindowManager.mTransactionFactory.get();
        this.mCleanupTransaction = transaction2;
        buildCleanupTransaction(transaction2, calculateTransitionInfo);
        if (this.mController.getTransitionPlayer() != null && this.mIsPlayerEnabled) {
            this.mController.dispatchLegacyAppTransitionStarting(calculateTransitionInfo, this.mStatusBarTransitionDelay);
            try {
                if (ProtoLogCache.WM_DEBUG_WINDOW_TRANSITIONS_enabled) {
                    ProtoLogImpl.v(ProtoLogGroup.WM_DEBUG_WINDOW_TRANSITIONS, 1115248873, 0, (String) null, new Object[]{String.valueOf(calculateTransitionInfo)});
                }
                this.mLogger.mSendTimeNs = SystemClock.elapsedRealtimeNanos();
                this.mLogger.mInfo = calculateTransitionInfo;
                this.mTransitionExt.hookSetBinderUxFlag(-1, 1);
                this.mController.getTransitionPlayer().onTransitionReady(this.mToken, calculateTransitionInfo, transaction, this.mFinishTransaction);
                this.mTransitionExt.hookSetBinderUxFlag(-1, 0);
                if (Trace.isTagEnabled(32L)) {
                    asyncTraceBegin(TRACE_NAME_PLAY_TRANSITION, System.identityHashCode(this));
                }
            } catch (RemoteException unused) {
                postCleanupOnFailure();
            }
            for (int i6 = 0; i6 < this.mTargetDisplays.size(); i6++) {
                DisplayContent displayContent2 = this.mTargetDisplays.get(i6);
                AccessibilityController accessibilityController = displayContent2.mWmService.mAccessibilityController;
                if (accessibilityController.hasCallbacks()) {
                    accessibilityController.onWMTransition(displayContent2.getDisplayId(), this.mType);
                }
            }
        } else {
            if (!this.mIsPlayerEnabled) {
                this.mLogger.mSendTimeNs = SystemClock.uptimeNanos();
                if (ProtoLogCache.WM_DEBUG_WINDOW_TRANSITIONS_enabled) {
                    ProtoLogImpl.v(ProtoLogGroup.WM_DEBUG_WINDOW_TRANSITIONS, 378890013, 1, (String) null, new Object[]{Long.valueOf(this.mSyncId)});
                }
            }
            postCleanupOnFailure();
        }
        this.mOverrideOptions = null;
        reportStartReasonsToLogger();
        calculateTransitionInfo.releaseAnimSurfaces();
        Handler handler = this.mController.mLoggerHandler;
        final TransitionController.Logger logger = this.mLogger;
        Objects.requireNonNull(logger);
        handler.post(new Runnable() { // from class: com.android.server.wm.Transition$$ExternalSyntheticLambda10
            @Override // java.lang.Runnable
            public final void run() {
                TransitionController.Logger.this.logOnSend();
            }
        });
        if (this.mLogger.mInfo != null) {
            this.mController.mTransitionTracer.logSentTransition(this, this.mTargets);
        }
        this.mTransitionExt.setAnimThreadUxIfNeed(false);
    }

    /* JADX WARN: Removed duplicated region for block: B:28:0x008e  */
    @VisibleForTesting
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    void collectOrderChanges(boolean z) {
        boolean z2;
        int indexOfKey;
        if (this.mOnTopTasksStart.isEmpty()) {
            return;
        }
        int i = 0;
        while (true) {
            if (i >= this.mOnTopTasksAtReady.size()) {
                z2 = false;
                break;
            }
            if (!this.mOnTopTasksStart.contains(this.mOnTopTasksAtReady.get(i))) {
                z2 = true;
                break;
            }
            i++;
        }
        if (z2 || z) {
            ArrayList<Task> arrayList = new ArrayList<>();
            for (int i2 = 0; i2 < this.mTargetDisplays.size(); i2++) {
                addOnTopTasks(this.mTargetDisplays.get(i2), arrayList);
                int i3 = this.mTargetDisplays.get(i2).mDisplayId;
                ArrayList<Task> arrayList2 = this.mController.mLatestOnTopTasksReported.get(i3);
                for (int size = arrayList.size() - 1; size >= 0; size--) {
                    Task task = arrayList.get(size);
                    if (task.getDisplayId() == i3) {
                        if (arrayList2 == null) {
                            if (this.mOnTopTasksStart.contains(task)) {
                            }
                            this.mParticipants.add(task);
                            indexOfKey = this.mChanges.indexOfKey(task);
                            if (indexOfKey < 0) {
                                this.mChanges.put(task, new ChangeInfo(task));
                                indexOfKey = this.mChanges.indexOfKey(task);
                            }
                            this.mChanges.valueAt(indexOfKey).mFlags |= 32;
                        } else {
                            if (arrayList2.contains(task)) {
                            }
                            this.mParticipants.add(task);
                            indexOfKey = this.mChanges.indexOfKey(task);
                            if (indexOfKey < 0) {
                            }
                            this.mChanges.valueAt(indexOfKey).mFlags |= 32;
                        }
                    }
                }
                this.mController.mLatestOnTopTasksReported.put(i3, arrayList);
                arrayList = arrayList2 != null ? arrayList2 : new ArrayList<>();
                arrayList.clear();
            }
        }
    }

    private void postCleanupOnFailure() {
        this.mController.mAtm.mH.post(new Runnable() { // from class: com.android.server.wm.Transition$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                Transition.this.lambda$postCleanupOnFailure$6();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$postCleanupOnFailure$6() {
        WindowManagerGlobalLock windowManagerGlobalLock = this.mController.mAtm.mGlobalLock;
        WindowManagerService.boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                cleanUpOnFailure();
            } catch (Throwable th) {
                WindowManagerService.resetPriorityAfterLockedSection();
                throw th;
            }
        }
        WindowManagerService.resetPriorityAfterLockedSection();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void cleanUpOnFailure() {
        if (this.mState < 2) {
            return;
        }
        SurfaceControl.Transaction transaction = this.mStartTransaction;
        if (transaction != null) {
            transaction.apply();
        }
        SurfaceControl.Transaction transaction2 = this.mFinishTransaction;
        if (transaction2 != null) {
            transaction2.apply();
        }
        this.mController.finishTransition(this);
    }

    private void cleanUpInternal() {
        for (int i = 0; i < this.mChanges.size(); i++) {
            SurfaceControl surfaceControl = this.mChanges.valueAt(i).mSnapshot;
            if (surfaceControl != null) {
                surfaceControl.release();
            }
        }
        SurfaceControl.Transaction transaction = this.mCleanupTransaction;
        if (transaction != null) {
            transaction.apply();
            this.mCleanupTransaction = null;
        }
    }

    private void commitVisibleActivities(SurfaceControl.Transaction transaction) {
        for (int size = this.mParticipants.size() - 1; size >= 0; size--) {
            ActivityRecord asActivityRecord = this.mParticipants.valueAt(size).asActivityRecord();
            if (asActivityRecord != null && asActivityRecord.getTask() != null) {
                if (asActivityRecord.isVisibleRequested()) {
                    asActivityRecord.commitVisibility(true, false, true);
                    asActivityRecord.commitFinishDrawing(transaction);
                }
                asActivityRecord.getTask().setDeferTaskAppear(false);
            }
        }
    }

    private void commitVisibleWallpapers() {
        boolean shouldWallpaperBeVisible = shouldWallpaperBeVisible();
        for (int size = this.mParticipants.size() - 1; size >= 0; size--) {
            WallpaperWindowToken asWallpaperToken = this.mParticipants.valueAt(size).asWallpaperToken();
            if (asWallpaperToken != null) {
                asWallpaperToken.waitingToShow = false;
                if (!asWallpaperToken.isVisible() && asWallpaperToken.isVisibleRequested()) {
                    asWallpaperToken.commitVisibility(shouldWallpaperBeVisible);
                }
            }
        }
    }

    private boolean shouldWallpaperBeVisible() {
        for (int size = this.mParticipants.size() - 1; size >= 0; size--) {
            if (this.mParticipants.valueAt(size).showWallpaper()) {
                return true;
            }
        }
        return false;
    }

    private void handleLegacyRecentsStartBehavior(DisplayContent displayContent, TransitionInfo transitionInfo) {
        WindowState navigationBar;
        WindowToken windowToken;
        Task fromWindowContainerToken;
        if ((this.mFlags & 128) == 0) {
            return;
        }
        InputConsumerImpl inputConsumer = displayContent.getInputMonitor().getInputConsumer("recents_animation_input_consumer");
        WindowContainer windowContainer = null;
        ActivityRecord activityRecord = null;
        if (inputConsumer != null) {
            ActivityRecord activityRecord2 = null;
            for (int i = 0; i < transitionInfo.getChanges().size(); i++) {
                TransitionInfo.Change change = (TransitionInfo.Change) transitionInfo.getChanges().get(i);
                if (change.getTaskInfo() != null && (fromWindowContainerToken = Task.fromWindowContainerToken(((TransitionInfo.Change) transitionInfo.getChanges().get(i)).getTaskInfo().token)) != null) {
                    int i2 = change.getTaskInfo().topActivityType;
                    boolean z = i2 == 2 || i2 == 3;
                    if (z && activityRecord == null) {
                        activityRecord = fromWindowContainerToken.getTopVisibleActivity();
                    } else if (!z && activityRecord2 == null) {
                        activityRecord2 = fromWindowContainerToken.getTopNonFinishingActivity();
                    }
                }
            }
            if (activityRecord != null && activityRecord2 != null) {
                inputConsumer.mWindowHandle.touchableRegion.set(activityRecord2.getBounds());
                displayContent.getInputMonitor().setActiveRecents(activityRecord, activityRecord2);
            }
        }
        if (activityRecord == null) {
            return;
        }
        this.mRecentsDisplayId = displayContent.mDisplayId;
        if (displayContent.getDisplayPolicy().shouldAttachNavBarToAppDuringTransition() && displayContent.getAsyncRotationController() == null) {
            for (int i3 = 0; i3 < transitionInfo.getChanges().size(); i3++) {
                TransitionInfo.Change change2 = (TransitionInfo.Change) transitionInfo.getChanges().get(i3);
                if (change2.getTaskInfo() != null && change2.getTaskInfo().displayId == this.mRecentsDisplayId && change2.getTaskInfo().getActivityType() == 1 && (change2.getMode() == 2 || change2.getMode() == 4)) {
                    windowContainer = WindowContainer.fromBinder(change2.getContainer().asBinder());
                    break;
                }
            }
            if (windowContainer == null || windowContainer.inMultiWindowMode() || (navigationBar = displayContent.getDisplayPolicy().getNavigationBar()) == null || (windowToken = navigationBar.mToken) == null) {
                return;
            }
            this.mController.mNavigationBarAttachedToApp = true;
            windowToken.cancelAnimation();
            SurfaceControl.Transaction pendingTransaction = navigationBar.mToken.getPendingTransaction();
            SurfaceControl surfaceControl = navigationBar.mToken.getSurfaceControl();
            pendingTransaction.reparent(surfaceControl, windowContainer.getSurfaceControl());
            pendingTransaction.show(surfaceControl);
            DisplayArea.Tokens imeContainer = displayContent.getImeContainer();
            if (imeContainer.isVisible()) {
                pendingTransaction.setRelativeLayer(surfaceControl, imeContainer.getSurfaceControl(), 1);
            } else {
                pendingTransaction.setLayer(surfaceControl, Integer.MAX_VALUE);
            }
            StatusBarManagerInternal statusBarManagerInternal = displayContent.getDisplayPolicy().getStatusBarManagerInternal();
            if (statusBarManagerInternal != null) {
                statusBarManagerInternal.setNavigationBarLumaSamplingEnabled(this.mRecentsDisplayId, false);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void legacyRestoreNavigationBarFromApp() {
        TransitionController transitionController = this.mController;
        if (transitionController.mNavigationBarAttachedToApp) {
            boolean z = false;
            transitionController.mNavigationBarAttachedToApp = false;
            if (this.mRecentsDisplayId == -1) {
                Slog.e(TAG, "Reparented navigation bar without a valid display");
                this.mRecentsDisplayId = 0;
            }
            DisplayContent displayContent = this.mController.mAtm.mRootWindowContainer.getDisplayContent(this.mRecentsDisplayId);
            StatusBarManagerInternal statusBarManagerInternal = displayContent.getDisplayPolicy().getStatusBarManagerInternal();
            if (statusBarManagerInternal != null) {
                statusBarManagerInternal.setNavigationBarLumaSamplingEnabled(this.mRecentsDisplayId, true);
            }
            WindowState navigationBar = displayContent.getDisplayPolicy().getNavigationBar();
            if (navigationBar == null) {
                return;
            }
            navigationBar.setSurfaceTranslationY(0);
            WindowToken windowToken = navigationBar.mToken;
            if (windowToken == null) {
                return;
            }
            SurfaceControl.Transaction pendingTransaction = displayContent.getPendingTransaction();
            WindowContainer parent = windowToken.getParent();
            pendingTransaction.setLayer(windowToken.getSurfaceControl(), windowToken.getLastLayer());
            int i = 0;
            while (true) {
                if (i < this.mTargets.size()) {
                    Task asTask = this.mTargets.get(i).mContainer.asTask();
                    if (asTask != null && asTask.isActivityTypeHomeOrRecents()) {
                        z = asTask.isVisibleRequested();
                        break;
                    }
                    i++;
                } else {
                    break;
                }
            }
            AsyncRotationController asyncRotationController = displayContent.getAsyncRotationController();
            if (asyncRotationController != null) {
                asyncRotationController.accept(navigationBar);
            }
            if (z) {
                new NavBarFadeAnimationController(displayContent).fadeWindowToken(true);
            } else {
                pendingTransaction.reparent(windowToken.getSurfaceControl(), parent.getSurfaceControl());
            }
            displayContent.mWmService.scheduleAnimationLocked();
        }
    }

    private void reportStartReasonsToLogger() {
        int i;
        ArrayMap<WindowContainer, Integer> arrayMap = new ArrayMap<>();
        for (int size = this.mParticipants.size() - 1; size >= 0; size--) {
            ActivityRecord asActivityRecord = this.mParticipants.valueAt(size).asActivityRecord();
            if (asActivityRecord != null && asActivityRecord.isVisibleRequested()) {
                if (!(asActivityRecord.mStartingData instanceof SplashScreenStartingData) || asActivityRecord.mLastAllReadyAtSync) {
                    i = (asActivityRecord.isActivityTypeHomeOrRecents() && isTransientLaunch(asActivityRecord)) ? 5 : 2;
                } else {
                    i = 1;
                }
                arrayMap.put(asActivityRecord, Integer.valueOf(i));
            }
        }
        this.mController.mAtm.mTaskSupervisor.getActivityMetricsLogger().notifyTransitionStarting(arrayMap);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void dump(PrintWriter printWriter, String str, boolean z) {
        String str2 = "  " + str;
        printWriter.println(str2 + toString());
        for (int i = 0; i < this.mParticipants.size(); i++) {
            printWriter.println(str2 + this.mParticipants.valueAt(i));
        }
    }

    public String toString() {
        StringBuilder sb = new StringBuilder(64);
        sb.append("TransitionRecord{");
        sb.append(Integer.toHexString(System.identityHashCode(this)));
        sb.append(" id=" + this.mSyncId);
        sb.append(" type=" + WindowManager.transitTypeToString(this.mType));
        sb.append(" flags=0x" + Integer.toHexString(this.mFlags));
        sb.append('}');
        return sb.toString();
    }

    private static WindowContainer<?> getAnimatableParent(WindowContainer<?> windowContainer) {
        WindowContainer<?> parent = windowContainer.getParent();
        while (parent != null && !parent.canCreateRemoteAnimationTarget() && !parent.isOrganized()) {
            parent = parent.getParent();
        }
        return parent;
    }

    private static boolean reportIfNotTop(WindowContainer windowContainer) {
        return windowContainer.isOrganized();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static boolean isWallpaper(WindowContainer windowContainer) {
        return windowContainer.asWallpaperToken() != null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static boolean isInputMethod(WindowContainer windowContainer) {
        return windowContainer.getWindowType() == 2011;
    }

    private static boolean occludesKeyguard(WindowContainer windowContainer) {
        ActivityRecord activity;
        ActivityRecord asActivityRecord = windowContainer.asActivityRecord();
        if (asActivityRecord != null) {
            return asActivityRecord.canShowWhenLocked();
        }
        Task asTask = windowContainer.asTask();
        return (asTask == null || (activity = asTask.getActivity(new Predicate() { // from class: com.android.server.wm.Transition$$ExternalSyntheticLambda4
            @Override // java.util.function.Predicate
            public final boolean test(Object obj) {
                return ((ActivityRecord) obj).isClientVisible();
            }
        })) == null || !activity.canShowWhenLocked()) ? false : true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static boolean isTranslucent(WindowContainer windowContainer) {
        TaskFragment asTaskFragment = windowContainer.asTaskFragment();
        if (asTaskFragment == null) {
            return !windowContainer.fillsParent();
        }
        if (asTaskFragment.isTranslucentForTransition()) {
            return true;
        }
        TaskFragment adjacentTaskFragment = asTaskFragment.getAdjacentTaskFragment();
        if (adjacentTaskFragment != null) {
            return adjacentTaskFragment.isTranslucentForTransition();
        }
        return !windowContainer.fillsParent();
    }

    private void updatePriorVisibility() {
        for (int i = 0; i < this.mChanges.size(); i++) {
            ChangeInfo valueAt = this.mChanges.valueAt(i);
            if ((valueAt.mContainer.asActivityRecord() != null || valueAt.mContainer.asTask() != null) && valueAt.mVisible) {
                valueAt.mVisible = valueAt.mContainer.isVisible();
            }
        }
    }

    private static boolean canPromote(ChangeInfo changeInfo, Targets targets, ArrayMap<WindowContainer, ChangeInfo> arrayMap) {
        WindowContainer windowContainer = changeInfo.mContainer;
        WindowContainer parent = windowContainer.getParent();
        ChangeInfo changeInfo2 = arrayMap.get(parent);
        if (!parent.canCreateRemoteAnimationTarget() || changeInfo2 == null || !changeInfo2.hasChanged()) {
            if (ProtoLogCache.WM_DEBUG_WINDOW_TRANSITIONS_enabled) {
                ProtoLogImpl.v(ProtoLogGroup.WM_DEBUG_WINDOW_TRANSITIONS, 744171317, 0, (String) null, new Object[]{String.valueOf("parent can't be target " + parent)});
            }
            return false;
        }
        if (isWallpaper(windowContainer)) {
            if (ProtoLogCache.WM_DEBUG_WINDOW_TRANSITIONS_enabled) {
                ProtoLogImpl.v(ProtoLogGroup.WM_DEBUG_WINDOW_TRANSITIONS, -2036671725, 0, (String) null, (Object[]) null);
            }
            return false;
        }
        if (changeInfo.mStartParent != null && windowContainer.getParent() != changeInfo.mStartParent && mTransitionStaticExt.dontPromoteWhenReparent(changeInfo, arrayMap)) {
            return false;
        }
        int transitMode = changeInfo.getTransitMode(windowContainer);
        for (int childCount = parent.getChildCount() - 1; childCount >= 0; childCount--) {
            WindowContainer childAt = parent.getChildAt(childCount);
            if (windowContainer != childAt) {
                if (ProtoLogCache.WM_DEBUG_WINDOW_TRANSITIONS_enabled) {
                    ProtoLogImpl.v(ProtoLogGroup.WM_DEBUG_WINDOW_TRANSITIONS, -703543418, 0, (String) null, new Object[]{String.valueOf(childAt)});
                }
                ChangeInfo changeInfo3 = arrayMap.get(childAt);
                if (changeInfo3 == null || !targets.wasParticipated(changeInfo3)) {
                    if (childAt.isVisibleRequested()) {
                        if (ProtoLogCache.WM_DEBUG_WINDOW_TRANSITIONS_enabled) {
                            ProtoLogImpl.v(ProtoLogGroup.WM_DEBUG_WINDOW_TRANSITIONS, 793568608, 0, (String) null, (Object[]) null);
                        }
                        return false;
                    }
                    if (ProtoLogCache.WM_DEBUG_WINDOW_TRANSITIONS_enabled) {
                        ProtoLogImpl.v(ProtoLogGroup.WM_DEBUG_WINDOW_TRANSITIONS, -1728919185, 0, (String) null, new Object[]{String.valueOf(childAt)});
                    }
                } else {
                    int transitMode2 = changeInfo3.getTransitMode(childAt);
                    if (ProtoLogCache.WM_DEBUG_WINDOW_TRANSITIONS_enabled) {
                        ProtoLogImpl.v(ProtoLogGroup.WM_DEBUG_WINDOW_TRANSITIONS, -779095785, 0, (String) null, new Object[]{String.valueOf(TransitionInfo.modeToString(transitMode2))});
                    }
                    if (reduceMode(transitMode) != reduceMode(transitMode2)) {
                        if (ProtoLogCache.WM_DEBUG_WINDOW_TRANSITIONS_enabled) {
                            ProtoLogImpl.v(ProtoLogGroup.WM_DEBUG_WINDOW_TRANSITIONS, 1469310004, 0, (String) null, new Object[]{String.valueOf(TransitionInfo.modeToString(transitMode))});
                        }
                        return false;
                    }
                }
            }
        }
        return true;
    }

    private static void tryPromote(Targets targets, ArrayMap<WindowContainer, ChangeInfo> arrayMap) {
        int size = targets.mArray.size() - 1;
        WindowContainer windowContainer = null;
        while (size >= 0) {
            ChangeInfo valueAt = targets.mArray.valueAt(size);
            WindowContainer windowContainer2 = valueAt.mContainer;
            if (ProtoLogCache.WM_DEBUG_WINDOW_TRANSITIONS_enabled) {
                ProtoLogImpl.v(ProtoLogGroup.WM_DEBUG_WINDOW_TRANSITIONS, -509601642, 0, (String) null, new Object[]{String.valueOf(windowContainer2)});
            }
            WindowContainer parent = windowContainer2.getParent();
            if (parent == windowContainer) {
                if (ProtoLogCache.WM_DEBUG_WINDOW_TRANSITIONS_enabled) {
                    ProtoLogImpl.v(ProtoLogGroup.WM_DEBUG_WINDOW_TRANSITIONS, 112145970, 0, (String) null, (Object[]) null);
                }
            } else if (canPromote(valueAt, targets, arrayMap) && mTransitionStaticExt.canPromote(valueAt, arrayMap)) {
                if (reportIfNotTop(windowContainer2)) {
                    if (ProtoLogCache.WM_DEBUG_WINDOW_TRANSITIONS_enabled) {
                        ProtoLogImpl.v(ProtoLogGroup.WM_DEBUG_WINDOW_TRANSITIONS, 528150092, 0, (String) null, new Object[]{String.valueOf(windowContainer2)});
                    }
                } else {
                    if (ProtoLogCache.WM_DEBUG_WINDOW_TRANSITIONS_enabled) {
                        ProtoLogImpl.v(ProtoLogGroup.WM_DEBUG_WINDOW_TRANSITIONS, 182319432, 0, (String) null, new Object[]{String.valueOf(windowContainer2)});
                    }
                    targets.remove(size);
                }
                ChangeInfo changeInfo = arrayMap.get(parent);
                if (targets.mArray.indexOfValue(changeInfo) < 0) {
                    if (ProtoLogCache.WM_DEBUG_WINDOW_TRANSITIONS_enabled) {
                        ProtoLogImpl.v(ProtoLogGroup.WM_DEBUG_WINDOW_TRANSITIONS, -1452274694, 0, (String) null, new Object[]{String.valueOf(parent)});
                    }
                    size++;
                    targets.add(changeInfo);
                }
                if ((valueAt.mFlags & 8) != 0) {
                    changeInfo.mFlags |= 8;
                } else {
                    changeInfo.mFlags |= 16;
                }
            } else {
                windowContainer = parent;
            }
            size--;
        }
    }

    @VisibleForTesting
    static ArrayList<ChangeInfo> calculateTargets(ArraySet<WindowContainer> arraySet, ArrayMap<WindowContainer, ChangeInfo> arrayMap) {
        if (ProtoLogCache.WM_DEBUG_WINDOW_TRANSITIONS_enabled) {
            ProtoLogImpl.v(ProtoLogGroup.WM_DEBUG_WINDOW_TRANSITIONS, 184610856, 0, (String) null, new Object[]{String.valueOf(arraySet)});
        }
        Targets targets = new Targets();
        for (int size = arraySet.size() - 1; size >= 0; size--) {
            WindowContainer valueAt = arraySet.valueAt(size);
            if (!valueAt.isAttached()) {
                if (ProtoLogCache.WM_DEBUG_WINDOW_TRANSITIONS_enabled) {
                    ProtoLogImpl.v(ProtoLogGroup.WM_DEBUG_WINDOW_TRANSITIONS, 1494644409, 0, (String) null, new Object[]{String.valueOf(valueAt)});
                }
            } else if (valueAt.asWindowState() == null) {
                ChangeInfo changeInfo = arrayMap.get(valueAt);
                if (!changeInfo.hasChanged()) {
                    if (ProtoLogCache.WM_DEBUG_WINDOW_TRANSITIONS_enabled) {
                        ProtoLogImpl.v(ProtoLogGroup.WM_DEBUG_WINDOW_TRANSITIONS, -672355406, 0, (String) null, new Object[]{String.valueOf(valueAt)});
                    }
                } else {
                    targets.add(changeInfo);
                }
            }
        }
        if (ProtoLogCache.WM_DEBUG_WINDOW_TRANSITIONS_enabled) {
            ProtoLogImpl.v(ProtoLogGroup.WM_DEBUG_WINDOW_TRANSITIONS, -1844540996, 0, (String) null, new Object[]{String.valueOf(targets.mArray)});
        }
        tryPromote(targets, arrayMap);
        populateParentChanges(targets, arrayMap);
        ArrayList<ChangeInfo> listSortedByZ = targets.getListSortedByZ();
        mTransitionStaticExt.updateAnimTargetIfNeed(arraySet, listSortedByZ, arrayMap);
        mTransitionStaticExt.filterAnimTargetIfNeed(listSortedByZ, arrayMap);
        if (ProtoLogCache.WM_DEBUG_WINDOW_TRANSITIONS_enabled) {
            ProtoLogImpl.v(ProtoLogGroup.WM_DEBUG_WINDOW_TRANSITIONS, 405146734, 0, (String) null, new Object[]{String.valueOf(listSortedByZ)});
        }
        return listSortedByZ;
    }

    private static void populateParentChanges(Targets targets, ArrayMap<WindowContainer, ChangeInfo> arrayMap) {
        int i;
        boolean z;
        ChangeInfo changeInfo;
        ArrayList arrayList = new ArrayList();
        ArrayList arrayList2 = new ArrayList(targets.mArray.size());
        for (int size = targets.mArray.size() - 1; size >= 0; size--) {
            arrayList2.add(targets.mArray.valueAt(size));
        }
        for (int size2 = arrayList2.size() - 1; size2 >= 0; size2--) {
            ChangeInfo changeInfo2 = (ChangeInfo) arrayList2.get(size2);
            WindowContainer windowContainer = changeInfo2.mContainer;
            boolean isWallpaper = isWallpaper(windowContainer);
            arrayList.clear();
            WindowContainer<?> animatableParent = getAnimatableParent(windowContainer);
            while (true) {
                i = 0;
                if (animatableParent == null || (changeInfo = arrayMap.get(animatableParent)) == null || !changeInfo.hasChanged()) {
                    break;
                }
                if (animatableParent.mRemoteToken != null) {
                    if (changeInfo.mEndParent != null && !isWallpaper) {
                        changeInfo2.mEndParent = animatableParent;
                        break;
                    }
                    if (arrayList2.contains(changeInfo)) {
                        if (isWallpaper) {
                            changeInfo2.mEndParent = animatableParent;
                        } else {
                            arrayList.add(changeInfo);
                        }
                        z = true;
                    } else if (reportIfNotTop(animatableParent) && !isWallpaper) {
                        arrayList.add(changeInfo);
                    }
                }
                animatableParent = getAnimatableParent(animatableParent);
            }
            z = false;
            if (z && !arrayList.isEmpty()) {
                changeInfo2.mEndParent = ((ChangeInfo) arrayList.get(0)).mContainer;
                while (i < arrayList.size() - 1) {
                    ChangeInfo changeInfo3 = (ChangeInfo) arrayList.get(i);
                    i++;
                    changeInfo3.mEndParent = ((ChangeInfo) arrayList.get(i)).mContainer;
                    targets.add(changeInfo3);
                }
            }
        }
    }

    private static SurfaceControl getLeashSurface(WindowContainer windowContainer, SurfaceControl.Transaction transaction) {
        WindowToken asWindowToken;
        SurfaceControl fixedRotationLeash;
        DisplayContent asDisplayContent = windowContainer.asDisplayContent();
        if (asDisplayContent != null) {
            return asDisplayContent.getWindowingLayer();
        }
        if (!windowContainer.mTransitionController.useShellTransitionsRotation() && (asWindowToken = windowContainer.asWindowToken()) != null) {
            if (transaction != null) {
                fixedRotationLeash = asWindowToken.getOrCreateFixedRotationLeash(transaction);
            } else {
                fixedRotationLeash = asWindowToken.getFixedRotationLeash();
            }
            if (fixedRotationLeash != null) {
                return fixedRotationLeash;
            }
        }
        return windowContainer.getSurfaceControl();
    }

    private static SurfaceControl getOrigParentSurface(WindowContainer windowContainer) {
        if (windowContainer.asDisplayContent() != null) {
            return windowContainer.getSurfaceControl();
        }
        if (windowContainer.getParent().asDisplayContent() != null) {
            return windowContainer.getParent().asDisplayContent().getWindowingLayer();
        }
        return windowContainer.getParent().getSurfaceControl();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static boolean isReadyGroup(WindowContainer windowContainer) {
        return windowContainer instanceof DisplayContent;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static int getDisplayId(WindowContainer windowContainer) {
        if (windowContainer.getDisplayContent() != null) {
            return windowContainer.getDisplayContent().getDisplayId();
        }
        return -1;
    }

    @VisibleForTesting
    static void calculateTransitionRoots(TransitionInfo transitionInfo, ArrayList<ChangeInfo> arrayList, SurfaceControl.Transaction transaction) {
        DisplayContent displayContent;
        for (int i = 0; i < arrayList.size(); i++) {
            WindowContainer windowContainer = arrayList.get(i).mContainer;
            if (!isWallpaper(windowContainer) && (displayContent = windowContainer.getDisplayContent()) != null) {
                int displayId = displayContent.getDisplayId();
                if (transitionInfo.findRootIndex(displayId) < 0) {
                    WindowContainer<?> findCommonAncestor = findCommonAncestor(arrayList, windowContainer);
                    if (!windowContainer.isDescendantOf(findCommonAncestor)) {
                        Slog.e(TAG, "Did not find common ancestor! Ancestor= " + findCommonAncestor + " target= " + windowContainer);
                    } else {
                        while (windowContainer.getParent() != findCommonAncestor) {
                            windowContainer = windowContainer.getParent();
                        }
                    }
                    SurfaceControl build = windowContainer.makeAnimationLeash().setName("Transition Root: " + windowContainer.getName()).build();
                    build.setUnreleasedWarningCallSite("Transition.calculateTransitionRoots");
                    updateDisplayLayers(displayContent, transaction);
                    transaction.setLayer(build, windowContainer.getLastLayer());
                    mTransitionStaticExt.setWindowCropForTransitionIfNeed(transaction, build, windowContainer);
                    transitionInfo.addRootLeash(displayId, build, findCommonAncestor.getBounds().left, findCommonAncestor.getBounds().top);
                }
            }
        }
    }

    @VisibleForTesting
    static TransitionInfo calculateTransitionInfo(int i, int i2, ArrayList<ChangeInfo> arrayList, SurfaceControl.Transaction transaction) {
        WindowContainerToken windowContainerToken;
        WindowContainer windowContainer;
        TaskFragment organizedTaskFragment;
        Task task;
        int backgroundColor;
        SurfaceControl.Transaction transaction2 = transaction;
        TransitionInfo transitionInfo = new TransitionInfo(i, i2);
        calculateTransitionRoots(transitionInfo, arrayList, transaction2);
        if (transitionInfo.getRootCount() == 0) {
            return transitionInfo;
        }
        int size = arrayList.size();
        int i3 = 0;
        while (true) {
            if (i3 >= size) {
                break;
            }
            ChangeInfo changeInfo = arrayList.get(i3);
            WindowContainer windowContainer2 = changeInfo.mContainer;
            WindowContainer.RemoteToken remoteToken = windowContainer2.mRemoteToken;
            TransitionInfo.Change change = new TransitionInfo.Change(remoteToken != null ? remoteToken.toWindowContainerToken() : null, getLeashSurface(windowContainer2, transaction2));
            WindowContainer windowContainer3 = changeInfo.mEndParent;
            if (windowContainer3 != null) {
                change.setParent(windowContainer3.mRemoteToken.toWindowContainerToken());
            }
            WindowContainer windowContainer4 = changeInfo.mStartParent;
            if (windowContainer4 != null && windowContainer4.mRemoteToken != null) {
                WindowContainer parent = windowContainer2.getParent();
                WindowContainer windowContainer5 = changeInfo.mStartParent;
                if (parent != windowContainer5) {
                    change.setLastParent(windowContainer5.mRemoteToken.toWindowContainerToken());
                }
            }
            change.setMode(changeInfo.getTransitMode(windowContainer2));
            changeInfo.mReadyMode = change.getMode();
            change.setStartAbsBounds(changeInfo.mAbsoluteBounds);
            change.setFlags(changeInfo.getChangeFlags(windowContainer2));
            changeInfo.mReadyFlags = change.getFlags();
            change.setDisplayId(changeInfo.mDisplayId, getDisplayId(windowContainer2));
            Task asTask = windowContainer2.asTask();
            TaskFragment asTaskFragment = windowContainer2.asTaskFragment();
            ActivityRecord asActivityRecord = windowContainer2.asActivityRecord();
            if (asTask != null) {
                ActivityManager.RunningTaskInfo runningTaskInfo = new ActivityManager.RunningTaskInfo();
                asTask.fillTaskInfo(runningTaskInfo);
                change.setTaskInfo(runningTaskInfo);
                change.setRotationAnimation(getTaskRotationAnimation(asTask));
                ActivityRecord activityRecord = asTask.topRunningActivity();
                if (activityRecord != null) {
                    if (activityRecord.info.supportsPictureInPicture()) {
                        change.setAllowEnterPip(activityRecord.checkEnterPictureInPictureAppOpsState());
                    }
                    setEndFixedRotationIfNeeded(change, asTask, activityRecord);
                }
            } else if ((1 & changeInfo.mFlags) != 0) {
                change.setRotationAnimation(3);
            }
            WindowContainer parent2 = windowContainer2.getParent();
            Rect bounds = windowContainer2.getBounds();
            Rect bounds2 = parent2.getBounds();
            change.setEndRelOffset(bounds.left - bounds2.left, bounds.top - bounds2.top);
            int rotation = windowContainer2.getWindowConfiguration().getRotation();
            if (asActivityRecord != null) {
                change.setEndAbsBounds(bounds2);
                if (asActivityRecord.getRelativeDisplayRotation() != 0 && !asActivityRecord.mTransitionController.useShellTransitionsRotation()) {
                    rotation = parent2.getWindowConfiguration().getRotation();
                }
            } else {
                change.setEndAbsBounds(bounds);
            }
            if (asActivityRecord != null || (asTaskFragment != null && asTaskFragment.isEmbedded())) {
                if (asActivityRecord != null) {
                    organizedTaskFragment = asActivityRecord.getOrganizedTaskFragment();
                } else {
                    organizedTaskFragment = asTaskFragment.getOrganizedTaskFragment();
                }
                if (organizedTaskFragment != null && organizedTaskFragment.getAnimationParams().getAnimationBackgroundColor() != 0) {
                    backgroundColor = organizedTaskFragment.getAnimationParams().getAnimationBackgroundColor();
                } else {
                    if (asActivityRecord != null) {
                        task = asActivityRecord.getTask();
                    } else {
                        task = asTaskFragment.getTask();
                    }
                    backgroundColor = task.getTaskDescription().getBackgroundColor();
                }
                change.setBackgroundColor(ColorUtils.setAlphaComponent(backgroundColor, 255));
            }
            change.setRotation(changeInfo.mRotation, rotation);
            SurfaceControl surfaceControl = changeInfo.mSnapshot;
            if (surfaceControl != null) {
                change.setSnapshot(surfaceControl, changeInfo.mSnapshotLuma);
            }
            if (!mTransitionStaticExt.skipCurrentOrAdjustChange(i, i2, change, changeInfo, arrayList, windowContainer2)) {
                transitionInfo.addChange(change);
            }
            i3++;
            transaction2 = transaction;
        }
        int i4 = 0;
        while (true) {
            if (i4 >= arrayList.size()) {
                windowContainer = null;
                break;
            }
            if (!isWallpaper(arrayList.get(i4).mContainer)) {
                windowContainer = arrayList.get(i4).mContainer;
                break;
            }
            i4++;
        }
        if (windowContainer.asActivityRecord() != null) {
            ActivityRecord asActivityRecord2 = windowContainer.asActivityRecord();
            windowContainerToken = addCustomActivityTransition(asActivityRecord2, false, addCustomActivityTransition(asActivityRecord2, true, null));
        }
        WindowManager.LayoutParams layoutParamsForAnimationsStyle = getLayoutParamsForAnimationsStyle(i, arrayList);
        if (layoutParamsForAnimationsStyle != null && layoutParamsForAnimationsStyle.type != 3 && layoutParamsForAnimationsStyle.windowAnimations != 0) {
            if (windowContainerToken != null) {
                windowContainerToken.addOptionsFromLayoutParameters(layoutParamsForAnimationsStyle);
            } else {
                windowContainerToken = TransitionInfo.AnimationOptions.makeAnimOptionsFromLayoutParameters(layoutParamsForAnimationsStyle);
            }
        }
        if (windowContainerToken != null) {
            transitionInfo.setAnimationOptions(windowContainerToken);
        }
        return transitionInfo;
    }

    static TransitionInfo.AnimationOptions addCustomActivityTransition(ActivityRecord activityRecord, boolean z, TransitionInfo.AnimationOptions animationOptions) {
        ActivityRecord.CustomAppTransition customAnimation = activityRecord.getCustomAnimation(z);
        if (customAnimation != null) {
            if (animationOptions == null) {
                animationOptions = TransitionInfo.AnimationOptions.makeCommonAnimOptions(activityRecord.packageName);
            }
            animationOptions.addCustomActivityTransition(z, customAnimation.mEnterAnim, customAnimation.mExitAnim, customAnimation.mBackgroundColor);
        }
        return animationOptions;
    }

    private static void setEndFixedRotationIfNeeded(TransitionInfo.Change change, Task task, ActivityRecord activityRecord) {
        WindowContainer lastOrientationSource;
        int displayRotation;
        if (activityRecord.isVisibleRequested()) {
            if (task.inMultiWindowMode() && activityRecord.inMultiWindowMode()) {
                return;
            }
            int displayRotation2 = task.getWindowConfiguration().getDisplayRotation();
            int displayRotation3 = activityRecord.getWindowConfiguration().getDisplayRotation();
            if (displayRotation2 != displayRotation3) {
                change.setEndFixedRotation(displayRotation3);
            } else {
                if (!task.inPinnedWindowingMode() || activityRecord.mDisplayContent.inTransition() || (lastOrientationSource = activityRecord.mDisplayContent.getLastOrientationSource()) == null || displayRotation2 == (displayRotation = lastOrientationSource.getWindowConfiguration().getDisplayRotation())) {
                    return;
                }
                change.setEndFixedRotation(displayRotation);
            }
        }
    }

    private static WindowContainer<?> findCommonAncestor(ArrayList<ChangeInfo> arrayList, WindowContainer<?> windowContainer) {
        int transitMode;
        int displayId = getDisplayId(windowContainer);
        WindowContainer<?> parent = windowContainer.getParent();
        for (int size = arrayList.size() - 1; size >= 0; size--) {
            ChangeInfo changeInfo = arrayList.get(size);
            WindowContainer windowContainer2 = changeInfo.mContainer;
            if (!isWallpaper(windowContainer2) && getDisplayId(windowContainer2) == displayId) {
                if (changeInfo.mStartParent != null && windowContainer2.getParent() != null && changeInfo.mStartParent.isAttached() && windowContainer2.getParent() != changeInfo.mStartParent && size == arrayList.size() - 1 && ((transitMode = changeInfo.getTransitMode(windowContainer2)) == 2 || transitMode == 4)) {
                    parent = changeInfo.mStartParent;
                } else {
                    while (!windowContainer2.isDescendantOf(parent)) {
                        parent = parent.getParent();
                    }
                    WindowContainer<?> windowContainer3 = changeInfo.mCommonAncestor;
                    if (windowContainer3 != null && windowContainer3.isAttached()) {
                        while (windowContainer3 != parent && !windowContainer3.isDescendantOf(parent)) {
                            parent = parent.getParent();
                        }
                    }
                }
            }
        }
        return parent;
    }

    private static WindowManager.LayoutParams getLayoutParamsForAnimationsStyle(int i, ArrayList<ChangeInfo> arrayList) {
        ArraySet arraySet = new ArraySet();
        int size = arrayList.size();
        for (int i2 = 0; i2 < size; i2++) {
            WindowContainer windowContainer = arrayList.get(i2).mContainer;
            if (windowContainer.asActivityRecord() != null) {
                arraySet.add(Integer.valueOf(windowContainer.getActivityType()));
            } else if (windowContainer.asWindowToken() == null && windowContainer.asWindowState() == null) {
                return null;
            }
        }
        if (arraySet.isEmpty()) {
            return null;
        }
        ActivityRecord findAnimLayoutParamsActivityRecord = findAnimLayoutParamsActivityRecord(arrayList, i, arraySet);
        WindowState findMainWindow = findAnimLayoutParamsActivityRecord != null ? findAnimLayoutParamsActivityRecord.findMainWindow() : null;
        if (findMainWindow != null) {
            return findMainWindow.mAttrs;
        }
        return null;
    }

    private static ActivityRecord findAnimLayoutParamsActivityRecord(List<ChangeInfo> list, final int i, final ArraySet<Integer> arraySet) {
        ActivityRecord lookForTopWindowWithFilter = lookForTopWindowWithFilter(list, new Predicate() { // from class: com.android.server.wm.Transition$$ExternalSyntheticLambda11
            @Override // java.util.function.Predicate
            public final boolean test(Object obj) {
                boolean lambda$findAnimLayoutParamsActivityRecord$7;
                lambda$findAnimLayoutParamsActivityRecord$7 = Transition.lambda$findAnimLayoutParamsActivityRecord$7(i, arraySet, (ActivityRecord) obj);
                return lambda$findAnimLayoutParamsActivityRecord$7;
            }
        });
        if (lookForTopWindowWithFilter != null) {
            return lookForTopWindowWithFilter;
        }
        ActivityRecord lookForTopWindowWithFilter2 = lookForTopWindowWithFilter(list, new Predicate() { // from class: com.android.server.wm.Transition$$ExternalSyntheticLambda12
            @Override // java.util.function.Predicate
            public final boolean test(Object obj) {
                boolean lambda$findAnimLayoutParamsActivityRecord$8;
                lambda$findAnimLayoutParamsActivityRecord$8 = Transition.lambda$findAnimLayoutParamsActivityRecord$8((ActivityRecord) obj);
                return lambda$findAnimLayoutParamsActivityRecord$8;
            }
        });
        return lookForTopWindowWithFilter2 != null ? lookForTopWindowWithFilter2 : lookForTopWindowWithFilter(list, new Predicate() { // from class: com.android.server.wm.Transition$$ExternalSyntheticLambda13
            @Override // java.util.function.Predicate
            public final boolean test(Object obj) {
                boolean lambda$findAnimLayoutParamsActivityRecord$9;
                lambda$findAnimLayoutParamsActivityRecord$9 = Transition.lambda$findAnimLayoutParamsActivityRecord$9((ActivityRecord) obj);
                return lambda$findAnimLayoutParamsActivityRecord$9;
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ boolean lambda$findAnimLayoutParamsActivityRecord$7(int i, ArraySet arraySet, ActivityRecord activityRecord) {
        return activityRecord.getRemoteAnimationDefinition() != null && activityRecord.getRemoteAnimationDefinition().hasTransition(i, arraySet);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ boolean lambda$findAnimLayoutParamsActivityRecord$8(ActivityRecord activityRecord) {
        return activityRecord.fillsParent() && activityRecord.findMainWindow() != null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ boolean lambda$findAnimLayoutParamsActivityRecord$9(ActivityRecord activityRecord) {
        return activityRecord.findMainWindow() != null;
    }

    private static ActivityRecord lookForTopWindowWithFilter(List<ChangeInfo> list, Predicate<ActivityRecord> predicate) {
        ActivityRecord asActivityRecord;
        int size = list.size();
        for (int i = 0; i < size; i++) {
            WindowContainer windowContainer = list.get(i).mContainer;
            if (windowContainer.asTaskFragment() != null) {
                asActivityRecord = windowContainer.asTaskFragment().getTopNonFinishingActivity();
            } else {
                asActivityRecord = windowContainer.asActivityRecord();
            }
            if (asActivityRecord != null && predicate.test(asActivityRecord)) {
                return asActivityRecord;
            }
        }
        return null;
    }

    private static int getTaskRotationAnimation(Task task) {
        WindowState findMainWindow;
        ActivityRecord topVisibleActivity = task.getTopVisibleActivity();
        if (topVisibleActivity == null || (findMainWindow = topVisibleActivity.findMainWindow(false)) == null) {
            return -1;
        }
        int rotationAnimationHint = findMainWindow.getRotationAnimationHint();
        if (rotationAnimationHint >= 0) {
            return rotationAnimationHint;
        }
        int i = findMainWindow.getAttrs().rotationAnimation;
        if (i != 3) {
            return i;
        }
        if (findMainWindow == task.mDisplayContent.getDisplayPolicy().getTopFullscreenOpaqueWindow() && topVisibleActivity.matchParentBounds()) {
            return findMainWindow.getAttrs().rotationAnimation;
        }
        return -1;
    }

    private void validateKeyguardOcclusion() {
        if ((this.mFlags & 14592) != 0) {
            TransitionController transitionController = this.mController;
            ArrayList<Runnable> arrayList = transitionController.mStateValidators;
            WindowManagerPolicy windowManagerPolicy = transitionController.mAtm.mWindowManager.mPolicy;
            Objects.requireNonNull(windowManagerPolicy);
            arrayList.add(new KeyguardController$$ExternalSyntheticLambda0(windowManagerPolicy));
        }
    }

    private void validateVisibility() {
        for (int size = this.mTargets.size() - 1; size >= 0; size--) {
            if (reduceMode(this.mTargets.get(size).mReadyMode) != 2) {
                return;
            }
        }
        this.mController.mStateValidators.add(new Runnable() { // from class: com.android.server.wm.Transition$$ExternalSyntheticLambda3
            @Override // java.lang.Runnable
            public final void run() {
                Transition.this.lambda$validateVisibility$10();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$validateVisibility$10() {
        for (int size = this.mTargets.size() - 1; size >= 0; size--) {
            ChangeInfo changeInfo = this.mTargets.get(size);
            if (changeInfo.mContainer.isVisibleRequested() && changeInfo.mContainer.mSurfaceControl != null) {
                Slog.e(TAG, "Force show for visible " + changeInfo.mContainer + " which may be hidden by transition unexpectedly");
                changeInfo.mContainer.getSyncTransaction().show(changeInfo.mContainer.mSurfaceControl);
                changeInfo.mContainer.scheduleAnimation();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void applyDisplayChangeIfNeeded() {
        for (int size = this.mParticipants.size() - 1; size >= 0; size--) {
            final DisplayContent asDisplayContent = this.mParticipants.valueAt(size).asDisplayContent();
            if (asDisplayContent != null && this.mChanges.get(asDisplayContent).hasChanged()) {
                asDisplayContent.mWmService.mH.post(new Runnable() { // from class: com.android.server.wm.Transition.1
                    @Override // java.lang.Runnable
                    public void run() {
                        WindowManagerGlobalLock windowManagerGlobalLock = asDisplayContent.mWmService.mGlobalLock;
                        WindowManagerService.boostPriorityForLockedSection();
                        synchronized (windowManagerGlobalLock) {
                            try {
                                if (Transition.PANIC_DEBUG) {
                                    Slog.d(Transition.TAG, "sendNewConfiguration run in display thread");
                                }
                                asDisplayContent.sendNewConfiguration();
                            } catch (Throwable th) {
                                WindowManagerService.resetPriorityAfterLockedSection();
                                throw th;
                            }
                        }
                        WindowManagerService.resetPriorityAfterLockedSection();
                    }
                });
                if (!this.mReadyTracker.mUsed) {
                    setReady(asDisplayContent, true);
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean getLegacyIsReady() {
        return isCollecting() && this.mSyncId >= 0;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void asyncTraceBegin(String str, int i) {
        Trace.asyncTraceForTrackBegin(32L, TAG, str, i);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void asyncTraceEnd(int i) {
        Trace.asyncTraceForTrackEnd(32L, TAG, i);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @VisibleForTesting
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
    public static class ChangeInfo {
        private static final int FLAG_ABOVE_TRANSIENT_LAUNCH = 4;
        private static final int FLAG_CHANGE_MOVED_TO_TOP = 32;
        private static final int FLAG_CHANGE_NO_ANIMATION = 8;
        private static final int FLAG_CHANGE_YES_ANIMATION = 16;
        private static final int FLAG_NONE = 0;
        private static final int FLAG_SEAMLESS_ROTATION = 1;
        private static final int FLAG_TRANSIENT_LAUNCH = 2;
        final Rect mAbsoluteBounds;
        WindowContainer mCommonAncestor;
        final WindowContainer mContainer;
        int mDisplayId;
        WindowContainer mEndParent;
        boolean mExistenceChanged;
        int mFlags;
        int mKnownConfigChanges;

        @TransitionInfo.ChangeFlags
        int mReadyFlags;

        @TransitionInfo.TransitionMode
        int mReadyMode;
        int mRotation;
        boolean mShowWallpaper;
        SurfaceControl mSnapshot;
        float mSnapshotLuma;
        WindowContainer mStartParent;
        boolean mVisible;
        int mWindowingMode;

        @Retention(RetentionPolicy.SOURCE)
        /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
        @interface Flag {
        }

        ChangeInfo(WindowContainer windowContainer) {
            this.mExistenceChanged = false;
            Rect rect = new Rect();
            this.mAbsoluteBounds = rect;
            this.mRotation = -1;
            this.mDisplayId = -1;
            this.mFlags = 0;
            this.mContainer = windowContainer;
            this.mVisible = windowContainer.isVisibleRequested();
            this.mWindowingMode = windowContainer.getWindowingMode();
            rect.set(windowContainer.getBounds());
            this.mShowWallpaper = windowContainer.showWallpaper();
            this.mRotation = windowContainer.getWindowConfiguration().getRotation();
            this.mStartParent = windowContainer.getParent();
            this.mDisplayId = Transition.getDisplayId(windowContainer);
        }

        @VisibleForTesting
        ChangeInfo(WindowContainer windowContainer, boolean z, boolean z2) {
            this(windowContainer);
            this.mVisible = z;
            this.mExistenceChanged = z2;
            this.mShowWallpaper = false;
        }

        public String toString() {
            return this.mContainer.toString();
        }

        boolean hasChanged() {
            int i = this.mFlags;
            if ((i & 2) != 0 || (i & 4) != 0) {
                return true;
            }
            boolean isVisibleRequested = this.mContainer.isVisibleRequested();
            boolean z = this.mVisible;
            if (isVisibleRequested == z && !z) {
                return false;
            }
            if (isVisibleRequested == z && this.mKnownConfigChanges == 0) {
                return ((this.mWindowingMode == 0 || this.mContainer.getWindowingMode() == this.mWindowingMode) && this.mContainer.getBounds().equals(this.mAbsoluteBounds) && this.mRotation == this.mContainer.getWindowConfiguration().getRotation() && this.mDisplayId == Transition.getDisplayId(this.mContainer) && (this.mFlags & 32) == 0) ? false : true;
            }
            return true;
        }

        @TransitionInfo.TransitionMode
        int getTransitMode(WindowContainer windowContainer) {
            if ((this.mFlags & 4) != 0) {
                return this.mExistenceChanged ? 2 : 4;
            }
            boolean isVisibleRequested = windowContainer.isVisibleRequested();
            if (isVisibleRequested == this.mVisible) {
                return 6;
            }
            return this.mExistenceChanged ? isVisibleRequested ? 1 : 2 : isVisibleRequested ? 3 : 4;
        }

        @TransitionInfo.ChangeFlags
        int getChangeFlags(WindowContainer windowContainer) {
            Task task;
            int i = (this.mShowWallpaper || windowContainer.showWallpaper()) ? 1 : 0;
            if (Transition.isTranslucent(windowContainer)) {
                i |= 4;
            }
            if (windowContainer.mWmService.mAtmService.mBackNavigationController.isMonitorTransitionTarget(windowContainer)) {
                i |= 131072;
            }
            Task asTask = windowContainer.asTask();
            if (asTask != null) {
                ActivityRecord topNonFinishingActivity = asTask.getTopNonFinishingActivity();
                if (topNonFinishingActivity != null) {
                    StartingData startingData = topNonFinishingActivity.mStartingData;
                    if (startingData != null && startingData.hasImeSurface()) {
                        i |= 2048;
                    }
                    if (topNonFinishingActivity.mLaunchTaskBehind) {
                        Slog.e(Transition.TAG, "Unexpected launch-task-behind operation in shell transition");
                        i |= 524288;
                    }
                }
                if (asTask.voiceSession != null) {
                    i |= 16;
                }
            }
            ActivityRecord asActivityRecord = windowContainer.asActivityRecord();
            if (asActivityRecord != null) {
                task = asActivityRecord.getTask();
                if (asActivityRecord.mVoiceInteraction) {
                    i |= 16;
                }
                i |= asActivityRecord.mTransitionChangeFlags;
            } else {
                task = null;
            }
            TaskFragment asTaskFragment = windowContainer.asTaskFragment();
            if (asTaskFragment != null && asTask == null) {
                task = asTaskFragment.getTask();
            }
            if (task != null) {
                if (task.forAllLeafTaskFragments(new ActivityRecord$$ExternalSyntheticLambda34())) {
                    i |= 512;
                }
                if (task.forAllActivities(new Transition$ChangeInfo$$ExternalSyntheticLambda0())) {
                    i |= 16384;
                }
                if (isWindowFillingTask(windowContainer, task)) {
                    i |= 1024;
                }
            } else {
                DisplayContent asDisplayContent = windowContainer.asDisplayContent();
                if (asDisplayContent != null) {
                    i |= 32;
                    if (asDisplayContent.hasAlertWindowSurfaces() && !Transition.mTransitionStaticExt.forceSeamlesslyRotated(asDisplayContent, "System Alert windows")) {
                        i |= 128;
                    }
                } else if (Transition.isWallpaper(windowContainer)) {
                    i |= 2;
                } else if (Transition.isInputMethod(windowContainer)) {
                    i |= 256;
                } else {
                    int windowType = windowContainer.getWindowType();
                    if (windowType >= 2000 && windowType <= 2999) {
                        i |= 65536;
                    }
                }
            }
            int i2 = this.mFlags;
            if ((i2 & 8) != 0 && (i2 & 16) == 0) {
                i |= 262144;
            }
            return (i2 & 32) != 0 ? i | 1048576 : i;
        }

        private boolean isWindowFillingTask(WindowContainer windowContainer, Task task) {
            Rect bounds = task.getBounds();
            int width = bounds.width();
            int height = bounds.height();
            Rect rect = this.mAbsoluteBounds;
            Rect bounds2 = windowContainer.getBounds();
            return (!this.mVisible || (width == rect.width() && height == rect.height())) && (!windowContainer.isVisibleRequested() || (width == bounds2.width() && height == bounds2.height()));
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void deferTransitionReady() {
        this.mReadyTracker.mDeferReadyDepth++;
        this.mSyncEngine.setReady(this.mSyncId, false);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void continueTransitionReady() {
        ReadyTracker readyTracker = this.mReadyTracker;
        readyTracker.mDeferReadyDepth--;
        applyReady();
    }

    public ITransitionWrapper getWrapper() {
        return this.mTransitionWrapper;
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
    private class TransitionWrapper implements ITransitionWrapper {
        private TransitionWrapper() {
        }

        @Override // com.android.server.wm.ITransitionWrapper
        public ITransitionExt getExtImpl() {
            return Transition.this.mTransitionExt;
        }

        @Override // com.android.server.wm.ITransitionWrapper
        public boolean isWcTranslucent(WindowContainer windowContainer) {
            return Transition.isTranslucent(windowContainer);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
    public static class ReadyTracker {
        private int mDeferReadyDepth;
        private final ArrayMap<WindowContainer, Boolean> mReadyGroups;
        private boolean mReadyOverride;
        private boolean mUsed;

        private ReadyTracker() {
            this.mReadyGroups = new ArrayMap<>();
            this.mUsed = false;
            this.mReadyOverride = false;
            this.mDeferReadyDepth = 0;
        }

        void addGroup(WindowContainer windowContainer) {
            if (this.mReadyGroups.containsKey(windowContainer)) {
                Slog.e(Transition.TAG, "Trying to add a ready-group twice: " + windowContainer);
                return;
            }
            this.mReadyGroups.put(windowContainer, Boolean.FALSE);
        }

        void setReadyFrom(WindowContainer windowContainer, boolean z) {
            this.mUsed = true;
            for (WindowContainer windowContainer2 = windowContainer; windowContainer2 != null; windowContainer2 = windowContainer2.getParent()) {
                if (Transition.isReadyGroup(windowContainer2)) {
                    this.mReadyGroups.put(windowContainer2, Boolean.valueOf(z));
                    if (ProtoLogCache.WM_DEBUG_WINDOW_TRANSITIONS_enabled) {
                        ProtoLogImpl.v(ProtoLogGroup.WM_DEBUG_WINDOW_TRANSITIONS, -1924376693, 3, (String) null, new Object[]{Boolean.valueOf(z), String.valueOf(windowContainer2), String.valueOf(windowContainer)});
                        return;
                    }
                    return;
                }
            }
        }

        void setAllReady() {
            if (ProtoLogCache.WM_DEBUG_WINDOW_TRANSITIONS_enabled) {
                ProtoLogImpl.v(ProtoLogGroup.WM_DEBUG_WINDOW_TRANSITIONS, 1670933628, 0, (String) null, (Object[]) null);
            }
            this.mUsed = true;
            this.mReadyOverride = true;
        }

        boolean allReady() {
            if (ProtoLogCache.WM_DEBUG_WINDOW_TRANSITIONS_enabled) {
                ProtoLogImpl.v(ProtoLogGroup.WM_DEBUG_WINDOW_TRANSITIONS, -1383884640, 31, (String) null, new Object[]{Boolean.valueOf(this.mUsed), Boolean.valueOf(this.mReadyOverride), Long.valueOf(this.mDeferReadyDepth), String.valueOf(groupsToString())});
            }
            if (!this.mUsed || this.mDeferReadyDepth > 0) {
                return false;
            }
            if (this.mReadyOverride) {
                return true;
            }
            for (int size = this.mReadyGroups.size() - 1; size >= 0; size--) {
                WindowContainer keyAt = this.mReadyGroups.keyAt(size);
                if (keyAt.isAttached() && keyAt.isVisibleRequested() && !this.mReadyGroups.valueAt(size).booleanValue()) {
                    return false;
                }
            }
            return true;
        }

        private String groupsToString() {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < this.mReadyGroups.size(); i++) {
                if (i != 0) {
                    sb.append(',');
                }
                sb.append(this.mReadyGroups.keyAt(i));
                sb.append(':');
                sb.append(this.mReadyGroups.valueAt(i));
            }
            return sb.toString();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
    public static class Targets {
        final SparseArray<ChangeInfo> mArray;
        private int mDepthFactor;
        private ArrayList<ChangeInfo> mRemovedTargets;

        private Targets() {
            this.mArray = new SparseArray<>();
        }

        void add(ChangeInfo changeInfo) {
            if (this.mDepthFactor == 0) {
                this.mDepthFactor = changeInfo.mContainer.mWmService.mRoot.getTreeWeight() + 1;
            }
            int prefixOrderIndex = changeInfo.mContainer.getPrefixOrderIndex();
            WindowContainer windowContainer = changeInfo.mContainer;
            while (windowContainer != null) {
                windowContainer = windowContainer.getParent();
                if (windowContainer != null) {
                    prefixOrderIndex += this.mDepthFactor;
                }
            }
            this.mArray.put(prefixOrderIndex, changeInfo);
        }

        void remove(int i) {
            ChangeInfo valueAt = this.mArray.valueAt(i);
            this.mArray.removeAt(i);
            if (this.mRemovedTargets == null) {
                this.mRemovedTargets = new ArrayList<>();
            }
            this.mRemovedTargets.add(valueAt);
        }

        boolean wasParticipated(ChangeInfo changeInfo) {
            ArrayList<ChangeInfo> arrayList;
            return this.mArray.indexOfValue(changeInfo) >= 0 || ((arrayList = this.mRemovedTargets) != null && arrayList.contains(changeInfo));
        }

        ArrayList<ChangeInfo> getListSortedByZ() {
            SparseArray sparseArray = new SparseArray(this.mArray.size());
            for (int size = this.mArray.size() - 1; size >= 0; size--) {
                sparseArray.put(this.mArray.keyAt(size) % this.mDepthFactor, this.mArray.valueAt(size));
            }
            ArrayList<ChangeInfo> arrayList = new ArrayList<>(sparseArray.size());
            for (int size2 = sparseArray.size() - 1; size2 >= 0; size2--) {
                arrayList.add((ChangeInfo) sparseArray.valueAt(size2));
            }
            return arrayList;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    @VisibleForTesting
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
    public class ScreenshotFreezer implements IContainerFreezer {
        private final ArraySet<WindowContainer> mFrozen;

        private ScreenshotFreezer() {
            this.mFrozen = new ArraySet<>();
        }

        @Override // com.android.server.wm.Transition.IContainerFreezer
        public boolean freeze(WindowContainer windowContainer, Rect rect) {
            if (!windowContainer.isVisibleRequested()) {
                return false;
            }
            for (WindowContainer windowContainer2 = windowContainer; windowContainer2 != null; windowContainer2 = windowContainer2.getParent()) {
                if (this.mFrozen.contains(windowContainer2)) {
                    return false;
                }
            }
            if (Transition.this.mIsSeamlessRotation) {
                WindowState topFullscreenOpaqueWindow = windowContainer.getDisplayContent() == null ? null : windowContainer.getDisplayContent().getDisplayPolicy().getTopFullscreenOpaqueWindow();
                if (topFullscreenOpaqueWindow != null && (topFullscreenOpaqueWindow == windowContainer || topFullscreenOpaqueWindow.isDescendantOf(windowContainer))) {
                    this.mFrozen.add(windowContainer);
                    return true;
                }
            }
            if (ProtoLogCache.WM_DEBUG_WINDOW_TRANSITIONS_enabled) {
                ProtoLogImpl.v(ProtoLogGroup.WM_DEBUG_WINDOW_TRANSITIONS, 390947100, 0, (String) null, new Object[]{String.valueOf(windowContainer.toString()), String.valueOf(rect.toString())});
            }
            Rect rect2 = new Rect(rect);
            rect2.offsetTo(0, 0);
            boolean z = windowContainer.asDisplayContent() != null && windowContainer.asDisplayContent().isRotationChanging();
            ScreenCapture.ScreenshotHardwareBuffer captureLayers = ScreenCapture.captureLayers(new ScreenCapture.LayerCaptureArgs.Builder(windowContainer.getSurfaceControl()).setSourceCrop(rect2).setUid(-222L).setCaptureSecureLayers(true).setAllowProtected(true).setHintForSeamlessTransition(z).build());
            HardwareBuffer hardwareBuffer = captureLayers == null ? null : captureLayers.getHardwareBuffer();
            if (hardwareBuffer == null || hardwareBuffer.getWidth() <= 1 || hardwareBuffer.getHeight() <= 1) {
                Slog.w(Transition.TAG, "Failed to capture screenshot for " + windowContainer);
                return false;
            }
            SurfaceControl build = windowContainer.makeAnimationLeash().setName(z ? "RotationLayer" : "transition snapshot: " + windowContainer).setOpaque(windowContainer.fillsParent()).setParent(windowContainer.getSurfaceControl()).setSecure(captureLayers.containsSecureLayers()).setCallsite("Transition.ScreenshotSync").setBLASTLayer().build();
            this.mFrozen.add(windowContainer);
            ChangeInfo changeInfo = Transition.this.mChanges.get(windowContainer);
            Objects.requireNonNull(changeInfo);
            changeInfo.mSnapshot = build;
            if (z) {
                changeInfo.mSnapshotLuma = TransitionAnimation.getBorderLuma(hardwareBuffer, captureLayers.getColorSpace());
            }
            SurfaceControl.Transaction transaction = windowContainer.mWmService.mTransactionFactory.get();
            TransitionAnimation.configureScreenshotLayer(transaction, build, captureLayers);
            transaction.show(build);
            transaction.setLayer(build, Integer.MAX_VALUE);
            transaction.apply();
            transaction.close();
            hardwareBuffer.close();
            windowContainer.getSyncTransaction().reparent(build, null);
            return true;
        }

        @Override // com.android.server.wm.Transition.IContainerFreezer
        public void cleanUp(SurfaceControl.Transaction transaction) {
            for (int i = 0; i < this.mFrozen.size(); i++) {
                ChangeInfo changeInfo = Transition.this.mChanges.get(this.mFrozen.valueAt(i));
                Objects.requireNonNull(changeInfo);
                SurfaceControl surfaceControl = changeInfo.mSnapshot;
                if (surfaceControl != null) {
                    transaction.reparent(surfaceControl, null);
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
    public static class Token extends Binder {
        final WeakReference<Transition> mTransition;

        Token(Transition transition) {
            this.mTransition = new WeakReference<>(transition);
        }

        public String toString() {
            return "Token{" + Integer.toHexString(System.identityHashCode(this)) + " " + this.mTransition.get() + "}";
        }
    }
}
