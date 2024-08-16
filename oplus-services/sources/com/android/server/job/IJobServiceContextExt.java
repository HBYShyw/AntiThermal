package com.android.server.job;

import com.android.server.job.controllers.JobStatus;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public interface IJobServiceContextExt {
    default long translateJobTimeout(JobStatus jobStatus, int i, long j) {
        return j;
    }

    default boolean updateExecutingParameter(JobServiceContext jobServiceContext, int i) {
        return false;
    }
}
