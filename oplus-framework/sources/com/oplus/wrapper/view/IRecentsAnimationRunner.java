package com.oplus.wrapper.view;

import android.graphics.Rect;
import android.os.IBinder;
import android.os.IInterface;
import android.os.RemoteException;
import android.view.IRecentsAnimationRunner;
import android.window.TaskSnapshot;

/* loaded from: classes.dex */
public interface IRecentsAnimationRunner {

    /* loaded from: classes.dex */
    public static abstract class Stub implements IInterface, IRecentsAnimationRunner {
        private final android.view.IRecentsAnimationRunner mTarget = new IRecentsAnimationRunner.Stub() { // from class: com.oplus.wrapper.view.IRecentsAnimationRunner.Stub.1
            public void onAnimationCanceled(int[] ints, TaskSnapshot[] taskSnapshots) throws RemoteException {
            }

            public void onAnimationStart(android.view.IRecentsAnimationController iRecentsAnimationController, android.view.RemoteAnimationTarget[] remoteAnimationTargets, android.view.RemoteAnimationTarget[] remoteAnimationTargets1, Rect rect, Rect rect1) throws RemoteException {
            }

            public void onTasksAppeared(android.view.RemoteAnimationTarget[] remoteAnimationTargets) throws RemoteException {
            }
        };

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return this.mTarget.asBinder();
        }

        public static IRecentsAnimationRunner asInterface(IBinder obj) {
            return new Proxy(IRecentsAnimationRunner.Stub.asInterface(obj));
        }

        /* loaded from: classes.dex */
        private static class Proxy implements IRecentsAnimationRunner {
            private final android.view.IRecentsAnimationRunner mTarget;

            Proxy(android.view.IRecentsAnimationRunner target) {
                this.mTarget = target;
            }
        }
    }
}
