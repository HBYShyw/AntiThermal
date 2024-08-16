package android.hardware.usb;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public interface IUsbCallback extends IInterface {
    public static final String DESCRIPTOR = "android$hardware$usb$IUsbCallback".replace('$', '.');
    public static final String HASH = "6c1263ce61606107f41a9edb66be1783d5881f00";
    public static final int VERSION = 2;

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public static class Default implements IUsbCallback {
        @Override // android.os.IInterface
        public IBinder asBinder() {
            return null;
        }

        @Override // android.hardware.usb.IUsbCallback
        public String getInterfaceHash() {
            return "";
        }

        @Override // android.hardware.usb.IUsbCallback
        public int getInterfaceVersion() {
            return 0;
        }

        @Override // android.hardware.usb.IUsbCallback
        public void notifyContaminantEnabledStatus(String str, boolean z, int i, long j) throws RemoteException {
        }

        @Override // android.hardware.usb.IUsbCallback
        public void notifyEnableUsbDataStatus(String str, boolean z, int i, long j) throws RemoteException {
        }

        @Override // android.hardware.usb.IUsbCallback
        public void notifyEnableUsbDataWhileDockedStatus(String str, int i, long j) throws RemoteException {
        }

        @Override // android.hardware.usb.IUsbCallback
        public void notifyLimitPowerTransferStatus(String str, boolean z, int i, long j) throws RemoteException {
        }

        @Override // android.hardware.usb.IUsbCallback
        public void notifyPortStatusChange(PortStatus[] portStatusArr, int i) throws RemoteException {
        }

        @Override // android.hardware.usb.IUsbCallback
        public void notifyQueryPortStatus(String str, int i, long j) throws RemoteException {
        }

        @Override // android.hardware.usb.IUsbCallback
        public void notifyResetUsbPortStatus(String str, int i, long j) throws RemoteException {
        }

        @Override // android.hardware.usb.IUsbCallback
        public void notifyRoleSwitchStatus(String str, PortRole portRole, int i, long j) throws RemoteException {
        }
    }

    String getInterfaceHash() throws RemoteException;

    int getInterfaceVersion() throws RemoteException;

    void notifyContaminantEnabledStatus(String str, boolean z, int i, long j) throws RemoteException;

    void notifyEnableUsbDataStatus(String str, boolean z, int i, long j) throws RemoteException;

    void notifyEnableUsbDataWhileDockedStatus(String str, int i, long j) throws RemoteException;

    void notifyLimitPowerTransferStatus(String str, boolean z, int i, long j) throws RemoteException;

    void notifyPortStatusChange(PortStatus[] portStatusArr, int i) throws RemoteException;

    void notifyQueryPortStatus(String str, int i, long j) throws RemoteException;

    void notifyResetUsbPortStatus(String str, int i, long j) throws RemoteException;

    void notifyRoleSwitchStatus(String str, PortRole portRole, int i, long j) throws RemoteException;

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public static abstract class Stub extends Binder implements IUsbCallback {
        static final int TRANSACTION_getInterfaceHash = 16777214;
        static final int TRANSACTION_getInterfaceVersion = 16777215;
        static final int TRANSACTION_notifyContaminantEnabledStatus = 5;
        static final int TRANSACTION_notifyEnableUsbDataStatus = 3;
        static final int TRANSACTION_notifyEnableUsbDataWhileDockedStatus = 4;
        static final int TRANSACTION_notifyLimitPowerTransferStatus = 7;
        static final int TRANSACTION_notifyPortStatusChange = 1;
        static final int TRANSACTION_notifyQueryPortStatus = 6;
        static final int TRANSACTION_notifyResetUsbPortStatus = 8;
        static final int TRANSACTION_notifyRoleSwitchStatus = 2;

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return this;
        }

        public Stub() {
            markVintfStability();
            attachInterface(this, IUsbCallback.DESCRIPTOR);
        }

        public static IUsbCallback asInterface(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface queryLocalInterface = iBinder.queryLocalInterface(IUsbCallback.DESCRIPTOR);
            if (queryLocalInterface != null && (queryLocalInterface instanceof IUsbCallback)) {
                return (IUsbCallback) queryLocalInterface;
            }
            return new Proxy(iBinder);
        }

        @Override // android.os.Binder
        public boolean onTransact(int i, Parcel parcel, Parcel parcel2, int i2) throws RemoteException {
            String str = IUsbCallback.DESCRIPTOR;
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
                            PortStatus[] portStatusArr = (PortStatus[]) parcel.createTypedArray(PortStatus.CREATOR);
                            int readInt = parcel.readInt();
                            parcel.enforceNoDataAvail();
                            notifyPortStatusChange(portStatusArr, readInt);
                            return true;
                        case 2:
                            String readString = parcel.readString();
                            PortRole portRole = (PortRole) parcel.readTypedObject(PortRole.CREATOR);
                            int readInt2 = parcel.readInt();
                            long readLong = parcel.readLong();
                            parcel.enforceNoDataAvail();
                            notifyRoleSwitchStatus(readString, portRole, readInt2, readLong);
                            return true;
                        case 3:
                            String readString2 = parcel.readString();
                            boolean readBoolean = parcel.readBoolean();
                            int readInt3 = parcel.readInt();
                            long readLong2 = parcel.readLong();
                            parcel.enforceNoDataAvail();
                            notifyEnableUsbDataStatus(readString2, readBoolean, readInt3, readLong2);
                            return true;
                        case 4:
                            String readString3 = parcel.readString();
                            int readInt4 = parcel.readInt();
                            long readLong3 = parcel.readLong();
                            parcel.enforceNoDataAvail();
                            notifyEnableUsbDataWhileDockedStatus(readString3, readInt4, readLong3);
                            return true;
                        case 5:
                            String readString4 = parcel.readString();
                            boolean readBoolean2 = parcel.readBoolean();
                            int readInt5 = parcel.readInt();
                            long readLong4 = parcel.readLong();
                            parcel.enforceNoDataAvail();
                            notifyContaminantEnabledStatus(readString4, readBoolean2, readInt5, readLong4);
                            return true;
                        case 6:
                            String readString5 = parcel.readString();
                            int readInt6 = parcel.readInt();
                            long readLong5 = parcel.readLong();
                            parcel.enforceNoDataAvail();
                            notifyQueryPortStatus(readString5, readInt6, readLong5);
                            return true;
                        case 7:
                            String readString6 = parcel.readString();
                            boolean readBoolean3 = parcel.readBoolean();
                            int readInt7 = parcel.readInt();
                            long readLong6 = parcel.readLong();
                            parcel.enforceNoDataAvail();
                            notifyLimitPowerTransferStatus(readString6, readBoolean3, readInt7, readLong6);
                            return true;
                        case 8:
                            String readString7 = parcel.readString();
                            int readInt8 = parcel.readInt();
                            long readLong7 = parcel.readLong();
                            parcel.enforceNoDataAvail();
                            notifyResetUsbPortStatus(readString7, readInt8, readLong7);
                            return true;
                        default:
                            return super.onTransact(i, parcel, parcel2, i2);
                    }
            }
        }

        /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
        private static class Proxy implements IUsbCallback {
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
                return IUsbCallback.DESCRIPTOR;
            }

            @Override // android.hardware.usb.IUsbCallback
            public void notifyPortStatusChange(PortStatus[] portStatusArr, int i) throws RemoteException {
                Parcel obtain = Parcel.obtain(asBinder());
                try {
                    obtain.writeInterfaceToken(IUsbCallback.DESCRIPTOR);
                    obtain.writeTypedArray(portStatusArr, 0);
                    obtain.writeInt(i);
                    if (this.mRemote.transact(1, obtain, null, 1)) {
                    } else {
                        throw new RemoteException("Method notifyPortStatusChange is unimplemented.");
                    }
                } finally {
                    obtain.recycle();
                }
            }

            @Override // android.hardware.usb.IUsbCallback
            public void notifyRoleSwitchStatus(String str, PortRole portRole, int i, long j) throws RemoteException {
                Parcel obtain = Parcel.obtain(asBinder());
                try {
                    obtain.writeInterfaceToken(IUsbCallback.DESCRIPTOR);
                    obtain.writeString(str);
                    obtain.writeTypedObject(portRole, 0);
                    obtain.writeInt(i);
                    obtain.writeLong(j);
                    if (this.mRemote.transact(2, obtain, null, 1)) {
                    } else {
                        throw new RemoteException("Method notifyRoleSwitchStatus is unimplemented.");
                    }
                } finally {
                    obtain.recycle();
                }
            }

            @Override // android.hardware.usb.IUsbCallback
            public void notifyEnableUsbDataStatus(String str, boolean z, int i, long j) throws RemoteException {
                Parcel obtain = Parcel.obtain(asBinder());
                try {
                    obtain.writeInterfaceToken(IUsbCallback.DESCRIPTOR);
                    obtain.writeString(str);
                    obtain.writeBoolean(z);
                    obtain.writeInt(i);
                    obtain.writeLong(j);
                    if (this.mRemote.transact(3, obtain, null, 1)) {
                    } else {
                        throw new RemoteException("Method notifyEnableUsbDataStatus is unimplemented.");
                    }
                } finally {
                    obtain.recycle();
                }
            }

            @Override // android.hardware.usb.IUsbCallback
            public void notifyEnableUsbDataWhileDockedStatus(String str, int i, long j) throws RemoteException {
                Parcel obtain = Parcel.obtain(asBinder());
                try {
                    obtain.writeInterfaceToken(IUsbCallback.DESCRIPTOR);
                    obtain.writeString(str);
                    obtain.writeInt(i);
                    obtain.writeLong(j);
                    if (this.mRemote.transact(4, obtain, null, 1)) {
                    } else {
                        throw new RemoteException("Method notifyEnableUsbDataWhileDockedStatus is unimplemented.");
                    }
                } finally {
                    obtain.recycle();
                }
            }

            @Override // android.hardware.usb.IUsbCallback
            public void notifyContaminantEnabledStatus(String str, boolean z, int i, long j) throws RemoteException {
                Parcel obtain = Parcel.obtain(asBinder());
                try {
                    obtain.writeInterfaceToken(IUsbCallback.DESCRIPTOR);
                    obtain.writeString(str);
                    obtain.writeBoolean(z);
                    obtain.writeInt(i);
                    obtain.writeLong(j);
                    if (this.mRemote.transact(5, obtain, null, 1)) {
                    } else {
                        throw new RemoteException("Method notifyContaminantEnabledStatus is unimplemented.");
                    }
                } finally {
                    obtain.recycle();
                }
            }

            @Override // android.hardware.usb.IUsbCallback
            public void notifyQueryPortStatus(String str, int i, long j) throws RemoteException {
                Parcel obtain = Parcel.obtain(asBinder());
                try {
                    obtain.writeInterfaceToken(IUsbCallback.DESCRIPTOR);
                    obtain.writeString(str);
                    obtain.writeInt(i);
                    obtain.writeLong(j);
                    if (this.mRemote.transact(6, obtain, null, 1)) {
                    } else {
                        throw new RemoteException("Method notifyQueryPortStatus is unimplemented.");
                    }
                } finally {
                    obtain.recycle();
                }
            }

            @Override // android.hardware.usb.IUsbCallback
            public void notifyLimitPowerTransferStatus(String str, boolean z, int i, long j) throws RemoteException {
                Parcel obtain = Parcel.obtain(asBinder());
                try {
                    obtain.writeInterfaceToken(IUsbCallback.DESCRIPTOR);
                    obtain.writeString(str);
                    obtain.writeBoolean(z);
                    obtain.writeInt(i);
                    obtain.writeLong(j);
                    if (this.mRemote.transact(7, obtain, null, 1)) {
                    } else {
                        throw new RemoteException("Method notifyLimitPowerTransferStatus is unimplemented.");
                    }
                } finally {
                    obtain.recycle();
                }
            }

            @Override // android.hardware.usb.IUsbCallback
            public void notifyResetUsbPortStatus(String str, int i, long j) throws RemoteException {
                Parcel obtain = Parcel.obtain(asBinder());
                try {
                    obtain.writeInterfaceToken(IUsbCallback.DESCRIPTOR);
                    obtain.writeString(str);
                    obtain.writeInt(i);
                    obtain.writeLong(j);
                    if (this.mRemote.transact(8, obtain, null, 1)) {
                    } else {
                        throw new RemoteException("Method notifyResetUsbPortStatus is unimplemented.");
                    }
                } finally {
                    obtain.recycle();
                }
            }

            @Override // android.hardware.usb.IUsbCallback
            public int getInterfaceVersion() throws RemoteException {
                if (this.mCachedVersion == -1) {
                    Parcel obtain = Parcel.obtain(asBinder());
                    Parcel obtain2 = Parcel.obtain();
                    try {
                        obtain.writeInterfaceToken(IUsbCallback.DESCRIPTOR);
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

            @Override // android.hardware.usb.IUsbCallback
            public synchronized String getInterfaceHash() throws RemoteException {
                if ("-1".equals(this.mCachedHash)) {
                    Parcel obtain = Parcel.obtain(asBinder());
                    Parcel obtain2 = Parcel.obtain();
                    try {
                        obtain.writeInterfaceToken(IUsbCallback.DESCRIPTOR);
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
