package com.oplus.heimdall;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import com.oplus.heimdall.ICrashService;
import com.oplus.heimdall.IRootService;
import com.oplus.heimdall.ITraceService;

/* loaded from: classes.dex */
public interface IHeimdallService extends IInterface {
    public static final String DESCRIPTOR = "com.oplus.heimdall.IHeimdallService";

    ICrashService getCrashService() throws RemoteException;

    IRootService getRootService() throws RemoteException;

    ITraceService getTraceService() throws RemoteException;

    /* loaded from: classes.dex */
    public static class Default implements IHeimdallService {
        @Override // com.oplus.heimdall.IHeimdallService
        public ICrashService getCrashService() throws RemoteException {
            return null;
        }

        @Override // com.oplus.heimdall.IHeimdallService
        public IRootService getRootService() throws RemoteException {
            return null;
        }

        @Override // com.oplus.heimdall.IHeimdallService
        public ITraceService getTraceService() throws RemoteException {
            return null;
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return null;
        }
    }

    /* loaded from: classes.dex */
    public static abstract class Stub extends Binder implements IHeimdallService {
        static final int TRANSACTION_getCrashService = 1;
        static final int TRANSACTION_getRootService = 2;
        static final int TRANSACTION_getTraceService = 3;

        public Stub() {
            attachInterface(this, IHeimdallService.DESCRIPTOR);
        }

        public static IHeimdallService asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(IHeimdallService.DESCRIPTOR);
            if (iin != null && (iin instanceof IHeimdallService)) {
                return (IHeimdallService) iin;
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
                    return "getCrashService";
                case 2:
                    return "getRootService";
                case 3:
                    return "getTraceService";
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
                data.enforceInterface(IHeimdallService.DESCRIPTOR);
            }
            switch (code) {
                case 1598968902:
                    reply.writeString(IHeimdallService.DESCRIPTOR);
                    return true;
                default:
                    switch (code) {
                        case 1:
                            ICrashService _result = getCrashService();
                            reply.writeNoException();
                            reply.writeStrongInterface(_result);
                            return true;
                        case 2:
                            IRootService _result2 = getRootService();
                            reply.writeNoException();
                            reply.writeStrongInterface(_result2);
                            return true;
                        case 3:
                            ITraceService _result3 = getTraceService();
                            reply.writeNoException();
                            reply.writeStrongInterface(_result3);
                            return true;
                        default:
                            return super.onTransact(code, data, reply, flags);
                    }
            }
        }

        /* loaded from: classes.dex */
        private static class Proxy implements IHeimdallService {
            private IBinder mRemote;

            Proxy(IBinder remote) {
                this.mRemote = remote;
            }

            @Override // android.os.IInterface
            public IBinder asBinder() {
                return this.mRemote;
            }

            public String getInterfaceDescriptor() {
                return IHeimdallService.DESCRIPTOR;
            }

            @Override // com.oplus.heimdall.IHeimdallService
            public ICrashService getCrashService() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IHeimdallService.DESCRIPTOR);
                    this.mRemote.transact(1, _data, _reply, 0);
                    _reply.readException();
                    ICrashService _result = ICrashService.Stub.asInterface(_reply.readStrongBinder());
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.oplus.heimdall.IHeimdallService
            public IRootService getRootService() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IHeimdallService.DESCRIPTOR);
                    this.mRemote.transact(2, _data, _reply, 0);
                    _reply.readException();
                    IRootService _result = IRootService.Stub.asInterface(_reply.readStrongBinder());
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.oplus.heimdall.IHeimdallService
            public ITraceService getTraceService() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IHeimdallService.DESCRIPTOR);
                    this.mRemote.transact(3, _data, _reply, 0);
                    _reply.readException();
                    ITraceService _result = ITraceService.Stub.asInterface(_reply.readStrongBinder());
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
