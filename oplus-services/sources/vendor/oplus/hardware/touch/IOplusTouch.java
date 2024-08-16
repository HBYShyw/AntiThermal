package vendor.oplus.hardware.touch;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import vendor.oplus.hardware.touch.IOplusTouchEventCallback;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
public interface IOplusTouch extends IInterface {
    public static final String DESCRIPTOR = "vendor$oplus$hardware$touch$IOplusTouch".replace('$', '.');
    public static final String HASH = "ef41c5fab372bb5ad6a417f606845c87cf0e9b17";
    public static final int VERSION = 2;

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
    public static class Default implements IOplusTouch {
        @Override // android.os.IInterface
        public IBinder asBinder() {
            return null;
        }

        @Override // vendor.oplus.hardware.touch.IOplusTouch
        public String getInterfaceHash() {
            return "";
        }

        @Override // vendor.oplus.hardware.touch.IOplusTouch
        public int getInterfaceVersion() {
            return 0;
        }

        @Override // vendor.oplus.hardware.touch.IOplusTouch
        public int initialize() throws RemoteException {
            return 0;
        }

        @Override // vendor.oplus.hardware.touch.IOplusTouch
        public int isTouchNodeSupport(int i, int i2) throws RemoteException {
            return 0;
        }

        @Override // vendor.oplus.hardware.touch.IOplusTouch
        public int registerEventCallback(IOplusTouchEventCallback iOplusTouchEventCallback) throws RemoteException {
            return 0;
        }

        @Override // vendor.oplus.hardware.touch.IOplusTouch
        public int touchNotifyClient(int i, OplusTouchInfo oplusTouchInfo) throws RemoteException {
            return 0;
        }

        @Override // vendor.oplus.hardware.touch.IOplusTouch
        public String touchReadNodeFile(int i, int i2) throws RemoteException {
            return null;
        }

        @Override // vendor.oplus.hardware.touch.IOplusTouch
        public int touchWriteBtInfo(int i, int i2, String str) throws RemoteException {
            return 0;
        }

        @Override // vendor.oplus.hardware.touch.IOplusTouch
        public int touchWriteNodeFile(int i, int i2, String str) throws RemoteException {
            return 0;
        }

        @Override // vendor.oplus.hardware.touch.IOplusTouch
        public void touchWriteNodeFileOneWay(int i, int i2, String str) throws RemoteException {
        }

        @Override // vendor.oplus.hardware.touch.IOplusTouch
        public int unregisterEventCallback(IOplusTouchEventCallback iOplusTouchEventCallback) throws RemoteException {
            return 0;
        }
    }

    String getInterfaceHash() throws RemoteException;

    int getInterfaceVersion() throws RemoteException;

    int initialize() throws RemoteException;

    int isTouchNodeSupport(int i, int i2) throws RemoteException;

    int registerEventCallback(IOplusTouchEventCallback iOplusTouchEventCallback) throws RemoteException;

    int touchNotifyClient(int i, OplusTouchInfo oplusTouchInfo) throws RemoteException;

    String touchReadNodeFile(int i, int i2) throws RemoteException;

    int touchWriteBtInfo(int i, int i2, String str) throws RemoteException;

    int touchWriteNodeFile(int i, int i2, String str) throws RemoteException;

    void touchWriteNodeFileOneWay(int i, int i2, String str) throws RemoteException;

    int unregisterEventCallback(IOplusTouchEventCallback iOplusTouchEventCallback) throws RemoteException;

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
    public static abstract class Stub extends Binder implements IOplusTouch {
        static final int TRANSACTION_getInterfaceHash = 16777214;
        static final int TRANSACTION_getInterfaceVersion = 16777215;
        static final int TRANSACTION_initialize = 1;
        static final int TRANSACTION_isTouchNodeSupport = 2;
        static final int TRANSACTION_registerEventCallback = 8;
        static final int TRANSACTION_touchNotifyClient = 7;
        static final int TRANSACTION_touchReadNodeFile = 3;
        static final int TRANSACTION_touchWriteBtInfo = 5;
        static final int TRANSACTION_touchWriteNodeFile = 4;
        static final int TRANSACTION_touchWriteNodeFileOneWay = 6;
        static final int TRANSACTION_unregisterEventCallback = 9;

        public static String getDefaultTransactionName(int i) {
            switch (i) {
                case 1:
                    return "initialize";
                case 2:
                    return "isTouchNodeSupport";
                case 3:
                    return "touchReadNodeFile";
                case 4:
                    return "touchWriteNodeFile";
                case 5:
                    return "touchWriteBtInfo";
                case 6:
                    return "touchWriteNodeFileOneWay";
                case 7:
                    return "touchNotifyClient";
                case 8:
                    return "registerEventCallback";
                case 9:
                    return "unregisterEventCallback";
                default:
                    switch (i) {
                        case TRANSACTION_getInterfaceHash /* 16777214 */:
                            return "getInterfaceHash";
                        case TRANSACTION_getInterfaceVersion /* 16777215 */:
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
            attachInterface(this, IOplusTouch.DESCRIPTOR);
        }

        public static IOplusTouch asInterface(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface queryLocalInterface = iBinder.queryLocalInterface(IOplusTouch.DESCRIPTOR);
            if (queryLocalInterface != null && (queryLocalInterface instanceof IOplusTouch)) {
                return (IOplusTouch) queryLocalInterface;
            }
            return new Proxy(iBinder);
        }

        public String getTransactionName(int i) {
            return getDefaultTransactionName(i);
        }

        @Override // android.os.Binder
        public boolean onTransact(int i, Parcel parcel, Parcel parcel2, int i2) throws RemoteException {
            String str = IOplusTouch.DESCRIPTOR;
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
                    switch (i) {
                        case 1:
                            int initialize = initialize();
                            parcel2.writeNoException();
                            parcel2.writeInt(initialize);
                            return true;
                        case 2:
                            int readInt = parcel.readInt();
                            int readInt2 = parcel.readInt();
                            parcel.enforceNoDataAvail();
                            int isTouchNodeSupport = isTouchNodeSupport(readInt, readInt2);
                            parcel2.writeNoException();
                            parcel2.writeInt(isTouchNodeSupport);
                            return true;
                        case 3:
                            int readInt3 = parcel.readInt();
                            int readInt4 = parcel.readInt();
                            parcel.enforceNoDataAvail();
                            String str2 = touchReadNodeFile(readInt3, readInt4);
                            parcel2.writeNoException();
                            parcel2.writeString(str2);
                            return true;
                        case 4:
                            int readInt5 = parcel.readInt();
                            int readInt6 = parcel.readInt();
                            String readString = parcel.readString();
                            parcel.enforceNoDataAvail();
                            int i3 = touchWriteNodeFile(readInt5, readInt6, readString);
                            parcel2.writeNoException();
                            parcel2.writeInt(i3);
                            return true;
                        case 5:
                            int readInt7 = parcel.readInt();
                            int readInt8 = parcel.readInt();
                            String readString2 = parcel.readString();
                            parcel.enforceNoDataAvail();
                            int i4 = touchWriteBtInfo(readInt7, readInt8, readString2);
                            parcel2.writeNoException();
                            parcel2.writeInt(i4);
                            return true;
                        case 6:
                            int readInt9 = parcel.readInt();
                            int readInt10 = parcel.readInt();
                            String readString3 = parcel.readString();
                            parcel.enforceNoDataAvail();
                            touchWriteNodeFileOneWay(readInt9, readInt10, readString3);
                            return true;
                        case 7:
                            int readInt11 = parcel.readInt();
                            OplusTouchInfo oplusTouchInfo = (OplusTouchInfo) parcel.readTypedObject(OplusTouchInfo.CREATOR);
                            parcel.enforceNoDataAvail();
                            int i5 = touchNotifyClient(readInt11, oplusTouchInfo);
                            parcel2.writeNoException();
                            parcel2.writeInt(i5);
                            return true;
                        case 8:
                            IOplusTouchEventCallback asInterface = IOplusTouchEventCallback.Stub.asInterface(parcel.readStrongBinder());
                            parcel.enforceNoDataAvail();
                            int registerEventCallback = registerEventCallback(asInterface);
                            parcel2.writeNoException();
                            parcel2.writeInt(registerEventCallback);
                            return true;
                        case 9:
                            IOplusTouchEventCallback asInterface2 = IOplusTouchEventCallback.Stub.asInterface(parcel.readStrongBinder());
                            parcel.enforceNoDataAvail();
                            int unregisterEventCallback = unregisterEventCallback(asInterface2);
                            parcel2.writeNoException();
                            parcel2.writeInt(unregisterEventCallback);
                            return true;
                        default:
                            return super.onTransact(i, parcel, parcel2, i2);
                    }
            }
        }

        /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
        private static class Proxy implements IOplusTouch {
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
                return IOplusTouch.DESCRIPTOR;
            }

            @Override // vendor.oplus.hardware.touch.IOplusTouch
            public int initialize() throws RemoteException {
                Parcel obtain = Parcel.obtain(asBinder());
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(IOplusTouch.DESCRIPTOR);
                    if (!this.mRemote.transact(1, obtain, obtain2, 0)) {
                        throw new RemoteException("Method initialize is unimplemented.");
                    }
                    obtain2.readException();
                    return obtain2.readInt();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // vendor.oplus.hardware.touch.IOplusTouch
            public int isTouchNodeSupport(int i, int i2) throws RemoteException {
                Parcel obtain = Parcel.obtain(asBinder());
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(IOplusTouch.DESCRIPTOR);
                    obtain.writeInt(i);
                    obtain.writeInt(i2);
                    if (!this.mRemote.transact(2, obtain, obtain2, 0)) {
                        throw new RemoteException("Method isTouchNodeSupport is unimplemented.");
                    }
                    obtain2.readException();
                    return obtain2.readInt();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // vendor.oplus.hardware.touch.IOplusTouch
            public String touchReadNodeFile(int i, int i2) throws RemoteException {
                Parcel obtain = Parcel.obtain(asBinder());
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(IOplusTouch.DESCRIPTOR);
                    obtain.writeInt(i);
                    obtain.writeInt(i2);
                    if (!this.mRemote.transact(3, obtain, obtain2, 0)) {
                        throw new RemoteException("Method touchReadNodeFile is unimplemented.");
                    }
                    obtain2.readException();
                    return obtain2.readString();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // vendor.oplus.hardware.touch.IOplusTouch
            public int touchWriteNodeFile(int i, int i2, String str) throws RemoteException {
                Parcel obtain = Parcel.obtain(asBinder());
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(IOplusTouch.DESCRIPTOR);
                    obtain.writeInt(i);
                    obtain.writeInt(i2);
                    obtain.writeString(str);
                    if (!this.mRemote.transact(4, obtain, obtain2, 0)) {
                        throw new RemoteException("Method touchWriteNodeFile is unimplemented.");
                    }
                    obtain2.readException();
                    return obtain2.readInt();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // vendor.oplus.hardware.touch.IOplusTouch
            public int touchWriteBtInfo(int i, int i2, String str) throws RemoteException {
                Parcel obtain = Parcel.obtain(asBinder());
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(IOplusTouch.DESCRIPTOR);
                    obtain.writeInt(i);
                    obtain.writeInt(i2);
                    obtain.writeString(str);
                    if (!this.mRemote.transact(5, obtain, obtain2, 0)) {
                        throw new RemoteException("Method touchWriteBtInfo is unimplemented.");
                    }
                    obtain2.readException();
                    return obtain2.readInt();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // vendor.oplus.hardware.touch.IOplusTouch
            public void touchWriteNodeFileOneWay(int i, int i2, String str) throws RemoteException {
                Parcel obtain = Parcel.obtain(asBinder());
                try {
                    obtain.writeInterfaceToken(IOplusTouch.DESCRIPTOR);
                    obtain.writeInt(i);
                    obtain.writeInt(i2);
                    obtain.writeString(str);
                    if (this.mRemote.transact(6, obtain, null, 1)) {
                    } else {
                        throw new RemoteException("Method touchWriteNodeFileOneWay is unimplemented.");
                    }
                } finally {
                    obtain.recycle();
                }
            }

            @Override // vendor.oplus.hardware.touch.IOplusTouch
            public int touchNotifyClient(int i, OplusTouchInfo oplusTouchInfo) throws RemoteException {
                Parcel obtain = Parcel.obtain(asBinder());
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(IOplusTouch.DESCRIPTOR);
                    obtain.writeInt(i);
                    obtain.writeTypedObject(oplusTouchInfo, 0);
                    if (!this.mRemote.transact(7, obtain, obtain2, 0)) {
                        throw new RemoteException("Method touchNotifyClient is unimplemented.");
                    }
                    obtain2.readException();
                    return obtain2.readInt();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // vendor.oplus.hardware.touch.IOplusTouch
            public int registerEventCallback(IOplusTouchEventCallback iOplusTouchEventCallback) throws RemoteException {
                Parcel obtain = Parcel.obtain(asBinder());
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(IOplusTouch.DESCRIPTOR);
                    obtain.writeStrongInterface(iOplusTouchEventCallback);
                    if (!this.mRemote.transact(8, obtain, obtain2, 0)) {
                        throw new RemoteException("Method registerEventCallback is unimplemented.");
                    }
                    obtain2.readException();
                    return obtain2.readInt();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // vendor.oplus.hardware.touch.IOplusTouch
            public int unregisterEventCallback(IOplusTouchEventCallback iOplusTouchEventCallback) throws RemoteException {
                Parcel obtain = Parcel.obtain(asBinder());
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(IOplusTouch.DESCRIPTOR);
                    obtain.writeStrongInterface(iOplusTouchEventCallback);
                    if (!this.mRemote.transact(9, obtain, obtain2, 0)) {
                        throw new RemoteException("Method unregisterEventCallback is unimplemented.");
                    }
                    obtain2.readException();
                    return obtain2.readInt();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // vendor.oplus.hardware.touch.IOplusTouch
            public int getInterfaceVersion() throws RemoteException {
                if (this.mCachedVersion == -1) {
                    Parcel obtain = Parcel.obtain(asBinder());
                    Parcel obtain2 = Parcel.obtain();
                    try {
                        obtain.writeInterfaceToken(IOplusTouch.DESCRIPTOR);
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

            @Override // vendor.oplus.hardware.touch.IOplusTouch
            public synchronized String getInterfaceHash() throws RemoteException {
                if ("-1".equals(this.mCachedHash)) {
                    Parcel obtain = Parcel.obtain(asBinder());
                    Parcel obtain2 = Parcel.obtain();
                    try {
                        obtain.writeInterfaceToken(IOplusTouch.DESCRIPTOR);
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
