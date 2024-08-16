package cd;

import ed.DeserializedTypeParameterDescriptor;
import gb.KDeclarationContainer;
import gd.TypeConstructor;
import gd.TypeProjection;
import gd.TypeProjectionImpl;
import gd.Variance;
import gd.a1;
import gd.b1;
import gd.c1;
import gd.g0;
import gd.h0;
import gd.i0;
import gd.o0;
import gd.s0;
import gd.t0;
import gd.u0;
import id.ErrorTypeKind;
import id.ErrorUtils;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import jc.q;
import kotlin.collections._Collections;
import kotlin.collections.m0;
import lc.Flags;
import lc.protoTypeTableUtil;
import mb.KotlinBuiltIns;
import mb.StandardNames;
import mb.functionTypes;
import oc.ClassId;
import oc.FqName;
import pb.CallableDescriptor;
import pb.ClassDescriptor;
import pb.ClassifierDescriptor;
import pb.DeclarationDescriptor;
import pb.TypeAliasDescriptor;
import pb.TypeParameterDescriptor;
import pb.findClassInModule;
import qb.AnnotationDescriptor;
import qb.g;
import rd.Sequence;
import rd._Sequences;
import za.FunctionReference;
import za.Lambda;
import za.Reflection;

/* compiled from: TypeDeserializer.kt */
/* loaded from: classes2.dex */
public final class d0 {

    /* renamed from: a */
    private final m f5210a;

    /* renamed from: b */
    private final d0 f5211b;

    /* renamed from: c */
    private final String f5212c;

    /* renamed from: d */
    private final String f5213d;

    /* renamed from: e */
    private final ya.l<Integer, ClassifierDescriptor> f5214e;

    /* renamed from: f */
    private final ya.l<Integer, ClassifierDescriptor> f5215f;

    /* renamed from: g */
    private final Map<Integer, TypeParameterDescriptor> f5216g;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: TypeDeserializer.kt */
    /* loaded from: classes2.dex */
    public static final class a extends Lambda implements ya.l<Integer, ClassifierDescriptor> {
        a() {
            super(1);
        }

        public final ClassifierDescriptor a(int i10) {
            return d0.this.d(i10);
        }

        @Override // ya.l
        public /* bridge */ /* synthetic */ ClassifierDescriptor invoke(Integer num) {
            return a(num.intValue());
        }
    }

    /* compiled from: TypeDeserializer.kt */
    /* loaded from: classes2.dex */
    public static final class b extends Lambda implements ya.a<List<? extends AnnotationDescriptor>> {

        /* renamed from: f */
        final /* synthetic */ jc.q f5219f;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        b(jc.q qVar) {
            super(0);
            this.f5219f = qVar;
        }

        @Override // ya.a
        /* renamed from: a */
        public final List<AnnotationDescriptor> invoke() {
            return d0.this.f5210a.c().d().d(this.f5219f, d0.this.f5210a.g());
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: TypeDeserializer.kt */
    /* loaded from: classes2.dex */
    public static final class c extends Lambda implements ya.l<Integer, ClassifierDescriptor> {
        c() {
            super(1);
        }

        public final ClassifierDescriptor a(int i10) {
            return d0.this.f(i10);
        }

        @Override // ya.l
        public /* bridge */ /* synthetic */ ClassifierDescriptor invoke(Integer num) {
            return a(num.intValue());
        }
    }

    /* compiled from: TypeDeserializer.kt */
    /* loaded from: classes2.dex */
    public /* synthetic */ class d extends FunctionReference implements ya.l<ClassId, ClassId> {

        /* renamed from: n */
        public static final d f5221n = new d();

        d() {
            super(1);
        }

        @Override // za.CallableReference
        public final KDeclarationContainer C() {
            return Reflection.b(ClassId.class);
        }

        @Override // za.CallableReference
        public final String E() {
            return "getOuterClassId()Lorg/jetbrains/kotlin/name/ClassId;";
        }

        @Override // ya.l
        /* renamed from: G */
        public final ClassId invoke(ClassId classId) {
            za.k.e(classId, "p0");
            return classId.g();
        }

        @Override // za.CallableReference, gb.KCallable
        public final String getName() {
            return "getOuterClassId";
        }
    }

    /* compiled from: TypeDeserializer.kt */
    /* loaded from: classes2.dex */
    public static final class e extends Lambda implements ya.l<jc.q, jc.q> {
        e() {
            super(1);
        }

        @Override // ya.l
        /* renamed from: a */
        public final jc.q invoke(jc.q qVar) {
            za.k.e(qVar, "it");
            return protoTypeTableUtil.j(qVar, d0.this.f5210a.j());
        }
    }

    /* compiled from: TypeDeserializer.kt */
    /* loaded from: classes2.dex */
    public static final class f extends Lambda implements ya.l<jc.q, Integer> {

        /* renamed from: e */
        public static final f f5223e = new f();

        f() {
            super(1);
        }

        @Override // ya.l
        /* renamed from: a */
        public final Integer invoke(jc.q qVar) {
            za.k.e(qVar, "it");
            return Integer.valueOf(qVar.P());
        }
    }

    public d0(m mVar, d0 d0Var, List<jc.s> list, String str, String str2) {
        Map<Integer, TypeParameterDescriptor> linkedHashMap;
        za.k.e(mVar, "c");
        za.k.e(list, "typeParameterProtos");
        za.k.e(str, "debugName");
        za.k.e(str2, "containerPresentableName");
        this.f5210a = mVar;
        this.f5211b = d0Var;
        this.f5212c = str;
        this.f5213d = str2;
        this.f5214e = mVar.h().b(new a());
        this.f5215f = mVar.h().b(new c());
        if (list.isEmpty()) {
            linkedHashMap = m0.i();
        } else {
            linkedHashMap = new LinkedHashMap<>();
            int i10 = 0;
            for (jc.s sVar : list) {
                linkedHashMap.put(Integer.valueOf(sVar.H()), new DeserializedTypeParameterDescriptor(this.f5210a, sVar, i10));
                i10++;
            }
        }
        this.f5216g = linkedHashMap;
    }

    public final ClassifierDescriptor d(int i10) {
        ClassId a10 = NameResolverUtil.a(this.f5210a.g(), i10);
        if (a10.k()) {
            return this.f5210a.c().b(a10);
        }
        return findClassInModule.b(this.f5210a.c().p(), a10);
    }

    private final o0 e(int i10) {
        if (NameResolverUtil.a(this.f5210a.g(), i10).k()) {
            return this.f5210a.c().n().a();
        }
        return null;
    }

    public final ClassifierDescriptor f(int i10) {
        ClassId a10 = NameResolverUtil.a(this.f5210a.g(), i10);
        if (a10.k()) {
            return null;
        }
        return findClassInModule.d(this.f5210a.c().p(), a10);
    }

    private final o0 g(g0 g0Var, g0 g0Var2) {
        List O;
        int u7;
        KotlinBuiltIns i10 = ld.a.i(g0Var);
        qb.g i11 = g0Var.i();
        g0 j10 = functionTypes.j(g0Var);
        List<g0> e10 = functionTypes.e(g0Var);
        O = _Collections.O(functionTypes.l(g0Var), 1);
        u7 = kotlin.collections.s.u(O, 10);
        ArrayList arrayList = new ArrayList(u7);
        Iterator it = O.iterator();
        while (it.hasNext()) {
            arrayList.add(((TypeProjection) it.next()).getType());
        }
        return functionTypes.b(i10, i11, j10, e10, arrayList, null, g0Var2, true).a1(g0Var.X0());
    }

    private final o0 h(c1 c1Var, TypeConstructor typeConstructor, List<? extends TypeProjection> list, boolean z10) {
        int size;
        int size2 = typeConstructor.getParameters().size() - list.size();
        o0 o0Var = null;
        if (size2 == 0) {
            o0Var = i(c1Var, typeConstructor, list, z10);
        } else if (size2 == 1 && (size = list.size() - 1) >= 0) {
            TypeConstructor n10 = typeConstructor.t().X(size).n();
            za.k.d(n10, "functionTypeConstructor.…on(arity).typeConstructor");
            o0Var = h0.j(c1Var, n10, list, z10, null, 16, null);
        }
        return o0Var == null ? ErrorUtils.f12833a.f(ErrorTypeKind.T, list, typeConstructor, new String[0]) : o0Var;
    }

    private final o0 i(c1 c1Var, TypeConstructor typeConstructor, List<? extends TypeProjection> list, boolean z10) {
        o0 j10 = h0.j(c1Var, typeConstructor, list, z10, null, 16, null);
        if (functionTypes.p(j10)) {
            return p(j10);
        }
        return null;
    }

    private final TypeParameterDescriptor k(int i10) {
        TypeParameterDescriptor typeParameterDescriptor = this.f5216g.get(Integer.valueOf(i10));
        if (typeParameterDescriptor != null) {
            return typeParameterDescriptor;
        }
        d0 d0Var = this.f5211b;
        if (d0Var != null) {
            return d0Var.k(i10);
        }
        return null;
    }

    private static final List<q.b> m(jc.q qVar, d0 d0Var) {
        List<q.b> m02;
        List<q.b> Q = qVar.Q();
        za.k.d(Q, "argumentList");
        jc.q j10 = protoTypeTableUtil.j(qVar, d0Var.f5210a.j());
        List<q.b> m10 = j10 != null ? m(j10, d0Var) : null;
        if (m10 == null) {
            m10 = kotlin.collections.r.j();
        }
        m02 = _Collections.m0(Q, m10);
        return m02;
    }

    public static /* synthetic */ o0 n(d0 d0Var, jc.q qVar, boolean z10, int i10, Object obj) {
        if ((i10 & 2) != 0) {
            z10 = true;
        }
        return d0Var.l(qVar, z10);
    }

    private final c1 o(List<? extends b1> list, qb.g gVar, TypeConstructor typeConstructor, DeclarationDescriptor declarationDescriptor) {
        int u7;
        List<? extends a1<?>> w10;
        u7 = kotlin.collections.s.u(list, 10);
        ArrayList arrayList = new ArrayList(u7);
        Iterator<T> it = list.iterator();
        while (it.hasNext()) {
            arrayList.add(((b1) it.next()).a(gVar, typeConstructor, declarationDescriptor));
        }
        w10 = kotlin.collections.s.w(arrayList);
        return c1.f11749f.g(w10);
    }

    /* JADX WARN: Code restructure failed: missing block: B:13:0x003f, code lost:
    
        if (za.k.a(r2, r3) == false) goto L63;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private final o0 p(g0 g0Var) {
        Object g02;
        g0 type;
        Object q02;
        FqName fqName;
        g02 = _Collections.g0(functionTypes.l(g0Var));
        TypeProjection typeProjection = (TypeProjection) g02;
        if (typeProjection == null || (type = typeProjection.getType()) == null) {
            return null;
        }
        ClassifierDescriptor v7 = type.W0().v();
        FqName l10 = v7 != null ? wc.c.l(v7) : null;
        if (type.U0().size() == 1) {
            if (!za.k.a(l10, StandardNames.f15278p)) {
                fqName = e0.f5227a;
            }
            q02 = _Collections.q0(type.U0());
            g0 type2 = ((TypeProjection) q02).getType();
            za.k.d(type2, "continuationArgumentType.arguments.single().type");
            DeclarationDescriptor e10 = this.f5210a.e();
            if (!(e10 instanceof CallableDescriptor)) {
                e10 = null;
            }
            CallableDescriptor callableDescriptor = (CallableDescriptor) e10;
            if (za.k.a(callableDescriptor != null ? wc.c.h(callableDescriptor) : null, suspendFunctionTypeUtil.f5205a)) {
                return g(g0Var, type2);
            }
            return g(g0Var, type2);
        }
        return (o0) g0Var;
    }

    private final TypeProjection r(TypeParameterDescriptor typeParameterDescriptor, q.b bVar) {
        if (bVar.s() == q.b.c.STAR) {
            if (typeParameterDescriptor == null) {
                return new t0(this.f5210a.c().p().t());
            }
            return new u0(typeParameterDescriptor);
        }
        ProtoEnumFlags protoEnumFlags = ProtoEnumFlags.f5188a;
        q.b.c s7 = bVar.s();
        za.k.d(s7, "typeArgumentProto.projection");
        Variance c10 = protoEnumFlags.c(s7);
        jc.q p10 = protoTypeTableUtil.p(bVar, this.f5210a.j());
        if (p10 == null) {
            return new TypeProjectionImpl(ErrorUtils.d(ErrorTypeKind.D0, bVar.toString()));
        }
        return new TypeProjectionImpl(c10, q(p10));
    }

    private final TypeConstructor s(jc.q qVar) {
        ClassifierDescriptor invoke;
        Object obj;
        if (qVar.g0()) {
            invoke = this.f5214e.invoke(Integer.valueOf(qVar.R()));
            if (invoke == null) {
                invoke = t(this, qVar, qVar.R());
            }
        } else if (qVar.p0()) {
            invoke = k(qVar.c0());
            if (invoke == null) {
                return ErrorUtils.f12833a.e(ErrorTypeKind.R, String.valueOf(qVar.c0()), this.f5213d);
            }
        } else if (qVar.q0()) {
            String string = this.f5210a.g().getString(qVar.d0());
            Iterator<T> it = j().iterator();
            while (true) {
                if (!it.hasNext()) {
                    obj = null;
                    break;
                }
                obj = it.next();
                if (za.k.a(((TypeParameterDescriptor) obj).getName().b(), string)) {
                    break;
                }
            }
            invoke = (TypeParameterDescriptor) obj;
            if (invoke == null) {
                return ErrorUtils.f12833a.e(ErrorTypeKind.S, string, this.f5210a.e().toString());
            }
        } else if (qVar.o0()) {
            invoke = this.f5215f.invoke(Integer.valueOf(qVar.b0()));
            if (invoke == null) {
                invoke = t(this, qVar, qVar.b0());
            }
        } else {
            return ErrorUtils.f12833a.e(ErrorTypeKind.V, new String[0]);
        }
        TypeConstructor n10 = invoke.n();
        za.k.d(n10, "classifier.typeConstructor");
        return n10;
    }

    private static final ClassDescriptor t(d0 d0Var, jc.q qVar, int i10) {
        Sequence f10;
        Sequence w10;
        List<Integer> D;
        Sequence f11;
        int k10;
        ClassId a10 = NameResolverUtil.a(d0Var.f5210a.g(), i10);
        f10 = rd.l.f(qVar, new e());
        w10 = _Sequences.w(f10, f.f5223e);
        D = _Sequences.D(w10);
        f11 = rd.l.f(a10, d.f5221n);
        k10 = _Sequences.k(f11);
        while (D.size() < k10) {
            D.add(0);
        }
        return d0Var.f5210a.c().q().d(a10, D);
    }

    public final List<TypeParameterDescriptor> j() {
        List<TypeParameterDescriptor> z02;
        z02 = _Collections.z0(this.f5216g.values());
        return z02;
    }

    public final o0 l(jc.q qVar, boolean z10) {
        o0 e10;
        int u7;
        List<? extends TypeProjection> z02;
        o0 j10;
        o0 j11;
        List<? extends AnnotationDescriptor> k02;
        Object W;
        za.k.e(qVar, "proto");
        if (qVar.g0()) {
            e10 = e(qVar.R());
        } else {
            e10 = qVar.o0() ? e(qVar.b0()) : null;
        }
        if (e10 != null) {
            return e10;
        }
        TypeConstructor s7 = s(qVar);
        boolean z11 = true;
        if (ErrorUtils.m(s7.v())) {
            return ErrorUtils.f12833a.c(ErrorTypeKind.f12828y0, s7, s7.toString());
        }
        ed.a aVar = new ed.a(this.f5210a.h(), new b(qVar));
        c1 o10 = o(this.f5210a.c().v(), aVar, s7, this.f5210a.e());
        List<q.b> m10 = m(qVar, this);
        u7 = kotlin.collections.s.u(m10, 10);
        ArrayList arrayList = new ArrayList(u7);
        int i10 = 0;
        for (Object obj : m10) {
            int i11 = i10 + 1;
            if (i10 < 0) {
                kotlin.collections.r.t();
            }
            List<TypeParameterDescriptor> parameters = s7.getParameters();
            za.k.d(parameters, "constructor.parameters");
            W = _Collections.W(parameters, i10);
            arrayList.add(r((TypeParameterDescriptor) W, (q.b) obj));
            i10 = i11;
        }
        z02 = _Collections.z0(arrayList);
        ClassifierDescriptor v7 = s7.v();
        if (z10 && (v7 instanceof TypeAliasDescriptor)) {
            h0 h0Var = h0.f11813a;
            o0 b10 = h0.b((TypeAliasDescriptor) v7, z02);
            List<b1> v10 = this.f5210a.c().v();
            g.a aVar2 = qb.g.f17195b;
            k02 = _Collections.k0(aVar, b10.i());
            c1 o11 = o(v10, aVar2.a(k02), s7, this.f5210a.e());
            if (!i0.b(b10) && !qVar.Y()) {
                z11 = false;
            }
            j10 = b10.a1(z11).c1(o11);
        } else {
            Boolean d10 = Flags.f14666a.d(qVar.U());
            za.k.d(d10, "SUSPEND_TYPE.get(proto.flags)");
            if (d10.booleanValue()) {
                j10 = h(o10, s7, z02, qVar.Y());
            } else {
                j10 = h0.j(o10, s7, z02, qVar.Y(), null, 16, null);
                Boolean d11 = Flags.f14667b.d(qVar.U());
                za.k.d(d11, "DEFINITELY_NOT_NULL_TYPE.get(proto.flags)");
                if (d11.booleanValue()) {
                    gd.p b11 = gd.p.f11860h.b(j10, true);
                    if (b11 == null) {
                        throw new IllegalStateException(("null DefinitelyNotNullType for '" + j10 + '\'').toString());
                    }
                    j10 = b11;
                }
            }
        }
        jc.q a10 = protoTypeTableUtil.a(qVar, this.f5210a.j());
        if (a10 != null && (j11 = s0.j(j10, l(a10, false))) != null) {
            j10 = j11;
        }
        return qVar.g0() ? this.f5210a.c().t().a(NameResolverUtil.a(this.f5210a.g(), qVar.R()), j10) : j10;
    }

    public final g0 q(jc.q qVar) {
        za.k.e(qVar, "proto");
        if (qVar.i0()) {
            String string = this.f5210a.g().getString(qVar.V());
            o0 n10 = n(this, qVar, false, 2, null);
            jc.q f10 = protoTypeTableUtil.f(qVar, this.f5210a.j());
            za.k.b(f10);
            return this.f5210a.c().l().a(qVar, string, n10, n(this, f10, false, 2, null));
        }
        return l(qVar, true);
    }

    public String toString() {
        String str;
        StringBuilder sb2 = new StringBuilder();
        sb2.append(this.f5212c);
        if (this.f5211b == null) {
            str = "";
        } else {
            str = ". Child of " + this.f5211b.f5212c;
        }
        sb2.append(str);
        return sb2.toString();
    }
}
