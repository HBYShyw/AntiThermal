package com.android.server.pm;

import com.android.server.pm.Installer;
import com.android.server.pm.pkg.AndroidPackage;
import com.android.server.pm.pkg.PackageState;
import com.android.server.pm.pkg.PackageStateInternal;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public interface IAppDataHelperWrapper {
    default void assertPackageStorageValid(Computer computer, String str, String str2, int i) throws PackageManagerException {
    }

    default void executeBatchLI(Installer.Batch batch) {
    }

    default IAppDataHelperExt getExtImpl() {
        return null;
    }

    default void prepareAppDataAndMigrate(Installer.Batch batch, PackageState packageState, AndroidPackage androidPackage, int i, int i2, boolean z) {
    }

    default void prepareAppDataContentsLeafLIF(AndroidPackage androidPackage, PackageStateInternal packageStateInternal, int i, int i2) {
    }

    default boolean shouldHaveAppStorage(AndroidPackage androidPackage) {
        return true;
    }
}
