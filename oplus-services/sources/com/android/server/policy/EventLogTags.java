package com.android.server.policy;

import android.util.EventLog;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public class EventLogTags {
    public static final int INTERCEPT_POWER = 70001;
    public static final int SCREEN_TOGGLED = 70000;

    private EventLogTags() {
    }

    public static void writeScreenToggled(int i) {
        EventLog.writeEvent(SCREEN_TOGGLED, i);
    }

    public static void writeInterceptPower(String str, int i, int i2) {
        EventLog.writeEvent(INTERCEPT_POWER, str, Integer.valueOf(i), Integer.valueOf(i2));
    }
}
