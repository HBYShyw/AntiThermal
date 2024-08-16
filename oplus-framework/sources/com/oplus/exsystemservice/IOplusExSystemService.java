package com.oplus.exsystemservice;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

/* loaded from: classes.dex */
public interface IOplusExSystemService extends IInterface {
    public static final String DESCRIPTOR = "com.oplus.exsystemservice.IOplusExSystemService";

    void onBootPhase(int i) throws RemoteException;

    /* loaded from: classes.dex */
    public static class Default implements IOplusExSystemService {
        @Override // com.oplus.exsystemservice.IOplusExSystemService
        public void onBootPhase(int phase) throws RemoteException {
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return null;
        }
    }

    /* loaded from: classes.dex */
    public static abstract class Stub extends Binder implements IOplusExSystemService {
        static final int TRANSACTION_onBootPhase = 1;

        public Stub() {
            attachInterface(this, IOplusExSystemService.DESCRIPTOR);
        }

        public static IOplusExSystemService asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(IOplusExSystemService.DESCRIPTOR);
            if (iin != null && (iin instanceof IOplusExSystemService)) {
                return (IOplusExSystemService) iin;
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
                    return "onBootPhase";
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
                data.enforceInterface(IOplusExSystemService.DESCRIPTOR);
            }
            switch (code) {
                case 1598968902:
                    reply.writeString(IOplusExSystemService.DESCRIPTOR);
                    return true;
                default:
                    switch (code) {
                        case 1:
                            int _arg0 = data.readInt();
                            data.enforceNoDataAvail();
                            onBootPhase(_arg0);
                            reply.writeNoException();
                            return true;
                        default:
                            return super.onTransact(code, data, reply, flags);
                    }
            }
        }

        /* loaded from: classes.dex */
        private static class Proxy implements IOplusExSystemService {
            private IBinder mRemote;

            Proxy(IBinder remote) {
                this.mRemote = remote;
            }

            @Override // android.os.IInterface
            public IBinder asBinder() {
                return this.mRemote;
            }

            public String getInterfaceDescriptor() {
                return IOplusExSystemService.DESCRIPTOR;
            }

            @Override // com.oplus.exsystemservice.IOplusExSystemService
            public void onBootPhase(int phase) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusExSystemService.DESCRIPTOR);
                    _data.writeInt(phase);
                    this.mRemote.transact(1, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
        }

        public int getMaxTransactionId() {
            return 0;
        }
    }
}
