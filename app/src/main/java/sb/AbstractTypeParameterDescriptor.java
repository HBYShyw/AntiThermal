package sb;

import fd.StorageManager;
import gd.AbstractTypeConstructor;
import gd.TypeConstructor;
import gd.Variance;
import gd.c1;
import gd.o0;
import id.ErrorTypeKind;
import id.ErrorUtils;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import mb.KotlinBuiltIns;
import oc.Name;
import pb.ClassifierDescriptor;
import pb.DeclarationDescriptor;
import pb.DeclarationDescriptorVisitor;
import pb.SourceElement;
import pb.SupertypeLoopChecker;
import pb.TypeParameterDescriptor;
import sc.DescriptorEquivalenceForOverrides;
import zc.LazyScopeAdapter;
import zc.TypeIntersectionScope;

/* compiled from: AbstractTypeParameterDescriptor.java */
/* renamed from: sb.e, reason: use source file name */
/* loaded from: classes2.dex */
public abstract class AbstractTypeParameterDescriptor extends DeclarationDescriptorNonRootImpl implements TypeParameterDescriptor {

    /* renamed from: i, reason: collision with root package name */
    private final Variance f18257i;

    /* renamed from: j, reason: collision with root package name */
    private final boolean f18258j;

    /* renamed from: k, reason: collision with root package name */
    private final int f18259k;

    /* renamed from: l, reason: collision with root package name */
    private final fd.i<TypeConstructor> f18260l;

    /* renamed from: m, reason: collision with root package name */
    private final fd.i<o0> f18261m;

    /* renamed from: n, reason: collision with root package name */
    private final StorageManager f18262n;

    /* compiled from: AbstractTypeParameterDescriptor.java */
    /* renamed from: sb.e$a */
    /* loaded from: classes2.dex */
    class a implements ya.a<TypeConstructor> {

        /* renamed from: e, reason: collision with root package name */
        final /* synthetic */ StorageManager f18263e;

        /* renamed from: f, reason: collision with root package name */
        final /* synthetic */ SupertypeLoopChecker f18264f;

        a(StorageManager storageManager, SupertypeLoopChecker supertypeLoopChecker) {
            this.f18263e = storageManager;
            this.f18264f = supertypeLoopChecker;
        }

        @Override // ya.a
        /* renamed from: a, reason: merged with bridge method [inline-methods] */
        public TypeConstructor invoke() {
            return new c(AbstractTypeParameterDescriptor.this, this.f18263e, this.f18264f);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: AbstractTypeParameterDescriptor.java */
    /* renamed from: sb.e$b */
    /* loaded from: classes2.dex */
    public class b implements ya.a<o0> {

        /* renamed from: e, reason: collision with root package name */
        final /* synthetic */ Name f18266e;

        /* JADX INFO: Access modifiers changed from: package-private */
        /* compiled from: AbstractTypeParameterDescriptor.java */
        /* renamed from: sb.e$b$a */
        /* loaded from: classes2.dex */
        public class a implements ya.a<zc.h> {
            a() {
            }

            @Override // ya.a
            /* renamed from: a, reason: merged with bridge method [inline-methods] */
            public zc.h invoke() {
                return TypeIntersectionScope.j("Scope for type parameter " + b.this.f18266e.b(), AbstractTypeParameterDescriptor.this.getUpperBounds());
            }
        }

        b(Name name) {
            this.f18266e = name;
        }

        @Override // ya.a
        /* renamed from: a, reason: merged with bridge method [inline-methods] */
        public o0 invoke() {
            return gd.h0.k(c1.f11749f.h(), AbstractTypeParameterDescriptor.this.n(), Collections.emptyList(), false, new LazyScopeAdapter(new a()));
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* compiled from: AbstractTypeParameterDescriptor.java */
    /* renamed from: sb.e$c */
    /* loaded from: classes2.dex */
    public class c extends AbstractTypeConstructor {

        /* renamed from: d, reason: collision with root package name */
        private final SupertypeLoopChecker f18269d;

        /* renamed from: e, reason: collision with root package name */
        final /* synthetic */ AbstractTypeParameterDescriptor f18270e;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        public c(AbstractTypeParameterDescriptor abstractTypeParameterDescriptor, StorageManager storageManager, SupertypeLoopChecker supertypeLoopChecker) {
            super(storageManager);
            if (storageManager == null) {
                r(0);
            }
            this.f18270e = abstractTypeParameterDescriptor;
            this.f18269d = supertypeLoopChecker;
        }

        private static /* synthetic */ void r(int i10) {
            String str = (i10 == 1 || i10 == 2 || i10 == 3 || i10 == 4 || i10 == 5 || i10 == 8) ? "@NotNull method %s.%s must not return null" : "Argument for @NotNull parameter '%s' of %s.%s must not be null";
            Object[] objArr = new Object[(i10 == 1 || i10 == 2 || i10 == 3 || i10 == 4 || i10 == 5 || i10 == 8) ? 2 : 3];
            switch (i10) {
                case 1:
                case 2:
                case 3:
                case 4:
                case 5:
                case 8:
                    objArr[0] = "kotlin/reflect/jvm/internal/impl/descriptors/impl/AbstractTypeParameterDescriptor$TypeParameterTypeConstructor";
                    break;
                case 6:
                    objArr[0] = "type";
                    break;
                case 7:
                    objArr[0] = "supertypes";
                    break;
                case 9:
                    objArr[0] = "classifier";
                    break;
                default:
                    objArr[0] = "storageManager";
                    break;
            }
            if (i10 == 1) {
                objArr[1] = "computeSupertypes";
            } else if (i10 == 2) {
                objArr[1] = "getParameters";
            } else if (i10 == 3) {
                objArr[1] = "getDeclarationDescriptor";
            } else if (i10 == 4) {
                objArr[1] = "getBuiltIns";
            } else if (i10 == 5) {
                objArr[1] = "getSupertypeLoopChecker";
            } else if (i10 != 8) {
                objArr[1] = "kotlin/reflect/jvm/internal/impl/descriptors/impl/AbstractTypeParameterDescriptor$TypeParameterTypeConstructor";
            } else {
                objArr[1] = "processSupertypesWithoutCycles";
            }
            switch (i10) {
                case 1:
                case 2:
                case 3:
                case 4:
                case 5:
                case 8:
                    break;
                case 6:
                    objArr[2] = "reportSupertypeLoopError";
                    break;
                case 7:
                    objArr[2] = "processSupertypesWithoutCycles";
                    break;
                case 9:
                    objArr[2] = "isSameClassifier";
                    break;
                default:
                    objArr[2] = "<init>";
                    break;
            }
            String format = String.format(str, objArr);
            if (i10 != 1 && i10 != 2 && i10 != 3 && i10 != 4 && i10 != 5 && i10 != 8) {
                throw new IllegalArgumentException(format);
            }
            throw new IllegalStateException(format);
        }

        @Override // gd.ClassifierBasedTypeConstructor
        protected boolean e(ClassifierDescriptor classifierDescriptor) {
            if (classifierDescriptor == null) {
                r(9);
            }
            return (classifierDescriptor instanceof TypeParameterDescriptor) && DescriptorEquivalenceForOverrides.f18421a.h(this.f18270e, (TypeParameterDescriptor) classifierDescriptor, true);
        }

        @Override // gd.TypeConstructor
        public List<TypeParameterDescriptor> getParameters() {
            List<TypeParameterDescriptor> emptyList = Collections.emptyList();
            if (emptyList == null) {
                r(2);
            }
            return emptyList;
        }

        @Override // gd.AbstractTypeConstructor
        protected Collection<gd.g0> h() {
            List<gd.g0> U0 = this.f18270e.U0();
            if (U0 == null) {
                r(1);
            }
            return U0;
        }

        @Override // gd.AbstractTypeConstructor
        protected gd.g0 i() {
            return ErrorUtils.d(ErrorTypeKind.f12827y, new String[0]);
        }

        @Override // gd.AbstractTypeConstructor
        protected SupertypeLoopChecker l() {
            SupertypeLoopChecker supertypeLoopChecker = this.f18269d;
            if (supertypeLoopChecker == null) {
                r(5);
            }
            return supertypeLoopChecker;
        }

        @Override // gd.AbstractTypeConstructor
        protected List<gd.g0> n(List<gd.g0> list) {
            if (list == null) {
                r(7);
            }
            List<gd.g0> O0 = this.f18270e.O0(list);
            if (O0 == null) {
                r(8);
            }
            return O0;
        }

        @Override // gd.AbstractTypeConstructor
        protected void p(gd.g0 g0Var) {
            if (g0Var == null) {
                r(6);
            }
            this.f18270e.T0(g0Var);
        }

        @Override // gd.TypeConstructor
        public KotlinBuiltIns t() {
            KotlinBuiltIns j10 = wc.c.j(this.f18270e);
            if (j10 == null) {
                r(4);
            }
            return j10;
        }

        public String toString() {
            return this.f18270e.getName().toString();
        }

        @Override // gd.ClassifierBasedTypeConstructor, gd.TypeConstructor
        public ClassifierDescriptor v() {
            AbstractTypeParameterDescriptor abstractTypeParameterDescriptor = this.f18270e;
            if (abstractTypeParameterDescriptor == null) {
                r(3);
            }
            return abstractTypeParameterDescriptor;
        }

        @Override // gd.TypeConstructor
        public boolean w() {
            return true;
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public AbstractTypeParameterDescriptor(StorageManager storageManager, DeclarationDescriptor declarationDescriptor, qb.g gVar, Name name, Variance variance, boolean z10, int i10, SourceElement sourceElement, SupertypeLoopChecker supertypeLoopChecker) {
        super(declarationDescriptor, gVar, name, sourceElement);
        if (storageManager == null) {
            P(0);
        }
        if (declarationDescriptor == null) {
            P(1);
        }
        if (gVar == null) {
            P(2);
        }
        if (name == null) {
            P(3);
        }
        if (variance == null) {
            P(4);
        }
        if (sourceElement == null) {
            P(5);
        }
        if (supertypeLoopChecker == null) {
            P(6);
        }
        this.f18257i = variance;
        this.f18258j = z10;
        this.f18259k = i10;
        this.f18260l = storageManager.g(new a(storageManager, supertypeLoopChecker));
        this.f18261m = storageManager.g(new b(name));
        this.f18262n = storageManager;
    }

    private static /* synthetic */ void P(int i10) {
        String str;
        int i11;
        switch (i10) {
            case 7:
            case 8:
            case 9:
            case 10:
            case 11:
            case 13:
            case 14:
                str = "@NotNull method %s.%s must not return null";
                break;
            case 12:
            default:
                str = "Argument for @NotNull parameter '%s' of %s.%s must not be null";
                break;
        }
        switch (i10) {
            case 7:
            case 8:
            case 9:
            case 10:
            case 11:
            case 13:
            case 14:
                i11 = 2;
                break;
            case 12:
            default:
                i11 = 3;
                break;
        }
        Object[] objArr = new Object[i11];
        switch (i10) {
            case 1:
                objArr[0] = "containingDeclaration";
                break;
            case 2:
                objArr[0] = "annotations";
                break;
            case 3:
                objArr[0] = "name";
                break;
            case 4:
                objArr[0] = "variance";
                break;
            case 5:
                objArr[0] = "source";
                break;
            case 6:
                objArr[0] = "supertypeLoopChecker";
                break;
            case 7:
            case 8:
            case 9:
            case 10:
            case 11:
            case 13:
            case 14:
                objArr[0] = "kotlin/reflect/jvm/internal/impl/descriptors/impl/AbstractTypeParameterDescriptor";
                break;
            case 12:
                objArr[0] = "bounds";
                break;
            default:
                objArr[0] = "storageManager";
                break;
        }
        switch (i10) {
            case 7:
                objArr[1] = "getVariance";
                break;
            case 8:
                objArr[1] = "getUpperBounds";
                break;
            case 9:
                objArr[1] = "getTypeConstructor";
                break;
            case 10:
                objArr[1] = "getDefaultType";
                break;
            case 11:
                objArr[1] = "getOriginal";
                break;
            case 12:
            default:
                objArr[1] = "kotlin/reflect/jvm/internal/impl/descriptors/impl/AbstractTypeParameterDescriptor";
                break;
            case 13:
                objArr[1] = "processBoundsWithoutCycles";
                break;
            case 14:
                objArr[1] = "getStorageManager";
                break;
        }
        switch (i10) {
            case 7:
            case 8:
            case 9:
            case 10:
            case 11:
            case 13:
            case 14:
                break;
            case 12:
                objArr[2] = "processBoundsWithoutCycles";
                break;
            default:
                objArr[2] = "<init>";
                break;
        }
        String format = String.format(str, objArr);
        switch (i10) {
            case 7:
            case 8:
            case 9:
            case 10:
            case 11:
            case 13:
            case 14:
                throw new IllegalStateException(format);
            case 12:
            default:
                throw new IllegalArgumentException(format);
        }
    }

    @Override // pb.DeclarationDescriptor
    public <R, D> R H0(DeclarationDescriptorVisitor<R, D> declarationDescriptorVisitor, D d10) {
        return declarationDescriptorVisitor.b(this, d10);
    }

    @Override // pb.TypeParameterDescriptor
    public boolean N() {
        return this.f18258j;
    }

    protected List<gd.g0> O0(List<gd.g0> list) {
        if (list == null) {
            P(12);
        }
        if (list == null) {
            P(13);
        }
        return list;
    }

    protected abstract void T0(gd.g0 g0Var);

    protected abstract List<gd.g0> U0();

    @Override // pb.TypeParameterDescriptor
    public List<gd.g0> getUpperBounds() {
        List<gd.g0> q10 = ((c) n()).q();
        if (q10 == null) {
            P(8);
        }
        return q10;
    }

    @Override // pb.TypeParameterDescriptor
    public int j() {
        return this.f18259k;
    }

    @Override // pb.TypeParameterDescriptor, pb.ClassifierDescriptor
    public final TypeConstructor n() {
        TypeConstructor invoke = this.f18260l.invoke();
        if (invoke == null) {
            P(9);
        }
        return invoke;
    }

    @Override // pb.TypeParameterDescriptor
    public StorageManager o0() {
        StorageManager storageManager = this.f18262n;
        if (storageManager == null) {
            P(14);
        }
        return storageManager;
    }

    @Override // pb.TypeParameterDescriptor
    public Variance s() {
        Variance variance = this.f18257i;
        if (variance == null) {
            P(7);
        }
        return variance;
    }

    @Override // pb.TypeParameterDescriptor
    public boolean t0() {
        return false;
    }

    @Override // pb.ClassifierDescriptor
    public o0 x() {
        o0 invoke = this.f18261m.invoke();
        if (invoke == null) {
            P(10);
        }
        return invoke;
    }

    @Override // sb.DeclarationDescriptorNonRootImpl, sb.DeclarationDescriptorImpl, pb.DeclarationDescriptor
    public TypeParameterDescriptor a() {
        TypeParameterDescriptor typeParameterDescriptor = (TypeParameterDescriptor) super.a();
        if (typeParameterDescriptor == null) {
            P(11);
        }
        return typeParameterDescriptor;
    }
}
