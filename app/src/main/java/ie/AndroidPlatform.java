package ie;

import android.security.NetworkSecurityPolicy;
import com.oplus.backup.sdk.common.utils.Constants;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.security.cert.TrustAnchor;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.X509TrustManager;
import je.AndroidCertificateChainCleaner;
import je.AndroidSocketAdapter;
import je.BouncyCastleSocketAdapter;
import je.CloseGuard;
import je.ConscryptSocketAdapter;
import je.DeferredSocketAdapter;
import je.SocketAdapter;
import je.StandardAndroidSocketAdapter;
import kotlin.Metadata;
import kotlin.collections.r;
import le.CertificateChainCleaner;
import le.TrustRootIndex;
import za.DefaultConstructorMarker;
import za.k;
import zd.Protocol;

/* compiled from: AndroidPlatform.kt */
@Metadata(bv = {}, d1 = {"\u0000Z\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\u0000\n\u0002\b\u0004\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0006\b\u0007\u0018\u00002\u00020\u0001:\u0002#$B\u0007¢\u0006\u0004\b!\u0010\"J \u0010\t\u001a\u00020\b2\u0006\u0010\u0003\u001a\u00020\u00022\u0006\u0010\u0005\u001a\u00020\u00042\u0006\u0010\u0007\u001a\u00020\u0006H\u0016J(\u0010\u0011\u001a\u00020\b2\u0006\u0010\u000b\u001a\u00020\n2\b\u0010\r\u001a\u0004\u0018\u00010\f2\f\u0010\u0010\u001a\b\u0012\u0004\u0012\u00020\u000f0\u000eH\u0016J\u0012\u0010\u0012\u001a\u0004\u0018\u00010\f2\u0006\u0010\u000b\u001a\u00020\nH\u0016J\u0012\u0010\u0015\u001a\u0004\u0018\u00010\u00142\u0006\u0010\u0013\u001a\u00020\fH\u0016J\u001a\u0010\u0018\u001a\u00020\b2\u0006\u0010\u0016\u001a\u00020\f2\b\u0010\u0017\u001a\u0004\u0018\u00010\u0014H\u0016J\u0010\u0010\u001a\u001a\u00020\u00192\u0006\u0010\r\u001a\u00020\fH\u0016J\u0010\u0010\u001e\u001a\u00020\u001d2\u0006\u0010\u001c\u001a\u00020\u001bH\u0016J\u0010\u0010 \u001a\u00020\u001f2\u0006\u0010\u001c\u001a\u00020\u001bH\u0016¨\u0006%"}, d2 = {"Lie/b;", "Lie/h;", "Ljava/net/Socket;", "socket", "Ljava/net/InetSocketAddress;", "address", "", "connectTimeout", "Lma/f0;", "f", "Ljavax/net/ssl/SSLSocket;", "sslSocket", "", "hostname", "", "Lzd/y;", "protocols", "e", "g", "closer", "", "h", "message", "stackTrace", "l", "", "i", "Ljavax/net/ssl/X509TrustManager;", "trustManager", "Lle/c;", "c", "Lle/e;", "d", "<init>", "()V", "a", "b", "okhttp"}, k = 1, mv = {1, 6, 0})
/* renamed from: ie.b, reason: use source file name */
/* loaded from: classes2.dex */
public final class AndroidPlatform extends Platform {

    /* renamed from: f, reason: collision with root package name */
    public static final a f12843f = new a(null);

    /* renamed from: g, reason: collision with root package name */
    private static final boolean f12844g;

    /* renamed from: d, reason: collision with root package name */
    private final List<SocketAdapter> f12845d;

    /* renamed from: e, reason: collision with root package name */
    private final CloseGuard f12846e;

    /* compiled from: AndroidPlatform.kt */
    @Metadata(bv = {}, d1 = {"\u0000\u0016\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0007\b\u0086\u0003\u0018\u00002\u00020\u0001B\t\b\u0002¢\u0006\u0004\b\t\u0010\nJ\b\u0010\u0003\u001a\u0004\u0018\u00010\u0002R\u0017\u0010\u0005\u001a\u00020\u00048\u0006¢\u0006\f\n\u0004\b\u0005\u0010\u0006\u001a\u0004\b\u0007\u0010\b¨\u0006\u000b"}, d2 = {"Lie/b$a;", "", "Lie/h;", "a", "", "isSupported", "Z", "b", "()Z", "<init>", "()V", "okhttp"}, k = 1, mv = {1, 6, 0})
    /* renamed from: ie.b$a */
    /* loaded from: classes2.dex */
    public static final class a {
        private a() {
        }

        public /* synthetic */ a(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        public final Platform a() {
            if (b()) {
                return new AndroidPlatform();
            }
            return null;
        }

        public final boolean b() {
            return AndroidPlatform.f12844g;
        }
    }

    /* compiled from: AndroidPlatform.kt */
    @Metadata(bv = {}, d1 = {"\u00006\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\b\u0080\b\u0018\u00002\u00020\u0001B\u0017\u0012\u0006\u0010\u000e\u001a\u00020\r\u0012\u0006\u0010\u0010\u001a\u00020\u000f¢\u0006\u0004\b\u0011\u0010\u0012J\u0012\u0010\u0004\u001a\u0004\u0018\u00010\u00022\u0006\u0010\u0003\u001a\u00020\u0002H\u0016J\t\u0010\u0006\u001a\u00020\u0005HÖ\u0001J\t\u0010\b\u001a\u00020\u0007HÖ\u0001J\u0013\u0010\f\u001a\u00020\u000b2\b\u0010\n\u001a\u0004\u0018\u00010\tHÖ\u0003¨\u0006\u0013"}, d2 = {"Lie/b$b;", "Lle/e;", "Ljava/security/cert/X509Certificate;", "cert", "a", "", "toString", "", "hashCode", "", "other", "", "equals", "Ljavax/net/ssl/X509TrustManager;", "trustManager", "Ljava/lang/reflect/Method;", "findByIssuerAndSignatureMethod", "<init>", "(Ljavax/net/ssl/X509TrustManager;Ljava/lang/reflect/Method;)V", "okhttp"}, k = 1, mv = {1, 6, 0})
    /* renamed from: ie.b$b, reason: from toString */
    /* loaded from: classes2.dex */
    public static final /* data */ class CustomTrustRootIndex implements TrustRootIndex {

        /* renamed from: a, reason: collision with root package name and from toString */
        private final X509TrustManager trustManager;

        /* renamed from: b, reason: collision with root package name and from toString */
        private final Method findByIssuerAndSignatureMethod;

        public CustomTrustRootIndex(X509TrustManager x509TrustManager, Method method) {
            k.e(x509TrustManager, "trustManager");
            k.e(method, "findByIssuerAndSignatureMethod");
            this.trustManager = x509TrustManager;
            this.findByIssuerAndSignatureMethod = method;
        }

        @Override // le.TrustRootIndex
        public X509Certificate a(X509Certificate cert) {
            k.e(cert, "cert");
            try {
                Object invoke = this.findByIssuerAndSignatureMethod.invoke(this.trustManager, cert);
                if (invoke != null) {
                    return ((TrustAnchor) invoke).getTrustedCert();
                }
                throw new NullPointerException("null cannot be cast to non-null type java.security.cert.TrustAnchor");
            } catch (IllegalAccessException e10) {
                throw new AssertionError("unable to get issues and signature", e10);
            } catch (InvocationTargetException unused) {
                return null;
            }
        }

        public boolean equals(Object other) {
            if (this == other) {
                return true;
            }
            if (!(other instanceof CustomTrustRootIndex)) {
                return false;
            }
            CustomTrustRootIndex customTrustRootIndex = (CustomTrustRootIndex) other;
            return k.a(this.trustManager, customTrustRootIndex.trustManager) && k.a(this.findByIssuerAndSignatureMethod, customTrustRootIndex.findByIssuerAndSignatureMethod);
        }

        public int hashCode() {
            return (this.trustManager.hashCode() * 31) + this.findByIssuerAndSignatureMethod.hashCode();
        }

        public String toString() {
            return "CustomTrustRootIndex(trustManager=" + this.trustManager + ", findByIssuerAndSignatureMethod=" + this.findByIssuerAndSignatureMethod + ')';
        }
    }

    static {
        Platform.f12870a.h();
        f12844g = false;
    }

    public AndroidPlatform() {
        List o10;
        o10 = r.o(StandardAndroidSocketAdapter.a.b(StandardAndroidSocketAdapter.f13932j, null, 1, null), new DeferredSocketAdapter(AndroidSocketAdapter.f13914f.d()), new DeferredSocketAdapter(ConscryptSocketAdapter.f13928a.a()), new DeferredSocketAdapter(BouncyCastleSocketAdapter.f13922a.a()));
        ArrayList arrayList = new ArrayList();
        for (Object obj : o10) {
            if (((SocketAdapter) obj).b()) {
                arrayList.add(obj);
            }
        }
        this.f12845d = arrayList;
        this.f12846e = CloseGuard.f13924d.a();
    }

    @Override // ie.Platform
    public CertificateChainCleaner c(X509TrustManager trustManager) {
        k.e(trustManager, "trustManager");
        AndroidCertificateChainCleaner a10 = AndroidCertificateChainCleaner.f13907d.a(trustManager);
        return a10 == null ? super.c(trustManager) : a10;
    }

    @Override // ie.Platform
    public TrustRootIndex d(X509TrustManager trustManager) {
        k.e(trustManager, "trustManager");
        try {
            Method declaredMethod = trustManager.getClass().getDeclaredMethod("findTrustAnchorByIssuerAndSignature", X509Certificate.class);
            declaredMethod.setAccessible(true);
            k.d(declaredMethod, Constants.MessagerConstants.METHOD_KEY);
            return new CustomTrustRootIndex(trustManager, declaredMethod);
        } catch (NoSuchMethodException unused) {
            return super.d(trustManager);
        }
    }

    @Override // ie.Platform
    public void e(SSLSocket sSLSocket, String str, List<Protocol> list) {
        Object obj;
        k.e(sSLSocket, "sslSocket");
        k.e(list, "protocols");
        Iterator<T> it = this.f12845d.iterator();
        while (true) {
            if (!it.hasNext()) {
                obj = null;
                break;
            } else {
                obj = it.next();
                if (((SocketAdapter) obj).a(sSLSocket)) {
                    break;
                }
            }
        }
        SocketAdapter socketAdapter = (SocketAdapter) obj;
        if (socketAdapter == null) {
            return;
        }
        socketAdapter.d(sSLSocket, str, list);
    }

    @Override // ie.Platform
    public void f(Socket socket, InetSocketAddress inetSocketAddress, int i10) {
        k.e(socket, "socket");
        k.e(inetSocketAddress, "address");
        try {
            socket.connect(inetSocketAddress, i10);
        } catch (ClassCastException e10) {
            throw e10;
        }
    }

    @Override // ie.Platform
    public String g(SSLSocket sslSocket) {
        Object obj;
        k.e(sslSocket, "sslSocket");
        Iterator<T> it = this.f12845d.iterator();
        while (true) {
            if (!it.hasNext()) {
                obj = null;
                break;
            }
            obj = it.next();
            if (((SocketAdapter) obj).a(sslSocket)) {
                break;
            }
        }
        SocketAdapter socketAdapter = (SocketAdapter) obj;
        if (socketAdapter == null) {
            return null;
        }
        return socketAdapter.c(sslSocket);
    }

    @Override // ie.Platform
    public Object h(String closer) {
        k.e(closer, "closer");
        return this.f12846e.a(closer);
    }

    @Override // ie.Platform
    public boolean i(String hostname) {
        k.e(hostname, "hostname");
        return NetworkSecurityPolicy.getInstance().isCleartextTrafficPermitted(hostname);
    }

    @Override // ie.Platform
    public void l(String str, Object obj) {
        k.e(str, "message");
        if (this.f12846e.b(obj)) {
            return;
        }
        Platform.k(this, str, 5, null, 4, null);
    }
}
