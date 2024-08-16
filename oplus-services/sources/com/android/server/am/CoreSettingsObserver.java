package com.android.server.am;

import android.R;
import android.app.ActivityThread;
import android.content.ContentResolver;
import android.content.Context;
import android.database.ContentObserver;
import android.os.Bundle;
import android.provider.DeviceConfig;
import android.provider.Settings;
import com.android.internal.annotations.VisibleForTesting;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public final class CoreSettingsObserver extends ContentObserver {
    private static final String LOG_TAG = CoreSettingsObserver.class.getSimpleName();
    private static volatile boolean sDeviceConfigContextEntriesLoaded;
    static final List<DeviceConfigEntry> sDeviceConfigEntries;

    @VisibleForTesting
    static final Map<String, Class<?>> sGlobalSettingToTypeMap;

    @VisibleForTesting
    static final Map<String, Class<?>> sSecureSettingToTypeMap;

    @VisibleForTesting
    static final Map<String, Class<?>> sSystemSettingToTypeMap;
    private final ActivityManagerService mActivityManagerService;
    private final Bundle mCoreSettings;

    static {
        HashMap hashMap = new HashMap();
        sSecureSettingToTypeMap = hashMap;
        HashMap hashMap2 = new HashMap();
        sSystemSettingToTypeMap = hashMap2;
        HashMap hashMap3 = new HashMap();
        sGlobalSettingToTypeMap = hashMap3;
        ArrayList arrayList = new ArrayList();
        sDeviceConfigEntries = arrayList;
        Class cls = Integer.TYPE;
        hashMap.put("long_press_timeout", cls);
        hashMap.put("multi_press_timeout", cls);
        hashMap2.put("time_12_24", String.class);
        hashMap3.put("debug_view_attributes", cls);
        hashMap3.put("debug_view_attributes_application_package", String.class);
        hashMap3.put("angle_debug_package", String.class);
        hashMap3.put("angle_gl_driver_all_angle", cls);
        hashMap3.put("angle_gl_driver_selection_pkgs", String.class);
        hashMap3.put("angle_gl_driver_selection_values", String.class);
        hashMap3.put("angle_egl_features", String.class);
        hashMap3.put("show_angle_in_use_dialog_box", String.class);
        hashMap3.put("enable_gpu_debug_layers", cls);
        hashMap3.put("gpu_debug_app", String.class);
        hashMap3.put("gpu_debug_layers", String.class);
        hashMap3.put("gpu_debug_layers_gles", String.class);
        hashMap3.put("gpu_debug_layer_app", String.class);
        hashMap3.put("updatable_driver_all_apps", cls);
        hashMap3.put("updatable_driver_production_opt_in_apps", String.class);
        hashMap3.put("updatable_driver_prerelease_opt_in_apps", String.class);
        hashMap3.put("updatable_driver_production_opt_out_apps", String.class);
        hashMap3.put("updatable_driver_production_denylist", String.class);
        hashMap3.put("updatable_driver_production_allowlist", String.class);
        hashMap3.put("updatable_driver_production_denylists", String.class);
        hashMap3.put("updatable_driver_sphal_libraries", String.class);
        Class cls2 = Boolean.TYPE;
        arrayList.add(new DeviceConfigEntry(IOplusSceneManager.APP_SCENE_WIDGET, "CursorControlFeature__enable_cursor_drag_from_anywhere", "widget__enable_cursor_drag_from_anywhere", cls2, Boolean.TRUE));
        arrayList.add(new DeviceConfigEntry(IOplusSceneManager.APP_SCENE_WIDGET, "CursorControlFeature__min_angle_from_vertical_to_start_cursor_drag", "widget__min_angle_from_vertical_to_start_cursor_drag", cls, 45));
        arrayList.add(new DeviceConfigEntry(IOplusSceneManager.APP_SCENE_WIDGET, "CursorControlFeature__finger_to_cursor_distance", "widget__finger_to_cursor_distance", cls, -1));
        Boolean bool = Boolean.FALSE;
        arrayList.add(new DeviceConfigEntry(IOplusSceneManager.APP_SCENE_WIDGET, "CursorControlFeature__enable_insertion_handle_gestures", "widget__enable_insertion_handle_gestures", cls2, bool));
        arrayList.add(new DeviceConfigEntry(IOplusSceneManager.APP_SCENE_WIDGET, "CursorControlFeature__insertion_handle_delta_height", "widget__insertion_handle_delta_height", cls, 25));
        arrayList.add(new DeviceConfigEntry(IOplusSceneManager.APP_SCENE_WIDGET, "CursorControlFeature__insertion_handle_opacity", "widget__insertion_handle_opacity", cls, 50));
        Class cls3 = Float.TYPE;
        arrayList.add(new DeviceConfigEntry(IOplusSceneManager.APP_SCENE_WIDGET, "CursorControlFeature__line_slop_ratio", "widget__line_slop_ratio", cls3, Float.valueOf(0.5f)));
        arrayList.add(new DeviceConfigEntry(IOplusSceneManager.APP_SCENE_WIDGET, "CursorControlFeature__enable_new_magnifier", "widget__enable_new_magnifier", cls2, bool));
        arrayList.add(new DeviceConfigEntry(IOplusSceneManager.APP_SCENE_WIDGET, "CursorControlFeature__magnifier_zoom_factor", "widget__magnifier_zoom_factor", cls3, Float.valueOf(1.5f)));
        arrayList.add(new DeviceConfigEntry(IOplusSceneManager.APP_SCENE_WIDGET, "CursorControlFeature__magnifier_aspect_ratio", "widget__magnifier_aspect_ratio", cls3, Float.valueOf(5.5f)));
        arrayList.add(new DeviceConfigEntry("text", "TextEditing__enable_new_context_menu", "text__enable_new_context_menu", cls2, bool));
        sDeviceConfigContextEntriesLoaded = false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public static class DeviceConfigEntry<T> {
        String coreSettingKey;
        T defaultValue;
        String flag;
        String namespace;
        Class<T> type;

        DeviceConfigEntry(String str, String str2, String str3, Class<T> cls, T t) {
            this.namespace = str;
            this.flag = str2;
            this.coreSettingKey = str3;
            this.type = cls;
            Objects.requireNonNull(t);
            this.defaultValue = t;
        }
    }

    public CoreSettingsObserver(ActivityManagerService activityManagerService) {
        super(activityManagerService.mHandler);
        this.mCoreSettings = new Bundle();
        if (!sDeviceConfigContextEntriesLoaded) {
            synchronized (sDeviceConfigEntries) {
                if (!sDeviceConfigContextEntriesLoaded) {
                    loadDeviceConfigContextEntries(activityManagerService.mContext);
                    sDeviceConfigContextEntriesLoaded = true;
                }
            }
        }
        this.mActivityManagerService = activityManagerService;
        beginObserveCoreSettings();
        sendCoreSettings();
    }

    private static void loadDeviceConfigContextEntries(Context context) {
        sDeviceConfigEntries.add(new DeviceConfigEntry(IOplusSceneManager.APP_SCENE_WIDGET, "AnalogClockFeature__analog_clock_seconds_hand_fps", "widget__analog_clock_seconds_hand_fps", Integer.TYPE, Integer.valueOf(context.getResources().getInteger(R.integer.config_displayWhiteBalanceBrightnessFilterHorizon))));
    }

    public Bundle getCoreSettingsLocked() {
        return (Bundle) this.mCoreSettings.clone();
    }

    @Override // android.database.ContentObserver
    public void onChange(boolean z) {
        ActivityManagerService activityManagerService = this.mActivityManagerService;
        ActivityManagerService.boostPriorityForLockedSection();
        synchronized (activityManagerService) {
            try {
                sendCoreSettings();
            } catch (Throwable th) {
                ActivityManagerService.resetPriorityAfterLockedSection();
                throw th;
            }
        }
        ActivityManagerService.resetPriorityAfterLockedSection();
    }

    private void sendCoreSettings() {
        populateSettings(this.mCoreSettings, sSecureSettingToTypeMap);
        populateSettings(this.mCoreSettings, sSystemSettingToTypeMap);
        populateSettings(this.mCoreSettings, sGlobalSettingToTypeMap);
        populateSettingsFromDeviceConfig();
        this.mActivityManagerService.onCoreSettingsChange(this.mCoreSettings);
    }

    private void beginObserveCoreSettings() {
        Iterator<String> it = sSecureSettingToTypeMap.keySet().iterator();
        while (it.hasNext()) {
            this.mActivityManagerService.mContext.getContentResolver().registerContentObserver(Settings.Secure.getUriFor(it.next()), false, this);
        }
        Iterator<String> it2 = sSystemSettingToTypeMap.keySet().iterator();
        while (it2.hasNext()) {
            this.mActivityManagerService.mContext.getContentResolver().registerContentObserver(Settings.System.getUriFor(it2.next()), false, this);
        }
        Iterator<String> it3 = sGlobalSettingToTypeMap.keySet().iterator();
        while (it3.hasNext()) {
            this.mActivityManagerService.mContext.getContentResolver().registerContentObserver(Settings.Global.getUriFor(it3.next()), false, this);
        }
        HashSet hashSet = new HashSet();
        for (DeviceConfigEntry deviceConfigEntry : sDeviceConfigEntries) {
            if (!hashSet.contains(deviceConfigEntry.namespace)) {
                DeviceConfig.addOnPropertiesChangedListener(deviceConfigEntry.namespace, ActivityThread.currentApplication().getMainExecutor(), new DeviceConfig.OnPropertiesChangedListener() { // from class: com.android.server.am.CoreSettingsObserver$$ExternalSyntheticLambda0
                    public final void onPropertiesChanged(DeviceConfig.Properties properties) {
                        CoreSettingsObserver.this.lambda$beginObserveCoreSettings$0(properties);
                    }
                });
                hashSet.add(deviceConfigEntry.namespace);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$beginObserveCoreSettings$0(DeviceConfig.Properties properties) {
        onChange(false);
    }

    @VisibleForTesting
    void populateSettings(Bundle bundle, Map<String, Class<?>> map) {
        String string;
        ContentResolver contentResolver = this.mActivityManagerService.mContext.getContentResolver();
        for (Map.Entry<String, Class<?>> entry : map.entrySet()) {
            String key = entry.getKey();
            if (map == sSecureSettingToTypeMap) {
                string = Settings.Secure.getStringForUser(contentResolver, key, contentResolver.getUserId());
            } else if (map == sSystemSettingToTypeMap) {
                string = Settings.System.getStringForUser(contentResolver, key, contentResolver.getUserId());
            } else {
                string = Settings.Global.getString(contentResolver, key);
            }
            if (string == null) {
                bundle.remove(key);
            } else {
                Class<?> value = entry.getValue();
                if (value == String.class) {
                    bundle.putString(key, string);
                } else if (value == Integer.TYPE) {
                    bundle.putInt(key, Integer.parseInt(string));
                } else if (value == Float.TYPE) {
                    bundle.putFloat(key, Float.parseFloat(string));
                } else if (value == Long.TYPE) {
                    bundle.putLong(key, Long.parseLong(string));
                }
            }
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    private void populateSettingsFromDeviceConfig() {
        for (DeviceConfigEntry deviceConfigEntry : sDeviceConfigEntries) {
            Class cls = deviceConfigEntry.type;
            if (cls == String.class) {
                this.mCoreSettings.putString(deviceConfigEntry.coreSettingKey, DeviceConfig.getString(deviceConfigEntry.namespace, deviceConfigEntry.flag, (String) deviceConfigEntry.defaultValue));
            } else if (cls == Integer.TYPE) {
                this.mCoreSettings.putInt(deviceConfigEntry.coreSettingKey, DeviceConfig.getInt(deviceConfigEntry.namespace, deviceConfigEntry.flag, ((Integer) deviceConfigEntry.defaultValue).intValue()));
            } else if (cls == Float.TYPE) {
                this.mCoreSettings.putFloat(deviceConfigEntry.coreSettingKey, DeviceConfig.getFloat(deviceConfigEntry.namespace, deviceConfigEntry.flag, ((Float) deviceConfigEntry.defaultValue).floatValue()));
            } else if (cls == Long.TYPE) {
                this.mCoreSettings.putLong(deviceConfigEntry.coreSettingKey, DeviceConfig.getLong(deviceConfigEntry.namespace, deviceConfigEntry.flag, ((Long) deviceConfigEntry.defaultValue).longValue()));
            } else if (cls == Boolean.TYPE) {
                this.mCoreSettings.putInt(deviceConfigEntry.coreSettingKey, DeviceConfig.getBoolean(deviceConfigEntry.namespace, deviceConfigEntry.flag, ((Boolean) deviceConfigEntry.defaultValue).booleanValue()) ? 1 : 0);
            }
        }
    }
}
