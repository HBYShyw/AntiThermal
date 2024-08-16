package com.oplus.app;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import java.util.List;

/* loaded from: classes.dex */
public interface IOplusFreezeInfoListener extends IInterface {
    public static final String DESCRIPTOR = "com.oplus.app.IOplusFreezeInfoListener";

    void notifyFreezeInfo(List<OplusFreezeInfo> list) throws RemoteException;

    /* loaded from: classes.dex */
    public static class Default implements IOplusFreezeInfoListener {
        @Override // com.oplus.app.IOplusFreezeInfoListener
        public void notifyFreezeInfo(List<OplusFreezeInfo> freeInfoList) throws RemoteException {
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return null;
        }
    }

    /* loaded from: classes.dex */
    public static abstract class Stub extends Binder implements IOplusFreezeInfoListener {
        static final int TRANSACTION_notifyFreezeInfo = 1;

        public Stub() {
            attachInterface(this, IOplusFreezeInfoListener.DESCRIPTOR);
        }

        public static IOplusFreezeInfoListener asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(IOplusFreezeInfoListener.DESCRIPTOR);
            if (iin != null && (iin instanceof IOplusFreezeInfoListener)) {
                return (IOplusFreezeInfoListener) iin;
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
                    return "notifyFreezeInfo";
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
                data.enforceInterface(IOplusFreezeInfoListener.DESCRIPTOR);
            }
            switch (code) {
                case 1598968902:
                    reply.writeString(IOplusFreezeInfoListener.DESCRIPTOR);
                    return true;
                default:
                    switch (code) {
                        case 1:
                            List<OplusFreezeInfo> _arg0 = data.createTypedArrayList(OplusFreezeInfo.CREATOR);
                            data.enforceNoDataAvail();
                            notifyFreezeInfo(_arg0);
                            return true;
                        default:
                            return super.onTransact(code, data, reply, flags);
                    }
            }
        }

        /* loaded from: classes.dex */
        private static class Proxy implements IOplusFreezeInfoListener {
            private IBinder mRemote;

            Proxy(IBinder remote) {
                this.mRemote = remote;
            }

            @Override // android.os.IInterface
            public IBinder asBinder() {
                return this.mRemote;
            }

            public String getInterfaceDescriptor() {
                return IOplusFreezeInfoListener.DESCRIPTOR;
            }

            @Override // com.oplus.app.IOplusFreezeInfoListener
            public void notifyFreezeInfo(List<OplusFreezeInfo> freeInfoList) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(IOplusFreezeInfoListener.DESCRIPTOR);
                    _data.writeTypedList(freeInfoList, 0);
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
