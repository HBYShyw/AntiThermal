package com.android.server.pm;

import com.android.server.pm.dex.DexManager;
import com.android.server.pm.permission.PermissionManagerServiceInternal;
import com.android.server.pm.pkg.AndroidPackage;
import com.android.server.pm.resolution.ComponentResolver;
import com.android.server.utils.WatchedArrayMap;
import java.io.File;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public interface IPackageManagerServiceWrapper {
    default ApexManager getApexManager() {
        return null;
    }

    default AppDataHelper getAppDataHelper() {
        return null;
    }

    default AppsFilterImpl getAppsFilter() {
        return null;
    }

    default File getCacheDir() {
        return null;
    }

    default ComponentResolver getComponentResolver() {
        return null;
    }

    default DexManager getDexManager() {
        return null;
    }

    default DexOptHelper getDexOptHelper() {
        return null;
    }

    default InitAppsHelper getInitAppsHelper() {
        return null;
    }

    default InstallPackageHelper getInstallPackageHelper() {
        return null;
    }

    default ComputerLocked getLiveComputer() {
        return null;
    }

    default PackageDexOptimizer getPackageDexOptimizer() {
        return null;
    }

    default WatchedArrayMap<String, AndroidPackage> getPackages() {
        return null;
    }

    default PermissionManagerServiceInternal getPermissionManager() {
        return null;
    }

    default RemovePackageHelper getRemovePackageHelper() {
        return null;
    }

    default ResolveIntentHelper getResolveIntentHelper() {
        return null;
    }

    default SharedLibrariesImpl getSharedLibraries() {
        return null;
    }
}
