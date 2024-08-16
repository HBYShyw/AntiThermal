package com.android.server.pm.parsing.pkg;

import com.android.internal.content.om.OverlayConfig;
import com.android.server.pm.pkg.AndroidPackage;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public interface AndroidPackageInternal extends AndroidPackage, OverlayConfig.PackageProvider.Package {
    String[] getUsesLibrariesSorted();

    String[] getUsesOptionalLibrariesSorted();

    String[] getUsesSdkLibrariesSorted();

    String[] getUsesStaticLibrariesSorted();
}
