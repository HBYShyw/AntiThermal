package android.os.olc;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

/* loaded from: classes.dex */
public interface IOlcService extends IInterface {
    public static final String DESCRIPTOR = "android.os.olc.IOlcService";

    int raiseException(ExceptionInfo exceptionInfo) throws RemoteException;

    /* loaded from: classes.dex */
    public static class Default implements IOlcService {
        @Override // android.os.olc.IOlcService
        public int raiseException(ExceptionInfo exp) throws RemoteException {
            return 0;
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return null;
        }
    }

    /* loaded from: classes.dex */
    public static abstract class Stub extends Binder implements IOlcService {
        static final int TRANSACTION_raiseException = 1;

        public Stub() {
            attachInterface(this, IOlcService.DESCRIPTOR);
        }

        public static IOlcService asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(IOlcService.DESCRIPTOR);
            if (iin != null && (iin instanceof IOlcService)) {
                return (IOlcService) iin;
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
                    return "raiseException";
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
                data.enforceInterface(IOlcService.DESCRIPTOR);
            }
            switch (code) {
                case 1598968902:
                    reply.writeString(IOlcService.DESCRIPTOR);
                    return true;
                default:
                    switch (code) {
                        case 1:
                            ExceptionInfo _arg0 = (ExceptionInfo) data.readTypedObject(ExceptionInfo.CREATOR);
                            data.enforceNoDataAvail();
                            int _result = raiseException(_arg0);
                            reply.writeNoException();
                            reply.writeInt(_result);
                            return true;
                        default:
                            return super.onTransact(code, data, reply, flags);
                    }
            }
        }

        /* loaded from: classes.dex */
        private static class Proxy implements IOlcService {
            private IBinder mRemote;

            Proxy(IBinder remote) {
                this.mRemote = remote;
            }

            @Override // android.os.IInterface
            public IBinder asBinder() {
                return this.mRemote;
            }

            public String getInterfaceDescriptor() {
                return IOlcService.DESCRIPTOR;
            }

            @Override // android.os.olc.IOlcService
            public int raiseException(ExceptionInfo exp) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOlcService.DESCRIPTOR);
                    _data.writeTypedObject(exp, 0);
                    this.mRemote.transact(1, _data, _reply, 0);
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
            return 0;
        }
    }
}
