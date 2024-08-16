package android.frameworks.location.altitude;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public interface IAltitudeService extends IInterface {
    public static final String DESCRIPTOR = "android$frameworks$location$altitude$IAltitudeService".replace('$', '.');
    public static final String HASH = "763e0415cde10c922c590396b90bf622636470b1";
    public static final int VERSION = 1;

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public static class Default implements IAltitudeService {
        @Override // android.frameworks.location.altitude.IAltitudeService
        public AddMslAltitudeToLocationResponse addMslAltitudeToLocation(AddMslAltitudeToLocationRequest addMslAltitudeToLocationRequest) throws RemoteException {
            return null;
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return null;
        }

        @Override // android.frameworks.location.altitude.IAltitudeService
        public String getInterfaceHash() {
            return "";
        }

        @Override // android.frameworks.location.altitude.IAltitudeService
        public int getInterfaceVersion() {
            return 0;
        }
    }

    AddMslAltitudeToLocationResponse addMslAltitudeToLocation(AddMslAltitudeToLocationRequest addMslAltitudeToLocationRequest) throws RemoteException;

    String getInterfaceHash() throws RemoteException;

    int getInterfaceVersion() throws RemoteException;

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public static abstract class Stub extends Binder implements IAltitudeService {
        static final int TRANSACTION_addMslAltitudeToLocation = 1;
        static final int TRANSACTION_getInterfaceHash = 16777214;
        static final int TRANSACTION_getInterfaceVersion = 16777215;

        public static String getDefaultTransactionName(int i) {
            if (i == 1) {
                return "addMslAltitudeToLocation";
            }
            switch (i) {
                case TRANSACTION_getInterfaceHash /* 16777214 */:
                    return "getInterfaceHash";
                case 16777215:
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
            attachInterface(this, IAltitudeService.DESCRIPTOR);
        }

        public static IAltitudeService asInterface(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface queryLocalInterface = iBinder.queryLocalInterface(IAltitudeService.DESCRIPTOR);
            if (queryLocalInterface != null && (queryLocalInterface instanceof IAltitudeService)) {
                return (IAltitudeService) queryLocalInterface;
            }
            return new Proxy(iBinder);
        }

        public String getTransactionName(int i) {
            return getDefaultTransactionName(i);
        }

        @Override // android.os.Binder
        public boolean onTransact(int i, Parcel parcel, Parcel parcel2, int i2) throws RemoteException {
            String str = IAltitudeService.DESCRIPTOR;
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
                        AddMslAltitudeToLocationRequest addMslAltitudeToLocationRequest = (AddMslAltitudeToLocationRequest) parcel.readTypedObject(AddMslAltitudeToLocationRequest.CREATOR);
                        parcel.enforceNoDataAvail();
                        AddMslAltitudeToLocationResponse addMslAltitudeToLocation = addMslAltitudeToLocation(addMslAltitudeToLocationRequest);
                        parcel2.writeNoException();
                        parcel2.writeTypedObject(addMslAltitudeToLocation, 1);
                        return true;
                    }
                    return super.onTransact(i, parcel, parcel2, i2);
            }
        }

        /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
        private static class Proxy implements IAltitudeService {
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
                return IAltitudeService.DESCRIPTOR;
            }

            @Override // android.frameworks.location.altitude.IAltitudeService
            public AddMslAltitudeToLocationResponse addMslAltitudeToLocation(AddMslAltitudeToLocationRequest addMslAltitudeToLocationRequest) throws RemoteException {
                Parcel obtain = Parcel.obtain(asBinder());
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(IAltitudeService.DESCRIPTOR);
                    obtain.writeTypedObject(addMslAltitudeToLocationRequest, 0);
                    if (!this.mRemote.transact(1, obtain, obtain2, 0)) {
                        throw new RemoteException("Method addMslAltitudeToLocation is unimplemented.");
                    }
                    obtain2.readException();
                    return (AddMslAltitudeToLocationResponse) obtain2.readTypedObject(AddMslAltitudeToLocationResponse.CREATOR);
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // android.frameworks.location.altitude.IAltitudeService
            public int getInterfaceVersion() throws RemoteException {
                if (this.mCachedVersion == -1) {
                    Parcel obtain = Parcel.obtain(asBinder());
                    Parcel obtain2 = Parcel.obtain();
                    try {
                        obtain.writeInterfaceToken(IAltitudeService.DESCRIPTOR);
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

            @Override // android.frameworks.location.altitude.IAltitudeService
            public synchronized String getInterfaceHash() throws RemoteException {
                if ("-1".equals(this.mCachedHash)) {
                    Parcel obtain = Parcel.obtain(asBinder());
                    Parcel obtain2 = Parcel.obtain();
                    try {
                        obtain.writeInterfaceToken(IAltitudeService.DESCRIPTOR);
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
