package com.oplus.os;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import com.oplus.os.IAppCompressCallback;
import com.oplus.os.IFileDedupCallback;
import java.util.List;

/* loaded from: classes.dex */
public interface IAppCompressAndFileDedup extends IInterface {
    public static final String DESCRIPTOR = "com.oplus.os.IAppCompressAndFileDedup";

    void cancelAppCompress() throws RemoteException;

    void cancelAppCompressScan() throws RemoteException;

    void cancelFileDedup() throws RemoteException;

    void cancelFileDupScan() throws RemoteException;

    AppCompressInfoBatch getAppCompressList(int i, int i2) throws RemoteException;

    List<DupFileInfo> getDupFileInfo(List<String> list) throws RemoteException;

    DupFileGroupInfoBatch getDupFileList(int i, int i2) throws RemoteException;

    boolean isSupport(int i) throws RemoteException;

    void setAppCompressCallback(IAppCompressCallback iAppCompressCallback) throws RemoteException;

    void setAppCompressList(List<AppCompressInfo> list) throws RemoteException;

    void setBackground(boolean z) throws RemoteException;

    void setFileDedupCallback(IFileDedupCallback iFileDedupCallback) throws RemoteException;

    void setFileDedupList(List<DupFileGroupInfo> list) throws RemoteException;

    void startAppCompress() throws RemoteException;

    int startAppCompressAndFileDedup() throws RemoteException;

    void startAppCompressScan() throws RemoteException;

    void startFileDedup() throws RemoteException;

    void startFileDupScan() throws RemoteException;

    int stopAppCompressAndFileDedup() throws RemoteException;

    /* loaded from: classes.dex */
    public static class Default implements IAppCompressAndFileDedup {
        @Override // com.oplus.os.IAppCompressAndFileDedup
        public boolean isSupport(int type) throws RemoteException {
            return false;
        }

        @Override // com.oplus.os.IAppCompressAndFileDedup
        public void setBackground(boolean background) throws RemoteException {
        }

        @Override // com.oplus.os.IAppCompressAndFileDedup
        public AppCompressInfoBatch getAppCompressList(int batch, int offset) throws RemoteException {
            return null;
        }

        @Override // com.oplus.os.IAppCompressAndFileDedup
        public void setAppCompressList(List<AppCompressInfo> list) throws RemoteException {
        }

        @Override // com.oplus.os.IAppCompressAndFileDedup
        public void setAppCompressCallback(IAppCompressCallback callback) throws RemoteException {
        }

        @Override // com.oplus.os.IAppCompressAndFileDedup
        public void startAppCompress() throws RemoteException {
        }

        @Override // com.oplus.os.IAppCompressAndFileDedup
        public void cancelAppCompress() throws RemoteException {
        }

        @Override // com.oplus.os.IAppCompressAndFileDedup
        public void startAppCompressScan() throws RemoteException {
        }

        @Override // com.oplus.os.IAppCompressAndFileDedup
        public void cancelAppCompressScan() throws RemoteException {
        }

        @Override // com.oplus.os.IAppCompressAndFileDedup
        public DupFileGroupInfoBatch getDupFileList(int batch, int offset) throws RemoteException {
            return null;
        }

        @Override // com.oplus.os.IAppCompressAndFileDedup
        public void setFileDedupList(List<DupFileGroupInfo> list) throws RemoteException {
        }

        @Override // com.oplus.os.IAppCompressAndFileDedup
        public void setFileDedupCallback(IFileDedupCallback callback) throws RemoteException {
        }

        @Override // com.oplus.os.IAppCompressAndFileDedup
        public void startFileDedup() throws RemoteException {
        }

        @Override // com.oplus.os.IAppCompressAndFileDedup
        public void cancelFileDedup() throws RemoteException {
        }

        @Override // com.oplus.os.IAppCompressAndFileDedup
        public void startFileDupScan() throws RemoteException {
        }

        @Override // com.oplus.os.IAppCompressAndFileDedup
        public void cancelFileDupScan() throws RemoteException {
        }

        @Override // com.oplus.os.IAppCompressAndFileDedup
        public int startAppCompressAndFileDedup() throws RemoteException {
            return 0;
        }

        @Override // com.oplus.os.IAppCompressAndFileDedup
        public int stopAppCompressAndFileDedup() throws RemoteException {
            return 0;
        }

        @Override // com.oplus.os.IAppCompressAndFileDedup
        public List<DupFileInfo> getDupFileInfo(List<String> list) throws RemoteException {
            return null;
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return null;
        }
    }

    /* loaded from: classes.dex */
    public static abstract class Stub extends Binder implements IAppCompressAndFileDedup {
        static final int TRANSACTION_cancelAppCompress = 7;
        static final int TRANSACTION_cancelAppCompressScan = 9;
        static final int TRANSACTION_cancelFileDedup = 14;
        static final int TRANSACTION_cancelFileDupScan = 16;
        static final int TRANSACTION_getAppCompressList = 3;
        static final int TRANSACTION_getDupFileInfo = 19;
        static final int TRANSACTION_getDupFileList = 10;
        static final int TRANSACTION_isSupport = 1;
        static final int TRANSACTION_setAppCompressCallback = 5;
        static final int TRANSACTION_setAppCompressList = 4;
        static final int TRANSACTION_setBackground = 2;
        static final int TRANSACTION_setFileDedupCallback = 12;
        static final int TRANSACTION_setFileDedupList = 11;
        static final int TRANSACTION_startAppCompress = 6;
        static final int TRANSACTION_startAppCompressAndFileDedup = 17;
        static final int TRANSACTION_startAppCompressScan = 8;
        static final int TRANSACTION_startFileDedup = 13;
        static final int TRANSACTION_startFileDupScan = 15;
        static final int TRANSACTION_stopAppCompressAndFileDedup = 18;

        public Stub() {
            attachInterface(this, IAppCompressAndFileDedup.DESCRIPTOR);
        }

        public static IAppCompressAndFileDedup asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(IAppCompressAndFileDedup.DESCRIPTOR);
            if (iin != null && (iin instanceof IAppCompressAndFileDedup)) {
                return (IAppCompressAndFileDedup) iin;
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
                    return "isSupport";
                case 2:
                    return "setBackground";
                case 3:
                    return "getAppCompressList";
                case 4:
                    return "setAppCompressList";
                case 5:
                    return "setAppCompressCallback";
                case 6:
                    return "startAppCompress";
                case 7:
                    return "cancelAppCompress";
                case 8:
                    return "startAppCompressScan";
                case 9:
                    return "cancelAppCompressScan";
                case 10:
                    return "getDupFileList";
                case 11:
                    return "setFileDedupList";
                case 12:
                    return "setFileDedupCallback";
                case 13:
                    return "startFileDedup";
                case 14:
                    return "cancelFileDedup";
                case 15:
                    return "startFileDupScan";
                case 16:
                    return "cancelFileDupScan";
                case 17:
                    return "startAppCompressAndFileDedup";
                case 18:
                    return "stopAppCompressAndFileDedup";
                case 19:
                    return "getDupFileInfo";
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
                data.enforceInterface(IAppCompressAndFileDedup.DESCRIPTOR);
            }
            switch (code) {
                case 1598968902:
                    reply.writeString(IAppCompressAndFileDedup.DESCRIPTOR);
                    return true;
                default:
                    switch (code) {
                        case 1:
                            int _arg0 = data.readInt();
                            data.enforceNoDataAvail();
                            boolean _result = isSupport(_arg0);
                            reply.writeNoException();
                            reply.writeBoolean(_result);
                            return true;
                        case 2:
                            boolean _arg02 = data.readBoolean();
                            data.enforceNoDataAvail();
                            setBackground(_arg02);
                            reply.writeNoException();
                            return true;
                        case 3:
                            int _arg03 = data.readInt();
                            int _arg1 = data.readInt();
                            data.enforceNoDataAvail();
                            AppCompressInfoBatch _result2 = getAppCompressList(_arg03, _arg1);
                            reply.writeNoException();
                            reply.writeTypedObject(_result2, 1);
                            return true;
                        case 4:
                            List<AppCompressInfo> _arg04 = data.createTypedArrayList(AppCompressInfo.CREATOR);
                            data.enforceNoDataAvail();
                            setAppCompressList(_arg04);
                            reply.writeNoException();
                            return true;
                        case 5:
                            IAppCompressCallback _arg05 = IAppCompressCallback.Stub.asInterface(data.readStrongBinder());
                            data.enforceNoDataAvail();
                            setAppCompressCallback(_arg05);
                            reply.writeNoException();
                            return true;
                        case 6:
                            startAppCompress();
                            reply.writeNoException();
                            return true;
                        case 7:
                            cancelAppCompress();
                            reply.writeNoException();
                            return true;
                        case 8:
                            startAppCompressScan();
                            reply.writeNoException();
                            return true;
                        case 9:
                            cancelAppCompressScan();
                            reply.writeNoException();
                            return true;
                        case 10:
                            int _arg06 = data.readInt();
                            int _arg12 = data.readInt();
                            data.enforceNoDataAvail();
                            DupFileGroupInfoBatch _result3 = getDupFileList(_arg06, _arg12);
                            reply.writeNoException();
                            reply.writeTypedObject(_result3, 1);
                            return true;
                        case 11:
                            List<DupFileGroupInfo> _arg07 = data.createTypedArrayList(DupFileGroupInfo.CREATOR);
                            data.enforceNoDataAvail();
                            setFileDedupList(_arg07);
                            reply.writeNoException();
                            return true;
                        case 12:
                            IFileDedupCallback _arg08 = IFileDedupCallback.Stub.asInterface(data.readStrongBinder());
                            data.enforceNoDataAvail();
                            setFileDedupCallback(_arg08);
                            reply.writeNoException();
                            return true;
                        case 13:
                            startFileDedup();
                            reply.writeNoException();
                            return true;
                        case 14:
                            cancelFileDedup();
                            reply.writeNoException();
                            return true;
                        case 15:
                            startFileDupScan();
                            reply.writeNoException();
                            return true;
                        case 16:
                            cancelFileDupScan();
                            reply.writeNoException();
                            return true;
                        case 17:
                            int _result4 = startAppCompressAndFileDedup();
                            reply.writeNoException();
                            reply.writeInt(_result4);
                            return true;
                        case 18:
                            int _result5 = stopAppCompressAndFileDedup();
                            reply.writeNoException();
                            reply.writeInt(_result5);
                            return true;
                        case 19:
                            List<String> _arg09 = data.createStringArrayList();
                            data.enforceNoDataAvail();
                            List<DupFileInfo> _result6 = getDupFileInfo(_arg09);
                            reply.writeNoException();
                            reply.writeTypedList(_result6, 1);
                            return true;
                        default:
                            return super.onTransact(code, data, reply, flags);
                    }
            }
        }

        /* loaded from: classes.dex */
        private static class Proxy implements IAppCompressAndFileDedup {
            private IBinder mRemote;

            Proxy(IBinder remote) {
                this.mRemote = remote;
            }

            @Override // android.os.IInterface
            public IBinder asBinder() {
                return this.mRemote;
            }

            public String getInterfaceDescriptor() {
                return IAppCompressAndFileDedup.DESCRIPTOR;
            }

            @Override // com.oplus.os.IAppCompressAndFileDedup
            public boolean isSupport(int type) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IAppCompressAndFileDedup.DESCRIPTOR);
                    _data.writeInt(type);
                    this.mRemote.transact(1, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.oplus.os.IAppCompressAndFileDedup
            public void setBackground(boolean background) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IAppCompressAndFileDedup.DESCRIPTOR);
                    _data.writeBoolean(background);
                    this.mRemote.transact(2, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.oplus.os.IAppCompressAndFileDedup
            public AppCompressInfoBatch getAppCompressList(int batch, int offset) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IAppCompressAndFileDedup.DESCRIPTOR);
                    _data.writeInt(batch);
                    _data.writeInt(offset);
                    this.mRemote.transact(3, _data, _reply, 0);
                    _reply.readException();
                    AppCompressInfoBatch _result = (AppCompressInfoBatch) _reply.readTypedObject(AppCompressInfoBatch.CREATOR);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.oplus.os.IAppCompressAndFileDedup
            public void setAppCompressList(List<AppCompressInfo> list) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IAppCompressAndFileDedup.DESCRIPTOR);
                    _data.writeTypedList(list, 0);
                    this.mRemote.transact(4, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.oplus.os.IAppCompressAndFileDedup
            public void setAppCompressCallback(IAppCompressCallback callback) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IAppCompressAndFileDedup.DESCRIPTOR);
                    _data.writeStrongInterface(callback);
                    this.mRemote.transact(5, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.oplus.os.IAppCompressAndFileDedup
            public void startAppCompress() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IAppCompressAndFileDedup.DESCRIPTOR);
                    this.mRemote.transact(6, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.oplus.os.IAppCompressAndFileDedup
            public void cancelAppCompress() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IAppCompressAndFileDedup.DESCRIPTOR);
                    this.mRemote.transact(7, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.oplus.os.IAppCompressAndFileDedup
            public void startAppCompressScan() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IAppCompressAndFileDedup.DESCRIPTOR);
                    this.mRemote.transact(8, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.oplus.os.IAppCompressAndFileDedup
            public void cancelAppCompressScan() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IAppCompressAndFileDedup.DESCRIPTOR);
                    this.mRemote.transact(9, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.oplus.os.IAppCompressAndFileDedup
            public DupFileGroupInfoBatch getDupFileList(int batch, int offset) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IAppCompressAndFileDedup.DESCRIPTOR);
                    _data.writeInt(batch);
                    _data.writeInt(offset);
                    this.mRemote.transact(10, _data, _reply, 0);
                    _reply.readException();
                    DupFileGroupInfoBatch _result = (DupFileGroupInfoBatch) _reply.readTypedObject(DupFileGroupInfoBatch.CREATOR);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.oplus.os.IAppCompressAndFileDedup
            public void setFileDedupList(List<DupFileGroupInfo> list) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IAppCompressAndFileDedup.DESCRIPTOR);
                    _data.writeTypedList(list, 0);
                    this.mRemote.transact(11, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.oplus.os.IAppCompressAndFileDedup
            public void setFileDedupCallback(IFileDedupCallback callback) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IAppCompressAndFileDedup.DESCRIPTOR);
                    _data.writeStrongInterface(callback);
                    this.mRemote.transact(12, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.oplus.os.IAppCompressAndFileDedup
            public void startFileDedup() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IAppCompressAndFileDedup.DESCRIPTOR);
                    this.mRemote.transact(13, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.oplus.os.IAppCompressAndFileDedup
            public void cancelFileDedup() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IAppCompressAndFileDedup.DESCRIPTOR);
                    this.mRemote.transact(14, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.oplus.os.IAppCompressAndFileDedup
            public void startFileDupScan() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IAppCompressAndFileDedup.DESCRIPTOR);
                    this.mRemote.transact(15, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.oplus.os.IAppCompressAndFileDedup
            public void cancelFileDupScan() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IAppCompressAndFileDedup.DESCRIPTOR);
                    this.mRemote.transact(16, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.oplus.os.IAppCompressAndFileDedup
            public int startAppCompressAndFileDedup() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IAppCompressAndFileDedup.DESCRIPTOR);
                    this.mRemote.transact(17, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.oplus.os.IAppCompressAndFileDedup
            public int stopAppCompressAndFileDedup() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IAppCompressAndFileDedup.DESCRIPTOR);
                    this.mRemote.transact(18, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.oplus.os.IAppCompressAndFileDedup
            public List<DupFileInfo> getDupFileInfo(List<String> list) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IAppCompressAndFileDedup.DESCRIPTOR);
                    _data.writeStringList(list);
                    this.mRemote.transact(19, _data, _reply, 0);
                    _reply.readException();
                    List<DupFileInfo> _result = _reply.createTypedArrayList(DupFileInfo.CREATOR);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
        }

        public int getMaxTransactionId() {
            return 18;
        }
    }
}
