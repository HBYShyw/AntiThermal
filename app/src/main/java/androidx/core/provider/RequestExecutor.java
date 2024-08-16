package androidx.core.provider;

import android.os.Handler;
import android.os.Process;
import androidx.core.util.Consumer;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/* compiled from: RequestExecutor.java */
/* renamed from: androidx.core.provider.h, reason: use source file name */
/* loaded from: classes.dex */
class RequestExecutor {

    /* compiled from: RequestExecutor.java */
    /* renamed from: androidx.core.provider.h$a */
    /* loaded from: classes.dex */
    private static class a implements ThreadFactory {

        /* renamed from: a, reason: collision with root package name */
        private String f2258a;

        /* renamed from: b, reason: collision with root package name */
        private int f2259b;

        /* compiled from: RequestExecutor.java */
        /* renamed from: androidx.core.provider.h$a$a, reason: collision with other inner class name */
        /* loaded from: classes.dex */
        private static class C0002a extends Thread {

            /* renamed from: e, reason: collision with root package name */
            private final int f2260e;

            C0002a(Runnable runnable, String str, int i10) {
                super(runnable, str);
                this.f2260e = i10;
            }

            @Override // java.lang.Thread, java.lang.Runnable
            public void run() {
                Process.setThreadPriority(this.f2260e);
                super.run();
            }
        }

        a(String str, int i10) {
            this.f2258a = str;
            this.f2259b = i10;
        }

        @Override // java.util.concurrent.ThreadFactory
        public Thread newThread(Runnable runnable) {
            return new C0002a(runnable, this.f2258a, this.f2259b);
        }
    }

    /* compiled from: RequestExecutor.java */
    /* renamed from: androidx.core.provider.h$b */
    /* loaded from: classes.dex */
    private static class b<T> implements Runnable {

        /* renamed from: e, reason: collision with root package name */
        private Callable<T> f2261e;

        /* renamed from: f, reason: collision with root package name */
        private Consumer<T> f2262f;

        /* renamed from: g, reason: collision with root package name */
        private Handler f2263g;

        /* compiled from: RequestExecutor.java */
        /* renamed from: androidx.core.provider.h$b$a */
        /* loaded from: classes.dex */
        class a implements Runnable {

            /* renamed from: e, reason: collision with root package name */
            final /* synthetic */ Consumer f2264e;

            /* renamed from: f, reason: collision with root package name */
            final /* synthetic */ Object f2265f;

            a(Consumer consumer, Object obj) {
                this.f2264e = consumer;
                this.f2265f = obj;
            }

            /* JADX WARN: Multi-variable type inference failed */
            @Override // java.lang.Runnable
            public void run() {
                this.f2264e.accept(this.f2265f);
            }
        }

        b(Handler handler, Callable<T> callable, Consumer<T> consumer) {
            this.f2261e = callable;
            this.f2262f = consumer;
            this.f2263g = handler;
        }

        @Override // java.lang.Runnable
        public void run() {
            T t7;
            try {
                t7 = this.f2261e.call();
            } catch (Exception unused) {
                t7 = null;
            }
            this.f2263g.post(new a(this.f2262f, t7));
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static ThreadPoolExecutor a(String str, int i10, int i11) {
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(0, 1, i11, TimeUnit.MILLISECONDS, new LinkedBlockingDeque(), new a(str, i10));
        threadPoolExecutor.allowCoreThreadTimeOut(true);
        return threadPoolExecutor;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static <T> void b(Executor executor, Callable<T> callable, Consumer<T> consumer) {
        executor.execute(new b(CalleeHandler.a(), callable, consumer));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static <T> T c(ExecutorService executorService, Callable<T> callable, int i10) {
        try {
            return executorService.submit(callable).get(i10, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e10) {
            throw e10;
        } catch (ExecutionException e11) {
            throw new RuntimeException(e11);
        } catch (TimeoutException unused) {
            throw new InterruptedException("timeout");
        }
    }
}
