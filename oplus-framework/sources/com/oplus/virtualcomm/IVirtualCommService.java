package com.oplus.virtualcomm;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

/* loaded from: classes.dex */
public interface IVirtualCommService extends IInterface {
    public static final String DESCRIPTOR = "com.oplus.virtualcomm.IVirtualCommService";

    int enableVirtualcomm(boolean z) throws RemoteException;

    int enableWifiP2P(boolean z) throws RemoteException;

    VirtualCommServiceState getVirtualCommState() throws RemoteException;

    int getVirtualcommDeviceType() throws RemoteException;

    boolean hasVirtualCommCapability(int i, int i2) throws RemoteException;

    boolean isVirtualCommSupport() throws RemoteException;

    /* loaded from: classes.dex */
    public static class Default implements IVirtualCommService {
        @Override // com.oplus.virtualcomm.IVirtualCommService
        public boolean isVirtualCommSupport() throws RemoteException {
            return false;
        }

        @Override // com.oplus.virtualcomm.IVirtualCommService
        public boolean hasVirtualCommCapability(int queryType, int capability) throws RemoteException {
            return false;
        }

        @Override // com.oplus.virtualcomm.IVirtualCommService
        public VirtualCommServiceState getVirtualCommState() throws RemoteException {
            return null;
        }

        @Override // com.oplus.virtualcomm.IVirtualCommService
        public int getVirtualcommDeviceType() throws RemoteException {
            return 0;
        }

        @Override // com.oplus.virtualcomm.IVirtualCommService
        public int enableVirtualcomm(boolean enabled) throws RemoteException {
            return 0;
        }

        @Override // com.oplus.virtualcomm.IVirtualCommService
        public int enableWifiP2P(boolean enabled) throws RemoteException {
            return 0;
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return null;
        }
    }

    /* loaded from: classes.dex */
    public static abstract class Stub extends Binder implements IVirtualCommService {
        static final int TRANSACTION_enableVirtualcomm = 6;
        static final int TRANSACTION_enableWifiP2P = 7;
        static final int TRANSACTION_getVirtualCommState = 4;
        static final int TRANSACTION_getVirtualcommDeviceType = 5;
        static final int TRANSACTION_hasVirtualCommCapability = 3;
        static final int TRANSACTION_isVirtualCommSupport = 2;

        public Stub() {
            attachInterface(this, IVirtualCommService.DESCRIPTOR);
        }

        public static IVirtualCommService asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(IVirtualCommService.DESCRIPTOR);
            if (iin != null && (iin instanceof IVirtualCommService)) {
                return (IVirtualCommService) iin;
            }
            return new Proxy(obj);
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return this;
        }

        public static String getDefaultTransactionName(int transactionCode) {
            switch (transactionCode) {
                case 2:
                    return "isVirtualCommSupport";
                case 3:
                    return "hasVirtualCommCapability";
                case 4:
                    return "getVirtualCommState";
                case 5:
                    return "getVirtualcommDeviceType";
                case 6:
                    return "enableVirtualcomm";
                case 7:
                    return "enableWifiP2P";
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
                data.enforceInterface(IVirtualCommService.DESCRIPTOR);
            }
            switch (code) {
                case 1598968902:
                    reply.writeString(IVirtualCommService.DESCRIPTOR);
                    return true;
                default:
                    switch (code) {
                        case 2:
                            boolean _result = isVirtualCommSupport();
                            reply.writeNoException();
                            reply.writeBoolean(_result);
                            return true;
                        case 3:
                            int _arg0 = data.readInt();
                            int _arg1 = data.readInt();
                            data.enforceNoDataAvail();
                            boolean _result2 = hasVirtualCommCapability(_arg0, _arg1);
                            reply.writeNoException();
                            reply.writeBoolean(_result2);
                            return true;
                        case 4:
                            VirtualCommServiceState _result3 = getVirtualCommState();
                            reply.writeNoException();
                            reply.writeTypedObject(_result3, 1);
                            return true;
                        case 5:
                            int _result4 = getVirtualcommDeviceType();
                            reply.writeNoException();
                            reply.writeInt(_result4);
                            return true;
                        case 6:
                            boolean _arg02 = data.readBoolean();
                            data.enforceNoDataAvail();
                            int _result5 = enableVirtualcomm(_arg02);
                            reply.writeNoException();
                            reply.writeInt(_result5);
                            return true;
                        case 7:
                            boolean _arg03 = data.readBoolean();
                            data.enforceNoDataAvail();
                            int _result6 = enableWifiP2P(_arg03);
                            reply.writeNoException();
                            reply.writeInt(_result6);
                            return true;
                        default:
                            return super.onTransact(code, data, reply, flags);
                    }
            }
        }

        /* loaded from: classes.dex */
        private static class Proxy implements IVirtualCommService {
            private IBinder mRemote;

            Proxy(IBinder remote) {
                this.mRemote = remote;
            }

            @Override // android.os.IInterface
            public IBinder asBinder() {
                return this.mRemote;
            }

            public String getInterfaceDescriptor() {
                return IVirtualCommService.DESCRIPTOR;
            }

            @Override // com.oplus.virtualcomm.IVirtualCommService
            public boolean isVirtualCommSupport() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IVirtualCommService.DESCRIPTOR);
                    this.mRemote.transact(2, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.oplus.virtualcomm.IVirtualCommService
            public boolean hasVirtualCommCapability(int queryType, int capability) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IVirtualCommService.DESCRIPTOR);
                    _data.writeInt(queryType);
                    _data.writeInt(capability);
                    this.mRemote.transact(3, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.oplus.virtualcomm.IVirtualCommService
            public VirtualCommServiceState getVirtualCommState() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IVirtualCommService.DESCRIPTOR);
                    this.mRemote.transact(4, _data, _reply, 0);
                    _reply.readException();
                    VirtualCommServiceState _result = (VirtualCommServiceState) _reply.readTypedObject(VirtualCommServiceState.CREATOR);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.oplus.virtualcomm.IVirtualCommService
            public int getVirtualcommDeviceType() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IVirtualCommService.DESCRIPTOR);
                    this.mRemote.transact(5, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.oplus.virtualcomm.IVirtualCommService
            public int enableVirtualcomm(boolean enabled) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IVirtualCommService.DESCRIPTOR);
                    _data.writeBoolean(enabled);
                    this.mRemote.transact(6, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.oplus.virtualcomm.IVirtualCommService
            public int enableWifiP2P(boolean enabled) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IVirtualCommService.DESCRIPTOR);
                    _data.writeBoolean(enabled);
                    this.mRemote.transact(7, _data, _reply, 0);
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
            return 6;
        }
    }
}
