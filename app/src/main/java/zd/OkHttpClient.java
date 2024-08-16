package zd;

import com.oplus.deepthinker.sdk.app.DataLinkConstants;
import ee.RouteDatabase;
import ie.Platform;
import java.net.Proxy;
import java.net.ProxySelector;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import javax.net.SocketFactory;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.X509TrustManager;
import ke.NullProxySelector;
import kotlin.Metadata;
import le.CertificateChainCleaner;
import le.OkHostnameVerifier;
import za.DefaultConstructorMarker;
import zd.EventListener;

/* compiled from: OkHttpClient.kt */
@Metadata(bv = {}, d1 = {"\u0000Â\u0001\n\u0002\u0018\u0002\n\u0002\u0010\u001a\n\u0002\u0010\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\u000b\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\b\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\b\n\u0002\b\f\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0007\b\u0016\u0018\u00002\u00020\u00012\u00020\u00022\u00020\u0002:\u0002yzB\u0011\b\u0000\u0012\u0006\u0010u\u001a\u00020t¢\u0006\u0004\bv\u0010wB\t\b\u0016¢\u0006\u0004\bv\u0010xJ\b\u0010\u0004\u001a\u00020\u0003H\u0002J\u0010\u0010\b\u001a\u00020\u00072\u0006\u0010\u0006\u001a\u00020\u0005H\u0016R\u0017\u0010\n\u001a\u00020\t8G¢\u0006\f\n\u0004\b\n\u0010\u000b\u001a\u0004\b\f\u0010\rR\u0017\u0010\u000f\u001a\u00020\u000e8G¢\u0006\f\n\u0004\b\u000f\u0010\u0010\u001a\u0004\b\u0011\u0010\u0012R\u001d\u0010\u0015\u001a\b\u0012\u0004\u0012\u00020\u00140\u00138G¢\u0006\f\n\u0004\b\u0015\u0010\u0016\u001a\u0004\b\u0017\u0010\u0018R\u001d\u0010\u0019\u001a\b\u0012\u0004\u0012\u00020\u00140\u00138G¢\u0006\f\n\u0004\b\u0019\u0010\u0016\u001a\u0004\b\u001a\u0010\u0018R\u0017\u0010\u001c\u001a\u00020\u001b8G¢\u0006\f\n\u0004\b\u001c\u0010\u001d\u001a\u0004\b\u001e\u0010\u001fR\u0017\u0010!\u001a\u00020 8G¢\u0006\f\n\u0004\b!\u0010\"\u001a\u0004\b#\u0010$R\u0017\u0010&\u001a\u00020%8G¢\u0006\f\n\u0004\b&\u0010'\u001a\u0004\b(\u0010)R\u0017\u0010*\u001a\u00020 8G¢\u0006\f\n\u0004\b*\u0010\"\u001a\u0004\b+\u0010$R\u0017\u0010,\u001a\u00020 8G¢\u0006\f\n\u0004\b,\u0010\"\u001a\u0004\b-\u0010$R\u0017\u0010/\u001a\u00020.8G¢\u0006\f\n\u0004\b/\u00100\u001a\u0004\b1\u00102R\u0019\u00104\u001a\u0004\u0018\u0001038G¢\u0006\f\n\u0004\b4\u00105\u001a\u0004\b6\u00107R\u0017\u00109\u001a\u0002088G¢\u0006\f\n\u0004\b9\u0010:\u001a\u0004\b;\u0010<R\u0019\u0010>\u001a\u0004\u0018\u00010=8G¢\u0006\f\n\u0004\b>\u0010?\u001a\u0004\b@\u0010AR\u0017\u0010C\u001a\u00020B8G¢\u0006\f\n\u0004\bC\u0010D\u001a\u0004\bE\u0010FR\u0017\u0010G\u001a\u00020%8G¢\u0006\f\n\u0004\bG\u0010'\u001a\u0004\bH\u0010)R\u0017\u0010J\u001a\u00020I8G¢\u0006\f\n\u0004\bJ\u0010K\u001a\u0004\bL\u0010MR\u0011\u0010Q\u001a\u00020N8G¢\u0006\u0006\u001a\u0004\bO\u0010PR\u001d\u0010S\u001a\b\u0012\u0004\u0012\u00020R0\u00138G¢\u0006\f\n\u0004\bS\u0010\u0016\u001a\u0004\bT\u0010\u0018R\u001d\u0010V\u001a\b\u0012\u0004\u0012\u00020U0\u00138G¢\u0006\f\n\u0004\bV\u0010\u0016\u001a\u0004\bW\u0010\u0018R\u0017\u0010Y\u001a\u00020X8G¢\u0006\f\n\u0004\bY\u0010Z\u001a\u0004\b[\u0010\\R\u0017\u0010^\u001a\u00020]8G¢\u0006\f\n\u0004\b^\u0010_\u001a\u0004\b`\u0010aR\u0017\u0010c\u001a\u00020b8G¢\u0006\f\n\u0004\bc\u0010d\u001a\u0004\be\u0010fR\u0017\u0010g\u001a\u00020b8G¢\u0006\f\n\u0004\bg\u0010d\u001a\u0004\bh\u0010fR\u0017\u0010i\u001a\u00020b8G¢\u0006\f\n\u0004\bi\u0010d\u001a\u0004\bj\u0010fR\u0017\u0010k\u001a\u00020b8G¢\u0006\f\n\u0004\bk\u0010d\u001a\u0004\bl\u0010fR\u0017\u0010m\u001a\u00020b8G¢\u0006\f\n\u0004\bm\u0010d\u001a\u0004\bn\u0010fR\u0017\u0010p\u001a\u00020o8\u0006¢\u0006\f\n\u0004\bp\u0010q\u001a\u0004\br\u0010s¨\u0006{"}, d2 = {"Lzd/x;", "", "", "Lma/f0;", "E", "Lzd/z;", "request", "Lzd/e;", "u", "Lzd/p;", "dispatcher", "Lzd/p;", "k", "()Lzd/p;", "Lzd/k;", "connectionPool", "Lzd/k;", "h", "()Lzd/k;", "", "Lzd/v;", "interceptors", "Ljava/util/List;", "s", "()Ljava/util/List;", "networkInterceptors", "t", "Lzd/r$c;", "eventListenerFactory", "Lzd/r$c;", "n", "()Lzd/r$c;", "", "retryOnConnectionFailure", "Z", "B", "()Z", "Lzd/b;", "authenticator", "Lzd/b;", "c", "()Lzd/b;", "followRedirects", "o", "followSslRedirects", "p", "Lzd/n;", "cookieJar", "Lzd/n;", "j", "()Lzd/n;", "Lzd/c;", "cache", "Lzd/c;", "d", "()Lzd/c;", "Lzd/q;", "dns", "Lzd/q;", "l", "()Lzd/q;", "Ljava/net/Proxy;", "proxy", "Ljava/net/Proxy;", "x", "()Ljava/net/Proxy;", "Ljava/net/ProxySelector;", "proxySelector", "Ljava/net/ProxySelector;", "z", "()Ljava/net/ProxySelector;", "proxyAuthenticator", "y", "Ljavax/net/SocketFactory;", "socketFactory", "Ljavax/net/SocketFactory;", "C", "()Ljavax/net/SocketFactory;", "Ljavax/net/ssl/SSLSocketFactory;", "D", "()Ljavax/net/ssl/SSLSocketFactory;", "sslSocketFactory", "Lzd/l;", "connectionSpecs", "i", "Lzd/y;", "protocols", "w", "Ljavax/net/ssl/HostnameVerifier;", "hostnameVerifier", "Ljavax/net/ssl/HostnameVerifier;", "r", "()Ljavax/net/ssl/HostnameVerifier;", "Lzd/g;", "certificatePinner", "Lzd/g;", "f", "()Lzd/g;", "", "callTimeoutMillis", "I", "e", "()I", "connectTimeoutMillis", "g", "readTimeoutMillis", "A", "writeTimeoutMillis", "F", "pingIntervalMillis", "v", "Lee/h;", "routeDatabase", "Lee/h;", "q", "()Lee/h;", "Lzd/x$a;", "builder", "<init>", "(Lzd/x$a;)V", "()V", "a", "b", "okhttp"}, k = 1, mv = {1, 6, 0})
/* renamed from: zd.x, reason: use source file name */
/* loaded from: classes2.dex */
public class OkHttpClient implements Cloneable {
    public static final b H = new b(null);
    private static final List<Protocol> I = ae.d.v(Protocol.HTTP_2, Protocol.HTTP_1_1);
    private static final List<ConnectionSpec> J = ae.d.v(ConnectionSpec.f20660i, ConnectionSpec.f20662k);
    private final int A;
    private final int B;
    private final int C;
    private final int D;
    private final int E;
    private final long F;
    private final RouteDatabase G;

    /* renamed from: e, reason: collision with root package name */
    private final p f20739e;

    /* renamed from: f, reason: collision with root package name */
    private final ConnectionPool f20740f;

    /* renamed from: g, reason: collision with root package name */
    private final List<v> f20741g;

    /* renamed from: h, reason: collision with root package name */
    private final List<v> f20742h;

    /* renamed from: i, reason: collision with root package name */
    private final EventListener.c f20743i;

    /* renamed from: j, reason: collision with root package name */
    private final boolean f20744j;

    /* renamed from: k, reason: collision with root package name */
    private final Authenticator f20745k;

    /* renamed from: l, reason: collision with root package name */
    private final boolean f20746l;

    /* renamed from: m, reason: collision with root package name */
    private final boolean f20747m;

    /* renamed from: n, reason: collision with root package name */
    private final CookieJar f20748n;

    /* renamed from: o, reason: collision with root package name */
    private final Dns f20749o;

    /* renamed from: p, reason: collision with root package name */
    private final Proxy f20750p;

    /* renamed from: q, reason: collision with root package name */
    private final ProxySelector f20751q;

    /* renamed from: r, reason: collision with root package name */
    private final Authenticator f20752r;

    /* renamed from: s, reason: collision with root package name */
    private final SocketFactory f20753s;

    /* renamed from: t, reason: collision with root package name */
    private final SSLSocketFactory f20754t;

    /* renamed from: u, reason: collision with root package name */
    private final X509TrustManager f20755u;

    /* renamed from: v, reason: collision with root package name */
    private final List<ConnectionSpec> f20756v;

    /* renamed from: w, reason: collision with root package name */
    private final List<Protocol> f20757w;

    /* renamed from: x, reason: collision with root package name */
    private final HostnameVerifier f20758x;

    /* renamed from: y, reason: collision with root package name */
    private final CertificatePinner f20759y;

    /* renamed from: z, reason: collision with root package name */
    private final CertificateChainCleaner f20760z;

    /* compiled from: OkHttpClient.kt */
    @Metadata(bv = {}, d1 = {"\u0000À\u0001\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0010!\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0010\u000b\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\b\f\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\b\t\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0010\b\n\u0002\b\u0012\n\u0002\u0010\t\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\b\t\u0018\u00002\u00020\u0001B\t¢\u0006\u0006\b®\u0001\u0010¯\u0001R\"\u0010\u0003\u001a\u00020\u00028\u0000@\u0000X\u0080\u000e¢\u0006\u0012\n\u0004\b\u0003\u0010\u0004\u001a\u0004\b\u0005\u0010\u0006\"\u0004\b\u0007\u0010\bR\"\u0010\n\u001a\u00020\t8\u0000@\u0000X\u0080\u000e¢\u0006\u0012\n\u0004\b\n\u0010\u000b\u001a\u0004\b\f\u0010\r\"\u0004\b\u000e\u0010\u000fR \u0010\u0012\u001a\b\u0012\u0004\u0012\u00020\u00110\u00108\u0000X\u0080\u0004¢\u0006\f\n\u0004\b\u0012\u0010\u0013\u001a\u0004\b\u0014\u0010\u0015R \u0010\u0016\u001a\b\u0012\u0004\u0012\u00020\u00110\u00108\u0000X\u0080\u0004¢\u0006\f\n\u0004\b\u0016\u0010\u0013\u001a\u0004\b\u0017\u0010\u0015R\"\u0010\u0019\u001a\u00020\u00188\u0000@\u0000X\u0080\u000e¢\u0006\u0012\n\u0004\b\u0019\u0010\u001a\u001a\u0004\b\u001b\u0010\u001c\"\u0004\b\u001d\u0010\u001eR\"\u0010 \u001a\u00020\u001f8\u0000@\u0000X\u0080\u000e¢\u0006\u0012\n\u0004\b \u0010!\u001a\u0004\b\"\u0010#\"\u0004\b$\u0010%R\"\u0010'\u001a\u00020&8\u0000@\u0000X\u0080\u000e¢\u0006\u0012\n\u0004\b'\u0010(\u001a\u0004\b)\u0010*\"\u0004\b+\u0010,R\"\u0010-\u001a\u00020\u001f8\u0000@\u0000X\u0080\u000e¢\u0006\u0012\n\u0004\b-\u0010!\u001a\u0004\b.\u0010#\"\u0004\b/\u0010%R\"\u00100\u001a\u00020\u001f8\u0000@\u0000X\u0080\u000e¢\u0006\u0012\n\u0004\b0\u0010!\u001a\u0004\b1\u0010#\"\u0004\b2\u0010%R\"\u00104\u001a\u0002038\u0000@\u0000X\u0080\u000e¢\u0006\u0012\n\u0004\b4\u00105\u001a\u0004\b6\u00107\"\u0004\b8\u00109R$\u0010;\u001a\u0004\u0018\u00010:8\u0000@\u0000X\u0080\u000e¢\u0006\u0012\n\u0004\b;\u0010<\u001a\u0004\b=\u0010>\"\u0004\b?\u0010@R\"\u0010B\u001a\u00020A8\u0000@\u0000X\u0080\u000e¢\u0006\u0012\n\u0004\bB\u0010C\u001a\u0004\bD\u0010E\"\u0004\bF\u0010GR$\u0010I\u001a\u0004\u0018\u00010H8\u0000@\u0000X\u0080\u000e¢\u0006\u0012\n\u0004\bI\u0010J\u001a\u0004\bK\u0010L\"\u0004\bM\u0010NR$\u0010P\u001a\u0004\u0018\u00010O8\u0000@\u0000X\u0080\u000e¢\u0006\u0012\n\u0004\bP\u0010Q\u001a\u0004\bR\u0010S\"\u0004\bT\u0010UR\"\u0010V\u001a\u00020&8\u0000@\u0000X\u0080\u000e¢\u0006\u0012\n\u0004\bV\u0010(\u001a\u0004\bW\u0010*\"\u0004\bX\u0010,R\"\u0010Z\u001a\u00020Y8\u0000@\u0000X\u0080\u000e¢\u0006\u0012\n\u0004\bZ\u0010[\u001a\u0004\b\\\u0010]\"\u0004\b^\u0010_R$\u0010a\u001a\u0004\u0018\u00010`8\u0000@\u0000X\u0080\u000e¢\u0006\u0012\n\u0004\ba\u0010b\u001a\u0004\bc\u0010d\"\u0004\be\u0010fR$\u0010h\u001a\u0004\u0018\u00010g8\u0000@\u0000X\u0080\u000e¢\u0006\u0012\n\u0004\bh\u0010i\u001a\u0004\bj\u0010k\"\u0004\bl\u0010mR(\u0010p\u001a\b\u0012\u0004\u0012\u00020o0n8\u0000@\u0000X\u0080\u000e¢\u0006\u0012\n\u0004\bp\u0010\u0013\u001a\u0004\bq\u0010\u0015\"\u0004\br\u0010sR(\u0010u\u001a\b\u0012\u0004\u0012\u00020t0n8\u0000@\u0000X\u0080\u000e¢\u0006\u0012\n\u0004\bu\u0010\u0013\u001a\u0004\bv\u0010\u0015\"\u0004\bw\u0010sR\"\u0010y\u001a\u00020x8\u0000@\u0000X\u0080\u000e¢\u0006\u0012\n\u0004\by\u0010z\u001a\u0004\b{\u0010|\"\u0004\b}\u0010~R)\u0010\u0080\u0001\u001a\u00020\u007f8\u0000@\u0000X\u0080\u000e¢\u0006\u0018\n\u0006\b\u0080\u0001\u0010\u0081\u0001\u001a\u0006\b\u0082\u0001\u0010\u0083\u0001\"\u0006\b\u0084\u0001\u0010\u0085\u0001R,\u0010\u0087\u0001\u001a\u0005\u0018\u00010\u0086\u00018\u0000@\u0000X\u0080\u000e¢\u0006\u0018\n\u0006\b\u0087\u0001\u0010\u0088\u0001\u001a\u0006\b\u0089\u0001\u0010\u008a\u0001\"\u0006\b\u008b\u0001\u0010\u008c\u0001R*\u0010\u008e\u0001\u001a\u00030\u008d\u00018\u0000@\u0000X\u0080\u000e¢\u0006\u0018\n\u0006\b\u008e\u0001\u0010\u008f\u0001\u001a\u0006\b\u0090\u0001\u0010\u0091\u0001\"\u0006\b\u0092\u0001\u0010\u0093\u0001R*\u0010\u0094\u0001\u001a\u00030\u008d\u00018\u0000@\u0000X\u0080\u000e¢\u0006\u0018\n\u0006\b\u0094\u0001\u0010\u008f\u0001\u001a\u0006\b\u0095\u0001\u0010\u0091\u0001\"\u0006\b\u0096\u0001\u0010\u0093\u0001R*\u0010\u0097\u0001\u001a\u00030\u008d\u00018\u0000@\u0000X\u0080\u000e¢\u0006\u0018\n\u0006\b\u0097\u0001\u0010\u008f\u0001\u001a\u0006\b\u0098\u0001\u0010\u0091\u0001\"\u0006\b\u0099\u0001\u0010\u0093\u0001R*\u0010\u009a\u0001\u001a\u00030\u008d\u00018\u0000@\u0000X\u0080\u000e¢\u0006\u0018\n\u0006\b\u009a\u0001\u0010\u008f\u0001\u001a\u0006\b\u009b\u0001\u0010\u0091\u0001\"\u0006\b\u009c\u0001\u0010\u0093\u0001R*\u0010\u009d\u0001\u001a\u00030\u008d\u00018\u0000@\u0000X\u0080\u000e¢\u0006\u0018\n\u0006\b\u009d\u0001\u0010\u008f\u0001\u001a\u0006\b\u009e\u0001\u0010\u0091\u0001\"\u0006\b\u009f\u0001\u0010\u0093\u0001R*\u0010¡\u0001\u001a\u00030 \u00018\u0000@\u0000X\u0080\u000e¢\u0006\u0018\n\u0006\b¡\u0001\u0010¢\u0001\u001a\u0006\b£\u0001\u0010¤\u0001\"\u0006\b¥\u0001\u0010¦\u0001R,\u0010¨\u0001\u001a\u0005\u0018\u00010§\u00018\u0000@\u0000X\u0080\u000e¢\u0006\u0018\n\u0006\b¨\u0001\u0010©\u0001\u001a\u0006\bª\u0001\u0010«\u0001\"\u0006\b¬\u0001\u0010\u00ad\u0001¨\u0006°\u0001"}, d2 = {"Lzd/x$a;", "", "Lzd/p;", "dispatcher", "Lzd/p;", "j", "()Lzd/p;", "setDispatcher$okhttp", "(Lzd/p;)V", "Lzd/k;", "connectionPool", "Lzd/k;", "g", "()Lzd/k;", "setConnectionPool$okhttp", "(Lzd/k;)V", "", "Lzd/v;", "interceptors", "Ljava/util/List;", "p", "()Ljava/util/List;", "networkInterceptors", "r", "Lzd/r$c;", "eventListenerFactory", "Lzd/r$c;", "l", "()Lzd/r$c;", "setEventListenerFactory$okhttp", "(Lzd/r$c;)V", "", "retryOnConnectionFailure", "Z", "y", "()Z", "setRetryOnConnectionFailure$okhttp", "(Z)V", "Lzd/b;", "authenticator", "Lzd/b;", "a", "()Lzd/b;", "setAuthenticator$okhttp", "(Lzd/b;)V", "followRedirects", "m", "setFollowRedirects$okhttp", "followSslRedirects", "n", "setFollowSslRedirects$okhttp", "Lzd/n;", "cookieJar", "Lzd/n;", "i", "()Lzd/n;", "setCookieJar$okhttp", "(Lzd/n;)V", "Lzd/c;", "cache", "Lzd/c;", "b", "()Lzd/c;", "setCache$okhttp", "(Lzd/c;)V", "Lzd/q;", "dns", "Lzd/q;", "k", "()Lzd/q;", "setDns$okhttp", "(Lzd/q;)V", "Ljava/net/Proxy;", "proxy", "Ljava/net/Proxy;", "u", "()Ljava/net/Proxy;", "setProxy$okhttp", "(Ljava/net/Proxy;)V", "Ljava/net/ProxySelector;", "proxySelector", "Ljava/net/ProxySelector;", "w", "()Ljava/net/ProxySelector;", "setProxySelector$okhttp", "(Ljava/net/ProxySelector;)V", "proxyAuthenticator", "v", "setProxyAuthenticator$okhttp", "Ljavax/net/SocketFactory;", "socketFactory", "Ljavax/net/SocketFactory;", "A", "()Ljavax/net/SocketFactory;", "setSocketFactory$okhttp", "(Ljavax/net/SocketFactory;)V", "Ljavax/net/ssl/SSLSocketFactory;", "sslSocketFactoryOrNull", "Ljavax/net/ssl/SSLSocketFactory;", "B", "()Ljavax/net/ssl/SSLSocketFactory;", "setSslSocketFactoryOrNull$okhttp", "(Ljavax/net/ssl/SSLSocketFactory;)V", "Ljavax/net/ssl/X509TrustManager;", "x509TrustManagerOrNull", "Ljavax/net/ssl/X509TrustManager;", "D", "()Ljavax/net/ssl/X509TrustManager;", "setX509TrustManagerOrNull$okhttp", "(Ljavax/net/ssl/X509TrustManager;)V", "", "Lzd/l;", "connectionSpecs", "h", "setConnectionSpecs$okhttp", "(Ljava/util/List;)V", "Lzd/y;", "protocols", "t", "setProtocols$okhttp", "Ljavax/net/ssl/HostnameVerifier;", "hostnameVerifier", "Ljavax/net/ssl/HostnameVerifier;", "o", "()Ljavax/net/ssl/HostnameVerifier;", "setHostnameVerifier$okhttp", "(Ljavax/net/ssl/HostnameVerifier;)V", "Lzd/g;", "certificatePinner", "Lzd/g;", "e", "()Lzd/g;", "setCertificatePinner$okhttp", "(Lzd/g;)V", "Lle/c;", "certificateChainCleaner", "Lle/c;", "d", "()Lle/c;", "setCertificateChainCleaner$okhttp", "(Lle/c;)V", "", "callTimeout", "I", "c", "()I", "setCallTimeout$okhttp", "(I)V", "connectTimeout", "f", "setConnectTimeout$okhttp", "readTimeout", "x", "setReadTimeout$okhttp", "writeTimeout", "C", "setWriteTimeout$okhttp", "pingInterval", "s", "setPingInterval$okhttp", "", "minWebSocketMessageToCompress", "J", "q", "()J", "setMinWebSocketMessageToCompress$okhttp", "(J)V", "Lee/h;", "routeDatabase", "Lee/h;", "z", "()Lee/h;", "setRouteDatabase$okhttp", "(Lee/h;)V", "<init>", "()V", "okhttp"}, k = 1, mv = {1, 6, 0})
    /* renamed from: zd.x$a */
    /* loaded from: classes2.dex */
    public static final class a {
        private int A;
        private long B;
        private RouteDatabase C;

        /* renamed from: a, reason: collision with root package name */
        private p f20761a = new p();

        /* renamed from: b, reason: collision with root package name */
        private ConnectionPool f20762b = new ConnectionPool();

        /* renamed from: c, reason: collision with root package name */
        private final List<v> f20763c = new ArrayList();

        /* renamed from: d, reason: collision with root package name */
        private final List<v> f20764d = new ArrayList();

        /* renamed from: e, reason: collision with root package name */
        private EventListener.c f20765e = ae.d.g(EventListener.f20700b);

        /* renamed from: f, reason: collision with root package name */
        private boolean f20766f = true;

        /* renamed from: g, reason: collision with root package name */
        private Authenticator f20767g;

        /* renamed from: h, reason: collision with root package name */
        private boolean f20768h;

        /* renamed from: i, reason: collision with root package name */
        private boolean f20769i;

        /* renamed from: j, reason: collision with root package name */
        private CookieJar f20770j;

        /* renamed from: k, reason: collision with root package name */
        private Dns f20771k;

        /* renamed from: l, reason: collision with root package name */
        private Proxy f20772l;

        /* renamed from: m, reason: collision with root package name */
        private ProxySelector f20773m;

        /* renamed from: n, reason: collision with root package name */
        private Authenticator f20774n;

        /* renamed from: o, reason: collision with root package name */
        private SocketFactory f20775o;

        /* renamed from: p, reason: collision with root package name */
        private SSLSocketFactory f20776p;

        /* renamed from: q, reason: collision with root package name */
        private X509TrustManager f20777q;

        /* renamed from: r, reason: collision with root package name */
        private List<ConnectionSpec> f20778r;

        /* renamed from: s, reason: collision with root package name */
        private List<? extends Protocol> f20779s;

        /* renamed from: t, reason: collision with root package name */
        private HostnameVerifier f20780t;

        /* renamed from: u, reason: collision with root package name */
        private CertificatePinner f20781u;

        /* renamed from: v, reason: collision with root package name */
        private CertificateChainCleaner f20782v;

        /* renamed from: w, reason: collision with root package name */
        private int f20783w;

        /* renamed from: x, reason: collision with root package name */
        private int f20784x;

        /* renamed from: y, reason: collision with root package name */
        private int f20785y;

        /* renamed from: z, reason: collision with root package name */
        private int f20786z;

        public a() {
            Authenticator authenticator = Authenticator.f20502b;
            this.f20767g = authenticator;
            this.f20768h = true;
            this.f20769i = true;
            this.f20770j = CookieJar.f20686b;
            this.f20771k = Dns.f20697b;
            this.f20774n = authenticator;
            SocketFactory socketFactory = SocketFactory.getDefault();
            za.k.d(socketFactory, "getDefault()");
            this.f20775o = socketFactory;
            b bVar = OkHttpClient.H;
            this.f20778r = bVar.a();
            this.f20779s = bVar.b();
            this.f20780t = OkHostnameVerifier.f14709a;
            this.f20781u = CertificatePinner.f20572d;
            this.f20784x = DataLinkConstants.RUS_UPDATE;
            this.f20785y = DataLinkConstants.RUS_UPDATE;
            this.f20786z = DataLinkConstants.RUS_UPDATE;
            this.B = 1024L;
        }

        /* renamed from: A, reason: from getter */
        public final SocketFactory getF20775o() {
            return this.f20775o;
        }

        /* renamed from: B, reason: from getter */
        public final SSLSocketFactory getF20776p() {
            return this.f20776p;
        }

        /* renamed from: C, reason: from getter */
        public final int getF20786z() {
            return this.f20786z;
        }

        /* renamed from: D, reason: from getter */
        public final X509TrustManager getF20777q() {
            return this.f20777q;
        }

        /* renamed from: a, reason: from getter */
        public final Authenticator getF20767g() {
            return this.f20767g;
        }

        public final c b() {
            return null;
        }

        /* renamed from: c, reason: from getter */
        public final int getF20783w() {
            return this.f20783w;
        }

        /* renamed from: d, reason: from getter */
        public final CertificateChainCleaner getF20782v() {
            return this.f20782v;
        }

        /* renamed from: e, reason: from getter */
        public final CertificatePinner getF20781u() {
            return this.f20781u;
        }

        /* renamed from: f, reason: from getter */
        public final int getF20784x() {
            return this.f20784x;
        }

        /* renamed from: g, reason: from getter */
        public final ConnectionPool getF20762b() {
            return this.f20762b;
        }

        public final List<ConnectionSpec> h() {
            return this.f20778r;
        }

        /* renamed from: i, reason: from getter */
        public final CookieJar getF20770j() {
            return this.f20770j;
        }

        /* renamed from: j, reason: from getter */
        public final p getF20761a() {
            return this.f20761a;
        }

        /* renamed from: k, reason: from getter */
        public final Dns getF20771k() {
            return this.f20771k;
        }

        /* renamed from: l, reason: from getter */
        public final EventListener.c getF20765e() {
            return this.f20765e;
        }

        /* renamed from: m, reason: from getter */
        public final boolean getF20768h() {
            return this.f20768h;
        }

        /* renamed from: n, reason: from getter */
        public final boolean getF20769i() {
            return this.f20769i;
        }

        /* renamed from: o, reason: from getter */
        public final HostnameVerifier getF20780t() {
            return this.f20780t;
        }

        public final List<v> p() {
            return this.f20763c;
        }

        /* renamed from: q, reason: from getter */
        public final long getB() {
            return this.B;
        }

        public final List<v> r() {
            return this.f20764d;
        }

        /* renamed from: s, reason: from getter */
        public final int getA() {
            return this.A;
        }

        public final List<Protocol> t() {
            return this.f20779s;
        }

        /* renamed from: u, reason: from getter */
        public final Proxy getF20772l() {
            return this.f20772l;
        }

        /* renamed from: v, reason: from getter */
        public final Authenticator getF20774n() {
            return this.f20774n;
        }

        /* renamed from: w, reason: from getter */
        public final ProxySelector getF20773m() {
            return this.f20773m;
        }

        /* renamed from: x, reason: from getter */
        public final int getF20785y() {
            return this.f20785y;
        }

        /* renamed from: y, reason: from getter */
        public final boolean getF20766f() {
            return this.f20766f;
        }

        /* renamed from: z, reason: from getter */
        public final RouteDatabase getC() {
            return this.C;
        }
    }

    /* compiled from: OkHttpClient.kt */
    @Metadata(bv = {}, d1 = {"\u0000\u001c\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0005\b\u0086\u0003\u0018\u00002\u00020\u0001B\t\b\u0002¢\u0006\u0004\b\u000b\u0010\fR \u0010\u0004\u001a\b\u0012\u0004\u0012\u00020\u00030\u00028\u0000X\u0080\u0004¢\u0006\f\n\u0004\b\u0004\u0010\u0005\u001a\u0004\b\u0006\u0010\u0007R \u0010\t\u001a\b\u0012\u0004\u0012\u00020\b0\u00028\u0000X\u0080\u0004¢\u0006\f\n\u0004\b\t\u0010\u0005\u001a\u0004\b\n\u0010\u0007¨\u0006\r"}, d2 = {"Lzd/x$b;", "", "", "Lzd/y;", "DEFAULT_PROTOCOLS", "Ljava/util/List;", "b", "()Ljava/util/List;", "Lzd/l;", "DEFAULT_CONNECTION_SPECS", "a", "<init>", "()V", "okhttp"}, k = 1, mv = {1, 6, 0})
    /* renamed from: zd.x$b */
    /* loaded from: classes2.dex */
    public static final class b {
        private b() {
        }

        public /* synthetic */ b(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        public final List<ConnectionSpec> a() {
            return OkHttpClient.J;
        }

        public final List<Protocol> b() {
            return OkHttpClient.I;
        }
    }

    public OkHttpClient(a aVar) {
        ProxySelector f20773m;
        za.k.e(aVar, "builder");
        this.f20739e = aVar.getF20761a();
        this.f20740f = aVar.getF20762b();
        this.f20741g = ae.d.R(aVar.p());
        this.f20742h = ae.d.R(aVar.r());
        this.f20743i = aVar.getF20765e();
        this.f20744j = aVar.getF20766f();
        this.f20745k = aVar.getF20767g();
        this.f20746l = aVar.getF20768h();
        this.f20747m = aVar.getF20769i();
        this.f20748n = aVar.getF20770j();
        aVar.b();
        this.f20749o = aVar.getF20771k();
        this.f20750p = aVar.getF20772l();
        if (aVar.getF20772l() != null) {
            f20773m = NullProxySelector.f14304a;
        } else {
            f20773m = aVar.getF20773m();
            f20773m = f20773m == null ? ProxySelector.getDefault() : f20773m;
            if (f20773m == null) {
                f20773m = NullProxySelector.f14304a;
            }
        }
        this.f20751q = f20773m;
        this.f20752r = aVar.getF20774n();
        this.f20753s = aVar.getF20775o();
        List<ConnectionSpec> h10 = aVar.h();
        this.f20756v = h10;
        this.f20757w = aVar.t();
        this.f20758x = aVar.getF20780t();
        this.A = aVar.getF20783w();
        this.B = aVar.getF20784x();
        this.C = aVar.getF20785y();
        this.D = aVar.getF20786z();
        this.E = aVar.getA();
        this.F = aVar.getB();
        RouteDatabase c10 = aVar.getC();
        this.G = c10 == null ? new RouteDatabase() : c10;
        boolean z10 = true;
        if (!(h10 instanceof Collection) || !h10.isEmpty()) {
            Iterator<T> it = h10.iterator();
            while (true) {
                if (!it.hasNext()) {
                    break;
                } else if (((ConnectionSpec) it.next()).getF20663a()) {
                    z10 = false;
                    break;
                }
            }
        }
        if (z10) {
            this.f20754t = null;
            this.f20760z = null;
            this.f20755u = null;
            this.f20759y = CertificatePinner.f20572d;
        } else if (aVar.getF20776p() != null) {
            this.f20754t = aVar.getF20776p();
            CertificateChainCleaner f20782v = aVar.getF20782v();
            za.k.b(f20782v);
            this.f20760z = f20782v;
            X509TrustManager f20777q = aVar.getF20777q();
            za.k.b(f20777q);
            this.f20755u = f20777q;
            CertificatePinner f20781u = aVar.getF20781u();
            za.k.b(f20782v);
            this.f20759y = f20781u.e(f20782v);
        } else {
            Platform.a aVar2 = Platform.f12870a;
            X509TrustManager o10 = aVar2.g().o();
            this.f20755u = o10;
            Platform g6 = aVar2.g();
            za.k.b(o10);
            this.f20754t = g6.n(o10);
            CertificateChainCleaner.a aVar3 = CertificateChainCleaner.f14708a;
            za.k.b(o10);
            CertificateChainCleaner a10 = aVar3.a(o10);
            this.f20760z = a10;
            CertificatePinner f20781u2 = aVar.getF20781u();
            za.k.b(a10);
            this.f20759y = f20781u2.e(a10);
        }
        E();
    }

    private final void E() {
        boolean z10;
        if (!this.f20741g.contains(null)) {
            if (!this.f20742h.contains(null)) {
                List<ConnectionSpec> list = this.f20756v;
                if (!(list instanceof Collection) || !list.isEmpty()) {
                    Iterator<T> it = list.iterator();
                    while (it.hasNext()) {
                        if (((ConnectionSpec) it.next()).getF20663a()) {
                            z10 = false;
                            break;
                        }
                    }
                }
                z10 = true;
                if (z10) {
                    if (this.f20754t == null) {
                        if (this.f20760z == null) {
                            if (this.f20755u == null) {
                                if (!za.k.a(this.f20759y, CertificatePinner.f20572d)) {
                                    throw new IllegalStateException("Check failed.".toString());
                                }
                                return;
                            }
                            throw new IllegalStateException("Check failed.".toString());
                        }
                        throw new IllegalStateException("Check failed.".toString());
                    }
                    throw new IllegalStateException("Check failed.".toString());
                }
                if (this.f20754t != null) {
                    if (this.f20760z != null) {
                        if (this.f20755u == null) {
                            throw new IllegalStateException("x509TrustManager == null".toString());
                        }
                        return;
                    }
                    throw new IllegalStateException("certificateChainCleaner == null".toString());
                }
                throw new IllegalStateException("sslSocketFactory == null".toString());
            }
            throw new IllegalStateException(za.k.l("Null network interceptor: ", t()).toString());
        }
        throw new IllegalStateException(za.k.l("Null interceptor: ", s()).toString());
    }

    /* renamed from: A, reason: from getter */
    public final int getC() {
        return this.C;
    }

    /* renamed from: B, reason: from getter */
    public final boolean getF20744j() {
        return this.f20744j;
    }

    /* renamed from: C, reason: from getter */
    public final SocketFactory getF20753s() {
        return this.f20753s;
    }

    public final SSLSocketFactory D() {
        SSLSocketFactory sSLSocketFactory = this.f20754t;
        if (sSLSocketFactory != null) {
            return sSLSocketFactory;
        }
        throw new IllegalStateException("CLEARTEXT-only client");
    }

    /* renamed from: F, reason: from getter */
    public final int getD() {
        return this.D;
    }

    /* renamed from: c, reason: from getter */
    public final Authenticator getF20745k() {
        return this.f20745k;
    }

    public Object clone() {
        return super.clone();
    }

    public final c d() {
        return null;
    }

    /* renamed from: e, reason: from getter */
    public final int getA() {
        return this.A;
    }

    /* renamed from: f, reason: from getter */
    public final CertificatePinner getF20759y() {
        return this.f20759y;
    }

    /* renamed from: g, reason: from getter */
    public final int getB() {
        return this.B;
    }

    /* renamed from: h, reason: from getter */
    public final ConnectionPool getF20740f() {
        return this.f20740f;
    }

    public final List<ConnectionSpec> i() {
        return this.f20756v;
    }

    /* renamed from: j, reason: from getter */
    public final CookieJar getF20748n() {
        return this.f20748n;
    }

    /* renamed from: k, reason: from getter */
    public final p getF20739e() {
        return this.f20739e;
    }

    /* renamed from: l, reason: from getter */
    public final Dns getF20749o() {
        return this.f20749o;
    }

    /* renamed from: n, reason: from getter */
    public final EventListener.c getF20743i() {
        return this.f20743i;
    }

    /* renamed from: o, reason: from getter */
    public final boolean getF20746l() {
        return this.f20746l;
    }

    /* renamed from: p, reason: from getter */
    public final boolean getF20747m() {
        return this.f20747m;
    }

    /* renamed from: q, reason: from getter */
    public final RouteDatabase getG() {
        return this.G;
    }

    /* renamed from: r, reason: from getter */
    public final HostnameVerifier getF20758x() {
        return this.f20758x;
    }

    public final List<v> s() {
        return this.f20741g;
    }

    public final List<v> t() {
        return this.f20742h;
    }

    public e u(z request) {
        za.k.e(request, "request");
        return new ee.e(this, request, false);
    }

    /* renamed from: v, reason: from getter */
    public final int getE() {
        return this.E;
    }

    public final List<Protocol> w() {
        return this.f20757w;
    }

    /* renamed from: x, reason: from getter */
    public final Proxy getF20750p() {
        return this.f20750p;
    }

    /* renamed from: y, reason: from getter */
    public final Authenticator getF20752r() {
        return this.f20752r;
    }

    /* renamed from: z, reason: from getter */
    public final ProxySelector getF20751q() {
        return this.f20751q;
    }

    public OkHttpClient() {
        this(new a());
    }
}
