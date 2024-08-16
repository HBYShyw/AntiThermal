package android.hardware.boot;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public interface IBootControl extends IInterface {
    public static final int COMMAND_FAILED = -2;
    public static final String DESCRIPTOR = "android$hardware$boot$IBootControl".replace('$', '.');
    public static final String HASH = "2400346954240a5de495a1debc81429dd012d7b7";
    public static final int INVALID_SLOT = -1;
    public static final int VERSION = 1;

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public static class Default implements IBootControl {
        @Override // android.os.IInterface
        public IBinder asBinder() {
            return null;
        }

        @Override // android.hardware.boot.IBootControl
        public int getActiveBootSlot() throws RemoteException {
            return 0;
        }

        @Override // android.hardware.boot.IBootControl
        public int getCurrentSlot() throws RemoteException {
            return 0;
        }

        @Override // android.hardware.boot.IBootControl
        public String getInterfaceHash() {
            return "";
        }

        @Override // android.hardware.boot.IBootControl
        public int getInterfaceVersion() {
            return 0;
        }

        @Override // android.hardware.boot.IBootControl
        public int getNumberSlots() throws RemoteException {
            return 0;
        }

        @Override // android.hardware.boot.IBootControl
        public int getSnapshotMergeStatus() throws RemoteException {
            return 0;
        }

        @Override // android.hardware.boot.IBootControl
        public String getSuffix(int i) throws RemoteException {
            return null;
        }

        @Override // android.hardware.boot.IBootControl
        public boolean isSlotBootable(int i) throws RemoteException {
            return false;
        }

        @Override // android.hardware.boot.IBootControl
        public boolean isSlotMarkedSuccessful(int i) throws RemoteException {
            return false;
        }

        @Override // android.hardware.boot.IBootControl
        public void markBootSuccessful() throws RemoteException {
        }

        @Override // android.hardware.boot.IBootControl
        public void setActiveBootSlot(int i) throws RemoteException {
        }

        @Override // android.hardware.boot.IBootControl
        public void setSlotAsUnbootable(int i) throws RemoteException {
        }

        @Override // android.hardware.boot.IBootControl
        public void setSnapshotMergeStatus(int i) throws RemoteException {
        }
    }

    int getActiveBootSlot() throws RemoteException;

    int getCurrentSlot() throws RemoteException;

    String getInterfaceHash() throws RemoteException;

    int getInterfaceVersion() throws RemoteException;

    int getNumberSlots() throws RemoteException;

    int getSnapshotMergeStatus() throws RemoteException;

    String getSuffix(int i) throws RemoteException;

    boolean isSlotBootable(int i) throws RemoteException;

    boolean isSlotMarkedSuccessful(int i) throws RemoteException;

    void markBootSuccessful() throws RemoteException;

    void setActiveBootSlot(int i) throws RemoteException;

    void setSlotAsUnbootable(int i) throws RemoteException;

    void setSnapshotMergeStatus(int i) throws RemoteException;

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public static abstract class Stub extends Binder implements IBootControl {
        static final int TRANSACTION_getActiveBootSlot = 1;
        static final int TRANSACTION_getCurrentSlot = 2;
        static final int TRANSACTION_getInterfaceHash = 16777214;
        static final int TRANSACTION_getInterfaceVersion = 16777215;
        static final int TRANSACTION_getNumberSlots = 3;
        static final int TRANSACTION_getSnapshotMergeStatus = 4;
        static final int TRANSACTION_getSuffix = 5;
        static final int TRANSACTION_isSlotBootable = 6;
        static final int TRANSACTION_isSlotMarkedSuccessful = 7;
        static final int TRANSACTION_markBootSuccessful = 8;
        static final int TRANSACTION_setActiveBootSlot = 9;
        static final int TRANSACTION_setSlotAsUnbootable = 10;
        static final int TRANSACTION_setSnapshotMergeStatus = 11;

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return this;
        }

        public Stub() {
            markVintfStability();
            attachInterface(this, IBootControl.DESCRIPTOR);
        }

        public static IBootControl asInterface(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface queryLocalInterface = iBinder.queryLocalInterface(IBootControl.DESCRIPTOR);
            if (queryLocalInterface != null && (queryLocalInterface instanceof IBootControl)) {
                return (IBootControl) queryLocalInterface;
            }
            return new Proxy(iBinder);
        }

        @Override // android.os.Binder
        public boolean onTransact(int i, Parcel parcel, Parcel parcel2, int i2) throws RemoteException {
            String str = IBootControl.DESCRIPTOR;
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
                            int activeBootSlot = getActiveBootSlot();
                            parcel2.writeNoException();
                            parcel2.writeInt(activeBootSlot);
                            return true;
                        case 2:
                            int currentSlot = getCurrentSlot();
                            parcel2.writeNoException();
                            parcel2.writeInt(currentSlot);
                            return true;
                        case 3:
                            int numberSlots = getNumberSlots();
                            parcel2.writeNoException();
                            parcel2.writeInt(numberSlots);
                            return true;
                        case 4:
                            int snapshotMergeStatus = getSnapshotMergeStatus();
                            parcel2.writeNoException();
                            parcel2.writeInt(snapshotMergeStatus);
                            return true;
                        case 5:
                            int readInt = parcel.readInt();
                            parcel.enforceNoDataAvail();
                            String suffix = getSuffix(readInt);
                            parcel2.writeNoException();
                            parcel2.writeString(suffix);
                            return true;
                        case 6:
                            int readInt2 = parcel.readInt();
                            parcel.enforceNoDataAvail();
                            boolean isSlotBootable = isSlotBootable(readInt2);
                            parcel2.writeNoException();
                            parcel2.writeBoolean(isSlotBootable);
                            return true;
                        case 7:
                            int readInt3 = parcel.readInt();
                            parcel.enforceNoDataAvail();
                            boolean isSlotMarkedSuccessful = isSlotMarkedSuccessful(readInt3);
                            parcel2.writeNoException();
                            parcel2.writeBoolean(isSlotMarkedSuccessful);
                            return true;
                        case 8:
                            markBootSuccessful();
                            parcel2.writeNoException();
                            return true;
                        case 9:
                            int readInt4 = parcel.readInt();
                            parcel.enforceNoDataAvail();
                            setActiveBootSlot(readInt4);
                            parcel2.writeNoException();
                            return true;
                        case 10:
                            int readInt5 = parcel.readInt();
                            parcel.enforceNoDataAvail();
                            setSlotAsUnbootable(readInt5);
                            parcel2.writeNoException();
                            return true;
                        case 11:
                            int readInt6 = parcel.readInt();
                            parcel.enforceNoDataAvail();
                            setSnapshotMergeStatus(readInt6);
                            parcel2.writeNoException();
                            return true;
                        default:
                            return super.onTransact(i, parcel, parcel2, i2);
                    }
            }
        }

        /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
        private static class Proxy implements IBootControl {
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
                return IBootControl.DESCRIPTOR;
            }

            @Override // android.hardware.boot.IBootControl
            public int getActiveBootSlot() throws RemoteException {
                Parcel obtain = Parcel.obtain(asBinder());
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(IBootControl.DESCRIPTOR);
                    if (!this.mRemote.transact(1, obtain, obtain2, 0)) {
                        throw new RemoteException("Method getActiveBootSlot is unimplemented.");
                    }
                    obtain2.readException();
                    return obtain2.readInt();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // android.hardware.boot.IBootControl
            public int getCurrentSlot() throws RemoteException {
                Parcel obtain = Parcel.obtain(asBinder());
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(IBootControl.DESCRIPTOR);
                    if (!this.mRemote.transact(2, obtain, obtain2, 0)) {
                        throw new RemoteException("Method getCurrentSlot is unimplemented.");
                    }
                    obtain2.readException();
                    return obtain2.readInt();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // android.hardware.boot.IBootControl
            public int getNumberSlots() throws RemoteException {
                Parcel obtain = Parcel.obtain(asBinder());
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(IBootControl.DESCRIPTOR);
                    if (!this.mRemote.transact(3, obtain, obtain2, 0)) {
                        throw new RemoteException("Method getNumberSlots is unimplemented.");
                    }
                    obtain2.readException();
                    return obtain2.readInt();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // android.hardware.boot.IBootControl
            public int getSnapshotMergeStatus() throws RemoteException {
                Parcel obtain = Parcel.obtain(asBinder());
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(IBootControl.DESCRIPTOR);
                    if (!this.mRemote.transact(4, obtain, obtain2, 0)) {
                        throw new RemoteException("Method getSnapshotMergeStatus is unimplemented.");
                    }
                    obtain2.readException();
                    return obtain2.readInt();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // android.hardware.boot.IBootControl
            public String getSuffix(int i) throws RemoteException {
                Parcel obtain = Parcel.obtain(asBinder());
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(IBootControl.DESCRIPTOR);
                    obtain.writeInt(i);
                    if (!this.mRemote.transact(5, obtain, obtain2, 0)) {
                        throw new RemoteException("Method getSuffix is unimplemented.");
                    }
                    obtain2.readException();
                    return obtain2.readString();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // android.hardware.boot.IBootControl
            public boolean isSlotBootable(int i) throws RemoteException {
                Parcel obtain = Parcel.obtain(asBinder());
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(IBootControl.DESCRIPTOR);
                    obtain.writeInt(i);
                    if (!this.mRemote.transact(6, obtain, obtain2, 0)) {
                        throw new RemoteException("Method isSlotBootable is unimplemented.");
                    }
                    obtain2.readException();
                    return obtain2.readBoolean();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // android.hardware.boot.IBootControl
            public boolean isSlotMarkedSuccessful(int i) throws RemoteException {
                Parcel obtain = Parcel.obtain(asBinder());
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(IBootControl.DESCRIPTOR);
                    obtain.writeInt(i);
                    if (!this.mRemote.transact(7, obtain, obtain2, 0)) {
                        throw new RemoteException("Method isSlotMarkedSuccessful is unimplemented.");
                    }
                    obtain2.readException();
                    return obtain2.readBoolean();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // android.hardware.boot.IBootControl
            public void markBootSuccessful() throws RemoteException {
                Parcel obtain = Parcel.obtain(asBinder());
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(IBootControl.DESCRIPTOR);
                    if (!this.mRemote.transact(8, obtain, obtain2, 0)) {
                        throw new RemoteException("Method markBootSuccessful is unimplemented.");
                    }
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // android.hardware.boot.IBootControl
            public void setActiveBootSlot(int i) throws RemoteException {
                Parcel obtain = Parcel.obtain(asBinder());
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(IBootControl.DESCRIPTOR);
                    obtain.writeInt(i);
                    if (!this.mRemote.transact(9, obtain, obtain2, 0)) {
                        throw new RemoteException("Method setActiveBootSlot is unimplemented.");
                    }
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // android.hardware.boot.IBootControl
            public void setSlotAsUnbootable(int i) throws RemoteException {
                Parcel obtain = Parcel.obtain(asBinder());
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(IBootControl.DESCRIPTOR);
                    obtain.writeInt(i);
                    if (!this.mRemote.transact(10, obtain, obtain2, 0)) {
                        throw new RemoteException("Method setSlotAsUnbootable is unimplemented.");
                    }
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // android.hardware.boot.IBootControl
            public void setSnapshotMergeStatus(int i) throws RemoteException {
                Parcel obtain = Parcel.obtain(asBinder());
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(IBootControl.DESCRIPTOR);
                    obtain.writeInt(i);
                    if (!this.mRemote.transact(11, obtain, obtain2, 0)) {
                        throw new RemoteException("Method setSnapshotMergeStatus is unimplemented.");
                    }
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // android.hardware.boot.IBootControl
            public int getInterfaceVersion() throws RemoteException {
                if (this.mCachedVersion == -1) {
                    Parcel obtain = Parcel.obtain(asBinder());
                    Parcel obtain2 = Parcel.obtain();
                    try {
                        obtain.writeInterfaceToken(IBootControl.DESCRIPTOR);
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

            @Override // android.hardware.boot.IBootControl
            public synchronized String getInterfaceHash() throws RemoteException {
                if ("-1".equals(this.mCachedHash)) {
                    Parcel obtain = Parcel.obtain(asBinder());
                    Parcel obtain2 = Parcel.obtain();
                    try {
                        obtain.writeInterfaceToken(IBootControl.DESCRIPTOR);
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
