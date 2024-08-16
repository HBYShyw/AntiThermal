package ub;

import fc.u;
import java.util.Set;
import oc.ClassId;
import oc.FqName;
import sd.StringsJVM;
import vb.ReflectJavaPackage;
import yb.JavaClassFinder;

/* compiled from: ReflectJavaClassFinder.kt */
/* loaded from: classes2.dex */
public final class d implements JavaClassFinder {

    /* renamed from: a, reason: collision with root package name */
    private final ClassLoader f18973a;

    public d(ClassLoader classLoader) {
        za.k.e(classLoader, "classLoader");
        this.f18973a = classLoader;
    }

    @Override // yb.JavaClassFinder
    public fc.g a(JavaClassFinder.a aVar) {
        String y4;
        za.k.e(aVar, "request");
        ClassId a10 = aVar.a();
        FqName h10 = a10.h();
        za.k.d(h10, "classId.packageFqName");
        String b10 = a10.i().b();
        za.k.d(b10, "classId.relativeClassName.asString()");
        y4 = StringsJVM.y(b10, '.', '$', false, 4, null);
        if (!h10.d()) {
            y4 = h10.b() + '.' + y4;
        }
        Class<?> a11 = e.a(this.f18973a, y4);
        if (a11 != null) {
            return new vb.l(a11);
        }
        return null;
    }

    @Override // yb.JavaClassFinder
    public u b(FqName fqName, boolean z10) {
        za.k.e(fqName, "fqName");
        return new ReflectJavaPackage(fqName);
    }

    @Override // yb.JavaClassFinder
    public Set<String> c(FqName fqName) {
        za.k.e(fqName, "packageFqName");
        return null;
    }
}
