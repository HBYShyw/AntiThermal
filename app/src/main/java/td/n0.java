package td;

import kotlin.Metadata;

/* compiled from: DefaultExecutor.kt */
@Metadata(bv = {}, d1 = {"\u0000\b\n\u0002\u0018\u0002\n\u0002\b\u0006\u001a\b\u0010\u0001\u001a\u00020\u0000H\u0002\"\u001a\u0010\u0002\u001a\u00020\u00008\u0000X\u0080\u0004¢\u0006\f\n\u0004\b\u0002\u0010\u0003\u001a\u0004\b\u0004\u0010\u0005¨\u0006\u0006"}, d2 = {"Ltd/o0;", "b", "DefaultDelay", "Ltd/o0;", "a", "()Ltd/o0;", "kotlinx-coroutines-core"}, k = 2, mv = {1, 6, 0})
/* loaded from: classes2.dex */
public final class n0 {

    /* renamed from: a, reason: collision with root package name */
    private static final boolean f18764a = kotlinx.coroutines.internal.b0.e("kotlinx.coroutines.main.delay", false);

    /* renamed from: b, reason: collision with root package name */
    private static final Delay f18765b = b();

    public static final Delay a() {
        return f18765b;
    }

    /* JADX WARN: Multi-variable type inference failed */
    private static final Delay b() {
        if (!f18764a) {
            return m0.f18762l;
        }
        MainCoroutineDispatcher c10 = Dispatchers.c();
        return (kotlinx.coroutines.internal.s.c(c10) || !(c10 instanceof Delay)) ? m0.f18762l : (Delay) c10;
    }
}
