package com.android.server.pm.pkg;

import android.content.pm.ComponentInfo;
import com.android.server.pm.pkg.component.ParsedMainComponent;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public class PackageUserStateUtils {
    private static final boolean DEBUG = false;
    private static final String TAG = "PackageUserStateUtils";

    public static boolean reportIfDebug(boolean z, long j) {
        return z;
    }

    public static boolean isMatch(PackageUserState packageUserState, ComponentInfo componentInfo, long j) {
        return isMatch(packageUserState, componentInfo.applicationInfo.isSystemApp(), componentInfo.applicationInfo.enabled, componentInfo.enabled, componentInfo.directBootAware, componentInfo.name, j);
    }

    public static boolean isMatch(PackageUserState packageUserState, boolean z, boolean z2, ParsedMainComponent parsedMainComponent, long j) {
        return isMatch(packageUserState, z, z2, parsedMainComponent.isEnabled(), parsedMainComponent.isDirectBootAware(), parsedMainComponent.getName(), j);
    }

    public static boolean isMatch(PackageUserState packageUserState, boolean z, boolean z2, boolean z3, boolean z4, String str, long j) {
        boolean z5 = true;
        boolean z6 = (4202496 & j) != 0;
        if (!isAvailable(packageUserState, j) && (!z || !z6)) {
            return reportIfDebug(false, j);
        }
        if (!isEnabled(packageUserState, z2, z3, str, j)) {
            return reportIfDebug(false, j);
        }
        if ((1048576 & j) != 0 && !z) {
            return reportIfDebug(false, j);
        }
        boolean z7 = ((262144 & j) == 0 || z4) ? false : true;
        boolean z8 = (524288 & j) != 0 && z4;
        if (!z7 && !z8) {
            z5 = false;
        }
        return reportIfDebug(z5, j);
    }

    public static boolean isAvailable(PackageUserState packageUserState, long j) {
        boolean z = (4194304 & j) != 0;
        boolean z2 = (j & 8192) != 0;
        if (z) {
            return true;
        }
        return packageUserState.isInstalled() && (!packageUserState.isHidden() || z2);
    }

    public static boolean isEnabled(PackageUserState packageUserState, ComponentInfo componentInfo, long j) {
        return isEnabled(packageUserState, componentInfo.applicationInfo.enabled, componentInfo.enabled, componentInfo.name, j);
    }

    public static boolean isEnabled(PackageUserState packageUserState, boolean z, ParsedMainComponent parsedMainComponent, long j) {
        return isEnabled(packageUserState, z, parsedMainComponent.isEnabled(), parsedMainComponent.getName(), j);
    }

    /* JADX WARN: Removed duplicated region for block: B:18:0x0039 A[RETURN] */
    /* JADX WARN: Removed duplicated region for block: B:19:0x003a  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public static boolean isEnabled(PackageUserState packageUserState, boolean z, boolean z2, String str, long j) {
        if ((512 & j) != 0) {
            return true;
        }
        int enabledState = packageUserState.getEnabledState();
        if (enabledState != 0) {
            if (enabledState == 2 || enabledState == 3) {
                if (!packageUserState.ignorePackageDisabledInIsEnabled(packageUserState.getEnabledState(), j)) {
                    return false;
                }
            } else if (enabledState == 4) {
                if ((j & 32768) == 0) {
                    return false;
                }
            }
            if (!packageUserState.isComponentEnabled(str)) {
                return true;
            }
            if (packageUserState.isComponentDisabled(str)) {
                return false;
            }
            return z2;
        }
        if (!z) {
            return false;
        }
        if (!packageUserState.isComponentEnabled(str)) {
        }
    }

    public static boolean isPackageEnabled(PackageUserState packageUserState, AndroidPackage androidPackage) {
        int enabledState = packageUserState.getEnabledState();
        if (enabledState == 1) {
            return true;
        }
        if (enabledState == 2 || enabledState == 3 || enabledState == 4) {
            return false;
        }
        return androidPackage.isEnabled();
    }
}
