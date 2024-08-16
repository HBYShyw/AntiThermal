package com.android.server.job;

import android.app.job.JobInfo;
import android.app.job.JobWorkItem;
import android.content.Context;
import android.os.Handler;
import android.util.IndentingPrintWriter;
import com.android.server.job.controllers.JobStatus;
import com.android.server.job.controllers.StateController;
import com.android.server.job.restrictions.JobRestriction;
import java.util.List;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public interface IJobSchedulerServiceExt {
    default void acceptForMaybeReadyJobQueueFunctor(JobStatus jobStatus) {
    }

    default boolean checkIdleJobNotUserStatus(JobStatus jobStatus) {
        return false;
    }

    default void checkOplusPermission(JobInfo jobInfo, int i, int i2) {
    }

    default void dumpCacheJobs(IndentingPrintWriter indentingPrintWriter) {
    }

    default void dumpProxyJob(IndentingPrintWriter indentingPrintWriter) {
    }

    default JobInfo getPendingJob(int i, int i2) {
        return null;
    }

    default List<JobInfo> getPendingJobs(int i) {
        return null;
    }

    default void hookReceivePackageRemove(JobSchedulerService jobSchedulerService, int i, String str) {
    }

    default void hookReceivePackageRestarted(JobSchedulerService jobSchedulerService, int i, String str) {
    }

    default boolean ignoreJobRemoved(JobSchedulerService jobSchedulerService, String str, int i) {
        return false;
    }

    default boolean interceptScheduleJobLocked(JobStatus jobStatus, JobWorkItem jobWorkItem) {
        return false;
    }

    default boolean isComponentUsable(JobStatus jobStatus, boolean z) {
        return z;
    }

    default boolean isProxyJob(JobStatus jobStatus, String str) {
        return false;
    }

    default boolean isReadyToBeExecuted(JobStatus jobStatus) {
        return true;
    }

    default boolean isRunningHighCpuJobs() {
        return false;
    }

    default void jobQueueFunctorNotAccept(JobStatus jobStatus) {
    }

    default void onHookBootPhase(Context context, int i) {
    }

    default void onHookEndInit(JobSchedulerService jobSchedulerService, List<StateController> list, List<JobRestriction> list2) {
    }

    default void onHookPreInit(JobSchedulerService jobSchedulerService, Handler handler, Context context) {
    }

    default void onHookRedundantJob(Context context, JobStore jobStore, int i, int i2) {
    }

    default void onHookSystemReady() {
    }

    default int pendingJobs(int i) {
        return 1;
    }

    default boolean readyForPostProcess() {
        return false;
    }

    default void resetForMaybeReadyJobQueueFunctor() {
    }

    default int restoreSpecialJobs(int i) {
        return 2;
    }

    default void scheduleAsPackage(Context context, JobInfo jobInfo, int i) {
    }

    default long translateDelayTime(JobInfo jobInfo, long j) {
        return j;
    }
}
