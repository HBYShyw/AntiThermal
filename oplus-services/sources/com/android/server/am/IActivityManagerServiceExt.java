package com.android.server.am;

import android.app.ApplicationErrorReport;
import android.app.BackgroundStartPrivileges;
import android.app.BroadcastOptions;
import android.app.IApplicationThread;
import android.app.usage.UsageStatsManagerInternal;
import android.content.ComponentName;
import android.content.Context;
import android.content.IIntentReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ApplicationInfo;
import android.content.pm.IPackageDataObserver;
import android.content.pm.InstrumentationInfo;
import android.content.pm.ResolveInfo;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Parcel;
import android.os.RemoteException;
import android.os.SystemProperties;
import com.android.server.IntentResolver;
import com.android.server.am.ActivityManagerService;
import com.android.server.am.AppErrorDialog;
import com.android.server.pm.snapshot.PackageDataSnapshot;
import com.android.server.wm.ActivityTaskManagerService;
import com.android.server.wm.WindowProcessController;
import java.io.File;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public interface IActivityManagerServiceExt {
    public static final int OPLUS_AMS_BG_HANDLER = 3;
    public static final int OPLUS_AMS_KILL_HANDLER = 4;
    public static final int OPLUS_AMS_MAIN_HANLDER = 1;
    public static final int OPLUS_AMS_MSG_INDEX = 500;
    public static final int OPLUS_AMS_UI_HANDLER = 2;
    public static final String TAG = "ActivityManager";
    public static final int TYPE_DUMP_HEAP = 1;
    public static final int TYPE_DUMP_MEM = 2;
    public static final int POWER_KEY_DUMP = SystemProperties.getInt("persist.sys.powerkeydump", 0);
    public static final boolean DEBUG_OPLUS_AMS = SystemProperties.getBoolean("persist.sys.assert.panic", false);

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public interface IStaticExt {
        default boolean checkSafeWindowPermission(String str, int i) {
            return false;
        }

        default void handleProcessStop(ProcessRecord processRecord, int i) {
        }

        default boolean isSkipAnrDump() {
            return false;
        }

        default void writeTransactionToTrace(String str) {
        }
    }

    default void activityPreloadHandleAppDied(String str, int i, int i2) {
    }

    default void addCustomServiceToMap() {
    }

    default void addMonitor(Object obj) {
    }

    default boolean addProxyBinder(IBinder iBinder, int i, int i2) {
        return false;
    }

    default void addTimeInfo(StringBuilder sb) {
    }

    default int adjustQueryReceiverPmFlags(int i) {
        return i;
    }

    default List adjustQueueOrderedBroadcastLocked(BroadcastQueue broadcastQueue, Intent intent, ProcessRecord processRecord, String str, int i, int i2, boolean z, String str2, String[] strArr, String[] strArr2, String[] strArr3, int i3, BroadcastOptions broadcastOptions, List list, IIntentReceiver iIntentReceiver, int i4, String str3, Bundle bundle, boolean z2, boolean z3, boolean z4, int i5, BackgroundStartPrivileges backgroundStartPrivileges, boolean z5) {
        return list;
    }

    default List adjustReceiverList(List list, Intent intent) {
        return list;
    }

    default void appendCpuInfo(StringBuilder sb, String str) {
    }

    default void benchStepCheck(Context context, Intent intent) {
    }

    default void broadcastIntentLocked(Intent intent, int i, String str, int i2) {
    }

    default BroadcastQueue broadcastSpecialIntent(Intent intent, int i, boolean z, String str) {
        return null;
    }

    default void cameraActiveChanged(int i) {
    }

    default void cancelCheck(ProcessRecord processRecord) {
    }

    default void checkAndRemoveInstallKillObj(Handler handler, String str) {
    }

    default void cleanupAppByProvider(ActivityManagerService activityManagerService, ContentProviderRecord contentProviderRecord, String str, int i) {
    }

    default void clearCustomUIMode(String str, int i) {
    }

    default void collectExceptionStatistics(SecurityException securityException, String str) {
    }

    default List<BroadcastFilter> collectReceivers(PackageDataSnapshot packageDataSnapshot, Intent intent, int i, int i2, int[] iArr, IntentResolver<BroadcastFilter, BroadcastFilter> intentResolver, String str, List<BroadcastFilter> list) {
        return list;
    }

    default List<ResolveInfo> collectReceivers(List<ResolveInfo> list, Intent intent, String str, int i, int[] iArr, int[] iArr2) {
        return list;
    }

    default BroadcastQueue createOptimizeQueue(Intent intent, boolean z, int i) {
        return null;
    }

    default void debugBroadcast(String str, Intent intent, boolean z, boolean z2, int i, IIntentReceiver iIntentReceiver, int i2, int i3) {
    }

    default void debugReceiverIssue(boolean z, int i) {
    }

    default void dumpActivityAndWindow() {
    }

    default void dumpBinderProxies(PrintWriter printWriter, int i) {
    }

    default boolean dynamicLogDump(ActivityManagerService activityManagerService, String str, FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr, int i) {
        return false;
    }

    default void enableProcessMainThreadLooperLog(PrintWriter printWriter, String[] strArr, int i, ArrayList<ProcessRecord> arrayList) {
    }

    default boolean enforceCallingOplusWindowPermission(ActivityManagerService activityManagerService, String str) {
        return false;
    }

    default void exitOplusFreeform(Bundle bundle) {
    }

    default void filterReceiverBeforeEnqueue(BroadcastRecord broadcastRecord) {
    }

    default boolean forbidClearAppUserData(String str, IPackageDataObserver iPackageDataObserver, int i) {
        return false;
    }

    default List<ApplicationInfo> getAllTopAppInfo() {
        return null;
    }

    default List<String> getAllTopPkgName() {
        return null;
    }

    default Handler getBroadcastHandler(Handler handler) {
        return handler;
    }

    default int getDataFileSizeAjusted(int i, int i2, File file) {
        return -1;
    }

    default ComponentName getDockTopAppName() {
        return null;
    }

    default ApplicationInfo getFreeFormAppInfo() {
        return null;
    }

    default BroadcastQueue getQueueFromFlag(int i) {
        return null;
    }

    default int getWindowMode(IBinder iBinder) throws RemoteException {
        return 0;
    }

    default void grantUriPermissionToUser(IApplicationThread iApplicationThread, String str, Uri uri, int i, int i2) {
    }

    default void handleAppDiedLocked(String str, int i) {
    }

    default void handleApplicationCrash(UsageStatsManagerInternal usageStatsManagerInternal, IBinder iBinder, ProcessRecord processRecord, int i, int i2) {
    }

    default void handleForceStopPackage(String str, int i) {
    }

    default void handleOplusMessage(Message message, int i) {
    }

    default void handlePackageDisabled(String str, int i, boolean z) {
    }

    default void handleProcessDied(ProcessRecord processRecord) {
    }

    default boolean handleServiceTimeOut(Message message) {
        return false;
    }

    default void hookAMSConstructEnd() {
    }

    default void hookAddErrorToDropBox(OplusCrashInfo oplusCrashInfo) {
    }

    default void hookAfterPerformReceive(BroadcastRecord broadcastRecord, BroadcastFilter broadcastFilter, ProcessRecord processRecord) {
    }

    default void hookAttachApplicationLocked(ProcessRecord processRecord) {
    }

    default void hookBeforeCheckExportState(String str, ProcessRecord processRecord, IntentFilter intentFilter, boolean z) {
    }

    default void hookBindBackupAgentAfterStartProc(ProcessRecord processRecord, ApplicationInfo applicationInfo) {
    }

    default void hookBinderProxyCountCallback(Handler handler, int i) {
    }

    default void hookBootCompleted() {
    }

    default void hookCleanUpApplicationRecordAfterRestartProc(ProcessRecord processRecord) {
    }

    default boolean hookDoDump(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr, int i, String str) {
        return false;
    }

    default void hookDumpApplicationMemoryUsage() {
    }

    default void hookHandleAppNotResponding(OplusCrashInfo oplusCrashInfo) {
    }

    default void hookHandleApplicationCrashBeforeInner(ProcessRecord processRecord, ApplicationErrorReport.CrashInfo crashInfo) {
    }

    default void hookHandleApplicationCrashDialog(ProcessRecord processRecord, AppErrorDialog.Data data) {
    }

    default void hookHandlerMarketCrash(String str, ApplicationErrorReport.CrashInfo crashInfo) {
    }

    default void hookInterceptClearUserDataIfNeeded(String str) {
    }

    default boolean hookOnTransact(int i, Parcel parcel, Parcel parcel2, int i2) throws RemoteException {
        return false;
    }

    default void hookSystemReady(Context context, Handler handler, Context context2, ActivityManagerService activityManagerService) {
    }

    default void hookUnbindBackupAgent(ApplicationInfo applicationInfo) {
    }

    default void hookUpdateConfigForFontFlip(Configuration configuration) {
    }

    default void hookUpdateForegroundServiceState(int i, String str, boolean z) {
    }

    default void initAmsExAndInner(Context context, ActivityManagerService activityManagerService, ActivityTaskManagerService activityTaskManagerService) {
    }

    default void initBroadcastAndBootPressure(ActivityManagerService activityManagerService) {
    }

    default void instanceBroadcast(BroadcastConstants broadcastConstants, BroadcastConstants broadcastConstants2) {
    }

    default boolean interceptStartInstrumentation(int i, int i2, ComponentName componentName, InstrumentationInfo instrumentationInfo, ApplicationInfo applicationInfo) {
        return false;
    }

    default boolean isAllowedCallerKillProcess(int i) {
        return false;
    }

    default boolean isChinaModel() {
        return false;
    }

    default void isDisableDelayMCPKill(ActivityManagerService activityManagerService) {
    }

    default boolean isOnBackgroundServiceWhitelist(String str, int i) {
        return false;
    }

    default boolean isReceivingBroadcastLocked(ProcessRecord processRecord) {
        return false;
    }

    default boolean isRecentLockTask(String str, int i) {
        return false;
    }

    default boolean isWaitingPermissionChoice(ProcessRecord processRecord) {
        return false;
    }

    default boolean killBackgroundProcessFilter(String str, int i) {
        return false;
    }

    default void noteAssociation(int i, int i2, boolean z) {
    }

    default void notifyBindApplicationFinished(String str, int i, int i2) {
    }

    default void onBackPressedOnTheiaMonitor(long j) {
    }

    default void onDeathRecipient(ActivityManagerService activityManagerService, ProcessRecord processRecord, int i, IApplicationThread iApplicationThread) {
    }

    default void onOplusStart() {
    }

    default void onOplusSystemReady() {
    }

    default void optimizeVoldMsg(Handler handler, Message message, String str) {
    }

    default void ormsSetNotification(boolean z) {
    }

    default void preBindApplicationInfo(WindowProcessController windowProcessController, ApplicationInfo applicationInfo) {
    }

    default boolean preventSendBroadcast(Intent intent) {
        return false;
    }

    default void publishOplusAmsInternal() {
    }

    default void recordAppCrash(String str, ProcessRecord processRecord) {
    }

    default void recordBootSuccess() {
    }

    default void removeInstallPackageKillObjByPackage(String str) {
    }

    default void removeIsolatedUid(int i, int i2, String str) {
    }

    default boolean removeProxyBinder(IBinder iBinder, int i) {
        return false;
    }

    default void reorderPersistAppsIfNeeded(List<ApplicationInfo> list) {
    }

    default void reportBindApplicationFinished(String str, int i, int i2) {
    }

    default void scheduleNextDispatch(Intent intent) {
    }

    default void sendApplicationStop(Handler handler, Context context, String str, int i) {
    }

    default void sendApplicationStopByForceStop(Handler handler, int i, Context context, String str, ActivityManagerService.PidMap pidMap) {
    }

    default void sendForcestopInfoToPreload(String str, int i, int i2) {
    }

    default void sendTheiaEvent(long j, Intent intent) {
    }

    default void sendTheiaEvent(boolean z, String str, String str2) {
    }

    default boolean setAllowRestartBeforeCleanUpApplicationRecord(boolean z, ProcessRecord processRecord) {
        return z;
    }

    default void setBootstage() {
    }

    default void setConfiguration(Configuration configuration, Configuration configuration2) {
    }

    default void setCrashProcessRecord(ProcessRecord processRecord) {
    }

    default void setErrorPackageName(String str) {
    }

    default void setKeyLockModeNormal(Context context, String str, boolean z) {
    }

    default boolean setRestartAfterCleanUpApplicationRecord(boolean z, ProcessRecord processRecord) {
        return z;
    }

    default boolean setRestartBeforeRestartProc(boolean z, ProcessRecord processRecord) {
        return z;
    }

    default boolean shouldPrintOplusBroadcastLog(Intent intent) {
        return false;
    }

    default int startActivityForFreeform(Intent intent, Bundle bundle, int i, String str) {
        return -1;
    }

    default void storeKillProcessObjForInstallPackageLI(Object obj) {
    }

    default void updateBurmeseConfig(Configuration configuration) {
    }

    default void updateDumpUid(int i, boolean z, int i2) {
    }

    default String updateStopReasonIfNeeded(String str) {
        return str;
    }

    default void waitForDumpCondition(boolean z, String str) {
    }

    default void hookAddErrorToDropBox(Context context, String str, String str2, ProcessRecord processRecord, String str3, File file, ApplicationErrorReport.CrashInfo crashInfo) {
        OplusCrashInfo oplusCrashInfo = new OplusCrashInfo();
        oplusCrashInfo.context = context;
        oplusCrashInfo.dropboxTag = str;
        oplusCrashInfo.eventType = str2;
        oplusCrashInfo.process = processRecord;
        oplusCrashInfo.subject = str3;
        oplusCrashInfo.dataFile = file;
        oplusCrashInfo.crashInfo = crashInfo;
        hookAddErrorToDropBox(oplusCrashInfo);
    }
}
