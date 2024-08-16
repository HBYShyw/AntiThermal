package com.android.server.pm;

import com.android.server.pm.parsing.pkg.ParsedPackage;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public interface IPackageManagerServiceUtilsExt {
    default void addBootEvent(String str) {
    }

    default void afterCreateNewSettingInScanPackageOnlyLI(ParsedPackage parsedPackage, PackageSetting packageSetting, ScanRequest scanRequest) {
    }

    default boolean skipSharedUserSigMismatchInReconcilePackage(ParsedPackage parsedPackage) {
        return false;
    }
}
