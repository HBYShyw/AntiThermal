package com.oplus.wrapper.window;

import android.os.IBinder;
import android.os.IInterface;
import android.os.RemoteException;
import android.view.SurfaceControl;
import android.window.IRemoteTransition;
import android.window.IRemoteTransitionFinishedCallback;
import com.oplus.wrapper.window.IRemoteTransitionFinishedCallback;

/* loaded from: classes.dex */
public interface IRemoteTransition {
    void mergeAnimation(IBinder iBinder, TransitionInfo transitionInfo, SurfaceControl.Transaction transaction, IBinder iBinder2, IRemoteTransitionFinishedCallback iRemoteTransitionFinishedCallback) throws RemoteException;

    void startAnimation(IBinder iBinder, TransitionInfo transitionInfo, SurfaceControl.Transaction transaction, IRemoteTransitionFinishedCallback iRemoteTransitionFinishedCallback) throws RemoteException;

    /* loaded from: classes.dex */
    public static abstract class Stub implements IInterface, IRemoteTransition {
        private final android.window.IRemoteTransition mTarget = new IRemoteTransition.Stub() { // from class: com.oplus.wrapper.window.IRemoteTransition.Stub.1
            public void startAnimation(IBinder token, android.window.TransitionInfo info, SurfaceControl.Transaction t, final android.window.IRemoteTransitionFinishedCallback iRemoteTransitionFinishedCallback) throws RemoteException {
                IRemoteTransitionFinishedCallback wrapperRemoteTransitionFinishedCallback = null;
                if (iRemoteTransitionFinishedCallback != null) {
                    wrapperRemoteTransitionFinishedCallback = new IRemoteTransitionFinishedCallback.Stub() { // from class: com.oplus.wrapper.window.IRemoteTransition.Stub.1.1
                        @Override // com.oplus.wrapper.window.IRemoteTransitionFinishedCallback
                        public void onTransitionFinished(WindowContainerTransaction wct, SurfaceControl.Transaction sct) throws RemoteException {
                            iRemoteTransitionFinishedCallback.onTransitionFinished(wct.get(), sct);
                        }
                    };
                }
                Stub.this.startAnimation(token, new TransitionInfo(info), t, wrapperRemoteTransitionFinishedCallback);
            }

            public void mergeAnimation(IBinder transition, android.window.TransitionInfo info, SurfaceControl.Transaction t, IBinder mergeTarget, final android.window.IRemoteTransitionFinishedCallback finishCallback) throws RemoteException {
                IRemoteTransitionFinishedCallback wrapperRemoteTransitionFinishedCallback = null;
                if (finishCallback != null) {
                    wrapperRemoteTransitionFinishedCallback = new IRemoteTransitionFinishedCallback.Stub() { // from class: com.oplus.wrapper.window.IRemoteTransition.Stub.1.2
                        @Override // com.oplus.wrapper.window.IRemoteTransitionFinishedCallback
                        public void onTransitionFinished(WindowContainerTransaction wct, SurfaceControl.Transaction sct) throws RemoteException {
                            finishCallback.onTransitionFinished(wct.get(), sct);
                        }
                    };
                }
                Stub.this.mergeAnimation(transition, new TransitionInfo(info), t, mergeTarget, wrapperRemoteTransitionFinishedCallback);
            }
        };

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return this.mTarget.asBinder();
        }

        public static IRemoteTransition asInterface(IBinder obj) {
            return new Proxy(IRemoteTransition.Stub.asInterface(obj));
        }

        /* loaded from: classes.dex */
        private static class Proxy implements IRemoteTransition {
            private final android.window.IRemoteTransition mTarget;

            Proxy(android.window.IRemoteTransition target) {
                this.mTarget = target;
            }

            @Override // com.oplus.wrapper.window.IRemoteTransition
            public void startAnimation(IBinder token, TransitionInfo info, SurfaceControl.Transaction t, final IRemoteTransitionFinishedCallback finishCallback) throws RemoteException {
                android.window.IRemoteTransitionFinishedCallback iRemoteTransitionFinishedCallback = null;
                if (finishCallback != null) {
                    iRemoteTransitionFinishedCallback = new IRemoteTransitionFinishedCallback.Stub() { // from class: com.oplus.wrapper.window.IRemoteTransition.Stub.Proxy.1
                        public void onTransitionFinished(android.window.WindowContainerTransaction wct, SurfaceControl.Transaction sct) throws RemoteException {
                            finishCallback.onTransitionFinished(new WindowContainerTransaction(wct), sct);
                        }
                    };
                }
                this.mTarget.startAnimation(token, info.get(), t, iRemoteTransitionFinishedCallback);
            }

            @Override // com.oplus.wrapper.window.IRemoteTransition
            public void mergeAnimation(IBinder transition, TransitionInfo info, SurfaceControl.Transaction t, IBinder mergeTarget, final IRemoteTransitionFinishedCallback finishCallback) throws RemoteException {
                IRemoteTransitionFinishedCallback.Stub stub = null;
                if (finishCallback != null) {
                    stub = new IRemoteTransitionFinishedCallback.Stub() { // from class: com.oplus.wrapper.window.IRemoteTransition.Stub.Proxy.2
                        public void onTransitionFinished(android.window.WindowContainerTransaction wct, SurfaceControl.Transaction sct) throws RemoteException {
                            finishCallback.onTransitionFinished(new WindowContainerTransaction(wct), sct);
                        }
                    };
                }
                this.mTarget.mergeAnimation(transition, info.get(), t, mergeTarget, stub);
            }
        }
    }
}
