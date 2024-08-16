package com.oplus.obpf;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

/* loaded from: classes.dex */
public interface IOplusBpfService extends IInterface {
    public static final String DESCRIPTOR = "com.oplus.obpf.IOplusBpfService";

    void doDump(char[] cArr) throws RemoteException;

    /* loaded from: classes.dex */
    public static class Default implements IOplusBpfService {
        @Override // com.oplus.obpf.IOplusBpfService
        public void doDump(char[] argv) throws RemoteException {
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return null;
        }
    }

    /* loaded from: classes.dex */
    public static abstract class Stub extends Binder implements IOplusBpfService {
        static final int TRANSACTION_doDump = 1;

        public Stub() {
            attachInterface(this, IOplusBpfService.DESCRIPTOR);
        }

        public static IOplusBpfService asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(IOplusBpfService.DESCRIPTOR);
            if (iin != null && (iin instanceof IOplusBpfService)) {
                return (IOplusBpfService) iin;
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
                    return "doDump";
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
                data.enforceInterface(IOplusBpfService.DESCRIPTOR);
            }
            switch (code) {
                case 1598968902:
                    reply.writeString(IOplusBpfService.DESCRIPTOR);
                    return true;
                default:
                    switch (code) {
                        case 1:
                            char[] _arg0 = data.createCharArray();
                            data.enforceNoDataAvail();
                            doDump(_arg0);
                            return true;
                        default:
                            return super.onTransact(code, data, reply, flags);
                    }
            }
        }

        /* loaded from: classes.dex */
        private static class Proxy implements IOplusBpfService {
            private IBinder mRemote;

            Proxy(IBinder remote) {
                this.mRemote = remote;
            }

            @Override // android.os.IInterface
            public IBinder asBinder() {
                return this.mRemote;
            }

            public String getInterfaceDescriptor() {
                return IOplusBpfService.DESCRIPTOR;
            }

            @Override // com.oplus.obpf.IOplusBpfService
            public void doDump(char[] argv) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(IOplusBpfService.DESCRIPTOR);
                    _data.writeCharArray(argv);
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
