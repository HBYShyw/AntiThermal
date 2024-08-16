package hc;

import cd.AnnotatedCallableKind;
import cd.AnnotationLoader;
import cd.ProtoContainer;
import hc.KotlinJvmBinaryClass;
import hc.MemberSignature;
import hc.b.a;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import jc.c;
import lb.SpecialJvmAnnotations;
import lc.Flags;
import lc.NameResolver;
import lc.ProtoBufUtil;
import lc.TypeTable;
import lc.protoTypeTableUtil;
import mc.JvmProtoBuf;
import nc.ClassMapperLite;
import nc.JvmMemberSignature;
import nc.JvmProtoBufUtil;
import oc.ClassId;
import oc.FqName;
import oc.Name;
import pb.SourceElement;
import qc.i;
import sd.StringsJVM;
import xc.JvmClassName;

/* compiled from: AbstractBinaryClassAnnotationLoader.kt */
/* loaded from: classes2.dex */
public abstract class b<A, S extends a<? extends A>> implements AnnotationLoader<A> {

    /* renamed from: a, reason: collision with root package name */
    private final p f12110a;

    /* compiled from: AbstractBinaryClassAnnotationLoader.kt */
    /* loaded from: classes2.dex */
    public static abstract class a<A> {
        public abstract Map<MemberSignature, List<A>> a();
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* compiled from: AbstractBinaryClassAnnotationLoader.kt */
    /* renamed from: hc.b$b, reason: collision with other inner class name */
    /* loaded from: classes2.dex */
    public enum EnumC0043b {
        PROPERTY,
        BACKING_FIELD,
        DELEGATE_FIELD
    }

    /* compiled from: AbstractBinaryClassAnnotationLoader.kt */
    /* loaded from: classes2.dex */
    public /* synthetic */ class c {

        /* renamed from: a, reason: collision with root package name */
        public static final /* synthetic */ int[] f12115a;

        static {
            int[] iArr = new int[AnnotatedCallableKind.values().length];
            try {
                iArr[AnnotatedCallableKind.PROPERTY_GETTER.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                iArr[AnnotatedCallableKind.PROPERTY_SETTER.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                iArr[AnnotatedCallableKind.PROPERTY.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
            f12115a = iArr;
        }
    }

    /* compiled from: AbstractBinaryClassAnnotationLoader.kt */
    /* loaded from: classes2.dex */
    public static final class d implements KotlinJvmBinaryClass.c {

        /* renamed from: a, reason: collision with root package name */
        final /* synthetic */ b<A, S> f12116a;

        /* renamed from: b, reason: collision with root package name */
        final /* synthetic */ ArrayList<A> f12117b;

        d(b<A, S> bVar, ArrayList<A> arrayList) {
            this.f12116a = bVar;
            this.f12117b = arrayList;
        }

        @Override // hc.KotlinJvmBinaryClass.c
        public void a() {
        }

        @Override // hc.KotlinJvmBinaryClass.c
        public KotlinJvmBinaryClass.a b(ClassId classId, SourceElement sourceElement) {
            za.k.e(classId, "classId");
            za.k.e(sourceElement, "source");
            return this.f12116a.w(classId, sourceElement, this.f12117b);
        }
    }

    public b(p pVar) {
        za.k.e(pVar, "kotlinClassFinder");
        this.f12110a = pVar;
    }

    private final int l(ProtoContainer protoContainer, qc.q qVar) {
        if (qVar instanceof jc.i) {
            if (protoTypeTableUtil.g((jc.i) qVar)) {
                return 1;
            }
        } else if (qVar instanceof jc.n) {
            if (protoTypeTableUtil.h((jc.n) qVar)) {
                return 1;
            }
        } else if (qVar instanceof jc.d) {
            za.k.c(protoContainer, "null cannot be cast to non-null type org.jetbrains.kotlin.serialization.deserialization.ProtoContainer.Class");
            ProtoContainer.a aVar = (ProtoContainer.a) protoContainer;
            if (aVar.g() == c.EnumC0065c.ENUM_CLASS) {
                return 2;
            }
            if (aVar.i()) {
                return 1;
            }
        } else {
            throw new UnsupportedOperationException("Unsupported message: " + qVar.getClass());
        }
        return 0;
    }

    private final List<A> m(ProtoContainer protoContainer, MemberSignature memberSignature, boolean z10, boolean z11, Boolean bool, boolean z12) {
        List<A> j10;
        List<A> j11;
        KotlinJvmBinaryClass o10 = o(protoContainer, t(protoContainer, z10, z11, bool, z12));
        if (o10 == null) {
            j11 = kotlin.collections.r.j();
            return j11;
        }
        List<A> list = p(o10).a().get(memberSignature);
        if (list != null) {
            return list;
        }
        j10 = kotlin.collections.r.j();
        return j10;
    }

    static /* synthetic */ List n(b bVar, ProtoContainer protoContainer, MemberSignature memberSignature, boolean z10, boolean z11, Boolean bool, boolean z12, int i10, Object obj) {
        if (obj == null) {
            return bVar.m(protoContainer, memberSignature, (i10 & 4) != 0 ? false : z10, (i10 & 8) != 0 ? false : z11, (i10 & 16) != 0 ? null : bool, (i10 & 32) != 0 ? false : z12);
        }
        throw new UnsupportedOperationException("Super calls with default arguments not supported in this target, function: findClassAndLoadMemberAnnotations");
    }

    public static /* synthetic */ MemberSignature s(b bVar, qc.q qVar, NameResolver nameResolver, TypeTable typeTable, AnnotatedCallableKind annotatedCallableKind, boolean z10, int i10, Object obj) {
        if (obj != null) {
            throw new UnsupportedOperationException("Super calls with default arguments not supported in this target, function: getCallableSignature");
        }
        if ((i10 & 16) != 0) {
            z10 = false;
        }
        return bVar.r(qVar, nameResolver, typeTable, annotatedCallableKind, z10);
    }

    private final List<A> x(ProtoContainer protoContainer, jc.n nVar, EnumC0043b enumC0043b) {
        boolean I;
        List<A> j10;
        List<A> j11;
        List<A> j12;
        Boolean d10 = Flags.A.d(nVar.V());
        za.k.d(d10, "IS_CONST.get(proto.flags)");
        boolean booleanValue = d10.booleanValue();
        boolean f10 = JvmProtoBufUtil.f(nVar);
        if (enumC0043b == EnumC0043b.PROPERTY) {
            MemberSignature b10 = hc.c.b(nVar, protoContainer.b(), protoContainer.d(), false, true, false, 40, null);
            if (b10 != null) {
                return n(this, protoContainer, b10, true, false, Boolean.valueOf(booleanValue), f10, 8, null);
            }
            j12 = kotlin.collections.r.j();
            return j12;
        }
        MemberSignature b11 = hc.c.b(nVar, protoContainer.b(), protoContainer.d(), true, false, false, 48, null);
        if (b11 == null) {
            j11 = kotlin.collections.r.j();
            return j11;
        }
        I = sd.v.I(b11.a(), "$delegate", false, 2, null);
        if (I == (enumC0043b == EnumC0043b.DELEGATE_FIELD)) {
            return m(protoContainer, b11, true, true, Boolean.valueOf(booleanValue), f10);
        }
        j10 = kotlin.collections.r.j();
        return j10;
    }

    private final KotlinJvmBinaryClass z(ProtoContainer.a aVar) {
        SourceElement c10 = aVar.c();
        KotlinJvmBinarySourceElement kotlinJvmBinarySourceElement = c10 instanceof KotlinJvmBinarySourceElement ? (KotlinJvmBinarySourceElement) c10 : null;
        if (kotlinJvmBinarySourceElement != null) {
            return kotlinJvmBinarySourceElement.d();
        }
        return null;
    }

    @Override // cd.AnnotationLoader
    public List<A> a(ProtoContainer protoContainer, jc.n nVar) {
        za.k.e(protoContainer, "container");
        za.k.e(nVar, "proto");
        return x(protoContainer, nVar, EnumC0043b.BACKING_FIELD);
    }

    @Override // cd.AnnotationLoader
    public List<A> c(ProtoContainer protoContainer, jc.n nVar) {
        za.k.e(protoContainer, "container");
        za.k.e(nVar, "proto");
        return x(protoContainer, nVar, EnumC0043b.DELEGATE_FIELD);
    }

    @Override // cd.AnnotationLoader
    public List<A> d(jc.q qVar, NameResolver nameResolver) {
        int u7;
        za.k.e(qVar, "proto");
        za.k.e(nameResolver, "nameResolver");
        Object p10 = qVar.p(JvmProtoBuf.f15369f);
        za.k.d(p10, "proto.getExtension(JvmProtoBuf.typeAnnotation)");
        Iterable<jc.b> iterable = (Iterable) p10;
        u7 = kotlin.collections.s.u(iterable, 10);
        ArrayList arrayList = new ArrayList(u7);
        for (jc.b bVar : iterable) {
            za.k.d(bVar, "it");
            arrayList.add(y(bVar, nameResolver));
        }
        return arrayList;
    }

    @Override // cd.AnnotationLoader
    public List<A> e(ProtoContainer protoContainer, qc.q qVar, AnnotatedCallableKind annotatedCallableKind) {
        List<A> j10;
        za.k.e(protoContainer, "container");
        za.k.e(qVar, "proto");
        za.k.e(annotatedCallableKind, "kind");
        if (annotatedCallableKind == AnnotatedCallableKind.PROPERTY) {
            return x(protoContainer, (jc.n) qVar, EnumC0043b.PROPERTY);
        }
        MemberSignature s7 = s(this, qVar, protoContainer.b(), protoContainer.d(), annotatedCallableKind, false, 16, null);
        if (s7 != null) {
            return n(this, protoContainer, s7, false, false, null, false, 60, null);
        }
        j10 = kotlin.collections.r.j();
        return j10;
    }

    @Override // cd.AnnotationLoader
    public List<A> g(ProtoContainer protoContainer, qc.q qVar, AnnotatedCallableKind annotatedCallableKind) {
        List<A> j10;
        za.k.e(protoContainer, "container");
        za.k.e(qVar, "proto");
        za.k.e(annotatedCallableKind, "kind");
        MemberSignature s7 = s(this, qVar, protoContainer.b(), protoContainer.d(), annotatedCallableKind, false, 16, null);
        if (s7 != null) {
            return n(this, protoContainer, MemberSignature.f12206b.e(s7, 0), false, false, null, false, 60, null);
        }
        j10 = kotlin.collections.r.j();
        return j10;
    }

    @Override // cd.AnnotationLoader
    public List<A> h(ProtoContainer protoContainer, jc.g gVar) {
        za.k.e(protoContainer, "container");
        za.k.e(gVar, "proto");
        MemberSignature.a aVar = MemberSignature.f12206b;
        String string = protoContainer.b().getString(gVar.A());
        String c10 = ((ProtoContainer.a) protoContainer).e().c();
        za.k.d(c10, "container as ProtoContai…Class).classId.asString()");
        return n(this, protoContainer, aVar.a(string, ClassMapperLite.b(c10)), false, false, null, false, 60, null);
    }

    @Override // cd.AnnotationLoader
    public List<A> i(ProtoContainer.a aVar) {
        za.k.e(aVar, "container");
        KotlinJvmBinaryClass z10 = z(aVar);
        if (z10 != null) {
            ArrayList arrayList = new ArrayList(1);
            z10.d(new d(this, arrayList), q(z10));
            return arrayList;
        }
        throw new IllegalStateException(("Class for loading annotations is not found: " + aVar.a()).toString());
    }

    @Override // cd.AnnotationLoader
    public List<A> j(jc.s sVar, NameResolver nameResolver) {
        int u7;
        za.k.e(sVar, "proto");
        za.k.e(nameResolver, "nameResolver");
        Object p10 = sVar.p(JvmProtoBuf.f15371h);
        za.k.d(p10, "proto.getExtension(JvmPr….typeParameterAnnotation)");
        Iterable<jc.b> iterable = (Iterable) p10;
        u7 = kotlin.collections.s.u(iterable, 10);
        ArrayList arrayList = new ArrayList(u7);
        for (jc.b bVar : iterable) {
            za.k.d(bVar, "it");
            arrayList.add(y(bVar, nameResolver));
        }
        return arrayList;
    }

    @Override // cd.AnnotationLoader
    public List<A> k(ProtoContainer protoContainer, qc.q qVar, AnnotatedCallableKind annotatedCallableKind, int i10, jc.u uVar) {
        List<A> j10;
        za.k.e(protoContainer, "container");
        za.k.e(qVar, "callableProto");
        za.k.e(annotatedCallableKind, "kind");
        za.k.e(uVar, "proto");
        MemberSignature s7 = s(this, qVar, protoContainer.b(), protoContainer.d(), annotatedCallableKind, false, 16, null);
        if (s7 != null) {
            return n(this, protoContainer, MemberSignature.f12206b.e(s7, i10 + l(protoContainer, qVar)), false, false, null, false, 60, null);
        }
        j10 = kotlin.collections.r.j();
        return j10;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public final KotlinJvmBinaryClass o(ProtoContainer protoContainer, KotlinJvmBinaryClass kotlinJvmBinaryClass) {
        za.k.e(protoContainer, "container");
        if (kotlinJvmBinaryClass != null) {
            return kotlinJvmBinaryClass;
        }
        if (protoContainer instanceof ProtoContainer.a) {
            return z((ProtoContainer.a) protoContainer);
        }
        return null;
    }

    protected abstract S p(KotlinJvmBinaryClass kotlinJvmBinaryClass);

    /* JADX INFO: Access modifiers changed from: protected */
    public byte[] q(KotlinJvmBinaryClass kotlinJvmBinaryClass) {
        za.k.e(kotlinJvmBinaryClass, "kotlinClass");
        return null;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public final MemberSignature r(qc.q qVar, NameResolver nameResolver, TypeTable typeTable, AnnotatedCallableKind annotatedCallableKind, boolean z10) {
        za.k.e(qVar, "proto");
        za.k.e(nameResolver, "nameResolver");
        za.k.e(typeTable, "typeTable");
        za.k.e(annotatedCallableKind, "kind");
        if (qVar instanceof jc.d) {
            MemberSignature.a aVar = MemberSignature.f12206b;
            JvmMemberSignature.b b10 = JvmProtoBufUtil.f16006a.b((jc.d) qVar, nameResolver, typeTable);
            if (b10 == null) {
                return null;
            }
            return aVar.b(b10);
        }
        if (qVar instanceof jc.i) {
            MemberSignature.a aVar2 = MemberSignature.f12206b;
            JvmMemberSignature.b e10 = JvmProtoBufUtil.f16006a.e((jc.i) qVar, nameResolver, typeTable);
            if (e10 == null) {
                return null;
            }
            return aVar2.b(e10);
        }
        if (!(qVar instanceof jc.n)) {
            return null;
        }
        i.f<jc.n, JvmProtoBuf.d> fVar = JvmProtoBuf.f15367d;
        za.k.d(fVar, "propertySignature");
        JvmProtoBuf.d dVar = (JvmProtoBuf.d) ProtoBufUtil.a((i.d) qVar, fVar);
        if (dVar == null) {
            return null;
        }
        int i10 = c.f12115a[annotatedCallableKind.ordinal()];
        if (i10 == 1) {
            if (!dVar.B()) {
                return null;
            }
            MemberSignature.a aVar3 = MemberSignature.f12206b;
            JvmProtoBuf.c w10 = dVar.w();
            za.k.d(w10, "signature.getter");
            return aVar3.c(nameResolver, w10);
        }
        if (i10 != 2) {
            if (i10 != 3) {
                return null;
            }
            return hc.c.a((jc.n) qVar, nameResolver, typeTable, true, true, z10);
        }
        if (!dVar.C()) {
            return null;
        }
        MemberSignature.a aVar4 = MemberSignature.f12206b;
        JvmProtoBuf.c x10 = dVar.x();
        za.k.d(x10, "signature.setter");
        return aVar4.c(nameResolver, x10);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public final KotlinJvmBinaryClass t(ProtoContainer protoContainer, boolean z10, boolean z11, Boolean bool, boolean z12) {
        ProtoContainer.a h10;
        String y4;
        za.k.e(protoContainer, "container");
        if (z10) {
            if (bool != null) {
                if (protoContainer instanceof ProtoContainer.a) {
                    ProtoContainer.a aVar = (ProtoContainer.a) protoContainer;
                    if (aVar.g() == c.EnumC0065c.INTERFACE) {
                        p pVar = this.f12110a;
                        ClassId d10 = aVar.e().d(Name.f("DefaultImpls"));
                        za.k.d(d10, "container.classId.create…EFAULT_IMPLS_CLASS_NAME))");
                        return q.b(pVar, d10);
                    }
                }
                if (bool.booleanValue() && (protoContainer instanceof ProtoContainer.b)) {
                    SourceElement c10 = protoContainer.c();
                    JvmPackagePartSource jvmPackagePartSource = c10 instanceof JvmPackagePartSource ? (JvmPackagePartSource) c10 : null;
                    JvmClassName f10 = jvmPackagePartSource != null ? jvmPackagePartSource.f() : null;
                    if (f10 != null) {
                        p pVar2 = this.f12110a;
                        String f11 = f10.f();
                        za.k.d(f11, "facadeClassName.internalName");
                        y4 = StringsJVM.y(f11, '/', '.', false, 4, null);
                        ClassId m10 = ClassId.m(new FqName(y4));
                        za.k.d(m10, "topLevel(FqName(facadeCl…lName.replace('/', '.')))");
                        return q.b(pVar2, m10);
                    }
                }
            } else {
                throw new IllegalStateException(("isConst should not be null for property (container=" + protoContainer + ')').toString());
            }
        }
        if (z11 && (protoContainer instanceof ProtoContainer.a)) {
            ProtoContainer.a aVar2 = (ProtoContainer.a) protoContainer;
            if (aVar2.g() == c.EnumC0065c.COMPANION_OBJECT && (h10 = aVar2.h()) != null && (h10.g() == c.EnumC0065c.CLASS || h10.g() == c.EnumC0065c.ENUM_CLASS || (z12 && (h10.g() == c.EnumC0065c.INTERFACE || h10.g() == c.EnumC0065c.ANNOTATION_CLASS)))) {
                return z(h10);
            }
        }
        if (!(protoContainer instanceof ProtoContainer.b) || !(protoContainer.c() instanceof JvmPackagePartSource)) {
            return null;
        }
        SourceElement c11 = protoContainer.c();
        za.k.c(c11, "null cannot be cast to non-null type org.jetbrains.kotlin.load.kotlin.JvmPackagePartSource");
        JvmPackagePartSource jvmPackagePartSource2 = (JvmPackagePartSource) c11;
        KotlinJvmBinaryClass g6 = jvmPackagePartSource2.g();
        return g6 == null ? q.b(this.f12110a, jvmPackagePartSource2.d()) : g6;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public final boolean u(ClassId classId) {
        KotlinJvmBinaryClass b10;
        za.k.e(classId, "classId");
        return classId.g() != null && za.k.a(classId.j().b(), "Container") && (b10 = q.b(this.f12110a, classId)) != null && SpecialJvmAnnotations.f14656a.c(b10);
    }

    protected abstract KotlinJvmBinaryClass.a v(ClassId classId, SourceElement sourceElement, List<A> list);

    /* JADX INFO: Access modifiers changed from: protected */
    public final KotlinJvmBinaryClass.a w(ClassId classId, SourceElement sourceElement, List<A> list) {
        za.k.e(classId, "annotationClassId");
        za.k.e(sourceElement, "source");
        za.k.e(list, "result");
        if (SpecialJvmAnnotations.f14656a.b().contains(classId)) {
            return null;
        }
        return v(classId, sourceElement, list);
    }

    protected abstract A y(jc.b bVar, NameResolver nameResolver);
}
