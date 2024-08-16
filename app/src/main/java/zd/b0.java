package zd;

import com.oplus.thermalcontrol.config.ThermalBaseConfig;
import ee.Exchange;
import fe.HttpHeaders;
import java.io.Closeable;
import java.util.List;
import kotlin.Metadata;
import zd.Headers;

/* compiled from: Response.kt */
@Metadata(bv = {}, d1 = {"\u0000p\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\b\n\u0002\u0010\b\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\f\n\u0002\u0010\t\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0007\u0018\u00002\u00020\u0001:\u0001JB}\b\u0000\u0012\u0006\u0010\u000f\u001a\u00020\u000e\u0012\u0006\u0010\u0014\u001a\u00020\u0013\u0012\u0006\u0010\u0018\u001a\u00020\u0002\u0012\u0006\u0010\u001d\u001a\u00020\u001c\u0012\b\u0010\"\u001a\u0004\u0018\u00010!\u0012\u0006\u0010'\u001a\u00020&\u0012\b\u0010,\u001a\u0004\u0018\u00010+\u0012\b\u00100\u001a\u0004\u0018\u00010\u0000\u0012\b\u00104\u001a\u0004\u0018\u00010\u0000\u0012\b\u00106\u001a\u0004\u0018\u00010\u0000\u0012\u0006\u00109\u001a\u000208\u0012\u0006\u0010=\u001a\u000208\u0012\b\u0010@\u001a\u0004\u0018\u00010?¢\u0006\u0004\bH\u0010IJ\u001e\u0010\u0005\u001a\u0004\u0018\u00010\u00022\u0006\u0010\u0003\u001a\u00020\u00022\n\b\u0002\u0010\u0004\u001a\u0004\u0018\u00010\u0002H\u0007J\u0006\u0010\u0007\u001a\u00020\u0006J\f\u0010\n\u001a\b\u0012\u0004\u0012\u00020\t0\bJ\b\u0010\f\u001a\u00020\u000bH\u0016J\b\u0010\r\u001a\u00020\u0002H\u0016R\u0017\u0010\u000f\u001a\u00020\u000e8\u0007¢\u0006\f\n\u0004\b\u000f\u0010\u0010\u001a\u0004\b\u0011\u0010\u0012R\u0017\u0010\u0014\u001a\u00020\u00138\u0007¢\u0006\f\n\u0004\b\u0014\u0010\u0015\u001a\u0004\b\u0016\u0010\u0017R\u0017\u0010\u0018\u001a\u00020\u00028\u0007¢\u0006\f\n\u0004\b\u0018\u0010\u0019\u001a\u0004\b\u001a\u0010\u001bR\u0017\u0010\u001d\u001a\u00020\u001c8\u0007¢\u0006\f\n\u0004\b\u001d\u0010\u001e\u001a\u0004\b\u001f\u0010 R\u0019\u0010\"\u001a\u0004\u0018\u00010!8\u0007¢\u0006\f\n\u0004\b\"\u0010#\u001a\u0004\b$\u0010%R\u0017\u0010'\u001a\u00020&8\u0007¢\u0006\f\n\u0004\b'\u0010(\u001a\u0004\b)\u0010*R\u0019\u0010,\u001a\u0004\u0018\u00010+8\u0007¢\u0006\f\n\u0004\b,\u0010-\u001a\u0004\b.\u0010/R\u0019\u00100\u001a\u0004\u0018\u00010\u00008\u0007¢\u0006\f\n\u0004\b0\u00101\u001a\u0004\b2\u00103R\u0019\u00104\u001a\u0004\u0018\u00010\u00008\u0007¢\u0006\f\n\u0004\b4\u00101\u001a\u0004\b5\u00103R\u0019\u00106\u001a\u0004\u0018\u00010\u00008\u0007¢\u0006\f\n\u0004\b6\u00101\u001a\u0004\b7\u00103R\u0017\u00109\u001a\u0002088\u0007¢\u0006\f\n\u0004\b9\u0010:\u001a\u0004\b;\u0010<R\u0017\u0010=\u001a\u0002088\u0007¢\u0006\f\n\u0004\b=\u0010:\u001a\u0004\b>\u0010<R\u001c\u0010@\u001a\u0004\u0018\u00010?8\u0001X\u0080\u0004¢\u0006\f\n\u0004\b@\u0010A\u001a\u0004\bB\u0010CR\u0011\u0010G\u001a\u00020D8G¢\u0006\u0006\u001a\u0004\bE\u0010F¨\u0006K"}, d2 = {"Lzd/b0;", "Ljava/io/Closeable;", "", "name", "defaultValue", "N", "Lzd/b0$a;", "e0", "", "Lzd/h;", "u", "Lma/f0;", "close", "toString", "Lzd/z;", "request", "Lzd/z;", "m0", "()Lzd/z;", "Lzd/y;", "protocol", "Lzd/y;", "i0", "()Lzd/y;", "message", "Ljava/lang/String;", "X", "()Ljava/lang/String;", "", "code", "I", "v", "()I", "Lzd/s;", "handshake", "Lzd/s;", "L", "()Lzd/s;", "Lzd/t;", "headers", "Lzd/t;", "U", "()Lzd/t;", "Lzd/c0;", "body", "Lzd/c0;", "b", "()Lzd/c0;", "networkResponse", "Lzd/b0;", "a0", "()Lzd/b0;", "cacheResponse", "m", "priorResponse", "h0", "", "sentRequestAtMillis", "J", "o0", "()J", "receivedResponseAtMillis", "j0", "Lee/c;", "exchange", "Lee/c;", "w", "()Lee/c;", "Lzd/d;", "c", "()Lzd/d;", "cacheControl", "<init>", "(Lzd/z;Lzd/y;Ljava/lang/String;ILzd/s;Lzd/t;Lzd/c0;Lzd/b0;Lzd/b0;Lzd/b0;JJLee/c;)V", "a", "okhttp"}, k = 1, mv = {1, 6, 0})
/* loaded from: classes2.dex */
public final class b0 implements Closeable {

    /* renamed from: e, reason: collision with root package name */
    private final z f20505e;

    /* renamed from: f, reason: collision with root package name */
    private final Protocol f20506f;

    /* renamed from: g, reason: collision with root package name and from toString */
    private final String message;

    /* renamed from: h, reason: collision with root package name and from toString */
    private final int code;

    /* renamed from: i, reason: collision with root package name */
    private final Handshake f20509i;

    /* renamed from: j, reason: collision with root package name */
    private final Headers f20510j;

    /* renamed from: k, reason: collision with root package name */
    private final ResponseBody f20511k;

    /* renamed from: l, reason: collision with root package name */
    private final b0 f20512l;

    /* renamed from: m, reason: collision with root package name */
    private final b0 f20513m;

    /* renamed from: n, reason: collision with root package name */
    private final b0 f20514n;

    /* renamed from: o, reason: collision with root package name */
    private final long f20515o;

    /* renamed from: p, reason: collision with root package name */
    private final long f20516p;

    /* renamed from: q, reason: collision with root package name */
    private final Exchange f20517q;

    /* renamed from: r, reason: collision with root package name */
    private CacheControl f20518r;

    public b0(z zVar, Protocol protocol, String str, int i10, Handshake handshake, Headers headers, ResponseBody responseBody, b0 b0Var, b0 b0Var2, b0 b0Var3, long j10, long j11, Exchange exchange) {
        za.k.e(zVar, "request");
        za.k.e(protocol, "protocol");
        za.k.e(str, "message");
        za.k.e(headers, "headers");
        this.f20505e = zVar;
        this.f20506f = protocol;
        this.message = str;
        this.code = i10;
        this.f20509i = handshake;
        this.f20510j = headers;
        this.f20511k = responseBody;
        this.f20512l = b0Var;
        this.f20513m = b0Var2;
        this.f20514n = b0Var3;
        this.f20515o = j10;
        this.f20516p = j11;
        this.f20517q = exchange;
    }

    public static /* synthetic */ String S(b0 b0Var, String str, String str2, int i10, Object obj) {
        if ((i10 & 2) != 0) {
            str2 = null;
        }
        return b0Var.N(str, str2);
    }

    /* renamed from: L, reason: from getter */
    public final Handshake getF20509i() {
        return this.f20509i;
    }

    public final String N(String name, String defaultValue) {
        za.k.e(name, "name");
        String d10 = this.f20510j.d(name);
        return d10 == null ? defaultValue : d10;
    }

    /* renamed from: U, reason: from getter */
    public final Headers getF20510j() {
        return this.f20510j;
    }

    /* renamed from: X, reason: from getter */
    public final String getMessage() {
        return this.message;
    }

    /* renamed from: a0, reason: from getter */
    public final b0 getF20512l() {
        return this.f20512l;
    }

    /* renamed from: b, reason: from getter */
    public final ResponseBody getF20511k() {
        return this.f20511k;
    }

    public final CacheControl c() {
        CacheControl cacheControl = this.f20518r;
        if (cacheControl != null) {
            return cacheControl;
        }
        CacheControl b10 = CacheControl.f20536n.b(this.f20510j);
        this.f20518r = b10;
        return b10;
    }

    @Override // java.io.Closeable, java.lang.AutoCloseable
    public void close() {
        ResponseBody responseBody = this.f20511k;
        if (responseBody == null) {
            throw new IllegalStateException("response is not eligible for a body and must not be closed".toString());
        }
        responseBody.close();
    }

    public final a e0() {
        return new a(this);
    }

    /* renamed from: h0, reason: from getter */
    public final b0 getF20514n() {
        return this.f20514n;
    }

    /* renamed from: i0, reason: from getter */
    public final Protocol getF20506f() {
        return this.f20506f;
    }

    /* renamed from: j0, reason: from getter */
    public final long getF20516p() {
        return this.f20516p;
    }

    /* renamed from: m, reason: from getter */
    public final b0 getF20513m() {
        return this.f20513m;
    }

    /* renamed from: m0, reason: from getter */
    public final z getF20505e() {
        return this.f20505e;
    }

    /* renamed from: o0, reason: from getter */
    public final long getF20515o() {
        return this.f20515o;
    }

    public String toString() {
        return "Response{protocol=" + this.f20506f + ", code=" + this.code + ", message=" + this.message + ", url=" + this.f20505e.getF20796a() + '}';
    }

    public final List<Challenge> u() {
        String str;
        List<Challenge> j10;
        Headers headers = this.f20510j;
        int i10 = this.code;
        if (i10 == 401) {
            str = "WWW-Authenticate";
        } else {
            if (i10 != 407) {
                j10 = kotlin.collections.r.j();
                return j10;
            }
            str = "Proxy-Authenticate";
        }
        return HttpHeaders.a(headers, str);
    }

    /* renamed from: v, reason: from getter */
    public final int getCode() {
        return this.code;
    }

    /* renamed from: w, reason: from getter */
    public final Exchange getF20517q() {
        return this.f20517q;
    }

    /* compiled from: Response.kt */
    @Metadata(bv = {}, d1 = {"\u0000d\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\b\n\u0002\u0010\t\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u001d\n\u0002\u0018\u0002\n\u0002\b\u001d\b\u0016\u0018\u00002\u00020\u0001B\t\b\u0016¢\u0006\u0004\bd\u0010eB\u0011\b\u0010\u0012\u0006\u0010\u0005\u001a\u00020\u0004¢\u0006\u0004\bd\u0010XJ\u001a\u0010\u0007\u001a\u00020\u00062\u0006\u0010\u0003\u001a\u00020\u00022\b\u0010\u0005\u001a\u0004\u0018\u00010\u0004H\u0002J\u0012\u0010\b\u001a\u00020\u00062\b\u0010\u0005\u001a\u0004\u0018\u00010\u0004H\u0002J\u0010\u0010\u000b\u001a\u00020\u00002\u0006\u0010\n\u001a\u00020\tH\u0016J\u0010\u0010\u000e\u001a\u00020\u00002\u0006\u0010\r\u001a\u00020\fH\u0016J\u0010\u0010\u0011\u001a\u00020\u00002\u0006\u0010\u0010\u001a\u00020\u000fH\u0016J\u0010\u0010\u0013\u001a\u00020\u00002\u0006\u0010\u0012\u001a\u00020\u0002H\u0016J\u0012\u0010\u0016\u001a\u00020\u00002\b\u0010\u0015\u001a\u0004\u0018\u00010\u0014H\u0016J\u0018\u0010\u0018\u001a\u00020\u00002\u0006\u0010\u0003\u001a\u00020\u00022\u0006\u0010\u0017\u001a\u00020\u0002H\u0016J\u0018\u0010\u0019\u001a\u00020\u00002\u0006\u0010\u0003\u001a\u00020\u00022\u0006\u0010\u0017\u001a\u00020\u0002H\u0016J\u0010\u0010\u001c\u001a\u00020\u00002\u0006\u0010\u001b\u001a\u00020\u001aH\u0016J\u0012\u0010\u001f\u001a\u00020\u00002\b\u0010\u001e\u001a\u0004\u0018\u00010\u001dH\u0016J\u0012\u0010!\u001a\u00020\u00002\b\u0010 \u001a\u0004\u0018\u00010\u0004H\u0016J\u0012\u0010#\u001a\u00020\u00002\b\u0010\"\u001a\u0004\u0018\u00010\u0004H\u0016J\u0012\u0010%\u001a\u00020\u00002\b\u0010$\u001a\u0004\u0018\u00010\u0004H\u0016J\u0010\u0010(\u001a\u00020\u00002\u0006\u0010'\u001a\u00020&H\u0016J\u0010\u0010*\u001a\u00020\u00002\u0006\u0010)\u001a\u00020&H\u0016J\u0017\u0010-\u001a\u00020\u00062\u0006\u0010,\u001a\u00020+H\u0000¢\u0006\u0004\b-\u0010.J\b\u0010/\u001a\u00020\u0004H\u0016R$\u0010\n\u001a\u0004\u0018\u00010\t8\u0000@\u0000X\u0080\u000e¢\u0006\u0012\n\u0004\b\n\u00100\u001a\u0004\b1\u00102\"\u0004\b3\u00104R$\u0010\r\u001a\u0004\u0018\u00010\f8\u0000@\u0000X\u0080\u000e¢\u0006\u0012\n\u0004\b\r\u00105\u001a\u0004\b6\u00107\"\u0004\b8\u00109R\"\u0010\u0010\u001a\u00020\u000f8\u0000@\u0000X\u0080\u000e¢\u0006\u0012\n\u0004\b\u0010\u0010:\u001a\u0004\b;\u0010<\"\u0004\b=\u0010>R$\u0010\u0012\u001a\u0004\u0018\u00010\u00028\u0000@\u0000X\u0080\u000e¢\u0006\u0012\n\u0004\b\u0012\u0010?\u001a\u0004\b@\u0010A\"\u0004\bB\u0010CR$\u0010\u0015\u001a\u0004\u0018\u00010\u00148\u0000@\u0000X\u0080\u000e¢\u0006\u0012\n\u0004\b\u0015\u0010D\u001a\u0004\bE\u0010F\"\u0004\bG\u0010HR\"\u0010\u001b\u001a\u00020I8\u0000@\u0000X\u0080\u000e¢\u0006\u0012\n\u0004\b\u001b\u0010J\u001a\u0004\bK\u0010L\"\u0004\bM\u0010NR$\u0010\u001e\u001a\u0004\u0018\u00010\u001d8\u0000@\u0000X\u0080\u000e¢\u0006\u0012\n\u0004\b\u001e\u0010O\u001a\u0004\bP\u0010Q\"\u0004\bR\u0010SR$\u0010 \u001a\u0004\u0018\u00010\u00048\u0000@\u0000X\u0080\u000e¢\u0006\u0012\n\u0004\b \u0010T\u001a\u0004\bU\u0010V\"\u0004\bW\u0010XR$\u0010\"\u001a\u0004\u0018\u00010\u00048\u0000@\u0000X\u0080\u000e¢\u0006\u0012\n\u0004\b\"\u0010T\u001a\u0004\bY\u0010V\"\u0004\bZ\u0010XR$\u0010$\u001a\u0004\u0018\u00010\u00048\u0000@\u0000X\u0080\u000e¢\u0006\u0012\n\u0004\b$\u0010T\u001a\u0004\b[\u0010V\"\u0004\b\\\u0010XR\"\u0010'\u001a\u00020&8\u0000@\u0000X\u0080\u000e¢\u0006\u0012\n\u0004\b'\u0010]\u001a\u0004\b^\u0010_\"\u0004\b`\u0010aR\"\u0010)\u001a\u00020&8\u0000@\u0000X\u0080\u000e¢\u0006\u0012\n\u0004\b)\u0010]\u001a\u0004\bb\u0010_\"\u0004\bc\u0010a¨\u0006f"}, d2 = {"Lzd/b0$a;", "", "", "name", "Lzd/b0;", "response", "Lma/f0;", "f", "e", "Lzd/z;", "request", "s", "Lzd/y;", "protocol", "q", "", "code", "g", "message", "n", "Lzd/s;", "handshake", "j", ThermalBaseConfig.Item.ATTR_VALUE, "k", "a", "Lzd/t;", "headers", "l", "Lzd/c0;", "body", "b", "networkResponse", "o", "cacheResponse", "d", "priorResponse", "p", "", "sentRequestAtMillis", "t", "receivedResponseAtMillis", "r", "Lee/c;", "deferredTrailers", "m", "(Lee/c;)V", "c", "Lzd/z;", "getRequest$okhttp", "()Lzd/z;", "E", "(Lzd/z;)V", "Lzd/y;", "getProtocol$okhttp", "()Lzd/y;", "C", "(Lzd/y;)V", "I", "h", "()I", "w", "(I)V", "Ljava/lang/String;", "getMessage$okhttp", "()Ljava/lang/String;", "z", "(Ljava/lang/String;)V", "Lzd/s;", "getHandshake$okhttp", "()Lzd/s;", "x", "(Lzd/s;)V", "Lzd/t$a;", "Lzd/t$a;", "i", "()Lzd/t$a;", "y", "(Lzd/t$a;)V", "Lzd/c0;", "getBody$okhttp", "()Lzd/c0;", "u", "(Lzd/c0;)V", "Lzd/b0;", "getNetworkResponse$okhttp", "()Lzd/b0;", "A", "(Lzd/b0;)V", "getCacheResponse$okhttp", "v", "getPriorResponse$okhttp", "B", "J", "getSentRequestAtMillis$okhttp", "()J", "F", "(J)V", "getReceivedResponseAtMillis$okhttp", "D", "<init>", "()V", "okhttp"}, k = 1, mv = {1, 6, 0})
    /* loaded from: classes2.dex */
    public static class a {

        /* renamed from: a, reason: collision with root package name */
        private z f20519a;

        /* renamed from: b, reason: collision with root package name */
        private Protocol f20520b;

        /* renamed from: c, reason: collision with root package name */
        private int f20521c;

        /* renamed from: d, reason: collision with root package name */
        private String f20522d;

        /* renamed from: e, reason: collision with root package name */
        private Handshake f20523e;

        /* renamed from: f, reason: collision with root package name */
        private Headers.a f20524f;

        /* renamed from: g, reason: collision with root package name */
        private ResponseBody f20525g;

        /* renamed from: h, reason: collision with root package name */
        private b0 f20526h;

        /* renamed from: i, reason: collision with root package name */
        private b0 f20527i;

        /* renamed from: j, reason: collision with root package name */
        private b0 f20528j;

        /* renamed from: k, reason: collision with root package name */
        private long f20529k;

        /* renamed from: l, reason: collision with root package name */
        private long f20530l;

        /* renamed from: m, reason: collision with root package name */
        private Exchange f20531m;

        public a() {
            this.f20521c = -1;
            this.f20524f = new Headers.a();
        }

        private final void e(b0 b0Var) {
            if (b0Var == null) {
                return;
            }
            if (!(b0Var.getF20511k() == null)) {
                throw new IllegalArgumentException("priorResponse.body != null".toString());
            }
        }

        private final void f(String str, b0 b0Var) {
            if (b0Var == null) {
                return;
            }
            if (b0Var.getF20511k() == null) {
                if (b0Var.getF20512l() == null) {
                    if (b0Var.getF20513m() == null) {
                        if (!(b0Var.getF20514n() == null)) {
                            throw new IllegalArgumentException(za.k.l(str, ".priorResponse != null").toString());
                        }
                        return;
                    }
                    throw new IllegalArgumentException(za.k.l(str, ".cacheResponse != null").toString());
                }
                throw new IllegalArgumentException(za.k.l(str, ".networkResponse != null").toString());
            }
            throw new IllegalArgumentException(za.k.l(str, ".body != null").toString());
        }

        public final void A(b0 b0Var) {
            this.f20526h = b0Var;
        }

        public final void B(b0 b0Var) {
            this.f20528j = b0Var;
        }

        public final void C(Protocol protocol) {
            this.f20520b = protocol;
        }

        public final void D(long j10) {
            this.f20530l = j10;
        }

        public final void E(z zVar) {
            this.f20519a = zVar;
        }

        public final void F(long j10) {
            this.f20529k = j10;
        }

        public a a(String name, String value) {
            za.k.e(name, "name");
            za.k.e(value, ThermalBaseConfig.Item.ATTR_VALUE);
            getF20524f().a(name, value);
            return this;
        }

        public a b(ResponseBody body) {
            u(body);
            return this;
        }

        public b0 c() {
            int i10 = this.f20521c;
            if (i10 >= 0) {
                z zVar = this.f20519a;
                if (zVar != null) {
                    Protocol protocol = this.f20520b;
                    if (protocol != null) {
                        String str = this.f20522d;
                        if (str != null) {
                            return new b0(zVar, protocol, str, i10, this.f20523e, this.f20524f.d(), this.f20525g, this.f20526h, this.f20527i, this.f20528j, this.f20529k, this.f20530l, this.f20531m);
                        }
                        throw new IllegalStateException("message == null".toString());
                    }
                    throw new IllegalStateException("protocol == null".toString());
                }
                throw new IllegalStateException("request == null".toString());
            }
            throw new IllegalStateException(za.k.l("code < 0: ", Integer.valueOf(getF20521c())).toString());
        }

        public a d(b0 cacheResponse) {
            f("cacheResponse", cacheResponse);
            v(cacheResponse);
            return this;
        }

        public a g(int code) {
            w(code);
            return this;
        }

        /* renamed from: h, reason: from getter */
        public final int getF20521c() {
            return this.f20521c;
        }

        /* renamed from: i, reason: from getter */
        public final Headers.a getF20524f() {
            return this.f20524f;
        }

        public a j(Handshake handshake) {
            x(handshake);
            return this;
        }

        public a k(String name, String value) {
            za.k.e(name, "name");
            za.k.e(value, ThermalBaseConfig.Item.ATTR_VALUE);
            getF20524f().g(name, value);
            return this;
        }

        public a l(Headers headers) {
            za.k.e(headers, "headers");
            y(headers.f());
            return this;
        }

        public final void m(Exchange deferredTrailers) {
            za.k.e(deferredTrailers, "deferredTrailers");
            this.f20531m = deferredTrailers;
        }

        public a n(String message) {
            za.k.e(message, "message");
            z(message);
            return this;
        }

        public a o(b0 networkResponse) {
            f("networkResponse", networkResponse);
            A(networkResponse);
            return this;
        }

        public a p(b0 priorResponse) {
            e(priorResponse);
            B(priorResponse);
            return this;
        }

        public a q(Protocol protocol) {
            za.k.e(protocol, "protocol");
            C(protocol);
            return this;
        }

        public a r(long receivedResponseAtMillis) {
            D(receivedResponseAtMillis);
            return this;
        }

        public a s(z request) {
            za.k.e(request, "request");
            E(request);
            return this;
        }

        public a t(long sentRequestAtMillis) {
            F(sentRequestAtMillis);
            return this;
        }

        public final void u(ResponseBody responseBody) {
            this.f20525g = responseBody;
        }

        public final void v(b0 b0Var) {
            this.f20527i = b0Var;
        }

        public final void w(int i10) {
            this.f20521c = i10;
        }

        public final void x(Handshake handshake) {
            this.f20523e = handshake;
        }

        public final void y(Headers.a aVar) {
            za.k.e(aVar, "<set-?>");
            this.f20524f = aVar;
        }

        public final void z(String str) {
            this.f20522d = str;
        }

        public a(b0 b0Var) {
            za.k.e(b0Var, "response");
            this.f20521c = -1;
            this.f20519a = b0Var.getF20505e();
            this.f20520b = b0Var.getF20506f();
            this.f20521c = b0Var.getCode();
            this.f20522d = b0Var.getMessage();
            this.f20523e = b0Var.getF20509i();
            this.f20524f = b0Var.getF20510j().f();
            this.f20525g = b0Var.getF20511k();
            this.f20526h = b0Var.getF20512l();
            this.f20527i = b0Var.getF20513m();
            this.f20528j = b0Var.getF20514n();
            this.f20529k = b0Var.getF20515o();
            this.f20530l = b0Var.getF20516p();
            this.f20531m = b0Var.getF20517q();
        }
    }
}
