package com.android.server.pm.pkg;

import android.annotation.SystemApi;
import android.content.pm.overlay.OverlayPaths;
import android.processor.immutability.Immutable;
import android.util.ArraySet;
import java.util.Map;

@Immutable
@SystemApi(client = SystemApi.Client.SYSTEM_SERVER)
/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public interface PackageUserState {
    public static final PackageUserState DEFAULT = PackageUserStateInternal.DEFAULT;

    @Immutable.Ignore
    OverlayPaths getAllOverlayPaths();

    long getCeDataInode();

    ArraySet<String> getDisabledComponents();

    int getDistractionFlags();

    ArraySet<String> getEnabledComponents();

    int getEnabledState();

    long getFirstInstallTimeMillis();

    String getHarmfulAppWarning();

    int getInstallReason();

    String getLastDisableAppCaller();

    int getOplusFreezeState();

    @Immutable.Ignore
    OverlayPaths getOverlayPaths();

    @Immutable.Ignore
    Map<String, OverlayPaths> getSharedLibraryOverlayPaths();

    String getSplashScreenTheme();

    int getUninstallReason();

    boolean ignorePackageDisabledInIsEnabled(int i, long j);

    boolean isComponentDisabled(String str);

    boolean isComponentEnabled(String str);

    boolean isHidden();

    boolean isInstalled();

    boolean isInstantApp();

    boolean isNotLaunched();

    boolean isStopped();

    boolean isSuspended();

    boolean isVirtualPreload();
}
