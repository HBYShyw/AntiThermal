package com.android.server.job.controllers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.PowerManager;
import android.os.UserHandle;
import android.util.ArraySet;
import android.util.IndentingPrintWriter;
import android.util.Log;
import android.util.Slog;
import android.util.SparseBooleanArray;
import android.util.proto.ProtoOutputStream;
import com.android.internal.util.ArrayUtils;
import com.android.server.AppSchedulingModuleThread;
import com.android.server.DeviceIdleInternal;
import com.android.server.LocalServices;
import com.android.server.job.JobSchedulerService;
import com.android.server.pm.DumpState;
import com.android.server.pm.verify.domain.DomainVerificationPersistence;
import java.util.Arrays;
import java.util.function.Consumer;
import java.util.function.Predicate;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public final class DeviceIdleJobsController extends StateController {
    private static final long BACKGROUND_JOBS_DELAY = 3000;
    private static final boolean DEBUG;
    static final int PROCESS_BACKGROUND_JOBS = 1;
    private static final String TAG = "JobScheduler.DeviceIdle";
    private final ArraySet<JobStatus> mAllowInIdleJobs;
    private final BroadcastReceiver mBroadcastReceiver;
    private boolean mDeviceIdleMode;
    private final DeviceIdleUpdateFunctor mDeviceIdleUpdateFunctor;
    private int[] mDeviceIdleWhitelistAppIds;
    private final SparseBooleanArray mForegroundUids;
    private final DeviceIdleJobsDelayHandler mHandler;
    private final DeviceIdleInternal mLocalDeviceIdleController;
    private final PowerManager mPowerManager;
    private int[] mPowerSaveTempWhitelistAppIds;
    private final Predicate<JobStatus> mShouldRushEvaluation;

    static {
        DEBUG = JobSchedulerService.DEBUG || Log.isLoggable(TAG, 3);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ boolean lambda$new$0(JobStatus jobStatus) {
        return jobStatus.isRequestedExpeditedJob() || this.mForegroundUids.get(jobStatus.getSourceUid());
    }

    public DeviceIdleJobsController(JobSchedulerService jobSchedulerService) {
        super(jobSchedulerService);
        this.mForegroundUids = new SparseBooleanArray();
        BroadcastReceiver broadcastReceiver = new BroadcastReceiver() { // from class: com.android.server.job.controllers.DeviceIdleJobsController.1
            @Override // android.content.BroadcastReceiver
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                action.hashCode();
                boolean z = true;
                char c = 65535;
                switch (action.hashCode()) {
                    case -712152692:
                        if (action.equals("android.os.action.POWER_SAVE_TEMP_WHITELIST_CHANGED")) {
                            c = 0;
                            break;
                        }
                        break;
                    case -65633567:
                        if (action.equals("android.os.action.POWER_SAVE_WHITELIST_CHANGED")) {
                            c = 1;
                            break;
                        }
                        break;
                    case 498807504:
                        if (action.equals("android.os.action.LIGHT_DEVICE_IDLE_MODE_CHANGED")) {
                            c = 2;
                            break;
                        }
                        break;
                    case 870701415:
                        if (action.equals("android.os.action.DEVICE_IDLE_MODE_CHANGED")) {
                            c = 3;
                            break;
                        }
                        break;
                }
                switch (c) {
                    case 0:
                        synchronized (DeviceIdleJobsController.this.mLock) {
                            DeviceIdleJobsController deviceIdleJobsController = DeviceIdleJobsController.this;
                            deviceIdleJobsController.mPowerSaveTempWhitelistAppIds = deviceIdleJobsController.mLocalDeviceIdleController.getPowerSaveTempWhitelistAppIds();
                            if (DeviceIdleJobsController.DEBUG) {
                                Slog.d(DeviceIdleJobsController.TAG, "Got temp whitelist " + Arrays.toString(DeviceIdleJobsController.this.mPowerSaveTempWhitelistAppIds));
                            }
                            ArraySet<JobStatus> arraySet = new ArraySet<>();
                            long millis = JobSchedulerService.sElapsedRealtimeClock.millis();
                            for (int i = 0; i < DeviceIdleJobsController.this.mAllowInIdleJobs.size(); i++) {
                                DeviceIdleJobsController deviceIdleJobsController2 = DeviceIdleJobsController.this;
                                if (deviceIdleJobsController2.updateTaskStateLocked((JobStatus) deviceIdleJobsController2.mAllowInIdleJobs.valueAt(i), millis)) {
                                    arraySet.add((JobStatus) DeviceIdleJobsController.this.mAllowInIdleJobs.valueAt(i));
                                }
                            }
                            if (arraySet.size() > 0) {
                                DeviceIdleJobsController.this.mStateChangedListener.onControllerStateChanged(arraySet);
                            }
                        }
                        return;
                    case 1:
                        synchronized (DeviceIdleJobsController.this.mLock) {
                            DeviceIdleJobsController deviceIdleJobsController3 = DeviceIdleJobsController.this;
                            deviceIdleJobsController3.mDeviceIdleWhitelistAppIds = deviceIdleJobsController3.mLocalDeviceIdleController.getPowerSaveWhitelistUserAppIds();
                            if (DeviceIdleJobsController.DEBUG) {
                                Slog.d(DeviceIdleJobsController.TAG, "Got whitelist " + Arrays.toString(DeviceIdleJobsController.this.mDeviceIdleWhitelistAppIds));
                            }
                        }
                        return;
                    case 2:
                    case 3:
                        DeviceIdleJobsController deviceIdleJobsController4 = DeviceIdleJobsController.this;
                        if (deviceIdleJobsController4.mPowerManager == null || (!DeviceIdleJobsController.this.mPowerManager.isDeviceIdleMode() && !DeviceIdleJobsController.this.mPowerManager.isLightDeviceIdleMode())) {
                            z = false;
                        }
                        deviceIdleJobsController4.updateIdleMode(z);
                        return;
                    default:
                        return;
                }
            }
        };
        this.mBroadcastReceiver = broadcastReceiver;
        this.mShouldRushEvaluation = new Predicate() { // from class: com.android.server.job.controllers.DeviceIdleJobsController$$ExternalSyntheticLambda2
            @Override // java.util.function.Predicate
            public final boolean test(Object obj) {
                boolean lambda$new$0;
                lambda$new$0 = DeviceIdleJobsController.this.lambda$new$0((JobStatus) obj);
                return lambda$new$0;
            }
        };
        this.mHandler = new DeviceIdleJobsDelayHandler(AppSchedulingModuleThread.get().getLooper());
        this.mPowerManager = (PowerManager) this.mContext.getSystemService("power");
        DeviceIdleInternal deviceIdleInternal = (DeviceIdleInternal) LocalServices.getService(DeviceIdleInternal.class);
        this.mLocalDeviceIdleController = deviceIdleInternal;
        this.mDeviceIdleWhitelistAppIds = deviceIdleInternal.getPowerSaveWhitelistUserAppIds();
        this.mPowerSaveTempWhitelistAppIds = deviceIdleInternal.getPowerSaveTempWhitelistAppIds();
        this.mDeviceIdleUpdateFunctor = new DeviceIdleUpdateFunctor();
        this.mAllowInIdleJobs = new ArraySet<>();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.os.action.DEVICE_IDLE_MODE_CHANGED");
        intentFilter.addAction("android.os.action.LIGHT_DEVICE_IDLE_MODE_CHANGED");
        intentFilter.addAction("android.os.action.POWER_SAVE_WHITELIST_CHANGED");
        intentFilter.addAction("android.os.action.POWER_SAVE_TEMP_WHITELIST_CHANGED");
        this.mContext.registerReceiverAsUser(broadcastReceiver, UserHandle.ALL, intentFilter, null, null);
    }

    void updateIdleMode(boolean z) {
        boolean z2;
        synchronized (this.mLock) {
            z2 = this.mDeviceIdleMode != z;
            this.mDeviceIdleMode = z;
            logDeviceWideConstraintStateToStatsd(DumpState.DUMP_APEX, z ? false : true);
            if (DEBUG) {
                Slog.d(TAG, "mDeviceIdleMode=" + this.mDeviceIdleMode);
            }
            this.mDeviceIdleUpdateFunctor.prepare();
            if (z) {
                this.mHandler.removeMessages(1);
                this.mService.getJobStore().forEachJob(this.mDeviceIdleUpdateFunctor);
            } else {
                this.mService.getJobStore().forEachJob(this.mShouldRushEvaluation, this.mDeviceIdleUpdateFunctor);
                this.mHandler.sendEmptyMessageDelayed(1, BACKGROUND_JOBS_DELAY);
            }
        }
        if (z2) {
            this.mStateChangedListener.onDeviceIdleStateChanged(z);
        }
    }

    public void setUidActiveLocked(int i, boolean z) {
        if (z != this.mForegroundUids.get(i)) {
            if (DEBUG) {
                StringBuilder sb = new StringBuilder();
                sb.append("uid ");
                sb.append(i);
                sb.append(" going ");
                sb.append(z ? DomainVerificationPersistence.TAG_ACTIVE : "inactive");
                Slog.d(TAG, sb.toString());
            }
            this.mForegroundUids.put(i, z);
            this.mDeviceIdleUpdateFunctor.prepare();
            this.mService.getJobStore().forEachJobForSourceUid(i, this.mDeviceIdleUpdateFunctor);
            if (this.mDeviceIdleUpdateFunctor.mChangedJobs.size() > 0) {
                this.mStateChangedListener.onControllerStateChanged(this.mDeviceIdleUpdateFunctor.mChangedJobs);
            }
        }
    }

    boolean isWhitelistedLocked(JobStatus jobStatus) {
        return Arrays.binarySearch(this.mDeviceIdleWhitelistAppIds, UserHandle.getAppId(jobStatus.getSourceUid())) >= 0;
    }

    boolean isTempWhitelistedLocked(JobStatus jobStatus) {
        return ArrayUtils.contains(this.mPowerSaveTempWhitelistAppIds, UserHandle.getAppId(jobStatus.getSourceUid()));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean updateTaskStateLocked(JobStatus jobStatus, long j) {
        boolean z = true;
        boolean z2 = (jobStatus.getFlags() & 2) != 0 && (this.mForegroundUids.get(jobStatus.getSourceUid()) || isTempWhitelistedLocked(jobStatus));
        boolean isWhitelistedLocked = isWhitelistedLocked(jobStatus);
        if (this.mDeviceIdleMode && !isWhitelistedLocked && !z2) {
            z = false;
        }
        return jobStatus.setDeviceNotDozingConstraintSatisfied(j, z, isWhitelistedLocked);
    }

    @Override // com.android.server.job.controllers.StateController
    public void maybeStartTrackingJobLocked(JobStatus jobStatus, JobStatus jobStatus2) {
        if ((jobStatus.getFlags() & 2) != 0) {
            this.mAllowInIdleJobs.add(jobStatus);
        }
        updateTaskStateLocked(jobStatus, JobSchedulerService.sElapsedRealtimeClock.millis());
    }

    @Override // com.android.server.job.controllers.StateController
    public void maybeStopTrackingJobLocked(JobStatus jobStatus, JobStatus jobStatus2) {
        if ((jobStatus.getFlags() & 2) != 0) {
            this.mAllowInIdleJobs.remove(jobStatus);
        }
    }

    @Override // com.android.server.job.controllers.StateController
    public void dumpControllerStateLocked(final IndentingPrintWriter indentingPrintWriter, Predicate<JobStatus> predicate) {
        indentingPrintWriter.println("Idle mode: " + this.mDeviceIdleMode);
        indentingPrintWriter.println();
        this.mService.getJobStore().forEachJob(predicate, new Consumer() { // from class: com.android.server.job.controllers.DeviceIdleJobsController$$ExternalSyntheticLambda1
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                DeviceIdleJobsController.this.lambda$dumpControllerStateLocked$1(indentingPrintWriter, (JobStatus) obj);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$dumpControllerStateLocked$1(IndentingPrintWriter indentingPrintWriter, JobStatus jobStatus) {
        indentingPrintWriter.print("#");
        jobStatus.printUniqueId(indentingPrintWriter);
        indentingPrintWriter.print(" from ");
        UserHandle.formatUid(indentingPrintWriter, jobStatus.getSourceUid());
        indentingPrintWriter.print(": ");
        indentingPrintWriter.print(jobStatus.getSourcePackageName());
        indentingPrintWriter.print((jobStatus.satisfiedConstraints & DumpState.DUMP_APEX) != 0 ? " RUNNABLE" : " WAITING");
        if (jobStatus.appHasDozeExemption) {
            indentingPrintWriter.print(" WHITELISTED");
        }
        if (this.mAllowInIdleJobs.contains(jobStatus)) {
            indentingPrintWriter.print(" ALLOWED_IN_DOZE");
        }
        indentingPrintWriter.println();
    }

    @Override // com.android.server.job.controllers.StateController
    public void dumpControllerStateLocked(final ProtoOutputStream protoOutputStream, long j, Predicate<JobStatus> predicate) {
        long start = protoOutputStream.start(j);
        long start2 = protoOutputStream.start(1146756268037L);
        protoOutputStream.write(1133871366145L, this.mDeviceIdleMode);
        this.mService.getJobStore().forEachJob(predicate, new Consumer() { // from class: com.android.server.job.controllers.DeviceIdleJobsController$$ExternalSyntheticLambda0
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                DeviceIdleJobsController.this.lambda$dumpControllerStateLocked$2(protoOutputStream, (JobStatus) obj);
            }
        });
        protoOutputStream.end(start2);
        protoOutputStream.end(start);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$dumpControllerStateLocked$2(ProtoOutputStream protoOutputStream, JobStatus jobStatus) {
        long start = protoOutputStream.start(2246267895810L);
        jobStatus.writeToShortProto(protoOutputStream, 1146756268033L);
        protoOutputStream.write(1120986464258L, jobStatus.getSourceUid());
        protoOutputStream.write(1138166333443L, jobStatus.getSourcePackageName());
        protoOutputStream.write(1133871366148L, (jobStatus.satisfiedConstraints & DumpState.DUMP_APEX) != 0);
        protoOutputStream.write(1133871366149L, jobStatus.appHasDozeExemption);
        protoOutputStream.write(1133871366150L, this.mAllowInIdleJobs.contains(jobStatus));
        protoOutputStream.end(start);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public final class DeviceIdleUpdateFunctor implements Consumer<JobStatus> {
        final ArraySet<JobStatus> mChangedJobs = new ArraySet<>();
        long mUpdateTimeElapsed = 0;

        DeviceIdleUpdateFunctor() {
        }

        void prepare() {
            this.mChangedJobs.clear();
            this.mUpdateTimeElapsed = JobSchedulerService.sElapsedRealtimeClock.millis();
        }

        @Override // java.util.function.Consumer
        public void accept(JobStatus jobStatus) {
            if (DeviceIdleJobsController.this.updateTaskStateLocked(jobStatus, this.mUpdateTimeElapsed)) {
                this.mChangedJobs.add(jobStatus);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public final class DeviceIdleJobsDelayHandler extends Handler {
        public DeviceIdleJobsDelayHandler(Looper looper) {
            super(looper);
        }

        @Override // android.os.Handler
        public void handleMessage(Message message) {
            if (message.what != 1) {
                return;
            }
            synchronized (DeviceIdleJobsController.this.mLock) {
                DeviceIdleJobsController.this.mDeviceIdleUpdateFunctor.prepare();
                DeviceIdleJobsController.this.mService.getJobStore().forEachJob(DeviceIdleJobsController.this.mDeviceIdleUpdateFunctor);
                if (DeviceIdleJobsController.this.mDeviceIdleUpdateFunctor.mChangedJobs.size() > 0) {
                    DeviceIdleJobsController deviceIdleJobsController = DeviceIdleJobsController.this;
                    deviceIdleJobsController.mStateChangedListener.onControllerStateChanged(deviceIdleJobsController.mDeviceIdleUpdateFunctor.mChangedJobs);
                }
            }
        }
    }
}
