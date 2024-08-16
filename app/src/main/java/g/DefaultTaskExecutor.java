package g;

import android.os.Handler;
import android.os.Looper;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/* compiled from: DefaultTaskExecutor.java */
/* renamed from: g.b, reason: use source file name */
/* loaded from: classes.dex */
public class DefaultTaskExecutor extends TaskExecutor {

    /* renamed from: a, reason: collision with root package name */
    private final Object f11485a = new Object();

    /* renamed from: b, reason: collision with root package name */
    private final ExecutorService f11486b = Executors.newFixedThreadPool(4, new a());

    /* renamed from: c, reason: collision with root package name */
    private volatile Handler f11487c;

    /* compiled from: DefaultTaskExecutor.java */
    /* renamed from: g.b$a */
    /* loaded from: classes.dex */
    class a implements ThreadFactory {

        /* renamed from: a, reason: collision with root package name */
        private final AtomicInteger f11488a = new AtomicInteger(0);

        a() {
        }

        @Override // java.util.concurrent.ThreadFactory
        public Thread newThread(Runnable runnable) {
            Thread thread = new Thread(runnable);
            thread.setName(String.format("arch_disk_io_%d", Integer.valueOf(this.f11488a.getAndIncrement())));
            return thread;
        }
    }

    private static Handler e(Looper looper) {
        return Handler.createAsync(looper);
    }

    @Override // g.TaskExecutor
    public void a(Runnable runnable) {
        this.f11486b.execute(runnable);
    }

    @Override // g.TaskExecutor
    public boolean c() {
        return Looper.getMainLooper().getThread() == Thread.currentThread();
    }

    @Override // g.TaskExecutor
    public void d(Runnable runnable) {
        if (this.f11487c == null) {
            synchronized (this.f11485a) {
                if (this.f11487c == null) {
                    this.f11487c = e(Looper.getMainLooper());
                }
            }
        }
        this.f11487c.post(runnable);
    }
}
