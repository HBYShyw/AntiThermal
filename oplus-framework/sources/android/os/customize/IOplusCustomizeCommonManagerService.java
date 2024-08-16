package android.os.customize;

import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

/* loaded from: classes.dex */
public interface IOplusCustomizeCommonManagerService extends IInterface {
    public static final String DESCRIPTOR = "android.os.customize.IOplusCustomizeCommonManagerService";

    Bundle getPolicy(int i, Bundle bundle) throws RemoteException;

    void handleCmd(int i, Bundle bundle) throws RemoteException;

    Bundle handleCmdExt(int i, Bundle bundle) throws RemoteException;

    /* loaded from: classes.dex */
    public static class Default implements IOplusCustomizeCommonManagerService {
        @Override // android.os.customize.IOplusCustomizeCommonManagerService
        public void handleCmd(int cmd, Bundle param) throws RemoteException {
        }

        @Override // android.os.customize.IOplusCustomizeCommonManagerService
        public Bundle handleCmdExt(int cmd, Bundle param) throws RemoteException {
            return null;
        }

        @Override // android.os.customize.IOplusCustomizeCommonManagerService
        public Bundle getPolicy(int cmd, Bundle param) throws RemoteException {
            return null;
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return null;
        }
    }

    /* loaded from: classes.dex */
    public static abstract class Stub extends Binder implements IOplusCustomizeCommonManagerService {
        static final int TRANSACTION_getPolicy = 3;
        static final int TRANSACTION_handleCmd = 1;
        static final int TRANSACTION_handleCmdExt = 2;

        public Stub() {
            attachInterface(this, IOplusCustomizeCommonManagerService.DESCRIPTOR);
        }

        public static IOplusCustomizeCommonManagerService asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(IOplusCustomizeCommonManagerService.DESCRIPTOR);
            if (iin != null && (iin instanceof IOplusCustomizeCommonManagerService)) {
                return (IOplusCustomizeCommonManagerService) iin;
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
                    return "handleCmd";
                case 2:
                    return "handleCmdExt";
                case 3:
                    return "getPolicy";
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
                data.enforceInterface(IOplusCustomizeCommonManagerService.DESCRIPTOR);
            }
            switch (code) {
                case 1598968902:
                    reply.writeString(IOplusCustomizeCommonManagerService.DESCRIPTOR);
                    return true;
                default:
                    switch (code) {
                        case 1:
                            int _arg0 = data.readInt();
                            Bundle _arg1 = (Bundle) data.readTypedObject(Bundle.CREATOR);
                            data.enforceNoDataAvail();
                            handleCmd(_arg0, _arg1);
                            reply.writeNoException();
                            return true;
                        case 2:
                            int _arg02 = data.readInt();
                            Bundle _arg12 = (Bundle) data.readTypedObject(Bundle.CREATOR);
                            data.enforceNoDataAvail();
                            Bundle _result = handleCmdExt(_arg02, _arg12);
                            reply.writeNoException();
                            reply.writeTypedObject(_result, 1);
                            return true;
                        case 3:
                            int _arg03 = data.readInt();
                            Bundle _arg13 = (Bundle) data.readTypedObject(Bundle.CREATOR);
                            data.enforceNoDataAvail();
                            Bundle _result2 = getPolicy(_arg03, _arg13);
                            reply.writeNoException();
                            reply.writeTypedObject(_result2, 1);
                            return true;
                        default:
                            return super.onTransact(code, data, reply, flags);
                    }
            }
        }

        /* loaded from: classes.dex */
        private static class Proxy implements IOplusCustomizeCommonManagerService {
            private IBinder mRemote;

            Proxy(IBinder remote) {
                this.mRemote = remote;
            }

            @Override // android.os.IInterface
            public IBinder asBinder() {
                return this.mRemote;
            }

            public String getInterfaceDescriptor() {
                return IOplusCustomizeCommonManagerService.DESCRIPTOR;
            }

            @Override // android.os.customize.IOplusCustomizeCommonManagerService
            public void handleCmd(int cmd, Bundle param) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizeCommonManagerService.DESCRIPTOR);
                    _data.writeInt(cmd);
                    _data.writeTypedObject(param, 0);
                    this.mRemote.transact(1, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizeCommonManagerService
            public Bundle handleCmdExt(int cmd, Bundle param) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizeCommonManagerService.DESCRIPTOR);
                    _data.writeInt(cmd);
                    _data.writeTypedObject(param, 0);
                    this.mRemote.transact(2, _data, _reply, 0);
                    _reply.readException();
                    Bundle _result = (Bundle) _reply.readTypedObject(Bundle.CREATOR);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizeCommonManagerService
            public Bundle getPolicy(int cmd, Bundle param) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizeCommonManagerService.DESCRIPTOR);
                    _data.writeInt(cmd);
                    _data.writeTypedObject(param, 0);
                    this.mRemote.transact(3, _data, _reply, 0);
                    _reply.readException();
                    Bundle _result = (Bundle) _reply.readTypedObject(Bundle.CREATOR);
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
