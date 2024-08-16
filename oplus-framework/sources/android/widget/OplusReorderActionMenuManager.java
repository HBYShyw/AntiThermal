package android.widget;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.hardware.devicestate.DeviceStateManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import com.android.internal.util.ConcurrentUtils;
import com.oplus.content.OplusFeatureConfigManager;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;

/* loaded from: classes.dex */
public class OplusReorderActionMenuManager implements IOplusReorderActionMenuManager {
    private static final int DEFAULT_START_ORDER = 100;
    private static final String FEATURE_FOLD = "oplus.hardware.type.fold";
    private static final String FEATURE_TABLET = "oplus.hardware.type.tablet";
    private static final int LARGE_WINDOW_SW_DP = 600;
    private static final String MENU_ENABLE_NAME = "enable";
    private static final String MENU_ENTITIES_NAME = "menuEntities";
    private static final String MENU_LABEL_NAME = "menuName";
    private static final String MENU_TYPE_NAME = "menuType";
    private static final int NOTE_MENU_ORDER = 50;
    private static final String REORDER_ACTION_MENU_ACTION = "com.oplus.action.reorder_action_menu";
    private static final String SUPER_TEXT_PROPERTY_NAME = "supertext_input_entry";
    private static final String TAG = "OplusReorderActionMenuManager";
    private static final String TYPE_VALUE_ACTIVITY = "activity";
    private static final String TYPE_VALUE_SERVICE = "service";
    private static final String ZOOM_WINDOW_MODE_KEY = "android.activity.windowingMode";
    private static final int ZOOM_WINDOW_MODE_VALUE = 100;
    private static int sDeviceState;
    private JSONObject mSuperTextObject;
    private List<ResolveInfo> mSupportedActivities = new ArrayList();
    private List<ResolveInfo> mSupportedServices = new ArrayList();

    @Override // android.widget.IOplusReorderActionMenuManager
    public void onInitializeReorderActionMenu(Menu menu, Context context, TextView textView) {
        initDeviceStateCallback(context);
        if (!textView.isTextEditable()) {
            return;
        }
        String selectedStr = textView.getSelectedText();
        if ((selectedStr != null && selectedStr.length() > 0) || textView.isAnyPasswordInputType() || isAnyForbiddenType(textView)) {
            return;
        }
        loadMenuPropertiesFromSettings(context);
        loadSupportedActivities(context);
        int size = this.mSupportedActivities.size();
        for (int i = 0; i < size; i++) {
            ResolveInfo resolveInfo = this.mSupportedActivities.get(i);
            menu.add(0, 0, i + 100, getLabel(resolveInfo, context)).setIntent(createProcessTextIntentForResolveInfo(resolveInfo, textView)).setShowAsAction(2);
        }
        loadSupportedServices(context);
        int size2 = this.mSupportedServices.size();
        for (int i2 = 0; i2 < size2; i2++) {
            ResolveInfo resolveInfo2 = this.mSupportedServices.get(i2);
            menu.add(0, 0, i2 + 100, getLabel(resolveInfo2, context)).setIntent(createProcessTextIntentForResolveInfoService(resolveInfo2, textView)).setShowAsAction(2);
        }
    }

    @Override // android.widget.IOplusReorderActionMenuManager
    public boolean isOplusReorderActionMenu(Intent intent) {
        return REORDER_ACTION_MENU_ACTION.equals(intent.getAction());
    }

    @Override // android.widget.IOplusReorderActionMenuManager
    public void hookFireIntent(TextView textView, Intent intent) {
        if (TYPE_VALUE_SERVICE.equals(intent.getIdentifier())) {
            textView.getContext().startForegroundService(intent);
            textView.stopTextActionMode();
            return;
        }
        boolean startInZoomMode = false;
        if (!isLargeScreen(textView.getContext())) {
            startInZoomMode = false;
        } else if (isTabletDevice()) {
            startInZoomMode = true;
        } else if (isFoldDevice() && !DeviceStateManager.isFoldedDeviceState(sDeviceState)) {
            startInZoomMode = true;
        }
        if (forceNormalLaunch(intent)) {
            startInZoomMode = false;
        }
        log("package " + intent.getComponent().getPackageName() + " startInZoomMode ? " + startInZoomMode);
        if (startInZoomMode) {
            startActivityInZoomMode(textView, intent, 100);
        } else {
            textView.startActivityForResult(intent, 100);
        }
    }

    @Override // android.widget.IOplusReorderActionMenuManager
    public boolean raiseOplusMenuPriority(int order, CharSequence label, Intent intent, ResolveInfo resolveInfo, Menu menu) {
        if (!"com.coloros.note".equals(resolveInfo.activityInfo.packageName)) {
            return false;
        }
        menu.add(0, 0, 50, label).setIntent(intent).setShowAsAction(1);
        return true;
    }

    private boolean isTabletDevice() {
        return OplusFeatureConfigManager.getInstance().hasFeature("oplus.hardware.type.tablet");
    }

    private boolean isFoldDevice() {
        return OplusFeatureConfigManager.getInstance().hasFeature("oplus.hardware.type.fold");
    }

    private boolean isLargeScreen(Context context) {
        DisplayMetrics metrics = new DisplayMetrics();
        context.getDisplay().getRealMetrics(metrics);
        int swpx = Math.min(metrics.widthPixels, metrics.heightPixels);
        int swdp = (int) (swpx / metrics.density);
        log("swpx = " + swpx + " swdp " + swdp);
        return swdp > 600;
    }

    private boolean forceNormalLaunch(Intent intent) {
        if (intent != null) {
            String packageName = intent.getComponent().getPackageName();
            if ("com.coloros.note".equals(packageName) || "com.nearme.note".equals(packageName) || "com.oneplus.note".equals(packageName)) {
                return true;
            }
            return false;
        }
        return false;
    }

    private CharSequence getLabel(ResolveInfo resolveInfo, Context context) {
        return resolveInfo.loadLabel(context.getPackageManager());
    }

    private Intent createProcessTextIntentForResolveInfo(ResolveInfo info, TextView textView) {
        return createReorderProcessTextIntent().putExtra("android.intent.extra.PROCESS_TEXT_READONLY", !textView.isTextEditable()).setClassName(info.activityInfo.packageName, info.activityInfo.name);
    }

    private Intent createProcessTextIntentForResolveInfoService(ResolveInfo info, TextView textView) {
        return createReorderProcessTextIntent().putExtra("android.intent.extra.PROCESS_TEXT_READONLY", !textView.isTextEditable()).setClassName(info.serviceInfo.packageName, info.serviceInfo.name).setIdentifier(TYPE_VALUE_SERVICE);
    }

    private void loadSupportedActivities(Context context) {
        this.mSupportedActivities.clear();
        if (!context.canStartActivityForResult()) {
            return;
        }
        PackageManager packageManager = context.getPackageManager();
        List<ResolveInfo> unfiltered = packageManager.queryIntentActivities(createReorderProcessTextIntent(), 131072);
        for (ResolveInfo resolveInfo : unfiltered) {
            if (isSupportedActivity(resolveInfo, context)) {
                this.mSupportedActivities.add(resolveInfo);
            }
        }
    }

    private void loadMenuPropertiesFromSettings(Context context) {
        loadSuperTextMenuProperty(context);
    }

    private void loadSuperTextMenuProperty(Context context) {
        try {
            if (this.mSuperTextObject == null) {
                String propertyStr = Settings.System.getString(context.getContentResolver(), SUPER_TEXT_PROPERTY_NAME);
                this.mSuperTextObject = new JSONObject(propertyStr);
            }
        } catch (Exception e) {
            Log.d(TAG, "loadScanTextMenuProperty failed " + e.getMessage());
            this.mSuperTextObject = new JSONObject();
        }
    }

    private void loadSupportedServices(Context context) {
        this.mSupportedServices.clear();
        if (!context.canStartActivityForResult()) {
            return;
        }
        PackageManager packageManager = context.getPackageManager();
        List<ResolveInfo> unfiltered = packageManager.queryIntentServices(createReorderProcessTextIntent(), 131072);
        for (ResolveInfo resolveInfo : unfiltered) {
            if (isSupportedService(resolveInfo, context)) {
                this.mSupportedServices.add(resolveInfo);
            }
        }
    }

    private boolean isSupportedActivity(ResolveInfo info, Context context) {
        return context.getPackageName().equals(info.activityInfo.packageName) || (info.activityInfo.exported && ((info.activityInfo.permission == null || context.checkSelfPermission(info.activityInfo.permission) == 0) && activityMenuEnabledByApp(info, context)));
    }

    private boolean isSupportedService(ResolveInfo info, Context context) {
        return context.getPackageName().equals(info.serviceInfo.packageName) || (info.serviceInfo.exported && ((info.serviceInfo.permission == null || context.checkSelfPermission(info.serviceInfo.permission) == 0) && serviceMenuEnabledByApp(info, context)));
    }

    private boolean activityMenuEnabledByApp(ResolveInfo info, Context context) {
        JSONArray menuEntities;
        JSONObject jSONObject = this.mSuperTextObject;
        if (jSONObject == null || !jSONObject.has(MENU_ENTITIES_NAME)) {
            return false;
        }
        try {
            menuEntities = this.mSuperTextObject.optJSONArray(MENU_ENTITIES_NAME);
        } catch (Exception e) {
            Log.d(TAG, "activityMenuEnabledByApp failed " + e.getMessage());
        }
        if (menuEntities == null) {
            return false;
        }
        for (int i = 0; i < menuEntities.length(); i++) {
            JSONObject menuProperty = menuEntities.getJSONObject(i);
            if (menuProperty.has(MENU_LABEL_NAME) && menuProperty.has(MENU_TYPE_NAME) && menuProperty.has("enable") && info.activityInfo.name.contains(menuProperty.optString(MENU_LABEL_NAME)) && menuProperty.optString(MENU_TYPE_NAME).equals("activity") && menuProperty.optBoolean("enable")) {
                return true;
            }
        }
        return false;
    }

    private boolean serviceMenuEnabledByApp(ResolveInfo info, Context context) {
        JSONArray menuEntities;
        JSONObject jSONObject = this.mSuperTextObject;
        if (jSONObject == null || !jSONObject.has(MENU_ENTITIES_NAME)) {
            return false;
        }
        try {
            menuEntities = this.mSuperTextObject.optJSONArray(MENU_ENTITIES_NAME);
        } catch (Exception e) {
            Log.d(TAG, "serviceMenuEnabledByApp failed " + e.getMessage());
        }
        if (menuEntities == null) {
            return false;
        }
        for (int i = 0; i < menuEntities.length(); i++) {
            JSONObject menuProperty = menuEntities.getJSONObject(i);
            if (menuProperty.has(MENU_LABEL_NAME) && menuProperty.has(MENU_TYPE_NAME) && menuProperty.has("enable") && info.serviceInfo.name.contains(menuProperty.optString(MENU_LABEL_NAME)) && menuProperty.optString(MENU_TYPE_NAME).equals(TYPE_VALUE_SERVICE) && menuProperty.optBoolean("enable")) {
                return true;
            }
        }
        return false;
    }

    private Intent createReorderProcessTextIntent() {
        return new Intent().setAction(REORDER_ACTION_MENU_ACTION).setType("text/plain").setFlags(32);
    }

    private boolean isAnyForbiddenType(TextView textView) {
        int inputType = textView.getInputType();
        int type = inputType & 15;
        if (type == 3 || type == 4 || type == 2) {
            return true;
        }
        return false;
    }

    private void startActivityInZoomMode(TextView textView, Intent intent, int requestCode) {
        String whoIdentifier = "@android:view:" + System.identityHashCode(textView);
        intent.addFlags(268435456);
        Bundle startOptions = new Bundle();
        startOptions.putInt(ZOOM_WINDOW_MODE_KEY, 100);
        textView.getContext().startActivityForResult(whoIdentifier, intent, requestCode, startOptions);
    }

    private void initDeviceStateCallback(Context context) {
        try {
            final DeviceStateManager deviceStateManager = (DeviceStateManager) context.getSystemService("device_state");
            deviceStateManager.registerCallback(ConcurrentUtils.DIRECT_EXECUTOR, new DeviceStateManager.DeviceStateCallback() { // from class: android.widget.OplusReorderActionMenuManager.1
                public void onStateChanged(int state) {
                    Log.d(OplusReorderActionMenuManager.TAG, "onStateChanged " + state);
                    OplusReorderActionMenuManager.sDeviceState = state;
                    deviceStateManager.unregisterCallback(this);
                }
            });
        } catch (Exception e) {
            log("failed to register device state callback " + e.getMessage());
        }
    }

    private void log(String content) {
        Log.d(TAG, content);
    }
}
