package za;

import gb.KClass;
import gb.KDeclarationContainer;
import gb.KFunction;
import jb.ReflectionFactoryImpl;

/* compiled from: Reflection.java */
/* renamed from: za.z, reason: use source file name */
/* loaded from: classes2.dex */
public class Reflection {

    /* renamed from: a, reason: collision with root package name */
    private static final ReflectionFactory f20377a;

    /* renamed from: b, reason: collision with root package name */
    private static final KClass[] f20378b;

    static {
        ReflectionFactory reflectionFactory = null;
        try {
            reflectionFactory = (ReflectionFactory) ReflectionFactoryImpl.class.newInstance();
        } catch (ClassCastException | ClassNotFoundException | IllegalAccessException | InstantiationException unused) {
        }
        if (reflectionFactory == null) {
            reflectionFactory = new ReflectionFactory();
        }
        f20377a = reflectionFactory;
        f20378b = new KClass[0];
    }

    public static KFunction a(FunctionReference functionReference) {
        return f20377a.a(functionReference);
    }

    public static KClass b(Class cls) {
        return f20377a.b(cls);
    }

    public static KDeclarationContainer c(Class cls) {
        return f20377a.c(cls, "");
    }

    public static KDeclarationContainer d(Class cls, String str) {
        return f20377a.c(cls, str);
    }

    public static gb.j e(MutablePropertyReference1 mutablePropertyReference1) {
        return f20377a.d(mutablePropertyReference1);
    }

    public static gb.m f(PropertyReference0 propertyReference0) {
        return f20377a.e(propertyReference0);
    }

    public static gb.n g(PropertyReference1 propertyReference1) {
        return f20377a.f(propertyReference1);
    }

    public static String h(FunctionBase functionBase) {
        return f20377a.g(functionBase);
    }

    public static String i(Lambda lambda) {
        return f20377a.h(lambda);
    }
}
