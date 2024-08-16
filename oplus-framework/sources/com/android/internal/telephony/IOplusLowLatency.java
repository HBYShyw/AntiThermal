package com.android.internal.telephony;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

/* loaded from: classes.dex */
public interface IOplusLowLatency extends IInterface {
    public static final String DESCRIPTOR = "com.android.internal.telephony.IOplusLowLatency";

    int disableCellularDataPrioExt(String str) throws RemoteException;

    int disableCellularDataSocketPrioExt(String str, String str2, String str3, String str4, String str5, String str6) throws RemoteException;

    int enableCellularDataPrioExt(String str) throws RemoteException;

    int enableCellularDataSocketPrioExt(String str, String str2, String str3, String str4, String str5, String str6) throws RemoteException;

    /* loaded from: classes.dex */
    public static class Default implements IOplusLowLatency {
        @Override // com.android.internal.telephony.IOplusLowLatency
        public int enableCellularDataPrioExt(String packageName) throws RemoteException {
            return 0;
        }

        @Override // com.android.internal.telephony.IOplusLowLatency
        public int disableCellularDataPrioExt(String packageName) throws RemoteException {
            return 0;
        }

        @Override // com.android.internal.telephony.IOplusLowLatency
        public int enableCellularDataSocketPrioExt(String packageName, String srcAdd, String srcPort, String desAdd, String desPort, String protocol) throws RemoteException {
            return 0;
        }

        @Override // com.android.internal.telephony.IOplusLowLatency
        public int disableCellularDataSocketPrioExt(String packageName, String srcAdd, String srcPort, String desAdd, String desPort, String protocol) throws RemoteException {
            return 0;
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return null;
        }
    }

    /* loaded from: classes.dex */
    public static abstract class Stub extends Binder implements IOplusLowLatency {
        static final int TRANSACTION_disableCellularDataPrioExt = 2;
        static final int TRANSACTION_disableCellularDataSocketPrioExt = 4;
        static final int TRANSACTION_enableCellularDataPrioExt = 1;
        static final int TRANSACTION_enableCellularDataSocketPrioExt = 3;

        public Stub() {
            attachInterface(this, IOplusLowLatency.DESCRIPTOR);
        }

        public static IOplusLowLatency asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(IOplusLowLatency.DESCRIPTOR);
            if (iin != null && (iin instanceof IOplusLowLatency)) {
                return (IOplusLowLatency) iin;
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
                    return "enableCellularDataPrioExt";
                case 2:
                    return "disableCellularDataPrioExt";
                case 3:
                    return "enableCellularDataSocketPrioExt";
                case 4:
                    return "disableCellularDataSocketPrioExt";
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
                data.enforceInterface(IOplusLowLatency.DESCRIPTOR);
            }
            switch (code) {
                case 1598968902:
                    reply.writeString(IOplusLowLatency.DESCRIPTOR);
                    return true;
                default:
                    switch (code) {
                        case 1:
                            String _arg0 = data.readString();
                            data.enforceNoDataAvail();
                            int _result = enableCellularDataPrioExt(_arg0);
                            reply.writeNoException();
                            reply.writeInt(_result);
                            return true;
                        case 2:
                            String _arg02 = data.readString();
                            data.enforceNoDataAvail();
                            int _result2 = disableCellularDataPrioExt(_arg02);
                            reply.writeNoException();
                            reply.writeInt(_result2);
                            return true;
                        case 3:
                            String _arg03 = data.readString();
                            String _arg1 = data.readString();
                            String _arg2 = data.readString();
                            String _arg3 = data.readString();
                            String _arg4 = data.readString();
                            String _arg5 = data.readString();
                            data.enforceNoDataAvail();
                            int _result3 = enableCellularDataSocketPrioExt(_arg03, _arg1, _arg2, _arg3, _arg4, _arg5);
                            reply.writeNoException();
                            reply.writeInt(_result3);
                            return true;
                        case 4:
                            String _arg04 = data.readString();
                            String _arg12 = data.readString();
                            String _arg22 = data.readString();
                            String _arg32 = data.readString();
                            String _arg42 = data.readString();
                            String _arg52 = data.readString();
                            data.enforceNoDataAvail();
                            int _result4 = disableCellularDataSocketPrioExt(_arg04, _arg12, _arg22, _arg32, _arg42, _arg52);
                            reply.writeNoException();
                            reply.writeInt(_result4);
                            return true;
                        default:
                            return super.onTransact(code, data, reply, flags);
                    }
            }
        }

        /* loaded from: classes.dex */
        private static class Proxy implements IOplusLowLatency {
            private IBinder mRemote;

            Proxy(IBinder remote) {
                this.mRemote = remote;
            }

            @Override // android.os.IInterface
            public IBinder asBinder() {
                return this.mRemote;
            }

            public String getInterfaceDescriptor() {
                return IOplusLowLatency.DESCRIPTOR;
            }

            @Override // com.android.internal.telephony.IOplusLowLatency
            public int enableCellularDataPrioExt(String packageName) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusLowLatency.DESCRIPTOR);
                    _data.writeString(packageName);
                    this.mRemote.transact(1, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.IOplusLowLatency
            public int disableCellularDataPrioExt(String packageName) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusLowLatency.DESCRIPTOR);
                    _data.writeString(packageName);
                    this.mRemote.transact(2, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.IOplusLowLatency
            public int enableCellularDataSocketPrioExt(String packageName, String srcAdd, String srcPort, String desAdd, String desPort, String protocol) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusLowLatency.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeString(srcAdd);
                    _data.writeString(srcPort);
                    _data.writeString(desAdd);
                    _data.writeString(desPort);
                    _data.writeString(protocol);
                    this.mRemote.transact(3, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.IOplusLowLatency
            public int disableCellularDataSocketPrioExt(String packageName, String srcAdd, String srcPort, String desAdd, String desPort, String protocol) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusLowLatency.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeString(srcAdd);
                    _data.writeString(srcPort);
                    _data.writeString(desAdd);
                    _data.writeString(desPort);
                    _data.writeString(protocol);
                    this.mRemote.transact(4, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
        }

        public int getMaxTransactionId() {
            return 3;
        }
    }
}
