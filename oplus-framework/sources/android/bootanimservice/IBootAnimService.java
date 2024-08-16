package android.bootanimservice;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

/* loaded from: classes.dex */
public interface IBootAnimService extends IInterface {
    public static final String DESCRIPTOR = "android.bootanimservice.IBootAnimService";

    int getFlag() throws RemoteException;

    boolean isBootanimComplete() throws RemoteException;

    void setFlag(int i) throws RemoteException;

    /* loaded from: classes.dex */
    public static class Default implements IBootAnimService {
        @Override // android.bootanimservice.IBootAnimService
        public void setFlag(int val) throws RemoteException {
        }

        @Override // android.bootanimservice.IBootAnimService
        public int getFlag() throws RemoteException {
            return 0;
        }

        @Override // android.bootanimservice.IBootAnimService
        public boolean isBootanimComplete() throws RemoteException {
            return false;
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return null;
        }
    }

    /* loaded from: classes.dex */
    public static abstract class Stub extends Binder implements IBootAnimService {
        static final int TRANSACTION_getFlag = 2;
        static final int TRANSACTION_isBootanimComplete = 3;
        static final int TRANSACTION_setFlag = 1;

        public Stub() {
            attachInterface(this, IBootAnimService.DESCRIPTOR);
        }

        public static IBootAnimService asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(IBootAnimService.DESCRIPTOR);
            if (iin != null && (iin instanceof IBootAnimService)) {
                return (IBootAnimService) iin;
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
                    return "setFlag";
                case 2:
                    return "getFlag";
                case 3:
                    return "isBootanimComplete";
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
                data.enforceInterface(IBootAnimService.DESCRIPTOR);
            }
            switch (code) {
                case 1598968902:
                    reply.writeString(IBootAnimService.DESCRIPTOR);
                    return true;
                default:
                    switch (code) {
                        case 1:
                            int _arg0 = data.readInt();
                            data.enforceNoDataAvail();
                            setFlag(_arg0);
                            reply.writeNoException();
                            return true;
                        case 2:
                            int _result = getFlag();
                            reply.writeNoException();
                            reply.writeInt(_result);
                            return true;
                        case 3:
                            boolean _result2 = isBootanimComplete();
                            reply.writeNoException();
                            reply.writeBoolean(_result2);
                            return true;
                        default:
                            return super.onTransact(code, data, reply, flags);
                    }
            }
        }

        /* loaded from: classes.dex */
        private static class Proxy implements IBootAnimService {
            private IBinder mRemote;

            Proxy(IBinder remote) {
                this.mRemote = remote;
            }

            @Override // android.os.IInterface
            public IBinder asBinder() {
                return this.mRemote;
            }

            public String getInterfaceDescriptor() {
                return IBootAnimService.DESCRIPTOR;
            }

            @Override // android.bootanimservice.IBootAnimService
            public void setFlag(int val) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IBootAnimService.DESCRIPTOR);
                    _data.writeInt(val);
                    this.mRemote.transact(1, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.bootanimservice.IBootAnimService
            public int getFlag() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IBootAnimService.DESCRIPTOR);
                    this.mRemote.transact(2, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.bootanimservice.IBootAnimService
            public boolean isBootanimComplete() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IBootAnimService.DESCRIPTOR);
                    this.mRemote.transact(3, _data, _reply, 0);
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
            return 2;
        }
    }
}
