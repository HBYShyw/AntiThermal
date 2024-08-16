package com.android.server.pm;

import android.os.UserHandle;
import com.android.server.pm.ApexManager;
import com.android.server.pm.parsing.pkg.ParsedPackage;
import com.android.server.pm.pkg.AndroidPackage;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public interface IInstallPackageHelperWrapper {
    default AndroidPackage addForInitLI(ParsedPackage parsedPackage, int i, int i2, UserHandle userHandle, ApexManager.ActiveApexInfo activeApexInfo) throws PackageManagerException {
        throw new PackageManagerException(-110, "default impl of addForInitLI in wrapper");
    }
}
