package td;

import java.util.concurrent.locks.LockSupport;
import kotlin.Metadata;
import td.x0;

/* compiled from: EventLoop.kt */
@Metadata(bv = {}, d1 = {"\u0000$\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\t\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0006\b \u0018\u00002\u00020\u0001B\u0007¢\u0006\u0004\b\r\u0010\u000eJ\b\u0010\u0003\u001a\u00020\u0002H\u0004J\u0018\u0010\b\u001a\u00020\u00022\u0006\u0010\u0005\u001a\u00020\u00042\u0006\u0010\u0007\u001a\u00020\u0006H\u0014R\u0014\u0010\f\u001a\u00020\t8$X¤\u0004¢\u0006\u0006\u001a\u0004\b\n\u0010\u000b¨\u0006\u000f"}, d2 = {"Ltd/y0;", "Ltd/w0;", "Lma/f0;", "I0", "", "now", "Ltd/x0$a;", "delayedTask", "H0", "Ljava/lang/Thread;", "G0", "()Ljava/lang/Thread;", "thread", "<init>", "()V", "kotlinx-coroutines-core"}, k = 1, mv = {1, 6, 0})
/* loaded from: classes2.dex */
public abstract class y0 extends w0 {
    /* renamed from: G0 */
    protected abstract Thread getF18736l();

    /* JADX INFO: Access modifiers changed from: protected */
    public void H0(long j10, x0.a aVar) {
        m0.f18762l.T0(j10, aVar);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public final void I0() {
        Thread f18736l = getF18736l();
        if (Thread.currentThread() != f18736l) {
            c.a();
            LockSupport.unpark(f18736l);
        }
    }
}
