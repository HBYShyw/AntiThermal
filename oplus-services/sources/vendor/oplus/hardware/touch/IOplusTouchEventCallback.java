package vendor.oplus.hardware.touch;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
public interface IOplusTouchEventCallback extends IInterface {
    public static final String DESCRIPTOR = "vendor$oplus$hardware$touch$IOplusTouchEventCallback".replace('$', '.');
    public static final String HASH = "ef41c5fab372bb5ad6a417f606845c87cf0e9b17";
    public static final int VERSION = 2;

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
    public static class Default implements IOplusTouchEventCallback {
        @Override // android.os.IInterface
        public IBinder asBinder() {
            return null;
        }

        @Override // vendor.oplus.hardware.touch.IOplusTouchEventCallback
        public String getInterfaceHash() {
            return "";
        }

        @Override // vendor.oplus.hardware.touch.IOplusTouchEventCallback
        public int getInterfaceVersion() {
            return 0;
        }

        @Override // vendor.oplus.hardware.touch.IOplusTouchEventCallback
        public void touchSendCommand(int i, OplusTouchInfo oplusTouchInfo) throws RemoteException {
        }

        @Override // vendor.oplus.hardware.touch.IOplusTouchEventCallback
        public void touchSendCommandOneWay(int i, OplusTouchInfo oplusTouchInfo) throws RemoteException {
        }
    }

    String getInterfaceHash() throws RemoteException;

    int getInterfaceVersion() throws RemoteException;

    void touchSendCommand(int i, OplusTouchInfo oplusTouchInfo) throws RemoteException;

    void touchSendCommandOneWay(int i, OplusTouchInfo oplusTouchInfo) throws RemoteException;

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
    public static abstract class Stub extends Binder implements IOplusTouchEventCallback {
        static final int TRANSACTION_getInterfaceHash = 16777214;
        static final int TRANSACTION_getInterfaceVersion = 16777215;
        static final int TRANSACTION_touchSendCommand = 1;
        static final int TRANSACTION_touchSendCommandOneWay = 2;

        public static String getDefaultTransactionName(int i) {
            if (i == 1) {
                return "touchSendCommand";
            }
            if (i == 2) {
                return "touchSendCommandOneWay";
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
            attachInterface(this, IOplusTouchEventCallback.DESCRIPTOR);
        }

        public static IOplusTouchEventCallback asInterface(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface queryLocalInterface = iBinder.queryLocalInterface(IOplusTouchEventCallback.DESCRIPTOR);
            if (queryLocalInterface != null && (queryLocalInterface instanceof IOplusTouchEventCallback)) {
                return (IOplusTouchEventCallback) queryLocalInterface;
            }
            return new Proxy(iBinder);
        }

        public String getTransactionName(int i) {
            return getDefaultTransactionName(i);
        }

        @Override // android.os.Binder
        public boolean onTransact(int i, Parcel parcel, Parcel parcel2, int i2) throws RemoteException {
            String str = IOplusTouchEventCallback.DESCRIPTOR;
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
                        OplusTouchInfo oplusTouchInfo = (OplusTouchInfo) parcel.readTypedObject(OplusTouchInfo.CREATOR);
                        parcel.enforceNoDataAvail();
                        touchSendCommand(readInt, oplusTouchInfo);
                        parcel2.writeNoException();
                    } else if (i == 2) {
                        int readInt2 = parcel.readInt();
                        OplusTouchInfo oplusTouchInfo2 = (OplusTouchInfo) parcel.readTypedObject(OplusTouchInfo.CREATOR);
                        parcel.enforceNoDataAvail();
                        touchSendCommandOneWay(readInt2, oplusTouchInfo2);
                    } else {
                        return super.onTransact(i, parcel, parcel2, i2);
                    }
                    return true;
            }
        }

        /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
        private static class Proxy implements IOplusTouchEventCallback {
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
                return IOplusTouchEventCallback.DESCRIPTOR;
            }

            @Override // vendor.oplus.hardware.touch.IOplusTouchEventCallback
            public void touchSendCommand(int i, OplusTouchInfo oplusTouchInfo) throws RemoteException {
                Parcel obtain = Parcel.obtain(asBinder());
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(IOplusTouchEventCallback.DESCRIPTOR);
                    obtain.writeInt(i);
                    obtain.writeTypedObject(oplusTouchInfo, 0);
                    if (!this.mRemote.transact(1, obtain, obtain2, 0)) {
                        throw new RemoteException("Method touchSendCommand is unimplemented.");
                    }
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // vendor.oplus.hardware.touch.IOplusTouchEventCallback
            public void touchSendCommandOneWay(int i, OplusTouchInfo oplusTouchInfo) throws RemoteException {
                Parcel obtain = Parcel.obtain(asBinder());
                try {
                    obtain.writeInterfaceToken(IOplusTouchEventCallback.DESCRIPTOR);
                    obtain.writeInt(i);
                    obtain.writeTypedObject(oplusTouchInfo, 0);
                    if (this.mRemote.transact(2, obtain, null, 1)) {
                    } else {
                        throw new RemoteException("Method touchSendCommandOneWay is unimplemented.");
                    }
                } finally {
                    obtain.recycle();
                }
            }

            @Override // vendor.oplus.hardware.touch.IOplusTouchEventCallback
            public int getInterfaceVersion() throws RemoteException {
                if (this.mCachedVersion == -1) {
                    Parcel obtain = Parcel.obtain(asBinder());
                    Parcel obtain2 = Parcel.obtain();
                    try {
                        obtain.writeInterfaceToken(IOplusTouchEventCallback.DESCRIPTOR);
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

            @Override // vendor.oplus.hardware.touch.IOplusTouchEventCallback
            public synchronized String getInterfaceHash() throws RemoteException {
                if ("-1".equals(this.mCachedHash)) {
                    Parcel obtain = Parcel.obtain(asBinder());
                    Parcel obtain2 = Parcel.obtain();
                    try {
                        obtain.writeInterfaceToken(IOplusTouchEventCallback.DESCRIPTOR);
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
