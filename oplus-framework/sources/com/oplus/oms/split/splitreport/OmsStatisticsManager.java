package com.oplus.oms.split.splitreport;

import android.content.Context;
import com.oplus.oms.split.common.SplitLog;
import com.oplus.oms.split.splitreport.model.SplitReporterInfo;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/* loaded from: classes.dex */
public class OmsStatisticsManager {
    private static final int DEFAULT_DATA_MAP_SIZE = 1;
    private static final String TAG = "OmsStatisticsManager";

    private OmsStatisticsManager() {
    }

    public static OmsStatisticsManager getInstance() {
        return ClassHolder.INSTANCE;
    }

    public void reporterSplitInfo(Context context, String tag, List<SplitReporterInfo> reporterInfoList) {
        if (context == null || reporterInfoList == null || reporterInfoList.isEmpty()) {
            SplitLog.w(TAG, tag + " reporter split list params error", new Object[0]);
            return;
        }
        SplitLog.d(TAG, "reporter " + tag + " split list data:" + reporterInfoList, new Object[0]);
        HashMap<String, String> data = new HashMap<>(1);
        for (SplitReporterInfo info : reporterInfoList) {
            if (info != null) {
                data.clear();
                data.put(tag, info.toReporterString());
                OplusStatisticSdk.onCommon(context, OmsStatisticsConstants.STATISTICS_APP_ID, OmsStatisticsConstants.OMS_EVENT_GROUP_ID, OmsStatisticsConstants.OMS_SPLIT_EVENT_ID, data);
            }
        }
    }

    public void reporterSplitInfo(Context context, String tag, SplitReporterInfo reporterInfo) {
        if (context == null || reporterInfo == null) {
            SplitLog.w(TAG, tag + " reporter split params error", new Object[0]);
            return;
        }
        HashMap<String, String> data = new HashMap<>(1);
        data.put(tag, reporterInfo.toReporterString());
        SplitLog.d(TAG, "reporter " + tag + " split data:" + data, new Object[0]);
        if (!data.isEmpty()) {
            OplusStatisticSdk.onCommon(context, OmsStatisticsConstants.STATISTICS_APP_ID, OmsStatisticsConstants.OMS_EVENT_GROUP_ID, OmsStatisticsConstants.OMS_SPLIT_EVENT_ID, data);
        } else {
            SplitLog.d(TAG, "no statistics data need to upload.", new Object[0]);
        }
    }

    public void reporterResultCode(Context context, String tag, int resultCode) {
        if (context == null) {
            SplitLog.w(TAG, tag + " reporter split result params error", new Object[0]);
            return;
        }
        SplitLog.d(TAG, "reporter " + tag + " result data:" + resultCode, new Object[0]);
        HashMap<String, String> data = new HashMap<>(1);
        data.put(tag, String.valueOf(resultCode));
        OplusStatisticSdk.onCommon(context, OmsStatisticsConstants.STATISTICS_APP_ID, OmsStatisticsConstants.OMS_EVENT_GROUP_ID, OmsStatisticsConstants.OMS_SPLIT_EVENT_ID, data);
    }

    public void reporterOmsInfo(Context context, String tag, Map<String, String> extra) {
        if (context == null || extra == null || extra.isEmpty()) {
            SplitLog.w(TAG, tag + " reporter oms client error.", new Object[0]);
            return;
        }
        SplitLog.d(TAG, tag + " reporter oms info:" + extra, new Object[0]);
        HashMap<String, String> data = new HashMap<>();
        data.put(tag, extra.toString());
        OplusStatisticSdk.onCommon(context, OmsStatisticsConstants.STATISTICS_APP_ID, OmsStatisticsConstants.OMS_EVENT_GROUP_ID, OmsStatisticsConstants.OMS_CLIENT_EVENT_ID, data);
    }

    /* loaded from: classes.dex */
    private static class ClassHolder {
        private static final OmsStatisticsManager INSTANCE = new OmsStatisticsManager();

        private ClassHolder() {
        }
    }
}
