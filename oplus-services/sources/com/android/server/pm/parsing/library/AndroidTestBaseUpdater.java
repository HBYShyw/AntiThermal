package com.android.server.pm.parsing.library;

import android.os.RemoteException;
import android.os.ServiceManager;
import android.util.Log;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.compat.IPlatformCompat;
import com.android.server.pm.parsing.pkg.AndroidPackageUtils;
import com.android.server.pm.parsing.pkg.ParsedPackage;
import com.android.server.pm.pkg.AndroidPackage;

@VisibleForTesting
/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public class AndroidTestBaseUpdater extends PackageSharedLibraryUpdater {
    private static final long REMOVE_ANDROID_TEST_BASE = 133396946;
    private static final String TAG = "AndroidTestBaseUpdater";

    private static boolean isChangeEnabled(AndroidPackage androidPackage, boolean z) {
        if (!z) {
            try {
                return IPlatformCompat.Stub.asInterface(ServiceManager.getService("platform_compat")).isChangeEnabled(REMOVE_ANDROID_TEST_BASE, AndroidPackageUtils.generateAppInfoWithoutState(androidPackage));
            } catch (RemoteException | NullPointerException e) {
                Log.e(TAG, "Failed to get a response from PLATFORM_COMPAT_SERVICE", e);
            }
        }
        return androidPackage.getTargetSdkVersion() > 29;
    }

    @Override // com.android.server.pm.parsing.library.PackageSharedLibraryUpdater
    public void updatePackage(ParsedPackage parsedPackage, boolean z, boolean z2) {
        if (!isChangeEnabled(parsedPackage, z)) {
            prefixRequiredLibrary(parsedPackage, "android.test.base");
        } else {
            prefixImplicitDependency(parsedPackage, "android.test.runner", "android.test.base");
        }
    }
}
