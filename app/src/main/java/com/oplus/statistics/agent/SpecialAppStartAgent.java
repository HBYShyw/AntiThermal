package com.oplus.statistics.agent;

import android.content.Context;
import com.oplus.statistics.data.AppStartBean;
import com.oplus.statistics.record.ProxyRecorder;
import com.oplus.statistics.util.TimeInfoUtil;

/* loaded from: classes2.dex */
public class SpecialAppStartAgent {
    public static void onSpecialAppStart(Context context, int i10) {
        AppStartBean appStartBean = new AppStartBean(context, TimeInfoUtil.getFormatTime());
        appStartBean.setAppId(String.valueOf(i10));
        ProxyRecorder.getInstance().addTrackEvent(context, appStartBean);
    }
}
