package com.android.server.wm;

import android.R;
import android.app.ActivityManager;
import android.app.ActivityTaskManager;
import android.app.AppGlobals;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.IPackageManager;
import android.content.pm.ParceledListSlice;
import android.content.pm.UserInfo;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.os.Debug;
import android.os.Environment;
import android.os.IBinder;
import android.os.RemoteException;
import android.os.SystemProperties;
import android.os.UserHandle;
import android.text.TextUtils;
import android.util.ArraySet;
import android.util.Slog;
import android.util.SparseArray;
import android.util.SparseBooleanArray;
import android.view.MotionEvent;
import android.view.WindowManagerPolicyConstants;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.protolog.ProtoLogGroup;
import com.android.internal.protolog.ProtoLogImpl;
import com.android.internal.util.function.pooled.PooledLambda;
import com.android.server.wm.ActivityRecord;
import com.android.server.wm.RecentTasks;
import com.google.android.collect.Sets;
import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import system.ext.loader.core.ExtLoader;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
public class RecentTasks {
    private static final int DEFAULT_INITIAL_CAPACITY = 5;
    private long mActiveTasksSessionDurationMs;
    private final ArrayList<Callbacks> mCallbacks;
    private boolean mCheckTrimmableTasksOnIdle;
    private String mFeatureId;
    private boolean mFreezeTaskListReordering;
    private long mFreezeTaskListTimeoutMs;
    private int mGlobalMaxNumTasks;
    private boolean mHasVisibleRecentTasks;
    private final ArrayList<Task> mHiddenTasks;
    private final WindowManagerPolicyConstants.PointerEventListener mListener;
    private int mMaxNumVisibleTasks;
    private int mMinNumVisibleTasks;
    private final SparseArray<SparseBooleanArray> mPersistedTaskIds;
    IRecentTasksExt mRecentTasksExt;
    private final RecentTasksWrapper mRecentTasksWrapper;
    private ComponentName mRecentsComponent;
    private int mRecentsUid;
    private final Runnable mResetFreezeTaskListOnTimeoutRunnable;
    private final ActivityTaskManagerService mService;
    public IRecentTasksSocExt mSocExt;
    private final ActivityTaskSupervisor mSupervisor;
    private TaskChangeNotificationController mTaskNotificationController;
    private final TaskPersister mTaskPersister;
    private final ArrayList<Task> mTasks;
    private final HashMap<ComponentName, ActivityInfo> mTmpAvailActCache;
    private final HashMap<String, ApplicationInfo> mTmpAvailAppCache;
    private final SparseBooleanArray mTmpQuietProfileUserIds;
    private final ArrayList<Task> mTmpRecents;
    private final SparseBooleanArray mUsersWithRecentsLoaded;
    private static final String TAG = "ActivityTaskManager";
    private static final String TAG_RECENTS = TAG + ActivityTaskManagerDebugConfig.POSTFIX_RECENTS;
    private static final String TAG_TASKS = TAG + ActivityTaskManagerDebugConfig.POSTFIX_TASKS;
    private static final long FREEZE_TASK_LIST_TIMEOUT_MS = TimeUnit.SECONDS.toMillis(5);
    private static final Comparator<Task> TASK_ID_COMPARATOR = new Comparator() { // from class: com.android.server.wm.RecentTasks$$ExternalSyntheticLambda1
        @Override // java.util.Comparator
        public final int compare(Object obj, Object obj2) {
            int lambda$static$0;
            lambda$static$0 = RecentTasks.lambda$static$0((Task) obj, (Task) obj2);
            return lambda$static$0;
        }
    };
    private static final ActivityInfo NO_ACTIVITY_INFO_TOKEN = new ActivityInfo();
    private static final ApplicationInfo NO_APPLICATION_INFO_TOKEN = new ApplicationInfo();

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
    public interface Callbacks {
        void onRecentTaskAdded(Task task);

        void onRecentTaskRemoved(Task task, boolean z, boolean z2);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ int lambda$static$0(Task task, Task task2) {
        return task2.mTaskId - task.mTaskId;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: com.android.server.wm.RecentTasks$1, reason: invalid class name */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
    public class AnonymousClass1 implements WindowManagerPolicyConstants.PointerEventListener {
        AnonymousClass1() {
        }

        public void onPointerEvent(MotionEvent motionEvent) {
            if (RecentTasks.this.mService.mH.hasCallbacks(RecentTasks.this.mResetFreezeTaskListOnTimeoutRunnable) && RecentTasks.this.mRecentTasksExt.skipResetFreezeTaskListReordering(motionEvent)) {
                Slog.d(RecentTasks.TAG, "Ignore resetFreezeTaskListReordering while touching from touchpad!");
                return;
            }
            if (RecentTasks.this.mFreezeTaskListReordering && motionEvent.getAction() == 0 && motionEvent.getClassification() != 4) {
                final int displayId = motionEvent.getDisplayId();
                final int x = (int) motionEvent.getX();
                final int y = (int) motionEvent.getY();
                RecentTasks.this.mService.mH.post(PooledLambda.obtainRunnable(new Consumer() { // from class: com.android.server.wm.RecentTasks$1$$ExternalSyntheticLambda0
                    @Override // java.util.function.Consumer
                    public final void accept(Object obj) {
                        RecentTasks.AnonymousClass1.this.lambda$onPointerEvent$0(displayId, x, y, obj);
                    }
                }, (Object) null).recycleOnUse());
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onPointerEvent$0(int i, int i2, int i3, Object obj) {
            WindowManagerGlobalLock windowManagerGlobalLock = RecentTasks.this.mService.mGlobalLock;
            WindowManagerService.boostPriorityForLockedSection();
            synchronized (windowManagerGlobalLock) {
                try {
                    if (RecentTasks.this.mService.mRootWindowContainer.getDisplayContent(i).mDisplayContent.getWrapper().getExtImpl().pointWithinAppWindow(i2, i3)) {
                        Task topDisplayFocusedRootTask = RecentTasks.this.mService.getTopDisplayFocusedRootTask();
                        Task topMostTask = topDisplayFocusedRootTask != null ? topDisplayFocusedRootTask.getTopMostTask() : null;
                        if (RecentTasks.this.mFreezeTaskListReordering) {
                            Slog.i(RecentTasks.TAG, "onPointerEvent in app window x=" + i2 + " y=" + i3 + " topTask:" + topMostTask);
                        }
                        RecentTasks.this.resetFreezeTaskListReordering(topMostTask);
                    }
                } catch (Throwable th) {
                    WindowManagerService.resetPriorityAfterLockedSection();
                    throw th;
                }
            }
            WindowManagerService.resetPriorityAfterLockedSection();
        }
    }

    @VisibleForTesting
    RecentTasks(ActivityTaskManagerService activityTaskManagerService, TaskPersister taskPersister) {
        this.mRecentsUid = -1;
        this.mRecentsComponent = null;
        this.mUsersWithRecentsLoaded = new SparseBooleanArray(5);
        this.mPersistedTaskIds = new SparseArray<>(5);
        this.mTasks = new ArrayList<>();
        this.mCallbacks = new ArrayList<>();
        this.mHiddenTasks = new ArrayList<>();
        this.mFreezeTaskListTimeoutMs = FREEZE_TASK_LIST_TIMEOUT_MS;
        this.mTmpRecents = new ArrayList<>();
        this.mTmpAvailActCache = new HashMap<>();
        this.mTmpAvailAppCache = new HashMap<>();
        this.mTmpQuietProfileUserIds = new SparseBooleanArray();
        this.mRecentTasksExt = (IRecentTasksExt) ExtLoader.type(IRecentTasksExt.class).base(this).create();
        this.mSocExt = (IRecentTasksSocExt) ExtLoader.type(IRecentTasksSocExt.class).base(this).create();
        this.mListener = new AnonymousClass1();
        this.mResetFreezeTaskListOnTimeoutRunnable = new Runnable() { // from class: com.android.server.wm.RecentTasks$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                RecentTasks.this.resetFreezeTaskListReorderingOnTimeout();
            }
        };
        this.mRecentTasksWrapper = new RecentTasksWrapper();
        this.mService = activityTaskManagerService;
        this.mSupervisor = activityTaskManagerService.mTaskSupervisor;
        this.mTaskPersister = taskPersister;
        this.mGlobalMaxNumTasks = ActivityTaskManager.getMaxRecentTasksStatic();
        this.mHasVisibleRecentTasks = true;
        this.mTaskNotificationController = activityTaskManagerService.getTaskChangeNotificationController();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public RecentTasks(ActivityTaskManagerService activityTaskManagerService, ActivityTaskSupervisor activityTaskSupervisor) {
        this.mRecentsUid = -1;
        this.mRecentsComponent = null;
        this.mUsersWithRecentsLoaded = new SparseBooleanArray(5);
        this.mPersistedTaskIds = new SparseArray<>(5);
        this.mTasks = new ArrayList<>();
        this.mCallbacks = new ArrayList<>();
        this.mHiddenTasks = new ArrayList<>();
        this.mFreezeTaskListTimeoutMs = FREEZE_TASK_LIST_TIMEOUT_MS;
        this.mTmpRecents = new ArrayList<>();
        this.mTmpAvailActCache = new HashMap<>();
        this.mTmpAvailAppCache = new HashMap<>();
        this.mTmpQuietProfileUserIds = new SparseBooleanArray();
        this.mRecentTasksExt = (IRecentTasksExt) ExtLoader.type(IRecentTasksExt.class).base(this).create();
        this.mSocExt = (IRecentTasksSocExt) ExtLoader.type(IRecentTasksSocExt.class).base(this).create();
        this.mListener = new AnonymousClass1();
        this.mResetFreezeTaskListOnTimeoutRunnable = new Runnable() { // from class: com.android.server.wm.RecentTasks$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                RecentTasks.this.resetFreezeTaskListReorderingOnTimeout();
            }
        };
        this.mRecentTasksWrapper = new RecentTasksWrapper();
        File dataSystemDirectory = Environment.getDataSystemDirectory();
        Resources resources = activityTaskManagerService.mContext.getResources();
        this.mService = activityTaskManagerService;
        this.mSupervisor = activityTaskManagerService.mTaskSupervisor;
        this.mTaskPersister = new TaskPersister(dataSystemDirectory, activityTaskSupervisor, activityTaskManagerService, this, activityTaskSupervisor.mPersisterQueue);
        this.mGlobalMaxNumTasks = ActivityTaskManager.getMaxRecentTasksStatic();
        this.mTaskNotificationController = activityTaskManagerService.getTaskChangeNotificationController();
        this.mHasVisibleRecentTasks = resources.getBoolean(17891710);
        loadParametersFromResources(resources);
    }

    @VisibleForTesting
    void setParameters(int i, int i2, long j) {
        this.mMinNumVisibleTasks = i;
        this.mMaxNumVisibleTasks = i2;
        this.mActiveTasksSessionDurationMs = j;
    }

    @VisibleForTesting
    void setGlobalMaxNumTasks(int i) {
        this.mGlobalMaxNumTasks = i;
    }

    @VisibleForTesting
    void setFreezeTaskListTimeout(long j) {
        this.mFreezeTaskListTimeoutMs = j;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public WindowManagerPolicyConstants.PointerEventListener getInputListener() {
        return this.mListener;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setFreezeTaskListReordering() {
        if (!this.mFreezeTaskListReordering) {
            this.mTaskNotificationController.notifyTaskListFrozen(true);
            this.mFreezeTaskListReordering = true;
        }
        this.mService.mH.removeCallbacks(this.mResetFreezeTaskListOnTimeoutRunnable);
        this.mService.mH.postDelayed(this.mResetFreezeTaskListOnTimeoutRunnable, this.mFreezeTaskListTimeoutMs);
    }

    void resetFreezeTaskListReordering(Task task) {
        if (this.mFreezeTaskListReordering) {
            this.mFreezeTaskListReordering = false;
            this.mService.mH.removeCallbacks(this.mResetFreezeTaskListOnTimeoutRunnable);
            if (task != null) {
                this.mTasks.remove(task);
                this.mTasks.add(0, task);
            }
            trimInactiveRecentTasks();
            this.mTaskNotificationController.notifyTaskStackChanged();
            this.mTaskNotificationController.notifyTaskListFrozen(false);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @VisibleForTesting
    public void resetFreezeTaskListReorderingOnTimeout() {
        WindowManagerGlobalLock windowManagerGlobalLock = this.mService.mGlobalLock;
        WindowManagerService.boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                Task topDisplayFocusedRootTask = this.mService.getTopDisplayFocusedRootTask();
                Task task = null;
                Task topMostTask = topDisplayFocusedRootTask != null ? topDisplayFocusedRootTask.getTopMostTask() : null;
                if (topMostTask != null && topMostTask.hasChild()) {
                    task = topMostTask;
                }
                resetFreezeTaskListReordering(task);
            } catch (Throwable th) {
                WindowManagerService.resetPriorityAfterLockedSection();
                throw th;
            }
        }
        WindowManagerService.resetPriorityAfterLockedSection();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @VisibleForTesting
    public boolean isFreezeTaskListReorderingSet() {
        return this.mFreezeTaskListReordering;
    }

    @VisibleForTesting
    void loadParametersFromResources(Resources resources) {
        if (ActivityManager.isLowRamDeviceStatic()) {
            this.mMinNumVisibleTasks = resources.getInteger(R.integer.config_screen_rotation_total_90);
            this.mMaxNumVisibleTasks = resources.getInteger(R.integer.config_screenBrightnessDoze);
        } else if (SystemProperties.getBoolean("ro.recents.grid", false)) {
            this.mMinNumVisibleTasks = resources.getInteger(R.integer.config_screen_rotation_total_180);
            this.mMaxNumVisibleTasks = resources.getInteger(R.integer.config_screenBrightnessDim);
        } else {
            this.mMinNumVisibleTasks = resources.getInteger(R.integer.config_screen_rotation_fade_out);
            this.mMaxNumVisibleTasks = resources.getInteger(R.integer.config_screenBrightnessDark);
        }
        int integer = resources.getInteger(R.integer.config_activityShortDur);
        this.mActiveTasksSessionDurationMs = integer > 0 ? TimeUnit.HOURS.toMillis(integer) : -1L;
    }

    void loadRecentsComponent(Resources resources) {
        ComponentName unflattenFromString;
        String string = resources.getString(R.string.decline_remote_bugreport_action);
        if (TextUtils.isEmpty(string) || (unflattenFromString = ComponentName.unflattenFromString(string)) == null) {
            return;
        }
        try {
            ApplicationInfo applicationInfo = AppGlobals.getPackageManager().getApplicationInfo(unflattenFromString.getPackageName(), 8704L, this.mService.mContext.getUserId());
            if (applicationInfo != null) {
                this.mRecentsUid = applicationInfo.uid;
                this.mRecentsComponent = unflattenFromString;
            }
        } catch (RemoteException unused) {
            Slog.w(TAG, "Could not load application info for recents component: " + unflattenFromString);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean isCallerRecents(int i) {
        return UserHandle.isSameApp(i, this.mRecentsUid);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean isRecentsComponent(ComponentName componentName, int i) {
        return componentName.equals(this.mRecentsComponent) && UserHandle.isSameApp(i, this.mRecentsUid);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean isRecentsComponentHomeActivity(int i) {
        ComponentName defaultHomeActivity = this.mService.getPackageManagerInternalLocked().getDefaultHomeActivity(i);
        return (defaultHomeActivity == null || this.mRecentsComponent == null || !defaultHomeActivity.getPackageName().equals(this.mRecentsComponent.getPackageName())) ? false : true;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public ComponentName getRecentsComponent() {
        return this.mRecentsComponent;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public String getRecentsComponentFeatureId() {
        return this.mFeatureId;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int getRecentsComponentUid() {
        return this.mRecentsUid;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void registerCallback(Callbacks callbacks) {
        this.mCallbacks.add(callbacks);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void unregisterCallback(Callbacks callbacks) {
        this.mCallbacks.remove(callbacks);
    }

    private void notifyTaskAdded(Task task) {
        Slog.d(TAG_RECENTS, "notifyTaskAdded, task: " + task + " trace:" + Debug.getCallers(5));
        for (int i = 0; i < this.mCallbacks.size(); i++) {
            this.mCallbacks.get(i).onRecentTaskAdded(task);
        }
        this.mTaskNotificationController.notifyTaskListUpdated();
    }

    private void notifyTaskRemoved(Task task, boolean z, boolean z2) {
        Slog.d(TAG_RECENTS, "notifyTaskRemoved, task: " + task + " trace:" + Debug.getCallers(5));
        for (int i = 0; i < this.mCallbacks.size(); i++) {
            this.mCallbacks.get(i).onRecentTaskRemoved(task, z, z2);
        }
        this.mTaskNotificationController.notifyTaskListUpdated();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void loadUserRecentsLocked(int i) {
        if (this.mUsersWithRecentsLoaded.get(i)) {
            return;
        }
        loadPersistedTaskIdsForUserLocked(i);
        SparseBooleanArray sparseBooleanArray = new SparseBooleanArray();
        Iterator<Task> it = this.mTasks.iterator();
        while (it.hasNext()) {
            Task next = it.next();
            if (next.mUserId == i && shouldPersistTaskLocked(next)) {
                sparseBooleanArray.put(next.mTaskId, true);
            }
        }
        Slog.i(TAG, "Loading recents for user " + i + " into memory.");
        this.mTasks.addAll(this.mTaskPersister.restoreTasksForUserLocked(i, sparseBooleanArray));
        cleanupLocked(i);
        this.mUsersWithRecentsLoaded.put(i, true);
        if (sparseBooleanArray.size() > 0) {
            syncPersistentTaskIdsLocked();
        }
    }

    private void loadPersistedTaskIdsForUserLocked(int i) {
        if (this.mPersistedTaskIds.get(i) == null) {
            this.mPersistedTaskIds.put(i, this.mTaskPersister.loadPersistedTaskIdsForUser(i));
            Slog.i(TAG, "Loaded persisted task ids for user " + i);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean containsTaskId(int i, int i2) {
        loadPersistedTaskIdsForUserLocked(i2);
        return this.mPersistedTaskIds.get(i2).get(i);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public SparseBooleanArray getTaskIdsForUser(int i) {
        loadPersistedTaskIdsForUserLocked(i);
        return this.mPersistedTaskIds.get(i);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void notifyTaskPersisterLocked(Task task, boolean z) {
        Task rootTask = task != null ? task.getRootTask() : null;
        if ((rootTask == null || !rootTask.isActivityTypeHomeOrRecents()) && !this.mRecentTasksExt.skipPersistMultiSearchTask(task)) {
            if (task == null || !task.getWrapper().getExtImpl().isCreateForSingleSplit()) {
                syncPersistentTaskIdsLocked();
                this.mTaskPersister.wakeup(task, z);
            }
        }
    }

    private void syncPersistentTaskIdsLocked() {
        for (int size = this.mPersistedTaskIds.size() - 1; size >= 0; size--) {
            if (this.mUsersWithRecentsLoaded.get(this.mPersistedTaskIds.keyAt(size))) {
                this.mPersistedTaskIds.valueAt(size).clear();
            }
        }
        for (int size2 = this.mTasks.size() - 1; size2 >= 0; size2--) {
            Task task = this.mTasks.get(size2);
            if (shouldPersistTaskLocked(task)) {
                if (this.mPersistedTaskIds.get(task.mUserId) == null) {
                    Slog.wtf(TAG, "No task ids found for userId " + task.mUserId + ". task=" + task + " mPersistedTaskIds=" + this.mPersistedTaskIds);
                    this.mPersistedTaskIds.put(task.mUserId, new SparseBooleanArray());
                }
                this.mPersistedTaskIds.get(task.mUserId).put(task.mTaskId, true);
            }
        }
    }

    private static boolean shouldPersistTaskLocked(Task task) {
        Task rootTask = task.getRootTask();
        return task.isPersistable && (rootTask == null || !rootTask.isActivityTypeHomeOrRecents());
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void onSystemReadyLocked() {
        loadRecentsComponent(this.mService.mContext.getResources());
        this.mTasks.clear();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Bitmap getTaskDescriptionIcon(String str) {
        return this.mTaskPersister.getTaskDescriptionIcon(str);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void saveImage(Bitmap bitmap, String str) {
        this.mTaskPersister.saveImage(bitmap, str);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void flush() {
        WindowManagerGlobalLock windowManagerGlobalLock = this.mService.mGlobalLock;
        WindowManagerService.boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                syncPersistentTaskIdsLocked();
            } catch (Throwable th) {
                WindowManagerService.resetPriorityAfterLockedSection();
                throw th;
            }
        }
        WindowManagerService.resetPriorityAfterLockedSection();
        this.mTaskPersister.flush();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int[] usersWithRecentsLoadedLocked() {
        int size = this.mUsersWithRecentsLoaded.size();
        int[] iArr = new int[size];
        int i = 0;
        for (int i2 = 0; i2 < size; i2++) {
            int keyAt = this.mUsersWithRecentsLoaded.keyAt(i2);
            if (this.mUsersWithRecentsLoaded.valueAt(i2)) {
                iArr[i] = keyAt;
                i++;
            }
        }
        return i < size ? Arrays.copyOf(iArr, i) : iArr;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void unloadUserDataFromMemoryLocked(int i) {
        if (this.mUsersWithRecentsLoaded.get(i)) {
            Slog.i(TAG, "Unloading recents for user " + i + " from memory.");
            this.mUsersWithRecentsLoaded.delete(i);
            removeTasksForUserLocked(i);
        }
        this.mPersistedTaskIds.delete(i);
        this.mTaskPersister.unloadUserDataFromMemory(i);
    }

    private void removeTasksForUserLocked(int i) {
        if (i <= 0) {
            Slog.i(TAG, "Can't remove recent task on user " + i);
            return;
        }
        for (int size = this.mTasks.size() - 1; size >= 0; size--) {
            Task task = this.mTasks.get(size);
            if (task.mUserId == i) {
                if (ProtoLogCache.WM_DEBUG_TASKS_enabled) {
                    ProtoLogImpl.i(ProtoLogGroup.WM_DEBUG_TASKS, -1647332198, 4, (String) null, new Object[]{String.valueOf(task), Long.valueOf(i)});
                }
                remove(task);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void onPackagesSuspendedChanged(String[] strArr, boolean z, int i) {
        HashSet newHashSet = Sets.newHashSet(strArr);
        for (int size = this.mTasks.size() - 1; size >= 0; size--) {
            Task task = this.mTasks.get(size);
            ComponentName componentName = task.realActivity;
            if (componentName != null && newHashSet.contains(componentName.getPackageName()) && task.mUserId == i && task.realActivitySuspended != z) {
                task.realActivitySuspended = z;
                if (z) {
                    this.mSupervisor.removeTask(task, false, true, "suspended-package");
                }
                notifyTaskPersisterLocked(task, false);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void onLockTaskModeStateChanged(int i, int i2) {
        if (i != 1) {
            return;
        }
        for (int size = this.mTasks.size() - 1; size >= 0; size--) {
            Task task = this.mTasks.get(size);
            if (task.mUserId == i2) {
                this.mService.getLockTaskController();
                if (!LockTaskController.isTaskAuthAllowlisted(task.mLockTaskAuth)) {
                    remove(task);
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void removeTasksByPackageName(String str, int i) {
        for (int size = this.mTasks.size() - 1; size >= 0; size--) {
            Task task = this.mTasks.get(size);
            if (task.mUserId == i && task.getBasePackageName().equals(str)) {
                this.mSupervisor.removeTask(task, true, true, "remove-package-task");
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void removeAllVisibleTasks(int i) {
        Set<Integer> profileIds = getProfileIds(i);
        for (int size = this.mTasks.size() - 1; size >= 0; size--) {
            Task task = this.mTasks.get(size);
            if (profileIds.contains(Integer.valueOf(task.mUserId)) && isVisibleRecentTask(task)) {
                this.mTasks.remove(size);
                notifyTaskRemoved(task, true, true);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void cleanupDisabledPackageTasksLocked(String str, Set<String> set, int i) {
        for (int size = this.mTasks.size() - 1; size >= 0; size--) {
            Task task = this.mTasks.get(size);
            if (i == -1 || task.mUserId == i) {
                Intent intent = task.intent;
                ComponentName component = intent != null ? intent.getComponent() : null;
                if (component != null && component.getPackageName().equals(str) && (set == null || set.contains(component.getClassName()))) {
                    this.mSupervisor.removeTask(task, false, true, "disabled-package");
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void cleanupLocked(int i) {
        int i2;
        int size = this.mTasks.size();
        if (size == 0) {
            return;
        }
        this.mTmpAvailActCache.clear();
        this.mTmpAvailAppCache.clear();
        IPackageManager packageManager = AppGlobals.getPackageManager();
        int i3 = size - 1;
        while (true) {
            i2 = 0;
            if (i3 < 0) {
                break;
            }
            Task task = this.mTasks.get(i3);
            if (i == -1 || task.mUserId == i) {
                if (task.autoRemoveRecents && task.getTopNonFinishingActivity() == null) {
                    remove(task);
                    Slog.w(TAG, "Removing auto-remove without activity: " + task);
                } else {
                    ComponentName componentName = task.realActivity;
                    if (componentName != null) {
                        ActivityInfo activityInfo = this.mTmpAvailActCache.get(componentName);
                        if (activityInfo == null) {
                            try {
                                activityInfo = packageManager.getActivityInfo(task.realActivity, 268436480L, i);
                                if (activityInfo == null) {
                                    activityInfo = NO_ACTIVITY_INFO_TOKEN;
                                }
                                this.mTmpAvailActCache.put(task.realActivity, activityInfo);
                            } catch (RemoteException unused) {
                            }
                        }
                        if (activityInfo == NO_ACTIVITY_INFO_TOKEN) {
                            ApplicationInfo applicationInfo = this.mTmpAvailAppCache.get(task.realActivity.getPackageName());
                            if (applicationInfo == null) {
                                applicationInfo = packageManager.getApplicationInfo(task.realActivity.getPackageName(), 8192L, i);
                                if (applicationInfo == null) {
                                    applicationInfo = NO_APPLICATION_INFO_TOKEN;
                                }
                                this.mTmpAvailAppCache.put(task.realActivity.getPackageName(), applicationInfo);
                            }
                            if (applicationInfo == NO_APPLICATION_INFO_TOKEN || (applicationInfo.flags & 8388608) == 0) {
                                remove(task);
                                Slog.w(TAG, "Removing no longer valid recent: " + task);
                            } else {
                                if (ActivityTaskManagerDebugConfig.DEBUG_RECENTS && task.isAvailable) {
                                    Slog.d(TAG_RECENTS, "Making recent unavailable: " + task);
                                }
                                task.isAvailable = false;
                            }
                        } else {
                            if (activityInfo.enabled) {
                                ApplicationInfo applicationInfo2 = activityInfo.applicationInfo;
                                if (applicationInfo2.enabled && (applicationInfo2.flags & 8388608) != 0) {
                                    if (ActivityTaskManagerDebugConfig.DEBUG_RECENTS && !task.isAvailable) {
                                        Slog.d(TAG_RECENTS, "Making recent available: " + task);
                                    }
                                    task.isAvailable = true;
                                }
                            }
                            if (ActivityTaskManagerDebugConfig.DEBUG_RECENTS && task.isAvailable) {
                                Slog.d(TAG_RECENTS, "Making recent unavailable: " + task + " (enabled=" + activityInfo.enabled + "/" + activityInfo.applicationInfo.enabled + " flags=" + Integer.toHexString(activityInfo.applicationInfo.flags) + ")");
                            }
                            task.isAvailable = false;
                        }
                    }
                }
            }
            i3--;
        }
        int size2 = this.mTasks.size();
        while (i2 < size2) {
            i2 = processNextAffiliateChainLocked(i2);
        }
    }

    private boolean canAddTaskWithoutTrim(Task task) {
        return findRemoveIndexForAddTask(task) == -1;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public ArrayList<IBinder> getAppTasksList(int i, String str) {
        ArrayList<IBinder> arrayList = new ArrayList<>();
        int size = this.mTasks.size();
        for (int i2 = 0; i2 < size; i2++) {
            Task task = this.mTasks.get(i2);
            if (task.effectiveUid == i && str.equals(task.getBasePackageName())) {
                arrayList.add(new AppTaskImpl(this.mService, task.mTaskId, i).asBinder());
            }
        }
        return arrayList;
    }

    @VisibleForTesting
    Set<Integer> getProfileIds(int i) {
        ArraySet arraySet = new ArraySet();
        for (int i2 : this.mService.getUserManager().getProfileIds(i, false)) {
            arraySet.add(Integer.valueOf(i2));
        }
        return arraySet;
    }

    @VisibleForTesting
    UserInfo getUserInfo(int i) {
        return this.mService.getUserManager().getUserInfo(i);
    }

    @VisibleForTesting
    int[] getCurrentProfileIds() {
        return this.mService.mAmInternal.getCurrentProfileIds();
    }

    @VisibleForTesting
    boolean isUserRunning(int i, int i2) {
        return this.mService.mAmInternal.isUserRunning(i, i2);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public ParceledListSlice<ActivityManager.RecentTaskInfo> getRecentTasks(int i, int i2, boolean z, int i3, int i4) {
        return new ParceledListSlice<>(getRecentTasksImpl(i, i2, z, i3, i4));
    }

    private ArrayList<ActivityManager.RecentTaskInfo> getRecentTasksImpl(int i, int i2, boolean z, int i3, int i4) {
        boolean z2 = (i2 & 1) != 0;
        if (!isUserRunning(i3, 4)) {
            Slog.i(TAG, "user " + i3 + " is still locked. Cannot load recents");
            return new ArrayList<>();
        }
        loadUserRecentsLocked(i3);
        Set<Integer> profileIds = getProfileIds(i3);
        profileIds.add(Integer.valueOf(i3));
        ArrayList<ActivityManager.RecentTaskInfo> arrayList = new ArrayList<>();
        int size = this.mTasks.size();
        int i5 = 0;
        for (int i6 = 0; i6 < size; i6++) {
            Task task = this.mTasks.get(i6);
            if (isVisibleRecentTask(task)) {
                i5++;
                if (!isInVisibleRange(task, i6, i5, z2)) {
                    Slog.d(TAG_RECENTS, "Skipping, invisible task by policy range: " + task);
                } else if (arrayList.size() >= i) {
                    if (ActivityTaskManagerDebugConfig.DEBUG_RECENTS) {
                        Slog.d(TAG_RECENTS, "Skipping, task num reach the requested size: " + task);
                    }
                } else if (!profileIds.contains(Integer.valueOf(task.mUserId))) {
                    if (ActivityTaskManagerDebugConfig.DEBUG_RECENTS) {
                        Slog.d(TAG_RECENTS, "Skipping, not user: " + task);
                    }
                } else if (task.realActivitySuspended) {
                    if (ActivityTaskManagerDebugConfig.DEBUG_RECENTS) {
                        Slog.d(TAG_RECENTS, "Skipping, activity suspended: " + task);
                    }
                } else if (!z && !task.isActivityTypeHome() && task.effectiveUid != i4) {
                    if (ActivityTaskManagerDebugConfig.DEBUG_RECENTS) {
                        Slog.d(TAG_RECENTS, "Skipping, not allowed: " + task);
                    }
                } else if (task.autoRemoveRecents && task.getTopNonFinishingActivity() == null) {
                    if (ActivityTaskManagerDebugConfig.DEBUG_RECENTS) {
                        Slog.d(TAG_RECENTS, "Skipping, auto-remove without activity: " + task);
                    }
                } else if ((i2 & 2) != 0 && !task.isAvailable) {
                    if (ActivityTaskManagerDebugConfig.DEBUG_RECENTS) {
                        Slog.d(TAG_RECENTS, "Skipping, unavail real act: " + task);
                    }
                } else if (!task.mUserSetupComplete && !this.mRecentTasksExt.reCheckUserSetupComplete(task)) {
                    Slog.d(TAG_RECENTS, "Skipping, user setup not complete: " + task);
                } else if (!this.mRecentTasksExt.skipMultiSearchTask(task) && !this.mRecentTasksExt.skipShowRecentTask(task, i4) && !this.mRecentTasksExt.getRecentTasksImpl(task.getBaseIntent()) && !((IMirageWindowManagerExt) ExtLoader.type(IMirageWindowManagerExt.class).create()).shouldHideTaskInRecents(task)) {
                    if (this.mRecentTasksExt.skipPreloadingTaskInRecents(task)) {
                        Slog.d(TAG, "getRecentTasksImpl skip in ActivityPreloading!");
                    } else {
                        arrayList.add(createRecentTaskInfo(task, true, z));
                    }
                }
            } else {
                Slog.d(TAG_RECENTS, "Skipping, invisible task: " + task);
            }
        }
        Slog.d(TAG_RECENTS, "after skip size " + arrayList.size());
        return arrayList;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void getPersistableTaskIds(ArraySet<Integer> arraySet) {
        int size = this.mTasks.size();
        for (int i = 0; i < size; i++) {
            Task task = this.mTasks.get(i);
            Task rootTask = task.getRootTask();
            if ((task.isPersistable || task.inRecents) && (rootTask == null || !rootTask.isActivityTypeHomeOrRecents())) {
                arraySet.add(Integer.valueOf(task.mTaskId));
            }
        }
    }

    @VisibleForTesting
    ArrayList<Task> getRawTasks() {
        return this.mTasks;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public SparseBooleanArray getRecentTaskIds() {
        SparseBooleanArray sparseBooleanArray = new SparseBooleanArray();
        int size = this.mTasks.size();
        int i = 0;
        for (int i2 = 0; i2 < size; i2++) {
            Task task = this.mTasks.get(i2);
            if (isVisibleRecentTask(task)) {
                i++;
                if (isInVisibleRange(task, i2, i, false)) {
                    sparseBooleanArray.put(task.mTaskId, true);
                }
            }
        }
        return sparseBooleanArray;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Task getTask(int i) {
        int size = this.mTasks.size();
        for (int i2 = 0; i2 < size; i2++) {
            Task task = this.mTasks.get(i2);
            if (task.mTaskId == i) {
                return task;
            }
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: Removed duplicated region for block: B:100:0x01fb  */
    /* JADX WARN: Removed duplicated region for block: B:60:0x013e  */
    /* JADX WARN: Removed duplicated region for block: B:88:0x0213  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void add(Task task) {
        boolean z;
        if (ActivityTaskManagerDebugConfig.DEBUG_RECENTS_TRIM_TASKS) {
            Slog.d(TAG, "add: task=" + task);
        }
        boolean z2 = (task.mAffiliatedTaskId == task.mTaskId && task.mNextAffiliateTaskId == -1 && task.mPrevAffiliateTaskId == -1) ? false : true;
        int size = this.mTasks.size();
        if (task.voiceSession != null) {
            if (ActivityTaskManagerDebugConfig.DEBUG_RECENTS) {
                Slog.d(TAG_RECENTS, "addRecent: not adding voice interaction " + task);
                return;
            }
            return;
        }
        if (!z2 && size > 0 && this.mTasks.get(0) == task) {
            if (ActivityTaskManagerDebugConfig.DEBUG_RECENTS) {
                Slog.d(TAG_RECENTS, "addRecent: already at top: " + task);
                return;
            }
            return;
        }
        if (z2 && size > 0 && task.inRecents && task.mAffiliatedTaskId == this.mTasks.get(0).mAffiliatedTaskId) {
            if (ActivityTaskManagerDebugConfig.DEBUG_RECENTS) {
                Slog.d(TAG_RECENTS, "addRecent: affiliated " + this.mTasks.get(0) + " at top when adding " + task);
                return;
            }
            return;
        }
        if (this.mRecentTasksExt.skipAddPreloadingFakeTask(task)) {
            Slog.d(TAG_RECENTS, "addRecent: not adding preloading fake task: " + task);
            return;
        }
        if (task.inRecents) {
            int indexOf = this.mTasks.indexOf(task);
            if (indexOf < 0) {
                Slog.wtf(TAG, "Task with inRecent not in recents: " + task);
                z = true;
                if (ActivityTaskManagerDebugConfig.DEBUG_RECENTS) {
                    Slog.d(TAG_RECENTS, "addRecent: trimming tasks for " + task);
                }
                int removeForAddTask = removeForAddTask(task);
                task.inRecents = true;
                if (z2 || z) {
                    this.mTasks.add(this.mRecentTasksExt.adjustPreloadingTaskIndex((this.mFreezeTaskListReordering || removeForAddTask == -1) ? 0 : removeForAddTask, removeForAddTask, task, size), task);
                    notifyTaskAdded(task);
                    if (ActivityTaskManagerDebugConfig.DEBUG_RECENTS) {
                        Slog.d(TAG_RECENTS, "addRecent: adding " + task);
                    }
                } else if (z2) {
                    Task task2 = task.mNextAffiliate;
                    if (task2 == null) {
                        task2 = task.mPrevAffiliate;
                    }
                    if (task2 != null) {
                        int indexOf2 = this.mTasks.indexOf(task2);
                        if (indexOf2 >= 0) {
                            if (task2 == task.mNextAffiliate) {
                                indexOf2++;
                            }
                            if (ActivityTaskManagerDebugConfig.DEBUG_RECENTS) {
                                Slog.d(TAG_RECENTS, "addRecent: new affiliated task added at " + indexOf2 + ": " + task);
                            }
                            this.mTasks.add(indexOf2, task);
                            notifyTaskAdded(task);
                            if (moveAffiliatedTasksToFront(task, indexOf2)) {
                                return;
                            }
                        } else if (ActivityTaskManagerDebugConfig.DEBUG_RECENTS) {
                            Slog.d(TAG_RECENTS, "addRecent: couldn't find other affiliation " + task2);
                        }
                    } else if (ActivityTaskManagerDebugConfig.DEBUG_RECENTS) {
                        Slog.d(TAG_RECENTS, "addRecent: adding affiliated task without next/prev:" + task);
                    }
                    z = true;
                }
                if (z) {
                    if (ActivityTaskManagerDebugConfig.DEBUG_RECENTS) {
                        Slog.d(TAG_RECENTS, "addRecent: regrouping affiliations");
                    }
                    cleanupLocked(task.mUserId);
                }
                this.mCheckTrimmableTasksOnIdle = true;
                notifyTaskPersisterLocked(task, false);
            }
            if (!z2) {
                if (!this.mFreezeTaskListReordering && !this.mRecentTasksExt.skipMovePreloadingTask(task)) {
                    this.mTasks.remove(indexOf);
                    this.mTasks.add(0, task);
                    if (ActivityTaskManagerDebugConfig.DEBUG_RECENTS) {
                        Slog.d(TAG_RECENTS, "addRecent: moving to top " + task + " from " + indexOf);
                    }
                }
                notifyTaskPersisterLocked(task, false);
                return;
            }
        }
        z = false;
        if (ActivityTaskManagerDebugConfig.DEBUG_RECENTS) {
        }
        int removeForAddTask2 = removeForAddTask(task);
        task.inRecents = true;
        if (z2) {
        }
        this.mTasks.add(this.mRecentTasksExt.adjustPreloadingTaskIndex((this.mFreezeTaskListReordering || removeForAddTask2 == -1) ? 0 : removeForAddTask2, removeForAddTask2, task, size), task);
        notifyTaskAdded(task);
        if (ActivityTaskManagerDebugConfig.DEBUG_RECENTS) {
        }
        if (z) {
        }
        this.mCheckTrimmableTasksOnIdle = true;
        notifyTaskPersisterLocked(task, false);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean addToBottom(Task task) {
        if (!canAddTaskWithoutTrim(task)) {
            return false;
        }
        add(task);
        return true;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void remove(Task task) {
        this.mTasks.remove(task);
        notifyTaskRemoved(task, false, false);
        this.mSocExt.removeTaskUxPerf(task);
        if (task == null || !task.getWrapper().getExtImpl().isContainerTask()) {
            return;
        }
        this.mRecentTasksExt.removeContainerTask(task);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void onActivityIdle(ActivityRecord activityRecord) {
        if (!this.mHiddenTasks.isEmpty() && activityRecord.isActivityTypeHome() && activityRecord.isState(ActivityRecord.State.RESUMED)) {
            removeUnreachableHiddenTasks(activityRecord.getWindowingMode());
        }
        if (this.mCheckTrimmableTasksOnIdle) {
            this.mCheckTrimmableTasksOnIdle = false;
            trimInactiveRecentTasks();
        }
    }

    private void trimInactiveRecentTasks() {
        if (this.mFreezeTaskListReordering) {
            return;
        }
        int size = this.mTasks.size();
        while (size > this.mGlobalMaxNumTasks) {
            Task remove = this.mTasks.remove(size - 1);
            notifyTaskRemoved(remove, true, false);
            size--;
            if (ActivityTaskManagerDebugConfig.DEBUG_RECENTS_TRIM_TASKS) {
                Slog.d(TAG, "Trimming over max-recents task=" + remove + " max=" + this.mGlobalMaxNumTasks);
            }
        }
        int[] currentProfileIds = getCurrentProfileIds();
        this.mTmpQuietProfileUserIds.clear();
        for (int i : currentProfileIds) {
            UserInfo userInfo = getUserInfo(i);
            if (userInfo != null && userInfo.isManagedProfile() && userInfo.isQuietModeEnabled()) {
                this.mTmpQuietProfileUserIds.put(i, true);
            }
            if (ActivityTaskManagerDebugConfig.DEBUG_RECENTS_TRIM_TASKS) {
                Slog.d(TAG, "User: " + userInfo + " quiet=" + this.mTmpQuietProfileUserIds.get(i));
            }
        }
        int i2 = 0;
        int i3 = 0;
        while (i2 < this.mTasks.size()) {
            Task task = this.mTasks.get(i2);
            if (isActiveRecentTask(task, this.mTmpQuietProfileUserIds)) {
                if (this.mHasVisibleRecentTasks && isVisibleRecentTask(task)) {
                    i3++;
                    if (!isInVisibleRange(task, i2, i3, false) && isTrimmable(task)) {
                        if (ActivityTaskManagerDebugConfig.DEBUG_RECENTS_TRIM_TASKS) {
                            Slog.d(TAG, "Trimming out-of-range visible task=" + task);
                        }
                    }
                }
                i2++;
            } else if (ActivityTaskManagerDebugConfig.DEBUG_RECENTS_TRIM_TASKS) {
                Slog.d(TAG, "Trimming inactive task=" + task);
            }
            this.mTasks.remove(task);
            notifyTaskRemoved(task, true, false);
            notifyTaskPersisterLocked(task, false);
        }
    }

    private boolean isActiveRecentTask(Task task, SparseBooleanArray sparseBooleanArray) {
        Task task2;
        if (ActivityTaskManagerDebugConfig.DEBUG_RECENTS_TRIM_TASKS) {
            Slog.d(TAG, "isActiveRecentTask: task=" + task + " globalMax=" + this.mGlobalMaxNumTasks);
        }
        if (sparseBooleanArray.get(task.mUserId)) {
            if (ActivityTaskManagerDebugConfig.DEBUG_RECENTS_TRIM_TASKS) {
                Slog.d(TAG, "\tisQuietProfileTask=true");
            }
            return false;
        }
        int i = task.mAffiliatedTaskId;
        if (i == -1 || i == task.mTaskId || (task2 = getTask(i)) == null || isActiveRecentTask(task2, sparseBooleanArray)) {
            return true;
        }
        if (ActivityTaskManagerDebugConfig.DEBUG_RECENTS_TRIM_TASKS) {
            Slog.d(TAG, "\taffiliatedWithTask=" + task2 + " is not active");
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: Code restructure failed: missing block: B:11:0x0087, code lost:
    
        if (r0 != 5) goto L17;
     */
    @VisibleForTesting
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public boolean isVisibleRecentTask(Task task) {
        if (ActivityTaskManagerDebugConfig.DEBUG_RECENTS_TRIM_TASKS) {
            Slog.d(TAG, "isVisibleRecentTask: task=" + task + " minVis=" + this.mMinNumVisibleTasks + " maxVis=" + this.mMaxNumVisibleTasks + " sessionDuration=" + this.mActiveTasksSessionDurationMs + " inactiveDuration=" + task.getInactiveDuration() + " activityType=" + task.getActivityType() + " windowingMode=" + task.getWindowingMode() + " isAlwaysOnTopWhenVisible=" + task.isAlwaysOnTopWhenVisible() + " intentFlags=" + task.getBaseIntent().getFlags());
        }
        int activityType = task.getActivityType();
        if (activityType != 2 && activityType != 3) {
            if (activityType == 4) {
                if ((task.getBaseIntent().getFlags() & 8388608) == 8388608) {
                    return false;
                }
            }
            int windowingMode = task.getWindowingMode();
            if (windowingMode != 2) {
                if ((windowingMode == 6 && task.isAlwaysOnTopWhenVisible()) || task == this.mService.getLockTaskController().getRootTask()) {
                    return false;
                }
                return task.getDisplayContent() == null || task.getDisplayContent().canShowTasksInHostDeviceRecents();
            }
        }
        return false;
    }

    private boolean isInVisibleRange(Task task, int i, int i2, boolean z) {
        if (!z) {
            if ((task.getBaseIntent().getFlags() & 8388608) == 8388608) {
                if (ActivityTaskManagerDebugConfig.DEBUG_RECENTS_TRIM_TASKS) {
                    Slog.d(TAG, "\texcludeFromRecents=true, taskIndex = " + i + ", isOnHomeDisplay: " + task.isOnHomeDisplay());
                }
                return task.isOnHomeDisplay() && i == 0;
            }
        }
        int i3 = this.mMinNumVisibleTasks;
        if ((i3 >= 0 && i2 <= i3) || task.mChildPipActivity != null) {
            return true;
        }
        int i4 = this.mMaxNumVisibleTasks;
        return i4 >= 0 ? i2 <= i4 : this.mActiveTasksSessionDurationMs > 0 && task.getInactiveDuration() <= this.mActiveTasksSessionDurationMs;
    }

    protected boolean isTrimmable(Task task) {
        Task rootHomeTask;
        if (task.isAttached()) {
            return task.isOnHomeDisplay() && (rootHomeTask = task.getDisplayArea().getRootHomeTask()) != null && task.compareTo((WindowContainer) rootHomeTask) < 0;
        }
        return true;
    }

    private void removeUnreachableHiddenTasks(int i) {
        for (int size = this.mHiddenTasks.size() - 1; size >= 0; size--) {
            Task task = this.mHiddenTasks.get(size);
            if (!task.hasChild() || task.inRecents) {
                this.mHiddenTasks.remove(size);
            } else if (task.getWindowingMode() == i && task.getTopVisibleActivity() == null && (ActivityTaskManagerService.LTW_DISABLE || !this.mService.getWrapper().getExtImpl().getRemoteTaskManager().anyTaskExist(task.mTaskId))) {
                this.mHiddenTasks.remove(size);
                this.mSupervisor.removeTask(task, false, false, "remove-hidden-task");
            }
        }
    }

    private int removeForAddTask(Task task) {
        this.mHiddenTasks.remove(task);
        int findRemoveIndexForAddTask = findRemoveIndexForAddTask(task);
        if (findRemoveIndexForAddTask == -1) {
            return findRemoveIndexForAddTask;
        }
        Task remove = this.mTasks.remove(findRemoveIndexForAddTask);
        if (remove != task) {
            if (remove.hasChild() && !remove.isActivityTypeHome()) {
                Slog.i(TAG, "Add " + remove + " to hidden list because adding " + task);
                this.mHiddenTasks.add(remove);
            }
            notifyTaskRemoved(remove, false, false);
            if (ActivityTaskManagerDebugConfig.DEBUG_RECENTS_TRIM_TASKS) {
                Slog.d(TAG, "Trimming task=" + remove + " for addition of task=" + task);
            }
        }
        this.mRecentTasksExt.removeForAddTask(task, remove);
        notifyTaskPersisterLocked(remove, false);
        return findRemoveIndexForAddTask;
    }

    private int findRemoveIndexForAddTask(Task task) {
        ComponentName componentName;
        if (task != null && task.getWrapper().getExtImpl().isCreateForSingleSplit()) {
            return -1;
        }
        int size = this.mTasks.size();
        Intent intent = task.intent;
        boolean z = intent != null && intent.isDocument();
        int i = task.maxRecents - 1;
        for (int i2 = 0; i2 < size; i2++) {
            Task task2 = this.mTasks.get(i2);
            if (task != task2) {
                if (hasCompatibleActivityTypeAndWindowingMode(task, task2) && task.mUserId == task2.mUserId) {
                    Intent intent2 = task2.intent;
                    String str = task.affinity;
                    boolean z2 = str != null && str.equals(task2.affinity);
                    boolean z3 = intent != null && intent.filterEquals(intent2);
                    int flags = intent.getFlags();
                    boolean z4 = ((268959744 & flags) == 0 || (flags & 134217728) == 0) ? false : true;
                    boolean z5 = intent2 != null && intent2.isDocument();
                    boolean z6 = z && z5;
                    if (z2 || z3 || z6) {
                        if (z6) {
                            ComponentName componentName2 = task.realActivity;
                            if (!((componentName2 == null || (componentName = task2.realActivity) == null || !componentName2.equals(componentName)) ? false : true)) {
                                continue;
                            } else if (i > 0) {
                                i--;
                                if (z3 && !z4) {
                                }
                            }
                        } else if (!z && !z5 && !z4 && !this.mRecentTasksExt.shouldRemoveIndexForAddTask(task, task2)) {
                        }
                    }
                }
            }
            return i2;
        }
        return -1;
    }

    private int processNextAffiliateChainLocked(int i) {
        int i2;
        Task task = this.mTasks.get(i);
        int i3 = task.mAffiliatedTaskId;
        if (task.mTaskId == i3 && task.mPrevAffiliate == null && task.mNextAffiliate == null) {
            task.inRecents = true;
            return i + 1;
        }
        this.mTmpRecents.clear();
        for (int size = this.mTasks.size() - 1; size >= i; size--) {
            Task task2 = this.mTasks.get(size);
            if (task2.mAffiliatedTaskId == i3) {
                this.mTasks.remove(size);
                this.mTmpRecents.add(task2);
            }
        }
        Collections.sort(this.mTmpRecents, TASK_ID_COMPARATOR);
        Task task3 = this.mTmpRecents.get(0);
        task3.inRecents = true;
        if (task3.mNextAffiliate != null) {
            Slog.w(TAG, "Link error 1 first.next=" + task3.mNextAffiliate);
            task3.setNextAffiliate(null);
            notifyTaskPersisterLocked(task3, false);
        }
        int size2 = this.mTmpRecents.size();
        int i4 = 0;
        while (true) {
            i2 = size2 - 1;
            if (i4 >= i2) {
                break;
            }
            Task task4 = this.mTmpRecents.get(i4);
            i4++;
            Task task5 = this.mTmpRecents.get(i4);
            if (task4.mPrevAffiliate != task5) {
                Slog.w(TAG, "Link error 2 next=" + task4 + " prev=" + task4.mPrevAffiliate + " setting prev=" + task5);
                task4.setPrevAffiliate(task5);
                notifyTaskPersisterLocked(task4, false);
            }
            if (task5.mNextAffiliate != task4) {
                Slog.w(TAG, "Link error 3 prev=" + task5 + " next=" + task5.mNextAffiliate + " setting next=" + task4);
                task5.setNextAffiliate(task4);
                notifyTaskPersisterLocked(task5, false);
            }
            task5.inRecents = true;
        }
        Task task6 = this.mTmpRecents.get(i2);
        if (task6.mPrevAffiliate != null) {
            Slog.w(TAG, "Link error 4 last.prev=" + task6.mPrevAffiliate);
            task6.setPrevAffiliate(null);
            notifyTaskPersisterLocked(task6, false);
        }
        this.mTasks.addAll(i, this.mTmpRecents);
        this.mTmpRecents.clear();
        return i + size2;
    }

    /* JADX WARN: Code restructure failed: missing block: B:77:0x017a, code lost:
    
        android.util.Slog.wtf(com.android.server.wm.RecentTasks.TAG, "Bad chain @" + r7 + ": middle task " + r14 + " @" + r7 + " has bad next affiliate " + r14.mNextAffiliate + " id " + r14.mNextAffiliateTaskId + ", expected " + r10);
     */
    /* JADX WARN: Removed duplicated region for block: B:27:0x00f3  */
    /* JADX WARN: Removed duplicated region for block: B:62:0x00b2 A[SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private boolean moveAffiliatedTasksToFront(Task task, int i) {
        int size = this.mTasks.size();
        Task task2 = task;
        int i2 = i;
        while (true) {
            Task task3 = task2.mNextAffiliate;
            if (task3 == null || i2 <= 0) {
                break;
            }
            i2--;
            task2 = task3;
        }
        if (ActivityTaskManagerDebugConfig.DEBUG_RECENTS) {
            Slog.d(TAG_RECENTS, "addRecent: adding affiliates starting at " + i2 + " from initial " + i);
        }
        boolean z = task2.mAffiliatedTaskId == task.mAffiliatedTaskId;
        Task task4 = task2;
        int i3 = i2;
        while (true) {
            if (i3 >= size) {
                break;
            }
            Task task5 = this.mTasks.get(i3);
            if (ActivityTaskManagerDebugConfig.DEBUG_RECENTS) {
                Slog.d(TAG_RECENTS, "addRecent: looking at next chain @" + i3 + " " + task5);
            }
            if (task5 == task2) {
                if (task5.mNextAffiliate != null || task5.mNextAffiliateTaskId != -1) {
                    break;
                }
                if (task5.mPrevAffiliateTaskId != -1) {
                    if (task5.mPrevAffiliate != null) {
                        Slog.wtf(TAG, "Bad chain @" + i3 + ": last task " + task5 + " has previous affiliate " + task5.mPrevAffiliate);
                        z = false;
                    }
                    if (ActivityTaskManagerDebugConfig.DEBUG_RECENTS) {
                        Slog.d(TAG_RECENTS, "addRecent: end of chain @" + i3);
                    }
                } else {
                    if (task5.mPrevAffiliate == null) {
                        Slog.wtf(TAG, "Bad chain @" + i3 + ": task " + task5 + " has previous affiliate " + task5.mPrevAffiliate + " but should be id " + task5.mPrevAffiliate);
                        break;
                    }
                    if (task5.mAffiliatedTaskId != task.mAffiliatedTaskId) {
                        Slog.wtf(TAG, "Bad chain @" + i3 + ": task " + task5 + " has affiliated id " + task5.mAffiliatedTaskId + " but should be " + task.mAffiliatedTaskId);
                        break;
                    }
                    i3++;
                    if (i3 >= size) {
                        Slog.wtf(TAG, "Bad chain ran off index " + i3 + ": last task " + task5);
                        break;
                    }
                    task4 = task5;
                }
            } else {
                if (task5.mNextAffiliate != task4 || task5.mNextAffiliateTaskId != task4.mTaskId) {
                    break;
                }
                if (task5.mPrevAffiliateTaskId != -1) {
                }
            }
        }
        Slog.wtf(TAG, "Bad chain @" + i3 + ": first task has next affiliate: " + task4);
        z = false;
        if (z && i3 < i) {
            Slog.wtf(TAG, "Bad chain @" + i3 + ": did not extend to task " + task + " @" + i);
            z = false;
        }
        if (!z) {
            return false;
        }
        for (int i4 = i2; i4 <= i3; i4++) {
            if (ActivityTaskManagerDebugConfig.DEBUG_RECENTS) {
                Slog.d(TAG_RECENTS, "addRecent: moving affiliated " + task + " from " + i4 + " to " + (i4 - i2));
            }
            this.mTasks.add(i4 - i2, this.mTasks.remove(i4));
        }
        if (!ActivityTaskManagerDebugConfig.DEBUG_RECENTS) {
            return true;
        }
        Slog.d(TAG_RECENTS, "addRecent: done moving tasks  " + i2 + " to " + i3);
        return true;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void dump(PrintWriter printWriter, boolean z, String str) {
        int i;
        int i2;
        printWriter.println("ACTIVITY MANAGER RECENT TASKS (dumpsys activity recents)");
        printWriter.println("mRecentsUid=" + this.mRecentsUid);
        printWriter.println("mRecentsComponent=" + this.mRecentsComponent);
        printWriter.println("mFreezeTaskListReordering=" + this.mFreezeTaskListReordering);
        printWriter.println("mFreezeTaskListReorderingPendingTimeout=" + this.mService.mH.hasCallbacks(this.mResetFreezeTaskListOnTimeoutRunnable));
        if (!this.mHiddenTasks.isEmpty()) {
            printWriter.println("mHiddenTasks=" + this.mHiddenTasks);
        }
        if (this.mTasks.isEmpty()) {
            return;
        }
        int size = this.mTasks.size();
        boolean z2 = false;
        boolean z3 = false;
        for (0; i < size; i + 1) {
            Task task = this.mTasks.get(i);
            if (str != null) {
                Intent intent = task.intent;
                boolean z4 = (intent == null || intent.getComponent() == null || !str.equals(task.intent.getComponent().getPackageName())) ? false : true;
                if (!z4) {
                    Intent intent2 = task.affinityIntent;
                    z4 |= (intent2 == null || intent2.getComponent() == null || !str.equals(task.affinityIntent.getComponent().getPackageName())) ? false : true;
                }
                if (!z4) {
                    ComponentName componentName = task.origActivity;
                    z4 |= componentName != null && str.equals(componentName.getPackageName());
                }
                if (!z4) {
                    ComponentName componentName2 = task.realActivity;
                    z4 |= componentName2 != null && str.equals(componentName2.getPackageName());
                }
                if (!z4) {
                    z4 |= str.equals(task.mCallingPackage);
                }
                i = z4 ? 0 : i + 1;
            }
            if (!z2) {
                printWriter.println("  Recent tasks:");
                z2 = true;
                z3 = true;
            }
            printWriter.print("  * Recent #");
            printWriter.print(i);
            printWriter.print(": ");
            printWriter.println(task);
            if (z) {
                task.dump(printWriter, "    ");
            }
        }
        if (this.mHasVisibleRecentTasks) {
            ArrayList<ActivityManager.RecentTaskInfo> recentTasksImpl = getRecentTasksImpl(Integer.MAX_VALUE, 0, true, this.mService.getCurrentUserId(), 1000);
            boolean z5 = false;
            for (0; i2 < recentTasksImpl.size(); i2 + 1) {
                ActivityManager.RecentTaskInfo recentTaskInfo = recentTasksImpl.get(i2);
                if (str != null) {
                    Intent intent3 = recentTaskInfo.baseIntent;
                    boolean z6 = (intent3 == null || intent3.getComponent() == null || !str.equals(recentTaskInfo.baseIntent.getComponent().getPackageName())) ? false : true;
                    if (!z6) {
                        ComponentName componentName3 = recentTaskInfo.baseActivity;
                        z6 |= componentName3 != null && str.equals(componentName3.getPackageName());
                    }
                    if (!z6) {
                        ComponentName componentName4 = recentTaskInfo.topActivity;
                        z6 |= componentName4 != null && str.equals(componentName4.getPackageName());
                    }
                    if (!z6) {
                        ComponentName componentName5 = recentTaskInfo.origActivity;
                        z6 |= componentName5 != null && str.equals(componentName5.getPackageName());
                    }
                    if (!z6) {
                        ComponentName componentName6 = recentTaskInfo.realActivity;
                        z6 |= componentName6 != null && str.equals(componentName6.getPackageName());
                    }
                    i2 = z6 ? 0 : i2 + 1;
                }
                if (!z5) {
                    if (z3) {
                        printWriter.println();
                    }
                    printWriter.println("  Visible recent tasks (most recent first):");
                    z5 = true;
                    z3 = true;
                }
                printWriter.print("  * RecentTaskInfo #");
                printWriter.print(i2);
                printWriter.print(": ");
                recentTaskInfo.dump(printWriter, "    ");
            }
        }
        if (z3) {
            return;
        }
        printWriter.println("  (nothing)");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public ActivityManager.RecentTaskInfo createRecentTaskInfo(Task task, boolean z, boolean z2) {
        TaskDisplayArea defaultTaskDisplayArea;
        ActivityManager.RecentTaskInfo recentTaskInfo = new ActivityManager.RecentTaskInfo();
        if (task.isAttached()) {
            defaultTaskDisplayArea = task.getDisplayArea();
        } else {
            defaultTaskDisplayArea = this.mService.mRootWindowContainer.getDefaultTaskDisplayArea();
        }
        task.fillTaskInfo(recentTaskInfo, z, defaultTaskDisplayArea);
        recentTaskInfo.id = recentTaskInfo.isRunning ? recentTaskInfo.taskId : -1;
        recentTaskInfo.persistentId = recentTaskInfo.taskId;
        recentTaskInfo.lastSnapshotData.set(task.mLastTaskSnapshotData);
        if (!z2) {
            Task.trimIneffectiveInfo(task, recentTaskInfo);
        }
        if (task.mCreatedByOrganizer) {
            for (int childCount = task.getChildCount() - 1; childCount >= 0; childCount--) {
                Task asTask = task.getChildAt(childCount).asTask();
                if (asTask != null && asTask.isOrganized()) {
                    ActivityManager.RecentTaskInfo recentTaskInfo2 = new ActivityManager.RecentTaskInfo();
                    asTask.fillTaskInfo(recentTaskInfo2, true, defaultTaskDisplayArea);
                    recentTaskInfo.childrenTaskInfos.add(recentTaskInfo2);
                }
            }
        }
        return recentTaskInfo;
    }

    private boolean hasCompatibleActivityTypeAndWindowingMode(Task task, Task task2) {
        int activityType = task.getActivityType();
        int windowingMode = task.getWindowingMode();
        boolean z = activityType == 0;
        boolean z2 = windowingMode == 0;
        int activityType2 = task2.getActivityType();
        int windowingMode2 = task2.getWindowingMode();
        return (activityType == activityType2 || z || (activityType2 == 0)) && (windowingMode == windowingMode2 || z2 || (windowingMode2 == 0) || this.mRecentTasksExt.isLaunchedFromMultiSearch(task, task2) || this.mRecentTasksExt.hasCompatibleActivityTypeAndWindowingMode(windowingMode, windowingMode2));
    }

    public IRecentTasksWrapper getWrapper() {
        return this.mRecentTasksWrapper;
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
    private class RecentTasksWrapper implements IRecentTasksWrapper {
        private RecentTasksWrapper() {
        }

        @Override // com.android.server.wm.IRecentTasksWrapper
        public ArrayList<Task> getHiddenTasks() {
            return RecentTasks.this.mHiddenTasks;
        }
    }
}
