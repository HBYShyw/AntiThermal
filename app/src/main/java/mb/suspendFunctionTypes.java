package mb;

import fd.LockBasedStorageManager;
import fd.StorageManager;
import gd.TypeConstructor;
import gd.TypeProjection;
import gd.Variance;
import gd.c1;
import gd.g0;
import gd.h0;
import gd.o0;
import id.ErrorUtils;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import kotlin.collections.CollectionsJVM;
import kotlin.collections._Collections;
import kotlin.collections.s;
import oc.Name;
import pb.ClassKind;
import pb.DescriptorVisibilities;
import pb.Modality;
import pb.SourceElement;
import pb.TypeParameterDescriptor;
import sb.EmptyPackageFragmentDesciptor;
import sb.MutableClassDescriptor;
import sb.TypeParameterDescriptorImpl;

/* compiled from: suspendFunctionTypes.kt */
/* renamed from: mb.l, reason: use source file name */
/* loaded from: classes2.dex */
public final class suspendFunctionTypes {

    /* renamed from: a, reason: collision with root package name */
    private static final MutableClassDescriptor f15341a;

    static {
        List<TypeParameterDescriptor> e10;
        EmptyPackageFragmentDesciptor emptyPackageFragmentDesciptor = new EmptyPackageFragmentDesciptor(ErrorUtils.f12833a.i(), StandardNames.f15275m);
        ClassKind classKind = ClassKind.INTERFACE;
        Name g6 = StandardNames.f15278p.g();
        SourceElement sourceElement = SourceElement.f16664a;
        StorageManager storageManager = LockBasedStorageManager.f11424e;
        MutableClassDescriptor mutableClassDescriptor = new MutableClassDescriptor(emptyPackageFragmentDesciptor, classKind, false, false, g6, sourceElement, storageManager);
        mutableClassDescriptor.V0(Modality.ABSTRACT);
        mutableClassDescriptor.X0(DescriptorVisibilities.f16733e);
        e10 = CollectionsJVM.e(TypeParameterDescriptorImpl.a1(mutableClassDescriptor, qb.g.f17195b.b(), false, Variance.IN_VARIANCE, Name.f("T"), 0, storageManager));
        mutableClassDescriptor.W0(e10);
        mutableClassDescriptor.T0();
        f15341a = mutableClassDescriptor;
    }

    public static final o0 a(g0 g0Var) {
        int u7;
        List e10;
        List n02;
        o0 b10;
        za.k.e(g0Var, "suspendFunType");
        functionTypes.q(g0Var);
        KotlinBuiltIns i10 = ld.a.i(g0Var);
        qb.g i11 = g0Var.i();
        g0 j10 = functionTypes.j(g0Var);
        List<g0> e11 = functionTypes.e(g0Var);
        List<TypeProjection> l10 = functionTypes.l(g0Var);
        u7 = s.u(l10, 10);
        ArrayList arrayList = new ArrayList(u7);
        Iterator<T> it = l10.iterator();
        while (it.hasNext()) {
            arrayList.add(((TypeProjection) it.next()).getType());
        }
        c1 h10 = c1.f11749f.h();
        TypeConstructor n10 = f15341a.n();
        za.k.d(n10, "FAKE_CONTINUATION_CLASS_DESCRIPTOR.typeConstructor");
        e10 = CollectionsJVM.e(ld.a.a(functionTypes.k(g0Var)));
        n02 = _Collections.n0(arrayList, h0.j(h10, n10, e10, false, null, 16, null));
        o0 I = ld.a.i(g0Var).I();
        za.k.d(I, "suspendFunType.builtIns.nullableAnyType");
        b10 = functionTypes.b(i10, i11, j10, e11, n02, null, I, (r17 & 128) != 0 ? false : false);
        return b10.a1(g0Var.X0());
    }
}
