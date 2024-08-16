package android.app;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

/* loaded from: classes.dex */
public interface IProximityService extends IInterface {
    public static final String DESCRIPTOR = "android.app.IProximityService";

    void disableTrigger() throws RemoteException;

    void enableTrigger(String str) throws RemoteException;

    boolean isTriggerEnabled() throws RemoteException;

    void resetTriggerExpiration() throws RemoteException;

    /* loaded from: classes.dex */
    public static class Default implements IProximityService {
        @Override // android.app.IProximityService
        public void enableTrigger(String packageName) throws RemoteException {
        }

        @Override // android.app.IProximityService
        public void disableTrigger() throws RemoteException {
        }

        @Override // android.app.IProximityService
        public boolean isTriggerEnabled() throws RemoteException {
            return false;
        }

        @Override // android.app.IProximityService
        public void resetTriggerExpiration() throws RemoteException {
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return null;
        }
    }

    /* loaded from: classes.dex */
    public static abstract class Stub extends Binder implements IProximityService {
        static final int TRANSACTION_disableTrigger = 2;
        static final int TRANSACTION_enableTrigger = 1;
        static final int TRANSACTION_isTriggerEnabled = 3;
        static final int TRANSACTION_resetTriggerExpiration = 4;

        public Stub() {
            attachInterface(this, IProximityService.DESCRIPTOR);
        }

        public static IProximityService asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(IProximityService.DESCRIPTOR);
            if (iin != null && (iin instanceof IProximityService)) {
                return (IProximityService) iin;
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
                    return "enableTrigger";
                case 2:
                    return "disableTrigger";
                case 3:
                    return "isTriggerEnabled";
                case 4:
                    return "resetTriggerExpiration";
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
                data.enforceInterface(IProximityService.DESCRIPTOR);
            }
            switch (code) {
                case 1598968902:
                    reply.writeString(IProximityService.DESCRIPTOR);
                    return true;
                default:
                    switch (code) {
                        case 1:
                            String _arg0 = data.readString();
                            data.enforceNoDataAvail();
                            enableTrigger(_arg0);
                            reply.writeNoException();
                            return true;
                        case 2:
                            disableTrigger();
                            reply.writeNoException();
                            return true;
                        case 3:
                            boolean _result = isTriggerEnabled();
                            reply.writeNoException();
                            reply.writeBoolean(_result);
                            return true;
                        case 4:
                            resetTriggerExpiration();
                            reply.writeNoException();
                            return true;
                        default:
                            return super.onTransact(code, data, reply, flags);
                    }
            }
        }

        /* loaded from: classes.dex */
        private static class Proxy implements IProximityService {
            private IBinder mRemote;

            Proxy(IBinder remote) {
                this.mRemote = remote;
            }

            @Override // android.os.IInterface
            public IBinder asBinder() {
                return this.mRemote;
            }

            public String getInterfaceDescriptor() {
                return IProximityService.DESCRIPTOR;
            }

            @Override // android.app.IProximityService
            public void enableTrigger(String packageName) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IProximityService.DESCRIPTOR);
                    _data.writeString(packageName);
                    this.mRemote.transact(1, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IProximityService
            public void disableTrigger() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IProximityService.DESCRIPTOR);
                    this.mRemote.transact(2, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IProximityService
            public boolean isTriggerEnabled() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IProximityService.DESCRIPTOR);
                    this.mRemote.transact(3, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IProximityService
            public void resetTriggerExpiration() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IProximityService.DESCRIPTOR);
                    this.mRemote.transact(4, _data, _reply, 0);
                    _reply.readException();
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
