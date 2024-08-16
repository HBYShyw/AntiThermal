package android.app;

import android.app.ActivityManager;
import android.app.IApplicationThread;
import android.app.usage.UsageStats;
import android.content.Intent;
import android.net.Uri;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import android.os.SharedMemory;
import android.view.MotionEvent;
import com.oplus.app.IOplusAppStartController;
import com.oplus.app.IOplusGameSpaceController;
import com.oplus.app.IOplusHansListener;
import com.oplus.app.IOplusPermissionRecordController;
import com.oplus.app.IOplusProtectConnection;
import com.oplus.app.IProcessTerminateObserver;
import com.oplus.app.ITerminateObserver;
import com.oplus.compatmode.IOplusCompatModeSession;
import com.oplus.darkmode.OplusDarkModeData;
import com.oplus.eap.IOplusEapDataCallback;
import com.oplus.eventhub.sdk.aidl.IEventCallback;
import com.oplus.favorite.IOplusFavoriteQueryCallback;
import com.oplus.multiapp.OplusMultiAppConfig;
import com.oplus.osense.complexscene.IComplexSceneObserver;
import com.oplus.util.OplusAccidentallyTouchData;
import com.oplus.util.OplusDisplayCompatData;
import com.oplus.util.OplusDisplayOptimizationData;
import com.oplus.util.OplusPackageFreezeData;
import com.oplus.util.OplusProcDependData;
import com.oplus.util.OplusReflectData;
import com.oplus.util.OplusResolveData;
import com.oplus.util.OplusSecureKeyboardData;
import com.oplus.util.OplusUXIconData;
import com.oplus.verifycode.IOplusVerifyCodeListener;
import java.util.List;

/* loaded from: classes.dex */
public interface IOplusActivityManager extends IInterface {
    public static final String DESCRIPTOR = "android.app.IOplusActivityManager";

    void activeGc(int[] iArr) throws RemoteException;

    void addBackgroundRestrictedInfo(String str, List<String> list) throws RemoteException;

    void addFastAppThirdLogin(String str, String str2) throws RemoteException;

    void addFastAppWCPay(String str, String str2) throws RemoteException;

    void addMiniProgramShare(String str, String str2, String str3) throws RemoteException;

    void addOplusLoopLoadTime(long j, String str) throws RemoteException;

    boolean addOrRemoveOplusVerifyCodeListener(boolean z, IOplusVerifyCodeListener iOplusVerifyCodeListener) throws RemoteException;

    void addPreventIndulgeList(List<String> list) throws RemoteException;

    void addStageProtectInfo(String str, String str2, List<String> list, String str3, long j, IOplusProtectConnection iOplusProtectConnection) throws RemoteException;

    void anrViaTheiaEvent(int i, String str) throws RemoteException;

    void asyncReportDalvikMem(Bundle bundle, int i, long j, long j2, long j3) throws RemoteException;

    void asyncReportFrames(String str, int i) throws RemoteException;

    Bundle autoLayoutCall(Bundle bundle) throws RemoteException;

    void cleanPackageResources(String str, int i) throws RemoteException;

    void compactProcess(Bundle bundle, int i, int i2) throws RemoteException;

    boolean dumpProcPerfData(Bundle bundle) throws RemoteException;

    void enterFastFreezer(String str, int[] iArr, long j, String str2) throws RemoteException;

    void executeResPreload(String str, int i, int i2, String str2) throws RemoteException;

    void exitFastFreezer(String str, String str2) throws RemoteException;

    void favoriteQueryRule(String str, IOplusFavoriteQueryCallback iOplusFavoriteQueryCallback) throws RemoteException;

    void finishNotOrderReceiver(IBinder iBinder, int i, int i2, String str, Bundle bundle, boolean z) throws RemoteException;

    void forceStopPackageAndSaveActivity(String str, int i) throws RemoteException;

    void forceTrimAppMemory(int i) throws RemoteException;

    OplusAccidentallyTouchData getAccidentallyTouchData() throws RemoteException;

    List<ActivityManager.RecentTaskInfo> getAllVisibleTasksInfo(int i) throws RemoteException;

    IOplusCompatModeSession getCompatModeSession() throws RemoteException;

    Bundle getConfigInfo(String str, int i, int i2) throws RemoteException;

    SharedMemory getCpuLimitLatestLogs(String str) throws RemoteException;

    List<String> getCpuWorkingStats() throws RemoteException;

    OplusDarkModeData getDarkModeData(String str) throws RemoteException;

    OplusDisplayCompatData getDisplayCompatData() throws RemoteException;

    OplusDisplayOptimizationData getDisplayOptimizationData() throws RemoteException;

    List<OplusPackageFreezeData> getDownloadingList(int i, boolean z) throws RemoteException;

    String getFastAppReplacePkg(String str) throws RemoteException;

    int getFontVariationAdaptionData(String str) throws RemoteException;

    List<String> getGlobalPkgWhiteList(int i) throws RemoteException;

    List<String> getGlobalProcessWhiteList() throws RemoteException;

    boolean getIsSupportMultiApp() throws RemoteException;

    int getLoopCpuLoad() throws RemoteException;

    int getMultiAppAccessMode(String str) throws RemoteException;

    String getMultiAppAlias(String str) throws RemoteException;

    void getMultiAppAllAccessMode(Bundle bundle) throws RemoteException;

    OplusMultiAppConfig getMultiAppConfig() throws RemoteException;

    List<String> getMultiAppList(int i) throws RemoteException;

    int getMultiAppMaxCreateNum() throws RemoteException;

    List<OplusPackageFreezeData> getPackageFreezeDataInfos(Bundle bundle) throws RemoteException;

    int getPayJoyFlag() throws RemoteException;

    List<String> getPkgPreloadFiles(String str) throws RemoteException;

    long getPreloadIOSize() throws RemoteException;

    Bundle getPreloadPkgList() throws RemoteException;

    boolean getPreloadStatus(String str, int i) throws RemoteException;

    List<String> getProcCmdline(int[] iArr) throws RemoteException;

    List<String> getProcCommonInfoList(int i) throws RemoteException;

    List<OplusProcDependData> getProcDependency(int i) throws RemoteException;

    List<OplusProcDependData> getProcDependencyByUserId(String str, int i) throws RemoteException;

    OplusReflectData getReflectData() throws RemoteException;

    Bundle getResPreloadInfo(int i, int i2) throws RemoteException;

    OplusResolveData getResolveData() throws RemoteException;

    List<ActivityManager.RunningAppProcessInfo> getRunningAppProcessInfos(Bundle bundle) throws RemoteException;

    int[] getRunningPidsByUid(int i) throws RemoteException;

    List<OplusPackageFreezeData> getRunningProcesses() throws RemoteException;

    OplusSecureKeyboardData getSecureKeyboardData() throws RemoteException;

    List<String> getStageProtectList(int i) throws RemoteException;

    List<String> getStageProtectListAsUser(int i, int i2) throws RemoteException;

    List<String> getStageProtectListFromPkg(String str, int i) throws RemoteException;

    List<String> getStageProtectListFromPkgAsUser(String str, int i, int i2) throws RemoteException;

    List<String> getTaskPkgList(int i) throws RemoteException;

    int[] getTerminateObservers() throws RemoteException;

    List<String> getTopLoadPidsInfos(int i) throws RemoteException;

    long getTotalCpuLoadPercent() throws RemoteException;

    String getTrafficBytesList(Bundle bundle, Bundle bundle2) throws RemoteException;

    String getTrafficPacketList(Bundle bundle, Bundle bundle2) throws RemoteException;

    OplusUXIconData getUXIconData() throws RemoteException;

    List<String> getUidCpuWorkingStats() throws RemoteException;

    void grantOplusPermissionByGroup(String str, String str2) throws RemoteException;

    void grantUriPermissionToUser(IApplicationThread iApplicationThread, String str, Uri uri, int i, int i2) throws RemoteException;

    void handleAppForNotification(String str, int i, int i2) throws RemoteException;

    void handleAppFromControlCenter(String str, int i) throws RemoteException;

    boolean inDownloading(int i, int i2, boolean z) throws RemoteException;

    boolean isFrozenByHans(String str, int i) throws RemoteException;

    boolean isMultiApp(int i, String str) throws RemoteException;

    boolean isNightMode() throws RemoteException;

    boolean isPermissionInterceptEnabled() throws RemoteException;

    void killPidForce(int i) throws RemoteException;

    void notifyAppKillReason(int i, int i2, int i3, int i4, String str) throws RemoteException;

    void notifyAthenaOnekeyClearRunning(int i) throws RemoteException;

    void notifyProcessTerminate(int[] iArr, String str) throws RemoteException;

    void notifyProcessTerminateFinish(IProcessTerminateObserver iProcessTerminateObserver) throws RemoteException;

    void notifyUiSwitched(String str, int i) throws RemoteException;

    void onBackPressedOnTheiaMonitor(long j) throws RemoteException;

    boolean putConfigInfo(String str, Bundle bundle, int i, int i2) throws RemoteException;

    String queryProcessNameFromPid(int i) throws RemoteException;

    List<UsageStats> queryUsageStats(int i, long j, long j2) throws RemoteException;

    void registerAbConfigCallback(IEventCallback iEventCallback, String str) throws RemoteException;

    boolean registerComplexSceneObserver(Bundle bundle, IComplexSceneObserver iComplexSceneObserver) throws RemoteException;

    void registerEapDataCallback(IOplusEapDataCallback iOplusEapDataCallback) throws RemoteException;

    void registerErrorInfoCallback(IEventCallback iEventCallback) throws RemoteException;

    boolean registerHansListener(String str, IOplusHansListener iOplusHansListener) throws RemoteException;

    boolean registerTerminateObserver(IProcessTerminateObserver iProcessTerminateObserver) throws RemoteException;

    boolean registerTerminateStateObserver(ITerminateObserver iTerminateObserver) throws RemoteException;

    void removeFastAppThirdLogin(String str, String str2) throws RemoteException;

    void removeFastAppWCPay(String str, String str2) throws RemoteException;

    void removeMiniProgramShare(String str, String str2, String str3) throws RemoteException;

    void removeStageProtectInfo(String str, String str2) throws RemoteException;

    void reportBindApplicationFinished(String str, int i, int i2) throws RemoteException;

    void reportSkippedFrames(long j, boolean z, boolean z2, long j2) throws RemoteException;

    void reportSkippedFramesActivityName(long j, boolean z, boolean z2, long j2, String str, String str2) throws RemoteException;

    void reportSkippedFramesProcName(long j, boolean z, boolean z2, long j2, String str) throws RemoteException;

    boolean requestDeviceFolded(int i, boolean z) throws RemoteException;

    void revokeOplusPermissionByGroup(String str, String str2) throws RemoteException;

    void scanFileIfNeed(int i, String str) throws RemoteException;

    void sendFlingTransit(MotionEvent motionEvent, int i) throws RemoteException;

    void sendTheiaEvent(long j, Intent intent) throws RemoteException;

    boolean setAppFreeze(String str, Bundle bundle) throws RemoteException;

    void setAppStartMonitorController(IOplusAppStartController iOplusAppStartController) throws RemoteException;

    void setGameSpaceController(IOplusGameSpaceController iOplusGameSpaceController) throws RemoteException;

    void setGlThreads(int i, int i2) throws RemoteException;

    void setHwuiTaskThreads(int i, int i2) throws RemoteException;

    int setMultiAppAccessMode(String str, int i) throws RemoteException;

    int setMultiAppAlias(String str, String str2) throws RemoteException;

    int setMultiAppConfig(OplusMultiAppConfig oplusMultiAppConfig) throws RemoteException;

    int setMultiAppStatus(String str, int i) throws RemoteException;

    boolean setPayJoyFlag(int i) throws RemoteException;

    void setPermissionInterceptEnable(boolean z) throws RemoteException;

    void setPermissionRecordController(IOplusPermissionRecordController iOplusPermissionRecordController) throws RemoteException;

    void setPreventIndulgeController(IOplusAppStartController iOplusAppStartController) throws RemoteException;

    void setSceneActionTransit(String str, String str2, int i) throws RemoteException;

    void syncPermissionRecord() throws RemoteException;

    void trimSystemMemory(int i, boolean z) throws RemoteException;

    void unfreezeForKernel(int i, int i2, int i3, String str, int i4) throws RemoteException;

    void unfreezeForKernelTargetPid(int i, int i2, int i3, int i4, int i5, String str, int i6) throws RemoteException;

    void unregisterAbConfigCallback(IEventCallback iEventCallback) throws RemoteException;

    boolean unregisterComplexSceneObserver(Bundle bundle, IComplexSceneObserver iComplexSceneObserver) throws RemoteException;

    void unregisterEapDataCallback(IOplusEapDataCallback iOplusEapDataCallback) throws RemoteException;

    void unregisterErrorInfoCallback(IEventCallback iEventCallback) throws RemoteException;

    boolean unregisterHansListener(String str, IOplusHansListener iOplusHansListener) throws RemoteException;

    boolean unregisterTerminateObserver(IProcessTerminateObserver iProcessTerminateObserver) throws RemoteException;

    boolean unregisterTerminateStateObserver(ITerminateObserver iTerminateObserver) throws RemoteException;

    void updateANRDumpState(SharedMemory sharedMemory) throws RemoteException;

    float updateCpuTracker(long j) throws RemoteException;

    void updatePermissionChoice(String str, String str2, int i) throws RemoteException;

    void updateUidCpuTracker() throws RemoteException;

    /* loaded from: classes.dex */
    public static class Default implements IOplusActivityManager {
        @Override // android.app.IOplusActivityManager
        public void unfreezeForKernel(int type, int callerPid, int targetUid, String rpcName, int code) throws RemoteException {
        }

        @Override // android.app.IOplusActivityManager
        public void unfreezeForKernelTargetPid(int type, int callerPid, int callerUid, int targetPid, int targetUid, String rpcName, int code) throws RemoteException {
        }

        @Override // android.app.IOplusActivityManager
        public void setHwuiTaskThreads(int pid, int tid) throws RemoteException {
        }

        @Override // android.app.IOplusActivityManager
        public void setGlThreads(int glID, int tid) throws RemoteException {
        }

        @Override // android.app.IOplusActivityManager
        public void onBackPressedOnTheiaMonitor(long pressNow) throws RemoteException {
        }

        @Override // android.app.IOplusActivityManager
        public void sendTheiaEvent(long category, Intent args) throws RemoteException {
        }

        @Override // android.app.IOplusActivityManager
        public void reportBindApplicationFinished(String pkgName, int userId, int pid) throws RemoteException {
        }

        @Override // android.app.IOplusActivityManager
        public void favoriteQueryRule(String packageName, IOplusFavoriteQueryCallback callback) throws RemoteException {
        }

        @Override // android.app.IOplusActivityManager
        public OplusAccidentallyTouchData getAccidentallyTouchData() throws RemoteException {
            return null;
        }

        @Override // android.app.IOplusActivityManager
        public OplusSecureKeyboardData getSecureKeyboardData() throws RemoteException {
            return null;
        }

        @Override // android.app.IOplusActivityManager
        public void updatePermissionChoice(String packageName, String permission, int choice) throws RemoteException {
        }

        @Override // android.app.IOplusActivityManager
        public void setPermissionInterceptEnable(boolean enabled) throws RemoteException {
        }

        @Override // android.app.IOplusActivityManager
        public boolean isPermissionInterceptEnabled() throws RemoteException {
            return false;
        }

        @Override // android.app.IOplusActivityManager
        public void grantOplusPermissionByGroup(String packageName, String permission) throws RemoteException {
        }

        @Override // android.app.IOplusActivityManager
        public void revokeOplusPermissionByGroup(String packageName, String permission) throws RemoteException {
        }

        @Override // android.app.IOplusActivityManager
        public void killPidForce(int pid) throws RemoteException {
        }

        @Override // android.app.IOplusActivityManager
        public void handleAppForNotification(String pkgName, int uid, int otherInfo) throws RemoteException {
        }

        @Override // android.app.IOplusActivityManager
        public void setGameSpaceController(IOplusGameSpaceController watcher) throws RemoteException {
        }

        @Override // android.app.IOplusActivityManager
        public List<String> getGlobalPkgWhiteList(int type) throws RemoteException {
            return null;
        }

        @Override // android.app.IOplusActivityManager
        public List<String> getGlobalProcessWhiteList() throws RemoteException {
            return null;
        }

        @Override // android.app.IOplusActivityManager
        public void addStageProtectInfo(String callerPkg, String protectPkg, List<String> processList, String reason, long timeout, IOplusProtectConnection connection) throws RemoteException {
        }

        @Override // android.app.IOplusActivityManager
        public void removeStageProtectInfo(String protectPkg, String callerPkg) throws RemoteException {
        }

        @Override // android.app.IOplusActivityManager
        public OplusDisplayOptimizationData getDisplayOptimizationData() throws RemoteException {
            return null;
        }

        @Override // android.app.IOplusActivityManager
        public List<String> getStageProtectListFromPkg(String pkg, int type) throws RemoteException {
            return null;
        }

        @Override // android.app.IOplusActivityManager
        public List<String> getStageProtectListFromPkgAsUser(String pkg, int type, int userId) throws RemoteException {
            return null;
        }

        @Override // android.app.IOplusActivityManager
        public void handleAppFromControlCenter(String pkgName, int uid) throws RemoteException {
        }

        @Override // android.app.IOplusActivityManager
        public OplusDisplayCompatData getDisplayCompatData() throws RemoteException {
            return null;
        }

        @Override // android.app.IOplusActivityManager
        public boolean getIsSupportMultiApp() throws RemoteException {
            return false;
        }

        @Override // android.app.IOplusActivityManager
        public List<String> getMultiAppList(int type) throws RemoteException {
            return null;
        }

        @Override // android.app.IOplusActivityManager
        public String getMultiAppAlias(String pkgName) throws RemoteException {
            return null;
        }

        @Override // android.app.IOplusActivityManager
        public int setMultiAppConfig(OplusMultiAppConfig config) throws RemoteException {
            return 0;
        }

        @Override // android.app.IOplusActivityManager
        public OplusMultiAppConfig getMultiAppConfig() throws RemoteException {
            return null;
        }

        @Override // android.app.IOplusActivityManager
        public int setMultiAppAlias(String pkgName, String alias) throws RemoteException {
            return 0;
        }

        @Override // android.app.IOplusActivityManager
        public int getMultiAppAccessMode(String pkgName) throws RemoteException {
            return 0;
        }

        @Override // android.app.IOplusActivityManager
        public int setMultiAppAccessMode(String pkgName, int accessMode) throws RemoteException {
            return 0;
        }

        @Override // android.app.IOplusActivityManager
        public void getMultiAppAllAccessMode(Bundle outBundle) throws RemoteException {
        }

        @Override // android.app.IOplusActivityManager
        public boolean isMultiApp(int userId, String pkgName) throws RemoteException {
            return false;
        }

        @Override // android.app.IOplusActivityManager
        public int setMultiAppStatus(String pkgName, int status) throws RemoteException {
            return 0;
        }

        @Override // android.app.IOplusActivityManager
        public int getMultiAppMaxCreateNum() throws RemoteException {
            return 0;
        }

        @Override // android.app.IOplusActivityManager
        public void scanFileIfNeed(int userId, String path) throws RemoteException {
        }

        @Override // android.app.IOplusActivityManager
        public void addMiniProgramShare(String shareAppPkgName, String miniProgramPkgName, String miniProgramSignature) throws RemoteException {
        }

        @Override // android.app.IOplusActivityManager
        public void removeMiniProgramShare(String shareAppPkgName, String miniProgramPkgName, String miniProgramSignature) throws RemoteException {
        }

        @Override // android.app.IOplusActivityManager
        public OplusResolveData getResolveData() throws RemoteException {
            return null;
        }

        @Override // android.app.IOplusActivityManager
        public OplusReflectData getReflectData() throws RemoteException {
            return null;
        }

        @Override // android.app.IOplusActivityManager
        public void addFastAppWCPay(String originAppCpn, String fastAppCpn) throws RemoteException {
        }

        @Override // android.app.IOplusActivityManager
        public void removeFastAppWCPay(String originAppCpn, String fastAppCpn) throws RemoteException {
        }

        @Override // android.app.IOplusActivityManager
        public List<OplusPackageFreezeData> getRunningProcesses() throws RemoteException {
            return null;
        }

        @Override // android.app.IOplusActivityManager
        public void setAppStartMonitorController(IOplusAppStartController watcher) throws RemoteException {
        }

        @Override // android.app.IOplusActivityManager
        public void addFastAppThirdLogin(String callerPkg, String replacePkg) throws RemoteException {
        }

        @Override // android.app.IOplusActivityManager
        public void removeFastAppThirdLogin(String callerPkg, String replacePkg) throws RemoteException {
        }

        @Override // android.app.IOplusActivityManager
        public void addBackgroundRestrictedInfo(String callerPkg, List<String> targetPkgList) throws RemoteException {
        }

        @Override // android.app.IOplusActivityManager
        public void setPreventIndulgeController(IOplusAppStartController controller) throws RemoteException {
        }

        @Override // android.app.IOplusActivityManager
        public void addPreventIndulgeList(List<String> pkgNames) throws RemoteException {
        }

        @Override // android.app.IOplusActivityManager
        public boolean putConfigInfo(String configName, Bundle bundle, int flag, int userId) throws RemoteException {
            return false;
        }

        @Override // android.app.IOplusActivityManager
        public Bundle getConfigInfo(String configName, int flag, int userId) throws RemoteException {
            return null;
        }

        @Override // android.app.IOplusActivityManager
        public float updateCpuTracker(long lastUpdateTime) throws RemoteException {
            return 0.0f;
        }

        @Override // android.app.IOplusActivityManager
        public List<String> getCpuWorkingStats() throws RemoteException {
            return null;
        }

        @Override // android.app.IOplusActivityManager
        public void forceTrimAppMemory(int level) throws RemoteException {
        }

        @Override // android.app.IOplusActivityManager
        public void setPermissionRecordController(IOplusPermissionRecordController watcher) throws RemoteException {
        }

        @Override // android.app.IOplusActivityManager
        public OplusDarkModeData getDarkModeData(String packageName) throws RemoteException {
            return null;
        }

        @Override // android.app.IOplusActivityManager
        public boolean isNightMode() throws RemoteException {
            return false;
        }

        @Override // android.app.IOplusActivityManager
        public boolean dumpProcPerfData(Bundle bundle) throws RemoteException {
            return false;
        }

        @Override // android.app.IOplusActivityManager
        public List<String> getProcCommonInfoList(int type) throws RemoteException {
            return null;
        }

        @Override // android.app.IOplusActivityManager
        public List<OplusProcDependData> getProcDependency(int pid) throws RemoteException {
            return null;
        }

        @Override // android.app.IOplusActivityManager
        public List<OplusProcDependData> getProcDependencyByUserId(String packageName, int userId) throws RemoteException {
            return null;
        }

        @Override // android.app.IOplusActivityManager
        public List<String> getTaskPkgList(int taskId) throws RemoteException {
            return null;
        }

        @Override // android.app.IOplusActivityManager
        public void syncPermissionRecord() throws RemoteException {
        }

        @Override // android.app.IOplusActivityManager
        public void updateUidCpuTracker() throws RemoteException {
        }

        @Override // android.app.IOplusActivityManager
        public List<String> getUidCpuWorkingStats() throws RemoteException {
            return null;
        }

        @Override // android.app.IOplusActivityManager
        public long getTotalCpuLoadPercent() throws RemoteException {
            return 0L;
        }

        @Override // android.app.IOplusActivityManager
        public List<String> getTopLoadPidsInfos(int num) throws RemoteException {
            return null;
        }

        @Override // android.app.IOplusActivityManager
        public SharedMemory getCpuLimitLatestLogs(String pkgName) throws RemoteException {
            return null;
        }

        @Override // android.app.IOplusActivityManager
        public List<String> getProcCmdline(int[] pids) throws RemoteException {
            return null;
        }

        @Override // android.app.IOplusActivityManager
        public void activeGc(int[] pids) throws RemoteException {
        }

        @Override // android.app.IOplusActivityManager
        public void finishNotOrderReceiver(IBinder who, int hasCode, int resultCode, String resultData, Bundle resultExtras, boolean resultAbort) throws RemoteException {
        }

        @Override // android.app.IOplusActivityManager
        public void reportSkippedFrames(long currentTime, boolean isAnimation, boolean isForeground, long skippedFrames) throws RemoteException {
        }

        @Override // android.app.IOplusActivityManager
        public void reportSkippedFramesProcName(long currentTime, boolean isAnimation, boolean isForeground, long skippedFrames, String pckName) throws RemoteException {
        }

        @Override // android.app.IOplusActivityManager
        public void reportSkippedFramesActivityName(long currentTime, boolean isAnimation, boolean isForeground, long skippedFrames, String pckName, String activityName) throws RemoteException {
        }

        @Override // android.app.IOplusActivityManager
        public String queryProcessNameFromPid(int pid) throws RemoteException {
            return null;
        }

        @Override // android.app.IOplusActivityManager
        public void notifyAppKillReason(int pid, int uid, int reason, int subReason, String msg) throws RemoteException {
        }

        @Override // android.app.IOplusActivityManager
        public int getFontVariationAdaptionData(String packagename) throws RemoteException {
            return 0;
        }

        @Override // android.app.IOplusActivityManager
        public boolean registerHansListener(String callerPkg, IOplusHansListener listener) throws RemoteException {
            return false;
        }

        @Override // android.app.IOplusActivityManager
        public boolean unregisterHansListener(String callerPkg, IOplusHansListener listener) throws RemoteException {
            return false;
        }

        @Override // android.app.IOplusActivityManager
        public boolean setAppFreeze(String callerPkg, Bundle bundle) throws RemoteException {
            return false;
        }

        @Override // android.app.IOplusActivityManager
        public void notifyAthenaOnekeyClearRunning(int state) throws RemoteException {
        }

        @Override // android.app.IOplusActivityManager
        public void executeResPreload(String pkgName, int userId, int preloadType, String preloadReason) throws RemoteException {
        }

        @Override // android.app.IOplusActivityManager
        public Bundle getResPreloadInfo(int days, int userId) throws RemoteException {
            return null;
        }

        @Override // android.app.IOplusActivityManager
        public long getPreloadIOSize() throws RemoteException {
            return 0L;
        }

        @Override // android.app.IOplusActivityManager
        public boolean getPreloadStatus(String pkgName, int userId) throws RemoteException {
            return false;
        }

        @Override // android.app.IOplusActivityManager
        public Bundle getPreloadPkgList() throws RemoteException {
            return null;
        }

        @Override // android.app.IOplusActivityManager
        public List<String> getPkgPreloadFiles(String pkgName) throws RemoteException {
            return null;
        }

        @Override // android.app.IOplusActivityManager
        public void enterFastFreezer(String callerPkg, int[] uids, long timeout, String reason) throws RemoteException {
        }

        @Override // android.app.IOplusActivityManager
        public void exitFastFreezer(String callerPkg, String reason) throws RemoteException {
        }

        @Override // android.app.IOplusActivityManager
        public boolean isFrozenByHans(String packageName, int uid) throws RemoteException {
            return false;
        }

        @Override // android.app.IOplusActivityManager
        public String getTrafficBytesList(Bundle uids, Bundle outBundle) throws RemoteException {
            return null;
        }

        @Override // android.app.IOplusActivityManager
        public String getTrafficPacketList(Bundle uids, Bundle outBundle) throws RemoteException {
            return null;
        }

        @Override // android.app.IOplusActivityManager
        public void registerEapDataCallback(IOplusEapDataCallback callback) throws RemoteException {
        }

        @Override // android.app.IOplusActivityManager
        public void unregisterEapDataCallback(IOplusEapDataCallback callback) throws RemoteException {
        }

        @Override // android.app.IOplusActivityManager
        public void updateANRDumpState(SharedMemory sharedMemory) throws RemoteException {
        }

        @Override // android.app.IOplusActivityManager
        public void registerAbConfigCallback(IEventCallback callback, String packageName) throws RemoteException {
        }

        @Override // android.app.IOplusActivityManager
        public void unregisterAbConfigCallback(IEventCallback callback) throws RemoteException {
        }

        @Override // android.app.IOplusActivityManager
        public void registerErrorInfoCallback(IEventCallback callback) throws RemoteException {
        }

        @Override // android.app.IOplusActivityManager
        public void unregisterErrorInfoCallback(IEventCallback callback) throws RemoteException {
        }

        @Override // android.app.IOplusActivityManager
        public List<ActivityManager.RecentTaskInfo> getAllVisibleTasksInfo(int userId) throws RemoteException {
            return null;
        }

        @Override // android.app.IOplusActivityManager
        public void anrViaTheiaEvent(int pid, String reason) throws RemoteException {
        }

        @Override // android.app.IOplusActivityManager
        public List<String> getStageProtectList(int type) throws RemoteException {
            return null;
        }

        @Override // android.app.IOplusActivityManager
        public List<String> getStageProtectListAsUser(int type, int userId) throws RemoteException {
            return null;
        }

        @Override // android.app.IOplusActivityManager
        public OplusUXIconData getUXIconData() throws RemoteException {
            return null;
        }

        @Override // android.app.IOplusActivityManager
        public void compactProcess(Bundle pids, int compactionFlags, int advice) throws RemoteException {
        }

        @Override // android.app.IOplusActivityManager
        public boolean inDownloading(int uid, int thresholdSpeed, boolean rough) throws RemoteException {
            return false;
        }

        @Override // android.app.IOplusActivityManager
        public List<OplusPackageFreezeData> getDownloadingList(int thresholdSpeed, boolean rough) throws RemoteException {
            return null;
        }

        @Override // android.app.IOplusActivityManager
        public List<UsageStats> queryUsageStats(int intervalType, long beginTime, long endTime) throws RemoteException {
            return null;
        }

        @Override // android.app.IOplusActivityManager
        public List<ActivityManager.RunningAppProcessInfo> getRunningAppProcessInfos(Bundle pids) throws RemoteException {
            return null;
        }

        @Override // android.app.IOplusActivityManager
        public List<OplusPackageFreezeData> getPackageFreezeDataInfos(Bundle pids) throws RemoteException {
            return null;
        }

        @Override // android.app.IOplusActivityManager
        public void notifyUiSwitched(String uiInfo, int status) throws RemoteException {
        }

        @Override // android.app.IOplusActivityManager
        public boolean addOrRemoveOplusVerifyCodeListener(boolean add, IOplusVerifyCodeListener listener) throws RemoteException {
            return false;
        }

        @Override // android.app.IOplusActivityManager
        public void cleanPackageResources(String packageName, int uid) throws RemoteException {
        }

        @Override // android.app.IOplusActivityManager
        public void forceStopPackageAndSaveActivity(String packageName, int userId) throws RemoteException {
        }

        @Override // android.app.IOplusActivityManager
        public int[] getRunningPidsByUid(int uid) throws RemoteException {
            return null;
        }

        @Override // android.app.IOplusActivityManager
        public void asyncReportFrames(String pkgName, int skippedFrames) throws RemoteException {
        }

        @Override // android.app.IOplusActivityManager
        public boolean requestDeviceFolded(int folded, boolean enableSecDisplay) throws RemoteException {
            return false;
        }

        @Override // android.app.IOplusActivityManager
        public void sendFlingTransit(MotionEvent ev, int duration) throws RemoteException {
        }

        @Override // android.app.IOplusActivityManager
        public void setSceneActionTransit(String scene, String action, int timeout) throws RemoteException {
        }

        @Override // android.app.IOplusActivityManager
        public IOplusCompatModeSession getCompatModeSession() throws RemoteException {
            return null;
        }

        @Override // android.app.IOplusActivityManager
        public boolean registerTerminateObserver(IProcessTerminateObserver observer) throws RemoteException {
            return false;
        }

        @Override // android.app.IOplusActivityManager
        public boolean unregisterTerminateObserver(IProcessTerminateObserver observer) throws RemoteException {
            return false;
        }

        @Override // android.app.IOplusActivityManager
        public int[] getTerminateObservers() throws RemoteException {
            return null;
        }

        @Override // android.app.IOplusActivityManager
        public void notifyProcessTerminate(int[] pids, String reason) throws RemoteException {
        }

        @Override // android.app.IOplusActivityManager
        public void notifyProcessTerminateFinish(IProcessTerminateObserver observer) throws RemoteException {
        }

        @Override // android.app.IOplusActivityManager
        public boolean registerTerminateStateObserver(ITerminateObserver observer) throws RemoteException {
            return false;
        }

        @Override // android.app.IOplusActivityManager
        public boolean unregisterTerminateStateObserver(ITerminateObserver observer) throws RemoteException {
            return false;
        }

        @Override // android.app.IOplusActivityManager
        public void asyncReportDalvikMem(Bundle bundle, int pid, long dalvikMax, long dalvikUsed, long uptime) throws RemoteException {
        }

        @Override // android.app.IOplusActivityManager
        public boolean registerComplexSceneObserver(Bundle bundle, IComplexSceneObserver observer) throws RemoteException {
            return false;
        }

        @Override // android.app.IOplusActivityManager
        public boolean unregisterComplexSceneObserver(Bundle bundle, IComplexSceneObserver observer) throws RemoteException {
            return false;
        }

        @Override // android.app.IOplusActivityManager
        public String getFastAppReplacePkg(String callerPkg) throws RemoteException {
            return null;
        }

        @Override // android.app.IOplusActivityManager
        public int getLoopCpuLoad() throws RemoteException {
            return 0;
        }

        @Override // android.app.IOplusActivityManager
        public void addOplusLoopLoadTime(long timeEnd, String msg) throws RemoteException {
        }

        @Override // android.app.IOplusActivityManager
        public Bundle autoLayoutCall(Bundle inBundle) throws RemoteException {
            return null;
        }

        @Override // android.app.IOplusActivityManager
        public void grantUriPermissionToUser(IApplicationThread caller, String targetPkg, Uri uri, int modeFlags, int userId) throws RemoteException {
        }

        @Override // android.app.IOplusActivityManager
        public void trimSystemMemory(int level, boolean needGc) throws RemoteException {
        }

        @Override // android.app.IOplusActivityManager
        public boolean setPayJoyFlag(int payjoyFlag) throws RemoteException {
            return false;
        }

        @Override // android.app.IOplusActivityManager
        public int getPayJoyFlag() throws RemoteException {
            return 0;
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return null;
        }
    }

    /* loaded from: classes.dex */
    public static abstract class Stub extends Binder implements IOplusActivityManager {
        static final int TRANSACTION_activeGc = 74;
        static final int TRANSACTION_addBackgroundRestrictedInfo = 51;
        static final int TRANSACTION_addFastAppThirdLogin = 49;
        static final int TRANSACTION_addFastAppWCPay = 45;
        static final int TRANSACTION_addMiniProgramShare = 41;
        static final int TRANSACTION_addOplusLoopLoadTime = 137;
        static final int TRANSACTION_addOrRemoveOplusVerifyCodeListener = 116;
        static final int TRANSACTION_addPreventIndulgeList = 53;
        static final int TRANSACTION_addStageProtectInfo = 21;
        static final int TRANSACTION_anrViaTheiaEvent = 105;
        static final int TRANSACTION_asyncReportDalvikMem = 132;
        static final int TRANSACTION_asyncReportFrames = 120;
        static final int TRANSACTION_autoLayoutCall = 138;
        static final int TRANSACTION_cleanPackageResources = 117;
        static final int TRANSACTION_compactProcess = 109;
        static final int TRANSACTION_dumpProcPerfData = 62;
        static final int TRANSACTION_enterFastFreezer = 92;
        static final int TRANSACTION_executeResPreload = 86;
        static final int TRANSACTION_exitFastFreezer = 93;
        static final int TRANSACTION_favoriteQueryRule = 8;
        static final int TRANSACTION_finishNotOrderReceiver = 75;
        static final int TRANSACTION_forceStopPackageAndSaveActivity = 118;
        static final int TRANSACTION_forceTrimAppMemory = 58;
        static final int TRANSACTION_getAccidentallyTouchData = 9;
        static final int TRANSACTION_getAllVisibleTasksInfo = 104;
        static final int TRANSACTION_getCompatModeSession = 124;
        static final int TRANSACTION_getConfigInfo = 55;
        static final int TRANSACTION_getCpuLimitLatestLogs = 72;
        static final int TRANSACTION_getCpuWorkingStats = 57;
        static final int TRANSACTION_getDarkModeData = 60;
        static final int TRANSACTION_getDisplayCompatData = 27;
        static final int TRANSACTION_getDisplayOptimizationData = 23;
        static final int TRANSACTION_getDownloadingList = 111;
        static final int TRANSACTION_getFastAppReplacePkg = 135;
        static final int TRANSACTION_getFontVariationAdaptionData = 81;
        static final int TRANSACTION_getGlobalPkgWhiteList = 19;
        static final int TRANSACTION_getGlobalProcessWhiteList = 20;
        static final int TRANSACTION_getIsSupportMultiApp = 28;
        static final int TRANSACTION_getLoopCpuLoad = 136;
        static final int TRANSACTION_getMultiAppAccessMode = 34;
        static final int TRANSACTION_getMultiAppAlias = 30;
        static final int TRANSACTION_getMultiAppAllAccessMode = 36;
        static final int TRANSACTION_getMultiAppConfig = 32;
        static final int TRANSACTION_getMultiAppList = 29;
        static final int TRANSACTION_getMultiAppMaxCreateNum = 39;
        static final int TRANSACTION_getPackageFreezeDataInfos = 114;
        static final int TRANSACTION_getPayJoyFlag = 142;
        static final int TRANSACTION_getPkgPreloadFiles = 91;
        static final int TRANSACTION_getPreloadIOSize = 88;
        static final int TRANSACTION_getPreloadPkgList = 90;
        static final int TRANSACTION_getPreloadStatus = 89;
        static final int TRANSACTION_getProcCmdline = 73;
        static final int TRANSACTION_getProcCommonInfoList = 63;
        static final int TRANSACTION_getProcDependency = 64;
        static final int TRANSACTION_getProcDependencyByUserId = 65;
        static final int TRANSACTION_getReflectData = 44;
        static final int TRANSACTION_getResPreloadInfo = 87;
        static final int TRANSACTION_getResolveData = 43;
        static final int TRANSACTION_getRunningAppProcessInfos = 113;
        static final int TRANSACTION_getRunningPidsByUid = 119;
        static final int TRANSACTION_getRunningProcesses = 47;
        static final int TRANSACTION_getSecureKeyboardData = 10;
        static final int TRANSACTION_getStageProtectList = 106;
        static final int TRANSACTION_getStageProtectListAsUser = 107;
        static final int TRANSACTION_getStageProtectListFromPkg = 24;
        static final int TRANSACTION_getStageProtectListFromPkgAsUser = 25;
        static final int TRANSACTION_getTaskPkgList = 66;
        static final int TRANSACTION_getTerminateObservers = 127;
        static final int TRANSACTION_getTopLoadPidsInfos = 71;
        static final int TRANSACTION_getTotalCpuLoadPercent = 70;
        static final int TRANSACTION_getTrafficBytesList = 95;
        static final int TRANSACTION_getTrafficPacketList = 96;
        static final int TRANSACTION_getUXIconData = 108;
        static final int TRANSACTION_getUidCpuWorkingStats = 69;
        static final int TRANSACTION_grantOplusPermissionByGroup = 14;
        static final int TRANSACTION_grantUriPermissionToUser = 139;
        static final int TRANSACTION_handleAppForNotification = 17;
        static final int TRANSACTION_handleAppFromControlCenter = 26;
        static final int TRANSACTION_inDownloading = 110;
        static final int TRANSACTION_isFrozenByHans = 94;
        static final int TRANSACTION_isMultiApp = 37;
        static final int TRANSACTION_isNightMode = 61;
        static final int TRANSACTION_isPermissionInterceptEnabled = 13;
        static final int TRANSACTION_killPidForce = 16;
        static final int TRANSACTION_notifyAppKillReason = 80;
        static final int TRANSACTION_notifyAthenaOnekeyClearRunning = 85;
        static final int TRANSACTION_notifyProcessTerminate = 128;
        static final int TRANSACTION_notifyProcessTerminateFinish = 129;
        static final int TRANSACTION_notifyUiSwitched = 115;
        static final int TRANSACTION_onBackPressedOnTheiaMonitor = 5;
        static final int TRANSACTION_putConfigInfo = 54;
        static final int TRANSACTION_queryProcessNameFromPid = 79;
        static final int TRANSACTION_queryUsageStats = 112;
        static final int TRANSACTION_registerAbConfigCallback = 100;
        static final int TRANSACTION_registerComplexSceneObserver = 133;
        static final int TRANSACTION_registerEapDataCallback = 97;
        static final int TRANSACTION_registerErrorInfoCallback = 102;
        static final int TRANSACTION_registerHansListener = 82;
        static final int TRANSACTION_registerTerminateObserver = 125;
        static final int TRANSACTION_registerTerminateStateObserver = 130;
        static final int TRANSACTION_removeFastAppThirdLogin = 50;
        static final int TRANSACTION_removeFastAppWCPay = 46;
        static final int TRANSACTION_removeMiniProgramShare = 42;
        static final int TRANSACTION_removeStageProtectInfo = 22;
        static final int TRANSACTION_reportBindApplicationFinished = 7;
        static final int TRANSACTION_reportSkippedFrames = 76;
        static final int TRANSACTION_reportSkippedFramesActivityName = 78;
        static final int TRANSACTION_reportSkippedFramesProcName = 77;
        static final int TRANSACTION_requestDeviceFolded = 121;
        static final int TRANSACTION_revokeOplusPermissionByGroup = 15;
        static final int TRANSACTION_scanFileIfNeed = 40;
        static final int TRANSACTION_sendFlingTransit = 122;
        static final int TRANSACTION_sendTheiaEvent = 6;
        static final int TRANSACTION_setAppFreeze = 84;
        static final int TRANSACTION_setAppStartMonitorController = 48;
        static final int TRANSACTION_setGameSpaceController = 18;
        static final int TRANSACTION_setGlThreads = 4;
        static final int TRANSACTION_setHwuiTaskThreads = 3;
        static final int TRANSACTION_setMultiAppAccessMode = 35;
        static final int TRANSACTION_setMultiAppAlias = 33;
        static final int TRANSACTION_setMultiAppConfig = 31;
        static final int TRANSACTION_setMultiAppStatus = 38;
        static final int TRANSACTION_setPayJoyFlag = 141;
        static final int TRANSACTION_setPermissionInterceptEnable = 12;
        static final int TRANSACTION_setPermissionRecordController = 59;
        static final int TRANSACTION_setPreventIndulgeController = 52;
        static final int TRANSACTION_setSceneActionTransit = 123;
        static final int TRANSACTION_syncPermissionRecord = 67;
        static final int TRANSACTION_trimSystemMemory = 140;
        static final int TRANSACTION_unfreezeForKernel = 1;
        static final int TRANSACTION_unfreezeForKernelTargetPid = 2;
        static final int TRANSACTION_unregisterAbConfigCallback = 101;
        static final int TRANSACTION_unregisterComplexSceneObserver = 134;
        static final int TRANSACTION_unregisterEapDataCallback = 98;
        static final int TRANSACTION_unregisterErrorInfoCallback = 103;
        static final int TRANSACTION_unregisterHansListener = 83;
        static final int TRANSACTION_unregisterTerminateObserver = 126;
        static final int TRANSACTION_unregisterTerminateStateObserver = 131;
        static final int TRANSACTION_updateANRDumpState = 99;
        static final int TRANSACTION_updateCpuTracker = 56;
        static final int TRANSACTION_updatePermissionChoice = 11;
        static final int TRANSACTION_updateUidCpuTracker = 68;

        public Stub() {
            attachInterface(this, IOplusActivityManager.DESCRIPTOR);
        }

        public static IOplusActivityManager asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(IOplusActivityManager.DESCRIPTOR);
            if (iin != null && (iin instanceof IOplusActivityManager)) {
                return (IOplusActivityManager) iin;
            }
            return new Proxy(obj);
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return this;
        }

        public static String getDefaultTransactionName(int transactionCode) {
            switch (transactionCode) {
                case 1:
                    return "unfreezeForKernel";
                case 2:
                    return "unfreezeForKernelTargetPid";
                case 3:
                    return "setHwuiTaskThreads";
                case 4:
                    return "setGlThreads";
                case 5:
                    return "onBackPressedOnTheiaMonitor";
                case 6:
                    return "sendTheiaEvent";
                case 7:
                    return "reportBindApplicationFinished";
                case 8:
                    return "favoriteQueryRule";
                case 9:
                    return "getAccidentallyTouchData";
                case 10:
                    return "getSecureKeyboardData";
                case 11:
                    return "updatePermissionChoice";
                case 12:
                    return "setPermissionInterceptEnable";
                case 13:
                    return "isPermissionInterceptEnabled";
                case 14:
                    return "grantOplusPermissionByGroup";
                case 15:
                    return "revokeOplusPermissionByGroup";
                case 16:
                    return "killPidForce";
                case 17:
                    return "handleAppForNotification";
                case 18:
                    return "setGameSpaceController";
                case 19:
                    return "getGlobalPkgWhiteList";
                case 20:
                    return "getGlobalProcessWhiteList";
                case 21:
                    return "addStageProtectInfo";
                case 22:
                    return "removeStageProtectInfo";
                case 23:
                    return "getDisplayOptimizationData";
                case 24:
                    return "getStageProtectListFromPkg";
                case 25:
                    return "getStageProtectListFromPkgAsUser";
                case 26:
                    return "handleAppFromControlCenter";
                case 27:
                    return "getDisplayCompatData";
                case 28:
                    return "getIsSupportMultiApp";
                case 29:
                    return "getMultiAppList";
                case 30:
                    return "getMultiAppAlias";
                case 31:
                    return "setMultiAppConfig";
                case 32:
                    return "getMultiAppConfig";
                case 33:
                    return "setMultiAppAlias";
                case 34:
                    return "getMultiAppAccessMode";
                case 35:
                    return "setMultiAppAccessMode";
                case 36:
                    return "getMultiAppAllAccessMode";
                case 37:
                    return "isMultiApp";
                case 38:
                    return "setMultiAppStatus";
                case 39:
                    return "getMultiAppMaxCreateNum";
                case 40:
                    return "scanFileIfNeed";
                case 41:
                    return "addMiniProgramShare";
                case 42:
                    return "removeMiniProgramShare";
                case 43:
                    return "getResolveData";
                case 44:
                    return "getReflectData";
                case 45:
                    return "addFastAppWCPay";
                case 46:
                    return "removeFastAppWCPay";
                case 47:
                    return "getRunningProcesses";
                case 48:
                    return "setAppStartMonitorController";
                case 49:
                    return "addFastAppThirdLogin";
                case 50:
                    return "removeFastAppThirdLogin";
                case 51:
                    return "addBackgroundRestrictedInfo";
                case 52:
                    return "setPreventIndulgeController";
                case 53:
                    return "addPreventIndulgeList";
                case 54:
                    return "putConfigInfo";
                case 55:
                    return "getConfigInfo";
                case 56:
                    return "updateCpuTracker";
                case 57:
                    return "getCpuWorkingStats";
                case 58:
                    return "forceTrimAppMemory";
                case 59:
                    return "setPermissionRecordController";
                case 60:
                    return "getDarkModeData";
                case 61:
                    return "isNightMode";
                case 62:
                    return "dumpProcPerfData";
                case 63:
                    return "getProcCommonInfoList";
                case 64:
                    return "getProcDependency";
                case 65:
                    return "getProcDependencyByUserId";
                case 66:
                    return "getTaskPkgList";
                case 67:
                    return "syncPermissionRecord";
                case 68:
                    return "updateUidCpuTracker";
                case 69:
                    return "getUidCpuWorkingStats";
                case 70:
                    return "getTotalCpuLoadPercent";
                case 71:
                    return "getTopLoadPidsInfos";
                case 72:
                    return "getCpuLimitLatestLogs";
                case 73:
                    return "getProcCmdline";
                case 74:
                    return "activeGc";
                case 75:
                    return "finishNotOrderReceiver";
                case 76:
                    return "reportSkippedFrames";
                case 77:
                    return "reportSkippedFramesProcName";
                case 78:
                    return "reportSkippedFramesActivityName";
                case 79:
                    return "queryProcessNameFromPid";
                case 80:
                    return "notifyAppKillReason";
                case 81:
                    return "getFontVariationAdaptionData";
                case 82:
                    return "registerHansListener";
                case 83:
                    return "unregisterHansListener";
                case 84:
                    return "setAppFreeze";
                case 85:
                    return "notifyAthenaOnekeyClearRunning";
                case 86:
                    return "executeResPreload";
                case 87:
                    return "getResPreloadInfo";
                case 88:
                    return "getPreloadIOSize";
                case 89:
                    return "getPreloadStatus";
                case 90:
                    return "getPreloadPkgList";
                case 91:
                    return "getPkgPreloadFiles";
                case 92:
                    return "enterFastFreezer";
                case 93:
                    return "exitFastFreezer";
                case 94:
                    return "isFrozenByHans";
                case 95:
                    return "getTrafficBytesList";
                case 96:
                    return "getTrafficPacketList";
                case 97:
                    return "registerEapDataCallback";
                case 98:
                    return "unregisterEapDataCallback";
                case 99:
                    return "updateANRDumpState";
                case 100:
                    return "registerAbConfigCallback";
                case 101:
                    return "unregisterAbConfigCallback";
                case 102:
                    return "registerErrorInfoCallback";
                case 103:
                    return "unregisterErrorInfoCallback";
                case 104:
                    return "getAllVisibleTasksInfo";
                case 105:
                    return "anrViaTheiaEvent";
                case 106:
                    return "getStageProtectList";
                case 107:
                    return "getStageProtectListAsUser";
                case 108:
                    return "getUXIconData";
                case 109:
                    return "compactProcess";
                case 110:
                    return "inDownloading";
                case 111:
                    return "getDownloadingList";
                case 112:
                    return "queryUsageStats";
                case 113:
                    return "getRunningAppProcessInfos";
                case 114:
                    return "getPackageFreezeDataInfos";
                case 115:
                    return "notifyUiSwitched";
                case 116:
                    return "addOrRemoveOplusVerifyCodeListener";
                case 117:
                    return "cleanPackageResources";
                case 118:
                    return "forceStopPackageAndSaveActivity";
                case 119:
                    return "getRunningPidsByUid";
                case 120:
                    return "asyncReportFrames";
                case 121:
                    return "requestDeviceFolded";
                case 122:
                    return "sendFlingTransit";
                case 123:
                    return "setSceneActionTransit";
                case 124:
                    return "getCompatModeSession";
                case 125:
                    return "registerTerminateObserver";
                case 126:
                    return "unregisterTerminateObserver";
                case 127:
                    return "getTerminateObservers";
                case 128:
                    return "notifyProcessTerminate";
                case 129:
                    return "notifyProcessTerminateFinish";
                case 130:
                    return "registerTerminateStateObserver";
                case 131:
                    return "unregisterTerminateStateObserver";
                case 132:
                    return "asyncReportDalvikMem";
                case 133:
                    return "registerComplexSceneObserver";
                case 134:
                    return "unregisterComplexSceneObserver";
                case 135:
                    return "getFastAppReplacePkg";
                case 136:
                    return "getLoopCpuLoad";
                case 137:
                    return "addOplusLoopLoadTime";
                case 138:
                    return "autoLayoutCall";
                case 139:
                    return "grantUriPermissionToUser";
                case 140:
                    return "trimSystemMemory";
                case 141:
                    return "setPayJoyFlag";
                case 142:
                    return "getPayJoyFlag";
                default:
                    return null;
            }
        }

        public String getTransactionName(int transactionCode) {
            return getDefaultTransactionName(transactionCode);
        }

        @Override // android.os.Binder
        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            if (code >= 1 && code <= 16777215) {
                data.enforceInterface(IOplusActivityManager.DESCRIPTOR);
            }
            switch (code) {
                case 1598968902:
                    reply.writeString(IOplusActivityManager.DESCRIPTOR);
                    return true;
                default:
                    switch (code) {
                        case 1:
                            int _arg0 = data.readInt();
                            int _arg1 = data.readInt();
                            int _arg2 = data.readInt();
                            String _arg3 = data.readString();
                            int _arg4 = data.readInt();
                            data.enforceNoDataAvail();
                            unfreezeForKernel(_arg0, _arg1, _arg2, _arg3, _arg4);
                            reply.writeNoException();
                            return true;
                        case 2:
                            int _arg02 = data.readInt();
                            int _arg12 = data.readInt();
                            int _arg22 = data.readInt();
                            int _arg32 = data.readInt();
                            int _arg42 = data.readInt();
                            String _arg5 = data.readString();
                            int _arg6 = data.readInt();
                            data.enforceNoDataAvail();
                            unfreezeForKernelTargetPid(_arg02, _arg12, _arg22, _arg32, _arg42, _arg5, _arg6);
                            reply.writeNoException();
                            return true;
                        case 3:
                            int _arg03 = data.readInt();
                            int _arg13 = data.readInt();
                            data.enforceNoDataAvail();
                            setHwuiTaskThreads(_arg03, _arg13);
                            return true;
                        case 4:
                            int _arg04 = data.readInt();
                            int _arg14 = data.readInt();
                            data.enforceNoDataAvail();
                            setGlThreads(_arg04, _arg14);
                            return true;
                        case 5:
                            long _arg05 = data.readLong();
                            data.enforceNoDataAvail();
                            onBackPressedOnTheiaMonitor(_arg05);
                            return true;
                        case 6:
                            long _arg06 = data.readLong();
                            Intent _arg15 = (Intent) data.readTypedObject(Intent.CREATOR);
                            data.enforceNoDataAvail();
                            sendTheiaEvent(_arg06, _arg15);
                            return true;
                        case 7:
                            String _arg07 = data.readString();
                            int _arg16 = data.readInt();
                            int _arg23 = data.readInt();
                            data.enforceNoDataAvail();
                            reportBindApplicationFinished(_arg07, _arg16, _arg23);
                            return true;
                        case 8:
                            String _arg08 = data.readString();
                            IOplusFavoriteQueryCallback _arg17 = IOplusFavoriteQueryCallback.Stub.asInterface(data.readStrongBinder());
                            data.enforceNoDataAvail();
                            favoriteQueryRule(_arg08, _arg17);
                            return true;
                        case 9:
                            OplusAccidentallyTouchData _result = getAccidentallyTouchData();
                            reply.writeNoException();
                            reply.writeTypedObject(_result, 1);
                            return true;
                        case 10:
                            OplusSecureKeyboardData _result2 = getSecureKeyboardData();
                            reply.writeNoException();
                            reply.writeTypedObject(_result2, 1);
                            return true;
                        case 11:
                            String _arg09 = data.readString();
                            String _arg18 = data.readString();
                            int _arg24 = data.readInt();
                            data.enforceNoDataAvail();
                            updatePermissionChoice(_arg09, _arg18, _arg24);
                            reply.writeNoException();
                            return true;
                        case 12:
                            boolean _arg010 = data.readBoolean();
                            data.enforceNoDataAvail();
                            setPermissionInterceptEnable(_arg010);
                            reply.writeNoException();
                            return true;
                        case 13:
                            boolean _result3 = isPermissionInterceptEnabled();
                            reply.writeNoException();
                            reply.writeBoolean(_result3);
                            return true;
                        case 14:
                            String _arg011 = data.readString();
                            String _arg19 = data.readString();
                            data.enforceNoDataAvail();
                            grantOplusPermissionByGroup(_arg011, _arg19);
                            reply.writeNoException();
                            return true;
                        case 15:
                            String _arg012 = data.readString();
                            String _arg110 = data.readString();
                            data.enforceNoDataAvail();
                            revokeOplusPermissionByGroup(_arg012, _arg110);
                            reply.writeNoException();
                            return true;
                        case 16:
                            int _arg013 = data.readInt();
                            data.enforceNoDataAvail();
                            killPidForce(_arg013);
                            reply.writeNoException();
                            return true;
                        case 17:
                            String _arg014 = data.readString();
                            int _arg111 = data.readInt();
                            int _arg25 = data.readInt();
                            data.enforceNoDataAvail();
                            handleAppForNotification(_arg014, _arg111, _arg25);
                            reply.writeNoException();
                            return true;
                        case 18:
                            IOplusGameSpaceController _arg015 = IOplusGameSpaceController.Stub.asInterface(data.readStrongBinder());
                            data.enforceNoDataAvail();
                            setGameSpaceController(_arg015);
                            reply.writeNoException();
                            return true;
                        case 19:
                            int _arg016 = data.readInt();
                            data.enforceNoDataAvail();
                            List<String> _result4 = getGlobalPkgWhiteList(_arg016);
                            reply.writeNoException();
                            reply.writeStringList(_result4);
                            return true;
                        case 20:
                            List<String> _result5 = getGlobalProcessWhiteList();
                            reply.writeNoException();
                            reply.writeStringList(_result5);
                            return true;
                        case 21:
                            String _arg017 = data.readString();
                            String _arg112 = data.readString();
                            List<String> _arg26 = data.createStringArrayList();
                            String _arg33 = data.readString();
                            long _arg43 = data.readLong();
                            IOplusProtectConnection _arg52 = IOplusProtectConnection.Stub.asInterface(data.readStrongBinder());
                            data.enforceNoDataAvail();
                            addStageProtectInfo(_arg017, _arg112, _arg26, _arg33, _arg43, _arg52);
                            reply.writeNoException();
                            return true;
                        case 22:
                            String _arg018 = data.readString();
                            String _arg113 = data.readString();
                            data.enforceNoDataAvail();
                            removeStageProtectInfo(_arg018, _arg113);
                            reply.writeNoException();
                            return true;
                        case 23:
                            OplusDisplayOptimizationData _result6 = getDisplayOptimizationData();
                            reply.writeNoException();
                            reply.writeTypedObject(_result6, 1);
                            return true;
                        case 24:
                            String _arg019 = data.readString();
                            int _arg114 = data.readInt();
                            data.enforceNoDataAvail();
                            List<String> _result7 = getStageProtectListFromPkg(_arg019, _arg114);
                            reply.writeNoException();
                            reply.writeStringList(_result7);
                            return true;
                        case 25:
                            String _arg020 = data.readString();
                            int _arg115 = data.readInt();
                            int _arg27 = data.readInt();
                            data.enforceNoDataAvail();
                            List<String> _result8 = getStageProtectListFromPkgAsUser(_arg020, _arg115, _arg27);
                            reply.writeNoException();
                            reply.writeStringList(_result8);
                            return true;
                        case 26:
                            String _arg021 = data.readString();
                            int _arg116 = data.readInt();
                            data.enforceNoDataAvail();
                            handleAppFromControlCenter(_arg021, _arg116);
                            reply.writeNoException();
                            return true;
                        case 27:
                            OplusDisplayCompatData _result9 = getDisplayCompatData();
                            reply.writeNoException();
                            reply.writeTypedObject(_result9, 1);
                            return true;
                        case 28:
                            boolean _result10 = getIsSupportMultiApp();
                            reply.writeNoException();
                            reply.writeBoolean(_result10);
                            return true;
                        case 29:
                            int _arg022 = data.readInt();
                            data.enforceNoDataAvail();
                            List<String> _result11 = getMultiAppList(_arg022);
                            reply.writeNoException();
                            reply.writeStringList(_result11);
                            return true;
                        case 30:
                            String _arg023 = data.readString();
                            data.enforceNoDataAvail();
                            String _result12 = getMultiAppAlias(_arg023);
                            reply.writeNoException();
                            reply.writeString(_result12);
                            return true;
                        case 31:
                            OplusMultiAppConfig _arg024 = (OplusMultiAppConfig) data.readTypedObject(OplusMultiAppConfig.CREATOR);
                            data.enforceNoDataAvail();
                            int _result13 = setMultiAppConfig(_arg024);
                            reply.writeNoException();
                            reply.writeInt(_result13);
                            return true;
                        case 32:
                            OplusMultiAppConfig _result14 = getMultiAppConfig();
                            reply.writeNoException();
                            reply.writeTypedObject(_result14, 1);
                            return true;
                        case 33:
                            String _arg025 = data.readString();
                            String _arg117 = data.readString();
                            data.enforceNoDataAvail();
                            int _result15 = setMultiAppAlias(_arg025, _arg117);
                            reply.writeNoException();
                            reply.writeInt(_result15);
                            return true;
                        case 34:
                            String _arg026 = data.readString();
                            data.enforceNoDataAvail();
                            int _result16 = getMultiAppAccessMode(_arg026);
                            reply.writeNoException();
                            reply.writeInt(_result16);
                            return true;
                        case 35:
                            String _arg027 = data.readString();
                            int _arg118 = data.readInt();
                            data.enforceNoDataAvail();
                            int _result17 = setMultiAppAccessMode(_arg027, _arg118);
                            reply.writeNoException();
                            reply.writeInt(_result17);
                            return true;
                        case 36:
                            Bundle _arg028 = new Bundle();
                            data.enforceNoDataAvail();
                            getMultiAppAllAccessMode(_arg028);
                            reply.writeNoException();
                            reply.writeTypedObject(_arg028, 1);
                            return true;
                        case 37:
                            int _arg029 = data.readInt();
                            String _arg119 = data.readString();
                            data.enforceNoDataAvail();
                            boolean _result18 = isMultiApp(_arg029, _arg119);
                            reply.writeNoException();
                            reply.writeBoolean(_result18);
                            return true;
                        case 38:
                            String _arg030 = data.readString();
                            int _arg120 = data.readInt();
                            data.enforceNoDataAvail();
                            int _result19 = setMultiAppStatus(_arg030, _arg120);
                            reply.writeNoException();
                            reply.writeInt(_result19);
                            return true;
                        case 39:
                            int _result20 = getMultiAppMaxCreateNum();
                            reply.writeNoException();
                            reply.writeInt(_result20);
                            return true;
                        case 40:
                            int _arg031 = data.readInt();
                            String _arg121 = data.readString();
                            data.enforceNoDataAvail();
                            scanFileIfNeed(_arg031, _arg121);
                            reply.writeNoException();
                            return true;
                        case 41:
                            String _arg032 = data.readString();
                            String _arg122 = data.readString();
                            String _arg28 = data.readString();
                            data.enforceNoDataAvail();
                            addMiniProgramShare(_arg032, _arg122, _arg28);
                            reply.writeNoException();
                            return true;
                        case 42:
                            String _arg033 = data.readString();
                            String _arg123 = data.readString();
                            String _arg29 = data.readString();
                            data.enforceNoDataAvail();
                            removeMiniProgramShare(_arg033, _arg123, _arg29);
                            reply.writeNoException();
                            return true;
                        case 43:
                            OplusResolveData _result21 = getResolveData();
                            reply.writeNoException();
                            reply.writeTypedObject(_result21, 1);
                            return true;
                        case 44:
                            OplusReflectData _result22 = getReflectData();
                            reply.writeNoException();
                            reply.writeTypedObject(_result22, 1);
                            return true;
                        case 45:
                            String _arg034 = data.readString();
                            String _arg124 = data.readString();
                            data.enforceNoDataAvail();
                            addFastAppWCPay(_arg034, _arg124);
                            reply.writeNoException();
                            return true;
                        case 46:
                            String _arg035 = data.readString();
                            String _arg125 = data.readString();
                            data.enforceNoDataAvail();
                            removeFastAppWCPay(_arg035, _arg125);
                            reply.writeNoException();
                            return true;
                        case 47:
                            List<OplusPackageFreezeData> _result23 = getRunningProcesses();
                            reply.writeNoException();
                            reply.writeTypedList(_result23, 1);
                            return true;
                        case 48:
                            IOplusAppStartController _arg036 = IOplusAppStartController.Stub.asInterface(data.readStrongBinder());
                            data.enforceNoDataAvail();
                            setAppStartMonitorController(_arg036);
                            reply.writeNoException();
                            return true;
                        case 49:
                            String _arg037 = data.readString();
                            String _arg126 = data.readString();
                            data.enforceNoDataAvail();
                            addFastAppThirdLogin(_arg037, _arg126);
                            reply.writeNoException();
                            return true;
                        case 50:
                            String _arg038 = data.readString();
                            String _arg127 = data.readString();
                            data.enforceNoDataAvail();
                            removeFastAppThirdLogin(_arg038, _arg127);
                            reply.writeNoException();
                            return true;
                        case 51:
                            String _arg039 = data.readString();
                            List<String> _arg128 = data.createStringArrayList();
                            data.enforceNoDataAvail();
                            addBackgroundRestrictedInfo(_arg039, _arg128);
                            reply.writeNoException();
                            return true;
                        case 52:
                            IOplusAppStartController _arg040 = IOplusAppStartController.Stub.asInterface(data.readStrongBinder());
                            data.enforceNoDataAvail();
                            setPreventIndulgeController(_arg040);
                            reply.writeNoException();
                            return true;
                        case 53:
                            List<String> _arg041 = data.createStringArrayList();
                            data.enforceNoDataAvail();
                            addPreventIndulgeList(_arg041);
                            reply.writeNoException();
                            return true;
                        case 54:
                            String _arg042 = data.readString();
                            Bundle _arg129 = (Bundle) data.readTypedObject(Bundle.CREATOR);
                            int _arg210 = data.readInt();
                            int _arg34 = data.readInt();
                            data.enforceNoDataAvail();
                            boolean _result24 = putConfigInfo(_arg042, _arg129, _arg210, _arg34);
                            reply.writeNoException();
                            reply.writeBoolean(_result24);
                            return true;
                        case 55:
                            String _arg043 = data.readString();
                            int _arg130 = data.readInt();
                            int _arg211 = data.readInt();
                            data.enforceNoDataAvail();
                            Bundle _result25 = getConfigInfo(_arg043, _arg130, _arg211);
                            reply.writeNoException();
                            reply.writeTypedObject(_result25, 1);
                            return true;
                        case 56:
                            long _arg044 = data.readLong();
                            data.enforceNoDataAvail();
                            float _result26 = updateCpuTracker(_arg044);
                            reply.writeNoException();
                            reply.writeFloat(_result26);
                            return true;
                        case 57:
                            List<String> _result27 = getCpuWorkingStats();
                            reply.writeNoException();
                            reply.writeStringList(_result27);
                            return true;
                        case 58:
                            int _arg045 = data.readInt();
                            data.enforceNoDataAvail();
                            forceTrimAppMemory(_arg045);
                            reply.writeNoException();
                            return true;
                        case 59:
                            IOplusPermissionRecordController _arg046 = IOplusPermissionRecordController.Stub.asInterface(data.readStrongBinder());
                            data.enforceNoDataAvail();
                            setPermissionRecordController(_arg046);
                            reply.writeNoException();
                            return true;
                        case 60:
                            String _arg047 = data.readString();
                            data.enforceNoDataAvail();
                            OplusDarkModeData _result28 = getDarkModeData(_arg047);
                            reply.writeNoException();
                            reply.writeTypedObject(_result28, 1);
                            return true;
                        case 61:
                            boolean _result29 = isNightMode();
                            reply.writeNoException();
                            reply.writeBoolean(_result29);
                            return true;
                        case 62:
                            Bundle _arg048 = (Bundle) data.readTypedObject(Bundle.CREATOR);
                            data.enforceNoDataAvail();
                            boolean _result30 = dumpProcPerfData(_arg048);
                            reply.writeNoException();
                            reply.writeBoolean(_result30);
                            return true;
                        case 63:
                            int _arg049 = data.readInt();
                            data.enforceNoDataAvail();
                            List<String> _result31 = getProcCommonInfoList(_arg049);
                            reply.writeNoException();
                            reply.writeStringList(_result31);
                            return true;
                        case 64:
                            int _arg050 = data.readInt();
                            data.enforceNoDataAvail();
                            List<OplusProcDependData> _result32 = getProcDependency(_arg050);
                            reply.writeNoException();
                            reply.writeTypedList(_result32, 1);
                            return true;
                        case 65:
                            String _arg051 = data.readString();
                            int _arg131 = data.readInt();
                            data.enforceNoDataAvail();
                            List<OplusProcDependData> _result33 = getProcDependencyByUserId(_arg051, _arg131);
                            reply.writeNoException();
                            reply.writeTypedList(_result33, 1);
                            return true;
                        case 66:
                            int _arg052 = data.readInt();
                            data.enforceNoDataAvail();
                            List<String> _result34 = getTaskPkgList(_arg052);
                            reply.writeNoException();
                            reply.writeStringList(_result34);
                            return true;
                        case 67:
                            syncPermissionRecord();
                            reply.writeNoException();
                            return true;
                        case 68:
                            updateUidCpuTracker();
                            reply.writeNoException();
                            return true;
                        case 69:
                            List<String> _result35 = getUidCpuWorkingStats();
                            reply.writeNoException();
                            reply.writeStringList(_result35);
                            return true;
                        case 70:
                            long _result36 = getTotalCpuLoadPercent();
                            reply.writeNoException();
                            reply.writeLong(_result36);
                            return true;
                        case 71:
                            int _arg053 = data.readInt();
                            data.enforceNoDataAvail();
                            List<String> _result37 = getTopLoadPidsInfos(_arg053);
                            reply.writeNoException();
                            reply.writeStringList(_result37);
                            return true;
                        case 72:
                            String _arg054 = data.readString();
                            data.enforceNoDataAvail();
                            SharedMemory _result38 = getCpuLimitLatestLogs(_arg054);
                            reply.writeNoException();
                            reply.writeTypedObject(_result38, 1);
                            return true;
                        case 73:
                            int[] _arg055 = data.createIntArray();
                            data.enforceNoDataAvail();
                            List<String> _result39 = getProcCmdline(_arg055);
                            reply.writeNoException();
                            reply.writeStringList(_result39);
                            return true;
                        case 74:
                            int[] _arg056 = data.createIntArray();
                            data.enforceNoDataAvail();
                            activeGc(_arg056);
                            return true;
                        case 75:
                            IBinder _arg057 = data.readStrongBinder();
                            int _arg132 = data.readInt();
                            int _arg212 = data.readInt();
                            String _arg35 = data.readString();
                            Bundle _arg44 = (Bundle) data.readTypedObject(Bundle.CREATOR);
                            boolean _arg53 = data.readBoolean();
                            data.enforceNoDataAvail();
                            finishNotOrderReceiver(_arg057, _arg132, _arg212, _arg35, _arg44, _arg53);
                            reply.writeNoException();
                            return true;
                        case 76:
                            long _arg058 = data.readLong();
                            boolean _arg133 = data.readBoolean();
                            boolean _arg213 = data.readBoolean();
                            long _arg36 = data.readLong();
                            data.enforceNoDataAvail();
                            reportSkippedFrames(_arg058, _arg133, _arg213, _arg36);
                            reply.writeNoException();
                            return true;
                        case 77:
                            long _arg059 = data.readLong();
                            boolean _arg134 = data.readBoolean();
                            boolean _arg214 = data.readBoolean();
                            long _arg37 = data.readLong();
                            String _arg45 = data.readString();
                            data.enforceNoDataAvail();
                            reportSkippedFramesProcName(_arg059, _arg134, _arg214, _arg37, _arg45);
                            reply.writeNoException();
                            return true;
                        case 78:
                            long _arg060 = data.readLong();
                            boolean _arg135 = data.readBoolean();
                            boolean _arg215 = data.readBoolean();
                            long _arg38 = data.readLong();
                            String _arg46 = data.readString();
                            String _arg54 = data.readString();
                            data.enforceNoDataAvail();
                            reportSkippedFramesActivityName(_arg060, _arg135, _arg215, _arg38, _arg46, _arg54);
                            reply.writeNoException();
                            return true;
                        case 79:
                            int _arg061 = data.readInt();
                            data.enforceNoDataAvail();
                            String _result40 = queryProcessNameFromPid(_arg061);
                            reply.writeNoException();
                            reply.writeString(_result40);
                            return true;
                        case 80:
                            int _arg062 = data.readInt();
                            int _arg136 = data.readInt();
                            int _arg216 = data.readInt();
                            int _arg39 = data.readInt();
                            String _arg47 = data.readString();
                            data.enforceNoDataAvail();
                            notifyAppKillReason(_arg062, _arg136, _arg216, _arg39, _arg47);
                            return true;
                        case 81:
                            String _arg063 = data.readString();
                            data.enforceNoDataAvail();
                            int _result41 = getFontVariationAdaptionData(_arg063);
                            reply.writeNoException();
                            reply.writeInt(_result41);
                            return true;
                        case 82:
                            String _arg064 = data.readString();
                            IOplusHansListener _arg137 = IOplusHansListener.Stub.asInterface(data.readStrongBinder());
                            data.enforceNoDataAvail();
                            boolean _result42 = registerHansListener(_arg064, _arg137);
                            reply.writeNoException();
                            reply.writeBoolean(_result42);
                            return true;
                        case 83:
                            String _arg065 = data.readString();
                            IOplusHansListener _arg138 = IOplusHansListener.Stub.asInterface(data.readStrongBinder());
                            data.enforceNoDataAvail();
                            boolean _result43 = unregisterHansListener(_arg065, _arg138);
                            reply.writeNoException();
                            reply.writeBoolean(_result43);
                            return true;
                        case 84:
                            String _arg066 = data.readString();
                            Bundle _arg139 = (Bundle) data.readTypedObject(Bundle.CREATOR);
                            data.enforceNoDataAvail();
                            boolean _result44 = setAppFreeze(_arg066, _arg139);
                            reply.writeNoException();
                            reply.writeBoolean(_result44);
                            return true;
                        case 85:
                            int _arg067 = data.readInt();
                            data.enforceNoDataAvail();
                            notifyAthenaOnekeyClearRunning(_arg067);
                            return true;
                        case 86:
                            String _arg068 = data.readString();
                            int _arg140 = data.readInt();
                            int _arg217 = data.readInt();
                            String _arg310 = data.readString();
                            data.enforceNoDataAvail();
                            executeResPreload(_arg068, _arg140, _arg217, _arg310);
                            return true;
                        case 87:
                            int _arg069 = data.readInt();
                            int _arg141 = data.readInt();
                            data.enforceNoDataAvail();
                            Bundle _result45 = getResPreloadInfo(_arg069, _arg141);
                            reply.writeNoException();
                            reply.writeTypedObject(_result45, 1);
                            return true;
                        case 88:
                            long _result46 = getPreloadIOSize();
                            reply.writeNoException();
                            reply.writeLong(_result46);
                            return true;
                        case 89:
                            String _arg070 = data.readString();
                            int _arg142 = data.readInt();
                            data.enforceNoDataAvail();
                            boolean _result47 = getPreloadStatus(_arg070, _arg142);
                            reply.writeNoException();
                            reply.writeBoolean(_result47);
                            return true;
                        case 90:
                            Bundle _result48 = getPreloadPkgList();
                            reply.writeNoException();
                            reply.writeTypedObject(_result48, 1);
                            return true;
                        case 91:
                            String _arg071 = data.readString();
                            data.enforceNoDataAvail();
                            List<String> _result49 = getPkgPreloadFiles(_arg071);
                            reply.writeNoException();
                            reply.writeStringList(_result49);
                            return true;
                        case 92:
                            String _arg072 = data.readString();
                            int[] _arg143 = data.createIntArray();
                            long _arg218 = data.readLong();
                            String _arg311 = data.readString();
                            data.enforceNoDataAvail();
                            enterFastFreezer(_arg072, _arg143, _arg218, _arg311);
                            reply.writeNoException();
                            return true;
                        case 93:
                            String _arg073 = data.readString();
                            String _arg144 = data.readString();
                            data.enforceNoDataAvail();
                            exitFastFreezer(_arg073, _arg144);
                            reply.writeNoException();
                            return true;
                        case 94:
                            String _arg074 = data.readString();
                            int _arg145 = data.readInt();
                            data.enforceNoDataAvail();
                            boolean _result50 = isFrozenByHans(_arg074, _arg145);
                            reply.writeNoException();
                            reply.writeBoolean(_result50);
                            return true;
                        case 95:
                            Bundle _arg075 = (Bundle) data.readTypedObject(Bundle.CREATOR);
                            Bundle _arg146 = new Bundle();
                            data.enforceNoDataAvail();
                            String _result51 = getTrafficBytesList(_arg075, _arg146);
                            reply.writeNoException();
                            reply.writeString(_result51);
                            reply.writeTypedObject(_arg146, 1);
                            return true;
                        case 96:
                            Bundle _arg076 = (Bundle) data.readTypedObject(Bundle.CREATOR);
                            Bundle _arg147 = new Bundle();
                            data.enforceNoDataAvail();
                            String _result52 = getTrafficPacketList(_arg076, _arg147);
                            reply.writeNoException();
                            reply.writeString(_result52);
                            reply.writeTypedObject(_arg147, 1);
                            return true;
                        case 97:
                            IOplusEapDataCallback _arg077 = IOplusEapDataCallback.Stub.asInterface(data.readStrongBinder());
                            data.enforceNoDataAvail();
                            registerEapDataCallback(_arg077);
                            reply.writeNoException();
                            return true;
                        case 98:
                            IOplusEapDataCallback _arg078 = IOplusEapDataCallback.Stub.asInterface(data.readStrongBinder());
                            data.enforceNoDataAvail();
                            unregisterEapDataCallback(_arg078);
                            reply.writeNoException();
                            return true;
                        case 99:
                            SharedMemory _arg079 = (SharedMemory) data.readTypedObject(SharedMemory.CREATOR);
                            data.enforceNoDataAvail();
                            updateANRDumpState(_arg079);
                            reply.writeNoException();
                            return true;
                        case 100:
                            IEventCallback _arg080 = IEventCallback.Stub.asInterface(data.readStrongBinder());
                            String _arg148 = data.readString();
                            data.enforceNoDataAvail();
                            registerAbConfigCallback(_arg080, _arg148);
                            reply.writeNoException();
                            return true;
                        case 101:
                            IEventCallback _arg081 = IEventCallback.Stub.asInterface(data.readStrongBinder());
                            data.enforceNoDataAvail();
                            unregisterAbConfigCallback(_arg081);
                            reply.writeNoException();
                            return true;
                        case 102:
                            IEventCallback _arg082 = IEventCallback.Stub.asInterface(data.readStrongBinder());
                            data.enforceNoDataAvail();
                            registerErrorInfoCallback(_arg082);
                            reply.writeNoException();
                            return true;
                        case 103:
                            IEventCallback _arg083 = IEventCallback.Stub.asInterface(data.readStrongBinder());
                            data.enforceNoDataAvail();
                            unregisterErrorInfoCallback(_arg083);
                            reply.writeNoException();
                            return true;
                        case 104:
                            int _arg084 = data.readInt();
                            data.enforceNoDataAvail();
                            List<ActivityManager.RecentTaskInfo> _result53 = getAllVisibleTasksInfo(_arg084);
                            reply.writeNoException();
                            reply.writeTypedList(_result53, 1);
                            return true;
                        case 105:
                            int _arg085 = data.readInt();
                            String _arg149 = data.readString();
                            data.enforceNoDataAvail();
                            anrViaTheiaEvent(_arg085, _arg149);
                            reply.writeNoException();
                            return true;
                        case 106:
                            int _arg086 = data.readInt();
                            data.enforceNoDataAvail();
                            List<String> _result54 = getStageProtectList(_arg086);
                            reply.writeNoException();
                            reply.writeStringList(_result54);
                            return true;
                        case 107:
                            int _arg087 = data.readInt();
                            int _arg150 = data.readInt();
                            data.enforceNoDataAvail();
                            List<String> _result55 = getStageProtectListAsUser(_arg087, _arg150);
                            reply.writeNoException();
                            reply.writeStringList(_result55);
                            return true;
                        case 108:
                            OplusUXIconData _result56 = getUXIconData();
                            reply.writeNoException();
                            reply.writeTypedObject(_result56, 1);
                            return true;
                        case 109:
                            Bundle _arg088 = (Bundle) data.readTypedObject(Bundle.CREATOR);
                            int _arg151 = data.readInt();
                            int _arg219 = data.readInt();
                            data.enforceNoDataAvail();
                            compactProcess(_arg088, _arg151, _arg219);
                            reply.writeNoException();
                            return true;
                        case 110:
                            int _arg089 = data.readInt();
                            int _arg152 = data.readInt();
                            boolean _arg220 = data.readBoolean();
                            data.enforceNoDataAvail();
                            boolean _result57 = inDownloading(_arg089, _arg152, _arg220);
                            reply.writeNoException();
                            reply.writeBoolean(_result57);
                            return true;
                        case 111:
                            int _arg090 = data.readInt();
                            boolean _arg153 = data.readBoolean();
                            data.enforceNoDataAvail();
                            List<OplusPackageFreezeData> _result58 = getDownloadingList(_arg090, _arg153);
                            reply.writeNoException();
                            reply.writeTypedList(_result58, 1);
                            return true;
                        case 112:
                            int _arg091 = data.readInt();
                            long _arg154 = data.readLong();
                            long _arg221 = data.readLong();
                            data.enforceNoDataAvail();
                            List<UsageStats> _result59 = queryUsageStats(_arg091, _arg154, _arg221);
                            reply.writeNoException();
                            reply.writeTypedList(_result59, 1);
                            return true;
                        case 113:
                            Bundle _arg092 = (Bundle) data.readTypedObject(Bundle.CREATOR);
                            data.enforceNoDataAvail();
                            List<ActivityManager.RunningAppProcessInfo> _result60 = getRunningAppProcessInfos(_arg092);
                            reply.writeNoException();
                            reply.writeTypedList(_result60, 1);
                            return true;
                        case 114:
                            Bundle _arg093 = (Bundle) data.readTypedObject(Bundle.CREATOR);
                            data.enforceNoDataAvail();
                            List<OplusPackageFreezeData> _result61 = getPackageFreezeDataInfos(_arg093);
                            reply.writeNoException();
                            reply.writeTypedList(_result61, 1);
                            return true;
                        case 115:
                            String _arg094 = data.readString();
                            int _arg155 = data.readInt();
                            data.enforceNoDataAvail();
                            notifyUiSwitched(_arg094, _arg155);
                            return true;
                        case 116:
                            boolean _arg095 = data.readBoolean();
                            IOplusVerifyCodeListener _arg156 = IOplusVerifyCodeListener.Stub.asInterface(data.readStrongBinder());
                            data.enforceNoDataAvail();
                            boolean _result62 = addOrRemoveOplusVerifyCodeListener(_arg095, _arg156);
                            reply.writeNoException();
                            reply.writeBoolean(_result62);
                            return true;
                        case 117:
                            String _arg096 = data.readString();
                            int _arg157 = data.readInt();
                            data.enforceNoDataAvail();
                            cleanPackageResources(_arg096, _arg157);
                            reply.writeNoException();
                            return true;
                        case 118:
                            String _arg097 = data.readString();
                            int _arg158 = data.readInt();
                            data.enforceNoDataAvail();
                            forceStopPackageAndSaveActivity(_arg097, _arg158);
                            reply.writeNoException();
                            return true;
                        case 119:
                            int _arg098 = data.readInt();
                            data.enforceNoDataAvail();
                            int[] _result63 = getRunningPidsByUid(_arg098);
                            reply.writeNoException();
                            reply.writeIntArray(_result63);
                            return true;
                        case 120:
                            String _arg099 = data.readString();
                            int _arg159 = data.readInt();
                            data.enforceNoDataAvail();
                            asyncReportFrames(_arg099, _arg159);
                            return true;
                        case 121:
                            int _arg0100 = data.readInt();
                            boolean _arg160 = data.readBoolean();
                            data.enforceNoDataAvail();
                            boolean _result64 = requestDeviceFolded(_arg0100, _arg160);
                            reply.writeNoException();
                            reply.writeBoolean(_result64);
                            return true;
                        case 122:
                            MotionEvent _arg0101 = (MotionEvent) data.readTypedObject(MotionEvent.CREATOR);
                            int _arg161 = data.readInt();
                            data.enforceNoDataAvail();
                            sendFlingTransit(_arg0101, _arg161);
                            return true;
                        case 123:
                            String _arg0102 = data.readString();
                            String _arg162 = data.readString();
                            int _arg222 = data.readInt();
                            data.enforceNoDataAvail();
                            setSceneActionTransit(_arg0102, _arg162, _arg222);
                            return true;
                        case 124:
                            IOplusCompatModeSession _result65 = getCompatModeSession();
                            reply.writeNoException();
                            reply.writeStrongInterface(_result65);
                            return true;
                        case 125:
                            IProcessTerminateObserver _arg0103 = IProcessTerminateObserver.Stub.asInterface(data.readStrongBinder());
                            data.enforceNoDataAvail();
                            boolean _result66 = registerTerminateObserver(_arg0103);
                            reply.writeNoException();
                            reply.writeBoolean(_result66);
                            return true;
                        case 126:
                            IProcessTerminateObserver _arg0104 = IProcessTerminateObserver.Stub.asInterface(data.readStrongBinder());
                            data.enforceNoDataAvail();
                            boolean _result67 = unregisterTerminateObserver(_arg0104);
                            reply.writeNoException();
                            reply.writeBoolean(_result67);
                            return true;
                        case 127:
                            int[] _result68 = getTerminateObservers();
                            reply.writeNoException();
                            reply.writeIntArray(_result68);
                            return true;
                        case 128:
                            int[] _arg0105 = data.createIntArray();
                            String _arg163 = data.readString();
                            data.enforceNoDataAvail();
                            notifyProcessTerminate(_arg0105, _arg163);
                            reply.writeNoException();
                            return true;
                        case 129:
                            IProcessTerminateObserver _arg0106 = IProcessTerminateObserver.Stub.asInterface(data.readStrongBinder());
                            data.enforceNoDataAvail();
                            notifyProcessTerminateFinish(_arg0106);
                            reply.writeNoException();
                            return true;
                        case 130:
                            ITerminateObserver _arg0107 = ITerminateObserver.Stub.asInterface(data.readStrongBinder());
                            data.enforceNoDataAvail();
                            boolean _result69 = registerTerminateStateObserver(_arg0107);
                            reply.writeNoException();
                            reply.writeBoolean(_result69);
                            return true;
                        case 131:
                            ITerminateObserver _arg0108 = ITerminateObserver.Stub.asInterface(data.readStrongBinder());
                            data.enforceNoDataAvail();
                            boolean _result70 = unregisterTerminateStateObserver(_arg0108);
                            reply.writeNoException();
                            reply.writeBoolean(_result70);
                            return true;
                        case 132:
                            Bundle _arg0109 = (Bundle) data.readTypedObject(Bundle.CREATOR);
                            int _arg164 = data.readInt();
                            long _arg223 = data.readLong();
                            long _arg312 = data.readLong();
                            long _arg48 = data.readLong();
                            data.enforceNoDataAvail();
                            asyncReportDalvikMem(_arg0109, _arg164, _arg223, _arg312, _arg48);
                            return true;
                        case 133:
                            Bundle _arg0110 = (Bundle) data.readTypedObject(Bundle.CREATOR);
                            IComplexSceneObserver _arg165 = IComplexSceneObserver.Stub.asInterface(data.readStrongBinder());
                            data.enforceNoDataAvail();
                            boolean _result71 = registerComplexSceneObserver(_arg0110, _arg165);
                            reply.writeNoException();
                            reply.writeBoolean(_result71);
                            return true;
                        case 134:
                            Bundle _arg0111 = (Bundle) data.readTypedObject(Bundle.CREATOR);
                            IComplexSceneObserver _arg166 = IComplexSceneObserver.Stub.asInterface(data.readStrongBinder());
                            data.enforceNoDataAvail();
                            boolean _result72 = unregisterComplexSceneObserver(_arg0111, _arg166);
                            reply.writeNoException();
                            reply.writeBoolean(_result72);
                            return true;
                        case 135:
                            String _arg0112 = data.readString();
                            data.enforceNoDataAvail();
                            String _result73 = getFastAppReplacePkg(_arg0112);
                            reply.writeNoException();
                            reply.writeString(_result73);
                            return true;
                        case 136:
                            int _result74 = getLoopCpuLoad();
                            reply.writeNoException();
                            reply.writeInt(_result74);
                            return true;
                        case 137:
                            long _arg0113 = data.readLong();
                            String _arg167 = data.readString();
                            data.enforceNoDataAvail();
                            addOplusLoopLoadTime(_arg0113, _arg167);
                            reply.writeNoException();
                            return true;
                        case 138:
                            Bundle _arg0114 = (Bundle) data.readTypedObject(Bundle.CREATOR);
                            data.enforceNoDataAvail();
                            Bundle _result75 = autoLayoutCall(_arg0114);
                            reply.writeNoException();
                            reply.writeTypedObject(_result75, 1);
                            return true;
                        case 139:
                            IApplicationThread _arg0115 = IApplicationThread.Stub.asInterface(data.readStrongBinder());
                            String _arg168 = data.readString();
                            Uri _arg224 = (Uri) data.readTypedObject(Uri.CREATOR);
                            int _arg313 = data.readInt();
                            int _arg49 = data.readInt();
                            data.enforceNoDataAvail();
                            grantUriPermissionToUser(_arg0115, _arg168, _arg224, _arg313, _arg49);
                            reply.writeNoException();
                            return true;
                        case 140:
                            int _arg0116 = data.readInt();
                            boolean _arg169 = data.readBoolean();
                            data.enforceNoDataAvail();
                            trimSystemMemory(_arg0116, _arg169);
                            return true;
                        case 141:
                            int _arg0117 = data.readInt();
                            data.enforceNoDataAvail();
                            boolean _result76 = setPayJoyFlag(_arg0117);
                            reply.writeNoException();
                            reply.writeBoolean(_result76);
                            return true;
                        case 142:
                            int _result77 = getPayJoyFlag();
                            reply.writeNoException();
                            reply.writeInt(_result77);
                            return true;
                        default:
                            return super.onTransact(code, data, reply, flags);
                    }
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* loaded from: classes.dex */
        public static class Proxy implements IOplusActivityManager {
            private IBinder mRemote;

            Proxy(IBinder remote) {
                this.mRemote = remote;
            }

            @Override // android.os.IInterface
            public IBinder asBinder() {
                return this.mRemote;
            }

            public String getInterfaceDescriptor() {
                return IOplusActivityManager.DESCRIPTOR;
            }

            @Override // android.app.IOplusActivityManager
            public void unfreezeForKernel(int type, int callerPid, int targetUid, String rpcName, int code) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusActivityManager.DESCRIPTOR);
                    _data.writeInt(type);
                    _data.writeInt(callerPid);
                    _data.writeInt(targetUid);
                    _data.writeString(rpcName);
                    _data.writeInt(code);
                    this.mRemote.transact(1, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IOplusActivityManager
            public void unfreezeForKernelTargetPid(int type, int callerPid, int callerUid, int targetPid, int targetUid, String rpcName, int code) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusActivityManager.DESCRIPTOR);
                    _data.writeInt(type);
                    _data.writeInt(callerPid);
                    _data.writeInt(callerUid);
                    _data.writeInt(targetPid);
                    _data.writeInt(targetUid);
                    _data.writeString(rpcName);
                    _data.writeInt(code);
                    this.mRemote.transact(2, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IOplusActivityManager
            public void setHwuiTaskThreads(int pid, int tid) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(IOplusActivityManager.DESCRIPTOR);
                    _data.writeInt(pid);
                    _data.writeInt(tid);
                    this.mRemote.transact(3, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.app.IOplusActivityManager
            public void setGlThreads(int glID, int tid) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(IOplusActivityManager.DESCRIPTOR);
                    _data.writeInt(glID);
                    _data.writeInt(tid);
                    this.mRemote.transact(4, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.app.IOplusActivityManager
            public void onBackPressedOnTheiaMonitor(long pressNow) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(IOplusActivityManager.DESCRIPTOR);
                    _data.writeLong(pressNow);
                    this.mRemote.transact(5, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.app.IOplusActivityManager
            public void sendTheiaEvent(long category, Intent args) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(IOplusActivityManager.DESCRIPTOR);
                    _data.writeLong(category);
                    _data.writeTypedObject(args, 0);
                    this.mRemote.transact(6, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.app.IOplusActivityManager
            public void reportBindApplicationFinished(String pkgName, int userId, int pid) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(IOplusActivityManager.DESCRIPTOR);
                    _data.writeString(pkgName);
                    _data.writeInt(userId);
                    _data.writeInt(pid);
                    this.mRemote.transact(7, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.app.IOplusActivityManager
            public void favoriteQueryRule(String packageName, IOplusFavoriteQueryCallback callback) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(IOplusActivityManager.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeStrongInterface(callback);
                    this.mRemote.transact(8, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.app.IOplusActivityManager
            public OplusAccidentallyTouchData getAccidentallyTouchData() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusActivityManager.DESCRIPTOR);
                    this.mRemote.transact(9, _data, _reply, 0);
                    _reply.readException();
                    OplusAccidentallyTouchData _result = (OplusAccidentallyTouchData) _reply.readTypedObject(OplusAccidentallyTouchData.CREATOR);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IOplusActivityManager
            public OplusSecureKeyboardData getSecureKeyboardData() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusActivityManager.DESCRIPTOR);
                    this.mRemote.transact(10, _data, _reply, 0);
                    _reply.readException();
                    OplusSecureKeyboardData _result = (OplusSecureKeyboardData) _reply.readTypedObject(OplusSecureKeyboardData.CREATOR);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IOplusActivityManager
            public void updatePermissionChoice(String packageName, String permission, int choice) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusActivityManager.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeString(permission);
                    _data.writeInt(choice);
                    this.mRemote.transact(11, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IOplusActivityManager
            public void setPermissionInterceptEnable(boolean enabled) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusActivityManager.DESCRIPTOR);
                    _data.writeBoolean(enabled);
                    this.mRemote.transact(12, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IOplusActivityManager
            public boolean isPermissionInterceptEnabled() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusActivityManager.DESCRIPTOR);
                    this.mRemote.transact(13, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IOplusActivityManager
            public void grantOplusPermissionByGroup(String packageName, String permission) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusActivityManager.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeString(permission);
                    this.mRemote.transact(14, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IOplusActivityManager
            public void revokeOplusPermissionByGroup(String packageName, String permission) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusActivityManager.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeString(permission);
                    this.mRemote.transact(15, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IOplusActivityManager
            public void killPidForce(int pid) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusActivityManager.DESCRIPTOR);
                    _data.writeInt(pid);
                    this.mRemote.transact(16, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IOplusActivityManager
            public void handleAppForNotification(String pkgName, int uid, int otherInfo) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusActivityManager.DESCRIPTOR);
                    _data.writeString(pkgName);
                    _data.writeInt(uid);
                    _data.writeInt(otherInfo);
                    this.mRemote.transact(17, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IOplusActivityManager
            public void setGameSpaceController(IOplusGameSpaceController watcher) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusActivityManager.DESCRIPTOR);
                    _data.writeStrongInterface(watcher);
                    this.mRemote.transact(18, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IOplusActivityManager
            public List<String> getGlobalPkgWhiteList(int type) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusActivityManager.DESCRIPTOR);
                    _data.writeInt(type);
                    this.mRemote.transact(19, _data, _reply, 0);
                    _reply.readException();
                    List<String> _result = _reply.createStringArrayList();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IOplusActivityManager
            public List<String> getGlobalProcessWhiteList() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusActivityManager.DESCRIPTOR);
                    this.mRemote.transact(20, _data, _reply, 0);
                    _reply.readException();
                    List<String> _result = _reply.createStringArrayList();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IOplusActivityManager
            public void addStageProtectInfo(String callerPkg, String protectPkg, List<String> processList, String reason, long timeout, IOplusProtectConnection connection) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusActivityManager.DESCRIPTOR);
                    _data.writeString(callerPkg);
                    _data.writeString(protectPkg);
                    _data.writeStringList(processList);
                    _data.writeString(reason);
                    _data.writeLong(timeout);
                    _data.writeStrongInterface(connection);
                    this.mRemote.transact(21, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IOplusActivityManager
            public void removeStageProtectInfo(String protectPkg, String callerPkg) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusActivityManager.DESCRIPTOR);
                    _data.writeString(protectPkg);
                    _data.writeString(callerPkg);
                    this.mRemote.transact(22, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IOplusActivityManager
            public OplusDisplayOptimizationData getDisplayOptimizationData() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusActivityManager.DESCRIPTOR);
                    this.mRemote.transact(23, _data, _reply, 0);
                    _reply.readException();
                    OplusDisplayOptimizationData _result = (OplusDisplayOptimizationData) _reply.readTypedObject(OplusDisplayOptimizationData.CREATOR);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IOplusActivityManager
            public List<String> getStageProtectListFromPkg(String pkg, int type) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusActivityManager.DESCRIPTOR);
                    _data.writeString(pkg);
                    _data.writeInt(type);
                    this.mRemote.transact(24, _data, _reply, 0);
                    _reply.readException();
                    List<String> _result = _reply.createStringArrayList();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IOplusActivityManager
            public List<String> getStageProtectListFromPkgAsUser(String pkg, int type, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusActivityManager.DESCRIPTOR);
                    _data.writeString(pkg);
                    _data.writeInt(type);
                    _data.writeInt(userId);
                    this.mRemote.transact(25, _data, _reply, 0);
                    _reply.readException();
                    List<String> _result = _reply.createStringArrayList();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IOplusActivityManager
            public void handleAppFromControlCenter(String pkgName, int uid) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusActivityManager.DESCRIPTOR);
                    _data.writeString(pkgName);
                    _data.writeInt(uid);
                    this.mRemote.transact(26, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IOplusActivityManager
            public OplusDisplayCompatData getDisplayCompatData() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusActivityManager.DESCRIPTOR);
                    this.mRemote.transact(27, _data, _reply, 0);
                    _reply.readException();
                    OplusDisplayCompatData _result = (OplusDisplayCompatData) _reply.readTypedObject(OplusDisplayCompatData.CREATOR);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IOplusActivityManager
            public boolean getIsSupportMultiApp() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusActivityManager.DESCRIPTOR);
                    this.mRemote.transact(28, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IOplusActivityManager
            public List<String> getMultiAppList(int type) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusActivityManager.DESCRIPTOR);
                    _data.writeInt(type);
                    this.mRemote.transact(29, _data, _reply, 0);
                    _reply.readException();
                    List<String> _result = _reply.createStringArrayList();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IOplusActivityManager
            public String getMultiAppAlias(String pkgName) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusActivityManager.DESCRIPTOR);
                    _data.writeString(pkgName);
                    this.mRemote.transact(30, _data, _reply, 0);
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IOplusActivityManager
            public int setMultiAppConfig(OplusMultiAppConfig config) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusActivityManager.DESCRIPTOR);
                    _data.writeTypedObject(config, 0);
                    this.mRemote.transact(31, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IOplusActivityManager
            public OplusMultiAppConfig getMultiAppConfig() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusActivityManager.DESCRIPTOR);
                    this.mRemote.transact(32, _data, _reply, 0);
                    _reply.readException();
                    OplusMultiAppConfig _result = (OplusMultiAppConfig) _reply.readTypedObject(OplusMultiAppConfig.CREATOR);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IOplusActivityManager
            public int setMultiAppAlias(String pkgName, String alias) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusActivityManager.DESCRIPTOR);
                    _data.writeString(pkgName);
                    _data.writeString(alias);
                    this.mRemote.transact(33, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IOplusActivityManager
            public int getMultiAppAccessMode(String pkgName) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusActivityManager.DESCRIPTOR);
                    _data.writeString(pkgName);
                    this.mRemote.transact(34, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IOplusActivityManager
            public int setMultiAppAccessMode(String pkgName, int accessMode) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusActivityManager.DESCRIPTOR);
                    _data.writeString(pkgName);
                    _data.writeInt(accessMode);
                    this.mRemote.transact(35, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IOplusActivityManager
            public void getMultiAppAllAccessMode(Bundle outBundle) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusActivityManager.DESCRIPTOR);
                    this.mRemote.transact(36, _data, _reply, 0);
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        outBundle.readFromParcel(_reply);
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IOplusActivityManager
            public boolean isMultiApp(int userId, String pkgName) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusActivityManager.DESCRIPTOR);
                    _data.writeInt(userId);
                    _data.writeString(pkgName);
                    this.mRemote.transact(37, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IOplusActivityManager
            public int setMultiAppStatus(String pkgName, int status) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusActivityManager.DESCRIPTOR);
                    _data.writeString(pkgName);
                    _data.writeInt(status);
                    this.mRemote.transact(38, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IOplusActivityManager
            public int getMultiAppMaxCreateNum() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusActivityManager.DESCRIPTOR);
                    this.mRemote.transact(39, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IOplusActivityManager
            public void scanFileIfNeed(int userId, String path) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusActivityManager.DESCRIPTOR);
                    _data.writeInt(userId);
                    _data.writeString(path);
                    this.mRemote.transact(40, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IOplusActivityManager
            public void addMiniProgramShare(String shareAppPkgName, String miniProgramPkgName, String miniProgramSignature) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusActivityManager.DESCRIPTOR);
                    _data.writeString(shareAppPkgName);
                    _data.writeString(miniProgramPkgName);
                    _data.writeString(miniProgramSignature);
                    this.mRemote.transact(41, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IOplusActivityManager
            public void removeMiniProgramShare(String shareAppPkgName, String miniProgramPkgName, String miniProgramSignature) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusActivityManager.DESCRIPTOR);
                    _data.writeString(shareAppPkgName);
                    _data.writeString(miniProgramPkgName);
                    _data.writeString(miniProgramSignature);
                    this.mRemote.transact(42, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IOplusActivityManager
            public OplusResolveData getResolveData() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusActivityManager.DESCRIPTOR);
                    this.mRemote.transact(43, _data, _reply, 0);
                    _reply.readException();
                    OplusResolveData _result = (OplusResolveData) _reply.readTypedObject(OplusResolveData.CREATOR);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IOplusActivityManager
            public OplusReflectData getReflectData() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusActivityManager.DESCRIPTOR);
                    this.mRemote.transact(44, _data, _reply, 0);
                    _reply.readException();
                    OplusReflectData _result = (OplusReflectData) _reply.readTypedObject(OplusReflectData.CREATOR);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IOplusActivityManager
            public void addFastAppWCPay(String originAppCpn, String fastAppCpn) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusActivityManager.DESCRIPTOR);
                    _data.writeString(originAppCpn);
                    _data.writeString(fastAppCpn);
                    this.mRemote.transact(45, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IOplusActivityManager
            public void removeFastAppWCPay(String originAppCpn, String fastAppCpn) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusActivityManager.DESCRIPTOR);
                    _data.writeString(originAppCpn);
                    _data.writeString(fastAppCpn);
                    this.mRemote.transact(46, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IOplusActivityManager
            public List<OplusPackageFreezeData> getRunningProcesses() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusActivityManager.DESCRIPTOR);
                    this.mRemote.transact(47, _data, _reply, 0);
                    _reply.readException();
                    List<OplusPackageFreezeData> _result = _reply.createTypedArrayList(OplusPackageFreezeData.CREATOR);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IOplusActivityManager
            public void setAppStartMonitorController(IOplusAppStartController watcher) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusActivityManager.DESCRIPTOR);
                    _data.writeStrongInterface(watcher);
                    this.mRemote.transact(48, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IOplusActivityManager
            public void addFastAppThirdLogin(String callerPkg, String replacePkg) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusActivityManager.DESCRIPTOR);
                    _data.writeString(callerPkg);
                    _data.writeString(replacePkg);
                    this.mRemote.transact(49, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IOplusActivityManager
            public void removeFastAppThirdLogin(String callerPkg, String replacePkg) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusActivityManager.DESCRIPTOR);
                    _data.writeString(callerPkg);
                    _data.writeString(replacePkg);
                    this.mRemote.transact(50, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IOplusActivityManager
            public void addBackgroundRestrictedInfo(String callerPkg, List<String> targetPkgList) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusActivityManager.DESCRIPTOR);
                    _data.writeString(callerPkg);
                    _data.writeStringList(targetPkgList);
                    this.mRemote.transact(51, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IOplusActivityManager
            public void setPreventIndulgeController(IOplusAppStartController controller) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusActivityManager.DESCRIPTOR);
                    _data.writeStrongInterface(controller);
                    this.mRemote.transact(52, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IOplusActivityManager
            public void addPreventIndulgeList(List<String> pkgNames) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusActivityManager.DESCRIPTOR);
                    _data.writeStringList(pkgNames);
                    this.mRemote.transact(53, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IOplusActivityManager
            public boolean putConfigInfo(String configName, Bundle bundle, int flag, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusActivityManager.DESCRIPTOR);
                    _data.writeString(configName);
                    _data.writeTypedObject(bundle, 0);
                    _data.writeInt(flag);
                    _data.writeInt(userId);
                    this.mRemote.transact(54, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IOplusActivityManager
            public Bundle getConfigInfo(String configName, int flag, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusActivityManager.DESCRIPTOR);
                    _data.writeString(configName);
                    _data.writeInt(flag);
                    _data.writeInt(userId);
                    this.mRemote.transact(55, _data, _reply, 0);
                    _reply.readException();
                    Bundle _result = (Bundle) _reply.readTypedObject(Bundle.CREATOR);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IOplusActivityManager
            public float updateCpuTracker(long lastUpdateTime) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusActivityManager.DESCRIPTOR);
                    _data.writeLong(lastUpdateTime);
                    this.mRemote.transact(56, _data, _reply, 0);
                    _reply.readException();
                    float _result = _reply.readFloat();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IOplusActivityManager
            public List<String> getCpuWorkingStats() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusActivityManager.DESCRIPTOR);
                    this.mRemote.transact(57, _data, _reply, 0);
                    _reply.readException();
                    List<String> _result = _reply.createStringArrayList();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IOplusActivityManager
            public void forceTrimAppMemory(int level) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusActivityManager.DESCRIPTOR);
                    _data.writeInt(level);
                    this.mRemote.transact(58, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IOplusActivityManager
            public void setPermissionRecordController(IOplusPermissionRecordController watcher) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusActivityManager.DESCRIPTOR);
                    _data.writeStrongInterface(watcher);
                    this.mRemote.transact(59, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IOplusActivityManager
            public OplusDarkModeData getDarkModeData(String packageName) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusActivityManager.DESCRIPTOR);
                    _data.writeString(packageName);
                    this.mRemote.transact(60, _data, _reply, 0);
                    _reply.readException();
                    OplusDarkModeData _result = (OplusDarkModeData) _reply.readTypedObject(OplusDarkModeData.CREATOR);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IOplusActivityManager
            public boolean isNightMode() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusActivityManager.DESCRIPTOR);
                    this.mRemote.transact(61, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IOplusActivityManager
            public boolean dumpProcPerfData(Bundle bundle) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusActivityManager.DESCRIPTOR);
                    _data.writeTypedObject(bundle, 0);
                    this.mRemote.transact(62, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IOplusActivityManager
            public List<String> getProcCommonInfoList(int type) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusActivityManager.DESCRIPTOR);
                    _data.writeInt(type);
                    this.mRemote.transact(63, _data, _reply, 0);
                    _reply.readException();
                    List<String> _result = _reply.createStringArrayList();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IOplusActivityManager
            public List<OplusProcDependData> getProcDependency(int pid) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusActivityManager.DESCRIPTOR);
                    _data.writeInt(pid);
                    this.mRemote.transact(64, _data, _reply, 0);
                    _reply.readException();
                    List<OplusProcDependData> _result = _reply.createTypedArrayList(OplusProcDependData.CREATOR);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IOplusActivityManager
            public List<OplusProcDependData> getProcDependencyByUserId(String packageName, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusActivityManager.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeInt(userId);
                    this.mRemote.transact(65, _data, _reply, 0);
                    _reply.readException();
                    List<OplusProcDependData> _result = _reply.createTypedArrayList(OplusProcDependData.CREATOR);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IOplusActivityManager
            public List<String> getTaskPkgList(int taskId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusActivityManager.DESCRIPTOR);
                    _data.writeInt(taskId);
                    this.mRemote.transact(66, _data, _reply, 0);
                    _reply.readException();
                    List<String> _result = _reply.createStringArrayList();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IOplusActivityManager
            public void syncPermissionRecord() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusActivityManager.DESCRIPTOR);
                    this.mRemote.transact(67, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IOplusActivityManager
            public void updateUidCpuTracker() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusActivityManager.DESCRIPTOR);
                    this.mRemote.transact(68, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IOplusActivityManager
            public List<String> getUidCpuWorkingStats() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusActivityManager.DESCRIPTOR);
                    this.mRemote.transact(69, _data, _reply, 0);
                    _reply.readException();
                    List<String> _result = _reply.createStringArrayList();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IOplusActivityManager
            public long getTotalCpuLoadPercent() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusActivityManager.DESCRIPTOR);
                    this.mRemote.transact(70, _data, _reply, 0);
                    _reply.readException();
                    long _result = _reply.readLong();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IOplusActivityManager
            public List<String> getTopLoadPidsInfos(int num) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusActivityManager.DESCRIPTOR);
                    _data.writeInt(num);
                    this.mRemote.transact(71, _data, _reply, 0);
                    _reply.readException();
                    List<String> _result = _reply.createStringArrayList();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IOplusActivityManager
            public SharedMemory getCpuLimitLatestLogs(String pkgName) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusActivityManager.DESCRIPTOR);
                    _data.writeString(pkgName);
                    this.mRemote.transact(72, _data, _reply, 0);
                    _reply.readException();
                    SharedMemory _result = (SharedMemory) _reply.readTypedObject(SharedMemory.CREATOR);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IOplusActivityManager
            public List<String> getProcCmdline(int[] pids) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusActivityManager.DESCRIPTOR);
                    _data.writeIntArray(pids);
                    this.mRemote.transact(73, _data, _reply, 0);
                    _reply.readException();
                    List<String> _result = _reply.createStringArrayList();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IOplusActivityManager
            public void activeGc(int[] pids) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(IOplusActivityManager.DESCRIPTOR);
                    _data.writeIntArray(pids);
                    this.mRemote.transact(74, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.app.IOplusActivityManager
            public void finishNotOrderReceiver(IBinder who, int hasCode, int resultCode, String resultData, Bundle resultExtras, boolean resultAbort) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusActivityManager.DESCRIPTOR);
                    _data.writeStrongBinder(who);
                    _data.writeInt(hasCode);
                    _data.writeInt(resultCode);
                    _data.writeString(resultData);
                    _data.writeTypedObject(resultExtras, 0);
                    _data.writeBoolean(resultAbort);
                    this.mRemote.transact(75, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IOplusActivityManager
            public void reportSkippedFrames(long currentTime, boolean isAnimation, boolean isForeground, long skippedFrames) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusActivityManager.DESCRIPTOR);
                    _data.writeLong(currentTime);
                    _data.writeBoolean(isAnimation);
                    _data.writeBoolean(isForeground);
                    _data.writeLong(skippedFrames);
                    this.mRemote.transact(76, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IOplusActivityManager
            public void reportSkippedFramesProcName(long currentTime, boolean isAnimation, boolean isForeground, long skippedFrames, String pckName) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusActivityManager.DESCRIPTOR);
                    _data.writeLong(currentTime);
                    _data.writeBoolean(isAnimation);
                    _data.writeBoolean(isForeground);
                    _data.writeLong(skippedFrames);
                    _data.writeString(pckName);
                    this.mRemote.transact(77, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IOplusActivityManager
            public void reportSkippedFramesActivityName(long currentTime, boolean isAnimation, boolean isForeground, long skippedFrames, String pckName, String activityName) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusActivityManager.DESCRIPTOR);
                    _data.writeLong(currentTime);
                    _data.writeBoolean(isAnimation);
                    _data.writeBoolean(isForeground);
                    _data.writeLong(skippedFrames);
                    _data.writeString(pckName);
                    _data.writeString(activityName);
                    this.mRemote.transact(78, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IOplusActivityManager
            public String queryProcessNameFromPid(int pid) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusActivityManager.DESCRIPTOR);
                    _data.writeInt(pid);
                    this.mRemote.transact(79, _data, _reply, 0);
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IOplusActivityManager
            public void notifyAppKillReason(int pid, int uid, int reason, int subReason, String msg) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(IOplusActivityManager.DESCRIPTOR);
                    _data.writeInt(pid);
                    _data.writeInt(uid);
                    _data.writeInt(reason);
                    _data.writeInt(subReason);
                    _data.writeString(msg);
                    this.mRemote.transact(80, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.app.IOplusActivityManager
            public int getFontVariationAdaptionData(String packagename) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusActivityManager.DESCRIPTOR);
                    _data.writeString(packagename);
                    this.mRemote.transact(81, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IOplusActivityManager
            public boolean registerHansListener(String callerPkg, IOplusHansListener listener) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusActivityManager.DESCRIPTOR);
                    _data.writeString(callerPkg);
                    _data.writeStrongInterface(listener);
                    this.mRemote.transact(82, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IOplusActivityManager
            public boolean unregisterHansListener(String callerPkg, IOplusHansListener listener) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusActivityManager.DESCRIPTOR);
                    _data.writeString(callerPkg);
                    _data.writeStrongInterface(listener);
                    this.mRemote.transact(83, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IOplusActivityManager
            public boolean setAppFreeze(String callerPkg, Bundle bundle) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusActivityManager.DESCRIPTOR);
                    _data.writeString(callerPkg);
                    _data.writeTypedObject(bundle, 0);
                    this.mRemote.transact(84, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IOplusActivityManager
            public void notifyAthenaOnekeyClearRunning(int state) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(IOplusActivityManager.DESCRIPTOR);
                    _data.writeInt(state);
                    this.mRemote.transact(85, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.app.IOplusActivityManager
            public void executeResPreload(String pkgName, int userId, int preloadType, String preloadReason) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(IOplusActivityManager.DESCRIPTOR);
                    _data.writeString(pkgName);
                    _data.writeInt(userId);
                    _data.writeInt(preloadType);
                    _data.writeString(preloadReason);
                    this.mRemote.transact(86, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.app.IOplusActivityManager
            public Bundle getResPreloadInfo(int days, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusActivityManager.DESCRIPTOR);
                    _data.writeInt(days);
                    _data.writeInt(userId);
                    this.mRemote.transact(87, _data, _reply, 0);
                    _reply.readException();
                    Bundle _result = (Bundle) _reply.readTypedObject(Bundle.CREATOR);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IOplusActivityManager
            public long getPreloadIOSize() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusActivityManager.DESCRIPTOR);
                    this.mRemote.transact(88, _data, _reply, 0);
                    _reply.readException();
                    long _result = _reply.readLong();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IOplusActivityManager
            public boolean getPreloadStatus(String pkgName, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusActivityManager.DESCRIPTOR);
                    _data.writeString(pkgName);
                    _data.writeInt(userId);
                    this.mRemote.transact(89, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IOplusActivityManager
            public Bundle getPreloadPkgList() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusActivityManager.DESCRIPTOR);
                    this.mRemote.transact(90, _data, _reply, 0);
                    _reply.readException();
                    Bundle _result = (Bundle) _reply.readTypedObject(Bundle.CREATOR);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IOplusActivityManager
            public List<String> getPkgPreloadFiles(String pkgName) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusActivityManager.DESCRIPTOR);
                    _data.writeString(pkgName);
                    this.mRemote.transact(91, _data, _reply, 0);
                    _reply.readException();
                    List<String> _result = _reply.createStringArrayList();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IOplusActivityManager
            public void enterFastFreezer(String callerPkg, int[] uids, long timeout, String reason) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusActivityManager.DESCRIPTOR);
                    _data.writeString(callerPkg);
                    _data.writeIntArray(uids);
                    _data.writeLong(timeout);
                    _data.writeString(reason);
                    this.mRemote.transact(92, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IOplusActivityManager
            public void exitFastFreezer(String callerPkg, String reason) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusActivityManager.DESCRIPTOR);
                    _data.writeString(callerPkg);
                    _data.writeString(reason);
                    this.mRemote.transact(93, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IOplusActivityManager
            public boolean isFrozenByHans(String packageName, int uid) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusActivityManager.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeInt(uid);
                    this.mRemote.transact(94, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IOplusActivityManager
            public String getTrafficBytesList(Bundle uids, Bundle outBundle) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusActivityManager.DESCRIPTOR);
                    _data.writeTypedObject(uids, 0);
                    this.mRemote.transact(95, _data, _reply, 0);
                    _reply.readException();
                    String _result = _reply.readString();
                    if (_reply.readInt() != 0) {
                        outBundle.readFromParcel(_reply);
                    }
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IOplusActivityManager
            public String getTrafficPacketList(Bundle uids, Bundle outBundle) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusActivityManager.DESCRIPTOR);
                    _data.writeTypedObject(uids, 0);
                    this.mRemote.transact(96, _data, _reply, 0);
                    _reply.readException();
                    String _result = _reply.readString();
                    if (_reply.readInt() != 0) {
                        outBundle.readFromParcel(_reply);
                    }
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IOplusActivityManager
            public void registerEapDataCallback(IOplusEapDataCallback callback) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusActivityManager.DESCRIPTOR);
                    _data.writeStrongInterface(callback);
                    this.mRemote.transact(97, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IOplusActivityManager
            public void unregisterEapDataCallback(IOplusEapDataCallback callback) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusActivityManager.DESCRIPTOR);
                    _data.writeStrongInterface(callback);
                    this.mRemote.transact(98, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IOplusActivityManager
            public void updateANRDumpState(SharedMemory sharedMemory) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusActivityManager.DESCRIPTOR);
                    _data.writeTypedObject(sharedMemory, 0);
                    this.mRemote.transact(99, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IOplusActivityManager
            public void registerAbConfigCallback(IEventCallback callback, String packageName) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusActivityManager.DESCRIPTOR);
                    _data.writeStrongInterface(callback);
                    _data.writeString(packageName);
                    this.mRemote.transact(100, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IOplusActivityManager
            public void unregisterAbConfigCallback(IEventCallback callback) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusActivityManager.DESCRIPTOR);
                    _data.writeStrongInterface(callback);
                    this.mRemote.transact(101, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IOplusActivityManager
            public void registerErrorInfoCallback(IEventCallback callback) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusActivityManager.DESCRIPTOR);
                    _data.writeStrongInterface(callback);
                    this.mRemote.transact(102, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IOplusActivityManager
            public void unregisterErrorInfoCallback(IEventCallback callback) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusActivityManager.DESCRIPTOR);
                    _data.writeStrongInterface(callback);
                    this.mRemote.transact(103, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IOplusActivityManager
            public List<ActivityManager.RecentTaskInfo> getAllVisibleTasksInfo(int userId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusActivityManager.DESCRIPTOR);
                    _data.writeInt(userId);
                    this.mRemote.transact(104, _data, _reply, 0);
                    _reply.readException();
                    List<ActivityManager.RecentTaskInfo> _result = _reply.createTypedArrayList(ActivityManager.RecentTaskInfo.CREATOR);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IOplusActivityManager
            public void anrViaTheiaEvent(int pid, String reason) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusActivityManager.DESCRIPTOR);
                    _data.writeInt(pid);
                    _data.writeString(reason);
                    this.mRemote.transact(105, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IOplusActivityManager
            public List<String> getStageProtectList(int type) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusActivityManager.DESCRIPTOR);
                    _data.writeInt(type);
                    this.mRemote.transact(106, _data, _reply, 0);
                    _reply.readException();
                    List<String> _result = _reply.createStringArrayList();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IOplusActivityManager
            public List<String> getStageProtectListAsUser(int type, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusActivityManager.DESCRIPTOR);
                    _data.writeInt(type);
                    _data.writeInt(userId);
                    this.mRemote.transact(107, _data, _reply, 0);
                    _reply.readException();
                    List<String> _result = _reply.createStringArrayList();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IOplusActivityManager
            public OplusUXIconData getUXIconData() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusActivityManager.DESCRIPTOR);
                    this.mRemote.transact(108, _data, _reply, 0);
                    _reply.readException();
                    OplusUXIconData _result = (OplusUXIconData) _reply.readTypedObject(OplusUXIconData.CREATOR);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IOplusActivityManager
            public void compactProcess(Bundle pids, int compactionFlags, int advice) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusActivityManager.DESCRIPTOR);
                    _data.writeTypedObject(pids, 0);
                    _data.writeInt(compactionFlags);
                    _data.writeInt(advice);
                    this.mRemote.transact(109, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IOplusActivityManager
            public boolean inDownloading(int uid, int thresholdSpeed, boolean rough) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusActivityManager.DESCRIPTOR);
                    _data.writeInt(uid);
                    _data.writeInt(thresholdSpeed);
                    _data.writeBoolean(rough);
                    this.mRemote.transact(110, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IOplusActivityManager
            public List<OplusPackageFreezeData> getDownloadingList(int thresholdSpeed, boolean rough) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusActivityManager.DESCRIPTOR);
                    _data.writeInt(thresholdSpeed);
                    _data.writeBoolean(rough);
                    this.mRemote.transact(111, _data, _reply, 0);
                    _reply.readException();
                    List<OplusPackageFreezeData> _result = _reply.createTypedArrayList(OplusPackageFreezeData.CREATOR);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IOplusActivityManager
            public List<UsageStats> queryUsageStats(int intervalType, long beginTime, long endTime) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusActivityManager.DESCRIPTOR);
                    _data.writeInt(intervalType);
                    _data.writeLong(beginTime);
                    _data.writeLong(endTime);
                    this.mRemote.transact(112, _data, _reply, 0);
                    _reply.readException();
                    List<UsageStats> _result = _reply.createTypedArrayList(UsageStats.CREATOR);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IOplusActivityManager
            public List<ActivityManager.RunningAppProcessInfo> getRunningAppProcessInfos(Bundle pids) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusActivityManager.DESCRIPTOR);
                    _data.writeTypedObject(pids, 0);
                    this.mRemote.transact(113, _data, _reply, 0);
                    _reply.readException();
                    List<ActivityManager.RunningAppProcessInfo> _result = _reply.createTypedArrayList(ActivityManager.RunningAppProcessInfo.CREATOR);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IOplusActivityManager
            public List<OplusPackageFreezeData> getPackageFreezeDataInfos(Bundle pids) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusActivityManager.DESCRIPTOR);
                    _data.writeTypedObject(pids, 0);
                    this.mRemote.transact(114, _data, _reply, 0);
                    _reply.readException();
                    List<OplusPackageFreezeData> _result = _reply.createTypedArrayList(OplusPackageFreezeData.CREATOR);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IOplusActivityManager
            public void notifyUiSwitched(String uiInfo, int status) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(IOplusActivityManager.DESCRIPTOR);
                    _data.writeString(uiInfo);
                    _data.writeInt(status);
                    this.mRemote.transact(115, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.app.IOplusActivityManager
            public boolean addOrRemoveOplusVerifyCodeListener(boolean add, IOplusVerifyCodeListener listener) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusActivityManager.DESCRIPTOR);
                    _data.writeBoolean(add);
                    _data.writeStrongInterface(listener);
                    this.mRemote.transact(116, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IOplusActivityManager
            public void cleanPackageResources(String packageName, int uid) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusActivityManager.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeInt(uid);
                    this.mRemote.transact(117, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IOplusActivityManager
            public void forceStopPackageAndSaveActivity(String packageName, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusActivityManager.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeInt(userId);
                    this.mRemote.transact(118, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IOplusActivityManager
            public int[] getRunningPidsByUid(int uid) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusActivityManager.DESCRIPTOR);
                    _data.writeInt(uid);
                    this.mRemote.transact(119, _data, _reply, 0);
                    _reply.readException();
                    int[] _result = _reply.createIntArray();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IOplusActivityManager
            public void asyncReportFrames(String pkgName, int skippedFrames) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(IOplusActivityManager.DESCRIPTOR);
                    _data.writeString(pkgName);
                    _data.writeInt(skippedFrames);
                    this.mRemote.transact(120, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.app.IOplusActivityManager
            public boolean requestDeviceFolded(int folded, boolean enableSecDisplay) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusActivityManager.DESCRIPTOR);
                    _data.writeInt(folded);
                    _data.writeBoolean(enableSecDisplay);
                    this.mRemote.transact(121, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IOplusActivityManager
            public void sendFlingTransit(MotionEvent ev, int duration) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(IOplusActivityManager.DESCRIPTOR);
                    _data.writeTypedObject(ev, 0);
                    _data.writeInt(duration);
                    this.mRemote.transact(122, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.app.IOplusActivityManager
            public void setSceneActionTransit(String scene, String action, int timeout) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(IOplusActivityManager.DESCRIPTOR);
                    _data.writeString(scene);
                    _data.writeString(action);
                    _data.writeInt(timeout);
                    this.mRemote.transact(123, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.app.IOplusActivityManager
            public IOplusCompatModeSession getCompatModeSession() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusActivityManager.DESCRIPTOR);
                    this.mRemote.transact(124, _data, _reply, 0);
                    _reply.readException();
                    IOplusCompatModeSession _result = IOplusCompatModeSession.Stub.asInterface(_reply.readStrongBinder());
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IOplusActivityManager
            public boolean registerTerminateObserver(IProcessTerminateObserver observer) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusActivityManager.DESCRIPTOR);
                    _data.writeStrongInterface(observer);
                    this.mRemote.transact(125, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IOplusActivityManager
            public boolean unregisterTerminateObserver(IProcessTerminateObserver observer) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusActivityManager.DESCRIPTOR);
                    _data.writeStrongInterface(observer);
                    this.mRemote.transact(126, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IOplusActivityManager
            public int[] getTerminateObservers() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusActivityManager.DESCRIPTOR);
                    this.mRemote.transact(127, _data, _reply, 0);
                    _reply.readException();
                    int[] _result = _reply.createIntArray();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IOplusActivityManager
            public void notifyProcessTerminate(int[] pids, String reason) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusActivityManager.DESCRIPTOR);
                    _data.writeIntArray(pids);
                    _data.writeString(reason);
                    this.mRemote.transact(128, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IOplusActivityManager
            public void notifyProcessTerminateFinish(IProcessTerminateObserver observer) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusActivityManager.DESCRIPTOR);
                    _data.writeStrongInterface(observer);
                    this.mRemote.transact(129, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IOplusActivityManager
            public boolean registerTerminateStateObserver(ITerminateObserver observer) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusActivityManager.DESCRIPTOR);
                    _data.writeStrongInterface(observer);
                    this.mRemote.transact(130, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IOplusActivityManager
            public boolean unregisterTerminateStateObserver(ITerminateObserver observer) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusActivityManager.DESCRIPTOR);
                    _data.writeStrongInterface(observer);
                    this.mRemote.transact(131, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IOplusActivityManager
            public void asyncReportDalvikMem(Bundle bundle, int pid, long dalvikMax, long dalvikUsed, long uptime) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(IOplusActivityManager.DESCRIPTOR);
                    _data.writeTypedObject(bundle, 0);
                    _data.writeInt(pid);
                    _data.writeLong(dalvikMax);
                    _data.writeLong(dalvikUsed);
                    _data.writeLong(uptime);
                    this.mRemote.transact(132, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.app.IOplusActivityManager
            public boolean registerComplexSceneObserver(Bundle bundle, IComplexSceneObserver observer) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusActivityManager.DESCRIPTOR);
                    _data.writeTypedObject(bundle, 0);
                    _data.writeStrongInterface(observer);
                    this.mRemote.transact(133, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IOplusActivityManager
            public boolean unregisterComplexSceneObserver(Bundle bundle, IComplexSceneObserver observer) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusActivityManager.DESCRIPTOR);
                    _data.writeTypedObject(bundle, 0);
                    _data.writeStrongInterface(observer);
                    this.mRemote.transact(134, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IOplusActivityManager
            public String getFastAppReplacePkg(String callerPkg) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusActivityManager.DESCRIPTOR);
                    _data.writeString(callerPkg);
                    this.mRemote.transact(135, _data, _reply, 0);
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IOplusActivityManager
            public int getLoopCpuLoad() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusActivityManager.DESCRIPTOR);
                    this.mRemote.transact(136, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IOplusActivityManager
            public void addOplusLoopLoadTime(long timeEnd, String msg) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusActivityManager.DESCRIPTOR);
                    _data.writeLong(timeEnd);
                    _data.writeString(msg);
                    this.mRemote.transact(137, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IOplusActivityManager
            public Bundle autoLayoutCall(Bundle inBundle) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusActivityManager.DESCRIPTOR);
                    _data.writeTypedObject(inBundle, 0);
                    this.mRemote.transact(138, _data, _reply, 0);
                    _reply.readException();
                    Bundle _result = (Bundle) _reply.readTypedObject(Bundle.CREATOR);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IOplusActivityManager
            public void grantUriPermissionToUser(IApplicationThread caller, String targetPkg, Uri uri, int modeFlags, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusActivityManager.DESCRIPTOR);
                    _data.writeStrongInterface(caller);
                    _data.writeString(targetPkg);
                    _data.writeTypedObject(uri, 0);
                    _data.writeInt(modeFlags);
                    _data.writeInt(userId);
                    this.mRemote.transact(139, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IOplusActivityManager
            public void trimSystemMemory(int level, boolean needGc) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(IOplusActivityManager.DESCRIPTOR);
                    _data.writeInt(level);
                    _data.writeBoolean(needGc);
                    this.mRemote.transact(140, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.app.IOplusActivityManager
            public boolean setPayJoyFlag(int payjoyFlag) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusActivityManager.DESCRIPTOR);
                    _data.writeInt(payjoyFlag);
                    this.mRemote.transact(141, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IOplusActivityManager
            public int getPayJoyFlag() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusActivityManager.DESCRIPTOR);
                    this.mRemote.transact(142, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
        }

        public int getMaxTransactionId() {
            return 141;
        }
    }
}
