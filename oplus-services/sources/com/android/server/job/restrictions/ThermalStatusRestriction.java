package com.android.server.job.restrictions;

import android.os.PowerManager;
import android.util.IndentingPrintWriter;
import com.android.internal.annotations.VisibleForTesting;
import com.android.server.job.JobSchedulerService;
import com.android.server.job.controllers.JobStatus;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public class ThermalStatusRestriction extends JobRestriction {
    private static final int HIGHER_PRIORITY_THRESHOLD = 2;
    private static final int LOWER_THRESHOLD = 1;
    private static final int LOW_PRIORITY_THRESHOLD = 1;
    private static final String TAG = "ThermalStatusRestriction";
    private static final int UPPER_THRESHOLD = 3;
    private volatile int mThermalStatus;

    public ThermalStatusRestriction(JobSchedulerService jobSchedulerService) {
        super(jobSchedulerService, 4, 12, 5);
        this.mThermalStatus = 0;
    }

    @Override // com.android.server.job.restrictions.JobRestriction
    public void onSystemServicesReady() {
        ((PowerManager) this.mService.getTestableContext().getSystemService(PowerManager.class)).addThermalStatusListener(new PowerManager.OnThermalStatusChangedListener() { // from class: com.android.server.job.restrictions.ThermalStatusRestriction.1
            @Override // android.os.PowerManager.OnThermalStatusChangedListener
            public void onThermalStatusChanged(int i) {
                boolean z = (i >= 1 && i <= 3) || (ThermalStatusRestriction.this.mThermalStatus >= 1 && i < 1) || (ThermalStatusRestriction.this.mThermalStatus < 3 && i > 3);
                boolean z2 = ThermalStatusRestriction.this.mThermalStatus < i;
                ThermalStatusRestriction.this.mThermalStatus = i;
                if (z) {
                    ThermalStatusRestriction thermalStatusRestriction = ThermalStatusRestriction.this;
                    thermalStatusRestriction.mService.onRestrictionStateChanged(thermalStatusRestriction, z2);
                }
            }
        });
    }

    @Override // com.android.server.job.restrictions.JobRestriction
    public boolean isJobRestricted(JobStatus jobStatus) {
        if (this.mThermalStatus >= 3) {
            return true;
        }
        int effectivePriority = jobStatus.getEffectivePriority();
        if (this.mThermalStatus >= 2) {
            if (jobStatus.shouldTreatAsUserInitiatedJob()) {
                return false;
            }
            if (!jobStatus.shouldTreatAsExpeditedJob()) {
                return (effectivePriority == 400 && this.mService.isCurrentlyRunningLocked(jobStatus) && !this.mService.isJobInOvertimeLocked(jobStatus)) ? false : true;
            }
            if (jobStatus.getNumPreviousAttempts() <= 0) {
                return this.mService.isCurrentlyRunningLocked(jobStatus) && this.mService.isJobInOvertimeLocked(jobStatus);
            }
            return true;
        }
        if (this.mThermalStatus < 1) {
            return false;
        }
        if (effectivePriority != 100) {
            return effectivePriority == 200 && (!this.mService.isCurrentlyRunningLocked(jobStatus) || this.mService.isJobInOvertimeLocked(jobStatus));
        }
        return true;
    }

    @VisibleForTesting
    int getThermalStatus() {
        return this.mThermalStatus;
    }

    @Override // com.android.server.job.restrictions.JobRestriction
    public void dumpConstants(IndentingPrintWriter indentingPrintWriter) {
        indentingPrintWriter.print("Thermal status: ");
        indentingPrintWriter.println(this.mThermalStatus);
    }
}
