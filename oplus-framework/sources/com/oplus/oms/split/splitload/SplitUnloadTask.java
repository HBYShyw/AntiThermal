package com.oplus.oms.split.splitload;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import com.oplus.oms.split.common.SplitLog;
import com.oplus.oms.split.splitload.extension.AABExtension;
import com.oplus.oms.split.splitreport.SplitReporterConstant;
import com.oplus.oms.split.splitreport.SplitReporterHelper;
import com.oplus.oms.split.splitreport.model.SplitReporterInfo;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public abstract class SplitUnloadTask implements Runnable {
    protected static final String TAG = "SplitUnloadTask";
    protected final Context mContext;
    protected SplitReporterInfo mReportInfo;
    protected final Split mSplit;
    protected int mResultCode = 1;
    protected Handler mHandler = new Handler(Looper.getMainLooper());

    abstract void unloadCode();

    public SplitUnloadTask(Context context, Split split, SplitReporterInfo reporterInfo) {
        this.mContext = context;
        this.mSplit = split;
        this.mReportInfo = reporterInfo;
    }

    @Override // java.lang.Runnable
    public void run() {
        if (Looper.myLooper() != Looper.getMainLooper()) {
            this.mHandler.post(new Runnable() { // from class: com.oplus.oms.split.splitload.SplitUnloadTask$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    SplitUnloadTask.this.unloadSync();
                }
            });
        } else {
            unloadSync();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void unloadSync() {
        this.mResultCode = 1;
        deactivateSplit();
        unloadCode();
        unloadResource();
        report();
    }

    void deactivateSplit() {
        if (this.mSplit.splitApp != null) {
            try {
                this.mSplit.splitApp.onTerminate();
            } catch (Throwable e) {
                SplitLog.w(TAG, "unloadSplit[%s] execute onTerminate error: %s", this.mSplit.splitName, e.getMessage());
                this.mResultCode = -52;
            }
        }
        AABExtension.getInstance().removeSplitProviders(this.mSplit.splitName);
    }

    void unloadResource() {
    }

    void report() {
        SplitReporterInfo splitReporterInfo = this.mReportInfo;
        if (splitReporterInfo == null) {
            this.mReportInfo = new SplitReporterInfo();
            return;
        }
        splitReporterInfo.calcTimeCost(System.currentTimeMillis());
        this.mReportInfo.setName(this.mSplit.splitName);
        this.mReportInfo.setVersion(this.mSplit.splitVersion);
        this.mReportInfo.setResultCode(this.mResultCode);
        SplitReporterHelper.reporter(SplitReporterConstant.UNLOAD_TAG, this.mReportInfo);
    }
}
