package com.android.server.usage;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public class UnixCalendar {
    public static final long DAY_IN_MILLIS = 86400000;
    public static final long MONTH_IN_MILLIS = 2592000000L;
    public static final long WEEK_IN_MILLIS = 604800000;
    public static final long YEAR_IN_MILLIS = 31536000000L;
    private long mTime;

    public UnixCalendar(long j) {
        this.mTime = j;
    }

    public void addDays(int i) {
        this.mTime += i * 86400000;
    }

    public void addWeeks(int i) {
        this.mTime += i * WEEK_IN_MILLIS;
    }

    public void addMonths(int i) {
        this.mTime += i * MONTH_IN_MILLIS;
    }

    public void addYears(int i) {
        this.mTime += i * 31536000000L;
    }

    public void setTimeInMillis(long j) {
        this.mTime = j;
    }

    public long getTimeInMillis() {
        return this.mTime;
    }
}
