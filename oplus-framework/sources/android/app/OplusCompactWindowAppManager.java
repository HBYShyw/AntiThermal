package android.app;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.IApplicationInfoExt;
import android.content.res.CompatibilityInfo;
import android.content.res.Configuration;
import android.content.res.OplusBaseConfiguration;
import android.content.res.OplusThemeResources;
import android.content.res.Resources;
import android.content.res.ResourcesImpl;
import android.graphics.Rect;
import android.hardware.devicestate.DeviceStateManager;
import android.os.Process;
import android.os.SystemProperties;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.Slog;
import android.view.DisplayAdjustments;
import com.android.internal.util.ConcurrentUtils;
import com.google.android.collect.Sets;
import com.oplus.compactwindow.OplusCompactWindowManager;
import com.oplus.content.IOplusFeatureConfigList;
import com.oplus.content.OplusFeatureConfigManager;
import com.oplus.util.OplusTypeCastingHelper;
import java.util.Set;

/* loaded from: classes.dex */
public class OplusCompactWindowAppManager implements IOplusCompactWindowAppManager {
    private static final int CUSTOM_UI_MODE_FOLLOW_SYSTEM = 2;
    private static final String CUSTOM_UI_MODE_KEY = "@int:customUIMode";
    private static final String LEVEL_DEBUG = "systemui_debug";
    private static final int LEVEL_INTERNAL_BIT = 16;
    private static final int PADDING = 1;
    private static final int SW_DP_THRESHOLD_FOR_COMPACT_WINDOW = 550;
    private static final int SYSTEM_FOLDING_MODE_CLOSE = 0;
    private static final String SYSTEM_FOLDING_MODE_KEYS = "oplus_system_folding_mode";
    private static final int SYSTEM_FOLDING_MODE_OPEN = 1;
    private static final String TAG = "CompactWindowAppManager";
    private static final String WE_CHAT_PKG = "com.tencent.mm";
    private int mCompactModeFlag = 0;
    private Boolean mIsBlockPackageForCompactWindow = null;
    private int mNormalModeFlag = 0;
    private int mSystemFoldingMode = 0;
    public static final boolean DEBUG_ADJUST_CONFIG_V2 = SystemProperties.getBoolean("persist.sys.compact.adjustConfigV2", true);
    private static final Boolean DEBUG = false;
    private static final boolean DEBUG_COMPACT_CONFIG = SystemProperties.getBoolean("persist.sys.debug.compactwindow.config", false);
    private static Boolean sSupportReviseSquareDisplayOrientation = null;
    private static final Set<String> BLACK_SENSOR_EVENT_PACKAGES = Sets.newHashSet(new String[]{"com.qiyi.video", "tv.danmaku.bili"});
    private static final Set<String> BLACK_PACKAGES_FOR_COMPACT_WINDOW_ADJUSTMENT = Sets.newHashSet(new String[]{"com.android.launcher", "com.oplus.screenrecorder", "com.coloros.screenrecorder", "com.oplus.uxdesign", OplusThemeResources.FRAMEWORK_PACKAGE, "com.oplus.camera", "com.android.systemui", "com.dreamtee.apkfure", "com.android.permissioncontroller"});
    private static final Set<String> MODIFY_ORIGIN_MATRIX_FOR_COMPACT_WINDOW_PACKAGE = Sets.newHashSet(new String[]{"cn.yonghui.hyd", "com.android.bankabc", "com.czb.charge"});
    private static Boolean sIsTablet = null;
    private static Boolean sSupportCompactWindow = null;
    private static int sDeviceState = -1;
    private static volatile OplusCompactWindowAppManager sInstance = null;

    public static OplusCompactWindowAppManager getInstance() {
        if (sInstance == null) {
            synchronized (OplusCompactWindowAppManager.class) {
                if (sInstance == null) {
                    sInstance = new OplusCompactWindowAppManager();
                }
            }
        }
        return sInstance;
    }

    OplusCompactWindowAppManager() {
    }

    private boolean isTablet() {
        if (sIsTablet == null) {
            sIsTablet = Boolean.valueOf(OplusFeatureConfigManager.getInstance().hasFeature(IOplusFeatureConfigList.FEATURE_TABLET));
        }
        return sIsTablet.booleanValue();
    }

    private boolean inCompactWindowingMode(Configuration configuration) {
        return configuration != null && configuration.windowConfiguration.getWindowingMode() == 120;
    }

    public boolean blockOrientationSensorEventInCompactWindowMode(Context context, int sensorType, String packageName) {
        if (context == null || context.getResources() == null || context.getResources().getConfiguration() == null) {
            return false;
        }
        boolean sensorCondition = sensorType == 1 || sensorType == 3;
        int windowingMode = context.getResources().getConfiguration().windowConfiguration.getWindowingMode();
        boolean condition = sensorCondition && (OplusCompactWindowManager.isModeParallel() || ResourcesManagerExtImpl.inOplusCompatMode(context.getResources().getConfiguration()));
        boolean conditionForPhone = sensorCondition && (windowingMode == 120 || ResourcesManagerExtImpl.inOplusCompatMode(context.getResources().getConfiguration()));
        boolean conditionOfNormalMode = sensorCondition && (windowingMode == 1 || windowingMode == 0);
        if (!isTablet() && conditionForPhone && BLACK_SENSOR_EVENT_PACKAGES.contains(packageName)) {
            if (DEBUG.booleanValue()) {
                Slog.w(TAG, "Block orientation sensor event in parallelwindow of package :" + packageName + "; type = " + sensorType);
            }
            return true;
        }
        if (isTablet() && condition && OplusCompactWindowManager.isBlockSensor(this.mCompactModeFlag) && (context.getResources().getConfiguration().orientation == 1 || ((windowingMode == 1 && context.getResources().getConfiguration().orientation == 2) || OplusCompactWindowManager.isForceBlock(this.mCompactModeFlag)))) {
            if (DEBUG.booleanValue()) {
                Slog.w(TAG, "Block orientation sensor event in parallelwindow of package :" + packageName + "; type = " + sensorType);
            }
            return true;
        }
        OplusBaseConfiguration baseConfig = (OplusBaseConfiguration) OplusTypeCastingHelper.typeCasting(OplusBaseConfiguration.class, context.getResources().getConfiguration());
        if (baseConfig != null && baseConfig.mOplusExtraConfiguration.getScenario() >= 1 && OplusCompactWindowManager.isBlockSensor(this.mNormalModeFlag)) {
            if (DEBUG.booleanValue()) {
                Slog.w(TAG, "Block orientation sensor event in pocketstudio :" + packageName + "; type = " + sensorType);
            }
            return true;
        }
        if (isSupportReviseSquareDisplayOrientation() && this.mSystemFoldingMode == 1 && conditionOfNormalMode && OplusCompactWindowManager.isBlockSensor(this.mNormalModeFlag)) {
            if (DEBUG.booleanValue()) {
                Slog.w(TAG, "Block orientation sensor event in normal window mode of package :" + packageName + "; type = " + sensorType);
            }
            return true;
        }
        OplusBaseConfiguration appConfig = (OplusBaseConfiguration) OplusTypeCastingHelper.typeCasting(OplusBaseConfiguration.class, context.getResources().getConfiguration());
        if (!sensorCondition || !OplusCompactWindowManager.isBlockSensor(this.mNormalModeFlag) || appConfig == null || appConfig.mOplusExtraConfiguration.getScenario() != 3) {
            return false;
        }
        if (DEBUG.booleanValue()) {
            Slog.w(TAG, "Block orientation sensor event for lab application: " + packageName + "; type = " + sensorType);
        }
        return true;
    }

    public DisplayMetrics getCompactWindowMetrics(ResourcesImpl resImpl, DisplayMetrics originMetrics) {
        DisplayAdjustments adjustments;
        if (resImpl == null || (adjustments = resImpl.mResourcesImplExt.getCompactWindowAdjustments()) == null || blackPackageForCompactWindowAdjustment()) {
            return null;
        }
        Rect widthrect = adjustments.getConfiguration().windowConfiguration.getAppBounds();
        Rect heightrect = adjustments.getConfiguration().windowConfiguration.getAppBounds();
        if (widthrect == null || heightrect == null) {
            return null;
        }
        if (MODIFY_ORIGIN_MATRIX_FOR_COMPACT_WINDOW_PACKAGE.contains(ActivityThread.currentPackageName())) {
            int width = widthrect.width();
            originMetrics.widthPixels = width;
            originMetrics.noncompatWidthPixels = width;
            int height = heightrect.height();
            originMetrics.heightPixels = height;
            originMetrics.noncompatHeightPixels = height;
            return originMetrics;
        }
        DisplayMetrics adjustMetrics = new DisplayMetrics();
        adjustMetrics.setTo(originMetrics);
        int width2 = widthrect.width();
        adjustMetrics.widthPixels = width2;
        adjustMetrics.noncompatWidthPixels = width2;
        int height2 = heightrect.height();
        adjustMetrics.heightPixels = height2;
        adjustMetrics.noncompatHeightPixels = height2;
        return adjustMetrics;
    }

    public void updateCompactWindowConfigToApplicationResourcesImpl(Configuration activityThreadConfig, ResourcesImpl impl) {
        if (DEBUG_ADJUST_CONFIG_V2 && !ResourcesManagerExtImpl.inOplusCompatMode(activityThreadConfig)) {
            return;
        }
        Configuration baseConfig = null;
        if (impl != null && !isTablet()) {
            baseConfig = impl.mResourcesImplExt.getConfiguration();
            impl.mResourcesImplExt.setIsAppConfig(true);
        }
        if (activityThreadConfig != null && baseConfig != null) {
            if (activityThreadConfig.windowConfiguration.getWindowingMode() == 120 || ResourcesManagerExtImpl.inOplusCompatMode(activityThreadConfig)) {
                Configuration temp = new Configuration(baseConfig);
                temp.windowConfiguration.setAppBounds(activityThreadConfig.windowConfiguration.getAppBounds());
                temp.windowConfiguration.setWindowingMode(activityThreadConfig.windowConfiguration.getWindowingMode());
                Log.d(TAG, "updateCompactWindowConfigToApplicationResourcesImpl temp =" + temp);
                impl.updateConfiguration(temp, (DisplayMetrics) null, (CompatibilityInfo) null);
            }
        }
    }

    public void setCompactWindowDisplayAdjustment(ResourcesImpl impl, Configuration oldOverrideConfig, Configuration newOverrideConfig) {
        if ((!DEBUG_ADJUST_CONFIG_V2 || (!inCompactWindowingMode(oldOverrideConfig) && !inCompactWindowingMode(newOverrideConfig))) && impl != null) {
            impl.mResourcesImplExt.updateCompactWindowAdjustments(oldOverrideConfig, newOverrideConfig);
        }
    }

    public DisplayAdjustments getCompactWindowDisplayAdjustment(Resources resources) {
        if (resources != null && resources.getImpl() != null && !blackPackageForCompactWindowAdjustment()) {
            return resources.getImpl().mResourcesImplExt.getCompactWindowAdjustments();
        }
        return null;
    }

    private boolean blackPackageForCompactWindowAdjustment() {
        if (this.mIsBlockPackageForCompactWindow == null) {
            String appName = ActivityThread.currentPackageName();
            if (!TextUtils.isEmpty(appName)) {
                this.mIsBlockPackageForCompactWindow = Boolean.valueOf(BLACK_PACKAGES_FOR_COMPACT_WINDOW_ADJUSTMENT.contains(appName) || OplusCompactWindowManager.isBlackCompactWindowAdjustment(this.mCompactModeFlag));
            }
        }
        Boolean bool = this.mIsBlockPackageForCompactWindow;
        return bool != null && bool.booleanValue();
    }

    public int getCompactWindowRotation(Resources resources) {
        DisplayAdjustments compactWindowAdjustment = getCompactWindowDisplayAdjustment(resources);
        if (compactWindowAdjustment != null && compactWindowAdjustment.getConfiguration().windowConfiguration.getAppBounds() != null && isTablet() && resources != null && resources.getConfiguration() != null) {
            if (resources.getConfiguration().orientation != 2 || OplusCompactWindowManager.isForceBlock(this.mCompactModeFlag)) {
                return OplusCompactWindowManager.getBlockDisplayRotation(this.mCompactModeFlag);
            }
            return -1;
        }
        return -1;
    }

    public void initCompactApplicationInfo(ApplicationInfo appInfo) {
        IApplicationInfoExt appInfoExt = appInfo == null ? null : appInfo.mApplicationInfoExt;
        if (appInfoExt == null) {
            Log.d(TAG, "initCompactApplicationInfo error");
            return;
        }
        this.mCompactModeFlag = appInfoExt.getCompatModeFlag();
        this.mNormalModeFlag = appInfoExt.getNormalModeFlag();
        OplusCompactWindowManager.getInstance().initmCompactModeFlag(this.mCompactModeFlag, appInfo.packageName);
        Log.d(TAG, "initCompactApplicationInfo CompactMode: " + OplusCompactWindowManager.flagToString(this.mCompactModeFlag) + ", NormalMode: " + OplusCompactWindowManager.flagToString(this.mNormalModeFlag));
    }

    public void updateCustomDarkModeForWechat(Configuration targetConfig, Configuration threadConfig, ResourcesImpl targetImpl, String currentProcessName) {
        Configuration baseConfig;
        int uiModeSetting;
        if (WE_CHAT_PKG.equals(currentProcessName) && supportCompactWindow() && (baseConfig = targetImpl.mResourcesImplExt.getConfiguration()) != null && ActivityThread.currentApplication() != null && ActivityThread.currentApplication().getApplicationContext() != null && !Process.isIsolated()) {
            int currentUser = ActivityThread.currentApplication().getApplicationContext().getUserId();
            boolean shouldModifyConfig = ((baseConfig.uiMode & 48) == 32 && (threadConfig.uiMode & 48) == 16 && (targetConfig.uiMode & 48) == 16) || ((baseConfig.uiMode & 48) == 16 && (threadConfig.uiMode & 48) == 32 && (targetConfig.uiMode & 48) == 32);
            if (shouldModifyConfig && (uiModeSetting = Settings.System.getIntForUser(ActivityThread.currentApplication().getApplicationContext().getContentResolver(), CUSTOM_UI_MODE_KEY, -1, currentUser)) != -1 && 2 != uiModeSetting) {
                targetConfig.uiMode = baseConfig.uiMode;
            }
        }
    }

    private boolean supportCompactWindow() {
        if (sSupportCompactWindow == null) {
            sSupportCompactWindow = Boolean.valueOf(OplusFeatureConfigManager.getInstance().hasFeature(IOplusFeatureConfigList.FEATURE_WINDOW_COMPACT) || OplusFeatureConfigManager.getInstance().hasFeature(IOplusFeatureConfigList.FEATURE_WINDOW_COMPACT_LITE));
        }
        return sSupportCompactWindow.booleanValue();
    }

    private boolean isSupportReviseSquareDisplayOrientation() {
        if (sSupportReviseSquareDisplayOrientation == null) {
            sSupportReviseSquareDisplayOrientation = Boolean.valueOf(OplusFeatureConfigManager.getInstance().hasFeature(IOplusFeatureConfigList.FEATURE_REVISE_SQUARE_DISPLAY_ORIENTATION));
        }
        return sSupportReviseSquareDisplayOrientation.booleanValue();
    }

    public void updateFoldStateForApplication() {
        if (!ActivityThread.isSystem() && ActivityThread.currentApplication() != null && ActivityThread.currentApplication().getApplicationContext() != null && isSupportReviseSquareDisplayOrientation() && OplusCompactWindowManager.isBlockSensor(this.mNormalModeFlag) && !Process.isIsolated()) {
            this.mSystemFoldingMode = Settings.Global.getInt(ActivityThread.currentApplication().getApplicationContext().getContentResolver(), "oplus_system_folding_mode", 0);
            if (DEBUG.booleanValue()) {
                Log.i(TAG, "updateFoldStateForApplication SystemFoldingMode: " + this.mSystemFoldingMode + "; uid = " + Process.myUid() + "; isIsolated = " + Process.isIsolated() + "; packageName " + ActivityThread.currentProcessName());
            }
        }
    }

    public boolean canOverrideConfig(Configuration config, Configuration mOverrideConfiguration) {
        return (DEBUG_ADJUST_CONFIG_V2 || DEBUG_COMPACT_CONFIG || !checkProcess() || config == null || mOverrideConfiguration == null || config.windowConfiguration.getWindowingMode() != 120 || mOverrideConfiguration.windowConfiguration.getWindowingMode() == 120) ? false : true;
    }

    public void updateAppBoundsForComapctWindowIfNeed(Configuration config, DisplayMetrics displayMetrics, boolean forceUpdateConfig) {
        Rect maxBounds;
        if (DEBUG_ADJUST_CONFIG_V2 || DEBUG_COMPACT_CONFIG || !OplusCompactWindowManager.isModeBuying() || !checkProcess() || config == null || displayMetrics == null) {
            return;
        }
        if (forceUpdateConfig) {
            config.windowConfiguration.setWindowingMode(120);
        }
        if (config.windowConfiguration.getWindowingMode() != 120) {
            return;
        }
        if ((isTablet() && displayMetrics.widthPixels < displayMetrics.heightPixels) || (maxBounds = config.windowConfiguration.getMaxBounds()) == null || maxBounds.width() <= 0) {
            return;
        }
        int width = (displayMetrics.widthPixels / 2) - 1;
        Rect appbounds = config.windowConfiguration.getAppBounds();
        Rect bounds = config.windowConfiguration.getBounds();
        if (appbounds == null || bounds == null || width <= 0 || displayMetrics.widthPixels < appbounds.width() || bounds.width() / displayMetrics.density < 550.0f) {
            return;
        }
        adjustBoundsWidth(appbounds, width, displayMetrics.widthPixels);
        adjustBoundsWidth(bounds, width, displayMetrics.widthPixels);
        adjustBoundsWidth(maxBounds, width, displayMetrics.widthPixels);
        if (DEBUG.booleanValue()) {
            Log.d(TAG, "updateAppBoundsForComapctWindowIfNeed adjusted width: " + appbounds.width());
        }
    }

    private void adjustBoundsWidth(Rect appbounds, int width, int maxBounds) {
        appbounds.right = appbounds.left + width;
        if (appbounds.right > maxBounds) {
            appbounds.right = maxBounds;
        }
    }

    private boolean checkProcess() {
        Application app = ActivityThread.currentApplication();
        if (app == null || BLACK_PACKAGES_FOR_COMPACT_WINDOW_ADJUSTMENT.contains(app.getApplicationInfo().packageName)) {
            return false;
        }
        return true;
    }

    public void registerDeviceStateCallBack(ApplicationInfo appInfo, Context context) {
        DeviceStateManager deviceStateManager;
        if (context != null && "com.android.settings".equals(appInfo.packageName) && (deviceStateManager = (DeviceStateManager) context.getSystemService("device_state")) != null) {
            deviceStateManager.registerCallback(ConcurrentUtils.DIRECT_EXECUTOR, new DeviceStateManager.DeviceStateCallback() { // from class: android.app.OplusCompactWindowAppManager$$ExternalSyntheticLambda0
                public final void onStateChanged(int i) {
                    OplusCompactWindowAppManager.this.setDeviceState(i);
                }
            });
        }
    }

    public void setDeviceState(int state) {
        sDeviceState = state;
    }

    public int getDeviceState() {
        return sDeviceState;
    }
}
