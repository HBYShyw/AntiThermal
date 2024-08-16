package com.oplus.nfcextns;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

/* loaded from: classes.dex */
public interface INfcExtnsService extends IInterface {
    public static final String DESCRIPTOR = "com.oplus.nfcextns.INfcExtnsService";

    int oplusPnscrGetCurrent() throws RemoteException;

    int oplusPnscrGetFreq() throws RemoteException;

    boolean oplusPnscrTestCardEmulation() throws RemoteException;

    int oplusPnscrTestGpFelicaSpc() throws RemoteException;

    /* loaded from: classes.dex */
    public static class Default implements INfcExtnsService {
        @Override // com.oplus.nfcextns.INfcExtnsService
        public int oplusPnscrTestGpFelicaSpc() throws RemoteException {
            return 0;
        }

        @Override // com.oplus.nfcextns.INfcExtnsService
        public boolean oplusPnscrTestCardEmulation() throws RemoteException {
            return false;
        }

        @Override // com.oplus.nfcextns.INfcExtnsService
        public int oplusPnscrGetFreq() throws RemoteException {
            return 0;
        }

        @Override // com.oplus.nfcextns.INfcExtnsService
        public int oplusPnscrGetCurrent() throws RemoteException {
            return 0;
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return null;
        }
    }

    /* loaded from: classes.dex */
    public static abstract class Stub extends Binder implements INfcExtnsService {
        static final int TRANSACTION_oplusPnscrGetCurrent = 4;
        static final int TRANSACTION_oplusPnscrGetFreq = 3;
        static final int TRANSACTION_oplusPnscrTestCardEmulation = 2;
        static final int TRANSACTION_oplusPnscrTestGpFelicaSpc = 1;

        public Stub() {
            attachInterface(this, INfcExtnsService.DESCRIPTOR);
        }

        public static INfcExtnsService asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(INfcExtnsService.DESCRIPTOR);
            if (iin != null && (iin instanceof INfcExtnsService)) {
                return (INfcExtnsService) iin;
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
                    return "oplusPnscrTestGpFelicaSpc";
                case 2:
                    return "oplusPnscrTestCardEmulation";
                case 3:
                    return "oplusPnscrGetFreq";
                case 4:
                    return "oplusPnscrGetCurrent";
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
                data.enforceInterface(INfcExtnsService.DESCRIPTOR);
            }
            switch (code) {
                case 1598968902:
                    reply.writeString(INfcExtnsService.DESCRIPTOR);
                    return true;
                default:
                    switch (code) {
                        case 1:
                            int _result = oplusPnscrTestGpFelicaSpc();
                            reply.writeNoException();
                            reply.writeInt(_result);
                            return true;
                        case 2:
                            boolean _result2 = oplusPnscrTestCardEmulation();
                            reply.writeNoException();
                            reply.writeBoolean(_result2);
                            return true;
                        case 3:
                            int _result3 = oplusPnscrGetFreq();
                            reply.writeNoException();
                            reply.writeInt(_result3);
                            return true;
                        case 4:
                            int _result4 = oplusPnscrGetCurrent();
                            reply.writeNoException();
                            reply.writeInt(_result4);
                            return true;
                        default:
                            return super.onTransact(code, data, reply, flags);
                    }
            }
        }

        /* loaded from: classes.dex */
        private static class Proxy implements INfcExtnsService {
            private IBinder mRemote;

            Proxy(IBinder remote) {
                this.mRemote = remote;
            }

            @Override // android.os.IInterface
            public IBinder asBinder() {
                return this.mRemote;
            }

            public String getInterfaceDescriptor() {
                return INfcExtnsService.DESCRIPTOR;
            }

            @Override // com.oplus.nfcextns.INfcExtnsService
            public int oplusPnscrTestGpFelicaSpc() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(INfcExtnsService.DESCRIPTOR);
                    this.mRemote.transact(1, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.oplus.nfcextns.INfcExtnsService
            public boolean oplusPnscrTestCardEmulation() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(INfcExtnsService.DESCRIPTOR);
                    this.mRemote.transact(2, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.oplus.nfcextns.INfcExtnsService
            public int oplusPnscrGetFreq() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(INfcExtnsService.DESCRIPTOR);
                    this.mRemote.transact(3, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.oplus.nfcextns.INfcExtnsService
            public int oplusPnscrGetCurrent() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(INfcExtnsService.DESCRIPTOR);
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
