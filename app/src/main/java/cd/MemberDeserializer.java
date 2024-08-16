package cd;

import cd.ProtoContainer;
import ed.DeserializedClassDescriptor;
import gd.g0;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import kotlin.collections.CollectionsJVM;
import kotlin.collections._Collections;
import kotlin.collections.m0;
import lc.Flags;
import lc.VersionRequirement;
import lc.protoTypeTableUtil;
import oc.Name;
import pb.CallableDescriptor;
import pb.CallableMemberDescriptor;
import pb.ClassConstructorDescriptor;
import pb.ClassDescriptor;
import pb.ClassKind;
import pb.DeclarationDescriptor;
import pb.Modality;
import pb.PackageFragmentDescriptor;
import pb.PropertyDescriptor;
import pb.ReceiverParameterDescriptor;
import pb.SimpleFunctionDescriptor;
import pb.SourceElement;
import pb.TypeAliasDescriptor;
import pb.TypeParameterDescriptor;
import pb.ValueParameterDescriptor;
import qb.AnnotationDescriptor;
import qb.g;
import sb.FieldDescriptorImpl;
import sb.PropertyGetterDescriptorImpl;
import sb.PropertySetterDescriptorImpl;
import sb.ValueParameterDescriptorImpl;
import sc.DescriptorFactory;
import za.Lambda;

/* compiled from: MemberDeserializer.kt */
/* renamed from: cd.w, reason: use source file name */
/* loaded from: classes2.dex */
public final class MemberDeserializer {

    /* renamed from: a, reason: collision with root package name */
    private final m f5292a;

    /* renamed from: b, reason: collision with root package name */
    private final AnnotationDeserializer f5293b;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: MemberDeserializer.kt */
    /* renamed from: cd.w$a */
    /* loaded from: classes2.dex */
    public static final class a extends Lambda implements ya.a<List<? extends AnnotationDescriptor>> {

        /* renamed from: f, reason: collision with root package name */
        final /* synthetic */ qc.q f5295f;

        /* renamed from: g, reason: collision with root package name */
        final /* synthetic */ AnnotatedCallableKind f5296g;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        a(qc.q qVar, AnnotatedCallableKind annotatedCallableKind) {
            super(0);
            this.f5295f = qVar;
            this.f5296g = annotatedCallableKind;
        }

        @Override // ya.a
        /* renamed from: a, reason: merged with bridge method [inline-methods] */
        public final List<AnnotationDescriptor> invoke() {
            List<AnnotationDescriptor> list;
            List<AnnotationDescriptor> j10;
            MemberDeserializer memberDeserializer = MemberDeserializer.this;
            ProtoContainer c10 = memberDeserializer.c(memberDeserializer.f5292a.e());
            if (c10 != null) {
                MemberDeserializer memberDeserializer2 = MemberDeserializer.this;
                list = _Collections.z0(memberDeserializer2.f5292a.c().d().e(c10, this.f5295f, this.f5296g));
            } else {
                list = null;
            }
            if (list != null) {
                return list;
            }
            j10 = kotlin.collections.r.j();
            return j10;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: MemberDeserializer.kt */
    /* renamed from: cd.w$b */
    /* loaded from: classes2.dex */
    public static final class b extends Lambda implements ya.a<List<? extends AnnotationDescriptor>> {

        /* renamed from: f, reason: collision with root package name */
        final /* synthetic */ boolean f5298f;

        /* renamed from: g, reason: collision with root package name */
        final /* synthetic */ jc.n f5299g;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        b(boolean z10, jc.n nVar) {
            super(0);
            this.f5298f = z10;
            this.f5299g = nVar;
        }

        @Override // ya.a
        /* renamed from: a, reason: merged with bridge method [inline-methods] */
        public final List<AnnotationDescriptor> invoke() {
            List<AnnotationDescriptor> list;
            List<AnnotationDescriptor> j10;
            MemberDeserializer memberDeserializer = MemberDeserializer.this;
            ProtoContainer c10 = memberDeserializer.c(memberDeserializer.f5292a.e());
            if (c10 != null) {
                boolean z10 = this.f5298f;
                MemberDeserializer memberDeserializer2 = MemberDeserializer.this;
                jc.n nVar = this.f5299g;
                if (z10) {
                    list = _Collections.z0(memberDeserializer2.f5292a.c().d().c(c10, nVar));
                } else {
                    list = _Collections.z0(memberDeserializer2.f5292a.c().d().a(c10, nVar));
                }
            } else {
                list = null;
            }
            if (list != null) {
                return list;
            }
            j10 = kotlin.collections.r.j();
            return j10;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: MemberDeserializer.kt */
    /* renamed from: cd.w$c */
    /* loaded from: classes2.dex */
    public static final class c extends Lambda implements ya.a<List<? extends AnnotationDescriptor>> {

        /* renamed from: f, reason: collision with root package name */
        final /* synthetic */ qc.q f5301f;

        /* renamed from: g, reason: collision with root package name */
        final /* synthetic */ AnnotatedCallableKind f5302g;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        c(qc.q qVar, AnnotatedCallableKind annotatedCallableKind) {
            super(0);
            this.f5301f = qVar;
            this.f5302g = annotatedCallableKind;
        }

        @Override // ya.a
        /* renamed from: a, reason: merged with bridge method [inline-methods] */
        public final List<AnnotationDescriptor> invoke() {
            List<AnnotationDescriptor> list;
            List<AnnotationDescriptor> j10;
            MemberDeserializer memberDeserializer = MemberDeserializer.this;
            ProtoContainer c10 = memberDeserializer.c(memberDeserializer.f5292a.e());
            if (c10 != null) {
                MemberDeserializer memberDeserializer2 = MemberDeserializer.this;
                list = memberDeserializer2.f5292a.c().d().g(c10, this.f5301f, this.f5302g);
            } else {
                list = null;
            }
            if (list != null) {
                return list;
            }
            j10 = kotlin.collections.r.j();
            return j10;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: MemberDeserializer.kt */
    /* renamed from: cd.w$d */
    /* loaded from: classes2.dex */
    public static final class d extends Lambda implements ya.a<fd.j<? extends uc.g<?>>> {

        /* renamed from: f, reason: collision with root package name */
        final /* synthetic */ jc.n f5304f;

        /* renamed from: g, reason: collision with root package name */
        final /* synthetic */ ed.j f5305g;

        /* JADX INFO: Access modifiers changed from: package-private */
        /* compiled from: MemberDeserializer.kt */
        /* renamed from: cd.w$d$a */
        /* loaded from: classes2.dex */
        public static final class a extends Lambda implements ya.a<uc.g<?>> {

            /* renamed from: e, reason: collision with root package name */
            final /* synthetic */ MemberDeserializer f5306e;

            /* renamed from: f, reason: collision with root package name */
            final /* synthetic */ jc.n f5307f;

            /* renamed from: g, reason: collision with root package name */
            final /* synthetic */ ed.j f5308g;

            /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
            a(MemberDeserializer memberDeserializer, jc.n nVar, ed.j jVar) {
                super(0);
                this.f5306e = memberDeserializer;
                this.f5307f = nVar;
                this.f5308g = jVar;
            }

            @Override // ya.a
            /* renamed from: a, reason: merged with bridge method [inline-methods] */
            public final uc.g<?> invoke() {
                MemberDeserializer memberDeserializer = this.f5306e;
                ProtoContainer c10 = memberDeserializer.c(memberDeserializer.f5292a.e());
                za.k.b(c10);
                AnnotationAndConstantLoader<AnnotationDescriptor, uc.g<?>> d10 = this.f5306e.f5292a.c().d();
                jc.n nVar = this.f5307f;
                g0 f10 = this.f5308g.f();
                za.k.d(f10, "property.returnType");
                return d10.f(c10, nVar, f10);
            }
        }

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        d(jc.n nVar, ed.j jVar) {
            super(0);
            this.f5304f = nVar;
            this.f5305g = jVar;
        }

        @Override // ya.a
        /* renamed from: a, reason: merged with bridge method [inline-methods] */
        public final fd.j<uc.g<?>> invoke() {
            return MemberDeserializer.this.f5292a.h().f(new a(MemberDeserializer.this, this.f5304f, this.f5305g));
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: MemberDeserializer.kt */
    /* renamed from: cd.w$e */
    /* loaded from: classes2.dex */
    public static final class e extends Lambda implements ya.a<fd.j<? extends uc.g<?>>> {

        /* renamed from: f, reason: collision with root package name */
        final /* synthetic */ jc.n f5310f;

        /* renamed from: g, reason: collision with root package name */
        final /* synthetic */ ed.j f5311g;

        /* JADX INFO: Access modifiers changed from: package-private */
        /* compiled from: MemberDeserializer.kt */
        /* renamed from: cd.w$e$a */
        /* loaded from: classes2.dex */
        public static final class a extends Lambda implements ya.a<uc.g<?>> {

            /* renamed from: e, reason: collision with root package name */
            final /* synthetic */ MemberDeserializer f5312e;

            /* renamed from: f, reason: collision with root package name */
            final /* synthetic */ jc.n f5313f;

            /* renamed from: g, reason: collision with root package name */
            final /* synthetic */ ed.j f5314g;

            /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
            a(MemberDeserializer memberDeserializer, jc.n nVar, ed.j jVar) {
                super(0);
                this.f5312e = memberDeserializer;
                this.f5313f = nVar;
                this.f5314g = jVar;
            }

            @Override // ya.a
            /* renamed from: a, reason: merged with bridge method [inline-methods] */
            public final uc.g<?> invoke() {
                MemberDeserializer memberDeserializer = this.f5312e;
                ProtoContainer c10 = memberDeserializer.c(memberDeserializer.f5292a.e());
                za.k.b(c10);
                AnnotationAndConstantLoader<AnnotationDescriptor, uc.g<?>> d10 = this.f5312e.f5292a.c().d();
                jc.n nVar = this.f5313f;
                g0 f10 = this.f5314g.f();
                za.k.d(f10, "property.returnType");
                return d10.b(c10, nVar, f10);
            }
        }

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        e(jc.n nVar, ed.j jVar) {
            super(0);
            this.f5310f = nVar;
            this.f5311g = jVar;
        }

        @Override // ya.a
        /* renamed from: a, reason: merged with bridge method [inline-methods] */
        public final fd.j<uc.g<?>> invoke() {
            return MemberDeserializer.this.f5292a.h().f(new a(MemberDeserializer.this, this.f5310f, this.f5311g));
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: MemberDeserializer.kt */
    /* renamed from: cd.w$f */
    /* loaded from: classes2.dex */
    public static final class f extends Lambda implements ya.a<List<? extends AnnotationDescriptor>> {

        /* renamed from: f, reason: collision with root package name */
        final /* synthetic */ ProtoContainer f5316f;

        /* renamed from: g, reason: collision with root package name */
        final /* synthetic */ qc.q f5317g;

        /* renamed from: h, reason: collision with root package name */
        final /* synthetic */ AnnotatedCallableKind f5318h;

        /* renamed from: i, reason: collision with root package name */
        final /* synthetic */ int f5319i;

        /* renamed from: j, reason: collision with root package name */
        final /* synthetic */ jc.u f5320j;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        f(ProtoContainer protoContainer, qc.q qVar, AnnotatedCallableKind annotatedCallableKind, int i10, jc.u uVar) {
            super(0);
            this.f5316f = protoContainer;
            this.f5317g = qVar;
            this.f5318h = annotatedCallableKind;
            this.f5319i = i10;
            this.f5320j = uVar;
        }

        @Override // ya.a
        /* renamed from: a, reason: merged with bridge method [inline-methods] */
        public final List<AnnotationDescriptor> invoke() {
            List<AnnotationDescriptor> z02;
            z02 = _Collections.z0(MemberDeserializer.this.f5292a.c().d().k(this.f5316f, this.f5317g, this.f5318h, this.f5319i, this.f5320j));
            return z02;
        }
    }

    public MemberDeserializer(m mVar) {
        za.k.e(mVar, "c");
        this.f5292a = mVar;
        this.f5293b = new AnnotationDeserializer(mVar.c().p(), mVar.c().q());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final ProtoContainer c(DeclarationDescriptor declarationDescriptor) {
        if (declarationDescriptor instanceof PackageFragmentDescriptor) {
            return new ProtoContainer.b(((PackageFragmentDescriptor) declarationDescriptor).d(), this.f5292a.g(), this.f5292a.j(), this.f5292a.d());
        }
        if (declarationDescriptor instanceof DeserializedClassDescriptor) {
            return ((DeserializedClassDescriptor) declarationDescriptor).o1();
        }
        return null;
    }

    private final qb.g d(qc.q qVar, int i10, AnnotatedCallableKind annotatedCallableKind) {
        if (!Flags.f14668c.d(i10).booleanValue()) {
            return qb.g.f17195b.b();
        }
        return new ed.n(this.f5292a.h(), new a(qVar, annotatedCallableKind));
    }

    private final ReceiverParameterDescriptor e() {
        DeclarationDescriptor e10 = this.f5292a.e();
        ClassDescriptor classDescriptor = e10 instanceof ClassDescriptor ? (ClassDescriptor) e10 : null;
        if (classDescriptor != null) {
            return classDescriptor.S0();
        }
        return null;
    }

    private final qb.g f(jc.n nVar, boolean z10) {
        if (!Flags.f14668c.d(nVar.V()).booleanValue()) {
            return qb.g.f17195b.b();
        }
        return new ed.n(this.f5292a.h(), new b(z10, nVar));
    }

    private final qb.g g(qc.q qVar, AnnotatedCallableKind annotatedCallableKind) {
        return new ed.a(this.f5292a.h(), new c(qVar, annotatedCallableKind));
    }

    private final void h(ed.k kVar, ReceiverParameterDescriptor receiverParameterDescriptor, ReceiverParameterDescriptor receiverParameterDescriptor2, List<? extends ReceiverParameterDescriptor> list, List<? extends TypeParameterDescriptor> list2, List<? extends ValueParameterDescriptor> list3, g0 g0Var, Modality modality, pb.u uVar, Map<? extends CallableDescriptor.a<?>, ?> map) {
        kVar.x1(receiverParameterDescriptor, receiverParameterDescriptor2, list, list2, list3, g0Var, modality, uVar, map);
    }

    private final int k(int i10) {
        return (i10 & 63) + ((i10 >> 8) << 6);
    }

    private final ReceiverParameterDescriptor n(jc.q qVar, m mVar, CallableDescriptor callableDescriptor) {
        return DescriptorFactory.b(callableDescriptor, mVar.i().q(qVar), null, qb.g.f17195b.b());
    }

    /* JADX WARN: Removed duplicated region for block: B:16:0x00e8  */
    /* JADX WARN: Removed duplicated region for block: B:19:0x00f3  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private final List<ValueParameterDescriptor> o(List<jc.u> list, qc.q qVar, AnnotatedCallableKind annotatedCallableKind) {
        int u7;
        List<ValueParameterDescriptor> z02;
        qb.g b10;
        DeclarationDescriptor e10 = this.f5292a.e();
        za.k.c(e10, "null cannot be cast to non-null type org.jetbrains.kotlin.descriptors.CallableDescriptor");
        CallableDescriptor callableDescriptor = (CallableDescriptor) e10;
        DeclarationDescriptor b11 = callableDescriptor.b();
        za.k.d(b11, "callableDescriptor.containingDeclaration");
        ProtoContainer c10 = c(b11);
        u7 = kotlin.collections.s.u(list, 10);
        ArrayList arrayList = new ArrayList(u7);
        int i10 = 0;
        for (Object obj : list) {
            int i11 = i10 + 1;
            if (i10 < 0) {
                kotlin.collections.r.t();
            }
            jc.u uVar = (jc.u) obj;
            int F = uVar.L() ? uVar.F() : 0;
            if (c10 != null) {
                Boolean d10 = Flags.f14668c.d(F);
                za.k.d(d10, "HAS_ANNOTATIONS.get(flags)");
                if (d10.booleanValue()) {
                    b10 = new ed.n(this.f5292a.h(), new f(c10, qVar, annotatedCallableKind, i10, uVar));
                    Name b12 = NameResolverUtil.b(this.f5292a.g(), uVar.G());
                    g0 q10 = this.f5292a.i().q(protoTypeTableUtil.q(uVar, this.f5292a.j()));
                    Boolean d11 = Flags.G.d(F);
                    za.k.d(d11, "DECLARES_DEFAULT_VALUE.get(flags)");
                    boolean booleanValue = d11.booleanValue();
                    Boolean d12 = Flags.H.d(F);
                    za.k.d(d12, "IS_CROSSINLINE.get(flags)");
                    boolean booleanValue2 = d12.booleanValue();
                    Boolean d13 = Flags.I.d(F);
                    za.k.d(d13, "IS_NOINLINE.get(flags)");
                    boolean booleanValue3 = d13.booleanValue();
                    jc.q t7 = protoTypeTableUtil.t(uVar, this.f5292a.j());
                    g0 q11 = t7 == null ? this.f5292a.i().q(t7) : null;
                    SourceElement sourceElement = SourceElement.f16664a;
                    za.k.d(sourceElement, "NO_SOURCE");
                    ArrayList arrayList2 = arrayList;
                    arrayList2.add(new ValueParameterDescriptorImpl(callableDescriptor, null, i10, b10, b12, q10, booleanValue, booleanValue2, booleanValue3, q11, sourceElement));
                    arrayList = arrayList2;
                    i10 = i11;
                }
            }
            b10 = qb.g.f17195b.b();
            Name b122 = NameResolverUtil.b(this.f5292a.g(), uVar.G());
            g0 q102 = this.f5292a.i().q(protoTypeTableUtil.q(uVar, this.f5292a.j()));
            Boolean d112 = Flags.G.d(F);
            za.k.d(d112, "DECLARES_DEFAULT_VALUE.get(flags)");
            boolean booleanValue4 = d112.booleanValue();
            Boolean d122 = Flags.H.d(F);
            za.k.d(d122, "IS_CROSSINLINE.get(flags)");
            boolean booleanValue22 = d122.booleanValue();
            Boolean d132 = Flags.I.d(F);
            za.k.d(d132, "IS_NOINLINE.get(flags)");
            boolean booleanValue32 = d132.booleanValue();
            jc.q t72 = protoTypeTableUtil.t(uVar, this.f5292a.j());
            g0 q112 = t72 == null ? this.f5292a.i().q(t72) : null;
            SourceElement sourceElement2 = SourceElement.f16664a;
            za.k.d(sourceElement2, "NO_SOURCE");
            ArrayList arrayList22 = arrayList;
            arrayList22.add(new ValueParameterDescriptorImpl(callableDescriptor, null, i10, b10, b122, q102, booleanValue4, booleanValue22, booleanValue32, q112, sourceElement2));
            arrayList = arrayList22;
            i10 = i11;
        }
        z02 = _Collections.z0(arrayList);
        return z02;
    }

    public final ClassConstructorDescriptor i(jc.d dVar, boolean z10) {
        List j10;
        za.k.e(dVar, "proto");
        DeclarationDescriptor e10 = this.f5292a.e();
        za.k.c(e10, "null cannot be cast to non-null type org.jetbrains.kotlin.descriptors.ClassDescriptor");
        ClassDescriptor classDescriptor = (ClassDescriptor) e10;
        int E = dVar.E();
        AnnotatedCallableKind annotatedCallableKind = AnnotatedCallableKind.FUNCTION;
        ed.c cVar = new ed.c(classDescriptor, null, d(dVar, E, annotatedCallableKind), z10, CallableMemberDescriptor.a.DECLARATION, dVar, this.f5292a.g(), this.f5292a.j(), this.f5292a.k(), this.f5292a.d(), null, 1024, null);
        m mVar = this.f5292a;
        j10 = kotlin.collections.r.j();
        MemberDeserializer f10 = m.b(mVar, cVar, j10, null, null, null, null, 60, null).f();
        List<jc.u> H = dVar.H();
        za.k.d(H, "proto.valueParameterList");
        cVar.z1(f10.o(H, dVar, annotatedCallableKind), ProtoEnumFlagsUtils.a(ProtoEnumFlags.f5188a, Flags.f14669d.d(dVar.E())));
        cVar.p1(classDescriptor.x());
        cVar.f1(classDescriptor.U());
        cVar.h1(!Flags.f14679n.d(dVar.E()).booleanValue());
        return cVar;
    }

    public final SimpleFunctionDescriptor j(jc.i iVar) {
        qb.g b10;
        VersionRequirement k10;
        Map<? extends CallableDescriptor.a<?>, ?> i10;
        g0 q10;
        za.k.e(iVar, "proto");
        int X = iVar.n0() ? iVar.X() : k(iVar.Z());
        AnnotatedCallableKind annotatedCallableKind = AnnotatedCallableKind.FUNCTION;
        qb.g d10 = d(iVar, X, annotatedCallableKind);
        if (protoTypeTableUtil.g(iVar)) {
            b10 = g(iVar, annotatedCallableKind);
        } else {
            b10 = qb.g.f17195b.b();
        }
        if (za.k.a(wc.c.l(this.f5292a.e()).c(NameResolverUtil.b(this.f5292a.g(), iVar.Y())), suspendFunctionTypeUtil.f5205a)) {
            k10 = VersionRequirement.f14699b.b();
        } else {
            k10 = this.f5292a.k();
        }
        ed.k kVar = new ed.k(this.f5292a.e(), null, d10, NameResolverUtil.b(this.f5292a.g(), iVar.Y()), ProtoEnumFlagsUtils.b(ProtoEnumFlags.f5188a, Flags.f14680o.d(X)), iVar, this.f5292a.g(), this.f5292a.j(), k10, this.f5292a.d(), null, 1024, null);
        m mVar = this.f5292a;
        List<jc.s> g02 = iVar.g0();
        za.k.d(g02, "proto.typeParameterList");
        m b11 = m.b(mVar, kVar, g02, null, null, null, null, 60, null);
        jc.q k11 = protoTypeTableUtil.k(iVar, this.f5292a.j());
        ReceiverParameterDescriptor i11 = (k11 == null || (q10 = b11.i().q(k11)) == null) ? null : DescriptorFactory.i(kVar, q10, b10);
        ReceiverParameterDescriptor e10 = e();
        List<jc.q> c10 = protoTypeTableUtil.c(iVar, this.f5292a.j());
        List<? extends ReceiverParameterDescriptor> arrayList = new ArrayList<>();
        Iterator<T> it = c10.iterator();
        while (it.hasNext()) {
            ReceiverParameterDescriptor n10 = n((jc.q) it.next(), b11, kVar);
            if (n10 != null) {
                arrayList.add(n10);
            }
        }
        List<TypeParameterDescriptor> j10 = b11.i().j();
        MemberDeserializer f10 = b11.f();
        List<jc.u> k02 = iVar.k0();
        za.k.d(k02, "proto.valueParameterList");
        List<ValueParameterDescriptor> o10 = f10.o(k02, iVar, AnnotatedCallableKind.FUNCTION);
        g0 q11 = b11.i().q(protoTypeTableUtil.m(iVar, this.f5292a.j()));
        ProtoEnumFlags protoEnumFlags = ProtoEnumFlags.f5188a;
        Modality b12 = protoEnumFlags.b(Flags.f14670e.d(X));
        pb.u a10 = ProtoEnumFlagsUtils.a(protoEnumFlags, Flags.f14669d.d(X));
        i10 = m0.i();
        h(kVar, i11, e10, arrayList, j10, o10, q11, b12, a10, i10);
        Boolean d11 = Flags.f14681p.d(X);
        za.k.d(d11, "IS_OPERATOR.get(flags)");
        kVar.o1(d11.booleanValue());
        Boolean d12 = Flags.f14682q.d(X);
        za.k.d(d12, "IS_INFIX.get(flags)");
        kVar.l1(d12.booleanValue());
        Boolean d13 = Flags.f14685t.d(X);
        za.k.d(d13, "IS_EXTERNAL_FUNCTION.get(flags)");
        kVar.g1(d13.booleanValue());
        Boolean d14 = Flags.f14683r.d(X);
        za.k.d(d14, "IS_INLINE.get(flags)");
        kVar.n1(d14.booleanValue());
        Boolean d15 = Flags.f14684s.d(X);
        za.k.d(d15, "IS_TAILREC.get(flags)");
        kVar.r1(d15.booleanValue());
        Boolean d16 = Flags.f14686u.d(X);
        za.k.d(d16, "IS_SUSPEND.get(flags)");
        kVar.q1(d16.booleanValue());
        Boolean d17 = Flags.f14687v.d(X);
        za.k.d(d17, "IS_EXPECT_FUNCTION.get(flags)");
        kVar.f1(d17.booleanValue());
        kVar.h1(!Flags.f14688w.d(X).booleanValue());
        ma.o<CallableDescriptor.a<?>, Object> a11 = this.f5292a.c().h().a(iVar, kVar, this.f5292a.j(), b11.i());
        if (a11 != null) {
            kVar.d1(a11.c(), a11.d());
        }
        return kVar;
    }

    public final PropertyDescriptor l(jc.n nVar) {
        jc.n nVar2;
        qb.g b10;
        ed.j jVar;
        ReceiverParameterDescriptor receiverParameterDescriptor;
        int u7;
        Flags.d<jc.x> dVar;
        m mVar;
        Flags.d<jc.k> dVar2;
        PropertyGetterDescriptorImpl propertyGetterDescriptorImpl;
        PropertyGetterDescriptorImpl propertyGetterDescriptorImpl2;
        ed.j jVar2;
        jc.n nVar3;
        int i10;
        boolean z10;
        PropertySetterDescriptorImpl propertySetterDescriptorImpl;
        List j10;
        List<jc.u> e10;
        Object q02;
        PropertyGetterDescriptorImpl d10;
        g0 q10;
        za.k.e(nVar, "proto");
        int V = nVar.j0() ? nVar.V() : k(nVar.Y());
        DeclarationDescriptor e11 = this.f5292a.e();
        qb.g d11 = d(nVar, V, AnnotatedCallableKind.PROPERTY);
        ProtoEnumFlags protoEnumFlags = ProtoEnumFlags.f5188a;
        Modality b11 = protoEnumFlags.b(Flags.f14670e.d(V));
        pb.u a10 = ProtoEnumFlagsUtils.a(protoEnumFlags, Flags.f14669d.d(V));
        Boolean d12 = Flags.f14689x.d(V);
        za.k.d(d12, "IS_VAR.get(flags)");
        boolean booleanValue = d12.booleanValue();
        Name b12 = NameResolverUtil.b(this.f5292a.g(), nVar.X());
        CallableMemberDescriptor.a b13 = ProtoEnumFlagsUtils.b(protoEnumFlags, Flags.f14680o.d(V));
        Boolean d13 = Flags.B.d(V);
        za.k.d(d13, "IS_LATEINIT.get(flags)");
        boolean booleanValue2 = d13.booleanValue();
        Boolean d14 = Flags.A.d(V);
        za.k.d(d14, "IS_CONST.get(flags)");
        boolean booleanValue3 = d14.booleanValue();
        Boolean d15 = Flags.D.d(V);
        za.k.d(d15, "IS_EXTERNAL_PROPERTY.get(flags)");
        boolean booleanValue4 = d15.booleanValue();
        Boolean d16 = Flags.E.d(V);
        za.k.d(d16, "IS_DELEGATED.get(flags)");
        boolean booleanValue5 = d16.booleanValue();
        Boolean d17 = Flags.F.d(V);
        za.k.d(d17, "IS_EXPECT_PROPERTY.get(flags)");
        ed.j jVar3 = new ed.j(e11, null, d11, b11, a10, booleanValue, b12, b13, booleanValue2, booleanValue3, booleanValue4, booleanValue5, d17.booleanValue(), nVar, this.f5292a.g(), this.f5292a.j(), this.f5292a.k(), this.f5292a.d());
        m mVar2 = this.f5292a;
        List<jc.s> h02 = nVar.h0();
        za.k.d(h02, "proto.typeParameterList");
        m b14 = m.b(mVar2, jVar3, h02, null, null, null, null, 60, null);
        Boolean d18 = Flags.f14690y.d(V);
        za.k.d(d18, "HAS_GETTER.get(flags)");
        boolean booleanValue6 = d18.booleanValue();
        if (booleanValue6 && protoTypeTableUtil.h(nVar)) {
            nVar2 = nVar;
            b10 = g(nVar2, AnnotatedCallableKind.PROPERTY_GETTER);
        } else {
            nVar2 = nVar;
            b10 = qb.g.f17195b.b();
        }
        g0 q11 = b14.i().q(protoTypeTableUtil.n(nVar2, this.f5292a.j()));
        List<TypeParameterDescriptor> j11 = b14.i().j();
        ReceiverParameterDescriptor e12 = e();
        jc.q l10 = protoTypeTableUtil.l(nVar2, this.f5292a.j());
        if (l10 == null || (q10 = b14.i().q(l10)) == null) {
            jVar = jVar3;
            receiverParameterDescriptor = null;
        } else {
            jVar = jVar3;
            receiverParameterDescriptor = DescriptorFactory.i(jVar, q10, b10);
        }
        List<jc.q> d19 = protoTypeTableUtil.d(nVar2, this.f5292a.j());
        u7 = kotlin.collections.s.u(d19, 10);
        ArrayList arrayList = new ArrayList(u7);
        Iterator<T> it = d19.iterator();
        while (it.hasNext()) {
            arrayList.add(n((jc.q) it.next(), b14, jVar));
        }
        jVar.k1(q11, j11, e12, receiverParameterDescriptor, arrayList);
        Boolean d20 = Flags.f14668c.d(V);
        za.k.d(d20, "HAS_ANNOTATIONS.get(flags)");
        boolean booleanValue7 = d20.booleanValue();
        Flags.d<jc.x> dVar3 = Flags.f14669d;
        jc.x d21 = dVar3.d(V);
        Flags.d<jc.k> dVar4 = Flags.f14670e;
        int b15 = Flags.b(booleanValue7, d21, dVar4.d(V), false, false, false);
        if (booleanValue6) {
            int W = nVar.k0() ? nVar.W() : b15;
            Boolean d22 = Flags.J.d(W);
            za.k.d(d22, "IS_NOT_DEFAULT.get(getterFlags)");
            boolean booleanValue8 = d22.booleanValue();
            Boolean d23 = Flags.K.d(W);
            za.k.d(d23, "IS_EXTERNAL_ACCESSOR.get(getterFlags)");
            boolean booleanValue9 = d23.booleanValue();
            Boolean d24 = Flags.L.d(W);
            za.k.d(d24, "IS_INLINE_ACCESSOR.get(getterFlags)");
            boolean booleanValue10 = d24.booleanValue();
            qb.g d25 = d(nVar2, W, AnnotatedCallableKind.PROPERTY_GETTER);
            if (booleanValue8) {
                ProtoEnumFlags protoEnumFlags2 = ProtoEnumFlags.f5188a;
                mVar = b14;
                dVar2 = dVar4;
                dVar = dVar3;
                d10 = new PropertyGetterDescriptorImpl(jVar, d25, protoEnumFlags2.b(dVar4.d(W)), ProtoEnumFlagsUtils.a(protoEnumFlags2, dVar3.d(W)), !booleanValue8, booleanValue9, booleanValue10, jVar.getKind(), null, SourceElement.f16664a);
            } else {
                dVar = dVar3;
                mVar = b14;
                dVar2 = dVar4;
                d10 = DescriptorFactory.d(jVar, d25);
                za.k.d(d10, "{\n                Descri…nnotations)\n            }");
            }
            d10.Z0(jVar.f());
            propertyGetterDescriptorImpl = d10;
        } else {
            dVar = dVar3;
            mVar = b14;
            dVar2 = dVar4;
            propertyGetterDescriptorImpl = null;
        }
        Boolean d26 = Flags.f14691z.d(V);
        za.k.d(d26, "HAS_SETTER.get(flags)");
        if (d26.booleanValue()) {
            if (nVar.r0()) {
                b15 = nVar.d0();
            }
            int i11 = b15;
            Boolean d27 = Flags.J.d(i11);
            za.k.d(d27, "IS_NOT_DEFAULT.get(setterFlags)");
            boolean booleanValue11 = d27.booleanValue();
            Boolean d28 = Flags.K.d(i11);
            za.k.d(d28, "IS_EXTERNAL_ACCESSOR.get(setterFlags)");
            boolean booleanValue12 = d28.booleanValue();
            Boolean d29 = Flags.L.d(i11);
            za.k.d(d29, "IS_INLINE_ACCESSOR.get(setterFlags)");
            boolean booleanValue13 = d29.booleanValue();
            AnnotatedCallableKind annotatedCallableKind = AnnotatedCallableKind.PROPERTY_SETTER;
            qb.g d30 = d(nVar2, i11, annotatedCallableKind);
            if (booleanValue11) {
                ProtoEnumFlags protoEnumFlags3 = ProtoEnumFlags.f5188a;
                propertyGetterDescriptorImpl2 = propertyGetterDescriptorImpl;
                PropertySetterDescriptorImpl propertySetterDescriptorImpl2 = new PropertySetterDescriptorImpl(jVar, d30, protoEnumFlags3.b(dVar2.d(i11)), ProtoEnumFlagsUtils.a(protoEnumFlags3, dVar.d(i11)), !booleanValue11, booleanValue12, booleanValue13, jVar.getKind(), null, SourceElement.f16664a);
                j10 = kotlin.collections.r.j();
                z10 = true;
                jVar2 = jVar;
                nVar3 = nVar2;
                i10 = V;
                MemberDeserializer f10 = m.b(mVar, propertySetterDescriptorImpl2, j10, null, null, null, null, 60, null).f();
                e10 = CollectionsJVM.e(nVar.e0());
                q02 = _Collections.q0(f10.o(e10, nVar3, annotatedCallableKind));
                propertySetterDescriptorImpl2.a1((ValueParameterDescriptor) q02);
                propertySetterDescriptorImpl = propertySetterDescriptorImpl2;
            } else {
                propertyGetterDescriptorImpl2 = propertyGetterDescriptorImpl;
                jVar2 = jVar;
                nVar3 = nVar2;
                i10 = V;
                z10 = true;
                propertySetterDescriptorImpl = DescriptorFactory.e(jVar2, d30, qb.g.f17195b.b());
                za.k.d(propertySetterDescriptorImpl, "{\n                Descri…          )\n            }");
            }
        } else {
            propertyGetterDescriptorImpl2 = propertyGetterDescriptorImpl;
            jVar2 = jVar;
            nVar3 = nVar2;
            i10 = V;
            z10 = true;
            propertySetterDescriptorImpl = null;
        }
        Boolean d31 = Flags.C.d(i10);
        za.k.d(d31, "HAS_CONSTANT.get(flags)");
        if (d31.booleanValue()) {
            jVar2.U0(new d(nVar3, jVar2));
        }
        DeclarationDescriptor e13 = this.f5292a.e();
        ClassDescriptor classDescriptor = e13 instanceof ClassDescriptor ? (ClassDescriptor) e13 : null;
        if ((classDescriptor != null ? classDescriptor.getKind() : null) == ClassKind.ANNOTATION_CLASS) {
            jVar2.U0(new e(nVar3, jVar2));
        }
        jVar2.e1(propertyGetterDescriptorImpl2, propertySetterDescriptorImpl, new FieldDescriptorImpl(f(nVar3, false), jVar2), new FieldDescriptorImpl(f(nVar3, z10), jVar2));
        return jVar2;
    }

    public final TypeAliasDescriptor m(jc.r rVar) {
        int u7;
        za.k.e(rVar, "proto");
        g.a aVar = qb.g.f17195b;
        List<jc.b> L = rVar.L();
        za.k.d(L, "proto.annotationList");
        u7 = kotlin.collections.s.u(L, 10);
        ArrayList arrayList = new ArrayList(u7);
        for (jc.b bVar : L) {
            AnnotationDeserializer annotationDeserializer = this.f5293b;
            za.k.d(bVar, "it");
            arrayList.add(annotationDeserializer.a(bVar, this.f5292a.g()));
        }
        ed.l lVar = new ed.l(this.f5292a.h(), this.f5292a.e(), aVar.a(arrayList), NameResolverUtil.b(this.f5292a.g(), rVar.R()), ProtoEnumFlagsUtils.a(ProtoEnumFlags.f5188a, Flags.f14669d.d(rVar.Q())), rVar, this.f5292a.g(), this.f5292a.j(), this.f5292a.k(), this.f5292a.d());
        m mVar = this.f5292a;
        List<jc.s> U = rVar.U();
        za.k.d(U, "proto.typeParameterList");
        m b10 = m.b(mVar, lVar, U, null, null, null, null, 60, null);
        lVar.Z0(b10.i().j(), b10.i().l(protoTypeTableUtil.r(rVar, this.f5292a.j()), false), b10.i().l(protoTypeTableUtil.e(rVar, this.f5292a.j()), false));
        return lVar;
    }
}
