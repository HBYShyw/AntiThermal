package cc;

import ac.PossiblyExternalAnnotationDescriptor;
import fc.x;
import gd.TypeUsage;
import gd.Variance;
import gd.g0;
import gd.i0;
import gd.o0;
import id.ErrorTypeKind;
import id.ErrorUtils;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import kotlin.collections.m0;
import kotlin.collections.s;
import ma.o;
import ma.u;
import ob.JavaToKotlinClassMapper;
import oc.ClassId;
import oc.FqName;
import oc.Name;
import pb.ClassDescriptor;
import pb.ModuleDescriptor;
import pb.ValueParameterDescriptor;
import pb.findClassInModule;
import qb.AnnotationDescriptor;
import uc.ConstantValueFactory;
import uc.q;
import yb.b0;
import za.DefaultConstructorMarker;
import za.Lambda;
import za.PropertyReference1Impl;
import za.Reflection;
import zb.DescriptorResolverUtils;

/* compiled from: LazyJavaAnnotationDescriptor.kt */
/* renamed from: cc.e, reason: use source file name */
/* loaded from: classes2.dex */
public final class LazyJavaAnnotationDescriptor implements AnnotationDescriptor, PossiblyExternalAnnotationDescriptor {

    /* renamed from: i, reason: collision with root package name */
    static final /* synthetic */ gb.l<Object>[] f5055i = {Reflection.g(new PropertyReference1Impl(Reflection.b(LazyJavaAnnotationDescriptor.class), "fqName", "getFqName()Lorg/jetbrains/kotlin/name/FqName;")), Reflection.g(new PropertyReference1Impl(Reflection.b(LazyJavaAnnotationDescriptor.class), "type", "getType()Lorg/jetbrains/kotlin/types/SimpleType;")), Reflection.g(new PropertyReference1Impl(Reflection.b(LazyJavaAnnotationDescriptor.class), "allValueArguments", "getAllValueArguments()Ljava/util/Map;"))};

    /* renamed from: a, reason: collision with root package name */
    private final bc.g f5056a;

    /* renamed from: b, reason: collision with root package name */
    private final fc.a f5057b;

    /* renamed from: c, reason: collision with root package name */
    private final fd.j f5058c;

    /* renamed from: d, reason: collision with root package name */
    private final fd.i f5059d;

    /* renamed from: e, reason: collision with root package name */
    private final ec.a f5060e;

    /* renamed from: f, reason: collision with root package name */
    private final fd.i f5061f;

    /* renamed from: g, reason: collision with root package name */
    private final boolean f5062g;

    /* renamed from: h, reason: collision with root package name */
    private final boolean f5063h;

    /* compiled from: LazyJavaAnnotationDescriptor.kt */
    /* renamed from: cc.e$a */
    /* loaded from: classes2.dex */
    static final class a extends Lambda implements ya.a<Map<Name, ? extends uc.g<?>>> {
        a() {
            super(0);
        }

        @Override // ya.a
        /* renamed from: a, reason: merged with bridge method [inline-methods] */
        public final Map<Name, uc.g<?>> invoke() {
            Map<Name, uc.g<?>> q10;
            Collection<fc.b> b10 = LazyJavaAnnotationDescriptor.this.f5057b.b();
            LazyJavaAnnotationDescriptor lazyJavaAnnotationDescriptor = LazyJavaAnnotationDescriptor.this;
            ArrayList arrayList = new ArrayList();
            for (fc.b bVar : b10) {
                Name name = bVar.getName();
                if (name == null) {
                    name = b0.f20020c;
                }
                uc.g l10 = lazyJavaAnnotationDescriptor.l(bVar);
                o a10 = l10 != null ? u.a(name, l10) : null;
                if (a10 != null) {
                    arrayList.add(a10);
                }
            }
            q10 = m0.q(arrayList);
            return q10;
        }
    }

    /* compiled from: LazyJavaAnnotationDescriptor.kt */
    /* renamed from: cc.e$b */
    /* loaded from: classes2.dex */
    static final class b extends Lambda implements ya.a<FqName> {
        b() {
            super(0);
        }

        @Override // ya.a
        /* renamed from: a, reason: merged with bridge method [inline-methods] */
        public final FqName invoke() {
            ClassId e10 = LazyJavaAnnotationDescriptor.this.f5057b.e();
            if (e10 != null) {
                return e10.b();
            }
            return null;
        }
    }

    /* compiled from: LazyJavaAnnotationDescriptor.kt */
    /* renamed from: cc.e$c */
    /* loaded from: classes2.dex */
    static final class c extends Lambda implements ya.a<o0> {
        c() {
            super(0);
        }

        @Override // ya.a
        /* renamed from: a, reason: merged with bridge method [inline-methods] */
        public final o0 invoke() {
            FqName d10 = LazyJavaAnnotationDescriptor.this.d();
            if (d10 == null) {
                return ErrorUtils.d(ErrorTypeKind.J0, LazyJavaAnnotationDescriptor.this.f5057b.toString());
            }
            ClassDescriptor f10 = JavaToKotlinClassMapper.f(JavaToKotlinClassMapper.f16359a, d10, LazyJavaAnnotationDescriptor.this.f5056a.d().t(), null, 4, null);
            if (f10 == null) {
                fc.g G = LazyJavaAnnotationDescriptor.this.f5057b.G();
                f10 = G != null ? LazyJavaAnnotationDescriptor.this.f5056a.a().n().a(G) : null;
                if (f10 == null) {
                    f10 = LazyJavaAnnotationDescriptor.this.g(d10);
                }
            }
            return f10.x();
        }
    }

    public LazyJavaAnnotationDescriptor(bc.g gVar, fc.a aVar, boolean z10) {
        za.k.e(gVar, "c");
        za.k.e(aVar, "javaAnnotation");
        this.f5056a = gVar;
        this.f5057b = aVar;
        this.f5058c = gVar.e().f(new b());
        this.f5059d = gVar.e().g(new c());
        this.f5060e = gVar.a().t().a(aVar);
        this.f5061f = gVar.e().g(new a());
        this.f5062g = aVar.h();
        this.f5063h = aVar.C() || z10;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final ClassDescriptor g(FqName fqName) {
        ModuleDescriptor d10 = this.f5056a.d();
        ClassId m10 = ClassId.m(fqName);
        za.k.d(m10, "topLevel(fqName)");
        return findClassInModule.c(d10, m10, this.f5056a.a().b().d().q());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final uc.g<?> l(fc.b bVar) {
        if (bVar instanceof fc.o) {
            return ConstantValueFactory.f18991a.c(((fc.o) bVar).getValue());
        }
        if (bVar instanceof fc.m) {
            fc.m mVar = (fc.m) bVar;
            return o(mVar.b(), mVar.d());
        }
        if (!(bVar instanceof fc.e)) {
            if (bVar instanceof fc.c) {
                return m(((fc.c) bVar).a());
            }
            if (bVar instanceof fc.h) {
                return p(((fc.h) bVar).c());
            }
            return null;
        }
        fc.e eVar = (fc.e) bVar;
        Name name = eVar.getName();
        if (name == null) {
            name = b0.f20020c;
        }
        za.k.d(name, "argument.name ?: DEFAULT_ANNOTATION_MEMBER_NAME");
        return n(name, eVar.e());
    }

    private final uc.g<?> m(fc.a aVar) {
        return new uc.a(new LazyJavaAnnotationDescriptor(this.f5056a, aVar, false, 4, null));
    }

    private final uc.g<?> n(Name name, List<? extends fc.b> list) {
        g0 l10;
        int u7;
        o0 type = getType();
        za.k.d(type, "type");
        if (i0.a(type)) {
            return null;
        }
        ClassDescriptor i10 = wc.c.i(this);
        za.k.b(i10);
        ValueParameterDescriptor b10 = DescriptorResolverUtils.b(name, i10);
        if (b10 == null || (l10 = b10.getType()) == null) {
            l10 = this.f5056a.a().m().t().l(Variance.INVARIANT, ErrorUtils.d(ErrorTypeKind.I0, new String[0]));
        }
        za.k.d(l10, "DescriptorResolverUtils.…GUMENT)\n                )");
        u7 = s.u(list, 10);
        ArrayList arrayList = new ArrayList(u7);
        Iterator<T> it = list.iterator();
        while (it.hasNext()) {
            uc.g<?> l11 = l((fc.b) it.next());
            if (l11 == null) {
                l11 = new uc.s();
            }
            arrayList.add(l11);
        }
        return ConstantValueFactory.f18991a.a(arrayList, l10);
    }

    private final uc.g<?> o(ClassId classId, Name name) {
        if (classId == null || name == null) {
            return null;
        }
        return new uc.j(classId, name);
    }

    private final uc.g<?> p(x xVar) {
        return q.f19013b.a(this.f5056a.g().o(xVar, dc.b.b(TypeUsage.COMMON, false, false, null, 7, null)));
    }

    @Override // qb.AnnotationDescriptor
    public Map<Name, uc.g<?>> a() {
        return (Map) fd.m.a(this.f5061f, this, f5055i[2]);
    }

    @Override // qb.AnnotationDescriptor
    public FqName d() {
        return (FqName) fd.m.b(this.f5058c, this, f5055i[0]);
    }

    @Override // ac.PossiblyExternalAnnotationDescriptor
    public boolean h() {
        return this.f5062g;
    }

    @Override // qb.AnnotationDescriptor
    /* renamed from: i, reason: merged with bridge method [inline-methods] */
    public ec.a z() {
        return this.f5060e;
    }

    @Override // qb.AnnotationDescriptor
    /* renamed from: j, reason: merged with bridge method [inline-methods] */
    public o0 getType() {
        return (o0) fd.m.a(this.f5059d, this, f5055i[1]);
    }

    public final boolean k() {
        return this.f5063h;
    }

    public String toString() {
        return rc.c.s(rc.c.f17708g, this, null, 2, null);
    }

    public /* synthetic */ LazyJavaAnnotationDescriptor(bc.g gVar, fc.a aVar, boolean z10, int i10, DefaultConstructorMarker defaultConstructorMarker) {
        this(gVar, aVar, (i10 & 4) != 0 ? false : z10);
    }
}
