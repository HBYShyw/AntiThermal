package com.android.server.am;

import android.os.Process;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public interface IProcessListSocExt {
    default void onStartProcess(ActivityManagerService activityManagerService, String str, String str2) {
    }

    default void startProcess(HostingRecord hostingRecord, Process.ProcessStartResult processStartResult, ProcessRecord processRecord) {
    }
}
