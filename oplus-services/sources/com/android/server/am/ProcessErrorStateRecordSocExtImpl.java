package com.android.server.am;

import android.content.pm.ApplicationInfo;
import com.android.internal.os.anr.AnrLatencyTracker;
import com.mediatek.server.anr.AnrManager;
import java.io.File;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public class ProcessErrorStateRecordSocExtImpl implements IProcessErrorStateRecordSocExt {
    private static final String TAG = "ProcessErrorStateRecordSocExtImpl";
    ProcessErrorStateRecord mProcessErrorStateRecord;

    public ProcessErrorStateRecordSocExtImpl(Object obj) {
        this.mProcessErrorStateRecord = (ProcessErrorStateRecord) obj;
    }

    @Override // com.android.server.am.IProcessErrorStateRecordSocExt
    public boolean startAnrDump(ActivityManagerService activityManagerService, ProcessErrorStateRecord processErrorStateRecord, String str, ApplicationInfo applicationInfo, String str2, ProcessRecord processRecord, boolean z, String str3, boolean z2, long j, boolean z3, UUID uuid, String str4, ExecutorService executorService, AnrLatencyTracker anrLatencyTracker, String str5, Future<?> future, boolean z4, Future<File> future2) {
        AnrManager anrManager = (AnrManager) activityManagerService.getWrapper().getAnrManager();
        if (anrManager == null) {
            return false;
        }
        return anrManager.startAnrDump(activityManagerService, processErrorStateRecord, str, applicationInfo, str2, processRecord, z, str3, z2, j, z3, uuid, str4, executorService, anrLatencyTracker, str5, future, z4, future2);
    }
}
