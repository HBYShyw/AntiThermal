package com.android.server.pm;

import com.android.internal.annotations.VisibleForTesting;
import com.android.server.pm.pkg.AndroidPackage;
import com.android.server.pm.pkg.PackageStateInternal;

/* JADX INFO: Access modifiers changed from: package-private */
@VisibleForTesting(visibility = VisibleForTesting.Visibility.PRIVATE)
/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public interface FeatureConfig {
    void enableLogging(int i, boolean z);

    boolean isGloballyEnabled();

    boolean isLoggingEnabled(int i);

    void onSystemReady();

    boolean packageIsEnabled(AndroidPackage androidPackage);

    FeatureConfig snapshot();

    void updatePackageState(PackageStateInternal packageStateInternal, boolean z);
}
