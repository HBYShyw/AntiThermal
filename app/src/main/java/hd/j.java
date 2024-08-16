package hd;

import gd.TypeProjection;
import gd.g0;
import gd.v1;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import mb.KotlinBuiltIns;
import pb.ClassifierDescriptor;
import pb.TypeParameterDescriptor;
import za.DefaultConstructorMarker;
import za.Lambda;

/* compiled from: NewCapturedType.kt */
/* loaded from: classes2.dex */
public final class j implements tc.b {

    /* renamed from: a, reason: collision with root package name */
    private final TypeProjection f12223a;

    /* renamed from: b, reason: collision with root package name */
    private ya.a<? extends List<? extends v1>> f12224b;

    /* renamed from: c, reason: collision with root package name */
    private final j f12225c;

    /* renamed from: d, reason: collision with root package name */
    private final TypeParameterDescriptor f12226d;

    /* renamed from: e, reason: collision with root package name */
    private final ma.h f12227e;

    /* compiled from: NewCapturedType.kt */
    /* loaded from: classes2.dex */
    static final class a extends Lambda implements ya.a<List<? extends v1>> {

        /* renamed from: e, reason: collision with root package name */
        final /* synthetic */ List<v1> f12228e;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        /* JADX WARN: Multi-variable type inference failed */
        a(List<? extends v1> list) {
            super(0);
            this.f12228e = list;
        }

        @Override // ya.a
        /* renamed from: a, reason: merged with bridge method [inline-methods] */
        public final List<v1> invoke() {
            return this.f12228e;
        }
    }

    /* compiled from: NewCapturedType.kt */
    /* loaded from: classes2.dex */
    static final class b extends Lambda implements ya.a<List<? extends v1>> {
        b() {
            super(0);
        }

        @Override // ya.a
        /* renamed from: a, reason: merged with bridge method [inline-methods] */
        public final List<v1> invoke() {
            ya.a aVar = j.this.f12224b;
            if (aVar != null) {
                return (List) aVar.invoke();
            }
            return null;
        }
    }

    /* compiled from: NewCapturedType.kt */
    /* loaded from: classes2.dex */
    static final class c extends Lambda implements ya.a<List<? extends v1>> {

        /* renamed from: e, reason: collision with root package name */
        final /* synthetic */ List<v1> f12230e;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        /* JADX WARN: Multi-variable type inference failed */
        c(List<? extends v1> list) {
            super(0);
            this.f12230e = list;
        }

        @Override // ya.a
        /* renamed from: a, reason: merged with bridge method [inline-methods] */
        public final List<v1> invoke() {
            return this.f12230e;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: NewCapturedType.kt */
    /* loaded from: classes2.dex */
    public static final class d extends Lambda implements ya.a<List<? extends v1>> {

        /* renamed from: f, reason: collision with root package name */
        final /* synthetic */ g f12232f;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        d(g gVar) {
            super(0);
            this.f12232f = gVar;
        }

        @Override // ya.a
        /* renamed from: a, reason: merged with bridge method [inline-methods] */
        public final List<v1> invoke() {
            int u7;
            List<v1> q10 = j.this.q();
            g gVar = this.f12232f;
            u7 = kotlin.collections.s.u(q10, 10);
            ArrayList arrayList = new ArrayList(u7);
            Iterator<T> it = q10.iterator();
            while (it.hasNext()) {
                arrayList.add(((v1) it.next()).g1(gVar));
            }
            return arrayList;
        }
    }

    public j(TypeProjection typeProjection, ya.a<? extends List<? extends v1>> aVar, j jVar, TypeParameterDescriptor typeParameterDescriptor) {
        ma.h a10;
        za.k.e(typeProjection, "projection");
        this.f12223a = typeProjection;
        this.f12224b = aVar;
        this.f12225c = jVar;
        this.f12226d = typeParameterDescriptor;
        a10 = ma.j.a(ma.l.PUBLICATION, new b());
        this.f12227e = a10;
    }

    private final List<v1> e() {
        return (List) this.f12227e.getValue();
    }

    @Override // tc.b
    public TypeProjection b() {
        return this.f12223a;
    }

    @Override // gd.TypeConstructor
    /* renamed from: d, reason: merged with bridge method [inline-methods] */
    public List<v1> q() {
        List<v1> j10;
        List<v1> e10 = e();
        if (e10 != null) {
            return e10;
        }
        j10 = kotlin.collections.r.j();
        return j10;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!za.k.a(j.class, obj != null ? obj.getClass() : null)) {
            return false;
        }
        za.k.c(obj, "null cannot be cast to non-null type org.jetbrains.kotlin.types.checker.NewCapturedTypeConstructor");
        j jVar = (j) obj;
        j jVar2 = this.f12225c;
        if (jVar2 != null) {
            this = jVar2;
        }
        j jVar3 = jVar.f12225c;
        if (jVar3 != null) {
            jVar = jVar3;
        }
        return this == jVar;
    }

    public final void f(List<? extends v1> list) {
        za.k.e(list, "supertypes");
        this.f12224b = new c(list);
    }

    @Override // gd.TypeConstructor
    /* renamed from: g, reason: merged with bridge method [inline-methods] */
    public j u(g gVar) {
        za.k.e(gVar, "kotlinTypeRefiner");
        TypeProjection u7 = b().u(gVar);
        za.k.d(u7, "projection.refine(kotlinTypeRefiner)");
        d dVar = this.f12224b != null ? new d(gVar) : null;
        j jVar = this.f12225c;
        if (jVar == null) {
            jVar = this;
        }
        return new j(u7, dVar, jVar, this.f12226d);
    }

    @Override // gd.TypeConstructor
    public List<TypeParameterDescriptor> getParameters() {
        List<TypeParameterDescriptor> j10;
        j10 = kotlin.collections.r.j();
        return j10;
    }

    public int hashCode() {
        j jVar = this.f12225c;
        return jVar != null ? jVar.hashCode() : super.hashCode();
    }

    @Override // gd.TypeConstructor
    public KotlinBuiltIns t() {
        g0 type = b().getType();
        za.k.d(type, "projection.type");
        return ld.a.i(type);
    }

    public String toString() {
        return "CapturedType(" + b() + ')';
    }

    @Override // gd.TypeConstructor
    public ClassifierDescriptor v() {
        return null;
    }

    @Override // gd.TypeConstructor
    public boolean w() {
        return false;
    }

    public /* synthetic */ j(TypeProjection typeProjection, ya.a aVar, j jVar, TypeParameterDescriptor typeParameterDescriptor, int i10, DefaultConstructorMarker defaultConstructorMarker) {
        this(typeProjection, (i10 & 2) != 0 ? null : aVar, (i10 & 4) != 0 ? null : jVar, (i10 & 8) != 0 ? null : typeParameterDescriptor);
    }

    public /* synthetic */ j(TypeProjection typeProjection, List list, j jVar, int i10, DefaultConstructorMarker defaultConstructorMarker) {
        this(typeProjection, list, (i10 & 4) != 0 ? null : jVar);
    }

    /* JADX WARN: 'this' call moved to the top of the method (can break code semantics) */
    public j(TypeProjection typeProjection, List<? extends v1> list, j jVar) {
        this(typeProjection, new a(list), jVar, null, 8, null);
        za.k.e(typeProjection, "projection");
        za.k.e(list, "supertypes");
    }
}
