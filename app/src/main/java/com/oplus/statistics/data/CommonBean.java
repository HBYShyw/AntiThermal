package com.oplus.statistics.data;

import android.content.Context;
import com.oplus.statistics.DataTypeConstants;
import com.oplus.statistics.util.CastUtil;
import java.util.Map;

/* loaded from: classes2.dex */
public class CommonBean extends TrackEvent {
    protected static final String APP_ID = "appId";
    protected static final String EVENT_ID = "eventID";
    protected static final String LOG_MAP = "logMap";
    protected static final String LOG_TAG = "logTag";
    private int mAppId;
    private String mEventId;
    protected String mLogMap;
    private String mLogTag;

    public CommonBean(Context context) {
        super(context);
        this.mLogMap = "";
        this.mLogTag = "";
        this.mEventId = "";
        this.mAppId = 0;
    }

    public int getAppID() {
        return this.mAppId;
    }

    public String getEventID() {
        return this.mEventId;
    }

    @Override // com.oplus.statistics.data.TrackEvent
    public int getEventType() {
        return DataTypeConstants.COMMON;
    }

    public String getLogMap() {
        return this.mLogMap;
    }

    public String getLogTag() {
        return this.mLogTag;
    }

    public void setAppId(int i10) {
        this.mAppId = i10;
        addTrackInfo(APP_ID, i10);
    }

    public void setEventID(String str) {
        this.mEventId = str;
        addTrackInfo(EVENT_ID, str);
    }

    public void setLogMap(Map<String, String> map) {
        String jSONObject = CastUtil.map2JsonObject(map).toString();
        this.mLogMap = jSONObject;
        addTrackInfo(LOG_MAP, jSONObject);
    }

    public void setLogTag(String str) {
        this.mLogTag = str;
        addTrackInfo(LOG_TAG, str);
    }

    public String toString() {
        return " type is :" + getEventType() + ", tag is :" + getLogTag() + ", eventID is :" + getEventID() + ", map is :" + getLogMap();
    }

    public void setLogMap(String str) {
        this.mLogMap = str;
        addTrackInfo(LOG_MAP, str);
    }

    public CommonBean(Context context, String str, String str2) {
        super(context);
        this.mLogMap = "";
        this.mAppId = 0;
        this.mLogTag = str;
        this.mEventId = str2;
        addTrackInfo(LOG_TAG, str);
        addTrackInfo(EVENT_ID, this.mEventId);
    }

    public CommonBean(Context context, String str, String str2, String str3) {
        super(context);
        this.mLogMap = "";
        this.mAppId = 0;
        this.mLogTag = str2;
        this.mEventId = str3;
        setAppId(str);
        addTrackInfo(LOG_TAG, this.mLogTag);
        addTrackInfo(EVENT_ID, this.mEventId);
    }
}
