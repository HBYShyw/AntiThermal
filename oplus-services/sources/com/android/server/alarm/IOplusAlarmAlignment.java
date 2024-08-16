package com.android.server.alarm;

import android.common.IOplusCommonFeature;
import android.common.OplusFeatureList;
import android.content.Context;
import android.os.Looper;
import java.io.PrintWriter;
import java.util.Set;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public interface IOplusAlarmAlignment extends IOplusCommonFeature {
    public static final int ALIGN_MODE_EARLY = 0;
    public static final int ALIGN_MODE_INVALID = Integer.MAX_VALUE;
    public static final int ALIGN_MODE_LATE = 2;
    public static final int ALIGN_MODE_NEAR = 1;
    public static final int ALIGN_TYPE_BY_PKG = 0;
    public static final int ALIGN_TYPE_BY_PROC = 1;
    public static final IOplusAlarmAlignment DEFAULT = new IOplusAlarmAlignment() { // from class: com.android.server.alarm.IOplusAlarmAlignment.1
    };
    public static final int MAX_REQUESTERS = 3;
    public static final int MSG_ACME_OFF = 4;
    public static final int MSG_ACME_ON = 3;
    public static final int MSG_ADJUST_ALARMS = 6;
    public static final int MSG_ADJUST_ALL_ALARMS = 5;
    public static final int MSG_SCREEN_OFF = 2;
    public static final int MSG_SCREEN_ON = 1;
    public static final String NAME = "IOplusAlarmAlignment";
    public static final int REQUESTER_OFREEZER = 1;
    public static final int REQUESTER_OGUARD = 2;
    public static final int REQUESTER_SCREENOFF = 0;

    default void adjustAlarmLocked(Alarm alarm) {
    }

    default void adjustAlarms() {
    }

    default void alignAlarmsByUid(int i, String str, Set<String> set, Set<String> set2, int i2) {
    }

    default void dump(PrintWriter printWriter) {
    }

    default void initArgs(Context context, Object obj, AlarmManagerService alarmManagerService, Looper looper) {
    }

    default void onScreenOff() {
    }

    default void onScreenOn() {
    }

    default void proxyAlarmsByUid(int i, String str, int i2) {
    }

    default void unalignAlarmsByUid(int i, String str, Set<String> set, Set<String> set2, int i2) {
    }

    default void unproxyAlarmsByUid(int i, String str, int i2) {
    }

    default OplusFeatureList.OplusIndex index() {
        return OplusFeatureList.OplusIndex.IOplusAlarmAlignment;
    }

    default IOplusCommonFeature getDefault() {
        return DEFAULT;
    }
}
