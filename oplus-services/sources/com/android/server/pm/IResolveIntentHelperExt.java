package com.android.server.pm;

import android.content.Intent;
import android.content.pm.ResolveInfo;
import java.util.List;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public interface IResolveIntentHelperExt {
    default ResolveInfo adjustQueryAndResultForUsePrefInChooseBestActivity(Computer computer, Intent intent, List<ResolveInfo> list, ResolveInfo resolveInfo) {
        return null;
    }

    default int changeUserIdInChooseBestActivity(int i, ResolveInfo resolveInfo) {
        return i;
    }

    default ResolveInfo findPriorBeforeUsePreferenceInChooseBestActivity(Intent intent, List<ResolveInfo> list) {
        return null;
    }

    default boolean hasOplusPackageName(String str) {
        return false;
    }

    default ResolveInfo interceptAppDetailsToMarket(Intent intent, List<ResolveInfo> list, Computer computer, int i) {
        return null;
    }

    default boolean interceptHandler(Intent intent) {
        return false;
    }

    default boolean interceptHttpAppDetails(Intent intent) {
        return false;
    }
}
