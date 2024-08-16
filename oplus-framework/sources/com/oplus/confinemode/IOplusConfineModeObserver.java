package com.oplus.confinemode;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

/* loaded from: classes.dex */
public interface IOplusConfineModeObserver extends IInterface {
    public static final String DESCRIPTOR = "com.oplus.confinemode.IOplusConfineModeObserver";

    void onChange(int i, int i2, int i3) throws RemoteException;

    /* loaded from: classes.dex */
    public static class Default implements IOplusConfineModeObserver {
        @Override // com.oplus.confinemode.IOplusConfineModeObserver
        public void onChange(int oldMode, int newMode, int userId) throws RemoteException {
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return null;
        }
    }

    /* loaded from: classes.dex */
    public static abstract class Stub extends Binder implements IOplusConfineModeObserver {
        static final int TRANSACTION_onChange = 1;

        public Stub() {
            attachInterface(this, IOplusConfineModeObserver.DESCRIPTOR);
        }

        public static IOplusConfineModeObserver asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(IOplusConfineModeObserver.DESCRIPTOR);
            if (iin != null && (iin instanceof IOplusConfineModeObserver)) {
                return (IOplusConfineModeObserver) iin;
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
                    return "onChange";
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
                data.enforceInterface(IOplusConfineModeObserver.DESCRIPTOR);
            }
            switch (code) {
                case 1598968902:
                    reply.writeString(IOplusConfineModeObserver.DESCRIPTOR);
                    return true;
                default:
                    switch (code) {
                        case 1:
                            int _arg0 = data.readInt();
                            int _arg1 = data.readInt();
                            int _arg2 = data.readInt();
                            data.enforceNoDataAvail();
                            onChange(_arg0, _arg1, _arg2);
                            return true;
                        default:
                            return super.onTransact(code, data, reply, flags);
                    }
            }
        }

        /* loaded from: classes.dex */
        private static class Proxy implements IOplusConfineModeObserver {
            private IBinder mRemote;

            Proxy(IBinder remote) {
                this.mRemote = remote;
            }

            @Override // android.os.IInterface
            public IBinder asBinder() {
                return this.mRemote;
            }

            public String getInterfaceDescriptor() {
                return IOplusConfineModeObserver.DESCRIPTOR;
            }

            @Override // com.oplus.confinemode.IOplusConfineModeObserver
            public void onChange(int oldMode, int newMode, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(IOplusConfineModeObserver.DESCRIPTOR);
                    _data.writeInt(oldMode);
                    _data.writeInt(newMode);
                    _data.writeInt(userId);
                    this.mRemote.transact(1, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }
        }

        public int getMaxTransactionId() {
            return 0;
        }
    }
}
