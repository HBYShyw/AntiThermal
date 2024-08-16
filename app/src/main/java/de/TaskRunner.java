package de;

import com.oplus.deepthinker.sdk.app.DataLinkConstants;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import kotlin.Metadata;
import ma.Unit;
import za.DefaultConstructorMarker;
import za.k;

/* compiled from: TaskRunner.kt */
@Metadata(bv = {}, d1 = {"\u0000.\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\t\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\b\t\u0018\u00002\u00020\u0001:\u0003\u0018\u0019\tB\u000f\u0012\u0006\u0010\u0012\u001a\u00020\u0011¢\u0006\u0004\b\u0016\u0010\u0017J\u0010\u0010\u0005\u001a\u00020\u00042\u0006\u0010\u0003\u001a\u00020\u0002H\u0002J\u0010\u0010\u0006\u001a\u00020\u00042\u0006\u0010\u0003\u001a\u00020\u0002H\u0002J\u0018\u0010\t\u001a\u00020\u00042\u0006\u0010\u0003\u001a\u00020\u00022\u0006\u0010\b\u001a\u00020\u0007H\u0002J\u0017\u0010\f\u001a\u00020\u00042\u0006\u0010\u000b\u001a\u00020\nH\u0000¢\u0006\u0004\b\f\u0010\rJ\b\u0010\u000e\u001a\u0004\u0018\u00010\u0002J\u0006\u0010\u000f\u001a\u00020\nJ\u0006\u0010\u0010\u001a\u00020\u0004R\u0017\u0010\u0012\u001a\u00020\u00118\u0006¢\u0006\f\n\u0004\b\u0012\u0010\u0013\u001a\u0004\b\u0014\u0010\u0015¨\u0006\u001a"}, d2 = {"Lde/e;", "", "Lde/a;", "task", "Lma/f0;", "e", "j", "", "delayNanos", "c", "Lde/d;", "taskQueue", "h", "(Lde/d;)V", "d", "i", "f", "Lde/e$a;", "backend", "Lde/e$a;", "g", "()Lde/e$a;", "<init>", "(Lde/e$a;)V", "a", "b", "okhttp"}, k = 1, mv = {1, 6, 0})
/* renamed from: de.e, reason: use source file name */
/* loaded from: classes2.dex */
public final class TaskRunner {

    /* renamed from: h, reason: collision with root package name */
    public static final b f10943h = new b(null);

    /* renamed from: i, reason: collision with root package name */
    public static final TaskRunner f10944i = new TaskRunner(new c(ae.d.L(k.l(ae.d.f245i, " TaskRunner"), true)));

    /* renamed from: j, reason: collision with root package name */
    private static final Logger f10945j;

    /* renamed from: a, reason: collision with root package name */
    private final a f10946a;

    /* renamed from: b, reason: collision with root package name */
    private int f10947b;

    /* renamed from: c, reason: collision with root package name */
    private boolean f10948c;

    /* renamed from: d, reason: collision with root package name */
    private long f10949d;

    /* renamed from: e, reason: collision with root package name */
    private final List<de.d> f10950e;

    /* renamed from: f, reason: collision with root package name */
    private final List<de.d> f10951f;

    /* renamed from: g, reason: collision with root package name */
    private final Runnable f10952g;

    /* compiled from: TaskRunner.kt */
    @Metadata(bv = {}, d1 = {"\u0000$\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\u0010\t\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\bf\u0018\u00002\u00020\u0001J\b\u0010\u0003\u001a\u00020\u0002H&J\u0010\u0010\u0007\u001a\u00020\u00062\u0006\u0010\u0005\u001a\u00020\u0004H&J\u0018\u0010\t\u001a\u00020\u00062\u0006\u0010\u0005\u001a\u00020\u00042\u0006\u0010\b\u001a\u00020\u0002H&J\u0010\u0010\f\u001a\u00020\u00062\u0006\u0010\u000b\u001a\u00020\nH&¨\u0006\r"}, d2 = {"Lde/e$a;", "", "", "c", "Lde/e;", "taskRunner", "Lma/f0;", "b", "nanos", "a", "Ljava/lang/Runnable;", "runnable", "execute", "okhttp"}, k = 1, mv = {1, 6, 0})
    /* renamed from: de.e$a */
    /* loaded from: classes2.dex */
    public interface a {
        void a(TaskRunner taskRunner, long j10);

        void b(TaskRunner taskRunner);

        long c();

        void execute(Runnable runnable);
    }

    /* compiled from: TaskRunner.kt */
    @Metadata(bv = {}, d1 = {"\u0000\u0018\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0005\b\u0086\u0003\u0018\u00002\u00020\u0001B\t\b\u0002¢\u0006\u0004\b\n\u0010\u000bR\u0017\u0010\u0003\u001a\u00020\u00028\u0006¢\u0006\f\n\u0004\b\u0003\u0010\u0004\u001a\u0004\b\u0005\u0010\u0006R\u0014\u0010\b\u001a\u00020\u00078\u0006X\u0087\u0004¢\u0006\u0006\n\u0004\b\b\u0010\t¨\u0006\f"}, d2 = {"Lde/e$b;", "", "Ljava/util/logging/Logger;", "logger", "Ljava/util/logging/Logger;", "a", "()Ljava/util/logging/Logger;", "Lde/e;", "INSTANCE", "Lde/e;", "<init>", "()V", "okhttp"}, k = 1, mv = {1, 6, 0})
    /* renamed from: de.e$b */
    /* loaded from: classes2.dex */
    public static final class b {
        private b() {
        }

        public /* synthetic */ b(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        public final Logger a() {
            return TaskRunner.f10945j;
        }
    }

    /* compiled from: TaskRunner.kt */
    @Metadata(bv = {}, d1 = {"\u0000,\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010\t\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\u0018\u00002\u00020\u0001B\u000f\u0012\u0006\u0010\u000e\u001a\u00020\r¢\u0006\u0004\b\u000f\u0010\u0010J\b\u0010\u0003\u001a\u00020\u0002H\u0016J\u0010\u0010\u0007\u001a\u00020\u00062\u0006\u0010\u0005\u001a\u00020\u0004H\u0016J\u0018\u0010\t\u001a\u00020\u00062\u0006\u0010\u0005\u001a\u00020\u00042\u0006\u0010\b\u001a\u00020\u0002H\u0016J\u0010\u0010\f\u001a\u00020\u00062\u0006\u0010\u000b\u001a\u00020\nH\u0016¨\u0006\u0011"}, d2 = {"Lde/e$c;", "Lde/e$a;", "", "c", "Lde/e;", "taskRunner", "Lma/f0;", "b", "nanos", "a", "Ljava/lang/Runnable;", "runnable", "execute", "Ljava/util/concurrent/ThreadFactory;", "threadFactory", "<init>", "(Ljava/util/concurrent/ThreadFactory;)V", "okhttp"}, k = 1, mv = {1, 6, 0})
    /* renamed from: de.e$c */
    /* loaded from: classes2.dex */
    public static final class c implements a {

        /* renamed from: a, reason: collision with root package name */
        private final ThreadPoolExecutor f10953a;

        public c(ThreadFactory threadFactory) {
            k.e(threadFactory, "threadFactory");
            this.f10953a = new ThreadPoolExecutor(0, Integer.MAX_VALUE, 60L, TimeUnit.SECONDS, new SynchronousQueue(), threadFactory);
        }

        @Override // de.TaskRunner.a
        public void a(TaskRunner taskRunner, long j10) {
            k.e(taskRunner, "taskRunner");
            long j11 = j10 / 1000000;
            long j12 = j10 - (1000000 * j11);
            if (j11 > 0 || j10 > 0) {
                taskRunner.wait(j11, (int) j12);
            }
        }

        @Override // de.TaskRunner.a
        public void b(TaskRunner taskRunner) {
            k.e(taskRunner, "taskRunner");
            taskRunner.notify();
        }

        @Override // de.TaskRunner.a
        public long c() {
            return System.nanoTime();
        }

        @Override // de.TaskRunner.a
        public void execute(Runnable runnable) {
            k.e(runnable, "runnable");
            this.f10953a.execute(runnable);
        }
    }

    /* compiled from: TaskRunner.kt */
    @Metadata(bv = {}, d1 = {"\u0000\u0011\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002*\u0001\u0000\b\n\u0018\u00002\u00020\u0001J\b\u0010\u0003\u001a\u00020\u0002H\u0016¨\u0006\u0004"}, d2 = {"de/e$d", "Ljava/lang/Runnable;", "Lma/f0;", "run", "okhttp"}, k = 1, mv = {1, 6, 0})
    /* renamed from: de.e$d */
    /* loaded from: classes2.dex */
    public static final class d implements Runnable {
        d() {
        }

        @Override // java.lang.Runnable
        public void run() {
            de.a d10;
            while (true) {
                TaskRunner taskRunner = TaskRunner.this;
                synchronized (taskRunner) {
                    d10 = taskRunner.d();
                }
                if (d10 == null) {
                    return;
                }
                de.d f10932c = d10.getF10932c();
                k.b(f10932c);
                TaskRunner taskRunner2 = TaskRunner.this;
                long j10 = -1;
                boolean isLoggable = TaskRunner.f10943h.a().isLoggable(Level.FINE);
                if (isLoggable) {
                    j10 = f10932c.getF10937a().getF10946a().c();
                    TaskLogger.c(d10, f10932c, "starting");
                }
                try {
                    try {
                        taskRunner2.j(d10);
                        Unit unit = Unit.f15173a;
                        if (isLoggable) {
                            TaskLogger.c(d10, f10932c, k.l("finished run in ", TaskLogger.b(f10932c.getF10937a().getF10946a().c() - j10)));
                        }
                    } finally {
                    }
                } catch (Throwable th) {
                    if (isLoggable) {
                        TaskLogger.c(d10, f10932c, k.l("failed a run in ", TaskLogger.b(f10932c.getF10937a().getF10946a().c() - j10)));
                    }
                    throw th;
                }
            }
        }
    }

    static {
        Logger logger = Logger.getLogger(TaskRunner.class.getName());
        k.d(logger, "getLogger(TaskRunner::class.java.name)");
        f10945j = logger;
    }

    public TaskRunner(a aVar) {
        k.e(aVar, "backend");
        this.f10946a = aVar;
        this.f10947b = DataLinkConstants.RUS_UPDATE;
        this.f10950e = new ArrayList();
        this.f10951f = new ArrayList();
        this.f10952g = new d();
    }

    private final void c(de.a aVar, long j10) {
        if (ae.d.f244h && !Thread.holdsLock(this)) {
            throw new AssertionError("Thread " + ((Object) Thread.currentThread().getName()) + " MUST hold lock on " + this);
        }
        de.d f10932c = aVar.getF10932c();
        k.b(f10932c);
        if (f10932c.getF10940d() == aVar) {
            boolean f10942f = f10932c.getF10942f();
            f10932c.m(false);
            f10932c.l(null);
            this.f10950e.remove(f10932c);
            if (j10 != -1 && !f10942f && !f10932c.getF10939c()) {
                f10932c.k(aVar, j10, true);
            }
            if (!f10932c.e().isEmpty()) {
                this.f10951f.add(f10932c);
                return;
            }
            return;
        }
        throw new IllegalStateException("Check failed.".toString());
    }

    private final void e(de.a aVar) {
        if (ae.d.f244h && !Thread.holdsLock(this)) {
            throw new AssertionError("Thread " + ((Object) Thread.currentThread().getName()) + " MUST hold lock on " + this);
        }
        aVar.g(-1L);
        de.d f10932c = aVar.getF10932c();
        k.b(f10932c);
        f10932c.e().remove(aVar);
        this.f10951f.remove(f10932c);
        f10932c.l(aVar);
        this.f10950e.add(f10932c);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void j(de.a aVar) {
        if (ae.d.f244h && Thread.holdsLock(this)) {
            throw new AssertionError("Thread " + ((Object) Thread.currentThread().getName()) + " MUST NOT hold lock on " + this);
        }
        Thread currentThread = Thread.currentThread();
        String name = currentThread.getName();
        currentThread.setName(aVar.getF10930a());
        try {
            long f10 = aVar.f();
            synchronized (this) {
                c(aVar, f10);
                Unit unit = Unit.f15173a;
            }
            currentThread.setName(name);
        } catch (Throwable th) {
            synchronized (this) {
                c(aVar, -1L);
                Unit unit2 = Unit.f15173a;
                currentThread.setName(name);
                throw th;
            }
        }
    }

    public final de.a d() {
        boolean z10;
        if (ae.d.f244h && !Thread.holdsLock(this)) {
            throw new AssertionError("Thread " + ((Object) Thread.currentThread().getName()) + " MUST hold lock on " + this);
        }
        while (!this.f10951f.isEmpty()) {
            long c10 = this.f10946a.c();
            long j10 = Long.MAX_VALUE;
            Iterator<de.d> it = this.f10951f.iterator();
            de.a aVar = null;
            while (true) {
                if (!it.hasNext()) {
                    z10 = false;
                    break;
                }
                de.a aVar2 = it.next().e().get(0);
                long max = Math.max(0L, aVar2.getF10933d() - c10);
                if (max > 0) {
                    j10 = Math.min(max, j10);
                } else {
                    if (aVar != null) {
                        z10 = true;
                        break;
                    }
                    aVar = aVar2;
                }
            }
            if (aVar != null) {
                e(aVar);
                if (z10 || (!this.f10948c && (!this.f10951f.isEmpty()))) {
                    this.f10946a.execute(this.f10952g);
                }
                return aVar;
            }
            if (this.f10948c) {
                if (j10 < this.f10949d - c10) {
                    this.f10946a.b(this);
                }
                return null;
            }
            this.f10948c = true;
            this.f10949d = c10 + j10;
            try {
                try {
                    this.f10946a.a(this, j10);
                } catch (InterruptedException unused) {
                    f();
                }
            } finally {
                this.f10948c = false;
            }
        }
        return null;
    }

    public final void f() {
        int size = this.f10950e.size() - 1;
        if (size >= 0) {
            while (true) {
                int i10 = size - 1;
                this.f10950e.get(size).b();
                if (i10 < 0) {
                    break;
                } else {
                    size = i10;
                }
            }
        }
        int size2 = this.f10951f.size() - 1;
        if (size2 < 0) {
            return;
        }
        while (true) {
            int i11 = size2 - 1;
            de.d dVar = this.f10951f.get(size2);
            dVar.b();
            if (dVar.e().isEmpty()) {
                this.f10951f.remove(size2);
            }
            if (i11 < 0) {
                return;
            } else {
                size2 = i11;
            }
        }
    }

    /* renamed from: g, reason: from getter */
    public final a getF10946a() {
        return this.f10946a;
    }

    public final void h(de.d taskQueue) {
        k.e(taskQueue, "taskQueue");
        if (ae.d.f244h && !Thread.holdsLock(this)) {
            throw new AssertionError("Thread " + ((Object) Thread.currentThread().getName()) + " MUST hold lock on " + this);
        }
        if (taskQueue.getF10940d() == null) {
            if (!taskQueue.e().isEmpty()) {
                ae.d.c(this.f10951f, taskQueue);
            } else {
                this.f10951f.remove(taskQueue);
            }
        }
        if (this.f10948c) {
            this.f10946a.b(this);
        } else {
            this.f10946a.execute(this.f10952g);
        }
    }

    public final de.d i() {
        int i10;
        synchronized (this) {
            i10 = this.f10947b;
            this.f10947b = i10 + 1;
        }
        return new de.d(this, k.l("Q", Integer.valueOf(i10)));
    }
}
