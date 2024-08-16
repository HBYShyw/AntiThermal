package com.android.server.pm.dex;

import android.os.Binder;
import android.os.UserHandle;
import android.util.Log;
import com.android.internal.annotations.GuardedBy;
import com.android.server.pm.Installer;
import com.android.server.pm.parsing.PackageInfoUtils;
import com.android.server.pm.pkg.AndroidPackage;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public class ViewCompiler {
    private final Object mInstallLock;

    @GuardedBy({"mInstallLock"})
    private final Installer mInstaller;

    public ViewCompiler(Object obj, Installer installer) {
        this.mInstallLock = obj;
        this.mInstaller = installer;
    }

    public boolean compileLayouts(AndroidPackage androidPackage) {
        boolean compileLayouts;
        try {
            String packageName = androidPackage.getPackageName();
            String baseApkPath = androidPackage.getBaseApkPath();
            String str = PackageInfoUtils.getDataDir(androidPackage, UserHandle.myUserId()).getAbsolutePath() + "/code_cache/compiled_view.dex";
            Log.i("PackageManager", "Compiling layouts in " + packageName + " (" + baseApkPath + ") to " + str);
            long clearCallingIdentity = Binder.clearCallingIdentity();
            try {
                synchronized (this.mInstallLock) {
                    compileLayouts = this.mInstaller.compileLayouts(baseApkPath, packageName, str, androidPackage.getUid());
                }
                return compileLayouts;
            } finally {
                Binder.restoreCallingIdentity(clearCallingIdentity);
            }
        } catch (Throwable th) {
            Log.e("PackageManager", "Failed to compile layouts", th);
            return false;
        }
    }
}
