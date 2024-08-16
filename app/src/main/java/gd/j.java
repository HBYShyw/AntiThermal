package gd;

import gb.KClass;
import za.Reflection;

/* compiled from: AnnotationsTypeAttribute.kt */
/* loaded from: classes2.dex */
public final class j extends a1<j> {

    /* renamed from: a, reason: collision with root package name */
    private final qb.g f11832a;

    public j(qb.g gVar) {
        za.k.e(gVar, "annotations");
        this.f11832a = gVar;
    }

    @Override // gd.a1
    public KClass<? extends j> b() {
        return Reflection.b(j.class);
    }

    @Override // gd.a1
    /* renamed from: d, reason: merged with bridge method [inline-methods] */
    public j a(j jVar) {
        return jVar == null ? this : new j(qb.i.a(this.f11832a, jVar.f11832a));
    }

    public final qb.g e() {
        return this.f11832a;
    }

    public boolean equals(Object obj) {
        if (obj instanceof j) {
            return za.k.a(((j) obj).f11832a, this.f11832a);
        }
        return false;
    }

    @Override // gd.a1
    /* renamed from: f, reason: merged with bridge method [inline-methods] */
    public j c(j jVar) {
        if (za.k.a(jVar, this)) {
            return this;
        }
        return null;
    }

    public int hashCode() {
        return this.f11832a.hashCode();
    }
}
