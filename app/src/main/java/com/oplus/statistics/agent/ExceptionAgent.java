package com.oplus.statistics.agent;

import android.content.Context;
import com.oplus.statistics.data.ExceptionBean;
import com.oplus.statistics.record.ProxyRecorder;

/* loaded from: classes2.dex */
public class ExceptionAgent {
    public static void recordException(Context context, ExceptionBean exceptionBean) {
        ProxyRecorder.getInstance().addTrackEvent(context, exceptionBean);
    }
}
