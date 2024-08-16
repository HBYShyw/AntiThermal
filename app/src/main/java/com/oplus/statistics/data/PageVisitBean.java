package com.oplus.statistics.data;

import android.content.Context;
import com.oplus.statistics.DataTypeConstants;

/* loaded from: classes2.dex */
public class PageVisitBean extends TrackEvent {
    private static final String PAGE_VISIT_ACTIVIES = "activities";
    private static final String PAGE_VISIT_DURATION = "duration";
    private static final String PAGE_VISIT_TIME = "time";
    private String mActivities;
    private long mDuration;
    private String mTime;

    public PageVisitBean(Context context) {
        super(context);
    }

    public String getActivities() {
        return this.mActivities;
    }

    public long getDuration() {
        return this.mDuration;
    }

    @Override // com.oplus.statistics.data.TrackEvent
    public int getEventType() {
        return DataTypeConstants.PAGE_VISIT;
    }

    public String getTime() {
        return this.mTime;
    }

    public void setActivities(String str) {
        this.mActivities = str;
        addTrackInfo(PAGE_VISIT_ACTIVIES, str);
    }

    public void setDuration(long j10) {
        this.mDuration = j10;
        addTrackInfo("duration", j10);
    }

    public void setTime(String str) {
        this.mTime = str;
        addTrackInfo(PAGE_VISIT_TIME, str);
    }

    public String toString() {
        return "time is :" + getTime() + "\nduration is :" + getDuration() + "\nactivities is :" + getActivities() + "\n";
    }
}
