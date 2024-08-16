package com.android.server.display;

import java.util.ArrayDeque;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public class HighBrightnessModeMetadata {
    private final ArrayDeque<HbmEvent> mEvents = new ArrayDeque<>();
    private long mRunningStartTimeMillis = -1;

    public long getRunningStartTimeMillis() {
        return this.mRunningStartTimeMillis;
    }

    public void setRunningStartTimeMillis(long j) {
        this.mRunningStartTimeMillis = j;
    }

    public ArrayDeque<HbmEvent> getHbmEventQueue() {
        return this.mEvents;
    }

    public void addHbmEvent(HbmEvent hbmEvent) {
        this.mEvents.addFirst(hbmEvent);
    }
}
