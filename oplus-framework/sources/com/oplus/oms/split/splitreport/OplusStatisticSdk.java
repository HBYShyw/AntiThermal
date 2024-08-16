package com.oplus.oms.split.splitreport;

import android.content.Context;
import java.util.Map;
import oplus.util.OplusStatistics;

/* loaded from: classes.dex */
public class OplusStatisticSdk {
    private OplusStatisticSdk() {
    }

    public static boolean onCommon(Context context, String appId, String logTag, String eventId, Map<String, String> logMap) {
        OplusStatistics.onCommon(context, appId, logTag, eventId, logMap, true);
        return true;
    }
}
