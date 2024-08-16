package com.android.server.devicepolicy;

import android.app.admin.PolicyValue;
import java.util.LinkedHashMap;
import java.util.List;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
final class MostRestrictive<V> extends ResolutionMechanism<V> {
    private List<PolicyValue<V>> mMostToLeastRestrictive;

    /* JADX INFO: Access modifiers changed from: package-private */
    public MostRestrictive(List<PolicyValue<V>> list) {
        this.mMostToLeastRestrictive = list;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.android.server.devicepolicy.ResolutionMechanism
    public PolicyValue<V> resolve(LinkedHashMap<EnforcingAdmin, PolicyValue<V>> linkedHashMap) {
        if (linkedHashMap.isEmpty()) {
            return null;
        }
        for (PolicyValue<V> policyValue : this.mMostToLeastRestrictive) {
            if (linkedHashMap.containsValue(policyValue)) {
                return policyValue;
            }
        }
        return linkedHashMap.entrySet().stream().findFirst().get().getValue();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.android.server.devicepolicy.ResolutionMechanism
    /* renamed from: getParcelableResolutionMechanism, reason: merged with bridge method [inline-methods] */
    public android.app.admin.MostRestrictive<V> mo3346getParcelableResolutionMechanism() {
        return new android.app.admin.MostRestrictive<>(this.mMostToLeastRestrictive);
    }

    public String toString() {
        return "MostRestrictive { mMostToLeastRestrictive= " + this.mMostToLeastRestrictive + " }";
    }
}
