package com.android.server.pm;

import android.content.ComponentName;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import com.android.internal.annotations.VisibleForTesting;
import com.android.server.pm.PackageManagerService;

@VisibleForTesting(visibility = VisibleForTesting.Visibility.PRIVATE)
/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public final class ComputerLocked extends ComputerEngine {
    /* JADX INFO: Access modifiers changed from: package-private */
    public ComputerLocked(PackageManagerService.Snapshot snapshot) {
        super(snapshot, -1);
    }

    @Override // com.android.server.pm.ComputerEngine
    protected ComponentName resolveComponentName() {
        return this.mService.getResolveComponentName();
    }

    @Override // com.android.server.pm.ComputerEngine
    protected ActivityInfo instantAppInstallerActivity() {
        return this.mService.mInstantAppInstallerActivity;
    }

    @Override // com.android.server.pm.ComputerEngine
    protected ApplicationInfo androidApplication() {
        return this.mService.getCoreAndroidApplication();
    }
}
