package tc;

import fd.LockBasedStorageManager;
import fd.StorageManager;
import gd.TypeProjection;
import gd.TypeProjectionImpl;
import gd.Variance;
import gd.e0;
import gd.g0;
import gd.j0;
import gd.n1;
import gd.q;
import java.util.ArrayList;
import java.util.List;
import kotlin.collections._Arrays;
import kotlin.collections.s;
import ma.o;
import pb.ClassifierDescriptor;
import pb.TypeParameterDescriptor;
import za.Lambda;
import za.k;

/* compiled from: CapturedTypeConstructor.kt */
/* loaded from: classes2.dex */
public final class d {

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: CapturedTypeConstructor.kt */
    /* loaded from: classes2.dex */
    public static final class a extends Lambda implements ya.a<g0> {

        /* renamed from: e, reason: collision with root package name */
        final /* synthetic */ TypeProjection f18713e;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        a(TypeProjection typeProjection) {
            super(0);
            this.f18713e = typeProjection;
        }

        @Override // ya.a
        /* renamed from: a, reason: merged with bridge method [inline-methods] */
        public final g0 invoke() {
            g0 type = this.f18713e.getType();
            k.d(type, "this@createCapturedIfNeeded.type");
            return type;
        }
    }

    /* compiled from: CapturedTypeConstructor.kt */
    /* loaded from: classes2.dex */
    public static final class b extends q {

        /* renamed from: d, reason: collision with root package name */
        final /* synthetic */ boolean f18714d;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        b(n1 n1Var, boolean z10) {
            super(n1Var);
            this.f18714d = z10;
        }

        @Override // gd.n1
        public boolean b() {
            return this.f18714d;
        }

        @Override // gd.q, gd.n1
        public TypeProjection e(g0 g0Var) {
            k.e(g0Var, "key");
            TypeProjection e10 = super.e(g0Var);
            if (e10 == null) {
                return null;
            }
            ClassifierDescriptor v7 = g0Var.W0().v();
            return d.b(e10, v7 instanceof TypeParameterDescriptor ? (TypeParameterDescriptor) v7 : null);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final TypeProjection b(TypeProjection typeProjection, TypeParameterDescriptor typeParameterDescriptor) {
        if (typeParameterDescriptor == null || typeProjection.a() == Variance.INVARIANT) {
            return typeProjection;
        }
        if (typeParameterDescriptor.s() == typeProjection.a()) {
            if (typeProjection.b()) {
                StorageManager storageManager = LockBasedStorageManager.f11424e;
                k.d(storageManager, "NO_LOCKS");
                return new TypeProjectionImpl(new j0(storageManager, new a(typeProjection)));
            }
            return new TypeProjectionImpl(typeProjection.getType());
        }
        return new TypeProjectionImpl(c(typeProjection));
    }

    public static final g0 c(TypeProjection typeProjection) {
        k.e(typeProjection, "typeProjection");
        return new tc.a(typeProjection, null, false, null, 14, null);
    }

    public static final boolean d(g0 g0Var) {
        k.e(g0Var, "<this>");
        return g0Var.W0() instanceof tc.b;
    }

    public static final n1 e(n1 n1Var, boolean z10) {
        List<o> t02;
        int u7;
        k.e(n1Var, "<this>");
        if (n1Var instanceof e0) {
            e0 e0Var = (e0) n1Var;
            TypeParameterDescriptor[] j10 = e0Var.j();
            t02 = _Arrays.t0(e0Var.i(), e0Var.j());
            u7 = s.u(t02, 10);
            ArrayList arrayList = new ArrayList(u7);
            for (o oVar : t02) {
                arrayList.add(b((TypeProjection) oVar.c(), (TypeParameterDescriptor) oVar.d()));
            }
            return new e0(j10, (TypeProjection[]) arrayList.toArray(new TypeProjection[0]), z10);
        }
        return new b(n1Var, z10);
    }

    public static /* synthetic */ n1 f(n1 n1Var, boolean z10, int i10, Object obj) {
        if ((i10 & 1) != 0) {
            z10 = true;
        }
        return e(n1Var, z10);
    }
}
