package com.android.server.pm.parsing.library;

import com.android.internal.annotations.VisibleForTesting;
import com.android.server.pm.parsing.pkg.ParsedPackage;

@VisibleForTesting
/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public class AndroidHidlUpdater extends PackageSharedLibraryUpdater {
    @Override // com.android.server.pm.parsing.library.PackageSharedLibraryUpdater
    public void updatePackage(ParsedPackage parsedPackage, boolean z, boolean z2) {
        if ((parsedPackage.getTargetSdkVersion() <= 28) && (z || z2)) {
            prefixRequiredLibrary(parsedPackage, "android.hidl.base-V1.0-java");
            prefixRequiredLibrary(parsedPackage, "android.hidl.manager-V1.0-java");
        } else {
            PackageSharedLibraryUpdater.removeLibrary(parsedPackage, "android.hidl.base-V1.0-java");
            PackageSharedLibraryUpdater.removeLibrary(parsedPackage, "android.hidl.manager-V1.0-java");
        }
    }
}
