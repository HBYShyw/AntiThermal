package com.android.server.alarm;

import android.app.PendingIntent;
import com.android.server.alarm.AlarmManagerService;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public interface IAlarmManagerServiceWrapper {
    default boolean adjustDeliveryTimeBasedOnDeviceIdle(Alarm alarm) {
        return false;
    }

    default IAlarmManagerServiceExt getExt() {
        return null;
    }

    default int set(long j, int i, long j2, long j3) {
        return -1;
    }

    default void setImplLocked(Alarm alarm) {
    }

    default void updateNextAlarmClockLocked() {
    }

    default AlarmManagerService.BroadcastStats getStatsLocked(PendingIntent pendingIntent) {
        return new AlarmManagerService.BroadcastStats(0, "");
    }
}
