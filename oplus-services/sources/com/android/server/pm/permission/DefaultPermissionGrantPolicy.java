package com.android.server.pm.permission;

import android.R;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManagerInternal;
import android.content.pm.PermissionInfo;
import android.content.pm.ProviderInfo;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.UserHandle;
import android.permission.PermissionManager;
import android.util.ArrayMap;
import android.util.ArraySet;
import android.util.Log;
import android.util.Slog;
import android.util.SparseArray;
import android.util.Xml;
import com.android.internal.util.ArrayUtils;
import com.android.internal.util.XmlUtils;
import com.android.modules.utils.TypedXmlPullParser;
import com.android.server.LocalServices;
import com.android.server.ServiceThread;
import com.android.server.pm.permission.LegacyPermissionManagerInternal;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.IntFunction;
import org.xmlpull.v1.XmlPullParserException;
import system.ext.loader.core.ExtLoader;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public final class DefaultPermissionGrantPolicy {
    private static final String ACTION_TRACK = "com.android.fitness.TRACK";
    private static final Set<String> ACTIVITY_RECOGNITION_PERMISSIONS;
    private static final Set<String> ALWAYS_LOCATION_PERMISSIONS;
    private static final String ATTR_FIXED = "fixed";
    private static final String ATTR_NAME = "name";
    private static final String ATTR_PACKAGE = "package";
    private static final String ATTR_WHITELISTED = "whitelisted";
    private static final String AUDIO_MIME_TYPE = "audio/mpeg";
    private static final Set<String> CALENDAR_PERMISSIONS;
    private static final Set<String> CAMERA_PERMISSIONS;
    private static final Set<String> COARSE_BACKGROUND_LOCATION_PERMISSIONS;
    private static final Set<String> CONTACTS_PERMISSIONS;
    private static final boolean DEBUG = false;
    private static final int DEFAULT_INTENT_QUERY_FLAGS = 794624;
    private static final int DEFAULT_PACKAGE_INFO_QUERY_FLAGS = 536915968;
    private static final Set<String> FINE_LOCATION_PERMISSIONS;
    private static final Set<String> FOREGROUND_LOCATION_PERMISSIONS;
    private static final Set<String> MICROPHONE_PERMISSIONS;
    private static final int MSG_READ_DEFAULT_PERMISSION_EXCEPTIONS = 1;
    private static final Set<String> NEARBY_DEVICES_PERMISSIONS;
    private static final Set<String> NOTIFICATION_PERMISSIONS;
    private static final Set<String> PHONE_PERMISSIONS;
    private static final Set<String> SENSORS_PERMISSIONS;
    private static final Set<String> SMS_PERMISSIONS;
    private static final Set<String> STORAGE_PERMISSIONS;
    private static final String TAG = "DefaultPermGrantPolicy";
    private static final String TAG_EXCEPTION = "exception";
    private static final String TAG_EXCEPTIONS = "exceptions";
    private static final String TAG_PERMISSION = "permission";
    private final Context mContext;
    private IDefaultPermissionGrantPolicyExt mDefaultPermissionGrantPolicyExt;
    private LegacyPermissionManagerInternal.PackagesProvider mDialerAppPackagesProvider;
    private ArrayMap<String, List<DefaultPermissionGrant>> mGrantExceptions;
    private final Handler mHandler;
    private LegacyPermissionManagerInternal.PackagesProvider mLocationExtraPackagesProvider;
    private LegacyPermissionManagerInternal.PackagesProvider mLocationPackagesProvider;
    private final PackageManagerInternal mServiceInternal;
    private LegacyPermissionManagerInternal.PackagesProvider mSimCallManagerPackagesProvider;
    private LegacyPermissionManagerInternal.PackagesProvider mSmsAppPackagesProvider;
    private LegacyPermissionManagerInternal.SyncAdapterPackagesProvider mSyncAdapterPackagesProvider;
    private LegacyPermissionManagerInternal.PackagesProvider mUseOpenWifiAppPackagesProvider;
    private LegacyPermissionManagerInternal.PackagesProvider mVoiceInteractionPackagesProvider;
    private final Object mLock = new Object();
    private final PackageManagerWrapper NO_PM_CACHE = new PackageManagerWrapper() { // from class: com.android.server.pm.permission.DefaultPermissionGrantPolicy.1
        @Override // com.android.server.pm.permission.DefaultPermissionGrantPolicy.PackageManagerWrapper
        public int getPermissionFlags(String str, PackageInfo packageInfo, UserHandle userHandle) {
            return DefaultPermissionGrantPolicy.this.mContext.getPackageManager().getPermissionFlags(str, packageInfo.packageName, userHandle);
        }

        @Override // com.android.server.pm.permission.DefaultPermissionGrantPolicy.PackageManagerWrapper
        public void updatePermissionFlags(String str, PackageInfo packageInfo, int i, int i2, UserHandle userHandle) {
            DefaultPermissionGrantPolicy.this.mContext.getPackageManager().updatePermissionFlags(str, packageInfo.packageName, i, i2, userHandle);
        }

        @Override // com.android.server.pm.permission.DefaultPermissionGrantPolicy.PackageManagerWrapper
        public void grantPermission(String str, PackageInfo packageInfo, UserHandle userHandle) {
            DefaultPermissionGrantPolicy.this.mContext.getPackageManager().grantRuntimePermission(packageInfo.packageName, str, userHandle);
        }

        @Override // com.android.server.pm.permission.DefaultPermissionGrantPolicy.PackageManagerWrapper
        public void revokePermission(String str, PackageInfo packageInfo, UserHandle userHandle) {
            DefaultPermissionGrantPolicy.this.mContext.getPackageManager().revokeRuntimePermission(packageInfo.packageName, str, userHandle);
        }

        @Override // com.android.server.pm.permission.DefaultPermissionGrantPolicy.PackageManagerWrapper
        public boolean isGranted(String str, PackageInfo packageInfo, UserHandle userHandle) {
            return DefaultPermissionGrantPolicy.this.mContext.createContextAsUser(userHandle, 0).getPackageManager().checkPermission(str, packageInfo.packageName) == 0;
        }

        @Override // com.android.server.pm.permission.DefaultPermissionGrantPolicy.PackageManagerWrapper
        public PermissionInfo getPermissionInfo(String str) {
            if (str == null) {
                return null;
            }
            try {
                return DefaultPermissionGrantPolicy.this.mContext.getPackageManager().getPermissionInfo(str, 0);
            } catch (PackageManager.NameNotFoundException unused) {
                Slog.w(DefaultPermissionGrantPolicy.TAG, "Permission not found: " + str);
                return null;
            }
        }

        @Override // com.android.server.pm.permission.DefaultPermissionGrantPolicy.PackageManagerWrapper
        public PackageInfo getPackageInfo(String str) {
            if (str == null) {
                return null;
            }
            try {
                return DefaultPermissionGrantPolicy.this.mContext.getPackageManager().getPackageInfo(str, DefaultPermissionGrantPolicy.DEFAULT_PACKAGE_INFO_QUERY_FLAGS);
            } catch (PackageManager.NameNotFoundException unused) {
                Slog.e(DefaultPermissionGrantPolicy.TAG, "Package not found: " + str);
                return null;
            }
        }
    };
    private final IDefaultPermissionGrantPolicyWrapper mWrapper = new DefaultPermissionGrantPolicyWrapper();

    private boolean isFixedOrUserSet(int i) {
        return (i & 23) != 0;
    }

    static {
        ArraySet arraySet = new ArraySet();
        PHONE_PERMISSIONS = arraySet;
        arraySet.add("android.permission.READ_PHONE_STATE");
        arraySet.add("android.permission.CALL_PHONE");
        arraySet.add("android.permission.READ_CALL_LOG");
        arraySet.add("android.permission.WRITE_CALL_LOG");
        arraySet.add("com.android.voicemail.permission.ADD_VOICEMAIL");
        arraySet.add("android.permission.USE_SIP");
        arraySet.add("android.permission.PROCESS_OUTGOING_CALLS");
        ArraySet arraySet2 = new ArraySet();
        CONTACTS_PERMISSIONS = arraySet2;
        arraySet2.add("android.permission.READ_CONTACTS");
        arraySet2.add("android.permission.WRITE_CONTACTS");
        arraySet2.add("android.permission.GET_ACCOUNTS");
        ArraySet arraySet3 = new ArraySet();
        ALWAYS_LOCATION_PERMISSIONS = arraySet3;
        arraySet3.add("android.permission.ACCESS_FINE_LOCATION");
        arraySet3.add("android.permission.ACCESS_COARSE_LOCATION");
        arraySet3.add("android.permission.ACCESS_BACKGROUND_LOCATION");
        ArraySet arraySet4 = new ArraySet();
        FOREGROUND_LOCATION_PERMISSIONS = arraySet4;
        arraySet4.add("android.permission.ACCESS_FINE_LOCATION");
        arraySet4.add("android.permission.ACCESS_COARSE_LOCATION");
        ArraySet arraySet5 = new ArraySet();
        COARSE_BACKGROUND_LOCATION_PERMISSIONS = arraySet5;
        arraySet5.add("android.permission.ACCESS_COARSE_LOCATION");
        arraySet5.add("android.permission.ACCESS_BACKGROUND_LOCATION");
        ArraySet arraySet6 = new ArraySet();
        FINE_LOCATION_PERMISSIONS = arraySet6;
        arraySet6.add("android.permission.ACCESS_FINE_LOCATION");
        ArraySet arraySet7 = new ArraySet();
        ACTIVITY_RECOGNITION_PERMISSIONS = arraySet7;
        arraySet7.add("android.permission.ACTIVITY_RECOGNITION");
        ArraySet arraySet8 = new ArraySet();
        CALENDAR_PERMISSIONS = arraySet8;
        arraySet8.add("android.permission.READ_CALENDAR");
        arraySet8.add("android.permission.WRITE_CALENDAR");
        ArraySet arraySet9 = new ArraySet();
        SMS_PERMISSIONS = arraySet9;
        arraySet9.add("android.permission.SEND_SMS");
        arraySet9.add("android.permission.RECEIVE_SMS");
        arraySet9.add("android.permission.READ_SMS");
        arraySet9.add("android.permission.RECEIVE_WAP_PUSH");
        arraySet9.add("android.permission.RECEIVE_MMS");
        arraySet9.add("android.permission.READ_CELL_BROADCASTS");
        ArraySet arraySet10 = new ArraySet();
        MICROPHONE_PERMISSIONS = arraySet10;
        arraySet10.add("android.permission.RECORD_AUDIO");
        ArraySet arraySet11 = new ArraySet();
        CAMERA_PERMISSIONS = arraySet11;
        arraySet11.add("android.permission.CAMERA");
        ArraySet arraySet12 = new ArraySet();
        SENSORS_PERMISSIONS = arraySet12;
        arraySet12.add("android.permission.BODY_SENSORS");
        arraySet12.add("android.permission.BODY_SENSORS_BACKGROUND");
        ArraySet arraySet13 = new ArraySet();
        STORAGE_PERMISSIONS = arraySet13;
        arraySet13.add("android.permission.READ_EXTERNAL_STORAGE");
        arraySet13.add("android.permission.WRITE_EXTERNAL_STORAGE");
        arraySet13.add("android.permission.ACCESS_MEDIA_LOCATION");
        arraySet13.add("android.permission.READ_MEDIA_AUDIO");
        arraySet13.add("android.permission.READ_MEDIA_VIDEO");
        arraySet13.add("android.permission.READ_MEDIA_IMAGES");
        arraySet13.add("android.permission.READ_MEDIA_VISUAL_USER_SELECTED");
        ArraySet arraySet14 = new ArraySet();
        NEARBY_DEVICES_PERMISSIONS = arraySet14;
        arraySet14.add("android.permission.BLUETOOTH_ADVERTISE");
        arraySet14.add("android.permission.BLUETOOTH_CONNECT");
        arraySet14.add("android.permission.BLUETOOTH_SCAN");
        arraySet14.add("android.permission.UWB_RANGING");
        arraySet14.add("android.permission.NEARBY_WIFI_DEVICES");
        ArraySet arraySet15 = new ArraySet();
        NOTIFICATION_PERMISSIONS = arraySet15;
        arraySet15.add("android.permission.POST_NOTIFICATIONS");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public DefaultPermissionGrantPolicy(Context context) {
        this.mContext = context;
        ServiceThread serviceThread = new ServiceThread(TAG, 10, true);
        serviceThread.start();
        this.mHandler = new Handler(serviceThread.getLooper()) { // from class: com.android.server.pm.permission.DefaultPermissionGrantPolicy.2
            @Override // android.os.Handler
            public void handleMessage(Message message) {
                if (message.what == 1) {
                    synchronized (DefaultPermissionGrantPolicy.this.mLock) {
                        if (DefaultPermissionGrantPolicy.this.mGrantExceptions == null) {
                            DefaultPermissionGrantPolicy defaultPermissionGrantPolicy = DefaultPermissionGrantPolicy.this;
                            defaultPermissionGrantPolicy.mGrantExceptions = defaultPermissionGrantPolicy.readDefaultPermissionExceptionsLocked(defaultPermissionGrantPolicy.NO_PM_CACHE);
                        }
                    }
                }
            }
        };
        this.mServiceInternal = (PackageManagerInternal) LocalServices.getService(PackageManagerInternal.class);
        this.mDefaultPermissionGrantPolicyExt = (IDefaultPermissionGrantPolicyExt) ExtLoader.type(IDefaultPermissionGrantPolicyExt.class).create();
    }

    public void setLocationPackagesProvider(LegacyPermissionManagerInternal.PackagesProvider packagesProvider) {
        synchronized (this.mLock) {
            this.mLocationPackagesProvider = packagesProvider;
        }
    }

    public void setLocationExtraPackagesProvider(LegacyPermissionManagerInternal.PackagesProvider packagesProvider) {
        synchronized (this.mLock) {
            this.mLocationExtraPackagesProvider = packagesProvider;
        }
    }

    public void setVoiceInteractionPackagesProvider(LegacyPermissionManagerInternal.PackagesProvider packagesProvider) {
        synchronized (this.mLock) {
            this.mVoiceInteractionPackagesProvider = packagesProvider;
        }
    }

    public void setSmsAppPackagesProvider(LegacyPermissionManagerInternal.PackagesProvider packagesProvider) {
        synchronized (this.mLock) {
            this.mSmsAppPackagesProvider = packagesProvider;
        }
    }

    public void setDialerAppPackagesProvider(LegacyPermissionManagerInternal.PackagesProvider packagesProvider) {
        synchronized (this.mLock) {
            this.mDialerAppPackagesProvider = packagesProvider;
        }
    }

    public void setSimCallManagerPackagesProvider(LegacyPermissionManagerInternal.PackagesProvider packagesProvider) {
        synchronized (this.mLock) {
            this.mSimCallManagerPackagesProvider = packagesProvider;
        }
    }

    public void setUseOpenWifiAppPackagesProvider(LegacyPermissionManagerInternal.PackagesProvider packagesProvider) {
        synchronized (this.mLock) {
            this.mUseOpenWifiAppPackagesProvider = packagesProvider;
        }
    }

    public void setSyncAdapterPackagesProvider(LegacyPermissionManagerInternal.SyncAdapterPackagesProvider syncAdapterPackagesProvider) {
        synchronized (this.mLock) {
            this.mSyncAdapterPackagesProvider = syncAdapterPackagesProvider;
        }
    }

    public void grantDefaultPermissions(int i) {
        DelayingPackageManagerCache delayingPackageManagerCache = new DelayingPackageManagerCache();
        grantPermissionsToSysComponentsAndPrivApps(delayingPackageManagerCache, i);
        grantDefaultSystemHandlerPermissions(delayingPackageManagerCache, i);
        grantSignatureAppsNotificationPermissions(delayingPackageManagerCache, i);
        grantDefaultPermissionExceptions(delayingPackageManagerCache, i);
        delayingPackageManagerCache.apply();
    }

    private void grantSignatureAppsNotificationPermissions(PackageManagerWrapper packageManagerWrapper, int i) {
        Log.i(TAG, "Granting Notification permissions to platform signature apps for user " + i);
        for (PackageInfo packageInfo : this.mContext.getPackageManager().getInstalledPackagesAsUser(DEFAULT_PACKAGE_INFO_QUERY_FLAGS, 0)) {
            if (packageInfo != null && packageInfo.applicationInfo.isSystemApp() && packageInfo.applicationInfo.isSignedWithPlatformKey()) {
                grantRuntimePermissionsForSystemPackage(packageManagerWrapper, i, packageInfo, NOTIFICATION_PERMISSIONS);
            }
        }
    }

    private void grantRuntimePermissionsForSystemPackage(PackageManagerWrapper packageManagerWrapper, int i, PackageInfo packageInfo) {
        grantRuntimePermissionsForSystemPackage(packageManagerWrapper, i, packageInfo, null);
    }

    private void grantRuntimePermissionsForSystemPackage(PackageManagerWrapper packageManagerWrapper, int i, PackageInfo packageInfo, Set<String> set) {
        if (ArrayUtils.isEmpty(packageInfo.requestedPermissions)) {
            return;
        }
        ArraySet arraySet = new ArraySet();
        for (String str : packageInfo.requestedPermissions) {
            PermissionInfo permissionInfo = packageManagerWrapper.getPermissionInfo(str);
            if (permissionInfo != null && ((set == null || set.contains(str)) && permissionInfo.isRuntime())) {
                arraySet.add(str);
            }
        }
        if (arraySet.isEmpty()) {
            return;
        }
        grantRuntimePermissions(packageManagerWrapper, packageInfo, arraySet, true, i);
    }

    public void scheduleReadDefaultPermissionExceptions() {
        this.mHandler.sendEmptyMessage(1);
    }

    private void grantPermissionsToSysComponentsAndPrivApps(DelayingPackageManagerCache delayingPackageManagerCache, int i) {
        Log.i(TAG, "Granting permissions to platform components for user " + i);
        List<PackageInfo> installedPackagesAsUser = this.mContext.getPackageManager().getInstalledPackagesAsUser(DEFAULT_PACKAGE_INFO_QUERY_FLAGS, 0);
        for (PackageInfo packageInfo : installedPackagesAsUser) {
            if (packageInfo != null) {
                delayingPackageManagerCache.addPackageInfo(packageInfo.packageName, packageInfo);
                if (delayingPackageManagerCache.isSysComponentOrPersistentPlatformSignedPrivApp(packageInfo) && doesPackageSupportRuntimePermissions(packageInfo) && !ArrayUtils.isEmpty(packageInfo.requestedPermissions)) {
                    grantRuntimePermissionsForSystemPackage(delayingPackageManagerCache, i, packageInfo);
                }
            }
        }
        for (PackageInfo packageInfo2 : installedPackagesAsUser) {
            if (packageInfo2 != null && doesPackageSupportRuntimePermissions(packageInfo2) && !ArrayUtils.isEmpty(packageInfo2.requestedPermissions) && delayingPackageManagerCache.isGranted("android.permission.READ_PRIVILEGED_PHONE_STATE", packageInfo2, UserHandle.of(i)) && delayingPackageManagerCache.isGranted("android.permission.READ_PHONE_STATE", packageInfo2, UserHandle.of(i)) && !delayingPackageManagerCache.isSysComponentOrPersistentPlatformSignedPrivApp(packageInfo2)) {
                delayingPackageManagerCache.updatePermissionFlags("android.permission.READ_PHONE_STATE", packageInfo2, 16, 0, UserHandle.of(i));
            }
        }
    }

    @SafeVarargs
    private final void grantIgnoringSystemPackage(PackageManagerWrapper packageManagerWrapper, String str, int i, Set<String>... setArr) {
        grantPermissionsToPackage(packageManagerWrapper, str, i, true, true, setArr);
    }

    @SafeVarargs
    private final void grantSystemFixedPermissionsToSystemPackage(PackageManagerWrapper packageManagerWrapper, String str, int i, Set<String>... setArr) {
        grantPermissionsToSystemPackage(packageManagerWrapper, str, i, true, setArr);
    }

    @SafeVarargs
    private final void grantPermissionsToSystemPackage(PackageManagerWrapper packageManagerWrapper, String str, int i, Set<String>... setArr) {
        grantPermissionsToSystemPackage(packageManagerWrapper, str, i, false, setArr);
    }

    @SafeVarargs
    private final void grantPermissionsToSystemPackage(PackageManagerWrapper packageManagerWrapper, String str, int i, boolean z, Set<String>... setArr) {
        if (packageManagerWrapper.isSystemPackage(str)) {
            grantPermissionsToPackage(packageManagerWrapper, packageManagerWrapper.getSystemPackageInfo(str), i, z, false, true, setArr);
        }
    }

    @SafeVarargs
    private final void grantPermissionsToPackage(PackageManagerWrapper packageManagerWrapper, String str, int i, boolean z, boolean z2, Set<String>... setArr) {
        grantPermissionsToPackage(packageManagerWrapper, packageManagerWrapper.getPackageInfo(str), i, false, z, z2, setArr);
    }

    @SafeVarargs
    private final void grantPermissionsToPackage(PackageManagerWrapper packageManagerWrapper, PackageInfo packageInfo, int i, boolean z, boolean z2, boolean z3, Set<String>... setArr) {
        if (packageInfo != null && doesPackageSupportRuntimePermissions(packageInfo)) {
            for (Set<String> set : setArr) {
                grantRuntimePermissions(packageManagerWrapper, packageInfo, set, z, z2, z3, i);
            }
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:55:0x02aa  */
    /* JADX WARN: Removed duplicated region for block: B:61:0x02de  */
    /* JADX WARN: Removed duplicated region for block: B:64:0x030d  */
    /* JADX WARN: Removed duplicated region for block: B:69:0x0340  */
    /* JADX WARN: Removed duplicated region for block: B:75:0x03ba  */
    /* JADX WARN: Removed duplicated region for block: B:81:0x046b A[LOOP:5: B:80:0x0469->B:81:0x046b, LOOP_END] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private void grantDefaultSystemHandlerPermissions(PackageManagerWrapper packageManagerWrapper, int i) {
        LegacyPermissionManagerInternal.PackagesProvider packagesProvider;
        LegacyPermissionManagerInternal.PackagesProvider packagesProvider2;
        LegacyPermissionManagerInternal.PackagesProvider packagesProvider3;
        LegacyPermissionManagerInternal.PackagesProvider packagesProvider4;
        LegacyPermissionManagerInternal.PackagesProvider packagesProvider5;
        LegacyPermissionManagerInternal.PackagesProvider packagesProvider6;
        LegacyPermissionManagerInternal.PackagesProvider packagesProvider7;
        LegacyPermissionManagerInternal.SyncAdapterPackagesProvider syncAdapterPackagesProvider;
        String str;
        Log.i(TAG, "Granting permissions to default platform handlers for user " + i);
        synchronized (this.mLock) {
            packagesProvider = this.mLocationPackagesProvider;
            packagesProvider2 = this.mLocationExtraPackagesProvider;
            packagesProvider3 = this.mVoiceInteractionPackagesProvider;
            packagesProvider4 = this.mSmsAppPackagesProvider;
            packagesProvider5 = this.mDialerAppPackagesProvider;
            packagesProvider6 = this.mSimCallManagerPackagesProvider;
            packagesProvider7 = this.mUseOpenWifiAppPackagesProvider;
            syncAdapterPackagesProvider = this.mSyncAdapterPackagesProvider;
        }
        String[] packages = packagesProvider3 != null ? packagesProvider3.getPackages(i) : null;
        String[] packages2 = packagesProvider != null ? packagesProvider.getPackages(i) : null;
        String[] packages3 = packagesProvider2 != null ? packagesProvider2.getPackages(i) : null;
        String[] packages4 = packagesProvider4 != null ? packagesProvider4.getPackages(i) : null;
        String[] packages5 = packagesProvider5 != null ? packagesProvider5.getPackages(i) : null;
        String[] packages6 = packagesProvider6 != null ? packagesProvider6.getPackages(i) : null;
        String[] packages7 = packagesProvider7 != null ? packagesProvider7.getPackages(i) : null;
        String[] packages8 = syncAdapterPackagesProvider != null ? syncAdapterPackagesProvider.getPackages("com.android.contacts", i) : null;
        String[] packages9 = syncAdapterPackagesProvider != null ? syncAdapterPackagesProvider.getPackages("com.android.calendar", i) : null;
        String permissionControllerPackageName = this.mContext.getPackageManager().getPermissionControllerPackageName();
        Set<String> set = NOTIFICATION_PERMISSIONS;
        grantSystemFixedPermissionsToSystemPackage(packageManagerWrapper, permissionControllerPackageName, i, set);
        String str2 = (String) ArrayUtils.firstOrNull(getKnownPackages(2, i));
        Set<String> set2 = STORAGE_PERMISSIONS;
        grantSystemFixedPermissionsToSystemPackage(packageManagerWrapper, str2, i, set2, set);
        String str3 = (String) ArrayUtils.firstOrNull(getKnownPackages(4, i));
        grantSystemFixedPermissionsToSystemPackage(packageManagerWrapper, str3, i, set2);
        Set<String> set3 = PHONE_PERMISSIONS;
        String[] strArr = packages3;
        grantPermissionsToSystemPackage(packageManagerWrapper, str3, i, set3, SMS_PERMISSIONS, set);
        String str4 = (String) ArrayUtils.firstOrNull(getKnownPackages(1, i));
        Set<String> set4 = CONTACTS_PERMISSIONS;
        String[] strArr2 = packages2;
        Set<String> set5 = ALWAYS_LOCATION_PERMISSIONS;
        String[] strArr3 = packages;
        Set<String> set6 = CAMERA_PERMISSIONS;
        String[] strArr4 = packages8;
        grantPermissionsToSystemPackage(packageManagerWrapper, str4, i, set3, set4, set5, set6, NEARBY_DEVICES_PERMISSIONS);
        grantSystemFixedPermissionsToSystemPackage(packageManagerWrapper, str4, i, set);
        grantPermissionsToSystemPackage(packageManagerWrapper, getDefaultSearchSelectorPackage(), i, set);
        grantPermissionsToSystemPackage(packageManagerWrapper, getDefaultCaptivePortalLoginPackage(), i, set);
        grantPermissionsToSystemPackage(packageManagerWrapper, getDefaultDockManagerPackage(), i, set);
        String defaultSystemHandlerActivityPackage = getDefaultSystemHandlerActivityPackage(packageManagerWrapper, "android.media.action.IMAGE_CAPTURE", i);
        Set<String> set7 = MICROPHONE_PERMISSIONS;
        grantPermissionsToSystemPackage(packageManagerWrapper, defaultSystemHandlerActivityPackage, i, set6, set7, set2);
        grantPermissionsToSystemPackage(packageManagerWrapper, getDefaultSystemHandlerActivityPackage(packageManagerWrapper, "android.provider.MediaStore.RECORD_SOUND", i), i, set7);
        grantSystemFixedPermissionsToSystemPackage(packageManagerWrapper, getDefaultProviderAuthorityPackage("media", i), i, set2, set);
        grantSystemFixedPermissionsToSystemPackage(packageManagerWrapper, getDefaultProviderAuthorityPackage("downloads", i), i, set2, set);
        grantSystemFixedPermissionsToSystemPackage(packageManagerWrapper, getDefaultSystemHandlerActivityPackage(packageManagerWrapper, "android.intent.action.VIEW_DOWNLOADS", i), i, set2);
        grantSystemFixedPermissionsToSystemPackage(packageManagerWrapper, getDefaultProviderAuthorityPackage("com.android.externalstorage.documents", i), i, set2);
        grantSystemFixedPermissionsToSystemPackage(packageManagerWrapper, getDefaultSystemHandlerActivityPackage(packageManagerWrapper, "android.credentials.INSTALL", i), i, set2);
        if (packages5 == null) {
            grantDefaultPermissionsToDefaultSystemDialerApp(packageManagerWrapper, getDefaultSystemHandlerActivityPackage(packageManagerWrapper, "android.intent.action.DIAL", i), i);
        } else {
            for (String str5 : packages5) {
                grantDefaultPermissionsToDefaultSystemDialerApp(packageManagerWrapper, str5, i);
            }
        }
        if (packages6 != null) {
            for (String str6 : packages6) {
                grantDefaultPermissionsToDefaultSystemSimCallManager(packageManagerWrapper, str6, i);
            }
        }
        if (packages7 != null) {
            for (String str7 : packages7) {
                grantDefaultPermissionsToDefaultSystemUseOpenWifiApp(packageManagerWrapper, str7, i);
            }
        }
        if (packages4 == null) {
            grantDefaultPermissionsToDefaultSystemSmsApp(packageManagerWrapper, getDefaultSystemHandlerActivityPackageForCategory(packageManagerWrapper, "android.intent.category.APP_MESSAGING", i), i);
        } else {
            for (String str8 : packages4) {
                grantDefaultPermissionsToDefaultSystemSmsApp(packageManagerWrapper, str8, i);
            }
        }
        String defaultSystemHandlerActivityPackage2 = getDefaultSystemHandlerActivityPackage(packageManagerWrapper, "android.provider.Telephony.SMS_CB_RECEIVED", i);
        Set<String> set8 = SMS_PERMISSIONS;
        Set<String> set9 = NEARBY_DEVICES_PERMISSIONS;
        Set<String> set10 = NOTIFICATION_PERMISSIONS;
        grantSystemFixedPermissionsToSystemPackage(packageManagerWrapper, defaultSystemHandlerActivityPackage2, i, set8, set9, set10);
        grantPermissionsToSystemPackage(packageManagerWrapper, getDefaultSystemHandlerServicePackage(packageManagerWrapper, "android.provider.Telephony.SMS_CARRIER_PROVISION", i), i, set8);
        IDefaultPermissionGrantPolicyExt iDefaultPermissionGrantPolicyExt = this.mDefaultPermissionGrantPolicyExt;
        Set<String> set11 = CONTACTS_PERMISSIONS;
        Set<String> hookGrantDefaultSystemHandlerPermissions = iDefaultPermissionGrantPolicyExt.hookGrantDefaultSystemHandlerPermissions(set11);
        String defaultSystemHandlerActivityPackageForCategory = getDefaultSystemHandlerActivityPackageForCategory(packageManagerWrapper, "android.intent.category.APP_CALENDAR", i);
        Set<String> set12 = CALENDAR_PERMISSIONS;
        grantPermissionsToSystemPackage(packageManagerWrapper, defaultSystemHandlerActivityPackageForCategory, i, set12, hookGrantDefaultSystemHandlerPermissions, set10);
        String defaultProviderAuthorityPackage = getDefaultProviderAuthorityPackage("com.android.calendar", i);
        Set<String> set13 = STORAGE_PERMISSIONS;
        grantPermissionsToSystemPackage(packageManagerWrapper, defaultProviderAuthorityPackage, i, set11, set13);
        grantSystemFixedPermissionsToSystemPackage(packageManagerWrapper, defaultProviderAuthorityPackage, i, set12);
        if (packages9 != null) {
            grantPermissionToEachSystemPackage(packageManagerWrapper, getHeadlessSyncAdapterPackages(packageManagerWrapper, packages9, i), i, set12);
        }
        String defaultSystemHandlerActivityPackageForCategory2 = getDefaultSystemHandlerActivityPackageForCategory(packageManagerWrapper, "android.intent.category.APP_CONTACTS", i);
        Set<String> set14 = PHONE_PERMISSIONS;
        grantPermissionsToSystemPackage(packageManagerWrapper, defaultSystemHandlerActivityPackageForCategory2, i, set11, set14);
        if (strArr4 != null) {
            grantPermissionToEachSystemPackage(packageManagerWrapper, getHeadlessSyncAdapterPackages(packageManagerWrapper, strArr4, i), i, set11);
        }
        String defaultProviderAuthorityPackage2 = getDefaultProviderAuthorityPackage("com.android.contacts", i);
        grantSystemFixedPermissionsToSystemPackage(packageManagerWrapper, defaultProviderAuthorityPackage2, i, set11, set14);
        grantPermissionsToSystemPackage(packageManagerWrapper, defaultProviderAuthorityPackage2, i, set13);
        grantPermissionsToSystemPackage(packageManagerWrapper, getDefaultSystemHandlerActivityPackage(packageManagerWrapper, "android.app.action.PROVISION_MANAGED_DEVICE", i), i, set11, set10);
        if (this.mContext.getPackageManager().hasSystemFeature("android.hardware.type.automotive", 0)) {
            grantPermissionsToSystemPackage(packageManagerWrapper, getDefaultSystemHandlerActivityPackageForCategory(packageManagerWrapper, "android.intent.category.APP_MAPS", i), i, FOREGROUND_LOCATION_PERMISSIONS);
        }
        grantPermissionsToSystemPackage(packageManagerWrapper, getDefaultSystemHandlerActivityPackageForCategory(packageManagerWrapper, "android.intent.category.APP_EMAIL", i), i, set11, set12);
        String str9 = (String) ArrayUtils.firstOrNull(getKnownPackages(5, i));
        if (str9 == null) {
            str9 = getDefaultSystemHandlerActivityPackageForCategory(packageManagerWrapper, "android.intent.category.APP_BROWSER", i);
            if (!packageManagerWrapper.isSystemPackage(str9)) {
                str = null;
                grantPermissionsToPackage(packageManagerWrapper, str, i, false, true, FOREGROUND_LOCATION_PERMISSIONS);
                if (strArr3 != null) {
                    for (String str10 : strArr3) {
                        grantPermissionsToSystemPackage(packageManagerWrapper, str10, i, CONTACTS_PERMISSIONS, CALENDAR_PERMISSIONS, MICROPHONE_PERMISSIONS, PHONE_PERMISSIONS, SMS_PERMISSIONS, COARSE_BACKGROUND_LOCATION_PERMISSIONS, NEARBY_DEVICES_PERMISSIONS, NOTIFICATION_PERMISSIONS);
                        revokeRuntimePermissions(packageManagerWrapper, str10, FINE_LOCATION_PERMISSIONS, false, i);
                    }
                }
                if (ActivityManager.isLowRamDeviceStatic()) {
                    grantPermissionsToSystemPackage(packageManagerWrapper, getDefaultSystemHandlerActivityPackage(packageManagerWrapper, "android.search.action.GLOBAL_SEARCH", i), i, MICROPHONE_PERMISSIONS, ALWAYS_LOCATION_PERMISSIONS, NOTIFICATION_PERMISSIONS);
                }
                grantPermissionsToSystemPackage(packageManagerWrapper, getDefaultSystemHandlerServicePackage(packageManagerWrapper, new Intent("android.speech.RecognitionService").addCategory("android.intent.category.DEFAULT"), i), i, MICROPHONE_PERMISSIONS);
                if (strArr2 != null) {
                    for (String str11 : strArr2) {
                        grantPermissionsToSystemPackage(packageManagerWrapper, str11, i, CONTACTS_PERMISSIONS, CALENDAR_PERMISSIONS, MICROPHONE_PERMISSIONS, PHONE_PERMISSIONS, SMS_PERMISSIONS, CAMERA_PERMISSIONS, SENSORS_PERMISSIONS, STORAGE_PERMISSIONS, NEARBY_DEVICES_PERMISSIONS, NOTIFICATION_PERMISSIONS);
                        grantSystemFixedPermissionsToSystemPackage(packageManagerWrapper, str11, i, ALWAYS_LOCATION_PERMISSIONS, ACTIVITY_RECOGNITION_PERMISSIONS);
                    }
                }
                if (strArr != null) {
                    for (String str12 : strArr) {
                        grantPermissionsToSystemPackage(packageManagerWrapper, str12, i, ALWAYS_LOCATION_PERMISSIONS, NEARBY_DEVICES_PERMISSIONS);
                        grantSystemFixedPermissionsToSystemPackage(packageManagerWrapper, str12, i, ACTIVITY_RECOGNITION_PERMISSIONS);
                    }
                }
                String defaultSystemHandlerActivityPackage3 = getDefaultSystemHandlerActivityPackage(packageManagerWrapper, new Intent("android.intent.action.VIEW").addCategory("android.intent.category.DEFAULT").setDataAndType(Uri.fromFile(new File("foo.mp3")), AUDIO_MIME_TYPE), i);
                Set<String> set15 = STORAGE_PERMISSIONS;
                grantPermissionsToSystemPackage(packageManagerWrapper, defaultSystemHandlerActivityPackage3, i, set15);
                String defaultSystemHandlerActivityPackage4 = getDefaultSystemHandlerActivityPackage(packageManagerWrapper, new Intent("android.intent.action.MAIN").addCategory("android.intent.category.HOME").addCategory("android.intent.category.LAUNCHER_APP"), i);
                Set<String> set16 = ALWAYS_LOCATION_PERMISSIONS;
                Set<String> set17 = NOTIFICATION_PERMISSIONS;
                grantPermissionsToSystemPackage(packageManagerWrapper, defaultSystemHandlerActivityPackage4, i, set16, set17);
                if (this.mContext.getPackageManager().hasSystemFeature("android.hardware.type.watch", 0)) {
                    String defaultSystemHandlerActivityPackageForCategory3 = getDefaultSystemHandlerActivityPackageForCategory(packageManagerWrapper, "android.intent.category.HOME_MAIN", i);
                    grantPermissionsToSystemPackage(packageManagerWrapper, defaultSystemHandlerActivityPackageForCategory3, i, CONTACTS_PERMISSIONS, MICROPHONE_PERMISSIONS, set16);
                    grantSystemFixedPermissionsToSystemPackage(packageManagerWrapper, defaultSystemHandlerActivityPackageForCategory3, i, PHONE_PERMISSIONS, ACTIVITY_RECOGNITION_PERMISSIONS);
                    if (this.mContext.getResources().getBoolean(17891873)) {
                        Log.d(TAG, "Wear: Skipping permission grant for Default fitness tracker app : " + defaultSystemHandlerActivityPackageForCategory3);
                    } else {
                        grantPermissionsToSystemPackage(packageManagerWrapper, getDefaultSystemHandlerActivityPackage(packageManagerWrapper, ACTION_TRACK, i), i, SENSORS_PERMISSIONS);
                    }
                }
                grantSystemFixedPermissionsToSystemPackage(packageManagerWrapper, "com.android.printspooler", i, set16, set17);
                String defaultSystemHandlerActivityPackage5 = getDefaultSystemHandlerActivityPackage(packageManagerWrapper, "android.telephony.action.EMERGENCY_ASSISTANCE", i);
                Set<String> set18 = CONTACTS_PERMISSIONS;
                Set<String> set19 = PHONE_PERMISSIONS;
                grantSystemFixedPermissionsToSystemPackage(packageManagerWrapper, defaultSystemHandlerActivityPackage5, i, set18, set19);
                grantPermissionsToSystemPackage(packageManagerWrapper, getDefaultSystemHandlerActivityPackage(packageManagerWrapper, new Intent("android.intent.action.VIEW").setType("vnd.android.cursor.item/ndef_msg"), i), i, set18, set19);
                grantSystemFixedPermissionsToSystemPackage(packageManagerWrapper, getDefaultSystemHandlerActivityPackage(packageManagerWrapper, "android.os.storage.action.MANAGE_STORAGE", i), i, set15);
                grantSystemFixedPermissionsToSystemPackage(packageManagerWrapper, "com.android.companiondevicemanager", i, set16, NEARBY_DEVICES_PERMISSIONS);
                grantSystemFixedPermissionsToSystemPackage(packageManagerWrapper, getDefaultSystemHandlerActivityPackage(packageManagerWrapper, "android.intent.action.RINGTONE_PICKER", i), i, set15);
                for (String str13 : getKnownPackages(6, i)) {
                    grantPermissionsToSystemPackage(packageManagerWrapper, str13, i, COARSE_BACKGROUND_LOCATION_PERMISSIONS, CONTACTS_PERMISSIONS);
                }
                grantSystemFixedPermissionsToSystemPackage(packageManagerWrapper, "com.android.sharedstoragebackup", i, STORAGE_PERMISSIONS);
                grantSystemFixedPermissionsToSystemPackage(packageManagerWrapper, "com.android.bluetoothmidiservice", i, NEARBY_DEVICES_PERMISSIONS);
                grantPermissionsToSystemPackage(packageManagerWrapper, getDefaultSystemHandlerServicePackage(packageManagerWrapper, "android.adservices.AD_SERVICES_COMMON_SERVICE", i), i, NOTIFICATION_PERMISSIONS);
            }
        }
        str = str9;
        grantPermissionsToPackage(packageManagerWrapper, str, i, false, true, FOREGROUND_LOCATION_PERMISSIONS);
        if (strArr3 != null) {
        }
        if (ActivityManager.isLowRamDeviceStatic()) {
        }
        grantPermissionsToSystemPackage(packageManagerWrapper, getDefaultSystemHandlerServicePackage(packageManagerWrapper, new Intent("android.speech.RecognitionService").addCategory("android.intent.category.DEFAULT"), i), i, MICROPHONE_PERMISSIONS);
        if (strArr2 != null) {
        }
        if (strArr != null) {
        }
        String defaultSystemHandlerActivityPackage32 = getDefaultSystemHandlerActivityPackage(packageManagerWrapper, new Intent("android.intent.action.VIEW").addCategory("android.intent.category.DEFAULT").setDataAndType(Uri.fromFile(new File("foo.mp3")), AUDIO_MIME_TYPE), i);
        Set<String> set152 = STORAGE_PERMISSIONS;
        grantPermissionsToSystemPackage(packageManagerWrapper, defaultSystemHandlerActivityPackage32, i, set152);
        String defaultSystemHandlerActivityPackage42 = getDefaultSystemHandlerActivityPackage(packageManagerWrapper, new Intent("android.intent.action.MAIN").addCategory("android.intent.category.HOME").addCategory("android.intent.category.LAUNCHER_APP"), i);
        Set<String> set162 = ALWAYS_LOCATION_PERMISSIONS;
        Set<String> set172 = NOTIFICATION_PERMISSIONS;
        grantPermissionsToSystemPackage(packageManagerWrapper, defaultSystemHandlerActivityPackage42, i, set162, set172);
        if (this.mContext.getPackageManager().hasSystemFeature("android.hardware.type.watch", 0)) {
        }
        grantSystemFixedPermissionsToSystemPackage(packageManagerWrapper, "com.android.printspooler", i, set162, set172);
        String defaultSystemHandlerActivityPackage52 = getDefaultSystemHandlerActivityPackage(packageManagerWrapper, "android.telephony.action.EMERGENCY_ASSISTANCE", i);
        Set<String> set182 = CONTACTS_PERMISSIONS;
        Set<String> set192 = PHONE_PERMISSIONS;
        grantSystemFixedPermissionsToSystemPackage(packageManagerWrapper, defaultSystemHandlerActivityPackage52, i, set182, set192);
        grantPermissionsToSystemPackage(packageManagerWrapper, getDefaultSystemHandlerActivityPackage(packageManagerWrapper, new Intent("android.intent.action.VIEW").setType("vnd.android.cursor.item/ndef_msg"), i), i, set182, set192);
        grantSystemFixedPermissionsToSystemPackage(packageManagerWrapper, getDefaultSystemHandlerActivityPackage(packageManagerWrapper, "android.os.storage.action.MANAGE_STORAGE", i), i, set152);
        grantSystemFixedPermissionsToSystemPackage(packageManagerWrapper, "com.android.companiondevicemanager", i, set162, NEARBY_DEVICES_PERMISSIONS);
        grantSystemFixedPermissionsToSystemPackage(packageManagerWrapper, getDefaultSystemHandlerActivityPackage(packageManagerWrapper, "android.intent.action.RINGTONE_PICKER", i), i, set152);
        while (r10 < r2) {
        }
        grantSystemFixedPermissionsToSystemPackage(packageManagerWrapper, "com.android.sharedstoragebackup", i, STORAGE_PERMISSIONS);
        grantSystemFixedPermissionsToSystemPackage(packageManagerWrapper, "com.android.bluetoothmidiservice", i, NEARBY_DEVICES_PERMISSIONS);
        grantPermissionsToSystemPackage(packageManagerWrapper, getDefaultSystemHandlerServicePackage(packageManagerWrapper, "android.adservices.AD_SERVICES_COMMON_SERVICE", i), i, NOTIFICATION_PERMISSIONS);
    }

    private String getDefaultSystemHandlerActivityPackageForCategory(PackageManagerWrapper packageManagerWrapper, String str, int i) {
        return getDefaultSystemHandlerActivityPackage(packageManagerWrapper, new Intent("android.intent.action.MAIN").addCategory(str), i);
    }

    private String getDefaultSearchSelectorPackage() {
        return this.mContext.getString(R.string.config_rawContactsLocalAccountName);
    }

    private String getDefaultCaptivePortalLoginPackage() {
        return this.mContext.getString(R.string.config_mms_user_agent);
    }

    private String getDefaultDockManagerPackage() {
        return this.mContext.getString(R.string.config_networkLocationProviderPackageName);
    }

    @SafeVarargs
    private final void grantPermissionToEachSystemPackage(PackageManagerWrapper packageManagerWrapper, ArrayList<String> arrayList, int i, Set<String>... setArr) {
        if (arrayList == null) {
            return;
        }
        int size = arrayList.size();
        for (int i2 = 0; i2 < size; i2++) {
            grantPermissionsToSystemPackage(packageManagerWrapper, arrayList.get(i2), i, setArr);
        }
    }

    private String[] getKnownPackages(int i, int i2) {
        return this.mServiceInternal.getKnownPackageNames(i, i2);
    }

    private void grantDefaultPermissionsToDefaultSystemDialerApp(PackageManagerWrapper packageManagerWrapper, String str, int i) {
        if (str == null) {
            return;
        }
        if (this.mContext.getPackageManager().hasSystemFeature("android.hardware.type.watch", 0)) {
            grantSystemFixedPermissionsToSystemPackage(packageManagerWrapper, str, i, PHONE_PERMISSIONS, NOTIFICATION_PERMISSIONS);
        } else {
            grantPermissionsToSystemPackage(packageManagerWrapper, str, i, PHONE_PERMISSIONS);
        }
        grantPermissionsToSystemPackage(packageManagerWrapper, str, i, CONTACTS_PERMISSIONS, SMS_PERMISSIONS, MICROPHONE_PERMISSIONS, CAMERA_PERMISSIONS, NOTIFICATION_PERMISSIONS);
        if (this.mContext.getPackageManager().hasSystemFeature("android.hardware.type.automotive", 0)) {
            grantPermissionsToSystemPackage(packageManagerWrapper, str, i, NEARBY_DEVICES_PERMISSIONS);
        }
    }

    private void grantDefaultPermissionsToDefaultSystemSmsApp(PackageManagerWrapper packageManagerWrapper, String str, int i) {
        grantPermissionsToSystemPackage(packageManagerWrapper, str, i, PHONE_PERMISSIONS, CONTACTS_PERMISSIONS, SMS_PERMISSIONS, STORAGE_PERMISSIONS, MICROPHONE_PERMISSIONS, CAMERA_PERMISSIONS, NOTIFICATION_PERMISSIONS);
    }

    private void grantDefaultPermissionsToDefaultSystemUseOpenWifiApp(PackageManagerWrapper packageManagerWrapper, String str, int i) {
        grantPermissionsToSystemPackage(packageManagerWrapper, str, i, ALWAYS_LOCATION_PERMISSIONS);
    }

    public void grantDefaultPermissionsToDefaultUseOpenWifiApp(String str, int i) {
        Log.i(TAG, "Granting permissions to default Use Open WiFi app for user:" + i);
        grantIgnoringSystemPackage(this.NO_PM_CACHE, str, i, ALWAYS_LOCATION_PERMISSIONS);
    }

    public void grantDefaultPermissionsToDefaultSimCallManager(String str, int i) {
        grantDefaultPermissionsToDefaultSimCallManager(this.NO_PM_CACHE, str, i);
    }

    private void grantDefaultPermissionsToDefaultSimCallManager(PackageManagerWrapper packageManagerWrapper, String str, int i) {
        if (str == null) {
            return;
        }
        Log.i(TAG, "Granting permissions to sim call manager for user:" + i);
        grantPermissionsToPackage(packageManagerWrapper, str, i, false, true, PHONE_PERMISSIONS, MICROPHONE_PERMISSIONS);
    }

    private void grantDefaultPermissionsToDefaultSystemSimCallManager(PackageManagerWrapper packageManagerWrapper, String str, int i) {
        if (packageManagerWrapper.isSystemPackage(str)) {
            grantDefaultPermissionsToDefaultSimCallManager(packageManagerWrapper, str, i);
        }
    }

    public void grantDefaultPermissionsToEnabledCarrierApps(String[] strArr, int i) {
        Log.i(TAG, "Granting permissions to enabled carrier apps for user:" + i);
        if (strArr == null) {
            return;
        }
        for (String str : strArr) {
            grantPermissionsToSystemPackage(this.NO_PM_CACHE, str, i, PHONE_PERMISSIONS, ALWAYS_LOCATION_PERMISSIONS, SMS_PERMISSIONS);
        }
    }

    public void grantDefaultPermissionsToEnabledImsServices(String[] strArr, int i) {
        Log.i(TAG, "Granting permissions to enabled ImsServices for user:" + i);
        if (strArr == null) {
            return;
        }
        for (String str : strArr) {
            grantPermissionsToSystemPackage(this.NO_PM_CACHE, str, i, PHONE_PERMISSIONS, MICROPHONE_PERMISSIONS, ALWAYS_LOCATION_PERMISSIONS, CAMERA_PERMISSIONS, CONTACTS_PERMISSIONS);
        }
    }

    public void grantDefaultPermissionsToEnabledTelephonyDataServices(String[] strArr, int i) {
        Log.i(TAG, "Granting permissions to enabled data services for user:" + i);
        if (strArr == null) {
            return;
        }
        for (String str : strArr) {
            grantSystemFixedPermissionsToSystemPackage(this.NO_PM_CACHE, str, i, PHONE_PERMISSIONS, ALWAYS_LOCATION_PERMISSIONS);
        }
    }

    public void revokeDefaultPermissionsFromDisabledTelephonyDataServices(String[] strArr, int i) {
        Log.i(TAG, "Revoking permissions from disabled data services for user:" + i);
        if (strArr == null) {
            return;
        }
        for (String str : strArr) {
            PackageInfo systemPackageInfo = this.NO_PM_CACHE.getSystemPackageInfo(str);
            if (this.NO_PM_CACHE.isSystemPackage(systemPackageInfo) && doesPackageSupportRuntimePermissions(systemPackageInfo)) {
                revokeRuntimePermissions(this.NO_PM_CACHE, str, PHONE_PERMISSIONS, true, i);
                revokeRuntimePermissions(this.NO_PM_CACHE, str, ALWAYS_LOCATION_PERMISSIONS, true, i);
            }
        }
    }

    public void grantDefaultPermissionsToActiveLuiApp(String str, int i) {
        Log.i(TAG, "Granting permissions to active LUI app for user:" + i);
        grantSystemFixedPermissionsToSystemPackage(this.NO_PM_CACHE, str, i, CAMERA_PERMISSIONS, NOTIFICATION_PERMISSIONS);
    }

    public void revokeDefaultPermissionsFromLuiApps(String[] strArr, int i) {
        Log.i(TAG, "Revoke permissions from LUI apps for user:" + i);
        if (strArr == null) {
            return;
        }
        for (String str : strArr) {
            PackageInfo systemPackageInfo = this.NO_PM_CACHE.getSystemPackageInfo(str);
            if (this.NO_PM_CACHE.isSystemPackage(systemPackageInfo) && doesPackageSupportRuntimePermissions(systemPackageInfo)) {
                revokeRuntimePermissions(this.NO_PM_CACHE, str, CAMERA_PERMISSIONS, true, i);
            }
        }
    }

    public void grantDefaultPermissionsToCarrierServiceApp(String str, int i) {
        Log.i(TAG, "Grant permissions to Carrier Service app " + str + " for user:" + i);
        grantPermissionsToPackage(this.NO_PM_CACHE, str, i, false, true, NOTIFICATION_PERMISSIONS);
    }

    private String getDefaultSystemHandlerActivityPackage(PackageManagerWrapper packageManagerWrapper, String str, int i) {
        return getDefaultSystemHandlerActivityPackage(packageManagerWrapper, new Intent(str), i);
    }

    private String getDefaultSystemHandlerActivityPackage(PackageManagerWrapper packageManagerWrapper, Intent intent, int i) {
        ActivityInfo activityInfo;
        ResolveInfo resolveActivityAsUser = this.mContext.getPackageManager().resolveActivityAsUser(intent, DEFAULT_INTENT_QUERY_FLAGS, i);
        if (resolveActivityAsUser == null || (activityInfo = resolveActivityAsUser.activityInfo) == null || this.mServiceInternal.isResolveActivityComponent(activityInfo)) {
            return null;
        }
        String str = resolveActivityAsUser.activityInfo.packageName;
        if (packageManagerWrapper.isSystemPackage(str)) {
            return str;
        }
        return null;
    }

    private String getDefaultSystemHandlerServicePackage(PackageManagerWrapper packageManagerWrapper, String str, int i) {
        return getDefaultSystemHandlerServicePackage(packageManagerWrapper, new Intent(str), i);
    }

    private String getDefaultSystemHandlerServicePackage(PackageManagerWrapper packageManagerWrapper, Intent intent, int i) {
        List queryIntentServicesAsUser = this.mContext.getPackageManager().queryIntentServicesAsUser(intent, DEFAULT_INTENT_QUERY_FLAGS, i);
        if (queryIntentServicesAsUser == null) {
            return null;
        }
        int size = queryIntentServicesAsUser.size();
        for (int i2 = 0; i2 < size; i2++) {
            String str = ((ResolveInfo) queryIntentServicesAsUser.get(i2)).serviceInfo.packageName;
            if (packageManagerWrapper.isSystemPackage(str)) {
                return str;
            }
        }
        return null;
    }

    private ArrayList<String> getHeadlessSyncAdapterPackages(PackageManagerWrapper packageManagerWrapper, String[] strArr, int i) {
        ArrayList<String> arrayList = new ArrayList<>();
        Intent addCategory = new Intent("android.intent.action.MAIN").addCategory("android.intent.category.LAUNCHER");
        for (String str : strArr) {
            addCategory.setPackage(str);
            if (this.mContext.getPackageManager().resolveActivityAsUser(addCategory, DEFAULT_INTENT_QUERY_FLAGS, i) == null && packageManagerWrapper.isSystemPackage(str)) {
                arrayList.add(str);
            }
        }
        return arrayList;
    }

    private String getDefaultProviderAuthorityPackage(String str, int i) {
        ProviderInfo resolveContentProviderAsUser = this.mContext.getPackageManager().resolveContentProviderAsUser(str, DEFAULT_INTENT_QUERY_FLAGS, i);
        if (resolveContentProviderAsUser != null) {
            return resolveContentProviderAsUser.packageName;
        }
        return null;
    }

    private void grantRuntimePermissions(PackageManagerWrapper packageManagerWrapper, PackageInfo packageInfo, Set<String> set, boolean z, int i) {
        grantRuntimePermissions(packageManagerWrapper, packageInfo, set, z, false, true, i);
    }

    private void revokeRuntimePermissions(PackageManagerWrapper packageManagerWrapper, String str, Set<String> set, boolean z, int i) {
        PackageInfo systemPackageInfo = packageManagerWrapper.getSystemPackageInfo(str);
        if (systemPackageInfo == null || ArrayUtils.isEmpty(systemPackageInfo.requestedPermissions)) {
            return;
        }
        ArraySet arraySet = new ArraySet(Arrays.asList(systemPackageInfo.requestedPermissions));
        for (String str2 : set) {
            if (arraySet.contains(str2)) {
                UserHandle of = UserHandle.of(i);
                int permissionFlags = packageManagerWrapper.getPermissionFlags(str2, packageManagerWrapper.getPackageInfo(str), of);
                if ((permissionFlags & 32) != 0 && (permissionFlags & 4) == 0 && ((permissionFlags & 16) == 0 || z)) {
                    packageManagerWrapper.revokePermission(str2, systemPackageInfo, of);
                    packageManagerWrapper.updatePermissionFlags(str2, systemPackageInfo, 32, 0, of);
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Removed duplicated region for block: B:50:0x00c7  */
    /* JADX WARN: Removed duplicated region for block: B:60:0x00e1  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void grantRuntimePermissions(PackageManagerWrapper packageManagerWrapper, PackageInfo packageInfo, Set<String> set, boolean z, boolean z2, boolean z3, int i) {
        String[] strArr;
        ArraySet arraySet;
        int i2;
        int i3;
        String str;
        String str2;
        PackageInfo systemPackageInfo;
        UserHandle of = UserHandle.of(i);
        if (packageInfo == null) {
            return;
        }
        String[] strArr2 = packageInfo.requestedPermissions;
        if (ArrayUtils.isEmpty(strArr2)) {
            return;
        }
        String[] strArr3 = packageManagerWrapper.getPackageInfo(packageInfo.packageName).requestedPermissions;
        int length = strArr2.length;
        for (int i4 = 0; i4 < length; i4++) {
            if (!ArrayUtils.contains(strArr3, strArr2[i4])) {
                strArr2[i4] = null;
            }
        }
        String[] strArr4 = (String[]) ArrayUtils.filterNotNull(strArr2, new IntFunction() { // from class: com.android.server.pm.permission.DefaultPermissionGrantPolicy$$ExternalSyntheticLambda0
            @Override // java.util.function.IntFunction
            public final Object apply(int i5) {
                String[] lambda$grantRuntimePermissions$0;
                lambda$grantRuntimePermissions$0 = DefaultPermissionGrantPolicy.lambda$grantRuntimePermissions$0(i5);
                return lambda$grantRuntimePermissions$0;
            }
        });
        ArraySet arraySet2 = new ArraySet(set);
        ApplicationInfo applicationInfo = packageInfo.applicationInfo;
        int i5 = z ? 48 : 32;
        List splitPermissions = ((PermissionManager) this.mContext.getSystemService(PermissionManager.class)).getSplitPermissions();
        int size = splitPermissions.size();
        for (int i6 = 0; i6 < size; i6++) {
            PermissionManager.SplitPermissionInfo splitPermissionInfo = (PermissionManager.SplitPermissionInfo) splitPermissions.get(i6);
            if (applicationInfo != null && applicationInfo.targetSdkVersion < splitPermissionInfo.getTargetSdk() && set.contains(splitPermissionInfo.getSplitPermission())) {
                arraySet2.addAll(splitPermissionInfo.getNewPermissions());
            }
        }
        if (!z2 && applicationInfo != null && applicationInfo.isUpdatedSystemApp() && (systemPackageInfo = packageManagerWrapper.getSystemPackageInfo(this.mServiceInternal.getDisabledSystemPackageName(packageInfo.packageName))) != null) {
            if (ArrayUtils.isEmpty(systemPackageInfo.requestedPermissions)) {
                return;
            }
            if (!Arrays.equals(strArr4, systemPackageInfo.requestedPermissions)) {
                ArraySet arraySet3 = new ArraySet(Arrays.asList(strArr4));
                strArr = systemPackageInfo.requestedPermissions;
                arraySet = arraySet3;
                int length2 = strArr.length;
                String[] strArr5 = new String[length2];
                int i7 = 0;
                int i8 = 0;
                for (String str3 : strArr) {
                    if (packageManagerWrapper.getBackgroundPermission(str3) != null) {
                        strArr5[i8] = str3;
                        i8++;
                    } else {
                        strArr5[(length2 - 1) - i7] = str3;
                        i7++;
                    }
                }
                for (String str4 : strArr) {
                    if ((arraySet == null || arraySet.contains(str4)) && arraySet2.contains(str4)) {
                        int permissionFlags = packageManagerWrapper.getPermissionFlags(str4, packageInfo, of);
                        boolean z4 = z && (permissionFlags & 16) != 0;
                        boolean needRepairReadPhoneStatePermission = needRepairReadPhoneStatePermission(packageManagerWrapper, str4, packageInfo);
                        if (isFixedOrUserSet(permissionFlags) && !z2 && !z4 && !needRepairReadPhoneStatePermission) {
                            i2 = i5;
                            i3 = permissionFlags;
                            str2 = str4;
                        } else if ((permissionFlags & 4) == 0) {
                            i2 = i5 | (permissionFlags & 14336);
                            if (z3 && packageManagerWrapper.isPermissionRestricted(str4)) {
                                i3 = permissionFlags;
                                str = str4;
                                packageManagerWrapper.updatePermissionFlags(str4, packageInfo, 4096, 4096, of);
                            } else {
                                i3 = permissionFlags;
                                str = str4;
                            }
                            if (z4) {
                                packageManagerWrapper.updatePermissionFlags(str, packageInfo, i3, i3 & (-17), of);
                            }
                            String str5 = str;
                            if (!packageManagerWrapper.isGranted(str5, packageInfo, of)) {
                                packageManagerWrapper.grantPermission(str5, packageInfo, of);
                            }
                            str2 = str5;
                            packageManagerWrapper.updatePermissionFlags(str5, packageInfo, i2 | 64, i2, of);
                        }
                        if ((i3 & 32) != 0 && (i3 & 16) != 0 && !z) {
                            packageManagerWrapper.updatePermissionFlags(str2, packageInfo, 16, 0, of);
                        }
                        i5 = i2;
                    }
                }
            }
        }
        strArr = strArr4;
        arraySet = null;
        int length22 = strArr.length;
        String[] strArr52 = new String[length22];
        int i72 = 0;
        int i82 = 0;
        while (r2 < length22) {
        }
        while (r15 < length22) {
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ String[] lambda$grantRuntimePermissions$0(int i) {
        return new String[i];
    }

    private void grantDefaultPermissionExceptions(PackageManagerWrapper packageManagerWrapper, int i) {
        int i2;
        this.mHandler.removeMessages(1);
        synchronized (this.mLock) {
            if (this.mGrantExceptions == null) {
                this.mGrantExceptions = readDefaultPermissionExceptionsLocked(packageManagerWrapper);
            }
        }
        int size = this.mGrantExceptions.size();
        ArraySet arraySet = null;
        for (int i3 = 0; i3 < size; i3++) {
            PackageInfo systemPackageInfo = packageManagerWrapper.getSystemPackageInfo(this.mGrantExceptions.keyAt(i3));
            List<DefaultPermissionGrant> valueAt = this.mGrantExceptions.valueAt(i3);
            int size2 = valueAt.size();
            int i4 = 0;
            while (i4 < size2) {
                DefaultPermissionGrant defaultPermissionGrant = valueAt.get(i4);
                if (packageManagerWrapper.isPermissionDangerous(defaultPermissionGrant.name)) {
                    if (arraySet == null) {
                        arraySet = new ArraySet();
                    } else {
                        arraySet.clear();
                    }
                    ArraySet arraySet2 = arraySet;
                    arraySet2.add(defaultPermissionGrant.name);
                    i2 = i4;
                    grantRuntimePermissions(packageManagerWrapper, systemPackageInfo, arraySet2, defaultPermissionGrant.fixed, defaultPermissionGrant.whitelisted, true, i);
                    arraySet = arraySet2;
                } else {
                    Log.w(TAG, "Ignoring permission " + defaultPermissionGrant.name + " which isn't dangerous");
                    i2 = i4;
                }
                i4 = i2 + 1;
            }
        }
    }

    private File[] getDefaultPermissionFiles() {
        ArrayList<File> arrayList = new ArrayList<>();
        File file = new File(Environment.getRootDirectory(), "etc/default-permissions");
        if (file.isDirectory() && file.canRead()) {
            Collections.addAll(arrayList, file.listFiles());
        }
        File file2 = new File(Environment.getVendorDirectory(), "etc/default-permissions");
        if (file2.isDirectory() && file2.canRead()) {
            Collections.addAll(arrayList, file2.listFiles());
        }
        File file3 = new File(Environment.getOdmDirectory(), "etc/default-permissions");
        if (file3.isDirectory() && file3.canRead()) {
            Collections.addAll(arrayList, file3.listFiles());
        }
        File file4 = new File(Environment.getProductDirectory(), "etc/default-permissions");
        if (file4.isDirectory() && file4.canRead()) {
            Collections.addAll(arrayList, file4.listFiles());
        }
        File file5 = new File(Environment.getSystemExtDirectory(), "etc/default-permissions");
        if (file5.isDirectory() && file5.canRead()) {
            Collections.addAll(arrayList, file5.listFiles());
        }
        if (this.mContext.getPackageManager().hasSystemFeature("android.hardware.type.embedded", 0)) {
            file5 = new File(Environment.getOemDirectory(), "etc/default-permissions");
            if (file5.isDirectory() && file5.canRead()) {
                Collections.addAll(arrayList, file5.listFiles());
            }
        }
        this.mDefaultPermissionGrantPolicyExt.hookGetDefaultPermissionFiles(arrayList, file5);
        if (arrayList.isEmpty()) {
            return null;
        }
        return (File[]) arrayList.toArray(new File[0]);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public ArrayMap<String, List<DefaultPermissionGrant>> readDefaultPermissionExceptionsLocked(PackageManagerWrapper packageManagerWrapper) {
        File[] defaultPermissionFiles = getDefaultPermissionFiles();
        if (defaultPermissionFiles == null) {
            return new ArrayMap<>(0);
        }
        ArrayMap<String, List<DefaultPermissionGrant>> arrayMap = new ArrayMap<>();
        for (File file : defaultPermissionFiles) {
            if (!file.getPath().endsWith(".xml")) {
                Slog.i(TAG, "Non-xml file " + file + " in " + file.getParent() + " directory, ignoring");
            } else if (file.canRead()) {
                try {
                    FileInputStream fileInputStream = new FileInputStream(file);
                    try {
                        parse(packageManagerWrapper, Xml.resolvePullParser(fileInputStream), arrayMap);
                        fileInputStream.close();
                    } catch (Throwable th) {
                        try {
                            fileInputStream.close();
                        } catch (Throwable th2) {
                            th.addSuppressed(th2);
                        }
                        throw th;
                        break;
                    }
                } catch (IOException | XmlPullParserException e) {
                    Slog.w(TAG, "Error reading default permissions file " + file, e);
                }
            } else {
                Slog.w(TAG, "Default permissions file " + file + " cannot be read");
            }
        }
        return arrayMap;
    }

    private void parse(PackageManagerWrapper packageManagerWrapper, TypedXmlPullParser typedXmlPullParser, Map<String, List<DefaultPermissionGrant>> map) throws IOException, XmlPullParserException {
        int depth = typedXmlPullParser.getDepth();
        while (true) {
            int next = typedXmlPullParser.next();
            if (next == 1) {
                return;
            }
            if (next == 3 && typedXmlPullParser.getDepth() <= depth) {
                return;
            }
            if (next != 3 && next != 4) {
                if (TAG_EXCEPTIONS.equals(typedXmlPullParser.getName())) {
                    parseExceptions(packageManagerWrapper, typedXmlPullParser, map);
                } else {
                    Log.e(TAG, "Unknown tag " + typedXmlPullParser.getName());
                }
            }
        }
    }

    private void parseExceptions(PackageManagerWrapper packageManagerWrapper, TypedXmlPullParser typedXmlPullParser, Map<String, List<DefaultPermissionGrant>> map) throws IOException, XmlPullParserException {
        int depth = typedXmlPullParser.getDepth();
        while (true) {
            int next = typedXmlPullParser.next();
            if (next == 1) {
                return;
            }
            if (next == 3 && typedXmlPullParser.getDepth() <= depth) {
                return;
            }
            if (next != 3 && next != 4) {
                if (TAG_EXCEPTION.equals(typedXmlPullParser.getName())) {
                    String attributeValue = typedXmlPullParser.getAttributeValue((String) null, "package");
                    List<DefaultPermissionGrant> list = map.get(attributeValue);
                    if (list == null) {
                        PackageInfo systemPackageInfo = packageManagerWrapper.getSystemPackageInfo(attributeValue);
                        if (systemPackageInfo == null) {
                            Log.w(TAG, "No such package:" + attributeValue);
                            XmlUtils.skipCurrentTag(typedXmlPullParser);
                        } else if (!packageManagerWrapper.isSystemPackage(systemPackageInfo)) {
                            Log.w(TAG, "Unknown system package:" + attributeValue);
                            XmlUtils.skipCurrentTag(typedXmlPullParser);
                        } else if (!doesPackageSupportRuntimePermissions(systemPackageInfo)) {
                            Log.w(TAG, "Skipping non supporting runtime permissions package:" + attributeValue);
                            XmlUtils.skipCurrentTag(typedXmlPullParser);
                        } else {
                            list = new ArrayList<>();
                            map.put(attributeValue, list);
                        }
                    }
                    parsePermission(typedXmlPullParser, list);
                } else {
                    Log.e(TAG, "Unknown tag " + typedXmlPullParser.getName() + "under <exceptions>");
                }
            }
        }
    }

    private void parsePermission(TypedXmlPullParser typedXmlPullParser, List<DefaultPermissionGrant> list) throws IOException, XmlPullParserException {
        int depth = typedXmlPullParser.getDepth();
        while (true) {
            int next = typedXmlPullParser.next();
            if (next == 1) {
                return;
            }
            if (next == 3 && typedXmlPullParser.getDepth() <= depth) {
                return;
            }
            if (next != 3 && next != 4) {
                if ("permission".contains(typedXmlPullParser.getName())) {
                    String attributeValue = typedXmlPullParser.getAttributeValue((String) null, "name");
                    if (attributeValue == null) {
                        Log.w(TAG, "Mandatory name attribute missing for permission tag");
                        XmlUtils.skipCurrentTag(typedXmlPullParser);
                    } else {
                        list.add(new DefaultPermissionGrant(attributeValue, typedXmlPullParser.getAttributeBoolean((String) null, ATTR_FIXED, false), typedXmlPullParser.getAttributeBoolean((String) null, ATTR_WHITELISTED, false)));
                    }
                } else {
                    Log.e(TAG, "Unknown tag " + typedXmlPullParser.getName() + "under <exception>");
                }
            }
        }
    }

    private static boolean doesPackageSupportRuntimePermissions(PackageInfo packageInfo) {
        ApplicationInfo applicationInfo = packageInfo.applicationInfo;
        return applicationInfo != null && applicationInfo.targetSdkVersion > 22;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public abstract class PackageManagerWrapper {
        abstract PackageInfo getPackageInfo(String str);

        abstract int getPermissionFlags(String str, PackageInfo packageInfo, UserHandle userHandle);

        abstract PermissionInfo getPermissionInfo(String str);

        abstract void grantPermission(String str, PackageInfo packageInfo, UserHandle userHandle);

        abstract boolean isGranted(String str, PackageInfo packageInfo, UserHandle userHandle);

        abstract void revokePermission(String str, PackageInfo packageInfo, UserHandle userHandle);

        abstract void updatePermissionFlags(String str, PackageInfo packageInfo, int i, int i2, UserHandle userHandle);

        private PackageManagerWrapper() {
        }

        PackageInfo getSystemPackageInfo(String str) {
            PackageInfo packageInfo = getPackageInfo(str);
            if (packageInfo == null || !packageInfo.applicationInfo.isSystemApp()) {
                return null;
            }
            return packageInfo;
        }

        boolean isPermissionRestricted(String str) {
            PermissionInfo permissionInfo = getPermissionInfo(str);
            if (permissionInfo == null) {
                return false;
            }
            return permissionInfo.isRestricted();
        }

        boolean isPermissionDangerous(String str) {
            PermissionInfo permissionInfo = getPermissionInfo(str);
            return permissionInfo != null && permissionInfo.getProtection() == 1;
        }

        String getBackgroundPermission(String str) {
            PermissionInfo permissionInfo = getPermissionInfo(str);
            if (permissionInfo == null) {
                return null;
            }
            return permissionInfo.backgroundPermission;
        }

        boolean isSystemPackage(String str) {
            return isSystemPackage(getPackageInfo(str));
        }

        boolean isSystemPackage(PackageInfo packageInfo) {
            return (packageInfo == null || !packageInfo.applicationInfo.isSystemApp() || isSysComponentOrPersistentPlatformSignedPrivApp(packageInfo)) ? false : true;
        }

        boolean isSysComponentOrPersistentPlatformSignedPrivApp(PackageInfo packageInfo) {
            if (UserHandle.getAppId(packageInfo.applicationInfo.uid) < 10000) {
                return true;
            }
            if (!packageInfo.applicationInfo.isPrivilegedApp()) {
                return false;
            }
            PackageInfo systemPackageInfo = getSystemPackageInfo(DefaultPermissionGrantPolicy.this.mServiceInternal.getDisabledSystemPackageName(packageInfo.applicationInfo.packageName));
            if (systemPackageInfo != null) {
                ApplicationInfo applicationInfo = systemPackageInfo.applicationInfo;
                if (applicationInfo != null && (applicationInfo.flags & 8) == 0) {
                    return false;
                }
            } else if ((packageInfo.applicationInfo.flags & 8) == 0) {
                return false;
            }
            return DefaultPermissionGrantPolicy.this.mServiceInternal.isPlatformSigned(packageInfo.packageName);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public class DelayingPackageManagerCache extends PackageManagerWrapper {
        private SparseArray<ArrayMap<String, PermissionState>> mDelayedPermissionState;
        private ArrayMap<String, PackageInfo> mPackageInfos;
        private ArrayMap<String, PermissionInfo> mPermissionInfos;
        private SparseArray<Context> mUserContexts;

        private DelayingPackageManagerCache() {
            super();
            this.mDelayedPermissionState = new SparseArray<>();
            this.mUserContexts = new SparseArray<>();
            this.mPermissionInfos = new ArrayMap<>();
            this.mPackageInfos = new ArrayMap<>();
        }

        void apply() {
            PackageManager.corkPackageInfoCache();
            for (int i = 0; i < this.mDelayedPermissionState.size(); i++) {
                for (int i2 = 0; i2 < this.mDelayedPermissionState.valueAt(i).size(); i2++) {
                    try {
                        this.mDelayedPermissionState.valueAt(i).valueAt(i2).apply();
                    } catch (IllegalArgumentException e) {
                        Slog.w(DefaultPermissionGrantPolicy.TAG, "Cannot set permission " + this.mDelayedPermissionState.valueAt(i).keyAt(i2) + " of uid " + this.mDelayedPermissionState.keyAt(i), e);
                    }
                }
            }
            PackageManager.uncorkPackageInfoCache();
        }

        void addPackageInfo(String str, PackageInfo packageInfo) {
            this.mPackageInfos.put(str, packageInfo);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public Context createContextAsUser(UserHandle userHandle) {
            int indexOfKey = this.mUserContexts.indexOfKey(userHandle.getIdentifier());
            if (indexOfKey >= 0) {
                return this.mUserContexts.valueAt(indexOfKey);
            }
            Context createContextAsUser = DefaultPermissionGrantPolicy.this.mContext.createContextAsUser(userHandle, 0);
            this.mUserContexts.put(userHandle.getIdentifier(), createContextAsUser);
            return createContextAsUser;
        }

        private PermissionState getPermissionState(String str, PackageInfo packageInfo, UserHandle userHandle) {
            ArrayMap<String, PermissionState> arrayMap;
            int uid = UserHandle.getUid(userHandle.getIdentifier(), UserHandle.getAppId(packageInfo.applicationInfo.uid));
            int indexOfKey = this.mDelayedPermissionState.indexOfKey(uid);
            if (indexOfKey >= 0) {
                arrayMap = this.mDelayedPermissionState.valueAt(indexOfKey);
            } else {
                ArrayMap<String, PermissionState> arrayMap2 = new ArrayMap<>();
                this.mDelayedPermissionState.put(uid, arrayMap2);
                arrayMap = arrayMap2;
            }
            int indexOfKey2 = arrayMap.indexOfKey(str);
            if (indexOfKey2 >= 0) {
                PermissionState valueAt = arrayMap.valueAt(indexOfKey2);
                if (ArrayUtils.contains(valueAt.mPkgRequestingPerm.requestedPermissions, str) || !ArrayUtils.contains(packageInfo.requestedPermissions, str)) {
                    return valueAt;
                }
                valueAt.mPkgRequestingPerm = packageInfo;
                return valueAt;
            }
            PermissionState permissionState = new PermissionState(str, packageInfo, userHandle);
            arrayMap.put(str, permissionState);
            return permissionState;
        }

        @Override // com.android.server.pm.permission.DefaultPermissionGrantPolicy.PackageManagerWrapper
        public int getPermissionFlags(String str, PackageInfo packageInfo, UserHandle userHandle) {
            PermissionState permissionState = getPermissionState(str, packageInfo, userHandle);
            permissionState.initFlags();
            return permissionState.newFlags.intValue();
        }

        @Override // com.android.server.pm.permission.DefaultPermissionGrantPolicy.PackageManagerWrapper
        public void updatePermissionFlags(String str, PackageInfo packageInfo, int i, int i2, UserHandle userHandle) {
            PermissionState permissionState = getPermissionState(str, packageInfo, userHandle);
            permissionState.initFlags();
            permissionState.newFlags = Integer.valueOf((permissionState.newFlags.intValue() & (~i)) | (i2 & i));
        }

        @Override // com.android.server.pm.permission.DefaultPermissionGrantPolicy.PackageManagerWrapper
        public void grantPermission(String str, PackageInfo packageInfo, UserHandle userHandle) {
            PermissionState permissionState = getPermissionState(str, packageInfo, userHandle);
            permissionState.initGranted();
            permissionState.newGranted = Boolean.TRUE;
        }

        @Override // com.android.server.pm.permission.DefaultPermissionGrantPolicy.PackageManagerWrapper
        public void revokePermission(String str, PackageInfo packageInfo, UserHandle userHandle) {
            PermissionState permissionState = getPermissionState(str, packageInfo, userHandle);
            permissionState.initGranted();
            permissionState.newGranted = Boolean.FALSE;
        }

        @Override // com.android.server.pm.permission.DefaultPermissionGrantPolicy.PackageManagerWrapper
        public boolean isGranted(String str, PackageInfo packageInfo, UserHandle userHandle) {
            PermissionState permissionState = getPermissionState(str, packageInfo, userHandle);
            permissionState.initGranted();
            return permissionState.newGranted.booleanValue();
        }

        @Override // com.android.server.pm.permission.DefaultPermissionGrantPolicy.PackageManagerWrapper
        public PermissionInfo getPermissionInfo(String str) {
            int indexOfKey = this.mPermissionInfos.indexOfKey(str);
            if (indexOfKey >= 0) {
                return this.mPermissionInfos.valueAt(indexOfKey);
            }
            PermissionInfo permissionInfo = DefaultPermissionGrantPolicy.this.NO_PM_CACHE.getPermissionInfo(str);
            this.mPermissionInfos.put(str, permissionInfo);
            return permissionInfo;
        }

        @Override // com.android.server.pm.permission.DefaultPermissionGrantPolicy.PackageManagerWrapper
        public PackageInfo getPackageInfo(String str) {
            int indexOfKey = this.mPackageInfos.indexOfKey(str);
            if (indexOfKey >= 0) {
                return this.mPackageInfos.valueAt(indexOfKey);
            }
            PackageInfo packageInfo = DefaultPermissionGrantPolicy.this.NO_PM_CACHE.getPackageInfo(str);
            this.mPackageInfos.put(str, packageInfo);
            return packageInfo;
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
        public class PermissionState {
            private Integer mOriginalFlags;
            private Boolean mOriginalGranted;
            private final String mPermission;
            private PackageInfo mPkgRequestingPerm;
            private final UserHandle mUser;
            Integer newFlags;
            Boolean newGranted;

            private PermissionState(String str, PackageInfo packageInfo, UserHandle userHandle) {
                this.mPermission = str;
                this.mPkgRequestingPerm = packageInfo;
                this.mUser = userHandle;
            }

            void apply() {
                int i;
                int i2;
                Integer num = this.newFlags;
                if (num != null) {
                    i = num.intValue() & (~this.mOriginalFlags.intValue());
                    i2 = this.mOriginalFlags.intValue() & (~this.newFlags.intValue());
                } else {
                    i = 0;
                    i2 = 0;
                }
                if (i2 != 0) {
                    DefaultPermissionGrantPolicy.this.NO_PM_CACHE.updatePermissionFlags(this.mPermission, this.mPkgRequestingPerm, i2, 0, this.mUser);
                }
                int i3 = i & 14336;
                if (i3 != 0) {
                    DefaultPermissionGrantPolicy.this.NO_PM_CACHE.updatePermissionFlags(this.mPermission, this.mPkgRequestingPerm, i3, -1, this.mUser);
                }
                Boolean bool = this.newGranted;
                if (bool != null && !Objects.equals(bool, this.mOriginalGranted)) {
                    if (this.newGranted.booleanValue()) {
                        DefaultPermissionGrantPolicy.this.NO_PM_CACHE.grantPermission(this.mPermission, this.mPkgRequestingPerm, this.mUser);
                    } else {
                        DefaultPermissionGrantPolicy.this.NO_PM_CACHE.revokePermission(this.mPermission, this.mPkgRequestingPerm, this.mUser);
                    }
                }
                int i4 = i & (-14337);
                if (i4 != 0) {
                    DefaultPermissionGrantPolicy.this.NO_PM_CACHE.updatePermissionFlags(this.mPermission, this.mPkgRequestingPerm, i4, -1, this.mUser);
                }
            }

            void initFlags() {
                if (this.newFlags == null) {
                    Integer valueOf = Integer.valueOf(DefaultPermissionGrantPolicy.this.NO_PM_CACHE.getPermissionFlags(this.mPermission, this.mPkgRequestingPerm, this.mUser));
                    this.mOriginalFlags = valueOf;
                    this.newFlags = valueOf;
                }
            }

            void initGranted() {
                if (this.newGranted == null) {
                    Boolean valueOf = Boolean.valueOf(DelayingPackageManagerCache.this.createContextAsUser(this.mUser).getPackageManager().checkPermission(this.mPermission, this.mPkgRequestingPerm.packageName) == 0);
                    this.mOriginalGranted = valueOf;
                    this.newGranted = valueOf;
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public static final class DefaultPermissionGrant {
        final boolean fixed;
        final String name;
        final boolean whitelisted;

        public DefaultPermissionGrant(String str, boolean z, boolean z2) {
            this.name = str;
            this.fixed = z;
            this.whitelisted = z2;
        }
    }

    public IDefaultPermissionGrantPolicyWrapper getWrapper() {
        return this.mWrapper;
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    private class DefaultPermissionGrantPolicyWrapper implements IDefaultPermissionGrantPolicyWrapper {
        private DefaultPermissionGrantPolicyWrapper() {
        }

        @Override // com.android.server.pm.permission.IDefaultPermissionGrantPolicyWrapper
        public Object getNoPmCache() {
            return DefaultPermissionGrantPolicy.this.NO_PM_CACHE;
        }

        @Override // com.android.server.pm.permission.IDefaultPermissionGrantPolicyWrapper
        public void grantRuntimePermissions(Object obj, PackageInfo packageInfo, Set<String> set, boolean z, boolean z2, boolean z3, int i) {
            if (obj instanceof PackageManagerWrapper) {
                DefaultPermissionGrantPolicy.this.grantRuntimePermissions((PackageManagerWrapper) obj, packageInfo, set, z, z2, z3, i);
            } else {
                Slog.e(DefaultPermissionGrantPolicy.TAG, "wrapp -> grantRuntimePermissions, wrong argument");
            }
        }
    }

    private boolean needRepairReadPhoneStatePermission(PackageManagerWrapper packageManagerWrapper, String str, PackageInfo packageInfo) {
        return str != null && str.equals("android.permission.READ_PHONE_STATE") && packageManagerWrapper.isSysComponentOrPersistentPlatformSignedPrivApp(packageInfo);
    }
}
