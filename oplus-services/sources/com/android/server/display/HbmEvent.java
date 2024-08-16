package com.android.server.display;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
class HbmEvent {
    private long mEndTimeMillis;
    private long mStartTimeMillis;

    /* JADX INFO: Access modifiers changed from: package-private */
    public HbmEvent(long j, long j2) {
        this.mStartTimeMillis = j;
        this.mEndTimeMillis = j2;
    }

    public long getStartTimeMillis() {
        return this.mStartTimeMillis;
    }

    public long getEndTimeMillis() {
        return this.mEndTimeMillis;
    }

    public String toString() {
        return "HbmEvent: {startTimeMillis:" + this.mStartTimeMillis + ", endTimeMillis: " + this.mEndTimeMillis + "}, total: " + ((this.mEndTimeMillis - this.mStartTimeMillis) / 1000) + "]";
    }
}
