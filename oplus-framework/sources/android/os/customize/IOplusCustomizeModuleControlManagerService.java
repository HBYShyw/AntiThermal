package android.os.customize;

import android.content.ComponentName;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

/* loaded from: classes.dex */
public interface IOplusCustomizeModuleControlManagerService extends IInterface {
    public static final String DESCRIPTOR = "android.os.customize.IOplusCustomizeModuleControlManagerService";

    boolean getModuleControlStateByType(int i) throws RemoteException;

    boolean getModuleControlStateByTypeAdmin(int i, ComponentName componentName, Bundle bundle) throws RemoteException;

    boolean getModuleControlStateByTypeBundle(int i, Bundle bundle) throws RemoteException;

    /* loaded from: classes.dex */
    public static class Default implements IOplusCustomizeModuleControlManagerService {
        @Override // android.os.customize.IOplusCustomizeModuleControlManagerService
        public boolean getModuleControlStateByType(int moduleType) throws RemoteException {
            return false;
        }

        @Override // android.os.customize.IOplusCustomizeModuleControlManagerService
        public boolean getModuleControlStateByTypeBundle(int moduleType, Bundle bundle) throws RemoteException {
            return false;
        }

        @Override // android.os.customize.IOplusCustomizeModuleControlManagerService
        public boolean getModuleControlStateByTypeAdmin(int moduleType, ComponentName admin, Bundle bundle) throws RemoteException {
            return false;
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return null;
        }
    }

    /* loaded from: classes.dex */
    public static abstract class Stub extends Binder implements IOplusCustomizeModuleControlManagerService {
        static final int TRANSACTION_getModuleControlStateByType = 1;
        static final int TRANSACTION_getModuleControlStateByTypeAdmin = 3;
        static final int TRANSACTION_getModuleControlStateByTypeBundle = 2;

        public Stub() {
            attachInterface(this, IOplusCustomizeModuleControlManagerService.DESCRIPTOR);
        }

        public static IOplusCustomizeModuleControlManagerService asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(IOplusCustomizeModuleControlManagerService.DESCRIPTOR);
            if (iin != null && (iin instanceof IOplusCustomizeModuleControlManagerService)) {
                return (IOplusCustomizeModuleControlManagerService) iin;
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
                    return "getModuleControlStateByType";
                case 2:
                    return "getModuleControlStateByTypeBundle";
                case 3:
                    return "getModuleControlStateByTypeAdmin";
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
                data.enforceInterface(IOplusCustomizeModuleControlManagerService.DESCRIPTOR);
            }
            switch (code) {
                case 1598968902:
                    reply.writeString(IOplusCustomizeModuleControlManagerService.DESCRIPTOR);
                    return true;
                default:
                    switch (code) {
                        case 1:
                            int _arg0 = data.readInt();
                            data.enforceNoDataAvail();
                            boolean _result = getModuleControlStateByType(_arg0);
                            reply.writeNoException();
                            reply.writeBoolean(_result);
                            return true;
                        case 2:
                            int _arg02 = data.readInt();
                            Bundle _arg1 = (Bundle) data.readTypedObject(Bundle.CREATOR);
                            data.enforceNoDataAvail();
                            boolean _result2 = getModuleControlStateByTypeBundle(_arg02, _arg1);
                            reply.writeNoException();
                            reply.writeBoolean(_result2);
                            return true;
                        case 3:
                            int _arg03 = data.readInt();
                            ComponentName _arg12 = (ComponentName) data.readTypedObject(ComponentName.CREATOR);
                            Bundle _arg2 = (Bundle) data.readTypedObject(Bundle.CREATOR);
                            data.enforceNoDataAvail();
                            boolean _result3 = getModuleControlStateByTypeAdmin(_arg03, _arg12, _arg2);
                            reply.writeNoException();
                            reply.writeBoolean(_result3);
                            return true;
                        default:
                            return super.onTransact(code, data, reply, flags);
                    }
            }
        }

        /* loaded from: classes.dex */
        private static class Proxy implements IOplusCustomizeModuleControlManagerService {
            private IBinder mRemote;

            Proxy(IBinder remote) {
                this.mRemote = remote;
            }

            @Override // android.os.IInterface
            public IBinder asBinder() {
                return this.mRemote;
            }

            public String getInterfaceDescriptor() {
                return IOplusCustomizeModuleControlManagerService.DESCRIPTOR;
            }

            @Override // android.os.customize.IOplusCustomizeModuleControlManagerService
            public boolean getModuleControlStateByType(int moduleType) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizeModuleControlManagerService.DESCRIPTOR);
                    _data.writeInt(moduleType);
                    this.mRemote.transact(1, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizeModuleControlManagerService
            public boolean getModuleControlStateByTypeBundle(int moduleType, Bundle bundle) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizeModuleControlManagerService.DESCRIPTOR);
                    _data.writeInt(moduleType);
                    _data.writeTypedObject(bundle, 0);
                    this.mRemote.transact(2, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizeModuleControlManagerService
            public boolean getModuleControlStateByTypeAdmin(int moduleType, ComponentName admin, Bundle bundle) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizeModuleControlManagerService.DESCRIPTOR);
                    _data.writeInt(moduleType);
                    _data.writeTypedObject(admin, 0);
                    _data.writeTypedObject(bundle, 0);
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
