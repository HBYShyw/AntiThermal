package gd;

import gd.c1;
import java.util.List;
import kotlin.collections.CollectionsJVM;
import pb.DeclarationDescriptor;

/* compiled from: TypeAttributeTranslator.kt */
/* loaded from: classes2.dex */
public final class o implements b1 {

    /* renamed from: a, reason: collision with root package name */
    public static final o f11859a = new o();

    private o() {
    }

    @Override // gd.b1
    public c1 a(qb.g gVar, TypeConstructor typeConstructor, DeclarationDescriptor declarationDescriptor) {
        List<? extends a1<?>> e10;
        za.k.e(gVar, "annotations");
        if (gVar.isEmpty()) {
            return c1.f11749f.h();
        }
        c1.a aVar = c1.f11749f;
        e10 = CollectionsJVM.e(new j(gVar));
        return aVar.g(e10);
    }
}
