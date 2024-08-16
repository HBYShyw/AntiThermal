package com.oplus.oms.split.splitreport;

import android.content.Context;
import com.oplus.oms.split.common.SplitLog;
import com.oplus.oms.split.splitreport.model.SplitReporterInfo;
import java.util.List;

/* loaded from: classes.dex */
public class OmsSplitReporter implements SplitReporter {
    private static final String TAG = "OmsSplitReporter";
    private final Context mContext;

    public OmsSplitReporter(Context context) {
        this.mContext = context;
    }

    @Override // com.oplus.oms.split.splitreport.SplitReporter
    public void onHandleInfo(String tag, SplitReporterInfo info) {
        OmsStatisticsManager.getInstance().reporterSplitInfo(this.mContext, tag, info);
    }

    @Override // com.oplus.oms.split.splitreport.SplitReporter
    public void onBatchHandleInfo(String tag, List<SplitReporterInfo> infoList) {
        SplitLog.d(TAG, "onBatchHandleInfo tag:" + tag + ",list:" + infoList, new Object[0]);
        OmsStatisticsManager.getInstance().reporterSplitInfo(this.mContext, tag, infoList);
    }

    @Override // com.oplus.oms.split.splitreport.SplitReporter
    public void onHandleResult(String tag, int errorCode) {
        OmsStatisticsManager.getInstance().reporterResultCode(this.mContext, tag, errorCode);
    }
}
