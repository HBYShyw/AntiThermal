package gd;

import id.ErrorUtils;
import pb.ClassifierDescriptor;
import pb.DeclarationDescriptor;
import pb.ModuleDescriptor;
import pb.PackageFragmentDescriptor;

/* compiled from: ClassifierBasedTypeConstructor.kt */
/* renamed from: gd.m, reason: use source file name */
/* loaded from: classes2.dex */
public abstract class ClassifierBasedTypeConstructor implements TypeConstructor {

    /* renamed from: a, reason: collision with root package name */
    private int f11853a;

    private final boolean d(ClassifierDescriptor classifierDescriptor) {
        return (ErrorUtils.m(classifierDescriptor) || sc.e.E(classifierDescriptor)) ? false : true;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public final boolean c(ClassifierDescriptor classifierDescriptor, ClassifierDescriptor classifierDescriptor2) {
        za.k.e(classifierDescriptor, "first");
        za.k.e(classifierDescriptor2, "second");
        if (!za.k.a(classifierDescriptor.getName(), classifierDescriptor2.getName())) {
            return false;
        }
        DeclarationDescriptor b10 = classifierDescriptor.b();
        for (DeclarationDescriptor b11 = classifierDescriptor2.b(); b10 != null && b11 != null; b11 = b11.b()) {
            if (b10 instanceof ModuleDescriptor) {
                return b11 instanceof ModuleDescriptor;
            }
            if (b11 instanceof ModuleDescriptor) {
                return false;
            }
            if (b10 instanceof PackageFragmentDescriptor) {
                return (b11 instanceof PackageFragmentDescriptor) && za.k.a(((PackageFragmentDescriptor) b10).d(), ((PackageFragmentDescriptor) b11).d());
            }
            if ((b11 instanceof PackageFragmentDescriptor) || !za.k.a(b10.getName(), b11.getName())) {
                return false;
            }
            b10 = b10.b();
        }
        return true;
    }

    protected abstract boolean e(ClassifierDescriptor classifierDescriptor);

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof TypeConstructor) || obj.hashCode() != hashCode()) {
            return false;
        }
        TypeConstructor typeConstructor = (TypeConstructor) obj;
        if (typeConstructor.getParameters().size() != getParameters().size()) {
            return false;
        }
        ClassifierDescriptor v7 = v();
        ClassifierDescriptor v10 = typeConstructor.v();
        if (v10 != null && d(v7) && d(v10)) {
            return e(v10);
        }
        return false;
    }

    public int hashCode() {
        int identityHashCode;
        int i10 = this.f11853a;
        if (i10 != 0) {
            return i10;
        }
        ClassifierDescriptor v7 = v();
        if (d(v7)) {
            identityHashCode = sc.e.m(v7).hashCode();
        } else {
            identityHashCode = System.identityHashCode(this);
        }
        this.f11853a = identityHashCode;
        return identityHashCode;
    }

    @Override // gd.TypeConstructor
    public abstract ClassifierDescriptor v();
}
