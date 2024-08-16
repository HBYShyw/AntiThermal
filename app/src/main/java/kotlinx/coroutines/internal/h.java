package kotlinx.coroutines.internal;

import kotlin.Metadata;
import ma.p;

/* compiled from: FastServiceLoader.kt */
@Metadata(bv = {}, d1 = {"\u0000\b\n\u0002\u0010\u000b\n\u0002\b\u0005\"\u001a\u0010\u0004\u001a\u00020\u00008\u0000X\u0080\u0004¢\u0006\f\n\u0004\b\u0001\u0010\u0002\u001a\u0004\b\u0001\u0010\u0003¨\u0006\u0005"}, d2 = {"", "a", "Z", "()Z", "ANDROID_DETECTED", "kotlinx-coroutines-core"}, k = 2, mv = {1, 6, 0})
/* loaded from: classes2.dex */
public final class h {

    /* renamed from: a, reason: collision with root package name */
    private static final boolean f14366a;

    static {
        Object a10;
        try {
            p.a aVar = ma.p.f15184e;
            a10 = ma.p.a(Class.forName("android.os.Build"));
        } catch (Throwable th) {
            p.a aVar2 = ma.p.f15184e;
            a10 = ma.p.a(ma.q.a(th));
        }
        f14366a = ma.p.d(a10);
    }

    public static final boolean a() {
        return f14366a;
    }
}
