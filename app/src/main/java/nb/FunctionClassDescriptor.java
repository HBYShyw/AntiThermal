package nb;

import fb.PrimitiveRanges;
import fd.StorageManager;
import gd.AbstractClassTypeConstructor;
import gd.TypeConstructor;
import gd.TypeProjectionImpl;
import gd.Variance;
import gd.c1;
import gd.g0;
import gd.h0;
import gd.o0;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import kotlin.collections.CollectionsJVM;
import kotlin.collections.PrimitiveIterators;
import kotlin.collections._Collections;
import kotlin.collections.r;
import kotlin.collections.s;
import ma.NoWhenBranchMatchedException;
import ma.Unit;
import mb.StandardNames;
import oc.ClassId;
import oc.Name;
import pb.ClassConstructorDescriptor;
import pb.ClassDescriptor;
import pb.ClassKind;
import pb.DescriptorVisibilities;
import pb.Modality;
import pb.ModuleDescriptor;
import pb.PackageFragmentDescriptor;
import pb.SourceElement;
import pb.SupertypeLoopChecker;
import pb.TypeParameterDescriptor;
import pb.ValueClassRepresentation;
import pb.findClassInModule;
import pb.u;
import qb.g;
import sb.AbstractClassDescriptor;
import sb.TypeParameterDescriptorImpl;
import za.DefaultConstructorMarker;
import za.k;
import zc.h;

/* compiled from: FunctionClassDescriptor.kt */
/* renamed from: nb.b, reason: use source file name */
/* loaded from: classes2.dex */
public final class FunctionClassDescriptor extends AbstractClassDescriptor {

    /* renamed from: q, reason: collision with root package name */
    public static final a f15956q = new a(null);

    /* renamed from: r, reason: collision with root package name */
    private static final ClassId f15957r = new ClassId(StandardNames.f15283u, Name.f("Function"));

    /* renamed from: s, reason: collision with root package name */
    private static final ClassId f15958s = new ClassId(StandardNames.f15280r, Name.f("KFunction"));

    /* renamed from: j, reason: collision with root package name */
    private final StorageManager f15959j;

    /* renamed from: k, reason: collision with root package name */
    private final PackageFragmentDescriptor f15960k;

    /* renamed from: l, reason: collision with root package name */
    private final FunctionClassKind f15961l;

    /* renamed from: m, reason: collision with root package name */
    private final int f15962m;

    /* renamed from: n, reason: collision with root package name */
    private final b f15963n;

    /* renamed from: o, reason: collision with root package name */
    private final FunctionClassScope f15964o;

    /* renamed from: p, reason: collision with root package name */
    private final List<TypeParameterDescriptor> f15965p;

    /* compiled from: FunctionClassDescriptor.kt */
    /* renamed from: nb.b$a */
    /* loaded from: classes2.dex */
    public static final class a {
        private a() {
        }

        public /* synthetic */ a(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }
    }

    /* compiled from: FunctionClassDescriptor.kt */
    /* renamed from: nb.b$b */
    /* loaded from: classes2.dex */
    private final class b extends AbstractClassTypeConstructor {

        /* compiled from: FunctionClassDescriptor.kt */
        /* renamed from: nb.b$b$a */
        /* loaded from: classes2.dex */
        public /* synthetic */ class a {

            /* renamed from: a, reason: collision with root package name */
            public static final /* synthetic */ int[] f15967a;

            static {
                int[] iArr = new int[FunctionClassKind.values().length];
                try {
                    iArr[FunctionClassKind.f15969j.ordinal()] = 1;
                } catch (NoSuchFieldError unused) {
                }
                try {
                    iArr[FunctionClassKind.f15971l.ordinal()] = 2;
                } catch (NoSuchFieldError unused2) {
                }
                try {
                    iArr[FunctionClassKind.f15970k.ordinal()] = 3;
                } catch (NoSuchFieldError unused3) {
                }
                try {
                    iArr[FunctionClassKind.f15972m.ordinal()] = 4;
                } catch (NoSuchFieldError unused4) {
                }
                f15967a = iArr;
            }
        }

        public b() {
            super(FunctionClassDescriptor.this.f15959j);
        }

        @Override // gd.TypeConstructor
        public List<TypeParameterDescriptor> getParameters() {
            return FunctionClassDescriptor.this.f15965p;
        }

        @Override // gd.AbstractTypeConstructor
        protected Collection<g0> h() {
            List<ClassId> e10;
            int u7;
            List z02;
            List w02;
            int u10;
            int i10 = a.f15967a[FunctionClassDescriptor.this.d1().ordinal()];
            if (i10 == 1) {
                e10 = CollectionsJVM.e(FunctionClassDescriptor.f15957r);
            } else if (i10 == 2) {
                e10 = r.m(FunctionClassDescriptor.f15958s, new ClassId(StandardNames.f15283u, FunctionClassKind.f15969j.d(FunctionClassDescriptor.this.Z0())));
            } else if (i10 == 3) {
                e10 = CollectionsJVM.e(FunctionClassDescriptor.f15957r);
            } else {
                if (i10 != 4) {
                    throw new NoWhenBranchMatchedException();
                }
                e10 = r.m(FunctionClassDescriptor.f15958s, new ClassId(StandardNames.f15275m, FunctionClassKind.f15970k.d(FunctionClassDescriptor.this.Z0())));
            }
            ModuleDescriptor b10 = FunctionClassDescriptor.this.f15960k.b();
            u7 = s.u(e10, 10);
            ArrayList arrayList = new ArrayList(u7);
            for (ClassId classId : e10) {
                ClassDescriptor a10 = findClassInModule.a(b10, classId);
                if (a10 != null) {
                    w02 = _Collections.w0(getParameters(), a10.n().getParameters().size());
                    u10 = s.u(w02, 10);
                    ArrayList arrayList2 = new ArrayList(u10);
                    Iterator it = w02.iterator();
                    while (it.hasNext()) {
                        arrayList2.add(new TypeProjectionImpl(((TypeParameterDescriptor) it.next()).x()));
                    }
                    arrayList.add(h0.g(c1.f11749f.h(), a10, arrayList2));
                } else {
                    throw new IllegalStateException(("Built-in class " + classId + " not found").toString());
                }
            }
            z02 = _Collections.z0(arrayList);
            return z02;
        }

        @Override // gd.AbstractTypeConstructor
        protected SupertypeLoopChecker l() {
            return SupertypeLoopChecker.a.f16675a;
        }

        public String toString() {
            return v().toString();
        }

        @Override // gd.TypeConstructor
        public boolean w() {
            return true;
        }

        @Override // gd.ClassifierBasedTypeConstructor, gd.TypeConstructor
        /* renamed from: x, reason: merged with bridge method [inline-methods] */
        public FunctionClassDescriptor v() {
            return FunctionClassDescriptor.this;
        }
    }

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public FunctionClassDescriptor(StorageManager storageManager, PackageFragmentDescriptor packageFragmentDescriptor, FunctionClassKind functionClassKind, int i10) {
        super(storageManager, functionClassKind.d(i10));
        int u7;
        List<TypeParameterDescriptor> z02;
        k.e(storageManager, "storageManager");
        k.e(packageFragmentDescriptor, "containingDeclaration");
        k.e(functionClassKind, "functionKind");
        this.f15959j = storageManager;
        this.f15960k = packageFragmentDescriptor;
        this.f15961l = functionClassKind;
        this.f15962m = i10;
        this.f15963n = new b();
        this.f15964o = new FunctionClassScope(storageManager, this);
        ArrayList arrayList = new ArrayList();
        PrimitiveRanges primitiveRanges = new PrimitiveRanges(1, i10);
        u7 = s.u(primitiveRanges, 10);
        ArrayList arrayList2 = new ArrayList(u7);
        Iterator<Integer> it = primitiveRanges.iterator();
        while (it.hasNext()) {
            int b10 = ((PrimitiveIterators) it).b();
            Variance variance = Variance.IN_VARIANCE;
            StringBuilder sb2 = new StringBuilder();
            sb2.append('P');
            sb2.append(b10);
            T0(arrayList, this, variance, sb2.toString());
            arrayList2.add(Unit.f15173a);
        }
        T0(arrayList, this, Variance.OUT_VARIANCE, "R");
        z02 = _Collections.z0(arrayList);
        this.f15965p = z02;
    }

    private static final void T0(ArrayList<TypeParameterDescriptor> arrayList, FunctionClassDescriptor functionClassDescriptor, Variance variance, String str) {
        arrayList.add(TypeParameterDescriptorImpl.a1(functionClassDescriptor, g.f17195b.b(), false, variance, Name.f(str), arrayList.size(), functionClassDescriptor.f15959j));
    }

    @Override // pb.ClassDescriptor, pb.ClassifierDescriptorWithTypeParameters
    public List<TypeParameterDescriptor> B() {
        return this.f15965p;
    }

    @Override // pb.MemberDescriptor
    public boolean D() {
        return false;
    }

    @Override // pb.ClassDescriptor
    public boolean F() {
        return false;
    }

    @Override // pb.ClassDescriptor
    public ValueClassRepresentation<o0> G0() {
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

    @Override // pb.MemberDescriptor
    public boolean U() {
        return false;
    }

    @Override // pb.ClassDescriptor
    public /* bridge */ /* synthetic */ ClassConstructorDescriptor Z() {
        return (ClassConstructorDescriptor) h1();
    }

    public final int Z0() {
        return this.f15962m;
    }

    public Void a1() {
        return null;
    }

    @Override // pb.ClassDescriptor
    /* renamed from: b1, reason: merged with bridge method [inline-methods] */
    public List<ClassConstructorDescriptor> p() {
        List<ClassConstructorDescriptor> j10;
        j10 = r.j();
        return j10;
    }

    @Override // pb.ClassDescriptor
    public /* bridge */ /* synthetic */ ClassDescriptor c0() {
        return (ClassDescriptor) a1();
    }

    @Override // pb.ClassDescriptor, pb.DeclarationDescriptorNonRoot, pb.DeclarationDescriptor
    /* renamed from: c1, reason: merged with bridge method [inline-methods] */
    public PackageFragmentDescriptor b() {
        return this.f15960k;
    }

    public final FunctionClassKind d1() {
        return this.f15961l;
    }

    @Override // pb.ClassDescriptor
    /* renamed from: e1, reason: merged with bridge method [inline-methods] */
    public List<ClassDescriptor> S() {
        List<ClassDescriptor> j10;
        j10 = r.j();
        return j10;
    }

    @Override // pb.ClassDescriptor
    /* renamed from: f1, reason: merged with bridge method [inline-methods] */
    public h.b a0() {
        return h.b.f20465b;
    }

    @Override // pb.ClassDescriptor, pb.DeclarationDescriptorWithVisibility, pb.MemberDescriptor
    public u g() {
        u uVar = DescriptorVisibilities.f16733e;
        k.d(uVar, "PUBLIC");
        return uVar;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // sb.t
    /* renamed from: g1, reason: merged with bridge method [inline-methods] */
    public FunctionClassScope Q(hd.g gVar) {
        k.e(gVar, "kotlinTypeRefiner");
        return this.f15964o;
    }

    @Override // pb.ClassDescriptor
    public ClassKind getKind() {
        return ClassKind.INTERFACE;
    }

    public Void h1() {
        return null;
    }

    @Override // qb.a
    public g i() {
        return g.f17195b.b();
    }

    @Override // pb.ClassifierDescriptor
    public TypeConstructor n() {
        return this.f15963n;
    }

    @Override // pb.ClassDescriptor, pb.MemberDescriptor
    public Modality o() {
        return Modality.ABSTRACT;
    }

    @Override // pb.ClassDescriptor
    public boolean q() {
        return false;
    }

    @Override // pb.ClassifierDescriptorWithTypeParameters
    public boolean r() {
        return false;
    }

    public String toString() {
        String b10 = getName().b();
        k.d(b10, "name.asString()");
        return b10;
    }

    @Override // pb.ClassDescriptor
    public boolean y() {
        return false;
    }

    @Override // pb.DeclarationDescriptorWithSource
    public SourceElement z() {
        SourceElement sourceElement = SourceElement.f16664a;
        k.d(sourceElement, "NO_SOURCE");
        return sourceElement;
    }
}
