package androidx.room;

import java.util.ArrayDeque;
import java.util.concurrent.Executor;

/* compiled from: TransactionExecutor.java */
/* renamed from: androidx.room.k, reason: use source file name */
/* loaded from: classes.dex */
class TransactionExecutor implements Executor {

    /* renamed from: e, reason: collision with root package name */
    private final Executor f3942e;

    /* renamed from: f, reason: collision with root package name */
    private final ArrayDeque<Runnable> f3943f = new ArrayDeque<>();

    /* renamed from: g, reason: collision with root package name */
    private Runnable f3944g;

    /* compiled from: TransactionExecutor.java */
    /* renamed from: androidx.room.k$a */
    /* loaded from: classes.dex */
    class a implements Runnable {

        /* renamed from: e, reason: collision with root package name */
        final /* synthetic */ Runnable f3945e;

        a(Runnable runnable) {
            this.f3945e = runnable;
        }

        @Override // java.lang.Runnable
        public void run() {
            try {
                this.f3945e.run();
            } finally {
                TransactionExecutor.this.a();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public TransactionExecutor(Executor executor) {
        this.f3942e = executor;
    }

    synchronized void a() {
        Runnable poll = this.f3943f.poll();
        this.f3944g = poll;
        if (poll != null) {
            this.f3942e.execute(poll);
        }
    }

    @Override // java.util.concurrent.Executor
    public synchronized void execute(Runnable runnable) {
        this.f3943f.offer(new a(runnable));
        if (this.f3944g == null) {
            a();
        }
    }
}
