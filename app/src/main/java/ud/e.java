package ud;

import android.os.Handler;
import android.os.Looper;
import android.view.Choreographer;
import java.util.Objects;
import kotlin.Metadata;
import ma.p;
import ma.q;

/* compiled from: HandlerDispatcher.kt */
@Metadata(bv = {}, d1 = {"\u0000\u0012\n\u0002\u0018\u0002\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\u001a\u0014\u0010\u0004\u001a\u00020\u0003*\u00020\u00002\u0006\u0010\u0002\u001a\u00020\u0001H\u0001¨\u0006\u0005"}, d2 = {"Landroid/os/Looper;", "", "async", "Landroid/os/Handler;", "a", "kotlinx-coroutines-android"}, k = 2, mv = {1, 6, 0})
/* loaded from: classes2.dex */
public final class e {

    /* renamed from: a, reason: collision with root package name */
    public static final d f19020a;
    private static volatile Choreographer choreographer;

    static {
        Object a10;
        try {
            p.a aVar = p.f15184e;
            a10 = p.a(new c(a(Looper.getMainLooper(), true), null, 2, null));
        } catch (Throwable th) {
            p.a aVar2 = p.f15184e;
            a10 = p.a(q.a(th));
        }
        f19020a = (d) (p.c(a10) ? null : a10);
    }

    public static final Handler a(Looper looper, boolean z10) {
        if (z10) {
            Object invoke = Handler.class.getDeclaredMethod("createAsync", Looper.class).invoke(null, looper);
            Objects.requireNonNull(invoke, "null cannot be cast to non-null type android.os.Handler");
            return (Handler) invoke;
        }
        return new Handler(looper);
    }
}
