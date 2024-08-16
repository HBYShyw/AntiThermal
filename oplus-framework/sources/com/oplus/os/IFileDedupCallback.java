package com.oplus.os;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

/* loaded from: classes.dex */
public interface IFileDedupCallback extends IInterface {
    public static final String DESCRIPTOR = "com.oplus.os.IFileDedupCallback";

    void onDedupCancel() throws RemoteException;

    void onDedupFinish() throws RemoteException;

    void onDedupProgress(int i, long j, long j2) throws RemoteException;

    void onDupScanCancel() throws RemoteException;

    void onDupScanFinish() throws RemoteException;

    void onDupScanProgress(long j, long j2) throws RemoteException;

    /* loaded from: classes.dex */
    public static class Default implements IFileDedupCallback {
        @Override // com.oplus.os.IFileDedupCallback
        public void onDedupProgress(int groupId, long dedupFileCount, long totalFileCount) throws RemoteException {
        }

        @Override // com.oplus.os.IFileDedupCallback
        public void onDedupCancel() throws RemoteException {
        }

        @Override // com.oplus.os.IFileDedupCallback
        public void onDedupFinish() throws RemoteException {
        }

        @Override // com.oplus.os.IFileDedupCallback
        public void onDupScanProgress(long scanFileCount, long totalFileCount) throws RemoteException {
        }

        @Override // com.oplus.os.IFileDedupCallback
        public void onDupScanCancel() throws RemoteException {
        }

        @Override // com.oplus.os.IFileDedupCallback
        public void onDupScanFinish() throws RemoteException {
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return null;
        }
    }

    /* loaded from: classes.dex */
    public static abstract class Stub extends Binder implements IFileDedupCallback {
        static final int TRANSACTION_onDedupCancel = 2;
        static final int TRANSACTION_onDedupFinish = 3;
        static final int TRANSACTION_onDedupProgress = 1;
        static final int TRANSACTION_onDupScanCancel = 5;
        static final int TRANSACTION_onDupScanFinish = 6;
        static final int TRANSACTION_onDupScanProgress = 4;

        public Stub() {
            attachInterface(this, IFileDedupCallback.DESCRIPTOR);
        }

        public static IFileDedupCallback asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(IFileDedupCallback.DESCRIPTOR);
            if (iin != null && (iin instanceof IFileDedupCallback)) {
                return (IFileDedupCallback) iin;
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
                    return "onDedupProgress";
                case 2:
                    return "onDedupCancel";
                case 3:
                    return "onDedupFinish";
                case 4:
                    return "onDupScanProgress";
                case 5:
                    return "onDupScanCancel";
                case 6:
                    return "onDupScanFinish";
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
                data.enforceInterface(IFileDedupCallback.DESCRIPTOR);
            }
            switch (code) {
                case 1598968902:
                    reply.writeString(IFileDedupCallback.DESCRIPTOR);
                    return true;
                default:
                    switch (code) {
                        case 1:
                            int _arg0 = data.readInt();
                            long _arg1 = data.readLong();
                            long _arg2 = data.readLong();
                            data.enforceNoDataAvail();
                            onDedupProgress(_arg0, _arg1, _arg2);
                            reply.writeNoException();
                            return true;
                        case 2:
                            onDedupCancel();
                            reply.writeNoException();
                            return true;
                        case 3:
                            onDedupFinish();
                            reply.writeNoException();
                            return true;
                        case 4:
                            long _arg02 = data.readLong();
                            long _arg12 = data.readLong();
                            data.enforceNoDataAvail();
                            onDupScanProgress(_arg02, _arg12);
                            reply.writeNoException();
                            return true;
                        case 5:
                            onDupScanCancel();
                            reply.writeNoException();
                            return true;
                        case 6:
                            onDupScanFinish();
                            reply.writeNoException();
                            return true;
                        default:
                            return super.onTransact(code, data, reply, flags);
                    }
            }
        }

        /* loaded from: classes.dex */
        private static class Proxy implements IFileDedupCallback {
            private IBinder mRemote;

            Proxy(IBinder remote) {
                this.mRemote = remote;
            }

            @Override // android.os.IInterface
            public IBinder asBinder() {
                return this.mRemote;
            }

            public String getInterfaceDescriptor() {
                return IFileDedupCallback.DESCRIPTOR;
            }

            @Override // com.oplus.os.IFileDedupCallback
            public void onDedupProgress(int groupId, long dedupFileCount, long totalFileCount) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IFileDedupCallback.DESCRIPTOR);
                    _data.writeInt(groupId);
                    _data.writeLong(dedupFileCount);
                    _data.writeLong(totalFileCount);
                    this.mRemote.transact(1, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.oplus.os.IFileDedupCallback
            public void onDedupCancel() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IFileDedupCallback.DESCRIPTOR);
                    this.mRemote.transact(2, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.oplus.os.IFileDedupCallback
            public void onDedupFinish() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IFileDedupCallback.DESCRIPTOR);
                    this.mRemote.transact(3, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.oplus.os.IFileDedupCallback
            public void onDupScanProgress(long scanFileCount, long totalFileCount) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IFileDedupCallback.DESCRIPTOR);
                    _data.writeLong(scanFileCount);
                    _data.writeLong(totalFileCount);
                    this.mRemote.transact(4, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.oplus.os.IFileDedupCallback
            public void onDupScanCancel() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IFileDedupCallback.DESCRIPTOR);
                    this.mRemote.transact(5, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.oplus.os.IFileDedupCallback
            public void onDupScanFinish() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IFileDedupCallback.DESCRIPTOR);
                    this.mRemote.transact(6, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
        }

        public int getMaxTransactionId() {
            return 5;
        }
    }
}
