package com.android.server.profcollect;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public interface IProviderStatusCallback extends IInterface {
    public static final String DESCRIPTOR = "com.android.server.profcollect.IProviderStatusCallback";

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public static class Default implements IProviderStatusCallback {
        @Override // android.os.IInterface
        public IBinder asBinder() {
            return null;
        }

        @Override // com.android.server.profcollect.IProviderStatusCallback
        public void onProviderReady() throws RemoteException {
        }
    }

    void onProviderReady() throws RemoteException;

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public static abstract class Stub extends Binder implements IProviderStatusCallback {
        static final int TRANSACTION_onProviderReady = 1;

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return this;
        }

        public Stub() {
            attachInterface(this, IProviderStatusCallback.DESCRIPTOR);
        }

        public static IProviderStatusCallback asInterface(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface queryLocalInterface = iBinder.queryLocalInterface(IProviderStatusCallback.DESCRIPTOR);
            if (queryLocalInterface != null && (queryLocalInterface instanceof IProviderStatusCallback)) {
                return (IProviderStatusCallback) queryLocalInterface;
            }
            return new Proxy(iBinder);
        }

        @Override // android.os.Binder
        public boolean onTransact(int i, Parcel parcel, Parcel parcel2, int i2) throws RemoteException {
            if (i >= 1 && i <= 16777215) {
                parcel.enforceInterface(IProviderStatusCallback.DESCRIPTOR);
            }
            if (i == 1598968902) {
                parcel2.writeString(IProviderStatusCallback.DESCRIPTOR);
                return true;
            }
            if (i == 1) {
                onProviderReady();
                return true;
            }
            return super.onTransact(i, parcel, parcel2, i2);
        }

        /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
        private static class Proxy implements IProviderStatusCallback {
            private IBinder mRemote;

            public String getInterfaceDescriptor() {
                return IProviderStatusCallback.DESCRIPTOR;
            }

            Proxy(IBinder iBinder) {
                this.mRemote = iBinder;
            }

            @Override // android.os.IInterface
            public IBinder asBinder() {
                return this.mRemote;
            }

            @Override // com.android.server.profcollect.IProviderStatusCallback
            public void onProviderReady() throws RemoteException {
                Parcel obtain = Parcel.obtain(asBinder());
                try {
                    obtain.writeInterfaceToken(IProviderStatusCallback.DESCRIPTOR);
                    this.mRemote.transact(1, obtain, null, 1);
                } finally {
                    obtain.recycle();
                }
            }
        }
    }
}
