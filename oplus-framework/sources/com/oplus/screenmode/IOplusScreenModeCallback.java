package com.oplus.screenmode;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

/* loaded from: classes.dex */
public interface IOplusScreenModeCallback extends IInterface {
    public static final String DESCRIPTOR = "com.oplus.screenmode.IOplusScreenModeCallback";

    void OnNotificationChange(boolean z) throws RemoteException;

    void notifyMemcStatus(boolean z) throws RemoteException;

    void notifyRequestNewRefresh(int i, boolean z, int i2) throws RemoteException;

    void requestRefreshRate(String str, int i) throws RemoteException;

    /* loaded from: classes.dex */
    public static class Default implements IOplusScreenModeCallback {
        @Override // com.oplus.screenmode.IOplusScreenModeCallback
        public void requestRefreshRate(String pkg, int rate) throws RemoteException {
        }

        @Override // com.oplus.screenmode.IOplusScreenModeCallback
        public void OnNotificationChange(boolean enable) throws RemoteException {
        }

        @Override // com.oplus.screenmode.IOplusScreenModeCallback
        public void notifyRequestNewRefresh(int priority, boolean open, int rate) throws RemoteException {
        }

        @Override // com.oplus.screenmode.IOplusScreenModeCallback
        public void notifyMemcStatus(boolean memc) throws RemoteException {
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return null;
        }
    }

    /* loaded from: classes.dex */
    public static abstract class Stub extends Binder implements IOplusScreenModeCallback {
        static final int TRANSACTION_OnNotificationChange = 2;
        static final int TRANSACTION_notifyMemcStatus = 4;
        static final int TRANSACTION_notifyRequestNewRefresh = 3;
        static final int TRANSACTION_requestRefreshRate = 1;

        public Stub() {
            attachInterface(this, IOplusScreenModeCallback.DESCRIPTOR);
        }

        public static IOplusScreenModeCallback asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(IOplusScreenModeCallback.DESCRIPTOR);
            if (iin != null && (iin instanceof IOplusScreenModeCallback)) {
                return (IOplusScreenModeCallback) iin;
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
                    return "requestRefreshRate";
                case 2:
                    return "OnNotificationChange";
                case 3:
                    return "notifyRequestNewRefresh";
                case 4:
                    return "notifyMemcStatus";
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
                data.enforceInterface(IOplusScreenModeCallback.DESCRIPTOR);
            }
            switch (code) {
                case 1598968902:
                    reply.writeString(IOplusScreenModeCallback.DESCRIPTOR);
                    return true;
                default:
                    switch (code) {
                        case 1:
                            String _arg0 = data.readString();
                            int _arg1 = data.readInt();
                            data.enforceNoDataAvail();
                            requestRefreshRate(_arg0, _arg1);
                            return true;
                        case 2:
                            boolean _arg02 = data.readBoolean();
                            data.enforceNoDataAvail();
                            OnNotificationChange(_arg02);
                            return true;
                        case 3:
                            int _arg03 = data.readInt();
                            boolean _arg12 = data.readBoolean();
                            int _arg2 = data.readInt();
                            data.enforceNoDataAvail();
                            notifyRequestNewRefresh(_arg03, _arg12, _arg2);
                            return true;
                        case 4:
                            boolean _arg04 = data.readBoolean();
                            data.enforceNoDataAvail();
                            notifyMemcStatus(_arg04);
                            return true;
                        default:
                            return super.onTransact(code, data, reply, flags);
                    }
            }
        }

        /* loaded from: classes.dex */
        private static class Proxy implements IOplusScreenModeCallback {
            private IBinder mRemote;

            Proxy(IBinder remote) {
                this.mRemote = remote;
            }

            @Override // android.os.IInterface
            public IBinder asBinder() {
                return this.mRemote;
            }

            public String getInterfaceDescriptor() {
                return IOplusScreenModeCallback.DESCRIPTOR;
            }

            @Override // com.oplus.screenmode.IOplusScreenModeCallback
            public void requestRefreshRate(String pkg, int rate) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(IOplusScreenModeCallback.DESCRIPTOR);
                    _data.writeString(pkg);
                    _data.writeInt(rate);
                    this.mRemote.transact(1, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // com.oplus.screenmode.IOplusScreenModeCallback
            public void OnNotificationChange(boolean enable) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(IOplusScreenModeCallback.DESCRIPTOR);
                    _data.writeBoolean(enable);
                    this.mRemote.transact(2, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // com.oplus.screenmode.IOplusScreenModeCallback
            public void notifyRequestNewRefresh(int priority, boolean open, int rate) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(IOplusScreenModeCallback.DESCRIPTOR);
                    _data.writeInt(priority);
                    _data.writeBoolean(open);
                    _data.writeInt(rate);
                    this.mRemote.transact(3, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // com.oplus.screenmode.IOplusScreenModeCallback
            public void notifyMemcStatus(boolean memc) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(IOplusScreenModeCallback.DESCRIPTOR);
                    _data.writeBoolean(memc);
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
