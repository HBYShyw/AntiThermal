package ma;

import ma.p;

/* compiled from: Result.kt */
/* loaded from: classes2.dex */
public final class q {
    public static final Object a(Throwable th) {
        za.k.e(th, "exception");
        return new p.b(th);
    }

    public static final void b(Object obj) {
        if (obj instanceof p.b) {
            throw ((p.b) obj).f15185e;
        }
    }
}
