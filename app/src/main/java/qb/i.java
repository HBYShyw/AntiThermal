package qb;

/* compiled from: Annotations.kt */
/* loaded from: classes2.dex */
public final class i {
    public static final g a(g gVar, g gVar2) {
        za.k.e(gVar, "first");
        za.k.e(gVar2, "second");
        return gVar.isEmpty() ? gVar2 : gVar2.isEmpty() ? gVar : new k(gVar, gVar2);
    }
}
