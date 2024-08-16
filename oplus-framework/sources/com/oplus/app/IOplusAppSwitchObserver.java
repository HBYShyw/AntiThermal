package com.oplus.app;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

/* loaded from: classes.dex */
public interface IOplusAppSwitchObserver extends IInterface {
    public static final String DESCRIPTOR = "com.oplus.app.IOplusAppSwitchObserver";

    void onActivityEnter(OplusAppEnterInfo oplusAppEnterInfo) throws RemoteException;

    void onActivityExit(OplusAppExitInfo oplusAppExitInfo) throws RemoteException;

    void onAppEnter(OplusAppEnterInfo oplusAppEnterInfo) throws RemoteException;

    void onAppExit(OplusAppExitInfo oplusAppExitInfo) throws RemoteException;

    /* loaded from: classes.dex */
    public static class Default implements IOplusAppSwitchObserver {
        @Override // com.oplus.app.IOplusAppSwitchObserver
        public void onAppEnter(OplusAppEnterInfo info) throws RemoteException {
        }

        @Override // com.oplus.app.IOplusAppSwitchObserver
        public void onAppExit(OplusAppExitInfo info) throws RemoteException {
        }

        @Override // com.oplus.app.IOplusAppSwitchObserver
        public void onActivityEnter(OplusAppEnterInfo info) throws RemoteException {
        }

        @Override // com.oplus.app.IOplusAppSwitchObserver
        public void onActivityExit(OplusAppExitInfo info) throws RemoteException {
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return null;
        }
    }

    /* loaded from: classes.dex */
    public static abstract class Stub extends Binder implements IOplusAppSwitchObserver {
        static final int TRANSACTION_onActivityEnter = 3;
        static final int TRANSACTION_onActivityExit = 4;
        static final int TRANSACTION_onAppEnter = 1;
        static final int TRANSACTION_onAppExit = 2;

        public Stub() {
            attachInterface(this, IOplusAppSwitchObserver.DESCRIPTOR);
        }

        public static IOplusAppSwitchObserver asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(IOplusAppSwitchObserver.DESCRIPTOR);
            if (iin != null && (iin instanceof IOplusAppSwitchObserver)) {
                return (IOplusAppSwitchObserver) iin;
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
                    return "onAppEnter";
                case 2:
                    return "onAppExit";
                case 3:
                    return "onActivityEnter";
                case 4:
                    return "onActivityExit";
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
                data.enforceInterface(IOplusAppSwitchObserver.DESCRIPTOR);
            }
            switch (code) {
                case 1598968902:
                    reply.writeString(IOplusAppSwitchObserver.DESCRIPTOR);
                    return true;
                default:
                    switch (code) {
                        case 1:
                            OplusAppEnterInfo _arg0 = (OplusAppEnterInfo) data.readTypedObject(OplusAppEnterInfo.CREATOR);
                            data.enforceNoDataAvail();
                            onAppEnter(_arg0);
                            return true;
                        case 2:
                            OplusAppExitInfo _arg02 = (OplusAppExitInfo) data.readTypedObject(OplusAppExitInfo.CREATOR);
                            data.enforceNoDataAvail();
                            onAppExit(_arg02);
                            return true;
                        case 3:
                            OplusAppEnterInfo _arg03 = (OplusAppEnterInfo) data.readTypedObject(OplusAppEnterInfo.CREATOR);
                            data.enforceNoDataAvail();
                            onActivityEnter(_arg03);
                            return true;
                        case 4:
                            OplusAppExitInfo _arg04 = (OplusAppExitInfo) data.readTypedObject(OplusAppExitInfo.CREATOR);
                            data.enforceNoDataAvail();
                            onActivityExit(_arg04);
                            return true;
                        default:
                            return super.onTransact(code, data, reply, flags);
                    }
            }
        }

        /* loaded from: classes.dex */
        private static class Proxy implements IOplusAppSwitchObserver {
            private IBinder mRemote;

            Proxy(IBinder remote) {
                this.mRemote = remote;
            }

            @Override // android.os.IInterface
            public IBinder asBinder() {
                return this.mRemote;
            }

            public String getInterfaceDescriptor() {
                return IOplusAppSwitchObserver.DESCRIPTOR;
            }

            @Override // com.oplus.app.IOplusAppSwitchObserver
            public void onAppEnter(OplusAppEnterInfo info) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(IOplusAppSwitchObserver.DESCRIPTOR);
                    _data.writeTypedObject(info, 0);
                    this.mRemote.transact(1, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // com.oplus.app.IOplusAppSwitchObserver
            public void onAppExit(OplusAppExitInfo info) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(IOplusAppSwitchObserver.DESCRIPTOR);
                    _data.writeTypedObject(info, 0);
                    this.mRemote.transact(2, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // com.oplus.app.IOplusAppSwitchObserver
            public void onActivityEnter(OplusAppEnterInfo info) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(IOplusAppSwitchObserver.DESCRIPTOR);
                    _data.writeTypedObject(info, 0);
                    this.mRemote.transact(3, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // com.oplus.app.IOplusAppSwitchObserver
            public void onActivityExit(OplusAppExitInfo info) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(IOplusAppSwitchObserver.DESCRIPTOR);
                    _data.writeTypedObject(info, 0);
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
