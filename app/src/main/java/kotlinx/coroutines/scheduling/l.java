package kotlinx.coroutines.scheduling;

import fb._Ranges;
import java.util.concurrent.TimeUnit;
import kotlin.Metadata;
import kotlinx.coroutines.internal.b0;
import kotlinx.coroutines.internal.d0;

/* compiled from: Tasks.kt */
@Metadata(bv = {}, d1 = {"\u0000 \n\u0002\u0010\t\n\u0002\b\u0003\n\u0002\u0010\b\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0006\"\u0014\u0010\u0003\u001a\u00020\u00008\u0000X\u0081\u0004¢\u0006\u0006\n\u0004\b\u0001\u0010\u0002\"\u0014\u0010\u0007\u001a\u00020\u00048\u0000X\u0081\u0004¢\u0006\u0006\n\u0004\b\u0005\u0010\u0006\"\u0014\u0010\t\u001a\u00020\u00048\u0000X\u0081\u0004¢\u0006\u0006\n\u0004\b\b\u0010\u0006\"\u0014\u0010\u000b\u001a\u00020\u00008\u0000X\u0081\u0004¢\u0006\u0006\n\u0004\b\n\u0010\u0002\"\u0016\u0010\u000f\u001a\u00020\f8\u0000@\u0000X\u0081\u000e¢\u0006\u0006\n\u0004\b\r\u0010\u000e\"\u0014\u0010\u0013\u001a\u00020\u00108\u0000X\u0081\u0004¢\u0006\u0006\n\u0004\b\u0011\u0010\u0012\"\u0014\u0010\u0015\u001a\u00020\u00108\u0000X\u0081\u0004¢\u0006\u0006\n\u0004\b\u0014\u0010\u0012¨\u0006\u0016"}, d2 = {"", "a", "J", "WORK_STEALING_TIME_RESOLUTION_NS", "", "b", "I", "CORE_POOL_SIZE", "c", "MAX_POOL_SIZE", "d", "IDLE_WORKER_KEEP_ALIVE_NS", "Lkotlinx/coroutines/scheduling/g;", "e", "Lkotlinx/coroutines/scheduling/g;", "schedulerTimeSource", "Lkotlinx/coroutines/scheduling/i;", "f", "Lkotlinx/coroutines/scheduling/i;", "NonBlockingContext", "g", "BlockingContext", "kotlinx-coroutines-core"}, k = 2, mv = {1, 6, 0})
/* loaded from: classes2.dex */
public final class l {

    /* renamed from: a, reason: collision with root package name */
    public static final long f14446a;

    /* renamed from: b, reason: collision with root package name */
    public static final int f14447b;

    /* renamed from: c, reason: collision with root package name */
    public static final int f14448c;

    /* renamed from: d, reason: collision with root package name */
    public static final long f14449d;

    /* renamed from: e, reason: collision with root package name */
    public static g f14450e;

    /* renamed from: f, reason: collision with root package name */
    public static final i f14451f;

    /* renamed from: g, reason: collision with root package name */
    public static final i f14452g;

    static {
        long e10;
        int c10;
        int d10;
        int d11;
        long e11;
        e10 = d0.e("kotlinx.coroutines.scheduler.resolution.ns", 100000L, 0L, 0L, 12, null);
        f14446a = e10;
        c10 = _Ranges.c(b0.a(), 2);
        d10 = d0.d("kotlinx.coroutines.scheduler.core.pool.size", c10, 1, 0, 8, null);
        f14447b = d10;
        d11 = d0.d("kotlinx.coroutines.scheduler.max.pool.size", 2097150, 0, 2097150, 4, null);
        f14448c = d11;
        TimeUnit timeUnit = TimeUnit.SECONDS;
        e11 = d0.e("kotlinx.coroutines.scheduler.keep.alive.sec", 60L, 0L, 0L, 12, null);
        f14449d = timeUnit.toNanos(e11);
        f14450e = e.f14436a;
        f14451f = new j(0);
        f14452g = new j(1);
    }
}
