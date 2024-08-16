package com.android.server.wm;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Binder;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.ArrayMap;
import android.util.ArraySet;
import android.util.Slog;
import android.util.SparseArray;
import android.view.RemoteAnimationDefinition;
import android.window.ITaskFragmentOrganizer;
import android.window.ITaskFragmentOrganizerController;
import android.window.TaskFragmentInfo;
import android.window.TaskFragmentOrganizer;
import android.window.TaskFragmentParentInfo;
import android.window.TaskFragmentTransaction;
import android.window.WindowContainerTransaction;
import com.android.internal.protolog.ProtoLogGroup;
import com.android.internal.protolog.ProtoLogImpl;
import com.android.server.wm.TaskFragmentOrganizerController;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.WeakHashMap;
import java.util.function.Predicate;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
public class TaskFragmentOrganizerController extends ITaskFragmentOrganizerController.Stub {
    private static final String TAG = "TaskFragmentOrganizerController";
    private static final long TEMPORARY_ACTIVITY_TOKEN_TIMEOUT_MS = 5000;
    private final ActivityTaskManagerService mAtmService;
    private final WindowManagerGlobalLock mGlobalLock;
    private final WindowOrganizerController mWindowOrganizerController;
    private final ArrayMap<IBinder, TaskFragmentOrganizerState> mTaskFragmentOrganizerState = new ArrayMap<>();
    private final ArrayMap<IBinder, List<PendingTaskFragmentEvent>> mPendingTaskFragmentEvents = new ArrayMap<>();
    private final ArraySet<Task> mTmpTaskSet = new ArraySet<>();

    /* JADX INFO: Access modifiers changed from: package-private */
    public TaskFragmentOrganizerController(ActivityTaskManagerService activityTaskManagerService, WindowOrganizerController windowOrganizerController) {
        Objects.requireNonNull(activityTaskManagerService);
        this.mAtmService = activityTaskManagerService;
        this.mGlobalLock = activityTaskManagerService.mGlobalLock;
        Objects.requireNonNull(windowOrganizerController);
        this.mWindowOrganizerController = windowOrganizerController;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
    public class TaskFragmentOrganizerState implements IBinder.DeathRecipient {
        private final ITaskFragmentOrganizer mOrganizer;
        private final int mOrganizerPid;
        private final int mOrganizerUid;
        private RemoteAnimationDefinition mRemoteAnimationDefinition;
        private final ArrayList<TaskFragment> mOrganizedTaskFragments = new ArrayList<>();
        private final Map<TaskFragment, TaskFragmentInfo> mLastSentTaskFragmentInfos = new WeakHashMap();
        private final Map<TaskFragment, Integer> mTaskFragmentTaskIds = new WeakHashMap();
        private final SparseArray<TaskFragmentParentInfo> mLastSentTaskFragmentParentInfos = new SparseArray<>();
        private final Map<IBinder, ActivityRecord> mTemporaryActivityTokens = new WeakHashMap();
        private final ArrayMap<IBinder, Integer> mDeferredTransitions = new ArrayMap<>();

        TaskFragmentOrganizerState(ITaskFragmentOrganizer iTaskFragmentOrganizer, int i, int i2) {
            this.mOrganizer = iTaskFragmentOrganizer;
            this.mOrganizerPid = i;
            this.mOrganizerUid = i2;
            try {
                iTaskFragmentOrganizer.asBinder().linkToDeath(this, 0);
            } catch (RemoteException unused) {
                Slog.e(TaskFragmentOrganizerController.TAG, "TaskFragmentOrganizer failed to register death recipient");
            }
        }

        @Override // android.os.IBinder.DeathRecipient
        public void binderDied() {
            WindowManagerGlobalLock windowManagerGlobalLock = TaskFragmentOrganizerController.this.mGlobalLock;
            WindowManagerService.boostPriorityForLockedSection();
            synchronized (windowManagerGlobalLock) {
                try {
                    TaskFragmentOrganizerController.this.removeOrganizer(this.mOrganizer);
                } catch (Throwable th) {
                    WindowManagerService.resetPriorityAfterLockedSection();
                    throw th;
                }
            }
            WindowManagerService.resetPriorityAfterLockedSection();
        }

        boolean addTaskFragment(TaskFragment taskFragment) {
            if (taskFragment.mTaskFragmentAppearedSent || this.mOrganizedTaskFragments.contains(taskFragment)) {
                return false;
            }
            this.mOrganizedTaskFragments.add(taskFragment);
            return true;
        }

        void removeTaskFragment(TaskFragment taskFragment) {
            this.mOrganizedTaskFragments.remove(taskFragment);
        }

        void dispose() {
            for (int size = this.mOrganizedTaskFragments.size() - 1; size >= 0; size--) {
                this.mOrganizedTaskFragments.get(size).onTaskFragmentOrganizerRemoved();
            }
            TaskFragmentOrganizerController.this.mAtmService.deferWindowLayout();
            while (!this.mOrganizedTaskFragments.isEmpty()) {
                try {
                    this.mOrganizedTaskFragments.remove(0).removeImmediately();
                } catch (Throwable th) {
                    TaskFragmentOrganizerController.this.mAtmService.continueWindowLayout();
                    throw th;
                }
            }
            TaskFragmentOrganizerController.this.mAtmService.continueWindowLayout();
            for (int size2 = this.mDeferredTransitions.size() - 1; size2 >= 0; size2--) {
                onTransactionFinished(this.mDeferredTransitions.keyAt(size2));
            }
            this.mOrganizer.asBinder().unlinkToDeath(this, 0);
        }

        TaskFragmentTransaction.Change prepareTaskFragmentAppeared(TaskFragment taskFragment) {
            if (ProtoLogCache.WM_DEBUG_WINDOW_ORGANIZER_enabled) {
                ProtoLogImpl.v(ProtoLogGroup.WM_DEBUG_WINDOW_ORGANIZER, 1284122013, 0, (String) null, new Object[]{String.valueOf(taskFragment.getName())});
            }
            TaskFragmentInfo taskFragmentInfo = taskFragment.getTaskFragmentInfo();
            int i = taskFragment.getTask().mTaskId;
            taskFragment.mTaskFragmentAppearedSent = true;
            this.mLastSentTaskFragmentInfos.put(taskFragment, taskFragmentInfo);
            this.mTaskFragmentTaskIds.put(taskFragment, Integer.valueOf(i));
            return new TaskFragmentTransaction.Change(1).setTaskFragmentToken(taskFragment.getFragmentToken()).setTaskFragmentInfo(taskFragmentInfo).setTaskId(i);
        }

        TaskFragmentTransaction.Change prepareTaskFragmentVanished(TaskFragment taskFragment) {
            int i;
            if (ProtoLogCache.WM_DEBUG_WINDOW_ORGANIZER_enabled) {
                ProtoLogImpl.v(ProtoLogGroup.WM_DEBUG_WINDOW_ORGANIZER, -542756093, 0, (String) null, new Object[]{String.valueOf(taskFragment.getName())});
            }
            taskFragment.mTaskFragmentAppearedSent = false;
            this.mLastSentTaskFragmentInfos.remove(taskFragment);
            if (this.mTaskFragmentTaskIds.containsKey(taskFragment)) {
                i = this.mTaskFragmentTaskIds.remove(taskFragment).intValue();
                if (!this.mTaskFragmentTaskIds.containsValue(Integer.valueOf(i))) {
                    this.mLastSentTaskFragmentParentInfos.remove(i);
                }
            } else {
                i = -1;
            }
            return new TaskFragmentTransaction.Change(3).setTaskFragmentToken(taskFragment.getFragmentToken()).setTaskFragmentInfo(taskFragment.getTaskFragmentInfo()).setTaskId(i);
        }

        TaskFragmentTransaction.Change prepareTaskFragmentInfoChanged(TaskFragment taskFragment) {
            TaskFragmentInfo taskFragmentInfo = taskFragment.getTaskFragmentInfo();
            TaskFragmentInfo taskFragmentInfo2 = this.mLastSentTaskFragmentInfos.get(taskFragment);
            if (taskFragmentInfo.equalsForTaskFragmentOrganizer(taskFragmentInfo2) && WindowOrganizerController.configurationsAreEqualForOrganizer(taskFragmentInfo.getConfiguration(), taskFragmentInfo2.getConfiguration())) {
                return null;
            }
            if (ProtoLogCache.WM_DEBUG_WINDOW_ORGANIZER_enabled) {
                ProtoLogImpl.v(ProtoLogGroup.WM_DEBUG_WINDOW_ORGANIZER, 1022095595, 0, (String) null, new Object[]{String.valueOf(taskFragment.getName())});
            }
            this.mLastSentTaskFragmentInfos.put(taskFragment, taskFragmentInfo);
            return new TaskFragmentTransaction.Change(2).setTaskFragmentToken(taskFragment.getFragmentToken()).setTaskFragmentInfo(taskFragmentInfo).setTaskId(taskFragment.getTask().mTaskId);
        }

        TaskFragmentTransaction.Change prepareTaskFragmentParentInfoChanged(Task task) {
            int i = task.mTaskId;
            TaskFragmentParentInfo taskFragmentParentInfo = task.getTaskFragmentParentInfo();
            TaskFragmentParentInfo taskFragmentParentInfo2 = this.mLastSentTaskFragmentParentInfos.get(i);
            Configuration configuration = taskFragmentParentInfo2 != null ? taskFragmentParentInfo2.getConfiguration() : null;
            if (taskFragmentParentInfo.equalsForTaskFragmentOrganizer(taskFragmentParentInfo2) && WindowOrganizerController.configurationsAreEqualForOrganizer(taskFragmentParentInfo.getConfiguration(), configuration)) {
                return null;
            }
            if (ProtoLogCache.WM_DEBUG_WINDOW_ORGANIZER_enabled) {
                ProtoLogImpl.v(ProtoLogGroup.WM_DEBUG_WINDOW_ORGANIZER, -706481945, 4, (String) null, new Object[]{String.valueOf(task.getName()), Long.valueOf(i)});
            }
            this.mLastSentTaskFragmentParentInfos.put(i, new TaskFragmentParentInfo(taskFragmentParentInfo));
            return new TaskFragmentTransaction.Change(4).setTaskId(i).setTaskFragmentParentInfo(taskFragmentParentInfo);
        }

        TaskFragmentTransaction.Change prepareTaskFragmentError(IBinder iBinder, TaskFragment taskFragment, int i, Throwable th) {
            if (ProtoLogCache.WM_DEBUG_WINDOW_ORGANIZER_enabled) {
                ProtoLogImpl.v(ProtoLogGroup.WM_DEBUG_WINDOW_ORGANIZER, 743418423, 0, (String) null, new Object[]{String.valueOf(th.toString())});
            }
            return new TaskFragmentTransaction.Change(5).setErrorCallbackToken(iBinder).setErrorBundle(TaskFragmentOrganizer.putErrorInfoInBundle(th, taskFragment != null ? taskFragment.getTaskFragmentInfo() : null, i));
        }

        TaskFragmentTransaction.Change prepareActivityReparentedToTask(ActivityRecord activityRecord) {
            IBinder iBinder;
            if (activityRecord.finishing) {
                Slog.d(TaskFragmentOrganizerController.TAG, "Reparent activity=" + activityRecord.token + " is finishing");
                return null;
            }
            Task task = activityRecord.getTask();
            if (task != null) {
                int i = task.effectiveUid;
                int i2 = this.mOrganizerUid;
                if (i == i2) {
                    if (task.isAllowedToEmbedActivity(activityRecord, i2) != 0 || !task.isAllowedToEmbedActivityInTrustedMode(activityRecord, this.mOrganizerUid)) {
                        Slog.d(TaskFragmentOrganizerController.TAG, "Reparent activity=" + activityRecord.token + " is not allowed to be embedded in trusted mode.");
                        return null;
                    }
                    if (activityRecord.getPid() == this.mOrganizerPid) {
                        iBinder = activityRecord.token;
                    } else {
                        final Binder binder = new Binder("TemporaryActivityToken");
                        this.mTemporaryActivityTokens.put(binder, activityRecord);
                        TaskFragmentOrganizerController.this.mAtmService.mWindowManager.mH.postDelayed(new Runnable() { // from class: com.android.server.wm.TaskFragmentOrganizerController$TaskFragmentOrganizerState$$ExternalSyntheticLambda0
                            @Override // java.lang.Runnable
                            public final void run() {
                                TaskFragmentOrganizerController.TaskFragmentOrganizerState.this.lambda$prepareActivityReparentedToTask$0(binder);
                            }
                        }, TaskFragmentOrganizerController.TEMPORARY_ACTIVITY_TOKEN_TIMEOUT_MS);
                        iBinder = binder;
                    }
                    if (ProtoLogCache.WM_DEBUG_WINDOW_ORGANIZER_enabled) {
                        ProtoLogImpl.v(ProtoLogGroup.WM_DEBUG_WINDOW_ORGANIZER, 873160948, 4, (String) null, new Object[]{String.valueOf(activityRecord.token), Long.valueOf(task.mTaskId)});
                    }
                    return new TaskFragmentTransaction.Change(6).setTaskId(task.mTaskId).setActivityIntent(TaskFragmentOrganizerController.trimIntent(activityRecord.intent)).setActivityToken(iBinder);
                }
            }
            Slog.d(TaskFragmentOrganizerController.TAG, "Reparent activity=" + activityRecord.token + " is not in a task belong to the organizer app.");
            return null;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$prepareActivityReparentedToTask$0(IBinder iBinder) {
            WindowManagerGlobalLock windowManagerGlobalLock = TaskFragmentOrganizerController.this.mGlobalLock;
            WindowManagerService.boostPriorityForLockedSection();
            synchronized (windowManagerGlobalLock) {
                try {
                    this.mTemporaryActivityTokens.remove(iBinder);
                } catch (Throwable th) {
                    WindowManagerService.resetPriorityAfterLockedSection();
                    throw th;
                }
            }
            WindowManagerService.resetPriorityAfterLockedSection();
        }

        void dispatchTransaction(TaskFragmentTransaction taskFragmentTransaction) {
            if (taskFragmentTransaction.isEmpty()) {
                return;
            }
            try {
                this.mOrganizer.onTransactionReady(taskFragmentTransaction);
                onTransactionStarted(taskFragmentTransaction.getTransactionToken());
            } catch (RemoteException e) {
                Slog.d(TaskFragmentOrganizerController.TAG, "Exception sending TaskFragmentTransaction", e);
            }
        }

        void onTransactionStarted(IBinder iBinder) {
            if (TaskFragmentOrganizerController.this.mWindowOrganizerController.getTransitionController().isCollecting()) {
                int collectingTransitionId = TaskFragmentOrganizerController.this.mWindowOrganizerController.getTransitionController().getCollectingTransitionId();
                if (ProtoLogCache.WM_DEBUG_WINDOW_TRANSITIONS_enabled) {
                    ProtoLogImpl.v(ProtoLogGroup.WM_DEBUG_WINDOW_TRANSITIONS, 1046228706, 1, (String) null, new Object[]{Long.valueOf(collectingTransitionId), String.valueOf(iBinder)});
                }
                this.mDeferredTransitions.put(iBinder, Integer.valueOf(collectingTransitionId));
                TaskFragmentOrganizerController.this.mWindowOrganizerController.getTransitionController().deferTransitionReady();
            }
        }

        void onTransactionFinished(IBinder iBinder) {
            if (this.mDeferredTransitions.containsKey(iBinder)) {
                int intValue = this.mDeferredTransitions.remove(iBinder).intValue();
                if (!TaskFragmentOrganizerController.this.mWindowOrganizerController.getTransitionController().isCollecting() || TaskFragmentOrganizerController.this.mWindowOrganizerController.getTransitionController().getCollectingTransitionId() != intValue) {
                    if (ProtoLogCache.WM_DEBUG_WINDOW_TRANSITIONS_enabled) {
                        ProtoLogImpl.w(ProtoLogGroup.WM_DEBUG_WINDOW_TRANSITIONS, 851368695, 1, (String) null, new Object[]{Long.valueOf(intValue), String.valueOf(iBinder)});
                        return;
                    }
                    return;
                }
                if (ProtoLogCache.WM_DEBUG_WINDOW_TRANSITIONS_enabled) {
                    ProtoLogImpl.v(ProtoLogGroup.WM_DEBUG_WINDOW_TRANSITIONS, 1075460705, 1, (String) null, new Object[]{Long.valueOf(intValue), String.valueOf(iBinder)});
                }
                TaskFragmentOrganizerController.this.mWindowOrganizerController.getTransitionController().continueTransitionReady();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public ActivityRecord getReparentActivityFromTemporaryToken(ITaskFragmentOrganizer iTaskFragmentOrganizer, IBinder iBinder) {
        TaskFragmentOrganizerState taskFragmentOrganizerState;
        if (iTaskFragmentOrganizer == null || iBinder == null || (taskFragmentOrganizerState = this.mTaskFragmentOrganizerState.get(iTaskFragmentOrganizer.asBinder())) == null) {
            return null;
        }
        return (ActivityRecord) taskFragmentOrganizerState.mTemporaryActivityTokens.remove(iBinder);
    }

    public void registerOrganizer(ITaskFragmentOrganizer iTaskFragmentOrganizer) {
        int callingPid = Binder.getCallingPid();
        int callingUid = Binder.getCallingUid();
        WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
        WindowManagerService.boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                if (ProtoLogCache.WM_DEBUG_WINDOW_ORGANIZER_enabled) {
                    ProtoLogImpl.v(ProtoLogGroup.WM_DEBUG_WINDOW_ORGANIZER, 1653025361, 20, (String) null, new Object[]{String.valueOf(iTaskFragmentOrganizer.asBinder()), Long.valueOf(callingUid), Long.valueOf(callingPid)});
                }
                if (isOrganizerRegistered(iTaskFragmentOrganizer)) {
                    throw new IllegalStateException("Replacing existing organizer currently unsupported");
                }
                this.mTaskFragmentOrganizerState.put(iTaskFragmentOrganizer.asBinder(), new TaskFragmentOrganizerState(iTaskFragmentOrganizer, callingPid, callingUid));
                this.mPendingTaskFragmentEvents.put(iTaskFragmentOrganizer.asBinder(), new ArrayList());
            } catch (Throwable th) {
                WindowManagerService.resetPriorityAfterLockedSection();
                throw th;
            }
        }
        WindowManagerService.resetPriorityAfterLockedSection();
    }

    public void unregisterOrganizer(ITaskFragmentOrganizer iTaskFragmentOrganizer) {
        int callingPid = Binder.getCallingPid();
        long callingUid = Binder.getCallingUid();
        long clearCallingIdentity = Binder.clearCallingIdentity();
        try {
            WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
            WindowManagerService.boostPriorityForLockedSection();
            synchronized (windowManagerGlobalLock) {
                try {
                    if (ProtoLogCache.WM_DEBUG_WINDOW_ORGANIZER_enabled) {
                        ProtoLogImpl.v(ProtoLogGroup.WM_DEBUG_WINDOW_ORGANIZER, -1311436264, 20, (String) null, new Object[]{String.valueOf(iTaskFragmentOrganizer.asBinder()), Long.valueOf(callingUid), Long.valueOf(callingPid)});
                    }
                    removeOrganizer(iTaskFragmentOrganizer);
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

    public void registerRemoteAnimations(ITaskFragmentOrganizer iTaskFragmentOrganizer, RemoteAnimationDefinition remoteAnimationDefinition) {
        int callingPid = Binder.getCallingPid();
        int callingUid = Binder.getCallingUid();
        WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
        WindowManagerService.boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                if (ProtoLogCache.WM_DEBUG_WINDOW_ORGANIZER_enabled) {
                    ProtoLogImpl.v(ProtoLogGroup.WM_DEBUG_WINDOW_ORGANIZER, 1210037962, 20, (String) null, new Object[]{String.valueOf(iTaskFragmentOrganizer.asBinder()), Long.valueOf(callingUid), Long.valueOf(callingPid)});
                }
                TaskFragmentOrganizerState taskFragmentOrganizerState = this.mTaskFragmentOrganizerState.get(iTaskFragmentOrganizer.asBinder());
                if (taskFragmentOrganizerState == null) {
                    throw new IllegalStateException("The organizer hasn't been registered.");
                }
                if (taskFragmentOrganizerState.mRemoteAnimationDefinition != null) {
                    throw new IllegalStateException("The organizer has already registered remote animations=" + taskFragmentOrganizerState.mRemoteAnimationDefinition);
                }
                remoteAnimationDefinition.setCallingPidUid(callingPid, callingUid);
                taskFragmentOrganizerState.mRemoteAnimationDefinition = remoteAnimationDefinition;
            } catch (Throwable th) {
                WindowManagerService.resetPriorityAfterLockedSection();
                throw th;
            }
        }
        WindowManagerService.resetPriorityAfterLockedSection();
    }

    public void unregisterRemoteAnimations(ITaskFragmentOrganizer iTaskFragmentOrganizer) {
        int callingPid = Binder.getCallingPid();
        long callingUid = Binder.getCallingUid();
        WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
        WindowManagerService.boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                if (ProtoLogCache.WM_DEBUG_WINDOW_ORGANIZER_enabled) {
                    ProtoLogImpl.v(ProtoLogGroup.WM_DEBUG_WINDOW_ORGANIZER, -70719599, 20, (String) null, new Object[]{String.valueOf(iTaskFragmentOrganizer.asBinder()), Long.valueOf(callingUid), Long.valueOf(callingPid)});
                }
                TaskFragmentOrganizerState taskFragmentOrganizerState = this.mTaskFragmentOrganizerState.get(iTaskFragmentOrganizer.asBinder());
                if (taskFragmentOrganizerState == null) {
                    Slog.e(TAG, "The organizer hasn't been registered.");
                    WindowManagerService.resetPriorityAfterLockedSection();
                } else {
                    taskFragmentOrganizerState.mRemoteAnimationDefinition = null;
                    WindowManagerService.resetPriorityAfterLockedSection();
                }
            } catch (Throwable th) {
                WindowManagerService.resetPriorityAfterLockedSection();
                throw th;
            }
        }
    }

    public void onTransactionHandled(IBinder iBinder, WindowContainerTransaction windowContainerTransaction, int i, boolean z) {
        WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
        WindowManagerService.boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                if (isValidTransaction(windowContainerTransaction)) {
                    applyTransaction(windowContainerTransaction, i, z);
                }
                ITaskFragmentOrganizer taskFragmentOrganizer = windowContainerTransaction.getTaskFragmentOrganizer();
                TaskFragmentOrganizerState taskFragmentOrganizerState = taskFragmentOrganizer != null ? this.mTaskFragmentOrganizerState.get(taskFragmentOrganizer.asBinder()) : null;
                if (taskFragmentOrganizerState != null) {
                    taskFragmentOrganizerState.onTransactionFinished(iBinder);
                }
            } catch (Throwable th) {
                WindowManagerService.resetPriorityAfterLockedSection();
                throw th;
            }
        }
        WindowManagerService.resetPriorityAfterLockedSection();
    }

    public void applyTransaction(WindowContainerTransaction windowContainerTransaction, int i, boolean z) {
        WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
        WindowManagerService.boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                if (!isValidTransaction(windowContainerTransaction)) {
                    WindowManagerService.resetPriorityAfterLockedSection();
                } else {
                    this.mWindowOrganizerController.applyTaskFragmentTransactionLocked(windowContainerTransaction, i, z);
                    WindowManagerService.resetPriorityAfterLockedSection();
                }
            } catch (Throwable th) {
                WindowManagerService.resetPriorityAfterLockedSection();
                throw th;
            }
        }
    }

    public RemoteAnimationDefinition getRemoteAnimationDefinition(ITaskFragmentOrganizer iTaskFragmentOrganizer) {
        WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
        WindowManagerService.boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                TaskFragmentOrganizerState taskFragmentOrganizerState = this.mTaskFragmentOrganizerState.get(iTaskFragmentOrganizer.asBinder());
                if (taskFragmentOrganizerState == null) {
                    Slog.e(TAG, "TaskFragmentOrganizer has been unregistered or died when trying to play animation on its organized windows.");
                    WindowManagerService.resetPriorityAfterLockedSection();
                    return null;
                }
                RemoteAnimationDefinition remoteAnimationDefinition = taskFragmentOrganizerState.mRemoteAnimationDefinition;
                WindowManagerService.resetPriorityAfterLockedSection();
                return remoteAnimationDefinition;
            } catch (Throwable th) {
                WindowManagerService.resetPriorityAfterLockedSection();
                throw th;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int getTaskFragmentOrganizerUid(ITaskFragmentOrganizer iTaskFragmentOrganizer) {
        return validateAndGetState(iTaskFragmentOrganizer).mOrganizerUid;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void onTaskFragmentAppeared(ITaskFragmentOrganizer iTaskFragmentOrganizer, TaskFragment taskFragment) {
        if (taskFragment.mTaskFragmentVanishedSent) {
            return;
        }
        if (taskFragment.getTask() == null) {
            Slog.w(TAG, "onTaskFragmentAppeared failed because it is not attached tf=" + taskFragment);
            return;
        }
        if (validateAndGetState(iTaskFragmentOrganizer).addTaskFragment(taskFragment) && getPendingTaskFragmentEvent(taskFragment, 0) == null) {
            addPendingEvent(new PendingTaskFragmentEvent.Builder(0, iTaskFragmentOrganizer).setTaskFragment(taskFragment).build());
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void onTaskFragmentInfoChanged(ITaskFragmentOrganizer iTaskFragmentOrganizer, TaskFragment taskFragment) {
        if (taskFragment.mTaskFragmentVanishedSent) {
            return;
        }
        validateAndGetState(iTaskFragmentOrganizer);
        if (taskFragment.mTaskFragmentAppearedSent) {
            PendingTaskFragmentEvent lastPendingLifecycleEvent = getLastPendingLifecycleEvent(taskFragment);
            if (lastPendingLifecycleEvent == null) {
                lastPendingLifecycleEvent = new PendingTaskFragmentEvent.Builder(2, iTaskFragmentOrganizer).setTaskFragment(taskFragment).build();
            } else {
                removePendingEvent(lastPendingLifecycleEvent);
                lastPendingLifecycleEvent.mDeferTime = 0L;
            }
            addPendingEvent(lastPendingLifecycleEvent);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void onTaskFragmentVanished(ITaskFragmentOrganizer iTaskFragmentOrganizer, TaskFragment taskFragment) {
        if (taskFragment.mTaskFragmentVanishedSent) {
            return;
        }
        taskFragment.mTaskFragmentVanishedSent = true;
        TaskFragmentOrganizerState validateAndGetState = validateAndGetState(iTaskFragmentOrganizer);
        List<PendingTaskFragmentEvent> list = this.mPendingTaskFragmentEvents.get(iTaskFragmentOrganizer.asBinder());
        for (int size = list.size() - 1; size >= 0; size--) {
            if (taskFragment == list.get(size).mTaskFragment) {
                list.remove(size);
            }
        }
        addPendingEvent(new PendingTaskFragmentEvent.Builder(1, iTaskFragmentOrganizer).setTaskFragment(taskFragment).build());
        validateAndGetState.removeTaskFragment(taskFragment);
        this.mAtmService.mWindowManager.mWindowPlacerLocked.requestTraversal();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void onTaskFragmentError(ITaskFragmentOrganizer iTaskFragmentOrganizer, IBinder iBinder, TaskFragment taskFragment, int i, Throwable th) {
        if (taskFragment == null || !taskFragment.mTaskFragmentVanishedSent) {
            validateAndGetState(iTaskFragmentOrganizer);
            Slog.w(TAG, "onTaskFragmentError ", th);
            addPendingEvent(new PendingTaskFragmentEvent.Builder(4, iTaskFragmentOrganizer).setErrorCallbackToken(iBinder).setTaskFragment(taskFragment).setException(th).setOpType(i).build());
            this.mAtmService.mWindowManager.mWindowPlacerLocked.requestTraversal();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void onActivityReparentedToTask(ActivityRecord activityRecord) {
        ITaskFragmentOrganizer iTaskFragmentOrganizer = activityRecord.mLastTaskFragmentOrganizerBeforePip;
        if (iTaskFragmentOrganizer == null) {
            final TaskFragment[] taskFragmentArr = new TaskFragment[1];
            activityRecord.getTask().forAllLeafTaskFragments(new Predicate() { // from class: com.android.server.wm.TaskFragmentOrganizerController$$ExternalSyntheticLambda0
                @Override // java.util.function.Predicate
                public final boolean test(Object obj) {
                    boolean lambda$onActivityReparentedToTask$0;
                    lambda$onActivityReparentedToTask$0 = TaskFragmentOrganizerController.lambda$onActivityReparentedToTask$0(taskFragmentArr, (TaskFragment) obj);
                    return lambda$onActivityReparentedToTask$0;
                }
            });
            TaskFragment taskFragment = taskFragmentArr[0];
            if (taskFragment == null) {
                return;
            } else {
                iTaskFragmentOrganizer = taskFragment.getTaskFragmentOrganizer();
            }
        }
        if (!isOrganizerRegistered(iTaskFragmentOrganizer)) {
            Slog.w(TAG, "The last TaskFragmentOrganizer no longer exists");
        } else {
            addPendingEvent(new PendingTaskFragmentEvent.Builder(5, iTaskFragmentOrganizer).setActivity(activityRecord).build());
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ boolean lambda$onActivityReparentedToTask$0(TaskFragment[] taskFragmentArr, TaskFragment taskFragment) {
        if (!taskFragment.isOrganizedTaskFragment()) {
            return false;
        }
        taskFragmentArr[0] = taskFragment;
        return true;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void onTaskFragmentParentInfoChanged(ITaskFragmentOrganizer iTaskFragmentOrganizer, Task task) {
        validateAndGetState(iTaskFragmentOrganizer);
        if (getLastPendingParentInfoChangedEvent(iTaskFragmentOrganizer, task) == null) {
            addPendingEvent(new PendingTaskFragmentEvent.Builder(3, iTaskFragmentOrganizer).setTask(task).build());
        }
    }

    private PendingTaskFragmentEvent getLastPendingParentInfoChangedEvent(ITaskFragmentOrganizer iTaskFragmentOrganizer, Task task) {
        List<PendingTaskFragmentEvent> list = this.mPendingTaskFragmentEvents.get(iTaskFragmentOrganizer.asBinder());
        for (int size = list.size() - 1; size >= 0; size--) {
            PendingTaskFragmentEvent pendingTaskFragmentEvent = list.get(size);
            if (task == pendingTaskFragmentEvent.mTask && pendingTaskFragmentEvent.mEventType == 3) {
                return pendingTaskFragmentEvent;
            }
        }
        return null;
    }

    private void addPendingEvent(PendingTaskFragmentEvent pendingTaskFragmentEvent) {
        this.mPendingTaskFragmentEvents.get(pendingTaskFragmentEvent.mTaskFragmentOrg.asBinder()).add(pendingTaskFragmentEvent);
    }

    private void removePendingEvent(PendingTaskFragmentEvent pendingTaskFragmentEvent) {
        this.mPendingTaskFragmentEvents.get(pendingTaskFragmentEvent.mTaskFragmentOrg.asBinder()).remove(pendingTaskFragmentEvent);
    }

    private boolean isOrganizerRegistered(ITaskFragmentOrganizer iTaskFragmentOrganizer) {
        return this.mTaskFragmentOrganizerState.containsKey(iTaskFragmentOrganizer.asBinder());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void removeOrganizer(ITaskFragmentOrganizer iTaskFragmentOrganizer) {
        TaskFragmentOrganizerState taskFragmentOrganizerState = this.mTaskFragmentOrganizerState.get(iTaskFragmentOrganizer.asBinder());
        if (taskFragmentOrganizerState == null) {
            Slog.w(TAG, "The organizer has already been removed.");
            return;
        }
        this.mPendingTaskFragmentEvents.remove(iTaskFragmentOrganizer.asBinder());
        taskFragmentOrganizerState.dispose();
        this.mTaskFragmentOrganizerState.remove(iTaskFragmentOrganizer.asBinder());
    }

    private TaskFragmentOrganizerState validateAndGetState(ITaskFragmentOrganizer iTaskFragmentOrganizer) {
        TaskFragmentOrganizerState taskFragmentOrganizerState = this.mTaskFragmentOrganizerState.get(iTaskFragmentOrganizer.asBinder());
        if (taskFragmentOrganizerState != null) {
            return taskFragmentOrganizerState;
        }
        throw new IllegalArgumentException("TaskFragmentOrganizer has not been registered. Organizer=" + iTaskFragmentOrganizer);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean isValidTransaction(WindowContainerTransaction windowContainerTransaction) {
        if (windowContainerTransaction.isEmpty()) {
            return false;
        }
        ITaskFragmentOrganizer taskFragmentOrganizer = windowContainerTransaction.getTaskFragmentOrganizer();
        if (windowContainerTransaction.getTaskFragmentOrganizer() != null && isOrganizerRegistered(taskFragmentOrganizer)) {
            return true;
        }
        Slog.e(TAG, "Caller organizer=" + taskFragmentOrganizer + " is no longer registered");
        return false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
    public static class PendingTaskFragmentEvent {
        static final int EVENT_ACTIVITY_REPARENTED_TO_TASK = 5;
        static final int EVENT_APPEARED = 0;
        static final int EVENT_ERROR = 4;
        static final int EVENT_INFO_CHANGED = 2;
        static final int EVENT_PARENT_INFO_CHANGED = 3;
        static final int EVENT_VANISHED = 1;
        private final ActivityRecord mActivity;
        private long mDeferTime;
        private final IBinder mErrorCallbackToken;
        private final int mEventType;
        private final Throwable mException;
        private int mOpType;
        private final Task mTask;
        private final TaskFragment mTaskFragment;
        private final ITaskFragmentOrganizer mTaskFragmentOrg;

        @Retention(RetentionPolicy.SOURCE)
        /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
        public @interface EventType {
        }

        private PendingTaskFragmentEvent(int i, ITaskFragmentOrganizer iTaskFragmentOrganizer, TaskFragment taskFragment, IBinder iBinder, Throwable th, ActivityRecord activityRecord, Task task, int i2) {
            this.mEventType = i;
            this.mTaskFragmentOrg = iTaskFragmentOrganizer;
            this.mTaskFragment = taskFragment;
            this.mErrorCallbackToken = iBinder;
            this.mException = th;
            this.mActivity = activityRecord;
            this.mTask = task;
            this.mOpType = i2;
        }

        boolean isLifecycleEvent() {
            int i = this.mEventType;
            return i == 0 || i == 1 || i == 2 || i == 3;
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
        public static class Builder {
            private ActivityRecord mActivity;
            private IBinder mErrorCallbackToken;
            private final int mEventType;
            private Throwable mException;
            private int mOpType;
            private Task mTask;
            private TaskFragment mTaskFragment;
            private final ITaskFragmentOrganizer mTaskFragmentOrg;

            Builder(int i, ITaskFragmentOrganizer iTaskFragmentOrganizer) {
                this.mEventType = i;
                Objects.requireNonNull(iTaskFragmentOrganizer);
                this.mTaskFragmentOrg = iTaskFragmentOrganizer;
            }

            Builder setTaskFragment(TaskFragment taskFragment) {
                this.mTaskFragment = taskFragment;
                return this;
            }

            Builder setErrorCallbackToken(IBinder iBinder) {
                this.mErrorCallbackToken = iBinder;
                return this;
            }

            Builder setException(Throwable th) {
                Objects.requireNonNull(th);
                this.mException = th;
                return this;
            }

            Builder setActivity(ActivityRecord activityRecord) {
                Objects.requireNonNull(activityRecord);
                this.mActivity = activityRecord;
                return this;
            }

            Builder setTask(Task task) {
                Objects.requireNonNull(task);
                this.mTask = task;
                return this;
            }

            Builder setOpType(int i) {
                this.mOpType = i;
                return this;
            }

            PendingTaskFragmentEvent build() {
                return new PendingTaskFragmentEvent(this.mEventType, this.mTaskFragmentOrg, this.mTaskFragment, this.mErrorCallbackToken, this.mException, this.mActivity, this.mTask, this.mOpType);
            }
        }
    }

    private PendingTaskFragmentEvent getLastPendingLifecycleEvent(TaskFragment taskFragment) {
        List<PendingTaskFragmentEvent> list = this.mPendingTaskFragmentEvents.get(taskFragment.getTaskFragmentOrganizer().asBinder());
        for (int size = list.size() - 1; size >= 0; size--) {
            PendingTaskFragmentEvent pendingTaskFragmentEvent = list.get(size);
            if (taskFragment == pendingTaskFragmentEvent.mTaskFragment && pendingTaskFragmentEvent.isLifecycleEvent()) {
                return pendingTaskFragmentEvent;
            }
        }
        return null;
    }

    private PendingTaskFragmentEvent getPendingTaskFragmentEvent(TaskFragment taskFragment, int i) {
        List<PendingTaskFragmentEvent> list = this.mPendingTaskFragmentEvents.get(taskFragment.getTaskFragmentOrganizer().asBinder());
        for (int size = list.size() - 1; size >= 0; size--) {
            PendingTaskFragmentEvent pendingTaskFragmentEvent = list.get(size);
            if (taskFragment == pendingTaskFragmentEvent.mTaskFragment && i == pendingTaskFragmentEvent.mEventType) {
                return pendingTaskFragmentEvent;
            }
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void dispatchPendingEvents() {
        if (this.mAtmService.mWindowManager.mWindowPlacerLocked.isLayoutDeferred() || this.mPendingTaskFragmentEvents.isEmpty()) {
            return;
        }
        int size = this.mPendingTaskFragmentEvents.size();
        for (int i = 0; i < size; i++) {
            dispatchPendingEvents(this.mTaskFragmentOrganizerState.get(this.mPendingTaskFragmentEvents.keyAt(i)), this.mPendingTaskFragmentEvents.valueAt(i));
        }
    }

    private void dispatchPendingEvents(TaskFragmentOrganizerState taskFragmentOrganizerState, List<PendingTaskFragmentEvent> list) {
        if (list.isEmpty() || shouldDeferPendingEvents(taskFragmentOrganizerState, list)) {
            return;
        }
        this.mTmpTaskSet.clear();
        int size = list.size();
        TaskFragmentTransaction taskFragmentTransaction = new TaskFragmentTransaction();
        for (int i = 0; i < size; i++) {
            PendingTaskFragmentEvent pendingTaskFragmentEvent = list.get(i);
            if (pendingTaskFragmentEvent.mEventType == 0 || pendingTaskFragmentEvent.mEventType == 2) {
                Task task = pendingTaskFragmentEvent.mTaskFragment.getTask();
                if (this.mTmpTaskSet.add(task)) {
                    taskFragmentTransaction.addChange(prepareChange(new PendingTaskFragmentEvent.Builder(3, taskFragmentOrganizerState.mOrganizer).setTask(task).build()));
                }
            }
            taskFragmentTransaction.addChange(prepareChange(pendingTaskFragmentEvent));
        }
        this.mTmpTaskSet.clear();
        taskFragmentOrganizerState.dispatchTransaction(taskFragmentTransaction);
        list.clear();
    }

    private boolean shouldDeferPendingEvents(TaskFragmentOrganizerState taskFragmentOrganizerState, List<PendingTaskFragmentEvent> list) {
        Task task;
        ArrayList arrayList = new ArrayList();
        ArrayList arrayList2 = new ArrayList();
        int size = list.size();
        for (int i = 0; i < size; i++) {
            PendingTaskFragmentEvent pendingTaskFragmentEvent = list.get(i);
            if (pendingTaskFragmentEvent.mEventType != 3 && pendingTaskFragmentEvent.mEventType != 2 && pendingTaskFragmentEvent.mEventType != 0) {
                return false;
            }
            if (pendingTaskFragmentEvent.mEventType == 3) {
                task = pendingTaskFragmentEvent.mTask;
            } else {
                task = pendingTaskFragmentEvent.mTaskFragment.getTask();
            }
            if ((task.lastActiveTime > pendingTaskFragmentEvent.mDeferTime && isTaskVisible(task, arrayList, arrayList2)) || shouldSendEventWhenTaskInvisible(task, taskFragmentOrganizerState, pendingTaskFragmentEvent)) {
                return false;
            }
            pendingTaskFragmentEvent.mDeferTime = task.lastActiveTime;
        }
        return true;
    }

    private static boolean isTaskVisible(Task task, ArrayList<Task> arrayList, ArrayList<Task> arrayList2) {
        if (arrayList.contains(task)) {
            return true;
        }
        if (arrayList2.contains(task)) {
            return false;
        }
        if (task.shouldBeVisible(null)) {
            arrayList.add(task);
            return true;
        }
        arrayList2.add(task);
        return false;
    }

    private boolean shouldSendEventWhenTaskInvisible(Task task, TaskFragmentOrganizerState taskFragmentOrganizerState, PendingTaskFragmentEvent pendingTaskFragmentEvent) {
        TaskFragmentParentInfo taskFragmentParentInfo = (TaskFragmentParentInfo) taskFragmentOrganizerState.mLastSentTaskFragmentParentInfos.get(task.mTaskId);
        if (taskFragmentParentInfo == null || taskFragmentParentInfo.isVisible()) {
            return true;
        }
        if (pendingTaskFragmentEvent.mEventType != 2) {
            return false;
        }
        TaskFragmentInfo taskFragmentInfo = (TaskFragmentInfo) taskFragmentOrganizerState.mLastSentTaskFragmentInfos.get(pendingTaskFragmentEvent.mTaskFragment);
        return taskFragmentInfo == null || taskFragmentInfo.isEmpty() != (pendingTaskFragmentEvent.mTaskFragment.getNonFinishingActivityCount() == 0);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void dispatchPendingInfoChangedEvent(TaskFragment taskFragment) {
        PendingTaskFragmentEvent pendingTaskFragmentEvent = getPendingTaskFragmentEvent(taskFragment, 2);
        if (pendingTaskFragmentEvent == null) {
            return;
        }
        ITaskFragmentOrganizer taskFragmentOrganizer = taskFragment.getTaskFragmentOrganizer();
        TaskFragmentOrganizerState validateAndGetState = validateAndGetState(taskFragmentOrganizer);
        TaskFragmentTransaction taskFragmentTransaction = new TaskFragmentTransaction();
        taskFragmentTransaction.addChange(prepareChange(new PendingTaskFragmentEvent.Builder(3, taskFragmentOrganizer).setTask(taskFragment.getTask()).build()));
        taskFragmentTransaction.addChange(prepareChange(pendingTaskFragmentEvent));
        validateAndGetState.dispatchTransaction(taskFragmentTransaction);
        this.mPendingTaskFragmentEvents.get(taskFragmentOrganizer.asBinder()).remove(pendingTaskFragmentEvent);
    }

    private TaskFragmentTransaction.Change prepareChange(PendingTaskFragmentEvent pendingTaskFragmentEvent) {
        ITaskFragmentOrganizer iTaskFragmentOrganizer = pendingTaskFragmentEvent.mTaskFragmentOrg;
        TaskFragment taskFragment = pendingTaskFragmentEvent.mTaskFragment;
        TaskFragmentOrganizerState taskFragmentOrganizerState = this.mTaskFragmentOrganizerState.get(iTaskFragmentOrganizer.asBinder());
        if (taskFragmentOrganizerState == null) {
            return null;
        }
        int i = pendingTaskFragmentEvent.mEventType;
        if (i == 0) {
            return taskFragmentOrganizerState.prepareTaskFragmentAppeared(taskFragment);
        }
        if (i == 1) {
            return taskFragmentOrganizerState.prepareTaskFragmentVanished(taskFragment);
        }
        if (i == 2) {
            return taskFragmentOrganizerState.prepareTaskFragmentInfoChanged(taskFragment);
        }
        if (i == 3) {
            return taskFragmentOrganizerState.prepareTaskFragmentParentInfoChanged(pendingTaskFragmentEvent.mTask);
        }
        if (i == 4) {
            return taskFragmentOrganizerState.prepareTaskFragmentError(pendingTaskFragmentEvent.mErrorCallbackToken, taskFragment, pendingTaskFragmentEvent.mOpType, pendingTaskFragmentEvent.mException);
        }
        if (i == 5) {
            return taskFragmentOrganizerState.prepareActivityReparentedToTask(pendingTaskFragmentEvent.mActivity);
        }
        throw new IllegalArgumentException("Unknown TaskFragmentEvent=" + pendingTaskFragmentEvent.mEventType);
    }

    public boolean isActivityEmbedded(IBinder iBinder) {
        WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
        WindowManagerService.boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                ActivityRecord forTokenLocked = ActivityRecord.forTokenLocked(iBinder);
                if (forTokenLocked == null) {
                    WindowManagerService.resetPriorityAfterLockedSection();
                    return false;
                }
                TaskFragment organizedTaskFragment = forTokenLocked.getOrganizedTaskFragment();
                if (organizedTaskFragment == null) {
                    WindowManagerService.resetPriorityAfterLockedSection();
                    return false;
                }
                boolean hookIsActivityEmbedded = organizedTaskFragment.mTaskFragmentExt.hookIsActivityEmbedded(organizedTaskFragment.isEmbeddedWithBoundsOverride(), organizedTaskFragment, forTokenLocked);
                WindowManagerService.resetPriorityAfterLockedSection();
                return hookIsActivityEmbedded;
            } catch (Throwable th) {
                WindowManagerService.resetPriorityAfterLockedSection();
                throw th;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static Intent trimIntent(Intent intent) {
        return new Intent().setComponent(intent.getComponent()).setPackage(intent.getPackage()).setAction(intent.getAction());
    }
}
