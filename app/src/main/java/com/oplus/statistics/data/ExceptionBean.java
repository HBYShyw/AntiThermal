package com.oplus.statistics.data;

import android.content.Context;
import com.oplus.statistics.DataTypeConstants;

/* loaded from: classes2.dex */
public class ExceptionBean extends TrackEvent {
    private static final String EXCEPTION = "exception";
    private static final String EXCEPTION_COUNT = "count";
    private static final String EXCEPTION_TIME = "time";
    private int mCount;
    private long mEventTime;
    private String mException;

    public ExceptionBean(Context context) {
        super(context);
    }

    public int getCount() {
        return this.mCount;
    }

    public long getEventTime() {
        return this.mEventTime;
    }

    @Override // com.oplus.statistics.data.TrackEvent
    public int getEventType() {
        return DataTypeConstants.EXCEPTION;
    }

    public String getException() {
        return this.mException;
    }

    public void setCount(int i10) {
        this.mCount = i10;
        addTrackInfo(EXCEPTION_TIME, i10);
    }

    public void setEventTime(long j10) {
        this.mEventTime = j10;
        addTrackInfo(EXCEPTION_TIME, j10);
    }

    public void setException(String str) {
        this.mException = str;
        addTrackInfo(EXCEPTION, str);
    }

    public String toString() {
        return "exception is :" + getException() + "\ncount is :" + getCount() + "\ntime is :" + getEventTime() + "\n";
    }
}
