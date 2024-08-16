package com.android.server.pm;

import android.app.job.JobParameters;
import android.app.job.JobService;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public final class BackgroundDexOptJobService extends JobService {
    @Override // android.app.job.JobService
    public boolean onStartJob(JobParameters jobParameters) {
        return BackgroundDexOptService.getService().onStartJob(this, jobParameters);
    }

    @Override // android.app.job.JobService
    public boolean onStopJob(JobParameters jobParameters) {
        return BackgroundDexOptService.getService().onStopJob(this, jobParameters);
    }
}
