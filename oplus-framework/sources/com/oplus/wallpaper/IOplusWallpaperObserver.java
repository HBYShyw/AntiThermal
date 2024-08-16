package com.oplus.wallpaper;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

/* loaded from: classes.dex */
public interface IOplusWallpaperObserver extends IInterface {
    public static final String DESCRIPTOR = "com.oplus.wallpaper.IOplusWallpaperObserver";

    void onWallpaperChanged(OplusWallpaperInfo oplusWallpaperInfo) throws RemoteException;

    void onWallpaperRemoved() throws RemoteException;

    /* loaded from: classes.dex */
    public static class Default implements IOplusWallpaperObserver {
        @Override // com.oplus.wallpaper.IOplusWallpaperObserver
        public void onWallpaperChanged(OplusWallpaperInfo info) throws RemoteException {
        }

        @Override // com.oplus.wallpaper.IOplusWallpaperObserver
        public void onWallpaperRemoved() throws RemoteException {
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return null;
        }
    }

    /* loaded from: classes.dex */
    public static abstract class Stub extends Binder implements IOplusWallpaperObserver {
        static final int TRANSACTION_onWallpaperChanged = 1;
        static final int TRANSACTION_onWallpaperRemoved = 2;

        public Stub() {
            attachInterface(this, IOplusWallpaperObserver.DESCRIPTOR);
        }

        public static IOplusWallpaperObserver asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(IOplusWallpaperObserver.DESCRIPTOR);
            if (iin != null && (iin instanceof IOplusWallpaperObserver)) {
                return (IOplusWallpaperObserver) iin;
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
                    return "onWallpaperChanged";
                case 2:
                    return "onWallpaperRemoved";
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
                data.enforceInterface(IOplusWallpaperObserver.DESCRIPTOR);
            }
            switch (code) {
                case 1598968902:
                    reply.writeString(IOplusWallpaperObserver.DESCRIPTOR);
                    return true;
                default:
                    switch (code) {
                        case 1:
                            OplusWallpaperInfo _arg0 = (OplusWallpaperInfo) data.readTypedObject(OplusWallpaperInfo.CREATOR);
                            data.enforceNoDataAvail();
                            onWallpaperChanged(_arg0);
                            return true;
                        case 2:
                            onWallpaperRemoved();
                            return true;
                        default:
                            return super.onTransact(code, data, reply, flags);
                    }
            }
        }

        /* loaded from: classes.dex */
        private static class Proxy implements IOplusWallpaperObserver {
            private IBinder mRemote;

            Proxy(IBinder remote) {
                this.mRemote = remote;
            }

            @Override // android.os.IInterface
            public IBinder asBinder() {
                return this.mRemote;
            }

            public String getInterfaceDescriptor() {
                return IOplusWallpaperObserver.DESCRIPTOR;
            }

            @Override // com.oplus.wallpaper.IOplusWallpaperObserver
            public void onWallpaperChanged(OplusWallpaperInfo info) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(IOplusWallpaperObserver.DESCRIPTOR);
                    _data.writeTypedObject(info, 0);
                    this.mRemote.transact(1, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // com.oplus.wallpaper.IOplusWallpaperObserver
            public void onWallpaperRemoved() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(IOplusWallpaperObserver.DESCRIPTOR);
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
