package com.android.server.am;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.os.SystemProperties;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public interface IProcessErrorStateRecordExt {
    public static final boolean isAgingVersion = "1".equals(SystemProperties.get("persist.sys.agingtest"));

    default void clearAnrErrorDialogs(ActivityManagerService activityManagerService, ProcessRecord processRecord) {
    }

    default void clearAnrErrorProgressDialogs(ActivityManagerService activityManagerService, ProcessRecord processRecord) {
    }

    default void dumpStackTraces(int i, ArrayList<Integer> arrayList, ArrayList<Integer> arrayList2, File file) {
    }

    default void dumpSystraceWhenAnr(ActivityManagerService activityManagerService) {
    }

    default void hookANRInfo(int i, int i2, String str) {
    }

    default boolean hookAddANRProc(boolean z) {
        return false;
    }

    default void hookAddAnrAppProcNames(int i, int i2, int i3, ArrayList<Integer> arrayList) {
    }

    default void hookAddFirstPids(String str, ArrayList<Integer> arrayList, int i) {
    }

    default boolean hookAddLikelyIME() {
        return false;
    }

    default boolean hookAddPersistentProc(boolean z) {
        return false;
    }

    default void hookAssertANRInfo(File file, int i) {
    }

    default boolean hookReturnIsInterestProc(ProcessRecord processRecord) {
        return false;
    }

    default void hookSendApplicationStop(ActivityManagerService activityManagerService, ProcessRecord processRecord) {
    }

    default void hookSendTheiaEvent(ProcessRecord processRecord, ActivityManagerService activityManagerService) {
    }

    default void initForAnrStackDump() {
    }

    default boolean isDumpMiddle(ApplicationInfo applicationInfo) {
        return false;
    }

    default boolean isDumpRestart(ApplicationInfo applicationInfo) {
        return false;
    }

    default boolean isOnlyDumpSelf(ApplicationInfo applicationInfo) {
        return false;
    }

    default boolean isTheiaAnrTestApp(String str) {
        return false;
    }

    default void moveAnrTaskToBackIfNeed(ActivityManagerService activityManagerService, ProcessRecord processRecord, boolean z, boolean z2) {
    }

    default void notifyTheiaAnrFinished(int i, int i2, String str, String str2) {
    }

    default void resetProcNumDumpStackPids() {
    }

    default void showAnrErrorDialogs(ActivityManagerService activityManagerService, List<Context> list, ProcessRecord processRecord, int i) {
    }

    default void showAnrErrorProgressDialogs(ActivityManagerService activityManagerService, List<Context> list, ProcessRecord processRecord) {
    }
}
