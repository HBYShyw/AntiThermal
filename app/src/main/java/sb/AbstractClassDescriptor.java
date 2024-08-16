package sb;

import fd.StorageManager;
import gd.TypeSubstitutor;
import gd.n1;
import gd.o0;
import gd.s1;
import java.util.Collections;
import java.util.List;
import oc.Name;
import pb.ClassDescriptor;
import pb.ClassifierDescriptor;
import pb.DeclarationDescriptorVisitor;
import pb.ReceiverParameterDescriptor;
import pb.TypeAliasDescriptor;
import zc.InnerClassesScopeWrapper;
import zc.SubstitutingScope;

/* compiled from: AbstractClassDescriptor.java */
/* renamed from: sb.a, reason: use source file name */
/* loaded from: classes2.dex */
public abstract class AbstractClassDescriptor extends t {

    /* renamed from: f, reason: collision with root package name */
    private final Name f18202f;

    /* renamed from: g, reason: collision with root package name */
    protected final fd.i<o0> f18203g;

    /* renamed from: h, reason: collision with root package name */
    private final fd.i<zc.h> f18204h;

    /* renamed from: i, reason: collision with root package name */
    private final fd.i<ReceiverParameterDescriptor> f18205i;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: AbstractClassDescriptor.java */
    /* renamed from: sb.a$a */
    /* loaded from: classes2.dex */
    public class a implements ya.a<o0> {

        /* JADX INFO: Access modifiers changed from: package-private */
        /* compiled from: AbstractClassDescriptor.java */
        /* renamed from: sb.a$a$a, reason: collision with other inner class name */
        /* loaded from: classes2.dex */
        public class C0101a implements ya.l<hd.g, o0> {
            C0101a() {
            }

            @Override // ya.l
            /* renamed from: a, reason: merged with bridge method [inline-methods] */
            public o0 invoke(hd.g gVar) {
                ClassifierDescriptor f10 = gVar.f(AbstractClassDescriptor.this);
                if (f10 == null) {
                    return AbstractClassDescriptor.this.f18203g.invoke();
                }
                if (f10 instanceof TypeAliasDescriptor) {
                    return gd.h0.b((TypeAliasDescriptor) f10, s1.g(f10.n().getParameters()));
                }
                if (f10 instanceof t) {
                    return s1.u(f10.n().u(gVar), ((t) f10).Q(gVar), this);
                }
                return f10.x();
            }
        }

        a() {
        }

        @Override // ya.a
        /* renamed from: a, reason: merged with bridge method [inline-methods] */
        public o0 invoke() {
            AbstractClassDescriptor abstractClassDescriptor = AbstractClassDescriptor.this;
            return s1.v(abstractClassDescriptor, abstractClassDescriptor.M0(), new C0101a());
        }
    }

    /* compiled from: AbstractClassDescriptor.java */
    /* renamed from: sb.a$b */
    /* loaded from: classes2.dex */
    class b implements ya.a<zc.h> {
        b() {
        }

        @Override // ya.a
        /* renamed from: a, reason: merged with bridge method [inline-methods] */
        public zc.h invoke() {
            return new InnerClassesScopeWrapper(AbstractClassDescriptor.this.M0());
        }
    }

    /* compiled from: AbstractClassDescriptor.java */
    /* renamed from: sb.a$c */
    /* loaded from: classes2.dex */
    class c implements ya.a<ReceiverParameterDescriptor> {
        c() {
        }

        @Override // ya.a
        /* renamed from: a, reason: merged with bridge method [inline-methods] */
        public ReceiverParameterDescriptor invoke() {
            return new LazyClassReceiverParameterDescriptor(AbstractClassDescriptor.this);
        }
    }

    public AbstractClassDescriptor(StorageManager storageManager, Name name) {
        if (storageManager == null) {
            J0(0);
        }
        if (name == null) {
            J0(1);
        }
        this.f18202f = name;
        this.f18203g = storageManager.g(new a());
        this.f18204h = storageManager.g(new b());
        this.f18205i = storageManager.g(new c());
    }

    private static /* synthetic */ void J0(int i10) {
        String str = (i10 == 2 || i10 == 3 || i10 == 4 || i10 == 5 || i10 == 6 || i10 == 9 || i10 == 12 || i10 == 14 || i10 == 16 || i10 == 17 || i10 == 19 || i10 == 20) ? "@NotNull method %s.%s must not return null" : "Argument for @NotNull parameter '%s' of %s.%s must not be null";
        Object[] objArr = new Object[(i10 == 2 || i10 == 3 || i10 == 4 || i10 == 5 || i10 == 6 || i10 == 9 || i10 == 12 || i10 == 14 || i10 == 16 || i10 == 17 || i10 == 19 || i10 == 20) ? 2 : 3];
        switch (i10) {
            case 1:
                objArr[0] = "name";
                break;
            case 2:
            case 3:
            case 4:
            case 5:
            case 6:
            case 9:
            case 12:
            case 14:
            case 16:
            case 17:
            case 19:
            case 20:
                objArr[0] = "kotlin/reflect/jvm/internal/impl/descriptors/impl/AbstractClassDescriptor";
                break;
            case 7:
            case 13:
                objArr[0] = "typeArguments";
                break;
            case 8:
            case 11:
                objArr[0] = "kotlinTypeRefiner";
                break;
            case 10:
            case 15:
                objArr[0] = "typeSubstitution";
                break;
            case 18:
                objArr[0] = "substitutor";
                break;
            default:
                objArr[0] = "storageManager";
                break;
        }
        if (i10 == 2) {
            objArr[1] = "getName";
        } else if (i10 == 3) {
            objArr[1] = "getOriginal";
        } else if (i10 == 4) {
            objArr[1] = "getUnsubstitutedInnerClassesScope";
        } else if (i10 == 5) {
            objArr[1] = "getThisAsReceiverParameter";
        } else if (i10 == 6) {
            objArr[1] = "getContextReceivers";
        } else if (i10 == 9 || i10 == 12 || i10 == 14 || i10 == 16) {
            objArr[1] = "getMemberScope";
        } else if (i10 == 17) {
            objArr[1] = "getUnsubstitutedMemberScope";
        } else if (i10 == 19) {
            objArr[1] = "substitute";
        } else if (i10 != 20) {
            objArr[1] = "kotlin/reflect/jvm/internal/impl/descriptors/impl/AbstractClassDescriptor";
        } else {
            objArr[1] = "getDefaultType";
        }
        switch (i10) {
            case 2:
            case 3:
            case 4:
            case 5:
            case 6:
            case 9:
            case 12:
            case 14:
            case 16:
            case 17:
            case 19:
            case 20:
                break;
            case 7:
            case 8:
            case 10:
            case 11:
            case 13:
            case 15:
                objArr[2] = "getMemberScope";
                break;
            case 18:
                objArr[2] = "substitute";
                break;
            default:
                objArr[2] = "<init>";
                break;
        }
        String format = String.format(str, objArr);
        if (i10 != 2 && i10 != 3 && i10 != 4 && i10 != 5 && i10 != 6 && i10 != 9 && i10 != 12 && i10 != 14 && i10 != 16 && i10 != 17 && i10 != 19 && i10 != 20) {
            throw new IllegalArgumentException(format);
        }
        throw new IllegalStateException(format);
    }

    @Override // pb.ClassDescriptor
    public zc.h F0() {
        zc.h invoke = this.f18204h.invoke();
        if (invoke == null) {
            J0(4);
        }
        return invoke;
    }

    @Override // pb.DeclarationDescriptor
    public <R, D> R H0(DeclarationDescriptorVisitor<R, D> declarationDescriptorVisitor, D d10) {
        return declarationDescriptorVisitor.c(this, d10);
    }

    @Override // pb.ClassDescriptor
    public zc.h I0(n1 n1Var) {
        if (n1Var == null) {
            J0(15);
        }
        zc.h P = P(n1Var, wc.c.o(sc.e.g(this)));
        if (P == null) {
            J0(16);
        }
        return P;
    }

    @Override // pb.ClassDescriptor
    public zc.h M0() {
        zc.h Q = Q(wc.c.o(sc.e.g(this)));
        if (Q == null) {
            J0(17);
        }
        return Q;
    }

    @Override // pb.Substitutable
    /* renamed from: O0, reason: merged with bridge method [inline-methods] */
    public ClassDescriptor c(TypeSubstitutor typeSubstitutor) {
        if (typeSubstitutor == null) {
            J0(18);
        }
        return typeSubstitutor.k() ? this : new LazySubstitutingClassDescriptor(this, typeSubstitutor);
    }

    @Override // sb.t
    public zc.h P(n1 n1Var, hd.g gVar) {
        if (n1Var == null) {
            J0(10);
        }
        if (gVar == null) {
            J0(11);
        }
        if (!n1Var.f()) {
            return new SubstitutingScope(Q(gVar), TypeSubstitutor.g(n1Var));
        }
        zc.h Q = Q(gVar);
        if (Q == null) {
            J0(12);
        }
        return Q;
    }

    @Override // pb.ClassDescriptor
    public List<ReceiverParameterDescriptor> P0() {
        List<ReceiverParameterDescriptor> emptyList = Collections.emptyList();
        if (emptyList == null) {
            J0(6);
        }
        return emptyList;
    }

    @Override // pb.ClassDescriptor
    public ReceiverParameterDescriptor S0() {
        ReceiverParameterDescriptor invoke = this.f18205i.invoke();
        if (invoke == null) {
            J0(5);
        }
        return invoke;
    }

    @Override // sb.t, pb.DeclarationDescriptor
    public ClassDescriptor a() {
        return this;
    }

    @Override // pb.Named
    public Name getName() {
        Name name = this.f18202f;
        if (name == null) {
            J0(2);
        }
        return name;
    }

    @Override // pb.ClassDescriptor, pb.ClassifierDescriptor
    public o0 x() {
        o0 invoke = this.f18203g.invoke();
        if (invoke == null) {
            J0(20);
        }
        return invoke;
    }
}
