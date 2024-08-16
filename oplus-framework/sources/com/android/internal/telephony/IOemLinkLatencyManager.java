package com.android.internal.telephony;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

/* loaded from: classes.dex */
public interface IOemLinkLatencyManager extends IInterface {
    public static final String DESCRIPTOR = "com.android.internal.telephony.IOemLinkLatencyManager";

    boolean gameOptimizeExit() throws RemoteException;

    boolean gameOptimizeSetLoad(int i, String str) throws RemoteException;

    OplusLinkLatencyInfo getCurrentLevel() throws RemoteException;

    long prioritizeDefaultDataSubscription(boolean z) throws RemoteException;

    long qctAddFilter(String[] strArr, long j, long j2, long j3) throws RemoteException;

    long qctDeleteFilter(String[] strArr) throws RemoteException;

    void setLevel(long j, long j2, long j3) throws RemoteException;

    /* loaded from: classes.dex */
    public static class Default implements IOemLinkLatencyManager {
        @Override // com.android.internal.telephony.IOemLinkLatencyManager
        public void setLevel(long rat, long uplink, long downlink) throws RemoteException {
        }

        @Override // com.android.internal.telephony.IOemLinkLatencyManager
        public OplusLinkLatencyInfo getCurrentLevel() throws RemoteException {
            return null;
        }

        @Override // com.android.internal.telephony.IOemLinkLatencyManager
        public long prioritizeDefaultDataSubscription(boolean isEnabled) throws RemoteException {
            return 0L;
        }

        @Override // com.android.internal.telephony.IOemLinkLatencyManager
        public boolean gameOptimizeSetLoad(int id, String pkgName) throws RemoteException {
            return false;
        }

        @Override // com.android.internal.telephony.IOemLinkLatencyManager
        public boolean gameOptimizeExit() throws RemoteException {
            return false;
        }

        @Override // com.android.internal.telephony.IOemLinkLatencyManager
        public long qctAddFilter(String[] socket, long level, long timer, long ood) throws RemoteException {
            return 0L;
        }

        @Override // com.android.internal.telephony.IOemLinkLatencyManager
        public long qctDeleteFilter(String[] socket) throws RemoteException {
            return 0L;
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return null;
        }
    }

    /* loaded from: classes.dex */
    public static abstract class Stub extends Binder implements IOemLinkLatencyManager {
        static final int TRANSACTION_gameOptimizeExit = 5;
        static final int TRANSACTION_gameOptimizeSetLoad = 4;
        static final int TRANSACTION_getCurrentLevel = 2;
        static final int TRANSACTION_prioritizeDefaultDataSubscription = 3;
        static final int TRANSACTION_qctAddFilter = 6;
        static final int TRANSACTION_qctDeleteFilter = 7;
        static final int TRANSACTION_setLevel = 1;

        public Stub() {
            attachInterface(this, IOemLinkLatencyManager.DESCRIPTOR);
        }

        public static IOemLinkLatencyManager asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(IOemLinkLatencyManager.DESCRIPTOR);
            if (iin != null && (iin instanceof IOemLinkLatencyManager)) {
                return (IOemLinkLatencyManager) iin;
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
                    return "setLevel";
                case 2:
                    return "getCurrentLevel";
                case 3:
                    return "prioritizeDefaultDataSubscription";
                case 4:
                    return "gameOptimizeSetLoad";
                case 5:
                    return "gameOptimizeExit";
                case 6:
                    return "qctAddFilter";
                case 7:
                    return "qctDeleteFilter";
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
                data.enforceInterface(IOemLinkLatencyManager.DESCRIPTOR);
            }
            switch (code) {
                case 1598968902:
                    reply.writeString(IOemLinkLatencyManager.DESCRIPTOR);
                    return true;
                default:
                    switch (code) {
                        case 1:
                            long _arg0 = data.readLong();
                            long _arg1 = data.readLong();
                            long _arg2 = data.readLong();
                            data.enforceNoDataAvail();
                            setLevel(_arg0, _arg1, _arg2);
                            reply.writeNoException();
                            return true;
                        case 2:
                            OplusLinkLatencyInfo _result = getCurrentLevel();
                            reply.writeNoException();
                            reply.writeTypedObject(_result, 1);
                            return true;
                        case 3:
                            boolean _arg02 = data.readBoolean();
                            data.enforceNoDataAvail();
                            long _result2 = prioritizeDefaultDataSubscription(_arg02);
                            reply.writeNoException();
                            reply.writeLong(_result2);
                            return true;
                        case 4:
                            int _arg03 = data.readInt();
                            String _arg12 = data.readString();
                            data.enforceNoDataAvail();
                            boolean _result3 = gameOptimizeSetLoad(_arg03, _arg12);
                            reply.writeNoException();
                            reply.writeBoolean(_result3);
                            return true;
                        case 5:
                            boolean _result4 = gameOptimizeExit();
                            reply.writeNoException();
                            reply.writeBoolean(_result4);
                            return true;
                        case 6:
                            String[] _arg04 = data.createStringArray();
                            long _arg13 = data.readLong();
                            long _arg22 = data.readLong();
                            long _arg3 = data.readLong();
                            data.enforceNoDataAvail();
                            long _result5 = qctAddFilter(_arg04, _arg13, _arg22, _arg3);
                            reply.writeNoException();
                            reply.writeLong(_result5);
                            return true;
                        case 7:
                            String[] _arg05 = data.createStringArray();
                            data.enforceNoDataAvail();
                            long _result6 = qctDeleteFilter(_arg05);
                            reply.writeNoException();
                            reply.writeLong(_result6);
                            return true;
                        default:
                            return super.onTransact(code, data, reply, flags);
                    }
            }
        }

        /* loaded from: classes.dex */
        private static class Proxy implements IOemLinkLatencyManager {
            private IBinder mRemote;

            Proxy(IBinder remote) {
                this.mRemote = remote;
            }

            @Override // android.os.IInterface
            public IBinder asBinder() {
                return this.mRemote;
            }

            public String getInterfaceDescriptor() {
                return IOemLinkLatencyManager.DESCRIPTOR;
            }

            @Override // com.android.internal.telephony.IOemLinkLatencyManager
            public void setLevel(long rat, long uplink, long downlink) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOemLinkLatencyManager.DESCRIPTOR);
                    _data.writeLong(rat);
                    _data.writeLong(uplink);
                    _data.writeLong(downlink);
                    this.mRemote.transact(1, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.IOemLinkLatencyManager
            public OplusLinkLatencyInfo getCurrentLevel() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOemLinkLatencyManager.DESCRIPTOR);
                    this.mRemote.transact(2, _data, _reply, 0);
                    _reply.readException();
                    OplusLinkLatencyInfo _result = (OplusLinkLatencyInfo) _reply.readTypedObject(OplusLinkLatencyInfo.CREATOR);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.IOemLinkLatencyManager
            public long prioritizeDefaultDataSubscription(boolean isEnabled) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOemLinkLatencyManager.DESCRIPTOR);
                    _data.writeBoolean(isEnabled);
                    this.mRemote.transact(3, _data, _reply, 0);
                    _reply.readException();
                    long _result = _reply.readLong();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.IOemLinkLatencyManager
            public boolean gameOptimizeSetLoad(int id, String pkgName) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOemLinkLatencyManager.DESCRIPTOR);
                    _data.writeInt(id);
                    _data.writeString(pkgName);
                    this.mRemote.transact(4, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.IOemLinkLatencyManager
            public boolean gameOptimizeExit() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOemLinkLatencyManager.DESCRIPTOR);
                    this.mRemote.transact(5, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.IOemLinkLatencyManager
            public long qctAddFilter(String[] socket, long level, long timer, long ood) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOemLinkLatencyManager.DESCRIPTOR);
                    _data.writeStringArray(socket);
                    _data.writeLong(level);
                    _data.writeLong(timer);
                    _data.writeLong(ood);
                    this.mRemote.transact(6, _data, _reply, 0);
                    _reply.readException();
                    long _result = _reply.readLong();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.IOemLinkLatencyManager
            public long qctDeleteFilter(String[] socket) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOemLinkLatencyManager.DESCRIPTOR);
                    _data.writeStringArray(socket);
                    this.mRemote.transact(7, _data, _reply, 0);
                    _reply.readException();
                    long _result = _reply.readLong();
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
