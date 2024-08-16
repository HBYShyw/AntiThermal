package com.oplus.app;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

/* loaded from: classes.dex */
public interface IOplusZoomWindowConfigChangedListener extends IInterface {
    public static final String DESCRIPTOR = "com.oplus.app.IOplusZoomWindowConfigChangedListener";

    void onConfigSwitchChanged(boolean z) throws RemoteException;

    void onConfigTypeChanged(int i) throws RemoteException;

    /* loaded from: classes.dex */
    public static class Default implements IOplusZoomWindowConfigChangedListener {
        @Override // com.oplus.app.IOplusZoomWindowConfigChangedListener
        public void onConfigTypeChanged(int type) throws RemoteException {
        }

        @Override // com.oplus.app.IOplusZoomWindowConfigChangedListener
        public void onConfigSwitchChanged(boolean enable) throws RemoteException {
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return null;
        }
    }

    /* loaded from: classes.dex */
    public static abstract class Stub extends Binder implements IOplusZoomWindowConfigChangedListener {
        static final int TRANSACTION_onConfigSwitchChanged = 2;
        static final int TRANSACTION_onConfigTypeChanged = 1;

        public Stub() {
            attachInterface(this, IOplusZoomWindowConfigChangedListener.DESCRIPTOR);
        }

        public static IOplusZoomWindowConfigChangedListener asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(IOplusZoomWindowConfigChangedListener.DESCRIPTOR);
            if (iin != null && (iin instanceof IOplusZoomWindowConfigChangedListener)) {
                return (IOplusZoomWindowConfigChangedListener) iin;
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
                    return "onConfigTypeChanged";
                case 2:
                    return "onConfigSwitchChanged";
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
                data.enforceInterface(IOplusZoomWindowConfigChangedListener.DESCRIPTOR);
            }
            switch (code) {
                case 1598968902:
                    reply.writeString(IOplusZoomWindowConfigChangedListener.DESCRIPTOR);
                    return true;
                default:
                    switch (code) {
                        case 1:
                            int _arg0 = data.readInt();
                            data.enforceNoDataAvail();
                            onConfigTypeChanged(_arg0);
                            return true;
                        case 2:
                            boolean _arg02 = data.readBoolean();
                            data.enforceNoDataAvail();
                            onConfigSwitchChanged(_arg02);
                            return true;
                        default:
                            return super.onTransact(code, data, reply, flags);
                    }
            }
        }

        /* loaded from: classes.dex */
        private static class Proxy implements IOplusZoomWindowConfigChangedListener {
            private IBinder mRemote;

            Proxy(IBinder remote) {
                this.mRemote = remote;
            }

            @Override // android.os.IInterface
            public IBinder asBinder() {
                return this.mRemote;
            }

            public String getInterfaceDescriptor() {
                return IOplusZoomWindowConfigChangedListener.DESCRIPTOR;
            }

            @Override // com.oplus.app.IOplusZoomWindowConfigChangedListener
            public void onConfigTypeChanged(int type) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(IOplusZoomWindowConfigChangedListener.DESCRIPTOR);
                    _data.writeInt(type);
                    this.mRemote.transact(1, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // com.oplus.app.IOplusZoomWindowConfigChangedListener
            public void onConfigSwitchChanged(boolean enable) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(IOplusZoomWindowConfigChangedListener.DESCRIPTOR);
                    _data.writeBoolean(enable);
                    this.mRemote.transact(2, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }
        }

        public int getMaxTransactionId() {
            return 1;
        }
    }
}
