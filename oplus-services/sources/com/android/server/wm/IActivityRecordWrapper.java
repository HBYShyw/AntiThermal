package com.android.server.wm;

import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.res.Configuration;
import android.util.MergedConfiguration;
import android.window.RemoteTransition;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
public interface IActivityRecordWrapper {
    default ActivityRecord getAppToken() {
        return null;
    }

    default ApplicationInfo getAppliationInfo() {
        return null;
    }

    default int getConfigurationChanges(Configuration configuration) {
        return 0;
    }

    default Intent getIntent() {
        return null;
    }

    default MergedConfiguration getLastReportedConfiguration() {
        return null;
    }

    default long getLaunchTickTime() {
        return 0L;
    }

    default String getLaunchedFromPackage() {
        return null;
    }

    default int getLaunchedFromPid() {
        return -1;
    }

    default int getLaunchedFromUid() {
        return -1;
    }

    default String getPackageName() {
        return null;
    }

    default RemoteTransition getPendingRemoteTransition() {
        return null;
    }

    default int getPid() {
        return 0;
    }

    default String getProcessName() {
        return null;
    }

    default String getResultToPackageName() {
        return null;
    }

    default int getResultToUserId() {
        return 0;
    }

    default int getUid() {
        return 0;
    }

    default String getshortComponentName() {
        return null;
    }

    default boolean isActivityTypeHome() {
        return false;
    }

    default boolean isNowVisible() {
        return false;
    }

    default boolean shouldRelaunchLocked(int i, Configuration configuration) {
        return false;
    }

    default IActivityRecordExt getExtImpl() {
        return new IActivityRecordExt() { // from class: com.android.server.wm.IActivityRecordWrapper.1
        };
    }

    default IActivityRecordSocExt getSocExtImpl() {
        return new IActivityRecordSocExt() { // from class: com.android.server.wm.IActivityRecordWrapper.2
        };
    }
}
