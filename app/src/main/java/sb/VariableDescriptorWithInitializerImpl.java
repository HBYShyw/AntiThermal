package sb;

import oc.Name;
import pb.DeclarationDescriptor;
import pb.SourceElement;

/* compiled from: VariableDescriptorWithInitializerImpl.java */
/* renamed from: sb.n0, reason: use source file name */
/* loaded from: classes2.dex */
public abstract class VariableDescriptorWithInitializerImpl extends VariableDescriptorImpl {

    /* renamed from: j, reason: collision with root package name */
    private final boolean f18322j;

    /* renamed from: k, reason: collision with root package name */
    protected fd.j<uc.g<?>> f18323k;

    /* renamed from: l, reason: collision with root package name */
    protected ya.a<fd.j<uc.g<?>>> f18324l;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public VariableDescriptorWithInitializerImpl(DeclarationDescriptor declarationDescriptor, qb.g gVar, Name name, gd.g0 g0Var, boolean z10, SourceElement sourceElement) {
        super(declarationDescriptor, gVar, name, g0Var, sourceElement);
        if (declarationDescriptor == null) {
            P(0);
        }
        if (gVar == null) {
            P(1);
        }
        if (name == null) {
            P(2);
        }
        if (sourceElement == null) {
            P(3);
        }
        this.f18322j = z10;
    }

    private static /* synthetic */ void P(int i10) {
        Object[] objArr = new Object[3];
        if (i10 == 1) {
            objArr[0] = "annotations";
        } else if (i10 == 2) {
            objArr[0] = "name";
        } else if (i10 == 3) {
            objArr[0] = "source";
        } else if (i10 == 4 || i10 == 5) {
            objArr[0] = "compileTimeInitializerFactory";
        } else {
            objArr[0] = "containingDeclaration";
        }
        objArr[1] = "kotlin/reflect/jvm/internal/impl/descriptors/impl/VariableDescriptorWithInitializerImpl";
        if (i10 == 4) {
            objArr[2] = "setCompileTimeInitializerFactory";
        } else if (i10 != 5) {
            objArr[2] = "<init>";
        } else {
            objArr[2] = "setCompileTimeInitializer";
        }
        throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", objArr));
    }

    public void T0(fd.j<uc.g<?>> jVar, ya.a<fd.j<uc.g<?>>> aVar) {
        if (aVar == null) {
            P(5);
        }
        this.f18324l = aVar;
        if (jVar == null) {
            jVar = aVar.invoke();
        }
        this.f18323k = jVar;
    }

    public void U0(ya.a<fd.j<uc.g<?>>> aVar) {
        if (aVar == null) {
            P(4);
        }
        T0(null, aVar);
    }

    @Override // pb.VariableDescriptor
    public uc.g<?> f0() {
        fd.j<uc.g<?>> jVar = this.f18323k;
        if (jVar != null) {
            return jVar.invoke();
        }
        return null;
    }

    @Override // pb.VariableDescriptor
    public boolean p0() {
        return this.f18322j;
    }
}
