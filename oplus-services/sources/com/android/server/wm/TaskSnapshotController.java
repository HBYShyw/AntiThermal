package com.android.server.wm;

import android.R;
import android.app.ActivityManager;
import android.graphics.Rect;
import android.os.Handler;
import android.os.SystemProperties;
import android.util.ArraySet;
import android.util.IntArray;
import android.util.Slog;
import android.view.SurfaceControl;
import android.window.ScreenCapture;
import android.window.TaskSnapshot;
import com.android.internal.annotations.VisibleForTesting;
import com.android.server.policy.WindowManagerPolicy;
import com.android.server.wm.BaseAppSnapshotPersister;
import com.android.server.wm.SnapshotController;
import java.util.Iterator;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Predicate;
import system.ext.loader.core.ExtLoader;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
public class TaskSnapshotController extends AbsAppSnapshotController<Task, TaskSnapshotCache> {
    private static final boolean CROP_TASKSNAPSHOT_ENABLE_VALUE = SystemProperties.getBoolean("persist.sys.crop_task_snapshot_enable", false);
    static final String SNAPSHOTS_DIRNAME = "snapshots";
    private final Handler mHandler;
    private final BaseAppSnapshotPersister.PersistInfoProvider mPersistInfoProvider;
    private final TaskSnapshotPersister mPersister;
    private final IntArray mSkipClosingAppSnapshotTasks;
    public ITaskSnapshotControllerExt mTaskSnapConExt;
    private final ArraySet<Task> mTmpTasks;

    /* JADX INFO: Access modifiers changed from: package-private */
    public TaskSnapshotController(WindowManagerService windowManagerService, SnapshotPersistQueue snapshotPersistQueue) {
        super(windowManagerService);
        this.mTaskSnapConExt = (ITaskSnapshotControllerExt) ExtLoader.type(ITaskSnapshotControllerExt.class).base(this).create();
        this.mSkipClosingAppSnapshotTasks = new IntArray();
        this.mTmpTasks = new ArraySet<>();
        this.mHandler = new Handler();
        BaseAppSnapshotPersister.PersistInfoProvider createPersistInfoProvider = createPersistInfoProvider(windowManagerService, new ActivitySnapshotController$$ExternalSyntheticLambda0());
        this.mPersistInfoProvider = createPersistInfoProvider;
        this.mPersister = new TaskSnapshotPersister(snapshotPersistQueue, createPersistInfoProvider);
        initialize(new TaskSnapshotCache(windowManagerService, new AppSnapshotLoader(createPersistInfoProvider)));
        setSnapshotEnabled(!windowManagerService.mContext.getResources().getBoolean(R.bool.use_lock_pattern_drawable));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void systemReady() {
        if (shouldDisableSnapshots()) {
            return;
        }
        this.mService.mSnapshotController.registerTransitionStateConsumer(8, new Consumer() { // from class: com.android.server.wm.TaskSnapshotController$$ExternalSyntheticLambda1
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                TaskSnapshotController.this.handleTaskClose((SnapshotController.TransitionState) obj);
            }
        });
    }

    static BaseAppSnapshotPersister.PersistInfoProvider createPersistInfoProvider(WindowManagerService windowManagerService, BaseAppSnapshotPersister.DirectoryResolver directoryResolver) {
        boolean z;
        float f = windowManagerService.mContext.getResources().getFloat(R.dimen.config_screenBrightnessSettingForVrMinimumFloat);
        float f2 = windowManagerService.mContext.getResources().getFloat(R.dimen.config_viewConfigurationTouchSlop);
        float f3 = 0.0f;
        if (f2 < 0.0f || 1.0f <= f2) {
            throw new RuntimeException("Low-res scale must be between 0 and 1");
        }
        if (f <= 0.0f || 1.0f < f) {
            throw new RuntimeException("High-res scale must be between 0 and 1");
        }
        if (f <= f2) {
            throw new RuntimeException("High-res scale must be greater than low-res scale");
        }
        if (f2 > 0.0f) {
            f3 = f2 / f;
            z = true;
        } else {
            z = false;
        }
        return new BaseAppSnapshotPersister.PersistInfoProvider(directoryResolver, SNAPSHOTS_DIRNAME, z, f3, windowManagerService.mContext.getResources().getBoolean(17891879));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void handleTaskClose(SnapshotController.TransitionState<Task> transitionState) {
        if (shouldDisableSnapshots()) {
            return;
        }
        this.mTmpTasks.clear();
        ArraySet<Task> participant = transitionState.getParticipant(false);
        if (this.mService.mAtmService.getTransitionController().isShellTransitionsEnabled()) {
            this.mTmpTasks.addAll((ArraySet<? extends Task>) participant);
        } else {
            Iterator<Task> it = participant.iterator();
            while (it.hasNext()) {
                getClosingTasksInner(it.next(), this.mTmpTasks);
            }
        }
        snapshotTasks(this.mTmpTasks);
        this.mSkipClosingAppSnapshotTasks.clear();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @VisibleForTesting
    public void addSkipClosingAppSnapshotTasks(Set<Task> set) {
        if (shouldDisableSnapshots()) {
            return;
        }
        Iterator<Task> it = set.iterator();
        while (it.hasNext()) {
            this.mSkipClosingAppSnapshotTasks.add(it.next().mTaskId);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void snapshotTasks(ArraySet<Task> arraySet) {
        snapshotTasks(arraySet, false);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public TaskSnapshot recordSnapshot(Task task, boolean z) {
        boolean z2 = z && task.isActivityTypeHome();
        TaskSnapshot recordSnapshotInner = recordSnapshotInner(task, z);
        if (!z2 && recordSnapshotInner != null) {
            this.mPersister.persistSnapshot(task.mTaskId, task.mUserId, recordSnapshotInner);
            task.onSnapshotChanged(recordSnapshotInner);
        }
        return recordSnapshotInner;
    }

    private void snapshotTasks(ArraySet<Task> arraySet, boolean z) {
        for (int size = arraySet.size() - 1; size >= 0; size--) {
            recordSnapshot(arraySet.valueAt(size), z);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public TaskSnapshot getSnapshot(int i, int i2, boolean z, boolean z2) {
        return ((TaskSnapshotCache) this.mCache).getSnapshot(i, i2, z, z2 && this.mPersistInfoProvider.enableLowResSnapshots());
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public long getSnapshotCaptureTime(int i) {
        TaskSnapshot snapshot = ((TaskSnapshotCache) this.mCache).getSnapshot(Integer.valueOf(i));
        if (snapshot != null) {
            return snapshot.getCaptureTime();
        }
        return -1L;
    }

    public void clearSnapshotCache() {
        ((TaskSnapshotCache) this.mCache).clearRunningCache();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.android.server.wm.AbsAppSnapshotController
    public ActivityRecord findAppTokenForSnapshot(Task task) {
        return task.getActivity(new Predicate() { // from class: com.android.server.wm.TaskSnapshotController$$ExternalSyntheticLambda3
            @Override // java.util.function.Predicate
            public final boolean test(Object obj) {
                return ((ActivityRecord) obj).canCaptureSnapshot();
            }
        });
    }

    @Override // com.android.server.wm.AbsAppSnapshotController
    protected boolean use16BitFormat() {
        return this.mPersistInfoProvider.use16BitFormat();
    }

    private ScreenCapture.ScreenshotHardwareBuffer createImeSnapshot(Task task, int i) {
        if (task.getSurfaceControl() == null) {
            if (WindowManagerDebugConfig.DEBUG_SCREENSHOT) {
                Slog.w("WindowManager", "Failed to take screenshot. No surface control for " + task);
            }
            return null;
        }
        WindowState windowState = task.getDisplayContent().mInputMethodWindow;
        if (windowState == null || !windowState.isVisible()) {
            return null;
        }
        Rect parentFrame = windowState.getParentFrame();
        parentFrame.offsetTo(0, 0);
        return ScreenCapture.captureLayersExcluding(windowState.getSurfaceControl(), parentFrame, 1.0f, i, (SurfaceControl[]) null);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public ScreenCapture.ScreenshotHardwareBuffer snapshotImeFromAttachedTask(Task task) {
        if (checkIfReadyToSnapshot(task) == null) {
            return null;
        }
        int i = 4;
        if (!CROP_TASKSNAPSHOT_ENABLE_VALUE && !this.mPersistInfoProvider.use16BitFormat()) {
            i = 1;
        }
        return createImeSnapshot(task, i);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.android.server.wm.AbsAppSnapshotController
    public ActivityRecord getTopActivity(Task task) {
        return task.getTopMostActivity();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.android.server.wm.AbsAppSnapshotController
    public ActivityRecord getTopFullscreenActivity(Task task) {
        return task.getTopFullscreenActivity();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.android.server.wm.AbsAppSnapshotController
    public ActivityManager.TaskDescription getTaskDescription(Task task) {
        return task.getTaskDescription();
    }

    @VisibleForTesting
    void getClosingTasks(ArraySet<ActivityRecord> arraySet, ArraySet<Task> arraySet2) {
        arraySet2.clear();
        for (int size = arraySet.size() - 1; size >= 0; size--) {
            ActivityRecord valueAt = arraySet.valueAt(size);
            Task task = valueAt.getTask();
            if (task != null) {
                if (this.mSkipClosingAppSnapshotTasks.indexOf(task.mTaskId) >= 0) {
                    this.mTaskSnapConExt.getClosingTasks(valueAt);
                }
                getClosingTasksInner(task, arraySet2);
            }
        }
    }

    void getClosingTasksInner(Task task, ArraySet<Task> arraySet) {
        if (isAnimatingByRecents(task)) {
            this.mSkipClosingAppSnapshotTasks.add(task.mTaskId);
        }
        if (task.isVisible() || this.mSkipClosingAppSnapshotTasks.indexOf(task.mTaskId) >= 0) {
            return;
        }
        arraySet.add(task);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void notifyTaskRemovedFromRecents(int i, int i2) {
        ((TaskSnapshotCache) this.mCache).onIdRemoved(Integer.valueOf(i));
        this.mPersister.onTaskRemovedFromRecents(i, i2);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void removeSnapshotCache(int i) {
        ((TaskSnapshotCache) this.mCache).removeRunningEntry(Integer.valueOf(i));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void removeObsoleteTaskFiles(ArraySet<Integer> arraySet, int[] iArr) {
        this.mPersister.removeObsoleteFiles(arraySet, iArr);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void screenTurningOff(final int i, final WindowManagerPolicy.ScreenOffListener screenOffListener) {
        if (shouldDisableSnapshots() || this.mTaskSnapConExt.shouldDisableSnapshots()) {
            screenOffListener.onScreenOff();
        } else {
            this.mHandler.post(new Runnable() { // from class: com.android.server.wm.TaskSnapshotController$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    TaskSnapshotController.this.lambda$screenTurningOff$0(i, screenOffListener);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$screenTurningOff$0(int i, WindowManagerPolicy.ScreenOffListener screenOffListener) {
        try {
            WindowManagerGlobalLock windowManagerGlobalLock = this.mService.mGlobalLock;
            WindowManagerService.boostPriorityForLockedSection();
            synchronized (windowManagerGlobalLock) {
                try {
                    snapshotForSleeping(i);
                } catch (Throwable th) {
                    WindowManagerService.resetPriorityAfterLockedSection();
                    throw th;
                }
            }
            WindowManagerService.resetPriorityAfterLockedSection();
        } finally {
            screenOffListener.onScreenOff();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void snapshotForSleeping(int i) {
        DisplayContent displayContent;
        boolean z;
        if (shouldDisableSnapshots()) {
            return;
        }
        WindowManagerService windowManagerService = this.mService;
        if (windowManagerService.mDisplayEnabled && (displayContent = windowManagerService.mRoot.getDisplayContent(i)) != null) {
            this.mTmpTasks.clear();
            displayContent.forAllTasks(new Consumer() { // from class: com.android.server.wm.TaskSnapshotController$$ExternalSyntheticLambda2
                @Override // java.util.function.Consumer
                public final void accept(Object obj) {
                    TaskSnapshotController.this.lambda$snapshotForSleeping$1((Task) obj);
                }
            });
            if (i == 0) {
                WindowManagerService windowManagerService2 = this.mService;
                if (windowManagerService2.mPolicy.isKeyguardSecure(windowManagerService2.mCurrentUserId)) {
                    z = true;
                    snapshotTasks(this.mTmpTasks, z);
                }
            }
            z = false;
            snapshotTasks(this.mTmpTasks, z);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$snapshotForSleeping$1(Task task) {
        if (!task.isVisible() || isAnimatingByRecents(task)) {
            return;
        }
        this.mTmpTasks.add(task);
    }
}
