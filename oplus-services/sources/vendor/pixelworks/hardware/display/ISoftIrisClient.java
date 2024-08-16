package vendor.pixelworks.hardware.display;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
public interface ISoftIrisClient extends IInterface {
    public static final String DESCRIPTOR = "vendor$pixelworks$hardware$display$ISoftIrisClient".replace('$', '.');
    public static final String HASH = "02c8c5526cbde39f502b3bf8cccaf196c81de25f";
    public static final int VERSION = 1;

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
    public static class Default implements ISoftIrisClient {
        @Override // android.os.IInterface
        public IBinder asBinder() {
            return null;
        }

        @Override // vendor.pixelworks.hardware.display.ISoftIrisClient
        public int[] getConfig(long j, int i, int[] iArr) throws RemoteException {
            return null;
        }

        @Override // vendor.pixelworks.hardware.display.ISoftIrisClient
        public String getInterfaceHash() {
            return "";
        }

        @Override // vendor.pixelworks.hardware.display.ISoftIrisClient
        public int getInterfaceVersion() {
            return 0;
        }

        @Override // vendor.pixelworks.hardware.display.ISoftIrisClient
        public String setBatchConfig(int i, String str) throws RemoteException {
            return null;
        }

        @Override // vendor.pixelworks.hardware.display.ISoftIrisClient
        public int setConfig(long j, int i, int[] iArr) throws RemoteException {
            return 0;
        }
    }

    int[] getConfig(long j, int i, int[] iArr) throws RemoteException;

    String getInterfaceHash() throws RemoteException;

    int getInterfaceVersion() throws RemoteException;

    String setBatchConfig(int i, String str) throws RemoteException;

    int setConfig(long j, int i, int[] iArr) throws RemoteException;

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
    public static abstract class Stub extends Binder implements ISoftIrisClient {
        static final int TRANSACTION_getConfig = 2;
        static final int TRANSACTION_getInterfaceHash = 16777214;
        static final int TRANSACTION_getInterfaceVersion = 16777215;
        static final int TRANSACTION_setBatchConfig = 3;
        static final int TRANSACTION_setConfig = 1;

        public static String getDefaultTransactionName(int i) {
            if (i == 1) {
                return "setConfig";
            }
            if (i == 2) {
                return "getConfig";
            }
            if (i == 3) {
                return "setBatchConfig";
            }
            switch (i) {
                case TRANSACTION_getInterfaceHash /* 16777214 */:
                    return "getInterfaceHash";
                case TRANSACTION_getInterfaceVersion /* 16777215 */:
                    return "getInterfaceVersion";
                default:
                    return null;
            }
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return this;
        }

        public int getMaxTransactionId() {
            return TRANSACTION_getInterfaceHash;
        }

        public Stub() {
            markVintfStability();
            attachInterface(this, ISoftIrisClient.DESCRIPTOR);
        }

        public static ISoftIrisClient asInterface(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface queryLocalInterface = iBinder.queryLocalInterface(ISoftIrisClient.DESCRIPTOR);
            if (queryLocalInterface != null && (queryLocalInterface instanceof ISoftIrisClient)) {
                return (ISoftIrisClient) queryLocalInterface;
            }
            return new Proxy(iBinder);
        }

        public String getTransactionName(int i) {
            return getDefaultTransactionName(i);
        }

        @Override // android.os.Binder
        public boolean onTransact(int i, Parcel parcel, Parcel parcel2, int i2) throws RemoteException {
            String str = ISoftIrisClient.DESCRIPTOR;
            if (i >= 1 && i <= TRANSACTION_getInterfaceVersion) {
                parcel.enforceInterface(str);
            }
            switch (i) {
                case TRANSACTION_getInterfaceHash /* 16777214 */:
                    parcel2.writeNoException();
                    parcel2.writeString(getInterfaceHash());
                    return true;
                case TRANSACTION_getInterfaceVersion /* 16777215 */:
                    parcel2.writeNoException();
                    parcel2.writeInt(getInterfaceVersion());
                    return true;
                case 1598968902:
                    parcel2.writeString(str);
                    return true;
                default:
                    if (i == 1) {
                        long readLong = parcel.readLong();
                        int readInt = parcel.readInt();
                        int[] createIntArray = parcel.createIntArray();
                        parcel.enforceNoDataAvail();
                        int config = setConfig(readLong, readInt, createIntArray);
                        parcel2.writeNoException();
                        parcel2.writeInt(config);
                    } else if (i == 2) {
                        long readLong2 = parcel.readLong();
                        int readInt2 = parcel.readInt();
                        int[] createIntArray2 = parcel.createIntArray();
                        parcel.enforceNoDataAvail();
                        int[] config2 = getConfig(readLong2, readInt2, createIntArray2);
                        parcel2.writeNoException();
                        parcel2.writeIntArray(config2);
                    } else if (i == 3) {
                        int readInt3 = parcel.readInt();
                        String readString = parcel.readString();
                        parcel.enforceNoDataAvail();
                        String batchConfig = setBatchConfig(readInt3, readString);
                        parcel2.writeNoException();
                        parcel2.writeString(batchConfig);
                    } else {
                        return super.onTransact(i, parcel, parcel2, i2);
                    }
                    return true;
            }
        }

        /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
        private static class Proxy implements ISoftIrisClient {
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
                return ISoftIrisClient.DESCRIPTOR;
            }

            @Override // vendor.pixelworks.hardware.display.ISoftIrisClient
            public int setConfig(long j, int i, int[] iArr) throws RemoteException {
                Parcel obtain = Parcel.obtain(asBinder());
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(ISoftIrisClient.DESCRIPTOR);
                    obtain.writeLong(j);
                    obtain.writeInt(i);
                    obtain.writeIntArray(iArr);
                    if (!this.mRemote.transact(1, obtain, obtain2, 0)) {
                        throw new RemoteException("Method setConfig is unimplemented.");
                    }
                    obtain2.readException();
                    return obtain2.readInt();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // vendor.pixelworks.hardware.display.ISoftIrisClient
            public int[] getConfig(long j, int i, int[] iArr) throws RemoteException {
                Parcel obtain = Parcel.obtain(asBinder());
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(ISoftIrisClient.DESCRIPTOR);
                    obtain.writeLong(j);
                    obtain.writeInt(i);
                    obtain.writeIntArray(iArr);
                    if (!this.mRemote.transact(2, obtain, obtain2, 0)) {
                        throw new RemoteException("Method getConfig is unimplemented.");
                    }
                    obtain2.readException();
                    return obtain2.createIntArray();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // vendor.pixelworks.hardware.display.ISoftIrisClient
            public String setBatchConfig(int i, String str) throws RemoteException {
                Parcel obtain = Parcel.obtain(asBinder());
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(ISoftIrisClient.DESCRIPTOR);
                    obtain.writeInt(i);
                    obtain.writeString(str);
                    if (!this.mRemote.transact(3, obtain, obtain2, 0)) {
                        throw new RemoteException("Method setBatchConfig is unimplemented.");
                    }
                    obtain2.readException();
                    return obtain2.readString();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // vendor.pixelworks.hardware.display.ISoftIrisClient
            public int getInterfaceVersion() throws RemoteException {
                if (this.mCachedVersion == -1) {
                    Parcel obtain = Parcel.obtain(asBinder());
                    Parcel obtain2 = Parcel.obtain();
                    try {
                        obtain.writeInterfaceToken(ISoftIrisClient.DESCRIPTOR);
                        this.mRemote.transact(Stub.TRANSACTION_getInterfaceVersion, obtain, obtain2, 0);
                        obtain2.readException();
                        this.mCachedVersion = obtain2.readInt();
                    } finally {
                        obtain2.recycle();
                        obtain.recycle();
                    }
                }
                return this.mCachedVersion;
            }

            @Override // vendor.pixelworks.hardware.display.ISoftIrisClient
            public synchronized String getInterfaceHash() throws RemoteException {
                if ("-1".equals(this.mCachedHash)) {
                    Parcel obtain = Parcel.obtain(asBinder());
                    Parcel obtain2 = Parcel.obtain();
                    try {
                        obtain.writeInterfaceToken(ISoftIrisClient.DESCRIPTOR);
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
