package com.oplus.zoomwindow;

import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

/* loaded from: classes.dex */
public interface IOplusZoomAppObserver extends IInterface {
    public static final String DESCRIPTOR = "com.oplus.zoomwindow.IOplusZoomAppObserver";

    void notifyControlViewChange(OplusZoomWindowInfo oplusZoomWindowInfo) throws RemoteException;

    void notifyDecorationChange(int i) throws RemoteException;

    void notifyShowCompatibilityToast(int i, int i2, String str, String str2, Bundle bundle) throws RemoteException;

    /* loaded from: classes.dex */
    public static class Default implements IOplusZoomAppObserver {
        @Override // com.oplus.zoomwindow.IOplusZoomAppObserver
        public void notifyControlViewChange(OplusZoomWindowInfo info) throws RemoteException {
        }

        @Override // com.oplus.zoomwindow.IOplusZoomAppObserver
        public void notifyShowCompatibilityToast(int type, int userId, String target, String callPkg, Bundle extension) throws RemoteException {
        }

        @Override // com.oplus.zoomwindow.IOplusZoomAppObserver
        public void notifyDecorationChange(int type) throws RemoteException {
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return null;
        }
    }

    /* loaded from: classes.dex */
    public static abstract class Stub extends Binder implements IOplusZoomAppObserver {
        static final int TRANSACTION_notifyControlViewChange = 1;
        static final int TRANSACTION_notifyDecorationChange = 3;
        static final int TRANSACTION_notifyShowCompatibilityToast = 2;

        public Stub() {
            attachInterface(this, IOplusZoomAppObserver.DESCRIPTOR);
        }

        public static IOplusZoomAppObserver asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(IOplusZoomAppObserver.DESCRIPTOR);
            if (iin != null && (iin instanceof IOplusZoomAppObserver)) {
                return (IOplusZoomAppObserver) iin;
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
                    return "notifyControlViewChange";
                case 2:
                    return "notifyShowCompatibilityToast";
                case 3:
                    return "notifyDecorationChange";
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
                data.enforceInterface(IOplusZoomAppObserver.DESCRIPTOR);
            }
            switch (code) {
                case 1598968902:
                    reply.writeString(IOplusZoomAppObserver.DESCRIPTOR);
                    return true;
                default:
                    switch (code) {
                        case 1:
                            OplusZoomWindowInfo _arg0 = (OplusZoomWindowInfo) data.readTypedObject(OplusZoomWindowInfo.CREATOR);
                            data.enforceNoDataAvail();
                            notifyControlViewChange(_arg0);
                            return true;
                        case 2:
                            int _arg02 = data.readInt();
                            int _arg1 = data.readInt();
                            String _arg2 = data.readString();
                            String _arg3 = data.readString();
                            Bundle _arg4 = (Bundle) data.readTypedObject(Bundle.CREATOR);
                            data.enforceNoDataAvail();
                            notifyShowCompatibilityToast(_arg02, _arg1, _arg2, _arg3, _arg4);
                            return true;
                        case 3:
                            int _arg03 = data.readInt();
                            data.enforceNoDataAvail();
                            notifyDecorationChange(_arg03);
                            return true;
                        default:
                            return super.onTransact(code, data, reply, flags);
                    }
            }
        }

        /* loaded from: classes.dex */
        private static class Proxy implements IOplusZoomAppObserver {
            private IBinder mRemote;

            Proxy(IBinder remote) {
                this.mRemote = remote;
            }

            @Override // android.os.IInterface
            public IBinder asBinder() {
                return this.mRemote;
            }

            public String getInterfaceDescriptor() {
                return IOplusZoomAppObserver.DESCRIPTOR;
            }

            @Override // com.oplus.zoomwindow.IOplusZoomAppObserver
            public void notifyControlViewChange(OplusZoomWindowInfo info) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(IOplusZoomAppObserver.DESCRIPTOR);
                    _data.writeTypedObject(info, 0);
                    this.mRemote.transact(1, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // com.oplus.zoomwindow.IOplusZoomAppObserver
            public void notifyShowCompatibilityToast(int type, int userId, String target, String callPkg, Bundle extension) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(IOplusZoomAppObserver.DESCRIPTOR);
                    _data.writeInt(type);
                    _data.writeInt(userId);
                    _data.writeString(target);
                    _data.writeString(callPkg);
                    _data.writeTypedObject(extension, 0);
                    this.mRemote.transact(2, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // com.oplus.zoomwindow.IOplusZoomAppObserver
            public void notifyDecorationChange(int type) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(IOplusZoomAppObserver.DESCRIPTOR);
                    _data.writeInt(type);
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
