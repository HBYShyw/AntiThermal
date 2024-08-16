package com.android.server;

import android.R;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.PermissionChecker;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.pm.ServiceInfo;
import android.net.NetworkScorerAppData;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import com.android.internal.annotations.VisibleForTesting;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@VisibleForTesting
/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public class NetworkScorerAppManager {
    private final Context mContext;
    private final SettingsFacade mSettingsFacade;
    private static final String TAG = "NetworkScorerAppManager";
    private static final boolean DEBUG = Log.isLoggable(TAG, 3);
    private static final boolean VERBOSE = Log.isLoggable(TAG, 2);

    public NetworkScorerAppManager(Context context) {
        this(context, new SettingsFacade());
    }

    @VisibleForTesting
    public NetworkScorerAppManager(Context context, SettingsFacade settingsFacade) {
        this.mContext = context;
        this.mSettingsFacade = settingsFacade;
    }

    @VisibleForTesting
    public List<NetworkScorerAppData> getAllValidScorers() {
        if (VERBOSE) {
            Log.v(TAG, "getAllValidScorers()");
        }
        PackageManager packageManager = this.mContext.getPackageManager();
        Intent intent = new Intent("android.net.action.RECOMMEND_NETWORKS");
        List<ResolveInfo> queryIntentServices = packageManager.queryIntentServices(intent, 128);
        if (queryIntentServices == null || queryIntentServices.isEmpty()) {
            if (DEBUG) {
                Log.d(TAG, "Found 0 Services able to handle " + intent);
            }
            return Collections.emptyList();
        }
        ArrayList arrayList = new ArrayList();
        for (int i = 0; i < queryIntentServices.size(); i++) {
            ServiceInfo serviceInfo = queryIntentServices.get(i).serviceInfo;
            if (hasPermissions(serviceInfo.applicationInfo.uid, serviceInfo.packageName)) {
                if (VERBOSE) {
                    Log.v(TAG, serviceInfo.packageName + " is a valid scorer/recommender.");
                }
                arrayList.add(new NetworkScorerAppData(serviceInfo.applicationInfo.uid, new ComponentName(serviceInfo.packageName, serviceInfo.name), getRecommendationServiceLabel(serviceInfo, packageManager), findUseOpenWifiNetworksActivity(serviceInfo), getNetworkAvailableNotificationChannelId(serviceInfo)));
            } else if (VERBOSE) {
                Log.v(TAG, serviceInfo.packageName + " is NOT a valid scorer/recommender.");
            }
        }
        return arrayList;
    }

    private String getRecommendationServiceLabel(ServiceInfo serviceInfo, PackageManager packageManager) {
        Bundle bundle = serviceInfo.metaData;
        if (bundle != null) {
            String string = bundle.getString("android.net.scoring.recommendation_service_label");
            if (!TextUtils.isEmpty(string)) {
                return string;
            }
        }
        CharSequence loadLabel = serviceInfo.loadLabel(packageManager);
        if (loadLabel == null) {
            return null;
        }
        return loadLabel.toString();
    }

    private ComponentName findUseOpenWifiNetworksActivity(ServiceInfo serviceInfo) {
        ActivityInfo activityInfo;
        Bundle bundle = serviceInfo.metaData;
        if (bundle == null) {
            if (DEBUG) {
                Log.d(TAG, "No metadata found on " + serviceInfo.getComponentName());
            }
            return null;
        }
        String string = bundle.getString("android.net.wifi.use_open_wifi_package");
        if (TextUtils.isEmpty(string)) {
            if (DEBUG) {
                Log.d(TAG, "No use_open_wifi_package metadata found on " + serviceInfo.getComponentName());
            }
            return null;
        }
        Intent intent = new Intent("android.net.scoring.CUSTOM_ENABLE").setPackage(string);
        ResolveInfo resolveActivity = this.mContext.getPackageManager().resolveActivity(intent, 0);
        if (VERBOSE) {
            Log.d(TAG, "Resolved " + intent + " to " + resolveActivity);
        }
        if (resolveActivity == null || (activityInfo = resolveActivity.activityInfo) == null) {
            return null;
        }
        return activityInfo.getComponentName();
    }

    private static String getNetworkAvailableNotificationChannelId(ServiceInfo serviceInfo) {
        Bundle bundle = serviceInfo.metaData;
        if (bundle == null) {
            if (!DEBUG) {
                return null;
            }
            Log.d(TAG, "No metadata found on " + serviceInfo.getComponentName());
            return null;
        }
        return bundle.getString("android.net.wifi.notification_channel_id_network_available");
    }

    @VisibleForTesting
    public NetworkScorerAppData getActiveScorer() {
        if (getNetworkRecommendationsEnabledSetting() == -1) {
            return null;
        }
        return getScorer(getNetworkRecommendationsPackage());
    }

    private NetworkScorerAppData getScorer(String str) {
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        List<NetworkScorerAppData> allValidScorers = getAllValidScorers();
        for (int i = 0; i < allValidScorers.size(); i++) {
            NetworkScorerAppData networkScorerAppData = allValidScorers.get(i);
            if (networkScorerAppData.getRecommendationServicePackageName().equals(str)) {
                return networkScorerAppData;
            }
        }
        return null;
    }

    private boolean hasPermissions(int i, String str) {
        return hasScoreNetworksPermission(str) && canAccessLocation(i, str);
    }

    private boolean hasScoreNetworksPermission(String str) {
        return this.mContext.getPackageManager().checkPermission("android.permission.SCORE_NETWORKS", str) == 0;
    }

    private boolean canAccessLocation(int i, String str) {
        return isLocationModeEnabled() && PermissionChecker.checkPermissionForPreflight(this.mContext, "android.permission.ACCESS_COARSE_LOCATION", -1, i, str) == 0;
    }

    private boolean isLocationModeEnabled() {
        return this.mSettingsFacade.getSecureInt(this.mContext, "location_mode", 0) != 0;
    }

    @VisibleForTesting
    public boolean setActiveScorer(String str) {
        String networkRecommendationsPackage = getNetworkRecommendationsPackage();
        if (TextUtils.equals(networkRecommendationsPackage, str)) {
            return true;
        }
        if (TextUtils.isEmpty(str)) {
            Log.i(TAG, "Network scorer forced off, was: " + networkRecommendationsPackage);
            setNetworkRecommendationsPackage(null);
            setNetworkRecommendationsEnabledSetting(-1);
            return true;
        }
        if (getScorer(str) != null) {
            Log.i(TAG, "Changing network scorer from " + networkRecommendationsPackage + " to " + str);
            setNetworkRecommendationsPackage(str);
            setNetworkRecommendationsEnabledSetting(1);
            return true;
        }
        Log.w(TAG, "Requested network scorer is not valid: " + str);
        return false;
    }

    @VisibleForTesting
    public void updateState() {
        if (getNetworkRecommendationsEnabledSetting() == -1) {
            if (DEBUG) {
                Log.d(TAG, "Recommendations forced off.");
                return;
            }
            return;
        }
        String networkRecommendationsPackage = getNetworkRecommendationsPackage();
        int i = 1;
        if (getScorer(networkRecommendationsPackage) != null) {
            if (VERBOSE) {
                Log.v(TAG, networkRecommendationsPackage + " is the active scorer.");
            }
            setNetworkRecommendationsEnabledSetting(1);
            return;
        }
        String defaultPackageSetting = getDefaultPackageSetting();
        if (TextUtils.equals(networkRecommendationsPackage, defaultPackageSetting) || getScorer(defaultPackageSetting) == null) {
            i = 0;
        } else {
            if (DEBUG) {
                Log.d(TAG, "Defaulting the network recommendations app to: " + defaultPackageSetting);
            }
            setNetworkRecommendationsPackage(defaultPackageSetting);
        }
        setNetworkRecommendationsEnabledSetting(i);
    }

    @VisibleForTesting
    public void migrateNetworkScorerAppSettingIfNeeded() {
        NetworkScorerAppData activeScorer;
        String string = this.mSettingsFacade.getString(this.mContext, "network_scorer_app");
        if (TextUtils.isEmpty(string) || (activeScorer = getActiveScorer()) == null) {
            return;
        }
        boolean z = DEBUG;
        if (z) {
            Log.d(TAG, "Migrating Settings.Global.NETWORK_SCORER_APP (" + string + ")...");
        }
        ComponentName enableUseOpenWifiActivity = activeScorer.getEnableUseOpenWifiActivity();
        if (TextUtils.isEmpty(this.mSettingsFacade.getString(this.mContext, "use_open_wifi_package")) && enableUseOpenWifiActivity != null && string.equals(enableUseOpenWifiActivity.getPackageName())) {
            this.mSettingsFacade.putString(this.mContext, "use_open_wifi_package", string);
            if (z) {
                Log.d(TAG, "Settings.Global.USE_OPEN_WIFI_PACKAGE set to '" + string + "'.");
            }
        }
        this.mSettingsFacade.putString(this.mContext, "network_scorer_app", null);
        if (z) {
            Log.d(TAG, "Settings.Global.NETWORK_SCORER_APP migration complete.");
            Log.d(TAG, "Settings.Global.USE_OPEN_WIFI_PACKAGE is: '" + this.mSettingsFacade.getString(this.mContext, "use_open_wifi_package") + "'.");
        }
    }

    private String getDefaultPackageSetting() {
        return this.mContext.getResources().getString(R.string.config_pdp_reject_user_authentication_failed);
    }

    private String getNetworkRecommendationsPackage() {
        return this.mSettingsFacade.getString(this.mContext, "network_recommendations_package");
    }

    private void setNetworkRecommendationsPackage(String str) {
        this.mSettingsFacade.putString(this.mContext, "network_recommendations_package", str);
        if (VERBOSE) {
            Log.d(TAG, "network_recommendations_package set to " + str);
        }
    }

    private int getNetworkRecommendationsEnabledSetting() {
        return this.mSettingsFacade.getInt(this.mContext, "network_recommendations_enabled", 0);
    }

    private void setNetworkRecommendationsEnabledSetting(int i) {
        this.mSettingsFacade.putInt(this.mContext, "network_recommendations_enabled", i);
        if (VERBOSE) {
            Log.d(TAG, "network_recommendations_enabled set to " + i);
        }
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public static class SettingsFacade {
        public boolean putString(Context context, String str, String str2) {
            return Settings.Global.putString(context.getContentResolver(), str, str2);
        }

        public String getString(Context context, String str) {
            return Settings.Global.getString(context.getContentResolver(), str);
        }

        public boolean putInt(Context context, String str, int i) {
            return Settings.Global.putInt(context.getContentResolver(), str, i);
        }

        public int getInt(Context context, String str, int i) {
            return Settings.Global.getInt(context.getContentResolver(), str, i);
        }

        public int getSecureInt(Context context, String str, int i) {
            ContentResolver contentResolver = context.getContentResolver();
            return Settings.Secure.getIntForUser(contentResolver, str, i, contentResolver.getUserId());
        }
    }
}
