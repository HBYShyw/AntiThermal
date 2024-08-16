package com.android.server.job;

import java.util.ArrayList;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public interface JobSchedulerInternalExt {
    public static final int PENDING_FAIL = 1;
    public static final int PENDING_PROCESSING = 2;
    public static final String REASON_PENDING_JOB = "frozen_pending_internal";
    public static final int RESTORE_IGNORE = 1;
    public static final int RESTORE_SUCCESS = 2;

    default int cancelExecutingJobsForHans(int i, ArrayList<String> arrayList) {
        return 1;
    }

    default void cancelJobsForKilledApp(String str, int i) {
    }

    default int pendingJobs(int i) {
        return 1;
    }

    default int restoreJobs(int i) {
        return 1;
    }

    void stopStrictModeOnJob();
}
