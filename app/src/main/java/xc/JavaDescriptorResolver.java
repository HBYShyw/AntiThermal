package xc;

import bc.LazyJavaPackageFragmentProvider;
import cc.LazyJavaPackageFragment;
import fc.d0;
import fc.g;
import kotlin.collections._Collections;
import oc.FqName;
import pb.ClassDescriptor;
import pb.ClassifierDescriptor;
import za.k;
import zb.JavaResolverCache;
import zc.h;

/* compiled from: JavaDescriptorResolver.kt */
/* renamed from: xc.c, reason: use source file name */
/* loaded from: classes2.dex */
public final class JavaDescriptorResolver {

    /* renamed from: a, reason: collision with root package name */
    private final LazyJavaPackageFragmentProvider f19696a;

    /* renamed from: b, reason: collision with root package name */
    private final JavaResolverCache f19697b;

    public JavaDescriptorResolver(LazyJavaPackageFragmentProvider lazyJavaPackageFragmentProvider, JavaResolverCache javaResolverCache) {
        k.e(lazyJavaPackageFragmentProvider, "packageFragmentProvider");
        k.e(javaResolverCache, "javaResolverCache");
        this.f19696a = lazyJavaPackageFragmentProvider;
        this.f19697b = javaResolverCache;
    }

    public final LazyJavaPackageFragmentProvider a() {
        return this.f19696a;
    }

    public final ClassDescriptor b(g gVar) {
        Object V;
        k.e(gVar, "javaClass");
        FqName d10 = gVar.d();
        if (d10 != null && gVar.O() == d0.SOURCE) {
            return this.f19697b.d(d10);
        }
        g u7 = gVar.u();
        if (u7 != null) {
            ClassDescriptor b10 = b(u7);
            h F0 = b10 != null ? b10.F0() : null;
            ClassifierDescriptor e10 = F0 != null ? F0.e(gVar.getName(), xb.d.FROM_JAVA_LOADER) : null;
            if (e10 instanceof ClassDescriptor) {
                return (ClassDescriptor) e10;
            }
            return null;
        }
        if (d10 == null) {
            return null;
        }
        LazyJavaPackageFragmentProvider lazyJavaPackageFragmentProvider = this.f19696a;
        FqName e11 = d10.e();
        k.d(e11, "fqName.parent()");
        V = _Collections.V(lazyJavaPackageFragmentProvider.b(e11));
        LazyJavaPackageFragment lazyJavaPackageFragment = (LazyJavaPackageFragment) V;
        if (lazyJavaPackageFragment != null) {
            return lazyJavaPackageFragment.U0(gVar);
        }
        return null;
    }
}
