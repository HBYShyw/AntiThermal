package android.app;

import android.common.OplusFeatureCache;
import android.content.Context;
import android.content.res.CompatibilityInfo;
import android.content.res.Configuration;
import android.content.res.IResourcesImplExt;
import android.content.res.OplusBaseConfiguration;
import android.content.res.Resources;
import android.content.res.ResourcesImpl;
import android.content.res.ResourcesKey;
import android.hardware.devicestate.DeviceStateManager;
import android.os.IBinder;
import android.os.LocaleList;
import android.os.UserHandle;
import android.text.TextUtils;
import android.util.ArrayMap;
import android.util.DisplayMetrics;
import android.util.IndentingPrintWriter;
import android.util.Log;
import android.util.Slog;
import android.view.DisplayAdjustments;
import android.view.autolayout.IOplusAutoLayoutManager;
import com.android.internal.util.MimeIconUtils;
import com.google.android.collect.Sets;
import com.oplus.compactwindow.OplusCompactWindowManager;
import com.oplus.util.OplusTypeCastingHelper;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.function.Predicate;
import java.util.stream.Stream;

/* loaded from: classes.dex */
public class ResourcesManagerExtImpl implements IResourcesManagerExt {
    private static final String NAVIGATION_OVERLAY = "/product/overlay/NavigationBarMode3Button/NavigationBarMode3ButtonOverlay.apk";
    private static final String PKG_SYSTEMUI = "com.android.systemui";
    private static final String SYSTEM_RES_PATH = "/system/framework/framework-res.apk";
    private static final String TAG = "ResourcesManagerExtImpl";
    private String mPackageName;
    private ResourcesManager mResourcesManager;
    private static final HashSet<String> PKG_FOR_CONFIG_DISPATCH_LATER = Sets.newHashSet(new String[]{"com.coloros.alarmclock", "com.baidu.input_oppo"});
    private static final HashSet CONFIG_CHECK_APPS = Sets.newHashSet(new String[]{"com.coloros.alarmclock", "com.android.launcher", "com.android.systemui"});
    private static String sCacheList = "";
    private IOplusCompactWindowAppManager mCompactWindowAppManager = OplusFeatureCache.getOrCreate(IOplusCompactWindowAppManager.DEFAULT, new Object[0]);
    private ArrayMap<String, Object> mSupplierCache = new ArrayMap<>();

    public ResourcesManagerExtImpl(Object base) {
        this.mResourcesManager = (ResourcesManager) base;
    }

    public void updateResourcesForActivity(String packageName, IBinder activityToken, Configuration overrideConfig, int displayId) {
        synchronized (this) {
            this.mPackageName = packageName;
            this.mResourcesManager.updateResourcesForActivity(activityToken, overrideConfig, displayId);
        }
    }

    public void redirectResourcesToNewImplLocked(Resources r, ResourcesImpl impl, boolean update) {
        IResourcesImplExt resourcesImplExt;
        String name = null;
        if (r != null && r.getImpl() != null && (resourcesImplExt = r.getImpl().mResourcesImplExt) != null) {
            name = resourcesImplExt.getPackageName();
        }
        if (impl != null && !TextUtils.isEmpty(name)) {
            impl.mResourcesImplExt.init(name);
        }
    }

    public void applyConfigurationToResources(Configuration config, int change) {
        ((IOplusCommonInjector) OplusFeatureCache.getOrCreate(IOplusCommonInjector.DEFAULT, new Object[0])).applyConfigurationToResourcesForResourcesManager(config, change);
    }

    public void updateCompactWindowConfigToApplicationResourcesImpl(Configuration configuration, ResourcesImpl resourcesImpl) {
        IOplusCompactWindowAppManager iOplusCompactWindowAppManager = this.mCompactWindowAppManager;
        if (iOplusCompactWindowAppManager != null) {
            iOplusCompactWindowAppManager.updateCompactWindowConfigToApplicationResourcesImpl(configuration, resourcesImpl);
        }
    }

    public void setCompactWindowDisplayAdjustment(ResourcesImpl impl, Configuration oldOverrideConfig, Configuration newOverrideConfig) {
        IOplusCompactWindowAppManager iOplusCompactWindowAppManager = this.mCompactWindowAppManager;
        if (iOplusCompactWindowAppManager != null) {
            iOplusCompactWindowAppManager.setCompactWindowDisplayAdjustment(impl, oldOverrideConfig, newOverrideConfig);
        }
    }

    public void updateCustomDarkModeForWechat(Configuration targetConfig, Configuration theadConfig, ResourcesImpl targetImpl, String currentProcessName) {
        IOplusCompactWindowAppManager iOplusCompactWindowAppManager = this.mCompactWindowAppManager;
        if (iOplusCompactWindowAppManager != null) {
            iOplusCompactWindowAppManager.updateCustomDarkModeForWechat(targetConfig, theadConfig, targetImpl, currentProcessName);
        }
    }

    public boolean isPackageInCompatMode() {
        return inOplusCompatMode(this.mResourcesManager.getConfiguration());
    }

    public static boolean inOplusCompatMode(Configuration config) {
        OplusBaseConfiguration baseConfig = (OplusBaseConfiguration) OplusTypeCastingHelper.typeCasting(OplusBaseConfiguration.class, config);
        return (baseConfig == null || baseConfig.mOplusExtraConfiguration == null || (baseConfig.mOplusExtraConfiguration.getFlag() & 2) == 0) ? false : true;
    }

    public boolean canUseOverlayConfiguration(ResourcesKey resourcesKey, Configuration configuration) {
        Configuration newconfig = resourcesKey.mOverrideConfiguration;
        if (updateConfigForComapctAndEmbeddedWindow(newconfig, configuration) && newconfig.getLayoutDirection() != configuration.getLayoutDirection()) {
            return false;
        }
        if ((!ActivityThread.currentActivityThread().mSystemThread && !PKG_FOR_CONFIG_DISPATCH_LATER.contains(ActivityThread.currentPackageName())) || TextUtils.isEmpty(resourcesKey.mResDir)) {
            return true;
        }
        Configuration overlayConfig = resourcesKey.mOverrideConfiguration;
        int diff = overlayConfig.diff(configuration);
        if (SYSTEM_RES_PATH.equals(resourcesKey.mResDir)) {
            if ((diff & 4096) == 0) {
                return true;
            }
            if (resourcesKey.mOverlayPaths == null) {
                return false;
            }
            overlayConfig.densityDpi = 0;
            Stream<String> stream = Arrays.stream(resourcesKey.mOverlayPaths);
            if (overlayConfig.toString().equals(Configuration.EMPTY.toString()) || stream.filter(new Predicate() { // from class: android.app.ResourcesManagerExtImpl$$ExternalSyntheticLambda0
                @Override // java.util.function.Predicate
                public final boolean test(Object obj) {
                    boolean equals;
                    equals = ResourcesManagerExtImpl.NAVIGATION_OVERLAY.equals((String) obj);
                    return equals;
                }
            }).findAny().isPresent()) {
                return false;
            }
        }
        if ((diff & 4) != 0) {
            LocaleList appLocale = this.mResourcesManager.getConfiguration().getLocales();
            LocaleList configLocale = configuration.getLocales();
            LocaleList overrideLocale = overlayConfig.getLocales();
            if (!overrideLocale.isEmpty() && !configLocale.isEmpty() && !overrideLocale.isEmpty() && !overrideLocale.get(0).equals(configLocale.get(0)) && !overrideLocale.get(0).equals(appLocale.get(0))) {
                resourcesKey.mOverrideConfiguration.setLocales(configLocale);
            }
        }
        if ((diff & 512) != 0) {
            int appNight = this.mResourcesManager.getConfiguration().uiMode;
            int configNight = configuration.uiMode;
            int overrideNight = overlayConfig.uiMode;
            if (appNight != 0 && configNight != 0 && overrideNight != 0 && (appNight & 48) != (overrideNight & 48) && (configNight & 48) != (overrideNight & 48)) {
                resourcesKey.mOverrideConfiguration.uiMode = configNight;
            }
        }
        return true;
    }

    public boolean updateConfigForComapctAndEmbeddedWindow(Configuration newconfig, Configuration oldconfig) {
        return (!OplusCompactWindowManager.FEATURE_OPLUS_EMBEDDING || newconfig == null || oldconfig == null || DeviceStateManager.isFoldedDeviceState(this.mCompactWindowAppManager.getDeviceState()) || !"com.android.settings".equals(ActivityThread.currentPackageName())) ? false : true;
    }

    public DisplayMetrics hookGetDisplayMetrics(DisplayMetrics originalDisplayMetrics) {
        return getOplusAutoLayoutManager().getAutoLayoutDisplayMetrics(originalDisplayMetrics);
    }

    private IOplusAutoLayoutManager getOplusAutoLayoutManager() {
        return (IOplusAutoLayoutManager) OplusFeatureCache.getOrCreate(IOplusAutoLayoutManager.mDefault, new Object[0]);
    }

    public boolean canOverrideConfig(Configuration config, Configuration mOverrideConfiguration) {
        IOplusCompactWindowAppManager iOplusCompactWindowAppManager = this.mCompactWindowAppManager;
        if (iOplusCompactWindowAppManager != null) {
            return iOplusCompactWindowAppManager.canOverrideConfig(config, mOverrideConfiguration);
        }
        return false;
    }

    public void updateAppBoundsForComapctWindowIfNeed(Configuration config, DisplayMetrics displayMetrics, boolean forceUpdateConfig) {
        IOplusCompactWindowAppManager iOplusCompactWindowAppManager = this.mCompactWindowAppManager;
        if (iOplusCompactWindowAppManager != null) {
            iOplusCompactWindowAppManager.updateAppBoundsForComapctWindowIfNeed(config, displayMetrics, forceUpdateConfig);
        }
    }

    public void checkAppConfigChanged(Context context, Configuration newConfig) {
        Context appCxt;
        if (context == null || !PKG_FOR_CONFIG_DISPATCH_LATER.contains(context.getPackageName())) {
            return;
        }
        if (context instanceof Application) {
            appCxt = context;
        } else {
            appCxt = context.getApplicationContext();
        }
        if (appCxt == null || appCxt.getResources() == null) {
            return;
        }
        Configuration appConfig = appCxt.getResources().getConfiguration();
        int diff = appConfig.diff(newConfig);
        if ((diff & 512) != 0) {
            int appNight = appConfig.uiMode;
            int newConfigNight = newConfig.uiMode;
            int resConfigNight = this.mResourcesManager.getConfiguration().uiMode;
            if (appNight != 0 && newConfigNight != 0 && resConfigNight != 0 && (appNight & 48) == (resConfigNight & 48) && (appNight & 48) != (newConfigNight & 48)) {
                Configuration overrideConfig = new Configuration(appConfig);
                overrideConfig.uiMode = newConfig.uiMode;
                appCxt.getResources().updateConfiguration(overrideConfig, null);
            }
        }
    }

    public ResourcesImpl getApplicationContextResImpl() {
        Application application = ActivityThread.currentApplication();
        boolean isVaildRes = (application == null || application.getResources() == null) ? false : true;
        if (isVaildRes) {
            return application.getResources().getImpl();
        }
        return null;
    }

    public void forceUpdateAppContextResource(Application application, Configuration newConfig, CompatibilityInfo compat) {
        Configuration oldConfig = application.getResources().getConfiguration();
        if (oldConfig == null || newConfig == null || !CONFIG_CHECK_APPS.contains(application.getPackageName())) {
            return;
        }
        int oldWindowMode = oldConfig.windowConfiguration.getWindowingMode();
        int newWindowMode = newConfig.windowConfiguration.getWindowingMode();
        if (oldWindowMode == 1 || oldWindowMode == 0) {
            if (newWindowMode == 1 || newWindowMode == 0) {
                int diff = oldConfig.diffPublicOnly(newConfig);
                if (diff == 0) {
                    return;
                }
                ResourcesImpl impl = application.getResources().getImpl();
                DisplayAdjustments daj = impl.getDisplayAdjustments();
                if (compat != null) {
                    daj = new DisplayAdjustments(daj);
                    daj.setCompatibilityInfo(compat);
                }
                daj.setConfiguration(newConfig);
                DisplayMetrics dm = ResourcesManager.getInstance().getDisplayMetrics(application.getDisplayId(), daj);
                impl.updateConfiguration(newConfig, dm, compat);
                Log.i(TAG, "Not found in map, forceUpdateAppContextResource appContexImp: " + impl + ", package: " + application.getPackageName() + ", displayid: " + application.getDisplayId());
            }
        }
    }

    private void updateResourcesAndMetrics(Configuration currentAppConfig, DisplayMetrics metrics) {
        if (currentAppConfig == null || metrics == null) {
            Slog.d(TAG, "currentAppConfig or metrics is null.");
            return;
        }
        this.mResourcesManager.getConfiguration().setMultiWindowConfigTo(currentAppConfig);
        Resources.updateSystemConfiguration(currentAppConfig, metrics, null);
        int width = currentAppConfig.windowConfiguration.getAppBounds().width();
        metrics.widthPixels = width;
        metrics.noncompatWidthPixels = width;
        int height = currentAppConfig.windowConfiguration.getAppBounds().height();
        metrics.heightPixels = height;
        metrics.noncompatHeightPixels = height;
        ApplicationPackageManager.configurationChanged();
        MimeIconUtils.clearCache();
    }

    private void updateResourcesConfiguration(Context app, DisplayMetrics metrics, Configuration config, ArrayList<WeakReference<Resources>> resourcesReferences) {
        if (app == null || metrics == null || config == null || resourcesReferences == null) {
            Slog.d(TAG, "app or metrics or config is null.");
            return;
        }
        int refCount = resourcesReferences.size();
        boolean isAppResImplUpdated = false;
        Resources appRes = app.getResources();
        for (int i = 0; i < refCount; i++) {
            Resources resources = resourcesReferences.get(i).get();
            if (resources != null) {
                if (appRes.getImpl() == resources.getImpl()) {
                    isAppResImplUpdated = true;
                }
                resources.updateConfiguration(config, metrics, null);
            }
        }
        if (!isAppResImplUpdated) {
            appRes.updateConfiguration(config, metrics, null);
        }
    }

    private DisplayMetrics getDisplayMetrics(Context app, int displayId) {
        if (app == null) {
            return null;
        }
        DisplayMetrics metrics = app.getResources().getDisplayMetrics();
        if (app.getDisplayId() != displayId) {
            app.updateDisplay(displayId);
            if (displayId == 0) {
                DisplayMetrics metrics2 = this.mResourcesManager.getDisplayMetrics();
                return metrics2;
            }
            DisplayMetrics metrics3 = this.mResourcesManager.getDisplayMetrics(displayId, app.getResources().getDisplayAdjustments());
            return metrics3;
        }
        return metrics;
    }

    public void applyConfigurationToAppResourcesLocked(Context app, int displayId, Configuration config, ArrayList<WeakReference<Resources>> resourcesReferences) {
        Configuration currentAppConfig;
        try {
            if (!ActivityThread.isSystem() && !"com.android.systemui".equals(app.getPackageName()) && UserHandle.getAppId(app.getApplicationInfo().uid) != 1000) {
                if (app != null && config != null && config.windowConfiguration.getAppBounds() != null && (currentAppConfig = app.getResources().getConfiguration()) != null && currentAppConfig.windowConfiguration.getAppBounds() != null) {
                    int currentDisplayId = displayId;
                    if (currentDisplayId == -1) {
                        currentDisplayId = 0;
                    }
                    if (currentAppConfig.windowConfiguration.getAppBounds().equals(config.windowConfiguration.getAppBounds()) && app.getDisplayId() == currentDisplayId && currentAppConfig.orientation == config.orientation) {
                        Slog.d(TAG, "applyConfigurationToAppResourcesLocked app.getDisplayId() return callback.displayId:" + displayId);
                        return;
                    }
                    if (app.getDisplayId() == currentDisplayId && OplusCompactWindowAppManager.DEBUG_ADJUST_CONFIG_V2 && config.windowConfiguration.getWindowingMode() == 120) {
                        int densityDpi = currentAppConfig.densityDpi;
                        currentAppConfig.setMultiWindowConfigTo(config);
                        currentAppConfig.densityDpi = densityDpi;
                        DisplayMetrics metrics = getDisplayMetrics(app, currentDisplayId);
                        updateResourcesAndMetrics(currentAppConfig, metrics);
                        updateResourcesConfiguration(app, metrics, currentAppConfig, resourcesReferences);
                        return;
                    }
                    int densityDpi2 = app.getDisplayId();
                    if (densityDpi2 == currentDisplayId && currentDisplayId == 0) {
                        Slog.d(TAG, "applyConfigurationToAppResourcesLocked displayId is DEFAULT return.");
                        return;
                    }
                    if (!OplusActivityThreadExtImpl.isMirageWindowDisplayId(currentDisplayId) && !OplusActivityThreadExtImpl.isMirageWindowDisplayId(app.getDisplayId())) {
                        Slog.d(TAG, "applyConfigurationToAppResourcesLocked displayId is bad.");
                        return;
                    }
                    currentAppConfig.setMultiWindowConfigTo(config);
                    DisplayMetrics metrics2 = getDisplayMetrics(app, currentDisplayId);
                    updateResourcesAndMetrics(currentAppConfig, metrics2);
                    updateResourcesConfiguration(app, metrics2, currentAppConfig, resourcesReferences);
                    return;
                }
                Slog.d(TAG, "config or app is null.");
            }
        } catch (Exception e) {
            Slog.i(TAG, "applyConfigurationToAppResourcesLocked fail");
        }
    }

    public Object getCachedSupplier(String apkPath) {
        if (this.mSupplierCache.get(apkPath) != null) {
            Log.d(TAG, "got cache for package " + apkPath);
            return this.mSupplierCache.get(apkPath);
        }
        return null;
    }

    public void cacheSupplier(String apkPath, Object object) {
        String[] cacheList = sCacheList.split(",");
        if (cacheList == null) {
            return;
        }
        for (String needCachePackage : cacheList) {
            if (!TextUtils.isEmpty(needCachePackage) && apkPath.contains(needCachePackage) && this.mSupplierCache.get(apkPath) == null) {
                this.mSupplierCache.put(apkPath, object);
                Log.d(TAG, "cache ApkAsset " + needCachePackage);
            }
        }
    }

    public void dumpExtraInfo(IndentingPrintWriter pw, ArrayList<WeakReference<Resources>> arrayList, ArrayMap<ResourcesKey, WeakReference<ResourcesImpl>> arrayMap) {
        ResourcesImpl impl;
        ResourcesManager.getInstance().getWrapper().dumpActivityResources(pw);
        pw.println();
        pw.println("mResourceReferences:");
        Iterator<WeakReference<Resources>> it = arrayList.iterator();
        while (it.hasNext()) {
            WeakReference<Resources> ref = it.next();
            Resources value = ref != null ? ref.get() : null;
            if (value != null) {
                pw.println();
                pw.print(value);
                pw.print(" --> ");
                pw.print(value.getImpl());
            }
        }
        pw.println();
        pw.println("mResourceImpls:");
        for (ResourcesKey key : arrayMap.keySet()) {
            WeakReference<ResourcesImpl> value2 = arrayMap.get(key);
            if (value2 != null && (impl = value2.get()) != null) {
                pw.println();
                pw.println(key);
                pw.println(impl);
                pw.println(impl.mResourcesImplExt.getConfiguration());
                pw.println(impl.mResourcesImplExt.getDisplayMetrics());
            }
        }
    }

    public static void setResourcesCacheList(String cacheList) {
        sCacheList = cacheList;
    }
}
