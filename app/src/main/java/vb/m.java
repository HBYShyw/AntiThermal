package vb;

import oc.Name;

/* compiled from: ReflectJavaAnnotationArguments.kt */
/* loaded from: classes2.dex */
public final class m extends f implements fc.h {

    /* renamed from: c, reason: collision with root package name */
    private final Class<?> f19249c;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public m(Name name, Class<?> cls) {
        super(name, null);
        za.k.e(cls, "klass");
        this.f19249c = cls;
    }

    @Override // fc.h
    public fc.x c() {
        return ReflectJavaType.f19262a.a(this.f19249c);
    }
}
