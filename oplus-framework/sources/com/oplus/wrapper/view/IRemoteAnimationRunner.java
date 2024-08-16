package com.oplus.wrapper.view;

import android.os.IBinder;
import android.os.IInterface;
import android.os.RemoteException;
import android.view.IRemoteAnimationFinishedCallback;
import android.view.IRemoteAnimationRunner;
import com.oplus.wrapper.view.IRemoteAnimationFinishedCallback;

/* loaded from: classes.dex */
public interface IRemoteAnimationRunner {
    void onAnimationCancelled() throws RemoteException;

    void onAnimationStart(int i, RemoteAnimationTarget[] remoteAnimationTargetArr, RemoteAnimationTarget[] remoteAnimationTargetArr2, RemoteAnimationTarget[] remoteAnimationTargetArr3, IRemoteAnimationFinishedCallback iRemoteAnimationFinishedCallback) throws RemoteException;

    /* loaded from: classes.dex */
    public static abstract class Stub implements IInterface, IRemoteAnimationRunner {
        private final android.view.IRemoteAnimationRunner mTarget = new IRemoteAnimationRunner.Stub() { // from class: com.oplus.wrapper.view.IRemoteAnimationRunner.Stub.1
            public void onAnimationStart(int i, android.view.RemoteAnimationTarget[] remoteAnimationTargets, android.view.RemoteAnimationTarget[] remoteAnimationTargets1, android.view.RemoteAnimationTarget[] remoteAnimationTargets2, final android.view.IRemoteAnimationFinishedCallback iRemoteAnimationFinishedCallback) throws RemoteException {
                IRemoteAnimationFinishedCallback wrapperRemoteAnimationFinishedCallback = null;
                if (iRemoteAnimationFinishedCallback != null) {
                    wrapperRemoteAnimationFinishedCallback = new IRemoteAnimationFinishedCallback.Stub() { // from class: com.oplus.wrapper.view.IRemoteAnimationRunner.Stub.1.1
                        @Override // com.oplus.wrapper.view.IRemoteAnimationFinishedCallback
                        public void onAnimationFinished() throws RemoteException {
                            iRemoteAnimationFinishedCallback.onAnimationFinished();
                        }
                    };
                }
                Stub.this.onAnimationStart(i, RemoteAnimationTarget.getWrapperRemoteAnimationTarget(remoteAnimationTargets), RemoteAnimationTarget.getWrapperRemoteAnimationTarget(remoteAnimationTargets1), RemoteAnimationTarget.getWrapperRemoteAnimationTarget(remoteAnimationTargets2), wrapperRemoteAnimationFinishedCallback);
            }

            public void onAnimationCancelled() throws RemoteException {
            }
        };

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return this.mTarget.asBinder();
        }

        public static IRemoteAnimationRunner asInterface(IBinder obj) {
            return new Proxy(IRemoteAnimationRunner.Stub.asInterface(obj));
        }

        /* loaded from: classes.dex */
        private static class Proxy implements IRemoteAnimationRunner {
            private final android.view.IRemoteAnimationRunner mTarget;

            Proxy(android.view.IRemoteAnimationRunner target) {
                this.mTarget = target;
            }

            @Override // com.oplus.wrapper.view.IRemoteAnimationRunner
            public void onAnimationStart(int transit, RemoteAnimationTarget[] apps, RemoteAnimationTarget[] wallpapers, RemoteAnimationTarget[] nonApps, final IRemoteAnimationFinishedCallback finishedCallback) throws RemoteException {
                IRemoteAnimationFinishedCallback.Stub stub = null;
                if (finishedCallback != null) {
                    stub = new IRemoteAnimationFinishedCallback.Stub() { // from class: com.oplus.wrapper.view.IRemoteAnimationRunner.Stub.Proxy.1
                        public void onAnimationFinished() throws RemoteException {
                            finishedCallback.onAnimationFinished();
                        }
                    };
                }
                this.mTarget.onAnimationStart(transit, RemoteAnimationTarget.getInternalRemoteAnimationTarget(apps), RemoteAnimationTarget.getInternalRemoteAnimationTarget(wallpapers), RemoteAnimationTarget.getInternalRemoteAnimationTarget(nonApps), stub);
            }

            @Override // com.oplus.wrapper.view.IRemoteAnimationRunner
            public void onAnimationCancelled() throws RemoteException {
                this.mTarget.onAnimationCancelled();
            }
        }
    }
}
