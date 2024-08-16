package me;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;
import ma.Unit;
import za.DefaultConstructorMarker;

/* compiled from: AsyncTimeout.kt */
/* renamed from: me.c, reason: use source file name */
/* loaded from: classes2.dex */
public class AsyncTimeout extends Timeout {

    /* renamed from: i, reason: collision with root package name */
    public static final a f15471i = new a(null);

    /* renamed from: j, reason: collision with root package name */
    private static final ReentrantLock f15472j;

    /* renamed from: k, reason: collision with root package name */
    private static final Condition f15473k;

    /* renamed from: l, reason: collision with root package name */
    private static final long f15474l;

    /* renamed from: m, reason: collision with root package name */
    private static final long f15475m;

    /* renamed from: n, reason: collision with root package name */
    private static AsyncTimeout f15476n;

    /* renamed from: f, reason: collision with root package name */
    private boolean f15477f;

    /* renamed from: g, reason: collision with root package name */
    private AsyncTimeout f15478g;

    /* renamed from: h, reason: collision with root package name */
    private long f15479h;

    /* compiled from: AsyncTimeout.kt */
    /* renamed from: me.c$a */
    /* loaded from: classes2.dex */
    public static final class a {
        private a() {
        }

        public /* synthetic */ a(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public final boolean d(AsyncTimeout asyncTimeout) {
            ReentrantLock f10 = AsyncTimeout.f15471i.f();
            f10.lock();
            try {
                if (!asyncTimeout.f15477f) {
                    return false;
                }
                asyncTimeout.f15477f = false;
                for (AsyncTimeout asyncTimeout2 = AsyncTimeout.f15476n; asyncTimeout2 != null; asyncTimeout2 = asyncTimeout2.f15478g) {
                    if (asyncTimeout2.f15478g == asyncTimeout) {
                        asyncTimeout2.f15478g = asyncTimeout.f15478g;
                        asyncTimeout.f15478g = null;
                        return false;
                    }
                }
                return true;
            } finally {
                f10.unlock();
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public final void g(AsyncTimeout asyncTimeout, long j10, boolean z10) {
            ReentrantLock f10 = AsyncTimeout.f15471i.f();
            f10.lock();
            try {
                if (!asyncTimeout.f15477f) {
                    asyncTimeout.f15477f = true;
                    if (AsyncTimeout.f15476n == null) {
                        AsyncTimeout.f15476n = new AsyncTimeout();
                        new b().start();
                    }
                    long nanoTime = System.nanoTime();
                    if (j10 != 0 && z10) {
                        asyncTimeout.f15479h = Math.min(j10, asyncTimeout.c() - nanoTime) + nanoTime;
                    } else if (j10 != 0) {
                        asyncTimeout.f15479h = j10 + nanoTime;
                    } else if (z10) {
                        asyncTimeout.f15479h = asyncTimeout.c();
                    } else {
                        throw new AssertionError();
                    }
                    long y4 = asyncTimeout.y(nanoTime);
                    AsyncTimeout asyncTimeout2 = AsyncTimeout.f15476n;
                    za.k.b(asyncTimeout2);
                    while (asyncTimeout2.f15478g != null) {
                        AsyncTimeout asyncTimeout3 = asyncTimeout2.f15478g;
                        za.k.b(asyncTimeout3);
                        if (y4 < asyncTimeout3.y(nanoTime)) {
                            break;
                        }
                        asyncTimeout2 = asyncTimeout2.f15478g;
                        za.k.b(asyncTimeout2);
                    }
                    asyncTimeout.f15478g = asyncTimeout2.f15478g;
                    asyncTimeout2.f15478g = asyncTimeout;
                    if (asyncTimeout2 == AsyncTimeout.f15476n) {
                        AsyncTimeout.f15471i.e().signal();
                    }
                    Unit unit = Unit.f15173a;
                    return;
                }
                throw new IllegalStateException("Unbalanced enter/exit".toString());
            } finally {
                f10.unlock();
            }
        }

        public final AsyncTimeout c() {
            AsyncTimeout asyncTimeout = AsyncTimeout.f15476n;
            za.k.b(asyncTimeout);
            AsyncTimeout asyncTimeout2 = asyncTimeout.f15478g;
            if (asyncTimeout2 == null) {
                long nanoTime = System.nanoTime();
                e().await(AsyncTimeout.f15474l, TimeUnit.MILLISECONDS);
                AsyncTimeout asyncTimeout3 = AsyncTimeout.f15476n;
                za.k.b(asyncTimeout3);
                if (asyncTimeout3.f15478g != null || System.nanoTime() - nanoTime < AsyncTimeout.f15475m) {
                    return null;
                }
                return AsyncTimeout.f15476n;
            }
            long y4 = asyncTimeout2.y(System.nanoTime());
            if (y4 > 0) {
                e().await(y4, TimeUnit.NANOSECONDS);
                return null;
            }
            AsyncTimeout asyncTimeout4 = AsyncTimeout.f15476n;
            za.k.b(asyncTimeout4);
            asyncTimeout4.f15478g = asyncTimeout2.f15478g;
            asyncTimeout2.f15478g = null;
            return asyncTimeout2;
        }

        public final Condition e() {
            return AsyncTimeout.f15473k;
        }

        public final ReentrantLock f() {
            return AsyncTimeout.f15472j;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* compiled from: AsyncTimeout.kt */
    /* renamed from: me.c$b */
    /* loaded from: classes2.dex */
    public static final class b extends Thread {
        public b() {
            super("Okio Watchdog");
            setDaemon(true);
        }

        @Override // java.lang.Thread, java.lang.Runnable
        public void run() {
            ReentrantLock f10;
            AsyncTimeout c10;
            while (true) {
                try {
                    a aVar = AsyncTimeout.f15471i;
                    f10 = aVar.f();
                    f10.lock();
                    try {
                        c10 = aVar.c();
                    } finally {
                        f10.unlock();
                    }
                } catch (InterruptedException unused) {
                    continue;
                }
                if (c10 == AsyncTimeout.f15476n) {
                    AsyncTimeout.f15476n = null;
                    return;
                }
                Unit unit = Unit.f15173a;
                f10.unlock();
                if (c10 != null) {
                    c10.B();
                }
            }
        }
    }

    /* compiled from: AsyncTimeout.kt */
    /* renamed from: me.c$c */
    /* loaded from: classes2.dex */
    public static final class c implements Sink {

        /* renamed from: f, reason: collision with root package name */
        final /* synthetic */ Sink f15481f;

        c(Sink sink) {
            this.f15481f = sink;
        }

        @Override // me.Sink
        /* renamed from: b, reason: merged with bridge method [inline-methods] */
        public AsyncTimeout timeout() {
            return AsyncTimeout.this;
        }

        @Override // me.Sink, java.io.Closeable, java.lang.AutoCloseable
        public void close() {
            AsyncTimeout asyncTimeout = AsyncTimeout.this;
            Sink sink = this.f15481f;
            asyncTimeout.v();
            try {
                sink.close();
                Unit unit = Unit.f15173a;
                if (asyncTimeout.w()) {
                    throw asyncTimeout.p(null);
                }
            } catch (IOException e10) {
                if (!asyncTimeout.w()) {
                    throw e10;
                }
                throw asyncTimeout.p(e10);
            } finally {
                asyncTimeout.w();
            }
        }

        @Override // me.Sink, java.io.Flushable
        public void flush() {
            AsyncTimeout asyncTimeout = AsyncTimeout.this;
            Sink sink = this.f15481f;
            asyncTimeout.v();
            try {
                sink.flush();
                Unit unit = Unit.f15173a;
                if (asyncTimeout.w()) {
                    throw asyncTimeout.p(null);
                }
            } catch (IOException e10) {
                if (!asyncTimeout.w()) {
                    throw e10;
                }
                throw asyncTimeout.p(e10);
            } finally {
                asyncTimeout.w();
            }
        }

        public String toString() {
            return "AsyncTimeout.sink(" + this.f15481f + ')';
        }

        @Override // me.Sink
        public void write(me.d dVar, long j10) {
            za.k.e(dVar, "source");
            me.b.b(dVar.v0(), 0L, j10);
            while (true) {
                long j11 = 0;
                if (j10 <= 0) {
                    return;
                }
                Segment segment = dVar.f15484e;
                za.k.b(segment);
                while (true) {
                    if (j11 >= 65536) {
                        break;
                    }
                    j11 += segment.f15533c - segment.f15532b;
                    if (j11 >= j10) {
                        j11 = j10;
                        break;
                    } else {
                        segment = segment.f15536f;
                        za.k.b(segment);
                    }
                }
                AsyncTimeout asyncTimeout = AsyncTimeout.this;
                Sink sink = this.f15481f;
                asyncTimeout.v();
                try {
                    sink.write(dVar, j11);
                    Unit unit = Unit.f15173a;
                    if (asyncTimeout.w()) {
                        throw asyncTimeout.p(null);
                    }
                    j10 -= j11;
                } catch (IOException e10) {
                    if (!asyncTimeout.w()) {
                        throw e10;
                    }
                    throw asyncTimeout.p(e10);
                } finally {
                    asyncTimeout.w();
                }
            }
        }
    }

    /* compiled from: AsyncTimeout.kt */
    /* renamed from: me.c$d */
    /* loaded from: classes2.dex */
    public static final class d implements Source {

        /* renamed from: f, reason: collision with root package name */
        final /* synthetic */ Source f15483f;

        d(Source source) {
            this.f15483f = source;
        }

        @Override // me.Source
        /* renamed from: b, reason: merged with bridge method [inline-methods] */
        public AsyncTimeout timeout() {
            return AsyncTimeout.this;
        }

        @Override // me.Source, java.io.Closeable, java.lang.AutoCloseable
        public void close() {
            AsyncTimeout asyncTimeout = AsyncTimeout.this;
            Source source = this.f15483f;
            asyncTimeout.v();
            try {
                source.close();
                Unit unit = Unit.f15173a;
                if (asyncTimeout.w()) {
                    throw asyncTimeout.p(null);
                }
            } catch (IOException e10) {
                if (!asyncTimeout.w()) {
                    throw e10;
                }
                throw asyncTimeout.p(e10);
            } finally {
                asyncTimeout.w();
            }
        }

        @Override // me.Source
        public long read(me.d dVar, long j10) {
            za.k.e(dVar, "sink");
            AsyncTimeout asyncTimeout = AsyncTimeout.this;
            Source source = this.f15483f;
            asyncTimeout.v();
            try {
                long read = source.read(dVar, j10);
                if (asyncTimeout.w()) {
                    throw asyncTimeout.p(null);
                }
                return read;
            } catch (IOException e10) {
                if (asyncTimeout.w()) {
                    throw asyncTimeout.p(e10);
                }
                throw e10;
            } finally {
                asyncTimeout.w();
            }
        }

        public String toString() {
            return "AsyncTimeout.source(" + this.f15483f + ')';
        }
    }

    static {
        ReentrantLock reentrantLock = new ReentrantLock();
        f15472j = reentrantLock;
        Condition newCondition = reentrantLock.newCondition();
        za.k.d(newCondition, "lock.newCondition()");
        f15473k = newCondition;
        long millis = TimeUnit.SECONDS.toMillis(60L);
        f15474l = millis;
        f15475m = TimeUnit.MILLISECONDS.toNanos(millis);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final long y(long j10) {
        return this.f15479h - j10;
    }

    public final Source A(Source source) {
        za.k.e(source, "source");
        return new d(source);
    }

    protected void B() {
    }

    public final IOException p(IOException iOException) {
        return x(iOException);
    }

    public final void v() {
        long h10 = h();
        boolean e10 = e();
        if (h10 != 0 || e10) {
            f15471i.g(this, h10, e10);
        }
    }

    public final boolean w() {
        return f15471i.d(this);
    }

    protected IOException x(IOException iOException) {
        InterruptedIOException interruptedIOException = new InterruptedIOException("timeout");
        if (iOException != null) {
            interruptedIOException.initCause(iOException);
        }
        return interruptedIOException;
    }

    public final Sink z(Sink sink) {
        za.k.e(sink, "sink");
        return new c(sink);
    }
}
