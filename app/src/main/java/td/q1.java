package td;

import kotlin.Metadata;
import kotlinx.coroutines.internal.Symbol;

/* compiled from: JobSupport.kt */
@Metadata(bv = {}, d1 = {"\u0000\b\n\u0002\u0010\u0000\n\u0002\b\u0003\u001a\u0010\u0010\u0001\u001a\u0004\u0018\u00010\u0000*\u0004\u0018\u00010\u0000H\u0000\u001a\u0010\u0010\u0002\u001a\u0004\u0018\u00010\u0000*\u0004\u0018\u00010\u0000H\u0000¨\u0006\u0003"}, d2 = {"", "g", "h", "kotlinx-coroutines-core"}, k = 2, mv = {1, 6, 0})
/* loaded from: classes2.dex */
public final class q1 {

    /* renamed from: a, reason: collision with root package name */
    private static final Symbol f18779a = new Symbol("COMPLETING_ALREADY");

    /* renamed from: b, reason: collision with root package name */
    public static final Symbol f18780b = new Symbol("COMPLETING_WAITING_CHILDREN");

    /* renamed from: c, reason: collision with root package name */
    private static final Symbol f18781c = new Symbol("COMPLETING_RETRY");

    /* renamed from: d, reason: collision with root package name */
    private static final Symbol f18782d = new Symbol("TOO_LATE_TO_CANCEL");

    /* renamed from: e, reason: collision with root package name */
    private static final Symbol f18783e = new Symbol("SEALED");

    /* renamed from: f, reason: collision with root package name */
    private static final v0 f18784f = new v0(false);

    /* renamed from: g, reason: collision with root package name */
    private static final v0 f18785g = new v0(true);

    public static final Object g(Object obj) {
        return obj instanceof d1 ? new e1((d1) obj) : obj;
    }

    public static final Object h(Object obj) {
        d1 d1Var;
        e1 e1Var = obj instanceof e1 ? (e1) obj : null;
        return (e1Var == null || (d1Var = e1Var.f18734a) == null) ? obj : d1Var;
    }
}
