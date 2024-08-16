package pb;

import fb.PrimitiveRanges;
import fb._Ranges;
import fd.StorageManager;
import gd.ClassTypeConstructorImpl;
import gd.Variance;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import kotlin.collections.PrimitiveIterators;
import kotlin.collections.SetsJVM;
import kotlin.collections._Collections;
import oc.ClassId;
import oc.FqName;
import oc.Name;
import sb.ClassDescriptorBase;
import sb.EmptyPackageFragmentDesciptor;
import sb.TypeParameterDescriptorImpl;
import za.Lambda;
import zc.h;

/* compiled from: NotFoundClasses.kt */
/* renamed from: pb.k0, reason: use source file name */
/* loaded from: classes2.dex */
public final class NotFoundClasses {

    /* renamed from: a, reason: collision with root package name */
    private final StorageManager f16697a;

    /* renamed from: b, reason: collision with root package name */
    private final ModuleDescriptor f16698b;

    /* renamed from: c, reason: collision with root package name */
    private final fd.g<FqName, PackageFragmentDescriptor> f16699c;

    /* renamed from: d, reason: collision with root package name */
    private final fd.g<a, ClassDescriptor> f16700d;

    /* JADX INFO: Access modifiers changed from: private */
    /* compiled from: NotFoundClasses.kt */
    /* renamed from: pb.k0$a */
    /* loaded from: classes2.dex */
    public static final class a {

        /* renamed from: a, reason: collision with root package name */
        private final ClassId f16701a;

        /* renamed from: b, reason: collision with root package name */
        private final List<Integer> f16702b;

        public a(ClassId classId, List<Integer> list) {
            za.k.e(classId, "classId");
            za.k.e(list, "typeParametersCount");
            this.f16701a = classId;
            this.f16702b = list;
        }

        public final ClassId a() {
            return this.f16701a;
        }

        public final List<Integer> b() {
            return this.f16702b;
        }

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (!(obj instanceof a)) {
                return false;
            }
            a aVar = (a) obj;
            return za.k.a(this.f16701a, aVar.f16701a) && za.k.a(this.f16702b, aVar.f16702b);
        }

        public int hashCode() {
            return (this.f16701a.hashCode() * 31) + this.f16702b.hashCode();
        }

        public String toString() {
            return "ClassRequest(classId=" + this.f16701a + ", typeParametersCount=" + this.f16702b + ')';
        }
    }

    /* compiled from: NotFoundClasses.kt */
    /* renamed from: pb.k0$b */
    /* loaded from: classes2.dex */
    public static final class b extends ClassDescriptorBase {

        /* renamed from: m, reason: collision with root package name */
        private final boolean f16703m;

        /* renamed from: n, reason: collision with root package name */
        private final List<TypeParameterDescriptor> f16704n;

        /* renamed from: o, reason: collision with root package name */
        private final ClassTypeConstructorImpl f16705o;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        public b(StorageManager storageManager, DeclarationDescriptor declarationDescriptor, Name name, boolean z10, int i10) {
            super(storageManager, declarationDescriptor, name, SourceElement.f16664a, false);
            PrimitiveRanges k10;
            int u7;
            Set d10;
            za.k.e(storageManager, "storageManager");
            za.k.e(declarationDescriptor, "container");
            za.k.e(name, "name");
            this.f16703m = z10;
            k10 = _Ranges.k(0, i10);
            u7 = kotlin.collections.s.u(k10, 10);
            ArrayList arrayList = new ArrayList(u7);
            Iterator<Integer> it = k10.iterator();
            while (it.hasNext()) {
                int b10 = ((PrimitiveIterators) it).b();
                qb.g b11 = qb.g.f17195b.b();
                Variance variance = Variance.INVARIANT;
                StringBuilder sb2 = new StringBuilder();
                sb2.append('T');
                sb2.append(b10);
                arrayList.add(TypeParameterDescriptorImpl.a1(this, b11, false, variance, Name.f(sb2.toString()), b10, storageManager));
            }
            this.f16704n = arrayList;
            List<TypeParameterDescriptor> d11 = g1.d(this);
            d10 = SetsJVM.d(wc.c.p(this).t().i());
            this.f16705o = new ClassTypeConstructorImpl(this, d11, d10, storageManager);
        }

        @Override // pb.ClassDescriptor, pb.ClassifierDescriptorWithTypeParameters
        public List<TypeParameterDescriptor> B() {
            return this.f16704n;
        }

        @Override // sb.ClassDescriptorBase, pb.MemberDescriptor
        public boolean D() {
            return false;
        }

        @Override // pb.ClassDescriptor
        public boolean F() {
            return false;
        }

        @Override // pb.ClassDescriptor
        public ValueClassRepresentation<gd.o0> G0() {
            return null;
        }

        @Override // pb.ClassDescriptor
        public boolean L() {
            return false;
        }

        @Override // pb.MemberDescriptor
        public boolean N0() {
            return false;
        }

        @Override // pb.ClassDescriptor
        public boolean R0() {
            return false;
        }

        @Override // pb.ClassDescriptor
        public Collection<ClassDescriptor> S() {
            List j10;
            j10 = kotlin.collections.r.j();
            return j10;
        }

        @Override // pb.ClassDescriptor
        /* renamed from: T0, reason: merged with bridge method [inline-methods] */
        public h.b a0() {
            return h.b.f20465b;
        }

        @Override // pb.MemberDescriptor
        public boolean U() {
            return false;
        }

        @Override // pb.ClassifierDescriptor
        /* renamed from: U0, reason: merged with bridge method [inline-methods] */
        public ClassTypeConstructorImpl n() {
            return this.f16705o;
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // sb.t
        /* renamed from: V0, reason: merged with bridge method [inline-methods] */
        public h.b Q(hd.g gVar) {
            za.k.e(gVar, "kotlinTypeRefiner");
            return h.b.f20465b;
        }

        @Override // pb.ClassDescriptor
        public ClassConstructorDescriptor Z() {
            return null;
        }

        @Override // pb.ClassDescriptor
        public ClassDescriptor c0() {
            return null;
        }

        @Override // pb.ClassDescriptor, pb.DeclarationDescriptorWithVisibility, pb.MemberDescriptor
        public u g() {
            u uVar = DescriptorVisibilities.f16733e;
            za.k.d(uVar, "PUBLIC");
            return uVar;
        }

        @Override // pb.ClassDescriptor
        public ClassKind getKind() {
            return ClassKind.CLASS;
        }

        @Override // qb.a
        public qb.g i() {
            return qb.g.f17195b.b();
        }

        @Override // pb.ClassDescriptor, pb.MemberDescriptor
        public Modality o() {
            return Modality.FINAL;
        }

        @Override // pb.ClassDescriptor
        public Collection<ClassConstructorDescriptor> p() {
            Set e10;
            e10 = kotlin.collections.s0.e();
            return e10;
        }

        @Override // pb.ClassDescriptor
        public boolean q() {
            return false;
        }

        @Override // pb.ClassifierDescriptorWithTypeParameters
        public boolean r() {
            return this.f16703m;
        }

        public String toString() {
            return "class " + getName() + " (not found)";
        }

        @Override // pb.ClassDescriptor
        public boolean y() {
            return false;
        }
    }

    /* compiled from: NotFoundClasses.kt */
    /* renamed from: pb.k0$c */
    /* loaded from: classes2.dex */
    static final class c extends Lambda implements ya.l<a, ClassDescriptor> {
        c() {
            super(1);
        }

        /* JADX WARN: Code restructure failed: missing block: B:6:0x0024, code lost:
        
            if (r1 != null) goto L10;
         */
        @Override // ya.l
        /* renamed from: a, reason: merged with bridge method [inline-methods] */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        public final ClassDescriptor invoke(a aVar) {
            DeclarationDescriptor declarationDescriptor;
            Object V;
            List<Integer> N;
            za.k.e(aVar, "<name for destructuring parameter 0>");
            ClassId a10 = aVar.a();
            List<Integer> b10 = aVar.b();
            if (!a10.k()) {
                ClassId g6 = a10.g();
                if (g6 != null) {
                    NotFoundClasses notFoundClasses = NotFoundClasses.this;
                    N = _Collections.N(b10, 1);
                    declarationDescriptor = notFoundClasses.d(g6, N);
                }
                fd.g gVar = NotFoundClasses.this.f16699c;
                FqName h10 = a10.h();
                za.k.d(h10, "classId.packageFqName");
                declarationDescriptor = (ClassOrPackageFragmentDescriptor) gVar.invoke(h10);
                DeclarationDescriptor declarationDescriptor2 = declarationDescriptor;
                boolean l10 = a10.l();
                StorageManager storageManager = NotFoundClasses.this.f16697a;
                Name j10 = a10.j();
                za.k.d(j10, "classId.shortClassName");
                V = _Collections.V(b10);
                Integer num = (Integer) V;
                return new b(storageManager, declarationDescriptor2, j10, l10, num != null ? num.intValue() : 0);
            }
            throw new UnsupportedOperationException("Unresolved local class: " + a10);
        }
    }

    /* compiled from: NotFoundClasses.kt */
    /* renamed from: pb.k0$d */
    /* loaded from: classes2.dex */
    static final class d extends Lambda implements ya.l<FqName, PackageFragmentDescriptor> {
        d() {
            super(1);
        }

        @Override // ya.l
        /* renamed from: a, reason: merged with bridge method [inline-methods] */
        public final PackageFragmentDescriptor invoke(FqName fqName) {
            za.k.e(fqName, "fqName");
            return new EmptyPackageFragmentDesciptor(NotFoundClasses.this.f16698b, fqName);
        }
    }

    public NotFoundClasses(StorageManager storageManager, ModuleDescriptor moduleDescriptor) {
        za.k.e(storageManager, "storageManager");
        za.k.e(moduleDescriptor, "module");
        this.f16697a = storageManager;
        this.f16698b = moduleDescriptor;
        this.f16699c = storageManager.d(new d());
        this.f16700d = storageManager.d(new c());
    }

    public final ClassDescriptor d(ClassId classId, List<Integer> list) {
        za.k.e(classId, "classId");
        za.k.e(list, "typeParametersCount");
        return this.f16700d.invoke(new a(classId, list));
    }
}
