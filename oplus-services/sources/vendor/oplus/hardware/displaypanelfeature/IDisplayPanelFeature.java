package vendor.oplus.hardware.displaypanelfeature;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import java.util.ArrayList;
import java.util.List;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
public interface IDisplayPanelFeature extends IInterface {
    public static final String DESCRIPTOR = "vendor$oplus$hardware$displaypanelfeature$IDisplayPanelFeature".replace('$', '.');
    public static final String HASH = "39afca22ac77253421ad681eeb2bae0c6ff62c13";
    public static final int VERSION = 1;

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
    public static class Default implements IDisplayPanelFeature {
        @Override // android.os.IInterface
        public IBinder asBinder() {
            return null;
        }

        @Override // vendor.oplus.hardware.displaypanelfeature.IDisplayPanelFeature
        public int getDisplayPanelFeatureValue(int i, int[] iArr) throws RemoteException {
            return 0;
        }

        @Override // vendor.oplus.hardware.displaypanelfeature.IDisplayPanelFeature
        public int getDisplayPanelInfo(int i, List<String> list) throws RemoteException {
            return 0;
        }

        @Override // vendor.oplus.hardware.displaypanelfeature.IDisplayPanelFeature
        public String getInterfaceHash() {
            return "";
        }

        @Override // vendor.oplus.hardware.displaypanelfeature.IDisplayPanelFeature
        public int getInterfaceVersion() {
            return 0;
        }

        @Override // vendor.oplus.hardware.displaypanelfeature.IDisplayPanelFeature
        public int setDisplayPanelFeatureValue(int i, int[] iArr) throws RemoteException {
            return 0;
        }
    }

    int getDisplayPanelFeatureValue(int i, int[] iArr) throws RemoteException;

    int getDisplayPanelInfo(int i, List<String> list) throws RemoteException;

    String getInterfaceHash() throws RemoteException;

    int getInterfaceVersion() throws RemoteException;

    int setDisplayPanelFeatureValue(int i, int[] iArr) throws RemoteException;

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
    public static abstract class Stub extends Binder implements IDisplayPanelFeature {
        static final int TRANSACTION_getDisplayPanelFeatureValue = 1;
        static final int TRANSACTION_getDisplayPanelInfo = 3;
        static final int TRANSACTION_getInterfaceHash = 16777214;
        static final int TRANSACTION_getInterfaceVersion = 16777215;
        static final int TRANSACTION_setDisplayPanelFeatureValue = 2;

        public static String getDefaultTransactionName(int i) {
            if (i == 1) {
                return "getDisplayPanelFeatureValue";
            }
            if (i == 2) {
                return "setDisplayPanelFeatureValue";
            }
            if (i == 3) {
                return "getDisplayPanelInfo";
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
            attachInterface(this, IDisplayPanelFeature.DESCRIPTOR);
        }

        public static IDisplayPanelFeature asInterface(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface queryLocalInterface = iBinder.queryLocalInterface(IDisplayPanelFeature.DESCRIPTOR);
            if (queryLocalInterface != null && (queryLocalInterface instanceof IDisplayPanelFeature)) {
                return (IDisplayPanelFeature) queryLocalInterface;
            }
            return new Proxy(iBinder);
        }

        public String getTransactionName(int i) {
            return getDefaultTransactionName(i);
        }

        @Override // android.os.Binder
        public boolean onTransact(int i, Parcel parcel, Parcel parcel2, int i2) throws RemoteException {
            String str = IDisplayPanelFeature.DESCRIPTOR;
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
                        int readInt2 = parcel.readInt();
                        int[] iArr = readInt2 < 0 ? null : new int[readInt2];
                        parcel.enforceNoDataAvail();
                        int displayPanelFeatureValue = getDisplayPanelFeatureValue(readInt, iArr);
                        parcel2.writeNoException();
                        parcel2.writeInt(displayPanelFeatureValue);
                        parcel2.writeIntArray(iArr);
                    } else if (i == 2) {
                        int readInt3 = parcel.readInt();
                        int[] createIntArray = parcel.createIntArray();
                        parcel.enforceNoDataAvail();
                        int displayPanelFeatureValue2 = setDisplayPanelFeatureValue(readInt3, createIntArray);
                        parcel2.writeNoException();
                        parcel2.writeInt(displayPanelFeatureValue2);
                    } else if (i == 3) {
                        int readInt4 = parcel.readInt();
                        ArrayList arrayList = new ArrayList();
                        parcel.enforceNoDataAvail();
                        int displayPanelInfo = getDisplayPanelInfo(readInt4, arrayList);
                        parcel2.writeNoException();
                        parcel2.writeInt(displayPanelInfo);
                        parcel2.writeStringList(arrayList);
                    } else {
                        return super.onTransact(i, parcel, parcel2, i2);
                    }
                    return true;
            }
        }

        /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
        private static class Proxy implements IDisplayPanelFeature {
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
                return IDisplayPanelFeature.DESCRIPTOR;
            }

            @Override // vendor.oplus.hardware.displaypanelfeature.IDisplayPanelFeature
            public int getDisplayPanelFeatureValue(int i, int[] iArr) throws RemoteException {
                Parcel obtain = Parcel.obtain(asBinder());
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(IDisplayPanelFeature.DESCRIPTOR);
                    obtain.writeInt(i);
                    obtain.writeInt(iArr.length);
                    if (!this.mRemote.transact(1, obtain, obtain2, 0)) {
                        throw new RemoteException("Method getDisplayPanelFeatureValue is unimplemented.");
                    }
                    obtain2.readException();
                    int readInt = obtain2.readInt();
                    obtain2.readIntArray(iArr);
                    return readInt;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // vendor.oplus.hardware.displaypanelfeature.IDisplayPanelFeature
            public int setDisplayPanelFeatureValue(int i, int[] iArr) throws RemoteException {
                Parcel obtain = Parcel.obtain(asBinder());
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(IDisplayPanelFeature.DESCRIPTOR);
                    obtain.writeInt(i);
                    obtain.writeIntArray(iArr);
                    if (!this.mRemote.transact(2, obtain, obtain2, 0)) {
                        throw new RemoteException("Method setDisplayPanelFeatureValue is unimplemented.");
                    }
                    obtain2.readException();
                    return obtain2.readInt();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // vendor.oplus.hardware.displaypanelfeature.IDisplayPanelFeature
            public int getDisplayPanelInfo(int i, List<String> list) throws RemoteException {
                Parcel obtain = Parcel.obtain(asBinder());
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(IDisplayPanelFeature.DESCRIPTOR);
                    obtain.writeInt(i);
                    if (!this.mRemote.transact(3, obtain, obtain2, 0)) {
                        throw new RemoteException("Method getDisplayPanelInfo is unimplemented.");
                    }
                    obtain2.readException();
                    int readInt = obtain2.readInt();
                    obtain2.readStringList(list);
                    return readInt;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // vendor.oplus.hardware.displaypanelfeature.IDisplayPanelFeature
            public int getInterfaceVersion() throws RemoteException {
                if (this.mCachedVersion == -1) {
                    Parcel obtain = Parcel.obtain(asBinder());
                    Parcel obtain2 = Parcel.obtain();
                    try {
                        obtain.writeInterfaceToken(IDisplayPanelFeature.DESCRIPTOR);
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

            @Override // vendor.oplus.hardware.displaypanelfeature.IDisplayPanelFeature
            public synchronized String getInterfaceHash() throws RemoteException {
                if ("-1".equals(this.mCachedHash)) {
                    Parcel obtain = Parcel.obtain(asBinder());
                    Parcel obtain2 = Parcel.obtain();
                    try {
                        obtain.writeInterfaceToken(IDisplayPanelFeature.DESCRIPTOR);
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
