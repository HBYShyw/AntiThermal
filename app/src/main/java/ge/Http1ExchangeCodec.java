package ge;

import ee.RealConnection;
import fe.ExchangeCodec;
import fe.HttpHeaders;
import fe.RequestLine;
import fe.StatusLine;
import java.io.EOFException;
import java.io.IOException;
import java.net.ProtocolException;
import java.net.Proxy;
import java.util.concurrent.TimeUnit;
import kotlin.Metadata;
import me.BufferedSink;
import me.BufferedSource;
import me.ForwardingTimeout;
import me.Sink;
import me.Source;
import me.Timeout;
import sd.StringsJVM;
import sd.v;
import za.DefaultConstructorMarker;
import za.k;
import zd.CookieJar;
import zd.Headers;
import zd.HttpUrl;
import zd.OkHttpClient;
import zd.b0;
import zd.z;

/* compiled from: Http1ExchangeCodec.kt */
@Metadata(bv = {}, d1 = {"\u0000x\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\t\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\u0018\u00002\u00020\u0001:\u0007\u001c\u001a\u0016\u0014%/\u0019B)\u0012\b\u00102\u001a\u0004\u0018\u000101\u0012\u0006\u0010-\u001a\u00020,\u0012\u0006\u00104\u001a\u000203\u0012\u0006\u00106\u001a\u000205¢\u0006\u0004\b7\u00108J\b\u0010\u0003\u001a\u00020\u0002H\u0002J\b\u0010\u0004\u001a\u00020\u0002H\u0002J\u0010\u0010\b\u001a\u00020\u00072\u0006\u0010\u0006\u001a\u00020\u0005H\u0002J\u0010\u0010\u000b\u001a\u00020\u00072\u0006\u0010\n\u001a\u00020\tH\u0002J\b\u0010\f\u001a\u00020\u0007H\u0002J\u0010\u0010\u0010\u001a\u00020\u000f2\u0006\u0010\u000e\u001a\u00020\rH\u0002J\u0018\u0010\u0014\u001a\u00020\u00022\u0006\u0010\u0012\u001a\u00020\u00112\u0006\u0010\u0013\u001a\u00020\u0005H\u0016J\b\u0010\u0015\u001a\u00020\u000fH\u0016J\u0010\u0010\u0016\u001a\u00020\u000f2\u0006\u0010\u0012\u001a\u00020\u0011H\u0016J\u0010\u0010\u0019\u001a\u00020\u00052\u0006\u0010\u0018\u001a\u00020\u0017H\u0016J\u0010\u0010\u001a\u001a\u00020\u00072\u0006\u0010\u0018\u001a\u00020\u0017H\u0016J\b\u0010\u001b\u001a\u00020\u000fH\u0016J\b\u0010\u001c\u001a\u00020\u000fH\u0016J\u0016\u0010!\u001a\u00020\u000f2\u0006\u0010\u001e\u001a\u00020\u001d2\u0006\u0010 \u001a\u00020\u001fJ\u0012\u0010%\u001a\u0004\u0018\u00010$2\u0006\u0010#\u001a\u00020\"H\u0016J\u000e\u0010&\u001a\u00020\u000f2\u0006\u0010\u0018\u001a\u00020\u0017R\u0018\u0010)\u001a\u00020\"*\u00020\u00178BX\u0082\u0004¢\u0006\u0006\u001a\u0004\b'\u0010(R\u0018\u0010)\u001a\u00020\"*\u00020\u00118BX\u0082\u0004¢\u0006\u0006\u001a\u0004\b*\u0010+R\u001a\u0010-\u001a\u00020,8\u0016X\u0096\u0004¢\u0006\f\n\u0004\b-\u0010.\u001a\u0004\b/\u00100¨\u00069"}, d2 = {"Lge/b;", "Lfe/d;", "Lme/y;", "u", "x", "", "length", "Lme/a0;", "w", "Lzd/u;", "url", "v", "y", "Lme/j;", "timeout", "Lma/f0;", "r", "Lzd/z;", "request", "contentLength", "d", "cancel", "c", "Lzd/b0;", "response", "g", "b", "h", "a", "Lzd/t;", "headers", "", "requestLine", "A", "", "expectContinue", "Lzd/b0$a;", "e", "z", "t", "(Lzd/b0;)Z", "isChunked", "s", "(Lzd/z;)Z", "Lee/f;", "connection", "Lee/f;", "f", "()Lee/f;", "Lzd/x;", "client", "Lme/f;", "source", "Lme/e;", "sink", "<init>", "(Lzd/x;Lee/f;Lme/f;Lme/e;)V", "okhttp"}, k = 1, mv = {1, 6, 0})
/* renamed from: ge.b, reason: use source file name */
/* loaded from: classes2.dex */
public final class Http1ExchangeCodec implements ExchangeCodec {

    /* renamed from: h, reason: collision with root package name */
    public static final d f11923h = new d(null);

    /* renamed from: a, reason: collision with root package name */
    private final OkHttpClient f11924a;

    /* renamed from: b, reason: collision with root package name */
    private final RealConnection f11925b;

    /* renamed from: c, reason: collision with root package name */
    private final BufferedSource f11926c;

    /* renamed from: d, reason: collision with root package name */
    private final BufferedSink f11927d;

    /* renamed from: e, reason: collision with root package name */
    private int f11928e;

    /* renamed from: f, reason: collision with root package name */
    private final HeadersReader f11929f;

    /* renamed from: g, reason: collision with root package name */
    private Headers f11930g;

    /* JADX INFO: Access modifiers changed from: private */
    /* compiled from: Http1ExchangeCodec.kt */
    @Metadata(bv = {}, d1 = {"\u0000*\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\t\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\t\b¢\u0004\u0018\u00002\u00020\u0001B\u0007¢\u0006\u0004\b\u0012\u0010\u0013J\b\u0010\u0003\u001a\u00020\u0002H\u0016J\u0018\u0010\b\u001a\u00020\u00062\u0006\u0010\u0005\u001a\u00020\u00042\u0006\u0010\u0007\u001a\u00020\u0006H\u0016J\u0006\u0010\n\u001a\u00020\tR\"\u0010\f\u001a\u00020\u000b8\u0004@\u0004X\u0084\u000e¢\u0006\u0012\n\u0004\b\f\u0010\r\u001a\u0004\b\u000e\u0010\u000f\"\u0004\b\u0010\u0010\u0011¨\u0006\u0014"}, d2 = {"Lge/b$a;", "Lme/a0;", "Lme/b0;", "timeout", "Lme/d;", "sink", "", "byteCount", "read", "Lma/f0;", "c", "", "closed", "Z", "b", "()Z", "m", "(Z)V", "<init>", "(Lge/b;)V", "okhttp"}, k = 1, mv = {1, 6, 0})
    /* renamed from: ge.b$a */
    /* loaded from: classes2.dex */
    public abstract class a implements Source {

        /* renamed from: e, reason: collision with root package name */
        private final ForwardingTimeout f11931e;

        /* renamed from: f, reason: collision with root package name */
        private boolean f11932f;

        /* renamed from: g, reason: collision with root package name */
        final /* synthetic */ Http1ExchangeCodec f11933g;

        public a(Http1ExchangeCodec http1ExchangeCodec) {
            k.e(http1ExchangeCodec, "this$0");
            this.f11933g = http1ExchangeCodec;
            this.f11931e = new ForwardingTimeout(http1ExchangeCodec.f11926c.timeout());
        }

        /* renamed from: b, reason: from getter */
        protected final boolean getF11932f() {
            return this.f11932f;
        }

        public final void c() {
            if (this.f11933g.f11928e == 6) {
                return;
            }
            if (this.f11933g.f11928e == 5) {
                this.f11933g.r(this.f11931e);
                this.f11933g.f11928e = 6;
                return;
            }
            throw new IllegalStateException(k.l("state: ", Integer.valueOf(this.f11933g.f11928e)));
        }

        protected final void m(boolean z10) {
            this.f11932f = z10;
        }

        @Override // me.Source
        public long read(me.d sink, long byteCount) {
            k.e(sink, "sink");
            try {
                return this.f11933g.f11926c.read(sink, byteCount);
            } catch (IOException e10) {
                this.f11933g.getF12402a().y();
                this.c();
                throw e10;
            }
        }

        @Override // me.Source
        public Timeout timeout() {
            return this.f11931e;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* compiled from: Http1ExchangeCodec.kt */
    @Metadata(bv = {}, d1 = {"\u0000\"\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\t\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0006\b\u0082\u0004\u0018\u00002\u00020\u0001B\u0007¢\u0006\u0004\b\f\u0010\rJ\b\u0010\u0003\u001a\u00020\u0002H\u0016J\u0018\u0010\t\u001a\u00020\b2\u0006\u0010\u0005\u001a\u00020\u00042\u0006\u0010\u0007\u001a\u00020\u0006H\u0016J\b\u0010\n\u001a\u00020\bH\u0016J\b\u0010\u000b\u001a\u00020\bH\u0016¨\u0006\u000e"}, d2 = {"Lge/b$b;", "Lme/y;", "Lme/b0;", "timeout", "Lme/d;", "source", "", "byteCount", "Lma/f0;", "write", "flush", "close", "<init>", "(Lge/b;)V", "okhttp"}, k = 1, mv = {1, 6, 0})
    /* renamed from: ge.b$b */
    /* loaded from: classes2.dex */
    public final class b implements Sink {

        /* renamed from: e, reason: collision with root package name */
        private final ForwardingTimeout f11934e;

        /* renamed from: f, reason: collision with root package name */
        private boolean f11935f;

        /* renamed from: g, reason: collision with root package name */
        final /* synthetic */ Http1ExchangeCodec f11936g;

        public b(Http1ExchangeCodec http1ExchangeCodec) {
            k.e(http1ExchangeCodec, "this$0");
            this.f11936g = http1ExchangeCodec;
            this.f11934e = new ForwardingTimeout(http1ExchangeCodec.f11927d.timeout());
        }

        @Override // me.Sink, java.io.Closeable, java.lang.AutoCloseable
        public synchronized void close() {
            if (this.f11935f) {
                return;
            }
            this.f11935f = true;
            this.f11936g.f11927d.E("0\r\n\r\n");
            this.f11936g.r(this.f11934e);
            this.f11936g.f11928e = 3;
        }

        @Override // me.Sink, java.io.Flushable
        public synchronized void flush() {
            if (this.f11935f) {
                return;
            }
            this.f11936g.f11927d.flush();
        }

        @Override // me.Sink
        public Timeout timeout() {
            return this.f11934e;
        }

        @Override // me.Sink
        public void write(me.d dVar, long j10) {
            k.e(dVar, "source");
            if (!(!this.f11935f)) {
                throw new IllegalStateException("closed".toString());
            }
            if (j10 == 0) {
                return;
            }
            this.f11936g.f11927d.T(j10);
            this.f11936g.f11927d.E("\r\n");
            this.f11936g.f11927d.write(dVar, j10);
            this.f11936g.f11927d.E("\r\n");
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* compiled from: Http1ExchangeCodec.kt */
    @Metadata(bv = {}, d1 = {"\u0000(\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\t\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0004\b\u0082\u0004\u0018\u00002\u00060\u0001R\u00020\u0002B\u000f\u0012\u0006\u0010\f\u001a\u00020\u000b¢\u0006\u0004\b\r\u0010\u000eJ\b\u0010\u0004\u001a\u00020\u0003H\u0002J\u0018\u0010\t\u001a\u00020\u00072\u0006\u0010\u0006\u001a\u00020\u00052\u0006\u0010\b\u001a\u00020\u0007H\u0016J\b\u0010\n\u001a\u00020\u0003H\u0016¨\u0006\u000f"}, d2 = {"Lge/b$c;", "Lge/b$a;", "Lge/b;", "Lma/f0;", "u", "Lme/d;", "sink", "", "byteCount", "read", "close", "Lzd/u;", "url", "<init>", "(Lge/b;Lzd/u;)V", "okhttp"}, k = 1, mv = {1, 6, 0})
    /* renamed from: ge.b$c */
    /* loaded from: classes2.dex */
    public final class c extends a {

        /* renamed from: h, reason: collision with root package name */
        private final HttpUrl f11937h;

        /* renamed from: i, reason: collision with root package name */
        private long f11938i;

        /* renamed from: j, reason: collision with root package name */
        private boolean f11939j;

        /* renamed from: k, reason: collision with root package name */
        final /* synthetic */ Http1ExchangeCodec f11940k;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        public c(Http1ExchangeCodec http1ExchangeCodec, HttpUrl httpUrl) {
            super(http1ExchangeCodec);
            k.e(http1ExchangeCodec, "this$0");
            k.e(httpUrl, "url");
            this.f11940k = http1ExchangeCodec;
            this.f11937h = httpUrl;
            this.f11938i = -1L;
            this.f11939j = true;
        }

        /* JADX WARN: Code restructure failed: missing block: B:12:0x004b, code lost:
        
            if (r1 != false) goto L14;
         */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        private final void u() {
            CharSequence J0;
            boolean D;
            if (this.f11938i != -1) {
                this.f11940k.f11926c.Z();
            }
            try {
                this.f11938i = this.f11940k.f11926c.r0();
                J0 = v.J0(this.f11940k.f11926c.Z());
                String obj = J0.toString();
                if (this.f11938i >= 0) {
                    if (obj.length() > 0) {
                        D = StringsJVM.D(obj, ";", false, 2, null);
                    }
                    if (this.f11938i == 0) {
                        this.f11939j = false;
                        Http1ExchangeCodec http1ExchangeCodec = this.f11940k;
                        http1ExchangeCodec.f11930g = http1ExchangeCodec.f11929f.a();
                        OkHttpClient okHttpClient = this.f11940k.f11924a;
                        k.b(okHttpClient);
                        CookieJar f20748n = okHttpClient.getF20748n();
                        HttpUrl httpUrl = this.f11937h;
                        Headers headers = this.f11940k.f11930g;
                        k.b(headers);
                        HttpHeaders.f(f20748n, httpUrl, headers);
                        c();
                        return;
                    }
                    return;
                }
                throw new ProtocolException("expected chunk size and optional extensions but was \"" + this.f11938i + obj + '\"');
            } catch (NumberFormatException e10) {
                throw new ProtocolException(e10.getMessage());
            }
        }

        @Override // me.Source, java.io.Closeable, java.lang.AutoCloseable
        public void close() {
            if (getF11932f()) {
                return;
            }
            if (this.f11939j && !ae.d.r(this, 100, TimeUnit.MILLISECONDS)) {
                this.f11940k.getF12402a().y();
                c();
            }
            m(true);
        }

        @Override // ge.Http1ExchangeCodec.a, me.Source
        public long read(me.d sink, long byteCount) {
            k.e(sink, "sink");
            if (byteCount >= 0) {
                if (!getF11932f()) {
                    if (!this.f11939j) {
                        return -1L;
                    }
                    long j10 = this.f11938i;
                    if (j10 == 0 || j10 == -1) {
                        u();
                        if (!this.f11939j) {
                            return -1L;
                        }
                    }
                    long read = super.read(sink, Math.min(byteCount, this.f11938i));
                    if (read != -1) {
                        this.f11938i -= read;
                        return read;
                    }
                    this.f11940k.getF12402a().y();
                    ProtocolException protocolException = new ProtocolException("unexpected end of stream");
                    c();
                    throw protocolException;
                }
                throw new IllegalStateException("closed".toString());
            }
            throw new IllegalArgumentException(k.l("byteCount < 0: ", Long.valueOf(byteCount)).toString());
        }
    }

    /* compiled from: Http1ExchangeCodec.kt */
    @Metadata(bv = {}, d1 = {"\u0000\u0018\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\u0010\t\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u000b\b\u0086\u0003\u0018\u00002\u00020\u0001B\t\b\u0002¢\u0006\u0004\b\u000e\u0010\u000fR\u0014\u0010\u0003\u001a\u00020\u00028\u0002X\u0082T¢\u0006\u0006\n\u0004\b\u0003\u0010\u0004R\u0014\u0010\u0006\u001a\u00020\u00058\u0002X\u0082T¢\u0006\u0006\n\u0004\b\u0006\u0010\u0007R\u0014\u0010\b\u001a\u00020\u00058\u0002X\u0082T¢\u0006\u0006\n\u0004\b\b\u0010\u0007R\u0014\u0010\t\u001a\u00020\u00058\u0002X\u0082T¢\u0006\u0006\n\u0004\b\t\u0010\u0007R\u0014\u0010\n\u001a\u00020\u00058\u0002X\u0082T¢\u0006\u0006\n\u0004\b\n\u0010\u0007R\u0014\u0010\u000b\u001a\u00020\u00058\u0002X\u0082T¢\u0006\u0006\n\u0004\b\u000b\u0010\u0007R\u0014\u0010\f\u001a\u00020\u00058\u0002X\u0082T¢\u0006\u0006\n\u0004\b\f\u0010\u0007R\u0014\u0010\r\u001a\u00020\u00058\u0002X\u0082T¢\u0006\u0006\n\u0004\b\r\u0010\u0007¨\u0006\u0010"}, d2 = {"Lge/b$d;", "", "", "NO_CHUNK_YET", "J", "", "STATE_CLOSED", "I", "STATE_IDLE", "STATE_OPEN_REQUEST_BODY", "STATE_OPEN_RESPONSE_BODY", "STATE_READING_RESPONSE_BODY", "STATE_READ_RESPONSE_HEADERS", "STATE_WRITING_REQUEST_BODY", "<init>", "()V", "okhttp"}, k = 1, mv = {1, 6, 0})
    /* renamed from: ge.b$d */
    /* loaded from: classes2.dex */
    public static final class d {
        private d() {
        }

        public /* synthetic */ d(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* compiled from: Http1ExchangeCodec.kt */
    @Metadata(bv = {}, d1 = {"\u0000\"\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\t\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\b\u0082\u0004\u0018\u00002\u00060\u0001R\u00020\u0002B\u000f\u0012\u0006\u0010\n\u001a\u00020\u0005¢\u0006\u0004\b\u000b\u0010\fJ\u0018\u0010\u0007\u001a\u00020\u00052\u0006\u0010\u0004\u001a\u00020\u00032\u0006\u0010\u0006\u001a\u00020\u0005H\u0016J\b\u0010\t\u001a\u00020\bH\u0016¨\u0006\r"}, d2 = {"Lge/b$e;", "Lge/b$a;", "Lge/b;", "Lme/d;", "sink", "", "byteCount", "read", "Lma/f0;", "close", "bytesRemaining", "<init>", "(Lge/b;J)V", "okhttp"}, k = 1, mv = {1, 6, 0})
    /* renamed from: ge.b$e */
    /* loaded from: classes2.dex */
    public final class e extends a {

        /* renamed from: h, reason: collision with root package name */
        private long f11941h;

        /* renamed from: i, reason: collision with root package name */
        final /* synthetic */ Http1ExchangeCodec f11942i;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        public e(Http1ExchangeCodec http1ExchangeCodec, long j10) {
            super(http1ExchangeCodec);
            k.e(http1ExchangeCodec, "this$0");
            this.f11942i = http1ExchangeCodec;
            this.f11941h = j10;
            if (j10 == 0) {
                c();
            }
        }

        @Override // me.Source, java.io.Closeable, java.lang.AutoCloseable
        public void close() {
            if (getF11932f()) {
                return;
            }
            if (this.f11941h != 0 && !ae.d.r(this, 100, TimeUnit.MILLISECONDS)) {
                this.f11942i.getF12402a().y();
                c();
            }
            m(true);
        }

        @Override // ge.Http1ExchangeCodec.a, me.Source
        public long read(me.d sink, long byteCount) {
            k.e(sink, "sink");
            if (byteCount >= 0) {
                if (!getF11932f()) {
                    long j10 = this.f11941h;
                    if (j10 == 0) {
                        return -1L;
                    }
                    long read = super.read(sink, Math.min(j10, byteCount));
                    if (read != -1) {
                        long j11 = this.f11941h - read;
                        this.f11941h = j11;
                        if (j11 == 0) {
                            c();
                        }
                        return read;
                    }
                    this.f11942i.getF12402a().y();
                    ProtocolException protocolException = new ProtocolException("unexpected end of stream");
                    c();
                    throw protocolException;
                }
                throw new IllegalStateException("closed".toString());
            }
            throw new IllegalArgumentException(k.l("byteCount < 0: ", Long.valueOf(byteCount)).toString());
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* compiled from: Http1ExchangeCodec.kt */
    @Metadata(bv = {}, d1 = {"\u0000\"\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\t\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0006\b\u0082\u0004\u0018\u00002\u00020\u0001B\u0007¢\u0006\u0004\b\f\u0010\rJ\b\u0010\u0003\u001a\u00020\u0002H\u0016J\u0018\u0010\t\u001a\u00020\b2\u0006\u0010\u0005\u001a\u00020\u00042\u0006\u0010\u0007\u001a\u00020\u0006H\u0016J\b\u0010\n\u001a\u00020\bH\u0016J\b\u0010\u000b\u001a\u00020\bH\u0016¨\u0006\u000e"}, d2 = {"Lge/b$f;", "Lme/y;", "Lme/b0;", "timeout", "Lme/d;", "source", "", "byteCount", "Lma/f0;", "write", "flush", "close", "<init>", "(Lge/b;)V", "okhttp"}, k = 1, mv = {1, 6, 0})
    /* renamed from: ge.b$f */
    /* loaded from: classes2.dex */
    public final class f implements Sink {

        /* renamed from: e, reason: collision with root package name */
        private final ForwardingTimeout f11943e;

        /* renamed from: f, reason: collision with root package name */
        private boolean f11944f;

        /* renamed from: g, reason: collision with root package name */
        final /* synthetic */ Http1ExchangeCodec f11945g;

        public f(Http1ExchangeCodec http1ExchangeCodec) {
            k.e(http1ExchangeCodec, "this$0");
            this.f11945g = http1ExchangeCodec;
            this.f11943e = new ForwardingTimeout(http1ExchangeCodec.f11927d.timeout());
        }

        @Override // me.Sink, java.io.Closeable, java.lang.AutoCloseable
        public void close() {
            if (this.f11944f) {
                return;
            }
            this.f11944f = true;
            this.f11945g.r(this.f11943e);
            this.f11945g.f11928e = 3;
        }

        @Override // me.Sink, java.io.Flushable
        public void flush() {
            if (this.f11944f) {
                return;
            }
            this.f11945g.f11927d.flush();
        }

        @Override // me.Sink
        public Timeout timeout() {
            return this.f11943e;
        }

        @Override // me.Sink
        public void write(me.d dVar, long j10) {
            k.e(dVar, "source");
            if (!this.f11944f) {
                ae.d.k(dVar.v0(), 0L, j10);
                this.f11945g.f11927d.write(dVar, j10);
                return;
            }
            throw new IllegalStateException("closed".toString());
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* compiled from: Http1ExchangeCodec.kt */
    @Metadata(bv = {}, d1 = {"\u0000\"\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\t\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\b\u0082\u0004\u0018\u00002\u00060\u0001R\u00020\u0002B\u0007¢\u0006\u0004\b\n\u0010\u000bJ\u0018\u0010\u0007\u001a\u00020\u00052\u0006\u0010\u0004\u001a\u00020\u00032\u0006\u0010\u0006\u001a\u00020\u0005H\u0016J\b\u0010\t\u001a\u00020\bH\u0016¨\u0006\f"}, d2 = {"Lge/b$g;", "Lge/b$a;", "Lge/b;", "Lme/d;", "sink", "", "byteCount", "read", "Lma/f0;", "close", "<init>", "(Lge/b;)V", "okhttp"}, k = 1, mv = {1, 6, 0})
    /* renamed from: ge.b$g */
    /* loaded from: classes2.dex */
    public final class g extends a {

        /* renamed from: h, reason: collision with root package name */
        private boolean f11946h;

        /* renamed from: i, reason: collision with root package name */
        final /* synthetic */ Http1ExchangeCodec f11947i;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        public g(Http1ExchangeCodec http1ExchangeCodec) {
            super(http1ExchangeCodec);
            k.e(http1ExchangeCodec, "this$0");
            this.f11947i = http1ExchangeCodec;
        }

        @Override // me.Source, java.io.Closeable, java.lang.AutoCloseable
        public void close() {
            if (getF11932f()) {
                return;
            }
            if (!this.f11946h) {
                c();
            }
            m(true);
        }

        @Override // ge.Http1ExchangeCodec.a, me.Source
        public long read(me.d sink, long byteCount) {
            k.e(sink, "sink");
            if (byteCount >= 0) {
                if (!getF11932f()) {
                    if (this.f11946h) {
                        return -1L;
                    }
                    long read = super.read(sink, byteCount);
                    if (read != -1) {
                        return read;
                    }
                    this.f11946h = true;
                    c();
                    return -1L;
                }
                throw new IllegalStateException("closed".toString());
            }
            throw new IllegalArgumentException(k.l("byteCount < 0: ", Long.valueOf(byteCount)).toString());
        }
    }

    public Http1ExchangeCodec(OkHttpClient okHttpClient, RealConnection realConnection, BufferedSource bufferedSource, BufferedSink bufferedSink) {
        k.e(realConnection, "connection");
        k.e(bufferedSource, "source");
        k.e(bufferedSink, "sink");
        this.f11924a = okHttpClient;
        this.f11925b = realConnection;
        this.f11926c = bufferedSource;
        this.f11927d = bufferedSink;
        this.f11929f = new HeadersReader(bufferedSource);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void r(ForwardingTimeout forwardingTimeout) {
        Timeout i10 = forwardingTimeout.i();
        forwardingTimeout.j(Timeout.f15467e);
        i10.a();
        i10.b();
    }

    private final boolean s(z zVar) {
        boolean r10;
        r10 = StringsJVM.r("chunked", zVar.d("Transfer-Encoding"), true);
        return r10;
    }

    private final boolean t(b0 b0Var) {
        boolean r10;
        r10 = StringsJVM.r("chunked", b0.S(b0Var, "Transfer-Encoding", null, 2, null), true);
        return r10;
    }

    private final Sink u() {
        int i10 = this.f11928e;
        if (i10 == 1) {
            this.f11928e = 2;
            return new b(this);
        }
        throw new IllegalStateException(k.l("state: ", Integer.valueOf(i10)).toString());
    }

    private final Source v(HttpUrl url) {
        int i10 = this.f11928e;
        if (i10 == 4) {
            this.f11928e = 5;
            return new c(this, url);
        }
        throw new IllegalStateException(k.l("state: ", Integer.valueOf(i10)).toString());
    }

    private final Source w(long length) {
        int i10 = this.f11928e;
        if (i10 == 4) {
            this.f11928e = 5;
            return new e(this, length);
        }
        throw new IllegalStateException(k.l("state: ", Integer.valueOf(i10)).toString());
    }

    private final Sink x() {
        int i10 = this.f11928e;
        if (i10 == 1) {
            this.f11928e = 2;
            return new f(this);
        }
        throw new IllegalStateException(k.l("state: ", Integer.valueOf(i10)).toString());
    }

    private final Source y() {
        int i10 = this.f11928e;
        if (i10 == 4) {
            this.f11928e = 5;
            getF12402a().y();
            return new g(this);
        }
        throw new IllegalStateException(k.l("state: ", Integer.valueOf(i10)).toString());
    }

    public final void A(Headers headers, String str) {
        k.e(headers, "headers");
        k.e(str, "requestLine");
        int i10 = this.f11928e;
        if (i10 == 0) {
            this.f11927d.E(str).E("\r\n");
            int size = headers.size();
            for (int i11 = 0; i11 < size; i11++) {
                this.f11927d.E(headers.e(i11)).E(": ").E(headers.g(i11)).E("\r\n");
            }
            this.f11927d.E("\r\n");
            this.f11928e = 1;
            return;
        }
        throw new IllegalStateException(k.l("state: ", Integer.valueOf(i10)).toString());
    }

    @Override // fe.ExchangeCodec
    public void a() {
        this.f11927d.flush();
    }

    @Override // fe.ExchangeCodec
    public Source b(b0 response) {
        k.e(response, "response");
        if (!HttpHeaders.b(response)) {
            return w(0L);
        }
        if (t(response)) {
            return v(response.getF20505e().getF20796a());
        }
        long u7 = ae.d.u(response);
        if (u7 != -1) {
            return w(u7);
        }
        return y();
    }

    @Override // fe.ExchangeCodec
    public void c(z zVar) {
        k.e(zVar, "request");
        RequestLine requestLine = RequestLine.f11473a;
        Proxy.Type type = getF12402a().getF11211d().getF20561b().type();
        k.d(type, "connection.route().proxy.type()");
        A(zVar.getF20798c(), requestLine.a(zVar, type));
    }

    @Override // fe.ExchangeCodec
    public void cancel() {
        getF12402a().d();
    }

    @Override // fe.ExchangeCodec
    public Sink d(z request, long contentLength) {
        k.e(request, "request");
        if (request.getF20799d() != null && request.getF20799d().d()) {
            throw new ProtocolException("Duplex connections are not supported for HTTP/1");
        }
        if (s(request)) {
            return u();
        }
        if (contentLength != -1) {
            return x();
        }
        throw new IllegalStateException("Cannot stream a request body without chunked encoding or a known content length!");
    }

    @Override // fe.ExchangeCodec
    public b0.a e(boolean expectContinue) {
        int i10 = this.f11928e;
        boolean z10 = true;
        if (i10 != 1 && i10 != 3) {
            z10 = false;
        }
        if (z10) {
            try {
                StatusLine a10 = StatusLine.f11476d.a(this.f11929f.b());
                b0.a l10 = new b0.a().q(a10.f11477a).g(a10.f11478b).n(a10.f11479c).l(this.f11929f.a());
                if (expectContinue && a10.f11478b == 100) {
                    return null;
                }
                if (a10.f11478b == 100) {
                    this.f11928e = 3;
                    return l10;
                }
                this.f11928e = 4;
                return l10;
            } catch (EOFException e10) {
                throw new IOException(k.l("unexpected end of stream on ", getF12402a().getF11211d().getF20560a().getF20493i().n()), e10);
            }
        }
        throw new IllegalStateException(k.l("state: ", Integer.valueOf(i10)).toString());
    }

    @Override // fe.ExchangeCodec
    /* renamed from: f, reason: from getter */
    public RealConnection getF12402a() {
        return this.f11925b;
    }

    @Override // fe.ExchangeCodec
    public long g(b0 response) {
        k.e(response, "response");
        if (!HttpHeaders.b(response)) {
            return 0L;
        }
        if (t(response)) {
            return -1L;
        }
        return ae.d.u(response);
    }

    @Override // fe.ExchangeCodec
    public void h() {
        this.f11927d.flush();
    }

    public final void z(b0 b0Var) {
        k.e(b0Var, "response");
        long u7 = ae.d.u(b0Var);
        if (u7 == -1) {
            return;
        }
        Source w10 = w(u7);
        ae.d.K(w10, Integer.MAX_VALUE, TimeUnit.MILLISECONDS);
        w10.close();
    }
}
