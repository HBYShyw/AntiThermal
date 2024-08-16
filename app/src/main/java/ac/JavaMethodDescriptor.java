package ac;

import gd.g0;
import java.util.List;
import java.util.Map;
import ma.o;
import nd.p;
import oc.Name;
import pb.CallableDescriptor;
import pb.CallableMemberDescriptor;
import pb.DeclarationDescriptor;
import pb.FunctionDescriptor;
import pb.Modality;
import pb.ReceiverParameterDescriptor;
import pb.SimpleFunctionDescriptor;
import pb.SourceElement;
import pb.TypeParameterDescriptor;
import pb.ValueParameterDescriptor;
import pb.u;
import sb.SimpleFunctionDescriptorImpl;
import sc.DescriptorFactory;

/* compiled from: JavaMethodDescriptor.java */
/* renamed from: ac.e, reason: use source file name */
/* loaded from: classes2.dex */
public class JavaMethodDescriptor extends SimpleFunctionDescriptorImpl implements JavaCallableMemberDescriptor {
    public static final CallableDescriptor.a<ValueParameterDescriptor> K = new a();
    public static final CallableDescriptor.a<Boolean> L = new b();
    private c I;
    private final boolean J;

    /* compiled from: JavaMethodDescriptor.java */
    /* renamed from: ac.e$a */
    /* loaded from: classes2.dex */
    static class a implements CallableDescriptor.a<ValueParameterDescriptor> {
        a() {
        }
    }

    /* compiled from: JavaMethodDescriptor.java */
    /* renamed from: ac.e$b */
    /* loaded from: classes2.dex */
    static class b implements CallableDescriptor.a<Boolean> {
        b() {
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* compiled from: JavaMethodDescriptor.java */
    /* renamed from: ac.e$c */
    /* loaded from: classes2.dex */
    public enum c {
        NON_STABLE_DECLARED(false, false),
        STABLE_DECLARED(true, false),
        NON_STABLE_SYNTHESIZED(false, true),
        STABLE_SYNTHESIZED(true, true);


        /* renamed from: e, reason: collision with root package name */
        public final boolean f222e;

        /* renamed from: f, reason: collision with root package name */
        public final boolean f223f;

        c(boolean z10, boolean z11) {
            this.f222e = z10;
            this.f223f = z11;
        }

        private static /* synthetic */ void a(int i10) {
            throw new IllegalStateException(String.format("@NotNull method %s.%s must not return null", "kotlin/reflect/jvm/internal/impl/load/java/descriptors/JavaMethodDescriptor$ParameterNamesStatus", "get"));
        }

        public static c b(boolean z10, boolean z11) {
            c cVar = z10 ? z11 ? STABLE_SYNTHESIZED : STABLE_DECLARED : z11 ? NON_STABLE_SYNTHESIZED : NON_STABLE_DECLARED;
            if (cVar == null) {
                a(0);
            }
            return cVar;
        }
    }

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    protected JavaMethodDescriptor(DeclarationDescriptor declarationDescriptor, SimpleFunctionDescriptor simpleFunctionDescriptor, qb.g gVar, Name name, CallableMemberDescriptor.a aVar, SourceElement sourceElement, boolean z10) {
        super(declarationDescriptor, simpleFunctionDescriptor, gVar, name, aVar, sourceElement);
        if (declarationDescriptor == null) {
            P(0);
        }
        if (gVar == null) {
            P(1);
        }
        if (name == null) {
            P(2);
        }
        if (aVar == null) {
            P(3);
        }
        if (sourceElement == null) {
            P(4);
        }
        this.I = null;
        this.J = z10;
    }

    private static /* synthetic */ void P(int i10) {
        String str = (i10 == 13 || i10 == 18 || i10 == 21) ? "@NotNull method %s.%s must not return null" : "Argument for @NotNull parameter '%s' of %s.%s must not be null";
        Object[] objArr = new Object[(i10 == 13 || i10 == 18 || i10 == 21) ? 2 : 3];
        switch (i10) {
            case 1:
            case 6:
            case 16:
                objArr[0] = "annotations";
                break;
            case 2:
            case 7:
                objArr[0] = "name";
                break;
            case 3:
            case 15:
                objArr[0] = "kind";
                break;
            case 4:
            case 8:
            case 17:
                objArr[0] = "source";
                break;
            case 5:
            default:
                objArr[0] = "containingDeclaration";
                break;
            case 9:
                objArr[0] = "contextReceiverParameters";
                break;
            case 10:
                objArr[0] = "typeParameters";
                break;
            case 11:
                objArr[0] = "unsubstitutedValueParameters";
                break;
            case 12:
                objArr[0] = "visibility";
                break;
            case 13:
            case 18:
            case 21:
                objArr[0] = "kotlin/reflect/jvm/internal/impl/load/java/descriptors/JavaMethodDescriptor";
                break;
            case 14:
                objArr[0] = "newOwner";
                break;
            case 19:
                objArr[0] = "enhancedValueParameterTypes";
                break;
            case 20:
                objArr[0] = "enhancedReturnType";
                break;
        }
        if (i10 == 13) {
            objArr[1] = "initialize";
        } else if (i10 == 18) {
            objArr[1] = "createSubstitutedCopy";
        } else if (i10 != 21) {
            objArr[1] = "kotlin/reflect/jvm/internal/impl/load/java/descriptors/JavaMethodDescriptor";
        } else {
            objArr[1] = "enhance";
        }
        switch (i10) {
            case 5:
            case 6:
            case 7:
            case 8:
                objArr[2] = "createJavaMethod";
                break;
            case 9:
            case 10:
            case 11:
            case 12:
                objArr[2] = "initialize";
                break;
            case 13:
            case 18:
            case 21:
                break;
            case 14:
            case 15:
            case 16:
            case 17:
                objArr[2] = "createSubstitutedCopy";
                break;
            case 19:
            case 20:
                objArr[2] = "enhance";
                break;
            default:
                objArr[2] = "<init>";
                break;
        }
        String format = String.format(str, objArr);
        if (i10 != 13 && i10 != 18 && i10 != 21) {
            throw new IllegalArgumentException(format);
        }
        throw new IllegalStateException(format);
    }

    public static JavaMethodDescriptor y1(DeclarationDescriptor declarationDescriptor, qb.g gVar, Name name, SourceElement sourceElement, boolean z10) {
        if (declarationDescriptor == null) {
            P(5);
        }
        if (gVar == null) {
            P(6);
        }
        if (name == null) {
            P(7);
        }
        if (sourceElement == null) {
            P(8);
        }
        return new JavaMethodDescriptor(declarationDescriptor, null, gVar, name, CallableMemberDescriptor.a.DECLARATION, sourceElement, z10);
    }

    @Override // ac.JavaCallableMemberDescriptor
    /* renamed from: A1, reason: merged with bridge method [inline-methods] */
    public JavaMethodDescriptor G(g0 g0Var, List<g0> list, g0 g0Var2, o<CallableDescriptor.a<?>, ?> oVar) {
        if (list == null) {
            P(19);
        }
        if (g0Var2 == null) {
            P(20);
        }
        JavaMethodDescriptor javaMethodDescriptor = (JavaMethodDescriptor) A().c(h.a(list, l(), this)).e(g0Var2).j(g0Var == null ? null : DescriptorFactory.i(this, g0Var, qb.g.f17195b.b())).a().h().build();
        if (oVar != null) {
            javaMethodDescriptor.d1(oVar.c(), oVar.d());
        }
        if (javaMethodDescriptor == null) {
            P(21);
        }
        return javaMethodDescriptor;
    }

    public void B1(boolean z10, boolean z11) {
        this.I = c.b(z10, z11);
    }

    @Override // sb.FunctionDescriptorImpl, pb.CallableDescriptor
    public boolean O() {
        return this.I.f223f;
    }

    @Override // sb.FunctionDescriptorImpl
    public boolean Z0() {
        return this.I.f222e;
    }

    @Override // sb.SimpleFunctionDescriptorImpl
    public SimpleFunctionDescriptorImpl x1(ReceiverParameterDescriptor receiverParameterDescriptor, ReceiverParameterDescriptor receiverParameterDescriptor2, List<ReceiverParameterDescriptor> list, List<? extends TypeParameterDescriptor> list2, List<ValueParameterDescriptor> list3, g0 g0Var, Modality modality, u uVar, Map<? extends CallableDescriptor.a<?>, ?> map) {
        if (list == null) {
            P(9);
        }
        if (list2 == null) {
            P(10);
        }
        if (list3 == null) {
            P(11);
        }
        if (uVar == null) {
            P(12);
        }
        SimpleFunctionDescriptorImpl x12 = super.x1(receiverParameterDescriptor, receiverParameterDescriptor2, list, list2, list3, g0Var, modality, uVar, map);
        o1(p.f16041a.a(x12).a());
        if (x12 == null) {
            P(13);
        }
        return x12;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // sb.SimpleFunctionDescriptorImpl, sb.FunctionDescriptorImpl
    /* renamed from: z1, reason: merged with bridge method [inline-methods] */
    public JavaMethodDescriptor U0(DeclarationDescriptor declarationDescriptor, FunctionDescriptor functionDescriptor, CallableMemberDescriptor.a aVar, Name name, qb.g gVar, SourceElement sourceElement) {
        if (declarationDescriptor == null) {
            P(14);
        }
        if (aVar == null) {
            P(15);
        }
        if (gVar == null) {
            P(16);
        }
        if (sourceElement == null) {
            P(17);
        }
        SimpleFunctionDescriptor simpleFunctionDescriptor = (SimpleFunctionDescriptor) functionDescriptor;
        if (name == null) {
            name = getName();
        }
        JavaMethodDescriptor javaMethodDescriptor = new JavaMethodDescriptor(declarationDescriptor, simpleFunctionDescriptor, gVar, name, aVar, sourceElement, this.J);
        javaMethodDescriptor.B1(Z0(), O());
        return javaMethodDescriptor;
    }
}
