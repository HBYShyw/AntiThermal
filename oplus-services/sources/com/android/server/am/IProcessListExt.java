package com.android.server.am;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import java.util.ArrayList;
import java.util.List;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public interface IProcessListExt {

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public interface IStaticExt {
        default boolean returnIsNotThreadGroudTid(int i) {
            return false;
        }

        default boolean returnPidNotMatchUid(int i, int i2) {
            return false;
        }
    }

    default void addComputeGids(int i, int i2, ArrayList<Integer> arrayList) {
    }

    default void addGidsForMultiApp(int i, ArrayList<Integer> arrayList) {
    }

    default int customizeExtraFreeKbytes(int i, long j) {
        return i;
    }

    default void customizeMinfreeLevels(int[] iArr, int[] iArr2, long j, Context context) {
    }

    default void decideCleanupAppInLaunchingProvidersLocked(ActivityManagerService activityManagerService, ProcessRecord processRecord) {
    }

    default void decideHandleActivityStart(HostingRecord hostingRecord, ProcessRecord processRecord) {
    }

    default void handleAppZygoteStart(ApplicationInfo applicationInfo) {
    }

    default void hookAddIsolatedUid(int i, int i2, String str) {
    }

    default void hookHandleProcessKilled(boolean z, boolean z2, ProcessRecord processRecord, int i, String str) {
    }

    default void hookHandleProcessStart(ProcessRecord processRecord) {
    }

    default void hookOnSystemReady(ActivityManagerService activityManagerService) {
    }

    default void hookRemoveIsolatedUid(ProcessRecord processRecord) {
    }

    default int hookRuntimeFlags(ActivityManagerService activityManagerService, ProcessRecord processRecord, int i) {
        return i;
    }

    default void hookScheduleApplicationInfoChanged(List<String> list, ApplicationInfo applicationInfo, boolean z, ProcessRecord processRecord, boolean z2) {
    }

    default void hookStartProcessAfterHandleProcessStart(ActiveUids activeUids, ProcessRecord processRecord) {
    }

    default void hookStartProcessAfterHandleProcessStartAsync(ActiveUids activeUids, ProcessRecord processRecord) {
    }

    default void hookStartProcessBeforeCheckPackageStartable(ProcessRecord processRecord, HostingRecord hostingRecord) {
    }

    default boolean interceptStartProcessBeforeHandle(ActivityManagerService activityManagerService, ProcessRecord processRecord, HostingRecord hostingRecord) {
        return false;
    }

    default boolean interceptStartProcessBeforePendingStartCheck(ActivityManagerService activityManagerService, ProcessRecord processRecord, HostingRecord hostingRecord) {
        return false;
    }

    default ProcessRecord replaceProcessRecordAtNewProcessRecord(ApplicationInfo applicationInfo, ActiveUids activeUids, int i, String str, HostingRecord hostingRecord) {
        return null;
    }

    default boolean returnIsFromSwitchUser(List<String> list) {
        return false;
    }

    default boolean returnIsRunningDisallowed(String str) {
        return false;
    }

    default boolean returnKillPackageProcessesFilter(ProcessRecord processRecord, String str, boolean z, int i) {
        return false;
    }

    default int returnRrocessRecordPid(int i) {
        return i;
    }

    default boolean returnSkipForLru(int i, ProcessRecord processRecord) {
        return false;
    }

    default void sendApplicationStartAndDump(ProcessRecord processRecord, int i, ActivityManagerService activityManagerService) {
    }

    default int updateReasonCodeIfNeeded(int i) {
        return i;
    }

    default int updateSubReasonIfNeeded(int i) {
        return i;
    }
}
