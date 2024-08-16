package ie;

import java.security.KeyStore;
import java.security.Provider;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;
import kotlin.Metadata;
import org.conscrypt.Conscrypt;
import org.conscrypt.ConscryptHostnameVerifier;
import za.DefaultConstructorMarker;
import za.k;
import zd.Protocol;

/* compiled from: ConscryptPlatform.kt */
@Metadata(bv = {}, d1 = {"\u0000:\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0006\u0018\u00002\u00020\u0001:\u0002\u0015\u0016B\t\b\u0002¢\u0006\u0004\b\u0013\u0010\u0014J\b\u0010\u0003\u001a\u00020\u0002H\u0016J\b\u0010\u0005\u001a\u00020\u0004H\u0016J(\u0010\u000e\u001a\u00020\r2\u0006\u0010\u0007\u001a\u00020\u00062\b\u0010\t\u001a\u0004\u0018\u00010\b2\f\u0010\f\u001a\b\u0012\u0004\u0012\u00020\u000b0\nH\u0016J\u0012\u0010\u000f\u001a\u0004\u0018\u00010\b2\u0006\u0010\u0007\u001a\u00020\u0006H\u0016J\u0010\u0010\u0012\u001a\u00020\u00112\u0006\u0010\u0010\u001a\u00020\u0004H\u0016¨\u0006\u0017"}, d2 = {"Lie/d;", "Lie/h;", "Ljavax/net/ssl/SSLContext;", "m", "Ljavax/net/ssl/X509TrustManager;", "o", "Ljavax/net/ssl/SSLSocket;", "sslSocket", "", "hostname", "", "Lzd/y;", "protocols", "Lma/f0;", "e", "g", "trustManager", "Ljavax/net/ssl/SSLSocketFactory;", "n", "<init>", "()V", "a", "b", "okhttp"}, k = 1, mv = {1, 6, 0})
/* renamed from: ie.d, reason: use source file name */
/* loaded from: classes2.dex */
public final class ConscryptPlatform extends Platform {

    /* renamed from: e, reason: collision with root package name */
    public static final a f12852e;

    /* renamed from: f, reason: collision with root package name */
    private static final boolean f12853f;

    /* renamed from: d, reason: collision with root package name */
    private final Provider f12854d;

    /* compiled from: ConscryptPlatform.kt */
    @Metadata(bv = {}, d1 = {"\u0000\u001e\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0002\b\b\b\u0086\u0003\u0018\u00002\u00020\u0001B\t\b\u0002¢\u0006\u0004\b\u000e\u0010\u000fJ\b\u0010\u0003\u001a\u0004\u0018\u00010\u0002J\"\u0010\t\u001a\u00020\b2\u0006\u0010\u0005\u001a\u00020\u00042\b\b\u0002\u0010\u0006\u001a\u00020\u00042\b\b\u0002\u0010\u0007\u001a\u00020\u0004R\u0017\u0010\n\u001a\u00020\b8\u0006¢\u0006\f\n\u0004\b\n\u0010\u000b\u001a\u0004\b\f\u0010\r¨\u0006\u0010"}, d2 = {"Lie/d$a;", "", "Lie/d;", "b", "", "major", "minor", "patch", "", "a", "isSupported", "Z", "c", "()Z", "<init>", "()V", "okhttp"}, k = 1, mv = {1, 6, 0})
    /* renamed from: ie.d$a */
    /* loaded from: classes2.dex */
    public static final class a {
        private a() {
        }

        public /* synthetic */ a(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        public final boolean a(int major, int minor, int patch) {
            Conscrypt.Version version = Conscrypt.version();
            return version.major() != major ? version.major() > major : version.minor() != minor ? version.minor() > minor : version.patch() >= patch;
        }

        public final ConscryptPlatform b() {
            DefaultConstructorMarker defaultConstructorMarker = null;
            if (c()) {
                return new ConscryptPlatform(defaultConstructorMarker);
            }
            return null;
        }

        public final boolean c() {
            return ConscryptPlatform.f12853f;
        }
    }

    /* compiled from: ConscryptPlatform.kt */
    @Metadata(bv = {}, d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\bÀ\u0002\u0018\u00002\u00020\u0001B\t\b\u0002¢\u0006\u0004\b\u0002\u0010\u0003¨\u0006\u0004"}, d2 = {"Lie/d$b;", "Lorg/conscrypt/ConscryptHostnameVerifier;", "<init>", "()V", "okhttp"}, k = 1, mv = {1, 6, 0})
    /* renamed from: ie.d$b */
    /* loaded from: classes2.dex */
    public static final class b implements ConscryptHostnameVerifier {

        /* renamed from: a, reason: collision with root package name */
        public static final b f12855a = new b();

        private b() {
        }
    }

    static {
        a aVar = new a(null);
        f12852e = aVar;
        boolean z10 = false;
        try {
            Class.forName("org.conscrypt.Conscrypt$Version", false, aVar.getClass().getClassLoader());
            if (Conscrypt.isAvailable()) {
                if (aVar.a(2, 1, 0)) {
                    z10 = true;
                }
            }
        } catch (ClassNotFoundException | NoClassDefFoundError unused) {
        }
        f12853f = z10;
    }

    private ConscryptPlatform() {
        Provider newProvider = Conscrypt.newProvider();
        k.d(newProvider, "newProvider()");
        this.f12854d = newProvider;
    }

    public /* synthetic */ ConscryptPlatform(DefaultConstructorMarker defaultConstructorMarker) {
        this();
    }

    @Override // ie.Platform
    public void e(SSLSocket sSLSocket, String str, List<Protocol> list) {
        k.e(sSLSocket, "sslSocket");
        k.e(list, "protocols");
        if (Conscrypt.isConscrypt(sSLSocket)) {
            Conscrypt.setUseSessionTickets(sSLSocket, true);
            Object[] array = Platform.f12870a.b(list).toArray(new String[0]);
            Objects.requireNonNull(array, "null cannot be cast to non-null type kotlin.Array<T of kotlin.collections.ArraysKt__ArraysJVMKt.toTypedArray>");
            Conscrypt.setApplicationProtocols(sSLSocket, (String[]) array);
            return;
        }
        super.e(sSLSocket, str, list);
    }

    @Override // ie.Platform
    public String g(SSLSocket sslSocket) {
        k.e(sslSocket, "sslSocket");
        if (Conscrypt.isConscrypt(sslSocket)) {
            return Conscrypt.getApplicationProtocol(sslSocket);
        }
        return super.g(sslSocket);
    }

    @Override // ie.Platform
    public SSLContext m() {
        SSLContext sSLContext = SSLContext.getInstance("TLS", this.f12854d);
        k.d(sSLContext, "getInstance(\"TLS\", provider)");
        return sSLContext;
    }

    @Override // ie.Platform
    public SSLSocketFactory n(X509TrustManager trustManager) {
        k.e(trustManager, "trustManager");
        SSLContext m10 = m();
        m10.init(null, new TrustManager[]{trustManager}, null);
        SSLSocketFactory socketFactory = m10.getSocketFactory();
        k.d(socketFactory, "newSSLContext().apply {\n…null)\n    }.socketFactory");
        return socketFactory;
    }

    @Override // ie.Platform
    public X509TrustManager o() {
        TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
        trustManagerFactory.init((KeyStore) null);
        TrustManager[] trustManagers = trustManagerFactory.getTrustManagers();
        k.b(trustManagers);
        if (trustManagers.length == 1 && (trustManagers[0] instanceof X509TrustManager)) {
            TrustManager trustManager = trustManagers[0];
            Objects.requireNonNull(trustManager, "null cannot be cast to non-null type javax.net.ssl.X509TrustManager");
            X509TrustManager x509TrustManager = (X509TrustManager) trustManager;
            Conscrypt.setHostnameVerifier(x509TrustManager, b.f12855a);
            return x509TrustManager;
        }
        String arrays = Arrays.toString(trustManagers);
        k.d(arrays, "toString(this)");
        throw new IllegalStateException(k.l("Unexpected default trust managers: ", arrays).toString());
    }
}
