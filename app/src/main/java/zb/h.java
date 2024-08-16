package zb;

import fd.m;
import gb.l;
import java.util.Map;
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
public final class h extends b {

    /* renamed from: h, reason: collision with root package name */
    static final /* synthetic */ l<Object>[] f20405h = {Reflection.g(new PropertyReference1Impl(Reflection.b(h.class), "allValueArguments", "getAllValueArguments()Ljava/util/Map;"))};

    /* renamed from: g, reason: collision with root package name */
    private final fd.i f20406g;

    /* compiled from: JavaAnnotationMapper.kt */
    /* loaded from: classes2.dex */
    static final class a extends Lambda implements ya.a<Map<Name, ? extends uc.g<?>>> {
        a() {
            super(0);
        }

        @Override // ya.a
        /* renamed from: a, reason: merged with bridge method [inline-methods] */
        public final Map<Name, uc.g<?>> invoke() {
            Map<Name, uc.g<?>> i10;
            uc.g<?> a10 = d.f20396a.a(h.this.b());
            Map<Name, uc.g<?>> f10 = a10 != null ? MapsJVM.f(u.a(c.f20391a.c(), a10)) : null;
            if (f10 != null) {
                return f10;
            }
            i10 = m0.i();
            return i10;
        }
    }

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public h(fc.a aVar, bc.g gVar) {
        super(gVar, aVar, StandardNames.a.L);
        k.e(aVar, "annotation");
        k.e(gVar, "c");
        this.f20406g = gVar.e().g(new a());
    }

    @Override // zb.b, qb.AnnotationDescriptor
    public Map<Name, uc.g<?>> a() {
        return (Map) m.a(this.f20406g, this, f20405h[0]);
    }
}
