package nb;

import fd.StorageManager;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import kotlin.collections._Collections;
import kotlin.collections.s0;
import mb.BuiltInsPackageFragment;
import mb.FunctionInterfacePackageFragment;
import nb.FunctionClassKind;
import oc.ClassId;
import oc.FqName;
import oc.Name;
import pb.ClassDescriptor;
import pb.ModuleDescriptor;
import pb.PackageFragmentDescriptor;
import rb.ClassDescriptorFactory;
import sd.StringsJVM;
import sd.v;
import za.k;

/* compiled from: BuiltInFictitiousFunctionClassFactory.kt */
/* renamed from: nb.a, reason: use source file name */
/* loaded from: classes2.dex */
public final class BuiltInFictitiousFunctionClassFactory implements ClassDescriptorFactory {

    /* renamed from: a, reason: collision with root package name */
    private final StorageManager f15954a;

    /* renamed from: b, reason: collision with root package name */
    private final ModuleDescriptor f15955b;

    public BuiltInFictitiousFunctionClassFactory(StorageManager storageManager, ModuleDescriptor moduleDescriptor) {
        k.e(storageManager, "storageManager");
        k.e(moduleDescriptor, "module");
        this.f15954a = storageManager;
        this.f15955b = moduleDescriptor;
    }

    @Override // rb.ClassDescriptorFactory
    public ClassDescriptor a(ClassId classId) {
        boolean I;
        Object V;
        Object T;
        k.e(classId, "classId");
        if (classId.k() || classId.l()) {
            return null;
        }
        String b10 = classId.i().b();
        k.d(b10, "classId.relativeClassName.asString()");
        I = v.I(b10, "Function", false, 2, null);
        if (!I) {
            return null;
        }
        FqName h10 = classId.h();
        k.d(h10, "classId.packageFqName");
        FunctionClassKind.a.C0082a c10 = FunctionClassKind.f15968i.c(b10, h10);
        if (c10 == null) {
            return null;
        }
        FunctionClassKind a10 = c10.a();
        int b11 = c10.b();
        List<PackageFragmentDescriptor> R = this.f15955b.H(h10).R();
        ArrayList arrayList = new ArrayList();
        for (Object obj : R) {
            if (obj instanceof BuiltInsPackageFragment) {
                arrayList.add(obj);
            }
        }
        ArrayList arrayList2 = new ArrayList();
        for (Object obj2 : arrayList) {
            if (obj2 instanceof FunctionInterfacePackageFragment) {
                arrayList2.add(obj2);
            }
        }
        V = _Collections.V(arrayList2);
        PackageFragmentDescriptor packageFragmentDescriptor = (FunctionInterfacePackageFragment) V;
        if (packageFragmentDescriptor == null) {
            T = _Collections.T(arrayList);
            packageFragmentDescriptor = (BuiltInsPackageFragment) T;
        }
        return new FunctionClassDescriptor(this.f15954a, packageFragmentDescriptor, a10, b11);
    }

    @Override // rb.ClassDescriptorFactory
    public boolean b(FqName fqName, Name name) {
        boolean D;
        boolean D2;
        boolean D3;
        boolean D4;
        k.e(fqName, "packageFqName");
        k.e(name, "name");
        String b10 = name.b();
        k.d(b10, "name.asString()");
        D = StringsJVM.D(b10, "Function", false, 2, null);
        if (!D) {
            D2 = StringsJVM.D(b10, "KFunction", false, 2, null);
            if (!D2) {
                D3 = StringsJVM.D(b10, "SuspendFunction", false, 2, null);
                if (!D3) {
                    D4 = StringsJVM.D(b10, "KSuspendFunction", false, 2, null);
                    if (!D4) {
                        return false;
                    }
                }
            }
        }
        return FunctionClassKind.f15968i.c(b10, fqName) != null;
    }

    @Override // rb.ClassDescriptorFactory
    public Collection<ClassDescriptor> c(FqName fqName) {
        Set e10;
        k.e(fqName, "packageFqName");
        e10 = s0.e();
        return e10;
    }
}
