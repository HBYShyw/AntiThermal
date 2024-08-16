package com.android.server.am;

import android.app.ApplicationExitInfo;
import android.os.Bundle;
import android.util.Pair;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public interface IAppExitInfoTrackerExt {
    default void notifyAppExitInfo(ApplicationExitInfo applicationExitInfo) {
    }

    default void notifyAthenaKill(int i, int i2, int i3, int i4, String str) {
    }

    default void notifyOplusExitInfo(ApplicationExitInfo applicationExitInfo, ProcessRecord processRecord) {
    }

    default Pair<String, Pair<Integer, Integer>> removeAthenaKillRecord(int i, int i2) {
        return null;
    }

    default void removeByUid(int i, Integer num, boolean z) {
    }

    default void removeByUserId(int i) {
    }

    default void removeProcessInfo(ProcessRecord processRecord) {
    }

    default Bundle updateAppExitFeature(Bundle bundle) {
        return null;
    }

    default void updateApplicationExitInfo(ApplicationExitInfo applicationExitInfo, int i, int i2, String str) {
    }

    default String updateExitInfoMsg(String str, ProcessRecord processRecord) {
        return str;
    }

    default boolean updateKillReasonInfo(ApplicationExitInfo applicationExitInfo, Integer num) {
        return false;
    }

    default void updateOplusExitInfo(ApplicationExitInfo applicationExitInfo) {
    }
}
