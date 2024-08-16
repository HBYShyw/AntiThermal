package com.mediatek.server.pm;

import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.LauncherActivityInfoInternal;
import android.content.pm.PackageInfo;
import android.os.RemoteException;
import android.os.UserHandle;
import com.android.server.pm.PackageManagerException;
import com.android.server.pm.PackageManagerService;
import com.android.server.pm.PackageSetting;
import com.android.server.pm.UserManagerService;
import com.android.server.pm.parsing.PackageParser2;
import com.android.server.pm.parsing.pkg.ParsedPackage;
import com.android.server.pm.pkg.AndroidPackage;
import com.android.server.utils.WatchedArrayMap;
import java.io.PrintWriter;
import java.util.List;
import java.util.concurrent.ExecutorService;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
public class PmsExt {
    public static final int INDEX_CIP_FW = 1;
    public static final int INDEX_CUSTOM_APP = 6;
    public static final int INDEX_CUSTOM_PLUGIN = 7;
    public static final int INDEX_ROOT_PLUGIN = 4;
    public static final int INDEX_RSC_PRODUCT_APP = 19;
    public static final int INDEX_RSC_PRODUCT_OVERLAY = 10;
    public static final int INDEX_RSC_PRODUCT_PRIV = 18;
    public static final int INDEX_RSC_SYSTEM_APP = 15;
    public static final int INDEX_RSC_SYSTEM_EXT_APP = 17;
    public static final int INDEX_RSC_SYSTEM_EXT_OVERLAY = 9;
    public static final int INDEX_RSC_SYSTEM_EXT_PRIV = 16;
    public static final int INDEX_RSC_SYSTEM_FW = 12;
    public static final int INDEX_RSC_SYSTEM_OVERLAY = 8;
    public static final int INDEX_RSC_SYSTEM_PLUGIN = 22;
    public static final int INDEX_RSC_SYSTEM_PRIV = 14;
    public static final int INDEX_RSC_VENDOR_APP = 21;
    public static final int INDEX_RSC_VENDOR_FW = 13;
    public static final int INDEX_RSC_VENDOR_OVERLAY = 11;
    public static final int INDEX_RSC_VENDOR_PLUGIN = 23;
    public static final int INDEX_RSC_VENDOR_PRIV = 20;
    public static final int INDEX_VENDOR_FW = 2;
    public static final int INDEX_VENDOR_OP_APP = 3;
    public static final int INDEX_VENDOR_PLUGIN = 5;

    public void checkMtkResPkg(AndroidPackage androidPackage) throws PackageManagerException {
    }

    public int customizeDeletePkg(int[] iArr, String str, int i, int i2, boolean z) {
        return 1;
    }

    public int customizeDeletePkgFlags(int i, String str) {
        return i;
    }

    public int customizeInstallPkgFlags(int i, String str, WatchedArrayMap<String, PackageSetting> watchedArrayMap, UserHandle userHandle) {
        return i;
    }

    public boolean dumpCmdHandle(String str, PrintWriter printWriter, String[] strArr, int i) {
        return false;
    }

    public void init(PackageManagerService packageManagerService, UserManagerService userManagerService) {
    }

    public void initAfterScan(WatchedArrayMap<String, PackageSetting> watchedArrayMap) {
    }

    public void initBeforeScan() {
    }

    public boolean isRemovableSysApp(String str) {
        return false;
    }

    public boolean needSkipAppInfo(ApplicationInfo applicationInfo) {
        return false;
    }

    public boolean needSkipScanning(ParsedPackage parsedPackage, PackageSetting packageSetting, PackageSetting packageSetting2) {
        return false;
    }

    public void onPackageAdded(String str, PackageSetting packageSetting, int i) {
    }

    public void scanDirLI(int i, int i2, int i3, long j, PackageParser2 packageParser2, ExecutorService executorService) {
    }

    public void scanDirLI(int i, boolean z, int i2, int i3, long j, PackageParser2 packageParser2, ExecutorService executorService) {
    }

    public void scanMoreDirLi(int i, int i2, PackageParser2 packageParser2, ExecutorService executorService) {
    }

    public ActivityInfo updateActivityInfoForRemovable(ActivityInfo activityInfo) throws RemoteException {
        return activityInfo;
    }

    public ApplicationInfo updateApplicationInfoForRemovable(ApplicationInfo applicationInfo) {
        return applicationInfo;
    }

    public ApplicationInfo updateApplicationInfoForRemovable(String str, ApplicationInfo applicationInfo) {
        return applicationInfo;
    }

    public boolean updateNativeLibDir(ApplicationInfo applicationInfo, String str) {
        return false;
    }

    public PackageInfo updatePackageInfoForRemovable(PackageInfo packageInfo) {
        return packageInfo;
    }

    public void updatePackageSettings(int i, String str, AndroidPackage androidPackage, PackageSetting packageSetting, int[] iArr, String str2) {
    }

    public List<LauncherActivityInfoInternal> updateResolveInfoListForRemovable(List<LauncherActivityInfoInternal> list) throws RemoteException {
        return list;
    }
}
