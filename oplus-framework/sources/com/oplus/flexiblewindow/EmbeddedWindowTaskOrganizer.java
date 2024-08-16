package com.oplus.flexiblewindow;

import android.app.ActivityManager;
import android.os.IBinder;
import android.os.RemoteException;
import android.os.SystemProperties;
import android.util.ArrayMap;
import android.util.Slog;
import android.util.SparseArray;
import android.view.SurfaceControl;
import android.window.WindowContainerToken;
import android.window.WindowContainerTransaction;
import com.oplus.flexiblewindow.EmbeddedWindowTaskOrganizer;
import com.oplus.flexiblewindow.IFlexibleWindowObserver;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.concurrent.Executor;

/* loaded from: classes.dex */
public class EmbeddedWindowTaskOrganizer {
    private static boolean DEBUG = SystemProperties.getBoolean("persist.sys.assert.panic", false);
    private static final String TAG = "EmbeddedWindowTaskOrganizer";
    public static final int TASK_LISTENER_TYPE_FULLSCREEN = -2;
    public static final int TASK_LISTENER_TYPE_MULTI_WINDOW = -3;
    public static final int TASK_LISTENER_TYPE_PIP = -4;
    public static final int TASK_LISTENER_TYPE_UNDEFINED = -1;
    private final Executor mExecutor;
    private boolean mOrganizerRegistered;
    private final SparseArray<TaskListener> mTaskListeners = new SparseArray<>();
    private final SparseArray<TaskAppearedInfo> mTasks = new SparseArray<>();
    private final ArrayMap<IBinder, TaskListener> mLaunchCookieToListener = new ArrayMap<>();
    private final ArrayMap<Integer, IBinder> mLaunchTaskToCookie = new ArrayMap<>();
    private final Object mLock = new Object();
    private FlexibleWindowManager mFlexibleWindowManager = FlexibleWindowManager.getInstance();
    private FlexibleWindowObserver mFlexibleWindowObserver = new FlexibleWindowObserver();

    public EmbeddedWindowTaskOrganizer(Executor executor) {
        this.mExecutor = executor;
    }

    static int taskInfoToTaskListenerType(ActivityManager.RunningTaskInfo runningTaskInfo) {
        switch (runningTaskInfo.configuration.windowConfiguration.getWindowingMode()) {
            case 1:
                return -2;
            case 2:
                return -4;
            case 6:
                return -3;
            default:
                return -1;
        }
    }

    public void start() {
        if (this.mOrganizerRegistered) {
            return;
        }
        this.mFlexibleWindowManager.registerFlexibleWindowObserver(this.mFlexibleWindowObserver);
        this.mOrganizerRegistered = true;
    }

    public void stop() {
        if (!this.mOrganizerRegistered) {
            return;
        }
        this.mFlexibleWindowManager.unregisterFlexibleWindowObserver(this.mFlexibleWindowObserver);
        this.mOrganizerRegistered = false;
    }

    public void setInterceptBackPressedOnTaskRoot(WindowContainerToken token, boolean interceptBackPressed) {
        this.mFlexibleWindowManager.setInterceptBackPressedOnTaskRoot(token, interceptBackPressed);
    }

    public void applyTransaction(WindowContainerTransaction windowContainerTransaction) {
        this.mFlexibleWindowManager.applyTransaction(windowContainerTransaction);
    }

    public Executor getExecutor() {
        return this.mExecutor;
    }

    public void addListenerForTaskId(TaskListener listener, int taskId) {
        synchronized (this.mLock) {
            if (DEBUG) {
                Slog.d(TAG, "addListenerForTaskId taskId = " + taskId);
            }
            if (this.mTaskListeners.get(taskId) != null) {
                throw new IllegalArgumentException("Listener for taskId=" + taskId + " already exists");
            }
            TaskAppearedInfo info = this.mTasks.get(taskId);
            if (info == null) {
                throw new IllegalArgumentException("addListenerForTaskId unknown taskId=" + taskId);
            }
            TaskListener oldListener = getTaskListener(info.getTaskInfo());
            this.mTaskListeners.put(taskId, listener);
            updateTaskListenerIfNeeded(info.getTaskInfo(), info.getLeash(), oldListener, listener);
        }
    }

    public void addListenerForType(TaskListener listener, int... listenerTypes) {
        synchronized (this.mLock) {
            if (DEBUG) {
                Slog.d(TAG, "addListenerForType types=" + Arrays.toString(listenerTypes) + " listener=" + listener);
            }
            for (int listenerType : listenerTypes) {
                if (this.mTaskListeners.get(listenerType) != null) {
                    throw new IllegalArgumentException("Listener for listenerType=" + listenerType + " already exists");
                }
                this.mTaskListeners.put(listenerType, listener);
                for (int i = this.mTasks.size() - 1; i >= 0; i--) {
                    TaskAppearedInfo data = this.mTasks.valueAt(i);
                    TaskListener taskListener = getTaskListener(data.getTaskInfo());
                    if (taskListener == listener) {
                        listener.onTaskAppeared(data.getTaskInfo(), data.getLeash());
                    }
                }
            }
        }
    }

    public void removeListener(TaskListener listener) {
        synchronized (this.mLock) {
            if (DEBUG) {
                Slog.d(TAG, "Remove listener=" + listener);
            }
            int index = this.mTaskListeners.indexOfValue(listener);
            if (index == -1) {
                if (DEBUG) {
                    Slog.w(TAG, "No registered listener found");
                }
                return;
            }
            ArrayList<TaskAppearedInfo> tasks = new ArrayList<>();
            for (int i = this.mTasks.size() - 1; i >= 0; i--) {
                TaskAppearedInfo data = this.mTasks.valueAt(i);
                TaskListener taskListener = getTaskListener(data.getTaskInfo());
                if (taskListener == listener) {
                    tasks.add(data);
                }
            }
            this.mTaskListeners.removeAt(index);
            for (int i2 = tasks.size() - 1; i2 >= 0; i2--) {
                TaskAppearedInfo data2 = tasks.get(i2);
                updateTaskListenerIfNeeded(data2.getTaskInfo(), data2.getLeash(), null, getTaskListener(data2.getTaskInfo()));
            }
        }
    }

    public void setPendingLaunchCookieListener(IBinder cookie, TaskListener listener) {
        synchronized (this.mLock) {
            this.mLaunchCookieToListener.put(cookie, listener);
            Slog.d(TAG, "setPendingLaunchCookieListener cookie=" + cookie + ", listener=" + listener);
        }
    }

    public ActivityManager.RunningTaskInfo getRunningTaskInfo(int taskId) {
        ActivityManager.RunningTaskInfo taskInfo;
        synchronized (this.mLock) {
            TaskAppearedInfo info = this.mTasks.get(taskId);
            taskInfo = info != null ? info.getTaskInfo() : null;
        }
        return taskInfo;
    }

    private boolean updateTaskListenerIfNeeded(ActivityManager.RunningTaskInfo taskInfo, SurfaceControl leash, TaskListener oldListener, TaskListener newListener) {
        if (oldListener == newListener) {
            return false;
        }
        if (oldListener != null) {
            onTaskVanished(taskInfo);
        }
        if (newListener != null) {
            onTaskAppeared(new TaskAppearedInfo(taskInfo, leash));
            return true;
        }
        return true;
    }

    public boolean isEmptyListener() {
        return this.mTaskListeners.size() == 0 && this.mLaunchCookieToListener.isEmpty();
    }

    private TaskListener getTaskListener(ActivityManager.RunningTaskInfo runningTaskInfo) {
        return getTaskListener(runningTaskInfo, false);
    }

    private TaskListener getTaskListener(ActivityManager.RunningTaskInfo runningTaskInfo, boolean removeLaunchCookieIfNeeded) {
        IBinder cookie;
        TaskListener listener;
        int taskId = runningTaskInfo.taskId;
        ArrayList<IBinder> launchCookies = runningTaskInfo.launchCookies;
        int i = launchCookies.size();
        do {
            i--;
            if (i >= 0) {
                cookie = launchCookies.get(i);
                listener = this.mLaunchCookieToListener.get(cookie);
            } else {
                TaskListener listener2 = this.mTaskListeners.get(taskId);
                if (listener2 != null) {
                    if (DEBUG) {
                        Slog.d(TAG, "getTaskListener this=" + this + ",taskId=" + taskId + ",return listener=" + listener2);
                    }
                    return listener2;
                }
                int taskListenerType = taskInfoToTaskListenerType(runningTaskInfo);
                if (DEBUG) {
                    Slog.d(TAG, "getTaskListener this=" + this + ",taskListenerType=" + taskListenerType + ", return listener=" + this.mTaskListeners.get(taskListenerType));
                }
                return this.mTaskListeners.get(taskListenerType);
            }
        } while (listener == null);
        if (removeLaunchCookieIfNeeded) {
            if (DEBUG) {
                Slog.d(TAG, "getTaskListener this=" + this + ",remove the cookie=" + cookie);
            }
            this.mLaunchCookieToListener.remove(cookie);
            this.mLaunchTaskToCookie.put(Integer.valueOf(taskId), cookie);
            this.mTaskListeners.put(taskId, listener);
        }
        if (DEBUG) {
            Slog.d(TAG, "getTaskListener this=" + this + ",return cookie listener=" + listener);
        }
        return listener;
    }

    public void onTaskAppeared(TaskAppearedInfo info) {
        if (!this.mOrganizerRegistered) {
            if (DEBUG) {
                Slog.d(TAG, "onTaskAppeared: taskId=" + info.getTaskInfo().taskId + ", return for unregistered organizer");
                return;
            }
            return;
        }
        synchronized (this.mLock) {
            int taskId = info.getTaskInfo().taskId;
            this.mTasks.put(taskId, info);
            TaskListener listener = getTaskListener(info.getTaskInfo(), true);
            if (DEBUG) {
                Slog.d(TAG, "onTaskAppeared: this=" + this + ", taskId=" + taskId + ", info=" + info.mTaskInfo.topActivity + ", listener=" + listener);
            }
            if (listener != null) {
                listener.onTaskAppeared(info.getTaskInfo(), info.getLeash());
            }
        }
    }

    public void onTaskInfoChanged(ActivityManager.RunningTaskInfo taskInfo, SurfaceControl leash) {
        if (!this.mOrganizerRegistered) {
            if (DEBUG) {
                Slog.d(TAG, "onTaskInfoChanged: taskId=" + taskInfo.taskId + ", return for unregistered organizer");
                return;
            }
            return;
        }
        synchronized (this.mLock) {
            TaskAppearedInfo data = this.mTasks.get(taskInfo.taskId);
            TaskListener oldListener = data != null ? getTaskListener(data.getTaskInfo()) : null;
            TaskListener newListener = getTaskListener(taskInfo);
            this.mTasks.put(taskInfo.taskId, new TaskAppearedInfo(taskInfo, leash));
            boolean updated = updateTaskListenerIfNeeded(taskInfo, leash, oldListener, newListener);
            if (DEBUG) {
                Slog.d(TAG, "onTaskInfoChanged: this=" + this + ", taskId=" + taskInfo.taskId + ", topActivity=" + taskInfo.topActivity + ", updated=" + updated + ", oldListener=" + oldListener + ", newListener=" + newListener + ", data=" + data);
            }
            if (!updated && newListener != null) {
                newListener.onTaskInfoChanged(taskInfo, leash);
            }
        }
    }

    public void onBackPressedOnTaskRoot(ActivityManager.RunningTaskInfo taskInfo) {
        if (!this.mOrganizerRegistered) {
            return;
        }
        synchronized (this.mLock) {
            TaskListener listener = getTaskListener(taskInfo);
            if (DEBUG) {
                Slog.d(TAG, "onBackPressedOnTaskRoot: taskId=" + taskInfo.taskId + ", topActivity=" + taskInfo.topActivity + ", listener=" + listener);
            }
            if (listener != null) {
                listener.onBackPressedOnTaskRoot(taskInfo);
            }
        }
    }

    public void onTaskVanished(ActivityManager.RunningTaskInfo taskInfo) {
        if (!this.mOrganizerRegistered) {
            if (DEBUG) {
                Slog.d(TAG, "onTaskVanished: taskId=" + taskInfo.taskId + ", return for unregistered organizer");
                return;
            }
            return;
        }
        synchronized (this.mLock) {
            int taskId = taskInfo.taskId;
            this.mLaunchTaskToCookie.remove(Integer.valueOf(taskId));
            TaskAppearedInfo info = this.mTasks.get(taskId);
            if (info == null) {
                Slog.e(TAG, "onTaskVanished: get unknown taskId=" + taskId);
                return;
            }
            TaskListener listener = getTaskListener(info.getTaskInfo());
            this.mTasks.remove(taskId);
            if (DEBUG) {
                Slog.d(TAG, "onTaskVanished: this=" + this + ", taskId=" + taskInfo.taskId + ", topActivity=" + taskInfo.topActivity + ", listener=" + listener);
            }
            if (listener != null) {
                listener.onTaskVanished(taskInfo);
            }
        }
    }

    public void setTaskListenerForNewTask(ActivityManager.RunningTaskInfo taskInfo) {
        int taskId;
        Iterator<Integer> it = this.mLaunchTaskToCookie.keySet().iterator();
        while (it.hasNext() && taskInfo.taskId != (taskId = it.next().intValue())) {
            ArrayList<IBinder> launchCookies = taskInfo.launchCookies;
            int i = launchCookies.size() - 1;
            while (true) {
                if (i >= 0) {
                    IBinder cookie = launchCookies.get(i);
                    if (!this.mLaunchTaskToCookie.get(Integer.valueOf(taskId)).equals(cookie)) {
                        i--;
                    } else {
                        TaskListener listener = this.mTaskListeners.get(taskId);
                        if (listener != null) {
                            this.mTaskListeners.remove(taskId);
                            if (DEBUG) {
                                Slog.d(TAG, "setTaskListenerForNewTask: taskId=" + taskInfo.taskId + " listener=" + listener);
                            }
                            this.mTaskListeners.put(taskInfo.taskId, listener);
                        }
                    }
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public class FlexibleWindowObserver extends IFlexibleWindowObserver.Stub {
        private FlexibleWindowObserver() {
        }

        @Override // com.oplus.flexiblewindow.IFlexibleWindowObserver
        public void onTaskAppeared(final ActivityManager.RunningTaskInfo taskInfo, final SurfaceControl leash) throws RemoteException {
            EmbeddedWindowTaskOrganizer.this.mExecutor.execute(new Runnable() { // from class: com.oplus.flexiblewindow.EmbeddedWindowTaskOrganizer$FlexibleWindowObserver$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    EmbeddedWindowTaskOrganizer.FlexibleWindowObserver.this.lambda$onTaskAppeared$0(taskInfo, leash);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onTaskAppeared$0(ActivityManager.RunningTaskInfo taskInfo, SurfaceControl leash) {
            EmbeddedWindowTaskOrganizer.this.setTaskListenerForNewTask(taskInfo);
            EmbeddedWindowTaskOrganizer.this.onTaskAppeared(new TaskAppearedInfo(taskInfo, leash));
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onTaskInfoChanged$1(ActivityManager.RunningTaskInfo taskInfo, SurfaceControl leash) {
            EmbeddedWindowTaskOrganizer.this.onTaskInfoChanged(taskInfo, leash);
        }

        @Override // com.oplus.flexiblewindow.IFlexibleWindowObserver
        public void onTaskInfoChanged(final ActivityManager.RunningTaskInfo taskInfo, final SurfaceControl leash) throws RemoteException {
            EmbeddedWindowTaskOrganizer.this.mExecutor.execute(new Runnable() { // from class: com.oplus.flexiblewindow.EmbeddedWindowTaskOrganizer$FlexibleWindowObserver$$ExternalSyntheticLambda1
                @Override // java.lang.Runnable
                public final void run() {
                    EmbeddedWindowTaskOrganizer.FlexibleWindowObserver.this.lambda$onTaskInfoChanged$1(taskInfo, leash);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onBackPressedOnTaskRoot$2(ActivityManager.RunningTaskInfo taskInfo) {
            EmbeddedWindowTaskOrganizer.this.onBackPressedOnTaskRoot(taskInfo);
        }

        @Override // com.oplus.flexiblewindow.IFlexibleWindowObserver
        public void onBackPressedOnTaskRoot(final ActivityManager.RunningTaskInfo taskInfo) throws RemoteException {
            EmbeddedWindowTaskOrganizer.this.mExecutor.execute(new Runnable() { // from class: com.oplus.flexiblewindow.EmbeddedWindowTaskOrganizer$FlexibleWindowObserver$$ExternalSyntheticLambda2
                @Override // java.lang.Runnable
                public final void run() {
                    EmbeddedWindowTaskOrganizer.FlexibleWindowObserver.this.lambda$onBackPressedOnTaskRoot$2(taskInfo);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onTaskVanished$3(ActivityManager.RunningTaskInfo taskInfo) {
            EmbeddedWindowTaskOrganizer.this.onTaskVanished(taskInfo);
        }

        @Override // com.oplus.flexiblewindow.IFlexibleWindowObserver
        public void onTaskVanished(final ActivityManager.RunningTaskInfo taskInfo) throws RemoteException {
            EmbeddedWindowTaskOrganizer.this.mExecutor.execute(new Runnable() { // from class: com.oplus.flexiblewindow.EmbeddedWindowTaskOrganizer$FlexibleWindowObserver$$ExternalSyntheticLambda3
                @Override // java.lang.Runnable
                public final void run() {
                    EmbeddedWindowTaskOrganizer.FlexibleWindowObserver.this.lambda$onTaskVanished$3(taskInfo);
                }
            });
        }
    }

    /* loaded from: classes.dex */
    public interface TaskListener {
        default void onTaskAppeared(ActivityManager.RunningTaskInfo taskInfo, SurfaceControl leash) {
        }

        default void onTaskInfoChanged(ActivityManager.RunningTaskInfo taskInfo, SurfaceControl leash) {
        }

        default void onTaskVanished(ActivityManager.RunningTaskInfo taskInfo) {
        }

        default void onBackPressedOnTaskRoot(ActivityManager.RunningTaskInfo taskInfo) {
        }
    }

    /* loaded from: classes.dex */
    public static class TaskAppearedInfo {
        private SurfaceControl mLeash;
        private ActivityManager.RunningTaskInfo mTaskInfo;

        TaskAppearedInfo(ActivityManager.RunningTaskInfo taskInfo, SurfaceControl leash) {
            this.mTaskInfo = taskInfo;
            this.mLeash = leash;
        }

        ActivityManager.RunningTaskInfo getTaskInfo() {
            return this.mTaskInfo;
        }

        public SurfaceControl getLeash() {
            return this.mLeash;
        }
    }
}
