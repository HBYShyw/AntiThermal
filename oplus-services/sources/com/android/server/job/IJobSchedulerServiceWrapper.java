package com.android.server.job;

import com.android.server.job.controllers.JobStatus;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public interface IJobSchedulerServiceWrapper {
    default void cancelJobImplLocked(JobStatus jobStatus, JobStatus jobStatus2, int i, int i2, String str) {
    }

    default void cancelJobsForPackageAndUidLocked(String str, int i, int i2, int i3, String str2) {
    }

    default int getMAX_JOBS_PER_APP() {
        return 0;
    }

    default boolean stopTrackingJobLocked(JobStatus jobStatus, JobStatus jobStatus2, boolean z) {
        return false;
    }

    default IJobSchedulerServiceExt getExtImpl() {
        return new IJobSchedulerServiceExt() { // from class: com.android.server.job.IJobSchedulerServiceWrapper.1
        };
    }
}
