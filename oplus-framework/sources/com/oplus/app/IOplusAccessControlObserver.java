package com.oplus.app;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

/* loaded from: classes.dex */
public interface IOplusAccessControlObserver extends IInterface {
    public static final String DESCRIPTOR = "com.oplus.app.IOplusAccessControlObserver";

    void onEncryptEnableChange(boolean z) throws RemoteException;

    void onEncryptStateChange(OplusAccessControlInfo oplusAccessControlInfo) throws RemoteException;

    void onHideEnableChange(boolean z) throws RemoteException;

    void onHideStateChange(OplusAccessControlInfo oplusAccessControlInfo) throws RemoteException;

    /* loaded from: classes.dex */
    public static class Default implements IOplusAccessControlObserver {
        @Override // com.oplus.app.IOplusAccessControlObserver
        public void onEncryptStateChange(OplusAccessControlInfo info) throws RemoteException {
        }

        @Override // com.oplus.app.IOplusAccessControlObserver
        public void onHideStateChange(OplusAccessControlInfo info) throws RemoteException {
        }

        @Override // com.oplus.app.IOplusAccessControlObserver
        public void onEncryptEnableChange(boolean enable) throws RemoteException {
        }

        @Override // com.oplus.app.IOplusAccessControlObserver
        public void onHideEnableChange(boolean enable) throws RemoteException {
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return null;
        }
    }

    /* loaded from: classes.dex */
    public static abstract class Stub extends Binder implements IOplusAccessControlObserver {
        static final int TRANSACTION_onEncryptEnableChange = 3;
        static final int TRANSACTION_onEncryptStateChange = 1;
        static final int TRANSACTION_onHideEnableChange = 4;
        static final int TRANSACTION_onHideStateChange = 2;

        public Stub() {
            attachInterface(this, IOplusAccessControlObserver.DESCRIPTOR);
        }

        public static IOplusAccessControlObserver asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(IOplusAccessControlObserver.DESCRIPTOR);
            if (iin != null && (iin instanceof IOplusAccessControlObserver)) {
                return (IOplusAccessControlObserver) iin;
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
                    return "onEncryptStateChange";
                case 2:
                    return "onHideStateChange";
                case 3:
                    return "onEncryptEnableChange";
                case 4:
                    return "onHideEnableChange";
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
                data.enforceInterface(IOplusAccessControlObserver.DESCRIPTOR);
            }
            switch (code) {
                case 1598968902:
                    reply.writeString(IOplusAccessControlObserver.DESCRIPTOR);
                    return true;
                default:
                    switch (code) {
                        case 1:
                            OplusAccessControlInfo _arg0 = (OplusAccessControlInfo) data.readTypedObject(OplusAccessControlInfo.CREATOR);
                            data.enforceNoDataAvail();
                            onEncryptStateChange(_arg0);
                            return true;
                        case 2:
                            OplusAccessControlInfo _arg02 = (OplusAccessControlInfo) data.readTypedObject(OplusAccessControlInfo.CREATOR);
                            data.enforceNoDataAvail();
                            onHideStateChange(_arg02);
                            return true;
                        case 3:
                            boolean _arg03 = data.readBoolean();
                            data.enforceNoDataAvail();
                            onEncryptEnableChange(_arg03);
                            return true;
                        case 4:
                            boolean _arg04 = data.readBoolean();
                            data.enforceNoDataAvail();
                            onHideEnableChange(_arg04);
                            return true;
                        default:
                            return super.onTransact(code, data, reply, flags);
                    }
            }
        }

        /* loaded from: classes.dex */
        private static class Proxy implements IOplusAccessControlObserver {
            private IBinder mRemote;

            Proxy(IBinder remote) {
                this.mRemote = remote;
            }

            @Override // android.os.IInterface
            public IBinder asBinder() {
                return this.mRemote;
            }

            public String getInterfaceDescriptor() {
                return IOplusAccessControlObserver.DESCRIPTOR;
            }

            @Override // com.oplus.app.IOplusAccessControlObserver
            public void onEncryptStateChange(OplusAccessControlInfo info) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(IOplusAccessControlObserver.DESCRIPTOR);
                    _data.writeTypedObject(info, 0);
                    this.mRemote.transact(1, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // com.oplus.app.IOplusAccessControlObserver
            public void onHideStateChange(OplusAccessControlInfo info) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(IOplusAccessControlObserver.DESCRIPTOR);
                    _data.writeTypedObject(info, 0);
                    this.mRemote.transact(2, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // com.oplus.app.IOplusAccessControlObserver
            public void onEncryptEnableChange(boolean enable) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(IOplusAccessControlObserver.DESCRIPTOR);
                    _data.writeBoolean(enable);
                    this.mRemote.transact(3, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // com.oplus.app.IOplusAccessControlObserver
            public void onHideEnableChange(boolean enable) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(IOplusAccessControlObserver.DESCRIPTOR);
                    _data.writeBoolean(enable);
                    this.mRemote.transact(4, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }
        }

        public int getMaxTransactionId() {
            return 3;
        }
    }
}
