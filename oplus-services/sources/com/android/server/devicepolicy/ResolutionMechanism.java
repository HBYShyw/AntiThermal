package com.android.server.devicepolicy;

import android.app.admin.PolicyValue;
import java.util.LinkedHashMap;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
abstract class ResolutionMechanism<V> {
    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: getParcelableResolutionMechanism */
    public abstract android.app.admin.ResolutionMechanism<V> mo3346getParcelableResolutionMechanism();

    /* JADX INFO: Access modifiers changed from: package-private */
    public abstract PolicyValue<V> resolve(LinkedHashMap<EnforcingAdmin, PolicyValue<V>> linkedHashMap);
}
