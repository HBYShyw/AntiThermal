package com.oplus.screenshot;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import java.util.List;

/* loaded from: classes.dex */
public interface IOplusScrollCaptureResponseListener extends IInterface {
    public static final String DESCRIPTOR = "com.oplus.screenshot.IOplusScrollCaptureResponseListener";

    void onScrollCaptureResponse(List<OplusScrollCaptureResponse> list) throws RemoteException;

    /* loaded from: classes.dex */
    public static class Default implements IOplusScrollCaptureResponseListener {
        @Override // com.oplus.screenshot.IOplusScrollCaptureResponseListener
        public void onScrollCaptureResponse(List<OplusScrollCaptureResponse> responses) throws RemoteException {
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return null;
        }
    }

    /* loaded from: classes.dex */
    public static abstract class Stub extends Binder implements IOplusScrollCaptureResponseListener {
        static final int TRANSACTION_onScrollCaptureResponse = 1;

        public Stub() {
            attachInterface(this, IOplusScrollCaptureResponseListener.DESCRIPTOR);
        }

        public static IOplusScrollCaptureResponseListener asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(IOplusScrollCaptureResponseListener.DESCRIPTOR);
            if (iin != null && (iin instanceof IOplusScrollCaptureResponseListener)) {
                return (IOplusScrollCaptureResponseListener) iin;
            }
            return new Proxy(obj);
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return this;
        }

        public static String getDefaultTransactionName(int transactionCode) {
            switch (transactionCode) {
                case 1:
                    return "onScrollCaptureResponse";
                default:
                    return null;
            }
        }

        public String getTransactionName(int transactionCode) {
            return getDefaultTransactionName(transactionCode);
        }

        @Override // android.os.Binder
        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            if (code >= 1 && code <= 16777215) {
                data.enforceInterface(IOplusScrollCaptureResponseListener.DESCRIPTOR);
            }
            switch (code) {
                case 1598968902:
                    reply.writeString(IOplusScrollCaptureResponseListener.DESCRIPTOR);
                    return true;
                default:
                    switch (code) {
                        case 1:
                            List<OplusScrollCaptureResponse> _arg0 = data.createTypedArrayList(OplusScrollCaptureResponse.CREATOR);
                            data.enforceNoDataAvail();
                            onScrollCaptureResponse(_arg0);
                            return true;
                        default:
                            return super.onTransact(code, data, reply, flags);
                    }
            }
        }

        /* loaded from: classes.dex */
        private static class Proxy implements IOplusScrollCaptureResponseListener {
            private IBinder mRemote;

            Proxy(IBinder remote) {
                this.mRemote = remote;
            }

            @Override // android.os.IInterface
            public IBinder asBinder() {
                return this.mRemote;
            }

            public String getInterfaceDescriptor() {
                return IOplusScrollCaptureResponseListener.DESCRIPTOR;
            }

            @Override // com.oplus.screenshot.IOplusScrollCaptureResponseListener
            public void onScrollCaptureResponse(List<OplusScrollCaptureResponse> responses) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(IOplusScrollCaptureResponseListener.DESCRIPTOR);
                    _data.writeTypedList(responses, 0);
                    this.mRemote.transact(1, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }
        }

        public int getMaxTransactionId() {
            return 0;
        }
    }
}
