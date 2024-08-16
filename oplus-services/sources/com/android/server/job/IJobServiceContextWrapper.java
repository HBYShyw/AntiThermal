package com.android.server.job;

import android.app.job.JobParameters;
import com.android.server.job.controllers.JobStatus;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public interface IJobServiceContextWrapper {
    default Object getLock() {
        return null;
    }

    default JobParameters getParams() {
        return null;
    }

    default JobStatus getRunningJob() {
        return null;
    }

    default IJobServiceContextExt getExtImpl() {
        return new IJobServiceContextExt() { // from class: com.android.server.job.IJobServiceContextWrapper.1
        };
    }
}
