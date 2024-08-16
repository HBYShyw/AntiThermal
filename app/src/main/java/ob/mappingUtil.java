package ob;

import gd.h1;
import gd.o0;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import kotlin.collections._Collections;
import kotlin.collections.m0;
import kotlin.collections.s;
import pb.ClassDescriptor;
import pb.TypeParameterDescriptor;

/* compiled from: mappingUtil.kt */
/* renamed from: ob.l, reason: use source file name */
/* loaded from: classes2.dex */
public final class mappingUtil {
    public static final h1 a(ClassDescriptor classDescriptor, ClassDescriptor classDescriptor2) {
        int u7;
        int u10;
        List G0;
        Map q10;
        za.k.e(classDescriptor, "from");
        za.k.e(classDescriptor2, "to");
        classDescriptor.B().size();
        classDescriptor2.B().size();
        h1.a aVar = h1.f11827c;
        List<TypeParameterDescriptor> B = classDescriptor.B();
        za.k.d(B, "from.declaredTypeParameters");
        u7 = s.u(B, 10);
        ArrayList arrayList = new ArrayList(u7);
        Iterator<T> it = B.iterator();
        while (it.hasNext()) {
            arrayList.add(((TypeParameterDescriptor) it.next()).n());
        }
        List<TypeParameterDescriptor> B2 = classDescriptor2.B();
        za.k.d(B2, "to.declaredTypeParameters");
        u10 = s.u(B2, 10);
        ArrayList arrayList2 = new ArrayList(u10);
        Iterator<T> it2 = B2.iterator();
        while (it2.hasNext()) {
            o0 x10 = ((TypeParameterDescriptor) it2.next()).x();
            za.k.d(x10, "it.defaultType");
            arrayList2.add(ld.a.a(x10));
        }
        G0 = _Collections.G0(arrayList, arrayList2);
        q10 = m0.q(G0);
        return h1.a.e(aVar, q10, false, 2, null);
    }
}
