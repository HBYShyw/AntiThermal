package com.oplus.wrapper.view;

import android.graphics.Rect;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.RemoteException;
import android.view.IRecentsAnimationController;
import com.oplus.wrapper.window.PictureInPictureSurfaceTransaction;
import com.oplus.wrapper.window.TaskSnapshot;

/* loaded from: classes.dex */
public interface IRecentsAnimationController {
    void animateNavigationBarToApp(long j) throws RemoteException;

    void cleanupScreenshot() throws RemoteException;

    void detachNavigationBarFromApp(boolean z) throws RemoteException;

    void finish(boolean z, boolean z2) throws RemoteException;

    boolean removeTask(int i) throws RemoteException;

    TaskSnapshot screenshotTask(int i) throws RemoteException;

    void setAnimationTargetsBehindSystemBars(boolean z) throws RemoteException;

    void setDeferCancelUntilNextTransition(boolean z, boolean z2) throws RemoteException;

    void setFinishTaskTransaction(int i, PictureInPictureSurfaceTransaction pictureInPictureSurfaceTransaction, android.view.SurfaceControl surfaceControl) throws RemoteException;

    void setInputConsumerEnabled(boolean z) throws RemoteException;

    void setWillFinishToHome(boolean z) throws RemoteException;

    /* loaded from: classes.dex */
    public static abstract class Stub implements IInterface, IRecentsAnimationController {
        private final android.view.IRecentsAnimationController mTarget = new IRecentsAnimationController.Stub() { // from class: com.oplus.wrapper.view.IRecentsAnimationController.Stub.1
            public android.window.TaskSnapshot screenshotTask(int i) throws RemoteException {
                return Stub.this.screenshotTask(i).getTaskSnapshot();
            }

            public void setFinishTaskTransaction(int i, android.window.PictureInPictureSurfaceTransaction pictureInPictureSurfaceTransaction, android.view.SurfaceControl surfaceControl) throws RemoteException {
                Stub.this.setFinishTaskTransaction(i, new PictureInPictureSurfaceTransaction(pictureInPictureSurfaceTransaction), surfaceControl);
            }

            public void finish(boolean b, boolean b1) throws RemoteException {
                Stub.this.finish(b, b1);
            }

            public void setInputConsumerEnabled(boolean b) throws RemoteException {
                Stub.this.setInputConsumerEnabled(b);
            }

            public void setAnimationTargetsBehindSystemBars(boolean b) throws RemoteException {
                Stub.this.setAnimationTargetsBehindSystemBars(b);
            }

            public void cleanupScreenshot() throws RemoteException {
                Stub.this.cleanupScreenshot();
            }

            public void setDeferCancelUntilNextTransition(boolean b, boolean b1) throws RemoteException {
                Stub.this.setDeferCancelUntilNextTransition(b, b1);
            }

            public void setWillFinishToHome(boolean b) throws RemoteException {
                Stub.this.setWillFinishToHome(b);
            }

            public boolean removeTask(int i) throws RemoteException {
                return Stub.this.removeTask(i);
            }

            public void detachNavigationBarFromApp(boolean b) throws RemoteException {
                Stub.this.detachNavigationBarFromApp(b);
            }

            public void animateNavigationBarToApp(long l) throws RemoteException {
                Stub.this.animateNavigationBarToApp(l);
            }

            public void finishZoom(boolean moveHomeToTop, boolean sendUserLeaveHint, int taskId, int type, Rect rect, int orientation, Bundle bOptions) throws RemoteException {
            }

            public void finishPutt(int type, int taskId, Rect rect, int orientation, Bundle bOptions) throws RemoteException {
            }

            public void enterZoomFromRecent(android.view.SurfaceControl zoomRootLeash, android.view.SurfaceControl targetLeash, Rect windowCrop, Rect endRect, Bundle bOptions) throws RemoteException {
            }
        };

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return this.mTarget.asBinder();
        }

        public static IRecentsAnimationController asInterface(IBinder obj) {
            return new Proxy(IRecentsAnimationController.Stub.asInterface(obj));
        }

        /* loaded from: classes.dex */
        private static class Proxy implements IRecentsAnimationController {
            private final android.view.IRecentsAnimationController mTarget;

            Proxy(android.view.IRecentsAnimationController target) {
                this.mTarget = target;
            }

            @Override // com.oplus.wrapper.view.IRecentsAnimationController
            public void animateNavigationBarToApp(long duration) throws RemoteException {
                this.mTarget.animateNavigationBarToApp(duration);
            }

            @Override // com.oplus.wrapper.view.IRecentsAnimationController
            public void cleanupScreenshot() throws RemoteException {
                this.mTarget.cleanupScreenshot();
            }

            @Override // com.oplus.wrapper.view.IRecentsAnimationController
            public void detachNavigationBarFromApp(boolean moveHomeToTop) throws RemoteException {
                this.mTarget.detachNavigationBarFromApp(moveHomeToTop);
            }

            @Override // com.oplus.wrapper.view.IRecentsAnimationController
            public void finish(boolean moveHomeToTop, boolean sendUserLeaveHint) throws RemoteException {
                this.mTarget.finish(moveHomeToTop, sendUserLeaveHint);
            }

            @Override // com.oplus.wrapper.view.IRecentsAnimationController
            public boolean removeTask(int taskId) throws RemoteException {
                return this.mTarget.removeTask(taskId);
            }

            @Override // com.oplus.wrapper.view.IRecentsAnimationController
            public TaskSnapshot screenshotTask(int taskId) throws RemoteException {
                return new TaskSnapshot(this.mTarget.screenshotTask(taskId));
            }

            @Override // com.oplus.wrapper.view.IRecentsAnimationController
            public void setAnimationTargetsBehindSystemBars(boolean behindSystemBars) throws RemoteException {
                this.mTarget.setAnimationTargetsBehindSystemBars(behindSystemBars);
            }

            @Override // com.oplus.wrapper.view.IRecentsAnimationController
            public void setDeferCancelUntilNextTransition(boolean defer, boolean screenshot) throws RemoteException {
                this.mTarget.setDeferCancelUntilNextTransition(defer, screenshot);
            }

            @Override // com.oplus.wrapper.view.IRecentsAnimationController
            public void setInputConsumerEnabled(boolean enabled) throws RemoteException {
                this.mTarget.setInputConsumerEnabled(enabled);
            }

            @Override // com.oplus.wrapper.view.IRecentsAnimationController
            public void setWillFinishToHome(boolean willFinishToHome) throws RemoteException {
                this.mTarget.setWillFinishToHome(willFinishToHome);
            }

            @Override // com.oplus.wrapper.view.IRecentsAnimationController
            public void setFinishTaskTransaction(int taskId, PictureInPictureSurfaceTransaction finishTransaction, android.view.SurfaceControl overlay) throws RemoteException {
                this.mTarget.setFinishTaskTransaction(taskId, finishTransaction.getPictureInPictureSurfaceTransaction(), overlay);
            }
        }
    }
}
