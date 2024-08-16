package android.hardware.broadcastradio;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public interface ITunerCallback extends IInterface {
    public static final String DESCRIPTOR = "android$hardware$broadcastradio$ITunerCallback".replace('$', '.');
    public static final String HASH = "3c864ddf392d28cfbf95849bedf0b753b81cc013";
    public static final int VERSION = 1;

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public static class Default implements ITunerCallback {
        @Override // android.os.IInterface
        public IBinder asBinder() {
            return null;
        }

        @Override // android.hardware.broadcastradio.ITunerCallback
        public String getInterfaceHash() {
            return "";
        }

        @Override // android.hardware.broadcastradio.ITunerCallback
        public int getInterfaceVersion() {
            return 0;
        }

        @Override // android.hardware.broadcastradio.ITunerCallback
        public void onAntennaStateChange(boolean z) throws RemoteException {
        }

        @Override // android.hardware.broadcastradio.ITunerCallback
        public void onConfigFlagUpdated(int i, boolean z) throws RemoteException {
        }

        @Override // android.hardware.broadcastradio.ITunerCallback
        public void onCurrentProgramInfoChanged(ProgramInfo programInfo) throws RemoteException {
        }

        @Override // android.hardware.broadcastradio.ITunerCallback
        public void onParametersUpdated(VendorKeyValue[] vendorKeyValueArr) throws RemoteException {
        }

        @Override // android.hardware.broadcastradio.ITunerCallback
        public void onProgramListUpdated(ProgramListChunk programListChunk) throws RemoteException {
        }

        @Override // android.hardware.broadcastradio.ITunerCallback
        public void onTuneFailed(int i, ProgramSelector programSelector) throws RemoteException {
        }
    }

    String getInterfaceHash() throws RemoteException;

    int getInterfaceVersion() throws RemoteException;

    void onAntennaStateChange(boolean z) throws RemoteException;

    void onConfigFlagUpdated(int i, boolean z) throws RemoteException;

    void onCurrentProgramInfoChanged(ProgramInfo programInfo) throws RemoteException;

    void onParametersUpdated(VendorKeyValue[] vendorKeyValueArr) throws RemoteException;

    void onProgramListUpdated(ProgramListChunk programListChunk) throws RemoteException;

    void onTuneFailed(int i, ProgramSelector programSelector) throws RemoteException;

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public static abstract class Stub extends Binder implements ITunerCallback {
        static final int TRANSACTION_getInterfaceHash = 16777214;
        static final int TRANSACTION_getInterfaceVersion = 16777215;
        static final int TRANSACTION_onAntennaStateChange = 4;
        static final int TRANSACTION_onConfigFlagUpdated = 5;
        static final int TRANSACTION_onCurrentProgramInfoChanged = 2;
        static final int TRANSACTION_onParametersUpdated = 6;
        static final int TRANSACTION_onProgramListUpdated = 3;
        static final int TRANSACTION_onTuneFailed = 1;

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return this;
        }

        public Stub() {
            markVintfStability();
            attachInterface(this, ITunerCallback.DESCRIPTOR);
        }

        public static ITunerCallback asInterface(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface queryLocalInterface = iBinder.queryLocalInterface(ITunerCallback.DESCRIPTOR);
            if (queryLocalInterface != null && (queryLocalInterface instanceof ITunerCallback)) {
                return (ITunerCallback) queryLocalInterface;
            }
            return new Proxy(iBinder);
        }

        @Override // android.os.Binder
        public boolean onTransact(int i, Parcel parcel, Parcel parcel2, int i2) throws RemoteException {
            String str = ITunerCallback.DESCRIPTOR;
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
                            ProgramSelector programSelector = (ProgramSelector) parcel.readTypedObject(ProgramSelector.CREATOR);
                            parcel.enforceNoDataAvail();
                            onTuneFailed(readInt, programSelector);
                            return true;
                        case 2:
                            ProgramInfo programInfo = (ProgramInfo) parcel.readTypedObject(ProgramInfo.CREATOR);
                            parcel.enforceNoDataAvail();
                            onCurrentProgramInfoChanged(programInfo);
                            return true;
                        case 3:
                            ProgramListChunk programListChunk = (ProgramListChunk) parcel.readTypedObject(ProgramListChunk.CREATOR);
                            parcel.enforceNoDataAvail();
                            onProgramListUpdated(programListChunk);
                            return true;
                        case 4:
                            boolean readBoolean = parcel.readBoolean();
                            parcel.enforceNoDataAvail();
                            onAntennaStateChange(readBoolean);
                            return true;
                        case 5:
                            int readInt2 = parcel.readInt();
                            boolean readBoolean2 = parcel.readBoolean();
                            parcel.enforceNoDataAvail();
                            onConfigFlagUpdated(readInt2, readBoolean2);
                            return true;
                        case 6:
                            VendorKeyValue[] vendorKeyValueArr = (VendorKeyValue[]) parcel.createTypedArray(VendorKeyValue.CREATOR);
                            parcel.enforceNoDataAvail();
                            onParametersUpdated(vendorKeyValueArr);
                            return true;
                        default:
                            return super.onTransact(i, parcel, parcel2, i2);
                    }
            }
        }

        /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
        private static class Proxy implements ITunerCallback {
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
                return ITunerCallback.DESCRIPTOR;
            }

            @Override // android.hardware.broadcastradio.ITunerCallback
            public void onTuneFailed(int i, ProgramSelector programSelector) throws RemoteException {
                Parcel obtain = Parcel.obtain(asBinder());
                try {
                    obtain.writeInterfaceToken(ITunerCallback.DESCRIPTOR);
                    obtain.writeInt(i);
                    obtain.writeTypedObject(programSelector, 0);
                    if (this.mRemote.transact(1, obtain, null, 1)) {
                    } else {
                        throw new RemoteException("Method onTuneFailed is unimplemented.");
                    }
                } finally {
                    obtain.recycle();
                }
            }

            @Override // android.hardware.broadcastradio.ITunerCallback
            public void onCurrentProgramInfoChanged(ProgramInfo programInfo) throws RemoteException {
                Parcel obtain = Parcel.obtain(asBinder());
                try {
                    obtain.writeInterfaceToken(ITunerCallback.DESCRIPTOR);
                    obtain.writeTypedObject(programInfo, 0);
                    if (this.mRemote.transact(2, obtain, null, 1)) {
                    } else {
                        throw new RemoteException("Method onCurrentProgramInfoChanged is unimplemented.");
                    }
                } finally {
                    obtain.recycle();
                }
            }

            @Override // android.hardware.broadcastradio.ITunerCallback
            public void onProgramListUpdated(ProgramListChunk programListChunk) throws RemoteException {
                Parcel obtain = Parcel.obtain(asBinder());
                try {
                    obtain.writeInterfaceToken(ITunerCallback.DESCRIPTOR);
                    obtain.writeTypedObject(programListChunk, 0);
                    if (this.mRemote.transact(3, obtain, null, 1)) {
                    } else {
                        throw new RemoteException("Method onProgramListUpdated is unimplemented.");
                    }
                } finally {
                    obtain.recycle();
                }
            }

            @Override // android.hardware.broadcastradio.ITunerCallback
            public void onAntennaStateChange(boolean z) throws RemoteException {
                Parcel obtain = Parcel.obtain(asBinder());
                try {
                    obtain.writeInterfaceToken(ITunerCallback.DESCRIPTOR);
                    obtain.writeBoolean(z);
                    if (this.mRemote.transact(4, obtain, null, 1)) {
                    } else {
                        throw new RemoteException("Method onAntennaStateChange is unimplemented.");
                    }
                } finally {
                    obtain.recycle();
                }
            }

            @Override // android.hardware.broadcastradio.ITunerCallback
            public void onConfigFlagUpdated(int i, boolean z) throws RemoteException {
                Parcel obtain = Parcel.obtain(asBinder());
                try {
                    obtain.writeInterfaceToken(ITunerCallback.DESCRIPTOR);
                    obtain.writeInt(i);
                    obtain.writeBoolean(z);
                    if (this.mRemote.transact(5, obtain, null, 1)) {
                    } else {
                        throw new RemoteException("Method onConfigFlagUpdated is unimplemented.");
                    }
                } finally {
                    obtain.recycle();
                }
            }

            @Override // android.hardware.broadcastradio.ITunerCallback
            public void onParametersUpdated(VendorKeyValue[] vendorKeyValueArr) throws RemoteException {
                Parcel obtain = Parcel.obtain(asBinder());
                try {
                    obtain.writeInterfaceToken(ITunerCallback.DESCRIPTOR);
                    obtain.writeTypedArray(vendorKeyValueArr, 0);
                    if (this.mRemote.transact(6, obtain, null, 1)) {
                    } else {
                        throw new RemoteException("Method onParametersUpdated is unimplemented.");
                    }
                } finally {
                    obtain.recycle();
                }
            }

            @Override // android.hardware.broadcastradio.ITunerCallback
            public int getInterfaceVersion() throws RemoteException {
                if (this.mCachedVersion == -1) {
                    Parcel obtain = Parcel.obtain(asBinder());
                    Parcel obtain2 = Parcel.obtain();
                    try {
                        obtain.writeInterfaceToken(ITunerCallback.DESCRIPTOR);
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

            @Override // android.hardware.broadcastradio.ITunerCallback
            public synchronized String getInterfaceHash() throws RemoteException {
                if ("-1".equals(this.mCachedHash)) {
                    Parcel obtain = Parcel.obtain(asBinder());
                    Parcel obtain2 = Parcel.obtain();
                    try {
                        obtain.writeInterfaceToken(ITunerCallback.DESCRIPTOR);
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
