package android.hardware.broadcastradio;

import android.hardware.broadcastradio.IAnnouncementListener;
import android.hardware.broadcastradio.ICloseHandle;
import android.hardware.broadcastradio.ITunerCallback;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public interface IBroadcastRadio extends IInterface {
    public static final int ANTENNA_STATE_CHANGE_TIMEOUT_MS = 100;
    public static final String DESCRIPTOR = "android$hardware$broadcastradio$IBroadcastRadio".replace('$', '.');
    public static final String HASH = "3c864ddf392d28cfbf95849bedf0b753b81cc013";
    public static final int INVALID_IMAGE = 0;
    public static final int LIST_COMPLETE_TIMEOUT_MS = 300000;
    public static final int TUNER_TIMEOUT_MS = 30000;
    public static final int VERSION = 1;

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public static class Default implements IBroadcastRadio {
        @Override // android.os.IInterface
        public IBinder asBinder() {
            return null;
        }

        @Override // android.hardware.broadcastradio.IBroadcastRadio
        public void cancel() throws RemoteException {
        }

        @Override // android.hardware.broadcastradio.IBroadcastRadio
        public AmFmRegionConfig getAmFmRegionConfig(boolean z) throws RemoteException {
            return null;
        }

        @Override // android.hardware.broadcastradio.IBroadcastRadio
        public DabTableEntry[] getDabRegionConfig() throws RemoteException {
            return null;
        }

        @Override // android.hardware.broadcastradio.IBroadcastRadio
        public byte[] getImage(int i) throws RemoteException {
            return null;
        }

        @Override // android.hardware.broadcastradio.IBroadcastRadio
        public String getInterfaceHash() {
            return "";
        }

        @Override // android.hardware.broadcastradio.IBroadcastRadio
        public int getInterfaceVersion() {
            return 0;
        }

        @Override // android.hardware.broadcastradio.IBroadcastRadio
        public VendorKeyValue[] getParameters(String[] strArr) throws RemoteException {
            return null;
        }

        @Override // android.hardware.broadcastradio.IBroadcastRadio
        public Properties getProperties() throws RemoteException {
            return null;
        }

        @Override // android.hardware.broadcastradio.IBroadcastRadio
        public boolean isConfigFlagSet(int i) throws RemoteException {
            return false;
        }

        @Override // android.hardware.broadcastradio.IBroadcastRadio
        public ICloseHandle registerAnnouncementListener(IAnnouncementListener iAnnouncementListener, byte[] bArr) throws RemoteException {
            return null;
        }

        @Override // android.hardware.broadcastradio.IBroadcastRadio
        public void seek(boolean z, boolean z2) throws RemoteException {
        }

        @Override // android.hardware.broadcastradio.IBroadcastRadio
        public void setConfigFlag(int i, boolean z) throws RemoteException {
        }

        @Override // android.hardware.broadcastradio.IBroadcastRadio
        public VendorKeyValue[] setParameters(VendorKeyValue[] vendorKeyValueArr) throws RemoteException {
            return null;
        }

        @Override // android.hardware.broadcastradio.IBroadcastRadio
        public void setTunerCallback(ITunerCallback iTunerCallback) throws RemoteException {
        }

        @Override // android.hardware.broadcastradio.IBroadcastRadio
        public void startProgramListUpdates(ProgramFilter programFilter) throws RemoteException {
        }

        @Override // android.hardware.broadcastradio.IBroadcastRadio
        public void step(boolean z) throws RemoteException {
        }

        @Override // android.hardware.broadcastradio.IBroadcastRadio
        public void stopProgramListUpdates() throws RemoteException {
        }

        @Override // android.hardware.broadcastradio.IBroadcastRadio
        public void tune(ProgramSelector programSelector) throws RemoteException {
        }

        @Override // android.hardware.broadcastradio.IBroadcastRadio
        public void unsetTunerCallback() throws RemoteException {
        }
    }

    void cancel() throws RemoteException;

    AmFmRegionConfig getAmFmRegionConfig(boolean z) throws RemoteException;

    DabTableEntry[] getDabRegionConfig() throws RemoteException;

    byte[] getImage(int i) throws RemoteException;

    String getInterfaceHash() throws RemoteException;

    int getInterfaceVersion() throws RemoteException;

    VendorKeyValue[] getParameters(String[] strArr) throws RemoteException;

    Properties getProperties() throws RemoteException;

    boolean isConfigFlagSet(int i) throws RemoteException;

    ICloseHandle registerAnnouncementListener(IAnnouncementListener iAnnouncementListener, byte[] bArr) throws RemoteException;

    void seek(boolean z, boolean z2) throws RemoteException;

    void setConfigFlag(int i, boolean z) throws RemoteException;

    VendorKeyValue[] setParameters(VendorKeyValue[] vendorKeyValueArr) throws RemoteException;

    void setTunerCallback(ITunerCallback iTunerCallback) throws RemoteException;

    void startProgramListUpdates(ProgramFilter programFilter) throws RemoteException;

    void step(boolean z) throws RemoteException;

    void stopProgramListUpdates() throws RemoteException;

    void tune(ProgramSelector programSelector) throws RemoteException;

    void unsetTunerCallback() throws RemoteException;

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public static abstract class Stub extends Binder implements IBroadcastRadio {
        static final int TRANSACTION_cancel = 9;
        static final int TRANSACTION_getAmFmRegionConfig = 2;
        static final int TRANSACTION_getDabRegionConfig = 3;
        static final int TRANSACTION_getImage = 16;
        static final int TRANSACTION_getInterfaceHash = 16777214;
        static final int TRANSACTION_getInterfaceVersion = 16777215;
        static final int TRANSACTION_getParameters = 15;
        static final int TRANSACTION_getProperties = 1;
        static final int TRANSACTION_isConfigFlagSet = 12;
        static final int TRANSACTION_registerAnnouncementListener = 17;
        static final int TRANSACTION_seek = 7;
        static final int TRANSACTION_setConfigFlag = 13;
        static final int TRANSACTION_setParameters = 14;
        static final int TRANSACTION_setTunerCallback = 4;
        static final int TRANSACTION_startProgramListUpdates = 10;
        static final int TRANSACTION_step = 8;
        static final int TRANSACTION_stopProgramListUpdates = 11;
        static final int TRANSACTION_tune = 6;
        static final int TRANSACTION_unsetTunerCallback = 5;

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return this;
        }

        public Stub() {
            markVintfStability();
            attachInterface(this, IBroadcastRadio.DESCRIPTOR);
        }

        public static IBroadcastRadio asInterface(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface queryLocalInterface = iBinder.queryLocalInterface(IBroadcastRadio.DESCRIPTOR);
            if (queryLocalInterface != null && (queryLocalInterface instanceof IBroadcastRadio)) {
                return (IBroadcastRadio) queryLocalInterface;
            }
            return new Proxy(iBinder);
        }

        @Override // android.os.Binder
        public boolean onTransact(int i, Parcel parcel, Parcel parcel2, int i2) throws RemoteException {
            String str = IBroadcastRadio.DESCRIPTOR;
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
                            Properties properties = getProperties();
                            parcel2.writeNoException();
                            parcel2.writeTypedObject(properties, 1);
                            return true;
                        case 2:
                            boolean readBoolean = parcel.readBoolean();
                            parcel.enforceNoDataAvail();
                            AmFmRegionConfig amFmRegionConfig = getAmFmRegionConfig(readBoolean);
                            parcel2.writeNoException();
                            parcel2.writeTypedObject(amFmRegionConfig, 1);
                            return true;
                        case 3:
                            DabTableEntry[] dabRegionConfig = getDabRegionConfig();
                            parcel2.writeNoException();
                            parcel2.writeTypedArray(dabRegionConfig, 1);
                            return true;
                        case 4:
                            ITunerCallback asInterface = ITunerCallback.Stub.asInterface(parcel.readStrongBinder());
                            parcel.enforceNoDataAvail();
                            setTunerCallback(asInterface);
                            parcel2.writeNoException();
                            return true;
                        case 5:
                            unsetTunerCallback();
                            parcel2.writeNoException();
                            return true;
                        case 6:
                            ProgramSelector programSelector = (ProgramSelector) parcel.readTypedObject(ProgramSelector.CREATOR);
                            parcel.enforceNoDataAvail();
                            tune(programSelector);
                            parcel2.writeNoException();
                            return true;
                        case 7:
                            boolean readBoolean2 = parcel.readBoolean();
                            boolean readBoolean3 = parcel.readBoolean();
                            parcel.enforceNoDataAvail();
                            seek(readBoolean2, readBoolean3);
                            parcel2.writeNoException();
                            return true;
                        case 8:
                            boolean readBoolean4 = parcel.readBoolean();
                            parcel.enforceNoDataAvail();
                            step(readBoolean4);
                            parcel2.writeNoException();
                            return true;
                        case 9:
                            cancel();
                            parcel2.writeNoException();
                            return true;
                        case 10:
                            ProgramFilter programFilter = (ProgramFilter) parcel.readTypedObject(ProgramFilter.CREATOR);
                            parcel.enforceNoDataAvail();
                            startProgramListUpdates(programFilter);
                            parcel2.writeNoException();
                            return true;
                        case 11:
                            stopProgramListUpdates();
                            parcel2.writeNoException();
                            return true;
                        case 12:
                            int readInt = parcel.readInt();
                            parcel.enforceNoDataAvail();
                            boolean isConfigFlagSet = isConfigFlagSet(readInt);
                            parcel2.writeNoException();
                            parcel2.writeBoolean(isConfigFlagSet);
                            return true;
                        case 13:
                            int readInt2 = parcel.readInt();
                            boolean readBoolean5 = parcel.readBoolean();
                            parcel.enforceNoDataAvail();
                            setConfigFlag(readInt2, readBoolean5);
                            parcel2.writeNoException();
                            return true;
                        case 14:
                            VendorKeyValue[] vendorKeyValueArr = (VendorKeyValue[]) parcel.createTypedArray(VendorKeyValue.CREATOR);
                            parcel.enforceNoDataAvail();
                            VendorKeyValue[] parameters = setParameters(vendorKeyValueArr);
                            parcel2.writeNoException();
                            parcel2.writeTypedArray(parameters, 1);
                            return true;
                        case 15:
                            String[] createStringArray = parcel.createStringArray();
                            parcel.enforceNoDataAvail();
                            VendorKeyValue[] parameters2 = getParameters(createStringArray);
                            parcel2.writeNoException();
                            parcel2.writeTypedArray(parameters2, 1);
                            return true;
                        case 16:
                            int readInt3 = parcel.readInt();
                            parcel.enforceNoDataAvail();
                            byte[] image = getImage(readInt3);
                            parcel2.writeNoException();
                            parcel2.writeByteArray(image);
                            return true;
                        case 17:
                            IAnnouncementListener asInterface2 = IAnnouncementListener.Stub.asInterface(parcel.readStrongBinder());
                            byte[] createByteArray = parcel.createByteArray();
                            parcel.enforceNoDataAvail();
                            ICloseHandle registerAnnouncementListener = registerAnnouncementListener(asInterface2, createByteArray);
                            parcel2.writeNoException();
                            parcel2.writeStrongInterface(registerAnnouncementListener);
                            return true;
                        default:
                            return super.onTransact(i, parcel, parcel2, i2);
                    }
            }
        }

        /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
        private static class Proxy implements IBroadcastRadio {
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
                return IBroadcastRadio.DESCRIPTOR;
            }

            @Override // android.hardware.broadcastradio.IBroadcastRadio
            public Properties getProperties() throws RemoteException {
                Parcel obtain = Parcel.obtain(asBinder());
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(IBroadcastRadio.DESCRIPTOR);
                    if (!this.mRemote.transact(1, obtain, obtain2, 0)) {
                        throw new RemoteException("Method getProperties is unimplemented.");
                    }
                    obtain2.readException();
                    return (Properties) obtain2.readTypedObject(Properties.CREATOR);
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // android.hardware.broadcastradio.IBroadcastRadio
            public AmFmRegionConfig getAmFmRegionConfig(boolean z) throws RemoteException {
                Parcel obtain = Parcel.obtain(asBinder());
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(IBroadcastRadio.DESCRIPTOR);
                    obtain.writeBoolean(z);
                    if (!this.mRemote.transact(2, obtain, obtain2, 0)) {
                        throw new RemoteException("Method getAmFmRegionConfig is unimplemented.");
                    }
                    obtain2.readException();
                    return (AmFmRegionConfig) obtain2.readTypedObject(AmFmRegionConfig.CREATOR);
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // android.hardware.broadcastradio.IBroadcastRadio
            public DabTableEntry[] getDabRegionConfig() throws RemoteException {
                Parcel obtain = Parcel.obtain(asBinder());
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(IBroadcastRadio.DESCRIPTOR);
                    if (!this.mRemote.transact(3, obtain, obtain2, 0)) {
                        throw new RemoteException("Method getDabRegionConfig is unimplemented.");
                    }
                    obtain2.readException();
                    return (DabTableEntry[]) obtain2.createTypedArray(DabTableEntry.CREATOR);
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // android.hardware.broadcastradio.IBroadcastRadio
            public void setTunerCallback(ITunerCallback iTunerCallback) throws RemoteException {
                Parcel obtain = Parcel.obtain(asBinder());
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(IBroadcastRadio.DESCRIPTOR);
                    obtain.writeStrongInterface(iTunerCallback);
                    if (!this.mRemote.transact(4, obtain, obtain2, 0)) {
                        throw new RemoteException("Method setTunerCallback is unimplemented.");
                    }
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // android.hardware.broadcastradio.IBroadcastRadio
            public void unsetTunerCallback() throws RemoteException {
                Parcel obtain = Parcel.obtain(asBinder());
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(IBroadcastRadio.DESCRIPTOR);
                    if (!this.mRemote.transact(5, obtain, obtain2, 0)) {
                        throw new RemoteException("Method unsetTunerCallback is unimplemented.");
                    }
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // android.hardware.broadcastradio.IBroadcastRadio
            public void tune(ProgramSelector programSelector) throws RemoteException {
                Parcel obtain = Parcel.obtain(asBinder());
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(IBroadcastRadio.DESCRIPTOR);
                    obtain.writeTypedObject(programSelector, 0);
                    if (!this.mRemote.transact(6, obtain, obtain2, 0)) {
                        throw new RemoteException("Method tune is unimplemented.");
                    }
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // android.hardware.broadcastradio.IBroadcastRadio
            public void seek(boolean z, boolean z2) throws RemoteException {
                Parcel obtain = Parcel.obtain(asBinder());
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(IBroadcastRadio.DESCRIPTOR);
                    obtain.writeBoolean(z);
                    obtain.writeBoolean(z2);
                    if (!this.mRemote.transact(7, obtain, obtain2, 0)) {
                        throw new RemoteException("Method seek is unimplemented.");
                    }
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // android.hardware.broadcastradio.IBroadcastRadio
            public void step(boolean z) throws RemoteException {
                Parcel obtain = Parcel.obtain(asBinder());
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(IBroadcastRadio.DESCRIPTOR);
                    obtain.writeBoolean(z);
                    if (!this.mRemote.transact(8, obtain, obtain2, 0)) {
                        throw new RemoteException("Method step is unimplemented.");
                    }
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // android.hardware.broadcastradio.IBroadcastRadio
            public void cancel() throws RemoteException {
                Parcel obtain = Parcel.obtain(asBinder());
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(IBroadcastRadio.DESCRIPTOR);
                    if (!this.mRemote.transact(9, obtain, obtain2, 0)) {
                        throw new RemoteException("Method cancel is unimplemented.");
                    }
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // android.hardware.broadcastradio.IBroadcastRadio
            public void startProgramListUpdates(ProgramFilter programFilter) throws RemoteException {
                Parcel obtain = Parcel.obtain(asBinder());
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(IBroadcastRadio.DESCRIPTOR);
                    obtain.writeTypedObject(programFilter, 0);
                    if (!this.mRemote.transact(10, obtain, obtain2, 0)) {
                        throw new RemoteException("Method startProgramListUpdates is unimplemented.");
                    }
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // android.hardware.broadcastradio.IBroadcastRadio
            public void stopProgramListUpdates() throws RemoteException {
                Parcel obtain = Parcel.obtain(asBinder());
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(IBroadcastRadio.DESCRIPTOR);
                    if (!this.mRemote.transact(11, obtain, obtain2, 0)) {
                        throw new RemoteException("Method stopProgramListUpdates is unimplemented.");
                    }
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // android.hardware.broadcastradio.IBroadcastRadio
            public boolean isConfigFlagSet(int i) throws RemoteException {
                Parcel obtain = Parcel.obtain(asBinder());
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(IBroadcastRadio.DESCRIPTOR);
                    obtain.writeInt(i);
                    if (!this.mRemote.transact(12, obtain, obtain2, 0)) {
                        throw new RemoteException("Method isConfigFlagSet is unimplemented.");
                    }
                    obtain2.readException();
                    return obtain2.readBoolean();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // android.hardware.broadcastradio.IBroadcastRadio
            public void setConfigFlag(int i, boolean z) throws RemoteException {
                Parcel obtain = Parcel.obtain(asBinder());
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(IBroadcastRadio.DESCRIPTOR);
                    obtain.writeInt(i);
                    obtain.writeBoolean(z);
                    if (!this.mRemote.transact(13, obtain, obtain2, 0)) {
                        throw new RemoteException("Method setConfigFlag is unimplemented.");
                    }
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // android.hardware.broadcastradio.IBroadcastRadio
            public VendorKeyValue[] setParameters(VendorKeyValue[] vendorKeyValueArr) throws RemoteException {
                Parcel obtain = Parcel.obtain(asBinder());
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(IBroadcastRadio.DESCRIPTOR);
                    obtain.writeTypedArray(vendorKeyValueArr, 0);
                    if (!this.mRemote.transact(14, obtain, obtain2, 0)) {
                        throw new RemoteException("Method setParameters is unimplemented.");
                    }
                    obtain2.readException();
                    return (VendorKeyValue[]) obtain2.createTypedArray(VendorKeyValue.CREATOR);
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // android.hardware.broadcastradio.IBroadcastRadio
            public VendorKeyValue[] getParameters(String[] strArr) throws RemoteException {
                Parcel obtain = Parcel.obtain(asBinder());
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(IBroadcastRadio.DESCRIPTOR);
                    obtain.writeStringArray(strArr);
                    if (!this.mRemote.transact(15, obtain, obtain2, 0)) {
                        throw new RemoteException("Method getParameters is unimplemented.");
                    }
                    obtain2.readException();
                    return (VendorKeyValue[]) obtain2.createTypedArray(VendorKeyValue.CREATOR);
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // android.hardware.broadcastradio.IBroadcastRadio
            public byte[] getImage(int i) throws RemoteException {
                Parcel obtain = Parcel.obtain(asBinder());
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(IBroadcastRadio.DESCRIPTOR);
                    obtain.writeInt(i);
                    if (!this.mRemote.transact(16, obtain, obtain2, 0)) {
                        throw new RemoteException("Method getImage is unimplemented.");
                    }
                    obtain2.readException();
                    return obtain2.createByteArray();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // android.hardware.broadcastradio.IBroadcastRadio
            public ICloseHandle registerAnnouncementListener(IAnnouncementListener iAnnouncementListener, byte[] bArr) throws RemoteException {
                Parcel obtain = Parcel.obtain(asBinder());
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(IBroadcastRadio.DESCRIPTOR);
                    obtain.writeStrongInterface(iAnnouncementListener);
                    obtain.writeByteArray(bArr);
                    if (!this.mRemote.transact(17, obtain, obtain2, 0)) {
                        throw new RemoteException("Method registerAnnouncementListener is unimplemented.");
                    }
                    obtain2.readException();
                    return ICloseHandle.Stub.asInterface(obtain2.readStrongBinder());
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // android.hardware.broadcastradio.IBroadcastRadio
            public int getInterfaceVersion() throws RemoteException {
                if (this.mCachedVersion == -1) {
                    Parcel obtain = Parcel.obtain(asBinder());
                    Parcel obtain2 = Parcel.obtain();
                    try {
                        obtain.writeInterfaceToken(IBroadcastRadio.DESCRIPTOR);
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

            @Override // android.hardware.broadcastradio.IBroadcastRadio
            public synchronized String getInterfaceHash() throws RemoteException {
                if ("-1".equals(this.mCachedHash)) {
                    Parcel obtain = Parcel.obtain(asBinder());
                    Parcel obtain2 = Parcel.obtain();
                    try {
                        obtain.writeInterfaceToken(IBroadcastRadio.DESCRIPTOR);
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
