package android.hardware.security.keymint;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public interface IRemotelyProvisionedComponent extends IInterface {
    public static final String DESCRIPTOR = "android$hardware$security$keymint$IRemotelyProvisionedComponent".replace('$', '.');
    public static final String HASH = "7d14edbfab5c490efa407ba55fa80614bb48ae8e";
    public static final int STATUS_FAILED = 1;
    public static final int STATUS_INVALID_EEK = 5;
    public static final int STATUS_INVALID_MAC = 2;
    public static final int STATUS_PRODUCTION_KEY_IN_TEST_REQUEST = 3;
    public static final int STATUS_REMOVED = 6;
    public static final int STATUS_TEST_KEY_IN_PRODUCTION_REQUEST = 4;
    public static final int VERSION = 3;

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public static class Default implements IRemotelyProvisionedComponent {
        @Override // android.os.IInterface
        public IBinder asBinder() {
            return null;
        }

        @Override // android.hardware.security.keymint.IRemotelyProvisionedComponent
        public byte[] generateCertificateRequest(boolean z, MacedPublicKey[] macedPublicKeyArr, byte[] bArr, byte[] bArr2, DeviceInfo deviceInfo, ProtectedData protectedData) throws RemoteException {
            return null;
        }

        @Override // android.hardware.security.keymint.IRemotelyProvisionedComponent
        public byte[] generateCertificateRequestV2(MacedPublicKey[] macedPublicKeyArr, byte[] bArr) throws RemoteException {
            return null;
        }

        @Override // android.hardware.security.keymint.IRemotelyProvisionedComponent
        public byte[] generateEcdsaP256KeyPair(boolean z, MacedPublicKey macedPublicKey) throws RemoteException {
            return null;
        }

        @Override // android.hardware.security.keymint.IRemotelyProvisionedComponent
        public RpcHardwareInfo getHardwareInfo() throws RemoteException {
            return null;
        }

        @Override // android.hardware.security.keymint.IRemotelyProvisionedComponent
        public String getInterfaceHash() {
            return "";
        }

        @Override // android.hardware.security.keymint.IRemotelyProvisionedComponent
        public int getInterfaceVersion() {
            return 0;
        }
    }

    byte[] generateCertificateRequest(boolean z, MacedPublicKey[] macedPublicKeyArr, byte[] bArr, byte[] bArr2, DeviceInfo deviceInfo, ProtectedData protectedData) throws RemoteException;

    byte[] generateCertificateRequestV2(MacedPublicKey[] macedPublicKeyArr, byte[] bArr) throws RemoteException;

    byte[] generateEcdsaP256KeyPair(boolean z, MacedPublicKey macedPublicKey) throws RemoteException;

    RpcHardwareInfo getHardwareInfo() throws RemoteException;

    String getInterfaceHash() throws RemoteException;

    int getInterfaceVersion() throws RemoteException;

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public static abstract class Stub extends Binder implements IRemotelyProvisionedComponent {
        static final int TRANSACTION_generateCertificateRequest = 3;
        static final int TRANSACTION_generateCertificateRequestV2 = 4;
        static final int TRANSACTION_generateEcdsaP256KeyPair = 2;
        static final int TRANSACTION_getHardwareInfo = 1;
        static final int TRANSACTION_getInterfaceHash = 16777214;
        static final int TRANSACTION_getInterfaceVersion = 16777215;

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return this;
        }

        public Stub() {
            markVintfStability();
            attachInterface(this, IRemotelyProvisionedComponent.DESCRIPTOR);
        }

        public static IRemotelyProvisionedComponent asInterface(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface queryLocalInterface = iBinder.queryLocalInterface(IRemotelyProvisionedComponent.DESCRIPTOR);
            if (queryLocalInterface != null && (queryLocalInterface instanceof IRemotelyProvisionedComponent)) {
                return (IRemotelyProvisionedComponent) queryLocalInterface;
            }
            return new Proxy(iBinder);
        }

        @Override // android.os.Binder
        public boolean onTransact(int i, Parcel parcel, Parcel parcel2, int i2) throws RemoteException {
            String str = IRemotelyProvisionedComponent.DESCRIPTOR;
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
                        RpcHardwareInfo hardwareInfo = getHardwareInfo();
                        parcel2.writeNoException();
                        parcel2.writeTypedObject(hardwareInfo, 1);
                    } else if (i == 2) {
                        boolean readBoolean = parcel.readBoolean();
                        MacedPublicKey macedPublicKey = new MacedPublicKey();
                        parcel.enforceNoDataAvail();
                        byte[] generateEcdsaP256KeyPair = generateEcdsaP256KeyPair(readBoolean, macedPublicKey);
                        parcel2.writeNoException();
                        parcel2.writeByteArray(generateEcdsaP256KeyPair);
                        parcel2.writeTypedObject(macedPublicKey, 1);
                    } else if (i == 3) {
                        boolean readBoolean2 = parcel.readBoolean();
                        MacedPublicKey[] macedPublicKeyArr = (MacedPublicKey[]) parcel.createTypedArray(MacedPublicKey.CREATOR);
                        byte[] createByteArray = parcel.createByteArray();
                        byte[] createByteArray2 = parcel.createByteArray();
                        DeviceInfo deviceInfo = new DeviceInfo();
                        ProtectedData protectedData = new ProtectedData();
                        parcel.enforceNoDataAvail();
                        byte[] generateCertificateRequest = generateCertificateRequest(readBoolean2, macedPublicKeyArr, createByteArray, createByteArray2, deviceInfo, protectedData);
                        parcel2.writeNoException();
                        parcel2.writeByteArray(generateCertificateRequest);
                        parcel2.writeTypedObject(deviceInfo, 1);
                        parcel2.writeTypedObject(protectedData, 1);
                    } else if (i == 4) {
                        MacedPublicKey[] macedPublicKeyArr2 = (MacedPublicKey[]) parcel.createTypedArray(MacedPublicKey.CREATOR);
                        byte[] createByteArray3 = parcel.createByteArray();
                        parcel.enforceNoDataAvail();
                        byte[] generateCertificateRequestV2 = generateCertificateRequestV2(macedPublicKeyArr2, createByteArray3);
                        parcel2.writeNoException();
                        parcel2.writeByteArray(generateCertificateRequestV2);
                    } else {
                        return super.onTransact(i, parcel, parcel2, i2);
                    }
                    return true;
            }
        }

        /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
        private static class Proxy implements IRemotelyProvisionedComponent {
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
                return IRemotelyProvisionedComponent.DESCRIPTOR;
            }

            @Override // android.hardware.security.keymint.IRemotelyProvisionedComponent
            public RpcHardwareInfo getHardwareInfo() throws RemoteException {
                Parcel obtain = Parcel.obtain(asBinder());
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(IRemotelyProvisionedComponent.DESCRIPTOR);
                    if (!this.mRemote.transact(1, obtain, obtain2, 0)) {
                        throw new RemoteException("Method getHardwareInfo is unimplemented.");
                    }
                    obtain2.readException();
                    return (RpcHardwareInfo) obtain2.readTypedObject(RpcHardwareInfo.CREATOR);
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // android.hardware.security.keymint.IRemotelyProvisionedComponent
            public byte[] generateEcdsaP256KeyPair(boolean z, MacedPublicKey macedPublicKey) throws RemoteException {
                Parcel obtain = Parcel.obtain(asBinder());
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(IRemotelyProvisionedComponent.DESCRIPTOR);
                    obtain.writeBoolean(z);
                    if (!this.mRemote.transact(2, obtain, obtain2, 0)) {
                        throw new RemoteException("Method generateEcdsaP256KeyPair is unimplemented.");
                    }
                    obtain2.readException();
                    byte[] createByteArray = obtain2.createByteArray();
                    if (obtain2.readInt() != 0) {
                        macedPublicKey.readFromParcel(obtain2);
                    }
                    return createByteArray;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // android.hardware.security.keymint.IRemotelyProvisionedComponent
            public byte[] generateCertificateRequest(boolean z, MacedPublicKey[] macedPublicKeyArr, byte[] bArr, byte[] bArr2, DeviceInfo deviceInfo, ProtectedData protectedData) throws RemoteException {
                Parcel obtain = Parcel.obtain(asBinder());
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(IRemotelyProvisionedComponent.DESCRIPTOR);
                    obtain.writeBoolean(z);
                    obtain.writeTypedArray(macedPublicKeyArr, 0);
                    obtain.writeByteArray(bArr);
                    obtain.writeByteArray(bArr2);
                    if (!this.mRemote.transact(3, obtain, obtain2, 0)) {
                        throw new RemoteException("Method generateCertificateRequest is unimplemented.");
                    }
                    obtain2.readException();
                    byte[] createByteArray = obtain2.createByteArray();
                    if (obtain2.readInt() != 0) {
                        deviceInfo.readFromParcel(obtain2);
                    }
                    if (obtain2.readInt() != 0) {
                        protectedData.readFromParcel(obtain2);
                    }
                    return createByteArray;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // android.hardware.security.keymint.IRemotelyProvisionedComponent
            public byte[] generateCertificateRequestV2(MacedPublicKey[] macedPublicKeyArr, byte[] bArr) throws RemoteException {
                Parcel obtain = Parcel.obtain(asBinder());
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(IRemotelyProvisionedComponent.DESCRIPTOR);
                    obtain.writeTypedArray(macedPublicKeyArr, 0);
                    obtain.writeByteArray(bArr);
                    if (!this.mRemote.transact(4, obtain, obtain2, 0)) {
                        throw new RemoteException("Method generateCertificateRequestV2 is unimplemented.");
                    }
                    obtain2.readException();
                    return obtain2.createByteArray();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // android.hardware.security.keymint.IRemotelyProvisionedComponent
            public int getInterfaceVersion() throws RemoteException {
                if (this.mCachedVersion == -1) {
                    Parcel obtain = Parcel.obtain(asBinder());
                    Parcel obtain2 = Parcel.obtain();
                    try {
                        obtain.writeInterfaceToken(IRemotelyProvisionedComponent.DESCRIPTOR);
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

            @Override // android.hardware.security.keymint.IRemotelyProvisionedComponent
            public synchronized String getInterfaceHash() throws RemoteException {
                if ("-1".equals(this.mCachedHash)) {
                    Parcel obtain = Parcel.obtain(asBinder());
                    Parcel obtain2 = Parcel.obtain();
                    try {
                        obtain.writeInterfaceToken(IRemotelyProvisionedComponent.DESCRIPTOR);
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
