package com.android.server.alarm;

import android.app.PendingIntent;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public interface IAlarmExt {
    default void alarmToStringExtend(StringBuilder sb, long j, long j2, long j3, long j4, PendingIntent pendingIntent, String str, int i, int i2) {
    }

    default String getAction() {
        return null;
    }

    default String getComponent() {
        return null;
    }

    default String getProcName() {
        return null;
    }

    default String getTag() {
        return null;
    }

    default void init(String str, String str2, String str3, String str4) {
    }
}
