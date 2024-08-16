package com.android.server.devicepolicy;

import android.app.admin.PolicyValue;
import android.app.admin.StringSetPolicyValue;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Objects;
import java.util.Set;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
final class StringSetUnion extends ResolutionMechanism<Set<String>> {
    public String toString() {
        return "SetUnion {}";
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.android.server.devicepolicy.ResolutionMechanism
    public PolicyValue<Set<String>> resolve(LinkedHashMap<EnforcingAdmin, PolicyValue<Set<String>>> linkedHashMap) {
        Objects.requireNonNull(linkedHashMap);
        if (linkedHashMap.isEmpty()) {
            return null;
        }
        HashSet hashSet = new HashSet();
        Iterator<PolicyValue<Set<String>>> it = linkedHashMap.values().iterator();
        while (it.hasNext()) {
            hashSet.addAll((Collection) it.next().getValue());
        }
        return new StringSetPolicyValue(hashSet);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.android.server.devicepolicy.ResolutionMechanism
    /* renamed from: getParcelableResolutionMechanism, reason: avoid collision after fix types in other method and merged with bridge method [inline-methods] */
    public android.app.admin.StringSetUnion mo3346getParcelableResolutionMechanism() {
        return new android.app.admin.StringSetUnion();
    }
}
