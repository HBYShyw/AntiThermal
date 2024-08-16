package vendor.pixelworks.hardware.display;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
public interface IIrisCallback extends IInterface {
    public static final String DESCRIPTOR = "vendor$pixelworks$hardware$display$IIrisCallback".replace('$', '.');
    public static final String HASH = "02c8c5526cbde39f502b3bf8cccaf196c81de25f";
    public static final int VERSION = 1;

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
    public static class Default implements IIrisCallback {
        @Override // android.os.IInterface
        public IBinder asBinder() {
            return null;
        }

        @Override // vendor.pixelworks.hardware.display.IIrisCallback
        public String getInterfaceHash() {
            return "";
        }

        @Override // vendor.pixelworks.hardware.display.IIrisCallback
        public int getInterfaceVersion() {
            return 0;
        }

        @Override // vendor.pixelworks.hardware.display.IIrisCallback
        public int onCalibratePatternChanged(long j, int i) throws RemoteException {
            return 0;
        }

        @Override // vendor.pixelworks.hardware.display.IIrisCallback
        public ContentSamples onContentSamplingRequested(long j, int i, long j2) throws RemoteException {
            return null;
        }

        @Override // vendor.pixelworks.hardware.display.IIrisCallback
        public void onDisplayPowerChanged(long j, int i) throws RemoteException {
        }

        @Override // vendor.pixelworks.hardware.display.IIrisCallback
        public void onFeatureChanged(int i, int[] iArr) throws RemoteException {
        }

        @Override // vendor.pixelworks.hardware.display.IIrisCallback
        public void onRefreshRequested(long j) throws RemoteException {
        }
    }

    String getInterfaceHash() throws RemoteException;

    int getInterfaceVersion() throws RemoteException;

    int onCalibratePatternChanged(long j, int i) throws RemoteException;

    ContentSamples onContentSamplingRequested(long j, int i, long j2) throws RemoteException;

    void onDisplayPowerChanged(long j, int i) throws RemoteException;

    void onFeatureChanged(int i, int[] iArr) throws RemoteException;

    void onRefreshRequested(long j) throws RemoteException;

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
    public static abstract class Stub extends Binder implements IIrisCallback {
        static final int TRANSACTION_getInterfaceHash = 16777214;
        static final int TRANSACTION_getInterfaceVersion = 16777215;
        static final int TRANSACTION_onCalibratePatternChanged = 3;
        static final int TRANSACTION_onContentSamplingRequested = 5;
        static final int TRANSACTION_onDisplayPowerChanged = 4;
        static final int TRANSACTION_onFeatureChanged = 1;
        static final int TRANSACTION_onRefreshRequested = 2;

        public static String getDefaultTransactionName(int i) {
            if (i == 1) {
                return "onFeatureChanged";
            }
            if (i == 2) {
                return "onRefreshRequested";
            }
            if (i == 3) {
                return "onCalibratePatternChanged";
            }
            if (i == 4) {
                return "onDisplayPowerChanged";
            }
            if (i == 5) {
                return "onContentSamplingRequested";
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
            attachInterface(this, IIrisCallback.DESCRIPTOR);
        }

        public static IIrisCallback asInterface(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface queryLocalInterface = iBinder.queryLocalInterface(IIrisCallback.DESCRIPTOR);
            if (queryLocalInterface != null && (queryLocalInterface instanceof IIrisCallback)) {
                return (IIrisCallback) queryLocalInterface;
            }
            return new Proxy(iBinder);
        }

        public String getTransactionName(int i) {
            return getDefaultTransactionName(i);
        }

        @Override // android.os.Binder
        public boolean onTransact(int i, Parcel parcel, Parcel parcel2, int i2) throws RemoteException {
            String str = IIrisCallback.DESCRIPTOR;
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
                        int readInt = parcel.readInt();
                        int[] createIntArray = parcel.createIntArray();
                        parcel.enforceNoDataAvail();
                        onFeatureChanged(readInt, createIntArray);
                        parcel2.writeNoException();
                    } else if (i == 2) {
                        long readLong = parcel.readLong();
                        parcel.enforceNoDataAvail();
                        onRefreshRequested(readLong);
                        parcel2.writeNoException();
                    } else if (i == 3) {
                        long readLong2 = parcel.readLong();
                        int readInt2 = parcel.readInt();
                        parcel.enforceNoDataAvail();
                        int onCalibratePatternChanged = onCalibratePatternChanged(readLong2, readInt2);
                        parcel2.writeNoException();
                        parcel2.writeInt(onCalibratePatternChanged);
                    } else if (i == 4) {
                        long readLong3 = parcel.readLong();
                        int readInt3 = parcel.readInt();
                        parcel.enforceNoDataAvail();
                        onDisplayPowerChanged(readLong3, readInt3);
                        parcel2.writeNoException();
                    } else if (i == 5) {
                        long readLong4 = parcel.readLong();
                        int readInt4 = parcel.readInt();
                        long readLong5 = parcel.readLong();
                        parcel.enforceNoDataAvail();
                        ContentSamples onContentSamplingRequested = onContentSamplingRequested(readLong4, readInt4, readLong5);
                        parcel2.writeNoException();
                        parcel2.writeTypedObject(onContentSamplingRequested, 1);
                    } else {
                        return super.onTransact(i, parcel, parcel2, i2);
                    }
                    return true;
            }
        }

        /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
        private static class Proxy implements IIrisCallback {
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
                return IIrisCallback.DESCRIPTOR;
            }

            @Override // vendor.pixelworks.hardware.display.IIrisCallback
            public void onFeatureChanged(int i, int[] iArr) throws RemoteException {
                Parcel obtain = Parcel.obtain(asBinder());
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(IIrisCallback.DESCRIPTOR);
                    obtain.writeInt(i);
                    obtain.writeIntArray(iArr);
                    if (!this.mRemote.transact(1, obtain, obtain2, 0)) {
                        throw new RemoteException("Method onFeatureChanged is unimplemented.");
                    }
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // vendor.pixelworks.hardware.display.IIrisCallback
            public void onRefreshRequested(long j) throws RemoteException {
                Parcel obtain = Parcel.obtain(asBinder());
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(IIrisCallback.DESCRIPTOR);
                    obtain.writeLong(j);
                    if (!this.mRemote.transact(2, obtain, obtain2, 0)) {
                        throw new RemoteException("Method onRefreshRequested is unimplemented.");
                    }
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // vendor.pixelworks.hardware.display.IIrisCallback
            public int onCalibratePatternChanged(long j, int i) throws RemoteException {
                Parcel obtain = Parcel.obtain(asBinder());
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(IIrisCallback.DESCRIPTOR);
                    obtain.writeLong(j);
                    obtain.writeInt(i);
                    if (!this.mRemote.transact(3, obtain, obtain2, 0)) {
                        throw new RemoteException("Method onCalibratePatternChanged is unimplemented.");
                    }
                    obtain2.readException();
                    return obtain2.readInt();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // vendor.pixelworks.hardware.display.IIrisCallback
            public void onDisplayPowerChanged(long j, int i) throws RemoteException {
                Parcel obtain = Parcel.obtain(asBinder());
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(IIrisCallback.DESCRIPTOR);
                    obtain.writeLong(j);
                    obtain.writeInt(i);
                    if (!this.mRemote.transact(4, obtain, obtain2, 0)) {
                        throw new RemoteException("Method onDisplayPowerChanged is unimplemented.");
                    }
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // vendor.pixelworks.hardware.display.IIrisCallback
            public ContentSamples onContentSamplingRequested(long j, int i, long j2) throws RemoteException {
                Parcel obtain = Parcel.obtain(asBinder());
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(IIrisCallback.DESCRIPTOR);
                    obtain.writeLong(j);
                    obtain.writeInt(i);
                    obtain.writeLong(j2);
                    if (!this.mRemote.transact(5, obtain, obtain2, 0)) {
                        throw new RemoteException("Method onContentSamplingRequested is unimplemented.");
                    }
                    obtain2.readException();
                    return (ContentSamples) obtain2.readTypedObject(ContentSamples.CREATOR);
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // vendor.pixelworks.hardware.display.IIrisCallback
            public int getInterfaceVersion() throws RemoteException {
                if (this.mCachedVersion == -1) {
                    Parcel obtain = Parcel.obtain(asBinder());
                    Parcel obtain2 = Parcel.obtain();
                    try {
                        obtain.writeInterfaceToken(IIrisCallback.DESCRIPTOR);
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

            @Override // vendor.pixelworks.hardware.display.IIrisCallback
            public synchronized String getInterfaceHash() throws RemoteException {
                if ("-1".equals(this.mCachedHash)) {
                    Parcel obtain = Parcel.obtain(asBinder());
                    Parcel obtain2 = Parcel.obtain();
                    try {
                        obtain.writeInterfaceToken(IIrisCallback.DESCRIPTOR);
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
