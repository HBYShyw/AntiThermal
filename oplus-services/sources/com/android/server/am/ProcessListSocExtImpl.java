package com.android.server.am;

import android.os.Process;
import com.mediatek.server.am.AmsExt;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public class ProcessListSocExtImpl implements IProcessListSocExt {
    private static final String TAG = "ProcessListSocExtImpl";
    ProcessList mProcessList;

    @Override // com.android.server.am.IProcessListSocExt
    public void startProcess(HostingRecord hostingRecord, Process.ProcessStartResult processStartResult, ProcessRecord processRecord) {
    }

    public ProcessListSocExtImpl(Object obj) {
        this.mProcessList = (ProcessList) obj;
    }

    @Override // com.android.server.am.IProcessListSocExt
    public void onStartProcess(ActivityManagerService activityManagerService, String str, String str2) {
        AmsExt amsExt = (AmsExt) activityManagerService.getWrapper().getAmsExt();
        if (amsExt == null) {
            return;
        }
        amsExt.onStartProcess(str, str2);
    }
}
