package vc;

import za.k;

/* compiled from: DeprecationInfo.kt */
/* loaded from: classes2.dex */
public abstract class a implements Comparable<a> {
    @Override // java.lang.Comparable
    /* renamed from: a, reason: merged with bridge method [inline-methods] */
    public int compareTo(a aVar) {
        k.e(aVar, "other");
        int compareTo = b().compareTo(aVar.b());
        if (compareTo == 0 && !c() && aVar.c()) {
            return 1;
        }
        return compareTo;
    }

    public abstract b b();

    public abstract boolean c();
}
