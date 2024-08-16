package zb;

import fc.m;
import gb.l;
import java.util.List;
import java.util.Map;
import kotlin.collections.CollectionsJVM;
import kotlin.collections.MapsJVM;
import kotlin.collections.m0;
import ma.u;
import mb.StandardNames;
import oc.Name;
import za.Lambda;
import za.PropertyReference1Impl;
import za.Reflection;
import za.k;

/* compiled from: JavaAnnotationMapper.kt */
/* loaded from: classes2.dex */
public final class i extends b {

    /* renamed from: h, reason: collision with root package name */
    static final /* synthetic */ l<Object>[] f20408h = {Reflection.g(new PropertyReference1Impl(Reflection.b(i.class), "allValueArguments", "getAllValueArguments()Ljava/util/Map;"))};

    /* renamed from: g, reason: collision with root package name */
    private final fd.i f20409g;

    /* compiled from: JavaAnnotationMapper.kt */
    /* loaded from: classes2.dex */
    static final class a extends Lambda implements ya.a<Map<Name, ? extends uc.g<? extends Object>>> {
        a() {
            super(0);
        }

        @Override // ya.a
        /* renamed from: a, reason: merged with bridge method [inline-methods] */
        public final Map<Name, uc.g<Object>> invoke() {
            uc.g<?> gVar;
            List<? extends fc.b> e10;
            Map<Name, uc.g<Object>> i10;
            fc.b b10 = i.this.b();
            if (b10 instanceof fc.e) {
                gVar = d.f20396a.c(((fc.e) i.this.b()).e());
            } else if (b10 instanceof m) {
                d dVar = d.f20396a;
                e10 = CollectionsJVM.e(i.this.b());
                gVar = dVar.c(e10);
            } else {
                gVar = null;
            }
            Map<Name, uc.g<Object>> f10 = gVar != null ? MapsJVM.f(u.a(c.f20391a.d(), gVar)) : null;
            if (f10 != null) {
                return f10;
            }
            i10 = m0.i();
            return i10;
        }
    }

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public i(fc.a aVar, bc.g gVar) {
        super(gVar, aVar, StandardNames.a.H);
        k.e(aVar, "annotation");
        k.e(gVar, "c");
        this.f20409g = gVar.e().g(new a());
    }

    @Override // zb.b, qb.AnnotationDescriptor
    public Map<Name, uc.g<Object>> a() {
        return (Map) fd.m.a(this.f20409g, this, f20408h[0]);
    }
}
