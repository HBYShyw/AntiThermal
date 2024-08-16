package android.os.storage;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

/* loaded from: classes.dex */
public interface IStorageHealthInfoService extends IInterface {
    public static final String DESCRIPTOR = "android.os.storage.IStorageHealthInfoService";

    String[] getStorageHealthInfoItem() throws RemoteException;

    byte[] getStorageOriginalInfo() throws RemoteException;

    String[] getstrStorageHealthInfo() throws RemoteException;

    /* loaded from: classes.dex */
    public static class Default implements IStorageHealthInfoService {
        @Override // android.os.storage.IStorageHealthInfoService
        public String[] getstrStorageHealthInfo() throws RemoteException {
            return null;
        }

        @Override // android.os.storage.IStorageHealthInfoService
        public String[] getStorageHealthInfoItem() throws RemoteException {
            return null;
        }

        @Override // android.os.storage.IStorageHealthInfoService
        public byte[] getStorageOriginalInfo() throws RemoteException {
            return null;
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return null;
        }
    }

    /* loaded from: classes.dex */
    public static abstract class Stub extends Binder implements IStorageHealthInfoService {
        static final int TRANSACTION_getStorageHealthInfoItem = 2;
        static final int TRANSACTION_getStorageOriginalInfo = 3;
        static final int TRANSACTION_getstrStorageHealthInfo = 1;

        public Stub() {
            attachInterface(this, IStorageHealthInfoService.DESCRIPTOR);
        }

        public static IStorageHealthInfoService asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(IStorageHealthInfoService.DESCRIPTOR);
            if (iin != null && (iin instanceof IStorageHealthInfoService)) {
                return (IStorageHealthInfoService) iin;
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
                    return "getstrStorageHealthInfo";
                case 2:
                    return "getStorageHealthInfoItem";
                case 3:
                    return "getStorageOriginalInfo";
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
                data.enforceInterface(IStorageHealthInfoService.DESCRIPTOR);
            }
            switch (code) {
                case 1598968902:
                    reply.writeString(IStorageHealthInfoService.DESCRIPTOR);
                    return true;
                default:
                    switch (code) {
                        case 1:
                            String[] _result = getstrStorageHealthInfo();
                            reply.writeNoException();
                            reply.writeStringArray(_result);
                            return true;
                        case 2:
                            String[] _result2 = getStorageHealthInfoItem();
                            reply.writeNoException();
                            reply.writeStringArray(_result2);
                            return true;
                        case 3:
                            byte[] _result3 = getStorageOriginalInfo();
                            reply.writeNoException();
                            reply.writeByteArray(_result3);
                            return true;
                        default:
                            return super.onTransact(code, data, reply, flags);
                    }
            }
        }

        /* loaded from: classes.dex */
        private static class Proxy implements IStorageHealthInfoService {
            private IBinder mRemote;

            Proxy(IBinder remote) {
                this.mRemote = remote;
            }

            @Override // android.os.IInterface
            public IBinder asBinder() {
                return this.mRemote;
            }

            public String getInterfaceDescriptor() {
                return IStorageHealthInfoService.DESCRIPTOR;
            }

            @Override // android.os.storage.IStorageHealthInfoService
            public String[] getstrStorageHealthInfo() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IStorageHealthInfoService.DESCRIPTOR);
                    this.mRemote.transact(1, _data, _reply, 0);
                    _reply.readException();
                    String[] _result = _reply.createStringArray();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.storage.IStorageHealthInfoService
            public String[] getStorageHealthInfoItem() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IStorageHealthInfoService.DESCRIPTOR);
                    this.mRemote.transact(2, _data, _reply, 0);
                    _reply.readException();
                    String[] _result = _reply.createStringArray();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.storage.IStorageHealthInfoService
            public byte[] getStorageOriginalInfo() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IStorageHealthInfoService.DESCRIPTOR);
                    this.mRemote.transact(3, _data, _reply, 0);
                    _reply.readException();
                    byte[] _result = _reply.createByteArray();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
        }

        public int getMaxTransactionId() {
            return 2;
        }
    }
}
