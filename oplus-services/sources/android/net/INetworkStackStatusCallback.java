package android.net;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public interface INetworkStackStatusCallback extends IInterface {
    public static final String DESCRIPTOR = "android$net$INetworkStackStatusCallback".replace('$', '.');
    public static final String HASH = "4d26968d0f6cb11c9bb669a3f8ebc7a1c39f9391";
    public static final int VERSION = 18;

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public static class Default implements INetworkStackStatusCallback {
        @Override // android.os.IInterface
        public IBinder asBinder() {
            return null;
        }

        @Override // android.net.INetworkStackStatusCallback
        public String getInterfaceHash() {
            return "";
        }

        @Override // android.net.INetworkStackStatusCallback
        public int getInterfaceVersion() {
            return 0;
        }

        @Override // android.net.INetworkStackStatusCallback
        public void onStatusAvailable(int i) throws RemoteException {
        }
    }

    String getInterfaceHash() throws RemoteException;

    int getInterfaceVersion() throws RemoteException;

    void onStatusAvailable(int i) throws RemoteException;

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public static abstract class Stub extends Binder implements INetworkStackStatusCallback {
        static final int TRANSACTION_getInterfaceHash = 16777214;
        static final int TRANSACTION_getInterfaceVersion = 16777215;
        static final int TRANSACTION_onStatusAvailable = 1;

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return this;
        }

        public Stub() {
            attachInterface(this, INetworkStackStatusCallback.DESCRIPTOR);
        }

        public static INetworkStackStatusCallback asInterface(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface queryLocalInterface = iBinder.queryLocalInterface(INetworkStackStatusCallback.DESCRIPTOR);
            if (queryLocalInterface != null && (queryLocalInterface instanceof INetworkStackStatusCallback)) {
                return (INetworkStackStatusCallback) queryLocalInterface;
            }
            return new Proxy(iBinder);
        }

        @Override // android.os.Binder
        public boolean onTransact(int i, Parcel parcel, Parcel parcel2, int i2) throws RemoteException {
            String str = INetworkStackStatusCallback.DESCRIPTOR;
            if (i >= 1 && i <= 16777215) {
                parcel.enforceInterface(str);
            }
            switch (i) {
                case TRANSACTION_getInterfaceHash /* 16777214 */:
                    parcel2.writeNoException();
                    parcel2.writeString(getInterfaceHash());
                    return true;
                case 16777215:
                    parcel2.writeNoException();
                    parcel2.writeInt(getInterfaceVersion());
                    return true;
                case 1598968902:
                    parcel2.writeString(str);
                    return true;
                default:
                    if (i == 1) {
                        onStatusAvailable(parcel.readInt());
                        return true;
                    }
                    return super.onTransact(i, parcel, parcel2, i2);
            }
        }

        /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
        private static class Proxy implements INetworkStackStatusCallback {
            private IBinder mRemote;
            private int mCachedVersion = -1;
            private String mCachedHash = "-1";

            Proxy(IBinder iBinder) {
                this.mRemote = iBinder;
            }

            @Override // android.os.IInterface
            public IBinder asBinder() {
                return this.mRemote;
            }

            public String getInterfaceDescriptor() {
                return INetworkStackStatusCallback.DESCRIPTOR;
            }

            @Override // android.net.INetworkStackStatusCallback
            public void onStatusAvailable(int i) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(INetworkStackStatusCallback.DESCRIPTOR);
                    obtain.writeInt(i);
                    if (this.mRemote.transact(1, obtain, null, 1)) {
                    } else {
                        throw new RemoteException("Method onStatusAvailable is unimplemented.");
                    }
                } finally {
                    obtain.recycle();
                }
            }

            @Override // android.net.INetworkStackStatusCallback
            public int getInterfaceVersion() throws RemoteException {
                if (this.mCachedVersion == -1) {
                    Parcel obtain = Parcel.obtain();
                    Parcel obtain2 = Parcel.obtain();
                    try {
                        obtain.writeInterfaceToken(INetworkStackStatusCallback.DESCRIPTOR);
                        this.mRemote.transact(16777215, obtain, obtain2, 0);
                        obtain2.readException();
                        this.mCachedVersion = obtain2.readInt();
                    } finally {
                        obtain2.recycle();
                        obtain.recycle();
                    }
                }
                return this.mCachedVersion;
            }

            @Override // android.net.INetworkStackStatusCallback
            public synchronized String getInterfaceHash() throws RemoteException {
                if ("-1".equals(this.mCachedHash)) {
                    Parcel obtain = Parcel.obtain();
                    Parcel obtain2 = Parcel.obtain();
                    try {
                        obtain.writeInterfaceToken(INetworkStackStatusCallback.DESCRIPTOR);
                        this.mRemote.transact(Stub.TRANSACTION_getInterfaceHash, obtain, obtain2, 0);
                        obtain2.readException();
                        this.mCachedHash = obtain2.readString();
                        obtain2.recycle();
                        obtain.recycle();
                    } catch (Throwable th) {
                        obtain2.recycle();
                        obtain.recycle();
                        throw th;
                    }
                }
                return this.mCachedHash;
            }
        }
    }
}
