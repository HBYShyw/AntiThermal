package com.oplus.statistics.data;

import android.content.Context;
import com.oplus.statistics.DataTypeConstants;
import com.oplus.statistics.util.VersionUtil;

/* loaded from: classes2.dex */
public class PeriodDataBean extends CommonBean {
    private final int mEventType;

    public PeriodDataBean(Context context, String str, String str2, String str3) {
        super(context, str, str2);
        int i10 = VersionUtil.isSupportPeriodData(context) ? DataTypeConstants.PERIOD_DATA : DataTypeConstants.COMMON;
        this.mEventType = i10;
        addTrackInfo("dataType", i10);
        setLogMap(str3);
    }

    @Override // com.oplus.statistics.data.CommonBean, com.oplus.statistics.data.TrackEvent
    public int getEventType() {
        return this.mEventType;
    }

    @Override // com.oplus.statistics.data.CommonBean
    public String toString() {
        return " type is :" + getEventType() + ", tag is :" + getLogTag() + ", eventID is :" + getEventID() + ", map is :" + getLogMap();
    }

    public PeriodDataBean(Context context) {
        super(context);
        int i10 = VersionUtil.isSupportPeriodData(context) ? DataTypeConstants.PERIOD_DATA : DataTypeConstants.COMMON;
        this.mEventType = i10;
        addTrackInfo("dataType", i10);
    }
}
