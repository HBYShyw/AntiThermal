package jb;

import gb.KClass;
import gb.KDeclarationContainer;
import gb.KFunction;
import ib.reflectLambda;
import za.CallableReference;
import za.FunctionBase;
import za.FunctionReference;
import za.Lambda;
import za.MutablePropertyReference1;
import za.PropertyReference0;
import za.PropertyReference1;
import za.ReflectionFactory;

/* compiled from: ReflectionFactoryImpl.java */
/* renamed from: jb.j0, reason: use source file name */
/* loaded from: classes2.dex */
public class ReflectionFactoryImpl extends ReflectionFactory {
    private static KDeclarationContainerImpl i(CallableReference callableReference) {
        KDeclarationContainer C = callableReference.C();
        return C instanceof KDeclarationContainerImpl ? (KDeclarationContainerImpl) C : EmptyContainerForLocal.f13191h;
    }

    @Override // za.ReflectionFactory
    public KFunction a(FunctionReference functionReference) {
        return new KFunctionImpl(i(functionReference), functionReference.getName(), functionReference.E(), functionReference.B());
    }

    @Override // za.ReflectionFactory
    public KClass b(Class cls) {
        return caches.a(cls);
    }

    @Override // za.ReflectionFactory
    public KDeclarationContainer c(Class cls, String str) {
        return caches.b(cls);
    }

    @Override // za.ReflectionFactory
    public gb.j d(MutablePropertyReference1 mutablePropertyReference1) {
        return new r(i(mutablePropertyReference1), mutablePropertyReference1.getName(), mutablePropertyReference1.E(), mutablePropertyReference1.B());
    }

    @Override // za.ReflectionFactory
    public gb.m e(PropertyReference0 propertyReference0) {
        return new w(i(propertyReference0), propertyReference0.getName(), propertyReference0.E(), propertyReference0.B());
    }

    @Override // za.ReflectionFactory
    public gb.n f(PropertyReference1 propertyReference1) {
        return new x(i(propertyReference1), propertyReference1.getName(), propertyReference1.E(), propertyReference1.B());
    }

    @Override // za.ReflectionFactory
    public String g(FunctionBase functionBase) {
        KFunctionImpl c10;
        KFunction a10 = reflectLambda.a(functionBase);
        if (a10 != null && (c10 = o0.c(a10)) != null) {
            return ReflectionObjectRenderer.f13232a.e(c10.L());
        }
        return super.g(functionBase);
    }

    @Override // za.ReflectionFactory
    public String h(Lambda lambda) {
        return g(lambda);
    }
}
