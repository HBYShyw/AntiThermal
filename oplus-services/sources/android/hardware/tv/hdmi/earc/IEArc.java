package android.hardware.tv.hdmi.earc;

import android.hardware.tv.hdmi.earc.IEArcCallback;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public interface IEArc extends IInterface {
    public static final String DESCRIPTOR = "android$hardware$tv$hdmi$earc$IEArc".replace('$', '.');
    public static final String HASH = "101230f18c7b8438921e517e80eea4ccc7c1e463";
    public static final int VERSION = 1;

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public static class Default implements IEArc {
        @Override // android.os.IInterface
        public IBinder asBinder() {
            return null;
        }

        @Override // android.hardware.tv.hdmi.earc.IEArc
        public String getInterfaceHash() {
            return "";
        }

        @Override // android.hardware.tv.hdmi.earc.IEArc
        public int getInterfaceVersion() {
            return 0;
        }

        @Override // android.hardware.tv.hdmi.earc.IEArc
        public byte[] getLastReportedAudioCapabilities(int i) throws RemoteException {
            return null;
        }

        @Override // android.hardware.tv.hdmi.earc.IEArc
        public byte getState(int i) throws RemoteException {
            return (byte) 0;
        }

        @Override // android.hardware.tv.hdmi.earc.IEArc
        public boolean isEArcEnabled() throws RemoteException {
            return false;
        }

        @Override // android.hardware.tv.hdmi.earc.IEArc
        public void setCallback(IEArcCallback iEArcCallback) throws RemoteException {
        }

        @Override // android.hardware.tv.hdmi.earc.IEArc
        public void setEArcEnabled(boolean z) throws RemoteException {
        }
    }

    String getInterfaceHash() throws RemoteException;

    int getInterfaceVersion() throws RemoteException;

    byte[] getLastReportedAudioCapabilities(int i) throws RemoteException;

    byte getState(int i) throws RemoteException;

    boolean isEArcEnabled() throws RemoteException;

    void setCallback(IEArcCallback iEArcCallback) throws RemoteException;

    void setEArcEnabled(boolean z) throws RemoteException;

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public static abstract class Stub extends Binder implements IEArc {
        static final int TRANSACTION_getInterfaceHash = 16777214;
        static final int TRANSACTION_getInterfaceVersion = 16777215;
        static final int TRANSACTION_getLastReportedAudioCapabilities = 5;
        static final int TRANSACTION_getState = 4;
        static final int TRANSACTION_isEArcEnabled = 2;
        static final int TRANSACTION_setCallback = 3;
        static final int TRANSACTION_setEArcEnabled = 1;

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return this;
        }

        public Stub() {
            markVintfStability();
            attachInterface(this, IEArc.DESCRIPTOR);
        }

        public static IEArc asInterface(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface queryLocalInterface = iBinder.queryLocalInterface(IEArc.DESCRIPTOR);
            if (queryLocalInterface != null && (queryLocalInterface instanceof IEArc)) {
                return (IEArc) queryLocalInterface;
            }
            return new Proxy(iBinder);
        }

        @Override // android.os.Binder
        public boolean onTransact(int i, Parcel parcel, Parcel parcel2, int i2) throws RemoteException {
            String str = IEArc.DESCRIPTOR;
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
                        boolean readBoolean = parcel.readBoolean();
                        parcel.enforceNoDataAvail();
                        setEArcEnabled(readBoolean);
                        parcel2.writeNoException();
                    } else if (i == 2) {
                        boolean isEArcEnabled = isEArcEnabled();
                        parcel2.writeNoException();
                        parcel2.writeBoolean(isEArcEnabled);
                    } else if (i == 3) {
                        IEArcCallback asInterface = IEArcCallback.Stub.asInterface(parcel.readStrongBinder());
                        parcel.enforceNoDataAvail();
                        setCallback(asInterface);
                        parcel2.writeNoException();
                    } else if (i == 4) {
                        int readInt = parcel.readInt();
                        parcel.enforceNoDataAvail();
                        byte state = getState(readInt);
                        parcel2.writeNoException();
                        parcel2.writeByte(state);
                    } else if (i == 5) {
                        int readInt2 = parcel.readInt();
                        parcel.enforceNoDataAvail();
                        byte[] lastReportedAudioCapabilities = getLastReportedAudioCapabilities(readInt2);
                        parcel2.writeNoException();
                        parcel2.writeByteArray(lastReportedAudioCapabilities);
                    } else {
                        return super.onTransact(i, parcel, parcel2, i2);
                    }
                    return true;
            }
        }

        /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
        private static class Proxy implements IEArc {
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
                return IEArc.DESCRIPTOR;
            }

            @Override // android.hardware.tv.hdmi.earc.IEArc
            public void setEArcEnabled(boolean z) throws RemoteException {
                Parcel obtain = Parcel.obtain(asBinder());
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(IEArc.DESCRIPTOR);
                    obtain.writeBoolean(z);
                    if (!this.mRemote.transact(1, obtain, obtain2, 0)) {
                        throw new RemoteException("Method setEArcEnabled is unimplemented.");
                    }
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // android.hardware.tv.hdmi.earc.IEArc
            public boolean isEArcEnabled() throws RemoteException {
                Parcel obtain = Parcel.obtain(asBinder());
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(IEArc.DESCRIPTOR);
                    if (!this.mRemote.transact(2, obtain, obtain2, 0)) {
                        throw new RemoteException("Method isEArcEnabled is unimplemented.");
                    }
                    obtain2.readException();
                    return obtain2.readBoolean();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // android.hardware.tv.hdmi.earc.IEArc
            public void setCallback(IEArcCallback iEArcCallback) throws RemoteException {
                Parcel obtain = Parcel.obtain(asBinder());
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(IEArc.DESCRIPTOR);
                    obtain.writeStrongInterface(iEArcCallback);
                    if (!this.mRemote.transact(3, obtain, obtain2, 0)) {
                        throw new RemoteException("Method setCallback is unimplemented.");
                    }
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // android.hardware.tv.hdmi.earc.IEArc
            public byte getState(int i) throws RemoteException {
                Parcel obtain = Parcel.obtain(asBinder());
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(IEArc.DESCRIPTOR);
                    obtain.writeInt(i);
                    if (!this.mRemote.transact(4, obtain, obtain2, 0)) {
                        throw new RemoteException("Method getState is unimplemented.");
                    }
                    obtain2.readException();
                    return obtain2.readByte();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // android.hardware.tv.hdmi.earc.IEArc
            public byte[] getLastReportedAudioCapabilities(int i) throws RemoteException {
                Parcel obtain = Parcel.obtain(asBinder());
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(IEArc.DESCRIPTOR);
                    obtain.writeInt(i);
                    if (!this.mRemote.transact(5, obtain, obtain2, 0)) {
                        throw new RemoteException("Method getLastReportedAudioCapabilities is unimplemented.");
                    }
                    obtain2.readException();
                    return obtain2.createByteArray();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // android.hardware.tv.hdmi.earc.IEArc
            public int getInterfaceVersion() throws RemoteException {
                if (this.mCachedVersion == -1) {
                    Parcel obtain = Parcel.obtain(asBinder());
                    Parcel obtain2 = Parcel.obtain();
                    try {
                        obtain.writeInterfaceToken(IEArc.DESCRIPTOR);
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

            @Override // android.hardware.tv.hdmi.earc.IEArc
            public synchronized String getInterfaceHash() throws RemoteException {
                if ("-1".equals(this.mCachedHash)) {
                    Parcel obtain = Parcel.obtain(asBinder());
                    Parcel obtain2 = Parcel.obtain();
                    try {
                        obtain.writeInterfaceToken(IEArc.DESCRIPTOR);
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
