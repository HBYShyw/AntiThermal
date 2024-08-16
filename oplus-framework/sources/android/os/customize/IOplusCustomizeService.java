package android.os.customize;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

/* loaded from: classes.dex */
public interface IOplusCustomizeService extends IInterface {
    public static final String DESCRIPTOR = "android.os.customize.IOplusCustomizeService";

    void checkPermission() throws RemoteException;

    IBinder getDeviceManagerServiceByName(String str) throws RemoteException;

    boolean isOTAUpdated() throws RemoteException;

    boolean isPlatformSigned(int i) throws RemoteException;

    boolean isSimUnlockedState() throws RemoteException;

    /* loaded from: classes.dex */
    public static class Default implements IOplusCustomizeService {
        @Override // android.os.customize.IOplusCustomizeService
        public IBinder getDeviceManagerServiceByName(String serviceName) throws RemoteException {
            return null;
        }

        @Override // android.os.customize.IOplusCustomizeService
        public void checkPermission() throws RemoteException {
        }

        @Override // android.os.customize.IOplusCustomizeService
        public boolean isPlatformSigned(int uid) throws RemoteException {
            return false;
        }

        @Override // android.os.customize.IOplusCustomizeService
        public boolean isOTAUpdated() throws RemoteException {
            return false;
        }

        @Override // android.os.customize.IOplusCustomizeService
        public boolean isSimUnlockedState() throws RemoteException {
            return false;
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return null;
        }
    }

    /* loaded from: classes.dex */
    public static abstract class Stub extends Binder implements IOplusCustomizeService {
        static final int TRANSACTION_checkPermission = 2;
        static final int TRANSACTION_getDeviceManagerServiceByName = 1;
        static final int TRANSACTION_isOTAUpdated = 4;
        static final int TRANSACTION_isPlatformSigned = 3;
        static final int TRANSACTION_isSimUnlockedState = 5;

        public Stub() {
            attachInterface(this, IOplusCustomizeService.DESCRIPTOR);
        }

        public static IOplusCustomizeService asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(IOplusCustomizeService.DESCRIPTOR);
            if (iin != null && (iin instanceof IOplusCustomizeService)) {
                return (IOplusCustomizeService) iin;
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
                    return "getDeviceManagerServiceByName";
                case 2:
                    return "checkPermission";
                case 3:
                    return "isPlatformSigned";
                case 4:
                    return "isOTAUpdated";
                case 5:
                    return "isSimUnlockedState";
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
                data.enforceInterface(IOplusCustomizeService.DESCRIPTOR);
            }
            switch (code) {
                case 1598968902:
                    reply.writeString(IOplusCustomizeService.DESCRIPTOR);
                    return true;
                default:
                    switch (code) {
                        case 1:
                            String _arg0 = data.readString();
                            data.enforceNoDataAvail();
                            IBinder _result = getDeviceManagerServiceByName(_arg0);
                            reply.writeNoException();
                            reply.writeStrongBinder(_result);
                            return true;
                        case 2:
                            checkPermission();
                            reply.writeNoException();
                            return true;
                        case 3:
                            int _arg02 = data.readInt();
                            data.enforceNoDataAvail();
                            boolean _result2 = isPlatformSigned(_arg02);
                            reply.writeNoException();
                            reply.writeBoolean(_result2);
                            return true;
                        case 4:
                            boolean _result3 = isOTAUpdated();
                            reply.writeNoException();
                            reply.writeBoolean(_result3);
                            return true;
                        case 5:
                            boolean _result4 = isSimUnlockedState();
                            reply.writeNoException();
                            reply.writeBoolean(_result4);
                            return true;
                        default:
                            return super.onTransact(code, data, reply, flags);
                    }
            }
        }

        /* loaded from: classes.dex */
        private static class Proxy implements IOplusCustomizeService {
            private IBinder mRemote;

            Proxy(IBinder remote) {
                this.mRemote = remote;
            }

            @Override // android.os.IInterface
            public IBinder asBinder() {
                return this.mRemote;
            }

            public String getInterfaceDescriptor() {
                return IOplusCustomizeService.DESCRIPTOR;
            }

            @Override // android.os.customize.IOplusCustomizeService
            public IBinder getDeviceManagerServiceByName(String serviceName) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizeService.DESCRIPTOR);
                    _data.writeString(serviceName);
                    this.mRemote.transact(1, _data, _reply, 0);
                    _reply.readException();
                    IBinder _result = _reply.readStrongBinder();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizeService
            public void checkPermission() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizeService.DESCRIPTOR);
                    this.mRemote.transact(2, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizeService
            public boolean isPlatformSigned(int uid) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizeService.DESCRIPTOR);
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

            @Override // android.os.customize.IOplusCustomizeService
            public boolean isOTAUpdated() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizeService.DESCRIPTOR);
                    this.mRemote.transact(4, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizeService
            public boolean isSimUnlockedState() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizeService.DESCRIPTOR);
                    this.mRemote.transact(5, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
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
