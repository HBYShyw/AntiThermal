package ee;

import ce.CacheInterceptor;
import fe.BridgeInterceptor;
import fe.CallServerInterceptor;
import fe.RetryAndFollowUpInterceptor;
import ie.Platform;
import java.io.IOException;
import java.io.InterruptedIOException;
import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSocketFactory;
import kotlin.Metadata;
import kotlin.collections.MutableCollections;
import ma.Unit;
import me.AsyncTimeout;
import za.k;
import zd.Address;
import zd.CertificatePinner;
import zd.EventListener;
import zd.HttpUrl;
import zd.OkHttpClient;
import zd.b0;
import zd.p;
import zd.z;

/* compiled from: RealCall.kt */
@Metadata(bv = {}, d1 = {"\u0000n\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\b\n\u0002\u0018\u0002\n\u0002\b\t\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0017\u0018\u00002\u00020\u0001:\u0002NOB\u001f\u0012\u0006\u00105\u001a\u000204\u0012\u0006\u0010J\u001a\u00020\u0018\u0012\u0006\u0010K\u001a\u00020\u0012¢\u0006\u0004\bL\u0010MJ\b\u0010\u0003\u001a\u00020\u0002H\u0002J#\u0010\u0006\u001a\u00028\u0000\"\n\b\u0000\u0010\u0005*\u0004\u0018\u00010\u00042\u0006\u0010\u0003\u001a\u00028\u0000H\u0002¢\u0006\u0004\b\u0006\u0010\u0007J#\u0010\t\u001a\u00028\u0000\"\n\b\u0000\u0010\u0005*\u0004\u0018\u00010\u00042\u0006\u0010\b\u001a\u00028\u0000H\u0002¢\u0006\u0004\b\t\u0010\u0007J\u0010\u0010\r\u001a\u00020\f2\u0006\u0010\u000b\u001a\u00020\nH\u0002J\b\u0010\u000f\u001a\u00020\u000eH\u0002J\b\u0010\u0010\u001a\u00020\u0000H\u0016J\b\u0010\u0011\u001a\u00020\u0002H\u0016J\b\u0010\u0013\u001a\u00020\u0012H\u0016J\b\u0010\u0015\u001a\u00020\u0014H\u0016J\u000f\u0010\u0016\u001a\u00020\u0014H\u0000¢\u0006\u0004\b\u0016\u0010\u0017J\u0016\u0010\u001b\u001a\u00020\u00022\u0006\u0010\u0019\u001a\u00020\u00182\u0006\u0010\u001a\u001a\u00020\u0012J\u0017\u0010\u001f\u001a\u00020\u001e2\u0006\u0010\u001d\u001a\u00020\u001cH\u0000¢\u0006\u0004\b\u001f\u0010 J\u000e\u0010#\u001a\u00020\u00022\u0006\u0010\"\u001a\u00020!J;\u0010'\u001a\u00028\u0000\"\n\b\u0000\u0010\u0005*\u0004\u0018\u00010\u00042\u0006\u0010$\u001a\u00020\u001e2\u0006\u0010%\u001a\u00020\u00122\u0006\u0010&\u001a\u00020\u00122\u0006\u0010\u0003\u001a\u00028\u0000H\u0000¢\u0006\u0004\b'\u0010(J\u001b\u0010)\u001a\u0004\u0018\u00010\u00042\b\u0010\u0003\u001a\u0004\u0018\u00010\u0004H\u0000¢\u0006\u0004\b)\u0010\u0007J\u0011\u0010+\u001a\u0004\u0018\u00010*H\u0000¢\u0006\u0004\b+\u0010,J\u0006\u0010-\u001a\u00020\u0002J\u0017\u0010/\u001a\u00020\u00022\u0006\u0010.\u001a\u00020\u0012H\u0000¢\u0006\u0004\b/\u00100J\u0006\u00101\u001a\u00020\u0012J\u000f\u00102\u001a\u00020\u000eH\u0000¢\u0006\u0004\b2\u00103R\u0017\u00105\u001a\u0002048\u0006¢\u0006\f\n\u0004\b5\u00106\u001a\u0004\b7\u00108R\u001a\u0010:\u001a\u0002098\u0000X\u0080\u0004¢\u0006\f\n\u0004\b:\u0010;\u001a\u0004\b<\u0010=R(\u0010\"\u001a\u0004\u0018\u00010!2\b\u0010>\u001a\u0004\u0018\u00010!8\u0006@BX\u0086\u000e¢\u0006\f\n\u0004\b\"\u0010?\u001a\u0004\b@\u0010AR(\u0010B\u001a\u0004\u0018\u00010\u001e2\b\u0010>\u001a\u0004\u0018\u00010\u001e8\u0000@BX\u0080\u000e¢\u0006\f\n\u0004\bB\u0010C\u001a\u0004\bD\u0010ER$\u0010F\u001a\u0004\u0018\u00010!8\u0006@\u0006X\u0086\u000e¢\u0006\u0012\n\u0004\bF\u0010?\u001a\u0004\bG\u0010A\"\u0004\bH\u0010I¨\u0006P"}, d2 = {"Lee/e;", "Lzd/e;", "Lma/f0;", "e", "Ljava/io/IOException;", "E", "d", "(Ljava/io/IOException;)Ljava/io/IOException;", "cause", "z", "Lzd/u;", "url", "Lzd/a;", "h", "", "A", "g", "f", "", "r", "Lzd/b0;", "execute", "p", "()Lzd/b0;", "Lzd/z;", "request", "newExchangeFinder", "i", "Lfe/g;", "chain", "Lee/c;", "q", "(Lfe/g;)Lee/c;", "Lee/f;", "connection", "c", "exchange", "requestDone", "responseDone", "s", "(Lee/c;ZZLjava/io/IOException;)Ljava/io/IOException;", "t", "Ljava/net/Socket;", "v", "()Ljava/net/Socket;", "y", "closeExchange", "j", "(Z)V", "w", "u", "()Ljava/lang/String;", "Lzd/x;", "client", "Lzd/x;", "k", "()Lzd/x;", "Lzd/r;", "eventListener", "Lzd/r;", "n", "()Lzd/r;", "<set-?>", "Lee/f;", "l", "()Lee/f;", "interceptorScopedExchange", "Lee/c;", "o", "()Lee/c;", "connectionToCancel", "getConnectionToCancel", "x", "(Lee/f;)V", "originalRequest", "forWebSocket", "<init>", "(Lzd/x;Lzd/z;Z)V", "a", "b", "okhttp"}, k = 1, mv = {1, 6, 0})
/* loaded from: classes2.dex */
public final class e implements zd.e {

    /* renamed from: e, reason: collision with root package name */
    private final OkHttpClient f11186e;

    /* renamed from: f, reason: collision with root package name */
    private final z f11187f;

    /* renamed from: g, reason: collision with root package name */
    private final boolean f11188g;

    /* renamed from: h, reason: collision with root package name */
    private final RealConnectionPool f11189h;

    /* renamed from: i, reason: collision with root package name */
    private final EventListener f11190i;

    /* renamed from: j, reason: collision with root package name */
    private final c f11191j;

    /* renamed from: k, reason: collision with root package name */
    private final AtomicBoolean f11192k;

    /* renamed from: l, reason: collision with root package name */
    private Object f11193l;

    /* renamed from: m, reason: collision with root package name */
    private ExchangeFinder f11194m;

    /* renamed from: n, reason: collision with root package name */
    private RealConnection f11195n;

    /* renamed from: o, reason: collision with root package name */
    private boolean f11196o;

    /* renamed from: p, reason: collision with root package name */
    private Exchange f11197p;

    /* renamed from: q, reason: collision with root package name */
    private boolean f11198q;

    /* renamed from: r, reason: collision with root package name */
    private boolean f11199r;

    /* renamed from: s, reason: collision with root package name */
    private boolean f11200s;

    /* renamed from: t, reason: collision with root package name */
    private volatile boolean f11201t;

    /* renamed from: u, reason: collision with root package name */
    private volatile Exchange f11202u;

    /* renamed from: v, reason: collision with root package name */
    private volatile RealConnection f11203v;

    /* compiled from: RealCall.kt */
    @Metadata(bv = {}, d1 = {"\u0000\u001e\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0006\b\u0080\u0004\u0018\u00002\u00020\u0001J\u000e\u0010\u0005\u001a\u00020\u00042\u0006\u0010\u0003\u001a\u00020\u0002J\b\u0010\u0006\u001a\u00020\u0004H\u0016R$\u0010\t\u001a\u00020\u00072\u0006\u0010\b\u001a\u00020\u00078\u0006@BX\u0086\u000e¢\u0006\f\n\u0004\b\t\u0010\n\u001a\u0004\b\u000b\u0010\f¨\u0006\r"}, d2 = {"Lee/e$a;", "Ljava/lang/Runnable;", "Ljava/util/concurrent/ExecutorService;", "executorService", "Lma/f0;", "a", "run", "Ljava/util/concurrent/atomic/AtomicInteger;", "<set-?>", "callsPerHost", "Ljava/util/concurrent/atomic/AtomicInteger;", "b", "()Ljava/util/concurrent/atomic/AtomicInteger;", "okhttp"}, k = 1, mv = {1, 6, 0})
    /* loaded from: classes2.dex */
    public final class a implements Runnable {

        /* renamed from: e, reason: collision with root package name */
        private final zd.f f11204e;

        /* renamed from: f, reason: collision with root package name */
        private volatile AtomicInteger f11205f;

        /* renamed from: g, reason: collision with root package name */
        final /* synthetic */ e f11206g;

        public final void a(ExecutorService executorService) {
            k.e(executorService, "executorService");
            p f20739e = this.f11206g.getF11186e().getF20739e();
            if (ae.d.f244h && Thread.holdsLock(f20739e)) {
                throw new AssertionError("Thread " + ((Object) Thread.currentThread().getName()) + " MUST NOT hold lock on " + f20739e);
            }
            try {
                try {
                    executorService.execute(this);
                } catch (RejectedExecutionException e10) {
                    InterruptedIOException interruptedIOException = new InterruptedIOException("executor rejected");
                    interruptedIOException.initCause(e10);
                    this.f11206g.t(interruptedIOException);
                    this.f11204e.a(this.f11206g, interruptedIOException);
                    this.f11206g.getF11186e().getF20739e().d(this);
                }
            } catch (Throwable th) {
                this.f11206g.getF11186e().getF20739e().d(this);
                throw th;
            }
        }

        /* renamed from: b, reason: from getter */
        public final AtomicInteger getF11205f() {
            return this.f11205f;
        }

        @Override // java.lang.Runnable
        public void run() {
            boolean z10;
            Throwable th;
            IOException e10;
            p f20739e;
            String l10 = k.l("OkHttp ", this.f11206g.u());
            e eVar = this.f11206g;
            Thread currentThread = Thread.currentThread();
            String name = currentThread.getName();
            currentThread.setName(l10);
            try {
                try {
                    eVar.f11191j.v();
                    try {
                        z10 = true;
                    } catch (IOException e11) {
                        z10 = false;
                        e10 = e11;
                    } catch (Throwable th2) {
                        z10 = false;
                        th = th2;
                    }
                    try {
                        this.f11204e.b(eVar, eVar.p());
                        f20739e = eVar.getF11186e().getF20739e();
                    } catch (IOException e12) {
                        e10 = e12;
                        if (z10) {
                            Platform.f12870a.g().j(k.l("Callback failure for ", eVar.A()), 4, e10);
                        } else {
                            this.f11204e.a(eVar, e10);
                        }
                        f20739e = eVar.getF11186e().getF20739e();
                        f20739e.d(this);
                    } catch (Throwable th3) {
                        th = th3;
                        eVar.f();
                        if (!z10) {
                            IOException iOException = new IOException(k.l("canceled due to ", th));
                            ma.b.a(iOException, th);
                            this.f11204e.a(eVar, iOException);
                        }
                        throw th;
                    }
                    f20739e.d(this);
                } catch (Throwable th4) {
                    eVar.getF11186e().getF20739e().d(this);
                    throw th4;
                }
            } finally {
                currentThread.setName(name);
            }
        }
    }

    /* compiled from: RealCall.kt */
    @Metadata(bv = {}, d1 = {"\u0000\u0014\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\b\b\u0000\u0018\u00002\b\u0012\u0004\u0012\u00020\u00020\u0001B\u0019\u0012\u0006\u0010\b\u001a\u00020\u0002\u0012\b\u0010\u0004\u001a\u0004\u0018\u00010\u0003¢\u0006\u0004\b\t\u0010\nR\u0019\u0010\u0004\u001a\u0004\u0018\u00010\u00038\u0006¢\u0006\f\n\u0004\b\u0004\u0010\u0005\u001a\u0004\b\u0006\u0010\u0007¨\u0006\u000b"}, d2 = {"Lee/e$b;", "Ljava/lang/ref/WeakReference;", "Lee/e;", "", "callStackTrace", "Ljava/lang/Object;", "a", "()Ljava/lang/Object;", "referent", "<init>", "(Lee/e;Ljava/lang/Object;)V", "okhttp"}, k = 1, mv = {1, 6, 0})
    /* loaded from: classes2.dex */
    public static final class b extends WeakReference<e> {

        /* renamed from: a, reason: collision with root package name */
        private final Object f11207a;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        public b(e eVar, Object obj) {
            super(eVar);
            k.e(eVar, "referent");
            this.f11207a = obj;
        }

        /* renamed from: a, reason: from getter */
        public final Object getF11207a() {
            return this.f11207a;
        }
    }

    /* compiled from: RealCall.kt */
    @Metadata(bv = {}, d1 = {"\u0000\u0011\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002*\u0001\u0000\b\n\u0018\u00002\u00020\u0001J\b\u0010\u0003\u001a\u00020\u0002H\u0014¨\u0006\u0004"}, d2 = {"ee/e$c", "Lme/c;", "Lma/f0;", "B", "okhttp"}, k = 1, mv = {1, 6, 0})
    /* loaded from: classes2.dex */
    public static final class c extends AsyncTimeout {
        c() {
        }

        @Override // me.AsyncTimeout
        protected void B() {
            e.this.f();
        }
    }

    public e(OkHttpClient okHttpClient, z zVar, boolean z10) {
        k.e(okHttpClient, "client");
        k.e(zVar, "originalRequest");
        this.f11186e = okHttpClient;
        this.f11187f = zVar;
        this.f11188g = z10;
        this.f11189h = okHttpClient.getF20740f().getF20655a();
        this.f11190i = okHttpClient.getF20743i().a(this);
        c cVar = new c();
        cVar.g(getF11186e().getA(), TimeUnit.MILLISECONDS);
        this.f11191j = cVar;
        this.f11192k = new AtomicBoolean();
        this.f11200s = true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final String A() {
        StringBuilder sb2 = new StringBuilder();
        sb2.append(getF11201t() ? "canceled " : "");
        sb2.append(this.f11188g ? "web socket" : "call");
        sb2.append(" to ");
        sb2.append(u());
        return sb2.toString();
    }

    private final <E extends IOException> E d(E e10) {
        Socket v7;
        boolean z10 = ae.d.f244h;
        if (z10 && Thread.holdsLock(this)) {
            throw new AssertionError("Thread " + ((Object) Thread.currentThread().getName()) + " MUST NOT hold lock on " + this);
        }
        RealConnection realConnection = this.f11195n;
        if (realConnection != null) {
            if (z10 && Thread.holdsLock(realConnection)) {
                throw new AssertionError("Thread " + ((Object) Thread.currentThread().getName()) + " MUST NOT hold lock on " + realConnection);
            }
            synchronized (realConnection) {
                v7 = v();
            }
            if (this.f11195n == null) {
                if (v7 != null) {
                    ae.d.m(v7);
                }
                this.f11190i.k(this, realConnection);
            } else {
                if (!(v7 == null)) {
                    throw new IllegalStateException("Check failed.".toString());
                }
            }
        }
        E e11 = (E) z(e10);
        if (e10 != null) {
            EventListener eventListener = this.f11190i;
            k.b(e11);
            eventListener.d(this, e11);
        } else {
            this.f11190i.c(this);
        }
        return e11;
    }

    private final void e() {
        this.f11193l = Platform.f12870a.g().h("response.body().close()");
        this.f11190i.e(this);
    }

    private final Address h(HttpUrl url) {
        SSLSocketFactory sSLSocketFactory;
        HostnameVerifier hostnameVerifier;
        CertificatePinner certificatePinner;
        if (url.getF20722j()) {
            SSLSocketFactory D = this.f11186e.D();
            hostnameVerifier = this.f11186e.getF20758x();
            sSLSocketFactory = D;
            certificatePinner = this.f11186e.getF20759y();
        } else {
            sSLSocketFactory = null;
            hostnameVerifier = null;
            certificatePinner = null;
        }
        return new Address(url.getF20716d(), url.getF20717e(), this.f11186e.getF20749o(), this.f11186e.getF20753s(), sSLSocketFactory, hostnameVerifier, certificatePinner, this.f11186e.getF20752r(), this.f11186e.getF20750p(), this.f11186e.w(), this.f11186e.i(), this.f11186e.getF20751q());
    }

    private final <E extends IOException> E z(E cause) {
        if (this.f11196o || !this.f11191j.w()) {
            return cause;
        }
        InterruptedIOException interruptedIOException = new InterruptedIOException("timeout");
        if (cause != null) {
            interruptedIOException.initCause(cause);
        }
        return interruptedIOException;
    }

    public final void c(RealConnection realConnection) {
        k.e(realConnection, "connection");
        if (ae.d.f244h && !Thread.holdsLock(realConnection)) {
            throw new AssertionError("Thread " + ((Object) Thread.currentThread().getName()) + " MUST hold lock on " + realConnection);
        }
        if (this.f11195n == null) {
            this.f11195n = realConnection;
            realConnection.n().add(new b(this, this.f11193l));
            return;
        }
        throw new IllegalStateException("Check failed.".toString());
    }

    @Override // zd.e
    public b0 execute() {
        if (this.f11192k.compareAndSet(false, true)) {
            this.f11191j.v();
            e();
            try {
                this.f11186e.getF20739e().a(this);
                return p();
            } finally {
                this.f11186e.getF20739e().e(this);
            }
        }
        throw new IllegalStateException("Already Executed".toString());
    }

    public void f() {
        if (this.f11201t) {
            return;
        }
        this.f11201t = true;
        Exchange exchange = this.f11202u;
        if (exchange != null) {
            exchange.b();
        }
        RealConnection realConnection = this.f11203v;
        if (realConnection != null) {
            realConnection.d();
        }
        this.f11190i.f(this);
    }

    /* renamed from: g, reason: merged with bridge method [inline-methods] */
    public e clone() {
        return new e(this.f11186e, this.f11187f, this.f11188g);
    }

    public final void i(z zVar, boolean z10) {
        k.e(zVar, "request");
        if (this.f11197p == null) {
            synchronized (this) {
                if (!this.f11199r) {
                    if (!this.f11198q) {
                        Unit unit = Unit.f15173a;
                    } else {
                        throw new IllegalStateException("Check failed.".toString());
                    }
                } else {
                    throw new IllegalStateException("cannot make a new request because the previous response is still open: please call response.close()".toString());
                }
            }
            if (z10) {
                this.f11194m = new ExchangeFinder(this.f11189h, h(zVar.getF20796a()), this, this.f11190i);
                return;
            }
            return;
        }
        throw new IllegalStateException("Check failed.".toString());
    }

    public final void j(boolean closeExchange) {
        Exchange exchange;
        synchronized (this) {
            if (this.f11200s) {
                Unit unit = Unit.f15173a;
            } else {
                throw new IllegalStateException("released".toString());
            }
        }
        if (closeExchange && (exchange = this.f11202u) != null) {
            exchange.d();
        }
        this.f11197p = null;
    }

    /* renamed from: k, reason: from getter */
    public final OkHttpClient getF11186e() {
        return this.f11186e;
    }

    /* renamed from: l, reason: from getter */
    public final RealConnection getF11195n() {
        return this.f11195n;
    }

    /* renamed from: n, reason: from getter */
    public final EventListener getF11190i() {
        return this.f11190i;
    }

    /* renamed from: o, reason: from getter */
    public final Exchange getF11197p() {
        return this.f11197p;
    }

    /* JADX WARN: Removed duplicated region for block: B:25:0x00a2  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public final b0 p() {
        ArrayList arrayList = new ArrayList();
        MutableCollections.z(arrayList, this.f11186e.s());
        arrayList.add(new RetryAndFollowUpInterceptor(this.f11186e));
        arrayList.add(new BridgeInterceptor(this.f11186e.getF20748n()));
        this.f11186e.d();
        arrayList.add(new CacheInterceptor(null));
        arrayList.add(ConnectInterceptor.f11154a);
        if (!this.f11188g) {
            MutableCollections.z(arrayList, this.f11186e.t());
        }
        arrayList.add(new CallServerInterceptor(this.f11188g));
        boolean z10 = false;
        try {
            b0 a10 = new fe.g(this, arrayList, 0, null, this.f11187f, this.f11186e.getB(), this.f11186e.getC(), this.f11186e.getD()).a(this.f11187f);
            if (!getF11201t()) {
                t(null);
                return a10;
            }
            ae.d.l(a10);
            throw new IOException("Canceled");
        } catch (IOException e10) {
            try {
                IOException t7 = t(e10);
                if (t7 == null) {
                    throw new NullPointerException("null cannot be cast to non-null type kotlin.Throwable");
                }
                throw t7;
            } catch (Throwable th) {
                th = th;
                z10 = true;
                if (!z10) {
                    t(null);
                }
                throw th;
            }
        } catch (Throwable th2) {
            th = th2;
            if (!z10) {
            }
            throw th;
        }
    }

    public final Exchange q(fe.g chain) {
        k.e(chain, "chain");
        synchronized (this) {
            if (this.f11200s) {
                if (!this.f11199r) {
                    if (!this.f11198q) {
                        Unit unit = Unit.f15173a;
                    } else {
                        throw new IllegalStateException("Check failed.".toString());
                    }
                } else {
                    throw new IllegalStateException("Check failed.".toString());
                }
            } else {
                throw new IllegalStateException("released".toString());
            }
        }
        ExchangeFinder exchangeFinder = this.f11194m;
        k.b(exchangeFinder);
        Exchange exchange = new Exchange(this, this.f11190i, exchangeFinder, exchangeFinder.a(this.f11186e, chain));
        this.f11197p = exchange;
        this.f11202u = exchange;
        synchronized (this) {
            this.f11198q = true;
            this.f11199r = true;
        }
        if (this.f11201t) {
            throw new IOException("Canceled");
        }
        return exchange;
    }

    /* renamed from: r, reason: from getter */
    public boolean getF11201t() {
        return this.f11201t;
    }

    /* JADX WARN: Removed duplicated region for block: B:12:0x0021 A[Catch: all -> 0x0017, TryCatch #0 {all -> 0x0017, blocks: (B:44:0x0012, B:12:0x0021, B:14:0x0025, B:15:0x0027, B:17:0x002c, B:21:0x0035, B:23:0x0039, B:27:0x0042, B:9:0x001b), top: B:43:0x0012 }] */
    /* JADX WARN: Removed duplicated region for block: B:14:0x0025 A[Catch: all -> 0x0017, TryCatch #0 {all -> 0x0017, blocks: (B:44:0x0012, B:12:0x0021, B:14:0x0025, B:15:0x0027, B:17:0x002c, B:21:0x0035, B:23:0x0039, B:27:0x0042, B:9:0x001b), top: B:43:0x0012 }] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public final <E extends IOException> E s(Exchange exchange, boolean requestDone, boolean responseDone, E e10) {
        boolean z10;
        boolean z11;
        boolean z12;
        k.e(exchange, "exchange");
        if (!k.a(exchange, this.f11202u)) {
            return e10;
        }
        synchronized (this) {
            z10 = false;
            if (requestDone) {
                try {
                    if (!this.f11198q) {
                    }
                    if (requestDone) {
                        this.f11198q = false;
                    }
                    if (responseDone) {
                        this.f11199r = false;
                    }
                    z12 = this.f11198q;
                    boolean z13 = z12 && !this.f11199r;
                    if (!z12 && !this.f11199r && !this.f11200s) {
                        z10 = true;
                    }
                    z11 = z10;
                    z10 = z13;
                    Unit unit = Unit.f15173a;
                } catch (Throwable th) {
                    throw th;
                }
            }
            if (!responseDone || !this.f11199r) {
                z11 = false;
                Unit unit2 = Unit.f15173a;
            }
            if (requestDone) {
            }
            if (responseDone) {
            }
            z12 = this.f11198q;
            if (z12) {
            }
            if (!z12) {
                z10 = true;
            }
            z11 = z10;
            z10 = z13;
            Unit unit22 = Unit.f15173a;
        }
        if (z10) {
            this.f11202u = null;
            RealConnection realConnection = this.f11195n;
            if (realConnection != null) {
                realConnection.s();
            }
        }
        return z11 ? (E) d(e10) : e10;
    }

    public final IOException t(IOException e10) {
        boolean z10;
        synchronized (this) {
            z10 = false;
            if (this.f11200s) {
                this.f11200s = false;
                if (!this.f11198q && !this.f11199r) {
                    z10 = true;
                }
            }
            Unit unit = Unit.f15173a;
        }
        return z10 ? d(e10) : e10;
    }

    public final String u() {
        return this.f11187f.getF20796a().n();
    }

    public final Socket v() {
        RealConnection realConnection = this.f11195n;
        k.b(realConnection);
        if (ae.d.f244h && !Thread.holdsLock(realConnection)) {
            throw new AssertionError("Thread " + ((Object) Thread.currentThread().getName()) + " MUST hold lock on " + realConnection);
        }
        List<Reference<e>> n10 = realConnection.n();
        Iterator<Reference<e>> it = n10.iterator();
        int i10 = 0;
        while (true) {
            if (!it.hasNext()) {
                i10 = -1;
                break;
            }
            if (k.a(it.next().get(), this)) {
                break;
            }
            i10++;
        }
        if (i10 != -1) {
            n10.remove(i10);
            this.f11195n = null;
            if (n10.isEmpty()) {
                realConnection.B(System.nanoTime());
                if (this.f11189h.c(realConnection)) {
                    return realConnection.D();
                }
            }
            return null;
        }
        throw new IllegalStateException("Check failed.".toString());
    }

    public final boolean w() {
        ExchangeFinder exchangeFinder = this.f11194m;
        k.b(exchangeFinder);
        return exchangeFinder.e();
    }

    public final void x(RealConnection realConnection) {
        this.f11203v = realConnection;
    }

    public final void y() {
        if (!this.f11196o) {
            this.f11196o = true;
            this.f11191j.w();
            return;
        }
        throw new IllegalStateException("Check failed.".toString());
    }
}
