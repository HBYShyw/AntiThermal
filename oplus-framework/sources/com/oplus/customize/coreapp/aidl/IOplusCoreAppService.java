package com.oplus.customize.coreapp.aidl;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

/* loaded from: classes.dex */
public interface IOplusCoreAppService extends IInterface {
    public static final String DESCRIPTOR = "com.oplus.customize.coreapp.aidl.IOplusCoreAppService";

    IBinder getManager(String str) throws RemoteException;

    boolean isPackageContainsOplusCertificates(String str) throws RemoteException;

    void onBootPhase(int i) throws RemoteException;

    /* loaded from: classes.dex */
    public static class Default implements IOplusCoreAppService {
        @Override // com.oplus.customize.coreapp.aidl.IOplusCoreAppService
        public IBinder getManager(String strManagerName) throws RemoteException {
            return null;
        }

        @Override // com.oplus.customize.coreapp.aidl.IOplusCoreAppService
        public boolean isPackageContainsOplusCertificates(String packageName) throws RemoteException {
            return false;
        }

        @Override // com.oplus.customize.coreapp.aidl.IOplusCoreAppService
        public void onBootPhase(int phase) throws RemoteException {
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return null;
        }
    }

    /* loaded from: classes.dex */
    public static abstract class Stub extends Binder implements IOplusCoreAppService {
        static final int TRANSACTION_getManager = 1;
        static final int TRANSACTION_isPackageContainsOplusCertificates = 2;
        static final int TRANSACTION_onBootPhase = 3;

        public Stub() {
            attachInterface(this, IOplusCoreAppService.DESCRIPTOR);
        }

        public static IOplusCoreAppService asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(IOplusCoreAppService.DESCRIPTOR);
            if (iin != null && (iin instanceof IOplusCoreAppService)) {
                return (IOplusCoreAppService) iin;
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
                    return "getManager";
                case 2:
                    return "isPackageContainsOplusCertificates";
                case 3:
                    return "onBootPhase";
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
                data.enforceInterface(IOplusCoreAppService.DESCRIPTOR);
            }
            switch (code) {
                case 1598968902:
                    reply.writeString(IOplusCoreAppService.DESCRIPTOR);
                    return true;
                default:
                    switch (code) {
                        case 1:
                            String _arg0 = data.readString();
                            data.enforceNoDataAvail();
                            IBinder _result = getManager(_arg0);
                            reply.writeNoException();
                            reply.writeStrongBinder(_result);
                            return true;
                        case 2:
                            String _arg02 = data.readString();
                            data.enforceNoDataAvail();
                            boolean _result2 = isPackageContainsOplusCertificates(_arg02);
                            reply.writeNoException();
                            reply.writeBoolean(_result2);
                            return true;
                        case 3:
                            int _arg03 = data.readInt();
                            data.enforceNoDataAvail();
                            onBootPhase(_arg03);
                            return true;
                        default:
                            return super.onTransact(code, data, reply, flags);
                    }
            }
        }

        /* loaded from: classes.dex */
        private static class Proxy implements IOplusCoreAppService {
            private IBinder mRemote;

            Proxy(IBinder remote) {
                this.mRemote = remote;
            }

            @Override // android.os.IInterface
            public IBinder asBinder() {
                return this.mRemote;
            }

            public String getInterfaceDescriptor() {
                return IOplusCoreAppService.DESCRIPTOR;
            }

            @Override // com.oplus.customize.coreapp.aidl.IOplusCoreAppService
            public IBinder getManager(String strManagerName) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCoreAppService.DESCRIPTOR);
                    _data.writeString(strManagerName);
                    this.mRemote.transact(1, _data, _reply, 0);
                    _reply.readException();
                    IBinder _result = _reply.readStrongBinder();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.oplus.customize.coreapp.aidl.IOplusCoreAppService
            public boolean isPackageContainsOplusCertificates(String packageName) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCoreAppService.DESCRIPTOR);
                    _data.writeString(packageName);
                    this.mRemote.transact(2, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.oplus.customize.coreapp.aidl.IOplusCoreAppService
            public void onBootPhase(int phase) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(IOplusCoreAppService.DESCRIPTOR);
                    _data.writeInt(phase);
                    this.mRemote.transact(3, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }
        }

        public int getMaxTransactionId() {
            return 2;
        }
    }
}
