package com.oplus.filter;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

/* loaded from: classes.dex */
public interface IDynamicFilterService extends IInterface {
    public static final String DESCRIPTOR = "com.oplus.filter.IDynamicFilterService";

    void addToFilter(String str, String str2, String str3) throws RemoteException;

    String getFilterTagValue(String str, String str2) throws RemoteException;

    boolean hasFilter(String str) throws RemoteException;

    boolean inFilter(String str, String str2) throws RemoteException;

    void removeFromFilter(String str, String str2) throws RemoteException;

    /* loaded from: classes.dex */
    public static class Default implements IDynamicFilterService {
        @Override // com.oplus.filter.IDynamicFilterService
        public boolean hasFilter(String name) throws RemoteException {
            return false;
        }

        @Override // com.oplus.filter.IDynamicFilterService
        public boolean inFilter(String name, String tag) throws RemoteException {
            return false;
        }

        @Override // com.oplus.filter.IDynamicFilterService
        public void addToFilter(String name, String tag, String value) throws RemoteException {
        }

        @Override // com.oplus.filter.IDynamicFilterService
        public void removeFromFilter(String name, String tag) throws RemoteException {
        }

        @Override // com.oplus.filter.IDynamicFilterService
        public String getFilterTagValue(String name, String tag) throws RemoteException {
            return null;
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return null;
        }
    }

    /* loaded from: classes.dex */
    public static abstract class Stub extends Binder implements IDynamicFilterService {
        static final int TRANSACTION_addToFilter = 3;
        static final int TRANSACTION_getFilterTagValue = 5;
        static final int TRANSACTION_hasFilter = 1;
        static final int TRANSACTION_inFilter = 2;
        static final int TRANSACTION_removeFromFilter = 4;

        public Stub() {
            attachInterface(this, IDynamicFilterService.DESCRIPTOR);
        }

        public static IDynamicFilterService asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(IDynamicFilterService.DESCRIPTOR);
            if (iin != null && (iin instanceof IDynamicFilterService)) {
                return (IDynamicFilterService) iin;
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
                    return "hasFilter";
                case 2:
                    return "inFilter";
                case 3:
                    return "addToFilter";
                case 4:
                    return "removeFromFilter";
                case 5:
                    return "getFilterTagValue";
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
                data.enforceInterface(IDynamicFilterService.DESCRIPTOR);
            }
            switch (code) {
                case 1598968902:
                    reply.writeString(IDynamicFilterService.DESCRIPTOR);
                    return true;
                default:
                    switch (code) {
                        case 1:
                            String _arg0 = data.readString();
                            data.enforceNoDataAvail();
                            boolean _result = hasFilter(_arg0);
                            reply.writeNoException();
                            reply.writeBoolean(_result);
                            return true;
                        case 2:
                            String _arg02 = data.readString();
                            String _arg1 = data.readString();
                            data.enforceNoDataAvail();
                            boolean _result2 = inFilter(_arg02, _arg1);
                            reply.writeNoException();
                            reply.writeBoolean(_result2);
                            return true;
                        case 3:
                            String _arg03 = data.readString();
                            String _arg12 = data.readString();
                            String _arg2 = data.readString();
                            data.enforceNoDataAvail();
                            addToFilter(_arg03, _arg12, _arg2);
                            return true;
                        case 4:
                            String _arg04 = data.readString();
                            String _arg13 = data.readString();
                            data.enforceNoDataAvail();
                            removeFromFilter(_arg04, _arg13);
                            return true;
                        case 5:
                            String _arg05 = data.readString();
                            String _arg14 = data.readString();
                            data.enforceNoDataAvail();
                            String _result3 = getFilterTagValue(_arg05, _arg14);
                            reply.writeNoException();
                            reply.writeString(_result3);
                            return true;
                        default:
                            return super.onTransact(code, data, reply, flags);
                    }
            }
        }

        /* loaded from: classes.dex */
        private static class Proxy implements IDynamicFilterService {
            private IBinder mRemote;

            Proxy(IBinder remote) {
                this.mRemote = remote;
            }

            @Override // android.os.IInterface
            public IBinder asBinder() {
                return this.mRemote;
            }

            public String getInterfaceDescriptor() {
                return IDynamicFilterService.DESCRIPTOR;
            }

            @Override // com.oplus.filter.IDynamicFilterService
            public boolean hasFilter(String name) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IDynamicFilterService.DESCRIPTOR);
                    _data.writeString(name);
                    this.mRemote.transact(1, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.oplus.filter.IDynamicFilterService
            public boolean inFilter(String name, String tag) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IDynamicFilterService.DESCRIPTOR);
                    _data.writeString(name);
                    _data.writeString(tag);
                    this.mRemote.transact(2, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.oplus.filter.IDynamicFilterService
            public void addToFilter(String name, String tag, String value) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(IDynamicFilterService.DESCRIPTOR);
                    _data.writeString(name);
                    _data.writeString(tag);
                    _data.writeString(value);
                    this.mRemote.transact(3, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // com.oplus.filter.IDynamicFilterService
            public void removeFromFilter(String name, String tag) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(IDynamicFilterService.DESCRIPTOR);
                    _data.writeString(name);
                    _data.writeString(tag);
                    this.mRemote.transact(4, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // com.oplus.filter.IDynamicFilterService
            public String getFilterTagValue(String name, String tag) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IDynamicFilterService.DESCRIPTOR);
                    _data.writeString(name);
                    _data.writeString(tag);
                    this.mRemote.transact(5, _data, _reply, 0);
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
            return 4;
        }
    }
}
