package com.android.server.appwidget;

import android.content.pm.ResolveInfo;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public interface IAppWidgetServiceImplExt {
    default int hookGetRepeatAlarmType(int i) {
        return i;
    }

    default String hookHostPackageNameOnReadProfileState(String str) {
        return str;
    }

    default void hookUpdateWidgetSate(int i, String str, boolean z) {
    }

    default boolean hookaddProviderLocked(ResolveInfo resolveInfo) {
        return false;
    }

    default int hookqueryIntent(int i) {
        return i;
    }

    default void notifyBindAppWidget(String str, int i) {
    }

    default void notifyBindLoadedWidgets(String str, int i) {
    }

    default void notifyClearWidgetsLocked() {
    }

    default void notifyOnWidgetProviderAddedOrChangedLocked(int i, int i2, String str, boolean z) {
    }

    default void notifyRemoveAppWidget(int i, String str, int i2) {
    }

    default void notifyRestoreAppWidget(String str, int i) {
    }

    default void notifyUpdateAppWidgetTimeLocked(int i) {
    }
}
