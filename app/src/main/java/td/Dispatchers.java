package td;

import kotlin.Metadata;

/* compiled from: Dispatchers.kt */
@Metadata(bv = {}, d1 = {"\u0000\u0018\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\b\t\bÆ\u0002\u0018\u00002\u00020\u0001B\t\b\u0002¢\u0006\u0004\b\u0011\u0010\bR \u0010\u0003\u001a\u00020\u00028\u0006X\u0087\u0004¢\u0006\u0012\n\u0004\b\u0003\u0010\u0004\u0012\u0004\b\u0007\u0010\b\u001a\u0004\b\u0005\u0010\u0006R\u001a\u0010\r\u001a\u00020\t8FX\u0087\u0004¢\u0006\f\u0012\u0004\b\f\u0010\b\u001a\u0004\b\n\u0010\u000bR \u0010\u000e\u001a\u00020\u00028\u0006X\u0087\u0004¢\u0006\u0012\n\u0004\b\u000e\u0010\u0004\u0012\u0004\b\u0010\u0010\b\u001a\u0004\b\u000f\u0010\u0006¨\u0006\u0012"}, d2 = {"Ltd/t0;", "", "Ltd/c0;", "Default", "Ltd/c0;", "a", "()Ltd/c0;", "getDefault$annotations", "()V", "Ltd/s1;", "c", "()Ltd/s1;", "getMain$annotations", "Main", "IO", "b", "getIO$annotations", "<init>", "kotlinx-coroutines-core"}, k = 1, mv = {1, 6, 0})
/* renamed from: td.t0, reason: use source file name */
/* loaded from: classes2.dex */
public final class Dispatchers {

    /* renamed from: a, reason: collision with root package name */
    public static final Dispatchers f18789a = new Dispatchers();

    /* renamed from: b, reason: collision with root package name */
    private static final CoroutineDispatcher f18790b = kotlinx.coroutines.scheduling.c.f14435m;

    /* renamed from: c, reason: collision with root package name */
    private static final CoroutineDispatcher f18791c = d2.f18732g;

    /* renamed from: d, reason: collision with root package name */
    private static final CoroutineDispatcher f18792d = kotlinx.coroutines.scheduling.b.f14433h;

    private Dispatchers() {
    }

    public static final CoroutineDispatcher a() {
        return f18790b;
    }

    public static final CoroutineDispatcher b() {
        return f18792d;
    }

    public static final MainCoroutineDispatcher c() {
        return kotlinx.coroutines.internal.r.f14395c;
    }
}
