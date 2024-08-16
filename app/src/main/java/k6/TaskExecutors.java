package k6;

import android.os.Handler;
import android.os.Looper;
import java.util.concurrent.Executor;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import kotlin.Metadata;
import ma.j;
import ma.l;
import td.CoroutineName;
import td.Dispatchers;
import td.a2;
import td.h0;
import td.i0;
import za.DefaultConstructorMarker;
import za.Lambda;

/* compiled from: TaskExecutors.kt */
@Metadata(bv = {}, d1 = {"\u0000\u0016\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\n\u0018\u00002\u00020\u0001:\u0004\f\r\u0007\u0003B\t\b\u0002¢\u0006\u0004\b\n\u0010\u000bJ\u0006\u0010\u0003\u001a\u00020\u0002R\u001b\u0010\t\u001a\u00020\u00048FX\u0086\u0084\u0002¢\u0006\f\n\u0004\b\u0005\u0010\u0006\u001a\u0004\b\u0007\u0010\b¨\u0006\u000e"}, d2 = {"Lk6/f;", "", "Ljava/util/concurrent/Executor;", "d", "Lk6/f$a;", "background$delegate", "Lma/h;", "c", "()Lk6/f$a;", "background", "<init>", "()V", "a", "b", "com.oplus.deepthinker.sdk_release"}, k = 1, mv = {1, 6, 0})
/* renamed from: k6.f, reason: use source file name */
/* loaded from: classes.dex */
public final class TaskExecutors {

    /* renamed from: f, reason: collision with root package name */
    public static final b f14056f = new b(null);

    /* renamed from: g, reason: collision with root package name */
    private static volatile TaskExecutors f14057g;

    /* renamed from: a, reason: collision with root package name */
    private final ma.h f14058a;

    /* renamed from: b, reason: collision with root package name */
    private final ma.h f14059b;

    /* renamed from: c, reason: collision with root package name */
    private final ma.h f14060c;

    /* renamed from: d, reason: collision with root package name */
    private final ma.h f14061d;

    /* renamed from: e, reason: collision with root package name */
    private Executor f14062e;

    /* compiled from: TaskExecutors.kt */
    @Metadata(bv = {}, d1 = {"\u0000\u001c\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\b\u0018\u00002\u00020\u0001B\u0007¢\u0006\u0004\b\f\u0010\rJ\u0012\u0010\u0005\u001a\u00020\u00042\b\u0010\u0003\u001a\u0004\u0018\u00010\u0002H\u0016R\u001b\u0010\u000b\u001a\u00020\u00068BX\u0082\u0084\u0002¢\u0006\f\n\u0004\b\u0007\u0010\b\u001a\u0004\b\t\u0010\n¨\u0006\u000e"}, d2 = {"Lk6/f$a;", "Ljava/util/concurrent/Executor;", "Ljava/lang/Runnable;", "command", "Lma/f0;", "execute", "Ljava/util/concurrent/ThreadPoolExecutor;", "executor$delegate", "Lma/h;", "a", "()Ljava/util/concurrent/ThreadPoolExecutor;", "executor", "<init>", "()V", "com.oplus.deepthinker.sdk_release"}, k = 1, mv = {1, 6, 0})
    /* renamed from: k6.f$a */
    /* loaded from: classes.dex */
    public static final class a implements Executor {

        /* renamed from: e, reason: collision with root package name */
        private final ma.h f14063e;

        /* compiled from: TaskExecutors.kt */
        @Metadata(bv = {}, d1 = {"\u0000\b\n\u0002\u0018\u0002\n\u0002\b\u0002\u0010\u0001\u001a\u00020\u0000H\n¢\u0006\u0004\b\u0001\u0010\u0002"}, d2 = {"Ljava/util/concurrent/ThreadPoolExecutor;", "a", "()Ljava/util/concurrent/ThreadPoolExecutor;"}, k = 3, mv = {1, 6, 0})
        /* renamed from: k6.f$a$a, reason: collision with other inner class name */
        /* loaded from: classes.dex */
        static final class C0069a extends Lambda implements ya.a<ThreadPoolExecutor> {

            /* renamed from: e, reason: collision with root package name */
            public static final C0069a f14064e = new C0069a();

            C0069a() {
                super(0);
            }

            @Override // ya.a
            /* renamed from: a, reason: merged with bridge method [inline-methods] */
            public final ThreadPoolExecutor invoke() {
                return new ThreadPoolExecutor(0, Integer.MAX_VALUE, 60L, TimeUnit.SECONDS, new SynchronousQueue(), new ThreadPoolExecutor.DiscardPolicy());
            }
        }

        public a() {
            ma.h a10;
            a10 = j.a(l.SYNCHRONIZED, C0069a.f14064e);
            this.f14063e = a10;
        }

        private final ThreadPoolExecutor a() {
            return (ThreadPoolExecutor) this.f14063e.getValue();
        }

        @Override // java.util.concurrent.Executor
        public void execute(Runnable runnable) {
            if (runnable == null) {
                return;
            }
            a().execute(runnable);
        }
    }

    /* compiled from: TaskExecutors.kt */
    @Metadata(bv = {}, d1 = {"\u0000\u001e\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0010\t\n\u0002\b\u0007\b\u0086\u0003\u0018\u00002\u00020\u0001B\t\b\u0002¢\u0006\u0004\b\f\u0010\rJ\u0006\u0010\u0003\u001a\u00020\u0002R\u0014\u0010\u0005\u001a\u00020\u00048\u0002X\u0082T¢\u0006\u0006\n\u0004\b\u0005\u0010\u0006R\u0014\u0010\b\u001a\u00020\u00078\u0002X\u0082T¢\u0006\u0006\n\u0004\b\b\u0010\tR\u0018\u0010\n\u001a\u0004\u0018\u00010\u00028\u0002@\u0002X\u0082\u000e¢\u0006\u0006\n\u0004\b\n\u0010\u000b¨\u0006\u000e"}, d2 = {"Lk6/f$b;", "", "Lk6/f;", "a", "", "TAG", "Ljava/lang/String;", "", "THREAD_KEEP_ALIVE_TIME", "J", "instance", "Lk6/f;", "<init>", "()V", "com.oplus.deepthinker.sdk_release"}, k = 1, mv = {1, 6, 0})
    /* renamed from: k6.f$b */
    /* loaded from: classes.dex */
    public static final class b {
        private b() {
        }

        public /* synthetic */ b(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        public final TaskExecutors a() {
            TaskExecutors taskExecutors = TaskExecutors.f14057g;
            if (taskExecutors == null) {
                synchronized (this) {
                    taskExecutors = TaskExecutors.f14057g;
                    if (taskExecutors == null) {
                        taskExecutors = new TaskExecutors(null);
                        b bVar = TaskExecutors.f14056f;
                        TaskExecutors.f14057g = taskExecutors;
                    }
                }
            }
            return taskExecutors;
        }
    }

    /* compiled from: TaskExecutors.kt */
    @Metadata(bv = {}, d1 = {"\u0000\u0016\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\u0018\u00002\u00020\u0001B\u0007¢\u0006\u0004\b\u0006\u0010\u0007J\u0012\u0010\u0005\u001a\u00020\u00042\b\u0010\u0003\u001a\u0004\u0018\u00010\u0002H\u0016¨\u0006\b"}, d2 = {"Lk6/f$c;", "Ljava/util/concurrent/Executor;", "Ljava/lang/Runnable;", "command", "Lma/f0;", "execute", "<init>", "()V", "com.oplus.deepthinker.sdk_release"}, k = 1, mv = {1, 6, 0})
    /* renamed from: k6.f$c */
    /* loaded from: classes.dex */
    public static final class c implements Executor {
        @Override // java.util.concurrent.Executor
        public void execute(Runnable runnable) {
            if (runnable == null) {
                return;
            }
            runnable.run();
        }
    }

    /* compiled from: TaskExecutors.kt */
    @Metadata(bv = {}, d1 = {"\u0000\u001c\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\b\u0018\u00002\u00020\u0001B\u0007¢\u0006\u0004\b\f\u0010\rJ\u0012\u0010\u0005\u001a\u00020\u00042\b\u0010\u0003\u001a\u0004\u0018\u00010\u0002H\u0016R\u001b\u0010\u000b\u001a\u00020\u00068BX\u0082\u0084\u0002¢\u0006\f\n\u0004\b\u0007\u0010\b\u001a\u0004\b\t\u0010\n¨\u0006\u000e"}, d2 = {"Lk6/f$d;", "Ljava/util/concurrent/Executor;", "Ljava/lang/Runnable;", "command", "Lma/f0;", "execute", "Landroid/os/Handler;", "handler$delegate", "Lma/h;", "a", "()Landroid/os/Handler;", "handler", "<init>", "()V", "com.oplus.deepthinker.sdk_release"}, k = 1, mv = {1, 6, 0})
    /* renamed from: k6.f$d */
    /* loaded from: classes.dex */
    public static final class d implements Executor {

        /* renamed from: e, reason: collision with root package name */
        private final ma.h f14065e;

        /* compiled from: TaskExecutors.kt */
        @Metadata(bv = {}, d1 = {"\u0000\b\n\u0002\u0018\u0002\n\u0002\b\u0002\u0010\u0001\u001a\u00020\u0000H\n¢\u0006\u0004\b\u0001\u0010\u0002"}, d2 = {"Landroid/os/Handler;", "a", "()Landroid/os/Handler;"}, k = 3, mv = {1, 6, 0})
        /* renamed from: k6.f$d$a */
        /* loaded from: classes.dex */
        static final class a extends Lambda implements ya.a<Handler> {

            /* renamed from: e, reason: collision with root package name */
            public static final a f14066e = new a();

            a() {
                super(0);
            }

            @Override // ya.a
            /* renamed from: a, reason: merged with bridge method [inline-methods] */
            public final Handler invoke() {
                return new Handler(Looper.getMainLooper());
            }
        }

        public d() {
            ma.h a10;
            a10 = j.a(l.SYNCHRONIZED, a.f14066e);
            this.f14065e = a10;
        }

        private final Handler a() {
            return (Handler) this.f14065e.getValue();
        }

        @Override // java.util.concurrent.Executor
        public void execute(Runnable runnable) {
            if (runnable == null) {
                return;
            }
            a().post(runnable);
        }
    }

    /* compiled from: TaskExecutors.kt */
    @Metadata(bv = {}, d1 = {"\u0000\b\n\u0002\u0018\u0002\n\u0002\b\u0002\u0010\u0001\u001a\u00020\u0000H\n¢\u0006\u0004\b\u0001\u0010\u0002"}, d2 = {"Lk6/f$a;", "a", "()Lk6/f$a;"}, k = 3, mv = {1, 6, 0})
    /* renamed from: k6.f$e */
    /* loaded from: classes.dex */
    static final class e extends Lambda implements ya.a<a> {

        /* renamed from: e, reason: collision with root package name */
        public static final e f14067e = new e();

        e() {
            super(0);
        }

        @Override // ya.a
        /* renamed from: a, reason: merged with bridge method [inline-methods] */
        public final a invoke() {
            return new a();
        }
    }

    /* compiled from: TaskExecutors.kt */
    @Metadata(bv = {}, d1 = {"\u0000\b\n\u0002\u0018\u0002\n\u0002\b\u0002\u0010\u0001\u001a\u00020\u0000H\n¢\u0006\u0004\b\u0001\u0010\u0002"}, d2 = {"Ltd/h0;", "a", "()Ltd/h0;"}, k = 3, mv = {1, 6, 0})
    /* renamed from: k6.f$f */
    /* loaded from: classes.dex */
    static final class f extends Lambda implements ya.a<h0> {

        /* renamed from: e, reason: collision with root package name */
        public static final f f14068e = new f();

        f() {
            super(0);
        }

        @Override // ya.a
        /* renamed from: a, reason: merged with bridge method [inline-methods] */
        public final h0 invoke() {
            return i0.a(new CoroutineName("TaskExecutors").o0(Dispatchers.a()).o0(a2.b(null, 1, null)));
        }
    }

    /* compiled from: TaskExecutors.kt */
    @Metadata(bv = {}, d1 = {"\u0000\b\n\u0002\u0018\u0002\n\u0002\b\u0002\u0010\u0001\u001a\u00020\u0000H\n¢\u0006\u0004\b\u0001\u0010\u0002"}, d2 = {"Lk6/f$c;", "a", "()Lk6/f$c;"}, k = 3, mv = {1, 6, 0})
    /* renamed from: k6.f$g */
    /* loaded from: classes.dex */
    static final class g extends Lambda implements ya.a<c> {

        /* renamed from: e, reason: collision with root package name */
        public static final g f14069e = new g();

        g() {
            super(0);
        }

        @Override // ya.a
        /* renamed from: a, reason: merged with bridge method [inline-methods] */
        public final c invoke() {
            return new c();
        }
    }

    /* compiled from: TaskExecutors.kt */
    @Metadata(bv = {}, d1 = {"\u0000\b\n\u0002\u0018\u0002\n\u0002\b\u0002\u0010\u0001\u001a\u00020\u0000H\n¢\u0006\u0004\b\u0001\u0010\u0002"}, d2 = {"Lk6/f$d;", "a", "()Lk6/f$d;"}, k = 3, mv = {1, 6, 0})
    /* renamed from: k6.f$h */
    /* loaded from: classes.dex */
    static final class h extends Lambda implements ya.a<d> {

        /* renamed from: e, reason: collision with root package name */
        public static final h f14070e = new h();

        h() {
            super(0);
        }

        @Override // ya.a
        /* renamed from: a, reason: merged with bridge method [inline-methods] */
        public final d invoke() {
            return new d();
        }
    }

    private TaskExecutors() {
        ma.h a10;
        ma.h a11;
        ma.h a12;
        ma.h a13;
        l lVar = l.SYNCHRONIZED;
        a10 = j.a(lVar, h.f14070e);
        this.f14058a = a10;
        a11 = j.a(lVar, e.f14067e);
        this.f14059b = a11;
        a12 = j.a(lVar, g.f14069e);
        this.f14060c = a12;
        a13 = j.a(lVar, f.f14068e);
        this.f14061d = a13;
    }

    public /* synthetic */ TaskExecutors(DefaultConstructorMarker defaultConstructorMarker) {
        this();
    }

    public final a c() {
        return (a) this.f14059b.getValue();
    }

    public final Executor d() {
        Executor executor = this.f14062e;
        return executor == null ? c() : executor;
    }
}
