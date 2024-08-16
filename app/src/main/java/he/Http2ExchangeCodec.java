package he;

import ee.RealConnection;
import fe.ExchangeCodec;
import fe.HttpHeaders;
import fe.RequestLine;
import fe.StatusLine;
import java.io.IOException;
import java.net.ProtocolException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;
import kotlin.Metadata;
import me.Sink;
import me.Source;
import me.Timeout;
import za.DefaultConstructorMarker;
import zd.Headers;
import zd.OkHttpClient;
import zd.Protocol;
import zd.b0;
import zd.z;

/* compiled from: Http2ExchangeCodec.kt */
@Metadata(bv = {}, d1 = {"\u0000Z\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\t\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\u0018\u00002\u00020\u0001:\u0001\u000bB'\u0012\u0006\u0010\u001c\u001a\u00020\u001b\u0012\u0006\u0010\u0017\u001a\u00020\u0016\u0012\u0006\u0010\u001e\u001a\u00020\u001d\u0012\u0006\u0010 \u001a\u00020\u001f¢\u0006\u0004\b!\u0010\"J\u0018\u0010\u0007\u001a\u00020\u00062\u0006\u0010\u0003\u001a\u00020\u00022\u0006\u0010\u0005\u001a\u00020\u0004H\u0016J\u0010\u0010\t\u001a\u00020\b2\u0006\u0010\u0003\u001a\u00020\u0002H\u0016J\b\u0010\n\u001a\u00020\bH\u0016J\b\u0010\u000b\u001a\u00020\bH\u0016J\u0012\u0010\u000f\u001a\u0004\u0018\u00010\u000e2\u0006\u0010\r\u001a\u00020\fH\u0016J\u0010\u0010\u0012\u001a\u00020\u00042\u0006\u0010\u0011\u001a\u00020\u0010H\u0016J\u0010\u0010\u0014\u001a\u00020\u00132\u0006\u0010\u0011\u001a\u00020\u0010H\u0016J\b\u0010\u0015\u001a\u00020\bH\u0016R\u001a\u0010\u0017\u001a\u00020\u00168\u0016X\u0096\u0004¢\u0006\f\n\u0004\b\u0017\u0010\u0018\u001a\u0004\b\u0019\u0010\u001a¨\u0006#"}, d2 = {"Lhe/g;", "Lfe/d;", "Lzd/z;", "request", "", "contentLength", "Lme/y;", "d", "Lma/f0;", "c", "h", "a", "", "expectContinue", "Lzd/b0$a;", "e", "Lzd/b0;", "response", "g", "Lme/a0;", "b", "cancel", "Lee/f;", "connection", "Lee/f;", "f", "()Lee/f;", "Lzd/x;", "client", "Lfe/g;", "chain", "Lhe/f;", "http2Connection", "<init>", "(Lzd/x;Lee/f;Lfe/g;Lhe/f;)V", "okhttp"}, k = 1, mv = {1, 6, 0})
/* renamed from: he.g, reason: use source file name */
/* loaded from: classes2.dex */
public final class Http2ExchangeCodec implements ExchangeCodec {

    /* renamed from: g, reason: collision with root package name */
    public static final a f12399g = new a(null);

    /* renamed from: h, reason: collision with root package name */
    private static final List<String> f12400h = ae.d.v("connection", "host", "keep-alive", "proxy-connection", "te", "transfer-encoding", "encoding", "upgrade", ":method", ":path", ":scheme", ":authority");

    /* renamed from: i, reason: collision with root package name */
    private static final List<String> f12401i = ae.d.v("connection", "host", "keep-alive", "proxy-connection", "te", "transfer-encoding", "encoding", "upgrade");

    /* renamed from: a, reason: collision with root package name */
    private final RealConnection f12402a;

    /* renamed from: b, reason: collision with root package name */
    private final fe.g f12403b;

    /* renamed from: c, reason: collision with root package name */
    private final Http2Connection f12404c;

    /* renamed from: d, reason: collision with root package name */
    private volatile Http2Stream f12405d;

    /* renamed from: e, reason: collision with root package name */
    private final Protocol f12406e;

    /* renamed from: f, reason: collision with root package name */
    private volatile boolean f12407f;

    /* compiled from: Http2ExchangeCodec.kt */
    @Metadata(bv = {}, d1 = {"\u00002\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u000f\b\u0086\u0003\u0018\u00002\u00020\u0001B\t\b\u0002¢\u0006\u0004\b\u001a\u0010\u001bJ\u0014\u0010\u0006\u001a\b\u0012\u0004\u0012\u00020\u00050\u00042\u0006\u0010\u0003\u001a\u00020\u0002J\u0016\u0010\f\u001a\u00020\u000b2\u0006\u0010\b\u001a\u00020\u00072\u0006\u0010\n\u001a\u00020\tR\u0014\u0010\u000e\u001a\u00020\r8\u0002X\u0082T¢\u0006\u0006\n\u0004\b\u000e\u0010\u000fR\u0014\u0010\u0010\u001a\u00020\r8\u0002X\u0082T¢\u0006\u0006\n\u0004\b\u0010\u0010\u000fR\u0014\u0010\u0011\u001a\u00020\r8\u0002X\u0082T¢\u0006\u0006\n\u0004\b\u0011\u0010\u000fR\u001a\u0010\u0012\u001a\b\u0012\u0004\u0012\u00020\r0\u00048\u0002X\u0082\u0004¢\u0006\u0006\n\u0004\b\u0012\u0010\u0013R\u001a\u0010\u0014\u001a\b\u0012\u0004\u0012\u00020\r0\u00048\u0002X\u0082\u0004¢\u0006\u0006\n\u0004\b\u0014\u0010\u0013R\u0014\u0010\u0015\u001a\u00020\r8\u0002X\u0082T¢\u0006\u0006\n\u0004\b\u0015\u0010\u000fR\u0014\u0010\u0016\u001a\u00020\r8\u0002X\u0082T¢\u0006\u0006\n\u0004\b\u0016\u0010\u000fR\u0014\u0010\u0017\u001a\u00020\r8\u0002X\u0082T¢\u0006\u0006\n\u0004\b\u0017\u0010\u000fR\u0014\u0010\u0018\u001a\u00020\r8\u0002X\u0082T¢\u0006\u0006\n\u0004\b\u0018\u0010\u000fR\u0014\u0010\u0019\u001a\u00020\r8\u0002X\u0082T¢\u0006\u0006\n\u0004\b\u0019\u0010\u000f¨\u0006\u001c"}, d2 = {"Lhe/g$a;", "", "Lzd/z;", "request", "", "Lhe/c;", "a", "Lzd/t;", "headerBlock", "Lzd/y;", "protocol", "Lzd/b0$a;", "b", "", "CONNECTION", "Ljava/lang/String;", "ENCODING", "HOST", "HTTP_2_SKIPPED_REQUEST_HEADERS", "Ljava/util/List;", "HTTP_2_SKIPPED_RESPONSE_HEADERS", "KEEP_ALIVE", "PROXY_CONNECTION", "TE", "TRANSFER_ENCODING", "UPGRADE", "<init>", "()V", "okhttp"}, k = 1, mv = {1, 6, 0})
    /* renamed from: he.g$a */
    /* loaded from: classes2.dex */
    public static final class a {
        private a() {
        }

        public /* synthetic */ a(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        public final List<Header> a(z request) {
            za.k.e(request, "request");
            Headers f20798c = request.getF20798c();
            ArrayList arrayList = new ArrayList(f20798c.size() + 4);
            arrayList.add(new Header(Header.f12274g, request.getF20797b()));
            arrayList.add(new Header(Header.f12275h, RequestLine.f11473a.c(request.getF20796a())));
            String d10 = request.d("Host");
            if (d10 != null) {
                arrayList.add(new Header(Header.f12277j, d10));
            }
            arrayList.add(new Header(Header.f12276i, request.getF20796a().getF20713a()));
            int i10 = 0;
            int size = f20798c.size();
            while (i10 < size) {
                int i11 = i10 + 1;
                String e10 = f20798c.e(i10);
                Locale locale = Locale.US;
                za.k.d(locale, "US");
                String lowerCase = e10.toLowerCase(locale);
                za.k.d(lowerCase, "this as java.lang.String).toLowerCase(locale)");
                if (!Http2ExchangeCodec.f12400h.contains(lowerCase) || (za.k.a(lowerCase, "te") && za.k.a(f20798c.g(i10), "trailers"))) {
                    arrayList.add(new Header(lowerCase, f20798c.g(i10)));
                }
                i10 = i11;
            }
            return arrayList;
        }

        public final b0.a b(Headers headerBlock, Protocol protocol) {
            za.k.e(headerBlock, "headerBlock");
            za.k.e(protocol, "protocol");
            Headers.a aVar = new Headers.a();
            int size = headerBlock.size();
            StatusLine statusLine = null;
            int i10 = 0;
            while (i10 < size) {
                int i11 = i10 + 1;
                String e10 = headerBlock.e(i10);
                String g6 = headerBlock.g(i10);
                if (za.k.a(e10, ":status")) {
                    statusLine = StatusLine.f11476d.a(za.k.l("HTTP/1.1 ", g6));
                } else if (!Http2ExchangeCodec.f12401i.contains(e10)) {
                    aVar.c(e10, g6);
                }
                i10 = i11;
            }
            if (statusLine != null) {
                return new b0.a().q(protocol).g(statusLine.f11478b).n(statusLine.f11479c).l(aVar.d());
            }
            throw new ProtocolException("Expected ':status' header not present");
        }
    }

    public Http2ExchangeCodec(OkHttpClient okHttpClient, RealConnection realConnection, fe.g gVar, Http2Connection http2Connection) {
        za.k.e(okHttpClient, "client");
        za.k.e(realConnection, "connection");
        za.k.e(gVar, "chain");
        za.k.e(http2Connection, "http2Connection");
        this.f12402a = realConnection;
        this.f12403b = gVar;
        this.f12404c = http2Connection;
        List<Protocol> w10 = okHttpClient.w();
        Protocol protocol = Protocol.H2_PRIOR_KNOWLEDGE;
        this.f12406e = w10.contains(protocol) ? protocol : Protocol.HTTP_2;
    }

    @Override // fe.ExchangeCodec
    public void a() {
        Http2Stream http2Stream = this.f12405d;
        za.k.b(http2Stream);
        http2Stream.n().close();
    }

    @Override // fe.ExchangeCodec
    public Source b(b0 response) {
        za.k.e(response, "response");
        Http2Stream http2Stream = this.f12405d;
        za.k.b(http2Stream);
        return http2Stream.getF12429i();
    }

    @Override // fe.ExchangeCodec
    public void c(z zVar) {
        za.k.e(zVar, "request");
        if (this.f12405d != null) {
            return;
        }
        this.f12405d = this.f12404c.K0(f12399g.a(zVar), zVar.getF20799d() != null);
        if (!this.f12407f) {
            Http2Stream http2Stream = this.f12405d;
            za.k.b(http2Stream);
            Timeout v7 = http2Stream.v();
            long f11467g = this.f12403b.getF11467g();
            TimeUnit timeUnit = TimeUnit.MILLISECONDS;
            v7.g(f11467g, timeUnit);
            Http2Stream http2Stream2 = this.f12405d;
            za.k.b(http2Stream2);
            http2Stream2.G().g(this.f12403b.getF11468h(), timeUnit);
            return;
        }
        Http2Stream http2Stream3 = this.f12405d;
        za.k.b(http2Stream3);
        http2Stream3.f(ErrorCode.CANCEL);
        throw new IOException("Canceled");
    }

    @Override // fe.ExchangeCodec
    public void cancel() {
        this.f12407f = true;
        Http2Stream http2Stream = this.f12405d;
        if (http2Stream == null) {
            return;
        }
        http2Stream.f(ErrorCode.CANCEL);
    }

    @Override // fe.ExchangeCodec
    public Sink d(z request, long contentLength) {
        za.k.e(request, "request");
        Http2Stream http2Stream = this.f12405d;
        za.k.b(http2Stream);
        return http2Stream.n();
    }

    @Override // fe.ExchangeCodec
    public b0.a e(boolean expectContinue) {
        Http2Stream http2Stream = this.f12405d;
        za.k.b(http2Stream);
        b0.a b10 = f12399g.b(http2Stream.E(), this.f12406e);
        if (expectContinue && b10.getF20521c() == 100) {
            return null;
        }
        return b10;
    }

    @Override // fe.ExchangeCodec
    /* renamed from: f, reason: from getter */
    public RealConnection getF12402a() {
        return this.f12402a;
    }

    @Override // fe.ExchangeCodec
    public long g(b0 response) {
        za.k.e(response, "response");
        if (HttpHeaders.b(response)) {
            return ae.d.u(response);
        }
        return 0L;
    }

    @Override // fe.ExchangeCodec
    public void h() {
        this.f12404c.flush();
    }
}
