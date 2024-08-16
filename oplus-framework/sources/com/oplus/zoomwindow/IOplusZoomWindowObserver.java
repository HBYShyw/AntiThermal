package com.oplus.zoomwindow;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

/* loaded from: classes.dex */
public interface IOplusZoomWindowObserver extends IInterface {
    public static final String DESCRIPTOR = "com.oplus.zoomwindow.IOplusZoomWindowObserver";

    void onInputMethodChanged(boolean z) throws RemoteException;

    void onZoomWindowDied(String str) throws RemoteException;

    void onZoomWindowHide(OplusZoomWindowInfo oplusZoomWindowInfo) throws RemoteException;

    void onZoomWindowShow(OplusZoomWindowInfo oplusZoomWindowInfo) throws RemoteException;

    /* loaded from: classes.dex */
    public static class Default implements IOplusZoomWindowObserver {
        @Override // com.oplus.zoomwindow.IOplusZoomWindowObserver
        public void onZoomWindowShow(OplusZoomWindowInfo info) throws RemoteException {
        }

        @Override // com.oplus.zoomwindow.IOplusZoomWindowObserver
        public void onZoomWindowHide(OplusZoomWindowInfo info) throws RemoteException {
        }

        @Override // com.oplus.zoomwindow.IOplusZoomWindowObserver
        public void onZoomWindowDied(String appName) throws RemoteException {
        }

        @Override // com.oplus.zoomwindow.IOplusZoomWindowObserver
        public void onInputMethodChanged(boolean isShown) throws RemoteException {
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return null;
        }
    }

    /* loaded from: classes.dex */
    public static abstract class Stub extends Binder implements IOplusZoomWindowObserver {
        static final int TRANSACTION_onInputMethodChanged = 4;
        static final int TRANSACTION_onZoomWindowDied = 3;
        static final int TRANSACTION_onZoomWindowHide = 2;
        static final int TRANSACTION_onZoomWindowShow = 1;

        public Stub() {
            attachInterface(this, IOplusZoomWindowObserver.DESCRIPTOR);
        }

        public static IOplusZoomWindowObserver asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(IOplusZoomWindowObserver.DESCRIPTOR);
            if (iin != null && (iin instanceof IOplusZoomWindowObserver)) {
                return (IOplusZoomWindowObserver) iin;
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
                    return "onZoomWindowShow";
                case 2:
                    return "onZoomWindowHide";
                case 3:
                    return "onZoomWindowDied";
                case 4:
                    return "onInputMethodChanged";
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
                data.enforceInterface(IOplusZoomWindowObserver.DESCRIPTOR);
            }
            switch (code) {
                case 1598968902:
                    reply.writeString(IOplusZoomWindowObserver.DESCRIPTOR);
                    return true;
                default:
                    switch (code) {
                        case 1:
                            OplusZoomWindowInfo _arg0 = (OplusZoomWindowInfo) data.readTypedObject(OplusZoomWindowInfo.CREATOR);
                            data.enforceNoDataAvail();
                            onZoomWindowShow(_arg0);
                            return true;
                        case 2:
                            OplusZoomWindowInfo _arg02 = (OplusZoomWindowInfo) data.readTypedObject(OplusZoomWindowInfo.CREATOR);
                            data.enforceNoDataAvail();
                            onZoomWindowHide(_arg02);
                            return true;
                        case 3:
                            String _arg03 = data.readString();
                            data.enforceNoDataAvail();
                            onZoomWindowDied(_arg03);
                            return true;
                        case 4:
                            boolean _arg04 = data.readBoolean();
                            data.enforceNoDataAvail();
                            onInputMethodChanged(_arg04);
                            return true;
                        default:
                            return super.onTransact(code, data, reply, flags);
                    }
            }
        }

        /* loaded from: classes.dex */
        private static class Proxy implements IOplusZoomWindowObserver {
            private IBinder mRemote;

            Proxy(IBinder remote) {
                this.mRemote = remote;
            }

            @Override // android.os.IInterface
            public IBinder asBinder() {
                return this.mRemote;
            }

            public String getInterfaceDescriptor() {
                return IOplusZoomWindowObserver.DESCRIPTOR;
            }

            @Override // com.oplus.zoomwindow.IOplusZoomWindowObserver
            public void onZoomWindowShow(OplusZoomWindowInfo info) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(IOplusZoomWindowObserver.DESCRIPTOR);
                    _data.writeTypedObject(info, 0);
                    this.mRemote.transact(1, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // com.oplus.zoomwindow.IOplusZoomWindowObserver
            public void onZoomWindowHide(OplusZoomWindowInfo info) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(IOplusZoomWindowObserver.DESCRIPTOR);
                    _data.writeTypedObject(info, 0);
                    this.mRemote.transact(2, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // com.oplus.zoomwindow.IOplusZoomWindowObserver
            public void onZoomWindowDied(String appName) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(IOplusZoomWindowObserver.DESCRIPTOR);
                    _data.writeString(appName);
                    this.mRemote.transact(3, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // com.oplus.zoomwindow.IOplusZoomWindowObserver
            public void onInputMethodChanged(boolean isShown) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(IOplusZoomWindowObserver.DESCRIPTOR);
                    _data.writeBoolean(isShown);
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
