package vendor.oplus.hardware.osense.client;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
public interface IOsenseAidlHalReporter extends IInterface {
    public static final String DESCRIPTOR = "vendor$oplus$hardware$osense$client$IOsenseAidlHalReporter".replace('$', '.');
    public static final String HASH = "ec186a0b12479cb8b761545e0f28fb272053f7bc";
    public static final int VERSION = 1;

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
    public static class Default implements IOsenseAidlHalReporter {
        @Override // android.os.IInterface
        public IBinder asBinder() {
            return null;
        }

        @Override // vendor.oplus.hardware.osense.client.IOsenseAidlHalReporter
        public int checkAccessPermission(String str) throws RemoteException {
            return 0;
        }

        @Override // vendor.oplus.hardware.osense.client.IOsenseAidlHalReporter
        public String getInterfaceHash() {
            return "";
        }

        @Override // vendor.oplus.hardware.osense.client.IOsenseAidlHalReporter
        public int getInterfaceVersion() {
            return 0;
        }

        @Override // vendor.oplus.hardware.osense.client.IOsenseAidlHalReporter
        public void osenseClrSceneAction(String str, long j) throws RemoteException {
        }

        @Override // vendor.oplus.hardware.osense.client.IOsenseAidlHalReporter
        public void osenseResetCtrlData(String str) throws RemoteException {
        }

        @Override // vendor.oplus.hardware.osense.client.IOsenseAidlHalReporter
        public void osenseSetCtrlData(String str, OsenseControlInfo osenseControlInfo) throws RemoteException {
        }

        @Override // vendor.oplus.hardware.osense.client.IOsenseAidlHalReporter
        public void osenseSetNotification(String str, OsenseAidlHalNotifyRequest osenseAidlHalNotifyRequest) throws RemoteException {
        }

        @Override // vendor.oplus.hardware.osense.client.IOsenseAidlHalReporter
        public void osenseSetSceneAction(String str, OsenseAidlHalSaRequest osenseAidlHalSaRequest) throws RemoteException {
        }
    }

    int checkAccessPermission(String str) throws RemoteException;

    String getInterfaceHash() throws RemoteException;

    int getInterfaceVersion() throws RemoteException;

    void osenseClrSceneAction(String str, long j) throws RemoteException;

    void osenseResetCtrlData(String str) throws RemoteException;

    void osenseSetCtrlData(String str, OsenseControlInfo osenseControlInfo) throws RemoteException;

    void osenseSetNotification(String str, OsenseAidlHalNotifyRequest osenseAidlHalNotifyRequest) throws RemoteException;

    void osenseSetSceneAction(String str, OsenseAidlHalSaRequest osenseAidlHalSaRequest) throws RemoteException;

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
    public static abstract class Stub extends Binder implements IOsenseAidlHalReporter {
        static final int TRANSACTION_checkAccessPermission = 1;
        static final int TRANSACTION_getInterfaceHash = 16777214;
        static final int TRANSACTION_getInterfaceVersion = 16777215;
        static final int TRANSACTION_osenseClrSceneAction = 2;
        static final int TRANSACTION_osenseResetCtrlData = 5;
        static final int TRANSACTION_osenseSetCtrlData = 6;
        static final int TRANSACTION_osenseSetNotification = 3;
        static final int TRANSACTION_osenseSetSceneAction = 4;

        public static String getDefaultTransactionName(int i) {
            switch (i) {
                case 1:
                    return "checkAccessPermission";
                case 2:
                    return "osenseClrSceneAction";
                case 3:
                    return "osenseSetNotification";
                case 4:
                    return "osenseSetSceneAction";
                case 5:
                    return "osenseResetCtrlData";
                case 6:
                    return "osenseSetCtrlData";
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
            attachInterface(this, IOsenseAidlHalReporter.DESCRIPTOR);
        }

        public static IOsenseAidlHalReporter asInterface(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface queryLocalInterface = iBinder.queryLocalInterface(IOsenseAidlHalReporter.DESCRIPTOR);
            if (queryLocalInterface != null && (queryLocalInterface instanceof IOsenseAidlHalReporter)) {
                return (IOsenseAidlHalReporter) queryLocalInterface;
            }
            return new Proxy(iBinder);
        }

        public String getTransactionName(int i) {
            return getDefaultTransactionName(i);
        }

        @Override // android.os.Binder
        public boolean onTransact(int i, Parcel parcel, Parcel parcel2, int i2) throws RemoteException {
            String str = IOsenseAidlHalReporter.DESCRIPTOR;
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
                            String readString = parcel.readString();
                            parcel.enforceNoDataAvail();
                            int checkAccessPermission = checkAccessPermission(readString);
                            parcel2.writeNoException();
                            parcel2.writeInt(checkAccessPermission);
                            return true;
                        case 2:
                            String readString2 = parcel.readString();
                            long readLong = parcel.readLong();
                            parcel.enforceNoDataAvail();
                            osenseClrSceneAction(readString2, readLong);
                            return true;
                        case 3:
                            String readString3 = parcel.readString();
                            OsenseAidlHalNotifyRequest osenseAidlHalNotifyRequest = (OsenseAidlHalNotifyRequest) parcel.readTypedObject(OsenseAidlHalNotifyRequest.CREATOR);
                            parcel.enforceNoDataAvail();
                            osenseSetNotification(readString3, osenseAidlHalNotifyRequest);
                            return true;
                        case 4:
                            String readString4 = parcel.readString();
                            OsenseAidlHalSaRequest osenseAidlHalSaRequest = (OsenseAidlHalSaRequest) parcel.readTypedObject(OsenseAidlHalSaRequest.CREATOR);
                            parcel.enforceNoDataAvail();
                            osenseSetSceneAction(readString4, osenseAidlHalSaRequest);
                            return true;
                        case 5:
                            String readString5 = parcel.readString();
                            parcel.enforceNoDataAvail();
                            osenseResetCtrlData(readString5);
                            return true;
                        case 6:
                            String readString6 = parcel.readString();
                            OsenseControlInfo osenseControlInfo = (OsenseControlInfo) parcel.readTypedObject(OsenseControlInfo.CREATOR);
                            parcel.enforceNoDataAvail();
                            osenseSetCtrlData(readString6, osenseControlInfo);
                            return true;
                        default:
                            return super.onTransact(i, parcel, parcel2, i2);
                    }
            }
        }

        /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
        private static class Proxy implements IOsenseAidlHalReporter {
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
                return IOsenseAidlHalReporter.DESCRIPTOR;
            }

            @Override // vendor.oplus.hardware.osense.client.IOsenseAidlHalReporter
            public int checkAccessPermission(String str) throws RemoteException {
                Parcel obtain = Parcel.obtain(asBinder());
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(IOsenseAidlHalReporter.DESCRIPTOR);
                    obtain.writeString(str);
                    if (!this.mRemote.transact(1, obtain, obtain2, 0)) {
                        throw new RemoteException("Method checkAccessPermission is unimplemented.");
                    }
                    obtain2.readException();
                    return obtain2.readInt();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // vendor.oplus.hardware.osense.client.IOsenseAidlHalReporter
            public void osenseClrSceneAction(String str, long j) throws RemoteException {
                Parcel obtain = Parcel.obtain(asBinder());
                try {
                    obtain.writeInterfaceToken(IOsenseAidlHalReporter.DESCRIPTOR);
                    obtain.writeString(str);
                    obtain.writeLong(j);
                    if (this.mRemote.transact(2, obtain, null, 1)) {
                    } else {
                        throw new RemoteException("Method osenseClrSceneAction is unimplemented.");
                    }
                } finally {
                    obtain.recycle();
                }
            }

            @Override // vendor.oplus.hardware.osense.client.IOsenseAidlHalReporter
            public void osenseSetNotification(String str, OsenseAidlHalNotifyRequest osenseAidlHalNotifyRequest) throws RemoteException {
                Parcel obtain = Parcel.obtain(asBinder());
                try {
                    obtain.writeInterfaceToken(IOsenseAidlHalReporter.DESCRIPTOR);
                    obtain.writeString(str);
                    obtain.writeTypedObject(osenseAidlHalNotifyRequest, 0);
                    if (this.mRemote.transact(3, obtain, null, 1)) {
                    } else {
                        throw new RemoteException("Method osenseSetNotification is unimplemented.");
                    }
                } finally {
                    obtain.recycle();
                }
            }

            @Override // vendor.oplus.hardware.osense.client.IOsenseAidlHalReporter
            public void osenseSetSceneAction(String str, OsenseAidlHalSaRequest osenseAidlHalSaRequest) throws RemoteException {
                Parcel obtain = Parcel.obtain(asBinder());
                try {
                    obtain.writeInterfaceToken(IOsenseAidlHalReporter.DESCRIPTOR);
                    obtain.writeString(str);
                    obtain.writeTypedObject(osenseAidlHalSaRequest, 0);
                    if (this.mRemote.transact(4, obtain, null, 1)) {
                    } else {
                        throw new RemoteException("Method osenseSetSceneAction is unimplemented.");
                    }
                } finally {
                    obtain.recycle();
                }
            }

            @Override // vendor.oplus.hardware.osense.client.IOsenseAidlHalReporter
            public void osenseResetCtrlData(String str) throws RemoteException {
                Parcel obtain = Parcel.obtain(asBinder());
                try {
                    obtain.writeInterfaceToken(IOsenseAidlHalReporter.DESCRIPTOR);
                    obtain.writeString(str);
                    if (this.mRemote.transact(5, obtain, null, 1)) {
                    } else {
                        throw new RemoteException("Method osenseResetCtrlData is unimplemented.");
                    }
                } finally {
                    obtain.recycle();
                }
            }

            @Override // vendor.oplus.hardware.osense.client.IOsenseAidlHalReporter
            public void osenseSetCtrlData(String str, OsenseControlInfo osenseControlInfo) throws RemoteException {
                Parcel obtain = Parcel.obtain(asBinder());
                try {
                    obtain.writeInterfaceToken(IOsenseAidlHalReporter.DESCRIPTOR);
                    obtain.writeString(str);
                    obtain.writeTypedObject(osenseControlInfo, 0);
                    if (this.mRemote.transact(6, obtain, null, 1)) {
                    } else {
                        throw new RemoteException("Method osenseSetCtrlData is unimplemented.");
                    }
                } finally {
                    obtain.recycle();
                }
            }

            @Override // vendor.oplus.hardware.osense.client.IOsenseAidlHalReporter
            public int getInterfaceVersion() throws RemoteException {
                if (this.mCachedVersion == -1) {
                    Parcel obtain = Parcel.obtain(asBinder());
                    Parcel obtain2 = Parcel.obtain();
                    try {
                        obtain.writeInterfaceToken(IOsenseAidlHalReporter.DESCRIPTOR);
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

            @Override // vendor.oplus.hardware.osense.client.IOsenseAidlHalReporter
            public synchronized String getInterfaceHash() throws RemoteException {
                if ("-1".equals(this.mCachedHash)) {
                    Parcel obtain = Parcel.obtain(asBinder());
                    Parcel obtain2 = Parcel.obtain();
                    try {
                        obtain.writeInterfaceToken(IOsenseAidlHalReporter.DESCRIPTOR);
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
