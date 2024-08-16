package android.system.suspend;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import android.system.suspend.ISuspendCallback;
import android.system.suspend.IWakelockCallback;
import com.oplus.network.OlkConstants;

/* loaded from: classes.dex */
public interface ISuspendControlService extends IInterface {
    public static final String DESCRIPTOR = "android.system.suspend.ISuspendControlService";

    boolean registerCallback(ISuspendCallback iSuspendCallback) throws RemoteException;

    boolean registerWakelockCallback(IWakelockCallback iWakelockCallback, String str) throws RemoteException;

    /* loaded from: classes.dex */
    public static class Default implements ISuspendControlService {
        @Override // android.system.suspend.ISuspendControlService
        public boolean registerCallback(ISuspendCallback callback) throws RemoteException {
            return false;
        }

        @Override // android.system.suspend.ISuspendControlService
        public boolean registerWakelockCallback(IWakelockCallback callback, String name) throws RemoteException {
            return false;
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return null;
        }
    }

    /* loaded from: classes.dex */
    public static abstract class Stub extends Binder implements ISuspendControlService {
        static final int TRANSACTION_registerCallback = 1;
        static final int TRANSACTION_registerWakelockCallback = 2;

        public Stub() {
            attachInterface(this, ISuspendControlService.DESCRIPTOR);
        }

        public static ISuspendControlService asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(ISuspendControlService.DESCRIPTOR);
            if (iin != null && (iin instanceof ISuspendControlService)) {
                return (ISuspendControlService) iin;
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
                    return OlkConstants.FUN_REGISTER_CALLBACK;
                case 2:
                    return "registerWakelockCallback";
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
                data.enforceInterface(ISuspendControlService.DESCRIPTOR);
            }
            switch (code) {
                case 1598968902:
                    reply.writeString(ISuspendControlService.DESCRIPTOR);
                    return true;
                default:
                    switch (code) {
                        case 1:
                            ISuspendCallback _arg0 = ISuspendCallback.Stub.asInterface(data.readStrongBinder());
                            data.enforceNoDataAvail();
                            boolean _result = registerCallback(_arg0);
                            reply.writeNoException();
                            reply.writeBoolean(_result);
                            return true;
                        case 2:
                            IWakelockCallback _arg02 = IWakelockCallback.Stub.asInterface(data.readStrongBinder());
                            String _arg1 = data.readString();
                            data.enforceNoDataAvail();
                            boolean _result2 = registerWakelockCallback(_arg02, _arg1);
                            reply.writeNoException();
                            reply.writeBoolean(_result2);
                            return true;
                        default:
                            return super.onTransact(code, data, reply, flags);
                    }
            }
        }

        /* loaded from: classes.dex */
        private static class Proxy implements ISuspendControlService {
            private IBinder mRemote;

            Proxy(IBinder remote) {
                this.mRemote = remote;
            }

            @Override // android.os.IInterface
            public IBinder asBinder() {
                return this.mRemote;
            }

            public String getInterfaceDescriptor() {
                return ISuspendControlService.DESCRIPTOR;
            }

            @Override // android.system.suspend.ISuspendControlService
            public boolean registerCallback(ISuspendCallback callback) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(ISuspendControlService.DESCRIPTOR);
                    _data.writeStrongInterface(callback);
                    this.mRemote.transact(1, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.system.suspend.ISuspendControlService
            public boolean registerWakelockCallback(IWakelockCallback callback, String name) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(ISuspendControlService.DESCRIPTOR);
                    _data.writeStrongInterface(callback);
                    _data.writeString(name);
                    this.mRemote.transact(2, _data, _reply, 0);
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
            return 1;
        }
    }
}
