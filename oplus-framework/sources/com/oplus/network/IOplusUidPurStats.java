package com.oplus.network;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import com.oplus.network.IOplusUidNwChange;
import com.oplus.network.stats.AppFreezeStatsInfo;
import com.oplus.network.stats.AppFreezeSyncInfo;

/* loaded from: classes.dex */
public interface IOplusUidPurStats extends IInterface {
    public static final String DESCRIPTOR = "com.oplus.network.IOplusUidPurStats";

    AppFreezeStatsInfo[] fetchAppFreezeStatsInfoList() throws RemoteException;

    AppFreezeSyncInfo[] fetchAppFreezeSynInfoList() throws RemoteException;

    int[] getNoDataUids() throws RemoteException;

    boolean getStatsCheckStatus() throws RemoteException;

    boolean isUidNoData(int i) throws RemoteException;

    void registerUidNwStatusChange(IOplusUidNwChange iOplusUidNwChange) throws RemoteException;

    void unregisterUidNwStatusChange(IOplusUidNwChange iOplusUidNwChange) throws RemoteException;

    /* loaded from: classes.dex */
    public static class Default implements IOplusUidPurStats {
        @Override // com.oplus.network.IOplusUidPurStats
        public boolean getStatsCheckStatus() throws RemoteException {
            return false;
        }

        @Override // com.oplus.network.IOplusUidPurStats
        public int[] getNoDataUids() throws RemoteException {
            return null;
        }

        @Override // com.oplus.network.IOplusUidPurStats
        public boolean isUidNoData(int uid) throws RemoteException {
            return false;
        }

        @Override // com.oplus.network.IOplusUidPurStats
        public void registerUidNwStatusChange(IOplusUidNwChange cb) throws RemoteException {
        }

        @Override // com.oplus.network.IOplusUidPurStats
        public void unregisterUidNwStatusChange(IOplusUidNwChange cb) throws RemoteException {
        }

        @Override // com.oplus.network.IOplusUidPurStats
        public AppFreezeSyncInfo[] fetchAppFreezeSynInfoList() throws RemoteException {
            return null;
        }

        @Override // com.oplus.network.IOplusUidPurStats
        public AppFreezeStatsInfo[] fetchAppFreezeStatsInfoList() throws RemoteException {
            return null;
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return null;
        }
    }

    /* loaded from: classes.dex */
    public static abstract class Stub extends Binder implements IOplusUidPurStats {
        static final int TRANSACTION_fetchAppFreezeStatsInfoList = 7;
        static final int TRANSACTION_fetchAppFreezeSynInfoList = 6;
        static final int TRANSACTION_getNoDataUids = 2;
        static final int TRANSACTION_getStatsCheckStatus = 1;
        static final int TRANSACTION_isUidNoData = 3;
        static final int TRANSACTION_registerUidNwStatusChange = 4;
        static final int TRANSACTION_unregisterUidNwStatusChange = 5;

        public Stub() {
            attachInterface(this, IOplusUidPurStats.DESCRIPTOR);
        }

        public static IOplusUidPurStats asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(IOplusUidPurStats.DESCRIPTOR);
            if (iin != null && (iin instanceof IOplusUidPurStats)) {
                return (IOplusUidPurStats) iin;
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
                    return "getStatsCheckStatus";
                case 2:
                    return "getNoDataUids";
                case 3:
                    return "isUidNoData";
                case 4:
                    return "registerUidNwStatusChange";
                case 5:
                    return "unregisterUidNwStatusChange";
                case 6:
                    return "fetchAppFreezeSynInfoList";
                case 7:
                    return "fetchAppFreezeStatsInfoList";
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
                data.enforceInterface(IOplusUidPurStats.DESCRIPTOR);
            }
            switch (code) {
                case 1598968902:
                    reply.writeString(IOplusUidPurStats.DESCRIPTOR);
                    return true;
                default:
                    switch (code) {
                        case 1:
                            boolean _result = getStatsCheckStatus();
                            reply.writeNoException();
                            reply.writeBoolean(_result);
                            return true;
                        case 2:
                            int[] _result2 = getNoDataUids();
                            reply.writeNoException();
                            reply.writeIntArray(_result2);
                            return true;
                        case 3:
                            int _arg0 = data.readInt();
                            data.enforceNoDataAvail();
                            boolean _result3 = isUidNoData(_arg0);
                            reply.writeNoException();
                            reply.writeBoolean(_result3);
                            return true;
                        case 4:
                            IOplusUidNwChange _arg02 = IOplusUidNwChange.Stub.asInterface(data.readStrongBinder());
                            data.enforceNoDataAvail();
                            registerUidNwStatusChange(_arg02);
                            reply.writeNoException();
                            return true;
                        case 5:
                            IOplusUidNwChange _arg03 = IOplusUidNwChange.Stub.asInterface(data.readStrongBinder());
                            data.enforceNoDataAvail();
                            unregisterUidNwStatusChange(_arg03);
                            reply.writeNoException();
                            return true;
                        case 6:
                            AppFreezeSyncInfo[] _result4 = fetchAppFreezeSynInfoList();
                            reply.writeNoException();
                            reply.writeTypedArray(_result4, 1);
                            return true;
                        case 7:
                            AppFreezeStatsInfo[] _result5 = fetchAppFreezeStatsInfoList();
                            reply.writeNoException();
                            reply.writeTypedArray(_result5, 1);
                            return true;
                        default:
                            return super.onTransact(code, data, reply, flags);
                    }
            }
        }

        /* loaded from: classes.dex */
        private static class Proxy implements IOplusUidPurStats {
            private IBinder mRemote;

            Proxy(IBinder remote) {
                this.mRemote = remote;
            }

            @Override // android.os.IInterface
            public IBinder asBinder() {
                return this.mRemote;
            }

            public String getInterfaceDescriptor() {
                return IOplusUidPurStats.DESCRIPTOR;
            }

            @Override // com.oplus.network.IOplusUidPurStats
            public boolean getStatsCheckStatus() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusUidPurStats.DESCRIPTOR);
                    this.mRemote.transact(1, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.oplus.network.IOplusUidPurStats
            public int[] getNoDataUids() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusUidPurStats.DESCRIPTOR);
                    this.mRemote.transact(2, _data, _reply, 0);
                    _reply.readException();
                    int[] _result = _reply.createIntArray();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.oplus.network.IOplusUidPurStats
            public boolean isUidNoData(int uid) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusUidPurStats.DESCRIPTOR);
                    _data.writeInt(uid);
                    this.mRemote.transact(3, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.oplus.network.IOplusUidPurStats
            public void registerUidNwStatusChange(IOplusUidNwChange cb) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusUidPurStats.DESCRIPTOR);
                    _data.writeStrongInterface(cb);
                    this.mRemote.transact(4, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.oplus.network.IOplusUidPurStats
            public void unregisterUidNwStatusChange(IOplusUidNwChange cb) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusUidPurStats.DESCRIPTOR);
                    _data.writeStrongInterface(cb);
                    this.mRemote.transact(5, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.oplus.network.IOplusUidPurStats
            public AppFreezeSyncInfo[] fetchAppFreezeSynInfoList() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusUidPurStats.DESCRIPTOR);
                    this.mRemote.transact(6, _data, _reply, 0);
                    _reply.readException();
                    AppFreezeSyncInfo[] _result = (AppFreezeSyncInfo[]) _reply.createTypedArray(AppFreezeSyncInfo.CREATOR);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.oplus.network.IOplusUidPurStats
            public AppFreezeStatsInfo[] fetchAppFreezeStatsInfoList() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusUidPurStats.DESCRIPTOR);
                    this.mRemote.transact(7, _data, _reply, 0);
                    _reply.readException();
                    AppFreezeStatsInfo[] _result = (AppFreezeStatsInfo[]) _reply.createTypedArray(AppFreezeStatsInfo.CREATOR);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
        }

        public int getMaxTransactionId() {
            return 6;
        }
    }
}
