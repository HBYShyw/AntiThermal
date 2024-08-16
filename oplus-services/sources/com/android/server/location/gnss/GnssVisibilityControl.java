package com.android.server.location.gnss;

import android.R;
import android.annotation.SuppressLint;
import android.app.AppOpsManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.os.Looper;
import android.os.PowerManager;
import android.os.UserHandle;
import android.text.TextUtils;
import android.util.ArrayMap;
import android.util.Log;
import com.android.internal.location.GpsNetInitiatedHandler;
import com.android.internal.notification.SystemNotificationChannels;
import com.android.internal.util.FrameworkStatsLog;
import com.android.server.location.common.OplusLbsFactory;
import com.android.server.location.interfaces.IOplusLBSMainClass;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public class GnssVisibilityControl {
    private static final int ARRAY_MAP_INITIAL_CAPACITY_PROXY_APPS_STATE = 5;
    private static final long EMERGENCY_EXTENSION_FOR_MISMATCH = 128000;
    private static final long LOCATION_ICON_DISPLAY_DURATION_MILLIS = 5000;
    private static final String LOCATION_PERMISSION_NAME = "android.permission.ACCESS_FINE_LOCATION";
    private static final long ON_GPS_ENABLED_CHANGED_TIMEOUT_MILLIS = 3000;
    private static final String TAG = "GnssVisibilityControl";
    private static final String WAKELOCK_KEY = "GnssVisibilityControl";
    private static final long WAKELOCK_TIMEOUT_MILLIS = 60000;
    private final AppOpsManager mAppOps;
    private final Context mContext;
    private final Handler mHandler;
    private boolean mIsGpsEnabled;
    private final GpsNetInitiatedHandler mNiHandler;
    private final PackageManager mPackageManager;
    private final PowerManager.WakeLock mWakeLock;
    private static final boolean DEBUG = Log.isLoggable("GnssVisibilityControl", 3);
    private static final String[] NO_LOCATION_ENABLED_PROXY_APPS = new String[0];
    private ArrayMap<String, ProxyAppState> mProxyAppsState = new ArrayMap<>(5);
    private PackageManager.OnPermissionsChangedListener mOnPermissionsChangedListener = new PackageManager.OnPermissionsChangedListener() { // from class: com.android.server.location.gnss.GnssVisibilityControl$$ExternalSyntheticLambda4
        public final void onPermissionsChanged(int i) {
            GnssVisibilityControl.this.lambda$new$1(i);
        }
    };

    private native boolean native_enable_nfw_location_access(String[] strArr);

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public static final class ProxyAppState {
        private boolean mHasLocationPermission;
        private boolean mIsLocationIconOn;

        private ProxyAppState(boolean z) {
            this.mHasLocationPermission = z;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$1(final int i) {
        runOnHandler(new Runnable() { // from class: com.android.server.location.gnss.GnssVisibilityControl$$ExternalSyntheticLambda2
            @Override // java.lang.Runnable
            public final void run() {
                GnssVisibilityControl.this.lambda$new$0(i);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public GnssVisibilityControl(Context context, Looper looper, GpsNetInitiatedHandler gpsNetInitiatedHandler) {
        this.mContext = context;
        this.mWakeLock = ((PowerManager) context.getSystemService("power")).newWakeLock(1, "GnssVisibilityControl");
        this.mHandler = new Handler(looper);
        this.mNiHandler = gpsNetInitiatedHandler;
        this.mAppOps = (AppOpsManager) context.getSystemService(AppOpsManager.class);
        this.mPackageManager = context.getPackageManager();
        runOnHandler(new Runnable() { // from class: com.android.server.location.gnss.GnssVisibilityControl$$ExternalSyntheticLambda5
            @Override // java.lang.Runnable
            public final void run() {
                GnssVisibilityControl.this.handleInitialize();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void onGpsEnabledChanged(final boolean z) {
        if (this.mHandler.runWithScissors(new Runnable() { // from class: com.android.server.location.gnss.GnssVisibilityControl$$ExternalSyntheticLambda7
            @Override // java.lang.Runnable
            public final void run() {
                GnssVisibilityControl.this.lambda$onGpsEnabledChanged$2(z);
            }
        }, ON_GPS_ENABLED_CHANGED_TIMEOUT_MILLIS) || z) {
            return;
        }
        Log.w("GnssVisibilityControl", "Native call to disable non-framework location access in GNSS HAL may get executed after native_cleanup().");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$reportNfwNotification$3(String str, byte b, String str2, byte b2, String str3, byte b3, boolean z, boolean z2) {
        handleNfwNotification(new NfwNotification(str, b, str2, b2, str3, b3, z, z2));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void reportNfwNotification(final String str, final byte b, final String str2, final byte b2, final String str3, final byte b3, final boolean z, final boolean z2) {
        runOnHandler(new Runnable() { // from class: com.android.server.location.gnss.GnssVisibilityControl$$ExternalSyntheticLambda6
            @Override // java.lang.Runnable
            public final void run() {
                GnssVisibilityControl.this.lambda$reportNfwNotification$3(str, b, str2, b2, str3, b3, z, z2);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void onConfigurationUpdated(GnssConfiguration gnssConfiguration) {
        final List<String> nfwProxyApps = ((IOplusLBSMainClass) OplusLbsFactory.getInstance().getFeature(IOplusLBSMainClass.DEFAULT, this.mContext)).getNfwProxyApps(gnssConfiguration.getProxyApps());
        Log.d("GnssVisibilityControl", "normalizedApps: " + String.join(",", nfwProxyApps));
        runOnHandler(new Runnable() { // from class: com.android.server.location.gnss.GnssVisibilityControl$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                GnssVisibilityControl.this.lambda$onConfigurationUpdated$4(nfwProxyApps);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleInitialize() {
        listenForProxyAppsPackageUpdates();
    }

    private void listenForProxyAppsPackageUpdates() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.intent.action.PACKAGE_ADDED");
        intentFilter.addAction("android.intent.action.PACKAGE_REMOVED");
        intentFilter.addAction("android.intent.action.PACKAGE_REPLACED");
        intentFilter.addAction("android.intent.action.PACKAGE_CHANGED");
        intentFilter.addDataScheme("package");
        this.mContext.registerReceiverAsUser(new BroadcastReceiver() { // from class: com.android.server.location.gnss.GnssVisibilityControl.1
            @Override // android.content.BroadcastReceiver
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                if (action == null) {
                    return;
                }
                char c = 65535;
                switch (action.hashCode()) {
                    case -810471698:
                        if (action.equals("android.intent.action.PACKAGE_REPLACED")) {
                            c = 0;
                            break;
                        }
                        break;
                    case 172491798:
                        if (action.equals("android.intent.action.PACKAGE_CHANGED")) {
                            c = 1;
                            break;
                        }
                        break;
                    case 525384130:
                        if (action.equals("android.intent.action.PACKAGE_REMOVED")) {
                            c = 2;
                            break;
                        }
                        break;
                    case 1544582882:
                        if (action.equals("android.intent.action.PACKAGE_ADDED")) {
                            c = 3;
                            break;
                        }
                        break;
                }
                switch (c) {
                    case 0:
                    case 1:
                    case 2:
                    case 3:
                        GnssVisibilityControl.this.handleProxyAppPackageUpdate(intent.getData().getEncodedSchemeSpecificPart(), action);
                        return;
                    default:
                        return;
                }
            }
        }, UserHandle.ALL, intentFilter, null, this.mHandler);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleProxyAppPackageUpdate(String str, String str2) {
        ProxyAppState proxyAppState = this.mProxyAppsState.get(str);
        if (proxyAppState == null) {
            return;
        }
        if (DEBUG) {
            Log.d("GnssVisibilityControl", "Proxy app " + str + " package changed: " + str2);
        }
        boolean shouldEnableLocationPermissionInGnssHal = shouldEnableLocationPermissionInGnssHal(str);
        if (proxyAppState.mHasLocationPermission != shouldEnableLocationPermissionInGnssHal) {
            Log.i("GnssVisibilityControl", "Proxy app " + str + " location permission changed. IsLocationPermissionEnabled: " + shouldEnableLocationPermissionInGnssHal);
            proxyAppState.mHasLocationPermission = shouldEnableLocationPermissionInGnssHal;
            updateNfwLocationAccessProxyAppsInGnssHal();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* renamed from: handleUpdateProxyApps, reason: merged with bridge method [inline-methods] */
    public void lambda$onConfigurationUpdated$4(List<String> list) {
        if (isProxyAppListUpdated(list)) {
            if (list.isEmpty()) {
                if (this.mProxyAppsState.isEmpty()) {
                    return;
                }
                this.mPackageManager.removeOnPermissionsChangeListener(this.mOnPermissionsChangedListener);
                resetProxyAppsState();
                updateNfwLocationAccessProxyAppsInGnssHal();
                return;
            }
            if (this.mProxyAppsState.isEmpty()) {
                this.mPackageManager.addOnPermissionsChangeListener(this.mOnPermissionsChangedListener);
            } else {
                resetProxyAppsState();
            }
            for (String str : list) {
                this.mProxyAppsState.put(str, new ProxyAppState(shouldEnableLocationPermissionInGnssHal(str)));
            }
            updateNfwLocationAccessProxyAppsInGnssHal();
        }
    }

    private void resetProxyAppsState() {
        for (Map.Entry<String, ProxyAppState> entry : this.mProxyAppsState.entrySet()) {
            ProxyAppState value = entry.getValue();
            if (value.mIsLocationIconOn) {
                this.mHandler.removeCallbacksAndMessages(value);
                ApplicationInfo proxyAppInfo = getProxyAppInfo(entry.getKey());
                if (proxyAppInfo != null) {
                    clearLocationIcon(value, proxyAppInfo.uid, entry.getKey());
                }
            }
        }
        this.mProxyAppsState.clear();
    }

    private boolean isProxyAppListUpdated(List<String> list) {
        if (list.size() != this.mProxyAppsState.size()) {
            return true;
        }
        Iterator<String> it = list.iterator();
        while (it.hasNext()) {
            if (!this.mProxyAppsState.containsKey(it.next())) {
                return true;
            }
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* renamed from: handleGpsEnabledChanged, reason: merged with bridge method [inline-methods] */
    public void lambda$onGpsEnabledChanged$2(boolean z) {
        if (DEBUG) {
            Log.d("GnssVisibilityControl", "handleGpsEnabledChanged, mIsGpsEnabled: " + this.mIsGpsEnabled + ", isGpsEnabled: " + z);
        }
        this.mIsGpsEnabled = z;
        if (!z) {
            disableNfwLocationAccess();
        } else {
            setNfwLocationAccessProxyAppsInGnssHal(getLocationPermissionEnabledProxyApps());
        }
    }

    private void disableNfwLocationAccess() {
        setNfwLocationAccessProxyAppsInGnssHal(NO_LOCATION_ENABLED_PROXY_APPS);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public static class NfwNotification {
        private static final byte NFW_RESPONSE_TYPE_ACCEPTED_LOCATION_PROVIDED = 2;
        private static final byte NFW_RESPONSE_TYPE_ACCEPTED_NO_LOCATION_PROVIDED = 1;
        private static final byte NFW_RESPONSE_TYPE_REJECTED = 0;
        private final boolean mInEmergencyMode;
        private final boolean mIsCachedLocation;
        private final String mOtherProtocolStackName;
        private final byte mProtocolStack;
        private final String mProxyAppPackageName;
        private final byte mRequestor;
        private final String mRequestorId;
        private final byte mResponseType;

        private NfwNotification(String str, byte b, String str2, byte b2, String str3, byte b3, boolean z, boolean z2) {
            this.mProxyAppPackageName = str;
            this.mProtocolStack = b;
            this.mOtherProtocolStackName = str2;
            this.mRequestor = b2;
            this.mRequestorId = str3;
            this.mResponseType = b3;
            this.mInEmergencyMode = z;
            this.mIsCachedLocation = z2;
        }

        @SuppressLint({"DefaultLocale"})
        public String toString() {
            return String.format("{proxyAppPackageName: %s, protocolStack: %d, otherProtocolStackName: %s, requestor: %d, requestorId: %s, responseType: %s, inEmergencyMode: %b, isCachedLocation: %b}", this.mProxyAppPackageName, Byte.valueOf(this.mProtocolStack), this.mOtherProtocolStackName, Byte.valueOf(this.mRequestor), this.mRequestorId, getResponseTypeAsString(), Boolean.valueOf(this.mInEmergencyMode), Boolean.valueOf(this.mIsCachedLocation));
        }

        /* JADX INFO: Access modifiers changed from: private */
        public String getResponseTypeAsString() {
            byte b = this.mResponseType;
            return b != 0 ? b != 1 ? b != 2 ? "<Unknown>" : "ACCEPTED_LOCATION_PROVIDED" : "ACCEPTED_NO_LOCATION_PROVIDED" : "REJECTED";
        }

        /* JADX INFO: Access modifiers changed from: private */
        public boolean isRequestAccepted() {
            return this.mResponseType != 0;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public boolean isLocationProvided() {
            return this.mResponseType == 2;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public boolean isRequestAttributedToProxyApp() {
            return !TextUtils.isEmpty(this.mProxyAppPackageName);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public boolean isEmergencyRequestNotification() {
            return this.mInEmergencyMode && !isRequestAttributedToProxyApp();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* renamed from: handlePermissionsChanged, reason: merged with bridge method [inline-methods] */
    public void lambda$new$0(int i) {
        if (this.mProxyAppsState.isEmpty()) {
            return;
        }
        for (Map.Entry<String, ProxyAppState> entry : this.mProxyAppsState.entrySet()) {
            String key = entry.getKey();
            ApplicationInfo proxyAppInfo = getProxyAppInfo(key);
            if (proxyAppInfo != null && proxyAppInfo.uid == i) {
                boolean shouldEnableLocationPermissionInGnssHal = shouldEnableLocationPermissionInGnssHal(key);
                ProxyAppState value = entry.getValue();
                if (shouldEnableLocationPermissionInGnssHal != value.mHasLocationPermission) {
                    Log.i("GnssVisibilityControl", "Proxy app " + key + " location permission changed. IsLocationPermissionEnabled: " + shouldEnableLocationPermissionInGnssHal);
                    value.mHasLocationPermission = shouldEnableLocationPermissionInGnssHal;
                    updateNfwLocationAccessProxyAppsInGnssHal();
                    return;
                }
                return;
            }
        }
    }

    private ApplicationInfo getProxyAppInfo(String str) {
        try {
            return this.mPackageManager.getApplicationInfo(str, 0);
        } catch (PackageManager.NameNotFoundException unused) {
            if (!DEBUG) {
                return null;
            }
            Log.d("GnssVisibilityControl", "Proxy app " + str + " is not found.");
            return null;
        }
    }

    private boolean shouldEnableLocationPermissionInGnssHal(String str) {
        return isProxyAppInstalled(str) && hasLocationPermission(str);
    }

    private boolean isProxyAppInstalled(String str) {
        ApplicationInfo proxyAppInfo = getProxyAppInfo(str);
        return proxyAppInfo != null && proxyAppInfo.enabled;
    }

    private boolean hasLocationPermission(String str) {
        return this.mPackageManager.checkPermission(LOCATION_PERMISSION_NAME, str) == 0;
    }

    private void updateNfwLocationAccessProxyAppsInGnssHal() {
        if (this.mIsGpsEnabled) {
            setNfwLocationAccessProxyAppsInGnssHal(getLocationPermissionEnabledProxyApps());
        }
    }

    private void setNfwLocationAccessProxyAppsInGnssHal(String[] strArr) {
        String arrays = Arrays.toString(strArr);
        Log.i("GnssVisibilityControl", "Updating non-framework location access proxy apps in the GNSS HAL to: " + arrays);
        if (native_enable_nfw_location_access(strArr)) {
            return;
        }
        Log.e("GnssVisibilityControl", "Failed to update non-framework location access proxy apps in the GNSS HAL to: " + arrays);
    }

    private String[] getLocationPermissionEnabledProxyApps() {
        Iterator<ProxyAppState> it = this.mProxyAppsState.values().iterator();
        int i = 0;
        int i2 = 0;
        while (it.hasNext()) {
            if (it.next().mHasLocationPermission) {
                i2++;
            }
        }
        String[] strArr = new String[i2];
        for (Map.Entry<String, ProxyAppState> entry : this.mProxyAppsState.entrySet()) {
            String key = entry.getKey();
            if (entry.getValue().mHasLocationPermission) {
                strArr[i] = key;
                i++;
            }
        }
        return strArr;
    }

    public boolean hasLocationPermissionEnabledProxyApps() {
        return getLocationPermissionEnabledProxyApps().length > 0;
    }

    private void handleNfwNotification(NfwNotification nfwNotification) {
        boolean z = DEBUG;
        if (z) {
            Log.d("GnssVisibilityControl", "Non-framework location access notification: " + nfwNotification);
        }
        if (nfwNotification.isEmergencyRequestNotification()) {
            handleEmergencyNfwNotification(nfwNotification);
            return;
        }
        String str = nfwNotification.mProxyAppPackageName;
        ProxyAppState proxyAppState = this.mProxyAppsState.get(str);
        boolean isRequestAccepted = nfwNotification.isRequestAccepted();
        boolean isPermissionMismatched = isPermissionMismatched(proxyAppState, nfwNotification);
        logEvent(nfwNotification, isPermissionMismatched);
        if (!nfwNotification.isRequestAttributedToProxyApp()) {
            if (isRequestAccepted) {
                Log.e("GnssVisibilityControl", "ProxyAppPackageName field is not set. AppOps service not notified for notification: " + nfwNotification);
                return;
            }
            if (z) {
                Log.d("GnssVisibilityControl", "Non-framework location request rejected. ProxyAppPackageName field is not set in the notification: " + nfwNotification + ". Number of configured proxy apps: " + this.mProxyAppsState.size());
                return;
            }
            return;
        }
        if (proxyAppState == null) {
            Log.w("GnssVisibilityControl", "Could not find proxy app " + str + " in the value specified for config parameter: NFW_PROXY_APPS. AppOps service not notified for notification: " + nfwNotification);
            return;
        }
        ApplicationInfo proxyAppInfo = getProxyAppInfo(str);
        if (proxyAppInfo == null) {
            Log.e("GnssVisibilityControl", "Proxy app " + str + " is not found. AppOps service not notified for notification: " + nfwNotification);
            return;
        }
        if (nfwNotification.isLocationProvided()) {
            showLocationIcon(proxyAppState, nfwNotification, proxyAppInfo.uid, str);
            this.mAppOps.noteOpNoThrow(1, proxyAppInfo.uid, str);
        }
        if (isPermissionMismatched) {
            Log.w("GnssVisibilityControl", "Permission mismatch. Proxy app " + str + " location permission is set to " + proxyAppState.mHasLocationPermission + " and GNSS HAL enabled is set to " + this.mIsGpsEnabled + " but GNSS non-framework location access response type is " + nfwNotification.getResponseTypeAsString() + " for notification: " + nfwNotification);
        }
    }

    private boolean isPermissionMismatched(ProxyAppState proxyAppState, NfwNotification nfwNotification) {
        boolean isRequestAccepted = nfwNotification.isRequestAccepted();
        return (proxyAppState == null || !this.mIsGpsEnabled) ? isRequestAccepted : proxyAppState.mHasLocationPermission != isRequestAccepted;
    }

    private void showLocationIcon(ProxyAppState proxyAppState, NfwNotification nfwNotification, int i, final String str) {
        boolean z = proxyAppState.mIsLocationIconOn;
        if (!z) {
            if (!updateLocationIcon(true, i, str)) {
                Log.w("GnssVisibilityControl", "Failed to show Location icon for notification: " + nfwNotification);
                return;
            }
            proxyAppState.mIsLocationIconOn = true;
        } else {
            this.mHandler.removeCallbacksAndMessages(proxyAppState);
        }
        if (DEBUG) {
            StringBuilder sb = new StringBuilder();
            sb.append("Location icon on. ");
            sb.append(z ? "Extending" : "Setting");
            sb.append(" icon display timer. Uid: ");
            sb.append(i);
            sb.append(", proxyAppPkgName: ");
            sb.append(str);
            Log.d("GnssVisibilityControl", sb.toString());
        }
        if (this.mHandler.postDelayed(new Runnable() { // from class: com.android.server.location.gnss.GnssVisibilityControl$$ExternalSyntheticLambda1
            @Override // java.lang.Runnable
            public final void run() {
                GnssVisibilityControl.this.lambda$showLocationIcon$5(str);
            }
        }, proxyAppState, LOCATION_ICON_DISPLAY_DURATION_MILLIS)) {
            return;
        }
        clearLocationIcon(proxyAppState, i, str);
        Log.w("GnssVisibilityControl", "Failed to show location icon for the full duration for notification: " + nfwNotification);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* renamed from: handleLocationIconTimeout, reason: merged with bridge method [inline-methods] */
    public void lambda$showLocationIcon$5(String str) {
        ApplicationInfo proxyAppInfo = getProxyAppInfo(str);
        if (proxyAppInfo != null) {
            clearLocationIcon(this.mProxyAppsState.get(str), proxyAppInfo.uid, str);
        }
    }

    private void clearLocationIcon(ProxyAppState proxyAppState, int i, String str) {
        updateLocationIcon(false, i, str);
        if (proxyAppState != null) {
            proxyAppState.mIsLocationIconOn = false;
        }
        if (DEBUG) {
            Log.d("GnssVisibilityControl", "Location icon off. Uid: " + i + ", proxyAppPkgName: " + str);
        }
    }

    private boolean updateLocationIcon(boolean z, int i, String str) {
        if (z) {
            if (this.mAppOps.startOpNoThrow(41, i, str) != 0) {
                return false;
            }
            if (this.mAppOps.startOpNoThrow(42, i, str) == 0) {
                return true;
            }
            this.mAppOps.finishOp(41, i, str);
            return false;
        }
        this.mAppOps.finishOp(41, i, str);
        this.mAppOps.finishOp(42, i, str);
        return true;
    }

    private void handleEmergencyNfwNotification(NfwNotification nfwNotification) {
        boolean z;
        boolean z2 = true;
        if (nfwNotification.isRequestAccepted()) {
            z = false;
        } else {
            Log.e("GnssVisibilityControl", "Emergency non-framework location request incorrectly rejected. Notification: " + nfwNotification);
            z = true;
        }
        if (this.mNiHandler.getInEmergency(EMERGENCY_EXTENSION_FOR_MISMATCH)) {
            z2 = z;
        } else {
            Log.w("GnssVisibilityControl", "Emergency state mismatch. Device currently not in user initiated emergency session. Notification: " + nfwNotification);
        }
        logEvent(nfwNotification, z2);
        if (nfwNotification.isLocationProvided()) {
            postEmergencyLocationUserNotification(nfwNotification);
        }
    }

    private void postEmergencyLocationUserNotification(NfwNotification nfwNotification) {
        NotificationManager notificationManager = (NotificationManager) this.mContext.getSystemService("notification");
        if (notificationManager == null) {
            Log.w("GnssVisibilityControl", "Could not notify user of emergency location request. Notification: " + nfwNotification);
            return;
        }
        notificationManager.notifyAsUser(null, 0, createEmergencyLocationUserNotification(this.mContext), UserHandle.ALL);
    }

    private static Notification createEmergencyLocationUserNotification(Context context) {
        String string = context.getString(R.string.lockscreen_missing_sim_message_short);
        String string2 = context.getString(R.string.lockscreen_access_pattern_start);
        return new Notification.Builder(context, SystemNotificationChannels.NETWORK_STATUS).setSmallIcon(R.drawable.tab_unselected_focused_holo).setWhen(0L).setOngoing(false).setAutoCancel(true).setColor(context.getColor(R.color.system_notification_accent_color)).setDefaults(0).setTicker(string + " (" + string2 + ")").setContentTitle(string).setContentText(string2).build();
    }

    private void logEvent(NfwNotification nfwNotification, boolean z) {
        FrameworkStatsLog.write(131, nfwNotification.mProxyAppPackageName, nfwNotification.mProtocolStack, nfwNotification.mOtherProtocolStackName, nfwNotification.mRequestor, nfwNotification.mRequestorId, nfwNotification.mResponseType, nfwNotification.mInEmergencyMode, nfwNotification.mIsCachedLocation, z);
    }

    private void runOnHandler(Runnable runnable) {
        this.mWakeLock.acquire(WAKELOCK_TIMEOUT_MILLIS);
        if (this.mHandler.post(runEventAndReleaseWakeLock(runnable))) {
            return;
        }
        this.mWakeLock.release();
    }

    private Runnable runEventAndReleaseWakeLock(final Runnable runnable) {
        return new Runnable() { // from class: com.android.server.location.gnss.GnssVisibilityControl$$ExternalSyntheticLambda3
            @Override // java.lang.Runnable
            public final void run() {
                GnssVisibilityControl.this.lambda$runEventAndReleaseWakeLock$6(runnable);
            }
        };
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$runEventAndReleaseWakeLock$6(Runnable runnable) {
        try {
            runnable.run();
        } finally {
            this.mWakeLock.release();
        }
    }
}
