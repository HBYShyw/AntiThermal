package com.android.server.engineer;

import android.common.IOplusCommonFeature;
import android.common.OplusFeatureList;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.ActivityInfo;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public interface IOplusEngineerService extends IOplusCommonFeature {
    public static final IOplusEngineerService DEFAULT = new IOplusEngineerService() { // from class: com.android.server.engineer.IOplusEngineerService.1
    };
    public static final String Name = "IOplusEngineerService";

    default void init() {
    }

    default void onAdbEnabled(boolean z) {
    }

    default void onPwkPressed() {
    }

    default void onPwkReleased() {
    }

    default void onUsageShutdown() {
    }

    default boolean recordApkDeleteEvent(String str, String str2, String str3) {
        return false;
    }

    default boolean resolveActivityForOtgTest() {
        return false;
    }

    default boolean saveAppUsageHistoryRecord(ComponentName componentName) {
        return false;
    }

    default boolean shouldPreventStartActivity(ActivityInfo activityInfo, String str, int i, int i2) {
        return false;
    }

    default boolean shouldPreventStartService(Intent intent) {
        return false;
    }

    default void tryRemoveAllUserRecentTasksLocked() {
    }

    default OplusFeatureList.OplusIndex index() {
        return OplusFeatureList.OplusIndex.IOplusEngineerService;
    }

    default IOplusCommonFeature getDefault() {
        return DEFAULT;
    }
}
