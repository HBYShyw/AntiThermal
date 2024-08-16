package com.oplus.deepthinker.service;

import android.app.job.JobInfo;
import android.common.IOplusCommonFeature;
import android.common.OplusFeatureList;
import java.io.FileDescriptor;
import java.io.PrintWriter;

/* loaded from: classes.dex */
public interface IOplusDeepThinkerExService extends IOplusCommonFeature {
    public static final IOplusDeepThinkerExService DEFAULT = new IOplusDeepThinkerExService() { // from class: com.oplus.deepthinker.service.IOplusDeepThinkerExService.1
    };

    default OplusFeatureList.OplusIndex index() {
        return OplusFeatureList.OplusIndex.IOplusDeepThinkerExService;
    }

    default IOplusCommonFeature getDefault() {
        return DEFAULT;
    }

    default boolean onHandleDump(FileDescriptor fd, PrintWriter pw, String[] args) {
        return false;
    }

    default void onWakeFullnessChanged(int wakefulness) {
    }

    default void onScheduleJob(JobInfo job) {
    }

    default void onReleaseWakelock(String packageName, String tag, long totalTime) {
    }

    default void onDeliverAlarm(Object bs, Object alarm, long nowElapsed) {
    }
}
