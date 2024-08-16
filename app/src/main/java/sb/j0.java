package sb;

import ad.ImplicitReceiver;
import ad.ReceiverValue;
import fd.StorageManager;
import gd.TypeSubstitutor;
import gd.Variance;
import gd.o0;
import gd.s0;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import oc.Name;
import oc.SpecialNames;
import pb.CallableMemberDescriptor;
import pb.ClassConstructorDescriptor;
import pb.ClassDescriptor;
import pb.DeclarationDescriptor;
import pb.FunctionDescriptor;
import pb.Modality;
import pb.ReceiverParameterDescriptor;
import pb.SourceElement;
import pb.TypeAliasDescriptor;
import pb.ValueParameterDescriptor;
import sc.DescriptorFactory;
import za.DefaultConstructorMarker;
import za.Lambda;
import za.PropertyReference1Impl;
import za.Reflection;

/* compiled from: TypeAliasConstructorDescriptor.kt */
/* loaded from: classes2.dex */
public final class j0 extends FunctionDescriptorImpl implements i0 {
    private final StorageManager I;
    private final TypeAliasDescriptor J;
    private final fd.j K;
    private ClassConstructorDescriptor L;
    static final /* synthetic */ gb.l<Object>[] N = {Reflection.g(new PropertyReference1Impl(Reflection.b(j0.class), "withDispatchReceiver", "getWithDispatchReceiver()Lorg/jetbrains/kotlin/descriptors/impl/TypeAliasConstructorDescriptor;"))};
    public static final a M = new a(null);

    /* compiled from: TypeAliasConstructorDescriptor.kt */
    /* loaded from: classes2.dex */
    public static final class a {
        private a() {
        }

        public /* synthetic */ a(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public final TypeSubstitutor c(TypeAliasDescriptor typeAliasDescriptor) {
            if (typeAliasDescriptor.w() == null) {
                return null;
            }
            return TypeSubstitutor.f(typeAliasDescriptor.e0());
        }

        public final i0 b(StorageManager storageManager, TypeAliasDescriptor typeAliasDescriptor, ClassConstructorDescriptor classConstructorDescriptor) {
            ClassConstructorDescriptor c10;
            List<ReceiverParameterDescriptor> j10;
            List<ReceiverParameterDescriptor> list;
            int u7;
            za.k.e(storageManager, "storageManager");
            za.k.e(typeAliasDescriptor, "typeAliasDescriptor");
            za.k.e(classConstructorDescriptor, "constructor");
            TypeSubstitutor c11 = c(typeAliasDescriptor);
            if (c11 == null || (c10 = classConstructorDescriptor.c(c11)) == null) {
                return null;
            }
            qb.g i10 = classConstructorDescriptor.i();
            CallableMemberDescriptor.a kind = classConstructorDescriptor.getKind();
            za.k.d(kind, "constructor.kind");
            SourceElement z10 = typeAliasDescriptor.z();
            za.k.d(z10, "typeAliasDescriptor.source");
            j0 j0Var = new j0(storageManager, typeAliasDescriptor, c10, null, i10, kind, z10, null);
            List<ValueParameterDescriptor> X0 = FunctionDescriptorImpl.X0(j0Var, classConstructorDescriptor.l(), c11);
            if (X0 == null) {
                return null;
            }
            o0 c12 = gd.d0.c(c10.f().Z0());
            o0 x10 = typeAliasDescriptor.x();
            za.k.d(x10, "typeAliasDescriptor.defaultType");
            o0 j11 = s0.j(c12, x10);
            ReceiverParameterDescriptor m02 = classConstructorDescriptor.m0();
            ReceiverParameterDescriptor i11 = m02 != null ? DescriptorFactory.i(j0Var, c11.n(m02.getType(), Variance.INVARIANT), qb.g.f17195b.b()) : null;
            ClassDescriptor w10 = typeAliasDescriptor.w();
            if (w10 != null) {
                List<ReceiverParameterDescriptor> w02 = classConstructorDescriptor.w0();
                za.k.d(w02, "constructor.contextReceiverParameters");
                u7 = kotlin.collections.s.u(w02, 10);
                ArrayList arrayList = new ArrayList(u7);
                for (ReceiverParameterDescriptor receiverParameterDescriptor : w02) {
                    gd.g0 n10 = c11.n(receiverParameterDescriptor.getType(), Variance.INVARIANT);
                    ReceiverValue value = receiverParameterDescriptor.getValue();
                    za.k.c(value, "null cannot be cast to non-null type org.jetbrains.kotlin.resolve.scopes.receivers.ImplicitContextReceiver");
                    arrayList.add(DescriptorFactory.c(w10, n10, ((ImplicitReceiver) value).a(), qb.g.f17195b.b()));
                }
                list = arrayList;
            } else {
                j10 = kotlin.collections.r.j();
                list = j10;
            }
            j0Var.a1(i11, null, list, typeAliasDescriptor.B(), X0, j11, Modality.FINAL, typeAliasDescriptor.g());
            return j0Var;
        }
    }

    /* compiled from: TypeAliasConstructorDescriptor.kt */
    /* loaded from: classes2.dex */
    static final class b extends Lambda implements ya.a<j0> {

        /* renamed from: f, reason: collision with root package name */
        final /* synthetic */ ClassConstructorDescriptor f18290f;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        b(ClassConstructorDescriptor classConstructorDescriptor) {
            super(0);
            this.f18290f = classConstructorDescriptor;
        }

        /* JADX WARN: Multi-variable type inference failed */
        @Override // ya.a
        /* renamed from: a, reason: merged with bridge method [inline-methods] */
        public final j0 invoke() {
            int u7;
            StorageManager o02 = j0.this.o0();
            TypeAliasDescriptor x12 = j0.this.x1();
            ClassConstructorDescriptor classConstructorDescriptor = this.f18290f;
            j0 j0Var = j0.this;
            qb.g i10 = classConstructorDescriptor.i();
            CallableMemberDescriptor.a kind = this.f18290f.getKind();
            za.k.d(kind, "underlyingConstructorDescriptor.kind");
            SourceElement z10 = j0.this.x1().z();
            za.k.d(z10, "typeAliasDescriptor.source");
            j0 j0Var2 = new j0(o02, x12, classConstructorDescriptor, j0Var, i10, kind, z10, null);
            j0 j0Var3 = j0.this;
            ClassConstructorDescriptor classConstructorDescriptor2 = this.f18290f;
            TypeSubstitutor c10 = j0.M.c(j0Var3.x1());
            if (c10 == null) {
                return null;
            }
            ReceiverParameterDescriptor m02 = classConstructorDescriptor2.m0();
            ReceiverParameterDescriptor c11 = m02 != 0 ? m02.c(c10) : null;
            List<ReceiverParameterDescriptor> w02 = classConstructorDescriptor2.w0();
            za.k.d(w02, "underlyingConstructorDes…contextReceiverParameters");
            u7 = kotlin.collections.s.u(w02, 10);
            ArrayList arrayList = new ArrayList(u7);
            Iterator<T> it = w02.iterator();
            while (it.hasNext()) {
                arrayList.add(((ReceiverParameterDescriptor) it.next()).c(c10));
            }
            j0Var2.a1(null, c11, arrayList, j0Var3.x1().B(), j0Var3.l(), j0Var3.f(), Modality.FINAL, j0Var3.x1().g());
            return j0Var2;
        }
    }

    private j0(StorageManager storageManager, TypeAliasDescriptor typeAliasDescriptor, ClassConstructorDescriptor classConstructorDescriptor, i0 i0Var, qb.g gVar, CallableMemberDescriptor.a aVar, SourceElement sourceElement) {
        super(typeAliasDescriptor, i0Var, gVar, SpecialNames.f16455j, aVar, sourceElement);
        this.I = storageManager;
        this.J = typeAliasDescriptor;
        e1(x1().N0());
        this.K = storageManager.f(new b(classConstructorDescriptor));
        this.L = classConstructorDescriptor;
    }

    public /* synthetic */ j0(StorageManager storageManager, TypeAliasDescriptor typeAliasDescriptor, ClassConstructorDescriptor classConstructorDescriptor, i0 i0Var, qb.g gVar, CallableMemberDescriptor.a aVar, SourceElement sourceElement, DefaultConstructorMarker defaultConstructorMarker) {
        this(storageManager, typeAliasDescriptor, classConstructorDescriptor, i0Var, gVar, aVar, sourceElement);
    }

    @Override // pb.ConstructorDescriptor
    public boolean J() {
        return u0().J();
    }

    @Override // pb.ConstructorDescriptor
    public ClassDescriptor K() {
        ClassDescriptor K = u0().K();
        za.k.d(K, "underlyingConstructorDescriptor.constructedClass");
        return K;
    }

    @Override // sb.FunctionDescriptorImpl, pb.CallableDescriptor
    public gd.g0 f() {
        gd.g0 f10 = super.f();
        za.k.b(f10);
        return f10;
    }

    public final StorageManager o0() {
        return this.I;
    }

    @Override // sb.FunctionDescriptorImpl
    /* renamed from: t1, reason: merged with bridge method [inline-methods] */
    public i0 T0(DeclarationDescriptor declarationDescriptor, Modality modality, pb.u uVar, CallableMemberDescriptor.a aVar, boolean z10) {
        za.k.e(declarationDescriptor, "newOwner");
        za.k.e(modality, "modality");
        za.k.e(uVar, "visibility");
        za.k.e(aVar, "kind");
        FunctionDescriptor build = A().r(declarationDescriptor).f(modality).p(uVar).q(aVar).l(z10).build();
        za.k.c(build, "null cannot be cast to non-null type org.jetbrains.kotlin.descriptors.impl.TypeAliasConstructorDescriptor");
        return (i0) build;
    }

    @Override // sb.i0
    public ClassConstructorDescriptor u0() {
        return this.L;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // sb.FunctionDescriptorImpl
    /* renamed from: u1, reason: merged with bridge method [inline-methods] */
    public j0 U0(DeclarationDescriptor declarationDescriptor, FunctionDescriptor functionDescriptor, CallableMemberDescriptor.a aVar, Name name, qb.g gVar, SourceElement sourceElement) {
        za.k.e(declarationDescriptor, "newOwner");
        za.k.e(aVar, "kind");
        za.k.e(gVar, "annotations");
        za.k.e(sourceElement, "source");
        CallableMemberDescriptor.a aVar2 = CallableMemberDescriptor.a.DECLARATION;
        if (aVar != aVar2) {
            CallableMemberDescriptor.a aVar3 = CallableMemberDescriptor.a.SYNTHESIZED;
        }
        return new j0(this.I, x1(), u0(), this, gVar, aVar2, sourceElement);
    }

    @Override // sb.DeclarationDescriptorNonRootImpl, pb.DeclarationDescriptor
    /* renamed from: v1, reason: merged with bridge method [inline-methods] */
    public TypeAliasDescriptor b() {
        return x1();
    }

    @Override // sb.FunctionDescriptorImpl, sb.DeclarationDescriptorNonRootImpl, sb.DeclarationDescriptorImpl, pb.DeclarationDescriptor
    /* renamed from: w1, reason: merged with bridge method [inline-methods] */
    public i0 T0() {
        FunctionDescriptor T0 = super.T0();
        za.k.c(T0, "null cannot be cast to non-null type org.jetbrains.kotlin.descriptors.impl.TypeAliasConstructorDescriptor");
        return (i0) T0;
    }

    public TypeAliasDescriptor x1() {
        return this.J;
    }

    @Override // sb.FunctionDescriptorImpl, pb.FunctionDescriptor, pb.Substitutable
    /* renamed from: y1, reason: merged with bridge method [inline-methods] */
    public i0 c(TypeSubstitutor typeSubstitutor) {
        za.k.e(typeSubstitutor, "substitutor");
        FunctionDescriptor c10 = super.c(typeSubstitutor);
        za.k.c(c10, "null cannot be cast to non-null type org.jetbrains.kotlin.descriptors.impl.TypeAliasConstructorDescriptorImpl");
        j0 j0Var = (j0) c10;
        TypeSubstitutor f10 = TypeSubstitutor.f(j0Var.f());
        za.k.d(f10, "create(substitutedTypeAliasConstructor.returnType)");
        ClassConstructorDescriptor c11 = u0().T0().c(f10);
        if (c11 == null) {
            return null;
        }
        j0Var.L = c11;
        return j0Var;
    }
}
