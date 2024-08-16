package com.android.server.wm;

import android.app.ActivityOptions;
import android.app.ITaskStackListener;
import android.app.PictureInPictureParams;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.os.RemoteException;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
public interface IActivityTaskManagerServiceExt {
    default void adjustConfigurationForUser(ContentResolver contentResolver, Configuration configuration, int i) {
    }

    default int adjustUserIdIfNeed(Intent intent, int i) {
        return i;
    }

    default void applySleepTokens(boolean z) {
    }

    default void checkGoToSleep(ActivityRecord activityRecord, int i) {
    }

    default boolean checkOplusWindowPermission() {
        return false;
    }

    default boolean checkSetTaskWindowingMode(Task task) {
        return false;
    }

    default void clearCacheWhenOnConfigurationChange(Configuration configuration, int i) {
    }

    default void clearCompactWindowModeWhenUpdateConfiguration(Configuration configuration, Configuration configuration2) {
    }

    default void clearSnapshotCacheForPackage(String str) {
    }

    default void exitRunningScheduler() {
    }

    default ActivityTaskManagerService getActivityTaskManagerService(Context context) {
        return null;
    }

    default IRemoteTaskHandlerManagerExt getRemoteTaskManager() {
        return null;
    }

    default String getTaskStackListenerDescriptor(ITaskStackListener iTaskStackListener) {
        return null;
    }

    default void handleCompatibilityException(int i, String str) {
    }

    default void handleExtraConfigurationChanges(int i, Configuration configuration, Context context, Handler handler, int i2) {
    }

    default void handleForcedResizableTaskIfNeeded(Task task) {
    }

    default void handleUiModeChanged(int i) {
    }

    default void hookAtmsConfigurationChang(int i, RootWindowContainer rootWindowContainer, WindowManagerService windowManagerService, Configuration configuration) {
    }

    default boolean hookAtmssendPutConfigurationForUserMsg(ContentResolver contentResolver, int i, Configuration configuration) {
        return false;
    }

    default void hookInitOplusATMSEnhance(ActivityTaskManagerService activityTaskManagerService) {
    }

    default void hookRecordAppDiedCount(int i, String str, String str2) {
    }

    default boolean inSplitRootTask(WindowContainer windowContainer) {
        return false;
    }

    default void init(Context context) {
    }

    default void initBurmeseConfigForUser(ContentResolver contentResolver, Configuration configuration) {
    }

    default boolean interceptEnterPictureInPictureMode(ActivityRecord activityRecord, PictureInPictureParams pictureInPictureParams) {
        return false;
    }

    default boolean interceptHandleAppDied(WindowProcessController windowProcessController, boolean z, boolean z2) {
        return false;
    }

    default boolean interceptOnForceStopPackage(String str, int i) {
        return false;
    }

    default boolean isFromViewExtract(boolean z, Bundle bundle) {
        return z;
    }

    default boolean isIOPreloadPkg(String str, int i) {
        return false;
    }

    default boolean ismOplusActivityControlerSchedulerexist() {
        return false;
    }

    default void moveTaskToDefaultDisplaySplitScreenSPrimaryTask(Task task, boolean z, ActivityTaskManagerService activityTaskManagerService) {
    }

    default void moveTaskToDefaultDisplaySplitScreenSecondaryTask(Task task, boolean z, ActivityTaskManagerService activityTaskManagerService) {
    }

    default void notifySysActivityHotLaunch(Class cls, ActivityRecord activityRecord, Task task) {
    }

    default void onConfigurationChanged(Configuration configuration) {
    }

    default void onOplusStart() {
    }

    default void onPackageUninstalled(String str) {
    }

    default void onPreBindApplication(WindowProcessController windowProcessController) {
    }

    default void onProcessUnMapped(WindowProcessController windowProcessController) {
    }

    default void onSystemReady() {
    }

    default void publish() {
    }

    default void putTaskStackListenerDescriptor(ITaskStackListener iTaskStackListener, String str) {
    }

    default void removeTaskStackListenerDescriptor(ITaskStackListener iTaskStackListener) {
    }

    default boolean scheduleAppCrash(String str, int i, String str2, String str3, long j, String str4) throws RemoteException {
        return false;
    }

    default void sendApplicationFocusGain(Handler handler, Context context, String str) {
    }

    default boolean setAgingTestLockScreenShown(boolean z) {
        return z;
    }

    default void setBootstage() {
    }

    default void setProcRaiseAdjList(Object obj) {
    }

    default void setScreenOffPlay(boolean z) {
    }

    default void setWindowConfigAndDisplayId(ActivityRecord activityRecord, Configuration configuration, Bundle bundle) {
    }

    default boolean shouldAbortMoveTaskToFront(Task task, ActivityOptions activityOptions) {
        return false;
    }

    default boolean startPairTaskIfNeed(Intent[] intentArr, Bundle bundle, int i) {
        return false;
    }

    default void startSecurityPayService(ActivityRecord activityRecord, ActivityRecord activityRecord2) {
    }

    default int startZoomWindow(Intent intent, Bundle bundle, int i, String str) {
        return -1;
    }

    default void systemReady() {
    }

    default void taskFocusChanged(Task task, Task task2, ActivityRecord activityRecord, String str) {
    }

    default void tryRemoveAllUserRecentTasksLocked() {
    }

    default void updataeAccidentPreventionState(Context context, boolean z, int i, int i2) {
    }

    default void updateBurmeseFontLinkForUser(Configuration configuration, int i, Context context, int i2) {
    }

    default void updateConfigForLauncherLocked(ActivityRecord activityRecord, int i) {
    }

    default void updateExtraConfigurationForUser(Context context, Configuration configuration, int i) {
    }

    default void updateOomAdjForSleep(Runnable runnable) {
    }

    default void updateUserIdInExtraConfiguration(Configuration configuration, int i) {
    }

    default boolean withNoneTransition(ActivityRecord activityRecord, Task task, ActivityOptions activityOptions, int i, String str) {
        return false;
    }
}
