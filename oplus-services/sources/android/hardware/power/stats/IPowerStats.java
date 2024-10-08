package android.hardware.power.stats;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public interface IPowerStats extends IInterface {
    public static final String DESCRIPTOR = "android$hardware$power$stats$IPowerStats".replace('$', '.');
    public static final String HASH = "c3e113101b731c666717eb579492efa287a8f529";
    public static final int VERSION = 2;

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public static class Default implements IPowerStats {
        @Override // android.os.IInterface
        public IBinder asBinder() {
            return null;
        }

        @Override // android.hardware.power.stats.IPowerStats
        public EnergyConsumerResult[] getEnergyConsumed(int[] iArr) throws RemoteException {
            return null;
        }

        @Override // android.hardware.power.stats.IPowerStats
        public EnergyConsumer[] getEnergyConsumerInfo() throws RemoteException {
            return null;
        }

        @Override // android.hardware.power.stats.IPowerStats
        public Channel[] getEnergyMeterInfo() throws RemoteException {
            return null;
        }

        @Override // android.hardware.power.stats.IPowerStats
        public String getInterfaceHash() {
            return "";
        }

        @Override // android.hardware.power.stats.IPowerStats
        public int getInterfaceVersion() {
            return 0;
        }

        @Override // android.hardware.power.stats.IPowerStats
        public PowerEntity[] getPowerEntityInfo() throws RemoteException {
            return null;
        }

        @Override // android.hardware.power.stats.IPowerStats
        public StateResidencyResult[] getStateResidency(int[] iArr) throws RemoteException {
            return null;
        }

        @Override // android.hardware.power.stats.IPowerStats
        public EnergyMeasurement[] readEnergyMeter(int[] iArr) throws RemoteException {
            return null;
        }
    }

    EnergyConsumerResult[] getEnergyConsumed(int[] iArr) throws RemoteException;

    EnergyConsumer[] getEnergyConsumerInfo() throws RemoteException;

    Channel[] getEnergyMeterInfo() throws RemoteException;

    String getInterfaceHash() throws RemoteException;

    int getInterfaceVersion() throws RemoteException;

    PowerEntity[] getPowerEntityInfo() throws RemoteException;

    StateResidencyResult[] getStateResidency(int[] iArr) throws RemoteException;

    EnergyMeasurement[] readEnergyMeter(int[] iArr) throws RemoteException;

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public static abstract class Stub extends Binder implements IPowerStats {
        static final int TRANSACTION_getEnergyConsumed = 4;
        static final int TRANSACTION_getEnergyConsumerInfo = 3;
        static final int TRANSACTION_getEnergyMeterInfo = 5;
        static final int TRANSACTION_getInterfaceHash = 16777214;
        static final int TRANSACTION_getInterfaceVersion = 16777215;
        static final int TRANSACTION_getPowerEntityInfo = 1;
        static final int TRANSACTION_getStateResidency = 2;
        static final int TRANSACTION_readEnergyMeter = 6;

        public static String getDefaultTransactionName(int i) {
            switch (i) {
                case 1:
                    return "getPowerEntityInfo";
                case 2:
                    return "getStateResidency";
                case 3:
                    return "getEnergyConsumerInfo";
                case 4:
                    return "getEnergyConsumed";
                case 5:
                    return "getEnergyMeterInfo";
                case 6:
                    return "readEnergyMeter";
                default:
                    switch (i) {
                        case TRANSACTION_getInterfaceHash /* 16777214 */:
                            return "getInterfaceHash";
                        case 16777215:
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
            attachInterface(this, IPowerStats.DESCRIPTOR);
        }

        public static IPowerStats asInterface(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface queryLocalInterface = iBinder.queryLocalInterface(IPowerStats.DESCRIPTOR);
            if (queryLocalInterface != null && (queryLocalInterface instanceof IPowerStats)) {
                return (IPowerStats) queryLocalInterface;
            }
            return new Proxy(iBinder);
        }

        public String getTransactionName(int i) {
            return getDefaultTransactionName(i);
        }

        @Override // android.os.Binder
        public boolean onTransact(int i, Parcel parcel, Parcel parcel2, int i2) throws RemoteException {
            String str = IPowerStats.DESCRIPTOR;
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
                            PowerEntity[] powerEntityInfo = getPowerEntityInfo();
                            parcel2.writeNoException();
                            parcel2.writeTypedArray(powerEntityInfo, 1);
                            return true;
                        case 2:
                            int[] createIntArray = parcel.createIntArray();
                            parcel.enforceNoDataAvail();
                            StateResidencyResult[] stateResidency = getStateResidency(createIntArray);
                            parcel2.writeNoException();
                            parcel2.writeTypedArray(stateResidency, 1);
                            return true;
                        case 3:
                            EnergyConsumer[] energyConsumerInfo = getEnergyConsumerInfo();
                            parcel2.writeNoException();
                            parcel2.writeTypedArray(energyConsumerInfo, 1);
                            return true;
                        case 4:
                            int[] createIntArray2 = parcel.createIntArray();
                            parcel.enforceNoDataAvail();
                            EnergyConsumerResult[] energyConsumed = getEnergyConsumed(createIntArray2);
                            parcel2.writeNoException();
                            parcel2.writeTypedArray(energyConsumed, 1);
                            return true;
                        case 5:
                            Channel[] energyMeterInfo = getEnergyMeterInfo();
                            parcel2.writeNoException();
                            parcel2.writeTypedArray(energyMeterInfo, 1);
                            return true;
                        case 6:
                            int[] createIntArray3 = parcel.createIntArray();
                            parcel.enforceNoDataAvail();
                            EnergyMeasurement[] readEnergyMeter = readEnergyMeter(createIntArray3);
                            parcel2.writeNoException();
                            parcel2.writeTypedArray(readEnergyMeter, 1);
                            return true;
                        default:
                            return super.onTransact(i, parcel, parcel2, i2);
                    }
            }
        }

        /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
        private static class Proxy implements IPowerStats {
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
                return IPowerStats.DESCRIPTOR;
            }

            @Override // android.hardware.power.stats.IPowerStats
            public PowerEntity[] getPowerEntityInfo() throws RemoteException {
                Parcel obtain = Parcel.obtain(asBinder());
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(IPowerStats.DESCRIPTOR);
                    if (!this.mRemote.transact(1, obtain, obtain2, 0)) {
                        throw new RemoteException("Method getPowerEntityInfo is unimplemented.");
                    }
                    obtain2.readException();
                    return (PowerEntity[]) obtain2.createTypedArray(PowerEntity.CREATOR);
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // android.hardware.power.stats.IPowerStats
            public StateResidencyResult[] getStateResidency(int[] iArr) throws RemoteException {
                Parcel obtain = Parcel.obtain(asBinder());
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(IPowerStats.DESCRIPTOR);
                    obtain.writeIntArray(iArr);
                    if (!this.mRemote.transact(2, obtain, obtain2, 0)) {
                        throw new RemoteException("Method getStateResidency is unimplemented.");
                    }
                    obtain2.readException();
                    return (StateResidencyResult[]) obtain2.createTypedArray(StateResidencyResult.CREATOR);
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // android.hardware.power.stats.IPowerStats
            public EnergyConsumer[] getEnergyConsumerInfo() throws RemoteException {
                Parcel obtain = Parcel.obtain(asBinder());
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(IPowerStats.DESCRIPTOR);
                    if (!this.mRemote.transact(3, obtain, obtain2, 0)) {
                        throw new RemoteException("Method getEnergyConsumerInfo is unimplemented.");
                    }
                    obtain2.readException();
                    return (EnergyConsumer[]) obtain2.createTypedArray(EnergyConsumer.CREATOR);
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // android.hardware.power.stats.IPowerStats
            public EnergyConsumerResult[] getEnergyConsumed(int[] iArr) throws RemoteException {
                Parcel obtain = Parcel.obtain(asBinder());
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(IPowerStats.DESCRIPTOR);
                    obtain.writeIntArray(iArr);
                    if (!this.mRemote.transact(4, obtain, obtain2, 0)) {
                        throw new RemoteException("Method getEnergyConsumed is unimplemented.");
                    }
                    obtain2.readException();
                    return (EnergyConsumerResult[]) obtain2.createTypedArray(EnergyConsumerResult.CREATOR);
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // android.hardware.power.stats.IPowerStats
            public Channel[] getEnergyMeterInfo() throws RemoteException {
                Parcel obtain = Parcel.obtain(asBinder());
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(IPowerStats.DESCRIPTOR);
                    if (!this.mRemote.transact(5, obtain, obtain2, 0)) {
                        throw new RemoteException("Method getEnergyMeterInfo is unimplemented.");
                    }
                    obtain2.readException();
                    return (Channel[]) obtain2.createTypedArray(Channel.CREATOR);
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // android.hardware.power.stats.IPowerStats
            public EnergyMeasurement[] readEnergyMeter(int[] iArr) throws RemoteException {
                Parcel obtain = Parcel.obtain(asBinder());
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(IPowerStats.DESCRIPTOR);
                    obtain.writeIntArray(iArr);
                    if (!this.mRemote.transact(6, obtain, obtain2, 0)) {
                        throw new RemoteException("Method readEnergyMeter is unimplemented.");
                    }
                    obtain2.readException();
                    return (EnergyMeasurement[]) obtain2.createTypedArray(EnergyMeasurement.CREATOR);
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // android.hardware.power.stats.IPowerStats
            public int getInterfaceVersion() throws RemoteException {
                if (this.mCachedVersion == -1) {
                    Parcel obtain = Parcel.obtain(asBinder());
                    Parcel obtain2 = Parcel.obtain();
                    try {
                        obtain.writeInterfaceToken(IPowerStats.DESCRIPTOR);
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

            @Override // android.hardware.power.stats.IPowerStats
            public synchronized String getInterfaceHash() throws RemoteException {
                if ("-1".equals(this.mCachedHash)) {
                    Parcel obtain = Parcel.obtain(asBinder());
                    Parcel obtain2 = Parcel.obtain();
                    try {
                        obtain.writeInterfaceToken(IPowerStats.DESCRIPTOR);
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
