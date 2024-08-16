package android.payjoy;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

/* loaded from: classes.dex */
public interface IPayJoyCustomizedService extends IInterface {
    public static final String DESCRIPTOR = "android.payjoy.IPayJoyCustomizedService";

    void activatePayJoyControl() throws RemoteException;

    boolean isPayJoyCustState() throws RemoteException;

    boolean isSupportPayJoy() throws RemoteException;

    /* loaded from: classes.dex */
    public static class Default implements IPayJoyCustomizedService {
        @Override // android.payjoy.IPayJoyCustomizedService
        public void activatePayJoyControl() throws RemoteException {
        }

        @Override // android.payjoy.IPayJoyCustomizedService
        public boolean isSupportPayJoy() throws RemoteException {
            return false;
        }

        @Override // android.payjoy.IPayJoyCustomizedService
        public boolean isPayJoyCustState() throws RemoteException {
            return false;
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return null;
        }
    }

    /* loaded from: classes.dex */
    public static abstract class Stub extends Binder implements IPayJoyCustomizedService {
        static final int TRANSACTION_activatePayJoyControl = 1;
        static final int TRANSACTION_isPayJoyCustState = 3;
        static final int TRANSACTION_isSupportPayJoy = 2;

        public Stub() {
            attachInterface(this, IPayJoyCustomizedService.DESCRIPTOR);
        }

        public static IPayJoyCustomizedService asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(IPayJoyCustomizedService.DESCRIPTOR);
            if (iin != null && (iin instanceof IPayJoyCustomizedService)) {
                return (IPayJoyCustomizedService) iin;
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
                    return "activatePayJoyControl";
                case 2:
                    return "isSupportPayJoy";
                case 3:
                    return "isPayJoyCustState";
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
                data.enforceInterface(IPayJoyCustomizedService.DESCRIPTOR);
            }
            switch (code) {
                case 1598968902:
                    reply.writeString(IPayJoyCustomizedService.DESCRIPTOR);
                    return true;
                default:
                    switch (code) {
                        case 1:
                            activatePayJoyControl();
                            reply.writeNoException();
                            return true;
                        case 2:
                            boolean _result = isSupportPayJoy();
                            reply.writeNoException();
                            reply.writeBoolean(_result);
                            return true;
                        case 3:
                            boolean _result2 = isPayJoyCustState();
                            reply.writeNoException();
                            reply.writeBoolean(_result2);
                            return true;
                        default:
                            return super.onTransact(code, data, reply, flags);
                    }
            }
        }

        /* loaded from: classes.dex */
        private static class Proxy implements IPayJoyCustomizedService {
            private IBinder mRemote;

            Proxy(IBinder remote) {
                this.mRemote = remote;
            }

            @Override // android.os.IInterface
            public IBinder asBinder() {
                return this.mRemote;
            }

            public String getInterfaceDescriptor() {
                return IPayJoyCustomizedService.DESCRIPTOR;
            }

            @Override // android.payjoy.IPayJoyCustomizedService
            public void activatePayJoyControl() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IPayJoyCustomizedService.DESCRIPTOR);
                    this.mRemote.transact(1, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.payjoy.IPayJoyCustomizedService
            public boolean isSupportPayJoy() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IPayJoyCustomizedService.DESCRIPTOR);
                    this.mRemote.transact(2, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.payjoy.IPayJoyCustomizedService
            public boolean isPayJoyCustState() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IPayJoyCustomizedService.DESCRIPTOR);
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
