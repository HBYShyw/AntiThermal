package g;

import java.util.concurrent.Executor;

/* compiled from: ArchTaskExecutor.java */
/* renamed from: g.a, reason: use source file name */
/* loaded from: classes.dex */
public class ArchTaskExecutor extends TaskExecutor {

    /* renamed from: c, reason: collision with root package name */
    private static volatile ArchTaskExecutor f11480c;

    /* renamed from: d, reason: collision with root package name */
    private static final Executor f11481d = new a();

    /* renamed from: e, reason: collision with root package name */
    private static final Executor f11482e = new b();

    /* renamed from: a, reason: collision with root package name */
    private TaskExecutor f11483a;

    /* renamed from: b, reason: collision with root package name */
    private TaskExecutor f11484b;

    /* compiled from: ArchTaskExecutor.java */
    /* renamed from: g.a$a */
    /* loaded from: classes.dex */
    static class a implements Executor {
        a() {
        }

        @Override // java.util.concurrent.Executor
        public void execute(Runnable runnable) {
            ArchTaskExecutor.f().d(runnable);
        }
    }

    /* compiled from: ArchTaskExecutor.java */
    /* renamed from: g.a$b */
    /* loaded from: classes.dex */
    static class b implements Executor {
        b() {
        }

        @Override // java.util.concurrent.Executor
        public void execute(Runnable runnable) {
            ArchTaskExecutor.f().a(runnable);
        }
    }

    private ArchTaskExecutor() {
        DefaultTaskExecutor defaultTaskExecutor = new DefaultTaskExecutor();
        this.f11484b = defaultTaskExecutor;
        this.f11483a = defaultTaskExecutor;
    }

    public static Executor e() {
        return f11482e;
    }

    public static ArchTaskExecutor f() {
        if (f11480c != null) {
            return f11480c;
        }
        synchronized (ArchTaskExecutor.class) {
            if (f11480c == null) {
                f11480c = new ArchTaskExecutor();
            }
        }
        return f11480c;
    }

    @Override // g.TaskExecutor
    public void a(Runnable runnable) {
        this.f11483a.a(runnable);
    }

    @Override // g.TaskExecutor
    public boolean c() {
        return this.f11483a.c();
    }

    @Override // g.TaskExecutor
    public void d(Runnable runnable) {
        this.f11483a.d(runnable);
    }
}
