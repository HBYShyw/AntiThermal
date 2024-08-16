package nc;

import java.util.ArrayList;
import java.util.List;
import mc.JvmProtoBuf;
import za.k;

/* compiled from: JvmNameResolver.kt */
/* loaded from: classes2.dex */
public final class h {
    public static final List<JvmProtoBuf.e.c> a(List<JvmProtoBuf.e.c> list) {
        k.e(list, "<this>");
        ArrayList arrayList = new ArrayList();
        arrayList.ensureCapacity(list.size());
        for (JvmProtoBuf.e.c cVar : list) {
            int A = cVar.A();
            for (int i10 = 0; i10 < A; i10++) {
                arrayList.add(cVar);
            }
        }
        arrayList.trimToSize();
        return arrayList;
    }
}
