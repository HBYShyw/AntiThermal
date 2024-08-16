package com.oplus.kinect;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

/* loaded from: classes.dex */
public interface IOplusKinectObserver extends IInterface {
    public static final String DESCRIPTOR = "com.oplus.kinect.IOplusKinectObserver";

    void onKinectDetected(int i) throws RemoteException;

    /* loaded from: classes.dex */
    public static class Default implements IOplusKinectObserver {
        @Override // com.oplus.kinect.IOplusKinectObserver
        public void onKinectDetected(int type) throws RemoteException {
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return null;
        }
    }

    /* loaded from: classes.dex */
    public static abstract class Stub extends Binder implements IOplusKinectObserver {
        static final int TRANSACTION_onKinectDetected = 1;

        public Stub() {
            attachInterface(this, IOplusKinectObserver.DESCRIPTOR);
        }

        public static IOplusKinectObserver asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(IOplusKinectObserver.DESCRIPTOR);
            if (iin != null && (iin instanceof IOplusKinectObserver)) {
                return (IOplusKinectObserver) iin;
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
                    return "onKinectDetected";
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
                data.enforceInterface(IOplusKinectObserver.DESCRIPTOR);
            }
            switch (code) {
                case 1598968902:
                    reply.writeString(IOplusKinectObserver.DESCRIPTOR);
                    return true;
                default:
                    switch (code) {
                        case 1:
                            int _arg0 = data.readInt();
                            data.enforceNoDataAvail();
                            onKinectDetected(_arg0);
                            return true;
                        default:
                            return super.onTransact(code, data, reply, flags);
                    }
            }
        }

        /* loaded from: classes.dex */
        private static class Proxy implements IOplusKinectObserver {
            private IBinder mRemote;

            Proxy(IBinder remote) {
                this.mRemote = remote;
            }

            @Override // android.os.IInterface
            public IBinder asBinder() {
                return this.mRemote;
            }

            public String getInterfaceDescriptor() {
                return IOplusKinectObserver.DESCRIPTOR;
            }

            @Override // com.oplus.kinect.IOplusKinectObserver
            public void onKinectDetected(int type) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(IOplusKinectObserver.DESCRIPTOR);
                    _data.writeInt(type);
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
