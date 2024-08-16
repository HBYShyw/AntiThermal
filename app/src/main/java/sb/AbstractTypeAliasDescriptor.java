package sb;

import fd.StorageManager;
import gd.TypeConstructor;
import gd.o0;
import gd.s1;
import gd.v1;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import mb.KotlinBuiltIns;
import oc.Name;
import pb.ClassConstructorDescriptor;
import pb.ClassDescriptor;
import pb.ClassifierDescriptor;
import pb.DeclarationDescriptor;
import pb.DeclarationDescriptorVisitor;
import pb.DeclarationDescriptorWithSource;
import pb.SourceElement;
import pb.TypeAliasDescriptor;
import pb.TypeParameterDescriptor;
import sb.j0;
import za.Lambda;
import zc.h;

/* compiled from: AbstractTypeAliasDescriptor.kt */
/* renamed from: sb.d, reason: use source file name */
/* loaded from: classes2.dex */
public abstract class AbstractTypeAliasDescriptor extends DeclarationDescriptorNonRootImpl implements TypeAliasDescriptor {

    /* renamed from: i, reason: collision with root package name */
    private final pb.u f18249i;

    /* renamed from: j, reason: collision with root package name */
    private List<? extends TypeParameterDescriptor> f18250j;

    /* renamed from: k, reason: collision with root package name */
    private final c f18251k;

    /* compiled from: AbstractTypeAliasDescriptor.kt */
    /* renamed from: sb.d$a */
    /* loaded from: classes2.dex */
    static final class a extends Lambda implements ya.l<hd.g, o0> {
        a() {
            super(1);
        }

        @Override // ya.l
        /* renamed from: a, reason: merged with bridge method [inline-methods] */
        public final o0 invoke(hd.g gVar) {
            ClassifierDescriptor f10 = gVar.f(AbstractTypeAliasDescriptor.this);
            if (f10 != null) {
                return f10.x();
            }
            return null;
        }
    }

    /* compiled from: AbstractTypeAliasDescriptor.kt */
    /* renamed from: sb.d$b */
    /* loaded from: classes2.dex */
    static final class b extends Lambda implements ya.l<v1, Boolean> {
        b() {
            super(1);
        }

        /* JADX WARN: Code restructure failed: missing block: B:8:0x002a, code lost:
        
            if (((r4 instanceof pb.TypeParameterDescriptor) && !za.k.a(((pb.TypeParameterDescriptor) r4).b(), r3)) != false) goto L13;
         */
        @Override // ya.l
        /* renamed from: a, reason: merged with bridge method [inline-methods] */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        public final Boolean invoke(v1 v1Var) {
            za.k.d(v1Var, "type");
            boolean z10 = true;
            if (!gd.i0.a(v1Var)) {
                AbstractTypeAliasDescriptor abstractTypeAliasDescriptor = AbstractTypeAliasDescriptor.this;
                ClassifierDescriptor v7 = v1Var.W0().v();
            }
            z10 = false;
            return Boolean.valueOf(z10);
        }
    }

    /* compiled from: AbstractTypeAliasDescriptor.kt */
    /* renamed from: sb.d$c */
    /* loaded from: classes2.dex */
    public static final class c implements TypeConstructor {
        c() {
        }

        @Override // gd.TypeConstructor
        /* renamed from: c, reason: merged with bridge method [inline-methods] */
        public TypeAliasDescriptor v() {
            return AbstractTypeAliasDescriptor.this;
        }

        @Override // gd.TypeConstructor
        public List<TypeParameterDescriptor> getParameters() {
            return AbstractTypeAliasDescriptor.this.V0();
        }

        @Override // gd.TypeConstructor
        public Collection<gd.g0> q() {
            Collection<gd.g0> q10 = v().n0().W0().q();
            za.k.d(q10, "declarationDescriptor.un…pe.constructor.supertypes");
            return q10;
        }

        @Override // gd.TypeConstructor
        public KotlinBuiltIns t() {
            return wc.c.j(v());
        }

        public String toString() {
            return "[typealias " + v().getName().b() + ']';
        }

        @Override // gd.TypeConstructor
        public TypeConstructor u(hd.g gVar) {
            za.k.e(gVar, "kotlinTypeRefiner");
            return this;
        }

        @Override // gd.TypeConstructor
        public boolean w() {
            return true;
        }
    }

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public AbstractTypeAliasDescriptor(DeclarationDescriptor declarationDescriptor, qb.g gVar, Name name, SourceElement sourceElement, pb.u uVar) {
        super(declarationDescriptor, gVar, name, sourceElement);
        za.k.e(declarationDescriptor, "containingDeclaration");
        za.k.e(gVar, "annotations");
        za.k.e(name, "name");
        za.k.e(sourceElement, "sourceElement");
        za.k.e(uVar, "visibilityImpl");
        this.f18249i = uVar;
        this.f18251k = new c();
    }

    @Override // pb.ClassifierDescriptorWithTypeParameters
    public List<TypeParameterDescriptor> B() {
        List list = this.f18250j;
        if (list != null) {
            return list;
        }
        za.k.s("declaredTypeParametersImpl");
        return null;
    }

    @Override // pb.MemberDescriptor
    public boolean D() {
        return false;
    }

    @Override // pb.DeclarationDescriptor
    public <R, D> R H0(DeclarationDescriptorVisitor<R, D> declarationDescriptorVisitor, D d10) {
        za.k.e(declarationDescriptorVisitor, "visitor");
        return declarationDescriptorVisitor.i(this, d10);
    }

    @Override // pb.MemberDescriptor
    public boolean N0() {
        return false;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public final o0 O0() {
        zc.h hVar;
        ClassDescriptor w10 = w();
        if (w10 == null || (hVar = w10.M0()) == null) {
            hVar = h.b.f20465b;
        }
        o0 v7 = s1.v(this, hVar, new a());
        za.k.d(v7, "@OptIn(TypeRefinement::c…s)?.defaultType\n        }");
        return v7;
    }

    @Override // sb.DeclarationDescriptorNonRootImpl, sb.DeclarationDescriptorImpl, pb.DeclarationDescriptor
    public TypeAliasDescriptor T0() {
        DeclarationDescriptorWithSource T0 = super.T0();
        za.k.c(T0, "null cannot be cast to non-null type org.jetbrains.kotlin.descriptors.TypeAliasDescriptor");
        return (TypeAliasDescriptor) T0;
    }

    @Override // pb.MemberDescriptor
    public boolean U() {
        return false;
    }

    public final Collection<i0> U0() {
        List j10;
        ClassDescriptor w10 = w();
        if (w10 == null) {
            j10 = kotlin.collections.r.j();
            return j10;
        }
        Collection<ClassConstructorDescriptor> p10 = w10.p();
        za.k.d(p10, "classDescriptor.constructors");
        ArrayList arrayList = new ArrayList();
        for (ClassConstructorDescriptor classConstructorDescriptor : p10) {
            j0.a aVar = j0.M;
            StorageManager o02 = o0();
            za.k.d(classConstructorDescriptor, "it");
            i0 b10 = aVar.b(o02, this, classConstructorDescriptor);
            if (b10 != null) {
                arrayList.add(b10);
            }
        }
        return arrayList;
    }

    protected abstract List<TypeParameterDescriptor> V0();

    public final void W0(List<? extends TypeParameterDescriptor> list) {
        za.k.e(list, "declaredTypeParameters");
        this.f18250j = list;
    }

    @Override // pb.DeclarationDescriptorWithVisibility, pb.MemberDescriptor
    public pb.u g() {
        return this.f18249i;
    }

    @Override // pb.ClassifierDescriptor
    public TypeConstructor n() {
        return this.f18251k;
    }

    protected abstract StorageManager o0();

    @Override // pb.ClassifierDescriptorWithTypeParameters
    public boolean r() {
        return s1.c(n0(), new b());
    }

    @Override // sb.DeclarationDescriptorImpl
    public String toString() {
        return "typealias " + getName().b();
    }
}
