package com.android.server.am;

import android.content.pm.ProviderInfo;
import com.android.server.wm.ActivityTaskManagerService;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public interface IContentProviderHelperExt {
    default void handleReturnHolder(ProviderInfo providerInfo, String str, int i, boolean z) {
    }

    default boolean hookAgingVersionWait(String str) {
        return false;
    }

    default void hookComsumeTokenIfNeeded(ActivityManagerService activityManagerService, ContentProviderRecord contentProviderRecord, ProviderInfo providerInfo, int i, String str) {
    }

    default void hookGetContentProviderImplAfterStartProc(ProcessRecord processRecord, ContentProviderRecord contentProviderRecord) {
    }

    default boolean hookGetProviderAndInfo(ContentProviderRecord contentProviderRecord, int i) {
        return false;
    }

    default boolean hookGetProviderAndInfo(ContentProviderRecord contentProviderRecord, int i, ActivityTaskManagerService activityTaskManagerService, String str, int i2) {
        return false;
    }

    default boolean hookHansProviderIfNeeded(ProviderInfo providerInfo, int i, String str) {
        return false;
    }

    default boolean hookPreloadProviderBlock(int i, ProviderInfo providerInfo, int i2, String str, ProcessRecord processRecord, ContentProviderRecord contentProviderRecord) {
        return false;
    }

    default void noteAssociation(int i, int i2, boolean z) {
    }
}
