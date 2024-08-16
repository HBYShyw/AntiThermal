package wb;

import oc.Name;
import pb.ClassDescriptor;
import pb.PackageFragmentDescriptor;
import xb.b;
import xb.c;
import xb.e;
import xb.f;
import za.k;

/* compiled from: utils.kt */
/* loaded from: classes2.dex */
public final class a {
    public static final void a(c cVar, b bVar, ClassDescriptor classDescriptor, Name name) {
        xb.a a10;
        k.e(cVar, "<this>");
        k.e(bVar, "from");
        k.e(classDescriptor, "scopeOwner");
        k.e(name, "name");
        if (cVar == c.a.f19665a || (a10 = bVar.a()) == null) {
            return;
        }
        e position = cVar.a() ? a10.getPosition() : e.f19688g.a();
        String a11 = a10.a();
        String b10 = sc.e.m(classDescriptor).b();
        k.d(b10, "getFqName(scopeOwner).asString()");
        f fVar = f.CLASSIFIER;
        String b11 = name.b();
        k.d(b11, "name.asString()");
        cVar.b(a11, position, b10, fVar, b11);
    }

    public static final void b(c cVar, b bVar, PackageFragmentDescriptor packageFragmentDescriptor, Name name) {
        k.e(cVar, "<this>");
        k.e(bVar, "from");
        k.e(packageFragmentDescriptor, "scopeOwner");
        k.e(name, "name");
        String b10 = packageFragmentDescriptor.d().b();
        k.d(b10, "scopeOwner.fqName.asString()");
        String b11 = name.b();
        k.d(b11, "name.asString()");
        c(cVar, bVar, b10, b11);
    }

    public static final void c(c cVar, b bVar, String str, String str2) {
        xb.a a10;
        k.e(cVar, "<this>");
        k.e(bVar, "from");
        k.e(str, "packageFqName");
        k.e(str2, "name");
        if (cVar == c.a.f19665a || (a10 = bVar.a()) == null) {
            return;
        }
        cVar.b(a10.a(), cVar.a() ? a10.getPosition() : e.f19688g.a(), str, f.PACKAGE, str2);
    }
}
