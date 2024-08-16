package com.android.server.devicepolicy;

import android.app.admin.IntegerPolicyValue;
import android.app.admin.PolicyValue;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Objects;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
final class FlagUnion extends ResolutionMechanism<Integer> {
    public String toString() {
        return "IntegerUnion {}";
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.android.server.devicepolicy.ResolutionMechanism
    public IntegerPolicyValue resolve(LinkedHashMap<EnforcingAdmin, PolicyValue<Integer>> linkedHashMap) {
        Objects.requireNonNull(linkedHashMap);
        if (linkedHashMap.isEmpty()) {
            return null;
        }
        Integer num = 0;
        Iterator<PolicyValue<Integer>> it = linkedHashMap.values().iterator();
        while (it.hasNext()) {
            num = Integer.valueOf(num.intValue() | ((Integer) it.next().getValue()).intValue());
        }
        return new IntegerPolicyValue(num.intValue());
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.android.server.devicepolicy.ResolutionMechanism
    /* renamed from: getParcelableResolutionMechanism, reason: avoid collision after fix types in other method and merged with bridge method [inline-methods] */
    public android.app.admin.FlagUnion mo3346getParcelableResolutionMechanism() {
        return android.app.admin.FlagUnion.FLAG_UNION;
    }
}
