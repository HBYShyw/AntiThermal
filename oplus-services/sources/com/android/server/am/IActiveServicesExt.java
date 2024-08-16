package com.android.server.am;

import android.app.IApplicationThread;
import android.app.IServiceConnection;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.os.IBinder;
import com.android.server.am.ActiveServices;
import com.android.server.wm.ActivityServiceConnectionsHolder;
import java.util.ArrayList;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public interface IActiveServicesExt {
    default boolean checkAllowIfAppStartModeNotNormal(String str, Intent intent, ServiceRecord serviceRecord) {
        return false;
    }

    default boolean delayRestartServices(ServiceRecord serviceRecord, ActivityManagerService activityManagerService) {
        return false;
    }

    default void handleAfterBindInnerService(ServiceRecord serviceRecord, String str, int i, boolean z) {
    }

    default void handleAfterStartInnerService(ComponentName componentName, ServiceRecord serviceRecord, String str, int i, boolean z) {
    }

    default void handleExceptionWhenBringUpService(ServiceRecord serviceRecord, boolean z) {
    }

    default void hookBindServiceAfterStartAssociation(ConnectionRecord connectionRecord, AppBindRecord appBindRecord, ServiceRecord serviceRecord) {
    }

    default void hookBindServiceLockedEnd(int i, Intent intent, ServiceRecord serviceRecord) {
    }

    default void hookBringUpServicesAfterStartProc(ServiceRecord serviceRecord, int i, int i2) {
    }

    default void hookKillServicesWhenRemoveServiceConnection(ProcessRecord processRecord, long j) {
    }

    default void hookPerformRestartServiceBegin(ServiceRecord serviceRecord) {
    }

    default boolean hookRetrieveServiceChangeUserIdToSystemIfNeed(ResolveInfo resolveInfo, int i, int i2) {
        return false;
    }

    default void hookScheduleServiceRestart(ServiceRecord serviceRecord, long j, long j2) {
    }

    default void hookStartServiceLockedBegin(Context context, Intent intent, int i) {
    }

    default void hookUnBindServiceLockedAfterRemoveConnection(ConnectionRecord connectionRecord) {
    }

    default void hookUpdateServiceBindStatus(ServiceRecord serviceRecord, String str, boolean z) {
    }

    default boolean interceptBindServiceLockedBeforeConnection(ActivityManagerService activityManagerService, int i, int i2, String str, ServiceRecord serviceRecord, IApplicationThread iApplicationThread, IBinder iBinder, Intent intent, String str2, IServiceConnection iServiceConnection, long j, String str3, int i3, ProcessRecord processRecord) {
        return false;
    }

    default boolean interceptBindServiceLockedBegin(Context context, Intent intent, int i, ProcessRecord processRecord, String str) {
        return false;
    }

    default boolean interceptBringDownDisabledPackageServicesBeforeBringDown(ActiveServices.ServiceMap serviceMap, ServiceRecord serviceRecord) {
        return false;
    }

    default boolean interceptBringDownServiceIfNeeded(ActiveServices.ServiceMap serviceMap, ServiceRecord serviceRecord) {
        return false;
    }

    default boolean interceptBringUpServices(ServiceRecord serviceRecord, ActivityManagerService activityManagerService, int i, int i2) {
        return false;
    }

    default boolean interceptProcessStartTimedOutBeforeBringDown(ActiveServices.ServiceMap serviceMap, ServiceRecord serviceRecord) {
        return false;
    }

    default boolean interceptStartServiceLockedAfterStartMode(int i, int i2, String str, ServiceRecord serviceRecord, Intent intent) {
        return false;
    }

    default boolean interceptStartServiceLockedBeforeStartInner(ActivityManagerService activityManagerService, ServiceRecord serviceRecord, IApplicationThread iApplicationThread, Intent intent, String str, boolean z, String str2, int i, int i2, int i3, String str3) {
        return false;
    }

    default boolean interceptStartServiceLockedIfCallerNotNull(Intent intent, ProcessRecord processRecord, String str) {
        return false;
    }

    default boolean logFgsBackgroundStart() {
        return true;
    }

    default void noteAssociation(int i, int i2, boolean z) {
    }

    default void onServiceConnectionInfoCollect(String str, int i) {
    }

    default void removeCallerAppPackage(ServiceRecord serviceRecord) {
    }

    default ConnectionRecord retrieveConnectionRecordLocked(ArrayList<ConnectionRecord> arrayList, IBinder iBinder, AppBindRecord appBindRecord, ActivityServiceConnectionsHolder<ConnectionRecord> activityServiceConnectionsHolder, long j, int i, PendingIntent pendingIntent, int i2, String str, String str2) {
        return null;
    }

    default void setActiveServicesDynamicalLogEnable(boolean z) {
    }

    default void setCallerAppPackage(ServiceRecord serviceRecord, String str) {
    }

    default boolean setTimeoutNeededToFalseIfNeed(ServiceRecord serviceRecord, boolean z, String str) {
        return false;
    }

    default boolean skipStopInBackgroundBegin(ServiceRecord serviceRecord, int i) {
        return false;
    }

    default void updateExecutingComponent(int i, String str, int i2) {
    }
}
