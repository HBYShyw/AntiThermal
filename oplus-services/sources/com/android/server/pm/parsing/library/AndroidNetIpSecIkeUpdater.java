package com.android.server.pm.parsing.library;

import com.android.internal.annotations.VisibleForTesting;
import com.android.server.pm.parsing.pkg.ParsedPackage;

@VisibleForTesting
/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public class AndroidNetIpSecIkeUpdater extends PackageSharedLibraryUpdater {
    private static final String LIBRARY_NAME = "android.net.ipsec.ike";

    @Override // com.android.server.pm.parsing.library.PackageSharedLibraryUpdater
    public void updatePackage(ParsedPackage parsedPackage, boolean z, boolean z2) {
        PackageSharedLibraryUpdater.removeLibrary(parsedPackage, LIBRARY_NAME);
    }
}
