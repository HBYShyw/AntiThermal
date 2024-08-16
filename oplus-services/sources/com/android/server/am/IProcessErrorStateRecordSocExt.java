package com.android.server.am;

import android.content.pm.ApplicationInfo;
import com.android.internal.os.anr.AnrLatencyTracker;
import java.io.File;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public interface IProcessErrorStateRecordSocExt {
    default boolean startAnrDump(ActivityManagerService activityManagerService, ProcessErrorStateRecord processErrorStateRecord, String str, ApplicationInfo applicationInfo, String str2, ProcessRecord processRecord, boolean z, String str3, boolean z2, long j, boolean z3, UUID uuid, String str4, ExecutorService executorService, AnrLatencyTracker anrLatencyTracker, String str5, Future<?> future, boolean z4, Future<File> future2) {
        return false;
    }
}
