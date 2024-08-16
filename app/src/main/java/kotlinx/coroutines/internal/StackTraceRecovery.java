package kotlinx.coroutines.internal;

import kotlin.Metadata;
import ma.p;

/* compiled from: StackTraceRecovery.kt */
@Metadata(bv = {}, d1 = {"\u0000\u001e\n\u0002\u0010\u0003\n\u0002\b\u0004\n\u0002\u0010\u000e\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\u001a!\u0010\u0003\u001a\u00028\u0000\"\b\b\u0000\u0010\u0001*\u00020\u00002\u0006\u0010\u0002\u001a\u00028\u0000H\u0000¢\u0006\u0004\b\u0003\u0010\u0004\"\u001c\u0010\b\u001a\n \u0006*\u0004\u0018\u00010\u00050\u00058\u0002X\u0082\u0004¢\u0006\u0006\n\u0004\b\u0003\u0010\u0007\"\u001c\u0010\n\u001a\n \u0006*\u0004\u0018\u00010\u00050\u00058\u0002X\u0082\u0004¢\u0006\u0006\n\u0004\b\t\u0010\u0007*\f\b\u0000\u0010\f\"\u00020\u000b2\u00020\u000b*\f\b\u0000\u0010\u000e\"\u00020\r2\u00020\r¨\u0006\u000f"}, d2 = {"", "E", "exception", "a", "(Ljava/lang/Throwable;)Ljava/lang/Throwable;", "", "kotlin.jvm.PlatformType", "Ljava/lang/String;", "baseContinuationImplClassName", "b", "stackTraceRecoveryClassName", "Lsa/e;", "CoroutineStackFrame", "Ljava/lang/StackTraceElement;", "StackTraceElement", "kotlinx-coroutines-core"}, k = 2, mv = {1, 6, 0})
/* renamed from: kotlinx.coroutines.internal.z, reason: use source file name */
/* loaded from: classes2.dex */
public final class StackTraceRecovery {

    /* renamed from: a, reason: collision with root package name */
    private static final String f14404a;

    /* renamed from: b, reason: collision with root package name */
    private static final String f14405b;

    static {
        Object a10;
        Object a11;
        try {
            p.a aVar = ma.p.f15184e;
            a10 = ma.p.a(Class.forName("sa.a").getCanonicalName());
        } catch (Throwable th) {
            p.a aVar2 = ma.p.f15184e;
            a10 = ma.p.a(ma.q.a(th));
        }
        if (ma.p.b(a10) != null) {
            a10 = "kotlin.coroutines.jvm.internal.BaseContinuationImpl";
        }
        f14404a = (String) a10;
        try {
            p.a aVar3 = ma.p.f15184e;
            a11 = ma.p.a(StackTraceRecovery.class.getCanonicalName());
        } catch (Throwable th2) {
            p.a aVar4 = ma.p.f15184e;
            a11 = ma.p.a(ma.q.a(th2));
        }
        if (ma.p.b(a11) != null) {
            a11 = "kotlinx.coroutines.internal.StackTraceRecoveryKt";
        }
        f14405b = (String) a11;
    }

    public static final <E extends Throwable> E a(E e10) {
        return e10;
    }
}
