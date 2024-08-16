package com.android.server.pm;

import android.content.pm.parsing.PackageLite;
import java.io.File;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public interface IPackageInstallerSessionWrapper {
    default void extractNativeLibraries(PackageLite packageLite, File file, String str, boolean z) throws PackageManagerException {
    }
}
