package com.android.server.devicepolicy;

import android.app.admin.PolicyValue;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
final class MostRecent<V> extends ResolutionMechanism<V> {
    public String toString() {
        return "MostRecent {}";
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.android.server.devicepolicy.ResolutionMechanism
    public PolicyValue<V> resolve(LinkedHashMap<EnforcingAdmin, PolicyValue<V>> linkedHashMap) {
        ArrayList arrayList = new ArrayList(linkedHashMap.entrySet());
        if (arrayList.isEmpty()) {
            return null;
        }
        return (PolicyValue) ((Map.Entry) arrayList.get(arrayList.size() - 1)).getValue();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.android.server.devicepolicy.ResolutionMechanism
    /* renamed from: getParcelableResolutionMechanism, reason: merged with bridge method [inline-methods] */
    public android.app.admin.MostRecent<V> mo3346getParcelableResolutionMechanism() {
        return new android.app.admin.MostRecent<>();
    }
}
