package com.android.server.wm;

import android.app.ActivityManager;
import android.app.IApplicationThread;
import android.app.WindowConfiguration;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Handler;
import android.os.IBinder;
import android.os.IRemoteCallback;
import android.os.RemoteException;
import android.os.SystemClock;
import android.os.SystemProperties;
import android.util.ArrayMap;
import android.util.Slog;
import android.util.SparseArray;
import android.util.TimeUtils;
import android.util.proto.ProtoOutputStream;
import android.view.SurfaceControl;
import android.window.ITransitionMetricsReporter;
import android.window.ITransitionPlayer;
import android.window.RemoteTransition;
import android.window.TransitionInfo;
import android.window.TransitionRequestInfo;
import android.window.WindowContainerTransaction;
import com.android.internal.annotations.GuardedBy;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.protolog.ProtoLogGroup;
import com.android.internal.protolog.ProtoLogImpl;
import com.android.server.FgThread;
import com.android.server.wm.BLASTSyncEngine;
import com.android.server.wm.Transition;
import com.android.server.wm.WindowManagerInternal;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.function.Consumer;
import java.util.function.LongConsumer;
import system.ext.loader.core.ExtLoader;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
public class TransitionController {
    private static final int CHANGE_TIMEOUT_MS = 2000;
    private static final int DEFAULT_TIMEOUT_MS = 5000;
    private static final int LEGACY_STATE_IDLE = 0;
    private static final int LEGACY_STATE_READY = 1;
    private static final int LEGACY_STATE_RUNNING = 2;
    private static final boolean SHELL_TRANSITIONS_ROTATION = SystemProperties.getBoolean("persist.wm.debug.shell_transit_rotate", false);
    static final int SYNC_METHOD = SystemProperties.getBoolean("persist.wm.debug.shell_transit_blast", false) ? 1 : 0;
    private static final String TAG = "TransitionController";
    final ActivityTaskManagerService mAtm;
    Transition mFinishingTransition;
    final RemotePlayer mRemotePlayer;
    SnapshotController mSnapshotController;
    BLASTSyncEngine mSyncEngine;
    private ITransitionPlayer mTransitionPlayer;
    private WindowProcessController mTransitionPlayerProc;
    TransitionTracer mTransitionTracer;
    final TransitionMetricsReporter mTransitionMetricsReporter = new TransitionMetricsReporter();
    private final ArrayList<WindowManagerInternal.AppTransitionListener> mLegacyListeners = new ArrayList<>();
    final ArrayList<Runnable> mStateValidators = new ArrayList<>();
    final ArrayList<ActivityRecord> mValidateCommitVis = new ArrayList<>();
    final ArrayList<ActivityRecord> mValidateActivityCompat = new ArrayList<>();
    private final ArrayList<Transition> mPlayingTransitions = new ArrayList<>();
    int mTrackCount = 0;
    final ArrayList<WindowState> mAnimatingExitWindows = new ArrayList<>();
    final Lock mRunningLock = new Lock();
    private final ArrayList<QueuedTransition> mQueuedTransitions = new ArrayList<>();
    private Transition mCollectingTransition = null;
    final ArrayList<Transition> mWaitingTransitions = new ArrayList<>();
    final SparseArray<ArrayList<Task>> mLatestOnTopTasksReported = new SparseArray<>();
    boolean mBuildingFinishLayers = false;
    boolean mNavigationBarAttachedToApp = false;
    private boolean mAnimatingState = false;
    final Handler mLoggerHandler = FgThread.getHandler();
    boolean mIsWaitingForDisplayEnabled = false;
    public ITransitionControllerExt mTransitionControllerExt = (ITransitionControllerExt) ExtLoader.type(ITransitionControllerExt.class).base(this).create();
    private ITransitionControllerWrapper mWrapper = new TransitionControllerWrapper();
    private final IBinder.DeathRecipient mTransitionPlayerDeath = new IBinder.DeathRecipient() { // from class: com.android.server.wm.TransitionController$$ExternalSyntheticLambda0
        @Override // android.os.IBinder.DeathRecipient
        public final void binderDied() {
            TransitionController.this.lambda$new$0();
        }
    };

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
    public interface OnStartCollect {
        void onCollectStarted(boolean z);
    }

    private static boolean isExistenceType(int i) {
        return i == 1 || i == 2;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
    public static class QueuedTransition {
        final BLASTSyncEngine.SyncGroup mLegacySync;
        final OnStartCollect mOnStartCollect;
        final Transition mTransition;

        QueuedTransition(Transition transition, OnStartCollect onStartCollect) {
            this.mTransition = transition;
            this.mOnStartCollect = onStartCollect;
            this.mLegacySync = null;
        }

        QueuedTransition(BLASTSyncEngine.SyncGroup syncGroup, OnStartCollect onStartCollect) {
            this.mTransition = null;
            this.mOnStartCollect = onStartCollect;
            this.mLegacySync = syncGroup;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public TransitionController(ActivityTaskManagerService activityTaskManagerService) {
        this.mAtm = activityTaskManagerService;
        this.mRemotePlayer = new RemotePlayer(activityTaskManagerService);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$0() {
        WindowManagerGlobalLock windowManagerGlobalLock = this.mAtm.mGlobalLock;
        WindowManagerService.boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                detachPlayer();
            } catch (Throwable th) {
                WindowManagerService.resetPriorityAfterLockedSection();
                throw th;
            }
        }
        WindowManagerService.resetPriorityAfterLockedSection();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setWindowManager(WindowManagerService windowManagerService) {
        this.mSnapshotController = windowManagerService.mSnapshotController;
        this.mTransitionTracer = windowManagerService.mTransitionTracer;
        this.mIsWaitingForDisplayEnabled = !windowManagerService.mDisplayEnabled;
        registerLegacyListener(windowManagerService.mActivityManagerAppTransitionNotifier);
        setSyncEngine(windowManagerService.mSyncEngine);
    }

    @VisibleForTesting
    void setSyncEngine(BLASTSyncEngine bLASTSyncEngine) {
        this.mSyncEngine = bLASTSyncEngine;
        bLASTSyncEngine.addOnIdleListener(new Runnable() { // from class: com.android.server.wm.TransitionController$$ExternalSyntheticLambda2
            @Override // java.lang.Runnable
            public final void run() {
                TransitionController.this.tryStartCollectFromQueue();
            }
        });
    }

    private void detachPlayer() {
        if (this.mTransitionPlayer == null) {
            return;
        }
        this.mTransitionPlayer = null;
        for (int i = 0; i < this.mPlayingTransitions.size(); i++) {
            this.mPlayingTransitions.get(i).cleanUpOnFailure();
        }
        this.mPlayingTransitions.clear();
        for (int i2 = 0; i2 < this.mWaitingTransitions.size(); i2++) {
            this.mWaitingTransitions.get(i2).abort();
        }
        this.mWaitingTransitions.clear();
        Transition transition = this.mCollectingTransition;
        if (transition != null) {
            transition.abort();
        }
        this.mTransitionPlayerProc = null;
        this.mRemotePlayer.clear();
        this.mRunningLock.doNotifyLocked();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Transition createTransition(int i) {
        return createTransition(i, 0);
    }

    private Transition createTransition(int i, int i2) {
        if (this.mTransitionPlayer == null) {
            throw new IllegalStateException("Shell Transitions not enabled");
        }
        if (this.mCollectingTransition != null) {
            throw new IllegalStateException("Trying to directly start transition collection while  collection is already ongoing. Use {@link #startCollectOrQueue} if possible.");
        }
        Transition transition = new Transition(i, i2, this, this.mSyncEngine);
        if (ProtoLogCache.WM_DEBUG_WINDOW_TRANSITIONS_enabled) {
            ProtoLogImpl.v(ProtoLogGroup.WM_DEBUG_WINDOW_TRANSITIONS, 259206414, 0, (String) null, new Object[]{String.valueOf(transition)});
        }
        moveToCollecting(transition);
        return transition;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void moveToCollecting(Transition transition) {
        if (this.mCollectingTransition != null) {
            throw new IllegalStateException("Simultaneous transition collection not supported.");
        }
        if (this.mTransitionPlayer == null) {
            transition.abort();
            return;
        }
        this.mCollectingTransition = transition;
        transition.startCollecting(transition.mType == 6 ? 2000L : 5000L);
        if (ProtoLogCache.WM_DEBUG_WINDOW_TRANSITIONS_enabled) {
            ProtoLogImpl.v(ProtoLogGroup.WM_DEBUG_WINDOW_TRANSITIONS, -1764792832, 0, (String) null, new Object[]{String.valueOf(this.mCollectingTransition)});
        }
        dispatchLegacyAppTransitionPending();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void registerTransitionPlayer(ITransitionPlayer iTransitionPlayer, WindowProcessController windowProcessController) {
        try {
            ITransitionPlayer iTransitionPlayer2 = this.mTransitionPlayer;
            if (iTransitionPlayer2 != null) {
                if (iTransitionPlayer2.asBinder() != null) {
                    this.mTransitionPlayer.asBinder().unlinkToDeath(this.mTransitionPlayerDeath, 0);
                }
                detachPlayer();
            }
            if (iTransitionPlayer.asBinder() != null) {
                iTransitionPlayer.asBinder().linkToDeath(this.mTransitionPlayerDeath, 0);
            }
            this.mTransitionPlayer = iTransitionPlayer;
            this.mTransitionPlayerProc = windowProcessController;
            this.mTransitionControllerExt.initFoldScreenBlackCoverStrategy();
        } catch (RemoteException unused) {
            throw new RuntimeException("Unable to set transition player");
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public ITransitionPlayer getTransitionPlayer() {
        return this.mTransitionPlayer;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean isShellTransitionsEnabled() {
        return this.mTransitionPlayer != null;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean useShellTransitionsRotation() {
        return isShellTransitionsEnabled() && SHELL_TRANSITIONS_ROTATION;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean isCollecting() {
        return this.mCollectingTransition != null;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Transition getCollectingTransition() {
        return this.mCollectingTransition;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int getCollectingTransitionId() {
        Transition transition = this.mCollectingTransition;
        if (transition == null) {
            throw new IllegalStateException("There is no collecting transition");
        }
        return transition.getSyncId();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean isCollecting(WindowContainer windowContainer) {
        Transition transition = this.mCollectingTransition;
        if (transition == null) {
            return false;
        }
        if (transition.mParticipants.contains(windowContainer)) {
            return true;
        }
        for (int i = 0; i < this.mWaitingTransitions.size(); i++) {
            if (this.mWaitingTransitions.get(i).mParticipants.contains(windowContainer)) {
                return true;
            }
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean inCollectingTransition(WindowContainer windowContainer) {
        if (!isCollecting()) {
            return false;
        }
        if (this.mCollectingTransition.isInTransition(windowContainer)) {
            return true;
        }
        for (int i = 0; i < this.mWaitingTransitions.size(); i++) {
            if (this.mWaitingTransitions.get(i).isInTransition(windowContainer)) {
                return true;
            }
        }
        return false;
    }

    boolean isPlaying() {
        return !this.mPlayingTransitions.isEmpty();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean inPlayingTransition(WindowContainer windowContainer) {
        for (int size = this.mPlayingTransitions.size() - 1; size >= 0; size--) {
            if (this.mPlayingTransitions.get(size).isInTransition(windowContainer)) {
                return true;
            }
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean inFinishingTransition(WindowContainer<?> windowContainer) {
        Transition transition = this.mFinishingTransition;
        return transition != null && transition.isInTransition(windowContainer);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean inTransition() {
        return isCollecting() || isPlaying() || !this.mQueuedTransitions.isEmpty();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean inTransition(WindowContainer windowContainer) {
        return inCollectingTransition(windowContainer) || inPlayingTransition(windowContainer);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean inTransition(int i) {
        Transition transition = this.mCollectingTransition;
        if (transition != null && transition.getSyncId() == i) {
            return true;
        }
        for (int size = this.mPlayingTransitions.size() - 1; size >= 0; size--) {
            if (this.mPlayingTransitions.get(size).getSyncId() == i) {
                return true;
            }
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean isTransitionOnDisplay(DisplayContent displayContent) {
        Transition transition = this.mCollectingTransition;
        if (transition != null && transition.isOnDisplay(displayContent)) {
            return true;
        }
        for (int size = this.mWaitingTransitions.size() - 1; size >= 0; size--) {
            if (this.mWaitingTransitions.get(size).isOnDisplay(displayContent)) {
                return true;
            }
        }
        for (int size2 = this.mPlayingTransitions.size() - 1; size2 >= 0; size2--) {
            if (this.mPlayingTransitions.get(size2).isOnDisplay(displayContent)) {
                return true;
            }
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean isTransientHide(Task task) {
        Transition transition = this.mCollectingTransition;
        if (transition != null && transition.isInTransientHide(task)) {
            return true;
        }
        for (int size = this.mWaitingTransitions.size() - 1; size >= 0; size--) {
            if (this.mWaitingTransitions.get(size).isInTransientHide(task)) {
                return true;
            }
        }
        for (int size2 = this.mPlayingTransitions.size() - 1; size2 >= 0; size2--) {
            if (this.mPlayingTransitions.get(size2).isInTransientHide(task)) {
                return true;
            }
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean canApplyDim(Task task) {
        if (task == null) {
            return true;
        }
        for (int size = this.mPlayingTransitions.size() - 1; size >= 0; size--) {
            if (!this.mPlayingTransitions.get(size).canApplyDim(task)) {
                return false;
            }
        }
        return true;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean shouldKeepFocus(WindowContainer windowContainer) {
        if (this.mCollectingTransition != null) {
            if (this.mPlayingTransitions.isEmpty()) {
                return this.mCollectingTransition.isInTransientHide(windowContainer);
            }
            return false;
        }
        if (this.mPlayingTransitions.size() == 1) {
            return this.mPlayingTransitions.get(0).isInTransientHide(windowContainer);
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean isTransientCollect(ActivityRecord activityRecord) {
        Transition transition = this.mCollectingTransition;
        return transition != null && transition.isTransientLaunch(activityRecord);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean isTransientLaunch(ActivityRecord activityRecord) {
        if (isTransientCollect(activityRecord)) {
            return true;
        }
        for (int size = this.mPlayingTransitions.size() - 1; size >= 0; size--) {
            if (this.mPlayingTransitions.get(size).isTransientLaunch(activityRecord)) {
                return true;
            }
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean canAssignLayers(WindowContainer windowContainer) {
        if (this.mBuildingFinishLayers) {
            return windowContainer.asWindowState() == null;
        }
        if (windowContainer.asWindowState() == null) {
            if (isPlaying()) {
                return false;
            }
            if (windowContainer.asTask() != null && isCollecting() && !this.mTransitionControllerExt.canAssignLayers(windowContainer)) {
                return false;
            }
        }
        return true;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @WindowConfiguration.WindowingMode
    public int getWindowingModeAtStart(WindowContainer windowContainer) {
        Transition transition = this.mCollectingTransition;
        if (transition == null) {
            return windowContainer.getWindowingMode();
        }
        Transition.ChangeInfo changeInfo = transition.mChanges.get(windowContainer);
        if (changeInfo == null) {
            return windowContainer.getWindowingMode();
        }
        return changeInfo.mWindowingMode;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int getCollectingTransitionType() {
        Transition transition = this.mCollectingTransition;
        if (transition != null) {
            return transition.mType;
        }
        return 0;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Transition requestTransitionIfNeeded(int i, WindowContainer windowContainer) {
        return requestTransitionIfNeeded(i, 0, windowContainer, windowContainer);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Transition requestTransitionIfNeeded(int i, int i2, WindowContainer windowContainer, WindowContainer windowContainer2) {
        return requestTransitionIfNeeded(i, i2, windowContainer, windowContainer2, null, null);
    }

    private void setDisplaySyncMethod(TransitionRequestInfo.DisplayChange displayChange, Transition transition, DisplayContent displayContent) {
        int startRotation = displayChange.getStartRotation();
        int endRotation = displayChange.getEndRotation();
        if (startRotation != endRotation && (startRotation + endRotation) % 2 == 0) {
            this.mSyncEngine.setSyncMethod(transition.getSyncId(), 1);
        }
        Rect startAbsBounds = displayChange.getStartAbsBounds();
        Rect endAbsBounds = displayChange.getEndAbsBounds();
        if (startAbsBounds == null || endAbsBounds == null) {
            return;
        }
        int width = startAbsBounds.width();
        int height = startAbsBounds.height();
        int width2 = endAbsBounds.width();
        int height2 = endAbsBounds.height();
        if ((width2 > width) == (height2 > height)) {
            if (width2 == width && height2 == height) {
                return;
            }
            displayContent.forAllWindows(new Consumer() { // from class: com.android.server.wm.TransitionController$$ExternalSyntheticLambda1
                @Override // java.util.function.Consumer
                public final void accept(Object obj) {
                    TransitionController.lambda$setDisplaySyncMethod$1((WindowState) obj);
                }
            }, true);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$setDisplaySyncMethod$1(WindowState windowState) {
        if (windowState.mToken.mRoundedCornerOverlay && windowState.mHasSurface) {
            windowState.mSyncMethodOverride = 1;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Transition requestTransitionIfNeeded(int i, int i2, WindowContainer windowContainer, WindowContainer windowContainer2, RemoteTransition remoteTransition, TransitionRequestInfo.DisplayChange displayChange) {
        r1 = null;
        Transition requestStartTransition = null;
        if (this.mTransitionPlayer == null) {
            return null;
        }
        if (isCollecting()) {
            if (displayChange != null) {
                Slog.e(TAG, "Provided displayChange for a non-new request", new Throwable());
            }
            this.mCollectingTransition.setReady(windowContainer2, false);
            int i3 = i2 & 14592;
            if (i3 != 0) {
                this.mCollectingTransition.addFlag(i3);
            }
        } else {
            requestStartTransition = requestStartTransition(createTransition(i, i2), windowContainer != null ? windowContainer.asTask() : null, remoteTransition, displayChange);
            if (requestStartTransition != null && displayChange != null && windowContainer != null && windowContainer.asDisplayContent() != null) {
                setDisplaySyncMethod(displayChange, requestStartTransition, windowContainer.asDisplayContent());
            }
        }
        if (windowContainer != null) {
            if (isExistenceType(i)) {
                collectExistenceChange(windowContainer);
            } else {
                collect(windowContainer);
            }
        }
        return requestStartTransition;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Transition requestStartTransition(final Transition transition, Task task, RemoteTransition remoteTransition, TransitionRequestInfo.DisplayChange displayChange) {
        ActivityManager.RunningTaskInfo runningTaskInfo;
        if (this.mIsWaitingForDisplayEnabled) {
            if (ProtoLogCache.WM_DEBUG_WINDOW_TRANSITIONS_enabled) {
                ProtoLogImpl.v(ProtoLogGroup.WM_DEBUG_WINDOW_TRANSITIONS, 1282992082, 1, (String) null, new Object[]{Long.valueOf(transition.getSyncId())});
            }
            transition.mIsPlayerEnabled = false;
            transition.mLogger.mRequestTimeNs = SystemClock.uptimeNanos();
            this.mAtm.mH.post(new Runnable() { // from class: com.android.server.wm.TransitionController$$ExternalSyntheticLambda6
                @Override // java.lang.Runnable
                public final void run() {
                    TransitionController.this.lambda$requestStartTransition$2(transition);
                }
            });
            return transition;
        }
        if (this.mTransitionPlayer == null || transition.isAborted()) {
            if (transition.isCollecting()) {
                transition.abort();
            }
            return transition;
        }
        try {
            if (ProtoLogCache.WM_DEBUG_WINDOW_TRANSITIONS_enabled) {
                ProtoLogImpl.v(ProtoLogGroup.WM_DEBUG_WINDOW_TRANSITIONS, 1794249572, 0, (String) null, new Object[]{String.valueOf(transition)});
            }
            this.mTransitionControllerExt.hookSetBinderUxFlag(true);
            if (task != null) {
                runningTaskInfo = new ActivityManager.RunningTaskInfo();
                task.fillTaskInfo(runningTaskInfo);
            } else {
                runningTaskInfo = null;
            }
            TransitionRequestInfo transitionRequestInfo = new TransitionRequestInfo(transition.mType, runningTaskInfo, remoteTransition, displayChange);
            transition.mLogger.mRequestTimeNs = SystemClock.elapsedRealtimeNanos();
            transition.mLogger.mRequest = transitionRequestInfo;
            this.mTransitionControllerExt.requestStartTransition(transitionRequestInfo, transition);
            this.mTransitionControllerExt.notifySysWindowRotation(TransitionController.class, null, displayChange);
            this.mTransitionPlayer.requestStartTransition(transition.getToken(), transitionRequestInfo);
            if (remoteTransition != null) {
                transition.setRemoteAnimationApp(remoteTransition.getAppThread());
            }
        } catch (RemoteException e) {
            Slog.e(TAG, "Error requesting transition", e);
            transition.cleanUpOnFailure();
        }
        this.mTransitionControllerExt.hookSetBinderUxFlag(false);
        return transition;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$requestStartTransition$2(Transition transition) {
        this.mAtm.mWindowOrganizerController.startTransition(transition.getToken(), null);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Transition requestCloseTransitionIfNeeded(WindowContainer<?> windowContainer) {
        if (this.mTransitionPlayer == null || this.mTransitionControllerExt.skipRequestCloseTransitionIfNeeded(windowContainer)) {
            return null;
        }
        if (windowContainer.isVisibleRequested()) {
            r1 = isCollecting() ? null : requestStartTransition(createTransition(2, 0), windowContainer.asTask(), null, null);
            collectExistenceChange(windowContainer);
        } else {
            collect(windowContainer);
        }
        return r1;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void collect(WindowContainer windowContainer) {
        Transition transition = this.mCollectingTransition;
        if (transition == null) {
            return;
        }
        transition.collect(windowContainer);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void collectExistenceChange(WindowContainer windowContainer) {
        Transition transition = this.mCollectingTransition;
        if (transition == null) {
            return;
        }
        transition.collectExistenceChange(windowContainer);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void recordTaskOrder(WindowContainer windowContainer) {
        Transition transition = this.mCollectingTransition;
        if (transition == null) {
            return;
        }
        transition.recordTaskOrder(windowContainer);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void collectForDisplayAreaChange(DisplayArea<?> displayArea) {
        final Transition transition = this.mCollectingTransition;
        if (transition == null || !transition.mParticipants.contains(displayArea)) {
            return;
        }
        transition.collectVisibleChange(displayArea);
        displayArea.forAllLeafTasks(new Consumer() { // from class: com.android.server.wm.TransitionController$$ExternalSyntheticLambda3
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                TransitionController.lambda$collectForDisplayAreaChange$3(Transition.this, (Task) obj);
            }
        }, true);
        DisplayContent asDisplayContent = displayArea.asDisplayContent();
        if (asDisplayContent != null) {
            final boolean z = asDisplayContent.getAsyncRotationController() == null;
            displayArea.forAllWindows(new Consumer() { // from class: com.android.server.wm.TransitionController$$ExternalSyntheticLambda4
                @Override // java.util.function.Consumer
                public final void accept(Object obj) {
                    TransitionController.this.lambda$collectForDisplayAreaChange$4(z, transition, (WindowState) obj);
                }
            }, true);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$collectForDisplayAreaChange$3(Transition transition, Task task) {
        if (task.isVisible()) {
            transition.collect(task);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$collectForDisplayAreaChange$4(boolean z, Transition transition, WindowState windowState) {
        if (windowState.mActivityRecord == null && windowState.isVisible() && !isCollecting(windowState.mToken)) {
            if (z || !AsyncRotationController.canBeAsync(windowState.mToken)) {
                transition.collect(windowState.mToken);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void collectVisibleChange(WindowContainer windowContainer) {
        if (isCollecting()) {
            this.mCollectingTransition.collectVisibleChange(windowContainer);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void collectReparentChange(WindowContainer windowContainer, WindowContainer windowContainer2) {
        if (isCollecting()) {
            this.mCollectingTransition.collectReparentChange(windowContainer, windowContainer2);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setStatusBarTransitionDelay(long j) {
        Transition transition = this.mCollectingTransition;
        if (transition == null) {
            return;
        }
        transition.mStatusBarTransitionDelay = j;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setOverrideAnimation(TransitionInfo.AnimationOptions animationOptions, IRemoteCallback iRemoteCallback, IRemoteCallback iRemoteCallback2) {
        Transition transition = this.mCollectingTransition;
        if (transition == null) {
            return;
        }
        transition.setOverrideAnimation(animationOptions, iRemoteCallback, iRemoteCallback2);
        this.mTransitionControllerExt.setOverrideAnimation(animationOptions, this.mCollectingTransition);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setNoAnimation(WindowContainer windowContainer) {
        Transition transition = this.mCollectingTransition;
        if (transition == null) {
            return;
        }
        transition.setNoAnimation(windowContainer);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setReady(WindowContainer windowContainer, boolean z) {
        Transition transition = this.mCollectingTransition;
        if (transition == null) {
            return;
        }
        transition.setReady(windowContainer, z);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setReady(WindowContainer windowContainer) {
        setReady(windowContainer, true);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void deferTransitionReady() {
        if (isShellTransitionsEnabled()) {
            Transition transition = this.mCollectingTransition;
            if (transition == null) {
                throw new IllegalStateException("No collecting transition to defer readiness for.");
            }
            transition.deferTransitionReady();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void continueTransitionReady() {
        if (isShellTransitionsEnabled()) {
            Transition transition = this.mCollectingTransition;
            if (transition == null) {
                throw new IllegalStateException("No collecting transition to defer readiness for.");
            }
            transition.continueTransitionReady();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void finishTransition(Transition transition) {
        this.mTransitionMetricsReporter.reportAnimationStart(transition.getToken(), 0L);
        this.mAtm.endLaunchPowerMode(2);
        if (!this.mPlayingTransitions.contains(transition)) {
            Slog.e(TAG, "Trying to finish a non-playing transition " + transition);
            return;
        }
        if (ProtoLogCache.WM_DEBUG_WINDOW_TRANSITIONS_enabled) {
            ProtoLogImpl.v(ProtoLogGroup.WM_DEBUG_WINDOW_TRANSITIONS, -622017164, 0, (String) null, new Object[]{String.valueOf(transition)});
        }
        this.mPlayingTransitions.remove(transition);
        if (!inTransition()) {
            this.mTrackCount = 0;
        }
        updateRunningRemoteAnimation(transition, false);
        transition.finishTransition();
        for (int size = this.mAnimatingExitWindows.size() - 1; size >= 0; size--) {
            WindowState windowState = this.mAnimatingExitWindows.get(size);
            if (windowState.mAnimatingExit && windowState.mHasSurface && !windowState.inTransition()) {
                windowState.onExitAnimationDone();
            }
            if (!windowState.mAnimatingExit || !windowState.mHasSurface) {
                this.mAnimatingExitWindows.remove(size);
            }
        }
        this.mRunningLock.doNotifyLocked();
        if (!inTransition()) {
            validateStates();
            this.mAtm.mWindowManager.onAnimationFinished();
        }
        this.mTransitionControllerExt.finishTransition(transition);
        if (inTransition() || this.mAtm.mWindowManager.mH.hasMessages(61)) {
            return;
        }
        Slog.d(TAG, "NFW_send RECOMPUTE_FOCUS in finishTransition");
        this.mAtm.mWindowManager.mH.sendEmptyMessage(61);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void onCommittedInvisibles() {
        Transition transition = this.mCollectingTransition;
        if (transition != null) {
            transition.mPriorVisibilityMightBeDirty = true;
        }
        for (int size = this.mWaitingTransitions.size() - 1; size >= 0; size--) {
            this.mWaitingTransitions.get(size).mPriorVisibilityMightBeDirty = true;
        }
    }

    private void validateStates() {
        for (int i = 0; i < this.mStateValidators.size(); i++) {
            this.mStateValidators.get(i).run();
            if (inTransition()) {
                this.mStateValidators.subList(0, i + 1).clear();
                return;
            }
        }
        this.mStateValidators.clear();
        for (int i2 = 0; i2 < this.mValidateCommitVis.size(); i2++) {
            ActivityRecord activityRecord = this.mValidateCommitVis.get(i2);
            if (!activityRecord.isVisibleRequested() && activityRecord.isVisible()) {
                Slog.e(TAG, "Uncommitted visibility change: " + activityRecord);
                activityRecord.commitVisibility(activityRecord.isVisibleRequested(), false, false);
            }
        }
        this.mValidateCommitVis.clear();
        for (int i3 = 0; i3 < this.mValidateActivityCompat.size(); i3++) {
            ActivityRecord activityRecord2 = this.mValidateActivityCompat.get(i3);
            if (activityRecord2.getSurfaceControl() != null) {
                activityRecord2.getRelativePosition(new Point());
                activityRecord2.getSyncTransaction().setPosition(activityRecord2.getSurfaceControl(), r2.x, r2.y);
            }
        }
        this.mValidateActivityCompat.clear();
        this.mTransitionControllerExt.validateKeyguardOcclusion(this.mAtm.mRootWindowContainer.getDefaultDisplay());
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void onTransitionPopulated(Transition transition) {
        tryStartCollectFromQueue();
    }

    private boolean canStartCollectingNow(Transition transition) {
        Transition transition2 = this.mCollectingTransition;
        if (transition2 == null) {
            return true;
        }
        if (!transition2.isPopulated() || !getCanBeIndependent(this.mCollectingTransition, transition)) {
            return false;
        }
        for (int i = 0; i < this.mWaitingTransitions.size(); i++) {
            if (!getCanBeIndependent(this.mWaitingTransitions.get(i), transition)) {
                return false;
            }
        }
        return true;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void tryStartCollectFromQueue() {
        if (this.mQueuedTransitions.isEmpty()) {
            return;
        }
        final QueuedTransition queuedTransition = this.mQueuedTransitions.get(0);
        if (this.mCollectingTransition != null) {
            Transition transition = queuedTransition.mTransition;
            if (transition == null || !canStartCollectingNow(transition)) {
                return;
            }
            if (ProtoLogCache.WM_DEBUG_WINDOW_TRANSITIONS_MIN_enabled) {
                ProtoLogImpl.v(ProtoLogGroup.WM_DEBUG_WINDOW_TRANSITIONS_MIN, -266707683, 1, "Moving #%d from collecting to waiting.", new Object[]{Long.valueOf(this.mCollectingTransition.getSyncId())});
            }
            this.mWaitingTransitions.add(this.mCollectingTransition);
            this.mCollectingTransition = null;
        } else if (this.mSyncEngine.hasActiveSync()) {
            return;
        }
        this.mQueuedTransitions.remove(0);
        Transition transition2 = queuedTransition.mTransition;
        if (transition2 != null) {
            moveToCollecting(transition2);
        } else {
            this.mSyncEngine.startSyncSet(queuedTransition.mLegacySync);
        }
        this.mAtm.mH.post(new Runnable() { // from class: com.android.server.wm.TransitionController$$ExternalSyntheticLambda7
            @Override // java.lang.Runnable
            public final void run() {
                TransitionController.this.lambda$tryStartCollectFromQueue$5(queuedTransition);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$tryStartCollectFromQueue$5(QueuedTransition queuedTransition) {
        WindowManagerGlobalLock windowManagerGlobalLock = this.mAtm.mGlobalLock;
        WindowManagerService.boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                queuedTransition.mOnStartCollect.onCollectStarted(true);
            } catch (Throwable th) {
                WindowManagerService.resetPriorityAfterLockedSection();
                throw th;
            }
        }
        WindowManagerService.resetPriorityAfterLockedSection();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void moveToPlaying(Transition transition) {
        if (transition == this.mCollectingTransition) {
            this.mCollectingTransition = null;
            if (!this.mWaitingTransitions.isEmpty()) {
                this.mCollectingTransition = this.mWaitingTransitions.remove(0);
            }
            if (this.mCollectingTransition == null) {
                this.mLatestOnTopTasksReported.clear();
            }
        } else if (!this.mWaitingTransitions.remove(transition)) {
            throw new IllegalStateException("Trying to move non-collecting transition toplaying " + transition.getSyncId());
        }
        this.mPlayingTransitions.add(transition);
        updateRunningRemoteAnimation(transition, true);
    }

    boolean getCanBeIndependent(Transition transition, Transition transition2) {
        if (transition2 != null && transition2.mParallelCollectType == 1 && transition.mParallelCollectType == 1) {
            return true;
        }
        if (transition2 == null || transition2.mParallelCollectType != 2) {
            return transition.mParallelCollectType == 2;
        }
        if (transition.mParallelCollectType == 2) {
            return false;
        }
        for (int i = 0; i < transition.mParticipants.size(); i++) {
            WindowContainer valueAt = transition.mParticipants.valueAt(i);
            ActivityRecord asActivityRecord = valueAt.asActivityRecord();
            if (asActivityRecord == null && valueAt.asWindowState() == null && valueAt.asWindowToken() == null) {
                return false;
            }
            if (asActivityRecord != null && asActivityRecord.isActivityTypeHomeOrRecents()) {
                return false;
            }
        }
        return true;
    }

    static boolean getIsIndependent(Transition transition, Transition transition2) {
        int i = transition.mParallelCollectType;
        if (i == 1 && transition2.mParallelCollectType == 1) {
            return true;
        }
        if (i == 2 && transition.hasTransientLaunch()) {
            if (transition2.mParallelCollectType == 2) {
                return false;
            }
            transition2 = transition;
            transition = transition2;
        } else if (transition2.mParallelCollectType != 2 || !transition2.hasTransientLaunch()) {
            return false;
        }
        for (int i2 = 0; i2 < transition.mTargets.size(); i2++) {
            WindowContainer windowContainer = transition.mTargets.get(i2).mContainer;
            ActivityRecord asActivityRecord = windowContainer.asActivityRecord();
            if (asActivityRecord == null && windowContainer.asWindowState() == null && windowContainer.asWindowToken() == null) {
                return false;
            }
            if (asActivityRecord != null && transition2.isTransientLaunch(asActivityRecord)) {
                return false;
            }
        }
        return true;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void assignTrack(Transition transition, TransitionInfo transitionInfo) {
        boolean z;
        int i = -1;
        int i2 = 0;
        while (true) {
            if (i2 >= this.mPlayingTransitions.size()) {
                z = false;
                break;
            }
            if (this.mPlayingTransitions.get(i2) != transition && !getIsIndependent(this.mPlayingTransitions.get(i2), transition)) {
                if (i >= 0) {
                    z = true;
                    break;
                }
                i = this.mPlayingTransitions.get(i2).mAnimationTrack;
            }
            i2++;
        }
        int i3 = z ? 0 : i;
        if (i3 < 0 && (i3 = this.mTrackCount) > 0 && ProtoLogCache.WM_DEBUG_WINDOW_TRANSITIONS_enabled) {
            ProtoLogImpl.v(ProtoLogGroup.WM_DEBUG_WINDOW_TRANSITIONS, -1005167552, 5, (String) null, new Object[]{Long.valueOf(transition.getSyncId()), Long.valueOf(i3)});
        }
        if (z) {
            transitionInfo.setFlags(transitionInfo.getFlags() | 2097152);
            if (ProtoLogCache.WM_DEBUG_WINDOW_TRANSITIONS_enabled) {
                ProtoLogImpl.v(ProtoLogGroup.WM_DEBUG_WINDOW_TRANSITIONS, -774908272, 1, (String) null, new Object[]{Long.valueOf(transition.getSyncId())});
            }
        }
        transition.mAnimationTrack = i3;
        transitionInfo.setTrack(i3);
        this.mTrackCount = Math.max(this.mTrackCount, i3 + 1);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void updateAnimatingState(SurfaceControl.Transaction transaction) {
        Transition transition;
        boolean z = !this.mPlayingTransitions.isEmpty() || ((transition = this.mCollectingTransition) != null && transition.isStarted());
        if (z && !this.mAnimatingState) {
            transaction.setEarlyWakeupStart();
            this.mSnapshotController.setPause(true);
            this.mAnimatingState = true;
            Transition.asyncTraceBegin("animating", 68942577);
            return;
        }
        if (z || !this.mAnimatingState) {
            return;
        }
        transaction.setEarlyWakeupEnd();
        this.mSnapshotController.setPause(false);
        this.mAnimatingState = false;
        Transition.asyncTraceEnd(68942577);
    }

    private void updateRunningRemoteAnimation(Transition transition, boolean z) {
        WindowProcessController processController;
        WindowProcessController windowProcessController = this.mTransitionPlayerProc;
        if (windowProcessController == null) {
            return;
        }
        if (z) {
            windowProcessController.setRunningRemoteAnimation(true);
        } else if (this.mPlayingTransitions.isEmpty()) {
            this.mTransitionPlayerProc.setRunningRemoteAnimation(false);
            this.mRemotePlayer.clear();
            return;
        }
        IApplicationThread remoteAnimationApp = transition.getRemoteAnimationApp();
        if (remoteAnimationApp == null || remoteAnimationApp == this.mTransitionPlayerProc.getThread() || (processController = this.mAtm.getProcessController(remoteAnimationApp)) == null) {
            return;
        }
        this.mRemotePlayer.update(processController, z, true);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void onAbort(Transition transition) {
        if (transition != this.mCollectingTransition) {
            int indexOf = this.mWaitingTransitions.indexOf(transition);
            if (indexOf < 0) {
                throw new IllegalStateException("Too late for abort.");
            }
            this.mWaitingTransitions.remove(indexOf);
            return;
        }
        this.mCollectingTransition = null;
        if (!this.mWaitingTransitions.isEmpty()) {
            this.mCollectingTransition = this.mWaitingTransitions.remove(0);
        }
        if (this.mCollectingTransition == null) {
            this.mLatestOnTopTasksReported.clear();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setTransientLaunch(ActivityRecord activityRecord, Task task) {
        Transition transition = this.mCollectingTransition;
        if (transition == null) {
            return;
        }
        transition.setTransientLaunch(activityRecord, task);
        if (activityRecord.isActivityTypeHomeOrRecents()) {
            this.mCollectingTransition.addFlag(128);
            activityRecord.getTask().setCanAffectSystemUiFlags(false);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setCanPipOnFinish(boolean z) {
        Transition transition = this.mCollectingTransition;
        if (transition == null) {
            return;
        }
        transition.setCanPipOnFinish(z);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void legacyDetachNavigationBarFromApp(IBinder iBinder) {
        Transition fromBinder = Transition.fromBinder(iBinder);
        if (fromBinder == null || !this.mPlayingTransitions.contains(fromBinder)) {
            Slog.e(TAG, "Transition isn't playing: " + iBinder);
            return;
        }
        fromBinder.legacyRestoreNavigationBarFromApp();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void registerLegacyListener(WindowManagerInternal.AppTransitionListener appTransitionListener) {
        this.mLegacyListeners.add(appTransitionListener);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void unregisterLegacyListener(WindowManagerInternal.AppTransitionListener appTransitionListener) {
        this.mLegacyListeners.remove(appTransitionListener);
    }

    void dispatchLegacyAppTransitionPending() {
        for (int i = 0; i < this.mLegacyListeners.size(); i++) {
            this.mLegacyListeners.get(i).onAppTransitionPendingLocked();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void dispatchLegacyAppTransitionStarting(TransitionInfo transitionInfo, long j) {
        for (int i = 0; i < this.mLegacyListeners.size(); i++) {
            this.mLegacyListeners.get(i).onAppTransitionStartingLocked(SystemClock.uptimeMillis() + j, 120L);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void dispatchLegacyAppTransitionFinished(ActivityRecord activityRecord) {
        for (int i = 0; i < this.mLegacyListeners.size(); i++) {
            this.mLegacyListeners.get(i).onAppTransitionFinishedLocked(activityRecord.token);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void dispatchLegacyAppTransitionCancelled() {
        for (int i = 0; i < this.mLegacyListeners.size(); i++) {
            this.mLegacyListeners.get(i).onAppTransitionCancelledLocked(false);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void dumpDebugLegacy(ProtoOutputStream protoOutputStream, long j) {
        int i;
        long start = protoOutputStream.start(j);
        if (this.mPlayingTransitions.isEmpty()) {
            Transition transition = this.mCollectingTransition;
            i = ((transition == null || !transition.getLegacyIsReady()) && !this.mSyncEngine.hasPendingSyncSets()) ? 0 : 1;
        } else {
            i = 2;
        }
        protoOutputStream.write(1159641169921L, i);
        protoOutputStream.end(start);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void dump(PrintWriter printWriter, String str, boolean z) {
        String str2 = "  " + str;
        printWriter.println(str + "TransitionController:");
        printWriter.println(str2 + "  mPlayingTransitions:" + this.mPlayingTransitions.size());
        for (int i = 0; i < this.mPlayingTransitions.size(); i++) {
            this.mPlayingTransitions.get(i).dump(printWriter, str2, z);
        }
        printWriter.println(str2 + "mWaitingTransitions:" + this.mWaitingTransitions.size());
        for (int i2 = 0; i2 < this.mWaitingTransitions.size(); i2++) {
            this.mWaitingTransitions.get(i2).dump(printWriter, str2, z);
        }
        if (this.mCollectingTransition != null) {
            printWriter.println(str2 + "mCollectingTransition:");
            this.mCollectingTransition.dump(printWriter, str2 + "    ", z);
        }
        this.mSyncEngine.dump(printWriter, str, z);
    }

    private void queueTransition(Transition transition, OnStartCollect onStartCollect) {
        this.mQueuedTransitions.add(new QueuedTransition(transition, onStartCollect));
        if (ProtoLogCache.WM_DEBUG_WINDOW_TRANSITIONS_MIN_enabled) {
            ProtoLogImpl.v(ProtoLogGroup.WM_DEBUG_WINDOW_TRANSITIONS_MIN, 1735199721, 0, "Queueing transition: %s", new Object[]{String.valueOf(transition)});
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean startCollectOrQueue(Transition transition, OnStartCollect onStartCollect) {
        if (!this.mQueuedTransitions.isEmpty()) {
            queueTransition(transition, onStartCollect);
            return false;
        }
        if (this.mSyncEngine.hasActiveSync()) {
            if (isCollecting()) {
                if (canStartCollectingNow(transition)) {
                    if (ProtoLogCache.WM_DEBUG_WINDOW_TRANSITIONS_MIN_enabled) {
                        ProtoLogImpl.v(ProtoLogGroup.WM_DEBUG_WINDOW_TRANSITIONS_MIN, -266707683, 1, "Moving #%d from collecting to waiting.", new Object[]{Long.valueOf(this.mCollectingTransition.getSyncId())});
                    }
                    this.mWaitingTransitions.add(this.mCollectingTransition);
                    this.mCollectingTransition = null;
                    moveToCollecting(transition);
                    onStartCollect.onCollectStarted(false);
                    return true;
                }
            } else {
                Slog.w(TAG, "Ongoing Sync outside of transition.");
            }
            queueTransition(transition, onStartCollect);
            return false;
        }
        moveToCollecting(transition);
        onStartCollect.onCollectStarted(false);
        return true;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Transition createAndStartCollecting(int i) {
        if (this.mTransitionPlayer == null || !this.mQueuedTransitions.isEmpty()) {
            return null;
        }
        if (this.mSyncEngine.hasActiveSync()) {
            if (isCollecting()) {
                if (canStartCollectingNow(null)) {
                    if (ProtoLogCache.WM_DEBUG_WINDOW_TRANSITIONS_MIN_enabled) {
                        ProtoLogImpl.v(ProtoLogGroup.WM_DEBUG_WINDOW_TRANSITIONS_MIN, -266707683, 1, "Moving #%d from collecting to waiting.", new Object[]{Long.valueOf(this.mCollectingTransition.getSyncId())});
                    }
                    this.mWaitingTransitions.add(this.mCollectingTransition);
                    this.mCollectingTransition = null;
                    Transition transition = new Transition(i, 0, this, this.mSyncEngine);
                    moveToCollecting(transition);
                    return transition;
                }
            } else {
                Slog.w(TAG, "Ongoing Sync outside of transition.");
            }
            return null;
        }
        Transition transition2 = new Transition(i, 0, this, this.mSyncEngine);
        moveToCollecting(transition2);
        return transition2;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void startLegacySyncOrQueue(BLASTSyncEngine.SyncGroup syncGroup, final Consumer<Boolean> consumer) {
        if (!this.mQueuedTransitions.isEmpty() || this.mSyncEngine.hasActiveSync()) {
            this.mQueuedTransitions.add(new QueuedTransition(syncGroup, new OnStartCollect() { // from class: com.android.server.wm.TransitionController$$ExternalSyntheticLambda5
                @Override // com.android.server.wm.TransitionController.OnStartCollect
                public final void onCollectStarted(boolean z) {
                    TransitionController.lambda$startLegacySyncOrQueue$6(consumer, z);
                }
            }));
            if (ProtoLogCache.WM_DEBUG_WINDOW_TRANSITIONS_MIN_enabled) {
                ProtoLogImpl.v(ProtoLogGroup.WM_DEBUG_WINDOW_TRANSITIONS_MIN, 1463355909, 0, "Queueing legacy sync-set: %s", new Object[]{String.valueOf(syncGroup.mSyncId)});
                return;
            }
            return;
        }
        this.mSyncEngine.startSyncSet(syncGroup);
        consumer.accept(Boolean.FALSE);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$startLegacySyncOrQueue$6(Consumer consumer, boolean z) {
        consumer.accept(Boolean.TRUE);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
    public static class RemotePlayer {
        private static final long REPORT_RUNNING_GRACE_PERIOD_MS = 100;
        private final ActivityTaskManagerService mAtm;

        @GuardedBy({"itself"})
        private final ArrayMap<IBinder, DelegateProcess> mDelegateProcesses = new ArrayMap<>();

        /* JADX INFO: Access modifiers changed from: private */
        /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
        public class DelegateProcess implements Runnable {
            boolean mNeedReport;
            final WindowProcessController mProc;

            DelegateProcess(WindowProcessController windowProcessController) {
                this.mProc = windowProcessController;
            }

            @Override // java.lang.Runnable
            public void run() {
                synchronized (RemotePlayer.this.mAtm.mGlobalLockWithoutBoost) {
                    RemotePlayer.this.update(this.mProc, false, false);
                }
            }
        }

        RemotePlayer(ActivityTaskManagerService activityTaskManagerService) {
            this.mAtm = activityTaskManagerService;
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public void update(WindowProcessController windowProcessController, boolean z, boolean z2) {
            boolean z3 = true;
            if (!z) {
                synchronized (this.mDelegateProcesses) {
                    int size = this.mDelegateProcesses.size() - 1;
                    while (true) {
                        if (size < 0) {
                            z3 = false;
                            break;
                        } else {
                            if (this.mDelegateProcesses.valueAt(size).mProc == windowProcessController) {
                                this.mDelegateProcesses.removeAt(size);
                                break;
                            }
                            size--;
                        }
                    }
                    if (z3) {
                        windowProcessController.setRunningRemoteAnimation(false);
                        return;
                    }
                    return;
                }
            }
            if (windowProcessController.isRunningRemoteTransition() || !windowProcessController.hasThread()) {
                return;
            }
            windowProcessController.setRunningRemoteAnimation(true);
            DelegateProcess delegateProcess = new DelegateProcess(windowProcessController);
            if (z2) {
                delegateProcess.mNeedReport = true;
                this.mAtm.mH.postDelayed(delegateProcess, REPORT_RUNNING_GRACE_PERIOD_MS);
            }
            synchronized (this.mDelegateProcesses) {
                this.mDelegateProcesses.put(windowProcessController.getThread().asBinder(), delegateProcess);
            }
        }

        void clear() {
            synchronized (this.mDelegateProcesses) {
                for (int size = this.mDelegateProcesses.size() - 1; size >= 0; size--) {
                    this.mDelegateProcesses.valueAt(size).mProc.setRunningRemoteAnimation(false);
                }
                this.mDelegateProcesses.clear();
            }
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public boolean reportRunning(IApplicationThread iApplicationThread) {
            DelegateProcess delegateProcess;
            synchronized (this.mDelegateProcesses) {
                delegateProcess = this.mDelegateProcesses.get(iApplicationThread.asBinder());
                if (delegateProcess != null && delegateProcess.mNeedReport) {
                    delegateProcess.mNeedReport = false;
                    this.mAtm.mH.removeCallbacks(delegateProcess);
                }
            }
            return delegateProcess != null;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
    public static class Logger {
        long mAbortTimeNs;
        long mCollectTimeNs;
        long mCreateTimeNs;
        long mCreateWallTimeMs;
        long mFinishTimeNs;
        TransitionInfo mInfo;
        long mReadyTimeNs;
        TransitionRequestInfo mRequest;
        long mRequestTimeNs;
        long mSendTimeNs;
        long mStartTimeNs;
        WindowContainerTransaction mStartWCT;
        int mSyncId;

        private String buildOnSendLog() {
            StringBuilder sb = new StringBuilder("Sent Transition #");
            sb.append(this.mSyncId);
            sb.append(" createdAt=");
            sb.append(TimeUtils.logTimeOfDay(this.mCreateWallTimeMs));
            if (this.mRequest != null) {
                sb.append(" via request=");
                sb.append(this.mRequest);
            }
            return sb.toString();
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public void logOnSend() {
            if (ProtoLogCache.WM_DEBUG_WINDOW_TRANSITIONS_MIN_enabled) {
                ProtoLogImpl.v(ProtoLogGroup.WM_DEBUG_WINDOW_TRANSITIONS_MIN, 2021079047, 0, "%s", new Object[]{String.valueOf(buildOnSendLog())});
            }
            if (ProtoLogCache.WM_DEBUG_WINDOW_TRANSITIONS_MIN_enabled) {
                ProtoLogImpl.v(ProtoLogGroup.WM_DEBUG_WINDOW_TRANSITIONS_MIN, 1621562070, 0, "    startWCT=%s", new Object[]{String.valueOf(this.mStartWCT)});
            }
            if (ProtoLogCache.WM_DEBUG_WINDOW_TRANSITIONS_MIN_enabled) {
                ProtoLogImpl.v(ProtoLogGroup.WM_DEBUG_WINDOW_TRANSITIONS_MIN, 273212558, 0, "    info=%s", new Object[]{String.valueOf(this.mInfo)});
            }
        }

        private static String toMsString(long j) {
            return (Math.round(j / 1000.0d) / 1000.0d) + "ms";
        }

        private String buildOnFinishLog() {
            StringBuilder sb = new StringBuilder("Finish Transition #");
            sb.append(this.mSyncId);
            sb.append(": created at ");
            sb.append(TimeUtils.logTimeOfDay(this.mCreateWallTimeMs));
            sb.append(" collect-started=");
            sb.append(toMsString(this.mCollectTimeNs - this.mCreateTimeNs));
            if (this.mRequestTimeNs != 0) {
                sb.append(" request-sent=");
                sb.append(toMsString(this.mRequestTimeNs - this.mCreateTimeNs));
            }
            sb.append(" started=");
            sb.append(toMsString(this.mStartTimeNs - this.mCreateTimeNs));
            sb.append(" ready=");
            sb.append(toMsString(this.mReadyTimeNs - this.mCreateTimeNs));
            sb.append(" sent=");
            sb.append(toMsString(this.mSendTimeNs - this.mCreateTimeNs));
            sb.append(" finished=");
            sb.append(toMsString(this.mFinishTimeNs - this.mCreateTimeNs));
            return sb.toString();
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public void logOnFinish() {
            if (ProtoLogCache.WM_DEBUG_WINDOW_TRANSITIONS_MIN_enabled) {
                ProtoLogImpl.v(ProtoLogGroup.WM_DEBUG_WINDOW_TRANSITIONS_MIN, 2021079047, 0, "%s", new Object[]{String.valueOf(buildOnFinishLog())});
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
    public static class TransitionMetricsReporter extends ITransitionMetricsReporter.Stub {
        private final ArrayMap<IBinder, LongConsumer> mMetricConsumers = new ArrayMap<>();

        TransitionMetricsReporter() {
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public void associate(IBinder iBinder, LongConsumer longConsumer) {
            synchronized (this.mMetricConsumers) {
                this.mMetricConsumers.put(iBinder, longConsumer);
            }
        }

        public void reportAnimationStart(IBinder iBinder, long j) {
            synchronized (this.mMetricConsumers) {
                if (this.mMetricConsumers.isEmpty()) {
                    return;
                }
                LongConsumer remove = this.mMetricConsumers.remove(iBinder);
                if (remove != null) {
                    remove.accept(j);
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
    public class Lock {
        private int mTransitionWaiters = 0;

        Lock() {
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public void runWhenIdle(long j, Runnable runnable) {
            WindowManagerGlobalLock windowManagerGlobalLock = TransitionController.this.mAtm.mGlobalLock;
            WindowManagerService.boostPriorityForLockedSection();
            synchronized (windowManagerGlobalLock) {
                try {
                    if (!TransitionController.this.inTransition()) {
                        runnable.run();
                        return;
                    }
                    this.mTransitionWaiters++;
                    WindowManagerService.resetPriorityAfterLockedSection();
                    long uptimeMillis = SystemClock.uptimeMillis() + j;
                    while (true) {
                        WindowManagerGlobalLock windowManagerGlobalLock2 = TransitionController.this.mAtm.mGlobalLock;
                        WindowManagerService.boostPriorityForLockedSection();
                        synchronized (windowManagerGlobalLock2) {
                            try {
                                if (!TransitionController.this.inTransition() || SystemClock.uptimeMillis() > uptimeMillis) {
                                    break;
                                }
                            } finally {
                                WindowManagerService.resetPriorityAfterLockedSection();
                            }
                        }
                        synchronized (this) {
                            try {
                                try {
                                    wait(j);
                                } finally {
                                }
                            } catch (InterruptedException unused) {
                                return;
                            }
                        }
                    }
                    this.mTransitionWaiters--;
                    runnable.run();
                    WindowManagerService.resetPriorityAfterLockedSection();
                } finally {
                    WindowManagerService.resetPriorityAfterLockedSection();
                }
            }
        }

        void doNotifyLocked() {
            synchronized (this) {
                if (this.mTransitionWaiters > 0) {
                    notifyAll();
                }
            }
        }
    }

    public ITransitionControllerWrapper getWrapper() {
        return this.mWrapper;
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
    private class TransitionControllerWrapper implements ITransitionControllerWrapper {
        private TransitionControllerWrapper() {
        }

        @Override // com.android.server.wm.ITransitionControllerWrapper
        public ArrayList<Transition> getPlayingTransitions() {
            return TransitionController.this.mPlayingTransitions;
        }
    }
}
