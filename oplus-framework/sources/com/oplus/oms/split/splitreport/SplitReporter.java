package com.oplus.oms.split.splitreport;

import com.oplus.oms.split.splitreport.model.SplitReporterInfo;
import java.util.List;

/* loaded from: classes.dex */
public interface SplitReporter {
    void onBatchHandleInfo(String str, List<SplitReporterInfo> list);

    void onHandleInfo(String str, SplitReporterInfo splitReporterInfo);

    void onHandleResult(String str, int i);
}
