package kotlinx.coroutines.scheduling;

import kotlin.Metadata;
import td.Executors;

/* compiled from: Dispatcher.kt */
@Metadata(bv = {}, d1 = {"\u0000H\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0010\b\n\u0002\b\u0005\n\u0002\u0010\t\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0002\b\t\b\u0010\u0018\u00002\u00020\u0001B/\u0012\b\b\u0002\u0010\u0013\u001a\u00020\u0010\u0012\b\b\u0002\u0010\u0015\u001a\u00020\u0010\u0012\b\b\u0002\u0010\u0019\u001a\u00020\u0016\u0012\b\b\u0002\u0010\u001d\u001a\u00020\u001a¢\u0006\u0004\b!\u0010\"J\b\u0010\u0003\u001a\u00020\u0002H\u0002J\u001c\u0010\n\u001a\u00020\t2\u0006\u0010\u0005\u001a\u00020\u00042\n\u0010\b\u001a\u00060\u0006j\u0002`\u0007H\u0016J+\u0010\u000e\u001a\u00020\t2\n\u0010\b\u001a\u00060\u0006j\u0002`\u00072\u0006\u0010\u0005\u001a\u00020\u000b2\u0006\u0010\r\u001a\u00020\fH\u0000¢\u0006\u0004\b\u000e\u0010\u000fR\u0014\u0010\u0013\u001a\u00020\u00108\u0002X\u0082\u0004¢\u0006\u0006\n\u0004\b\u0011\u0010\u0012R\u0014\u0010\u0015\u001a\u00020\u00108\u0002X\u0082\u0004¢\u0006\u0006\n\u0004\b\u0014\u0010\u0012R\u0014\u0010\u0019\u001a\u00020\u00168\u0002X\u0082\u0004¢\u0006\u0006\n\u0004\b\u0017\u0010\u0018R\u0014\u0010\u001d\u001a\u00020\u001a8\u0002X\u0082\u0004¢\u0006\u0006\n\u0004\b\u001b\u0010\u001cR\u0016\u0010 \u001a\u00020\u00028\u0002@\u0002X\u0082\u000e¢\u0006\u0006\n\u0004\b\u001e\u0010\u001f¨\u0006#"}, d2 = {"Lkotlinx/coroutines/scheduling/f;", "Ltd/b1;", "Lkotlinx/coroutines/scheduling/a;", "w0", "Lqa/g;", "context", "Ljava/lang/Runnable;", "Lkotlinx/coroutines/Runnable;", "block", "Lma/f0;", "t0", "Lkotlinx/coroutines/scheduling/i;", "", "tailDispatch", "x0", "(Ljava/lang/Runnable;Lkotlinx/coroutines/scheduling/i;Z)V", "", "h", "I", "corePoolSize", "i", "maxPoolSize", "", "j", "J", "idleWorkerKeepAliveNs", "", "k", "Ljava/lang/String;", "schedulerName", "l", "Lkotlinx/coroutines/scheduling/a;", "coroutineScheduler", "<init>", "(IIJLjava/lang/String;)V", "kotlinx-coroutines-core"}, k = 1, mv = {1, 6, 0})
/* loaded from: classes2.dex */
public class f extends Executors {

    /* renamed from: h, reason: collision with root package name and from kotlin metadata */
    private final int corePoolSize;

    /* renamed from: i, reason: collision with root package name and from kotlin metadata */
    private final int maxPoolSize;

    /* renamed from: j, reason: collision with root package name and from kotlin metadata */
    private final long idleWorkerKeepAliveNs;

    /* renamed from: k, reason: collision with root package name and from kotlin metadata */
    private final String schedulerName;

    /* renamed from: l, reason: collision with root package name and from kotlin metadata */
    private CoroutineScheduler coroutineScheduler = w0();

    public f(int i10, int i11, long j10, String str) {
        this.corePoolSize = i10;
        this.maxPoolSize = i11;
        this.idleWorkerKeepAliveNs = j10;
        this.schedulerName = str;
    }

    private final CoroutineScheduler w0() {
        return new CoroutineScheduler(this.corePoolSize, this.maxPoolSize, this.idleWorkerKeepAliveNs, this.schedulerName);
    }

    @Override // td.CoroutineDispatcher
    public void t0(qa.g gVar, Runnable runnable) {
        CoroutineScheduler.w(this.coroutineScheduler, runnable, null, false, 6, null);
    }

    public final void x0(Runnable block, i context, boolean tailDispatch) {
        this.coroutineScheduler.v(block, context, tailDispatch);
    }
}
