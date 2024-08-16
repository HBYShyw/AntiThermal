package com.oplus.oms.split.splitreport;

import android.text.TextUtils;
import com.oplus.oms.split.common.SplitInfoUtils;
import com.oplus.oms.split.common.SplitLog;
import com.oplus.oms.split.splitreport.model.SplitReporterInfo;
import java.util.List;

/* loaded from: classes.dex */
public class SplitReportUtil {
    private static final String TAG = "SplitReportUtil";

    public static void reportInstallResult(List<String> moudleNames, String action, int result) {
        String names = SplitInfoUtils.getSplitNames(moudleNames);
        if (TextUtils.isEmpty(names)) {
            SplitLog.w(TAG, "reportInstallResult error", new Object[0]);
            return;
        }
        SplitReporterInfo info = new SplitReporterInfo(names);
        info.setAction(action);
        info.setResultCode(result);
        SplitReporterHelper.reporter(SplitReporterConstant.INSTALL_TAG, info);
    }
}
