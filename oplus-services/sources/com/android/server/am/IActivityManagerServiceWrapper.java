package com.android.server.am;

import android.app.IApplicationThread;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.os.IBinder;
import android.os.IProgressListener;
import android.util.ArrayMap;
import java.util.List;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public interface IActivityManagerServiceWrapper {
    default void addBootEvent(String str) {
    }

    default void addServiceToMap(ArrayMap<String, IBinder> arrayMap, String str) {
    }

    default void cleanupDisabledPackageComponentsLocked(String str, int i, String[] strArr) {
    }

    default List<ResolveInfo> collectReceiverComponents(Intent intent, String str, int i, int[] iArr, int[] iArr2) {
        return null;
    }

    default void dynamicalConfigLog(String str, IApplicationThread iApplicationThread, boolean z) {
    }

    default void forceStopPackageLocked(String str, int i) {
    }

    default boolean forceStopPackageLocked(String str, int i, boolean z, boolean z2, boolean z3, boolean z4, boolean z5, int i2, String str2) {
        return false;
    }

    default Object getAmsExt() {
        return null;
    }

    default Object getAnrManager() {
        return null;
    }

    default int getCurrentUserIdLU() {
        return -1;
    }

    default ProcessRecord getTopAppLockedForBroadcast() {
        return null;
    }

    default boolean isCameraActiveForUid(int i) {
        return false;
    }

    default boolean isInRestartingServicesList(String str, int i) {
        return false;
    }

    default void killPackageProcessesLocked(String str, int i, int i2, int i3, int i4, int i5, String str2) {
    }

    default void removeDyingProviderLocked(ProcessRecord processRecord, ContentProviderRecord contentProviderRecord, boolean z) {
    }

    default void removeRecentTasksByPackageName(String str, int i) {
    }

    default void removeUriPermissionsForPackage(String str, int i, boolean z, boolean z2) {
    }

    default boolean startUser(int i, int i2, IProgressListener iProgressListener) {
        return false;
    }

    default void trimApplications(boolean z, int i) {
    }
}
