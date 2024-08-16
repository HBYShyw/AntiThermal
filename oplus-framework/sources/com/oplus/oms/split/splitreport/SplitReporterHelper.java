package com.oplus.oms.split.splitreport;

import com.oplus.oms.split.common.SplitLog;
import com.oplus.oms.split.splitreport.model.SplitReporterInfo;
import java.util.List;

/* loaded from: classes.dex */
public class SplitReporterHelper {
    private static final String TAG = "SplitReporterHelper";

    private SplitReporterHelper() {
    }

    public static void reporter(String tag, List<SplitReporterInfo> infoList) {
        SplitReporter reporter = SplitReporterManager.getSplitReporter();
        if (reporter == null) {
            SplitLog.w(TAG, tag + " reporter null.", new Object[0]);
        } else {
            reporter.onBatchHandleInfo(tag, infoList);
        }
    }

    public static void reporter(String tag, SplitReporterInfo reporterInfo) {
        SplitReporter reporter = SplitReporterManager.getSplitReporter();
        if (reporter == null) {
            SplitLog.w(TAG, tag + " reporter null.", new Object[0]);
        } else {
            reporter.onHandleInfo(tag, reporterInfo);
        }
    }

    public static void reporter(String tag, int errorCode) {
        SplitReporter reporter = SplitReporterManager.getSplitReporter();
        if (reporter == null) {
            SplitLog.w(TAG, tag + " reporter null.", new Object[0]);
        } else {
            reporter.onHandleResult(tag, errorCode);
        }
    }
}
