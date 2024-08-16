package com.android.server.powerstats;

import android.hardware.power.stats.Channel;
import android.hardware.power.stats.EnergyConsumer;
import android.hardware.power.stats.EnergyConsumerResult;
import android.hardware.power.stats.EnergyMeasurement;
import android.hardware.power.stats.IPowerStats;
import android.hardware.power.stats.PowerEntity;
import android.hardware.power.stats.StateResidencyResult;
import android.os.Binder;
import android.os.IBinder;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.util.Slog;
import com.android.internal.annotations.GuardedBy;
import java.util.function.Supplier;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public final class PowerStatsHALWrapper {
    private static final boolean DEBUG = false;
    private static final String TAG = "PowerStatsHALWrapper";

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public interface IPowerStatsHALWrapper {
        EnergyConsumerResult[] getEnergyConsumed(int[] iArr);

        EnergyConsumer[] getEnergyConsumerInfo();

        Channel[] getEnergyMeterInfo();

        PowerEntity[] getPowerEntityInfo();

        StateResidencyResult[] getStateResidency(int[] iArr);

        boolean isInitialized();

        EnergyMeasurement[] readEnergyMeter(int[] iArr);
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public static final class PowerStatsHAL20WrapperImpl implements IPowerStatsHALWrapper {
        private static Supplier<IPowerStats> sVintfPowerStats;

        public PowerStatsHAL20WrapperImpl() {
            VintfHalCache vintfHalCache = new VintfHalCache();
            sVintfPowerStats = null;
            if (vintfHalCache.get() == null) {
                sVintfPowerStats = null;
            } else {
                sVintfPowerStats = vintfHalCache;
            }
        }

        @Override // com.android.server.powerstats.PowerStatsHALWrapper.IPowerStatsHALWrapper
        public PowerEntity[] getPowerEntityInfo() {
            Supplier<IPowerStats> supplier = sVintfPowerStats;
            if (supplier != null) {
                try {
                    return supplier.get().getPowerEntityInfo();
                } catch (RemoteException e) {
                    Slog.w(PowerStatsHALWrapper.TAG, "Failed to get power entity info: ", e);
                }
            }
            return null;
        }

        @Override // com.android.server.powerstats.PowerStatsHALWrapper.IPowerStatsHALWrapper
        public StateResidencyResult[] getStateResidency(int[] iArr) {
            Supplier<IPowerStats> supplier = sVintfPowerStats;
            if (supplier != null) {
                try {
                    return supplier.get().getStateResidency(iArr);
                } catch (RemoteException e) {
                    Slog.w(PowerStatsHALWrapper.TAG, "Failed to get state residency: ", e);
                }
            }
            return null;
        }

        @Override // com.android.server.powerstats.PowerStatsHALWrapper.IPowerStatsHALWrapper
        public EnergyConsumer[] getEnergyConsumerInfo() {
            Supplier<IPowerStats> supplier = sVintfPowerStats;
            if (supplier != null) {
                try {
                    return supplier.get().getEnergyConsumerInfo();
                } catch (RemoteException e) {
                    Slog.w(PowerStatsHALWrapper.TAG, "Failed to get energy consumer info: ", e);
                }
            }
            return null;
        }

        @Override // com.android.server.powerstats.PowerStatsHALWrapper.IPowerStatsHALWrapper
        public EnergyConsumerResult[] getEnergyConsumed(int[] iArr) {
            Supplier<IPowerStats> supplier = sVintfPowerStats;
            if (supplier != null) {
                try {
                    return supplier.get().getEnergyConsumed(iArr);
                } catch (RemoteException e) {
                    Slog.w(PowerStatsHALWrapper.TAG, "Failed to get energy consumer results: ", e);
                }
            }
            return null;
        }

        @Override // com.android.server.powerstats.PowerStatsHALWrapper.IPowerStatsHALWrapper
        public Channel[] getEnergyMeterInfo() {
            Supplier<IPowerStats> supplier = sVintfPowerStats;
            if (supplier != null) {
                try {
                    return supplier.get().getEnergyMeterInfo();
                } catch (RemoteException e) {
                    Slog.w(PowerStatsHALWrapper.TAG, "Failed to get energy meter info: ", e);
                }
            }
            return null;
        }

        @Override // com.android.server.powerstats.PowerStatsHALWrapper.IPowerStatsHALWrapper
        public EnergyMeasurement[] readEnergyMeter(int[] iArr) {
            Supplier<IPowerStats> supplier = sVintfPowerStats;
            if (supplier != null) {
                try {
                    return supplier.get().readEnergyMeter(iArr);
                } catch (RemoteException e) {
                    Slog.w(PowerStatsHALWrapper.TAG, "Failed to get energy measurements: ", e);
                }
            }
            return null;
        }

        @Override // com.android.server.powerstats.PowerStatsHALWrapper.IPowerStatsHALWrapper
        public boolean isInitialized() {
            return sVintfPowerStats != null;
        }
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public static final class PowerStatsHAL10WrapperImpl implements IPowerStatsHALWrapper {
        private boolean mIsInitialized;

        private static native Channel[] nativeGetEnergyMeterInfo();

        private static native PowerEntity[] nativeGetPowerEntityInfo();

        private static native StateResidencyResult[] nativeGetStateResidency(int[] iArr);

        private static native boolean nativeInit();

        private static native EnergyMeasurement[] nativeReadEnergyMeters(int[] iArr);

        @Override // com.android.server.powerstats.PowerStatsHALWrapper.IPowerStatsHALWrapper
        public EnergyConsumerResult[] getEnergyConsumed(int[] iArr) {
            return new EnergyConsumerResult[0];
        }

        @Override // com.android.server.powerstats.PowerStatsHALWrapper.IPowerStatsHALWrapper
        public EnergyConsumer[] getEnergyConsumerInfo() {
            return new EnergyConsumer[0];
        }

        public PowerStatsHAL10WrapperImpl() {
            if (nativeInit()) {
                this.mIsInitialized = true;
            } else {
                this.mIsInitialized = false;
            }
        }

        @Override // com.android.server.powerstats.PowerStatsHALWrapper.IPowerStatsHALWrapper
        public PowerEntity[] getPowerEntityInfo() {
            return nativeGetPowerEntityInfo();
        }

        @Override // com.android.server.powerstats.PowerStatsHALWrapper.IPowerStatsHALWrapper
        public StateResidencyResult[] getStateResidency(int[] iArr) {
            return nativeGetStateResidency(iArr);
        }

        @Override // com.android.server.powerstats.PowerStatsHALWrapper.IPowerStatsHALWrapper
        public Channel[] getEnergyMeterInfo() {
            return nativeGetEnergyMeterInfo();
        }

        @Override // com.android.server.powerstats.PowerStatsHALWrapper.IPowerStatsHALWrapper
        public EnergyMeasurement[] readEnergyMeter(int[] iArr) {
            return nativeReadEnergyMeters(iArr);
        }

        @Override // com.android.server.powerstats.PowerStatsHALWrapper.IPowerStatsHALWrapper
        public boolean isInitialized() {
            return this.mIsInitialized;
        }
    }

    public static IPowerStatsHALWrapper getPowerStatsHalImpl() {
        PowerStatsHAL20WrapperImpl powerStatsHAL20WrapperImpl = new PowerStatsHAL20WrapperImpl();
        return powerStatsHAL20WrapperImpl.isInitialized() ? powerStatsHAL20WrapperImpl : new PowerStatsHAL10WrapperImpl();
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    private static class VintfHalCache implements Supplier<IPowerStats>, IBinder.DeathRecipient {

        @GuardedBy({"this"})
        private IPowerStats mInstance;

        private VintfHalCache() {
            this.mInstance = null;
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // java.util.function.Supplier
        public synchronized IPowerStats get() {
            IBinder allowBlocking;
            if (this.mInstance == null && (allowBlocking = Binder.allowBlocking(ServiceManager.waitForDeclaredService("android.hardware.power.stats.IPowerStats/default"))) != null) {
                this.mInstance = IPowerStats.Stub.asInterface(allowBlocking);
                try {
                    allowBlocking.linkToDeath(this, 0);
                } catch (RemoteException unused) {
                    Slog.e(PowerStatsHALWrapper.TAG, "Unable to register DeathRecipient for " + this.mInstance);
                }
            }
            return this.mInstance;
        }

        @Override // android.os.IBinder.DeathRecipient
        public synchronized void binderDied() {
            Slog.w(PowerStatsHALWrapper.TAG, "PowerStats HAL died");
            this.mInstance = null;
        }
    }
}
