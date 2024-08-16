package cd;

import oc.ClassId;
import oc.FqName;
import pb.PackageFragmentDescriptor;
import pb.m0;
import pb.o0;

/* compiled from: DeserializedClassDataFinder.kt */
/* renamed from: cd.o, reason: use source file name */
/* loaded from: classes2.dex */
public final class DeserializedClassDataFinder implements ClassDataFinder {

    /* renamed from: a, reason: collision with root package name */
    private final m0 f5275a;

    public DeserializedClassDataFinder(m0 m0Var) {
        za.k.e(m0Var, "packageFragmentProvider");
        this.f5275a = m0Var;
    }

    @Override // cd.ClassDataFinder
    public ClassData a(ClassId classId) {
        ClassData a10;
        za.k.e(classId, "classId");
        m0 m0Var = this.f5275a;
        FqName h10 = classId.h();
        za.k.d(h10, "classId.packageFqName");
        for (PackageFragmentDescriptor packageFragmentDescriptor : o0.c(m0Var, h10)) {
            if ((packageFragmentDescriptor instanceof DeserializedPackageFragment) && (a10 = ((DeserializedPackageFragment) packageFragmentDescriptor).O0().a(classId)) != null) {
                return a10;
            }
        }
        return null;
    }
}
