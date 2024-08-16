package vb;

import java.util.Collection;
import java.util.List;
import oc.FqName;
import oc.Name;

/* compiled from: ReflectJavaPackage.kt */
/* renamed from: vb.w, reason: use source file name */
/* loaded from: classes2.dex */
public final class ReflectJavaPackage extends ReflectJavaElement implements fc.u {

    /* renamed from: a, reason: collision with root package name */
    private final FqName f19257a;

    public ReflectJavaPackage(FqName fqName) {
        za.k.e(fqName, "fqName");
        this.f19257a = fqName;
    }

    @Override // fc.u
    public Collection<fc.u> H() {
        List j10;
        j10 = kotlin.collections.r.j();
        return j10;
    }

    @Override // fc.u
    public FqName d() {
        return this.f19257a;
    }

    public boolean equals(Object obj) {
        return (obj instanceof ReflectJavaPackage) && za.k.a(d(), ((ReflectJavaPackage) obj).d());
    }

    public int hashCode() {
        return d().hashCode();
    }

    @Override // fc.d
    public fc.a j(FqName fqName) {
        za.k.e(fqName, "fqName");
        return null;
    }

    @Override // fc.d
    public boolean k() {
        return false;
    }

    @Override // fc.u
    public Collection<fc.g> s(ya.l<? super Name, Boolean> lVar) {
        List j10;
        za.k.e(lVar, "nameFilter");
        j10 = kotlin.collections.r.j();
        return j10;
    }

    public String toString() {
        return ReflectJavaPackage.class.getName() + ": " + d();
    }

    @Override // fc.d
    public List<fc.a> i() {
        List<fc.a> j10;
        j10 = kotlin.collections.r.j();
        return j10;
    }
}
