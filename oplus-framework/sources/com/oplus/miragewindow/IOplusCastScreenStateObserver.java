package com.oplus.miragewindow;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

/* loaded from: classes.dex */
public interface IOplusCastScreenStateObserver extends IInterface {
    public static final String DESCRIPTOR = "com.oplus.miragewindow.IOplusCastScreenStateObserver";

    void onCastScreenStateChanged(OplusCastScreenState oplusCastScreenState) throws RemoteException;

    /* loaded from: classes.dex */
    public static class Default implements IOplusCastScreenStateObserver {
        @Override // com.oplus.miragewindow.IOplusCastScreenStateObserver
        public void onCastScreenStateChanged(OplusCastScreenState state) throws RemoteException {
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return null;
        }
    }

    /* loaded from: classes.dex */
    public static abstract class Stub extends Binder implements IOplusCastScreenStateObserver {
        static final int TRANSACTION_onCastScreenStateChanged = 1;

        public Stub() {
            attachInterface(this, IOplusCastScreenStateObserver.DESCRIPTOR);
        }

        public static IOplusCastScreenStateObserver asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(IOplusCastScreenStateObserver.DESCRIPTOR);
            if (iin != null && (iin instanceof IOplusCastScreenStateObserver)) {
                return (IOplusCastScreenStateObserver) iin;
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
                    return "onCastScreenStateChanged";
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
                data.enforceInterface(IOplusCastScreenStateObserver.DESCRIPTOR);
            }
            switch (code) {
                case 1598968902:
                    reply.writeString(IOplusCastScreenStateObserver.DESCRIPTOR);
                    return true;
                default:
                    switch (code) {
                        case 1:
                            OplusCastScreenState _arg0 = (OplusCastScreenState) data.readTypedObject(OplusCastScreenState.CREATOR);
                            data.enforceNoDataAvail();
                            onCastScreenStateChanged(_arg0);
                            return true;
                        default:
                            return super.onTransact(code, data, reply, flags);
                    }
            }
        }

        /* loaded from: classes.dex */
        private static class Proxy implements IOplusCastScreenStateObserver {
            private IBinder mRemote;

            Proxy(IBinder remote) {
                this.mRemote = remote;
            }

            @Override // android.os.IInterface
            public IBinder asBinder() {
                return this.mRemote;
            }

            public String getInterfaceDescriptor() {
                return IOplusCastScreenStateObserver.DESCRIPTOR;
            }

            @Override // com.oplus.miragewindow.IOplusCastScreenStateObserver
            public void onCastScreenStateChanged(OplusCastScreenState state) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(IOplusCastScreenStateObserver.DESCRIPTOR);
                    _data.writeTypedObject(state, 0);
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
