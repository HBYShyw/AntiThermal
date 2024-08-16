package com.oplus.statistics.agent;

import android.content.Context;
import com.oplus.statistics.data.DebugBean;
import com.oplus.statistics.record.ProxyRecorder;

/* loaded from: classes2.dex */
public class DebugAgent {
    public static void setDebug(Context context, boolean z10) {
        ProxyRecorder.getInstance().addTrackEvent(context, new DebugBean(context, z10));
    }
}
