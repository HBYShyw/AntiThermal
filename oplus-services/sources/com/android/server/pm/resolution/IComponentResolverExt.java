package com.android.server.pm.resolution;

import com.android.server.pm.pkg.AndroidPackage;
import com.android.server.pm.pkg.PackageStateInternal;
import com.android.server.pm.pkg.component.ParsedActivity;
import com.android.server.pm.pkg.component.ParsedProvider;
import com.android.server.pm.pkg.component.ParsedService;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public interface IComponentResolverExt {

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public interface IStaticExt {
        default boolean onIsFilterStopped(PackageStateInternal packageStateInternal, boolean z) {
            return z;
        }
    }

    default void onAddActivitiesLocked(ParsedActivity parsedActivity, AndroidPackage androidPackage) {
    }

    default void onAddProvidersLocked(ParsedProvider parsedProvider, AndroidPackage androidPackage) {
    }

    default void onAddReceiversLocked(ParsedActivity parsedActivity, AndroidPackage androidPackage) {
    }

    default void onAddServicesLocked(ParsedService parsedService, AndroidPackage androidPackage) {
    }

    default boolean shouldOverrideProviderByAuthority(String str, AndroidPackage androidPackage, ParsedProvider parsedProvider) {
        return false;
    }
}
