package ee;

import com.coui.appcompat.touchsearchview.COUIAccessibilityUtil;
import de.TaskRunner;
import fe.ExchangeCodec;
import ge.Http1ExchangeCodec;
import he.ConnectionShutdownException;
import he.ErrorCode;
import he.Http2Connection;
import he.Http2ExchangeCodec;
import he.Http2Stream;
import he.Settings;
import he.StreamResetException;
import ie.Platform;
import java.io.IOException;
import java.lang.ref.Reference;
import java.net.ConnectException;
import java.net.ProtocolException;
import java.net.Proxy;
import java.net.Socket;
import java.net.UnknownServiceException;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLPeerUnverifiedException;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import kotlin.Metadata;
import kotlin.collections.s;
import le.CertificateChainCleaner;
import le.OkHostnameVerifier;
import me.BufferedSink;
import me.BufferedSource;
import me.Timeout;
import me.n;
import sd.Indent;
import sd.StringsJVM;
import za.DefaultConstructorMarker;
import za.Lambda;
import za.k;
import zd.Address;
import zd.CertificatePinner;
import zd.CipherSuite;
import zd.Connection;
import zd.ConnectionSpec;
import zd.EventListener;
import zd.Handshake;
import zd.HttpUrl;
import zd.OkHttpClient;
import zd.Protocol;
import zd.b0;
import zd.d0;
import zd.z;

/* compiled from: RealConnection.kt */
@Metadata(bv = {}, d1 = {"\u0000Æ\u0001\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\b\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\r\n\u0002\u0010!\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\t\n\u0002\b\b\n\u0002\u0018\u0002\n\u0002\b\u0005\u0018\u00002\u00020\u00012\u00020\u0002:\u0001BB\u0017\u0012\u0006\u0010j\u001a\u00020i\u0012\u0006\u0010k\u001a\u00020\u001b¢\u0006\u0004\bl\u0010mJ0\u0010\f\u001a\u00020\u000b2\u0006\u0010\u0004\u001a\u00020\u00032\u0006\u0010\u0005\u001a\u00020\u00032\u0006\u0010\u0006\u001a\u00020\u00032\u0006\u0010\b\u001a\u00020\u00072\u0006\u0010\n\u001a\u00020\tH\u0002J(\u0010\r\u001a\u00020\u000b2\u0006\u0010\u0004\u001a\u00020\u00032\u0006\u0010\u0005\u001a\u00020\u00032\u0006\u0010\b\u001a\u00020\u00072\u0006\u0010\n\u001a\u00020\tH\u0002J(\u0010\u0011\u001a\u00020\u000b2\u0006\u0010\u000f\u001a\u00020\u000e2\u0006\u0010\u0010\u001a\u00020\u00032\u0006\u0010\b\u001a\u00020\u00072\u0006\u0010\n\u001a\u00020\tH\u0002J\u0010\u0010\u0012\u001a\u00020\u000b2\u0006\u0010\u0010\u001a\u00020\u0003H\u0002J\u0010\u0010\u0013\u001a\u00020\u000b2\u0006\u0010\u000f\u001a\u00020\u000eH\u0002J*\u0010\u0018\u001a\u0004\u0018\u00010\u00142\u0006\u0010\u0005\u001a\u00020\u00032\u0006\u0010\u0006\u001a\u00020\u00032\u0006\u0010\u0015\u001a\u00020\u00142\u0006\u0010\u0017\u001a\u00020\u0016H\u0002J\b\u0010\u0019\u001a\u00020\u0014H\u0002J\u0016\u0010\u001e\u001a\u00020\u001d2\f\u0010\u001c\u001a\b\u0012\u0004\u0012\u00020\u001b0\u001aH\u0002J\u0010\u0010\u001f\u001a\u00020\u001d2\u0006\u0010\u0017\u001a\u00020\u0016H\u0002J\u0018\u0010\"\u001a\u00020\u001d2\u0006\u0010\u0017\u001a\u00020\u00162\u0006\u0010!\u001a\u00020 H\u0002J\u000f\u0010#\u001a\u00020\u000bH\u0000¢\u0006\u0004\b#\u0010$J\u000f\u0010%\u001a\u00020\u000bH\u0000¢\u0006\u0004\b%\u0010$J\u000f\u0010&\u001a\u00020\u000bH\u0000¢\u0006\u0004\b&\u0010$J>\u0010(\u001a\u00020\u000b2\u0006\u0010\u0004\u001a\u00020\u00032\u0006\u0010\u0005\u001a\u00020\u00032\u0006\u0010\u0006\u001a\u00020\u00032\u0006\u0010\u0010\u001a\u00020\u00032\u0006\u0010'\u001a\u00020\u001d2\u0006\u0010\b\u001a\u00020\u00072\u0006\u0010\n\u001a\u00020\tJ'\u0010,\u001a\u00020\u001d2\u0006\u0010*\u001a\u00020)2\u000e\u0010+\u001a\n\u0012\u0004\u0012\u00020\u001b\u0018\u00010\u001aH\u0000¢\u0006\u0004\b,\u0010-J\u001f\u00103\u001a\u0002022\u0006\u0010/\u001a\u00020.2\u0006\u00101\u001a\u000200H\u0000¢\u0006\u0004\b3\u00104J\b\u00105\u001a\u00020\u001bH\u0016J\u0006\u00106\u001a\u00020\u000bJ\b\u00108\u001a\u000207H\u0016J\u000e\u0010:\u001a\u00020\u001d2\u0006\u00109\u001a\u00020\u001dJ\u0010\u0010=\u001a\u00020\u000b2\u0006\u0010<\u001a\u00020;H\u0016J\u0018\u0010B\u001a\u00020\u000b2\u0006\u0010?\u001a\u00020>2\u0006\u0010A\u001a\u00020@H\u0016J\n\u0010C\u001a\u0004\u0018\u00010 H\u0016J'\u0010G\u001a\u00020\u000b2\u0006\u0010/\u001a\u00020.2\u0006\u0010D\u001a\u00020\u001b2\u0006\u0010F\u001a\u00020EH\u0000¢\u0006\u0004\bG\u0010HJ!\u0010J\u001a\u00020\u000b2\u0006\u0010\b\u001a\u00020I2\b\u0010\"\u001a\u0004\u0018\u00010EH\u0000¢\u0006\u0004\bJ\u0010KJ\b\u0010M\u001a\u00020LH\u0016R\"\u0010N\u001a\u00020\u001d8\u0006@\u0006X\u0086\u000e¢\u0006\u0012\n\u0004\bN\u0010O\u001a\u0004\bP\u0010Q\"\u0004\bR\u0010SR\"\u0010T\u001a\u00020\u00038\u0000@\u0000X\u0080\u000e¢\u0006\u0012\n\u0004\bT\u0010U\u001a\u0004\bV\u0010W\"\u0004\bX\u0010YR#\u0010\\\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020I0[0Z8\u0006¢\u0006\f\n\u0004\b\\\u0010]\u001a\u0004\b^\u0010_R\"\u0010a\u001a\u00020`8\u0000@\u0000X\u0080\u000e¢\u0006\u0012\n\u0004\ba\u0010b\u001a\u0004\bc\u0010d\"\u0004\be\u0010fR\u0014\u0010h\u001a\u00020\u001d8@X\u0080\u0004¢\u0006\u0006\u001a\u0004\bg\u0010Q¨\u0006n"}, d2 = {"Lee/f;", "Lhe/f$c;", "Lzd/j;", "", "connectTimeout", "readTimeout", "writeTimeout", "Lzd/e;", "call", "Lzd/r;", "eventListener", "Lma/f0;", "j", "h", "Lee/b;", "connectionSpecSelector", "pingIntervalMillis", "m", "E", "i", "Lzd/z;", "tunnelRequest", "Lzd/u;", "url", "k", "l", "", "Lzd/d0;", "candidates", "", "A", "F", "Lzd/s;", "handshake", "e", "y", "()V", "x", "s", "connectionRetryEnabled", "f", "Lzd/a;", "address", "routes", "t", "(Lzd/a;Ljava/util/List;)Z", "Lzd/x;", "client", "Lfe/g;", "chain", "Lfe/d;", "w", "(Lzd/x;Lfe/g;)Lfe/d;", "z", "d", "Ljava/net/Socket;", "D", "doExtensiveChecks", "u", "Lhe/i;", "stream", "b", "Lhe/f;", "connection", "Lhe/m;", "settings", "a", "r", "failedRoute", "Ljava/io/IOException;", "failure", "g", "(Lzd/x;Lzd/d0;Ljava/io/IOException;)V", "Lee/e;", "G", "(Lee/e;Ljava/io/IOException;)V", "", "toString", "noNewExchanges", "Z", "p", "()Z", "C", "(Z)V", "routeFailureCount", "I", "q", "()I", "setRouteFailureCount$okhttp", "(I)V", "", "Ljava/lang/ref/Reference;", "calls", "Ljava/util/List;", "n", "()Ljava/util/List;", "", "idleAtNs", "J", "o", "()J", "B", "(J)V", "v", "isMultiplexed", "Lee/g;", "connectionPool", "route", "<init>", "(Lee/g;Lzd/d0;)V", "okhttp"}, k = 1, mv = {1, 6, 0})
/* renamed from: ee.f, reason: use source file name */
/* loaded from: classes2.dex */
public final class RealConnection extends Http2Connection.c implements Connection {

    /* renamed from: t, reason: collision with root package name */
    public static final a f11209t = new a(null);

    /* renamed from: c, reason: collision with root package name */
    private final RealConnectionPool f11210c;

    /* renamed from: d, reason: collision with root package name */
    private final d0 f11211d;

    /* renamed from: e, reason: collision with root package name */
    private Socket f11212e;

    /* renamed from: f, reason: collision with root package name */
    private Socket f11213f;

    /* renamed from: g, reason: collision with root package name */
    private Handshake f11214g;

    /* renamed from: h, reason: collision with root package name */
    private Protocol f11215h;

    /* renamed from: i, reason: collision with root package name */
    private Http2Connection f11216i;

    /* renamed from: j, reason: collision with root package name */
    private BufferedSource f11217j;

    /* renamed from: k, reason: collision with root package name */
    private BufferedSink f11218k;

    /* renamed from: l, reason: collision with root package name */
    private boolean f11219l;

    /* renamed from: m, reason: collision with root package name */
    private boolean f11220m;

    /* renamed from: n, reason: collision with root package name */
    private int f11221n;

    /* renamed from: o, reason: collision with root package name */
    private int f11222o;

    /* renamed from: p, reason: collision with root package name */
    private int f11223p;

    /* renamed from: q, reason: collision with root package name */
    private int f11224q;

    /* renamed from: r, reason: collision with root package name */
    private final List<Reference<e>> f11225r;

    /* renamed from: s, reason: collision with root package name */
    private long f11226s;

    /* compiled from: RealConnection.kt */
    @Metadata(bv = {}, d1 = {"\u0000 \n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\u0010\t\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0005\b\u0086\u0003\u0018\u00002\u00020\u0001B\t\b\u0002¢\u0006\u0004\b\u000b\u0010\fR\u0014\u0010\u0003\u001a\u00020\u00028\u0000X\u0080T¢\u0006\u0006\n\u0004\b\u0003\u0010\u0004R\u0014\u0010\u0006\u001a\u00020\u00058\u0002X\u0082T¢\u0006\u0006\n\u0004\b\u0006\u0010\u0007R\u0014\u0010\t\u001a\u00020\b8\u0002X\u0082T¢\u0006\u0006\n\u0004\b\t\u0010\n¨\u0006\r"}, d2 = {"Lee/f$a;", "", "", "IDLE_CONNECTION_HEALTHY_NS", "J", "", "MAX_TUNNEL_ATTEMPTS", "I", "", "NPE_THROW_WITH_NULL", "Ljava/lang/String;", "<init>", "()V", "okhttp"}, k = 1, mv = {1, 6, 0})
    /* renamed from: ee.f$a */
    /* loaded from: classes2.dex */
    public static final class a {
        private a() {
        }

        public /* synthetic */ a(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }
    }

    /* compiled from: RealConnection.kt */
    @Metadata(k = 3, mv = {1, 6, 0}, xi = 48)
    /* renamed from: ee.f$b */
    /* loaded from: classes2.dex */
    public /* synthetic */ class b {

        /* renamed from: a, reason: collision with root package name */
        public static final /* synthetic */ int[] f11227a;

        static {
            int[] iArr = new int[Proxy.Type.values().length];
            iArr[Proxy.Type.DIRECT.ordinal()] = 1;
            iArr[Proxy.Type.HTTP.ordinal()] = 2;
            f11227a = iArr;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: RealConnection.kt */
    @Metadata(bv = {}, d1 = {"\u0000\f\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0002\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00010\u0000H\n¢\u0006\u0004\b\u0002\u0010\u0003"}, d2 = {"", "Ljava/security/cert/Certificate;", "a", "()Ljava/util/List;"}, k = 3, mv = {1, 6, 0})
    /* renamed from: ee.f$c */
    /* loaded from: classes2.dex */
    public static final class c extends Lambda implements ya.a<List<? extends Certificate>> {

        /* renamed from: e, reason: collision with root package name */
        final /* synthetic */ CertificatePinner f11228e;

        /* renamed from: f, reason: collision with root package name */
        final /* synthetic */ Handshake f11229f;

        /* renamed from: g, reason: collision with root package name */
        final /* synthetic */ Address f11230g;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        c(CertificatePinner certificatePinner, Handshake handshake, Address address) {
            super(0);
            this.f11228e = certificatePinner;
            this.f11229f = handshake;
            this.f11230g = address;
        }

        @Override // ya.a
        /* renamed from: a, reason: merged with bridge method [inline-methods] */
        public final List<Certificate> invoke() {
            CertificateChainCleaner f20574b = this.f11228e.getF20574b();
            k.b(f20574b);
            return f20574b.a(this.f11229f.d(), this.f11230g.getF20493i().getF20716d());
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: RealConnection.kt */
    @Metadata(bv = {}, d1 = {"\u0000\f\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0002\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00010\u0000H\n¢\u0006\u0004\b\u0002\u0010\u0003"}, d2 = {"", "Ljava/security/cert/X509Certificate;", "a", "()Ljava/util/List;"}, k = 3, mv = {1, 6, 0})
    /* renamed from: ee.f$d */
    /* loaded from: classes2.dex */
    public static final class d extends Lambda implements ya.a<List<? extends X509Certificate>> {
        d() {
            super(0);
        }

        @Override // ya.a
        /* renamed from: a, reason: merged with bridge method [inline-methods] */
        public final List<X509Certificate> invoke() {
            int u7;
            Handshake handshake = RealConnection.this.f11214g;
            k.b(handshake);
            List<Certificate> d10 = handshake.d();
            u7 = s.u(d10, 10);
            ArrayList arrayList = new ArrayList(u7);
            Iterator<T> it = d10.iterator();
            while (it.hasNext()) {
                arrayList.add((X509Certificate) ((Certificate) it.next()));
            }
            return arrayList;
        }
    }

    public RealConnection(RealConnectionPool realConnectionPool, d0 d0Var) {
        k.e(realConnectionPool, "connectionPool");
        k.e(d0Var, "route");
        this.f11210c = realConnectionPool;
        this.f11211d = d0Var;
        this.f11224q = 1;
        this.f11225r = new ArrayList();
        this.f11226s = Long.MAX_VALUE;
    }

    private final boolean A(List<d0> candidates) {
        if (!(candidates instanceof Collection) || !candidates.isEmpty()) {
            for (d0 d0Var : candidates) {
                if (d0Var.getF20561b().type() == Proxy.Type.DIRECT && this.f11211d.getF20561b().type() == Proxy.Type.DIRECT && k.a(this.f11211d.getF20562c(), d0Var.getF20562c())) {
                    return true;
                }
            }
        }
        return false;
    }

    private final void E(int i10) {
        Socket socket = this.f11213f;
        k.b(socket);
        BufferedSource bufferedSource = this.f11217j;
        k.b(bufferedSource);
        BufferedSink bufferedSink = this.f11218k;
        k.b(bufferedSink);
        socket.setSoTimeout(0);
        Http2Connection a10 = new Http2Connection.a(true, TaskRunner.f10944i).s(socket, this.f11211d.getF20560a().getF20493i().getF20716d(), bufferedSource, bufferedSink).k(this).l(i10).a();
        this.f11216i = a10;
        this.f11224q = Http2Connection.G.a().d();
        Http2Connection.X0(a10, false, null, 3, null);
    }

    private final boolean F(HttpUrl url) {
        Handshake handshake;
        if (ae.d.f244h && !Thread.holdsLock(this)) {
            throw new AssertionError("Thread " + ((Object) Thread.currentThread().getName()) + " MUST hold lock on " + this);
        }
        HttpUrl f20493i = this.f11211d.getF20560a().getF20493i();
        if (url.getF20717e() != f20493i.getF20717e()) {
            return false;
        }
        if (k.a(url.getF20716d(), f20493i.getF20716d())) {
            return true;
        }
        if (this.f11220m || (handshake = this.f11214g) == null) {
            return false;
        }
        k.b(handshake);
        return e(url, handshake);
    }

    private final boolean e(HttpUrl url, Handshake handshake) {
        List<Certificate> d10 = handshake.d();
        return (d10.isEmpty() ^ true) && OkHostnameVerifier.f14709a.e(url.getF20716d(), (X509Certificate) d10.get(0));
    }

    private final void h(int i10, int i11, zd.e eVar, EventListener eventListener) {
        Socket createSocket;
        Proxy f20561b = this.f11211d.getF20561b();
        Address f20560a = this.f11211d.getF20560a();
        Proxy.Type type = f20561b.type();
        int i12 = type == null ? -1 : b.f11227a[type.ordinal()];
        if (i12 != 1 && i12 != 2) {
            createSocket = new Socket(f20561b);
        } else {
            createSocket = f20560a.getF20486b().createSocket();
            k.b(createSocket);
        }
        this.f11212e = createSocket;
        eventListener.i(eVar, this.f11211d.getF20562c(), f20561b);
        createSocket.setSoTimeout(i11);
        try {
            Platform.f12870a.g().f(createSocket, this.f11211d.getF20562c(), i10);
            try {
                this.f11217j = n.b(n.f(createSocket));
                this.f11218k = n.a(n.d(createSocket));
            } catch (NullPointerException e10) {
                if (k.a(e10.getMessage(), "throw with null exception")) {
                    throw new IOException(e10);
                }
            }
        } catch (ConnectException e11) {
            ConnectException connectException = new ConnectException(k.l("Failed to connect to ", this.f11211d.getF20562c()));
            connectException.initCause(e11);
            throw connectException;
        }
    }

    private final void i(ConnectionSpecSelector connectionSpecSelector) {
        String h10;
        Address f20560a = this.f11211d.getF20560a();
        SSLSocketFactory f20487c = f20560a.getF20487c();
        SSLSocket sSLSocket = null;
        try {
            k.b(f20487c);
            Socket createSocket = f20487c.createSocket(this.f11212e, f20560a.getF20493i().getF20716d(), f20560a.getF20493i().getF20717e(), true);
            if (createSocket != null) {
                SSLSocket sSLSocket2 = (SSLSocket) createSocket;
                try {
                    ConnectionSpec a10 = connectionSpecSelector.a(sSLSocket2);
                    if (a10.getF20664b()) {
                        Platform.f12870a.g().e(sSLSocket2, f20560a.getF20493i().getF20716d(), f20560a.f());
                    }
                    sSLSocket2.startHandshake();
                    SSLSession session = sSLSocket2.getSession();
                    Handshake.a aVar = Handshake.f20701e;
                    k.d(session, "sslSocketSession");
                    Handshake a11 = aVar.a(session);
                    HostnameVerifier f20488d = f20560a.getF20488d();
                    k.b(f20488d);
                    if (!f20488d.verify(f20560a.getF20493i().getF20716d(), session)) {
                        List<Certificate> d10 = a11.d();
                        if (!d10.isEmpty()) {
                            X509Certificate x509Certificate = (X509Certificate) d10.get(0);
                            h10 = Indent.h("\n              |Hostname " + f20560a.getF20493i().getF20716d() + " not verified:\n              |    certificate: " + CertificatePinner.f20571c.a(x509Certificate) + "\n              |    DN: " + ((Object) x509Certificate.getSubjectDN().getName()) + "\n              |    subjectAltNames: " + OkHostnameVerifier.f14709a.a(x509Certificate) + "\n              ", null, 1, null);
                            throw new SSLPeerUnverifiedException(h10);
                        }
                        throw new SSLPeerUnverifiedException("Hostname " + f20560a.getF20493i().getF20716d() + " not verified (no certificates)");
                    }
                    CertificatePinner f20489e = f20560a.getF20489e();
                    k.b(f20489e);
                    this.f11214g = new Handshake(a11.getF20702a(), a11.getF20703b(), a11.c(), new c(f20489e, a11, f20560a));
                    f20489e.b(f20560a.getF20493i().getF20716d(), new d());
                    String g6 = a10.getF20664b() ? Platform.f12870a.g().g(sSLSocket2) : null;
                    this.f11213f = sSLSocket2;
                    this.f11217j = n.b(n.f(sSLSocket2));
                    this.f11218k = n.a(n.d(sSLSocket2));
                    this.f11215h = g6 != null ? Protocol.f20787f.a(g6) : Protocol.HTTP_1_1;
                    Platform.f12870a.g().b(sSLSocket2);
                    return;
                } catch (Throwable th) {
                    th = th;
                    sSLSocket = sSLSocket2;
                    if (sSLSocket != null) {
                        Platform.f12870a.g().b(sSLSocket);
                    }
                    if (sSLSocket != null) {
                        ae.d.m(sSLSocket);
                    }
                    throw th;
                }
            }
            throw new NullPointerException("null cannot be cast to non-null type javax.net.ssl.SSLSocket");
        } catch (Throwable th2) {
            th = th2;
        }
    }

    private final void j(int i10, int i11, int i12, zd.e eVar, EventListener eventListener) {
        z l10 = l();
        HttpUrl f20796a = l10.getF20796a();
        int i13 = 0;
        while (i13 < 21) {
            i13++;
            h(i10, i11, eVar, eventListener);
            l10 = k(i11, i12, l10, f20796a);
            if (l10 == null) {
                return;
            }
            Socket socket = this.f11212e;
            if (socket != null) {
                ae.d.m(socket);
            }
            this.f11212e = null;
            this.f11218k = null;
            this.f11217j = null;
            eventListener.g(eVar, this.f11211d.getF20562c(), this.f11211d.getF20561b(), null);
        }
    }

    private final z k(int readTimeout, int writeTimeout, z tunnelRequest, HttpUrl url) {
        boolean r10;
        String str = "CONNECT " + ae.d.P(url, true) + " HTTP/1.1";
        while (true) {
            BufferedSource bufferedSource = this.f11217j;
            k.b(bufferedSource);
            BufferedSink bufferedSink = this.f11218k;
            k.b(bufferedSink);
            Http1ExchangeCodec http1ExchangeCodec = new Http1ExchangeCodec(null, this, bufferedSource, bufferedSink);
            TimeUnit timeUnit = TimeUnit.MILLISECONDS;
            bufferedSource.timeout().g(readTimeout, timeUnit);
            bufferedSink.timeout().g(writeTimeout, timeUnit);
            http1ExchangeCodec.A(tunnelRequest.getF20798c(), str);
            http1ExchangeCodec.a();
            b0.a e10 = http1ExchangeCodec.e(false);
            k.b(e10);
            b0 c10 = e10.s(tunnelRequest).c();
            http1ExchangeCodec.z(c10);
            int code = c10.getCode();
            if (code == 200) {
                if (bufferedSource.a().s() && bufferedSink.a().s()) {
                    return null;
                }
                throw new IOException("TLS tunnel buffered too many bytes!");
            }
            if (code == 407) {
                z a10 = this.f11211d.getF20560a().getF20490f().a(this.f11211d, c10);
                if (a10 != null) {
                    r10 = StringsJVM.r("close", b0.S(c10, "Connection", null, 2, null), true);
                    if (r10) {
                        return a10;
                    }
                    tunnelRequest = a10;
                } else {
                    throw new IOException("Failed to authenticate with proxy");
                }
            } else {
                throw new IOException(k.l("Unexpected response code for CONNECT: ", Integer.valueOf(c10.getCode())));
            }
        }
    }

    private final z l() {
        z b10 = new z.a().n(this.f11211d.getF20560a().getF20493i()).f("CONNECT", null).d("Host", ae.d.P(this.f11211d.getF20560a().getF20493i(), true)).d("Proxy-Connection", "Keep-Alive").d("User-Agent", "okhttp/4.10.0").b();
        z a10 = this.f11211d.getF20560a().getF20490f().a(this.f11211d, new b0.a().s(b10).q(Protocol.HTTP_1_1).g(407).n("Preemptive Authenticate").b(ae.d.f239c).t(-1L).r(-1L).k("Proxy-Authenticate", "OkHttp-Preemptive").c());
        return a10 == null ? b10 : a10;
    }

    private final void m(ConnectionSpecSelector connectionSpecSelector, int i10, zd.e eVar, EventListener eventListener) {
        if (this.f11211d.getF20560a().getF20487c() == null) {
            List<Protocol> f10 = this.f11211d.getF20560a().f();
            Protocol protocol = Protocol.H2_PRIOR_KNOWLEDGE;
            if (f10.contains(protocol)) {
                this.f11213f = this.f11212e;
                this.f11215h = protocol;
                E(i10);
                return;
            } else {
                this.f11213f = this.f11212e;
                this.f11215h = Protocol.HTTP_1_1;
                return;
            }
        }
        eventListener.B(eVar);
        i(connectionSpecSelector);
        eventListener.A(eVar, this.f11214g);
        if (this.f11215h == Protocol.HTTP_2) {
            E(i10);
        }
    }

    public final void B(long j10) {
        this.f11226s = j10;
    }

    public final void C(boolean z10) {
        this.f11219l = z10;
    }

    public Socket D() {
        Socket socket = this.f11213f;
        k.b(socket);
        return socket;
    }

    public final synchronized void G(e call, IOException e10) {
        k.e(call, "call");
        if (e10 instanceof StreamResetException) {
            if (((StreamResetException) e10).f12469e == ErrorCode.REFUSED_STREAM) {
                int i10 = this.f11223p + 1;
                this.f11223p = i10;
                if (i10 > 1) {
                    this.f11219l = true;
                    this.f11221n++;
                }
            } else if (((StreamResetException) e10).f12469e != ErrorCode.CANCEL || !call.getF11201t()) {
                this.f11219l = true;
                this.f11221n++;
            }
        } else if (!v() || (e10 instanceof ConnectionShutdownException)) {
            this.f11219l = true;
            if (this.f11222o == 0) {
                if (e10 != null) {
                    g(call.getF11186e(), this.f11211d, e10);
                }
                this.f11221n++;
            }
        }
    }

    @Override // he.Http2Connection.c
    public synchronized void a(Http2Connection http2Connection, Settings settings) {
        k.e(http2Connection, "connection");
        k.e(settings, "settings");
        this.f11224q = settings.d();
    }

    @Override // he.Http2Connection.c
    public void b(Http2Stream http2Stream) {
        k.e(http2Stream, "stream");
        http2Stream.d(ErrorCode.REFUSED_STREAM, null);
    }

    public final void d() {
        Socket socket = this.f11212e;
        if (socket == null) {
            return;
        }
        ae.d.m(socket);
    }

    /* JADX WARN: Removed duplicated region for block: B:37:0x0109  */
    /* JADX WARN: Removed duplicated region for block: B:40:0x0111  */
    /* JADX WARN: Removed duplicated region for block: B:43:0x013b  */
    /* JADX WARN: Removed duplicated region for block: B:45:0x0146  */
    /* JADX WARN: Removed duplicated region for block: B:50:0x014e A[SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:52:0x0141  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public final void f(int i10, int i11, int i12, int i13, boolean z10, zd.e eVar, EventListener eventListener) {
        Socket socket;
        Socket socket2;
        k.e(eVar, "call");
        k.e(eventListener, "eventListener");
        if (this.f11215h == null) {
            List<ConnectionSpec> b10 = this.f11211d.getF20560a().b();
            ConnectionSpecSelector connectionSpecSelector = new ConnectionSpecSelector(b10);
            if (this.f11211d.getF20560a().getF20487c() == null) {
                if (b10.contains(ConnectionSpec.f20662k)) {
                    String f20716d = this.f11211d.getF20560a().getF20493i().getF20716d();
                    if (!Platform.f12870a.g().i(f20716d)) {
                        throw new RouteException(new UnknownServiceException("CLEARTEXT communication to " + f20716d + " not permitted by network security policy"));
                    }
                } else {
                    throw new RouteException(new UnknownServiceException("CLEARTEXT communication not enabled for client"));
                }
            } else if (this.f11211d.getF20560a().f().contains(Protocol.H2_PRIOR_KNOWLEDGE)) {
                throw new RouteException(new UnknownServiceException("H2_PRIOR_KNOWLEDGE cannot be used with HTTPS"));
            }
            RouteException routeException = null;
            do {
                try {
                } catch (IOException e10) {
                    e = e10;
                }
                try {
                    if (this.f11211d.c()) {
                        j(i10, i11, i12, eVar, eventListener);
                        if (this.f11212e == null) {
                            if (!this.f11211d.c() && this.f11212e == null) {
                                throw new RouteException(new ProtocolException("Too many tunnel connections attempted: 21"));
                            }
                            this.f11226s = System.nanoTime();
                            return;
                        }
                    } else {
                        try {
                            h(i10, i11, eVar, eventListener);
                        } catch (IOException e11) {
                            e = e11;
                            socket = this.f11213f;
                            if (socket != null) {
                            }
                            socket2 = this.f11212e;
                            if (socket2 != null) {
                            }
                            this.f11213f = null;
                            this.f11212e = null;
                            this.f11217j = null;
                            this.f11218k = null;
                            this.f11214g = null;
                            this.f11215h = null;
                            this.f11216i = null;
                            this.f11224q = 1;
                            eventListener.h(eVar, this.f11211d.getF20562c(), this.f11211d.getF20561b(), null, e);
                            if (routeException != null) {
                            }
                            if (z10) {
                            }
                        }
                    }
                    m(connectionSpecSelector, i13, eVar, eventListener);
                    eventListener.g(eVar, this.f11211d.getF20562c(), this.f11211d.getF20561b(), this.f11215h);
                    if (!this.f11211d.c()) {
                    }
                    this.f11226s = System.nanoTime();
                    return;
                } catch (IOException e12) {
                    e = e12;
                    socket = this.f11213f;
                    if (socket != null) {
                        ae.d.m(socket);
                    }
                    socket2 = this.f11212e;
                    if (socket2 != null) {
                        ae.d.m(socket2);
                    }
                    this.f11213f = null;
                    this.f11212e = null;
                    this.f11217j = null;
                    this.f11218k = null;
                    this.f11214g = null;
                    this.f11215h = null;
                    this.f11216i = null;
                    this.f11224q = 1;
                    eventListener.h(eVar, this.f11211d.getF20562c(), this.f11211d.getF20561b(), null, e);
                    if (routeException != null) {
                        routeException = new RouteException(e);
                    } else {
                        routeException.a(e);
                    }
                    if (z10) {
                        throw routeException;
                    }
                }
            } while (connectionSpecSelector.b(e));
            throw routeException;
        }
        throw new IllegalStateException("already connected".toString());
    }

    public final void g(OkHttpClient client, d0 failedRoute, IOException failure) {
        k.e(client, "client");
        k.e(failedRoute, "failedRoute");
        k.e(failure, "failure");
        if (failedRoute.getF20561b().type() != Proxy.Type.DIRECT) {
            Address f20560a = failedRoute.getF20560a();
            f20560a.getF20492h().connectFailed(f20560a.getF20493i().q(), failedRoute.getF20561b().address(), failure);
        }
        client.getG().b(failedRoute);
    }

    public final List<Reference<e>> n() {
        return this.f11225r;
    }

    /* renamed from: o, reason: from getter */
    public final long getF11226s() {
        return this.f11226s;
    }

    /* renamed from: p, reason: from getter */
    public final boolean getF11219l() {
        return this.f11219l;
    }

    /* renamed from: q, reason: from getter */
    public final int getF11221n() {
        return this.f11221n;
    }

    /* renamed from: r, reason: from getter */
    public Handshake getF11214g() {
        return this.f11214g;
    }

    public final synchronized void s() {
        this.f11222o++;
    }

    public final boolean t(Address address, List<d0> routes) {
        k.e(address, "address");
        if (ae.d.f244h && !Thread.holdsLock(this)) {
            throw new AssertionError("Thread " + ((Object) Thread.currentThread().getName()) + " MUST hold lock on " + this);
        }
        if (this.f11225r.size() >= this.f11224q || this.f11219l || !this.f11211d.getF20560a().d(address)) {
            return false;
        }
        if (k.a(address.getF20493i().getF20716d(), getF11211d().getF20560a().getF20493i().getF20716d())) {
            return true;
        }
        if (this.f11216i == null || routes == null || !A(routes) || address.getF20488d() != OkHostnameVerifier.f14709a || !F(address.getF20493i())) {
            return false;
        }
        try {
            CertificatePinner f20489e = address.getF20489e();
            k.b(f20489e);
            String f20716d = address.getF20493i().getF20716d();
            Handshake f11214g = getF11214g();
            k.b(f11214g);
            f20489e.a(f20716d, f11214g.d());
            return true;
        } catch (SSLPeerUnverifiedException unused) {
            return false;
        }
    }

    public String toString() {
        CipherSuite f20703b;
        StringBuilder sb2 = new StringBuilder();
        sb2.append("Connection{");
        sb2.append(this.f11211d.getF20560a().getF20493i().getF20716d());
        sb2.append(COUIAccessibilityUtil.ENABLED_ACCESSIBILITY_SERVICES_SEPARATOR);
        sb2.append(this.f11211d.getF20560a().getF20493i().getF20717e());
        sb2.append(", proxy=");
        sb2.append(this.f11211d.getF20561b());
        sb2.append(" hostAddress=");
        sb2.append(this.f11211d.getF20562c());
        sb2.append(" cipherSuite=");
        Handshake handshake = this.f11214g;
        Object obj = "none";
        if (handshake != null && (f20703b = handshake.getF20703b()) != null) {
            obj = f20703b;
        }
        sb2.append(obj);
        sb2.append(" protocol=");
        sb2.append(this.f11215h);
        sb2.append('}');
        return sb2.toString();
    }

    public final boolean u(boolean doExtensiveChecks) {
        long f11226s;
        if (ae.d.f244h && Thread.holdsLock(this)) {
            throw new AssertionError("Thread " + ((Object) Thread.currentThread().getName()) + " MUST NOT hold lock on " + this);
        }
        long nanoTime = System.nanoTime();
        Socket socket = this.f11212e;
        k.b(socket);
        Socket socket2 = this.f11213f;
        k.b(socket2);
        BufferedSource bufferedSource = this.f11217j;
        k.b(bufferedSource);
        if (socket.isClosed() || socket2.isClosed() || socket2.isInputShutdown() || socket2.isOutputShutdown()) {
            return false;
        }
        Http2Connection http2Connection = this.f11216i;
        if (http2Connection != null) {
            return http2Connection.I0(nanoTime);
        }
        synchronized (this) {
            f11226s = nanoTime - getF11226s();
        }
        if (f11226s < 10000000000L || !doExtensiveChecks) {
            return true;
        }
        return ae.d.E(socket2, bufferedSource);
    }

    public final boolean v() {
        return this.f11216i != null;
    }

    public final ExchangeCodec w(OkHttpClient client, fe.g chain) {
        k.e(client, "client");
        k.e(chain, "chain");
        Socket socket = this.f11213f;
        k.b(socket);
        BufferedSource bufferedSource = this.f11217j;
        k.b(bufferedSource);
        BufferedSink bufferedSink = this.f11218k;
        k.b(bufferedSink);
        Http2Connection http2Connection = this.f11216i;
        if (http2Connection != null) {
            return new Http2ExchangeCodec(client, this, chain, http2Connection);
        }
        socket.setSoTimeout(chain.j());
        Timeout timeout = bufferedSource.timeout();
        long f11467g = chain.getF11467g();
        TimeUnit timeUnit = TimeUnit.MILLISECONDS;
        timeout.g(f11467g, timeUnit);
        bufferedSink.timeout().g(chain.getF11468h(), timeUnit);
        return new Http1ExchangeCodec(client, this, bufferedSource, bufferedSink);
    }

    public final synchronized void x() {
        this.f11220m = true;
    }

    public final synchronized void y() {
        this.f11219l = true;
    }

    /* renamed from: z, reason: from getter */
    public d0 getF11211d() {
        return this.f11211d;
    }
}
