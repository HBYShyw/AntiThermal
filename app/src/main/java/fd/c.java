package fd;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import ma.Unit;

/* compiled from: locks.kt */
/* loaded from: classes2.dex */
public final class c extends d {

    /* renamed from: c, reason: collision with root package name */
    private final Runnable f11419c;

    /* renamed from: d, reason: collision with root package name */
    private final ya.l<InterruptedException, Unit> f11420d;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    /* JADX WARN: Multi-variable type inference failed */
    public c(Lock lock, Runnable runnable, ya.l<? super InterruptedException, Unit> lVar) {
        super(lock);
        za.k.e(lock, "lock");
        za.k.e(runnable, "checkCancelled");
        za.k.e(lVar, "interruptedExceptionHandler");
        this.f11419c = runnable;
        this.f11420d = lVar;
    }

    @Override // fd.d, fd.k
    public void b() {
        while (!c().tryLock(50L, TimeUnit.MILLISECONDS)) {
            try {
                this.f11419c.run();
            } catch (InterruptedException e10) {
                this.f11420d.invoke(e10);
                return;
            }
        }
    }

    /* JADX WARN: 'this' call moved to the top of the method (can break code semantics) */
    public c(Runnable runnable, ya.l<? super InterruptedException, Unit> lVar) {
        this(new ReentrantLock(), runnable, lVar);
        za.k.e(runnable, "checkCancelled");
        za.k.e(lVar, "interruptedExceptionHandler");
    }
}
