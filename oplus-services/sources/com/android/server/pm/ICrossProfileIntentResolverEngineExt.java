package com.android.server.pm;

import android.content.Intent;
import android.content.pm.ResolveInfo;
import java.util.List;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public interface ICrossProfileIntentResolverEngineExt {
    default void checkIfSkipCrossProfile(int i, int i2, List<CrossProfileDomainInfo> list) {
    }

    default void filterDuplicateCandidatesByMultiAppFlag(List<CrossProfileDomainInfo> list, List<ResolveInfo> list2, Intent intent) {
    }
}
