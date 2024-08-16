package com.oplus.os;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

/* loaded from: classes.dex */
public interface IAppCompressCallback extends IInterface {
    public static final String DESCRIPTOR = "com.oplus.os.IAppCompressCallback";

    void onComPressCancel(long j) throws RemoteException;

    void onComPressFinish(long j) throws RemoteException;

    void onCompressProgress(String str, long j, long j2) throws RemoteException;

    void onScanCancel() throws RemoteException;

    void onScanFinish() throws RemoteException;

    void onScanProgress(long j, long j2) throws RemoteException;

    /* loaded from: classes.dex */
    public static class Default implements IAppCompressCallback {
        @Override // com.oplus.os.IAppCompressCallback
        public void onCompressProgress(String packageName, long compressFileCount, long totalFileCount) throws RemoteException {
        }

        @Override // com.oplus.os.IAppCompressCallback
        public void onComPressCancel(long savedSize) throws RemoteException {
        }

        @Override // com.oplus.os.IAppCompressCallback
        public void onComPressFinish(long savedSize) throws RemoteException {
        }

        @Override // com.oplus.os.IAppCompressCallback
        public void onScanProgress(long scanAppCount, long totalAppCount) throws RemoteException {
        }

        @Override // com.oplus.os.IAppCompressCallback
        public void onScanCancel() throws RemoteException {
        }

        @Override // com.oplus.os.IAppCompressCallback
        public void onScanFinish() throws RemoteException {
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return null;
        }
    }

    /* loaded from: classes.dex */
    public static abstract class Stub extends Binder implements IAppCompressCallback {
        static final int TRANSACTION_onComPressCancel = 2;
        static final int TRANSACTION_onComPressFinish = 3;
        static final int TRANSACTION_onCompressProgress = 1;
        static final int TRANSACTION_onScanCancel = 5;
        static final int TRANSACTION_onScanFinish = 6;
        static final int TRANSACTION_onScanProgress = 4;

        public Stub() {
            attachInterface(this, IAppCompressCallback.DESCRIPTOR);
        }

        public static IAppCompressCallback asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(IAppCompressCallback.DESCRIPTOR);
            if (iin != null && (iin instanceof IAppCompressCallback)) {
                return (IAppCompressCallback) iin;
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
                    return "onCompressProgress";
                case 2:
                    return "onComPressCancel";
                case 3:
                    return "onComPressFinish";
                case 4:
                    return "onScanProgress";
                case 5:
                    return "onScanCancel";
                case 6:
                    return "onScanFinish";
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
                data.enforceInterface(IAppCompressCallback.DESCRIPTOR);
            }
            switch (code) {
                case 1598968902:
                    reply.writeString(IAppCompressCallback.DESCRIPTOR);
                    return true;
                default:
                    switch (code) {
                        case 1:
                            String _arg0 = data.readString();
                            long _arg1 = data.readLong();
                            long _arg2 = data.readLong();
                            data.enforceNoDataAvail();
                            onCompressProgress(_arg0, _arg1, _arg2);
                            reply.writeNoException();
                            return true;
                        case 2:
                            long _arg02 = data.readLong();
                            data.enforceNoDataAvail();
                            onComPressCancel(_arg02);
                            reply.writeNoException();
                            return true;
                        case 3:
                            long _arg03 = data.readLong();
                            data.enforceNoDataAvail();
                            onComPressFinish(_arg03);
                            reply.writeNoException();
                            return true;
                        case 4:
                            long _arg04 = data.readLong();
                            long _arg12 = data.readLong();
                            data.enforceNoDataAvail();
                            onScanProgress(_arg04, _arg12);
                            reply.writeNoException();
                            return true;
                        case 5:
                            onScanCancel();
                            reply.writeNoException();
                            return true;
                        case 6:
                            onScanFinish();
                            reply.writeNoException();
                            return true;
                        default:
                            return super.onTransact(code, data, reply, flags);
                    }
            }
        }

        /* loaded from: classes.dex */
        private static class Proxy implements IAppCompressCallback {
            private IBinder mRemote;

            Proxy(IBinder remote) {
                this.mRemote = remote;
            }

            @Override // android.os.IInterface
            public IBinder asBinder() {
                return this.mRemote;
            }

            public String getInterfaceDescriptor() {
                return IAppCompressCallback.DESCRIPTOR;
            }

            @Override // com.oplus.os.IAppCompressCallback
            public void onCompressProgress(String packageName, long compressFileCount, long totalFileCount) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IAppCompressCallback.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeLong(compressFileCount);
                    _data.writeLong(totalFileCount);
                    this.mRemote.transact(1, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.oplus.os.IAppCompressCallback
            public void onComPressCancel(long savedSize) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IAppCompressCallback.DESCRIPTOR);
                    _data.writeLong(savedSize);
                    this.mRemote.transact(2, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.oplus.os.IAppCompressCallback
            public void onComPressFinish(long savedSize) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IAppCompressCallback.DESCRIPTOR);
                    _data.writeLong(savedSize);
                    this.mRemote.transact(3, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.oplus.os.IAppCompressCallback
            public void onScanProgress(long scanAppCount, long totalAppCount) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IAppCompressCallback.DESCRIPTOR);
                    _data.writeLong(scanAppCount);
                    _data.writeLong(totalAppCount);
                    this.mRemote.transact(4, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.oplus.os.IAppCompressCallback
            public void onScanCancel() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IAppCompressCallback.DESCRIPTOR);
                    this.mRemote.transact(5, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.oplus.os.IAppCompressCallback
            public void onScanFinish() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IAppCompressCallback.DESCRIPTOR);
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
