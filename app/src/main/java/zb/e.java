package zb;

import fd.m;
import gb.l;
import java.util.Map;
import kotlin.collections.MapsJVM;
import ma.u;
import mb.StandardNames;
import oc.Name;
import uc.v;
import za.Lambda;
import za.PropertyReference1Impl;
import za.Reflection;
import za.k;

/* compiled from: JavaAnnotationMapper.kt */
/* loaded from: classes2.dex */
public final class e extends b {

    /* renamed from: h, reason: collision with root package name */
    static final /* synthetic */ l<Object>[] f20400h = {Reflection.g(new PropertyReference1Impl(Reflection.b(e.class), "allValueArguments", "getAllValueArguments()Ljava/util/Map;"))};

    /* renamed from: g, reason: collision with root package name */
    private final fd.i f20401g;

    /* compiled from: JavaAnnotationMapper.kt */
    /* loaded from: classes2.dex */
    static final class a extends Lambda implements ya.a<Map<Name, ? extends v>> {

        /* renamed from: e, reason: collision with root package name */
        public static final a f20402e = new a();

        a() {
            super(0);
        }

        @Override // ya.a
        /* renamed from: a, reason: merged with bridge method [inline-methods] */
        public final Map<Name, v> invoke() {
            Map<Name, v> f10;
            f10 = MapsJVM.f(u.a(c.f20391a.b(), new v("Deprecated in Java")));
            return f10;
        }
    }

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public e(fc.a aVar, bc.g gVar) {
        super(gVar, aVar, StandardNames.a.f15337y);
        k.e(gVar, "c");
        this.f20401g = gVar.e().g(a.f20402e);
    }

    @Override // zb.b, qb.AnnotationDescriptor
    public Map<Name, uc.g<?>> a() {
        return (Map) m.a(this.f20401g, this, f20400h[0]);
    }
}
