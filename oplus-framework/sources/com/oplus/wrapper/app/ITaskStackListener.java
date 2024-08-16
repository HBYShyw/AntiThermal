package com.oplus.wrapper.app;

import android.app.ActivityManager;
import android.app.ITaskStackListener;
import android.content.ComponentName;
import android.os.IBinder;
import android.os.IInterface;
import android.os.RemoteException;
import com.oplus.wrapper.window.TaskSnapshot;

/* loaded from: classes.dex */
public interface ITaskStackListener {
    void onActivityPinned(String str, int i, int i2, int i3) throws RemoteException;

    void onActivityUnpinned() throws RemoteException;

    void onTaskDescriptionChanged(ActivityManager.RunningTaskInfo runningTaskInfo) throws RemoteException;

    void onTaskSnapshotChanged(int i, TaskSnapshot taskSnapshot) throws RemoteException;

    /* loaded from: classes.dex */
    public static abstract class Stub implements IInterface, ITaskStackListener {
        private final android.app.ITaskStackListener mTarget = new ITaskStackListener.Stub() { // from class: com.oplus.wrapper.app.ITaskStackListener.Stub.1
            public void onTaskStackChanged() throws RemoteException {
            }

            public void onActivityPinned(String s, int i, int i1, int i2) throws RemoteException {
                Stub.this.onActivityPinned(s, i, i1, i2);
            }

            public void onActivityUnpinned() throws RemoteException {
                Stub.this.onActivityUnpinned();
            }

            public void onActivityRestartAttempt(ActivityManager.RunningTaskInfo runningTaskInfo, boolean b, boolean b1, boolean b2) throws RemoteException {
            }

            public void onActivityForcedResizable(String s, int i, int i1) throws RemoteException {
            }

            public void onActivityDismissingDockedTask() throws RemoteException {
            }

            public void onActivityLaunchOnSecondaryDisplayFailed(ActivityManager.RunningTaskInfo runningTaskInfo, int i) throws RemoteException {
            }

            public void onActivityLaunchOnSecondaryDisplayRerouted(ActivityManager.RunningTaskInfo runningTaskInfo, int i) throws RemoteException {
            }

            public void onTaskCreated(int i, ComponentName componentName) throws RemoteException {
            }

            public void onTaskRemoved(int i) throws RemoteException {
            }

            public void onTaskMovedToFront(ActivityManager.RunningTaskInfo runningTaskInfo) throws RemoteException {
            }

            public void onTaskDescriptionChanged(ActivityManager.RunningTaskInfo runningTaskInfo) throws RemoteException {
                Stub.this.onTaskDescriptionChanged(runningTaskInfo);
            }

            public void onActivityRequestedOrientationChanged(int i, int i1) throws RemoteException {
            }

            public void onTaskRemovalStarted(ActivityManager.RunningTaskInfo runningTaskInfo) throws RemoteException {
            }

            public void onTaskProfileLocked(ActivityManager.RunningTaskInfo runningTaskInfo, int userid) throws RemoteException {
            }

            public void onTaskSnapshotChanged(int i, android.window.TaskSnapshot taskSnapshot) throws RemoteException {
                if (taskSnapshot != null) {
                    Stub.this.onTaskSnapshotChanged(i, new TaskSnapshot(taskSnapshot));
                }
            }

            public void onBackPressedOnTaskRoot(ActivityManager.RunningTaskInfo runningTaskInfo) throws RemoteException {
            }

            public void onTaskDisplayChanged(int i, int i1) throws RemoteException {
            }

            public void onRecentTaskListUpdated() throws RemoteException {
            }

            public void onRecentTaskListFrozenChanged(boolean b) throws RemoteException {
            }

            public void onTaskFocusChanged(int i, boolean b) throws RemoteException {
            }

            public void onTaskRequestedOrientationChanged(int i, int i1) throws RemoteException {
            }

            public void onActivityRotation(int i) throws RemoteException {
            }

            public void onTaskMovedToBack(ActivityManager.RunningTaskInfo runningTaskInfo) throws RemoteException {
            }

            public void onLockTaskModeChanged(int i) throws RemoteException {
            }
        };

        public static ITaskStackListener asInterface(IBinder obj) {
            return new Proxy(ITaskStackListener.Stub.asInterface(obj));
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return this.mTarget.asBinder();
        }

        /* loaded from: classes.dex */
        private static class Proxy implements ITaskStackListener {
            private final android.app.ITaskStackListener mTarget;

            Proxy(android.app.ITaskStackListener target) {
                this.mTarget = target;
            }

            @Override // com.oplus.wrapper.app.ITaskStackListener
            public void onTaskSnapshotChanged(int taskId, TaskSnapshot snapshot) throws RemoteException {
                this.mTarget.onTaskSnapshotChanged(taskId, snapshot.getTaskSnapshot());
            }

            @Override // com.oplus.wrapper.app.ITaskStackListener
            public void onActivityPinned(String packageName, int userId, int taskId, int stackId) throws RemoteException {
                this.mTarget.onActivityPinned(packageName, userId, taskId, stackId);
            }

            @Override // com.oplus.wrapper.app.ITaskStackListener
            public void onActivityUnpinned() throws RemoteException {
                this.mTarget.onActivityUnpinned();
            }

            @Override // com.oplus.wrapper.app.ITaskStackListener
            public void onTaskDescriptionChanged(ActivityManager.RunningTaskInfo taskInfo) throws RemoteException {
                this.mTarget.onTaskDescriptionChanged(taskInfo);
            }
        }
    }
}
