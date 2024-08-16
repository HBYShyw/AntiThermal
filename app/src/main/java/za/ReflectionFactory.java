package za;

import gb.KClass;
import gb.KDeclarationContainer;
import gb.KFunction;

/* compiled from: ReflectionFactory.java */
/* renamed from: za.a0, reason: use source file name */
/* loaded from: classes2.dex */
public class ReflectionFactory {
    public KFunction a(FunctionReference functionReference) {
        return functionReference;
    }

    public KClass b(Class cls) {
        return new ClassReference(cls);
    }

    public KDeclarationContainer c(Class cls, String str) {
        return new PackageReference(cls, str);
    }

    public gb.j d(MutablePropertyReference1 mutablePropertyReference1) {
        return mutablePropertyReference1;
    }

    public gb.m e(PropertyReference0 propertyReference0) {
        return propertyReference0;
    }

    public gb.n f(PropertyReference1 propertyReference1) {
        return propertyReference1;
    }

    public String g(FunctionBase functionBase) {
        String obj = functionBase.getClass().getGenericInterfaces()[0].toString();
        return obj.startsWith("kotlin.jvm.functions.") ? obj.substring(21) : obj;
    }

    public String h(Lambda lambda) {
        return g(lambda);
    }
}
