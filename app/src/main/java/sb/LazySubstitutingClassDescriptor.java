package sb;

import fd.LockBasedStorageManager;
import gd.ClassTypeConstructorImpl;
import gd.DescriptorSubstitutor;
import gd.TypeConstructor;
import gd.TypeSubstitutor;
import gd.Variance;
import gd.n1;
import gd.o0;
import gd.s1;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import kotlin.collections._Collections;
import oc.Name;
import pb.ClassConstructorDescriptor;
import pb.ClassDescriptor;
import pb.ClassKind;
import pb.DeclarationDescriptor;
import pb.DeclarationDescriptorVisitor;
import pb.Modality;
import pb.ReceiverParameterDescriptor;
import pb.SourceElement;
import pb.TypeParameterDescriptor;
import pb.ValueClassRepresentation;
import zc.SubstitutingScope;

/* compiled from: LazySubstitutingClassDescriptor.java */
/* renamed from: sb.s, reason: use source file name */
/* loaded from: classes2.dex */
public class LazySubstitutingClassDescriptor extends t {

    /* renamed from: f, reason: collision with root package name */
    private final t f18382f;

    /* renamed from: g, reason: collision with root package name */
    private final TypeSubstitutor f18383g;

    /* renamed from: h, reason: collision with root package name */
    private TypeSubstitutor f18384h;

    /* renamed from: i, reason: collision with root package name */
    private List<TypeParameterDescriptor> f18385i;

    /* renamed from: j, reason: collision with root package name */
    private List<TypeParameterDescriptor> f18386j;

    /* renamed from: k, reason: collision with root package name */
    private TypeConstructor f18387k;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: LazySubstitutingClassDescriptor.java */
    /* renamed from: sb.s$a */
    /* loaded from: classes2.dex */
    public class a implements ya.l<TypeParameterDescriptor, Boolean> {
        a() {
        }

        @Override // ya.l
        /* renamed from: a, reason: merged with bridge method [inline-methods] */
        public Boolean invoke(TypeParameterDescriptor typeParameterDescriptor) {
            return Boolean.valueOf(!typeParameterDescriptor.t0());
        }
    }

    /* compiled from: LazySubstitutingClassDescriptor.java */
    /* renamed from: sb.s$b */
    /* loaded from: classes2.dex */
    class b implements ya.l<o0, o0> {
        b() {
        }

        @Override // ya.l
        /* renamed from: a, reason: merged with bridge method [inline-methods] */
        public o0 invoke(o0 o0Var) {
            return LazySubstitutingClassDescriptor.this.V0(o0Var);
        }
    }

    public LazySubstitutingClassDescriptor(t tVar, TypeSubstitutor typeSubstitutor) {
        this.f18382f = tVar;
        this.f18383g = typeSubstitutor;
    }

    /* JADX WARN: Removed duplicated region for block: B:32:0x0069  */
    /* JADX WARN: Removed duplicated region for block: B:34:0x00c6 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:44:0x00e3 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:58:0x006e  */
    /* JADX WARN: Removed duplicated region for block: B:59:0x0073  */
    /* JADX WARN: Removed duplicated region for block: B:60:0x0078  */
    /* JADX WARN: Removed duplicated region for block: B:61:0x007d  */
    /* JADX WARN: Removed duplicated region for block: B:62:0x0082  */
    /* JADX WARN: Removed duplicated region for block: B:63:0x0087  */
    /* JADX WARN: Removed duplicated region for block: B:64:0x008c  */
    /* JADX WARN: Removed duplicated region for block: B:65:0x0091  */
    /* JADX WARN: Removed duplicated region for block: B:66:0x0094  */
    /* JADX WARN: Removed duplicated region for block: B:67:0x0099  */
    /* JADX WARN: Removed duplicated region for block: B:68:0x009e  */
    /* JADX WARN: Removed duplicated region for block: B:69:0x00a3  */
    /* JADX WARN: Removed duplicated region for block: B:70:0x00a8  */
    /* JADX WARN: Removed duplicated region for block: B:71:0x00ad  */
    /* JADX WARN: Removed duplicated region for block: B:72:0x00b2  */
    /* JADX WARN: Removed duplicated region for block: B:73:0x00b7  */
    /* JADX WARN: Removed duplicated region for block: B:74:0x00bc  */
    /* JADX WARN: Removed duplicated region for block: B:75:0x00bf  */
    /* JADX WARN: Removed duplicated region for block: B:76:0x00c2  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private static /* synthetic */ void J0(int i10) {
        String format;
        String str = (i10 == 2 || i10 == 3 || i10 == 5 || i10 == 6 || i10 == 8 || i10 == 10 || i10 == 13 || i10 == 23) ? "Argument for @NotNull parameter '%s' of %s.%s must not be null" : "@NotNull method %s.%s must not return null";
        Object[] objArr = new Object[(i10 == 2 || i10 == 3 || i10 == 5 || i10 == 6 || i10 == 8 || i10 == 10 || i10 == 13 || i10 == 23) ? 3 : 2];
        if (i10 != 2) {
            if (i10 != 3) {
                if (i10 != 5) {
                    if (i10 != 6) {
                        if (i10 != 8) {
                            if (i10 != 10) {
                                if (i10 != 13) {
                                    if (i10 != 23) {
                                        objArr[0] = "kotlin/reflect/jvm/internal/impl/descriptors/impl/LazySubstitutingClassDescriptor";
                                    } else {
                                        objArr[0] = "substitutor";
                                    }
                                    switch (i10) {
                                        case 2:
                                        case 3:
                                        case 5:
                                        case 6:
                                        case 8:
                                        case 10:
                                        case 13:
                                        case 23:
                                            objArr[1] = "kotlin/reflect/jvm/internal/impl/descriptors/impl/LazySubstitutingClassDescriptor";
                                            break;
                                        case 4:
                                        case 7:
                                        case 9:
                                        case 11:
                                            objArr[1] = "getMemberScope";
                                            break;
                                        case 12:
                                        case 14:
                                            objArr[1] = "getUnsubstitutedMemberScope";
                                            break;
                                        case 15:
                                            objArr[1] = "getStaticScope";
                                            break;
                                        case 16:
                                            objArr[1] = "getDefaultType";
                                            break;
                                        case 17:
                                            objArr[1] = "getContextReceivers";
                                            break;
                                        case 18:
                                            objArr[1] = "getConstructors";
                                            break;
                                        case 19:
                                            objArr[1] = "getAnnotations";
                                            break;
                                        case 20:
                                            objArr[1] = "getName";
                                            break;
                                        case 21:
                                            objArr[1] = "getOriginal";
                                            break;
                                        case 22:
                                            objArr[1] = "getContainingDeclaration";
                                            break;
                                        case 24:
                                            objArr[1] = "substitute";
                                            break;
                                        case 25:
                                            objArr[1] = "getKind";
                                            break;
                                        case 26:
                                            objArr[1] = "getModality";
                                            break;
                                        case 27:
                                            objArr[1] = "getVisibility";
                                            break;
                                        case 28:
                                            objArr[1] = "getUnsubstitutedInnerClassesScope";
                                            break;
                                        case 29:
                                            objArr[1] = "getSource";
                                            break;
                                        case 30:
                                            objArr[1] = "getDeclaredTypeParameters";
                                            break;
                                        case 31:
                                            objArr[1] = "getSealedSubclasses";
                                            break;
                                        default:
                                            objArr[1] = "getTypeConstructor";
                                            break;
                                    }
                                    if (i10 != 2 || i10 == 3 || i10 == 5 || i10 == 6 || i10 == 8 || i10 == 10) {
                                        objArr[2] = "getMemberScope";
                                    } else if (i10 == 13) {
                                        objArr[2] = "getUnsubstitutedMemberScope";
                                    } else if (i10 == 23) {
                                        objArr[2] = "substitute";
                                    }
                                    format = String.format(str, objArr);
                                    if (i10 == 2 && i10 != 3 && i10 != 5 && i10 != 6 && i10 != 8 && i10 != 10 && i10 != 13 && i10 != 23) {
                                        throw new IllegalStateException(format);
                                    }
                                    throw new IllegalArgumentException(format);
                                }
                            }
                        }
                    }
                }
                objArr[0] = "typeSubstitution";
                switch (i10) {
                }
                if (i10 != 2) {
                }
                objArr[2] = "getMemberScope";
                format = String.format(str, objArr);
                if (i10 == 2) {
                }
                throw new IllegalArgumentException(format);
            }
            objArr[0] = "kotlinTypeRefiner";
            switch (i10) {
            }
            if (i10 != 2) {
            }
            objArr[2] = "getMemberScope";
            format = String.format(str, objArr);
            if (i10 == 2) {
            }
            throw new IllegalArgumentException(format);
        }
        objArr[0] = "typeArguments";
        switch (i10) {
        }
        if (i10 != 2) {
        }
        objArr[2] = "getMemberScope";
        format = String.format(str, objArr);
        if (i10 == 2) {
        }
        throw new IllegalArgumentException(format);
    }

    private TypeSubstitutor T0() {
        List<TypeParameterDescriptor> P;
        if (this.f18384h == null) {
            if (this.f18383g.k()) {
                this.f18384h = this.f18383g;
            } else {
                List<TypeParameterDescriptor> parameters = this.f18382f.n().getParameters();
                this.f18385i = new ArrayList(parameters.size());
                this.f18384h = DescriptorSubstitutor.b(parameters, this.f18383g.j(), this, this.f18385i);
                P = _Collections.P(this.f18385i, new a());
                this.f18386j = P;
            }
        }
        return this.f18384h;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public o0 V0(o0 o0Var) {
        return (o0Var == null || this.f18383g.k()) ? o0Var : (o0) T0().p(o0Var, Variance.INVARIANT);
    }

    @Override // pb.ClassDescriptor, pb.ClassifierDescriptorWithTypeParameters
    public List<TypeParameterDescriptor> B() {
        T0();
        List<TypeParameterDescriptor> list = this.f18386j;
        if (list == null) {
            J0(30);
        }
        return list;
    }

    @Override // pb.MemberDescriptor
    public boolean D() {
        return this.f18382f.D();
    }

    @Override // pb.ClassDescriptor
    public boolean F() {
        return this.f18382f.F();
    }

    @Override // pb.ClassDescriptor
    public zc.h F0() {
        zc.h F0 = this.f18382f.F0();
        if (F0 == null) {
            J0(28);
        }
        return F0;
    }

    @Override // pb.ClassDescriptor
    public ValueClassRepresentation<o0> G0() {
        ValueClassRepresentation<o0> G0 = this.f18382f.G0();
        if (G0 == null) {
            return null;
        }
        return G0.b(new b());
    }

    @Override // pb.DeclarationDescriptor
    public <R, D> R H0(DeclarationDescriptorVisitor<R, D> declarationDescriptorVisitor, D d10) {
        return declarationDescriptorVisitor.c(this, d10);
    }

    @Override // pb.ClassDescriptor
    public zc.h I0(n1 n1Var) {
        if (n1Var == null) {
            J0(10);
        }
        zc.h P = P(n1Var, wc.c.o(sc.e.g(this)));
        if (P == null) {
            J0(11);
        }
        return P;
    }

    @Override // pb.ClassDescriptor
    public boolean L() {
        return this.f18382f.L();
    }

    @Override // pb.ClassDescriptor
    public zc.h M0() {
        zc.h Q = Q(wc.c.o(sc.e.g(this.f18382f)));
        if (Q == null) {
            J0(12);
        }
        return Q;
    }

    @Override // pb.MemberDescriptor
    public boolean N0() {
        return this.f18382f.N0();
    }

    @Override // sb.t
    public zc.h P(n1 n1Var, hd.g gVar) {
        if (n1Var == null) {
            J0(5);
        }
        if (gVar == null) {
            J0(6);
        }
        zc.h P = this.f18382f.P(n1Var, gVar);
        if (!this.f18383g.k()) {
            return new SubstitutingScope(P, T0());
        }
        if (P == null) {
            J0(7);
        }
        return P;
    }

    @Override // pb.ClassDescriptor
    public List<ReceiverParameterDescriptor> P0() {
        List<ReceiverParameterDescriptor> emptyList = Collections.emptyList();
        if (emptyList == null) {
            J0(17);
        }
        return emptyList;
    }

    @Override // sb.t
    public zc.h Q(hd.g gVar) {
        if (gVar == null) {
            J0(13);
        }
        zc.h Q = this.f18382f.Q(gVar);
        if (!this.f18383g.k()) {
            return new SubstitutingScope(Q, T0());
        }
        if (Q == null) {
            J0(14);
        }
        return Q;
    }

    @Override // pb.ClassDescriptor
    public boolean R0() {
        return this.f18382f.R0();
    }

    @Override // pb.ClassDescriptor
    public Collection<ClassDescriptor> S() {
        Collection<ClassDescriptor> S = this.f18382f.S();
        if (S == null) {
            J0(31);
        }
        return S;
    }

    @Override // pb.ClassDescriptor
    public ReceiverParameterDescriptor S0() {
        throw new UnsupportedOperationException();
    }

    @Override // pb.MemberDescriptor
    public boolean U() {
        return this.f18382f.U();
    }

    @Override // pb.Substitutable
    /* renamed from: U0, reason: merged with bridge method [inline-methods] */
    public ClassDescriptor c(TypeSubstitutor typeSubstitutor) {
        if (typeSubstitutor == null) {
            J0(23);
        }
        return typeSubstitutor.k() ? this : new LazySubstitutingClassDescriptor(this, TypeSubstitutor.h(typeSubstitutor.j(), T0().j()));
    }

    @Override // pb.ClassDescriptor
    public ClassConstructorDescriptor Z() {
        return this.f18382f.Z();
    }

    @Override // pb.ClassDescriptor
    public zc.h a0() {
        zc.h a02 = this.f18382f.a0();
        if (a02 == null) {
            J0(15);
        }
        return a02;
    }

    @Override // pb.ClassDescriptor, pb.DeclarationDescriptorNonRoot, pb.DeclarationDescriptor
    public DeclarationDescriptor b() {
        DeclarationDescriptor b10 = this.f18382f.b();
        if (b10 == null) {
            J0(22);
        }
        return b10;
    }

    @Override // pb.ClassDescriptor
    public ClassDescriptor c0() {
        return this.f18382f.c0();
    }

    @Override // pb.ClassDescriptor, pb.DeclarationDescriptorWithVisibility, pb.MemberDescriptor
    public pb.u g() {
        pb.u g6 = this.f18382f.g();
        if (g6 == null) {
            J0(27);
        }
        return g6;
    }

    @Override // pb.ClassDescriptor
    public ClassKind getKind() {
        ClassKind kind = this.f18382f.getKind();
        if (kind == null) {
            J0(25);
        }
        return kind;
    }

    @Override // pb.Named
    public Name getName() {
        Name name = this.f18382f.getName();
        if (name == null) {
            J0(20);
        }
        return name;
    }

    @Override // qb.a
    public qb.g i() {
        qb.g i10 = this.f18382f.i();
        if (i10 == null) {
            J0(19);
        }
        return i10;
    }

    @Override // pb.ClassifierDescriptor
    public TypeConstructor n() {
        TypeConstructor n10 = this.f18382f.n();
        if (this.f18383g.k()) {
            if (n10 == null) {
                J0(0);
            }
            return n10;
        }
        if (this.f18387k == null) {
            TypeSubstitutor T0 = T0();
            Collection<gd.g0> q10 = n10.q();
            ArrayList arrayList = new ArrayList(q10.size());
            Iterator<gd.g0> it = q10.iterator();
            while (it.hasNext()) {
                arrayList.add(T0.p(it.next(), Variance.INVARIANT));
            }
            this.f18387k = new ClassTypeConstructorImpl(this, this.f18385i, arrayList, LockBasedStorageManager.f11424e);
        }
        TypeConstructor typeConstructor = this.f18387k;
        if (typeConstructor == null) {
            J0(1);
        }
        return typeConstructor;
    }

    @Override // pb.ClassDescriptor, pb.MemberDescriptor
    public Modality o() {
        Modality o10 = this.f18382f.o();
        if (o10 == null) {
            J0(26);
        }
        return o10;
    }

    @Override // pb.ClassDescriptor
    public Collection<ClassConstructorDescriptor> p() {
        Collection<ClassConstructorDescriptor> p10 = this.f18382f.p();
        ArrayList arrayList = new ArrayList(p10.size());
        for (ClassConstructorDescriptor classConstructorDescriptor : p10) {
            arrayList.add(((ClassConstructorDescriptor) classConstructorDescriptor.A().s(classConstructorDescriptor.T0()).f(classConstructorDescriptor.o()).p(classConstructorDescriptor.g()).q(classConstructorDescriptor.getKind()).l(false).build()).c(T0()));
        }
        return arrayList;
    }

    @Override // pb.ClassDescriptor
    public boolean q() {
        return this.f18382f.q();
    }

    @Override // pb.ClassifierDescriptorWithTypeParameters
    public boolean r() {
        return this.f18382f.r();
    }

    @Override // pb.ClassDescriptor, pb.ClassifierDescriptor
    public o0 x() {
        o0 k10 = gd.h0.k(gd.o.f11859a.a(i(), null, null), n(), s1.g(n().getParameters()), false, M0());
        if (k10 == null) {
            J0(16);
        }
        return k10;
    }

    @Override // pb.ClassDescriptor
    public boolean y() {
        return this.f18382f.y();
    }

    @Override // pb.DeclarationDescriptorWithSource
    public SourceElement z() {
        SourceElement sourceElement = SourceElement.f16664a;
        if (sourceElement == null) {
            J0(29);
        }
        return sourceElement;
    }

    @Override // sb.t, pb.DeclarationDescriptor
    /* renamed from: a */
    public ClassDescriptor T0() {
        ClassDescriptor T0 = this.f18382f.T0();
        if (T0 == null) {
            J0(21);
        }
        return T0;
    }
}
