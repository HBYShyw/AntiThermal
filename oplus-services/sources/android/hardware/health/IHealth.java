package android.hardware.health;

import android.hardware.health.IHealthInfoCallback;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public interface IHealth extends IInterface {
    public static final String DESCRIPTOR = "android$hardware$health$IHealth".replace('$', '.');
    public static final String HASH = "d92c40b74b56341959d2ad70271145fdbd70b5c7";
    public static final int STATUS_CALLBACK_DIED = 4;
    public static final int STATUS_UNKNOWN = 2;
    public static final int VERSION = 2;

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public static class Default implements IHealth {
        @Override // android.os.IInterface
        public IBinder asBinder() {
            return null;
        }

        @Override // android.hardware.health.IHealth
        public BatteryHealthData getBatteryHealthData() throws RemoteException {
            return null;
        }

        @Override // android.hardware.health.IHealth
        public int getCapacity() throws RemoteException {
            return 0;
        }

        @Override // android.hardware.health.IHealth
        public int getChargeCounterUah() throws RemoteException {
            return 0;
        }

        @Override // android.hardware.health.IHealth
        public int getChargeStatus() throws RemoteException {
            return 0;
        }

        @Override // android.hardware.health.IHealth
        public int getChargingPolicy() throws RemoteException {
            return 0;
        }

        @Override // android.hardware.health.IHealth
        public int getCurrentAverageMicroamps() throws RemoteException {
            return 0;
        }

        @Override // android.hardware.health.IHealth
        public int getCurrentNowMicroamps() throws RemoteException {
            return 0;
        }

        @Override // android.hardware.health.IHealth
        public DiskStats[] getDiskStats() throws RemoteException {
            return null;
        }

        @Override // android.hardware.health.IHealth
        public long getEnergyCounterNwh() throws RemoteException {
            return 0L;
        }

        @Override // android.hardware.health.IHealth
        public HealthInfo getHealthInfo() throws RemoteException {
            return null;
        }

        @Override // android.hardware.health.IHealth
        public String getInterfaceHash() {
            return "";
        }

        @Override // android.hardware.health.IHealth
        public int getInterfaceVersion() {
            return 0;
        }

        @Override // android.hardware.health.IHealth
        public StorageInfo[] getStorageInfo() throws RemoteException {
            return null;
        }

        @Override // android.hardware.health.IHealth
        public void registerCallback(IHealthInfoCallback iHealthInfoCallback) throws RemoteException {
        }

        @Override // android.hardware.health.IHealth
        public void setChargingPolicy(int i) throws RemoteException {
        }

        @Override // android.hardware.health.IHealth
        public void unregisterCallback(IHealthInfoCallback iHealthInfoCallback) throws RemoteException {
        }

        @Override // android.hardware.health.IHealth
        public void update() throws RemoteException {
        }
    }

    BatteryHealthData getBatteryHealthData() throws RemoteException;

    int getCapacity() throws RemoteException;

    int getChargeCounterUah() throws RemoteException;

    int getChargeStatus() throws RemoteException;

    int getChargingPolicy() throws RemoteException;

    int getCurrentAverageMicroamps() throws RemoteException;

    int getCurrentNowMicroamps() throws RemoteException;

    DiskStats[] getDiskStats() throws RemoteException;

    long getEnergyCounterNwh() throws RemoteException;

    HealthInfo getHealthInfo() throws RemoteException;

    String getInterfaceHash() throws RemoteException;

    int getInterfaceVersion() throws RemoteException;

    StorageInfo[] getStorageInfo() throws RemoteException;

    void registerCallback(IHealthInfoCallback iHealthInfoCallback) throws RemoteException;

    void setChargingPolicy(int i) throws RemoteException;

    void unregisterCallback(IHealthInfoCallback iHealthInfoCallback) throws RemoteException;

    void update() throws RemoteException;

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public static abstract class Stub extends Binder implements IHealth {
        static final int TRANSACTION_getBatteryHealthData = 15;
        static final int TRANSACTION_getCapacity = 7;
        static final int TRANSACTION_getChargeCounterUah = 4;
        static final int TRANSACTION_getChargeStatus = 9;
        static final int TRANSACTION_getChargingPolicy = 14;
        static final int TRANSACTION_getCurrentAverageMicroamps = 6;
        static final int TRANSACTION_getCurrentNowMicroamps = 5;
        static final int TRANSACTION_getDiskStats = 11;
        static final int TRANSACTION_getEnergyCounterNwh = 8;
        static final int TRANSACTION_getHealthInfo = 12;
        static final int TRANSACTION_getInterfaceHash = 16777214;
        static final int TRANSACTION_getInterfaceVersion = 16777215;
        static final int TRANSACTION_getStorageInfo = 10;
        static final int TRANSACTION_registerCallback = 1;
        static final int TRANSACTION_setChargingPolicy = 13;
        static final int TRANSACTION_unregisterCallback = 2;
        static final int TRANSACTION_update = 3;

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return this;
        }

        public Stub() {
            markVintfStability();
            attachInterface(this, IHealth.DESCRIPTOR);
        }

        public static IHealth asInterface(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface queryLocalInterface = iBinder.queryLocalInterface(IHealth.DESCRIPTOR);
            if (queryLocalInterface != null && (queryLocalInterface instanceof IHealth)) {
                return (IHealth) queryLocalInterface;
            }
            return new Proxy(iBinder);
        }

        @Override // android.os.Binder
        public boolean onTransact(int i, Parcel parcel, Parcel parcel2, int i2) throws RemoteException {
            String str = IHealth.DESCRIPTOR;
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
                            IHealthInfoCallback asInterface = IHealthInfoCallback.Stub.asInterface(parcel.readStrongBinder());
                            parcel.enforceNoDataAvail();
                            registerCallback(asInterface);
                            parcel2.writeNoException();
                            return true;
                        case 2:
                            IHealthInfoCallback asInterface2 = IHealthInfoCallback.Stub.asInterface(parcel.readStrongBinder());
                            parcel.enforceNoDataAvail();
                            unregisterCallback(asInterface2);
                            parcel2.writeNoException();
                            return true;
                        case 3:
                            update();
                            parcel2.writeNoException();
                            return true;
                        case 4:
                            int chargeCounterUah = getChargeCounterUah();
                            parcel2.writeNoException();
                            parcel2.writeInt(chargeCounterUah);
                            return true;
                        case 5:
                            int currentNowMicroamps = getCurrentNowMicroamps();
                            parcel2.writeNoException();
                            parcel2.writeInt(currentNowMicroamps);
                            return true;
                        case 6:
                            int currentAverageMicroamps = getCurrentAverageMicroamps();
                            parcel2.writeNoException();
                            parcel2.writeInt(currentAverageMicroamps);
                            return true;
                        case 7:
                            int capacity = getCapacity();
                            parcel2.writeNoException();
                            parcel2.writeInt(capacity);
                            return true;
                        case 8:
                            long energyCounterNwh = getEnergyCounterNwh();
                            parcel2.writeNoException();
                            parcel2.writeLong(energyCounterNwh);
                            return true;
                        case 9:
                            int chargeStatus = getChargeStatus();
                            parcel2.writeNoException();
                            parcel2.writeInt(chargeStatus);
                            return true;
                        case 10:
                            StorageInfo[] storageInfo = getStorageInfo();
                            parcel2.writeNoException();
                            parcel2.writeTypedArray(storageInfo, 1);
                            return true;
                        case 11:
                            DiskStats[] diskStats = getDiskStats();
                            parcel2.writeNoException();
                            parcel2.writeTypedArray(diskStats, 1);
                            return true;
                        case 12:
                            HealthInfo healthInfo = getHealthInfo();
                            parcel2.writeNoException();
                            parcel2.writeTypedObject(healthInfo, 1);
                            return true;
                        case 13:
                            int readInt = parcel.readInt();
                            parcel.enforceNoDataAvail();
                            setChargingPolicy(readInt);
                            parcel2.writeNoException();
                            return true;
                        case 14:
                            int chargingPolicy = getChargingPolicy();
                            parcel2.writeNoException();
                            parcel2.writeInt(chargingPolicy);
                            return true;
                        case 15:
                            BatteryHealthData batteryHealthData = getBatteryHealthData();
                            parcel2.writeNoException();
                            parcel2.writeTypedObject(batteryHealthData, 1);
                            return true;
                        default:
                            return super.onTransact(i, parcel, parcel2, i2);
                    }
            }
        }

        /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
        private static class Proxy implements IHealth {
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
                return IHealth.DESCRIPTOR;
            }

            @Override // android.hardware.health.IHealth
            public void registerCallback(IHealthInfoCallback iHealthInfoCallback) throws RemoteException {
                Parcel obtain = Parcel.obtain(asBinder());
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(IHealth.DESCRIPTOR);
                    obtain.writeStrongInterface(iHealthInfoCallback);
                    if (!this.mRemote.transact(1, obtain, obtain2, 0)) {
                        throw new RemoteException("Method registerCallback is unimplemented.");
                    }
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // android.hardware.health.IHealth
            public void unregisterCallback(IHealthInfoCallback iHealthInfoCallback) throws RemoteException {
                Parcel obtain = Parcel.obtain(asBinder());
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(IHealth.DESCRIPTOR);
                    obtain.writeStrongInterface(iHealthInfoCallback);
                    if (!this.mRemote.transact(2, obtain, obtain2, 0)) {
                        throw new RemoteException("Method unregisterCallback is unimplemented.");
                    }
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // android.hardware.health.IHealth
            public void update() throws RemoteException {
                Parcel obtain = Parcel.obtain(asBinder());
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(IHealth.DESCRIPTOR);
                    if (!this.mRemote.transact(3, obtain, obtain2, 0)) {
                        throw new RemoteException("Method update is unimplemented.");
                    }
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // android.hardware.health.IHealth
            public int getChargeCounterUah() throws RemoteException {
                Parcel obtain = Parcel.obtain(asBinder());
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(IHealth.DESCRIPTOR);
                    if (!this.mRemote.transact(4, obtain, obtain2, 0)) {
                        throw new RemoteException("Method getChargeCounterUah is unimplemented.");
                    }
                    obtain2.readException();
                    return obtain2.readInt();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // android.hardware.health.IHealth
            public int getCurrentNowMicroamps() throws RemoteException {
                Parcel obtain = Parcel.obtain(asBinder());
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(IHealth.DESCRIPTOR);
                    if (!this.mRemote.transact(5, obtain, obtain2, 0)) {
                        throw new RemoteException("Method getCurrentNowMicroamps is unimplemented.");
                    }
                    obtain2.readException();
                    return obtain2.readInt();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // android.hardware.health.IHealth
            public int getCurrentAverageMicroamps() throws RemoteException {
                Parcel obtain = Parcel.obtain(asBinder());
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(IHealth.DESCRIPTOR);
                    if (!this.mRemote.transact(6, obtain, obtain2, 0)) {
                        throw new RemoteException("Method getCurrentAverageMicroamps is unimplemented.");
                    }
                    obtain2.readException();
                    return obtain2.readInt();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // android.hardware.health.IHealth
            public int getCapacity() throws RemoteException {
                Parcel obtain = Parcel.obtain(asBinder());
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(IHealth.DESCRIPTOR);
                    if (!this.mRemote.transact(7, obtain, obtain2, 0)) {
                        throw new RemoteException("Method getCapacity is unimplemented.");
                    }
                    obtain2.readException();
                    return obtain2.readInt();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // android.hardware.health.IHealth
            public long getEnergyCounterNwh() throws RemoteException {
                Parcel obtain = Parcel.obtain(asBinder());
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(IHealth.DESCRIPTOR);
                    if (!this.mRemote.transact(8, obtain, obtain2, 0)) {
                        throw new RemoteException("Method getEnergyCounterNwh is unimplemented.");
                    }
                    obtain2.readException();
                    return obtain2.readLong();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // android.hardware.health.IHealth
            public int getChargeStatus() throws RemoteException {
                Parcel obtain = Parcel.obtain(asBinder());
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(IHealth.DESCRIPTOR);
                    if (!this.mRemote.transact(9, obtain, obtain2, 0)) {
                        throw new RemoteException("Method getChargeStatus is unimplemented.");
                    }
                    obtain2.readException();
                    return obtain2.readInt();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // android.hardware.health.IHealth
            public StorageInfo[] getStorageInfo() throws RemoteException {
                Parcel obtain = Parcel.obtain(asBinder());
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(IHealth.DESCRIPTOR);
                    if (!this.mRemote.transact(10, obtain, obtain2, 0)) {
                        throw new RemoteException("Method getStorageInfo is unimplemented.");
                    }
                    obtain2.readException();
                    return (StorageInfo[]) obtain2.createTypedArray(StorageInfo.CREATOR);
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // android.hardware.health.IHealth
            public DiskStats[] getDiskStats() throws RemoteException {
                Parcel obtain = Parcel.obtain(asBinder());
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(IHealth.DESCRIPTOR);
                    if (!this.mRemote.transact(11, obtain, obtain2, 0)) {
                        throw new RemoteException("Method getDiskStats is unimplemented.");
                    }
                    obtain2.readException();
                    return (DiskStats[]) obtain2.createTypedArray(DiskStats.CREATOR);
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // android.hardware.health.IHealth
            public HealthInfo getHealthInfo() throws RemoteException {
                Parcel obtain = Parcel.obtain(asBinder());
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(IHealth.DESCRIPTOR);
                    if (!this.mRemote.transact(12, obtain, obtain2, 0)) {
                        throw new RemoteException("Method getHealthInfo is unimplemented.");
                    }
                    obtain2.readException();
                    return (HealthInfo) obtain2.readTypedObject(HealthInfo.CREATOR);
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // android.hardware.health.IHealth
            public void setChargingPolicy(int i) throws RemoteException {
                Parcel obtain = Parcel.obtain(asBinder());
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(IHealth.DESCRIPTOR);
                    obtain.writeInt(i);
                    if (!this.mRemote.transact(13, obtain, obtain2, 0)) {
                        throw new RemoteException("Method setChargingPolicy is unimplemented.");
                    }
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // android.hardware.health.IHealth
            public int getChargingPolicy() throws RemoteException {
                Parcel obtain = Parcel.obtain(asBinder());
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(IHealth.DESCRIPTOR);
                    if (!this.mRemote.transact(14, obtain, obtain2, 0)) {
                        throw new RemoteException("Method getChargingPolicy is unimplemented.");
                    }
                    obtain2.readException();
                    return obtain2.readInt();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // android.hardware.health.IHealth
            public BatteryHealthData getBatteryHealthData() throws RemoteException {
                Parcel obtain = Parcel.obtain(asBinder());
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(IHealth.DESCRIPTOR);
                    if (!this.mRemote.transact(15, obtain, obtain2, 0)) {
                        throw new RemoteException("Method getBatteryHealthData is unimplemented.");
                    }
                    obtain2.readException();
                    return (BatteryHealthData) obtain2.readTypedObject(BatteryHealthData.CREATOR);
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // android.hardware.health.IHealth
            public int getInterfaceVersion() throws RemoteException {
                if (this.mCachedVersion == -1) {
                    Parcel obtain = Parcel.obtain(asBinder());
                    Parcel obtain2 = Parcel.obtain();
                    try {
                        obtain.writeInterfaceToken(IHealth.DESCRIPTOR);
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

            @Override // android.hardware.health.IHealth
            public synchronized String getInterfaceHash() throws RemoteException {
                if ("-1".equals(this.mCachedHash)) {
                    Parcel obtain = Parcel.obtain(asBinder());
                    Parcel obtain2 = Parcel.obtain();
                    try {
                        obtain.writeInterfaceToken(IHealth.DESCRIPTOR);
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
