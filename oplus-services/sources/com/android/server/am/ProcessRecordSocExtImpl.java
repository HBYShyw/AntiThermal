package com.android.server.am;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public class ProcessRecordSocExtImpl implements IProcessRecordSocExt {
    private static final String TAG = "ProcessRecordSocExtImpl";
    ProcessRecord mProcessRecord;

    @Override // com.android.server.am.IProcessRecordSocExt
    public void killLocked(ActivityManagerService activityManagerService, ProcessErrorStateRecord processErrorStateRecord, ProcessRecord processRecord) {
    }

    public ProcessRecordSocExtImpl(Object obj) {
        this.mProcessRecord = (ProcessRecord) obj;
    }
}
