package com.android.server.job.restrictions;

import android.util.IndentingPrintWriter;
import android.util.proto.ProtoOutputStream;
import com.android.server.job.JobSchedulerService;
import com.android.server.job.controllers.JobStatus;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public abstract class JobRestriction {
    private final int mInternalReason;
    private final int mPendingReason;
    final JobSchedulerService mService;
    private final int mStopReason;

    public abstract void dumpConstants(IndentingPrintWriter indentingPrintWriter);

    public void dumpConstants(ProtoOutputStream protoOutputStream) {
    }

    public abstract boolean isJobRestricted(JobStatus jobStatus);

    public void onSystemServicesReady() {
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public JobRestriction(JobSchedulerService jobSchedulerService, int i, int i2, int i3) {
        this.mService = jobSchedulerService;
        this.mPendingReason = i2;
        this.mStopReason = i;
        this.mInternalReason = i3;
    }

    public final int getPendingReason() {
        return this.mPendingReason;
    }

    public final int getStopReason() {
        return this.mStopReason;
    }

    public final int getInternalReason() {
        return this.mInternalReason;
    }
}
