package com.oplus.oms.split.splitload;

import android.content.Context;
import com.oplus.oms.split.splitreport.model.SplitReporterInfo;

/* loaded from: classes.dex */
public class SplitMultipleModeUnloadTaskImpl extends SplitUnloadTask {
    @Override // com.oplus.oms.split.splitload.SplitUnloadTask, java.lang.Runnable
    public /* bridge */ /* synthetic */ void run() {
        super.run();
    }

    public SplitMultipleModeUnloadTaskImpl(Context context, Split split, SplitReporterInfo reporterInfo) {
        super(context, split, reporterInfo);
    }

    @Override // com.oplus.oms.split.splitload.SplitUnloadTask
    void unloadCode() {
        SplitApplicationLoaders.getInstance().removeClassLoader(this.mSplit.splitName);
    }
}
