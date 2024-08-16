package com.android.server.pm;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.os.Binder;
import com.android.internal.config.appcloning.AppCloningDeviceConfigHelper;
import com.android.server.pm.pkg.PackageStateInternal;
import com.android.server.pm.resolution.ComponentResolverApi;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public class NoFilteringResolver extends CrossProfileResolver {
    private static final String FLAG_ALLOW_INTENT_REDIRECTION_FOR_CLONE_PROFILE = "allow_intent_redirection_for_clone_profile";

    @Override // com.android.server.pm.CrossProfileResolver
    public List<CrossProfileDomainInfo> filterResolveInfoWithDomainPreferredActivity(Intent intent, List<CrossProfileDomainInfo> list, long j, int i, int i2, int i3) {
        return list;
    }

    public static boolean isIntentRedirectionAllowed(Context context, AppCloningDeviceConfigHelper appCloningDeviceConfigHelper, boolean z, long j) {
        return isAppCloningBuildingBlocksEnabled(context, appCloningDeviceConfigHelper) && (z || ((536870912 & j) != 0 && hasPermission(context, "android.permission.QUERY_CLONED_APPS")));
    }

    public NoFilteringResolver(ComponentResolverApi componentResolverApi, UserManagerService userManagerService) {
        super(componentResolverApi, userManagerService);
    }

    @Override // com.android.server.pm.CrossProfileResolver
    public List<CrossProfileDomainInfo> resolveIntent(Computer computer, Intent intent, String str, int i, int i2, long j, String str2, List<CrossProfileIntentFilter> list, boolean z, Function<String, PackageStateInternal> function) {
        List<ResolveInfo> queryActivities = this.mComponentResolver.queryActivities(computer, intent, str, j, i2);
        ArrayList arrayList = new ArrayList();
        if (queryActivities != null) {
            for (int i3 = 0; i3 < queryActivities.size(); i3++) {
                arrayList.add(new CrossProfileDomainInfo(queryActivities.get(i3), 0, i2));
            }
        }
        return filterIfNotSystemUser(arrayList, i);
    }

    private static boolean hasPermission(Context context, String str) {
        return context.checkCallingOrSelfPermission(str) == 0;
    }

    private static boolean isAppCloningBuildingBlocksEnabled(Context context, AppCloningDeviceConfigHelper appCloningDeviceConfigHelper) {
        boolean z;
        long clearCallingIdentity = Binder.clearCallingIdentity();
        try {
            if (context.getResources().getBoolean(17891653)) {
                if (appCloningDeviceConfigHelper.getEnableAppCloningBuildingBlocks()) {
                    z = true;
                    return z;
                }
            }
            z = false;
            return z;
        } finally {
            Binder.restoreCallingIdentity(clearCallingIdentity);
        }
    }
}
