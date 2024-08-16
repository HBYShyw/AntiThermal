package gd;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import kotlin.collections._Collections;
import pb.TypeParameterDescriptor;
import za.DefaultConstructorMarker;

/* compiled from: TypeSubstitution.kt */
/* loaded from: classes2.dex */
public abstract class h1 extends n1 {

    /* renamed from: c, reason: collision with root package name */
    public static final a f11827c = new a(null);

    /* compiled from: TypeSubstitution.kt */
    /* loaded from: classes2.dex */
    public static final class a {

        /* compiled from: TypeSubstitution.kt */
        /* renamed from: gd.h1$a$a, reason: collision with other inner class name */
        /* loaded from: classes2.dex */
        public static final class C0042a extends h1 {

            /* renamed from: d, reason: collision with root package name */
            final /* synthetic */ Map<TypeConstructor, TypeProjection> f11828d;

            /* renamed from: e, reason: collision with root package name */
            final /* synthetic */ boolean f11829e;

            /* JADX WARN: Multi-variable type inference failed */
            C0042a(Map<TypeConstructor, ? extends TypeProjection> map, boolean z10) {
                this.f11828d = map;
                this.f11829e = z10;
            }

            @Override // gd.n1
            public boolean a() {
                return this.f11829e;
            }

            @Override // gd.n1
            public boolean f() {
                return this.f11828d.isEmpty();
            }

            @Override // gd.h1
            public TypeProjection k(TypeConstructor typeConstructor) {
                za.k.e(typeConstructor, "key");
                return this.f11828d.get(typeConstructor);
            }
        }

        private a() {
        }

        public /* synthetic */ a(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        public static /* synthetic */ h1 e(a aVar, Map map, boolean z10, int i10, Object obj) {
            if ((i10 & 2) != 0) {
                z10 = false;
            }
            return aVar.d(map, z10);
        }

        public final n1 a(g0 g0Var) {
            za.k.e(g0Var, "kotlinType");
            return b(g0Var.W0(), g0Var.U0());
        }

        public final n1 b(TypeConstructor typeConstructor, List<? extends TypeProjection> list) {
            Object g02;
            int u7;
            List G0;
            Map q10;
            za.k.e(typeConstructor, "typeConstructor");
            za.k.e(list, "arguments");
            List<TypeParameterDescriptor> parameters = typeConstructor.getParameters();
            za.k.d(parameters, "typeConstructor.parameters");
            g02 = _Collections.g0(parameters);
            TypeParameterDescriptor typeParameterDescriptor = (TypeParameterDescriptor) g02;
            if (typeParameterDescriptor != null && typeParameterDescriptor.t0()) {
                List<TypeParameterDescriptor> parameters2 = typeConstructor.getParameters();
                za.k.d(parameters2, "typeConstructor.parameters");
                u7 = kotlin.collections.s.u(parameters2, 10);
                ArrayList arrayList = new ArrayList(u7);
                Iterator<T> it = parameters2.iterator();
                while (it.hasNext()) {
                    arrayList.add(((TypeParameterDescriptor) it.next()).n());
                }
                G0 = _Collections.G0(arrayList, list);
                q10 = kotlin.collections.m0.q(G0);
                return e(this, q10, false, 2, null);
            }
            return new e0(parameters, list);
        }

        public final h1 c(Map<TypeConstructor, ? extends TypeProjection> map) {
            za.k.e(map, "map");
            return e(this, map, false, 2, null);
        }

        public final h1 d(Map<TypeConstructor, ? extends TypeProjection> map, boolean z10) {
            za.k.e(map, "map");
            return new C0042a(map, z10);
        }
    }

    public static final n1 i(TypeConstructor typeConstructor, List<? extends TypeProjection> list) {
        return f11827c.b(typeConstructor, list);
    }

    public static final h1 j(Map<TypeConstructor, ? extends TypeProjection> map) {
        return f11827c.c(map);
    }

    @Override // gd.n1
    public TypeProjection e(g0 g0Var) {
        za.k.e(g0Var, "key");
        return k(g0Var.W0());
    }

    public abstract TypeProjection k(TypeConstructor typeConstructor);
}
