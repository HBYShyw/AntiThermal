package ie;

import java.net.InetSocketAddress;
import java.net.Socket;
import java.security.GeneralSecurityException;
import java.security.KeyStore;
import java.security.Security;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;
import kotlin.Metadata;
import kotlin.collections.s;
import le.BasicCertificateChainCleaner;
import le.BasicTrustRootIndex;
import le.CertificateChainCleaner;
import le.TrustRootIndex;
import za.DefaultConstructorMarker;
import za.k;
import zd.OkHttpClient;
import zd.Protocol;

/* compiled from: Platform.kt */
@Metadata(bv = {}, d1 = {"\u0000j\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0004\n\u0002\u0010\u0003\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0006\b\u0016\u0018\u00002\u00020\u0001:\u0001-B\u0007¢\u0006\u0004\b+\u0010,J\b\u0010\u0003\u001a\u00020\u0002H\u0016J\b\u0010\u0005\u001a\u00020\u0004H\u0016J(\u0010\u000e\u001a\u00020\r2\u0006\u0010\u0007\u001a\u00020\u00062\b\u0010\t\u001a\u0004\u0018\u00010\b2\f\u0010\f\u001a\b\u0012\u0004\u0012\u00020\u000b0\nH\u0016J\u0010\u0010\u000f\u001a\u00020\r2\u0006\u0010\u0007\u001a\u00020\u0006H\u0016J\u0012\u0010\u0010\u001a\u0004\u0018\u00010\b2\u0006\u0010\u0007\u001a\u00020\u0006H\u0016J \u0010\u0017\u001a\u00020\r2\u0006\u0010\u0012\u001a\u00020\u00112\u0006\u0010\u0014\u001a\u00020\u00132\u0006\u0010\u0016\u001a\u00020\u0015H\u0016J&\u0010\u001c\u001a\u00020\r2\u0006\u0010\u0018\u001a\u00020\b2\b\b\u0002\u0010\u0019\u001a\u00020\u00152\n\b\u0002\u0010\u001b\u001a\u0004\u0018\u00010\u001aH\u0016J\u0010\u0010\u001e\u001a\u00020\u001d2\u0006\u0010\t\u001a\u00020\bH\u0016J\u0012\u0010 \u001a\u0004\u0018\u00010\u00012\u0006\u0010\u001f\u001a\u00020\bH\u0016J\u001a\u0010\"\u001a\u00020\r2\u0006\u0010\u0018\u001a\u00020\b2\b\u0010!\u001a\u0004\u0018\u00010\u0001H\u0016J\u0010\u0010%\u001a\u00020$2\u0006\u0010#\u001a\u00020\u0004H\u0016J\u0010\u0010'\u001a\u00020&2\u0006\u0010#\u001a\u00020\u0004H\u0016J\u0010\u0010)\u001a\u00020(2\u0006\u0010#\u001a\u00020\u0004H\u0016J\b\u0010*\u001a\u00020\bH\u0016¨\u0006."}, d2 = {"Lie/h;", "", "Ljavax/net/ssl/SSLContext;", "m", "Ljavax/net/ssl/X509TrustManager;", "o", "Ljavax/net/ssl/SSLSocket;", "sslSocket", "", "hostname", "", "Lzd/y;", "protocols", "Lma/f0;", "e", "b", "g", "Ljava/net/Socket;", "socket", "Ljava/net/InetSocketAddress;", "address", "", "connectTimeout", "f", "message", "level", "", "t", "j", "", "i", "closer", "h", "stackTrace", "l", "trustManager", "Lle/c;", "c", "Lle/e;", "d", "Ljavax/net/ssl/SSLSocketFactory;", "n", "toString", "<init>", "()V", "a", "okhttp"}, k = 1, mv = {1, 6, 0})
/* renamed from: ie.h, reason: use source file name */
/* loaded from: classes2.dex */
public class Platform {

    /* renamed from: a, reason: collision with root package name */
    public static final a f12870a;

    /* renamed from: b, reason: collision with root package name */
    private static volatile Platform f12871b;

    /* renamed from: c, reason: collision with root package name */
    private static final Logger f12872c;

    /* compiled from: Platform.kt */
    @Metadata(bv = {}, d1 = {"\u0000>\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\u0012\n\u0000\n\u0002\u0010\u000b\n\u0002\b\t\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\b\b\u0086\u0003\u0018\u00002\u00020\u0001B\t\b\u0002¢\u0006\u0004\b\"\u0010#J\b\u0010\u0003\u001a\u00020\u0002H\u0002J\b\u0010\u0004\u001a\u00020\u0002H\u0002J\b\u0010\u0005\u001a\u00020\u0002H\u0002J\b\u0010\u0006\u001a\u00020\u0002H\u0007J\u001a\u0010\u000b\u001a\b\u0012\u0004\u0012\u00020\n0\u00072\f\u0010\t\u001a\b\u0012\u0004\u0012\u00020\b0\u0007J\u0014\u0010\r\u001a\u00020\f2\f\u0010\t\u001a\b\u0012\u0004\u0012\u00020\b0\u0007R\u0014\u0010\u0011\u001a\u00020\u000e8BX\u0082\u0004¢\u0006\u0006\u001a\u0004\b\u000f\u0010\u0010R\u0014\u0010\u0013\u001a\u00020\u000e8BX\u0082\u0004¢\u0006\u0006\u001a\u0004\b\u0012\u0010\u0010R\u0014\u0010\u0015\u001a\u00020\u000e8BX\u0082\u0004¢\u0006\u0006\u001a\u0004\b\u0014\u0010\u0010R\u0011\u0010\u0017\u001a\u00020\u000e8F¢\u0006\u0006\u001a\u0004\b\u0016\u0010\u0010R\u0014\u0010\u0019\u001a\u00020\u00188\u0006X\u0086T¢\u0006\u0006\n\u0004\b\u0019\u0010\u001aR\u0014\u0010\u001b\u001a\u00020\u00188\u0006X\u0086T¢\u0006\u0006\n\u0004\b\u001b\u0010\u001aR\u001c\u0010\u001e\u001a\n \u001d*\u0004\u0018\u00010\u001c0\u001c8\u0002X\u0082\u0004¢\u0006\u0006\n\u0004\b\u001e\u0010\u001fR\u0016\u0010 \u001a\u00020\u00028\u0002@\u0002X\u0082\u000e¢\u0006\u0006\n\u0004\b \u0010!¨\u0006$"}, d2 = {"Lie/h$a;", "", "Lie/h;", "f", "d", "e", "g", "", "Lzd/y;", "protocols", "", "b", "", "c", "", "j", "()Z", "isConscryptPreferred", "k", "isOpenJSSEPreferred", "i", "isBouncyCastlePreferred", "h", "isAndroid", "", "INFO", "I", "WARN", "Ljava/util/logging/Logger;", "kotlin.jvm.PlatformType", "logger", "Ljava/util/logging/Logger;", "platform", "Lie/h;", "<init>", "()V", "okhttp"}, k = 1, mv = {1, 6, 0})
    /* renamed from: ie.h$a */
    /* loaded from: classes2.dex */
    public static final class a {
        private a() {
        }

        public /* synthetic */ a(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private final Platform d() {
            je.c.f13910a.b();
            Platform a10 = Android10Platform.f12840e.a();
            if (a10 != null) {
                return a10;
            }
            Platform a11 = AndroidPlatform.f12843f.a();
            k.b(a11);
            return a11;
        }

        private final Platform e() {
            OpenJSSEPlatform a10;
            BouncyCastlePlatform a11;
            ConscryptPlatform b10;
            if (j() && (b10 = ConscryptPlatform.f12852e.b()) != null) {
                return b10;
            }
            if (i() && (a11 = BouncyCastlePlatform.f12849e.a()) != null) {
                return a11;
            }
            if (k() && (a10 = OpenJSSEPlatform.f12867e.a()) != null) {
                return a10;
            }
            Jdk9Platform a12 = Jdk9Platform.f12865d.a();
            if (a12 != null) {
                return a12;
            }
            Platform a13 = Jdk8WithJettyBootPlatform.f12856i.a();
            return a13 != null ? a13 : new Platform();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public final Platform f() {
            if (h()) {
                return d();
            }
            return e();
        }

        private final boolean i() {
            return k.a("BC", Security.getProviders()[0].getName());
        }

        private final boolean j() {
            return k.a("Conscrypt", Security.getProviders()[0].getName());
        }

        private final boolean k() {
            return k.a("OpenJSSE", Security.getProviders()[0].getName());
        }

        public final List<String> b(List<? extends Protocol> protocols) {
            int u7;
            k.e(protocols, "protocols");
            ArrayList arrayList = new ArrayList();
            for (Object obj : protocols) {
                if (((Protocol) obj) != Protocol.HTTP_1_0) {
                    arrayList.add(obj);
                }
            }
            u7 = s.u(arrayList, 10);
            ArrayList arrayList2 = new ArrayList(u7);
            Iterator it = arrayList.iterator();
            while (it.hasNext()) {
                arrayList2.add(((Protocol) it.next()).getF20795e());
            }
            return arrayList2;
        }

        public final byte[] c(List<? extends Protocol> protocols) {
            k.e(protocols, "protocols");
            me.d dVar = new me.d();
            for (String str : b(protocols)) {
                dVar.t(str.length());
                dVar.E(str);
            }
            return dVar.a0();
        }

        public final Platform g() {
            return Platform.f12871b;
        }

        public final boolean h() {
            return k.a("Dalvik", System.getProperty("java.vm.name"));
        }
    }

    static {
        a aVar = new a(null);
        f12870a = aVar;
        f12871b = aVar.f();
        f12872c = Logger.getLogger(OkHttpClient.class.getName());
    }

    public static /* synthetic */ void k(Platform platform, String str, int i10, Throwable th, int i11, Object obj) {
        if (obj != null) {
            throw new UnsupportedOperationException("Super calls with default arguments not supported in this target, function: log");
        }
        if ((i11 & 2) != 0) {
            i10 = 4;
        }
        if ((i11 & 4) != 0) {
            th = null;
        }
        platform.j(str, i10, th);
    }

    public void b(SSLSocket sSLSocket) {
        k.e(sSLSocket, "sslSocket");
    }

    public CertificateChainCleaner c(X509TrustManager trustManager) {
        k.e(trustManager, "trustManager");
        return new BasicCertificateChainCleaner(d(trustManager));
    }

    public TrustRootIndex d(X509TrustManager trustManager) {
        k.e(trustManager, "trustManager");
        X509Certificate[] acceptedIssuers = trustManager.getAcceptedIssuers();
        k.d(acceptedIssuers, "trustManager.acceptedIssuers");
        return new BasicTrustRootIndex((X509Certificate[]) Arrays.copyOf(acceptedIssuers, acceptedIssuers.length));
    }

    public void e(SSLSocket sSLSocket, String str, List<Protocol> list) {
        k.e(sSLSocket, "sslSocket");
        k.e(list, "protocols");
    }

    public void f(Socket socket, InetSocketAddress inetSocketAddress, int i10) {
        k.e(socket, "socket");
        k.e(inetSocketAddress, "address");
        socket.connect(inetSocketAddress, i10);
    }

    public String g(SSLSocket sslSocket) {
        k.e(sslSocket, "sslSocket");
        return null;
    }

    public Object h(String closer) {
        k.e(closer, "closer");
        if (f12872c.isLoggable(Level.FINE)) {
            return new Throwable(closer);
        }
        return null;
    }

    public boolean i(String hostname) {
        k.e(hostname, "hostname");
        return true;
    }

    public void j(String str, int i10, Throwable th) {
        k.e(str, "message");
        f12872c.log(i10 == 5 ? Level.WARNING : Level.INFO, str, th);
    }

    public void l(String str, Object obj) {
        k.e(str, "message");
        if (obj == null) {
            str = k.l(str, " To see where this was allocated, set the OkHttpClient logger level to FINE: Logger.getLogger(OkHttpClient.class.getName()).setLevel(Level.FINE);");
        }
        j(str, 5, (Throwable) obj);
    }

    public SSLContext m() {
        SSLContext sSLContext = SSLContext.getInstance("TLS");
        k.d(sSLContext, "getInstance(\"TLS\")");
        return sSLContext;
    }

    public SSLSocketFactory n(X509TrustManager trustManager) {
        k.e(trustManager, "trustManager");
        try {
            SSLContext m10 = m();
            m10.init(null, new TrustManager[]{trustManager}, null);
            SSLSocketFactory socketFactory = m10.getSocketFactory();
            k.d(socketFactory, "newSSLContext().apply {\n…ll)\n      }.socketFactory");
            return socketFactory;
        } catch (GeneralSecurityException e10) {
            throw new AssertionError(k.l("No System TLS: ", e10), e10);
        }
    }

    public X509TrustManager o() {
        TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
        trustManagerFactory.init((KeyStore) null);
        TrustManager[] trustManagers = trustManagerFactory.getTrustManagers();
        k.b(trustManagers);
        if (trustManagers.length == 1 && (trustManagers[0] instanceof X509TrustManager)) {
            TrustManager trustManager = trustManagers[0];
            Objects.requireNonNull(trustManager, "null cannot be cast to non-null type javax.net.ssl.X509TrustManager");
            return (X509TrustManager) trustManager;
        }
        String arrays = Arrays.toString(trustManagers);
        k.d(arrays, "toString(this)");
        throw new IllegalStateException(k.l("Unexpected default trust managers: ", arrays).toString());
    }

    public String toString() {
        String simpleName = getClass().getSimpleName();
        k.d(simpleName, "javaClass.simpleName");
        return simpleName;
    }
}
