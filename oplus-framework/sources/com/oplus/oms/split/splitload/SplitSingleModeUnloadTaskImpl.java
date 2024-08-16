package com.oplus.oms.split.splitload;

import android.content.Context;
import com.oplus.oms.split.common.SplitLog;
import com.oplus.oms.split.splitreport.model.SplitReporterInfo;

/* loaded from: classes.dex */
public class SplitSingleModeUnloadTaskImpl extends SplitUnloadTask {
    @Override // com.oplus.oms.split.splitload.SplitUnloadTask, java.lang.Runnable
    public /* bridge */ /* synthetic */ void run() {
        super.run();
    }

    public SplitSingleModeUnloadTaskImpl(Context context, Split split, SplitReporterInfo reporterInfo) {
        super(context, split, reporterInfo);
    }

    @Override // com.oplus.oms.split.splitload.SplitUnloadTask
    void unloadCode() {
        try {
            SplitCompatDexLoader.unLoad(this.mContext.getClassLoader());
        } catch (Throwable th) {
            SplitLog.w("SplitUnloadTask", "unloadSplit error", new Object[0]);
            this.mResultCode = -50;
        }
    }
}
