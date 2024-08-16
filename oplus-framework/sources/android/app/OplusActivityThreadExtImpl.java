package android.app;

import android.app.ActivityThread;
import android.batterySipper.OplusBaseBatterySipper;
import android.common.OplusFeatureCache;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.IPackageManager;
import android.content.pm.OplusPackageManager;
import android.content.res.CompatibilityInfo;
import android.content.res.Configuration;
import android.content.res.IOplusThemeManager;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.hardware.SystemSensorManager;
import android.hardware.SystemSensorManagerExtImpl;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Parcel;
import android.os.Process;
import android.os.RemoteException;
import android.os.StrictMode;
import android.os.SystemClock;
import android.os.SystemProperties;
import android.os.Trace;
import android.text.TextUtils;
import android.util.Log;
import android.util.Printer;
import android.util.Slog;
import android.view.Choreographer;
import android.view.DisplayInfo;
import android.view.IOplusScrollToTopManager;
import android.view.OplusFlexibleViewManager;
import android.view.OplusTaskBarUtils;
import android.view.OplusViewMirrorManager;
import android.view.WindowManagerGlobal;
import android.view.autolayout.IOplusAutoLayoutManager;
import android.view.debug.IOplusViewDebugManager;
import android.view.rgbnormalize.IOplusRGBNormalizeManager;
import android.view.viewextract.IOplusViewExtractManager;
import com.google.android.collect.Sets;
import com.oplus.compactwindow.OplusCompactWindowManager;
import com.oplus.darkmode.IOplusDarkModeManager;
import com.oplus.flexiblewindow.FlexibleWindowManager;
import com.oplus.screenmode.IOplusAutoResolutionFeature;
import com.oplus.scrolloptim.ScrOptController;
import com.oplus.splitscreen.OplusSplitScreenManager;
import com.oplus.uamodel.OplusModelUtil;
import com.oplus.view.OplusWindowUtils;
import com.oplus.zoomwindow.OplusZoomWindowManager;
import java.lang.reflect.Method;
import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

/* loaded from: classes.dex */
public class OplusActivityThreadExtImpl implements IActivityThreadExt {
    private static final short CONFIG_INDEX_FGC_MIN_MATCH_CNT = 4;
    private static final short CONFIG_INDEX_HIGH_CHECK_OR_SYNC_INV = 3;
    private static final short CONFIG_INDEX_HIGH_SYNC_PERCENT = 1;
    private static final short CONFIG_INDEX_LOW_CHECK_OR_SYNC_INV = 2;
    private static final short CONFIG_INDEX_LOW_SYNC_PERCENT = 0;
    private static final short CONFIG_INDEX_MAX = 5;
    private static final short DALVIK_MONITOR_BASE_VALUE = 100;
    private static final String DALVIK_SYNC_CONFIG_SEPARATOR = ",";
    private static final String DALVIK_SYNC_FEATURE_OFF = "disable";
    public static final String KEY_DISPLAY = "display";
    private static final String LAUNCHER_PACKAGE_NAME = "com.android.launcher";
    private static final HashMap<String, Set<String>> LOCAL_START_TOKENS;
    public static final int MIRAGE_ID_BASE = 10000;
    private static final int OSENSE_TIMEOUT = 600;
    private static final Set<String> PACKAGE_SYSTEM_IGNORE_UPDATE_DISPLAY = Sets.newHashSet(new String[]{"com.android.systemui", OplusWindowUtils.PACKAGE_INCALL});
    private static final Set<String> PACKAGE_ZOOM_IGNORE_DISPATCH_CONFIGURATIONCHANGED = Sets.newHashSet(new String[]{"com.truecaller", "com.htmedia.mint"});
    private static final String PROP_DALVIK_SYNC_CONFIG = "sys.oplus.dalvik_sync_config";
    private static final String TENCENT_MM_PACKAGE_NAME = "com.tencent.mm";
    private static final int TYPE_ENLARGE_HEAP_APPS = 4;
    public static boolean sDoFrameOptEnabled = false;
    private static boolean sIs64Bit = false;
    private static int sPid = 0;
    private static final String sSYSTEM_UI_PKG = "com.android.systemui";
    private static int sUid;
    private static long sVersionCode;
    private ActivityThread mBase;
    private boolean mShouldCallActivityConfigChange = true;
    private boolean mShouldReportExtraConfigChange = false;
    private long mLaunchActivityStartTime = 0;
    private long mResumeActivityStartTime = 0;
    private long mPauseActivityStartTime = 0;
    private boolean mOnCreateState = false;
    private boolean mIsTopApp = false;
    private boolean mExecuteTransaction = false;
    private boolean mFreezeEnable = SystemProperties.getBoolean("persist.sys.hans.skipframe.enable", false);
    private long mSkipFramesCnt = SystemProperties.getInt("persist.sys.hans.skipframe.count", 30);
    private long mFreezeDuration = SystemProperties.getLong("persist.sys.hans.skipframe.f_duration", 30000);
    private long mLastFreezeTime = 0;
    private volatile String mCurrActivityName = null;
    private Boolean mIsZoomSupportMultiWindow = null;
    private long mNextExpectedTime = SystemClock.uptimeMillis();
    private Configuration mConfiguration = null;
    private ResourcesManager mResourcesManager = null;
    private int mDisplayId = -1;
    private Configuration mFakeDisplayConfiguration = new Configuration();
    private boolean mDalvikSyncFeatureOn = false;
    private int mLastDalvikSyncConfigHashCode = 0;
    private int mLowSyncPercent = 0;
    private int mHighSyncPercent = 0;
    private int mLowCheckOrSyncInv = 0;
    private int mHighCheckOrSyncInv = 0;
    private int mFrequentGcMinMatchCount = 0;
    private boolean mHasReportedWarningDalvik = false;
    private long mLastReportWarningDalvikTime = 0;
    private long mLastReportFrequentGcTime = 0;
    private long mDalvikMax = 0;
    private long mAppCreatedUptime = 0;
    private ArrayDeque<Long> mGcRunTimeQueue = null;

    static {
        HashMap<String, Set<String>> hashMap = new HashMap<>();
        LOCAL_START_TOKENS = hashMap;
        sDoFrameOptEnabled = false;
        sUid = -1;
        sPid = -1;
        sIs64Bit = false;
        hashMap.put("com.tencent.mobileqq", Sets.newHashSet(new String[]{"qcircle-app"}));
        hashMap.put("com.sina.weibo", Sets.newHashSet(new String[]{"mblog_tab"}));
    }

    public OplusActivityThreadExtImpl(Object base) {
        this.mBase = (ActivityThread) base;
    }

    private void initVersionInfo(ApplicationInfo ai) {
        sVersionCode = ai.longVersionCode;
        sUid = Process.myUid();
        sPid = Process.myPid();
        sIs64Bit = Process.is64Bit();
        this.mAppCreatedUptime = ai.createTimestamp;
        Runtime runtime = Runtime.getRuntime();
        this.mDalvikMax = runtime.maxMemory();
    }

    public static long getPackageVersionCode() {
        return sVersionCode;
    }

    public static int getUid() {
        return sUid;
    }

    public static int getPid() {
        return sPid;
    }

    public static boolean is64Bit() {
        return sIs64Bit;
    }

    public static int changeConfigDiff(int diff) {
        int result = diff & (-16385);
        return result & (-9);
    }

    public void shouldInterceptConfigRelaunch(int diff, Configuration configuration) {
        if (((IOplusDarkModeManager) OplusFeatureCache.getOrCreate(IOplusDarkModeManager.DEFAULT, new Object[0])).shouldInterceptConfigRelaunch(diff, configuration)) {
            this.mShouldCallActivityConfigChange = true;
        }
    }

    public boolean shouldReportExtraConfig(ActivityInfo activityInfo, IPackageManager pm, Configuration currentConfig, Configuration newConfig, boolean shouldReportChange) {
        int realChanges = activityInfo.getRealConfigChanged();
        int extraDiff = currentConfig.diffPublicOnly(newConfig);
        if (shouldReportChange) {
            shouldInterceptConfigRelaunch(extraDiff, currentConfig);
        }
        if (!shouldReportChange && (((IOplusThemeManager) OplusFeatureCache.getOrCreate(IOplusThemeManager.DEFAULT, new Object[0])).shouldReportExtraConfig(extraDiff, realChanges) || ((IOplusThemeManager) OplusFeatureCache.getOrCreate(IOplusThemeManager.DEFAULT, new Object[0])).interceptOplusConfigChange(activityInfo, pm, newConfig, extraDiff) || (524288 & extraDiff) != 0)) {
            this.mShouldReportExtraConfigChange = true;
        }
        return false;
    }

    public void changeToSpecialModel(String packageName) {
        OplusModelUtil oplusModel = new OplusModelUtil();
        if (oplusModel.setModelOk(packageName)) {
            oplusModel.changeToSpecialModel();
        }
    }

    public boolean checkOplusExSystemService(boolean systemThread, String className) {
        return OplusExSystemServiceHelper.getInstance().checkOplusExSystemService(systemThread, className);
    }

    public boolean checkOplusExSystemService(boolean systemThread, Intent intent) {
        return OplusExSystemServiceHelper.getInstance().checkOplusExSystemService(systemThread, intent);
    }

    public boolean getShouldCallActivityConfigChangeState(boolean shouldReportChange) {
        boolean configChange = shouldReportChange || this.mShouldReportExtraConfigChange;
        this.mShouldReportExtraConfigChange = false;
        return configChange;
    }

    public void enableDoFrameOpt(boolean enable) {
        sDoFrameOptEnabled = enable;
    }

    public boolean shouldConfigChangeByMultiSystem(boolean change, int realConfigChange, int diff, String pkgName) {
        if (!change && (realConfigChange & diff) != 0 && "com.android.launcher".equals(pkgName)) {
            Slog.d("ActivityThread", "performActivityConfigurationChanged no relaunch need reportChange");
            this.mShouldReportExtraConfigChange = true;
            return true;
        }
        return false;
    }

    public boolean isWindowModeChanged(int currentMode, int newMode, String packageName) {
        List zoomPackageList = Arrays.asList(this.mBase.getSystemContext().getResources().getStringArray(201785416));
        if (zoomPackageList != null && zoomPackageList.contains(packageName)) {
            Slog.d("ActivityThread", "Avoid updating and notifying config for these zoom window packages ");
            return false;
        }
        if (currentMode != 100 || newMode == 100) {
            return false;
        }
        this.mShouldReportExtraConfigChange = true;
        return true;
    }

    public void handleLaunchActivity() {
        this.mLaunchActivityStartTime = System.nanoTime();
        try {
            OplusActivityManager.getInstance().setSceneActionTransit("", "OSENSE_ACTION_ACTIVITY_START", 600);
        } catch (Exception ex) {
            Log.e("ActivityThread", "Exception = " + ex);
        }
    }

    public void reportBindApplicationFinishedInActivityThread(Application app, ApplicationInfo ai) {
        try {
            OplusActivityManager.getInstance().reportBindApplicationFinished(app.getPackageName(), app.getUserId(), Process.myPid());
        } catch (Exception ex) {
            Slog.e("ActivityThread", "Exception = " + ex);
        }
        initVersionInfo(ai);
    }

    public void setDynamicalLogEnable(boolean enable) {
        ActivityDynamicLogHelper.setDynamicalLogEnable(enable);
    }

    public void enableDynamicalLogIfNeed() {
        ActivityDynamicLogHelper.enableDynamicalLogIfNeed();
    }

    public void setDynamicalLogConfig(List<String> configs) {
        AppDynamicalLogEnabler.getInstance().setDynamicalLogConfig(configs);
    }

    public void initDisplayCompat(ApplicationInfo ai, CompatibilityInfo ci) {
        ((IOplusAutoResolutionFeature) OplusFeatureCache.getOrCreate(IOplusAutoResolutionFeature.DEFAULT, new Object[0])).initDisplayCompat(ai, ci);
    }

    public void onDisplayChanged(int displayId) {
        if (!"com.android.systemui".equals(ActivityThread.currentPackageName()) && (displayId == 2020 || displayId >= 10000)) {
            SystemSensorManager.mInMirage = true;
        } else {
            SystemSensorManager.mInMirage = false;
        }
    }

    public boolean isTopApp() {
        return this.mIsTopApp;
    }

    public void setTopApp(boolean topApp) {
        this.mIsTopApp = topApp;
    }

    public boolean onTransact(int code, Parcel data, Parcel reply, int flags) {
        switch (code) {
            case 10005:
                data.enforceInterface(IOplusApplicationThread.DESCRIPTOR);
                boolean on = 1 == data.readInt();
                setZoomSensorState(on);
                return true;
            case 10006:
                data.enforceInterface(IOplusApplicationThread.DESCRIPTOR);
                final int level = data.readInt();
                Handler.getMain().post(new Runnable() { // from class: android.app.OplusActivityThreadExtImpl$$ExternalSyntheticLambda1
                    @Override // java.lang.Runnable
                    public final void run() {
                        OplusActivityThreadExtImpl.lambda$onTransact$0(level);
                    }
                });
                return true;
            case 10007:
                data.enforceInterface(IOplusApplicationThread.DESCRIPTOR);
                final OplusBracketModeUnit bracketModeUnit = (OplusBracketModeUnit) data.readParcelable(OplusBracketModeUnit.class.getClassLoader());
                Handler.getMain().post(new Runnable() { // from class: android.app.OplusActivityThreadExtImpl$$ExternalSyntheticLambda2
                    @Override // java.lang.Runnable
                    public final void run() {
                        OplusViewMirrorManager.getInstance().initBracketModeUnitDataSource(OplusBracketModeUnit.this);
                    }
                });
                return true;
            case 10008:
                data.enforceInterface(IOplusApplicationThread.DESCRIPTOR);
                final Bundle bundle = data.readBundle(getClass().getClassLoader());
                final IBinder iBinder = data.readStrongBinder();
                Handler.getMain().post(new Runnable() { // from class: android.app.OplusActivityThreadExtImpl$$ExternalSyntheticLambda3
                    @Override // java.lang.Runnable
                    public final void run() {
                        OplusActivityThreadExtImpl.this.lambda$onTransact$2(bundle, iBinder);
                    }
                });
                return true;
            default:
                return false;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ void lambda$onTransact$0(int level) {
        boolean success = WindowManagerGlobal.getInstance().getWrapper().trimMemoryIfAllowed(level);
        Slog.d("ActivityThread", "do gfx trim " + level + (success ? " success" : " failed"));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onTransact$2(Bundle bundle, IBinder iBinder) {
        ((IOplusViewExtractManager) OplusFeatureCache.getOrCreate(IOplusViewExtractManager.DEFAULT, new Object[0])).extractViewBundle(bundle, this.mBase, iBinder);
    }

    private void setZoomSensorState(boolean isZoom) {
        Slog.d("ActivityThread", "setZoomSensorState: isZoom " + isZoom);
        SystemSensorManagerExtImpl.sInZoomWindow = isZoom;
    }

    public final boolean hasImportMessage() {
        return this.mExecuteTransaction;
    }

    public void setImportMessage(boolean isFirstFrame) {
        this.mExecuteTransaction = isFirstFrame;
    }

    public void handleResumeActivity(String activityName) {
        this.mResumeActivityStartTime = System.nanoTime();
        this.mCurrActivityName = activityName;
        ScrOptController.getInstance().getSceneManager().updateCurrentActivity(activityName);
    }

    public void handlePauseActivity(String activityName) {
        this.mPauseActivityStartTime = System.nanoTime();
        Choreographer choreographer = Choreographer.getMainThreadInstance();
        if (choreographer != null && choreographer.mChoreographerExt != null) {
            choreographer.mChoreographerExt.makePauseActivityEnd();
        }
        if (activityName != null && activityName.equals(this.mCurrActivityName)) {
            this.mCurrActivityName = null;
            ScrOptController.getInstance().getSceneManager().updateCurrentActivity(null);
        }
    }

    public void setLifecycleState(int state) {
        if (state == 1) {
            this.mOnCreateState = true;
            return;
        }
        if (Choreographer.getMainThreadInstance() == null) {
            Choreographer.getInstance();
        }
        Choreographer choreographer = Choreographer.getMainThreadInstance();
        if (choreographer != null && choreographer.mChoreographerExt != null) {
            choreographer.mChoreographerExt.setLifecycleState(state, this.mOnCreateState, this.mLaunchActivityStartTime, this.mResumeActivityStartTime, this.mPauseActivityStartTime);
            this.mOnCreateState = false;
            this.mLaunchActivityStartTime = 0L;
            this.mResumeActivityStartTime = 0L;
            this.mPauseActivityStartTime = 0L;
        }
    }

    public String getCurrentActivityName() {
        return this.mCurrActivityName;
    }

    public void asyncReportFrames(long skippedFrames) {
        if (this.mFreezeEnable && skippedFrames >= this.mSkipFramesCnt) {
            long curTime = SystemClock.uptimeMillis();
            if (curTime - this.mLastFreezeTime >= this.mFreezeDuration) {
                try {
                    OplusActivityManager.getInstance().asyncReportFrames(ActivityThread.currentPackageName(), (int) skippedFrames);
                } catch (RemoteException e) {
                    Slog.e("ActivityThread", "asyncReportFrames ERROR");
                }
                this.mLastFreezeTime = curTime;
            }
        }
    }

    public void initCompactApplicationInfo(ApplicationInfo ai) {
        OplusFeatureCache.getOrCreate(IOplusCompactWindowAppManager.DEFAULT, new Object[0]).initCompactApplicationInfo(ai);
        OplusTaskBarUtils.getInstance().initTaskBarApplicationInfo(ai);
    }

    public boolean shouldInterceptConfigForSplit(IBinder token, Configuration config, Configuration newConfig, Configuration overrideConfig, boolean displayChanged, Boolean configChanged) {
        boolean z;
        ActivityThread activityThread;
        if ((configChanged != null && !OplusSplitScreenManager.getInstance().isInSplitScreenMode()) || config == null || displayChanged || !ResourcesManager.getInstance().isSameResourcesOverrideConfig(token, overrideConfig)) {
            return false;
        }
        Activity activity = (token == null || (activityThread = this.mBase) == null) ? null : activityThread.getActivity(token);
        if (activity != null && FlexibleWindowManager.getInstance().isSupportPocketStudio(activity)) {
            return false;
        }
        int origRot = config.windowConfiguration.getDisplayRotation();
        int newRot = newConfig.windowConfiguration.getDisplayRotation();
        if (newRot == -1 || origRot == -1) {
        }
        if (origRot == newRot) {
            z = false;
        } else {
            z = true;
        }
        boolean rotationChanged = z;
        if (rotationChanged || configChanged.booleanValue()) {
            return false;
        }
        return true;
    }

    public int excludeExtConfigDiff(int diff) {
        if ((268435456 & diff) != 0) {
            return (-268435457) & diff;
        }
        return diff;
    }

    public boolean shouldSendConfigration(Configuration currentConfig, Configuration newConfig, IBinder token) {
        ActivityThread activityThread;
        Activity activity = (token == null || (activityThread = this.mBase) == null) ? null : activityThread.getActivity(token);
        return OplusCompactWindowManager.getInstance().shouldSendConfigration(currentConfig, newConfig, token, activity);
    }

    public void hookHandleBindApplication(ActivityThread.AppBindData data, Context context) {
        getAutoLayoutManager().hookHandleBindApplication(data.appInfo, context);
        getRGBNormalizeManager().hookHandleBindApplication(data.appInfo);
        getScrollToTopManager().setIsInWhiteList(data.appInfo.mApplicationInfoExt.isInScrollToTopWhiteList());
        getScrollToTopManager().setNeedShowGuidePopup(data.appInfo.mApplicationInfoExt.isNeedShowScrollToTopGuidePopup());
        OplusFeatureCache.getOrCreate(IOplusCompactWindowAppManager.DEFAULT, new Object[0]).registerDeviceStateCallBack(data.appInfo, context);
        ((IOplusViewDebugManager) OplusFeatureCache.getOrCreate(IOplusViewDebugManager.mDefault, new Object[0])).markOnBindApplication(data.appInfo);
        FlexibleWindowManager.getInstance().hookHandleBindApplication(data.config);
        Bundle applicationBundle = data.appInfo.mApplicationInfoExt.getResourcesManagerExtraBundle();
        if (applicationBundle != null) {
            String resourcesCacheList = applicationBundle.getString("resources_cache_list", "");
            ResourcesManagerExtImpl.setResourcesCacheList(resourcesCacheList);
        }
    }

    public void hookPerformLaunchActivity(ActivityInfo activityInfo, Configuration configuration) {
        getAutoLayoutManager().hookPerformLaunchActivity(activityInfo, configuration);
        getRGBNormalizeManager().hookPerformLaunchActivity(activityInfo);
    }

    public void hookPerformResumeActivity(ActivityInfo activityInfo, Configuration configuration) {
        getAutoLayoutManager().hookPerformResumeActivity(activityInfo, configuration);
        getRGBNormalizeManager().hookPerformResumeActivity(activityInfo);
    }

    public void hookConfigurationChangedActivity(ActivityInfo activityInfo, Configuration configuration) {
        getAutoLayoutManager().hookConfigurationChangedActivity(activityInfo, configuration);
    }

    private IOplusAutoLayoutManager getAutoLayoutManager() {
        return (IOplusAutoLayoutManager) OplusFeatureCache.getOrCreate(IOplusAutoLayoutManager.mDefault, new Object[0]);
    }

    public void hookApplication(Application app) {
        getAutoLayoutManager().hookApplication(app);
    }

    private IOplusScrollToTopManager getScrollToTopManager() {
        return (IOplusScrollToTopManager) OplusFeatureCache.getOrCreate(IOplusScrollToTopManager.DEFAULT, new Object[0]);
    }

    private IOplusRGBNormalizeManager getRGBNormalizeManager() {
        return (IOplusRGBNormalizeManager) OplusFeatureCache.getOrCreate(IOplusRGBNormalizeManager.DEFAULT, new Object[0]);
    }

    public void enableActivityThreadLog(ActivityThread activityThread) {
        String activitylog = SystemProperties.get("persist.vendor.sys.activitylog", (String) null);
        if (activitylog != null && !activitylog.equals("")) {
            if (activitylog.indexOf(" ") != -1 && activitylog.indexOf(" ") + 1 <= activitylog.length()) {
                String option = activitylog.substring(0, activitylog.indexOf(" "));
                String enable = activitylog.substring(activitylog.indexOf(" ") + 1, activitylog.length());
                boolean isEnable = "on".equals(enable);
                if (option.equals("x")) {
                    enableActivityThreadLog(isEnable, activityThread);
                    return;
                }
                return;
            }
            SystemProperties.set("persist.vendor.sys.activitylog", "");
        }
    }

    public void enableActivityThreadLog(boolean isEnable, ActivityThread activityThread) {
        ActivityThread.localLOGV = isEnable;
        ActivityThread.DEBUG_MESSAGES = isEnable;
        ActivityThread.DEBUG_BROADCAST = isEnable;
        ActivityThread.DEBUG_RESULTS = isEnable;
        ActivityThread.DEBUG_BACKUP = isEnable;
        ActivityThread.DEBUG_CONFIGURATION = isEnable;
        ActivityThread.DEBUG_SERVICE = isEnable;
        ActivityThread.DEBUG_MEMORY_TRIM = isEnable;
        ActivityThread.DEBUG_PROVIDER = isEnable;
        ActivityThread.DEBUG_ORDER = isEnable;
    }

    public void enableProcessMainThreadLooperLog() {
        Slog.v("ActivityThread", "enableMainThreadLooperLog:" + this.mBase.getProcessName() + ", mLooper=" + this.mBase.getLooper());
        if (this.mBase.getLooper() != null) {
            this.mBase.getLooper().setMessageLogging(new Printer() { // from class: android.app.OplusActivityThreadExtImpl$$ExternalSyntheticLambda0
                @Override // android.util.Printer
                public final void println(String str) {
                    Slog.v("ActivityThread", "main thread looper msg: " + str);
                }
            });
        }
    }

    public boolean isZoomSupportMultiWindow(Activity activity, int windowingMode) {
        if (windowingMode != 100) {
            return false;
        }
        if (this.mIsZoomSupportMultiWindow == null) {
            this.mIsZoomSupportMultiWindow = Boolean.valueOf(OplusZoomWindowManager.getInstance().isZoomSupportMultiWindow(activity.getPackageName(), activity.getComponentName()));
        }
        return this.mIsZoomSupportMultiWindow.booleanValue();
    }

    public void performConfigurationChangedForActivity(Activity activity, Configuration newConfiguration) {
        activity.getWrapper().getExtImpl().onScenarioChanged(newConfiguration);
    }

    public boolean handleScenarioChangeInPocketStudioIfNeeded(Activity activity, Configuration newConfiguration) {
        if (activity == null) {
            return false;
        }
        int extraDiff = activity.mCurrentConfig.diffPublicOnly(newConfiguration);
        boolean scenarioChanged = (524288 & extraDiff) != 0;
        boolean isSupportSplitScreenWindowingMode = activity.getWrapper().getExtImpl().isSupportSplitScreenWindowingMode();
        if (!scenarioChanged || !isSupportSplitScreenWindowingMode) {
            return false;
        }
        boolean isInMultiWindowMode = activity.getWrapper().getExtImpl().isInMultiWindowModeInPocketStudio();
        activity.dispatchMultiWindowModeChanged(isInMultiWindowMode, newConfiguration);
        return true;
    }

    public void enableBitmapTracking() {
        boolean bPreVersion = SystemProperties.get("ro.build.version.ota", "ota_version").contains("PRE");
        boolean bReleaseType = SystemProperties.getBoolean("ro.build.release_type", false);
        boolean bEnabled = bPreVersion || !bReleaseType;
        if (bEnabled) {
            Bitmap.enableTracking(1);
        } else {
            Bitmap.enableTracking(0);
        }
    }

    public String dumpBitmapTrackingInfo() {
        return "\n" + Bitmap.dump(1) + "\n";
    }

    private void checkUpdateDalvikSyncConfig() {
        String config = SystemProperties.get(PROP_DALVIK_SYNC_CONFIG);
        if (TextUtils.isEmpty(config)) {
            this.mDalvikSyncFeatureOn = false;
            return;
        }
        int curConfigHashCode = config.hashCode();
        if (curConfigHashCode != this.mLastDalvikSyncConfigHashCode) {
            this.mLastDalvikSyncConfigHashCode = curConfigHashCode;
            if ("disable".equals(config)) {
                this.mDalvikSyncFeatureOn = false;
            } else {
                this.mDalvikSyncFeatureOn = parseDalvikSyncConfig(config);
            }
        }
    }

    private boolean parseDalvikSyncConfig(String config) {
        String[] dalvikConfig = config.split(DALVIK_SYNC_CONFIG_SEPARATOR);
        if (dalvikConfig.length < 5) {
            return false;
        }
        try {
            this.mLowSyncPercent = Integer.parseInt(dalvikConfig[0]);
            this.mHighSyncPercent = Integer.parseInt(dalvikConfig[1]);
            this.mLowCheckOrSyncInv = Integer.parseInt(dalvikConfig[2]);
            this.mHighCheckOrSyncInv = Integer.parseInt(dalvikConfig[3]);
            this.mFrequentGcMinMatchCount = Integer.parseInt(dalvikConfig[4]);
            return true;
        } catch (Exception e) {
            if (ActivityThread.DEBUG_MEMORY_TRIM) {
                Log.e("ActivityThread", "parse dalvik sync config error: ", e);
            }
            return false;
        }
    }

    private boolean isFrequentGcScene(long curTime) {
        if (this.mHighCheckOrSyncInv <= 0 || this.mFrequentGcMinMatchCount <= 0) {
            return false;
        }
        if (this.mGcRunTimeQueue == null) {
            this.mGcRunTimeQueue = new ArrayDeque<>(this.mFrequentGcMinMatchCount);
        }
        this.mGcRunTimeQueue.add(Long.valueOf(curTime));
        if (this.mGcRunTimeQueue.size() >= this.mFrequentGcMinMatchCount) {
            Long oldestOne = this.mGcRunTimeQueue.poll();
            boolean report = oldestOne != null && oldestOne.longValue() >= curTime - ((long) this.mHighCheckOrSyncInv);
            if (report) {
                long j = curTime - this.mLastReportFrequentGcTime;
                int i = this.mHighCheckOrSyncInv;
                if (j >= i && curTime - this.mLastReportWarningDalvikTime >= i) {
                    this.mLastReportFrequentGcTime = curTime;
                    return true;
                }
            }
        }
        return false;
    }

    private void reportDalvikMem(int percent, long uptime, long used) {
        try {
            Bundle bundle = new Bundle();
            bundle.putString(OplusBaseBatterySipper.BundlePkgName, this.mBase.mBoundApplication.appInfo.packageName);
            bundle.putInt("uid", this.mBase.mBoundApplication.appInfo.uid);
            bundle.putString("procName", this.mBase.getProcessName());
            bundle.putLong("appCreatedUptime", this.mAppCreatedUptime);
            if (ActivityThread.DEBUG_MEMORY_TRIM) {
                try {
                    try {
                        Log.d("ActivityThread", "async dalvik event max=" + this.mDalvikMax + ", used=" + used + "(" + percent + ")");
                    } catch (RemoteException e) {
                        e = e;
                        Slog.e("ActivityThread", "asyncReportDalvikMem error: ", e);
                        return;
                    }
                } catch (RemoteException e2) {
                    e = e2;
                    Slog.e("ActivityThread", "asyncReportDalvikMem error: ", e);
                    return;
                }
            }
            OplusActivityManager.getInstance().asyncReportDalvikMem(bundle, Process.myPid(), this.mDalvikMax, used, uptime);
        } catch (RemoteException e3) {
            e = e3;
        }
    }

    private void checkIfReportWarningDalvikMem(int percent, long curTime, long used, long checkInv) {
        if (curTime - this.mLastReportWarningDalvikTime >= checkInv) {
            this.mLastReportWarningDalvikTime = curTime;
            this.mHasReportedWarningDalvik = true;
            reportDalvikMem(percent, curTime, used);
        }
    }

    private void checkIfReportNormalDalvikMem(int percent, long curTime, long used) {
        if (this.mHasReportedWarningDalvik) {
            reportDalvikMem(percent, curTime, used);
            this.mHasReportedWarningDalvik = false;
        }
    }

    public void asyncReportDalvikMem() {
        boolean frequentGc;
        if (this.mDalvikMax <= 0) {
            return;
        }
        checkUpdateDalvikSyncConfig();
        if (!this.mDalvikSyncFeatureOn) {
            this.mGcRunTimeQueue = null;
            return;
        }
        long curTime = SystemClock.uptimeMillis();
        if (curTime >= this.mNextExpectedTime) {
            frequentGc = false;
        } else {
            boolean frequentGc2 = isFrequentGcScene(curTime);
            if (frequentGc2) {
                frequentGc = frequentGc2;
            } else {
                return;
            }
        }
        Runtime runtime = Runtime.getRuntime();
        long curDalvikUsed = runtime.totalMemory() - runtime.freeMemory();
        int curDalvikPercent = (int) ((100 * curDalvikUsed) / this.mDalvikMax);
        if (frequentGc && ActivityThread.DEBUG_MEMORY_TRIM) {
            Log.d("ActivityThread", "report frequent gc event max=" + this.mDalvikMax + ", used=" + curDalvikUsed + "(" + curDalvikPercent + ")");
        }
        long checkInv = curDalvikPercent < this.mHighSyncPercent ? this.mLowCheckOrSyncInv : this.mHighCheckOrSyncInv;
        this.mNextExpectedTime = curTime + checkInv;
        if (curDalvikPercent < this.mLowSyncPercent) {
            checkIfReportNormalDalvikMem(curDalvikPercent, curTime, curDalvikUsed);
        } else {
            checkIfReportWarningDalvikMem(curDalvikPercent, curTime, curDalvikUsed, checkInv);
        }
    }

    public static boolean isMirageWindowDisplayId(int displayId) {
        if (displayId == 2020 || displayId >= 10000) {
            return true;
        }
        return false;
    }

    public int needChangeDiff(int diff, int lastDisplayId, int newDisplayId) {
        if (isMirageWindowDisplayId(lastDisplayId) || isMirageWindowDisplayId(newDisplayId)) {
            Slog.d("TAG", " mirage display clear CONFIG_COLOR_MODE and CONFIG_TOUCHSCREEN change for ActivityThread");
            return changeConfigDiff(diff);
        }
        return diff;
    }

    public void setConfigurationAndResourceManager(Configuration configuration, ResourcesManager resourcesManager) {
        this.mConfiguration = configuration;
        this.mResourcesManager = resourcesManager;
    }

    public Configuration getConfiguration() {
        return this.mConfiguration;
    }

    public ResourcesManager getResourcesManager() {
        return this.mResourcesManager;
    }

    public void extractBundleDataIfNeeded(Bundle bundle) {
        this.mDisplayId = -1;
        if (bundle.containsKey("display")) {
            this.mDisplayId = bundle.getInt("display");
            bundle.remove("display");
        }
    }

    public void applyConfigurationToAppResourcesLocked(ResourcesManager resourcesManager, Context app, int displayId, Configuration config, boolean needToJudge) {
        if (!needToJudge || ((app == null || !isMirageWindowDisplayId(app.getDisplayId())) && !isMirageWindowDisplayId(displayId))) {
            this.mDisplayId = displayId;
        }
        applyConfigurationToAppResourcesLocked(resourcesManager, app, config);
    }

    public void applyConfigurationToAppResourcesLocked(ResourcesManager resourcesManager, Context app, Configuration config) {
        resourcesManager.mIResourcesManagerExt.applyConfigurationToAppResourcesLocked(app, this.mDisplayId, config, resourcesManager.getWrapper().getResourceReferences());
    }

    public void resetDisplayId() {
        this.mDisplayId = -1;
    }

    public int getDisplayId() {
        return this.mDisplayId;
    }

    public boolean isSecondDisplay(int displayId) {
        return displayId == 1;
    }

    public void updateSystemUIOrientation(ConfigurationController configurationController, Configuration updatedConfig, Configuration globalConfig) {
        Configuration config = configurationController.getConfiguration();
        if ("com.android.systemui".equals(ActivityThread.currentPackageName()) && updatedConfig == null && config != null && (globalConfig.diffPublicOnly(config) & 128) != 0) {
            Log.d("ActivityThread", "updateSystemUIOrientation: " + globalConfig);
            this.mBase.sendMessage(118, globalConfig);
        }
    }

    public void updateDisplayIfNeed(Application application, int displayId, Configuration overrideConfig) {
        boolean isSuitableApplication = false;
        boolean isValidDisplay = displayId == 0 || isSecondDisplay(displayId) || overrideConfig.getOplusExtraConfiguration().isPuttDisplay();
        if (application != null && !PACKAGE_SYSTEM_IGNORE_UPDATE_DISPLAY.contains(application.getPackageName())) {
            isSuitableApplication = true;
        }
        if (isSuitableApplication && isValidDisplay && application.getDisplayId() != displayId) {
            Slog.d("ActivityThread", "updateDisplayIfNeed update displayId to " + displayId);
            application.updateDisplay(displayId);
        }
    }

    public void hookResumeActivity(String className, boolean finished, int lifecycleState) {
        Slog.d("ActivityThread", className + " checkFinished=" + finished + " " + lifecycleState);
        Trace.traceBegin(64L, className + " hookFinished=" + finished + " " + lifecycleState);
        Trace.traceEnd(64L);
    }

    public boolean isPerfMonitorEnable() {
        return StrictMode.mStrictModeExt.isPerfMonitorEnable();
    }

    public boolean enlargeHeapGrowthLimit(String pkgName) {
        OplusPackageManager pm = new OplusPackageManager();
        if (pkgName == null) {
            return false;
        }
        try {
            boolean isEnlargeHeapApp = pm.inOplusStandardWhiteList("sys_launch_opt_whitelist", 4, pkgName);
            if (isEnlargeHeapApp) {
                Class vmRuntime = Class.forName("dalvik.system.VMRuntime");
                Method getRuntime = vmRuntime.getMethod("getRuntime", null);
                Object obj = getRuntime.invoke(null, null);
                Method enlargeHeapGrowthLimit = vmRuntime.getMethod("enlargeHeapGrowthLimit", null);
                enlargeHeapGrowthLimit.invoke(obj, null);
            }
            return isEnlargeHeapApp;
        } catch (NoSuchMethodException e) {
            Slog.d("ActivityThread", e.toString());
            return false;
        } catch (Exception e2) {
            Slog.d("ActivityThread", e2.toString());
            return false;
        }
    }

    public Configuration getFakeDisplayConfiguration() {
        return this.mFakeDisplayConfiguration;
    }

    public int hookCreateBaseContextForActivity(Application application, ActivityThread.ActivityClientRecord r, int displayId) {
        if (!FlexibleWindowManager.getInstance().hasFSDFeature()) {
            return displayId;
        }
        if (application != null && application.getBaseContext() != null && r != null && displayId == 0) {
            Set<String> tokens = LOCAL_START_TOKENS.get(application.getPackageName());
            boolean isLocalStartToken = tokens != null && tokens.contains(r.embeddedID);
            Context applicationContext = application.getApplicationContext();
            if (isLocalStartToken && applicationContext != null) {
                Configuration config = applicationContext.getResources().getConfiguration();
                int scenario = config != null ? config.getOplusExtraConfiguration().getScenario() : 0;
                if (scenario == 3) {
                    Slog.d("ActivityThread", "hookCreateBaseContextForActivity: 1");
                    return 1;
                }
            }
        }
        return displayId;
    }

    public void handleRelaunchActivityConfig(ActivityThread.ActivityClientRecord ac, ActivityThread thread) {
        if (ac != null && thread != null && thread.getSystemContext() != null && ac.activity != null) {
            DisplayInfo lastDisplay = new DisplayInfo();
            DisplayInfo currentDisplay = new DisplayInfo();
            ac.activity.getDisplay().getDisplayInfo(lastDisplay);
            thread.getSystemContext().getDisplay().getDisplayInfo(currentDisplay);
            Slog.d("ActivityThread", "lastDisplayInfo is: " + lastDisplay + " currentDisplay is: " + currentDisplay);
            if (lastDisplay.displayId == 1 && currentDisplay.displayId == 0 && lastDisplay.state == 1 && currentDisplay.state == 2) {
                Slog.d("ActivityThread", "handleRelaunchActivityConfig, activity config is: " + ac.overrideConfig + " application config is: " + thread.getConfiguration());
                ac.overrideConfig.updateFrom(thread.getConfiguration());
            }
        }
    }

    public int getLabApplicationDisplayId(Application application, ActivityThread.ActivityClientRecord activityClientRecord, Configuration activityConfig, Configuration overrideConfig) {
        if (application != null && activityClientRecord != null && activityConfig != null && overrideConfig != null) {
            int currentDisplayId = application.getDisplayId();
            int preDisplayId = activityClientRecord.activity.getDisplayId();
            String packageName = application.getPackageName();
            boolean isWechat = packageName != null && packageName.equals(TENCENT_MM_PACKAGE_NAME);
            boolean isLabScene = activityConfig.getOplusExtraConfiguration().getScenario() == 3 && overrideConfig.getOplusExtraConfiguration().getScenario() == 0;
            if (currentDisplayId == 0 && preDisplayId == 1 && isLabScene && isWechat) {
                Slog.d("ActivityThread", "getLabApplicationDisplayId: 0");
                return 0;
            }
            return -1;
        }
        return -1;
    }

    public void setLastReportedConfig(Configuration configuration) {
        OplusFlexibleViewManager.setLastReportedConfig(configuration);
    }

    public boolean isDispatchConfigurationChanged(Configuration currentConfiguration, Configuration newConfiguration, Activity activity) {
        if (currentConfiguration != null && newConfiguration != null) {
            Rect currentappbounds = currentConfiguration.windowConfiguration.getAppBounds();
            Rect newappbounds = newConfiguration.windowConfiguration.getAppBounds();
            if (currentappbounds != null && newappbounds != null && !currentappbounds.equals(newappbounds) && newConfiguration.windowConfiguration.getWindowingMode() == 100 && currentConfiguration.windowConfiguration.getWindowingMode() == 100 && PACKAGE_ZOOM_IGNORE_DISPATCH_CONFIGURATIONCHANGED.contains(activity.getPackageName())) {
                return false;
            }
            return true;
        }
        return true;
    }

    public void hookUpdateProcessState(int state, int stateValue) {
        getAppHeapManager().updateProcessState(state, stateValue);
    }

    public void hookBindApplication(ApplicationInfo ai) {
        if (ai == null || ai.mApplicationInfoExt == null) {
            return;
        }
        getAppHeapManager().setIsInWhiteList(ai.mApplicationInfoExt.isInAppHeapWhiteList(), ai.packageName);
    }

    private IOplusAppHeapManager getAppHeapManager() {
        return (IOplusAppHeapManager) OplusFeatureCache.getOrCreate(IOplusAppHeapManager.DEFAULT, new Object[0]);
    }
}
