package com.android.server.pm.parsing.library;

import com.android.internal.annotations.VisibleForTesting;
import com.android.server.pm.parsing.pkg.ParsedPackage;

@VisibleForTesting
/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public class ComGoogleAndroidMapsUpdater extends PackageSharedLibraryUpdater {
    private static final String LIBRARY_NAME = "com.google.android.maps";

    @Override // com.android.server.pm.parsing.library.PackageSharedLibraryUpdater
    public void updatePackage(ParsedPackage parsedPackage, boolean z, boolean z2) {
        parsedPackage.removeUsesLibrary(LIBRARY_NAME);
        parsedPackage.removeUsesOptionalLibrary(LIBRARY_NAME);
    }
}
