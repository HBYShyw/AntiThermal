package com.oplus.wrapper.app;

import android.app.IAssistDataReceiver;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.RemoteException;

/* loaded from: classes.dex */
public interface IAssistDataReceiver {
    void onHandleAssistData(Bundle bundle) throws RemoteException;

    void onHandleAssistScreenshot(Bitmap bitmap) throws RemoteException;

    /* loaded from: classes.dex */
    public static abstract class Stub implements IInterface, IAssistDataReceiver {
        private final android.app.IAssistDataReceiver mTarget = new IAssistDataReceiver.Stub() { // from class: com.oplus.wrapper.app.IAssistDataReceiver.Stub.1
            public void onHandleAssistData(Bundle bundle) throws RemoteException {
                Stub.this.onHandleAssistData(bundle);
            }

            public void onHandleAssistScreenshot(Bitmap bitmap) throws RemoteException {
                Stub.this.onHandleAssistScreenshot(bitmap);
            }
        };

        public static IAssistDataReceiver asInterface(IBinder obj) {
            return new Proxy(IAssistDataReceiver.Stub.asInterface(obj));
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return this.mTarget.asBinder();
        }

        /* loaded from: classes.dex */
        private static class Proxy implements IAssistDataReceiver {
            private final android.app.IAssistDataReceiver mTarget;

            Proxy(android.app.IAssistDataReceiver target) {
                this.mTarget = target;
            }

            @Override // com.oplus.wrapper.app.IAssistDataReceiver
            public void onHandleAssistData(Bundle resultData) throws RemoteException {
                this.mTarget.onHandleAssistData(resultData);
            }

            @Override // com.oplus.wrapper.app.IAssistDataReceiver
            public void onHandleAssistScreenshot(Bitmap screenshot) throws RemoteException {
                this.mTarget.onHandleAssistScreenshot(screenshot);
            }
        }
    }
}
