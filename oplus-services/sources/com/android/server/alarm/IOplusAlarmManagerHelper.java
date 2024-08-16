package com.android.server.alarm;

import android.app.PendingIntent;
import android.common.IOplusCommonFeature;
import android.common.OplusFeatureList;
import android.content.ComponentName;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Slog;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public interface IOplusAlarmManagerHelper extends IOplusCommonFeature {
    public static final long ACME_ALIGN_INTERVAL = 15;
    public static final long ACME_SCREENOFF_TIME = 30;
    public static final long ALIGN_INTERVAL = 5;
    public static final IOplusAlarmManagerHelper DEFAULT = new IOplusAlarmManagerHelper() { // from class: com.android.server.alarm.IOplusAlarmManagerHelper.1
    };
    public static final String NAME = "IOplusAlarmManagerHelper";

    default void addCustomAppAlarmWhiteList(List<String> list) {
    }

    default void alarmToStringExtend(StringBuilder sb, long j, long j2, long j3, long j4, PendingIntent pendingIntent, String str, int i, int i2, String str2, String str3, String str4) {
    }

    default boolean containKeyWord(String str) {
        return false;
    }

    default void dump(PrintWriter printWriter) {
    }

    default void dumpWhiteListVersion(PrintWriter printWriter) {
    }

    default boolean extremeSleepFeature(String str) {
        return false;
    }

    default long getAcmeAlignInterval() {
        return 15L;
    }

    default long getAcmeScreenOffTime() {
        return 30L;
    }

    default long getAlignFirstDelay() {
        return 0L;
    }

    default long getAlignInterval() {
        return 5L;
    }

    default int getPendingJobCount() {
        return -1;
    }

    default long getWindowLengthForDualApps() {
        return 0L;
    }

    default boolean inPackageNameWhiteList(String str) {
        return false;
    }

    default boolean inUidWhiteList(int i) {
        return false;
    }

    default void init(Context context, AlarmManagerService alarmManagerService) {
    }

    default void initSmartDozeAlarmExemptionBroadcast(Context context, Handler handler) {
    }

    default boolean isAcmeBlackWord(String str) {
        return false;
    }

    default boolean isAlignmentForDualAppsEnabled() {
        return false;
    }

    default boolean isBlackJobList(String str, String str2) {
        return false;
    }

    default boolean isFilterRemovePackage(String str) {
        return false;
    }

    default boolean isInAlignEnforcedWhiteList(String str) {
        return false;
    }

    default boolean isInAlignWhiteList(String str) {
        return false;
    }

    default boolean isInDualAppActionBlackList(String str, String str2) {
        return false;
    }

    default boolean isInSuperPowerSaveBlackList(String str) {
        return false;
    }

    default boolean isMatchDeepSleepRule(ComponentName componentName) {
        return false;
    }

    default boolean isMatchDeepSleepRule(String str, String str2, int i) {
        return false;
    }

    default boolean isMatchExDeepsleepBlockRule(String str) {
        return false;
    }

    default boolean isMatchExDeepsleepRule(String str, boolean z, boolean z2) {
        return true;
    }

    default boolean isMatchExsleepAllowRuleJob(ComponentName componentName, boolean z) {
        return true;
    }

    default boolean isMatchExsleepBlockRuleJob(ComponentName componentName, boolean z) {
        return false;
    }

    default boolean isMatchRetoreNetworkRule(String str) {
        return true;
    }

    default void maxAlarmsPerUidHandle(String str, int i, int i2) {
    }

    default void monitorAlarmWakeup(Alarm alarm) {
    }

    default void monitorFrameworkWakeupEvent() {
    }

    default void processDied(String str) {
    }

    default void removeAlarmLocked(int i, int i2) {
    }

    default boolean removeCustomAllAppAlarmWhiteList() {
        return false;
    }

    default boolean removeCustomAppAlarmWhiteList(List<String> list) {
        return false;
    }

    default long setInexactAlarm(long j, String str, String str2, String str3) {
        return j;
    }

    default OplusFeatureList.OplusIndex index() {
        return OplusFeatureList.OplusIndex.IOplusAlarmManagerHelper;
    }

    default IOplusCommonFeature getDefault() {
        return DEFAULT;
    }

    default void init(Context context, AlarmManagerService alarmManagerService, Looper looper) {
        Slog.d(NAME, "init");
    }

    default Integer getAcmeBlackConfig(String str, String str2) {
        return 0;
    }

    default ArrayList<String> getDeepSleepWhiteList() {
        return new ArrayList<>();
    }

    default List<String> getCustomAppAlarmWhiteList() {
        return new ArrayList();
    }
}
