package ub;

import dd.BuiltInSerializerProtocol;
import dd.BuiltInsResourceLoader;
import hc.p;
import java.io.InputStream;
import mb.StandardNames;
import oc.ClassId;
import oc.FqName;

/* compiled from: ReflectKotlinClassFinder.kt */
/* loaded from: classes2.dex */
public final class g implements p {

    /* renamed from: a, reason: collision with root package name */
    private final ClassLoader f18977a;

    /* renamed from: b, reason: collision with root package name */
    private final BuiltInsResourceLoader f18978b;

    public g(ClassLoader classLoader) {
        za.k.e(classLoader, "classLoader");
        this.f18977a = classLoader;
        this.f18978b = new BuiltInsResourceLoader();
    }

    private final p.a d(String str) {
        f a10;
        Class<?> a11 = e.a(this.f18977a, str);
        if (a11 == null || (a10 = f.f18974c.a(a11)) == null) {
            return null;
        }
        return new p.a.b(a10, null, 2, null);
    }

    @Override // cd.KotlinMetadataFinder
    public InputStream a(FqName fqName) {
        za.k.e(fqName, "packageFqName");
        if (fqName.i(StandardNames.f15282t)) {
            return this.f18978b.a(BuiltInSerializerProtocol.f10926n.n(fqName));
        }
        return null;
    }

    @Override // hc.p
    public p.a b(ClassId classId) {
        String b10;
        za.k.e(classId, "classId");
        b10 = h.b(classId);
        return d(b10);
    }

    @Override // hc.p
    public p.a c(fc.g gVar) {
        String b10;
        za.k.e(gVar, "javaClass");
        FqName d10 = gVar.d();
        if (d10 == null || (b10 = d10.b()) == null) {
            return null;
        }
        return d(b10);
    }
}
