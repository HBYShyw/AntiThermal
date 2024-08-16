package com.android.server.pm.pkg.parsing;

import android.content.pm.ApplicationInfo;
import android.content.pm.IApplicationInfoExt;
import com.android.server.pm.pkg.PackageUserState;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public interface IParsingPackageUtilsExt {
    default void adjustPermissionInParseBaseApkTags(ParsingPackage parsingPackage) {
    }

    default void adjustResultInGenerateApplicationInfoUnchecked(ApplicationInfo applicationInfo, PackageUserState packageUserState, IApplicationInfoExt iApplicationInfoExt) {
    }
}
