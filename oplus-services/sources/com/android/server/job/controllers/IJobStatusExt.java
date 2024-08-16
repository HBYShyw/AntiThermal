package com.android.server.job.controllers;

import android.app.job.JobInfo;
import java.io.PrintWriter;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public interface IJobStatusExt {
    public static final int CONSTRAINT_BATTERY_DILE = 1024;
    public static final int CONSTRAINT_CONNECTIVITY = 7;
    public static final int CONSTRAINT_CPU = 4096;
    public static final int CONSTRAINT_FORE_APP = 2048;
    public static final int CONSTRAINT_PROTECT_SCENE = 16384;
    public static final int CONSTRAINT_TEMPERATURE = 8192;

    default void dumpConstraints(PrintWriter printWriter, int i) {
    }

    default boolean getBooleanValue(String str, String str2, boolean z) {
        return z;
    }

    default int getIntValue(String str, String str2, int i) {
        return i;
    }

    default String getOplusExtraStr(JobStatus jobStatus) {
        return null;
    }

    default int getProtectForeScene(JobStatus jobStatus) {
        return 0;
    }

    default int getProtectForeType(JobStatus jobStatus) {
        return 0;
    }

    default int initRequiredConstraints(JobInfo jobInfo) {
        return 0;
    }

    default boolean isReady(boolean z, boolean z2, JobStatus jobStatus, int i, int i2, int i3, int i4, int i5) {
        return true;
    }

    default boolean setBooleanValue(String str, String str2, boolean z, boolean z2) {
        return z2;
    }

    default int setIntValue(String str, String str2, int i) {
        return 0;
    }

    default void setSyncJobAbnormal(JobInfo jobInfo) {
    }
}
