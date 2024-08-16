package android.app;

import android.common.OplusFeatureCache;
import android.content.ComponentCallbacks2;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.ResourcesImpl;
import android.content.res.ResourcesKey;
import android.util.ArrayMap;
import android.util.Slog;
import android.view.OplusViewMirrorManager;
import com.google.android.collect.Sets;
import com.oplus.darkmode.IOplusDarkModeManager;
import com.oplus.flexiblewindow.FlexibleWindowManager;
import java.lang.ref.WeakReference;
import java.util.HashSet;

/* loaded from: classes.dex */
public class ConfigurationControllerExtImpl implements IConfigurationControllerExt {
    private static final HashSet CONFIG_CHECK_APPS = Sets.newHashSet(new String[]{"com.coloros.alarmclock", "com.android.launcher", "com.android.systemui"});
    private static final String EXSERVICEUI_PKG = "com.oplus.exserviceui";
    private static final int MIRAGE_DISPLAY_ID_BASE = 10000;
    private static final int MIRAGE_TV_DISPLAY_ID = 2020;
    private static final String TAG = "ConfigurationControllerExtImpl";
    private ConfigurationController mConfigurationController;

    public ConfigurationControllerExtImpl(Object base) {
        this.mConfigurationController = null;
        this.mConfigurationController = (ConfigurationController) base;
    }

    public void updateFoldStateForApplication() {
        OplusFeatureCache.getOrCreate(IOplusCompactWindowAppManager.DEFAULT, new Object[0]).updateFoldStateForApplication();
    }

    public boolean hookHandleConfigurationChanged(Application application, Configuration config, int diff, Configuration configuration) {
        ((IOplusCommonInjector) OplusFeatureCache.getOrCreate(IOplusCommonInjector.DEFAULT, new Object[0])).onConfigurationChangedForApplication(application, config);
        return ((IOplusDarkModeManager) OplusFeatureCache.getOrCreate(IOplusDarkModeManager.DEFAULT, new Object[0])).shouldInterceptConfigRelaunch(diff, configuration);
    }

    public boolean handleStateCheck(String packageName, Configuration newConfig, Configuration oldConfig) {
        if (oldConfig == null || (oldConfig.diff(newConfig) & 4) == 0) {
            return true;
        }
        return false;
    }

    public void hookConfigChangeForLocale(Context appCxt, Configuration config) {
        Configuration appConfig = appCxt.getResources().getConfiguration();
        if (appConfig == null || !CONFIG_CHECK_APPS.contains(appCxt.getPackageName())) {
            return;
        }
        int diff = appConfig.diffPublicOnly(config);
        ResourcesImpl impl = appCxt.getResources().getImpl();
        ArrayMap<ResourcesKey, WeakReference<ResourcesImpl>> implMap = ResourcesManager.getInstance().getWrapper().getResourcesImplMap();
        if (diff == 0 || impl == null || implMap == null || implMap.size() == 0) {
            return;
        }
        boolean findAppImpl = false;
        int i = implMap.size();
        while (true) {
            i--;
            if (i < 0) {
                break;
            }
            WeakReference<ResourcesImpl> weakImplRef = implMap.valueAt(i);
            ResourcesImpl r = weakImplRef != null ? weakImplRef.get() : null;
            if (impl.equals(r)) {
                findAppImpl = true;
            }
        }
        if (!findAppImpl) {
            Slog.i(TAG, "app resources impl not found in map, need update this: " + impl + ", package: " + appCxt.getPackageName());
            appCxt.getResources().updateConfiguration(config, null, null);
        }
    }

    public void hookHandleConfigurationChangedForBracket(Application app, Configuration currWinConfig, Configuration newWinConfig) {
        int currWinMode;
        int newWindMode;
        if (currWinConfig != null && newWinConfig != null && app != null && (currWinMode = currWinConfig.windowConfiguration.getWindowingMode()) != (newWindMode = newWinConfig.windowConfiguration.getWindowingMode())) {
            if (newWindMode == 115) {
                OplusViewMirrorManager.getInstance().enterMirrorMode(app.getBasePackageName(), "enter bracket mode");
            } else if (currWinMode == 115 && newWindMode != 0) {
                OplusViewMirrorManager.getInstance().exitMirrorMode(app.getBasePackageName(), "exit bracket mode");
            }
        }
    }

    public void handleConfigurationChangedBeforeApplyResource(Application app, Configuration config) {
        FlexibleWindowManager.getInstance().handleConfigurationChanged(app, config);
    }

    public void onConfigurationChanged(ComponentCallbacks2 cb, Configuration configToReport, Application app) {
        if (isMirageMode(app.getDisplayId())) {
            Configuration tempConfig = new Configuration(configToReport);
            cb.onConfigurationChanged(tempConfig);
            Slog.d(TAG, "performConfigurationChanged: cb： " + cb + "\n configToReport: \n" + configToReport);
            return;
        }
        cb.onConfigurationChanged(configToReport);
    }

    public boolean isMirageMode(int displayid) {
        if (displayid == 2020 || displayid >= 10000) {
            return true;
        }
        return false;
    }
}
