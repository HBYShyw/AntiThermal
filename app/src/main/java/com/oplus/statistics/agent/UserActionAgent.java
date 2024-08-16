package com.oplus.statistics.agent;

import android.content.Context;
import com.oplus.statistics.data.UserActionBean;
import com.oplus.statistics.record.ProxyRecorder;
import com.oplus.statistics.util.TimeInfoUtil;

/* loaded from: classes2.dex */
public class UserActionAgent {
    public static void recordUserAction(Context context, int i10, int i11) {
        ProxyRecorder.getInstance().addTrackEvent(context, new UserActionBean(context, i10, TimeInfoUtil.getFormatHour(), i11));
    }
}
