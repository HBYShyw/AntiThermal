package com.oplus.obpf;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

/* loaded from: classes.dex */
public interface IOBpfSystemService extends IInterface {
    public static final String DESCRIPTOR = "com.oplus.obpf.IOBpfSystemService";

    String getBpfClientName(int i, int i2) throws RemoteException;

    /* loaded from: classes.dex */
    public static class Default implements IOBpfSystemService {
        @Override // com.oplus.obpf.IOBpfSystemService
        public String getBpfClientName(int uid, int pid) throws RemoteException {
            return null;
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return null;
        }
    }

    /* loaded from: classes.dex */
    public static abstract class Stub extends Binder implements IOBpfSystemService {
        static final int TRANSACTION_getBpfClientName = 1;

        public Stub() {
            attachInterface(this, IOBpfSystemService.DESCRIPTOR);
        }

        public static IOBpfSystemService asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(IOBpfSystemService.DESCRIPTOR);
            if (iin != null && (iin instanceof IOBpfSystemService)) {
                return (IOBpfSystemService) iin;
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
                    return "getBpfClientName";
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
                data.enforceInterface(IOBpfSystemService.DESCRIPTOR);
            }
            switch (code) {
                case 1598968902:
                    reply.writeString(IOBpfSystemService.DESCRIPTOR);
                    return true;
                default:
                    switch (code) {
                        case 1:
                            int _arg0 = data.readInt();
                            int _arg1 = data.readInt();
                            data.enforceNoDataAvail();
                            String _result = getBpfClientName(_arg0, _arg1);
                            reply.writeNoException();
                            reply.writeString(_result);
                            return true;
                        default:
                            return super.onTransact(code, data, reply, flags);
                    }
            }
        }

        /* loaded from: classes.dex */
        private static class Proxy implements IOBpfSystemService {
            private IBinder mRemote;

            Proxy(IBinder remote) {
                this.mRemote = remote;
            }

            @Override // android.os.IInterface
            public IBinder asBinder() {
                return this.mRemote;
            }

            public String getInterfaceDescriptor() {
                return IOBpfSystemService.DESCRIPTOR;
            }

            @Override // com.oplus.obpf.IOBpfSystemService
            public String getBpfClientName(int uid, int pid) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOBpfSystemService.DESCRIPTOR);
                    _data.writeInt(uid);
                    _data.writeInt(pid);
                    this.mRemote.transact(1, _data, _reply, 0);
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
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
