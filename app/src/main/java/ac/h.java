package ac;

import cc.LazyJavaStaticClassScope;
import gd.g0;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import kotlin.collections._Collections;
import kotlin.collections.s;
import ma.o;
import oc.Name;
import pb.CallableDescriptor;
import pb.ClassDescriptor;
import pb.SourceElement;
import pb.ValueParameterDescriptor;
import sb.ValueParameterDescriptorImpl;
import za.k;

/* compiled from: util.kt */
/* loaded from: classes2.dex */
public final class h {
    public static final List<ValueParameterDescriptor> a(Collection<? extends g0> collection, Collection<? extends ValueParameterDescriptor> collection2, CallableDescriptor callableDescriptor) {
        List<o> G0;
        int u7;
        k.e(collection, "newValueParameterTypes");
        k.e(collection2, "oldValueParameters");
        k.e(callableDescriptor, "newOwner");
        collection.size();
        collection2.size();
        G0 = _Collections.G0(collection, collection2);
        u7 = s.u(G0, 10);
        ArrayList arrayList = new ArrayList(u7);
        for (o oVar : G0) {
            g0 g0Var = (g0) oVar.a();
            ValueParameterDescriptor valueParameterDescriptor = (ValueParameterDescriptor) oVar.b();
            int j10 = valueParameterDescriptor.j();
            qb.g i10 = valueParameterDescriptor.i();
            Name name = valueParameterDescriptor.getName();
            k.d(name, "oldParameter.name");
            boolean z02 = valueParameterDescriptor.z0();
            boolean i02 = valueParameterDescriptor.i0();
            boolean g02 = valueParameterDescriptor.g0();
            g0 k10 = valueParameterDescriptor.q0() != null ? wc.c.p(callableDescriptor).t().k(g0Var) : null;
            SourceElement z10 = valueParameterDescriptor.z();
            k.d(z10, "oldParameter.source");
            arrayList.add(new ValueParameterDescriptorImpl(callableDescriptor, null, j10, i10, name, g0Var, z02, i02, g02, k10, z10));
        }
        return arrayList;
    }

    public static final LazyJavaStaticClassScope b(ClassDescriptor classDescriptor) {
        k.e(classDescriptor, "<this>");
        ClassDescriptor t7 = wc.c.t(classDescriptor);
        if (t7 == null) {
            return null;
        }
        zc.h a02 = t7.a0();
        LazyJavaStaticClassScope lazyJavaStaticClassScope = a02 instanceof LazyJavaStaticClassScope ? (LazyJavaStaticClassScope) a02 : null;
        return lazyJavaStaticClassScope == null ? b(t7) : lazyJavaStaticClassScope;
    }
}
