package ee;

import fe.ExchangeCodec;
import fe.RealResponseBody;
import java.io.IOException;
import java.net.ProtocolException;
import kotlin.Metadata;
import me.ForwardingSink;
import me.ForwardingSource;
import me.Sink;
import me.Source;
import me.n;
import za.k;
import zd.EventListener;
import zd.RequestBody;
import zd.ResponseBody;
import zd.b0;
import zd.z;

/* compiled from: Exchange.kt */
@Metadata(bv = {}, d1 = {"\u0000p\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\t\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\t\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\b\u0004\u0018\u00002\u00020\u0001:\u0002\u001f\u0018B'\u0012\u0006\u0010#\u001a\u00020\"\u0012\u0006\u0010(\u001a\u00020'\u0012\u0006\u0010-\u001a\u00020,\u0012\u0006\u0010>\u001a\u00020=¢\u0006\u0004\b?\u0010@J\u0010\u0010\u0005\u001a\u00020\u00042\u0006\u0010\u0003\u001a\u00020\u0002H\u0002J\u000e\u0010\b\u001a\u00020\u00042\u0006\u0010\u0007\u001a\u00020\u0006J\u0016\u0010\f\u001a\u00020\u000b2\u0006\u0010\u0007\u001a\u00020\u00062\u0006\u0010\n\u001a\u00020\tJ\u0006\u0010\r\u001a\u00020\u0004J\u0006\u0010\u0003\u001a\u00020\u0004J\u0006\u0010\u000e\u001a\u00020\u0004J\u0010\u0010\u0011\u001a\u0004\u0018\u00010\u00102\u0006\u0010\u000f\u001a\u00020\tJ\u000e\u0010\u0014\u001a\u00020\u00042\u0006\u0010\u0013\u001a\u00020\u0012J\u000e\u0010\u0016\u001a\u00020\u00152\u0006\u0010\u0013\u001a\u00020\u0012J\u0006\u0010\u0017\u001a\u00020\u0004J\u0006\u0010\u0018\u001a\u00020\u0004J\u0006\u0010\u0019\u001a\u00020\u0004J9\u0010\u001f\u001a\u00028\u0000\"\n\b\u0000\u0010\u001a*\u0004\u0018\u00010\u00022\u0006\u0010\u001c\u001a\u00020\u001b2\u0006\u0010\u001d\u001a\u00020\t2\u0006\u0010\u001e\u001a\u00020\t2\u0006\u0010\u0003\u001a\u00028\u0000¢\u0006\u0004\b\u001f\u0010 J\u0006\u0010!\u001a\u00020\u0004R\u001a\u0010#\u001a\u00020\"8\u0000X\u0080\u0004¢\u0006\f\n\u0004\b#\u0010$\u001a\u0004\b%\u0010&R\u001a\u0010(\u001a\u00020'8\u0000X\u0080\u0004¢\u0006\f\n\u0004\b(\u0010)\u001a\u0004\b*\u0010+R\u001a\u0010-\u001a\u00020,8\u0000X\u0080\u0004¢\u0006\f\n\u0004\b-\u0010.\u001a\u0004\b/\u00100R$\u00102\u001a\u00020\t2\u0006\u00101\u001a\u00020\t8\u0000@BX\u0080\u000e¢\u0006\f\n\u0004\b2\u00103\u001a\u0004\b4\u00105R\u001a\u00107\u001a\u0002068\u0000X\u0080\u0004¢\u0006\f\n\u0004\b7\u00108\u001a\u0004\b9\u0010:R\u0014\u0010<\u001a\u00020\t8@X\u0080\u0004¢\u0006\u0006\u001a\u0004\b;\u00105¨\u0006A"}, d2 = {"Lee/c;", "", "Ljava/io/IOException;", "e", "Lma/f0;", "s", "Lzd/z;", "request", "t", "", "duplex", "Lme/y;", "c", "f", "r", "expectContinue", "Lzd/b0$a;", "p", "Lzd/b0;", "response", "q", "Lzd/c0;", "o", "m", "b", "d", "E", "", "bytesRead", "responseDone", "requestDone", "a", "(JZZLjava/io/IOException;)Ljava/io/IOException;", "n", "Lee/e;", "call", "Lee/e;", "g", "()Lee/e;", "Lzd/r;", "eventListener", "Lzd/r;", "i", "()Lzd/r;", "Lee/d;", "finder", "Lee/d;", "j", "()Lee/d;", "<set-?>", "isDuplex", "Z", "l", "()Z", "Lee/f;", "connection", "Lee/f;", "h", "()Lee/f;", "k", "isCoalescedConnection", "Lfe/d;", "codec", "<init>", "(Lee/e;Lzd/r;Lee/d;Lfe/d;)V", "okhttp"}, k = 1, mv = {1, 6, 0})
/* renamed from: ee.c, reason: use source file name */
/* loaded from: classes2.dex */
public final class Exchange {

    /* renamed from: a, reason: collision with root package name */
    private final e f11159a;

    /* renamed from: b, reason: collision with root package name */
    private final EventListener f11160b;

    /* renamed from: c, reason: collision with root package name */
    private final ExchangeFinder f11161c;

    /* renamed from: d, reason: collision with root package name */
    private final ExchangeCodec f11162d;

    /* renamed from: e, reason: collision with root package name */
    private boolean f11163e;

    /* renamed from: f, reason: collision with root package name */
    private final RealConnection f11164f;

    /* compiled from: Exchange.kt */
    @Metadata(bv = {}, d1 = {"\u0000,\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\t\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0005\b\u0082\u0004\u0018\u00002\u00020\u0001B\u0017\u0012\u0006\u0010\u0010\u001a\u00020\u000f\u0012\u0006\u0010\u0011\u001a\u00020\t¢\u0006\u0004\b\u0012\u0010\u0013J#\u0010\u0005\u001a\u00028\u0000\"\n\b\u0000\u0010\u0003*\u0004\u0018\u00010\u00022\u0006\u0010\u0004\u001a\u00028\u0000H\u0002¢\u0006\u0004\b\u0005\u0010\u0006J\u0018\u0010\f\u001a\u00020\u000b2\u0006\u0010\b\u001a\u00020\u00072\u0006\u0010\n\u001a\u00020\tH\u0016J\b\u0010\r\u001a\u00020\u000bH\u0016J\b\u0010\u000e\u001a\u00020\u000bH\u0016¨\u0006\u0014"}, d2 = {"Lee/c$a;", "Lme/h;", "Ljava/io/IOException;", "E", "e", "b", "(Ljava/io/IOException;)Ljava/io/IOException;", "Lme/d;", "source", "", "byteCount", "Lma/f0;", "write", "flush", "close", "Lme/y;", "delegate", "contentLength", "<init>", "(Lee/c;Lme/y;J)V", "okhttp"}, k = 1, mv = {1, 6, 0})
    /* renamed from: ee.c$a */
    /* loaded from: classes2.dex */
    private final class a extends ForwardingSink {

        /* renamed from: e, reason: collision with root package name */
        private final long f11165e;

        /* renamed from: f, reason: collision with root package name */
        private boolean f11166f;

        /* renamed from: g, reason: collision with root package name */
        private long f11167g;

        /* renamed from: h, reason: collision with root package name */
        private boolean f11168h;

        /* renamed from: i, reason: collision with root package name */
        final /* synthetic */ Exchange f11169i;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        public a(Exchange exchange, Sink sink, long j10) {
            super(sink);
            k.e(exchange, "this$0");
            k.e(sink, "delegate");
            this.f11169i = exchange;
            this.f11165e = j10;
        }

        private final <E extends IOException> E b(E e10) {
            if (this.f11166f) {
                return e10;
            }
            this.f11166f = true;
            return (E) this.f11169i.a(this.f11167g, false, true, e10);
        }

        @Override // me.ForwardingSink, me.Sink, java.io.Closeable, java.lang.AutoCloseable
        public void close() {
            if (this.f11168h) {
                return;
            }
            this.f11168h = true;
            long j10 = this.f11165e;
            if (j10 != -1 && this.f11167g != j10) {
                throw new ProtocolException("unexpected end of stream");
            }
            try {
                super.close();
                b(null);
            } catch (IOException e10) {
                throw b(e10);
            }
        }

        @Override // me.ForwardingSink, me.Sink, java.io.Flushable
        public void flush() {
            try {
                super.flush();
            } catch (IOException e10) {
                throw b(e10);
            }
        }

        @Override // me.ForwardingSink, me.Sink
        public void write(me.d dVar, long j10) {
            k.e(dVar, "source");
            if (!this.f11168h) {
                long j11 = this.f11165e;
                if (j11 != -1 && this.f11167g + j10 > j11) {
                    throw new ProtocolException("expected " + this.f11165e + " bytes but received " + (this.f11167g + j10));
                }
                try {
                    super.write(dVar, j10);
                    this.f11167g += j10;
                    return;
                } catch (IOException e10) {
                    throw b(e10);
                }
            }
            throw new IllegalStateException("closed".toString());
        }
    }

    /* compiled from: Exchange.kt */
    @Metadata(bv = {}, d1 = {"\u0000,\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\t\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0005\b\u0080\u0004\u0018\u00002\u00020\u0001B\u0017\u0012\u0006\u0010\u000f\u001a\u00020\u000e\u0012\u0006\u0010\u0010\u001a\u00020\u0004¢\u0006\u0004\b\u0011\u0010\u0012J\u0018\u0010\u0006\u001a\u00020\u00042\u0006\u0010\u0003\u001a\u00020\u00022\u0006\u0010\u0005\u001a\u00020\u0004H\u0016J\b\u0010\b\u001a\u00020\u0007H\u0016J!\u0010\f\u001a\u00028\u0000\"\n\b\u0000\u0010\n*\u0004\u0018\u00010\t2\u0006\u0010\u000b\u001a\u00028\u0000¢\u0006\u0004\b\f\u0010\r¨\u0006\u0013"}, d2 = {"Lee/c$b;", "Lme/i;", "Lme/d;", "sink", "", "byteCount", "read", "Lma/f0;", "close", "Ljava/io/IOException;", "E", "e", "c", "(Ljava/io/IOException;)Ljava/io/IOException;", "Lme/a0;", "delegate", "contentLength", "<init>", "(Lee/c;Lme/a0;J)V", "okhttp"}, k = 1, mv = {1, 6, 0})
    /* renamed from: ee.c$b */
    /* loaded from: classes2.dex */
    public final class b extends ForwardingSource {

        /* renamed from: f, reason: collision with root package name */
        private final long f11170f;

        /* renamed from: g, reason: collision with root package name */
        private long f11171g;

        /* renamed from: h, reason: collision with root package name */
        private boolean f11172h;

        /* renamed from: i, reason: collision with root package name */
        private boolean f11173i;

        /* renamed from: j, reason: collision with root package name */
        private boolean f11174j;

        /* renamed from: k, reason: collision with root package name */
        final /* synthetic */ Exchange f11175k;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        public b(Exchange exchange, Source source, long j10) {
            super(source);
            k.e(exchange, "this$0");
            k.e(source, "delegate");
            this.f11175k = exchange;
            this.f11170f = j10;
            this.f11172h = true;
            if (j10 == 0) {
                c(null);
            }
        }

        public final <E extends IOException> E c(E e10) {
            if (this.f11173i) {
                return e10;
            }
            this.f11173i = true;
            if (e10 == null && this.f11172h) {
                this.f11172h = false;
                this.f11175k.getF11160b().v(this.f11175k.getF11159a());
            }
            return (E) this.f11175k.a(this.f11171g, true, false, e10);
        }

        @Override // me.ForwardingSource, me.Source, java.io.Closeable, java.lang.AutoCloseable
        public void close() {
            if (this.f11174j) {
                return;
            }
            this.f11174j = true;
            try {
                super.close();
                c(null);
            } catch (IOException e10) {
                throw c(e10);
            }
        }

        @Override // me.Source
        public long read(me.d sink, long byteCount) {
            k.e(sink, "sink");
            if (!this.f11174j) {
                try {
                    long read = b().read(sink, byteCount);
                    if (this.f11172h) {
                        this.f11172h = false;
                        this.f11175k.getF11160b().v(this.f11175k.getF11159a());
                    }
                    if (read == -1) {
                        c(null);
                        return -1L;
                    }
                    long j10 = this.f11171g + read;
                    long j11 = this.f11170f;
                    if (j11 != -1 && j10 > j11) {
                        throw new ProtocolException("expected " + this.f11170f + " bytes but received " + j10);
                    }
                    this.f11171g = j10;
                    if (j10 == j11) {
                        c(null);
                    }
                    return read;
                } catch (IOException e10) {
                    throw c(e10);
                }
            }
            throw new IllegalStateException("closed".toString());
        }
    }

    public Exchange(e eVar, EventListener eventListener, ExchangeFinder exchangeFinder, ExchangeCodec exchangeCodec) {
        k.e(eVar, "call");
        k.e(eventListener, "eventListener");
        k.e(exchangeFinder, "finder");
        k.e(exchangeCodec, "codec");
        this.f11159a = eVar;
        this.f11160b = eventListener;
        this.f11161c = exchangeFinder;
        this.f11162d = exchangeCodec;
        this.f11164f = exchangeCodec.getF12402a();
    }

    private final void s(IOException iOException) {
        this.f11161c.h(iOException);
        this.f11162d.getF12402a().G(this.f11159a, iOException);
    }

    public final <E extends IOException> E a(long bytesRead, boolean responseDone, boolean requestDone, E e10) {
        if (e10 != null) {
            s(e10);
        }
        if (requestDone) {
            if (e10 != null) {
                this.f11160b.r(this.f11159a, e10);
            } else {
                this.f11160b.p(this.f11159a, bytesRead);
            }
        }
        if (responseDone) {
            if (e10 != null) {
                this.f11160b.w(this.f11159a, e10);
            } else {
                this.f11160b.u(this.f11159a, bytesRead);
            }
        }
        return (E) this.f11159a.s(this, requestDone, responseDone, e10);
    }

    public final void b() {
        this.f11162d.cancel();
    }

    public final Sink c(z request, boolean duplex) {
        k.e(request, "request");
        this.f11163e = duplex;
        RequestBody f20799d = request.getF20799d();
        k.b(f20799d);
        long a10 = f20799d.a();
        this.f11160b.q(this.f11159a);
        return new a(this, this.f11162d.d(request, a10), a10);
    }

    public final void d() {
        this.f11162d.cancel();
        this.f11159a.s(this, true, true, null);
    }

    public final void e() {
        try {
            this.f11162d.a();
        } catch (IOException e10) {
            this.f11160b.r(this.f11159a, e10);
            s(e10);
            throw e10;
        }
    }

    public final void f() {
        try {
            this.f11162d.h();
        } catch (IOException e10) {
            this.f11160b.r(this.f11159a, e10);
            s(e10);
            throw e10;
        }
    }

    /* renamed from: g, reason: from getter */
    public final e getF11159a() {
        return this.f11159a;
    }

    /* renamed from: h, reason: from getter */
    public final RealConnection getF11164f() {
        return this.f11164f;
    }

    /* renamed from: i, reason: from getter */
    public final EventListener getF11160b() {
        return this.f11160b;
    }

    /* renamed from: j, reason: from getter */
    public final ExchangeFinder getF11161c() {
        return this.f11161c;
    }

    public final boolean k() {
        return !k.a(this.f11161c.getF11177b().getF20493i().getF20716d(), this.f11164f.getF11211d().getF20560a().getF20493i().getF20716d());
    }

    /* renamed from: l, reason: from getter */
    public final boolean getF11163e() {
        return this.f11163e;
    }

    public final void m() {
        this.f11162d.getF12402a().y();
    }

    public final void n() {
        this.f11159a.s(this, true, false, null);
    }

    public final ResponseBody o(b0 response) {
        k.e(response, "response");
        try {
            String S = b0.S(response, "Content-Type", null, 2, null);
            long g6 = this.f11162d.g(response);
            return new RealResponseBody(S, g6, n.b(new b(this, this.f11162d.b(response), g6)));
        } catch (IOException e10) {
            this.f11160b.w(this.f11159a, e10);
            s(e10);
            throw e10;
        }
    }

    public final b0.a p(boolean expectContinue) {
        try {
            b0.a e10 = this.f11162d.e(expectContinue);
            if (e10 != null) {
                e10.m(this);
            }
            return e10;
        } catch (IOException e11) {
            this.f11160b.w(this.f11159a, e11);
            s(e11);
            throw e11;
        }
    }

    public final void q(b0 b0Var) {
        k.e(b0Var, "response");
        this.f11160b.x(this.f11159a, b0Var);
    }

    public final void r() {
        this.f11160b.y(this.f11159a);
    }

    public final void t(z zVar) {
        k.e(zVar, "request");
        try {
            this.f11160b.t(this.f11159a);
            this.f11162d.c(zVar);
            this.f11160b.s(this.f11159a, zVar);
        } catch (IOException e10) {
            this.f11160b.r(this.f11159a, e10);
            s(e10);
            throw e10;
        }
    }
}
