package com.oplus.nwpower;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

/* loaded from: classes.dex */
public interface IOSysNetControl extends IInterface {
    public static final String DESCRIPTOR = "com.oplus.nwpower.IOSysNetControl";

    void setDataEnabled(boolean z) throws RemoteException;

    void setWifiEnabled(boolean z) throws RemoteException;

    /* loaded from: classes.dex */
    public static class Default implements IOSysNetControl {
        @Override // com.oplus.nwpower.IOSysNetControl
        public void setDataEnabled(boolean enable) throws RemoteException {
        }

        @Override // com.oplus.nwpower.IOSysNetControl
        public void setWifiEnabled(boolean enable) throws RemoteException {
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return null;
        }
    }

    /* loaded from: classes.dex */
    public static abstract class Stub extends Binder implements IOSysNetControl {
        static final int TRANSACTION_setDataEnabled = 1;
        static final int TRANSACTION_setWifiEnabled = 2;

        public Stub() {
            attachInterface(this, IOSysNetControl.DESCRIPTOR);
        }

        public static IOSysNetControl asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(IOSysNetControl.DESCRIPTOR);
            if (iin != null && (iin instanceof IOSysNetControl)) {
                return (IOSysNetControl) iin;
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
                    return "setDataEnabled";
                case 2:
                    return "setWifiEnabled";
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
                data.enforceInterface(IOSysNetControl.DESCRIPTOR);
            }
            switch (code) {
                case 1598968902:
                    reply.writeString(IOSysNetControl.DESCRIPTOR);
                    return true;
                default:
                    switch (code) {
                        case 1:
                            boolean _arg0 = data.readBoolean();
                            data.enforceNoDataAvail();
                            setDataEnabled(_arg0);
                            reply.writeNoException();
                            return true;
                        case 2:
                            boolean _arg02 = data.readBoolean();
                            data.enforceNoDataAvail();
                            setWifiEnabled(_arg02);
                            reply.writeNoException();
                            return true;
                        default:
                            return super.onTransact(code, data, reply, flags);
                    }
            }
        }

        /* loaded from: classes.dex */
        private static class Proxy implements IOSysNetControl {
            private IBinder mRemote;

            Proxy(IBinder remote) {
                this.mRemote = remote;
            }

            @Override // android.os.IInterface
            public IBinder asBinder() {
                return this.mRemote;
            }

            public String getInterfaceDescriptor() {
                return IOSysNetControl.DESCRIPTOR;
            }

            @Override // com.oplus.nwpower.IOSysNetControl
            public void setDataEnabled(boolean enable) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOSysNetControl.DESCRIPTOR);
                    _data.writeBoolean(enable);
                    this.mRemote.transact(1, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.oplus.nwpower.IOSysNetControl
            public void setWifiEnabled(boolean enable) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOSysNetControl.DESCRIPTOR);
                    _data.writeBoolean(enable);
                    this.mRemote.transact(2, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
        }

        public int getMaxTransactionId() {
            return 1;
        }
    }
}
