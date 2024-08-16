package sc;

import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import kotlin.collections._Collections;
import kotlin.collections.r;
import oc.Name;
import pb.ClassDescriptor;
import pb.ClassifierDescriptor;
import pb.DeclarationDescriptor;
import pb.Modality;
import pb.PackageFragmentDescriptor;
import pb.TypeAliasDescriptor;
import zc.ResolutionScope;

/* compiled from: SealedClassInheritorsProvider.kt */
/* loaded from: classes2.dex */
public final class a extends p {

    /* renamed from: a, reason: collision with root package name */
    public static final a f18417a = new a();

    /* compiled from: Comparisons.kt */
    /* renamed from: sc.a$a, reason: collision with other inner class name */
    /* loaded from: classes2.dex */
    public static final class C0103a<T> implements Comparator {
        @Override // java.util.Comparator
        public final int compare(T t7, T t10) {
            int a10;
            a10 = pa.b.a(wc.c.l((ClassDescriptor) t7).b(), wc.c.l((ClassDescriptor) t10).b());
            return a10;
        }
    }

    private a() {
    }

    private static final void b(ClassDescriptor classDescriptor, LinkedHashSet<ClassDescriptor> linkedHashSet, zc.h hVar, boolean z10) {
        for (DeclarationDescriptor declarationDescriptor : ResolutionScope.a.a(hVar, zc.d.f20441t, null, 2, null)) {
            if (declarationDescriptor instanceof ClassDescriptor) {
                ClassDescriptor classDescriptor2 = (ClassDescriptor) declarationDescriptor;
                if (classDescriptor2.U()) {
                    Name name = classDescriptor2.getName();
                    za.k.d(name, "descriptor.name");
                    ClassifierDescriptor e10 = hVar.e(name, xb.d.WHEN_GET_ALL_DESCRIPTORS);
                    if (e10 instanceof ClassDescriptor) {
                        classDescriptor2 = (ClassDescriptor) e10;
                    } else {
                        classDescriptor2 = e10 instanceof TypeAliasDescriptor ? ((TypeAliasDescriptor) e10).w() : null;
                    }
                }
                if (classDescriptor2 != null) {
                    if (e.z(classDescriptor2, classDescriptor)) {
                        linkedHashSet.add(classDescriptor2);
                    }
                    if (z10) {
                        zc.h F0 = classDescriptor2.F0();
                        za.k.d(F0, "refinedDescriptor.unsubstitutedInnerClassesScope");
                        b(classDescriptor, linkedHashSet, F0, z10);
                    }
                }
            }
        }
    }

    public Collection<ClassDescriptor> a(ClassDescriptor classDescriptor, boolean z10) {
        DeclarationDescriptor declarationDescriptor;
        DeclarationDescriptor declarationDescriptor2;
        List u02;
        List j10;
        za.k.e(classDescriptor, "sealedClass");
        if (classDescriptor.o() != Modality.SEALED) {
            j10 = r.j();
            return j10;
        }
        LinkedHashSet linkedHashSet = new LinkedHashSet();
        if (!z10) {
            declarationDescriptor2 = classDescriptor.b();
        } else {
            Iterator<DeclarationDescriptor> it = wc.c.q(classDescriptor).iterator();
            while (true) {
                if (!it.hasNext()) {
                    declarationDescriptor = null;
                    break;
                }
                declarationDescriptor = it.next();
                if (declarationDescriptor instanceof PackageFragmentDescriptor) {
                    break;
                }
            }
            declarationDescriptor2 = declarationDescriptor;
        }
        if (declarationDescriptor2 instanceof PackageFragmentDescriptor) {
            b(classDescriptor, linkedHashSet, ((PackageFragmentDescriptor) declarationDescriptor2).u(), z10);
        }
        zc.h F0 = classDescriptor.F0();
        za.k.d(F0, "sealedClass.unsubstitutedInnerClassesScope");
        b(classDescriptor, linkedHashSet, F0, true);
        u02 = _Collections.u0(linkedHashSet, new C0103a());
        return u02;
    }
}
