package android.hardware.power;

import android.hardware.power.IPowerHintSession;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public interface IPower extends IInterface {
    public static final String DESCRIPTOR = "android$hardware$power$IPower".replace('$', '.');
    public static final String HASH = "141ac3bb33bb4f524de020669f12599c18cdd67f";
    public static final int VERSION = 4;

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public static class Default implements IPower {
        @Override // android.os.IInterface
        public IBinder asBinder() {
            return null;
        }

        @Override // android.hardware.power.IPower
        public IPowerHintSession createHintSession(int i, int i2, int[] iArr, long j) throws RemoteException {
            return null;
        }

        @Override // android.hardware.power.IPower
        public long getHintSessionPreferredRate() throws RemoteException {
            return 0L;
        }

        @Override // android.hardware.power.IPower
        public String getInterfaceHash() {
            return "";
        }

        @Override // android.hardware.power.IPower
        public int getInterfaceVersion() {
            return 0;
        }

        @Override // android.hardware.power.IPower
        public boolean isBoostSupported(int i) throws RemoteException {
            return false;
        }

        @Override // android.hardware.power.IPower
        public boolean isModeSupported(int i) throws RemoteException {
            return false;
        }

        @Override // android.hardware.power.IPower
        public void setBoost(int i, int i2) throws RemoteException {
        }

        @Override // android.hardware.power.IPower
        public void setMode(int i, boolean z) throws RemoteException {
        }
    }

    IPowerHintSession createHintSession(int i, int i2, int[] iArr, long j) throws RemoteException;

    long getHintSessionPreferredRate() throws RemoteException;

    String getInterfaceHash() throws RemoteException;

    int getInterfaceVersion() throws RemoteException;

    boolean isBoostSupported(int i) throws RemoteException;

    boolean isModeSupported(int i) throws RemoteException;

    void setBoost(int i, int i2) throws RemoteException;

    void setMode(int i, boolean z) throws RemoteException;

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public static abstract class Stub extends Binder implements IPower {
        static final int TRANSACTION_createHintSession = 5;
        static final int TRANSACTION_getHintSessionPreferredRate = 6;
        static final int TRANSACTION_getInterfaceHash = 16777214;
        static final int TRANSACTION_getInterfaceVersion = 16777215;
        static final int TRANSACTION_isBoostSupported = 4;
        static final int TRANSACTION_isModeSupported = 2;
        static final int TRANSACTION_setBoost = 3;
        static final int TRANSACTION_setMode = 1;

        public static String getDefaultTransactionName(int i) {
            switch (i) {
                case 1:
                    return "setMode";
                case 2:
                    return "isModeSupported";
                case 3:
                    return "setBoost";
                case 4:
                    return "isBoostSupported";
                case 5:
                    return "createHintSession";
                case 6:
                    return "getHintSessionPreferredRate";
                default:
                    switch (i) {
                        case TRANSACTION_getInterfaceHash /* 16777214 */:
                            return "getInterfaceHash";
                        case 16777215:
                            return "getInterfaceVersion";
                        default:
                            return null;
                    }
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
            attachInterface(this, IPower.DESCRIPTOR);
        }

        public static IPower asInterface(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface queryLocalInterface = iBinder.queryLocalInterface(IPower.DESCRIPTOR);
            if (queryLocalInterface != null && (queryLocalInterface instanceof IPower)) {
                return (IPower) queryLocalInterface;
            }
            return new Proxy(iBinder);
        }

        public String getTransactionName(int i) {
            return getDefaultTransactionName(i);
        }

        @Override // android.os.Binder
        public boolean onTransact(int i, Parcel parcel, Parcel parcel2, int i2) throws RemoteException {
            String str = IPower.DESCRIPTOR;
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
                    switch (i) {
                        case 1:
                            int readInt = parcel.readInt();
                            boolean readBoolean = parcel.readBoolean();
                            parcel.enforceNoDataAvail();
                            setMode(readInt, readBoolean);
                            return true;
                        case 2:
                            int readInt2 = parcel.readInt();
                            parcel.enforceNoDataAvail();
                            boolean isModeSupported = isModeSupported(readInt2);
                            parcel2.writeNoException();
                            parcel2.writeBoolean(isModeSupported);
                            return true;
                        case 3:
                            int readInt3 = parcel.readInt();
                            int readInt4 = parcel.readInt();
                            parcel.enforceNoDataAvail();
                            setBoost(readInt3, readInt4);
                            return true;
                        case 4:
                            int readInt5 = parcel.readInt();
                            parcel.enforceNoDataAvail();
                            boolean isBoostSupported = isBoostSupported(readInt5);
                            parcel2.writeNoException();
                            parcel2.writeBoolean(isBoostSupported);
                            return true;
                        case 5:
                            int readInt6 = parcel.readInt();
                            int readInt7 = parcel.readInt();
                            int[] createIntArray = parcel.createIntArray();
                            long readLong = parcel.readLong();
                            parcel.enforceNoDataAvail();
                            IPowerHintSession createHintSession = createHintSession(readInt6, readInt7, createIntArray, readLong);
                            parcel2.writeNoException();
                            parcel2.writeStrongInterface(createHintSession);
                            return true;
                        case 6:
                            long hintSessionPreferredRate = getHintSessionPreferredRate();
                            parcel2.writeNoException();
                            parcel2.writeLong(hintSessionPreferredRate);
                            return true;
                        default:
                            return super.onTransact(i, parcel, parcel2, i2);
                    }
            }
        }

        /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
        private static class Proxy implements IPower {
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
                return IPower.DESCRIPTOR;
            }

            @Override // android.hardware.power.IPower
            public void setMode(int i, boolean z) throws RemoteException {
                Parcel obtain = Parcel.obtain(asBinder());
                try {
                    obtain.writeInterfaceToken(IPower.DESCRIPTOR);
                    obtain.writeInt(i);
                    obtain.writeBoolean(z);
                    if (this.mRemote.transact(1, obtain, null, 1)) {
                    } else {
                        throw new RemoteException("Method setMode is unimplemented.");
                    }
                } finally {
                    obtain.recycle();
                }
            }

            @Override // android.hardware.power.IPower
            public boolean isModeSupported(int i) throws RemoteException {
                Parcel obtain = Parcel.obtain(asBinder());
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(IPower.DESCRIPTOR);
                    obtain.writeInt(i);
                    if (!this.mRemote.transact(2, obtain, obtain2, 0)) {
                        throw new RemoteException("Method isModeSupported is unimplemented.");
                    }
                    obtain2.readException();
                    return obtain2.readBoolean();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // android.hardware.power.IPower
            public void setBoost(int i, int i2) throws RemoteException {
                Parcel obtain = Parcel.obtain(asBinder());
                try {
                    obtain.writeInterfaceToken(IPower.DESCRIPTOR);
                    obtain.writeInt(i);
                    obtain.writeInt(i2);
                    if (this.mRemote.transact(3, obtain, null, 1)) {
                    } else {
                        throw new RemoteException("Method setBoost is unimplemented.");
                    }
                } finally {
                    obtain.recycle();
                }
            }

            @Override // android.hardware.power.IPower
            public boolean isBoostSupported(int i) throws RemoteException {
                Parcel obtain = Parcel.obtain(asBinder());
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(IPower.DESCRIPTOR);
                    obtain.writeInt(i);
                    if (!this.mRemote.transact(4, obtain, obtain2, 0)) {
                        throw new RemoteException("Method isBoostSupported is unimplemented.");
                    }
                    obtain2.readException();
                    return obtain2.readBoolean();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // android.hardware.power.IPower
            public IPowerHintSession createHintSession(int i, int i2, int[] iArr, long j) throws RemoteException {
                Parcel obtain = Parcel.obtain(asBinder());
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(IPower.DESCRIPTOR);
                    obtain.writeInt(i);
                    obtain.writeInt(i2);
                    obtain.writeIntArray(iArr);
                    obtain.writeLong(j);
                    if (!this.mRemote.transact(5, obtain, obtain2, 0)) {
                        throw new RemoteException("Method createHintSession is unimplemented.");
                    }
                    obtain2.readException();
                    return IPowerHintSession.Stub.asInterface(obtain2.readStrongBinder());
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // android.hardware.power.IPower
            public long getHintSessionPreferredRate() throws RemoteException {
                Parcel obtain = Parcel.obtain(asBinder());
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(IPower.DESCRIPTOR);
                    if (!this.mRemote.transact(6, obtain, obtain2, 0)) {
                        throw new RemoteException("Method getHintSessionPreferredRate is unimplemented.");
                    }
                    obtain2.readException();
                    return obtain2.readLong();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // android.hardware.power.IPower
            public int getInterfaceVersion() throws RemoteException {
                if (this.mCachedVersion == -1) {
                    Parcel obtain = Parcel.obtain(asBinder());
                    Parcel obtain2 = Parcel.obtain();
                    try {
                        obtain.writeInterfaceToken(IPower.DESCRIPTOR);
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

            @Override // android.hardware.power.IPower
            public synchronized String getInterfaceHash() throws RemoteException {
                if ("-1".equals(this.mCachedHash)) {
                    Parcel obtain = Parcel.obtain(asBinder());
                    Parcel obtain2 = Parcel.obtain();
                    try {
                        obtain.writeInterfaceToken(IPower.DESCRIPTOR);
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
