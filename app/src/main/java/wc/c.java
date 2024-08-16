package wc;

import com.oplus.thermalcontrol.config.ThermalBaseConfig;
import gb.KDeclarationContainer;
import gd.g0;
import gd.o0;
import hd.g;
import hd.h;
import hd.p;
import hd.x;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import kotlin.collections.CollectionsJVM;
import kotlin.collections.r;
import kotlin.collections.s;
import mb.KotlinBuiltIns;
import oc.ClassId;
import oc.FqName;
import oc.FqNameUnsafe;
import oc.Name;
import pb.CallableMemberDescriptor;
import pb.ClassDescriptor;
import pb.ClassifierDescriptor;
import pb.ClassifierDescriptorWithTypeParameters;
import pb.DeclarationDescriptor;
import pb.InlineClassRepresentation;
import pb.ModuleDescriptor;
import pb.PackageFragmentDescriptor;
import pb.PropertyAccessorDescriptor;
import pb.PropertyDescriptor;
import pb.ValueClassRepresentation;
import pb.ValueParameterDescriptor;
import qb.AnnotationDescriptor;
import qd.DFS;
import rd.Sequence;
import rd._Sequences;
import sc.e;
import ya.l;
import za.FunctionReference;
import za.Lambda;
import za.Reflection;
import za.k;
import za.y;

/* compiled from: DescriptorUtils.kt */
/* loaded from: classes2.dex */
public final class c {

    /* renamed from: a, reason: collision with root package name */
    private static final Name f19436a;

    /* compiled from: DescriptorUtils.kt */
    /* loaded from: classes2.dex */
    /* synthetic */ class a extends FunctionReference implements l<ValueParameterDescriptor, Boolean> {

        /* renamed from: n, reason: collision with root package name */
        public static final a f19437n = new a();

        a() {
            super(1);
        }

        @Override // za.CallableReference
        public final KDeclarationContainer C() {
            return Reflection.b(ValueParameterDescriptor.class);
        }

        @Override // za.CallableReference
        public final String E() {
            return "declaresDefaultValue()Z";
        }

        @Override // ya.l
        /* renamed from: G, reason: merged with bridge method [inline-methods] */
        public final Boolean invoke(ValueParameterDescriptor valueParameterDescriptor) {
            k.e(valueParameterDescriptor, "p0");
            return Boolean.valueOf(valueParameterDescriptor.z0());
        }

        @Override // za.CallableReference, gb.KCallable
        public final String getName() {
            return "declaresDefaultValue";
        }
    }

    /* compiled from: DescriptorUtils.kt */
    /* loaded from: classes2.dex */
    public static final class b extends DFS.b<CallableMemberDescriptor, CallableMemberDescriptor> {

        /* renamed from: a, reason: collision with root package name */
        final /* synthetic */ y<CallableMemberDescriptor> f19438a;

        /* renamed from: b, reason: collision with root package name */
        final /* synthetic */ l<CallableMemberDescriptor, Boolean> f19439b;

        /* JADX WARN: Multi-variable type inference failed */
        b(y<CallableMemberDescriptor> yVar, l<? super CallableMemberDescriptor, Boolean> lVar) {
            this.f19438a = yVar;
            this.f19439b = lVar;
        }

        /* JADX WARN: Multi-variable type inference failed */
        @Override // qd.DFS.b, qd.DFS.d
        /* renamed from: d, reason: merged with bridge method [inline-methods] */
        public void b(CallableMemberDescriptor callableMemberDescriptor) {
            k.e(callableMemberDescriptor, "current");
            if (this.f19438a.f20376e == null && this.f19439b.invoke(callableMemberDescriptor).booleanValue()) {
                this.f19438a.f20376e = callableMemberDescriptor;
            }
        }

        @Override // qd.DFS.d
        /* renamed from: e, reason: merged with bridge method [inline-methods] */
        public boolean c(CallableMemberDescriptor callableMemberDescriptor) {
            k.e(callableMemberDescriptor, "current");
            return this.f19438a.f20376e == null;
        }

        @Override // qd.DFS.d
        /* renamed from: f, reason: merged with bridge method [inline-methods] */
        public CallableMemberDescriptor a() {
            return this.f19438a.f20376e;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: DescriptorUtils.kt */
    /* renamed from: wc.c$c, reason: collision with other inner class name */
    /* loaded from: classes2.dex */
    public static final class C0114c extends Lambda implements l<DeclarationDescriptor, DeclarationDescriptor> {

        /* renamed from: e, reason: collision with root package name */
        public static final C0114c f19440e = new C0114c();

        C0114c() {
            super(1);
        }

        @Override // ya.l
        /* renamed from: a, reason: merged with bridge method [inline-methods] */
        public final DeclarationDescriptor invoke(DeclarationDescriptor declarationDescriptor) {
            k.e(declarationDescriptor, "it");
            return declarationDescriptor.b();
        }
    }

    static {
        Name f10 = Name.f(ThermalBaseConfig.Item.ATTR_VALUE);
        k.d(f10, "identifier(\"value\")");
        f19436a = f10;
    }

    public static final boolean c(ValueParameterDescriptor valueParameterDescriptor) {
        List e10;
        k.e(valueParameterDescriptor, "<this>");
        e10 = CollectionsJVM.e(valueParameterDescriptor);
        Boolean e11 = DFS.e(e10, wc.a.f19434a, a.f19437n);
        k.d(e11, "ifAny(\n        listOf(th…eclaresDefaultValue\n    )");
        return e11.booleanValue();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final Iterable d(ValueParameterDescriptor valueParameterDescriptor) {
        int u7;
        Collection<ValueParameterDescriptor> e10 = valueParameterDescriptor.e();
        u7 = s.u(e10, 10);
        ArrayList arrayList = new ArrayList(u7);
        Iterator<T> it = e10.iterator();
        while (it.hasNext()) {
            arrayList.add(((ValueParameterDescriptor) it.next()).a());
        }
        return arrayList;
    }

    public static final CallableMemberDescriptor e(CallableMemberDescriptor callableMemberDescriptor, boolean z10, l<? super CallableMemberDescriptor, Boolean> lVar) {
        List e10;
        k.e(callableMemberDescriptor, "<this>");
        k.e(lVar, "predicate");
        y yVar = new y();
        e10 = CollectionsJVM.e(callableMemberDescriptor);
        return (CallableMemberDescriptor) DFS.b(e10, new wc.b(z10), new b(yVar, lVar));
    }

    public static /* synthetic */ CallableMemberDescriptor f(CallableMemberDescriptor callableMemberDescriptor, boolean z10, l lVar, int i10, Object obj) {
        if ((i10 & 1) != 0) {
            z10 = false;
        }
        return e(callableMemberDescriptor, z10, lVar);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final Iterable g(boolean z10, CallableMemberDescriptor callableMemberDescriptor) {
        List j10;
        if (z10) {
            callableMemberDescriptor = callableMemberDescriptor != null ? callableMemberDescriptor.a() : null;
        }
        Collection<? extends CallableMemberDescriptor> e10 = callableMemberDescriptor != null ? callableMemberDescriptor.e() : null;
        if (e10 != null) {
            return e10;
        }
        j10 = r.j();
        return j10;
    }

    public static final FqName h(DeclarationDescriptor declarationDescriptor) {
        k.e(declarationDescriptor, "<this>");
        FqNameUnsafe m10 = m(declarationDescriptor);
        if (!m10.f()) {
            m10 = null;
        }
        if (m10 != null) {
            return m10.l();
        }
        return null;
    }

    public static final ClassDescriptor i(AnnotationDescriptor annotationDescriptor) {
        k.e(annotationDescriptor, "<this>");
        ClassifierDescriptor v7 = annotationDescriptor.getType().W0().v();
        if (v7 instanceof ClassDescriptor) {
            return (ClassDescriptor) v7;
        }
        return null;
    }

    public static final KotlinBuiltIns j(DeclarationDescriptor declarationDescriptor) {
        k.e(declarationDescriptor, "<this>");
        return p(declarationDescriptor).t();
    }

    public static final ClassId k(ClassifierDescriptor classifierDescriptor) {
        DeclarationDescriptor b10;
        ClassId k10;
        if (classifierDescriptor == null || (b10 = classifierDescriptor.b()) == null) {
            return null;
        }
        if (b10 instanceof PackageFragmentDescriptor) {
            return new ClassId(((PackageFragmentDescriptor) b10).d(), classifierDescriptor.getName());
        }
        if (!(b10 instanceof ClassifierDescriptorWithTypeParameters) || (k10 = k((ClassifierDescriptor) b10)) == null) {
            return null;
        }
        return k10.d(classifierDescriptor.getName());
    }

    public static final FqName l(DeclarationDescriptor declarationDescriptor) {
        k.e(declarationDescriptor, "<this>");
        FqName n10 = e.n(declarationDescriptor);
        k.d(n10, "getFqNameSafe(this)");
        return n10;
    }

    public static final FqNameUnsafe m(DeclarationDescriptor declarationDescriptor) {
        k.e(declarationDescriptor, "<this>");
        FqNameUnsafe m10 = e.m(declarationDescriptor);
        k.d(m10, "getFqName(this)");
        return m10;
    }

    public static final InlineClassRepresentation<o0> n(ClassDescriptor classDescriptor) {
        ValueClassRepresentation<o0> G0 = classDescriptor != null ? classDescriptor.G0() : null;
        if (G0 instanceof InlineClassRepresentation) {
            return (InlineClassRepresentation) G0;
        }
        return null;
    }

    public static final g o(ModuleDescriptor moduleDescriptor) {
        k.e(moduleDescriptor, "<this>");
        p pVar = (p) moduleDescriptor.k0(h.a());
        x xVar = pVar != null ? (x) pVar.a() : null;
        return xVar instanceof x.a ? ((x.a) xVar).b() : g.a.f12215a;
    }

    public static final ModuleDescriptor p(DeclarationDescriptor declarationDescriptor) {
        k.e(declarationDescriptor, "<this>");
        ModuleDescriptor g6 = e.g(declarationDescriptor);
        k.d(g6, "getContainingModule(this)");
        return g6;
    }

    public static final Sequence<DeclarationDescriptor> q(DeclarationDescriptor declarationDescriptor) {
        Sequence<DeclarationDescriptor> l10;
        k.e(declarationDescriptor, "<this>");
        l10 = _Sequences.l(r(declarationDescriptor), 1);
        return l10;
    }

    public static final Sequence<DeclarationDescriptor> r(DeclarationDescriptor declarationDescriptor) {
        Sequence<DeclarationDescriptor> f10;
        k.e(declarationDescriptor, "<this>");
        f10 = rd.l.f(declarationDescriptor, C0114c.f19440e);
        return f10;
    }

    public static final CallableMemberDescriptor s(CallableMemberDescriptor callableMemberDescriptor) {
        k.e(callableMemberDescriptor, "<this>");
        if (!(callableMemberDescriptor instanceof PropertyAccessorDescriptor)) {
            return callableMemberDescriptor;
        }
        PropertyDescriptor K0 = ((PropertyAccessorDescriptor) callableMemberDescriptor).K0();
        k.d(K0, "correspondingProperty");
        return K0;
    }

    public static final ClassDescriptor t(ClassDescriptor classDescriptor) {
        k.e(classDescriptor, "<this>");
        for (g0 g0Var : classDescriptor.x().W0().q()) {
            if (!KotlinBuiltIns.b0(g0Var)) {
                ClassifierDescriptor v7 = g0Var.W0().v();
                if (e.w(v7)) {
                    k.c(v7, "null cannot be cast to non-null type org.jetbrains.kotlin.descriptors.ClassDescriptor");
                    return (ClassDescriptor) v7;
                }
            }
        }
        return null;
    }

    public static final boolean u(ModuleDescriptor moduleDescriptor) {
        x xVar;
        k.e(moduleDescriptor, "<this>");
        p pVar = (p) moduleDescriptor.k0(h.a());
        return (pVar == null || (xVar = (x) pVar.a()) == null || !xVar.a()) ? false : true;
    }

    public static final ClassDescriptor v(ModuleDescriptor moduleDescriptor, FqName fqName, xb.b bVar) {
        k.e(moduleDescriptor, "<this>");
        k.e(fqName, "topLevelClassFqName");
        k.e(bVar, "location");
        fqName.d();
        FqName e10 = fqName.e();
        k.d(e10, "topLevelClassFqName.parent()");
        zc.h u7 = moduleDescriptor.H(e10).u();
        Name g6 = fqName.g();
        k.d(g6, "topLevelClassFqName.shortName()");
        ClassifierDescriptor e11 = u7.e(g6, bVar);
        if (e11 instanceof ClassDescriptor) {
            return (ClassDescriptor) e11;
        }
        return null;
    }
}
