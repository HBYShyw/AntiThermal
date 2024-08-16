package kotlinx.coroutines.scheduling;

import kotlin.Metadata;
import td.CoroutineDispatcher;

/* compiled from: Dispatcher.kt */
@Metadata(bv = {}, d1 = {"\u0000 \n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\bÂ\u0002\u0018\u00002\u00020\u0001B\t\b\u0002¢\u0006\u0004\b\t\u0010\nJ\u001c\u0010\b\u001a\u00020\u00072\u0006\u0010\u0003\u001a\u00020\u00022\n\u0010\u0006\u001a\u00060\u0004j\u0002`\u0005H\u0016¨\u0006\u000b"}, d2 = {"Lkotlinx/coroutines/scheduling/m;", "Ltd/c0;", "Lqa/g;", "context", "Ljava/lang/Runnable;", "Lkotlinx/coroutines/Runnable;", "block", "Lma/f0;", "t0", "<init>", "()V", "kotlinx-coroutines-core"}, k = 1, mv = {1, 6, 0})
/* loaded from: classes2.dex */
final class m extends CoroutineDispatcher {

    /* renamed from: g, reason: collision with root package name */
    public static final m f14453g = new m();

    private m() {
    }

    @Override // td.CoroutineDispatcher
    public void t0(qa.g gVar, Runnable runnable) {
        c.f14435m.x0(runnable, l.f14452g, false);
    }
}
