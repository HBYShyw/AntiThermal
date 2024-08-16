package com.android.server.wm;

import android.app.ActivityManager;
import android.app.TaskInfo;
import android.app.WindowConfiguration;
import android.content.Intent;
import android.content.pm.ParceledListSlice;
import android.graphics.Rect;
import android.os.Binder;
import android.os.IBinder;
import android.os.Parcel;
import android.os.RemoteException;
import android.util.ArrayMap;
import android.util.Slog;
import android.util.proto.ProtoOutputStream;
import android.view.SurfaceControl;
import android.window.ITaskOrganizer;
import android.window.ITaskOrganizerController;
import android.window.IWindowlessStartingSurfaceCallback;
import android.window.StartingWindowInfo;
import android.window.StartingWindowRemovalInfo;
import android.window.TaskAppearedInfo;
import android.window.TaskSnapshot;
import android.window.WindowContainerToken;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.protolog.ProtoLogGroup;
import com.android.internal.protolog.ProtoLogImpl;
import com.android.internal.util.ArrayUtils;
import com.android.server.wm.SurfaceAnimator;
import com.android.server.wm.Task;
import com.android.server.wm.TaskOrganizerController;
import com.android.server.wm.utils.LogUtil;
import java.io.PrintWriter;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.WeakHashMap;
import java.util.function.Consumer;
import system.ext.loader.core.ExtLoader;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
public class TaskOrganizerController extends ITaskOrganizerController.Stub {
    private static final String TAG = "TaskOrganizerController";
    static ITaskOrganizerControllerExt mExt;
    private Consumer<Runnable> mDeferTaskOrgCallbacksConsumer;
    private final WindowManagerGlobalLock mGlobalLock;
    private final ActivityTaskManagerService mService;
    private final ArrayDeque<ITaskOrganizer> mTaskOrganizers = new ArrayDeque<>();
    private final ArrayMap<IBinder, TaskOrganizerState> mTaskOrganizerStates = new ArrayMap<>();
    private final HashSet<Integer> mInterceptBackPressedOnRootTasks = new HashSet<>();

    /* JADX INFO: Access modifiers changed from: package-private */
    @VisibleForTesting
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
    public class DeathRecipient implements IBinder.DeathRecipient {
        ITaskOrganizer mTaskOrganizer;

        DeathRecipient(ITaskOrganizer iTaskOrganizer) {
            this.mTaskOrganizer = iTaskOrganizer;
        }

        @Override // android.os.IBinder.DeathRecipient
        public void binderDied() {
            WindowManagerGlobalLock windowManagerGlobalLock = TaskOrganizerController.this.mGlobalLock;
            WindowManagerService.boostPriorityForLockedSection();
            synchronized (windowManagerGlobalLock) {
                try {
                    TaskOrganizerState taskOrganizerState = (TaskOrganizerState) TaskOrganizerController.this.mTaskOrganizerStates.get(this.mTaskOrganizer.asBinder());
                    if (taskOrganizerState != null) {
                        taskOrganizerState.dispose();
                    }
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
    public static class TaskOrganizerCallbacks {
        final Consumer<Runnable> mDeferTaskOrgCallbacksConsumer;
        final ITaskOrganizer mTaskOrganizer;

        TaskOrganizerCallbacks(ITaskOrganizer iTaskOrganizer, Consumer<Runnable> consumer) {
            this.mDeferTaskOrgCallbacksConsumer = consumer;
            this.mTaskOrganizer = iTaskOrganizer;
        }

        IBinder getBinder() {
            return this.mTaskOrganizer.asBinder();
        }

        SurfaceControl prepareLeash(Task task, String str) {
            return new SurfaceControl(task.getSurfaceControl(), str);
        }

        void onTaskAppeared(Task task) {
            if (ProtoLogCache.WM_DEBUG_WINDOW_ORGANIZER_enabled) {
                ProtoLogImpl.v(ProtoLogGroup.WM_DEBUG_WINDOW_ORGANIZER, 1918448345, 1, (String) null, new Object[]{Long.valueOf(task.mTaskId)});
            }
            ActivityManager.RunningTaskInfo taskInfo = task.getTaskInfo();
            try {
                LogUtil.d(TaskOrganizerController.TAG, "TaskOrganizerCallbacks.onTaskAppeared");
                this.mTaskOrganizer.onTaskAppeared(taskInfo, prepareLeash(task, "TaskOrganizerController.onTaskAppeared"));
                TaskOrganizerController.mExt.onTaskAppeared(task, taskInfo);
            } catch (RemoteException e) {
                Slog.e(TaskOrganizerController.TAG, "Exception sending onTaskAppeared callback", e);
            }
        }

        void onTaskVanished(Task task) {
            if (ProtoLogCache.WM_DEBUG_WINDOW_ORGANIZER_enabled) {
                ProtoLogImpl.v(ProtoLogGroup.WM_DEBUG_WINDOW_ORGANIZER, -1364754753, 1, (String) null, new Object[]{Long.valueOf(task.mTaskId)});
            }
            ActivityManager.RunningTaskInfo taskInfo = task.getTaskInfo();
            try {
                this.mTaskOrganizer.onTaskVanished(taskInfo);
                TaskOrganizerController.mExt.onTaskVanished(task, taskInfo);
            } catch (RemoteException e) {
                Slog.e(TaskOrganizerController.TAG, "Exception sending onTaskVanished callback", e);
            }
        }

        void onTaskInfoChanged(Task task, ActivityManager.RunningTaskInfo runningTaskInfo) {
            if (task.mTaskAppearedSent) {
                if (ProtoLogCache.WM_DEBUG_WINDOW_ORGANIZER_enabled) {
                    ProtoLogImpl.v(ProtoLogGroup.WM_DEBUG_WINDOW_ORGANIZER, 302969511, 1, (String) null, new Object[]{Long.valueOf(task.mTaskId)});
                }
                if (task.isOrganized()) {
                    try {
                        Slog.i(TaskOrganizerController.TAG, "onTaskInfoChanged taskid " + task.mTaskId + " config " + runningTaskInfo.configuration.windowConfiguration);
                        this.mTaskOrganizer.onTaskInfoChanged(runningTaskInfo);
                        TaskOrganizerController.mExt.onTaskInfoChanged(task, runningTaskInfo);
                    } catch (RemoteException e) {
                        Slog.e(TaskOrganizerController.TAG, "Exception sending onTaskInfoChanged callback", e);
                    }
                }
            }
        }

        void onBackPressedOnTaskRoot(Task task) {
            if (ProtoLogCache.WM_DEBUG_WINDOW_ORGANIZER_enabled) {
                ProtoLogImpl.v(ProtoLogGroup.WM_DEBUG_WINDOW_ORGANIZER, -2049725903, 1, (String) null, new Object[]{Long.valueOf(task.mTaskId)});
            }
            if (task.mTaskAppearedSent && task.isOrganized()) {
                try {
                    this.mTaskOrganizer.onBackPressedOnTaskRoot(task.getTaskInfo());
                    TaskOrganizerController.mExt.onBackPressedOnTaskRoot(task, task.getTaskInfo());
                } catch (Exception e) {
                    Slog.e(TaskOrganizerController.TAG, "Exception sending onBackPressedOnTaskRoot callback", e);
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
    public static final class TaskOrganizerPendingEventsQueue {
        private final TaskOrganizerState mOrganizerState;
        private ActivityManager.RunningTaskInfo mTmpTaskInfo;
        private final WeakHashMap<Task, ActivityManager.RunningTaskInfo> mLastSentTaskInfos = new WeakHashMap<>();
        private final ArrayList<PendingTaskEvent> mPendingTaskEvents = new ArrayList<>();

        TaskOrganizerPendingEventsQueue(TaskOrganizerState taskOrganizerState) {
            this.mOrganizerState = taskOrganizerState;
        }

        @VisibleForTesting
        public ArrayList<PendingTaskEvent> getPendingEventList() {
            return this.mPendingTaskEvents;
        }

        int numPendingTaskEvents() {
            return this.mPendingTaskEvents.size();
        }

        void clearPendingTaskEvents() {
            this.mPendingTaskEvents.clear();
        }

        void addPendingTaskEvent(PendingTaskEvent pendingTaskEvent) {
            this.mPendingTaskEvents.add(pendingTaskEvent);
        }

        void removePendingTaskEvent(PendingTaskEvent pendingTaskEvent) {
            this.mPendingTaskEvents.remove(pendingTaskEvent);
        }

        boolean removePendingTaskEvents(Task task) {
            boolean z = false;
            for (int size = this.mPendingTaskEvents.size() - 1; size >= 0; size--) {
                PendingTaskEvent pendingTaskEvent = this.mPendingTaskEvents.get(size);
                if (task.mTaskId == pendingTaskEvent.mTask.mTaskId) {
                    this.mPendingTaskEvents.remove(size);
                    if (pendingTaskEvent.mEventType == 0) {
                        z = true;
                    }
                }
            }
            return z;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public PendingTaskEvent getPendingTaskEvent(Task task, int i) {
            for (int size = this.mPendingTaskEvents.size() - 1; size >= 0; size--) {
                PendingTaskEvent pendingTaskEvent = this.mPendingTaskEvents.get(size);
                if (task.mTaskId == pendingTaskEvent.mTask.mTaskId && i == pendingTaskEvent.mEventType) {
                    return pendingTaskEvent;
                }
            }
            return null;
        }

        @VisibleForTesting
        PendingTaskEvent getPendingLifecycleTaskEvent(Task task) {
            for (int size = this.mPendingTaskEvents.size() - 1; size >= 0; size--) {
                PendingTaskEvent pendingTaskEvent = this.mPendingTaskEvents.get(size);
                if (task.mTaskId == pendingTaskEvent.mTask.mTaskId && pendingTaskEvent.isLifecycleEvent()) {
                    return pendingTaskEvent;
                }
            }
            return null;
        }

        void dispatchPendingEvents() {
            if (this.mPendingTaskEvents.isEmpty()) {
                return;
            }
            int size = this.mPendingTaskEvents.size();
            for (int i = 0; i < size; i++) {
                dispatchPendingEvent(this.mPendingTaskEvents.get(i));
            }
            this.mPendingTaskEvents.clear();
        }

        private void dispatchPendingEvent(PendingTaskEvent pendingTaskEvent) {
            Task task = pendingTaskEvent.mTask;
            int i = pendingTaskEvent.mEventType;
            if (i == 0) {
                if (task.taskAppearedReady()) {
                    this.mOrganizerState.mOrganizer.onTaskAppeared(task);
                }
            } else if (i == 1) {
                this.mOrganizerState.mOrganizer.onTaskVanished(task);
                TaskOrganizerController.mExt.onTaskVanished(task, task.getTaskInfo());
                this.mLastSentTaskInfos.remove(task);
            } else if (i == 2) {
                dispatchTaskInfoChanged(task, pendingTaskEvent.mForce);
            } else {
                if (i != 3) {
                    return;
                }
                this.mOrganizerState.mOrganizer.onBackPressedOnTaskRoot(task);
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void dispatchTaskInfoChanged(Task task, boolean z) {
            ActivityManager.RunningTaskInfo runningTaskInfo = this.mLastSentTaskInfos.get(task);
            if (this.mTmpTaskInfo == null) {
                this.mTmpTaskInfo = new ActivityManager.RunningTaskInfo();
            }
            this.mTmpTaskInfo.configuration.unset();
            task.fillTaskInfo(this.mTmpTaskInfo);
            boolean z2 = (this.mTmpTaskInfo.equalsForTaskOrganizer(runningTaskInfo) && WindowOrganizerController.configurationsAreEqualForOrganizer(this.mTmpTaskInfo.configuration, runningTaskInfo.configuration)) ? false : true;
            if (!z2) {
                z2 |= !TaskOrganizerController.mExt.sameTaskInfoForSplitScreen(this.mTmpTaskInfo, runningTaskInfo);
            }
            if (TaskOrganizerController.mExt.shouldDispatchTaskInfoChangedForEmbeddedTask(task, z2 || TaskOrganizerController.mExt.shouldDispatchTaskInfoChanged(this.mTmpTaskInfo, runningTaskInfo)) || z) {
                ActivityManager.RunningTaskInfo runningTaskInfo2 = this.mTmpTaskInfo;
                this.mLastSentTaskInfos.put(task, runningTaskInfo2);
                this.mTmpTaskInfo = null;
                if (task.isOrganized()) {
                    this.mOrganizerState.mOrganizer.onTaskInfoChanged(task, runningTaskInfo2);
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @VisibleForTesting
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
    public class TaskOrganizerState {
        private final DeathRecipient mDeathRecipient;
        private final ArrayList<Task> mOrganizedTasks = new ArrayList<>();
        private final TaskOrganizerCallbacks mOrganizer;
        private final TaskOrganizerPendingEventsQueue mPendingEventsQueue;
        private final int mUid;

        TaskOrganizerState(ITaskOrganizer iTaskOrganizer, int i) {
            Consumer consumer;
            if (TaskOrganizerController.this.mDeferTaskOrgCallbacksConsumer != null) {
                consumer = TaskOrganizerController.this.mDeferTaskOrgCallbacksConsumer;
            } else {
                final WindowAnimator windowAnimator = TaskOrganizerController.this.mService.mWindowManager.mAnimator;
                Objects.requireNonNull(windowAnimator);
                consumer = new Consumer() { // from class: com.android.server.wm.TaskOrganizerController$TaskOrganizerState$$ExternalSyntheticLambda0
                    @Override // java.util.function.Consumer
                    public final void accept(Object obj) {
                        WindowAnimator.this.addAfterPrepareSurfacesRunnable((Runnable) obj);
                    }
                };
            }
            this.mOrganizer = new TaskOrganizerCallbacks(iTaskOrganizer, consumer);
            DeathRecipient deathRecipient = new DeathRecipient(iTaskOrganizer);
            this.mDeathRecipient = deathRecipient;
            this.mPendingEventsQueue = new TaskOrganizerPendingEventsQueue(this);
            try {
                iTaskOrganizer.asBinder().linkToDeath(deathRecipient, 0);
            } catch (RemoteException unused) {
                Slog.e(TaskOrganizerController.TAG, "TaskOrganizer failed to register death recipient");
            }
            this.mUid = i;
        }

        @VisibleForTesting
        DeathRecipient getDeathRecipient() {
            return this.mDeathRecipient;
        }

        @VisibleForTesting
        TaskOrganizerPendingEventsQueue getPendingEventsQueue() {
            return this.mPendingEventsQueue;
        }

        SurfaceControl addTaskWithoutCallback(Task task, String str) {
            task.mTaskAppearedSent = true;
            if (!this.mOrganizedTasks.contains(task)) {
                this.mOrganizedTasks.add(task);
            }
            return this.mOrganizer.prepareLeash(task, str);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public boolean addTask(Task task) {
            if (task.mTaskAppearedSent) {
                return false;
            }
            if (!this.mOrganizedTasks.contains(task)) {
                this.mOrganizedTasks.add(task);
            }
            if (!task.taskAppearedReady()) {
                return false;
            }
            task.mTaskAppearedSent = true;
            return true;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public boolean removeTask(Task task, boolean z) {
            this.mOrganizedTasks.remove(task);
            TaskOrganizerController.this.mInterceptBackPressedOnRootTasks.remove(Integer.valueOf(task.mTaskId));
            boolean z2 = task.mTaskAppearedSent;
            if (z2) {
                if (task.getSurfaceControl() != null) {
                    task.migrateToNewSurfaceControl(task.getPendingTransaction());
                }
                task.mTaskAppearedSent = false;
            }
            if (z) {
                TaskOrganizerController.this.mService.removeTask(task.mTaskId);
            }
            return z2;
        }

        void dispose() {
            TaskOrganizerController.this.mTaskOrganizers.remove(this.mOrganizer.mTaskOrganizer);
            while (!this.mOrganizedTasks.isEmpty()) {
                Task task = this.mOrganizedTasks.get(0);
                if (task.mCreatedByOrganizer) {
                    task.removeImmediately();
                } else {
                    task.updateTaskOrganizerState();
                }
                if (this.mOrganizedTasks.contains(task) && removeTask(task, task.mRemoveWithTaskOrganizer)) {
                    TaskOrganizerController.this.onTaskVanishedInternal(this, task);
                }
                if (TaskOrganizerController.this.mService.getTransitionController().isShellTransitionsEnabled() && task.mTaskOrganizer != null && task.getSurfaceControl() != null) {
                    task.getSyncTransaction().show(task.getSurfaceControl());
                }
            }
            this.mPendingEventsQueue.clearPendingTaskEvents();
            TaskOrganizerController.this.mTaskOrganizerStates.remove(this.mOrganizer.getBinder());
        }

        void unlinkDeath() {
            this.mOrganizer.getBinder().unlinkToDeath(this.mDeathRecipient, 0);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
    public static class PendingTaskEvent {
        static final int EVENT_APPEARED = 0;
        static final int EVENT_INFO_CHANGED = 2;
        static final int EVENT_ROOT_BACK_PRESSED = 3;
        static final int EVENT_VANISHED = 1;
        final int mEventType;
        boolean mForce;
        final Task mTask;
        final ITaskOrganizer mTaskOrg;

        PendingTaskEvent(Task task, int i) {
            this(task, task.mTaskOrganizer, i);
        }

        PendingTaskEvent(Task task, ITaskOrganizer iTaskOrganizer, int i) {
            this.mTask = task;
            this.mTaskOrg = iTaskOrganizer;
            this.mEventType = i;
        }

        boolean isLifecycleEvent() {
            int i = this.mEventType;
            return i == 0 || i == 1 || i == 2;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public TaskOrganizerController(ActivityTaskManagerService activityTaskManagerService) {
        this.mService = activityTaskManagerService;
        this.mGlobalLock = activityTaskManagerService.mGlobalLock;
        mExt = (ITaskOrganizerControllerExt) ExtLoader.type(ITaskOrganizerControllerExt.class).base(this).create();
    }

    public boolean onTransact(int i, Parcel parcel, Parcel parcel2, int i2) throws RemoteException {
        try {
            return super.onTransact(i, parcel, parcel2, i2);
        } catch (RuntimeException e) {
            throw ActivityTaskManagerService.logAndRethrowRuntimeExceptionOnTransact(TAG, e);
        }
    }

    @VisibleForTesting
    public void setDeferTaskOrgCallbacksConsumer(Consumer<Runnable> consumer) {
        this.mDeferTaskOrgCallbacksConsumer = consumer;
    }

    public ParceledListSlice<TaskAppearedInfo> registerTaskOrganizer(final ITaskOrganizer iTaskOrganizer) {
        ActivityTaskManagerService.enforceTaskPermission("registerTaskOrganizer()");
        final int callingUid = Binder.getCallingUid();
        long clearCallingIdentity = Binder.clearCallingIdentity();
        try {
            final ArrayList arrayList = new ArrayList();
            Runnable runnable = new Runnable() { // from class: com.android.server.wm.TaskOrganizerController$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    TaskOrganizerController.this.lambda$registerTaskOrganizer$1(iTaskOrganizer, callingUid, arrayList);
                }
            };
            if (this.mService.getTransitionController().isShellTransitionsEnabled()) {
                this.mService.getTransitionController().mRunningLock.runWhenIdle(1000L, runnable);
            } else {
                WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
                WindowManagerService.boostPriorityForLockedSection();
                synchronized (windowManagerGlobalLock) {
                    try {
                        runnable.run();
                    } catch (Throwable th) {
                        WindowManagerService.resetPriorityAfterLockedSection();
                        throw th;
                    }
                }
                WindowManagerService.resetPriorityAfterLockedSection();
            }
            return new ParceledListSlice<>(arrayList);
        } finally {
            Binder.restoreCallingIdentity(clearCallingIdentity);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$registerTaskOrganizer$1(ITaskOrganizer iTaskOrganizer, int i, final ArrayList arrayList) {
        if (ProtoLogCache.WM_DEBUG_WINDOW_ORGANIZER_enabled) {
            ProtoLogImpl.v(ProtoLogGroup.WM_DEBUG_WINDOW_ORGANIZER, -1792633344, 4, (String) null, new Object[]{String.valueOf(iTaskOrganizer.asBinder()), Long.valueOf(i)});
        }
        if (!this.mTaskOrganizerStates.containsKey(iTaskOrganizer.asBinder())) {
            this.mTaskOrganizers.add(iTaskOrganizer);
            this.mTaskOrganizerStates.put(iTaskOrganizer.asBinder(), new TaskOrganizerState(iTaskOrganizer, i));
        }
        final TaskOrganizerState taskOrganizerState = this.mTaskOrganizerStates.get(iTaskOrganizer.asBinder());
        this.mService.mRootWindowContainer.forAllTasks(new Consumer() { // from class: com.android.server.wm.TaskOrganizerController$$ExternalSyntheticLambda1
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                TaskOrganizerController.lambda$registerTaskOrganizer$0(TaskOrganizerController.TaskOrganizerState.this, arrayList, (Task) obj);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$registerTaskOrganizer$0(TaskOrganizerState taskOrganizerState, ArrayList arrayList, Task task) {
        boolean z = !task.mCreatedByOrganizer;
        task.updateTaskOrganizerState(z);
        if (task.isOrganized() && z && task.getSurfaceControl() != null && task.getSurfaceControl().isValid()) {
            arrayList.add(new TaskAppearedInfo(task.getTaskInfo(), taskOrganizerState.addTaskWithoutCallback(task, "TaskOrganizerController.registerTaskOrganizer")));
        }
    }

    public void unregisterTaskOrganizer(final ITaskOrganizer iTaskOrganizer) {
        ActivityTaskManagerService.enforceTaskPermission("unregisterTaskOrganizer()");
        final int callingUid = Binder.getCallingUid();
        long clearCallingIdentity = Binder.clearCallingIdentity();
        try {
            Runnable runnable = new Runnable() { // from class: com.android.server.wm.TaskOrganizerController$$ExternalSyntheticLambda2
                @Override // java.lang.Runnable
                public final void run() {
                    TaskOrganizerController.this.lambda$unregisterTaskOrganizer$2(iTaskOrganizer, callingUid);
                }
            };
            if (this.mService.getTransitionController().isShellTransitionsEnabled()) {
                this.mService.getTransitionController().mRunningLock.runWhenIdle(1000L, runnable);
            } else {
                WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
                WindowManagerService.boostPriorityForLockedSection();
                synchronized (windowManagerGlobalLock) {
                    try {
                        runnable.run();
                    } catch (Throwable th) {
                        WindowManagerService.resetPriorityAfterLockedSection();
                        throw th;
                    }
                }
                WindowManagerService.resetPriorityAfterLockedSection();
            }
        } finally {
            Binder.restoreCallingIdentity(clearCallingIdentity);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$unregisterTaskOrganizer$2(ITaskOrganizer iTaskOrganizer, int i) {
        TaskOrganizerState taskOrganizerState = this.mTaskOrganizerStates.get(iTaskOrganizer.asBinder());
        if (taskOrganizerState == null) {
            return;
        }
        if (ProtoLogCache.WM_DEBUG_WINDOW_ORGANIZER_enabled) {
            ProtoLogImpl.v(ProtoLogGroup.WM_DEBUG_WINDOW_ORGANIZER, -951939129, 4, (String) null, new Object[]{String.valueOf(iTaskOrganizer.asBinder()), Long.valueOf(i)});
        }
        taskOrganizerState.unlinkDeath();
        taskOrganizerState.dispose();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public ITaskOrganizer getTaskOrganizer() {
        return this.mTaskOrganizers.peekLast();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
    public static class StartingWindowAnimationAdaptor implements AnimationAdapter {
        SurfaceControl mAnimationLeash;

        @Override // com.android.server.wm.AnimationAdapter
        public void dumpDebug(ProtoOutputStream protoOutputStream) {
        }

        @Override // com.android.server.wm.AnimationAdapter
        public long getDurationHint() {
            return 0L;
        }

        @Override // com.android.server.wm.AnimationAdapter
        public boolean getShowWallpaper() {
            return false;
        }

        @Override // com.android.server.wm.AnimationAdapter
        public long getStatusBarTransitionsStartTime() {
            return 0L;
        }

        StartingWindowAnimationAdaptor() {
        }

        @Override // com.android.server.wm.AnimationAdapter
        public void startAnimation(SurfaceControl surfaceControl, SurfaceControl.Transaction transaction, int i, SurfaceAnimator.OnAnimationFinishedCallback onAnimationFinishedCallback) {
            this.mAnimationLeash = surfaceControl;
        }

        @Override // com.android.server.wm.AnimationAdapter
        public void onAnimationCancelled(SurfaceControl surfaceControl) {
            if (this.mAnimationLeash == surfaceControl) {
                this.mAnimationLeash = null;
            }
        }

        @Override // com.android.server.wm.AnimationAdapter
        public void dump(PrintWriter printWriter, String str) {
            printWriter.print(str + "StartingWindowAnimationAdaptor mCapturedLeash=");
            printWriter.print(this.mAnimationLeash);
            printWriter.println();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static SurfaceControl applyStartingWindowAnimation(WindowState windowState) {
        SurfaceControl.Transaction pendingTransaction = windowState.getPendingTransaction();
        Rect relativeFrame = windowState.getRelativeFrame();
        StartingWindowAnimationAdaptor startingWindowAnimationAdaptor = new StartingWindowAnimationAdaptor();
        windowState.startAnimation(pendingTransaction, startingWindowAnimationAdaptor, false, 128);
        SurfaceControl surfaceControl = startingWindowAnimationAdaptor.mAnimationLeash;
        if (surfaceControl == null) {
            Slog.e(TAG, "Cannot start starting window animation, the window " + windowState + " was removed");
            return null;
        }
        if (surfaceControl == null || !surfaceControl.isValid()) {
            Slog.e(TAG, "Cannot start starting window animation, the window " + windowState + "was removed");
            return null;
        }
        pendingTransaction.setPosition(startingWindowAnimationAdaptor.mAnimationLeash, relativeFrame.left, relativeFrame.top);
        if (windowState.getWrapper().getExtImpl().adjustPosForComapctWindow(windowState)) {
            pendingTransaction.setPosition(startingWindowAnimationAdaptor.mAnimationLeash, 0.0f, relativeFrame.top);
        }
        return startingWindowAnimationAdaptor.mAnimationLeash;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean addStartingWindow(Task task, ActivityRecord activityRecord, int i, TaskSnapshot taskSnapshot) {
        ITaskOrganizer peekLast;
        if (task.getRootTask() == null || activityRecord.mStartingData == null || (peekLast = this.mTaskOrganizers.peekLast()) == null) {
            return false;
        }
        StartingWindowInfo startingWindowInfo = task.getStartingWindowInfo(activityRecord);
        if (i != 0) {
            startingWindowInfo.splashScreenThemeResId = i;
        }
        startingWindowInfo.taskSnapshot = taskSnapshot;
        mExt.hookAddStartingWindow(activityRecord, startingWindowInfo);
        startingWindowInfo.appToken = activityRecord.token;
        try {
            mExt.hookSetBinderUxFlag(true);
            peekLast.addStartingWindow(startingWindowInfo);
            mExt.hookSetBinderUxFlag(false);
            return true;
        } catch (RemoteException e) {
            mExt.hookSetBinderUxFlag(false);
            Slog.e(TAG, "Exception sending onTaskStart callback", e);
            return false;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void removeStartingWindow(Task task, boolean z) {
        ITaskOrganizer peekLast;
        if (task.getRootTask() == null || (peekLast = this.mTaskOrganizers.peekLast()) == null) {
            return;
        }
        StartingWindowRemovalInfo startingWindowRemovalInfo = new StartingWindowRemovalInfo();
        startingWindowRemovalInfo.taskId = task.mTaskId;
        startingWindowRemovalInfo.playRevealAnimation = z && task.getDisplayContent() != null && task.getDisplayInfo().state == 2;
        boolean z2 = !task.inMultiWindowMode();
        ActivityRecord activityRecord = task.topActivityContainsStartingWindow();
        if (activityRecord != null) {
            DisplayContent displayContent = activityRecord.getDisplayContent();
            WindowState windowState = displayContent.mInputMethodWindow;
            if (activityRecord.isVisibleRequested() && windowState != null && !windowState.isVisible() && displayContent.mayImeShowOnLaunchingActivity(activityRecord) && displayContent.isFixedRotationLaunchingApp(activityRecord)) {
                startingWindowRemovalInfo.deferRemoveForImeMode = 2;
            } else if (displayContent.mayImeShowOnLaunchingActivity(activityRecord)) {
                startingWindowRemovalInfo.deferRemoveForImeMode = 1;
            } else {
                startingWindowRemovalInfo.deferRemoveForImeMode = 0;
            }
            WindowState findMainWindow = activityRecord.findMainWindow(false);
            if (findMainWindow == null || findMainWindow.mRemoved) {
                startingWindowRemovalInfo.playRevealAnimation = false;
            } else if (startingWindowRemovalInfo.playRevealAnimation && z2 && mExt.playShiftUpAnimation(activityRecord)) {
                startingWindowRemovalInfo.roundedCornerRadius = activityRecord.mLetterboxUiController.getRoundedCornersRadius(findMainWindow);
                startingWindowRemovalInfo.windowAnimationLeash = applyStartingWindowAnimation(findMainWindow);
                startingWindowRemovalInfo.mainFrame = findMainWindow.getRelativeFrame();
                if (findMainWindow.getWrapper().getExtImpl().adjustPosForComapctWindow(findMainWindow)) {
                    startingWindowRemovalInfo.mainFrame.left = 0;
                }
            }
        }
        try {
            peekLast.removeStartingWindow(startingWindowRemovalInfo);
        } catch (RemoteException e) {
            Slog.e(TAG, "Exception sending onStartTaskFinished callback", e);
        }
    }

    int addWindowlessStartingSurface(Task task, ActivityRecord activityRecord, SurfaceControl surfaceControl, TaskSnapshot taskSnapshot, IWindowlessStartingSurfaceCallback iWindowlessStartingSurfaceCallback) {
        ITaskOrganizer peekLast;
        if (task.getRootTask() == null || (peekLast = this.mTaskOrganizers.peekLast()) == null) {
            return -1;
        }
        StartingWindowInfo startingWindowInfo = task.getStartingWindowInfo(activityRecord);
        startingWindowInfo.taskInfo.taskDescription = activityRecord.taskDescription;
        startingWindowInfo.taskSnapshot = taskSnapshot;
        startingWindowInfo.windowlessStartingSurfaceCallback = iWindowlessStartingSurfaceCallback;
        startingWindowInfo.rootSurface = surfaceControl;
        try {
            peekLast.addStartingWindow(startingWindowInfo);
            return task.mTaskId;
        } catch (RemoteException e) {
            Slog.e(TAG, "Exception sending addWindowlessStartingSurface ", e);
            return -1;
        }
    }

    void removeWindowlessStartingSurface(int i, boolean z) {
        ITaskOrganizer peekLast = this.mTaskOrganizers.peekLast();
        if (peekLast == null || i == 0) {
            return;
        }
        StartingWindowRemovalInfo startingWindowRemovalInfo = new StartingWindowRemovalInfo();
        startingWindowRemovalInfo.taskId = i;
        startingWindowRemovalInfo.windowlessSurface = true;
        startingWindowRemovalInfo.removeImmediately = z;
        try {
            peekLast.removeStartingWindow(startingWindowRemovalInfo);
        } catch (RemoteException e) {
            Slog.e(TAG, "Exception sending removeWindowlessStartingSurface ", e);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean copySplashScreenView(Task task) {
        ITaskOrganizer peekLast;
        if (task.getRootTask() == null || (peekLast = this.mTaskOrganizers.peekLast()) == null) {
            return false;
        }
        try {
            peekLast.copySplashScreenView(task.mTaskId);
            return true;
        } catch (RemoteException e) {
            Slog.e(TAG, "Exception sending copyStartingWindowView callback", e);
            return false;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean isSupportWindowlessStartingSurface() {
        this.mTaskOrganizers.peekLast();
        return false;
    }

    public void onAppSplashScreenViewRemoved(Task task) {
        ITaskOrganizer peekLast;
        if (task.getRootTask() == null || (peekLast = this.mTaskOrganizers.peekLast()) == null) {
            return;
        }
        try {
            peekLast.onAppSplashScreenViewRemoved(task.mTaskId);
        } catch (RemoteException e) {
            Slog.e(TAG, "Exception sending onAppSplashScreenViewRemoved callback", e);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void onTaskAppeared(ITaskOrganizer iTaskOrganizer, Task task) {
        TaskOrganizerState taskOrganizerState = this.mTaskOrganizerStates.get(iTaskOrganizer.asBinder());
        if (taskOrganizerState == null || !taskOrganizerState.addTask(task)) {
            return;
        }
        TaskOrganizerPendingEventsQueue taskOrganizerPendingEventsQueue = taskOrganizerState.mPendingEventsQueue;
        if (taskOrganizerPendingEventsQueue.getPendingTaskEvent(task, 0) == null) {
            taskOrganizerPendingEventsQueue.addPendingTaskEvent(new PendingTaskEvent(task, 0));
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void onTaskVanished(ITaskOrganizer iTaskOrganizer, Task task) {
        TaskOrganizerState taskOrganizerState = this.mTaskOrganizerStates.get(iTaskOrganizer.asBinder());
        if (taskOrganizerState == null || !taskOrganizerState.removeTask(task, task.mRemoveWithTaskOrganizer)) {
            return;
        }
        onTaskVanishedInternal(taskOrganizerState, task);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onTaskVanishedInternal(TaskOrganizerState taskOrganizerState, Task task) {
        if (taskOrganizerState == null) {
            Slog.i(TAG, "cannot send onTaskVanished because organizer state is not present for this organizer");
            return;
        }
        TaskOrganizerPendingEventsQueue taskOrganizerPendingEventsQueue = taskOrganizerState.mPendingEventsQueue;
        if (taskOrganizerPendingEventsQueue.removePendingTaskEvents(task)) {
            return;
        }
        taskOrganizerPendingEventsQueue.addPendingTaskEvent(new PendingTaskEvent(task, taskOrganizerState.mOrganizer.mTaskOrganizer, 1));
    }

    public void createRootTask(int i, int i2, IBinder iBinder, boolean z) {
        ActivityTaskManagerService.enforceTaskPermission("createRootTask()");
        long clearCallingIdentity = Binder.clearCallingIdentity();
        try {
            WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
            WindowManagerService.boostPriorityForLockedSection();
            synchronized (windowManagerGlobalLock) {
                try {
                    DisplayContent displayContent = this.mService.mRootWindowContainer.getDisplayContent(i);
                    if (displayContent == null) {
                        if (ProtoLogCache.WM_DEBUG_WINDOW_ORGANIZER_enabled) {
                            ProtoLogImpl.e(ProtoLogGroup.WM_DEBUG_WINDOW_ORGANIZER, 1396893178, 1, (String) null, new Object[]{Long.valueOf(i)});
                        }
                        WindowManagerService.resetPriorityAfterLockedSection();
                    } else {
                        createRootTask(displayContent, i2, iBinder, z);
                        WindowManagerService.resetPriorityAfterLockedSection();
                    }
                } catch (Throwable th) {
                    WindowManagerService.resetPriorityAfterLockedSection();
                    throw th;
                }
            }
        } finally {
            Binder.restoreCallingIdentity(clearCallingIdentity);
        }
    }

    @VisibleForTesting
    Task createRootTask(DisplayContent displayContent, int i, IBinder iBinder) {
        return createRootTask(displayContent, i, iBinder, false);
    }

    Task createRootTask(DisplayContent displayContent, int i, IBinder iBinder, boolean z) {
        if (ProtoLogCache.WM_DEBUG_WINDOW_ORGANIZER_enabled) {
            ProtoLogImpl.v(ProtoLogGroup.WM_DEBUG_WINDOW_ORGANIZER, -1939861963, 5, (String) null, new Object[]{Long.valueOf(displayContent.mDisplayId), Long.valueOf(i)});
        }
        Task build = new Task.Builder(this.mService).setWindowingMode(i).setIntent(new Intent()).setCreatedByOrganizer(true).setDeferTaskAppear(true).setLaunchCookie(iBinder).setParent(displayContent.getDefaultTaskDisplayArea()).setRemoveWithTaskOrganizer(z).build();
        build.setDeferTaskAppear(false);
        return build;
    }

    /* JADX WARN: Finally extract failed */
    public boolean deleteRootTask(WindowContainerToken windowContainerToken) {
        ActivityTaskManagerService.enforceTaskPermission("deleteRootTask()");
        long clearCallingIdentity = Binder.clearCallingIdentity();
        try {
            WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
            WindowManagerService.boostPriorityForLockedSection();
            synchronized (windowManagerGlobalLock) {
                try {
                    WindowContainer fromBinder = WindowContainer.fromBinder(windowContainerToken.asBinder());
                    if (fromBinder != null) {
                        Task asTask = fromBinder.asTask();
                        if (asTask != null) {
                            if (!asTask.mCreatedByOrganizer) {
                                throw new IllegalArgumentException("Attempt to delete task not created by organizer task=" + asTask);
                            }
                            if (ProtoLogCache.WM_DEBUG_WINDOW_ORGANIZER_enabled) {
                                ProtoLogImpl.v(ProtoLogGroup.WM_DEBUG_WINDOW_ORGANIZER, -1895337367, 5, (String) null, new Object[]{Long.valueOf(asTask.getDisplayId()), Long.valueOf(asTask.getWindowingMode())});
                            }
                            asTask.remove(true, "deleteRootTask");
                            WindowManagerService.resetPriorityAfterLockedSection();
                            return true;
                        }
                    }
                    WindowManagerService.resetPriorityAfterLockedSection();
                    return false;
                } catch (Throwable th) {
                    WindowManagerService.resetPriorityAfterLockedSection();
                    throw th;
                }
            }
        } finally {
            Binder.restoreCallingIdentity(clearCallingIdentity);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void dispatchPendingEvents() {
        if (this.mService.mWindowManager.mWindowPlacerLocked.isLayoutDeferred()) {
            return;
        }
        for (int i = 0; i < this.mTaskOrganizerStates.size(); i++) {
            this.mTaskOrganizerStates.valueAt(i).mPendingEventsQueue.dispatchPendingEvents();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void reportImeDrawnOnTask(Task task) {
        TaskOrganizerState taskOrganizerState = this.mTaskOrganizerStates.get(task.mTaskOrganizer.asBinder());
        if (taskOrganizerState != null) {
            try {
                taskOrganizerState.mOrganizer.mTaskOrganizer.onImeDrawnOnTask(task.mTaskId);
            } catch (RemoteException e) {
                Slog.e(TAG, "Exception sending onImeDrawnOnTask callback", e);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void onTaskInfoChanged(Task task, boolean z) {
        if (task.mTaskAppearedSent) {
            TaskOrganizerState taskOrganizerState = this.mTaskOrganizerStates.get(task.mTaskOrganizer.asBinder());
            if (taskOrganizerState == null) {
                Slog.i(TAG, "cannot send onTaskInfoChanged because task organizer state is not present for this organizer");
                return;
            }
            TaskOrganizerPendingEventsQueue taskOrganizerPendingEventsQueue = taskOrganizerState.mPendingEventsQueue;
            if (taskOrganizerPendingEventsQueue == null) {
                Slog.i(TAG, "cannot send onTaskInfoChanged because pending events queue is not present for this organizer");
                return;
            }
            if (z && taskOrganizerPendingEventsQueue.numPendingTaskEvents() == 0) {
                taskOrganizerPendingEventsQueue.dispatchTaskInfoChanged(task, true);
                return;
            }
            PendingTaskEvent pendingLifecycleTaskEvent = taskOrganizerPendingEventsQueue.getPendingLifecycleTaskEvent(task);
            if (pendingLifecycleTaskEvent == null) {
                pendingLifecycleTaskEvent = new PendingTaskEvent(task, 2);
            } else if (pendingLifecycleTaskEvent.mEventType != 2) {
                return;
            } else {
                taskOrganizerPendingEventsQueue.removePendingTaskEvent(pendingLifecycleTaskEvent);
            }
            pendingLifecycleTaskEvent.mForce |= z;
            taskOrganizerPendingEventsQueue.addPendingTaskEvent(pendingLifecycleTaskEvent);
        }
    }

    /* JADX WARN: Finally extract failed */
    public WindowContainerToken getImeTarget(int i) {
        ActivityTaskManagerService.enforceTaskPermission("getImeTarget()");
        long clearCallingIdentity = Binder.clearCallingIdentity();
        try {
            WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
            WindowManagerService.boostPriorityForLockedSection();
            synchronized (windowManagerGlobalLock) {
                try {
                    DisplayContent displayContent = this.mService.mWindowManager.mRoot.getDisplayContent(i);
                    if (displayContent != null) {
                        InsetsControlTarget imeTarget = displayContent.getImeTarget(0);
                        if (imeTarget != null && imeTarget.getWindow() != null) {
                            Task task = imeTarget.getWindow().getTask();
                            if (task != null) {
                                WindowContainerToken windowContainerToken = task.mRemoteToken.toWindowContainerToken();
                                WindowManagerService.resetPriorityAfterLockedSection();
                                return windowContainerToken;
                            }
                        }
                    }
                    WindowManagerService.resetPriorityAfterLockedSection();
                    return null;
                } catch (Throwable th) {
                    WindowManagerService.resetPriorityAfterLockedSection();
                    throw th;
                }
            }
        } finally {
            Binder.restoreCallingIdentity(clearCallingIdentity);
        }
    }

    /* JADX WARN: Finally extract failed */
    public List<ActivityManager.RunningTaskInfo> getChildTasks(WindowContainerToken windowContainerToken, int[] iArr) {
        ActivityTaskManagerService.enforceTaskPermission("getChildTasks()");
        long clearCallingIdentity = Binder.clearCallingIdentity();
        try {
            WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
            WindowManagerService.boostPriorityForLockedSection();
            synchronized (windowManagerGlobalLock) {
                try {
                    if (windowContainerToken == null) {
                        throw new IllegalArgumentException("Can't get children of null parent");
                    }
                    WindowContainer fromBinder = WindowContainer.fromBinder(windowContainerToken.asBinder());
                    if (fromBinder == null) {
                        Slog.e(TAG, "Can't get children of " + windowContainerToken + " because it is not valid.");
                    } else {
                        Task asTask = fromBinder.asTask();
                        if (asTask == null) {
                            Slog.e(TAG, fromBinder + " is not a task...");
                        } else if (!asTask.mCreatedByOrganizer) {
                            Slog.w(TAG, "Can only get children of root tasks created via createRootTask");
                        } else {
                            ArrayList arrayList = new ArrayList();
                            for (int childCount = asTask.getChildCount() - 1; childCount >= 0; childCount--) {
                                Task asTask2 = asTask.getChildAt(childCount).asTask();
                                if (asTask2 != null && (iArr == null || ArrayUtils.contains(iArr, asTask2.getActivityType()))) {
                                    arrayList.add(asTask2.getTaskInfo());
                                }
                            }
                            WindowManagerService.resetPriorityAfterLockedSection();
                            return arrayList;
                        }
                    }
                    WindowManagerService.resetPriorityAfterLockedSection();
                    return null;
                } catch (Throwable th) {
                    WindowManagerService.resetPriorityAfterLockedSection();
                    throw th;
                }
            }
        } finally {
            Binder.restoreCallingIdentity(clearCallingIdentity);
        }
    }

    public List<ActivityManager.RunningTaskInfo> getRootTasks(int i, final int[] iArr) {
        final ArrayList arrayList;
        ActivityTaskManagerService.enforceTaskPermission("getRootTasks()");
        long clearCallingIdentity = Binder.clearCallingIdentity();
        try {
            WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
            WindowManagerService.boostPriorityForLockedSection();
            synchronized (windowManagerGlobalLock) {
                try {
                    DisplayContent displayContent = this.mService.mRootWindowContainer.getDisplayContent(i);
                    if (displayContent == null) {
                        throw new IllegalArgumentException("Display " + i + " doesn't exist");
                    }
                    arrayList = new ArrayList();
                    displayContent.forAllRootTasks(new Consumer() { // from class: com.android.server.wm.TaskOrganizerController$$ExternalSyntheticLambda3
                        @Override // java.util.function.Consumer
                        public final void accept(Object obj) {
                            TaskOrganizerController.lambda$getRootTasks$3(iArr, arrayList, (Task) obj);
                        }
                    });
                } catch (Throwable th) {
                    WindowManagerService.resetPriorityAfterLockedSection();
                    throw th;
                }
            }
            WindowManagerService.resetPriorityAfterLockedSection();
            return arrayList;
        } finally {
            Binder.restoreCallingIdentity(clearCallingIdentity);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$getRootTasks$3(int[] iArr, ArrayList arrayList, Task task) {
        if (iArr == null || ArrayUtils.contains(iArr, task.getActivityType())) {
            arrayList.add(task.getTaskInfo());
        }
    }

    /* JADX WARN: Finally extract failed */
    public void setInterceptBackPressedOnTaskRoot(WindowContainerToken windowContainerToken, boolean z) {
        ActivityTaskManagerService.enforceTaskPermission("setInterceptBackPressedOnTaskRoot()");
        long clearCallingIdentity = Binder.clearCallingIdentity();
        try {
            WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
            WindowManagerService.boostPriorityForLockedSection();
            synchronized (windowManagerGlobalLock) {
                try {
                    if (ProtoLogCache.WM_DEBUG_WINDOW_ORGANIZER_enabled) {
                        ProtoLogImpl.v(ProtoLogGroup.WM_DEBUG_WINDOW_ORGANIZER, 232317536, 3, (String) null, new Object[]{Boolean.valueOf(z)});
                    }
                    WindowContainer fromBinder = WindowContainer.fromBinder(windowContainerToken.asBinder());
                    if (fromBinder == null) {
                        Slog.w(TAG, "Could not resolve window from token");
                    } else {
                        Task asTask = fromBinder.asTask();
                        if (asTask != null) {
                            if (z) {
                                this.mInterceptBackPressedOnRootTasks.add(Integer.valueOf(asTask.mTaskId));
                            } else {
                                this.mInterceptBackPressedOnRootTasks.remove(Integer.valueOf(asTask.mTaskId));
                            }
                            WindowManagerService.resetPriorityAfterLockedSection();
                            return;
                        }
                        Slog.w(TAG, "Could not resolve task from token");
                    }
                    WindowManagerService.resetPriorityAfterLockedSection();
                } catch (Throwable th) {
                    WindowManagerService.resetPriorityAfterLockedSection();
                    throw th;
                }
            }
        } finally {
            Binder.restoreCallingIdentity(clearCallingIdentity);
        }
    }

    /* JADX WARN: Finally extract failed */
    public void restartTaskTopActivityProcessIfVisible(WindowContainerToken windowContainerToken) {
        ActivityTaskManagerService.enforceTaskPermission("restartTopActivityProcessIfVisible()");
        long clearCallingIdentity = Binder.clearCallingIdentity();
        try {
            WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
            WindowManagerService.boostPriorityForLockedSection();
            synchronized (windowManagerGlobalLock) {
                try {
                    WindowContainer fromBinder = WindowContainer.fromBinder(windowContainerToken.asBinder());
                    if (fromBinder == null) {
                        Slog.w(TAG, "Could not resolve window from token");
                    } else {
                        Task asTask = fromBinder.asTask();
                        if (asTask == null) {
                            Slog.w(TAG, "Could not resolve task from token");
                        } else {
                            if (ProtoLogCache.WM_DEBUG_WINDOW_ORGANIZER_enabled) {
                                ProtoLogImpl.v(ProtoLogGroup.WM_DEBUG_WINDOW_ORGANIZER, -1963363332, 1, (String) null, new Object[]{Long.valueOf(asTask.mTaskId)});
                            }
                            ActivityRecord topNonFinishingActivity = asTask.getTopNonFinishingActivity();
                            if (topNonFinishingActivity != null) {
                                topNonFinishingActivity.restartProcessIfVisible();
                            }
                            WindowManagerService.resetPriorityAfterLockedSection();
                            return;
                        }
                    }
                    WindowManagerService.resetPriorityAfterLockedSection();
                } catch (Throwable th) {
                    WindowManagerService.resetPriorityAfterLockedSection();
                    throw th;
                }
            }
        } finally {
            Binder.restoreCallingIdentity(clearCallingIdentity);
        }
    }

    /* JADX WARN: Finally extract failed */
    public void updateCameraCompatControlState(WindowContainerToken windowContainerToken, int i) {
        ActivityTaskManagerService.enforceTaskPermission("updateCameraCompatControlState()");
        long clearCallingIdentity = Binder.clearCallingIdentity();
        try {
            WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
            WindowManagerService.boostPriorityForLockedSection();
            synchronized (windowManagerGlobalLock) {
                try {
                    WindowContainer fromBinder = WindowContainer.fromBinder(windowContainerToken.asBinder());
                    if (fromBinder == null) {
                        Slog.w(TAG, "Could not resolve window from token");
                    } else {
                        Task asTask = fromBinder.asTask();
                        if (asTask == null) {
                            Slog.w(TAG, "Could not resolve task from token");
                        } else {
                            if (ProtoLogCache.WM_DEBUG_WINDOW_ORGANIZER_enabled) {
                                ProtoLogImpl.v(ProtoLogGroup.WM_DEBUG_WINDOW_ORGANIZER, -846931068, 4, (String) null, new Object[]{String.valueOf(TaskInfo.cameraCompatControlStateToString(i)), Long.valueOf(asTask.mTaskId)});
                            }
                            ActivityRecord topNonFinishingActivity = asTask.getTopNonFinishingActivity();
                            if (topNonFinishingActivity != null) {
                                topNonFinishingActivity.updateCameraCompatStateFromUser(i);
                            }
                            WindowManagerService.resetPriorityAfterLockedSection();
                            return;
                        }
                    }
                    WindowManagerService.resetPriorityAfterLockedSection();
                } catch (Throwable th) {
                    WindowManagerService.resetPriorityAfterLockedSection();
                    throw th;
                }
            }
        } finally {
            Binder.restoreCallingIdentity(clearCallingIdentity);
        }
    }

    public void setOrientationRequestPolicy(boolean z, int[] iArr, int[] iArr2) {
        ActivityTaskManagerService.enforceTaskPermission("setOrientationRequestPolicy()");
        long clearCallingIdentity = Binder.clearCallingIdentity();
        try {
            WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
            WindowManagerService.boostPriorityForLockedSection();
            synchronized (windowManagerGlobalLock) {
                try {
                    this.mService.mWindowManager.setOrientationRequestPolicy(z, iArr, iArr2);
                } catch (Throwable th) {
                    WindowManagerService.resetPriorityAfterLockedSection();
                    throw th;
                }
            }
            WindowManagerService.resetPriorityAfterLockedSection();
        } finally {
            Binder.restoreCallingIdentity(clearCallingIdentity);
        }
    }

    public boolean handleInterceptBackPressedOnTaskRoot(Task task) {
        if (task == null || !task.isOrganized() || !this.mInterceptBackPressedOnRootTasks.contains(Integer.valueOf(task.mTaskId))) {
            return false;
        }
        TaskOrganizerPendingEventsQueue taskOrganizerPendingEventsQueue = this.mTaskOrganizerStates.get(task.mTaskOrganizer.asBinder()).mPendingEventsQueue;
        if (taskOrganizerPendingEventsQueue == null) {
            Slog.w(TAG, "cannot get handle BackPressedOnTaskRoot because organizerState is not present");
            return false;
        }
        if (taskOrganizerPendingEventsQueue.getPendingTaskEvent(task, 1) != null) {
            return false;
        }
        PendingTaskEvent pendingTaskEvent = taskOrganizerPendingEventsQueue.getPendingTaskEvent(task, 3);
        if (pendingTaskEvent == null) {
            pendingTaskEvent = new PendingTaskEvent(task, 3);
        } else {
            taskOrganizerPendingEventsQueue.removePendingTaskEvent(pendingTaskEvent);
        }
        taskOrganizerPendingEventsQueue.addPendingTaskEvent(pendingTaskEvent);
        this.mService.mWindowManager.mWindowPlacerLocked.requestTraversal();
        return true;
    }

    public void dump(PrintWriter printWriter, String str) {
        String str2 = str + "  ";
        printWriter.print(str);
        printWriter.println("TaskOrganizerController:");
        for (TaskOrganizerState taskOrganizerState : this.mTaskOrganizerStates.values()) {
            ArrayList arrayList = taskOrganizerState.mOrganizedTasks;
            printWriter.print(str2 + "  ");
            printWriter.println(taskOrganizerState.mOrganizer.mTaskOrganizer + " uid=" + taskOrganizerState.mUid + ":");
            for (int i = 0; i < arrayList.size(); i++) {
                Task task = (Task) arrayList.get(i);
                printWriter.println(str2 + "    (" + WindowConfiguration.windowingModeToString(task.getWindowingMode()) + ") " + task);
            }
        }
        printWriter.println();
    }

    @VisibleForTesting
    TaskOrganizerState getTaskOrganizerState(IBinder iBinder) {
        return this.mTaskOrganizerStates.get(iBinder);
    }

    @VisibleForTesting
    TaskOrganizerPendingEventsQueue getTaskOrganizerPendingEvents(IBinder iBinder) {
        return this.mTaskOrganizerStates.get(iBinder).mPendingEventsQueue;
    }
}
