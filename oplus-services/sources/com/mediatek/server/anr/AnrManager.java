package com.mediatek.server.anr;

import android.content.pm.ApplicationInfo;
import android.os.Handler;
import android.os.Message;
import com.android.internal.os.anr.AnrLatencyTracker;
import com.android.server.am.ActivityManagerService;
import com.android.server.am.ProcessErrorStateRecord;
import com.android.server.am.ProcessRecord;
import java.io.File;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
public class AnrManager {
    public static final int EVENT_BOOT_COMPLETED = 9001;

    public void AddAnrManagerService() {
    }

    public boolean delayMessage(Handler handler, Message message, int i, int i2) {
        return false;
    }

    public boolean isAnrDeferrable() {
        return false;
    }

    public void removeBroadcastMonitorMessage() {
    }

    public void removeServiceMonitorMessage() {
    }

    public void sendBroadcastMonitorMessage(long j, long j2) {
    }

    public void sendServiceMonitorMessage() {
    }

    public boolean startAnrDump(ActivityManagerService activityManagerService, ProcessErrorStateRecord processErrorStateRecord, String str, ApplicationInfo applicationInfo, String str2, ProcessRecord processRecord, boolean z, String str3, boolean z2, long j, boolean z3, UUID uuid, String str4, ExecutorService executorService, AnrLatencyTracker anrLatencyTracker, String str5, Future<?> future, boolean z4, Future<File> future2) {
        return false;
    }

    public void startAnrManagerService(int i) {
    }

    public void writeEvent(int i) {
    }
}
