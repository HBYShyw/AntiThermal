package com.android.server.wm;

import android.app.ActivityManager;
import android.app.ITaskStackListener;
import android.app.TaskInfo;
import android.app.TaskStackListener;
import android.content.ComponentName;
import android.os.Binder;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.RemoteCallbackList;
import android.os.RemoteException;
import android.util.Slog;
import android.window.TaskSnapshot;
import com.android.internal.annotations.GuardedBy;
import com.android.internal.os.SomeArgs;
import java.util.ArrayList;
import system.ext.loader.core.ExtLoader;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
public class TaskChangeNotificationController {
    private static final int NOTIFY_ACTIVITY_DISMISSING_DOCKED_ROOT_TASK_MSG = 7;
    private static final int NOTIFY_ACTIVITY_LAUNCH_ON_SECONDARY_DISPLAY_FAILED_MSG = 18;
    private static final int NOTIFY_ACTIVITY_LAUNCH_ON_SECONDARY_DISPLAY_REROUTED_MSG = 19;
    private static final int NOTIFY_ACTIVITY_PINNED_LISTENERS_MSG = 3;
    private static final int NOTIFY_ACTIVITY_REQUESTED_ORIENTATION_CHANGED_LISTENERS = 12;
    private static final int NOTIFY_ACTIVITY_RESTART_ATTEMPT_LISTENERS_MSG = 4;
    private static final int NOTIFY_ACTIVITY_ROTATED_MSG = 26;
    private static final int NOTIFY_ACTIVITY_UNPINNED_LISTENERS_MSG = 17;
    private static final int NOTIFY_BACK_PRESSED_ON_TASK_ROOT = 20;
    private static final int NOTIFY_FORCED_RESIZABLE_MSG = 6;
    private static final int NOTIFY_LOCK_TASK_MODE_CHANGED_MSG = 28;
    private static final int NOTIFY_TASK_ADDED_LISTENERS_MSG = 8;
    private static final int NOTIFY_TASK_DESCRIPTION_CHANGED_LISTENERS_MSG = 11;
    private static final int NOTIFY_TASK_DISPLAY_CHANGED_LISTENERS_MSG = 21;
    private static final int NOTIFY_TASK_FOCUS_CHANGED_MSG = 24;
    private static final int NOTIFY_TASK_LIST_FROZEN_UNFROZEN_MSG = 23;
    private static final int NOTIFY_TASK_LIST_UPDATED_LISTENERS_MSG = 22;
    private static final int NOTIFY_TASK_MOVED_TO_BACK_LISTENERS_MSG = 27;
    private static final int NOTIFY_TASK_MOVED_TO_FRONT_LISTENERS_MSG = 10;
    private static final int NOTIFY_TASK_PROFILE_LOCKED_LISTENERS_MSG = 14;
    private static final int NOTIFY_TASK_REMOVAL_STARTED_LISTENERS = 13;
    private static final int NOTIFY_TASK_REMOVED_LISTENERS_MSG = 9;
    private static final int NOTIFY_TASK_REQUESTED_ORIENTATION_CHANGED_MSG = 25;
    private static final int NOTIFY_TASK_SNAPSHOT_CHANGED_LISTENERS_MSG = 15;
    private static final int NOTIFY_TASK_STACK_CHANGE_LISTENERS_DELAY = 100;
    private static final int NOTIFY_TASK_STACK_CHANGE_LISTENERS_MSG = 2;
    private final Handler mHandler;
    private ActivityTaskSupervisor mTaskSupervisor;

    @GuardedBy({"mRemoteTaskStackListeners"})
    private final RemoteCallbackList<ITaskStackListener> mRemoteTaskStackListeners = new RemoteCallbackList<>();
    private ITaskChangeNotificationControllerExt mTaskChangeNotificationControllerExt = (ITaskChangeNotificationControllerExt) ExtLoader.type(ITaskChangeNotificationControllerExt.class).base(this).create();

    @GuardedBy({"mLocalTaskStackListeners"})
    private final ArrayList<ITaskStackListener> mLocalTaskStackListeners = new ArrayList<>();
    private final TaskStackConsumer mNotifyTaskStackChanged = new TaskStackConsumer() { // from class: com.android.server.wm.TaskChangeNotificationController$$ExternalSyntheticLambda0
        @Override // com.android.server.wm.TaskChangeNotificationController.TaskStackConsumer
        public final void accept(ITaskStackListener iTaskStackListener, Message message) {
            iTaskStackListener.onTaskStackChanged();
        }
    };
    private final TaskStackConsumer mNotifyTaskCreated = new TaskStackConsumer() { // from class: com.android.server.wm.TaskChangeNotificationController$$ExternalSyntheticLambda11
        @Override // com.android.server.wm.TaskChangeNotificationController.TaskStackConsumer
        public final void accept(ITaskStackListener iTaskStackListener, Message message) {
            TaskChangeNotificationController.lambda$new$1(iTaskStackListener, message);
        }
    };
    private final TaskStackConsumer mNotifyTaskRemoved = new TaskStackConsumer() { // from class: com.android.server.wm.TaskChangeNotificationController$$ExternalSyntheticLambda17
        @Override // com.android.server.wm.TaskChangeNotificationController.TaskStackConsumer
        public final void accept(ITaskStackListener iTaskStackListener, Message message) {
            TaskChangeNotificationController.lambda$new$2(iTaskStackListener, message);
        }
    };
    private final TaskStackConsumer mNotifyTaskMovedToFront = new TaskStackConsumer() { // from class: com.android.server.wm.TaskChangeNotificationController$$ExternalSyntheticLambda18
        @Override // com.android.server.wm.TaskChangeNotificationController.TaskStackConsumer
        public final void accept(ITaskStackListener iTaskStackListener, Message message) {
            TaskChangeNotificationController.lambda$new$3(iTaskStackListener, message);
        }
    };
    private final TaskStackConsumer mNotifyTaskDescriptionChanged = new TaskStackConsumer() { // from class: com.android.server.wm.TaskChangeNotificationController$$ExternalSyntheticLambda19
        @Override // com.android.server.wm.TaskChangeNotificationController.TaskStackConsumer
        public final void accept(ITaskStackListener iTaskStackListener, Message message) {
            TaskChangeNotificationController.lambda$new$4(iTaskStackListener, message);
        }
    };
    private final TaskStackConsumer mNotifyBackPressedOnTaskRoot = new TaskStackConsumer() { // from class: com.android.server.wm.TaskChangeNotificationController$$ExternalSyntheticLambda20
        @Override // com.android.server.wm.TaskChangeNotificationController.TaskStackConsumer
        public final void accept(ITaskStackListener iTaskStackListener, Message message) {
            TaskChangeNotificationController.lambda$new$5(iTaskStackListener, message);
        }
    };
    private final TaskStackConsumer mNotifyActivityRequestedOrientationChanged = new TaskStackConsumer() { // from class: com.android.server.wm.TaskChangeNotificationController$$ExternalSyntheticLambda21
        @Override // com.android.server.wm.TaskChangeNotificationController.TaskStackConsumer
        public final void accept(ITaskStackListener iTaskStackListener, Message message) {
            TaskChangeNotificationController.lambda$new$6(iTaskStackListener, message);
        }
    };
    private final TaskStackConsumer mNotifyTaskRemovalStarted = new TaskStackConsumer() { // from class: com.android.server.wm.TaskChangeNotificationController$$ExternalSyntheticLambda22
        @Override // com.android.server.wm.TaskChangeNotificationController.TaskStackConsumer
        public final void accept(ITaskStackListener iTaskStackListener, Message message) {
            TaskChangeNotificationController.lambda$new$7(iTaskStackListener, message);
        }
    };
    private final TaskStackConsumer mNotifyActivityPinned = new TaskStackConsumer() { // from class: com.android.server.wm.TaskChangeNotificationController$$ExternalSyntheticLambda23
        @Override // com.android.server.wm.TaskChangeNotificationController.TaskStackConsumer
        public final void accept(ITaskStackListener iTaskStackListener, Message message) {
            TaskChangeNotificationController.lambda$new$8(iTaskStackListener, message);
        }
    };
    private final TaskStackConsumer mNotifyActivityUnpinned = new TaskStackConsumer() { // from class: com.android.server.wm.TaskChangeNotificationController$$ExternalSyntheticLambda24
        @Override // com.android.server.wm.TaskChangeNotificationController.TaskStackConsumer
        public final void accept(ITaskStackListener iTaskStackListener, Message message) {
            iTaskStackListener.onActivityUnpinned();
        }
    };
    private final TaskStackConsumer mNotifyActivityRestartAttempt = new TaskStackConsumer() { // from class: com.android.server.wm.TaskChangeNotificationController$$ExternalSyntheticLambda1
        @Override // com.android.server.wm.TaskChangeNotificationController.TaskStackConsumer
        public final void accept(ITaskStackListener iTaskStackListener, Message message) {
            TaskChangeNotificationController.lambda$new$10(iTaskStackListener, message);
        }
    };
    private final TaskStackConsumer mNotifyActivityForcedResizable = new TaskStackConsumer() { // from class: com.android.server.wm.TaskChangeNotificationController$$ExternalSyntheticLambda2
        @Override // com.android.server.wm.TaskChangeNotificationController.TaskStackConsumer
        public final void accept(ITaskStackListener iTaskStackListener, Message message) {
            TaskChangeNotificationController.lambda$new$11(iTaskStackListener, message);
        }
    };
    private final TaskStackConsumer mNotifyActivityDismissingDockedTask = new TaskStackConsumer() { // from class: com.android.server.wm.TaskChangeNotificationController$$ExternalSyntheticLambda3
        @Override // com.android.server.wm.TaskChangeNotificationController.TaskStackConsumer
        public final void accept(ITaskStackListener iTaskStackListener, Message message) {
            iTaskStackListener.onActivityDismissingDockedTask();
        }
    };
    private final TaskStackConsumer mNotifyActivityLaunchOnSecondaryDisplayFailed = new TaskStackConsumer() { // from class: com.android.server.wm.TaskChangeNotificationController$$ExternalSyntheticLambda4
        @Override // com.android.server.wm.TaskChangeNotificationController.TaskStackConsumer
        public final void accept(ITaskStackListener iTaskStackListener, Message message) {
            TaskChangeNotificationController.lambda$new$13(iTaskStackListener, message);
        }
    };
    private final TaskStackConsumer mNotifyActivityLaunchOnSecondaryDisplayRerouted = new TaskStackConsumer() { // from class: com.android.server.wm.TaskChangeNotificationController$$ExternalSyntheticLambda5
        @Override // com.android.server.wm.TaskChangeNotificationController.TaskStackConsumer
        public final void accept(ITaskStackListener iTaskStackListener, Message message) {
            TaskChangeNotificationController.lambda$new$14(iTaskStackListener, message);
        }
    };
    private final TaskStackConsumer mNotifyTaskProfileLocked = new TaskStackConsumer() { // from class: com.android.server.wm.TaskChangeNotificationController$$ExternalSyntheticLambda6
        @Override // com.android.server.wm.TaskChangeNotificationController.TaskStackConsumer
        public final void accept(ITaskStackListener iTaskStackListener, Message message) {
            TaskChangeNotificationController.lambda$new$15(iTaskStackListener, message);
        }
    };
    private final TaskStackConsumer mNotifyTaskSnapshotChanged = new TaskStackConsumer() { // from class: com.android.server.wm.TaskChangeNotificationController$$ExternalSyntheticLambda7
        @Override // com.android.server.wm.TaskChangeNotificationController.TaskStackConsumer
        public final void accept(ITaskStackListener iTaskStackListener, Message message) {
            TaskChangeNotificationController.this.lambda$new$16(iTaskStackListener, message);
        }
    };
    private final TaskStackConsumer mNotifyTaskDisplayChanged = new TaskStackConsumer() { // from class: com.android.server.wm.TaskChangeNotificationController$$ExternalSyntheticLambda8
        @Override // com.android.server.wm.TaskChangeNotificationController.TaskStackConsumer
        public final void accept(ITaskStackListener iTaskStackListener, Message message) {
            TaskChangeNotificationController.lambda$new$17(iTaskStackListener, message);
        }
    };
    private final TaskStackConsumer mNotifyTaskListUpdated = new TaskStackConsumer() { // from class: com.android.server.wm.TaskChangeNotificationController$$ExternalSyntheticLambda9
        @Override // com.android.server.wm.TaskChangeNotificationController.TaskStackConsumer
        public final void accept(ITaskStackListener iTaskStackListener, Message message) {
            iTaskStackListener.onRecentTaskListUpdated();
        }
    };
    private final TaskStackConsumer mNotifyTaskListFrozen = new TaskStackConsumer() { // from class: com.android.server.wm.TaskChangeNotificationController$$ExternalSyntheticLambda10
        @Override // com.android.server.wm.TaskChangeNotificationController.TaskStackConsumer
        public final void accept(ITaskStackListener iTaskStackListener, Message message) {
            TaskChangeNotificationController.lambda$new$19(iTaskStackListener, message);
        }
    };
    private final TaskStackConsumer mNotifyTaskFocusChanged = new TaskStackConsumer() { // from class: com.android.server.wm.TaskChangeNotificationController$$ExternalSyntheticLambda12
        @Override // com.android.server.wm.TaskChangeNotificationController.TaskStackConsumer
        public final void accept(ITaskStackListener iTaskStackListener, Message message) {
            TaskChangeNotificationController.lambda$new$20(iTaskStackListener, message);
        }
    };
    private final TaskStackConsumer mNotifyTaskRequestedOrientationChanged = new TaskStackConsumer() { // from class: com.android.server.wm.TaskChangeNotificationController$$ExternalSyntheticLambda13
        @Override // com.android.server.wm.TaskChangeNotificationController.TaskStackConsumer
        public final void accept(ITaskStackListener iTaskStackListener, Message message) {
            TaskChangeNotificationController.lambda$new$21(iTaskStackListener, message);
        }
    };
    private final TaskStackConsumer mNotifyOnActivityRotation = new TaskStackConsumer() { // from class: com.android.server.wm.TaskChangeNotificationController$$ExternalSyntheticLambda14
        @Override // com.android.server.wm.TaskChangeNotificationController.TaskStackConsumer
        public final void accept(ITaskStackListener iTaskStackListener, Message message) {
            TaskChangeNotificationController.lambda$new$22(iTaskStackListener, message);
        }
    };
    private final TaskStackConsumer mNotifyTaskMovedToBack = new TaskStackConsumer() { // from class: com.android.server.wm.TaskChangeNotificationController$$ExternalSyntheticLambda15
        @Override // com.android.server.wm.TaskChangeNotificationController.TaskStackConsumer
        public final void accept(ITaskStackListener iTaskStackListener, Message message) {
            TaskChangeNotificationController.lambda$new$23(iTaskStackListener, message);
        }
    };
    private final TaskStackConsumer mNotifyLockTaskModeChanged = new TaskStackConsumer() { // from class: com.android.server.wm.TaskChangeNotificationController$$ExternalSyntheticLambda16
        @Override // com.android.server.wm.TaskChangeNotificationController.TaskStackConsumer
        public final void accept(ITaskStackListener iTaskStackListener, Message message) {
            TaskChangeNotificationController.lambda$new$24(iTaskStackListener, message);
        }
    };

    @FunctionalInterface
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
    public interface TaskStackConsumer {
        void accept(ITaskStackListener iTaskStackListener, Message message) throws RemoteException;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$new$1(ITaskStackListener iTaskStackListener, Message message) throws RemoteException {
        iTaskStackListener.onTaskCreated(message.arg1, (ComponentName) message.obj);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$new$2(ITaskStackListener iTaskStackListener, Message message) throws RemoteException {
        iTaskStackListener.onTaskRemoved(message.arg1);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$new$3(ITaskStackListener iTaskStackListener, Message message) throws RemoteException {
        iTaskStackListener.onTaskMovedToFront((ActivityManager.RunningTaskInfo) message.obj);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$new$4(ITaskStackListener iTaskStackListener, Message message) throws RemoteException {
        iTaskStackListener.onTaskDescriptionChanged((ActivityManager.RunningTaskInfo) message.obj);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$new$5(ITaskStackListener iTaskStackListener, Message message) throws RemoteException {
        iTaskStackListener.onBackPressedOnTaskRoot((ActivityManager.RunningTaskInfo) message.obj);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$new$6(ITaskStackListener iTaskStackListener, Message message) throws RemoteException {
        iTaskStackListener.onActivityRequestedOrientationChanged(message.arg1, message.arg2);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$new$7(ITaskStackListener iTaskStackListener, Message message) throws RemoteException {
        iTaskStackListener.onTaskRemovalStarted((ActivityManager.RunningTaskInfo) message.obj);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$new$8(ITaskStackListener iTaskStackListener, Message message) throws RemoteException {
        iTaskStackListener.onActivityPinned((String) message.obj, message.sendingUid, message.arg1, message.arg2);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$new$10(ITaskStackListener iTaskStackListener, Message message) throws RemoteException {
        SomeArgs someArgs = (SomeArgs) message.obj;
        iTaskStackListener.onActivityRestartAttempt((ActivityManager.RunningTaskInfo) someArgs.arg1, someArgs.argi1 != 0, someArgs.argi2 != 0, someArgs.argi3 != 0);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$new$11(ITaskStackListener iTaskStackListener, Message message) throws RemoteException {
        iTaskStackListener.onActivityForcedResizable((String) message.obj, message.arg1, message.arg2);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$new$13(ITaskStackListener iTaskStackListener, Message message) throws RemoteException {
        iTaskStackListener.onActivityLaunchOnSecondaryDisplayFailed((ActivityManager.RunningTaskInfo) message.obj, message.arg1);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$new$14(ITaskStackListener iTaskStackListener, Message message) throws RemoteException {
        iTaskStackListener.onActivityLaunchOnSecondaryDisplayRerouted((ActivityManager.RunningTaskInfo) message.obj, message.arg1);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$new$15(ITaskStackListener iTaskStackListener, Message message) throws RemoteException {
        iTaskStackListener.onTaskProfileLocked((ActivityManager.RunningTaskInfo) message.obj, message.arg1);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$16(ITaskStackListener iTaskStackListener, Message message) throws RemoteException {
        TaskSnapshot taskSnapshot = (TaskSnapshot) message.obj;
        if (taskSnapshot != null && taskSnapshot.getSnapshot() != null && taskSnapshot.getSnapshot().isDestroyed()) {
            Slog.i("TaskChangeNotificationController", "NotifyTaskSnapshotChanged " + taskSnapshot + ", is destroy");
            return;
        }
        if (this.mTaskChangeNotificationControllerExt.shouldSkipSendTaskSnapshot(this.mTaskSupervisor, iTaskStackListener, message)) {
            return;
        }
        iTaskStackListener.onTaskSnapshotChanged(message.arg1, (TaskSnapshot) message.obj);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$new$17(ITaskStackListener iTaskStackListener, Message message) throws RemoteException {
        iTaskStackListener.onTaskDisplayChanged(message.arg1, message.arg2);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$new$19(ITaskStackListener iTaskStackListener, Message message) throws RemoteException {
        iTaskStackListener.onRecentTaskListFrozenChanged(message.arg1 != 0);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$new$20(ITaskStackListener iTaskStackListener, Message message) throws RemoteException {
        iTaskStackListener.onTaskFocusChanged(message.arg1, message.arg2 != 0);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$new$21(ITaskStackListener iTaskStackListener, Message message) throws RemoteException {
        iTaskStackListener.onTaskRequestedOrientationChanged(message.arg1, message.arg2);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$new$22(ITaskStackListener iTaskStackListener, Message message) throws RemoteException {
        iTaskStackListener.onActivityRotation(message.arg1);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$new$23(ITaskStackListener iTaskStackListener, Message message) throws RemoteException {
        iTaskStackListener.onTaskMovedToBack((ActivityManager.RunningTaskInfo) message.obj);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$new$24(ITaskStackListener iTaskStackListener, Message message) throws RemoteException {
        iTaskStackListener.onLockTaskModeChanged(message.arg1);
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
    private class MainHandler extends Handler {
        public MainHandler(Looper looper) {
            super(looper);
        }

        @Override // android.os.Handler
        public void handleMessage(Message message) {
            switch (message.what) {
                case 2:
                    TaskChangeNotificationController taskChangeNotificationController = TaskChangeNotificationController.this;
                    taskChangeNotificationController.forAllRemoteListeners(taskChangeNotificationController.mNotifyTaskStackChanged, message);
                    break;
                case 3:
                    TaskChangeNotificationController taskChangeNotificationController2 = TaskChangeNotificationController.this;
                    taskChangeNotificationController2.forAllRemoteListeners(taskChangeNotificationController2.mNotifyActivityPinned, message);
                    break;
                case 4:
                    TaskChangeNotificationController taskChangeNotificationController3 = TaskChangeNotificationController.this;
                    taskChangeNotificationController3.forAllRemoteListeners(taskChangeNotificationController3.mNotifyActivityRestartAttempt, message);
                    break;
                case 6:
                    TaskChangeNotificationController taskChangeNotificationController4 = TaskChangeNotificationController.this;
                    taskChangeNotificationController4.forAllRemoteListeners(taskChangeNotificationController4.mNotifyActivityForcedResizable, message);
                    break;
                case 7:
                    TaskChangeNotificationController taskChangeNotificationController5 = TaskChangeNotificationController.this;
                    taskChangeNotificationController5.forAllRemoteListeners(taskChangeNotificationController5.mNotifyActivityDismissingDockedTask, message);
                    break;
                case 8:
                    TaskChangeNotificationController taskChangeNotificationController6 = TaskChangeNotificationController.this;
                    taskChangeNotificationController6.forAllRemoteListeners(taskChangeNotificationController6.mNotifyTaskCreated, message);
                    break;
                case 9:
                    TaskChangeNotificationController taskChangeNotificationController7 = TaskChangeNotificationController.this;
                    taskChangeNotificationController7.forAllRemoteListeners(taskChangeNotificationController7.mNotifyTaskRemoved, message);
                    break;
                case 10:
                    TaskChangeNotificationController taskChangeNotificationController8 = TaskChangeNotificationController.this;
                    taskChangeNotificationController8.forAllRemoteListeners(taskChangeNotificationController8.mNotifyTaskMovedToFront, message);
                    break;
                case 11:
                    TaskChangeNotificationController taskChangeNotificationController9 = TaskChangeNotificationController.this;
                    taskChangeNotificationController9.forAllRemoteListeners(taskChangeNotificationController9.mNotifyTaskDescriptionChanged, message);
                    break;
                case 12:
                    TaskChangeNotificationController taskChangeNotificationController10 = TaskChangeNotificationController.this;
                    taskChangeNotificationController10.forAllRemoteListeners(taskChangeNotificationController10.mNotifyActivityRequestedOrientationChanged, message);
                    break;
                case 13:
                    TaskChangeNotificationController taskChangeNotificationController11 = TaskChangeNotificationController.this;
                    taskChangeNotificationController11.forAllRemoteListeners(taskChangeNotificationController11.mNotifyTaskRemovalStarted, message);
                    break;
                case 14:
                    TaskChangeNotificationController taskChangeNotificationController12 = TaskChangeNotificationController.this;
                    taskChangeNotificationController12.forAllRemoteListeners(taskChangeNotificationController12.mNotifyTaskProfileLocked, message);
                    break;
                case 15:
                    TaskChangeNotificationController taskChangeNotificationController13 = TaskChangeNotificationController.this;
                    taskChangeNotificationController13.forAllRemoteListeners(taskChangeNotificationController13.mNotifyTaskSnapshotChanged, message);
                    break;
                case 17:
                    TaskChangeNotificationController taskChangeNotificationController14 = TaskChangeNotificationController.this;
                    taskChangeNotificationController14.forAllRemoteListeners(taskChangeNotificationController14.mNotifyActivityUnpinned, message);
                    break;
                case 18:
                    TaskChangeNotificationController taskChangeNotificationController15 = TaskChangeNotificationController.this;
                    taskChangeNotificationController15.forAllRemoteListeners(taskChangeNotificationController15.mNotifyActivityLaunchOnSecondaryDisplayFailed, message);
                    break;
                case 19:
                    TaskChangeNotificationController taskChangeNotificationController16 = TaskChangeNotificationController.this;
                    taskChangeNotificationController16.forAllRemoteListeners(taskChangeNotificationController16.mNotifyActivityLaunchOnSecondaryDisplayRerouted, message);
                    break;
                case 20:
                    TaskChangeNotificationController taskChangeNotificationController17 = TaskChangeNotificationController.this;
                    taskChangeNotificationController17.forAllRemoteListeners(taskChangeNotificationController17.mNotifyBackPressedOnTaskRoot, message);
                    break;
                case 21:
                    TaskChangeNotificationController taskChangeNotificationController18 = TaskChangeNotificationController.this;
                    taskChangeNotificationController18.forAllRemoteListeners(taskChangeNotificationController18.mNotifyTaskDisplayChanged, message);
                    break;
                case 22:
                    TaskChangeNotificationController taskChangeNotificationController19 = TaskChangeNotificationController.this;
                    taskChangeNotificationController19.forAllRemoteListeners(taskChangeNotificationController19.mNotifyTaskListUpdated, message);
                    break;
                case 23:
                    TaskChangeNotificationController taskChangeNotificationController20 = TaskChangeNotificationController.this;
                    taskChangeNotificationController20.forAllRemoteListeners(taskChangeNotificationController20.mNotifyTaskListFrozen, message);
                    break;
                case 24:
                    TaskChangeNotificationController taskChangeNotificationController21 = TaskChangeNotificationController.this;
                    taskChangeNotificationController21.forAllRemoteListeners(taskChangeNotificationController21.mNotifyTaskFocusChanged, message);
                    break;
                case 25:
                    TaskChangeNotificationController taskChangeNotificationController22 = TaskChangeNotificationController.this;
                    taskChangeNotificationController22.forAllRemoteListeners(taskChangeNotificationController22.mNotifyTaskRequestedOrientationChanged, message);
                    break;
                case 26:
                    TaskChangeNotificationController taskChangeNotificationController23 = TaskChangeNotificationController.this;
                    taskChangeNotificationController23.forAllRemoteListeners(taskChangeNotificationController23.mNotifyOnActivityRotation, message);
                    break;
                case 27:
                    TaskChangeNotificationController taskChangeNotificationController24 = TaskChangeNotificationController.this;
                    taskChangeNotificationController24.forAllRemoteListeners(taskChangeNotificationController24.mNotifyTaskMovedToBack, message);
                    break;
                case 28:
                    TaskChangeNotificationController taskChangeNotificationController25 = TaskChangeNotificationController.this;
                    taskChangeNotificationController25.forAllRemoteListeners(taskChangeNotificationController25.mNotifyLockTaskModeChanged, message);
                    break;
            }
            Object obj = message.obj;
            if (obj instanceof SomeArgs) {
                ((SomeArgs) obj).recycle();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public TaskChangeNotificationController(ActivityTaskSupervisor activityTaskSupervisor, Handler handler) {
        this.mTaskSupervisor = activityTaskSupervisor;
        this.mHandler = new MainHandler(handler.getLooper());
    }

    public void registerTaskStackListener(ITaskStackListener iTaskStackListener) {
        if (!(iTaskStackListener instanceof Binder)) {
            if (iTaskStackListener != null) {
                synchronized (this.mRemoteTaskStackListeners) {
                    this.mRemoteTaskStackListeners.register(iTaskStackListener);
                }
                return;
            }
            return;
        }
        synchronized (this.mLocalTaskStackListeners) {
            if (!this.mLocalTaskStackListeners.contains(iTaskStackListener)) {
                if (iTaskStackListener instanceof TaskStackListener) {
                    ((TaskStackListener) iTaskStackListener).setIsLocal();
                }
                this.mLocalTaskStackListeners.add(iTaskStackListener);
            }
        }
    }

    public void unregisterTaskStackListener(ITaskStackListener iTaskStackListener) {
        if (iTaskStackListener instanceof Binder) {
            synchronized (this.mLocalTaskStackListeners) {
                this.mLocalTaskStackListeners.remove(iTaskStackListener);
            }
        } else if (iTaskStackListener != null) {
            synchronized (this.mRemoteTaskStackListeners) {
                this.mRemoteTaskStackListeners.unregister(iTaskStackListener);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void forAllRemoteListeners(TaskStackConsumer taskStackConsumer, Message message) {
        synchronized (this.mRemoteTaskStackListeners) {
            for (int beginBroadcast = this.mRemoteTaskStackListeners.beginBroadcast() - 1; beginBroadcast >= 0; beginBroadcast--) {
                try {
                    taskStackConsumer.accept(this.mRemoteTaskStackListeners.getBroadcastItem(beginBroadcast), message);
                } catch (RemoteException unused) {
                }
            }
            this.mRemoteTaskStackListeners.finishBroadcast();
        }
    }

    private void forAllLocalListeners(TaskStackConsumer taskStackConsumer, Message message) {
        synchronized (this.mLocalTaskStackListeners) {
            for (int size = this.mLocalTaskStackListeners.size() - 1; size >= 0; size--) {
                try {
                    taskStackConsumer.accept(this.mLocalTaskStackListeners.get(size), message);
                } catch (RemoteException unused) {
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void notifyTaskStackChanged() {
        this.mTaskSupervisor.getActivityMetricsLogger().logWindowState();
        this.mHandler.removeMessages(2);
        Message obtainMessage = this.mHandler.obtainMessage(2);
        forAllLocalListeners(this.mNotifyTaskStackChanged, obtainMessage);
        this.mHandler.sendMessageDelayed(obtainMessage, 100L);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void notifyActivityPinned(ActivityRecord activityRecord) {
        this.mHandler.removeMessages(3);
        Message obtainMessage = this.mHandler.obtainMessage(3, activityRecord.getTask().mTaskId, activityRecord.getRootTaskId(), activityRecord.packageName);
        obtainMessage.sendingUid = activityRecord.mUserId;
        forAllLocalListeners(this.mNotifyActivityPinned, obtainMessage);
        obtainMessage.sendToTarget();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void notifyActivityUnpinned() {
        this.mHandler.removeMessages(17);
        Message obtainMessage = this.mHandler.obtainMessage(17);
        forAllLocalListeners(this.mNotifyActivityUnpinned, obtainMessage);
        obtainMessage.sendToTarget();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void notifyActivityRestartAttempt(ActivityManager.RunningTaskInfo runningTaskInfo, boolean z, boolean z2, boolean z3) {
        this.mHandler.removeMessages(4);
        SomeArgs obtain = SomeArgs.obtain();
        obtain.arg1 = runningTaskInfo;
        obtain.argi1 = z ? 1 : 0;
        obtain.argi2 = z2 ? 1 : 0;
        obtain.argi3 = z3 ? 1 : 0;
        Message obtainMessage = this.mHandler.obtainMessage(4, obtain);
        forAllLocalListeners(this.mNotifyActivityRestartAttempt, obtainMessage);
        obtainMessage.sendToTarget();
    }

    void notifyActivityDismissingDockedRootTask() {
        this.mHandler.removeMessages(7);
        Message obtainMessage = this.mHandler.obtainMessage(7);
        forAllLocalListeners(this.mNotifyActivityDismissingDockedTask, obtainMessage);
        obtainMessage.sendToTarget();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void notifyActivityForcedResizable(int i, int i2, String str) {
        this.mHandler.removeMessages(6);
        Message obtainMessage = this.mHandler.obtainMessage(6, i, i2, str);
        forAllLocalListeners(this.mNotifyActivityForcedResizable, obtainMessage);
        obtainMessage.sendToTarget();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void notifyActivityLaunchOnSecondaryDisplayFailed(TaskInfo taskInfo, int i) {
        this.mHandler.removeMessages(18);
        Message obtainMessage = this.mHandler.obtainMessage(18, i, 0, taskInfo);
        forAllLocalListeners(this.mNotifyActivityLaunchOnSecondaryDisplayFailed, obtainMessage);
        obtainMessage.sendToTarget();
    }

    void notifyActivityLaunchOnSecondaryDisplayRerouted(TaskInfo taskInfo, int i) {
        this.mHandler.removeMessages(19);
        Message obtainMessage = this.mHandler.obtainMessage(19, i, 0, taskInfo);
        forAllLocalListeners(this.mNotifyActivityLaunchOnSecondaryDisplayRerouted, obtainMessage);
        obtainMessage.sendToTarget();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void notifyTaskCreated(int i, ComponentName componentName) {
        Message obtainMessage = this.mHandler.obtainMessage(8, i, 0, componentName);
        forAllLocalListeners(this.mNotifyTaskCreated, obtainMessage);
        obtainMessage.sendToTarget();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void notifyTaskRemoved(int i) {
        Message obtainMessage = this.mHandler.obtainMessage(9, i, 0);
        forAllLocalListeners(this.mNotifyTaskRemoved, obtainMessage);
        obtainMessage.sendToTarget();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void notifyTaskMovedToFront(TaskInfo taskInfo) {
        Message obtainMessage = this.mHandler.obtainMessage(10, taskInfo);
        forAllLocalListeners(this.mNotifyTaskMovedToFront, obtainMessage);
        obtainMessage.sendToTarget();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void notifyTaskDescriptionChanged(TaskInfo taskInfo) {
        Message obtainMessage = this.mHandler.obtainMessage(11, taskInfo);
        forAllLocalListeners(this.mNotifyTaskDescriptionChanged, obtainMessage);
        obtainMessage.sendToTarget();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void notifyActivityRequestedOrientationChanged(int i, int i2) {
        Message obtainMessage = this.mHandler.obtainMessage(12, i, i2);
        forAllLocalListeners(this.mNotifyActivityRequestedOrientationChanged, obtainMessage);
        obtainMessage.sendToTarget();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void notifyTaskRemovalStarted(ActivityManager.RunningTaskInfo runningTaskInfo) {
        Message obtainMessage = this.mHandler.obtainMessage(13, runningTaskInfo);
        forAllLocalListeners(this.mNotifyTaskRemovalStarted, obtainMessage);
        obtainMessage.sendToTarget();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void notifyTaskProfileLocked(ActivityManager.RunningTaskInfo runningTaskInfo, int i) {
        Message obtainMessage = this.mHandler.obtainMessage(14, i, 0, runningTaskInfo);
        forAllLocalListeners(this.mNotifyTaskProfileLocked, obtainMessage);
        obtainMessage.sendToTarget();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void notifyTaskSnapshotChanged(int i, TaskSnapshot taskSnapshot) {
        Message obtainMessage = this.mHandler.obtainMessage(15, i, 0, taskSnapshot);
        forAllLocalListeners(this.mNotifyTaskSnapshotChanged, obtainMessage);
        obtainMessage.sendToTarget();
    }

    void notifyBackPressedOnTaskRoot(TaskInfo taskInfo) {
        Message obtainMessage = this.mHandler.obtainMessage(20, taskInfo);
        forAllLocalListeners(this.mNotifyBackPressedOnTaskRoot, obtainMessage);
        obtainMessage.sendToTarget();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void notifyTaskDisplayChanged(int i, int i2) {
        Message obtainMessage = this.mHandler.obtainMessage(21, i, i2);
        forAllLocalListeners(this.mNotifyTaskDisplayChanged, obtainMessage);
        obtainMessage.sendToTarget();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void notifyTaskListUpdated() {
        Message obtainMessage = this.mHandler.obtainMessage(22);
        forAllLocalListeners(this.mNotifyTaskListUpdated, obtainMessage);
        obtainMessage.sendToTarget();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void notifyTaskListFrozen(boolean z) {
        Message obtainMessage = this.mHandler.obtainMessage(23, z ? 1 : 0, 0);
        forAllLocalListeners(this.mNotifyTaskListFrozen, obtainMessage);
        obtainMessage.sendToTarget();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void notifyTaskFocusChanged(int i, boolean z) {
        Message obtainMessage = this.mHandler.obtainMessage(24, i, z ? 1 : 0);
        forAllLocalListeners(this.mNotifyTaskFocusChanged, obtainMessage);
        obtainMessage.sendToTarget();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void notifyTaskRequestedOrientationChanged(int i, int i2) {
        Message obtainMessage = this.mHandler.obtainMessage(25, i, i2);
        forAllLocalListeners(this.mNotifyTaskRequestedOrientationChanged, obtainMessage);
        obtainMessage.sendToTarget();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void notifyOnActivityRotation(int i) {
        Message obtainMessage = this.mHandler.obtainMessage(26, i, 0);
        forAllLocalListeners(this.mNotifyOnActivityRotation, obtainMessage);
        obtainMessage.sendToTarget();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void notifyTaskMovedToBack(TaskInfo taskInfo) {
        Message obtainMessage = this.mHandler.obtainMessage(27, taskInfo);
        forAllLocalListeners(this.mNotifyTaskMovedToBack, obtainMessage);
        obtainMessage.sendToTarget();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void notifyLockTaskModeChanged(int i) {
        Message obtainMessage = this.mHandler.obtainMessage(28, i, 0);
        forAllLocalListeners(this.mNotifyLockTaskModeChanged, obtainMessage);
        obtainMessage.sendToTarget();
    }
}
