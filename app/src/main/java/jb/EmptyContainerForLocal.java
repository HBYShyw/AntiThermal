package jb;

import java.util.Collection;
import oc.Name;
import pb.ConstructorDescriptor;
import pb.FunctionDescriptor;
import pb.PropertyDescriptor;

/* compiled from: EmptyContainerForLocal.kt */
/* renamed from: jb.g, reason: use source file name */
/* loaded from: classes2.dex */
public final class EmptyContainerForLocal extends KDeclarationContainerImpl {

    /* renamed from: h, reason: collision with root package name */
    public static final EmptyContainerForLocal f13191h = new EmptyContainerForLocal();

    private EmptyContainerForLocal() {
    }

    private final Void U() {
        throw new KotlinReflectionInternalError("Introspecting local functions, lambdas, anonymous functions, local variables and typealiases is not yet fully supported in Kotlin reflection");
    }

    @Override // jb.KDeclarationContainerImpl
    public Collection<ConstructorDescriptor> I() {
        U();
        throw null;
    }

    @Override // jb.KDeclarationContainerImpl
    public Collection<FunctionDescriptor> J(Name name) {
        za.k.e(name, "name");
        U();
        throw null;
    }

    @Override // jb.KDeclarationContainerImpl
    public PropertyDescriptor K(int i10) {
        return null;
    }

    @Override // jb.KDeclarationContainerImpl
    public Collection<PropertyDescriptor> N(Name name) {
        za.k.e(name, "name");
        U();
        throw null;
    }

    @Override // za.ClassBasedDeclarationContainer
    public Class<?> e() {
        U();
        throw null;
    }
}
