package com.oplus.statistics.agent;

import android.content.Context;
import com.oplus.statistics.data.CommonBean;
import com.oplus.statistics.record.ProxyRecorder;

/* loaded from: classes2.dex */
public class CommonAgent extends BaseAgent {
    public static void recordCommon(Context context, CommonBean commonBean) {
        ProxyRecorder.getInstance().addTrackEvent(context, commonBean);
    }
}
