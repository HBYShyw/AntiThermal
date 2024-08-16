package com.oplus.screenshot;

import android.graphics.Rect;
import android.os.CancellationSignal;
import android.os.IBinder;
import android.os.ICancellationSignal;
import android.os.IInterface;
import android.os.RemoteException;
import android.view.Surface;
import com.oplus.screenshot.IOplusScrollCaptureCallbacksInner;
import com.oplus.screenshot.IOplusScrollCaptureConnectionInner;
import com.oplus.util.OplusLog;

/* loaded from: classes.dex */
public interface IOplusScrollCaptureConnection extends IInterface {
    void close() throws RemoteException;

    CancellationSignal endCapture() throws RemoteException;

    CancellationSignal requestImage(Rect rect) throws RemoteException;

    CancellationSignal startCapture(Surface surface, IOplusScrollCaptureCallbacks iOplusScrollCaptureCallbacks) throws RemoteException;

    /* loaded from: classes.dex */
    public static class Stub implements IOplusScrollCaptureConnection {
        private static final String TAG = "IOplusScrollCaptureConnection";
        private final IOplusScrollCaptureConnectionInner mConnection;

        private Stub(IOplusScrollCaptureConnectionInner connection) {
            this.mConnection = connection;
        }

        @Override // com.oplus.screenshot.IOplusScrollCaptureConnection
        public CancellationSignal startCapture(Surface surface, IOplusScrollCaptureCallbacks callbacks) throws RemoteException {
            CancellationSignal signal = new CancellationSignal();
            if (surface == null) {
                OplusLog.e(TAG, "surface is null.");
                return signal;
            }
            if (callbacks == null) {
                OplusLog.e(TAG, "callbacks is null.");
                return signal;
            }
            ICancellationSignal remote = this.mConnection.startCapture(surface, new ScrollCaptureCallbacks(callbacks));
            signal.setRemote(remote);
            return signal;
        }

        @Override // com.oplus.screenshot.IOplusScrollCaptureConnection
        public CancellationSignal requestImage(Rect captureArea) throws RemoteException {
            ICancellationSignal remote = this.mConnection.requestImage(captureArea);
            CancellationSignal signal = new CancellationSignal();
            signal.setRemote(remote);
            return signal;
        }

        @Override // com.oplus.screenshot.IOplusScrollCaptureConnection
        public CancellationSignal endCapture() throws RemoteException {
            ICancellationSignal remote = this.mConnection.endCapture();
            CancellationSignal signal = new CancellationSignal();
            signal.setRemote(remote);
            return signal;
        }

        @Override // com.oplus.screenshot.IOplusScrollCaptureConnection
        public void close() throws RemoteException {
            this.mConnection.close();
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return this.mConnection.asBinder();
        }

        public static IOplusScrollCaptureConnection asInterface(IBinder binder) {
            IOplusScrollCaptureConnectionInner connection = IOplusScrollCaptureConnectionInner.Stub.asInterface(binder);
            if (connection == null) {
                return null;
            }
            return new Stub(connection);
        }

        /* loaded from: classes.dex */
        private static class ScrollCaptureCallbacks extends IOplusScrollCaptureCallbacksInner.Stub {
            private final IOplusScrollCaptureCallbacks mLocal;

            private ScrollCaptureCallbacks(IOplusScrollCaptureCallbacks mLocal) {
                this.mLocal = mLocal;
            }

            @Override // com.oplus.screenshot.IOplusScrollCaptureCallbacksInner
            public void onCaptureStarted() throws RemoteException {
                this.mLocal.onCaptureStarted();
            }

            @Override // com.oplus.screenshot.IOplusScrollCaptureCallbacksInner
            public void onImageRequestCompleted(int flags, Rect capturedArea, Rect screenArea) throws RemoteException {
                this.mLocal.onImageRequestCompleted(flags, capturedArea, screenArea);
            }

            @Override // com.oplus.screenshot.IOplusScrollCaptureCallbacksInner
            public void onCaptureEnded() throws RemoteException {
                this.mLocal.onCaptureEnded();
            }
        }
    }
}
