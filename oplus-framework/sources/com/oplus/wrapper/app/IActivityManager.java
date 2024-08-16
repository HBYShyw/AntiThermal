package com.oplus.wrapper.app;

import android.app.ActivityManager;
import android.app.ActivityTaskManager;
import android.app.ApplicationErrorReport;
import android.app.ApplicationExitInfo;
import android.app.ApplicationStartInfo;
import android.app.ContentProviderHolder;
import android.app.IActivityController;
import android.app.IActivityManager;
import android.app.IApplicationStartInfoCompleteListener;
import android.app.IForegroundServiceObserver;
import android.app.IInstrumentationWatcher;
import android.app.IProcessObserver;
import android.app.IServiceConnection;
import android.app.IStopUserCallback;
import android.app.IUiAutomationConnection;
import android.app.IUidFrozenStateChangedCallback;
import android.app.IUidObserver;
import android.app.IUserSwitchObserver;
import android.app.Notification;
import android.app.ProfilerInfo;
import android.content.ComponentName;
import android.content.IIntentReceiver;
import android.content.IIntentSender;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.LocusId;
import android.content.pm.ApplicationInfo;
import android.content.pm.IPackageDataObserver;
import android.content.pm.ParceledListSlice;
import android.content.res.Configuration;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.os.Debug;
import android.os.IBinder;
import android.os.IInterface;
import android.os.IProgressListener;
import android.os.ParcelFileDescriptor;
import android.os.RemoteCallback;
import android.os.RemoteException;
import android.os.StrictMode;
import android.os.WorkSource;
import com.android.internal.os.IResultReceiver;
import com.oplus.wrapper.app.IActivityManager;
import com.oplus.wrapper.app.IProcessObserver;
import com.oplus.wrapper.content.pm.IPackageDataObserver;
import com.oplus.wrapper.content.pm.UserInfo;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

/* loaded from: classes.dex */
public interface IActivityManager {
    boolean clearApplicationUserData(String str, boolean z, IPackageDataObserver iPackageDataObserver, int i) throws RemoteException;

    void closeSystemDialogs(String str) throws RemoteException;

    Configuration getConfiguration() throws RemoteException;

    UserInfo getCurrentUser() throws RemoteException;

    String getLaunchedFromPackage(IBinder iBinder) throws RemoteException;

    long[] getProcessPss(int[] iArr) throws RemoteException;

    boolean isInLockTaskMode() throws RemoteException;

    void registerProcessObserver(IProcessObserver iProcessObserver) throws RemoteException;

    boolean removeTask(int i) throws RemoteException;

    void resumeAppSwitches() throws RemoteException;

    void setProcessLimit(int i) throws RemoteException;

    boolean switchUser(int i) throws RemoteException;

    void unregisterProcessObserver(IProcessObserver iProcessObserver) throws RemoteException;

    boolean updateConfiguration(Configuration configuration) throws RemoteException;

    /* loaded from: classes.dex */
    public static abstract class Stub implements IInterface, IActivityManager {
        private final Map<android.app.IProcessObserver, IProcessObserver> mProcessObserverMap = new ConcurrentHashMap();
        private final android.app.IActivityManager mTarget = new AnonymousClass1();

        /* JADX INFO: Access modifiers changed from: package-private */
        /* renamed from: com.oplus.wrapper.app.IActivityManager$Stub$1, reason: invalid class name */
        /* loaded from: classes.dex */
        public class AnonymousClass1 extends IActivityManager.Stub {
            AnonymousClass1() {
            }

            public ParcelFileDescriptor openContentUri(String s) throws RemoteException {
                return null;
            }

            public void registerUidObserver(IUidObserver iUidObserver, int i, int i1, String s) throws RemoteException {
            }

            public void setDeterministicUidIdle(boolean deterministic) throws RemoteException {
            }

            public void removeUidFromObserver(IBinder i, String s, int uid) throws RemoteException {
            }

            public void addUidToObserver(IBinder observerToken, String callingPackage, int uid) throws RemoteException {
            }

            public IBinder registerUidObserverForUids(IUidObserver observer, int which, int cutpoint, String callingPackage, int[] uids) throws RemoteException {
                return null;
            }

            public void unregisterUidObserver(IUidObserver iUidObserver) throws RemoteException {
            }

            public void forceStopPackageEvenWhenStopping(String pkg, int userid) throws RemoteException {
            }

            public boolean isUidActive(int i, String s) throws RemoteException {
                return false;
            }

            public int getUidProcessState(int i, String s) throws RemoteException {
                return 0;
            }

            public int checkPermission(String s, int i, int i1) throws RemoteException {
                return 0;
            }

            public void handleApplicationCrash(IBinder iBinder, ApplicationErrorReport.ParcelableCrashInfo parcelableCrashInfo) throws RemoteException {
            }

            public int startActivity(android.app.IApplicationThread iApplicationThread, String s, Intent intent, String s1, IBinder iBinder, String s2, int i, int i1, ProfilerInfo profilerInfo, Bundle bundle) throws RemoteException {
                return 0;
            }

            public int startActivityWithFeature(android.app.IApplicationThread iApplicationThread, String s, String s1, Intent intent, String s2, IBinder iBinder, String s3, int i, int i1, ProfilerInfo profilerInfo, Bundle bundle) throws RemoteException {
                return 0;
            }

            public void unhandledBack() throws RemoteException {
            }

            public boolean finishActivity(IBinder iBinder, int i, Intent intent, int i1) throws RemoteException {
                return false;
            }

            public Intent registerReceiver(android.app.IApplicationThread iApplicationThread, String s, IIntentReceiver iIntentReceiver, IntentFilter intentFilter, String s1, int i, int i1) throws RemoteException {
                return null;
            }

            public Intent registerReceiverWithFeature(android.app.IApplicationThread iApplicationThread, String s, String s1, String s2, IIntentReceiver iIntentReceiver, IntentFilter intentFilter, String s3, int i, int i1) throws RemoteException {
                return null;
            }

            public void unregisterReceiver(IIntentReceiver iIntentReceiver) throws RemoteException {
            }

            public int broadcastIntent(android.app.IApplicationThread iApplicationThread, Intent intent, String s, IIntentReceiver iIntentReceiver, int i, String s1, Bundle bundle, String[] strings, int i1, Bundle bundle1, boolean b, boolean b1, int i2) throws RemoteException {
                return 0;
            }

            public int broadcastIntentWithFeature(android.app.IApplicationThread caller, String callingFeatureId, Intent intent, String resolvedType, IIntentReceiver resultTo, int resultCode, String resultData, Bundle map, String[] requiredPermissions, String[] excludePermissions, String[] excludePackages, int appOp, Bundle options, boolean serialized, boolean sticky, int userId) throws RemoteException {
                return 0;
            }

            public void unbroadcastIntent(android.app.IApplicationThread iApplicationThread, Intent intent, int i) throws RemoteException {
            }

            public void finishReceiver(IBinder iBinder, int i, String s, Bundle bundle, boolean b, int i1) throws RemoteException {
            }

            public void attachApplication(android.app.IApplicationThread iApplicationThread, long l) throws RemoteException {
            }

            public void finishAttachApplication(long startSeq) throws RemoteException {
            }

            public List<ActivityManager.RunningTaskInfo> getTasks(int i) throws RemoteException {
                return null;
            }

            public void moveTaskToFront(android.app.IApplicationThread iApplicationThread, String s, int i, int i1, Bundle bundle) throws RemoteException {
            }

            public int getTaskForActivity(IBinder iBinder, boolean b) throws RemoteException {
                return 0;
            }

            public ContentProviderHolder getContentProvider(android.app.IApplicationThread iApplicationThread, String s, String s1, int i, boolean b) throws RemoteException {
                return null;
            }

            public void publishContentProviders(android.app.IApplicationThread iApplicationThread, List<ContentProviderHolder> list) throws RemoteException {
            }

            public boolean refContentProvider(IBinder iBinder, int i, int i1) throws RemoteException {
                return false;
            }

            public android.app.PendingIntent getRunningServiceControlPanel(ComponentName componentName) throws RemoteException {
                return null;
            }

            public ComponentName startService(android.app.IApplicationThread iApplicationThread, Intent intent, String s, boolean b, String s1, String s2, int i) throws RemoteException {
                return null;
            }

            public int stopService(android.app.IApplicationThread iApplicationThread, Intent intent, String s, int i) throws RemoteException {
                return 0;
            }

            public int bindService(android.app.IApplicationThread iApplicationThread, IBinder iBinder, Intent intent, String s, IServiceConnection iServiceConnection, long i, String s1, int i1) throws RemoteException {
                return 0;
            }

            public int bindServiceInstance(android.app.IApplicationThread iApplicationThread, IBinder iBinder, Intent intent, String s, IServiceConnection iServiceConnection, long i, String s1, String s2, int i1) throws RemoteException {
                return 0;
            }

            public void updateServiceGroup(IServiceConnection iServiceConnection, int i, int i1) throws RemoteException {
            }

            public boolean unbindService(IServiceConnection iServiceConnection) throws RemoteException {
                return false;
            }

            public void publishService(IBinder iBinder, Intent intent, IBinder iBinder1) throws RemoteException {
            }

            public void setDebugApp(String s, boolean b, boolean b1) throws RemoteException {
            }

            public void setAgentApp(String s, String s1) throws RemoteException {
            }

            public void setAlwaysFinish(boolean b) throws RemoteException {
            }

            public boolean startInstrumentation(ComponentName componentName, String s, int i, Bundle bundle, IInstrumentationWatcher iInstrumentationWatcher, IUiAutomationConnection iUiAutomationConnection, int i1, String s1) throws RemoteException {
                return false;
            }

            public void addInstrumentationResults(android.app.IApplicationThread iApplicationThread, Bundle bundle) throws RemoteException {
            }

            public void finishInstrumentation(android.app.IApplicationThread iApplicationThread, int i, Bundle bundle) throws RemoteException {
            }

            public Configuration getConfiguration() throws RemoteException {
                return Stub.this.getConfiguration();
            }

            public boolean updateConfiguration(Configuration configuration) throws RemoteException {
                return Stub.this.updateConfiguration(configuration);
            }

            public boolean updateMccMncConfiguration(String s, String s1) throws RemoteException {
                return false;
            }

            public boolean stopServiceToken(ComponentName componentName, IBinder iBinder, int i) throws RemoteException {
                return false;
            }

            public void setProcessLimit(int max) throws RemoteException {
                Stub.this.setProcessLimit(max);
            }

            public int getProcessLimit() throws RemoteException {
                return 0;
            }

            public int checkUriPermission(Uri uri, int i, int i1, int i2, int i3, IBinder iBinder) throws RemoteException {
                return 0;
            }

            public int[] checkUriPermissions(List<Uri> list, int i, int i1, int i2, int i3, IBinder iBinder) throws RemoteException {
                return new int[0];
            }

            public void grantUriPermission(android.app.IApplicationThread iApplicationThread, String s, Uri uri, int i, int i1) throws RemoteException {
            }

            public void revokeUriPermission(android.app.IApplicationThread iApplicationThread, String s, Uri uri, int i, int i1) throws RemoteException {
            }

            public void setActivityController(IActivityController iActivityController, boolean b) throws RemoteException {
            }

            public void showWaitingForDebugger(android.app.IApplicationThread iApplicationThread, boolean b) throws RemoteException {
            }

            public void signalPersistentProcesses(int i) throws RemoteException {
            }

            public ParceledListSlice getRecentTasks(int i, int i1, int i2) throws RemoteException {
                return null;
            }

            public void serviceDoneExecuting(IBinder iBinder, int i, int i1, int i2) throws RemoteException {
            }

            public IIntentSender getIntentSender(int i, String s, IBinder iBinder, String s1, int i1, Intent[] intents, String[] strings, int i2, Bundle bundle, int i3) throws RemoteException {
                return null;
            }

            public IIntentSender getIntentSenderWithFeature(int i, String s, String s1, IBinder iBinder, String s2, int i1, Intent[] intents, String[] strings, int i2, Bundle bundle, int i3) throws RemoteException {
                return null;
            }

            public void cancelIntentSender(IIntentSender iIntentSender) throws RemoteException {
            }

            public ActivityManager.PendingIntentInfo getInfoForIntentSender(IIntentSender iIntentSender) throws RemoteException {
                return null;
            }

            public boolean registerIntentSenderCancelListenerEx(IIntentSender iIntentSender, IResultReceiver iResultReceiver) throws RemoteException {
                return false;
            }

            public void unregisterIntentSenderCancelListener(IIntentSender iIntentSender, IResultReceiver iResultReceiver) throws RemoteException {
            }

            public void enterSafeMode() throws RemoteException {
            }

            public void noteWakeupAlarm(IIntentSender iIntentSender, WorkSource workSource, int i, String s, String s1) throws RemoteException {
            }

            public void removeContentProvider(IBinder iBinder, boolean b) throws RemoteException {
            }

            public void setRequestedOrientation(IBinder iBinder, int i) throws RemoteException {
            }

            public void unbindFinished(IBinder iBinder, Intent intent, boolean b) throws RemoteException {
            }

            public void setProcessImportant(IBinder iBinder, int i, boolean b, String s) throws RemoteException {
            }

            public void setServiceForeground(ComponentName componentName, IBinder iBinder, int i, Notification notification, int i1, int i2) throws RemoteException {
            }

            public int getForegroundServiceType(ComponentName componentName, IBinder iBinder) throws RemoteException {
                return 0;
            }

            public boolean moveActivityTaskToBack(IBinder iBinder, boolean b) throws RemoteException {
                return false;
            }

            public void getMemoryInfo(ActivityManager.MemoryInfo memoryInfo) throws RemoteException {
            }

            public List<ActivityManager.ProcessErrorStateInfo> getProcessesInErrorState() throws RemoteException {
                return null;
            }

            public boolean clearApplicationUserData(String s, boolean b, final android.content.pm.IPackageDataObserver iPackageDataObserver, int i) throws RemoteException {
                IPackageDataObserver packageDataObserver = null;
                if (iPackageDataObserver != null) {
                    packageDataObserver = new IPackageDataObserver() { // from class: com.oplus.wrapper.app.IActivityManager.Stub.1.1
                        @Override // com.oplus.wrapper.content.pm.IPackageDataObserver
                        public void onRemoveCompleted(String packageName, boolean succeeded) throws RemoteException {
                            iPackageDataObserver.onRemoveCompleted(packageName, succeeded);
                        }
                    };
                }
                return Stub.this.clearApplicationUserData(s, b, packageDataObserver, i);
            }

            public void stopAppForUser(String s, int i) throws RemoteException {
            }

            public boolean registerForegroundServiceObserver(IForegroundServiceObserver iForegroundServiceObserver) throws RemoteException {
                return false;
            }

            public void forceStopPackage(String s, int i) throws RemoteException {
            }

            public boolean killPids(int[] ints, String s, boolean b) throws RemoteException {
                return false;
            }

            public List<ActivityManager.RunningServiceInfo> getServices(int i, int i1) throws RemoteException {
                return null;
            }

            public List<ActivityManager.RunningAppProcessInfo> getRunningAppProcesses() throws RemoteException {
                return null;
            }

            public IBinder peekService(Intent intent, String s, String s1) throws RemoteException {
                return null;
            }

            public boolean profileControl(String s, int i, boolean b, ProfilerInfo profilerInfo, int i1) throws RemoteException {
                return false;
            }

            public boolean shutdown(int i) throws RemoteException {
                return false;
            }

            public void stopAppSwitches() throws RemoteException {
            }

            public void resumeAppSwitches() throws RemoteException {
                Stub.this.resumeAppSwitches();
            }

            public boolean bindBackupAgent(String s, int i, int i1, int i2) throws RemoteException {
                return false;
            }

            public void backupAgentCreated(String s, IBinder iBinder, int i) throws RemoteException {
            }

            public void unbindBackupAgent(ApplicationInfo applicationInfo) throws RemoteException {
            }

            public int handleIncomingUser(int i, int i1, int i2, boolean b, boolean b1, String s, String s1) throws RemoteException {
                return 0;
            }

            public void addPackageDependency(String s) throws RemoteException {
            }

            public void killApplication(String s, int i, int i1, String s1, int i2) throws RemoteException {
            }

            public void closeSystemDialogs(String s) throws RemoteException {
            }

            public Debug.MemoryInfo[] getProcessMemoryInfo(int[] ints) throws RemoteException {
                return new Debug.MemoryInfo[0];
            }

            public void killApplicationProcess(String s, int i) throws RemoteException {
            }

            public boolean handleApplicationWtf(IBinder iBinder, String s, boolean b, ApplicationErrorReport.ParcelableCrashInfo parcelableCrashInfo, int i) throws RemoteException {
                return false;
            }

            public void killBackgroundProcesses(String s, int i) throws RemoteException {
            }

            public boolean isUserAMonkey() throws RemoteException {
                return false;
            }

            public List<ApplicationInfo> getRunningExternalApplications() throws RemoteException {
                return null;
            }

            public void finishHeavyWeightApp() throws RemoteException {
            }

            public void handleApplicationStrictModeViolation(IBinder iBinder, int i, StrictMode.ViolationInfo violationInfo) throws RemoteException {
            }

            public void registerStrictModeCallback(IBinder binder) throws RemoteException {
            }

            public boolean isTopActivityImmersive() throws RemoteException {
                return false;
            }

            public void crashApplicationWithType(int i, int i1, String s, int i2, String s1, boolean b, int i3) throws RemoteException {
            }

            public void crashApplicationWithTypeWithExtras(int i, int i1, String s, int i2, String s1, boolean b, int i3, Bundle bundle) throws RemoteException {
            }

            public void getMimeTypeFilterAsync(Uri uri, int userId, RemoteCallback resultCallback) throws RemoteException {
            }

            public boolean dumpHeap(String s, int i, boolean b, boolean b1, boolean b2, String s1, ParcelFileDescriptor parcelFileDescriptor, RemoteCallback remoteCallback) throws RemoteException {
                return false;
            }

            public boolean isUserRunning(int i, int i1) throws RemoteException {
                return false;
            }

            public void setPackageScreenCompatMode(String s, int i) throws RemoteException {
            }

            public boolean switchUser(int i) throws RemoteException {
                return Stub.this.switchUser(i);
            }

            public String getSwitchingFromUserMessage() throws RemoteException {
                return null;
            }

            public String getSwitchingToUserMessage() throws RemoteException {
                return null;
            }

            public void setStopUserOnSwitch(int i) throws RemoteException {
            }

            public boolean removeTask(int taskId) throws RemoteException {
                return Stub.this.removeTask(taskId);
            }

            public void registerProcessObserver(android.app.IProcessObserver iProcessObserver) throws RemoteException {
                if (iProcessObserver == null) {
                    return;
                }
                IProcessObserver oplusProcessObserver = (IProcessObserver) Stub.this.mProcessObserverMap.computeIfAbsent(iProcessObserver, new Function() { // from class: com.oplus.wrapper.app.IActivityManager$Stub$1$$ExternalSyntheticLambda0
                    @Override // java.util.function.Function
                    public final Object apply(Object obj) {
                        IProcessObserver lambda$registerProcessObserver$0;
                        lambda$registerProcessObserver$0 = IActivityManager.Stub.AnonymousClass1.this.lambda$registerProcessObserver$0((android.app.IProcessObserver) obj);
                        return lambda$registerProcessObserver$0;
                    }
                });
                Stub.this.registerProcessObserver(oplusProcessObserver);
            }

            /* JADX INFO: Access modifiers changed from: private */
            public /* synthetic */ IProcessObserver lambda$registerProcessObserver$0(final android.app.IProcessObserver processObserver) {
                return new IProcessObserver.Stub() { // from class: com.oplus.wrapper.app.IActivityManager.Stub.1.2
                    @Override // com.oplus.wrapper.app.IProcessObserver
                    public void onForegroundActivitiesChanged(int pid, int uid, boolean foregroundActivities) throws RemoteException {
                        processObserver.onForegroundActivitiesChanged(pid, uid, foregroundActivities);
                    }

                    @Override // com.oplus.wrapper.app.IProcessObserver
                    public void onForegroundServicesChanged(int pid, int uid, int serviceTypes) throws RemoteException {
                        processObserver.onForegroundServicesChanged(pid, uid, serviceTypes);
                    }

                    @Override // com.oplus.wrapper.app.IProcessObserver
                    public void onProcessDied(int pid, int uid) throws RemoteException {
                        processObserver.onProcessDied(pid, uid);
                    }
                };
            }

            public void unregisterProcessObserver(android.app.IProcessObserver iProcessObserver) throws RemoteException {
                IProcessObserver processObserver;
                if (iProcessObserver == null || (processObserver = (IProcessObserver) Stub.this.mProcessObserverMap.get(iProcessObserver)) == null) {
                    return;
                }
                Stub.this.unregisterProcessObserver(processObserver);
                Stub.this.mProcessObserverMap.remove(iProcessObserver);
            }

            public boolean isIntentSenderTargetedToPackage(IIntentSender iIntentSender) throws RemoteException {
                return false;
            }

            public void updatePersistentConfiguration(Configuration configuration) throws RemoteException {
            }

            public void updatePersistentConfigurationWithAttribution(Configuration configuration, String s, String s1) throws RemoteException {
            }

            public long[] getProcessPss(int[] pids) throws RemoteException {
                return Stub.this.getProcessPss(pids);
            }

            public void showBootMessage(CharSequence charSequence, boolean b) throws RemoteException {
            }

            public void killAllBackgroundProcesses() throws RemoteException {
            }

            public ContentProviderHolder getContentProviderExternal(String s, int i, IBinder iBinder, String s1) throws RemoteException {
                return null;
            }

            public void removeContentProviderExternal(String s, IBinder iBinder) throws RemoteException {
            }

            public void removeContentProviderExternalAsUser(String s, IBinder iBinder, int i) throws RemoteException {
            }

            public void getMyMemoryState(ActivityManager.RunningAppProcessInfo runningAppProcessInfo) throws RemoteException {
            }

            public boolean killProcessesBelowForeground(String s) throws RemoteException {
                return false;
            }

            public android.content.pm.UserInfo getCurrentUser() throws RemoteException {
                return Stub.this.getCurrentUser().getUserInfo();
            }

            public int getCurrentUserId() throws RemoteException {
                return 0;
            }

            public int getLaunchedFromUid(IBinder iBinder) throws RemoteException {
                return 0;
            }

            public void unstableProviderDied(IBinder iBinder) throws RemoteException {
            }

            public boolean isIntentSenderAnActivity(IIntentSender iIntentSender) throws RemoteException {
                return false;
            }

            public int startActivityAsUser(android.app.IApplicationThread iApplicationThread, String s, Intent intent, String s1, IBinder iBinder, String s2, int i, int i1, ProfilerInfo profilerInfo, Bundle bundle, int i2) throws RemoteException {
                return 0;
            }

            public int startActivityAsUserWithFeature(android.app.IApplicationThread iApplicationThread, String s, String s1, Intent intent, String s2, IBinder iBinder, String s3, int i, int i1, ProfilerInfo profilerInfo, Bundle bundle, int i2) throws RemoteException {
                return 0;
            }

            public int stopUser(int i, boolean b, IStopUserCallback iStopUserCallback) throws RemoteException {
                return 0;
            }

            public int stopUserWithDelayedLocking(int i, boolean b, IStopUserCallback iStopUserCallback) throws RemoteException {
                return 0;
            }

            public void registerUserSwitchObserver(IUserSwitchObserver iUserSwitchObserver, String s) throws RemoteException {
            }

            public void unregisterUserSwitchObserver(IUserSwitchObserver iUserSwitchObserver) throws RemoteException {
            }

            public int[] getRunningUserIds() throws RemoteException {
                return new int[0];
            }

            public void requestSystemServerHeapDump() throws RemoteException {
            }

            public void requestBugReport(int i) throws RemoteException {
            }

            public void requestBugReportWithDescription(String s, String s1, int i) throws RemoteException {
            }

            public void requestTelephonyBugReport(String s, String s1) throws RemoteException {
            }

            public void requestWifiBugReport(String s, String s1) throws RemoteException {
            }

            public void requestInteractiveBugReportWithDescription(String s, String s1) throws RemoteException {
            }

            public void requestInteractiveBugReport() throws RemoteException {
            }

            public void requestFullBugReport() throws RemoteException {
            }

            public void requestRemoteBugReport(long nonce) throws RemoteException {
            }

            public boolean launchBugReportHandlerApp() throws RemoteException {
                return false;
            }

            public List<String> getBugreportWhitelistedPackages() throws RemoteException {
                return null;
            }

            public Intent getIntentForIntentSender(IIntentSender iIntentSender) throws RemoteException {
                return null;
            }

            public String getLaunchedFromPackage(IBinder activityToken) throws RemoteException {
                return Stub.this.getLaunchedFromPackage(activityToken);
            }

            public void killUid(int i, int i1, String s) throws RemoteException {
            }

            public void setUserIsMonkey(boolean b) throws RemoteException {
            }

            public void hang(IBinder iBinder, boolean b) throws RemoteException {
            }

            public List<ActivityTaskManager.RootTaskInfo> getAllRootTaskInfos() throws RemoteException {
                return null;
            }

            public void moveTaskToRootTask(int i, int i1, boolean b) throws RemoteException {
            }

            public void setFocusedRootTask(int i) throws RemoteException {
            }

            public ActivityTaskManager.RootTaskInfo getFocusedRootTaskInfo() throws RemoteException {
                return null;
            }

            public void restart() throws RemoteException {
            }

            public void performIdleMaintenance() throws RemoteException {
            }

            public void appNotRespondingViaProvider(IBinder iBinder) throws RemoteException {
            }

            public Rect getTaskBounds(int i) throws RemoteException {
                return null;
            }

            public boolean setProcessMemoryTrimLevel(String s, int i, int i1) throws RemoteException {
                return false;
            }

            public String getTagForIntentSender(IIntentSender iIntentSender, String s) throws RemoteException {
                return null;
            }

            public boolean startUserInBackground(int i) throws RemoteException {
                return false;
            }

            public boolean isInLockTaskMode() throws RemoteException {
                return Stub.this.isInLockTaskMode();
            }

            public int startActivityFromRecents(int i, Bundle bundle) throws RemoteException {
                return 0;
            }

            public void startSystemLockTaskMode(int i) throws RemoteException {
            }

            public boolean isTopOfTask(IBinder iBinder) throws RemoteException {
                return false;
            }

            public void bootAnimationComplete() throws RemoteException {
            }

            public void registerTaskStackListener(android.app.ITaskStackListener iTaskStackListener) throws RemoteException {
            }

            public void unregisterTaskStackListener(android.app.ITaskStackListener iTaskStackListener) throws RemoteException {
            }

            public void notifyCleartextNetwork(int i, byte[] bytes) throws RemoteException {
            }

            public void setTaskResizeable(int i, int i1) throws RemoteException {
            }

            public void resizeTask(int i, Rect rect, int i1) throws RemoteException {
            }

            public int getLockTaskModeState() throws RemoteException {
                return 0;
            }

            public void setDumpHeapDebugLimit(String s, int i, long l, String s1) throws RemoteException {
            }

            public void dumpHeapFinished(String s) throws RemoteException {
            }

            public void updateLockTaskPackages(int i, String[] strings) throws RemoteException {
            }

            public void noteAlarmStart(IIntentSender iIntentSender, WorkSource workSource, int i, String s) throws RemoteException {
            }

            public void noteAlarmFinish(IIntentSender iIntentSender, WorkSource workSource, int i, String s) throws RemoteException {
            }

            public int getPackageProcessState(String s, String s1) throws RemoteException {
                return 0;
            }

            public boolean startBinderTracking() throws RemoteException {
                return false;
            }

            public boolean stopBinderTrackingAndDump(ParcelFileDescriptor parcelFileDescriptor) throws RemoteException {
                return false;
            }

            public void suppressResizeConfigChanges(boolean b) throws RemoteException {
            }

            public boolean unlockUser(int i, byte[] bytes, byte[] bytes1, IProgressListener iProgressListener) throws RemoteException {
                return false;
            }

            public boolean unlockUser2(int userId, IProgressListener listener) throws RemoteException {
                return false;
            }

            public void killPackageDependents(String s, int i) throws RemoteException {
            }

            public void makePackageIdle(String s, int i) throws RemoteException {
            }

            public int getMemoryTrimLevel() throws RemoteException {
                return 0;
            }

            public boolean isVrModePackageEnabled(ComponentName componentName) throws RemoteException {
                return false;
            }

            public void notifyLockedProfile(int i) throws RemoteException {
            }

            public void startConfirmDeviceCredentialIntent(Intent intent, Bundle bundle) throws RemoteException {
            }

            public void sendIdleJobTrigger() throws RemoteException {
            }

            public int sendIntentSender(android.app.IApplicationThread caller, IIntentSender target, IBinder whitelistToken, int code, Intent intent, String resolvedType, IIntentReceiver finishedReceiver, String requiredPermission, Bundle options) throws RemoteException {
                return 0;
            }

            public boolean isBackgroundRestricted(String s) throws RemoteException {
                return false;
            }

            public void setRenderThread(int i) throws RemoteException {
            }

            public void setHasTopUi(boolean b) throws RemoteException {
            }

            public int restartUserInBackground(int i, int j) throws RemoteException {
                return 0;
            }

            public void cancelTaskWindowTransition(int i) throws RemoteException {
            }

            public void scheduleApplicationInfoChanged(List<String> list, int i) throws RemoteException {
            }

            public void setPersistentVrThread(int i) throws RemoteException {
            }

            public void waitForNetworkStateUpdate(long l) throws RemoteException {
            }

            public void backgroundAllowlistUid(int i) throws RemoteException {
            }

            public boolean startUserInBackgroundWithListener(int i, IProgressListener iProgressListener) throws RemoteException {
                return false;
            }

            public void startDelegateShellPermissionIdentity(int i, String[] strings) throws RemoteException {
            }

            public void stopDelegateShellPermissionIdentity() throws RemoteException {
            }

            public List<String> getDelegatedShellPermissions() throws RemoteException {
                return null;
            }

            public ParcelFileDescriptor getLifeMonitor() throws RemoteException {
                return null;
            }

            public boolean startUserInForegroundWithListener(int i, IProgressListener iProgressListener) throws RemoteException {
                return false;
            }

            public void appNotResponding(String s) throws RemoteException {
            }

            public ParceledListSlice<ApplicationStartInfo> getHistoricalProcessStartReasons(String packageName, int maxNum, int userId) throws RemoteException {
                return null;
            }

            public void setApplicationStartInfoCompleteListener(IApplicationStartInfoCompleteListener listener, int userId) throws RemoteException {
            }

            public void removeApplicationStartInfoCompleteListener(int userId) throws RemoteException {
            }

            public ParceledListSlice<ApplicationExitInfo> getHistoricalProcessExitReasons(String s, int i, int i1, int i2) throws RemoteException {
                return null;
            }

            public void killProcessesWhenImperceptible(int[] ints, String s) throws RemoteException {
            }

            public void setActivityLocusContext(ComponentName componentName, LocusId locusId, IBinder iBinder) throws RemoteException {
            }

            public void setProcessStateSummary(byte[] bytes) throws RemoteException {
            }

            public boolean isAppFreezerSupported() throws RemoteException {
                return false;
            }

            public boolean isAppFreezerEnabled() throws RemoteException {
                return false;
            }

            public void killUidForPermissionChange(int i, int i1, String s) throws RemoteException {
            }

            public void resetAppErrors() throws RemoteException {
            }

            public boolean enableAppFreezer(boolean b) throws RemoteException {
                return false;
            }

            public boolean enableFgsNotificationRateLimit(boolean b) throws RemoteException {
                return false;
            }

            public void holdLock(IBinder iBinder, int i) throws RemoteException {
            }

            public boolean startProfile(int i) throws RemoteException {
                return false;
            }

            public boolean stopProfile(int i) throws RemoteException {
                return false;
            }

            public ParceledListSlice queryIntentComponentsForIntentSender(IIntentSender iIntentSender, int i) throws RemoteException {
                return null;
            }

            public int getUidProcessCapabilities(int i, String s) throws RemoteException {
                return 0;
            }

            public void waitForBroadcastIdle() throws RemoteException {
            }

            public void waitForBroadcastBarrier() throws RemoteException {
            }

            public void forceDelayBroadcastDelivery(String targetPackage, long delayedDurationMs) throws RemoteException {
            }

            public boolean isModernBroadcastQueueEnabled() throws RemoteException {
                return false;
            }

            public boolean isProcessFrozen(int pid) throws RemoteException {
                return false;
            }

            public int getBackgroundRestrictionExemptionReason(int i) throws RemoteException {
                return 0;
            }

            public int[] getDisplayIdsForStartingVisibleBackgroundUsers() throws RemoteException {
                return null;
            }

            public boolean shouldServiceTimeOut(ComponentName className, IBinder token) throws RemoteException {
                return false;
            }

            public boolean startUserInBackgroundVisibleOnDisplay(int userid, int displayId, IProgressListener unlockProgressListener) throws RemoteException {
                return false;
            }

            public boolean startProfileWithListener(int userid, IProgressListener unlockProgressListener) throws RemoteException {
                return false;
            }

            public void logFgsApiBegin(int apiType, int appUid, int appPid) throws RemoteException {
            }

            public void logFgsApiEnd(int apiType, int appUid, int appPid) throws RemoteException {
            }

            public void logFgsApiStateChanged(int apiType, int state, int appUid, int appPid) throws RemoteException {
            }

            public void registerUidFrozenStateChangedCallback(IUidFrozenStateChangedCallback callback) throws RemoteException {
            }

            public void unregisterUidFrozenStateChangedCallback(IUidFrozenStateChangedCallback callback) throws RemoteException {
            }

            public int[] getUidFrozenState(int[] uids) throws RemoteException {
                return null;
            }
        }

        public static IActivityManager asInterface(IBinder obj) {
            return new Proxy(IActivityManager.Stub.asInterface(obj));
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return this.mTarget.asBinder();
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* loaded from: classes.dex */
        public static class Proxy implements IActivityManager {
            private Map<IProcessObserver, android.app.IProcessObserver> mProcessObserverMap = new ConcurrentHashMap();
            private final android.app.IActivityManager mTarget;

            Proxy(android.app.IActivityManager target) {
                this.mTarget = target;
            }

            @Override // com.oplus.wrapper.app.IActivityManager
            public Configuration getConfiguration() throws RemoteException {
                return this.mTarget.getConfiguration();
            }

            @Override // com.oplus.wrapper.app.IActivityManager
            public boolean removeTask(int taskId) throws RemoteException {
                return this.mTarget.removeTask(taskId);
            }

            @Override // com.oplus.wrapper.app.IActivityManager
            public void setProcessLimit(int max) throws RemoteException {
                this.mTarget.setProcessLimit(max);
            }

            @Override // com.oplus.wrapper.app.IActivityManager
            public long[] getProcessPss(int[] pids) throws RemoteException {
                return this.mTarget.getProcessPss(pids);
            }

            @Override // com.oplus.wrapper.app.IActivityManager
            public void resumeAppSwitches() throws RemoteException {
                this.mTarget.resumeAppSwitches();
            }

            @Override // com.oplus.wrapper.app.IActivityManager
            public boolean isInLockTaskMode() throws RemoteException {
                return this.mTarget.isInLockTaskMode();
            }

            @Override // com.oplus.wrapper.app.IActivityManager
            public boolean clearApplicationUserData(String packageName, boolean keepState, final IPackageDataObserver observer, int userId) throws RemoteException {
                android.content.pm.IPackageDataObserver dataObserver = null;
                if (observer != null) {
                    dataObserver = new IPackageDataObserver.Stub() { // from class: com.oplus.wrapper.app.IActivityManager.Stub.Proxy.1
                        public void onRemoveCompleted(String packageName2, boolean succeeded) throws RemoteException {
                            observer.onRemoveCompleted(packageName2, succeeded);
                        }
                    };
                }
                return this.mTarget.clearApplicationUserData(packageName, keepState, dataObserver, userId);
            }

            @Override // com.oplus.wrapper.app.IActivityManager
            public boolean updateConfiguration(Configuration values) throws RemoteException {
                return this.mTarget.updateConfiguration(values);
            }

            /* JADX INFO: Access modifiers changed from: private */
            public /* synthetic */ android.app.IProcessObserver lambda$registerProcessObserver$0(final IProcessObserver observer) {
                return new IProcessObserver.Stub() { // from class: com.oplus.wrapper.app.IActivityManager.Stub.Proxy.2
                    public void onForegroundActivitiesChanged(int i, int i1, boolean b) throws RemoteException {
                        observer.onForegroundActivitiesChanged(i, i1, b);
                    }

                    public void onForegroundServicesChanged(int i, int i1, int i2) throws RemoteException {
                        observer.onForegroundServicesChanged(i, i1, i2);
                    }

                    public void onProcessDied(int i, int i1) throws RemoteException {
                        observer.onProcessDied(i, i1);
                    }
                };
            }

            @Override // com.oplus.wrapper.app.IActivityManager
            public void registerProcessObserver(IProcessObserver oplusObserver) throws RemoteException {
                android.app.IProcessObserver processObserver = this.mProcessObserverMap.computeIfAbsent(oplusObserver, new Function() { // from class: com.oplus.wrapper.app.IActivityManager$Stub$Proxy$$ExternalSyntheticLambda0
                    @Override // java.util.function.Function
                    public final Object apply(Object obj) {
                        android.app.IProcessObserver lambda$registerProcessObserver$0;
                        lambda$registerProcessObserver$0 = IActivityManager.Stub.Proxy.this.lambda$registerProcessObserver$0((IProcessObserver) obj);
                        return lambda$registerProcessObserver$0;
                    }
                });
                this.mTarget.registerProcessObserver(processObserver);
            }

            @Override // com.oplus.wrapper.app.IActivityManager
            public void unregisterProcessObserver(IProcessObserver observer) throws RemoteException {
                android.app.IProcessObserver processObserver = this.mProcessObserverMap.get(observer);
                if (processObserver == null) {
                    return;
                }
                this.mTarget.unregisterProcessObserver(processObserver);
                this.mProcessObserverMap.remove(observer);
            }

            @Override // com.oplus.wrapper.app.IActivityManager
            public boolean switchUser(int targetUserId) throws RemoteException {
                return this.mTarget.switchUser(targetUserId);
            }

            @Override // com.oplus.wrapper.app.IActivityManager
            public String getLaunchedFromPackage(IBinder activityToken) throws RemoteException {
                return this.mTarget.getLaunchedFromPackage(activityToken);
            }

            @Override // com.oplus.wrapper.app.IActivityManager
            public void closeSystemDialogs(String reason) throws RemoteException {
                this.mTarget.closeSystemDialogs(reason);
            }

            @Override // com.oplus.wrapper.app.IActivityManager
            public UserInfo getCurrentUser() throws RemoteException {
                return new UserInfo(this.mTarget.getCurrentUser());
            }
        }
    }
}
