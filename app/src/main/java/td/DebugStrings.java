package td;

import kotlin.Metadata;
import ma.p;

/* compiled from: DebugStrings.kt */
@Metadata(bv = {}, d1 = {"\u0000\u0012\n\u0002\u0018\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\u0000\n\u0002\b\u0006\u001a\u0010\u0010\u0002\u001a\u00020\u0001*\u0006\u0012\u0002\b\u00030\u0000H\u0000\"\u0018\u0010\u0006\u001a\u00020\u0001*\u00020\u00038@X\u0080\u0004¢\u0006\u0006\u001a\u0004\b\u0004\u0010\u0005\"\u0018\u0010\b\u001a\u00020\u0001*\u00020\u00038@X\u0080\u0004¢\u0006\u0006\u001a\u0004\b\u0007\u0010\u0005¨\u0006\t"}, d2 = {"Lqa/d;", "", "c", "", "b", "(Ljava/lang/Object;)Ljava/lang/String;", "hexAddress", "a", "classSimpleName", "kotlinx-coroutines-core"}, k = 2, mv = {1, 6, 0})
/* renamed from: td.l0, reason: use source file name */
/* loaded from: classes2.dex */
public final class DebugStrings {
    public static final String a(Object obj) {
        return obj.getClass().getSimpleName();
    }

    public static final String b(Object obj) {
        return Integer.toHexString(System.identityHashCode(obj));
    }

    public static final String c(qa.d<?> dVar) {
        Object a10;
        if (dVar instanceof kotlinx.coroutines.internal.e) {
            return dVar.toString();
        }
        try {
            p.a aVar = ma.p.f15184e;
            a10 = ma.p.a(dVar + '@' + b(dVar));
        } catch (Throwable th) {
            p.a aVar2 = ma.p.f15184e;
            a10 = ma.p.a(ma.q.a(th));
        }
        if (ma.p.b(a10) != null) {
            a10 = dVar.getClass().getName() + '@' + b(dVar);
        }
        return (String) a10;
    }
}
