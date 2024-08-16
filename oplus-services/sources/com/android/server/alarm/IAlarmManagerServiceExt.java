package com.android.server.alarm;

import android.app.AlarmManager;
import android.app.IAlarmListener;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.WorkSource;
import com.android.server.alarm.AlarmManagerService;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.ArrayList;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public interface IAlarmManagerServiceExt {
    default int SyncAlarmHandleOnSetImplLocked(int i, PendingIntent pendingIntent, String str, String str2) {
        return i;
    }

    default long adjDeviceIdlePolicyTime(long j, long j2, Alarm alarm) {
        return j;
    }

    default int adjustAlarmFlagsForDualApps(int i) {
        return i;
    }

    default int adjustAlarmFlagsWhenSetImpl(String str, int i, long j, int i2) {
        return i;
    }

    default void adjustAlarmLocked(Alarm alarm, boolean z) {
    }

    default long adjustAlarmWindowLengthForDualApps(long j) {
        return j;
    }

    default void adjustAlarms() {
    }

    default long adjustWindowLengthsWhenSetImpl(PendingIntent pendingIntent, String str, int i, AlarmManager.AlarmClockInfo alarmClockInfo, long j, String str2, String str3) {
        return j;
    }

    default void cancelPoweroffAlarmImpl(String str) {
    }

    default void canceledPendingIntentDetection(Alarm alarm, long j) {
    }

    default void countAlarmWakeup() {
    }

    default void deliverAlarmsLockedEnd() {
    }

    default void deliverAlarmsLockedStart() {
    }

    default void deliverLockedEnd(Alarm alarm, AlarmManagerService.BroadcastStats broadcastStats, long j, boolean z) {
    }

    default boolean dumpImpl(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
        return false;
    }

    default boolean filterAlarmForHans(Alarm alarm) {
        return false;
    }

    default void filterTriggerListForStrictMode(ArrayList<Alarm> arrayList) {
    }

    default String[] getActionComponent(PendingIntent pendingIntent) {
        return null;
    }

    default String getProcessName(int i) {
        return null;
    }

    default void init(Context context, long j) {
    }

    default boolean interceptDeliverAlarmsLockedInLoop(Alarm alarm) {
        return false;
    }

    default boolean interceptPkgBrdcast(Intent intent, Context context) {
        return false;
    }

    default boolean isBackgroundRestricted(Alarm alarm) {
        return false;
    }

    default boolean isDynamicLogEnabled() {
        return false;
    }

    default boolean isFilterRemovePackage(String str) {
        return false;
    }

    default boolean isInSmartDozeEearlyTime() {
        return false;
    }

    default boolean isPowerOffAlarmType(int i) {
        return false;
    }

    default void maxAlarmsPerUidHandle(String str, int i, int i2) {
    }

    default void onAlarmInfoCollect(int i, String str, AlarmStore alarmStore) {
    }

    default void onScreenOff() {
    }

    default void onScreenOn() {
    }

    default void printStackTraceInfo() {
    }

    default void removeAlarmsForUidLocked(int i, int i2) {
    }

    default boolean schedulePoweroffAlarm(int i, long j, long j2, PendingIntent pendingIntent, IAlarmListener iAlarmListener, String str, WorkSource workSource, AlarmManager.AlarmClockInfo alarmClockInfo, String str2) {
        return true;
    }

    default boolean shouldAdjustForDualApps(String str, String str2) {
        return false;
    }

    default void systemServiceReady() {
    }

    default void trackEventCancelAlarmLocked(PendingIntent pendingIntent, IAlarmListener iAlarmListener) {
    }

    default void trackEventRemoveAlarmsForPkgByBroadcastLocked(String str) {
    }

    default void trackEventRemoveAlarmsForPkgByLocalServiceLocked(String str) {
    }

    default void trackEventRemoveAlarmsForUidByAppStandbyLocked(int i) {
    }

    default void trackEventRemoveAlarmsForUidByBroadcastLocked(int i) {
    }

    default void trackEventRemoveAlarmsForUidByLocalServiceLocked(int i) {
    }

    default void trackEventSendAlarmLocked(Alarm alarm) {
    }

    default void trackEventSetAlarmLocked(Alarm alarm) {
    }

    default void updateGoogleAlarmTypeAndTag(Alarm alarm) {
    }

    default void updatePoweroffAlarmtoNowRtc() {
    }
}
