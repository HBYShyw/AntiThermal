package com.android.server.backup;

import java.util.HashSet;
import java.util.Set;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public final class SetUtils {
    private SetUtils() {
    }

    public static <T> Set<T> union(Set<T> set, Set<T> set2) {
        HashSet hashSet = new HashSet(set);
        hashSet.addAll(set2);
        return hashSet;
    }
}
