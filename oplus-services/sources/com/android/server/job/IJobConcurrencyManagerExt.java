package com.android.server.job;

import com.android.server.job.controllers.JobStatus;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public interface IJobConcurrencyManagerExt {
    default void hookStartJobErrorExecute(JobStatus jobStatus, int i) {
    }
}
