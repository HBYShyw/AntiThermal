package dc;

import gd.ErasureProjectionComputer;
import gd.TypeConstructor;
import gd.TypeParameterUpperBoundEraser;
import gd.TypeProjection;
import gd.TypeProjectionImpl;
import gd.TypeUsage;
import gd.Variance;
import gd.c1;
import gd.d0;
import gd.g0;
import gd.h0;
import gd.i0;
import gd.n1;
import gd.o0;
import id.ErrorTypeKind;
import id.ErrorUtils;
import java.util.ArrayList;
import java.util.List;
import kotlin.collections.CollectionsJVM;
import kotlin.collections.s;
import ma.o;
import ma.u;
import mb.KotlinBuiltIns;
import oc.ClassId;
import pb.ClassDescriptor;
import pb.ClassifierDescriptor;
import pb.TypeParameterDescriptor;
import ya.l;
import za.DefaultConstructorMarker;
import za.Lambda;
import za.k;

/* compiled from: RawSubstitution.kt */
/* renamed from: dc.g, reason: use source file name */
/* loaded from: classes2.dex */
public final class RawSubstitution extends n1 {

    /* renamed from: e, reason: collision with root package name */
    public static final a f10916e = new a(null);

    /* renamed from: f, reason: collision with root package name */
    private static final dc.a f10917f;

    /* renamed from: g, reason: collision with root package name */
    private static final dc.a f10918g;

    /* renamed from: c, reason: collision with root package name */
    private final RawProjectionComputer f10919c;

    /* renamed from: d, reason: collision with root package name */
    private final TypeParameterUpperBoundEraser f10920d;

    /* compiled from: RawSubstitution.kt */
    /* renamed from: dc.g$a */
    /* loaded from: classes2.dex */
    public static final class a {
        private a() {
        }

        public /* synthetic */ a(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: RawSubstitution.kt */
    /* renamed from: dc.g$b */
    /* loaded from: classes2.dex */
    public static final class b extends Lambda implements l<hd.g, o0> {

        /* renamed from: e, reason: collision with root package name */
        final /* synthetic */ ClassDescriptor f10921e;

        /* renamed from: f, reason: collision with root package name */
        final /* synthetic */ RawSubstitution f10922f;

        /* renamed from: g, reason: collision with root package name */
        final /* synthetic */ o0 f10923g;

        /* renamed from: h, reason: collision with root package name */
        final /* synthetic */ dc.a f10924h;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        b(ClassDescriptor classDescriptor, RawSubstitution rawSubstitution, o0 o0Var, dc.a aVar) {
            super(1);
            this.f10921e = classDescriptor;
            this.f10922f = rawSubstitution;
            this.f10923g = o0Var;
            this.f10924h = aVar;
        }

        @Override // ya.l
        /* renamed from: a, reason: merged with bridge method [inline-methods] */
        public final o0 invoke(hd.g gVar) {
            ClassId k10;
            ClassDescriptor b10;
            k.e(gVar, "kotlinTypeRefiner");
            ClassDescriptor classDescriptor = this.f10921e;
            if (!(classDescriptor instanceof ClassDescriptor)) {
                classDescriptor = null;
            }
            if (classDescriptor == null || (k10 = wc.c.k(classDescriptor)) == null || (b10 = gVar.b(k10)) == null || k.a(b10, this.f10921e)) {
                return null;
            }
            return (o0) this.f10922f.j(this.f10923g, b10, this.f10924h).c();
        }
    }

    static {
        TypeUsage typeUsage = TypeUsage.COMMON;
        f10917f = dc.b.b(typeUsage, false, true, null, 5, null).l(JavaTypeFlexibility.FLEXIBLE_LOWER_BOUND);
        f10918g = dc.b.b(typeUsage, false, true, null, 5, null).l(JavaTypeFlexibility.FLEXIBLE_UPPER_BOUND);
    }

    public RawSubstitution(TypeParameterUpperBoundEraser typeParameterUpperBoundEraser) {
        RawProjectionComputer rawProjectionComputer = new RawProjectionComputer();
        this.f10919c = rawProjectionComputer;
        this.f10920d = typeParameterUpperBoundEraser == null ? new TypeParameterUpperBoundEraser(rawProjectionComputer, null, 2, null) : typeParameterUpperBoundEraser;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final o<o0, Boolean> j(o0 o0Var, ClassDescriptor classDescriptor, dc.a aVar) {
        int u7;
        List e10;
        if (o0Var.W0().getParameters().isEmpty()) {
            return u.a(o0Var, Boolean.FALSE);
        }
        if (KotlinBuiltIns.c0(o0Var)) {
            TypeProjection typeProjection = o0Var.U0().get(0);
            Variance a10 = typeProjection.a();
            g0 type = typeProjection.getType();
            k.d(type, "componentTypeProjection.type");
            e10 = CollectionsJVM.e(new TypeProjectionImpl(a10, k(type, aVar)));
            return u.a(h0.j(o0Var.V0(), o0Var.W0(), e10, o0Var.X0(), null, 16, null), Boolean.FALSE);
        }
        if (i0.a(o0Var)) {
            return u.a(ErrorUtils.d(ErrorTypeKind.O, o0Var.W0().toString()), Boolean.FALSE);
        }
        zc.h I0 = classDescriptor.I0(this);
        k.d(I0, "declaration.getMemberScope(this)");
        c1 V0 = o0Var.V0();
        TypeConstructor n10 = classDescriptor.n();
        k.d(n10, "declaration.typeConstructor");
        List<TypeParameterDescriptor> parameters = classDescriptor.n().getParameters();
        k.d(parameters, "declaration.typeConstructor.parameters");
        u7 = s.u(parameters, 10);
        ArrayList arrayList = new ArrayList(u7);
        for (TypeParameterDescriptor typeParameterDescriptor : parameters) {
            RawProjectionComputer rawProjectionComputer = this.f10919c;
            k.d(typeParameterDescriptor, "parameter");
            arrayList.add(ErasureProjectionComputer.b(rawProjectionComputer, typeParameterDescriptor, aVar, this.f10920d, null, 8, null));
        }
        return u.a(h0.l(V0, n10, arrayList, o0Var.X0(), I0, new b(classDescriptor, this, o0Var, aVar)), Boolean.TRUE);
    }

    private final g0 k(g0 g0Var, dc.a aVar) {
        ClassifierDescriptor v7 = g0Var.W0().v();
        if (v7 instanceof TypeParameterDescriptor) {
            return k(this.f10920d.c((TypeParameterDescriptor) v7, aVar.j(true)), aVar);
        }
        if (v7 instanceof ClassDescriptor) {
            ClassifierDescriptor v10 = d0.d(g0Var).W0().v();
            if (v10 instanceof ClassDescriptor) {
                o<o0, Boolean> j10 = j(d0.c(g0Var), (ClassDescriptor) v7, f10917f);
                o0 a10 = j10.a();
                boolean booleanValue = j10.b().booleanValue();
                o<o0, Boolean> j11 = j(d0.d(g0Var), (ClassDescriptor) v10, f10918g);
                o0 a11 = j11.a();
                boolean booleanValue2 = j11.b().booleanValue();
                if (!booleanValue && !booleanValue2) {
                    return h0.d(a10, a11);
                }
                return new h(a10, a11);
            }
            throw new IllegalStateException(("For some reason declaration for upper bound is not a class but \"" + v10 + "\" while for lower it's \"" + v7 + '\"').toString());
        }
        throw new IllegalStateException(("Unexpected declaration kind: " + v7).toString());
    }

    static /* synthetic */ g0 l(RawSubstitution rawSubstitution, g0 g0Var, dc.a aVar, int i10, Object obj) {
        if ((i10 & 2) != 0) {
            aVar = new dc.a(TypeUsage.COMMON, null, false, false, null, null, 62, null);
        }
        return rawSubstitution.k(g0Var, aVar);
    }

    @Override // gd.n1
    public boolean f() {
        return false;
    }

    @Override // gd.n1
    /* renamed from: m, reason: merged with bridge method [inline-methods] */
    public TypeProjectionImpl e(g0 g0Var) {
        k.e(g0Var, "key");
        return new TypeProjectionImpl(l(this, g0Var, null, 2, null));
    }

    public /* synthetic */ RawSubstitution(TypeParameterUpperBoundEraser typeParameterUpperBoundEraser, int i10, DefaultConstructorMarker defaultConstructorMarker) {
        this((i10 & 1) != 0 ? null : typeParameterUpperBoundEraser);
    }
}
