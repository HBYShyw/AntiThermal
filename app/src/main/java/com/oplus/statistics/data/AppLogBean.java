package com.oplus.statistics.data;

import android.content.Context;
import com.oplus.statistics.DataTypeConstants;

/* loaded from: classes2.dex */
public class AppLogBean extends TrackEvent {
    private static final String EVENT_BODY = "eventBody";
    private static final String EVENT_TYPE = "eventType";
    private String mBody;
    private String mType;

    public AppLogBean(Context context, String str, String str2) {
        super(context);
        this.mType = str;
        this.mBody = str2;
        addTrackInfo(EVENT_TYPE, str);
        addTrackInfo(EVENT_BODY, this.mBody);
    }

    public String getBody() {
        return this.mBody;
    }

    @Override // com.oplus.statistics.data.TrackEvent
    public int getEventType() {
        return DataTypeConstants.APP_LOG;
    }

    public String getType() {
        return this.mType;
    }

    public void setAppLog(String str) {
        this.mBody = str;
        addTrackInfo(EVENT_BODY, str);
    }

    public void setType(String str) {
        this.mType = str;
        addTrackInfo(EVENT_TYPE, str);
    }

    public String toString() {
        return "type is :" + getEventType() + "\nbody is :" + getBody() + "\n";
    }
}
