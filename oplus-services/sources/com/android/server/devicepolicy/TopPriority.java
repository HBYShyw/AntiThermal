package com.android.server.devicepolicy;

import android.app.admin.Authority;
import android.app.admin.PolicyValue;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Predicate;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
final class TopPriority<V> extends ResolutionMechanism<V> {
    private final List<String> mHighestToLowestPriorityAuthorities;

    /* JADX INFO: Access modifiers changed from: package-private */
    public TopPriority(List<String> list) {
        Objects.requireNonNull(list);
        this.mHighestToLowestPriorityAuthorities = list;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.android.server.devicepolicy.ResolutionMechanism
    public PolicyValue<V> resolve(LinkedHashMap<EnforcingAdmin, PolicyValue<V>> linkedHashMap) {
        if (linkedHashMap.isEmpty()) {
            return null;
        }
        for (final String str : this.mHighestToLowestPriorityAuthorities) {
            Optional<EnforcingAdmin> findFirst = linkedHashMap.keySet().stream().filter(new Predicate() { // from class: com.android.server.devicepolicy.TopPriority$$ExternalSyntheticLambda0
                @Override // java.util.function.Predicate
                public final boolean test(Object obj) {
                    boolean lambda$resolve$0;
                    lambda$resolve$0 = TopPriority.lambda$resolve$0(str, (EnforcingAdmin) obj);
                    return lambda$resolve$0;
                }
            }).findFirst();
            if (findFirst.isPresent()) {
                return linkedHashMap.get(findFirst.get());
            }
        }
        return linkedHashMap.entrySet().stream().findFirst().get().getValue();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ boolean lambda$resolve$0(String str, EnforcingAdmin enforcingAdmin) {
        return enforcingAdmin.hasAuthority(str);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.android.server.devicepolicy.ResolutionMechanism
    /* renamed from: getParcelableResolutionMechanism, reason: merged with bridge method [inline-methods] */
    public android.app.admin.TopPriority<V> mo3346getParcelableResolutionMechanism() {
        return new android.app.admin.TopPriority<>(getParcelableAuthorities());
    }

    private List<Authority> getParcelableAuthorities() {
        ArrayList arrayList = new ArrayList();
        Iterator<String> it = this.mHighestToLowestPriorityAuthorities.iterator();
        while (it.hasNext()) {
            arrayList.add(EnforcingAdmin.getParcelableAuthority(it.next()));
        }
        return arrayList;
    }

    public String toString() {
        return "TopPriority { mHighestToLowestPriorityAuthorities= " + this.mHighestToLowestPriorityAuthorities + " }";
    }
}
