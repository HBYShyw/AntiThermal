package android.hardware.tv.hdmi.connection;

import android.hardware.tv.hdmi.connection.IHdmiConnectionCallback;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public interface IHdmiConnection extends IInterface {
    public static final String DESCRIPTOR = "android$hardware$tv$hdmi$connection$IHdmiConnection".replace('$', '.');
    public static final String HASH = "85c26fa47f3c3062aa93ffc8bb0897a85c8cb118";
    public static final int VERSION = 1;

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public static class Default implements IHdmiConnection {
        @Override // android.os.IInterface
        public IBinder asBinder() {
            return null;
        }

        @Override // android.hardware.tv.hdmi.connection.IHdmiConnection
        public byte getHpdSignal(int i) throws RemoteException {
            return (byte) 0;
        }

        @Override // android.hardware.tv.hdmi.connection.IHdmiConnection
        public String getInterfaceHash() {
            return "";
        }

        @Override // android.hardware.tv.hdmi.connection.IHdmiConnection
        public int getInterfaceVersion() {
            return 0;
        }

        @Override // android.hardware.tv.hdmi.connection.IHdmiConnection
        public HdmiPortInfo[] getPortInfo() throws RemoteException {
            return null;
        }

        @Override // android.hardware.tv.hdmi.connection.IHdmiConnection
        public boolean isConnected(int i) throws RemoteException {
            return false;
        }

        @Override // android.hardware.tv.hdmi.connection.IHdmiConnection
        public void setCallback(IHdmiConnectionCallback iHdmiConnectionCallback) throws RemoteException {
        }

        @Override // android.hardware.tv.hdmi.connection.IHdmiConnection
        public void setHpdSignal(byte b, int i) throws RemoteException {
        }
    }

    byte getHpdSignal(int i) throws RemoteException;

    String getInterfaceHash() throws RemoteException;

    int getInterfaceVersion() throws RemoteException;

    HdmiPortInfo[] getPortInfo() throws RemoteException;

    boolean isConnected(int i) throws RemoteException;

    void setCallback(IHdmiConnectionCallback iHdmiConnectionCallback) throws RemoteException;

    void setHpdSignal(byte b, int i) throws RemoteException;

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public static abstract class Stub extends Binder implements IHdmiConnection {
        static final int TRANSACTION_getHpdSignal = 5;
        static final int TRANSACTION_getInterfaceHash = 16777214;
        static final int TRANSACTION_getInterfaceVersion = 16777215;
        static final int TRANSACTION_getPortInfo = 1;
        static final int TRANSACTION_isConnected = 2;
        static final int TRANSACTION_setCallback = 3;
        static final int TRANSACTION_setHpdSignal = 4;

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return this;
        }

        public Stub() {
            markVintfStability();
            attachInterface(this, IHdmiConnection.DESCRIPTOR);
        }

        public static IHdmiConnection asInterface(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface queryLocalInterface = iBinder.queryLocalInterface(IHdmiConnection.DESCRIPTOR);
            if (queryLocalInterface != null && (queryLocalInterface instanceof IHdmiConnection)) {
                return (IHdmiConnection) queryLocalInterface;
            }
            return new Proxy(iBinder);
        }

        @Override // android.os.Binder
        public boolean onTransact(int i, Parcel parcel, Parcel parcel2, int i2) throws RemoteException {
            String str = IHdmiConnection.DESCRIPTOR;
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
                        HdmiPortInfo[] portInfo = getPortInfo();
                        parcel2.writeNoException();
                        parcel2.writeTypedArray(portInfo, 1);
                    } else if (i == 2) {
                        int readInt = parcel.readInt();
                        parcel.enforceNoDataAvail();
                        boolean isConnected = isConnected(readInt);
                        parcel2.writeNoException();
                        parcel2.writeBoolean(isConnected);
                    } else if (i == 3) {
                        IHdmiConnectionCallback asInterface = IHdmiConnectionCallback.Stub.asInterface(parcel.readStrongBinder());
                        parcel.enforceNoDataAvail();
                        setCallback(asInterface);
                        parcel2.writeNoException();
                    } else if (i == 4) {
                        byte readByte = parcel.readByte();
                        int readInt2 = parcel.readInt();
                        parcel.enforceNoDataAvail();
                        setHpdSignal(readByte, readInt2);
                        parcel2.writeNoException();
                    } else if (i == 5) {
                        int readInt3 = parcel.readInt();
                        parcel.enforceNoDataAvail();
                        byte hpdSignal = getHpdSignal(readInt3);
                        parcel2.writeNoException();
                        parcel2.writeByte(hpdSignal);
                    } else {
                        return super.onTransact(i, parcel, parcel2, i2);
                    }
                    return true;
            }
        }

        /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
        private static class Proxy implements IHdmiConnection {
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
                return IHdmiConnection.DESCRIPTOR;
            }

            @Override // android.hardware.tv.hdmi.connection.IHdmiConnection
            public HdmiPortInfo[] getPortInfo() throws RemoteException {
                Parcel obtain = Parcel.obtain(asBinder());
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(IHdmiConnection.DESCRIPTOR);
                    if (!this.mRemote.transact(1, obtain, obtain2, 0)) {
                        throw new RemoteException("Method getPortInfo is unimplemented.");
                    }
                    obtain2.readException();
                    return (HdmiPortInfo[]) obtain2.createTypedArray(HdmiPortInfo.CREATOR);
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // android.hardware.tv.hdmi.connection.IHdmiConnection
            public boolean isConnected(int i) throws RemoteException {
                Parcel obtain = Parcel.obtain(asBinder());
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(IHdmiConnection.DESCRIPTOR);
                    obtain.writeInt(i);
                    if (!this.mRemote.transact(2, obtain, obtain2, 0)) {
                        throw new RemoteException("Method isConnected is unimplemented.");
                    }
                    obtain2.readException();
                    return obtain2.readBoolean();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // android.hardware.tv.hdmi.connection.IHdmiConnection
            public void setCallback(IHdmiConnectionCallback iHdmiConnectionCallback) throws RemoteException {
                Parcel obtain = Parcel.obtain(asBinder());
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(IHdmiConnection.DESCRIPTOR);
                    obtain.writeStrongInterface(iHdmiConnectionCallback);
                    if (!this.mRemote.transact(3, obtain, obtain2, 0)) {
                        throw new RemoteException("Method setCallback is unimplemented.");
                    }
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // android.hardware.tv.hdmi.connection.IHdmiConnection
            public void setHpdSignal(byte b, int i) throws RemoteException {
                Parcel obtain = Parcel.obtain(asBinder());
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(IHdmiConnection.DESCRIPTOR);
                    obtain.writeByte(b);
                    obtain.writeInt(i);
                    if (!this.mRemote.transact(4, obtain, obtain2, 0)) {
                        throw new RemoteException("Method setHpdSignal is unimplemented.");
                    }
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // android.hardware.tv.hdmi.connection.IHdmiConnection
            public byte getHpdSignal(int i) throws RemoteException {
                Parcel obtain = Parcel.obtain(asBinder());
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(IHdmiConnection.DESCRIPTOR);
                    obtain.writeInt(i);
                    if (!this.mRemote.transact(5, obtain, obtain2, 0)) {
                        throw new RemoteException("Method getHpdSignal is unimplemented.");
                    }
                    obtain2.readException();
                    return obtain2.readByte();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // android.hardware.tv.hdmi.connection.IHdmiConnection
            public int getInterfaceVersion() throws RemoteException {
                if (this.mCachedVersion == -1) {
                    Parcel obtain = Parcel.obtain(asBinder());
                    Parcel obtain2 = Parcel.obtain();
                    try {
                        obtain.writeInterfaceToken(IHdmiConnection.DESCRIPTOR);
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

            @Override // android.hardware.tv.hdmi.connection.IHdmiConnection
            public synchronized String getInterfaceHash() throws RemoteException {
                if ("-1".equals(this.mCachedHash)) {
                    Parcel obtain = Parcel.obtain(asBinder());
                    Parcel obtain2 = Parcel.obtain();
                    try {
                        obtain.writeInterfaceToken(IHdmiConnection.DESCRIPTOR);
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
