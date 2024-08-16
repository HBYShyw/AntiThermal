package com.android.server.powerstats;

import android.content.Context;
import android.hardware.power.stats.Channel;
import android.hardware.power.stats.EnergyConsumer;
import android.hardware.power.stats.EnergyConsumerResult;
import android.hardware.power.stats.EnergyMeasurement;
import android.hardware.power.stats.PowerEntity;
import android.hardware.power.stats.StateResidencyResult;
import android.os.Binder;
import android.os.Environment;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.power.PowerStatsInternal;
import android.util.Slog;
import com.android.internal.annotations.GuardedBy;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.util.DumpUtils;
import com.android.internal.util.function.pooled.PooledLambda;
import com.android.server.SystemService;
import com.android.server.powerstats.PowerStatsHALWrapper;
import com.android.server.powerstats.ProtoStreamUtils;
import java.io.File;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public class PowerStatsService extends SystemService {
    private static final String DATA_STORAGE_SUBDIR = "powerstats";
    private static final int DATA_STORAGE_VERSION = 0;
    private static final boolean DEBUG = false;
    private static final String METER_CACHE_FILENAME = "meterCache";
    private static final String METER_FILENAME = "log.powerstats.meter.0";
    private static final String MODEL_CACHE_FILENAME = "modelCache";
    private static final String MODEL_FILENAME = "log.powerstats.model.0";
    private static final String RESIDENCY_CACHE_FILENAME = "residencyCache";
    private static final String RESIDENCY_FILENAME = "log.powerstats.residency.0";
    private static final String TAG = "PowerStatsService";
    private BatteryTrigger mBatteryTrigger;
    private Context mContext;
    private File mDataStoragePath;

    @GuardedBy({"this"})
    private EnergyConsumer[] mEnergyConsumers;
    private final Injector mInjector;

    @GuardedBy({"this"})
    private Looper mLooper;
    private PowerStatsInternal mPowerStatsInternal;
    private PowerStatsLogger mPowerStatsLogger;
    private StatsPullAtomCallbackImpl mPullAtomCallback;
    private TimerTrigger mTimerTrigger;

    /* JADX INFO: Access modifiers changed from: package-private */
    @VisibleForTesting
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public static class Injector {

        @GuardedBy({"this"})
        private PowerStatsHALWrapper.IPowerStatsHALWrapper mPowerStatsHALWrapper;

        String createMeterCacheFilename() {
            return PowerStatsService.METER_CACHE_FILENAME;
        }

        String createMeterFilename() {
            return PowerStatsService.METER_FILENAME;
        }

        String createModelCacheFilename() {
            return PowerStatsService.MODEL_CACHE_FILENAME;
        }

        String createModelFilename() {
            return PowerStatsService.MODEL_FILENAME;
        }

        String createResidencyCacheFilename() {
            return PowerStatsService.RESIDENCY_CACHE_FILENAME;
        }

        String createResidencyFilename() {
            return PowerStatsService.RESIDENCY_FILENAME;
        }

        Injector() {
        }

        File createDataStoragePath() {
            return new File(Environment.getDataSystemDeDirectory(0), PowerStatsService.DATA_STORAGE_SUBDIR);
        }

        PowerStatsHALWrapper.IPowerStatsHALWrapper createPowerStatsHALWrapperImpl() {
            return PowerStatsHALWrapper.getPowerStatsHalImpl();
        }

        PowerStatsHALWrapper.IPowerStatsHALWrapper getPowerStatsHALWrapperImpl() {
            PowerStatsHALWrapper.IPowerStatsHALWrapper iPowerStatsHALWrapper;
            synchronized (this) {
                if (this.mPowerStatsHALWrapper == null) {
                    this.mPowerStatsHALWrapper = PowerStatsHALWrapper.getPowerStatsHalImpl();
                }
                iPowerStatsHALWrapper = this.mPowerStatsHALWrapper;
            }
            return iPowerStatsHALWrapper;
        }

        PowerStatsLogger createPowerStatsLogger(Context context, Looper looper, File file, String str, String str2, String str3, String str4, String str5, String str6, PowerStatsHALWrapper.IPowerStatsHALWrapper iPowerStatsHALWrapper) {
            return new PowerStatsLogger(context, looper, file, str, str2, str3, str4, str5, str6, iPowerStatsHALWrapper);
        }

        BatteryTrigger createBatteryTrigger(Context context, PowerStatsLogger powerStatsLogger) {
            return new BatteryTrigger(context, powerStatsLogger, true);
        }

        TimerTrigger createTimerTrigger(Context context, PowerStatsLogger powerStatsLogger) {
            return new TimerTrigger(context, powerStatsLogger, true);
        }

        StatsPullAtomCallbackImpl createStatsPullerImpl(Context context, PowerStatsInternal powerStatsInternal) {
            return new StatsPullAtomCallbackImpl(context, powerStatsInternal);
        }
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    private final class BinderService extends Binder {
        private BinderService() {
        }

        @Override // android.os.Binder
        protected void dump(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
            if (DumpUtils.checkDumpPermission(PowerStatsService.this.mContext, PowerStatsService.TAG, printWriter)) {
                if (PowerStatsService.this.mPowerStatsLogger == null) {
                    Slog.e(PowerStatsService.TAG, "PowerStats HAL is not initialized.  No data available.");
                    return;
                }
                if (strArr.length > 0 && "--proto".equals(strArr[0])) {
                    if ("model".equals(strArr[1])) {
                        PowerStatsService.this.mPowerStatsLogger.writeModelDataToFile(fileDescriptor);
                        return;
                    } else if ("meter".equals(strArr[1])) {
                        PowerStatsService.this.mPowerStatsLogger.writeMeterDataToFile(fileDescriptor);
                        return;
                    } else {
                        if ("residency".equals(strArr[1])) {
                            PowerStatsService.this.mPowerStatsLogger.writeResidencyDataToFile(fileDescriptor);
                            return;
                        }
                        return;
                    }
                }
                if (strArr.length == 0) {
                    printWriter.println("PowerStatsService dumpsys: available PowerEntities");
                    ProtoStreamUtils.PowerEntityUtils.dumpsys(PowerStatsService.this.getPowerStatsHal().getPowerEntityInfo(), printWriter);
                    printWriter.println("PowerStatsService dumpsys: available Channels");
                    ProtoStreamUtils.ChannelUtils.dumpsys(PowerStatsService.this.getPowerStatsHal().getEnergyMeterInfo(), printWriter);
                    printWriter.println("PowerStatsService dumpsys: available EnergyConsumers");
                    ProtoStreamUtils.EnergyConsumerUtils.dumpsys(PowerStatsService.this.getPowerStatsHal().getEnergyConsumerInfo(), printWriter);
                }
            }
        }
    }

    public void onBootPhase(int i) {
        if (i == 500) {
            onSystemServicesReady();
        } else if (i == 1000) {
            onBootCompleted();
        }
    }

    public void onStart() {
        if (getPowerStatsHal().isInitialized()) {
            LocalService localService = new LocalService();
            this.mPowerStatsInternal = localService;
            publishLocalService(PowerStatsInternal.class, localService);
        }
        publishBinderService(DATA_STORAGE_SUBDIR, new BinderService());
    }

    private void onSystemServicesReady() {
        this.mPullAtomCallback = this.mInjector.createStatsPullerImpl(this.mContext, this.mPowerStatsInternal);
    }

    @VisibleForTesting
    public boolean getDeleteMeterDataOnBoot() {
        return this.mPowerStatsLogger.getDeleteMeterDataOnBoot();
    }

    @VisibleForTesting
    public boolean getDeleteModelDataOnBoot() {
        return this.mPowerStatsLogger.getDeleteModelDataOnBoot();
    }

    @VisibleForTesting
    public boolean getDeleteResidencyDataOnBoot() {
        return this.mPowerStatsLogger.getDeleteResidencyDataOnBoot();
    }

    private void onBootCompleted() {
        if (getPowerStatsHal().isInitialized()) {
            this.mDataStoragePath = this.mInjector.createDataStoragePath();
            PowerStatsLogger createPowerStatsLogger = this.mInjector.createPowerStatsLogger(this.mContext, getLooper(), this.mDataStoragePath, this.mInjector.createMeterFilename(), this.mInjector.createMeterCacheFilename(), this.mInjector.createModelFilename(), this.mInjector.createModelCacheFilename(), this.mInjector.createResidencyFilename(), this.mInjector.createResidencyCacheFilename(), getPowerStatsHal());
            this.mPowerStatsLogger = createPowerStatsLogger;
            this.mBatteryTrigger = this.mInjector.createBatteryTrigger(this.mContext, createPowerStatsLogger);
            this.mTimerTrigger = this.mInjector.createTimerTrigger(this.mContext, this.mPowerStatsLogger);
            return;
        }
        Slog.e(TAG, "Failed to start PowerStatsService loggers");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public PowerStatsHALWrapper.IPowerStatsHALWrapper getPowerStatsHal() {
        return this.mInjector.getPowerStatsHALWrapperImpl();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public Looper getLooper() {
        synchronized (this) {
            Looper looper = this.mLooper;
            if (looper != null) {
                return looper;
            }
            HandlerThread handlerThread = new HandlerThread(TAG);
            handlerThread.start();
            return handlerThread.getLooper();
        }
    }

    private EnergyConsumer[] getEnergyConsumerInfo() {
        EnergyConsumer[] energyConsumerArr;
        synchronized (this) {
            if (this.mEnergyConsumers == null) {
                this.mEnergyConsumers = getPowerStatsHal().getEnergyConsumerInfo();
            }
            energyConsumerArr = this.mEnergyConsumers;
        }
        return energyConsumerArr;
    }

    public PowerStatsService(Context context) {
        this(context, new Injector());
    }

    @VisibleForTesting
    public PowerStatsService(Context context, Injector injector) {
        super(context);
        this.mEnergyConsumers = null;
        this.mContext = context;
        this.mInjector = injector;
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    private final class LocalService extends PowerStatsInternal {
        private final Handler mHandler;

        LocalService() {
            this.mHandler = new Handler(PowerStatsService.this.getLooper());
        }

        public EnergyConsumer[] getEnergyConsumerInfo() {
            return PowerStatsService.this.getPowerStatsHal().getEnergyConsumerInfo();
        }

        public CompletableFuture<EnergyConsumerResult[]> getEnergyConsumedAsync(int[] iArr) {
            CompletableFuture<EnergyConsumerResult[]> completableFuture = new CompletableFuture<>();
            Handler handler = this.mHandler;
            final PowerStatsService powerStatsService = PowerStatsService.this;
            handler.sendMessage(PooledLambda.obtainMessage(new BiConsumer() { // from class: com.android.server.powerstats.PowerStatsService$LocalService$$ExternalSyntheticLambda0
                @Override // java.util.function.BiConsumer
                public final void accept(Object obj, Object obj2) {
                    PowerStatsService.this.getEnergyConsumedAsync((CompletableFuture) obj, (int[]) obj2);
                }
            }, completableFuture, iArr));
            return completableFuture;
        }

        public PowerEntity[] getPowerEntityInfo() {
            return PowerStatsService.this.getPowerStatsHal().getPowerEntityInfo();
        }

        public CompletableFuture<StateResidencyResult[]> getStateResidencyAsync(int[] iArr) {
            CompletableFuture<StateResidencyResult[]> completableFuture = new CompletableFuture<>();
            Handler handler = this.mHandler;
            final PowerStatsService powerStatsService = PowerStatsService.this;
            handler.sendMessage(PooledLambda.obtainMessage(new BiConsumer() { // from class: com.android.server.powerstats.PowerStatsService$LocalService$$ExternalSyntheticLambda1
                @Override // java.util.function.BiConsumer
                public final void accept(Object obj, Object obj2) {
                    PowerStatsService.this.getStateResidencyAsync((CompletableFuture) obj, (int[]) obj2);
                }
            }, completableFuture, iArr));
            return completableFuture;
        }

        public Channel[] getEnergyMeterInfo() {
            return PowerStatsService.this.getPowerStatsHal().getEnergyMeterInfo();
        }

        public CompletableFuture<EnergyMeasurement[]> readEnergyMeterAsync(int[] iArr) {
            CompletableFuture<EnergyMeasurement[]> completableFuture = new CompletableFuture<>();
            Handler handler = this.mHandler;
            final PowerStatsService powerStatsService = PowerStatsService.this;
            handler.sendMessage(PooledLambda.obtainMessage(new BiConsumer() { // from class: com.android.server.powerstats.PowerStatsService$LocalService$$ExternalSyntheticLambda2
                @Override // java.util.function.BiConsumer
                public final void accept(Object obj, Object obj2) {
                    PowerStatsService.this.readEnergyMeterAsync((CompletableFuture) obj, (int[]) obj2);
                }
            }, completableFuture, iArr));
            return completableFuture;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void getEnergyConsumedAsync(CompletableFuture<EnergyConsumerResult[]> completableFuture, int[] iArr) {
        int length;
        EnergyConsumerResult[] energyConsumed = getPowerStatsHal().getEnergyConsumed(iArr);
        EnergyConsumer[] energyConsumerInfo = getEnergyConsumerInfo();
        if (energyConsumerInfo != null) {
            if (iArr.length == 0) {
                length = energyConsumerInfo.length;
            } else {
                length = iArr.length;
            }
            if (energyConsumed == null || length != energyConsumed.length) {
                StringBuilder sb = new StringBuilder();
                sb.append("Requested ids:");
                if (iArr.length == 0) {
                    sb.append("ALL");
                }
                sb.append("[");
                for (int i = 0; i < iArr.length; i++) {
                    int i2 = iArr[i];
                    sb.append(i2);
                    sb.append("(type:");
                    sb.append((int) energyConsumerInfo[i2].type);
                    sb.append(",ord:");
                    sb.append(energyConsumerInfo[i2].ordinal);
                    sb.append(",name:");
                    sb.append(energyConsumerInfo[i2].name);
                    sb.append(")");
                    if (i != length - 1) {
                        sb.append(", ");
                    }
                }
                sb.append("]");
                sb.append(", Received result ids:");
                if (energyConsumed == null) {
                    sb.append("null");
                } else {
                    sb.append("[");
                    int length2 = energyConsumed.length;
                    for (int i3 = 0; i3 < length2; i3++) {
                        int i4 = energyConsumed[i3].id;
                        sb.append(i4);
                        sb.append("(type:");
                        sb.append((int) energyConsumerInfo[i4].type);
                        sb.append(",ord:");
                        sb.append(energyConsumerInfo[i4].ordinal);
                        sb.append(",name:");
                        sb.append(energyConsumerInfo[i4].name);
                        sb.append(")");
                        if (i3 != length2 - 1) {
                            sb.append(", ");
                        }
                    }
                    sb.append("]");
                }
                Slog.wtf(TAG, "Missing result from getEnergyConsumedAsync call. " + ((Object) sb));
            }
        }
        completableFuture.complete(energyConsumed);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void getStateResidencyAsync(CompletableFuture<StateResidencyResult[]> completableFuture, int[] iArr) {
        completableFuture.complete(getPowerStatsHal().getStateResidency(iArr));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void readEnergyMeterAsync(CompletableFuture<EnergyMeasurement[]> completableFuture, int[] iArr) {
        completableFuture.complete(getPowerStatsHal().readEnergyMeter(iArr));
    }
}
