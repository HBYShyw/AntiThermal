package com.oplus.dynamicvsync;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

/* loaded from: classes.dex */
public interface IOplusDynamicVsync extends IInterface {
    public static final String DESCRIPTOR = "com.oplus.dynamicvsync.IOplusDynamicVsync";

    void doAnimation(int i, String str) throws RemoteException;

    void flingEvent(String str, int i) throws RemoteException;

    /* loaded from: classes.dex */
    public static class Default implements IOplusDynamicVsync {
        @Override // com.oplus.dynamicvsync.IOplusDynamicVsync
        public void doAnimation(int durationInMs, String detail) throws RemoteException {
        }

        @Override // com.oplus.dynamicvsync.IOplusDynamicVsync
        public void flingEvent(String pkgName, int durationInMs) throws RemoteException {
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return null;
        }
    }

    /* loaded from: classes.dex */
    public static abstract class Stub extends Binder implements IOplusDynamicVsync {
        static final int TRANSACTION_doAnimation = 1;
        static final int TRANSACTION_flingEvent = 2;

        public Stub() {
            attachInterface(this, IOplusDynamicVsync.DESCRIPTOR);
        }

        public static IOplusDynamicVsync asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(IOplusDynamicVsync.DESCRIPTOR);
            if (iin != null && (iin instanceof IOplusDynamicVsync)) {
                return (IOplusDynamicVsync) iin;
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
                    return "doAnimation";
                case 2:
                    return "flingEvent";
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
                data.enforceInterface(IOplusDynamicVsync.DESCRIPTOR);
            }
            switch (code) {
                case 1598968902:
                    reply.writeString(IOplusDynamicVsync.DESCRIPTOR);
                    return true;
                default:
                    switch (code) {
                        case 1:
                            int _arg0 = data.readInt();
                            String _arg1 = data.readString();
                            data.enforceNoDataAvail();
                            doAnimation(_arg0, _arg1);
                            return true;
                        case 2:
                            String _arg02 = data.readString();
                            int _arg12 = data.readInt();
                            data.enforceNoDataAvail();
                            flingEvent(_arg02, _arg12);
                            return true;
                        default:
                            return super.onTransact(code, data, reply, flags);
                    }
            }
        }

        /* loaded from: classes.dex */
        private static class Proxy implements IOplusDynamicVsync {
            private IBinder mRemote;

            Proxy(IBinder remote) {
                this.mRemote = remote;
            }

            @Override // android.os.IInterface
            public IBinder asBinder() {
                return this.mRemote;
            }

            public String getInterfaceDescriptor() {
                return IOplusDynamicVsync.DESCRIPTOR;
            }

            @Override // com.oplus.dynamicvsync.IOplusDynamicVsync
            public void doAnimation(int durationInMs, String detail) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(IOplusDynamicVsync.DESCRIPTOR);
                    _data.writeInt(durationInMs);
                    _data.writeString(detail);
                    this.mRemote.transact(1, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // com.oplus.dynamicvsync.IOplusDynamicVsync
            public void flingEvent(String pkgName, int durationInMs) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(IOplusDynamicVsync.DESCRIPTOR);
                    _data.writeString(pkgName);
                    _data.writeInt(durationInMs);
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
