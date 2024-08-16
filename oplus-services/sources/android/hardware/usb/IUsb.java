package android.hardware.usb;

import android.hardware.usb.IUsbCallback;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public interface IUsb extends IInterface {
    public static final String DESCRIPTOR = "android$hardware$usb$IUsb".replace('$', '.');
    public static final String HASH = "6c1263ce61606107f41a9edb66be1783d5881f00";
    public static final int VERSION = 2;

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public static class Default implements IUsb {
        @Override // android.os.IInterface
        public IBinder asBinder() {
            return null;
        }

        @Override // android.hardware.usb.IUsb
        public void enableContaminantPresenceDetection(String str, boolean z, long j) throws RemoteException {
        }

        @Override // android.hardware.usb.IUsb
        public void enableUsbData(String str, boolean z, long j) throws RemoteException {
        }

        @Override // android.hardware.usb.IUsb
        public void enableUsbDataWhileDocked(String str, long j) throws RemoteException {
        }

        @Override // android.hardware.usb.IUsb
        public String getInterfaceHash() {
            return "";
        }

        @Override // android.hardware.usb.IUsb
        public int getInterfaceVersion() {
            return 0;
        }

        @Override // android.hardware.usb.IUsb
        public void limitPowerTransfer(String str, boolean z, long j) throws RemoteException {
        }

        @Override // android.hardware.usb.IUsb
        public void queryPortStatus(long j) throws RemoteException {
        }

        @Override // android.hardware.usb.IUsb
        public void resetUsbPort(String str, long j) throws RemoteException {
        }

        @Override // android.hardware.usb.IUsb
        public void setCallback(IUsbCallback iUsbCallback) throws RemoteException {
        }

        @Override // android.hardware.usb.IUsb
        public void switchRole(String str, PortRole portRole, long j) throws RemoteException {
        }
    }

    void enableContaminantPresenceDetection(String str, boolean z, long j) throws RemoteException;

    void enableUsbData(String str, boolean z, long j) throws RemoteException;

    void enableUsbDataWhileDocked(String str, long j) throws RemoteException;

    String getInterfaceHash() throws RemoteException;

    int getInterfaceVersion() throws RemoteException;

    void limitPowerTransfer(String str, boolean z, long j) throws RemoteException;

    void queryPortStatus(long j) throws RemoteException;

    void resetUsbPort(String str, long j) throws RemoteException;

    void setCallback(IUsbCallback iUsbCallback) throws RemoteException;

    void switchRole(String str, PortRole portRole, long j) throws RemoteException;

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public static abstract class Stub extends Binder implements IUsb {
        static final int TRANSACTION_enableContaminantPresenceDetection = 1;
        static final int TRANSACTION_enableUsbData = 2;
        static final int TRANSACTION_enableUsbDataWhileDocked = 3;
        static final int TRANSACTION_getInterfaceHash = 16777214;
        static final int TRANSACTION_getInterfaceVersion = 16777215;
        static final int TRANSACTION_limitPowerTransfer = 7;
        static final int TRANSACTION_queryPortStatus = 4;
        static final int TRANSACTION_resetUsbPort = 8;
        static final int TRANSACTION_setCallback = 5;
        static final int TRANSACTION_switchRole = 6;

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return this;
        }

        public Stub() {
            markVintfStability();
            attachInterface(this, IUsb.DESCRIPTOR);
        }

        public static IUsb asInterface(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface queryLocalInterface = iBinder.queryLocalInterface(IUsb.DESCRIPTOR);
            if (queryLocalInterface != null && (queryLocalInterface instanceof IUsb)) {
                return (IUsb) queryLocalInterface;
            }
            return new Proxy(iBinder);
        }

        @Override // android.os.Binder
        public boolean onTransact(int i, Parcel parcel, Parcel parcel2, int i2) throws RemoteException {
            String str = IUsb.DESCRIPTOR;
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
                            String readString = parcel.readString();
                            boolean readBoolean = parcel.readBoolean();
                            long readLong = parcel.readLong();
                            parcel.enforceNoDataAvail();
                            enableContaminantPresenceDetection(readString, readBoolean, readLong);
                            return true;
                        case 2:
                            String readString2 = parcel.readString();
                            boolean readBoolean2 = parcel.readBoolean();
                            long readLong2 = parcel.readLong();
                            parcel.enforceNoDataAvail();
                            enableUsbData(readString2, readBoolean2, readLong2);
                            return true;
                        case 3:
                            String readString3 = parcel.readString();
                            long readLong3 = parcel.readLong();
                            parcel.enforceNoDataAvail();
                            enableUsbDataWhileDocked(readString3, readLong3);
                            return true;
                        case 4:
                            long readLong4 = parcel.readLong();
                            parcel.enforceNoDataAvail();
                            queryPortStatus(readLong4);
                            return true;
                        case 5:
                            IUsbCallback asInterface = IUsbCallback.Stub.asInterface(parcel.readStrongBinder());
                            parcel.enforceNoDataAvail();
                            setCallback(asInterface);
                            return true;
                        case 6:
                            String readString4 = parcel.readString();
                            PortRole portRole = (PortRole) parcel.readTypedObject(PortRole.CREATOR);
                            long readLong5 = parcel.readLong();
                            parcel.enforceNoDataAvail();
                            switchRole(readString4, portRole, readLong5);
                            return true;
                        case 7:
                            String readString5 = parcel.readString();
                            boolean readBoolean3 = parcel.readBoolean();
                            long readLong6 = parcel.readLong();
                            parcel.enforceNoDataAvail();
                            limitPowerTransfer(readString5, readBoolean3, readLong6);
                            return true;
                        case 8:
                            String readString6 = parcel.readString();
                            long readLong7 = parcel.readLong();
                            parcel.enforceNoDataAvail();
                            resetUsbPort(readString6, readLong7);
                            return true;
                        default:
                            return super.onTransact(i, parcel, parcel2, i2);
                    }
            }
        }

        /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
        private static class Proxy implements IUsb {
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
                return IUsb.DESCRIPTOR;
            }

            @Override // android.hardware.usb.IUsb
            public void enableContaminantPresenceDetection(String str, boolean z, long j) throws RemoteException {
                Parcel obtain = Parcel.obtain(asBinder());
                try {
                    obtain.writeInterfaceToken(IUsb.DESCRIPTOR);
                    obtain.writeString(str);
                    obtain.writeBoolean(z);
                    obtain.writeLong(j);
                    if (this.mRemote.transact(1, obtain, null, 1)) {
                    } else {
                        throw new RemoteException("Method enableContaminantPresenceDetection is unimplemented.");
                    }
                } finally {
                    obtain.recycle();
                }
            }

            @Override // android.hardware.usb.IUsb
            public void enableUsbData(String str, boolean z, long j) throws RemoteException {
                Parcel obtain = Parcel.obtain(asBinder());
                try {
                    obtain.writeInterfaceToken(IUsb.DESCRIPTOR);
                    obtain.writeString(str);
                    obtain.writeBoolean(z);
                    obtain.writeLong(j);
                    if (this.mRemote.transact(2, obtain, null, 1)) {
                    } else {
                        throw new RemoteException("Method enableUsbData is unimplemented.");
                    }
                } finally {
                    obtain.recycle();
                }
            }

            @Override // android.hardware.usb.IUsb
            public void enableUsbDataWhileDocked(String str, long j) throws RemoteException {
                Parcel obtain = Parcel.obtain(asBinder());
                try {
                    obtain.writeInterfaceToken(IUsb.DESCRIPTOR);
                    obtain.writeString(str);
                    obtain.writeLong(j);
                    if (this.mRemote.transact(3, obtain, null, 1)) {
                    } else {
                        throw new RemoteException("Method enableUsbDataWhileDocked is unimplemented.");
                    }
                } finally {
                    obtain.recycle();
                }
            }

            @Override // android.hardware.usb.IUsb
            public void queryPortStatus(long j) throws RemoteException {
                Parcel obtain = Parcel.obtain(asBinder());
                try {
                    obtain.writeInterfaceToken(IUsb.DESCRIPTOR);
                    obtain.writeLong(j);
                    if (this.mRemote.transact(4, obtain, null, 1)) {
                    } else {
                        throw new RemoteException("Method queryPortStatus is unimplemented.");
                    }
                } finally {
                    obtain.recycle();
                }
            }

            @Override // android.hardware.usb.IUsb
            public void setCallback(IUsbCallback iUsbCallback) throws RemoteException {
                Parcel obtain = Parcel.obtain(asBinder());
                try {
                    obtain.writeInterfaceToken(IUsb.DESCRIPTOR);
                    obtain.writeStrongInterface(iUsbCallback);
                    if (this.mRemote.transact(5, obtain, null, 1)) {
                    } else {
                        throw new RemoteException("Method setCallback is unimplemented.");
                    }
                } finally {
                    obtain.recycle();
                }
            }

            @Override // android.hardware.usb.IUsb
            public void switchRole(String str, PortRole portRole, long j) throws RemoteException {
                Parcel obtain = Parcel.obtain(asBinder());
                try {
                    obtain.writeInterfaceToken(IUsb.DESCRIPTOR);
                    obtain.writeString(str);
                    obtain.writeTypedObject(portRole, 0);
                    obtain.writeLong(j);
                    if (this.mRemote.transact(6, obtain, null, 1)) {
                    } else {
                        throw new RemoteException("Method switchRole is unimplemented.");
                    }
                } finally {
                    obtain.recycle();
                }
            }

            @Override // android.hardware.usb.IUsb
            public void limitPowerTransfer(String str, boolean z, long j) throws RemoteException {
                Parcel obtain = Parcel.obtain(asBinder());
                try {
                    obtain.writeInterfaceToken(IUsb.DESCRIPTOR);
                    obtain.writeString(str);
                    obtain.writeBoolean(z);
                    obtain.writeLong(j);
                    if (this.mRemote.transact(7, obtain, null, 1)) {
                    } else {
                        throw new RemoteException("Method limitPowerTransfer is unimplemented.");
                    }
                } finally {
                    obtain.recycle();
                }
            }

            @Override // android.hardware.usb.IUsb
            public void resetUsbPort(String str, long j) throws RemoteException {
                Parcel obtain = Parcel.obtain(asBinder());
                try {
                    obtain.writeInterfaceToken(IUsb.DESCRIPTOR);
                    obtain.writeString(str);
                    obtain.writeLong(j);
                    if (this.mRemote.transact(8, obtain, null, 1)) {
                    } else {
                        throw new RemoteException("Method resetUsbPort is unimplemented.");
                    }
                } finally {
                    obtain.recycle();
                }
            }

            @Override // android.hardware.usb.IUsb
            public int getInterfaceVersion() throws RemoteException {
                if (this.mCachedVersion == -1) {
                    Parcel obtain = Parcel.obtain(asBinder());
                    Parcel obtain2 = Parcel.obtain();
                    try {
                        obtain.writeInterfaceToken(IUsb.DESCRIPTOR);
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

            @Override // android.hardware.usb.IUsb
            public synchronized String getInterfaceHash() throws RemoteException {
                if ("-1".equals(this.mCachedHash)) {
                    Parcel obtain = Parcel.obtain(asBinder());
                    Parcel obtain2 = Parcel.obtain();
                    try {
                        obtain.writeInterfaceToken(IUsb.DESCRIPTOR);
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
