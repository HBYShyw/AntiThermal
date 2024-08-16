package lc;

import java.util.ArrayList;
import java.util.List;
import jc.q;
import jc.t;
import kotlin.collections.r;
import kotlin.collections.s;
import za.k;

/* compiled from: TypeTable.kt */
/* renamed from: lc.g, reason: use source file name */
/* loaded from: classes2.dex */
public final class TypeTable {

    /* renamed from: a, reason: collision with root package name */
    private final List<q> f14698a;

    public TypeTable(t tVar) {
        int u7;
        k.e(tVar, "typeTable");
        List<q> v7 = tVar.v();
        if (tVar.w()) {
            int s7 = tVar.s();
            List<q> v10 = tVar.v();
            k.d(v10, "typeTable.typeList");
            u7 = s.u(v10, 10);
            ArrayList arrayList = new ArrayList(u7);
            int i10 = 0;
            for (Object obj : v10) {
                int i11 = i10 + 1;
                if (i10 < 0) {
                    r.t();
                }
                q qVar = (q) obj;
                if (i10 >= s7) {
                    qVar = qVar.toBuilder().D(true).build();
                }
                arrayList.add(qVar);
                i10 = i11;
            }
            v7 = arrayList;
        }
        k.d(v7, "run {\n        val origin… else originalTypes\n    }");
        this.f14698a = v7;
    }

    public final q a(int i10) {
        return this.f14698a.get(i10);
    }
}
