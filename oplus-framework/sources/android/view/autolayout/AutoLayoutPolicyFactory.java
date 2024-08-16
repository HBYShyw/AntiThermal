package android.view.autolayout;

import android.app.WindowConfiguration;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.IActivityInfoExt;
import android.content.pm.IApplicationInfoExt;
import android.content.res.Configuration;
import android.hardware.devicestate.DeviceStateManager;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.ArrayMap;
import android.util.Log;
import android.util.Slog;
import android.util.SparseArray;
import com.android.internal.os.ClassLoaderFactory;
import com.oplus.module.loader.Module;
import dalvik.system.PathClassLoader;
import java.lang.reflect.Constructor;
import org.json.JSONArray;
import org.json.JSONObject;
import system.ext.module.loader.ModuleLoader;

/* loaded from: classes.dex */
public class AutoLayoutPolicyFactory {
    private static final String AUTO_LAYOUT_CLASS_NAME = "com.oplus.autolayout.OplusAutoLayout2Manager";
    private static final String AUTO_LAYOUT_JAR_PATH = "/system_ext/framework/autolayout.jar";
    public static final int COMMON_TYPE = 3;
    public static final int DEFAULT_TYPE = -1;
    public static final int NORMAL_TYPE = 1;
    public static final int SPECIAL_TYPE = 4;
    private static final int STATUS_DISMISS = 2;
    private static final int STATUS_ENABLE = 1;
    private static final int UNFOLD_APP_WIDTH = 1500;
    public static final int WIDGET_TYPE = 2;
    private static boolean sHasCover;
    private static boolean sIsAppInAutoLayoutList;
    private static boolean sIsNeedCover;
    private static IOplusAutoLayout2Manager sOplusAutoLayout2MgrImpl;
    private static final ArrayMap<String, PathClassLoader> LOAD_PATH_MAP = new ArrayMap<>();
    private static SparseArray<IAutoLayoutPolicy> sPolicies = new SparseArray<>();
    private static ArrayMap<String, Integer> sChildActivities = new ArrayMap<>();
    private static String sGlobalPolicy = "";
    private static int sStretchPolicy = -1;
    private static int sCurrentPolicy = -1;
    private static int sDeviceState = -1;
    private static int sUnFoldDisplayWidth = 1920;
    private static int sAutoDisplayWidth = 1076;

    private static PathClassLoader createClassLoader(String path, ClassLoader parent) {
        ArrayMap<String, PathClassLoader> arrayMap = LOAD_PATH_MAP;
        if (arrayMap.containsKey(path)) {
            return arrayMap.get(path);
        }
        PathClassLoader pathClassLoader = (PathClassLoader) ClassLoaderFactory.createClassLoader(path, (String) null, (String) null, parent, Build.VERSION.SDK_INT, true, (String) null);
        arrayMap.put(path, pathClassLoader);
        return pathClassLoader;
    }

    public static boolean createAutoLayout2MgrImplIfNeed() {
        if (sOplusAutoLayout2MgrImpl == null) {
            IOplusAutoLayout2Manager iOplusAutoLayout2Manager = (IOplusAutoLayout2Manager) ModuleLoader.type(IOplusAutoLayout2Manager.class).module(Module.AUTOLAYOUT).base(AutoLayoutPolicyFactory.class).create();
            sOplusAutoLayout2MgrImpl = iOplusAutoLayout2Manager;
            if (iOplusAutoLayout2Manager != null) {
                return true;
            }
            try {
                ClassLoader classLoader = createClassLoader(AUTO_LAYOUT_JAR_PATH, AutoLayoutPolicyFactory.class.getClassLoader());
                Class autoLayout2MgrClazz = classLoader.loadClass(AUTO_LAYOUT_CLASS_NAME);
                Constructor constructor = autoLayout2MgrClazz.getConstructor(new Class[0]);
                sOplusAutoLayout2MgrImpl = (IOplusAutoLayout2Manager) constructor.newInstance(new Object[0]);
            } catch (Exception e) {
            }
        }
        return sOplusAutoLayout2MgrImpl != null;
    }

    public static IOplusAutoLayout2Manager getAutoLayout2MgrImpl() {
        return sOplusAutoLayout2MgrImpl;
    }

    public static IAutoLayoutPolicy getPolicy(int type) {
        synchronized (AutoLayoutPolicyFactory.class) {
            IAutoLayoutPolicy policy = sPolicies.get(type);
            if (policy != null) {
                return policy;
            }
            IAutoLayoutPolicy policy2 = createPolicy(type);
            sPolicies.put(type, policy2);
            return policy2;
        }
    }

    private static IAutoLayoutPolicy createPolicy(int type) {
        IAutoLayoutPolicy policy = IAutoLayoutPolicy.DEFAULT;
        if (type == 3) {
            policy = new AutoLayoutCommonPolicy();
        }
        if (type == 4) {
            IAutoLayoutPolicy policy2 = new AutoLayoutSpecialPolicy();
            return policy2;
        }
        return policy;
    }

    public static void setCurrentPolicy(int policy) {
        sCurrentPolicy = policy;
    }

    public static int getCurrentPolicy() {
        return sCurrentPolicy;
    }

    public static void setDeviceState(int state) {
        sDeviceState = state;
    }

    public static int getDeviceState() {
        return sDeviceState;
    }

    public static boolean hasCover() {
        return sHasCover;
    }

    public static int getUnFoldDisplayWidth() {
        return sUnFoldDisplayWidth;
    }

    public static int getAutoDisplayWidth() {
        return sAutoDisplayWidth;
    }

    public static boolean getIsAppInAutoLayoutList() {
        return sIsAppInAutoLayoutList;
    }

    public static boolean getIsNeedCover() {
        return sIsNeedCover;
    }

    public static Integer getActivityPolicy(ActivityInfo activityInfo, Configuration configuration, int reason) {
        boolean isEnableWindowMode = isEnableWindowMode(activityInfo, configuration);
        int activityPolicy = getActivityPolicy(activityInfo.name, reason).intValue();
        if (createAutoLayout2MgrImplIfNeed()) {
            getAutoLayout2MgrImpl().setWindowMode(isEnableWindowMode);
            getAutoLayout2MgrImpl().setActivityPolicy(activityPolicy);
        }
        return Integer.valueOf(isEnableWindowMode ? activityPolicy : -1);
    }

    public static Integer getActivityPolicy(String activityName, int reason) {
        if (sGlobalPolicy.equals(IOplusAutoLayoutManager.GLOBAL)) {
            Log.d("AutoLayout", "GLOBAL: " + activityName + " StretchPolicy " + sStretchPolicy + " " + AutoLayoutDebug.updatePolicyReason(reason));
            return Integer.valueOf(sStretchPolicy);
        }
        if (sGlobalPolicy.equals("activity")) {
            if (sChildActivities.containsKey(activityName)) {
                Integer policy = sChildActivities.get(activityName);
                Log.d("AutoLayout", "ACTIVITY: " + activityName + " activity_policy " + policy + " " + AutoLayoutDebug.updatePolicyReason(reason));
                return policy;
            }
            Log.d("AutoLayout", "Disable ACTIVITY: " + activityName + " activity_policy -1 " + AutoLayoutDebug.updatePolicyReason(reason));
        }
        return -1;
    }

    public static boolean isEnableWindowMode(ActivityInfo activityInfo, Configuration configuration) {
        IActivityInfoExt activityInfoExt = activityInfo.mActivityInfoExt;
        if (activityInfoExt.inOplusCompatMode()) {
            Log.d("AutoLayout", "unable WindowMode OplusCompatMode");
            return false;
        }
        if (DeviceStateManager.isFoldedDeviceState(sDeviceState)) {
            Log.d("AutoLayout", "unable DeviceState" + sDeviceState);
            return false;
        }
        if (configuration != null) {
            WindowConfiguration windowConfiguration = configuration.windowConfiguration;
            int windowMode = windowConfiguration.getWindowingMode();
            if ((windowMode == 1 || windowMode == 0) && windowConfiguration.getAppBounds().width() > 1500) {
                int width = windowConfiguration.getMaxBounds().width();
                sUnFoldDisplayWidth = width;
                sAutoDisplayWidth = width / 2;
                return true;
            }
            Log.d("AutoLayout", "unable WindowMode " + WindowConfiguration.windowingModeToString(windowMode));
        }
        return false;
    }

    public static void initAutoLayoutApplicationInfo(ApplicationInfo appInfo, Context context) {
        IApplicationInfoExt appInfoExt = appInfo == null ? null : appInfo.mApplicationInfoExt;
        if (appInfoExt != null) {
            Bundle bundle = appInfoExt.getAutoLayoutExtraBundle();
            if (bundle != null) {
                boolean autoLayoutEnable = bundle.getInt(IOplusAutoLayoutManager.AUTOLAYOUT_SWITCH_STATUE, 2) == 1;
                boolean needCover = bundle.getBoolean(IOplusAutoLayoutManager.AUTOLAYOUT_NEED_COVER, false);
                sIsNeedCover = needCover;
                Log.d("AutoLayout", "init app info: needCover = " + needCover + " autoLayoutEnable = " + autoLayoutEnable);
                if (autoLayoutEnable) {
                    AutoLayoutUtils.registerDeviceStateCallBack(context);
                    sHasCover = bundle.getBoolean(IOplusAutoLayoutManager.HAS_COVER, false);
                    sGlobalPolicy = bundle.getString("policy", "");
                    sStretchPolicy = AutoLayoutUtils.strToInt(bundle.getString(IOplusAutoLayoutManager.STRETCH_POLICY), -1);
                    String activitiesPolicy = bundle.getString(IOplusAutoLayoutManager.APP_POLICY_NAME, "");
                    String customActivitiesPolicy = bundle.getString(IOplusAutoLayoutManager.CUSTOM_POLICY_NAME, "");
                    try {
                        if (!TextUtils.isEmpty(activitiesPolicy)) {
                            parseActivitiesParams(new JSONArray(activitiesPolicy));
                        }
                        if (!TextUtils.isEmpty(customActivitiesPolicy)) {
                            parseActivitiesParams(new JSONArray(customActivitiesPolicy));
                        }
                        sIsAppInAutoLayoutList = true;
                    } catch (Exception ex) {
                        Log.e("AutoLayout", "parseContent error " + appInfo.processName + " " + ex);
                    }
                    Log.d("AutoLayout", "hookHandleBindApplication: " + appInfo.processName + " mActivitiesPolicy =" + activitiesPolicy + " mCustomActivitiesPolicy =" + customActivitiesPolicy + " sGlobalPolicy =" + sGlobalPolicy + " sStretchPolicy =" + sStretchPolicy);
                    return;
                }
                sIsAppInAutoLayoutList = false;
                return;
            }
            sIsAppInAutoLayoutList = false;
            sIsNeedCover = false;
        }
    }

    public static void parseActivitiesParams(JSONArray entities) {
        if (entities == null || entities.length() <= 0) {
            Slog.d("AutoLayout", "no activity Params");
            return;
        }
        for (int i = 0; i < entities.length(); i++) {
            JSONObject eachEntity = entities.optJSONObject(i);
            if (eachEntity != null) {
                String activityName = eachEntity.optString("activity_name");
                if (eachEntity.has(IOplusAutoLayoutManager.ACTIVITY_POLICY)) {
                    sChildActivities.put(activityName, Integer.valueOf(AutoLayoutUtils.strToInt(eachEntity.optString(IOplusAutoLayoutManager.ACTIVITY_POLICY), -1)));
                }
            }
        }
    }
}
