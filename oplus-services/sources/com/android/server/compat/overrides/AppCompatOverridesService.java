package com.android.server.compat.overrides;

import android.app.compat.PackageOverride;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.hardware.audio.common.V2_0.AudioDevice;
import android.net.Uri;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.provider.DeviceConfig;
import android.util.ArrayMap;
import android.util.ArraySet;
import android.util.Slog;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.compat.CompatibilityOverrideConfig;
import com.android.internal.compat.CompatibilityOverridesByPackageConfig;
import com.android.internal.compat.CompatibilityOverridesToRemoveByPackageConfig;
import com.android.internal.compat.CompatibilityOverridesToRemoveConfig;
import com.android.internal.compat.IPlatformCompat;
import com.android.server.SystemService;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public final class AppCompatOverridesService {
    private static final List<String> SUPPORTED_NAMESPACES = Arrays.asList("app_compat_overrides");
    private static final String TAG = "AppCompatOverridesService";
    private final Context mContext;
    private final List<DeviceConfigListener> mDeviceConfigListeners;
    private final AppCompatOverridesParser mOverridesParser;
    private final PackageManager mPackageManager;
    private final PackageReceiver mPackageReceiver;
    private final IPlatformCompat mPlatformCompat;
    private final List<String> mSupportedNamespaces;

    private AppCompatOverridesService(Context context) {
        this(context, IPlatformCompat.Stub.asInterface(ServiceManager.getService("platform_compat")), SUPPORTED_NAMESPACES);
    }

    @VisibleForTesting
    AppCompatOverridesService(Context context, IPlatformCompat iPlatformCompat, List<String> list) {
        this.mContext = context;
        PackageManager packageManager = context.getPackageManager();
        this.mPackageManager = packageManager;
        this.mPlatformCompat = iPlatformCompat;
        this.mSupportedNamespaces = list;
        this.mOverridesParser = new AppCompatOverridesParser(packageManager);
        byte b = 0;
        this.mPackageReceiver = new PackageReceiver(context);
        this.mDeviceConfigListeners = new ArrayList();
        Iterator<String> it = list.iterator();
        while (it.hasNext()) {
            this.mDeviceConfigListeners.add(new DeviceConfigListener(this.mContext, it.next()));
        }
    }

    public void finalize() {
        unregisterDeviceConfigListeners();
        unregisterPackageReceiver();
    }

    @VisibleForTesting
    void registerDeviceConfigListeners() {
        Iterator<DeviceConfigListener> it = this.mDeviceConfigListeners.iterator();
        while (it.hasNext()) {
            it.next().register();
        }
    }

    private void unregisterDeviceConfigListeners() {
        Iterator<DeviceConfigListener> it = this.mDeviceConfigListeners.iterator();
        while (it.hasNext()) {
            it.next().unregister();
        }
    }

    @VisibleForTesting
    void registerPackageReceiver() {
        this.mPackageReceiver.register();
    }

    private void unregisterPackageReceiver() {
        this.mPackageReceiver.unregister();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void applyAllOverrides(String str, Set<Long> set, Map<String, Set<Long>> map) {
        applyOverrides(DeviceConfig.getProperties(str, new String[0]), set, map);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void applyOverrides(DeviceConfig.Properties properties, Set<Long> set, Map<String, Set<Long>> map) {
        ArraySet<String> arraySet = new ArraySet(properties.getKeyset());
        arraySet.remove("owned_change_ids");
        arraySet.remove("remove_overrides");
        ArrayMap arrayMap = new ArrayMap();
        ArrayMap arrayMap2 = new ArrayMap();
        for (String str : arraySet) {
            Set<Long> orDefault = map.getOrDefault(str, Collections.emptySet());
            Map<Long, PackageOverride> emptyMap = Collections.emptyMap();
            Long versionCodeOrNull = getVersionCodeOrNull(str);
            if (versionCodeOrNull != null) {
                emptyMap = this.mOverridesParser.parsePackageOverrides(properties.getString(str, ""), str, versionCodeOrNull.longValue(), orDefault);
            }
            if (!emptyMap.isEmpty()) {
                arrayMap.put(str, new CompatibilityOverrideConfig(emptyMap));
            }
            ArraySet arraySet2 = new ArraySet();
            for (Long l : set) {
                if (!emptyMap.containsKey(l) && !orDefault.contains(l)) {
                    arraySet2.add(l);
                }
            }
            if (!arraySet2.isEmpty()) {
                arrayMap2.put(str, new CompatibilityOverridesToRemoveConfig(arraySet2));
            }
        }
        putAllPackageOverrides(arrayMap);
        removeAllPackageOverrides(arrayMap2);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void addAllPackageOverrides(String str) {
        Long versionCodeOrNull = getVersionCodeOrNull(str);
        if (versionCodeOrNull == null) {
            return;
        }
        for (String str2 : this.mSupportedNamespaces) {
            putPackageOverrides(str, this.mOverridesParser.parsePackageOverrides(DeviceConfig.getString(str2, str, ""), str, versionCodeOrNull.longValue(), getOverridesToRemove(str2, getOwnedChangeIds(str2)).getOrDefault(str, Collections.emptySet())));
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void removeAllPackageOverrides(String str) {
        for (String str2 : this.mSupportedNamespaces) {
            if (!DeviceConfig.getString(str2, str, "").isEmpty()) {
                removePackageOverrides(str, getOwnedChangeIds(str2));
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void removeOverrides(Map<String, Set<Long>> map) {
        ArrayMap arrayMap = new ArrayMap();
        for (Map.Entry<String, Set<Long>> entry : map.entrySet()) {
            arrayMap.put(entry.getKey(), new CompatibilityOverridesToRemoveConfig(entry.getValue()));
        }
        removeAllPackageOverrides(arrayMap);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public Map<String, Set<Long>> getOverridesToRemove(String str, Set<Long> set) {
        return this.mOverridesParser.parseRemoveOverrides(DeviceConfig.getString(str, "remove_overrides", ""), set);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static Set<Long> getOwnedChangeIds(String str) {
        return AppCompatOverridesParser.parseOwnedChangeIds(DeviceConfig.getString(str, "owned_change_ids", ""));
    }

    private void putAllPackageOverrides(Map<String, CompatibilityOverrideConfig> map) {
        if (map.isEmpty()) {
            return;
        }
        try {
            this.mPlatformCompat.putAllOverridesOnReleaseBuilds(new CompatibilityOverridesByPackageConfig(map));
        } catch (RemoteException e) {
            Slog.e(TAG, "Failed to call IPlatformCompat#putAllOverridesOnReleaseBuilds", e);
        }
    }

    private void putPackageOverrides(String str, Map<Long, PackageOverride> map) {
        if (map.isEmpty()) {
            return;
        }
        try {
            this.mPlatformCompat.putOverridesOnReleaseBuilds(new CompatibilityOverrideConfig(map), str);
        } catch (RemoteException e) {
            Slog.e(TAG, "Failed to call IPlatformCompat#putOverridesOnReleaseBuilds", e);
        }
    }

    private void removeAllPackageOverrides(Map<String, CompatibilityOverridesToRemoveConfig> map) {
        if (map.isEmpty()) {
            return;
        }
        try {
            this.mPlatformCompat.removeAllOverridesOnReleaseBuilds(new CompatibilityOverridesToRemoveByPackageConfig(map));
        } catch (RemoteException e) {
            Slog.e(TAG, "Failed to call IPlatformCompat#removeAllOverridesOnReleaseBuilds", e);
        }
    }

    private void removePackageOverrides(String str, Set<Long> set) {
        if (set.isEmpty()) {
            return;
        }
        try {
            this.mPlatformCompat.removeOverridesOnReleaseBuilds(new CompatibilityOverridesToRemoveConfig(set), str);
        } catch (RemoteException e) {
            Slog.e(TAG, "Failed to call IPlatformCompat#removeOverridesOnReleaseBuilds", e);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isInstalledForAnyUser(String str) {
        return getVersionCodeOrNull(str) != null;
    }

    private Long getVersionCodeOrNull(String str) {
        try {
            return Long.valueOf(this.mPackageManager.getApplicationInfo(str, AudioDevice.OUT_SPEAKER_SAFE).longVersionCode);
        } catch (PackageManager.NameNotFoundException unused) {
            return null;
        }
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public static final class Lifecycle extends SystemService {
        private AppCompatOverridesService mService;

        public Lifecycle(Context context) {
            super(context);
        }

        @Override // com.android.server.SystemService
        public void onStart() {
            AppCompatOverridesService appCompatOverridesService = new AppCompatOverridesService(getContext());
            this.mService = appCompatOverridesService;
            appCompatOverridesService.registerDeviceConfigListeners();
            this.mService.registerPackageReceiver();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public final class DeviceConfigListener implements DeviceConfig.OnPropertiesChangedListener {
        private final Context mContext;
        private final String mNamespace;

        private DeviceConfigListener(Context context, String str) {
            this.mContext = context;
            this.mNamespace = str;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void register() {
            DeviceConfig.addOnPropertiesChangedListener(this.mNamespace, this.mContext.getMainExecutor(), this);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void unregister() {
            DeviceConfig.removeOnPropertiesChangedListener(this);
        }

        public void onPropertiesChanged(DeviceConfig.Properties properties) {
            boolean contains = properties.getKeyset().contains("remove_overrides");
            boolean contains2 = properties.getKeyset().contains("owned_change_ids");
            Set ownedChangeIds = AppCompatOverridesService.getOwnedChangeIds(this.mNamespace);
            Map overridesToRemove = AppCompatOverridesService.this.getOverridesToRemove(this.mNamespace, ownedChangeIds);
            if (contains || contains2) {
                AppCompatOverridesService.this.removeOverrides(overridesToRemove);
            }
            if (contains) {
                AppCompatOverridesService.this.applyAllOverrides(this.mNamespace, ownedChangeIds, overridesToRemove);
            } else {
                AppCompatOverridesService.this.applyOverrides(properties, ownedChangeIds, overridesToRemove);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public final class PackageReceiver extends BroadcastReceiver {
        private final Context mContext;
        private final IntentFilter mIntentFilter;

        private PackageReceiver(Context context) {
            this.mContext = context;
            IntentFilter intentFilter = new IntentFilter();
            this.mIntentFilter = intentFilter;
            intentFilter.addAction("android.intent.action.PACKAGE_ADDED");
            intentFilter.addAction("android.intent.action.PACKAGE_CHANGED");
            intentFilter.addAction("android.intent.action.PACKAGE_REMOVED");
            intentFilter.addDataScheme("package");
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void register() {
            this.mContext.registerReceiverForAllUsers(this, this.mIntentFilter, null, null);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void unregister() {
            this.mContext.unregisterReceiver(this);
        }

        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            Uri data = intent.getData();
            if (data == null) {
                Slog.w(AppCompatOverridesService.TAG, "Failed to get package name in package receiver");
                return;
            }
            String schemeSpecificPart = data.getSchemeSpecificPart();
            String action = intent.getAction();
            if (action == null) {
                Slog.w(AppCompatOverridesService.TAG, "Failed to get action in package receiver");
                return;
            }
            char c = 65535;
            switch (action.hashCode()) {
                case 172491798:
                    if (action.equals("android.intent.action.PACKAGE_CHANGED")) {
                        c = 0;
                        break;
                    }
                    break;
                case 525384130:
                    if (action.equals("android.intent.action.PACKAGE_REMOVED")) {
                        c = 1;
                        break;
                    }
                    break;
                case 1544582882:
                    if (action.equals("android.intent.action.PACKAGE_ADDED")) {
                        c = 2;
                        break;
                    }
                    break;
            }
            switch (c) {
                case 0:
                case 2:
                    AppCompatOverridesService.this.addAllPackageOverrides(schemeSpecificPart);
                    return;
                case 1:
                    if (AppCompatOverridesService.this.isInstalledForAnyUser(schemeSpecificPart)) {
                        return;
                    }
                    AppCompatOverridesService.this.removeAllPackageOverrides(schemeSpecificPart);
                    return;
                default:
                    Slog.w(AppCompatOverridesService.TAG, "Unsupported action in package receiver: " + action);
                    return;
            }
        }
    }
}
